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
package ognl;

import java.util.Map;

import com.runwaysdk.generation.loader.LoaderDecorator;

// Heads up: remove class
public class OgnlClassResolver implements ClassResolver
{

  /* (non-Javadoc)
   * @see ognl.ClassResolver#classForName(java.lang.String, java.util.Map)
   */
  @Override
  public Class<?> classForName(String className, @SuppressWarnings("rawtypes") Map context) throws ClassNotFoundException
  {
    return LoaderDecorator.load(className);
  }
  
  /** 
   * Clears the cache of reflected methods in Ognl.
   */
  public static void clearOgnlRuntimeMethodCache()
  {
    synchronized (OgnlRuntime.cacheGetMethod)
    {
      OgnlRuntime.cacheGetMethod.clear();
    }
  }
  
  /** 
   * Returns the count of cached reflected methods in Ognl.
   */
  public static int methodCacheCount()
  {
    synchronized (OgnlRuntime.cacheGetMethod)
    {
     return OgnlRuntime.cacheGetMethod.size();
    }
  }
  
}
