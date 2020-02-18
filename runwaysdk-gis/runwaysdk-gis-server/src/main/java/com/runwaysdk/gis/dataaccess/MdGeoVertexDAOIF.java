/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.gis.dataaccess;

import java.util.List;

import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.gis.dataaccess.metadata.graph.MdGeoVertexDAO;

public interface MdGeoVertexDAOIF extends MdVertexDAOIF
{
  /**
   * Name of the table used to store instances of this class.
   */
  public static final String TABLE = "md_vertex";

  /**
   * Returns the {@link MdGeoVertexDAOIF} that is the root of the hierarchy that
   * this type belongs to. returns a reference to itself if it is the root.
   *
   * @return {@link MdGeoVertexDAOIF} that is the root of the hierarchy that this
   *         type belongs to. returns a reference to itself if it is the root.
   */
  public abstract MdGeoVertexDAOIF getRootMdClassDAO();

  /**
   * Returns an array of {@link MdGeoVertexDAOIF} that defines immediate subclasses
   * of this class.
   * 
   * @return an array of {@link MdGeoVertexDAOIF} that defines immediate subclasses
   *         of this class.
   */
  public List<? extends MdGeoVertexDAOIF> getSubClasses();

  /**
   * Returns a list of {@link MdGeoVertexDAOIF} objects that are subclasses of the
   * given entity. Only non abstract entities are returned (i.e. entities that
   * can be instantiated)
   * 
   * @return list of {@link MdGeoVertexDAOIF} objects that are subclasses of the
   *         given entity. Only non abstract entities are returned (i.e.
   *         entities that can be instantiated)
   */
  public List<? extends MdGeoVertexDAOIF> getAllConcreteSubClasses();

  /**
   * Returns a list of {@link MdGeoVertexDAOIF} objects that represent entities
   * that are subclasses of the given entity, including all recursive entities.
   *
   * @return list of {@link MdGeoVertexDAOIF} objects that represent entities that
   *         are subclasses of the given entity, including all recursive
   *         entities.
   */
  public List<? extends MdGeoVertexDAOIF> getAllSubClasses();

  /**
   * Returns an {@link MdGeoVertexDAOIF} representing the super class of this
   * class, or null if it does not have one.
   * 
   * @return an {@link MdGeoVertexDAOIF} representing the super class of this
   *         class, or null if it does not have one.
   */
  public MdGeoVertexDAOIF getSuperClass();

  /**
   * Returns a list of {@link MdGeoVertexDAOIF} instances representing every parent
   * of this {@link MdGeoVertexDAOIF} partaking in an inheritance relationship.
   * 
   * @return a list of {@link MdGeoVertexDAOIF} instances that are parents of this
   *         class.
   */
  public List<? extends MdGeoVertexDAOIF> getSuperClasses();
  
  /**
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdGeoVertexDAO getBusinessDAO();
}
