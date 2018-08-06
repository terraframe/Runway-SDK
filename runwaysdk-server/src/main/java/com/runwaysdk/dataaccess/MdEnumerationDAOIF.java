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
 * Created on Aug 14, 2005
 *
 */
package com.runwaysdk.dataaccess;

import java.util.List;

import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;

/**
 * @author nathan
 *
 */
public interface MdEnumerationDAOIF extends MdTypeDAOIF
{
  /**
   * Name of the table used to store intances of this class.
   */
  public static final String TABLE   = "md_enumeration";
  
  /**
   * Column name of the attribute that stores the name of the the database table used to store
   * instances of the class defined by this metadata object.
   */
  public static final String TABLE_NAME_COLUMN         = "table_name";
  /**
   * Column name of ID of an enumeration attribute of a BusinessDAO
   * 
   */
  public static final String SET_ID_COLUMN             = "set_id";
  /**
   * Column name of a BusinessDAO that is an enumeration item.
   */
  public static final String ITEM_ID_COLUMN            = "item_id";
  /**
   * Returns the name of the method used on the generated master list query class to find
   * items that participate in this enumeration.
   * @return name of the method used on the generated master list query class to find
   * items that participate in this enumeration.
   */
  public String getQueryMethodName();

  /**
   * Returns the name of the method used on the generated master list query class to find
   * items that do not participate in this enumeration.
   * @return name of the method used on the generated master list query class to find
   * items that do not participate in this enumeration.
   */
  public String getNegatedQueryMethodName();

  /**
   * Returns the name of the table used to store instances of the class that this object defines.
   * @return name of the table used to store instances of the class that this object defines.
   */
  public String getTableName();

  /**
   *Returns the type of the enumeration that this object defines.
   * @return the type of the enumeration that this object defines.
   */
  public String definesEnumeration();

  /**
   *Returns the name of the enumeration that this object defines.
   * @return the name of the enumeration that this object defines.
   */
  public String getEnumerationName();

  /**
   *True if this enumeration includes all items from the master enumeration list,
   * false otherwise.
   * @return True if this enumeration includes all items from the master enumeration list,
   *         false otherwise.
   */
  public boolean includeAllEnumerationItems();

  /**
   * Adds the given enumeration item to this enumeration.
   * @param enumerationItem item to add.
   */
  public void addEnumItem(EnumerationItemDAO enumerationItem);

  /**
   * Adds the given enumeration item to this enumeration.
   * @param oid oid of the enumerationItem item to add.
   */
  @Transaction
  public void addEnumItem(String oid);

  /**
   *Returns the MdBusinessIF that defines the master list of
   * items used buy this enumeration.
   * @return the MdBusinessIF that defines the master list of
   *         items used buy this enumeration.
   */
  public MdBusinessDAOIF getMasterListMdBusinessDAO();


  /**
   *
   * @param enumerationName
   * @return
   */
  public List<BusinessDAOIF> getAllEnumItems();

  /**
   * alphabetical order
   * @param enumerationName
   * @return
   */
  public List<BusinessDAOIF> getAllEnumItemsOrdered();

  /**
   *
   * @param enumItemId
   * @return
   */
  public boolean isValidEnumerationItem(String enumItemId);

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAOIF#getBusinessDAO()
   */
  public MdEnumerationDAO getBusinessDAO();
}
