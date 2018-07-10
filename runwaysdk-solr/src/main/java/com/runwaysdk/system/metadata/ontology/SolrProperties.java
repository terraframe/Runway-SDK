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
package com.runwaysdk.system.metadata.ontology;

import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.configuration.ConfigurationManager.ConfigGroup;
import com.runwaysdk.configuration.ConfigurationReaderIF;
import com.runwaysdk.generation.loader.;

public class SolrProperties implements 
{
  private static class Singleton implements 
  {
    private static SolrProperties INSTANCE = new SolrProperties();

    private static SolrProperties getInstance()
    {
      // INSTANCE will only ever be null if there is a problem. The if check is
      // to allow for debugging.
      if (INSTANCE == null)
      {
        INSTANCE = new SolrProperties();
      }

      return INSTANCE;
    }

    private static ConfigurationReaderIF getProps()
    {
      return getInstance().props;
    }
  }

  /**
   * The server.properties configuration file
   */
  private ConfigurationReaderIF props;

  private SolrProperties()
  {
    this.props = ConfigurationManager.getReader(ConfigGroup.SERVER, "solr.properties");
  }

  public static String getUrl()
  {
    return Singleton.getProps().getString("solr.url");
  }
}
