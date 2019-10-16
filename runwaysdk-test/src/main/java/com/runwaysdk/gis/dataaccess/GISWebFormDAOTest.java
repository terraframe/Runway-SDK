/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.gis.dataaccess;

import java.util.Locale;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.constants.MdWebAttributeInfo;
import com.runwaysdk.constants.MdWebFieldInfo;
import com.runwaysdk.constants.MdWebFormInfo;
import com.runwaysdk.constants.MdWebHeaderInfo;
import com.runwaysdk.constants.TypeInfo;
import com.runwaysdk.dataaccess.metadata.MdFieldDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFormDAO;
import com.runwaysdk.dataaccess.metadata.MdWebHeaderDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.gis.GISAbstractTest;
import com.runwaysdk.gis.GISMasterTestSetup;
import com.runwaysdk.gis.dataaccess.metadata.MdWebPointDAO;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.metadata.MdWebHeader;
import com.runwaysdk.system.metadata.MetadataDisplayLabel;

public class GISWebFormDAOTest extends GISAbstractTest
{
  private static TypeInfo      FORM_CLASS = new TypeInfo(GISMasterTestSetup.JUNIT_PACKAGE, "GISTestForm");

  private static MdWebFormDAO  testForm;

  public static final String   TEST_POINT = "testPoint";

  private static MdWebPointDAO mdWebPoint;

  private static int           fieldOrder = 0;

  private static String nextOrder()
  {
    return Integer.toString(++fieldOrder);
  }

  @Request
  @BeforeClass
  public static void classSetUp()
  {
    doClassSetUp(); // Request MUST come before Transaction
  }

  @Transaction
  public static void doClassSetUp()
  {
    // create a new form that wraps the test class in the master setup
    testForm = MdWebFormDAO.newInstance();
    testForm.setValue(MdWebFormInfo.NAME, FORM_CLASS.getTypeName());
    testForm.setValue(MdWebFormInfo.PACKAGE, FORM_CLASS.getPackageName());
    testForm.setValue(MdWebFormInfo.FORM_NAME, FORM_CLASS.getTypeName());
    testForm.setStructValue(MdWebFormInfo.DISPLAY_LABEL, MetadataDisplayLabel.DEFAULTLOCALE, "GIS Test Form.");
    testForm.setStructValue(MdWebFormInfo.DESCRIPTION, MetadataDisplayLabel.DEFAULTLOCALE, "A GIS Test Form for Geometry Attributes");
    testForm.setValue(MdWebFormInfo.FORM_MD_CLASS, GISMasterTestSetup.testClassMdBusinessDAO.getOid());
    testForm.apply();

    mdWebPoint = MdWebPointDAO.newInstance();
    mdWebPoint.setValue(MdWebFieldInfo.FIELD_NAME, TEST_POINT);
    mdWebPoint.setStructValue(MdWebFieldInfo.DISPLAY_LABEL, MetadataDisplayLabel.DEFAULTLOCALE, "Test Point");
    mdWebPoint.setStructValue(MdWebFieldInfo.DESCRIPTION, MetadataDisplayLabel.DEFAULTLOCALE, "Test Point Desc");
    mdWebPoint.setValue(MdWebFieldInfo.DEFINING_MD_FORM, testForm.getOid());
    mdWebPoint.setValue(MdWebAttributeInfo.DEFINING_MD_ATTRIBUTE, GISMasterTestSetup.mdAttributePointDAO.getOid());
    mdWebPoint.setValue(MdWebAttributeInfo.FIELD_ORDER, nextOrder());
    mdWebPoint.apply();

    // header
    MdFieldDAO headerField = MdWebHeaderDAO.newInstance();
    headerField.setValue(MdWebHeaderInfo.FIELD_NAME, "testHeader");
    headerField.setStructValue(MdWebHeaderInfo.DISPLAY_LABEL, MetadataDisplayLabel.DEFAULTLOCALE, "Test Header");
    headerField.setStructValue(MdWebHeaderInfo.DESCRIPTION, MetadataDisplayLabel.DEFAULTLOCALE, "Test Header Desc");
    headerField.setValue(MdWebHeaderInfo.DEFINING_MD_FORM, testForm.getOid());
    headerField.setStructValue(MdWebHeader.HEADERTEXT, MetadataDisplayLabel.DEFAULTLOCALE, "This is a test header");
    headerField.setValue(MdWebAttributeInfo.FIELD_ORDER, nextOrder());
    headerField.apply();
  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    doClassTearDown(); // Request MUST come before Transaction
  }

  @Transaction
  public static void doClassTearDown()
  {
    if (testForm != null && testForm.isAppliedToDB())
    {
      testForm.delete();
    }
  }

  @Request
  @Test
  public void testWebPoint()
  {
    MdWebPointDAO returned = (MdWebPointDAO) testForm.getMdField(TEST_POINT);

    Assert.assertEquals(mdWebPoint.getFieldName(), returned.getFieldName());
    Assert.assertEquals(mdWebPoint.getFieldOrder(), returned.getFieldOrder());
    Assert.assertEquals(mdWebPoint.getMdFormId(), returned.getMdFormId());
    Assert.assertEquals(mdWebPoint.getDisplayLabel(Locale.ENGLISH), returned.getDisplayLabel(Locale.ENGLISH));
    Assert.assertEquals(mdWebPoint.getDescription(Locale.ENGLISH), returned.getDescription(Locale.ENGLISH));
    Assert.assertEquals(mdWebPoint.getMdForm(), returned.getMdForm());
    Assert.assertEquals(mdWebPoint.getBusinessDAO(), returned.getBusinessDAO());
    Assert.assertEquals(mdWebPoint.getDefiningMdAttribute(), returned.getDefiningMdAttribute());
  }
}
