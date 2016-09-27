/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.mvc.conversion;

import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.ComponentDTO;
import com.runwaysdk.business.LocalStructDTO;
import com.runwaysdk.business.RelationshipDTO;
import com.runwaysdk.business.StructDTO;
import com.runwaysdk.business.UtilDTO;
import com.runwaysdk.business.ViewDTO;
import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.generation.loader.LoaderDecorator;

public abstract class BasicJSONToComponentDTO extends BasicJSONToDTO
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
  protected BasicJSONToComponentDTO(String sessionId, Locale locale, String jsonString) throws JSONException
  {
    super(sessionId, locale);

    if (jsonString != null && jsonString.length() > 0)
    {
      json = new JSONObject(jsonString);
    }
    else
    {
      json = new JSONObject();
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
  public BasicJSONToComponentDTO(String sessionId, Locale locale, JSONObject json)
  {
    super(sessionId, locale);

    this.json = json;
  }

  public ComponentDTO populate()
  {
    try
    {
      // type
      String type = json.getString(ComponentInfo.TYPE);

      ComponentDTO componentDTO = this.factoryMethod(type);

      this.populate(componentDTO);

      return componentDTO;
    }
    catch (JSONException e)
    {
      String msg = "Could not convert object of type from JSON to a ComponentDTO.";

      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), msg, e);
    }

    return null;
  }

  protected abstract void populate(ComponentDTO componentDTO) throws JSONException;

  /**
   * Factory method that subclasses must implement to return an instantiated DTO
   * with the given information.
   * 
   * @param type
   * @return
   * @throws JSONException
   */
  protected abstract ComponentDTO factoryMethod(String type) throws JSONException;

  public static BasicJSONToComponentDTO getConverter(String sessionId, Locale locale, String className, String json)
  {
    BasicJSONToComponentDTO converter = null;

    try
    {
      Class<?> type = LoaderDecorator.loadClass(className);

      if (BusinessDTO.class.isAssignableFrom(type))
      {
        converter = new BasicJSONToBusinessDTO(sessionId, locale, json);
      }
      else if (RelationshipDTO.class.isAssignableFrom(type))
      {
        converter = new BasicJSONToRelationshipDTO(sessionId, locale, json);
      }
      else if (LocalStructDTO.class.isAssignableFrom(type))
      {
        converter = new BasicJSONToLocalStructDTO(sessionId, locale, json);
      }
      else if (StructDTO.class.isAssignableFrom(type))
      {
        converter = new BasicJSONToStructDTO(sessionId, locale, json);
      }
      else if (ViewDTO.class.isAssignableFrom(type))
      {
        converter = new BasicJSONToViewDTO(sessionId, locale, json);
      }
      else if (UtilDTO.class.isAssignableFrom(type))
      {
        converter = new BasicJSONToUtilDTO(sessionId, locale, json);
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
    catch (ClassNotFoundException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return converter;
  }

}
