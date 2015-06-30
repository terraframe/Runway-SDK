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

import com.runwaysdk.dataaccess.MdSessionDAOIF;

public abstract class SessionDTOBaseGenerator extends TransientDTOBaseGenerator
{
  /**
   * @param mdEntity
   */
  public SessionDTOBaseGenerator(MdSessionDAOIF mdSessionIF)
  {
    super(mdSessionIF);  
  }

  @Override
  protected void write()
  {
    // Write the getter and setters for the attributes
    super.write();

    // write the apply method
    writeApply();
    
    // write the delete method
    writeDelete();
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
    getWriter().writeLine("getRequest().createSessionComponent(this);");
    getWriter().closeBracket();
    getWriter().writeLine("else");
    getWriter().openBracket();
    getWriter().writeLine("getRequest().update(this);");
    getWriter().closeBracket();  
    getWriter().closeBracket();
  } 


  /**
   * Write the method to unlock the current object 
   */
  protected void writeDelete()
  {
    getWriter().writeLine("public void delete()");
    getWriter().openBracket();
    getWriter().writeLine("getRequest().delete(this.getId());");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }
  
}
