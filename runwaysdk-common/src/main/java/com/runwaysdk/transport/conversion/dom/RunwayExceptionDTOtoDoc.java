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

import com.runwaysdk.RunwayExceptionDTO;

public class RunwayExceptionDTOtoDoc 
{
  /**
   * The constructing document.
   */
  private Document document;

  private RunwayExceptionDTO runwayExceptionDTO;

  /**
   * Constructor to set the constructing document.
   * 
   * @param document
   */
  public RunwayExceptionDTOtoDoc(RunwayExceptionDTO runwayExceptionDTO, Document document)
  {
    this.document = document;
    this.runwayExceptionDTO = runwayExceptionDTO;
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
    
    Element runwayException = document.createElement(Elements.RUNWAYEXCEPTION_DTO.getLabel());

    Element type = document.createElement(Elements.RUNWAYEXCEPTION_DTO_TYPE.getLabel());
    type.appendChild(document.createTextNode(this.runwayExceptionDTO.getType()));
    runwayException.appendChild(type);

    Element localizedMessage = document.createElement(Elements.RUNWAYEXCEPTION_LOCALIZED_MESSAGE.getLabel());
    localizedMessage.appendChild(document.createTextNode(this.runwayExceptionDTO.getLocalizedMessage()));
    runwayException.appendChild(localizedMessage);
    
    Element devMessage = document.createElement(Elements.RUNWAYEXCEPTION_DEVELOPER_MESSAGE.getLabel());
    devMessage.appendChild(document.createTextNode(this.runwayExceptionDTO.getDeveloperMessage()));
    runwayException.appendChild(devMessage);
    
    return runwayException;
  }
}
