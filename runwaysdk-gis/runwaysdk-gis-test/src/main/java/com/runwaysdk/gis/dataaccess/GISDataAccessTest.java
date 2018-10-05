/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.gis.dataaccess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Assert;
import org.junit.Test;

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.gis.AttributeLineStringParseException;
import com.runwaysdk.gis.AttributeMultiLineStringParseException;
import com.runwaysdk.gis.AttributeMultiPointParseException;
import com.runwaysdk.gis.AttributeMultiPolygonParseException;
import com.runwaysdk.gis.AttributePointParseException;
import com.runwaysdk.gis.AttributePolygonParseException;
import com.runwaysdk.gis.GISAbstractTest;
import com.runwaysdk.gis.GISMasterTestSetup;
import com.runwaysdk.gis.constants.MdAttributePointInfo;
import com.runwaysdk.gis.dataaccess.attributes.entity.AttributeLineString;
import com.runwaysdk.gis.dataaccess.attributes.entity.AttributeMultiLineString;
import com.runwaysdk.gis.dataaccess.attributes.entity.AttributeMultiPoint;
import com.runwaysdk.gis.dataaccess.attributes.entity.AttributeMultiPolygon;
import com.runwaysdk.gis.dataaccess.attributes.entity.AttributePoint;
import com.runwaysdk.gis.dataaccess.attributes.entity.AttributePolygon;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributePointDAO;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.gis.metadata.InvalidDimensionException;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class GISDataAccessTest extends GISAbstractTest
{
  @Request
  @Test
  public void testInvalidDimension()
  {
    MdAttributePointDAO mdAttributePointDAO = MdAttributePointDAO.newInstance();

    try
    {
      mdAttributePointDAO.setValue(MdAttributePointInfo.NAME, "inalidPoint");
      mdAttributePointDAO.setValue(MdAttributePointInfo.DISPLAY_LABEL, "Invalid Point");
      mdAttributePointDAO.setValue(MdAttributePointInfo.DESCRIPTION, "An invalid point with an invalid dimension");
      mdAttributePointDAO.setValue(MdAttributePointInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributePointDAO.setValue(MdAttributePointInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      mdAttributePointDAO.setValue(MdAttributePointInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributePointDAO.setValue(MdAttributePointInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      mdAttributePointDAO.setValue(MdAttributePointInfo.SRID, "4326");
      mdAttributePointDAO.setValue(MdAttributePointInfo.DIMENSION, "5");
      mdAttributePointDAO.setValue(MdAttributePointInfo.DEFINING_MD_CLASS, GISMasterTestSetup.testClassMdBusinessDAO.getOid());
      mdAttributePointDAO.apply();
    }
    catch (InvalidDimensionException e)
    {
      // this is expected
    }
    finally
    {
      if (!mdAttributePointDAO.isNew())
      {
        mdAttributePointDAO.delete();
      }
    }
  }

  /**
   *
   */
  @Request
  @Test
  public void testPointCRUD()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(GISMasterTestSetup.TEST_CLASS.getType());

    try
    {
      ( (AttributePoint) businessDAO.getAttribute("testPoint") ).setValue(191232, 243118);
      businessDAO.apply();

      BusinessDAOIF businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      Point point = ( (AttributePointIF) businessDAOIF.getAttributeIF("testPoint") ).getPoint();

      Assert.assertEquals("X Coordinate on the point was not the expected value.", 191232d, point.getX(), 0.001);
      Assert.assertEquals("Y Coordinate on the point was not the expected value.", 243118d, point.getY(), 0.001);

      businessDAO = businessDAOIF.getBusinessDAO();
      ( (AttributePoint) businessDAO.getAttribute("testPoint") ).setValue(191108, 243242);
      businessDAO.apply();

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      point = ( (AttributePointIF) businessDAOIF.getAttributeIF("testPoint") ).getPoint();

      Assert.assertEquals("X Coordinate on the point was not the expected value.", 191108d, point.getX(), 0.001);
      Assert.assertEquals("Y Coordinate on the point was not the expected value.", 243242d, point.getY(), 0.001);
    }

    finally
    {
      if (!businessDAO.isNew())
      {
        businessDAO = BusinessDAO.get(businessDAO.getOid()).getBusinessDAO();
        businessDAO.delete();
      }
    }
  }

  /**
  *
  */
  @Request
  @Test
  public void testPointCRUD_WKT()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(GISMasterTestSetup.TEST_CLASS.getType());

    try
    {
      ( (AttributePoint) businessDAO.getAttribute("testPoint") ).setValue("POINT(191232 243118)");
      businessDAO.apply();

      BusinessDAOIF businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      Point point = ( (AttributePointIF) businessDAOIF.getAttributeIF("testPoint") ).getPoint();

      Assert.assertEquals("X Coordinate on the point was not the expected value.", 191232d, point.getX(), 0.001);
      Assert.assertEquals("Y Coordinate on the point was not the expected value.", 243118d, point.getY(), 0.001);

      businessDAO = businessDAOIF.getBusinessDAO();
      ( (AttributePoint) businessDAO.getAttribute("testPoint") ).setValue("POINT(191108 243242)");
      businessDAO.apply();

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      point = ( (AttributePointIF) businessDAOIF.getAttributeIF("testPoint") ).getPoint();

      Assert.assertEquals("X Coordinate on the point was not the expected value.", 191108d, point.getX(), 0.001);
      Assert.assertEquals("Y Coordinate on the point was not the expected value.", 243242d, point.getY(), 0.001);
    }
    finally
    {
      if (!businessDAO.isNew())
      {
        businessDAO = BusinessDAO.get(businessDAO.getOid()).getBusinessDAO();
        businessDAO.delete();
      }
    }
  }

  /**
   * @throws
   * 
   */
  @Request
  @Test
  public void testLineStringCRUD_WKT()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(GISMasterTestSetup.TEST_CLASS.getType());

    try
    {
      String lineStringText1 = "LINESTRING (191232 243118, 191108 243242)";

      ( (AttributeLineString) businessDAO.getAttribute("testLineString") ).setValue(lineStringText1);
      businessDAO.apply();

      BusinessDAOIF businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      LineString lineString = ( (AttributeLineStringIF) businessDAOIF.getAttributeIF("testLineString") ).getLineString();

      WKTReader reader = new WKTReader();
      LineString expectedLine = (LineString) reader.read(lineStringText1);

      Assert.assertTrue("LineString was not the expected value.", expectedLine.equalsExact(lineString));

      String lineStringText2 = "LINESTRING (189141 244158, 189265 244817)";

      businessDAO = businessDAOIF.getBusinessDAO();
      ( (AttributeLineString) businessDAO.getAttribute("testLineString") ).setValue(lineStringText2);
      businessDAO.apply();

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      lineString = ( (AttributeLineStringIF) businessDAOIF.getAttributeIF("testLineString") ).getLineString();

      expectedLine = (LineString) reader.read(lineStringText2);

      Assert.assertTrue("LineString was not the expected value.", expectedLine.equalsExact(lineString));
    }
    catch (ParseException e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (!businessDAO.isNew())
      {
        businessDAO = BusinessDAO.get(businessDAO.getOid()).getBusinessDAO();
        businessDAO.delete();
      }
    }
  }

  /**
   * @throws
   * 
   */
  @Request
  @Test
  public void testPolygonCRUD_WKT()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(GISMasterTestSetup.TEST_CLASS.getType());

    try
    {
      String polygonText1 = "POLYGON (( 10 10, 10 20, 20 20, 20 15, 10 10))";

      ( (AttributePolygon) businessDAO.getAttribute("testPolygon") ).setValue(polygonText1);
      businessDAO.apply();

      BusinessDAOIF businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      Polygon polygon = ( (AttributePolygonIF) businessDAOIF.getAttributeIF("testPolygon") ).getPolygon();

      WKTReader reader = new WKTReader();
      Polygon expectedPolygon = (Polygon) reader.read(polygonText1);

      Assert.assertTrue("Polygon was not the expected value.", expectedPolygon.equalsExact(polygon));

      String polygonText2 = "POLYGON (( 10 10, 15 25, 40 40, 30 25, 10 10))";

      businessDAO = businessDAOIF.getBusinessDAO();
      ( (AttributePolygon) businessDAO.getAttribute("testPolygon") ).setValue(polygonText2);
      businessDAO.apply();

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      polygon = ( (AttributePolygon) businessDAOIF.getAttributeIF("testPolygon") ).getPolygon();

      expectedPolygon = (Polygon) reader.read(polygonText2);

      Assert.assertTrue("Polygon was not the expected value.", expectedPolygon.equalsExact(polygon));
    }
    catch (ParseException e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (!businessDAO.isNew())
      {
        businessDAO = BusinessDAO.get(businessDAO.getOid()).getBusinessDAO();
        businessDAO.delete();
      }
    }
  }

  /**
   * @throws
   * 
   */
  @Request
  @Test
  public void testMultiPointCRUD_WKT()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(GISMasterTestSetup.TEST_CLASS.getType());

    try
    {
      String multiPointText1 = "MULTIPOINT(191232 243118, 10000 20000)";

      ( (AttributeMultiPoint) businessDAO.getAttribute("testMultiPoint") ).setValue(multiPointText1);
      businessDAO.apply();

      BusinessDAOIF businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      MultiPoint multiPoint = ( (AttributeMultiPointIF) businessDAOIF.getAttributeIF("testMultiPoint") ).getMultiPoint();

      WKTReader reader = new WKTReader();
      MultiPoint expectedMultiPoint = (MultiPoint) reader.read(multiPointText1);

      Assert.assertTrue("MultiPoint was not the expected value.", expectedMultiPoint.equalsExact(multiPoint));

      String multiPointText2 = "MULTIPOINT(191108 243242, 30000 40000)";

      businessDAO = businessDAOIF.getBusinessDAO();
      ( (AttributeMultiPoint) businessDAO.getAttribute("testMultiPoint") ).setValue(multiPointText2);
      businessDAO.apply();

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      multiPoint = ( (AttributeMultiPoint) businessDAOIF.getAttributeIF("testMultiPoint") ).getMultiPoint();

      expectedMultiPoint = (MultiPoint) reader.read(multiPointText2);

      Assert.assertTrue("MultiPoint was not the expected value.", expectedMultiPoint.equalsExact(multiPoint));
    }
    catch (ParseException e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (!businessDAO.isNew())
      {
        businessDAO = BusinessDAO.get(businessDAO.getOid()).getBusinessDAO();
        businessDAO.delete();
      }
    }
  }

  /**
   * @throws
   * 
   */
  @Request
  @Test
  public void testMultiLineStringCRUD_WKT()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(GISMasterTestSetup.TEST_CLASS.getType());

    try
    {
      String multiLineStringText1 = "MULTILINESTRING ((191232 243118, 191108 243242, 200000 250000, 275000 300000))";

      ( (AttributeMultiLineString) businessDAO.getAttribute("testMultiLineString") ).setValue(multiLineStringText1);
      businessDAO.apply();

      BusinessDAOIF businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      MultiLineString multiLineString = ( (AttributeMultiLineStringIF) businessDAOIF.getAttributeIF("testMultiLineString") ).getMultiLineString();

      WKTReader reader = new WKTReader();
      MultiLineString expectedMultiLineString = (MultiLineString) reader.read(multiLineStringText1);

      Assert.assertTrue("MultiLineString was not the expected value.", expectedMultiLineString.equalsExact(multiLineString));

      String multiLineStringText2 = "MULTILINESTRING ((189141 244158, 189265 244817, 100000 150000, 175000 200000))";

      businessDAO = businessDAOIF.getBusinessDAO();
      ( (AttributeMultiLineString) businessDAO.getAttribute("testMultiLineString") ).setValue(multiLineStringText2);
      businessDAO.apply();

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      multiLineString = ( (AttributeMultiLineStringIF) businessDAOIF.getAttributeIF("testMultiLineString") ).getMultiLineString();

      expectedMultiLineString = (MultiLineString) reader.read(multiLineStringText2);

      Assert.assertTrue("MultiLineString was not the expected value.", expectedMultiLineString.equalsExact(multiLineString));
    }
    catch (ParseException e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (!businessDAO.isNew())
      {
        businessDAO = BusinessDAO.get(businessDAO.getOid()).getBusinessDAO();
        businessDAO.delete();
      }
    }
  }

  /**
   * @throws
   * 
   */
  @Request
  @Test
  public void testMultiPolygonCRUD_WKT()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(GISMasterTestSetup.TEST_CLASS.getType());

    try
    {
      String multiPolygonText1 = "MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2, 3 2, 3 3, 2 3,2 2)),((3 3,6 2,6 4,3 3)))";

      ( (AttributeMultiPolygon) businessDAO.getAttribute("testMultiPolygon") ).setValue(multiPolygonText1);
      businessDAO.apply();

      BusinessDAOIF businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      MultiPolygon multiPolygon = ( (AttributeMultiPolygonIF) businessDAOIF.getAttributeIF("testMultiPolygon") ).getMultiPolygon();

      WKTReader reader = new WKTReader();
      MultiPolygon expectedMultiPolygon = (MultiPolygon) reader.read(multiPolygonText1);

      Assert.assertTrue("MultiPolygon was not the expected value.", expectedMultiPolygon.equalsExact(multiPolygon));

      String multiPolygonText2 = "MULTIPOLYGON(((1 1,5 1,10 10,1 5,1 1),(2 2, 3 2, 6 6, 2 3,2 2)),((3 3,6 2,6 4,3 3)))";

      businessDAO = businessDAOIF.getBusinessDAO();
      ( (AttributeMultiPolygon) businessDAO.getAttribute("testMultiPolygon") ).setValue(multiPolygonText2);
      businessDAO.apply();

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      multiPolygon = ( (AttributeMultiPolygon) businessDAOIF.getAttributeIF("testMultiPolygon") ).getMultiPolygon();

      expectedMultiPolygon = (MultiPolygon) reader.read(multiPolygonText2);

      Assert.assertTrue("MultiPolygon was not the expected value.", expectedMultiPolygon.equalsExact(multiPolygon));
    }
    catch (ParseException e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (!businessDAO.isNew())
      {
        businessDAO = BusinessDAO.get(businessDAO.getOid()).getBusinessDAO();
        businessDAO.delete();
      }
    }
  }

  @Request
  @Test
  public void testNullPointObject()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(GISMasterTestSetup.TEST_CLASS.getType());

    try
    {
      ( (AttributePoint) businessDAO.getAttribute("testPoint") ).setPoint(null);
      businessDAO.apply();

      BusinessDAOIF businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      Point point = ( (AttributePointIF) businessDAOIF.getAttributeIF("testPoint") ).getPoint();

      Assert.assertEquals("Object Created. Point object should be null.", null, point);

      businessDAO = businessDAOIF.getBusinessDAO();
      ( (AttributePoint) businessDAO.getAttribute("testPoint") ).setValue(191108, 243242);
      businessDAO.apply();

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      point = ( (AttributePointIF) businessDAOIF.getAttributeIF("testPoint") ).getPoint();

      Assert.assertEquals("X Coordinate on the point was not the expected value.", 191108d, point.getX(), 0.001);
      Assert.assertEquals("Y Coordinate on the point was not the expected value.", 243242d, point.getY(), 0.001);

      businessDAO = businessDAOIF.getBusinessDAO();
      ( (AttributePoint) businessDAO.getAttribute("testPoint") ).setPoint(null);
      businessDAO.apply();

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      point = ( (AttributePointIF) businessDAOIF.getAttributeIF("testPoint") ).getPoint();

      Assert.assertEquals("Object Updated. Point object should be null.", null, point);
    }
    finally
    {
      if (!businessDAO.isNew())
      {
        businessDAO = BusinessDAO.get(businessDAO.getOid()).getBusinessDAO();
        businessDAO.delete();
      }
    }
  }

  @Request
  @Test
  public void testNullLineStringObject()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(GISMasterTestSetup.TEST_CLASS.getType());

    try
    {
      String lineStringText1 = "LINESTRING (191232 243118, 191108 243242)";

      ( (AttributeLineString) businessDAO.getAttribute("testLineString") ).setLineString(null);
      businessDAO.apply();

      BusinessDAOIF businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      LineString lineString = ( (AttributeLineStringIF) businessDAOIF.getAttributeIF("testLineString") ).getLineString();

      Assert.assertEquals("Object Created. LineString object should be null.", null, lineString);

      businessDAO = businessDAOIF.getBusinessDAO();
      ( (AttributeLineString) businessDAO.getAttribute("testLineString") ).setValue(lineStringText1);
      businessDAO.apply();

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      lineString = ( (AttributeLineStringIF) businessDAOIF.getAttributeIF("testLineString") ).getLineString();

      WKTReader reader = new WKTReader();
      LineString expectedLine = (LineString) reader.read(lineStringText1);

      Assert.assertTrue("LineString was not the expected value.", expectedLine.equalsExact(lineString));

      businessDAO = businessDAOIF.getBusinessDAO();
      ( (AttributeLineString) businessDAO.getAttribute("testLineString") ).setLineString(null);
      businessDAO.apply();

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      lineString = ( (AttributeLineStringIF) businessDAOIF.getAttributeIF("testLineString") ).getLineString();

      Assert.assertEquals("Object Updated. LineString object should be null.", null, lineString);
    }
    catch (ParseException e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (!businessDAO.isNew())
      {
        businessDAO = BusinessDAO.get(businessDAO.getOid()).getBusinessDAO();
        businessDAO.delete();
      }
    }
  }

  /**
   * @throws
   * 
   */
  @Request
  @Test
  public void testNullPolygonCRUD_WKT()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(GISMasterTestSetup.TEST_CLASS.getType());

    try
    {
      ( (AttributePolygon) businessDAO.getAttribute("testPolygon") ).setPolygon(null);
      businessDAO.apply();

      BusinessDAOIF businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      Polygon polygon = ( (AttributePolygonIF) businessDAOIF.getAttributeIF("testPolygon") ).getPolygon();

      Assert.assertEquals("Object Created. Polygon object should be null.", null, polygon);

      String polygonText2 = "POLYGON (( 10 10, 15 25, 40 40, 30 25, 10 10))";

      businessDAO = businessDAOIF.getBusinessDAO();
      ( (AttributePolygon) businessDAO.getAttribute("testPolygon") ).setValue(polygonText2);
      businessDAO.apply();

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      polygon = ( (AttributePolygon) businessDAOIF.getAttributeIF("testPolygon") ).getPolygon();

      WKTReader reader = new WKTReader();
      Polygon expectedPolygon = (Polygon) reader.read(polygonText2);

      expectedPolygon = (Polygon) reader.read(polygonText2);

      Assert.assertTrue("Polygon was not the expected value.", expectedPolygon.equalsExact(polygon));

      ( (AttributePolygon) businessDAO.getAttribute("testPolygon") ).setPolygon(null);
      businessDAO.apply();

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      polygon = ( (AttributePolygonIF) businessDAOIF.getAttributeIF("testPolygon") ).getPolygon();

      Assert.assertEquals("Object Created. Polygon object should be null.", null, polygon);
    }
    catch (ParseException e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (!businessDAO.isNew())
      {
        businessDAO = BusinessDAO.get(businessDAO.getOid()).getBusinessDAO();
        businessDAO.delete();
      }
    }
  }

  /**
   * @throws
   * 
   */
  @Request
  @Test
  public void testNullMultiPointCRUD_WKT()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(GISMasterTestSetup.TEST_CLASS.getType());

    try
    {

      ( (AttributeMultiPoint) businessDAO.getAttribute("testMultiPoint") ).setValue(null);
      businessDAO.apply();

      BusinessDAOIF businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      MultiPoint multiPoint = ( (AttributeMultiPointIF) businessDAOIF.getAttributeIF("testMultiPoint") ).getMultiPoint();

      Assert.assertEquals("Object Created. MultiPoint object should be null.", null, multiPoint);

      String multiPointText2 = "MULTIPOINT(191108 243242, 30000 40000)";

      businessDAO = businessDAOIF.getBusinessDAO();
      ( (AttributeMultiPoint) businessDAO.getAttribute("testMultiPoint") ).setValue(multiPointText2);
      businessDAO.apply();

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      multiPoint = ( (AttributeMultiPoint) businessDAOIF.getAttributeIF("testMultiPoint") ).getMultiPoint();

      WKTReader reader = new WKTReader();
      MultiPoint expectedMultiPoint = (MultiPoint) reader.read(multiPointText2);

      Assert.assertTrue("MultiPoint was not the expected value.", expectedMultiPoint.equalsExact(multiPoint));

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      businessDAO = businessDAOIF.getBusinessDAO();
      ( (AttributeMultiPoint) businessDAO.getAttribute("testMultiPoint") ).setValue(null);
      businessDAO.apply();

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      multiPoint = ( (AttributeMultiPointIF) businessDAOIF.getAttributeIF("testMultiPoint") ).getMultiPoint();

      Assert.assertEquals("Object Created. MultiPoint object should be null.", null, multiPoint);
    }
    catch (ParseException e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (!businessDAO.isNew())
      {
        businessDAO = BusinessDAO.get(businessDAO.getOid()).getBusinessDAO();
        businessDAO.delete();
      }
    }
  }

  /**
   * @throws
   * 
   */
  @Request
  @Test
  public void testNullMultiLineStringCRUD_WKT()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(GISMasterTestSetup.TEST_CLASS.getType());

    try
    {
      ( (AttributeMultiLineString) businessDAO.getAttribute("testMultiLineString") ).setValue(null);
      businessDAO.apply();

      BusinessDAOIF businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      MultiLineString multiLineString = ( (AttributeMultiLineStringIF) businessDAOIF.getAttributeIF("testMultiLineString") ).getMultiLineString();

      Assert.assertEquals("Object Created. MultiLineString object should be null.", null, multiLineString);

      String multiLineStringText2 = "MULTILINESTRING ((189141 244158, 189265 244817, 100000 150000, 175000 200000))";

      businessDAO = businessDAOIF.getBusinessDAO();
      ( (AttributeMultiLineString) businessDAO.getAttribute("testMultiLineString") ).setValue(multiLineStringText2);
      businessDAO.apply();

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      multiLineString = ( (AttributeMultiLineStringIF) businessDAOIF.getAttributeIF("testMultiLineString") ).getMultiLineString();

      WKTReader reader = new WKTReader();
      MultiLineString expectedMultiLineString = (MultiLineString) reader.read(multiLineStringText2);

      Assert.assertTrue("MultiLineString was not the expected value.", expectedMultiLineString.equalsExact(multiLineString));

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      businessDAO = businessDAOIF.getBusinessDAO();

      ( (AttributeMultiLineString) businessDAO.getAttribute("testMultiLineString") ).setValue(null);
      businessDAO.apply();

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      multiLineString = ( (AttributeMultiLineStringIF) businessDAOIF.getAttributeIF("testMultiLineString") ).getMultiLineString();

      Assert.assertEquals("Object Created. MultiLineString object should be null.", null, multiLineString);
    }
    catch (ParseException e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (!businessDAO.isNew())
      {
        businessDAO = BusinessDAO.get(businessDAO.getOid()).getBusinessDAO();
        businessDAO.delete();
      }
    }
  }

  /**
   * @throws
   * 
   */
  @Request
  @Test
  public void testNullMultiPolygonCRUD_WKT()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(GISMasterTestSetup.TEST_CLASS.getType());

    try
    {
      ( (AttributeMultiPolygon) businessDAO.getAttribute("testMultiPolygon") ).setValue(null);
      businessDAO.apply();

      BusinessDAOIF businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      MultiPolygon multiPolygon = ( (AttributeMultiPolygonIF) businessDAOIF.getAttributeIF("testMultiPolygon") ).getMultiPolygon();

      Assert.assertEquals("Object Created. MultiPolygon object should be null.", null, multiPolygon);

      String multiPolygonText2 = "MULTIPOLYGON(((1 1,5 1,10 10,1 5,1 1),(2 2, 3 2, 6 6, 2 3,2 2)),((3 3,6 2,6 4,3 3)))";
      ;

      businessDAO = businessDAOIF.getBusinessDAO();
      ( (AttributeMultiPolygon) businessDAO.getAttribute("testMultiPolygon") ).setValue(multiPolygonText2);
      businessDAO.apply();

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      multiPolygon = ( (AttributeMultiPolygon) businessDAOIF.getAttributeIF("testMultiPolygon") ).getMultiPolygon();

      WKTReader reader = new WKTReader();
      MultiPolygon expectedMultiPolygon = (MultiPolygon) reader.read(multiPolygonText2);

      Assert.assertTrue("MultiPolygon was not the expected value.", expectedMultiPolygon.equalsExact(multiPolygon));

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      businessDAO = businessDAOIF.getBusinessDAO();
      ( (AttributeMultiPolygon) businessDAO.getAttribute("testMultiPolygon") ).setValue(null);
      businessDAO.apply();

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      multiPolygon = ( (AttributeMultiPolygon) businessDAOIF.getAttributeIF("testMultiPolygon") ).getMultiPolygon();

      Assert.assertEquals("Object Created. MultiPolygon object should be null.", null, multiPolygon);
    }
    catch (ParseException e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (!businessDAO.isNew())
      {
        businessDAO = BusinessDAO.get(businessDAO.getOid()).getBusinessDAO();
        businessDAO.delete();
      }
    }
  }

  @Request
  @Test
  public void testEmptyStringPointObject()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(GISMasterTestSetup.TEST_CLASS.getType());

    try
    {
      businessDAO.setValue("testPoint", "");
      businessDAO.apply();

      BusinessDAOIF businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      Point point = ( (AttributePointIF) businessDAOIF.getAttributeIF("testPoint") ).getPoint();

      Assert.assertEquals("Object Created. Point object should be null.", null, point);

      businessDAO = businessDAOIF.getBusinessDAO();
      ( (AttributePoint) businessDAO.getAttribute("testPoint") ).setValue(191108, 243242);
      businessDAO.apply();

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      point = ( (AttributePointIF) businessDAOIF.getAttributeIF("testPoint") ).getPoint();

      Assert.assertEquals("X Coordinate on the point was not the expected value.", 191108d, point.getX(), 0.001);
      Assert.assertEquals("Y Coordinate on the point was not the expected value.", 243242d, point.getY(), 0.001);

      businessDAO = businessDAOIF.getBusinessDAO();
      businessDAO.setValue("testPoint", "");
      businessDAO.apply();

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      point = ( (AttributePointIF) businessDAOIF.getAttributeIF("testPoint") ).getPoint();

      Assert.assertEquals("Object Updated. Point object should be null.", null, point);

    }

    finally
    {
      if (!businessDAO.isNew())
      {
        businessDAO = BusinessDAO.get(businessDAO.getOid()).getBusinessDAO();
        businessDAO.delete();
      }
    }
  }

  @Request
  @Test
  public void testEmptyStringLineStringObject()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(GISMasterTestSetup.TEST_CLASS.getType());

    try
    {
      String lineStringText1 = "LINESTRING (191232 243118, 191108 243242)";

      businessDAO.setValue("testLineString", "");
      businessDAO.apply();

      BusinessDAOIF businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      LineString lineString = ( (AttributeLineStringIF) businessDAOIF.getAttributeIF("testLineString") ).getLineString();

      Assert.assertEquals("Object Created. LineString object should be null.", null, lineString);

      businessDAO = businessDAOIF.getBusinessDAO();
      ( (AttributeLineString) businessDAO.getAttribute("testLineString") ).setValue(lineStringText1);
      businessDAO.apply();

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      lineString = ( (AttributeLineStringIF) businessDAOIF.getAttributeIF("testLineString") ).getLineString();

      WKTReader reader = new WKTReader();
      LineString expectedLine = (LineString) reader.read(lineStringText1);

      Assert.assertTrue("LineString was not the expected value.", expectedLine.equalsExact(lineString));

      businessDAO = businessDAOIF.getBusinessDAO();
      businessDAO.setValue("testLineString", "");
      businessDAO.apply();

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      lineString = ( (AttributeLineStringIF) businessDAOIF.getAttributeIF("testLineString") ).getLineString();

      Assert.assertEquals("Object Updated. LineString object should be null.", null, lineString);
    }
    catch (ParseException e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (!businessDAO.isNew())
      {
        businessDAO = BusinessDAO.get(businessDAO.getOid()).getBusinessDAO();
        businessDAO.delete();
      }
    }
  }

  @Request
  @Test
  public void testEmptyStringPolygonObject()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(GISMasterTestSetup.TEST_CLASS.getType());

    try
    {
      String polygonText1 = "POLYGON (( 10 10, 10 20, 20 20, 20 15, 10 10))";

      businessDAO.setValue("testPolygon", "");
      businessDAO.apply();

      BusinessDAOIF businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      Polygon polygon = ( (AttributePolygonIF) businessDAOIF.getAttributeIF("testPolygon") ).getPolygon();

      Assert.assertEquals("Object Created. Polygon object should be null.", null, polygon);

      businessDAO = businessDAOIF.getBusinessDAO();
      ( (AttributePolygon) businessDAO.getAttribute("testPolygon") ).setValue(polygonText1);
      businessDAO.apply();

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      polygon = ( (AttributePolygonIF) businessDAOIF.getAttributeIF("testPolygon") ).getPolygon();

      WKTReader reader = new WKTReader();
      Polygon expectedPolygon = (Polygon) reader.read(polygonText1);

      Assert.assertTrue("Polygon was not the expected value.", expectedPolygon.equalsExact(expectedPolygon));

      businessDAO = businessDAOIF.getBusinessDAO();
      businessDAO.setValue("testPolygon", "");
      businessDAO.apply();

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      expectedPolygon = ( (AttributePolygonIF) businessDAOIF.getAttributeIF("testPolygon") ).getPolygon();

      Assert.assertEquals("Object Updated. Polygon object should be null.", null, expectedPolygon);
    }
    catch (ParseException e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (!businessDAO.isNew())
      {
        businessDAO = BusinessDAO.get(businessDAO.getOid()).getBusinessDAO();
        businessDAO.delete();
      }
    }
  }

  /**
   * @throws
   * 
   */
  @Request
  @Test
  public void testEmptyStringMultiPointObject()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(GISMasterTestSetup.TEST_CLASS.getType());

    try
    {

      businessDAO.setValue("testMultiPoint", "");
      businessDAO.apply();

      BusinessDAOIF businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      MultiPoint multiPoint = ( (AttributeMultiPointIF) businessDAOIF.getAttributeIF("testMultiPoint") ).getMultiPoint();

      Assert.assertEquals("Object Created. MultiPoint object should be null.", null, multiPoint);

      String multiPointText2 = "MULTIPOINT(191108 243242, 30000 40000)";

      businessDAO = businessDAOIF.getBusinessDAO();
      ( (AttributeMultiPoint) businessDAO.getAttribute("testMultiPoint") ).setValue(multiPointText2);
      businessDAO.apply();

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      multiPoint = ( (AttributeMultiPoint) businessDAOIF.getAttributeIF("testMultiPoint") ).getMultiPoint();

      WKTReader reader = new WKTReader();
      MultiPoint expectedMultiPoint = (MultiPoint) reader.read(multiPointText2);

      Assert.assertTrue("MultiPoint was not the expected value.", expectedMultiPoint.equalsExact(multiPoint));

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      businessDAO = businessDAOIF.getBusinessDAO();
      businessDAO.setValue("testMultiPoint", "");
      businessDAO.apply();

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      multiPoint = ( (AttributeMultiPointIF) businessDAOIF.getAttributeIF("testMultiPoint") ).getMultiPoint();

      Assert.assertEquals("Object Created. MultiPoint object should be null.", null, multiPoint);
    }
    catch (ParseException e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (!businessDAO.isNew())
      {
        businessDAO = BusinessDAO.get(businessDAO.getOid()).getBusinessDAO();
        businessDAO.delete();
      }
    }
  }

  /**
   * @throws
   * 
   */
  @Request
  @Test
  public void testEmptyMultiLineStringCRUD_WKT()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(GISMasterTestSetup.TEST_CLASS.getType());

    try
    {
      businessDAO.setValue("testMultiLineString", "");
      businessDAO.apply();

      BusinessDAOIF businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      MultiLineString multiLineString = ( (AttributeMultiLineStringIF) businessDAOIF.getAttributeIF("testMultiLineString") ).getMultiLineString();

      Assert.assertEquals("Object Created. MultiLineString object should be null.", null, multiLineString);

      String multiLineStringText2 = "MULTILINESTRING ((189141 244158, 189265 244817, 100000 150000, 175000 200000))";

      businessDAO = businessDAOIF.getBusinessDAO();
      ( (AttributeMultiLineString) businessDAO.getAttribute("testMultiLineString") ).setValue(multiLineStringText2);
      businessDAO.apply();

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      multiLineString = ( (AttributeMultiLineStringIF) businessDAOIF.getAttributeIF("testMultiLineString") ).getMultiLineString();

      WKTReader reader = new WKTReader();
      MultiLineString expectedMultiLineString = (MultiLineString) reader.read(multiLineStringText2);

      Assert.assertTrue("MultiLineString was not the expected value.", expectedMultiLineString.equalsExact(multiLineString));

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      businessDAO = businessDAOIF.getBusinessDAO();

      businessDAO.setValue("testMultiLineString", "");
      businessDAO.apply();

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      multiLineString = ( (AttributeMultiLineStringIF) businessDAOIF.getAttributeIF("testMultiLineString") ).getMultiLineString();

      Assert.assertEquals("Object Created. MultiLineString object should be null.", null, multiLineString);
    }
    catch (ParseException e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (!businessDAO.isNew())
      {
        businessDAO = BusinessDAO.get(businessDAO.getOid()).getBusinessDAO();
        businessDAO.delete();
      }
    }
  }

  /**
   * @throws
   * 
   */
  @Request
  @Test
  public void testEmptyStringMultiPolygonCRUD_WKT()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(GISMasterTestSetup.TEST_CLASS.getType());

    try
    {
      businessDAO.setValue("testMultiPolygon", "");
      businessDAO.apply();

      BusinessDAOIF businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      MultiPolygon multiPolygon = ( (AttributeMultiPolygonIF) businessDAOIF.getAttributeIF("testMultiPolygon") ).getMultiPolygon();

      Assert.assertEquals("Object Created. MultiPolygon object should be null.", null, multiPolygon);

      String multiPolygonText2 = "MULTIPOLYGON(((1 1,5 1,10 10,1 5,1 1),(2 2, 3 2, 6 6, 2 3,2 2)),((3 3,6 2,6 4,3 3)))";
      ;

      businessDAO = businessDAOIF.getBusinessDAO();
      ( (AttributeMultiPolygon) businessDAO.getAttribute("testMultiPolygon") ).setValue(multiPolygonText2);
      businessDAO.apply();

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      multiPolygon = ( (AttributeMultiPolygon) businessDAOIF.getAttributeIF("testMultiPolygon") ).getMultiPolygon();

      WKTReader reader = new WKTReader();
      MultiPolygon expectedMultiPolygon = (MultiPolygon) reader.read(multiPolygonText2);

      Assert.assertTrue("MultiPolygon was not the expected value.", expectedMultiPolygon.equalsExact(multiPolygon));

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      businessDAO = businessDAOIF.getBusinessDAO();
      businessDAO.setValue("testMultiPolygon", "");
      businessDAO.apply();

      businessDAOIF = BusinessDAO.get(businessDAO.getOid());
      multiPolygon = ( (AttributeMultiPolygon) businessDAOIF.getAttributeIF("testMultiPolygon") ).getMultiPolygon();

      Assert.assertEquals("Object Created. MultiPolygon object should be null.", null, multiPolygon);
    }
    catch (ParseException e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (!businessDAO.isNew())
      {
        businessDAO = BusinessDAO.get(businessDAO.getOid()).getBusinessDAO();
        businessDAO.delete();
      }
    }
  }

  @Request
  @Test
  public void testInvalidPointStringObject()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(GISMasterTestSetup.TEST_CLASS.getType());

    try
    {
      businessDAO.setValue("testPoint", "Total Garbage");
      businessDAO.apply();

      Assert.fail("An invalid WKT value was set on a point attribute.");
    }
    catch (AttributePointParseException e)
    {
      // this is expected
    }

    finally
    {
      if (!businessDAO.isNew())
      {
        businessDAO = BusinessDAO.get(businessDAO.getOid()).getBusinessDAO();
        businessDAO.delete();
      }
    }
  }

  @Request
  @Test
  public void testInvalidLineStringStringObject()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(GISMasterTestSetup.TEST_CLASS.getType());

    try
    {
      businessDAO.setValue("testLineString", "Total Garbage");
      businessDAO.apply();

      Assert.fail("An invalid WKT value was set on a LineString attribute.");
    }
    catch (AttributeLineStringParseException e)
    {
      // this is expected
    }

    finally
    {
      if (!businessDAO.isNew())
      {
        businessDAO = BusinessDAO.get(businessDAO.getOid()).getBusinessDAO();
        businessDAO.delete();
      }
    }
  }

  @Request
  @Test
  public void testInvalidPolygonString()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(GISMasterTestSetup.TEST_CLASS.getType());

    try
    {
      businessDAO.setValue("testPolygon", "Total Garbage");
      businessDAO.apply();

      Assert.fail("An invalid WKT value was set on a Polygon attribute.");
    }
    catch (AttributePolygonParseException e)
    {
      // this is expected
    }

    finally
    {
      if (!businessDAO.isNew())
      {
        businessDAO = BusinessDAO.get(businessDAO.getOid()).getBusinessDAO();
        businessDAO.delete();
      }
    }
  }

  @Request
  @Test
  public void testInvalidMultiPointString()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(GISMasterTestSetup.TEST_CLASS.getType());

    try
    {
      businessDAO.setValue("testMultiPoint", "Total Garbage");
      businessDAO.apply();

      Assert.fail("An invalid WKT value was set on a MultiPoint attribute.");
    }
    catch (AttributeMultiPointParseException e)
    {
      // this is expected
    }

    finally
    {
      if (!businessDAO.isNew())
      {
        businessDAO = BusinessDAO.get(businessDAO.getOid()).getBusinessDAO();
        businessDAO.delete();
      }
    }
  }

  @Request
  @Test
  public void testInvalidMultiLineStringString()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(GISMasterTestSetup.TEST_CLASS.getType());

    try
    {
      businessDAO.setValue("testMultiLineString", "Total Garbage");
      businessDAO.apply();

      Assert.fail("An invalid WKT value was set on a MultiLineString attribute.");
    }
    catch (AttributeMultiLineStringParseException e)
    {
      // this is expected
    }

    finally
    {
      if (!businessDAO.isNew())
      {
        businessDAO = BusinessDAO.get(businessDAO.getOid()).getBusinessDAO();
        businessDAO.delete();
      }
    }
  }

  @Request
  @Test
  public void testInvalidMultiPolygonString()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(GISMasterTestSetup.TEST_CLASS.getType());

    try
    {
      businessDAO.setValue("testMultiPolygon", "Total Garbage");
      businessDAO.apply();

      Assert.fail("An invalid WKT value was set on a MultiPolygon attribute.");
    }
    catch (AttributeMultiPolygonParseException e)
    {
      // this is expected
    }

    finally
    {
      if (!businessDAO.isNew())
      {
        businessDAO = BusinessDAO.get(businessDAO.getOid()).getBusinessDAO();
        businessDAO.delete();
      }
    }
  }
}
