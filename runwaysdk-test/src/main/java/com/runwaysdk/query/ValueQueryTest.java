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

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.business.BusinessQuery;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;

public class ValueQueryTest extends TestCase
{
  private static List<String> testObjectIdList = new LinkedList<String>();

  private static List<String> objectList;

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

  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(new QueryMasterSetup(ValueQueryTest.suite(), QueryMasterSetup.parentQueryInfo.getType(), QueryMasterSetup.parentRefQueryInfo.getType()));
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ValueQueryTest.class);

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
    BusinessDAO testQueryObject1 = BusinessDAO.newInstance(QueryMasterSetup.childMdBusiness.definesType());
    testQueryObject1.setValue("queryBoolean", MdAttributeBooleanInfo.TRUE);
    testQueryObject1.setValue("queryInteger", "200");
    testQueryObject1.apply();

    testObjectIdList.add(testQueryObject1.getId());

    BusinessDAO testQueryObject2 = BusinessDAO.newInstance(QueryMasterSetup.childMdBusiness.definesType());
    testQueryObject2.setValue("queryBoolean", MdAttributeBooleanInfo.FALSE);
    testQueryObject2.setValue("queryInteger", "150");
    testQueryObject2.apply();

    testObjectIdList.add(testQueryObject2.getId());

    BusinessDAO testQueryObject3 = BusinessDAO.newInstance(QueryMasterSetup.childMdBusiness.definesType());
    testQueryObject3.setValue("queryBoolean", MdAttributeBooleanInfo.FALSE);
    testQueryObject3.setValue("queryInteger", "250");
    testQueryObject3.apply();

    testObjectIdList.add(testQueryObject3.getId());

    BusinessDAO testQueryObject4 = BusinessDAO.newInstance(QueryMasterSetup.childMdBusiness.definesType());
    testQueryObject4.setValue("queryDate", "");
    testQueryObject4.apply();

    testObjectIdList.add(testQueryObject4.getId());

    objectList = MdBusinessDAO.getEntityIdsDB(QueryMasterSetup.childQueryInfo.getType());
  }

  public static void classTearDown()
  {
    for (String id : testObjectIdList)
    {
      BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();
      testQueryObject.delete();
    }
  }

  /**
   * No setup needed non-Javadoc)
   * 
   * @see junit.framework.TestCase#setUp()
   */
  protected void setUp() throws Exception
  {

  }

  /**
   * No teardown neaded
   * 
   * @see junit.framework.TestCase#tearDown()
   */
  protected void tearDown() throws Exception
  {
  }
  
  public void testWindowFunction()
  {
    OIterator<ValueObject> i = null;

    try
    {
      QueryFactory qf = new QueryFactory();
      
      // Query number of ID fields
      
      ValueQuery vQ1 = qf.valueQuery();
      BusinessDAOQuery mdAttrQ = qf.businessDAOQuery(MdAttributeConcreteInfo.CLASS);
          
      vQ1.SELECT(
          F.COUNT(mdAttrQ.get(MdAttributeConcreteInfo.NAME), "count"),
          mdAttrQ.get(MdAttributeConcreteInfo.NAME), 
          mdAttrQ.get(MdAttributeConcreteInfo.REQUIRED));
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
          Boolean required = Boolean.parseBoolean(valueObject.getValue(MdAttributeConcreteInfo.REQUIRED));
        
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
      
      vQ2.SELECT_DISTINCT(
          F.COUNT(mdAttrQ.get(MdAttributeConcreteInfo.NAME), "count"),
          F.STRING_AGG(mdAttrQ.get(MdAttributeConcreteInfo.NAME), ", ", "STRING_AGG").OVER(F.PARTITION_BY(F.COUNT(mdAttrQ.get(MdAttributeConcreteInfo.NAME)), mdAttrQ.get(MdAttributeConcreteInfo.REQUIRED))),
          mdAttrQ.RANK().OVER(F.PARTITION_BY(mdAttrQ.get(MdAttributeConcreteInfo.REQUIRED)), new OrderBy(F.COUNT(mdAttrQ.get(MdAttributeConcreteInfo.NAME)), OrderBy.SortOrder.DESC)), 
          mdAttrQ.get(MdAttributeConcreteInfo.REQUIRED));
      vQ2.GROUP_BY(mdAttrQ.get(MdAttributeConcreteInfo.NAME), mdAttrQ.get(MdAttributeConcreteInfo.REQUIRED));
      vQ2.ORDER_BY_DESC(F.COUNT(mdAttrQ.get(MdAttributeConcreteInfo.NAME), "count"));
      
      i = vQ2.getIterator();
      
      try
      {
        for (ValueObject valueObject : i)
        {
          Integer idCount = Integer.parseInt(valueObject.getValue("count"));
          String stringAgg = valueObject.getValue("STRING_AGG");
          Boolean required = Boolean.parseBoolean(valueObject.getValue(MdAttributeConcreteInfo.REQUIRED));
          
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
          assertEquals("The number of values returned in the STRING_AGG function were not as expected.", reqSet.toArray().length, windowValues.length);
          for (String windowValue : windowValues)
          {
            assertTrue("Window function did not return an expected value from the STRING_AGG function", reqSet.contains(windowValue.trim()));
          }
        }
      }
      finally
      {
        i.close();
      }
//      assertTrue("Invalid query. Expecting to fetch the number of ID attributes", idCount > 0);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
      {
        i.close();
      }
    }
  }

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

      vQ.SELECT(childQuery.aCharacter("queryCharacter"), childQuery.id("id"));
      vQ.WHERE(childQuery.aCharacter("queryCharacter").NE(""));

      vQ2.SELECT(childRefQuery.aCharacter("refQueryCharacter"), childRefQuery.id("id"));
      vQ2.WHERE(childRefQuery.aCharacter("refQueryCharacter").NE(""));

      vQ3.UNION(vQ, vQ2);

      i = vQ3.getIterator();

      if (!i.hasNext())
      {
        fail("The query did not return any results when it should have.");
      }

      int count = 0;

      while (i.hasNext())
      {
        i.next();

        count++;
      }

      int expected = 0;

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        if (!testQueryObject.getValue("queryCharacter").equals(""))
          expected++;
      }
      for (String id : MdBusinessDAO.getEntityIdsDB(QueryMasterSetup.childRefQueryInfo.getType()))
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        if (!testQueryObject.getValue("refQueryCharacter").equals(""))
          expected++;
      }

      if (expected != count)
      {
        fail("The value query union did not return the proper number of results. Expected is " + expected + ", it returned " + count + ".");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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

      vQ.SELECT(childQuery1.id("id"));
      vQ2.SELECT(childQuery2.id("id"));

      vQ3.UNION(vQ, vQ2);

      long expected = 2 * vQ3.getCount();

      // UNION ALL
      qf = new QueryFactory();

      vQ = qf.valueQuery();
      vQ2 = qf.valueQuery();
      vQ3 = qf.valueQuery();
      childQuery1 = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      childQuery2 = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(childQuery1.id("id"));
      vQ2.SELECT(childQuery2.id("id"));

      vQ3.UNION_ALL(vQ, vQ2);

      long count = vQ3.getCount();

      if (expected != count)
      {
        fail("The value query UNION ALL did not return the proper number of results. Expected is " + expected + ", it returned " + count + ".");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

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
        fail("The query did not return any results when it should have.");
      }

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (!o.getValue("refQueryCharacter").equals("")) // this is an object
        // from childRefQuery
        {
          if (!o.getValue("queryInteger").equals("200"))
            fail("The Left Join query returned a resultant object with incorrect integer attributes. queryInteger = '" + o.getValue("queryInteger") + "'");
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

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
        fail("The query did not return any results when it should have.");
      }

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (!o.getValue("refQueryCharacter").equals("")) // this is an object
        // from childRefQuery
        {
          if (!o.getValue("refQueryInteger").equals("200"))
            fail("The Left Join query returned a resultant object with incorrect integer attributes. queryInteger = '" + o.getValue("refQueryInteger") + "'");
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

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
        fail("The query did not return any results when it should have.");
      }

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (!o.getValue("refQueryCharacter").equals("")) // this is an object
        // from childRefQuery
        {
          if (o.getValue("queryInteger").equals("200"))
            fail("The Left Join query returned a resultant object with incorrect integer attributes. queryInteger = '" + o.getValue("queryInteger") + "'");
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

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
        fail("The query did not return any results when it should have.");
      }

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (!o.getValue("refQueryCharacter").equals("")) // this is an object
        // from childRefQuery
        {
          if (!o.getValue("refQueryInteger").equals("200"))
            fail("The Left Join query returned a resultant object with incorrect integer attributes. refQueryInteger = '" + o.getValue("refQueryInteger") + "'");
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

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
        fail("The query did not return any results when it should have.");
      }

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (!o.getValue("refQueryCharacter").equals("")) // this is an object
        // from childRefQuery
        {
          if (Integer.parseInt(o.getValue("queryInteger")) < 200)
            fail("The Left Join query returned a resultant object with incorrect integer attributes. queryInteger = '" + o.getValue("queryInteger") + "'");
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

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
        fail("The query did not return any results when it should have.");
      }

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (!o.getValue("refQueryCharacter").equals("")) // this is an object
        // from childRefQuery
        {
          if (!o.getValue("refQueryInteger").equals("200"))
            fail("The Left Join query returned a resultant object with incorrect integer attributes. refQueryInteger = '" + o.getValue("refQueryInteger") + "'");
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

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
        fail("The query did not return any results when it should have.");
      }

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (!o.getValue("refQueryCharacter").equals("")) // this is an object
        // from childRefQuery
        {
          if (Integer.parseInt(o.getValue("queryInteger")) <= 200)
            fail("The Left Join query returned a resultant object with incorrect integer attributes. queryInteger = '" + o.getValue("queryInteger") + "'");
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

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
        fail("The query did not return any results when it should have.");
      }

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (!o.getValue("refQueryCharacter").equals("")) // this is an object
        // from childRefQuery
        {
          if (!o.getValue("refQueryInteger").equals("200"))
            fail("The Left Join query returned a resultant object with incorrect integer attributes. refQueryInteger = '" + o.getValue("refQueryInteger") + "'");
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

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
        fail("The query did not return any results when it should have.");
      }

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (!o.getValue("refQueryCharacter").equals("")) // this is an object
        // from childRefQuery
        {
          if (Integer.parseInt(o.getValue("queryInteger")) > 200)
            fail("The Left Join query returned a resultant object with incorrect integer attributes. queryInteger = '" + o.getValue("queryInteger") + "'");
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

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
        fail("The query did not return any results when it should have.");
      }

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (!o.getValue("refQueryCharacter").equals("")) // this is an object
        // from childRefQuery
        {
          if (!o.getValue("refQueryInteger").equals("200"))
            fail("The Left Join query returned a resultant object with incorrect integer attributes. refQueryInteger = '" + o.getValue("refQueryInteger") + "'");
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

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
        fail("The query did not return any results when it should have.");
      }

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (!o.getValue("refQueryCharacter").equals("")) // this is an object
        // from childRefQuery
        {
          if (Integer.parseInt(o.getValue("queryInteger")) >= 200)
            fail("The Left Join query returned a resultant object with incorrect integer attributes. queryInteger = '" + o.getValue("queryInteger") + "'");
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

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
        fail("The query did not return any results when it should have.");
      }

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (!o.getValue("refQueryCharacter").equals("")) // this is an object
        // from childRefQuery
        {
          if (!o.getValue("refQueryInteger").equals("200"))
            fail("The Left Join query returned a resultant object with incorrect integer attributes. refQueryInteger = '" + o.getValue("refQueryInteger") + "'");
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aBoolean("queryBoolean"), query.id("objectId"));
      vQ.WHERE(query.aBoolean("queryBoolean").EQ(true));

      // vQ.WHERE( query.LEFT_JOIN_EQ(query.aBoolean("queryBoolean"),
      // query.aCharacter("queryCharacter")) );

      i = vQ.getIterator();

      int count = 0;

      while (i.hasNext())
      {
        ValueObject o = i.next();
        boolean value = Boolean.parseBoolean(o.getValue("queryBoolean"));

        assertTrue(value);

        count++;
      }

      int expected = 0;

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        if (testQueryObject.getValue("queryBoolean").equals("true"))
          expected++;
      }

      assertEquals(expected, count);

      // perform another query for false values that should find exactly 2
      // matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aBoolean("queryBoolean"), query.id("objectId"));
      vQ.WHERE(query.aBoolean("queryBoolean").EQ(false));

      i = vQ.getIterator();

      count = 0;

      while (i.hasNext())
      {
        ValueObject o = i.next();
        boolean value = Boolean.parseBoolean(o.getValue("queryBoolean"));

        assertFalse(value);

        count++;
      }

      expected = 0;

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        if (testQueryObject.getValue("queryBoolean").equals("false"))
          expected++;
      }

      assertEquals(expected, count);
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aBoolean("queryBoolean"), query.id("objectId"));
      vQ.WHERE(query.aBoolean("queryBoolean").EQ(MdAttributeBooleanInfo.TRUE));

      i = vQ.getIterator();

      int count = 0;

      while (i.hasNext())
      {
        ValueObject o = i.next();
        boolean value = Boolean.parseBoolean(o.getValue("queryBoolean"));

        assertTrue(value);

        count++;
      }

      int expected = 0;

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        if (testQueryObject.getValue("queryBoolean").equals("true"))
          expected++;
      }

      assertEquals(expected, count);

      // perform another query for false values that should find exactly 2
      // matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aBoolean("queryBoolean"), query.id("objectId"));
      vQ.WHERE(query.aBoolean("queryBoolean").EQ(MdAttributeBooleanInfo.FALSE));

      i = vQ.getIterator();

      count = 0;

      while (i.hasNext())
      {
        ValueObject o = i.next();
        boolean value = Boolean.parseBoolean(o.getValue("queryBoolean"));

        assertFalse(value);

        count++;
      }

      expected = 0;

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        if (testQueryObject.getValue("queryBoolean").equals("false"))
          expected++;
      }

      assertEquals(expected, count);
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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

    String origValue = QueryMasterSetup.testQueryObject1.getValue("queryBoolean");
    QueryMasterSetup.testQueryObject1.setValue("queryBoolean", "");
    QueryMasterSetup.testQueryObject1.apply();

    try
    {
      // perform a query for null values
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aBoolean("queryBoolean"), query.id("objectId"));
      vQ.WHERE(query.aBoolean("queryBoolean").EQ(""));

      i = vQ.getIterator();

      int count = 0;

      while (i.hasNext())
      {
        ValueObject o = i.next();
        String value = o.getValue("queryBoolean");

        assertEquals("", value);

        count++;
      }

      int expected = 0;

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        if (testQueryObject.getValue("queryBoolean").equals(""))
          expected++;
      }

      assertEquals(expected, count);

      // perform another query to find all objects with non-null queryBoolean
      // values
      vQ = qf.valueQuery();

      vQ.SELECT(query.aBoolean("queryBoolean"), query.id("objectId"));
      vQ.WHERE(query.aBoolean("queryBoolean").NE(""));

      i = vQ.getIterator();

      count = 0;

      while (i.hasNext())
      {
        i.next();
        count++;
      }

      expected = 0;

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        if (!testQueryObject.getValue("queryBoolean").equals(""))
          expected++;
      }

      assertEquals(expected, count);
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.testQueryObject1.setValue("queryBoolean", origValue);
      QueryMasterSetup.testQueryObject1.apply();

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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aBoolean("queryBoolean"), query.id("objectId"));
      vQ.WHERE(query.aBoolean("queryBoolean").NE(false));

      i = vQ.getIterator();

      int count = 0;

      while (i.hasNext())
      {
        ValueObject o = i.next();
        boolean value = Boolean.parseBoolean(o.getValue("queryBoolean"));

        assertTrue(value);

        count++;
      }

      int expected = 0;

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        if (testQueryObject.getValue("queryBoolean").equals("true"))
          expected++;
      }

      assertEquals(expected, count);

      // perform another query for false values that should find exactly 2
      // matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aBoolean("queryBoolean"), query.id("objectId"));
      vQ.WHERE(query.aBoolean("queryBoolean").NE(true));

      i = vQ.getIterator();

      count = 0;

      while (i.hasNext())
      {
        ValueObject o = i.next();
        boolean value = Boolean.parseBoolean(o.getValue("queryBoolean"));

        assertFalse(value);

        count++;
      }

      expected = 0;

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        if (testQueryObject.getValue("queryBoolean").equals("false"))
          expected++;
      }

      assertEquals(expected, count);
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testBooleanNotEqualString()
  {
    try
    {
      // perform a query for true values that should find exactly 2 matches
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aBoolean("queryBoolean"), query.id("objectId"));
      vQ.WHERE(query.aBoolean("queryBoolean").NE(MdAttributeBooleanInfo.FALSE));

      OIterator<ValueObject> i = vQ.getIterator();

      int count = 0;

      while (i.hasNext())
      {
        ValueObject o = i.next();
        boolean value = Boolean.parseBoolean(o.getValue("queryBoolean"));

        assertTrue(value);

        count++;
      }

      int expected = 0;

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        if (testQueryObject.getValue("queryBoolean").equals("false"))
          expected++;
      }

      assertEquals(expected, count);

      // perform another query for false values that should find exactly 2
      // matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aBoolean("queryBoolean"), query.id("objectId"));
      vQ.WHERE(query.aBoolean("queryBoolean").NE(MdAttributeBooleanInfo.TRUE));

      i = vQ.getIterator();

      count = 0;

      while (i.hasNext())
      {
        ValueObject o = i.next();
        boolean value = Boolean.parseBoolean(o.getValue("queryBoolean"));

        assertFalse(value);

        count++;
      }

      expected = 0;

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        if (testQueryObject.getValue("queryBoolean").equals("true"))
          expected++;
      }

      assertEquals(expected, count);
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aCharacter("queryCharacter").EQ("some character value"));

      i = vQ.getIterator();

      int count = 0;

      while (i.hasNext())
      {
        ValueObject o = i.next();
        String value = o.getValue("queryCharacter");

        assertEquals("some character value", value);

        count++;
      }

      int expected = 0;

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        if (testQueryObject.getValue("queryCharacter").equals("some character value"))
          expected++;
      }

      assertEquals(expected, count);

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aCharacter("queryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aCharacter("queryCharacter").EQ("wrong character value"));

      i = vQ.getIterator();

      count = 0;

      while (i.hasNext())
      {
        ValueObject o = i.next();
        String value = o.getValue("queryCharacter");

        assertEquals("wrong character value", value);

        count++;
      }

      expected = 0;

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        if (testQueryObject.getValue("queryCharacter").equals("wrong character value"))
          expected++;
      }

      assertEquals(expected, count);
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aCharacter("queryCharacter").EQi("SOME CHARACTER VALUE"));

      i = vQ.getIterator();

      int count = 0;

      while (i.hasNext())
      {
        ValueObject o = i.next();
        String value = o.getValue("queryCharacter");

        assertEquals("some character value", value);

        count++;
      }

      int expected = 0;

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        if (testQueryObject.getValue("queryCharacter").equals("some character value"))
          expected++;
      }

      assertEquals(expected, count);

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aCharacter("queryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aCharacter("queryCharacter").EQi("WRONG CHARACTER VALUE"));

      i = vQ.getIterator();

      assertFalse(i.hasNext());
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aCharacter("queryCharacter").IN("wrong value 1", "some character value", "wrong value 2"));

      i = vQ.getIterator();

      int count = 0;

      while (i.hasNext())
      {
        ValueObject o = i.next();
        String value = o.getValue("queryCharacter");

        assertEquals("some character value", value);

        count++;
      }

      int expected = 0;

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        if (testQueryObject.getValue("queryCharacter").equals("some character value"))
          expected++;
      }

      assertEquals(expected, count);

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aCharacter("queryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aCharacter("queryCharacter").IN("wrong value 1", "wrong value 2", "wrong value 3", "SOME CHARACTER VALUE"));

      i = vQ.getIterator();

      assertFalse(i.hasNext());
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aCharacter("queryCharacter").INi("wrong value 1", "SOME CHARACTER VALUE", "wrong value 2"));

      i = vQ.getIterator();

      int count = 0;

      while (i.hasNext())
      {
        ValueObject o = i.next();
        String value = o.getValue("queryCharacter");

        assertEquals("some character value", value);

        count++;
      }

      int expected = 0;

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        if (testQueryObject.getValue("queryCharacter").equals("some character value"))
          expected++;
      }

      assertEquals(expected, count);

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aCharacter("queryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aCharacter("queryCharacter").IN("wrong value 1", "wrong value 2", "wrong value 3"));

      i = vQ.getIterator();

      assertFalse(i.hasNext());
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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

    String origValue = QueryMasterSetup.testQueryObject1.getValue("queryCharacter");
    QueryMasterSetup.testQueryObject1.setValue("queryCharacter", "");
    QueryMasterSetup.testQueryObject1.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aCharacter("queryCharacter").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 3 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aCharacter("queryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aCharacter("queryCharacter").NE(""));

      i = vQ.getIterator();

      int count = 0;

      while (i.hasNext())
      {
        i.next();

        count++;
      }

      int expected = 0;

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        if (!testQueryObject.getValue("queryCharacter").equals(""))
          expected++;
      }

      assertEquals(expected, count);
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.testQueryObject1.setValue("queryCharacter", origValue);
      QueryMasterSetup.testQueryObject1.apply();

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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aCharacter("queryCharacter").LIKE("%character%"));

      i = vQ.getIterator();

      int count = 0;

      while (i.hasNext())
      {
        ValueObject o = i.next();
        String value = o.getValue("queryCharacter");

        assertEquals("some character value", value);

        count++;
      }

      int expected = 0;

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        if (testQueryObject.getValue("queryCharacter").equals("some character value"))
          expected++;
      }

      assertEquals(expected, count);

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aCharacter("queryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aCharacter("queryCharacter").LIKE("%character"));

      i = vQ.getIterator();

      assertFalse(i.hasNext());
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aCharacter("queryCharacter").LIKEi("%CHARACTER%"));

      i = vQ.getIterator();

      int count = 0;

      while (i.hasNext())
      {
        ValueObject o = i.next();
        String value = o.getValue("queryCharacter");

        assertEquals("some character value", value);

        count++;
      }

      int expected = 0;

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        if (testQueryObject.getValue("queryCharacter").equals("some character value"))
          expected++;
      }

      assertEquals(expected, count);

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aCharacter("queryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aCharacter("queryCharacter").LIKEi("%CHARACTER"));

      i = vQ.getIterator();

      assertFalse(i.hasNext());
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter"), query.id("objectId"));
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

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        String value = testQueryObject.getValue("queryCharacter");
        if (!value.equals("wrong character value"))
          expected++;
      }

      assertEquals(expected, count);
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter"), query.id("objectId"));
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

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        String value = testQueryObject.getValue("queryCharacter");
        if (!value.equals("wrong character value"))
          expected++;
      }

      assertEquals(expected, count);
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter"), query.id("objectId"));
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

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        String value = testQueryObject.getValue("queryCharacter");
        if (! ( value.equals("wrong 1") || value.equals("wrong 2") || value.equals("wrong 3") ))
          expected++;
      }

      assertEquals(expected, count);
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter"), query.id("objectId"));
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

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        String value = testQueryObject.getValue("queryCharacter");
        if (! ( value.equals("wrong 1") || value.equals("wrong 2") || value.equals("wrong 3") ))
          expected++;
      }

      assertEquals(expected, count);
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter"), query.id("objectId"));
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

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        String value = testQueryObject.getValue("queryCharacter");
        if (!value.equals("some character value"))
          expected++;
      }

      assertEquals(expected, count);
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter"), query.id("objectId"));
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

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        String value = testQueryObject.getValue("queryCharacter");
        if (!value.equals("some character value"))
          expected++;
      }

      assertEquals(expected, count);
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-06", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDate("queryDate"), query.id("objectId"));
      vQ.WHERE(query.aDate("queryDate").EQ(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-05", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aDate("queryDate"), query.id("objectId"));
      vQ.WHERE(query.aDate("queryDate").EQ(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDate("queryDate"), query.id("objectId"));
      vQ.WHERE(query.aDate("queryDate").EQ("2006-12-06"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDate("queryDate"), query.id("objectId"));
      vQ.WHERE(query.aDate("queryDate").EQ("2006-05-05"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-05", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDate("queryDate"), query.id("objectId"));
      vQ.WHERE(query.aDate("queryDate").GT(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-07", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aDate("queryDate"), query.id("objectId"));
      vQ.WHERE(query.aDate("queryDate").GT(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDate("queryDate"), query.id("objectId"));
      vQ.WHERE(query.aDate("queryDate").GT("2006-12-05"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDate("queryDate"), query.id("objectId"));
      vQ.WHERE(query.aDate("queryDate").GT("2006-12-07"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-06", new java.text.ParsePosition(0));

    try
    {
      // perform a query with a date equal to the stored
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDate("queryDate"), query.id("objectId"));
      vQ.WHERE(query.aDate("queryDate").GE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query with a date less than the stored
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-05", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aDate("queryDate"), query.id("objectId"));
      vQ.WHERE(query.aDate("queryDate").GE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query with a date greater than the stored
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-07", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aDate("queryDate"), query.id("objectId"));
      vQ.WHERE(query.aDate("queryDate").GE(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDate("queryDate"), query.id("objectId"));
      vQ.WHERE(query.aDate("queryDate").GE("2006-12-06"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query with a date less than the stored
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDate("queryDate"), query.id("objectId"));
      vQ.WHERE(query.aDate("queryDate").GE("2006-12-05"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query with a date greater than the stored
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDate("queryDate"), query.id("objectId"));
      vQ.WHERE(query.aDate("queryDate").GE("2006-12-07"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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

    String origValue = QueryMasterSetup.testQueryObject1.getValue("queryDate");
    QueryMasterSetup.testQueryObject1.setValue("queryDate", "");
    QueryMasterSetup.testQueryObject1.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDate("queryDate"), query.id("objectId"));
      vQ.WHERE(query.aDate("queryDate").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      int count = 0;

      while (i.hasNext())
      {
        assertTrue(i.next().getValue("queryDate").equals(""));

        count++;
      }

      int expected = 0;

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        if (testQueryObject.getValue("queryDate").equals(""))
          expected++;
      }

      assertEquals(expected, count);

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDate("queryDate"), query.id("objectId"));
      vQ.WHERE(query.aDate("queryDate").NE(""));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.testQueryObject1.setValue("queryDate", origValue);
      QueryMasterSetup.testQueryObject1.apply();

      if (i != null)
        i.close();
    }
  }

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

      vQ.SELECT(query.aDate("queryDate"), query.id("objectId"));
      vQ.WHERE(query.aDate("queryDate").LT(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-05", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aDate("queryDate"), query.id("objectId"));
      vQ.WHERE(query.aDate("queryDate").LT(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDate("queryDate"), query.id("objectId"));
      vQ.WHERE(query.aDate("queryDate").LT("2006-12-07"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDate("queryDate"), query.id("objectId"));
      vQ.WHERE(query.aDate("queryDate").LT("2006-12-05"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-06", new java.text.ParsePosition(0));

    try
    {
      // perform a query with a date equal to the stored
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDate("queryDate"), query.id("objectId"));
      vQ.WHERE(query.aDate("queryDate").LE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query with a date less than the stored
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-07", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aDate("queryDate"), query.id("objectId"));
      vQ.WHERE(query.aDate("queryDate").LE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query with a date greater than the stored
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-05", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aDate("queryDate"), query.id("objectId"));
      vQ.WHERE(query.aDate("queryDate").LE(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDate("queryDate"), query.id("objectId"));
      vQ.WHERE(query.aDate("queryDate").LE("2006-12-06"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query with a date less than the stored
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDate("queryDate"), query.id("objectId"));
      vQ.WHERE(query.aDate("queryDate").LE("2006-12-07"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query with a date greater than the stored
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDate("queryDate"), query.id("objectId"));
      vQ.WHERE(query.aDate("queryDate").LE("2006-12-05"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-05", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDate("queryDate"), query.id("objectId"));
      vQ.WHERE(query.aDate("queryDate").NE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-12-06", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aDate("queryDate"), query.id("objectId"));
      vQ.WHERE(query.aDate("queryDate").NE(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDate("queryDate"), query.id("objectId"));
      vQ.WHERE(query.aDate("queryDate").NE("2006-12-05"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDate("queryDate"), query.id("objectId"));
      vQ.WHERE(query.aDate("queryDate").NE("2006-12-06"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-06 13:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDateTime("queryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").EQ(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-06 12:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aDateTime("queryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").EQ(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDateTime("queryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").EQ("2006-12-06 13:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDateTime("queryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").EQ("2006-12-06 12:00:00"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-06 12:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDateTime("queryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").GT(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-06 13:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aDateTime("queryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").GT(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDateTime("queryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").GT("2006-12-06 12:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDateTime("queryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").GT("2006-12-06 13:00:00"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-06 13:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDateTime("queryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").GE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-05 13:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aDateTime("queryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").GE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-07 13:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aDateTime("queryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").GE(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDateTime("queryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").GE("2006-12-06 13:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDateTime("queryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").GE("2006-12-05 13:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDateTime("queryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").GE("2006-12-07 13:00:00"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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

    String origValue = QueryMasterSetup.testQueryObject1.getValue("queryDateTime");
    QueryMasterSetup.testQueryObject1.setValue("queryDateTime", "");
    QueryMasterSetup.testQueryObject1.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDateTime("queryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      int count = 0;

      while (i.hasNext())
      {
        assertTrue(i.next().getValue("queryDateTime").equals(""));

        count++;
      }

      int expected = 0;

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        if (testQueryObject.getValue("queryDateTime").equals(""))
          expected++;
      }

      assertEquals(expected, count);

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDateTime("queryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").NE(""));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.testQueryObject1.setValue("queryDateTime", origValue);
      QueryMasterSetup.testQueryObject1.apply();

      if (i != null)
        i.close();
    }
  }

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

      vQ.SELECT(query.aDateTime("queryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").LT(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-05 13:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aDateTime("queryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").LT(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDateTime("queryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").LT("2006-12-07 12:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDateTime("queryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").LT("2006-12-05 13:00:00"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-06 13:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDateTime("queryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").LE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-07 13:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aDateTime("queryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").LE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-05 13:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aDateTime("queryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").LE(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDateTime("queryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").LE("2006-12-06 13:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDateTime("queryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").LE("2006-12-07 13:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDateTime("queryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").LE("2006-12-05 13:00:00"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-05 13:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDateTime("queryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").NE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-06 13:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aDateTime("queryDateTime"), query.id("objectId"));
      vQ.WHERE(AND.get(query.aDateTime("queryDateTime").NE(date), query.aDateTime("queryDateTime").NE("")));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDateTime("queryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aDateTime("queryDateTime").NE("2006-12-05 13:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDateTime("queryDateTime"), query.id("objectId"));
      vQ.WHERE(AND.get(query.aDateTime("queryDateTime").NE("2006-12-06 13:00:00"), query.aDateTime("queryDateTime").NE("")));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDecimal("queryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").EQ(new BigDecimal(100.5)));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDecimal("queryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").EQ(new BigDecimal(101.5)));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDecimal("queryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").EQ("100.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDecimal("queryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").EQ("101.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDecimal("queryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").GT(new BigDecimal(100)));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDecimal("queryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").GT(new BigDecimal(101)));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDecimal("queryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").GT("100"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDecimal("queryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").GT("101"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDecimal("queryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").GE(new BigDecimal(100.5)));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDecimal("queryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").GE(new BigDecimal(100)));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDecimal("queryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").GE(new BigDecimal(101)));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDecimal("queryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").GE("100.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDecimal("queryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").GE("100"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDecimal("queryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").GE("101"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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

    String origValue = QueryMasterSetup.testQueryObject1.getValue("queryDecimal");
    QueryMasterSetup.testQueryObject1.setValue("queryDecimal", "");
    QueryMasterSetup.testQueryObject1.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDecimal("queryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      int count = 0;

      while (i.hasNext())
      {
        assertTrue(i.next().getValue("queryDecimal").equals(""));

        count++;
      }

      int expected = 0;

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        if (testQueryObject.getValue("queryDecimal").equals(""))
          expected++;
      }

      assertEquals(expected, count);

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDecimal("queryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").NE(""));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.testQueryObject1.setValue("queryDecimal", origValue);
      QueryMasterSetup.testQueryObject1.apply();

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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDecimal("queryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").LT(new BigDecimal(101)));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDecimal("queryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").LT(new BigDecimal(100)));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDecimal("queryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").LT("101"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDecimal("queryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").LT("100"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDecimal("queryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").LE(new BigDecimal(100.5)));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDecimal("queryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").LE(new BigDecimal(101)));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDecimal("queryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").LE(new BigDecimal(99)));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDecimal("queryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").LE("100.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDecimal("queryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").LE("101"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDecimal("queryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").LE("99"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDecimal("queryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").NE(new BigDecimal(101)));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDecimal("queryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").NE(new BigDecimal(100.5)));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDecimal("queryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").NE("101"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDecimal("queryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aDecimal("queryDecimal").NE("100.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDouble("queryDouble"), query.id("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").EQ(100.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDouble("queryDouble"), query.id("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").EQ(101.5));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDouble("queryDouble"), query.id("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").EQ("100.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDouble("queryDouble"), query.id("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").EQ("101.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDouble("queryDouble"), query.id("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").GT(10.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDouble("queryDouble"), query.id("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").GT(120.5));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDouble("queryDouble"), query.id("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").GT("10.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDouble("queryDouble"), query.id("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").GT("120.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDouble("queryDouble"), query.id("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").GE(100.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDouble("queryDouble"), query.id("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").GE(10.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDouble("queryDouble"), query.id("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").GE(120.5));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDouble("queryDouble"), query.id("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").GE("100.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDouble("queryDouble"), query.id("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").GE("10.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDouble("queryDouble"), query.id("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").GE("120.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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

    String origValue = QueryMasterSetup.testQueryObject1.getValue("queryDouble");
    QueryMasterSetup.testQueryObject1.setValue("queryDouble", "");
    QueryMasterSetup.testQueryObject1.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDouble("queryDouble"), query.id("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      int count = 0;

      while (i.hasNext())
      {
        assertTrue(i.next().getValue("queryDouble").equals(""));

        count++;
      }

      int expected = 0;

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        if (testQueryObject.getValue("queryDouble").equals(""))
          expected++;
      }

      assertEquals(expected, count);

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDouble("queryDouble"), query.id("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").NE(""));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.testQueryObject1.setValue("queryDouble", origValue);
      QueryMasterSetup.testQueryObject1.apply();

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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDouble("queryDouble"), query.id("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").LT(120.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDouble("queryDouble"), query.id("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").LT(10.5));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDouble("queryDouble"), query.id("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").LT("120.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDouble("queryDouble"), query.id("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").LT("10.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDouble("queryDouble"), query.id("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").LE(100.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on less than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDouble("queryDouble"), query.id("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").LE(120.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDouble("queryDouble"), query.id("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").LE(10.5));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDouble("queryDouble"), query.id("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").LE("100.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on less than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDouble("queryDouble"), query.id("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").LE("120.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDouble("queryDouble"), query.id("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").LE("10.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDouble("queryDouble"), query.id("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").NE(101.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDouble("queryDouble"), query.id("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").NE(100.5));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aDouble("queryDouble"), query.id("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").NE("101.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aDouble("queryDouble"), query.id("objectId"));
      vQ.WHERE(query.aDouble("queryDouble").NE("100.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aFloat("queryFloat"), query.id("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").EQ((float) 100.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aFloat("queryFloat"), query.id("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").EQ((float) 101.5));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aFloat("queryFloat"), query.id("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").EQ("100.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aFloat("queryFloat"), query.id("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").EQ("101.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aFloat("queryFloat"), query.id("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").GT((float) 10.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aFloat("queryFloat"), query.id("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").GT((float) 121.5));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aFloat("queryFloat"), query.id("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").GT("10.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aFloat("queryFloat"), query.id("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").GT("121.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aFloat("queryFloat"), query.id("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").GE((float) 100.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aFloat("queryFloat"), query.id("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").GE((float) 11.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aFloat("queryFloat"), query.id("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").GE((float) 121.5));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aFloat("queryFloat"), query.id("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").GE("100.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aFloat("queryFloat"), query.id("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").GE("11.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aFloat("queryFloat"), query.id("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").GE("121.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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

    String origValue = QueryMasterSetup.testQueryObject1.getValue("queryFloat");
    QueryMasterSetup.testQueryObject1.setValue("queryFloat", "");
    QueryMasterSetup.testQueryObject1.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aFloat("queryFloat"), query.id("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      int count = 0;

      while (i.hasNext())
      {
        assertTrue(i.next().getValue("queryFloat").equals(""));

        count++;
      }

      int expected = 0;

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        if (testQueryObject.getValue("queryFloat").equals(""))
          expected++;
      }

      assertEquals(expected, count);

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aFloat("queryFloat"), query.id("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").NE(""));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.testQueryObject1.setValue("queryFloat", origValue);
      QueryMasterSetup.testQueryObject1.apply();

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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aFloat("queryFloat"), query.id("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").LT((float) 120.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aFloat("queryFloat"), query.id("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").LT((float) 11.5));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aFloat("queryFloat"), query.id("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").LT("120.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aFloat("queryFloat"), query.id("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").LT("11.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aFloat("queryFloat"), query.id("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").LE((float) 100.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on less than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aFloat("queryFloat"), query.id("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").LE((float) 101.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aFloat("queryFloat"), query.id("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").LE((float) 11.5));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aFloat("queryFloat"), query.id("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").LE("100.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on less than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aFloat("queryFloat"), query.id("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").LE("101.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aFloat("queryFloat"), query.id("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").LE("11.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aFloat("queryFloat"), query.id("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").NE((float) 110.5));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aFloat("queryFloat"), query.id("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").NE((float) 100.5));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aFloat("queryFloat"), query.id("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").NE("110.5"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aFloat("queryFloat"), query.id("objectId"));
      vQ.WHERE(query.aFloat("queryFloat").NE("100.5"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aInteger("queryInteger"), query.id("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").EQ(100));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aInteger("queryInteger"), query.id("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").EQ(101));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aInteger("queryInteger"), query.id("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").EQ("100"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aInteger("queryInteger"), query.id("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").EQ("101"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aInteger("queryInteger"), query.id("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").GT(10));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (Integer.parseInt(o.getValue("queryInteger")) <= 10)
        {
          fail("One of the objects returned by the query had an incorrect integer attribute.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aInteger("queryInteger"), query.id("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").GT(101));

      i = vQ.getIterator();

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (Integer.parseInt(o.getValue("queryInteger")) <= 101)
        {
          fail("One of the objects returned by the query had an incorrect integer attribute.");
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aInteger("queryInteger"), query.id("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").GT("10"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (Integer.parseInt(o.getValue("queryInteger")) <= 10)
        {
          fail("One of the objects returned by the query had an incorrect integer attribute.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aInteger("queryInteger"), query.id("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").GT("101"));

      i = vQ.getIterator();

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (Integer.parseInt(o.getValue("queryInteger")) <= 101)
        {
          fail("One of the objects returned by the query had an incorrect integer attribute.");
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aInteger("queryInteger"), query.id("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").GE(100));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (Integer.parseInt(o.getValue("queryInteger")) < 100)
        {
          fail("One of the objects returned by the query had an incorrect integer attribute.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aInteger("queryInteger"), query.id("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").GE(10));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (Integer.parseInt(o.getValue("queryInteger")) < 10)
        {
          fail("One of the objects returned by the query had an incorrect integer attribute.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aInteger("queryInteger"), query.id("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").GE(101));

      i = vQ.getIterator();

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (Integer.parseInt(o.getValue("queryInteger")) < 101)
        {
          fail("One of the objects returned by the query had an incorrect integer attribute.");
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aInteger("queryInteger"), query.id("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").GE("100"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        ValueObject o = i.next();

        if (Integer.parseInt(o.getValue("queryInteger")) < 100)
        {
          fail("One of the objects returned by the query had an incorrect integer attribute.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aInteger("queryInteger"), query.id("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").GE("10"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (Integer.parseInt(i.next().getValue("queryInteger")) < 10)
        {
          fail("One of the objects returned by the query had an incorrect integer attribute.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aInteger("queryInteger"), query.id("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").GE("101"));

      i = vQ.getIterator();

      while (i.hasNext())
      {
        if (Integer.parseInt(i.next().getValue("queryInteger")) < 101)
        {
          fail("One of the objects returned by the query had an incorrect integer attribute.");
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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

    String origValue = QueryMasterSetup.testQueryObject1.getValue("queryInteger");
    QueryMasterSetup.testQueryObject1.setValue("queryInteger", "");
    QueryMasterSetup.testQueryObject1.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aInteger("queryInteger"), query.id("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      int count = 0;

      while (i.hasNext())
      {
        assertTrue(i.next().getValue("queryInteger").equals(""));

        count++;
      }

      int expected = 0;

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        if (testQueryObject.getValue("queryInteger").equals(""))
          expected++;
      }

      assertEquals(expected, count);

      // perform another query
      vQ = qf.valueQuery();

      vQ.SELECT(query.aInteger("queryInteger"), query.id("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").NE(""));

      i = vQ.getIterator();

      count = 0;

      while (i.hasNext())
      {
        assertTrue(!i.next().getValue("queryInteger").equals(""));

        count++;
      }

      expected = 0;

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        if (!testQueryObject.getValue("queryInteger").equals(""))
          expected++;
      }

      assertEquals(expected, count);
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.testQueryObject1.setValue("queryInteger", origValue);
      QueryMasterSetup.testQueryObject1.apply();

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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aInteger("queryInteger"), query.id("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").LT(101));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aInteger("queryInteger"), query.id("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").LT(10));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aInteger("queryInteger"), query.id("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").LT("101"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aInteger("queryInteger"), query.id("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").LT("10"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aInteger("queryInteger"), query.id("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").LE(100));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aInteger("queryInteger"), query.id("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").LE(101));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aInteger("queryInteger"), query.id("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").LE(10));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aInteger("queryInteger"), query.id("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").LE("100"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aInteger("queryInteger"), query.id("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").LE("101"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aInteger("queryInteger"), query.id("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").LE("10"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aInteger("queryInteger"), query.id("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").NE(101));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (Integer.parseInt(i.next().getValue("queryInteger")) == 101)
        {
          fail("One of the objects returned by the query had an incorrect integer attribute.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aInteger("queryInteger"), query.id("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").NE(100));

      i = vQ.getIterator();

      while (i.hasNext())
      {
        if (Integer.parseInt(i.next().getValue("queryInteger")) == 100)
        {
          fail("One of the objects returned by the query had an incorrect integer attribute.");
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aInteger("queryInteger"), query.id("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").NE("101"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (Integer.parseInt(i.next().getValue("queryInteger")) == 101)
        {
          fail("One of the objects returned by the query returned an object with incorrect integer attributes.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aInteger("queryInteger"), query.id("objectId"));
      vQ.WHERE(query.aInteger("queryInteger").NE("100"));

      i = vQ.getIterator();

      while (i.hasNext())
      {
        if (Integer.parseInt(i.next().getValue("queryInteger")) == 100)
        {
          fail("One of the objects returned by the query had an incorrect integer attribute.");
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
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
      
      assertEquals(replaced.getSQL(), original.getSQL());
      assertEquals(vQ.getSelectableRefs().size(), 1);
      assertEquals(vQ.getIterator().next().getValue("anInt"), "2");
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aLong("queryLong"), query.id("objectId"));
      vQ.WHERE(query.aLong("queryLong").EQ((long) 100));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aLong("queryLong"), query.id("objectId"));
      vQ.WHERE(query.aLong("queryLong").EQ((long) 101));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aLong("queryLong"), query.id("objectId"));
      vQ.WHERE(query.aLong("queryLong").EQ("100"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aLong("queryLong"), query.id("objectId"));
      vQ.WHERE(query.aLong("queryLong").EQ("101"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aLong("queryLong"), query.id("objectId"));
      vQ.WHERE(query.aLong("queryLong").GT((long) 10));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aLong("queryLong"), query.id("objectId"));
      vQ.WHERE(query.aLong("queryLong").GT((long) 101));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aLong("queryLong"), query.id("objectId"));
      vQ.WHERE(query.aLong("queryLong").GT("10"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aLong("queryLong"), query.id("objectId"));
      vQ.WHERE(query.aLong("queryLong").GT("101"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aLong("queryLong"), query.id("objectId"));
      vQ.WHERE(query.aLong("queryLong").GE((long) 100));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aLong("queryLong"), query.id("objectId"));
      vQ.WHERE(query.aLong("queryLong").GE((long) 10));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aLong("queryLong"), query.id("objectId"));
      vQ.WHERE(query.aLong("queryLong").GE((long) 101));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aLong("queryLong"), query.id("objectId"));
      vQ.WHERE(query.aLong("queryLong").GE("100"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aLong("queryLong"), query.id("objectId"));
      vQ.WHERE(query.aLong("queryLong").GE("10"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aLong("queryLong"), query.id("objectId"));
      vQ.WHERE(query.aLong("queryLong").GE("101"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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

    String origValue = QueryMasterSetup.testQueryObject1.getValue("queryLong");
    QueryMasterSetup.testQueryObject1.setValue("queryLong", "");
    QueryMasterSetup.testQueryObject1.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aLong("queryLong"), query.id("objectId"));
      vQ.WHERE(query.aLong("queryLong").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      int count = 0;

      while (i.hasNext())
      {
        assertTrue(i.next().getValue("queryLong").equals(""));

        count++;
      }

      int expected = 0;

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        if (testQueryObject.getValue("queryLong").equals(""))
          expected++;
      }

      assertEquals(expected, count);

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aLong("queryLong"), query.id("objectId"));
      vQ.WHERE(query.aLong("queryLong").NE(""));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        QueryMasterSetup.testQueryObject1.setValue("queryLong", origValue);
        QueryMasterSetup.testQueryObject1.apply();

        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.testQueryObject1.setValue("queryLong", origValue);
      QueryMasterSetup.testQueryObject1.apply();

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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aLong("queryLong"), query.id("objectId"));
      vQ.WHERE(query.aLong("queryLong").LT((long) 120));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aLong("queryLong"), query.id("objectId"));
      vQ.WHERE(query.aLong("queryLong").LT((long) 10));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aLong("queryLong"), query.id("objectId"));
      vQ.WHERE(query.aLong("queryLong").LT("120"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aLong("queryLong"), query.id("objectId"));
      vQ.WHERE(query.aLong("queryLong").LT("10"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aLong("queryLong"), query.id("objectId"));
      vQ.WHERE(query.aLong("queryLong").LE((long) 100));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aLong("queryLong"), query.id("objectId"));
      vQ.WHERE(query.aLong("queryLong").LE((long) 120));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aLong("queryLong"), query.id("objectId"));
      vQ.WHERE(query.aLong("queryLong").LE((long) 10));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aLong("queryLong"), query.id("objectId"));
      vQ.WHERE(query.aLong("queryLong").LE("100"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aLong("queryLong"), query.id("objectId"));
      vQ.WHERE(query.aLong("queryLong").LE("120"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aLong("queryLong"), query.id("objectId"));
      vQ.WHERE(query.aLong("queryLong").LE("10"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aLong("queryLong"), query.id("objectId"));
      vQ.WHERE(query.aLong("queryLong").NE((long) 10));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aLong("queryLong"), query.id("objectId"));
      vQ.WHERE(query.aLong("queryLong").NE((long) 100));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aLong("queryLong"), query.id("objectId"));
      vQ.WHERE(query.aLong("queryLong").NE("10"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aLong("queryLong"), query.id("objectId"));
      vQ.WHERE(query.aLong("queryLong").NE("100"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aText("queryText"), query.id("objectId"));
      vQ.WHERE(query.aText("queryText").EQ("some text value"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aText("queryText"), query.id("objectId"));
      vQ.WHERE(query.aText("queryText").EQ("wrong text value"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aText("queryText"), query.id("objectId"));
      vQ.WHERE(query.aText("queryText").EQi("SOME TEXT VALUE"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aText("queryText"), query.id("objectId"));
      vQ.WHERE(query.aText("queryText").EQi("WRONG TEXT VALUE"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aText("queryText"), query.id("objectId"));
      vQ.WHERE(query.aText("queryText").IN("SOME text value", "some text value", "wrong text value"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aText("queryText"), query.id("objectId"));
      vQ.WHERE(query.aText("queryText").IN("SOME text value", "SOME TEXT value", "SOME TEXT VALUE"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aText("queryText"), query.id("objectId"));
      vQ.WHERE(query.aText("queryText").INi("WRONG TEXT VALUE", "SOME TEXT VALUE", "wrong TEXT value 2"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aText("queryText"), query.id("objectId"));
      vQ.WHERE(query.aText("queryText").INi("WRONG TEXT VALUE", "WRONG TEXT VALUE 2", "WRONG TEXT VALUE 3"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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

    String origValue = QueryMasterSetup.testQueryObject1.getValue("queryText");
    QueryMasterSetup.testQueryObject1.setValue("queryText", "");
    QueryMasterSetup.testQueryObject1.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aText("queryText"), query.id("objectId"));
      vQ.WHERE(query.aText("queryText").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      int count = 0;

      while (i.hasNext())
      {
        assertTrue(i.next().getValue("queryText").equals(""));

        count++;
      }

      int expected = 0;

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        if (testQueryObject.getValue("queryText").equals(""))
          expected++;
      }

      assertEquals(expected, count);

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aText("queryText"), query.id("objectId"));
      vQ.WHERE(query.aText("queryText").NE(""));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.testQueryObject1.setValue("queryText", origValue);
      QueryMasterSetup.testQueryObject1.apply();

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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aText("queryText"), query.id("objectId"));
      vQ.WHERE(query.aText("queryText").LIKE("%text%"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aText("queryText"), query.id("objectId"));
      vQ.WHERE(query.aText("queryText").LIKE("%text"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aText("queryText"), query.id("objectId"));
      vQ.WHERE(query.aText("queryText").LIKEi("%TEXT%"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aText("queryText"), query.id("objectId"));
      vQ.WHERE(query.aText("queryText").LIKEi("%TEXT"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aText("queryText"), query.id("objectId"));
      vQ.WHERE(query.aText("queryText").NE("wrong text value"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aText("queryText"), query.id("objectId"));
      vQ.WHERE(query.aText("queryText").NE("some text value"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aText("queryText"), query.id("objectId"));
      vQ.WHERE(query.aText("queryText").NEi("WRONG TEXT VALUE"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aText("queryText"), query.id("objectId"));
      vQ.WHERE(query.aText("queryText").NEi("SOME TEXT VALUE"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aText("queryText"), query.id("objectId"));
      vQ.WHERE(query.aText("queryText").NI("SOME text value", "SOME TEXT value", "SOME TEXT VALUE"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aText("queryText"), query.id("objectId"));
      vQ.WHERE(query.aText("queryText").NI("SOME text value", "some text value", "wrong text value"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aText("queryText"), query.id("objectId"));
      vQ.WHERE(query.aText("queryText").NIi("WRONG text value", "WRONG TEXT value 2", "WRONG TEXT VALUE 3"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aText("queryText"), query.id("objectId"));
      vQ.WHERE(query.aText("queryText").NIi("SOME text value", "SOME TEXT VALUE", "SOME TEXT value"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aText("queryText"), query.id("objectId"));
      vQ.WHERE(query.aText("queryText").NLIKEi("%WRONG%"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aText("queryText"), query.id("objectId"));
      vQ.WHERE(query.aText("queryText").NLIKEi("%TEXT%"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
    Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aTime("queryTime"), query.id("objectId"));
      vQ.WHERE(query.aTime("queryTime").EQ(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aTime("queryTime"), query.id("objectId"));
      vQ.WHERE(query.aTime("queryTime").EQ(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aTime("queryTime"), query.id("objectId"));
      vQ.WHERE(query.aTime("queryTime").EQ("13:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aTime("queryTime"), query.id("objectId"));
      vQ.WHERE(query.aTime("queryTime").EQ("12:00:00"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
    Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aTime("queryTime"), query.id("objectId"));
      vQ.WHERE(query.aTime("queryTime").GT(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aTime("queryTime"), query.id("objectId"));
      vQ.WHERE(query.aTime("queryTime").GT(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aTime("queryTime"), query.id("objectId"));
      vQ.WHERE(query.aTime("queryTime").GT("12:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aTime("queryTime"), query.id("objectId"));
      vQ.WHERE(query.aTime("queryTime").GT("14:00:00"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
    Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aTime("queryTime"), query.id("objectId"));
      vQ.WHERE(query.aTime("queryTime").GE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aTime("queryTime"), query.id("objectId"));
      vQ.WHERE(query.aTime("queryTime").GE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aTime("queryTime"), query.id("objectId"));
      vQ.WHERE(query.aTime("queryTime").GE(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aTime("queryTime"), query.id("objectId"));
      vQ.WHERE(query.aTime("queryTime").GE("13:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aTime("queryTime"), query.id("objectId"));
      vQ.WHERE(query.aTime("queryTime").GE("12:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aTime("queryTime"), query.id("objectId"));
      vQ.WHERE(query.aTime("queryTime").GE("14:00:00"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
    String origValue = QueryMasterSetup.testQueryObject1.getValue("queryTime");
    QueryMasterSetup.testQueryObject1.setValue("queryTime", "");
    QueryMasterSetup.testQueryObject1.apply();

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aTime("queryTime"), query.id("objectId"));
      vQ.WHERE(query.aTime("queryTime").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      int count = 0;

      while (i.hasNext())
      {
        assertTrue(i.next().getValue("queryTime").equals(""));

        count++;
      }

      int expected = 0;

      for (String id : objectList)
      {
        BusinessDAO testQueryObject = BusinessDAO.get(id).getBusinessDAO();

        if (testQueryObject.getValue("queryTime").equals(""))
          expected++;
      }

      assertEquals(expected, count);

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aTime("queryTime"), query.id("objectId"));
      vQ.WHERE(query.aTime("queryTime").NE(""));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.testQueryObject1.setValue("queryTime", origValue);
      QueryMasterSetup.testQueryObject1.apply();

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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aTime("queryTime"), query.id("objectId"));
      vQ.WHERE(query.aTime("queryTime").LT(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("10:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aTime("queryTime"), query.id("objectId"));
      vQ.WHERE(query.aTime("queryTime").LT(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aTime("queryTime"), query.id("objectId"));
      vQ.WHERE(query.aTime("queryTime").LT("14:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aTime("queryTime"), query.id("objectId"));
      vQ.WHERE(query.aTime("queryTime").LT("10:00:00"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
    Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aTime("queryTime"), query.id("objectId"));
      vQ.WHERE(query.aTime("queryTime").LE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aTime("queryTime"), query.id("objectId"));
      vQ.WHERE(query.aTime("queryTime").LE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aTime("queryTime"), query.id("objectId"));
      vQ.WHERE(query.aTime("queryTime").LE(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aTime("queryTime"), query.id("objectId"));
      vQ.WHERE(query.aTime("queryTime").LE("13:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();

      vQ.SELECT(query.aTime("queryTime"), query.id("objectId"));
      vQ.WHERE(query.aTime("queryTime").LE("14:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();

      vQ.SELECT(query.aTime("queryTime"), query.id("objectId"));
      vQ.WHERE(query.aTime("queryTime").LE("12:00:00"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
    Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aTime("queryTime"), query.id("objectId"));
      vQ.WHERE(query.aTime("queryTime").NE(date));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      vQ = qf.valueQuery();

      vQ.SELECT(query.aTime("queryTime"), query.id("objectId"));
      vQ.WHERE(query.aTime("queryTime").NE(date));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
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
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      vQ.SELECT(query.aTime("queryTime"), query.id("objectId"));
      vQ.WHERE(query.aTime("queryTime").NE("12:00:00"));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 0 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aTime("queryTime"), query.id("objectId"));
      vQ.WHERE(query.aTime("queryTime").NE("13:00:00"));

      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testInvalidSubSelectInSelectClause()
  {
    QueryFactory queryFactory = new QueryFactory();

    BusinessDAOQuery bq = queryFactory.businessDAOQuery(MdBusinessInfo.CLASS);

    ValueQuery valueQuery1 = new ValueQuery(queryFactory);
    ValueQuery valueQuery2 = new ValueQuery(queryFactory);

    valueQuery1.SELECT(bq.aCharacter(MdBusinessInfo.ID));

    valueQuery2.SELECT(bq.aCharacter(MdBusinessInfo.NAME), valueQuery1.getSubSelect());

    String failMsg = "Subselct contained a query that returned more than one row, yet the proper excepition was not thrown.";

    try
    {
      valueQuery2.getIterator();
      fail(failMsg);
    }
    catch (SubSelectReturnedMultipleRowsException e)
    {
      // this is expected
    }
    catch (Exception e)
    {
      fail(failMsg);
    }
  }

  public void testValidSubSelectInSelectClause()
  {
    QueryFactory queryFactory = new QueryFactory();

    BusinessDAOQuery bq = queryFactory.businessDAOQuery(MdBusinessInfo.CLASS);

    ValueQuery valueQuery1 = new ValueQuery(queryFactory);
    ValueQuery valueQuery2 = new ValueQuery(queryFactory);

    valueQuery1.SELECT(F.COUNT(bq.aCharacter(MdBusinessInfo.ID)));

    valueQuery2.SELECT(bq.aCharacter(MdBusinessInfo.NAME), valueQuery1.getSubSelect());

    OIterator<ValueObject> i = null;

    try
    {
      i = valueQuery2.getIterator();
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
      {
        i.close();
      }
    }
  }

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
      fail("Able to query for objects using an EntityQuery object that was defined for value queries. ");
    }
    catch (QueryException e)
    {
      // this is expected
    }
  }

  public void testFalseContainsSQLSelectable()
  {
    QueryFactory qf = new QueryFactory();

    ValueQuery vQ = qf.valueQuery();
    BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

    vQ.SELECT(query.aBoolean("queryBoolean"));

    assertFalse(vQ.containsSelectableSQL());
  }

  public void testContainsSQLSelectableInSelectStatement()
  {
    QueryFactory qf = new QueryFactory();

    ValueQuery vQ = qf.valueQuery();
    BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

    vQ.SELECT(query.aBoolean("queryBoolean"));
    vQ.SELECT(vQ.aSQLCharacter("testSelectable", "'A'"));

    assertTrue(vQ.containsSelectableSQL());
  }

  public void testContainsSQLSelectableInWhere()
  {
    QueryFactory qf = new QueryFactory();

    ValueQuery vQ = qf.valueQuery();
    BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

    vQ.SELECT(query.aBoolean("queryBoolean"));
    vQ.WHERE(query.aBoolean("queryBoolean").EQ(vQ.aSQLBoolean(TestFixConst.ATTRIBUTE_BOOLEAN, "1")));

    assertTrue(vQ.containsSelectableSQL());
  }

//  public void testContainsSQLSelectableInJoin()
//  {
//    QueryFactory qf = new QueryFactory();
//
//    ValueQuery vQ = qf.valueQuery();
//    ValueQuery vQ2 = qf.valueQuery();
//    BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.selectedMdBusiness.getType());
//    BusinessDAOQuery refQuery = qf.businessDAOQuery(QueryMasterSetup.childRefMdBusiness.getType());
//
//    AttributeCharacter attribute = refQuery.aCharacter("id");
//
//    vQ.SELECT(query.aCharacter("id"));
//    vQ2.SELECT(attribute);
//    vQ.WHERE(query.aReference("reference").LEFT_JOIN_EQ(vQ2.aSQLCharacter("left_join", vQ2.getTableAlias() + "." + attribute._getAttributeName())));
//
//    assertTrue(vQ.containsSelectableSQL());
//  }

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
      assertEquals("getCount() method returned a different result than the query iterator.", iteratorCount, sqlCount);
    }
  }

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
      assertEquals("getCount() method returned a different result than the query iterator.", iteratorCount, sqlCount);
    }
  }

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
      assertEquals("getCount() method returned a different result than the query iterator.", iteratorCount, sqlCount);
    }
  }
  
  /*// FIXME find default MySQL equivalent
  public void testCountSelectable()
  {
    long total = 100;
    QueryFactory f = new QueryFactory();
    ValueQuery v = new ValueQuery(f);
    
    String gen = "gen";
    String ct = "ct";
    SelectableSQLInteger s = v.aSQLInteger(gen, gen, gen);
    SelectableSQLLong c = v.aSQLLong(ct, "count(*) over()", ct);
    v.SELECT(s, c);
    
    v.setCountSelectable(c);
    
    v.FROM("generate_series(1,"+total+")", gen);
    
    // restrict the rows. This not only gives us one result, which is all we need,
    // but it shows that the window count isn't affected by LIMIT
    v.restrictRows(1, 1);
    
    ValueObject o = v.getIterator().getAll().get(0);
    assertEquals(Long.parseLong(o.getValue(ct)), total);
    assertEquals(v.getCount(), total);
  }
  
  public void testAggregateCountSelectable()
  {
    long total = 100;
    
    QueryFactory f = new QueryFactory();
    ValueQuery v = new ValueQuery(f);
    
    String gen = "gen";
    String ct = "ct";
    SelectableSQLInteger s = v.aSQLInteger(gen, gen, gen);
    SelectableSQLLong c = v.aSQLAggregateLong(ct, "count(*) over()", ct);
    v.SELECT(s, c);
    
    v.setCountSelectable(c);
    
    v.FROM("generate_series(1,"+total+")", gen);
    v.GROUP_BY(s);
    
    // restrict the rows. This not only gives us one result, which is all we need,
    // but it shows that the window count isn't affected by LIMIT
    v.restrictRows(1, 1);
    
    ValueObject o = v.getIterator().getAll().get(0);
    assertEquals(Long.parseLong(o.getValue(ct)), total);
    assertEquals(v.getCount(), total);
  }
  */

}
