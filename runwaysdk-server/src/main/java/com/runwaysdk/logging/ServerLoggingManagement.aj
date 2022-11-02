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
package com.runwaysdk.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public aspect ServerLoggingManagement
{
  // @SuppressAjWarnings({ "adviceDidNotMatch" })
  // Object around() : (cflow(AbstractRequestManagement.allRequestEntryPoints())
  // && execution(* com.runwaysdk.logging.RunwayLog.getSessionId(..)))
  // {
  // SessionIF session = Session.getCurrentSession();
  //
  // if (session != null)
  // {
  // return session.getOid();
  // }
  //
  // return proceed();
  // }

  Object around() : execution(* com.runwaysdk.facade.Facade.*(..))
  {
    String fullyQualifiedMethodName = thisJoinPointStaticPart.getSignature().getDeclaringTypeName() + "." + thisJoinPointStaticPart.getSignature().getName();

    String loggerName = thisJoinPointStaticPart.getSignature().getDeclaringTypeName();

    Logger log = loggingHelperMethod(thisJoinPoint.getArgs(), loggerName, fullyQualifiedMethodName);

    Object returnObject = proceed();

    log.trace("Exiting method [" + fullyQualifiedMethodName + "] and returning object [" + returnObject + "].");

    return returnObject;
  }

  public static Logger loggingHelperMethod(Object[] aArgs, String loggerName, String fullyQualifiedMethodName)
  {
    StringBuilder sArgs = new StringBuilder();

    int index = 1;
    for (Object oArg : aArgs)
    {
      sArgs.append("[" + index + " : ");

      if (oArg != null)
      {
        sArgs.append(oArg.toString());
      }
      else
      {
        sArgs.append("null");
      }

      sArgs.append("]");

      index++;
    }

    if (aArgs.length == 0)
    {
      sArgs = new StringBuilder("[]");
    }

    Logger log = LoggerFactory.getLogger(loggerName);

    String msg = "Entering method [" + fullyQualifiedMethodName + "] with arguments " + sArgs.toString() + ".";

    log.trace(msg);

    return log;
  }
}
