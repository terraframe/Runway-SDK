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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.ProblemException;
import com.runwaysdk.ProblemIF;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.MdAttributeDateUtil;
import com.runwaysdk.constants.MdAttributeDecimalInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeFloatInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLongInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.dataaccess.attributes.AttributeValueAboveRangeProblem;
import com.runwaysdk.dataaccess.attributes.AttributeValueBelowRangeProblem;
import com.runwaysdk.dataaccess.attributes.AttributeValueException;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDecimalDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeFloatDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLongDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;

public class AttributeValidationTest extends TestCase
{
  @Override
  public TestResult run()
  {
    return super.run();
  }

  @Override
  public void run(TestResult testResult)
  {
    super.run(testResult);
  }

  private static MdBusinessDAO mdBusiness;

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(AttributeValidationTest.class);

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
    mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setGenerateMdController(false);
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdAttributeDoubleDAO mdAttributeDouble = TestFixtureFactory.addDoubleAttribute(mdBusiness);
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.START_RANGE, "-4");
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.END_RANGE, "15");
    mdAttributeDouble.apply();

    MdAttributeDecimalDAO mdAttributeDecimal = TestFixtureFactory.addDecimalAttribute(mdBusiness);
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.REJECT_NEGATIVE, "false");
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.START_RANGE, "-4");
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.END_RANGE, "15");
    mdAttributeDecimal.apply();

    MdAttributeFloatDAO mdAttributeFloat = TestFixtureFactory.addFloatAttribute(mdBusiness);
    mdAttributeFloat.setValue(MdAttributeFloatInfo.REJECT_NEGATIVE, "false");
    mdAttributeFloat.setValue(MdAttributeFloatInfo.REJECT_POSITIVE, "false");
    mdAttributeFloat.setValue(MdAttributeFloatInfo.START_RANGE, "-4");
    mdAttributeFloat.setValue(MdAttributeFloatInfo.END_RANGE, "15");
    mdAttributeFloat.apply();

    MdAttributeLongDAO mdAttributeLong = TestFixtureFactory.addLongAttribute(mdBusiness);
    mdAttributeLong.setValue(MdAttributeLongInfo.START_RANGE, "-4");
    mdAttributeLong.setValue(MdAttributeLongInfo.END_RANGE, "15");
    mdAttributeLong.apply();

    MdAttributeIntegerDAO mdAttributeInteger = TestFixtureFactory.addIntegerAttribute(mdBusiness);
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.REJECT_POSITIVE, "false");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.START_RANGE, "-4");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.END_RANGE, "15");
    mdAttributeInteger.apply();

    MdAttributeDateDAO mdAttributeDateBeforeExclusive = TestFixtureFactory.addDateAttribute(mdBusiness, "attributeDateBeforeExclusive");
    mdAttributeDateBeforeExclusive.setValue(MdAttributeDateInfo.REQUIRED, "false");
    mdAttributeDateBeforeExclusive.setValue(MdAttributeDateInfo.INDEX_TYPE, IndexTypes.NO_INDEX.getId());
    mdAttributeDateBeforeExclusive.setValue(MdAttributeDateInfo.BEFORE_TODAY_EXCLUSIVE, "true");
    mdAttributeDateBeforeExclusive.apply();

    MdAttributeDateDAO mdAttributeDateBeforeInclusive = TestFixtureFactory.addDateAttribute(mdBusiness, "attributeDateBeforeInclusive");
    mdAttributeDateBeforeInclusive.setValue(MdAttributeDateInfo.REQUIRED, "false");
    mdAttributeDateBeforeInclusive.setValue(MdAttributeDateInfo.BEFORE_TODAY_INCLUSIVE, "true");
    mdAttributeDateBeforeInclusive.setValue(MdAttributeDateInfo.INDEX_TYPE, IndexTypes.NO_INDEX.getId());
    mdAttributeDateBeforeInclusive.apply();

    MdAttributeDateDAO mdAttributeDateAfterExclusive = TestFixtureFactory.addDateAttribute(mdBusiness, "attributeDateAfterExclusive");
    mdAttributeDateAfterExclusive.setValue(MdAttributeDateInfo.REQUIRED, "false");
    mdAttributeDateAfterExclusive.setValue(MdAttributeDateInfo.INDEX_TYPE, IndexTypes.NO_INDEX.getId());
    mdAttributeDateAfterExclusive.setValue(MdAttributeDateInfo.AFTER_TODAY_EXCLUSIVE, "true");
    mdAttributeDateAfterExclusive.setValue(MdAttributeDateInfo.DEFAULT_VALUE, "");
    mdAttributeDateAfterExclusive.apply();

    MdAttributeDateDAO mdAttributeDateAfterInclusive = TestFixtureFactory.addDateAttribute(mdBusiness, "attributeDateAfterInclusive");
    mdAttributeDateAfterInclusive.setValue(MdAttributeDateInfo.REQUIRED, "false");
    mdAttributeDateAfterInclusive.setValue(MdAttributeDateInfo.INDEX_TYPE, IndexTypes.NO_INDEX.getId());
    mdAttributeDateAfterInclusive.setValue(MdAttributeDateInfo.AFTER_TODAY_INCLUSIVE, "true");
    mdAttributeDateAfterInclusive.setValue(MdAttributeDateInfo.DEFAULT_VALUE, "");
    mdAttributeDateAfterInclusive.apply();

    MdAttributeDateDAO mdAttributeDate = TestFixtureFactory.addDateAttribute(mdBusiness);
    mdAttributeDate.setValue(MdAttributeDateInfo.REQUIRED, "false");
    mdAttributeDate.setValue(MdAttributeDateInfo.INDEX_TYPE, IndexTypes.NO_INDEX.getId());
    mdAttributeDate.setValue(MdAttributeDateInfo.DEFAULT_VALUE, "");
    mdAttributeDate.setValue(MdAttributeDateInfo.START_DATE, "2001-12-04");
    mdAttributeDate.setValue(MdAttributeDateInfo.END_DATE, "2001-12-12");
    mdAttributeDate.apply();
  }

  public static void classTearDown()
  {
    TestFixtureFactory.delete(mdBusiness);
  }

  public void testValidDouble()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.setValue("testDouble", "3");
    businessDAO.apply();
  }

  public void testUpperLimitDouble()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.setValue("testDouble", "15");
    businessDAO.apply();
  }

  public void testLowerLimitDouble()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.setValue("testDouble", "-4");
    businessDAO.apply();
  }

  public void testOverUpperLimitDouble() throws Exception
  {
    try
    {
      BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
      businessDAO.setValue("testDouble", "20");
      businessDAO.apply();

      fail("Able to set a value which is greater than the end range");
    }
    catch (ProblemException e)
    {
      // This is expected
      List<ProblemIF> problems = e.getProblems();
      boolean problemFound = false;
      for (ProblemIF p : problems)
      {
        if (p instanceof AttributeValueAboveRangeProblem)
          problemFound = true;
      }
      if (!problemFound)
        throw new Exception("AttributeValueAboveRangeProblem was not thrown");
    }
  }

  public void testOverLowerLimitDouble() throws Exception
  {
    try
    {
      BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
      businessDAO.setValue("testDouble", "-13");
      businessDAO.apply();

      fail("Able to set a value which is less than the start range");
    }
    catch (ProblemException e)
    {
      // This is expected
      List<ProblemIF> problems = e.getProblems();
      boolean problemFound = false;
      for (ProblemIF p : problems)
      {
        if (p instanceof AttributeValueBelowRangeProblem)
          problemFound = true;
      }
      if (!problemFound)
        throw new Exception("AttributeValueBelowRangeProblem was not thrown");
    }
  }

  public void testValidDecimal()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.setValue("testDecimal", "3");
    businessDAO.apply();
  }

  public void testUpperLimitDecimal()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.setValue("testDecimal", "15");
    businessDAO.apply();
  }

  public void testLowerLimitDecimal()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.setValue("testDecimal", "-4");
    businessDAO.apply();
  }

  public void testOverUpperLimitDecimal() throws Exception
  {
    try
    {
      BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
      businessDAO.setValue("testDecimal", "20");
      businessDAO.apply();

      fail("Able to set a value which is greater than the end range");
    }
    catch (ProblemException e)
    {
      // This is expected
      List<ProblemIF> problems = e.getProblems();
      boolean problemFound = false;
      for (ProblemIF p : problems)
      {
        if (p instanceof AttributeValueAboveRangeProblem)
          problemFound = true;
      }
      if (!problemFound)
        throw new Exception("AttributeValueAboveRangeProblem was not thrown");
    }
  }

  public void testOverLowerLimitDecimal() throws Exception
  {
    try
    {
      BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
      businessDAO.setValue("testDecimal", "-13");
      businessDAO.apply();

      fail("Able to set a value which is less than the start range");
    }
    catch (ProblemException e)
    {
      // This is expected
      List<ProblemIF> problems = e.getProblems();
      boolean problemFound = false;
      for (ProblemIF p : problems)
      {
        if (p instanceof AttributeValueBelowRangeProblem)
          problemFound = true;
      }
      if (!problemFound)
        throw new Exception("AttributeValueBelowRangeProblem was not thrown");
    }
  }

  public void testValidFloat()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.setValue("testFloat", "3");
    businessDAO.apply();
  }

  public void testUpperLimitFloat()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.setValue("testFloat", "15");
    businessDAO.apply();
  }

  public void testLowerLimitFloat()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.setValue("testFloat", "-4");
    businessDAO.apply();
  }

  public void testOverUpperLimitFloat() throws Exception
  {
    try
    {
      BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
      businessDAO.setValue("testFloat", "20");
      businessDAO.apply();

      fail("Able to set a value which is greater than the end range");
    }
    catch (ProblemException e)
    {
      // This is expected
      List<ProblemIF> problems = e.getProblems();
      boolean problemFound = false;
      for (ProblemIF p : problems)
      {
        if (p instanceof AttributeValueAboveRangeProblem)
          problemFound = true;
      }
      if (!problemFound)
        throw new Exception("AttributeValueAboveRangeProblem was not thrown");
    }
  }

  public void testOverLowerLimitFloat() throws Exception
  {
    try
    {
      BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
      businessDAO.setValue("testFloat", "-13");
      businessDAO.apply();

      fail("Able to set a value which is less than the start range");
    }
    catch (ProblemException e)
    {
      // This is expected
      List<ProblemIF> problems = e.getProblems();
      boolean problemFound = false;
      for (ProblemIF p : problems)
      {
        if (p instanceof AttributeValueBelowRangeProblem)
          problemFound = true;
      }
      if (!problemFound)
        throw new Exception("AttributeValueBelowRangeProblem was not thrown");
    }
  }

  public void testValidLong()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.setValue("testLong", "3");
    businessDAO.apply();
  }

  public void testUpperLimitLong()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.setValue("testLong", "15");
    businessDAO.apply();
  }

  public void testLowerLimitLong()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.setValue("testLong", "-4");
    businessDAO.apply();
  }

  public void testOverUpperLimitLong() throws Exception
  {
    try
    {
      BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
      businessDAO.setValue("testLong", "20");
      businessDAO.apply();

      fail("Able to set a value which is greater than the end range");
    }
    catch (ProblemException e)
    {
      // This is expected
      List<ProblemIF> problems = e.getProblems();
      boolean problemFound = false;
      for (ProblemIF p : problems)
      {
        if (p instanceof AttributeValueAboveRangeProblem)
          problemFound = true;
      }
      if (!problemFound)
        throw new Exception("AttributeValueAboveRangeProblem was not thrown");
    }
  }

  public void testOverLowerLimitLong() throws Exception
  {
    try
    {
      BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
      businessDAO.setValue("testLong", "-13");
      businessDAO.apply();

      fail("Able to set a value which is less than the start range");
    }
    catch (ProblemException e)
    {
      // This is expected
      List<ProblemIF> problems = e.getProblems();
      boolean problemFound = false;
      for (ProblemIF p : problems)
      {
        if (p instanceof AttributeValueBelowRangeProblem)
          problemFound = true;
      }
      if (!problemFound)
        throw new Exception("AttributeValueBelowRangeProblem was not thrown");
    }
  }

  public void testValidInteger()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.setValue("testInteger", "3");
    businessDAO.apply();
  }

  public void testUpperLimitInteger()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.setValue("testInteger", "15");
    businessDAO.apply();
  }

  public void testLowerLimitInteger()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.setValue("testInteger", "-4");
    businessDAO.apply();
  }

  public void testOverUpperLimitInteger() throws Exception
  {
    try
    {
      BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
      businessDAO.setValue("testInteger", "20");
      businessDAO.apply();

      fail("Able to set a value which is greater than the end range");
    }
    catch (ProblemException e)
    {
      // This is expected
      List<ProblemIF> problems = e.getProblems();
      boolean problemFound = false;
      for (ProblemIF p : problems)
      {
        if (p instanceof AttributeValueAboveRangeProblem)
          problemFound = true;
      }
      if (!problemFound)
        throw new Exception("AttributeValueAboveRangeProblem was not thrown");
    }
  }

  public void testOverLowerLimitInteger() throws Exception
  {
    try
    {
      BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
      businessDAO.setValue("testInteger", "-13");
      businessDAO.apply();

      fail("Able to set a value which is less than the start range");
    }
    catch (ProblemException e)
    {
      // This is expected
      List<ProblemIF> problems = e.getProblems();
      boolean problemFound = false;
      for (ProblemIF p : problems)
      {
        if (p instanceof AttributeValueBelowRangeProblem)
          problemFound = true;
      }
      if (!problemFound)
        throw new Exception("AttributeValueBelowRangeProblem was not thrown");
    }
  }

  public void testFailDateBeforeExclusive()
  {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_YEAR, 3);
    Date date = calendar.getTime();

    try
    {
      BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
      businessDAO.setValue("attributeDateBeforeExclusive", MdAttributeDateUtil.getTypeUnsafeValue(date));
      businessDAO.apply();

      fail("Failed validation for before exclusive");
    }
    catch (AttributeValueException e)
    {
      // This is expected
    }
  }

  public void testSameDateBeforeExclusive()
  {
    Calendar calendar = Calendar.getInstance();
    Date date = calendar.getTime();

    try
    {
      BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
      businessDAO.setValue("attributeDateBeforeExclusive", MdAttributeDateUtil.getTypeUnsafeValue(date));
      businessDAO.apply();

      fail("Failed validation for before exclusive");
    }
    catch (AttributeValueException e)
    {
      // This is expected
    }
  }

  public void testDateBeforeExclusive()
  {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_YEAR, -2);
    Date date = calendar.getTime();

    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.setValue("attributeDateBeforeExclusive", MdAttributeDateUtil.getTypeUnsafeValue(date));
    businessDAO.apply();
  }

  public void testFailDateBeforeInclusive()
  {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_YEAR, 3);
    Date date = calendar.getTime();

    try
    {
      BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
      businessDAO.setValue("attributeDateBeforeInclusive", MdAttributeDateUtil.getTypeUnsafeValue(date));
      businessDAO.apply();

      fail("Failed validation for before exclusive");
    }
    catch (AttributeValueException e)
    {
      // This is expected
    }
  }

  public void testSameDateBeforeInclusive()
  {
    Calendar calendar = Calendar.getInstance();
    Date date = calendar.getTime();

    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.setValue("attributeDateBeforeInclusive", MdAttributeDateUtil.getTypeUnsafeValue(date));
    businessDAO.apply();
  }

  public void testDateBeforeInclusive()
  {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_YEAR, -2);
    Date date = calendar.getTime();

    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.setValue("attributeDateBeforeInclusive", MdAttributeDateUtil.getTypeUnsafeValue(date));
    businessDAO.apply();
  }

  public void testFailDateAfterExclusive()
  {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_YEAR, -3);
    Date date = calendar.getTime();

    try
    {
      BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
      businessDAO.setValue("attributeDateAfterExclusive", MdAttributeDateUtil.getTypeUnsafeValue(date));
      businessDAO.apply();

      fail("Failed validation for before exclusive");
    }
    catch (AttributeValueException e)
    {
      // This is expected
    }
  }

  public void testSameDateAfterExclusive()
  {
    Calendar calendar = Calendar.getInstance();
    Date date = calendar.getTime();

    try
    {
      BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
      businessDAO.setValue("attributeDateAfterExclusive", MdAttributeDateUtil.getTypeUnsafeValue(date));
      businessDAO.apply();

      fail("Failed validation for before exclusive");
    }
    catch (AttributeValueException e)
    {
      // This is expected
    }
  }

  public void testDateAfterExclusive()
  {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_YEAR, 2);
    Date date = calendar.getTime();

    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.setValue("attributeDateAfterExclusive", MdAttributeDateUtil.getTypeUnsafeValue(date));
    businessDAO.apply();
  }

  public void testFailDateAfterInclusive()
  {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_YEAR, -3);
    Date date = calendar.getTime();

    try
    {
      BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
      businessDAO.setValue("attributeDateAfterInclusive", MdAttributeDateUtil.getTypeUnsafeValue(date));
      businessDAO.apply();

      fail("Failed validation for before exclusive");
    }
    catch (AttributeValueException e)
    {
      // This is expected
    }
  }

  public void testSameDateAfterInclusive()
  {
    Calendar calendar = Calendar.getInstance();
    Date date = calendar.getTime();

    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.setValue("attributeDateAfterInclusive", MdAttributeDateUtil.getTypeUnsafeValue(date));
    businessDAO.apply();
  }

  public void testDateAfterInclusive()
  {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_YEAR, 2);
    Date date = calendar.getTime();

    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.setValue("attributeDateAfterInclusive", MdAttributeDateUtil.getTypeUnsafeValue(date));
    businessDAO.apply();
  }

  public void testDateBetween()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.setValue("testDate", "2001-12-08");
    businessDAO.apply();
  }

  public void testDateOutsideBetween()
  {
    try
    {
      BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
      businessDAO.setValue("testDate", "2010-12-12");
      businessDAO.apply();

      fail("Failed validation for before exclusive");
    }
    catch (AttributeValueException e)
    {
      // This is expected
    }
  }
}
