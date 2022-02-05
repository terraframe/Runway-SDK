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
package com.runwaysdk.constants;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.configuration.ConfigurationManager.ConfigGroup;
import com.runwaysdk.configuration.ConfigurationReaderIF;
import com.runwaysdk.configuration.LegacyPropertiesSupport;


public class DeployProperties
{
  @SuppressWarnings("unused")
  private static final Logger logger = LoggerFactory.getLogger(DeployProperties.class);
  
  private ConfigurationReaderIF props;
  
  private DeployProperties()
  {
    props = ConfigurationManager.getReader(ConfigGroup.COMMON, LegacyPropertiesSupport.pickRelevant("deploy.properties", "common.properties"));
  }
  
  /**
   * A holder class for access to the singleton. Allows for lazy instantiation and thread
   * safety because the class is not loaded until the first access to INSTANCE.
   */
  private static class Singleton
  {
    private static DeployProperties INSTANCE = new DeployProperties();
  }
  
  /**
   * The path to the deployed webapp
   * 
   * TODO: This needs to return a URI (or a file)! If people are later doing something like: new File(getDeployPath())
   *   it will fail if this path has spaces in it! Unfortunately this is an indicator that we may need to
   *   refactor a lot more than just this method... 
   * 
   * @return
   */
  public static String getDeployPath()
  {
    return Singleton.INSTANCE.props.getString("deploy.path");
  }

  /**
   * @return The directory for generated jsp files
   */
  public static String getJspDir()
  {
    return Singleton.INSTANCE.props.getString("deploy.webinf");
  }
  
  /**
   * The admin password for the container.
   * @return
   */
  public static String getContainerPassword()
  {
    return Singleton.INSTANCE.props.getString("deploy.password");
  }
  
  public static String getDeployLib()
  {
    return Singleton.INSTANCE.props.getString("deploy.lib");
  }
  
  /**
   * @return The lib directory that contains the container's provided jars, like servlet-api.jar
   */
  public static String getContainerLib()
  {
    return Singleton.INSTANCE.props.getString("container.lib");
  }
  
  /**
   * @deprecated
   * Use getContainerLib instead.
   * 
   * @return Returns the location of the servlet-api jar.
   */
  public static String getContainerServletAPIJarLocation()
  {
    return Singleton.INSTANCE.props.getString("deploy.servlet.jar");
  }
  
  public static String getDeployBin()
  {
    return Singleton.INSTANCE.props.getString("deploy.bin");
  }
  
  public static String getApplicationURL()
  {
    return Singleton.INSTANCE.props.getString("container.app.url");
  }
  
  public static String getStoredTransactionAppFiles()
  {
    return Singleton.INSTANCE.props.getString("deploy.webinf") + File.separator + "storedAppFiles" + File.separator;
  }
  
  public static String getDeployRoot() {
    return CommonProperties.getDeployRoot();
  }
  
  /**
   * The admin username for the container.
   * 
   * @return
   */
  public static String getContainerUsername()
  {
    return Singleton.INSTANCE.props.getString("deploy.username");
  }
  
  public static String getAppName() {
    return Singleton.INSTANCE.props.getString("deploy.appname");
  }
  
  /**
   * DO NOT CALL THIS METHOD. This method exists for testing.
   */
  public static void dumpInstance()
  {
    DeployProperties.Singleton.INSTANCE = new DeployProperties();
  }
}
