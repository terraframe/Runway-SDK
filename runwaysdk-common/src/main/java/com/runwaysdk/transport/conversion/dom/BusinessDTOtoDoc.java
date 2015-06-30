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
package com.runwaysdk.transport.conversion.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.runwaysdk.business.BusinessDTO;

public class BusinessDTOtoDoc extends ElementDTOtoDoc
{
  /**
   * Constructor that allows a node to act as a parent to the created 
   * 
   * @param businessDTO
   * @param document
   * @param convertMetaData
   */
  public BusinessDTOtoDoc(BusinessDTO businessDTO, Document document, boolean convertMetaData)
  {
    super(businessDTO, document, document.createElement(Elements.BUSINESS_DTO.getLabel()), convertMetaData);
  }
  
  @Override
  protected BusinessDTO getComponentDTO()
  {
    return (BusinessDTO) super.getComponentDTO();
  }

  /**
   * Sets the properties specific to BusinessDTOs.
   */
  public void setProperties()
  {
    super.setProperties();
    
    Document document = getDocument();
    
    // set the state
    Element stateElement = document.createElement(Elements.BUSINESS_STATE.getLabel());
    stateElement.appendChild(document.createTextNode(this.getComponentDTO().getState()));
    this.getPropertiesNode().appendChild(stateElement);
    
    // set the transitions
    Element transitionsElement = document.createElement(Elements.BUSINESS_TRANSITIONS.getLabel());
    this.getPropertiesNode().appendChild(transitionsElement);
    
    for(String transition : this.getComponentDTO().getTransitions())
    {
      Element transitionEl = document.createElement(Elements.BUSINESS_TRANSITION.getLabel());
      transitionEl.setTextContent(transition);
      transitionsElement.appendChild(transitionEl);
    }
  }

}
