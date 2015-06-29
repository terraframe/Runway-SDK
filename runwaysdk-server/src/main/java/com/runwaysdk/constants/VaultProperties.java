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

import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.configuration.LegacyPropertiesSupport;
import com.runwaysdk.configuration.ConfigurationManager.ConfigGroup;
import com.runwaysdk.configuration.ConfigurationReaderIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;

/**
 * Convenience class that allows easy access to the vault.properties file.
 * 
 * @author jsmethie
 */
public class VaultProperties
{
  /**
   * The server.properties configuration file
   */
  private ConfigurationReaderIF props;

  /**
   * Private constructor loads the server.properties configuration
   */
  private VaultProperties()
  {
    this.props = ConfigurationManager.getReader(ConfigGroup.SERVER, LegacyPropertiesSupport.pickRelevant("vault.properties", "server.properties"));
  }

  /**
   * A holder class for access to the singleton. Allows for lazy instantiation
   * and thread safety because the class is not loaded until the first access to
   * INSTANCE.
   */
  private static class Singleton
  {
    private static final VaultProperties INSTANCE = new VaultProperties();
  }

  public static String getPath(String vaultName)
  {
    vaultName = LegacyPropertiesSupport.pickRelevant(vaultName, "vault." + vaultName);
    
    String path = Singleton.INSTANCE.props.getString(vaultName);

    if (path == null)
    {
      path = Singleton.INSTANCE.props.getString(VaultInfo.DEFAULT);
    }

    if (path == null)
    {
      throw new ProgrammingErrorException("A mapping for the vault [" + vaultName + "] has not been defined in " + LegacyPropertiesSupport.pickRelevant("vault.properties", "server.properties"));
    }

    return path;
  }
}
