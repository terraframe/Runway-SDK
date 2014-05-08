/**
 * 
 */
package com.runwaysdk.system.scheduler;

import org.quartz.SchedulerException;

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
public class SchedulerStartException extends BusinessException
{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * @param devMessage
   * @param cause
   */
  public SchedulerStartException(String devMessage, SchedulerException cause)
  {
    super(devMessage, cause);
  }
  
  /**
   * Uses the stored parameters and the given Locale to produce an error message
   * suitable for display to end users.
   *
   */
  public String getLocalizedMessage()
  {
    return ServerExceptionMessageLocalizer.schedulerStartException(this.getLocale());
  }

}
