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

package com.runwaysdk.gis.constants;

import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.configuration.ConfigurationManager.ConfigGroup;
import com.runwaysdk.configuration.ConfigurationReaderIF;

public class ServerGISproperties
{
  /**
   * The server.properties configuration file
   */
  private ConfigurationReaderIF props;
  
  /**
   * Private constructor loads the server.properties configuration
   */
  private ServerGISproperties()
  {
    this.props = ConfigurationManager.getReader(ConfigGroup.SERVER, "server-gis.properties");
  }
  
  /**
   * A holder class for access to the singleton. Allows for lazy instantiation and thread safety because the class is not loaded until the first access to INSTANCE.
   */
  private static class Singleton
  {
    private static final ServerGISproperties INSTANCE = new ServerGISproperties();
  }
  
  /**
   * URL of the Solr core that is storing the strategy for the geo tree.
   */
  public static String getSolrGeoEntitiesUrl()
  {
    return Singleton.INSTANCE.props.getString("solr.geoentities.url");
  }
  
}
