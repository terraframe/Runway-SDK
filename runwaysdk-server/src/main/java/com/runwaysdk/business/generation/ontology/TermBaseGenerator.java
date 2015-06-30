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
package com.runwaysdk.business.generation.ontology;

import com.runwaysdk.business.generation.BusinessBaseGenerator;
import com.runwaysdk.business.ontology.OntologyStrategyIF;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;

public class TermBaseGenerator extends BusinessBaseGenerator
{
  public TermBaseGenerator(MdBusinessDAOIF mdBusinessIF)
  {
    super(mdBusinessIF);
  }

  @Override
  protected String getExtends(MdClassDAOIF parent)
  {
    if (parent == null)
    {
      return Term.class.getName();
    }
    else
    {
      return getParentClass(parent);
    }
  }

  @Override
  protected void addStaticInitializerBlock()
  {
    super.addStaticInitializerBlock();

    getWriter().writeLine("private static final " + OntologyStrategyIF.class.getName() + " strategy;");
    getWriter().writeLine("static ");
    getWriter().openBracket();
    getWriter().writeLine("strategy =  " + Term.class.getName() + ".assignStrategy(\"" + this.getMdTypeDAOIF().getPackage() + "." + this.getSubClassName() + "\");");
    getWriter().closeBracket();
  }

  @Override
  protected void addMethods()
  {
    super.addMethods();

    getWriter().writeLine("public static " + OntologyStrategyIF.class.getName() + " getStrategy()");
    getWriter().openBracket();
    // getWriter().writeLine("return " + Term.class.getName() +
    // ".getStrategy(\"" + this.getMdTypeDAOIF().getPackage() + "." +
    // this.getSubClassName() + "\");");
    getWriter().writeLine("return strategy;");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  // @Override
  // protected void addConstructor()
  // {
  // String baseTypeName = this.getBaseClassName();
  //
  // //Constructor for the base class
  // getWriter().writeLine("public " + baseTypeName + "()");
  // getWriter().openBracket();
  // getWriter().writeLine("super();");
  // addStructInitializers();
  // getWriter().writeLine("");
  // getWriter().closeBracket();
  // getWriter().writeLine("");
  // }
}
