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
package com.runwaysdk.dataaccess.graph.attributes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.IsoFields;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.GregorianCalendar;

import com.runwaysdk.dataaccess.attributes.AttributeFrequencyException;
import com.runwaysdk.system.graph.ChangeFrequency;

public class ValueOverTime
{
  public static final Date INFINITY_END_DATE = new GregorianCalendar(5000, 0, 0).getTime();
  
  private static class FirstDayOfQuarter implements TemporalAdjuster
  {

    @Override
    public Temporal adjustInto(Temporal temporal)
    {
      int currentQuarter = YearMonth.from(temporal).get(IsoFields.QUARTER_OF_YEAR);

      if (currentQuarter == 1)
      {
        return LocalDate.from(temporal).with(TemporalAdjusters.firstDayOfYear());
      }
      else if (currentQuarter == 2)
      {
        return LocalDate.from(temporal).withMonth(Month.APRIL.getValue()).with(TemporalAdjusters.firstDayOfMonth());
      }
      else if (currentQuarter == 3)
      {
        return LocalDate.from(temporal).withMonth(Month.JULY.getValue()).with(TemporalAdjusters.firstDayOfMonth());
      }
      else
      {
        return LocalDate.from(temporal).withMonth(Month.OCTOBER.getValue()).with(TemporalAdjusters.firstDayOfMonth());
      }
    }
  }

  private static class LastDayOfQuarter implements TemporalAdjuster
  {

    @Override
    public Temporal adjustInto(Temporal temporal)
    {
      int currentQuarter = YearMonth.from(temporal).get(IsoFields.QUARTER_OF_YEAR);

      if (currentQuarter == 1)
      {
        return LocalDate.from(temporal).withMonth(Month.MARCH.getValue()).with(TemporalAdjusters.lastDayOfMonth());
      }
      else if (currentQuarter == 2)
      {
        return LocalDate.from(temporal).withMonth(Month.JUNE.getValue()).with(TemporalAdjusters.lastDayOfMonth());
      }
      else if (currentQuarter == 3)
      {
        return LocalDate.from(temporal).withMonth(Month.SEPTEMBER.getValue()).with(TemporalAdjusters.lastDayOfMonth());
      }
      else
      {
        return LocalDate.from(temporal).withMonth(Month.DECEMBER.getValue()).with(TemporalAdjusters.lastDayOfMonth());
      }
    }
  }
  
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
    return this.startDate.before(date) && this.endDate.after(date);
  }
  
  public String toString()
  {
    DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
    
    String endDate = dateFormat.format(INFINITY_END_DATE);
    if (this.endDate != null)
    {
      endDate = dateFormat.format(this.endDate);
    }
    
    return "value [" + this.value + "] from " + dateFormat.format(this.startDate) + " to " + endDate;
  }
  
  public void validate(ChangeFrequency frequency)
  {
    if (startDate != null && endDate != null)
    {
      if (frequency != null)
      {
        LocalDate lStartDate = startDate.toInstant().atZone(ZoneId.of("Z")).toLocalDate();
        LocalDate lEndDate = endDate.toInstant().atZone(ZoneId.of("Z")).toLocalDate();
        
        if (frequency.equals(ChangeFrequency.ANNUAL))
        {
          LocalDate expectedStartDate = lStartDate.with(TemporalAdjusters.firstDayOfYear());
          LocalDate expectedEndDate = lEndDate.with(TemporalAdjusters.lastDayOfYear());

          if (!lStartDate.equals(expectedStartDate) || !lEndDate.equals(expectedEndDate))
          {
            throw new AttributeFrequencyException("Invalid frequency", frequency.name(), startDate, endDate);
          }
        }
        else if (frequency.equals(ChangeFrequency.QUARTER))
        {
          LocalDate expectedStartDate = lStartDate.with(new FirstDayOfQuarter());
          LocalDate expectedEndDate = lEndDate.with(new LastDayOfQuarter());

          if (!lStartDate.equals(expectedStartDate) || !lEndDate.equals(expectedEndDate))
          {
            throw new AttributeFrequencyException("Invalid frequency", frequency.name(), startDate, endDate);
          }
        }
        else if (frequency.equals(ChangeFrequency.MONTHLY))
        {
          LocalDate expectedStartDate = lStartDate.with(TemporalAdjusters.firstDayOfMonth());
          LocalDate expectedEndDate = lEndDate.with(TemporalAdjusters.lastDayOfMonth());

          if (!lStartDate.equals(expectedStartDate) || !lEndDate.equals(expectedEndDate))
          {
            throw new AttributeFrequencyException("Invalid frequency", frequency.name(), startDate, endDate);
          }
        }
      }
    }
  }
}
