package com.runwaysdk.mvc;

import org.json.JSONException;

public interface JsonSerializable
{
  public Object serialize(JsonSerializer serializer) throws JSONException;
}
