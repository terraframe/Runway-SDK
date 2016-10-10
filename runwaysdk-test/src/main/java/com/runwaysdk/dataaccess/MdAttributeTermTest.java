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
/**
*
*/
package com.runwaysdk.dataaccess;

import java.util.List;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.Assert;

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeTermInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.dataaccess.attributes.InvalidReferenceException;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeTermDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdTermDAO;

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
public class MdAttributeTermTest extends TestCase
{
  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(MdAttributeTermTest.class);

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

  public void testCreateAndGet()
  {
    MdTermDAO mdTerm = TestFixtureFactory.createMdTerm();
    mdTerm.setGenerateMdController(false);
    mdTerm.setValue(MdTermInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTerm.apply();

    try
    {
      MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
      mdBusiness.setGenerateMdController(false);
      mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdBusiness.apply();

      try
      {
        MdAttributeTermDAO mdAttributeTerm = MdAttributeTermDAO.newInstance();
        mdAttributeTerm.setValue(MdAttributeTermInfo.NAME, "testTerm");
        mdAttributeTerm.setStructValue(MdAttributeTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term Test");
        mdAttributeTerm.setValue(MdAttributeTermInfo.REF_MD_ENTITY, mdTerm.getId());
        mdAttributeTerm.setValue(MdAttributeTermInfo.DEFINING_MD_CLASS, mdBusiness.getId());
        mdAttributeTerm.apply();

        try
        {
          MdAttributeTermDAOIF result = MdAttributeTermDAO.get(mdAttributeTerm.getId());

          Assert.assertNotNull(result);
          Assert.assertEquals(result.getValue(MdAttributeTermInfo.NAME), mdAttributeTerm.getValue(MdAttributeTermInfo.NAME));
          Assert.assertEquals(result.getStructValue(MdAttributeTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), mdAttributeTerm.getStructValue(MdAttributeTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
          Assert.assertEquals(result.getValue(MdAttributeTermInfo.REF_MD_ENTITY), mdAttributeTerm.getValue(MdAttributeTermInfo.REF_MD_ENTITY));
          Assert.assertEquals(result.getValue(MdAttributeTermInfo.DEFINING_MD_CLASS), mdAttributeTerm.getValue(MdAttributeTermInfo.DEFINING_MD_CLASS));
        }
        finally
        {
          TestFixtureFactory.delete(mdAttributeTerm);
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

  public void testInvalidType()
  {
    MdBusinessDAO mdReference = TestFixtureFactory.createMdBusiness1();
    mdReference.setGenerateMdController(false);
    mdReference.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdReference.apply();

    try
    {
      MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness2();
      mdBusiness.setGenerateMdController(false);
      mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdBusiness.apply();

      try
      {
        MdAttributeTermDAO mdAttributeTerm = MdAttributeTermDAO.newInstance();
        mdAttributeTerm.setValue(MdAttributeTermInfo.NAME, "testTerm");
        mdAttributeTerm.setStructValue(MdAttributeTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term Test");
        mdAttributeTerm.setValue(MdAttributeTermInfo.REF_MD_ENTITY, mdReference.getId());
        mdAttributeTerm.setValue(MdAttributeTermInfo.DEFINING_MD_CLASS, mdBusiness.getId());
        mdAttributeTerm.apply();

        TestFixtureFactory.delete(mdAttributeTerm);

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

  public void testAddAttributeRoots()
  {
    MdTermDAO mdTerm = TestFixtureFactory.createMdTerm();
    mdTerm.setGenerateMdController(false);
    mdTerm.setValue(MdTermInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTerm.apply();

    try
    {
      BusinessDAO term = BusinessDAO.newInstance(mdTerm.definesType());
      term.setStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalCharacterInfo.DEFAULT_LOCALE, "Test Term");
      term.apply();

      MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
      mdBusiness.setGenerateMdController(false);
      mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdBusiness.apply();

      try
      {
        MdAttributeTermDAO mdAttributeTerm = MdAttributeTermDAO.newInstance();
        mdAttributeTerm.setValue(MdAttributeTermInfo.NAME, "testTerm");
        mdAttributeTerm.setStructValue(MdAttributeTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term Test");
        mdAttributeTerm.setValue(MdAttributeTermInfo.REF_MD_ENTITY, mdTerm.getId());
        mdAttributeTerm.setValue(MdAttributeTermInfo.DEFINING_MD_CLASS, mdBusiness.getId());
        mdAttributeTerm.apply();

        mdAttributeTerm.addAttributeRoot(term, true);

        List<RelationshipDAOIF> roots = mdAttributeTerm.getAllAttributeRoots();

        Assert.assertEquals(1, roots.size());
        Assert.assertEquals(term.getId(), roots.get(0).getChildId());
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
