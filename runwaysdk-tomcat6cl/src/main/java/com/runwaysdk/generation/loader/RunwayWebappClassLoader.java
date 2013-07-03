package com.runwaysdk.generation.loader;
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


import java.lang.reflect.Method;

import org.apache.catalina.loader.WebappClassLoader;

public class RunwayWebappClassLoader extends WebappClassLoader
{
  private ClassLoader loaderManager;

  private Method      lock;

  private Method      unlock;
  
  private boolean     useLocks = false;

  public RunwayWebappClassLoader()
  {
    super();
  }

  public RunwayWebappClassLoader(ClassLoader parent)
  {
    super(parent);
  }

  /**
   * This ghost method is called by other classes when they attempt to resolve
   * dependencies. Rather that immediately loading a class that may be
   * {@link Reloadable}, this method instead delegates to
   * {@link LoaderManager#loadClass(String)}, which attempts to load through
   * {@link LoaderDecorator#load(String)} before using the
   * {@link WebappClassLoader}
   */
  @Override
  public Class<?> loadClass(String name) throws ClassNotFoundException
  {
    Class<?> c = workaround(name);
    if (c!=null)
    {
      return c;
    }
    
    lock();
    try
    {
      // If loaderManager is null then we're not initialized yet. Do a regular load.
      if (loaderManager == null)
      {
        return actualLoad(name);
      }
      else
      {
        return loaderManager.loadClass(name);
      }
    }
    finally
    {
      unlock();
    }
  }

  /**
   * @see RunwayWebappClassLoader#loadClass(String)
   */
  @Override
  public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException
  {
    Class<?> c = workaround(name);
    if (c!=null)
    {
      return c;
    }
    
    lock();
    try
    {
      // If loaderManager is null then we're not initialized yet. Do a regular load.
      if (loaderManager == null)
      {
        return actualLoad(name, resolve);
      }
      else
      {
        // FIXME: Consider reflection or the addition of an interface to preserve
        // the value of resolve
        return loaderManager.loadClass(name);
      }
    }
    finally
    {
      unlock();
    }
  }
  
  /**
   * A hardcoded workaround for known classloader bug 6265952.
   * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6265952
   */
  private synchronized Class<?> workaround(String name)
  {
    if (name.startsWith("java.") || name.startsWith("sun."))
    {
      try
      {
        return getParent().loadClass(name);
      }
      catch (Exception e)
      {
      }
    }
    return null;
  }

  /**
   * Called by {@link LoaderManager#loadClass(String)} if
   * {@link LoaderDecorator#load(String)} fails to find the given class.
   */
  public Class<?> actualLoad(String name) throws ClassNotFoundException
  {
    lock();
    try
    {
      return super.loadClass(name, false);
    }
    finally
    {
      unlock();
    }
  }

  /**
   * @see RunwayWebappClassLoader#actualLoad(String)
   */
  public Class<?> actualLoad(String name, boolean resolve) throws ClassNotFoundException
  {
    lock();
    try
    {
      return super.loadClass(name, resolve);
    }
    finally
    {
      unlock();
    }
  }

  public void setManager(ClassLoader loaderManager)
  {
    this.loaderManager = loaderManager;
  }

  public void lock() throws ClassNotFoundException
  {
    if (!useLocks)
    {
      return;
    }
    else
    {
      init();
    }
    
    try
    {
      lock.invoke(null, this);
    }
    catch (Exception e)
    {
      throw new ClassNotFoundException("Unable to invoke LockHolder.lock()", e);
    }
  }

  private void unlock() throws ClassNotFoundException
  {
    if (!useLocks)
    {
      return;
    }
    else
    {
      init();
    }
    
    try
    {
      unlock.invoke(null);
    }
    catch (Exception e)
    {
      throw new ClassNotFoundException("Unable to invoke LockHolder.unlock()", e);
    }
  }

  private void init() throws ClassNotFoundException
  {
    Class<?> clazz = super.loadClass("com.runwaysdk.generation.loader.LockHolder", false);
    try
    {
      lock = clazz.getMethod("lock", Object.class);
      unlock = clazz.getMethod("unlock");
    }
    catch (NoSuchMethodException e)
    {
      throw new ClassNotFoundException("Could not find lock/unlock methods on LockHolder", e);
    }
  }
  
  public void disableLocks()
  {
    this.useLocks = false;
  }
  
  public void enableLocks()
  {
    this.useLocks = true;
  }
}
