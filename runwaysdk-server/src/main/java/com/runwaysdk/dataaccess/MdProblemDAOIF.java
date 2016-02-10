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

import com.runwaysdk.dataaccess.metadata.MdProblemDAO;

public interface MdProblemDAOIF extends MdNotificationDAOIF
{
  /**
   * Name of the table used to store intances of this class.
   */
  public static final String TABLE   = "md_problem";

  /**
   *Returns the MdProblemIF that is the root of the hierarchy that this type belongs to.
   * returns a reference to inself if it is the root.
   *
   * @return MdProblemIF that is the root of the hierarchy that this type belongs to.
   * returns a reference to inself if it is the root.
   */
  public MdProblemDAOIF getRootMdClassDAO();

  /**
   * Returns an array of MdProblemIF that defines immediate subclasses of this class.
   * @return an array of MdProblemIF that defines immediate subclasses of this class.
   */
  public List<MdProblemDAOIF> getSubClasses();

  /**
   * Returns a list of MdProblemIF objects that are subclasses of the given
   * entity. Only non abstract entities are returned (i.e. entities that can be
   * instantiated)
   * 
   * @return list of MdProblemIF objects that are subclasses of the given entity.
   *         Only non abstract entities are returned (i.e. entities that can be
   *         instantiated)
   */
  public List<MdProblemDAOIF> getAllConcreteSubClasses();

  /**
   *Returns a list of MdProblemIF objects that represent entites 
   * that are subclasses of the given entity, including all recursive entities.
   *
   * @return list of MdProblemIF objects that represent entites 
   * that are subclasses of the given entity, including all recursive entities.
   */
  public List<MdProblemDAOIF> getAllSubClasses();

  /**
   * Returns an MdProblemIF representing the superclass of this class, or null if
   * this class is basic.
   * 
   * @return an MdProblemIF representing the superclass of this class, or null if
   *         the class is basic.
   */
  public MdProblemDAOIF getSuperClass();

  /**
   * Returns a list of MdProblemIF instances representing every
   * parent of this MdProblemIF partaking in an inheritance relationship.
   * 
   * @return a list of MdProblemIF instances that are parents of this class.
   */
  public List<MdProblemDAOIF> getSuperClasses();
  
  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdProblemDAO getBusinessDAO();
}
