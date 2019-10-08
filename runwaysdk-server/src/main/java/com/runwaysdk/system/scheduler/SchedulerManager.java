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

import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerConfigException;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.NameMatcher;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;

public class SchedulerManager
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
    
    resumeRunningJobs();
  }
  
  @Request
  private void resumeRunningJobs()
  {
    resumeRunningJobsInTrans();
  }
  
  @Transaction
  private void resumeRunningJobsInTrans()
  {
    Iterator<JobHistoryRecord> it = getRunningJobs().iterator();
    
    while (it.hasNext())
    {
      JobHistoryRecord jhr = it.next();
      ExecutableJob ej = jhr.getParent();
      
      ej.resume(jhr);
    }
  }
  
  public static JobDetail getJobDetail(String id) throws SchedulerException
  {
    return scheduler().getJobDetail(JobKey.jobKey(id));
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
  public static synchronized void startJob(QuartzRunwayJob quartzJob)
  {
    try
    {
      JobDetail detail = quartzJob.getJobDetail();

      Trigger trigger = buildTrigger(detail, null);

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
        SchedulerManager.removeTriggers(quartzJob.getExecutableJob().getOid());

        Trigger trigger = buildTrigger(detail, cron);

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
  public static void removeTriggers(String oid) throws SchedulerException
  {
    JobKey key = JobKey.jobKey(oid);

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
    try
    {
      JobKey key = JobKey.jobKey(job.getExecutableJob().getOid());

      if (scheduler().checkExists(key))
      {
        scheduler().deleteJob(key);
      }
    }
    catch (SchedulerException e)
    {
      throw new ProgrammingErrorException(e.getLocalizedMessage(), e);
    }
  }

  /**
   * @param detail
   * @param expression
   * @return
   */
  private static Trigger buildTrigger(JobDetail detail, String expression)
  {
    if (expression == null || expression.length() == 0)
    {
      return TriggerBuilder.newTrigger().forJob(detail).build();
    }
    else
    {
      return TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(expression)).forJob(detail).build();
    }
  }

  public static void addJobListener(ExecutableJob job, JobListener jobListener)
  {
    try
    {
      // The listener will fire if the Quartz job oid matches the Runway job oid

      String oid = job.getOid();
      scheduler().getListenerManager().addJobListener(new JobListenerDelegate(jobListener, job), NameMatcher.jobNameEquals(oid));
    }
    catch (SchedulerException e)
    {
      throw new ProgrammingErrorException("Unable to add job listener [" + jobListener.getName() + "] to job [" + job.toString() + "].", e);
    }
  }

  public synchronized static void standby()
  {
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
    return instance != null;
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
}