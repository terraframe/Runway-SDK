/**
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
 */
package com.runwaysdk.web.json;

import org.json.JSONObject;

import com.runwaysdk.ClientException;
import com.runwaysdk.RunwayExceptionDTO;
import com.runwaysdk.business.SmartExceptionDTO;
import com.runwaysdk.transport.conversion.json.JSONExceptionDTO;
import com.runwaysdk.transport.conversion.json.RunwayExceptionDTOToJSON;

public class JSONRunwayExceptionDTO extends JSONExceptionDTO
{

  /**
   *
   */
  private static final long serialVersionUID = 2087901187339558079L;

  public JSONRunwayExceptionDTO(RunwayExceptionDTO e)
  {
    String exceptionName = e.getType();
    String devMessage = e.getDeveloperMessage();
    String localizedMessage = e.getLocalizedMessage();

    convert(exceptionName, devMessage, localizedMessage);
  }

  public JSONRunwayExceptionDTO(Throwable t)
  {
    String exceptionName = t.getClass().getName();
    String devMessage = t.getMessage();

    String localizedMessage;
    if(t instanceof SmartExceptionDTO)
    {
      localizedMessage = ((SmartExceptionDTO)t).getLocalizedMessage();
    }
    else if(t instanceof RunwayExceptionDTO)
    {
      localizedMessage = ((RunwayExceptionDTO)t).getLocalizedMessage();
    }
    else
    {
      // use ApplicationException to get the default localized message if
      // this is not a SmartException.
      ClientException ex = new ClientException(t);
      localizedMessage = ex.getLocalizedMessage();
    }

    convert(exceptionName, devMessage, localizedMessage);
  }

  public JSONRunwayExceptionDTO(String exceptionName, String devMessage, String localizedMessage)
  {
    super();

    convert(exceptionName, devMessage, localizedMessage);
  }

  /**
   * Converts the parameters into JSON parameters.
   *
   * @param exceptionName
   * @param devMessage
   * @param localizedMessage
   */
  private void convert(String exceptionName, String devMessage, String localizedMessage)
  {
    try
    {
      RunwayExceptionDTOToJSON converter = new RunwayExceptionDTOToJSON(exceptionName, devMessage,
          localizedMessage);
      JSONObject json = converter.populate();

      setJSON(json.toString());
    }
    catch (Throwable t)
    {
      throw new ClientException(t);
    }
  }

}
