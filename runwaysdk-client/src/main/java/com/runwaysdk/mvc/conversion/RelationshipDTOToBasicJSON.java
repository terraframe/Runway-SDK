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
package com.runwaysdk.mvc.conversion;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.RelationshipDTO;
import com.runwaysdk.constants.JSON;

public class RelationshipDTOToBasicJSON extends ElementDTOToBasicJSON
{
  /**
   *
   * @param relationshipDTO
   */
  protected RelationshipDTOToBasicJSON(RelationshipDTO relationshipDTO)
  {
    super(relationshipDTO);
  }

  /**
   * Returns the source ComponentDTO downcasted as a RelationshipDTO.
   */
  @Override
  protected RelationshipDTO getComponentDTO()
  {
    return (RelationshipDTO) super.getComponentDTO();
  }

  /**
   * Sets the Relationship properties
   */
  protected void setProperties() throws JSONException
  {
    super.setProperties();

    JSONObject json = getJSON();
    RelationshipDTO relationshipDTO = getComponentDTO();

    // parent
    String parentId = relationshipDTO.getParentId();
    json.put(JSON.RELATIONSHIP_DTO_PARENT_ID.getLabel(), parentId);

    // child
    String childId = relationshipDTO.getChildId();
    json.put(JSON.RELATIONSHIP_DTO_CHILD_ID.getLabel(), childId);
  }
}
