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
import java.util.Locale;

import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.configuration.ConfigurationManager.ConfigGroup;
import com.runwaysdk.configuration.ConfigurationReaderIF;
import com.runwaysdk.transport.conversion.ConversionFacade;

/**
 * Convenience class that allows easy access to the common.properties file.
 * 
 * @author Eric
 */
public class CommonProperties
{
  /**
   * The common.properties configuration file
   */
  private ConfigurationReaderIF props;

  private ConfigurationReaderIF tprops        = null;

  private volatile String       domain;

  private static Locale         defaultLocale = ConversionFacade.getLocale(getDefaultLocaleString());

  /**
   * Private constructor loads the common.properties configuration
   */
  private CommonProperties()
  {
    props = ConfigurationManager.getReader(ConfigGroup.COMMON, "common.properties");

    if (ConfigurationManager.checkExistence(ConfigGroup.COMMON, "terraframe.properties"))
    {
      tprops = ConfigurationManager.getReader(ConfigGroup.COMMON, "terraframe.properties");
      domain = tprops.getString("domain");
    }
    else
    {
      // terraframe.properties does not exist.
      domain = props.getString("domain");
    }
  }

  /**
   * A holder class for access to the singleton. Allows for lazy instantiation
   * and thread safety because the class is not loaded until the first access to
   * INSTANCE.
   */
  private static class Singleton
  {
    private static CommonProperties INSTANCE = new CommonProperties();
  }

  /**
   * A convenience method to save us from typing Singleton.INSTANCE.props on
   * every getter.
   * 
   * @return
   */
  private static ConfigurationReaderIF instance()
  {
    return CommonProperties.Singleton.INSTANCE.props;
  }

  /**
   * Gets the length of a session (in minutes)
   * 
   * @return
   */
  public static int getSessionTime()
  {
    return instance().getInteger("sessionTime");
  }

  public static String[] getServerExpansionModules()
  {
    String rawProperty = instance().getString("server.expansion.modules");

    if (rawProperty.trim().length() != 0)
    {
      return instance().getString("server.expansion.modules").split(",");
    }
    else
    {
      return new String[0];
    }
  }

  public static String[] getClientExpansionModules()
  {
    String rawProperty = instance().getString("client.expansion.modules");

    if (rawProperty.trim().length() != 0)
    {
      return instance().getString("client.expansion.modules").split(",");
    }
    else
    {
      return new String[0];
    }
  }

  public static String getServerModulesLoader()
  {
    return instance().getString("server.modules.loader");
  }

  public static String getRMIService()
  {
    return instance().getString("java.rmi.service");
  }

  public static String getJSONRMIService()
  {
    return instance().getString("json.rmi.service");
  }

  public static Integer getRegistryPort()
  {
    return instance().getInteger("rmi.port");
  }

  /**
   * @param i
   * @return
   */
  public static int getServicePort(int defaultPort)
  {
    return instance().getInteger("rmi.service.port", defaultPort);
  }

  public static String getDefaultLocaleString()
  {
    return instance().getString("locale");
  }

  /**
   * @return Array of classpath entries
   */
  public static List<String> getCommonClasspath()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append(Singleton.INSTANCE.props.getString("common.classpath"));

    String append = Singleton.INSTANCE.props.getString("common.classpath.append");

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
   * Returns a default locale which is specified in a properties file and is
   * only used for populating sessions when the user has no locale. This locale
   * HAS NO RELATION to the LocalStruct DEFAULT_LOCALE concept and they are NOT
   * interchangable. This locale is also as opposed to the Java Locale.getDefault(),
   * which instead comes from the operating system.
   */
  public static java.util.Locale getDefaultLocale()
  {
    return defaultLocale;
  }

  /**
   * Returns the Default locale, which is cached to avoid a costly conversion.
   * 
   * @return
   */
  public static void setDefaultLocaleForTestingPurposesOnly(Locale newDefaultLocale)
  {
    defaultLocale = newDefaultLocale;
  }

  public static String getInstanceXMLschemaLocation()
  {
    return ConfigurationManager.getResource(ConfigGroup.XSD, "instance.xsd").toString();
  }

  public static String getTransactionXMLschemaLocation()
  {
    return ConfigurationManager.getResource(ConfigGroup.XSD, "transaction.xsd").toString();
  }

  public static String getTransactionRecordXMLschemaLocation()
  {
    return ConfigurationManager.getResource(ConfigGroup.XSD, "transactionRecord.xsd").toString();
  }

  public static Boolean getIncludeTimezone()
  {
    return instance().getBoolean("includeTimezone");
  }

  public static String getDefaultLog4JConfiguration()
  {
    return instance().getString("defaultLog4JConfiguration");
  }

  public static String getFormatFactoryClass()
  {
    return instance().getString("formatFactory.class");
  }

  public static Locale getFormatFactoryLocale()
  {
    if (instance().getString("formatFactory.locale") != null)
    {
      return new Locale(instance().getString("formatFactory.locale"));
    }
    else
    {
      return null;
    }
  }

  public static Boolean isFormatFactoryDelegate()
  {
    return instance().getBoolean("formatFactory.delegate", new Boolean(true));
  }

  /**
   * Gets the unique domain name for this site.
   * 
   * @return site domain
   */
  public static String getDomain()
  {
    return Singleton.INSTANCE.domain;
  }

  /**
   * Gets the name of the web application. Do not use this in place of the
   * request application context (i.e. for building URL's)! You should get it
   * from the servlet request object instead. The reason for this is it will
   * break our application context agnostic paradigm.
   * 
   * @return webapp name
   */
  public static String getDeployAppName()
  {
    if (Singleton.INSTANCE.tprops == null)
    {
      return DeployProperties.getAppName();
    }
    else
    {
      return Singleton.INSTANCE.tprops.getString("deploy.appname");
    }
  }

  public static String getDeployRoot()
  {
    if (Singleton.INSTANCE.tprops == null)
    {
      return null;
    }

    return Singleton.INSTANCE.tprops.getString("deploy.root");
  }

  /**
   * @return Returns the absolute path to project.basedir, as specified in
   *         common.properties configuration file. By default this property is
   *         filled automatically using a value provided by Maven, and as such
   *         will likely need to be set differently upon deploy. This path does
   *         not contain a / after it, so add a slash to your path when using
   *         it.
   */
  public static String getProjectBasedir()
  {
    return Singleton.INSTANCE.props.getString("project.basedir");
  }

  /**
   * DO NOT CALL THIS METHOD. This method only exists so that transaction
   * logging and importing can be tested!
   * 
   * @param domain
   */
  public static void setDomain(String domain)
  {
    if (LocalProperties.isRunwayEnvironment())
    {
      Singleton.INSTANCE.domain = domain;
    }
    else
    {
      String errorMsg = "Changing the site master setting can only be changed programmatically during runway development testing.";
      throw new UnsupportedOperationException(errorMsg);
    }
  }

  /**
   * DO NOT CALL THIS METHOD. This method exists for testing.
   */
  public static void dumpInstance()
  {
    CommonProperties.Singleton.INSTANCE = new CommonProperties();
  }

}