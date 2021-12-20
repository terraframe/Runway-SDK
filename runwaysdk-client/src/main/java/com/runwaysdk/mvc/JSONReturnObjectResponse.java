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
package com.runwaysdk.mvc;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.transport.conversion.json.ComponentDTOIFToJSON;
import com.runwaysdk.transport.conversion.json.JSONReturnObject;

public class JSONReturnObjectResponse extends AbstractRestResponse implements ResponseIF
{
  private ComponentDTOIF component;

  public JSONReturnObjectResponse(ComponentDTOIF component)
  {
    super(200);

    this.component = component;
  }

  @Override
  protected Object serialize()
  {
    try
    {
      ComponentDTOIFToJSON converter = ComponentDTOIFToJSON.getConverter(this.component);
      JSONObject object = converter.populate();
      JSONReturnObject returnObject = new JSONReturnObject(object);

      return returnObject.getJSON();
    }
    catch (JSONException e)
    {
      throw new RuntimeException(e);
    }
  }

}
