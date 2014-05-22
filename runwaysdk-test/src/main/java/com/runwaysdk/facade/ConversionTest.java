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
package com.runwaysdk.facade;

import java.util.Locale;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.w3c.dom.Document;

import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.BusinessQueryDTO;
import com.runwaysdk.business.EnumDTO;
import com.runwaysdk.business.MethodMetaData;
import com.runwaysdk.business.RelationshipDTO;
import com.runwaysdk.business.UtilDTO;
import com.runwaysdk.business.ViewDTO;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.MdUtilInfo;
import com.runwaysdk.constants.MdViewInfo;
import com.runwaysdk.constants.QueryConditions;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.constants.TypeInfo;
import com.runwaysdk.constants.UserInfo;
import com.runwaysdk.transport.conversion.ConversionFacade;

public class ConversionTest extends TestCase
{
  private static TypeInfo    houseType            = new TypeInfo("com.test.conversion", "House");

  private static TypeInfo    brickType            = new TypeInfo("com.test.conversion", "Brick");

  private static TypeInfo    madeOfType           = new TypeInfo("com.test.conversion", "MadeOf");

  private static BusinessDTO houseMdEntity        = null;

  private static BusinessDTO brickMdEntity        = null;

  private static BusinessDTO madeOfMdRelationship = null;

  private static TypeInfo    houseUtilType        = new TypeInfo("com.test.conversion", "HouseUtil");

  private static BusinessDTO houseMdUtil          = null;

  private static TypeInfo    houseViewType        = new TypeInfo("com.test.conversion", "HouseView");

  private static BusinessDTO houseMdView          = null;

  private static String      sessionId            = null;

  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(ConversionTest.suite());
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ConversionTest.class);

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
    sessionId = Facade.login(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[]{CommonProperties.getDefaultLocale()});

    /*
     * Since we're only testing conversions, especially Object <-> Document,
     * just create some simple types.
     */

    // define a house type
    houseMdEntity = Facade.newBusiness(sessionId, MdBusinessInfo.CLASS);
    houseMdEntity.setValue(MdBusinessInfo.NAME, houseType.getTypeName());
    houseMdEntity.setValue(MdBusinessInfo.PACKAGE, houseType.getPackageName());
    houseMdEntity.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "It's a house, silly!");
    houseMdEntity.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "brick house");
    houseMdEntity = Facade.createBusiness(sessionId, houseMdEntity);

    // define a brick type
    brickMdEntity = Facade.newBusiness(sessionId, MdBusinessInfo.CLASS);
    brickMdEntity.setValue(MdBusinessInfo.NAME, brickType.getTypeName());
    brickMdEntity.setValue(MdBusinessInfo.PACKAGE, brickType.getPackageName());
    brickMdEntity.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "It's a brick ... for building.");
    brickMdEntity.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "brick");
    brickMdEntity = Facade.createBusiness(sessionId, brickMdEntity);

    // define a relationship between house and brick
    madeOfMdRelationship = Facade.newBusiness(sessionId, MdRelationshipInfo.CLASS);
    madeOfMdRelationship.setValue(MdRelationshipInfo.COMPOSITION, MdAttributeBooleanInfo.TRUE);
    madeOfMdRelationship.setValue(MdRelationshipInfo.NAME, madeOfType.getTypeName());
    madeOfMdRelationship.setValue(MdRelationshipInfo.PACKAGE, madeOfType.getPackageName());
    madeOfMdRelationship.setStructValue(MdRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A house is made of bricks.");
    madeOfMdRelationship.setValue(MdRelationshipInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    madeOfMdRelationship.setValue(MdRelationshipInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    madeOfMdRelationship.setValue(MdRelationshipInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    madeOfMdRelationship.setValue(MdRelationshipInfo.PARENT_MD_BUSINESS, houseMdEntity.getId());
    madeOfMdRelationship.setValue(MdRelationshipInfo.PARENT_CARDINALITY, "1");
    madeOfMdRelationship.setStructValue(MdRelationshipInfo.PARENT_DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE, "parent " + houseType.getTypeName());
    madeOfMdRelationship.setValue(MdRelationshipInfo.CHILD_MD_BUSINESS, brickMdEntity.getId());
    madeOfMdRelationship.setValue(MdRelationshipInfo.CHILD_CARDINALITY, "*");
    madeOfMdRelationship.setStructValue(MdRelationshipInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "child " + brickType.getTypeName());
    madeOfMdRelationship.setValue(MdRelationshipInfo.PARENT_METHOD, "house");
    madeOfMdRelationship.setValue(MdRelationshipInfo.CHILD_METHOD, "brick");
    madeOfMdRelationship = Facade.createBusiness(sessionId, madeOfMdRelationship);

    // define a house util type
    houseMdUtil = Facade.newBusiness(sessionId, MdUtilInfo.CLASS);
    houseMdUtil.setValue(MdUtilInfo.NAME, houseUtilType.getTypeName());
    houseMdUtil.setValue(MdUtilInfo.PACKAGE, houseUtilType.getPackageName());
    houseMdUtil.setStructValue(MdUtilInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "It's a house, silly!");
    houseMdUtil.setStructValue(MdUtilInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "brick house");
    houseMdUtil = Facade.createBusiness(sessionId, houseMdUtil);

    // define a house view type
    houseMdView = Facade.newBusiness(sessionId, MdViewInfo.CLASS);
    houseMdView.setValue(MdUtilInfo.NAME, houseViewType.getTypeName());
    houseMdView.setValue(MdUtilInfo.PACKAGE, houseViewType.getPackageName());
    houseMdView.setStructValue(MdUtilInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "It's a house, silly!");
    houseMdView.setStructValue(MdUtilInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "brick house");
    houseMdView = Facade.createBusiness(sessionId, houseMdView);
  }

  public static void classTearDown()
  {
    if (houseMdEntity != null)
    {
      Facade.lock(sessionId, houseMdEntity.getId());
      Facade.delete(sessionId, houseMdEntity.getId());
    }

    if (brickMdEntity != null)
    {
      Facade.lock(sessionId, brickMdEntity.getId());
      Facade.delete(sessionId, brickMdEntity.getId());
    }

    if (houseMdUtil != null)
    {
      Facade.lock(sessionId, houseMdUtil.getId());
      Facade.delete(sessionId, houseMdUtil.getId());
    }

    if (houseMdView!= null)
    {
      Facade.lock(sessionId, houseMdView.getId());
      Facade.delete(sessionId, houseMdView.getId());
    }

    Facade.logout(sessionId);
  }

  public void testBusinessDTOConversion()
  {
    BusinessDTO house = null;
    try
    {
      // create a house
      house = Facade.newBusiness(sessionId, houseType.getType());
      house = Facade.createBusiness(sessionId, house);

      // convert it
      Document document = ConversionFacade.getDocumentFromObject(house, false);
      BusinessDTO retHouse = (BusinessDTO) ConversionFacade.getObjectFromDocument(null, document);

      // check the values
      for (String name : house.getAttributeNames())
      {
        if (!house.getValue(name).equals(retHouse.getValue(name)))
        {
          fail("Conversion with a BusinessDTO failed.");
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (house != null && !house.isNewInstance())
      {
        Facade.lock(sessionId, house.getId());
        Facade.delete(sessionId, house.getId());
      }
    }
  }

  public void testArrayBusinessDTO()
  {
    BusinessDTO[] houses = null;
    try
    {
      // create 10 houses
      houses = new BusinessDTO[10];
      for (int i = 0; i < 10; i++)
      {
        BusinessDTO house = Facade.newBusiness(sessionId, houseType.getType());
        house = Facade.createBusiness(sessionId, house);
        houses[i] = house;
      }

      // convert it
      Document document = ConversionFacade.getDocumentFromObject(houses, false);
      BusinessDTO[] retHouses = (BusinessDTO[]) ConversionFacade.getObjectFromDocument(null, document);

      // test all values
      for (int i = 0; i < houses.length; i++)
      {
        BusinessDTO retHouse = retHouses[i];
        BusinessDTO house = houses[i];

        for (String name : house.getAttributeNames())
        {
          if (!house.getValue(name).equals(retHouse.getValue(name)))
          {
            fail("Conversion with an array of BusinessDTOs failed.");
          }
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (houses != null)
      {
        for (BusinessDTO house : houses)
        {
          if (house != null && !house.isNewInstance())
          {
            Facade.lock(sessionId, house.getId());
            Facade.delete(sessionId, house.getId());
          }
        }
      }
    }
  }

  public void testArrayRelationshipDTO()
  {
    BusinessDTO house = null;

    try
    {
      // create the house (parent)
      house = Facade.newBusiness(sessionId, houseType.getType());
      house = Facade.createBusiness(sessionId, house);

      // create some bricks
      BusinessDTO brick1 = null, brick2 = null, brick3 = null;
      brick1 = Facade.newBusiness(sessionId, brickType.getType());
      brick2 = Facade.newBusiness(sessionId, brickType.getType());
      brick3 = Facade.newBusiness(sessionId, brickType.getType());

      brick1 = Facade.createBusiness(sessionId, brick1);
      brick2 = Facade.createBusiness(sessionId, brick2);
      brick3 = Facade.createBusiness(sessionId, brick3);

      // create some relationships
      RelationshipDTO[] relArray = new RelationshipDTO[3];

      relArray[0] = Facade.addChild(sessionId, house.getId(), brick1.getId(), madeOfType.getType());
      Facade.createRelationship(sessionId, relArray[0]);
      relArray[1] = Facade.addChild(sessionId, house.getId(), brick2.getId(), madeOfType.getType());
      Facade.createRelationship(sessionId, relArray[1]);
      relArray[2] = Facade.addChild(sessionId, house.getId(), brick3.getId(), madeOfType.getType());
      Facade.createRelationship(sessionId, relArray[2]);

      // convert the relationships
      Document document = ConversionFacade.getDocumentFromObject(relArray, false);
      RelationshipDTO[] retRelArray =
        (RelationshipDTO[]) ConversionFacade.getObjectFromDocument(null, document);

      // test all values
      for (int i = 0; i < retRelArray.length; i++)
      {
        for (String name : relArray[i].getAttributeNames())
        {
          if (!relArray[i].getValue(name).equals(retRelArray[i].getValue(name)))
          {
            fail("Conversion with an array of RelationshipDTOs failed.");
          }
        }
      }

    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (house != null && !house.isNewInstance())
      {
        Facade.lock(sessionId, house.getId());
        Facade.delete(sessionId, house.getId());
      }
    }
  }

  public void testRelationshipDTO()
  {
    BusinessDTO house = null;
    try
    {
      // create a parent and child
      house = Facade.newBusiness(sessionId, houseType.getType());
      house = Facade.createBusiness(sessionId, house);

      BusinessDTO brick = Facade.newBusiness(sessionId, brickType.getType());
      brick = Facade.createBusiness(sessionId, brick);

      // create the relationship
      RelationshipDTO madeOf = Facade.addChild(sessionId, house.getId(), brick.getId(), madeOfType.getType());
      Facade.createRelationship(sessionId, madeOf);

      // convert it
      Document document = ConversionFacade.getDocumentFromObject(madeOf, false);
      RelationshipDTO retRel = (RelationshipDTO) ConversionFacade.getObjectFromDocument(null, document);

      // check the values
      for (String name : madeOf.getAttributeNames())
      {
        if (!madeOf.getValue(name).equals(retRel.getValue(name)))
        {
          fail("Conversion with a RelationshipDTO failed.");
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (house != null && !house.isNewInstance())
      {
        Facade.lock(sessionId, house.getId());
        Facade.delete(sessionId, house.getId());
      }
    }
  }

  public void testIntegerPrimitiveConversion()
  {
    try
    {
      Integer testInt = new Integer(123123);

      // convert the primitive
      Document primDoc = ConversionFacade.getDocumentFromObject(testInt, false);

      // convert it back
      Integer retInt = (Integer) ConversionFacade.getObjectFromDocument(null, primDoc);
      if (testInt != retInt.intValue())
      {
        fail("Converting a primitive int did not work");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testLongPrimitiveConversion()
  {
    try
    {
      Long testLong = new Long(123123L);

      // convert the primitive
      Document document = ConversionFacade.getDocumentFromObject(testLong, false);

      // convert it back
      Long retLong = (Long) ConversionFacade.getObjectFromDocument(null, document);
      if (testLong != retLong.longValue())
      {
        fail("Converting a primitive long did not work");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testShortPrimitiveConversion()
  {
    try
    {
      Short testShort = new Short("123");

      // convert the primitive
      Document document = ConversionFacade.getDocumentFromObject(testShort, false);

      // convert it back
      Short retShort = (Short) ConversionFacade.getObjectFromDocument(null, document);
      if (testShort != retShort.shortValue())
      {
        fail("Converting a primitive short did not work");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testFloatPrimitiveConversion()
  {
    try
    {
      Float testFloat = new Float(123.123F);

      // convert the primitive
      Document document = ConversionFacade.getDocumentFromObject(testFloat, false);

      // convert it back
      Float retFloat = (Float) ConversionFacade.getObjectFromDocument(null, document);
      if (testFloat != retFloat.floatValue())
      {
        fail("Converting a primitive float did not work");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testDoublePrimitiveConversion()
  {
    try
    {
      Double testDouble = new Double(123123.123123);

      // convert the primitive
      Document document = ConversionFacade.getDocumentFromObject(testDouble, false);

      // convert it back
      Double retDouble = (Double) ConversionFacade.getObjectFromDocument(null, document);
      if (testDouble != retDouble.doubleValue())
      {
        fail("Converting a primitive double did not work");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testBytePrimitiveConversion()
  {
    try
    {
      Byte testByte = new Byte("123");

      // convert the primitive
      Document document = ConversionFacade.getDocumentFromObject(testByte, false);

      // convert it back
      Byte retByte = (Byte) ConversionFacade.getObjectFromDocument(null, document);
      if (testByte != retByte.doubleValue())
      {
        fail("Converting a primitive byte did not work");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStringPrimitiveConversion()
  {
    try
    {
      String testString = "Just a Test";

      // convert the primitive
      Document document = ConversionFacade.getDocumentFromObject(testString, false);

      // convert it back
      String retString = (String) ConversionFacade.getObjectFromDocument(null, document);
      if (!testString.equals(retString))
      {
        fail("Converting a primitive String did not work");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testCharacterPrimitiveConversion()
  {
    try
    {
      Character testCharacter = new Character('\u0123');

      // convert the primitive
      Document document = ConversionFacade.getDocumentFromObject(testCharacter, false);

      // convert it back
      Character retCharacter = (Character) ConversionFacade.getObjectFromDocument(null, document);
      if (testCharacter != retCharacter.charValue())
      {
        fail("Converting a primitive Character did not work");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testNullConversion()
  {
    try
    {
      // convert the primitive
      Document document = ConversionFacade.getDocumentFromObject(null, false);

      // convert it back
      Object object = ConversionFacade.getObjectFromDocument(null, document);
      if (object != null)
      {
        fail("Converting a null did not work");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testBooleanConversion()
  {
    try
    {
      Boolean bool = new Boolean(true);

      // convert the primitive
      Document document = ConversionFacade.getDocumentFromObject(bool, false);

      // convert it back
      Boolean retBool = (Boolean) ConversionFacade.getObjectFromDocument(null, document);
      if (retBool.booleanValue() != bool.booleanValue())
      {
        fail("Converting a boolean did not work");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testArrayIntegerPrimitiveConversion()
  {
    try
    {
      Integer[] array = { 100, 200, 300, 400, 500, 600, 700, 800, 900, 100999999 };

      // convert the primitive
      Document primDoc = ConversionFacade.getDocumentFromObject(array, false);

      // convert it back
      Integer[] retArray = (Integer[]) ConversionFacade.getObjectFromDocument(null, primDoc);
      for (int i = 0; i < retArray.length; i++)
      {
        if (!retArray[i].equals(array[i]))
        {
          fail("Converting an array of primitive ints did not work");
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  public void testArrayLongPrimitiveConversion()
  {
    try
    {
      Long[] array = { 123123L, 21312L, 90980L, 1231L, 9999L };

      // convert the primitive
      Document document = ConversionFacade.getDocumentFromObject(array, false);

      // convert it back
      Long[] retArray = (Long[]) ConversionFacade.getObjectFromDocument(null, document);
      for (int i = 0; i < retArray.length; i++)
      {
        if (!retArray[i].equals(array[i]))
        {
          fail("Converting an array of primitive longs did not work");
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testArrayShortPrimitiveConversion()
  {
    try
    {
      Short[] array = { 123, 111, 321, 634, 909, 11000 };

      // convert the primitive
      Document document = ConversionFacade.getDocumentFromObject(array, false);

      // convert it back
      Short[] retArray = (Short[]) ConversionFacade.getObjectFromDocument(null, document);
      for (int i = 0; i < retArray.length; i++)
      {
        if (!retArray[i].equals(array[i]))
        {
          fail("Converting an array of primitive shorts did not work");
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testArrayFloatPrimitiveConversion()
  {
    try
    {
      Float[] array = { 12.123134F, 10293.1237F, 3223.39081F, 634.131F, 909F, 11.0F };

      // convert the primitive
      Document document = ConversionFacade.getDocumentFromObject(array, false);

      // convert it back
      Float[] retArray = (Float[]) ConversionFacade.getObjectFromDocument(null, document);
      for (int i = 0; i < retArray.length; i++)
      {
        if (!retArray[i].equals(array[i]))
        {
          fail("Converting an array of primitive floats did not work");
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testArrayDoublePrimitiveConversion()
  {
    try
    {
      Double[] array = { 12.123134, 10293.1237, 3223.39081, 634.131, 909.01, 11.0 };

      // convert the primitive
      Document document = ConversionFacade.getDocumentFromObject(array, false);

      // convert it back
      Double[] retArray = (Double[]) ConversionFacade.getObjectFromDocument(null, document);
      for (int i = 0; i < retArray.length; i++)
      {
        if (!retArray[i].equals(array[i]))
        {
          fail("Converting an array of primitive doubles did not work");
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testArrayBytePrimitiveConversion()
  {
    try
    {
      Byte[] array = { 111, 123, 121, -056, -74 };

      // convert the primitive
      Document document = ConversionFacade.getDocumentFromObject(array, false);

      // convert it back
      Byte[] retArray = (Byte[]) ConversionFacade.getObjectFromDocument(null, document);
      for (int i = 0; i < retArray.length; i++)
      {
        if (!retArray[i].equals(array[i]))
        {
          fail("Converting an array of primitive bytes did not work");
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testArrayStringPrimitiveConversion()
  {
    try
    {
      String[] array = { "Not", " ", "another", " ", "hello", " ", "world" };

      // convert the primitive
      Document document = ConversionFacade.getDocumentFromObject(array, false);

      // convert it back
      String[] retArray = (String[]) ConversionFacade.getObjectFromDocument(null, document);
      for (int i = 0; i < retArray.length; i++)
      {
        if (!retArray[i].equals(array[i]))
        {
          fail("Converting an array of primitive doubles did not work");
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testArrayCharacterPrimitiveConversion()
  {
    try
    {
      Character[] array = { 'b', '\u0123', 'z', '\u0199', 'R', '.' };

      // convert the primitive
      Document document = ConversionFacade.getDocumentFromObject(array, false);

      // convert it back
      Character[] retArray = (Character[]) ConversionFacade.getObjectFromDocument(null, document);
      for (int i = 0; i < retArray.length; i++)
      {
        if (!retArray[i].equals(array[i]))
        {
          fail("Converting an array of primitive character did not work");
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testArrayNullConversion()
  {
    try
    {
      Object[] array = { null, null, null, null, null, null, null, null };

      // convert the primitive
      Document document = ConversionFacade.getDocumentFromObject(array, false);

      // convert it back
      Object[] retArray = (Object[]) ConversionFacade.getObjectFromDocument(null, document);
      for (int i = 0; i < retArray.length; i++)
      {
        if (retArray[i] != null)
        {
          fail("Converting an array of nulls did not work");
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testArrayBooleanConversion()
  {
    try
    {
      Boolean[] array = { true, false, false, true, true, false, true, false };

      // convert the primitive
      Document document = ConversionFacade.getDocumentFromObject(array, false);

      // convert it back
      Boolean[] retArray = (Boolean[]) ConversionFacade.getObjectFromDocument(null, document);
      for (int i = 0; i < retArray.length; i++)
      {
        if (retArray[i].booleanValue() != array[i].booleanValue())
        {
          fail("Converting an array of booleans did not work");
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testArrayOfArrayPrimitive()
  {
    // create a two dimensional array and just throw integer values in it
    Object[] firstArray = new Object[5];
    for (int i = 0; i < firstArray.length; i++)
    {
      Integer[] secArray = new Integer[10];
      for (int j = 0; j < 10; j++)
      {
        secArray[j] = new Integer(i + j);
      }
      firstArray[i] = secArray;
    }

    Document document = ConversionFacade.getDocumentFromObject(firstArray, false);

    Object[] retArray = (Object[]) ConversionFacade.getObjectFromDocument(null, document);

    // check all values
    for (int i = 0; i < firstArray.length; i++)
    {
      Integer[] secArray = (Integer[]) retArray[i];
      for (int j = 0; j < 10; j++)
      {
        if (!secArray[j].equals( ( i + j )))
        {
          fail("A two-dimensional array was not correctly converted into a document and back.");
        }
      }
    }
  }

  public void testQueryDTOConversion()
  {
    try
    {
      // Create a new queryDTO (it won't actually query anything)
      BusinessQueryDTO queryDTO = (BusinessQueryDTO) Facade.getQuery(sessionId, UserInfo.CLASS);
      queryDTO.addCondition(UserInfo.USERNAME, QueryConditions.EQUALS, "Tommy");

      // convert the queryDTO
      Document document = ConversionFacade.getDocumentFromObject(queryDTO, false);
      BusinessQueryDTO retDTO = (BusinessQueryDTO) ConversionFacade.getObjectFromDocument(null, document);

      // check the type
      if (!queryDTO.getType().equals(retDTO.getType()))
      {
        fail("Conversion with a QueryDTO did not work correctly.");
      }

      // now check the conditions
      if (!retDTO.hasCondition(UserInfo.USERNAME, QueryConditions.EQUALS, "Tommy"))
      {
        fail("Conversion with a QueryDTO conditions did not work correctly.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testMetaDataConversion() throws Exception
  {
    String[] declaredTypes = {houseType.getType(), brickType.getType()};
    String[] actualTypes = {brickType.getType(), brickType.getType()};

    MethodMetaData metadata = new MethodMetaData(houseType.getType(), "testMethod", declaredTypes);
    metadata.setActualTypes(actualTypes);

    Document document = ConversionFacade.getDocumentFromMethodMetaData(metadata);
    MethodMetaData output = ConversionFacade.getMethodMetaDataFromDocument(document);
    String[] outDeclared = output.getDeclaredTypes();
    String[] outActual = output.getActualTypes();

    assertEquals(metadata.getClassName(), output.getClassName());
    assertEquals(metadata.getMethodName(), output.getMethodName());
    assertEquals(declaredTypes.length, outDeclared.length);
    assertEquals(actualTypes.length, outActual.length);


    for(int i = 0; i < declaredTypes.length; i++)
    {
      assertEquals(declaredTypes[i], outDeclared[i]);
    }

    for(int i = 0; i < actualTypes.length; i++)
    {
      assertEquals(actualTypes[i], outActual[i]);
    }
  }

  public void testMetaDataConversion2() throws Exception
  {
    String[] declaredTypes = {};
    String[] actualTypes = {};

    MethodMetaData metadata = new MethodMetaData(houseType.getType(), "testMethod", declaredTypes);
    metadata.setActualTypes(actualTypes);

    Document document = ConversionFacade.getDocumentFromMethodMetaData(metadata);
    MethodMetaData output = ConversionFacade.getMethodMetaDataFromDocument(document);
    String[] outDeclared = output.getDeclaredTypes();
    String[] outActual = output.getActualTypes();

    assertEquals(metadata.getClassName(), output.getClassName());
    assertEquals(metadata.getMethodName(), output.getMethodName());
    assertEquals(declaredTypes.length, outDeclared.length);
    assertEquals(actualTypes.length, outActual.length);


    for(int i = 0; i < declaredTypes.length; i++)
    {
      assertEquals(declaredTypes[i], outDeclared[i]);
    }

    for(int i = 0; i < actualTypes.length; i++)
    {
      assertEquals(actualTypes[i], outActual[i]);
    }
  }

  public void testEnumDTOConversion() throws Exception
  {
    String type = "test.state.AllStates";
    String name = "Colorado";

    EnumDTO enumDTO = new EnumDTO(type, name);

    Document document = ConversionFacade.getDocumentFromEnumDTO(enumDTO, false);
    EnumDTO output = ConversionFacade.getEnumDTOFromDocument(null, document);

    assertEquals(type, output.getEnumType());
    assertEquals(name, output.getEnumName());
  }

  public void testEnumDTOConversion2() throws Exception
  {
    String type = "test.state.AllStates";
    String name = "Colorado";
//    BusinessDTO house = null;

    try
    {
      EnumDTO enumDTO = new EnumDTO(type, name);

      // convert it
      Document document = ConversionFacade.getDocumentFromObject(enumDTO, false);
      EnumDTO output = (EnumDTO) ConversionFacade.getObjectFromDocument(null, document);

      assertEquals(type, output.getEnumType());
      assertEquals(name, output.getEnumName());
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testUtilDTOConversion()
  {
    UtilDTO house = null;
    try
    {
      // create a house
      house = (UtilDTO)Facade.newMutable(sessionId, houseUtilType.getType());
      house = (UtilDTO)Facade.createSessionComponent(sessionId, house);

      // convert it
      Document document = ConversionFacade.getDocumentFromObject(house, false);
      UtilDTO retHouse = (UtilDTO) ConversionFacade.getObjectFromDocument(null, document);

      // check the values
      for (String name : house.getAttributeNames())
      {
        if (!house.getValue(name).equals(retHouse.getValue(name)))
        {
          fail("Conversion with a UtilDTO failed.");
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (house != null && !house.isNewInstance())
      {
        Facade.delete(sessionId, house.getId());
      }
    }
  }

  public void testArrayUtilDTO()
  {
    UtilDTO[] houses = null;
    try
    {
      // create 10 houses
      houses = new UtilDTO[10];
      for (int i = 0; i < 10; i++)
      {
        UtilDTO house = (UtilDTO)Facade.newMutable(sessionId, houseUtilType.getType());
        house = (UtilDTO)Facade.createSessionComponent(sessionId, house);
        houses[i] = house;
      }

      // convert it
      Document document = ConversionFacade.getDocumentFromObject(houses, false);
      UtilDTO[] retHouses = (UtilDTO[]) ConversionFacade.getObjectFromDocument(null, document);

      // test all values
      for (int i = 0; i < houses.length; i++)
      {
        UtilDTO retHouse = retHouses[i];
        UtilDTO house = houses[i];

        for (String name : house.getAttributeNames())
        {
          if (!house.getValue(name).equals(retHouse.getValue(name)))
          {
            fail("Conversion with an array of UtilDTOs failed.");
          }
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (houses != null)
      {
        for (UtilDTO house : houses)
        {
          if (house != null && !house.isNewInstance())
          {
            Facade.delete(sessionId, house.getId());
          }
        }
      }
    }
  }

  public void testViewDTOConversion()
  {
    ViewDTO house = null;
    try
    {
      // create a house
      house = (ViewDTO)Facade.newMutable(sessionId, houseViewType.getType());
      house = (ViewDTO)Facade.createSessionComponent(sessionId, house);

      // convert it
      Document document = ConversionFacade.getDocumentFromObject(house, false);
      ViewDTO retHouse = (ViewDTO) ConversionFacade.getObjectFromDocument(null, document);

      // check the values
      for (String name : house.getAttributeNames())
      {
        if (!house.getValue(name).equals(retHouse.getValue(name)))
        {
          fail("Conversion with a ViewDTO failed.");
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (house != null && !house.isNewInstance())
      {
        Facade.delete(sessionId, house.getId());
      }
    }
  }

  public void testArrayViewDTO()
  {
    ViewDTO[] houses = null;
    try
    {
      // create 10 houses
      houses = new ViewDTO[10];
      for (int i = 0; i < 10; i++)
      {
        ViewDTO house = (ViewDTO)Facade.newMutable(sessionId, houseViewType.getType());
        house = (ViewDTO)Facade.createSessionComponent(sessionId, house);
        houses[i] = house;
      }

      // convert it
      Document document = ConversionFacade.getDocumentFromObject(houses, false);
      ViewDTO[] retHouses = (ViewDTO[]) ConversionFacade.getObjectFromDocument(null, document);

      // test all values
      for (int i = 0; i < houses.length; i++)
      {
        ViewDTO retHouse = retHouses[i];
        ViewDTO house = houses[i];

        for (String name : house.getAttributeNames())
        {
          if (!house.getValue(name).equals(retHouse.getValue(name)))
          {
            fail("Conversion with an array of ViewDTOs failed.");
          }
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (houses != null)
      {
        for (ViewDTO house : houses)
        {
          if (house != null && !house.isNewInstance())
          {
            Facade.delete(sessionId, house.getId());
          }
        }
      }
    }
  }

}
