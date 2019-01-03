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
package com.runwaysdk.business.generation;

import com.runwaysdk.business.generation.AbstractServerGenerator.PluginIF;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.gis.Util;
import com.runwaysdk.gis.constants.GISConstants;
import com.runwaysdk.gis.constants.RunwayGisProperties;

public class GISAbstractServerGenerator implements PluginIF
{
  public String getModuleIdentifier()
  {
    return GISConstants.GIS_SYSTEM_PACKAGE;
  }

  public String getClassDirectory(String fileSystemPackage)
  {
    if (LocalProperties.isRunwayEnvironment() && Util.isPluginPackageFileSystem(fileSystemPackage))
    {      
      return RunwayGisProperties.getRunwayBin()+"/";
    }
    else
    {
      return null;
    }
  }
  
  public String getBaseDirectory(String fileSystemPackage)
  {
    if (LocalProperties.isRunwayEnvironment() && Util.isPluginPackageFileSystem(fileSystemPackage))
    {      
      return RunwayGisProperties.getServerSrc()+"/";
    }
    else
    {
      return null;
    }
  }

  public String getSourceDirectory(String fileSystemPackage)
  {
    if (LocalProperties.isRunwayEnvironment() && Util.isPluginPackageFileSystem(fileSystemPackage))
    {      
      return RunwayGisProperties.getServerSrc()+"/";
    }
    else
    {
      return null;
    }
  }
}

