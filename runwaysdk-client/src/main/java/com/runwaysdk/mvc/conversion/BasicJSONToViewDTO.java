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
package com.runwaysdk.mvc.conversion;

import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.ViewDTO;
import com.runwaysdk.constants.ClientRequestIF;

public class BasicJSONToViewDTO extends BasicJSONToSessionDTO
{

  public BasicJSONToViewDTO(String sessionId, Locale locale, JSONObject json)
  {
    super(sessionId, locale, json);
  }

  public BasicJSONToViewDTO(String sessionId, Locale locale, String json) throws JSONException
  {
    super(sessionId, locale, json);
  }

  @Override
  protected ViewDTO factoryMethod(String type, boolean newInstance, String oid) throws JSONException
  {
    ClientRequestIF request = getClientRequest();

    if (newInstance)
    {
      return (ViewDTO) request.newMutable(type);
    }
    else
    {
      return (ViewDTO) request.get(oid);
    }
  }

  @Override
  public ViewDTO populate()
  {
    return (ViewDTO) super.populate();
  }

}
