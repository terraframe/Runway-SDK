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
package com.runwaysdk.dataaccess.metadata;

import java.util.List;
import java.util.Map;

import com.runwaysdk.constants.MdMessageInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdMessageDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.cache.ObjectCache;

public abstract class MdMessageDAO extends MdNotificationDAO implements MdMessageDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = 1657588130472466544L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdMessageDAO()
  {
    super();
  }

  /**
   * Constructs a MdMessage from the given hashtable of Attributes.
   * 
   * <br/><b>Precondition:</b> attributeMap != null 
   * <br/><b>Precondition:</b> type != null <br/>
   * 
   * 
   * @param attributeMap
   * @param type
   * @param useCache
   */
  public MdMessageDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /**
   * Returns an MdMessageIF instance of the metadata for the given type. 
   * 
   * <br/><b>Precondition:</b>  messageType != null
   * <br/><b>Precondition:</b>  !messageType.trim().equals("")
   * <br/><b>Precondition:</b>  messageType is a valid class defined in the database
   * <br/><b>Postcondition:</b> Returns a MdMessageIF instance of the metadata for the 
   *                            given class
   *                            (MdMessageIF().definesType().equals(messageType)
   * 
   * @param  messageType 
   * @return MdProblemIF instance of the metadata for the given type.
   */
  public static MdMessageDAOIF getMdMessage(String messageType)
  {
    return ObjectCache.getMdMessageDAO(messageType);
  }
  
  /**
   * Returns a new MdProblem. 
   * Some attributes will contain default values, as defined in the attribute
   * metadata. Otherwise, the attributes will be blank.
   * 
   * @return instance of MdView.
   */
  public static MdMessageDAO newInstance()
  {
    return (MdMessageDAO) BusinessDAO.newInstance(MdMessageInfo.CLASS);
  }

  /**
   * Returns true if this class is the root class of a hierarchy, false otherwise.
   * @return true if this class is the root class of a hierarchy, false otherwise.
   */
  public abstract boolean isRootOfHierarchy();

  /**
   *Returns the MdMessageIF that is the root of the hierarchy that this type belongs to.
   * returns a reference to inself if it is the root.
   *
   * @return MdMessageIF that is the root of the hierarchy that this type belongs to.
   * returns a reference to inself if it is the root.
   */
  public MdMessageDAOIF getRootMdClassDAO()
  {
    return (MdMessageDAOIF)super.getRootMdClassDAO();
  }

  /**
   * Returns an array of MdMessageIF that defines immediate subclasses of this class.
   * @return an array of MdMessageIF that defines immediate subclasses of this class.
   */
  public abstract List<? extends MdMessageDAOIF> getSubClasses();
  
  /**
   * Returns a list of MdMessageIF objects that are subclasses of the given
   * entity. Only non abstract entities are returned (i.e. entities that can be
   * instantiated)
   * 
   * @return list of MdMessageIF objects that are subclasses of the given entity.
   *         Only non abstract entities are returned (i.e. entities that can be
   *         instantiated)
   */
  @SuppressWarnings("unchecked")
  public List<? extends MdMessageDAOIF> getAllConcreteSubClasses()
  {
    return (List<MdMessageDAOIF>)super.getAllConcreteSubClasses();
  }

  /**
   *Returns a list of MdMessageIF objects that represent entites 
   * that are subclasses of the given entity, including all recursive entities.
   *
   * @return list of MdMessageIF objects that represent entites 
   * that are subclasses of the given entity, including all recursive entities.
   */
  @SuppressWarnings("unchecked")
  public List<? extends MdMessageDAOIF> getAllSubClasses()
  {
    return (List<MdMessageDAOIF>)super.getAllSubClasses();
  }

  /**
   * Returns a list of MdMessageIF instances representing every
   * parent of this MdMessageIF partaking in an inheritance relationship.
   * 
   * @return a list of MdMessageIF instances that are parents of this class.
   */
  @SuppressWarnings("unchecked")
  public List<? extends MdMessageDAOIF> getSuperClasses()
  {
    return (List<MdMessageDAOIF>) super.getSuperClasses();
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdMessageDAO getBusinessDAO()
  {
    return (MdMessageDAO) super.getBusinessDAO();
  }

  /**
   * Returns an MdMessageIF instance of the metadata for the given type. 
   * 
   * <br/><b>Precondition:</b>  warningType != null
   * <br/><b>Precondition:</b>  !warningType.trim().equals("")
   * <br/><b>Precondition:</b>  warningType is a valid class defined in the database
   * <br/><b>Postcondition:</b> Returns a MdMessageIF instance of the metadata for the 
   *                            given class
   *                            (MdMessageIF().definesType().equals(warningType)
   * 
   * @param  problemType 
   * @return MdProblemIF instance of the metadata for the given type.
   */
  public static MdMessageDAOIF getMdBusiness(String warningType)
  {
    return ObjectCache.getMdMessageDAO(warningType);
  }

  /**
   * Returns an MdMessageIF representing the superclass of this class, or null if
   * this class is basic.
   * 
   * @return an MdMessageIF representing the superclass of this class, or null if
   *         the class is basic.
   */
  public abstract MdMessageDAOIF getSuperClass();

  public String toString()
  {
    return '[' + this.definesType() + " definition]";
  }
  
}
