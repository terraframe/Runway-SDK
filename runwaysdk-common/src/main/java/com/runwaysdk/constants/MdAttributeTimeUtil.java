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
package com.runwaysdk.constants;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A common util class for MdAttributeTime objects
 * @author jsmethie
 *
 */
public class MdAttributeTimeUtil extends MdAttributeUtil
{
  /**
   * Returns the type safe object with the given value for this attribute type.  Returns
   * null iff the value is an empty string.  Assumes the value is not null and represents
   * a valid value of the return type.
   * @param toParse
   * @return type safe object with the given value for this attribute type. 
   */
  public static java.util.Date getTypeSafeValue(String toParse)
  {
    if (toParse == null || toParse.trim().equals(""))
    {
      return null;
    }
    else
    {
      return new java.text.SimpleDateFormat(Constants.TIME_FORMAT).parse(toParse, new java.text.ParsePosition(0));
    }
  }
  
  /**
   * Formats a date object into a string. If the date object is null, an empty
   * string is returned.
   * 
   * @param date
   * @return
   */
  public static String getTypeUnsafeValue(Date date)
  {
    if(date == null)
    {
      return "";
    }
    else
    {
      SimpleDateFormat format = new java.text.SimpleDateFormat(Constants.TIME_FORMAT);
      return format.format(date);
    }
  }
}
