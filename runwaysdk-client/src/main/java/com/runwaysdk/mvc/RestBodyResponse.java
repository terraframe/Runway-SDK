package com.runwaysdk.mvc;

import org.json.JSONException;

public class RestBodyResponse extends AbstractRestResponse implements ResponseIF
{
  private Object object;

  public RestBodyResponse(Object object)
  {
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
