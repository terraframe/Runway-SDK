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

import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.session.Request;

public class KeyTest
{
  private static MdBusinessDAO mdBusiness;

  @Request
  @BeforeClass
  public static void classSetUp()
  {
    mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();
  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    TestFixtureFactory.delete(mdBusiness);
  }

  @Request
  @Test
  public void testIndex()
  {
    MdAttributeConcreteDAOIF mdAttribute = mdBusiness.definesAttribute(EntityInfo.KEY);
    AttributeEnumerationIF attribute = (AttributeEnumerationIF) mdAttribute.getAttributeIF(MdAttributeEnumerationInfo.INDEX_TYPE);
    EnumerationItemDAOIF[] dereference = attribute.dereference();

    Assert.assertEquals(1, dereference.length);
    Assert.assertEquals(IndexTypes.UNIQUE_INDEX.getOid(), dereference[0].getOid());
  }

  @Request
  @Test
  public void testDuplicateKey()
  {
    String key = "test_key";

    BusinessDAO business1 = BusinessDAO.newInstance(mdBusiness.definesType());
    business1.setKey(key);
    business1.apply();

    BusinessDAOIF business1IF = BusinessDAO.get(business1.getOid());

    Assert.assertEquals(key, business1IF.getKey());

    try
    {
      BusinessDAO business2 = BusinessDAO.newInstance(mdBusiness.definesType());
      business2.setKey(key);
      business2.apply();

      Assert.fail("Failed to throw duplicate database exception when keys are the same");
    }
    catch (DuplicateDataException e)
    {
      // This is expected
    }
  }
}
