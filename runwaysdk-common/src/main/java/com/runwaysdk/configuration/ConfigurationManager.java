/**
 * 
 */
package com.runwaysdk.configuration;

import java.net.URL;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.runwaysdk.profile.ProfileFlattener;

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
public class ConfigurationManager
{
  private Log log = LogFactory.getLog(ConfigurationManager.class);
  
  public static enum ConfigGroup {
    CLIENT("runwaysdk/client/"),
    COMMON("runwaysdk/common/"),
    SERVER("runwaysdk/server/"),
    TEST("runwaysdk/test/"),
    XSD("com/runwaysdk/resources/xsd/"),
    METADATA("com/runwaysdk/resources/metadata/"),
    ROOT("");
    
    private String path;
    
    ConfigGroup(String path) {
      this.path = path;
    }
    
    public String getPath() {
      return this.path;
    }
  }
  
  public static enum ConfigType {
    PROFILE,
    DEFAULT_PROPERTIES, // Not used unless explicitly set, because default java properties sucks.
    COMMONS_CONFIG;
  }
  
  public static class Singleton
  {
    public static final ConfigurationManager INSTANCE = new ConfigurationManager();
  }
  
  private ConfigType configType;
  private static BaseConfiguration inMemoryCFG = new BaseConfiguration();
  
  public ConfigurationManager() {
    URL terraframeProps = ConfigurationManager.class.getClassLoader().getResource("terraframe.properties");
    URL masterProps = ConfigurationManager.class.getClassLoader().getResource("master.properties");
    
    if (terraframeProps != null || masterProps != null) {
      configType = ConfigType.PROFILE;
    }
    else {
      URL runwayProps = ConfigurationManager.class.getClassLoader().getResource("runwaysdk");
      
      if (runwayProps == null) {
        String msg = "Runway SDK configuration files are missing. Runway expects at the classpath root either 1) A configuration directory called runwaysdk or 2) A master.properties file with associated profile directories or 3) A flattened profile with terraframe.properties.";
        throw new RunwayConfigurationException(msg);
      }
      
      configType = ConfigType.COMMONS_CONFIG;
    }
  }
  
  public ConfigurationReaderIF iGetReader(ConfigGroup configGroup, String config) {
    if (configType == ConfigType.COMMONS_CONFIG) {
      return new CommonsConfigurationReader(configGroup, config);
    }
    else if (configType == ConfigType.DEFAULT_PROPERTIES) {
      return new DefaultPropertiesConfigurationReader(configGroup, config);
    }
    else if (configType == ConfigType.PROFILE) {
      String path = "";
      if (configGroup == ConfigGroup.CLIENT) { path = "client"; }
      else if (configGroup == ConfigGroup.COMMON) { path = "common"; }
      else if (configGroup == ConfigGroup.SERVER) { path = "server"; }
      else if (configGroup == ConfigGroup.TEST) { path = "test"; }
      
      return ProfileManager.getBundle(path + "/" + config);
    }
    else {
      String msg = "Invalid configType.";
      throw new RunwayConfigurationException(msg);
    }
  }
  
  public static ConfigurationReaderIF getReader(ConfigGroup configGroup, String config) {
    return Singleton.INSTANCE.iGetReader(configGroup, config);
  }
  
  public void iSetConfigType(ConfigType configType) {
    this.configType = configType;
  }
  
  public static void setConfigType(ConfigType configType) {
    Singleton.INSTANCE.iSetConfigType(configType);
  }
  
  public URL iGetResource(ConfigGroup configGroup, String name) {
    URL resource = null;
    
    if (configType == ConfigType.COMMONS_CONFIG) {
      resource = ConfigurationManager.class.getClassLoader().getResource(configGroup.getPath() + name);
    }
    else if (configType == ConfigType.PROFILE) {
      String path = ProfileManager.getProfileDir().getName();
      if (configGroup == ConfigGroup.CLIENT) { path += "/client/"; }
      else if (configGroup == ConfigGroup.COMMON) { path += "/common/"; }
      else if (configGroup == ConfigGroup.SERVER) { path += "/server/"; }
      else if (configGroup == ConfigGroup.TEST) { path += "/test/"; }
      else if (configGroup == ConfigGroup.XSD) { path = ConfigGroup.XSD.getPath(); }
      path += name; 
      
      resource = ConfigurationManager.class.getClassLoader().getResource(path);
    }
    else {
      String msg = "Invalid configType.";
      throw new RunwayConfigurationException(msg);
    }
    
    if (resource == null) {
      String msg = "The configuration resource [" + configGroup.path + name + "] does not exist on the classpath.";
      throw new RunwayConfigurationException(msg);
    }
    
    log.trace("getResource resolved [" + configGroup.getPath() + name + "] to " + resource);
    
    return resource;
  }
  
  public static URL getResource(ConfigGroup configGroup, String name) {
    return Singleton.INSTANCE.iGetResource(configGroup, name);
  }
  
  public static Configuration getInMemoryConfigurator() {
    return ConfigurationManager.inMemoryCFG;
  }
}
