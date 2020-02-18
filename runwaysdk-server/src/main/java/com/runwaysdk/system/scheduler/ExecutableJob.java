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

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.LocalizationFacade;
import com.runwaysdk.business.SmartException;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;

public abstract class ExecutableJob extends ExecutableJobBase
{
  private static final long        serialVersionUID = 328266996;

  private final static Logger              logger           = LoggerFactory.getLogger(ExecutableJob.class);

  private QuartzRunwayJob quartzJob;
  
  public ExecutableJob()
  {
    super();
  }
  
  public QuartzRunwayJob getQuartzJob()
  {
    if (quartzJob != null)
    {
      return quartzJob;
    }
    else
    {
      return new QuartzRunwayJob(this);
    }
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
  protected void executeDownstreamJobs(ExecutableJob job, String errorMessage) {
    List<? extends DownstreamJobRelationship> lDownstreamRel = job.getAlldownstreamJobRel().getAll();
    if (lDownstreamRel.size() > 0)
    {
      DownstreamJobRelationship rel = lDownstreamRel.get(0);
      ExecutableJob downstream = rel.getChild();
      
      if ( (errorMessage == null) || (errorMessage != null && rel.getTriggerOnFailure()) )
      {
        // TODO : This is kind of a hack because directly invoking start() here will cause a NullPointerException in the @Authenticate (in ReportJob.start)
        downstream.executableJobStart();
      }
    }
  }

  @Request
  protected void writeHistory(JobHistory history, ExecutionContext executionContext, String errorMessage)
  {
    writeHistoryInTrans(history, executionContext, errorMessage);
  }
  @Transaction
  protected void writeHistoryInTrans(JobHistory history, ExecutionContext executionContext,
      String errorMessage)
  {
    JobHistory jh = JobHistory.get(history.getOid());

    jh.appLock();
    jh.setEndTime(new Date());
    jh.clearStatus();
    if (errorMessage != null)
    {
      jh.getHistoryInformation().setValue(errorMessage);
      jh.addStatus(AllJobStatus.FAILURE);
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
  }
  
  public static String getMessageFromException(Throwable t)
  {
    String errorMessage = null;
    
    if (t instanceof InvocationTargetException)
    {
      t = t.getCause();
    }
    
    // TODO : If this is a Runway exception then the localized exception is only available at the DTO layer (for good reason). We should be sending the exception type
    // to the client, having them instantiate it, and then returning the localized value from that dto. Instead, we'll just do something dumb in the meantime here.
    if (t instanceof SmartException)
    {
      SmartException se = ( (SmartException) t );
      
      errorMessage = se.localize(com.runwaysdk.session.Session.getCurrentLocale());
      
      if (errorMessage == null || errorMessage.length() == 0)
      {
        errorMessage = se.getLocalizedMessage();
      }
      if (errorMessage == null || errorMessage.length() == 0)
      {
        errorMessage = se.getClassDisplayLabel();
      }
      if (errorMessage == null || errorMessage.length() == 0)
      {
        errorMessage = se.getType();
      }
    }
    else
    {
      errorMessage = t.getLocalizedMessage();
      
      if (errorMessage == null || errorMessage.length() == 0)
      {
        errorMessage = t.getMessage();
      }
    }
    
    if (errorMessage == null || errorMessage.length() == 0)
    {
      errorMessage = t.getClass().getTypeName();
    }
    
    return errorMessage;
  }

  /**
   * Defines what the job should actually do when executed (or started). Must be overridden with actual behavior.
   */
  abstract public void execute(ExecutionContext executionContext);

  public String getLocalizedDescription()
  {
    return this.getDescription().getValue();
  }

  public synchronized JobHistory start()
  {
    return executableJobStart();
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

  /**
   * If the job can be automatically resumed, it will be started. Otherwise, the history will be set to FAILURE. 
   */
  @Request
  public synchronized void resume(JobHistoryRecord jhr)
  {
    if (canResume())
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
      
      // TODO : Maybe read the locale from the job owner's locale
      String msg = LocalizationFacade.getMessage(CommonProperties.getDefaultLocale(), "SchedulerJobCannotResume", "The server was shutdown while the job was running.");
      jh.getHistoryInformation().setValue(msg);
      
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
    
    // TODO : Maybe read the locale from the job owner's locale
    String msg = LocalizationFacade.getMessage(CommonProperties.getDefaultLocale(), "SchedulerJobMisfire", "The job could not be scheduled as requested, the system is out of resources (scheduler misfire).");
    jh.getHistoryInformation().setValue(msg);
    
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
  
  public boolean canResume()
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

    if (oid != null && desc != null && oid == desc)
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
