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
package com.runwaysdk.business;

import java.util.Locale;
import java.util.Map;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.ProblemDTOInfo;
import com.runwaysdk.transport.attributes.AttributeDTO;

public class ProblemDTO extends NotificationDTO implements ProblemDTOIF
{
  
  public static final String CLASS = ProblemDTOInfo.CLASS;
  
  /**
   * 
   */
  private static final long serialVersionUID = 6896940060310386857L;
  
  private Locale locale = null;
  
  /**
   * Constructor used when the object is instantiated on the front-end.
   */
  protected ProblemDTO(ClientRequestIF clientRequest, String type)
  {
    super(clientRequest, type);
  }
  
  /**
   * Constructor used when the object is instantiated on the front-end or back-end.
   * If the clientRequest is null, then it is instantiated on the front-end, otherwise on the back-end.
   */
  protected ProblemDTO(ClientRequestIF clientRequest, String type, Map<String, AttributeDTO> attributeMap)
  {
    super(clientRequest, type, attributeMap);
  }
  
  /**
   * Constructor used when the object is instantiated on the front-end or back-end.
   * If the clientRequest is null, then it is instantiated on the front-end, otherwise on the back-end.
   */
  protected ProblemDTO(ClientRequestIF clientRequest, Map<String, AttributeDTO> attributeMap)
  {
    super(clientRequest, attributeMap);
  }

  /**
   * Constructor used when the object is instantiated on the front-end.
   */
  protected ProblemDTO(ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
   * Constructor used when the object is instantiated on the back-end.
   */
  protected ProblemDTO(ClientRequestIF clientRequest, Locale locale)
  {
    super(clientRequest);
    this.locale = locale;

    ProblemDTO dto = (ProblemDTO)clientRequest.newGenericMutable(this.getDeclaredType());
    // Not bothering to clone the map, since the source dto is gargage collected anyway.
    this.copyProperties(dto, dto.attributeMap);
  }
  
  @Override
  protected String getDeclaredType()
  {
    return ProblemDTO.class.getName();
  }
  
  /**
   * Copies over properties from the given componentDTOIF.
   * @param componentDTOIF
   */
  public void copyProperties(ComponentDTOIF componentDTOIF)
  {
    super.copyProperties(componentDTOIF);
    
    if (componentDTOIF instanceof ProblemDTO)
    {
      // state
      ProblemDTO problemDTO = (ProblemDTO)componentDTOIF;
      this.setLocalizedMessage(problemDTO.getMessage());
      this.setDeveloperMessage(problemDTO.getDeveloperMessage());
    }
  }

  /**
   * Returns the locale of this exception, or null if this exception
   * was thrown from the server.
   * @return locale of this exception, or null if this exception
   * was thrown from the server.
   */
  protected Locale getLocale()
  {
    return this.locale;
  }
}
