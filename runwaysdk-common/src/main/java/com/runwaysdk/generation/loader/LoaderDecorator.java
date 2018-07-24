package com.runwaysdk.generation.loader;

public class LoaderDecorator
{

  public static Class<?> load(String name)
  {
    try
    {
      ClassLoader classloader = Thread.currentThread().getContextClassLoader();

//      System.out.println("Loading class [" + name + "] with loader [" + classloader.toString() + "]");

      return classloader.loadClass(name);
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
