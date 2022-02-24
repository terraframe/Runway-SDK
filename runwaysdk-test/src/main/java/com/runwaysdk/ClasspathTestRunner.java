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
package com.runwaysdk;

import java.net.MalformedURLException;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import com.runwaysdk.generation.loader.GeneratedLoader;

public class ClasspathTestRunner extends BlockJUnit4ClassRunner
{
  private ClassLoader loader;

  public ClasspathTestRunner(Class<?> clazz) throws InitializationError, MalformedURLException
  {
    this(GeneratedLoader.createClassLoader(), clazz);
  }

  public ClasspathTestRunner(GeneratedLoader loader, Class<?> clazz) throws InitializationError, MalformedURLException
  {
    super(loadFromCustomClassloader(loader, clazz));

    this.loader = loader;
  }

  // Loads a class in the custom classloader
  private static Class<?> loadFromCustomClassloader(ClassLoader loader, Class<?> clazz) throws InitializationError
  {
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
}