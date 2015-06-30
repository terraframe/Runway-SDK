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
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.ElementDTO;
import com.runwaysdk.constants.JSON;
import com.runwaysdk.transport.attributes.AttributeDTO;

public abstract class JSONToElementDTO extends JSONToEntityDTO
{  
  /**
   * 
   * @param json
   * @param elementDTO
   * @throws JSONException
   */
  protected JSONToElementDTO(String sessionId, Locale locale, String json) throws JSONException
  {
    super(sessionId, locale, json);
  }

  /**
   * 
   * @param jsonObject
   * @param elementDTO
   * @throws JSONException 
   */
  protected JSONToElementDTO(String sessionId, Locale locale, JSONObject jsonObject) throws JSONException
  {
    super(sessionId, locale, jsonObject);
  }
  
  /**
   * Instantiates an Element (not type safe)
   * 
   * @param toString
   * @param type
   * @param attributeMap
   * @param newInstance
   * @param readable
   * @param writable
   * @param modified
   * @return BusinessDTO (not type safe)
   */
  @Override
  protected ElementDTO factoryMethod(String toString, String type, Map<String, AttributeDTO> attributeMap,
      boolean newInstance, boolean readable, boolean writable, boolean modified) throws JSONException
  {
    boolean lockedByCurrentUser = Boolean.parseBoolean(getJSON().getString(JSON.ENTITY_DTO_LOCKED_BY_CURRENT_USER.getLabel()));
    
    return factoryMethod(toString, type, attributeMap,
         newInstance, readable, writable, modified, lockedByCurrentUser);
  }
  
  /**
   * Instantiates an Element (not type safe)
   * 
   * @param toString
   * @param type
   * @param attributeMap
   * @param newInstance
   * @param readable
   * @param writable
   * @param modified
   * @param lockedByCurrentUser
   * @return ElementDTO (not type safe)
   */
  protected abstract ElementDTO factoryMethod(String toString, String type, Map<String, AttributeDTO> attributeMap,
      boolean newInstance, boolean readable, boolean writable, boolean modified, boolean lockedByCurrentUser) throws JSONException;

}
