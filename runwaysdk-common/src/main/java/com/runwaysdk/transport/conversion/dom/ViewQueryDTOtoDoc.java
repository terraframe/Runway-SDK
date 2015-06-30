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

import com.runwaysdk.business.ViewQueryDTO;

public class ViewQueryDTOtoDoc extends ClassQueryDTOtoDoc
{

  public ViewQueryDTOtoDoc(ViewQueryDTO queryDTO, Document document)
  {
    super(queryDTO, document);
  }
  
  @Override
  public Node populate()
  {
    Node root = super.populate();
    
    Document document = getDocument();
    ViewQueryDTO queryDTO = (ViewQueryDTO) getComponentQueryDTO();
    
    // isAbstract
    Element temp = document.createElement(Elements.VIEW_QUERY_DTO_ISABSTRACT.getLabel());
    temp.setTextContent(Boolean.toString(queryDTO.isAbstract()));
    root.appendChild(temp);
    
    return root;
  }
  
  /**
   * Returns the root element name.
   * @return root element name.
   */
  @Override
  protected String getRootElementName()
  {
    return Elements.VIEW_QUERY_DTO.getLabel();
  }

}
