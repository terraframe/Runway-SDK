/**
*
*/
package com.runwaysdk.dataaccess;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.Assert;

import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeMultiReferenceInfo;
import com.runwaysdk.constants.TermInfo;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeMultiReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdTermDAO;
import com.runwaysdk.dataaccess.metadata.MdViewDAO;

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
public class MdAttributeMultiReferenceTest extends TestCase
{
  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(MdAttributeMultiReferenceTest.class);

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
   * 
   */
  public static void classTearDown()
  {
  }

  /**
   * 
   */
  public static void classSetUp()
  {
  }

  public void testCreateOnEntity()
  {
    String tableName = "class1_test_multi_reference";
    MdTermDAO mdTerm = TestFixtureFactory.createMdTerm();
    mdTerm.apply();

    try
    {
      MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
      mdBusiness.apply();

      try
      {
        MdAttributeMultiReferenceDAO mdAttributeMultReference = MdAttributeMultiReferenceDAO.newInstance();
        mdAttributeMultReference.setValue(MdAttributeMultiReferenceInfo.NAME, "testMultiReference");
        mdAttributeMultReference.setStructValue(MdAttributeMultiReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term Test");
        mdAttributeMultReference.setValue(MdAttributeMultiReferenceInfo.REF_MD_ENTITY, mdTerm.getId());
        mdAttributeMultReference.setValue(MdAttributeMultiReferenceInfo.DEFINING_MD_CLASS, mdBusiness.getId());
        mdAttributeMultReference.apply();

        try
        {
          MdAttributeMultiReferenceDAOIF result = MdAttributeMultiReferenceDAO.get(mdAttributeMultReference.getId());

          Assert.assertNotNull(result);
          Assert.assertEquals(result.getValue(MdAttributeMultiReferenceInfo.NAME), mdAttributeMultReference.getValue(MdAttributeMultiReferenceInfo.NAME));
          Assert.assertEquals(result.getStructValue(MdAttributeMultiReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), mdAttributeMultReference.getStructValue(MdAttributeMultiReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
          Assert.assertEquals(result.getValue(MdAttributeMultiReferenceInfo.REF_MD_ENTITY), mdAttributeMultReference.getValue(MdAttributeMultiReferenceInfo.REF_MD_ENTITY));
          Assert.assertEquals(result.getValue(MdAttributeMultiReferenceInfo.DEFINING_MD_CLASS), mdAttributeMultReference.getValue(MdAttributeMultiReferenceInfo.DEFINING_MD_CLASS));
          Assert.assertEquals(tableName, result.getValue(MdAttributeMultiReferenceInfo.TABLE_NAME));

          // Ensure the set table was created
          Assert.assertTrue(Database.tableExists(tableName));
        }
        finally
        {
          TestFixtureFactory.delete(mdAttributeMultReference);

          // Ensure the set table was deleted
          Assert.assertFalse(Database.tableExists(tableName));
        }
      }
      finally
      {
        TestFixtureFactory.delete(mdBusiness);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdTerm);
    }
  }

  public void testCreateOnView()
  {
    String tableName = "view1_test_multi_reference";
    MdTermDAO mdTerm = TestFixtureFactory.createMdTerm();
    mdTerm.apply();

    try
    {
      MdViewDAO mdView = TestFixtureFactory.createMdView1();
      mdView.apply();

      try
      {
        MdAttributeMultiReferenceDAO mdAttributeMultReference = MdAttributeMultiReferenceDAO.newInstance();
        mdAttributeMultReference.setValue(MdAttributeMultiReferenceInfo.NAME, "testMultiReference");
        mdAttributeMultReference.setStructValue(MdAttributeMultiReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term Test");
        mdAttributeMultReference.setValue(MdAttributeMultiReferenceInfo.REF_MD_ENTITY, mdTerm.getId());
        mdAttributeMultReference.setValue(MdAttributeMultiReferenceInfo.DEFINING_MD_CLASS, mdView.getId());
        mdAttributeMultReference.apply();

        try
        {
          MdAttributeMultiReferenceDAOIF result = MdAttributeMultiReferenceDAO.get(mdAttributeMultReference.getId());

          Assert.assertNotNull(result);
          Assert.assertEquals(result.getValue(MdAttributeMultiReferenceInfo.NAME), mdAttributeMultReference.getValue(MdAttributeMultiReferenceInfo.NAME));
          Assert.assertEquals(result.getStructValue(MdAttributeMultiReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), mdAttributeMultReference.getStructValue(MdAttributeMultiReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
          Assert.assertEquals(result.getValue(MdAttributeMultiReferenceInfo.REF_MD_ENTITY), mdAttributeMultReference.getValue(MdAttributeMultiReferenceInfo.REF_MD_ENTITY));
          Assert.assertEquals(result.getValue(MdAttributeMultiReferenceInfo.DEFINING_MD_CLASS), mdAttributeMultReference.getValue(MdAttributeMultiReferenceInfo.DEFINING_MD_CLASS));
          Assert.assertEquals(tableName, result.getValue(MdAttributeMultiReferenceInfo.TABLE_NAME));

          // Ensure the set table was created
          Assert.assertFalse(Database.tableExists(tableName));
        }
        finally
        {
          TestFixtureFactory.delete(mdAttributeMultReference);

          // Ensure the set table was deleted
          Assert.assertFalse(Database.tableExists(tableName));
        }
      }
      finally
      {
        TestFixtureFactory.delete(mdView);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdTerm);
    }
  }

  public void testDefaultValue()
  {
    MdTermDAO mdTerm = TestFixtureFactory.createMdTerm();
    mdTerm.apply();

    try
    {
      BusinessDAO defaultValue = BusinessDAO.newInstance(mdTerm.definesType());
      defaultValue.setStructValue(TermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Term 1");
      defaultValue.apply();

      MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
      mdBusiness.apply();

      try
      {
        MdAttributeMultiReferenceDAO mdAttributeMultReference = MdAttributeMultiReferenceDAO.newInstance();
        mdAttributeMultReference.setValue(MdAttributeMultiReferenceInfo.NAME, "testMultiReference");
        mdAttributeMultReference.setStructValue(MdAttributeMultiReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term Test");
        mdAttributeMultReference.setValue(MdAttributeMultiReferenceInfo.REF_MD_ENTITY, mdTerm.getId());
        mdAttributeMultReference.setValue(MdAttributeMultiReferenceInfo.DEFINING_MD_CLASS, mdBusiness.getId());
        mdAttributeMultReference.setValue(MdAttributeMultiReferenceInfo.DEFAULT_VALUE, defaultValue.getId());
        mdAttributeMultReference.apply();

        try
        {
          MdAttributeMultiReferenceDAOIF result = MdAttributeMultiReferenceDAO.get(mdAttributeMultReference.getId());

          Assert.assertNotNull(result);
          Assert.assertEquals(defaultValue.getId(), result.getValue(MdAttributeMultiReferenceInfo.DEFAULT_VALUE));
        }
        finally
        {
          TestFixtureFactory.delete(mdAttributeMultReference);
        }
      }
      finally
      {
        TestFixtureFactory.delete(mdBusiness);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdTerm);
    }
  }

}
