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
  
  private Job job;
  
  ExecutionContext(Job job)
  {
    this.job = job;
  }
  
  /**
   * @return
   */
  public Job getJob()
  {
    return this.job;
  }
  
  /**
   * 
   * @param job
   * @return
   */
  public static ExecutionContext factory(Context context, Job job)
  {
    if(context == Context.EXECUTION)
    {
      return new ExecutionContext(job);
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
