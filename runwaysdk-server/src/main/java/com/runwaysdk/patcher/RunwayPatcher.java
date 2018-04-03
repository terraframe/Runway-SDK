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
package com.runwaysdk.patcher;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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

import com.runwaysdk.ClasspathResource;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.dataaccess.CoreException;
import com.runwaysdk.dataaccess.InstallerCP;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.cache.globalcache.ehcache.CacheShutdown;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.DatabaseException;
import com.runwaysdk.dataaccess.io.ResourceStreamSource;
import com.runwaysdk.dataaccess.io.TimeFormat;
import com.runwaysdk.dataaccess.io.XMLImporter;
import com.runwaysdk.dataaccess.io.dataDefinition.SAXImporter;
import com.runwaysdk.dataaccess.io.dataDefinition.VersionHandler;
import com.runwaysdk.dataaccess.io.dataDefinition.VersionHandler.Action;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.session.Request;

public class RunwayPatcher
{
  private static Logger logger = LoggerFactory.getLogger(RunwayPatcher.class);
  
  public static final List<String> supportedExtensions = toList("sql,xml,java");
  
  public static final String RUNWAY_METADATA_VERSION_TIMESTAMP_PROPERTY = Database.VERSION_TIMESTAMP_PROPERTY;
  
  private static final String DATE_PATTEN  = "\\d{4,}";
  
  private static final String NAME_PATTERN = "^([A-Za-z_\\-\\d\\.]*)\\((" + DATE_PATTEN + ")\\)([A-Za-z_\\-\\d\\.]*).(?:" + StringUtils.join(supportedExtensions,"|") + ")$";
  
  public static final String METADATA_CLASSPATH_LOC = "domain";
  
  public static final String MODE_STANDARD = "standard";
  
  public static final String MODE_BOOTSTRAP = "bootstrap";
  
  class VersionComparator implements Comparator<ClasspathResource>
  {
    public int compare(ClasspathResource arg0, ClasspathResource arg1)
    {
      return RunwayPatcher.compare(arg0, arg1);
    }
  }
  
  /**
   * List of timestamps which have already been imported
   */
  protected Set<Date> timestamps;
  
  /**
   * List of all schema resources in the given location in order from earliest to
   * latest
   */
  protected Set<ClasspathResource>       ordered;

  /**
   * Mapping between a resource and its timestamp
   */
  protected Map<Date, ClasspathResource> map;
  
  private List<String> extensions;
  
  private String path;
  
  private Boolean ignoreErrors;
  
  public RunwayPatcher(List<String> extensions, String path, Boolean ignoreErrors)
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
    
    initialize();
  }
  
  private void initialize()
  {
    logger.info("Initializing Runway patcher.");
    
    this.map = new HashMap<Date, ClasspathResource>();
    this.ordered = new TreeSet<ClasspathResource>(new VersionComparator());

    for (ClasspathResource resource : getTimestampedResources(this.path))
    {
      ordered.add(resource);

      map.put(getDate(resource), resource);
    }

    timestamps = new TreeSet<Date>();

    // Get a list of all the imported versions
    List<String> values = Database.getPropertyValue(RUNWAY_METADATA_VERSION_TIMESTAMP_PROPERTY);

    for (String timestamp : values)
    {
      timestamps.add(new Date(Long.parseLong(timestamp)));
    }
  }
  
  /**
   * Bootstrapping must not be done inside a request or transaction.
   */
  public static void bootstrap(String rootUser, String rootPass, String template, Boolean clean)
  {
    boolean tableExist;
    try
    {
      tableExist = Database.tableExists("md_class");
    }
    catch (DatabaseException ex)
    {
      // An exception can be thrown if the database itself doesn't exist. (not just the table)
      tableExist = false;
    }
    
    if (clean == true || !tableExist)
    {
      logger.info("Bootstrapping Runway into an empty database.");
      
      if (rootUser != null && rootPass != null && template != null)
      {
        Database.close(); // Connection pooling leaves connections floating around. We have to make sure everything is closed.
        Database.initialSetup(rootUser, rootPass, template);
      }
      
      InputStream schema = Thread.currentThread().getContextClassLoader().getResourceAsStream("com/runwaysdk/resources/xsd/schema.xsd");
      
      try
      {
        InputStream[] xmlFilesIS = InstallerCP.buildMetadataInputStreamList();
  
        XMLImporter importer = new XMLImporter(schema, xmlFilesIS);
        importer.toDatabase();
      }
      catch (IOException e)
      {
        throw new CoreException(e);
      }
      finally
      {
        try
        {
          schema.close();
        }
        catch (IOException e)
        {
          throw new CoreException(e);
        }
      }
    }
  }
  
  protected void performDoIt(ClasspathResource resource, Date timestamp, Boolean isTransaction)
  {
    // Only perform the doIt if this file has not already been imported
    if (!timestamps.contains(timestamp) && this.extensions.contains(resource.getNameExtension()))
    {
      logger.info("Importing domain classpath resource [" + resource.getName() + "].");
      
      Database.addPropertyValue(Database.VERSION_NUMBER, MdAttributeCharacterInfo.CLASS, new TimeFormat(timestamp.getTime()).format(), RUNWAY_METADATA_VERSION_TIMESTAMP_PROPERTY);

      // We always want to use the context class loader because it ensures our resource paths are absolute.
      InputStream schema = Thread.currentThread().getContextClassLoader().getResourceAsStream("com/runwaysdk/resources/xsd/schema.xsd");
      InputStream stream = null;
      
      Savepoint sp = null;
      if (ignoreErrors && isTransaction)
      {
        sp = Database.setSavepoint();
      }
      
      try
      {
        if (resource.getNameExtension().equals("sql"))
        {
          ObjectCache.shutdownGlobalCache();
          
          stream = resource.getStream();
          String sql = IOUtils.toString(stream, "UTF-8");
          
          Database.executeStatement(sql);
        }
        else if (resource.getNameExtension().equals("xml"))
        {
          if (resource.getName().contains("universal")) // TODO : Don't hardcode this
          {
            SAXImporter.runImport(new ResourceStreamSource(resource.getAbsolutePath()), null);
          }
          else
          {
            VersionHandler.runImport(resource, Action.DO_IT, null);
          }
        }
        else if (resource.getNameExtension().equals("java"))
        {
          Class<?> clazz = LoaderDecorator.load("com.runwaysdk.patcher." + RunwayPatcher.METADATA_CLASSPATH_LOC + "." + RunwayPatcher.getName(resource));
          Method main = clazz.getMethod("main", String[].class);
          main.invoke(null, (Object) new String[]{});
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
            Database.rollbackSavepoint(sp);
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
          Database.releaseSavepoint(sp);
        }
        
        try {
          if (stream != null)
          {
            stream.close();
          }
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
  
  public void performDoIt(List<ClasspathResource> resources, Boolean isTransaction)
  {
    for (ClasspathResource resource : resources)
    {
      Date date = getDate(resource);

      this.performDoIt(resource, date, isTransaction);
    }
  }
  
  /**
   * Migrates databases using the legacy patcher system to our new versioned patcher.
   */
//  public void migrateToNewPatcher()
//  {
//    RunwayMetadataVersion version = Database.getMetadataVersion();
//    
//    String sql = null;
//    
//    if (version.toString().equals("1.27.0"))
//    {
//      
//    }
//  }
  
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
  
  /**
   * Brings your database to a fully patched state (as far as Runway is concerned).
   * 
   * @param args
   */
  public static void main(String[] args)
  {
    CommandLineParser parser = new DefaultParser();
    Options options = new Options();
    options.addOption(Option.builder("mode").hasArg().argName("mode").longOpt("mode").desc("The mode to run the RunwayPatcher in. Can be either bootstrap or standard. If omitted standard is assumed. During standard mode, bootstrapping will be attempted if Runway does not exist.").optionalArg(true).build());
    options.addOption(Option.builder("rootUser").hasArg().argName("rootUser").longOpt("rootUser").desc("The username of the root database user. Only required when bootstrapping.").optionalArg(true).build());
    options.addOption(Option.builder("rootPass").hasArg().argName("rootPass").longOpt("rootPass").desc("The password of the root database user. Only required when bootstrapping.").optionalArg(true).build());
    options.addOption(Option.builder("templateDb").hasArg().argName("templateDb").longOpt("templateDb").desc("The template database to use when creating the application database. Only required when bootstrapping.").optionalArg(true).build());
    options.addOption(Option.builder("extensions").hasArg().argName("extensions").longOpt("extensions").desc("A comma separated list of extensions denoting which schema files to run. If unspecified we will use all supported.").optionalArg(true).build());
    options.addOption(Option.builder("clean").hasArg().argName("clean").longOpt("clean").desc("A boolean parameter denoting whether or not to clean the database and delete all data. Default is false.").optionalArg(true).build());
    options.addOption(Option.builder("path").hasArg().argName("path").longOpt("path").desc("The path (from the root of the classpath) to the location of the metadata files. Defaults to 'domain'").optionalArg(true).build());
    options.addOption(Option.builder("ignoreErrors").hasArg().argName("ignoreErrors").longOpt("ignoreErrors").desc("Ignore errors if one occurs while importing sql. Not recommended for everyday usage.").optionalArg(true).build());
    
    try
    {
      CommandLine line = parser.parse( options, args );
      
      String mode = line.getOptionValue("mode");
      String user = line.getOptionValue("rootUser");
      String pass = line.getOptionValue("rootPass");
      String template = line.getOptionValue("templateDb");
      String extensions = line.getOptionValue("extensions");
      String path = line.getOptionValue("path");
      Boolean ignoreErrors = line.getOptionValue("ignoreErrors") == null || line.getOptionValue("ignoreErrors").equals("") ? false : Boolean.valueOf(line.getOptionValue("ignoreErrors"));
      Boolean clean = line.getOptionValue("clean") == null || line.getOptionValue("clean").equals("") ? false : Boolean.valueOf(line.getOptionValue("clean"));
      
      List<String> exts = supportedExtensions;
      if (extensions != null && extensions.length() > 0)
      {
        exts = toList(extensions);
      }
      
      try
      {
        if (path == null || path.length() == 0)
        {
          path = RunwayPatcher.METADATA_CLASSPATH_LOC;
        }
        
        if (mode != null && mode.equals(RunwayPatcher.MODE_BOOTSTRAP))
        {
          RunwayPatcher.bootstrap(user, pass, template, clean);
        }
        else
        {
          RunwayPatcher.bootstrap(user, pass, template, clean);
          
          // I think this code is somehow running inside a transaction even though we're trying to avoid it.
          // I'm commenting this code out (because it doesn't work anyway and) because it breaks my "ignoreErrors"
          //   logic which I need for DDMS BackupDevImporter.
//          if (exts.contains("sql"))
//          {
//            RunwayPatcher.run(toList("sql"), path, ignoreErrors);
//            exts.remove(exts.indexOf("sql"));
//          }
          
          RunwayPatcher.run(exts, path, ignoreErrors);
        }
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
  public static void run(List<String> extensions, String path, Boolean ignoreErrors)
  {
//    if (extensions.contains("sql"))
//    {
//      RunwayPatcher patcher = new RunwayPatcher(extensions, path, ignoreErrors);
//      patcher.doAll();
//    }
//    else
//    {
      RunwayPatcher patcher = new RunwayPatcher(extensions, path, ignoreErrors);
      patcher.doAllInTransaction();
//    }
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

  public static Long getTimestamp(ClasspathResource resource)
  {
    Pattern namePattern = Pattern.compile(NAME_PATTERN);
    Matcher nameMatcher = namePattern.matcher(resource.getName());

    if (nameMatcher.find())
    {
      Long timeStamp = Long.parseLong(nameMatcher.group(2));
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
      String name = resource.getName();
      Matcher nameMatcher = namePattern.matcher(name);
      
      if (nameMatcher.find())
      {
        list.add(resource);
      }
    }

    return list;
  }
}
