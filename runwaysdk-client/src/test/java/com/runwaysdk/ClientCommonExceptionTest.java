package com.runwaysdk;

import org.junit.Test;

import com.runwaysdk.constants.ExceptionConstants;

public class ClientCommonExceptionTest
{
  String[] exceptions = new String[] {
      ExceptionConstants.AttributeException.getExceptionClass(),
      ExceptionConstants.ConfigurationException.getExceptionClass(),
      ExceptionConstants.ConversionException.getExceptionClass(),
      ExceptionConstants.CoreException.getExceptionClass(),
      ExceptionConstants.ForbiddenMethodException.getExceptionClass(),
      ExceptionConstants.LoaderDecoratorException.getExceptionClass(),
      ExceptionConstants.ProgrammingErrorException.getExceptionClass(),
      ExceptionConstants.SystemException.getExceptionClass()
  };

  @Test
  public void testCommonExceptionProcessor()
  {
    for (String exception : exceptions)
    {
      try
      {
        CommonExceptionProcessor.processException(exception, "Some message, doesn't matter.");
      }
      catch (RunwayExceptionDTO err)
      {
        // Expected
      }
    }
  }
}
