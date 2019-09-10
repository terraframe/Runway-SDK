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
package com.runwaysdk.constants;

import java.util.Hashtable;

import com.runwaysdk.gis.constants.MdAttributeLineStringInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiLineStringInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiPointInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiPolygonInfo;
import com.runwaysdk.gis.constants.MdAttributePointInfo;
import com.runwaysdk.gis.constants.MdAttributePolygonInfo;

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
    { "databaseClass", com.runwaysdk.dataaccess.database.general.PostGIS.class.getName() },
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
