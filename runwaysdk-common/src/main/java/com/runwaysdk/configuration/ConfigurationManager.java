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
/**
 * 
 */
package com.runwaysdk.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

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
public class ConfigurationManager
{
  public static interface ConfigGroupIF {
    public String getPath();
    public String getIdentifier();
  }
  
  public static enum ConfigGroup implements ConfigGroupIF {
    CLIENT("runwaysdk/", "client"), COMMON("runwaysdk/", "common"), SERVER("runwaysdk/", "server"), TEST("runwaysdk/", "test"), XSD("com/runwaysdk/resources/xsd/", "xsd"), METADATA("com/runwaysdk/resources/metadata/", "metadata"), ROOT("", "root");

    private String path;

    private String identifier;

    ConfigGroup(String path, String identifier)
    {
      this.path = path;
      this.identifier = identifier;
    }

    public String getPath()
    {
      return this.path;
    }

    public String getIdentifier()
    {
      return identifier;
    }

    @Override
    public String toString()
    {
      return "ConfigGroup : " + getIdentifier();
    }
  }

//    PROFILE("profile"), JAVA_PROPERTIES("java properties"), // Not used unless
                                                            // explicitly set,
                                                            // because default
                                                            // java properties
                                                            // sucks.

  public static class Singleton
  {
    public static final ConfigurationManager INSTANCE = new ConfigurationManager();
  }

  private ConfigurationResolverIF                configResolver;

  public ConfigurationManager()
  {
    // configuration.properties is a properties file created specially just for this class. Sometimes abiguities can arise (specifically in the test projects) if you have a lot of properties files floating around everywhere
    // This properties file exists to resolve ambiguities as to which resolver we're using.
    InputStream configProps = ConfigurationManager.class.getClassLoader().getResourceAsStream("configuration.properties");
    if (configProps != null) {
      Properties props = new Properties();
      try
      {
        props.load(configProps);
        String resolver = props.getProperty("resolver", "Apache Commons");
        if (resolver.equals("Apache Commons")) {
          configResolver = new CommonsConfigurationResolver();
          return;
        }
        else {
          throw new RunwayConfigurationException("Unsupported configuration resolver '" + resolver + "'.");
        }
      }
      catch (IOException e)
      {
        throw new RunwayConfigurationException(e);
      }
    }
    
    URL terraframeProps = ConfigurationManager.class.getClassLoader().getResource("terraframe.properties");
    URL masterProps = ConfigurationManager.class.getClassLoader().getResource("master.properties");

    if (terraframeProps != null || masterProps != null)
    {
      configResolver = new ProfileConfigurationResolver();
    }
    else
    {
      URL runwayProps = ConfigurationManager.class.getClassLoader().getResource("runwaysdk");

      if (runwayProps == null)
      {
        String msg = "Runway SDK configuration files are missing. Runway expects at the classpath root either 1) A configuration directory called runwaysdk or 2) A master.properties file with associated profile directories or 3) A flattened profile with terraframe.properties.";
        throw new RunwayConfigurationException(msg);
      }

      configResolver = new CommonsConfigurationResolver();
    }
  }

  public ConfigurationReaderIF iGetReader(ConfigGroupIF configGroup, String config)
  {
    return configResolver.getReader(configGroup, config);
  }

  public static ConfigurationReaderIF getReader(ConfigGroupIF configGroup, String config)
  {
    return Singleton.INSTANCE.iGetReader(configGroup, config);
  }

  public void iSetConfigResolver(ConfigurationResolverIF configResolver)
  {
    this.configResolver = configResolver;
  }
  
  public ConfigurationResolverIF getConfigResolver()
  {
    return this.configResolver;
  }

  public static void setConfigResolver(ConfigurationResolverIF configResolver)
  {
    Singleton.INSTANCE.iSetConfigResolver(configResolver);
  }

  private URL iGetResource(ConfigGroupIF configGroup, String name, boolean throwEx)
  {
    URL resource = null;
    
    try
    {
      resource = configResolver.getResource(configGroup, name);
    }
    catch (RunwayConfigurationException e)
    {
      if (throwEx)
      {
        throw e;
      }
    }

//    if (resource != null)
//    {
//      log.trace("getResource resolved [" + configGroup.getPath() + name + "] to " + resource);
//    }

    return resource;
  }

  public static URL getResource(ConfigGroupIF configGroup, String name)
  {
    return Singleton.INSTANCE.iGetResource(configGroup, name, true);
  }

  public boolean iCheckExistence(ConfigGroupIF configGroup, String name)
  {
    URL resource = iGetResource(configGroup, name, false);

    if (resource == null)
    {
      return false;
    }
    return true;
  }

  public static InputStream getResourceAsStream(ConfigGroupIF configGroup, String name)
  {
    try
    {
      return getResource(configGroup, name).openStream();
    }
    catch (IOException e)
    {
      throw new RunwayConfigurationException(e);
    }
  }

  public static boolean checkExistence(ConfigGroupIF configGroup, String name)
  {
    return ConfigurationManager.Singleton.INSTANCE.iCheckExistence(configGroup, name);
  }
}
