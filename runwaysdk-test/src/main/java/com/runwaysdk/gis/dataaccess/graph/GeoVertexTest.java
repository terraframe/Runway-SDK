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
package com.runwaysdk.gis.dataaccess.graph;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.dataaccess.graph.GraphDBService;
import com.runwaysdk.dataaccess.graph.GraphRequest;
import com.runwaysdk.dataaccess.graph.MdGraphClassTest;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;
import com.runwaysdk.gis.constants.GeoEntityInfo;
import com.runwaysdk.gis.constants.MdGeoVertexInfo;
import com.runwaysdk.gis.dataaccess.metadata.graph.MdGeoVertexDAO;
import com.runwaysdk.session.Request;

public class GeoVertexTest
{
  public static String TEST_PACKAGE            = TestFixConst.TEST_PACKAGE;

  public static String GEO_VERTEX_CLASS_NAME_1 = "TestGeoVertexClass1";

  public static String GEO_VERTEX_CLASS_NAME_2 = "TestGeoVertexClass2";

  public static String GEO_VERTEX_CLASS_1      = TEST_PACKAGE + "." + GEO_VERTEX_CLASS_NAME_1;

  public static String GEO_VERTEX_CLASS_2      = TEST_PACKAGE + "." + GEO_VERTEX_CLASS_NAME_2;

  public static String GEO_EDGE_CLASS_NAME     = "GeoTestEdgeClass";

  public static String GEO_EDGE_CLASS          = TEST_PACKAGE + "." + GEO_EDGE_CLASS_NAME;

  @Request
  @BeforeClass
  public static void classSetUp()
  {
    LocalProperties.setSkipCodeGenAndCompile(true);

    TestFixtureFactory.deleteMdClass(GEO_EDGE_CLASS);

    TestFixtureFactory.deleteMdClass(GEO_VERTEX_CLASS_2);

    TestFixtureFactory.deleteMdClass(GEO_VERTEX_CLASS_1);
  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    LocalProperties.setSkipCodeGenAndCompile(false);
  }

  @Request
  @Before
  public void setUp() throws Exception
  {

  }

  @Request
  @After
  public void tearDown() throws Exception
  {
    TestFixtureFactory.deleteMdClass(GEO_EDGE_CLASS);

    TestFixtureFactory.deleteMdClass(GEO_VERTEX_CLASS_2);

    TestFixtureFactory.deleteMdClass(GEO_VERTEX_CLASS_1);
  }

  @Request
  @Test
  public void testCreateMdGeoVertex()
  {
    MdGeoVertexDAO mdGeoVertexDAO = createGeoVertexClass(GEO_VERTEX_CLASS_NAME_1);

    String dbClassName = mdGeoVertexDAO.getValue(MdGeoVertexInfo.DB_CLASS_NAME);
    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();

    MdGraphClassTest.examineMdGraphClassCreationAndDefaultAttributes(mdGeoVertexDAO, dbClassName, graphRequest);

    boolean attrDefined;

    for (String dbAttrName : GeoEntityInfo.DEFAULT_ATTRIBUTES)
    {
      if (!dbAttrName.equals(GeoEntityInfo.GEOID))
      {
        attrDefined = GraphDBService.getInstance().isClassAttributeDefined(graphRequest, dbClassName, dbAttrName);
        Assert.assertEquals("Attribute was not defined in the graph DB", true, attrDefined);
      }
    }

    mdGeoVertexDAO.delete();

    boolean classDefined = GraphDBService.getInstance().isVertexClassDefined(graphRequest, dbClassName);
    Assert.assertEquals("Vertex class still exists in the database", false, classDefined);
  }

  private static MdGeoVertexDAO createGeoVertexClass(String className)
  {
    MdGeoVertexDAO mdGeoVertexDAO = TestFixtureFactory.createMdGeoVertex(className);
    mdGeoVertexDAO.setValue(MdGeoVertexInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdGeoVertexDAO.apply();

    return mdGeoVertexDAO;
  }
}
