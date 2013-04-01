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

import java.util.Arrays;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.tools.ajc.Main;

import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.RunwayProperties;

/**
 * Concrete implementation for programmatic compilation of generated content
 * with AspectJ, including compile-time weaving.
 *
 * @author Eric
 */
public class AspectJCompiler extends AbstractCompiler
{
  private LinkedList<String> fails;
  private LinkedList<String> errors;
  private LinkedList<String> warnings;
  private LinkedList<String> infos;

  /**
   * Sets default values for the compiler (most notably -aspectpath)
   */
  protected AspectJCompiler()
  {
    super();
    fails = new LinkedList<String>();
    errors = new LinkedList<String>();
    warnings = new LinkedList<String>();
    infos = new LinkedList<String>();

    if (LocalProperties.isRunwayEnvironment())
    {
      arguments.common.addAspectpath(RunwayProperties.getRunwayCommonBin());
      arguments.client.addAspectpath(RunwayProperties.getRunwayClientBin());
      arguments.server.addAspectpath(RunwayProperties.getRunwayServerBin());
    }
    else
    {
      arguments.common.addAspectpath(runwaysdkCommonJarPath);
      arguments.client.addAspectpath(runwaysdkClientJarPath);
      arguments.server.addAspectpath(runwaysdkServerJarPath);

      String[] aspectjPaths = LocalProperties.aspectJPath();
      for (String aspectjPath: aspectjPaths)
      {
        arguments.server.addAspectpath(aspectjPath);
      }

    }
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.business.generation.AbstractCompiler#execute()
   */
  protected void execute()
  {
    if (arguments.common.hasSources())
    {
      callAJC(arguments.common.getAspectJArgs());
    }
    if (arguments.server.hasSources())
    {
      callAJC(arguments.server.getAspectJArgs());
    }
    if (arguments.client.hasSources())
    {
      callAJC(arguments.client.getAspectJArgs());
    }
  }

  /**
   * Calls the AspectJ Compiler and wraps any errors or failures in a
   * {@link CompilerException}
   *
   * @param args Arguments for the compiler
   * @throws CompilerException if compilation fails
   */
  private void callAJC(String args[])
  {
    Log log = LogFactory.getLog(COMPILER_LOG);
    log.trace(Arrays.deepToString(args));

    fails.clear();
    errors.clear();
    warnings.clear();
    infos.clear();

    if (0<Main.bareMain(args, false, fails, errors, warnings, infos))
    {
      // We have errors
      String message = new String();
      for (String error : errors)
      {
        message += '\n' + error;
      }
      for (String fail : fails)
      {
        message += '\n' + fail;
      }
      throw new CompilerException("Errors found during compilation:" + message, message);
    }
  }
}
