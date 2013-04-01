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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.business.BusinessQueryDTO;
import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.business.ComponentQueryDTO;
import com.runwaysdk.business.RelationshipQueryDTO;
import com.runwaysdk.business.StructQueryDTO;
import com.runwaysdk.business.ValueQueryDTO;
import com.runwaysdk.business.ViewQueryDTO;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.constants.JSON;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.transport.attributes.AttributeDTO;

public abstract class ComponentQueryDTOToJSON extends DTOToJSON
{
  /**
   * The QueryDTO to convert.
   */
  private ComponentQueryDTO queryDTO;

  /**
   * The json to construct.
   */
  private JSONObject json;
  
  private boolean typeSafe;
  
  /**
   * Constructor to set the QueryDTO that will converted into JSON
   * 
   * @param queryDTO
   */
  protected ComponentQueryDTOToJSON(ComponentQueryDTO queryDTO, boolean typeSafe)
  {
    this.queryDTO = queryDTO;
    this.json = new JSONObject();
    this.typeSafe = typeSafe;
  }
  
  
  /**
   * Returns the destination JSON object.
   * 
   * @return
   */
  protected JSONObject getJSON()
  {
    return json;
  }
  
  /**
   * Returns the source ComponentQueryDTO object.
   * 
   * @return
   */
  protected ComponentQueryDTO getComponentQueryDTO()
  {
    return queryDTO;
  }
  
  /**
   * Subclasses must override this to return the appropriate query 
   * DTO subclass as defined 
   * @return
   */
  protected abstract String getDTOType();

  /**
   * Returns a populated json object.
   * 
   * @return
   */
  public final JSONObject populate() throws JSONException
  {
    setProperties();
    
    setAttributes();
    
    return this.getJSON();
  }
  
  /**
   * Adds the result set to the json.
   */
  protected void addResultSet() throws JSONException
  {
    ComponentQueryDTO queryDTO = (ComponentQueryDTO) getComponentQueryDTO();
    
    JSONArray resultSet = new JSONArray();
    
    for(ComponentDTOIF componentDTOIF : queryDTO.getResultSet())
    {
      ComponentDTOIFToJSON componentDTOToJSON = ComponentDTOIFToJSON.getConverter(componentDTOIF);
      resultSet.put(componentDTOToJSON.populate());
    }

    JSONObject json = getJSON();
    json.put(JSON.CLASS_QUERY_DTO_RESULT_SET.getLabel(), resultSet);
  }

  
  /**
   * Adds the defined attributes to the json.
   * 
   * @throws JSONException
   */
  private void setAttributes() throws JSONException
  {
    ComponentQueryDTO queryDTO = this.getComponentQueryDTO();
    
    JSONObject attributes = new JSONObject();

    for (String name : queryDTO.getAttributeNames())
    {
      AttributeDTO attributeDTO = queryDTO.getAttributeDTO(name);
      
      JSONObject attribute = setAttribute(attributeDTO);
      if(attribute != null)
      {
        // add the attribute to the attributeMap
        attributes.put(name, attribute);
      }
    }

    JSONObject json = getJSON();
    json.put(JSON.CLASS_QUERY_DTO_DEFINED_ATTRIBUTES.getLabel(), attributes);
  }
  
  
  /**
   * 
   */
  protected void setProperties() throws JSONException
  {
    ComponentQueryDTO queryDTO = this.getComponentQueryDTO();

    // query type
    String type = queryDTO.getType();
    this.getJSON().put(JSON.CLASS_QUERY_DTO_QUERY_TYPE.getLabel(), queryDTO.getType());
    
    /*
     * If this is a type-safe query DTO, as is used for the MdMethod return types,
     * then include the JSON.COMPONENT_DTO_TYPE property, which will signals to the
     * Javascript that there is a "type-safe" javascript class definition for this object.
     */
    if(this.typeSafe)
    {
      // type (for type-safety)
      String typeSafeClass = type + TypeGeneratorInfo.QUERY_DTO_SUFFIX;
      this.getJSON().put(JSON.COMPONENT_DTO_TYPE.getLabel(), typeSafeClass);
    }
    
    // query dto subclass type
    String dtoType = getDTOType();
    json.put(JSON.DTO_TYPE.getLabel(), dtoType);

    // page number
    this.getJSON().put(JSON.CLASS_QUERY_DTO_PAGE_NUMBER.getLabel(), queryDTO.getPageNumber());
    
    // page size
    this.getJSON().put(JSON.CLASS_QUERY_DTO_PAGE_SIZE.getLabel(), queryDTO.getPageSize());
    
    // count
    this.getJSON().put(JSON.CLASS_QUERY_DTO_COUNT.getLabel(), queryDTO.getCount());
    
    // countEnabled
    this.getJSON().put(JSON.CLASS_QUERY_DTO_COUNT_ENABLED.getLabel(), queryDTO.isCountEnabled());
    
    // groovy query
    this.json.put(JSON.COMPONENT_QUERY_DTO_GROOVY_QUERY.getLabel(), queryDTO.getGroovyQuery());
    
    // result set
    addResultSet();
  }
  
  /**
   * Given an ComponentQueryDTO, this method returns a ComponentQueryDTOToJSON
   * subclass that generates the JSON to represent the ComponentQueryDTO.
   * 
   * @param queryDTO
   * @return
   */
  public static ComponentQueryDTOToJSON getConverter(ComponentQueryDTO queryDTO, boolean typeSafe)
  {
    ComponentQueryDTOToJSON converter = null;
    if(queryDTO instanceof BusinessQueryDTO)
    {
      converter = new BusinessQueryDTOToJSON((BusinessQueryDTO) queryDTO, typeSafe);
    }
    else if(queryDTO instanceof RelationshipQueryDTO)
    {
      converter =  new RelationshipQueryDTOToJSON((RelationshipQueryDTO) queryDTO, typeSafe);
    }
    else if(queryDTO instanceof StructQueryDTO)
    {
      converter =  new StructQueryDTOToJSON((StructQueryDTO) queryDTO, typeSafe);
    }
    else if(queryDTO instanceof ValueQueryDTO)
    {
      // The value query is always type-unsafe
      converter =  new ValueQueryDTOToJSON((ValueQueryDTO) queryDTO, false);
    }
    else if(queryDTO instanceof ViewQueryDTO)
    {
      converter =  new ViewQueryDTOToJSON((ViewQueryDTO) queryDTO, typeSafe);
    }
    else
    {
      String error = "Cannot convert an "+ComponentQueryDTO.class.getName()+" of type ["+queryDTO.getType()+"] into JSON.";
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), error);    
    }
    
    return converter;
  }
}
