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
package com.runwaysdk.mvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.ComponentDTO;

public class RestResponse extends AbstractRestResponse implements ResponseIF
{
  private Map<String, Object> attributes;

  public RestResponse()
  {
    super(200);

    this.attributes = new HashMap<String, Object>();
  }

  public Map<String, Object> getAttributes()
  {
    return attributes;
  }

  public void set(String name, ComponentDTO dto)
  {
    this.attributes.put(name, dto);
  }

  public void set(String name, Object dto)
  {
    this.attributes.put(name, dto);
  }

  public Object getAttribute(String name)
  {
    return this.attributes.get(name);
  }

  @Override
  protected Object serialize()
  {
    try
    {
      RestSerializer serializer = new RestSerializer();

      JSONObject object = new JSONObject();

      Set<Entry<String, Object>> entries = this.attributes.entrySet();

      for (Entry<String, Object> entry : entries)
      {
        object.put(entry.getKey(), serializer.serialize(entry.getValue()));
      }

      return object;
    }
    catch (JSONException e)
    {
      throw new RuntimeException(e);
    }
  }
}
