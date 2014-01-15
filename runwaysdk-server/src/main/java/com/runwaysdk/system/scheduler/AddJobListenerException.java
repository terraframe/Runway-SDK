/**
 * 
 */
package com.runwaysdk.system.scheduler;

import com.runwaysdk.ServerExceptionMessageLocalizer;
import com.runwaysdk.business.BusinessException;

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
public class AddJobListenerException extends BusinessException
{

  private ExecutableJob job;
  
  private JobListener jobListener;
  

  /**
   * Thrown when a JobListener cannot be added for a given Job.
   * 
   * @param devMessage
   * @param cause
   * @param jobListener
   * @param job
   */
  public AddJobListenerException(String devMessage, Throwable cause, JobListener jobListener, ExecutableJob job)
  {
    super(devMessage, cause);
    
    this.jobListener = jobListener;
    this.job = job;
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.RunwayException#getLocalizedMessage()
   */
  @Override
  public String getLocalizedMessage()
  {
    return ServerExceptionMessageLocalizer.addJobListenerException(this.getLocale(), jobListener, job);
  }
}
