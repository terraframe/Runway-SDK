/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.gis.constants;

import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.configuration.ConfigurationManager.ConfigGroup;
import com.runwaysdk.configuration.ConfigurationReaderIF;
import com.runwaysdk.configuration.ProfileReader;

public class RunwayGisProperties
{
  /**
   * The local.properties configuration file
   */
  private ConfigurationReaderIF props;

  /**
   * A holder class for access to the singleton. Allows for lazy instantiation
   * and thread safety because the class is not loaded until the first access to
   * INSTANCE.
   */
  private static class Singleton
  {
    private static final RunwayGisProperties INSTANCE = new RunwayGisProperties();
  }

  /**
   * A convenience method to save us from typing Singleton.INSTANCE.props on
   * every getter.
   * 
   * @return
   */
  private static ConfigurationReaderIF instance()
  {
    return RunwayGisProperties.Singleton.INSTANCE.props;
  }

  /**
   * Private constructor loads the local.properties reader
   */
  private RunwayGisProperties()
  {
    props = ConfigurationManager.getReader(ConfigGroup.COMMON, "runwaygis.properties");
  }

  /**
   * Gets the runway bin directory
   */
  public static String getRunwayBin()
  {
    return instance().getString("runwaygis.bin");
  }

  /**
   * @return The root of the runway client source
   */
  public static String getClientSrc()
  {
    return instance().getString("runwaygis.src.client");
  }

  /**
   * @return The root of the runway client source
   */
  public static String getCommonSrc()
  {
    return instance().getString("runwaygis.src.common");
  }

  /**
   * @return The root of the runway client source
   */
  public static String getServerSrc()
  {
    return instance().getString("runwaygis.src.server");
  }
  
  /**
   * @return The server resources
   */
  public static String getGisServerResourcesDir()
  {
    return instance().getString("runwaygis.server.resources");
  }
}
