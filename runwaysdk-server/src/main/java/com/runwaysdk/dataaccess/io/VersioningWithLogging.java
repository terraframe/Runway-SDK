/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.runwaysdk.dataaccess.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.dataaccess.CoreException;
import com.runwaysdk.dataaccess.cache.globalcache.ehcache.CacheShutdown;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.io.dataDefinition.VersionHandler;
import com.runwaysdk.dataaccess.io.dataDefinition.VersionHandler.Action;
import com.runwaysdk.session.Request;

/**
 * Performs versioning and creates a DDL diff output to a file in the ddl directory.
 * 
 * @author Richard Rowlands
 */
public class VersioningWithLogging extends Versioning
{
  private File ddlDir;
  private PrintStream consoleOut;
  
  public VersioningWithLogging(String location, String xsd, File ddlDir)
  {
    super(location, xsd);
    
    this.ddlDir = ddlDir;
    if (!ddlDir.exists()) {
      ddlDir.mkdir();
    }
    consoleOut = System.out;
  }

  public static void main(String[] args)
  {
    if (args.length < 2)
    {
      String errMsg = "At least two arguments are required for VersioningWithLogging:\n" + " 1) Location to export DDL file  2) Location of the folder containing the schema(version date).xml files\n" + "  3) xsd file to use (optional)";
      throw new CoreException(errMsg);
    }
    
    File ddlDir = new File(args[0]);
    
    Database.enableLoggingDMLAndDDLstatements(true);
    
    PrintStream console = System.out;
    
    try
    {
      List<String> lArgs = new ArrayList<String>(Arrays.asList(args));
      lArgs.remove(0);
      
      VersioningWithLogging.run(lArgs.toArray(new String[lArgs.size()]), ddlDir);
    }
    finally
    {
      Database.enableLoggingDMLAndDDLstatements(false);
      CacheShutdown.shutdown();
      System.setOut(console);
    }
  }
  
  @Request
  private static void run(String[] args, File ddlDir)
  {
    boolean record = true;

    String xsd;
    if (args.length == 1)
    {
      xsd = null;
    }
    else
    {
      xsd = args[1];
    }

    if (args.length > 2)
    {
      record = Boolean.parseBoolean(args[2]);
    }

    try
    {
      if (record)
      {
        new VersioningWithLogging(args[0], xsd, ddlDir).new DatabaseVersioningWithLogging(args[0], xsd, ddlDir).doAll();
      }
      else
      {
        new VersioningWithLogging(args[0], xsd, ddlDir).doAll();
      }
    }
    catch (ParseException e)
    {
      throw new CoreException(e);
    }
  }
  
  @Override
  protected void performDoIt(File file, Date date)
  {
    consoleOut.println("Importing " + file.getName() + "  [" + date + "]");
    
    try
    {
      File ddlFile = new File(ddlDir, file.getName().substring(0, file.getName().length()-4) + ".sql");
      if (ddlFile.exists()) {
        ddlFile.delete();
      }
      ddlFile.createNewFile();
      
      System.setOut(new PrintStream(new FileOutputStream(ddlFile)));
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }

    VersionHandler.runImport(file, Action.DO_IT, xsd);
  }
  
  public class DatabaseVersioningWithLogging extends DatabaseVersioning {

    private File ddlDir;
    
    public DatabaseVersioningWithLogging(String location, String xsd, File ddlDir)
    {
      super(location, xsd);
      
      this.ddlDir = ddlDir;
      if (!ddlDir.exists()) {
        ddlDir.mkdir();
      }
    }
    
    @Override
    protected void performDoIt(File file, Date timestamp)
    {
      consoleOut.println("Importing " + file.getName() + "  [" + timestamp + "]");
      
      try
      {
        File ddlFile = new File(ddlDir, file.getName().substring(0, file.getName().length()-4) + ".sql");
        if (ddlFile.exists()) {
          ddlFile.delete();
        }
        ddlFile.createNewFile();
        
        System.setOut(new PrintStream(new FileOutputStream(ddlFile)));
      }
      catch (Exception e)
      {
        throw new RuntimeException(e);
      }
      
      // Only perform the doIt if this file has not already been imported
      if (!timestamps.contains(timestamp))
      {
        Database.addPropertyValue(Database.VERSION_NUMBER, MdAttributeCharacterInfo.CLASS, new TimeFormat(timestamp.getTime()).format(), Database.VERSION_TIMESTAMP_PROPERTY);

        VersionHandler.runImport(file, Action.DO_IT, xsd);

        timestamps.add(timestamp);
      }
    }
  }
}
