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
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.ComponentDTOFacade;
import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.business.ViewDTO;
import com.runwaysdk.transport.attributes.AttributeDTO;

public class JSONToViewDTO extends JSONToSessionDTO
{

  public JSONToViewDTO(String sessionId, Locale locale, JSONObject json)
  {
    super(sessionId, locale, json);
  }
  
  public JSONToViewDTO(String sessionId, Locale locale, String json) throws JSONException
  {
    super(sessionId, locale, json);
  }

  @Override
  protected MutableDTO factoryMethod(String toString, String type,
      Map<String, AttributeDTO> attributeMap, boolean newInstance, boolean readable, boolean writable,
      boolean modified) throws JSONException
  {
    return ComponentDTOFacade.buildViewDTO(null, type, attributeMap,
        newInstance, readable, writable, modified,  toString);
  }
  
  @Override
  public ViewDTO populate()
  {
    return (ViewDTO) super.populate();
  }

}
