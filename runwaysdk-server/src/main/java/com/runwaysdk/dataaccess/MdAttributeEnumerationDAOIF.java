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
/**
 * Created on Aug 28, 2005
 *
 */
package com.runwaysdk.dataaccess;

import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;

/**
 * @author nathan
 *
 */
public interface MdAttributeEnumerationDAOIF extends MdAttributeRefDAOIF
{
  /**
   * Name of the table used to store intances of this class.
   */
  public static final String TABLE                             = "md_attribute_enumeration";

  /**
   * Column name of the attribute that references the enumeration definition
   * that this attribute uses.
   */
  public static final String MD_ENUMERATION_COLUMN             = "md_enumeration";

  /**
   * Column name of the attribute that references the enumeration definition
   * that this attribute uses.
   */
  public static final String CACHE_COLUMN_NAME_DB_COLUMN       = "cache_column_name";
  /**
   * Delimiter string used on database cache column names.
   */
  public static final String CACHE_COLUMN_DELIMITER            = "_c";

  /**
   *Returns the metadata object that defines the enumeration that this attribute uses,
   * or null if it does not reference anything.
   *
   * @return the metadata object that defines the enumeration that this attribute uses,
   *         or null if it does not reference anything.
   */
  public MdEnumerationDAOIF getMdEnumerationDAO();

  /**
   * Returns true if the attribute can select more than one value from the master list, false otherwise.
   * @return true if the attribute can select more than one value from the master list, false otherwise.
   */
  public boolean selectMultiple();

  /**
   * Returns the name of the database column that caches enumeration mappings.
   *
   * @return the name of the database column that caches enumeration mappings.
   */
  public String getCacheColumnName();
  
  /**
   * Returns the name of the database column that caches enumeration mappings as specified in the metadata.
   *
   * @return the name of the database column that caches enumeration mappings as specified in the metadata.
   */
  public String getDefinedCacheColumnName();

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeEnumerationDAO getBusinessDAO();
}
