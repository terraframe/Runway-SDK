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
package com.runwaysdk.format;

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Class that represents an error when formatting or parsing a value. Instances
 * of this class and its subclasses are expected to be used internally for developers
 * as checked exceptions to handle control flow. This means instances of this class
 * should never propagate up the stack for user-friendly localized messages.
 */
public abstract class AbstractFormatException extends RuntimeException
{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * The Log to use to record debugging information.
   */
  private static Log log = LogFactory.getLog(AbstractFormatException.class);
  
  /**
   * The Format class that attempted to format/parse the target value.
   */
  private Format<?> format;
  
  /**
   * The Locale used when formatting/parsing the target value.
   */
  private Locale locale;
  
  /**
   * 
   * @param locale
   */
  public AbstractFormatException(Throwable cause, Format<?> format, Locale locale)
  {
    super(cause);
    
    this.format = format;
    this.locale = locale;
  }
  
  /**
   * 
   * @param format
   * @param locale
   */
  public AbstractFormatException(Format<?> format, Locale locale)
  {
    super();
    
    this.format = format;
    this.locale = locale;
  }

  /**
   * 
   * @return
   */
  public Log getLog()
  {
    return log;
  }
  
  /**
   * 
   * @return
   */
  public Format<?> getFormat()
  {
    return format;
  }
  
  /**
   * 
   * @return
   */
  public Locale getLocale()
  {
    return locale;
  }
  
  /**
   * Logs information about the subclass state for debugging.
   */
  protected abstract void logException();
  
}
