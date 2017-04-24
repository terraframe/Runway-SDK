package com.runwaysdk.dataaccess;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdTableDAO;

public class MdTableTestSuite extends TestCase
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
  
  /**
   * 
   * @return
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(MdTableTestSuite.class);

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
  
  /**
   */
  public static void classSetUp()
  { 
    MdBusinessDAO mdBusiness1 = TestFixtureFactory.createMdBusiness1();
    mdBusiness1.setGenerateMdController(false);
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
