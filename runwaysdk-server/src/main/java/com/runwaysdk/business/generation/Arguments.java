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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.ServerProperties;

/**
 * An encapsulation for arguments to programmatic compilers. Supports multiple
 * configurations, which we use to enforce client / server / common separation.
 * 
 * @author Eric
 */
public class Arguments
{
  private final static Logger logger = LoggerFactory.getLogger(Arguments.class);
  
  /**
   * We create 3 separate configurations - common, server, and client
   */
  public Configuration common;

  public Configuration client;

  public Configuration server;

  /**
   * Creates the configurations and sets basic values (destination and
   * classpath)
   */
  public Arguments()
  {
    common = new Configuration();
    server = new Configuration();
    client = new Configuration();

    common.addClasspath(LocalProperties.getCommonGenBin());
    server.addClasspath(LocalProperties.getServerGenBin());
    client.addClasspath(LocalProperties.getClientGenBin());

    common.setDestination(LocalProperties.getCommonGenBin());
    server.setDestination(LocalProperties.getServerGenBin());
    client.setDestination(LocalProperties.getClientGenBin());
  }

  /**
   * Represents a single configuration, which corresponds to a single execution
   * of the compiler
   * 
   * @author Eric
   */
  class Configuration
  {
    private List<String>  sourceFiles;

    private List<String>  sourceDirs;

    private List<String>  classpath;

    private List<String>  aspectpath;

    protected String        destination;

    private String        compliance;

    /**
     * A configuration can be dependent on another. The classpath of the
     * dependency is added to the classpath for this configuration.
     */
    private Configuration dependency;

    private Configuration()
    {
      sourceFiles = new LinkedList<String>();
      sourceDirs = new LinkedList<String>();
      classpath = new LinkedList<String>();
      aspectpath = new LinkedList<String>();

      compliance = ServerProperties.getJavaComplianceLevel();
    }

    /**
     * Add a source file to be compiled
     * 
     * @param source
     */
    public void addSourceFile(String source)
    {
      sourceFiles.add(source);
    }

    /**
     * Add a source directory to be compiled
     * 
     * @param source
     */
    public void addSourceDir(String source)
    {
      sourceDirs.add(source);
    }
    
    public List<String> getSourceDirs()
    {
      return sourceDirs;
    }
    
    public List<String> getSourceFiles()
    {
      return sourceFiles;
    }

    /**
     * Add a source directory to be compiled
     * 
     * @param source
     */
    public void removeSourceDir(String source)
    {
      sourceDirs.remove(source);
    }

    /**
     * A configuration with no sources doesn't need to be compiled.
     * 
     * @return <code>true</code> if this configuration has one or more sources
     */
    public boolean hasSources()
    {
      return !sourceFiles.isEmpty() || !sourceDirs.isEmpty();
    }

    /**
     * Adds a directory or zip/jar to the classpath
     * 
     * @param entry
     */
    public void addClasspath(String entry)
    {
      // Only add the classpath if it's valid
      if (new File(entry).exists())
        classpath.add(entry);
    }

    /**
     * Adds an entry (either a directory or a zip/jar) to the classpath
     * 
     * @param entry
     */
    public void addAspectpath(String entry)
    {
      aspectpath.add(entry);
    }

    /**
     * Sets the destination directory for compiled .class files. Setting this
     * flag to "none" will prevent .class files from being created.
     * 
     * @param destination
     */
    public void setDestination(String destination)
    {
      this.destination = destination;
    }

    /**
     * @return the destination
     */
    public String getDestination()
    {
      return destination;
    }

    /**
     * Sets the compliance level (IE 1.4, 1.5, etc.) for the compiler
     * 
     * @param compliance
     */
    public void setCompliance(String compliance)
    {
      this.compliance = compliance;
    }

    /**
     * Sets the dependency on another Configuration
     * 
     * @param dependency
     */
    public void setDependency(Configuration dependency)
    {
      this.dependency = dependency;
    }

    public Iterable<String> getJavac8Options()
    {
      LinkedList<String> opts = new LinkedList<String>();
      opts.add("-source");
      opts.add(compliance);
      opts.add("-nowarn");
      
      if (destination == null || destination.equals("none"))
      {
        try
        {
          destination = Files.createTempDirectory("javactemp").toFile().getAbsolutePath();
          
          opts.add("-d");
          opts.add(destination);
        }
        catch (IOException e)
        {
          logger.error("IOError when trying to get a temp directory for 'no output' compilation. We may have a compile error coming up real soon here...", e);
        }
      }
      else
      {
        opts.add("-d");
        opts.add(destination);
      }
      
      opts.add("-classpath");
      opts.add(buildClassPath());
      
      return opts;
    }
    
    /**
     * Converts this Configuration object into a String array suitable for
     * feeding into a standard javac compiler (java 8)
     * 
     * @return
     */
    public String[] getJavac8Args()
    {
      LinkedList<String> args = new LinkedList<String>();
      
      if (destination == null || destination.equals("none"))
      {
        try
        {
          destination = Files.createTempDirectory("javactemp").toFile().getAbsolutePath();
          
          args.add("-d");
          args.add(destination);
        }
        catch (IOException e)
        {
          
        }
      }
      else
      {
        args.add("-d");
        args.add(destination);
      }
      
      args.add("-classpath");
      args.add(buildClassPath());
      args.addAll(sourceDirs);
      args.addAll(sourceFiles);
      return args.toArray(new String[args.size()]);
    }
    
    
    
    /**
     * Converts this Configuration object into a String array suitable for
     * feeding into the Eclipse compiler via
     * {@link org.eclipse.jdt.internal.compiler.batch.Main}.
     * 
     * @return
     */
    public String[] getEclipseArgs()
    {
      LinkedList<String> args = new LinkedList<String>();
      args.add("-encoding");
      args.add("utf8");
      args.add("-d");
      args.add(destination);
      args.add("-classpath");
      args.add(buildClassPath());
      args.add("-" + compliance);
      args.add("-nowarn");
      args.addAll(sourceDirs);
      args.addAll(sourceFiles);
      return args.toArray(new String[args.size()]);
    }

    /**
     * Converts this Configuration object into a String array suitable for
     * feeding into the AsjpectJ compiler via {@link org.aspectj.tools.ajc.Main}
     * 
     * @return
     */
    public String[] getAspectJArgs()
    {
      LinkedList<String> args = new LinkedList<String>();
      args.add("-encoding");
      args.add("utf8");
      args.add("-d");
      args.add(destination);
      args.add("-classpath");
      args.add(buildClassPath());
      args.add("-aspectpath");
      args.add(buildAspectPath());
      args.add("-" + compliance);
      args.add("-sourceroots");
      args.add(delimit(sourceDirs));
      args.addAll(sourceFiles);
      return args.toArray(new String[args.size()]);
    }
    
    public Iterable<String> getClassPath()
    {
      if (dependency != null)
      {
        return dependency.classpath;
      }
      
      return null;
    }

    /**
     * Converts the classpath into a single String using the OS-specific path
     * delimiter. Also adds in the entries from the dependency's classpath if
     * there are any.
     * 
     * @return
     */
    private String buildClassPath()
    {
      // combinedPath unions the two classpaths
      Set<String> combinedPath = new TreeSet<String>();
      if (dependency != null)
      {
        for (String path : dependency.classpath)
        {
          combinedPath.add(path);
        }
      }
      combinedPath.addAll(classpath);

      return delimit(combinedPath);
    }

    /**
     * Converts the AspectPath into a single String using the OS-specific path
     * delimiter. Also adds in the entries from the dependency's AspectPath if
     * there are any.
     * 
     * @return
     */
    private String buildAspectPath()
    {
      // combinedPath unions the two apectpaths
      Set<String> combinedPath = new TreeSet<String>();
      if (dependency != null)
      {
        for (String path : dependency.aspectpath)
        {
          combinedPath.add(path);
        }
      }
      combinedPath.addAll(aspectpath);

      return delimit(combinedPath);
    }

    /**
     * Concatenates a collection of Strings with the OS-specific path delimiter
     * 
     * @param iterable
     * @return
     */
    private String delimit(Iterable<String> iterable)
    {
      String result = new String();
      boolean first = true;
      for (String item : iterable)
      {
        if (!first)
        {
          result += File.pathSeparator;
        }
        result += item;
        first = false;
      }
      return result;
    }
  }
}
