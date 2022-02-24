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
/**
 * 
 */
package com.runwaysdk.business.generation;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import com.runwaysdk.business.generation.Arguments.Configuration;
import com.runwaysdk.configuration.RunwayConfigurationException;

public class SystemJavaCompiler extends AbstractCompiler
{

  /**
   * Sets default values for the compiler
   */
  public SystemJavaCompiler()
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
      callSystemCompiler(arguments.common);
    }
    if (arguments.server.hasSources())
    {
      callSystemCompiler(arguments.server);
    }
    if (arguments.client.hasSources())
    {
      callSystemCompiler(arguments.client);
    }
  }
  
  private void callSystemCompiler(Configuration args)
  {
    List<File> listToCompile = new ArrayList<File>(3000);
    
    // Compile the full tree of all files under these paths
    List<String> sourceDirs = args.getSourceDirs();
    for (String sourceDir : sourceDirs)
    {
      File sourceRoot = new File(sourceDir);
      
      if (sourceRoot.exists())
      {
        compileDirTree(listToCompile, sourceRoot);
      }
    }
    
    // Compile any and all individually specified files
    for (String sFile : args.getSourceFiles())
    {
      File fFile = new File(sFile);
      if (fFile.exists())
      {
        listToCompile.add(fFile);
      }
    }
    
    if (listToCompile.size() > 0)
    {
      StringWriter output = new StringWriter();
      
      JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
      if (compiler == null)
      {
        throw new RunwayConfigurationException("Cannot find System Java Compiler. Ensure that you have installed a JDK (not just a JRE) and configured your JAVA_HOME system variable to point to the according directory.");
      }
      
      StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
      
      Iterable<? extends JavaFileObject> compilationUnits1 = fileManager.getJavaFileObjectsFromFiles(listToCompile);
      Boolean success = compiler.getTask(new PrintWriter(output), fileManager, null, args.getJavac8Options(), null, compilationUnits1).call();
      
      try
      {
        fileManager.close();
      }
      catch (IOException e)
      {
        throw new CompilerException(e);
      }
      
      if (!success)
      {
        // We have errors
        String message = output.toString();
        throw new CompilerException("Errors found during compilation:" + message, message);
      }
    }
  }
  
  private void compileDirTree(List<File> listToCompile, File dir)
  {
    File[] children = dir.listFiles();
    for (File child : children)
    {
      if (child.isFile() && child.getName().endsWith(".java"))
      {
        listToCompile.add(child);
      }
      else if (child.isDirectory())
      {
        compileDirTree(listToCompile, child);
      }
    }
  }

}
