package com.runwaysdk.dataaccess;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.constants.DatabaseProperties;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeDecimalInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeFloatInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeLongInfo;
import com.runwaysdk.constants.MdAttributeRatioInfo;
import com.runwaysdk.constants.RatioInfo;
import com.runwaysdk.constants.RatioPrimitiveInfo;
import com.runwaysdk.constants.TestConstants;
import com.runwaysdk.dataaccess.io.XMLImporter;
import com.runwaysdk.dataaccess.metadata.MdAttributeDecimalDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeFloatDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLongDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeRatioDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.system.RatioOperator;

public class EntityAttributeRatioTest extends TestCase
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
  
  private static final String                         TEST_INTEGER_RATIO          = "testIntegerRatio";
  
  private static final String                         TEST_LONG_1                 = "testLong1";

  private static final String                         TEST_LONG_2                 = "testLong2";
  
  private static final String                         TEST_FLOAT_1                = "testFloat1";

  private static final String                         TEST_FLOAT_2                = "testFloat2";
  
  private static final String                         TEST_DOUBLE_1               = "testDouble1";

  private static final String                         TEST_DOUBLE_2               = "testDouble2";
  
  private static final String                         TEST_DECIMAL_1              = "testDecimal1";

  private static final String                         TEST_DECIMAL_2              = "testDecimal2";
  
  /**
   * The launch point for the Junit tests.
   * 
   * @param args
   */
  public static void main(String[] args)
  {

    if (DatabaseProperties.getDatabaseClass().equals("hsqldb"))
      XMLImporter.main(new String[] { TestConstants.Path.schema_xsd, TestConstants.Path.metadata_xml });

    junit.textui.TestRunner.run(new EntityMasterTestSetup(EntityAttributeRatioTest.suite()));

  }
  
  /**
   * A suite() takes <b>this </b> <code>EntityAttributeRatio.class</code> and
   * wraps it in <code>MasterTestSetup</code>. The returned class is a suite of
   * all the tests in <code>AttributeTest</code>, with the global setUp() and
   * tearDown() methods from <code>MasterTestSetup</code>.
   * 
   * @return A suite of tests wrapped in global setUp and tearDown methods
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(EntityAttributeRatioTest.class);

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
    
    RatioPrimitiveDAO ratioPrimitiveInteger1 = RatioPrimitiveDAO.newInstance();
    ratioPrimitiveInteger1.setValue(RatioPrimitiveInfo.MD_ATTRIBUTE_PRIMITIVE, mdAttributeInteger1.getId());
    ratioPrimitiveInteger1.apply();
    
    RatioPrimitiveDAO ratioPrimitiveInteger2 = RatioPrimitiveDAO.newInstance();
    ratioPrimitiveInteger2.setValue(RatioPrimitiveInfo.MD_ATTRIBUTE_PRIMITIVE, mdAttributeInteger2.getId());
    ratioPrimitiveInteger2.apply();
    
    RatioDAO ratioInteger = RatioDAO.newInstance();
    ratioInteger.setValue(RatioInfo.LEFT_OPERAND, ratioPrimitiveInteger1.getId());
    ratioInteger.setValue(RatioInfo.OPERATOR, RatioOperator.DIV.getId());
    ratioInteger.setValue(RatioInfo.RIGHT_OPERAND, ratioPrimitiveInteger2.getId());
    ratioInteger.apply();
    
    MdAttributeRatioDAO mdAttributeRatio1 = MdAttributeRatioDAO.newInstance();
    mdAttributeRatio1.setValue(MdAttributeRatioInfo.NAME, TEST_INTEGER_RATIO);
    mdAttributeRatio1.setStructValue(MdAttributeRatioInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Integer Ratio");
    mdAttributeRatio1.setValue(MdAttributeRatioInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeRatio1.setValue(MdAttributeRatioInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeRatio1.setValue(MdAttributeRatioInfo.RATIO, ratioInteger.getId());
    mdAttributeRatio1.setValue(MdAttributeRatioInfo.DEFINING_MD_CLASS, testMdBusinessIF.getId());
    mdAttributeRatio1.apply();
    
    BusinessDAO bus1 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    bus1.setValue(TEST_INTEGER_1, "4");
    bus1.setValue(TEST_INTEGER_2, "2");
    bus1.apply();
    
    
  }
  
  public static void classTearDown()
  {
//    BusinessDAO temp = BusinessDAO.get(testMdBusinessIF.getId()).getBusinessDAO();
//    temp.delete();
  }
  
  public void testTest()
  {
    assertTrue(true);
  }
}
