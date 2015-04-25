/**
 * 
 */
package com.runwaysdk.configuration;

import java.net.URL;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.configuration.ConfigurationManager.ConfigGroup;
import com.runwaysdk.configuration.ConfigurationManager.ConfigGroupIF;

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
public class CommonsConfigurationResolver implements ConfigurationResolverIF
{
  private Logger log = LoggerFactory.getLogger(ConfigurationManager.class);
  
  private CompositeConfiguration cconfig;
  
  public CommonsConfigurationResolver()
  {
    cconfig = new CompositeConfiguration();
    cconfig.addConfiguration(ConfigurationManager.getInMemoryConfigurator().getImpl());
    
    try
    {
      // Read the instance configuration
      String path2 = ConfigGroup.COMMON.getPath() + "instance.properties";
      ClassLoader loader2 = CommonsConfigurationReader.class.getClassLoader();
      URL clPath2 = loader2.getResource(path2);
      if (clPath2 != null)
      {
        cconfig.addConfiguration(new PropertiesConfiguration(clPath2));
        log.trace("Loading platform configuration overrides.");
      }
      else
      {
        log.info("Did not find instance.properties. No overrides were loaded.");
      }
    }
    catch (ConfigurationException e)
    {
      
    }
    
    try
    {
      // Read the platform configuration
      String path = ConfigGroup.COMMON.getPath() + "platform.properties";
      ClassLoader loader = CommonsConfigurationReader.class.getClassLoader();
      URL clPath = loader.getResource(path);
      if (clPath != null)
      {
        cconfig.addConfiguration(new PropertiesConfiguration(clPath));
        log.trace("Loading platform configuration overrides.");
      }
      else
      {
        log.info("Did not find platform.properties. No overrides were loaded.");
      }
    }
    catch (ConfigurationException e)
    {
      
    }
  }
  
  public ConfigurationReaderIF getReader(ConfigGroupIF configGroup, String config)
  {
    CompositeConfiguration _cconfig = new CompositeConfiguration();
    _cconfig.addConfiguration(this.cconfig);
    
    return new CommonsConfigurationReader(configGroup, config, _cconfig);
  }
  
  public URL getResource(ConfigGroupIF configGroup, String name)
  {
    String location = "classpath:" + configGroup.getPath() + name;
    URL resource = ConfigurationManager.class.getClassLoader().getResource(configGroup.getPath() + name);
    
    if (resource == null)
    {
      String msg = "Unable to find configuration resource named [" + name + "] in config group [" + configGroup.getIdentifier() + "] at location [" + location + "] with configResolver [" + this.toString() + "].";
      throw new RunwayConfigurationException(msg);
    }
    
    return resource;
  }
  
  @Override
  public String toString()
  {
    return this.getClass().getName();
  }
}
