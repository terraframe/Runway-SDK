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


import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.business.BusinessQueryDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.transport.conversion.ConversionFacade;

public class DocToBusinessQueryDTO extends DocToElementQueryDTO
{

  /**
   * Constructor to set the Node containing the QueryDTO
   *
   * @param clientRequest
   * @param rootElement
   * @param queryDTO
   */
  public DocToBusinessQueryDTO(ClientRequestIF clientRequest, Element root, BusinessQueryDTO queryDTO)
  {
    super(clientRequest, root, queryDTO);
  }

  @Override
  public BusinessQueryDTO populate()
  {
    BusinessQueryDTO queryDTO = (BusinessQueryDTO) super.populate();
    try
    {
      // result set
      Node asParentList = (Node)ConversionFacade.getXPath().evaluate(Elements.BUSINESS_QUERY_DTO_TYPE_IN_REL_AS_PARENT.getLabel(), this.getRootElement(), XPathConstants.NODE);
      NodeList parentTypeInInstances = (NodeList)ConversionFacade.getXPath().evaluate(Elements.BUSINESS_QUERY_DTO_TYPE_IN_REL_INSTANCE.getLabel(), asParentList, XPathConstants.NODESET);
      for(int i=0; i<parentTypeInInstances.getLength(); i++)
      {
        Node typeInInstance = parentTypeInInstances.item(i);

        String parentDisplayLabel = (String)ConversionFacade.getXPath().evaluate(Elements.BUSINESS_QUERY_DTO_TYPE_IN_REL_PARENT_DISPLAY_LABEL.getLabel(), typeInInstance, XPathConstants.STRING);
        String relationshipType = (String)ConversionFacade.getXPath().evaluate(Elements.BUSINESS_QUERY_DTO_TYPE_IN_RELATIONSHIP_TYPE.getLabel(), typeInInstance, XPathConstants.STRING);

        queryDTO.addTypeInMdRelationshipAsParent(relationshipType, parentDisplayLabel);
      }

      Node asChildList = (Node)ConversionFacade.getXPath().evaluate(Elements.BUSINESS_QUERY_DTO_TYPE_IN_REL_AS_CHILD.getLabel(), this.getRootElement(), XPathConstants.NODE);
      NodeList childTypeInInstances = (NodeList)ConversionFacade.getXPath().evaluate(Elements.BUSINESS_QUERY_DTO_TYPE_IN_REL_INSTANCE.getLabel(), asChildList, XPathConstants.NODESET);

      for(int i=0; i<childTypeInInstances.getLength(); i++)
      {
        Node typeInInstance = childTypeInInstances.item(i);

        String childDisplayLabel = (String)ConversionFacade.getXPath().evaluate(Elements.BUSINESS_QUERY_DTO_TYPE_IN_REL_CHILD_DISPLAY_LABEL.getLabel(), typeInInstance, XPathConstants.STRING);
        String relationshipType = (String)ConversionFacade.getXPath().evaluate(Elements.BUSINESS_QUERY_DTO_TYPE_IN_RELATIONSHIP_TYPE.getLabel(), typeInInstance, XPathConstants.STRING);

        queryDTO.addTypeInMdRelationshipAsChild(relationshipType, childDisplayLabel);
      }
    }
    catch(XPathExpressionException ex)
    {
      String errString = "Improper XPath expression: "+ex.getMessage();
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), errString, ex);
    }

    return queryDTO;
  }



}
