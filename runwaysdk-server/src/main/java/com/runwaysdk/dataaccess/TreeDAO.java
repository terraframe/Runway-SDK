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

import java.util.Map;

import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.database.RelationshipDAOFactory;


public class TreeDAO extends GraphDAO implements TreeDAOIF
{

  /**
   *
   */
  private static final long serialVersionUID = -6080868069787166451L;

  /**
   *
   * @param parentId
   * @param childId
   * @param attributeMap
   * @param relationshipType
   */
  public TreeDAO(String parentId, String childId, Map<String, Attribute> attributeMap, String relationshipType)
  {
    super(parentId, childId, attributeMap, relationshipType);
  }

  /**
   * Checks the Relationship for several validity constraints: the classes of the parent and
   * child, as well as the cardinality of the parent and child for this relationship.
   */
  protected void validate()
  {
    super.validate();
    RelationshipDAOFactory.recursiveLinkCheck(this.getParentId(), this.getChildId(), this.getMdRelationshipDAO().definesType());
  }

  /**
   *
   * @param relationshipType
   * @return
   */
  public static TreeDAO newInstance(String parentId, String childId, String relationshipType)
  {
    return (TreeDAO) RelationshipDAOFactory.newInstance(parentId, childId, relationshipType);
  }
}
