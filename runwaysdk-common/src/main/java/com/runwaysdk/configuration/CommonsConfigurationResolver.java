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

public class CommonsConfigurationResolver implements ConfigurationResolverIF
{
  protected static boolean              includeRuntimeProperties = true;

  private static Logger               log                      = LoggerFactory.getLogger(ConfigurationManager.class);

  /*
   * The in memory configurator reads properties from the environment variables.
   */
  private static InMemoryConfigurator inMemoryCFG              = new InMemoryConfigurator();

  protected CompositeConfiguration      cconfig;
  
  /**
   * This value may be set by our custom tomcat class loader, since only the webapp class loader can tell what the deploy path is and our custom class loader obscures the webapp
   * class loader.
   */
  private static File deployPath;

  public CommonsConfigurationResolver()
  {
    cconfig = new CompositeConfiguration();
    cconfig.addConfiguration(CommonsConfigurationResolver.getInMemoryConfigurator().getImpl());

    if (CommonsConfigurationResolver.getIncludeRuntimeProperties())
    {
      cconfig.addConfiguration(this.getRuntimeProperties());
    }
  }

  /**
   * Calculates and returns the deployed path of the currently running application. If we are deployed inside a container, this will return $CATALINA_HOME/webapps/$CONTEXT_PATH
   *   as a resolved absolute path. If we are running inside a jar, this will return the directory that contains the running jar.
   * 
   * This method is what populates DeployProperties.getDeployPath() and thus it is preferred that you get this value from there instead.
   * 
   * @return An absolute file path of the deploy.path.
   */
  public File getDeployedPath()
  {
    if (deployPath != null)
    {
      return deployPath;
    }
    
    String sDeployPath;
    
    URL rootPath = CommonsConfigurationResolver.class.getResource("/");
    if (rootPath != null && !rootPath.getPath().equals(""))
    {
      sDeployPath = rootPath.getPath();
    }
    else
    {
      // If our code lives inside a jar, getResource will return null
      String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
      
      if (path.endsWith(".jar") || path.endsWith(".war") || path.endsWith(".class"))
      {
        path = new File(path).getParent();
      }
      
      sDeployPath = path.replace(this.getClass().getPackage().getName().replace(".", "/"), "");
    }
    
    if (sDeployPath.endsWith("/"))
    {
      sDeployPath = sDeployPath.substring(0, sDeployPath.length() - 1);
    }
    
    if (sDeployPath.endsWith("WEB-INF/classes"))
    {
      sDeployPath = sDeployPath.replace("WEB-INF/classes", "");
    }

    // getPath returns spaces as %20 for some reason
    sDeployPath = sDeployPath.replace("%20", " ");
    
    deployPath = new File(sDeployPath);
    return deployPath;
  }
  
  /*
   * Calculate any special properties that only have values at runtime.
   */
  protected BaseConfiguration getRuntimeProperties()
  {
    BaseConfiguration properties = new BaseConfiguration();

    // Calculate the value of deploy.path. The reason we do this at runtime is
    // because the value of this property may vary depending on the application
    // context path.
    String deployPath = getDeployedPath().getAbsolutePath();
    
    if (new File(deployPath).exists())
    {
      properties.setProperty("deploy.path", deployPath);
      log.info("deploy.path resolved to [" + deployPath + "]");
    }
    else
    {
      throw new RunwayConfigurationException("Unable to determine deploy.path, the location [" + deployPath + "] does not exist.");
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
      String msg = "Unable to find configuration resource named [" + name + "] in config group [" + configGroup.getOidentifier() + "] at location [" + location + "] with configResolver [" + this.toString() + "].";
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
   * WARNING : Do not change this method signature, it is referenced via strings in the TomcatLoader.
   */
  public static void setDeployPath(String _deployPath)
  {
    deployPath = new File(_deployPath);
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
