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

import java.io.File;
import java.io.FileFilter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.runwaysdk.business.generation.maven.MavenClasspathBuilder;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.DeployProperties;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.RunwayProperties;
import com.runwaysdk.dataaccess.CoreException;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.generation.CommonMarker;
import com.runwaysdk.util.FileIO;

/**
 * The abstract root of programmatic compilers, AbstractCompiler contains all
 * code that is common to different compiler implementations (creating
 * {@link Arguments}, setting destination, etc.)
 *
 * @author Eric
 */
public abstract class AbstractCompiler
{
  public static final String COMPILER_LOG = "Compiler";
  
  /**
   * Contains the args for this particular execution of the compiler
   */
  protected Arguments arguments;

  protected String runwaysdkCommonJarPath = LocalProperties.getCommonLib()+"/"+Constants.RUNWAYSDK_COMMON_JAR;

  protected String runwaysdkServerJarPath = LocalProperties.getServerLib()+"/"+Constants.RUNWAYSDK_SERVER_JAR;

  protected String runwaysdkClientJarPath = LocalProperties.getClientLib()+"/"+Constants.RUNWAYSDK_CLIENT_JAR;

  /**
   * Creates the Arguments object and sets the default values
   */
  protected AbstractCompiler()
  {
    arguments = new Arguments();

    arguments.common.setDestination(LocalProperties.getCommonBin());

    // Add all of the custom classpath entries
    for (String path : LocalProperties.getLocalClasspath())
    {
      arguments.common.addClasspath(path);
    }

    // We need to add the runway to the classpath, either in a jar or directly
    if (LocalProperties.isRunwayEnvironment())
    {
      // Check to make sure Runway is compiled, otherwise we get an unhelpful error.
      String uncompiled = "";
      File commonClass = new File(RunwayProperties.getRunwayCommonBin() + "/com/runwaysdk/business/BusinessDTO.class");
      if (!commonClass.exists()) {
        uncompiled = "runwaysdk-common";
      }
      File clientClass = new File(RunwayProperties.getRunwayClientBin() + "/com/runwaysdk/controller/DTOFacade.class");
      if (!clientClass.exists()) {
        uncompiled = uncompiled + " runwaysdk-client";
      }
      File serverClass = new File(RunwayProperties.getRunwayServerBin() + "/com/runwaysdk/business/Business.class");
      if (!serverClass.exists()) {
        uncompiled = uncompiled + " runwaysdk-server";
      }
      if (!uncompiled.equals("")) {
        throw new CoreException("This project has declared a runway environment, yet the following runway projects have not been compiled: " + uncompiled + ". First compile these projects, then try your operation again.");
      }
      
      arguments.common.addClasspath(RunwayProperties.getRunwayCommonBin());
      arguments.server.addClasspath(RunwayProperties.getRunwayServerBin());
      arguments.client.addClasspath(RunwayProperties.getRunwayClientBin());
      arguments.common.addClasspath(LocalProperties.getLocalBin());
    }
    else if (!LocalProperties.useMavenLib())
    {
      arguments.common.addClasspath(runwaysdkCommonJarPath);
      arguments.server.addClasspath(runwaysdkServerJarPath);
      arguments.client.addClasspath(runwaysdkClientJarPath);
    }

    // application environments have static classes that must be compiled
    // with the metadata generated classes.
    if(LocalProperties.isDevelopEnvironment())
    {
      arguments.common.addClasspath(LocalProperties.getLocalBin());
      arguments.common.addSourceDir(LocalProperties.getLocalSource()+"/common");
      arguments.client.addSourceDir(LocalProperties.getLocalSource()+"/client");
      arguments.server.addSourceDir(LocalProperties.getLocalSource()+"/server");
    }

    FileFilter fileFilter = new FileFilter()
    {
      public boolean accept(File pathname)
      {
        if (pathname.getAbsolutePath().endsWith(".jar") ||
            pathname.isDirectory())
        {
          return true;
        }
        else
        {
          return false;
        }
      }
    };

    if (!LocalProperties.useMavenLib()) {
      for (File lib : FileIO.listFilesRecursively(new File(LocalProperties.getCommonLib()), fileFilter))
      {
        arguments.common.addClasspath(lib.getAbsolutePath());
      }
      for (File lib : FileIO.listFilesRecursively(new File(LocalProperties.getClientLib()), fileFilter))
      {
        arguments.client.addClasspath(lib.getAbsolutePath());
      }
      for (File lib : FileIO.listFilesRecursively(new File(LocalProperties.getServerLib()), fileFilter))
      {
        arguments.server.addClasspath(lib.getAbsolutePath());
      }
    }
    else {
      String commonPom = LocalProperties.getCommonPom();
      String clientPom = LocalProperties.getClientPom();
      String serverPom = LocalProperties.getServerPom();
      
      List<String> commonClasspath;
      List<String> clientClasspath;
      List<String> serverClasspath;
      
      try {
        if (!commonPom.equals(clientPom) || !commonPom.equals(serverPom) || !clientPom.equals(serverPom)) {
          commonClasspath = MavenClasspathBuilder.getClasspathFromMavenProject(new File(LocalProperties.getCommonPom()), new File(LocalProperties.getLocalRepository()), LocalProperties.isRunwayEnvironment());
          serverClasspath = MavenClasspathBuilder.getClasspathFromMavenProject(new File(LocalProperties.getServerPom()), new File(LocalProperties.getLocalRepository()), LocalProperties.isRunwayEnvironment());
          clientClasspath = MavenClasspathBuilder.getClasspathFromMavenProject(new File(LocalProperties.getClientPom()), new File(LocalProperties.getLocalRepository()), LocalProperties.isRunwayEnvironment());
        }
        else {
          List<String> classpath = MavenClasspathBuilder.getClasspathFromMavenProject(new File(LocalProperties.getCommonPom()), new File(LocalProperties.getLocalRepository()), LocalProperties.isRunwayEnvironment());
          commonClasspath = classpath;
          clientClasspath = classpath;
          serverClasspath = classpath;
        }
      }
      catch (Exception e) {
        throw new CompilerException(e, "Errors occurred while attempting to retrieve the classpath using Maven.");
      }
      
      Iterator<String> cI = commonClasspath.iterator();
      while(cI.hasNext()) {
        arguments.common.addClasspath(cI.next());
      }
      Iterator<String> clI = clientClasspath.iterator();
      while(clI.hasNext()) {
        arguments.client.addClasspath(clI.next());
      }
      Iterator<String> sI = serverClasspath.iterator();
      while(sI.hasNext()) {
        arguments.server.addClasspath(sI.next());
      }
    }
    
    
    arguments.common.addClasspath(LocalProperties.getCommonBin());
    if (LocalProperties.isDeployedInContainer())
    {
    	arguments.common.addClasspath(DeployProperties.getContainerServletAPIJarLocation());
    }

    arguments.client.setDestination(LocalProperties.getClientBin());
    arguments.client.addClasspath(LocalProperties.getClientBin());
    arguments.client.setDependency(arguments.common);


    arguments.server.setDestination(LocalProperties.getServerBin());
    arguments.server.addClasspath(LocalProperties.getServerBin());
    arguments.server.setDependency(arguments.common);
  }

  /**
   * Compiles the sources for the given {@link MdTypeDAO}s
   *
   * @param types A collection of types to compile
   * @throws CompilerException if compilation fails
   */
  protected void compile(Collection<? extends MdTypeDAOIF> types)
  {
    // No need to go further if there is nothing to compile
    if (types.size() == 0)
      return;

    for (MdTypeDAOIF mdType : types)
    {
      for (String source : GenerationManager.getServerFiles(mdType))
      {
        arguments.server.addSourceFile(source);
      }

      for (String source : GenerationManager.getCommonFiles(mdType))
      {
        arguments.common.addSourceFile(source);
      }

      for (String source : GenerationManager.getClientFiles(mdType))
      {
        arguments.client.addSourceFile(source);
      }
    }

    execute();
  }

  /**
   * Configures the compiler to produce no output then compiles all generated
   * sources. This can be useful to check if a change will break existing code.
   *
   * @throws CompilerException if compilation fails
   */
  protected void compileAllNoOutput()
  {
    arguments.common.setDestination("none");
    arguments.server.setDestination("none");
    arguments.client.setDestination("none");

    compileAll();
  }

  /**
   * Compiles all generated content. Can be slow if there are a lot of classes.
   *
   * @throws CompilerException if compilation fails
   */
  protected void compileAll()
  {
    arguments.common.addSourceDir(CommonMarker.BASE_DIRECTORY);
    arguments.common.addSourceDir(CommonMarker.SOURCE_DIRECTORY);
    arguments.server.addSourceDir(ServerMarker.BASE_DIRECTORY);
    arguments.server.addSourceDir(ServerMarker.SOURCE_DIRECTORY);
    arguments.client.addSourceDir(ClientMarker.BASE_DIRECTORY);
    arguments.client.addSourceDir(ClientMarker.SOURCE_DIRECTORY);

    execute();
  }

  /**
   * Converts the arguments into a usable String[] and passes them into the
   * compiler.
   *
   * @throws CompilerException if compilation fails
   */
  protected abstract void execute();
}
