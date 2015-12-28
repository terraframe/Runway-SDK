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
package com.runwaysdk.business.generation;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jdt.internal.compiler.batch.Main;

/**
 * Concrete implementation for programmatic compilation of generated content
 * with the Eclipse Compiler for Java (ECJ).
 * 
 * @author Eric
 */
public class EclipseCompiler extends AbstractCompiler
{
  /**
   * Sets default values for the compiler
   */
  public EclipseCompiler()
  {
    super();
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.business.generation.AbstractCompiler#execute()
   */
  @Override
  protected void execute()
  {
    if (arguments.common.hasSources())
    {
      callECJ(arguments.common.getEclipseArgs());
    }
    if (arguments.server.hasSources())
    {
      callECJ(arguments.server.getEclipseArgs());
    }
    if (arguments.client.hasSources())
    {
      callECJ(arguments.client.getEclipseArgs());
    }
  }
  
  /**
   * Calls the ECJ and wraps any errors in a {@link CompilerException}
   * 
   * @param args Arguments for the compiler
   * @throws CompilerException if compilation fails
   */
  private void callECJ(String args[])
  {
    Log log = LogFactory.getLog(COMPILER_LOG);
    log.trace(Arrays.deepToString(args));
    
    StringWriter output = new StringWriter();
    StringWriter errors = new StringWriter();
    Main compiler = new Main(new PrintWriter(output), new PrintWriter(errors), false);
    
    compiler.compile(args);
    
    String message = errors.toString();

    if (message.length() > 0)
    {
      String error = "Errors found during compilation:\n" + message;
      throw new CompilerException(error, message);
    }    
  }
}
