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
/**
 * Created on Aug 5, 2005
 *
 */
package com.runwaysdk.dataaccess;

import java.io.Serializable;
import java.util.List;


/**
 * @author nathan
 *
 */
public interface BusinessDAOIF extends ElementDAOIF, Serializable
{
  /**
   *
   * @return
   */
  public BusinessDAO getBusinessDAO();

  /**
   *
   * @param name
   * @return
   */
  public AttributeIF getAttributeIF(String name);

  /**
   *
   * @param relationshipType
   * @return
   */
  public List<RelationshipDAOIF> getChildren(String relationshipType);

  /**
   *
   * @return
   */
  public List<RelationshipDAOIF> getAllChildren();

  /**
   *
   * @param relationshipType
   * @return
   */
  public List<RelationshipDAOIF> getParents(String relationshipType);

  /**
   *
   * @return
   */
  public List<RelationshipDAOIF> getAllParents();

  /**
   *
   * @return
   */
  public MdBusinessDAOIF getMdBusinessDAO();

  /**
   * @param structAttributeName
   * @param attributeName
   * @return the value of a struct attribute
   */
  public String getStructValue(String structAttributeName, String attributeName);

  /**
   * Returns a copy of the given BusinessDAO instance, with a new oid and mastered at the current site.
   * The state of the object is new and has not been applied to the database.
   *
   * @return a copy of the given BusinessDAO instance
   */
  public BusinessDAO copy();

}
