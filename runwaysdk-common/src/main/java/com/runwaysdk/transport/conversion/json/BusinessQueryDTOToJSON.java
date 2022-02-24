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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.BusinessQueryDTO;
import com.runwaysdk.business.BusinessQueryDTO.TypeInMdRelationshipAsChild;
import com.runwaysdk.business.BusinessQueryDTO.TypeInMdRelationshipAsParent;
import com.runwaysdk.constants.BusinessQueryDTOInfo;
import com.runwaysdk.constants.JSON;

public class BusinessQueryDTOToJSON extends ElementQueryDTOToJSON
{

  protected BusinessQueryDTOToJSON(BusinessQueryDTO queryDTO, boolean typeSafe)
  {
    super(queryDTO, typeSafe);
  }
  
  @Override
  protected BusinessQueryDTO getComponentQueryDTO()
  {
    return (BusinessQueryDTO) super.getComponentQueryDTO();
  }
  
  @Override
  protected void setProperties() throws JSONException
  {
    super.setProperties();
    
    JSONObject json = getJSON();
    BusinessQueryDTO queryDTO = this.getComponentQueryDTO();
    
    /*
     * The following information is solely for use with the admin page. It doesn't need to 
     * serialized on the return trip from JSON to an BusinessQueryDTO.
     */
    
    // get MdRelationship information for which the query type is a parent
    JSONArray asParents = new JSONArray();
    for(TypeInMdRelationshipAsParent asParent : queryDTO.getTypeInMdRelationshipAsParentList())
    {
      JSONObject asParentJSON = new JSONObject();
      asParentJSON.put(JSON.BUSINESS_QUERY_DTO_TYPE_IN_REL_PARENT_DISPLAY_LABEL.getLabel(), asParent.getParentDisplayLabel());
      asParentJSON.put(JSON.BUSINESS_QUERY_DTO_TYPE_IN_REL_RELATIONSHIP_TYPE.getLabel(), asParent.getRelationshipType());
      
      asParents.put(asParentJSON);
    }
    json.put(JSON.BUSINESS_QUERY_DTO_TYPE_IN_REL_AS_PARENT.getLabel(), asParents);
    
    
    // get MdRelationship information for which the query type is a child
    JSONArray asChildren = new JSONArray();
    for(TypeInMdRelationshipAsChild asChild : queryDTO.getTypeInMdRelationshipAsChildList())
    {
      JSONObject asChildJSON = new JSONObject();
      asChildJSON.put(JSON.BUSINSS_QUERY_DTO_TYPE_IN_REL_CHILD_DISPLAY_LABEL.getLabel(), asChild.getChildDisplayLabel());
      asChildJSON.put(JSON.BUSINESS_QUERY_DTO_TYPE_IN_REL_RELATIONSHIP_TYPE.getLabel(), asChild.getRelationshipType());
      
      asChildren.put(asChildJSON);
    }
    json.put(JSON.BUSINESS_QUERY_DTO_TYPE_IN_REL_AS_CHILD.getLabel(), asChildren);
  }

  @Override
  protected String getDTOType()
  {
    return BusinessQueryDTOInfo.CLASS;
  }

}
