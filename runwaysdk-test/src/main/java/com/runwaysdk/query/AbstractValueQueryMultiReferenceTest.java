/**
*
*/
package com.runwaysdk.query;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeMultiReferenceInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDecimalDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeFloatDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLongDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeMultiReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTextDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdTermDAO;

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
public abstract class AbstractValueQueryMultiReferenceTest extends TestCase
{

  private static MdTermDAO                    mdTerm;

  private static MdAttributeBooleanDAO        mdAttributeBoolean;

  private static MdAttributeCharacterDAO      mdAttributeCharacter;

  private static MdAttributeDateDAO           mdAttributeDate;

  private static MdAttributeDateTimeDAO       mdAttributeDateTime;

  private static MdAttributeTimeDAO           mdAttributeTime;

  private static MdAttributeDecimalDAO        mdAttributeDecimal;

  private static MdAttributeDoubleDAO         mdAttributeDouble;

  private static MdAttributeFloatDAO          mdAttributeFloat;

  private static MdAttributeIntegerDAO        mdAttributeInteger;

  private static MdAttributeLongDAO           mdAttributeLong;

  private static MdAttributeTextDAO           mdAttributeText;

  private static MdBusinessDAO                mdBusiness;

  private static MdAttributeMultiReferenceDAO mdAttributeMultiReference;

  private static BusinessDAO                  term1;

  private static BusinessDAO                  term2;

  private static BusinessDAO                  term3;

  private static BusinessDAO                  business;

  /**
   * @param mdAttributeMultiReferenceDAO
   * 
   */
  public static void classSetUp(MdAttributeMultiReferenceDAO mdAttributeMultiReferenceDAO)
  {
    mdTerm = TestFixtureFactory.createMdTerm();
    mdTerm.apply();

    mdAttributeBoolean = TestFixtureFactory.addBooleanAttribute(mdTerm);
    mdAttributeBoolean.apply();

    mdAttributeCharacter = TestFixtureFactory.addCharacterAttribute(mdTerm);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "Default Character");
    mdAttributeCharacter.apply();

    mdAttributeDate = TestFixtureFactory.addDateAttribute(mdTerm, IndexTypes.NO_INDEX);
    mdAttributeDate.setValue(MdAttributeDateInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeDate.apply();

    mdAttributeDateTime = TestFixtureFactory.addDateTimeAttribute(mdTerm);
    mdAttributeDateTime.apply();

    mdAttributeTime = TestFixtureFactory.addTimeAttribute(mdTerm);
    mdAttributeTime.apply();

    mdAttributeDecimal = TestFixtureFactory.addDecimalAttribute(mdTerm);
    mdAttributeDecimal.apply();

    mdAttributeDouble = TestFixtureFactory.addDoubleAttribute(mdTerm);
    mdAttributeDouble.apply();

    mdAttributeFloat = TestFixtureFactory.addFloatAttribute(mdTerm);
    mdAttributeFloat.apply();

    mdAttributeInteger = TestFixtureFactory.addIntegerAttribute(mdTerm);
    mdAttributeInteger.apply();

    mdAttributeLong = TestFixtureFactory.addLongAttribute(mdTerm);
    mdAttributeLong.apply();

    mdAttributeText = TestFixtureFactory.addTextAttribute(mdTerm);
    mdAttributeText.apply();

    mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.apply();

    mdAttributeMultiReference = mdAttributeMultiReferenceDAO;
    mdAttributeMultiReference.setValue(MdAttributeMultiReferenceInfo.NAME, "testMultiReference");
    mdAttributeMultiReference.setStructValue(MdAttributeMultiReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term Test");
    mdAttributeMultiReference.setValue(MdAttributeMultiReferenceInfo.REF_MD_ENTITY, mdTerm.getId());
    mdAttributeMultiReference.setValue(MdAttributeMultiReferenceInfo.DEFINING_MD_CLASS, mdBusiness.getId());
    mdAttributeMultiReference.apply();
  }

  /**
  * 
  */
  public static void classTearDown()
  {
    TestFixtureFactory.delete(mdBusiness);
    TestFixtureFactory.delete(mdTerm);
  }

  protected abstract AttributeMultiReference getQueryAttribute(BusinessDAOQuery query);

  /**
   * @return the mdAttributeMultiReference
   */
  public MdAttributeMultiReferenceDAO getMdAttribute()
  {
    return mdAttributeMultiReference;
  }

  /*
   * (non-Javadoc)
   * 
   * @see junit.framework.TestCase#setUp()
   */
  @Override
  protected void setUp() throws Exception
  {
    term1 = this.createTerm();
    term2 = this.createTerm();
    term3 = this.createTerm();
    business = this.createBusiness(term2, term3);
  }

  /*
   * (non-Javadoc)
   * 
   * @see junit.framework.TestCase#tearDown()
   */
  @Override
  protected void tearDown() throws Exception
  {
    mdBusiness.deleteAllRecords();
    mdTerm.deleteAllRecords();
  }

  // test enumeration equal to enumeration

  public void testMultiReferenceContainsAny()
  {

    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find a match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).containsAny(term2.getId()));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that should NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query));
      vQ.WHERE(this.getQueryAttribute(query).containsAny(term1.getId()));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testMultiReferenceNotContainsAny()
  {

    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find a match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).notContainsAny(term1.getId()));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that should NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query));
      vQ.WHERE(this.getQueryAttribute(query).notContainsAny(term2.getId()));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testMultiReferenceContainsAll()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find a match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).containsAll(term2.getId(), term3.getId()));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that should NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query));
      vQ.WHERE(this.getQueryAttribute(query).containsAll(term2.getId(), term1.getId()));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testMultiReferenceNotContainsAll()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find a match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).notContainsAll(term2.getId(), term1.getId()));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that should NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query));
      vQ.WHERE(this.getQueryAttribute(query).notContainsAll(term2.getId(), term3.getId()));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testMultiReferenceContainsExactly()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find a match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).containsExactly(term2.getId(), term3.getId()));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that should NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query));
      vQ.WHERE(this.getQueryAttribute(query).containsExactly(term2.getId()));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testBooleanEqualBoolean()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query for true values that should find exactly 2 matches
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aBoolean("testBoolean"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aBoolean("testBoolean").EQ(true));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query for false values that should find exactly 2
      // matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aBoolean("testBoolean"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aBoolean("testBoolean").EQ(false));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testBooleanEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query for true values that should find exactly 2 matches
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aBoolean("testBoolean"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aBoolean("testBoolean").EQ(MdAttributeBooleanInfo.TRUE));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query for false values that should find exactly 2
      // matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aBoolean("testBoolean"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aBoolean("testBoolean").EQ(MdAttributeBooleanInfo.FALSE));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testBooleanEqualNull()
  {
    OIterator<ValueObject> i = null;

    BusinessDAO enumObject1 = BusinessDAO.get(term3.getId()).getBusinessDAO();
    String origValue1 = enumObject1.getValue("testBoolean");
    enumObject1.setValue("testBoolean", "");
    enumObject1.apply();

    BusinessDAO enumObject2 = BusinessDAO.get(term2.getId()).getBusinessDAO();
    String origValue2 = enumObject2.getValue("testBoolean");
    enumObject2.setValue("testBoolean", "");
    enumObject2.apply();

    try
    {
      // perform a query for null values
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aBoolean("testBoolean"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aBoolean("testBoolean").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query to find all objects with non-null queryBoolean
      // values
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aBoolean("testBoolean"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aBoolean("testBoolean").NE(""));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      enumObject1.setValue("testBoolean", origValue1);
      enumObject1.apply();

      enumObject2.setValue("testBoolean", origValue2);
      enumObject2.apply();

      if (i != null)
        i.close();
    }
  }

  public void testBooleanNotEqualBoolean()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query for true values that should find exactly 2 matches
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aBoolean("testBoolean"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aBoolean("testBoolean").NE(false));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query for false values that should find exactly 2
      // matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aBoolean("testBoolean"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aBoolean("testBoolean").NE(true));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testBooleanNotEqualString()
  {
    // perform a query for true values that should find exactly 2 matches
    QueryFactory qf = new QueryFactory();

    ValueQuery vQ = qf.valueQuery();
    BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

    vQ.SELECT(this.getQueryAttribute(query).aBoolean("testBoolean"), query.id("objectId"));
    vQ.WHERE(this.getQueryAttribute(query).aBoolean("testBoolean").NE(MdAttributeBooleanInfo.FALSE));

    OIterator<ValueObject> i = vQ.getIterator();

    if (!i.hasNext())
    {
      fail("A query did not return any results when it should have");
    }

    while (i.hasNext())
    {
      if (!i.next().getValue("objectId").equals(business.getId()))
      {
        fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
      }
    }

    // perform another query for false values that should find exactly 2
    // matches
    vQ = qf.valueQuery();

    vQ.SELECT(this.getQueryAttribute(query).aBoolean("testBoolean"), query.id("objectId"));
    vQ.WHERE(this.getQueryAttribute(query).aBoolean("testBoolean").NE(MdAttributeBooleanInfo.TRUE));

    i = vQ.getIterator();

    if (i.hasNext())
    {
      fail("A query based on attribute reference values returned objects when it shouldn't have.");
    }
  }

  public void testCharacterEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aCharacter("testCharacter"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aCharacter("testCharacter").EQ("Test Value"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aCharacter("testCharacter"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aCharacter("testCharacter").EQ("wrong character value"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testCharacterEqualString_IgnoreCase()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aCharacter("testCharacter"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aCharacter("testCharacter").EQi("Test Value".toUpperCase()));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aCharacter("testCharacter"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aCharacter("testCharacter").EQi("WRONG CHARACTER VALUE"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testCharacterEqualStringInArray()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aCharacter("testCharacter"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aCharacter("testCharacter").IN("wrong value 1", "Test Value", "wrong value 2"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aCharacter("testCharacter"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aCharacter("testCharacter").IN("wrong value 1", "wrong value 2", "wrong value 3", "Test Value".toUpperCase()));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testCharacterEqualStringInArray_IgnoreCase()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aCharacter("testCharacter"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aCharacter("testCharacter").INi("wrong value 1", "Test Value".toUpperCase(), "wrong value 2"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aCharacter("testCharacter"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aCharacter("testCharacter").IN("wrong value 1", "wrong value 2", "wrong value 3"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testCharacterEqualNull()
  {
    OIterator<ValueObject> i = null;

    BusinessDAO enumObject1 = BusinessDAO.get(term3.getId()).getBusinessDAO();
    String origValue1 = enumObject1.getValue("testCharacter");
    enumObject1.setValue("testCharacter", "");
    enumObject1.apply();

    BusinessDAO enumObject2 = BusinessDAO.get(term2.getId()).getBusinessDAO();
    String origValue2 = enumObject2.getValue("testCharacter");
    enumObject2.setValue("testCharacter", "");
    enumObject2.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aCharacter("testCharacter"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aCharacter("testCharacter").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 3 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aCharacter("testCharacter"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aCharacter("testCharacter").NE(""));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      enumObject1.setValue("testCharacter", origValue1);
      enumObject1.apply();

      enumObject2.setValue("testCharacter", origValue2);
      enumObject2.apply();

      if (i != null)
        i.close();
    }
  }

  public void testCharacterLikeString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aCharacter("testCharacter"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aCharacter("testCharacter").LIKE("%Test Value%"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aCharacter("testCharacter"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aCharacter("testCharacter").LIKE("%character"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testCharacterLikeString_IgnoreCase()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aCharacter("testCharacter"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aCharacter("testCharacter").LIKEi("%Test Value%".toUpperCase()));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aCharacter("testCharacter"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aCharacter("testCharacter").LIKEi("%CHARACTER"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testCharacterNotEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aCharacter("testCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(this.getQueryAttribute(query).aCharacter("testCharacter").NE("wrong character value"), this.getQueryAttribute(query).aCharacter("testCharacter").EQ("")));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aCharacter("testCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(this.getQueryAttribute(query).aCharacter("testCharacter").NE("Test Value"), this.getQueryAttribute(query).aCharacter("testCharacter").EQ("")));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testCharacterNotEqualString_IgnoreCase()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aCharacter("testCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(this.getQueryAttribute(query).aCharacter("testCharacter").NEi("WRONG CHARACTER VALUE"), this.getQueryAttribute(query).aCharacter("testCharacter").EQ("")));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aCharacter("testCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(this.getQueryAttribute(query).aCharacter("testCharacter").NEi("Test Value".toUpperCase()), this.getQueryAttribute(query).aCharacter("testCharacter").EQ("")));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testCharacterNotEqualStringInArray()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aCharacter("testCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(this.getQueryAttribute(query).aCharacter("testCharacter").NI("wrong character value 1", "wrong character value 2", "wrong character value 3"), this.getQueryAttribute(query).aCharacter("testCharacter").EQ("")));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aCharacter("testCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(this.getQueryAttribute(query).aCharacter("testCharacter").NI("wrong value", "Test Value", "wrong value 2"), this.getQueryAttribute(query).aCharacter("testCharacter").EQ("")));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testCharacterNotEqualStringInArray_IgnoreCase()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aCharacter("testCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(this.getQueryAttribute(query).aCharacter("testCharacter").NIi("WRONG VALUE 1", "WRONG VALUE 2", "WRONG VALUE 3"), this.getQueryAttribute(query).aCharacter("testCharacter").EQ("")));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aCharacter("testCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(this.getQueryAttribute(query).aCharacter("testCharacter").NIi("WRONG VALUE", "Test Value".toUpperCase(), "WRONG VALUE 2"), this.getQueryAttribute(query).aCharacter("testCharacter").EQ("")));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testCharacterNotLikeString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform another query that should find 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aCharacter("testCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(this.getQueryAttribute(query).aCharacter("testCharacter").NLIKE("%wrong%"), this.getQueryAttribute(query).aCharacter("testCharacter").EQ("")));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aCharacter("testCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(this.getQueryAttribute(query).aCharacter("testCharacter").NLIKE("%Test Value%"), this.getQueryAttribute(query).aCharacter("testCharacter").EQ("")));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testCharacterNotLikeString_IgnoreCase()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform another query that should find 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aCharacter("testCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(this.getQueryAttribute(query).aCharacter("testCharacter").NLIKEi("%WRONG%"), this.getQueryAttribute(query).aCharacter("testCharacter").EQ("")));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aCharacter("testCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(this.getQueryAttribute(query).aCharacter("testCharacter").NLIKEi("%Test Value%".toUpperCase()), this.getQueryAttribute(query).aCharacter("testCharacter").EQ("")));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDateEqualDate()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2012-02-11", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDate("testDate"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDate("testDate").EQ(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2011-02-12", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDate("testDate"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDate("testDate").EQ(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDateEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDate("testDate"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDate("testDate").EQ("2012-02-11"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDate("testDate"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDate("testDate").EQ("2006-05-05"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDateGTDate()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2011-02-12", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDate("testDate"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDate("testDate").GT(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2012-02-12", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDate("testDate"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDate("testDate").GT(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDateGTString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDate("testDate"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDate("testDate").GT("2011-02-12"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDate("testDate"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDate("testDate").GT("2012-02-12"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDateGEDate()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2012-02-11", new java.text.ParsePosition(0));

    try
    {
      // perform a query with a date equal to the stored
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDate("testDate"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDate("testDate").GE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query with a date less than the stored
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2011-02-12", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDate("testDate"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDate("testDate").GE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query with a date greater than the stored
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2012-02-12", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDate("testDate"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDate("testDate").GE(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDateGEString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query with a date equal to the stored
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDate("testDate"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDate("testDate").GE("2012-02-11"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query with a date less than the stored
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDate("testDate"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDate("testDate").GE("2011-02-12"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query with a date greater than the stored
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDate("testDate"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDate("testDate").GE("2012-02-12"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDateEqualNull()
  {
    OIterator<ValueObject> i = null;

    BusinessDAO enumObject1 = BusinessDAO.get(term3.getId()).getBusinessDAO();
    String origValue1 = enumObject1.getValue("testDate");
    enumObject1.setValue("testDate", "");
    enumObject1.apply();

    BusinessDAO enumObject2 = BusinessDAO.get(term2.getId()).getBusinessDAO();
    String origValue2 = enumObject2.getValue("testDate");
    enumObject2.setValue("testDate", "");
    enumObject2.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDate("testDate"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDate("testDate").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDate("testDate"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDate("testDate").NE(""));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      enumObject1.setValue("testDate", origValue1);
      enumObject1.apply();

      enumObject2.setValue("testDate", origValue2);
      enumObject2.apply();

      if (i != null)
        i.close();
    }
  }

  public void testDateLTDate()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2012-02-12", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDate("testDate"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDate("testDate").LT(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2011-02-12", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDate("testDate"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDate("testDate").LT(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDateLTString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDate("testDate"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDate("testDate").LT("2012-02-12"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDate("testDate"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDate("testDate").LT("2011-02-12"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDateLEDate()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2012-02-12", new java.text.ParsePosition(0));

    try
    {
      // perform a query with a date equal to the stored
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDate("testDate"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDate("testDate").LE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query with a date less than the stored
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2012-02-13", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDate("testDate"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDate("testDate").LE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query with a date greater than the stored
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2012-01-12", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDate("testDate"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDate("testDate").LE(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDateLEString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query with a date equal to the stored
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDate("testDate"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDate("testDate").LE("2012-02-12"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query with a date less than the stored
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDate("testDate"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDate("testDate").LE("2012-02-13"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query with a date greater than the stored
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDate("testDate"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDate("testDate").LE("2012-02-10"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDateNotEqualDate()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2011-02-12", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDate("testDate"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDate("testDate").NE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2012-02-11", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDate("testDate"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDate("testDate").NE(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDateNotEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDate("testDate"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDate("testDate").NE("2011-02-12"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDate("testDate"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDate("testDate").NE("2012-02-11"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDateTimeEqualDateTime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2012-02-11 12:30:30", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDateTime("testDateTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDateTime("testDateTime").EQ(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-06 11:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDateTime("testDateTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDateTime("testDateTime").EQ(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDateTimeEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDateTime("testDateTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDateTime("testDateTime").EQ("2012-02-11 12:30:30"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDateTime("testDateTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDateTime("testDateTime").EQ("2006-11-06 11:00:00"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDateTimeGTDateTime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-06 11:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDateTime("testDateTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDateTime("testDateTime").GT(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2012-02-11 12:30:30", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDateTime("testDateTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDateTime("testDateTime").GT(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDateTimeGTString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDateTime("testDateTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDateTime("testDateTime").GT("2006-11-06 11:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDateTime("testDateTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDateTime("testDateTime").GT("2012-02-11 12:30:30"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDateTimeGEDateTime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2012-02-11 12:30:30", new java.text.ParsePosition(0));

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDateTime("testDateTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDateTime("testDateTime").GE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-05 12:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDateTime("testDateTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDateTime("testDateTime").GE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2012-03-11 12:30:30", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDateTime("testDateTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDateTime("testDateTime").GE(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDateTimeGEString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDateTime("testDateTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDateTime("testDateTime").GE("2012-02-11 12:30:30"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDateTime("testDateTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDateTime("testDateTime").GE("2006-11-05 12:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDateTime("testDateTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDateTime("testDateTime").GE("2012-03-11 12:30:30"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDateTimeEqualNull()
  {
    OIterator<ValueObject> i = null;

    BusinessDAO enumObject1 = BusinessDAO.get(term3.getId()).getBusinessDAO();
    String origValue1 = enumObject1.getValue("testDateTime");
    enumObject1.setValue("testDateTime", "");
    enumObject1.apply();

    BusinessDAO enumObject2 = BusinessDAO.get(term2.getId()).getBusinessDAO();
    String origValue2 = enumObject2.getValue("testDateTime");
    enumObject2.setValue("testDateTime", "");
    enumObject2.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDateTime("testDateTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDateTime("testDateTime").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDateTime("testDateTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDateTime("testDateTime").NE(""));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      enumObject1.setValue("testDateTime", origValue1);
      enumObject1.apply();

      enumObject2.setValue("testDateTime", origValue2);
      enumObject2.apply();

      if (i != null)
        i.close();
    }
  }

  public void testDateTimeLTDateTime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2012-03-11 12:30:30", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDateTime("testDateTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDateTime("testDateTime").LT(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-05 12:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDateTime("testDateTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDateTime("testDateTime").LT(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDateTimeLTString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDateTime("testDateTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDateTime("testDateTime").LT("2012-03-11 12:30:30"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDateTime("testDateTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDateTime("testDateTime").LT("2006-11-05 12:00:00"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDateTimeLEDateTime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2012-02-11 12:30:30", new java.text.ParsePosition(0));

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDateTime("testDateTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDateTime("testDateTime").LE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2012-03-11 12:30:30", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDateTime("testDateTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDateTime("testDateTime").LE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-05 12:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDateTime("testDateTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDateTime("testDateTime").LE(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDateTimeLEString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDateTime("testDateTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDateTime("testDateTime").LE("2012-02-11 12:30:30"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDateTime("testDateTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDateTime("testDateTime").LE("2012-03-11 12:30:30"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDateTime("testDateTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDateTime("testDateTime").LE("2006-11-05 12:00:00"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDateTimeNotEqualDateTime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-05 12:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDateTime("testDateTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDateTime("testDateTime").NE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2012-02-11 12:30:30", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDateTime("testDateTime"), query.id("objectId"));
      vQ.WHERE(AND.get(this.getQueryAttribute(query).aDateTime("testDateTime").NE(date), this.getQueryAttribute(query).aDateTime("testDateTime").NE("")));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDateTimeNotEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDateTime("testDateTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDateTime("testDateTime").NE("2006-11-05 12:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDateTime("testDateTime"), query.id("objectId"));
      vQ.WHERE(AND.get(this.getQueryAttribute(query).aDateTime("testDateTime").NE("2012-02-11 12:30:30"), this.getQueryAttribute(query).aDateTime("testDateTime").NE("")));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDecimalEqualDecimal()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDecimal("testDecimal"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDecimal("testDecimal").EQ(new BigDecimal("12.1")));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDecimal("testDecimal"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDecimal("testDecimal").EQ(new BigDecimal(15.5)));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDecimalEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDecimal("testDecimal"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDecimal("testDecimal").EQ("12.1"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDecimal("testDecimal"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDecimal("testDecimal").EQ("15.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDecimalGTDecimal()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDecimal("testDecimal"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDecimal("testDecimal").GT(new BigDecimal(12.0)));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDecimal("testDecimal"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDecimal("testDecimal").GT(new BigDecimal(15.5)));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDecimalGTString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDecimal("testDecimal"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDecimal("testDecimal").GT("12.0"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDecimal("testDecimal"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDecimal("testDecimal").GT("15.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDecimalGEDecimal()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDecimal("testDecimal"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDecimal("testDecimal").GE(new BigDecimal(12.1)));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDecimal("testDecimal"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDecimal("testDecimal").GE(new BigDecimal(11.1)));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDecimal("testDecimal"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDecimal("testDecimal").GE(new BigDecimal(201)));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDecimalGEString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDecimal("testDecimal"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDecimal("testDecimal").GE("12.1"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDecimal("testDecimal"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDecimal("testDecimal").GE("11.1"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDecimal("testDecimal"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDecimal("testDecimal").GE("15.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDecimalEqualNull()
  {
    OIterator<ValueObject> i = null;

    BusinessDAO enumObject1 = BusinessDAO.get(term3.getId()).getBusinessDAO();
    String origValue1 = enumObject1.getValue("testDecimal");
    enumObject1.setValue("testDecimal", "");
    enumObject1.apply();

    BusinessDAO enumObject2 = BusinessDAO.get(term2.getId()).getBusinessDAO();
    String origValue2 = enumObject2.getValue("testDecimal");
    enumObject2.setValue("testDecimal", "");
    enumObject2.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDecimal("testDecimal"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDecimal("testDecimal").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDecimal("testDecimal"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDecimal("testDecimal").NE(""));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      enumObject1.setValue("testDecimal", origValue1);
      enumObject1.apply();

      enumObject2.setValue("testDecimal", origValue2);
      enumObject2.apply();

      if (i != null)
        i.close();
    }
  }

  public void testDecimalLTDecimal()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDecimal("testDecimal"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDecimal("testDecimal").LT(new BigDecimal(15.5)));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDecimal("testDecimal"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDecimal("testDecimal").LT(new BigDecimal(11.1)));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDecimalLTString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDecimal("testDecimal"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDecimal("testDecimal").LT("15.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDecimal("testDecimal"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDecimal("testDecimal").LT("12.1"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDecimalLEDecimal()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDecimal("testDecimal"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDecimal("testDecimal").LE(new BigDecimal("12.1")));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDecimal("testDecimal"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDecimal("testDecimal").LE(new BigDecimal(15.5)));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDecimal("testDecimal"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDecimal("testDecimal").LE(new BigDecimal(11.1)));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDecimalLEString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDecimal("testDecimal"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDecimal("testDecimal").LE("12.1"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDecimal("testDecimal"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDecimal("testDecimal").LE("15.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDecimal("testDecimal"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDecimal("testDecimal").LE("12.0"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDecimalNotEqualDecimal()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDecimal("testDecimal"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDecimal("testDecimal").NE(new BigDecimal(15.5)));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDecimal("testDecimal"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDecimal("testDecimal").NE(new BigDecimal("12.1")));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDecimalNotEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDecimal("testDecimal"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDecimal("testDecimal").NE("15.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDecimal("testDecimal"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDecimal("testDecimal").NE("12.1"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDoubleEqualDouble()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDouble("testDouble"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDouble("testDouble").EQ(13.2));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDouble("testDouble"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDouble("testDouble").EQ(201.5));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDoubleEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDouble("testDouble"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDouble("testDouble").EQ("13.2"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDouble("testDouble"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDouble("testDouble").EQ("201.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDoubleGTDouble()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDouble("testDouble"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDouble("testDouble").GT(7.2));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDouble("testDouble"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDouble("testDouble").GT(220.5));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDoubleGTString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDouble("testDouble"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDouble("testDouble").GT("7.2"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDouble("testDouble"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDouble("testDouble").GT("220.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDoubleGEDouble()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equal to
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDouble("testDouble"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDouble("testDouble").GE(13.2));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDouble("testDouble"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDouble("testDouble").GE(7.2));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDouble("testDouble"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDouble("testDouble").GE(220.5));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDoubleGEString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equal to
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDouble("testDouble"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDouble("testDouble").GE("13.2"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDouble("testDouble"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDouble("testDouble").GE("7.2"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDouble("testDouble"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDouble("testDouble").GE("220.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDoubleEqualNull()
  {
    OIterator<ValueObject> i = null;

    BusinessDAO enumObject1 = BusinessDAO.get(term3.getId()).getBusinessDAO();
    String origValue1 = enumObject1.getValue("testDouble");
    enumObject1.setValue("testDouble", "");
    enumObject1.apply();

    BusinessDAO enumObject2 = BusinessDAO.get(term2.getId()).getBusinessDAO();
    String origValue2 = enumObject2.getValue("testDouble");
    enumObject2.setValue("testDouble", "");
    enumObject2.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDouble("testDouble"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDouble("testDouble").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDouble("testDouble"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDouble("testDouble").NE(""));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      enumObject1.setValue("testDouble", origValue1);
      enumObject1.apply();

      enumObject2.setValue("testDouble", origValue2);
      enumObject2.apply();

      if (i != null)
        i.close();
    }
  }

  public void testDoubleLTDouble()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDouble("testDouble"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDouble("testDouble").LT(220.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDouble("testDouble"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDouble("testDouble").LT(7.2));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDoubleLTString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDouble("testDouble"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDouble("testDouble").LT("220.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDouble("testDouble"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDouble("testDouble").LT("7.2"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDoubleLEDouble()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equal
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDouble("testDouble"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDouble("testDouble").LE(13.2));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on less than
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDouble("testDouble"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDouble("testDouble").LE(220.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDouble("testDouble"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDouble("testDouble").LE(7.2));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDoubleLEString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equal
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDouble("testDouble"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDouble("testDouble").LE("13.2"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on less than
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDouble("testDouble"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDouble("testDouble").LE("220.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDouble("testDouble"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDouble("testDouble").LE("7.2"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDoubleNotEqualDouble()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDouble("testDouble"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDouble("testDouble").NE(201.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDouble("testDouble"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDouble("testDouble").NE(13.2));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDoubleNotEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aDouble("testDouble"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDouble("testDouble").NE("201.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aDouble("testDouble"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aDouble("testDouble").NE("13.2"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testFloatEqualFloat()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aFloat("testFloat"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aFloat("testFloat").EQ((float) 0.0));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aFloat("testFloat"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aFloat("testFloat").EQ((float) 201.5));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testFloatEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aFloat("testFloat"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aFloat("testFloat").EQ("0.0"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aFloat("testFloat"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aFloat("testFloat").EQ("201.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testFloatGTFloat()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aFloat("testFloat"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aFloat("testFloat").GT((float) -20.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aFloat("testFloat"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aFloat("testFloat").GT((float) 221.5));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testFloatGTString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aFloat("testFloat"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aFloat("testFloat").GT("-1.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aFloat("testFloat"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aFloat("testFloat").GT("221.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testFloatGEFloat()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aFloat("testFloat"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aFloat("testFloat").GE((float) 0.0));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aFloat("testFloat"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aFloat("testFloat").GE((float) -21.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aFloat("testFloat"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aFloat("testFloat").GE((float) 221.5));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testFloatGEString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aFloat("testFloat"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aFloat("testFloat").GE("-200.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aFloat("testFloat"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aFloat("testFloat").GE("-21.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aFloat("testFloat"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aFloat("testFloat").GE("221.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testFloatEqualNull()
  {
    OIterator<ValueObject> i = null;

    BusinessDAO enumObject1 = BusinessDAO.get(term3.getId()).getBusinessDAO();
    String origValue1 = enumObject1.getValue("testFloat");
    enumObject1.setValue("testFloat", "");
    enumObject1.apply();

    BusinessDAO enumObject2 = BusinessDAO.get(term2.getId()).getBusinessDAO();
    String origValue2 = enumObject2.getValue("testFloat");
    enumObject2.setValue("testFloat", "");
    enumObject2.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aFloat("testFloat"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aFloat("testFloat").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aFloat("testFloat"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aFloat("testFloat").NE(""));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      enumObject1.setValue("testFloat", origValue1);
      enumObject1.apply();

      enumObject2.setValue("testFloat", origValue2);
      enumObject2.apply();

      if (i != null)
        i.close();
    }
  }

  public void testFloatLTFloat()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aFloat("testFloat"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aFloat("testFloat").LT((float) 220.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aFloat("testFloat"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aFloat("testFloat").LT((float) -21.5));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testFloatLTString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aFloat("testFloat"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aFloat("testFloat").LT("220.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aFloat("testFloat"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aFloat("testFloat").LT("-21.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testFloatLEFloat()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aFloat("testFloat"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aFloat("testFloat").LE((float) 0));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on less than
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aFloat("testFloat"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aFloat("testFloat").LE((float) 201.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aFloat("testFloat"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aFloat("testFloat").LE((float) -21.5));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testFloatLEString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aFloat("testFloat"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aFloat("testFloat").LE("0.0"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on less than
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aFloat("testFloat"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aFloat("testFloat").LE("201.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aFloat("testFloat"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aFloat("testFloat").LE("-21.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testFloatNotEqualFloat()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aFloat("testFloat"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aFloat("testFloat").NE((float) 210.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aFloat("testFloat"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aFloat("testFloat").NE((float) 0.0));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testFloatNotEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aFloat("testFloat"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aFloat("testFloat").NE("210.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aFloat("testFloat"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aFloat("testFloat").NE("0"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testIntegerEqualInteger()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aInteger("testInteger"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aInteger("testInteger").EQ(-10));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aInteger("testInteger"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aInteger("testInteger").EQ(45));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testIntegerEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aInteger("testInteger"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aInteger("testInteger").EQ("-10"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aInteger("testInteger"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aInteger("testInteger").EQ("45"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testIntegerGTInteger()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aInteger("testInteger"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aInteger("testInteger").GT(-15));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aInteger("testInteger"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aInteger("testInteger").GT(45));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testIntegerGTString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aInteger("testInteger"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aInteger("testInteger").GT("-15"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aInteger("testInteger"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aInteger("testInteger").GT("45"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testIntegerGEInteger()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aInteger("testInteger"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aInteger("testInteger").GE(-10));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aInteger("testInteger"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aInteger("testInteger").GE(-15));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aInteger("testInteger"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aInteger("testInteger").GE(45));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testIntegerGEString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aInteger("testInteger"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aInteger("testInteger").GE("-10"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aInteger("testInteger"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aInteger("testInteger").GE("-15"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aInteger("testInteger"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aInteger("testInteger").GE("45"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testIntegerEqualNull()
  {
    OIterator<ValueObject> i = null;

    BusinessDAO enumObject1 = BusinessDAO.get(term3.getId()).getBusinessDAO();
    String origValue1 = enumObject1.getValue("testInteger");
    enumObject1.setValue("testInteger", "");
    enumObject1.apply();

    BusinessDAO enumObject2 = BusinessDAO.get(term2.getId()).getBusinessDAO();
    String origValue2 = enumObject2.getValue("testInteger");
    enumObject2.setValue("testInteger", "");
    enumObject2.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aInteger("testInteger"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aInteger("testInteger").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aInteger("testInteger"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aInteger("testInteger").NE(""));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      enumObject1.setValue("testInteger", origValue1);
      enumObject1.apply();

      enumObject2.setValue("testInteger", origValue2);
      enumObject2.apply();

      if (i != null)
        i.close();
    }
  }

  public void testIntegerLTInteger()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aInteger("testInteger"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aInteger("testInteger").LT(45));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aInteger("testInteger"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aInteger("testInteger").LT(-10));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testIntegerLTString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aInteger("testInteger"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aInteger("testInteger").LT("45"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aInteger("testInteger"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aInteger("testInteger").LT("-10"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testIntegerLEInteger()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aInteger("testInteger"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aInteger("testInteger").LE(-10));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aInteger("testInteger"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aInteger("testInteger").LE(45));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aInteger("testInteger"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aInteger("testInteger").LE(-15));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testIntegerLEString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aInteger("testInteger"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aInteger("testInteger").LE("-10"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aInteger("testInteger"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aInteger("testInteger").LE("45"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aInteger("testInteger"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aInteger("testInteger").LE("-15"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testIntegerNotEqualInteger()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aInteger("testInteger"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aInteger("testInteger").NE(-11));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aInteger("testInteger"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aInteger("testInteger").NE(-10));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testIntegerNotEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aInteger("testInteger"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aInteger("testInteger").NE("45"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aInteger("testInteger"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aInteger("testInteger").NE("-10"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testLongEqualLong()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aLong("testLong"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aLong("testLong").EQ((long) 20));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aLong("testLong"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aLong("testLong").EQ((long) 201));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testLongEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aLong("testLong"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aLong("testLong").EQ("20"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aLong("testLong"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aLong("testLong").EQ("201"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testLongGTLong()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aLong("testLong"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aLong("testLong").GT((long) 19));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aLong("testLong"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aLong("testLong").GT((long) 201));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testLongGTString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aLong("testLong"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aLong("testLong").GT("19"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aLong("testLong"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aLong("testLong").GT("201"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testLongGELong()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aLong("testLong"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aLong("testLong").GE((long) 20));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aLong("testLong"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aLong("testLong").GE((long) 19));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aLong("testLong"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aLong("testLong").GE((long) 201));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testLongGEString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aLong("testLong"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aLong("testLong").GE("20"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aLong("testLong"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aLong("testLong").GE("10"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aLong("testLong"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aLong("testLong").GE("201"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testLongEqualNull()
  {
    OIterator<ValueObject> i = null;

    BusinessDAO enumObject1 = BusinessDAO.get(term3.getId()).getBusinessDAO();
    String origValue1 = enumObject1.getValue("testLong");
    enumObject1.setValue("testLong", "");
    enumObject1.apply();

    BusinessDAO enumObject2 = BusinessDAO.get(term2.getId()).getBusinessDAO();
    String origValue2 = enumObject2.getValue("testLong");
    enumObject2.setValue("testLong", "");
    enumObject2.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aLong("testLong"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aLong("testLong").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aLong("testLong"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aLong("testLong").NE(""));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      enumObject1.setValue("testLong", origValue1);
      enumObject1.apply();

      enumObject2.setValue("testLong", origValue2);
      enumObject2.apply();

      if (i != null)
        i.close();
    }
  }

  public void testLongLTLong()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aLong("testLong"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aLong("testLong").LT((long) 220));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aLong("testLong"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aLong("testLong").LT((long) 20));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testLongLTString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aLong("testLong"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aLong("testLong").LT("220"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aLong("testLong"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aLong("testLong").LT("20"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testLongLELong()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aLong("testLong"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aLong("testLong").LE((long) 20));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aLong("testLong"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aLong("testLong").LE((long) 220));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aLong("testLong"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aLong("testLong").LE((long) 19));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testLongLEString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aLong("testLong"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aLong("testLong").LE("20"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aLong("testLong"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aLong("testLong").LE("220"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aLong("testLong"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aLong("testLong").LE("19"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testLongNotEqualLong()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aLong("testLong"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aLong("testLong").NE((long) 200));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aLong("testLong"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aLong("testLong").NE((long) 20));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testLongNotEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aLong("testLong"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aLong("testLong").NE("200"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aLong("testLong"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aLong("testLong").NE("20"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testTextEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aText("testText"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aText("testText").EQ("Test Text Value"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aText("testText"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aText("testText").EQ("Not Test Text Value"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testTextEqualString_IgnoreCase()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aText("testText"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aText("testText").EQi("Test Text Value".toUpperCase()));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aText("testText"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aText("testText").EQi("WRONG TEXT VALUE"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testTextEqualStringInArray()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aText("testText"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aText("testText").IN("Test Text Value", "enum text value", "wrong text value"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aText("testText"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aText("testText").IN("ENUM text value", "ENUM TEXT value", "ENUM TEXT VALUE"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testTextEqualStringInArray_IgnoreCase()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aText("testText"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aText("testText").INi("Test Text Value".toUpperCase(), "ENUM TEXT VALUE", "wrong TEXT value 2"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aText("testText"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aText("testText").INi("WRONG TEXT VALUE", "WRONG TEXT VALUE 2", "WRONG TEXT VALUE 3"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testTextEqualNull()
  {
    OIterator<ValueObject> i = null;

    BusinessDAO enumObject1 = BusinessDAO.get(term3.getId()).getBusinessDAO();
    String origValue1 = enumObject1.getValue("testText");
    enumObject1.setValue("testText", "");
    enumObject1.apply();

    BusinessDAO enumObject2 = BusinessDAO.get(term2.getId()).getBusinessDAO();
    String origValue2 = enumObject2.getValue("testText");
    enumObject2.setValue("testText", "");
    enumObject2.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aText("testText"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aText("testText").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aText("testText"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aText("testText").NE(""));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      enumObject1.setValue("testText", origValue1);
      enumObject1.apply();

      enumObject2.setValue("testText", origValue2);
      enumObject2.apply();

      if (i != null)
        i.close();
    }
  }

  public void testTextLikeString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aText("testText"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aText("testText").LIKE("%Text%"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aText("testText"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aText("testText").LIKE("%text"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testTextLikeString_IgnoreCase()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aText("testText"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aText("testText").LIKEi("%TEXT%"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aText("testText"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aText("testText").LIKEi("%TEXT"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testTextNotEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aText("testText"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aText("testText").NE("wrong text value"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aText("testText"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aText("testText").NE("Test Text Value"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testTextNotEqualString_IgnoreCase()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aText("testText"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aText("testText").NEi("WRONG TEXT VALUE"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aText("testText"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aText("testText").NEi("Test Text Value".toUpperCase()));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testTextNotEqualStringInArray()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aText("testText"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aText("testText").NI("ENUM text value", "ENUM TEXT value", "ENUM TEXT VALUE"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aText("testText"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aText("testText").NI("Test Text Value", "enum text value", "wrong text value"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testTextNotEqualStringInArray_IgnoreCase()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aText("testText"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aText("testText").NIi("WRONG text value", "WRONG TEXT value 2", "WRONG TEXT VALUE 3"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aText("testText"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aText("testText").NIi("Test Text Value".toLowerCase(), "ENUM TEXT VALUE", "ENUM TEXT value"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testTextNotLikeString_IgnoreCase()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aText("testText"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aText("testText").NLIKEi("%WRONG%"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aText("testText"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aText("testText").NLIKEi("%TEXT%"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testTimeEqualTime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:30:30", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aTime("testTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aTime("testTime").EQ(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("11:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aTime("testTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aTime("testTime").EQ(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testTimeEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aTime("testTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aTime("testTime").EQ("12:30:30"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aTime("testTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aTime("testTime").EQ("11:00:00"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testTimeGTTime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("11:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aTime("testTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aTime("testTime").GT(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aTime("testTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aTime("testTime").GT(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testTimeGTString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aTime("testTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aTime("testTime").GT("11:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aTime("testTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aTime("testTime").GT("14:00:00"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testTimeGETime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:30:30", new java.text.ParsePosition(0));

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aTime("testTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aTime("testTime").GE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:30:30", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aTime("testTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aTime("testTime").GE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aTime("testTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aTime("testTime").GE(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testTimeGEString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aTime("testTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aTime("testTime").GE("12:30:30"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aTime("testTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aTime("testTime").GE("12:30:30"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aTime("testTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aTime("testTime").GE("14:00:00"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testTimeEqualNull()
  {
    OIterator<ValueObject> i = null;

    BusinessDAO enumObject1 = BusinessDAO.get(term3.getId()).getBusinessDAO();
    String origValue1 = enumObject1.getValue("testTime");
    enumObject1.setValue("testTime", "");
    enumObject1.apply();

    BusinessDAO enumObject2 = BusinessDAO.get(term2.getId()).getBusinessDAO();
    String origValue2 = enumObject2.getValue("testTime");
    enumObject2.setValue("testTime", "");
    enumObject2.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aTime("testTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aTime("testTime").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aTime("testTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aTime("testTime").NE(""));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      enumObject1.setValue("testTime", origValue1);
      enumObject1.apply();

      enumObject2.setValue("testTime", origValue2);
      enumObject2.apply();

      if (i != null)
        i.close();
    }
  }

  public void testTimeLTTime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aTime("testTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aTime("testTime").LT(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("10:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aTime("testTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aTime("testTime").LT(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testTimeLTString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aTime("testTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aTime("testTime").LT("14:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aTime("testTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aTime("testTime").LT("10:00:00"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testTimeLETime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:30:30", new java.text.ParsePosition(0));

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aTime("testTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aTime("testTime").LE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aTime("testTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aTime("testTime").LE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("10:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aTime("testTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aTime("testTime").LE(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testTimeLEString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aTime("testTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aTime("testTime").LE("12:30:30"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aTime("testTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aTime("testTime").LE("14:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aTime("testTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aTime("testTime").LE("10:00:00"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testTimeNotEqualTime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("10:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aTime("testTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aTime("testTime").NE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:30:30", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aTime("testTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aTime("testTime").NE(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testTimeNotEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(mdBusiness.definesType());

      vQ.SELECT(this.getQueryAttribute(query).aTime("testTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aTime("testTime").NE("10:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(business.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(this.getQueryAttribute(query).aTime("testTime"), query.id("objectId"));
      vQ.WHERE(this.getQueryAttribute(query).aTime("testTime").NE("12:30:30"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  /**
   * @return
   */
  private BusinessDAO createTerm()
  {
    BusinessDAO term = BusinessDAO.newInstance(mdTerm.definesType());
    term.setValue(mdAttributeBoolean.definesAttribute(), MdAttributeBooleanInfo.TRUE);
    term.setValue(mdAttributeCharacter.definesAttribute(), "Test Value");
    term.setValue(mdAttributeDate.definesAttribute(), "2012-02-11");
    term.setValue(mdAttributeDateTime.definesAttribute(), "2012-02-11 12:30:30");
    term.setValue(mdAttributeTime.definesAttribute(), "12:30:30");
    term.setValue(mdAttributeDecimal.definesAttribute(), "12.1");
    term.setValue(mdAttributeDouble.definesAttribute(), "13.2");
    term.setValue(mdAttributeFloat.definesAttribute(), "0");
    term.setValue(mdAttributeInteger.definesAttribute(), "-10");
    term.setValue(mdAttributeLong.definesAttribute(), "20");
    term.setValue(mdAttributeText.definesAttribute(), "Test Text Value");
    term.apply();

    return term;
  }

  /**
   * @param term
   * @return
   */
  private BusinessDAO createBusiness(ComponentIF... terms)
  {
    BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());

    for (ComponentIF term : terms)
    {
      business.addItem(mdAttributeMultiReference.definesAttribute(), term.getId());
    }

    business.apply();

    return business;
  }

}
