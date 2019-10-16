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
package com.runwaysdk.gis.dataaccess.attributes.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.postgis.jts.JtsGeometry;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeFactory.PluginIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.gis.constants.GISConstants;
import com.runwaysdk.gis.constants.MdAttributeLineStringInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiLineStringInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiPointInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiPolygonInfo;
import com.runwaysdk.gis.constants.MdAttributePointInfo;
import com.runwaysdk.gis.constants.MdAttributePolygonInfo;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class GISAttributeFactory implements PluginIF
{
  public String getModuleIdentifier()
  {
    return GISConstants.GIS_SYSTEM_PACKAGE;
  }
  
  /**
   * Returns and Object with the vaule from the column and row.
   * @param row
   * @param columnAlias
   * @param attributeType
   * @return Object with the vaule from the column and row.
   * @throws  
   */
  public Object getColumnValueFromRow(ResultSet resultSet, String columnAlias, String attributeType) 
  {
    Object columnValue = null;

    String columnAliasLowerCase = columnAlias.toLowerCase();
    
    try
    {
      if (attributeType.equals(MdAttributePointInfo.CLASS) ||
          attributeType.equals(MdAttributeLineStringInfo.CLASS) ||
          attributeType.equals(MdAttributePolygonInfo.CLASS) ||
          attributeType.equals(MdAttributeMultiPointInfo.CLASS) ||
          attributeType.equals(MdAttributeMultiLineStringInfo.CLASS) ||
          attributeType.equals(MdAttributeMultiPolygonInfo.CLASS))
      {
        columnValue = resultSet.getObject(columnAliasLowerCase);
        
//        org.postgis.PGgeometry pgGeometry = (org.postgis.PGgeometry)resultSet.getObject(columnAliasLowerCase);
//        org.postgis.Geometry geometry = pgGeometry.getGeometry();  
//        
//        return new WKTReader().read( geometry.getTypeString()+geometry.getValue());
        
      }
    }
    catch (SQLException sqlEx)
    {
      Database.throwDatabaseException(sqlEx);
    }
//    catch(ParseException pe)
//    {
//      String errMsg = "Invalid WKT came out of a geometry column in the database.";
//      throw new ProgrammingErrorException(errMsg);
//    }
    return columnValue;
  }


  /**
   * 
   */
  public Attribute createAttribute(String mdAttributeKey, String attributeType, String attributeName,
      String definingType, Object attributeValue)
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
  
  /**
   * 
  public Attribute createAttribute(String mdAttributeKey, String attributeType, String attributeName,
      String definingType, Object attributeValue)
  {
    Attribute attribute = null;
    if (attributeType.equals(MdAttributePointInfo.CLASS))
    {
      if (attributeValue != null && attributeValue instanceof Point)
      {
        attribute = new AttributePoint(attributeName, mdAttributeKey, definingType, (Point)attributeValue);
      }
      
      if (attribute == null)
      {
        attribute = new AttributePoint(attributeName, mdAttributeKey, definingType);
      }
    }
    else if (attributeType.equals(MdAttributeLineStringInfo.CLASS))
    {
      if (attributeValue != null && attributeValue instanceof LineString)
      {
        attribute = new AttributeLineString(attributeName, mdAttributeKey, definingType, (LineString)attributeValue);
      }
      
      if (attribute == null)
      {
        attribute = new AttributeLineString(attributeName, mdAttributeKey, definingType);
      }
    }
    else if (attributeType.equals(MdAttributePolygonInfo.CLASS))
    {
      if (attributeValue != null && attributeValue instanceof Polygon)
      {
        attribute = new AttributePolygon(attributeName, mdAttributeKey, definingType, (Polygon)attributeValue);
      }
      
      if (attribute == null)
      {
        attribute = new AttributePolygon(attributeName, mdAttributeKey, definingType);
      }
    }
    else if (attributeType.equals(MdAttributeMultiPointInfo.CLASS))
    {
      if (attributeValue != null && attributeValue instanceof MultiPoint)
      {
        attribute = new AttributeMultiPoint(attributeName, mdAttributeKey, definingType, (MultiPoint)attributeValue);
      }
      
      if (attribute == null)
      {
        attribute = new AttributeMultiPoint(attributeName, mdAttributeKey, definingType);
      }
    }
    else if (attributeType.equals(MdAttributeMultiLineStringInfo.CLASS))
    {
      if (attributeValue != null && attributeValue instanceof MultiLineString)
      {
        attribute = new AttributeMultiLineString(attributeName, mdAttributeKey, definingType, (MultiLineString)attributeValue);
      }
      
      if (attribute == null)
      {
        attribute = new AttributeMultiLineString(attributeName, mdAttributeKey, definingType);
      }
    }    
    else if (attributeType.equals(MdAttributeMultiPolygonInfo.CLASS))
    {
      if (attributeValue != null && attributeValue instanceof MultiPolygon)
      {  
        attribute = new AttributeMultiPolygon(attributeName, mdAttributeKey, definingType, (MultiPolygon)attributeValue);
      }
      
      if (attribute == null)
      {
        attribute = new AttributeMultiPolygon(attributeName, mdAttributeKey, definingType);
      }
    }
    
    return attribute;
  }*/
}
