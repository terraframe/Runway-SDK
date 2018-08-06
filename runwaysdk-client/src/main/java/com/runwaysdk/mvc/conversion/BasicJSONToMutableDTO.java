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

import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.ComponentDTO;
import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.constants.ComponentInfo;

public abstract class BasicJSONToMutableDTO extends BasicJSONToComponentDTO
{

  /**
   * Constructor to set the sessionId and source json (as a JSONObject)
   * 
   * @param sessionId
   * @param jsonString
   */
  protected BasicJSONToMutableDTO(String sessionId, Locale locale, JSONObject json)
  {
    super(sessionId, locale, json);
  }

  /**
   * Constructor to set the sessionId and source json.
   * 
   * @param sessionId
   * @param jsonString
   * @throws JSONException
   */
  protected BasicJSONToMutableDTO(String sessionId, Locale locale, String json) throws JSONException
  {
    super(sessionId, locale, json);
  }

  @Override
  protected MutableDTO factoryMethod(String type) throws JSONException
  {
    JSONObject json = getJSON();

    boolean newInstance = !json.has(ComponentInfo.ID);
    String oid = newInstance ? "" : json.getString(ComponentInfo.ID);

    return factoryMethod(type, newInstance, oid);
  }

  /**
   * Factory method that subclasses must override that returns an instantiated
   * DTO given the information in the parameter list.
   * 
   * @param type
   * @param newInstance
   * @param oid
   *          TODO
   * @return
   * @throws JSONException
   */
  protected abstract MutableDTO factoryMethod(String type, boolean newInstance, String oid) throws JSONException;

  @Override
  protected void populate(ComponentDTO componentDTO) throws JSONException
  {
    List<String> attributeNames = componentDTO.getAttributeNames();

    for (String attributeName : attributeNames)
    {
      this.populate((MutableDTO) componentDTO, attributeName, this.getJSON());
    }
  }

}
