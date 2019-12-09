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
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.attributes.AttributeFrequencyException;
import com.runwaysdk.system.graph.ChangeFrequency;

public class ValueOverTime implements Comparable<ValueOverTime>
{
  public static final Date INFINITY_END_DATE;

  static
  {
    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    cal.clear();
    cal.set(5000, Calendar.DECEMBER, 31);

    INFINITY_END_DATE = cal.getTime();
  }

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

//  private static class LastDayOfQuarter implements TemporalAdjuster
//  {
//
//    @Override
//    public Temporal adjustInto(Temporal temporal)
//    {
//      int currentQuarter = YearMonth.from(temporal).get(IsoFields.QUARTER_OF_YEAR);
//
//      if (currentQuarter == 1)
//      {
//        return LocalDate.from(temporal).withMonth(Month.MARCH.getValue()).with(TemporalAdjusters.lastDayOfMonth());
//      }
//      else if (currentQuarter == 2)
//      {
//        return LocalDate.from(temporal).withMonth(Month.JUNE.getValue()).with(TemporalAdjusters.lastDayOfMonth());
//      }
//      else if (currentQuarter == 3)
//      {
//        return LocalDate.from(temporal).withMonth(Month.SEPTEMBER.getValue()).with(TemporalAdjusters.lastDayOfMonth());
//      }
//      else
//      {
//        return LocalDate.from(temporal).withMonth(Month.DECEMBER.getValue()).with(TemporalAdjusters.lastDayOfMonth());
//      }
//    }
//  }

  private LocalDate   startDate;

  private LocalDate   endDate;

  private Object value;

  /**
   * @param startDate
   * @param endDate
   * @param value
   */
  public ValueOverTime(Date startDate, Date endDate, Object value)
  {
    super();
    
    this.value = value;
    
    this.setStartDate(startDate);
    this.setEndDate(endDate);
  }

  /**
   * @return the startDate
   */
  public Date getStartDate()
  {
    return Date.from(startDate.atStartOfDay().atZone(ZoneId.of("Z")).toInstant());
  }
  
  public LocalDate getLocalStartDate()
  {
    return this.startDate;
  }

  /**
   * @param startDate
   *          the startDate to set
   */
  public void setStartDate(Date startDate)
  {
    this.startDate = startDate.toInstant().atZone(ZoneId.of("Z")).toLocalDate();
  }

  /**
   * @return the endDate
   */
  public Date getEndDate()
  {
    if (endDate == null)
    {
      return null;
    }
    
    return Date.from(endDate.atStartOfDay().atZone(ZoneId.of("Z")).toInstant());
  }
  
  public LocalDate getLocalEndDate()
  {
    return this.endDate;
  }

  /**
   * @param endDate
   *          the endDate to set
   */
  public void setEndDate(Date endDate)
  {
    if (endDate != null)
    {
      this.endDate = endDate.toInstant().atZone(ZoneId.of("Z")).toLocalDate();
    }
    else
    {
      endDate = null;
    }
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
    LocalDate localDate = date.toInstant().atZone(ZoneId.of("Z")).toLocalDate();
    
    return ( this.startDate.equals(localDate) || this.startDate.isBefore(localDate) ) && ( this.endDate.equals(localDate) || this.endDate.isAfter(localDate) );
  }

  @Override
  public int compareTo(ValueOverTime o)
  {
    return this.startDate.compareTo(o.getLocalStartDate());
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
//    if (startDate != null)
//    {
//      if (frequency != null)
//      {
//        if (frequency.equals(ChangeFrequency.ANNUAL))
//        {
//          LocalDate expectedStartDate = startDate.with(TemporalAdjusters.firstDayOfYear());
//          // LocalDate expectedEndDate =
//          // lEndDate.with(TemporalAdjusters.lastDayOfYear());
//
//          // if (!lStartDate.equals(expectedStartDate) ||
//          // !lEndDate.equals(expectedEndDate))
//          if (!startDate.equals(expectedStartDate))
//          {
//            throw new AttributeFrequencyException("Invalid frequency", frequency.name(), this.getStartDate());
//          }
//        }
//        else if (frequency.equals(ChangeFrequency.QUARTER))
//        {
//          LocalDate expectedStartDate = startDate.with(new FirstDayOfQuarter());
//          // LocalDate expectedEndDate = lEndDate.with(new LastDayOfQuarter());
//
//          if (!startDate.equals(expectedStartDate))
//          {
//            throw new AttributeFrequencyException("Invalid frequency", frequency.name(), this.getStartDate());
//          }
//        }
//        else if (frequency.equals(ChangeFrequency.MONTHLY))
//        {
//          LocalDate expectedStartDate = startDate.with(TemporalAdjusters.firstDayOfMonth());
//          // LocalDate expectedEndDate =
//          // lEndDate.with(TemporalAdjusters.lastDayOfMonth());
//
//          if (!startDate.equals(expectedStartDate))
//          {
//            throw new AttributeFrequencyException("Invalid frequency", frequency.name(), this.getStartDate());
//          }
//        }
//      }
//    }
  }
}
