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
package com.runwaysdk.business;

import com.runwaysdk.constants.DatabaseProperties;
import com.runwaysdk.constants.IndicatorCompositeInfo;
import com.runwaysdk.constants.IndicatorPrimitiveInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeDecimalInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeFloatInfo;
import com.runwaysdk.constants.MdAttributeIndicatorInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeLongInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.TestConstants;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityMasterTestSetup;
import com.runwaysdk.dataaccess.IndicatorCompositeDAO;
import com.runwaysdk.dataaccess.IndicatorPrimitiveDAO;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;
import com.runwaysdk.dataaccess.io.XMLImporter;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDecimalDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeFloatDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIndicatorDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLongDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.system.metadata.IndicatorAggregateFunction;
import com.runwaysdk.system.metadata.IndicatorOperator;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

public class EntityAttributeIndicatorGenTest extends TestCase
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
  
  private static MdBusinessDAOIF                      testMdBusinessIF;
  
  private static final String                         TEST_INTEGER_1              = "testInteger1";
  
  private static final String                         TEST_INTEGER_2              = "testInteger2";
  
  private static final String                         TEST_INTEGER_INDICATOR      = "testIntegerIndicator";
  
  private static final String                         TEST_LONG_1                 = "testLong1";

  private static final String                         TEST_LONG_2                 = "testLong2";
  
  private static final String                         TEST_FLOAT_1                = "testFloat1";

  private static final String                         TEST_FLOAT_INDICATOR        = "testFloatIndicator";
  
  private static final String                         TEST_FLOAT_2                = "testFloat2";
  
  private static final String                         TEST_DOUBLE_1               = "testDouble1";

  private static final String                         TEST_DOUBLE_2               = "testDouble2";
  
  private static final String                         TEST_DECIMAL_1              = "testDecimal1";

  private static final String                         TEST_DECIMAL_2              = "testDecimal2";
  
  private static final String                         TEST_BOOLEAN_1              = "testBoolean1";

  private static final String                         TEST_BOOLEAN_2              = "testBoolean2";
  
  private static final String                         TEST_BOOLEAN_INDICATOR      = "testBooleanIndicator";
  
  private static final String                         TEST_COUNT_INDICATOR        = "testCountIndicator";
  
  /**
   * The launch point for the Junit tests.
   * 
   * @param args
   */
  public static void main(String[] args)
  {

    if (DatabaseProperties.getDatabaseClass().equals("hsqldb"))
      XMLImporter.main(new String[] { TestConstants.Path.schema_xsd, TestConstants.Path.metadata_xml });

    junit.textui.TestRunner.run(new EntityMasterTestSetup(EntityAttributeIndicatorGenTest.suite()));

  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(EntityAttributeIndicatorGenTest.class);

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
    MdBusinessDAO testMdBusiness = TestFixtureFactory.createMdBusiness1();
    testMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.TRUE);
    testMdBusiness.apply();
    
    testMdBusinessIF = testMdBusiness;
    
    MdAttributeIntegerDAO mdAttributeInteger1 = MdAttributeIntegerDAO.newInstance();
    mdAttributeInteger1.setValue(MdAttributeIntegerInfo.NAME, TEST_INTEGER_1);
    mdAttributeInteger1.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Integer 1");
    mdAttributeInteger1.setValue(MdAttributeIntegerInfo.DEFAULT_VALUE, "");
    mdAttributeInteger1.setValue(MdAttributeIntegerInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeInteger1.setValue(MdAttributeIntegerInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeInteger1.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeInteger1.apply();
    
    MdAttributeIntegerDAO mdAttributeInteger2 = MdAttributeIntegerDAO.newInstance();
    mdAttributeInteger2.setValue(MdAttributeIntegerInfo.NAME, TEST_INTEGER_2);
    mdAttributeInteger2.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Integer 2");
    mdAttributeInteger2.setValue(MdAttributeIntegerInfo.DEFAULT_VALUE, "");
    mdAttributeInteger2.setValue(MdAttributeIntegerInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeInteger2.setValue(MdAttributeIntegerInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeInteger2.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeInteger2.apply();
    
    MdAttributeLongDAO mdAttributeLong1 = MdAttributeLongDAO.newInstance();
    mdAttributeLong1.setValue(MdAttributeLongInfo.NAME, TEST_LONG_1);
    mdAttributeLong1.setStructValue(MdAttributeLongInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Long 1");
    mdAttributeLong1.setValue(MdAttributeLongInfo.DEFAULT_VALUE, "");
    mdAttributeLong1.setValue(MdAttributeLongInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeLong1.setValue(MdAttributeLongInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeLong1.setValue(MdAttributeLongInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeLong1.apply();

    MdAttributeLongDAO mdAttributeLong2 = MdAttributeLongDAO.newInstance();
    mdAttributeLong2.setValue(MdAttributeLongInfo.NAME, TEST_LONG_2);
    mdAttributeLong2.setStructValue(MdAttributeLongInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Long 2");
    mdAttributeLong2.setValue(MdAttributeLongInfo.DEFAULT_VALUE, "");
    mdAttributeLong2.setValue(MdAttributeLongInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeLong2.setValue(MdAttributeLongInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeLong2.setValue(MdAttributeLongInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeLong2.apply();
    
    MdAttributeFloatDAO mdAttributeFloat1 = MdAttributeFloatDAO.newInstance();
    mdAttributeFloat1.setValue(MdAttributeFloatInfo.NAME, TEST_FLOAT_1);
    mdAttributeFloat1.setStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Float 1");
    mdAttributeFloat1.setValue(MdAttributeFloatInfo.DEFAULT_VALUE, "");
    mdAttributeFloat1.setValue(MdAttributeFloatInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeFloat1.setValue(MdAttributeFloatInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeFloat1.setValue(MdAttributeFloatInfo.LENGTH, "10");
    mdAttributeFloat1.setValue(MdAttributeFloatInfo.DECIMAL, "2");
    mdAttributeFloat1.setValue(MdAttributeFloatInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeFloat1.apply();
    
    MdAttributeFloatDAO mdAttributeFloat2 = MdAttributeFloatDAO.newInstance();
    mdAttributeFloat2.setValue(MdAttributeFloatInfo.NAME, TEST_FLOAT_2);
    mdAttributeFloat2.setStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Float 2");
    mdAttributeFloat2.setValue(MdAttributeFloatInfo.DEFAULT_VALUE, "");
    mdAttributeFloat2.setValue(MdAttributeFloatInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeFloat2.setValue(MdAttributeFloatInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeFloat2.setValue(MdAttributeFloatInfo.LENGTH, "10");
    mdAttributeFloat2.setValue(MdAttributeFloatInfo.DECIMAL, "2");
    mdAttributeFloat2.setValue(MdAttributeFloatInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeFloat2.apply();
    
    MdAttributeDoubleDAO mdAttributeDouble1 = MdAttributeDoubleDAO.newInstance();
    mdAttributeDouble1.setValue(MdAttributeDoubleInfo.NAME, TEST_DOUBLE_1);
    mdAttributeDouble1.setStructValue(MdAttributeDoubleInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Double 1");
    mdAttributeDouble1.setValue(MdAttributeDoubleInfo.DEFAULT_VALUE, "");
    mdAttributeDouble1.setValue(MdAttributeDoubleInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeDouble1.setValue(MdAttributeDoubleInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeDouble1.setValue(MdAttributeDoubleInfo.LENGTH, "16");
    mdAttributeDouble1.setValue(MdAttributeDoubleInfo.DECIMAL, "4");
    mdAttributeDouble1.setValue(MdAttributeDoubleInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeDouble1.apply();
    
    MdAttributeDoubleDAO mdAttributeDouble2 = MdAttributeDoubleDAO.newInstance();
    mdAttributeDouble2.setValue(MdAttributeDoubleInfo.NAME, TEST_DOUBLE_2);
    mdAttributeDouble2.setStructValue(MdAttributeDoubleInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Double 2");
    mdAttributeDouble2.setValue(MdAttributeDoubleInfo.DEFAULT_VALUE, "");
    mdAttributeDouble2.setValue(MdAttributeDoubleInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeDouble2.setValue(MdAttributeDoubleInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeDouble2.setValue(MdAttributeDoubleInfo.LENGTH, "16");
    mdAttributeDouble2.setValue(MdAttributeDoubleInfo.DECIMAL, "4");
    mdAttributeDouble2.setValue(MdAttributeDoubleInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeDouble2.apply();
    
    MdAttributeDecimalDAO mdAttributeDecimal1 = MdAttributeDecimalDAO.newInstance();
    mdAttributeDecimal1.setValue(MdAttributeDecimalInfo.NAME, TEST_DECIMAL_1);
    mdAttributeDecimal1.setStructValue(MdAttributeDecimalInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Decimal 1");
    mdAttributeDecimal1.setValue(MdAttributeDecimalInfo.DEFAULT_VALUE, "");
    mdAttributeDecimal1.setValue(MdAttributeDecimalInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeDecimal1.setValue(MdAttributeDecimalInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeDecimal1.setValue(MdAttributeDecimalInfo.LENGTH, "13");
    mdAttributeDecimal1.setValue(MdAttributeDecimalInfo.DECIMAL, "3");
    mdAttributeDecimal1.setValue(MdAttributeDecimalInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeDecimal1.apply();
    
    MdAttributeDecimalDAO mdAttributeDecimal2 = MdAttributeDecimalDAO.newInstance();
    mdAttributeDecimal2.setValue(MdAttributeDecimalInfo.NAME, TEST_DECIMAL_2);
    mdAttributeDecimal2.setStructValue(MdAttributeDecimalInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Decimal 2");
    mdAttributeDecimal2.setValue(MdAttributeDecimalInfo.DEFAULT_VALUE, "");
    mdAttributeDecimal2.setValue(MdAttributeDecimalInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeDecimal2.setValue(MdAttributeDecimalInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeDecimal2.setValue(MdAttributeDecimalInfo.LENGTH, "13");
    mdAttributeDecimal2.setValue(MdAttributeDecimalInfo.DECIMAL, "3");
    mdAttributeDecimal2.setValue(MdAttributeDecimalInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeDecimal2.apply();
    
    MdAttributeBooleanDAO mdAttributeBoolean1 = MdAttributeBooleanDAO.newInstance();
    mdAttributeBoolean1.setValue(MdAttributeBooleanInfo.NAME, TEST_BOOLEAN_1);
    mdAttributeBoolean1.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Boolean 1");
    mdAttributeBoolean1.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "True");
    mdAttributeBoolean1.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "False");
    mdAttributeBoolean1.setValue(MdAttributeBooleanInfo.DEFAULT_VALUE, "");
    mdAttributeBoolean1.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeBoolean1.setValue(MdAttributeBooleanInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeBoolean1.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeBoolean1.apply();
    
    MdAttributeBooleanDAO mdAttributeBoolean2 = MdAttributeBooleanDAO.newInstance();
    mdAttributeBoolean2.setValue(MdAttributeBooleanInfo.NAME, TEST_BOOLEAN_2);
    mdAttributeBoolean2.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Boolean 2");
    mdAttributeBoolean2.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "True");
    mdAttributeBoolean2.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "False");
    mdAttributeBoolean2.setValue(MdAttributeBooleanInfo.DEFAULT_VALUE, "");
    mdAttributeBoolean2.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeBoolean2.setValue(MdAttributeBooleanInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeBoolean2.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeBoolean2.apply();
    
    // Integer Indicator
    IndicatorPrimitiveDAO indicatorPrimitiveInteger1 = IndicatorPrimitiveDAO.newInstance();
    indicatorPrimitiveInteger1.setValue(IndicatorPrimitiveInfo.MD_ATTRIBUTE_PRIMITIVE, mdAttributeInteger1.getId());
    indicatorPrimitiveInteger1.setValue(IndicatorPrimitiveInfo.INDICATOR_FUNCTION, IndicatorAggregateFunction.SUM.getId());
    indicatorPrimitiveInteger1.apply();
    
    IndicatorPrimitiveDAO IndicatorPrimitiveInteger2 = IndicatorPrimitiveDAO.newInstance();
    IndicatorPrimitiveInteger2.setValue(IndicatorPrimitiveInfo.MD_ATTRIBUTE_PRIMITIVE, mdAttributeInteger2.getId());
    IndicatorPrimitiveInteger2.setValue(IndicatorPrimitiveInfo.INDICATOR_FUNCTION, IndicatorAggregateFunction.SUM.getId());
    IndicatorPrimitiveInteger2.apply();
    
    IndicatorCompositeDAO indicatorIntegerComposite = IndicatorCompositeDAO.newInstance();
    indicatorIntegerComposite.setValue(IndicatorCompositeInfo.LEFT_OPERAND, indicatorPrimitiveInteger1.getId());
    indicatorIntegerComposite.setValue(IndicatorCompositeInfo.OPERATOR, IndicatorOperator.DIV.getId());
    indicatorIntegerComposite.setValue(IndicatorCompositeInfo.RIGHT_OPERAND, IndicatorPrimitiveInteger2.getId());
    indicatorIntegerComposite.apply();
    
    MdAttributeIndicatorDAO mdAttrIntegerIndicator1 = MdAttributeIndicatorDAO.newInstance();
    mdAttrIntegerIndicator1.setValue(MdAttributeIndicatorInfo.NAME, TEST_INTEGER_INDICATOR);
    mdAttrIntegerIndicator1.setStructValue(MdAttributeIndicatorInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Integer Indicator");
    mdAttrIntegerIndicator1.setValue(MdAttributeIndicatorInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrIntegerIndicator1.setValue(MdAttributeIndicatorInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrIntegerIndicator1.setValue(MdAttributeIndicatorInfo.INDICATOR_ELEMENT, indicatorIntegerComposite.getId());
    mdAttrIntegerIndicator1.setValue(MdAttributeIndicatorInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttrIntegerIndicator1.apply();
    
    // Float Indicator
    IndicatorPrimitiveDAO indicatorPrimitiveFloat1 = IndicatorPrimitiveDAO.newInstance();
    indicatorPrimitiveFloat1.setValue(IndicatorPrimitiveInfo.MD_ATTRIBUTE_PRIMITIVE, mdAttributeFloat1.getId());
    indicatorPrimitiveFloat1.setValue(IndicatorPrimitiveInfo.INDICATOR_FUNCTION, IndicatorAggregateFunction.SUM.getId());
    indicatorPrimitiveFloat1.apply();
    
    IndicatorPrimitiveDAO indicatorPrimitiveFloat2 = IndicatorPrimitiveDAO.newInstance();
    indicatorPrimitiveFloat2.setValue(IndicatorPrimitiveInfo.MD_ATTRIBUTE_PRIMITIVE, mdAttributeFloat2.getId());
    indicatorPrimitiveFloat2.setValue(IndicatorPrimitiveInfo.INDICATOR_FUNCTION, IndicatorAggregateFunction.SUM.getId());
    indicatorPrimitiveFloat2.apply();
    
    IndicatorCompositeDAO indicatorFloatComposite = IndicatorCompositeDAO.newInstance();
    indicatorFloatComposite.setValue(IndicatorCompositeInfo.LEFT_OPERAND, indicatorPrimitiveFloat1.getId());
    indicatorFloatComposite.setValue(IndicatorCompositeInfo.OPERATOR, IndicatorOperator.DIV.getId());
    indicatorFloatComposite.setValue(IndicatorCompositeInfo.RIGHT_OPERAND, indicatorPrimitiveFloat2.getId());
    indicatorFloatComposite.apply();
    
    MdAttributeIndicatorDAO mdAttrFloatIndicator1 = MdAttributeIndicatorDAO.newInstance();
    mdAttrFloatIndicator1.setValue(MdAttributeIndicatorInfo.NAME, TEST_FLOAT_INDICATOR);
    mdAttrFloatIndicator1.setStructValue(MdAttributeIndicatorInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Float Indicator");
    mdAttrFloatIndicator1.setValue(MdAttributeIndicatorInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrFloatIndicator1.setValue(MdAttributeIndicatorInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrFloatIndicator1.setValue(MdAttributeIndicatorInfo.INDICATOR_ELEMENT, indicatorFloatComposite.getId());
    mdAttrFloatIndicator1.setValue(MdAttributeIndicatorInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttrFloatIndicator1.apply();
    
    // Boolean Indicator
    IndicatorPrimitiveDAO indicatorPrimitiveBoolean1 = IndicatorPrimitiveDAO.newInstance();
    indicatorPrimitiveBoolean1.setValue(IndicatorPrimitiveInfo.MD_ATTRIBUTE_PRIMITIVE, mdAttributeBoolean1.getId());
    indicatorPrimitiveBoolean1.setValue(IndicatorPrimitiveInfo.INDICATOR_FUNCTION, IndicatorAggregateFunction.SUM.getId());
    indicatorPrimitiveBoolean1.apply();

    IndicatorPrimitiveDAO indicatorPrimitiveBoolean2 = IndicatorPrimitiveDAO.newInstance();
    indicatorPrimitiveBoolean2.setValue(IndicatorPrimitiveInfo.MD_ATTRIBUTE_PRIMITIVE, mdAttributeBoolean2.getId());
    indicatorPrimitiveBoolean2.setValue(IndicatorPrimitiveInfo.INDICATOR_FUNCTION, IndicatorAggregateFunction.SUM.getId());
    indicatorPrimitiveBoolean2.apply();
    
    IndicatorCompositeDAO indicatorBooleanComposite = IndicatorCompositeDAO.newInstance();
    indicatorBooleanComposite.setValue(IndicatorCompositeInfo.LEFT_OPERAND, indicatorPrimitiveBoolean1.getId());
    indicatorBooleanComposite.setValue(IndicatorCompositeInfo.OPERATOR, IndicatorOperator.DIV.getId());
    indicatorBooleanComposite.setValue(IndicatorCompositeInfo.RIGHT_OPERAND, indicatorPrimitiveBoolean2.getId());
    indicatorBooleanComposite.apply();
    
    MdAttributeIndicatorDAO mdAttrBooleanIndicator1 = MdAttributeIndicatorDAO.newInstance();
    mdAttrBooleanIndicator1.setValue(MdAttributeIndicatorInfo.NAME, TEST_BOOLEAN_INDICATOR);
    mdAttrBooleanIndicator1.setStructValue(MdAttributeIndicatorInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Boolean Indicator");
    mdAttrBooleanIndicator1.setValue(MdAttributeIndicatorInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrBooleanIndicator1.setValue(MdAttributeIndicatorInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrBooleanIndicator1.setValue(MdAttributeIndicatorInfo.INDICATOR_ELEMENT, indicatorBooleanComposite.getId());
    mdAttrBooleanIndicator1.setValue(MdAttributeIndicatorInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttrBooleanIndicator1.apply();
    
    IndicatorPrimitiveDAO indicatorPrimitiveCount1 = IndicatorPrimitiveDAO.newInstance();
    indicatorPrimitiveCount1.setValue(IndicatorPrimitiveInfo.MD_ATTRIBUTE_PRIMITIVE, mdAttributeInteger1.getId());
    indicatorPrimitiveCount1.setValue(IndicatorPrimitiveInfo.INDICATOR_FUNCTION, IndicatorAggregateFunction.COUNT.getId());
    indicatorPrimitiveCount1.apply();
    
    MdAttributeIndicatorDAO mdAttrCountIndicator1 = MdAttributeIndicatorDAO.newInstance();
    mdAttrCountIndicator1.setValue(MdAttributeIndicatorInfo.NAME, TEST_COUNT_INDICATOR);
    mdAttrCountIndicator1.setStructValue(MdAttributeIndicatorInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Count Indicator");
    mdAttrCountIndicator1.setValue(MdAttributeIndicatorInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrCountIndicator1.setValue(MdAttributeIndicatorInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrCountIndicator1.setValue(MdAttributeIndicatorInfo.INDICATOR_ELEMENT, indicatorPrimitiveCount1.getId());
    mdAttrCountIndicator1.setValue(MdAttributeIndicatorInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttrCountIndicator1.apply();
    
    BusinessDAO bus1 = BusinessDAO.newInstance(testMdBusinessIF.definesType());
    bus1.setValue(TEST_INTEGER_1, "4");
    bus1.setValue(TEST_INTEGER_2, "2");
    bus1.apply();
  }
  
  public static void classTearDown()
  {
    MdBusinessDAO testMdBusiness = MdBusinessDAO.getMdBusinessDAO(TestFixConst.TEST_CLASS1_TYPE).getBusinessDAO();
    testMdBusiness.delete();
  }
}
