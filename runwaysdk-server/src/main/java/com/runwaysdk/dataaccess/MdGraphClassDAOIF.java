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

import com.runwaysdk.dataaccess.metadata.graph.MdGraphClassDAO;

public interface MdGraphClassDAOIF extends MdClassDAOIF
{
  /**
   * Name of the table used to store instances of this class.
   */
  public static final String TABLE   = "md_graph_class";
  
  /**
   * Returns the {@link MdGraphClassDAOIF} that is the root of the hierarchy that this type belongs to.
   * returns a reference to itself if it is the root.
   *
   * @return {@link MdGraphClassDAOIF} that is the root of the hierarchy that this type belongs to.
   * returns a reference to itself if it is the root.
   */
  public abstract MdGraphClassDAOIF getRootMdClassDAO();
  
  /**
   * Returns an array of {@link MdGraphClassDAOIF} that defines immediate subclasses of this class.
   * @return an array of {@link MdGraphClassDAOIF} that defines immediate subclasses of this class.
   */
  public List<? extends MdGraphClassDAOIF> getSubClasses();
  
  /**
   * Returns a list of {@link MdGraphClassDAOIF} objects that are subclasses of the given
   * entity. Only non abstract entities are returned (i.e. entities that can be
   * instantiated)
   * 
   * @return list of {@link MdGraphClassDAOIF} objects that are subclasses of the given entity.
   *         Only non abstract entities are returned (i.e. entities that can be
   *         instantiated)
   */
  public List<? extends MdGraphClassDAOIF> getAllConcreteSubClasses();

  /**
   * Returns a list of {@link MdGraphClassDAOIF} objects that represent entities 
   * that are subclasses of the given entity, including all recursive entities.
   *
   * @return list of {@link MdGraphClassDAOIF} objects that represent entities 
   * that are subclasses of the given entity, including all recursive entities.
   */
  public List<? extends MdGraphClassDAOIF> getAllSubClasses();
  
  /**
   * Returns an {@link MdGraphClassDAOIF} representing the super class of this class, or null if
   * it does not have one.
   * 
   * @return an {@link MdGraphClassDAOIF} representing the super class of this class, or null if
   * it does not have one.
   */
  public MdGraphClassDAOIF getSuperClass();
  
  /**
   * Returns a list of {@link MdGraphClassDAOIF} instances representing every
   * parent of this {@link MdGraphClassDAOIF} partaking in an inheritance relationship.
   * 
   * @return a list of {@link MdGraphClassDAOIF} instances that are parents of this class.
   */
  public List<? extends MdGraphClassDAOIF> getSuperClasses();
  
  /**
   * Returns a sorted list of <code>MdAttributeConcreteDAOIF</code> objects that this <code>MdEntityDAOIF</code> defines.
   *
   * @return an List of <code>MdAttributeConcreteDAOIF</code> objects that this <code>MdEntityDAOIF</code> defines.
   */
  public List<? extends MdAttributeConcreteDAOIF> definesAttributes();
  
  /**
   *Returns the {@link MdAttributeDAOIF} that defines the given attribute for the this entity.  This method
   * only works if the attribute is explicitly defined by the this.  In other words, it
   * will return null if the attribute exits for the given entity, but is inherited from a 
   * super entity.
   * 
   * <br/><b>Precondition:</b>   attributeName != null
   * <br/><b>Precondition:</b>   !attributeName.trim().equals("")
   */
  public MdAttributeDAOIF definesAttribute(String attributeName);
  
  /**
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdGraphClassDAO getBusinessDAO();
}
