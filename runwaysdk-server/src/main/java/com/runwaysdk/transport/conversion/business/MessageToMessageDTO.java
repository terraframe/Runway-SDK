/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.transport.conversion.business;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.Message;
import com.runwaysdk.business.MessageDTO;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.transport.attributes.AttributeEnumerationDTO;

public abstract class MessageToMessageDTO extends NotificationToNotificationDTO
{

  /**
   * Constructor to use when the Session parameter is to be converted into a
   * DTO. 
   * 
   * @param sessionId
   * @param message
   * @param convertMetaData
   */
  public MessageToMessageDTO(String sessionId, Message message, boolean convertMetaData)
  {
    super(sessionId, message, convertMetaData);
  }  

  /**
   * Returns the component that is being converted into a DTO.
   * @return component that is being converted into a DTO.
   */
  protected Message getComponentIF()
  {
    return (Message)super.getComponentIF();
  }

  /**
   * Creates and populates an MessageDTO based on the provided ComponentIF
   * when this object was constructed. The created MessageDTO is returned.
   * 
   * @return
   */
  public MessageDTO populate()
  {
    MessageDTO populate = (MessageDTO)super.populate();
    populate.setLocalizedMessage(this.getComponentIF().getLocalizedMessage());
    return populate;
  }

  @Override
  protected void setAttributeEnumerationNames(MdAttributeDAOIF mdAttributeIF, AttributeEnumerationDTO attributeEnumerationDTO)
  {
    BusinessFacade.getAttributeEnumerationNames(this.getComponentIF(), mdAttributeIF.definesAttribute(), attributeEnumerationDTO);
  }

}
