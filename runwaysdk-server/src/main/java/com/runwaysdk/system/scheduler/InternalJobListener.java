/**
 * 
 */
package com.runwaysdk.system.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

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
public class InternalJobListener implements JobListener
{
  private com.runwaysdk.system.scheduler.JobListener jobListener;
  
  InternalJobListener(com.runwaysdk.system.scheduler.JobListener jobListener)
  {
    super();
    
    this.jobListener = jobListener;
  }
  
  /* (non-Javadoc)
   * @see org.quartz.JobListener#getName()
   */
  @Override
  public String getName()
  {
    return this.jobListener.getName();
  }
  
  private Job getJob(JobExecutionContext context)
  {
    Job job = (Job) context.getJobDetail().getJobDataMap().get(Job.CLASS);

    return job;
  }

  /* (non-Javadoc)
   * @see org.quartz.JobListener#jobToBeExecuted(org.quartz.JobExecutionContext)
   */
  @Override
  public void jobToBeExecuted(JobExecutionContext context)
  {
    this.jobListener.onStart(getJob(context));
  }

  /* (non-Javadoc)
   * @see org.quartz.JobListener#jobExecutionVetoed(org.quartz.JobExecutionContext)
   */
  @Override
  public void jobExecutionVetoed(JobExecutionContext context)
  {
    this.jobListener.onCancel(getJob(context));
  }

  /* (non-Javadoc)
   * @see org.quartz.JobListener#jobWasExecuted(org.quartz.JobExecutionContext, org.quartz.JobExecutionException)
   */
  @Override
  public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException)
  {
    this.jobListener.onStop(getJob(context));
  }

}
