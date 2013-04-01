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


public class RunwayProperties
{
  /**
   * The local.properties configuration file
   */
  private ProfileReader props;
  
  /**
   * A holder class for access to the singleton. Allows for lazy instantiation and thread
   * safety because the class is not loaded until the first access to INSTANCE.
   */
  private static class Singleton
  {
    private static final RunwayProperties INSTANCE = new RunwayProperties();
  }
  
  /**
   * A convenience method to save us from typing Singleton.INSTANCE.props on
   * every getter.
   * 
   * @return
   */
  private static ProfileReader instance()
  {
    return RunwayProperties.Singleton.INSTANCE.props;
  }
  
  /**
   * Private constructor loads the local.properties reader
   */
  private RunwayProperties()
  {
    props = ProfileManager.getBundle("common/runway.properties");
  }
  
  /**
   * Gets the runway bin directory
   */
  public static String getRunwayServerBin()
  {
    return instance().getString("runway.classes.server");
  }
  
  public static String getRunwayCommonBin()
  {
    return instance().getString("runway.classes.common");
  }
  
  public static String getRunwayClientBin()
  {
    return instance().getString("runway.classes.client");
  }
  
  /**
   * @return The root of the runway client source 
   */
  public static String getClientSrc()
  {
    return instance().getString("runway.src.client");
  }
  
  /**
   * @return The root of the runway client source 
   */
  public static String getCommonSrc()
  {
    return instance().getString("runway.src.common");
  }
  
  /**
   * @return The root of the runway client source 
   */
  public static String getServerSrc()
  {
    return instance().getString("runway.src.server");
  }
}
