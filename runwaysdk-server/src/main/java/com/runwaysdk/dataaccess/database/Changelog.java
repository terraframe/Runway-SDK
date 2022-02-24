/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.dataaccess.database;

import java.util.Date;

public class Changelog
{
  private Long   changeNumber;

  private Date   completeDate;

  private String appliedBy;

  private String description;

  /**
   * @return the changeNumber
   */
  public Long getChangeNumber()
  {
    return changeNumber;
  }

  /**
   * @param changeNumber
   *          the changeNumber to set
   */
  public void setChangeNumber(Long changeNumber)
  {
    this.changeNumber = changeNumber;
  }

  /**
   * @return the completeDate
   */
  public Date getCompleteDate()
  {
    return completeDate;
  }

  /**
   * @param completeDate
   *          the completeDate to set
   */
  public void setCompleteDate(Date completeDate)
  {
    this.completeDate = completeDate;
  }

  /**
   * @return the appliedBy
   */
  public String getAppliedBy()
  {
    return appliedBy;
  }

  /**
   * @param appliedBy
   *          the appliedBy to set
   */
  public void setAppliedBy(String appliedBy)
  {
    this.appliedBy = appliedBy;
  }

  /**
   * @return the description
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * @param description
   *          the description to set
   */
  public void setDescription(String description)
  {
    this.description = description;
  }
}
