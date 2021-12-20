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
package com.runwaysdk.gis;

import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.gis.constants.GISConstants;

public class Util
{
  /**
   * Returns true if the package directory contains system metadata classes, false otherwise.
   * Assumes the package has been converted to a directory, where the "."'s have been replaced 
   * with "/"'s.
   *
   * @param fileSystemPackage
   * 
   * @return true if the package contains system metadata classes, false otherwise.
   */
  public static boolean isPluginPackageFileSystem(String fileSystemPackage)
  {
    String systemPackage = CommonGenerationUtil.replacePackageDotsWithSlashes(GISConstants.GIS_SYSTEM_PACKAGE);
    if (fileSystemPackage.indexOf(systemPackage) == 0)
    {
      return true;
    }
    else
    {
      return false;
    }
  }
}
