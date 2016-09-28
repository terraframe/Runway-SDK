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
package com.runwaysdk.gis.transport.metadata;

import com.runwaysdk.gis.constants.GISConstants;
import com.runwaysdk.gis.constants.MdAttributeLineStringInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiLineStringInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiPointInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiPolygonInfo;
import com.runwaysdk.gis.constants.MdAttributePointInfo;
import com.runwaysdk.gis.constants.MdAttributePolygonInfo;
import com.runwaysdk.transport.metadata.AttributeMdDTO;
import com.runwaysdk.transport.metadata.AttributeMdDTOFactory.PluginIF;

public class GISAttributeMdDTOFactory implements PluginIF
{
  public String getModuleIdentifier()
  {
    return GISConstants.GIS_SYSTEM_PACKAGE;
  }
  
  public AttributeMdDTO createAttributeMdDTO(String attributeType)
  {
    AttributeMdDTO attributeMdDTO = null;
    
    if(attributeType.equals(MdAttributePointInfo.CLASS))
    {
      attributeMdDTO = new AttributePointMdDTO();
    }
    else if(attributeType.equals(MdAttributeLineStringInfo.CLASS))
    {
      attributeMdDTO = new AttributeLineStringMdDTO();
    }
    else if(attributeType.equals(MdAttributePolygonInfo.CLASS))
    {
      attributeMdDTO = new AttributePolygonMdDTO();
    }
    else if(attributeType.equals(MdAttributeMultiPointInfo.CLASS))
    {
      attributeMdDTO = new AttributeMultiPointMdDTO();
    }
    else if(attributeType.equals(MdAttributeMultiLineStringInfo.CLASS))
    {
      attributeMdDTO = new AttributeMultiLineStringMdDTO();
    }
    else if(attributeType.equals(MdAttributeMultiPolygonInfo.CLASS))
    {
      attributeMdDTO = new AttributeMultiPolygonMdDTO();
    }
    
    return attributeMdDTO;
  }
}
