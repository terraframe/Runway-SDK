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

public class CommonAttributeNumberMdBuilder extends CommonAttributeMdBuilder
{

  /**
   * Denotes if this attribute rejects zero.
   */
  private boolean rejectZero;

  /**
   * Denotes if this attribute rejects negative numbers
   */
  private boolean rejectNegative;

  /**
   * Denotes if this attribute rejects positive numbers
   */
  private boolean rejectPositive;

  /**
   *
   * @param source
   * @param dest
   */
  protected CommonAttributeNumberMdBuilder(AttributeNumberMdDTO source, AttributeNumberMdDTO dest)
  {
    super(source, dest);

    this.rejectZero = source.rejectZero();
    this.rejectNegative = source.rejectNegative();
    this.rejectPositive = source.rejectPositive();
  }

  /**
   *
   * @param metadata
   * @param dest
   */
  protected CommonAttributeNumberMdBuilder(Node metadata, Node properties, AttributeNumberMdDTO dest)
  {
    super(metadata, properties, dest);

    try
    {
      this.rejectZero = Boolean.parseBoolean((String)ConversionFacade.getXPath().evaluate(Elements.NUMBER_METADATA_REJECT_ZERO.getLabel(), metadata, XPathConstants.STRING));
      this.rejectNegative = Boolean.parseBoolean((String)ConversionFacade.getXPath().evaluate(Elements.NUMBER_METADATA_REJECT_NEGATIVE.getLabel(), metadata, XPathConstants.STRING));
      this.rejectPositive = Boolean.parseBoolean((String)ConversionFacade.getXPath().evaluate(Elements.NUMBER_METADATA_REJECT_POSITIVE.getLabel(), metadata, XPathConstants.STRING));
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

    AttributeNumberMdDTO destSafe = (AttributeNumberMdDTO) dest;

    destSafe.setRejectZero(rejectZero);
    destSafe.setRejectNegative(rejectNegative);
    destSafe.setRejectPositive(rejectPositive);
  }

}
