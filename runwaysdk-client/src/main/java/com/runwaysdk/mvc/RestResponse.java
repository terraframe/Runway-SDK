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
    super();

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
