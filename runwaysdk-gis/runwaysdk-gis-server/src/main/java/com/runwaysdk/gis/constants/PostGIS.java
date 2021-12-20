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

import java.util.Hashtable;

import com.runwaysdk.constants.PostgreSQL;

public class PostGIS extends PostgreSQL 
{
  
  @Override
  public Object[][] getContents()
  {
    return contents;
  }
  
  static final Object[][] contents =
  {
    { "time.format", timeFormat }, 
    { "date.format", dateFormat }, 
    { "datetime.format", dateTimeFormat }, 
    { "attributeTypes", makeTable() },
    { "dbErrorCodes", dbErrorCodes }, 
    { "seriousDBErrorCodes",  seriousDBErrorCodes}, 
    { "databaseClass", com.runwaysdk.gis.dataaccess.database.PostGIS.class.getName() },
  };
	
  /**
   * Creates the HashTable that maps core attribute types to database types
   * 
   * @return MdAttribute->DbType HashTable
   */
  protected static Hashtable<String, String> makeTable()
  {
    Hashtable<String, String> hashtable = PostgreSQL.makeTable();
    // add additional geometry attributes.
    hashtable.put(MdAttributePointInfo.CLASS,             "POINT");
    hashtable.put(MdAttributeLineStringInfo.CLASS,        "LINESTRING");
    hashtable.put(MdAttributePolygonInfo.CLASS,           "POLYGON");
    hashtable.put(MdAttributeMultiPointInfo.CLASS,        "MULTIPOINT");
    hashtable.put(MdAttributeMultiLineStringInfo.CLASS,   "MULTILINESTRING");
    hashtable.put(MdAttributeMultiPolygonInfo.CLASS,      "MULTIPOLYGON");
    return hashtable;
  }
  
  
}
