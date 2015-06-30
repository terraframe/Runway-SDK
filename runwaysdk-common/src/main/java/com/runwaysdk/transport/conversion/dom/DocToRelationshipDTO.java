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

import java.util.Map;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.business.ComponentDTOFacade;
import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.business.RelationshipDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.transport.attributes.AttributeDTO;
import com.runwaysdk.transport.conversion.ConversionFacade;
import com.runwaysdk.transport.metadata.RelationshipTypeMd;

/**
 * Converts a Document into a RelationhipDTO
 */
public class DocToRelationshipDTO extends DocToElementDTO
{
  /**
   * Constructor
   *
   * @param clientRequest
   * @param root
   */
  public DocToRelationshipDTO(ClientRequestIF clientRequest, Element rootElement)
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
  protected RelationshipDTO factoryMethod(String type, Map<String, AttributeDTO> attributeMap,
      boolean newInstance, boolean readable, boolean writable, boolean modified)
  {
    try
    {
      Node root = getRootElement();
      boolean lockedByCurrentUser = Boolean.parseBoolean((String)ConversionFacade.getXPath().evaluate(Elements.ENTITY_LOCKED_BY_CURRENT_USER.getLabel(), root, XPathConstants.STRING));

      String parentId = (String)ConversionFacade.getXPath().evaluate(Elements.RELATIONSHIP_PARENT_ID.getLabel(), this.getPropertyNode(), XPathConstants.STRING );
      String childId = (String)ConversionFacade.getXPath().evaluate(Elements.RELATIONSHIP_CHILD_ID.getLabel(), this.getPropertyNode(), XPathConstants.STRING );
      String toString = (String)ConversionFacade.getXPath().evaluate(Elements.MUTABLE_DTO_TOSTRING.getLabel(), this.getPropertyNode(), XPathConstants.STRING );

      return ComponentDTOFacade.buildRelationshipDTO(this.clientRequest, type, attributeMap, parentId, childId,
          newInstance, readable, writable, modified,  toString, lockedByCurrentUser);

    }
    catch(XPathExpressionException ex)
    {
      String errString = "Improper XPath expression: "+ex.getMessage();
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), errString, ex);
      return null;
    }
  }

  @Override
  public RelationshipDTO populate()
  {
    return (RelationshipDTO) super.populate();
  }

  /**
   * Sets the TypeMd on the RelationshipDTO. This method must be called
   * after factoryMethod().
   */
  protected void setTypeMd(ComponentDTOIF componentDTOIF)
  {
    try
    {
      Node root = getRootElement();

      Node metadata = ((Node)ConversionFacade.getXPath().evaluate(Elements.TYPE_MD.getLabel(), root, XPathConstants.NODE));

      if (metadata != null)
      {
        Node cdata = (CDATASection)((Node)ConversionFacade.getXPath().evaluate(Elements.TYPE_MD_DISPLAY_LABEL.getLabel(), metadata, XPathConstants.NODE)).getFirstChild();

        String displayLabel = "";
        if(cdata != null)
        {
          displayLabel = cdata.getTextContent();
        }
        cdata = (CDATASection)((Node)ConversionFacade.getXPath().evaluate(Elements.TYPE_MD_DESCRIPTION.getLabel(), metadata, XPathConstants.NODE)).getFirstChild();

        String description = "";
        if(cdata != null)
        {
          description = cdata.getTextContent();
        }

        String metadataId = ((Node)ConversionFacade.getXPath().evaluate(Elements.TYPE_MD_ID.getLabel(), metadata, XPathConstants.NODE)).getTextContent();

        // parentMdBusiness
        String parentMdBusiness = ((String)ConversionFacade.getXPath().evaluate(Elements.RELATIONSHIP_MD_PARENT_MD_BUSINESS.getLabel(), metadata, XPathConstants.STRING));

        // childMdBusiness
        String childMdBusiness = ((String)ConversionFacade.getXPath().evaluate(Elements.RELATIONSHIP_MD_CHILD_MD_BUSINESS.getLabel(), metadata, XPathConstants.STRING));

        RelationshipTypeMd typeMd = new RelationshipTypeMd(displayLabel, description, metadataId, parentMdBusiness, childMdBusiness);
        componentDTOIF.setMd(typeMd);
      }
      else
      {
        componentDTOIF.setMd(new RelationshipTypeMd());
      }
    }
    catch(XPathExpressionException ex)
    {
      String errString = "Improper XPath expression: "+ex.getMessage();
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), errString, ex);
    }
  }
}
