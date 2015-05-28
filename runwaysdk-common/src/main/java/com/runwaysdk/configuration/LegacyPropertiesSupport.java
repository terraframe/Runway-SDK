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

/**
 * This class is a facade which maps a legacy property value to its new equivalent.
 */
public class LegacyPropertiesSupport
{
  private HashMap<String, String> legacyMap = new HashMap<String, String>();
  
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
    legacyMap.put("smtp.host", "email.host");
    legacyMap.put("fromAddress", "email.fromAddress");
    legacyMap.put("loginUser", "email.loginUser");
    legacyMap.put("loginPass", "email.loginPass");
    legacyMap.put("keyExpire", "email.keyExpire");
    
    // Database Properties
    legacyMap.put("dataDumpExecutable", "database.execDump");
    legacyMap.put("dataImportExecutable", "database.execImport");
    legacyMap.put("db.connection.pooling", "database.connection.pooling");
    legacyMap.put("db.connection.initial", "database.connection.initial");
    legacyMap.put("db.connection.max", "database.connection.max");
    legacyMap.put("namespace", "database.namespace");
    legacyMap.put("databaseVendor", "database.vendor");
    legacyMap.put("serverName", "database.hostURL");
    legacyMap.put("port", "database.port");
    legacyMap.put("user", "database.user");
    legacyMap.put("password", "database.password");
    legacyMap.put("databaseName", "database.name");
    legacyMap.put("jndiDataSource", "database.jndiDataSource");
    legacyMap.put("databaseBinDirectory", "database.bin");
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
    if (legacyMap.containsKey(property))
    {
      return legacyMap.get(property);
    }
    return property;
  }
  public static String getProperty(String property) { return LegacyPropertiesSupport.Singleton.INSTANCE.iGetProperty(property); }
}
