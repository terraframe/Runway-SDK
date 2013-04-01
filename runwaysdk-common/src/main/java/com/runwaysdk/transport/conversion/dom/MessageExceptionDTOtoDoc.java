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
package com.runwaysdk.transport.conversion.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.runwaysdk.MessageExceptionDTO;
import com.runwaysdk.business.InformationDTO;
import com.runwaysdk.business.MessageDTO;
import com.runwaysdk.business.WarningDTO;
import com.runwaysdk.transport.conversion.ConversionFacade;

public class MessageExceptionDTOtoDoc
{
  /**
   * The constructing document.
   */
  private Document document;
  
  private MessageExceptionDTO messageExceptionDTO;

  /**
   * Constructor to set the constructing document.
   * 
   * @param document
   */
  public MessageExceptionDTOtoDoc(MessageExceptionDTO messageExceptionDTO, Document document)
  {
    this.messageExceptionDTO = messageExceptionDTO;
    this.document = document;
  }
  
  /**
   * Returns the destination document.
   * 
   * @return
   */
  protected Document getDocument()
  {
    return document;
  }

  public Element populate()
  {
    Document document = getDocument();
    
    Element messageExceptionElement = document.createElement(Elements.MESSAGEEXCEPTION_DTO.getLabel());
    Element returnObjectElement = document.createElement(Elements.RETURN_OBJECT.getLabel());
    
    returnObjectElement.appendChild(ConversionFacade.getElementFromObject(document, this.messageExceptionDTO.getReturnObject(), true));
    
    messageExceptionElement.appendChild(returnObjectElement);
    
    for (MessageDTO messageDTO : this.messageExceptionDTO.getMessages())
    {
      Element messageElement = document.createElement(Elements.MESSAGE_DTO.getLabel());
      messageExceptionElement.appendChild(messageElement);

      if (messageDTO instanceof WarningDTO)
      {
        WarningDTOtoDoc warningDTOtoDoc = new WarningDTOtoDoc((WarningDTO)messageDTO, document, false);
        messageElement.appendChild(warningDTOtoDoc.populate());
      }
      else if (messageDTO instanceof InformationDTO)
      {
        InformationDTOtoDoc informationDTOtoDoc = new InformationDTOtoDoc((InformationDTO)messageDTO, document, false);
        messageElement.appendChild(informationDTOtoDoc.populate());
      }      
    }
    
    return messageExceptionElement;
  }
}
