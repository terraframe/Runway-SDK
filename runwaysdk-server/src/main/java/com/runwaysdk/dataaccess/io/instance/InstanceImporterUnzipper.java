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
package com.runwaysdk.dataaccess.io.instance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.dataaccess.io.dataDefinition.SAXImporter;
import com.runwaysdk.util.FileIO;

/**
 * Unzips a directory of zip files into a temporary file, and then runs the
 * InstanceImporter on the temp directory.
 */
public class InstanceImporterUnzipper
{
  private static final Logger logger   = LoggerFactory.getLogger(InstanceImporterUnzipper.class);

  private static final String TEMP_DIR = "/temp";

  public static void main(String[] args)
  {
    if (args.length != 1)
    {
      String msg = "Please include the arguments 1) The path to the application's data directory.";

      throw new RuntimeException(msg);
    }

    processZipDir(args[0] + "/universals");
    processZipDir(args[0] + "/geoentities");
    // processZipDir(args[0] + "/classifiers");
  }

  /**
   * Expands the zip files and imports the terms therein.
   * 
   * @param dir
   */
  public static void processZipDir(String dir)
  {
    File directory = new File(dir);
    if (!directory.exists())
    {
      logger.error("Directory [" + directory.getAbsolutePath() + "] does not exist, aborting import.");
      return;
    }

    final File outputDir = new File(dir + TEMP_DIR);

    if (outputDir.exists())
    {
      try
      {
        FileUtils.deleteDirectory(outputDir);
      }
      catch (IOException e)
      {
        throw new RuntimeException(e); // I hate checked exceptions
      }
    }
    outputDir.mkdir();

    for (File zip : directory.listFiles())
    {
      if (zip.getName().endsWith(".gz"))
      {
        logger.info("Unzipping " + zip.getAbsolutePath() + " to " + outputDir + ".");

        FileIO.gunzip(zip, new File(outputDir, zip.getName().substring(0, zip.getName().length() - 3)));
      }
    }

    // InstanceImporter.runImport(outputDir, (String)null, new
    // DefaultConflictResolver());

    importXmlFiles(outputDir);

    // Versioning.main(new String[]{outputDir.getAbsolutePath()});
  }

  /**
   * Just processes the XML file.
   * 
   * @param dir
   */
  public static void importXmlFiles(String dir)
  {
    final File outputDir = new File(dir + TEMP_DIR);
    importXmlFiles(outputDir);
  }

  private static void importXmlFiles(final File outputDir)
  {
    for (File xml : outputDir.listFiles())
    {
      if (xml.getName().endsWith(".xml"))
      {
        logger.info("Importing " + xml.getAbsolutePath() + ".");
        SAXImporter.runImport(xml, "classpath:com/runwaysdk/resources/xsd/datatype.xsd");
      }
    }
  }
}
