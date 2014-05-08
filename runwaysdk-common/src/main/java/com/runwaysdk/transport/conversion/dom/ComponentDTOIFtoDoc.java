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

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.ComponentDTO;
import com.runwaysdk.business.ComponentDTOFacade;
import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.business.ExceptionDTO;
import com.runwaysdk.business.InformationDTO;
import com.runwaysdk.business.ProblemDTO;
import com.runwaysdk.business.RelationshipDTO;
import com.runwaysdk.business.SmartExceptionDTO;
import com.runwaysdk.business.StructDTO;
import com.runwaysdk.business.UtilDTO;
import com.runwaysdk.business.ValueObjectDTO;
import com.runwaysdk.business.ViewDTO;
import com.runwaysdk.business.WarningDTO;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.transport.attributes.AttributeDTO;

public abstract class ComponentDTOIFtoDoc extends DTOtoDoc
{
  /**
   * The ComponentDTO as the source of the document.
   */
  private ComponentDTO componentDTO;

  /**
   * The root node defining the ComponentDTO
   */
  private Node componentRoot;

  /**
   * Contains additional nodes other than the attribtute list.
   */
  private Node propertiesNode;

  /**
   *
   */
  private boolean convertMetaData;

  /**
   * Constructor
   *
   * @param componentDTO
   * @param document
   * @param root
   * @param convertMetaData
   */
  public ComponentDTOIFtoDoc(ComponentDTO componentDTO, Document document, Node root, boolean convertMetaData)
  {
    super(document);
    this.componentRoot = root;
    this.componentDTO = componentDTO;

    this.propertiesNode = document.createElement(Elements.PROPERTIES.getLabel());

    this.convertMetaData = convertMetaData;
  }

  /**
   * Returns the componentDTO being converted to a document.
   * @return componentDTO being converted to a document.
   */
  protected ComponentDTO getComponentDTO()
  {
    return this.componentDTO;
  }

  /**
   * Returns the root node of the document.
   * @return root node of the document.
   */
  protected Node getRoot()
  {
    return this.componentRoot;
  }

  /**
   * Contains additional nodes other than the attribtute list.
   * @return node that contains additional nodes other than the attribtute list.
   */
  protected Node getPropertiesNode()
  {
    return this.propertiesNode;
  }

  /**
   * True if the metadata should be included in the document, false otherwise.
   * @return metadata should be included in the document, false otherwise.
   */
  protected boolean convertMetaData()
  {
    return this.convertMetaData;
  }

  /**
   * Populates the DOM and returns the root node.
   *
   * @return
   */
  public Node populate()
  {
    // add the TypeMd
    this.setTypeMd();

    setProperties();

    setAttributes();

    return this.getRoot();
  }

  /**
   * Sets the EntityDTO information to the document that all EntityDTOs share.
   */
  protected void setProperties()
  {
    Document document = getDocument();

    this.componentRoot.appendChild(this.propertiesNode);

    // create the id
    Element tempElement = document.createElement(Elements.ENTITY_ID.getLabel());
    tempElement.appendChild(document.createTextNode(this.getComponentDTO().getId()));
    this.getPropertiesNode().appendChild(tempElement);

    // create the type element
    tempElement = document.createElement(Elements.COMPONENT_TYPE.getLabel());
    tempElement.appendChild(document.createTextNode(this.getComponentDTO().getType()));
    this.getPropertiesNode().appendChild(tempElement);

    // create the readable element
    tempElement = document.createElement(Elements.COMPONENT_READABLE.getLabel());
    tempElement.appendChild(document.createTextNode(Boolean.toString(this.getComponentDTO().isReadable())));
    this.getPropertiesNode().appendChild(tempElement);
  }

  /**
   * Sets all attributes that an ComponentDTO contains.
   */
  protected void setAttributes()
  {
    Document document = getDocument();

    Element attributeList = document.createElement(Elements.COMPONENT_ATTRIBUTE_LIST.getLabel());

    // create the attribute list element
    this.getRoot().appendChild(attributeList);

    // add all attributes to the document defined by the entityDTO
    for (String attributeName : this.getComponentDTO().getAttributeNames())
    {
      AttributeDTO attributeDTO = ComponentDTOFacade.getAttributeDTO(this.getComponentDTO(), attributeName);

      Node attributeNode = setAttribute(attributeDTO);

      attributeList.appendChild(attributeNode);
    }
  }

  /**
   * Sets the TypeMd data on the Document.
   */
  protected void setTypeMd()
  {
    Document document = getDocument();

    if (this.convertMetaData())
    {
      // type metadata
      Element metadata = document.createElement(Elements.TYPE_MD.getLabel());
      this.getRoot().appendChild(metadata);

      // display label
      Element tempElement = document.createElement(Elements.TYPE_MD_DISPLAY_LABEL.getLabel());
      CDATASection cdata = document.createCDATASection(this.getComponentDTO().getMd().getDisplayLabel());
      tempElement.appendChild(cdata);
      metadata.appendChild(tempElement);

      // description
      tempElement = document.createElement(Elements.TYPE_MD_DESCRIPTION.getLabel());
      cdata = document.createCDATASection(this.getComponentDTO().getMd().getDescription());
      tempElement.appendChild(cdata);
      metadata.appendChild(tempElement);

      // metadata id
      tempElement = document.createElement(Elements.TYPE_MD_ID.getLabel());
      tempElement.setTextContent(this.getComponentDTO().getMd().getId());
      metadata.appendChild(tempElement);
    }
  }

  /**
   * Returns a concrete implementation of EntityDTOToDoc depending on the given
   * componentDTOIF subclass.
   *
   * @param componentDTOIF
   * @param document
   * @return
   */
  public static ComponentDTOIFtoDoc getConverter(ComponentDTOIF componentDTOIF, Document document, boolean convertMetaData)
  {
    if(componentDTOIF instanceof BusinessDTO)
    {
      return new BusinessDTOtoDoc((BusinessDTO) componentDTOIF, document, convertMetaData);
    }
    else if(componentDTOIF instanceof RelationshipDTO)
    {
      return new RelationshipDTOtoDoc((RelationshipDTO) componentDTOIF, document, convertMetaData);
    }
    else if(componentDTOIF instanceof StructDTO)
    {
      return new StructDTOtoDoc((StructDTO) componentDTOIF, document, convertMetaData);
    }
    else if(componentDTOIF instanceof ValueObjectDTO)
    {
      return new ValueObjectDTOtoDOC((ValueObjectDTO) componentDTOIF, document);
    }
    else if(componentDTOIF instanceof UtilDTO)
    {
      return new UtilDTOtoDoc((UtilDTO) componentDTOIF, document, convertMetaData);
    }
    else if(componentDTOIF instanceof ViewDTO)
    {
      return new ViewDTOtoDoc((ViewDTO) componentDTOIF, document, convertMetaData);
    }
    else if (componentDTOIF instanceof SmartExceptionDTO)
    {
      return new SmartExceptionDTOtoDoc((SmartExceptionDTO) componentDTOIF, document, convertMetaData);
    }
    else if (componentDTOIF instanceof ExceptionDTO)
    {
      return new SmartExceptionDTOtoDoc((ExceptionDTO) componentDTOIF, document, convertMetaData);
    }
    else if(componentDTOIF instanceof ProblemDTO)
    {
      return new ProblemDTOtoDoc((ProblemDTO) componentDTOIF, document, convertMetaData);
    }
    else if(componentDTOIF instanceof WarningDTO)
    {
      return new WarningDTOtoDoc((WarningDTO) componentDTOIF, document, convertMetaData);
    }
    else if(componentDTOIF instanceof InformationDTO)
    {
      return new InformationDTOtoDoc((InformationDTO) componentDTOIF, document, convertMetaData);
    }
    else
    {
      String error = "Cannot convert EntityDTO type ["+componentDTOIF.getType()+"] into a document.";
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), error);
      return null;
    }
  }
}
