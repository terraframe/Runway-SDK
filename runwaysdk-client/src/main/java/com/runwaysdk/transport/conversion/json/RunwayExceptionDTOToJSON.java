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

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.constants.JSON;
import com.runwaysdk.constants.RunwayExceptionDTOInfo;

public class RunwayExceptionDTOToJSON
{
  private String wrappedException;

  private String devMessage;

  private String localizedMessage;

  public RunwayExceptionDTOToJSON(String wrappedException, String devMessage, String localizedMessage)
  {
    this.wrappedException = wrappedException;
    this.devMessage = devMessage;
    this.localizedMessage = localizedMessage;
  }

  public JSONObject populate() throws JSONException
  {
    JSONObject json = new JSONObject();

    // exception type
    json.put(JSON.DTO_TYPE.getLabel(), RunwayExceptionDTOInfo.CLASS);

    // wrapped exception
    json.put(JSON.EXCEPTION_DTO_WRAPPED_EXCEPTION.getLabel(), wrappedException);

    // localized message
    json.put(JSON.EXCEPTION_LOCALIZED_MESSAGE.getLabel(), localizedMessage != null ? localizedMessage : JSONObject.NULL);

    // developer message
    json.put(JSON.EXCEPTION_DEVELOPER_MESSAGE.getLabel(), devMessage != null ? devMessage : JSONObject.NULL);

    return json;
  }
}
