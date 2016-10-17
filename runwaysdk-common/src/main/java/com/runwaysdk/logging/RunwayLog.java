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

import java.net.URL;
import java.util.Date;
import java.util.Enumeration;

import org.apache.commons.logging.Log;
import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.log4j.PropertyConfigurator;

public class RunwayLog implements Log
{
  static
  {
    // set up the default configuration if nothing is already set
    if(!isConfigured())
    {
      setDefaultConfiguration();
    }
  }
  
  private Logger        logger           = null;

  private static String sessionIdKeyName = "Session"; // setting this to
                                                      // "SessionID" or
                                                      // "Session ID" seems to
                                                      // glitch out Chainsaw
  
  public RunwayLog(String name)
  {
    super();

    logger = Logger.getLogger(name);
  }
  
  public RunwayLog(Class<?> clazz)
  {
    super();
    
    logger = Logger.getLogger(clazz);
  }

  /*
   * Util
   */

  String getSessionId()
  {
    // aspect weaving server code in here from LoggingManagement.aj

    return "";
  }

  @SuppressWarnings("unchecked")
  private static boolean isConfigured()
  {
    Enumeration<Appender> appenders = LogManager.getRootLogger().getAllAppenders();
    while(appenders.hasMoreElements())
    {
      if(!(appenders.nextElement() instanceof ConsoleAppender))
      {
        return true;
      }
    }
    
    return false;
  }
  
  private static void setDefaultConfiguration() 
  {
    try
    {
      URL url = RunwayLog.class.getClassLoader().getResource("log4j.properties");
      if(url != null)
      {
        PropertyConfigurator.configure(url);
      }
    }
    catch(Throwable t)
    {
      // Could not initialize default log4j configuration
    }
  }

  private void addSessionIdInfo()
  {
    MDC.put("ReadableTimestamp", (new Date()).toString());
    MDC.put(sessionIdKeyName, getSessionId());
  }

  private void removeSessionIdInfo()
  {
    MDC.remove("ReadableTimestamp");
    MDC.remove(sessionIdKeyName);
  }

  /*
   * Trace
   */

  public void trace(Object arg0)
  {
    try
    {
      addSessionIdInfo();
      logger.trace(arg0);
      removeSessionIdInfo();
    }
    catch (Throwable e)
    {
      System.err.println(e.getLocalizedMessage());
      System.err.println(e.getStackTrace());
    }
  }

  public void trace(Object arg0, Throwable arg1)
  {
    try
    {
      addSessionIdInfo();
      logger.trace(arg0, arg1);
      removeSessionIdInfo();
    }
    catch (Throwable e)
    {
      System.err.println(e.getLocalizedMessage());
      System.err.println(e.getStackTrace());
    }
  }

  public boolean isTraceEnabled()
  {
    return logger.isTraceEnabled();
  }

  /*
   * Debug
   */

  public void debug(Object arg0)
  {
    try
    {
      addSessionIdInfo();
      logger.debug(arg0);
      removeSessionIdInfo();
    }
    catch (Throwable e)
    {
      System.err.println(e.getLocalizedMessage());
      System.err.println(e.getStackTrace());
    }
  }

  public void debug(Object arg0, Throwable arg1)
  {
    try
    {
      addSessionIdInfo();
      logger.debug(arg0, arg1);
      removeSessionIdInfo();
    }
    catch (Throwable e)
    {
      System.err.println(e.getLocalizedMessage());
      System.err.println(e.getStackTrace());
    }
  }

  public boolean isDebugEnabled()
  {
    return logger.isDebugEnabled();
  }

  /*
   * Info
   */

  public void info(Object arg0)
  {
    try
    {
      addSessionIdInfo();
      logger.info(arg0);
      removeSessionIdInfo();
    }
    catch (Throwable e)
    {
      System.err.println(e.getLocalizedMessage());
      System.err.println(e.getStackTrace());
    }
  }

  public void info(Object arg0, Throwable arg1)
  {
    try
    {
      addSessionIdInfo();
      logger.info(arg0, arg1);
      removeSessionIdInfo();
    }
    catch (Throwable e)
    {
      System.err.println(e.getLocalizedMessage());
      System.err.println(e.getStackTrace());
    }
  }

  public boolean isInfoEnabled()
  {
    return logger.isInfoEnabled();
  }

  /*
   * Warn
   */

  public void warn(Object arg0)
  {
    try
    {
      addSessionIdInfo();
      logger.warn(arg0);
      removeSessionIdInfo();
    }
    catch (Throwable e)
    {
      System.err.println(e.getLocalizedMessage());
      System.err.println(e.getStackTrace());
    }
  }

  public void warn(Object arg0, Throwable arg1)
  {
    try
    {
      addSessionIdInfo();
      logger.warn(arg0, arg1);
      removeSessionIdInfo();
    }
    catch (Throwable e)
    {
      System.err.println(e.getLocalizedMessage());
      System.err.println(e.getStackTrace());
    }
  }

  public boolean isWarnEnabled()
  {
    return logger.isEnabledFor(Level.toLevel("WARN"));
  }

  /*
   * Error
   */

  public void error(Object arg0)
  {
    try
    {
      addSessionIdInfo();
      logger.error(arg0);
      removeSessionIdInfo();
    }
    catch (Throwable e)
    {
      System.err.println(e.getLocalizedMessage());
      System.err.println(e.getStackTrace());
    }
  }

  public void error(Object arg0, Throwable arg1)
  {
    try
    {
      addSessionIdInfo();
      logger.error(arg0, arg1);
      removeSessionIdInfo();
    }
    catch (Throwable e)
    {
      System.err.println(e.getLocalizedMessage());
      System.err.println(e.getStackTrace());
    }
  }

  public boolean isErrorEnabled()
  {
    return logger.isEnabledFor(Level.toLevel("ERROR"));
  }

  /*
   * Fatal
   */

  public void fatal(Object arg0)
  {
    try
    {
      addSessionIdInfo();
      logger.fatal(arg0);
      removeSessionIdInfo();
    }
    catch (Throwable e)
    {
      System.err.println(e.getLocalizedMessage());
      System.err.println(e.getStackTrace());
    }
  }

  public void fatal(Object arg0, Throwable arg1)
  {
    try
    {
      addSessionIdInfo();
      logger.fatal(arg0, arg1);
      removeSessionIdInfo();
    }
    catch (Throwable e)
    {
      System.err.println(e.getLocalizedMessage());
      System.err.println(e.getStackTrace());
    }
  }

  public boolean isFatalEnabled()
  {
    return logger.isEnabledFor(Level.toLevel("FATAL"));
  }
}
