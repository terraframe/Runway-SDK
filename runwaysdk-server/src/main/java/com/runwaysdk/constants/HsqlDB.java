/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.runwaysdk.constants;

import java.util.Hashtable;
import java.util.ListResourceBundle;

public class HsqlDB extends ListResourceBundle
{
  public static final String NAME = "HsqlDB";

  @Override
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = { { "time.format", "HH:mm:ss" }, { "date.format", "yyyy-MM-dd" }, { "datetime.format", "yyyy-MM-dd HH:mm:ss" }, { "attributeTypes", makeTable() }, { "dbErrorCodes", "" }, { "seriousDBErrorCodes", "" }, { "databaseClass", com.runwaysdk.dataaccess.database.general.HsqlDB.class.getName() }, };

  /**
   * Creates the HashTable that maps core attribute types to database types
   * 
   * @return MdAttribute->DbType HashTable
   */
  private static Hashtable<String, String> makeTable()
  {
    Hashtable<String, String> hashtable = new Hashtable<String, String>();
    hashtable.put(MdAttributeBooleanInfo.CLASS, "int");
    hashtable.put(MdAttributeIntegerInfo.CLASS, "int");
    hashtable.put(MdAttributeLongInfo.CLASS, "bigint");
    hashtable.put(MdAttributeFloatInfo.CLASS, "decimal");
    hashtable.put(MdAttributeDoubleInfo.CLASS, "decimal");
    hashtable.put(MdAttributeDecimalInfo.CLASS, "decimal");
    hashtable.put(MdAttributeCharacterInfo.CLASS, "varchar");
    hashtable.put(MdAttributeFileInfo.CLASS, "varchar");
    hashtable.put(MdAttributeTextInfo.CLASS, "longvarchar");
    hashtable.put(MdAttributeClobInfo.CLASS, "longvarchar");
    hashtable.put(MdAttributeBlobInfo.CLASS, "varbinary");
    hashtable.put(MdAttributeTimeInfo.CLASS, "varchar");
    hashtable.put(MdAttributeDateInfo.CLASS, "varchar");
    hashtable.put(MdAttributeDateTimeInfo.CLASS, "varchar");
    hashtable.put(MdAttributeReferenceInfo.CLASS, "char");
    hashtable.put(MdAttributeTermInfo.CLASS, "char");
    hashtable.put(MdAttributeEnumerationInfo.CLASS, "char");
    hashtable.put(MdAttributeMultiReferenceInfo.CLASS, "char");
    hashtable.put(MdAttributeMultiTermInfo.CLASS, "char");
    hashtable.put(MdAttributeStructInfo.CLASS, "char");
    hashtable.put(MdAttributeLocalCharacterInfo.CLASS, "char");
    hashtable.put(MdAttributeLocalTextInfo.CLASS, "char");
    hashtable.put(MdAttributeHashInfo.CLASS, "varchar");
    hashtable.put(MdAttributeSymmetricInfo.CLASS, "varchar");

    return hashtable;
  }
}
