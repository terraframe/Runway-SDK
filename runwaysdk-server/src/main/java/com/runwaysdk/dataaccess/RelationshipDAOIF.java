/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.dataaccess;

public interface RelationshipDAOIF extends ElementDAOIF
{
  /**
   * Attribute of relationship table.
   */
  public static final String PARENT_ID_COLUMN                          = "parent_id";

  /**
   * Attribute of relationship table.
   */
  public static final String CHILD_ID_COLUMN                            = "child_id";
  
  /**
   * Returns the id of the parent BusinessDAO in this relationship.
   *
   * <br/>
   * <b>Precondition: </b> true <br/>
   * <b>Postcondition: </b> return value != null
   *
   * @return id of the parent BusinessDAO in this relationship
   */
  public String getParentId();

  /**
   * Returns the parent BusinessDAO in this relationship.
   *
   * <br/>
   * <b>Precondition: </b> true <br/>
   * <b>Postcondition: </b> return value != null
   *
   * @return the parent BusinessDAO in this relationship
   */
  public BusinessDAOIF getParent();

  /**
   * Returns the id of the child BusinessDAO in this relationship.
   *
   * <br/>
   * <b>Precondition: </b> true <br/>
   * <b>Postcondition: </b> return value != null
   *
   * @return id of the child BusinessDAO in this relationship
   */
  public String getChildId();
  
  /**
   * Returns the child BusinessDAO in this relationship.
   *
   * <br/>
   * <b>Precondition: </b> true <br/>
   * <b>Postcondition: </b> return value != null
   *
   * @return the child BusinessDAO in this relationship
   */
  public BusinessDAOIF getChild();

  /**
   *
   * @param name
   * @return
   */
  public AttributeIF getAttributeIF(String name);

  /**
   * Returns the metadata BusinessDAO describing this relationship type.
   *
   * <br/>
   * <b>Precondition: </b> true <br/>
   * <b>Postcondition: </b> return value != null
   *
   * @return the metadata BusinessDAO describing this relationship type
   */
  public MdRelationshipDAOIF getMdRelationshipDAO();

  /**
   * Returns a deep cloned-copy of this Relationship
   */
  public RelationshipDAO getRelationshipDAO();

  /**
   * Returns a copy of the given RelationshipDAO instance, with a new id and mastered at the current site.
   * The state of the object is new and has not been applied to the database.
   *
   * @return a copy of the given RelationshipDAO instance
   */
  public RelationshipDAO copy();
}
