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


public interface MdEnumerationInfo extends MdTypeInfo
{

  /**
   * Class MdEnumeration.
   */
  public static final String CLASS   = Constants.METADATA_PACKAGE+".MdEnumeration";
  /**
   * Name of the attribute that stores the name of the the database table used to store
   * instances of the class defined by this metadata object.
   */
  public static final String TABLE_NAME                = "tableName";
  /**
   * Name of the attribute that specifies the id of the metadata that defines 
   * the type used to store attributes on this relationship.
   */
  public static final String MASTER_MD_BUSINESS        = "masterMdBusiness";
  /**
   * Name of the attribute that specifies if all values in an enumeration master list
   * should be included in this enumeration.
   */
  public static final String INCLUDE_ALL               = "includeAll";
  /**
   * ID of an enumeration attribute of a BusinessDAO
   * 
   */
  public static final String SET_ID                    = "set_id";
  /**
   * Name of a BusinessDAO that is an enumeration item.
   */
  public static final String ITEM_ID                   = "item_id";

}
