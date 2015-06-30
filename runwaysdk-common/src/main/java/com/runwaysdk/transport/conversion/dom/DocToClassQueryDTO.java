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

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.business.ClassQueryDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.transport.conversion.ConversionFacade;

public abstract class DocToClassQueryDTO extends DocToComponentQueryDTO
{

  /**
   * Constructor to set the Node containing the QueryDTO
   *
   * @param clientRequest
   * @param rootElement
   * @param queryDTO
   */
  public DocToClassQueryDTO(ClientRequestIF clientRequest, Element rootElement, ClassQueryDTO queryDTO)
  {
    super(clientRequest, rootElement, queryDTO);
  }

  /**
   * Returns the destination ClassQueryDTO
   *
   * @return
   */
  protected ClassQueryDTO getComponentQueryDTO()
  {
    return (ClassQueryDTO)super.getComponentQueryDTO();
  }

  @Override
  public ClassQueryDTO populate()
  {
    ClassQueryDTO queryDTO = (ClassQueryDTO)super.populate();

    try
    {
      Node classes = (Node)ConversionFacade.getXPath().evaluate(Elements.QUERY_DTO_CLASSES.getLabel(), this.getRootElement(), XPathConstants.NODE);
      addClasses(classes);

      // condition list
      Node conditionList = (Node)ConversionFacade.getXPath().evaluate(Elements.QUERY_DTO_CONDITION_LIST.getLabel(), this.getRootElement(), XPathConstants.NODE);
      addConditionList(conditionList);
    }
    catch(XPathExpressionException ex)
    {
      String errString = "Improper XPath expression: "+ex.getMessage();
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), errString, ex);
    }

    return queryDTO;
  }


  /**
   * Adds the classes.
   *
   * @param parent
   */
  private void addClasses(Node parent)
  {
    ClassQueryDTO queryDTO = (ClassQueryDTO) getComponentQueryDTO();

    try
    {
      NodeList classes = (NodeList)ConversionFacade.getXPath().evaluate(Elements.QUERY_DTO_CLASS.getLabel(), parent, XPathConstants.NODESET);
      for(int i=0; i<classes.getLength(); i++)
      {
        Node classNode = classes.item(i);
        String type = (String)ConversionFacade.getXPath().evaluate(Elements.QUERY_DTO_CLASS_TYPE.getLabel(), classNode, XPathConstants.STRING);

        Node superClassesNode = (Node)ConversionFacade.getXPath().evaluate(Elements.QUERY_DTO_CLASS_SUPERCLASSES.getLabel(), classNode, XPathConstants.NODE);

        NodeList superClasses = superClassesNode.getChildNodes();
        List<String> superClassesList = new LinkedList<String>();
        for(int j=0; j<superClasses.getLength(); j++)
        {
          String superClass = superClasses.item(j).getTextContent();
          superClassesList.add(superClass);
        }

        queryDTO.addClassType(type, superClassesList);
      }
    }
    catch(XPathExpressionException ex)
    {
      String errString = "Improper XPath expression: "+ex.getMessage();
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), errString, ex);
    }
  }


  /**
   * Adds the condition list.
   *
   * @param parent
   */
  private void addConditionList(Node parent)
  {
    ClassQueryDTO queryDTO =  getComponentQueryDTO();

    try
    {
      NodeList conditions = parent.getChildNodes();
      for (int i = 0; i < conditions.getLength(); i++)
      {
        Node condition = conditions.item(i);

        String attribute = (String)ConversionFacade.getXPath().evaluate(Elements.QUERY_DTO_CONDITION_ATTRIBUTE.getLabel(), condition, XPathConstants.STRING);
        String conditionType = (String)ConversionFacade.getXPath().evaluate(Elements.QUERY_DTO_CONDITION_TYPE.getLabel(), condition, XPathConstants.STRING);

        Node cdataNode = ((Node)ConversionFacade.getXPath().evaluate(Elements.QUERY_DTO_CONDITION_VALUE.getLabel(), condition, XPathConstants.NODE)).getFirstChild();

        String conditionValue = "";
        if(cdataNode != null && cdataNode instanceof CDATASection)
        {
          conditionValue = cdataNode.getTextContent();
        }

        // use the condition data to add a condition to the query dto
        queryDTO.addCondition(attribute, conditionType, conditionValue);
      }
    }
    catch(XPathExpressionException ex)
    {
      String errString = "Improper XPath expression: "+ex.getMessage();
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), errString, ex);
    }
  }
}
