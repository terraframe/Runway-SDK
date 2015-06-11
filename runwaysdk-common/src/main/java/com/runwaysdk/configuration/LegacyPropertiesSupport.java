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
package com.runwaysdk.configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is a facade which maps a legacy property value to its new equivalent.
 */
public class LegacyPropertiesSupport
{
  private HashMap<String, String> legacyToModern = new HashMap<String, String>();
  private HashMap<String, String> modernToLegacy = new HashMap<String, String>();
  
  private boolean isLegacy;
  
  /**
   * A holder class for access to the singleton. Allows for lazy instantiation and thread
   * safety because the class is not loaded until the first access to INSTANCE.
   */
  private static class Singleton
  {
    private static LegacyPropertiesSupport INSTANCE = new LegacyPropertiesSupport();
  }
  
  /**
   * Used only for testing.
   */
  protected static void dumpInstance()
  {
    LegacyPropertiesSupport.Singleton.INSTANCE = new LegacyPropertiesSupport();
  }
  
  public LegacyPropertiesSupport()
  {
    this.isLegacy = !(ConfigurationManager.Singleton.INSTANCE.getConfigResolver() instanceof CommonsConfigurationResolver);
    
    // Email Properties
    legacyToModern.put("smtp.host", "email.host");
    legacyToModern.put("fromAddress", "email.fromAddress");
    legacyToModern.put("loginUser", "email.loginUser");
    legacyToModern.put("loginPass", "email.loginPass");
    legacyToModern.put("keyExpire", "email.keyExpire");
    
    // Database Properties
    legacyToModern.put("dataDumpExecutable", "database.execDump");
    legacyToModern.put("dataImportExecutable", "database.execImport");
    legacyToModern.put("db.connection.pooling", "database.connection.pooling");
    legacyToModern.put("db.connection.initial", "database.connection.initial");
    legacyToModern.put("db.connection.max", "database.connection.max");
    legacyToModern.put("namespace", "database.namespace");
    legacyToModern.put("databaseVendor", "database.vendor");
    legacyToModern.put("serverName", "database.hostURL");
    legacyToModern.put("port", "database.port");
    legacyToModern.put("user", "database.user");
    legacyToModern.put("password", "database.password");
    legacyToModern.put("databaseName", "database.name");
    legacyToModern.put("jndiDataSource", "database.jndiDataSource");
    legacyToModern.put("databaseBinDirectory", "database.bin");
    
    // Invert the map for bi-directional fast lookups
    for(Map.Entry<String, String> entry : legacyToModern.entrySet())
    {
      modernToLegacy.put(entry.getValue(), entry.getKey());
    }
  }
  
  public static LegacyPropertiesSupport getInstance()
  {
    return LegacyPropertiesSupport.Singleton.INSTANCE;
  }
  
  /**
   * @return Returns true if the system is reading from legacy properties files.
   */
  public boolean iIsLegacy()
  {
    return this.isLegacy;
  }
  public static boolean isLegacy() { return LegacyPropertiesSupport.Singleton.INSTANCE.iIsLegacy(); }
  
  /**
   * Returns the legacy string, if the system is using legacy configuration. Otherwise it returns the 'notLegacy' value.
   */
  public String iPickRelevant(String legacy, String notLegacy)
  {
    if (this.isLegacy)
    {
      return legacy;
    }
    return notLegacy;
  }
  public static String pickRelevant(String legacy, String notLegacy) { return LegacyPropertiesSupport.Singleton.INSTANCE.iPickRelevant(legacy, notLegacy); }
  
  /**
   * Takes in a legacy property key and returns the new property key.
   * 
   * @param property The name of the legacy property.
   */
  public String iGetProperty(String property)
  {
    if (legacyToModern.containsKey(property))
    {
      return legacyToModern.get(property);
    }
    return property;
  }
  public static String getProperty(String property) { return LegacyPropertiesSupport.Singleton.INSTANCE.iGetProperty(property); }
  
  public String iModernToLegacy(String modernKey)
  {
    if (modernToLegacy.containsKey(modernKey))
    {
      return modernToLegacy.get(modernKey);
    }
    return modernKey;
  }
}
