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
 * 
 */
package com.runwaysdk.dataaccess;

import java.util.Set;

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
public interface AttributeMultiReferenceIF extends AttributeIF
{
  /**
   * 
   * @return
   */
  public BusinessDAOIF[] dereference();

  /**
   * Returns a list of mappings between this object and business items from the
   * database and will OVERWRITE the values that have been added but not
   * applied.
   * 
   * @return Returns the itemIdList.
   */
  public Set<String> getItemIdList();

  /**
   * Returns a cached list of mappings between this object and referenced
   * business items. If an enumeration item is removed from the master list,
   * this cache may still contain an ID to the item.
   * 
   * @return Returns the itemIdList.
   */
  public Set<String> getCachedItemIdSet();

  /**
   * Returns the concrete attribute metadata that defines this attribute. If
   * this is defined by a concrete attribute, this object is returned. If it is
   * a virtual attribute, then the concrete attribute it references is returned.
   * 
   * @return MdAttributeMultiReferenceDAOIF that defines the this attribute
   */
  public MdAttributeMultiReferenceDAOIF getMdAttributeConcrete();

}
