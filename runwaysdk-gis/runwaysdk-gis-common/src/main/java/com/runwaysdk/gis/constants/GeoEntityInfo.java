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
package com.runwaysdk.gis.constants;

import com.runwaysdk.constants.BusinessInfo;

public interface GeoEntityInfo extends BusinessInfo
{
  public static final String   CLASS              = GISConstants.GEO_PACKAGE+".GeoEntity";
  
  public static final String   ID_VALUE           = "8b521d00-d772-3efc-9525-d88f520000d8";
  
  public static final String   TABLE              = "geo_entity";
  
  
  public static final String   GEOID              = "geoId";
  
  public static final String   GEOPOINT           = "geoPoint";
  
  public static final String   GEOMULTIPOINT      = "geoMultiPoint";
  
  public static final String   GEOLINE            = "geoLine";
  
  public static final String   GEOMULTILINE       = "geoMultiLine";
  
  public static final String   GEOPOLYGON         = "geoPolygon";
  
  public static final String   GEOMULTIPOLYGON    = "geoMultiPolygon";
  
  public static final String[] DEFAULT_ATTRIBUTES = new String[] {GEOID, GEOPOINT, GEOMULTIPOINT, GEOLINE, GEOMULTILINE, GEOPOLYGON, GEOMULTIPOLYGON};
  
}
