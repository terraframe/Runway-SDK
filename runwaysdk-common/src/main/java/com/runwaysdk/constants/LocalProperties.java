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
import com.runwaysdk.configuration.ProfileManager;

public class LocalProperties
{
  private static final String RUNWAY = "runway";

  private static final String DEVELOP = "develop";

  private static final String TEST = "test";

  private static final String DEPLOY = "deploy";

  /**
   * The local.properties configuration file
   */
  private ConfigurationReaderIF props;

  /**
   * A boolean that tracks whether or not, on generation, we keep existing
   * stub source code that is found on the filesystem. Parsing a null property (in
   * the event that the flag is not set) will result in <code>true</code>,
   * which is our preferred default anyway.
   */
  private Boolean keepSource;

  /**
   * A boolean that tracks whether or not, on generation, we keep existing
   * base source code that is found on the filesystem. Parsing a null property (in
   * the event that the flag is not set) will result in <code>true</code>,
   * which is our preferred default anyway.
   */
  private Boolean keepBaseSource;

  /**
   * A boolean that tracks whether or not code should be generated and
   * during a transaction. Parsing a null property (in
   * the event that the flag is not set) will result in <code>false</code>,
   * which is our preferred default anyway.
   */
  private Boolean skipCodeGenAndCompile = Boolean.parseBoolean(System.getProperty("runway.skipCodeGenAndCompile"));

  /**
   * A holder class for access to the singleton. Allows for lazy instantiation and thread
   * safety because the class is not loaded until the first access to INSTANCE.
   */
  private static class Singleton
  {
    private static final LocalProperties INSTANCE = new LocalProperties();
  }

  /**
   * A convenience method to save us from typing Singleton.INSTANCE.props on
   * every getter.
   *
   * @return
   */
  private static ConfigurationReaderIF instance()
  {
    return LocalProperties.Singleton.INSTANCE.props;
  }

  /**
   * Private constructor loads the local.properties reader
   */
  private LocalProperties()
  {
    String keepSourceString = System.getProperty("runway.keepSource");
    if (keepSourceString == null)
    {
      keepSource = true;
    }
    else
    {
      keepSource = Boolean.parseBoolean(keepSourceString);
    }

    String keepBaseSourceString = System.getProperty("runway.keepBaseSource");
    if (keepBaseSourceString == null)
    {
      keepBaseSource = true;
    }
    else
    {
      keepBaseSource = Boolean.parseBoolean(keepBaseSourceString);
    }

    props = ConfigurationManager.getReader(ConfigGroup.COMMON, "local.properties");
  }

  /**
   * @return The client bin directory
   */
  public static String getClientBin()
  {
    return instance().getString("client.bin");
  }

  /**
   * @return The client lib directory
   */
  public static String getClientLib()
  {
    return instance().getString("client.lib");
  }

  /**
   * @return The root of the generated client source
   */
  public static String getClientSrc()
  {
    return instance().getString("client.src");
  }

  /**
   * @return The common bin directory
   */
  public static String getCommonBin()
  {
    return instance().getString("common.bin");
  }

  /**
   * @return The common lib directory
   */
  public static String getCommonLib()
  {
    return instance().getString("common.lib");
  }
  
  public static boolean isReloadableClassesEnabled() {
    return instance().getBoolean("classloader.reloadable.enabled", true);
  }
  
  /**
   * @return Find out if we should use maven to construct our classpath.
   */
  public static boolean useMavenLib()
  {
    return getClientPom() != null && getCommonPom() != null && getServerPom() != null && getLocalRepository() != null;
  }
  
  /**
   * @return When using Maven, this is the path to the client's pom.xml
   */
  public static String getClientPom()
  {
    return instance().getString("client.pom");
  }
  
  /**
   * @return When using Maven, this is the path to the common's pom.xml
   */
  public static String getCommonPom()
  {
    return instance().getString("common.pom");
  }
  
  /**
   * @return When using Maven, this is the path to the server's pom.xml
   */
  public static String getServerPom()
  {
    return instance().getString("server.pom");
  }
  
  /**
   * @return When using Maven, this is the path to the local repository.
   */
  public static String getLocalRepository()
  {
    return instance().getString("local.maven.repo");
  }

  /**
   * @return The root of the generated common source
   */
  public static String getCommonSrc()
  {
    return instance().getString("common.src");
  }

  /**
   * @return The directory for generated jsp files
   */
  public static String getJspDir()
  {
    return instance().getString("jsp.dir");
  }

  /**
   * @return The local root lib directory
   */
  public static String getLocalLib()
  {
    return instance().getString("local.lib");
  }

  /**
   * @return The local root bin directory
   */
  public static String getLocalBin()
  {
    return instance().getString("local.bin");
  }
  
  public static String getGeneratedRoot()
  {
    return instance().getString("generated.root");
  }

  /**
   * @return The log directory
   */
  public static String getLocalSource()
  {
    return instance().getString("local.src");
  }

  /**
   * @return Array of classpath entries
   */
  public static String[] getLocalClasspath()
  {
    String string = instance().getString("local.classpath");

    if (string == null || string.length()==0)
    {
      return new String[0];
    }
    else
    {
//      return string.split(";|:"); Splitting on ':' messes with windows pathing, e.g. C:\tomcat6
      return string.split(";");
    }
  }

  /**
   * @return The log directory
   */
  public static String getLogDirectory()
  {
    return instance().getString("log.dir");
  }

  /**
   * @return The server bin directory
   */
  public static String getServerBin()
  {
    return instance().getString("server.bin");
  }

  /**
   * @return The server lib directory
   */
  public static String getServerLib()
  {
    return instance().getString("server.lib");
  }

  /**
   * @return The root of the generated server source
   */
  public static String getServerSrc()
  {
    return instance().getString("server.src");
  }

  /**
   * @return The directory where sessions are stored
   */
  public static String getSessionCacheDirectory()
  {
    return instance().getString("session.cache");
  }
  
  /**
   * @return The directory where sessions are stored
   */
  public static String getPermissionCacheDirectory()
  {
    return instance().getString("permission.cache");
  }
  

  /**
   * @return The web directory
   */
  public static String getWebDirectory()
  {
    return instance().getString("web.dir");
  }

  public static boolean isKeepSource()
  {
    return Singleton.INSTANCE.keepSource;
  }

  public static void setKeepSource(boolean keepSource)
  {
    Singleton.INSTANCE.keepSource = keepSource;
  }

  public static boolean isKeepBaseSource()
  {
    return Singleton.INSTANCE.keepBaseSource;
  }

  public static void setBaseKeepSource(boolean keepBaseSource)
  {
    Singleton.INSTANCE.keepBaseSource = keepBaseSource;
  }

  public static boolean isSkipCodeGenAndCompile()
  {
    return Singleton.INSTANCE.skipCodeGenAndCompile;
  }

  public static void setSkipCodeGenAndCompile(boolean skipCodeGenAndCompile)
  {
    Singleton.INSTANCE.skipCodeGenAndCompile = skipCodeGenAndCompile;
  }

  /**
   * Returns the environment for this installation (I.E. development, production)
   *
   * @return
   */
  private static String getEnvironment()
  {
    return instance().getString("environment");
  }

  /**
   * Checks if the core is in the develop environment. The develop environment
   * is either Runway development and application development using Runway.
   *
   * @return true if the core is in a develop environment; otherwise, false
   */
  public static boolean isRunwayEnvironment()
  {
    return getEnvironment().equals(RUNWAY);
  }

  /**
   * Checks if the environment is configured for application development using the core.
   *
   * @return
   */
  public static boolean isDevelopEnvironment()
  {
    return getEnvironment().equals(DEVELOP);
  }

  /**
   * Checks if the environment is configured for application test using the core.
   *
   * @return
   */
  public static boolean isTestEnvironment()
  {
    return getEnvironment().equals(TEST);
  }

  /**
   * Checks to see if the core is in a deployed environment
   *
   * @return
   */
  public static boolean isDeployEnviroment()
  {
    return getEnvironment().equals(DEPLOY);
  }

  /**
   * Checks to see if the core is in a deployed environment
   *
   * @return
   */
  public static boolean isDeployedInContainer()
  {
    return getEnvironment().equals(DEPLOY) || getEnvironment().equals(TEST);
  }

  /**
   * Additional Aspectj path for server side weaving
   */
  public static String[] aspectJPath()
  {
    String rawProperty = instance().getString("serverAspectPath");

    if (rawProperty.trim().length() != 0)
    {
      return instance().getString("serverAspectPath").split(",");
    }
    else
    {
      return new String[0];
    }
  }
}
