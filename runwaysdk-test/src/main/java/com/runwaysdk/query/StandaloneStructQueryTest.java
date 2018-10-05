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
package com.runwaysdk.query;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.runwaysdk.ClasspathTestRunner;
import com.runwaysdk.business.generation.EntityQueryAPIGenerator;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.MdAttributeBlobInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.MdAttributeDateTimeInfo;
import com.runwaysdk.constants.MdAttributeDecimalInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeFloatInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeLongInfo;
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.constants.MdAttributeTimeInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdStructInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.StructDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeBlobDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeClobDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDecimalDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeFloatDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLongDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTextDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.session.Request;

@RunWith(ClasspathTestRunner.class)
public class StandaloneStructQueryTest
{
  private static final String     pack = "test.query";

  private static String           clubsId;

  private static String           heartsId;

  private static MdStructDAO      collection;

  private static MdBusinessDAO    suitMaster;

  private static MdEnumerationDAO suitEnum;

  private static StructDAO        testStructQueryObject;

  @Request
  @BeforeClass
  public static void classSetUp()
  {
    suitMaster = MdBusinessDAO.newInstance();
    suitMaster.setValue(MdBusinessInfo.NAME, "SuitMaster");
    suitMaster.setValue(MdBusinessInfo.PACKAGE, pack);
    suitMaster.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Suit Enumeration Master List");
    suitMaster.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    suitMaster.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, EnumerationMasterInfo.ID_VALUE);
    suitMaster.apply();

    suitEnum = MdEnumerationDAO.newInstance();
    suitEnum.setValue(MdEnumerationInfo.NAME, "SuitEnum");
    suitEnum.setValue(MdEnumerationInfo.PACKAGE, pack);
    suitEnum.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Suit Enumeration");
    suitEnum.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    suitEnum.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, suitMaster.getOid());
    suitEnum.apply();

    MdAttributeBlobDAO pic = MdAttributeBlobDAO.newInstance();
    pic.setValue(MdAttributeBlobInfo.NAME, "pic");
    pic.setStructValue(MdAttributeBlobInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Suit Pic");
    pic.setValue(MdAttributeBlobInfo.DEFINING_MD_CLASS, suitMaster.getOid());
    pic.apply();

    BusinessDAO enumItem = BusinessDAO.newInstance(suitMaster.definesType());
    enumItem.setValue(EnumerationMasterInfo.NAME, "CLUBS");
    enumItem.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Clubs");
    enumItem.setBlob("pic", new byte[] { 1, 2, 3, 4 });
    clubsId = enumItem.apply();

    enumItem = BusinessDAO.newInstance(suitMaster.definesType());
    enumItem.setValue(EnumerationMasterInfo.NAME, "DIAMONDS");
    enumItem.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Diamonds");
    enumItem.setBlob("pic", new byte[] { 2, 4, 6, 8 });
    enumItem.apply();

    enumItem = BusinessDAO.newInstance(suitMaster.definesType());
    enumItem.setValue(EnumerationMasterInfo.NAME, "HEARTS");
    enumItem.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Hearts");
    enumItem.setBlob("pic", new byte[] { 3, 6, 9 });
    heartsId = enumItem.apply();

    enumItem = BusinessDAO.newInstance(suitMaster.definesType());
    enumItem.setValue(EnumerationMasterInfo.NAME, "SPADES");
    enumItem.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Spades");
    enumItem.setBlob("pic", new byte[] { 4, 8 });
    enumItem.apply();

    collection = MdStructDAO.newInstance();
    collection.setValue(MdStructInfo.NAME, "Collection");
    collection.setValue(MdStructInfo.PACKAGE, pack);
    collection.setStructValue(MdStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "All Attributes");
    collection.apply();

    MdAttributeBlobDAO aBlob = MdAttributeBlobDAO.newInstance();
    aBlob.setValue(MdAttributeBlobInfo.NAME, "queryBlob");
    aBlob.setStructValue(MdAttributeBlobInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Query Blob");
    aBlob.setValue(MdAttributeBlobInfo.DEFINING_MD_CLASS, collection.getOid());
    aBlob.apply();

    MdAttributeBooleanDAO aBoolean = MdAttributeBooleanDAO.newInstance();
    aBoolean.setValue(MdAttributeBooleanInfo.NAME, "queryBoolean");
    aBoolean.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Query Boolean");
    aBoolean.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
    aBoolean.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
    aBoolean.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, collection.getOid());
    aBoolean.apply();

    MdAttributeCharacterDAO aCharacter = MdAttributeCharacterDAO.newInstance();
    aCharacter.setValue(MdAttributeCharacterInfo.NAME, "queryCharacter");
    aCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Character");
    aCharacter.setValue(MdAttributeCharacterInfo.SIZE, "32");
    aCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, collection.getOid());
    aCharacter.apply();

    MdAttributeDecimalDAO aDecimal = MdAttributeDecimalDAO.newInstance();
    aDecimal.setValue(MdAttributeDecimalInfo.NAME, "queryDecimal");
    aDecimal.setStructValue(MdAttributeDecimalInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Decimal");
    aDecimal.setValue(MdAttributeDecimalInfo.DEFINING_MD_CLASS, collection.getOid());
    aDecimal.setValue(MdAttributeDecimalInfo.LENGTH, "16");
    aDecimal.setValue(MdAttributeDecimalInfo.DECIMAL, "4");
    aDecimal.apply();

    MdAttributeDoubleDAO aDouble = MdAttributeDoubleDAO.newInstance();
    aDouble.setValue(MdAttributeDoubleInfo.NAME, "queryDouble");
    aDouble.setStructValue(MdAttributeDoubleInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Double");
    aDouble.setValue(MdAttributeDoubleInfo.DEFINING_MD_CLASS, collection.getOid());
    aDouble.setValue(MdAttributeDoubleInfo.LENGTH, "16");
    aDouble.setValue(MdAttributeDoubleInfo.DECIMAL, "4");
    aDouble.apply();

    MdAttributeEnumerationDAO anEnum = MdAttributeEnumerationDAO.newInstance();
    anEnum.setValue(MdAttributeEnumerationInfo.NAME, "queryEnumeration");
    anEnum.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "An Enuemrated Attribute");
    anEnum.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.FALSE);
    anEnum.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, collection.getOid());
    anEnum.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, suitEnum.getOid());
    anEnum.apply();

    MdAttributeFloatDAO aFloat = MdAttributeFloatDAO.newInstance();
    aFloat.setValue(MdAttributeFloatInfo.NAME, "queryFloat");
    aFloat.setStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Float");
    aFloat.setValue(MdAttributeFloatInfo.DEFINING_MD_CLASS, collection.getOid());
    aFloat.setValue(MdAttributeFloatInfo.LENGTH, "16");
    aFloat.setValue(MdAttributeFloatInfo.DECIMAL, "4");
    aFloat.apply();

    MdAttributeIntegerDAO anInteger = MdAttributeIntegerDAO.newInstance();
    anInteger.setValue(MdAttributeIntegerInfo.NAME, "queryInteger");
    anInteger.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "An Integer");
    anInteger.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, collection.getOid());
    anInteger.apply();

    MdAttributeLongDAO aLong = MdAttributeLongDAO.newInstance();
    aLong.setValue(MdAttributeLongInfo.NAME, "queryLong");
    aLong.setStructValue(MdAttributeLongInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Long");
    aLong.setValue(MdAttributeLongInfo.DEFINING_MD_CLASS, collection.getOid());
    aLong.apply();

    MdAttributeDateDAO aDate = MdAttributeDateDAO.newInstance();
    aDate.setValue(MdAttributeDateInfo.NAME, "queryDate");
    aDate.setStructValue(MdAttributeDateInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Date");
    aDate.setValue(MdAttributeDateInfo.DEFINING_MD_CLASS, collection.getOid());
    aDate.apply();

    MdAttributeDateTimeDAO aDateTime = MdAttributeDateTimeDAO.newInstance();
    aDateTime.setValue(MdAttributeDateTimeInfo.NAME, "queryDateTime");
    aDateTime.setStructValue(MdAttributeDateTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A DateTime");
    aDateTime.setValue(MdAttributeDateTimeInfo.DEFINING_MD_CLASS, collection.getOid());
    aDateTime.apply();

    MdAttributeTimeDAO aTime = MdAttributeTimeDAO.newInstance();
    aTime.setValue(MdAttributeTimeInfo.NAME, "queryTime");
    aTime.setStructValue(MdAttributeTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Time");
    aTime.setValue(MdAttributeTimeInfo.DEFINING_MD_CLASS, collection.getOid());
    aTime.apply();

    MdAttributeTextDAO aText = MdAttributeTextDAO.newInstance();
    aText.setValue(MdAttributeTextInfo.NAME, "queryText");
    aText.setStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Text");
    aText.setValue(MdAttributeTextInfo.DEFINING_MD_CLASS, collection.getOid());
    aText.apply();

    MdAttributeClobDAO aClob = MdAttributeClobDAO.newInstance();
    aClob.setValue(MdAttributeTextInfo.NAME, "queryClob");
    aClob.setStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Clob");
    aClob.setValue(MdAttributeTextInfo.DEFINING_MD_CLASS, collection.getOid());
    aClob.apply();

    testStructQueryObject = StructDAO.newInstance(collection.definesType());
    testStructQueryObject.addItem("queryEnumeration", clubsId);
    testStructQueryObject.addItem("queryEnumeration", heartsId);
    testStructQueryObject.setValue("queryBoolean", MdAttributeBooleanInfo.TRUE);
    testStructQueryObject.setValue("queryCharacter", "some character value");
    testStructQueryObject.setValue("queryText", "some text value");
    testStructQueryObject.setValue("queryClob", "some clob value");
    testStructQueryObject.setValue("queryDateTime", "2006-12-06 13:00:00");
    testStructQueryObject.setValue("queryDate", "2006-12-06");
    testStructQueryObject.setValue("queryTime", "13:00:00");
    testStructQueryObject.setValue("queryInteger", "100");
    testStructQueryObject.setValue("queryLong", "100");
    testStructQueryObject.setValue("queryFloat", "100.5");
    testStructQueryObject.setValue("queryDecimal", "100.5");
    testStructQueryObject.setValue("queryDouble", "100.5");
    testStructQueryObject.apply();
  }

  /**
   * The tear down done after all the test in the test suite have run
   */
  @Request
  @AfterClass
  public static void classTearDown()
  {
    collection.delete();
    suitEnum.delete();
    suitMaster.delete();
  }

  /**
   * Tests a query based on values for an AttributeBoolean using strings.
   */
  @Request
  @Test
  public void testBooleanEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aBoolean("queryBoolean").EQ(MdAttributeBooleanInfo.TRUE));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute boolean values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aBoolean("queryBoolean").EQ(MdAttributeBooleanInfo.FALSE));

      iterator = query.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute boolean values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  /**
   * Tests a query based on values for an AttributeBoolean using booleans.
   */
  @Request
  @Test
  public void testBooleanEqBoolean()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aBoolean("queryBoolean").EQ(true));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute boolean values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aBoolean("queryBoolean").EQ(false));

      iterator = query.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute boolean values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  /**
   * Tests a query based on values for an AttributeBoolean using booleans.
   */

  @Request
  @Test
  public void testBooleanEqBoolean_Generated()
  {
    try
    {
      try
      {
        String type = collection.definesType();
        Class<?> objectClass = LoaderDecorator.load(type);
        String queryType = EntityQueryAPIGenerator.getQueryClass(type);
        Class<?> queryClass = LoaderDecorator.load(queryType);

        QueryFactory factory = new QueryFactory();
        Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
        SelectableBoolean attributeBoolean = (SelectableBoolean) queryClass.getMethod("getQueryBoolean").invoke(queryObject);
        queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeBoolean.EQ(true));

        // Load the iterator class
        Class<?> iteratorClass = OIterator.class;
        Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

        Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

        if (!hasNext)
        {
          Assert.fail("A query did not return any results when it should have");
        }

        for (Object object : (Iterable<?>) resultIterator)
        {
          objectClass.cast(object);
          String objectId = (String) objectClass.getMethod("getOid").invoke(object);
          if (!objectId.equals(testStructQueryObject.getOid()))
          {
            Assert.fail("The objects returned by a query based on attribute boolean values are incorrect.");
          }
        }

        queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
        attributeBoolean = (SelectableBoolean) queryClass.getMethod("getQueryBoolean").invoke(queryObject);
        queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeBoolean.EQ(false));

        resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

        hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
        if (hasNext)
        {
          iteratorClass.getMethod("close").invoke(resultIterator);
          Assert.fail("A query based on attribute boolean values returned objects when it shouldn't have.");
        }
      }
      catch (Exception e)
      {
        Assert.fail(e.getMessage());
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  /**
   * Tests a query based on values for an AttributeBoolean using booleans.
   */
  @Request
  @Test
  public void testBooleanNotEqBoolean()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aBoolean("queryBoolean").NE(false));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute boolean values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aBoolean("queryBoolean").NE(true));

      iterator = query.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute boolean values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  /**
   * Tests a query based on values for an AttributeBoolean using booleans.
   */

  @Request
  @Test
  public void testBooleanNotEqBoolean_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableBoolean attributeBoolean = (SelectableBoolean) queryClass.getMethod("getQueryBoolean").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeBoolean.NE(false));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute boolean values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeBoolean = (SelectableBoolean) queryClass.getMethod("getQueryBoolean").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeBoolean.NE(true));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute boolean values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  /**
   * Tests a query based on values for an AttributeBoolean using strings.
   */
  @Request
  @Test
  public void testBooleanNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aBoolean("queryBoolean").NE(MdAttributeBooleanInfo.FALSE));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute boolean values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aBoolean("queryBoolean").NE(MdAttributeBooleanInfo.TRUE));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute boolean values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testCharacterEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aCharacter("queryCharacter").EQ("some character value"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aCharacter("queryCharacter").EQ("wrong character value"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testCharacterEqString_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeCharacter attributeCharacter = (AttributeCharacter) queryClass.getMethod("getQueryCharacter").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.EQ("some character value"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on character values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeCharacter = (AttributeCharacter) queryClass.getMethod("getQueryCharacter").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.EQ("wrong character value"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testCharacterEqIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aCharacter("queryCharacter").EQi("SOME CHARACTER VALUE"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aCharacter("queryCharacter").EQi("WRONG CHARACTER VALUE"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testCharacterEqIgnoreCaseString_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeCharacter attributeCharacter = (AttributeCharacter) queryClass.getMethod("getQueryCharacter").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.EQi("SOME CHARACTER VALUE"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on character values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeCharacter = (AttributeCharacter) queryClass.getMethod("getQueryCharacter").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.EQi("WRONG CHARACTER VALUE"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testCharacterInStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aCharacter("queryCharacter").IN("wrong value 1", "some character value", "wrong value 2"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aCharacter("queryCharacter").IN("wrong value 1", "wrong value 2", "wrong value 3"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testCharacterInStringArray_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeCharacter attributeCharacter = (AttributeCharacter) queryClass.getMethod("getQueryCharacter").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.IN("wrong value 1", "some character value", "wrong value 2"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on character values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeCharacter = (AttributeCharacter) queryClass.getMethod("getQueryCharacter").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.IN("wrong value 1", "wrong value 2", "wrong value 3"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testCharacterInIgnoreCaseStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aCharacter("queryCharacter").INi("WRONG VALUE 1", "SOME CHARACTER VALUE", "WRONG VALUE 2"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aCharacter("queryCharacter").INi("WRONG VALUE 1", "WRONG VALUE 2", "WRONG VALUE 3"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testCharacterInIgnoreCaseStringArray_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeCharacter attributeCharacter = (AttributeCharacter) queryClass.getMethod("getQueryCharacter").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.INi("WRONG VALUE 1", "SOME CHARACTER VALUE", "WRONG VALUE 2"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on character values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeCharacter = (AttributeCharacter) queryClass.getMethod("getQueryCharacter").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.INi("WRONG VALUE 1", "WRONG VALUE 2", "WRONG VALUE 3"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testCharacterLikeString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aCharacter("queryCharacter").LIKE("%character%"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aCharacter("queryCharacter").LIKE("%character"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testCharacterLikeString_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeCharacter attributeCharacter = (AttributeCharacter) queryClass.getMethod("getQueryCharacter").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.LIKE("%character%"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on character values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeCharacter = (AttributeCharacter) queryClass.getMethod("getQueryCharacter").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.LIKE("%character"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testCharacterLikeIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aCharacter("queryCharacter").LIKEi("%CHARACTER%"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aCharacter("queryCharacter").LIKEi("%CHARACTER"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testCharacterLikeIgnoreCaseString_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeCharacter attributeCharacter = (AttributeCharacter) queryClass.getMethod("getQueryCharacter").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.LIKEi("%CHARACTER%"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on character values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeCharacter = (AttributeCharacter) queryClass.getMethod("getQueryCharacter").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.LIKEi("%CHARACTER"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testCharacterNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aCharacter("queryCharacter").NE("wrong character value"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aCharacter("queryCharacter").NE("some character value"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testCharacterNotEqString_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeCharacter attributeCharacter = (AttributeCharacter) queryClass.getMethod("getQueryCharacter").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.NE("wrong character value"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on character values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeCharacter = (AttributeCharacter) queryClass.getMethod("getQueryCharacter").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.NE("some character value"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testCharacterNotEqIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aCharacter("queryCharacter").NEi("WRONG CHARACTER STRING"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aCharacter("queryCharacter").NEi("SOME CHARACTER VALUE"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testCharacterNotEqIgnoreCaseString_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeCharacter attributeCharacter = (AttributeCharacter) queryClass.getMethod("getQueryCharacter").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.NEi("WRONG CHARACTER STRING"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on character values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeCharacter = (AttributeCharacter) queryClass.getMethod("getQueryCharacter").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.NEi("SOME CHARACTER VALUE"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testCharacterNotInStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aCharacter("queryCharacter").NI("wrong 1", "wrong 2", "wrong 3"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aCharacter("queryCharacter").NI("wrong 1", "some character value", "wrong 2"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testCharacterNotInStringArray_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeCharacter attributeCharacter = (AttributeCharacter) queryClass.getMethod("getQueryCharacter").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.NI("wrong 1", "wrong 2", "wrong 3"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on character values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeCharacter = (AttributeCharacter) queryClass.getMethod("getQueryCharacter").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.NI("wrong 1", "some character value", "wrong 2"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testCharacterNotInIgnoreCaseStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aCharacter("queryCharacter").NIi("WRONG 1", "WRONG 2", "WRONG 3"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aCharacter("queryCharacter").NIi("WRONG 1", "SOME CHARACTER VALUE", "WRONG 2"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testCharacterNotInIgnoreCaseStringArray_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeCharacter attributeCharacter = (AttributeCharacter) queryClass.getMethod("getQueryCharacter").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.NIi("WRONG 1", "WRONG 2", "WRONG 3"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on character values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeCharacter = (AttributeCharacter) queryClass.getMethod("getQueryCharacter").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.NIi("WRONG 1", "SOME CHARACTER VALUE", "WRONG 2"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testCharacterNotLikeString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aCharacter("queryCharacter").NLIKE("%wrong%"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aCharacter("queryCharacter").NLIKE("%character%"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testCharacterNotLikeString_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeCharacter attributeCharacter = (AttributeCharacter) queryClass.getMethod("getQueryCharacter").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.NLIKE("%wrong%"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on character values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeCharacter = (AttributeCharacter) queryClass.getMethod("getQueryCharacter").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.NLIKE("%character%"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testCharacterNotLikeIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aCharacter("queryCharacter").NLIKEi("%WRONG%"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aCharacter("queryCharacter").NLIKEi("%CHARACTER%"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testCharacterNotLikeIgnoreCaseString_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeCharacter attributeCharacter = (AttributeCharacter) queryClass.getMethod("getQueryCharacter").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.NLIKEi("%WRONG%"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on character values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeCharacter = (AttributeCharacter) queryClass.getMethod("getQueryCharacter").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.NLIKEi("%CHARACTER%"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTextEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aText("queryText").EQ("some text value"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aText("queryText").EQ("wrong text value"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testClobEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aClob("queryClob").EQ("some clob value"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aClob("queryClob").EQ("wrong clob value"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTextEqString_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeText attributeText = (AttributeText) queryClass.getMethod("getQueryText").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.EQ("some text value"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeText = (AttributeText) queryClass.getMethod("getQueryText").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.EQ("wrong text value"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testClobEqString_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeClob attributeClob = (AttributeClob) queryClass.getMethod("getQueryClob").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.EQ("some clob value"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeClob = (AttributeClob) queryClass.getMethod("getQueryClob").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.EQ("wrong clob value"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTextEqIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aText("queryText").EQi("SOME TEXT VALUE"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aText("queryText").EQi("WRONG TEXT VALUE"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testClobEqIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aClob("queryClob").EQi("SOME CLOB VALUE"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aClob("queryClob").EQi("WRONG CLOB VALUE"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTextEqIgnoreCaseString_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeText attributeText = (AttributeText) queryClass.getMethod("getQueryText").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.EQi("SOME TEXT VALUE"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeText = (AttributeText) queryClass.getMethod("getQueryText").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.EQi("WRONG TEXT VALUE"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testClobEqIgnoreCaseString_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeClob attributeClob = (AttributeClob) queryClass.getMethod("getQueryClob").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.EQi("SOME CLOB VALUE"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeClob = (AttributeClob) queryClass.getMethod("getQueryClob").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.EQi("WRONG CLOB VALUE"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTextInStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aText("queryText").IN("wrong value 1", "some text value", "wrong value 2"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aText("queryText").IN("wrong value 1", "wrong value 2", "wrong value 3"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testClobInStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aClob("queryClob").IN("wrong value 1", "some clob value", "wrong value 2"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aClob("queryClob").IN("wrong value 1", "wrong value 2", "wrong value 3"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTextInStringArray_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeText attributeText = (AttributeText) queryClass.getMethod("getQueryText").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.IN("wrong value 1", "some text value", "wrong value 2"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeText = (AttributeText) queryClass.getMethod("getQueryText").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.IN("wrong value 1", "wrong value 2", "wrong value 3"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testClobInStringArray_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeClob attributeClob = (AttributeClob) queryClass.getMethod("getQueryClob").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.IN("wrong value 1", "some clob value", "wrong value 2"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeClob = (AttributeClob) queryClass.getMethod("getQueryClob").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.IN("wrong value 1", "wrong value 2", "wrong value 3"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTextInIgnoreCaseStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aText("queryText").INi("WRONG VALUE 1", "SOME TEXT VALUE", "WRONG VALUE 2"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aText("queryText").INi("WRONG VALUE 1", "WRONG VALUE 2", "WRONG VALUE 3"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testClobInIgnoreCaseStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aClob("queryClob").INi("WRONG VALUE 1", "SOME CLOB VALUE", "WRONG VALUE 2"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aClob("queryClob").INi("WRONG VALUE 1", "WRONG VALUE 2", "WRONG VALUE 3"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTextInIgnoreCaseStringArray_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeText attributeText = (AttributeText) queryClass.getMethod("getQueryText").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.INi("WRONG VALUE 1", "SOME TEXT VALUE", "WRONG VALUE 2"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeText = (AttributeText) queryClass.getMethod("getQueryText").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.INi("WRONG VALUE 1", "WRONG VALUE 2", "WRONG VALUE 3"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testClobInIgnoreCaseStringArray_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeClob attributeClob = (AttributeClob) queryClass.getMethod("getQueryClob").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.INi("WRONG VALUE 1", "SOME CLOB VALUE", "WRONG VALUE 2"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeClob = (AttributeClob) queryClass.getMethod("getQueryClob").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.INi("WRONG VALUE 1", "WRONG VALUE 2", "WRONG VALUE 3"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTextLikeString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aText("queryText").LIKE("%text%"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aText("queryText").LIKE("%text"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testClobLikeString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aClob("queryClob").LIKE("%clob%"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aClob("queryClob").LIKE("%clob"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTextLikeString_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeText attributeText = (AttributeText) queryClass.getMethod("getQueryText").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.LIKE("%text%"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeText = (AttributeText) queryClass.getMethod("getQueryText").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.LIKE("%text"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testClobLikeString_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeClob attributeClob = (AttributeClob) queryClass.getMethod("getQueryClob").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.LIKE("%clob%"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeClob = (AttributeClob) queryClass.getMethod("getQueryClob").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.LIKE("%clob"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTextLikeIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aText("queryText").LIKEi("%TEXT%"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aText("queryText").LIKEi("%TEXT"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testClobLikeIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aClob("queryClob").LIKEi("%CLOB%"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aClob("queryClob").LIKEi("%CLOB"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTextLikeIgnoreCaseString_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeText attributeText = (AttributeText) queryClass.getMethod("getQueryText").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.LIKEi("%TEXT%"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeText = (AttributeText) queryClass.getMethod("getQueryText").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.LIKEi("%TEXT"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testClobLikeIgnoreCaseString_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeClob attributeClob = (AttributeClob) queryClass.getMethod("getQueryClob").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.LIKEi("%CLOB%"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeClob = (AttributeClob) queryClass.getMethod("getQueryClob").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.LIKEi("%CLOB"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTextNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aText("queryText").NE("wrong text value"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aText("queryText").NE("some text value"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testClobNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aClob("queryClob").NE("wrong clob value"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aClob("queryClob").NE("some clob value"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTextNotEqString_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeText attributeText = (AttributeText) queryClass.getMethod("getQueryText").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NE("wrong text value"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeText = (AttributeText) queryClass.getMethod("getQueryText").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NE("some text value"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testClobNotEqString_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeClob attributeClob = (AttributeClob) queryClass.getMethod("getQueryClob").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NE("wrong clob value"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeClob = (AttributeClob) queryClass.getMethod("getQueryClob").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NE("some clob value"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTextNotEqIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aText("queryText").NEi("WRONG TEXT STRING"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aText("queryText").NEi("SOME TEXT VALUE"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testClobNotEqIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aClob("queryClob").NEi("WRONG CLOB STRING"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aClob("queryClob").NEi("SOME CLOB VALUE"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTextNotEqIgnoreCaseString_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeText attributeText = (AttributeText) queryClass.getMethod("getQueryText").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NEi("WRONG TEXT STRING"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeText = (AttributeText) queryClass.getMethod("getQueryText").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NEi("SOME TEXT VALUE"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testClobNotEqIgnoreCaseString_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeClob attributeClob = (AttributeClob) queryClass.getMethod("getQueryClob").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NEi("WRONG CLOB STRING"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeClob = (AttributeClob) queryClass.getMethod("getQueryClob").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NEi("SOME CLOB VALUE"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTextNotInStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aText("queryText").NI("wrong 1", "wrong 2", "wrong 3"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aText("queryText").NI("wrong 1", "some text value", "wrong 2"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testClobNotInStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aClob("queryClob").NI("wrong 1", "wrong 2", "wrong 3"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aClob("queryClob").NI("wrong 1", "some clob value", "wrong 2"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTextNotInStringArray_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeText attributeText = (AttributeText) queryClass.getMethod("getQueryText").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NI("wrong 1", "wrong 2", "wrong 3"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeText = (AttributeText) queryClass.getMethod("getQueryText").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NI("wrong 1", "some text value", "wrong 2"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testClobNotInStringArray_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeClob attributeClob = (AttributeClob) queryClass.getMethod("getQueryClob").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NI("wrong 1", "wrong 2", "wrong 3"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeClob = (AttributeClob) queryClass.getMethod("getQueryClob").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NI("wrong 1", "some clob value", "wrong 2"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTextNotInIgnoreCaseStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aText("queryText").NIi("WRONG 1", "WRONG 2", "WRONG 3"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aText("queryText").NIi("WRONG 1", "SOME TEXT VALUE", "WRONG 2"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testClobNotInIgnoreCaseStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aClob("queryClob").NIi("WRONG 1", "WRONG 2", "WRONG 3"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aClob("queryClob").NIi("WRONG 1", "SOME CLOB VALUE", "WRONG 2"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testClobNotInIgnoreCaseStringArray_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeClob attributeClob = (AttributeClob) queryClass.getMethod("getQueryClob").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NIi("WRONG 1", "WRONG 2", "WRONG 3"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeClob = (AttributeClob) queryClass.getMethod("getQueryClob").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NIi("WRONG 1", "SOME CLOB VALUE", "WRONG 2"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTextNotLikeString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aText("queryText").NLIKE("%wrong%"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aText("queryText").NLIKE("%text%"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testClobNotLikeString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aClob("queryClob").NLIKE("%wrong%"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aClob("queryClob").NLIKE("%clob%"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTextNotLikeString_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeText attributeText = (AttributeText) queryClass.getMethod("getQueryText").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NLIKE("%wrong%"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeText = (AttributeText) queryClass.getMethod("getQueryText").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NLIKE("%text%"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testClobNotLikeString_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeClob attributeClob = (AttributeClob) queryClass.getMethod("getQueryClob").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NLIKE("%wrong%"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeClob = (AttributeClob) queryClass.getMethod("getQueryClob").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NLIKE("%clob%"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTextNotLikeIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aText("queryText").NLIKEi("%WRONG%"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aText("queryText").NLIKEi("%TEXT%"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testClobNotLikeIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aClob("queryClob").NLIKEi("%WRONG%"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aClob("queryClob").NLIKEi("%CLOB%"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTextNotLikeIgnoreCaseString_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeText attributeText = (AttributeText) queryClass.getMethod("getQueryText").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NLIKEi("%WRONG%"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeText = (AttributeText) queryClass.getMethod("getQueryText").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NLIKEi("%TEXT%"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testClobNotLikeIgnoreCaseString_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeClob attributeClob = (AttributeClob) queryClass.getMethod("getQueryClob").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NLIKEi("%WRONG%"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeClob = (AttributeClob) queryClass.getMethod("getQueryClob").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NLIKEi("%CLOB%"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateTimeEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDateTime("queryDateTime").EQ("2006-12-06 13:00:00"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute datetime values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDateTime("queryDateTime").EQ("2006-05-05 13:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateTimeEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-06 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDateTime("queryDateTime").EQ(date));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-05-05 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDateTime("queryDateTime").EQ(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateTimeEq_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-06 13:00:00", new java.text.ParsePosition(0));

      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeDateTime = (SelectableMoment) queryClass.getMethod("getQueryDateTime").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.EQ(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-05-05 13:00:00", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeDateTime = (SelectableMoment) queryClass.getMethod("getQueryDateTime").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.EQ(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateTimeGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDateTime("queryDateTime").GT("2006-12-05 13:00:00"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute datetime values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDateTime("queryDateTime").GT("2006-12-07 13:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateTimeGt()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-05 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDateTime("queryDateTime").GT("2006-12-05 13:00:00"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-07 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDateTime("queryDateTime").GT(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateTimeGt_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-05 13:00:00", new java.text.ParsePosition(0));

      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeDateTime = (SelectableMoment) queryClass.getMethod("getQueryDateTime").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.GT(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-07 13:00:00", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeDateTime = (SelectableMoment) queryClass.getMethod("getQueryDateTime").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.GT(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateTimeGtEqString()
  {
    try
    {
      // perform a query that WILL find a based on equals
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDateTime("queryDateTime").GE("2006-12-06 13:00:00"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute datetime values are incorrect.");
        }
      }

      // perform a query that WILL find a based on greater than
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDateTime("queryDateTime").GE("2006-12-05 13:00:00"));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute datetime values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDateTime("queryDateTime").GE("2006-12-07 13:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateTimeGtEq()
  {
    // "2006-12-05 13:00:00"

    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-06 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDateTime("queryDateTime").GE(date));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-05 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on greater than
      factory = new QueryFactory();
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDateTime("queryDateTime").GE(date));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-07 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDateTime("queryDateTime").GE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateTimeGtEq_Generated()
  {
    try
    {
      // find a match based on equals
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-06 13:00:00", new java.text.ParsePosition(0));

      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeDateTime = (SelectableMoment) queryClass.getMethod("getQueryDateTime").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.GE(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on datetime values are incorrect.");
        }
      }

      // find a match based on greater than
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-05 13:00:00", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDateTime = (SelectableMoment) queryClass.getMethod("getQueryDateTime").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.GE(date));

      // Load the iterator class
      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-07 13:00:00", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeDateTime = (SelectableMoment) queryClass.getMethod("getQueryDateTime").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.GE(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateTimeLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDateTime("queryDateTime").LT("2006-12-07 13:00:00"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute datetime values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDateTime("queryDateTime").LT("2006-12-05 13:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateTimeLt()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-07 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDateTime("queryDateTime").LT(date));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-05 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDateTime("queryDateTime").LT(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateTimeLt_Generation()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-07 13:00:00", new java.text.ParsePosition(0));

      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeDateTime = (SelectableMoment) queryClass.getMethod("getQueryDateTime").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.LT(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-05 13:00:00", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeDateTime = (SelectableMoment) queryClass.getMethod("getQueryDateTime").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.LT(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateTimeLtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDateTime("queryDateTime").LE("2006-12-06 13:00:00"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute datetime values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less then
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDateTime("queryDateTime").LE("2006-12-07 13:00:00"));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute datetime values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDateTime("queryDateTime").LE("2006-12-05 13:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateTimeLtEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-06 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDateTime("queryDateTime").LE(date));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-07 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on less than
      factory = new QueryFactory();
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDateTime("queryDateTime").LE(date));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-05 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDateTime("queryDateTime").LE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateTimeLtEq_Generated()
  {
    try
    {
      // find a match based on equals
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-06 13:00:00", new java.text.ParsePosition(0));

      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeDateTime = (SelectableMoment) queryClass.getMethod("getQueryDateTime").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.LE(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on datetime values are incorrect.");
        }
      }

      // find a match based on less than
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-07 13:00:00", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDateTime = (SelectableMoment) queryClass.getMethod("getQueryDateTime").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.LE(date));

      // Load the iterator class
      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-05 13:00:00", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeDateTime = (SelectableMoment) queryClass.getMethod("getQueryDateTime").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.LE(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateTimeNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDateTime("queryDateTime").NE("2006-12-05 13:00:00"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute datetime values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDateTime("queryDateTime").NE("2006-12-06 13:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateTimeNotEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-05 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDateTime("queryDateTime").NE("2006-12-05 13:00:00"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-06 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDateTime("queryDateTime").NE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateTimeNotEq_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-05 13:00:00", new java.text.ParsePosition(0));

      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeDateTime = (SelectableMoment) queryClass.getMethod("getQueryDateTime").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.NE(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-06 13:00:00", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeDateTime = (SelectableMoment) queryClass.getMethod("getQueryDateTime").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.NE(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDate("queryDate").EQ("2006-12-06"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute date values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDate("queryDate").EQ("2006-05-05"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-06", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDate("queryDate").EQ(date));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-05-05", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDate("queryDate").EQ(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateEq_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-06", new java.text.ParsePosition(0));

      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeDate = (SelectableMoment) queryClass.getMethod("getQueryDate").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.EQ(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-05-05", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeDate = (SelectableMoment) queryClass.getMethod("getQueryDate").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.EQ(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDate("queryDate").GT("2006-12-05"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute date values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDate("queryDate").GT("2006-12-07"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateGt()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-05", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDate("queryDate").GT(date));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-07", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDate("queryDate").GT(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateGt_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-05", new java.text.ParsePosition(0));

      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeDate = (SelectableMoment) queryClass.getMethod("getQueryDate").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.GT(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-07", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeDate = (SelectableMoment) queryClass.getMethod("getQueryDate").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.GT(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateGtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDate("queryDate").GE("2006-12-06"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute date values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDate("queryDate").GE("2006-12-05"));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute date values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDate("queryDate").GE("2006-12-07"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateGtEq()
  {
    try
    {
      // find a match based on equals
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-06", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDate("queryDate").GE(date));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute date values are incorrect.");
        }
      }

      // find a match gased on less than
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-05", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      factory = new QueryFactory();
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDate("queryDate").GE(date));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-07", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDate("queryDate").GE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateGtEq_Generated()
  {
    try
    {
      // find a match based on equals
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-06", new java.text.ParsePosition(0));

      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeDate = (SelectableMoment) queryClass.getMethod("getQueryDate").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.GE(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on date values are incorrect.");
        }
      }

      // find a match based on less than
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-05", new java.text.ParsePosition(0));

      factory = new QueryFactory();

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDate = (SelectableMoment) queryClass.getMethod("getQueryDate").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.GE(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-07", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeDate = (SelectableMoment) queryClass.getMethod("getQueryDate").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.GE(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDate("queryDate").LT("2006-12-07"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute date values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDate("queryDate").LT("2006-12-05"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateLt()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-07", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDate("queryDate").LT(date));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-05", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDate("queryDate").LT(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateLt_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-07", new java.text.ParsePosition(0));

      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeDate = (SelectableMoment) queryClass.getMethod("getQueryDate").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.LT(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-05", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeDate = (SelectableMoment) queryClass.getMethod("getQueryDate").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.LT(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateLtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDate("queryDate").LE("2006-12-06"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute date values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDate("queryDate").LE("2006-12-07"));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute date values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDate("queryDate").LE("2006-12-05"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateLtEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-06", new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDate("queryDate").LE(date));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-07", new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on less than
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDate("queryDate").LE(date));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-05", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDate("queryDate").LE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateLtEq_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-06", new java.text.ParsePosition(0));

      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeDate = (SelectableMoment) queryClass.getMethod("getQueryDate").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.LE(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on date values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-07", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDate = (SelectableMoment) queryClass.getMethod("getQueryDate").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.LE(date));

      // Load the iterator class
      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-05", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeDate = (SelectableMoment) queryClass.getMethod("getQueryDate").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.LE(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDate("queryDate").NE("2006-12-05"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute date values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDate("queryDate").NE("2006-12-06"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateNotEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-05", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDate("queryDate").NE(date));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-06", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDate("queryDate").NE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDateNotEq_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-05", new java.text.ParsePosition(0));

      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeDate = (SelectableMoment) queryClass.getMethod("getQueryDate").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.NE(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-06", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeDate = (SelectableMoment) queryClass.getMethod("getQueryDate").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.NE(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTimeEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aTime("queryTime").EQ("13:00:00"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute time values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aTime("queryTime").EQ("12:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTimeEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aTime("queryTime").EQ(date));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aTime("queryTime").EQ(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTimeEq_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeTime = (SelectableMoment) queryClass.getMethod("getQueryTime").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeTime.EQ(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on Time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeTime = (SelectableMoment) queryClass.getMethod("getQueryTime").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeTime.EQ(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute Time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTimeGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aTime("queryTime").GT("12:00:00"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute time values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aTime("queryTime").GT("14:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTimeGt()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aTime("queryTime").GT(date));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aTime("queryTime").GT(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTimeGt_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeTime = (SelectableMoment) queryClass.getMethod("getQueryTime").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeTime.GT(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on Time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeTime = (SelectableMoment) queryClass.getMethod("getQueryTime").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeTime.GT(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute Time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTimeGtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aTime("queryTime").GE("13:00:00"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute date values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aTime("queryTime").GE("12:00:00"));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute date values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aTime("queryTime").GE("14:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTimeGtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aTime("queryTime").GE(date));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute date values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aTime("queryTime").GE(date));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aTime("queryTime").GE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTimeGtEq_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeTime = (SelectableMoment) queryClass.getMethod("getQueryTime").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeTime.GE(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on Time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeTime = (SelectableMoment) queryClass.getMethod("getQueryTime").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeTime.GE(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on Time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeTime = (SelectableMoment) queryClass.getMethod("getQueryTime").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeTime.GE(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute Time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTimeLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aTime("queryTime").LT("14:00:00"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute time values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aTime("queryTime").LT("12:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTimeLt()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aTime("queryTime").LT(date));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aTime("queryTime").LT(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTimeLt_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeTime = (SelectableMoment) queryClass.getMethod("getQueryTime").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeTime.LT(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on Time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeTime = (SelectableMoment) queryClass.getMethod("getQueryTime").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeTime.LT(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute Time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTimeLtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aTime("queryTime").LE("13:00:00"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute time values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aTime("queryTime").LE("14:00:00"));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute time values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aTime("queryTime").LE("12:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTimeLtEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aTime("queryTime").LE(date));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute time values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aTime("queryTime").LE(date));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aTime("queryTime").LE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTimeLtEq_Generated()
  {
    try
    {
      // Find a match based on equals
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeTime = (SelectableMoment) queryClass.getMethod("getQueryTime").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeTime.LE(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on Time values are incorrect.");
        }
      }

      // Find a match based on less than
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeTime = (SelectableMoment) queryClass.getMethod("getQueryTime").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeTime.LE(date));

      // Load the iterator class
      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on Time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeTime = (SelectableMoment) queryClass.getMethod("getQueryTime").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeTime.LE(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute Time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTimeNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aTime("queryTime").NE("12:00:00"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute time values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aTime("queryTime").NE("13:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTimeNotEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aTime("queryTime").NE(date));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aTime("queryTime").NE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTimeNotEq_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeTime = (SelectableMoment) queryClass.getMethod("getQueryTime").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeTime.NE(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on Time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeTime = (SelectableMoment) queryClass.getMethod("getQueryTime").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeTime.NE(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute Time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testIntegerEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aInteger("queryInteger").EQ("100"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aInteger("queryInteger").EQ("101"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testIntegerEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aInteger("queryInteger").EQ(100));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aInteger("queryInteger").EQ(101));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testIntegerEq_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger attributeInteger = (SelectableInteger) queryClass.getMethod("getQueryInteger").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.EQ(100));

      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on integer values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      ;
      attributeInteger = (SelectableInteger) queryClass.getMethod("getQueryInteger").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.EQ(101));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testIntegerGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aInteger("queryInteger").GT("99"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aInteger("queryInteger").GT("101"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testIntegerGt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aInteger("queryInteger").GT(99));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aInteger("queryInteger").GT(101));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testIntegerGt_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger attributeInteger = (SelectableInteger) queryClass.getMethod("getQueryInteger").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.GT(99));

      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on integer values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      ;
      attributeInteger = (SelectableInteger) queryClass.getMethod("getQueryInteger").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.GT(101));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testIntegerGtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aInteger("queryInteger").GE("100"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute integer values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aInteger("queryInteger").GE("99"));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aInteger("queryInteger").GE("101"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testIntegerGtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aInteger("queryInteger").GE(100));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute integer values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aInteger("queryInteger").GE(99));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aInteger("queryInteger").GE(101));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testIntegerGtEq_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      // perform a query that WILL find a match based on equals
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger attributeInteger = (SelectableInteger) queryClass.getMethod("getQueryInteger").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.GE(100));

      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on integer values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeInteger = (SelectableInteger) queryClass.getMethod("getQueryInteger").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.GE(99));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on integer values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      ;
      attributeInteger = (SelectableInteger) queryClass.getMethod("getQueryInteger").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.GE(101));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testIntegerLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aInteger("queryInteger").LT("101"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aInteger("queryInteger").LT("100"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testIntegerLt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aInteger("queryInteger").LT(101));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aInteger("queryInteger").LT(100));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testIntegerLt_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger attributeInteger = (SelectableInteger) queryClass.getMethod("getQueryInteger").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.LT(101));

      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on integer values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      ;
      attributeInteger = (SelectableInteger) queryClass.getMethod("getQueryInteger").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.LT(100));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testIntegerLtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aInteger("queryInteger").LE("100"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute integer values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aInteger("queryInteger").LE("101"));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aInteger("queryInteger").LE("99"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testIntegerLtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aInteger("queryInteger").LE(100));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute integer values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aInteger("queryInteger").LE(101));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aInteger("queryInteger").LE(99));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testIntegerLtEq_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();
      // perform a query that WILL find a match based on equals
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger attributeInteger = (SelectableInteger) queryClass.getMethod("getQueryInteger").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.LE(100));

      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on integer values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeInteger = (SelectableInteger) queryClass.getMethod("getQueryInteger").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.LE(101));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on integer values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      ;
      attributeInteger = (SelectableInteger) queryClass.getMethod("getQueryInteger").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.LE(99));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testIntegerNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aInteger("queryInteger").NE("101"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aInteger("queryInteger").NE("100"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testIntegerNotEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aInteger("queryInteger").NE(101));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aInteger("queryInteger").NE(100));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testIntegerNotEq_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger attributeInteger = (SelectableInteger) queryClass.getMethod("getQueryInteger").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.NE(101));

      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on integer values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      ;
      attributeInteger = (SelectableInteger) queryClass.getMethod("getQueryInteger").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.NE(100));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testLongEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aLong("queryLong").EQ("100"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aLong("queryLong").EQ("101"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testLongEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aLong("queryLong").EQ((long) 100));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aLong("queryLong").EQ((long) 101));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testLongEq_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong) queryClass.getMethod("getQueryLong").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.EQ((long) 100));

      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on long values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeLong = (SelectableLong) queryClass.getMethod("getQueryLong").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.EQ((long) 101));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testLongGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aLong("queryLong").GT("99"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aLong("queryLong").GT("101"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testLongGt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aLong("queryLong").GT((long) 99));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aLong("queryLong").GT((long) 101));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testLongGt_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong) queryClass.getMethod("getQueryLong").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GT((long) 99));

      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on long values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeLong = (SelectableLong) queryClass.getMethod("getQueryLong").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GT((long) 101));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testLongGtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aLong("queryLong").GE("100"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute long values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aLong("queryLong").GE("99"));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aLong("queryLong").GE("101"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testLongGtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aLong("queryLong").GE((long) 100));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute long values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aLong("queryLong").GE((long) 99));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aLong("queryLong").GE((long) 101));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testLongGtEq_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      // perform a query that WILL find a match based on equals
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong) queryClass.getMethod("getQueryLong").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GE((long) 100));

      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on long values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeLong = (SelectableLong) queryClass.getMethod("getQueryLong").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GE((long) 99));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on long values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeLong = (SelectableLong) queryClass.getMethod("getQueryLong").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GE((long) 101));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testLongLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aLong("queryLong").LT("101"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aLong("queryLong").LT("100"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testLongLt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aLong("queryLong").LT((long) 101));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aLong("queryLong").LT((long) 100));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testLongLt_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong) queryClass.getMethod("getQueryLong").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.LT((long) 101));

      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on long values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeLong = (SelectableLong) queryClass.getMethod("getQueryLong").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.LT((long) 100));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testLongLtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aLong("queryLong").LE("100"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute long values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aLong("queryLong").LE("101"));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aLong("queryLong").LE("99"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testLongLtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aLong("queryLong").LE((long) 100));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute long values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aLong("queryLong").LE((long) 101));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aLong("queryLong").LE((long) 99));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testLongLtEq_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      // perform a query that WILL find a match based on equals
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong) queryClass.getMethod("getQueryLong").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.LE((long) 100));

      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on long values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeLong = (SelectableLong) queryClass.getMethod("getQueryLong").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.LE((long) 101));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on long values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeLong = (SelectableLong) queryClass.getMethod("getQueryLong").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.LE((long) 99));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testLongNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aLong("queryLong").NE("101"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aLong("queryLong").NE("100"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testLongNotEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aLong("queryLong").NE((long) 101));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aLong("queryLong").NE((long) 100));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testLongNotEq_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong) queryClass.getMethod("getQueryLong").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.NE((long) 101));

      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on long values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeLong = (SelectableLong) queryClass.getMethod("getQueryLong").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.NE((long) 100));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aFloat("queryFloat").EQ("100.5"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aFloat("queryFloat").EQ("101.5"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aFloat("queryFloat").EQ((float) 100.5));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aFloat("queryFloat").EQ((float) 101.5));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatEq_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableFloat attributeFloat = (SelectableFloat) queryClass.getMethod("getQueryFloat").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.EQ((float) 100.5));

      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on float values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeFloat = (SelectableFloat) queryClass.getMethod("getQueryFloat").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.EQ((float) 101.5));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aFloat("queryFloat").GT("100"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aFloat("queryFloat").GT("101"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatGt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aFloat("queryFloat").GT((float) 100));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aFloat("queryFloat").GT((float) 101));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatGt_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableFloat attributeFloat = (SelectableFloat) queryClass.getMethod("getQueryFloat").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.GT((float) 100));

      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on float values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeFloat = (SelectableFloat) queryClass.getMethod("getQueryFloat").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.GT((float) 101));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatGtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aFloat("queryFloat").GE("100.5"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute float values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aFloat("queryFloat").GE("100"));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aFloat("queryFloat").GE("101"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatGtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aFloat("queryFloat").GE((float) 100.5));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute float values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aFloat("queryFloat").GE((float) 100));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aFloat("queryFloat").GE((float) 101));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatGtEq_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();
      // perform a query that WILL find a match based on equals
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableFloat attributeFloat = (SelectableFloat) queryClass.getMethod("getQueryFloat").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.GE((float) 100.5));

      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on float values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeFloat = (SelectableFloat) queryClass.getMethod("getQueryFloat").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.GE((float) 100));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on float values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeFloat = (SelectableFloat) queryClass.getMethod("getQueryFloat").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.GE((float) 101));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aFloat("queryFloat").LT("101"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aFloat("queryFloat").LT("100"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatLt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aFloat("queryFloat").LT((float) 101));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aFloat("queryFloat").LT((float) 100));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatLt_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableFloat attributeFloat = (SelectableFloat) queryClass.getMethod("getQueryFloat").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.LT((float) 101));

      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on float values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeFloat = (SelectableFloat) queryClass.getMethod("getQueryFloat").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.LT((float) 100));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatLtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aFloat("queryFloat").LE("100.5"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute float values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aFloat("queryFloat").LE("101"));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aFloat("queryFloat").LE("99"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatLtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aFloat("queryFloat").LE((float) 100.5));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute float values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aFloat("queryFloat").LE((float) 101));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aFloat("queryFloat").LE((float) 99));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatLtEq_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();
      // perform a query that WILL find a match based on equals
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableFloat attributeFloat = (SelectableFloat) queryClass.getMethod("getQueryFloat").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.LE((float) 100.5));

      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on float values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeFloat = (SelectableFloat) queryClass.getMethod("getQueryFloat").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.LE((float) 101));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on float values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeFloat = (SelectableFloat) queryClass.getMethod("getQueryFloat").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.LE((float) 99));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aFloat("queryFloat").NE("101"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aFloat("queryFloat").NE("100.5"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatNotEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aFloat("queryFloat").NE((float) 101));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aFloat("queryFloat").NE((float) 100.5));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatNotEq_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableFloat attributeFloat = (SelectableFloat) queryClass.getMethod("getQueryFloat").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.NE((float) 101));

      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on float values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeFloat = (SelectableFloat) queryClass.getMethod("getQueryFloat").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.NE((float) 100.5));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDecimalEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDecimal("queryDecimal").EQ("100.5"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDecimal("queryDecimal").EQ("101.5"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDecimalEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDecimal("queryDecimal").EQ(new BigDecimal(100.5)));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDecimal("queryDecimal").EQ(new BigDecimal(101.5)));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDecimalEq_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal) queryClass.getMethod("getQueryDecimal").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.EQ(new BigDecimal(100.5)));

      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on Decimal values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDecimal = (SelectableDecimal) queryClass.getMethod("getQueryDecimal").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.EQ(new BigDecimal(101.5)));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute Decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDecimalGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDecimal("queryDecimal").GT("100"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDecimal("queryDecimal").GT("101"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDecimalGt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDecimal("queryDecimal").GT(new BigDecimal(100)));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDecimal("queryDecimal").GT(new BigDecimal(101)));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDecimalGt_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal) queryClass.getMethod("getQueryDecimal").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.GT(new BigDecimal(100)));

      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on Decimal values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDecimal = (AttributeDecimal) queryClass.getMethod("getQueryDecimal").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.GT(new BigDecimal(101)));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute Decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDecimalGtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDecimal("queryDecimal").GE("100.5"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute decimal values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDecimal("queryDecimal").GE("100"));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDecimal("queryDecimal").GE("101"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDecimalGtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDecimal("queryDecimal").GE(new BigDecimal(100.5)));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute decimal values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDecimal("queryDecimal").GE(new BigDecimal(100)));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDecimal("queryDecimal").GE(new BigDecimal(101)));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDecimalGtEq_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();
      // perform a query that WILL find a match based on equals
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal) queryClass.getMethod("getQueryDecimal").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.GE(new BigDecimal(100.5)));

      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on Decimal values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDecimal = (SelectableDecimal) queryClass.getMethod("getQueryDecimal").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.GE(new BigDecimal(100)));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on Decimal values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDecimal = (SelectableDecimal) queryClass.getMethod("getQueryDecimal").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.GE(new BigDecimal(101)));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute Decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDecimalLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDecimal("queryDecimal").LT("101"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDecimal("queryDecimal").LT("100"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDecimalLt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDecimal("queryDecimal").LT(new BigDecimal(101)));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDecimal("queryDecimal").LT(new BigDecimal(100)));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDecimalLt_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal) queryClass.getMethod("getQueryDecimal").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.LT(new BigDecimal(101)));

      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on Decimal values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDecimal = (SelectableDecimal) queryClass.getMethod("getQueryDecimal").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.LT(new BigDecimal(100)));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute Decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDecimalLtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDecimal("queryDecimal").LE("100.5"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute decimal values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDecimal("queryDecimal").LE("101"));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDecimal("queryDecimal").LE("99"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDecimalLtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDecimal("queryDecimal").LE(new BigDecimal(100.5)));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute decimal values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDecimal("queryDecimal").LE(new BigDecimal(101)));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDecimal("queryDecimal").LE(new BigDecimal(99)));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDecimalLtEq_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();
      // perform a query that WILL find a match based on equals
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal) queryClass.getMethod("getQueryDecimal").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.LE(new BigDecimal(100.5)));

      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on Decimal values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDecimal = (SelectableDecimal) queryClass.getMethod("getQueryDecimal").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.LE(new BigDecimal(101)));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on Decimal values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDecimal = (SelectableDecimal) queryClass.getMethod("getQueryDecimal").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.LE(new BigDecimal(99)));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute Decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDecimalNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDecimal("queryDecimal").NE("101"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDecimal("queryDecimal").NE("100.5"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDecimalNotEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDecimal("queryDecimal").NE(new BigDecimal(101)));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDecimal("queryDecimal").NE(new BigDecimal(100.5)));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDecimalNotEq_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal) queryClass.getMethod("getQueryDecimal").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.NE(new BigDecimal(101)));

      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on Decimal values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDecimal = (SelectableDecimal) queryClass.getMethod("getQueryDecimal").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.NE(new BigDecimal(100.5)));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute Decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDoubleEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDouble("queryDouble").EQ("100.5"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDouble("queryDouble").EQ("101.5"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDoubleEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDouble("queryDouble").EQ(100.5));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDouble("queryDouble").EQ(101.5));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDoubleEq_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDouble attributeDouble = (SelectableDouble) queryClass.getMethod("getQueryDouble").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.EQ(100.5));

      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on Double values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDouble = (SelectableDouble) queryClass.getMethod("getQueryDouble").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.EQ(101.5));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute Double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDoubleGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDouble("queryDouble").GT("100"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDouble("queryDouble").GT("101"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDoubleGt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDouble("queryDouble").GT((double) 100));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDouble("queryDouble").GT((double) 101));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDoubleGt_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDouble attributeDouble = (SelectableDouble) queryClass.getMethod("getQueryDouble").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.GT((double) 100));

      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on Double values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDouble = (SelectableDouble) queryClass.getMethod("getQueryDouble").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.GT((double) 101));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute Double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDoubleGtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDouble("queryDouble").GE("100.5"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute double values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDouble("queryDouble").GE("100"));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDouble("queryDouble").GE("101"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDoubleGtEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDouble("queryDouble").GE(100.5));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute double values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDouble("queryDouble").GE((double) 100));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDouble("queryDouble").GE((double) 101));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDoubleGtEq_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      // perform a query that WILL find a match
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDouble attributeDouble = (SelectableDouble) queryClass.getMethod("getQueryDouble").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.GE(100.5));

      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on Double values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater th
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDouble = (SelectableDouble) queryClass.getMethod("getQueryDouble").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.GE((double) 100));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on Double values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDouble = (SelectableDouble) queryClass.getMethod("getQueryDouble").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.GE((double) 101));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute Double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDoubleLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDouble("queryDouble").LT("101"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDouble("queryDouble").LT("100"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDoubleLt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDouble("queryDouble").LT((double) 101));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDouble("queryDouble").LT((double) 100));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDoubleLt_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDouble attributeDouble = (SelectableDouble) queryClass.getMethod("getQueryDouble").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.GE(100.5));

      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on Double values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDouble = (SelectableDouble) queryClass.getMethod("getQueryDouble").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.GE((double) 101));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute Double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDoubleLtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDouble("queryDouble").LE("100.5"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute double values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDouble("queryDouble").LE("101"));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDouble("queryDouble").LE("99"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDoubleLtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equal
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDouble("queryDouble").LE(100.5));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute double values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDouble("queryDouble").LE((double) 101));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDouble("queryDouble").LE((double) 99));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDoubleLtEq_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();
      // perform a query that WILL find a match based on equal
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDouble attributeDouble = (SelectableDouble) queryClass.getMethod("getQueryDouble").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.LE(100.5));

      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on Double values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDouble = (SelectableDouble) queryClass.getMethod("getQueryDouble").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.LE((double) 101));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on Double values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDouble = (SelectableDouble) queryClass.getMethod("getQueryDouble").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.LE((double) 99));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute Double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDoubleNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDouble("queryDouble").NE("101"));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDouble("queryDouble").NE("100.5"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDoubleNotEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      StructDAOQuery query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDouble("queryDouble").NE((double) 101));

      OIterator<StructDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (StructDAOIF object : iterator)
      {
        if (!object.getOid().equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.structDAOQuery(collection.definesType());
      query.WHERE(query.aDouble("queryDouble").NE(100.5));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDoubleNotEq_Generated()
  {
    try
    {
      String type = collection.definesType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDouble attributeDouble = (SelectableDouble) queryClass.getMethod("getQueryDouble").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.NE((double) 101));

      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(testStructQueryObject.getOid()))
        {
          Assert.fail("The objects returned by a query based on Double values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDouble = (SelectableDouble) queryClass.getMethod("getQueryDouble").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.NE(100.5));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute Double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }
}
