/**
 * 
 */
package com.runwaysdk.format;

import java.util.Locale;

import org.apache.commons.logging.Log;

/*******************************************************************************
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
 ******************************************************************************/
public class DisplayException extends AbstractFormatException
{

  /**
   * The object attempted to display.
   */
  private Object object;
  
  /**
   * Constructor invoked when the given Format failed to display the object.
   * 
   * @param t
   * @param format
   * @param locale
   * @param object
   */
  public DisplayException(Throwable t, AbstractFormat<?> format, Locale locale, Object object)
  {
    super(t, format, locale);
    this.object = object;
  }
  
  /**
   * Constructor invoked when the given Format failed to display the object.
   * 
   * @param format
   * @param locale
   * @param object
   */
  public DisplayException(AbstractFormat<?> format, Locale locale, Object object)
  {
    super(format, locale);
    this.object = object;
  }
  
  /**
   * Logs an exception when a display error occurs. Because display errors
   * are a normal part of handling user input, this will only log at the
   * DEBUG level to avoid flooding the logs.
   */
  @Override
  protected void logException()
  {
    Log log = this.getLog();
    if(log.isDebugEnabled())
    {
      String template = "Error in [%s] when displaying [%s] with locale [%s].";
      String message = String.format(template, this.getFormat(), this.getObject(), this.getLocale());
      log.debug(message, this);
    }
  }
  
  /**
   * 
   * @return
   */
  public Object getObject()
  {
    return object;
  }
  
}
