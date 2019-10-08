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

import java.util.Locale;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.rbac.SingleActorDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;
import com.runwaysdk.session.SessionFacade;
import com.runwaysdk.system.SingleActor;
import com.runwaysdk.system.metadata.MdDimension;
import com.runwaysdk.transport.conversion.ConversionFacade;

/**
 * Handles the basic integration between Quartz and Runway.
 * 
 * This class is a wrapper around ExecutableJob which implements the Quartz interface and is used to
 * bootstrap our ExecutableJob Runway context from the more basic Quartz execution context.
 * 
 * @author rrowlands
 *
 */
public class QuartzRunwayJob implements org.quartz.Job
{
  public static final String HISTORY_RECORD_ID = "HISTORY_RECORD_ID";
  
  public static final String EXECUTABLE_JOB_ID = "EXECUTABLE_JOB_ID";
  
  private Logger logger = LoggerFactory.getLogger(QuartzRunwayJob.class);
  
  private JobDetail detail;
  
  private ExecutableJob execJob;
  
  public QuartzRunwayJob()
  {
    
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
  private Object[] buildExecutionPrereqs(JobExecutionContext context)
  {
    return buildExecutionPrereqsInTrans(context);
  }
  @Transaction
  private Object[] buildExecutionPrereqsInTrans(JobExecutionContext context)
  {
    JobHistoryRecord record;
    JobHistory history;
    
    this.detail = context.getJobDetail();
    JobDataMap dataMap = this.detail.getJobDataMap();
    
    execJob = ExecutableJob.get(dataMap.getString(EXECUTABLE_JOB_ID));

    if (dataMap.containsKey(HISTORY_RECORD_ID))
    {
      record = JobHistoryRecord.get(dataMap.getString(HISTORY_RECORD_ID));
      history = record.getChild();
    }
    else
    {
      history = execJob.createNewHistory();
  
      record = new JobHistoryRecord(execJob, history);
      record.apply();
    }
    
    execJob.setQuartzJob(this);
    
    ExecutionContext executionContext = ExecutionContext.factory(ExecutionContext.Context.EXECUTION, execJob, history);
    
    return new Object[]{execJob, history, record, execJob.getRunAsUser(), execJob.getRunAsDimension(), executionContext, execJob.toString()};
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
    ExecutionContext executionContext = (ExecutionContext) prereqs[5];
    String jobts = (String) prereqs[6];
    
    String errorMessage = null;
    
    // If the job wants to be run as a particular user then we need to create a session and a request for that user.
    try
    {
      if (user == null)
      {
        errorMessage = executeAsSystem(job, history, record, executionContext);
      }
      else
      {
        String sessionId = logIn(user, dimension);
        
        try
        {
          errorMessage = executeAsUser(sessionId, job, history, record, executionContext);
        }
        finally
        {
          logOut(sessionId);
        }
      }
    }
    catch (Throwable t)
    {
      logger.error("An error occurred while executing job " + jobts + ".", t);
      errorMessage = ExecutableJob.getMessageFromException(t);
    }
    finally
    {
      execJob.writeHistory(history, executionContext, errorMessage);
      
      execJob.executeDownstreamJobs(job, errorMessage);
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
  public String executeAsUser(String sessionId, ExecutableJob job, JobHistory history, JobHistoryRecord record, ExecutionContext executionContext) throws JobExecutionException
  {
    return executeJobWithinExistingRequest(job, history, record, executionContext);
  }
  
  @Request
  public String executeAsSystem(ExecutableJob job, JobHistory history, JobHistoryRecord record, ExecutionContext executionContext) throws JobExecutionException
  {
    return executeJobWithinExistingRequest(job, history, record, executionContext);
  }
  
  public String executeJobWithinExistingRequest(ExecutableJob job, JobHistory history, JobHistoryRecord record, ExecutionContext executionContext)
  {
    String errorMessage = null;

    try
    {
      job.execute(executionContext);
    }
    catch (Throwable t)
    {
      logger.error("An error occurred while executing job " + job.toString() + ".", t);
      errorMessage = ExecutableJob.getMessageFromException(t);
    }
    
    return errorMessage;
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
    this.detail.getJobDataMap().put(HISTORY_RECORD_ID, record.getOid());
    
    SchedulerManager.startJob(this);
  }
  
  private void initialize(ExecutableJob execJob)
  {
    this.execJob = execJob;
    
    try
    {
      String id = this.execJob.getOid();
      
      this.detail = SchedulerManager.getJobDetail(id);
  
      if (this.detail == null)
      {
        this.detail = JobBuilder.newJob(this.getClass()).withIdentity(id).build();
  
        // Give the Quartz Job a back-reference to the Runway Job
        this.detail.getJobDataMap().put(EXECUTABLE_JOB_ID, id);
      }
    }
    catch (SchedulerException e)
    {
      throw new ProgrammingErrorException(e.getLocalizedMessage(), e);
    }
  }
  
  public synchronized JobDetail getJobDetail() throws SchedulerException
  {
    return this.detail;
  }

  public void unschedule()
  {
    SchedulerManager.remove(this);
  }

}