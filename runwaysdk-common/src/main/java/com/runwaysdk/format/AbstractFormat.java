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

import com.runwaysdk.constants.CommonProperties;


public abstract class AbstractFormat<T> implements Format<T>
{
  
  /**
   */
  public AbstractFormat()
  {
    super();
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.format.Format#display(java.lang.Object, java.util.Locale)
   */
  @Override
  public String display(Object value, Locale locale) throws DisplayException
  {
    try
    {
      return this.format(value, locale);
    }
    catch(FormatException e)
    {
      throw this.createDisplayException(e, locale, value);
    }
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.format.Format#display(java.lang.Object)
   */
  @Override
  public String display(Object value) throws DisplayException
  {
    return this.display(value, CommonProperties.getDefaultLocale());
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.format.Format#format(java.lang.Object)
   */
  @Override
  public final String format(Object value) throws FormatException
  {
    return this.format(value, CommonProperties.getDefaultLocale());
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.format.Format#parse(java.lang.Object)
   */
  @Override
  public T parse(Object value) throws ParseException
  {
    return this.parse(value, CommonProperties.getDefaultLocale());
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.format.Format#parse(java.lang.Object, java.util.Locale)
   */
  @Override
  public T parse(Object value, Locale locale) throws ParseException
  {
    if(value == null)
    {
      return null;
    }
    else
    {
      return this.parse(value.toString(), locale);
    }
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.format.Format#parse(java.lang.String)
   */
  @Override
  public final T parse(String value) throws ParseException
  {
    return this.parse(value, CommonProperties.getDefaultLocale());
  }
  
  /**
   * Creates an exception when parsing fails.
   * 
   * @param cause
   * @param locale
   * @param value
   * @return
   */
  protected ParseException createParseException(Throwable cause, Locale locale, String value)
  {
    return new ParseException(cause, this, locale, value);
  }

  /**
   * 
   * @param locale
   * @param value
   * @return
   */
  protected ParseException createParseException(Locale locale, String value)
  {
    return new ParseException(this, locale, value);
  }

  /**
   * Creates an exception when formatting fails.
   * 
   * @param cause
   * @param locale
   * @param value
   * @return
   */
  protected FormatException createFormatException(Throwable cause, Locale locale, Object value)
  {
    return new FormatException(cause, this, locale, value);
  }

  /**
   * Creates an exception when formatting fails.
   * 
   * @param locale
   * @param value
   * @return
   */
  protected FormatException createFormatException(Locale locale, Object value)
  {
    return new FormatException(this, locale, value);
  }

  /**
   * Creates an exception when displaying fails.
   * 
   * @param cause
   * @param locale
   * @param value
   * @return
   */
  protected DisplayException createDisplayException(Throwable cause, Locale locale, Object value)
  {
    return new DisplayException(cause, this, locale, value);
  }
  
  /**
   * Creates an exception when displaying fails.
   * 
   * @param locale
   * @param value
   * @return
   */
  protected DisplayException createDisplayException(Locale locale, Object value)
  {
    return new DisplayException(this, locale, value);
  }
  
  /**
   * A valid value is one that is not null or a non-empty string.
   * @param value
   */
  protected boolean isValid(Object value)
  {
    if(value == null || (value instanceof String && value.toString().trim().length() == 0))
    {
      return false;
    }
    else
    {
      return true;
    }
  }

  /**
   * 
   */
  @Override
  public String toString()
  {
    return "Format " + this.getClass().getName();
  }
}
