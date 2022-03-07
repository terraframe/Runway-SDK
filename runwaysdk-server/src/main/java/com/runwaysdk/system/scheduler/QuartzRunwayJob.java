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
import java.util.Locale;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.RunwayException;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.rbac.SingleActorDAOIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;
import com.runwaysdk.session.SessionFacade;
import com.runwaysdk.system.SingleActor;
import com.runwaysdk.system.metadata.MdDimension;
import com.runwaysdk.transport.conversion.ConversionFacade;
import com.runwaysdk.util.IDGenerator;

/**
 * Handles the basic integration between Quartz and Runway.
 * 
 * This class is a wrapper around ExecutableJob which implements the Quartz interface and is used to
 * bootstrap our ExecutableJob Runway context from the more basic Quartz execution context.
 * 
 * @author rrowlands
 *
 */
public class QuartzRunwayJob implements org.quartz.Job, org.quartz.TriggerListener, org.quartz.JobListener
{
  public static final String HISTORY_RECORD_ID = "HISTORY_RECORD_ID";
  
  public static final String EXECUTABLE_JOB_ID = "EXECUTABLE_JOB_ID";
  
  private Logger logger = LoggerFactory.getLogger(QuartzRunwayJob.class);
  
  private JobDetail detail;
  
  private ExecutableJob execJob;
  
  protected static HashMap<String, Thread> runningThreads = new HashMap<String, Thread>();
  
  /**
   * This constructor is invoked directly by Quartz. Do not invoke it because the job will not be configured properly!
   */
  public QuartzRunwayJob()
  {
    // If you invoke the constructor you need to initialize the job afterwards. (Otherwise required stuff will null out)
  }
  
  public QuartzRunwayJob(ExecutableJob execJob)
  {
    this.initialize(execJob);
  }
  
  public ExecutableJob getExecutableJob()
  {
    return this.execJob;
  }
  
  @Request
  protected ExecutionContext buildExecutionContext(JobExecutionContext context)
  {
    return buildExecutionContextInTrans(context);
  }
  @Transaction
  protected ExecutionContext buildExecutionContextInTrans(JobExecutionContext context)
  {
    JobHistoryRecord record = null;
    JobHistory history = null;
    
    this.detail = context.getJobDetail();
    
    JobDataMap jobDataMap = this.detail.getJobDataMap();
    execJob = ExecutableJob.get(jobDataMap.getString(EXECUTABLE_JOB_ID));
    
    JobDataMap triggerDataMap = context.getTrigger().getJobDataMap();
    
    if (triggerDataMap.containsKey(HISTORY_RECORD_ID))
    {
      record = JobHistoryRecord.get(triggerDataMap.getString(HISTORY_RECORD_ID));
      history = record.getChild();
    }
    else
    {
      history = execJob.createNewHistory();
  
      record = new JobHistoryRecord(execJob, history);
      record.apply();
    }
    
    execJob.setQuartzJob(this);
    
    ExecutionContext ec = new ExecutionContext();
    ec.setJob(execJob);
    ec.setHistory(history);
    ec.setJobHistoryRecord(record);
    ec.setRunAsUser(execJob.getRunAsUser());
    ec.setRunAsDimension(execJob.getRunAsDimension());
    ec.setExecutableJobToString(execJob.toString());
    
    context.put(HISTORY_RECORD_ID, record.getOid());
    context.put(EXECUTABLE_JOB_ID, execJob.getOid());
    
    jobDataMap.put(HISTORY_RECORD_ID, record.getOid());
    triggerDataMap.put(HISTORY_RECORD_ID, record.getOid());
    
    runningThreads.put(history.getOid(), Thread.currentThread());
    
    return ec;
  }
  
  /**
   * Executes the Job within the context of Quartz.
   */
  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException
  {
    ExecutionContext executionContext = buildExecutionContext(context);
    
    Throwable error = null;
    
    // If the job wants to be run as a particular user then we need to create a session and a request for that user.
    try
    {
      if (executionContext.getRunAsUser() == null)
      {
        executeAsSystem(executionContext.getJob(), executionContext.getHistory(), executionContext.getJobHistoryRecord(), executionContext);
      }
      else
      {
        String sessionId = logIn(executionContext.getRunAsUser(), executionContext.getRunAsDimension());
        
        try
        {
          executeAsUser(sessionId, executionContext.getJob(), executionContext.getHistory(), executionContext.getJobHistoryRecord(), executionContext);
        }
        finally
        {
          logOut(sessionId);
        }
      }
    }
    catch (Throwable t)
    {
      error = t;
      
      // In the scenario where we cannot connect to the database, we want to make sure we execute this immediately before any database activity happens.
      // This is because our QuartzQueueingJob needs to remove the job from its queued hashmap otherwise jobs will be permanently queued.
      this.jobExecutionFailure(context, t);
      
      String errorMessage = RunwayException.localizeThrowable(t, CommonProperties.getDefaultLocale());
      logger.error("An error occurred while executing job " + executionContext.getExecutableJobToString() + ". " + errorMessage, t);
    }
    finally
    {
      runningThreads.remove(executionContext.getHistoryOid());
      
      execJob.writeHistory(executionContext.getHistory(), executionContext, error);
      
      execJob.executeDownstreamJobs(executionContext.getJob(), error);
    }
  }
  
  protected void jobExecutionFailure(JobExecutionContext context, Throwable t)
  {
    
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
  public void executeAsUser(String sessionId, ExecutableJob job, JobHistory history, JobHistoryRecord record, ExecutionContext executionContext) throws Throwable
  {
    executeJobWithinExistingRequest(job, history, record, executionContext);
  }
  
  @Request
  public void executeAsSystem(ExecutableJob job, JobHistory history, JobHistoryRecord record, ExecutionContext executionContext) throws Throwable
  {
    executeJobWithinExistingRequest(job, history, record, executionContext);
  }
  
  public void executeJobWithinExistingRequest(ExecutableJob job, JobHistory history, JobHistoryRecord record, ExecutionContext executionContext) throws Throwable
  {
    history.appLock();
    history.clearStatus();
    history.addStatus(AllJobStatus.RUNNING);
    history.apply();
    
    job.execute(executionContext);
  }

  /**
   * If the job has a cron expression, it will be scheduled to run in quartz.
   */
  public void schedule()
  {
    SchedulerManager.schedule(this);
  }
  
  /**
   * Schedules the job to be run immediately, regardless of any cron expression that may be configured for the job.
   */
  public void start(JobHistoryRecord record)
  {
    SchedulerManager.startJob(this, record);
  }
  
  private void initialize(ExecutableJob execJob)
  {
    this.execJob = execJob;
    
    try
    {
      JobKey jobKey = this.buildJobKey();
      
      this.detail = SchedulerManager.getJobDetail(this);
  
      if (this.detail == null)
      {
        this.detail = JobBuilder.newJob(this.getClass()).withIdentity(jobKey).build();
  
        // Give the Quartz Job a back-reference to the Runway Job
        this.detail.getJobDataMap().put(EXECUTABLE_JOB_ID, this.execJob.getOid());
      }
    }
    catch (SchedulerException e)
    {
      throw new ProgrammingErrorException(e.getLocalizedMessage(), e);
    }
  }
  
  public JobKey buildJobKey()
  {
    return JobKey.jobKey(this.getExecutableJob().getOid(), SchedulerManager.JOB_GROUP);
  }
  
  public JobDetail getJobDetail()
  {
    return this.detail;
  }
  
  public Trigger buildTrigger(JobDetail detail, String cronExpression, JobHistoryRecord record)
  {
    String id = IDGenerator.nextID();
    
    TriggerBuilder<Trigger> trigger = TriggerBuilder.newTrigger().withIdentity(id, SchedulerManager.TRIGGER_GROUP).forJob(detail);
    
    if (cronExpression != null && cronExpression.length() > 0)
    {
      trigger.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression));
    }
    else
    {
      trigger.startNow();
    }
    
    trigger.usingJobData(EXECUTABLE_JOB_ID, detail.getJobDataMap().getString(EXECUTABLE_JOB_ID));
    
    if (record != null)
    {
      trigger.usingJobData(HISTORY_RECORD_ID, record.getOid());
    }
    
    return trigger.build();
  }

  public void unschedule()
  {
    SchedulerManager.remove(this);
  }

  @Override
  public String getName()
  {
    String name = this.getClass().getName();
    
    if (this.detail != null)
    {
      name = name + " " + this.detail.getKey();
    }
    
    return name;
  }
  
  @Request
  protected void invokeAfterJobExecute(JobExecutionContext context)
  {
    try
    {
      ExecutableJob execJob = ExecutableJob.get((String) context.get(EXECUTABLE_JOB_ID));
      JobHistoryRecord history = JobHistoryRecord.get((String) context.get(HISTORY_RECORD_ID));
      
      execJob.afterJobExecute(history.getChild());
    }
    catch (Throwable t)
    {
      logger.error("Error thrown while invoking afterJobExecute.", t);
    }
  }
  
  @Request
  protected void updateHistoryFromListener(AllJobStatus newStatus, JobDataMap map)
  {
    try
    {
      updateHistoryFromListenerInTrans(newStatus, map);
    }
    catch (Throwable t)
    {
      logger.error("Error thrown while listening to job.", t);
    }
  }
  
  @Transaction
  protected void updateHistoryFromListenerInTrans(AllJobStatus newStatus, JobDataMap map)
  {
    if (map.containsKey(HISTORY_RECORD_ID))
    {
      JobHistoryRecord record = JobHistoryRecord.get(map.getString(HISTORY_RECORD_ID));
      JobHistory history = record.getChild();
      
      history.appLock();
      history.clearStatus();
      history.addStatus(newStatus);
      history.apply();
      
      System.out.println("Uploading history to [" + newStatus.getEnumName() + "]");
    }
    else
    {
      // This happens normally when a job is scheduled to run (not run via invoking start)
    }
  }
  
  @Request
  protected void handleTriggerMisfire(Trigger trigger)
  {
    JobDataMap triggerDataMap = trigger.getJobDataMap();
    
    if (triggerDataMap.containsKey(HISTORY_RECORD_ID) && triggerDataMap.containsKey(EXECUTABLE_JOB_ID))
    {
      JobHistoryRecord record = JobHistoryRecord.get(triggerDataMap.getString(HISTORY_RECORD_ID));
      
      ExecutableJob execJob = ExecutableJob.get(triggerDataMap.getString(EXECUTABLE_JOB_ID));
      
      execJob.handleSchedulerMisfire(record);
    }
    else
    {
      // A trigger may or may not have a history record associated with it.
    }
  }

  @Override
  public void triggerFired(Trigger trigger, JobExecutionContext context)
  {
    // Do nothing.
  }

  @Override
  public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context)
  {
    // This listener is always called. If we return true, then we will stop the job from being executed. 
    return false;
  }

  @Override
  public void triggerMisfired(Trigger trigger)
  {
    this.handleTriggerMisfire(trigger);
  }

  @Override
  public void triggerComplete(Trigger trigger, JobExecutionContext context, CompletedExecutionInstruction triggerInstructionCode)
  {
    this.invokeAfterJobExecute(context);
  }

  @Override
  public void jobToBeExecuted(JobExecutionContext context)
  {
    // It's better to do this from the execute method because we don't have a history object yet if we're run as part of a cron schedule
//    updateHistoryFromListener(AllJobStatus.RUNNING, context.getTrigger().getJobDataMap());
  }

  @Override
  public void jobExecutionVetoed(JobExecutionContext context)
  {
    updateHistoryFromListener(AllJobStatus.CANCELED, context.getTrigger().getJobDataMap());
  }

  @Override
  public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException)
  {
    // triggerComplete is the last listener to be invoked so we'll do it there instead.
//    this.invokeAfterJobExecute(context);
  }
  
  @Override
  public boolean equals(Object obj)
  {
      if (this == obj)
          return true;
      if (obj == null)
          return false;
      if (getClass() != obj.getClass())
          return false;
      
      JobKey myKey = this.buildJobKey();
      JobKey hisKey = ( (QuartzRunwayJob) obj ).buildJobKey();
      
      return myKey.equals(hisKey);
  }

}