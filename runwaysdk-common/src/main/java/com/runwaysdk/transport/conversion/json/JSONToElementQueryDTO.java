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

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.ElementQueryDTO;
import com.runwaysdk.constants.JSON;

public abstract class JSONToElementQueryDTO extends JSONToEntityQueryDTO
{

  /**
   * 
   * @param sessionId
   * @param json
   * @param queryDTO
   * @throws JSONException
   */
  public JSONToElementQueryDTO(String sessionId, Locale locale, JSONObject json, ElementQueryDTO queryDTO) throws JSONException
  {
    super(sessionId, locale, json, queryDTO);
  }
  
  @Override
  protected ElementQueryDTO getComponentQueryDTO()
  {
    return (ElementQueryDTO) super.getComponentQueryDTO();
  }
  
  @Override
  public ElementQueryDTO populate() throws JSONException
  {
    super.populate();
    
    ElementQueryDTO queryDTO = this.getComponentQueryDTO();
    JSONObject json = getJSON();
    
    // isAbstract
    boolean isAbstract = Boolean.parseBoolean(json.getString(JSON.ELEMENT_QUERY_DTO_ISABSTRACT.getLabel()));
    queryDTO.setAbstract(isAbstract);
    
    return queryDTO;
  }

}
