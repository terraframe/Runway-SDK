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

import com.runwaysdk.business.EntityDTO;

public abstract class JSONToEntityDTO extends JSONToMutableDTO
{
  /**
   * Constructor
   * 
   * @param json
   * @param entityDTOIF
   * @throws JSONException 
   */
  protected JSONToEntityDTO(String sessionId, Locale locale, String json) throws JSONException
  {
    super(sessionId, locale, json);
  }
  
  /**
   * 
   * @param json
   * @param entityDTO
   * @throws JSONException
   */
  protected JSONToEntityDTO(String sessionId, Locale locale, JSONObject json) throws JSONException
  {
    super(sessionId, locale, json);
  }

  @Override
  public EntityDTO populate()
  {
    return (EntityDTO) super.populate();
  }
}
