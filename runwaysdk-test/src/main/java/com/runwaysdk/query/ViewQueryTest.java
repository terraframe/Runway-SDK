/**
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
 */
package com.runwaysdk.query;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.business.generation.EntityQueryAPIGenerator;
import com.runwaysdk.business.generation.ViewQueryStubAPIGenerator;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.generation.loader.LoaderDecorator;

public class ViewQueryTest extends TestCase
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

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ViewQueryTest.class);

    TestSetup wrapper = new TestSetup(suite)
    {
      protected void setUp()
      {
      }

      protected void tearDown()
      {
      }
    };

    return wrapper;
  }

  @SuppressWarnings("unchecked")
  public void testSomething()
  {
    BusinessDAO someChildRefQueryObject = BusinessDAO.newInstance(QueryMasterSetup.childRefQueryInfo.getType());
    someChildRefQueryObject.addItem("refQueryEnumeration", QueryMasterSetup.connecticutItemId);
    someChildRefQueryObject.setValue("refQueryBoolean", MdAttributeBooleanInfo.TRUE);
    someChildRefQueryObject.setValue("refQueryCharacter", "other ref character value");
    someChildRefQueryObject.setValue("refQueryText", "other ref text value");
    someChildRefQueryObject.setValue("refQueryClob", "other ref clob value");
    someChildRefQueryObject.setValue("refQueryDateTime", "2008-11-06 12:00:00");
    someChildRefQueryObject.setValue("refQueryDate", "2008-11-06");
    someChildRefQueryObject.setValue("refQueryTime", "02:00:00");
    someChildRefQueryObject.setValue("refQueryInteger", "300");
    someChildRefQueryObject.setValue("refQueryLong", "300");
    someChildRefQueryObject.setValue("refQueryFloat", "300.5");
    someChildRefQueryObject.setValue("refQueryDecimal", "300.5");
    someChildRefQueryObject.setValue("refQueryDouble", "300.5");
    someChildRefQueryObject.apply();

    BusinessDAO someTestQueryObject = BusinessDAO.newInstance(QueryMasterSetup.childMdBusiness.definesType());
    someTestQueryObject.addItem("queryEnumeration", QueryMasterSetup.coloradoItemId);
    someTestQueryObject.addItem("queryEnumeration", QueryMasterSetup.californiaItemId);
    someTestQueryObject.setValue("queryBoolean", MdAttributeBooleanInfo.TRUE);
    someTestQueryObject.setValue("queryCharacter", "other character value");
    someTestQueryObject.setValue("queryText", "other text value");
    someTestQueryObject.setValue("queryClob", "other clob value");
    someTestQueryObject.setValue("queryDateTime", "2006-12-06 13:00:00");
    someTestQueryObject.setValue("queryDate", "2006-12-06");
    someTestQueryObject.setValue("queryTime", "13:00:00");
    someTestQueryObject.setValue("queryInteger", "400");
    someTestQueryObject.setValue("queryLong", "400");
    someTestQueryObject.setValue("queryFloat", "400.5");
    someTestQueryObject.setValue("queryDecimal", "400.5");
    someTestQueryObject.setValue("queryDouble", "400.5");
    someTestQueryObject.setValue("reference", someChildRefQueryObject.getId());
    someTestQueryObject.apply();

    try
    {
      String type = QueryMasterSetup.viewQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = ViewQueryStubAPIGenerator.getQueryStubClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeChar = (SelectableChar) queryClass.getMethod("getQueryCharacter").invoke(queryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.EQ("other character value"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        objectClass.cast(object);
        String queryCharacter = (String) objectClass.getMethod("getQueryCharacter").invoke(object);
        String queryText = (String) objectClass.getMethod("getRefQueryText").invoke(object);
        String queryClob = (String) objectClass.getMethod("getRefQueryClob").invoke(object);

        assertEquals(queryCharacter, "other character value");
        assertEquals(queryText, "other ref text value");
        assertEquals(queryClob, "other ref clob value");
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      fail(e.getMessage());
    }
    finally
    {
      someTestQueryObject.delete();
      someChildRefQueryObject.delete();
    }
  }

}
