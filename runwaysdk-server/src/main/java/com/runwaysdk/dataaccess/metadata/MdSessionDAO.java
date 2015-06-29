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

import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdSessionDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;

public abstract class MdSessionDAO extends MdTransientDAO implements MdSessionDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 5414432788530351004L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdSessionDAO()
  {
    super();
  }

  /**
   * Constructs a MdSession from the given hashtable of Attributes.
   *
   * <br/><b>Precondition:</b> attributeMap != null
   * <br/><b>Precondition:</b> type != null <br/>
   *
   *
   * @param attributeMap
   * @param type
   * @param useCache
   */
  public MdSessionDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /**
   *Returns a list of MdSessionIF objects that represent classes
   * that are subclasses of the given class, including this class,
   * including all recursive entities.
   *
   * @return list of MdSessionIF objects that represent classes
   * that are subclasses of the given class, including this class,
   * including all recursive entities.
   */
  @SuppressWarnings("unchecked")
  public List<? extends MdSessionDAOIF> getAllSubClasses()
  {
    return (List<? extends MdSessionDAOIF>)super.getAllSubClasses();
  }

  /**
   * Returns an array of MdSessionIF that defines immediate subclasses of this class.
   * @return an array of MdSessionIF that defines immediate subclasses of this class.
   */
  public abstract List<? extends MdSessionDAOIF> getSubClasses();

  /**
   * Returns a list of MdSessionIF objects that are subclasses of the given
   * entity. Only non abstract entities are returned (i.e. entities that can be
   * instantiated)
   *
   * @return list of MdSessionIF objects that are subclasses of the given entity.
   *         Only non abstract entities are returned (i.e. entities that can be
   *         instantiated)
   */
  @SuppressWarnings("unchecked")
  public List<? extends MdSessionDAOIF> getAllConcreteSubClasses()
  {
    return (List<? extends MdSessionDAOIF>)super.getAllConcreteSubClasses();
  }

  /**
   * Returns a list of MdSessionIF instances representing every
   * parent of this MdSessionIF partaking in an inheritance relationship.
   *
   * @return a list of MdSessionIF instances that are parents of this class.
   */
  public abstract MdSessionDAOIF getSuperClass();

  /**
   * Returns a list of MdSessionIF instances representing every
   * parent of this MdSessionIF partaking in an inheritance relationship.
   *
   * @return a list of MdSessionIF instances that are parents of this class.
   */
  @SuppressWarnings("unchecked")
  public List<? extends MdSessionDAOIF> getSuperClasses()
  {
    return (List<MdSessionDAOIF>) super.getSuperClasses();
  }

  /**
   *Returns the MdSessionIF that is the root of the hierarchy that this type belongs to.
   * returns a reference to inself if it is the root.
   *
   * @return MdSessionIF that is the root of the hierarchy that this type belongs to.
   * returns a reference to inself if it is the root.
   */
  public MdSessionDAOIF getRootMdClassDAO()
  {
    return (MdSessionDAOIF)super.getRootMdClassDAO();
  }

  /*
   * (non-Javadoc)
   *
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static MdSessionDAOIF get(String id)
  {
    return (MdSessionDAOIF) BusinessDAO.get(id);
  }

  /*
   * (non-Javadoc)
   *
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdSessionDAO getBusinessDAO()
  {
    return (MdSessionDAO)super.getBusinessDAO();
  }

}
