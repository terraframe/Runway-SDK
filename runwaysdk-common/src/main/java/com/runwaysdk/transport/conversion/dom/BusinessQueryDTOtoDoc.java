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

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.runwaysdk.business.BusinessQueryDTO;
import com.runwaysdk.business.BusinessQueryDTO.TypeInMdRelationshipAsChild;
import com.runwaysdk.business.BusinessQueryDTO.TypeInMdRelationshipAsParent;

public class BusinessQueryDTOtoDoc extends ElementQueryDTOtoDoc
{

  public BusinessQueryDTOtoDoc(BusinessQueryDTO queryDTO, Document document)
  {
    super(queryDTO, document);
  }
  
  @Override
  public Node populate()
  {
    Node root = super.populate();
    Document document = getDocument();
    
    // grab all information where the type defined by this QueryDTO participates as either a parent
    // or child in an MdRelationship.
    
    // as parent
    Element tempElement = document.createElement(Elements.BUSINESS_QUERY_DTO_TYPE_IN_REL_AS_PARENT.getLabel());
    root.appendChild(tempElement);
    
    BusinessQueryDTO queryDTO = (BusinessQueryDTO) getComponentQueryDTO();
    List<TypeInMdRelationshipAsParent> asParents =  queryDTO.getTypeInMdRelationshipAsParentList();
    for(TypeInMdRelationshipAsParent asParent : asParents)
    {
      Element instance = document.createElement(Elements.BUSINESS_QUERY_DTO_TYPE_IN_REL_INSTANCE.getLabel());
      
      Element parentDisplayLabel = document.createElement(Elements.BUSINESS_QUERY_DTO_TYPE_IN_REL_PARENT_DISPLAY_LABEL.getLabel());
      parentDisplayLabel.setTextContent(asParent.getParentDisplayLabel());
      
      Element relationshipType = document.createElement(Elements.BUSINESS_QUERY_DTO_TYPE_IN_RELATIONSHIP_TYPE.getLabel());
      relationshipType.setTextContent(asParent.getRelationshipType());
      
      // add the data to the instance
      instance.appendChild(parentDisplayLabel);
      instance.appendChild(relationshipType);
      
      tempElement.appendChild(instance);
    }

    
    // as child
    tempElement = document.createElement(Elements.BUSINESS_QUERY_DTO_TYPE_IN_REL_AS_CHILD.getLabel());
    root.appendChild(tempElement);
    
    queryDTO.getTypeInMdRelationshipAsChildList();
    List<TypeInMdRelationshipAsChild> asChilds = queryDTO.getTypeInMdRelationshipAsChildList();
    for(TypeInMdRelationshipAsChild asChild : asChilds)
    {
      Element instance = document.createElement(Elements.BUSINESS_QUERY_DTO_TYPE_IN_REL_INSTANCE.getLabel());
      
      Element childDisplayLabel = document.createElement(Elements.BUSINESS_QUERY_DTO_TYPE_IN_REL_CHILD_DISPLAY_LABEL.getLabel());
      childDisplayLabel.setTextContent(asChild.getChildDisplayLabel());
      
      Element relationshipType = document.createElement(Elements.BUSINESS_QUERY_DTO_TYPE_IN_RELATIONSHIP_TYPE.getLabel());
      relationshipType.setTextContent(asChild.getRelationshipType());
      
      // add the data to the instance
      instance.appendChild(childDisplayLabel);
      instance.appendChild(relationshipType);
      
      tempElement.appendChild(instance);
    }   

    
    return root;
  }
  
  /**
   * Returns the root element name.
   * @return root element name.
   */
  @Override
  protected String getRootElementName()
  {
    return Elements.BUSINESS_QUERY_DTO.getLabel();
  }
}
