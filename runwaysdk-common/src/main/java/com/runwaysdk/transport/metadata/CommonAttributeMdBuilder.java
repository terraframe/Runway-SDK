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
package com.runwaysdk.transport.metadata;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Node;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.transport.conversion.ConversionFacade;
import com.runwaysdk.transport.conversion.dom.Elements;

/**
 * Builds the metadata for an attribute.
 */
public class CommonAttributeMdBuilder
{
  /**
   * The destination of the metadata.
   */
  protected AttributeMdDTO dest;

  /**
   * The attribute display label
   */
  private String displayLabel;

  /**
   * The attribute description
   */
  private String description;

  /**
   * Denotes if this attribute is required
   */
  private boolean required;

  /**
   * Denotes if this attribute is immutable
   */
  private boolean immutable;

  /**
   * The id of the attribute metadata;
   */
  private String id;

  /**
   * The flag denoting if the attribute is a system attribute.
   */
  private boolean system;

  /**
   * The attribute name
   */
  private String name;

  /**
   * Flag denoting if the attribute has getters and setters
   */
  private boolean generateAccessor;

//  private XPath xpath;

  /**
   *
   * @param source
   * @param dest
   */
  protected CommonAttributeMdBuilder(AttributeMdDTO source, AttributeMdDTO dest)
  {
    this.dest = dest;

    this.displayLabel = source.getDisplayLabel();
    this.description = source.getDescription();
    this.required = source.isRequired();
    this.immutable = source.isImmutable();
    this.id = source.getId();
    this.system = source.isSystem();
    this.name = source.getName();
    this.generateAccessor = source.getGenerateAccessor();

  }

  /**
   *
   * @param metadata
   * @param dest
   */
  protected CommonAttributeMdBuilder(Node metadata, Node properties, AttributeMdDTO dest)
  {
    this.dest = dest;

    try
    {
      Node cdata = (CDATASection)((Node)ConversionFacade.getXPath().evaluate(Elements.ATTRIBUTE_METADATA_DISPLAY_LABEL.getLabel(), metadata, XPathConstants.NODE)).getFirstChild();
      this.displayLabel = "";
      if(cdata != null)
      {
        this.displayLabel = cdata.getTextContent();
      }

      cdata = (CDATASection)((Node)ConversionFacade.getXPath().evaluate(Elements.ATTRIBUTE_METADATA_DESCRIPTION.getLabel(), metadata, XPathConstants.NODE)).getFirstChild();
      this.description = "";
      if(cdata != null)
      {
        this.description = cdata.getTextContent();
      }

      this.required = Boolean.parseBoolean((String)ConversionFacade.getXPath().evaluate(Elements.ATTRIBUTE_METADATA_REQUIRED.getLabel(), metadata, XPathConstants.STRING));
      this.immutable = Boolean.parseBoolean((String)ConversionFacade.getXPath().evaluate(Elements.ATTRIBUTE_METADATA_IMMUTABLE.getLabel(), metadata, XPathConstants.STRING));
      this.id = (String)ConversionFacade.getXPath().evaluate(Elements.ATTRIBUTE_METADATA_ID.getLabel(), metadata, XPathConstants.STRING);
      this.system = Boolean.parseBoolean((String)ConversionFacade.getXPath().evaluate(Elements.ATTRIBUTE_METADATA_SYSTEM.getLabel(), metadata, XPathConstants.STRING));
      this.name = (String)ConversionFacade.getXPath().evaluate(Elements.ATTRIBUTE_NAME.getLabel(), properties, XPathConstants.STRING);
      this.generateAccessor = (Boolean)ConversionFacade.getXPath().evaluate(Elements.ATTRIBUTE_METADATA_GENERATE_ACCESSOR.getLabel(), metadata, XPathConstants.BOOLEAN);
    }
    catch(XPathExpressionException ex)
    {
      String errString = "Improper XPath expression: "+ex.getMessage();
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), errString, ex);
    }
  }

  /**
   * Sets the metadata on the provided AttributeMdDTO
   *
   * @return
   */
  protected void build()
  {
    dest.setDisplayLabel(displayLabel);
    dest.setDescription(description);
    dest.setRequired(required);
    dest.setImmutable(immutable);
    dest.setId(id);
    dest.setSystem(system);
    dest.setName(name);
    dest.setGenerateAccessor(generateAccessor);
  }

}
