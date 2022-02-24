/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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

import java.io.File;

import org.aspectj.lang.annotation.SuppressAjWarnings;

import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.session.AbstractRequestManagement;

public aspect CommonExceptions
{
//  protected pointcut commonExceptionOnServer(String runwayExceptionType, String developerMessage) :
//    cflow(execution(@com.runwaysdk.session.StartRequest * *+.*(..))) &&
//    execution(* com.runwaysdk.CommonExceptionProcessor.*(..)) && args(String String);

//  Object around(String runwayExceptionType, String developerMessage) :
//      (cflow(AbstractRequestManagement.allRequestEntryPoints()) &&
//      execution(* com.runwaysdk.CommonExceptionProcessor.processException(String, String)) && args(runwayExceptionType, developerMessage))
//  {
//    Class runwayExceptionClass = LoaderDecorator.load(runwayExceptionType);
//
//    RunwayException runwayException;
//
//    try
//    {
//      runwayException = (RunwayException)runwayExceptionClass.getConstructor(String.class).
//        newInstance(developerMessage);
//    }
//    catch (Exception e)
//    {
//      // This is one of the few times in which it is acceptable to throw a runtime
//      // exception in Runway.  This indicates a problem with the common
//      // exception mechanism.
//      e.printStackTrace();
//      throw new RuntimeException(e);
//    }
//    throw runwayException;
//  }

  @SuppressAjWarnings({"adviceDidNotMatch, unchecked"})
  Object around(String runwayExceptionType, String developerMessage, Throwable throwable) :
    (cflow(AbstractRequestManagement.allRequestEntryPoints()) &&
    execution(* com.runwaysdk.CommonExceptionProcessor.processException(String, String, Throwable)) && args(runwayExceptionType, developerMessage, throwable))
  {
    Class<?> runwayExceptionClass = LoaderDecorator.load(runwayExceptionType);

    RunwayException runwayException;

    try
    {
      runwayException = (RunwayException)runwayExceptionClass.getConstructor(String.class, Throwable.class).
        newInstance(developerMessage, throwable);
    }
    catch (Exception e)
    {
      // This is one of the few times in which it is acceptable to throw a runtime
      // exception in Runway.  This indicates a problem with the common
      // exception mechanism.
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    throw runwayException;
  }

  @SuppressAjWarnings({"adviceDidNotMatch, unchecked"})
  Object around(File file, Throwable throwable) :
    (cflow(AbstractRequestManagement.allRequestEntryPoints()) &&
    execution(* com.runwaysdk.CommonExceptionProcessor.fileWriteException(File, Throwable)) && args(file, throwable))
  {
    Class<?> runwayExceptionClass = LoaderDecorator.load(ExceptionConstants.FileWriteException.getExceptionClass());

    RunwayException runwayException;

    try
    {
      runwayException = (RunwayException)runwayExceptionClass.getConstructor(File.class, Throwable.class).
        newInstance(file, throwable);
    }
    catch (Exception e)
    {
      // This is one of the few times in which it is acceptable to throw a runtime
      // exception in Runway.  This indicates a problem with the common
      // exception mechanism.
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    throw runwayException;
  }

// String runwayExceptionType, String developerMessage
}
