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
package com.runwaysdk.dataaccess;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.ReservedWordException;
import com.runwaysdk.dataaccess.metadata.ReservedWords;

public class ReservedWordsTest extends TestCase
{
  private static MdBusinessDAO mdAttTest;
  private static final String TESTNAME = "ReservedWordType";
  private static final String PACKAGENAME = "com.runwaysdk.test";
  
  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ReservedWordsTest.class);

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

  public static void classSetUp()
  {
    mdAttTest = MdBusinessDAO.newInstance();
    mdAttTest.setValue(MdBusinessInfo.TABLE_NAME, "PurpleMonkeyDishwasher");
    mdAttTest.setValue(MdBusinessInfo.NAME, TESTNAME);
    mdAttTest.setValue(MdBusinessInfo.PACKAGE, PACKAGENAME);
    mdAttTest.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdAttTest.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Reserved Words");
    mdAttTest.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Reserved Words");
    mdAttTest.setGenerateMdController(false);
    mdAttTest.apply();
  }

  public static void classTearDown()
  {
    MdBusinessDAOIF md = MdBusinessDAO.getMdBusinessDAO(PACKAGENAME + "." + TESTNAME);
    md.getBusinessDAO().delete();
  }

  public static void main(String args[])
  {
    TestSuite suite = new TestSuite();

    suite.addTest(ReservedWordsTest.suite());

    junit.textui.TestRunner.run(new EntityMasterTestSetup(ReservedWordsTest.suite()));
  }

  public void testSQL() throws Exception
  {
    // Words in lists
    assertTrue ( ReservedWords.sqlContains("add") );
    assertTrue ( ReservedWords.sqlContains("float4") );
    assertTrue ( ReservedWords.sqlContains("blob") );
    assertTrue ( ReservedWords.sqlContains("as") );

    // Words not in lists
    assertTrue ( !ReservedWords.sqlContains("person") );
    assertTrue ( !ReservedWords.sqlContains("tree") );
    assertTrue ( !ReservedWords.sqlContains("apple") );
    assertTrue ( !ReservedWords.sqlContains("purple") );
  }

  public void testJava() throws Exception
  {
    // Words in lists
    assertTrue ( ReservedWords.javaContains("abstract") );
    assertTrue ( ReservedWords.javaContains("try") );
    assertTrue ( ReservedWords.javaContains("export") );
    assertTrue ( ReservedWords.javaContains("void") );

    // Words not in lists
    assertTrue ( !ReservedWords.javaContains("person") );
    assertTrue ( !ReservedWords.javaContains("tree") );
    assertTrue ( !ReservedWords.javaContains("apple") );
    assertTrue ( !ReservedWords.javaContains("purple") );
  }

  public void testBadTablename() throws Exception
  {
    try
    {
      MdBusinessDAO mdBusiness = MdBusinessDAO.newInstance();
      mdBusiness.setValue(MdBusinessInfo.TABLE_NAME, "blob");
      mdBusiness.setValue(MdBusinessInfo.NAME, "CollectionSub");
      mdBusiness.setValue(MdBusinessInfo.PACKAGE, "test.words");
      mdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Reserved Words");
      mdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Reserved Words");
      mdBusiness.setGenerateMdController(false);
      mdBusiness.apply();

      // If we get this far, it didn't throw the exception
      mdBusiness.delete();
      fail("Reserved word allowed for table name in new type");
    }
    catch (ReservedWordException e)
    {
      // This is expected
      assertTrue(e.getOrigin() == ReservedWordException.Origin.TABLE);
    }
  }
  
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
      mdBusiness.setGenerateMdController(false);
      mdBusiness.apply();

      // If we get this far, it didn't throw the exception, the test passed
      mdBusiness.delete();
    }
    catch (ReservedWordException e)
    {
      // The test failed
      fail("Reserved java word 'abstract' was not allowed for table name in new type.");
    }
  }
  
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
      mdBusiness.setGenerateMdController(false);
      mdBusiness.apply();

      // If we get this far, it didn't throw the exception, the test failed
      mdBusiness.delete();
      fail("Reserved Java word 'if' was allowed for type name in new type.");
    }
    catch (ReservedWordException e)
    {
      // The test passed
      assertTrue(e.getOrigin() == ReservedWordException.Origin.TYPE);
    }
  }
  
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
      mdBusiness.setGenerateMdController(false);
      mdBusiness.apply();

      // If we get this far, it didn't throw the exception, the test passed
      mdBusiness.delete();
    }
    catch (ReservedWordException e)
    {
      // The test failed
      fail("Reserved SQL word 'blob' was not allowed for type name in new type.");
    }
  }
  public void testBadColumnName() throws Exception
  {
    try
    {
      MdAttributeBooleanDAO mdAttributeBoolean = MdAttributeBooleanDAO.newInstance();
      mdAttributeBoolean.setValue(MdAttributeBooleanInfo.NAME, "PurpleMonkeyDishwasher");
      mdAttributeBoolean.setValue(MdAttributeBooleanInfo.COLUMN_NAME, "blob");
      mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
      mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
      mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Boolean");
      mdAttributeBoolean.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdAttTest.getId());
      mdAttributeBoolean.apply();

      // If we get this far, it didn't throw the exception, the test failed
      fail("Reserved SQL word 'blob' was allowed for a column name in new type.");
    }
    catch (ReservedWordException e)
    {
      // The test passed
      assertTrue(e.getOrigin() == ReservedWordException.Origin.COLUMN);
    }
  }
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
      mdAttributeBoolean.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdAttTest.getId());
      mdAttributeBoolean.apply();

      // If we get this far, it didn't throw the exception, the test passed
    }
    catch (ReservedWordException e)
    {
      fail("Reserved Java word 'abstract' was not allowed for a column name in new type.");
    }
  }
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
      mdAttributeBoolean.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdAttTest.getId());
      mdAttributeBoolean.apply();

      // If we get this far, it didn't throw the exception, the test failed
      fail("Reserved Java word 'if' was allowed for an attribute name in new type.");
    }
    catch (ReservedWordException e)
    {
      // The test passed
      assertTrue(e.getOrigin() == ReservedWordException.Origin.ATTRIBUTE);
    }
  }
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
      mdAttributeBoolean.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdAttTest.getId());
      mdAttributeBoolean.apply();

      // If we get this far, it didn't throw the exception, the test passed
    }
    catch (ReservedWordException e)
    {
      fail("Reserved SQL word 'blob' was not allowed for an attribute name in new type.");
    }
  }
  public void testInvalidCopiedColumnName() throws Exception
  {
    try
    {
      MdAttributeBooleanDAO mdAttributeBoolean = MdAttributeBooleanDAO.newInstance();
      mdAttributeBoolean.setValue(MdAttributeBooleanInfo.NAME, "Access");
      mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
      mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
      mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Boolean");
      mdAttributeBoolean.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdAttTest.getId());
      mdAttributeBoolean.apply();

      // If we get this far, it didn't throw the exception, the test failed
      fail("Reserved SQL attribute name 'Access' was copied to the missing column name and was accepted by the system.");
    }
    catch (ReservedWordException e)
    {
      // The test passed
      assertTrue(e.getOrigin() == ReservedWordException.Origin.COLUMN);
    }
  }
}
