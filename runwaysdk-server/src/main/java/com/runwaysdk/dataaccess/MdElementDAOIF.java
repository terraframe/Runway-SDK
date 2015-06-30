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
package com.runwaysdk.dataaccess;

import java.util.List;

import com.runwaysdk.dataaccess.metadata.MdElementDAO;


public interface MdElementDAOIF extends MdEntityDAOIF
{
  /**
   * Name of the table used to store intances of this class.
   */
  public static final String TABLE                     = "md_element";
  /**
   * Column name of the attribute that indicates if this type is abstract or not.
   */
  public static final String ABSTRACT_COLUMN           = "is_abstract";
  
  /**
   * Returns true if the type is abstract, false otherwise.
   * @return true if the type is abstract, false otherwise.
   */
  public boolean isAbstract();
  
  /**
   * Returns true if the type can be extended, false otherwise.
   * @return true if the type can be extended, false otherwise.
   */
  public boolean isExtendable();

  /**
   * Returns an array of MdElementIF that defines immediate subentites of this
   * entity.
   * 
   * @return an array of MdElementIF that defines immediate subentites of this
   *         entity.
   */
  public List<? extends MdElementDAOIF> getSubClasses();
  
  /**
   * Returns a list of MdElementIF objects that represent entites that are
   * subclasses of the given entity, including all recursive entities.
   * 
   * @return list of MdElementIF objects that represent entites that are
   *         subclasses of the given entity, including all recursive entities.
   */
  public List<? extends MdElementDAOIF> getAllSubClasses();
  
  /**
   * Returns a list of MdElementIF objects that are subclasses of the given
   * entity. Only non abstract entities are returned (i.e. entities that can be
   * instantiated)
   * 
   * @return list of MdElementIF objects that are subclasses of the given entity.
   *         Only non abstract entities are returned (i.e. entities that can be
   *         instantiated)
   */
  public List<? extends MdElementDAOIF> getAllConcreteSubClasses(); 
  
  /**
   * Returns an MdElementIF representing the super entity of this entity, or null if
   * it does not have one.
   * 
   * @return an MdElementIF representing the super entity of this entity, or null if
   * it does not have one.
   */
  public MdElementDAOIF getSuperClass();
  
  /**
   * Returns a list of MdElementIF object representing every parent of this
   * MdElementIF partaking in an inheritance relationship, including this class.
   * 
   * @return a list of parent MdElementIF objects.
   */
  public List<? extends MdElementDAOIF> getSuperClasses();
  
  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAOIF#getBusinessDAO()
   */
  public MdElementDAO getBusinessDAO();

}
