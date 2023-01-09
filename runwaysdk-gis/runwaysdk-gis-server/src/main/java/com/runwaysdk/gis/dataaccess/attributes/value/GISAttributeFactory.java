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
package com.runwaysdk.gis.dataaccess.attributes.value;

import java.util.Set;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.attributes.value.Attribute;
import com.runwaysdk.dataaccess.attributes.value.AttributeFactory.PluginIF;
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

  public Attribute createAttribute(MdAttributeConcreteDAOIF mdAttributeIF, String attributeName,
      String definingType, Object attributeValue,
      Set<MdAttributeConcreteDAOIF> entityMdAttributeIFset)
  {
    Attribute attribute = null;
    if (mdAttributeIF.getType().equals(MdAttributePointInfo.CLASS))
    {
      if (attributeValue != null && attributeValue instanceof JtsGeometry)
      {
        Geometry geometry = ((JtsGeometry)attributeValue).getGeometry();

        attribute = new AttributePoint(attributeName, (Point)geometry, definingType, mdAttributeIF, entityMdAttributeIFset);
      }

      if (attribute == null)
      {
        attribute = new AttributePoint(attributeName, null, definingType, mdAttributeIF, entityMdAttributeIFset);
      }
    }
    else if (mdAttributeIF.getType().equals(MdAttributeLineStringInfo.CLASS))
    {
      if (attributeValue != null && attributeValue instanceof JtsGeometry)
      {
        Geometry geometry = ((JtsGeometry)attributeValue).getGeometry();

        attribute = new AttributeLineString(attributeName, (LineString)geometry, definingType, mdAttributeIF, entityMdAttributeIFset);
      }

      if (attribute == null)
      {
        attribute = new AttributeLineString(attributeName, null, definingType, mdAttributeIF, entityMdAttributeIFset);
      }
    }
    else if (mdAttributeIF.getType().equals(MdAttributeShapeInfo.CLASS))
    {
      if (attributeValue != null && attributeValue instanceof JtsGeometry)
      {
        Geometry geometry = ((JtsGeometry)attributeValue).getGeometry();
        
        attribute = new AttributeShape(attributeName, geometry, definingType, mdAttributeIF, entityMdAttributeIFset);
      }
      
      if (attribute == null)
      {
        attribute = new AttributeShape(attributeName, null, definingType, mdAttributeIF, entityMdAttributeIFset);
      }
    }
    else if (mdAttributeIF.getType().equals(MdAttributePolygonInfo.CLASS))
    {
      if (attributeValue != null && attributeValue instanceof JtsGeometry)
      {
        Geometry geometry = ((JtsGeometry)attributeValue).getGeometry();

        attribute = new AttributePolygon(attributeName, (Polygon)geometry, definingType, mdAttributeIF, entityMdAttributeIFset);
      }

      if (attribute == null)
      {
        attribute = new AttributePolygon(attributeName, null, definingType, mdAttributeIF, entityMdAttributeIFset);
      }
    }
    else if (mdAttributeIF.getType().equals(MdAttributeMultiPointInfo.CLASS))
    {
      if (attributeValue != null && attributeValue instanceof JtsGeometry)
      {
        Geometry geometry = ((JtsGeometry)attributeValue).getGeometry();

        attribute = new AttributeMultiPoint(attributeName, (MultiPoint)geometry, definingType, mdAttributeIF, entityMdAttributeIFset);
      }

      if (attribute == null)
      {
        attribute = new AttributeMultiPoint(attributeName, null, definingType, mdAttributeIF, entityMdAttributeIFset);
      }
    }
    else if (mdAttributeIF.getType().equals(MdAttributeMultiLineStringInfo.CLASS))
    {
      if (attributeValue != null && attributeValue instanceof JtsGeometry)
      {
        Geometry geometry = ((JtsGeometry)attributeValue).getGeometry();

        attribute = new AttributeMultiLineString(attributeName, (MultiLineString)geometry, definingType, mdAttributeIF, entityMdAttributeIFset);
      }

      if (attribute == null)
      {
        attribute = new AttributeMultiLineString(attributeName, null, definingType, mdAttributeIF, entityMdAttributeIFset);
      }
    }
    else if (mdAttributeIF.getType().equals(MdAttributeMultiPolygonInfo.CLASS))
    {
      if (attributeValue != null && attributeValue instanceof JtsGeometry)
      {
        Geometry geometry = ((JtsGeometry)attributeValue).getGeometry();

        attribute = new AttributeMultiPolygon(attributeName, (MultiPolygon)geometry, definingType, mdAttributeIF, entityMdAttributeIFset);
      }
      if (attribute == null)
      {
        attribute = new AttributeMultiPolygon(attributeName, null, definingType, mdAttributeIF, entityMdAttributeIFset);
      }
    }

    return attribute;
  }
}
