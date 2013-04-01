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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.ComponentDTOFacade;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.transport.attributes.AttributeDTO;
import com.runwaysdk.transport.conversion.ConversionFacade;

/**
 * Converts a document into a BusinessDTO
 */
public class DocToBusinessDTO extends DocToElementDTO
{
  /**
   * Constructor
   *
   * @param clientRequest
   * @param root
   * @param convertMetaData
   */
  public DocToBusinessDTO(ClientRequestIF clientRequest, Element rootElement)
  {
    super(clientRequest, rootElement);

  }

  /**
   * Instantiates the proper entityDTO class (not type safe)
   * @param type
   * @param attributeMap
   * @param newInstance
   * @param readable
   * @param writable
   * @param modified
   * @return proper entityDTO class (not type safe)
   */
  protected BusinessDTO factoryMethod(String type, Map<String, AttributeDTO> attributeMap,
      boolean newInstance, boolean readable, boolean writable, boolean modified)
  {
    try
    {
      boolean lockedByCurrentUser = Boolean.parseBoolean((String)ConversionFacade.getXPath().evaluate(Elements.ENTITY_LOCKED_BY_CURRENT_USER.getLabel(), this.getPropertyNode(), XPathConstants.STRING));

      String toString = (String)ConversionFacade.getXPath().evaluate(Elements.MUTABLE_DTO_TOSTRING.getLabel(), this.getPropertyNode(), XPathConstants.STRING );

      return ComponentDTOFacade.buildBusinessDTO(this.clientRequest, type, attributeMap,
          newInstance, readable, writable, modified,  toString, lockedByCurrentUser);
    }
    catch(XPathExpressionException ex)
    {
      String errString = "Improper XPath expression: "+ex.getMessage();
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), errString, ex);
    }

    return null;
  }


  @Override
  public BusinessDTO populate()
  {
    BusinessDTO businessDTO = (BusinessDTO) super.populate();

    try
    {
      String state = (String)ConversionFacade.getXPath().evaluate(Elements.BUSINESS_STATE.getLabel(), this.getPropertyNode(), XPathConstants.STRING );
      businessDTO.setState(state);

      // transitions
      List<String> transistions = new LinkedList<String>();

      Node transitionNode = (Node)ConversionFacade.getXPath().evaluate(Elements.BUSINESS_TRANSITIONS.getLabel(), this.getPropertyNode(), XPathConstants.NODE );

      NodeList transitionEls = transitionNode.getChildNodes();

      for(int i=0; i<transitionEls.getLength(); i++)
      {
        Node transitionEl = transitionEls.item(i);
        transistions.add(transitionEl.getTextContent());
      }
    }
    catch(XPathExpressionException ex)
    {
      String errString = "Improper XPath expression: "+ex.getMessage();
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), errString, ex);
    }

    return businessDTO;
  }

}
