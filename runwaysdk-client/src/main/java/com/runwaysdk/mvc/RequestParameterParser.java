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

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.controller.BasicParameter;
import com.runwaysdk.controller.MultipartFileParameter;
import com.runwaysdk.controller.ParameterValue;
import com.runwaysdk.controller.RequestManager;
import com.runwaysdk.controller.ServletMethod;
import com.runwaysdk.request.ServletRequestIF;

public class RequestParameterParser
{
  private RequestManager manager;
  
  public RequestParameterParser(RequestManager manager)
  {
    this.manager = manager;
  }
  
  /**
   * Don't call this method if the request is GET (since we will be reading the request body). 
   * 
   * @param request
   * @return
   */
  @SuppressWarnings("unchecked")
  private Map<String, ParameterValue> parseBodyAsJson(ServletRequestIF request)
  {
    try
    {
      String body = IOUtils.toString(request.getReader());

      JSONObject object = new JSONObject(body);

      Map<String, ParameterValue> values = new HashMap<String, ParameterValue>();

      Iterator<String> keys = object.keys();

      while (keys.hasNext())
      {
        String key = keys.next();
        String value = object.get(key).toString();

        values.put(key, new BasicParameter(new String[] { value }));
      }

      return values;
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
    catch (JSONException e)
    {
      throw new RuntimeException(e);
    }
  }

  private Map<String, ParameterValue> convertRequestParameterMap(Map<String, String[]> parameters)
  {
    Map<String, ParameterValue> map = new HashMap<String, ParameterValue>();

    for (Entry<String, String[]> entry : parameters.entrySet())
    {
      map.put(entry.getKey(), new BasicParameter(entry.getValue()));
    }

    return map;
  }
  
  /**
   * @param annotation
   * @param req
   * @return
   * @throws FileUploadException
   */
  public Map<String, ParameterValue> getParameters(Endpoint annotation)
  {
    ServletRequestIF request = manager.getReq();

    if (request.isMultipartContent())
    {
      Map<String, ParameterValue> parameters = new HashMap<String, ParameterValue>();

      ServletFileUpload upload = this.createServletFileUpload(manager, annotation);

      try
      {
        List<FileItem> items = request.getFileItems(upload);

        for (FileItem item : items)
        {
          String fieldName = item.getFieldName();

          if (item.isFormField())
          {
            String fieldValue = item.getString();

            if (!parameters.containsKey(fieldName))
            {
              parameters.put(fieldName, new BasicParameter());
            }

            ( (BasicParameter) parameters.get(fieldName) ).add(fieldValue);
          }
          else if (!item.isFormField() && item.getSize() > 0)
          {
            parameters.put(fieldName, new MultipartFileParameter(item));
          }
        }

        return parameters;
      }
      catch (FileUploadException e)
      {
        // Change the exception type
        throw new RuntimeException(e);
      }
    }
    else
    {
      String contentType = request.getContentType();

      if (ServletMethod.POST.equals(this.manager.getMethod()) && contentType != null && contentType.contains("application/json"))
      {
        return this.parseBodyAsJson(request);
      }
      else
      {
        return this.convertRequestParameterMap(request.getParameterMap());
      }
    }
  }
  
  private ServletFileUpload createServletFileUpload(RequestManager manager, Endpoint annotation)
  {
    try
    {
      ServletFileUploadFactory factory = annotation.factory().newInstance();

      return factory.instance(manager);
    }
    catch (InstantiationException | IllegalAccessException e)
    {
      throw new RuntimeException(e);
    }
  }
}
