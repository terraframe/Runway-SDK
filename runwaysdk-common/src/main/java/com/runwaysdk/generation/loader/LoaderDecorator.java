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

public class LoaderDecorator
{
  private static volatile LoaderDecorator instance;

  private final ReloadableClassLoaderIF   loader;

  private LoaderDecorator()
  {
    this(new DelegatingClassLoader());
  }

  private LoaderDecorator(ReloadableClassLoaderIF loader)
  {
    this.loader = loader;
  }

  /**
   * A simple accessor to the Singleton Decorator instance
   * 
   * @return the singleton instance of LoaderDecorator
   */
  public static synchronized ReloadableClassLoaderIF instance()
  {
    if (instance == null)
    {
      instance = new LoaderDecorator();
    }

    return instance.loader;
  }

  public static synchronized void setClassLoader(ReloadableClassLoaderIF loader)
  {
    if (instance == null)
    {
      instance = new LoaderDecorator(loader);
    }
    else
    {
      throw new RuntimeException("The class loader cannot be set once it has already been initialized");
    }
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
    LockHolder.lock(instance());

    try
    {
      instance().setParent(newParent);
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
    LockHolder.lock(instance());

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
    instance().notifyListeners();
  }

  /**
   * Adds an observer who will be notified when {@link #reload()} is called
   * 
   * @param o
   */
  public static void addListener(Object listener)
  {
    instance().addListener(listener);
  }

  /**
   * Remove an observer, ceasing notification of {@link #reload()} calls
   * 
   * @param o
   */
  public static void deleteListener(Object listener)
  {
    instance().removeListener(listener);
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
      return instance().load(type, true);
    }
    catch (ClassNotFoundException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Tries to load the class. If a problem happens it does NOT call the
   * CommonExceptionProcessor, it just throws the exception.
   * 
   * @param type
   */
  public static Class<?> loadClassNoCommonExceptionProcessor(String type) throws ClassNotFoundException
  {
    return instance().load(type, false);
  }

  /**
   * Gets the class that represents the specified type. Loads directly from the
   * underlying ClassLoader bypassing any sort of managed delegation.
   * 
   * @param type
   *          Fully qualified type to load
   * @return Class specified by the type name
   * @throws ClassNotFoundException
   */
  public static Class<?> loadClass(String type) throws ClassNotFoundException
  {
    return instance().loadClass(type);
  }

  /**
   * Gets the class that represents the specified type. Loads directly from the
   * underlying ClassLoader bypassing any sort of managed delegation.
   * 
   * @param type
   *          Fully qualified type to load
   * @return Class specified by the type name
   * @throws ClassNotFoundException
   */
  public static Class<?> loadClass(String type, boolean resolve) throws ClassNotFoundException
  {
    return instance().loadClass(type, resolve);
  }
}