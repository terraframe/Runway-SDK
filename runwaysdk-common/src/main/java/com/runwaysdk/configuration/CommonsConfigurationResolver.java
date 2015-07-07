/**
 * 
 */
package com.runwaysdk.configuration;

import java.io.File;
import java.net.URL;

import org.apache.commons.configuration.BaseConfiguration;
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
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
public class CommonsConfigurationResolver implements ConfigurationResolverIF
{
  private static boolean              includeRuntimeProperties = true;

  private static Logger               log                      = LoggerFactory.getLogger(ConfigurationManager.class);

  /*
   * The in memory configurator reads properties from the environment variables.
   */
  private static InMemoryConfigurator inMemoryCFG              = new InMemoryConfigurator();

  private CompositeConfiguration      cconfig;

  public CommonsConfigurationResolver()
  {
    cconfig = new CompositeConfiguration();
    cconfig.addConfiguration(CommonsConfigurationResolver.getInMemoryConfigurator().getImpl());

    if (CommonsConfigurationResolver.getIncludeRuntimeProperties())
    {
      cconfig.addConfiguration(this.getRuntimeProperties());
    }

    this.loadProperties("instance.properties");
    this.loadProperties("platform.properties");
  }

  private void loadProperties(String fileName)
  {
    try
    {
      String path = ConfigGroup.COMMON.getPath() + fileName;

      // Read the configuration
      URL url = CommonsConfigurationReader.class.getClassLoader().getResource(path);

      if (url != null)
      {
        cconfig.addConfiguration(new PropertiesConfiguration(url));

        log.trace("Loading [" + fileName + "] configuration overrides.");
      }
      else
      {
        log.info("Did not find " + fileName + ". No overrides were loaded.");
      }
    }
    catch (ConfigurationException e)
    {
      log.error(e.getLocalizedMessage(), e);
    }
  }

  /*
   * Calculate any special properties that only have values at runtime.
   */
  private BaseConfiguration getRuntimeProperties()
  {
    BaseConfiguration properties = new BaseConfiguration();

    // Calculate the value of deploy.path. The reason we do this at runtime is
    // because the value of this property may vary depending on the application
    // context path.
    URL resource = CommonsConfigurationResolver.class.getResource("/");
    String path = resource.getPath().replace("WEB-INF/classes", "");

    if (path.endsWith("/"))
    {
      path = path.substring(0, path.length() - 1);
    }

    // getPath returns spaces as %20. The file constructor does not read this
    // properly.
    path = path.replace("%20", " ");

    // The reason we're using resource.toURI here is because if there's spaces
    // in the path then constructing a file with a string doesn't work...
    if (new File(path).exists())
    {
      properties.setProperty("deploy.path", path);
      log.info("deploy.path resolved to [" + path + "]");
    }
    else
    {
      throw new RunwayConfigurationException("Unable to determine deploy.path, the location [" + path + "] does not exist.");
    }

    return properties;
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

  public static InMemoryConfigurator getInMemoryConfigurator()
  {
    return CommonsConfigurationResolver.inMemoryCFG;
  }

  /**
   * @return
   */
  public synchronized static boolean getIncludeRuntimeProperties()
  {
    return CommonsConfigurationResolver.includeRuntimeProperties;
  }

  /**
   * @return
   */
  public synchronized static void setIncludeRuntimeProperties(boolean includeRuntimeProperties)
  {
    CommonsConfigurationResolver.includeRuntimeProperties = includeRuntimeProperties;
  }

}
