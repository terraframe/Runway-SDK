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

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
public class DelegateFormatFactory implements FormatFactory
{
  private static Log log = LogFactory.getLog(DelegateFormatFactory.class);
  
  private class DelegateFormat implements Format<Object>
  {

    private Format<?> customFormat;
    private Format<?> defaultFormat;
    
    private <T> DelegateFormat(Format<T> customFormat, Format<T> defaultFormat)
    {
      this.customFormat = customFormat;
      this.defaultFormat = defaultFormat;
    }
    
    @Override
    public String display(Object value, Locale locale) throws DisplayException
    {
      try
      {
        return this.customFormat.display(value, locale);
      }
      catch(Throwable t)
      {
        log.debug("Unable to display ["+value+"] using ["+this.customFormat+"] with locale ["+locale+"]. Delegating to ["+this.defaultFormat+"].", t);
        return this.defaultFormat.display(value, locale);
      }
    }
    
    @Override
    public String display(Object value) throws DisplayException
    {
      try
      {
        return this.customFormat.display(value);
      }
      catch(Throwable t)
      {
        log.debug("Unable to display ["+value+"] using ["+this.customFormat+"] with locale. Delegating to ["+this.defaultFormat+"].", t);
        return this.defaultFormat.display(value);
      }
    }
    
    @Override
    public String format(Object value, Locale locale) throws FormatException
    {
      try
      {
        return this.customFormat.format(value, locale);
      }
      catch(Throwable t)
      {
        log.debug("Unable to format ["+value+"] using ["+this.customFormat+"] with locale ["+locale+"]. Delegating to ["+this.defaultFormat+"].", t);
        return this.defaultFormat.format(value, locale);
      }
    }

    @Override
    public String format(Object value) throws FormatException
    {
      try
      {
        return this.customFormat.format(value);
      }
      catch(Throwable t)
      {
        log.debug("Unable to format ["+value+"] using ["+this.customFormat+"] with locale. Delegating to ["+this.defaultFormat+"].", t);
        return this.defaultFormat.format(value);
      }
    }
    
    @Override
    public Object parse(Object value, Locale locale) throws ParseException
    {
      try
      {
        return this.customFormat.parse(value, locale);
      }
      catch(Throwable t)
      {
        log.debug("Unable to parse ["+value+"] using ["+this.customFormat+"] with locale ["+locale+"]. Delegating to ["+this.defaultFormat+"].", t);
        return this.defaultFormat.parse(value, locale);
      }
    }

    @Override
    public Object parse(Object value) throws ParseException
    {
      try
      {
        return this.customFormat.parse(value);
      }
      catch(Throwable t)
      {
        log.debug("Unable to parse ["+value+"] using ["+this.customFormat+"]. Delegating to ["+this.defaultFormat+"].", t);
        return this.defaultFormat.parse(value);
      }
    }

    @Override
    public Object parse(String value, Locale locale) throws ParseException
    {
      try
      {
        return this.customFormat.parse(value, locale);
      }
      catch(Throwable t)
      {
        log.debug("Unable to parse ["+value+"] using ["+this.customFormat+"] with locale ["+locale+"]. Delegating to ["+this.defaultFormat+"].", t);
        return this.defaultFormat.parse(value, locale);
      }
    }

    @Override
    public Object parse(String value) throws ParseException
    {
      try
      {
        return this.customFormat.parse(value);
      }
      catch(Throwable t)
      {
        log.debug("Unable to parse ["+value+"] using ["+this.customFormat+"]. Delegating to ["+this.defaultFormat+"].", t);
        return this.defaultFormat.parse(value);
      }
    }
    
  }
  
  /**
   * The first factory to try.
   */
  private FormatFactory customFactory;
  
  /**
   * The default factory that is delegated to when the custom factory fails.
   */
  private FormatFactory defaultFactory;
  
  /**
   * @param formatFactory
   * @param standardFormat
   */
  public DelegateFormatFactory(FormatFactory customFactory, FormatFactory defaultFactory)
  {
    this.customFactory = customFactory;
    this.defaultFactory = defaultFactory;
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.format.FormatFactory#getFormat(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> Format<T> getFormat(Class<T> clazz)
  {
    Format<T> customFormat;
    
    try
    {
      customFormat = this.customFactory.getFormat(clazz);
    }
    catch(Throwable t)
    {
      log.debug("Format for ["+clazz+"] not found in FormatFactory ["+this.customFactory+"]. Delegating to ["+this.defaultFactory+"].", t);
      return this.defaultFactory.getFormat(clazz);
    }
    
    if(customFormat != null)
    {
      try
      {
        Format<T> defaultFormat = this.defaultFactory.getFormat(clazz);
        if(defaultFormat != null)
        {
          return (Format<T>) new DelegateFormat(customFormat, defaultFormat);
        }
        else
        {
          log.debug("Unable to delegate to ["+this.defaultFactory+"] to format ["+clazz+"]. Using ["+customFormat+"].");
          return customFormat;
        }
      }
      catch(Throwable t)
      {
        log.debug("Unable to delegate to ["+this.defaultFactory+"] to format ["+clazz+"]. Using ["+customFormat+"].");
        return customFormat;
      }
    }
    else
    {
      log.debug("Format for ["+clazz+"] not found in FormatFactory ["+this.customFactory+"]. Delegating to ["+this.defaultFactory+"].");
      return this.defaultFactory.getFormat(clazz);
    }
  }

}
