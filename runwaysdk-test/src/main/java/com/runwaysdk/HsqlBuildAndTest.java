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
package com.runwaysdk;

import java.io.File;
import java.io.IOException;

import com.runwaysdk.business.generation.ServerMarker;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.dataaccess.io.XMLImporter;
import com.runwaysdk.util.FileIO;


/**
 * Builds and tests HsqlDB
 */
public class HsqlBuildAndTest
{
  /**
   * Runs all Junits on Hsqldb after building the metadata.
   */
  public static void main(String[] args)
  {
    // build
    System.out.println("Building HsqlDB...");
    XMLImporter.main(args);
    System.out.println("Finished building HsqlDB");
    System.out.println("\n-----------------------------------\n");
    System.out.println("Running All JUnit Tests");
    
    String generated = LocalProperties.getServerSrc();
    deleteDirectory(new File(generated));
    
    generated = ServerMarker.CLASS_DIRECTORY;
    deleteDirectory(new File(generated));
    
    // run the tests
    UeberTestSuite.HsqlTestRun();
  }
  
  public static void deleteDirectory(File file)
  {
    if (file.exists())
    {
      File[] files = file.listFiles();

      for (File f : files)
      {
        if (f.isDirectory())
        {
          deleteDirectory(f);
        }
        else
        {
          try
          {
            FileIO.deleteFile(f);
          }
          catch (IOException e)
          {
            throw new SystemException(e.getMessage());
          }
        }
      }

      try
      {
        FileIO.deleteFile(file);
      }
      catch (IOException e)
      {
        throw new SystemException(e.getMessage());
      }
    }
  }
}
