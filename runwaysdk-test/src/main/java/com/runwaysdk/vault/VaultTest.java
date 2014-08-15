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

import com.runwaysdk.constants.VaultInfo;
import com.runwaysdk.dataaccess.io.FileReadException;
import com.runwaysdk.dataaccess.io.SAXParseTest;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.util.FileIO;

public class VaultTest extends TestCase
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
   * The first vault to store files
   */
  private static VaultDAO     vault1;

  /**
   * The second vault to store files
   */
  private static VaultDAO     vault2;

  private static VaultFileDAO file     = null;

  private static VaultFileDAO file2    = null;

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

    suite.addTestSuite(VaultTest.class);

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
    vault1 = VaultDAO.newInstance();
    vault1.setValue(VaultInfo.VAULT_NAME, "vault1");
    vault1.apply();

    vault2 = VaultDAO.newInstance();
    vault2.setValue(VaultInfo.VAULT_NAME, "vault2");
    vault2.apply();

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
    vault1 = (VaultDAO) VaultDAO.get(vault1.getId()).getBusinessDAO();
    vault1.delete();

    vault2 = (VaultDAO) VaultDAO.get(vault2.getId()).getBusinessDAO();
    vault2.delete();
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
    if (file != null && file.isAppliedToDB())
    {
      file = VaultFileDAO.get(file.getId()).getBusinessDAO();
      file.delete();
      file = null;
    }

    if (file2 != null && file2.isAppliedToDB())
    {
      file2 = VaultFileDAO.get(file2.getId()).getBusinessDAO();
      file2.delete();
      file2 = null;
    }
  }

  // @Transaction
  // private void invalidValut()
  // {
  // vault1.setValue(VaultInfo.VAULT_NAME, "invalid");
  // }
  //
  @Transaction
  public void testApplyMultipleFileInATransaction()
  {
    for (int i = 0; i < 10; i++)
    {
      file = VaultFileDAO.newInstance();

      file.setFileName("testFile");
      file.setSize(testFile.length);
      file.setExtension("xml");
      file.apply();

      file.putFile(testFile);
    }
  }

  /**
   * Test creating a file
   */
  public void testCreateFile()
  {
    file = VaultFileDAO.newInstance();

    file.setFileName("testFile");
    file.setSize(testFile.length);
    file.setExtension("xml");

    assertEquals("testFile", file.getFileName());
    assertEquals("xml", file.getExtension());

    file.apply();
  }

  /**
   * Test writting and getting the bytes from a file in a vault
   * 
   * @throws NoSuchFieldException
   * @throws SecurityException
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   */
  public void testPutFile() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException
  {
    file = VaultFileDAO.newInstance();

    file.setFileName("testFile");
    file.setExtension("xml");
    file.setSize(testFile.length);
    file.apply();

//    VaultDAOIF vault = VaultDAO.get(file.getVaultReference());

//    long byteCount = Long.parseLong(vault.getValue(VaultInfo.BYTE_COUNT));

    file.putFile(testFile);

    // Ensure the file exists in the vault
    String path = file.filePath() + file.getVaultFileName();

    File f = new File(path);

    assertTrue(f.exists());
    assertTrue(f.isFile());

    // Ensure that the file is the same
    BufferedReader bytes1 = new BufferedReader(new InputStreamReader(file.getFileStream()));
    BufferedReader bytes2 = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(testFile)));
//    int i = 0;

    try
    {
      while (bytes1.ready() || bytes2.ready())
      {
        assertEquals(bytes1.read(), bytes2.read());
//        i++;
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
    file = VaultFileDAO.newInstance();
    file.setFileName("testFile");
    file.setExtension("xml");
    file.setSize(testFile.length);
    file.apply();

    VaultDAOIF vault = VaultDAO.get(file.getVaultReference());
    long byteCount = Long.parseLong(vault.getValue(VaultInfo.BYTE_COUNT));

    file.putFile(testFile);

    // Ensure the file does exists in the vault and the temp file is deleted
    String path = vault.getVaultPath() + "/" + file.getVaultFilePath() + file.getVaultFileName();
    String tempPath = vault.getVaultPath() + "/" + file.getVaultFilePath() + file.getVaultFileName() + ".temp";

    assertTrue(new File(path).exists());
    assertFalse(new File(tempPath).exists());

    file.delete();
    file = null;

    // Ensure that the file and the temp file no longer exists on the
    // filesystem
    assertFalse(new File(path).exists());
    assertFalse(new File(tempPath).exists());
    assertEquals(byteCount, Long.parseLong(vault.getValue(VaultInfo.BYTE_COUNT)));
  }

  /**
   * Test deleting an empty file in a vault
   */
  public void testDeleteFile2()
  {
    file = VaultFileDAO.newInstance();

    file.setFileName("testFile");
    file.setExtension("xml");
    file.setSize(testFile.length);

    file.apply();

    file.delete();
    file = null;
  }

  /**
   * Test adding files to multiple different vaults
   */
  public void testMultiVaults()
  {
    // Create a file in the first file vault
    file = VaultFileDAO.newInstance();

    file.setFileName("testFile");
    file.setExtension("xml");
    file.setSize(testFile.length);
    file.apply();

    file.putFile(testFile);

    // Create a file in the second file vault
    file2 = VaultFileDAO.newInstance();

    file2.setFileName("testFile");
    file2.setExtension("xml");
    file2.setSize(testFile.length);

    file2.apply();

    file2.putFile(testFile);

    // Ensure the files are in separate vaults
    assertTrue(!file.getVaultReference().equals(file2.getId()));

    // Ensure that both files are equals in the different vaults
    BufferedReader bytes1 = new BufferedReader(new InputStreamReader(file.getFileStream()));
    BufferedReader bytes2 = new BufferedReader(new InputStreamReader(file2.getFileStream()));

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
   * Test the undo it command on an empty file
   */
  public void testUndoIt1()
  {
    createFile();

    try
    {
      runFailure(file);
    }
    catch (Exception e)
    {
      // Run failure always throws an error to ensure the undo it command is
      // called
    }

    // Ensure the file exists in the vault
    String path = file.filePath() + file.getVaultFileName();

    File f = new File(path);

    assertFalse(f.exists());
  }

  /**
   * Test the undo it command on an existing file
   */
  public void testUndoIt2()
  {
    createFile(testFile);

    try
    {
      runFailure(file);
    }
    catch (Exception e)
    {
      // Run failure always throws an error to ensure the undo it command is
      // called
    }

    // Ensure the file exists in the vault
    String path = file.filePath() + file.getVaultFileName();

    File f = new File(path);

    assertTrue(f.exists());
    assertTrue(f.isFile());

    // Ensure that the file is the same
    BufferedReader bytes1 = new BufferedReader(new InputStreamReader(file.getFileStream()));
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

  public void testDeleteRollBack()
  {
    createFile(testFile);
    String path = file.filePath() + file.getVaultFileName();

    try
    {
      runDeleteFailure(file);
    }
    catch (Exception e)
    {
      // Run failure always throws an error to ensure the undo it command is
      // called
    }

    // Ensure the file exists in the vault

    File f = new File(path);

    assertTrue(f.exists());
    assertTrue(f.isFile());

    // Ensure that the file is the same
    BufferedReader bytes1 = new BufferedReader(new InputStreamReader(file.getFileStream()));
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

    file = null;
  }

  public void testVaultDelete()
  {
    // Add files to an existing vault
    file = VaultFileDAO.newInstance();

    file.setFileName("testFile");
    file.setExtension("xml");
    file.setSize(testFile.length);
    file.apply();

    file.putFile(testFile);

    // Create a new vault
    VaultDAO tVault = VaultDAO.newInstance();
    tVault.setValue(VaultInfo.VAULT_NAME, "tVault1");
    tVault.apply();

    // Add a file to the new vault
    file2 = VaultFileDAO.newInstance();

    file2.setFileName("testFile");
    file2.setExtension("xml");
    file2.setSize(testFile.length);
    file2.apply();

    file2.putFile(testFile);

    try
    {
      // Delete the new vault
      tVault.delete();
    }
    finally
    {
      file2 = null;
    }

    VaultFileDAO tFile = VaultFileDAO.get(file.getId()).getBusinessDAO();

    // Ensure the the file in the existing vault is untouched.
    String path = tFile.filePath() + tFile.getVaultFileName();

    File f = new File(path);

    assertTrue(f.exists());
    assertTrue(f.isFile());

    // Ensure that the file is the same
    // Ensure that the file is the same
    BufferedReader bytes1 = new BufferedReader(new InputStreamReader(file.getFileStream()));
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

  @Transaction
  public static void createFile()
  {
    file = VaultFileDAO.newInstance();

    file.setFileName("testFile");
    file.setExtension("xml");
    file.setSize(testFile.length);
    file.apply();
  }

  @Transaction
  public static void createFile(byte[] bytes)
  {
    file = VaultFileDAO.newInstance();

    file.setFileName("testFile");
    file.setExtension("xml");
    file.setSize(bytes.length);
    file.apply();

    file.putFile(bytes);
  }

  @Transaction
  public static void runFailure(VaultFileDAO file) throws Exception
  {
    String input = "This is suppose to fail";

    file.putFile(input.getBytes());

    throw new Exception();
  }

  @Transaction
  public static void runDeleteFailure(VaultFileDAO file) throws Exception
  {
    file.delete();

    throw new Exception();
  }
}
