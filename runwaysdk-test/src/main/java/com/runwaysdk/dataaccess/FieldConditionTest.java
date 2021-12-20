/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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

import java.util.List;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.constants.AndFieldConditionInfo;
import com.runwaysdk.constants.CharacterConditionInfo;
import com.runwaysdk.constants.DateConditionInfo;
import com.runwaysdk.constants.DoubleConditionInfo;
import com.runwaysdk.constants.LongConditionInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdBusinessInfo;
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
import com.runwaysdk.session.Request;
import com.runwaysdk.system.FieldOperation;
import com.runwaysdk.system.metadata.FieldConditionDAO;

public class FieldConditionTest
{
  private static MdBusinessDAO           mdBusiness;

  private static MdAttributeCharacterDAO mdAttributeCharacter;

  private static MdAttributeDoubleDAO    mdAttributeDouble;

  private static MdAttributeDateDAO      mdAttributeDate;

  private static MdAttributeIntegerDAO   mdAttributeInteger;

  private static MdWebFormDAO            mdForm;

  private static MdWebCharacterDAO       mdWebCharacter;

  private static MdWebDoubleDAO          mdWebDouble;

  private static MdWebDateDAO            mdWebDate;

  @Request
  @BeforeClass
  public static void classSetUp()
  {
    mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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

  @Request
  @AfterClass
  public static void classTearDown()
  {
    TestFixtureFactory.delete(mdForm);
    TestFixtureFactory.delete(mdBusiness);
  }

  @Request
  @Test
  public void testDateCondition()
  {
    Assert.assertEquals(MdAttributeBooleanInfo.FALSE, mdWebDate.getValue(MdWebDateInfo.SHOW_ON_VIEW_ALL));
    Assert.assertEquals(MdAttributeBooleanInfo.TRUE, mdWebDate.getValue(MdWebDateInfo.SHOW_ON_SEARCH));
    Assert.assertEquals(MdAttributeBooleanInfo.FALSE, mdWebCharacter.getValue(MdWebDateInfo.SHOW_ON_SEARCH));

    EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "GTE");

    DateConditionDAO condition = DateConditionDAO.newInstance();
    condition.setValue(DateConditionInfo.VALUE, "2006-11-11");
    condition.setValue(DateConditionInfo.DEFINING_MD_FIELD, mdWebDate.getOid());
    condition.addItem(DateConditionInfo.OPERATION, item.getOid());
    condition.apply();

    try
    {
      DateConditionDAOIF test = DateConditionDAO.get(condition.getOid());

      AttributeEnumerationIF attribute = (AttributeEnumerationIF) test.getAttributeIF(DoubleConditionInfo.OPERATION);
      Set<String> itemIds = attribute.getEnumItemIdList();

      Assert.assertEquals(DateConditionInfo.CLASS, test.getType());
      Assert.assertEquals(condition.getValue(DoubleConditionInfo.VALUE), test.getValue(DoubleConditionInfo.VALUE));
      Assert.assertEquals(condition.getValue(DoubleConditionInfo.DEFINING_MD_FIELD), test.getValue(DoubleConditionInfo.DEFINING_MD_FIELD));
      Assert.assertEquals(1, itemIds.size());

      for (String itemId : itemIds)
      {
        Assert.assertEquals("GTE", EnumerationItemDAO.get(itemId).getName());
      }
    }
    finally
    {
      condition.delete();
    }
  }

  @Request
  @Test
  public void testDoubleCondition()
  {
    EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "LT");

    DoubleConditionDAO condition = DoubleConditionDAO.newInstance();
    condition.setValue(DoubleConditionInfo.VALUE, "10.0000000000");
    condition.setValue(DoubleConditionInfo.DEFINING_MD_FIELD, mdWebDouble.getOid());
    condition.addItem(DoubleConditionInfo.OPERATION, item.getOid());
    condition.apply();

    try
    {
      DoubleConditionDAOIF test = DoubleConditionDAO.get(condition.getOid());

      AttributeEnumerationIF attribute = (AttributeEnumerationIF) test.getAttributeIF(DoubleConditionInfo.OPERATION);
      Set<String> itemIds = attribute.getEnumItemIdList();

      Assert.assertEquals(DoubleConditionInfo.CLASS, test.getType());
      Assert.assertEquals(condition.getValue(DoubleConditionInfo.VALUE), test.getValue(DoubleConditionInfo.VALUE));
      Assert.assertEquals(condition.getValue(DoubleConditionInfo.DEFINING_MD_FIELD), test.getValue(DoubleConditionInfo.DEFINING_MD_FIELD));
      Assert.assertEquals(1, itemIds.size());

      for (String itemId : itemIds)
      {
        Assert.assertEquals("LT", EnumerationItemDAO.get(itemId).getName());
      }
    }
    finally
    {
      condition.delete();
    }
  }

  @Request
  @Test
  public void testLongCondition()
  {
    EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "LT");

    LongConditionDAO condition = LongConditionDAO.newInstance();
    condition.setValue(LongConditionInfo.VALUE, "10");
    condition.setValue(LongConditionInfo.DEFINING_MD_FIELD, mdWebDouble.getOid());
    condition.addItem(LongConditionInfo.OPERATION, item.getOid());
    condition.apply();

    try
    {
      LongConditionDAOIF test = LongConditionDAO.get(condition.getOid());

      AttributeEnumerationIF attribute = (AttributeEnumerationIF) test.getAttributeIF(LongConditionInfo.OPERATION);
      Set<String> itemIds = attribute.getEnumItemIdList();

      Assert.assertEquals(LongConditionInfo.CLASS, test.getType());
      Assert.assertEquals(condition.getValue(LongConditionInfo.VALUE), test.getValue(LongConditionInfo.VALUE));
      Assert.assertEquals(condition.getValue(LongConditionInfo.DEFINING_MD_FIELD), test.getValue(LongConditionInfo.DEFINING_MD_FIELD));
      Assert.assertEquals(1, itemIds.size());

      for (String itemId : itemIds)
      {
        Assert.assertEquals("LT", EnumerationItemDAO.get(itemId).getName());
      }
    }
    finally
    {
      condition.delete();
    }
  }

  @Request
  @Test
  public void testCharacterCondition()
  {
    EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "EQ");

    CharacterConditionDAO condition = CharacterConditionDAO.newInstance();
    condition.setValue(CharacterConditionInfo.VALUE, "testValue");
    condition.setValue(CharacterConditionInfo.DEFINING_MD_FIELD, mdWebCharacter.getOid());
    condition.addItem(CharacterConditionInfo.OPERATION, item.getOid());
    condition.apply();

    try
    {
      CharacterConditionDAOIF test = CharacterConditionDAO.get(condition.getOid());

      AttributeEnumerationIF attribute = (AttributeEnumerationIF) test.getAttributeIF(DoubleConditionInfo.OPERATION);
      Set<String> itemIds = attribute.getEnumItemIdList();

      Assert.assertEquals(CharacterConditionInfo.CLASS, test.getType());
      Assert.assertEquals(condition.getValue(DoubleConditionInfo.VALUE), test.getValue(DoubleConditionInfo.VALUE));
      Assert.assertEquals(condition.getValue(DoubleConditionInfo.DEFINING_MD_FIELD), test.getValue(DoubleConditionInfo.DEFINING_MD_FIELD));
      Assert.assertEquals(1, itemIds.size());

      for (String itemId : itemIds)
      {
        Assert.assertEquals("EQ", EnumerationItemDAO.get(itemId).getName());
      }
    }
    finally
    {
      TestFixtureFactory.delete(condition);
    }
  }

  @Request
  @Test
  public void testAndCondition()
  {
    EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "LT");

    DoubleConditionDAO firstCondition = DoubleConditionDAO.newInstance();
    firstCondition.setValue(DoubleConditionInfo.VALUE, "10.0000000000");
    firstCondition.setValue(DoubleConditionInfo.DEFINING_MD_FIELD, mdWebDouble.getOid());
    firstCondition.addItem(DoubleConditionInfo.OPERATION, item.getOid());
    firstCondition.apply();

    try
    {
      EnumerationItemDAO secondItem = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "GT");

      DoubleConditionDAO secondCondition = DoubleConditionDAO.newInstance();
      secondCondition.setValue(DoubleConditionInfo.VALUE, "135.0000000000");
      secondCondition.setValue(DoubleConditionInfo.DEFINING_MD_FIELD, mdWebDouble.getOid());
      secondCondition.addItem(DoubleConditionInfo.OPERATION, secondItem.getOid());
      secondCondition.apply();

      try
      {

        AndFieldConditionDAO condition = AndFieldConditionDAO.newInstance();
        condition.setValue(AndFieldConditionInfo.FIRST_CONDITION, firstCondition.getOid());
        condition.setValue(AndFieldConditionInfo.SECOND_CONDITION, secondCondition.getOid());
        condition.apply();

        try
        {
          AndFieldConditionDAOIF test = AndFieldConditionDAO.get(condition.getOid());

          Assert.assertEquals(AndFieldConditionInfo.CLASS, test.getType());
          Assert.assertEquals(condition.getValue(AndFieldConditionInfo.FIRST_CONDITION), test.getValue(AndFieldConditionInfo.FIRST_CONDITION));
          Assert.assertEquals(condition.getValue(AndFieldConditionInfo.SECOND_CONDITION), test.getValue(AndFieldConditionInfo.SECOND_CONDITION));
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

  @Request
  @Test
  public void testFieldDeleteWithCondition()
  {
    EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "LT");

    DoubleConditionDAO condition = DoubleConditionDAO.newInstance();
    condition.setValue(DoubleConditionInfo.VALUE, "10.0000000000");
    condition.setValue(DoubleConditionInfo.DEFINING_MD_FIELD, mdWebDouble.getOid());
    condition.addItem(DoubleConditionInfo.OPERATION, item.getOid());
    condition.apply();

    MdWebIntegerDAO field = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
    field.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getOid());
    field.apply();

    TestFixtureFactory.delete(field);

    try
    {
      FieldConditionDAOIF deletedCondition = FieldConditionDAO.get(condition.getOid());

      TestFixtureFactory.delete(deletedCondition);

      Assert.fail("The field condition was not deleted when the field was deleted");
    }
    catch (DataNotFoundException e)
    {
      // This is expected
    }
  }

  @Request
  @Test
  public void testGetCondition()
  {
    EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "LT");

    DoubleConditionDAO condition = DoubleConditionDAO.newInstance();
    condition.setValue(DoubleConditionInfo.VALUE, "10.0000000000");
    condition.setValue(DoubleConditionInfo.DEFINING_MD_FIELD, mdWebDouble.getOid());
    condition.addItem(DoubleConditionInfo.OPERATION, item.getOid());
    condition.apply();

    MdWebIntegerDAO field = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
    field.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getOid());
    field.apply();

    try
    {
      List<FieldConditionDAOIF> conditions = field.getConditions();

      Assert.assertEquals(1, conditions.size());
      Assert.assertEquals(condition.getOid(), conditions.get(0).getOid());
    }
    finally
    {
      TestFixtureFactory.delete(field);
    }
  }

  @Request
  @Test
  public void testConditionGrouping()
  {
    MdWebIntegerDAO field = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
    field.apply();

    try
    {
      EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "LT");

      DoubleConditionDAO condition = DoubleConditionDAO.newInstance();
      condition.setValue(DoubleConditionInfo.VALUE, "10.0000000000");
      condition.setValue(DoubleConditionInfo.DEFINING_MD_FIELD, mdWebDouble.getOid());
      condition.addItem(DoubleConditionInfo.OPERATION, item.getOid());
      condition.apply();

      MdWebGroupDAO group = TestFixtureFactory.addGroupField(mdForm);
      group.setValue(MdWebIntegerInfo.FIELD_CONDITION, condition.getOid());
      group.apply();

      try
      {
        TreeDAO relationship = group.addField(field);
        relationship.apply();

        try
        {
          List<FieldConditionDAOIF> conditions = field.getConditions();

          Assert.assertEquals(1, conditions.size());
          Assert.assertEquals(condition.getOid(), conditions.get(0).getOid());
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

  @Request
  @Test
  public void testMultipleConditionGrouping()
  {
    MdWebIntegerDAO field = TestFixtureFactory.addIntegerField(mdForm, mdAttributeInteger);
    field.apply();

    try
    {
      EnumerationItemDAO item = EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "LT");

      DoubleConditionDAO childCondition = DoubleConditionDAO.newInstance();
      childCondition.setValue(DoubleConditionInfo.VALUE, "10.0000000000");
      childCondition.setValue(DoubleConditionInfo.DEFINING_MD_FIELD, mdWebDouble.getOid());
      childCondition.addItem(DoubleConditionInfo.OPERATION, item.getOid());
      childCondition.apply();

      MdWebGroupDAO childGroup = TestFixtureFactory.addGroupField(mdForm);
      childGroup.setValue(MdWebIntegerInfo.FIELD_CONDITION, childCondition.getOid());
      childGroup.apply();

      try
      {
        TreeDAO childRelationship = childGroup.addField(field);
        childRelationship.apply();

        try
        {
          DoubleConditionDAO parentCondition = DoubleConditionDAO.newInstance();
          parentCondition.setValue(DoubleConditionInfo.VALUE, "50");
          parentCondition.setValue(DoubleConditionInfo.DEFINING_MD_FIELD, mdWebDouble.getOid());
          parentCondition.addItem(DoubleConditionInfo.OPERATION, EnumerationItemDAO.getEnumeration(FieldOperation.CLASS, "LT").getOid());
          parentCondition.apply();

          MdWebGroupDAO parentGroup = TestFixtureFactory.addGroupField(mdForm);
          parentGroup.setValue(MdWebIntegerInfo.FIELD_CONDITION, parentCondition.getOid());
          parentGroup.setValue(MdWebIntegerInfo.FIELD_NAME, "parentGroup");
          parentGroup.apply();

          try
          {
            TreeDAO relationship = parentGroup.addField(childGroup);
            relationship.apply();

            try
            {
              List<FieldConditionDAOIF> conditions = field.getConditions();

              Assert.assertEquals(2, conditions.size());
              Assert.assertTrue(conditions.contains(parentCondition));
              Assert.assertTrue(conditions.contains(childCondition));
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
