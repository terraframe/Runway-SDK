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
package com.runwaysdk.gis.transport.conversion.business;

import java.util.Locale;

import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.format.AbstractFormatFactory;
import com.runwaysdk.format.FormatFactory;
import com.runwaysdk.gis.AttributeLineStringParseException;
import com.runwaysdk.gis.AttributeMultiLineStringParseException;
import com.runwaysdk.gis.AttributeMultiPointParseException;
import com.runwaysdk.gis.AttributeMultiPolygonParseException;
import com.runwaysdk.gis.AttributePointParseException;
import com.runwaysdk.gis.AttributePolygonParseException;
import com.runwaysdk.gis.constants.GISConstants;
import com.runwaysdk.session.Session;
import com.runwaysdk.transport.conversion.business.MutableDTOToMutable.PluginIF;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

public class GISMutableDTOToMutable implements PluginIF
{
  public String getModuleIdentifier()
  {
    return GISConstants.GIS_SYSTEM_PACKAGE;
  }

  public Class<?> getParamClassAttribute(MdAttributeDAOIF mdAttributeIF)
  {
    Class<?> paramClass = null;
    String attributeType = mdAttributeIF.javaType(false);

    if (attributeType.equals(Point.class.getName()))
    {
      paramClass = Point.class;
    }
    else if (attributeType.equals(LineString.class.getName()))
    {
      paramClass = LineString.class;
    }
    else if (attributeType.equals(Polygon.class.getName()))
    {
      paramClass = Polygon.class;
    }
    else if (attributeType.equals(MultiPoint.class.getName()))
    {
      paramClass = MultiPoint.class;
    }
    else if (attributeType.equals(MultiLineString.class.getName()))
    {
      paramClass = MultiLineString.class;
    }
    else if (attributeType.equals(MultiPolygon.class.getName()))
    {
      paramClass = MultiPolygon.class;
    }

    return paramClass;
  }

  public Object parseTypeSafeAttribute(MdAttributeDAOIF mdAttributeIF, MutableDTO mutableDTO, Object param)
  {
    String attributeName = mdAttributeIF.definesAttribute();
    String attributeType = mdAttributeIF.javaType(false);

    FormatFactory factory = AbstractFormatFactory.getFormatFactory();
    Locale locale = Session.getCurrentLocale();
    
    if (attributeType.equals(Point.class.getName()))
    {
      Point point = (Point)mutableDTO.getObjectValue(attributeName);

      if (point != null)
      {
        param = point;
      }
      else
      {
        String stringValue = mutableDTO.getValue(attributeName);
        try
        {
          param = factory.getFormat(Point.class).parse(stringValue, locale);
        }
        catch(Throwable e)
        {
          String errMsg = "["+stringValue+"] is not a valid ["+Point.class.getName()+"]";
          throw new AttributePointParseException(errMsg, mdAttributeIF.getDisplayLabel(locale), stringValue);
        }
      }
    }
    else if (attributeType.equals(LineString.class.getName()))
    {
      LineString lineString = (LineString)mutableDTO.getObjectValue(attributeName);

      if (lineString != null)
      {
        param = lineString;
      }
      else
      {
        String stringValue = mutableDTO.getValue(attributeName);
        try
        {
          param = factory.getFormat(LineString.class).parse(stringValue, locale);
        }
        catch(Throwable e)
        {
          String errMsg = "["+stringValue+"] is not a valid ["+LineString.class.getName()+"]";
          throw new AttributeLineStringParseException(errMsg, mdAttributeIF.getDisplayLabel(locale), stringValue);
        }
      }
    }
    else if (attributeType.equals(Polygon.class.getName()))
    {
      Polygon polygon = (Polygon)mutableDTO.getObjectValue(attributeName);

      if (polygon != null)
      {
        param = polygon;
      }
      else
      {
        String stringValue = mutableDTO.getValue(attributeName);
        try
        {
          param = factory.getFormat(Polygon.class).parse(stringValue, locale);
        }
        catch(Throwable e)
        {
          String errMsg = "["+stringValue+"] is not a valid ["+Polygon.class.getName()+"]";
          throw new AttributePolygonParseException(errMsg, mdAttributeIF.getDisplayLabel(locale), stringValue);
        }
      }
    }
    else if (attributeType.equals(MultiPoint.class.getName()))
    {
      MultiPoint multiPoint = (MultiPoint)mutableDTO.getObjectValue(attributeName);

      if (multiPoint != null)
      {
        param = multiPoint;
      }
      else
      {
        String stringValue = mutableDTO.getValue(attributeName);
        try
        {
          param = factory.getFormat(MultiPoint.class).parse(stringValue, locale);
        }
        catch(Throwable e)
        {
          String errMsg = "["+stringValue+"] is not a valid ["+MultiPoint.class.getName()+"]";
          throw new AttributeMultiPointParseException(errMsg, mdAttributeIF.getDisplayLabel(locale), stringValue);
        }
      }
    }
    else if (attributeType.equals(MultiLineString.class.getName()))
    {
      MultiLineString multiLineString = (MultiLineString)mutableDTO.getObjectValue(attributeName);

      if (multiLineString != null)
      {
        param = multiLineString;
      }
      else
      {
        String stringValue = mutableDTO.getValue(attributeName);
        try
        {
          param = factory.getFormat(MultiLineString.class).parse(stringValue, locale);
        }
        catch(Throwable e)
        {
          String errMsg = "["+stringValue+"] is not a valid ["+MultiLineString.class.getName()+"]";
          throw new AttributeMultiLineStringParseException(errMsg, mdAttributeIF.getDisplayLabel(locale), stringValue);
        }
      }
    }
    else if (attributeType.equals(MultiPolygon.class.getName()))
    {
      MultiPolygon multiPolygon = (MultiPolygon)mutableDTO.getObjectValue(attributeName);

      if (multiPolygon != null)
      {
        param = multiPolygon;
      }
      else
      {
        String stringValue = mutableDTO.getValue(attributeName);
        try
        {
          param = factory.getFormat(MultiPolygon.class).parse(stringValue, locale);
        }
        catch(Throwable e)
        {
          String errMsg = "["+stringValue+"] is not a valid ["+MultiPolygon.class.getName()+"]";
          throw new AttributeMultiPolygonParseException(errMsg, mdAttributeIF.getDisplayLabel(locale), stringValue);
        }
      }
    }

    return param;
  }
}
