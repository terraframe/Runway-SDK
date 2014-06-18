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
package com.runwaysdk.business.generation;

import com.runwaysdk.business.MutableWithStructs;
import com.runwaysdk.business.Struct;
import com.runwaysdk.constants.StructInfo;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;


public class StructBaseGenerator extends EntityBaseGenerator
{
  public StructBaseGenerator(MdStructDAOIF mdStructIF)
  {
    super(mdStructIF);
  }

  @Override
  protected String getExtends(MdClassDAOIF parent)
  {
    return Struct.class.getName();
  }

  @Override
  protected void addConstructor()
  {
    String baseTypeName = this.getBaseClassName();

    getWriter().writeLine("public " + baseTypeName + "()");
    getWriter().openBracket();
    getWriter().writeLine("super();");
    getWriter().closeBracket();
    getWriter().writeLine("");

    getWriter().writeLine("public " + baseTypeName + "(" + MutableWithStructs.class.getName() + " component, String structName)");
    getWriter().openBracket();
    getWriter().writeLine("super(component, structName);");
    getWriter().closeBracket();
    getWriter().writeLine("");

    getWriter().writeLine("public static " + this.getSubClassName() + " get(String id)");
    getWriter().openBracket();
    getWriter().writeLine("return (" + this.getSubClassName() + ") " + StructInfo.CLASS + ".get(id);");
    getWriter().closeBracket();
    getWriter().writeLine("");

    getWriter().writeLine("public static " + this.getSubClassName() + " getByKey(String key)");
    getWriter().openBracket();
    getWriter().writeLine("return (" + this.getSubClassName() + ") " + StructInfo.CLASS + ".get(CLASS, key);");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }
}
