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
package com.runwaysdk.business.generation;

import com.runwaysdk.constants.UtilInfo;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdUtilDAOIF;

public class UtilBaseGenerator extends SessionBaseGenerator
{

  public UtilBaseGenerator(MdUtilDAOIF mdUtil)
  {
    super(mdUtil);
  }

  @Override
  protected String getExtends(MdClassDAOIF parent)
  {
    if (parent==null)
      return UtilInfo.CLASS;
    else
      return parent.definesType();
  }

  protected void addMethods()
  {
    super.addMethods();

    //get for the java class
    getWriter().writeLine("public static " + this.getMdTypeDAOIF().getTypeName() + " get(String id)");
    getWriter().openBracket();
    getWriter().writeLine("return (" + this.getMdTypeDAOIF().getTypeName() + ") " + UtilInfo.CLASS + ".get(id);");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }
  
}
