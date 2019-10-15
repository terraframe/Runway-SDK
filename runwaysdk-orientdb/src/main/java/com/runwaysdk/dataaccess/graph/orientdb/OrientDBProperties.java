/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
 */

package com.runwaysdk.dataaccess.graph.orientdb;

import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.configuration.ConfigurationManager.ConfigGroup;
import com.runwaysdk.configuration.ConfigurationReaderIF;

/**
 * Convenience class that allows easy access to the orientdb.properties file.
 * 
 * @author rrowlands, Eric
 */
public class OrientDBProperties
{
  /**
   * The orientdb.properties configuration file
   */
  private ConfigurationReaderIF props;

  /**
   * Private constructor loads the server.properties configuration
   */
  private OrientDBProperties()
  {
    this.props = ConfigurationManager.getReader(ConfigGroup.SERVER, "orientdb.properties");
  }

  /**
   * A holder class for access to the singleton. Allows for lazy instantiation
   * and thread safety because the class is not loaded until the first access to
   * INSTANCE.
   */
  private static class Singleton
  {
    private static final OrientDBProperties INSTANCE = new OrientDBProperties();
  }

  public static String getDatabaseName()
  {
    return Singleton.INSTANCE.props.getString("db.name");
  }

  public static String getUrl()
  {
    return Singleton.INSTANCE.props.getString("db.url");
  }

  public static String getRootUserName()
  {
    return Singleton.INSTANCE.props.getString("root.username");
  }

  public static String getRootUserPassword()
  {
    return Singleton.INSTANCE.props.getString("root.password");
  }

  public static Integer getMinPoolSize()
  {
    return Singleton.INSTANCE.props.getInteger("pool.min");
  }

  public static Integer getMaxPoolSize()
  {
    return Singleton.INSTANCE.props.getInteger("pool.max");
  }

  public static String getAdminUserName()
  {
    return Singleton.INSTANCE.props.getString("admin.username");
  }

  public static String getAdminUserPassword()
  {
    return Singleton.INSTANCE.props.getString("admin.password");
  }

}
