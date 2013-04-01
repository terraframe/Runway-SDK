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
package com.runwaysdk.constants;

import java.util.Locale;

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
  private ProfileReader props;

  /**
   * The terraframe.properties configuration file
   */
  private ProfileReader tfProps;

  /**
   * Domain of this site.
   */
  private volatile String domain;
  
  /**
   * Name of the webapp
   */
  private volatile String appName;
  
  private static final Locale defaultLocale = ConversionFacade.getLocale(getDefaultLocaleString());

  /**
   * Private constructor loads the common.properties configuration
   */
  private CommonProperties()
  {
    props = ProfileManager.getBundle("common/common.properties");
    this.tfProps = ProfileManager.getBundle("common/terraframe.properties");
    this.domain = tfProps.getString("domain");
    this.appName = tfProps.getString("deploy.appname");
  }

  /**
   * A holder class for access to the singleton. Allows for lazy instantiation and thread
   * safety because the class is not loaded until the first access to INSTANCE.
   */
  private static class Singleton
  {
    private static final CommonProperties INSTANCE = new CommonProperties();
  }

  private static ProfileReader instance()
  {
    return Singleton.INSTANCE.props;
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
   * Returns the Default locale, which is cached to avoid a costly conversion.
   * @return
   */
  public static java.util.Locale getDefaultLocale()
  {
    return defaultLocale;
  }

  public static String getInstanceXMLschemaLocation()
  {
    return CommonProperties.class.getResource(XMLConstants.INSTANCE_XSD).toString();
  }
  
  public static String getTransactionXMLschemaLocation()
  {
    return CommonProperties.class.getResource(XMLConstants.TRANSACTION_XSD).toString();
  }

  public static String getTransactionRecordXMLschemaLocation()
  {
    return CommonProperties.class.getResource(XMLConstants.TRANSACTIONRECORD_XSD).toString();
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
    return Singleton.INSTANCE.appName;
  }

  /**
   * DO NOT CALL THIS METHOD.
   * This method only exists so that transaction logging and importing can be tested!
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
}
