/**
 * 
 */
package com.runwaysdk.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
  Logger log = LoggerFactory.getLogger(ConfigurationManager.class);
  
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

  public static enum ConfigResolver {
    PROFILE("profile"), JAVA_PROPERTIES("java properties"), // Not used unless
                                                            // explicitly set,
                                                            // because default
                                                            // java properties
                                                            // sucks.
    COMMONS_CONFIG("commons config");

    private String displayName;

    ConfigResolver(String displayName)
    {
      this.displayName = displayName;
    }

    public String getDisplayName()
    {
      return displayName;
    }

    @Override
    public String toString()
    {
      return this.getClass().getName() + " : " + displayName;
    }
  }

  public static class Singleton
  {
    public static final ConfigurationManager INSTANCE = new ConfigurationManager();
  }

  private ConfigResolver                configResolver;

  private static InMemoryConfigurator inMemoryCFG;

  static
  {
    inMemoryCFG = new InMemoryConfigurator();
  }

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
          configResolver = ConfigResolver.COMMONS_CONFIG;
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
      configResolver = ConfigResolver.PROFILE;
    }
    else
    {
      URL runwayProps = ConfigurationManager.class.getClassLoader().getResource("runwaysdk");

      if (runwayProps == null)
      {
        String msg = "Runway SDK configuration files are missing. Runway expects at the classpath root either 1) A configuration directory called runwaysdk or 2) A master.properties file with associated profile directories or 3) A flattened profile with terraframe.properties.";
        throw new RunwayConfigurationException(msg);
      }

      configResolver = ConfigResolver.COMMONS_CONFIG;
    }
  }

  public ConfigurationReaderIF iGetReader(ConfigGroupIF configGroup, String config)
  {
    if (configResolver == ConfigResolver.COMMONS_CONFIG)
    {
      return new CommonsConfigurationReader(configGroup, config);
    }
    else if (configResolver == ConfigResolver.JAVA_PROPERTIES)
    {
      return new DefaultPropertiesConfigurationReader(configGroup, config);
    }
    else if (configResolver == ConfigResolver.PROFILE)
    {
      return ProfileManager.getBundle(configGroup.getIdentifier() + "/" + config);
    }
    else
    {
      String msg = "Invalid configResolver [" + configResolver.getDisplayName() + "].";
      throw new RunwayConfigurationException(msg);
    }
  }

  public static ConfigurationReaderIF getReader(ConfigGroupIF configGroup, String config)
  {
    return Singleton.INSTANCE.iGetReader(configGroup, config);
  }

  public void iSetConfigResolver(ConfigResolver configResolver)
  {
    this.configResolver = configResolver;
  }

  public static void setConfigResolver(ConfigResolver configResolver)
  {
    Singleton.INSTANCE.iSetConfigResolver(configResolver);
  }

  private URL iGetResource(ConfigGroupIF configGroup, String name, boolean throwEx)
  {
    String location = "";
    URL resource = null;

    if (configResolver == ConfigResolver.COMMONS_CONFIG || configResolver == ConfigResolver.JAVA_PROPERTIES)
    {
      location = "classpath:" + configGroup.getPath() + name;
      resource = ConfigurationManager.class.getClassLoader().getResource(configGroup.getPath() + name);
    }
    else if (configResolver == ConfigResolver.PROFILE)
    {
      if (configGroup.equals(ConfigGroup.XSD) || configGroup.equals(ConfigGroup.METADATA))
      {
        String path = configGroup.getPath() + name;
        location = "classpath:" + path;
        resource = ConfigurationManager.class.getClassLoader().getResource(path);
      }
      else
      {
        String profileName = ProfileManager.getProfileDir().getName();

        if (ProfileManager.getExplicitySpecifiedProfileHome() != null)
        {
          location = ProfileManager.getExplicitySpecifiedProfileHome();
          if (ProfileManager.isFlattened())
          {
            location += "/" + name;
          }
          else
          {
            location += "/" + profileName + "/" + configGroup.getIdentifier() + "/" + name;
          }

          File file = new File(location);
          if (file.exists())
          {
            try
            {
              return file.toURI().toURL();
            }
            catch (MalformedURLException e)
            {
            }
          }
        }
        else
        {
          String path;
          if (ProfileManager.isFlattened())
          {
            path = name;
          }
          else
          {
            path = profileName + "/" + configGroup.getIdentifier() + "/" + name;
          }
          location = "classpath:" + path;
          resource = ConfigurationManager.class.getClassLoader().getResource(path);
        }
      }
    }
    else
    {
      String msg = "Invalid configResolver [" + configResolver.getDisplayName() + "].";
      throw new RunwayConfigurationException(msg);
    }

    if (resource == null && throwEx)
    {
      String msg = "Unable to find configuration resource named [" + name + "] in config group [" + configGroup.getIdentifier() + "] at location [" + location + "] with configResolver [" + configResolver.getDisplayName() + "].";
      throw new RunwayConfigurationException(msg);
    }

    log.trace("getResource resolved [" + configGroup.getPath() + name + "] to " + resource);

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

  public static InMemoryConfigurator getInMemoryConfigurator()
  {
    return ConfigurationManager.inMemoryCFG;
  }
}
