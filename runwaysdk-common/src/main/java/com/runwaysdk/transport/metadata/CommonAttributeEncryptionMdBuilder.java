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
 * Builds the metadata for an attribute encryption.
 */
public class CommonAttributeEncryptionMdBuilder extends CommonAttributeMdBuilder
{
  /**
   * The encryption method this attribute uses.
   */
  private String encryptionMethod;

  /**
   *
   * @param source
   * @param dest
   */
  protected CommonAttributeEncryptionMdBuilder(AttributeEncryptionMdDTO source, AttributeEncryptionMdDTO dest)
  {
    super(source, dest);

    encryptionMethod = source.getEncryptionMethod();
  }

  /**
   *
   * @param metadata
   * @param dest
   */
  protected CommonAttributeEncryptionMdBuilder(Node metadata, Node properties, AttributeEncryptionMdDTO dest)
  {
    super(metadata, properties, dest);

    try
    {
      encryptionMethod = (String)ConversionFacade.getXPath().evaluate(Elements.ENCRYPTION_METADATA_ENCRYPTION_METHOD.getLabel(), metadata, XPathConstants.STRING);
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

    AttributeEncryptionMdDTO destSafe = (AttributeEncryptionMdDTO) dest;

    destSafe.setEncryptionMethod(encryptionMethod);
  }

}
