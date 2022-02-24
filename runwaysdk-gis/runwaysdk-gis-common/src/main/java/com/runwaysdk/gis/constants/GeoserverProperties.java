/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.gis.constants;

import java.net.MalformedURLException;
import java.util.ResourceBundle;

import org.slf4j.LoggerFactory;

import com.runwaysdk.business.BusinessDTO;import com.runwaysdk.constants.CommonProperties;

import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;

public class GeoserverProperties
{
  private static final String          GEOSERVER_PROPERTIES = "geoserver";

  private ResourceBundle               bundle;

  private static GeoServerRESTPublisher publisher;

  private static GeoServerRESTReader    reader;

  private GeoserverProperties()
  {
    bundle = ResourceBundle.getBundle(GEOSERVER_PROPERTIES, CommonProperties.getDefaultLocale(),
        BusinessDTO.class.getClassLoader());
  }

  private static class Singleton
  {
    private static GeoserverProperties INSTANCE = new GeoserverProperties();
  }

  private static ResourceBundle getBundle()
  {
    return Singleton.INSTANCE.bundle;
  }

  public static String getWorkspace()
  {
    return getBundle().getString("geoserver.workspace");
  }

  public static String getStore()
  {
    return getBundle().getString("geoserver.store");
  }

  public static String getAdminUser()
  {
    return getBundle().getString("admin.user");
  }

  public static String getAdminPassword()
  {
    return getBundle().getString("admin.password");
  }

  public static String getRemotePath()
  {
    return getBundle().getString("geoserver.remote.path");
  }

  public static String getLocalPath()
  {
    return getBundle().getString("geoserver.local.path");
  }

  public static String getGeoserverSLDDir()
  {
    return getBundle().getString("geoserver.sld.dir");
  }

  public static String getGeoserverGWCDir()
  {
    return getBundle().getString("geoserver.gwc.dir");
  }
  
  public static int getZoomStart()
  {
    return Integer.valueOf(getBundle().getString("geoserver.gwc.zoomStart"));
  }

  public static int getZoomStop()
  {
    return Integer.valueOf(getBundle().getString("geoserver.gwc.zoomStop"));
  }

  /**
   * Returns the Geoserver REST publisher.
   * 
   * @return
   */
  public static synchronized GeoServerRESTPublisher getPublisher()
  {
    if(publisher == null)
    {
      publisher = new GeoServerRESTPublisher(getLocalPath(), getAdminUser(), getAdminPassword());
    }
    
    return publisher;
  }

  /**
   * Returns the Geoserver REST reader.
   */
  public static synchronized GeoServerRESTReader getReader()
  {
    if(reader == null)
    {
      try
      {
        reader = new GeoServerRESTReader(getLocalPath(), getAdminUser(), getAdminPassword());
      }
      catch (MalformedURLException e)
      {
        // We don't know if this is being called via client or server code, so log
        // the error and throw an NPE to the calling code for its error handling mechanism.
        String msg = "The "+GeoserverProperties.class.getSimpleName()+"."+GeoServerRESTReader.class.getSimpleName()+" is null.";
        LoggerFactory.getLogger(GeoserverProperties.class.getClass()).error(msg, e);
        
        throw new RuntimeException(msg);
      }
    }
    
    return reader;
  }
}

