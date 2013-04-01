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

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.dataaccess.MdElementDAOIF;
import com.runwaysdk.dataaccess.MdParameterDAOIF;
import com.runwaysdk.dataaccess.metadata.MdParameterDAO;
import com.runwaysdk.dataaccess.metadata.Type;

public abstract class ElementBaseGenerator extends EntityBaseGenerator
{
  public ElementBaseGenerator(MdElementDAOIF mdElement)
  {
    super(mdElement);
  }

  /**
   * Generates the static lock and unlock methods.
   */
  @Override
  protected void addStaticMethods()
  {    
    List<MdParameterDAOIF> list = new LinkedList<MdParameterDAOIF>();
    Type returnType = new Type(this.getSubClassName());
    MdParameterDAO id = GenerationUtil.getMdParameterId();
    list.add(0, id);

    //Write the lock method
    addStaticMethod("lock", returnType, list);

    //Write the unlock method
    addStaticMethod("unlock", returnType, list);
  }

  protected void addStaticMethod(String methodName, Type returnType, List<MdParameterDAOIF> list)
  {
    addStaticMethod(methodName, returnType, list, "");
  }
  
  protected void addStaticMethod(String methodName, Type returnType, List<MdParameterDAOIF> list, String throwsStatement)
  {
    String parameters = GenerationUtil.buildBusinessParameters(list, true);  
    String parameterNames = GenerationUtil.buildParameterArray(list.subList(1, list.size()));
    String modifier = GenerationUtil.getModifier(true, false);
    
    getWriter().writeLine("public " + modifier + returnType.getType() + " " + methodName + "(" + parameters + ")" + throwsStatement);
    getWriter().openBracket();
    getWriter().writeLine(this.getSubClassName() + " _instance = " + this.getSubClassName() + ".get(id);");
    getWriter().writeLine("_instance." + methodName + "(" + parameterNames + ");");
    getWriter().writeLine("");
    getWriter().writeLine("return _instance;");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }
}
