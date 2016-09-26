package com.runwaysdk.mvc;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.business.RelationshipDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.mvc.JsonSerializable;
import com.runwaysdk.mvc.JsonSerializer;

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
  public Object serialize(JsonSerializer serializer) throws JSONException
  {
    MutableDTO parent = this.request.get(this.relationship.getParentId());
    MutableDTO child = this.request.get(this.relationship.getChildId());

    JSONObject object = (JSONObject) serializer.serialize(this.relationship);
    object.put("parent", (JSONObject) serializer.serialize(parent));
    object.put("child", (JSONObject) serializer.serialize(child));
    object.remove("parentId");
    object.remove("childId");

    return object;
  }

}
