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

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.ViewDTOInfo;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdViewDAOIF;

public class ViewDTOBaseGenerator extends SessionDTOBaseGenerator
{
  /**
   * @param mdEntity
   */
  public ViewDTOBaseGenerator(MdViewDAOIF mdViewIF)
  {
    super(mdViewIF);  
  }

  @Override
  protected String getExtends(MdClassDAOIF parent)
  {
    if (parent == null)
    {
      return ViewDTOInfo.CLASS;
    }
    else
    {
      return getParentClass(parent);
    }
  }


  
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
    getWriter().writeLine(ViewDTOInfo.CLASS + " dto = ("+ViewDTOInfo.CLASS+")clientRequest.get(oid);");
    getWriter().writeLine("");    
    getWriter().writeLine("return (" + this.getDTOStubClassName() + ") dto;");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

}
