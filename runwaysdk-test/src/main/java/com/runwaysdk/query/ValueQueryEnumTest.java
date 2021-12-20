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
package com.runwaysdk.query;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EnumerationItemDAO;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.session.Request;

public class ValueQueryEnumTest
{
  // test enumeration equal to enumeration

  @Request
  @Test
  public void testEnumerationContainsAny()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find a match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").containsAny(QueryMasterSetup.coloradoItemId));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that should NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").containsAny(QueryMasterSetup.connecticutItemId));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testEnumerationNotContainsAny()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find a match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").notContainsAny(QueryMasterSetup.connecticutItemId));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that should NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").notContainsAny(QueryMasterSetup.coloradoItemId));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testEnumerationContainsAll()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find a match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").containsAll(QueryMasterSetup.coloradoItemId, QueryMasterSetup.californiaItemId));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that should NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").containsAll(QueryMasterSetup.coloradoItemId, QueryMasterSetup.connecticutItemId));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testEnumerationNotContainsAll()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find a match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").notContainsAll(QueryMasterSetup.coloradoItemId, QueryMasterSetup.connecticutItemId));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that should NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").notContainsAll(QueryMasterSetup.coloradoItemId, QueryMasterSetup.californiaItemId));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testEnumerationContainsExactly()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find a match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").containsExactly(QueryMasterSetup.coloradoItemId, QueryMasterSetup.californiaItemId));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that should NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").containsExactly(QueryMasterSetup.coloradoItemId));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testBooleanEqualBoolean()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query for true values that should find exactly 2 matches
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aBoolean("enumQueryBoolean"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aBoolean("enumQueryBoolean").EQ(true));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query for false values that should find exactly 2
      // matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aBoolean("enumQueryBoolean"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aBoolean("enumQueryBoolean").EQ(false));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testBooleanEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query for true values that should find exactly 2 matches
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aBoolean("enumQueryBoolean"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aBoolean("enumQueryBoolean").EQ(MdAttributeBooleanInfo.TRUE));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query for false values that should find exactly 2
      // matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aBoolean("enumQueryBoolean"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aBoolean("enumQueryBoolean").EQ(MdAttributeBooleanInfo.FALSE));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testBooleanEqualNull()
  {
    OIterator<ValueObject> i = null;

    BusinessDAO enumObject1 = EnumerationItemDAO.get(QueryMasterSetup.californiaItemId).getBusinessDAO();
    String origValue1 = enumObject1.getValue("enumQueryBoolean");
    enumObject1.setValue("enumQueryBoolean", "");
    enumObject1.apply();

    BusinessDAO enumObject2 = EnumerationItemDAO.get(QueryMasterSetup.coloradoItemId).getBusinessDAO();
    String origValue2 = enumObject2.getValue("enumQueryBoolean");
    enumObject2.setValue("enumQueryBoolean", "");
    enumObject2.apply();

    try
    {
      // perform a query for null values
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aBoolean("enumQueryBoolean"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aBoolean("enumQueryBoolean").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query to find all objects with non-null queryBoolean
      // values
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aBoolean("enumQueryBoolean"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aBoolean("enumQueryBoolean").NE(""));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      enumObject1.setValue("enumQueryBoolean", origValue1);
      enumObject1.apply();

      enumObject2.setValue("enumQueryBoolean", origValue2);
      enumObject2.apply();

      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testBooleanNotEqualBoolean()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query for true values that should find exactly 2 matches
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aBoolean("enumQueryBoolean"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aBoolean("enumQueryBoolean").NE(false));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query for false values that should find exactly 2
      // matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aBoolean("enumQueryBoolean"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aBoolean("enumQueryBoolean").NE(true));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testBooleanNotEqualString()
  {
    try
    {
      // perform a query for true values that should find exactly 2 matches
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aBoolean("enumQueryBoolean"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aBoolean("enumQueryBoolean").NE(MdAttributeBooleanInfo.FALSE));

      OIterator<ValueObject> i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query for false values that should find exactly 2
      // matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aBoolean("enumQueryBoolean"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aBoolean("enumQueryBoolean").NE(MdAttributeBooleanInfo.TRUE));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testCharacterEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").EQ("enum character value"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").EQ("wrong character value"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testCharacterEqualString_IgnoreCase()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").EQi("ENUM CHARACTER VALUE"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").EQi("WRONG CHARACTER VALUE"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testCharacterEqualStringInArray()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").IN("wrong value 1", "enum character value", "wrong value 2"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").IN("wrong value 1", "wrong value 2", "wrong value 3", "ENUM CHARACTER VALUE"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testCharacterEqualStringInArray_IgnoreCase()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").INi("wrong value 1", "ENUM CHARACTER VALUE", "wrong value 2"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").IN("wrong value 1", "wrong value 2", "wrong value 3"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testCharacterEqualNull()
  {
    OIterator<ValueObject> i = null;

    BusinessDAO enumObject1 = EnumerationItemDAO.get(QueryMasterSetup.californiaItemId).getBusinessDAO();
    String origValue1 = enumObject1.getValue("enumQueryCharacter");
    enumObject1.setValue("enumQueryCharacter", "");
    enumObject1.apply();

    BusinessDAO enumObject2 = EnumerationItemDAO.get(QueryMasterSetup.coloradoItemId).getBusinessDAO();
    String origValue2 = enumObject2.getValue("enumQueryCharacter");
    enumObject2.setValue("enumQueryCharacter", "");
    enumObject2.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 3 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").NE(""));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      enumObject1.setValue("enumQueryCharacter", origValue1);
      enumObject1.apply();

      enumObject2.setValue("enumQueryCharacter", origValue2);
      enumObject2.apply();

      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testCharacterLikeString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").LIKE("%character%"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").LIKE("%character"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testCharacterLikeString_IgnoreCase()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").LIKEi("%CHARACTER%"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").LIKEi("%CHARACTER"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testCharacterNotEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter"), query.oid("objectId"));
      vQ.WHERE(OR.get(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").NE("wrong character value"), query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").EQ("")));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter"), query.oid("objectId"));
      vQ.WHERE(OR.get(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").NE("enum character value"), query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").EQ("")));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testCharacterNotEqualString_IgnoreCase()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter"), query.oid("objectId"));
      vQ.WHERE(OR.get(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").NEi("WRONG CHARACTER VALUE"), query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").EQ("")));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter"), query.oid("objectId"));
      vQ.WHERE(OR.get(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").NEi("ENUM CHARACTER VALUE"), query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").EQ("")));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testCharacterNotEqualStringInArray()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter"), query.oid("objectId"));
      vQ.WHERE(OR.get(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").NI("wrong character value 1", "wrong character value 2", "wrong character value 3"), query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").EQ("")));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter"), query.oid("objectId"));
      vQ.WHERE(OR.get(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").NI("wrong value", "enum character value", "wrong value 2"), query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").EQ("")));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testCharacterNotEqualStringInArray_IgnoreCase()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter"), query.oid("objectId"));
      vQ.WHERE(OR.get(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").NIi("WRONG VALUE 1", "WRONG VALUE 2", "WRONG VALUE 3"), query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").EQ("")));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter"), query.oid("objectId"));
      vQ.WHERE(OR.get(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").NIi("WRONG VALUE", "ENUM CHARACTER VALUE", "WRONG VALUE 2"), query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").EQ("")));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testCharacterNotLikeString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform another query that should find 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter"), query.oid("objectId"));
      vQ.WHERE(OR.get(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").NLIKE("%wrong%"), query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").EQ("")));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter"), query.oid("objectId"));
      vQ.WHERE(OR.get(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").NLIKE("%character%"), query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").EQ("")));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testCharacterNotLikeString_IgnoreCase()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform another query that should find 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter"), query.oid("objectId"));
      vQ.WHERE(OR.get(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").NLIKEi("%WRONG%"), query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").EQ("")));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter"), query.oid("objectId"));
      vQ.WHERE(OR.get(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").NLIKEi("%CHARACTER%"), query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").EQ("")));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDateEqualDate()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-06", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDate("enumQueryDate"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").EQ(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-05", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDate("enumQueryDate"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").EQ(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDateEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDate("enumQueryDate"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").EQ("2006-11-06"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDate("enumQueryDate"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").EQ("2006-05-05"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDateGTDate()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-05", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDate("enumQueryDate"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").GT(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-07", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDate("enumQueryDate"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").GT(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDateGTString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDate("enumQueryDate"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").GT("2006-11-05"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDate("enumQueryDate"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").GT("2006-11-07"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDateGEDate()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-06", new java.text.ParsePosition(0));

    try
    {
      // perform a query with a date equal to the stored
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDate("enumQueryDate"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").GE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query with a date less than the stored
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-05", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDate("enumQueryDate"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").GE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query with a date greater than the stored
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-07", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDate("enumQueryDate"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").GE(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDateGEString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query with a date equal to the stored
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDate("enumQueryDate"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").GE("2006-11-06"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query with a date less than the stored
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDate("enumQueryDate"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").GE("2006-11-05"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query with a date greater than the stored
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDate("enumQueryDate"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").GE("2006-11-07"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDateEqualNull()
  {
    OIterator<ValueObject> i = null;

    BusinessDAO enumObject1 = EnumerationItemDAO.get(QueryMasterSetup.californiaItemId).getBusinessDAO();
    String origValue1 = enumObject1.getValue("enumQueryDate");
    enumObject1.setValue("enumQueryDate", "");
    enumObject1.apply();

    BusinessDAO enumObject2 = EnumerationItemDAO.get(QueryMasterSetup.coloradoItemId).getBusinessDAO();
    String origValue2 = enumObject2.getValue("enumQueryDate");
    enumObject2.setValue("enumQueryDate", "");
    enumObject2.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDate("enumQueryDate"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDate("enumQueryDate"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").NE(""));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      enumObject1.setValue("enumQueryDate", origValue1);
      enumObject1.apply();

      enumObject2.setValue("enumQueryDate", origValue2);
      enumObject2.apply();

      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDateLTDate()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-07", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDate("enumQueryDate"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").LT(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-05", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDate("enumQueryDate"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").LT(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDateLTString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDate("enumQueryDate"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").LT("2006-11-07"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDate("enumQueryDate"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").LT("2006-11-05"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDateLEDate()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-06", new java.text.ParsePosition(0));

    try
    {
      // perform a query with a date equal to the stored
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDate("enumQueryDate"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").LE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query with a date less than the stored
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-07", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDate("enumQueryDate"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").LE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query with a date greater than the stored
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-05", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDate("enumQueryDate"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").LE(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDateLEString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query with a date equal to the stored
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDate("enumQueryDate"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").LE("2006-11-06"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query with a date less than the stored
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDate("enumQueryDate"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").LE("2006-11-07"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query with a date greater than the stored
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDate("enumQueryDate"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").LE("2006-11-05"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDateNotEqualDate()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-05", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDate("enumQueryDate"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").NE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-06", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDate("enumQueryDate"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").NE(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDateNotEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDate("enumQueryDate"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").NE("2006-11-05"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDate("enumQueryDate"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").NE("2006-11-06"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDateTimeEqualDateTime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-06 12:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").EQ(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-06 11:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").EQ(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDateTimeEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").EQ("2006-11-06 12:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").EQ("2006-11-06 11:00:00"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDateTimeGTDateTime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-06 11:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").GT(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-06 12:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").GT(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDateTimeGTString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").GT("2006-11-06 11:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").GT("2006-11-06 12:00:00"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDateTimeGEDateTime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-06 12:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").GE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-05 12:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").GE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-07 12:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").GE(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDateTimeGEString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").GE("2006-11-06 12:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").GE("2006-11-05 12:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").GE("2006-11-07 12:00:00"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDateTimeEqualNull()
  {
    OIterator<ValueObject> i = null;

    BusinessDAO enumObject1 = EnumerationItemDAO.get(QueryMasterSetup.californiaItemId).getBusinessDAO();
    String origValue1 = enumObject1.getValue("enumQueryDateTime");
    enumObject1.setValue("enumQueryDateTime", "");
    enumObject1.apply();

    BusinessDAO enumObject2 = EnumerationItemDAO.get(QueryMasterSetup.coloradoItemId).getBusinessDAO();
    String origValue2 = enumObject2.getValue("enumQueryDateTime");
    enumObject2.setValue("enumQueryDateTime", "");
    enumObject2.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").NE(""));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      enumObject1.setValue("enumQueryDateTime", origValue1);
      enumObject1.apply();

      enumObject2.setValue("enumQueryDateTime", origValue2);
      enumObject2.apply();

      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDateTimeLTDateTime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-07 12:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").LT(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-05 12:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").LT(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDateTimeLTString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").LT("2006-11-07 12:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").LT("2006-11-05 12:00:00"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDateTimeLEDateTime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-06 12:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").LE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-07 12:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").LE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-05 12:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").LE(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDateTimeLEString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").LE("2006-11-06 12:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").LE("2006-11-07 12:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").LE("2006-11-05 12:00:00"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDateTimeNotEqualDateTime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-05 12:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").NE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-06 12:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime"), query.oid("objectId"));
      vQ.WHERE(AND.get(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").NE(date), query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").NE("")));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDateTimeNotEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").NE("2006-11-05 12:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime"), query.oid("objectId"));
      vQ.WHERE(AND.get(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").NE("2006-11-06 12:00:00"), query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").NE("")));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDecimalEqualDecimal()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").EQ(new BigDecimal(200.5)));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").EQ(new BigDecimal(201.5)));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDecimalEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").EQ("200.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").EQ("201.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDecimalGTDecimal()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").GT(new BigDecimal(200)));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").GT(new BigDecimal(201)));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDecimalGTString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").GT("200"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").GT("201"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDecimalGEDecimal()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").GE(new BigDecimal(200.5)));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").GE(new BigDecimal(200)));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").GE(new BigDecimal(201)));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDecimalGEString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").GE("200.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").GE("200"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").GE("201"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDecimalEqualNull()
  {
    OIterator<ValueObject> i = null;

    BusinessDAO enumObject1 = EnumerationItemDAO.get(QueryMasterSetup.californiaItemId).getBusinessDAO();
    String origValue1 = enumObject1.getValue("enumQueryDecimal");
    enumObject1.setValue("enumQueryDecimal", "");
    enumObject1.apply();

    BusinessDAO enumObject2 = EnumerationItemDAO.get(QueryMasterSetup.coloradoItemId).getBusinessDAO();
    String origValue2 = enumObject2.getValue("enumQueryDecimal");
    enumObject2.setValue("enumQueryDecimal", "");
    enumObject2.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").NE(""));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      enumObject1.setValue("enumQueryDecimal", origValue1);
      enumObject1.apply();

      enumObject2.setValue("enumQueryDecimal", origValue2);
      enumObject2.apply();

      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDecimalLTDecimal()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").LT(new BigDecimal(201)));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").LT(new BigDecimal(200)));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDecimalLTString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").LT("201"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").LT("200"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDecimalLEDecimal()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").LE(new BigDecimal(200.5)));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").LE(new BigDecimal(201)));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").LE(new BigDecimal(99)));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDecimalLEString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").LE("200.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").LE("201"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").LE("99"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDecimalNotEqualDecimal()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").NE(new BigDecimal(201)));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").NE(new BigDecimal(200.5)));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDecimalNotEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").NE("201"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").NE("200.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDoubleEqualDouble()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").EQ(200.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").EQ(201.5));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDoubleEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").EQ("200.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").EQ("201.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDoubleGTDouble()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").GT(20.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").GT(220.5));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDoubleGTString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").GT("20.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").GT("220.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDoubleGEDouble()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equal to
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").GE(200.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").GE(20.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").GE(220.5));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDoubleGEString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equal to
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").GE("200.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").GE("20.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").GE("220.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDoubleEqualNull()
  {
    OIterator<ValueObject> i = null;

    BusinessDAO enumObject1 = EnumerationItemDAO.get(QueryMasterSetup.californiaItemId).getBusinessDAO();
    String origValue1 = enumObject1.getValue("enumQueryDouble");
    enumObject1.setValue("enumQueryDouble", "");
    enumObject1.apply();

    BusinessDAO enumObject2 = EnumerationItemDAO.get(QueryMasterSetup.coloradoItemId).getBusinessDAO();
    String origValue2 = enumObject2.getValue("enumQueryDouble");
    enumObject2.setValue("enumQueryDouble", "");
    enumObject2.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").NE(""));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      enumObject1.setValue("enumQueryDouble", origValue1);
      enumObject1.apply();

      enumObject2.setValue("enumQueryDouble", origValue2);
      enumObject2.apply();

      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDoubleLTDouble()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").LT(220.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").LT(20.5));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDoubleLTString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").LT("220.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").LT("20.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDoubleLEDouble()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equal
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").LE(200.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on less than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").LE(220.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").LE(20.5));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDoubleLEString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equal
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").LE("200.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on less than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").LE("220.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").LE("20.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDoubleNotEqualDouble()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").NE(201.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").NE(200.5));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDoubleNotEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").NE("201.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").NE("200.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testFloatEqualFloat()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").EQ((float) 200.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").EQ((float) 201.5));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testFloatEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").EQ("200.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").EQ("201.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testFloatGTFloat()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").GT((float) 20.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").GT((float) 221.5));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testFloatGTString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").GT("20.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").GT("221.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testFloatGEFloat()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").GE((float) 200.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").GE((float) 21.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").GE((float) 221.5));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testFloatGEString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").GE("200.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").GE("21.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").GE("221.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testFloatEqualNull()
  {
    OIterator<ValueObject> i = null;

    BusinessDAO enumObject1 = EnumerationItemDAO.get(QueryMasterSetup.californiaItemId).getBusinessDAO();
    String origValue1 = enumObject1.getValue("enumQueryFloat");
    enumObject1.setValue("enumQueryFloat", "");
    enumObject1.apply();

    BusinessDAO enumObject2 = EnumerationItemDAO.get(QueryMasterSetup.coloradoItemId).getBusinessDAO();
    String origValue2 = enumObject2.getValue("enumQueryFloat");
    enumObject2.setValue("enumQueryFloat", "");
    enumObject2.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").NE(""));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      enumObject1.setValue("enumQueryFloat", origValue1);
      enumObject1.apply();

      enumObject2.setValue("enumQueryFloat", origValue2);
      enumObject2.apply();

      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testFloatLTFloat()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").LT((float) 220.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").LT((float) 21.5));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testFloatLTString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").LT("220.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").LT("21.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testFloatLEFloat()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").LE((float) 200.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on less than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").LE((float) 201.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").LE((float) 21.5));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testFloatLEString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").LE("200.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on less than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").LE("201.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").LE("21.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testFloatNotEqualFloat()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").NE((float) 210.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").NE((float) 200.5));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testFloatNotEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").NE("210.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").NE("200.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testIntegerEqualInteger()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").EQ(200));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").EQ(201));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testIntegerEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").EQ("200"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").EQ("201"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testIntegerGTInteger()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").GT(20));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").GT(201));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testIntegerGTString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").GT("20"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").GT("201"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testIntegerGEInteger()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").GE(200));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").GE(20));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").GE(201));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testIntegerGEString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").GE("200"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").GE("20"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").GE("201"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testIntegerEqualNull()
  {
    OIterator<ValueObject> i = null;

    BusinessDAO enumObject1 = EnumerationItemDAO.get(QueryMasterSetup.californiaItemId).getBusinessDAO();
    String origValue1 = enumObject1.getValue("enumQueryInteger");
    enumObject1.setValue("enumQueryInteger", "");
    enumObject1.apply();

    BusinessDAO enumObject2 = EnumerationItemDAO.get(QueryMasterSetup.coloradoItemId).getBusinessDAO();
    String origValue2 = enumObject2.getValue("enumQueryInteger");
    enumObject2.setValue("enumQueryInteger", "");
    enumObject2.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").NE(""));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      enumObject1.setValue("enumQueryInteger", origValue1);
      enumObject1.apply();

      enumObject2.setValue("enumQueryInteger", origValue2);
      enumObject2.apply();

      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testIntegerLTInteger()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").LT(201));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").LT(20));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testIntegerLTString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").LT("201"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").LT("20"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testIntegerLEInteger()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").LE(200));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").LE(201));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").LE(20));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testIntegerLEString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").LE("200"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").LE("201"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").LE("20"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testIntegerNotEqualInteger()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").NE(201));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").NE(200));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testIntegerNotEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").NE("201"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").NE("200"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testLongEqualLong()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aLong("enumQueryLong"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").EQ((long) 200));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aLong("enumQueryLong"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").EQ((long) 201));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testLongEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aLong("enumQueryLong"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").EQ("200"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aLong("enumQueryLong"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").EQ("201"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testLongGTLong()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aLong("enumQueryLong"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").GT((long) 20));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aLong("enumQueryLong"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").GT((long) 201));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testLongGTString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aLong("enumQueryLong"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").GT("20"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aLong("enumQueryLong"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").GT("201"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testLongGELong()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aLong("enumQueryLong"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").GE((long) 200));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aLong("enumQueryLong"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").GE((long) 20));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aLong("enumQueryLong"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").GE((long) 201));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testLongGEString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aLong("enumQueryLong"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").GE("200"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aLong("enumQueryLong"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").GE("20"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aLong("enumQueryLong"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").GE("201"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testLongEqualNull()
  {
    OIterator<ValueObject> i = null;

    BusinessDAO enumObject1 = EnumerationItemDAO.get(QueryMasterSetup.californiaItemId).getBusinessDAO();
    String origValue1 = enumObject1.getValue("enumQueryLong");
    enumObject1.setValue("enumQueryLong", "");
    enumObject1.apply();

    BusinessDAO enumObject2 = EnumerationItemDAO.get(QueryMasterSetup.coloradoItemId).getBusinessDAO();
    String origValue2 = enumObject2.getValue("enumQueryLong");
    enumObject2.setValue("enumQueryLong", "");
    enumObject2.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aLong("enumQueryLong"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aLong("enumQueryLong"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").NE(""));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      enumObject1.setValue("enumQueryLong", origValue1);
      enumObject1.apply();

      enumObject2.setValue("enumQueryLong", origValue2);
      enumObject2.apply();

      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testLongLTLong()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aLong("enumQueryLong"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").LT((long) 220));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aLong("enumQueryLong"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").LT((long) 20));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testLongLTString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aLong("enumQueryLong"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").LT("220"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aLong("enumQueryLong"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").LT("20"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testLongLELong()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aLong("enumQueryLong"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").LE((long) 200));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aLong("enumQueryLong"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").LE((long) 220));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aLong("enumQueryLong"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").LE((long) 20));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testLongLEString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aLong("enumQueryLong"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").LE("200"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aLong("enumQueryLong"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").LE("220"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aLong("enumQueryLong"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").LE("20"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testLongNotEqualLong()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aLong("enumQueryLong"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").NE((long) 20));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aLong("enumQueryLong"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").NE((long) 200));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testLongNotEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aLong("enumQueryLong"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").NE("20"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aLong("enumQueryLong"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").NE("200"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testTextEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aText("enumQueryText"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").EQ("enum text value"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aText("enumQueryText"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").NE("enum text value"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testTextEqualString_IgnoreCase()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aText("enumQueryText"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").EQi("ENUM TEXT VALUE"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aText("enumQueryText"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").EQi("WRONG TEXT VALUE"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testTextEqualStringInArray()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aText("enumQueryText"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").IN("ENUM text value", "enum text value", "wrong text value"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aText("enumQueryText"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").IN("ENUM text value", "ENUM TEXT value", "ENUM TEXT VALUE"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testTextEqualStringInArray_IgnoreCase()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aText("enumQueryText"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").INi("WRONG TEXT VALUE", "ENUM TEXT VALUE", "wrong TEXT value 2"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aText("enumQueryText"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").INi("WRONG TEXT VALUE", "WRONG TEXT VALUE 2", "WRONG TEXT VALUE 3"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testTextEqualNull()
  {
    OIterator<ValueObject> i = null;

    BusinessDAO enumObject1 = EnumerationItemDAO.get(QueryMasterSetup.californiaItemId).getBusinessDAO();
    String origValue1 = enumObject1.getValue("enumQueryText");
    enumObject1.setValue("enumQueryText", "");
    enumObject1.apply();

    BusinessDAO enumObject2 = EnumerationItemDAO.get(QueryMasterSetup.coloradoItemId).getBusinessDAO();
    String origValue2 = enumObject2.getValue("enumQueryText");
    enumObject2.setValue("enumQueryText", "");
    enumObject2.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aText("enumQueryText"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aText("enumQueryText"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").NE(""));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      enumObject1.setValue("enumQueryText", origValue1);
      enumObject1.apply();

      enumObject2.setValue("enumQueryText", origValue2);
      enumObject2.apply();

      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testTextLikeString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aText("enumQueryText"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").LIKE("%text%"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aText("enumQueryText"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").LIKE("%text"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testTextLikeString_IgnoreCase()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aText("enumQueryText"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").LIKEi("%TEXT%"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aText("enumQueryText"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").LIKEi("%TEXT"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testTextNotEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aText("enumQueryText"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").NE("wrong text value"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aText("enumQueryText"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").NE("enum text value"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testTextNotEqualString_IgnoreCase()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aText("enumQueryText"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").NEi("WRONG TEXT VALUE"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aText("enumQueryText"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").NEi("ENUM TEXT VALUE"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testTextNotEqualStringInArray()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aText("enumQueryText"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").NI("ENUM text value", "ENUM TEXT value", "ENUM TEXT VALUE"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aText("enumQueryText"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").NI("ENUM text value", "enum text value", "wrong text value"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testTextNotEqualStringInArray_IgnoreCase()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aText("enumQueryText"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").NIi("WRONG text value", "WRONG TEXT value 2", "WRONG TEXT VALUE 3"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aText("enumQueryText"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").NIi("ENUM text value", "ENUM TEXT VALUE", "ENUM TEXT value"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testTextNotLikeString_IgnoreCase()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aText("enumQueryText"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").NLIKEi("%WRONG%"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aText("enumQueryText"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").NLIKEi("%TEXT%"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testTimeEqualTime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aTime("enumQueryTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").EQ(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("11:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aTime("enumQueryTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").EQ(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testTimeEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aTime("enumQueryTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").EQ("12:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aTime("enumQueryTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").EQ("11:00:00"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testTimeGTTime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("11:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aTime("enumQueryTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").GT(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aTime("enumQueryTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").GT(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testTimeGTString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aTime("enumQueryTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").GT("11:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aTime("enumQueryTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").GT("14:00:00"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testTimeGETime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aTime("enumQueryTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").GE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aTime("enumQueryTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").GE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aTime("enumQueryTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").GE(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testTimeGEString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aTime("enumQueryTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").GE("12:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aTime("enumQueryTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").GE("12:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aTime("enumQueryTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").GE("14:00:00"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testTimeEqualNull()
  {
    OIterator<ValueObject> i = null;

    BusinessDAO enumObject1 = EnumerationItemDAO.get(QueryMasterSetup.californiaItemId).getBusinessDAO();
    String origValue1 = enumObject1.getValue("enumQueryTime");
    enumObject1.setValue("enumQueryTime", "");
    enumObject1.apply();

    BusinessDAO enumObject2 = EnumerationItemDAO.get(QueryMasterSetup.coloradoItemId).getBusinessDAO();
    String origValue2 = enumObject2.getValue("enumQueryTime");
    enumObject2.setValue("enumQueryTime", "");
    enumObject2.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aTime("enumQueryTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aTime("enumQueryTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").NE(""));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      enumObject1.setValue("enumQueryTime", origValue1);
      enumObject1.apply();

      enumObject2.setValue("enumQueryTime", origValue2);
      enumObject2.apply();

      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testTimeLTTime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aTime("enumQueryTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").LT(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("10:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aTime("enumQueryTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").LT(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testTimeLTString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aTime("enumQueryTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").LT("14:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aTime("enumQueryTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").LT("10:00:00"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testTimeLETime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aTime("enumQueryTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").LE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aTime("enumQueryTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").LE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("10:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aTime("enumQueryTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").LE(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testTimeLEString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aTime("enumQueryTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").LE("12:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aTime("enumQueryTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").LE("14:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aTime("enumQueryTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").LE("10:00:00"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testTimeNotEqualTime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("10:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aTime("enumQueryTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").NE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aTime("enumQueryTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").NE(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testTimeNotEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aEnumeration("queryEnumeration").aTime("enumQueryTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").NE("10:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aEnumeration("queryEnumeration").aTime("enumQueryTime"), query.oid("objectId"));
      vQ.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").NE("12:00:00"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        Assert.fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

}
