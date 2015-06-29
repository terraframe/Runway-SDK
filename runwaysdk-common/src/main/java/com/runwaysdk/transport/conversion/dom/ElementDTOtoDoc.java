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

import com.runwaysdk.business.ElementDTO;

public abstract class ElementDTOtoDoc extends EntityDTOtoDoc
{
  /**
   * Constructor.
   * 
   * @param elementDTO
   * @param document
   * @param root
   * @param convertMetaData
   */
  public ElementDTOtoDoc(ElementDTO elementDTO, Document document, Node root, boolean convertMetaData)
  {
    super(elementDTO, document, root, convertMetaData);
  }

  @Override
  protected ElementDTO getComponentDTO()
  {
    return (ElementDTO) super.getComponentDTO();
  }
  
  protected void setProperties()
  {
    super.setProperties();
    
    Document document = getDocument();
    
    // create the locked by user element
    Element tempElement = document.createElement(Elements.ENTITY_LOCKED_BY_CURRENT_USER.getLabel());
    tempElement.appendChild(document.createTextNode(Boolean.toString(this.getComponentDTO().isLockedByCurrentUser())));
    this.getPropertiesNode().appendChild(tempElement);
  }
 
}
