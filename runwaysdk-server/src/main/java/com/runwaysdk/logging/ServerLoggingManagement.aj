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
package com.runwaysdk.logging;

import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.SuppressAjWarnings;

import com.runwaysdk.session.AbstractRequestManagement;
import com.runwaysdk.session.Session;
import com.runwaysdk.session.SessionIF;

public aspect ServerLoggingManagement
{
  @SuppressAjWarnings({"adviceDidNotMatch"})
  Object around() : (cflow(AbstractRequestManagement.allRequestEntryPoints()) && execution(* com.runwaysdk.logging.RunwayLog.getSessionId(..)))
  {
    SessionIF session = Session.getCurrentSession();

    if (session != null)
    {
      return session.getId();
    }

    return proceed();
  }

  Object around() : execution(* com.runwaysdk.facade.Facade.*(..))
  {
    String fullyQualifiedMethodName = thisJoinPointStaticPart.getSignature().getDeclaringTypeName() + "."
    + thisJoinPointStaticPart.getSignature().getName();

    String loggerName = thisJoinPointStaticPart.getSignature().getDeclaringTypeName();

    org.apache.commons.logging.Log log = loggingHelperMethod(thisJoinPoint.getArgs(), loggerName, fullyQualifiedMethodName, LogLevel.DEBUG );

    Object returnObject = proceed();

    RunwayLogUtil.logToLevel(log, LogLevel.TRACE, "Exiting method [" + fullyQualifiedMethodName + "] and returning object [" + returnObject + "].");

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
