package com.runwaysdk.util;

public class ResourceException extends RuntimeException
{

  private static final long serialVersionUID = 5698925280809269974L;

  public ResourceException(String message)
  {
    super(message);
  }
  
  public ResourceException(Throwable t)
  {
    super(t);
  }
  
}
