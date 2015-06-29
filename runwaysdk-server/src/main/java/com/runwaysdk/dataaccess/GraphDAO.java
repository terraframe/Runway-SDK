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
package com.runwaysdk.dataaccess;

import java.util.List;
import java.util.Map;

import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.RelationshipDAOFactory;

public class GraphDAO extends RelationshipDAO implements GraphDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = 9424564709853L;

  public GraphDAO(String parentId, String childId, Map<String, Attribute> attributeMap, String relationshipType)
  {
    super(parentId, childId, attributeMap, relationshipType);
  }

  /**
   * Checks the Relationship for several validity constraints: the classes of
   * the parent and child, as well as the cardinality of the parent and child
   * for this relationship.
   */
  protected void validate()
  {
    if (this.isNew())
    {
      if (Database.manuallyCheckForDuplicates())
      {
        GraphDAO.validateGraph(this);
      }
    }
    super.validate();
  }

  /**
   * 
   */
  public static void validateGraph(RelationshipDAO relationship)
  {
    List<RelationshipDAOIF> relationshipIFList = RelationshipDAOFactory.get(relationship.getParentId(), relationship.getChildId(), relationship.getType());

    if (relationshipIFList.size() >= 1)
    {
      MdRelationshipDAOIF mdRelationshipIF = relationship.getMdRelationshipDAO();

      String error = "A relationship of type [" + mdRelationshipIF.definesType() + "] already exists between parent object with id [" + relationship.getParentId() + "] and child object with id [" + relationship.getChildId() + "]";

      DuplicateGraphPathException e = new DuplicateGraphPathException(error);
      e.init(mdRelationshipIF, relationship.getParentId(), relationship.getChildId());
      throw e;
    }
  }
}
