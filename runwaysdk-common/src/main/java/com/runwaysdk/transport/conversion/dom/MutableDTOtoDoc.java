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
import org.w3c.dom.Node;

import com.runwaysdk.business.MutableDTO;

public abstract class MutableDTOtoDoc extends ComponentDTOIFtoDoc
{
  /**
   * Constructor.
   * 
   * @param mutableDTO
   * @param document
   * @param root
   * @param convertMetaData
   */
  public MutableDTOtoDoc(MutableDTO mutableDTO, Document document, Node root, boolean convertMetaData)
  {
    super(mutableDTO, document, root, convertMetaData);
  }

  /**
   * Returns the componentDTO being converted to a document.
   * @return componentDTO being converted to a document.
   */
  protected MutableDTO getComponentDTO()
  {
    return (MutableDTO)super.getComponentDTO();
  }
  
  
  /**
   * Sets the MutableDTO information to the document that all EntityDTOs share.
   */
  protected void setProperties()
  {
    super.setProperties();

    Document document = getDocument();
    
    // create the new instance element.
    Element tempElement = document.createElement(Elements.COMPONENT_NEW_INSTANCE.getLabel());
    tempElement.appendChild(document.createTextNode(Boolean.toString(this.getComponentDTO().isNewInstance())));
    this.getPropertiesNode().appendChild(tempElement);

    // create the writable element
    tempElement = document.createElement(Elements.COMPONENT_WRITABLE.getLabel());
    tempElement.appendChild(document.createTextNode(Boolean.toString(this.getComponentDTO().isWritable())));
    this.getPropertiesNode().appendChild(tempElement);
    
    // create the modified element.
    tempElement = document.createElement(Elements.COMPONENT_MODIFIED.getLabel());

    tempElement.appendChild(document.createTextNode(Boolean.toString(this.getComponentDTO().isModified())));
    this.getPropertiesNode().appendChild(tempElement);
    
    // add the toString value
    tempElement = document.createElement(Elements.MUTABLE_DTO_TOSTRING.getLabel());
    tempElement.appendChild(document.createTextNode(this.getComponentDTO().toString()));
    this.getPropertiesNode().appendChild(tempElement);
  }
}
