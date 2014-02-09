/**
 * 
 */
package com.runwaysdk.business.generation.ontology;

import com.runwaysdk.business.generation.BusinessStubGenerator;
import com.runwaysdk.business.ontology.Term;
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
  
  @Override
  protected void addMethods() {
    super.addMethods();
    
    getWriter().writeLine("/**");
    getWriter().writeLine("  * Specify the root ontology node.");
    getWriter().writeLine("  */");
    getWriter().writeLine("public static " + Term.class.getName() + " getRootNode()");
    getWriter().openBracket();
    getWriter().writeLine("return null;");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }
  
//  @Override
//  protected String getImplements() {
//    return OntologyProvider.class.getName();
//  }
}
