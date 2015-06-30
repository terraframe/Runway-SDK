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

import com.runwaysdk.business.ClassQueryDTO;

public abstract class JSONToClassQueryDTO extends JSONToComponentQueryDTO
{

  public JSONToClassQueryDTO(String sessionId, Locale locale, JSONObject json, ClassQueryDTO queryDTO) throws JSONException
  {
    super(sessionId, locale, json, queryDTO);
  }
  
  @Override
  protected ClassQueryDTO getComponentQueryDTO()
  {
    return (ClassQueryDTO) super.getComponentQueryDTO();
  }
  
  @Override
  public ClassQueryDTO populate() throws JSONException
  {
    super.populate();
    
    // classes
//    addClasses();
    
    return getComponentQueryDTO();
  }
  
  
  /**
   * Adds the result set to the QueryDTO.
   */
//  private void addResultSet() throws JSONException
//  {
//    EntityQueryDTO queryDTO = (EntityQueryDTO) getClassQueryDTO();
//    JSONObject json = getJSON();
//    JSONArray results = json.getJSONArray(JSON.CLASS_QUERY_DTO_RESULT_SET.getLabel());
//    for(int i=0; i<results.length(); i++)
//    {
//      String entityJSON = results.getString(i);
//      
//      EntityDTO entityDTO = JSONFacade.getEntityDTOFromJSON(this.getSessionId(), entityJSON);
//      queryDTO.addResultItem(entityDTO);
//    }
//  }

  
  /**
   * Adds defined attributes to the QueryDTO.
   */
//  private void addDefinedAttributes() throws JSONException
//  {
//    ClassQueryDTO queryDTO = (ClassQueryDTO) getClassQueryDTO();
//    JSONObject json = getJSON();
//    JSONObject attributeMap = json.getJSONObject(JSON.CLASS_QUERY_DTO_DEFINED_ATTRIBUTES.getLabel());
//    Iterator<?> iter = attributeMap.keys();
//    
//    while(iter.hasNext())
//    {
//      JSONObject attribute = attributeMap.getJSONObject((String)iter.next());
//
//      AttributeDTO attributeDTO = setAttribute(attribute);
//      
//      queryDTO.addAttribute(attributeDTO);
//    }
//  }
  
  /**
   * Adds the classes to the QueryDTO.
   */
//  private void addClasses() throws JSONException
//  {
//    ClassQueryDTO queryDTO = getClassQueryDTO();
//    JSONObject json = getJSON();
//    
//    JSONArray classes = json.getJSONArray(JSON.CLASS_QUERY_DTO_CLASSES.getLabel());
//    for(int i=0; i<classes.length(); i++)
//    {
//      JSONObject classObj = classes.getJSONObject(i);
//      
//      String type = classObj.getString(JSON.CLASS_QUERY_DTO_CLASS_TYPE.getLabel());
//      
//      JSONArray superClasses = classObj.getJSONArray(JSON.CLASS_QUERY_DTO_CLASS_SUPERCLASSES.getLabel());
//      List<String> superTypes = new LinkedList<String>();
//      for(int j=0; j<superClasses.length(); j++)
//      {
//        superTypes.add(superClasses.getString(j));
//      }
//      
//      queryDTO.addClassType(type, superTypes);
//    }
//  }
}
