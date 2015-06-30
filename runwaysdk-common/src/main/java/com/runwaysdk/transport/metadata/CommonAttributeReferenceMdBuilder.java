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
package com.runwaysdk.transport.metadata;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.transport.conversion.ConversionFacade;
import com.runwaysdk.transport.conversion.dom.Elements;

public class CommonAttributeReferenceMdBuilder extends CommonAttributeMdBuilder
{
  /**
   * The MdBusiness type the attribute references.
   */
  private String referencedMdBusiness;

  private String referencedDisplayLabel;

  /**
   * Constructor to set the source and destination AttributeReferenceMdDTO
   *
   * @param source
   * @param dest
   */
  protected CommonAttributeReferenceMdBuilder(AttributeReferenceMdDTO source, AttributeReferenceMdDTO dest)
  {
    super(source, dest);

    referencedMdBusiness = source.getReferencedMdBusiness();
    referencedDisplayLabel = source.getReferencedDisplayLabel();
  }

  /**
   *
   * @param metadata
   * @param dest
   */
  protected CommonAttributeReferenceMdBuilder(Node metadata, Node properties, AttributeReferenceMdDTO dest)
  {
    super(metadata, properties, dest);

    try
    {
      referencedMdBusiness = (String)ConversionFacade.getXPath().evaluate(Elements.REFERENCE_METADATA_REFERENCED_MD_BUSINESS.getLabel(), metadata, XPathConstants.STRING);
      referencedDisplayLabel = (String)ConversionFacade.getXPath().evaluate(Elements.REFERENCE_METADATA_REFERENCED_LABEL.getLabel(), metadata, XPathConstants.STRING);
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

    AttributeReferenceMdDTO destSafe = (AttributeReferenceMdDTO) dest;

    destSafe.setReferencedMdBusiness(referencedMdBusiness);
    destSafe.setReferencedDisplayLabel(referencedDisplayLabel);
  }

}
