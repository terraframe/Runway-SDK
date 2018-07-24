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
/**
*
*/
package com.runwaysdk.business;

import java.util.Date;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.runwaysdk.ClasspathTestRunner;
import com.runwaysdk.ProblemException;
import com.runwaysdk.ProblemIF;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.MdAttributeDateTimeUtil;
import com.runwaysdk.constants.MdAttributeDateUtil;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributePrimitiveInfo;
import com.runwaysdk.constants.MdAttributeTimeUtil;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.attributes.AttributeLengthCharacterException;
import com.runwaysdk.dataaccess.attributes.AttributeValueException;
import com.runwaysdk.dataaccess.attributes.EmptyValueProblem;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeClobDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDecimalDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeFloatDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLongDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTextDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFloatDAO;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.metadata.MdClass;
import com.runwaysdk.system.metadata.MdWebFloat;
import com.runwaysdk.system.metadata.MdWebForm;
import com.runwaysdk.system.metadata.MdWebPrimitive;

import ognl.OgnlClassResolver;

@RunWith(ClasspathTestRunner.class)
public class ExpressionAttributeTest
{

  public static final String   ATTR_BOOL1         = TestFixConst.ATTRIBUTE_BOOLEAN + "1";

  public static final String   ATTR_BOOL2         = TestFixConst.ATTRIBUTE_BOOLEAN + "2";

  public static final String   ATTR_BOOL_EXPR     = TestFixConst.ATTRIBUTE_BOOLEAN + "Expression";

  public static final String   ATTR_INT1          = TestFixConst.ATTRIBUTE_INTEGER + "1";

  public static final String   ATTR_INT2          = TestFixConst.ATTRIBUTE_INTEGER + "2";

  public static final String   ATTR_INT_EXPR      = TestFixConst.ATTRIBUTE_INTEGER + "Expression";

  public static final String   ATTR_LONG1         = TestFixConst.ATTRIBUTE_LONG + "1";

  public static final String   ATTR_LONG2         = TestFixConst.ATTRIBUTE_LONG + "2";

  public static final String   ATTR_LONG_EXPR     = TestFixConst.ATTRIBUTE_LONG + "Expression";

  public static final String   ATTR_FLOAT1        = TestFixConst.ATTRIBUTE_FLOAT + "1";

  public static final String   ATTR_FLOAT2        = TestFixConst.ATTRIBUTE_FLOAT + "2";

  public static final String   ATTR_FLOAT_EXPR    = TestFixConst.ATTRIBUTE_FLOAT + "Expression";

  public static final String   ATTR_DOUBLE1       = TestFixConst.ATTRIBUTE_DOUBLE + "1";

  public static final String   ATTR_DOUBLE2       = TestFixConst.ATTRIBUTE_DOUBLE + "2";

  public static final String   ATTR_DOUBLE_EXPR   = TestFixConst.ATTRIBUTE_DOUBLE + "Expression";

  public static final String   ATTR_DECIMAL1      = TestFixConst.ATTRIBUTE_DECIMAL + "1";

  public static final String   ATTR_DECIMAL2      = TestFixConst.ATTRIBUTE_DECIMAL + "2";

  public static final String   ATTR_DECIMAL_EXPR  = TestFixConst.ATTRIBUTE_DECIMAL + "Expression";

  public static final String   ATTR_DATETIME1     = TestFixConst.ATTRIBUTE_DATETIME + "1";

  public static final String   ATTR_DATETIME2     = TestFixConst.ATTRIBUTE_DATETIME + "2";

  public static final String   ATTR_DATETIME_EXPR = TestFixConst.ATTRIBUTE_DATETIME + "Expression";

  public static final String   ATTR_DATE1         = TestFixConst.ATTRIBUTE_DATE + "1";

  public static final String   ATTR_DATE2         = TestFixConst.ATTRIBUTE_DATE + "2";

  public static final String   ATTR_DATE_EXPR     = TestFixConst.ATTRIBUTE_DATE + "Expression";

  public static final String   ATTR_TIME1         = TestFixConst.ATTRIBUTE_TIME + "1";

  public static final String   ATTR_TIME2         = TestFixConst.ATTRIBUTE_TIME + "2";

  public static final String   ATTR_TIME_EXPR     = TestFixConst.ATTRIBUTE_TIME + "Expression";

  public static final String   ATTR_CHAR1         = TestFixConst.ATTRIBUTE_CHARACTER + "1";

  public static final String   ATTR_CHAR2         = TestFixConst.ATTRIBUTE_CHARACTER + "2";

  public static final String   ATTR_CHAR_EXPR     = TestFixConst.ATTRIBUTE_CHARACTER + "Expression";

  public static final String   ATTR_TEXT1         = TestFixConst.ATTRIBUTE_TEXT + "1";

  public static final String   ATTR_TEXT2         = TestFixConst.ATTRIBUTE_TEXT + "2";

  public static final String   ATTR_TEXT_EXPR     = TestFixConst.ATTRIBUTE_TEXT + "Expression";

  public static final String   ATTR_CLOB1         = TestFixConst.ATTRIBUTE_CLOB + "1";

  public static final String   ATTR_CLOB2         = TestFixConst.ATTRIBUTE_CLOB + "2";

  public static final String   ATTR_CLOB_EXPR     = TestFixConst.ATTRIBUTE_CLOB + "Expression";

  public static final String   ATTR_WEB_FLOAT     = ATTR_FLOAT1;

  public static final String   ATTR_WEB_FLOAT2    = ATTR_FLOAT2;

  private static MdBusinessDAO mdBusinessDAO;

  private static String        CLASS_TYPE;

  private static String        ATTR_CHAR_EXPR_KEY;

  private static String        ATTR_TEXT_EXPR_KEY;

  private static String        ATTR_CLOB_EXPR_KEY;

  private static String        ATTR_BOOL_EXPR_KEY;

  private static String        ATTR_INT_EXPR_KEY;

  private static String        ATTR_LONG_EXPR_KEY;

  private static String        ATTR_FLOAT_EXPR_KEY;

  private static String        ATTR_DOUBLE_EXPR_KEY;

  private static String        ATTR_DECIMAL_EXPR_KEY;

  private static String        ATTR_DATETIME_EXPR_KEY;

  private static String        ATTR_DATE_EXPR_KEY;

  private static String        ATTR_TIME_EXPR_KEY;

  private static String        WEB_FORM_KEY;

  @Request
  @BeforeClass
  public static void classSetUp()
  {
    mdBusinessDAO = TestFixtureFactory.createMdBusiness1();
    mdBusinessDAO.apply();
    CLASS_TYPE = mdBusinessDAO.getKey();

    MdAttributeCharacterDAO mdAttributeCharacterDAO1 = TestFixtureFactory.addCharacterAttribute(mdBusinessDAO, ATTR_CHAR1);
    mdAttributeCharacterDAO1.apply();
    MdAttributeCharacterDAO mdAttributeCharacterDAO2 = TestFixtureFactory.addCharacterAttribute(mdBusinessDAO, ATTR_CHAR2);
    mdAttributeCharacterDAO2.apply();
    MdAttributeCharacterDAO mdAttributeCharacterDAOexpr = TestFixtureFactory.addCharacterAttribute(mdBusinessDAO, ATTR_CHAR_EXPR);
    mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacterDAOexpr.setValue(MdAttributeCharacterInfo.SIZE, "20");
    mdAttributeCharacterDAOexpr.apply();
    ATTR_CHAR_EXPR_KEY = mdAttributeCharacterDAOexpr.getKey();

    MdAttributeTextDAO mdAttributeTextDAO1 = TestFixtureFactory.addTextAttribute(mdBusinessDAO, ATTR_TEXT1);
    mdAttributeTextDAO1.apply();
    MdAttributeTextDAO mdAttributeTextDAO2 = TestFixtureFactory.addTextAttribute(mdBusinessDAO, ATTR_TEXT2);
    mdAttributeTextDAO2.apply();
    MdAttributeTextDAO mdAttributeTextDAOexpr = TestFixtureFactory.addTextAttribute(mdBusinessDAO, ATTR_TEXT_EXPR);
    mdAttributeTextDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
    mdAttributeTextDAOexpr.apply();
    ATTR_TEXT_EXPR_KEY = mdAttributeTextDAOexpr.getKey();

    MdAttributeClobDAO mdAttributeClobDAO1 = TestFixtureFactory.addClobAttribute(mdBusinessDAO, ATTR_CLOB1);
    mdAttributeClobDAO1.apply();
    MdAttributeClobDAO mdAttributeClobDAO2 = TestFixtureFactory.addClobAttribute(mdBusinessDAO, ATTR_CLOB2);
    mdAttributeClobDAO2.apply();
    MdAttributeClobDAO mdAttributeClobDAOexpr = TestFixtureFactory.addClobAttribute(mdBusinessDAO, ATTR_CLOB_EXPR);
    mdAttributeClobDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
    mdAttributeClobDAOexpr.apply();
    ATTR_CLOB_EXPR_KEY = mdAttributeClobDAOexpr.getKey();

    MdAttributeBooleanDAO mdAttributeBooleanDAO1 = TestFixtureFactory.addBooleanAttribute(mdBusinessDAO, ATTR_BOOL1);
    mdAttributeBooleanDAO1.apply();
    MdAttributeBooleanDAO mdAttributeBooleanDAO2 = TestFixtureFactory.addBooleanAttribute(mdBusinessDAO, ATTR_BOOL2);
    mdAttributeBooleanDAO2.apply();
    MdAttributeBooleanDAO mdAttributeBooleanDAOexpr = TestFixtureFactory.addBooleanAttribute(mdBusinessDAO, ATTR_BOOL_EXPR);
    mdAttributeBooleanDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
    mdAttributeBooleanDAOexpr.apply();
    ATTR_BOOL_EXPR_KEY = mdAttributeBooleanDAOexpr.getKey();

    MdAttributeIntegerDAO mdAttributeIntegerDAO1 = TestFixtureFactory.addIntegerAttribute(mdBusinessDAO, ATTR_INT1);
    mdAttributeIntegerDAO1.setValue(MdAttributeIntegerInfo.REJECT_ZERO, MdAttributeBooleanInfo.FALSE);
    mdAttributeIntegerDAO1.setValue(MdAttributeIntegerInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeIntegerDAO1.apply();
    MdAttributeIntegerDAO mdAttributeIntegerDAO2 = TestFixtureFactory.addIntegerAttribute(mdBusinessDAO, ATTR_INT2);
    mdAttributeIntegerDAO2.setValue(MdAttributeIntegerInfo.REJECT_ZERO, MdAttributeBooleanInfo.FALSE);
    mdAttributeIntegerDAO2.setValue(MdAttributeIntegerInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeIntegerDAO2.apply();
    MdAttributeIntegerDAO mdAttributeIntegerDAOexpr = TestFixtureFactory.addIntegerAttribute(mdBusinessDAO, ATTR_INT_EXPR);
    mdAttributeIntegerDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
    mdAttributeIntegerDAOexpr.setValue(MdAttributeIntegerInfo.REJECT_ZERO, MdAttributeBooleanInfo.FALSE);
    mdAttributeIntegerDAOexpr.setValue(MdAttributeIntegerInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeIntegerDAOexpr.apply();
    ATTR_INT_EXPR_KEY = mdAttributeIntegerDAOexpr.getKey();

    MdAttributeLongDAO mdAttributeLongDAO1 = TestFixtureFactory.addLongAttribute(mdBusinessDAO, ATTR_LONG1);
    mdAttributeLongDAO1.setValue(MdAttributeIntegerInfo.REJECT_ZERO, MdAttributeBooleanInfo.FALSE);
    mdAttributeLongDAO1.setValue(MdAttributeIntegerInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeLongDAO1.apply();
    MdAttributeLongDAO mdAttributeLongDAO2 = TestFixtureFactory.addLongAttribute(mdBusinessDAO, ATTR_LONG2);
    mdAttributeLongDAO2.setValue(MdAttributeIntegerInfo.REJECT_ZERO, MdAttributeBooleanInfo.FALSE);
    mdAttributeLongDAO2.setValue(MdAttributeIntegerInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeLongDAO2.apply();
    MdAttributeLongDAO mdAttributeLongDAOexpr = TestFixtureFactory.addLongAttribute(mdBusinessDAO, ATTR_LONG_EXPR);
    mdAttributeLongDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
    mdAttributeLongDAOexpr.setValue(MdAttributeIntegerInfo.REJECT_ZERO, MdAttributeBooleanInfo.FALSE);
    mdAttributeLongDAOexpr.setValue(MdAttributeIntegerInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeLongDAOexpr.apply();
    ATTR_LONG_EXPR_KEY = mdAttributeLongDAOexpr.getKey();

    MdAttributeFloatDAO mdAttributeFloatDAO1 = TestFixtureFactory.addFloatAttribute(mdBusinessDAO, ATTR_FLOAT1);
    mdAttributeFloatDAO1.setValue(MdAttributeIntegerInfo.REJECT_ZERO, MdAttributeBooleanInfo.FALSE);
    mdAttributeFloatDAO1.setValue(MdAttributeIntegerInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeFloatDAO1.apply();
    MdAttributeFloatDAO mdAttributeFloatDAO2 = TestFixtureFactory.addFloatAttribute(mdBusinessDAO, ATTR_FLOAT2);
    mdAttributeFloatDAO2.setValue(MdAttributeIntegerInfo.REJECT_ZERO, MdAttributeBooleanInfo.FALSE);
    mdAttributeFloatDAO2.setValue(MdAttributeIntegerInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeFloatDAO2.apply();
    MdAttributeFloatDAO mdAttributeFloatDAOexpr = TestFixtureFactory.addFloatAttribute(mdBusinessDAO, ATTR_FLOAT_EXPR);
    mdAttributeFloatDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
    mdAttributeFloatDAOexpr.setValue(MdAttributeIntegerInfo.REJECT_ZERO, MdAttributeBooleanInfo.FALSE);
    mdAttributeFloatDAOexpr.setValue(MdAttributeIntegerInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeFloatDAOexpr.apply();
    ATTR_FLOAT_EXPR_KEY = mdAttributeFloatDAOexpr.getKey();

    MdAttributeDoubleDAO mdAttributeDoubleDAO1 = TestFixtureFactory.addDoubleAttribute(mdBusinessDAO, ATTR_DOUBLE1);
    mdAttributeDoubleDAO1.setValue(MdAttributeIntegerInfo.REJECT_ZERO, MdAttributeBooleanInfo.FALSE);
    mdAttributeDoubleDAO1.setValue(MdAttributeIntegerInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeDoubleDAO1.apply();
    MdAttributeDoubleDAO mdAttributeDoubleDAO2 = TestFixtureFactory.addDoubleAttribute(mdBusinessDAO, ATTR_DOUBLE2);
    mdAttributeDoubleDAO2.setValue(MdAttributeIntegerInfo.REJECT_ZERO, MdAttributeBooleanInfo.FALSE);
    mdAttributeDoubleDAO2.setValue(MdAttributeIntegerInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeDoubleDAO2.apply();
    MdAttributeDoubleDAO mdAttributeDoubleDAOexpr = TestFixtureFactory.addDoubleAttribute(mdBusinessDAO, ATTR_DOUBLE_EXPR);
    mdAttributeDoubleDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
    mdAttributeDoubleDAOexpr.setValue(MdAttributeIntegerInfo.REJECT_ZERO, MdAttributeBooleanInfo.FALSE);
    mdAttributeDoubleDAOexpr.setValue(MdAttributeIntegerInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeDoubleDAOexpr.apply();
    ATTR_DOUBLE_EXPR_KEY = mdAttributeDoubleDAOexpr.getKey();

    MdAttributeDecimalDAO mdAttributeDecimalDAO1 = TestFixtureFactory.addDecimalAttribute(mdBusinessDAO, ATTR_DECIMAL1);
    mdAttributeDecimalDAO1.setValue(MdAttributeIntegerInfo.REJECT_ZERO, MdAttributeBooleanInfo.FALSE);
    mdAttributeDecimalDAO1.setValue(MdAttributeIntegerInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeDecimalDAO1.apply();
    MdAttributeDecimalDAO mdAttributeDecimalDAO2 = TestFixtureFactory.addDecimalAttribute(mdBusinessDAO, ATTR_DECIMAL2);
    mdAttributeDecimalDAO2.setValue(MdAttributeIntegerInfo.REJECT_ZERO, MdAttributeBooleanInfo.FALSE);
    mdAttributeDecimalDAO2.setValue(MdAttributeIntegerInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeDecimalDAO2.apply();
    MdAttributeDecimalDAO mdAttributeDecimalDAOexpr = TestFixtureFactory.addDecimalAttribute(mdBusinessDAO, ATTR_DECIMAL_EXPR);
    mdAttributeDecimalDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
    mdAttributeDecimalDAOexpr.setValue(MdAttributeIntegerInfo.REJECT_ZERO, MdAttributeBooleanInfo.FALSE);
    mdAttributeDecimalDAOexpr.setValue(MdAttributeIntegerInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeDecimalDAOexpr.apply();
    ATTR_DECIMAL_EXPR_KEY = mdAttributeDecimalDAOexpr.getKey();

    MdAttributeDateTimeDAO mdAttributeDateTimeDAO1 = TestFixtureFactory.addDateTimeAttribute(mdBusinessDAO, ATTR_DATETIME1);
    mdAttributeDateTimeDAO1.setValue(MdAttributeDateInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeDateTimeDAO1.apply();
    MdAttributeDateTimeDAO mdAttributeDateTimeDAO2 = TestFixtureFactory.addDateTimeAttribute(mdBusinessDAO, ATTR_DATETIME2);
    mdAttributeDateTimeDAO2.setValue(MdAttributeDateInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeDateTimeDAO2.apply();
    MdAttributeDateTimeDAO mdAttributeDateTimeDAOexpr = TestFixtureFactory.addDateTimeAttribute(mdBusinessDAO, ATTR_DATETIME_EXPR);
    mdAttributeDateTimeDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
    mdAttributeDateTimeDAOexpr.apply();
    ATTR_DATETIME_EXPR_KEY = mdAttributeDateTimeDAOexpr.getKey();

    MdAttributeDateDAO mdAttributeDateDAO1 = TestFixtureFactory.addDateAttribute(mdBusinessDAO, ATTR_DATE1, IndexTypes.NON_UNIQUE_INDEX);
    mdAttributeDateDAO1.setValue(MdAttributeDateInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeDateDAO1.apply();
    MdAttributeDateDAO mdAttributeDateDAO2 = TestFixtureFactory.addDateAttribute(mdBusinessDAO, ATTR_DATE2, IndexTypes.NON_UNIQUE_INDEX);
    mdAttributeDateDAO2.setValue(MdAttributeDateInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeDateDAO2.apply();
    MdAttributeDateDAO mdAttributeDateDAOexpr = TestFixtureFactory.addDateAttribute(mdBusinessDAO, ATTR_DATE_EXPR, IndexTypes.NON_UNIQUE_INDEX);
    mdAttributeDateDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
    mdAttributeDateDAOexpr.apply();
    ATTR_DATE_EXPR_KEY = mdAttributeDateDAOexpr.getKey();

    MdAttributeTimeDAO mdAttributeTimeDAO1 = TestFixtureFactory.addTimeAttribute(mdBusinessDAO, ATTR_TIME1);
    mdAttributeTimeDAO1.apply();
    MdAttributeTimeDAO mdAttributeTimeDAO2 = TestFixtureFactory.addTimeAttribute(mdBusinessDAO, ATTR_TIME2);
    mdAttributeTimeDAO2.apply();
    MdAttributeTimeDAO mdAttributeTimeDAOexpr = TestFixtureFactory.addTimeAttribute(mdBusinessDAO, ATTR_TIME_EXPR);
    mdAttributeTimeDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
    mdAttributeTimeDAOexpr.apply();
    ATTR_TIME_EXPR_KEY = mdAttributeTimeDAOexpr.getKey();

    MdWebForm mdWebForm = new MdWebForm();
    mdWebForm.setFormMdClass(MdClass.get(mdBusinessDAO.getId()));
    mdWebForm.setFormName(TestFixConst.TEST_CLASS1 + "Form");
    mdWebForm.setTypeName(TestFixConst.TEST_CLASS1 + "Form");
    mdWebForm.setPackageName(TestFixConst.TEST_PACKAGE);
    mdWebForm.apply();
    WEB_FORM_KEY = mdWebForm.getKey();

    MdWebFloatDAO mdWebFloat = MdWebFloatDAO.newInstance();
    mdWebFloat.setValue(MdWebFloat.DEFININGMDFORM, mdWebForm.getId());
    mdWebFloat.setValue(MdWebPrimitive.DEFININGMDATTRIBUTE, mdAttributeFloatDAOexpr.getId());
    mdWebFloat.setValue(MdWebFloat.FIELDORDER, "1");
    mdWebFloat.setValue(MdWebFloat.FIELDNAME, ATTR_WEB_FLOAT);
    mdWebFloat.apply();
  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    MdWebForm mdWebForm = MdWebForm.getByKey(WEB_FORM_KEY);
    mdWebForm.delete();

    mdBusinessDAO = MdBusinessDAO.getMdBusinessDAO(mdBusinessDAO.definesType()).getBusinessDAO();
    mdBusinessDAO.delete();
  }

  @Request
  @Test
  public void testExpressionAttributeRequiresExpression()
  {
    MdAttributeCharacterDAO mdAttributeCharacterDAOexpr = (MdAttributeCharacterDAO) MdAttributeCharacterDAO.getByKey(ATTR_CHAR_EXPR_KEY).getBusinessDAO();

    try
    {
      mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
      mdAttributeCharacterDAOexpr.apply();

      Assert.fail(MdAttributePrimitiveInfo.CLASS + " Is set to true, net no expression was defined. An exception should have been thrown.");
    }
    catch (ProblemException e)
    {
      List<ProblemIF> problemList = e.getProblems();

      Assert.assertEquals(1, problemList.size());

      ProblemIF problemIF = problemList.get(0);

      assert ( problemIF instanceof EmptyValueProblem );
    }
    finally
    {
      mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeCharacterDAOexpr.apply();
    }
  }

  @Request
  @Test
  public void testInvalidSyntax()
  {
    MdAttributeCharacterDAO mdAttributeCharacterDAOexpr = (MdAttributeCharacterDAO) MdAttributeCharacterDAO.getByKey(ATTR_CHAR_EXPR_KEY).getBusinessDAO();

    try
    {
      mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
      mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("Some Invalid Expression");
      mdAttributeCharacterDAOexpr.apply();

      Assert.fail(InvalidExpressionSyntaxException.class.getName() + " was not thrown when an invalid expression syntax was defined.");
    }
    catch (InvalidExpressionSyntaxException e)
    {
      // this is expected
    }
    finally
    {
      mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("");
      mdAttributeCharacterDAOexpr.apply();
    }
  }

  @Request
  @Test
  public void testValidCharExpression()
  {
    MdAttributeCharacterDAO mdAttributeCharacterDAOexpr = (MdAttributeCharacterDAO) MdAttributeCharacterDAO.getByKey(ATTR_CHAR_EXPR_KEY).getBusinessDAO();
    mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue(ATTR_CHAR1 + "+\" \"+" + ATTR_CHAR2);
    mdAttributeCharacterDAOexpr.apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(CLASS_TYPE);
    businessDAO.setValue(ATTR_CHAR1, "Optimus");
    businessDAO.setValue(ATTR_CHAR2, "Prime");
    businessDAO.apply();

    Business business = Business.get(businessDAO.getId());

    try
    {
      business.apply();

      business = Business.get(business.getId());

      String expressionResult = business.getValue(ATTR_CHAR_EXPR);

      Assert.assertEquals("Optimus Prime", expressionResult);
    }
    finally
    {
      business.delete();

      mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("");
      mdAttributeCharacterDAOexpr.apply();
    }
  }

  @Request
  @Test
  public void testCharRequiredExpression()
  {
    MdAttributeCharacterDAO mdAttributeCharacterDAOexpr = (MdAttributeCharacterDAO) MdAttributeCharacterDAO.getByKey(ATTR_CHAR_EXPR_KEY).getBusinessDAO();
    mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.REQUIRED).setValue(MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue(ATTR_CHAR1);
    mdAttributeCharacterDAOexpr.apply();

    Business business = BusinessFacade.newBusiness(CLASS_TYPE);

    try
    {
      business.apply();

      business.delete();

      Assert.fail("An expression set to required did not receive a value.");
    }
    catch (ProblemException e)
    {
      List<ProblemIF> problemList = e.getProblems();

      Assert.assertEquals(1, problemList.size());

      ProblemIF problemIF = problemList.get(0);

      assert ( problemIF instanceof EmptyValueProblem );
    }
    finally
    {
      mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.REQUIRED).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("");
      mdAttributeCharacterDAOexpr.apply();
    }
  }

  @Request
  @Test
  public void testInvalidCharExpression_BadExpression()
  {
    MdAttributeCharacterDAO mdAttributeCharacterDAOexpr = (MdAttributeCharacterDAO) MdAttributeCharacterDAO.getByKey(ATTR_CHAR_EXPR_KEY).getBusinessDAO();
    mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue(ATTR_CHAR1 + "-" + ATTR_CHAR2);
    mdAttributeCharacterDAOexpr.apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(CLASS_TYPE);
    businessDAO.setValue(ATTR_CHAR1, "Optimus");
    businessDAO.setValue(ATTR_CHAR2, "Prime");
    businessDAO.apply();

    Business business = Business.get(businessDAO.getId());

    try
    {
      business.apply();

      Assert.fail("invalid expresison failed to throw exception.");
    }
    catch (ExpressionException e)
    {
      // this is expected
    }
    catch (RuntimeException e)
    {
      Assert.fail(ExpressionException.class.getName() + " was not thrown.");
    }
    finally
    {
      business.delete();

      mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("");
      mdAttributeCharacterDAOexpr.apply();
    }
  }

  @Request
  @Test
  public void testValidTextExpression()
  {
    MdAttributeTextDAO mdAttributeTextDAOexpr = (MdAttributeTextDAO) MdAttributeTextDAO.getByKey(ATTR_TEXT_EXPR_KEY).getBusinessDAO();
    mdAttributeTextDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
    mdAttributeTextDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue(ATTR_TEXT1 + "+\" \"+" + ATTR_TEXT2);
    mdAttributeTextDAOexpr.apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(CLASS_TYPE);
    businessDAO.setValue(ATTR_TEXT1, "Optimus");
    businessDAO.setValue(ATTR_TEXT2, "Prime");
    businessDAO.apply();

    Business business = Business.get(businessDAO.getId());

    try
    {
      business.apply();

      business = Business.get(business.getId());

      String expressionResult = business.getValue(ATTR_TEXT_EXPR);

      Assert.assertEquals("Optimus Prime", expressionResult);
    }
    finally
    {
      business.delete();

      mdAttributeTextDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeTextDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("");
      mdAttributeTextDAOexpr.apply();
    }
  }

  @Request
  @Test
  public void testInvalidTextExpression_BadExpression()
  {
    MdAttributeTextDAO mdAttributeTextDAOexpr = (MdAttributeTextDAO) MdAttributeTextDAO.getByKey(ATTR_TEXT_EXPR_KEY).getBusinessDAO();
    mdAttributeTextDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
    mdAttributeTextDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue(ATTR_TEXT1 + "-" + ATTR_TEXT2);
    mdAttributeTextDAOexpr.apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(CLASS_TYPE);
    businessDAO.setValue(ATTR_TEXT1, "Optimus");
    businessDAO.setValue(ATTR_TEXT2, "Prime");
    businessDAO.apply();

    Business business = Business.get(businessDAO.getId());

    try
    {
      business.apply();

      Assert.fail("invalid expresison failed to throw exception.");
    }
    catch (ExpressionException e)
    {
      // this is expected
    }
    catch (RuntimeException e)
    {
      Assert.fail(ExpressionException.class.getName() + " was not thrown.");
    }
    finally
    {
      business.delete();

      mdAttributeTextDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeTextDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("");
      mdAttributeTextDAOexpr.apply();
    }
  }

  @Request
  @Test
  public void testValidClobExpression()
  {
    MdAttributeClobDAO mdAttributeClobDAOexpr = (MdAttributeClobDAO) MdAttributeClobDAO.getByKey(ATTR_CLOB_EXPR_KEY).getBusinessDAO();
    mdAttributeClobDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
    mdAttributeClobDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue(ATTR_CLOB1 + "+\" \"+" + ATTR_CLOB2);
    mdAttributeClobDAOexpr.apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(CLASS_TYPE);
    businessDAO.setValue(ATTR_CLOB1, "Optimus");
    businessDAO.setValue(ATTR_CLOB2, "Prime");
    businessDAO.apply();

    Business business = Business.get(businessDAO.getId());

    try
    {
      business.apply();

      business = Business.get(business.getId());

      String expressionResult = business.getValue(ATTR_CLOB_EXPR);

      Assert.assertEquals("Optimus Prime", expressionResult);
    }
    finally
    {
      business.delete();

      mdAttributeClobDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeClobDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("");
      mdAttributeClobDAOexpr.apply();
    }
  }

  @Request
  @Test
  public void testInvalidClobExpression_BadExpression()
  {
    MdAttributeClobDAO mdAttributeClobDAOexpr = (MdAttributeClobDAO) MdAttributeClobDAO.getByKey(ATTR_CLOB_EXPR_KEY).getBusinessDAO();
    mdAttributeClobDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
    mdAttributeClobDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue(ATTR_CLOB1 + "-" + ATTR_CLOB2);
    mdAttributeClobDAOexpr.apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(CLASS_TYPE);
    businessDAO.setValue(ATTR_CLOB1, "Optimus");
    businessDAO.setValue(ATTR_CLOB2, "Prime");
    businessDAO.apply();

    Business business = Business.get(businessDAO.getId());

    try
    {
      business.apply();

      Assert.fail("invalid expresison failed to throw exception.");
    }
    catch (ExpressionException e)
    {
      // this is expected
    }
    catch (RuntimeException e)
    {
      Assert.fail(ExpressionException.class.getName() + " was not thrown.");
    }
    finally
    {
      business.delete();

      mdAttributeClobDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeClobDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("");
      mdAttributeClobDAOexpr.apply();
    }
  }

  @Request
  @Test
  public void testInvalidCharExpression_CharToLong()
  {
    MdAttributeCharacterDAO mdAttributeCharacterDAOexpr = (MdAttributeCharacterDAO) MdAttributeCharacterDAO.getByKey(ATTR_CHAR_EXPR_KEY).getBusinessDAO();
    mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue(ATTR_CHAR1 + "+\" \"+" + ATTR_CHAR2);
    mdAttributeCharacterDAOexpr.apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(CLASS_TYPE);
    businessDAO.setValue(ATTR_CHAR1, "01234567890123");
    businessDAO.setValue(ATTR_CHAR2, "01234567890123");
    businessDAO.apply();

    Business business = Business.get(businessDAO.getId());

    try
    {
      business.apply();

      Assert.fail("Result of the character expression exceded the length of the expression character field, yet no exception was thrown.");
    }
    catch (AttributeLengthCharacterException e)
    {
      // this is expected
    }

    catch (RuntimeException e)
    {
      Assert.fail(AttributeLengthCharacterException.class.getName() + " was not thrown.");
    }
    finally
    {
      business.delete();

      mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("");
      mdAttributeCharacterDAOexpr.apply();
    }
  }

  @Request
  @Test
  public void testValidBooleanExpression_False1()
  {
    MdAttributeBooleanDAO mdAttributeBooleanDAOexpr = (MdAttributeBooleanDAO) MdAttributeBooleanDAO.getByKey(ATTR_BOOL_EXPR_KEY).getBusinessDAO();
    mdAttributeBooleanDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
    mdAttributeBooleanDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("true&false");
    mdAttributeBooleanDAOexpr.apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(CLASS_TYPE);
    businessDAO.apply();

    Business business = Business.get(businessDAO.getId());

    try
    {
      business.apply();

      business = Business.get(business.getId());

      String expressionResult = business.getValue(ATTR_BOOL_EXPR);

      Assert.assertEquals(MdAttributeBooleanInfo.FALSE, expressionResult);
    }
    finally
    {
      business.delete();

      mdAttributeBooleanDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeBooleanDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("");
      mdAttributeBooleanDAOexpr.apply();
    }
  }

  @Request
  @Test
  public void testValidBooleanExpression_True1()
  {
    MdAttributeBooleanDAO mdAttributeBooleanDAOexpr = (MdAttributeBooleanDAO) MdAttributeBooleanDAO.getByKey(ATTR_BOOL_EXPR_KEY).getBusinessDAO();
    mdAttributeBooleanDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
    mdAttributeBooleanDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("true|false");
    mdAttributeBooleanDAOexpr.apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(CLASS_TYPE);
    businessDAO.setValue(ATTR_BOOL1, MdAttributeBooleanInfo.TRUE);
    businessDAO.setValue(ATTR_BOOL2, MdAttributeBooleanInfo.FALSE);
    businessDAO.apply();

    Business business = Business.get(businessDAO.getId());

    try
    {
      business.apply();

      business = Business.get(business.getId());

      String expressionResult = business.getValue(ATTR_BOOL_EXPR);

      Assert.assertEquals(MdAttributeBooleanInfo.TRUE, expressionResult);
    }
    finally
    {
      business.delete();

      mdAttributeBooleanDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeBooleanDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("");
      mdAttributeBooleanDAOexpr.apply();
    }
  }

  @Request
  @Test
  public void testValidBooleanExpression_False2()
  {
    MdAttributeBooleanDAO mdAttributeBooleanDAOexpr = (MdAttributeBooleanDAO) MdAttributeBooleanDAO.getByKey(ATTR_BOOL_EXPR_KEY).getBusinessDAO();
    mdAttributeBooleanDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
    mdAttributeBooleanDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue(ATTR_BOOL1 + "&" + ATTR_BOOL2);
    mdAttributeBooleanDAOexpr.apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(CLASS_TYPE);
    businessDAO.setValue(ATTR_BOOL1, MdAttributeBooleanInfo.TRUE);
    businessDAO.setValue(ATTR_BOOL2, MdAttributeBooleanInfo.FALSE);
    businessDAO.apply();

    Business business = Business.get(businessDAO.getId());

    try
    {
      business.apply();

      business = Business.get(business.getId());

      String expressionResult = business.getValue(ATTR_BOOL_EXPR);

      Assert.assertEquals(MdAttributeBooleanInfo.FALSE, expressionResult);
    }
    finally
    {
      business.delete();

      mdAttributeBooleanDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeBooleanDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("");
      mdAttributeBooleanDAOexpr.apply();
    }
  }

  @Request
  @Test
  public void testValidBooleanExpression_True2()
  {
    MdAttributeBooleanDAO mdAttributeBooleanDAOexpr = (MdAttributeBooleanDAO) MdAttributeBooleanDAO.getByKey(ATTR_BOOL_EXPR_KEY).getBusinessDAO();
    mdAttributeBooleanDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
    mdAttributeBooleanDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue(ATTR_BOOL1 + "|" + ATTR_BOOL2);
    mdAttributeBooleanDAOexpr.apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(CLASS_TYPE);
    businessDAO.setValue(ATTR_BOOL1, MdAttributeBooleanInfo.TRUE);
    businessDAO.setValue(ATTR_BOOL2, MdAttributeBooleanInfo.FALSE);
    businessDAO.apply();

    Business business = Business.get(businessDAO.getId());

    try
    {
      business.apply();

      business = Business.get(business.getId());

      String expressionResult = business.getValue(ATTR_BOOL_EXPR);

      Assert.assertEquals(MdAttributeBooleanInfo.TRUE, expressionResult);
    }
    finally
    {
      business.delete();

      mdAttributeBooleanDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeBooleanDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("");
      mdAttributeBooleanDAOexpr.apply();
    }
  }

  @Request
  @Test
  public void testInvalidBooleanExpression()
  {
    MdAttributeBooleanDAO mdAttributeBooleanDAOexpr = (MdAttributeBooleanDAO) MdAttributeBooleanDAO.getByKey(ATTR_BOOL_EXPR_KEY).getBusinessDAO();
    mdAttributeBooleanDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
    mdAttributeBooleanDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("\"invalidboolean\"");
    mdAttributeBooleanDAOexpr.apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(CLASS_TYPE);
    businessDAO.apply();

    Business business = Business.get(businessDAO.getId());

    try
    {
      business.apply();

      Assert.fail("An invalid boolean value was successfully persisted on a boolean field.");
    }
    catch (AttributeValueException e)
    {
      // this is expected
    }
    finally
    {
      business.delete();

      mdAttributeBooleanDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeBooleanDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("");
      mdAttributeBooleanDAOexpr.apply();
    }
  }

  @Request
  @Test
  public void testValidDateTimeExpression()
  {
    MdAttributeDateTimeDAO mdAttributeDateTimeDAOexpr = (MdAttributeDateTimeDAO) MdAttributeDateTimeDAO.getByKey(ATTR_DATETIME_EXPR_KEY).getBusinessDAO();
    mdAttributeDateTimeDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
    mdAttributeDateTimeDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue(ATTR_DATETIME1);
    mdAttributeDateTimeDAOexpr.apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(CLASS_TYPE);
    businessDAO.setValue(ATTR_DATETIME1, "2005-06-15 09:00:00");
    businessDAO.apply();

    Business business = Business.get(businessDAO.getId());

    try
    {
      business.apply();

      business = Business.get(business.getId());

      String expressionResult = business.getValue(ATTR_DATETIME_EXPR);

      Date expectedDate = MdAttributeDateTimeUtil.getTypeSafeValue("2005-06-15 09:00:00");
      Date expressionDate = MdAttributeDateTimeUtil.getTypeSafeValue(expressionResult);

      Assert.assertEquals(expectedDate, expressionDate);
    }
    finally
    {
      business.delete();

      mdAttributeDateTimeDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeDateTimeDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("");
      mdAttributeDateTimeDAOexpr.apply();
    }
  }

  @Request
  @Test
  public void testInvalidDateTimeExpression()
  {
    MdAttributeDateTimeDAO mdAttributeDateTimeDAOexpr = (MdAttributeDateTimeDAO) MdAttributeDateTimeDAO.getByKey(ATTR_DATETIME_EXPR_KEY).getBusinessDAO();
    mdAttributeDateTimeDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
    mdAttributeDateTimeDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("\"Invalid Date Time\"");
    mdAttributeDateTimeDAOexpr.apply();

    Business business = BusinessFacade.newBusiness(CLASS_TYPE);

    try
    {
      business.apply();

      business.delete();

      Assert.fail("Applied to an expression attribute of type date time a value that is a character");
    }
    catch (AttributeValueException e)
    {
      // this is expected
    }
    finally
    {
      mdAttributeDateTimeDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeDateTimeDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("");
      mdAttributeDateTimeDAOexpr.apply();
    }
  }

  @Request
  @Test
  public void testValidDateExpression()
  {
    MdAttributeDateDAO mdAttributeDateDAOexpr = (MdAttributeDateDAO) MdAttributeDateDAO.getByKey(ATTR_DATE_EXPR_KEY).getBusinessDAO();
    mdAttributeDateDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
    mdAttributeDateDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue(ATTR_DATE1);
    mdAttributeDateDAOexpr.apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(CLASS_TYPE);
    businessDAO.setValue(ATTR_DATE1, "2005-06-15");
    businessDAO.apply();

    Business business = Business.get(businessDAO.getId());

    try
    {
      business.apply();

      business = Business.get(business.getId());

      String expressionResult = business.getValue(ATTR_DATE_EXPR);

      Date expectedDate = MdAttributeDateUtil.getTypeSafeValue("2005-06-15");
      Date expressionDate = MdAttributeDateUtil.getTypeSafeValue(expressionResult);

      Assert.assertEquals(expectedDate, expressionDate);
    }
    finally
    {
      business.delete();

      mdAttributeDateDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeDateDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("");
      mdAttributeDateDAOexpr.apply();
    }
  }

  @Request
  @Test
  public void testInvalidDateExpression()
  {
    MdAttributeDateDAO mdAttributeDateDAOexpr = (MdAttributeDateDAO) MdAttributeDateDAO.getByKey(ATTR_DATE_EXPR_KEY).getBusinessDAO();
    mdAttributeDateDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
    mdAttributeDateDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("\"Invalid Date\"");
    mdAttributeDateDAOexpr.apply();

    Business business = BusinessFacade.newBusiness(CLASS_TYPE);

    try
    {
      business.apply();

      business.delete();

      Assert.fail("Applied to an expression attribute of type date a value that is a character");
    }
    catch (AttributeValueException e)
    {
      // this is expected
    }
    finally
    {
      mdAttributeDateDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeDateDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("");
      mdAttributeDateDAOexpr.apply();
    }
  }

  @Request
  @Test
  public void testValidTimeExpression()
  {
    MdAttributeTimeDAO mdAttributeTimeDAOexpr = (MdAttributeTimeDAO) MdAttributeTimeDAO.getByKey(ATTR_TIME_EXPR_KEY).getBusinessDAO();
    mdAttributeTimeDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
    mdAttributeTimeDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue(ATTR_TIME1);
    mdAttributeTimeDAOexpr.apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(CLASS_TYPE);
    businessDAO.setValue(ATTR_TIME1, "09:00:00");
    businessDAO.apply();

    Business business = Business.get(businessDAO.getId());

    try
    {
      business.apply();

      business = Business.get(business.getId());

      String expressionResult = business.getValue(ATTR_TIME_EXPR);

      Date expectedDate = MdAttributeTimeUtil.getTypeSafeValue("09:00:00");
      Date expressionDate = MdAttributeTimeUtil.getTypeSafeValue(expressionResult);

      Assert.assertEquals(expectedDate, expressionDate);
    }
    finally
    {
      business.delete();

      mdAttributeTimeDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeTimeDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("");
      mdAttributeTimeDAOexpr.apply();
    }
  }

  @Request
  @Test
  public void testInvalidTimeExpression()
  {
    MdAttributeTimeDAO mdAttributeTimeDAOexpr = (MdAttributeTimeDAO) MdAttributeTimeDAO.getByKey(ATTR_TIME_EXPR_KEY).getBusinessDAO();
    mdAttributeTimeDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
    mdAttributeTimeDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("\"Invalid Time\"");
    mdAttributeTimeDAOexpr.apply();

    Business business = BusinessFacade.newBusiness(CLASS_TYPE);

    try
    {
      business.apply();

      business.delete();

      Assert.fail("Applied to an expression attribute of type date a value that is a character");
    }
    catch (AttributeValueException e)
    {
      // this is expected
    }
    finally
    {
      mdAttributeTimeDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeTimeDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("");
      mdAttributeTimeDAOexpr.apply();
    }
  }

  @Request
  @Test
  public void testValidIntegerExpression()
  {
    MdAttributeIntegerDAO mdAttributeIntegerDAOexpr = (MdAttributeIntegerDAO) MdAttributeIntegerDAO.getByKey(ATTR_INT_EXPR_KEY).getBusinessDAO();
    mdAttributeIntegerDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
    mdAttributeIntegerDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue(ATTR_INT1 + "+" + ATTR_INT2);
    mdAttributeIntegerDAOexpr.apply();

    Business business = BusinessFacade.newBusiness(CLASS_TYPE);
    business.setValue(ATTR_INT1, "1");
    business.setValue(ATTR_INT2, "2");

    try
    {
      business.apply();

      business = Business.get(business.getId());

      String expressionResult = business.getValue(ATTR_INT_EXPR);

      Assert.assertEquals(3, Integer.parseInt(expressionResult));
    }
    finally
    {
      business.delete();

      mdAttributeIntegerDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeIntegerDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("");
      mdAttributeIntegerDAOexpr.apply();
    }
  }

  @Request
  @Test
  public void testInvalidIntegerExpression()
  {
    MdAttributeIntegerDAO mdAttributeIntegerDAOexpr = (MdAttributeIntegerDAO) MdAttributeIntegerDAO.getByKey(ATTR_INT_EXPR_KEY).getBusinessDAO();
    mdAttributeIntegerDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
    mdAttributeIntegerDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("\"Not An Integer\"");
    mdAttributeIntegerDAOexpr.apply();

    Business business = BusinessFacade.newBusiness(CLASS_TYPE);

    try
    {
      business.apply();

      business.delete();

      Assert.fail("Applied to an expression attribute of type integer a value that is a character");
    }
    catch (AttributeValueException e)
    {
      // this is expected
    }
    finally
    {
      mdAttributeIntegerDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeIntegerDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("");
      mdAttributeIntegerDAOexpr.apply();
    }
  }

  @Request
  @Test
  public void testValidLongExpression()
  {
    MdAttributeLongDAO mdAttributeLongDAOexpr = (MdAttributeLongDAO) MdAttributeLongDAO.getByKey(ATTR_LONG_EXPR_KEY).getBusinessDAO();
    mdAttributeLongDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
    mdAttributeLongDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue(ATTR_LONG1 + "+" + ATTR_LONG2);
    mdAttributeLongDAOexpr.apply();

    Business business = BusinessFacade.newBusiness(CLASS_TYPE);
    business.setValue(ATTR_LONG1, "1");
    business.setValue(ATTR_LONG2, "2");

    try
    {
      business.apply();

      business = Business.get(business.getId());

      String expressionResult = business.getValue(ATTR_LONG_EXPR);

      Assert.assertEquals(3, Integer.parseInt(expressionResult));
    }
    finally
    {
      business.delete();

      mdAttributeLongDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeLongDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("");
      mdAttributeLongDAOexpr.apply();
    }
  }

  @Request
  @Test
  public void testInvalidLongExpression()
  {
    MdAttributeLongDAO mdAttributeLongDAOexpr = (MdAttributeLongDAO) MdAttributeLongDAO.getByKey(ATTR_LONG_EXPR_KEY).getBusinessDAO();
    mdAttributeLongDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
    mdAttributeLongDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("\"Invalid Long\"");
    mdAttributeLongDAOexpr.apply();

    Business business = BusinessFacade.newBusiness(CLASS_TYPE);

    try
    {
      business.apply();

      business.delete();

      Assert.fail("Applied to an expression attribute of type long a value that is a character");
    }
    catch (AttributeValueException e)
    {
      // this is expected
    }
    finally
    {

      mdAttributeLongDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeLongDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("");
      mdAttributeLongDAOexpr.apply();
    }
  }

  @Request
  @Test
  public void testUpdateCalculatedInstances()
  {
    MdAttributeFloatDAO mdAttributeFloatDAOexpr = (MdAttributeFloatDAO) MdAttributeFloatDAO.getByKey(ATTR_FLOAT_EXPR_KEY).getBusinessDAO();
    mdAttributeFloatDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
    mdAttributeFloatDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue(ATTR_FLOAT1 + "+" + ATTR_FLOAT2);
    mdAttributeFloatDAOexpr.apply();

    Business business = BusinessFacade.newBusiness(CLASS_TYPE);
    business.setValue(ATTR_FLOAT1, "2.0");
    business.setValue(ATTR_FLOAT2, "2.0");
    business.apply();

    try
    {
      business = Business.get(business.getId());
      Assert.assertEquals(4.0f, Float.parseFloat(business.getValue(ATTR_FLOAT_EXPR)), 0);

      mdAttributeFloatDAOexpr = (MdAttributeFloatDAO) MdAttributeFloatDAO.getByKey(ATTR_FLOAT_EXPR_KEY).getBusinessDAO();
      mdAttributeFloatDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
      mdAttributeFloatDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue(ATTR_FLOAT1 + "-" + ATTR_FLOAT2);
      mdAttributeFloatDAOexpr.apply();

      business = Business.get(business.getId());
      Assert.assertEquals(0.0f, Float.parseFloat(business.getValue(ATTR_FLOAT_EXPR)), 0);
    }
    finally
    {
      business.delete();

      mdAttributeFloatDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeFloatDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("");
      mdAttributeFloatDAOexpr.apply();
    }
  }

  @Request
  @Test
  public void testValidFloatExpression()
  {
    MdAttributeFloatDAO mdAttributeFloatDAOexpr = (MdAttributeFloatDAO) MdAttributeFloatDAO.getByKey(ATTR_FLOAT_EXPR_KEY).getBusinessDAO();
    mdAttributeFloatDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
    mdAttributeFloatDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue(ATTR_FLOAT1 + "+" + ATTR_FLOAT2);
    mdAttributeFloatDAOexpr.apply();

    Business business = BusinessFacade.newBusiness(CLASS_TYPE);
    business.setValue(ATTR_FLOAT1, "1.0");
    business.setValue(ATTR_FLOAT2, "2.0");

    try
    {
      business.apply();

      business = Business.get(business.getId());

      String expressionResult = business.getValue(ATTR_FLOAT_EXPR);

      Assert.assertTrue(3.0 == Float.parseFloat(expressionResult));
    }
    finally
    {
      business.delete();

      mdAttributeFloatDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeFloatDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("");
      mdAttributeFloatDAOexpr.apply();
    }
  }

  @Request
  @Test
  public void testInvalidFloatExpression()
  {
    MdAttributeFloatDAO mdAttributeFloatDAOexpr = (MdAttributeFloatDAO) MdAttributeFloatDAO.getByKey(ATTR_FLOAT_EXPR_KEY).getBusinessDAO();
    mdAttributeFloatDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
    mdAttributeFloatDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("\"Invalid Float\"");
    mdAttributeFloatDAOexpr.apply();

    Business business = BusinessFacade.newBusiness(CLASS_TYPE);

    try
    {
      business.apply();

      business.delete();

      Assert.fail("Applied to an expression attribute of type float a value that is a character");
    }
    catch (AttributeValueException e)
    {
      // this is expected
    }
    finally
    {
      mdAttributeFloatDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeFloatDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("");
      mdAttributeFloatDAOexpr.apply();
    }
  }

  @Request
  @Test
  public void testValidDoubleExpression()
  {
    MdAttributeDoubleDAO mdAttributeDoubleDAOexpr = (MdAttributeDoubleDAO) MdAttributeDoubleDAO.getByKey(ATTR_DOUBLE_EXPR_KEY).getBusinessDAO();
    mdAttributeDoubleDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
    mdAttributeDoubleDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue(ATTR_DOUBLE1 + "+" + ATTR_DOUBLE2);
    mdAttributeDoubleDAOexpr.apply();

    Business business = BusinessFacade.newBusiness(CLASS_TYPE);
    business.setValue(ATTR_DOUBLE1, "1.0");
    business.setValue(ATTR_DOUBLE2, "2.0");

    try
    {
      business.apply();

      business = Business.get(business.getId());

      String expressionResult = business.getValue(ATTR_DOUBLE_EXPR);

      Assert.assertTrue(3.0 == Float.parseFloat(expressionResult));
    }
    finally
    {
      business.delete();

      mdAttributeDoubleDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeDoubleDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("");
      mdAttributeDoubleDAOexpr.apply();
    }
  }

  @Request
  @Test
  public void testInvalidDoubleExpression()
  {
    MdAttributeDoubleDAO mdAttributeDoubleDAOexpr = (MdAttributeDoubleDAO) MdAttributeDoubleDAO.getByKey(ATTR_DOUBLE_EXPR_KEY).getBusinessDAO();
    mdAttributeDoubleDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
    mdAttributeDoubleDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("\"Invalid Double\"");
    mdAttributeDoubleDAOexpr.apply();

    Business business = BusinessFacade.newBusiness(CLASS_TYPE);

    try
    {
      business.apply();

      business.delete();

      Assert.fail("Applied to an expression attribute of type float a value that is a character");
    }
    catch (AttributeValueException e)
    {
      // this is expected
    }
    finally
    {

      mdAttributeDoubleDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeDoubleDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("");
      mdAttributeDoubleDAOexpr.apply();
    }
  }

  @Request
  @Test
  public void testValidDecimalExpression()
  {
    MdAttributeDecimalDAO mdAttributeDecimalDAOexpr = (MdAttributeDecimalDAO) MdAttributeDecimalDAO.getByKey(ATTR_DECIMAL_EXPR_KEY).getBusinessDAO();
    mdAttributeDecimalDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
    mdAttributeDecimalDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue(ATTR_DECIMAL1 + "+" + ATTR_DECIMAL2);
    mdAttributeDecimalDAOexpr.apply();

    Business business = BusinessFacade.newBusiness(CLASS_TYPE);
    business.setValue(ATTR_DECIMAL1, "1.0");
    business.setValue(ATTR_DECIMAL2, "2.0");

    try
    {
      business.apply();

      business = Business.get(business.getId());

      String expressionResult = business.getValue(ATTR_DECIMAL_EXPR);

      Assert.assertTrue(3.0 == Float.parseFloat(expressionResult));
    }
    finally
    {
      business.delete();

      mdAttributeDecimalDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeDecimalDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("");
      mdAttributeDecimalDAOexpr.apply();
    }
  }

  @Request
  @Test
  public void testInvalidDecimalExpression()
  {
    MdAttributeDecimalDAO mdAttributeDecimalDAOexpr = (MdAttributeDecimalDAO) MdAttributeDecimalDAO.getByKey(ATTR_DECIMAL_EXPR_KEY).getBusinessDAO();
    mdAttributeDecimalDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
    mdAttributeDecimalDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("\"Invalid Decimal\"");
    mdAttributeDecimalDAOexpr.apply();

    Business business = BusinessFacade.newBusiness(CLASS_TYPE);

    try
    {
      business.apply();

      business.delete();

      Assert.fail("Applied to an expression attribute of type float a value that is a character");
    }
    catch (AttributeValueException e)
    {
      // this is expected
    }
    finally
    {
      mdAttributeDecimalDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeDecimalDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("");
      mdAttributeDecimalDAOexpr.apply();
    }
  }

  /**
   * Test to see if the expression engine can handel a classloader change.
   */
  @Request
  @Test
  public void testValidClassloader()
  {
    MdAttributeCharacterDAO mdAttributeCharacterDAOexpr = (MdAttributeCharacterDAO) MdAttributeCharacterDAO.getByKey(ATTR_CHAR_EXPR_KEY).getBusinessDAO();
    mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue(ATTR_CHAR1 + "+\" \"+" + ATTR_CHAR2);
    mdAttributeCharacterDAOexpr.apply();

    MdAttributeCharacterDAO mdAttributeCharacterDAO_New = null;

    BusinessDAO businessDAO = BusinessDAO.newInstance(CLASS_TYPE);
    businessDAO.setValue(ATTR_CHAR1, "Optimus");
    businessDAO.setValue(ATTR_CHAR2, "Prime");
    businessDAO.apply();

    Business business1 = Business.get(businessDAO.getId());
    Business business2 = null;
    try
    {
      business1.apply();
      business1 = Business.get(business1.getId());
      String expressionResult = business1.getValue(ATTR_CHAR_EXPR);
      Assert.assertEquals("Optimus Prime", expressionResult);

      int ognlCachedMethodsCount = OgnlClassResolver.methodCacheCount();
      Assert.assertTrue("Ongl changed the way they implement their reflection method cache.", ognlCachedMethodsCount > 0);
      Assert.assertEquals("Potentially Ongl changed its internal implementation, as since two attributes are in the expression, only two methods should be in the cache.", 2, ognlCachedMethodsCount);

      mdAttributeCharacterDAO_New = TestFixtureFactory.addCharacterAttribute(mdBusinessDAO, "NEW_CHARACTER");
      mdAttributeCharacterDAO_New.apply();

      business2 = BusinessFacade.newBusiness(CLASS_TYPE);
      business2.setValue(ATTR_CHAR1, "Ultra");
      business2.setValue(ATTR_CHAR2, "Magnus");
      business2.apply();
      business2 = Business.get(business2.getId());
      expressionResult = business2.getValue(ATTR_CHAR_EXPR);
      Assert.assertEquals("Ultra Magnus", expressionResult);

      Assert.assertEquals("Ognl should have the same number of cached items.", ognlCachedMethodsCount, OgnlClassResolver.methodCacheCount());
      Assert.assertEquals("Potentially Ongl changed its internal implementation, as since two attributes are in the expression, only two methods should be in the cache.", 2, OgnlClassResolver.methodCacheCount());
    }
    finally
    {
      if (business1 != null && business1.isAppliedToDB())
      {
        business1.delete();
      }

      if (business2 != null && business2.isAppliedToDB())
      {
        business2.delete();
      }

      mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("");
      mdAttributeCharacterDAOexpr.apply();

      if (mdAttributeCharacterDAO_New != null && mdAttributeCharacterDAO_New.isAppliedToDB())
      {
        mdAttributeCharacterDAO_New.delete();
      }
    }
  }

}
