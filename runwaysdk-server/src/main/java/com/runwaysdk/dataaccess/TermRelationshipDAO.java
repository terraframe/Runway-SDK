/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.runwaysdk.dataaccess;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.constants.AssociationType;
import com.runwaysdk.constants.MdTermRelationshipInfo;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.database.RelationshipDAOFactory;

public class TermRelationshipDAO extends RelationshipDAO implements TermRelationshipDAOIF, Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 9424564709853L;

  public TermRelationshipDAO(String parentId, String childId, Map<String, Attribute> attributeMap, String relationshipType)
  {
    super(parentId, childId, attributeMap, relationshipType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.RelationshipDAO#getMdRelationshipDAO()
   */
  @Override
  public MdTermRelationshipDAOIF getMdRelationshipDAO()
  {
    return (MdTermRelationshipDAOIF) super.getMdRelationshipDAO();
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
      MdTermRelationshipDAOIF mdRelationship = this.getMdRelationshipDAO();
      AttributeEnumerationIF associationType = (AttributeEnumerationIF) mdRelationship.getAttributeIF(MdTermRelationshipInfo.ASSOCIATION_TYPE);
      Set<String> items = associationType.getEnumItemIdList();

      if (items.contains(AssociationType.GRAPH.getId()))
      {
        GraphDAO.validateGraph(this);
      }
      else if (items.contains(AssociationType.TREE.getId()))
      {
        RelationshipDAOFactory.recursiveLinkCheck(this.getParentId(), this.getChildId(), this.getMdRelationshipDAO().definesType());
      }
    }

    super.validate();
  }
}
