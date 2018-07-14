/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
 */
package com.runwaysdk.dataaccess.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Test;

import com.runwaysdk.constants.DeployProperties;
import com.runwaysdk.session.Request;
import com.runwaysdk.util.FileIO;

public class BackupTest
{
  private String TEMP_FILE_NAME = "item.jsp";

  @Request
  @After
  public void tearDown() throws Exception
  {
    try
    {
      FileIO.deleteDirectory(new File("test/backup"));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  @Request
  @Test
  public void testBackupAndRestore() throws IOException
  {
    this.populateWebapp(1);

    try
    {
      String location = doBackup();

      doRestore(location);
    }
    finally
    {
      this.cleanupWebapp();
    }
  }

  private void doRestore(String location)
  {
    System.out.println("Starting restore from [" + location + "].");

    ByteArrayOutputStream errBaos = new ByteArrayOutputStream();
    PrintStream errPs = new PrintStream(errBaos);

    Restore restore = new Restore(System.out, errPs, location);
    restore.restore();

    String errOut = errBaos.toString();
    if (errOut.length() > 0)
    {
      throw new RuntimeException("psql produced this error output: " + errOut);
    }
  }

  private String doBackup()
  {
    System.out.println("Starting backup");

    ByteArrayOutputStream errBaos = new ByteArrayOutputStream();
    PrintStream errPs = new PrintStream(errBaos);

    Backup backup = new Backup(System.out, errPs, "test", "test/backup", true, true);
    String location = backup.backup(false);

    String errOut = errBaos.toString();
    if (errOut.length() > 0)
    {
      throw new RuntimeException("psql produced this error output: " + errOut);
    }
    else
    {
      System.out.println("Backed up to [" + location + "].");
    }

    return location;
  }

  @Request
  @Test
  public void testBackupAndRestoreOfDifferentFileName() throws IOException
  {
    this.populateWebapp(1);

    try
    {
      String location = doBackup();

      File destination = new File("test/backup/test.zip");
      FileIO.copy(new File(location), destination);

      System.out.println("Starting restore from [" + destination.getAbsolutePath() + "].");

      doRestore(destination.getAbsolutePath());
    }
    finally
    {
      this.cleanupWebapp();
    }
  }

  @Request
  @Test
  public void testBackupAndRestoreOfBigFile() throws IOException
  {
    /*
     * Creates a test file about 2 GiB in size in the webapp directory
     */
    this.populateWebapp(10240 * 1000 * 10);

    try
    {
      File file = new File(DeployProperties.getDeployPath());
      file.mkdirs();

      String location = doBackup();

      File destination = new File("test/backup/test.zip");
      FileIO.copy(new File(location), destination);

      doRestore(destination.getAbsolutePath());
    }
    finally
    {
      this.cleanupWebapp();
    }
  }

  private void cleanupWebapp() throws IOException
  {
    File webapp = new File(DeployProperties.getDeployPath());

    FileIO.deleteFile(new File(webapp, TEMP_FILE_NAME));
  }

  public void populateWebapp(int rows) throws IOException
  {
    /*
     * Create a temp file in the webapp directory
     */
    File webapp = new File(DeployProperties.getDeployPath());
    webapp.mkdirs();

    FileWriter writer = new FileWriter(new File(webapp, TEMP_FILE_NAME));

    try
    {
      for (int i = 0; i < rows; i++)
      {
        writer.append("This is a test file\n");
      }
    }
    finally
    {
      writer.close();
    }
  }
}
