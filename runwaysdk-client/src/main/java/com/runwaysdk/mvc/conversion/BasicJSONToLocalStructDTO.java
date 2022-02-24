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

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.business.LocalStructDTO;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.constants.JSON;

public class BasicJSONToLocalStructDTO extends BasicJSONToStructDTO
{
  /**
   * 
   * @param json
   * @param structDTO
   * @throws JSONException
   */
  protected BasicJSONToLocalStructDTO(String sessionId, Locale locale, String json) throws JSONException
  {
    super(sessionId, locale, json);
  }

  /**
   * 
   * @param jsonObject
   * @param entityDTOIF
   * @throws JSONException
   */
  protected BasicJSONToLocalStructDTO(String sessionId, Locale locale, JSONObject jsonObject) throws JSONException
  {
    super(sessionId, locale, jsonObject);
  }

  @Override
  public LocalStructDTO populate()
  {
    LocalStructDTO struct = (LocalStructDTO) super.populate();

    try
    {
      JSONObject json = this.getJSON();

      struct.setValue(json.getString(JSON.LOCALIZED_VALE.getLabel()));
    }
    catch (JSONException e)
    {
      String msg = "Could not convert object of type [" + struct.getType() + "] from JSON to a LocalStructDTO.";

      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), msg, e);
    }

    return struct;
  }
}
