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
import org.w3c.dom.Node;

import com.runwaysdk.business.RelationshipQueryDTO;

public class RelationshipQueryDTOtoDoc extends ElementQueryDTOtoDoc
{

  public RelationshipQueryDTOtoDoc(RelationshipQueryDTO queryDTO, Document document)
  {
    super(queryDTO, document);
  }
  
  @Override
  public Node populate()
  {
    Node root = super.populate();
    
    Document document = getDocument();
    RelationshipQueryDTO queryDTO = (RelationshipQueryDTO) getComponentQueryDTO();
    
    // parentMdBusiness
    Element temp = document.createElement(Elements.RELATIONSHIP_QUERY_DTO_PARENT_MD_BUSINESS.getLabel());
    temp.setTextContent(queryDTO.getParentMdBusiness());
    root.appendChild(temp);
    
    // childMdBusiness
    temp = document.createElement(Elements.RELATIONSHIP_QUERY_DTO_CHILD_MD_BUSINESS.getLabel());
    temp.setTextContent(queryDTO.getChildMdBusiness());
    root.appendChild(temp);
    
    return root;
  }

  /**
   * Returns the root element name.
   * @return root element name.
   */
  protected String getRootElementName()
  {
    return Elements.RELATIONSHIP_QUERY_DTO.getLabel();
  }
  
}
