/**
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
 */
package com.runwaysdk.dataaccess;

import java.util.List;

import com.runwaysdk.dataaccess.metadata.MdStructDAO;

public interface MdStructDAOIF extends MdEntityDAOIF
{
  /**
   * Name of the table used to store intances of this class.
   */
  public static final String TABLE                     = "md_struct";
  
  
  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAOIF#getBusinessDAO()
   */
  public MdStructDAO getBusinessDAO();
  
  /**
   * Returns all MdAttributeStruct that reference this class.
   * @return all MdAttributeStruct that reference this class.
   */
  public List<? extends MdAttributeStructDAOIF> getMdAttributeStruct();
  
  /**
   * Returns an empty list of MdStructIF objects, as MdStructs cannot be extended.
   * entity. Only non abstract entities are returned (i.e. entities that can be
   * instantiated)
   * 
   * @return an empty list of MdStructIF objects, as MdStructs cannot be extended.
   *         entity. Only non abstract entities are returned (i.e. entities that can be
   *         instantiated)
   */
  public List<? extends MdStructDAOIF> getAllConcreteSubClasses();
  
  /**
   * Returns the reference to this object, as an MdStruct is always the root 
   * of its own hierarchy.
   * 
   * @return reference to this object, as an MdStruct is always the root 
   *         of its own hierarchy.
   */
  public MdStructDAOIF getRootMdClassDAO();
  
  /**
   * Returns an empty list of MdStructIF objects that represent entites that are
   * subclasses of the given entity, including all recursive entities.  This list is empty
   * because structs to dnot inherit.
   * 
   * @return list of MdStructIF objects that represent entites that are
   *         subclasses of the given entity, including all recursive entities.
   */
  @SuppressWarnings("unchecked")
  public List<MdStructDAOIF> getAllSubClasses();
  
  /**
   * Returns an empty list of MdStructIF, as an MdStruct cannot have any sub entity. 
   * 
   * @return empty list of MdStructIF, as an MdStruct cannot have any sub entity. 
   */
  public List<MdStructDAOIF> getSubClasses();

  /**
   * Returns an empty list of MdStructIF, as an MdStruct cannot have a super entity. 
   * 
   * @return empty list of MdStructIF, as an MdStruct cannot have a super entity.  
   */
  @SuppressWarnings("unchecked")
  public List<MdStructDAOIF> getSuperClasses();

}
