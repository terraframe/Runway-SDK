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
package com.runwaysdk.dataaccess.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.runwaysdk.dataaccess.CoreException;
import com.runwaysdk.dataaccess.cache.globalcache.ehcache.CacheShutdown;

public class CreateDomainModel
{
  private File directory;

  public CreateDomainModel(String directory)
  {
    this.directory = new File(directory);
  }

  public String create()
  {
    long time = System.currentTimeMillis();
    String format = new TimeFormat(time).format();

    if (!directory.exists())
    {
      directory.mkdirs();
    }

    String path = directory.getAbsolutePath() + "/schema(" + format + ").xml";

    File file = new File(path);

    if (!file.exists())
    {
      try
      {
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(path));
        out.write("<version xsi:noNamespaceSchemaLocation=\"../../profiles/version_gis.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + "  <doIt>\n" + "    <create></create>\n" + "    <update></update>\n" + "  </doIt>\n" + "  <undoIt>\n" + "    <delete></delete>\n" + "  </undoIt>\n" + "</version>");
        out.close();

      }
      catch (IOException e)
      {
        throw new CoreException(e.getMessage());
      }
    }

    return path;
  }

  public static void main(String[] args)
  {
    try
    {
      if (args.length != 1)
      {
        String errMsg = "One argument is required to Create a new Domain Model:\n" + "  1) Location of the folder containing the schema(version date).xml files";
        throw new CoreException(errMsg);
      }

      new CreateDomainModel(args[0]).create();
    }
    finally
    {
      CacheShutdown.shutdown();
    }
  }
}
