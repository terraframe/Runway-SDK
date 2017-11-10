package com.runwaysdk.mvc;

public interface AttributeResponseIF extends ResponseIF
{
  public void set(String name, Object o);

  public Object getAttribute(String name);
}
