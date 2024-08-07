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
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdTableDAO;
import com.runwaysdk.session.Request;

public class MdTableTestSuite
{
  /**
   */
  @Request
  @BeforeClass
  public static void classSetUp()
  {
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness1.apply();

    MdAttributeCharacterDAO mdAttrCharMdBusiness = TestFixtureFactory.addCharacterAttribute(mdBusiness1, TestFixConst.ATTRIBUTE_CHARACTER);
    mdAttrCharMdBusiness.apply();

    MdTableDAO mdTable = TestFixtureFactory.createMdTableForMdBusiness1();
    mdTable.apply();

    MdAttributeCharacterDAO mdAttrCharMdTable = TestFixtureFactory.addCharacterAttribute(mdTable, TestFixConst.ATTRIBUTE_CHARACTER);
    mdAttrCharMdTable.setValue(MdAttributeCharacterInfo.COLUMN_NAME, mdAttrCharMdBusiness.getColumnName());
    mdAttrCharMdTable.apply();
  }

  /**
   * Deletes the abstract relationship.
   */
  @Request
  @AfterClass
  public static void classTearDown()
  {
    MdBusinessDAO testClassMdBusiness1 = MdBusinessDAO.getMdBusinessDAO(TestFixConst.TEST_CLASS1_TYPE).getBusinessDAO();
    testClassMdBusiness1.delete();

    MdTableDAO mdTable = MdTableDAO.getMdTableDAO(TestFixConst.TEST_TABLE1_TYPE).getBusinessDAO();
    mdTable.delete();
  }

  /**
   * 
   */
  @Request
  @Test
  public void testMdTable1()
  {
    BusinessDAO bus1 = BusinessDAO.newInstance(TestFixConst.TEST_CLASS1_TYPE);
    bus1.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "testVal1");
    bus1.apply();

    try
    {

    }
    finally
    {
      bus1.delete();
    }
  }

}
