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

import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class KeyTest extends TestCase
{
  private static MdBusinessDAO  mdBusiness;

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(KeyTest.class);

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

  protected static void classSetUp()
  {
    mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setGenerateMdController(false);
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();
  }

  protected static void classTearDown()
  {
    TestFixtureFactory.delete(mdBusiness);
  }

  public void testIndex()
  {
    MdAttributeConcreteDAOIF mdAttribute = mdBusiness.definesAttribute(EntityInfo.KEY);
    AttributeEnumerationIF attribute = (AttributeEnumerationIF) mdAttribute.getAttributeIF(MdAttributeEnumerationInfo.INDEX_TYPE);
    EnumerationItemDAOIF[] dereference = attribute.dereference();

    assertEquals(1, dereference.length);
    assertEquals(IndexTypes.UNIQUE_INDEX.getId(), dereference[0].getId());
  }

  public void testDuplicateKey()
  {
    String key = "test_key";

    BusinessDAO business1 = BusinessDAO.newInstance(mdBusiness.definesType());
    business1.setKey(key);
    business1.apply();

    BusinessDAOIF business1IF = BusinessDAO.get(business1.getId());

    assertEquals(key, business1IF.getKey());

    try
    {
      BusinessDAO business2 = BusinessDAO.newInstance(mdBusiness.definesType());
      business2.setKey(key);
      business2.apply();

      fail("Failed to throw duplicate database exception when keys are the same");
    }
    catch(DuplicateDataException e)
    {
      //This is expected
    }
  }
}
