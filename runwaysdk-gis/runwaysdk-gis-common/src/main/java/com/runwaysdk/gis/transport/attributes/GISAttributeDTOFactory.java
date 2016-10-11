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
package com.runwaysdk.gis.transport.attributes;

import com.runwaysdk.gis.constants.GISConstants;
import com.runwaysdk.gis.constants.MdAttributeLineStringInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiLineStringInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiPointInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiPolygonInfo;
import com.runwaysdk.gis.constants.MdAttributePointInfo;
import com.runwaysdk.gis.constants.MdAttributePolygonInfo;
import com.runwaysdk.transport.attributes.AttributeDTO;
import com.runwaysdk.transport.attributes.AttributeDTOFactory;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class GISAttributeDTOFactory implements AttributeDTOFactory.PluginIF
{
  public String getModuleIdentifier()
  {
    return GISConstants.GIS_SYSTEM_PACKAGE;
  }

  public AttributeDTO createAttributeDTO(String attributeName, String type, Object value, 
      boolean readable, boolean writable, boolean modified)
  {
    AttributeDTO attributeDTO = null;

    if(type.equals(MdAttributePointInfo.CLASS))
    {
      if (value != null)
      {
        if (value instanceof Geometry)
        {
          Geometry geometry = (Geometry)value;
        
          if (geometry instanceof Point)
          {     
            attributeDTO = new AttributePointDTO(attributeName, (Point)value, readable, writable, modified);
          }
        }
        else if (value instanceof String)
        {
          attributeDTO = new AttributePointDTO(attributeName, (String)value, readable, writable, modified);
        }
      }

      if (attributeDTO == null)
      {
        attributeDTO = new AttributePointDTO(attributeName, (Point)null, readable, writable, modified);
      }
    }
    else if(type.equals(MdAttributeLineStringInfo.CLASS))
    {
      if (value != null)
      {
        if (value instanceof Geometry)
        {
          Geometry geometry = (Geometry)value;
        
          if (geometry instanceof LineString)
          {     
            attributeDTO = new AttributeLineStringDTO(attributeName, (LineString)value, readable, writable, modified);
          }
        }
        else if (value instanceof String)
        {
          attributeDTO = new AttributeLineStringDTO(attributeName, (String)value, readable, writable, modified);
        }
      }

      if (attributeDTO == null)
      {
        attributeDTO = new AttributeLineStringDTO(attributeName, (LineString)null, readable, writable, modified);
      }
    }
    else if(type.equals(MdAttributePolygonInfo.CLASS))
    {
      if (value != null)
      {
        if (value instanceof Geometry)
        {
          Geometry geometry = (Geometry)value;
        
          if (geometry instanceof Polygon)
          {     
            attributeDTO = new AttributePolygonDTO(attributeName, (Polygon)value, readable, writable, modified);
          }
        }
        else if (value instanceof String)
        {
          attributeDTO = new AttributePolygonDTO(attributeName, (String)value, readable, writable, modified);
        }
      }

      if (attributeDTO == null)
      {
        attributeDTO = new AttributePolygonDTO(attributeName, (Polygon)null, readable, writable, modified);
      }
    }
    else if(type.equals(MdAttributeMultiPointInfo.CLASS))
    {
      if (value != null)
      {
        if (value instanceof Geometry)
        {
          Geometry geometry = (Geometry)value;
        
          if (geometry instanceof MultiPoint)
          {     
            attributeDTO = new AttributeMultiPointDTO(attributeName, (MultiPoint)value, readable, writable, modified);
          }
        }
        else if (value instanceof String)
        {
          attributeDTO = new AttributeMultiPointDTO(attributeName, (String)value, readable, writable, modified);
        }
      }

      if (attributeDTO == null)
      {
        attributeDTO = new AttributeMultiPointDTO(attributeName, (MultiPoint)null, readable, writable, modified);
      }
    }
    else if(type.equals(MdAttributeMultiLineStringInfo.CLASS))
    {
      if (value != null)
      {
        if (value instanceof Geometry)
        {
          Geometry geometry = (Geometry)value;
        
          if (geometry instanceof MultiLineString)
          {     
            attributeDTO = new AttributeMultiLineStringDTO(attributeName, (MultiLineString)value, readable, writable, modified);
          }
        }
        else if (value instanceof String)
        {
          attributeDTO = new AttributeMultiLineStringDTO(attributeName, (String)value, readable, writable, modified);
        }
      }

      if (attributeDTO == null)
      {
        attributeDTO = new AttributeMultiLineStringDTO(attributeName, (MultiLineString)null, readable, writable, modified);
      }
    }
    else if(type.equals(MdAttributeMultiPolygonInfo.CLASS))
    {
      if (value != null)
      {
        if (value instanceof Geometry)
        {
          Geometry geometry = (Geometry)value;
        
          if (geometry instanceof MultiPolygon)
          {     
            attributeDTO = new AttributeMultiPolygonDTO(attributeName, (MultiPolygon)value, readable, writable, modified);
          }
        }
        else if (value instanceof String)
        {
          attributeDTO = new AttributeMultiPolygonDTO(attributeName, (String)value, readable, writable, modified);
        }
      }

      if (attributeDTO == null)
      {
        attributeDTO = new AttributeMultiPolygonDTO(attributeName, (MultiPolygon)null, readable, writable, modified);
      }
    }
    
    
    return attributeDTO;
  }
}
