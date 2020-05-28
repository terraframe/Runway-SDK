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
package com.runwaysdk.build;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.ServerProperties;
import com.runwaysdk.dataaccess.CoreException;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.cache.globalcache.ehcache.CacheShutdown;
import com.runwaysdk.dataaccess.io.TimeFormat;
import com.runwaysdk.dataaccess.io.dataDefinition.ImportPluginIF;
import com.runwaysdk.dataaccess.io.dataDefinition.SAXSourceParser;
import com.runwaysdk.dataaccess.io.dataDefinition.VersionHandler;
import com.runwaysdk.dataaccess.io.dataDefinition.VersionHandler.Action;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.resource.ClasspathResource;
import com.runwaysdk.session.Request;

/**
 * This is a command-line Java tool which can be used to build the database. When running this tool, the goal
 * of the tool is to bring your system to a state where it is fully built and patched with all available
 * metadata (from the classpath). This tool is capable of building both Postgres and OrientDB databases.
 * 
 * There are two distinct modes in which the tool runs: install and patch. If neither are specified, the
 * tool will auto-detect which to run as. If clean is specified, the tool will first destroy any existing
 * database.
 * 
 * This tool reads metadata files from the "domain" directory on the classpath, although that location may be
 * changed via the `path` CLI param. Inside this directory you may place files in the `install` or `patch`
 * inner directories, which may contain metadata files which are only run depending on if the tool is doing
 * an install or patch.
 * 
 * Metadata file timestamps may be appended with -ALWAYS to specify that the file will still run, regardless of if it
 * has already been imported or not. 
 * 
 * For a complete list of options you may run this tool with `--help`, or you may view the `main` method in this source. 
 * 
 * @author Richard Rowlands (rrowlands)
 */
public class DatabaseBuilder
{
  private static Logger            logger                                     = LoggerFactory.getLogger(DatabaseBuilder.class);

  public static final List<String> supportedExtensions                        = toList("sql,xml,java");

  public static final String       RUNWAY_METADATA_VERSION_TIMESTAMP_PROPERTY = com.runwaysdk.dataaccess.database.Database.VERSION_TIMESTAMP_PROPERTY;

  private static final String      DATE_PATTEN                                = "\\d{4,}(?:-ALWAYS)?";

  private static final String      NAME_PATTERN                               = "^([A-Za-z_\\-\\d\\.]*)\\((" + DATE_PATTEN + ")\\)([A-Za-z_\\-\\d\\.]*).(?:" + StringUtils.join(supportedExtensions, "|") + ")$";

  public static final String       METADATA_CLASSPATH_LOC                     = "domain";

  /**
   * List of timestamps which have already been imported
   */
  protected Set<Date>                    timestamps;

  /**
   * List of all schema resources in the given location in order from earliest
   * to latest
   */
  protected Set<ClasspathResource>       ordered;

  private List<String>                   extensions;

  private String                         path;

  private Boolean                        ignoreErrors;
  
  private Boolean                        isPatch;
  
  public static Options buildCliOptions()
  {
    Options options = new Options();
    
    options.addOption(Option.builder("install").hasArg().argName("install").longOpt("install").desc("Indicates that we are doing a new install. This value can be set with a true/false value or simply specified without an argument. It is also optional. If you do not provide a install or a patch param this program will automatically detect the best course of action and bring your database up-to-date.").optionalArg(true).build());
    options.addOption(Option.builder("patch").hasArg().argName("patch").longOpt("patch").desc("Indicates that we are doing a patch. This value can be set with a true/false value or simply specified without an argument. It is also optional. If you do not provide a install or a patch param this program will automatically detect the best course of action and bring your database up-to-date.").optionalArg(true).build());
    options.addOption(Option.builder("rootUser").hasArg().argName("rootUser").longOpt("rootUser").desc("The username of the root database user. Only required when bootstrapping.").optionalArg(true).build());
    options.addOption(Option.builder("rootPass").hasArg().argName("rootPass").longOpt("rootPass").desc("The password of the root database user. Only required when bootstrapping.").optionalArg(true).build());
    options.addOption(Option.builder("templateDb").hasArg().argName("templateDb").longOpt("templateDb").desc("The template database to use when creating the application database. Only required when bootstrapping.").optionalArg(true).build());
    options.addOption(Option.builder("extensions").hasArg().argName("extensions").longOpt("extensions").desc("A comma separated list of extensions denoting which schema files to run. If unspecified we will use all supported.").optionalArg(true).build());
    options.addOption(Option.builder("clean").hasArg().argName("clean").longOpt("clean").desc("A boolean parameter denoting whether or not to clean the database and delete all data. Default is false.").optionalArg(true).build());
    options.addOption(Option.builder("path").hasArg().argName("path").longOpt("path").desc("The path (from the root of the classpath) to the location of the metadata files. Defaults to 'domain'").optionalArg(true).build());
    options.addOption(Option.builder("ignoreErrors").hasArg().argName("ignoreErrors").longOpt("ignoreErrors").desc("Ignore errors if one occurs while importing sql. Not recommended for everyday usage.").optionalArg(true).build());
    options.addOption(Option.builder("plugins").hasArg().argName("plugins").longOpt("plugins").desc("A string array of ImportPluginIF implementors.").optionalArg(true).build());
    
    return options;
  }
  
  /**
   * Builds your database such that it includes all the relevant metadata.
   * 
   * @param args
   */
  public static void main(String[] args)
  {
    CommandLineParser parser = new DefaultParser();
    
    Options options = buildCliOptions();
    
    try
    {
      CommandLine line = parser.parse(options, args);
      
      Boolean install;
      Boolean patch;

      install = line.hasOption("install");
      if (install && line.getOptionValue("install") != null && line.getOptionValue("install") != "")
      {
        install = Boolean.valueOf(line.getOptionValue("install"));
        
        if (!install)
        {
          patch = true;
        }
      }
      
      patch = line.hasOption("patch");
      if (patch && line.getOptionValue("patch") != null && line.getOptionValue("patch") != "")
      {
        patch = Boolean.valueOf(line.getOptionValue("patch"));
        
        if (!patch)
        {
          install = true;
        }
      }
      
      String user = line.getOptionValue("rootUser");
      String pass = line.getOptionValue("rootPass");
      String template = line.getOptionValue("templateDb");
      String extensions = line.getOptionValue("extensions");
      String sPlugins = line.getOptionValue("plugins");
      String path = line.getOptionValue("path");
      Boolean ignoreErrors = line.getOptionValue("ignoreErrors") == null || line.getOptionValue("ignoreErrors").equals("") ? false : Boolean.valueOf(line.getOptionValue("ignoreErrors"));
      Boolean clean = line.getOptionValue("clean") == null || line.getOptionValue("clean").equals("") ? false : Boolean.valueOf(line.getOptionValue("clean"));

      List<String> exts = supportedExtensions;
      if (extensions != null && extensions.length() > 0)
      {
        exts = toList(extensions);
      }
      
      List<String> plugins = new ArrayList<String>();
      if (sPlugins != null)
      {
        plugins = toList(sPlugins);
      }
      
      if (patch && clean)
      {
        logger.warn("'clean' param does nothing when 'patch' is present.");
      }

      try
      {
        if ( (!install && !patch) || (install && patch) ) // auto-detect patch / install
        {
          if (user != null && pass != null)
          {
            patch = DatabaseBootstrapper.isRunwayInstalled(user, pass, template);
          }
          else
          {
            throw new CoreException("Unable to auto-detect a Runway installation without root database credentials. If you are trying to patch, you can supply a --patch param, otherwise please provide root database credentials.");
          }
        }
        
        if (path == null || path.length() == 0)
        {
          path = DatabaseBuilder.METADATA_CLASSPATH_LOC;
        }

        if (!patch)
        {
          DatabaseBootstrapper.bootstrap(user, pass, template, clean);
        }

        DatabaseBuilder.run(exts, plugins, path, ignoreErrors, patch);
      }
      finally
      {
        CacheShutdown.shutdown();
      }
    }
    catch (ParseException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public DatabaseBuilder(List<String> extensions, String path, Boolean ignoreErrors, Boolean isPatch)
  {
    if (path == null)
    {
      this.path = METADATA_CLASSPATH_LOC;
    }
    else
    {
      this.path = path;
    }

    if (extensions != null && !extensions.equals(""))
    {
      this.extensions = extensions;
    }
    else
    {
      this.extensions = supportedExtensions;
    }

    this.ignoreErrors = ignoreErrors;
    this.timestamps = new TreeSet<Date>();
    this.isPatch = isPatch;

    initialize();
  }

  private void initialize()
  {
    logger.info("Initializing Runway database builder.");

    this.ordered = new TreeSet<ClasspathResource>(new VersionComparator());

    for (ClasspathResource resource : getTimestampedResources(this.path))
    {
      ordered.add(resource);
    }
    
    if (isPatch)
    {
      for (ClasspathResource resource : getTimestampedResources(this.path + "/patch"))
      {
        ordered.add(resource);
      }
    }
    else
    {
      for (ClasspathResource resource : getTimestampedResources(this.path + "/install"))
      {
        ordered.add(resource);
      }
    }
  }
  
  protected void performDoIt(ClasspathResource resource, Date timestamp, Boolean isTransaction)
  {
    refreshTimestamps();

    // Only perform the doIt if this file has not already been imported
    if ( (!timestamps.contains(timestamp) && this.extensions.contains(resource.getNameExtension())) || alwaysRun(resource) )
    {
      logger.info("Importing [" + resource.getName() + "].");

      com.runwaysdk.dataaccess.database.Database.addPropertyValue(com.runwaysdk.dataaccess.database.Database.VERSION_NUMBER, MdAttributeCharacterInfo.CLASS, new TimeFormat(timestamp.getTime()).format(), RUNWAY_METADATA_VERSION_TIMESTAMP_PROPERTY);

      // We always want to use the context class loader because it ensures our
      // resource paths are absolute.
      InputStream schema = Thread.currentThread().getContextClassLoader().getResourceAsStream("com/runwaysdk/resources/xsd/schema.xsd");

      Savepoint sp = null;
      if (ignoreErrors && isTransaction)
      {
        sp = com.runwaysdk.dataaccess.database.Database.setSavepoint();
      }

      try
      {
        if (resource.getNameExtension().equals("sql"))
        {
          ObjectCache.shutdownGlobalCache();

          try (InputStream stream = resource.openNewStream())
          {
            String sql = IOUtils.toString(stream, "UTF-8");

            com.runwaysdk.dataaccess.database.Database.executeStatement(sql);
          }
        }
        else if (resource.getNameExtension().equals("xml"))
        {
          VersionHandler.runImport(resource, Action.DO_IT, null);
        }
        else if (resource.getNameExtension().equals("java"))
        {
          Class<?> clazz = LoaderDecorator.load("com.runwaysdk.patcher.domain." + DatabaseBuilder.getName(resource));
          Method main = clazz.getMethod("main", String[].class);
          main.invoke(null, (Object) new String[] {});
        }
        else
        {
          throw new CoreException("Unknown extension [" + resource.getNameExtension() + "].");
        }
      }
      catch (Throwable t)
      {
        if (ignoreErrors)
        {
          if (isTransaction)
          {
            com.runwaysdk.dataaccess.database.Database.rollbackSavepoint(sp);
          }
        }
        else
        {
          if (t instanceof RuntimeException)
          {
            throw (RuntimeException) t;
          }
          else
          {
            throw new ProgrammingErrorException(t);
          }
        }
      }
      finally
      {
        if (ignoreErrors && isTransaction)
        {
          com.runwaysdk.dataaccess.database.Database.releaseSavepoint(sp);
        }

        try
        {
          schema.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }

      timestamps.add(timestamp);
    }
  }

  protected void refreshTimestamps()
  {
    timestamps = new TreeSet<Date>();

    // Get a list of all the imported versions
    List<String> values = com.runwaysdk.dataaccess.database.Database.getPropertyValue(RUNWAY_METADATA_VERSION_TIMESTAMP_PROPERTY);

    for (String value : values)
    {
      timestamps.add(new Date(Long.parseLong(value)));
    }
  }

  public void performDoIt(List<ClasspathResource> resources, Boolean isTransaction)
  {
    for (ClasspathResource resource : resources)
    {
      Date date = getDate(resource);

      this.performDoIt(resource, date, isTransaction);
    }
  }

  public void doAll()
  {
    LocalProperties.setSkipCodeGenAndCompile(true);

    List<ClasspathResource> list = new LinkedList<ClasspathResource>(ordered);

    this.performDoIt(list, false);
  }

  @Transaction
  public void doAllInTransaction()
  {
    LocalProperties.setSkipCodeGenAndCompile(true);

    List<ClasspathResource> list = new LinkedList<ClasspathResource>(ordered);

    this.performDoIt(list, true);
  }

  private static ArrayList<String> toList(String string)
  {
    String[] split = string.split(",");

    ArrayList<String> ret = new ArrayList<String>();
    for (String sp : split)
    {
      ret.add(sp);
    }

    return ret;
  }

  @Request
  public static void run(List<String> extensions, List<String> plugins, String path, Boolean ignoreErrors, Boolean isPatch)
  {
    ServerProperties.setAllowModificationOfMdAttribute(true);
    
    registerPlugins(plugins);

    DatabaseBuilder builder = new DatabaseBuilder(extensions, path, ignoreErrors, isPatch);
    builder.doAllInTransaction();
  }
  
  private static void registerPlugins(List<String> plugins)
  {
    try
    {
      for (String sPlugin : plugins)
      {
        Class<?> clazz = DatabaseBuilder.class.getClassLoader().loadClass(sPlugin);
        
        ImportPluginIF plugin = (ImportPluginIF) clazz.newInstance();
        
        SAXSourceParser.registerPlugin(plugin);
      }
    }
    catch (ClassNotFoundException | InstantiationException | IllegalAccessException e)
    {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public static String getName(ClasspathResource resource)
  {
    Pattern namePattern = Pattern.compile(NAME_PATTERN);
    Matcher nameMatcher = namePattern.matcher(resource.getName());

    if (nameMatcher.find())
    {
      String out = "";

      String name1 = nameMatcher.group(1);
      if (name1 != null)
      {
        out = out + name1;
      }

      String name2 = nameMatcher.group(3);
      if (name2 != null)
      {
        out = out + name2;
      }

      return out;
    }

    return null;
  }
  
  public static Boolean alwaysRun(ClasspathResource resource)
  {
    Pattern namePattern = Pattern.compile(NAME_PATTERN);
    Matcher nameMatcher = namePattern.matcher(resource.getName());

    if (nameMatcher.find())
    {
      String timeGroup = nameMatcher.group(2);
      
      if (timeGroup.endsWith("-ALWAYS"))
      {
        return true;
      }
    }
    
    return false;
  }

  public static Long getTimestamp(ClasspathResource resource)
  {
    Pattern namePattern = Pattern.compile(NAME_PATTERN);
    Matcher nameMatcher = namePattern.matcher(resource.getName());

    if (nameMatcher.find())
    {
      String timeGroup = nameMatcher.group(2);
      
      if (timeGroup.endsWith("-ALWAYS"))
      {
        timeGroup = timeGroup.replace("-ALWAYS", "");
      }
      
      Long timeStamp = Long.parseLong(timeGroup);
      return timeStamp;
    }

    return null;
  }

  public static Date getDate(ClasspathResource resource)
  {
    Long timeStamp = getTimestamp(resource);

    if (timeStamp != null)
    {
      return new Date(timeStamp);
    }

    return null;
  }

  public static int compare(ClasspathResource resource1, ClasspathResource resource2)
  {
    return getTimestamp(resource1).compareTo(getTimestamp(resource2));
  }

  public static List<ClasspathResource> getTimestampedResources(String cpPackage)
  {
    List<ClasspathResource> list = new LinkedList<ClasspathResource>();

    Pattern namePattern = Pattern.compile(NAME_PATTERN);

    List<ClasspathResource> resources = ClasspathResource.getResourcesInPackage(cpPackage);

    for (ClasspathResource resource : resources)
    {
      if (!resource.isPackage())
      {
        String name = resource.getName();
        Matcher nameMatcher = namePattern.matcher(name);
  
        if (nameMatcher.find())
        {
          list.add(resource);
        }
      }
    }

    return list;
  }
  
  class VersionComparator implements Comparator<ClasspathResource>
  {
    public int compare(ClasspathResource arg0, ClasspathResource arg1)
    {
      return DatabaseBuilder.compare(arg0, arg1);
    }
  }
}
