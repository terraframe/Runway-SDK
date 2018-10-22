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

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;

public abstract class MdFormDAOTest
{
  /**
   * The underlying MdClass for the TestType forms.
   */
  protected static MdBusinessDAO  testTypeMd;

  protected static MdBusinessDAO  refTypeMd;

  protected static MdAttributeDAO charAttr;

  protected static MdAttributeDAO textAttr;

  protected static MdAttributeDAO dateAttr;

  protected static MdAttributeDAO doubleAttr;

  protected static MdAttributeDAO floatAttr;

  protected static MdAttributeDAO integerAttr;

  protected static MdAttributeDAO booleanAttr;

  protected static MdAttributeDAO longAttr;

  protected static MdAttributeDAO dateTimeAttr;

  protected static MdAttributeDAO timeAttr;

  protected static MdAttributeDAO decimalAttr;

  protected static MdAttributeDAO geoAttr;

  protected static MdAttributeDAO singleTermAttr;

  protected static MdAttributeDAO multipleTermAttr;

  protected static MdAttributeDAO singleGridAttr;

  protected static MdAttributeDAO pointAttr;

  protected static MdAttributeDAO referenceAttr;

  protected static MdAttributeDAO group1Int;

  protected static MdAttributeDAO group2Boolean;

  protected static final String   PACKAGE         = "com.runwaysdk.form";

  protected static final String   TEST_TYPE_NAME  = "TestType";

  protected static final String   TEST_TYPE_CLASS = PACKAGE + "." + TEST_TYPE_NAME;

  /**
   * Defines the underlying MdClasses that the forms represent.
   * 
   * TODO add MdRelationship, MdView, MdStruct form representations.
   */
  protected static void defineMdClasses()
  {
    // Define a test class
    testTypeMd = TestFixtureFactory.createMdBusiness1();
    testTypeMd.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    testTypeMd.apply();

    // reference class
    refTypeMd = TestFixtureFactory.createMdBusiness2();
    refTypeMd.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    refTypeMd.apply();

    charAttr = TestFixtureFactory.addCharacterAttribute(testTypeMd);
    charAttr.apply();

    textAttr = TestFixtureFactory.addTextAttribute(testTypeMd);
    textAttr.apply();

    dateAttr = TestFixtureFactory.addDateAttribute(testTypeMd);
    dateAttr.apply();

    dateTimeAttr = TestFixtureFactory.addDateTimeAttribute(testTypeMd);
    dateTimeAttr.apply();

    timeAttr = TestFixtureFactory.addTimeAttribute(testTypeMd);
    timeAttr.apply();

    doubleAttr = TestFixtureFactory.addDoubleAttribute(testTypeMd);
    doubleAttr.apply();

    decimalAttr = TestFixtureFactory.addDecimalAttribute(testTypeMd);
    decimalAttr.apply();

    floatAttr = TestFixtureFactory.addFloatAttribute(testTypeMd);
    floatAttr.apply();

    integerAttr = TestFixtureFactory.addIntegerAttribute(testTypeMd);
    integerAttr.apply();

    longAttr = TestFixtureFactory.addLongAttribute(testTypeMd);
    longAttr.apply();

    booleanAttr = TestFixtureFactory.addBooleanAttribute(testTypeMd);
    booleanAttr.apply();

    referenceAttr = TestFixtureFactory.addReferenceAttribute(testTypeMd, refTypeMd);
    referenceAttr.apply();

    geoAttr = TestFixtureFactory.addReferenceAttribute2(testTypeMd, refTypeMd);
    geoAttr.apply();

    singleTermAttr = TestFixtureFactory.addReferenceAttribute3(testTypeMd, refTypeMd);
    singleTermAttr.apply();

    multipleTermAttr = TestFixtureFactory.addReferenceAttribute4(testTypeMd, refTypeMd);
    multipleTermAttr.apply();

    singleGridAttr = TestFixtureFactory.addReferenceAttribute5(testTypeMd, refTypeMd);
    singleGridAttr.apply();

    pointAttr = TestFixtureFactory.addReferenceAttribute6(testTypeMd, refTypeMd);
    pointAttr.apply();

    group1Int = TestFixtureFactory.addIntegerAttribute(testTypeMd, "group1Int");
    group1Int.apply();

    group2Boolean = TestFixtureFactory.addBooleanAttribute(testTypeMd, "group2Boolean");
    group2Boolean.apply();
  }

  /**
   * Removes the Metadata defined in defineMdClasses()
   */
  protected static void destroyMdClasses()
  {
    delete(refTypeMd);
    delete(testTypeMd);
  }

  protected static void delete(EntityDAO obj)
  {
    if (obj != null && obj.isAppliedToDB())
    {
      obj.delete();
    }
  }
}
