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
package com.runwaysdk.form.web.metadata;

import java.util.Date;


public class WebDateMd extends WebMomentMd
{

  private Boolean afterTodayExclusive;
  private Boolean afterTodayInclusive;
  private Boolean beforeTodayExclusive;
  private Boolean beforeTodayInclusive;
  private Date startDate;
  private Date endDate;

  public WebDateMd()
  {
    super();
    this.afterTodayExclusive = null;
    this.afterTodayInclusive = null;
    this.beforeTodayExclusive = null;
    this.beforeTodayInclusive = null;
    this.startDate = null;
    this.endDate = null;
  }

  protected void setAfterTodayExclusive(Boolean afterTodayExclusive)
  {
    this.afterTodayExclusive = afterTodayExclusive;
  }

  protected void setAfterTodayInclusive(Boolean afterTodayInclusive)
  {
    this.afterTodayInclusive = afterTodayInclusive;
  }

  protected void setBeforeTodayExclusive(Boolean beforeTodayExclusive)
  {
    this.beforeTodayExclusive = beforeTodayExclusive;
  }

  protected void setBeforeTodayInclusive(Boolean beforeTodayInclusive)
  {
    this.beforeTodayInclusive = beforeTodayInclusive;
  }

  protected void setStartDate(Date startDate)
  {
    this.startDate = startDate;
  }

  protected void setEndDate(Date endDate)
  {
    this.endDate = endDate;
  }

  public Boolean getAfterTodayExclusive()
  {
    return afterTodayExclusive;
  }
  
  public Boolean getAfterTodayInclusive()
  {
    return afterTodayInclusive;
  }
  
  public Boolean getBeforeTodayExclusive()
  {
    return beforeTodayExclusive;
  }
  
  public Boolean getBeforeTodayInclusive()
  {
    return beforeTodayInclusive;
  }
  
  public Date getEndDate()
  {
    return endDate;
  }
  
  public Date getStartDate()
  {
    return startDate;
  }

}
