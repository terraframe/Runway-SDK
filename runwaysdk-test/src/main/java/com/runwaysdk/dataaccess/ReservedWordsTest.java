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

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.ReservedWordException;
import com.runwaysdk.dataaccess.metadata.ReservedWords;
import com.runwaysdk.session.Request;

public class ReservedWordsTest
{
  private static MdBusinessDAO mdAttTest;

  private static final String  TESTNAME    = "ReservedWordType";

  private static final String  PACKAGENAME = "com.runwaysdk.test";

  @Request
  @BeforeClass
  public static void classSetUp()
  {
    mdAttTest = MdBusinessDAO.newInstance();
    mdAttTest.setValue(MdBusinessInfo.TABLE_NAME, "PurpleMonkeyDishwasher");
    mdAttTest.setValue(MdBusinessInfo.NAME, TESTNAME);
    mdAttTest.setValue(MdBusinessInfo.PACKAGE, PACKAGENAME);
    mdAttTest.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdAttTest.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Reserved Words");
    mdAttTest.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Reserved Words");
    mdAttTest.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdAttTest.apply();
  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    MdBusinessDAOIF md = MdBusinessDAO.getMdBusinessDAO(PACKAGENAME + "." + TESTNAME);
    md.getBusinessDAO().delete();
  }

  @Request
  @Test
  public void testSQL() throws Exception
  {
    // Words in lists
    Assert.assertTrue(ReservedWords.sqlContains("group"));
    Assert.assertTrue(ReservedWords.sqlContains("or"));
    Assert.assertTrue(ReservedWords.sqlContains("to"));
    Assert.assertTrue(ReservedWords.sqlContains("as"));

    // Words not in lists
    Assert.assertTrue(!ReservedWords.sqlContains("version"));
    Assert.assertTrue(!ReservedWords.sqlContains("tree"));
    Assert.assertTrue(!ReservedWords.sqlContains("apple"));
    Assert.assertTrue(!ReservedWords.sqlContains("purple"));
  }

  @Request
  @Test
  public void testJava() throws Exception
  {
    // Words in lists
    Assert.assertTrue(ReservedWords.javaContains("abstract"));
    Assert.assertTrue(ReservedWords.javaContains("try"));
    Assert.assertTrue(ReservedWords.javaContains("export"));
    Assert.assertTrue(ReservedWords.javaContains("void"));

    // Words not in lists
    Assert.assertTrue(!ReservedWords.javaContains("person"));
    Assert.assertTrue(!ReservedWords.javaContains("tree"));
    Assert.assertTrue(!ReservedWords.javaContains("apple"));
    Assert.assertTrue(!ReservedWords.javaContains("purple"));
  }

  @Request
  @Test
  public void testBadTablename() throws Exception
  {
    final String badWord = "true";
    
    try
    {
      MdBusinessDAO mdBusiness = MdBusinessDAO.newInstance();
      mdBusiness.setValue(MdBusinessInfo.TABLE_NAME, badWord);
      mdBusiness.setValue(MdBusinessInfo.NAME, "CollectionSub");
      mdBusiness.setValue(MdBusinessInfo.PACKAGE, "test.words");
      mdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Reserved Words");
      mdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Reserved Words");
      mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdBusiness.apply();

      // If we get this far, it didn't throw the exception
      mdBusiness.delete();
      Assert.fail("Reserved word [" + badWord + "] allowed for table name in new type");
    }
    catch (ReservedWordException e)
    {
      // This is expected
      Assert.assertTrue(e.getOrigin() == ReservedWordException.Origin.TABLE);
    }
  }

  @Request
  @Test
  public void testGoodTablename() throws Exception
  {
    try
    {
      MdBusinessDAO mdBusiness = MdBusinessDAO.newInstance();
      mdBusiness.setValue(MdBusinessInfo.TABLE_NAME, "abstract");
      mdBusiness.setValue(MdBusinessInfo.NAME, "CollectionSub");
      mdBusiness.setValue(MdBusinessInfo.PACKAGE, "test.words");
      mdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Reserved Words");
      mdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Reserved Words");
      mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdBusiness.apply();

      // If we get this far, it didn't throw the exception, the test passed
      mdBusiness.delete();
    }
    catch (ReservedWordException e)
    {
      // The test failed
      Assert.fail("Reserved java word 'abstract' was not allowed for table name in new type.");
    }
  }

  @Request
  @Test
  public void testBadTypename() throws Exception
  {
    try
    {
      MdBusinessDAO mdBusiness = MdBusinessDAO.newInstance();
      mdBusiness.setValue(MdBusinessInfo.TABLE_NAME, "PurpleMonk");
      mdBusiness.setValue(MdBusinessInfo.NAME, "If");
      mdBusiness.setValue(MdBusinessInfo.PACKAGE, "test.words");
      mdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Reserved Words");
      mdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Reserved Words");
      mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdBusiness.apply();

      // If we get this far, it didn't throw the exception, the test failed
      mdBusiness.delete();
      Assert.fail("Reserved Java word 'if' was allowed for type name in new type.");
    }
    catch (ReservedWordException e)
    {
      // The test passed
      Assert.assertTrue(e.getOrigin() == ReservedWordException.Origin.TYPE);
    }
  }

  @Request
  @Test
  public void testGoodTypename() throws Exception
  {
    try
    {
      MdBusinessDAO mdBusiness = MdBusinessDAO.newInstance();
      mdBusiness.setValue(MdBusinessInfo.TABLE_NAME, "PurpleMonk");
      mdBusiness.setValue(MdBusinessInfo.NAME, "Blob");
      mdBusiness.setValue(MdBusinessInfo.PACKAGE, "test.words");
      mdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Reserved Words");
      mdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Reserved Words");
      mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdBusiness.apply();

      // If we get this far, it didn't throw the exception, the test passed
      mdBusiness.delete();
    }
    catch (ReservedWordException e)
    {
      // The test failed
      Assert.fail("Reserved SQL word 'blob' was not allowed for type name in new type.");
    }
  }

  @Request
  @Test
  public void testBadColumnName() throws Exception
  {
    try
    {
      MdAttributeBooleanDAO mdAttributeBoolean = MdAttributeBooleanDAO.newInstance();
      mdAttributeBoolean.setValue(MdAttributeBooleanInfo.NAME, "PurpleMonkeyDishwasher");
      mdAttributeBoolean.setValue(MdAttributeBooleanInfo.COLUMN_NAME, "SELECT");
      mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
      mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
      mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Boolean");
      mdAttributeBoolean.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdAttTest.getOid());
      mdAttributeBoolean.apply();

      // If we get this far, it didn't throw the exception, the test failed
      Assert.fail("Reserved SQL word 'blob' was allowed for a column name in new type.");
    }
    catch (ReservedWordException e)
    {
      // The test passed
      Assert.assertTrue(e.getOrigin() == ReservedWordException.Origin.COLUMN);
    }
  }

  @Request
  @Test
  public void testGoodColumnName() throws Exception
  {
    try
    {
      MdAttributeBooleanDAO mdAttributeBoolean = MdAttributeBooleanDAO.newInstance();
      mdAttributeBoolean.setValue(MdAttributeBooleanInfo.NAME, "PurpleMonkey");
      mdAttributeBoolean.setValue(MdAttributeBooleanInfo.COLUMN_NAME, "abstract");
      mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
      mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
      mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Boolean");
      mdAttributeBoolean.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdAttTest.getOid());
      mdAttributeBoolean.apply();

      // If we get this far, it didn't throw the exception, the test passed
    }
    catch (ReservedWordException e)
    {
      Assert.fail("Reserved Java word 'abstract' was not allowed for a column name in new type.");
    }
  }

  @Request
  @Test
  public void testBadAttributeName() throws Exception
  {
    try
    {
      MdAttributeBooleanDAO mdAttributeBoolean = MdAttributeBooleanDAO.newInstance();
      mdAttributeBoolean.setValue(MdAttributeBooleanInfo.NAME, "If");
      mdAttributeBoolean.setValue(MdAttributeBooleanInfo.COLUMN_NAME, "PurpleMonkeyDishwasher");
      mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
      mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
      mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Boolean");
      mdAttributeBoolean.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdAttTest.getOid());
      mdAttributeBoolean.apply();

      // If we get this far, it didn't throw the exception, the test failed
      Assert.fail("Reserved Java word 'if' was allowed for an attribute name in new type.");
    }
    catch (ReservedWordException e)
    {
      // The test passed
      Assert.assertTrue(e.getOrigin() == ReservedWordException.Origin.ATTRIBUTE);
    }
  }

  @Request
  @Test
  public void testGoodAttributeName() throws Exception
  {
    try
    {
      MdAttributeBooleanDAO mdAttributeBoolean = MdAttributeBooleanDAO.newInstance();
      mdAttributeBoolean.setValue(MdAttributeBooleanInfo.NAME, "Blob");
      mdAttributeBoolean.setValue(MdAttributeBooleanInfo.COLUMN_NAME, "PurpleMonkeyDishwasher");
      mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
      mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
      mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Boolean");
      mdAttributeBoolean.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdAttTest.getOid());
      mdAttributeBoolean.apply();

      // If we get this far, it didn't throw the exception, the test passed
    }
    catch (ReservedWordException e)
    {
      Assert.fail("Reserved SQL word 'blob' was not allowed for an attribute name in new type.");
    }
  }

  @Request
  @Test
  public void testInvalidCopiedColumnName() throws Exception
  {
    final String badWord = "join";
    
    try
    {
      MdAttributeBooleanDAO mdAttributeBoolean = MdAttributeBooleanDAO.newInstance();
      mdAttributeBoolean.setValue(MdAttributeBooleanInfo.NAME, badWord);
      mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
      mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
      mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Boolean");
      mdAttributeBoolean.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdAttTest.getOid());
      mdAttributeBoolean.apply();

      // If we get this far, it didn't throw the exception, the test failed
      Assert.fail("Reserved SQL attribute name " + badWord + " was copied to the missing column name and was accepted by the system.");
    }
    catch (ReservedWordException e)
    {
      // The test passed
      Assert.assertTrue(e.getOrigin() == ReservedWordException.Origin.COLUMN);
    }
  }
}
