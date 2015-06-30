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

import com.runwaysdk.business.MessageDTO;
import com.runwaysdk.constants.JSON;

public abstract class MessageDTOToJSON extends NotificationDTOToJSON
{

  protected MessageDTOToJSON(MessageDTO messageDTO)
  {
    super(messageDTO);
  }
  
  @Override
  protected MessageDTO getComponentDTO()
  {
    return (MessageDTO) super.getComponentDTO();
  }
  
  @Override
  protected void setProperties() throws JSONException
  {
    super.setProperties();
    
    MessageDTO messageDTO = this.getComponentDTO();
    JSONObject json = this.getJSON();
    
    json.put(JSON.MESSAGE_DTO_LOCALIZED_MESSAGE.getLabel(), messageDTO.getMessage());
  }

}
