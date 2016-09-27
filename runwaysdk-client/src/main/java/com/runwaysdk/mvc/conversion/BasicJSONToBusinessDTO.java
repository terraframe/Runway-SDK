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

import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.constants.ClientRequestIF;

public class BasicJSONToBusinessDTO extends BasicJSONToElementDTO
{
  /**
   * 
   * @param locale
   * @param json
   * @throws JSONException
   */
  protected BasicJSONToBusinessDTO(String sessionId, Locale locale, String json) throws JSONException
  {
    super(sessionId, locale, json);
  }

  /**
   * 
   * @param jsonObject
   * @throws JSONException
   */
  protected BasicJSONToBusinessDTO(String sessionId, Locale locale, JSONObject jsonObject) throws JSONException
  {
    super(sessionId, locale, jsonObject);
  }

  /**
   * Instantiates StructDTO (not type safe)
   * 
   * @param type
   * @param newInstance
   * @return StructDTO (not type safe)
   */
  protected BusinessDTO factoryMethod(String type, boolean newInstance, String id) throws JSONException
  {
    ClientRequestIF request = getClientRequest();

    if (newInstance)
    {
      return request.newBusiness(type);
    }
    else
    {
      return (BusinessDTO) request.get(id);
    }
  }

  @Override
  public BusinessDTO populate()
  {
    return (BusinessDTO) super.populate();
  }
}
