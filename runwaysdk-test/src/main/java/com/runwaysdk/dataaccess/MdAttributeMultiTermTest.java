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
/**
*
*/
package com.runwaysdk.dataaccess;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeMultiTermInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.constants.MdViewInfo;
import com.runwaysdk.constants.TermInfo;
import com.runwaysdk.dataaccess.attributes.InvalidReferenceException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeMultiTermDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdTermDAO;
import com.runwaysdk.dataaccess.metadata.MdViewDAO;
import com.runwaysdk.session.Request;

public class MdAttributeMultiTermTest
{
  /**
   * 
   */
  @Request
  @AfterClass
  public static void classTearDown()
  {
  }

  /**
   * 
   */
  @Request
  @BeforeClass
  public static void classSetUp()
  {
  }

  @Request
  @Test
  public void testCreateOnEntity()
  {
    String tableName = "class1_test_multi_term";
    MdTermDAO mdTerm = TestFixtureFactory.createMdTerm();
    mdTerm.setValue(MdTermInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTerm.apply();

    try
    {
      MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
      mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdBusiness.apply();

      try
      {
        MdAttributeMultiTermDAO mdAttributeMultiTerm = MdAttributeMultiTermDAO.newInstance();
        mdAttributeMultiTerm.setValue(MdAttributeMultiTermInfo.NAME, "testMultiTerm");
        mdAttributeMultiTerm.setStructValue(MdAttributeMultiTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term Test");
        mdAttributeMultiTerm.setValue(MdAttributeMultiTermInfo.REF_MD_ENTITY, mdTerm.getOid());
        mdAttributeMultiTerm.setValue(MdAttributeMultiTermInfo.DEFINING_MD_CLASS, mdBusiness.getOid());
        mdAttributeMultiTerm.apply();

        try
        {
          MdAttributeMultiTermDAOIF result = MdAttributeMultiTermDAO.get(mdAttributeMultiTerm.getOid());

          Assert.assertNotNull(result);
          Assert.assertEquals(result.getValue(MdAttributeMultiTermInfo.NAME), mdAttributeMultiTerm.getValue(MdAttributeMultiTermInfo.NAME));
          Assert.assertEquals(result.getStructValue(MdAttributeMultiTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), mdAttributeMultiTerm.getStructValue(MdAttributeMultiTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
          Assert.assertEquals(result.getValue(MdAttributeMultiTermInfo.REF_MD_ENTITY), mdAttributeMultiTerm.getValue(MdAttributeMultiTermInfo.REF_MD_ENTITY));
          Assert.assertEquals(result.getValue(MdAttributeMultiTermInfo.DEFINING_MD_CLASS), mdAttributeMultiTerm.getValue(MdAttributeMultiTermInfo.DEFINING_MD_CLASS));
          Assert.assertEquals(tableName, result.getValue(MdAttributeMultiTermInfo.TABLE_NAME));

          // Ensure the set table was created
          Assert.assertTrue(Database.tableExists(tableName));
        }
        finally
        {
          TestFixtureFactory.delete(mdAttributeMultiTerm);

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

  @Request
  @Test
  public void testCreateOnView()
  {
    String tableName = "view1_test_multi_term";
    MdTermDAO mdTerm = TestFixtureFactory.createMdTerm();
    mdTerm.setValue(MdTermInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTerm.apply();

    try
    {
      MdViewDAO mdView = TestFixtureFactory.createMdView1();
      mdView.setValue(MdViewInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdView.apply();

      try
      {
        MdAttributeMultiTermDAO mdAttributeMultiTerm = MdAttributeMultiTermDAO.newInstance();
        mdAttributeMultiTerm.setValue(MdAttributeMultiTermInfo.NAME, "testMultiTerm");
        mdAttributeMultiTerm.setStructValue(MdAttributeMultiTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term Test");
        mdAttributeMultiTerm.setValue(MdAttributeMultiTermInfo.REF_MD_ENTITY, mdTerm.getOid());
        mdAttributeMultiTerm.setValue(MdAttributeMultiTermInfo.DEFINING_MD_CLASS, mdView.getOid());
        mdAttributeMultiTerm.apply();

        try
        {
          MdAttributeMultiTermDAOIF result = MdAttributeMultiTermDAO.get(mdAttributeMultiTerm.getOid());

          Assert.assertNotNull(result);
          Assert.assertEquals(result.getValue(MdAttributeMultiTermInfo.NAME), mdAttributeMultiTerm.getValue(MdAttributeMultiTermInfo.NAME));
          Assert.assertEquals(result.getStructValue(MdAttributeMultiTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), mdAttributeMultiTerm.getStructValue(MdAttributeMultiTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
          Assert.assertEquals(result.getValue(MdAttributeMultiTermInfo.REF_MD_ENTITY), mdAttributeMultiTerm.getValue(MdAttributeMultiTermInfo.REF_MD_ENTITY));
          Assert.assertEquals(result.getValue(MdAttributeMultiTermInfo.DEFINING_MD_CLASS), mdAttributeMultiTerm.getValue(MdAttributeMultiTermInfo.DEFINING_MD_CLASS));
          Assert.assertEquals(tableName, result.getValue(MdAttributeMultiTermInfo.TABLE_NAME));

          // Ensure the set table was created
          Assert.assertFalse(Database.tableExists(tableName));
        }
        finally
        {
          TestFixtureFactory.delete(mdAttributeMultiTerm);

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

  @Request
  @Test
  public void testDefaultValue()
  {
    MdTermDAO mdTerm = TestFixtureFactory.createMdTerm();
    mdTerm.setValue(MdTermInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTerm.apply();

    try
    {
      BusinessDAO defaultValue = BusinessDAO.newInstance(mdTerm.definesType());
      defaultValue.setStructValue(TermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Term 1");
      defaultValue.apply();

      MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
      mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdBusiness.apply();

      try
      {
        MdAttributeMultiTermDAO mdAttributeMultiTerm = MdAttributeMultiTermDAO.newInstance();
        mdAttributeMultiTerm.setValue(MdAttributeMultiTermInfo.NAME, "testMultiTerm");
        mdAttributeMultiTerm.setStructValue(MdAttributeMultiTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term Test");
        mdAttributeMultiTerm.setValue(MdAttributeMultiTermInfo.REF_MD_ENTITY, mdTerm.getOid());
        mdAttributeMultiTerm.setValue(MdAttributeMultiTermInfo.DEFINING_MD_CLASS, mdBusiness.getOid());
        mdAttributeMultiTerm.setValue(MdAttributeMultiTermInfo.DEFAULT_VALUE, defaultValue.getOid());
        mdAttributeMultiTerm.apply();

        try
        {
          MdAttributeMultiTermDAOIF result = MdAttributeMultiTermDAO.get(mdAttributeMultiTerm.getOid());

          Assert.assertNotNull(result);
          Assert.assertEquals(defaultValue.getOid(), result.getValue(MdAttributeMultiTermInfo.DEFAULT_VALUE));
        }
        finally
        {
          TestFixtureFactory.delete(mdAttributeMultiTerm);
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

  @Request
  @Test
  public void testInvalidType()
  {
    MdBusinessDAO mdReference = TestFixtureFactory.createMdBusiness1();
    mdReference.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdReference.apply();

    try
    {
      MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness2();
      mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdBusiness.apply();

      try
      {
        MdAttributeMultiTermDAO mdAttributeMultiTerm = MdAttributeMultiTermDAO.newInstance();
        mdAttributeMultiTerm.setValue(MdAttributeMultiTermInfo.NAME, "testMultiTerm");
        mdAttributeMultiTerm.setStructValue(MdAttributeMultiTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MultiTerm Test");
        mdAttributeMultiTerm.setValue(MdAttributeMultiTermInfo.REF_MD_ENTITY, mdReference.getOid());
        mdAttributeMultiTerm.setValue(MdAttributeMultiTermInfo.DEFINING_MD_CLASS, mdBusiness.getOid());
        mdAttributeMultiTerm.apply();

        TestFixtureFactory.delete(mdAttributeMultiTerm);

        Assert.fail("Able to create a MdAttributeTerm which doesn't reference a MdTerm");
      }
      catch (InvalidReferenceException e)
      {
        // This is expected
      }
      finally
      {
        TestFixtureFactory.delete(mdBusiness);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdReference);
    }
  }

}
