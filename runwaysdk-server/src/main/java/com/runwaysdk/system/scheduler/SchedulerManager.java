/**
 * 
 */
package com.runwaysdk.system.scheduler;

import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
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
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;

/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
public class SchedulerManager
{

  /**
   * 
   */
  private static class Singleton
  {
    private static final SchedulerManager INSTANCE = new SchedulerManager();
  }

  /**
   * The SchedulerFactory instance to manage all Schedulers.
   */
  private SchedulerFactory factory;

  /**
   * The default Scheduler instance (right now only one is supported).
   */
  private Scheduler        scheduler;

  private static boolean   initialized = false;

  /**
   * Constructor to initialize the scheduler factory and default instance.
   */
  private SchedulerManager()
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

        SchedulerConfigurationException sceWrapper = new SchedulerConfigurationException(sceOriginal.getLocalizedMessage(), sceOriginal);

        throw sceWrapper;
      }
      else
      {
        throw new ProgrammingErrorException("Error occurred when initializing the Scheduler Manager.", e);
      }
    }

    initialized = true;
  }

  private static Scheduler scheduler()
  {
    return Singleton.INSTANCE.scheduler;
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
      throw new SchedulerStartException(e.getLocalizedMessage(), e);
    }

    ExecutableJobQuery query = new ExecutableJobQuery(new QueryFactory());

    OIterator<? extends ExecutableJob> iterator = query.getIterator();

    try
    {
      while (iterator.hasNext())
      {
        ExecutableJob job = iterator.next();

        if (job.getCronExpression() != null && job.getCronExpression().length() > 0)
        {
          schedule(job, job.getCronExpression());
        }
      }
    }
    finally
    {

    }
  }

  public synchronized static void schedule(ExecutableJob job, String... expressions)
  {
    try
    {
      JobDetail detail = getJobDetail(job);

      if (expressions.length > 0)
      {
        SchedulerManager.removeTriggers(job);

        // specify the running period of the job
        for (String expression : expressions)
        {
          Trigger trigger = buildTrigger(detail, expression);

          schedule(detail, trigger);
        }
      }
      else
      {
        Trigger trigger = buildTrigger(detail, null);

        schedule(detail, trigger);
      }

    }
    catch (SchedulerException e)
    {
      throw new ScheduleJobException(e.getLocalizedMessage(), e, job);
    }
  }

  /**
   * @param job
   * @throws SchedulerException
   */
  public static void removeTriggers(ExecutableJob job) throws SchedulerException
  {
    JobKey key = JobKey.jobKey(job.getId());

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

  public synchronized static void remove(ExecutableJob job)
  {
    try
    {
      JobKey key = JobKey.jobKey(job.getId());

      if (scheduler().checkExists(key))
      {
        scheduler().deleteJob(key);
      }
    }
    catch (SchedulerException e)
    {
      throw new ScheduleJobException(e.getLocalizedMessage(), e, job);
    }
  }

  /**
   * @param detail
   * @param expression
   * @return
   */
  private static Trigger buildTrigger(JobDetail detail, String expression)
  {
    if (expression == null)
    {
      return TriggerBuilder.newTrigger().forJob(detail).build();
    }
    else
    {
      return TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(expression)).forJob(detail).build();
    }
  }

  /**
   * @param job
   * @throws SchedulerException
   */
  private synchronized static JobDetail getJobDetail(ExecutableJob job) throws SchedulerException
  {
    JobDetail detail = scheduler().getJobDetail(JobKey.jobKey(job.getId()));

    if (detail == null)
    {
      detail = JobBuilder.newJob(job.getClass()).withIdentity(job.getId()).build();

      // Give the Quartz Job a back-reference to the Runway Job
      detail.getJobDataMap().put(ExecutableJob.ID, job.getId());
    }

    return detail;
  }

  public static void addJobListener(ExecutableJob job, JobListener jobListener)
  {
    try
    {
      // The listener will fire if the Quartz job id matches the Runway job id

      String id = job.getId();
      scheduler().getListenerManager().addJobListener(new JobListenerDelegate(jobListener, job), NameMatcher.jobNameEquals(id));
    }
    catch (SchedulerException e)
    {
      throw new AddJobListenerException("Unable to add job listener [" + jobListener.getName() + "] to job [" + job.toString() + "].", e, jobListener, job);
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
      throw new SchedulerStandbyException(e.getLocalizedMessage(), e);
    }
  }

  public static synchronized boolean initialized()
  {
    return initialized;
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
      throw new SchedulerStopException(e.getLocalizedMessage(), e);
    }
  }
}
