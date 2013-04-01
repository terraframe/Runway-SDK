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

import com.runwaysdk.ProblemExceptionDTO;
import com.runwaysdk.business.ProblemDTO;
import com.runwaysdk.business.ProblemDTOIF;
import com.runwaysdk.business.RunwayProblemDTO;

public class ProblemExceptionDTOtoDoc
{
  /**
   * The constructing document.
   */
  private Document document;
  
  private ProblemExceptionDTO problemExceptionDTO;
  
  /**
   * Constructor to set the constructing document.
   * 
   * @param document
   */
  public ProblemExceptionDTOtoDoc(ProblemExceptionDTO problemExceptionDTO, Document document)
  {
    this.problemExceptionDTO = problemExceptionDTO;
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
  
  public Element populate()
  {
    Document document = getDocument();
    
    Element problemException = document.createElement(Elements.PROBLEMEXCEPTION_DTO.getLabel());
    Element localizedMessage = document.createElement(Elements.PROBLEMEXCEPTION_LOCALIZED_MESSAGE.getLabel());
    localizedMessage.appendChild(document.createTextNode(this.problemExceptionDTO.getLocalizedMessage()));
    problemException.appendChild(localizedMessage);
    
    for (ProblemDTOIF problemDTOIF : this.problemExceptionDTO.getProblems())
    {
      Element problemElement = document.createElement(Elements.PROBLEM_DTO.getLabel());
      problemException.appendChild(problemElement);

      if (problemDTOIF instanceof ProblemDTO)
      {
        ProblemDTOtoDoc problemDTOtoDoc = new ProblemDTOtoDoc((ProblemDTO)problemDTOIF, document, false);
        problemElement.appendChild(problemDTOtoDoc.populate());
      }
      else if (problemDTOIF instanceof RunwayProblemDTO)
      {
        RunwayProblemDTOtoDoc converter = RunwayProblemDTOtoDoc.getConverter((RunwayProblemDTO)problemDTOIF, document);
        problemElement.appendChild(converter.populate());
      }      
    }
    
    return problemException;
  }
}
