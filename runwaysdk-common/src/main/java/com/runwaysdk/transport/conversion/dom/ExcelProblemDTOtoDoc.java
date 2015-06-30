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
package com.runwaysdk.transport.conversion.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.runwaysdk.dataaccess.io.ExcelProblemDTO;

public class ExcelProblemDTOtoDoc extends RunwayProblemDTOtoDoc
{
  public ExcelProblemDTOtoDoc(ExcelProblemDTO problem, Document document)
  {
    super(problem, document);
  }
  
  @Override
  protected ExcelProblemDTO getRunwayProblemDTO()
  {
    return (ExcelProblemDTO) super.getRunwayProblemDTO();
  }
  
  @Override
  protected Element buildRootElement()
  {
    Document document = this.getDocument();
    Element excelProblem = document.createElement(Elements.EXCELPROBLEM_DTO.getLabel());
    return excelProblem;
  }

  @Override
  public Element populate()
  {
    Document document = this.getDocument();
    Element runwayProblem = super.populate();
    
    Element rowNumber = document.createElement(Elements.EXCELPROBLEM_ROWNUMBER.getLabel());
    rowNumber.appendChild(document.createTextNode(Integer.toString(this.getRunwayProblemDTO().getRowNumber())));
    runwayProblem.appendChild(rowNumber);
    
    Element column = document.createElement(Elements.EXCELPROBLEM_COLUMN.getLabel());
    column.appendChild(document.createTextNode(this.getRunwayProblemDTO().getColumn()));
    runwayProblem.appendChild(column);
    
    return runwayProblem;
  }
}
