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
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.io.DatabaseVersioning;
import com.runwaysdk.dataaccess.io.Versioning;
import com.runwaysdk.util.FileIO;

/**
 * Unzips a directory of zip files into a temporary file, and then runs the
 * InstanceImporter on the temp directory.
 */
public class VersioningUnzipper
{
  private static final Logger logger   = LoggerFactory.getLogger(InstanceImporterUnzipper.class);

  private static final String TEMP_DIR = "/temp";

  /**
   * Expands the zip files and imports the terms therein.
   * 
   * @param dir
   */
  public static void processZipDir(String dir)
  {
    try
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
        FileUtils.deleteDirectory(outputDir);
      }

      outputDir.mkdir();

      try
      {

        for (File zip : directory.listFiles())
        {
          if (zip.getName().endsWith(".gz"))
          {
            logger.info("Unzipping " + zip.getAbsolutePath() + " to " + outputDir + ".");

            FileIO.gunzip(zip, new File(outputDir, zip.getName().substring(0, zip.getName().length() - 3)));
          }
        }

        DatabaseVersioning.main(new String[] { outputDir.getAbsolutePath() });
      }
      finally
      {
        FileUtils.deleteDirectory(outputDir);
      }
    }
    catch (IOException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }
}
