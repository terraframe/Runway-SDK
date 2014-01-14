/**
 * 
 */
package com.runwaysdk.system.scheduler;

import java.util.Date;

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
public interface JobIF
{

  public String getJobId();
  
  public Boolean getPaused();
  
  public Boolean getPauseable();
  
  public Integer getWorkTotal();
  
  public Integer getWorkProgress();
  
  public Boolean getCompleted();
  
  public Boolean getCancelable();
  
  public Boolean getCanceled();
  
  public Boolean getRunning();
  
  public Integer getMaxRetries();
  
  public Integer getRetries();
  
  public Long getTimeout();
  
  public Date getStartTime();
  
  public Date getEndTime();
  
  public Boolean getRemoveOnComplete();
  
  public Boolean getStartOnCreate();
  
  public Date getLastRun();
  
  public String getLocalizedDisplayLabel();
  
}
