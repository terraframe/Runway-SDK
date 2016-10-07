package com.runwaysdk.mvc;

public interface JsonConfiguration
{

  boolean supports(Class<?> clazz);

  boolean exclude(String name);

}
