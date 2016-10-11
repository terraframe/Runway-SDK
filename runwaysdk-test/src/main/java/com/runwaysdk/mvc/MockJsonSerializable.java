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
import org.json.JSONObject;

import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.business.RelationshipDTO;
import com.runwaysdk.constants.ClientRequestIF;

public class MockJsonSerializable implements JsonSerializable
{
  private ClientRequestIF request;

  private RelationshipDTO relationship;

  public MockJsonSerializable(ClientRequestIF request, RelationshipDTO relationship)
  {
    this.request = request;
    this.relationship = relationship;
  }

  @Override
  public Object serialize(RestSerializer serializer, JsonConfiguration configuration) throws JSONException
  {
    MutableDTO parent = this.request.get(this.relationship.getParentId());
    MutableDTO child = this.request.get(this.relationship.getChildId());

    JSONObject object = (JSONObject) serializer.serialize(this.relationship, configuration);
    object.put("parent", (JSONObject) serializer.serialize(parent, configuration));
    object.put("child", (JSONObject) serializer.serialize(child, configuration));
    
    object.remove("parentId");
    object.remove("childId");

    return object;
  }

}
