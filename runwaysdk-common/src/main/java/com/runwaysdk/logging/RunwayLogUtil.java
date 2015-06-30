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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class RunwayLogUtil
{
  private static Log log = LogFactory.getLog(RunwayLogUtil.class);
  
  public static void setLogLevel(LogLevel level, Log logger)
  {
    if (logger instanceof Logger)
      ((Logger) logger).setLevel(convertLogLevelToLevel(level));
  }
  
  public static void setLogLevel(LogLevel level, Logger logger)
  {
    logger.setLevel(convertLogLevelToLevel(level));
  }
  
  public static void setLogLevel(LogLevel level)
  {
    setLogLevel(level, log);
  }
  
  public static Level convertLogLevelToLevel(LogLevel level)
  {
    if (level == LogLevel.TRACE)
      return Level.TRACE;
    else if (level == LogLevel.DEBUG)
      return Level.DEBUG;
    else if (level == LogLevel.INFO)
      return Level.INFO;
    else if (level == LogLevel.WARN)
      return Level.WARN;
    else if (level == LogLevel.ERROR)
      return Level.ERROR;
    else if (level == LogLevel.FATAL)
      return Level.FATAL;
    else
      return Level.TRACE;
  }
  
  public static String formatLoggableMessage(String developerMessage, String localizedMessage)
  {
    return "DeveloperMessage: " + developerMessage + "\nLocalizedMessage: " + localizedMessage;
  }
  
  public static String getExceptionLoggableMessage(Throwable e)
  {
    return formatLoggableMessage(e.getMessage(), e.getLocalizedMessage());
  }
  
  public static void logToLevel(LogLevel level, String msg)
  {
    logToLevel(log, level, msg);
  }
  
  public static void logToLevel(LogLevel level, String msg, Throwable t)
  {
    logToLevel(log, level, msg, t);
  }
  
  public static void logToLevel(Logger log, LogLevel level, String msg)
  {
    if (level == LogLevel.TRACE)
    {
      log.trace(msg);
    }
    else if (level == LogLevel.DEBUG)
    {
      log.debug(msg);
    }
    else if (level == LogLevel.INFO)
    {
      log.info(msg);
    }
    else if (level == LogLevel.WARN)
    {
     log.warn(msg); 
    }
    else if (level == LogLevel.ERROR)
    {
      log.error(msg);
    }
    else if (level == LogLevel.FATAL)
    {
      log.fatal(msg);
    }
    else
    {
      log.fatal("RunwayLogUtil.logToLevel was called, but an invalid level was specified. Here is the message we were passed: '" + msg + "'");
    }
  }
  
  public static void logToLevel(Logger log, LogLevel level, String msg, Throwable t)
  {
    if (level == LogLevel.TRACE)
    {
      log.trace(msg, t);
    }
    else if (level == LogLevel.DEBUG)
    {
      log.debug(msg, t);
    }
    else if (level == LogLevel.INFO)
    {
      log.info(msg, t);
    }
    else if (level == LogLevel.WARN)
    {
     log.warn(msg, t); 
    }
    else if (level == LogLevel.ERROR)
    {
      log.error(msg, t);
    }
    else if (level == LogLevel.FATAL)
    {
      log.fatal(msg, t);
    }
    else
    {
      log.fatal("RunwayLogUtil.logToLevel was called, but an invalid level was specified. Here is the message we were passed: '" + msg + "'");
    }
  }
  
  public static void logToLevel(Log log, LogLevel level, String msg)
  {
    if (level == LogLevel.TRACE)
    {
      log.trace(msg);
    }
    else if (level == LogLevel.DEBUG)
    {
      log.debug(msg);
    }
    else if (level == LogLevel.INFO)
    {
      log.info(msg);
    }
    else if (level == LogLevel.WARN)
    {
     log.warn(msg); 
    }
    else if (level == LogLevel.ERROR)
    {
      log.error(msg);
    }
    else if (level == LogLevel.FATAL)
    {
      log.fatal(msg);
    }
    else
    {
      log.fatal("RunwayLogUtil.logToLevel was called, but an invalid level was specified. Here is the message we were passed: '" + msg + "'");
    }
  }
  
  public static void logToLevel(Log log, LogLevel level, String msg, Throwable t)
  {
    if (level == LogLevel.TRACE)
    {
      log.trace(msg, t);
    }
    else if (level == LogLevel.DEBUG)
    {
      log.debug(msg, t);
    }
    else if (level == LogLevel.INFO)
    {
      log.info(msg, t);
    }
    else if (level == LogLevel.WARN)
    {
     log.warn(msg, t); 
    }
    else if (level == LogLevel.ERROR)
    {
      log.error(msg, t);
    }
    else if (level == LogLevel.FATAL)
    {
      log.fatal(msg, t);
    }
    else
    {
      log.fatal("RunwayLogUtil.logToLevel was called, but an invalid level was specified. Here is the message we were passed: '" + msg + "'");
    }
  }
}

/*
private static String formatObjectArrayForOutput(Object[] array)
  {
    String s = "";

    if (array.length == 0)
    {
      s = "[]";
    }
    else
    {
      for (Object o : array)
      {
        s = s + "[" + o.toString() + "]";
      }
    }

    return s;
  }

  public static String formatObjectArrayForOutput(Object[] array, String level, Log logger)
  {
    boolean shouldFormat = false;

    // I wish there was a better way to do this.... I really do.
    if (level == "TRACE")
      shouldFormat = logger.isTraceEnabled();
    else if (level == "DEBUG")
      shouldFormat = logger.isDebugEnabled();
    else if (level == "INFO")
      shouldFormat = logger.isInfoEnabled();
    else if (level == "WARN")
      shouldFormat = logger.isWarnEnabled();
    else if (level == "ERROR")
      shouldFormat = logger.isErrorEnabled();
    else if (level == "FATAL")
      shouldFormat = logger.isFatalEnabled();

    if (shouldFormat)
    {
      return formatObjectArrayForOutput(array);
    }

    return "";
  }

  public static String formatObjectArrayForOutput(Object[] array, boolean shouldFormat)
  {
    if (shouldFormat)
      return formatObjectArrayForOutput(array);

    return "";
  }
  
  public static String safelyExtractMessageFromException(Throwable e)
  {
    String msg;
    
    try
    {
      msg = e.getLocalizedMessage();
    }
    catch(Throwable whateverWhoCares)
    {
      try
      {
        msg = e.getMessage();
      }
      catch(Throwable whateverWhoCares2)
      {
        try
        {
          msg = e.toString();
        }
        catch(Throwable whateverWhoCares3)
        {
          msg = "Unable to find a useable message from exception. (This should never happen)";
        }
      }
    }
    
    return msg;
  }
*/
