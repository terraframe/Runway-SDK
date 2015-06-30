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

import com.runwaysdk.business.EnumDTO;
import com.runwaysdk.constants.ClientRequestIF;

public class EnumDTOConverter
{ 
  public static Element getDocument(EnumDTO enumDTO, Document document, boolean convertMetaData)
  {
    // set the MethodMetaData root element
    Element root = document.createElement(Elements.ENUM_DTO.getLabel());

    //Add the className value to the root element
    Element enumNameElement = document.createElement(Elements.ENUM_DTO_NAME.getLabel());
    enumNameElement.appendChild(document.createTextNode(enumDTO.getEnumName()));
    root.appendChild(enumNameElement);
    
    //Add the methodName value to the root element
    Element enumTypeElement = document.createElement(Elements.ENUM_DTO_TYPE.getLabel());
    enumTypeElement.appendChild(document.createTextNode(enumDTO.getEnumType()));
    root.appendChild(enumTypeElement);
        
    return root;
  }

  public static EnumDTO getEnumDTO(ClientRequestIF clientRequest, Node parentNode)
  {    
    Element root = (Element) parentNode;
    
    Node enumNameNode = root.getElementsByTagName(Elements.ENUM_DTO_NAME.getLabel()).item(0);
    String enumName = enumNameNode.getFirstChild().getNodeValue();
    
    Node enumTypeNode = root.getElementsByTagName(Elements.ENUM_DTO_TYPE.getLabel()).item(0);
    String enumType = enumTypeNode.getFirstChild().getNodeValue();
    
    return new EnumDTO(enumType, enumName);
  }

}
