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

import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.business.BusinessQueryDTO;
import com.runwaysdk.business.ComponentDTOFacade;
import com.runwaysdk.business.ComponentQueryDTO;
import com.runwaysdk.business.RelationshipQueryDTO;
import com.runwaysdk.business.StructQueryDTO;
import com.runwaysdk.business.ValueQueryDTO;
import com.runwaysdk.business.ViewQueryDTO;
import com.runwaysdk.constants.BusinessQueryDTOInfo;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.constants.JSON;
import com.runwaysdk.constants.RelationshipQueryDTOInfo;
import com.runwaysdk.constants.StructQueryDTOInfo;
import com.runwaysdk.constants.ValueQueryDTOInfo;
import com.runwaysdk.constants.ViewQueryDTOInfo;

public class JSONToComponentQueryDTO extends JSONToDTO
{
  /**
   * The source JSONObject
   */
  private JSONObject     json;

  /**
   * The destination QueryDTO
   */
  private ComponentQueryDTO queryDTO;
  
  /**
   * Constructor to set the json string.
   * 
   * @param json
   * @throws JSONException
   */
  public JSONToComponentQueryDTO(String sessionId, Locale locale, JSONObject json, ComponentQueryDTO queryDTO)
      throws JSONException
  {
    super(sessionId, locale);
    this.json = json;
    this.queryDTO = queryDTO;
  }
  
  protected ComponentQueryDTO getComponentQueryDTO()
  {
    return this.queryDTO;
  }

  /**
   * Returns the source JSON obect.
   * 
   * @return
   */
  protected JSONObject getJSON()
  {
    return this.json;
  }

  
  /**
   * Returns the populated QueryDTO
   * 
   * @return
   * @throws JSONException
   */
  public ComponentQueryDTO populate() throws JSONException
  {    
    ComponentQueryDTO queryDTO = this.getComponentQueryDTO();
    
    // page number
    int pageNumber = this.getJSON().getInt(JSON.CLASS_QUERY_DTO_PAGE_NUMBER.getLabel());
    this.getComponentQueryDTO().setPageNumber(pageNumber);

    // page size
    int pageSize = this.getJSON().getInt(JSON.CLASS_QUERY_DTO_PAGE_SIZE.getLabel());
    queryDTO.setPageSize(pageSize);

    // count
    int count = this.getJSON().getInt(JSON.CLASS_QUERY_DTO_COUNT.getLabel());
    queryDTO.setCount(count);

    // count enabled
    boolean countEnabled = this.getJSON().getBoolean(JSON.CLASS_QUERY_DTO_COUNT_ENABLED.getLabel());
    queryDTO.setCountEnabled(countEnabled);
    
    // result set
//    addResultSet();
    
    // defined attributes
//    addDefinedAttributes();
    
    return queryDTO;
  }
  
  /**
   * Given a json string, this method returns a subclass of JSONToComponentQueryDTO
   * that can convert the json string into an ComponentQueryDTO.
   * 
   * @param json
   * @return
   * @throws JSONException
   */
  public static JSONToComponentQueryDTO getConverter(String sessionId, Locale locale, String jsonStr) throws JSONException
  {
    JSONObject json = new JSONObject(jsonStr);
    JSONToComponentQueryDTO converter = null;
    String querySubclass = json.getString(JSON.DTO_TYPE.getLabel());

    if (querySubclass.equals(BusinessQueryDTOInfo.CLASS))
    {
      String type = json.getString(JSON.CLASS_QUERY_DTO_QUERY_TYPE.getLabel());

      BusinessQueryDTO queryDTO = ComponentDTOFacade.buildBusinessQueryDTO(type);
      converter = new JSONToBusinessQueryDTO(sessionId, locale, json, queryDTO);
    }
    else if (querySubclass.equals(RelationshipQueryDTOInfo.CLASS))
    {
      String type = json.getString(JSON.CLASS_QUERY_DTO_QUERY_TYPE.getLabel());

      RelationshipQueryDTO queryDTO = ComponentDTOFacade.buildRelationshipQueryDTO(type);
      converter = new JSONToRelationshipQueryDTO(sessionId, locale, json, queryDTO);
    }
    else if (querySubclass.equals(StructQueryDTOInfo.CLASS))
    {
      String type = json.getString(JSON.CLASS_QUERY_DTO_QUERY_TYPE.getLabel());

      StructQueryDTO queryDTO = ComponentDTOFacade.buildStructQueryDTO(type);
      converter = new JSONToStructQueryDTO(sessionId, locale, json, queryDTO);
    }
    else if (querySubclass.equals(ViewQueryDTOInfo.CLASS))
    {
      String type = json.getString(JSON.CLASS_QUERY_DTO_QUERY_TYPE.getLabel());
      ViewQueryDTO queryDTO = ComponentDTOFacade.buildViewQueryDTO(type);
      converter = new JSONToViewQueryDTO(sessionId, locale, json, queryDTO);
    }
    else if (querySubclass.equals(ValueQueryDTOInfo.CLASS))
    {
      String groovyQuery = json.getString(JSON.COMPONENT_QUERY_DTO_GROOVY_QUERY.getLabel());
      ValueQueryDTO valueQueryDTO = new ValueQueryDTO(groovyQuery);
      converter = new JSONToValueQueryDTO(sessionId, locale, json, valueQueryDTO);
    }
    else
    {
      String error = "Could not find the ["+JSONToComponentQueryDTO.class.getName()+"] converter for type ["+querySubclass+"]";
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), error);    
    }
    
    return converter;
  }
  
}
