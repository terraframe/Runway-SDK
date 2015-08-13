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
package com.runwaysdk.format;

import java.util.Date;
import java.util.Locale;

import com.runwaysdk.constants.Constants;
import com.runwaysdk.util.DateUtilities;

public class DateFormat extends AbstractFormat<Date>
{
  public DateFormat()
  {
    super();
  }

  @Override
  public Date parse(String value, Locale locale) throws ParseException
  {
    if(!this.isValid(value))
    {
      return null;
    }

    for (java.text.DateFormat parser : this.getDateParsers(locale))
    {
      try
      {
        Date date = parser.parse(value);
        return date;
      }
      catch (java.text.ParseException e)
      {
        // Do nothing try the next parser
      }
    }
    
    try
    {
      // the explicit parsers failed, so try this
      return DateUtilities.parseDate(value);
    }
    catch(Throwable t)
    {
      // we tried but could not convert the value
      throw this.createParseException(locale, value);
    }
  }

  /**
   * Accepted date formats.
   * 
   * @param locale
   * @return
   */
  private java.text.DateFormat[] getDateParsers(Locale locale)
  {
    if (locale == null)
    {
      locale = AbstractFormatFactory.getLocale();
    }

    /*
     * IMPORTANT: Go from more specific to least specific to match on the most amount of
     * information. For example, 1984-05-17 10:09:08 will match YYYY-MM-DD, but we want it
     * to match YYYY-MM-DD HH:MM:SS as to not lose the time dimension.
     */
    return new java.text.DateFormat[] {

        // DateTime formats
        new java.text.SimpleDateFormat(Constants.DATETIME_FORMAT, locale),
        new java.text.SimpleDateFormat(Constants.DATETIME_WITH_TIMEZONE_FORMAT, locale),
        java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.LONG, java.text.DateFormat.LONG,
            locale),

        // Date formats
        new java.text.SimpleDateFormat(Constants.DATE_FORMAT, locale),
        java.text.DateFormat.getDateInstance(java.text.DateFormat.LONG, locale),
        java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT, locale),

        // Time formats
        new java.text.SimpleDateFormat(Constants.TIME_FORMAT, locale),
        java.text.DateFormat.getTimeInstance(java.text.DateFormat.LONG, locale)
    };

  }

  @Override
  public String format(Object value, Locale locale) throws FormatException
  {
    if (value == null)
    {
      return null;
    }

    for (java.text.DateFormat parser : this.getDateParsers(locale))
    {
      try
      {
        String date = parser.format(value);
        return date;
      }
      catch(Throwable t)
      {
        // try the next parser
      }
    }
    
    throw this.createFormatException(locale, value);
  }
}
