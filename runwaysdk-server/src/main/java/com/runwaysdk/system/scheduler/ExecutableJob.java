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

import java.util.Date;
import java.util.List;

import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;

public abstract class ExecutableJob extends ExecutableJobBase
{
  private static final long        serialVersionUID = 328266996;

  private QuartzRunwayJob quartzJob;
  
  public ExecutableJob()
  {
    super();
  }
  
  public void afterJobExecute(JobHistory history)
  {
    // Do nothing. This is mostly useful for subclasses.
  }
  
  public QuartzRunwayJob getQuartzJob()
  {
    if (quartzJob != null)
    {
      return quartzJob;
    }
    else
    {
      return this.createQuartzRunwayJob();
    }
  }
  
  /**
   * Override this method to provide your own Quartz job (such as QueueingQuartzJob).
   * 
   * @return
   */
  protected QuartzRunwayJob createQuartzRunwayJob()
  {
    return new QuartzRunwayJob(this);
  }
  
  public void setQuartzJob(QuartzRunwayJob quartzJob)
  {
    this.quartzJob = quartzJob;
  }
  
  /**
   * Creates, configures and applies a new JobHistory that will be used to record history for the current job execution context.
   *   The beauty of this is that it can be overridden by subclasses if you want to extend JobHistory to record additional stuff.
   * 
   * @return A new, configured, applied instance of JobHistory
   */
  protected JobHistory createNewHistory()
  {
    JobHistory history = new JobHistory();
    history.setStartTime(new Date());
    history.addStatus(AllJobStatus.NEW);
    history.apply();
    
    return history;
  }
  
  @Request
  protected void executeDownstreamJobs(ExecutableJob job, Throwable error) {
    List<? extends DownstreamJobRelationship> lDownstreamRel = job.getAlldownstreamJobRel().getAll();
    if (lDownstreamRel.size() > 0)
    {
      DownstreamJobRelationship rel = lDownstreamRel.get(0);
      ExecutableJob downstream = rel.getChild();
      
      if ( (error == null) || (rel.getTriggerOnFailure()) )
      {
        downstream.executableJobStart();
      }
    }
  }

  @Request
  protected AllJobStatus writeHistory(JobHistory history, ExecutionContext executionContext, Throwable error)
  {
    return writeHistoryInTrans(history, executionContext, error);
  }
  @Transaction
  protected AllJobStatus writeHistoryInTrans(JobHistory history, ExecutionContext executionContext, Throwable error)
  {
    JobHistory jh = JobHistory.lock(history.getOid());

    jh.setEndTime(new Date());
    jh.clearStatus();
    
    if (error != null)
    {
      jh.addStatus(AllJobStatus.FAILURE);
      
      jh.setError(error);
    }
    else
    {
      if(executionContext.getStatus() != null)
      {
        jh.addStatus(executionContext.getStatus());
      }
      else
      {
        jh.addStatus(AllJobStatus.SUCCESS);
      }
    }
    jh.apply();
    
    return jh.getStatus().get(0);
  }

  /**
   * This method should only be invoked via Quartz. Do not invoke this method directly.
   * 
   * Subclasses should override this method to provide execution logic.
   * 
   * @throws Throwable 
   */
  abstract public void execute(ExecutionContext executionContext) throws Throwable;

  public String getLocalizedDescription()
  {
    return this.getDescription().getValue();
  }

  /**
   * Schedules the job for immediate running. The job will be invoked as part of a separate
   * worker thread when the system has availability, and may be queued if the system does not
   * have availability.
   */
  public synchronized JobHistory start()
  {
    return executableJobStart();
  }
  
  /**
   * Executes the job immediately, on the current thread. Any sort of queueing logic will be ignored.
   * 
   * @param job
   * @return
   * @throws Throwable
   */
  public ExecutionContext startSynchronously() throws Throwable
  {
    JobHistory history = this.createNewHistory();
    
    return this.startSynchronously(history);
  }
  
  /**
   * Executes the job immediately, on the current thread. Any sort of queueing logic will be ignored.
   * 
   * @param job
   * @return
   * @throws Throwable
   */
  public ExecutionContext startSynchronously(JobHistory history) throws Throwable
  {
    ExecutionContext context = new ExecutionContext();
    
    history.appLock();
    history.clearStatus();
    history.addStatus(AllJobStatus.RUNNING);
    history.apply();
    
    JobHistoryRecord record = new JobHistoryRecord(this, history);
    record.apply();
    
    context.setHistory(history);
    context.setJob(this);
    context.setHistory(history);
    context.setJobHistoryRecord(record);
    context.setRunAsUser(this.getRunAsUser());
    context.setRunAsDimension(this.getRunAsDimension());
    context.setExecutableJobToString(this.toString());
    
    Throwable error = null;
    
    try
    {
      this.execute(context);
    }
    catch (Throwable t)
    {
      error = t;
      
      throw t;
    }
    finally
    {
      JobHistory jh = JobHistory.get(history.getOid());

      jh.appLock();
      jh.setEndTime(new Date());
      jh.clearStatus();
      
      if (error != null)
      {
        jh.addStatus(AllJobStatus.FAILURE);
        
        jh.setError(error);
      }
      else
      {
        if(context.getStatus() != null)
        {
          jh.addStatus(context.getStatus());
        }
        else
        {
          jh.addStatus(AllJobStatus.SUCCESS);
        }
      }
      jh.apply();
      
      context.setStatus(jh.getStatus().get(0));
      context.setHistory(jh);
    }
    
    return context;
  }
  
  private JobHistory executableJobStart()
  {
    JobHistory history = this.createNewHistory();
    
    JobHistoryRecord record = new JobHistoryRecord(this, history);
    record.apply();

    this.getQuartzJob().start(record);

    return history;
  }

  @Request
  public void stop()
  {
    // TODO : Not supported yet.
  }

  @Request
  public void pause()
  {
    // TODO : Not supported yet.
  }
  
  @Request
  public synchronized void requeue(JobHistoryRecord jhr)
  {
    this.getQuartzJob().start(jhr);
  }

  /**
   * If the job can be automatically resumed, it will be started. Otherwise, the history will be set to FAILURE. 
   */
  @Request
  public synchronized void resume(JobHistoryRecord jhr)
  {
    if (canResume(jhr))
    {
      this.getQuartzJob().start(jhr);
    }
    else
    {
      JobHistory jh = jhr.getChild();
      
      jh.appLock();
      jh.clearStatus();
      jh.addStatus(AllJobStatus.FAILURE);
      
      jh.setEndTime(new Date());
      
      jh.setError(new SchedulerJobCannotResumeException());
      
      jh.apply();
    }
  }
  
  @Request
  protected void handleSchedulerMisfire(JobHistoryRecord record)
  {
    JobHistory jh = record.getChild();
    
    jh.appLock();
    jh.clearStatus();
    jh.addStatus(AllJobStatus.FAILURE);
    
    jh.setEndTime(new Date());
    
    jh.setError(new SchedulerMisfireException());
    
    jh.apply();
  }
  
  @Request
  protected void handleQueued(JobHistoryRecord record)
  {
    JobHistory jh = record.getChild();
    
    jh.appLock();
    jh.clearStatus();
    jh.addStatus(AllJobStatus.QUEUED);
    jh.apply();
  }
  
  public boolean canResume(JobHistoryRecord record)
  {
    return false;
  }

  @Request
  public void cancel()
  {
    // TODO : Not supported yet.
  }

  /**
   * 
   */
  @Override
  public void apply()
  {
    super.apply();

    if (this.isModified(CRONEXPRESSION))
    {
      if (this.getCronExpression() != null && this.getCronExpression().length() > 0)
      {
        this.getQuartzJob().schedule();
      }
      else
      {
        this.getQuartzJob().unschedule();
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.business.Element#delete()
   */
  @Override
  public void delete()
  {
    this.getQuartzJob().unschedule();

    super.delete();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.system.scheduler.JobBase#toString()
   */
  @Override
  public String toString()
  {
    String clazz = this.getClassDisplayLabel();
    String oid = this.getDisplayLabel().getValue();
    String desc = this.getDescription().getValue();

    if (oid != null && desc != null && oid.equals(desc))
    {
      return "[" + clazz + "] - " + desc;
    }
    else if (oid != null && desc != null)
    {
      return "[" + clazz + "] - " + desc + " (" + oid + ")";
    }
    else
    {
      return "[" + clazz + "] - " + this.getOid();
    }
  }
}
