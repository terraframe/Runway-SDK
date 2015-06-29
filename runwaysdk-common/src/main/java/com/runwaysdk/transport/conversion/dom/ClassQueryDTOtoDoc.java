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

import java.util.List;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.runwaysdk.business.ClassQueryDTO;
import com.runwaysdk.business.ClassQueryDTO.QueryCondition;


public abstract class ClassQueryDTOtoDoc extends ComponentQueryDTOtoDoc
{

  public ClassQueryDTOtoDoc(ClassQueryDTO queryDTO, Document document)
  {
    super(queryDTO, document);
  }
  
  @Override
  public Node populate()
  {
    Node root = super.populate();
    
    Document document = this.getDocument();

    Element queryType = document.createElement(Elements.QUERY_DTO_TYPE.getLabel());
    queryType.setTextContent(this.getComponentQueryDTO().getType());
    this.getRootElement().appendChild(queryType);
    
    // classes
    Element temp = document.createElement(Elements.QUERY_DTO_CLASSES.getLabel());
    addClasses(temp);
    root.appendChild(temp);
    
    // condition list
    temp = document.createElement(Elements.QUERY_DTO_CONDITION_LIST.getLabel());
    addConditionList(temp);
    root.appendChild(temp);

    temp = document.createElement(Elements.QUERY_DTO_RESULT_SET.getLabel());
    addResultSet(temp);
    root.appendChild(temp);
    
    return root;
  }
    
  /**
   * Adds the classes to the DOM.
   */
  private void addClasses(Element parent)
  {  
    Document document = this.getDocument();
    ClassQueryDTO queryDTO = (ClassQueryDTO) getComponentQueryDTO();
    
    for(String type : queryDTO.getClassTypes())
    {
      Element classNode = document.createElement(Elements.QUERY_DTO_CLASS.getLabel());
      
      // type
      Element typeNode = document.createElement(Elements.QUERY_DTO_CLASS_TYPE.getLabel());
      typeNode.setTextContent(type);
      classNode.appendChild(typeNode);
      
      // superclasses
      Element superClassesNode = document.createElement(Elements.QUERY_DTO_CLASS_SUPERCLASSES.getLabel());
      classNode.appendChild(superClassesNode);
      
      for(String superType : queryDTO.getSuperClassTypes(type))
      {
        // superclass
        Element superClassNode = document.createElement(Elements.QUERY_DTO_CLASS_SUPERCLASS.getLabel());
        superClassNode.setTextContent(superType);
        superClassesNode.appendChild(superClassNode);
        
      }
      
      parent.appendChild(classNode);
    }
  }
  
  /**
   * Adds a condition to a query document.
   * 
   * @param parent
   */
  private void addConditionList(Element parent)
  {
    Document document = getDocument();
    ClassQueryDTO queryDTO = (ClassQueryDTO) getComponentQueryDTO();
    
    List<QueryCondition> conditions = queryDTO.getConditions();
    for (QueryCondition queryCondition : conditions)
    {
      String attributeName = queryCondition.getAttribute();
      String condition = queryCondition.getCondition();
      String conditionValue = queryCondition.getConditionValue();

      // get the root node and add the condition node
      Element conditionNode = document.createElement(Elements.QUERY_DTO_CONDITION.getLabel());

      // add the attribute
      Element tempNode = document.createElement(Elements.QUERY_DTO_CONDITION_ATTRIBUTE.getLabel());
      tempNode.setTextContent(attributeName);
      conditionNode.appendChild(tempNode);

      // add the condition type
      tempNode = document.createElement(Elements.QUERY_DTO_CONDITION_TYPE.getLabel());
      tempNode.setTextContent(condition);
      conditionNode.appendChild(tempNode);

      // add the condition value
      tempNode = document.createElement(Elements.QUERY_DTO_CONDITION_VALUE.getLabel());
      // put the value in a CDATA tag to guard against special characters
      CDATASection cdata = document.createCDATASection(conditionValue);
      tempNode.appendChild(cdata);
      conditionNode.appendChild(tempNode);

      parent.appendChild(conditionNode);
    }
  }
  
}
