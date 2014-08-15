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
package com.runwaysdk.transport.conversion.json;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.MessageExceptionDTO;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.InformationDTO;
import com.runwaysdk.business.ViewDTO;
import com.runwaysdk.business.WarningDTO;
import com.runwaysdk.constants.ExceptionConstants;

public class JSONReturnObject
{
  /**
   * Key name for the return value of the call.
   */
  public static final String RETURN_VALUE = "returnValue";

  /**
   * Key name for list of warnings resulting from the call.
   */
  public static final String WARNINGS     = "warnings";

  /**
   * Key name for the list of information resulting from the call.
   */
  public static final String INFORMATION  = "information";

  /**
   * The main container.
   */
  private JSONObject         returnObject;

  /**
   * Creates a new JSONReturnObject to represent a return AJAX call.
   *
   * A return value is not defined by default. warning/information entries
   * contain empty lists.
   */
  public JSONReturnObject()
  {
    returnObject = new JSONObject();

    try
    {
      returnObject.put(WARNINGS, new JSONArray());
      returnObject.put(INFORMATION, new JSONArray());
    }
    catch (JSONException e)
    {
      String error = "The return JSON object could not be constructed.";
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), error, e);
    }
  }

  /**
   * Creates a new JSONReturnObject with the given return value and an empty
   * list for warnings/information.
   */
  public JSONReturnObject(Object returnValue)
  {
    returnObject = new JSONObject();

    try
    {
      setReturnValue(returnValue);

      returnObject.put(WARNINGS, new JSONArray());
      returnObject.put(INFORMATION, new JSONArray());
    }
    catch (JSONException e)
    {
      String error = "The return JSON object could not be constructed.";
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), error, e);
    }

  }

  /**
   * Sets the value for the return json.
   *
   * @param jsonObject
   */
  public void setReturnValue(Object value)
  {
    try
    {
      if (value == null)
      {
        returnObject.put(RETURN_VALUE, JSONObject.NULL);
      }
      else if (value instanceof ViewDTO) { // TODO : Implement the ability for controllers to return serialized objects.
        ViewDTOToJSON converter = new ViewDTOToJSON((ViewDTO) value);
        JSONObject converted = converter.populate();
        returnObject.put(RETURN_VALUE, converted);
      }
      else if (value instanceof BusinessDTO) { // TODO : Implement the ability for controllers to return serialized objects.
        BusinessDTOToJSON converter = new BusinessDTOToJSON((BusinessDTO) value);
        JSONObject converted = converter.populate();
        returnObject.put(RETURN_VALUE, converted);
      }
      else
      {
        returnObject.put(RETURN_VALUE, value);
      }
    }
    catch (JSONException e)
    {
      String error = "The return type and value could not be added to the JSON object.";
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), error, e);
    }
  }

  /**
   * Sets all the warnings on the return JSON object.
   *
   * @param warnings
   */
  public void setWarnings(List<WarningDTO> warnings)
  {
    try
    {
      JSONArray warningsArr = new JSONArray();

      for (WarningDTO warningDTO : warnings)
      {
        WarningDTOToJSON converter = new WarningDTOToJSON(warningDTO);
        JSONObject json = converter.populate();
        warningsArr.put(json);
      }

      returnObject.put(WARNINGS, warningsArr);
    }
    catch (JSONException e)
    {
      String error = "Could not add messages to the return JSON object.";
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), error, e);
    }
  }

  /**
   * Sets all the information on the return JSON object.
   *
   * @param information
   */
  public void setInformation(List<InformationDTO> information)
  {
    try
    {
      JSONArray informationArr = new JSONArray();

      for (InformationDTO informationDTO : information)
      {
        InformationDTOToJSON converter = new InformationDTOToJSON(informationDTO);
        JSONObject json = converter.populate();
        informationArr.put(json);
      }

      returnObject.put(INFORMATION, informationArr);
    }
    catch (JSONException e)
    {
      String error = "Could not add messages to the return JSON object.";
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), error, e);
    }
  }

  /**
   * Extracts all messages (warnings and information) from the given
   * {@link MessageExceptionDTO} and adds them to this return JSON object.
   *
   * @param me
   */
  public void extractMessages(MessageExceptionDTO me)
  {
    setWarnings(me.getWarnings());
    setInformation(me.getInformation());
  }

  /**
   * Returns the internal JSONObject.
   *
   * @return
   */
  public JSONObject getJSON()
  {
    return returnObject;
  }

  /**
   * Returns the JSON return object as a string in JSON format. This is the one
   * and only method that should be called to get the JSON object as a string.
   *
   * @return The JSON object as a string
   */
  public String toString()
  {
    return returnObject.toString();
  }
}
