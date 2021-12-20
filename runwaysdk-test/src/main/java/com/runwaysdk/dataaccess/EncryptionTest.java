/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.dataaccess;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.HashMethods;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeHashInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeSymmetricInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.SymmetricMethods;
import com.runwaysdk.constants.TypeInfo;
import com.runwaysdk.dataaccess.attributes.entity.AttributeHash;
import com.runwaysdk.dataaccess.metadata.MdAttributeHashDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeSymmetricDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.session.Request;
import com.runwaysdk.util.Base64;

import sun.security.provider.Sun;

public class EncryptionTest
{
  // The oid of different attribute enumeration items (This is only hardcoded for
  // testing)

  private static final TypeInfo TEST_CLASS             = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "SiteLogin");

  private static final TypeInfo MD_ATTRIBUTE_HASH      = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "myPasswordHash");

  private static final TypeInfo MD_ATTRIBUTE_SYMMETRIC = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "myPasswordSymmetric");

  @Request
  @BeforeClass
  public static void classSetUp()
  {
    MdBusinessDAO testMdBusiness = MdBusinessDAO.newInstance();
    testMdBusiness.setValue(MdBusinessInfo.NAME, TEST_CLASS.getTypeName());
    testMdBusiness.setValue(MdBusinessInfo.PACKAGE, TEST_CLASS.getPackageName());
    testMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    testMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, TEST_CLASS.getTypeName() + " Test Type");
    testMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
    testMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    testMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    testMdBusiness.setValue(MdBusinessInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getOid());
    testMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    testMdBusiness.apply();

    MdAttributeHashDAO mdAttributeHash = MdAttributeHashDAO.newInstance();
    mdAttributeHash.setValue(MdAttributeHashInfo.NAME, MD_ATTRIBUTE_HASH.getTypeName());
    mdAttributeHash.setStructValue(MdAttributeHashInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A hash password");
    mdAttributeHash.setValue(MdAttributeHashInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeHash.setValue(MdAttributeHashInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeHash.addItem(MdAttributeHashInfo.HASH_METHOD, HashMethods.MD5.getOid());
    mdAttributeHash.setValue(MdAttributeHashInfo.DEFINING_MD_CLASS, testMdBusiness.getOid());
    mdAttributeHash.apply();
    MD_ATTRIBUTE_HASH.setOid(mdAttributeHash.getOid());

    MdAttributeSymmetricDAO mdAttributeSymmetric = MdAttributeSymmetricDAO.newInstance();
    mdAttributeSymmetric.setValue(MdAttributeSymmetricInfo.NAME, MD_ATTRIBUTE_SYMMETRIC.getTypeName());
    mdAttributeSymmetric.setStructValue(MdAttributeSymmetricInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A symmetric password");
    mdAttributeSymmetric.setValue(MdAttributeSymmetricInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeSymmetric.setValue(MdAttributeSymmetricInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeSymmetric.addItem(MdAttributeSymmetricInfo.SYMMETRIC_METHOD, SymmetricMethods.DES.getOid());
    mdAttributeSymmetric.setValue(MdAttributeSymmetricInfo.SECRET_KEY_SIZE, "56");
    mdAttributeSymmetric.setValue(MdAttributeSymmetricInfo.DEFINING_MD_CLASS, testMdBusiness.getOid());
    mdAttributeSymmetric.apply();
    MD_ATTRIBUTE_SYMMETRIC.setOid(mdAttributeSymmetric.getOid());
  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    MdBusinessDAO newMdBusiness = (MdBusinessDAO) ( MdBusinessDAO.getMdBusinessDAO(TEST_CLASS.getType()).getBusinessDAO() );
    newMdBusiness.delete();
  }

  /**
   * Test to make sure MD5 hashes are working correctly.
   */
  @Request
  @Test
  public void testMD5_Hash()
  {
    MdAttributeHashDAO passDO = null;
    try
    {
      passDO = (MdAttributeHashDAO) ( BusinessDAO.get(MD_ATTRIBUTE_HASH.getOid()) ).getBusinessDAO();
      passDO.addItem(MdAttributeHashInfo.HASH_METHOD, HashMethods.MD5.getOid());
      passDO.apply();

      BusinessDAO user = BusinessDAO.newInstance(TEST_CLASS.getType());
      user.setValue(MD_ATTRIBUTE_HASH.getTypeName(), "myPass123_MD5");

      String userId = user.apply();

      // manually hash the value here for comparison
      String manualHash = null;
      try
      {
        MessageDigest digest = MessageDigest.getInstance("MD5", new Sun());
        digest.update(new String("myPass123_MD5").getBytes());
        manualHash = Base64.encodeToString(digest.digest(), false);
      }
      catch (NoSuchAlgorithmException e)
      {
        e.printStackTrace();
      }

      // get the hash value from the core and compare to the manual hash
      AttributeHash pass = (AttributeHash) user.getAttributeIF(MD_ATTRIBUTE_HASH.getTypeName());
      if (!pass.encryptionEquals(manualHash, true) || !pass.encryptionEquals("myPass123_MD5", false))
      {
        Assert.fail("An MD5 hash value was not correctly computed by AttributeHash (value from the core).");
      }

      // get the hash value from the database and compare to the manual hash
      user = null;
      pass = null;
      user = BusinessDAO.get(userId).getBusinessDAO();
      pass = (AttributeHash) user.getAttributeIF(MD_ATTRIBUTE_HASH.getTypeName());
      if (!pass.encryptionEquals(manualHash, true) || !pass.encryptionEquals("myPass123_MD5", false))
      {
        Assert.fail("An MD5 hash value was not correctly computed by AttributeHash (value from the database).");
      }

      // get the hash value from the database and compare to the manual hash
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  /**
   * Test to make sure MD5 hashes are working correctly.
   */
  @Request
  @Test
  public void testSHA_Hash()
  {
    MdAttributeHashDAO passDO = null;
    try
    {

      passDO = (MdAttributeHashDAO) ( MdAttributeHashDAO.get(MD_ATTRIBUTE_HASH.getOid()) ).getBusinessDAO();
      passDO.addItem(MdAttributeHashInfo.HASH_METHOD, HashMethods.SHA.getOid());
      passDO.apply();

      BusinessDAO user = BusinessDAO.newInstance(TEST_CLASS.getType());
      user.setValue(MD_ATTRIBUTE_HASH.getTypeName(), "myPass123_SHA");

      String userId = user.apply();

      // manually hash the value here for comparison
      String manualHash = null;
      try
      {
        MessageDigest digest = MessageDigest.getInstance("SHA-1", new Sun());
        digest.update(new String("myPass123_SHA").getBytes());
        manualHash = Base64.encodeToString(digest.digest(), false);
      }
      catch (NoSuchAlgorithmException e)
      {
        Assert.fail(e.getMessage());
      }

      // get the hash value from the core and compare to the manual hash
      AttributeHash pass = (AttributeHash) user.getAttributeIF(MD_ATTRIBUTE_HASH.getTypeName());
      if (!pass.encryptionEquals(manualHash, true) || !pass.encryptionEquals("myPass123_SHA", false))
      {
        Assert.fail("An SHA hash value was not correctly computed by AttributeHash (value from the core).");
      }

      // get the hash value from the database and compare to the manual hash
      user = null;
      pass = null;
      user = BusinessDAO.get(userId).getBusinessDAO();
      pass = (AttributeHash) user.getAttributeIF(MD_ATTRIBUTE_HASH.getTypeName());
      if (!pass.encryptionEquals(manualHash, true) || !pass.encryptionEquals("myPass123_SHA", false))
      {
        Assert.fail("An SHA hash value was not correctly computed by AttributeHash (value from the database).");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  /**
   * Tests DES symmetric encryption
   */
  @Request
  @Test
  public void testDES_Symmetric()
  {
    MdAttributeSymmetricDAO passDO = null;
    try
    {
      passDO = (MdAttributeSymmetricDAO) ( MdAttributeSymmetricDAO.get(MD_ATTRIBUTE_SYMMETRIC.getOid()) ).getBusinessDAO();
      passDO.addItem(MdAttributeSymmetricInfo.SYMMETRIC_METHOD, SymmetricMethods.DES.getOid());
      passDO.setValue(MdAttributeSymmetricInfo.SECRET_KEY_SIZE, "56");
      passDO.apply();

      BusinessDAO user = BusinessDAO.newInstance(TEST_CLASS.getType());
      user.setValue(MD_ATTRIBUTE_SYMMETRIC.getTypeName(), "myPass12_DES");
      user.apply();

      // now make sure the encrypted has succeeded (by comparing it to the
      // original; it should be different).
      AttributeSymmetricIF crypto = (AttributeSymmetricIF) user.getAttributeIF(MD_ATTRIBUTE_SYMMETRIC.getTypeName());
      String before = crypto.getRawValue();
      if (before.equals("myPass12_DES"))
      {
        Assert.fail("AttributeSymmetric did not properly encrypt the data.");
      }

      // also make sure that the decrypted does equal the original
      String after = crypto.getValue();

      if (!after.equals("myPass12_DES"))
      {
        Assert.fail("AttributeSymmetric did not properly decrypt the data.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  /**
   * Tests AES symmetric encryption
   */
  @Request
  @Test
  public void testAES_Symmetric()
  {
    MdAttributeSymmetricDAO passDO = null;
    try
    {

      passDO = (MdAttributeSymmetricDAO) ( MdAttributeSymmetricDAO.get(MD_ATTRIBUTE_SYMMETRIC.getOid()) ).getBusinessDAO();
      passDO.addItem(MdAttributeSymmetricInfo.SYMMETRIC_METHOD, SymmetricMethods.AES.getOid());
      passDO.setValue(MdAttributeSymmetricInfo.SECRET_KEY_SIZE, "128");
      passDO.apply();

      BusinessDAO user = BusinessDAO.newInstance(TEST_CLASS.getType());
      user.setValue(MD_ATTRIBUTE_SYMMETRIC.getTypeName(), "myPass12_AES");
      user.apply();

      // now make sure the encrypted has succeeded (by comparing it to the
      // original; it should be different).
      AttributeSymmetricIF crypto = (AttributeSymmetricIF) user.getAttributeIF(MD_ATTRIBUTE_SYMMETRIC.getTypeName());
      String before = crypto.getRawValue();
      if (before.equals("myPass12_AES"))
      {
        Assert.fail("AttributeSymmetric did not properly encrypt the data.");
      }

      // also make sure that the decrypted does equal the original
      String after = crypto.getValue();
      if (!after.equals("myPass12_AES"))
      {
        Assert.fail("AttributeSymmetric did not properly decrypt the data.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  /**
   * Tests the encryptionEquals() method for symmetric encryption.
   */
  @Request
  @Test
  public void testSymmetricEquals()
  {
    MdAttributeSymmetricDAO passDO = null;
    try
    {
      passDO = (MdAttributeSymmetricDAO) ( MdAttributeSymmetricDAO.get(MD_ATTRIBUTE_SYMMETRIC.getOid()) ).getBusinessDAO();
      passDO.addItem(MdAttributeSymmetricInfo.SYMMETRIC_METHOD, SymmetricMethods.AES.getOid());
      passDO.setValue(MdAttributeSymmetricInfo.SECRET_KEY_SIZE, "128");
      passDO.apply();

      BusinessDAO user = BusinessDAO.newInstance(TEST_CLASS.getType());
      user.setValue(MD_ATTRIBUTE_SYMMETRIC.getTypeName(), "myPass12_EQUALS");
      user.apply();

      // now make sure the encrypted has succeeded (by comparing it to the
      // original; it should be different).
      AttributeSymmetricIF crypto = (AttributeSymmetricIF) user.getAttributeIF(MD_ATTRIBUTE_SYMMETRIC.getTypeName());
      String before = crypto.getRawValue();

      // test the compare method as well
      if (!crypto.encryptionEquals("myPass12_EQUALS", false) || !crypto.encryptionEquals(before, true))
      {
        Assert.fail("AttributeSymmetric.encryptionEquals() did not properly compare the values.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

}
