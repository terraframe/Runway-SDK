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

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.business.ElementQueryDTO;
import com.runwaysdk.business.RelationshipQueryDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.transport.conversion.ConversionFacade;

public class DocToRelationshipQueryDTO extends DocToElementQueryDTO
{

  /**
   * Constructor to set the Node containing the QueryDTO
   *
   * @param clientRequest
   * @param rootElement
   * @param queryDTO
   */
  public DocToRelationshipQueryDTO(ClientRequestIF clientRequest, Element rootElement, RelationshipQueryDTO queryDTO)
  {
    super(clientRequest, rootElement, queryDTO);
  }

  @Override
  public ElementQueryDTO populate()
  {
    RelationshipQueryDTO queryDTO = (RelationshipQueryDTO) super.populate();

    try
    {
      // parentMdRelationship
      String parentMdBusiness = (String)ConversionFacade.getXPath().evaluate(Elements.RELATIONSHIP_QUERY_DTO_PARENT_MD_BUSINESS.getLabel(), this.getRootElement(), XPathConstants.STRING);
      queryDTO.setParentMdBusiness(parentMdBusiness);

      // childMdRelationship
      String childMdBusiness = (String)ConversionFacade.getXPath().evaluate(Elements.RELATIONSHIP_QUERY_DTO_CHILD_MD_BUSINESS.getLabel(), this.getRootElement(), XPathConstants.STRING);
      queryDTO.setChildMdBusiness(childMdBusiness);
    }
    catch(XPathExpressionException ex)
    {
      String errString = "Improper XPath expression: "+ex.getMessage();
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), errString, ex);
    }

    return queryDTO;
  }

}
