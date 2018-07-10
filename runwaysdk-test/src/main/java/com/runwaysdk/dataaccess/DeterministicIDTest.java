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
/**
 *
 */
package com.runwaysdk.dataaccess;

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdEntityInfo;
import com.runwaysdk.constants.TypeInfo;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

/**
 * @author Eric G
 *
 */
public class DeterministicIDTest extends TestCase
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
   * The name of the new class.
   */
  private static final TypeInfo NEW_CLASS = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "NewClass");

  private static final String CHAR_ATTR = "characterAttribute";
  
  /**
   * Launch-point for the standalone textui JUnit tests in this class.
   * @param args
   */
  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(new EntityMasterTestSetup(DeterministicIDTest.suite()));
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(DeterministicIDTest.class);

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
   * Set the testObject to a new Instance of the TEST type.
   */
  protected static void classSetUp()
  {
    MdBusinessDAO mdBusiness = MdBusinessDAO.newInstance();
    mdBusiness.setValue(MdBusinessInfo.NAME,                   NEW_CLASS.getTypeName());
    mdBusiness.setValue(MdBusinessInfo.PACKAGE,                NEW_CLASS.getPackageName());
    mdBusiness.setValue(MdBusinessInfo.REMOVE,                 MdAttributeBooleanInfo.TRUE);
    mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL,    MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class");
    mdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION,      MdAttributeLocalInfo.DEFAULT_LOCALE,      "Temporary JUnit Test Class");
    mdBusiness.setValue(MdBusinessInfo.EXTENDABLE,             MdAttributeBooleanInfo.TRUE);
    mdBusiness.setValue(MdBusinessInfo.ABSTRACT,               MdAttributeBooleanInfo.FALSE);
    mdBusiness.setValue(MdBusinessInfo.CACHE_ALGORITHM,        EntityCacheMaster.CACHE_EVERYTHING.getId());
    mdBusiness.setValue(MdEntityInfo.HAS_DETERMINISTIC_IDS,    MdAttributeBooleanInfo.FALSE);
    mdBusiness.setGenerateMdController(false);
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();
    
    MdAttributeCharacterDAO mdAttChar = MdAttributeCharacterDAO.newInstance();
    mdAttChar.setValue( MdAttributeCharacterInfo.NAME, CHAR_ATTR);
    mdAttChar.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Some CharacterAttribute");
    mdAttChar.setValue( MdAttributeCharacterInfo.SIZE, "32");
    mdAttChar.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttChar.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdBusiness.getId());
    mdAttChar.apply();

  }


  /**
   * If testObject was applied, it is removed from the database.
   *
   * @see TestCase#tearDown()
   */
  protected static void classTearDown()
  {
    MdBusinessDAOIF newMdBusiness = MdBusinessDAO.getMdBusinessDAO(NEW_CLASS.getType());

    if (!newMdBusiness.isNew())
    {
      newMdBusiness.getBusinessDAO().delete();
    }
  }

  public void testMissingKeyNameValueException()
  {
    MdBusinessDAO mdBusiness = MdBusinessDAO.getMdBusinessDAO(NEW_CLASS.getType()).getBusinessDAO();
    mdBusiness.setValue(MdEntityInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();
    
    // Test that they key name is populated by the ID
    BusinessDAO businessDAO1 = BusinessDAO.newInstance(NEW_CLASS.getType());
    businessDAO1.apply();
    
    assertEquals("ID and KeyName values were not equal on a type without deterministic IDs.", businessDAO1.getId(), businessDAO1.getKey());
    
    // Enable Deterministic IDs
    mdBusiness.setValue(MdEntityInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.TRUE);
    mdBusiness.apply();
        
    try
    {
      // Test to ensure the proper exception is thrown when no key is supplied
      BusinessDAO businessDAO2 = BusinessDAO.newInstance(NEW_CLASS.getType());
      businessDAO2.apply(); 
      
      // Should never get here, but delete the object in case we do
      businessDAO2.delete();
    }
    catch (MissingKeyNameValue e)
    {
      // this is expected
    }
    finally
    {
      businessDAO1.delete();
      
      // Disable Deterministic IDs
      mdBusiness.setValue(MdEntityInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.FALSE);
      mdBusiness.apply();
    }
    
  }

  public void testDuplicateKeysAndIDs()
  {
    MdBusinessDAO mdBusiness = MdBusinessDAO.getMdBusinessDAO(NEW_CLASS.getType()).getBusinessDAO();
    mdBusiness.setValue(MdEntityInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.TRUE);
    mdBusiness.apply();
   
    try
    {
    
      BusinessDAO businessDAO1 = BusinessDAO.newInstance(NEW_CLASS.getType());
      businessDAO1.setValue(EntityInfo.KEY, "TestKeyOne");
      businessDAO1.apply();
    
      String busId1 = businessDAO1.getId();
      businessDAO1.delete();
    
      BusinessDAO businessDAO2 = BusinessDAO.newInstance(NEW_CLASS.getType());
      businessDAO2.setValue(EntityInfo.KEY, "TestKeyOne");
      businessDAO2.apply();
      businessDAO2.delete();
    
      String busId2 = businessDAO2.getId();
    
      assertEquals("IDs from two different objects should have deterministically been the same", busId1, busId2);
    }
    finally
    {     
      // Disable Deterministic IDs
      mdBusiness.setValue(MdEntityInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.FALSE);
      mdBusiness.apply();
    }
  }
  
  public void testChaingingIDsDifferentTransactions()
  {
    MdBusinessDAO mdBusiness = MdBusinessDAO.getMdBusinessDAO(NEW_CLASS.getType()).getBusinessDAO();
    mdBusiness.setValue(MdEntityInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.TRUE);
    mdBusiness.apply();
   
    BusinessDAO businessDAO1 = null;
    BusinessDAO businessDAO2 = null;
    
    try
    {
      businessDAO1 = BusinessDAO.newInstance(NEW_CLASS.getType());
      businessDAO1.setValue(EntityInfo.KEY, "TestKeyOne");
      businessDAO1.setValue(CHAR_ATTR, "Obj1");
      businessDAO1.apply();
      String obj1TestKeyOneId = businessDAO1.getId();
      
      businessDAO1.setValue(EntityInfo.KEY, "TestKeyTwo");
      businessDAO1.apply();
      String obj1TestKeyTwoId = businessDAO1.getId();
      
      assertTrue("An ID for an object did not change when the key name value changed.", !obj1TestKeyOneId.equals(obj1TestKeyTwoId));
      
      businessDAO2 = BusinessDAO.newInstance(NEW_CLASS.getType());
      businessDAO2.setValue(EntityInfo.KEY, "TestKeyOne");
      businessDAO2.setValue(CHAR_ATTR, "Obj2");
      businessDAO2.apply();
      String obj2TestKeyOneId = businessDAO2.getId();
      
      assertTrue("The same ID was not generated for two different objects that have the same key.", obj1TestKeyOneId.equals(obj2TestKeyOneId)); 
      
      BusinessDAOIF businessDAOIF1 = BusinessDAO.get(obj1TestKeyTwoId);
      BusinessDAOIF businessDAOIF2 = BusinessDAO.get(obj2TestKeyOneId);
      
      assertEquals("After an ID change, the wrong object was fetched.", "Obj1", businessDAOIF1.getValue(CHAR_ATTR));
      assertEquals("After an ID change, the wrong object was fetched.", "Obj2", businessDAOIF2.getValue(CHAR_ATTR));      
      
    }
    finally
    {     
      if (businessDAO1 != null && !businessDAO1.isNew())
      {
        businessDAO1.delete();
      }
      
      if (businessDAO2 != null && !businessDAO2.isNew())
      {
        businessDAO2.delete();
      }

      // Disable Deterministic IDs
      mdBusiness.setValue(MdEntityInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.FALSE);
      mdBusiness.apply();
    }
  }

  public void testKeyAndIdCollisionInCache()
  {
    MdBusinessDAO mdBusiness = MdBusinessDAO.getMdBusinessDAO(NEW_CLASS.getType()).getBusinessDAO();
    mdBusiness.setValue(MdEntityInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.TRUE);
    mdBusiness.apply();
    
    List<BusinessDAO> returnList = null;
    
    try
    {     
      returnList = keyAndIdCollisionInCache();
    }
    finally
    {        
      
      if (returnList != null)
      {
        for (BusinessDAO businessDAO : returnList)
        {
          if (businessDAO != null && !businessDAO.isNew())
          {
            businessDAO.delete();
          }
        }
      }
      
      // Disable Deterministic IDs
      mdBusiness.setValue(MdEntityInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.FALSE);
      mdBusiness.apply();
    }
  }
  
  @Transaction
  private List<BusinessDAO> keyAndIdCollisionInCache()
  {
    BusinessDAO businessDAO1 = null;
    BusinessDAO businessDAO2 = null;
  
    List<BusinessDAO> returnList = new LinkedList<BusinessDAO>();

    businessDAO1 = BusinessDAO.newInstance(NEW_CLASS.getType());
    businessDAO1.setValue(EntityInfo.KEY, "TestKeyOne");
    businessDAO1.setValue(CHAR_ATTR, "Obj1");
    businessDAO1.apply();
    String obj1TestKeyOneId = businessDAO1.getId();
    returnList.add(businessDAO1);
      
    businessDAO1.setValue(EntityInfo.KEY, "TestKeyTwo");
    businessDAO1.apply();
    String obj1TestKeyTwoId = businessDAO1.getId();
    
    assertTrue("An ID for an object did not change when the key name value changed.", !obj1TestKeyOneId.equals(obj1TestKeyTwoId));
    
    businessDAO2 = BusinessDAO.newInstance(NEW_CLASS.getType());
    businessDAO2.setValue(EntityInfo.KEY, "TestKeyOne");
    businessDAO2.setValue(CHAR_ATTR, "Obj2");
    businessDAO2.apply();
    String obj2TestKeyOneId = businessDAO2.getId();
    returnList.add(businessDAO2);
      
    assertTrue("The same ID was not generated for two different objects that have the same key.", obj1TestKeyOneId.equals(obj2TestKeyOneId)); 
    
    BusinessDAOIF businessDAOIF1 = BusinessDAO.get(obj1TestKeyTwoId);
    BusinessDAOIF businessDAOIF2 = BusinessDAO.get(obj2TestKeyOneId);
    
    assertEquals("After an ID change, the wrong object was fetched.", "Obj1", businessDAOIF1.getValue(CHAR_ATTR));
    assertEquals("After an ID change, the wrong object was fetched.", "Obj2", businessDAOIF2.getValue(CHAR_ATTR));    
    
    return returnList;
  }
  
}
