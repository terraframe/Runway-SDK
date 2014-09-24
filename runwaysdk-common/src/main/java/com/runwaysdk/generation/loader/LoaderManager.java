/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.runwaysdk.generation.loader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LoaderManager extends ClassLoader
{
  private ClassLoader webappLoader;

  public LoaderManager(ClassLoader parent)
  {
    super(parent);
  }

  protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException
  {
    try
    {
      return LoaderDecorator.loadClass(name, resolve);
    }
    catch (ClassNotFoundException e)
    {
      try
      {
        Method method = webappLoader.getClass().getMethod("actualLoad", String.class, Boolean.TYPE);
        return (Class<?>) method.invoke(webappLoader, name, resolve);
      }
      catch (InvocationTargetException ite)
      {
        throw new ClassNotFoundException(name, ite.getTargetException());
      }
      catch (Exception ex)
      {
        throw new ClassNotFoundException(name, ex);
      }
    }
  }

  @Override
  public Class<?> loadClass(String name) throws ClassNotFoundException
  {
    try
    {
      return LoaderDecorator.loadClass(name);
    }
    catch (ClassNotFoundException e)
    {
      try
      {
        Method method = webappLoader.getClass().getMethod("actualLoad", String.class);
        return (Class<?>) method.invoke(webappLoader, name);
      }
      catch (InvocationTargetException ite)
      {
        throw new ClassNotFoundException(name, ite.getTargetException());
      }
      catch (Exception ex)
      {
        throw new ClassNotFoundException(name, ex);
      }
    }
  }

  public synchronized void setWebappClassLoader(ClassLoader webappLoader)
  {
    this.webappLoader = webappLoader;
  }
}
