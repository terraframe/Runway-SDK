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
package com.runwaysdk.transport.conversion.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.runwaysdk.business.ExceptionDTO;
import com.runwaysdk.business.SmartExceptionDTO;

public class SmartExceptionDTOtoDoc extends LocalizableDTOtoDoc
{
  /**
   * Constructor that allows a node to act as a parent to the created 
   * 
   * @param smartExceptionDTO
   * @param document
   * @param convertMetaData
   */
  public SmartExceptionDTOtoDoc(SmartExceptionDTO smartExceptionDTO, Document document, boolean convertMetaData)
  {
    super(SmartExceptionDTO.getExceptionDTO((SmartExceptionDTO) smartExceptionDTO), document, document.createElement(Elements.EXCEPTION_DTO.getLabel()), convertMetaData);
  }

  /**
   * Constructor that allows a node to act as a parent to the created 
   * 
   * @param exceptionDTO
   * @param document
   * @param convertMetaData
   */
  public SmartExceptionDTOtoDoc(ExceptionDTO exceptionDTO, Document document, boolean convertMetaData)
  {
    super(exceptionDTO, document, document.createElement(Elements.EXCEPTION_DTO.getLabel()), convertMetaData);
  }
  
  @Override
  protected ExceptionDTO getComponentDTO()
  {
    return (ExceptionDTO) super.getComponentDTO();
  }
  
  /**
   * Populates the DOM and returns the root node.
   * 
   * @return
   */
  @Override
  public Node populate()
  { 
    Node node = super.populate();// add the TypeMd

    Document document = getDocument();
    
    // create the new instance element.
    
    Element localizedMessage = document.createElement(Elements.EXCEPTION_LOCALIZED_MESSAGE.getLabel());
    localizedMessage.appendChild(document.createTextNode(this.getComponentDTO().getLocalizedMessage()));
    this.getRoot().appendChild(localizedMessage);
    
    Element devMessage = document.createElement(Elements.EXCEPTION_DEVELOPER_MESSAGE.getLabel());
    devMessage.appendChild(document.createTextNode(this.getComponentDTO().getDeveloperMessage()));
    this.getRoot().appendChild(devMessage);
    
    return node;
  }
}
