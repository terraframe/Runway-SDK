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
package com.runwaysdk.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.ExceptionConstants;

public class DateUtilities
{
  private static final String ISO8601     = "yyyy-MM-dd'T'HH:mm:ssZ";

  private static final String NO_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ss";

  public static String formatISO8601()
  {
    return formatISO8601(new Date());
  }

  public static String formatISO8601(Date date)
  {
    if (date == null)
    {
      return formatISO8601(new Date());
    }

    return new SimpleDateFormat(ISO8601).format(date);
  }

  public static Date parseISO8601(String time)
  {
    try
    {
      return new SimpleDateFormat(ISO8601).parse(time);
    }
    catch (ParseException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }

    return null;
  }

  public static Date parseDateWithoutTimezone(String time)
  {
    try
    {
      return new SimpleDateFormat(NO_TIMEZONE).parse(time);
    }
    catch (ParseException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }

    return null;
  }

  public static Date parseDate(String time)
  {
    //return new Date(Long.parseLong(json));
    if(CommonProperties.getIncludeTimezone())
    {
      return DateUtilities.parseISO8601(time);
    }
    else
    {
      return DateUtilities.parseDateWithoutTimezone(time);
    }
  }

}
