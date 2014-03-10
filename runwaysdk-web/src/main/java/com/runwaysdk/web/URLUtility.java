package com.runwaysdk.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.generation.loader.Reloadable;

public class URLUtility implements Reloadable
{
  private String baseURL;
  
  private Map<String, String> parameters;

  public URLUtility(String baseURL)
  {
    this(baseURL, new HashMap<String, String>());
  }
  
  public URLUtility(String baseURL, Map<String, String> parameters)
  {
    this.baseURL = baseURL;
    this.parameters = parameters;
  }

  public void addParameter(String key, String value)
  {
    parameters.put(key, value);
  }
  
  public String getURL()
  {
    StringBuffer buffer = new StringBuffer(baseURL);
    
    Set<String> keySet = parameters.keySet();

    if(keySet.size() > 0)
    {      
      buffer.append("?");
      
      String[] keys = keySet.toArray(new String[keySet.size()]);
      
      for(int i = 0; i < keys.length; i++)
      {
        if(i != 0)
        {
          buffer.append("&");
        }
        
        buffer.append(keys[i] + "=" + parameters.get(keys[i]));
      }
    }
    
    return buffer.toString();
  }
}
