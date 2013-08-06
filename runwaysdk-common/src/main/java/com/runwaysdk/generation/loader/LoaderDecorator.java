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

/**
 * Some web environments require access to a {@link ClassLoader} instance, but
 * don't provide an API to drop or change it. As a solution, we use a Decorator,
 * which extends {@link ClassLoader} but delegates all behavior to a private
 * instance (of {@link RunwayClassLoader} in this case) that it maintains. Thus
 * we can drop or change the hidden instance without having to change the
 * reference to the Decorator.
 * 
 * @author Eric
 */
public class LoaderDecorator extends URLClassLoader
{
  /**
   * The current loader
   */
  private ClassLoader loader;

  /**
   * Web environments often will switch out the parent loader. We keep track of
   * the current parent so that reloads of the RunwayClassLoader will be able to
   * delegate correctly.
   */
  private ClassLoader       parent;

  /**
   * A list of listeners to be notified when reload() is called.
   */
  private List<Object>      listeners;

  /**
   * Sets up the Decorator, including the default class loader
   */
  private LoaderDecorator()
  {
    super(urlArray());
    parent = ComponentInfo.class.getClassLoader();
    listeners = new LinkedList<Object>();
    newLoader();
  }

  /**
   * @return a URL array containing the generated bin directories, local bin, and additional local.classpath entries
   */
  private static URL[] urlArray()
  {
    if (LocalProperties.isReloadableClassesEnabled()) {
      Set<File> set = new HashSet<File>();
      set.add(new File(LocalProperties.getClientBin()));
      set.add(new File(LocalProperties.getCommonBin()));
      set.add(new File(LocalProperties.getServerBin()));
      set.add(new File(LocalProperties.getLocalBin()));
      for (String path : LocalProperties.getLocalClasspath())
      {
        set.add(new File(path));
      }
  
      File[] array = new File[set.size()];
      return LoaderDecorator.urlArray(set.toArray(array));
    }
    else {
      return new URL[]{}; // We delegate all class loads to the parent if we're not using reloadable classes
    }
  }

  static URL[] urlArray(File... bins)
  {
    try
    {
      List<URL> list = new LinkedList<URL>();
      
      for(File bin : bins)
      {
        list.add(bin.toURI().toURL());        
      }
      
      return list.toArray(new URL[list.size()]);
    }
    catch (MalformedURLException e)
    {
      throw new RuntimeException(e);
//      throw new LoaderDecoratorException(e.getMessage());
    }
  }

  /**
   * Creates a new ClassLoader (orphaning the old one).
   * 
   * @return
   * @throws MalformedURLException
   */
  private void newLoader()
  {
    if (LocalProperties.isReloadableClassesEnabled()) {
      loader = new RunwayClassLoader(urlArray(), parent);
    }
    else {
      loader = this.getParent();
    }
  }

  void setLoader(RunwayClassLoader loader)
  {
    this.loader = loader;
  }

  /**
   * A holder class for access to the singleton. Allows for lazy instantiation
   * and thread safety because the class is not loaded until the first access to
   * INSTANCE.
   */
  private static class Singleton
  {
    private static final LoaderDecorator INSTANCE = new LoaderDecorator();
  }

  /**
   * A simple accessor to the Singleton Decorator instance
   * 
   * @return the singleton instance of LoaderDecorator
   */
  public static LoaderDecorator instance()
  {
    return Singleton.INSTANCE;
  }

  /**
   * Sets the parent loader for all new instances of {@link RunwayClassLoader}.
   * Calls {@link #reload()}, because a change in the parent will corrupt the
   * heirarchy of currently loaded classes.
   * 
   * @param newParent
   *          The new parent {@link ClassLoader}
   */
  public static void setParentLoader(ClassLoader newParent)
  {
    LockHolder.lock(instance().loader);

    try
    {
      instance().parent = newParent;
      reload();
    }
    finally
    {
      LockHolder.unlock();
    }
  }

  /**
   * Creates a new ClassLoader (orphaning the old one).
   */
  public static void reload()
  {
    LockHolder.lock(instance().loader);

    try
    {
      instance().newLoader();
      notifyListeners();
    }
    finally
    {
      LockHolder.unlock();
    }
  }

  /**
   * Notifies all registered listeners of the reload.
   */
  private static void notifyListeners()
  {
    for (Object o : instance().listeners)
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

  /**
   * Adds an observer who will be notified when {@link #reload()} is called
   * 
   * @param o
   */
  public static void addListener(Object listener)
  {
    instance().listeners.add(listener);
  }

  /**
   * Remove an observer, ceasing notification of {@link #reload()} calls
   * 
   * @param o
   */
  public static void deleteListener(Object listener)
  {
    instance().listeners.remove(listener);
  }

  /**
   * Gets the class that represents the specified type.
   * 
   * @param type
   *          Fully qualified type to load
   * @return Class specified by the type name
   */
  public static Class<?> load(String type)
  {
    try
    {
      // Allows for testing non-reloadable logic in
      // business classes in a development environment
      if (LocalProperties.isDevelopEnvironment())
        return instance().parent.loadClass(type);
    }
    catch (ClassNotFoundException e1)
    {
      // Don't panic - we'll try our custom loader
    }

    try
    {
      return instance().loadClass(type);
    }
    catch (ClassNotFoundException e)
    {
      // Still don't panic: we might be ignoring the parent for a web
      // environment
      if (! ( instance().parent instanceof LoaderManager ))
      {
        CommonExceptionProcessor.processException(ExceptionConstants.LoaderDecoratorException.getExceptionClass(), e.getMessage(), e);
      }
    }

    try
    {
      return instance().parent.loadClass(type);
    }
    catch (ClassNotFoundException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.LoaderDecoratorException.getExceptionClass(), e.getMessage(), e);
    }

    return null;
  }

  /**
   * 
   * @see java.lang.ClassLoader#clearAssertionStatus()
   */
  public void clearAssertionStatus()
  {
    loader.clearAssertionStatus();
  }

  /**
   * @param obj
   * @return
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object obj)
  {
    return loader.equals(obj);
  }

  /**
   * @param name
   * @return
   * @see java.net.URLClassLoader#findResource(java.lang.String)
   */
  public URL findResource(String name)
  {
    if (loader instanceof RunwayClassLoader) {
      return ((RunwayClassLoader)loader).findResource(name);
    }
    else {
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
    if (loader instanceof RunwayClassLoader) {
      return ((RunwayClassLoader)loader).findResources(name);
    }
    else {
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
    return loader.getResource(name);
  }

  /**
   * @param name
   * @return
   * @see java.lang.ClassLoader#getResourceAsStream(java.lang.String)
   */
  public InputStream getResourceAsStream(String name)
  {
    return loader.getResourceAsStream(name);
  }

  /**
   * @param name
   * @return
   * @throws IOException
   * @see java.lang.ClassLoader#getResources(java.lang.String)
   */
  public Enumeration<URL> getResources(String name) throws IOException
  {
    return loader.getResources(name);
  }

  /**
   * @return
   * @see java.net.URLClassLoader#getURLs()
   */
  public URL[] getURLs()
  {
    if (parent instanceof URLClassLoader)
      return ( (URLClassLoader) parent ).getURLs();
    
    if (loader instanceof RunwayClassLoader) {
      return ((RunwayClassLoader)loader).getURLs();
    }
    else {
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
    if (loader instanceof RunwayClassLoader) {
      if (parent instanceof LoaderManager)
      {
        return ((RunwayClassLoader)loader).actualLoad(name, resolve);
      }
      else
      {
        return ((RunwayClassLoader)loader).loadClass(name, resolve);
      }
    }
    else {
      return loader.loadClass(name);
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
    loader.setClassAssertionStatus(className, enabled);
  }

  /**
   * @param enabled
   * @see java.lang.ClassLoader#setDefaultAssertionStatus(boolean)
   */
  public void setDefaultAssertionStatus(boolean enabled)
  {
    loader.setDefaultAssertionStatus(enabled);
  }

  /**
   * @param packageName
   * @param enabled
   * @see java.lang.ClassLoader#setPackageAssertionStatus(java.lang.String,
   *      boolean)
   */
  public void setPackageAssertionStatus(String packageName, boolean enabled)
  {
    loader.setPackageAssertionStatus(packageName, enabled);
  }

  /**
   * @return
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    return loader.toString();
  }
}
