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

import com.runwaysdk.business.EntityQueryDTO;

public abstract class EntityQueryDTOtoDoc extends ClassQueryDTOtoDoc
{

  public EntityQueryDTOtoDoc(EntityQueryDTO queryDTO, Document document)
  {
    super(queryDTO, document);
  }
  
//  @Override
//  public Node populate()
//  {
//    Node root = super.populate();
//    
//    Document document = getDocument();
//
//    // result set
//    Element temp = document.createElement(Elements.QUERY_DTO_RESULT_SET.getLabel());
//    addResultSet(temp);
//    root.appendChild(temp);
//    
//    return root;
//  }

}
