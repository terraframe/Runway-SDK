/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.system.scheduler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;

/**
 * Implements support for basic queueing behavior with Quartz jobs. By default, only one QueueingQuartzJob will
 * be allowed to run (across the entire system). You may change at what level queueing will occur by overriding
 * the getQueueGroup method and returning a different group. Only one job in a particular queue group will be
 * allowed to execute at a time.
 * 
 * @author rrowlands
 */
public class QueueingQuartzJob extends QuartzRunwayJob
{
  protected static HashMap<String, Queue<String>> queueMap = new HashMap<String, Queue<String>>();
  
  private static Semaphore lock = new Semaphore(1);
  
  private Logger logger = LoggerFactory.getLogger(QueueingQuartzJob.class);
  
  private String queueGroup;
  
  public QueueingQuartzJob(ExecutableJob execJob, String queueGroup)
  {
    super(execJob);
    this.queueGroup = queueGroup;
  }
  
  public QueueingQuartzJob(ExecutableJob execJob)
  {
    super(execJob);
    this.queueGroup = QueueingQuartzJob.class.getName();
  }
  
  /**
   * This constructor is invoked directly by Quartz. Do not invoke it because the job will not be configured properly!
   */
  public QueueingQuartzJob()
  {
    super();
  }
  
  public String getQueueGroup()
  {
    return this.queueGroup;
  }
  
  private Queue<String> getQueue()
  {
      String queueGroup = this.getQueueGroup();
      
      if (queueMap.containsKey(queueGroup))
      {
        return queueMap.get(queueGroup);
      }
      else
      {
        Queue<String> queue = new LinkedList<String>();
        
        queueMap.put(queueGroup, queue);
        
        return queue;
      }
  }
  
  @Override
  protected ExecutionContext buildExecutionContextInTrans(JobExecutionContext jec)
  {
    ExecutionContext context = super.buildExecutionContextInTrans(jec);
    
    QueueingQuartzJob quartzJob = (QueueingQuartzJob) this.execJob.createQuartzRunwayJob();
    this.queueGroup = quartzJob.queueGroup;
    
    return context;
  }
  
  /*
   * The scheduler won't respect our invocation ordering unless we add it to the queue immediately.
   * 
   * (non-Javadoc)
   */
  @Override
  public void start(JobHistoryRecord record)
  {
    if (!this.queue(this.getExecutableJob().getOid(), record.getOid()))
    {
      super.start(record);
    }
  }
  
  /**
   * Invoked when this job has been queued and denied immediate execution.
   */
  @Request
  protected void handleQueued(String execJobId, String historyId)
  {
    JobHistoryRecord record = JobHistoryRecord.get(historyId);
    
    ExecutableJob execJob = ExecutableJob.get(execJobId);
    
    execJob.handleQueued(record);
  }
  
  @Override
  protected void finalizeJobExecution(JobExecutionContext context)
  {
    // This is a failsafe for the scenario where we cannot connect to the database any longer (for whatever reason). In this
    // scenario, we need to immediately remove ourselves from the queue since the 'jobWasExecuted' listener will not be invoked.
    
    finalizeJob(context);
  }
  
  @Override
  @Request
  public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException)
  {
    finalizeJob(context);
    
    super.jobWasExecuted(context, jobException);
  }
  
  /**
   * This method is a catchall for any/all listeners which may tell us that a job has finished executing. It is designed to be
   * invoked by the individual 'situation specific' listener which may be quartz or runway event specific. It is critical
   * that this method gets invoked at the end of a job's execution because otherwise it will sit in the queue and prevent
   * all other jobs from running.
   */
  @Request
  protected void finalizeJob(JobExecutionContext context)
  {
    String nextHistoryId = null;
    String historyId = (String) context.get(HISTORY_RECORD_ID);
    
    
    
    // !Important! We are now entering LOCK DEPENDENT code. If your code does not require the lock, then you should either run it
    // before or after this try/finally block, the reason being that we want to reduce as much as possible the time that we have
    // consumed this valuable lock resource. If you are, for instance, invoking some downstream listener function in this then
    // that is an error! If you're pretty much doing anything other than interacting with the queue then you're probably doing it wrong.
    if (!tryAcquireLock())
    {
      return;
    }
    
    try
    {
      Queue<String> queue = getQueue();
      
      if (queue.peek() != null && !queue.peek().equals(historyId))
      {
        // If we're not first in line it might be because a previous listener already removed us from the queue.
        return;
      }
      
      queue.poll(); // Remove us from the queue
      
      if (queue.size() > 0) // Kick off the next job in the queue (if one exists)
      {
        nextHistoryId = queue.peek();
      }
    }
    finally
    {
      lock.release();
    }
    // End lock dependent code //
    
    
    
    if (nextHistoryId != null)
    {
      JobHistoryRecord history = JobHistoryRecord.get(nextHistoryId);
      ExecutableJob execJob = history.getParent();
      
      execJob.getQuartzJob().start(history);
    }
  }
  
  @Override
  @Request
  public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context)
  {
    // This listener is always called. If we return true, then we will stop the job from being executed.
    
    String historyId = context.getTrigger().getJobDataMap().getString(HISTORY_RECORD_ID);
    String execJobId = context.getJobDetail().getJobDataMap().getString(QuartzRunwayJob.EXECUTABLE_JOB_ID);
    
    return queue(execJobId, historyId);
  }
  
  private boolean queue(String execJobId, String historyId)
  {
    if (historyId == null)
    {
      ExecutableJob execJob = ExecutableJob.get(execJobId);
      
      JobHistory history = execJob.createNewHistory();
      JobHistoryRecord record = new JobHistoryRecord(execJob, history);
      record.apply();
      
      historyId = record.getOid();
    }
    
    
    
    boolean invokeHandleQueued = false;
    boolean stopJobExecution = true;
    
    // !Important! We are now entering LOCK DEPENDENT code. If your code does not require the lock, then you should either run it
    // before or after this try/finally block, the reason being that we want to reduce as much as possible the time that we have
    // consumed this valuable lock resource. If you are, for instance, invoking some downstream listener function in this then
    // that is an error! If you're pretty much doing anything other than interacting with the queue then you're probably doing it wrong.
    if (!tryAcquireLock())
    {
      return true;
    }
    
    try
    {
      Queue<String> queue = getQueue();
      
      if (queue.size() > 0 && queue.peek().equals(historyId)) // We're first in line. Allow execution and just pass through
      {
        stopJobExecution = false;
      }
      else if (!queue.contains(historyId))
      {
        if (queue.size() > 0) // Some other job is currently running. Add us to the queue and return.
        {
          queue.add(historyId);
          
          invokeHandleQueued = true;
          
          stopJobExecution = true;
        }
        else // Nobody is currently running. Add us to the front of the queue and begin running.
        {
          queue.add(historyId);
          
          stopJobExecution = false;
        }
      }
      else
      {
        stopJobExecution = true; // If the job is already queued then just ignore the firing.
      }
    }
    finally
    {
      lock.release();
    }
    // End lock dependent code //
    
    
    
    if (invokeHandleQueued)
    {
      this.handleQueued(execJobId, historyId);
    }
    
    return stopJobExecution;
  }
  
  @Override
  public void jobExecutionVetoed(JobExecutionContext context)
  {
    // Do nothing
  }
  
  private boolean tryAcquireLock()
  {
    try
    {
      boolean didLock = lock.tryAcquire(300, TimeUnit.SECONDS);
      
      if (!didLock)
      {
        logger.error("Unable to acquire lock.");
        return false;
      }
      else
      {
        return true;
      }
    }
    catch (InterruptedException e)
    {
      // If we get an InterruptedException it likely means that the server is dying for some reason. Maybe the OS sent us kill signal because
      // it ran out of memory or something. We just want to stop running as quick as we can in this scenario.
      logger.error("Was interrupted unexpectedly", e);
      
      return false;
    }
  }
}
