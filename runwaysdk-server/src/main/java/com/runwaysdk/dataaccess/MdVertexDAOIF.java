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

import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;

public interface MdVertexDAOIF extends MdGraphClassDAOIF
{
  /**
   * Name of the table used to store instances of this class.
   */
  public static final String TABLE = "md_vertex";

  /**
   * Returns the {@link MdVertexDAOIF} that is the root of the hierarchy that
   * this type belongs to. returns a reference to itself if it is the root.
   *
   * @return {@link MdVertexDAOIF} that is the root of the hierarchy that this
   *         type belongs to. returns a reference to itself if it is the root.
   */
  public abstract MdVertexDAOIF getRootMdClassDAO();

  /**
   * Returns an array of {@link MdVertexDAOIF} that defines immediate subclasses
   * of this class.
   * 
   * @return an array of {@link MdVertexDAOIF} that defines immediate subclasses
   *         of this class.
   */
  public List<? extends MdVertexDAOIF> getSubClasses();

  /**
   * Returns a list of {@link MdVertexDAOIF} objects that are subclasses of the
   * given entity. Only non abstract entities are returned (i.e. entities that
   * can be instantiated)
   * 
   * @return list of {@link MdVertexDAOIF} objects that are subclasses of the
   *         given entity. Only non abstract entities are returned (i.e.
   *         entities that can be instantiated)
   */
  public List<? extends MdVertexDAOIF> getAllConcreteSubClasses();

  /**
   * Returns a list of {@link MdVertexDAOIF} objects that represent entities
   * that are subclasses of the given entity, including all recursive entities.
   *
   * @return list of {@link MdVertexDAOIF} objects that represent entities that
   *         are subclasses of the given entity, including all recursive
   *         entities.
   */
  public List<? extends MdVertexDAOIF> getAllSubClasses();

  /**
   * Returns an {@link MdVertexDAOIF} representing the super class of this
   * class, or null if it does not have one.
   * 
   * @return an {@link MdVertexDAOIF} representing the super class of this
   *         class, or null if it does not have one.
   */
  public MdVertexDAOIF getSuperClass();

  /**
   * Returns a list of {@link MdVertexDAOIF} instances representing every parent
   * of this {@link MdVertexDAOIF} partaking in an inheritance relationship.
   * 
   * @return a list of {@link MdVertexDAOIF} instances that are parents of this
   *         class.
   */
  public List<? extends MdVertexDAOIF> getSuperClasses();

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

  /**
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdVertexDAO getBusinessDAO();

  public List<MdEdgeDAOIF> getChildMdEdges();

  public List<MdEdgeDAOIF> getParentMdEdges();
}
