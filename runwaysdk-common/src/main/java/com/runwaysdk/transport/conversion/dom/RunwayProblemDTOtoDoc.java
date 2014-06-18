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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.business.AttributeProblemDTO;
import com.runwaysdk.business.RunwayProblemDTO;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.dataaccess.io.ExcelProblemDTO;

public abstract class RunwayProblemDTOtoDoc
{
  /**
   * The constructing document.
   */
  private Document document;

  private RunwayProblemDTO runwayProblemDTO;

  /**
   * Constructor to set the constructing document.
   *
   * @param runwayProblemDTO
   * @param document
   */
  public RunwayProblemDTOtoDoc(RunwayProblemDTO runwayProblemDTO, Document document)
  {
    this.runwayProblemDTO = runwayProblemDTO;
    this.document = document;
  }

  /**
   * Returns the destination document.
   *
   * @return
   */
  protected Document getDocument()
  {
    return document;
  }

  protected RunwayProblemDTO getRunwayProblemDTO()
  {
    return this.runwayProblemDTO;
  }

  protected abstract Element buildRootElement();

  public Element populate()
  {
    Document document = this.getDocument();

    Element runwayProblem = this.buildRootElement();

    runwayProblem.appendChild(document.createTextNode(Elements.RUNWAYPROBLEM_DEVELOPER_MESSAGE.getLabel()));

    Element type = document.createElement(Elements.RUNWAYPROBLEM_TYPE.getLabel());
    type.appendChild(document.createTextNode(this.getRunwayProblemDTO().getType()));
    runwayProblem.appendChild(type);

    Element localizedMessage = document.createElement(Elements.RUNWAYPROBLEM_LOCALIZED_MESSAGE.getLabel());
    localizedMessage.appendChild(document.createTextNode(this.getRunwayProblemDTO().getMessage()));
    runwayProblem.appendChild(localizedMessage);

    Element developerMessage = document.createElement(Elements.RUNWAYPROBLEM_DEVELOPER_MESSAGE.getLabel());
    developerMessage.appendChild(document.createTextNode(this.getRunwayProblemDTO().getDeveloperMessage()));
    runwayProblem.appendChild(developerMessage);

    return runwayProblem;
  }

  public static RunwayProblemDTOtoDoc getConverter(RunwayProblemDTO runwayProblemDTO, Document document)
  {
    if (runwayProblemDTO instanceof AttributeProblemDTO)
    {
      return new AttributeProblemDTOtoDoc((AttributeProblemDTO)runwayProblemDTO, document);
    }
    else if (runwayProblemDTO instanceof ExcelProblemDTO)
    {
      return new ExcelProblemDTOtoDoc((ExcelProblemDTO)runwayProblemDTO, document);
    }
    else
    {
      String errMsg = "No Doc converter found for problem type [" + runwayProblemDTO.getClass().getName() + "]";
      CommonExceptionProcessor.processException(ExceptionConstants.ProgrammingErrorException.getExceptionClass(), errMsg);
      return null;
    }
  }
}
