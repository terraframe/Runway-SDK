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

public interface MdNotificationDAOIF extends MdLocalizableDAOIF
{
  /**
   * Name of the table used to store intances of this class.
   */
  public static final String TABLE   = "md_notification";
  
  /**
   *Returns the MdNotificationIF that is the root of the hierarchy that this type belongs to.
   * returns a reference to inself if it is the root.
   *
   * @return MdNotificationIF that is the root of the hierarchy that this type belongs to.
   * returns a reference to inself if it is the root.
   */
  public MdNotificationDAOIF getRootMdClassDAO();
  
  /**
   * Returns an array of MdNotificationIF that defines immediate subclasses of this class.
   * @return an array of MdNotificationIF that defines immediate subclasses of this class.
   */
  public List<? extends MdNotificationDAOIF> getSubClasses();
  
  /**
   * Returns a list of MdNotificationIF objects that are subclasses of the given
   * entity. Only non abstract entities are returned (i.e. entities that can be
   * instantiated)
   * 
   * @return list of MdNotificationIF objects that are subclasses of the given entity.
   *         Only non abstract entities are returned (i.e. entities that can be
   *         instantiated)
   */
  public List<? extends MdNotificationDAOIF> getAllConcreteSubClasses();
 
  /**
   *Returns a list of MdNotificationIF objects that represent entites 
   * that are subclasses of the given entity, including all recursive entities.
   *
   * @return list of MdNotificationIF objects that represent entites 
   * that are subclasses of the given entity, including all recursive entities.
   */
  public List<? extends MdNotificationDAOIF> getAllSubClasses();

  /**
   * Returns an MdNotificationIF representing the superclass of this class, or null if
   * this class is basic.
   * 
   * @return an MdNotificationIF representing the superclass of this class, or null if
   *         the class is basic.
   */
  public MdNotificationDAOIF getSuperClass();

  /**
   * Returns a list of MdNotificationIF instances representing every
   * parent of this MdNotificationIF partaking in an inheritance relationship.
   * 
   * @return a list of MdNotificationIF instances that are parents of this class.
   */
  public List<? extends MdNotificationDAOIF> getSuperClasses();

}
