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
