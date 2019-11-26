/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.dataaccess.graph.attributes;

import java.util.Date;

public class ValueOverTime implements Comparable<ValueOverTime>
{
  private Date   startDate;

  private Date   endDate;

  private Object value;

  /**
   * @param startDate
   * @param endDate
   * @param value
   */
  public ValueOverTime(Date startDate, Date endDate, Object value)
  {
    super();
    this.startDate = startDate;
    this.endDate = endDate;
    this.value = value;
  }

  /**
   * @return the startDate
   */
  public Date getStartDate()
  {
    return startDate;
  }

  /**
   * @param startDate
   *          the startDate to set
   */
  public void setStartDate(Date startDate)
  {
    this.startDate = startDate;
  }

  /**
   * @return the endDate
   */
  public Date getEndDate()
  {
    return endDate;
  }

  /**
   * @param endDate
   *          the endDate to set
   */
  public void setEndDate(Date endDate)
  {
    this.endDate = endDate;
  }

  /**
   * @return the value
   */
  public Object getValue()
  {
    return value;
  }

  /**
   * @param value
   *          the value to set
   */
  public void setValue(Object value)
  {
    this.value = value;
  }

  public boolean between(Date date)
  {
    return ( this.startDate.equals(date) || this.startDate.before(date) ) && ( this.endDate.equals(date) || this.endDate.after(date) );
  }

  @Override
  public int compareTo(ValueOverTime o)
  {
    return this.startDate.compareTo(o.getStartDate());
  }
}
