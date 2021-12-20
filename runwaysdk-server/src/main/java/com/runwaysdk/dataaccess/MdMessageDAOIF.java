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
package com.runwaysdk.dataaccess;

import java.util.List;

public interface MdMessageDAOIF extends MdNotificationDAOIF
{
  /**
   * Name of the table used to store intances of this class.
   */
  public static final String TABLE   = "md_message";
  
  /**
   *Returns the MdMessageIF that is the root of the hierarchy that this type belongs to.
   * returns a reference to inself if it is the root.
   *
   * @return MdMessageIF that is the root of the hierarchy that this type belongs to.
   * returns a reference to inself if it is the root.
   */
  public MdMessageDAOIF getRootMdClassDAO();

  /**
   * Returns an array of MdMessageIF that defines immediate subclasses of this class.
   * @return an array of MdMessageIF that defines immediate subclasses of this class.
   */
  public List<? extends MdMessageDAOIF> getSubClasses();

  /**
   * Returns a list of MdMessageIF objects that are subclasses of the given
   * entity. Only non abstract entities are returned (i.e. entities that can be
   * instantiated)
   * 
   * @return list of MdMessageIF objects that are subclasses of the given entity.
   *         Only non abstract entities are returned (i.e. entities that can be
   *         instantiated)
   */
  public List<? extends MdMessageDAOIF> getAllConcreteSubClasses();
  
  /**
   *Returns a list of MdMessageIF objects that represent entites 
   * that are subclasses of the given entity, including all recursive entities.
   *
   * @return list of MdMessageIF objects that represent entites 
   * that are subclasses of the given entity, including all recursive entities.
   */
  public List<? extends MdMessageDAOIF> getAllSubClasses();
  
  /**
   * Returns an MdMessageIF representing the superclass of this class, or null if
   * this class is basic.
   * 
   * @return an MdMessageIF representing the superclass of this class, or null if
   *         the class is basic.
   */
  public MdMessageDAOIF getSuperClass();

  /**
   * Returns a list of MdMessageIF instances representing every
   * parent of this MdMessageIF partaking in an inheritance relationship.
   * 
   * @return a list of MdMessageIF instances that are parents of this class.
   */
  public List<? extends MdMessageDAOIF> getSuperClasses();
  
}
