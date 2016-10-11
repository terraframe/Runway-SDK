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
package com.runwaysdk.gis.format;

import java.util.Locale;

import com.runwaysdk.format.AbstractFormat;
import com.runwaysdk.format.FormatException;
import com.runwaysdk.format.ParseException;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;

public class GISFormat extends AbstractFormat<Geometry>
{

  public GISFormat()
  {
    super();
  }
  
  /**
   * 
   */
  @Override
  public String format(Object value, Locale locale) throws FormatException
  {
    try
    {
      if(value == null)
      {
        return null;
      }
      
      if(value instanceof Geometry)
      {
        Geometry g = (Geometry) value;
        return g.toText();
      }
      else
      {
        return value.toString();
      }
    }
    catch(Throwable t)
    {
      throw this.createFormatException(t, locale, value);
    }
  }

  /**
   * 
   */
  @Override
  public Geometry parse(String value, Locale locale) throws ParseException
  {
    try
    {
      if(value == null)
      {
        return null;
      }
      
      WKTReader reader = new WKTReader();
      return reader.read(value);
    }
    catch(Throwable t)
    {
      throw this.createParseException(t, locale, value);
    }
  }

}
