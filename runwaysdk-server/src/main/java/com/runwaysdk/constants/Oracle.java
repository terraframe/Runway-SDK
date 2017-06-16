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
 * Contains static properties for operation of the core with Oracle. Login info
 * is specified in database.properties. Typical default values for logging in to
 * Oracle are:
 * 
 * <pre>
 * port=1521
 * databaseName=orcl
 * user=runway
 * password=runway
 * rootuser=system
 * rootpassword=system
 * rootdatabase=orcl
 * </pre>
 * 
 * @author Eric
 */
public class Oracle extends ListResourceBundle
{
  public static final String NAME = "Oracle";

  @Override
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = { { "time.format", "HH24:MI:SS" }, { "date.format", "YYYY-MM-DD" }, { "datetime.format", "YYYY-MM-DD HH24:MI:SS" }, { "attributeTypes", makeTable() }, { "dbErrorCodes", "" }, { "seriousDBErrorCodes", "" }, { "databaseClass", com.runwaysdk.dataaccess.database.general.Oracle.class.getName() }, };

  /**
   * Creates the HashTable that maps core attribute types to database types
   * 
   * @return MdAttribute->DbType HashTable
   */
  private static Hashtable<String, String> makeTable()
  {
    Hashtable<String, String> hashtable = new Hashtable<String, String>();
    hashtable.put(MdAttributeBooleanInfo.CLASS, "number");
    hashtable.put(MdAttributeIntegerInfo.CLASS, "number");
    hashtable.put(MdAttributeLongInfo.CLASS, "number");
    hashtable.put(MdAttributeFloatInfo.CLASS, "number");
    hashtable.put(MdAttributeDoubleInfo.CLASS, "number");
    hashtable.put(MdAttributeDecimalInfo.CLASS, "number");
    hashtable.put(MdAttributeCharacterInfo.CLASS, "varchar2");
    hashtable.put(MdAttributeFileInfo.CLASS, "varchar2");
    hashtable.put(MdAttributeTextInfo.CLASS, "clob");
    hashtable.put(MdAttributeClobInfo.CLASS, "clob");
    hashtable.put(MdAttributeBlobInfo.CLASS, "blob");
    hashtable.put(MdAttributeTimeInfo.CLASS, "date");
    hashtable.put(MdAttributeDateInfo.CLASS, "date");
    hashtable.put(MdAttributeDateTimeInfo.CLASS, "date");
    hashtable.put(MdAttributeReferenceInfo.CLASS, "char");
    hashtable.put(MdAttributeTermInfo.CLASS, "char");
    hashtable.put(MdAttributeEnumerationInfo.CLASS, "char");
    hashtable.put(MdAttributeMultiReferenceInfo.CLASS, "char");
    hashtable.put(MdAttributeMultiTermInfo.CLASS, "char");
    hashtable.put(MdAttributeIndicatorInfo.CLASS, "char");
    hashtable.put(MdAttributeStructInfo.CLASS, "char");
    hashtable.put(MdAttributeLocalCharacterInfo.CLASS, "char");
    hashtable.put(MdAttributeLocalTextInfo.CLASS, "char");
    hashtable.put(MdAttributeHashInfo.CLASS, "varchar2");
    hashtable.put(MdAttributeSymmetricInfo.CLASS, "clob");
    return hashtable;
  }
}
