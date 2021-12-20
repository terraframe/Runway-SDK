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
package com.runwaysdk.format;

import java.util.Locale;

public abstract class NumberFormat<T extends Number> extends AbstractFormat<T>
{
  /**
  * Denotes if grouping should be used (default is false). Grouping
  * is when either a comma or period is placed to denote a change of
  * 10^3 (for example 1,000 or 1,000,000). If grouping is disabled then
  * 1,000 will be printed as 1000. This is for formatting only as 
  * disabling grouping for parsing truncates at the group delimiter.
  * 
  * TODO make this a changeable property
  */
  private static final boolean USE_GROUPING = false;
  
  public NumberFormat()
  {
    super();  
  }

  /**
   * Parses the String value as a Number and returns it.
   * 
   * @param t
   * @param value
   * @param locale
   * @param T
   * @return
   */
  public T parse(String value, Locale locale)
  {
    if (value == null || value.trim().length() == 0)
    {
      return null;
    }
    
    try
    {
      java.text.NumberFormat format = this.getInstance(locale);
      Number number = format.parse(value);
      return this.convert(number);
    }
    catch (Throwable throwable)
    {
      throw this.createParseException(throwable, locale, value);
    }
  }
  
  /**
   * Converts the value from parsing into the specific Number subclass.
   * 
   * @param number
   * @return
   */
  protected abstract T convert(Number number);
  
  /**
   * Returns the concrete java.text.NumberFormat instance with a given locale.
   * 
   * @param locale
   * @return
   */
  protected abstract java.text.NumberFormat getInstance(Locale locale);

  /**
   * Formats the number into a readable string.
   * 
   */
  @Override
  public String format(Object value, Locale locale) throws FormatException
  {
    if(!this.isValid(value))
    {
      return null;
    }

    try
    {
      java.text.NumberFormat format = this.getInstance(locale);
      format.setGroupingUsed(USE_GROUPING);
      return format.format(value);
    }
    catch (Throwable t)
    {
      throw this.createFormatException(t, locale, value);
    }
  }
}
