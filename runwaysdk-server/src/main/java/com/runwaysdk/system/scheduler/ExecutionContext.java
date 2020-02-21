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
/**
 * 
 */
package com.runwaysdk.system.scheduler;

import com.runwaysdk.system.SingleActor;
import com.runwaysdk.system.metadata.MdDimension;

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
public class ExecutionContext
{
  private ExecutableJob job;
  
  private JobHistory history;
  
  private JobHistoryRecord jobHistoryRecord;
  
  private AllJobStatus status;
  
  private SingleActor runAsUser;
  
  private MdDimension runAsDimension;
  
  private String executableJobToString;

  public ExecutableJob getJob()
  {
    return job;
  }

  public void setJob(ExecutableJob job)
  {
    this.job = job;
  }

  public JobHistory getHistory()
  {
    return history;
  }

  public void setHistory(JobHistory history)
  {
    this.history = history;
  }

  public JobHistoryRecord getJobHistoryRecord()
  {
    return jobHistoryRecord;
  }

  public void setJobHistoryRecord(JobHistoryRecord jobHistoryRecord)
  {
    this.jobHistoryRecord = jobHistoryRecord;
  }

  public AllJobStatus getStatus()
  {
    return status;
  }

  /**
   * If specified, this will be the JobHistory's final status when the job completes execution.
   * 
   * @param status
   */
  public void setStatus(AllJobStatus status)
  {
    this.status = status;
  }

  public SingleActor getRunAsUser()
  {
    return runAsUser;
  }

  public void setRunAsUser(SingleActor runAsUser)
  {
    this.runAsUser = runAsUser;
  }

  public MdDimension getRunAsDimension()
  {
    return runAsDimension;
  }

  public void setRunAsDimension(MdDimension runAsDimension)
  {
    this.runAsDimension = runAsDimension;
  }

  public String getExecutableJobToString()
  {
    return executableJobToString;
  }

  public void setExecutableJobToString(String executableJobToString)
  {
    this.executableJobToString = executableJobToString;
  }
}
