package com.runwaysdk;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.io.RunwayClasspathEntityResolver;
import com.runwaysdk.session.Request;

public class ServerCommonExceptionTest
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
  
//  @Test
//  public void testNotInRequest()
//  {
//    for (String exception : exceptions)
//    {
//      try
//      {
//        CommonExceptionProcessor.processException(exception,"Some message, doesn't matter.");
//      }
//      catch (RunwayException err)
//      {
//        // Expected
//      }
//      catch (RunwayExceptionDTO dto) {
//        throw new RuntimeException("A DTO was thrown when processing [" + exception + "].", dto);
//      }
//    }
//  }
//
//  @Test
//  @Request
//  public void testInRequest()
//  {
//    for (String exception : exceptions)
//    {
//      try
//      {
//        CommonExceptionProcessor.processException(
//            exception,
//            "Some message, doesn't matter.");
//      }
//      catch (RunwayException err)
//      {
//        // Expected
//      }
//      catch (RunwayExceptionDTO dto) {
//        throw new RuntimeException("A DTO was thrown when processing [" + exception + "].", dto);
//      }
//    }
//  }
}
