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

import org.slf4j.Logger;


public class ParseException extends AbstractFormatException
{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  /**
   * The value attempted to parse.
   */
  private String value;
  
  /**
   * 
   * @param format
   * @param locale
   * @param log
   */
  public ParseException(Throwable cause, AbstractFormat<?> format, Locale locale, String value)
  {
    super(cause, format, locale);
    this.value = value;
  }
  
  public ParseException(AbstractFormat<?> format, Locale locale, String value)
  {
    super(format, locale);
    this.value = value;
  }

  /**
   * Logs an exception when a parse error occurs. Because parse errors
   * are a normal part of handling user input, this will only log at the
   * DEBUG level to avoid flooding the logs.
   */
  @Override
  protected void logException()
  {
    Logger log = this.getLog();
    if(log.isDebugEnabled())
    {
      String template = "Error in [%s] when parsing [%s] with locale [%s].";
      String message = String.format(template, this.getFormat(), this.getValue(), this.getLocale());
      log.debug(message, this);
    }
    
  }
  
  /**
   * 
   * @return
   */
  public String getValue()
  {
    return value;
  }
}
