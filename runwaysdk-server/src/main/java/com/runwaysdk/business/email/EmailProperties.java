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
package com.runwaysdk.business.email;

import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.configuration.LegacyPropertiesSupport;
import com.runwaysdk.configuration.ConfigurationManager.ConfigGroup;
import com.runwaysdk.configuration.ConfigurationReaderIF;

public class EmailProperties
{
  
  /**
   * The SMTP protocol.
   */
  public static final String PROTOCOL = "smtp";
  
  /**
   * The SMTP port.
   */
  public static final int PORT = 25;
  
  /**
   * The resource bundle with the properties.
   */
  private ConfigurationReaderIF bundle;
  
  /**
   * Private singleton constructor.
   */
  private EmailProperties()
  {
    bundle = ConfigurationManager.getReader(ConfigGroup.SERVER, LegacyPropertiesSupport.pickRelevant("email.properties", "server.properties"));
  }
  
  /**
   * A holder class for access to the singleton. Allows for lazy instantiation and thread
   * safety because the class is not loaded until the first access to INSTANCE.
   */
  private static class Singleton
  {
    private static final EmailProperties INSTANCE = new EmailProperties();
  }
  
  public static String getSmtpHost()
  {
    return Singleton.INSTANCE.bundle.getString("email.host");
  }
  
  public static String getFromAddress()
  {
    return Singleton.INSTANCE.bundle.getString("email.fromAddress");
  }
  
  public static String getLoginUser()
  {
    return Singleton.INSTANCE.bundle.getString("email.loginUser");
  }
  
  public static String getLoginPass()
  {
    return Singleton.INSTANCE.bundle.getString("email.loginPass");
  }
  
  public static int getKeyExpire()
  {
    return Integer.parseInt(Singleton.INSTANCE.bundle.getString("email.keyExpire"));
  }
}
