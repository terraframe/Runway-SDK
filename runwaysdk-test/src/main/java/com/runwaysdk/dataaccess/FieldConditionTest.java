/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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
 ******************************************************************************/
package com.runwaysdk.dataaccess;

import java.util.List;
import java.util.Set;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.constants.AndFieldConditionInfo;
import com.runwaysdk.constants.CharacterConditionInfo;
import com.runwaysdk.constants.DateConditionInfo;
import com.runwaysdk.constants.DoubleConditionInfo;
import com.runwaysdk.constants.LongConditionInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdWebAttributeInfo;
import com.runwaysdk.constants.MdWebDateInfo;
import com.runwaysdk.constants.MdWebIntegerInfo;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.AndFieldConditionDAO;
import com.runwaysdk.dataaccess.metadata.CharacterConditionDAO;
import com.runwaysdk.dataaccess.metadata.DateConditionDAO;
import com.runwaysdk.dataaccess.metadata.DoubleConditionDAO;
import com.runwaysdk.dataaccess.metadata.LongConditionDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdWebCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdWebDateDAO;
import com.runwaysdk.dataaccess.metadata.MdWebDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFormDAO;
import com.runwaysdk.dataaccess.metadata.MdWebGroupDAO;
import com.runwaysdk.dataaccess.metadata.MdWebIntegerDAO;
import com.runwaysdk.system.FieldOperation;
import com.runwaysdk.system.metadata.FieldConditionDAO;

public class FieldConditionTest extends TestCase
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

  private static MdBusinessDAO           mdBusiness;

  private static MdAttributeCharacterDAO mdAttributeCharacter;

  private static MdAttributeDoubleDAO    mdAttributeDouble;

  private static MdAttributeDateDAO      mdAttributeDate;

  private static MdAttributeIntegerDAO   mdAttributeInteger;

  private static MdWebFormDAO            mdForm;

  private static MdWebCharacterDAO       mdWebCharacter;

  private static MdWebDoubleDAO          mdWebDouble;

  private static MdWebDateDAO            mdWebDate;

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(FieldConditionTest.class);

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
    mdBusiness.apply();

    mdAttributeCharacter = TestFixtureFactory.addCharacterAttribute(mdBusiness);
    mdAttributeCharacter.apply();

    mdAttributeDouble = TestFixtureFactory.addDoubleAttribute(mdBusiness);
    mdAttributeDouble.apply();

    mdAttributeDate = TestFixtureFactory.addDateAttribute(mdBusiness);
    mdAttributeDate.apply();

    mdAttributeInteger = TestFixtureFactory.addIntegerAttribute(mdBusiness);
    mdAttributeInteger.apply();

    mdForm = TestFixtureFactory.createMdWebForm(mdBusiness);
    mdForm.apply();

    mdWebCharacter = TestFixtureFactory.addCharacterField(mdForm, mdAttributeCharacter);
    mdWebCharacter.setValue(MdWebAttributeInfo.SHOW_ON_SEARCH, MdAttributeBooleanInfo.FALSE);
    mdWebCharacter.apply();

    mdWebDouble = TestFixtureFactory.addDoubleField(mdForm, mdAttributeDouble);
    mdWebDouble.apply();

    mdWebDate = TestFixtureFactory.addDateField(mdForm, mdAttributeDate);
    mdWebDate.apply();
  }

  public static void classTearDown()
  {
    TestFixtureFactory.delete(mdForm);
    TestFixtureFactory.delete(mdBusiness);
  }

  public void testDateCondition()
  {
    assertEquals(MdAttributeBooleanInfo.FALSE, mdWebDate.getValue(MdWebDateInfo.SHOW_ON_VIEW_ALL));
    assertEquals(MdAttributeBooleanInfo.TRUE, mdWebDate.getValue(MdWebDateInfo.SHOW_ON_SEARCH));
    assertEquals(MdAttributeBooleanInfo.FALSE, mdWebCharacter.getValue(MdWebDateInfo.SHOW_ON_SEARCH));

    EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "GTE");

    DateConditionDAO condition = DateConditionDAO.newInstance();
    condition.setValue(DateConditionInfo.VALUE, "2006-11-11");
    condition.setValue(DateConditionInfo.DEFINING_MD_FIELD, mdWebDate.getId());
    condition.addItem(DateConditionInfo.OPERATION, item.getId());
    condition.apply();

    try
    {
      DateConditionDAOIF test = DateConditionDAO.get(condition.getId());

      AttributeEnumerationIF attribute = (AttributeEnumerationIF) test.getAttributeIF(DoubleConditionInfo.OPERATION);
      Set<String> itemIds = attribute.getEnumItemIdList();

      assertEquals(DateConditionInfo.CLASS, test.getType());
      assertEquals(condition.getValue(DoubleConditionInfo.VALUE), test.getValue(DoubleConditionInfo.VALUE));
      assertEquals(condition.getValue(DoubleConditionInfo.DEFINING_MD_FIELD), test.getValue(DoubleConditionInfo.DEFINING_MD_FIELD));
      assertEquals(1, itemIds.size());

      for (String itemId : itemIds)
      {
        assertEquals("GTE", EnumerationItemDAO.get(itemId).getName());
      }
    }
    finally
    {
      condition.delete();
    }
  }

  public void testDoubleCondition()
  {
    EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "LT");

    DoubleConditionDAO condition = DoubleConditionDAO.newInstance();
    condition.setValue(DoubleConditionInfo.VALUE, "10.0000000000");
    condition.setValue(DoubleConditionInfo.DEFINING_MD_FIELD, mdWebDouble.getId());
    condition.addItem(DoubleConditionInfo.OPERATION, item.getId());
    condition.apply();

    try
    {
      DoubleConditionDAOIF test = DoubleConditionDAO.get(condition.getId());

      AttributeEnumerationIF attribute = (AttributeEnumerationIF) test.getAttributeIF(DoubleConditionInfo.OPERATION);
      Set<String> itemIds = attribute.getEnumItemIdList();

      assertEquals(DoubleConditionInfo.CLASS, test.getType());
      assertEquals(condition.getValue(DoubleConditionInfo.VALUE), test.getValue(DoubleConditionInfo.VALUE));
      assertEquals(condition.getValue(DoubleConditionInfo.DEFINING_MD_FIELD), test.getValue(DoubleConditionInfo.DEFINING_MD_FIELD));
      assertEquals(1, itemIds.size());

      for (String itemId : itemIds)
      {
        assertEquals("LT", EnumerationItemDAO.get(itemId).getName());
      }
    }
    finally
    {
      condition.delete();
    }
  }

  public void testLongCondition()
  {
    EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "LT");

    LongConditionDAO condition = LongConditionDAO.newInstance();
    condition.setValue(LongConditionInfo.VALUE, "10");
    condition.setValue(LongConditionInfo.DEFINING_MD_FIELD, mdWebDouble.getId());
    condition.addItem(LongConditionInfo.OPERATION, item.getId());
    condition.apply();

    try
    {
      LongConditionDAOIF test = LongConditionDAO.get(condition.getId());

      AttributeEnumerationIF attribute = (AttributeEnumerationIF) test.getAttributeIF(LongConditionInfo.OPERATION);
      Set<String> itemIds = attribute.getEnumItemIdList();

      assertEquals(LongConditionInfo.CLASS, test.getType());
      assertEquals(condition.getValue(LongConditionInfo.VALUE), test.getValue(LongConditionInfo.VALUE));
      assertEquals(condition.getValue(LongConditionInfo.DEFINING_MD_FIELD), test.getValue(LongConditionInfo.DEFINING_MD_FIELD));
      assertEquals(1, itemIds.size());

      for (String itemId : itemIds)
      {
        assertEquals("LT", EnumerationItemDAO.get(itemId).getName());
      }
    }
    finally
    {
      condition.delete();
    }
  }

  public void testCharacterCondition()
  {
    EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "EQ");

    CharacterConditionDAO condition = CharacterConditionDAO.newInstance();
    condition.setValue(CharacterConditionInfo.VALUE, "testValue");
    condition.setValue(CharacterConditionInfo.DEFINING_MD_FIELD, mdWebCharacter.getId());
    condition.addItem(CharacterConditionInfo.OPERATION, item.getId());
    condition.apply();

    try
    {
      CharacterConditionDAOIF test = CharacterConditionDAO.get(condition.getId());

      AttributeEnumerationIF attribute = (AttributeEnumerationIF) test.getAttributeIF(DoubleConditionInfo.OPERATION);
      Set<String> itemIds = attribute.getEnumItemIdList();

      assertEquals(CharacterConditionInfo.CLASS, test.getType());
      assertEquals(condition.getValue(DoubleConditionInfo.VALUE), test.getValue(DoubleConditionInfo.VALUE));
      assertEquals(condition.getValue(DoubleConditionInfo.DEFINING_MD_FIELD), test.getValue(DoubleConditionInfo.DEFINING_MD_FIELD));
      assertEquals(1, itemIds.size());

      for (String itemId : itemIds)
      {
        assertEquals("EQ", EnumerationItemDAO.get(itemId).getName());
      }
    }
    finally
    {
      TestFixtureFactory.delete(condition);
    }
  }

  public void testAndCondition()
  {
    EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "LT");

    DoubleConditionDAO firstCondition = DoubleConditionDAO.newInstance();
    firstCondition.setValue(DoubleConditionInfo.VALUE, "10.0000000000");
    firstCondition.setValue(DoubleConditionInfo.DEFINING_MD_FIELD, mdWebDouble.getId());
    firstCondition.addItem(DoubleConditionInfo.OPERATION, item.getId());
    firstCondition.apply();

    try
    {
      EnumerationItemDAO secondItem = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "GT");

      DoubleConditionDAO secondCondition = DoubleConditionDAO.newInstance();
      secondCondition.setValue(DoubleConditionInfo.VALUE, "135.0000000000");
      secondCondition.setValue(DoubleConditionInfo.DEFINING_MD_FIELD, mdWebDouble.getId());
      secondCondition.addItem(DoubleConditionInfo.OPERATION, secondItem.getId());
      secondCondition.apply();

      try
      {

        AndFieldConditionDAO condition = AndFieldConditionDAO.newInstance();
        condition.setValue(AndFieldConditionInfo.FIRST_CONDITION, firstCondition.getId());
        condition.setValue(AndFieldConditionInfo.SECOND_CONDITION, secondCondition.getId());
        condition.apply();

        try
        {
          AndFieldConditionDAOIF test = AndFieldConditionDAO.get(condition.getId());

          assertEquals(AndFieldConditionInfo.CLASS, test.getType());
          assertEquals(condition.getValue(AndFieldConditionInfo.FIRST_CONDITION), test.getValue(AndFieldConditionInfo.FIRST_CONDITION));
          assertEquals(condition.getValue(AndFieldConditionInfo.SECOND_CONDITION), test.getValue(AndFieldConditionInfo.SECOND_CONDITION));
        }
        finally
        {
          TestFixtureFactory.delete(condition);
        }
      }
      finally
      {
        TestFixtureFactory.delete(secondCondition);
      }
    }
    finally
    {
      TestFixtureFactory.delete(firstCondition);
    }
  }

  public void testFieldDeleteWithCondition()
  {
    EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "LT");

    DoubleConditionDAO condition = DoubleConditionDAO.newInstance();
    condition.setValue(DoubleConditionInfo.VALUE, "10.0000000000");
    condition.setValue(DoubleConditionInfo.DEFINING_MD_FIELD, mdWebDouble.getId());
    condition.addItem(DoubleConditionInfo.OPERATION, item.getId());
    condition.apply();

    MdWebIntegerDAO field = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
    field.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
    field.apply();

    TestFixtureFactory.delete(field);

    try
    {
      FieldConditionDAOIF deletedCondition = FieldConditionDAO.get(condition.getId());

      TestFixtureFactory.delete(deletedCondition);

      fail("The field condition was not deleted when the field was deleted");
    }
    catch (DataNotFoundException e)
    {
      // This is expected
    }
  }

  public void testGetCondition()
  {
    EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "LT");

    DoubleConditionDAO condition = DoubleConditionDAO.newInstance();
    condition.setValue(DoubleConditionInfo.VALUE, "10.0000000000");
    condition.setValue(DoubleConditionInfo.DEFINING_MD_FIELD, mdWebDouble.getId());
    condition.addItem(DoubleConditionInfo.OPERATION, item.getId());
    condition.apply();

    MdWebIntegerDAO field = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
    field.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
    field.apply();

    try
    {
      List<FieldConditionDAOIF> conditions = field.getConditions();

      assertEquals(1, conditions.size());
      assertEquals(condition.getId(), conditions.get(0).getId());
    }
    finally
    {
      TestFixtureFactory.delete(field);
    }
  }

  public void testConditionGrouping()
  {
    MdWebIntegerDAO field = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
    field.apply();

    try
    {
      EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "LT");

      DoubleConditionDAO condition = DoubleConditionDAO.newInstance();
      condition.setValue(DoubleConditionInfo.VALUE, "10.0000000000");
      condition.setValue(DoubleConditionInfo.DEFINING_MD_FIELD, mdWebDouble.getId());
      condition.addItem(DoubleConditionInfo.OPERATION, item.getId());
      condition.apply();

      MdWebGroupDAO group = TestFixtureFactory.addGroupField(mdForm);
      group.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getId());
      group.apply();

      try
      {
        TreeDAO relationship = group.addField(field);
        relationship.apply();

        try
        {
          List<FieldConditionDAOIF> conditions = field.getConditions();

          assertEquals(1, conditions.size());
          assertEquals(condition.getId(), conditions.get(0).getId());
        }
        finally
        {
          TestFixtureFactory.delete(relationship);
        }
      }
      finally
      {
        TestFixtureFactory.delete(group);
      }
    }
    finally
    {
      TestFixtureFactory.delete(field);
    }
  }

  public void testMultipleConditionGrouping()
  {
    MdWebIntegerDAO field = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
    field.apply();

    try
    {
      EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "LT");

      DoubleConditionDAO childCondition = DoubleConditionDAO.newInstance();
      childCondition.setValue(DoubleConditionInfo.VALUE, "10.0000000000");
      childCondition.setValue(DoubleConditionInfo.DEFINING_MD_FIELD, mdWebDouble.getId());
      childCondition.addItem(DoubleConditionInfo.OPERATION, item.getId());
      childCondition.apply();

      MdWebGroupDAO childGroup = TestFixtureFactory.addGroupField(mdForm);
      childGroup.setValue(MdWebIntegerInfo.FIELD_CONDITION, childCondition.getId());
      childGroup.apply();

      try
      {
        TreeDAO childRelationship = childGroup.addField(field);
        childRelationship.apply();

        try
        {
          DoubleConditionDAO parentCondition = DoubleConditionDAO.newInstance();
          parentCondition.setValue(DoubleConditionInfo.VALUE, "50");
          parentCondition.setValue(DoubleConditionInfo.DEFINING_MD_FIELD, mdWebDouble.getId());
          parentCondition.addItem(DoubleConditionInfo.OPERATION, EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "LT").getId());
          parentCondition.apply();

          MdWebGroupDAO parentGroup = TestFixtureFactory.addGroupField(mdForm);
          parentGroup.setValue(MdWebIntegerInfo.FIELD_CONDITION, parentCondition.getId());
          parentGroup.setValue(MdWebIntegerInfo.FIELD_NAME, "parentGroup");
          parentGroup.apply();

          try
          {
            TreeDAO relationship = parentGroup.addField(childGroup);
            relationship.apply();

            try
            {
              List<FieldConditionDAOIF> conditions = field.getConditions();

              assertEquals(2, conditions.size());
              assertTrue(conditions.contains(parentCondition));
              assertTrue(conditions.contains(childCondition));
            }
            finally
            {
              TestFixtureFactory.delete(relationship);
            }
          }
          finally
          {
            TestFixtureFactory.delete(parentGroup);
          }
        }
        finally
        {
          TestFixtureFactory.delete(childRelationship);
        }
      }
      finally
      {
        TestFixtureFactory.delete(childGroup);
      }
    }
    finally
    {
      TestFixtureFactory.delete(field);
    }
  }
}
