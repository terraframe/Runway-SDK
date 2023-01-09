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
package com.runwaysdk.gis.controller;

import java.util.Locale;

import com.runwaysdk.gis.AttributeLineStringParseProblemDTO;
import com.runwaysdk.gis.AttributeMultiLineStringParseProblemDTO;
import com.runwaysdk.gis.AttributeMultiPointParseProblemDTO;
import com.runwaysdk.gis.AttributeMultiPolygonParseProblemDTO;
import com.runwaysdk.gis.AttributePointParseProblemDTO;
import com.runwaysdk.gis.AttributePolygonParseProblemDTO;
import com.runwaysdk.gis.constants.GISConstants;
import com.runwaysdk.AttributeNotificationDTO;
import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.controller.ParseProblemDTO;
import com.runwaysdk.controller.ParameterFactory.PluginIF;
import com.runwaysdk.transport.metadata.AttributeMdDTO;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

public class GISParameterFactory implements PluginIF
{
  public String getModuleIdentifier()
  {
    return GISConstants.GIS_SYSTEM_PACKAGE;
  }

  public AttributeNotificationDTO getException(Class<?> c, String name, Locale locale, String value)
  {
    if (c.equals(Point.class))
    {
      return new AttributePointParseProblemDTO(name, locale, value);
    }
    else if (c.equals(LineString.class))
    {
      return new AttributeLineStringParseProblemDTO(name, locale, value);
    }
    else if (c.equals(MultiLineString.class))
    {
      return new AttributeMultiLineStringParseProblemDTO(name, locale, value);
    }
    else if (c.equals(MultiPoint.class))
    {
      return new AttributeMultiPointParseProblemDTO(name, locale, value);
    }
    else if (c.equals(MultiPolygon.class))
    {
      return new AttributeMultiPolygonParseProblemDTO(name, locale, value);
    }
    else if (c.equals(Polygon.class))
    {
      return new AttributePolygonParseProblemDTO(name, locale, value);
    }
    
    return null;
  }

  public ParseProblemDTO getException(Class<?> c, MutableDTO mutableDTO, AttributeMdDTO attributeDTO, Locale locale, String value)
  {
    if (c.equals(Point.class))
    {
      return new AttributePointParseProblemDTO(mutableDTO, attributeDTO, locale, value);
    }
    else if (c.equals(LineString.class))
    {
      return new AttributeLineStringParseProblemDTO(mutableDTO, attributeDTO, locale, value);
    }
    else if (c.equals(MultiLineString.class))
    {
      return new AttributeMultiLineStringParseProblemDTO(mutableDTO, attributeDTO, locale, value);
    }
    else if (c.equals(MultiPoint.class))
    {
      return new AttributeMultiPointParseProblemDTO(mutableDTO, attributeDTO, locale, value);
    }
    else if (c.equals(MultiPolygon.class))
    {
      return new AttributeMultiPolygonParseProblemDTO(mutableDTO, attributeDTO, locale, value);
    }
    else if (c.equals(Polygon.class))
    {
      return new AttributePolygonParseProblemDTO(mutableDTO, attributeDTO, locale, value);
    }

    return null;
  }
}
