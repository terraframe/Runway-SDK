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
package com.runwaysdk.web.json;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.ExceptionDTO;
import com.runwaysdk.business.SmartExceptionDTO;
import com.runwaysdk.transport.conversion.json.JSONExceptionDTO;
import com.runwaysdk.transport.conversion.json.SmartExceptionDTOToJSON;

/**
 * Class that wraps
 */
public class JSONSmartExceptionDTO extends JSONExceptionDTO
{
  /**
   * 
   */
  private static final long serialVersionUID = -2363137717705367907L;

  /**
   * 
   * @param json
   */
  public JSONSmartExceptionDTO(SmartExceptionDTO e)
  {
    super();

    ExceptionDTO exceptionDTO = SmartExceptionDTO.getExceptionDTO((SmartExceptionDTO) e);
    SmartExceptionDTOToJSON converter = new SmartExceptionDTOToJSON(exceptionDTO);
    
    try
    {
      JSONObject json = converter.populate();
      setJSON(json.toString());
    }
    catch (JSONException e1)
    {
      throw new JSONRunwayExceptionDTO(e1);
    }
  }

}
