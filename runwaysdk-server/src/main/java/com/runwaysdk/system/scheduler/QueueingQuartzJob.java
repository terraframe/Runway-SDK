/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
  private static HashMap<String, Queue<String>> queueMap = new HashMap<String, Queue<String>>();
  
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
  @Request
  public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException)
  {
    try
    {
      boolean didLock = lock.tryAcquire(300, TimeUnit.SECONDS);
      
      if (!didLock)
      {
        logger.error("Unable to obtain lock after waiting a decently long time. Deadlocked?");
        return;
      }
    }
    catch (InterruptedException e)
    {
      logger.error("Was interrupted unexpectedly", e);
    }
    
    ExecutableJob startExecJob = null;
    JobHistoryRecord startRecord = null;
    
    try
    {
      String historyId = (String) context.get(HISTORY_RECORD_ID);
      
      Queue<String> queue = getQueue();
      
      if (queue.peek() != null && !queue.peek().equals(historyId))
      {
        logger.error("How in the world did we [" + historyId + "] manage to execute without being in the queue? First in line = [" + String.valueOf(queue.peek()) + "]");
      }
      
      queue.poll(); // Remove us from the queue
      
      if (queue.size() > 0) // Kick off the next job in the queue (if one exists)
      {
        String nextHistoryId = queue.peek();
        
        JobHistoryRecord history = JobHistoryRecord.get(nextHistoryId);
        ExecutableJob execJob = history.getParent();
        
        startExecJob = execJob;
        startRecord = history;
      }
    }
    finally
    {
      lock.release();
    }
    
    if (startExecJob != null && startRecord != null)
    {
      startExecJob.getQuartzJob().start(startRecord);
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
    
    try
    {
      boolean didLock = lock.tryAcquire(300, TimeUnit.SECONDS);
      
      if (!didLock)
      {
        logger.error("Unable to obtain lock after waiting a decently long time. Deadlocked?");
        return true;
      }
    }
    catch (InterruptedException e)
    {
      logger.error("Was interrupted unexpectedly", e);
      return true;
    }
    
    try
    {
      Queue<String> queue = getQueue();
      
      if (queue.size() > 0 && queue.peek().equals(historyId)) // We're first in line. Allow execution and just pass through
      {
        return false;
      }
      else if (!queue.contains(historyId))
      {
        if (queue.size() > 0) // Some other job is currently running. Add us to the queue and return.
        {
          queue.add(historyId);
          
          this.handleQueued(execJobId, historyId);
          
          return true;
        }
        else // Nobody is currently running. Add us to the front of the queue and begin running.
        {
          queue.add(historyId);
          
          return false;
        }
      }
      else
      {
        return true; // If the job is already queued then just ignore the firing.
      }
    }
    finally
    {
      lock.release();
    }
  }
  
  @Override
  public void jobExecutionVetoed(JobExecutionContext context)
  {
    // Do nothing
  }
}
