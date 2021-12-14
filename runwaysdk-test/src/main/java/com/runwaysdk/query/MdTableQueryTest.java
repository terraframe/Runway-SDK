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

import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeBooleanUtil;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdTableInfo;
import com.runwaysdk.constants.TypeInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityMasterTestSetup;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateDAOIF;
import com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdTableDAO;
import com.runwaysdk.session.Request;

public class MdTableQueryTest
{
  private static List<String>     testObjectIdList       = new LinkedList<String>();

  private static List<String>     objectList;

  protected static final TypeInfo childTableQueryInfo    = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "ChildQueryTable");

  protected static final TypeInfo childRefTableQueryInfo = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "ChildRefQueryTable");

  /**
   */
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

    MdBusinessDAOIF mdBusChildType = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childQueryInfo.getType());
    MdBusinessDAOIF mdBusChildRefType = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryInfo.getType());

    // Table pointing to child class
    MdTableDAO mdTableChild = MdTableDAO.newInstance();
    mdTableChild.setValue(MdTableInfo.NAME, childTableQueryInfo.getTypeName());
    mdTableChild.setValue(MdTableInfo.PACKAGE, childTableQueryInfo.getPackageName());
    mdTableChild.setStructValue(MdTableInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Child Test Table");
    mdTableChild.setValue(MdTableInfo.TABLE_NAME, mdBusChildType.getTableName());
    mdTableChild.setValue(MdTableInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTableChild.apply();

    // OID field
    MdAttributeCharacterDAO mdTableChildOidAttr = TestFixtureFactory.addCharacterAttribute(mdTableChild, "childObjId");
    mdTableChildOidAttr.setValue(MdAttributeCharacterInfo.COLUMN_NAME, ComponentInfo.OID);
    mdTableChildOidAttr.apply();

    MdAttributeCharacterDAOIF mdClassQueryCharAttr = (MdAttributeCharacterDAOIF) mdBusChildType.definesAttribute("queryCharacter");
    MdAttributeCharacterDAO mdTableQueryCharAttr = TestFixtureFactory.addCharacterAttribute(mdTableChild, mdClassQueryCharAttr.definesAttribute());
    mdTableQueryCharAttr.setValue(MdAttributeCharacterInfo.COLUMN_NAME, mdClassQueryCharAttr.getColumnName());
    mdTableQueryCharAttr.apply();

    MdAttributeBooleanDAOIF mdClassBoolAttr = (MdAttributeBooleanDAOIF) mdBusChildType.definesAttribute("queryBoolean");
    MdAttributeBooleanDAO mdTableChildBoolAttr = TestFixtureFactory.addBooleanAttribute(mdTableChild, mdClassBoolAttr.definesAttribute());
    mdTableChildBoolAttr.setValue(MdAttributeCharacterInfo.COLUMN_NAME, mdClassBoolAttr.getColumnName());
    mdTableChildBoolAttr.apply();

    MdAttributeIntegerDAOIF mdClassIntAttr = (MdAttributeIntegerDAOIF) mdBusChildType.definesAttribute("queryInteger");
    MdAttributeIntegerDAO mdTableChildIntAttr = TestFixtureFactory.addIntegerAttribute(mdTableChild, mdClassIntAttr.definesAttribute());
    mdTableChildIntAttr.setValue(MdAttributeIntegerInfo.COLUMN_NAME, mdClassIntAttr.getColumnName());
    mdTableChildIntAttr.apply();

    MdAttributeDateDAOIF mdClassDateAttr = (MdAttributeDateDAOIF) mdBusChildType.definesAttribute("queryDate");
    MdAttributeDateDAO mdTableChildDateAttr = TestFixtureFactory.addDateAttribute(mdTableChild, mdClassDateAttr.definesAttribute());
    mdTableChildDateAttr.setValue(MdAttributeDateInfo.COLUMN_NAME, mdClassDateAttr.getColumnName());
    mdTableChildDateAttr.apply();

    MdAttributeReferenceDAOIF mdClassRefAttr = (MdAttributeReferenceDAOIF) mdBusChildType.definesAttribute("reference");
    MdAttributeReferenceDAO mdTableChildRefAttr = TestFixtureFactory.addReferenceAttribute(mdTableChild, mdBusChildRefType, mdClassRefAttr.definesAttribute());
    mdTableChildRefAttr.setValue(MdAttributeReferenceInfo.COLUMN_NAME, mdClassRefAttr.getColumnName());
    mdTableChildRefAttr.apply();

    // Table pointing to child reference class
    MdTableDAO mdTableChildRef = MdTableDAO.newInstance();
    mdTableChildRef.setValue(MdTableInfo.NAME, childRefTableQueryInfo.getTypeName());
    mdTableChildRef.setValue(MdTableInfo.PACKAGE, childRefTableQueryInfo.getPackageName());
    mdTableChildRef.setStructValue(MdTableInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Child Referene Test Table");
    mdTableChildRef.setValue(MdTableInfo.TABLE_NAME, mdBusChildRefType.getTableName());
    mdTableChildRef.setValue(MdTableInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTableChildRef.apply();

    MdAttributeCharacterDAO mdTableChildRefIdAttr = TestFixtureFactory.addCharacterAttribute(mdTableChildRef, "childRefObjId");
    mdTableChildRefIdAttr.setValue(MdAttributeCharacterInfo.COLUMN_NAME, ComponentInfo.OID);
    mdTableChildRefIdAttr.apply();

    MdAttributeCharacterDAOIF mdClassRefQueryCharAttr = (MdAttributeCharacterDAOIF) mdBusChildRefType.definesAttribute("refQueryCharacter");
    MdAttributeCharacterDAO mdTableChildRefQueryCharAttr = TestFixtureFactory.addCharacterAttribute(mdTableChildRef, mdClassRefQueryCharAttr.definesAttribute());
    mdTableChildRefQueryCharAttr.setValue(MdAttributeCharacterInfo.COLUMN_NAME, mdClassRefQueryCharAttr.getColumnName());
    mdTableChildRefQueryCharAttr.apply();

    MdAttributeIntegerDAOIF mdClassRefIntAttr = (MdAttributeIntegerDAOIF) mdBusChildRefType.definesAttribute("refQueryInteger");
    MdAttributeIntegerDAO mdTableChildRefIntAttr = TestFixtureFactory.addIntegerAttribute(mdTableChildRef, mdClassRefIntAttr.definesAttribute());
    mdTableChildRefIntAttr.setValue(MdAttributeIntegerInfo.COLUMN_NAME, mdClassRefIntAttr.getColumnName());
    mdTableChildRefIntAttr.apply();

  }

  /**
   * Deletes the abstract relationship.
   */
  @Request
  @AfterClass
  public static void classTearDown()
  {
    // MdBusinessDAO testClassMdBusiness1 =
    // MdBusinessDAO.getMdBusinessDAO(TestFixConst.TEST_CLASS1_TYPE).getBusinessDAO();
    // testClassMdBusiness1.delete();
    //
    MdTableDAO mdTableChild = MdTableDAO.getMdTableDAO(childTableQueryInfo.getType()).getBusinessDAO();
    mdTableChild.delete();

    MdTableDAO mdTableChildRef = MdTableDAO.getMdTableDAO(childRefTableQueryInfo.getType()).getBusinessDAO();
    mdTableChildRef.delete();

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
    MdBusinessDAOIF mdBusMdAttrConcrete = MdBusinessDAO.getMdBusinessDAO(MdAttributeConcreteInfo.CLASS);

    MdTableDAO mdTabledAttrConcrete = MdTableDAO.newInstance();

    OIterator<ValueObject> i = null;

    try
    {
      mdTabledAttrConcrete.setValue(MdTableInfo.NAME, "MdTableAttributeConcrete");
      mdTabledAttrConcrete.setValue(MdTableInfo.PACKAGE, childRefTableQueryInfo.getPackageName());
      mdTabledAttrConcrete.setStructValue(MdTableInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MdTableAttributeConcrete");
      mdTabledAttrConcrete.setValue(MdTableInfo.TABLE_NAME, mdBusMdAttrConcrete.getTableName());
      mdTabledAttrConcrete.setValue(MdTableInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdTabledAttrConcrete.apply();

      MdAttributeCharacterDAOIF mdBusMdAttrConcreteColname = (MdAttributeCharacterDAOIF) mdBusMdAttrConcrete.definesAttribute(MdAttributeConcreteInfo.NAME);
      MdAttributeCharacterDAO mdTableMdAttrConcreteColname = TestFixtureFactory.addCharacterAttribute(mdTabledAttrConcrete, mdBusMdAttrConcreteColname.definesAttribute());
      mdTableMdAttrConcreteColname.setValue(MdAttributeCharacterInfo.COLUMN_NAME, mdBusMdAttrConcreteColname.getColumnName());
      mdTableMdAttrConcreteColname.apply();

      MdAttributeBooleanDAOIF mdBusMdAttrConcreteReq = (MdAttributeBooleanDAOIF) mdBusMdAttrConcrete.definesAttribute(MdAttributeConcreteInfo.REQUIRED);
      MdAttributeBooleanDAO mdTableChildBoolAttr = TestFixtureFactory.addBooleanAttribute(mdTabledAttrConcrete, mdBusMdAttrConcreteReq.definesAttribute());
      mdTableChildBoolAttr.setValue(MdAttributeBooleanInfo.COLUMN_NAME, mdBusMdAttrConcreteReq.getColumnName());
      mdTableChildBoolAttr.apply();

      QueryFactory qf = new QueryFactory();

      // Query number of OID fields

      ValueQuery vQ1 = qf.valueQuery();
      TableQuery mdAttrQ = qf.tableQuery(mdTabledAttrConcrete.definesType());

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

      vQ2.SELECT_DISTINCT(F.COUNT(mdAttrQ.get(MdAttributeConcreteInfo.NAME), "count"), F.STRING_AGG(mdAttrQ.get(MdAttributeConcreteInfo.NAME), ", ", "STRING_AGG").OVER(F.PARTITION_BY(F.COUNT(mdAttrQ.get(MdAttributeConcreteInfo.NAME)), mdAttrQ.get(MdAttributeConcreteInfo.REQUIRED))), mdAttrQ.RANK(mdAttrQ.get(MdAttributeConcreteInfo.NAME)).OVER(F.PARTITION_BY(mdAttrQ.get(MdAttributeConcreteInfo.REQUIRED)), new OrderBy(F.COUNT(mdAttrQ.get(MdAttributeConcreteInfo.NAME)), OrderBy.SortOrder.DESC)), mdAttrQ.get(MdAttributeConcreteInfo.REQUIRED));
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

      if (mdTabledAttrConcrete != null && !mdTabledAttrConcrete.isNew())
      {
        mdTabledAttrConcrete.delete();
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
    MdBusinessDAOIF mdBusMdAttrConcrete = MdBusinessDAO.getMdBusinessDAO(MdAttributeConcreteInfo.CLASS);

    MdTableDAO mdTabledAttrConcrete = MdTableDAO.newInstance();

    OIterator<ValueObject> i = null;

    try
    {
      mdTabledAttrConcrete.setValue(MdTableInfo.NAME, "MdTableAttributeConcrete");
      mdTabledAttrConcrete.setValue(MdTableInfo.PACKAGE, childRefTableQueryInfo.getPackageName());
      mdTabledAttrConcrete.setStructValue(MdTableInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MdTableAttributeConcrete");
      mdTabledAttrConcrete.setValue(MdTableInfo.TABLE_NAME, mdBusMdAttrConcrete.getTableName());
      mdTabledAttrConcrete.setValue(MdTableInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdTabledAttrConcrete.apply();

      MdAttributeCharacterDAOIF mdBusMdAttrConcreteColname = (MdAttributeCharacterDAOIF) mdBusMdAttrConcrete.definesAttribute(MdAttributeConcreteInfo.NAME);
      MdAttributeCharacterDAO mdTableMdAttrConcreteColname = TestFixtureFactory.addCharacterAttribute(mdTabledAttrConcrete, mdBusMdAttrConcreteColname.definesAttribute());
      mdTableMdAttrConcreteColname.setValue(MdAttributeCharacterInfo.COLUMN_NAME, mdBusMdAttrConcreteColname.getColumnName());
      mdTableMdAttrConcreteColname.apply();

      MdAttributeBooleanDAOIF mdBusMdAttrConcreteReq = (MdAttributeBooleanDAOIF) mdBusMdAttrConcrete.definesAttribute(MdAttributeConcreteInfo.REQUIRED);
      MdAttributeBooleanDAO mdTableChildBoolAttr = TestFixtureFactory.addBooleanAttribute(mdTabledAttrConcrete, mdBusMdAttrConcreteReq.definesAttribute());
      mdTableChildBoolAttr.setValue(MdAttributeBooleanInfo.COLUMN_NAME, mdBusMdAttrConcreteReq.getColumnName());
      mdTableChildBoolAttr.apply();

      QueryFactory qf = new QueryFactory();

      // Query number of OID fields

      ValueQuery vQ1 = qf.valueQuery();
      TableQuery mdAttrQ = qf.tableQuery(mdTabledAttrConcrete.definesType());

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

      if (mdTabledAttrConcrete != null && !mdTabledAttrConcrete.isNew())
      {
        mdTabledAttrConcrete.delete();
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
      TableQuery childRefQuery = qf.tableQuery(childRefTableQueryInfo.getType());
      TableQuery childQuery = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(childQuery.aCharacter("queryCharacter"), childQuery.get("childObjId"));
      vQ.WHERE(childQuery.aCharacter("queryCharacter").NE(""));

      vQ2.SELECT(childRefQuery.aCharacter("refQueryCharacter"), childRefQuery.get("childRefObjId"));
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
      TableQuery childQuery1 = qf.tableQuery(childTableQueryInfo.getType());
      TableQuery childQuery2 = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(childQuery1.get("childObjId"));
      vQ2.SELECT(childQuery2.get("childObjId"));

      vQ3.UNION(vQ, vQ2);

      long expected = 2 * vQ3.getCount();

      // UNION ALL
      qf = new QueryFactory();

      vQ = qf.valueQuery();
      vQ2 = qf.valueQuery();
      vQ3 = qf.valueQuery();
      childQuery1 = qf.tableQuery(childTableQueryInfo.getType());
      childQuery2 = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(childQuery1.get("childObjId"));
      vQ2.SELECT(childQuery2.get("childObjId"));

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

  /**
   * The UNION ALL operator does not exclude duplicate rows so we test this
   * through the API by UNION ALL a table with itself and ensuring there's twice
   * the rows of a normal UNION
   */
  @Request
  @Test
  public void testUnionAll_Generated()
  {
    try
    {
      // UNION
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();
      ValueQuery vQ2 = qf.valueQuery();
      ValueQuery vQ3 = qf.valueQuery();
      GenericTableQuery childQuery1 = new GenericTableQuery(MdTableDAO.getMdTableDAO(childTableQueryInfo.getType()), qf);
      GenericTableQuery childQuery2 = new GenericTableQuery(MdTableDAO.getMdTableDAO(childTableQueryInfo.getType()), qf);

      vQ.SELECT(childQuery1.get("childObjId"));
      vQ2.SELECT(childQuery2.get("childObjId"));

      vQ3.UNION(vQ, vQ2);

      long expected = 2 * vQ3.getCount();

      // UNION ALL
      qf = new QueryFactory();

      vQ = qf.valueQuery();
      vQ2 = qf.valueQuery();
      vQ3 = qf.valueQuery();
      childQuery1 = new GenericTableQuery(MdTableDAO.getMdTableDAO(childTableQueryInfo.getType()), qf);
      childQuery2 = new GenericTableQuery(MdTableDAO.getMdTableDAO(childTableQueryInfo.getType()), qf);

      vQ.SELECT(childQuery1.get("childObjId"));
      vQ2.SELECT(childQuery2.get("childObjId"));

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
      TableQuery childRefQuery = qf.tableQuery(childRefTableQueryInfo.getType());
      TableQuery childQuery = qf.tableQuery(childTableQueryInfo.getType());

      //
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
  public void testLeftJoinEqualSelectable_Generated()
  {
    OIterator<ValueObject> i = null;

    try
    {
      QueryFactory qf = new QueryFactory();

      ValueQuery vQ = qf.valueQuery();

      GenericTableQuery childRefQuery = new GenericTableQuery(MdTableDAO.getMdTableDAO(childRefTableQueryInfo.getType()), qf);
      GenericTableQuery childQuery = new GenericTableQuery(MdTableDAO.getMdTableDAO(childTableQueryInfo.getType()), qf);

      //
      vQ.SELECT(childRefQuery.get("refQueryCharacter"), childQuery.get("queryBoolean"), childQuery.get("reference"), childQuery.get("queryInteger"));
      vQ.WHERE(childQuery.get("queryInteger").LEFT_JOIN_EQ(childRefQuery.get("refQueryInteger")));

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
      TableQuery childRefQuery = qf.tableQuery(childRefTableQueryInfo.getType());
      TableQuery childQuery = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(childRefQuery.aCharacter("refQueryCharacter"), childRefQuery.aInteger("refQueryInteger"));
      vQ.WHERE(childQuery.aReference("reference").LEFT_JOIN_EQ(childRefQuery.get("childRefObjId")));

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
      TableQuery childRefQuery = qf.tableQuery(childRefTableQueryInfo.getType());
      TableQuery childQuery = qf.tableQuery(childTableQueryInfo.getType());

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
      TableQuery childRefQuery = qf.tableQuery(childRefTableQueryInfo.getType());
      TableQuery childQuery = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(childRefQuery.aCharacter("refQueryCharacter"), childRefQuery.aInteger("refQueryInteger"));
      vQ.WHERE(childQuery.aReference("reference").LEFT_JOIN_NE(childRefQuery.get("childRefObjId")));

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
      TableQuery childRefQuery = qf.tableQuery(childRefTableQueryInfo.getType());
      TableQuery childQuery = qf.tableQuery(childTableQueryInfo.getType());

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
      TableQuery childRefQuery = qf.tableQuery(childRefTableQueryInfo.getType());
      TableQuery childQuery = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(childRefQuery.aCharacter("refQueryCharacter"), childRefQuery.aInteger("refQueryInteger"));
      vQ.WHERE(childQuery.aReference("reference").LEFT_JOIN_GE(childRefQuery.get("childRefObjId")));

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
      TableQuery childRefQuery = qf.tableQuery(childRefTableQueryInfo.getType());
      TableQuery childQuery = qf.tableQuery(childTableQueryInfo.getType());

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
      TableQuery childRefQuery = qf.tableQuery(childRefTableQueryInfo.getType());
      TableQuery childQuery = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(childRefQuery.aCharacter("refQueryCharacter"), childRefQuery.aInteger("refQueryInteger"));
      vQ.WHERE(childQuery.aReference("reference").LEFT_JOIN_GT(childRefQuery.get("childRefObjId")));

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
      TableQuery childRefQuery = qf.tableQuery(childRefTableQueryInfo.getType());
      TableQuery childQuery = qf.tableQuery(childTableQueryInfo.getType());

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
      TableQuery childRefQuery = qf.tableQuery(childRefTableQueryInfo.getType());
      TableQuery childQuery = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(childRefQuery.aCharacter("refQueryCharacter"), childRefQuery.aInteger("refQueryInteger"));
      vQ.WHERE(childQuery.aReference("reference").LEFT_JOIN_LE(childRefQuery.get("childRefObjId")));

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
      TableQuery childRefQuery = qf.tableQuery(childRefTableQueryInfo.getType());
      TableQuery childQuery = qf.tableQuery(childTableQueryInfo.getType());

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
      TableQuery childRefQuery = qf.tableQuery(childRefTableQueryInfo.getType());
      TableQuery childQuery = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(childRefQuery.aCharacter("refQueryCharacter"), childRefQuery.aInteger("refQueryInteger"));
      vQ.WHERE(childQuery.aReference("reference").LEFT_JOIN_LT(childRefQuery.get("childRefObjId")));

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

  @SuppressWarnings("resource")
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
      TableQuery query = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(query.aBoolean("queryBoolean"), query.get("childObjId", "objectId"));
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

      vQ.SELECT(query.aBoolean("queryBoolean"), query.get("childObjId", "objectId"));
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

  @SuppressWarnings("resource")
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
      TableQuery query = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(query.aBoolean("queryBoolean"), query.get("childObjId", "objectId"));
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

      vQ.SELECT(query.aBoolean("queryBoolean"), query.get("childObjId", "objectId"));
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

  @SuppressWarnings("resource")
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
      TableQuery query = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(query.aBoolean("queryBoolean"), query.get("childObjId", "objectId"));
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

      vQ.SELECT(query.aBoolean("queryBoolean"), query.get("childObjId", "objectId"));
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

  @SuppressWarnings("resource")
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
      TableQuery query = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(query.aBoolean("queryBoolean"), query.get("childObjId", "objectId"));
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

      vQ.SELECT(query.aBoolean("queryBoolean"), query.get("childObjId", "objectId"));
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
      TableQuery query = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(query.aBoolean("queryBoolean"), query.get("childObjId", "objectId"));
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

      vQ.SELECT(query.aBoolean("queryBoolean"), query.get("childObjId", "objectId"));
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
      TableQuery query = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter", "attr1"), query.aCharacter("queryCharacter", "attr2"), query.get("childObjId"));
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

  @SuppressWarnings("resource")
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
      TableQuery query = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter"), query.get("childObjId"));
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

      vQ.SELECT(query.aCharacter("queryCharacter"), query.get("childObjId"));
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

  @SuppressWarnings("resource")
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
      TableQuery query = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter"), query.get("childObjId"));
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

      vQ.SELECT(query.aCharacter("queryCharacter"), query.get("childObjId"));
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

  @SuppressWarnings("resource")
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
      TableQuery query = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter"), query.get("childObjId"));
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

      vQ.SELECT(query.aCharacter("queryCharacter"), query.get("childObjId"));
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

  @SuppressWarnings("resource")
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
      TableQuery query = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter"), query.get("childObjId"));
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

      vQ.SELECT(query.aCharacter("queryCharacter"), query.get("childObjId"));
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

  @SuppressWarnings("resource")
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
      TableQuery query = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter"), query.get("childObjId"));
      vQ.WHERE(query.aCharacter("queryCharacter").EQ(""));

      i = vQ.getIterator();

      if (!i.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("childObjId").equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }

      // perform another query that should find 3 matches
      vQ = qf.valueQuery();

      vQ.SELECT(query.aCharacter("queryCharacter"), query.get("childObjId"));
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

  @SuppressWarnings("resource")
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
      TableQuery query = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter"), query.get("childObjId"));
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

      vQ.SELECT(query.aCharacter("queryCharacter"), query.get("childObjId"));
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

  @SuppressWarnings("resource")
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
      TableQuery query = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter"), query.get("childObjId"));
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

      vQ.SELECT(query.aCharacter("queryCharacter"), query.get("childObjId"));
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
      TableQuery query = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter"), query.get("childObjId"));
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
      TableQuery query = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter"), query.get("childObjId"));
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
      TableQuery query = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter"), query.get("childObjId"));
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
      TableQuery query = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter"), query.get("childObjId"));
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
      TableQuery query = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter"), query.get("childObjId"));
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
      TableQuery query = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(query.aCharacter("queryCharacter"), query.get("childObjId"));
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

  @SuppressWarnings("resource")
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
      TableQuery query = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(query.aDate("queryDate"), query.get("childObjId", "objectId"));
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

      vQ.SELECT(query.aDate("queryDate"), query.get("childObjId", "objectId"));
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

  @SuppressWarnings("resource")
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
      TableQuery query = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(query.aDate("queryDate"), query.get("childObjId", "objectId"));
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

      vQ.SELECT(query.aDate("queryDate"), query.get("childObjId", "objectId"));
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

  @SuppressWarnings("resource")
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
      TableQuery query = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(query.aDate("queryDate"), query.get("childObjId", "objectId"));
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

      vQ.SELECT(query.aDate("queryDate"), query.get("childObjId", "objectId"));
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

  @SuppressWarnings("resource")
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
      TableQuery query = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(query.aDate("queryDate"), query.get("childObjId", "objectId"));
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

      vQ.SELECT(query.aDate("queryDate"), query.get("childObjId", "objectId"));
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

  @SuppressWarnings("resource")
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
      TableQuery query = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(query.aDate("queryDate"), query.get("childObjId", "objectId"));
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

      vQ.SELECT(query.aDate("queryDate"), query.get("childObjId", "objectId"));
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

      vQ.SELECT(query.aDate("queryDate"), query.get("childObjId", "objectId"));
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

  @SuppressWarnings("resource")
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
      TableQuery query = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(query.aDate("queryDate"), query.get("childObjId", "objectId"));
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

      vQ.SELECT(query.aDate("queryDate"), query.get("childObjId", "objectId"));
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

      vQ.SELECT(query.aDate("queryDate"), query.get("childObjId", "objectId"));
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

  @SuppressWarnings("resource")
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
      TableQuery query = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(query.aDate("queryDate"), query.get("childObjId", "objectId"));
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

      vQ.SELECT(query.aDate("queryDate"), query.get("childObjId", "objectId"));
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

  @SuppressWarnings("resource")
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
      TableQuery query = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(query.aDate("queryDate"), query.get("childObjId", "objectId"));
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

      vQ.SELECT(query.aDate("queryDate"), query.get("childObjId", "objectId"));
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

  @SuppressWarnings("resource")
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
      TableQuery query = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(query.aDate("queryDate"), query.get("childObjId", "objectId"));
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

      vQ.SELECT(query.aDate("queryDate"), query.get("childObjId", "objectId"));
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

  @SuppressWarnings("resource")
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
      TableQuery query = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(query.aDate("queryDate"), query.get("childObjId", "objectId"));
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

      vQ.SELECT(query.aDate("queryDate"), query.get("childObjId", "objectId"));
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

      vQ.SELECT(query.aDate("queryDate"), query.get("childObjId", "objectId"));
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

  @SuppressWarnings("resource")
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
      TableQuery query = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(query.aDate("queryDate"), query.get("childObjId", "objectId"));
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

      vQ.SELECT(query.aDate("queryDate"), query.get("childObjId", "objectId"));
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

      vQ.SELECT(query.aDate("queryDate"), query.get("childObjId", "objectId"));
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

  @SuppressWarnings("resource")
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
      TableQuery query = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(query.aDate("queryDate"), query.get("childObjId", "objectId"));
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

      vQ.SELECT(query.aDate("queryDate"), query.get("childObjId", "objectId"));
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

  @SuppressWarnings("resource")
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
      TableQuery query = qf.tableQuery(childTableQueryInfo.getType());

      vQ.SELECT(query.aDate("queryDate"), query.get("childObjId", "objectId"));
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

      vQ.SELECT(query.aDate("queryDate"), query.get("childObjId", "objectId"));
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

}
