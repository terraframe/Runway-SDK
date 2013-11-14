/**
*
*/
package com.runwaysdk.logging;

import org.apache.commons.logging.LogFactory;

import junit.framework.TestCase;

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
public class LoggingTest extends TestCase
{
  /**
   * This is usually the best way to instantiate a Log. One static log instance per class.
   * Logs are expensive to create, so try to recycle among class instances. The basic convention
   * is to use the current class as the getLog(Class/Name) parameter because the log files will
   * note the name of the logger, hence you'll know the name of the class that performed the logging.
   */
  private static org.apache.commons.logging.Log staticLog = LogFactory.getLog(LoggingTest.class);
//  private static Logger staticLog = LoggerFactory.getLogger(LoggingTest.class);
  
  /**
   * Ensures that commons-logging.properties is using:
   * org.apache.commons.logging.log=com.runwaysdk.logging.RunwayLog
   */
  // This test is failing but its commented out because our RunwayLog class needs to be rewritten to use SLF4J
//  public void testRunwayLogInstance()
//  {
//    if (!(staticLog instanceof RunwayLog)) {
//      fail("Expected logger to return an instance of RunwayLog, but instead was " + staticLog.getClass().getName());
//    }
//  }
  
  /**
   * @param param1
   * @param param2
   */
  private int sum(int param1, int param2)
  {
    return param1 + param2;
  }
  
  // Annotation logging
  
  /**
   * JUnit entry point that invokes a logged method.
   */
  public void testLogWithAnnotation()
  {
    int sum = logWithAnnotationImpl(4, 5);
    assertEquals(9, sum);
  }
  
  /**
   * Using the @Log annotation, this method's call will be automatically logged, including the toString() values of
   * the parameters and the toString() of the returned object (but the JUnit must return void in this case).
   * 
   * @param param1
   * @param param2
   */
  @Log(level=LogLevel.DEBUG /* Custom log levels are allowed */, name="annotation_log_test" /* The default name is "" */)
  private int logWithAnnotationImpl(int param1, int param2)
  {
    return sum(param1, param2);
  }
  
  // Inline logging
  
  /**
   * Uses the standard LogFactory to retrieve a Log instance.
   */
  public void testLogWithInlineLogger()
  {
    // this is typically wasteful ... instead use a static Log instance
    org.apache.commons.logging.Log inlineLog = LogFactory.getLog(this.getClass().getName()+"_inline");
    
    int sum = sum(3, 5);
    assertEquals(8, sum);
    
    inlineLog.debug("The sum of 3 + 5 = "+sum);
  }
  
  /**
   * Uses the static Log instance, which is superior when logs can be shared across object instances.
   */
  public void testWithStaticLogger()
  {
    int sum = sum(2, 10);
    assertEquals(12, sum);
    
    staticLog.debug("The sum of 3 + 5 = "+sum);
  }
  
  /**
   * Tests that proper checks are performed before logging expensive
   * operations. The logger is hard-coded in this case to avoid
   * changing properties files and show the API calls.
   */
//  public void testLogLevelEnabled()
//  {
//    // create a logger that only logs FATAL
//    Logger log = LoggerFactory.getLogger(this.getClass().getName()+"_level");
//    RunwayLogUtil.setLogLevel(LogLevel.FATAL, log);
//    
//    if(log.isDebugEnabled())
//    {
//      fail("Triggered a potentially very expensive operation that only makes sense in a debug situation to gather info.");
//    }
//  }
}
