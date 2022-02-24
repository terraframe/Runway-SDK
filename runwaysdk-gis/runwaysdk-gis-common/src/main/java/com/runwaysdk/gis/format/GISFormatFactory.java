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
package com.runwaysdk.gis.format;

import com.runwaysdk.format.Format;
import com.runwaysdk.format.FormatFactory;
import com.runwaysdk.format.StandardFormat;
import com.runwaysdk.gis.constants.GISConstants;
import com.vividsolutions.jts.geom.Geometry;

/**
 * FormatFactory and Plugin to format/parse geometry values.
 */
public class GISFormatFactory implements StandardFormat.PluginIF, FormatFactory
{

  public GISFormatFactory()
  {
    super();
  }
  
  @Override
  public String getModuleIdentifier()
  {
    return GISConstants.GIS_SYSTEM_PACKAGE;
  }

  @SuppressWarnings("unchecked")
  public <T> Format<T> getFormat(Class<T> c)
  {
    Format<?> format = null;
    if (Geometry.class.isAssignableFrom(c))
    {
      format = new GISFormat();
    }
    
    return (Format<T>) format;
  }
}
