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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.ComponentDTO;
import com.runwaysdk.business.ComponentQueryDTO;
import com.runwaysdk.mvc.conversion.ComponentDTOIFToBasicJSON;
import com.runwaysdk.mvc.conversion.ComponentQueryDTOToBasicJSON;

public class RestSerializer implements JsonSerializer
{
  @Override
  public Object serialize(Object object, JsonConfiguration configuration) throws JSONException
  {
    if (object == null)
    {
      return null;
    }
    
    if (object instanceof ComponentDTO)
    {
      return this.serialize((ComponentDTO) object, configuration);
    }
    else if (object instanceof ComponentQueryDTO)
    {
      return this.serialize((ComponentQueryDTO) object, configuration);
    }
    else if (object instanceof JsonSerializable)
    {
      return ( (JsonSerializable) object ).serialize(this, configuration);
    }
    else if (object.getClass().isArray())
    {
      JSONArray serialized = new JSONArray();
      Object[] array = (Object[]) object;

      for (Object obj : array)
      {
        Object value = this.serialize(obj, configuration);
        serialized.put(value);
      }

      return serialized;
    }
    else
    {
      return object;
    }
  }

  private JSONObject serialize(ComponentDTO value, JsonConfiguration configuration) throws JSONException
  {
    ComponentDTOIFToBasicJSON componentDTOToJSON = ComponentDTOIFToBasicJSON.getConverter(value, configuration);
    return componentDTOToJSON.populate();
  }

  private JSONObject serialize(ComponentQueryDTO value, JsonConfiguration configuration) throws JSONException
  {
    ComponentQueryDTOToBasicJSON componentDTOToJSON = ComponentQueryDTOToBasicJSON.getConverter(value, configuration);
    return componentDTOToJSON.populate();
  }
}
