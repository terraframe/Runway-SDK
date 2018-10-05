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
package com.runwaysdk;

import java.net.MalformedURLException;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import com.runwaysdk.generation.loader.GeneratedLoader;

public class IsolatedTestRunner extends BlockJUnit4ClassRunner
{
  private ClassLoader loader;

  public IsolatedTestRunner(Class<?> clazz) throws InitializationError, MalformedURLException
  {
    this(GeneratedLoader.createClassLoader(), clazz);
  }

  public IsolatedTestRunner(GeneratedLoader loader, Class<?> clazz) throws InitializationError, MalformedURLException
  {
    super(loadFromCustomClassloader(loader, clazz));

    this.loader = loader;
  }

  // Loads a class in the custom classloader
  private static Class<?> loadFromCustomClassloader(GeneratedLoader loader, Class<?> clazz) throws InitializationError
  {
    loader.setName("Runner");
    
    try
    {
      return Class.forName(clazz.getName(), true, loader);
    }
    catch (ClassNotFoundException e)
    {
      throw new InitializationError(e);
    }
  }

  // Runs junit tests in a separate thread using the custom class loader
  @Override
  public void run(final RunNotifier notifier)
  {
    Runnable runnable = () -> {
      super.run(notifier);
    };
    Thread thread = new Thread(runnable);
    thread.setContextClassLoader(this.loader);
    thread.start();
    try
    {
      thread.join();
    }
    catch (InterruptedException e)
    {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void runChild(FrameworkMethod method, RunNotifier notifier)
  {
    try
    {
      GeneratedLoader loader = GeneratedLoader.createClassLoader(this.loader);
      loader.setName(method.getName());
      
      Thread.currentThread().setContextClassLoader(loader);
    }
    catch (MalformedURLException e)
    {
      e.printStackTrace();
    }
    
    super.runChild(method, notifier);
  }
}