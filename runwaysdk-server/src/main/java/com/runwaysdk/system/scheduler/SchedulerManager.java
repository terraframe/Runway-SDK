/**
 * 
 */
package com.runwaysdk.system.scheduler;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerConfigException;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.NameMatcher;

import com.runwaysdk.dataaccess.ProgrammingErrorException;

/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
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
  private Scheduler scheduler;
  
  private static boolean initialized = false;
  
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
      
      if(cause instanceof SchedulerConfigException)
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
  }
  
  public synchronized static void schedule(ExecutableJob job)
  {
    // Create a new Quartz job whose id equals the Runway Job's id.
    JobDetail jd = JobBuilder.newJob(job.getClass()).withIdentity(job.getId()).build();

    // specify the running period of the job
    Trigger trigger = TriggerBuilder
        .newTrigger()
        .build();

    // Give the Quartz Job a back-reference to the Runway Job
    jd.getJobDataMap().put(ExecutableJob.ID, job.getId());
    
    try
    {
      scheduler().getListenerManager().addJobListener(new JobSchedulerManagerListenerForHistory(job.getId() + "_JobSchedulerManagerListenerForHistory"), NameMatcher.jobNameEquals(id));
      
      scheduler().scheduleJob(jd, trigger);
      
    }
    catch (SchedulerException e)
    {
      throw new ScheduleJobException(e.getLocalizedMessage(), e, job);
    }
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
      throw new AddJobListenerException("Unable to add job listener ["+jobListener.getName()+"] to job ["+job.toString()+"].", e, jobListener, job);
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
      if(initialized())
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
