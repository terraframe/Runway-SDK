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

import java.util.Map;

import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.database.RelationshipDAOFactory;

public class MetadataRelationshipDAO extends TreeDAO implements SpecializedDAOImplementationIF
{
  /**
   *
   */
  private static final long serialVersionUID = -3325018795739737280L;

  public MetadataRelationshipDAO(String parentId, String childId, Map<String, Attribute> attributeMap, String relationshipType)
  {
    super(parentId, childId, attributeMap, relationshipType);
  }

  /**
   *
   */
  public String save(boolean save)
  {
    String id = this.getId();

    Attribute keyAttribute = this.getAttribute(ComponentInfo.KEY);

    if (!keyAttribute.isModified() && keyAttribute.getValue().equals(""))
    {
      this.setKey(id);
    }

    return super.save(save);
  }

  /**
   *
   * @param relationshipType
   * @return
   */
  public static MetadataRelationshipDAO newInstance(String parentId, String childId, String relationshipType)
  {
    return (MetadataRelationshipDAO) RelationshipDAOFactory.newInstance(parentId, childId, relationshipType);
  }
}
