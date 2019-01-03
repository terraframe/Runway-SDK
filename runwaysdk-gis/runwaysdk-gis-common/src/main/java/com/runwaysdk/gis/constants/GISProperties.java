/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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

import java.util.ResourceBundle;

import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.constants.CommonProperties;

public class GISProperties
{
  private static final String PROPERTIES_FILE = "gis";

  private ResourceBundle      bundle;

  private GISProperties()
  {
    bundle = ResourceBundle.getBundle(PROPERTIES_FILE, CommonProperties.getDefaultLocale(), BusinessDTO.class.getClassLoader());
  }

  private static class Singleton
  {
    private static GISProperties INSTANCE = new GISProperties();
  }

  private static ResourceBundle getBundle()
  {
    return Singleton.INSTANCE.bundle;
  }

  /**
   * Returns the package that will hold the generated MdBusiness artifacts when
   * a new Universal is defined.
   * 
   * @return
   */
  public static String getUniversalPackage()
  {
    return getBundle().getString("universal.package");
  }
}
