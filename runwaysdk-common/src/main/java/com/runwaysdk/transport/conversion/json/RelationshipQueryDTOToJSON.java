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
package com.runwaysdk.transport.conversion.json;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.RelationshipQueryDTO;
import com.runwaysdk.constants.JSON;
import com.runwaysdk.constants.RelationshipQueryDTOInfo;


public class RelationshipQueryDTOToJSON extends ElementQueryDTOToJSON
{

  protected RelationshipQueryDTOToJSON(RelationshipQueryDTO queryDTO, boolean typeSafe)
  {
    super(queryDTO, typeSafe);
  }
  
  @Override
  protected RelationshipQueryDTO getComponentQueryDTO()
  {
    return (RelationshipQueryDTO) super.getComponentQueryDTO();
  }
  
  @Override
  protected void setProperties() throws JSONException
  {
    super.setProperties();
    
    JSONObject json = getJSON();
    RelationshipQueryDTO queryDTO = getComponentQueryDTO();
    
    json.put(JSON.RELATIONSHIP_QUERY_DTO_PARENT_MDBUSINESS.getLabel(), queryDTO.getParentMdBusiness());
    json.put(JSON.RELATIONSHIP_QUERY_DTO_CHILD_MDBUSINESS.getLabel(), queryDTO.getChildMdBusiness());
  }

  @Override
  protected String getDTOType()
  {
    return RelationshipQueryDTOInfo.CLASS;
  }

}
