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

package com.runwaysdk.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.runwaysdk.business.generation.AbstractCompiler;
import com.runwaysdk.business.generation.CompilerException;
import com.runwaysdk.business.generation.EclipseCompiler;
import com.runwaysdk.business.generation.SystemJavaCompiler;
import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.configuration.ConfigurationManager.ConfigGroup;
import com.runwaysdk.configuration.ConfigurationReaderIF;
import com.runwaysdk.configuration.RunwayConfigurationException;

/**
 * Convenience class that allows easy access to the server.properties file.
 * 
 * @author rrowlands, Eric
 */
public class ServerProperties
{
  /**
   * The server.properties configuration file
   */
  private ConfigurationReaderIF props;

  /**
   * True if transactions should be logged, false otherwise.
   */
  private Boolean               logTransactions;

  /**
   * Private constructor loads the server.properties configuration
   */
  private ServerProperties()
  {
    this.props = ConfigurationManager.getReader(ConfigGroup.SERVER, "server.properties");
    this.logTransactions = props.getBoolean("logTransactions", false);
  }

  /**
   * A holder class for access to the singleton. Allows for lazy instantiation and thread safety because the class is not loaded until the first access to INSTANCE.
   */
  private static class Singleton
  {
    private static final ServerProperties INSTANCE = new ServerProperties();
  }

  /**
   * True if compile time aspectj weaving, false for loadtimeWeaving.
   */
  public static Boolean compileTimeWeaving()
  {
    return Singleton.INSTANCE.props.getBoolean("compileTimeWeaving");
  }

  /**
   * Sets the Java compilation compliance level.
   */
  public static String getJavaComplianceLevel()
  {
    return Singleton.INSTANCE.props.getString("javaComplianceLevel");
  }

  public static String getSecurityProvider()
  {
    return Singleton.INSTANCE.props.getString("security.provider");
  }

  /**
   * Indicates whether this node logs transactions.
   */
  public static Boolean logTransactions()
  {
    return Singleton.INSTANCE.logTransactions;
  }

  /**
   * DO NOT CALL THIS METHOD! This method only exists so that transaction logging and importing can be tested!
   * 
   * @param logTransactions
   */
  public static void setLogTransactions(boolean logTransactions)
  {
    // if (LocalProperties.isRunwayEnvironment())
    {
      Singleton.INSTANCE.logTransactions = logTransactions;
    }
    // else
    // {
    // String errorMsg =
    // "Changing the log transaction setting can only be changed programmatically during runway development testing.";
    // throw new UnsupportedOperationException(errorMsg);
    // }
  }

  /**
   * Gets the file (including path) of the keystore
   * 
   * @return
   */
  public static String getKeyStoreFile()
  {
    return Singleton.INSTANCE.props.getString("keyStore.file");
  }

  /**
   * Gets the type of the keystore
   * 
   * @return
   */
  public static String getKeyStoreType()
  {
    return Singleton.INSTANCE.props.getString("keyStore.type");
  }

  /**
   * Gets the <b>plaintext</b> password for the keystore
   * 
   * @return
   */
  public static String getKeyStorePassword()
  {
    return Singleton.INSTANCE.props.getString("keyStore.password");
  }

  /**
   * Returns the time in minutes that a user lock on an object will timeout.
   * 
   * @return
   */
  public static int getLockTimeout()
  {
    return Singleton.INSTANCE.props.getInteger("lock.timeout");
  }

  /**
   * Indicates whether the cache will be memory only.
   */
  public static Boolean memoryOnlyCache()
  {
    return Singleton.INSTANCE.props.getBoolean("memoryOnlyCache");
  }

  /**
   * Returns the global cache offheap storage size (in MB). This is an optional parameter and may return null if left unconfigured and offheap storage will not be utilized.
   * 
   * @return
   */
  public static Integer getGlobalCacheOffheapMemorySize()
  {
    return Singleton.INSTANCE.props.getInteger("globalCache.offheapMemorySize");
  }
  
  /**
   * Returns the global cache memory size (in EntryUnits). Additional objects spill over to disk.
   * 
   * @return
   */
  public static int getGlobalCacheMemorySize()
  {
    return Singleton.INSTANCE.props.getInteger("globalCache.memorySize");
  }
  
  /**
   * Returns the max size of the global cache diskstore (in MB). This is an optional configuration value and if left unset this function returns null and the system defaults to 5 GB.
   * 
   * @return
   */
  public static Integer getGlobalCacheDiskSize()
  {
    return Singleton.INSTANCE.props.getInteger("globalCache.diskstore.size", 5000);
  }

  /**
   * Returns the name of the global cache.
   * 
   * @return
   */
  public static String getGlobalCacheName()
  {
    return Singleton.INSTANCE.props.getString("globalCache.cacheName");
  }

  /**
   * Returns the location of the global cache disk store file.
   * 
   * @return
   */
  public static String getGlobalCacheFileLocation()
  {
    return Singleton.INSTANCE.props.getString("globalCache.cacheFileLocation");
  }

  public static boolean getGlobalCacheStats()
  {
    return Singleton.INSTANCE.props.getBoolean("globalCache.stats", false);
  }
  
  /**
   * Returns the actual compiler instance for doing NON-ASPECT compilations, which are suitable for common/client/no-output compiling.
   * 
   * @return
   */
  public static AbstractCompiler getJavaCompiler()
  {
    String impl = Singleton.INSTANCE.props.getString("runtime.compiler.impl");
    
    if (impl == null || impl.equals("SYSTEM"))
    {
      return new SystemJavaCompiler();
    }
    else if (impl.equals("ECLIPSE"))
    {
      return new EclipseCompiler();
    }
    else
    {
      throw new RunwayConfigurationException("Unsupported value for server.properties:runtime.compiler.impl [" + impl + "].");
    }
  }

  /**
   * Returns the transaction cache memory size. Additional objects spill over to disk.
   * 
   * @return
   */
  public static int getTransactionCacheMemorySize()
  {
    return Singleton.INSTANCE.props.getInteger("transactionCache.memorySize");
  }

  /**
   * @return Array of classpath entries
   */
  public static List<String> getServerClasspath()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append(Singleton.INSTANCE.props.getString("server.classpath"));

    String append = Singleton.INSTANCE.props.getString("server.classpath.append");

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

  @SuppressWarnings("unchecked")
  public static List<String> getClientClasspath()
  {
    try
    {
      Class<?> clazz = ServerProperties.class.getClassLoader().loadClass("com.runwaysdk.constants.ClientProperties");
      return (List<String>) clazz.getMethod("getClientClasspath", new Class<?>[] {}).invoke(null);
    }
    catch (ClassNotFoundException e)
    {
      // String string = Singleton.INSTANCE.props.getString("client.classpath");
      //
      // if (string == null || string.length() == 0)
      // {
      // return new ArrayList<String>();
      // }
      // else
      // {
      // // Splitting on ':' messes with windows pathing, e.g. C:\tomcat6
      // return Arrays.asList(string.split(";"));
      // }
      return null;
    }
    catch (Exception e)
    {
      throw new CompilerException(e);
    }
  }

  /**
   * Returns the name of the transaction cache.
   * 
   * @return
   */
  public static String getTransactionCacheName()
  {
    return Singleton.INSTANCE.props.getString("transactionCache.cacheName");
  }

  /**
   * Returns the location of the transaction cache disk store file.
   * 
   * @return
   */
  public static String getTransactionCacheFileLocation()
  {
    return Singleton.INSTANCE.props.getString("transactionCache.cacheFileLocation");
  }

  /**
   * Returns the global cache bucket size.
   * 
   * @return
   */
  public static int getLockedIdBucketSize()
  {
    return Singleton.INSTANCE.props.getInteger("transaction.lockedObject.bucketSize");
  }

  /**
   * Returns the global cache bucket size.
   * 
   * @return
   */
  public static int getTransationIdBucketSize()
  {
    return Singleton.INSTANCE.props.getInteger("transaction.objectsInTransaction.bucketSize");
  }

  public static int getTransactionDiskstoreSize()
  {
    return Singleton.INSTANCE.props.getInteger("transactionCache.diskstore.size");
  }

  public static boolean memoryOnlyTransactionCache()
  {
    return Singleton.INSTANCE.props.getBoolean("transactionCache.memoryOnly");
  }

  public static boolean getTransactionCacheStats()
  {
    return Singleton.INSTANCE.props.getBoolean("transactionCache.stats", false);
  }

  public static String getProviderBuilder()
  {
    return Singleton.INSTANCE.props.getString("provider.builder");
  }
  
  public static String[] aspectJPath()
  {
    String rawProperty = Singleton.INSTANCE.props.getString("server.aspectPath");

    if (rawProperty == null)
    {
      return null;
    }

    if (rawProperty.trim().length() != 0)
    {
      return rawProperty.split(";");
    }
    else
    {
      return new String[0];
    }
  }
}
