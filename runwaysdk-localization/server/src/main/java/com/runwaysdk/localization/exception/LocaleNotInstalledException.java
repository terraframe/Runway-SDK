package com.runwaysdk.localization.exception;

public class LocaleNotInstalledException extends LocaleNotInstalledExceptionBase
{
  private static final long serialVersionUID = 875221235;
  
  public LocaleNotInstalledException()
  {
    super();
  }
  
  public LocaleNotInstalledException(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public LocaleNotInstalledException(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public LocaleNotInstalledException(java.lang.Throwable cause)
  {
    super(cause);
  }
  
}
