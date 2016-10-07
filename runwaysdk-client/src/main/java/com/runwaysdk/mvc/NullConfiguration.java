package com.runwaysdk.mvc;

public class NullConfiguration implements JsonConfiguration
{

  @Override
  public boolean supports(Class<?> clazz)
  {
    return false;
  }

  @Override
  public boolean exclude(String name)
  {
    return false;
  }

}
