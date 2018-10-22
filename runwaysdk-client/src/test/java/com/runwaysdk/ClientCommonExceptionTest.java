/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk;

import org.junit.Assert;
import org.junit.Test;

import com.runwaysdk.constants.ExceptionConstants;

public class ClientCommonExceptionTest
{
  String[] exceptions = new String[] { ExceptionConstants.AttributeException.getExceptionClass(), ExceptionConstants.ConfigurationException.getExceptionClass(), ExceptionConstants.ConversionException.getExceptionClass(), ExceptionConstants.CoreException.getExceptionClass(), ExceptionConstants.ForbiddenMethodException.getExceptionClass(), ExceptionConstants.LoaderDecoratorException.getExceptionClass(), ExceptionConstants.ProgrammingErrorException.getExceptionClass(), ExceptionConstants.SystemException.getExceptionClass() };

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
