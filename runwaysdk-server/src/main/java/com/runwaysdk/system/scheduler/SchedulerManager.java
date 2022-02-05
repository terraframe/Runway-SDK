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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerConfigException;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerListener;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;

public class SchedulerManager implements JobListener, TriggerListener
{

  /**
   * The SchedulerFactory instance to manage all Schedulers.
   */
  private SchedulerFactory factory;

  /**
   * The default Scheduler instance (right now only one is supported).
   */
  private Scheduler        scheduler;

  private static SchedulerManager instance = null;
  
  public static final String TRIGGER_GROUP = SchedulerManager.class.getName();
  
  public static final String JOB_GROUP = SchedulerManager.class.getName();

  private SchedulerManager()
  {
  }
  
  private static synchronized SchedulerManager getInstance()
  {
    if (instance == null)
    {
      instance = new SchedulerManager();
      
      instance.initialize();
    }
    
    return instance;
  }
  
  private static Scheduler scheduler()
  {
    return SchedulerManager.getInstance().scheduler;
  }
  
  private void initialize()
  {
    factory = new StdSchedulerFactory();
    
    try
    {
      scheduler = factory.getScheduler();
      
      scheduler().getListenerManager().addJobListener(this, GroupMatcher.jobGroupEquals(JOB_GROUP));
      
      scheduler().getListenerManager().addTriggerListener(this, GroupMatcher.triggerGroupEquals(TRIGGER_GROUP));
    }
    catch (SchedulerException e)
    {
      Throwable cause = e.getUnderlyingException();

      if (cause instanceof SchedulerConfigException)
      {
        SchedulerConfigException sceOriginal = (SchedulerConfigException) cause;

        ProgrammingErrorException sceWrapper = new ProgrammingErrorException(sceOriginal.getLocalizedMessage(), sceOriginal);

        throw sceWrapper;
      }
      else
      {
        throw new ProgrammingErrorException("Error occurred when initializing the Scheduler Manager.", e);
      }
    }
  }
  
  @Request
  private void resumeRunningJobs()
  {
    resumeRunningJobsInTrans();
  }
  
  @Transaction
  private void resumeRunningJobsInTrans()
  {
    Iterator<JobHistoryRecord> it = getResumeJobs().iterator();
    
    while (it.hasNext())
    {
      JobHistoryRecord jhr = it.next();
      ExecutableJob ej = jhr.getParent();
      JobHistory history = jhr.getChild();
      
      if (history.getStatus().contains(AllJobStatus.QUEUED))
      {
        ej.requeue(jhr);
      }
      else
      {
        ej.resume(jhr);
      }
    }
  }
  
  public static JobDetail getJobDetail(QuartzRunwayJob quartzJob) throws SchedulerException
  {
    if (!initialized())
    {
      return null;
    }
	  
    return scheduler().getJobDetail(quartzJob.buildJobKey());
  }
  
  /**
   * Starts the Scheduler
   */
  @Request
  public synchronized static void start()
  {
    try
    {
      scheduler().start();
    }
    catch (SchedulerException e)
    {
      throw new ProgrammingErrorException(e.getLocalizedMessage(), e);
    }
    
    instance.resumeRunningJobs();

    /*
     * Schedule all of the jobs which have cron expressions
     */
    OIterator<? extends ExecutableJob> iterator = null;

    ExecutableJobQuery query = new ExecutableJobQuery(new QueryFactory());

    try
    {
      iterator = query.getIterator();

      while (iterator.hasNext())
      {
        ExecutableJob job = iterator.next();

        if (job.getCronExpression() != null && job.getCronExpression().length() > 0)
        {
          job.getQuartzJob().schedule();
        }
      }
    }
    finally
    {
      if (iterator != null)
      {
        iterator.close();
      }
    }
  }

  public static List<JobHistoryRecord> interruptAllRunningJobs()
  {
    List<JobHistoryRecord> stoppedJobs = new ArrayList<JobHistoryRecord>();
    List<JobHistoryRecord> jobs = SchedulerManager.getRunningJobs();
    
    for (JobHistoryRecord job : jobs)
    {
      String oid = job.getChildOid();
      
      if (QuartzRunwayJob.runningThreads.containsKey(oid))
      {
        QuartzRunwayJob.runningThreads.get(oid).interrupt();
        stoppedJobs.add(job);
      }
    }
    
    return stoppedJobs;
  }
  
  public static List<JobHistoryRecord> getResumeJobs()
  {
    List<JobHistoryRecord> records = new ArrayList<JobHistoryRecord>();

    QueryFactory qf = new QueryFactory();
    ExecutableJobQuery ejq = new ExecutableJobQuery(qf);
    JobHistoryQuery jhq = new JobHistoryQuery(qf);
    JobHistoryRecordQuery jhrq = new JobHistoryRecordQuery(qf);

    jhq.WHERE(jhq.getStatus().containsExactly(AllJobStatus.RUNNING).OR(jhq.getStatus().containsExactly(AllJobStatus.QUEUED)));
    ejq.WHERE(ejq.getOid().EQ(jhrq.parentOid()));
    jhrq.WHERE(jhrq.hasChild(jhq));

    OIterator<? extends JobHistoryRecord> it = jhrq.getIterator();

    try
    {
      while (it.hasNext())
      {
        records.add(it.next());
      }
    }
    finally
    {
      it.close();
    }

    return records;
  }
  
  public static List<JobHistoryRecord> getRunningJobs()
  {
    List<JobHistoryRecord> records = new ArrayList<JobHistoryRecord>();

    QueryFactory qf = new QueryFactory();
    ExecutableJobQuery ejq = new ExecutableJobQuery(qf);
    JobHistoryQuery jhq = new JobHistoryQuery(qf);
    JobHistoryRecordQuery jhrq = new JobHistoryRecordQuery(qf);

    jhq.WHERE(jhq.getStatus().containsExactly(AllJobStatus.RUNNING));
    ejq.WHERE(ejq.getOid().EQ(jhrq.parentOid()));
    jhrq.WHERE(jhrq.hasChild(jhq));

    OIterator<? extends JobHistoryRecord> it = jhrq.getIterator();

    try
    {
      while (it.hasNext())
      {
        records.add(it.next());
      }
    }
    finally
    {
      it.close();
    }

    return records;
  }
  
  /**
   * Schedules the job to be run immediately, regardless of any cron expression that may be configured for the job.
   */
  public static synchronized void startJob(QuartzRunwayJob quartzJob, JobHistoryRecord record)
  {
    try
    {
      if (!scheduler().isStarted())
      {
        throw new ProgrammingErrorException("The scheduler has not been started!");
      }
      
      JobDetail detail = quartzJob.getJobDetail();
      
      Trigger trigger = quartzJob.buildTrigger(detail, null, record);
      
      schedule(detail, trigger);
    }
    catch (SchedulerException e)
    {
      throw new ProgrammingErrorException(e.getLocalizedMessage(), e);
    }
  }

  /**
   * If the job has a cron expression, it will be scheduled to run in quartz.
   */
  public synchronized static void schedule(QuartzRunwayJob quartzJob)
  {
    try
    {
      JobDetail detail = quartzJob.getJobDetail();
      
      String cron = quartzJob.getExecutableJob().getCronExpression();

      if (cron.length() > 0)
      {
        SchedulerManager.removeTriggers(quartzJob);

        Trigger trigger = quartzJob.buildTrigger(detail, cron, null);

        schedule(detail, trigger);
      }
//      else
//      {
//        Trigger trigger = buildTrigger(detail, null);
//
//        schedule(detail, trigger);
//      }

    }
    catch (SchedulerException e)
    {
      throw new ProgrammingErrorException(e.getLocalizedMessage(), e);
    }
  }

  /**
   * @param job
   * @throws SchedulerException
   */
  public static void removeTriggers(QuartzRunwayJob quartzJob) throws SchedulerException
  {
    if (!initialized())
    {
      return;
    }
    
    JobKey key = quartzJob.buildJobKey();

    // Remove any existing triggers
    List<? extends Trigger> triggers = scheduler().getTriggersOfJob(key);

    for (Trigger trigger : triggers)
    {
      scheduler().unscheduleJob(trigger.getKey());
    }
  }

  /**
   * @param detail
   * @param trigger
   * @throws SchedulerException
   */
  private static void schedule(JobDetail detail, Trigger trigger) throws SchedulerException
  {
    if (!initialized())
    {
      throw new ProgrammingErrorException("The scheduler has not been started!");
    }
    
    if (scheduler().checkExists(detail.getKey()))
    {
      scheduler().scheduleJob(trigger);
    }
    else
    {
      scheduler().scheduleJob(detail, trigger);
    }
  }

  public synchronized static void remove(QuartzRunwayJob job)
  {
	if (!initialized())
	{
	  return;
	}
	
    try
    {
      JobKey key = job.buildJobKey();

      if (scheduler().checkExists(key))
      {
        scheduler().deleteJob(key);
      }
      else
      {
//        logger.error("Attempt to remove job which does not exist");
      }
    }
    catch (SchedulerException e)
    {
      throw new ProgrammingErrorException(e.getLocalizedMessage(), e);
    }
  }

  public synchronized static void standby()
  {
    if (!initialized())
    {
      throw new ProgrammingErrorException("The scheduler has not been started!");
    }
    
    try
    {
      scheduler().standby();
    }
    catch (SchedulerException e)
    {
      throw new ProgrammingErrorException(e.getLocalizedMessage(), e);
    }
  }

  public static synchronized boolean initialized()
  {
    try {
		return instance != null && scheduler().isStarted();
	} catch (SchedulerException e) {
		throw new ProgrammingErrorException(e);
	}
  }

  public synchronized static void shutdown()
  {
    try
    {
      if (initialized())
      {
        scheduler().shutdown();
      }
    }
    catch (SchedulerException e)
    {
      throw new ProgrammingErrorException(e.getLocalizedMessage(), e);
    }
  }
  
  @Request
  protected QuartzRunwayJob getQuartzRunwayJob(JobDataMap map)
  {
    if (map.containsKey(QuartzRunwayJob.EXECUTABLE_JOB_ID))
    {
      ExecutableJob execJob = ExecutableJob.get(map.getString(QuartzRunwayJob.EXECUTABLE_JOB_ID));
      
      return execJob.getQuartzJob();
    }
    else
    {
      return null;
    }
  }

  @Override
  public String getName()
  {
    return this.getClass().getName();
  }

  @Override
  public void jobToBeExecuted(JobExecutionContext context)
  {
    QuartzRunwayJob job = this.getQuartzRunwayJob(context.getTrigger().getJobDataMap());
    
    if (job != null)
    {
      job.jobToBeExecuted(context);
    }
  }

  @Override
  public void jobExecutionVetoed(JobExecutionContext context)
  {
    QuartzRunwayJob job = this.getQuartzRunwayJob(context.getTrigger().getJobDataMap());
    
    if (job != null)
    {
      job.jobExecutionVetoed(context);
    }
  }
  
  @Override
  public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException)
  {
    QuartzRunwayJob job = this.getQuartzRunwayJob(context.getTrigger().getJobDataMap());
    
    if (job != null)
    {
      job.jobWasExecuted(context, jobException);
    }
  }

  @Override
  public void triggerFired(Trigger trigger, JobExecutionContext context)
  {
    QuartzRunwayJob job = this.getQuartzRunwayJob(context.getTrigger().getJobDataMap());
    
    if (job != null)
    {
      job.triggerFired(trigger, context);
    }
  }

  @Override
  public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context)
  {
    QuartzRunwayJob job = this.getQuartzRunwayJob(context.getTrigger().getJobDataMap());
    
    if (job != null)
    {
      return job.vetoJobExecution(trigger, context);
    }
    else
    {
      return false;
    }
  }

  @Override
  public void triggerMisfired(Trigger trigger)
  {
    QuartzRunwayJob job = this.getQuartzRunwayJob(trigger.getJobDataMap());
    
    if (job != null)
    {
      job.triggerMisfired(trigger);
    }
  }

  @Override
  public void triggerComplete(Trigger trigger, JobExecutionContext context, CompletedExecutionInstruction triggerInstructionCode)
  {
    QuartzRunwayJob job = this.getQuartzRunwayJob(context.getTrigger().getJobDataMap());
    
    if (job != null)
    {
      job.triggerComplete(trigger, context, triggerInstructionCode);
    }
  }
}