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

import org.json.JSONException;

public class RestBodyResponse extends AbstractRestResponse implements ResponseIF
{
  private Object object;

  public RestBodyResponse(Object object)
  {
    super(200);
    
    this.object = object;
  }

  @Override
  public Object serialize()
  {
    try
    {
      RestSerializer serializer = new RestSerializer();

      return serializer.serialize(this.object);
    }
    catch (JSONException e)
    {
      throw new RuntimeException(e);
    }
  }
}
