package com.runwaysdk.generation.loader;

public class LoaderDecorator
{

  public static Class<?> load(String name)
  {
    try
    {
      return Thread.currentThread().getContextClassLoader().loadClass(name);
    }
    catch (ClassNotFoundException e)
    {
      throw new RuntimeException(e);
    }
  }

  public static Class<?> loadClass(String name) throws ClassNotFoundException
  {
    return Thread.currentThread().getContextClassLoader().loadClass(name);
  }

}
