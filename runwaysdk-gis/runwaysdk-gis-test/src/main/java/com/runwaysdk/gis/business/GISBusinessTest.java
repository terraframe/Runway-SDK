/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK GIS(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.gis.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Assert;
import org.junit.Test;

import com.runwaysdk.business.Business;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.gis.GISAbstractTest;
import com.runwaysdk.gis.GISMasterTestSetup;
import com.runwaysdk.session.Request;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;

public class GISBusinessTest extends GISAbstractTest
{

  /**
   *
   */
  @Request
  @Test
  public void testPointCRUD() throws Throwable
  {
    Business business = null;

    try
    {
      Class<?> testClass = LoaderDecorator.load(GISMasterTestSetup.TEST_CLASS.getType());

      GeometryFactory geometryFactory = new GeometryFactory();
      Point point = geometryFactory.createPoint(new Coordinate(191232, 243118));

      business = (Business) testClass.getConstructor().newInstance();
      testClass.getMethod("setTestPoint", Point.class).invoke(business, point);
      testClass.getMethod("apply").invoke(business);

      business = Business.get(business.getOid());
      Point newPoint = (Point) testClass.getMethod("getTestPoint").invoke(business);

      Assert.assertEquals("X Coordinate on the point was not the expected value.", point.getX(), newPoint.getX(), 0.001);
      Assert.assertEquals("Y Coordinate on the point was not the expected value.", point.getY(), newPoint.getY(), 0.001);
      Assert.assertTrue("Returned point from the database does not match the point that was set on the object.", point.equalsExact(newPoint));

      point = geometryFactory.createPoint(new Coordinate(191108, 243242));
      testClass.getMethod("setTestPoint", Point.class).invoke(business, point);
      testClass.getMethod("apply").invoke(business);

      business = Business.get(business.getOid());
      newPoint = (Point) testClass.getMethod("getTestPoint").invoke(business);

      Assert.assertEquals("X Coordinate on the point was not the expected value.", point.getX(), newPoint.getX(), 0.001);
      Assert.assertEquals("Y Coordinate on the point was not the expected value.", point.getY(), newPoint.getY(), 0.001);
      Assert.assertTrue("Returned point from the database does not match the point that was set on the object.", point.equalsExact(newPoint));
    }
    finally
    {
      if (business != null && !business.isNew())
      {
        business.delete();
      }
    }
  }

  /**
  *
  */
  @Request
  @Test
  public void testLineStringCRUD() throws Throwable
  {
    Business business = null;

    try
    {
      Class<?> testClass = LoaderDecorator.load(GISMasterTestSetup.TEST_CLASS.getType());

      GeometryFactory geometryFactory = new GeometryFactory();
      LineString lineString = geometryFactory.createLineString(new Coordinate[] { new Coordinate(191232, 243118), new Coordinate(191108, 243242) });

      business = (Business) testClass.getConstructor().newInstance();
      testClass.getMethod("setTestLineString", LineString.class).invoke(business, lineString);
      testClass.getMethod("apply").invoke(business);

      business = Business.get(business.getOid());
      LineString newLineString = (LineString) testClass.getMethod("getTestLineString").invoke(business);

      Assert.assertTrue("Returned LineString from the database does not match the LineString that was set on the object.", lineString.equalsExact(newLineString));

      lineString = geometryFactory.createLineString(new Coordinate[] { new Coordinate(189141, 244158), new Coordinate(189265, 244817) });
      testClass.getMethod("setTestLineString", LineString.class).invoke(business, lineString);
      testClass.getMethod("apply").invoke(business);

      business = Business.get(business.getOid());
      newLineString = (LineString) testClass.getMethod("getTestLineString").invoke(business);

      Assert.assertTrue("Returned LineString from the database does not match the LineString that was set on the object.", lineString.equalsExact(newLineString));
    }
    finally
    {
      if (business != null && !business.isNew())
      {
        business.delete();
      }
    }
  }

  /**
  *
  */
  @Request
  @Test
  public void testPolygonCRUD() throws Throwable
  {
    Business business = null;

    try
    {
      Class<?> testClass = LoaderDecorator.load(GISMasterTestSetup.TEST_CLASS.getType());

      String polygonText1 = "POLYGON (( 10 10, 10 20, 20 20, 20 15, 10 10))";

      WKTReader reader = new WKTReader();
      Polygon polygon = (Polygon) reader.read(polygonText1);

      business = (Business) testClass.getConstructor().newInstance();
      testClass.getMethod("setTestPolygon", Polygon.class).invoke(business, polygon);
      testClass.getMethod("apply").invoke(business);

      business = Business.get(business.getOid());
      Polygon newPolygon = (Polygon) testClass.getMethod("getTestPolygon").invoke(business);

      Assert.assertTrue("Returned Polygon from the database does not match the Polygon that was set on the object.", polygon.equalsExact(newPolygon));

      String polygonText2 = "POLYGON (( 10 10, 15 25, 40 40, 30 25, 10 10))";
      polygon = (Polygon) reader.read(polygonText2);

      testClass.getMethod("setTestPolygon", Polygon.class).invoke(business, polygon);
      testClass.getMethod("apply").invoke(business);

      business = Business.get(business.getOid());
      newPolygon = (Polygon) testClass.getMethod("getTestPolygon").invoke(business);

      Assert.assertTrue("Returned Polygon from the database does not match the Polygon that was set on the object.", polygon.equalsExact(newPolygon));
    }
    finally
    {
      if (business != null && !business.isNew())
      {
        business.delete();
      }
    }
  }

  /**
  *
  */
  @Request
  @Test
  public void testMultiPointCRUD() throws Throwable
  {
    Business business = null;

    try
    {
      Class<?> testClass = LoaderDecorator.load(GISMasterTestSetup.TEST_CLASS.getType());

      String multiPointText1 = "MULTIPOINT(191232 243118, 10000 20000)";

      WKTReader reader = new WKTReader();
      MultiPoint multiPoint = (MultiPoint) reader.read(multiPointText1);

      business = (Business) testClass.getConstructor().newInstance();
      testClass.getMethod("setTestMultiPoint", MultiPoint.class).invoke(business, multiPoint);
      testClass.getMethod("apply").invoke(business);

      business = Business.get(business.getOid());
      MultiPoint newMultiPoint = (MultiPoint) testClass.getMethod("getTestMultiPoint").invoke(business);

      Assert.assertTrue("Returned MultiPoint from the database does not match the MultiPoint that was set on the object.", multiPoint.equalsExact(newMultiPoint));

      String multiPointText2 = "MULTIPOINT(191108 243242, 30000 40000)";
      multiPoint = (MultiPoint) reader.read(multiPointText2);

      testClass.getMethod("setTestMultiPoint", MultiPoint.class).invoke(business, multiPoint);
      testClass.getMethod("apply").invoke(business);

      business = Business.get(business.getOid());
      newMultiPoint = (MultiPoint) testClass.getMethod("getTestMultiPoint").invoke(business);

      Assert.assertTrue("Returned MultiPoint from the database does not match the MultiPoint that was set on the object.", multiPoint.equalsExact(newMultiPoint));
    }
    finally
    {
      if (business != null && !business.isNew())
      {
        business.delete();
      }
    }
  }

  /**
  *
  */
  @Request
  @Test
  public void testMultiLineStringCRUD() throws Throwable
  {
    Business business = null;

    try
    {
      Class<?> testClass = LoaderDecorator.load(GISMasterTestSetup.TEST_CLASS.getType());

      String multiLineString1 = "MULTILINESTRING ((191232 243118, 191108 243242, 200000 250000, 275000 300000))";

      WKTReader reader = new WKTReader();
      MultiLineString multiLineString = (MultiLineString) reader.read(multiLineString1);

      business = (Business) testClass.getConstructor().newInstance();
      testClass.getMethod("setTestMultiLineString", MultiLineString.class).invoke(business, multiLineString);
      testClass.getMethod("apply").invoke(business);

      business = Business.get(business.getOid());
      MultiLineString newMultiLineString = (MultiLineString) testClass.getMethod("getTestMultiLineString").invoke(business);

      Assert.assertTrue("Returned MultiLineString from the database does not match the MultiLineString that was set on the object.", multiLineString.equalsExact(newMultiLineString));

      String multiLineString2 = "MULTILINESTRING ((189141 244158, 189265 244817, 100000 150000, 175000 200000))";
      multiLineString = (MultiLineString) reader.read(multiLineString2);

      testClass.getMethod("setTestMultiLineString", MultiLineString.class).invoke(business, multiLineString);
      testClass.getMethod("apply").invoke(business);

      business = Business.get(business.getOid());
      newMultiLineString = (MultiLineString) testClass.getMethod("getTestMultiLineString").invoke(business);

      Assert.assertTrue("Returned MultiLineString from the database does not match the MultiLineString that was set on the object.", multiLineString.equalsExact(newMultiLineString));
    }

    finally
    {
      if (business != null && !business.isNew())
      {
        business.delete();
      }
    }
  }

  /**
  *
  */
  @Request
  @Test
  public void testMultiPolygonCRUD() throws Throwable
  {
    Business business = null;

    try
    {
      Class<?> testClass = LoaderDecorator.load(GISMasterTestSetup.TEST_CLASS.getType());

      String multiPolygon1 = "MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2, 3 2, 3 3, 2 3,2 2)),((3 3,6 2,6 4,3 3)))";

      WKTReader reader = new WKTReader();
      MultiPolygon multiPolygon = (MultiPolygon) reader.read(multiPolygon1);

      business = (Business) testClass.getConstructor().newInstance();
      testClass.getMethod("setTestMultiPolygon", MultiPolygon.class).invoke(business, multiPolygon);
      testClass.getMethod("apply").invoke(business);

      business = Business.get(business.getOid());
      MultiPolygon newMultiPolygon = (MultiPolygon) testClass.getMethod("getTestMultiPolygon").invoke(business);

      Assert.assertTrue("Returned MultiPolygon from the database does not match the MultiPolygon that was set on the object.", multiPolygon.equalsExact(newMultiPolygon));

      String multiLineString2 = "MULTIPOLYGON(((1 1,5 1,10 10,1 5,1 1),(2 2, 3 2, 6 6, 2 3,2 2)),((3 3,6 2,6 4,3 3)))";
      multiPolygon = (MultiPolygon) reader.read(multiLineString2);

      testClass.getMethod("setTestMultiPolygon", MultiPolygon.class).invoke(business, multiPolygon);
      testClass.getMethod("apply").invoke(business);

      business = Business.get(business.getOid());
      newMultiPolygon = (MultiPolygon) testClass.getMethod("getTestMultiPolygon").invoke(business);

      Assert.assertTrue("Returned MultiPolygon from the database does not match the MultiPolygon that was set on the object.", multiPolygon.equalsExact(newMultiPolygon));
    }

    finally
    {
      if (business != null && !business.isNew())
      {
        business.delete();
      }
    }
  }

  @Request
  @Test
  public void testNullPointObject() throws Throwable
  {
    Business business = null;

    try
    {
      Class<?> testClass = LoaderDecorator.load(GISMasterTestSetup.TEST_CLASS.getType());

      business = (Business) testClass.getConstructor().newInstance();
      testClass.getMethod("setTestPoint", Point.class).invoke(business, (Point) null);
      testClass.getMethod("apply").invoke(business);

      business = Business.get(business.getOid());
      Point point = (Point) testClass.getMethod("getTestPoint").invoke(business);

      Assert.assertEquals("Point object should be null.", null, point);
    }

    finally
    {
      if (business != null && !business.isNew())
      {
        business.delete();
      }
    }
  }

  @Request
  @Test
  public void testNullLineStringObject() throws Throwable
  {
    Business business = null;

    try
    {
      Class<?> testClass = LoaderDecorator.load(GISMasterTestSetup.TEST_CLASS.getType());

      business = (Business) testClass.getConstructor().newInstance();
      testClass.getMethod("setTestLineString", LineString.class).invoke(business, (LineString) null);
      testClass.getMethod("apply").invoke(business);

      business = Business.get(business.getOid());
      LineString lineString = (LineString) testClass.getMethod("getTestLineString").invoke(business);

      Assert.assertEquals("LineString object should be null.", null, lineString);
    }

    finally
    {
      if (business != null && !business.isNew())
      {
        business.delete();
      }
    }
  }

  @Request
  @Test
  public void testNullPolygonObject() throws Throwable
  {
    Business business = null;

    try
    {
      Class<?> testClass = LoaderDecorator.load(GISMasterTestSetup.TEST_CLASS.getType());

      business = (Business) testClass.getConstructor().newInstance();
      testClass.getMethod("setTestPolygon", Polygon.class).invoke(business, (Polygon) null);
      testClass.getMethod("apply").invoke(business);

      business = Business.get(business.getOid());
      Polygon polygon = (Polygon) testClass.getMethod("getTestPolygon").invoke(business);

      Assert.assertEquals("Polygon object should be null.", null, polygon);
    }

    finally
    {
      if (business != null && !business.isNew())
      {
        business.delete();
      }
    }
  }

  @Request
  @Test
  public void testNullMultiPointObject() throws Throwable
  {
    Business business = null;

    try
    {
      Class<?> testClass = LoaderDecorator.load(GISMasterTestSetup.TEST_CLASS.getType());

      business = (Business) testClass.getConstructor().newInstance();
      testClass.getMethod("setTestMultiPoint", MultiPoint.class).invoke(business, (MultiPoint) null);
      testClass.getMethod("apply").invoke(business);

      business = Business.get(business.getOid());
      MultiPoint multiPoint = (MultiPoint) testClass.getMethod("getTestMultiPoint").invoke(business);

      Assert.assertEquals("MultiPoint object should be null.", null, multiPoint);
    }

    finally
    {
      if (business != null && !business.isNew())
      {
        business.delete();
      }
    }
  }

  @Request
  @Test
  public void testNullMultiLineStringObject() throws Throwable
  {
    Business business = null;

    try
    {
      Class<?> testClass = LoaderDecorator.load(GISMasterTestSetup.TEST_CLASS.getType());

      business = (Business) testClass.getConstructor().newInstance();
      testClass.getMethod("setTestMultiLineString", MultiLineString.class).invoke(business, (MultiLineString) null);
      testClass.getMethod("apply").invoke(business);

      business = Business.get(business.getOid());
      MultiLineString multiLineString = (MultiLineString) testClass.getMethod("getTestMultiLineString").invoke(business);

      Assert.assertEquals("MulitLineString object should be null.", null, multiLineString);
    }

    finally
    {
      if (business != null && !business.isNew())
      {
        business.delete();
      }
    }
  }

  @Request
  @Test
  public void testNullMultiPolygonObject() throws Throwable
  {
    Business business = null;

    try
    {
      Class<?> testClass = LoaderDecorator.load(GISMasterTestSetup.TEST_CLASS.getType());

      business = (Business) testClass.getConstructor().newInstance();
      testClass.getMethod("setTestMultiPolygon", MultiPolygon.class).invoke(business, (MultiPolygon) null);
      testClass.getMethod("apply").invoke(business);

      business = Business.get(business.getOid());
      MultiPolygon multipolygon = (MultiPolygon) testClass.getMethod("getTestMultiPolygon").invoke(business);

      Assert.assertEquals("MultiPolygon object should be null.", null, multipolygon);
    }

    finally
    {
      if (business != null && !business.isNew())
      {
        business.delete();
      }
    }
  }

}
