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

import org.w3c.dom.Node;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.transport.conversion.ConversionFacade;
import com.runwaysdk.transport.conversion.dom.Elements;

/**
 * Builds the metadata for an attribute struct.
 */
public class CommonAttributeStructMdBuilder extends CommonAttributeMdBuilder
{
  /**
   * The MdStruct that defines the struct.
   */
  private String definingMdStruct;

  /**
   *
   * @param source
   * @param dest
   */
  protected CommonAttributeStructMdBuilder(AttributeStructMdDTO source, AttributeMdDTO dest)
  {
    super(source, dest);

    definingMdStruct = source.getDefiningMdStruct();
  }

  /**
   *
   * @param metadata
   * @param dest
   */
  protected CommonAttributeStructMdBuilder(Node metadata, Node properties, AttributeMdDTO dest)
  {
    super(metadata, properties, dest);

    try
    {
      definingMdStruct = (String)ConversionFacade.getXPath().evaluate(Elements.STRUCT_METADATA_DEFINING_MDSTRUCT.getLabel(), metadata, XPathConstants.STRING);
    }
    catch(XPathExpressionException ex)
    {
      String errString = "Improper XPath expression: "+ex.getMessage();
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), errString, ex);
    }
  }

  /**
   * Builds the metadata.
   */
  protected void build()
  {
    super.build();

    AttributeStructMdDTO destSafe = (AttributeStructMdDTO) dest;

    destSafe.setDefiningMdStruct(definingMdStruct);
  }

}
