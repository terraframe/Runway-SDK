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

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.business.BusinessQueryDTO;
import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.business.ComponentQueryDTO;
import com.runwaysdk.business.RelationshipQueryDTO;
import com.runwaysdk.business.StructQueryDTO;
import com.runwaysdk.business.ValueQueryDTO;
import com.runwaysdk.business.ViewQueryDTO;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.transport.attributes.AttributeDTO;

public abstract class ComponentQueryDTOtoDoc extends DTOtoDoc
{
  /**
   * The QueryDTO to convert into a document.
   */
  private ComponentQueryDTO queryDTO;

  private Element rootElement;

  /**
   * Constructor to set the constructing document.
   *
   * @param document
   */
  public ComponentQueryDTOtoDoc(ComponentQueryDTO queryDTO, Document document)
  {
    super(document);
    this.queryDTO = queryDTO;

    this.rootElement = document.createElement(this.getRootElementName());
    document.getDocumentElement().appendChild(this.rootElement);
  }

  /**
   * Returns the source QueryDTO
   *
   * @return
   */
  protected ComponentQueryDTO getComponentQueryDTO()
  {
    return this.queryDTO;
  }

  protected Element getRootElement()
  {
    return this.rootElement;
  }

  /**
   * Returns true, as attribute metadata should be included in the Query (used to render the column headers), but not
   * for the DTOs in the result set.
   * @return true.
   */
  protected boolean convertMetaData()
  {
    return true;
  }

  /**
   * Creates and returns a Document populated with QueryDTO data.
   */
  public Node populate()
  {
    ComponentQueryDTO queryDTO = this.getComponentQueryDTO();
    Document document = getDocument();

    Element groovyQuery = document.createElement(Elements.GROOVY_QUERY_STRING.getLabel());
    CDATASection groovyQueryCdata = document.createCDATASection(queryDTO.getGroovyQuery());
    groovyQuery.appendChild(groovyQueryCdata);
    this.getRootElement().appendChild(groovyQuery);

    Element jsonQuery = document.createElement(Elements.JSON_QUERY_STRING.getLabel());
    CDATASection jsonQueryCdata = document.createCDATASection(queryDTO.getJsonQuery());
    jsonQuery.appendChild(jsonQueryCdata);
    this.getRootElement().appendChild(jsonQuery);

    // defined attributes
    Element temp = document.createElement(Elements.QUERY_DTO_DEFINED_ATTRIBUTES.getLabel());
    addDefinedAttributes(temp);
    this.getRootElement().appendChild(temp);


    // page number
    temp = document.createElement(Elements.QUERY_DTO_PAGE_NUMBER.getLabel());
    temp.setTextContent(Integer.toString(queryDTO.getPageNumber()));
    this.getRootElement().appendChild(temp);

    // page size
    temp = document.createElement(Elements.QUERY_DTO_PAGE_SIZE.getLabel());
    temp.setTextContent(Integer.toString(queryDTO.getPageSize()));
    this.getRootElement().appendChild(temp);

    // count
    temp = document.createElement(Elements.QUERY_DTO_COUNT.getLabel());
    temp.setTextContent(Long.toString(queryDTO.getCount()));
    this.getRootElement().appendChild(temp);

    // count enabled
    temp = document.createElement(Elements.QUERY_DTO_COUNT_ENABLED.getLabel());
    temp.setTextContent(Boolean.toString(queryDTO.isCountEnabled()));
    this.getRootElement().appendChild(temp);

    return this.getRootElement();
  }

  /**
   * Returns the root element name.
   * @return root element name.
   */
  protected abstract String getRootElementName();


  /**
   * Sets the attributes defined by the query type.
   *
   * @param at
   */
  private void addDefinedAttributes(Element parent)
  {
    ComponentQueryDTO queryDTO = this.getComponentQueryDTO();

    // add all attributes to the document defined by the entityDTO
    for (String attributeName : queryDTO.getAttributeNames())
    {
      AttributeDTO attributeDTO = queryDTO.getAttributeDTO(attributeName);

      Node attributeNode = setAttribute(attributeDTO);

      parent.appendChild(attributeNode);
    }
  }

  /**
   * Adds the result set to the DOM.
   */
  protected void addResultSet(Element parent)
  {
    ComponentQueryDTO queryDTO = this.getComponentQueryDTO();

    List<? extends ComponentDTOIF> resultSet = queryDTO.getResultSet();
    for(ComponentDTOIF componentDTOIF : resultSet)
    {
      ComponentDTOIFtoDoc componetDTOIFtoDoc = ComponentDTOIFtoDoc.getConverter(componentDTOIF, parent.getOwnerDocument(), false);
      parent.appendChild(componetDTOIFtoDoc.populate());
    }
  }

  /**
   * Given an ComponentQueryDTO and Document, this method returns an ComponentQueryDTOToDoc subclass
   * that can properly convert the ComponentQueryDTO object into a Document.
   *
   * @param queryDTO
   * @param document
   * @return
   */
  public static ComponentQueryDTOtoDoc getConverter(ComponentQueryDTO queryDTO, Document document)
  {
    if(queryDTO instanceof BusinessQueryDTO)
    {
      return new BusinessQueryDTOtoDoc((BusinessQueryDTO) queryDTO, document);
    }
    else if(queryDTO instanceof RelationshipQueryDTO)
    {
      return new RelationshipQueryDTOtoDoc((RelationshipQueryDTO) queryDTO, document);
    }
    else if(queryDTO instanceof StructQueryDTO)
    {
      return new StructQueryDTOtoDoc((StructQueryDTO) queryDTO, document);
    }
    else if(queryDTO instanceof ViewQueryDTO)
    {
      return new ViewQueryDTOtoDoc((ViewQueryDTO) queryDTO, document);
    }
    else if(queryDTO instanceof ValueQueryDTO)
    {
      return new ValueQueryDTOtoDoc((ValueQueryDTO) queryDTO, document);
    }
    else
    {
      String error = "Cannot convert QueryDTO object of type ["+queryDTO.getType()+"] into a Document.";
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), error);
      return null;
    }
  }
}
