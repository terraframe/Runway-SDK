/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.constants;

import java.util.Hashtable;
import java.util.ListResourceBundle;

/**
 * Contains static properties for operation of the core with postgres. Login
 * info is specified in database.properties. Typical default values for logging
 * in to postgres are:
 * 
 * <pre>
 * port=5432
 * databaseName=runwaydb
 * user=runway
 * password=runway
 * rootuser=root
 * rootpassword=root
 * rootdatabase=template1
 * </pre>
 * 
 * @author Eric
 */
public class PostgreSQL extends ListResourceBundle
{
  public static final String NAME = "Postgresql";

  @Override
  public Object[][] getContents()
  {
    return contents;
  }

  protected static final String timeFormat          = "HH:mm:ss";

  protected static final String dateFormat          = "yyyy-MM-dd";

  protected static final String dateTimeFormat      = "yyyy-MM-dd HH:mm:ss";

  protected static final String dbErrorCodes        = "1003,1006,1007,8000,8001,8003,8004,8006,8007,9000,24000,27000,28000,34000,38000,38001,38002,38003,38004,39000,39001,39004,42501,42723,42725,42803,42846,42883,44000,53000,53100,53200,53300,54023,57000,57014,58030,0A000,0F000,0F001,0L000,0LP01,0P000,2202E,22P04,22P05,2B000,2BP01,2F000,2F002,2F003,2F004,2F005,39P01,39P02,3D000,42P03,42P04,42P05,42P06,42P08,42P11,42P12,42P13,42P14,55P02,57P03,58P01,58P02,F0000,F0001,P0000,P0001,XX000,XX001,XX002,";

  protected static final String seriousDBErrorCodes = "1004,1008,2000,2001,3000,21000,22000,22001,22002,22003,22004,22005,22007,22008,22009,22010,22011,22012,22015,22018,22019,22020,22021,22022,22023,22024,22025,22026,22027,23000,23001,23502,23503,23514,25000,25001,25002,25003,25004,25005,25006,25007,25008,26000,40000,40001,40002,40003,42000,42601,42602,42611,42622,42702,42703,42704,42710,42712,42804,42809,42830,42939,54000,54001,54011,55000,55006,0B000,2200B,2200C,2200D,2200F,2200G,2201B,2201E,2201F,2201G,22P01,22P02,22P03,22P06,25P01,25P02,2D000,3B000,3B001,3F000,40P01,42P01,42P02,42P07,42P09,42P10,42P15,42P16,42P17,42P18,55P03";

  static final Object[][]       contents            = { { "time.format", timeFormat }, { "date.format", dateFormat }, { "datetime.format", dateTimeFormat }, { "attributeTypes", makeTable() }, { "dbErrorCodes", dbErrorCodes }, { "seriousDBErrorCodes", seriousDBErrorCodes }, { "databaseClass", com.runwaysdk.dataaccess.database.general.PostgreSQL.class.getName() }, };

  /**
   * Creates the HashTable that maps core attribute types to database types
   * 
   * @return MdAttribute->DbType HashTable
   */
  protected static Hashtable<String, String> makeTable()
  {
    Hashtable<String, String> hashtable = new Hashtable<String, String>();
    hashtable.put(MdAttributeBooleanInfo.CLASS, "int");
    hashtable.put(MdAttributeIntegerInfo.CLASS, "int");
    hashtable.put(MdAttributeLongInfo.CLASS, "bigint");
    hashtable.put(MdAttributeFloatInfo.CLASS, "numeric");
    hashtable.put(MdAttributeDoubleInfo.CLASS, "numeric");
    hashtable.put(MdAttributeDecimalInfo.CLASS, "numeric");
    hashtable.put(MdAttributeCharacterInfo.CLASS, "varchar");
    hashtable.put(MdAttributeFileInfo.CLASS, "varchar");
    hashtable.put(MdAttributeTextInfo.CLASS, "text");
    hashtable.put(MdAttributeClobInfo.CLASS, "text");
    hashtable.put(MdAttributeBlobInfo.CLASS, "bytea");
    hashtable.put(MdAttributeTimeInfo.CLASS, "time");
    hashtable.put(MdAttributeDateInfo.CLASS, "date");
    hashtable.put(MdAttributeDateTimeInfo.CLASS, "timestamp");
    hashtable.put(MdAttributeReferenceInfo.CLASS, "uuid");
//    hashtable.put(MdAttributeReferenceInfo.CLASS, "char");
    hashtable.put(MdAttributeTermInfo.CLASS, "uuid");
    hashtable.put(MdAttributeEnumerationInfo.CLASS, "char");
    hashtable.put(MdAttributeMultiReferenceInfo.CLASS, "char");
    hashtable.put(MdAttributeMultiTermInfo.CLASS, "char");
    hashtable.put(MdAttributeIndicatorInfo.CLASS, "char");
    hashtable.put(MdAttributeStructInfo.CLASS, "uuid");
    hashtable.put(MdAttributeLocalCharacterInfo.CLASS, "uuid");
    hashtable.put(MdAttributeLocalTextInfo.CLASS, "uuid");
    hashtable.put(MdAttributeHashInfo.CLASS, "varchar");
    hashtable.put(MdAttributeSymmetricInfo.CLASS, "text");
    hashtable.put(MdAttributeUUIDInfo.CLASS, "uuid");
    
    return hashtable;
  }
}
