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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.generation.loader.LoaderDecorator;


/**
 * Parses all primitives using the common constructor that accepts
 * a String argument. This is a simple, non-localized attempt to
 * format and parse a value.
 */
public class PrimitiveFormat extends AbstractFormat<Object>
{
  private static Map<String, String> primitives = new HashMap<String, String>();
  private static Set<String> primitiveObjs = new HashSet<String>();
  
  static {
    primitives.put(boolean.class.getName(), Boolean.class.getName());
    primitives.put(byte.class.getName(), Byte.class.getName());
    primitives.put(short.class.getName(), Short.class.getName());
    primitives.put(char.class.getName(), Character.class.getName());
    primitives.put(int.class.getName(), Integer.class.getName());
    primitives.put(long.class.getName(), Long.class.getName());
    primitives.put(float.class.getName(), Float.class.getName());
    primitives.put(double.class.getName(), Double.class.getName());
    
    primitiveObjs.addAll(primitives.values());
  }
  
  /**
   * The concrete primitive class that needs to be formatted.
   */
  private Class<?> primitiveClass;
  
  /**
   * Denotes if the class is a true primitive (e.g., byte).
   */
  private boolean isPrimitiveClass;
  
  /**
   * @param clazz
   */
  public PrimitiveFormat(Class<?> primitiveClass, boolean isPrimitiveClass)
  {
    super();
    this.primitiveClass = primitiveClass;
    this.isPrimitiveClass = isPrimitiveClass;
  }

  @Override
  public Object parse(String value, Locale locale) throws ParseException
  {
    if(value == null)
    {
      return null;
    }
    
    try
    {
      if(this.isPrimitiveClass)
      {
        String primitiveObject = primitives.get(this.primitiveClass.getName());
        return LoaderDecorator.load(primitiveObject).getConstructor(String.class).newInstance(value);
      }
      else
      {
        return this.primitiveClass.getConstructor(String.class).newInstance(value);
      }
    }
    catch(Throwable t)
    {
      throw this.createParseException(t, locale, value);
    }
  }
  
  @Override
  public String format(Object object, Locale locale) throws FormatException
  {
    if(object == null)
    {
      return null;
    }
    
    return object.toString();
  }

  public static boolean isPrimitive(Class<?> clazz)
  {
    return primitives.containsKey(clazz.getName());
  }
  
  public static boolean isPrimitiveObject(Class<?> clazz)
  {
    return primitiveObjs.contains(clazz.getName());
  }
}
