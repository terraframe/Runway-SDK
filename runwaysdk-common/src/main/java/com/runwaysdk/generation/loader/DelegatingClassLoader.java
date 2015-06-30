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
/**
 * 
 */
package com.runwaysdk.generation.loader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.constants.LocalProperties;

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
public class DelegatingClassLoader extends URLClassLoader implements ReloadableClassLoaderIF
{
  /**
   * The current loader
   */
  private ClassLoader  loader;

  /**
   * Web environments often will switch out the parent loader. We keep track of
   * the current parent so that reloads of the RunwayClassLoader will be able to
   * delegate correctly.
   */
  private ClassLoader  parent;

  /**
   * A list of listeners to be notified when reload() is called.
   */
  private List<Object> listeners;

  /**
   * Sets up the Decorator, including the default class loader
   */
  public DelegatingClassLoader()
  {
    super(urlArray());

    this.listeners = new LinkedList<Object>();
    this.parent = ComponentInfo.class.getClassLoader();

    newLoader();
  }
  
  private ClassLoader getLoader()
  {
    if (LocalProperties.isReloadableClassesEnabled())
    {
      return loader;
    }
    else
    {
      return DelegatingClassLoader.class.getClassLoader();
    }
  }

  /**
   * Creates a new ClassLoader (orphaning the old one).
   * 
   * @return
   * @throws MalformedURLException
   */
  public void newLoader()
  {
    if (LocalProperties.isReloadableClassesEnabled())
    {
      loader = new RunwayClassLoader(urlArray(), parent);
    }
    else
    {
      // Don't store the class loader, we don't want to risk a memory leak. The
      // class loader is fetched in getLoader.
      // loader = this.getParent();
    }
  }

  void setLoader(RunwayClassLoader loader)
  {
    this.loader = loader;
  }

  /**
   * 
   * @see java.lang.ClassLoader#clearAssertionStatus()
   */
  public void clearAssertionStatus()
  {
    getLoader().clearAssertionStatus();
  }

  /**
   * @param obj
   * @return
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object obj)
  {
    return getLoader().equals(obj);
  }

  /**
   * @param name
   * @return
   * @see java.net.URLClassLoader#findResource(java.lang.String)
   */
  public URL findResource(String name)
  {
    if (getLoader() instanceof RunwayClassLoader)
    {
      return ( (RunwayClassLoader) getLoader() ).findResource(name);
    }
    else
    {
      throw new UnsupportedOperationException();
    }
  }

  /**
   * @param name
   * @return
   * @throws IOException
   * @see java.net.URLClassLoader#findResources(java.lang.String)
   */
  public Enumeration<URL> findResources(String name) throws IOException
  {
    if (loader instanceof RunwayClassLoader)
    {
      return ( (RunwayClassLoader) getLoader() ).findResources(name);
    }
    else
    {
      throw new UnsupportedOperationException();
    }
  }

  /**
   * @param name
   * @return
   * @see java.lang.ClassLoader#getResource(java.lang.String)
   */
  public URL getResource(String name)
  {
    return getLoader().getResource(name);
  }

  /**
   * @param name
   * @return
   * @see java.lang.ClassLoader#getResourceAsStream(java.lang.String)
   */
  public InputStream getResourceAsStream(String name)
  {
    return getLoader().getResourceAsStream(name);
  }

  /**
   * @param name
   * @return
   * @throws IOException
   * @see java.lang.ClassLoader#getResources(java.lang.String)
   */
  public Enumeration<URL> getResources(String name) throws IOException
  {
    return getLoader().getResources(name);
  }

  /**
   * @return
   * @see java.net.URLClassLoader#getURLs()
   */
  public URL[] getURLs()
  {
    if (LocalProperties.isReloadableClassesEnabled())
    {
      if (parent instanceof URLClassLoader)
        return ( (URLClassLoader) parent ).getURLs();

      return ( (RunwayClassLoader) getLoader() ).getURLs();
    }
    else
    {
      throw new UnsupportedOperationException();
    }
  }

  /**
   * @param name
   * @param resolve
   * @return
   * @throws ClassNotFoundException
   * @see com.runwaysdk.generation.loader.RunwayClassLoader#loadClass(java.lang.String,
   *      boolean)
   */
  public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException
  {
    if (getLoader() instanceof RunwayClassLoader)
    {
      // This if check prevents an infinite loop.
      if (parent instanceof LoaderManager)
      {
        return ( (RunwayClassLoader) getLoader() ).actualLoad(name, resolve);
      }
      else
      {
        return ( (RunwayClassLoader) getLoader() ).loadClass(name, resolve);
      }
    }
    else
    {
      // The default class loader cannot load arrays...
      if (RunwayClassLoader.arrayPattern.matcher(name).matches())
      {
        return RunwayClassLoader.loadArray(name);
      }
      
      return getLoader().loadClass(name);
    }
  }

  /**
   * @param name
   * @return
   * @throws ClassNotFoundException
   * @see java.lang.ClassLoader#loadClass(java.lang.String)
   */
  public Class<?> loadClass(String name) throws ClassNotFoundException
  {
    return loadClass(name, false);
  }

  /**
   * @param className
   * @param enabled
   * @see java.lang.ClassLoader#setClassAssertionStatus(java.lang.String,
   *      boolean)
   */
  public void setClassAssertionStatus(String className, boolean enabled)
  {
    getLoader().setClassAssertionStatus(className, enabled);
  }

  /**
   * @param enabled
   * @see java.lang.ClassLoader#setDefaultAssertionStatus(boolean)
   */
  public void setDefaultAssertionStatus(boolean enabled)
  {
    getLoader().setDefaultAssertionStatus(enabled);
  }

  /**
   * @param packageName
   * @param enabled
   * @see java.lang.ClassLoader#setPackageAssertionStatus(java.lang.String,
   *      boolean)
   */
  public void setPackageAssertionStatus(String packageName, boolean enabled)
  {
    getLoader().setPackageAssertionStatus(packageName, enabled);
  }

  /**
   * @return
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    return getLoader().toString();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.generation.loader.ReloadableClassLoaderIF#setParent(java.
   * lang.ClassLoader)
   */
  @Override
  public void setParent(ClassLoader newParent)
  {
    this.parent = newParent;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.generation.loader.ReloadableClassLoaderIF#notifyListeners()
   */
  @Override
  public void notifyListeners()
  {
    for (Object o : this.listeners)
    {
      try
      {
        o.getClass().getMethod("onReload").invoke(o);
      }
      catch (NoSuchMethodException n)
      {
        String msg = "Registered reload listener [" + o + "] does not implement onReload()";
        CommonExceptionProcessor.processException(ExceptionConstants.LoaderDecoratorException.getExceptionClass(), msg, n);
      }
      catch (Exception e)
      {
        String msg = "Exception encountered when executing ReloadListeners: " + e.getMessage();
        CommonExceptionProcessor.processException(ExceptionConstants.LoaderDecoratorException.getExceptionClass(), msg, e);
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.generation.loader.ReloadableClassLoaderIF#addListener(java
   * .lang.Object)
   */
  @Override
  public void addListener(Object listener)
  {
    this.listeners.add(listener);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.generation.loader.ReloadableClassLoaderIF#removeListener(
   * java.lang.Object)
   */
  @Override
  public void removeListener(Object listener)
  {
    this.listeners.remove(listener);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.generation.loader.ReloadableClassLoaderIF#loadClassImpl(java
   * .lang.String, boolean)
   */
  @Override
  public Class<?> load(String type, boolean useExProcessor) throws ClassNotFoundException
  {
    if (!LocalProperties.isReloadableClassesEnabled())
    {
      try
      {
        return this.loadClass(type);
      }
      catch (ClassNotFoundException e)
      {
        if (useExProcessor)
        {
          CommonExceptionProcessor.processException(ExceptionConstants.LoaderDecoratorException.getExceptionClass(), e.getMessage(), e);
        }
        else
        {
          throw e;
        }
      }
    }

    try
    {
      // Allows for testing non-reloadable logic in
      // business classes in a development environment
      if (LocalProperties.isDevelopEnvironment())
      {
        return this.parent.loadClass(type);
      }
    }
    catch (ClassNotFoundException e1)
    {
      // Don't panic - we'll try our custom loader
    }

    try
    {
      return this.loadClass(type);
    }
    catch (ClassNotFoundException e)
    {
      // Still don't panic: we might be ignoring the parent for a web
      // environment
      if (! ( this.parent instanceof LoaderManager ))
      {
        if (useExProcessor)
        {
          CommonExceptionProcessor.processException(ExceptionConstants.LoaderDecoratorException.getExceptionClass(), e.getMessage(), e);
        }
        else
        {
          throw e;
        }
      }
    }

    try
    {
      return this.parent.loadClass(type);
    }
    catch (ClassNotFoundException e)
    {
      if (useExProcessor)
      {
        CommonExceptionProcessor.processException(ExceptionConstants.LoaderDecoratorException.getExceptionClass(), e.getMessage(), e);
      }
      else
      {
        throw e;
      }
    }

    return null;
  }

  /**
   * @return a URL array containing the generated bin directories, local bin,
   *         and additional local.classpath entries
   */
  public static URL[] urlArray()
  {
    if (LocalProperties.isReloadableClassesEnabled())
    {
      Set<File> set = new HashSet<File>();
      set.add(new File(LocalProperties.getClientGenBin()));
      set.add(new File(LocalProperties.getCommonGenBin()));
      set.add(new File(LocalProperties.getServerGenBin()));

      String localBin = LocalProperties.getLocalBin();
      if (localBin != null)
      {
        set.add(new File(localBin));
      }

      for (String path : LocalProperties.getLocalClasspath())
      {
        set.add(new File(path));
      }

      File[] array = new File[set.size()];

      return urlArray(set.toArray(array));
    }
    else
    {
      return new URL[] {};

      // We delegate all class loads to the parent if we're
      // not using reloadable classes
      // throw new UnsupportedOperationException();
    }
  }

  public static URL[] urlArray(File... bins)
  {
    try
    {
      List<URL> list = new LinkedList<URL>();

      for (File bin : bins)
      {
        list.add(bin.toURI().toURL());
      }

      return list.toArray(new URL[list.size()]);
    }
    catch (MalformedURLException e)
    {
      throw new RuntimeException(e);
      // throw new LoaderDecoratorException(e.getMessage());
    }
  }

}
