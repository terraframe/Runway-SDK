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

import com.runwaysdk.dataaccess.metadata.MdExceptionDAO;

public interface MdExceptionDAOIF extends MdLocalizableDAOIF
{
  /**
   * Name of the table used to store intances of this class.
   */
  public static final String TABLE   = "md_exception";
  
  /**
   *Returns the MdExceptionIF that is the root of the hierarchy that this type belongs to.
   * returns a reference to inself if it is the root.
   *
   * @return MdExceptionIF that is the root of the hierarchy that this type belongs to.
   * returns a reference to inself if it is the root.
   */
  public MdExceptionDAOIF getRootMdClassDAO();

  /**
   * Returns an array of MdExceptionIF that defines immediate subclasses of this class.
   * @return an array of MdExceptionIF that defines immediate subclasses of this class.
   */
  public List<MdExceptionDAOIF> getSubClasses();

  /**
   * Returns a list of MdExceptionIF objects that are subclasses of the given
   * entity. Only non abstract entities are returned (i.e. entities that can be
   * instantiated)
   * 
   * @return list of MdExceptionIF objects that are subclasses of the given entity.
   *         Only non abstract entities are returned (i.e. entities that can be
   *         instantiated)
   */
  public List<MdExceptionDAOIF> getAllConcreteSubClasses();
  
  /**
   *Returns a list of MdExceptionIF objects that represent entites 
   * that are subclasses of the given entity, including all recursive entities.
   *
   * @return list of MdExceptionIF objects that represent entites 
   * that are subclasses of the given entity, including all recursive entities.
   */
  public List<MdExceptionDAOIF> getAllSubClasses();
  
  /**
   * Returns an MdExceptionIF representing the superclass of this class, or null if
   * this class is basic.
   * 
   * @return an MdExceptionIF representing the superclass of this class, or null if
   *         the class is basic.
   */
  public MdExceptionDAOIF getSuperClass();
  
  /**
   * Returns a list of MdExceptionIF instances representing every
   * parent of this MdExceptionIF partaking in an inheritance relationship.
   * 
   * @return a list of MdExceptionIF instances that are parents of this class.
   */
  public List<MdExceptionDAOIF> getSuperClasses();

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdExceptionDAO getBusinessDAO();
}
