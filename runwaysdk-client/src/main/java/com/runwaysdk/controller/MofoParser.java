/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
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
 ******************************************************************************/
package com.runwaysdk.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.MdWebFieldInfo;
import com.runwaysdk.form.web.JSONWebFieldConstants;
import com.runwaysdk.form.web.WebFormObject;
import com.runwaysdk.form.web.field.WebField;
import com.runwaysdk.system.metadata.MdFormDTO;
import com.runwaysdk.transport.conversion.ConversionExceptionDTO;

public class MofoParser implements JSONWebFieldConstants
{
  private ClientRequestIF request;

  private String          mofoString;

  private JSONObject      json;

  public MofoParser(ClientRequestIF clientRequestIF, HttpServletRequest req)
  {
    this.request = clientRequestIF;
    mofoString = req.getParameter(ServletDispatcher.MOFO_OBJECT);
  }

  public MofoParser(ClientRequestIF clientRequestIF, String mofoString)
  {
    this.request = clientRequestIF;
    this.mofoString = mofoString;
  }

  /**
   * Constructs and returns a FormObject based on the request.
   * 
   * @return
   */
  public WebFormObject getFormObject()
  {
    try
    {
      this.json = new JSONObject(mofoString).getJSONObject(FORM_OBJECT);
      boolean isNew = this.json.getBoolean(NEW_INSTANCE);
      String dataId = this.json.getString(DATA_ID);
      JSONObject formMd = this.json.getJSONObject(FORM_MD);

      WebFormObject formObject;
      String mdFormId = formMd.getString(ID);

      MdFormDTO mdFormDTO = MdFormDTO.get(this.request, mdFormId);

      if (isNew)
      {
        formObject = WebFormObject.newInstance(mdFormDTO);
      }
      else
      {
        formObject = WebFormObject.getInstance(mdFormDTO, dataId);
      }

      this.copyAttributeValues(formObject);

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
  private void copyAttributeValues(WebFormObject formObject) throws JSONException
  {
    Map<String, ? extends WebField> fieldMap = formObject.getFieldMap();
    JSONArray fieldsArr = this.json.getJSONArray(FIELDS);
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
