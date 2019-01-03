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
package com.runwaysdk.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.constants.FormObjectInfo;
import com.runwaysdk.constants.MdWebFieldInfo;
import com.runwaysdk.form.web.JSONWebFieldConstants;
import com.runwaysdk.form.web.WebFormObject;
import com.runwaysdk.form.web.field.WebField;
import com.runwaysdk.system.metadata.MdFormDTO;
import com.runwaysdk.transport.conversion.ConversionExceptionDTO;

public class MofoParser implements JSONWebFieldConstants
{
  private RequestManager   manager;

  private ActionParameters annotation;

  private JSONObject       mofoObject;

  public MofoParser(RequestManager manager, ActionParameters annotation, HttpServletRequest req)
  {
    this(manager, annotation, req.getParameter(ServletDispatcher.MOFO_OBJECT));
  }

  public MofoParser(RequestManager manager, ActionParameters annotation, String mofoString)
  {
    try
    {
      this.manager = manager;
      this.annotation = annotation;
      this.mofoObject = new JSONObject(mofoString);
    }
    catch (JSONException e)
    {
      String msg = "Could not convert a FormObject string into an object.";
      throw new ConversionExceptionDTO(msg, e);
    }
  }

  public Object[] getObjects()
  {
    StringTokenizer toke = new StringTokenizer(this.annotation.parameters(), ",");
    List<Object> objects = new LinkedList<Object>();

    // Load objects from the parameters list
    while (toke.hasMoreTokens())
    {
      String parameter = toke.nextToken();
      String[] array = parameter.split(":");

      String type = array[0].trim();
      String parameterName = array[1].trim();

      objects.add(this.getObject(type, parameterName));
    }

    return objects.toArray(new Object[objects.size()]);
  }

  private Object getObject(String type, String parameterName)
  {
    try
    {
      if (mofoObject.isNull(parameterName))
      {
        return null;
      }
      else if (type.equals(FormObjectInfo.CLASS))
      {
        return this.getFormObject(parameterName);
      }
      else
      {
        return mofoObject.get(parameterName);
      }
    }
    catch (JSONException e)
    {
      String msg = "Could not convert a FormObject string into an object.";
      throw new ConversionExceptionDTO(msg, e);
    }
  }

  /**
   * Constructs and returns a FormObject based on the request.
   * 
   * @return
   */
  private WebFormObject getFormObject(String parameterName)
  {
    try
    {
      JSONObject json = mofoObject.getJSONObject(parameterName);
      boolean isNew = json.getBoolean(NEW_INSTANCE);
      String dataId = json.getString(DATA_ID);
      JSONObject formMd = json.getJSONObject(FORM_MD);

      WebFormObject formObject;
      String mdFormId = formMd.getString(OID);

      MdFormDTO mdFormDTO = MdFormDTO.get(manager.getClientRequest(), mdFormId);

      if (isNew)
      {
        if (!json.isNull(DISCONNECTED) && json.getBoolean(DISCONNECTED))
        {
          formObject = WebFormObject.newDisconnectedInstance(mdFormDTO);
        }
        else
        {
          formObject = WebFormObject.newInstance(mdFormDTO);
        }
      }
      else
      {
        formObject = WebFormObject.getInstance(mdFormDTO, dataId);
      }

      this.copyAttributeValues(json, formObject);

      return formObject;
    }
    catch (JSONException e)
    {
      String msg = "Could not convert a FormObject string into an object.";
      throw new ConversionExceptionDTO(msg, e);
    }
  }

  /**
   * Copies the values from the JSONObject to the FormObject.
   * 
   * @param formObject
   * @throws JSONException
   */
  private void copyAttributeValues(JSONObject json, WebFormObject formObject) throws JSONException
  {
    Map<String, ? extends WebField> fieldMap = formObject.getFieldMap();
    JSONArray fieldsArr = json.getJSONArray(FIELDS);
    for (int i = 0; i < fieldsArr.length(); i++)
    {
      // source field
      JSONObject fieldObj = fieldsArr.getJSONObject(i);
      JSONObject fieldMdObj = fieldObj.getJSONObject(FIELD_MD);
      String fieldName = fieldMdObj.getString(MdWebFieldInfo.FIELD_NAME);

      // destination field
      WebField destField = fieldMap.get(fieldName);

      if (fieldObj.has(VALUE))
      {
        Object value = fieldObj.get(VALUE);

        if (JSONObject.NULL.equals(value))
        {
          destField.setValue(null);
        }
        else
        {
          destField.setValue(value.toString());
        }
      }
      else
      {
        destField.setValue(null);
      }
    }
  }
}
