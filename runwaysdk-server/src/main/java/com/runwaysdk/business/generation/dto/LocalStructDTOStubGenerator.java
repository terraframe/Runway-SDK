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
package com.runwaysdk.business.generation.dto;

import com.runwaysdk.business.LocalStructDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.dataaccess.MdLocalStructDAOIF;

public class LocalStructDTOStubGenerator extends StructDTOStubGenerator
{

  public LocalStructDTOStubGenerator(MdLocalStructDAOIF mdLocalStruct)
  {
    super(mdLocalStruct);
  }
  
  @Override
  protected void writeStructConstructor()
  {
    getWriter().writeLine("/**");
    getWriter().writeLine("* Copy Constructor: Duplicates the values and attributes of the given LocalStructDTO into a new DTO.");
    getWriter().writeLine("* ");
    getWriter().writeLine("* @param localStructDTO The LocalStructDTO to duplicate");
    getWriter().writeLine("* @param clientRequest The clientRequest this DTO should use to communicate with the server.");
    getWriter().writeLine("*/");    
    getWriter().writeLine("protected " + getFileName() + "(" + LocalStructDTO.class.getName() + " localStructDTO, " + ClientRequestIF.class.getName() + " clientRequest)");
    getWriter().openBracket();
    getWriter().writeLine("super(localStructDTO, clientRequest);");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }
}
