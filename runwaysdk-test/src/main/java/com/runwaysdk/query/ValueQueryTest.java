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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.business.BusinessQuery;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeBooleanUtil;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.session.Request;

public class ValueQueryTest
{
  private static List<String> testObjectIdList = new LinkedList<String>();

  private static List<String> objectList;

  @Request
  @BeforeClass
  public static void classSetUp()
  {
    BusinessDAO testQueryObject1 = BusinessDAO.newInstance(QueryMasterSetup.childMdBusiness.definesType());
    testQueryObject1.setValue("queryBoolean", MdAttributeBooleanInfo.TRUE);
    testQueryObject1.setValue("queryInteger", "200");
    testQueryObject1.apply();

    testObjectIdList.add(testQueryObject1.getOid());

    BusinessDAO testQueryObject2 = BusinessDAO.newInstance(QueryMasterSetup.childMdBusiness.definesType());
    testQueryObject2.setValue("queryBoolean", MdAttributeBooleanInfo.FALSE);
    testQueryObject2.setValue("queryInteger", "150");
    testQueryObject2.apply();

    testObjectIdList.add(testQueryObject2.getOid());

    BusinessDAO testQueryObject3 = BusinessDAO.newInstance(QueryMasterSetup.childMdBusiness.definesType());
    testQueryObject3.setValue("queryBoolean", MdAttributeBooleanInfo.FALSE);
    testQueryObject3.setValue("queryInteger", "250");
    testQueryObject3.apply();

    testObjectIdList.add(testQueryObject3.getOid());

    BusinessDAO testQueryObject4 = BusinessDAO.newInstance(QueryMasterSetup.childMdBusiness.definesType());
    testQueryObject4.setValue("queryDate", "");
    testQueryObject4.apply();

    testObjectIdList.add(testQueryObject4.getOid());

    objectList = MdBusinessDAO.getEntityIdsDB(QueryMasterSetup.childQueryInfo.getType());
  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    for (String oid : testObjectIdList)
    {
      BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();
      testQueryObject.delete();
    }
  }

  /**
   * Testing windowing functions on EntityQueries.
   */
  @Request
  @Test
  public void testWindowFunctionEntityQuery() throws Exception
  {
    OIterator<ValueObject> i = null;

    try
    {
      QueryFactory qf = new QueryFactory();

      // Query number of OID fields

      ValueQuery vQ1 = qf.valueQuery();
      BusinessDAOQuery mdAttrQ = qf.businessDAOQuery(MdAttributeConcreteInfo.CLASS);

      vQ1.SELECT(F.COUNT(mdAttrQ.get(MdAttributeConcreteInfo.NAME), "count"), mdAttrQ.get(MdAttributeConcreteInfo.NAME), mdAttrQ.get(MdAttributeConcreteInfo.REQUIRED));
      vQ1.GROUP_BY(mdAttrQ.get(MdAttributeConcreteInfo.NAME), mdAttrQ.get(MdAttributeConcreteInfo.REQUIRED));
      vQ1.ORDER_BY_DESC(F.COUNT(mdAttrQ.get(MdAttributeConcreteInfo.NAME), "count"));

      Map<Integer, Set<String>> reqAttrCountMap = new HashMap<Integer, Set<String>>();
      Map<Integer, Set<String>> notReqAttrCountMap = new HashMap<Integer, Set<String>>();

      i = vQ1.getIterator();
      try
      {
        for (ValueObject valueObject : i)
        {
          Integer idCount = Integer.parseInt(valueObject.getValue("count"));
          String attributeName = valueObject.getValue(MdAttributeConcreteInfo.NAME);
          Boolean required = MdAttributeBooleanUtil.getBooleanValue(valueObject.getValue(MdAttributeConcreteInfo.REQUIRED));

          if (required)
          {
            Set<String> reqSet = reqAttrCountMap.get(idCount);
            if (reqSet == null)
            {
              reqSet = new HashSet<String>();
              reqAttrCountMap.put(idCount, reqSet);
            }
            reqSet.add(attributeName);
          }
          else
          {
            Set<String> notReqSet = notReqAttrCountMap.get(idCount);
            if (notReqSet == null)
            {
              notReqSet = new HashSet<String>();
              notReqAttrCountMap.put(idCount, notReqSet);
            }
            notReqSet.add(attributeName);
          }
        }
      }
      finally
      {
        i.close();
      }

      ValueQuery vQ2 = qf.valueQuery();

      vQ2.SELECT_DISTINCT(F.COUNT(mdAttrQ.get(MdAttributeConcreteInfo.NAME), "count"), F.STRING_AGG(mdAttrQ.get(MdAttributeConcreteInfo.NAME), ", ", "STRING_AGG").OVER(F.PARTITION_BY(F.COUNT(mdAttrQ.get(MdAttributeConcreteInfo.NAME)), mdAttrQ.get(MdAttributeConcreteInfo.REQUIRED))), mdAttrQ.RANK().OVER(F.PARTITION_BY(mdAttrQ.get(MdAttributeConcreteInfo.REQUIRED)), new OrderBy(F.COUNT(mdAttrQ.get(MdAttributeConcreteInfo.NAME)), OrderBy.SortOrder.DESC)), mdAttrQ.get(MdAttributeConcreteInfo.REQUIRED));
      vQ2.GROUP_BY(mdAttrQ.get(MdAttributeConcreteInfo.NAME), mdAttrQ.get(MdAttributeConcreteInfo.REQUIRED));
      vQ2.ORDER_BY_DESC(F.COUNT(mdAttrQ.get(MdAttributeConcreteInfo.NAME), "count"));

      i = vQ2.getIterator();

      try
      {
        for (ValueObject valueObject : i)
        {
          Integer idCount = Integer.parseInt(valueObject.getValue("count"));
          String stringAgg = valueObject.getValue("STRING_AGG");
          Boolean required = MdAttributeBooleanUtil.getBooleanValue(valueObject.getValue(MdAttributeConcreteInfo.REQUIRED));

          Set<String> reqSet;

          if (required)
          {
            reqSet = reqAttrCountMap.get(idCount);
          }
          else
          {
            reqSet = notReqAttrCountMap.get(idCount);
          }

          String[] windowValues = stringAgg.split(",");
          Assert.assertEquals("The number of values returned in the STRING_AGG function were not as expected.", reqSet.toArray().length, windowValues.length);
          for (String windowValue : windowValues)
          {
            Assert.assertTrue("Window function did not return an expected value from the STRING_AGG function", reqSet.contains(windowValue.trim()));
          }
        }
      }
      finally
      {
        i.close();
      }
    }
    finally
    {
      if (i != null)
      {
        i.close();
      }
    }
  }

  /**
   * Testing windowing functions on ValueQueries.
   */
  @Request
  @Test
  public void testWindowFunctionValueQuery() throws Exception
  {
    OIterator<ValueObject> i = null;

    try
    {
      QueryFactory qf = new QueryFactory();

      // Query number of OID fields

      ValueQuery vQ1 = qf.valueQuery();
      BusinessDAOQuery mdAttrQ = qf.businessDAOQuery(MdAttributeConcreteInfo.CLASS);

      vQ1.SELECT(F.COUNT(mdAttrQ.get(MdAttributeConcreteInfo.NAME), "count"), mdAttrQ.get(MdAttributeConcreteInfo.NAME), mdAttrQ.get(MdAttributeConcreteInfo.REQUIRED));
      vQ1.GROUP_BY(mdAttrQ.get(MdAttributeConcreteInfo.NAME), mdAttrQ.get(MdAttributeConcreteInfo.REQUIRED));
      vQ1.ORDER_BY_DESC(F.COUNT(mdAttrQ.get(MdAttributeConcreteInfo.NAME), "count"));

      Map<Integer, Set<String>> reqAttrCountMap = new HashMap<Integer, Set<String>>();
      Map<Integer, Set<String>> notReqAttrCountMap = new HashMap<Integer, Set<String>>();

      i = vQ1.getIterator();
      try
      {
        for (ValueObject valueObject : i)
        {
          Integer idCount = Integer.parseInt(valueObject.getValue("count"));
          String attributeName = valueObject.getValue(MdAttributeConcreteInfo.NAME);
          Boolean required = MdAttributeBooleanUtil.getBooleanValue(valueObject.getValue(MdAttributeConcreteInfo.REQUIRED));

          if (required)
          {
            Set<String> reqSet = reqAttrCountMap.get(idCount);
            if (reqSet == null)
            {
              reqSet = new HashSet<String>();
              reqAttrCountMap.put(idCount, reqSet);
            }
            reqSet.add(attributeName);
          }
          else
          {
            Set<String> notReqSet = notReqAttrCountMap.get(idCount);
            if (notReqSet == null)
            {
              notReqSet = new HashSet<String>();
              notReqAttrCountMap.put(idCount, notReqSet);
            }
            notReqSet.add(attributeName);
          }
        }
      }
      finally
      {
        i.close();
      }

      ValueQuery vQ2 = qf.valueQuery();

      vQ2.SELECT(mdAttrQ.get(MdAttributeConcreteInfo.NAME, "name"), mdAttrQ.get(MdAttributeConcreteInfo.REQUIRED, "required"));
      ValueQuery vQ3 = qf.valueQuery();

      // Test the RANK function on ValueQueries
      vQ3.SELECT_DISTINCT(F.COUNT(vQ2.get("name"), "count"), F.STRING_AGG(vQ2.get("name"), ", ", "STRING_AGG").OVER(F.PARTITION_BY(F.COUNT(vQ2.get("name")), vQ2.get("required"))), vQ2.RANK().OVER(F.PARTITION_BY(vQ2.get("required")), new OrderBy(F.COUNT(vQ2.get("name")), OrderBy.SortOrder.DESC)), vQ2.get("required"));
      vQ3.GROUP_BY(vQ2.get("name"), vQ2.get("required"));
      vQ3.ORDER_BY_DESC(F.COUNT(vQ2.get("name"), "count"));

      i = vQ3.getIterator();

      try
      {
        for (ValueObject valueObject : i)
        {
          Integer idCount = Integer.parseInt(valueObject.getValue("count"));
          String stringAgg = valueObject.getValue("STRING_AGG");
          Boolean required = MdAttributeBooleanUtil.getBooleanValue(valueObject.getValue(MdAttributeConcreteInfo.REQUIRED));

          Set<String> reqSet;

          if (required)
          {
            reqSet = reqAttrCountMap.get(idCount);
          }
          else
          {
            reqSet = notReqAttrCountMap.get(idCount);
          }

          String[] windowValues = stringAgg.split(",");
          Assert.assertEquals("The number of values returned in the STRING_AGG function were not as expected.", reqSet.toArray().length, windowValues.length);
          for (String windowValue : windowValues)
          {
            Assert.assertTrue("Window function did not return an expected value from the STRING_AGG function", reqSet.contains(windowValue.trim()));
          }
        }
      }
      finally
      {
        i.close();
      }
    }
    finally
    {
      if (i != null)
      {
        i.close();
      }
    }
  }

  @Request
  @Test
  public void testUnion()
  {
    OIterator<ValueObject> i = null;

    try
    {
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      ValueQuery vQ2 = qf.valueQuery();
      ValueQuery vQ3 = qf.valueQuery();
      BusinessDAOQuery childRefQuery = qf.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      BusinessDAOQuery childQuery = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(childQuery.aCharacter("queryCharacter"), childQuery.oid("oid"));
      vQ.WHERE(childQuery.aCharacter("queryCharacter").NE(""));

      vQ2.SELECT(childRefQuery.aCharacter("refQueryCharacter"), childRefQuery.oid("oid"));
      vQ2.WHERE(childRefQuery.aCharacter("refQueryCharacter").NE(""));

      vQ3.UNION(vQ, vQ2);

      i = vQ3.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("The query did not return any results when it should have.");
      }

      int count = 0;

      while (i.hasNext())
      {
        i.next();

        count++;
      }

      int expected = 0;

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        if (!testQueryObject.getValue("queryCharacter").equals(""))
          expected++;
      }
      for (String oid : MdBusinessDAO.getEntityIdsDB(QueryMasterSetup.childRefQueryInfo.getType()))
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        if (!testQueryObject.getValue("refQueryCharacter").equals(""))
          expected++;
      }

      if (expected != count)
      {
        Assert.fail("The value query union did not return the proper number of results. Expected is " + expected + ", it returned " + count + ".");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
      {
        i.close();
      }
    }
  }

  /**
   * The UNION ALL operator does not exclude duplicate rows so we test this
   * through the API by UNION ALL a table with itself and ensuring there's twice
   * the rows of a normal UNION
   */
  @Request
  @Test
  public void testUnionAll()
  {
    try
    {
      // UNION
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      ValueQuery vQ2 = qf.valueQuery();
      ValueQuery vQ3 = qf.valueQuery();
      BusinessDAOQuery childQuery1 = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery childQuery2 = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(childQuery1.oid("oid"));
      vQ2.SELECT(childQuery2.oid("oid"));

      vQ3.UNION(vQ, vQ2);

      long expected = 2 * vQ3.getCount();

      // UNION ALL
      qf = new QueryFactory();

      vQ = qf.valueQuery();
      vQ2 = qf.valueQuery();
      vQ3 = qf.valueQuery();
      childQuery1 = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      childQuery2 = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(childQuery1.oid("oid"));
      vQ2.SELECT(childQuery2.oid("oid"));

      vQ3.UNION_ALL(vQ, vQ2);

      long count = vQ3.getCount();

      if (expected != count)
      {
        Assert.fail("The value query UNION ALL did not return the proper number of results. Expected is " + expected + ", it returned " + count + ".");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testLeftJoinEqualSelectable()
  {
    OIterator<ValueObject> i = null;

    try
    {
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery childRefQuery = qf.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      BusinessDAOQuery childQuery = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(childRefQuery.aCharacter("refQueryCharacter"), childQuery.aBoolean("queryBoolean"), childQuery.aReference("reference"), childQuery.aInteger("queryInteger"));
      vQ.WHERE(childQuery.aInteger("queryInteger").LEFT_JOIN_EQ(childRefQuery.aInteger("refQueryInteger")));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("The query did not return any results when it should have.");
      }

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (!o.getValue("refQueryCharacter").equals("")) // this is an object
        // from childRefQuery
        {
          if (!o.getValue("queryInteger").equals("200"))
            Assert.fail("The Left Join query returned a resultant object with incorrect integer attributes. queryInteger = '" + o.getValue("queryInteger") + "'");
        }
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
  public void testLeftJoinEqualQuery()
  {
    OIterator<ValueObject> i = null;

    try
    {
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery childRefQuery = qf.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      BusinessDAOQuery childQuery = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(childRefQuery.aCharacter("refQueryCharacter"), childRefQuery.aInteger("refQueryInteger"));
      vQ.WHERE(childQuery.aReference("reference").LEFT_JOIN_EQ(childRefQuery));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("The query did not return any results when it should have.");
      }

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (!o.getValue("refQueryCharacter").equals("")) // this is an object
        // from childRefQuery
        {
          if (!o.getValue("refQueryInteger").equals("200"))
            Assert.fail("The Left Join query returned a resultant object with incorrect integer attributes. queryInteger = '" + o.getValue("refQueryInteger") + "'");
        }
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
  public void testLeftJoinNotEqualSelectable()
  {
    OIterator<ValueObject> i = null;

    try
    {
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery childRefQuery = qf.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      BusinessDAOQuery childQuery = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(childRefQuery.aCharacter("refQueryCharacter"), childQuery.aBoolean("queryBoolean"), childQuery.aReference("reference"), childQuery.aInteger("queryInteger"));
      vQ.WHERE(childQuery.aInteger("queryInteger").LEFT_JOIN_NE(childRefQuery.aInteger("refQueryInteger")));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("The query did not return any results when it should have.");
      }

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (!o.getValue("refQueryCharacter").equals("")) // this is an object
        // from childRefQuery
        {
          if (o.getValue("queryInteger").equals("200"))
            Assert.fail("The Left Join query returned a resultant object with incorrect integer attributes. queryInteger = '" + o.getValue("queryInteger") + "'");
        }
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
  public void testLeftJoinNotEqualQuery()
  {
    OIterator<ValueObject> i = null;

    try
    {
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery childRefQuery = qf.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      BusinessDAOQuery childQuery = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(childRefQuery.aCharacter("refQueryCharacter"), childRefQuery.aInteger("refQueryInteger"));
      vQ.WHERE(childQuery.aReference("reference").LEFT_JOIN_NE(childRefQuery));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("The query did not return any results when it should have.");
      }

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (!o.getValue("refQueryCharacter").equals("")) // this is an object
        // from childRefQuery
        {
          if (!o.getValue("refQueryInteger").equals("200"))
            Assert.fail("The Left Join query returned a resultant object with incorrect integer attributes. refQueryInteger = '" + o.getValue("refQueryInteger") + "'");
        }
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
  public void testLeftJoinGreaterOrEqualSelectable()
  {
    OIterator<ValueObject> i = null;

    try
    {
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery childRefQuery = qf.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      BusinessDAOQuery childQuery = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(childRefQuery.aCharacter("refQueryCharacter"), childQuery.aBoolean("queryBoolean"), childQuery.aReference("reference"), childQuery.aInteger("queryInteger"));
      vQ.WHERE(childQuery.aInteger("queryInteger").LEFT_JOIN_GE(childRefQuery.aInteger("refQueryInteger")));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("The query did not return any results when it should have.");
      }

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (!o.getValue("refQueryCharacter").equals("")) // this is an object
        // from childRefQuery
        {
          if (Integer.parseInt(o.getValue("queryInteger")) < 200)
            Assert.fail("The Left Join query returned a resultant object with incorrect integer attributes. queryInteger = '" + o.getValue("queryInteger") + "'");
        }
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
  public void testLeftJoinGreaterOrEqualQuery()
  {
    OIterator<ValueObject> i = null;

    try
    {
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery childRefQuery = qf.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      BusinessDAOQuery childQuery = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(childRefQuery.aCharacter("refQueryCharacter"), childRefQuery.aInteger("refQueryInteger"));
      vQ.WHERE(childQuery.aReference("reference").LEFT_JOIN_GE(childRefQuery));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("The query did not return any results when it should have.");
      }

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (!o.getValue("refQueryCharacter").equals("")) // this is an object
        // from childRefQuery
        {
          if (!o.getValue("refQueryInteger").equals("200"))
            Assert.fail("The Left Join query returned a resultant object with incorrect integer attributes. refQueryInteger = '" + o.getValue("refQueryInteger") + "'");
        }
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
  public void testLeftJoinGreaterThanSelectable()
  {
    OIterator<ValueObject> i = null;

    try
    {
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery childRefQuery = qf.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      BusinessDAOQuery childQuery = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(childRefQuery.aCharacter("refQueryCharacter"), childQuery.aBoolean("queryBoolean"), childQuery.aReference("reference"), childQuery.aInteger("queryInteger"));
      vQ.WHERE(childQuery.aInteger("queryInteger").LEFT_JOIN_GT(childRefQuery.aInteger("refQueryInteger")));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("The query did not return any results when it should have.");
      }

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (!o.getValue("refQueryCharacter").equals("")) // this is an object
        // from childRefQuery
        {
          if (Integer.parseInt(o.getValue("queryInteger")) <= 200)
            Assert.fail("The Left Join query returned a resultant object with incorrect integer attributes. queryInteger = '" + o.getValue("queryInteger") + "'");
        }
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
  public void testLeftJoinGreaterThanQuery()
  {
    OIterator<ValueObject> i = null;

    try
    {
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery childRefQuery = qf.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      BusinessDAOQuery childQuery = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(childRefQuery.aCharacter("refQueryCharacter"), childRefQuery.aInteger("refQueryInteger"));
      vQ.WHERE(childQuery.aReference("reference").LEFT_JOIN_GT(childRefQuery));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("The query did not return any results when it should have.");
      }

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (!o.getValue("refQueryCharacter").equals("")) // this is an object
        // from childRefQuery
        {
          if (!o.getValue("refQueryInteger").equals("200"))
            Assert.fail("The Left Join query returned a resultant object with incorrect integer attributes. refQueryInteger = '" + o.getValue("refQueryInteger") + "'");
        }
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
  public void testLeftJoinLessOrEqualSelectable()
  {
    OIterator<ValueObject> i = null;

    try
    {
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery childRefQuery = qf.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      BusinessDAOQuery childQuery = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(childRefQuery.aCharacter("refQueryCharacter"), childQuery.aBoolean("queryBoolean"), childQuery.aReference("reference"), childQuery.aInteger("queryInteger"));
      vQ.WHERE(childQuery.aInteger("queryInteger").LEFT_JOIN_LE(childRefQuery.aInteger("refQueryInteger")));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("The query did not return any results when it should have.");
      }

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (!o.getValue("refQueryCharacter").equals("")) // this is an object
        // from childRefQuery
        {
          if (Integer.parseInt(o.getValue("queryInteger")) > 200)
            Assert.fail("The Left Join query returned a resultant object with incorrect integer attributes. queryInteger = '" + o.getValue("queryInteger") + "'");
        }
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
  public void testLeftJoinLessOrEqualQuery()
  {
    OIterator<ValueObject> i = null;

    try
    {
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery childRefQuery = qf.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      BusinessDAOQuery childQuery = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(childRefQuery.aCharacter("refQueryCharacter"), childRefQuery.aInteger("refQueryInteger"));
      vQ.WHERE(childQuery.aReference("reference").LEFT_JOIN_LE(childRefQuery));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("The query did not return any results when it should have.");
      }

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (!o.getValue("refQueryCharacter").equals("")) // this is an object
        // from childRefQuery
        {
          if (!o.getValue("refQueryInteger").equals("200"))
            Assert.fail("The Left Join query returned a resultant object with incorrect integer attributes. refQueryInteger = '" + o.getValue("refQueryInteger") + "'");
        }
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
  public void testLeftJoinLessThanSelectable()
  {
    OIterator<ValueObject> i = null;

    try
    {
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery childRefQuery = qf.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      BusinessDAOQuery childQuery = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(childRefQuery.aCharacter("refQueryCharacter"), childQuery.aBoolean("queryBoolean"), childQuery.aReference("reference"), childQuery.aInteger("queryInteger"));
      vQ.WHERE(childQuery.aInteger("queryInteger").LEFT_JOIN_LT(childRefQuery.aInteger("refQueryInteger")));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("The query did not return any results when it should have.");
      }

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (!o.getValue("refQueryCharacter").equals("")) // this is an object
        // from childRefQuery
        {
          if (Integer.parseInt(o.getValue("queryInteger")) >= 200)
            Assert.fail("The Left Join query returned a resultant object with incorrect integer attributes. queryInteger = '" + o.getValue("queryInteger") + "'");
        }
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
  public void testLeftJoinLessThanQuery()
  {
    OIterator<ValueObject> i = null;

    try
    {
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery childRefQuery = qf.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      BusinessDAOQuery childQuery = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(childRefQuery.aCharacter("refQueryCharacter"), childRefQuery.aInteger("refQueryInteger"));
      vQ.WHERE(childQuery.aReference("reference").LEFT_JOIN_LT(childRefQuery));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("The query did not return any results when it should have.");
      }

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (!o.getValue("refQueryCharacter").equals("")) // this is an object
        // from childRefQuery
        {
          if (!o.getValue("refQueryInteger").equals("200"))
            Assert.fail("The Left Join query returned a resultant object with incorrect integer attributes. refQueryInteger = '" + o.getValue("refQueryInteger") + "'");
        }
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

      vQ.SELECT(query.aBoolean("queryBoolean"), query.oid("objectId"));
      vQ.WHERE(query.aBoolean("queryBoolean").EQ(true));

      // vQ.WHERE( query.LEFT_JOIN_EQ(query.aBoolean("queryBoolean"),
      // query.aCharacter("queryCharacter")) );

      i = vQ.getIterator();

      int count = 0;

      while (i.hasNext())
      {
        ValueObject o = i.next();
        boolean value = MdAttributeBooleanUtil.getBooleanValue(o.getValue("queryBoolean"));

        Assert.assertTrue(value);

        count++;
      }

      int expected = 0;

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        if (testQueryObject.getValue("queryBoolean").equals("true"))
          expected++;
      }

      Assert.assertEquals(expected, count);

      // perform another query for false values that should find exactly 2
      // matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aBoolean("queryBoolean"), query.oid("objectId"));
      vQ.WHERE(query.aBoolean("queryBoolean").EQ(false));

      i = vQ.getIterator();

      count = 0;

      while (i.hasNext())
      {
        ValueObject o = i.next();
        boolean value = MdAttributeBooleanUtil.getBooleanValue(o.getValue("queryBoolean"));

        Assert.assertFalse(value);

        count++;
      }

      expected = 0;

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        if (testQueryObject.getValue("queryBoolean").equals("false"))
          expected++;
      }

      Assert.assertEquals(expected, count);
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

      vQ.SELECT(query.aBoolean("queryBoolean"), query.oid("objectId"));
      vQ.WHERE(query.aBoolean("queryBoolean").EQ(MdAttributeBooleanInfo.TRUE));

      i = vQ.getIterator();

      int count = 0;

      while (i.hasNext())
      {
        ValueObject o = i.next();
        boolean value = MdAttributeBooleanUtil.getBooleanValue(o.getValue("queryBoolean"));

        Assert.assertTrue(value);

        count++;
      }

      int expected = 0;

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        if (testQueryObject.getValue("queryBoolean").equals("true"))
          expected++;
      }

      Assert.assertEquals(expected, count);

      // perform another query for false values that should find exactly 2
      // matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aBoolean("queryBoolean"), query.oid("objectId"));
      vQ.WHERE(query.aBoolean("queryBoolean").EQ(MdAttributeBooleanInfo.FALSE));

      i = vQ.getIterator();

      count = 0;

      while (i.hasNext())
      {
        ValueObject o = i.next();
        boolean value = MdAttributeBooleanUtil.getBooleanValue(o.getValue("queryBoolean"));

        Assert.assertFalse(value);

        count++;
      }

      expected = 0;

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        if (testQueryObject.getValue("queryBoolean").equals("false"))
          expected++;
      }

      Assert.assertEquals(expected, count);
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

    String origValue = QueryMasterSetup.testQueryObject1.getValue("queryBoolean");
    QueryMasterSetup.testQueryObject1.setValue("queryBoolean", "");
    QueryMasterSetup.testQueryObject1.apply();

    try
    {
      // perform a query for null values
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aBoolean("queryBoolean"), query.oid("objectId"));
      vQ.WHERE(query.aBoolean("queryBoolean").EQ(""));

      i = vQ.getIterator();

      int count = 0;

      while (i.hasNext())
      {
        ValueObject o = i.next();
        String value = o.getValue("queryBoolean");

        Assert.assertEquals("", value);

        count++;
      }

      int expected = 0;

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        if (testQueryObject.getValue("queryBoolean").equals(""))
          expected++;
      }

      Assert.assertEquals(expected, count);

      // perform another query to find all objects with non-null queryBoolean
      // values
      vQ = qf.valueQuery();

      vQ.SELECT(query.aBoolean("queryBoolean"), query.oid("objectId"));
      vQ.WHERE(query.aBoolean("queryBoolean").NE(""));

      i = vQ.getIterator();

      count = 0;

      while (i.hasNext())
      {
        i.next();
        count++;
      }

      expected = 0;

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        if (!testQueryObject.getValue("queryBoolean").equals(""))
          expected++;
      }

      Assert.assertEquals(expected, count);
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.testQueryObject1.setValue("queryBoolean", origValue);
      QueryMasterSetup.testQueryObject1.apply();

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

      vQ.SELECT(query.aBoolean("queryBoolean"), query.oid("objectId"));
      vQ.WHERE(query.aBoolean("queryBoolean").NE(false));

      i = vQ.getIterator();

      int count = 0;

      while (i.hasNext())
      {
        ValueObject o = i.next();
        boolean value = MdAttributeBooleanUtil.getBooleanValue(o.getValue("queryBoolean"));

        Assert.assertTrue(value);

        count++;
      }

      int expected = 0;

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        if (testQueryObject.getValue("queryBoolean").equals("true"))
          expected++;
      }

      Assert.assertEquals(expected, count);

      // perform another query for false values that should find exactly 2
      // matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aBoolean("queryBoolean"), query.oid("objectId"));
      vQ.WHERE(query.aBoolean("queryBoolean").NE(true));

      i = vQ.getIterator();

      count = 0;

      while (i.hasNext())
      {
        ValueObject o = i.next();
        boolean value = MdAttributeBooleanUtil.getBooleanValue(o.getValue("queryBoolean"));

        Assert.assertFalse(value);

        count++;
      }

      expected = 0;

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        if (testQueryObject.getValue("queryBoolean").equals("false"))
          expected++;
      }

      Assert.assertEquals(expected, count);
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

      vQ.SELECT(query.aBoolean("queryBoolean"), query.oid("objectId"));
      vQ.WHERE(query.aBoolean("queryBoolean").NE(MdAttributeBooleanInfo.FALSE));

      OIterator<ValueObject> i = vQ.getIterator();

      int count = 0;

      while (i.hasNext())
      {
        ValueObject o = i.next();
        boolean value = MdAttributeBooleanUtil.getBooleanValue(o.getValue("queryBoolean"));

        Assert.assertTrue(value);

        count++;
      }

      int expected = 0;

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        if (testQueryObject.getValue("queryBoolean").equals("false"))
          expected++;
      }

      Assert.assertEquals(expected, count);

      // perform another query for false values that should find exactly 2
      // matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aBoolean("queryBoolean"), query.oid("objectId"));
      vQ.WHERE(query.aBoolean("queryBoolean").NE(MdAttributeBooleanInfo.TRUE));

      i = vQ.getIterator();

      count = 0;

      while (i.hasNext())
      {
        ValueObject o = i.next();
        boolean value = MdAttributeBooleanUtil.getBooleanValue(o.getValue("queryBoolean"));

        Assert.assertFalse(value);

        count++;
      }

      expected = 0;

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        if (testQueryObject.getValue("queryBoolean").equals("true"))
          expected++;
      }

      Assert.assertEquals(expected, count);
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  /**
   * Tests whether two selectables can be created and queried for the same
   * attribute on a type.
   */
  @Request
  @Test
  public void testCharacterEqualStringDuplicateSelectable()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter", "attr1"), query.aCharacter("queryCharacter", "attr2"), query.oid("objectId"));
      vQ.WHERE(query.aCharacter("queryCharacter").EQ("some character value"));

      i = vQ.getIterator();

      int count = 0;

      while (i.hasNext())
      {
        ValueObject o = i.next();
        String value = o.getValue("attr1");

        Assert.assertEquals("some character value", value);

        value = o.getValue("attr2");

        Assert.assertEquals("some character value", value);

        count++;
      }

      int expected = 0;

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        if (testQueryObject.getValue("queryCharacter").equals("some character value"))
          expected++;
      }

      Assert.assertEquals(expected, count);
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
  public void testCharacterEqualString()
  {
    OIterator<ValueObject> i = null;

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter"), query.oid("objectId"));
      vQ.WHERE(query.aCharacter("queryCharacter").EQ("some character value"));

      i = vQ.getIterator();

      int count = 0;

      while (i.hasNext())
      {
        ValueObject o = i.next();
        String value = o.getValue("queryCharacter");

        Assert.assertEquals("some character value", value);

        count++;
      }

      int expected = 0;

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        if (testQueryObject.getValue("queryCharacter").equals("some character value"))
          expected++;
      }

      Assert.assertEquals(expected, count);

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aCharacter("queryCharacter"), query.oid("objectId"));
      vQ.WHERE(query.aCharacter("queryCharacter").EQ("wrong character value"));

      i = vQ.getIterator();

      count = 0;

      while (i.hasNext())
      {
        ValueObject o = i.next();
        String value = o.getValue("queryCharacter");

        Assert.assertEquals("wrong character value", value);

        count++;
      }

      expected = 0;

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        if (testQueryObject.getValue("queryCharacter").equals("wrong character value"))
          expected++;
      }

      Assert.assertEquals(expected, count);
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

      vQ.SELECT(query.aCharacter("queryCharacter"), query.oid("objectId"));
      vQ.WHERE(query.aCharacter("queryCharacter").EQi("SOME CHARACTER VALUE"));

      i = vQ.getIterator();

      int count = 0;

      while (i.hasNext())
      {
        ValueObject o = i.next();
        String value = o.getValue("queryCharacter");

        Assert.assertEquals("some character value", value);

        count++;
      }

      int expected = 0;

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        if (testQueryObject.getValue("queryCharacter").equals("some character value"))
          expected++;
      }

      Assert.assertEquals(expected, count);

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aCharacter("queryCharacter"), query.oid("objectId"));
      vQ.WHERE(query.aCharacter("queryCharacter").EQi("WRONG CHARACTER VALUE"));

      i = vQ.getIterator();

      Assert.assertFalse(i.hasNext());
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

      vQ.SELECT(query.aCharacter("queryCharacter"), query.oid("objectId"));
      vQ.WHERE(query.aCharacter("queryCharacter").IN("wrong value 1", "some character value", "wrong value 2"));

      i = vQ.getIterator();

      int count = 0;

      while (i.hasNext())
      {
        ValueObject o = i.next();
        String value = o.getValue("queryCharacter");

        Assert.assertEquals("some character value", value);

        count++;
      }

      int expected = 0;

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        if (testQueryObject.getValue("queryCharacter").equals("some character value"))
          expected++;
      }

      Assert.assertEquals(expected, count);

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aCharacter("queryCharacter"), query.oid("objectId"));
      vQ.WHERE(query.aCharacter("queryCharacter").IN("wrong value 1", "wrong value 2", "wrong value 3", "SOME CHARACTER VALUE"));

      i = vQ.getIterator();

      Assert.assertFalse(i.hasNext());
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

      vQ.SELECT(query.aCharacter("queryCharacter"), query.oid("objectId"));
      vQ.WHERE(query.aCharacter("queryCharacter").INi("wrong value 1", "SOME CHARACTER VALUE", "wrong value 2"));

      i = vQ.getIterator();

      int count = 0;

      while (i.hasNext())
      {
        ValueObject o = i.next();
        String value = o.getValue("queryCharacter");

        Assert.assertEquals("some character value", value);

        count++;
      }

      int expected = 0;

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        if (testQueryObject.getValue("queryCharacter").equals("some character value"))
          expected++;
      }

      Assert.assertEquals(expected, count);

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aCharacter("queryCharacter"), query.oid("objectId"));
      vQ.WHERE(query.aCharacter("queryCharacter").IN("wrong value 1", "wrong value 2", "wrong value 3"));

      i = vQ.getIterator();

      Assert.assertFalse(i.hasNext());
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

    String origValue = QueryMasterSetup.testQueryObject1.getValue("queryCharacter");
    QueryMasterSetup.testQueryObject1.setValue("queryCharacter", "");
    QueryMasterSetup.testQueryObject1.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter"), query.oid("objectId"));
      vQ.WHERE(query.aCharacter("queryCharacter").EQ(""));

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

      vQ.SELECT(query.aCharacter("queryCharacter"), query.oid("objectId"));
      vQ.WHERE(query.aCharacter("queryCharacter").NE(""));

      i = vQ.getIterator();

      int count = 0;

      while (i.hasNext())
      {
        i.next();

        count++;
      }

      int expected = 0;

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        if (!testQueryObject.getValue("queryCharacter").equals(""))
          expected++;
      }

      Assert.assertEquals(expected, count);
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.testQueryObject1.setValue("queryCharacter", origValue);
      QueryMasterSetup.testQueryObject1.apply();

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

      vQ.SELECT(query.aCharacter("queryCharacter"), query.oid("objectId"));
      vQ.WHERE(query.aCharacter("queryCharacter").LIKE("%character%"));

      i = vQ.getIterator();

      int count = 0;

      while (i.hasNext())
      {
        ValueObject o = i.next();
        String value = o.getValue("queryCharacter");

        Assert.assertEquals("some character value", value);

        count++;
      }

      int expected = 0;

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        if (testQueryObject.getValue("queryCharacter").equals("some character value"))
          expected++;
      }

      Assert.assertEquals(expected, count);

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aCharacter("queryCharacter"), query.oid("objectId"));
      vQ.WHERE(query.aCharacter("queryCharacter").LIKE("%character"));

      i = vQ.getIterator();

      Assert.assertFalse(i.hasNext());
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

      vQ.SELECT(query.aCharacter("queryCharacter"), query.oid("objectId"));
      vQ.WHERE(query.aCharacter("queryCharacter").LIKEi("%CHARACTER%"));

      i = vQ.getIterator();

      int count = 0;

      while (i.hasNext())
      {
        ValueObject o = i.next();
        String value = o.getValue("queryCharacter");

        Assert.assertEquals("some character value", value);

        count++;
      }

      int expected = 0;

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        if (testQueryObject.getValue("queryCharacter").equals("some character value"))
          expected++;
      }

      Assert.assertEquals(expected, count);

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aCharacter("queryCharacter"), query.oid("objectId"));
      vQ.WHERE(query.aCharacter("queryCharacter").LIKEi("%CHARACTER"));

      i = vQ.getIterator();

      Assert.assertFalse(i.hasNext());
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
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter"), query.oid("objectId"));
      vQ.WHERE(OR.get(query.aCharacter("queryCharacter").NE("wrong character value"), query.aCharacter("queryCharacter").EQ("")));

      i = vQ.getIterator();

      int count = 0;

      while (i.hasNext())
      {
        i.next();

        count++;
      }

      int expected = 0;

      List<String> objectList = MdBusinessDAO.getEntityIdsDB(QueryMasterSetup.childQueryInfo.getType());

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        String value = testQueryObject.getValue("queryCharacter");
        if (!value.equals("wrong character value"))
          expected++;
      }

      Assert.assertEquals(expected, count);
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
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter"), query.oid("objectId"));
      vQ.WHERE(OR.get(query.aCharacter("queryCharacter").NEi("WRONG CHARACTER VALUE"), query.aCharacter("queryCharacter").EQ("")));

      i = vQ.getIterator();

      int count = 0;

      while (i.hasNext())
      {
        i.next();

        count++;
      }

      int expected = 0;

      List<String> objectList = MdBusinessDAO.getEntityIdsDB(QueryMasterSetup.childQueryInfo.getType());

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        String value = testQueryObject.getValue("queryCharacter");
        if (!value.equals("wrong character value"))
          expected++;
      }

      Assert.assertEquals(expected, count);
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
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter"), query.oid("objectId"));
      vQ.WHERE(OR.get(query.aCharacter("queryCharacter").NI("wrong 1", "wrong 2", "wrong 3"), query.aCharacter("queryCharacter").EQ("")));

      i = vQ.getIterator();

      int count = 0;

      while (i.hasNext())
      {
        i.next();

        count++;
      }

      int expected = 0;

      List<String> objectList = MdBusinessDAO.getEntityIdsDB(QueryMasterSetup.childQueryInfo.getType());

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        String value = testQueryObject.getValue("queryCharacter");
        if (! ( value.equals("wrong 1") || value.equals("wrong 2") || value.equals("wrong 3") ))
          expected++;
      }

      Assert.assertEquals(expected, count);
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
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter"), query.oid("objectId"));
      vQ.WHERE(OR.get(query.aCharacter("queryCharacter").NIi("WRONG 1", "WRONG 2", "WRONG 3"), query.aCharacter("queryCharacter").EQ("")));

      i = vQ.getIterator();

      int count = 0;

      while (i.hasNext())
      {
        i.next();

        count++;
      }

      int expected = 0;

      List<String> objectList = MdBusinessDAO.getEntityIdsDB(QueryMasterSetup.childQueryInfo.getType());

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        String value = testQueryObject.getValue("queryCharacter");
        if (! ( value.equals("wrong 1") || value.equals("wrong 2") || value.equals("wrong 3") ))
          expected++;
      }

      Assert.assertEquals(expected, count);
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
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter"), query.oid("objectId"));
      vQ.WHERE(OR.get(query.aCharacter("queryCharacter").NLIKE("%character%"), query.aCharacter("queryCharacter").EQ("")));

      i = vQ.getIterator();

      int count = 0;

      while (i.hasNext())
      {
        i.next();

        count++;
      }

      int expected = 0;

      List<String> objectList = MdBusinessDAO.getEntityIdsDB(QueryMasterSetup.childQueryInfo.getType());

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        String value = testQueryObject.getValue("queryCharacter");
        if (!value.equals("some character value"))
          expected++;
      }

      Assert.assertEquals(expected, count);
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
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter"), query.oid("objectId"));
      vQ.WHERE(OR.get(query.aCharacter("queryCharacter").NLIKEi("%CHARACTER%"), query.aCharacter("queryCharacter").EQ("")));

      i = vQ.getIterator();

      int count = 0;

      while (i.hasNext())
      {
        i.next();

        count++;
      }

      int expected = 0;

      List<String> objectList = MdBusinessDAO.getEntityIdsDB(QueryMasterSetup.childQueryInfo.getType());

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        String value = testQueryObject.getValue("queryCharacter");
        if (!value.equals("some character value"))
          expected++;
      }

      Assert.assertEquals(expected, count);
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
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-06", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDate("queryDate"), query.oid("objectId"));
      vQ.WHERE(query.aDate("queryDate").EQ(date));

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
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-05", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aDate("queryDate"), query.oid("objectId"));
      vQ.WHERE(query.aDate("queryDate").EQ(date));

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

      vQ.SELECT(query.aDate("queryDate"), query.oid("objectId"));
      vQ.WHERE(query.aDate("queryDate").EQ("2006-12-06"));

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

      vQ.SELECT(query.aDate("queryDate"), query.oid("objectId"));
      vQ.WHERE(query.aDate("queryDate").EQ("2006-05-05"));

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
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-05", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDate("queryDate"), query.oid("objectId"));
      vQ.WHERE(query.aDate("queryDate").GT(date));

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
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-07", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aDate("queryDate"), query.oid("objectId"));
      vQ.WHERE(query.aDate("queryDate").GT(date));

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

      vQ.SELECT(query.aDate("queryDate"), query.oid("objectId"));
      vQ.WHERE(query.aDate("queryDate").GT("2006-12-05"));

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

      vQ.SELECT(query.aDate("queryDate"), query.oid("objectId"));
      vQ.WHERE(query.aDate("queryDate").GT("2006-12-07"));

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
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-06", new java.text.ParsePosition(0));

    try
    {
      // perform a query with a date equal to the stored
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDate("queryDate"), query.oid("objectId"));
      vQ.WHERE(query.aDate("queryDate").GE(date));

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
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-05", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aDate("queryDate"), query.oid("objectId"));
      vQ.WHERE(query.aDate("queryDate").GE(date));

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
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-07", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aDate("queryDate"), query.oid("objectId"));
      vQ.WHERE(query.aDate("queryDate").GE(date));

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

      vQ.SELECT(query.aDate("queryDate"), query.oid("objectId"));
      vQ.WHERE(query.aDate("queryDate").GE("2006-12-06"));

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

      vQ.SELECT(query.aDate("queryDate"), query.oid("objectId"));
      vQ.WHERE(query.aDate("queryDate").GE("2006-12-05"));

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

      vQ.SELECT(query.aDate("queryDate"), query.oid("objectId"));
      vQ.WHERE(query.aDate("queryDate").GE("2006-12-07"));

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

    String origValue = QueryMasterSetup.testQueryObject1.getValue("queryDate");
    QueryMasterSetup.testQueryObject1.setValue("queryDate", "");
    QueryMasterSetup.testQueryObject1.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDate("queryDate"), query.oid("objectId"));
      vQ.WHERE(query.aDate("queryDate").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date values did not return any results when it should have");
      }

      int count = 0;

      while (i.hasNext())
      {
        Assert.assertTrue(i.next().getValue("queryDate").equals(""));

        count++;
      }

      int expected = 0;

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        if (testQueryObject.getValue("queryDate").equals(""))
          expected++;
      }

      Assert.assertEquals(expected, count);

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDate("queryDate"), query.oid("objectId"));
      vQ.WHERE(query.aDate("queryDate").NE(""));

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
      QueryMasterSetup.testQueryObject1.setValue("queryDate", origValue);
      QueryMasterSetup.testQueryObject1.apply();

      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDateLTDate()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-07", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDate("queryDate"), query.oid("objectId"));
      vQ.WHERE(query.aDate("queryDate").LT(date));

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
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-05", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aDate("queryDate"), query.oid("objectId"));
      vQ.WHERE(query.aDate("queryDate").LT(date));

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

      vQ.SELECT(query.aDate("queryDate"), query.oid("objectId"));
      vQ.WHERE(query.aDate("queryDate").LT("2006-12-07"));

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

      vQ.SELECT(query.aDate("queryDate"), query.oid("objectId"));
      vQ.WHERE(query.aDate("queryDate").LT("2006-12-05"));

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
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-06", new java.text.ParsePosition(0));

    try
    {
      // perform a query with a date equal to the stored
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDate("queryDate"), query.oid("objectId"));
      vQ.WHERE(query.aDate("queryDate").LE(date));

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
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-07", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aDate("queryDate"), query.oid("objectId"));
      vQ.WHERE(query.aDate("queryDate").LE(date));

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
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-05", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aDate("queryDate"), query.oid("objectId"));
      vQ.WHERE(query.aDate("queryDate").LE(date));

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

      vQ.SELECT(query.aDate("queryDate"), query.oid("objectId"));
      vQ.WHERE(query.aDate("queryDate").LE("2006-12-06"));

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

      vQ.SELECT(query.aDate("queryDate"), query.oid("objectId"));
      vQ.WHERE(query.aDate("queryDate").LE("2006-12-07"));

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

      vQ.SELECT(query.aDate("queryDate"), query.oid("objectId"));
      vQ.WHERE(query.aDate("queryDate").LE("2006-12-05"));

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
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-05", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDate("queryDate"), query.oid("objectId"));
      vQ.WHERE(query.aDate("queryDate").NE(date));

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
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-06", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aDate("queryDate"), query.oid("objectId"));
      vQ.WHERE(query.aDate("queryDate").NE(date));

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

      vQ.SELECT(query.aDate("queryDate"), query.oid("objectId"));
      vQ.WHERE(query.aDate("queryDate").NE("2006-12-05"));

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

      vQ.SELECT(query.aDate("queryDate"), query.oid("objectId"));
      vQ.WHERE(query.aDate("queryDate").NE("2006-12-06"));

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
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-06 13:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDateTime("queryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").EQ(date));

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
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-06 12:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aDateTime("queryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").EQ(date));

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

      vQ.SELECT(query.aDateTime("queryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").EQ("2006-12-06 13:00:00"));

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

      vQ.SELECT(query.aDateTime("queryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").EQ("2006-12-06 12:00:00"));

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
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-06 12:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDateTime("queryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").GT(date));

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
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-06 13:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aDateTime("queryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").GT(date));

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

      vQ.SELECT(query.aDateTime("queryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").GT("2006-12-06 12:00:00"));

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

      vQ.SELECT(query.aDateTime("queryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").GT("2006-12-06 13:00:00"));

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
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-06 13:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDateTime("queryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").GE(date));

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
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-05 13:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aDateTime("queryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").GE(date));

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
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-07 13:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aDateTime("queryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").GE(date));

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

      vQ.SELECT(query.aDateTime("queryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").GE("2006-12-06 13:00:00"));

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

      vQ.SELECT(query.aDateTime("queryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").GE("2006-12-05 13:00:00"));

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

      vQ.SELECT(query.aDateTime("queryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").GE("2006-12-07 13:00:00"));

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

    String origValue = QueryMasterSetup.testQueryObject1.getValue("queryDateTime");
    QueryMasterSetup.testQueryObject1.setValue("queryDateTime", "");
    QueryMasterSetup.testQueryObject1.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDateTime("queryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute date time values did not return any results when it should have");
      }

      int count = 0;

      while (i.hasNext())
      {
        Assert.assertTrue(i.next().getValue("queryDateTime").equals(""));

        count++;
      }

      int expected = 0;

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        if (testQueryObject.getValue("queryDateTime").equals(""))
          expected++;
      }

      Assert.assertEquals(expected, count);

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDateTime("queryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").NE(""));

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
      QueryMasterSetup.testQueryObject1.setValue("queryDateTime", origValue);
      QueryMasterSetup.testQueryObject1.apply();

      if (i != null)
        i.close();
    }
  }

  @Request
  @Test
  public void testDateTimeLTDateTime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-07 12:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDateTime("queryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").LT(date));

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
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-05 13:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aDateTime("queryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").LT(date));

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

      vQ.SELECT(query.aDateTime("queryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").LT("2006-12-07 12:00:00"));

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

      vQ.SELECT(query.aDateTime("queryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").LT("2006-12-05 13:00:00"));

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
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-06 13:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDateTime("queryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").LE(date));

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
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-07 13:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aDateTime("queryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").LE(date));

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
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-05 13:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aDateTime("queryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").LE(date));

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

      vQ.SELECT(query.aDateTime("queryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").LE("2006-12-06 13:00:00"));

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

      vQ.SELECT(query.aDateTime("queryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").LE("2006-12-07 13:00:00"));

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

      vQ.SELECT(query.aDateTime("queryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").LE("2006-12-05 13:00:00"));

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
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-05 13:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDateTime("queryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").NE(date));

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
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-06 13:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aDateTime("queryDateTime"), query.oid("objectId"));
      vQ.WHERE(AND.get(query.aDateTime("queryDateTime").NE(date), query.aDateTime("queryDateTime").NE("")));

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

      vQ.SELECT(query.aDateTime("queryDateTime"), query.oid("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").NE("2006-12-05 13:00:00"));

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

      vQ.SELECT(query.aDateTime("queryDateTime"), query.oid("objectId"));
      vQ.WHERE(AND.get(query.aDateTime("queryDateTime").NE("2006-12-06 13:00:00"), query.aDateTime("queryDateTime").NE("")));

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

      vQ.SELECT(query.aDecimal("queryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").EQ(new BigDecimal(100.5)));

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

      vQ.SELECT(query.aDecimal("queryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").EQ(new BigDecimal(101.5)));

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

      vQ.SELECT(query.aDecimal("queryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").EQ("100.5"));

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

      vQ.SELECT(query.aDecimal("queryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").EQ("101.5"));

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

      vQ.SELECT(query.aDecimal("queryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").GT(new BigDecimal(100)));

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

      vQ.SELECT(query.aDecimal("queryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").GT(new BigDecimal(101)));

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

      vQ.SELECT(query.aDecimal("queryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").GT("100"));

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

      vQ.SELECT(query.aDecimal("queryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").GT("101"));

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

      vQ.SELECT(query.aDecimal("queryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").GE(new BigDecimal(100.5)));

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

      vQ.SELECT(query.aDecimal("queryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").GE(new BigDecimal(100)));

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

      vQ.SELECT(query.aDecimal("queryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").GE(new BigDecimal(101)));

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

      vQ.SELECT(query.aDecimal("queryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").GE("100.5"));

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

      vQ.SELECT(query.aDecimal("queryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").GE("100"));

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

      vQ.SELECT(query.aDecimal("queryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").GE("101"));

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

    String origValue = QueryMasterSetup.testQueryObject1.getValue("queryDecimal");
    QueryMasterSetup.testQueryObject1.setValue("queryDecimal", "");
    QueryMasterSetup.testQueryObject1.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDecimal("queryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute decimal values did not return any results when it should have");
      }

      int count = 0;

      while (i.hasNext())
      {
        Assert.assertTrue(i.next().getValue("queryDecimal").equals(""));

        count++;
      }

      int expected = 0;

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        if (testQueryObject.getValue("queryDecimal").equals(""))
          expected++;
      }

      Assert.assertEquals(expected, count);

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDecimal("queryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").NE(""));

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
      QueryMasterSetup.testQueryObject1.setValue("queryDecimal", origValue);
      QueryMasterSetup.testQueryObject1.apply();

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

      vQ.SELECT(query.aDecimal("queryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").LT(new BigDecimal(101)));

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

      vQ.SELECT(query.aDecimal("queryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").LT(new BigDecimal(100)));

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

      vQ.SELECT(query.aDecimal("queryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").LT("101"));

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

      vQ.SELECT(query.aDecimal("queryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").LT("100"));

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

      vQ.SELECT(query.aDecimal("queryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").LE(new BigDecimal(100.5)));

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

      vQ.SELECT(query.aDecimal("queryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").LE(new BigDecimal(101)));

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

      vQ.SELECT(query.aDecimal("queryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").LE(new BigDecimal(99)));

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

      vQ.SELECT(query.aDecimal("queryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").LE("100.5"));

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

      vQ.SELECT(query.aDecimal("queryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").LE("101"));

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

      vQ.SELECT(query.aDecimal("queryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").LE("99"));

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

      vQ.SELECT(query.aDecimal("queryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").NE(new BigDecimal(101)));

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

      vQ.SELECT(query.aDecimal("queryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").NE(new BigDecimal(100.5)));

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

      vQ.SELECT(query.aDecimal("queryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").NE("101"));

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

      vQ.SELECT(query.aDecimal("queryDecimal"), query.oid("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").NE("100.5"));

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

      vQ.SELECT(query.aDouble("queryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").EQ(100.5));

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

      vQ.SELECT(query.aDouble("queryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").EQ(101.5));

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

      vQ.SELECT(query.aDouble("queryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").EQ("100.5"));

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

      vQ.SELECT(query.aDouble("queryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").EQ("101.5"));

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

      vQ.SELECT(query.aDouble("queryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").GT(10.5));

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

      vQ.SELECT(query.aDouble("queryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").GT(120.5));

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

      vQ.SELECT(query.aDouble("queryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").GT("10.5"));

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

      vQ.SELECT(query.aDouble("queryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").GT("120.5"));

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

      vQ.SELECT(query.aDouble("queryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").GE(100.5));

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

      vQ.SELECT(query.aDouble("queryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").GE(10.5));

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

      vQ.SELECT(query.aDouble("queryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").GE(120.5));

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

      vQ.SELECT(query.aDouble("queryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").GE("100.5"));

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

      vQ.SELECT(query.aDouble("queryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").GE("10.5"));

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

      vQ.SELECT(query.aDouble("queryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").GE("120.5"));

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

    String origValue = QueryMasterSetup.testQueryObject1.getValue("queryDouble");
    QueryMasterSetup.testQueryObject1.setValue("queryDouble", "");
    QueryMasterSetup.testQueryObject1.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDouble("queryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute double values did not return any results when it should have");
      }

      int count = 0;

      while (i.hasNext())
      {
        Assert.assertTrue(i.next().getValue("queryDouble").equals(""));

        count++;
      }

      int expected = 0;

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        if (testQueryObject.getValue("queryDouble").equals(""))
          expected++;
      }

      Assert.assertEquals(expected, count);

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDouble("queryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").NE(""));

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
      QueryMasterSetup.testQueryObject1.setValue("queryDouble", origValue);
      QueryMasterSetup.testQueryObject1.apply();

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

      vQ.SELECT(query.aDouble("queryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").LT(120.5));

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

      vQ.SELECT(query.aDouble("queryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").LT(10.5));

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

      vQ.SELECT(query.aDouble("queryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").LT("120.5"));

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

      vQ.SELECT(query.aDouble("queryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").LT("10.5"));

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

      vQ.SELECT(query.aDouble("queryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").LE(100.5));

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

      vQ.SELECT(query.aDouble("queryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").LE(120.5));

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

      vQ.SELECT(query.aDouble("queryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").LE(10.5));

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

      vQ.SELECT(query.aDouble("queryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").LE("100.5"));

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

      vQ.SELECT(query.aDouble("queryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").LE("120.5"));

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

      vQ.SELECT(query.aDouble("queryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").LE("10.5"));

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

      vQ.SELECT(query.aDouble("queryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").NE(101.5));

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

      vQ.SELECT(query.aDouble("queryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").NE(100.5));

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

      vQ.SELECT(query.aDouble("queryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").NE("101.5"));

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

      vQ.SELECT(query.aDouble("queryDouble"), query.oid("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").NE("100.5"));

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

      vQ.SELECT(query.aFloat("queryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").EQ((float) 100.5));

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

      vQ.SELECT(query.aFloat("queryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").EQ((float) 101.5));

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

      vQ.SELECT(query.aFloat("queryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").EQ("100.5"));

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

      vQ.SELECT(query.aFloat("queryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").EQ("101.5"));

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

      vQ.SELECT(query.aFloat("queryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").GT((float) 10.5));

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

      vQ.SELECT(query.aFloat("queryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").GT((float) 121.5));

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

      vQ.SELECT(query.aFloat("queryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").GT("10.5"));

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

      vQ.SELECT(query.aFloat("queryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").GT("121.5"));

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

      vQ.SELECT(query.aFloat("queryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").GE((float) 100.5));

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

      vQ.SELECT(query.aFloat("queryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").GE((float) 11.5));

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

      vQ.SELECT(query.aFloat("queryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").GE((float) 121.5));

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

      vQ.SELECT(query.aFloat("queryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").GE("100.5"));

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

      vQ.SELECT(query.aFloat("queryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").GE("11.5"));

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

      vQ.SELECT(query.aFloat("queryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").GE("121.5"));

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

    String origValue = QueryMasterSetup.testQueryObject1.getValue("queryFloat");
    QueryMasterSetup.testQueryObject1.setValue("queryFloat", "");
    QueryMasterSetup.testQueryObject1.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aFloat("queryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute float values did not return any results when it should have");
      }

      int count = 0;

      while (i.hasNext())
      {
        Assert.assertTrue(i.next().getValue("queryFloat").equals(""));

        count++;
      }

      int expected = 0;

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        if (testQueryObject.getValue("queryFloat").equals(""))
          expected++;
      }

      Assert.assertEquals(expected, count);

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aFloat("queryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").NE(""));

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
      QueryMasterSetup.testQueryObject1.setValue("queryFloat", origValue);
      QueryMasterSetup.testQueryObject1.apply();

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

      vQ.SELECT(query.aFloat("queryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").LT((float) 120.5));

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

      vQ.SELECT(query.aFloat("queryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").LT((float) 11.5));

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

      vQ.SELECT(query.aFloat("queryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").LT("120.5"));

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

      vQ.SELECT(query.aFloat("queryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").LT("11.5"));

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

      vQ.SELECT(query.aFloat("queryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").LE((float) 100.5));

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

      vQ.SELECT(query.aFloat("queryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").LE((float) 101.5));

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

      vQ.SELECT(query.aFloat("queryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").LE((float) 11.5));

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

      vQ.SELECT(query.aFloat("queryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").LE("100.5"));

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

      vQ.SELECT(query.aFloat("queryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").LE("101.5"));

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

      vQ.SELECT(query.aFloat("queryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").LE("11.5"));

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

      vQ.SELECT(query.aFloat("queryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").NE((float) 110.5));

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

      vQ.SELECT(query.aFloat("queryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").NE((float) 100.5));

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

      vQ.SELECT(query.aFloat("queryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").NE("110.5"));

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

      vQ.SELECT(query.aFloat("queryFloat"), query.oid("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").NE("100.5"));

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

      vQ.SELECT(query.aInteger("queryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").EQ(100));

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

      vQ.SELECT(query.aInteger("queryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").EQ(101));

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

      vQ.SELECT(query.aInteger("queryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").EQ("100"));

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

      vQ.SELECT(query.aInteger("queryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").EQ("101"));

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

      vQ.SELECT(query.aInteger("queryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").GT(10));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (Integer.parseInt(o.getValue("queryInteger")) <= 10)
        {
          Assert.fail("One of the objects returned by the query had an incorrect integer attribute.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aInteger("queryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").GT(101));

      i = vQ.getIterator();

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (Integer.parseInt(o.getValue("queryInteger")) <= 101)
        {
          Assert.fail("One of the objects returned by the query had an incorrect integer attribute.");
        }
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

      vQ.SELECT(query.aInteger("queryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").GT("10"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (Integer.parseInt(o.getValue("queryInteger")) <= 10)
        {
          Assert.fail("One of the objects returned by the query had an incorrect integer attribute.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aInteger("queryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").GT("101"));

      i = vQ.getIterator();

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (Integer.parseInt(o.getValue("queryInteger")) <= 101)
        {
          Assert.fail("One of the objects returned by the query had an incorrect integer attribute.");
        }
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

      vQ.SELECT(query.aInteger("queryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").GE(100));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (Integer.parseInt(o.getValue("queryInteger")) < 100)
        {
          Assert.fail("One of the objects returned by the query had an incorrect integer attribute.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aInteger("queryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").GE(10));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (Integer.parseInt(o.getValue("queryInteger")) < 10)
        {
          Assert.fail("One of the objects returned by the query had an incorrect integer attribute.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aInteger("queryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").GE(101));

      i = vQ.getIterator();

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (Integer.parseInt(o.getValue("queryInteger")) < 101)
        {
          Assert.fail("One of the objects returned by the query had an incorrect integer attribute.");
        }
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

      vQ.SELECT(query.aInteger("queryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").GE("100"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (Integer.parseInt(o.getValue("queryInteger")) < 100)
        {
          Assert.fail("One of the objects returned by the query had an incorrect integer attribute.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aInteger("queryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").GE("10"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (Integer.parseInt(i.next().getValue("queryInteger")) < 10)
        {
          Assert.fail("One of the objects returned by the query had an incorrect integer attribute.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aInteger("queryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").GE("101"));

      i = vQ.getIterator();

      while (i.hasNext())
      {
        if (Integer.parseInt(i.next().getValue("queryInteger")) < 101)
        {
          Assert.fail("One of the objects returned by the query had an incorrect integer attribute.");
        }
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

    String origValue = QueryMasterSetup.testQueryObject1.getValue("queryInteger");
    QueryMasterSetup.testQueryObject1.setValue("queryInteger", "");
    QueryMasterSetup.testQueryObject1.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aInteger("queryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute integer values did not return any results when it should have");
      }

      int count = 0;

      while (i.hasNext())
      {
        Assert.assertTrue(i.next().getValue("queryInteger").equals(""));

        count++;
      }

      int expected = 0;

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        if (testQueryObject.getValue("queryInteger").equals(""))
          expected++;
      }

      Assert.assertEquals(expected, count);

      // perform another query
      vQ = qf.valueQuery();

      vQ.SELECT(query.aInteger("queryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").NE(""));

      i = vQ.getIterator();

      count = 0;

      while (i.hasNext())
      {
        Assert.assertTrue(!i.next().getValue("queryInteger").equals(""));

        count++;
      }

      expected = 0;

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        if (!testQueryObject.getValue("queryInteger").equals(""))
          expected++;
      }

      Assert.assertEquals(expected, count);
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.testQueryObject1.setValue("queryInteger", origValue);
      QueryMasterSetup.testQueryObject1.apply();

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

      vQ.SELECT(query.aInteger("queryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").LT(101));

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

      vQ.SELECT(query.aInteger("queryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").LT(10));

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

      vQ.SELECT(query.aInteger("queryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").LT("101"));

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

      vQ.SELECT(query.aInteger("queryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").LT("10"));

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

      vQ.SELECT(query.aInteger("queryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").LE(100));

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

      vQ.SELECT(query.aInteger("queryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").LE(101));

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

      vQ.SELECT(query.aInteger("queryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").LE(10));

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

      vQ.SELECT(query.aInteger("queryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").LE("100"));

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

      vQ.SELECT(query.aInteger("queryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").LE("101"));

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

      vQ.SELECT(query.aInteger("queryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").LE("10"));

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

      vQ.SELECT(query.aInteger("queryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").NE(101));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (Integer.parseInt(i.next().getValue("queryInteger")) == 101)
        {
          Assert.fail("One of the objects returned by the query had an incorrect integer attribute.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aInteger("queryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").NE(100));

      i = vQ.getIterator();

      while (i.hasNext())
      {
        if (Integer.parseInt(i.next().getValue("queryInteger")) == 100)
        {
          Assert.fail("One of the objects returned by the query had an incorrect integer attribute.");
        }
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

      vQ.SELECT(query.aInteger("queryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").NE("101"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (Integer.parseInt(i.next().getValue("queryInteger")) == 101)
        {
          Assert.fail("One of the objects returned by the query returned an object with incorrect integer attributes.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aInteger("queryInteger"), query.oid("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").NE("100"));

      i = vQ.getIterator();

      while (i.hasNext())
      {
        if (Integer.parseInt(i.next().getValue("queryInteger")) == 100)
        {
          Assert.fail("One of the objects returned by the query had an incorrect integer attribute.");
        }
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
  public void testReplaceSelectable()
  {
    OIterator<ValueObject> i = null;

    try
    {
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();

      SelectableSQL original = vQ.aSQLInteger("anInt", "one");
      vQ.SELECT(original);
      vQ.FROM("(SELECT 1 as one, 2 as two)", "numberSet");
      Selectable replaced = vQ.replaceSelectable(vQ.aSQLInteger("anInt", "two"));

      Assert.assertEquals(replaced.getSQL(), original.getSQL());
      Assert.assertEquals(vQ.getSelectableRefs().size(), 1);
      Assert.assertEquals(vQ.getIterator().next().getValue("anInt"), "2");
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

      vQ.SELECT(query.aLong("queryLong"), query.oid("objectId"));
      vQ.WHERE(query.aLong("queryLong").EQ((long) 100));

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

      vQ.SELECT(query.aLong("queryLong"), query.oid("objectId"));
      vQ.WHERE(query.aLong("queryLong").EQ((long) 101));

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

      vQ.SELECT(query.aLong("queryLong"), query.oid("objectId"));
      vQ.WHERE(query.aLong("queryLong").EQ("100"));

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

      vQ.SELECT(query.aLong("queryLong"), query.oid("objectId"));
      vQ.WHERE(query.aLong("queryLong").EQ("101"));

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

      vQ.SELECT(query.aLong("queryLong"), query.oid("objectId"));
      vQ.WHERE(query.aLong("queryLong").GT((long) 10));

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

      vQ.SELECT(query.aLong("queryLong"), query.oid("objectId"));
      vQ.WHERE(query.aLong("queryLong").GT((long) 101));

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

      vQ.SELECT(query.aLong("queryLong"), query.oid("objectId"));
      vQ.WHERE(query.aLong("queryLong").GT("10"));

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

      vQ.SELECT(query.aLong("queryLong"), query.oid("objectId"));
      vQ.WHERE(query.aLong("queryLong").GT("101"));

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

      vQ.SELECT(query.aLong("queryLong"), query.oid("objectId"));
      vQ.WHERE(query.aLong("queryLong").GE((long) 100));

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

      vQ.SELECT(query.aLong("queryLong"), query.oid("objectId"));
      vQ.WHERE(query.aLong("queryLong").GE((long) 10));

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

      vQ.SELECT(query.aLong("queryLong"), query.oid("objectId"));
      vQ.WHERE(query.aLong("queryLong").GE((long) 101));

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

      vQ.SELECT(query.aLong("queryLong"), query.oid("objectId"));
      vQ.WHERE(query.aLong("queryLong").GE("100"));

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

      vQ.SELECT(query.aLong("queryLong"), query.oid("objectId"));
      vQ.WHERE(query.aLong("queryLong").GE("10"));

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

      vQ.SELECT(query.aLong("queryLong"), query.oid("objectId"));
      vQ.WHERE(query.aLong("queryLong").GE("101"));

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

    String origValue = QueryMasterSetup.testQueryObject1.getValue("queryLong");
    QueryMasterSetup.testQueryObject1.setValue("queryLong", "");
    QueryMasterSetup.testQueryObject1.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aLong("queryLong"), query.oid("objectId"));
      vQ.WHERE(query.aLong("queryLong").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute long values did not return any results when it should have");
      }

      int count = 0;

      while (i.hasNext())
      {
        Assert.assertTrue(i.next().getValue("queryLong").equals(""));

        count++;
      }

      int expected = 0;

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        if (testQueryObject.getValue("queryLong").equals(""))
          expected++;
      }

      Assert.assertEquals(expected, count);

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aLong("queryLong"), query.oid("objectId"));
      vQ.WHERE(query.aLong("queryLong").NE(""));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        QueryMasterSetup.testQueryObject1.setValue("queryLong", origValue);
        QueryMasterSetup.testQueryObject1.apply();

        Assert.fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.testQueryObject1.setValue("queryLong", origValue);
      QueryMasterSetup.testQueryObject1.apply();

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

      vQ.SELECT(query.aLong("queryLong"), query.oid("objectId"));
      vQ.WHERE(query.aLong("queryLong").LT((long) 120));

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

      vQ.SELECT(query.aLong("queryLong"), query.oid("objectId"));
      vQ.WHERE(query.aLong("queryLong").LT((long) 10));

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

      vQ.SELECT(query.aLong("queryLong"), query.oid("objectId"));
      vQ.WHERE(query.aLong("queryLong").LT("120"));

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

      vQ.SELECT(query.aLong("queryLong"), query.oid("objectId"));
      vQ.WHERE(query.aLong("queryLong").LT("10"));

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

      vQ.SELECT(query.aLong("queryLong"), query.oid("objectId"));
      vQ.WHERE(query.aLong("queryLong").LE((long) 100));

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

      vQ.SELECT(query.aLong("queryLong"), query.oid("objectId"));
      vQ.WHERE(query.aLong("queryLong").LE((long) 120));

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

      vQ.SELECT(query.aLong("queryLong"), query.oid("objectId"));
      vQ.WHERE(query.aLong("queryLong").LE((long) 10));

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

      vQ.SELECT(query.aLong("queryLong"), query.oid("objectId"));
      vQ.WHERE(query.aLong("queryLong").LE("100"));

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

      vQ.SELECT(query.aLong("queryLong"), query.oid("objectId"));
      vQ.WHERE(query.aLong("queryLong").LE("120"));

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

      vQ.SELECT(query.aLong("queryLong"), query.oid("objectId"));
      vQ.WHERE(query.aLong("queryLong").LE("10"));

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

      vQ.SELECT(query.aLong("queryLong"), query.oid("objectId"));
      vQ.WHERE(query.aLong("queryLong").NE((long) 10));

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

      vQ.SELECT(query.aLong("queryLong"), query.oid("objectId"));
      vQ.WHERE(query.aLong("queryLong").NE((long) 100));

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

      vQ.SELECT(query.aLong("queryLong"), query.oid("objectId"));
      vQ.WHERE(query.aLong("queryLong").NE("10"));

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

      vQ.SELECT(query.aLong("queryLong"), query.oid("objectId"));
      vQ.WHERE(query.aLong("queryLong").NE("100"));

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

      vQ.SELECT(query.aText("queryText"), query.oid("objectId"));
      vQ.WHERE(query.aText("queryText").EQ("some text value"));

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

      vQ.SELECT(query.aText("queryText"), query.oid("objectId"));
      vQ.WHERE(query.aText("queryText").EQ("wrong text value"));

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

      vQ.SELECT(query.aText("queryText"), query.oid("objectId"));
      vQ.WHERE(query.aText("queryText").EQi("SOME TEXT VALUE"));

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

      vQ.SELECT(query.aText("queryText"), query.oid("objectId"));
      vQ.WHERE(query.aText("queryText").EQi("WRONG TEXT VALUE"));

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

      vQ.SELECT(query.aText("queryText"), query.oid("objectId"));
      vQ.WHERE(query.aText("queryText").IN("SOME text value", "some text value", "wrong text value"));

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

      vQ.SELECT(query.aText("queryText"), query.oid("objectId"));
      vQ.WHERE(query.aText("queryText").IN("SOME text value", "SOME TEXT value", "SOME TEXT VALUE"));

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

      vQ.SELECT(query.aText("queryText"), query.oid("objectId"));
      vQ.WHERE(query.aText("queryText").INi("WRONG TEXT VALUE", "SOME TEXT VALUE", "wrong TEXT value 2"));

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

      vQ.SELECT(query.aText("queryText"), query.oid("objectId"));
      vQ.WHERE(query.aText("queryText").INi("WRONG TEXT VALUE", "WRONG TEXT VALUE 2", "WRONG TEXT VALUE 3"));

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

    String origValue = QueryMasterSetup.testQueryObject1.getValue("queryText");
    QueryMasterSetup.testQueryObject1.setValue("queryText", "");
    QueryMasterSetup.testQueryObject1.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aText("queryText"), query.oid("objectId"));
      vQ.WHERE(query.aText("queryText").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute text values did not return any results when it should have");
      }

      int count = 0;

      while (i.hasNext())
      {
        Assert.assertTrue(i.next().getValue("queryText").equals(""));

        count++;
      }

      int expected = 0;

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        if (testQueryObject.getValue("queryText").equals(""))
          expected++;
      }

      Assert.assertEquals(expected, count);

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aText("queryText"), query.oid("objectId"));
      vQ.WHERE(query.aText("queryText").NE(""));

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
      QueryMasterSetup.testQueryObject1.setValue("queryText", origValue);
      QueryMasterSetup.testQueryObject1.apply();

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

      vQ.SELECT(query.aText("queryText"), query.oid("objectId"));
      vQ.WHERE(query.aText("queryText").LIKE("%text%"));

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

      vQ.SELECT(query.aText("queryText"), query.oid("objectId"));
      vQ.WHERE(query.aText("queryText").LIKE("%text"));

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

      vQ.SELECT(query.aText("queryText"), query.oid("objectId"));
      vQ.WHERE(query.aText("queryText").LIKEi("%TEXT%"));

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

      vQ.SELECT(query.aText("queryText"), query.oid("objectId"));
      vQ.WHERE(query.aText("queryText").LIKEi("%TEXT"));

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

      vQ.SELECT(query.aText("queryText"), query.oid("objectId"));
      vQ.WHERE(query.aText("queryText").NE("wrong text value"));

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

      vQ.SELECT(query.aText("queryText"), query.oid("objectId"));
      vQ.WHERE(query.aText("queryText").NE("some text value"));

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

      vQ.SELECT(query.aText("queryText"), query.oid("objectId"));
      vQ.WHERE(query.aText("queryText").NEi("WRONG TEXT VALUE"));

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

      vQ.SELECT(query.aText("queryText"), query.oid("objectId"));
      vQ.WHERE(query.aText("queryText").NEi("SOME TEXT VALUE"));

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

      vQ.SELECT(query.aText("queryText"), query.oid("objectId"));
      vQ.WHERE(query.aText("queryText").NI("SOME text value", "SOME TEXT value", "SOME TEXT VALUE"));

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

      vQ.SELECT(query.aText("queryText"), query.oid("objectId"));
      vQ.WHERE(query.aText("queryText").NI("SOME text value", "some text value", "wrong text value"));

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

      vQ.SELECT(query.aText("queryText"), query.oid("objectId"));
      vQ.WHERE(query.aText("queryText").NIi("WRONG text value", "WRONG TEXT value 2", "WRONG TEXT VALUE 3"));

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

      vQ.SELECT(query.aText("queryText"), query.oid("objectId"));
      vQ.WHERE(query.aText("queryText").NIi("SOME text value", "SOME TEXT VALUE", "SOME TEXT value"));

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

      vQ.SELECT(query.aText("queryText"), query.oid("objectId"));
      vQ.WHERE(query.aText("queryText").NLIKEi("%WRONG%"));

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

      vQ.SELECT(query.aText("queryText"), query.oid("objectId"));
      vQ.WHERE(query.aText("queryText").NLIKEi("%TEXT%"));

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
    Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aTime("queryTime"), query.oid("objectId"));
      vQ.WHERE(query.aTime("queryTime").EQ(date));

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

      vQ.SELECT(query.aTime("queryTime"), query.oid("objectId"));
      vQ.WHERE(query.aTime("queryTime").EQ(date));

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

      vQ.SELECT(query.aTime("queryTime"), query.oid("objectId"));
      vQ.WHERE(query.aTime("queryTime").EQ("13:00:00"));

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

      vQ.SELECT(query.aTime("queryTime"), query.oid("objectId"));
      vQ.WHERE(query.aTime("queryTime").EQ("12:00:00"));

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
    Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aTime("queryTime"), query.oid("objectId"));
      vQ.WHERE(query.aTime("queryTime").GT(date));

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
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aTime("queryTime"), query.oid("objectId"));
      vQ.WHERE(query.aTime("queryTime").GT(date));

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

      vQ.SELECT(query.aTime("queryTime"), query.oid("objectId"));
      vQ.WHERE(query.aTime("queryTime").GT("12:00:00"));

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

      vQ.SELECT(query.aTime("queryTime"), query.oid("objectId"));
      vQ.WHERE(query.aTime("queryTime").GT("14:00:00"));

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
    Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aTime("queryTime"), query.oid("objectId"));
      vQ.WHERE(query.aTime("queryTime").GE(date));

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

      vQ.SELECT(query.aTime("queryTime"), query.oid("objectId"));
      vQ.WHERE(query.aTime("queryTime").GE(date));

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

      vQ.SELECT(query.aTime("queryTime"), query.oid("objectId"));
      vQ.WHERE(query.aTime("queryTime").GE(date));

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

      vQ.SELECT(query.aTime("queryTime"), query.oid("objectId"));
      vQ.WHERE(query.aTime("queryTime").GE("13:00:00"));

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

      vQ.SELECT(query.aTime("queryTime"), query.oid("objectId"));
      vQ.WHERE(query.aTime("queryTime").GE("12:00:00"));

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

      vQ.SELECT(query.aTime("queryTime"), query.oid("objectId"));
      vQ.WHERE(query.aTime("queryTime").GE("14:00:00"));

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
    String origValue = QueryMasterSetup.testQueryObject1.getValue("queryTime");
    QueryMasterSetup.testQueryObject1.setValue("queryTime", "");
    QueryMasterSetup.testQueryObject1.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aTime("queryTime"), query.oid("objectId"));
      vQ.WHERE(query.aTime("queryTime").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query based on attribute time values did not return any results when it should have");
      }

      int count = 0;

      while (i.hasNext())
      {
        Assert.assertTrue(i.next().getValue("queryTime").equals(""));

        count++;
      }

      int expected = 0;

      for (String oid : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(oid).getBusinessDAO();

        if (testQueryObject.getValue("queryTime").equals(""))
          expected++;
      }

      Assert.assertEquals(expected, count);

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aTime("queryTime"), query.oid("objectId"));
      vQ.WHERE(query.aTime("queryTime").NE(""));

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
      QueryMasterSetup.testQueryObject1.setValue("queryTime", origValue);
      QueryMasterSetup.testQueryObject1.apply();

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

      vQ.SELECT(query.aTime("queryTime"), query.oid("objectId"));
      vQ.WHERE(query.aTime("queryTime").LT(date));

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

      vQ.SELECT(query.aTime("queryTime"), query.oid("objectId"));
      vQ.WHERE(query.aTime("queryTime").LT(date));

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

      vQ.SELECT(query.aTime("queryTime"), query.oid("objectId"));
      vQ.WHERE(query.aTime("queryTime").LT("14:00:00"));

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

      vQ.SELECT(query.aTime("queryTime"), query.oid("objectId"));
      vQ.WHERE(query.aTime("queryTime").LT("10:00:00"));

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
    Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aTime("queryTime"), query.oid("objectId"));
      vQ.WHERE(query.aTime("queryTime").LE(date));

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

      vQ.SELECT(query.aTime("queryTime"), query.oid("objectId"));
      vQ.WHERE(query.aTime("queryTime").LE(date));

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
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aTime("queryTime"), query.oid("objectId"));
      vQ.WHERE(query.aTime("queryTime").LE(date));

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

      vQ.SELECT(query.aTime("queryTime"), query.oid("objectId"));
      vQ.WHERE(query.aTime("queryTime").LE("13:00:00"));

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

      vQ.SELECT(query.aTime("queryTime"), query.oid("objectId"));
      vQ.WHERE(query.aTime("queryTime").LE("14:00:00"));

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

      vQ.SELECT(query.aTime("queryTime"), query.oid("objectId"));
      vQ.WHERE(query.aTime("queryTime").LE("12:00:00"));

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
    Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aTime("queryTime"), query.oid("objectId"));
      vQ.WHERE(query.aTime("queryTime").NE(date));

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

      vQ.SELECT(query.aTime("queryTime"), query.oid("objectId"));
      vQ.WHERE(query.aTime("queryTime").NE(date));

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

      vQ.SELECT(query.aTime("queryTime"), query.oid("objectId"));
      vQ.WHERE(query.aTime("queryTime").NE("12:00:00"));

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

      vQ.SELECT(query.aTime("queryTime"), query.oid("objectId"));
      vQ.WHERE(query.aTime("queryTime").NE("13:00:00"));

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
  public void testInvalidSubSelectInSelectClause()
  {
    QueryFactory queryFactory = new QueryFactory();

    BusinessDAOQuery bq = queryFactory.businessDAOQuery(MdBusinessInfo.CLASS);

    ValueQuery valueQuery1 = new ValueQuery(queryFactory);
    ValueQuery valueQuery2 = new ValueQuery(queryFactory);

    valueQuery1.SELECT(bq.aUUID(MdBusinessInfo.OID));

    valueQuery2.SELECT(bq.aCharacter(MdBusinessInfo.NAME), valueQuery1.getSubSelect());

    String failMsg = "Subselct contained a query that returned more than one row, yet the proper excepition was not thrown.";

    try
    {
      valueQuery2.getIterator();
      Assert.fail(failMsg);
    }
    catch (SubSelectReturnedMultipleRowsException e)
    {
      // this is expected
    }
    catch (Exception e)
    {
      Assert.fail(failMsg);
    }
  }

  @Request
  @Test
  public void testValidSubSelectInSelectClause()
  {
    QueryFactory queryFactory = new QueryFactory();

    BusinessDAOQuery bq = queryFactory.businessDAOQuery(MdBusinessInfo.CLASS);

    ValueQuery valueQuery1 = new ValueQuery(queryFactory);
    ValueQuery valueQuery2 = new ValueQuery(queryFactory);

    valueQuery1.SELECT(F.COUNT(bq.aUUID(MdBusinessInfo.OID)));

    valueQuery2.SELECT(bq.aCharacter(MdBusinessInfo.NAME), valueQuery1.getSubSelect());

    OIterator<ValueObject> i = null;

    try
    {
      i = valueQuery2.getIterator();
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (i != null)
      {
        i.close();
      }
    }
  }

  @Request
  @Test
  public void testUsingEntityQueryForValueQuery()
  {
    QueryFactory qf = new QueryFactory();

    ValueQuery vQ = qf.valueQuery();

    BusinessQuery mdClassQ = new BusinessQuery(vQ, MdClassInfo.CLASS);
    BusinessQuery mdAttrQ = new BusinessQuery(vQ, MdAttributeConcreteInfo.CLASS);

    vQ.SELECT(mdClassQ.aCharacter(MdClassInfo.NAME), mdAttrQ.aCharacter(MdAttributeConcreteInfo.NAME));
    vQ.WHERE(mdAttrQ.aReference(MdAttributeConcreteInfo.DEFINING_MD_CLASS).LEFT_JOIN_EQ(mdClassQ));

    try
    {
      mdClassQ.getIterator();
      Assert.fail("Able to query for objects using an EntityQuery object that was defined for value queries. ");
    }
    catch (QueryException e)
    {
      // this is expected
    }
  }

  @Request
  @Test
  public void testFalseContainsSQLSelectable()
  {
    QueryFactory qf = new QueryFactory();

    ValueQuery vQ = qf.valueQuery();
    BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

    vQ.SELECT(query.aBoolean("queryBoolean"));

    Assert.assertFalse(vQ.containsSelectableSQL());
  }

  @Request
  @Test
  public void testContainsSQLSelectableInSelectStatement()
  {
    QueryFactory qf = new QueryFactory();

    ValueQuery vQ = qf.valueQuery();
    BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

    vQ.SELECT(query.aBoolean("queryBoolean"));
    vQ.SELECT(vQ.aSQLCharacter("testSelectable", "'A'"));

    Assert.assertTrue(vQ.containsSelectableSQL());
  }

  @Request
  @Test
  public void testContainsSQLSelectableInWhere()
  {
    QueryFactory qf = new QueryFactory();

    ValueQuery vQ = qf.valueQuery();
    BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

    vQ.SELECT(query.aBoolean("queryBoolean"));
    vQ.WHERE(query.aBoolean("queryBoolean").EQ(vQ.aSQLBoolean(TestFixConst.ATTRIBUTE_BOOLEAN, "1")));

    Assert.assertTrue(vQ.containsSelectableSQL());
  }

  @Request
  @Test
  public void testColumnAlias()
  {
    QueryFactory qf = new QueryFactory();

    ValueQuery vQ = qf.valueQuery();
    BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

    AttributeBoolean selectable = query.aBoolean("queryBoolean");
    selectable.setColumnAlias("test");
    selectable.setUserDefinedAlias("test");

    vQ.SELECT(selectable);

    OIterator<ValueObject> iterator = vQ.getIterator();

    try
    {
      List<ValueObject> results = iterator.getAll();

      Assert.assertTrue(results.size() > 0);

      for (ValueObject result : results)
      {
        Assert.assertNotNull(result.getValue("test"));
      }
    }
    finally
    {
      iterator.close();
    }
  }

  @Request
  @Test
  public void testSameAttributeDifferentAliases()
  {
    QueryFactory qf = new QueryFactory();

    ValueQuery vQ = qf.valueQuery();
    BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

    AttributeBoolean first = query.aBoolean("queryBoolean");
    first.setColumnAlias("first");
    first.setUserDefinedAlias("first");

    AttributeBoolean second = query.aBoolean("queryBoolean");
    second.setColumnAlias("second");
    second.setUserDefinedAlias("second");

    vQ.SELECT(first, second);

    OIterator<ValueObject> iterator = vQ.getIterator();

    try
    {
      List<ValueObject> results = iterator.getAll();

      Assert.assertTrue(results.size() > 0);

      for (ValueObject result : results)
      {
        Assert.assertNotNull(result.getValue("first"));
        Assert.assertNotNull(result.getValue("second"));
      }
    }
    finally
    {
      iterator.close();
    }
  }

  // @Request @Test public void testContainsSQLSelectableInJoin()
  // {
  // QueryFactory qf = new QueryFactory();
  //
  // ValueQuery vQ = qf.valueQuery();
  // ValueQuery vQ2 = qf.valueQuery();
  // BusinessDAOQuery query =
  // qf.businessDAOQuery(QueryMasterSetup.selectedMdBusiness.getType());
  // BusinessDAOQuery refQuery =
  // qf.businessDAOQuery(QueryMasterSetup.childRefMdBusiness.getType());
  //
  // AttributeCharacter attribute = refQuery.aCharacter("oid");
  //
  // vQ.SELECT(query.aCharacter("oid"));
  // vQ2.SELECT(attribute);
  // vQ.WHERE(query.aReference("reference").LEFT_JOIN_EQ(vQ2.aSQLCharacter("left_join",
  // vQ2.getTableAlias() + "." + attribute._getAttributeName())));
  //
  // Assert.assertTrue(vQ.containsSelectableSQL());
  // }

  @Request
  @Test
  public void testCountBasic()
  {
    QueryFactory qf = new QueryFactory();

    ValueQuery vQ = qf.valueQuery();
    BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

    vQ.SELECT(query.aBoolean("queryBoolean"));

    long sqlCount = vQ.getCount();
    long iteratorCount = 0;

    OIterator<ValueObject> i = vQ.getIterator();

    while (i.hasNext())
    {
      i.next();
      iteratorCount++;
    }

    if (sqlCount != iteratorCount)
    {
      Assert.assertEquals("getCount() method returned a different result than the query iterator.", iteratorCount, sqlCount);
    }
  }

  @Request
  @Test
  public void testCountExplicitGroupBy()
  {
    QueryFactory qf = new QueryFactory();

    ValueQuery vQ = qf.valueQuery();
    BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

    vQ.SELECT(query.aBoolean("queryBoolean"));
    vQ.GROUP_BY(query.aBoolean("queryBoolean"));

    long sqlCount = vQ.getCount();
    long iteratorCount = 0;

    OIterator<ValueObject> i = vQ.getIterator();

    while (i.hasNext())
    {
      i.next();
      iteratorCount++;
    }

    if (sqlCount != iteratorCount)
    {
      Assert.assertEquals("getCount() method returned a different result than the query iterator.", iteratorCount, sqlCount);
    }
  }

  @Request
  @Test
  public void testCountImplicitGroupBy()
  {
    QueryFactory qf = new QueryFactory();

    ValueQuery vQ = qf.valueQuery();
    BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

    vQ.SELECT(F.MAX(query.get("queryCharacter")), query.get("queryBoolean"));
    vQ.GROUP_BY(query.get("queryBoolean"));

    long sqlCount = vQ.getCount();
    long iteratorCount = 0;

    OIterator<ValueObject> i = vQ.getIterator();

    while (i.hasNext())
    {
      i.next();
      iteratorCount++;
    }

    if (sqlCount != iteratorCount)
    {
      Assert.assertEquals("getCount() method returned a different result than the query iterator.", iteratorCount, sqlCount);
    }
  }

  /*
   * // FIXME find default MySQL equivalent @Request @Test public void
   * testCountSelectable() { long total = 100; QueryFactory f = new
   * QueryFactory(); ValueQuery v = new ValueQuery(f);
   * 
   * String gen = "gen"; String ct = "ct"; SelectableSQLInteger s =
   * v.aSQLInteger(gen, gen, gen); SelectableSQLLong c = v.aSQLLong(ct,
   * "count(*) over()", ct); v.SELECT(s, c);
   * 
   * v.setCountSelectable(c);
   * 
   * v.FROM("generate_series(1,"+total+")", gen);
   * 
   * // restrict the rows. This not only gives us one result, which is all we
   * need, // but it shows that the window count isn't affected by LIMIT
   * v.restrictRows(1, 1);
   * 
   * ValueObject o = v.getIterator().getAll().get(0);
   * Assert.assertEquals(Long.parseLong(o.getValue(ct)), total);
   * Assert.assertEquals(v.getCount(), total); }
   * 
   * @Request @Test public void testAggregateCountSelectable() { long total =
   * 100;
   * 
   * QueryFactory f = new QueryFactory(); ValueQuery v = new ValueQuery(f);
   * 
   * String gen = "gen"; String ct = "ct"; SelectableSQLInteger s =
   * v.aSQLInteger(gen, gen, gen); SelectableSQLLong c = v.aSQLAggregateLong(ct,
   * "count(*) over()", ct); v.SELECT(s, c);
   * 
   * v.setCountSelectable(c);
   * 
   * v.FROM("generate_series(1,"+total+")", gen); v.GROUP_BY(s);
   * 
   * // restrict the rows. This not only gives us one result, which is all we
   * need, // but it shows that the window count isn't affected by LIMIT
   * v.restrictRows(1, 1);
   * 
   * ValueObject o = v.getIterator().getAll().get(0);
   * Assert.assertEquals(Long.parseLong(o.getValue(ct)), total);
   * Assert.assertEquals(v.getCount(), total); }
   */

}
