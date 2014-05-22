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
import java.io.FileWriter;
import java.io.IOException;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.constants.DeployProperties;
import com.runwaysdk.session.Request;
import com.runwaysdk.util.FileIO;

public class BackupTest extends TestCase
{
  private String TEMP_FILE_NAME = "item.jsp";

  @Override
  public TestResult run()
  {
    return super.run();
  }

  @Override
  public void run(TestResult testResult)
  {
    super.run(testResult);
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(BackupTest.class);

    TestSetup wrapper = new TestSetup(suite)
    {
      protected void setUp()
      {
        classSetUp();
      }

      protected void tearDown()
      {
        classTearDown();
      }
    };

    return wrapper;
  }

  /**
   * The setup done before the test suite is run
   */
  @Request
  public static void classSetUp()
  {
  }

  /**
   * The tear down done after all the test in the test suite have run
   */
  public static void classTearDown()
  {
  }

  @Override
  protected void tearDown() throws Exception
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

  public void testBackupAndRestore() throws IOException
  {
    this.populateWebapp(1);

    try
    {

      Backup backup = new Backup(System.out, "test", "test/backup", true, true);
      String location = backup.backup(false);

      Restore restore = new Restore(System.out, location);
      restore.restore();
    }
    finally
    {
      this.cleanupWebapp();
    }
  }

  public void testBackupAndRestoreOfDifferentFileName() throws IOException
  {
    this.populateWebapp(1);

    try
    {
      Backup backup = new Backup(System.out, "test", "test/backup", true, true);
      String location = backup.backup(false);

      File destination = new File("test/backup/test.zip");
      FileIO.copy(new File(location), destination);

      Restore restore = new Restore(System.out, destination.getAbsolutePath());
      restore.restore();
    }
    finally
    {
      this.cleanupWebapp();
    }
  }

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

      Backup backup = new Backup(System.out, "test", "test/backup", true, true);
      String location = backup.backup(false);

      File destination = new File("test/backup/test.zip");
      FileIO.copy(new File(location), destination);

      Restore restore = new Restore(System.out, destination.getAbsolutePath());
      restore.restore();
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
