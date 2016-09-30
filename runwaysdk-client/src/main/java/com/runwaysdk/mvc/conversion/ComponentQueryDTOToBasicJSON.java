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

public abstract class ComponentQueryDTOToBasicJSON extends DTOToBasicJSON
{
  /**
   * The QueryDTO to convert.
   */
  private ComponentQueryDTO queryDTO;

  /**
   * The json to construct.
   */
  private JSONObject        json;

  /**
   * Constructor to set the QueryDTO that will converted into JSON
   * 
   * @param queryDTO
   */
  protected ComponentQueryDTOToBasicJSON(ComponentQueryDTO queryDTO)
  {
    this.queryDTO = queryDTO;
    this.json = new JSONObject();
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
   * Returns a populated json object.
   * 
   * @return
   */
  public final JSONObject populate() throws JSONException
  {
    setProperties();

    return this.getJSON();
  }

  /**
   * Adds the result set to the json.
   */
  protected void addResultSet() throws JSONException
  {
    ComponentQueryDTO queryDTO = (ComponentQueryDTO) getComponentQueryDTO();

    JSONArray resultSet = new JSONArray();

    for (ComponentDTOIF componentDTOIF : queryDTO.getResultSet())
    {
      ComponentDTOIFToBasicJSON componentDTOToJSON = ComponentDTOIFToBasicJSON.getConverter(componentDTOIF);
      resultSet.put(componentDTOToJSON.populate());
    }

    JSONObject json = getJSON();
    json.put(JSON.CLASS_QUERY_DTO_RESULT_SET.getLabel(), resultSet);
  }

  /**
   * 
   */
  protected void setProperties() throws JSONException
  {
    // page number
    this.getJSON().put(JSON.CLASS_QUERY_DTO_PAGE_NUMBER.getLabel(), queryDTO.getPageNumber());

    // page size
    this.getJSON().put(JSON.CLASS_QUERY_DTO_PAGE_SIZE.getLabel(), queryDTO.getPageSize());

    // count
    this.getJSON().put(JSON.CLASS_QUERY_DTO_COUNT.getLabel(), queryDTO.getCount());

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
  public static ComponentQueryDTOToBasicJSON getConverter(ComponentQueryDTO queryDTO, boolean typeSafe)
  {
    ComponentQueryDTOToBasicJSON converter = null;
    if (queryDTO instanceof BusinessQueryDTO)
    {
      converter = new BusinessQueryDTOToBasicJSON((BusinessQueryDTO) queryDTO, typeSafe);
    }
    else if (queryDTO instanceof RelationshipQueryDTO)
    {
      converter = new RelationshipQueryDTOToBasicJSON((RelationshipQueryDTO) queryDTO, typeSafe);
    }
    else if (queryDTO instanceof StructQueryDTO)
    {
      converter = new StructQueryDTOToBasicJSON((StructQueryDTO) queryDTO, typeSafe);
    }
    else if (queryDTO instanceof ValueQueryDTO)
    {
      // The value query is always type-unsafe
      converter = new ValueQueryDTOToBasicJSON((ValueQueryDTO) queryDTO, false);
    }
    else if (queryDTO instanceof ViewQueryDTO)
    {
      converter = new ViewQueryDTOToBasicJSON((ViewQueryDTO) queryDTO, typeSafe);
    }
    else
    {
      String error = "Cannot convert an " + ComponentQueryDTO.class.getName() + " of type [" + queryDTO.getType() + "] into JSON.";
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), error);
    }

    return converter;
  }
}
