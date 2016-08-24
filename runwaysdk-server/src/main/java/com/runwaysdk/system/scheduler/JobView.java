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

import java.util.List;

public class JobView extends JobViewBase
{
  private static final long serialVersionUID = 462701201;
  
  public JobView()
  {
    super();
  }
  
  /**
   * MdMethod
   */
  public void applyWithJob(ExecutableJob job)
  {
    job.apply();
    
    // Delete any existing downstream job relationship (THERE CAN ONLY BE ONE HIGHLANDER)
    List<? extends DownstreamJobRelationship> downs = job.getAlldownstreamJobRel().getAll();
    if (downs.size() > 0)
    {
      DownstreamJobRelationship rel = downs.get(0);
      rel.appLock();
      rel.delete();
    }
    
    if (this.getDownstreamJob() != null)
    {
      DownstreamJobRelationship rel = job.adddownstreamJob(getDownstreamJob());
      rel.setTriggerOnFailure(getTriggerOnFailure());
      rel.apply();
    }
  }
  
  /**
   * MdMethod
   * 
   * @param jobId
   * @return
   */
  public static com.runwaysdk.system.scheduler.JobView lockJob(java.lang.String jobId)
  {
    ExecutableJob job = ExecutableJob.get(jobId);
    job.lock();
    
    JobView view = new JobView();
    view.setJob(job);
    
    List<? extends DownstreamJobRelationship> downs = job.getAlldownstreamJobRel().getAll();
    if (downs.size() > 0)
    {
      DownstreamJobRelationship rel = downs.get(0);
      ExecutableJob downstreamJob = rel.getChild();
      
      view.setTriggerOnFailure(rel.getTriggerOnFailure());
      view.setDownstreamJob(downstreamJob);
      view.setDownstreamJobDisplayLabel(downstreamJob.getJobId());
    }
    
    return view;
  }
  
}
