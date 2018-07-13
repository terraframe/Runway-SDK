package com.runwaysdk;

import java.net.MalformedURLException;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import com.runwaysdk.generation.loader.GeneratedLoader;

public class ClasspathTestRunner extends BlockJUnit4ClassRunner
{
  static ClassLoader customClassLoader;

  public ClasspathTestRunner(Class<?> clazz) throws InitializationError
  {
    super(loadFromCustomClassloader(clazz));
  }

  // Loads a class in the custom classloader
  private static Class<?> loadFromCustomClassloader(Class<?> clazz) throws InitializationError
  {
    try
    {
      // Only load once to support parallel tests
      if (customClassLoader == null)
      {
        customClassLoader = GeneratedLoader.createClassLoader();
      }

      return Class.forName(clazz.getName(), true, customClassLoader);
    }
    catch (ClassNotFoundException | MalformedURLException e)
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
    thread.setContextClassLoader(customClassLoader);
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