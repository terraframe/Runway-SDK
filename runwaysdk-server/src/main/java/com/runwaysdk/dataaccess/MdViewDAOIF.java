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

import com.runwaysdk.dataaccess.metadata.MdViewDAO;

public interface MdViewDAOIF extends MdSessionDAOIF
{
  /**
   * Name of the table used to store intances of this class.
   */
  public static final String TABLE   = "md_view";

  /**
   * Column name Query Base Class source code.
   */
  public static final String QUERY_BASE_SOURCE_COLUMN      = "query_base_source";
  
  /**
   * Column name Query Base Class bytecode.
   */
  public static final String QUERY_BASE_CLASS_COLUMN       = "query_base_class";

  /**
   * Column name Query Stub Class source code.
   */
  public static final String QUERY_STUB_SOURCE_COLUMN      = "query_stub_source";
  
  /**
   * Column name Query Stub Class bytecode.
   */
  public static final String QUERY_STUB_CLASS_COLUMN       = "query_stub_class";
  
  /**
   * Column name Query DTO Class source code.
   */
  public static final String QUERY_DTO_SOURCE_COLUMN      = "query_dto_source";
  
  /**
   * Column name Query DTO Class bytecode.
   */
  public static final String QUERY_DTO_CLASS_COLUMN       = "query_dto_class";
  
  /**
   *Returns the MdViewIF that is the root of the hierarchy that this type belongs to.
   * returns a reference to inself if it is the root.
   *
   * @return MdViewIF that is the root of the hierarchy that this type belongs to.
   * returns a reference to inself if it is the root.
   */
  public MdViewDAOIF getRootMdClassDAO();

  /**
   * Returns an array of MdViewIF that defines immediate subclasses of this class.
   * @return an array of MdViewIF that defines immediate subclasses of this class.
   */
  public List<MdViewDAOIF> getSubClasses();
  
  /**
   * Returns a list of MdViewIF objects that are subclasses of the given
   * entity. Only non abstract entities are returned (i.e. entities that can be
   * instantiated)
   * 
   * @return list of MdViewIF objects that are subclasses of the given entity.
   *         Only non abstract entities are returned (i.e. entities that can be
   *         instantiated)
   */
  public List<? extends MdViewDAOIF> getAllConcreteSubClasses();  
  
  /**
   *Returns a list of MdViewIF objects that represent entites 
   * that are subclasses of the given entity, including all recursive entities.
   *
   * @return list of MdViewIF objects that represent entites 
   * that are subclasses of the given entity, including all recursive entities.
   */
  public List<? extends MdViewDAOIF> getAllSubClasses();

  /**
   * Returns an MdViewIF representing the superclass of this class, or null if
   * this class is basic.
   * 
   * @return an MdViewIF representing the superclass of this class, or null if
   *         the class is basic.
   */
  public MdViewDAOIF getSuperClass();

  /**
   * Returns a list of MdViewIF instances representing every
   * parent of this MdViewIF partaking in an inheritance relationship.
   * 
   * @return a list of MdViewIF instances that are parents of this class.
   */
  public List<MdViewDAOIF> getSuperClasses();
  
  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdViewDAO getBusinessDAO();
  
}
