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

import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;

public interface MdEdgeDAOIF extends MdGraphClassDAOIF, RelationshipMetadata
{
  /**
   * Name of the table used to store instances of this class.
   */
  public static final String TABLE = "md_edge";

  /**
   * Returns the {@link MdEdgeDAOIF} that is the root of the hierarchy that this
   * type belongs to. returns a reference to itself if it is the root.
   *
   * @return {@link MdEdgeDAOIF} that is the root of the hierarchy that this
   *         type belongs to. returns a reference to itself if it is the root.
   */
  public abstract MdEdgeDAOIF getRootMdClassDAO();

  /**
   * Returns an array of {@link MdEdgeDAOIF} that defines immediate subclasses
   * of this class.
   * 
   * @return an array of {@link MdEdgeDAOIF} that defines immediate subclasses
   *         of this class.
   */
  public List<? extends MdEdgeDAOIF> getSubClasses();

  /**
   * Returns a list of {@link MdEdgeDAOIF} objects that are subclasses of the
   * given entity. Only non abstract entities are returned (i.e. entities that
   * can be instantiated)
   * 
   * @return list of {@link MdEdgeDAOIF} objects that are subclasses of the
   *         given entity. Only non abstract entities are returned (i.e.
   *         entities that can be instantiated)
   */
  public List<? extends MdEdgeDAOIF> getAllConcreteSubClasses();

  /**
   * Returns a list of {@link MdEdgeDAOIF} objects that represent entities that
   * are subclasses of the given entity, including all recursive entities.
   *
   * @return list of {@link MdEdgeDAOIF} objects that represent entities that
   *         are subclasses of the given entity, including all recursive
   *         entities.
   */
  public List<? extends MdEdgeDAOIF> getAllSubClasses();

  /**
   * Returns an {@link MdEdgeDAOIF} representing the super class of this class,
   * or null if it does not have one.
   * 
   * @return an {@link MdEdgeDAOIF} representing the super class of this class,
   *         or null if it does not have one.
   */
  public MdEdgeDAOIF getSuperClass();

  /**
   * Returns a list of {@link MdEdgeDAOIF} instances representing every parent
   * of this {@link MdEdgeDAOIF} partaking in an inheritance relationship.
   * 
   * @return a list of {@link MdEdgeDAOIF} instances that are parents of this
   *         class.
   */
  public List<? extends MdEdgeDAOIF> getSuperClasses();

  /**
   * Returns the {@link MdAttributeDAOIF} that defines the given attribute for
   * the this entity. This method only works if the attribute is explicitly
   * defined by the this. In other words, it will return null if the attribute
   * exits for the given entity, but is inherited from a super entity.
   * 
   * <br/>
   * <b>Precondition:</b> attributeName != null <br/>
   * <b>Precondition:</b> !attributeName.trim().equals("")
   */
  public MdAttributeDAOIF definesAttribute(String attributeName);

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdEdgeDAO getBusinessDAO();

  public MdVertexDAOIF getParentMdVertex();

  public MdVertexDAOIF getChildMdVertex();
}
