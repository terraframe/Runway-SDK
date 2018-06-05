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
public class ExecutionContext
{
  enum Context
  {
    EXECUTION,
    LISTENER
  }
  
  private ExecutableJob job;
  private JobHistory history;
  
  private AllJobStatus status;
  
  ExecutionContext(ExecutableJob job, JobHistory history)
  {
    this.job = job;
    this.history = history;
    this.status = null;
  }
  
  /**
   * @return
   */
  public ExecutableJob getJob()
  {
    return this.job;
  }
  
  public JobHistory getJobHistory() {
    return this.history;
  }
  
  public void setStatus(AllJobStatus status)
  {
    this.status = status;
  }
  
  public AllJobStatus getStatus()
  {
    return status;
  }
  
  /**
   * 
   * @param job
   * @return
   */
  public static ExecutionContext factory(Context context, ExecutableJob job, JobHistory history)
  {
    if(context == Context.EXECUTION)
    {
      return new ExecutionContext(job, history);
    }
    else if(context == Context.LISTENER)
    {
      return new ListenerContext(job);
    }
    else
    {
      throw new ProgrammingErrorException("Context ["+context+"] not supported.");
    }
  }
}
