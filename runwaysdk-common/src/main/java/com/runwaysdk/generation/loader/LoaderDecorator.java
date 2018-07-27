package com.runwaysdk.generation.loader;

public class LoaderDecorator
{

  public static Class<?> load(String name)
  {
    try
    {
      ClassLoader classloader = Thread.currentThread().getContextClassLoader();

      // System.out.println("Loading class [" + name + "] with loader [" +
      // classloader.toString() + "]");

      return Class.forName(name, false, classloader);
    }
    catch (ClassNotFoundException e)
    {
      throw new RuntimeException(e);
    }
  }

  public static Class<?> loadClass(String name) throws ClassNotFoundException
  {
    return Class.forName(name, false, Thread.currentThread().getContextClassLoader());
  }

}
