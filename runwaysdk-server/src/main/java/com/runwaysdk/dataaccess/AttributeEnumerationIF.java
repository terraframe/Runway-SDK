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
/*
 * Created on Aug 8, 2005
 *
 */
package com.runwaysdk.dataaccess;

import java.util.Set;


/**
 * @author nathan
 *
 */
public interface AttributeEnumerationIF extends AttributeIF
{
  /**
   *
   * @return
   */
  public EnumerationItemDAOIF[] dereference();

  /**
   * Returns a cached list of mappings between this object and enumeration items.
   * If an enumeration item is removed from the master list, this cache may
   * still contain an OID to the item.
   * @return Returns the enumItemIdList.
   */
  public Set<String> getCachedEnumItemIdSet();

  /**
   * Returns a list of mappings between this object and enumeration items
   * from the database and will OVERWRITE the values that have been added but not applied.
   * @return Returns the enumItemIdList.
   */
  public Set<String> getEnumItemIdList();

  /**
   * Returns the concrete attribute metadata that defines this attribute.  If this is defined
   * by aa concrete attribute, this object is returned.  If it is a virtual attribute, then the
   * concrete attribute it references is returned.
   *
   * @return MdAttributeEnumerationDAOIF that defines the this attribute
   */
  public MdAttributeEnumerationDAOIF getMdAttributeConcrete();

}
