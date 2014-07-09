/**
 * 
 */
package com.runwaysdk.business.generation.ontology;

import com.runwaysdk.business.generation.BusinessStubGenerator;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;

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
public class TermStubGenerator extends BusinessStubGenerator
{
  /**
   * @param mdBusinessIF
   */
  public TermStubGenerator(MdBusinessDAOIF mdBusinessIF)
  {
    super(mdBusinessIF);
  }
  
//  @Override
//  public void addAttributes()
//  {
//    super.addAttributes();
//    
//    getWriter().writeLine("private static final String ROOT_KEY = \"ROOT\"");
//  }
//  
  @Override
  protected void addMethods() {
    super.addMethods();
    
    String typeName = this.getMdTypeDAOIF().getTypeName();
    
    // TODO: add getRoot as a default MdMethod (in MdTermDAO) added to all Terms.
    // We can't put this in the base generator because MdMethods also generate this in the base as well.
    // We want this method to be generated in the stub for 2 reasons:
    //   1) This should be an MdMethod, and MdMethods have to be overridden in the stub. This is a default implementation of that MdMethod.
    //   2) Even if it isn't an MdMethod, its still a convenience method for Term.getRoot, because it generates it with the CLASS parameter.
    getWriter().writeLine("public static " + typeName + " getRoot()");
    getWriter().openBracket();
    getWriter().writeLine("return (" + typeName + ") Term.getRoot(" + typeName + ".CLASS);");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }
  
//  @Override
//  protected String getImplements() {
//    return OntologyProvider.class.getName();
//  }
}
