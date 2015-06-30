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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.business.ComponentDTO;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.constants.JSON;
import com.runwaysdk.transport.attributes.AttributeDTO;

public abstract class JSONToComponentDTO extends JSONToDTO
{

  /**
   * The source JSON Object
   */
  private JSONObject json;

  /**
   * Constructor to set the sessionId and source json.
   * 
   * @param sessionId
   * @param jsonString
   * @throws JSONException
   */
  protected JSONToComponentDTO(String sessionId, Locale locale, String jsonString) throws JSONException
  {
    super(sessionId, locale);

    if (JSONUtil.isNull(jsonString))
    {
      json = new JSONObject();
    }
    else
    {
      json = new JSONObject(jsonString);
    }
  }

  /**
   * Returns the source json.
   * 
   * @return
   */
  protected final JSONObject getJSON()
  {
    return json;
  }

  /**
   * Constructor to set the sessionId and source json (as a JSONObject).
   * 
   * @param sessionId
   * @param json
   */
  public JSONToComponentDTO(String sessionId, Locale locale, JSONObject json)
  {
    super(sessionId, locale);

    this.json = json;
  }

  public ComponentDTO populate()
  {
    String type = null;
    ComponentDTO componentDTO = null;
    try
    {
      // toString
      String toString = json.getString(JSON.ENTITY_DTO_TO_STRING.getLabel());

      // type
      type = json.getString(JSON.COMPONENT_DTO_TYPE.getLabel());

      // readable
      boolean readable = Boolean.parseBoolean(json.getString(JSON.ENTITY_DTO_READABLE.getLabel()));

      // attribute map
      Map<String, AttributeDTO> attributeMap = setAttributes();

      componentDTO = factoryMethod(toString, type, attributeMap, readable);
    }
    catch (JSONException e)
    {
      String msg = "Could not convert object of type [" + type + "] from JSON to a ComponentDTO.";
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), msg, e);
    }

    return componentDTO;
  }

  /**
   * Sets the attributes on the DTO
   * 
   * @throws JSONException
   */
  private Map<String, AttributeDTO> setAttributes() throws JSONException
  {
    JSONObject json = getJSON();

    Map<String, AttributeDTO> attributeDTOmap = new HashMap<String, AttributeDTO>();

    JSONObject attributeMap = json.getJSONObject(JSON.ENTITY_DTO_ATTRIBUTE_MAP.getLabel());
    Iterator<?> iter = attributeMap.keys();

    while (iter.hasNext())
    {
      JSONObject attribute = attributeMap.getJSONObject((String) iter.next());

      AttributeDTO attributeDTO = setAttribute(attribute);
      attributeDTOmap.put(attributeDTO.getName(), attributeDTO);
    }

    return attributeDTOmap;
  }

  /**
   * Factory method that subclasses must implement to return an instantiated DTO
   * with the given information.
   * 
   * @param type
   * @param attributeMap
   * @param readable
   * @return
   * @throws JSONException
   */
  protected abstract ComponentDTO factoryMethod(String toString, String type, Map<String, AttributeDTO> attributeMap, boolean readable) throws JSONException;

  public static JSONToComponentDTO getConverter(String sessionId, Locale locale, String json)
  {
    JSONToComponentDTO converter = null;
    try
    {
      if (JSONUtil.isBusinessDTO(json))
      {
        converter = new JSONToBusinessDTO(sessionId, locale, json);
      }
      else if (JSONUtil.isRelationshipDTO(json))
      {
        converter = new JSONToRelationshipDTO(sessionId, locale, json);
      }
      else if (JSONUtil.isLocalStructDTO(json))
      {
        converter = new JSONToLocalStructDTO(sessionId, locale, json);
      }
      else if (JSONUtil.isStructDTO(json))
      {
        converter = new JSONToStructDTO(sessionId, locale, json);
      }
      else if (JSONUtil.isViewDTO(json))
      {
        converter = new JSONToViewDTO(sessionId, locale, json);
      }
      else if (JSONUtil.isUtilDTO(json))
      {
        converter = new JSONToUtilDTO(sessionId, locale, json);
      }
      else
      {
        String error = "Tried to convert a non-class from a JSON string";
        CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), error);
      }
    }
    catch (JSONException e)
    {
      String msg = "An error occured while trying to convert JSON to a ComponentDTO.";
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), msg, e);
    }

    return converter;
  }

}
