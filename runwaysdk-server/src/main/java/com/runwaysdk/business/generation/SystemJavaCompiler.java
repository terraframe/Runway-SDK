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
/**
 * 
 */
package com.runwaysdk.business.generation;



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
public class SystemJavaCompiler extends AbstractCompiler
{

  /**
   * Sets default values for the compiler
   */
  protected SystemJavaCompiler()
  {
    super();
  }
  
  /**
   * @see com.runwaysdk.business.generation.AbstractCompiler#execute()
   */
  @Override
  void execute()
  {
    if (arguments.common.hasSources())
    {
      callSystemCompiler(arguments.common.getEclipseArgs());
    }
    if (arguments.server.hasSources())
    {
      callSystemCompiler(arguments.server.getEclipseArgs());
    }
    if (arguments.client.hasSources())
    {
      callSystemCompiler(arguments.client.getEclipseArgs());
    }
  }
  
  private void callSystemCompiler(String args[]) {
    
    throw new UnsupportedOperationException();
    
//    Log log = LogFactory.getLog(COMPILER_LOG);
//    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
//    
//    StringWriter output = new StringWriter();
//    StringWriter errors = new StringWriter();
//    int compilationResult = compiler.run(null, output, errors, args);
//    
//    String message = errors.toString();
//    
//    log.trace("SystemJavaCompiler output: [" + output.toString() + "].");
//
//    if (message.length() > 0)
//    {
//      String error = "Errors found during compilation:\n" + message;
//      throw new CompilerException(error, message);
//    }
  }

}
