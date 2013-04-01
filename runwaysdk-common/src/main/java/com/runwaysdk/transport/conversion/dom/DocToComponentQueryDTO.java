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

import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.business.BusinessQueryDTO;
import com.runwaysdk.business.ComponentDTOFacade;
import com.runwaysdk.business.ComponentQueryDTO;
import com.runwaysdk.business.RelationshipQueryDTO;
import com.runwaysdk.business.StructQueryDTO;
import com.runwaysdk.business.ValueQueryDTO;
import com.runwaysdk.business.ViewQueryDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.transport.attributes.AttributeDTO;
import com.runwaysdk.transport.conversion.ConversionFacade;

public abstract class DocToComponentQueryDTO extends DocToDTO
{
  /**
   * The QueryDTO to be populated
   */
  private ComponentQueryDTO queryDTO;

  /**
   * Constructor to set the Node containing the QueryDTO
   *
   * @param clientRequest
   * @param rootElement
   * @param queryDTO
   */
  public DocToComponentQueryDTO(ClientRequestIF clientRequest, Element rootElement, ComponentQueryDTO queryDTO)
  {
    super(clientRequest, rootElement);
    this.queryDTO = queryDTO;
  }

  /**
   * Returns the destination ComponentQueryDTO
   *
   * @return
   */
  protected ComponentQueryDTO getComponentQueryDTO()
  {
    return this.queryDTO;
  }

  /**
   * True if the metadata should be included in the document, false otherwise.
   * @return metadata should be included in the document, false otherwise.
   */
  protected boolean convertMetaData()
  {
    return true;
  }

  /**
   * Returns a QueryDTO built from DOM.
   *
   * @param root
   * @param preserveResults
   * @return
   */
  public ComponentQueryDTO populate()
  {
    ComponentQueryDTO componetQueryDTO = this.getComponentQueryDTO();

    try
    {
      // groovy query
      Node groovyCdata = (CDATASection)((Node)ConversionFacade.getXPath().evaluate(Elements.GROOVY_QUERY_STRING.getLabel(), this.getRootElement(), XPathConstants.NODE)).getFirstChild();
      String groovyQuery = (groovyCdata != null) ? groovyCdata.getTextContent() : null;
      componetQueryDTO.setGroovyQuery(groovyQuery);

      // json query
      Node jsonCdata = (CDATASection)((Node)ConversionFacade.getXPath().evaluate(Elements.JSON_QUERY_STRING.getLabel(), this.getRootElement(), XPathConstants.NODE)).getFirstChild();
      String jsonQuery = (jsonCdata != null) ? jsonCdata.getTextContent() : null;
      componetQueryDTO.setJsonQuery(jsonQuery);

      Node definedAttributes = (Node)ConversionFacade.getXPath().evaluate(Elements.QUERY_DTO_DEFINED_ATTRIBUTES.getLabel(), this.getRootElement(), XPathConstants.NODE);
      // defined attributes
      addDefinedAttributes(definedAttributes);

      // page number
      int pageNumber = ((Double)ConversionFacade.getXPath().evaluate(Elements.QUERY_DTO_PAGE_NUMBER.getLabel(), this.getRootElement(), XPathConstants.NUMBER)).intValue();
      componetQueryDTO.setPageNumber(pageNumber);

      // page size
      int pageSize = ((Double)ConversionFacade.getXPath().evaluate(Elements.QUERY_DTO_PAGE_SIZE.getLabel(), this.getRootElement(), XPathConstants.NUMBER)).intValue();
      componetQueryDTO.setPageSize(pageSize);


      // count
      int count = ((Double)ConversionFacade.getXPath().evaluate(Elements.QUERY_DTO_COUNT.getLabel(), this.getRootElement(), XPathConstants.NUMBER)).intValue();
      componetQueryDTO.setCount(count);

      // count enabled
      boolean countEnabled = Boolean.parseBoolean((String)ConversionFacade.getXPath().evaluate(Elements.QUERY_DTO_COUNT_ENABLED.getLabel(), this.getRootElement(), XPathConstants.STRING));
      componetQueryDTO.setCountEnabled(countEnabled);
    }
    catch(XPathExpressionException ex)
    {
      String errString = "Improper XPath expression: "+ex.getMessage();
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), errString, ex);
    }

    return this.getComponentQueryDTO();
  }

  /**
   * Adds all defined attributes.
   *
   * @param definedAttributes
   */
  private void addDefinedAttributes(Node definedAttributes)
  {
    ComponentQueryDTO queryDTO = this.getComponentQueryDTO();

    NodeList attributes = definedAttributes.getChildNodes();
    for (int i=0; i<attributes.getLength(); i++)
    {
      Node attributeNode = attributes.item(i);

      AttributeDTO attributeDTO = setAttribute(attributeNode);
      ComponentDTOFacade.addAttributeQueryDTO(queryDTO, attributeDTO);
    }
  }


  /**
   * Adds the result set.
   *
   * @param parent
   */
  protected void addResultSet(ClientRequestIF clientRequest, Node parent)
  {
    ComponentQueryDTO queryDTO = this.getComponentQueryDTO();

    NodeList results = parent.getChildNodes();
    for(int i=0; i<results.getLength(); i++)
    {
      Node result = results.item(i);
      DocToComponentDTOIF docToComponentDTOIF = DocToComponentDTOIF.getConverter(clientRequest, (Element)result);
      ComponentDTOFacade.addResultItemToQueryDTO(queryDTO, docToComponentDTOIF.populate());
    }
  }

  /**
   * Given a root Node, this method returns a DocToComponentQueryDTO subclass which
   * will convert the contents of the root Node into an ComponentQueryDTO.
   *
   * @param root
   * @return
   */
  public static DocToComponentQueryDTO getConverter(ClientRequestIF clientRequest, Element root, boolean typeSafe)
  {

    String elementName = root.getNodeName();

    if (elementName.equals(Elements.BUSINESS_QUERY_DTO.getLabel()) ||
        elementName.equals(Elements.RELATIONSHIP_QUERY_DTO.getLabel()) ||
        elementName.equals(Elements.STRUCT_QUERY_DTO.getLabel()) ||
        elementName.equals(Elements.VIEW_QUERY_DTO.getLabel()))
    {

      //String type = children.item(Elements.QUERY_DTO_TYPE.getIndex()).getTextContent();
      String type = "";
      try
      {
        // type
        type = (String)ConversionFacade.getXPath().evaluate(Elements.QUERY_DTO_TYPE.getLabel(), root, XPathConstants.STRING);
      }
      catch(XPathExpressionException ex)
      {
        String errString = "Improper XPath expression: "+ex.getMessage();
        CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), errString, ex);
      }

      if(elementName.equals(Elements.BUSINESS_QUERY_DTO.getLabel()))
      {
        BusinessQueryDTO queryDTO;
        if (typeSafe)
        {
          queryDTO = (BusinessQueryDTO)ComponentDTOFacade.instantiateTypeSafeQueryDTO(type);
        }
        else
        {
          queryDTO = ComponentDTOFacade.buildBusinessQueryDTO(type);
        }

        return new DocToBusinessQueryDTO(clientRequest, root, queryDTO);
      }
      else if(elementName.equals(Elements.RELATIONSHIP_QUERY_DTO.getLabel()))
      {
        RelationshipQueryDTO queryDTO;

        if (typeSafe)
        {
          queryDTO = (RelationshipQueryDTO)ComponentDTOFacade.instantiateTypeSafeQueryDTO(type);
        }
        else
        {
          queryDTO  = ComponentDTOFacade.buildRelationshipQueryDTO(type);
        }

        return new DocToRelationshipQueryDTO(clientRequest, root, queryDTO);
      }
      else if(elementName.equals(Elements.VIEW_QUERY_DTO.getLabel()))
      {
        ViewQueryDTO queryDTO;
        if (typeSafe)
        {
          queryDTO = (ViewQueryDTO)ComponentDTOFacade.instantiateTypeSafeQueryDTO(type);
        }
        else
        {
          queryDTO = ComponentDTOFacade.buildViewQueryDTO(type);
        }

        return new DocToViewQueryDTO(clientRequest, root, queryDTO);
      }
      else //if(elementName.equals(Elements.STRUCT_QUERY_DTO.getLabel()))
      {
        StructQueryDTO queryDTO;

        if (typeSafe)
        {
          queryDTO = (StructQueryDTO)ComponentDTOFacade.instantiateTypeSafeQueryDTO(type);
        }
        else
        {
          queryDTO = ComponentDTOFacade.buildStructQueryDTO(type);
        }

        return new DocToStructQueryDTO(clientRequest, root, queryDTO);
      }
    }
    else if(elementName.equals(Elements.VALUE_QUERY_DTO.getLabel()))
    {
      try
      {
        String groovyQuery = (String)ConversionFacade.getXPath().evaluate(Elements.GROOVY_QUERY_STRING.getLabel(), root, XPathConstants.STRING);
        ValueQueryDTO queryDTO = new ValueQueryDTO(groovyQuery);
        return new DocToValueQueryDTO(clientRequest, root, queryDTO);
      }
      catch(XPathExpressionException ex)
      {
        String errString = "Improper XPath expression: "+ex.getMessage();
        CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), errString, ex);
      }
    }
    else
    {
      String error = "The Query Document of type ["+elementName+"] cannot be converted into a Query Object.";
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), error);
    }

    return null;
  }

}
