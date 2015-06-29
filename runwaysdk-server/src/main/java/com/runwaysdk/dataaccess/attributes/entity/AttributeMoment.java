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
package com.runwaysdk.dataaccess.attributes.entity;

public abstract class AttributeMoment extends Attribute
{
  /**
   * 
   */
  private static final long serialVersionUID = -4447569345401228714L;

  /**
   * The maximum value an hour can have.
   */
  private static final int MAX_HOUR = 23;
  
  /**
   * The maximum value a year can have.
   */
  private static final int MAX_YEAR = 4710;
  
  /**
   * The minimum value a year can have.
   */
  private static final int MIN_YEAR = 1760;
  
  /**
   * The time delimeter.
   */
  protected static final String TIME_DELIM = ":";
  
  /**
   * The date delimiter.
   */
  protected static final String DATE_DELIM = "-";
  
  /**
   * Inherited constrcutor, sets <code>name</code> and <code>definingEntityType</code>.
   * 
   * @param name The name of this integer attribute.
   * @param mdAttributeKey key of the defining attribute metadata
   * @param definingEntityType Name of the class that defines this attribute.
   */
  protected AttributeMoment(String name, String mdAttributeKey, String definingEntityType)
  {
    super(name, mdAttributeKey, definingEntityType);
  }

  /**
   * Inherited constrcutor, sets <code>name</code>, <code>definingEntityType</code>, and
   * <code>value</code>.
   * 
   * @param name The name of this integer attribute.
   * @param mdAttributeKey key of the defining attribute metadata
   * @param definingEntityType Name of the class that defines this attribute.
   * @param value The value of this integer. "<code>true</code>" or "<code>false</code>"
   */
  protected AttributeMoment(String name, String mdAttributeKey, String definingEntityType, String value)
  {
    super(name, mdAttributeKey, definingEntityType, value);
  }
  
  /**
   * Validates the bounds for a time.
   * 
   * @param The time to check.
   * @return true if the time passes validation, false otherwise.
   */
  protected static boolean checkTimeBounds(String timeValue)
  {
    int hour = Integer.parseInt(timeValue.split(TIME_DELIM)[0]);
    if(hour > MAX_HOUR)
    {
    	return false;
    }
    return true;
  }
  
  /**
   * Validates the bounds for a date.
   * 
   * @param The date to check.
   * @return true if the date passes validation, false otherwise.
   */
  protected static boolean checkDateBounds(String dateValue)
  {
    int year = Integer.parseInt(dateValue.split(DATE_DELIM)[0]);
    if(year < AttributeMoment.MIN_YEAR || year > AttributeMoment.MAX_YEAR)
    {
    	return false;
    }
    return true;
  }
}
