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

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.ClassQueryDTO.OrderBy;
import com.runwaysdk.business.ClassQueryDTO.QueryCondition;
import com.runwaysdk.business.ClassQueryDTO.StructOrderBy;
import com.runwaysdk.business.EntityQueryDTO;
import com.runwaysdk.constants.JSON;

public abstract class EntityQueryDTOToJSON extends ClassQueryDTOToJSON
{
  protected EntityQueryDTOToJSON(EntityQueryDTO queryDTO, boolean typeSafe)
  {
    super(queryDTO, typeSafe);
  }
  
  @Override
  protected void setProperties() throws JSONException
  {
    super.setProperties();
    
    // condition list
    addConditions();
    
    // add order bys
    addOrderBys();
  }
  
  @Override
  protected EntityQueryDTO getComponentQueryDTO()
  {
    return (EntityQueryDTO) super.getComponentQueryDTO();
  }
  
  /**
   * Adds the order by conditions to the json
   * @throws JSONException
   */
  private void addOrderBys() throws JSONException
  {
    EntityQueryDTO queryDTO = this.getComponentQueryDTO();
    JSONObject json = getJSON();
    
    JSONArray orderByList = new JSONArray();
    for(OrderBy orderBy : queryDTO.getOrderByList())
    {
      JSONObject orderByJSON = new JSONObject();
      if(orderBy instanceof StructOrderBy)
      {
        StructOrderBy structOrderBy = (StructOrderBy) orderBy;
        orderByJSON.put(JSON.ELEMENT_QUERY_DTO_ORDER_BY_ATTRIBUTE_STRUCT.getLabel(), structOrderBy.getAttributeStruct());
        orderByJSON.put(JSON.ENTITY_QUERY_DTO_ORDER_BY_ATTRIBUTE.getLabel(), structOrderBy.getAttribute());
        orderByJSON.put(JSON.ENTITY_QUERY_DTO_ORDER_BY_ORDER.getLabel(), structOrderBy.getOrder());
      }
      else
      {
        orderByJSON.put(JSON.ENTITY_QUERY_DTO_ORDER_BY_ATTRIBUTE.getLabel(), orderBy.getAttribute());
        orderByJSON.put(JSON.ENTITY_QUERY_DTO_ORDER_BY_ORDER.getLabel(), orderBy.getOrder());
      }
      
      orderByList.put(orderByJSON);
    }
    
    json.put(JSON.ENTITY_QUERY_DTO_ORDER_BY_LIST.getLabel(), orderByList);
  }
  
  /**
   * Adds the query conditions to the json.
   */
  private void addConditions() throws JSONException
  {
    EntityQueryDTO queryDTO = this.getComponentQueryDTO();
    
    JSONArray conditionsArray = new JSONArray();
    
    List<QueryCondition> conditions = queryDTO.getConditions();
    for(QueryCondition condition : conditions)
    {
      String name = condition.getAttribute();
      String conditionType = condition.getCondition();
      String value = condition.getConditionValue();
      
      JSONObject conditionJSON = new JSONObject();
      conditionJSON.put(JSON.ENTITY_QUERY_DTO_CONDITION_ATTRIBUTE.getLabel(), name);
      conditionJSON.put(JSON.ENTITY_QUERY_DTO_CONDITION_CONDITION.getLabel(), conditionType);
      conditionJSON.put(JSON.ENTITY_QUERY_DTO_CONDITION_VALUE.getLabel(), value);
      
      conditionsArray.put(conditionJSON);
    }

    JSONObject json = getJSON();
    json.put(JSON.ENTITY_QUERY_DTO_CONDITIONS.getLabel(), conditionsArray);
  }
}
