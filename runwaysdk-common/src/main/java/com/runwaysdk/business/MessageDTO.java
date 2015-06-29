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
package com.runwaysdk.business;

import java.util.Map;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.transport.attributes.AttributeDTO;

public abstract class MessageDTO extends NotificationDTO
{

  /**
   * 
   */
  private static final long serialVersionUID = 6239423320196201703L;
  
  /**
   * Constructor used when the object is instantiated on the front-end.
   */
  protected MessageDTO(ClientRequestIF clientRequest, String type)
  {
    super(clientRequest, type);
  }
  
  /**
   * Constructor used when the object is instantiated on the front-end or back-end.
   * If the clientRequest is null, then it is instantiated on the front-end, otherwise on the back-end.
   */
  protected MessageDTO(ClientRequestIF clientRequest, String type, Map<String, AttributeDTO> attributeMap)
  {
    super(clientRequest, type, attributeMap);
  }
  
  /**
   * Constructor used when the object is instantiated on the front-end.
   */
  protected MessageDTO(ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
   * Constructor used when the object is instantiated on the front-end or back-end.
   * If the clientRequest is null, then it is instantiated on the front-end, otherwise on the back-end.
   */
  protected MessageDTO(ClientRequestIF clientRequest, Map<String, AttributeDTO> attributeMap)
  {
    super(clientRequest, attributeMap);
  }

  /**
   * @return The localized end user Message.
   */
  public String getMessage()
  {
    return this.getLocalizedMessage();
  }
 
  
  /**
   * Copies over properties from the given componentDTOIF.
   * @param componentDTOIF
   */
  public void copyProperties(ComponentDTOIF componentDTOIF)
  {
    if (componentDTOIF instanceof MessageDTO)
    {
      // state
      MessageDTO messageDTO = (MessageDTO)componentDTOIF;
      this.setLocalizedMessage(messageDTO.getMessage());
    }
  }
}
