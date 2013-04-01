/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
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
 ******************************************************************************/
package com.runwaysdk.dataaccess;

import java.util.List;

import com.runwaysdk.ComponentIF;

public interface EntityDAOIF extends ComponentIF
{
  /**
   * Column name of the attribute that specifies the ID.
   */
  public static final String ID_COLUMN               = "id";
  
  /**
   * Column name of the attribute that specifies the type.
   */
  public static final String TYPE_COLUMN             = "type";
  
  /**
   * Column name of the attribute that specifies the key
   */
  public static final String KEY_COLUMN              = "key_name";
  
  /**
   * The sequence number column for this MdEntity
   */
  public static final String SEQUENCE_COLUMN         = "seq";
  
  /**
   * Returns the root id of this component.
   * 
   * @return root id of this component.
   */
  public String getRootId();
  
  /**
   * Returns true if this object originated from a cache, false otherwise.
   * @return true if this object originated from a cache, false otherwise.
   */
  public boolean isFromCache();
  
  /**
   * Returns true if this instance has been written to the database. It does not
   * indicate if it has been committed to the database.
   * 
   * @return true if this instance has been written to the database. It does not
   *         indicate if it has been committed to the database.
   */
  public boolean isAppliedToDB();


  /**
   * If true then this object originated from a cache, false otherwise.
   * @param isFromCache
   */
  public void setIsFromCacheMRU(boolean isFromCache);

  /**
   * Returns a LinkedList of MdAttributeIF objects representing metadata for each attribute
   * of this object's class.
   *
   * <br/><b>Precondition:</b> true <br/><b>Postcondition:</b> true
   *
   * @return a LinkedList of MdAttributeIF objects representing metadata for each attribute
   * of this object's class.
   */
  public List<? extends MdAttributeConcreteDAOIF> getMdAttributeDAOs();

  /**
   * Returns the attribute object of the given name.
   *
   * @param name
   * @return
   */
  public AttributeIF getAttributeIF(String name);

  /**
   * Returns an array of attribute objects.
   *
   * @return array of attribute objects.
   */
  public AttributeIF[] getAttributeArrayIF();

  /**
   * Returns the blob of the given name
   *
   * @param attributeName
   * @return
   */
  public byte[] getBlob(String attributeName);

  /**
   * Returns the MdEntityIF defining this EntityDAO
   */
  public MdEntityDAOIF getMdClassDAO();

  /**
   * Returns the site from which this object is mastered.
   * 
   * @return site from which this object is mastered.
   */
  public String getSiteMaster();
  
  /**
   * Deep clones this read-only object and returns the new copy.
   *
   * @return Deep clone of this EntityDAO.
   */
  public EntityDAO getEntityDAO();

  /**
   * Returns true if the entity has an owner, false otherwise.
   * @return if the entity has an owner, false otherwise.
   */
  public boolean hasOwner();

  /**
   * Returns a copy of the given EntityDAO instance, with a new id and mastered at the current site.
   * The state of the object is new and has not been applied to the database.
   *
   * @return a copy of the given EntityDAO instance
   */
  public EntityDAO copy();
}
