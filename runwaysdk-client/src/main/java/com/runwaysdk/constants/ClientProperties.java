/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
import java.util.Arrays;
import java.util.List;

import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.configuration.ConfigurationManager.ConfigGroup;
import com.runwaysdk.configuration.ConfigurationReaderIF;

/**
 * Convenience class that allows easy access to the client.properties file.
 * 
 * @author Eric
 */
public class ClientProperties
{
  /**
   * The client.properties configuration file
   */
  private ConfigurationReaderIF props;

  /**
   * A holder class for access to the singleton. Allows for lazy instantiation
   * and thread safety because the class is not loaded until the first access to
   * INSTANCE.
   */
  private static class Singleton
  {
    private static final ClientProperties INSTANCE = new ClientProperties();
  }

  /**
   * Private constructor loads the client.properties configuration
   */
  private ClientProperties()
  {
    props = ConfigurationManager.getReader(ConfigGroup.CLIENT, "client.properties");
  }

  public static String getConnectionsFile()
  {
    return Singleton.INSTANCE.props.getString("connections.file");
  }

  public static String getConnectionsSchemaFile()
  {
    return Singleton.INSTANCE.props.getString("connectionsSchema.file");
  }

  public static String getLibDirectory()
  {
    return Singleton.INSTANCE.props.getString("clientLibDirectory");
  }

  public static String getFileCacheDirectory()
  {
    return Singleton.INSTANCE.props.getString("fileCacheDirectory") + "/";
  }

  public static boolean getEnableFileCache()
  {
    return Singleton.INSTANCE.props.getBoolean("enableFileCache");
  }

  /**
   * @return Array of classpath entries
   */
  public static List<String> getClientClasspath()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append(Singleton.INSTANCE.props.getString("client.classpath"));

    String append = Singleton.INSTANCE.props.getString("client.classpath.append");

    if (append != null && append.length() > 0)
    {
      buffer.append(";");
      buffer.append(append);
    }

    String classpath = buffer.toString();

    if (classpath == null || classpath.length() == 0)
    {
      return new ArrayList<String>();
    }
    else
    {
      // Splitting on ':' messes with windows pathing, e.g. C:\tomcat6
      return Arrays.asList(classpath.split(";"));
    }
  }

  /**
   * Gets the directory containing the runway javascript.
   */
  public static String getJavascriptDir()
  {
    return Singleton.INSTANCE.props.getString("ajax.javascript");
  }

  public static Integer getRMIAppletPort()
  {
    return Singleton.INSTANCE.props.getInteger("applet.rmi.port");
  }

  public static String getRMIAppletName()
  {
    return Singleton.INSTANCE.props.getString("applet.rmi.name");
  }

  public static boolean getJavascriptGzip()
  {
    return Boolean.parseBoolean(Singleton.INSTANCE.props.getString("javascript.gzip"));
  }

  public static boolean getJavascriptMinify()
  {
    return Boolean.parseBoolean(Singleton.INSTANCE.props.getString("javascript.minify"));
  }

  public static boolean getJavascriptCache()
  {
    return Boolean.parseBoolean(Singleton.INSTANCE.props.getString("javascript.cache"));
  }
}
