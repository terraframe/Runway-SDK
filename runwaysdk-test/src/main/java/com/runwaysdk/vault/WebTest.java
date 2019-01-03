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
package com.runwaysdk.vault;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.dataaccess.io.FileReadException;
import com.runwaysdk.dataaccess.io.SAXParseTest;
import com.runwaysdk.session.Request;
import com.runwaysdk.util.FileIO;

public class WebTest
{
  /**
   *
   */
  private static WebFileDAO   file     = null;

  /**
   *
   */
  private static WebFileDAO   file2    = null;

  /**
   * The byte to test the files
   */
  private static byte[]       testFile;

  /**
   * The path of the files
   */
  private static final String filePath = SAXParseTest.FILTER_SET;

  /**
   * The setup done before the test suite is run
   */
  @Request
  @BeforeClass
  public static void classSetUp()
  {
    File file = new File(filePath);

    try
    {
      testFile = FileIO.readBytes(file);
    }
    catch (IOException e)
    {
      throw new FileReadException(file, e);
    }
  }

  /**
   * Delete all MetaData objects which were created in the class
   * 
   * @see junit.framework.TestCase#tearDown()
   */
  @Request
  @After
  public void tearDown() throws Exception
  {
    if (file != null)
    {
      file.delete();
    }

    if (file2 != null)
    {
      file2.delete();
    }
  }

  /**
   * Test creating a file
   */
  @Request
  @Test
  public void testCreateFile()
  {
    file = WebFileDAO.newInstance();

    file.setFileName("testFile");
    file.setExtension("xml");
    file.setFilePath("test/");

    Assert.assertEquals("testFile", file.getFileName());
    Assert.assertEquals("xml", file.getExtension());
    Assert.assertEquals("test/", file.getFilePath());

    file.apply();
  }

  @Request
  @Test
  public void testUnspecifiedPath()
  {
    file = WebFileDAO.newInstance();

    file.setFileName("testFile");
    file.setExtension("xml");

    Assert.assertEquals("testFile", file.getFileName());
    Assert.assertEquals("xml", file.getExtension());
    Assert.assertTrue(!file.getFilePath().equals(""));

    file.apply();
  }

  /**
   * Test writting and getting the bytes from a file in a vault
   */
  @Request
  @Test
  public void testPutFile()
  {
    file = WebFileDAO.newInstance();

    file.setFileName("testFile");
    file.setExtension("xml");
    file.setFilePath("test/");
    file.apply();

    file.putFile(testFile);

    // Ensure the file exists in the vault
    String path = file.getFullFilePathAndName();

    File f = new File(path);

    Assert.assertTrue(f.exists());
    Assert.assertTrue(f.isFile());

    // Ensure that the file is the same
    // Ensure that the file is the same
    BufferedReader bytes1 = new BufferedReader(new InputStreamReader(file.getFile()));
    BufferedReader bytes2 = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(testFile)));

    try
    {
      while (bytes1.ready() || bytes2.ready())
      {
        Assert.assertEquals(bytes1.read(), bytes2.read());
      }

      bytes1.close();
      bytes2.close();
    }
    catch (IOException e)
    {
      Assert.fail(e.getLocalizedMessage());
    }
  }

  /**
   * Test deleting a file in a vault
   */
  @Request
  @Test
  public void testDeleteFile()
  {
    file = WebFileDAO.newInstance();

    file.setFileName("testFile");
    file.setExtension("xml");
    file.setFilePath("test/");
    file.apply();

    file.putFile(testFile);

    // Ensure the file exists in the vault
    String path = file.getFullFilePathAndName();

    file.delete();
    file = null;

    File f = new File(path);
    Assert.assertFalse(f.exists());
  }

  /**
   * Test deleting an empty file in a vault
   */
  @Request
  @Test
  public void testDeleteFile2()
  {
    file = WebFileDAO.newInstance();

    file.setFileName("testFile");
    file.setExtension("xml");
    file.setFilePath("test/");
    file.apply();

    file.delete();
    file = null;
  }
}
