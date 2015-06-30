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

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.configuration.RunwayConfigurationException;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.DeployProperties;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.RunwayProperties;
import com.runwaysdk.constants.ServerProperties;
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
 * @author Richard Rowlands, Eric Gunzke
 */
public abstract class AbstractCompiler
{
  public static final String COMPILER_LOG           = "Compiler";

  private Logger logger = LoggerFactory.getLogger(AbstractCompiler.class);

  /**
   * Contains the args for this particular execution of the compiler
   */
  protected Arguments        arguments;

  protected String           runwaysdkCommonJarPath = LocalProperties.getCommonLib() + "/" + Constants.RUNWAYSDK_COMMON_JAR;

  protected String           runwaysdkServerJarPath = LocalProperties.getServerLib() + "/" + Constants.RUNWAYSDK_SERVER_JAR;

  protected String           runwaysdkClientJarPath = LocalProperties.getClientLib() + "/" + Constants.RUNWAYSDK_CLIENT_JAR;

  protected boolean          canCompileClient       = true;

  /**
   * Creates the Arguments object and sets the default values
   */
  protected AbstractCompiler()
  {
    // Ensure existence of some paths we're going to need
    ensureExistence(LocalProperties.getSrcRoot());
    ensureExistence(LocalProperties.getGenRoot());
    ArrayList<String> props = new ArrayList<String>();
    if (!ensureExistence(LocalProperties.getCommonSrc())) {
      props.add("common.src");
    }
    if (!ensureExistence(LocalProperties.getClientSrc())) {
      props.add("client.src");
    }
    if (!ensureExistence(LocalProperties.getServerSrc())) {
      props.add("server.src");
    }
    if (!ensureExistence(LocalProperties.getClientGenSrc())) {
      props.add("client.gen.src");
    }
    if (!ensureExistence(LocalProperties.getCommonGenSrc())) {
      props.add("common.gen.src");
    }
    if (!ensureExistence(LocalProperties.getServerGenSrc())) {
      props.add("server.gen.src");
    }
    if (!ensureExistence(LocalProperties.getClientGenBin())) {
      props.add("client.gen.bin");
    }
    if (!ensureExistence(LocalProperties.getCommonGenBin())) {
      props.add("common.gen.bin");
    }
    if (!ensureExistence(LocalProperties.getServerGenBin())) {
      props.add("server.gen.bin");
    }
    if (props.size() != 0) {
      throw new RunwayConfigurationException("Unable to generate source. Required configuration properties [" + StringUtils.join(props, ", ") + "] in local.properties do not exist.");
    }
    
    FileFilter fileFilter = new FileFilter()
    {
      public boolean accept(File pathname)
      {
        if (pathname.getAbsolutePath().endsWith(".jar") || pathname.isDirectory())
        {
          return true;
        }
        else
        {
          return false;
        }
      }
    };
    
    arguments = new Arguments();
    
    arguments.common.setDestination(LocalProperties.getCommonGenBin());

    // Add all of the custom classpath entries
    for (String path : LocalProperties.getLocalClasspath())
    {
      arguments.common.addClasspath(path);
    }
    
    // We need to add the runway to the classpath, either in a jar or directly
    if (LocalProperties.isRunwayEnvironment())
    {
      // Check to make sure Runway is compiled, otherwise we get an unhelpful
      // error.
      ArrayList<String> uncompiled = new ArrayList<String>();
      File commonClass = new File(RunwayProperties.getRunwayCommonBin() + "/com/runwaysdk/business/BusinessDTO.class");
      if (!commonClass.exists())
      {
        uncompiled.add("runwaysdk-common");
      }
      
      File clientClass = new File(RunwayProperties.getRunwayClientBin() + "/com/runwaysdk/controller/DTOFacade.class");
      if (!clientClass.exists())
      {
        uncompiled.add("runwaysdk-client");
      }
      
      File serverClass = new File(RunwayProperties.getRunwayServerBin() + "/com/runwaysdk/business/Business.class");
      if (!serverClass.exists())
      {
        uncompiled.add("runwaysdk-server");
      }
      
      if (uncompiled.size() > 0)
      {
        throw new CoreException("This project has declared a runway environment, yet the following runway projects have not been compiled [" + StringUtils.join(uncompiled, ", ") + "]. First compile these projects, then try your operation again.");
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
    if (LocalProperties.isDevelopEnvironment())
    {
      String localBin = LocalProperties.getLocalBin();
      if (localBin != null)
      {
        arguments.common.addClasspath(localBin);
      }
      
      arguments.common.addSourceDir(LocalProperties.getCommonSrc());
      arguments.client.addSourceDir(LocalProperties.getClientSrc());
      arguments.server.addSourceDir(LocalProperties.getServerSrc());
    }

    // Add the Project's Dependency Classpath
    if (!LocalProperties.useMavenLib())
    {
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
    else
    {
      List<String> commonClasspath;
      List<String> clientClasspath;
      List<String> serverClasspath;

      commonClasspath = CommonProperties.getCommonClasspath();
      serverClasspath = ServerProperties.getServerClasspath();

      clientClasspath = ServerProperties.getClientClasspath();
      if (clientClasspath == null)
      {
        logger.warn("Unable to compile client, client jar not on classpath.");
        canCompileClient = false;
      }
      else
      {
        Iterator<String> clI = clientClasspath.iterator();
        while (clI.hasNext())
        {
          arguments.client.addClasspath(clI.next());
        }
      }

      Iterator<String> cI = commonClasspath.iterator();
      while (cI.hasNext())
      {
        arguments.common.addClasspath(cI.next());
      }
      Iterator<String> sI = serverClasspath.iterator();
      while (sI.hasNext())
      {
        arguments.server.addClasspath(sI.next());
      }
    }

    arguments.common.addClasspath(LocalProperties.getCommonGenBin());
    if (LocalProperties.isDeployedInContainer())
    {
      String containerLib = DeployProperties.getContainerLib();
      if (containerLib != null && !containerLib.equals("") && new File(containerLib).exists()) {
        for (File lib : FileIO.listFilesRecursively(new File(containerLib), fileFilter))
        {
          arguments.common.addClasspath(lib.getAbsolutePath());
        }
      }
      else {
        String servletAPI = DeployProperties.getContainerServletAPIJarLocation();
        if (servletAPI != null && !servletAPI.equals("") && new File(servletAPI).exists()) {
          logger.warn("The deploy.properties configuration option 'deploy.servlet.jar' is deprecated in favor of 'container.lib'");
          arguments.common.addClasspath(servletAPI);
        }
        else {
          logger.error("Unable to add provided container jars to compilation classpath, deploy.properties configuration key 'container.lib' either does not exist or points to a file/directory that does not exist. This is likely to cause the compilation to fail.");
        }
      }
    }

    arguments.client.setDestination(LocalProperties.getClientGenBin());
    arguments.client.addClasspath(LocalProperties.getClientGenBin());
    arguments.client.setDependency(arguments.common);

    arguments.server.setDestination(LocalProperties.getServerGenBin());
    arguments.server.addClasspath(LocalProperties.getServerGenBin());
    arguments.server.setDependency(arguments.common);
  }

  /**
   * Compiles the sources for the given {@link MdTypeDAO}s
   * 
   * @param types
   *          A collection of types to compile
   * @throws CompilerException
   *           if compilation fails
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
  
  private static boolean ensureExistence(String path) {
    if (path == null) {
      return false;
    }
    
    File file = new File(path);
    
    if (file.exists()) {
      return true;
    }
    
    File parent = file.getParentFile();
    if (parent.exists()) {
      file.mkdir();
      return true;
    }
    
    return false;
  }

  /**
   * Configures the compiler to produce no output then compiles all generated
   * sources. This can be useful to check if a change will break existing code.
   * 
   * @throws CompilerException
   *           if compilation fails
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
   * @throws CompilerException
   *           if compilation fails
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
   * @throws CompilerException
   *           if compilation fails
   */
  abstract void execute();
}
