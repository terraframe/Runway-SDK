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
package com.runwaysdk.transport.conversion.json;

import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.RelationshipQueryDTO;

public class JSONToRelationshipQueryDTO extends JSONToElementQueryDTO
{

  public JSONToRelationshipQueryDTO(String sessionId, Locale locale, JSONObject json, RelationshipQueryDTO queryDTO)
      throws JSONException
  {
    super(sessionId, locale, json, queryDTO);
  }
  
  @Override
  protected RelationshipQueryDTO getComponentQueryDTO()
  {
    return (RelationshipQueryDTO) super.getComponentQueryDTO();
  }
  
  @Override
  public RelationshipQueryDTO populate() throws JSONException
  {
    super.populate();
    
    RelationshipQueryDTO queryDTO = getComponentQueryDTO();
//    JSONObject json = getJSON();
//    
//    queryDTO.setParentMdBusiness(json.getString(JSON.MD_RELATIONSHIP_QUERY_DTO_PARENT_MDBUSINESS.getLabel()));
//    queryDTO.setChildMdBusiness(json.getString(JSON.MD_RELATIONSHIP_QUERY_DTO_CHILD_MDBUSINESS.getLabel()));
    
    return queryDTO;
  }

}
