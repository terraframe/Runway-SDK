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
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.SmartException;
import com.runwaysdk.business.rbac.SingleActorDAOIF;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;
import com.runwaysdk.session.SessionFacade;
import com.runwaysdk.system.SingleActor;
import com.runwaysdk.system.metadata.MdDimension;
import com.runwaysdk.transport.conversion.ConversionFacade;

public abstract class ExecutableJob extends ExecutableJobBase implements org.quartz.Job, ExecutableJobIF
{
  private static final long        serialVersionUID = 328266996;

  private Map<String, JobListener> listeners;

  public static final String       JOB_ID_PREPEND   = "_JOB_";

  private final static Logger              logger           = LoggerFactory.getLogger(ExecutableJob.class);

  public ExecutableJob()
  {
    super();
    
    this.listeners = new LinkedHashMap<String, JobListener>();
  }

  /**
   * 
   * @param jobListener
   */
  public void addJobListener(JobListener jobListener)
  {
    this.listeners.put(jobListener.getName(), jobListener);
  }

  /**
   * 
   * @return
   */
  public Map<String, JobListener> getJobListeners()
  {
    return this.listeners;
  }
  
  public List<AllJobOperation> getSupportedOperations()
  {
    ArrayList<AllJobOperation> ops = new ArrayList<AllJobOperation>();
    
    ops.add(AllJobOperation.START);
//    ops.add(AllJobOperation.CANCEL);
    
    return ops;
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
    history.addStatus(AllJobStatus.RUNNING);
    history.apply();
    
    return history;
  }
  
  @Request
  private Object[] buildExecutionPrereqs(JobExecutionContext context)
  {
    JobHistoryRecord record;
    ExecutableJob job;
    JobHistory history;
    
    String oid = context.getJobDetail().getKey().getName();
    if (oid.startsWith(JOB_ID_PREPEND))
    {
      oid = oid.replaceFirst(JOB_ID_PREPEND, "");

      job = ExecutableJob.get(oid);

      history = createNewHistory();

      record = new JobHistoryRecord(job, history);
      record.apply();
    }
    else
    { 
      record = JobHistoryRecord.get(oid);
      job = record.getParent();
      history = record.getChild();
    }
    
    return new Object[]{job, history, record, job.getRunAsUser(), job.getRunAsDimension()};
  }
  
  /**
   * Executes the Job within the context of Quartz.
   */
  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException
  {
    Object[] prereqs = buildExecutionPrereqs(context);
    ExecutableJob job = (ExecutableJob) prereqs[0];
    JobHistory history = (JobHistory) prereqs[1];
    JobHistoryRecord record = (JobHistoryRecord) prereqs[2];
    SingleActor user = (SingleActor) prereqs[3];
    MdDimension dimension = (MdDimension) prereqs[4];
    
    // If the job wants to be run as a particular user then we need to create a session and a request for that user.
    if (user == null)
    {
      executeAsSystem(job, history, record);
    }
    else
    {
      String sessionId = logIn(user, dimension);
      
      try
      {
        executeAsUser(sessionId, job, history, record);
      }
      catch (Throwable t)
      {
        logger.error("An error occurred while executing job " + job.toString() + ".", t);
        throw t;
      }
      finally
      {
        logOut(sessionId);
      }
    }
  }
  
  @Request
  private void logOut(String sessionId)
  {
    SessionFacade.closeSession(sessionId);
  }
  
  @Request
  private String logIn(SingleActor user, MdDimension dimension)
  {
    SingleActorDAOIF userDAO = (SingleActorDAOIF) BusinessFacade.getEntityDAO(user);
    
    if (dimension == null)
    {
      return SessionFacade.logIn(userDAO, new Locale[]{ConversionFacade.getLocale(userDAO.getLocale())});
    }
    else
    {
      return SessionFacade.logIn(userDAO, dimension.getKey(), new Locale[]{ConversionFacade.getLocale(userDAO.getLocale())});
    }
  }
  
  @Request(RequestType.SESSION)
  public void executeAsUser(String sessionId, ExecutableJob job, JobHistory history, JobHistoryRecord record) throws JobExecutionException
  {
    executeJobWithinExistingRequest(job, history, record);
  }
  
  @Request
  public void executeAsSystem(ExecutableJob job, JobHistory history, JobHistoryRecord record) throws JobExecutionException
  {
    executeJobWithinExistingRequest(job, history, record);
  }
  
  public void executeJobWithinExistingRequest(ExecutableJob job, JobHistory history, JobHistoryRecord record)
  {
    // Execute the job
    
    ExecutionContext executionContext = ExecutionContext.factory(ExecutionContext.Context.EXECUTION, job, history);

    String errorMessage = null;

    try
    {
      job.execute(executionContext);
    }
    catch (Throwable t)
    {
      logger.error("An error occurred while executing job " + job.toString() + ".", t);
      errorMessage = getMessageFromException(t);
    }
    
    // Configure the history

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
    
    
    // Invoke Downstream jobs
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
      
      errorMessage = se.getLocalizedMessage();
      
      if (errorMessage == null)
      {
        errorMessage = se.getClassDisplayLabel();
      }
      if (errorMessage == null)
      {
        errorMessage = se.getType();
      }
    }
    else
    {
      errorMessage = t.getLocalizedMessage();
      
      if (errorMessage == null)
      {
        errorMessage = t.getMessage();
      }
    }
    
    return errorMessage;
  }

  /**
   * Defines what the job should actually do when executed (or started). Must be overridden with actual behavior.
   */
  @Override
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
    for (JobListener jobListener : this.listeners.values())
    {
      SchedulerManager.addJobListener(this, jobListener);
    }

    JobHistory jh = createNewHistory();

    JobHistoryRecord rec = new JobHistoryRecord(this, jh);
    rec.apply();

    SchedulerManager.schedule(this, rec.getOid());

    return jh;
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
  public void resume()
  {
    // TODO : Not supported yet.
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
        SchedulerManager.schedule(this, JOB_ID_PREPEND + this.getOid(), this.getCronExpression());
      }
      else
      {
        SchedulerManager.remove(this, JOB_ID_PREPEND + this.getOid());
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
    // Remove all scheduled jobs
    SchedulerManager.remove(this, JOB_ID_PREPEND + this.getOid());

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
