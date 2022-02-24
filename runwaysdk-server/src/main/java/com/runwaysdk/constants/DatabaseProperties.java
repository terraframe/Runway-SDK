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

import java.io.File;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.TreeSet;

import com.runwaysdk.ConfigurationException;
import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.configuration.ConfigurationManager.ConfigGroup;
import com.runwaysdk.configuration.ConfigurationReaderIF;
import com.runwaysdk.configuration.LegacyPropertiesSupport;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.database.general.AbstractDatabase;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.utf8.UTF8ResourceBundle;

/**
 * Acesses properties from two files: the agnostic database.properties, and the vendor
 * specified in databse.properties.
 *
 * @author Eric
 */
public class DatabaseProperties
{
  /**
   * The database.properties file, which is common to all dbs.
   */
  private ConfigurationReaderIF common_props;

  /**
   * Vendor-specific properties
   */
  private ResourceBundle vendor_props;

  /**
   * Stores error codes for lookup. Lookups are infrequent - only when exceptions are thrown
   * from the database, so a TreeSet is used instead of a HashSet because of the reduced memory
   */
  private TreeSet<String> errorCodes;

  /**
   * Stores serious error codes for lookup Lookups are infrequent - only when exceptions are thrown
   * from the database, so a TreeSet is used instead of a HashSet because of the reduced memory
   */
  private TreeSet<String> seriousErrorCodes;

  /**
   * Stores mappings of MdAttribute types to Database types. Lookups are frequent - nearly
   * all writes to the database will refernce these mappings, so a HashTable is used to
   * increase performance.
   */
  private Hashtable<String, String> attributeTypes;
  
  private static final String appUsername = System.getenv("POSTGRES_APP_USERNAME");
  
  private static final String appPassword = System.getenv("POSTGRES_APP_PASSWORD");

  /**
   * A holder class for access to the singleton. Allows for lazy instantiation and thread
   * safety because the class is not loaded until the first access to INSTANCE.
   */
  private static class Singleton
  {
    private static final DatabaseProperties INSTANCE = new DatabaseProperties();
  }

  /**
   * The private constructor for the singleton
   */
  @SuppressWarnings("unchecked")
  private DatabaseProperties()
  {
    common_props = ConfigurationManager.getReader(ConfigGroup.SERVER, LegacyPropertiesSupport.pickRelevant("database.properties", "server.properties"));
    String databaseVendor = common_props.getString("database.vendor");
    
    try
    {
      vendor_props = UTF8ResourceBundle.getBundle(databaseVendor);
    }
    catch(Exception e)
    {
      e.printStackTrace(System.out);
      throw new ConfigurationException("Specified database vendor [" + databaseVendor + "] is not supported.");
    }

    // Read in all the regular error codes
    errorCodes = new TreeSet<String>();
    for (String code : vendor_props.getString("dbErrorCodes").split(","))
      errorCodes.add(code);

    // Read in all the serious error codes
    seriousErrorCodes = new TreeSet<String>();
    for (String code : vendor_props.getString("seriousDBErrorCodes").split(","))
      seriousErrorCodes.add(code);

    // Get the MdAttribute->DbType mappings
    attributeTypes = (Hashtable<String, String>) vendor_props.getObject("attributeTypes");
  }

  /**
   * @return The bin directory of the database
   */
  public static String getDatabaseBinDirectory()
  {
    String databaseBinDirectory = Singleton.INSTANCE.common_props.getString("database.bin");
    databaseBinDirectory = databaseBinDirectory.replace("/", File.separator);
    databaseBinDirectory = databaseBinDirectory.replace("\\", File.separator);
    return databaseBinDirectory;
  }

  /**
   * @return The bin directory of the database
   */
  public static String getDataDumpExecutable()
  {
    return Singleton.INSTANCE.common_props.getString("database.execDump");
  }

  /**
   * @return The bin directory of the database
   */
  public static String getDataImportExecutable()
  {
    return Singleton.INSTANCE.common_props.getString("database.execImport");
  }

  /**
   * @return The fully qualified name of the JDBC driver
   */
  public static boolean getConnectionPooling()
  {
    return Singleton.INSTANCE.common_props.getBoolean("database.connection.pooling");
  }

  /**
   * @return The name of the database the core is running on
   */
  public static String getDatabaseName()
  {
    return Singleton.INSTANCE.common_props.getString("database.name");
  }
  
  /**
   * @return The name of the database namespace the core is running on
   */
  public static String getNamespace()
  {
    return Singleton.INSTANCE.common_props.getString("database.namespace");
  }

  /**
   * Takes a core Attribute type and returns the database type used to store it.
   *
   * @param coreType The Core Attribute type
   * @return The corresponding database type
   */
  public static String getDatabaseType(String coreType)
  {
    return Singleton.INSTANCE.attributeTypes.get(coreType);
  }

  /**
   * Takes a core Attribute type and returns the database type used to store it.
   *
   * @param coreType The Core Attribute type
   * @return The corresponding database type
   */
  public static String getDatabaseType(MdAttributeConcreteDAOIF coreType)
  {
    return getDatabaseType(coreType.getType());
  }

  /**
   * @return The database the core is running on
   */
  @SuppressWarnings("unchecked")
  public static Class<AbstractDatabase> getDatabaseClass()
  {
    String className = Singleton.INSTANCE.vendor_props.getString("databaseClass");

    return (Class<AbstractDatabase>) LoaderDecorator.load(className);
  }

  /**
   * @return The formatting for Date attributes
   */
  public static String getDateFormat()
  {
    return Singleton.INSTANCE.vendor_props.getString("date.format");
  }

  /**
   * @return The formatting for DateTime attributes
   */
  public static String getDateTimeFormat()
  {
    return Singleton.INSTANCE.vendor_props.getString("datetime.format");
  }

  /**
   * @return The number of database connections to initialize the pool with
   */
  public static int getInitialConnections()
  {
    return Singleton.INSTANCE.common_props.getInteger("database.connection.initial");
  }

  /**
   * Returns the JNDI Data Source
   *
   * @return
   */
  public static String getJNDIDataSource()
  {
    return Singleton.INSTANCE.common_props.getString("database.jndiDataSource");
  }

  /**
   * @return The maximum number of database connections
   */
  public static int getMaxConnections()
  {
    return Singleton.INSTANCE.common_props.getInteger("database.connection.max");
  }

  /**
   * @return The password for the runway's database user
   */
  public static String getPassword()
  {
    if (appPassword != null)
    {
      return appPassword;
    }
    
    return Singleton.INSTANCE.common_props.getString("database.password");
  }
  
  public static String getRootPassword()
  {
    String rootPassword = System.getenv("POSTGRES_ROOT_PASSWORD");
    if (rootPassword != null)
    {
      return rootPassword;
    }
    
    return Singleton.INSTANCE.common_props.getString("database.rootPassword");
  }

  /**
   * @return The port the database is listening on
   */
  public static int getPort()
  {
    return Singleton.INSTANCE.common_props.getInteger("database.port");
  }

  /**
   * @return The formatting for Time attributes
   */
  public static String getTimeFormat()
  {
    return Singleton.INSTANCE.vendor_props.getString("time.format");
  }

  /**
   * @return The location (IP Address/URL/Network location) of the database
   */
  public static String getServerName()
  {
    return Singleton.INSTANCE.common_props.getString("database.hostURL");
  }

  /**
   * @return The runway's database user name
   */
  public static String getUser()
  {
    if (appUsername != null)
    {
      return appUsername;
    }
    
    return Singleton.INSTANCE.common_props.getString("database.user");
  }
  
  public static String getRootUser()
  {
    String rootUsername = System.getenv("POSTGRES_ROOT_USERNAME");
    if (rootUsername != null)
    {
      return rootUsername;
    }
    
    return Singleton.INSTANCE.common_props.getString("database.rootUser");
  }

  /**
   * @param code An error code
   * @return true if the String is a recognized error code for the current database
   */
  public static boolean isError(String code)
  {
    return Singleton.INSTANCE.errorCodes.contains(code);
  }

  /**
   * @param code An error code
   * @return true if the String is a serious error code for the current database
   */
  public static boolean isSeriousError(String code)
  {
    return Singleton.INSTANCE.seriousErrorCodes.contains(code);
  }
}
