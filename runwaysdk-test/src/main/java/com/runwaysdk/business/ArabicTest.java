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
package com.runwaysdk.business;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdPackage;
import com.runwaysdk.session.Request;

public class ArabicTest
{

  private static String        pack = "test.arabic";

  private static MdBusinessDAO testMdBusiness;

  @Request
  @BeforeClass
  public static void classSetUp()
  {
    MdBusinessDAOIF enumMasterMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EnumerationMasterInfo.CLASS);

    testMdBusiness = MdBusinessDAO.newInstance();
    // testMdBusiness.setValue(MdBusinessInfo.NAME, "ArabicTest");
    testMdBusiness.setValue(MdBusinessInfo.NAME, "سلام");
    // String tableName = testMdBusiness.getValue(MdBusinessInfo.NAME);
    testMdBusiness.setValue(MdBusinessInfo.PACKAGE, pack);
    testMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    testMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "ArabicTest");
    testMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "ArabicTest");
    testMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    testMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    testMdBusiness.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, enumMasterMdBusinessIF.getOid());
    testMdBusiness.apply();
  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    new MdPackage(pack).delete();
  }

  @Request
  @Test
  public void testBusinessValueExistence()
  {
    /*
     * BusinessDAO businessDAO =
     * BusinessDAO.newInstance(testMdBusiness.definesType());
     * businessDAO.setValue(EnumerationMasterInfo.NAME, "FICTION");
     * businessDAO.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL,
     * MdAttributeLocalInfo.DEFAULT_LOCALE, "EnglishTest"); businessDAO.apply();
     */
    Assert.assertEquals("سلام", testMdBusiness.getTableName());
  }

}
