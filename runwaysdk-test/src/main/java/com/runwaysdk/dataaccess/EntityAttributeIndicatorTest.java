/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
 */
package com.runwaysdk.dataaccess;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

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
import com.runwaysdk.constants.MdTableInfo;
import com.runwaysdk.constants.TypeInfo;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDecimalDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeFloatDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIndicatorDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLongDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdTableDAO;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.TableQuery;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.metadata.IndicatorAggregateFunction;
import com.runwaysdk.system.metadata.IndicatorOperator;

public class EntityAttributeIndicatorTest
{
  protected static final TypeInfo      tableQueryInfo         = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "RefQueryTable");

  private static MdBusinessDAOIF       testMdBusinessIF;

  private static MdTableDAOIF          mdTableIF;

  private static MdAttributeFloatDAOIF mdAttributeFloatIF1;

  private static MdAttributeFloatDAOIF mdAttributeFloatIF2;

  private static final String          TEST_INTEGER_1         = "testInteger1";

  private static final String          TEST_INTEGER_2         = "testInteger2";

  private static final String          TEST_INTEGER_INDICATOR = "testIntegerIndicator";

  private static final String          TEST_LONG_1            = "testLong1";

  private static final String          TEST_LONG_2            = "testLong2";

  private static final String          TEST_FLOAT_1           = "testFloat1";

  private static final String          TEST_FLOAT_INDICATOR   = "testFloatIndicator";

  private static final String          TEST_FLOAT_2           = "testFloat2";

  private static final String          TEST_DOUBLE_1          = "testDouble1";

  private static final String          TEST_DOUBLE_2          = "testDouble2";

  private static final String          TEST_DECIMAL_1         = "testDecimal1";

  private static final String          TEST_DECIMAL_2         = "testDecimal2";

  private static final String          TEST_BOOLEAN_1         = "testBoolean1";

  private static final String          TEST_BOOLEAN_2         = "testBoolean2";

  private static final String          TEST_BOOLEAN_INDICATOR = "testBooleanIndicator";

  private static final String          TEST_COUNT_INDICATOR   = "testCountIndicator";

  @Request
  @BeforeClass
  public static void classSetUp()
  {
    testMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

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
    mdAttributeFloatIF1 = mdAttributeFloat1;

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
    mdAttributeFloatIF2 = mdAttributeFloat2;

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

    // Float Indicator for MdBusiness
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

    MdTableDAO mdTable = MdTableDAO.newInstance();
    mdTable.setValue(MdTableInfo.NAME, tableQueryInfo.getTypeName());
    mdTable.setValue(MdTableInfo.PACKAGE, tableQueryInfo.getPackageName());
    mdTable.setStructValue(MdTableInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Class Table");
    mdTable.setValue(MdTableInfo.TABLE_NAME, testMdBusinessIF.getTableName());
    mdTable.setValue(MdTableInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTable.apply();
    mdTableIF = mdTable;

    MdAttributeFloatDAO mdAttributeFloat3 = TestFixtureFactory.addFloatAttribute(mdTableIF, TEST_FLOAT_1);
    mdAttributeFloat3.setValue(MdAttributeFloatInfo.COLUMN_NAME, mdAttributeFloat1.getColumnName());
    mdAttributeFloat3.apply();

    MdAttributeFloatDAO mdAttributeFloat4 = TestFixtureFactory.addFloatAttribute(mdTableIF, TEST_FLOAT_2);
    mdAttributeFloat4.setValue(MdAttributeFloatInfo.COLUMN_NAME, mdAttributeFloat2.getColumnName());
    mdAttributeFloat4.apply();

    // Float Indicator for MdBusiness
    IndicatorPrimitiveDAO indicatorPrimitiveFloat3 = IndicatorPrimitiveDAO.newInstance();
    indicatorPrimitiveFloat3.setValue(IndicatorPrimitiveInfo.MD_ATTRIBUTE_PRIMITIVE, mdAttributeFloat3.getId());
    indicatorPrimitiveFloat3.setValue(IndicatorPrimitiveInfo.INDICATOR_FUNCTION, IndicatorAggregateFunction.SUM.getId());
    indicatorPrimitiveFloat3.apply();

    IndicatorPrimitiveDAO indicatorPrimitiveFloat4 = IndicatorPrimitiveDAO.newInstance();
    indicatorPrimitiveFloat4.setValue(IndicatorPrimitiveInfo.MD_ATTRIBUTE_PRIMITIVE, mdAttributeFloat4.getId());
    indicatorPrimitiveFloat4.setValue(IndicatorPrimitiveInfo.INDICATOR_FUNCTION, IndicatorAggregateFunction.SUM.getId());
    indicatorPrimitiveFloat4.apply();

    IndicatorCompositeDAO indicatorFloatComposite2 = IndicatorCompositeDAO.newInstance();
    indicatorFloatComposite2.setValue(IndicatorCompositeInfo.LEFT_OPERAND, indicatorPrimitiveFloat3.getId());
    indicatorFloatComposite2.setValue(IndicatorCompositeInfo.OPERATOR, IndicatorOperator.DIV.getId());
    indicatorFloatComposite2.setValue(IndicatorCompositeInfo.RIGHT_OPERAND, indicatorPrimitiveFloat4.getId());
    indicatorFloatComposite2.apply();

    MdAttributeIndicatorDAO mdAttrFloatIndicator2 = MdAttributeIndicatorDAO.newInstance();
    mdAttrFloatIndicator2.setValue(MdAttributeIndicatorInfo.NAME, TEST_FLOAT_INDICATOR);
    mdAttrFloatIndicator2.setStructValue(MdAttributeIndicatorInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Float Indicator");
    mdAttrFloatIndicator2.setValue(MdAttributeIndicatorInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrFloatIndicator2.setValue(MdAttributeIndicatorInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrFloatIndicator2.setValue(MdAttributeIndicatorInfo.INDICATOR_ELEMENT, indicatorFloatComposite2.getId());
    mdAttrFloatIndicator2.setValue(MdAttributeIndicatorInfo.DEFINING_MD_CLASS, mdTableIF.getId());
    mdAttrFloatIndicator2.apply();

    MdAttributeIntegerDAO mdAttributeInteger3 = TestFixtureFactory.addIntegerAttribute(mdTableIF, TEST_INTEGER_1);
    mdAttributeInteger3.setValue(MdAttributeFloatInfo.COLUMN_NAME, mdAttributeInteger1.getColumnName());
    mdAttributeInteger3.apply();

    MdAttributeIntegerDAO mdAttributeInteger4 = TestFixtureFactory.addIntegerAttribute(mdTableIF, TEST_INTEGER_2);
    mdAttributeInteger4.setValue(MdAttributeFloatInfo.COLUMN_NAME, mdAttributeInteger2.getColumnName());
    mdAttributeInteger4.apply();

    IndicatorPrimitiveDAO indicatorPrimitiveCount2 = IndicatorPrimitiveDAO.newInstance();
    indicatorPrimitiveCount2.setValue(IndicatorPrimitiveInfo.MD_ATTRIBUTE_PRIMITIVE, mdAttributeInteger3.getId());
    indicatorPrimitiveCount2.setValue(IndicatorPrimitiveInfo.INDICATOR_FUNCTION, IndicatorAggregateFunction.COUNT.getId());
    indicatorPrimitiveCount2.apply();

    MdAttributeIndicatorDAO mdAttrCountIndicator2 = MdAttributeIndicatorDAO.newInstance();
    mdAttrCountIndicator2.setValue(MdAttributeIndicatorInfo.NAME, TEST_COUNT_INDICATOR);
    mdAttrCountIndicator2.setStructValue(MdAttributeIndicatorInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Count Indicator");
    mdAttrCountIndicator2.setValue(MdAttributeIndicatorInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrCountIndicator2.setValue(MdAttributeIndicatorInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrCountIndicator2.setValue(MdAttributeIndicatorInfo.INDICATOR_ELEMENT, indicatorPrimitiveCount2.getId());
    mdAttrCountIndicator2.setValue(MdAttributeIndicatorInfo.DEFINING_MD_CLASS, mdTableIF.getId());
    mdAttrCountIndicator2.apply();

    BusinessDAO bus1 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    bus1.setValue(TEST_INTEGER_1, "4");
    bus1.setValue(TEST_INTEGER_2, "2");
    bus1.apply();

  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    mdTableIF.getBusinessDAO().delete();
  }

  /**
   * This test is to make sure a bug is fixed where deleting an indicator
   * attribute resulted in the attempt to drop a column that does not exist.
   */
  @Request
  @Test
  public void testDeleteMdIndicatorDefinition()
  {

    IndicatorPrimitiveDAO indicatorPrimitiveFloat1 = IndicatorPrimitiveDAO.newInstance();
    indicatorPrimitiveFloat1.setValue(IndicatorPrimitiveInfo.MD_ATTRIBUTE_PRIMITIVE, mdAttributeFloatIF1.getId());
    indicatorPrimitiveFloat1.setValue(IndicatorPrimitiveInfo.INDICATOR_FUNCTION, IndicatorAggregateFunction.SUM.getId());
    indicatorPrimitiveFloat1.apply();
    String indicatorPrimitiveFloat1Id = indicatorPrimitiveFloat1.getId();

    IndicatorPrimitiveDAO indicatorPrimitiveFloat2 = IndicatorPrimitiveDAO.newInstance();
    indicatorPrimitiveFloat2.setValue(IndicatorPrimitiveInfo.MD_ATTRIBUTE_PRIMITIVE, mdAttributeFloatIF2.getId());
    indicatorPrimitiveFloat2.setValue(IndicatorPrimitiveInfo.INDICATOR_FUNCTION, IndicatorAggregateFunction.SUM.getId());
    indicatorPrimitiveFloat2.apply();
    String indicatorPrimitiveFloat2Id = indicatorPrimitiveFloat2.getId();

    IndicatorCompositeDAO indicatorFloatComposite = IndicatorCompositeDAO.newInstance();
    indicatorFloatComposite.setValue(IndicatorCompositeInfo.LEFT_OPERAND, indicatorPrimitiveFloat1.getId());
    indicatorFloatComposite.setValue(IndicatorCompositeInfo.OPERATOR, IndicatorOperator.DIV.getId());
    indicatorFloatComposite.setValue(IndicatorCompositeInfo.RIGHT_OPERAND, indicatorPrimitiveFloat2.getId());
    indicatorFloatComposite.apply();
    String indicatorFloatCompositeId = indicatorFloatComposite.getId();

    String indicatorName = TEST_FLOAT_INDICATOR + "_Temp";

    MdAttributeIndicatorDAO mdAttrFloatIndicatorTemp = MdAttributeIndicatorDAO.newInstance();
    mdAttrFloatIndicatorTemp.setValue(MdAttributeIndicatorInfo.NAME, indicatorName);
    mdAttrFloatIndicatorTemp.setStructValue(MdAttributeIndicatorInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Float Indicator");
    mdAttrFloatIndicatorTemp.setValue(MdAttributeIndicatorInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrFloatIndicatorTemp.setValue(MdAttributeIndicatorInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrFloatIndicatorTemp.setValue(MdAttributeIndicatorInfo.INDICATOR_ELEMENT, indicatorFloatComposite.getId());
    mdAttrFloatIndicatorTemp.setValue(MdAttributeIndicatorInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttrFloatIndicatorTemp.apply();

    // now delete it
    mdAttrFloatIndicatorTemp.delete();

    try
    {
      IndicatorPrimitiveDAO.get(indicatorPrimitiveFloat1Id);
      Assert.fail("Indicator was not deleted");
    }
    catch (DataNotFoundException ex)
    {
      // We want to land here.
    }

    try
    {
      IndicatorPrimitiveDAO.get(indicatorPrimitiveFloat2Id);
      Assert.fail("Indicator was not deleted");
    }
    catch (DataNotFoundException ex)
    {
      // We want to land here.
    }

    try
    {
      IndicatorCompositeDAO.get(indicatorFloatCompositeId);
      Assert.fail("Indicator was not deleted");
    }
    catch (DataNotFoundException ex)
    {
      // We want to land here.
    }
  }

  /**
   * Behavior of a divide by zero should be that the value should be blank or
   * null.
   */
  @Request
  @Test
  public void testObjectDivideByZero()
  {
    BusinessDAO bus1 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    bus1.setValue(TEST_INTEGER_1, "4");
    bus1.setValue(TEST_INTEGER_2, "0");
    bus1.apply();

    try
    {
      String value = bus1.getValue(TEST_INTEGER_INDICATOR);

      Assert.assertTrue("A divide by zero should return an empty string.", value.equals(""));
    }
    finally
    {
      bus1.delete();
    }
  }

  /**
   * Behavior of a divide by zero should be that the value should be blank or
   * null.
   */
  @Request
  @Test
  public void testObjectDivision()
  {
    BusinessDAO bus1 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    bus1.setValue(TEST_INTEGER_1, "4");
    bus1.setValue(TEST_INTEGER_2, "2");
    bus1.apply();

    try
    {
      String value = bus1.getValue(TEST_INTEGER_INDICATOR);

      Assert.assertTrue("Division function should have returned the value 2", value.equals("2"));

      // Test fetching the object anew from the DB
      BusinessDAOIF bus1IF = BusinessDAO.get(bus1.getId());

      value = bus1IF.getValue(TEST_INTEGER_INDICATOR);

      Assert.assertTrue("Division function should have returned the value 2", value.equals("2"));
    }
    finally
    {
      bus1.delete();
    }
  }

  /**
   * Write test to check for different indicator types. such as float divided by
   * int should return double, etc.
   */
  @Request
  @Test
  public void testIntegerReturnType()
  {
    // Test Integer Java Type
    MdAttributeIndicatorDAOIF mdAttrIntIndicator = (MdAttributeIndicatorDAOIF) testMdBusinessIF.definesAttribute(TEST_INTEGER_INDICATOR);
    Assert.assertEquals("Return type of an integer indicator should be " + Double.class.getName(), Double.class.getSimpleName(), mdAttrIntIndicator.javaType(false));

    // Test Float Java Type
    MdAttributeIndicatorDAOIF mdAttrFloatIndicator = (MdAttributeIndicatorDAOIF) testMdBusinessIF.definesAttribute(TEST_FLOAT_INDICATOR);
    Assert.assertEquals("Return type of an float indicator should be " + Double.class.getName(), Double.class.getSimpleName(), mdAttrFloatIndicator.javaType(false));

    // Test Boolean Java Type
    MdAttributeIndicatorDAOIF mdAttrBooleanIndicator = (MdAttributeIndicatorDAOIF) testMdBusinessIF.definesAttribute(TEST_BOOLEAN_INDICATOR);
    Assert.assertEquals("Return type of an boolean indicator should be " + Boolean.class.getName(), Double.class.getSimpleName(), mdAttrBooleanIndicator.javaType(false));

  }

  @Request
  @Test
  public void testCountIndicator()
  {
    BusinessDAO bus1 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    bus1.setValue(TEST_INTEGER_1, "4");
    bus1.setValue(TEST_INTEGER_2, "2");
    bus1.apply();

    try
    {
      QueryFactory qf = new QueryFactory();

      ValueQuery vq = qf.valueQuery();
      BusinessDAOQuery bq = qf.businessDAOQuery(testMdBusinessIF.definesType());

      vq.SELECT(bq.get(TEST_INTEGER_1), bq.get(TEST_INTEGER_2), bq.getS(TEST_COUNT_INDICATOR));

      OIterator<ValueObject> i = vq.getIterator();

      try
      {
        Assert.assertEquals("Query did not return just one result - impropper aggregation of indicator.", 1, vq.getCount());

        for (ValueObject valueObject : i)
        {
          String stringCount = valueObject.getValue(TEST_COUNT_INDICATOR);

          Integer count = Integer.parseInt(stringCount);

          Assert.assertEquals("The query count should have returned a count of two.", (Integer) 2, (Integer) count);
        }
      }
      finally
      {
        i.close();
      }
    }
    finally
    {
      bus1.delete();
    }
  }

  @Request
  @Test
  public void testCountIndicatorTable()
  {
    BusinessDAO bus1 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    bus1.setValue(TEST_INTEGER_1, "4");
    bus1.setValue(TEST_INTEGER_2, "2");
    bus1.apply();

    try
    {
      QueryFactory qf = new QueryFactory();

      ValueQuery vq = qf.valueQuery();
      TableQuery tq = qf.tableQuery(mdTableIF.definesType());

      vq.SELECT(tq.get(TEST_INTEGER_1), tq.get(TEST_INTEGER_2), tq.getS(TEST_COUNT_INDICATOR));

      OIterator<ValueObject> i = vq.getIterator();

      try
      {
        Assert.assertEquals("Query did not return just one result - impropper aggregation of indicator.", 1, vq.getCount());

        for (ValueObject valueObject : i)
        {
          String stringCount = valueObject.getValue(TEST_COUNT_INDICATOR);

          Integer count = Integer.parseInt(stringCount);

          Assert.assertEquals("The query count should have returned a count of two.", (Integer) 2, (Integer) count);
        }
      }
      finally
      {
        i.close();
      }
    }
    finally
    {
      bus1.delete();
    }
  }

  @Request
  @Test
  public void testFloatIndicatorTable()
  {
    BusinessDAO bus1 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    bus1.setValue(TEST_FLOAT_1, "44.4");
    bus1.setValue(TEST_FLOAT_2, "22.2");
    bus1.apply();

    BusinessDAO bus2 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    bus2.setValue(TEST_FLOAT_1, "44.4");
    bus2.setValue(TEST_FLOAT_2, "22.2");
    bus2.apply();

    try
    {
      QueryFactory qf = new QueryFactory();

      ValueQuery vq = qf.valueQuery();
      TableQuery tq = qf.tableQuery(mdTableIF.definesType());

      vq.SELECT(tq.getS(TEST_FLOAT_INDICATOR));

      OIterator<ValueObject> i = vq.getIterator();

      try
      {
        Assert.assertEquals("Query did not return just one result - improper aggregation of indicator.", 1, vq.getCount());

        for (ValueObject valueObject : i)
        {

          String floatIndicator = valueObject.getValue(TEST_FLOAT_INDICATOR);

          Double resultDouble = Double.parseDouble(floatIndicator);

          Assert.assertEquals("The query count should have returned a count of two.", (Double) 2.0, (Double) resultDouble);
        }
      }
      finally
      {
        i.close();
      }
    }
    finally
    {
      bus1.delete();
      bus2.delete();
    }
  }

  @Request
  @Test
  public void testFloatIndicator()
  {
    BusinessDAO bus1 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    bus1.setValue(TEST_FLOAT_1, "44.4");
    bus1.setValue(TEST_FLOAT_2, "22.2");
    bus1.apply();

    BusinessDAO bus2 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    bus2.setValue(TEST_FLOAT_1, "44.4");
    bus2.setValue(TEST_FLOAT_2, "22.2");
    bus2.apply();

    try
    {
      QueryFactory qf = new QueryFactory();

      ValueQuery vq = qf.valueQuery();
      BusinessDAOQuery bq = qf.businessDAOQuery(testMdBusinessIF.definesType());

      vq.SELECT(bq.getS(TEST_FLOAT_INDICATOR));

      OIterator<ValueObject> i = vq.getIterator();

      try
      {
        Assert.assertEquals("Query did not return just one result - improper aggregation of indicator.", 1, vq.getCount());

        for (ValueObject valueObject : i)
        {

          String floatIndicator = valueObject.getValue(TEST_FLOAT_INDICATOR);

          Double resultDouble = Double.parseDouble(floatIndicator);

          Assert.assertEquals("The query count should have returned a count of two.", (Double) 2.0, (Double) resultDouble);
        }
      }
      finally
      {
        i.close();
      }
    }
    finally
    {
      bus1.delete();
      bus2.delete();
    }
  }

  @Request
  @Test
  public void testFloatDivideByZeroIndicator()
  {
    BusinessDAO bus1 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    bus1.setValue(TEST_FLOAT_1, "44.4");
    bus1.setValue(TEST_FLOAT_2, "0");
    bus1.apply();

    BusinessDAO bus2 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    bus2.setValue(TEST_FLOAT_1, "44.4");
    bus2.setValue(TEST_FLOAT_2, "0");
    bus2.apply();

    try
    {
      QueryFactory qf = new QueryFactory();

      ValueQuery vq = qf.valueQuery();
      BusinessDAOQuery bq = qf.businessDAOQuery(testMdBusinessIF.definesType());

      vq.SELECT(bq.getS(TEST_FLOAT_INDICATOR));

      OIterator<ValueObject> i = vq.getIterator();

      try
      {
        Assert.assertEquals("Query did not return just one result - improper aggregation of indicator.", 1, vq.getCount());

        for (ValueObject valueObject : i)
        {
          String floatIndicator = valueObject.getValue(TEST_FLOAT_INDICATOR);

          Assert.assertEquals("The query count should have returned a count of two.", "", floatIndicator.trim());

        }
      }
      finally
      {
        i.close();
      }
    }
    finally
    {
      bus1.delete();
      bus2.delete();
    }
  }

  @Request
  @Test
  public void testDoubleDTOIndicator()
  {
    BusinessDAO bus1 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    bus1.setValue(TEST_FLOAT_1, "99.9");
    bus1.setValue(TEST_FLOAT_2, "33.3");
    bus1.apply();

    BusinessDAO bus2 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    bus2.setValue(TEST_FLOAT_1, "99.9");
    bus2.setValue(TEST_FLOAT_2, "33.3");
    bus2.apply();

    try
    {
      QueryFactory qf = new QueryFactory();

      ValueQuery vq = qf.valueQuery();
      BusinessDAOQuery bq = qf.businessDAOQuery(testMdBusinessIF.definesType());

      vq.SELECT(bq.getS(TEST_FLOAT_INDICATOR));

      OIterator<ValueObject> i = vq.getIterator();

      try
      {
        Assert.assertEquals("Query did not return just one result - improper aggregation of indicator.", 1, vq.getCount());

        for (ValueObject valueObject : i)
        {

          String floatIndicator = valueObject.getValue(TEST_FLOAT_INDICATOR);

          Double resultDouble = Double.parseDouble(floatIndicator);

          Assert.assertEquals("The query count should have returned a value of three.", (Double) 3.0, (Double) resultDouble);
        }
      }
      finally
      {
        i.close();
      }
    }
    finally
    {
      bus1.delete();
      bus2.delete();
    }
  }
}
