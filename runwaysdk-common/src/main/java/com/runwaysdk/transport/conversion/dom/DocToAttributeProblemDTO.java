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

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.business.AttributeProblemDTO;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.transport.conversion.ConversionFacade;

public class DocToAttributeProblemDTO extends DocToRunwayProblemDTO
{

  public DocToAttributeProblemDTO(Element rootElement)
  {
    super(rootElement);
  }

  @Override
  public AttributeProblemDTO populate()
  {
    String type = null;
    String localizedMessage = null;
    String developerMessage = null;
    String componentId = null;
    String definingType = null;
    String definingTypeDisplayLabel = null;
    String attributeName = null;
    String attributeId = null;
    String attributeDisplayLabel = null;

    try
    {
      type = (String)ConversionFacade.getXPath().evaluate(Elements.RUNWAYPROBLEM_TYPE.getLabel(), this.getRootElement(), XPathConstants.STRING);
      localizedMessage = (String)ConversionFacade.getXPath().evaluate(Elements.RUNWAYPROBLEM_LOCALIZED_MESSAGE.getLabel(), this.getRootElement(), XPathConstants.STRING);
      developerMessage = (String)ConversionFacade.getXPath().evaluate(Elements.RUNWAYPROBLEM_DEVELOPER_MESSAGE.getLabel(), this.getRootElement(), XPathConstants.STRING);

      componentId = (String)ConversionFacade.getXPath().evaluate(Elements.ATTRIBUTEPROBLEM_COMPONENT_ID.getLabel(), this.getRootElement(), XPathConstants.STRING);
      definingType = (String)ConversionFacade.getXPath().evaluate(Elements.ATTRIBUTEPROBLEM_DEFINING_TYPE.getLabel(), this.getRootElement(), XPathConstants.STRING);
      definingTypeDisplayLabel = (String)ConversionFacade.getXPath().evaluate(Elements.ATTRIBUTEPROBLEM_DEFINING_TYPE_DISPLAY_LABEL.getLabel(), this.getRootElement(), XPathConstants.STRING);
      attributeName = (String)ConversionFacade.getXPath().evaluate(Elements.ATTRIBUTEPROBLEM_ATTRIBUTE_NAME.getLabel(), this.getRootElement(), XPathConstants.STRING);
      attributeId = (String)ConversionFacade.getXPath().evaluate(Elements.ATTRIBUTEPROBLEM_ATTRIBUTE_ID.getLabel(), this.getRootElement(), XPathConstants.STRING);
      attributeDisplayLabel = (String)ConversionFacade.getXPath().evaluate(Elements.ATTRIBUTEPROBLEM_ATTRIBUTE_DISPLAYLABEL.getLabel(), this.getRootElement(), XPathConstants.STRING);
    }
    catch(XPathExpressionException ex)
    {
      String errString = "Improper XPath expression: "+ex.getMessage();
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), errString, ex);
    }

    return ConversionFacade.createDynamicAttributeProblemDTO(
        type, componentId,
        definingType, definingTypeDisplayLabel,
        attributeName, attributeId, attributeDisplayLabel,
        localizedMessage, developerMessage);
  }


}
