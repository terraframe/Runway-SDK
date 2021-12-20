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
package com.runwaysdk.dataaccess.io.dataDefinition;

import java.util.HashMap;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.io.dataDefinition.ExportVisitor.PluginIF;
import com.runwaysdk.gis.constants.GISConstants;
import com.runwaysdk.gis.constants.MdAttributeGeometryInfo;
import com.runwaysdk.gis.dataaccess.MdAttributeGeometryDAOIF;

public class GISExportVisitor implements PluginIF
{
  public String getModuleIdentifier()
  {
    return GISConstants.GIS_SYSTEM_PACKAGE;
  }

  public void addParameters(HashMap<String, String> parameters, MdAttributeDAOIF mdAttributeIF)
  {
    if (mdAttributeIF instanceof MdAttributeGeometryDAOIF)
    {
      MdAttributeGeometryDAOIF mdAttributeGeometryDAOIF = (MdAttributeGeometryDAOIF)mdAttributeIF;
      parameters.put(XMLTags.SRID_ATTRIBUTE, mdAttributeGeometryDAOIF.getValue(MdAttributeGeometryInfo.SRID));
      parameters.put(XMLTags.DIMENSION_ATTRIBUTE, mdAttributeGeometryDAOIF.getValue(MdAttributeGeometryInfo.DIMENSION));
    }
  }
}
