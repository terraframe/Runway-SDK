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
package com.runwaysdk.gis.dataaccess.attributes.tranzient;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import com.runwaysdk.dataaccess.attributes.tranzient.Attribute;
import com.runwaysdk.dataaccess.attributes.tranzient.AttributeFactory.PluginIF;
import com.runwaysdk.gis.constants.GISConstants;
import com.runwaysdk.gis.constants.MdAttributeLineStringInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiLineStringInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiPointInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiPolygonInfo;
import com.runwaysdk.gis.constants.MdAttributePointInfo;
import com.runwaysdk.gis.constants.MdAttributePolygonInfo;
import com.runwaysdk.gis.constants.MdAttributeShapeInfo;

import net.postgis.jdbc.jts.JtsGeometry;

public class GISAttributeFactory implements PluginIF
{
  public String getModuleIdentifier()
  {
    return GISConstants.GIS_SYSTEM_PACKAGE;
  }
  
  public Attribute createAttribute(String attributeType, String mdAttributeKey,
      String attributeName, String definingType, 
      Object attributeValue)
  {
    Attribute attribute = null;
    if (attributeType.equals(MdAttributePointInfo.CLASS))
    {
      if (attributeValue != null && attributeValue instanceof JtsGeometry)
      {
        Geometry geometry = ((JtsGeometry)attributeValue).getGeometry();
        
        if (geometry instanceof Point)
        {     
          attribute = new AttributePoint(attributeName, mdAttributeKey, definingType, (Point)geometry);
        }
      }
      
      if (attribute == null)
      {
        attribute = new AttributePoint(attributeName, mdAttributeKey, definingType);
      }
    }
    else if (attributeType.equals(MdAttributeLineStringInfo.CLASS))
    {
      if (attributeValue != null && attributeValue instanceof JtsGeometry)
      {
        Geometry geometry = ((JtsGeometry)attributeValue).getGeometry();
        
        if (geometry instanceof LineString)
        {     
          attribute = new AttributeLineString(attributeName, mdAttributeKey, definingType, (LineString)geometry);
        }
      }
      
      if (attribute == null)
      {
        attribute = new AttributeLineString(attributeName, mdAttributeKey, definingType);
      }
    }
    else if (attributeType.equals(MdAttributeShapeInfo.CLASS))
    {
      if (attributeValue != null && attributeValue instanceof JtsGeometry)
      {
        Geometry geometry = ((JtsGeometry)attributeValue).getGeometry();
        
        attribute = new AttributeShape(attributeName, mdAttributeKey, definingType, geometry);
      }
      
      if (attribute == null)
      {
        attribute = new AttributeShape(attributeName, mdAttributeKey, definingType);
      }
    }
    else if (attributeType.equals(MdAttributePolygonInfo.CLASS))
    {
      if (attributeValue != null && attributeValue instanceof JtsGeometry)
      {
        Geometry geometry = ((JtsGeometry)attributeValue).getGeometry();
        
        if (geometry instanceof Polygon)
        {     
          attribute = new AttributePolygon(attributeName, mdAttributeKey, definingType, (Polygon)geometry);
        }
      }
      
      if (attribute == null)
      {
        attribute = new AttributePolygon(attributeName, mdAttributeKey, definingType);
      }
    }
    else if (attributeType.equals(MdAttributeMultiPointInfo.CLASS))
    {
      if (attributeValue != null && attributeValue instanceof JtsGeometry)
      {
        Geometry geometry = ((JtsGeometry)attributeValue).getGeometry();
        
        if (geometry instanceof MultiPoint)
        {     
          attribute = new AttributeMultiPoint(attributeName, mdAttributeKey, definingType, (MultiPoint)geometry);
        }
      }
      
      if (attribute == null)
      {
        attribute = new AttributeMultiPoint(attributeName, mdAttributeKey, definingType);
      }
    }
    else if (attributeType.equals(MdAttributeMultiLineStringInfo.CLASS))
    {
      if (attributeValue != null && attributeValue instanceof JtsGeometry)
      {
        Geometry geometry = ((JtsGeometry)attributeValue).getGeometry();
        
        if (geometry instanceof MultiLineString)
        {     
          attribute = new AttributeMultiLineString(attributeName, mdAttributeKey, definingType, (MultiLineString)geometry);
        }
      }
      
      if (attribute == null)
      {
        attribute = new AttributeMultiLineString(attributeName, mdAttributeKey, definingType);
      }
    }    
    else if (attributeType.equals(MdAttributeMultiPolygonInfo.CLASS))
    {
      if (attributeValue != null && attributeValue instanceof JtsGeometry)
      {
        Geometry geometry = ((JtsGeometry)attributeValue).getGeometry();
        
        if (geometry instanceof MultiPolygon)
        {     
          attribute = new AttributeMultiPolygon(attributeName, mdAttributeKey, definingType, (MultiPolygon)geometry);
        }
      }
      
      if (attribute == null)
      {
        attribute = new AttributeMultiPolygon(attributeName, mdAttributeKey, definingType);
      }
    }
    
    return attribute;
  }
}
