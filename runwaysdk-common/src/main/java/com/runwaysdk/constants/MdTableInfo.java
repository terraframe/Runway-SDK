/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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

public interface MdTableInfo extends MdClassInfo
{
  /**
   * Class {@link MdTableInfo}.
   */
  public static final String CLASS   = Constants.METADATA_PACKAGE+".MdTable";
  
  /**
   * OID.
   */
  public static final String ID_VALUE                   = "f694e1fd-b79d-377e-9be3-6e7cf900003a";  
  
  /**
   * Name of the attribute that stores the name of the the database table used
   * to store instances of the class defined by this metadata object.
   */
  public static final String TABLE_NAME                 = "tableName";
  
  /**
   * Databasee column of the attribute that stores the name of the the database table used
   * to store instances of the class defined by this metadata object.
   */
  public static final String COLUMN_TABLE_NAME          = "table_name";
  
  /**
   * The maximum size of the name of the database name.
   */
  public static final String MAX_TABLE_NAME             = "128";
  
}
