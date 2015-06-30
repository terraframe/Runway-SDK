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
package com.runwaysdk.transport.conversion.json;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.ExceptionDTO;
import com.runwaysdk.constants.JSON;
import com.runwaysdk.constants.SmartExceptionDTOInfo;

public class SmartExceptionDTOToJSON extends LocalizableDTOToJSON
{
 
  public SmartExceptionDTOToJSON(ExceptionDTO exceptionDTO)
  {
    super(exceptionDTO);
  }

  @Override
  protected String getDTOType()
  {
    return SmartExceptionDTOInfo.CLASS;
  }
  
  @Override
  protected ExceptionDTO getComponentDTO()
  {
    return (ExceptionDTO) super.getComponentDTO();
  }

  @Override
  protected void setProperties() throws JSONException
  {
    super.setProperties();
    
    ExceptionDTO exceptionDTO = getComponentDTO();
    JSONObject json = getJSON();
    
    // In the DTO layer, the localized message and message are the same thing
    json.put(JSON.EXCEPTION_LOCALIZED_MESSAGE.getLabel(), exceptionDTO.getLocalizedMessage());
    
    // developer message
    json.put(JSON.EXCEPTION_DEVELOPER_MESSAGE.getLabel(), exceptionDTO.getDeveloperMessage());

  }
}
