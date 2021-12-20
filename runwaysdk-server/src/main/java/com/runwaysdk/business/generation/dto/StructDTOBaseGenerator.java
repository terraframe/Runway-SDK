/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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

import com.runwaysdk.business.StructDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.EntityDTOInfo;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;

public class StructDTOBaseGenerator extends EntityDTOBaseGenerator
{
  /**
   * @param mdStruct
   */
  public StructDTOBaseGenerator(MdStructDAOIF mdStruct)
  {
    super(mdStruct);
  }
  
  /**
   * The general algorithm used to generate the DTOs
   */
  public void go(boolean forceRegeneration)
  {
    // Only in the runway development environment do we ever generate business classes for metadata.
    if (this.getMdTypeDAOIF().isSystemPackage() && !LocalProperties.isRunwayEnvironment())
    { 
      return;
    }
    
    // Do not generate a DTO if this class is not published.
    if (!this.getMdTypeDAOIF().isPublished())
    {
      return;
    }
    else
    {
      super.go(forceRegeneration);
    }
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.business.generation.DTOGenerator#write()
   */
  @Override
  protected void writeConcrete()
  {
    writeGet();
  }

  /**
   * Write the static get method for the generated BusinessDTO
   */
  private void writeGet()
  {
    getWriter().writeLine("public static " + this.getDTOStubClassName() + " get(" + ClientRequestIF.class.getName() + " clientRequest, String oid)");
    getWriter().openBracket();
    getWriter().writeLine(EntityDTOInfo.CLASS + " dto = ("+EntityDTOInfo.CLASS+")clientRequest.get(oid);");
    getWriter().writeLine("");    
    getWriter().writeLine("return (" + this.getDTOStubClassName() + ") dto;");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }
    
  /* (non-Javadoc)
   * @see com.runwaysdk.business.generation.DTOGenerator#getExtends()
   */
  @Override
  protected String getExtends(MdClassDAOIF parent)
  {
    return StructDTO.class.getName();
  }

  @Override
  protected void writeConstructor()
  {
    super.writeConstructor();
    
    getWriter().writeLine("/**");
    getWriter().writeLine("* Copy Constructor: Duplicates the values and attributes of the given StructDTO into a new DTO.");
    getWriter().writeLine("* ");
    getWriter().writeLine("* @param structDTO The StructDTO to duplicate");
    getWriter().writeLine("* @param clientRequest The clientRequest this DTO should use to communicate with the server.");
    getWriter().writeLine("*/");    
    getWriter().writeLine("protected " + getFileName() + "(" + getExtends(null) + " structDTO, " + ClientRequestIF.class.getName() + " clientRequest)");
    getWriter().openBracket();
    getWriter().writeLine("super(structDTO, clientRequest);");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }
  
  /**
   * Write the apply() method that creates the DTO
   * if it is a new instance or updates it if not.
   */
  protected void writeApply()
  {
    getWriter().writeLine("public void apply()");
    getWriter().openBracket();
    getWriter().writeLine("if(isNewInstance())");
    getWriter().openBracket();
    getWriter().writeLine("getRequest().createStruct(this);");
    getWriter().closeBracket();
    getWriter().writeLine("else");
    getWriter().openBracket();
    getWriter().writeLine("getRequest().update(this);");
    getWriter().closeBracket();  
    getWriter().closeBracket();
  }
}

