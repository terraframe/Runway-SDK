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

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.EnumDTO;
import com.runwaysdk.constants.JSON;

public class JSONToEnumDTO
{

  private String     sessionId;

  /**
   * The source JSON string
   */
  private JSONObject json;

  /**
   * Constructor
   * 
   * @param json
   * @throws JSONException
   */
  public JSONToEnumDTO(String sessionId, String jsonString) throws JSONException
  {
    this.sessionId = sessionId;

    json = new JSONObject(jsonString);
  }

  protected String getSessionId()
  {
    return this.sessionId;
  }

  public EnumDTO populate() throws JSONException
  {
    // type
    String type = json.getString(JSON.ENUM_DTO_TYPE.getLabel());

    // name
    String name = json.getString(JSON.ENUMERATION_DTO_IF_NAME.getLabel());

    return new EnumDTO(type, name);
  }
}
