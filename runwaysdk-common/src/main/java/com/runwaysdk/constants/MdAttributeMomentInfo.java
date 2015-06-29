/**
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
 */
package com.runwaysdk.constants;


public interface MdAttributeMomentInfo extends MdAttributePrimitiveInfo
{

  /**
   * Class.
   */
  public static final String CLASS   = Constants.METADATA_PACKAGE+".MdAttributeMoment";
  
  /**
   * The date delimiter between the year, month, and day.
   */
  public static final String DATE_DELIM = "-";
  
  /**
   * The time delimiter between the hours, minutes, and seconds.
   */
  public static final String TIME_DELIM = ":";
  
  /**
   * The length of hours input expected (00-24).
   */
  public static final int TIME_HOURS_LENGTH = 2;
  
  /**
   * The length of minutes input expected (00-59).
   */
  public static final int TIME_MINUTES_LENGTH = 2;
  
  /**
   * The length of seconds input expected (00-59).
   */
  public static final int TIME_SECONDS_LENGTH = 2;
  
  /**
   * The length of year input expected (yyyy).
   */
  public static final int DATE_YEAR_LENGTH = 4;
  
  /**
   * The length of month input expected (mm).
   */
  public static final int DATE_MONTH_LENGTH = 2;
  
  /**
   * The length of day input expected (dd).
   */
  public static final int DATE_DAY_LENGTH = 2;

}
