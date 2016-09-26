package com.runwaysdk.mvc;

import org.json.JSONException;


public interface JsonSerializer
{
  public Object serialize(Object object) throws JSONException;
}
