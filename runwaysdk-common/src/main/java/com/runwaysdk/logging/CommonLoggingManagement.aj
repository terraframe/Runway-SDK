/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
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
 ******************************************************************************/
package com.runwaysdk.logging;

import org.apache.commons.logging.LogFactory;

/**
 * This aspect weaves logging code into all methods defined in the facade class
 * and methods annotated with @Log
 *
 * @author Richard Rowlands
 */

public aspect CommonLoggingManagement
{
  Object around(com.runwaysdk.logging.Log logAnn) : (execution(* *(..)) && @annotation(logAnn))
  {
    String fullyQualifiedMethodName = thisJoinPointStaticPart.getSignature().getDeclaringTypeName() + "."
    + thisJoinPointStaticPart.getSignature().getName();

    String loggerName = logAnn.name();
    if (logAnn.name().equals(""))
    {
      loggerName = thisJoinPointStaticPart.getSignature().getDeclaringTypeName();
    }

    org.apache.commons.logging.Log log = loggingHelperMethod(thisJoinPoint.getArgs(), loggerName, fullyQualifiedMethodName, logAnn.level() );

    Object returnObject = proceed(logAnn);

    RunwayLogUtil.logToLevel(log, logAnn.level(), "Exiting method [" + fullyQualifiedMethodName + "] and returning object [" + returnObject + "].");

    return returnObject;
  }

  public static org.apache.commons.logging.Log loggingHelperMethod(Object[] aArgs, String loggerName, String fullyQualifiedMethodName, LogLevel level)
  {
    String sArgs = "";

    int index = 1;
    for (Object oArg : aArgs)
    {
      sArgs += "[" + index + " : ";

      if (oArg != null)
      {
        sArgs += oArg.toString();
      }
      else
      {
        sArgs += "null";
      }

      sArgs += "]";

      index++;
    }
    if (aArgs.length == 0)
      sArgs = "[]";

    org.apache.commons.logging.Log log = LogFactory.getLog(loggerName);

    RunwayLogUtil.logToLevel(log, level, "Entering method [" + fullyQualifiedMethodName + "] with arguments " + sArgs + ".");

    return log;
  }
}


/*
 * This now scrapped aspect attempts to intelligently weave logging code into most all catch statements.
 * The problem is, how do you intelligently do that? By adding a million !withincode's? No bueno.
 *
private org.apache.commons.logging.Log throwableHandleLogger = LogFactory.getLog(LoggingManagement.class);
before(Throwable e) : (
                        (within(com.runwaysdk..*) && handler(Throwable))
                        || handler(com.runwaysdk..*+)
                       )
                       && !withincode(* test*(..))
                       && !within(com.runwaysdk.session.IntegratedSessionTest)
                       && !within(com.runwaysdk.logging.RunwayLog)
                       && !within(com.runwaysdk.logging.LoggingManagement)
                       && !withincode(* com.runwaysdk.dataaccess.metadata.MdAttributeLocalDAO.findAttributeValueMatch(..))
                       && args(e)
{
  String msg = e.getLocalizedMessage();

  MDC.put("MDCClass", thisJoinPointStaticPart.getSignature().getDeclaringTypeName());
  MDC.put("MDCMethod", thisJoinPointStaticPart.getSignature().getName());
  MDC.put("MDCLine", thisJoinPointStaticPart.getSourceLocation().getLine());
  MDC.put("MDCFile", thisJoinPointStaticPart.getSourceLocation().getFileName());

  // Errors caught in AbstractRequestManagement are more serious, so log them at Error, else log it at debug (unless otherwise specified by the throwable)
  LogLevel defaultLevel = LogLevel.DEBUG;
  if (thisJoinPointStaticPart.getSignature().getDeclaringTypeName().equals("AbstractRequestManagement"))
  {
    defaultLevel = LogLevel.ERROR;
  }

  if (e instanceof Loggable)
  {
    Loggable loggable = (Loggable) e;

    LogLevel level = loggable.getLogLevel();
    if (level == null)
      level = defaultLevel;

    RunwayLogUtil.logToLevel(throwableHandleLogger, level, msg, e);
  }
  else
  {
    RunwayLogUtil.logToLevel(throwableHandleLogger, defaultLevel, msg, e);
  }

  MDC.remove("MDCClass");
  MDC.remove("MDCMethod");
  MDC.remove("MDCLine");
  MDC.remove("MDCFile");
}
*/
