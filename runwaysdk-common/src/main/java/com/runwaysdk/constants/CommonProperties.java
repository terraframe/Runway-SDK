/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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
 ******************************************************************************/
package com.runwaysdk.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.configuration.ConfigurationManager.ConfigGroup;
import com.runwaysdk.configuration.ConfigurationReaderIF;
import com.runwaysdk.configuration.RunwayConfigurationException;
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

  private static final Locale   defaultLocale = ConversionFacade.getLocale(getDefaultLocaleString());

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

  public static String getContainerWebServiceDeployURL()
  {
    return instance().getString("container.webservice.deployURL");
  }

  public static boolean getContainerWebServiceEnabled()
  {
    return instance().getBoolean("container.webservice.enable");
  }

  public static int getContainerWebServiceCallTimeout()
  {
    return instance().getInteger("container.webservice.callTimeout");
  }

  public static String getRMIService()
  {
    return instance().getString("java.rmi.service");
  }

  public static String getJSONRMIService()
  {
    return instance().getString("json.rmi.service");
  }

  public static Integer getRMIPort()
  {
    return instance().getInteger("rmi.port");
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
    String string = instance().getString("common.classpath");

    if (string == null || string.length()==0)
    {
      return new ArrayList<String>();
    }
    else
    {
      //  Splitting on ':' messes with windows pathing, e.g. C:\tomcat6
      return Arrays.asList(string.split(";"));
    }
  }

  /**
   * Returns the Default locale, which is cached to avoid a costly conversion.
   * 
   * @return
   */
  public static java.util.Locale getDefaultLocale()
  {
    return defaultLocale;
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
    return instance().getBoolean("formatFactory.delegate");
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
   * Gets the name of the web application
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