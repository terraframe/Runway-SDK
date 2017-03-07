package com.runwaysdk;

public class StopTransactionException extends RuntimeException
{
  private static final long serialVersionUID = 158466258123L;

  private boolean doNotLog = false;
  
  public StopTransactionException()
  {
    super();
  }
  
  public StopTransactionException(boolean doNotLog)
  {
    super();
    
    this.doNotLog = doNotLog;
  }
  
  public boolean shouldLog()
  {
    return !this.doNotLog;
  }
}
