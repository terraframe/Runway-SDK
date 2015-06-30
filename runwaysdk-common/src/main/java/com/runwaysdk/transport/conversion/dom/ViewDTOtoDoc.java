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

import com.runwaysdk.business.ViewDTO;

public class ViewDTOtoDoc extends SessionComponentDTOtoDoc
{
  /**
   * Constructor that allows a node to act as a parent to the created 
   * 
   * @param viewDTO
   * @param document
   * @param convertMetaData
   */
  public ViewDTOtoDoc(ViewDTO viewDTO, Document document, boolean convertMetaData)
  {
    super(viewDTO, document, document.createElement(Elements.VIEW_DTO.getLabel()), convertMetaData);
  }

  @Override
  protected ViewDTO getComponentDTO()
  {
    return (ViewDTO) super.getComponentDTO();
  }
}
