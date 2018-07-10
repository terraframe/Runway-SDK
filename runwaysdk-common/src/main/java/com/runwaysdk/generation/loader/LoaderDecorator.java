package com.runwaysdk.generation.loader;

public class LoaderDecorator
{

  public static Class<?> load(String name)
  {
    try
    {
      return LoaderDecorator.class.getClassLoader().loadClass(name);
    }
    catch (ClassNotFoundException e)
    {
      throw new RuntimeException(e);
    }
  }

}
