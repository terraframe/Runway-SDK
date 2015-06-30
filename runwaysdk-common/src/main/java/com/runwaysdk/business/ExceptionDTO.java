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
package com.runwaysdk.business;

import java.util.Map;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.transport.attributes.AttributeDTO;

public class ExceptionDTO extends LocalizableDTO implements SmartExceptionDTOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = -9215055088971796163L;

  /**
   * Constructor used when the object is instantiated on the front-end.
   */
  protected ExceptionDTO(ClientRequestIF clientRequest, String type)
  {
    super(clientRequest, type);
  }
  
  /**
   * Constructor used when the object is instantiated on the front-end or back-end.
   * If the clientRequest is null, then it is instantiated on the front-end, otherwise on the back-end.
   */
  protected ExceptionDTO(ClientRequestIF clientRequest, String type, Map<String, AttributeDTO> attributeMap)
  {
    super(clientRequest, type, attributeMap);
  }
  
  /**
   * Constructor used when the object is instantiated on the front-end.
   */
  protected ExceptionDTO(ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
   * Constructor used when the object is instantiated on the front-end or back-end.
   * If the clientRequest is null, then it is instantiated on the front-end, otherwise on the back-end.
   */
  protected ExceptionDTO(ClientRequestIF clientRequest, Map<String, AttributeDTO> attributeMap)
  {
    super(clientRequest, attributeMap);
  }

  @Override
  protected String getDeclaredType()
  {
    return ExceptionDTO.class.getName();
  }

  /**
   * Copies over properties from the given componentDTO.
   * @param componentDTOIF
   */
  public void copyProperties(ComponentDTOIF componentDTOIF)
  {  
    super.copyProperties(componentDTOIF);
    
    ExceptionDTO exceptionDTO = null;
    
    if (componentDTOIF instanceof ExceptionDTO)
    {
      exceptionDTO = (ExceptionDTO)componentDTOIF;
    }
    else
    {
      exceptionDTO = ((SmartExceptionDTO)componentDTOIF).getExceptionDTO();
    }

    this.setLocalizedMessage(exceptionDTO.getLocalizedMessage());
    this.setDeveloperMessage(exceptionDTO.getDeveloperMessage());
  }
}
