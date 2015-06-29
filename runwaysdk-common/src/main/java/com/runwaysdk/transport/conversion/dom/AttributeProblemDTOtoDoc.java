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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.runwaysdk.business.AttributeProblemDTO;

public class AttributeProblemDTOtoDoc extends RunwayProblemDTOtoDoc
{

  /**
   * Constructor to set the constructing document.
   * 
   * @param attributeProblemDTO
   * @param document
   */
  public AttributeProblemDTOtoDoc(AttributeProblemDTO attributeProblemDTO, Document document)
  {
    super(attributeProblemDTO, document);
  }

  protected AttributeProblemDTO getRunwayProblemDTO()
  {
    return (AttributeProblemDTO)super.getRunwayProblemDTO();
  }

  @Override
  protected Element buildRootElement()
  {
    Document document = this.getDocument();
    Element runwayProblem = document.createElement(Elements.ATTRIBUTEPROBLEM_DTO.getLabel());
    return runwayProblem;
  }

  public Element populate()
  {
    Document document = this.getDocument();
   
    Element runwayProblem = super.populate();
    
    Element componentId = document.createElement(Elements.ATTRIBUTEPROBLEM_COMPONENT_ID.getLabel());
    componentId.appendChild(document.createTextNode(this.getRunwayProblemDTO().getComponentId()));
    runwayProblem.appendChild(componentId);

    Element definingType = document.createElement(Elements.ATTRIBUTEPROBLEM_DEFINING_TYPE.getLabel());
    definingType.appendChild(document.createTextNode(this.getRunwayProblemDTO().getDefiningType()));
    runwayProblem.appendChild(definingType);

    Element displayLabel = document.createElement(Elements.ATTRIBUTEPROBLEM_DEFINING_TYPE_DISPLAY_LABEL.getLabel());
    displayLabel.appendChild(document.createTextNode(this.getRunwayProblemDTO().getDefiningTypeDisplayLabel()));
    runwayProblem.appendChild(displayLabel);
    
    Element attributeName = document.createElement(Elements.ATTRIBUTEPROBLEM_ATTRIBUTE_NAME.getLabel());
    attributeName.appendChild(document.createTextNode(this.getRunwayProblemDTO().getAttributeName()));
    runwayProblem.appendChild(attributeName);

    Element attributeDisplayLabel = document.createElement(Elements.ATTRIBUTEPROBLEM_ATTRIBUTE_DISPLAYLABEL.getLabel());
    attributeDisplayLabel.appendChild(document.createTextNode(this.getRunwayProblemDTO().getAttributeDisplayLabel()));
    runwayProblem.appendChild(attributeDisplayLabel);
    
    return runwayProblem;
  }
  
}
