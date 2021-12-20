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

import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.ComponentDTO;
import com.runwaysdk.constants.BusinessDTOInfo;
import com.runwaysdk.constants.EnumDTOInfo;
import com.runwaysdk.constants.JSON;
import com.runwaysdk.constants.LocalStructDTOInfo;
import com.runwaysdk.constants.RelationshipDTOInfo;
import com.runwaysdk.constants.StructDTOInfo;
import com.runwaysdk.constants.UtilDTOInfo;
import com.runwaysdk.constants.ViewDTOInfo;

public class JSONUtil
{
  /**
   * Checks if a JSON string is an array.
   * 
   * @param json
   * @return true if the JSON string is an array, false otherwise
   */
  public static boolean isArray(String json)
  {
    return ( json.startsWith("[") && json.endsWith("]") );
  }

  /**
   * Checks if a string is null.
   * 
   * @param json
   * @return
   */
  public static boolean isNull(String json)
  {
    // return ( json.equals("{}") || json.equalsIgnoreCase("null") ||
    // json.equalsIgnoreCase("undefined") );
    if (json == null)
    {
      return true;
    }
    return json.equalsIgnoreCase("null");
  }

  /**
   * Checks if a json string represents an EnumDTO
   * 
   * @param json
   * @return
   * @throws JSONException
   */
  public static boolean isEnumDTO(String json) throws JSONException
  {
    JSONObject jsonObject = new JSONObject(json);
    String subclass = jsonObject.getString(JSON.DTO_TYPE.getLabel());

    return subclass.equals(EnumDTOInfo.CLASS);
  }

  /**
   * Checks if a json string represents a BusinessDTO
   * 
   * @param json
   * @return
   * @throws JSONException
   */
  public static boolean isBusinessDTO(String json) throws JSONException
  {
    JSONObject jsonObject = new JSONObject(json);
    String subclass = jsonObject.getString(JSON.DTO_TYPE.getLabel());

    return subclass.equals(BusinessDTOInfo.CLASS);
  }

  /**
   * Checks if a json string represents a StructDTO
   * 
   * @param json
   * @return
   * @throws JSONException
   */
  public static boolean isStructDTO(String json) throws JSONException
  {
    JSONObject jsonObject = new JSONObject(json);
    String subclass = jsonObject.getString(JSON.DTO_TYPE.getLabel());

    return subclass.equals(StructDTOInfo.CLASS);
  }

  /**
   * Checks if a json string represents a LocalStructDTO
   * 
   * @param json
   * @return
   * @throws JSONException
   */
  public static boolean isLocalStructDTO(String json) throws JSONException
  {
    JSONObject jsonObject = new JSONObject(json);
    String subclass = jsonObject.getString(JSON.DTO_TYPE.getLabel());
    
    return subclass.equals(LocalStructDTOInfo.CLASS);
  }
  
  /**
   * Checks if a json string represents a ViewDTO
   * 
   * @param json
   * @return
   * @throws JSONException
   */
  public static boolean isViewDTO(String json) throws JSONException
  {
    JSONObject jsonObject = new JSONObject(json);
    String subclass = jsonObject.getString(JSON.DTO_TYPE.getLabel());

    return subclass.equals(ViewDTOInfo.CLASS);
  }

  /**
   * Checks if a json string represents a UtilDTO
   * 
   * @param json
   * @return
   * @throws JSONException
   */
  public static boolean isUtilDTO(String json) throws JSONException
  {
    JSONObject jsonObject = new JSONObject(json);
    String subclass = jsonObject.getString(JSON.DTO_TYPE.getLabel());

    return subclass.equals(UtilDTOInfo.CLASS);
  }

  /**
   * Checks if a json string represents a RelationshipDTO
   * 
   * @param json
   * @return
   * @throws JSONException
   */
  public static boolean isRelationshipDTO(String json) throws JSONException
  {
    JSONObject jsonObject = new JSONObject(json);
    String subclass = jsonObject.getString(JSON.DTO_TYPE.getLabel());

    return subclass.equals(RelationshipDTOInfo.CLASS);
  }

  /**
   * Returns an ComponentDTO whose runtime type is either BusinessDTO or
   * RelationshipDTO. It is up to the invoking method to cast properly. A null
   * is returned if the input json represents a null object.
   * 
   * @param json
   * @return EntityDTO
   */
  public static ComponentDTO getComponentDTOFromJSON(String sessionId, Locale locale, String json)
  {
    if (isNull(json))
    {
      return null;
    }
    else
    {
      JSONToComponentDTO jsonToComponentDTO = JSONToComponentDTO.getConverter(sessionId, locale, json);
      return jsonToComponentDTO.populate();
    }
  }
}
