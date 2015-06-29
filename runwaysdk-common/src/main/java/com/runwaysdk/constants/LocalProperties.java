/**
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
 */
package com.runwaysdk.constants;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.configuration.ConfigurationManager.ConfigGroup;
import com.runwaysdk.configuration.ConfigurationReaderIF;
import com.runwaysdk.configuration.LegacyPropertiesSupport;
import com.runwaysdk.configuration.RunwayConfigurationException;

public class LocalProperties
{
  public static final String    RUNWAY                = "runway";

  public static final String    DEVELOP               = "develop";

  public static final String    TEST                  = "test";

  public static final String    DEPLOY                = "deploy";

  /**
   * The local.properties configuration file
   */
  private ConfigurationReaderIF props;

  /**
   * A boolean that tracks whether or not, on generation, we keep existing stub
   * source code that is found on the filesystem. Parsing a null property (in
   * the event that the flag is not set) will result in <code>true</code>, which
   * is our preferred default anyway.
   */
  private Boolean               keepSource;

  /**
   * A boolean that tracks whether or not, on generation, we keep existing base
   * source code that is found on the filesystem. Parsing a null property (in
   * the event that the flag is not set) will result in <code>true</code>, which
   * is our preferred default anyway.
   */
  private Boolean               keepBaseSource;

  /**
   * DDMS uses only one property, local.src for specifying
   * server.src,client.src, and common.src but the new Runway projects require
   * separate properties. This helps us manage the trickiness.
   */
  private Boolean               usesCombinedLocalSrc  = true;

  /**
   * A boolean that tracks whether or not code should be generated and during a
   * transaction. Parsing a null property (in the event that the flag is not
   * set) will result in <code>false</code>, which is our preferred default
   * anyway.
   */
  private Boolean               skipCodeGenAndCompile = Boolean.parseBoolean(System.getProperty("runway.skipCodeGenAndCompile"));

  /**
   * A holder class for access to the singleton. Allows for lazy instantiation
   * and thread safety because the class is not loaded until the first access to
   * INSTANCE.
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

    props = ConfigurationManager.getReader(ConfigGroup.COMMON, LegacyPropertiesSupport.pickRelevant("local.properties", "common.properties"));

    if (props.getString("local.src") != null)
    {
      usesCombinedLocalSrc = true;
    }
    else {
      ArrayList<String> missingProps = new ArrayList<String>();
      if (props.getString("server.gen.src") == null) {
        missingProps.add("server.gen.src");
      }
      if (props.getString("common.gen.src") == null) {
        missingProps.add("common.gen.src");
      }
      if (props.getString("client.gen.src") == null) {
        missingProps.add("client.gen.src");
      }
      if (missingProps.size() > 0) {
        String errMsg = LegacyPropertiesSupport.pickRelevant("local.properties", "common.properties") + " is miconfigured. The directories for generated source can be specified in 1 of 2 different ways. Either a "
            + "local.src exists with inner directories (client/common/server), or 3 separate properties can be specified. The following properties [" + StringUtils.join(missingProps, ", ") + "] are null.";
        throw new RunwayConfigurationException(errMsg);
      }
      usesCombinedLocalSrc = false;
    }
  }

  /**
   * @return The client bin directory
   */
  public static String getClientGenBin()
  {
    String comGen = instance().getString("client.gen.bin");
    if (comGen != null)
    {
      return comGen;
    }
    else
    {
      return instance().getString("client.bin");
    }
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
  public static String getClientGenSrc()
  {
    String comGen = instance().getString("client.gen.src");
    if (comGen != null)
    {
      return comGen;
    }
    else
    {
      return instance().getString("client.src");
    }
  }

  /**
   * @return The root of the generated common source
   */
  public static String getCommonGenSrc()
  {
    String comGen = instance().getString("common.gen.src");
    if (comGen != null)
    {
      return comGen;
    }
    else
    {
      return instance().getString("common.src");
    }
  }

  /**
   * @return The common bin directory
   */
  public static String getCommonGenBin()
  {
    String comGen = instance().getString("common.gen.bin");
    if (comGen != null)
    {
      return comGen;
    }
    else
    {
      return instance().getString("common.bin");
    }
  }

  /**
   * @return The common lib directory
   */
  public static String getCommonLib()
  {
    return instance().getString("common.lib");
  }

  /**
   * @return The server bin directory
   */
  public static String getServerGenBin()
  {
    String comGen = instance().getString("server.gen.bin");
    if (comGen != null)
    {
      return comGen;
    }
    else
    {
      return instance().getString("server.bin");
    }
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
  public static String getServerGenSrc()
  {
    String comGen = instance().getString("server.gen.src");
    if (comGen != null)
    {
      return comGen;
    }
    else
    {
      return instance().getString("server.src");
    }
  }

  public static boolean isReloadableClassesEnabled()
  {
    return instance().getBoolean("classloader.reloadable.enabled", true);
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
  
  public static String getSrcRoot() {
    return instance().getString("local.src.root");
  }
  
  public static String getGenRoot() {
    return instance().getString("local.gen.root");
  }

  /**
   * @return The log directory
   */
  public static String getServerSrc()
  {
    if (Singleton.INSTANCE.usesCombinedLocalSrc)
    { // If DDMS:
      return instance().getString("local.src") + "/server";
    }
    else
    {
      return instance().getString("server.src");
    }
  }

  public static String getCommonSrc()
  {
    if (Singleton.INSTANCE.usesCombinedLocalSrc)
    { // If DDMS:
      return instance().getString("local.src") + "/common";
    }
    else
    {
      return instance().getString("common.src");
    }
  }

  public static String getClientSrc()
  {
    if (Singleton.INSTANCE.usesCombinedLocalSrc)
    { // If DDMS:
      return instance().getString("local.src") + "/client";
    }
    else
    {
      return instance().getString("client.src");
    }
  }

  /**
   * @return Array of classpath entries
   */
  public static String[] getLocalClasspath()
  {
    String string = instance().getString("local.classpath");

    if (string == null || string.length() == 0)
    {
      return new String[0];
    }
    else
    {
      // return string.split(";|:"); Splitting on ':' messes with windows
      // pathing, e.g. C:\tomcat6
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
   * Returns the environment for this installation (I.E. development,
   * production)
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
   * Checks if the environment is configured for application development using
   * the core.
   * 
   * @return
   */
  public static boolean isDevelopEnvironment()
  {
    return getEnvironment().equals(DEVELOP);
  }

  /**
   * Checks if the environment is configured for application test using the
   * core.
   * 
   * @return
   */
  public static boolean isTestEnvironment()
  {
    return getEnvironment().equals(TEST);
  }

  /**
   * @return
   */
  public static boolean getCopyArtifactsOnStart()
  {
    return instance().getBoolean("copyArtifactsOnStart", true);
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

    if (rawProperty == null)
    {
      return null;
    }

    if (rawProperty.trim().length() != 0)
    {
      return instance().getString("serverAspectPath").split(",");
    }
    else
    {
      return new String[0];
    }
  }

  /**
   * @return
   */
  public static boolean useMavenLib()
  {
    if (getServerLib() == null && getCommonLib() == null && getClientLib() == null)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

}
