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

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.runwaysdk.business.RelationshipDTO;

/**
 * Converts a RelationshipDTO into a Document
 */
public class RelationshipDTOtoDoc extends ElementDTOtoDoc
{
  
  /**
   * Constructor
   * 
   * @param relationshipDTO
   * @param document
   * @param convertMetaData
   */
  public RelationshipDTOtoDoc(RelationshipDTO relationshipDTO, Document document, boolean convertMetaData)
  {
    super(relationshipDTO, document, document.createElement(Elements.RELATIONSHIP_DTO.getLabel()), convertMetaData);
  }
  
  @Override
  protected RelationshipDTO getComponentDTO()
  {
    return (RelationshipDTO) super.getComponentDTO();
  }

  /**
   * Sets the properties specific to RelationshipDTOs.
   */
  public void setProperties()
  {
    super.setProperties();
    
    RelationshipDTO relationshipDTO = this.getComponentDTO();
    Document document = getDocument();
    
    // set the parent id
    Element tempElement = document.createElement(Elements.RELATIONSHIP_PARENT_ID.getLabel());
    tempElement.appendChild(document.createTextNode(relationshipDTO.getParentId()));
    this.getPropertiesNode().appendChild(tempElement);
    
    // set the child id
    tempElement = document.createElement(Elements.RELATIONSHIP_CHILD_ID.getLabel());
    tempElement.appendChild(document.createTextNode(relationshipDTO.getChildId()));
    this.getPropertiesNode().appendChild(tempElement);
  }
  
  /**
   * Sets the TypeMd data on the Document.
   */
  @Override
  protected void setTypeMd()
  {
    RelationshipDTO relationshipDTO = this.getComponentDTO();
    Document document = getDocument();    
    
    // type metadata
    Element metadata = document.createElement(Elements.TYPE_MD.getLabel());
    this.getRoot().appendChild(metadata);
    
    // display label
    Element tempElement = document.createElement(Elements.TYPE_MD_DISPLAY_LABEL.getLabel());
    CDATASection cdata = document.createCDATASection(relationshipDTO.getMd().getDisplayLabel());
    tempElement.appendChild(cdata);
    metadata.appendChild(tempElement);
    
    // description
    tempElement = document.createElement(Elements.TYPE_MD_DESCRIPTION.getLabel());
    cdata = document.createCDATASection(relationshipDTO.getMd().getDescription());
    tempElement.appendChild(cdata);
    metadata.appendChild(tempElement);
    
    // metadata id
    tempElement = document.createElement(Elements.TYPE_MD_ID.getLabel());
    tempElement.setTextContent(relationshipDTO.getMd().getId());
    metadata.appendChild(tempElement);
    
    // parent mdbusiness
    tempElement = document.createElement(Elements.RELATIONSHIP_MD_PARENT_MD_BUSINESS.getLabel());
    tempElement.setTextContent(relationshipDTO.getMd().getParentMdBusiness());
    metadata.appendChild(tempElement);
    
    // child mdbusiness
    tempElement = document.createElement(Elements.RELATIONSHIP_MD_CHILD_MD_BUSINESS.getLabel());
    tempElement.setTextContent(relationshipDTO.getMd().getChildMdBusiness());
    metadata.appendChild(tempElement);
  }

}
