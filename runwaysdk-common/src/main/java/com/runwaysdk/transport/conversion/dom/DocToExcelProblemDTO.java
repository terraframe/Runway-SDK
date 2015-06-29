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
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.dataaccess.io.ExcelProblemDTO;
import com.runwaysdk.transport.conversion.ConversionFacade;

public class DocToExcelProblemDTO extends DocToRunwayProblemDTO
{
  public DocToExcelProblemDTO(Element rootElement)
  {
    super(rootElement);
  }

  @Override
  public ExcelProblemDTO populate()
  {
    String type = null;
    String localizedMessage = null;
    String developerMessage = null;
    String rowNumber = null;
    String column = null;

    try
    {
      type = (String)ConversionFacade.getXPath().evaluate(Elements.RUNWAYPROBLEM_TYPE.getLabel(), this.getRootElement(), XPathConstants.STRING);
      localizedMessage = (String)ConversionFacade.getXPath().evaluate(Elements.RUNWAYPROBLEM_LOCALIZED_MESSAGE.getLabel(), this.getRootElement(), XPathConstants.STRING);
      developerMessage = (String)ConversionFacade.getXPath().evaluate(Elements.RUNWAYPROBLEM_DEVELOPER_MESSAGE.getLabel(), this.getRootElement(), XPathConstants.STRING);

      rowNumber = (String)ConversionFacade.getXPath().evaluate(Elements.EXCELPROBLEM_ROWNUMBER.getLabel(), this.getRootElement(), XPathConstants.STRING);
      column = (String)ConversionFacade.getXPath().evaluate(Elements.EXCELPROBLEM_COLUMN.getLabel(), this.getRootElement(), XPathConstants.STRING);
    }
    catch(XPathExpressionException ex)
    {
      String errString = "Improper XPath expression: "+ex.getMessage();
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), errString, ex);
    }

    return new ExcelProblemDTO(type, localizedMessage, developerMessage, Integer.parseInt(rowNumber), column);
  }
}
