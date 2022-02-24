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
package com.runwaysdk.business.generation;

import com.runwaysdk.constants.SmartExceptionInfo;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdExceptionDAOIF;

public class SmartExceptionBaseGenerator extends LocalizableBaseGenerator
{
  public SmartExceptionBaseGenerator(MdExceptionDAOIF mdException)
  {
    super(mdException);
  }

  @Override
  protected String getExtends(MdClassDAOIF parent)
  {
    if (parent==null)
      return SmartExceptionInfo.CLASS;
    else
      return parent.definesType();
  }
  
  /**
   * Do not generate <code>toString()</code> method for exceptions.  
   * As a result, the inherited <code>toString()</code> method the value of <code>getMessage()</code>
   */
  @Override
  protected void addToString(){}
  
  @Override
  protected void addConstructor()
  {
    super.addConstructor();
    
    String baseTypeName = this.getBaseClassName();
    
    // Constructor(String developerMessage, Exception cause)
    getWriter().writeLine("public " + baseTypeName + "(java.lang.String developerMessage, java.lang.Throwable cause)");
    getWriter().openBracket();
    getWriter().writeLine("super(developerMessage, cause);");
    // We'll need to initialize Reference objects here
    getWriter().closeBracket();
    getWriter().writeLine("");
    
    // Constructor(Exception cause)
    getWriter().writeLine("public " + baseTypeName + "(java.lang.Throwable cause)");
    getWriter().openBracket();
    getWriter().writeLine("super(cause);");
    // We'll need to initialize Reference objects here
    getWriter().closeBracket();
    getWriter().writeLine("");
  }
}
