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

import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.runwaysdk.business.MethodMetaData;

public class MethodMetaDataConverter
{

  public static void getDocument(MethodMetaData metadata, Document document)
  {
    // set the MethodMetaData root element
    Element root = document.createElement(Elements.METHOD_METADATA.getLabel());

    //Add the className value to the root element
    Element classNameElement = document.createElement(Elements.METHOD_CLASS_NAME.getLabel());
    classNameElement.appendChild(document.createTextNode(metadata.getClassName()));
    root.appendChild(classNameElement);
    
    //Add the methodName value to the root element
    Element methodNameElement = document.createElement(Elements.METHOD_NAME.getLabel());
    methodNameElement.appendChild(document.createTextNode(metadata.getMethodName()));
    root.appendChild(methodNameElement);
    
    //Add the declaredTypes array to the root element
    Element declaredTypesElement = document.createElement(Elements.METHOD_DECLARE_TYPES.getLabel());
    
    for(String declaredType : metadata.getDeclaredTypes())
    {
      Element typeElement = document.createElement(Elements.METHOD_TYPE.getLabel());
      typeElement.appendChild(document.createTextNode(declaredType));

      declaredTypesElement.appendChild(typeElement);
    }  
    
    root.appendChild(declaredTypesElement);
    
    //Add the actualTypes array to the root element
    Element actualTypesElement = document.createElement(Elements.METHOD_ACTUAL_TYPES.getLabel());
    
    for(String actualType : metadata.getActualTypes())
    {
      if(actualType == null)
      {
        actualType = "null";
      }
      
      Element typeElement = document.createElement(Elements.METHOD_TYPE.getLabel());
      typeElement.appendChild(document.createTextNode(actualType));
      
      actualTypesElement.appendChild(typeElement);
    }
    
    root.appendChild(actualTypesElement);
    
    document.getDocumentElement().appendChild(root);
  }
  
  public static MethodMetaData getMethodMetaData(Document document)
  {
    // get the basic info
    Node classNameNode = document.getElementsByTagName(Elements.METHOD_CLASS_NAME.getLabel()).item(0);
    String className = classNameNode.getFirstChild().getNodeValue();
    
    Node methodNameNode = document.getElementsByTagName(Elements.METHOD_NAME.getLabel()).item(0);
    String methodName = methodNameNode.getFirstChild().getNodeValue();

    
    List<String> declaredTypes = new LinkedList<String>();    
    Node declaredTypesNode = document.getElementsByTagName(Elements.METHOD_DECLARE_TYPES.getLabel()).item(0);
    NodeList declaredTypeNodes = declaredTypesNode.getChildNodes();
    
    for(int i = 0; i < declaredTypeNodes.getLength(); i++)
    {
      String value = declaredTypeNodes.item(i).getFirstChild().getNodeValue();
      declaredTypes.add(value);
    }

    List<String> actualTypes = new LinkedList<String>();    
    Node actualTypesNode = document.getElementsByTagName(Elements.METHOD_ACTUAL_TYPES.getLabel()).item(0);
    NodeList actualTypeNodes = actualTypesNode.getChildNodes();
    
    for(int i = 0; i < actualTypeNodes.getLength(); i++)
    {
      String value = actualTypeNodes.item(i).getFirstChild().getNodeValue();
      
      if(value.equals("null"))
      {
        actualTypes.add(null);
      }
      else
      {
        actualTypes.add(value);
      }
    }

    String[] declaredArray = new String[declaredTypes.size()];
    declaredArray = declaredTypes.toArray(declaredArray);

    String[] actualArray = new String[actualTypes.size()];
    actualArray = actualTypes.toArray(actualArray);

    MethodMetaData metadata = new MethodMetaData(className, methodName, declaredArray);
    metadata.setActualTypes(actualArray);
    
    return metadata;
  }

}
