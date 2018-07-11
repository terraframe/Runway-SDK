package com.runwaysdk.generation.loader;

import com.runwaysdk.constants.TypeGeneratorInfo;

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

  public static Class<?> loadClassNoCommonExceptionProcessor(String name)
  {
    return load(name.replaceAll(TypeGeneratorInfo.DTO_SUFFIX, ""));
  }

}
