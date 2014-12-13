/**
*
*/
package com.runwaysdk.business;

import java.util.List;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.ProblemException;
import com.runwaysdk.ProblemIF;
import com.runwaysdk.constants.DatabaseProperties;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributePrimitiveInfo;
import com.runwaysdk.constants.TestConstants;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.attributes.EmptyValueProblem;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;
import com.runwaysdk.dataaccess.io.XMLImporter;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;

/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
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
 ******************************************************************************/
public class ExpressionAttributeTest extends TestCase
{

  public static final String ATTR_BOOL1             = TestFixConst.ATTRIBUTE_BOOLEAN+"1";
  public static final String ATTR_BOOL2             = TestFixConst.ATTRIBUTE_BOOLEAN+"2";
  public static final String ATTR_BOOL_EXPR         = TestFixConst.ATTRIBUTE_BOOLEAN+"Expression";

  public static final String ATTR_INT1              = TestFixConst.ATTRIBUTE_INTEGER+"1";
  public static final String ATTR_INT2              = TestFixConst.ATTRIBUTE_INTEGER+"2";
  public static final String ATTR_INT_EXPR          = TestFixConst.ATTRIBUTE_INTEGER+"Expression";

  public static final String ATTR_LONG1             = TestFixConst.ATTRIBUTE_LONG+"1";
  public static final String ATTR_LONG2             = TestFixConst.ATTRIBUTE_LONG+"2";
  public static final String ATTR_LONG_EXPR         = TestFixConst.ATTRIBUTE_LONG+"Expression";

  public static final String ATTR_FLOAT1            = TestFixConst.ATTRIBUTE_FLOAT+"1";
  public static final String ATTR_FLOAT2            = TestFixConst.ATTRIBUTE_FLOAT+"2";
  public static final String ATTR_FLOAT_EXPR        = TestFixConst.ATTRIBUTE_FLOAT+"Expression";

  public static final String ATTR_DOUBLE1           = TestFixConst.ATTRIBUTE_DOUBLE+"1";
  public static final String ATTR_DOUBLE2           = TestFixConst.ATTRIBUTE_DOUBLE+"2";
  public static final String ATTR_DOUBLE_EXPR       = TestFixConst.ATTRIBUTE_DOUBLE+"Expression";

  public static final String ATTR_DECIMAL1          = TestFixConst.ATTRIBUTE_DECIMAL+"1";
  public static final String ATTR_DECIMAL2          = TestFixConst.ATTRIBUTE_DECIMAL+"2";
  public static final String ATTR_DECIMAL_EXPR      = TestFixConst.ATTRIBUTE_DECIMAL+"Expression";

  public static final String ATTR_DATETIME1         = TestFixConst.ATTRIBUTE_DATETIME+"1";
  public static final String ATTR_DATETIME2         = TestFixConst.ATTRIBUTE_DATETIME+"2";
  public static final String ATTR_DATETIME_EXPR     = TestFixConst.ATTRIBUTE_DATETIME+"Expression";

  public static final String ATTR_DATE1             = TestFixConst.ATTRIBUTE_DATE+"1";
  public static final String ATTR_DATE2             = TestFixConst.ATTRIBUTE_DATE+"2";
  public static final String ATTR_DATE_EXPR         = TestFixConst.ATTRIBUTE_DATE+"Expression";

  public static final String ATTR_TIME1             = TestFixConst.ATTRIBUTE_TIME+"1";
  public static final String ATTR_TIME2             = TestFixConst.ATTRIBUTE_TIME+"2";
  public static final String ATTR_TIME_EXPR         = TestFixConst.ATTRIBUTE_TIME+"Expression";
  
  public static final String ATTR_CHAR1             = TestFixConst.ATTRIBUTE_CHARACTER+"1";
  public static final String ATTR_CHAR2             = TestFixConst.ATTRIBUTE_CHARACTER+"2";
  public static final String ATTR_CHAR_EXPR         = TestFixConst.ATTRIBUTE_CHARACTER+"Expression";

  public static final String ATTR_CLOB1             = TestFixConst.ATTRIBUTE_CLOB+"1";
  public static final String ATTR_CLOB2             = TestFixConst.ATTRIBUTE_CLOB+"2";
  public static final String ATTR_CLOB_EXPR         = TestFixConst.ATTRIBUTE_CLOB+"Expression";
    
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

  /**
   * Launch-point for the standalone textui JUnit tests in this class.
   * 
   * @param args
   */
  public static void main(String[] args)
  {
    if (DatabaseProperties.getDatabaseClass().equals("hsqldb"))
    {
      XMLImporter.main(new String[] { TestConstants.Path.schema_xsd, TestConstants.Path.metadata_xml });
    }
    junit.textui.TestRunner.run(EntityGenTest.suite());
  }
  
  private static MdBusinessDAO                mdBusinessDAO;
  private static String                       CLASS_TYPE; 
  private static String                       ATTR_CHAR_EXPR_KEY;
  private static String                       ATTR_CLOB_EXPR_KEY;
  private static String                       ATTR_BOOL_EXPR_KEY;
  private static String                       ATTR_INT_EXPR_KEY;
  private static String                       ATTR_LONG_EXPR_KEY;
  private static String                       ATTR_FLOAT_EXPR_KEY;
  private static String                       ATTR_DOUBLE_EXPR_KEY;
  private static String                       ATTR_DECIMAL_EXPR_KEY;
  private static String                       ATTR_DATETIME_EXPR_KEY;
  private static String                       ATTR_DATE_EXPR_KEY;
  private static String                       ATTR_TIME_EXPR_KEY;
  
  /**
   * A suite() takes <b>this </b> <code>EntityAttributeTest.class</code> and
   * wraps it in <code>MasterTestSetup</code>. The returned class is a suite of
   * all the tests in <code>AttributeTest</code>, with the global setUp() and
   * tearDown() methods from <code>MasterTestSetup</code>.
   * 
   * @return A suite of tests wrapped in global setUp and tearDown methods
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ExpressionAttributeTest.class);

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
    mdBusinessDAO = TestFixtureFactory.createMdBusiness1();
    mdBusinessDAO.apply();
    CLASS_TYPE = mdBusinessDAO.getKey();

    MdAttributeCharacterDAO mdAttributeCharacterDAO1 = TestFixtureFactory.addCharacterAttribute(mdBusinessDAO, ATTR_CHAR1);
    mdAttributeCharacterDAO1.apply();
    
    MdAttributeCharacterDAO mdAttributeCharacterDAO2 = TestFixtureFactory.addCharacterAttribute(mdBusinessDAO, ATTR_CHAR2);
    mdAttributeCharacterDAO2.apply();
    
    MdAttributeCharacterDAO mdAttributeCharacterDAOexpr = TestFixtureFactory.addCharacterAttribute(mdBusinessDAO, ATTR_CHAR_EXPR);
    mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacterDAOexpr.apply();
    ATTR_CHAR_EXPR_KEY = mdAttributeCharacterDAOexpr.getKey();
    
    MdAttributeBooleanDAO mdAttributeBooleanDAO1 = TestFixtureFactory.addBooleanAttribute(mdBusinessDAO, ATTR_BOOL1);
    mdAttributeBooleanDAO1.apply();
    
    MdAttributeBooleanDAO mdAttributeBooleanDAO2 = TestFixtureFactory.addBooleanAttribute(mdBusinessDAO, ATTR_BOOL2);
    mdAttributeBooleanDAO2.apply();
    
    MdAttributeBooleanDAO mdAttributeBooleanDAOexpr = TestFixtureFactory.addBooleanAttribute(mdBusinessDAO, ATTR_BOOL_EXPR);
    mdAttributeBooleanDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
    mdAttributeBooleanDAOexpr.apply();
    ATTR_BOOL_EXPR_KEY = mdAttributeBooleanDAOexpr.getKey();
    
    MdAttributeIntegerDAO mdAttributeIntDAOexpr = TestFixtureFactory.addIntegerAttribute(mdBusinessDAO, ATTR_BOOL_EXPR);
    mdAttributeIntDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
    mdAttributeIntDAOexpr.apply();
    ATTR_INT_EXPR_KEY = mdAttributeIntDAOexpr.getKey();
    
    
    
    
  }
  
  public static void classTearDown()
  {
    mdBusinessDAO = MdBusinessDAO.getMdBusinessDAO(mdBusinessDAO.definesType()).getBusinessDAO();    
    mdBusinessDAO.delete();
  }

  public void testExpressionAttributeRequiresExpression()
  {
    MdAttributeCharacterDAO mdAttributeCharacterDAOexpr = (MdAttributeCharacterDAO)MdAttributeCharacterDAO.getByKey(ATTR_CHAR_EXPR_KEY).getBusinessDAO();
    
    try
    {
      mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
      mdAttributeCharacterDAOexpr.apply();
           
      fail(MdAttributePrimitiveInfo.CLASS + " Is set to true, net no expression was defined. An exception should have been thrown.");
    }
    catch (ProblemException e)
    {
      List<ProblemIF> problemList = e.getProblems();
      
      assertEquals(problemList.size(), 1);
      
      ProblemIF problemIF = problemList.get(0);
      
      assert(problemIF instanceof EmptyValueProblem);
    }
    finally
    {
      mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeCharacterDAOexpr.apply();
    }
  }
  
  public void testInvalidSyntax()
  {
    MdAttributeCharacterDAO mdAttributeCharacterDAOexpr = (MdAttributeCharacterDAO)MdAttributeCharacterDAO.getByKey(ATTR_CHAR_EXPR_KEY).getBusinessDAO();
    
    try
    {
      mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
      mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("Some Invalid Expression");
      mdAttributeCharacterDAOexpr.apply();
           
      fail(InvalidExpressionSyntaxException.class.getName()+" was not thrown when an invalid expression syntax was defined.");
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
  
  public void testValidCharExpression()
  {
    MdAttributeCharacterDAO mdAttributeCharacterDAOexpr = (MdAttributeCharacterDAO)MdAttributeCharacterDAO.getByKey(ATTR_CHAR_EXPR_KEY).getBusinessDAO();
    mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue(ATTR_CHAR1+"+\" \"+"+ATTR_CHAR2);
//    mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue(ATTR_CHAR1+"-"+ATTR_CHAR2);
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
      
      assertEquals(expressionResult, "Optimus Prime");
    }
    finally
    {
      business.delete();
      
      mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeCharacterDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("");
      mdAttributeCharacterDAOexpr.apply();
    }
  }
  
  public void testValidBooleanExpression()
  {
    MdAttributeBooleanDAO mdAttributeBooleanDAOexpr = (MdAttributeBooleanDAO)MdAttributeBooleanDAO.getByKey(ATTR_BOOL_EXPR_KEY).getBusinessDAO();
    mdAttributeBooleanDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.TRUE);
    mdAttributeBooleanDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue(ATTR_BOOL1+"&"+ATTR_BOOL2);
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
      
      assertEquals(expressionResult, MdAttributeBooleanInfo.FALSE);
    }
    finally
    {
      business.delete();
      
      mdAttributeBooleanDAOexpr.getAttribute(MdAttributePrimitiveInfo.IS_EXPRESSION).setValue(MdAttributeBooleanInfo.FALSE);
      mdAttributeBooleanDAOexpr.getAttribute(MdAttributePrimitiveInfo.EXPRESSION).setValue("");
      mdAttributeBooleanDAOexpr.apply();
    }
  }
 
}
