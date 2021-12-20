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

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.constants.ExceptionConstants;

/**
 * Parses string into Java Primitive Objects.
 */
public class StandardFormat implements FormatFactory
{
  private static Map<String, PluginIF> pluginMap = new ConcurrentHashMap<String, PluginIF>();

  /**
   * Default constructor
   */
  public StandardFormat()
  {
    super();
  }

  /**
   * Internal factory method to return the proper Format subclass.
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public <T> Format<T> getFormat(Class<T> clazz)
  {
    Format<?> format = null;
    if (clazz.equals(Integer.class))
    {
      format = new IntegerFormat();
    }
    else if (clazz.equals(Long.class))
    {
      format = new LongFormat();
    }
    else if (clazz.equals(Float.class))
    {
      format = new FloatFormat();
    }
    else if (clazz.equals(Double.class))
    {
      format = new DoubleFormat();
    }
    else if (clazz.equals(BigDecimal.class))
    {
      format = new DecimalFormat();
    }
    else if (clazz.equals(Character.class))
    {
      format = new CharacterFormat();
    }
    else if (clazz.equals(String.class))
    {
      format = new StringFormat();
    }
    else if (clazz.equals(Boolean.class))
    {
      format = new BooleanFormat();
    }
    else if (clazz.equals(Date.class))
    {
      format = new com.runwaysdk.format.DateFormat();
    }
    else if (PrimitiveFormat.isPrimitive(clazz))
    {
      format = new PrimitiveFormat(clazz, true);
    }
    else if(PrimitiveFormat.isPrimitiveObject(clazz))
    {
      format = new PrimitiveFormat(clazz, false);
    }
    else
    {
      // check all the plugins for custom parsing
      for (PluginIF pluginIF : pluginMap.values())
      {
        format = pluginIF.getFormat(clazz);
        if (format != null)
        {
          break;
        }
      }
    }
    
    // if we still don't have a format, even after checking the plugins, then
    // error out.
    if(format == null)
    {
      String msg = "The factory [" + this.getClass().getName() + "] could not find a Format for ["+clazz+"].";
      CommonExceptionProcessor.processException(
          ExceptionConstants.ConversionException.getExceptionClass(), msg);
    }

    return (Format<T>) format;
  }
  
  public static void registerPlugin(PluginIF pluginFactory)
  {
    pluginMap.put(pluginFactory.getModuleIdentifier(), pluginFactory);
  }

  public static interface PluginIF
  {
    public String getModuleIdentifier();
    
    /**
     * @param value
     * @param locale
     * @return
     */
    public <T> Format<T> getFormat(Class<T> c);
  }

}
