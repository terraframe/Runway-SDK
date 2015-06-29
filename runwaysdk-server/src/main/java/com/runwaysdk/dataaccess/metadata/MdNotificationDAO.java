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
package com.runwaysdk.dataaccess.metadata;

import java.util.List;
import java.util.Map;

import com.runwaysdk.dataaccess.MdNotificationDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;


public abstract class MdNotificationDAO extends MdLocalizableDAO implements MdNotificationDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 1676391987378181508L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdNotificationDAO()
  {
    super();
  }
  
  /**
   * Constructs a MdNotification from the given hashtable of Attributes.
   * 
   * <br/><b>Precondition:</b> attributeMap != null 
   * <br/><b>Precondition:</b> type != null <br/>
   * 
   * 
   * @param attributeMap
   * @param type
   * @param useCache
   */
  public MdNotificationDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }
  
  /**
   *Returns the MdNotificationIF that is the root of the hierarchy that this type belongs to.
   * returns a reference to inself if it is the root.
   *
   * @return MdNotificationIF that is the root of the hierarchy that this type belongs to.
   * returns a reference to inself if it is the root.
   */
  public MdNotificationDAOIF getRootMdClassDAO()
  {
    return (MdNotificationDAOIF)super.getRootMdClassDAO();
  }
  
  /**
   * Returns an array of MdNotificationIF that defines immediate subclasses of this class.
   * @return an array of MdNotificationIF that defines immediate subclasses of this class.
   */
  public abstract List<? extends MdNotificationDAOIF> getSubClasses();

  /**
   * Returns a list of MdNotificationIF objects that are subclasses of the given
   * entity. Only non abstract entities are returned (i.e. entities that can be
   * instantiated)
   * 
   * @return list of MdNotificationIF objects that are subclasses of the given entity.
   *         Only non abstract entities are returned (i.e. entities that can be
   *         instantiated)
   */
  @SuppressWarnings("unchecked")
  public List<? extends MdNotificationDAOIF> getAllConcreteSubClasses()
  {
    return (List<? extends MdNotificationDAOIF>)super.getAllConcreteSubClasses();
  }

  /**
   *Returns a list of MdClassIF objects that represent classes 
   * that are subclasses of the given class, including this class, 
   * including all recursive entities.
   *
   * @return list of MdClassIF objects that represent classes 
   * that are subclasses of the given class, including this class, 
   * including all recursive entities.
   */
  @SuppressWarnings("unchecked")
  public List<? extends MdNotificationDAOIF> getAllSubClasses()
  {
    return (List<? extends MdNotificationDAOIF>)super.getAllSubClasses();
  }
  
  /**
   * Returns an MdNotificationIF representing the superclass of this class, or null if
   * this class is basic.
   * 
   * @return an MdNotificationIF representing the superclass of this class, or null if
   *         the class is basic.
   */
  public abstract MdNotificationDAOIF getSuperClass();

  /**
   * Returns a list of MdNotificationIF instances representing every
   * parent of this MdNotificationIF partaking in an inheritance relationship.
   * 
   * @return a list of MdNotificationIF instances that are parents of this class.
   */
  @SuppressWarnings("unchecked")
  public List<? extends MdNotificationDAOIF> getSuperClasses()
  {
    return (List<? extends MdNotificationDAOIF>) super.getSuperClasses();
  }
  
}
