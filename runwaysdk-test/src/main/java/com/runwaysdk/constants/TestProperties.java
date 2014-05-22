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
package com.runwaysdk.constants;

import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.configuration.ConfigurationManager.ConfigGroup;
import com.runwaysdk.configuration.ConfigurationReaderIF;




/**
 * Convenience class that allows easy access to the test.properties file.
 * 
 * @author Eric
 */
public class TestProperties
{
  /**
   * The test.properties configuration file
   */
  private ConfigurationReaderIF props;
  
  /**
   * Private constructor loads the test.properties configuration
   */
  private TestProperties()
  {
    props = ConfigurationManager.getReader(ConfigGroup.TEST, "test.properties");
  }
  
  /**
   * A holder class for access to the singleton. Allows for lazy instantiation and thread
   * safety because the class is not loaded until the first access to INSTANCE.
   */
  private static class Singleton
  {
    private static final TestProperties INSTANCE = new TestProperties();
  }
  
  public static int getAppLockTreadNumber()
  {
    return Singleton.INSTANCE.props.getInteger("appLock.numThreads");
  }
  
  public static int getUserLockTreadNumber()
  {
    return Singleton.INSTANCE.props.getInteger("userLock.numThreads");
  }
  
  public static int getUserLockModCacheTreadNumber()
  {
    return Singleton.INSTANCE.props.getInteger("userLock.modCache.numThreads");
  }

  public static int getUserLockModTypeTreadNumber()
  {
    return Singleton.INSTANCE.props.getInteger("userLock.modType.numThreads");
  }
  
  public static int getUserLockModPublicPermissionsTreadNumber()
  {
    return Singleton.INSTANCE.props.getInteger("userLock.modPublicPermissions.numThreads");
  }

  public static int getUserLockModRolePermissionsTreadNumber()
  {
    return Singleton.INSTANCE.props.getInteger("userLock.modRolePermissions.numThreads");
  }
  
  public static int getUserLockModOwnerPermissionsTreadNumber()
  {
    return Singleton.INSTANCE.props.getInteger("userLock.modOwnerPermissions.numThreads");
  }
  
  public static int getUserRoleMethodExecutePermissions()
  {
    return Singleton.INSTANCE.props.getInteger("userLock.modRoleMethodExecutePermissions.numThreads");
  }
  
  public static int getMethodCreatePermissions()
  {
    return Singleton.INSTANCE.props.getInteger("userLock.modMethodCreatePermissions.numThreads");
  }

  public static int getRelLockCardinalityThreadNumber()
  {
    return Singleton.INSTANCE.props.getInteger("relLock.cardinality.numThreads");
  }
  
  public static int getSeleniumPort()
  {
    return Singleton.INSTANCE.props.getInteger("selenium.port");
  }
  
  public static String getSeleniumServer()
  {
    return Singleton.INSTANCE.props.getString("selenium.server");
  }
  
  public static String getWebtestAddress()
  {
    return Singleton.INSTANCE.props.getString("webtest.address");
  }

  public static String getWebtestWebapp()
  {
    return Singleton.INSTANCE.props.getString("webtest.webapp");
  }  

  public static boolean getMockWebServiceTests()
  {
    return Boolean.parseBoolean(Singleton.INSTANCE.props.getString("mockWebServiceTests"));
  }
  
  public static boolean getWebServiceTests()
  {
    return Boolean.parseBoolean(Singleton.INSTANCE.props.getString("webServiceTests"));
  }

  public static String[] getSeleniumBrowser()
  {
    String browsersString = Singleton.INSTANCE.props.getString("selenium.browser");
    
    String[] browsers = browsersString.split(",");
    for(int i=0; i<browsers.length; i++)
    {
      browsers[i] = browsers[i].trim();
    }
    return browsers;
  }
  
  public static String getWebTestClientBin()
  {
    return Singleton.INSTANCE.props.getString("webtest.client.bin");
  }
  
  public static String getWebTestCommonBin()
  {
    return Singleton.INSTANCE.props.getString("webtest.common.bin");
  }
  
  public static String getWebTestServerBin()
  {
    return Singleton.INSTANCE.props.getString("webtest.server.bin");
  }
}
