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

import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.transport.attributes.AttributeDTO;
import com.runwaysdk.transport.conversion.ConversionFacade;
import com.runwaysdk.transport.metadata.TypeMd;

public abstract class DocToComponentDTOIF extends DocToDTO
{
  private Node properties;

  /**
   * Constructor.
   *
   * @param clientRequest
   * @param root
   * @param convertMetaData
   */
  public DocToComponentDTOIF(ClientRequestIF clientRequest, Element root)
  {
    super(clientRequest, root);

    try
    {
      this.properties = (Node)ConversionFacade.getXPath().evaluate(Elements.PROPERTIES.getLabel(), root, XPathConstants.NODE );
    }
    catch(XPathExpressionException ex)
    {
      String errString = "Improper XPath expression: "+ex.getMessage();
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), errString, ex);
    }
  }

  /**
   *
   * @return
   */
  protected Node getPropertyNode()
  {
    return this.properties;
  }

  public ComponentDTOIF populate()
  {
    try
    {
      Map<String, AttributeDTO> attributeDTOmap = this.setAttributes();

    // type data
      String type = (String)ConversionFacade.getXPath().evaluate(Elements.COMPONENT_TYPE.getLabel(), this.getPropertyNode(), XPathConstants.STRING );

      boolean newInstance = Boolean.parseBoolean((String)ConversionFacade.getXPath().evaluate(Elements.COMPONENT_NEW_INSTANCE.getLabel(), this.getPropertyNode(), XPathConstants.STRING));
      boolean readable = Boolean.parseBoolean((String)ConversionFacade.getXPath().evaluate(Elements.COMPONENT_READABLE.getLabel(), this.getPropertyNode(), XPathConstants.STRING));
      boolean writable = Boolean.parseBoolean((String)ConversionFacade.getXPath().evaluate(Elements.COMPONENT_WRITABLE.getLabel(), this.getPropertyNode(), XPathConstants.STRING));
      boolean modified = Boolean.parseBoolean((String)ConversionFacade.getXPath().evaluate(Elements.COMPONENT_MODIFIED.getLabel(), this.getPropertyNode(), XPathConstants.STRING));

      ComponentDTOIF componentDTOIF = factoryMethod(type, attributeDTOmap, newInstance, readable, writable, modified);

      this.setTypeMd(componentDTOIF);

      return componentDTOIF;

    }
    catch(XPathExpressionException ex)
    {
      String errString = "Improper XPath expression: "+ex.getMessage();
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), errString, ex);
      return null;
    }

  }


  /**
   * Sets the attributes on the EntityDTOIF
   */
  protected Map<String, AttributeDTO> setAttributes()
  {
    Map<String, AttributeDTO> attributeDTOmap = new LinkedHashMap<String, AttributeDTO>();

    try
    {
      Node attributeList = (Node)ConversionFacade.getXPath().evaluate(Elements.COMPONENT_ATTRIBUTE_LIST.getLabel(), this.getRootElement(), XPathConstants.NODE );

      NodeList attributes = attributeList.getChildNodes();
      for (int i=0; i<attributes.getLength(); i++)
      {
        Node attribute = attributes.item(i);

        AttributeDTO attributeDTO = setAttribute(attribute);
        attributeDTOmap.put(attributeDTO.getName(), attributeDTO);
      }

    }
    catch(XPathExpressionException ex)
    {
      String errString = "Improper XPath expression: "+ex.getMessage();
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), errString, ex);
    }

    return attributeDTOmap;
  }

  /**
   * Sets the TypeMd on the ComponentDTOIF. This method must be called
   * after factoryMethod().
   */
  protected void setTypeMd(ComponentDTOIF componentDTOIF)
  {
    try
    {
      Node root = getRootElement();

      // type metadata
      Node metadata = ((Node)ConversionFacade.getXPath().evaluate(Elements.TYPE_MD.getLabel(), root, XPathConstants.NODE));

      if (metadata != null)
      {
        Node cdata = (CDATASection)((Node)ConversionFacade.getXPath().evaluate(Elements.TYPE_MD_DISPLAY_LABEL.getLabel(), metadata, XPathConstants.NODE)).getFirstChild();

        String displayLabel = "";
        if(cdata != null)
        {
          displayLabel = cdata.getTextContent();
        }

        // if there is no value, there won't be a CDATA tag
        cdata = (CDATASection)((Node)ConversionFacade.getXPath().evaluate(Elements.TYPE_MD_DESCRIPTION.getLabel(), metadata, XPathConstants.NODE)).getFirstChild();

        String description = "";
        if(cdata != null)
        {
          description = cdata.getTextContent();
        }

        String metadataId = ((Node)ConversionFacade.getXPath().evaluate(Elements.TYPE_MD_ID.getLabel(), metadata, XPathConstants.NODE)).getTextContent();

        TypeMd typeMd = new TypeMd(displayLabel, description, metadataId);
        componentDTOIF.setMd(typeMd);
      }
      else
      {
        componentDTOIF.setMd(new TypeMd());
      }

    }
    catch(XPathExpressionException ex)
    {
      String errString = "Improper XPath expression: "+ex.getMessage();
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), errString, ex);
    }
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
  protected abstract ComponentDTOIF factoryMethod(String type, Map<String, AttributeDTO> attributeMap,
      boolean newInstance, boolean readable, boolean writable, boolean modified);


  /**
   * Returns a concrete implementation of DocToEntityDTO depending on the given
   * EntityDTO subclass.
   *
   * @param clientRequest
   * @param rootElement
   * @return
   */
  public static DocToComponentDTOIF getConverter(ClientRequestIF clientRequest, Element rootElement)
  {
    String name = rootElement.getNodeName();

    if(name.equals(Elements.BUSINESS_DTO.getLabel()))
    {
      return new DocToBusinessDTO(clientRequest, rootElement);
    }
    else if(name.equals(Elements.RELATIONSHIP_DTO.getLabel()))
    {
      return new DocToRelationshipDTO(clientRequest, rootElement);
    }
    else if(name.equals(Elements.STRUCT_DTO.getLabel()))
    {
      return new DocToStructDTO(clientRequest, rootElement);
    }
    else if(name.equals(Elements.VALUE_DTO.getLabel()))
    {
      return new DocToValueObjectDTO(clientRequest, rootElement);
    }
    else if(name.equals(Elements.UTIL_DTO.getLabel()))
    {
      return new DocToUtilDTO(clientRequest, rootElement);
    }
    else if(name.equals(Elements.VIEW_DTO.getLabel()))
    {
      return new DocToViewDTO(clientRequest, rootElement);
    }
    else if(name.equals(Elements.EXCEPTION_DTO.getLabel()))
    {
      return new DocToExceptionDTO(clientRequest, rootElement);
    }
    else
    {
      String error = "Cannot convert document with root type ["+rootElement+"] into an EntityDTO.";
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), error);
      return null;
    }
  }

}
