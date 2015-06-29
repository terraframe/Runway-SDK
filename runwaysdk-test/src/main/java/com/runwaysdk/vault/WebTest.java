/**
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
 */
package com.runwaysdk.vault;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.runwaysdk.dataaccess.io.FileReadException;
import com.runwaysdk.dataaccess.io.SAXParseTest;
import com.runwaysdk.util.FileIO;

public class WebTest extends TestCase
{

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

  public static Test suite()
  {
    TestSuite suite = new TestSuite();

    suite.addTestSuite(WebTest.class);

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
   * The tear down done after all the test in the test suite have run
   */
  public static void classTearDown()
  {
  }

  /**
   * No setup needed non-Javadoc)
   * 
   * @see junit.framework.TestCase#setUp()
   */
  protected void setUp() throws Exception
  {
  }

  /**
   * Delete all MetaData objects which were created in the class
   * 
   * @see junit.framework.TestCase#tearDown()
   */
  protected void tearDown() throws Exception
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
  public void testCreateFile()
  {
    file = WebFileDAO.newInstance();

    file.setFileName("testFile");
    file.setExtension("xml");
    file.setFilePath("test/");

    assertEquals("testFile", file.getFileName());
    assertEquals("xml", file.getExtension());
    assertEquals("test/", file.getFilePath());

    file.apply();
  }

  public void testUnspecifiedPath()
  {
    file = WebFileDAO.newInstance();

    file.setFileName("testFile");
    file.setExtension("xml");

    assertEquals("testFile", file.getFileName());
    assertEquals("xml", file.getExtension());
    assertTrue(!file.getFilePath().equals(""));

    file.apply();
  }

  /**
   * Test writting and getting the bytes from a file in a vault
   */
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

    assertTrue(f.exists());
    assertTrue(f.isFile());

    // Ensure that the file is the same
    // Ensure that the file is the same
    BufferedReader bytes1 = new BufferedReader(new InputStreamReader(file.getFile()));
    BufferedReader bytes2 = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(testFile)));

    try
    {
      while (bytes1.ready() || bytes2.ready())
      {
        assertEquals(bytes1.read(), bytes2.read());
      }

      bytes1.close();
      bytes2.close();
    }
    catch (IOException e)
    {
      fail(e.getLocalizedMessage());
    }
  }

  /**
   * Test deleting a file in a vault
   */
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
    assertFalse(f.exists());
  }

  /**
   * Test deleting an empty file in a vault
   */
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

  public static void main(String[] args)
  {
    TestSuite suite = new TestSuite();

    suite.addTest(WebTest.suite());

    TestRunner.run(suite);
  }
}
