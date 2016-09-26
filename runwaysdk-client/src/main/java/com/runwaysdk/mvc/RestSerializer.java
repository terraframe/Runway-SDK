package com.runwaysdk.mvc;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.ComponentDTO;
import com.runwaysdk.mvc.conversion.ComponentDTOIFToBasicJSON;

public class RestSerializer implements JsonSerializer
{
  @Override
  public Object serialize(Object object) throws JSONException
  {
    if (object instanceof ComponentDTO)
    {
      return this.serialize((ComponentDTO) object);
    }
    else if (object instanceof JsonSerializable)
    {
      return ( (JsonSerializable) object ).serialize(this);
    }
    else
    {
      return object;
    }
  }

  private JSONObject serialize(ComponentDTO value) throws JSONException
  {
    ComponentDTOIFToBasicJSON componentDTOToJSON = ComponentDTOIFToBasicJSON.getConverter(value);
    return componentDTOToJSON.populate();
  }
}
