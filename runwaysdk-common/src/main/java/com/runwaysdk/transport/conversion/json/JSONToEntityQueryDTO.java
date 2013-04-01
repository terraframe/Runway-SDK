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
package com.runwaysdk.transport.conversion.json;

import java.util.Iterator;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.ElementQueryDTO;
import com.runwaysdk.business.EntityQueryDTO;
import com.runwaysdk.constants.JSON;

public class JSONToEntityQueryDTO extends JSONToClassQueryDTO
{

  public JSONToEntityQueryDTO(String sessionId, Locale locale, JSONObject json, EntityQueryDTO queryDTO) throws JSONException
  {
    super(sessionId, locale, json, queryDTO);
  }

  @Override
  public EntityQueryDTO populate() throws JSONException
  {
    super.populate();
    
    EntityQueryDTO queryDTO = (EntityQueryDTO) super.populate();
    
    // condition list
    addConditions();
    
    // order by
    addOrderBys();
    
    return queryDTO;
  }
  
  @Override
  protected EntityQueryDTO getComponentQueryDTO()
  {
    return (EntityQueryDTO) super.getComponentQueryDTO();
  }
  
  /**
   * Adds all OrderBy objects to the EntityQueryDTO
   * @throws JSONException
   */
  private void addOrderBys() throws JSONException
  {
    EntityQueryDTO queryDTO = this.getComponentQueryDTO();
    JSONObject json = getJSON();

    JSONArray orderByList = json.getJSONArray(JSON.ENTITY_QUERY_DTO_ORDER_BY_LIST.getLabel());
    for(int i=0; i<orderByList.length(); i++)
    {
      JSONObject orderByJSON = orderByList.getJSONObject(i);
      
      // check for the StructOrderBy, which as 3 values in it
      if(orderByJSON.length() == 3 && queryDTO instanceof ElementQueryDTO)
      {
        ElementQueryDTO elementQueryDTO = (ElementQueryDTO) queryDTO;
        String attributeStruct = orderByJSON.getString(JSON.ELEMENT_QUERY_DTO_ORDER_BY_ATTRIBUTE_STRUCT.getLabel());
        String attribute = orderByJSON.getString(JSON.ENTITY_QUERY_DTO_ORDER_BY_ATTRIBUTE.getLabel());
        String order = orderByJSON.getString(JSON.ENTITY_QUERY_DTO_ORDER_BY_ORDER.getLabel());
        
        elementQueryDTO.addStructOrderBy(attributeStruct, attribute, order, attribute);
      }
      else
      {
        String attribute = orderByJSON.getString(JSON.ENTITY_QUERY_DTO_ORDER_BY_ATTRIBUTE.getLabel());
        String order = orderByJSON.getString(JSON.ENTITY_QUERY_DTO_ORDER_BY_ORDER.getLabel());
        
        queryDTO.addOrderBy(attribute, order, attribute);
      }
    }
  }
  
  /**
   * 
   * @throws JSONException
   */
  private void addConditions() throws JSONException
  {
    EntityQueryDTO queryDTO = this.getComponentQueryDTO();
    JSONObject json = getJSON();
    
    // add all conditions
    // the conditions coming in are contained in an object, not an array as when they were sent to the client
    JSONObject conditions = json.getJSONObject(JSON.ENTITY_QUERY_DTO_CONDITIONS.getLabel());
    Iterator<?> iter = conditions.keys();
    while(iter.hasNext())
    {
      JSONObject entry = conditions.getJSONObject((String)iter.next());
      String attributeName = entry.getString(JSON.ENTITY_QUERY_DTO_CONDITION_ATTRIBUTE.getLabel());
      String condition = entry.getString(JSON.ENTITY_QUERY_DTO_CONDITION_CONDITION.getLabel());
      String value = entry.getString(JSON.ENTITY_QUERY_DTO_CONDITION_VALUE.getLabel());
      
      queryDTO.addCondition(attributeName, condition, value);
    }
  }
  

}
