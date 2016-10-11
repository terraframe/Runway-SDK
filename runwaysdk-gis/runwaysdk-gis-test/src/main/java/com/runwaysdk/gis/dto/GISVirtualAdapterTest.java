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
package com.runwaysdk.gis.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.ClientSession;
import com.runwaysdk.DoNotWeave;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.ViewDTO;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.constants.UserInfo;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.gis.AttributeLineStringParseExceptionDTO;
import com.runwaysdk.gis.AttributeMultiLineStringParseExceptionDTO;
import com.runwaysdk.gis.AttributeMultiPointParseExceptionDTO;
import com.runwaysdk.gis.AttributeMultiPolygonParseExceptionDTO;
import com.runwaysdk.gis.AttributePointParseExceptionDTO;
import com.runwaysdk.gis.AttributePolygonParseExceptionDTO;
import com.runwaysdk.gis.GISAbstractTest;
import com.runwaysdk.gis.GISMasterTestSetup;
import com.runwaysdk.session.AttributeReadPermissionExceptionDTO;
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

public class GISVirtualAdapterTest extends GISAbstractTest implements DoNotWeave
{
  protected static BusinessDTO     tommyUser     = null;

  protected static ClientSession   systemSession = null;

  protected static ClientRequestIF clientRequest = null;

  @BeforeClass
  public static void classSetUp()
  {
    systemSession = ClientSession.createUserSession("default", ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });
    clientRequest = systemSession.getRequest();

    tommyUser = clientRequest.newBusiness(UserInfo.CLASS);
    tommyUser.setValue(UserInfo.USERNAME, "Tommy");
    tommyUser.setValue(UserInfo.PASSWORD, "music");
    clientRequest.createBusiness(tommyUser);
  }

  @AfterClass
  public static void classTearDown()
  {
    clientRequest.delete(tommyUser.getId());

    systemSession.logout();
  }

  protected ClientSession createSession(String userName, String password)
  {
    return ClientSession.createUserSession("default", userName, password, new Locale[] { CommonProperties.getDefaultLocale() });
  }

  protected ClientRequestIF getRequest(ClientSession clientSession)
  {
    return clientSession.getRequest();
  }

  /**
  *
  */
  @Test
  @Request
  public void testPointCRUDTypeUnsafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePointDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePointDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = (ViewDTO) tommyRequest.newMutable(GISMasterTestSetup.testVirtualMdViewDAO.definesType());

    GeometryFactory geometryFactory = new GeometryFactory();

    try
    {
      Point point = geometryFactory.createPoint(new Coordinate(191232, 243118));
      viewDTO.setValue("testPoint", point);
      tommyRequest.createSessionComponent(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());

      point = (Point) viewDTO.getObjectValue("testPoint");

      assertEquals("X Coordinate on the point was not the expected value.", 191232d, point.getX(), 0.001);
      assertEquals("Y Coordinate on the point was not the expected value.", 243118d, point.getY(), 0.001);

      point = geometryFactory.createPoint(new Coordinate(191108, 243242));
      viewDTO.setValue("testPoint", point);
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      point = (Point) viewDTO.getObjectValue("testPoint");

      assertEquals("X Coordinate on the point was not the expected value.", 191108d, point.getX(), 0.001);
      assertEquals("Y Coordinate on the point was not the expected value.", 243242d, point.getY(), 0.001);
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePointDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePointDAO.getId(), Operation.READ.name());
    }
  }

  /**
*
*/
  public void testLineStringCRUDTypeUnsafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeLineStringDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeLineStringDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = (ViewDTO) tommyRequest.newMutable(GISMasterTestSetup.testVirtualMdViewDAO.definesType());

    GeometryFactory geometryFactory = new GeometryFactory();

    try
    {
      LineString lineString = geometryFactory.createLineString(new Coordinate[] { new Coordinate(191232, 243118), new Coordinate(191108, 243242) });
      viewDTO.setValue("testLineString", lineString);
      tommyRequest.createSessionComponent(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());

      LineString newLineString = (LineString) viewDTO.getObjectValue("testLineString");

      assertTrue("Returned LineString from the database does not match the LineString that was set on the object.", lineString.equalsExact(newLineString));

      lineString = geometryFactory.createLineString(new Coordinate[] { new Coordinate(189141, 244158), new Coordinate(189265, 244817) });
      viewDTO.setValue("testLineString", lineString);
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      newLineString = (LineString) viewDTO.getObjectValue("testLineString");

      assertTrue("Returned LineString from the database does not match the LineString that was set on the object.", lineString.equalsExact(newLineString));
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeLineStringDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeLineStringDAO.getId(), Operation.READ.name());
    }
  }

  public void testPolygonCRUDTypeUnsafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePolygonDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePolygonDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = (ViewDTO) tommyRequest.newMutable(GISMasterTestSetup.testVirtualMdViewDAO.definesType());

    try
    {
      String polygonText1 = "POLYGON (( 10 10, 10 20, 20 20, 20 15, 10 10))";

      WKTReader reader = new WKTReader();
      Polygon polygon = (Polygon) reader.read(polygonText1);

      viewDTO.setValue("testPolygon", polygon);
      tommyRequest.createSessionComponent(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());

      Polygon newPolygon = (Polygon) viewDTO.getObjectValue("testPolygon");

      assertTrue("Returned Polygon from the database does not match the Polygon that was set on the object.", polygon.equalsExact(newPolygon));

      String polygonText2 = "POLYGON (( 10 10, 15 25, 40 40, 30 25, 10 10))";
      polygon = (Polygon) reader.read(polygonText2);

      viewDTO.setValue("testPolygon", polygon);
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      newPolygon = (Polygon) viewDTO.getObjectValue("testPolygon");

      assertTrue("Returned Polygon from the database does not match the Polygon that was set on the object.", polygon.equalsExact(newPolygon));
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePolygonDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePolygonDAO.getId(), Operation.READ.name());
    }
  }

  public void testMultiPointCRUDTypeUnsafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPointDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPointDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = (ViewDTO) tommyRequest.newMutable(GISMasterTestSetup.testVirtualMdViewDAO.definesType());

    try
    {
      String multiPointText1 = "MULTIPOINT(191232 243118, 10000 20000)";

      WKTReader reader = new WKTReader();
      MultiPoint multiPoint = (MultiPoint) reader.read(multiPointText1);

      viewDTO.setValue("testMultiPoint", multiPoint);
      tommyRequest.createSessionComponent(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());

      MultiPoint newMultiPoint = (MultiPoint) viewDTO.getObjectValue("testMultiPoint");

      assertTrue("Returned MultiPoint from the database does not match the MultiPoint that was set on the object.", multiPoint.equalsExact(newMultiPoint));

      String polygonText2 = "MULTIPOINT(191108 243242, 30000 40000)";
      multiPoint = (MultiPoint) reader.read(polygonText2);

      viewDTO.setValue("testMultiPoint", multiPoint);
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      newMultiPoint = (MultiPoint) viewDTO.getObjectValue("testMultiPoint");

      assertTrue("Returned MultiPoint from the database does not match the MultiPoint that was set on the object.", multiPoint.equalsExact(newMultiPoint));
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPointDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPointDAO.getId(), Operation.READ.name());
    }
  }

  public void testMultiLineStringCRUDTypeUnsafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiLineStringDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiLineStringDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = (ViewDTO) tommyRequest.newMutable(GISMasterTestSetup.testVirtualMdViewDAO.definesType());

    try
    {
      String multiLineStringText1 = "MULTILINESTRING ((191232 243118, 191108 243242, 200000 250000, 275000 300000))";

      WKTReader reader = new WKTReader();
      MultiLineString multiLineString = (MultiLineString) reader.read(multiLineStringText1);

      viewDTO.setValue("testMultiLineString", multiLineString);
      tommyRequest.createSessionComponent(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());

      MultiLineString newMultiLineString = (MultiLineString) viewDTO.getObjectValue("testMultiLineString");

      assertTrue("Returned MultiLineString from the database does not match the MultiLineString that was set on the object.", multiLineString.equalsExact(newMultiLineString));

      String polygonText2 = "MULTILINESTRING ((189141 244158, 189265 244817, 100000 150000, 175000 200000))";
      multiLineString = (MultiLineString) reader.read(polygonText2);

      viewDTO.setValue("testMultiLineString", multiLineString);
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      newMultiLineString = (MultiLineString) viewDTO.getObjectValue("testMultiLineString");

      assertTrue("Returned MultiLineString from the database does not match the MultiLineString that was set on the object.", multiLineString.equalsExact(newMultiLineString));
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiLineStringDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiLineStringDAO.getId(), Operation.READ.name());
    }
  }

  public void testMultiPolygonCRUDTypeUnsafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPolygonDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPolygonDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = (ViewDTO) tommyRequest.newMutable(GISMasterTestSetup.testVirtualMdViewDAO.definesType());

    try
    {
      String multiPolygon1 = "MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2, 3 2, 3 3, 2 3,2 2)),((3 3,6 2,6 4,3 3)))";

      WKTReader reader = new WKTReader();
      MultiPolygon multiPolygon = (MultiPolygon) reader.read(multiPolygon1);

      viewDTO.setValue("testMultiPolygon", multiPolygon);
      tommyRequest.createSessionComponent(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());

      MultiPolygon newMultiPolygon = (MultiPolygon) viewDTO.getObjectValue("testMultiPolygon");

      assertTrue("Returned MultiPolygon from the database does not match the MultiPolygon that was set on the object.", multiPolygon.equalsExact(newMultiPolygon));

      String multiPolyton2 = "MULTIPOLYGON(((1 1,5 1,10 10,1 5,1 1),(2 2, 3 2, 6 6, 2 3,2 2)),((3 3,6 2,6 4,3 3)))";
      multiPolygon = (MultiPolygon) reader.read(multiPolyton2);

      viewDTO.setValue("testMultiPolygon", multiPolygon);
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      newMultiPolygon = (MultiPolygon) viewDTO.getObjectValue("testMultiPolygon");

      assertTrue("Returned MultiPolygon from the database does not match the MultiPolygon that was set on the object.", multiPolygon.equalsExact(newMultiPolygon));
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPolygonDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPolygonDAO.getId(), Operation.READ.name());
    }
  }

  /**
*
*/
  public void testPointCRUDTypeUnsafe_WKT()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePointDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePointDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = (ViewDTO) tommyRequest.newMutable(GISMasterTestSetup.testVirtualMdViewDAO.definesType());

    try
    {
      viewDTO.setValue("testPoint", "POINT(191232 243118)");
      tommyRequest.createSessionComponent(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());

      Point point = (Point) viewDTO.getObjectValue("testPoint");

      assertEquals("X Coordinate on the point was not the expected value.", 191232d, point.getX());
      assertEquals("Y Coordinate on the point was not the expected value.", 243118d, point.getY());

      viewDTO.setValue("testPoint", "POINT(191108 243242)");
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      point = (Point) viewDTO.getObjectValue("testPoint");

      assertEquals("X Coordinate on the point was not the expected value.", 191108d, point.getX());
      assertEquals("Y Coordinate on the point was not the expected value.", 243242d, point.getY());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePointDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePointDAO.getId(), Operation.READ.name());
    }
  }

  /**
*
*/
  public void testLineStringCRUDTypeUnsafe_WKT()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeLineStringDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeLineStringDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = (ViewDTO) tommyRequest.newMutable(GISMasterTestSetup.testVirtualMdViewDAO.definesType());

    try
    {
      String lineStringText1 = "LINESTRING (191232 243118, 191108 243242)";
      viewDTO.setValue("testLineString", lineStringText1);
      tommyRequest.createSessionComponent(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());

      LineString lineString = (LineString) viewDTO.getObjectValue("testLineString");

      WKTReader reader = new WKTReader();
      LineString expectedLine = (LineString) reader.read(lineStringText1);

      assertTrue("LineString was not the expected value.", expectedLine.equalsExact(lineString));

      String lineStringText2 = "LINESTRING (189141 244158, 189265 244817)";

      viewDTO.setValue("testLineString", lineStringText2);
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      lineString = (LineString) viewDTO.getObjectValue("testLineString");

      expectedLine = (LineString) reader.read(lineStringText2);

      assertTrue("LineString was not the expected value.", expectedLine.equalsExact(lineString));
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeLineStringDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeLineStringDAO.getId(), Operation.READ.name());
    }
  }

  /**
*
*/
  public void testPolygonCRUDTypeUnsafe_WKT()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePolygonDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePolygonDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = (ViewDTO) tommyRequest.newMutable(GISMasterTestSetup.testVirtualMdViewDAO.definesType());

    try
    {
      String polygonText1 = "POLYGON (( 10 10, 10 20, 20 20, 20 15, 10 10))";
      viewDTO.setValue("testPolygon", polygonText1);
      tommyRequest.createSessionComponent(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());

      Polygon polygon = (Polygon) viewDTO.getObjectValue("testPolygon");

      WKTReader reader = new WKTReader();
      Polygon expectedPolygon = (Polygon) reader.read(polygonText1);

      assertTrue("Polygon was not the expected value.", expectedPolygon.equalsExact(polygon));

      String polygonText2 = "POLYGON (( 10 10, 15 25, 40 40, 30 25, 10 10))";

      viewDTO.setValue("testPolygon", polygonText2);
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      polygon = (Polygon) viewDTO.getObjectValue("testPolygon");

      expectedPolygon = (Polygon) reader.read(polygonText2);

      assertTrue("Polygon was not the expected value.", expectedPolygon.equalsExact(polygon));
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePolygonDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePolygonDAO.getId(), Operation.READ.name());
    }
  }

  /**
*
*/
  public void testMultiPointCRUDTypeUnsafe_WKT()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPointDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPointDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = (ViewDTO) tommyRequest.newMutable(GISMasterTestSetup.testVirtualMdViewDAO.definesType());

    try
    {
      String multiPointText1 = "MULTIPOINT(191232 243118, 10000 20000)";
      viewDTO.setValue("testMultiPoint", multiPointText1);
      tommyRequest.createSessionComponent(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());

      MultiPoint multiPoint = (MultiPoint) viewDTO.getObjectValue("testMultiPoint");

      WKTReader reader = new WKTReader();
      MultiPoint expectedMultiPoint = (MultiPoint) reader.read(multiPointText1);

      assertTrue("MultiPoint was not the expected value.", expectedMultiPoint.equalsExact(multiPoint));

      String multiPointText2 = "MULTIPOINT(191108 243242, 30000 40000)";

      viewDTO.setValue("testMultiPoint", multiPointText2);
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      multiPoint = (MultiPoint) viewDTO.getObjectValue("testMultiPoint");

      expectedMultiPoint = (MultiPoint) reader.read(multiPointText2);

      assertTrue("MultiPoint was not the expected value.", expectedMultiPoint.equalsExact(multiPoint));
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPointDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPointDAO.getId(), Operation.READ.name());
    }
  }

  /**
*
*/
  public void testMultiLineStringCRUDTypeUnsafe_WKT()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiLineStringDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiLineStringDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = (ViewDTO) tommyRequest.newMutable(GISMasterTestSetup.testVirtualMdViewDAO.definesType());

    try
    {
      String multiLineStringText1 = "MULTILINESTRING ((191232 243118, 191108 243242, 200000 250000, 275000 300000))";
      viewDTO.setValue("testMultiLineString", multiLineStringText1);
      tommyRequest.createSessionComponent(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());

      MultiLineString multiLineString = (MultiLineString) viewDTO.getObjectValue("testMultiLineString");

      WKTReader reader = new WKTReader();
      MultiLineString expectedMultiLineString = (MultiLineString) reader.read(multiLineStringText1);

      assertTrue("MultiLineString was not the expected value.", expectedMultiLineString.equalsExact(multiLineString));

      String multiLineStringText2 = "MULTILINESTRING ((189141 244158, 189265 244817, 100000 150000, 175000 200000))";

      viewDTO.setValue("testMultiLineString", multiLineStringText2);
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      multiLineString = (MultiLineString) viewDTO.getObjectValue("testMultiLineString");

      expectedMultiLineString = (MultiLineString) reader.read(multiLineStringText2);

      assertTrue("MultiLineString was not the expected value.", expectedMultiLineString.equalsExact(multiLineString));
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiLineStringDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiLineStringDAO.getId(), Operation.READ.name());
    }
  }

  /**
*
*/
  public void testMultiPolygonCRUDTypeUnsafe_WKT()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPolygonDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPolygonDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = (ViewDTO) tommyRequest.newMutable(GISMasterTestSetup.testVirtualMdViewDAO.definesType());

    try
    {
      String multiPolygonText1 = "MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2, 3 2, 3 3, 2 3,2 2)),((3 3,6 2,6 4,3 3)))";
      viewDTO.setValue("testMultiPolygon", multiPolygonText1);
      tommyRequest.createSessionComponent(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());

      MultiPolygon multiPolygon = (MultiPolygon) viewDTO.getObjectValue("testMultiPolygon");

      WKTReader reader = new WKTReader();
      MultiPolygon expectedMultiPolygon = (MultiPolygon) reader.read(multiPolygonText1);

      assertTrue("MultiPolygon was not the expected value.", expectedMultiPolygon.equalsExact(multiPolygon));

      String multiPolygonText2 = "MULTIPOLYGON(((1 1,5 1,10 10,1 5,1 1),(2 2, 3 2, 6 6, 2 3,2 2)),((3 3,6 2,6 4,3 3)))";

      viewDTO.setValue("testMultiPolygon", multiPolygonText2);
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      multiPolygon = (MultiPolygon) viewDTO.getObjectValue("testMultiPolygon");

      expectedMultiPolygon = (MultiPolygon) reader.read(multiPolygonText2);

      assertTrue("MultiPolygon was not the expected value.", expectedMultiPolygon.equalsExact(multiPolygon));
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPolygonDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPolygonDAO.getId(), Operation.READ.name());
    }
  }

  /**
*
*/
  public void testPointCRUDTypeSafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePointDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePointDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = null;

    try
    {
      Class<?> testClass = LoaderDecorator.load(GISMasterTestSetup.VIRTUAL_CLASS.getType() + TypeGeneratorInfo.DTO_SUFFIX);

      GeometryFactory geometryFactory = new GeometryFactory();
      Point point = geometryFactory.createPoint(new Coordinate(191232, 243118));

      viewDTO = (ViewDTO) testClass.getConstructor(ClientRequestIF.class).newInstance(tommyRequest);
      testClass.getMethod("setTestPoint", Point.class).invoke(viewDTO, point);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      point = (Point) testClass.getMethod("getTestPoint").invoke(viewDTO);

      assertEquals("X Coordinate on the point was not the expected value.", 191232d, point.getX());
      assertEquals("Y Coordinate on the point was not the expected value.", 243118d, point.getY());

      point = geometryFactory.createPoint(new Coordinate(191108, 243242));

      testClass.getMethod("setTestPoint", Point.class).invoke(viewDTO, point);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      point = (Point) testClass.getMethod("getTestPoint").invoke(viewDTO);

      assertEquals("X Coordinate on the point was not the expected value.", 191108d, point.getX());
      assertEquals("Y Coordinate on the point was not the expected value.", 243242d, point.getY());
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePointDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePointDAO.getId(), Operation.READ.name());
    }
  }

  /**
*
*/
  public void testLineStringCRUDTypeSafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeLineStringDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeLineStringDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = null;

    try
    {
      Class<?> testClass = LoaderDecorator.load(GISMasterTestSetup.VIRTUAL_CLASS.getType() + TypeGeneratorInfo.DTO_SUFFIX);

      GeometryFactory geometryFactory = new GeometryFactory();
      LineString lineString = geometryFactory.createLineString(new Coordinate[] { new Coordinate(191232, 243118), new Coordinate(191108, 243242) });

      viewDTO = (ViewDTO) testClass.getConstructor(ClientRequestIF.class).newInstance(tommyRequest);
      testClass.getMethod("setTestLineString", LineString.class).invoke(viewDTO, lineString);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      LineString newLineString = (LineString) testClass.getMethod("getTestLineString").invoke(viewDTO);

      assertTrue("Returned LineString from the database does not match the LineString that was set on the object.", lineString.equalsExact(newLineString));

      lineString = geometryFactory.createLineString(new Coordinate[] { new Coordinate(189141, 244158), new Coordinate(189265, 244817) });

      testClass.getMethod("setTestLineString", LineString.class).invoke(viewDTO, lineString);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      newLineString = (LineString) testClass.getMethod("getTestLineString").invoke(viewDTO);

      assertTrue("Returned LineString from the database does not match the LineString that was set on the object.", lineString.equalsExact(newLineString));
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeLineStringDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeLineStringDAO.getId(), Operation.READ.name());
    }
  }

  /**
*
*/
  public void testPolygonCRUDTypeSafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePolygonDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePolygonDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = null;

    try
    {
      Class<?> testClass = LoaderDecorator.load(GISMasterTestSetup.VIRTUAL_CLASS.getType() + TypeGeneratorInfo.DTO_SUFFIX);

      String polygonText1 = "POLYGON (( 10 10, 10 20, 20 20, 20 15, 10 10))";

      WKTReader reader = new WKTReader();
      Polygon polygon = (Polygon) reader.read(polygonText1);

      viewDTO = (ViewDTO) testClass.getConstructor(ClientRequestIF.class).newInstance(tommyRequest);
      testClass.getMethod("setTestPolygon", Polygon.class).invoke(viewDTO, polygon);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      Polygon newPolygon = (Polygon) testClass.getMethod("getTestPolygon").invoke(viewDTO);

      assertTrue("Returned Polygon from the database does not match the Polygon that was set on the object.", polygon.equalsExact(newPolygon));

      String polygonText2 = "POLYGON (( 10 10, 15 25, 40 40, 30 25, 10 10))";
      polygon = (Polygon) reader.read(polygonText2);

      testClass.getMethod("setTestPolygon", Polygon.class).invoke(viewDTO, polygon);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      newPolygon = (Polygon) testClass.getMethod("getTestPolygon").invoke(viewDTO);

      assertTrue("Returned Polygon from the database does not match the Polygon that was set on the object.", polygon.equalsExact(newPolygon));
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePolygonDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePolygonDAO.getId(), Operation.READ.name());
    }
  }

  /**
*
*/
  public void testMultiPointCRUDTypeSafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPointDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPointDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = null;

    try
    {
      Class<?> testClass = LoaderDecorator.load(GISMasterTestSetup.VIRTUAL_CLASS.getType() + TypeGeneratorInfo.DTO_SUFFIX);

      String multiPointText1 = "MULTIPOINT(191232 243118, 10000 20000)";

      WKTReader reader = new WKTReader();
      MultiPoint multiPoint = (MultiPoint) reader.read(multiPointText1);

      viewDTO = (ViewDTO) testClass.getConstructor(ClientRequestIF.class).newInstance(tommyRequest);
      testClass.getMethod("setTestMultiPoint", MultiPoint.class).invoke(viewDTO, multiPoint);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      MultiPoint newMultiPoint = (MultiPoint) testClass.getMethod("getTestMultiPoint").invoke(viewDTO);

      assertTrue("Returned MultiPoint from the database does not match the MultiPoint that was set on the object.", multiPoint.equalsExact(newMultiPoint));

      String polygonText2 = "MULTIPOINT(191108 243242, 30000 40000)";
      multiPoint = (MultiPoint) reader.read(polygonText2);

      testClass.getMethod("setTestMultiPoint", MultiPoint.class).invoke(viewDTO, multiPoint);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      newMultiPoint = (MultiPoint) testClass.getMethod("getTestMultiPoint").invoke(viewDTO);

      assertTrue("Returned MultiPoint from the database does not match the MultiPoint that was set on the object.", multiPoint.equalsExact(newMultiPoint));
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPointDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPointDAO.getId(), Operation.READ.name());
    }
  }

  /**
*
*/
  public void testMultiLineStringCRUDTypeSafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiLineStringDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiLineStringDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = null;

    try
    {
      Class<?> testClass = LoaderDecorator.load(GISMasterTestSetup.VIRTUAL_CLASS.getType() + TypeGeneratorInfo.DTO_SUFFIX);

      String multiPointText1 = "MULTILINESTRING ((191232 243118, 191108 243242, 200000 250000, 275000 300000))";

      WKTReader reader = new WKTReader();
      MultiLineString multiLineString = (MultiLineString) reader.read(multiPointText1);

      viewDTO = (ViewDTO) testClass.getConstructor(ClientRequestIF.class).newInstance(tommyRequest);
      testClass.getMethod("setTestMultiLineString", MultiLineString.class).invoke(viewDTO, multiLineString);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      MultiLineString newMultiLineString = (MultiLineString) testClass.getMethod("getTestMultiLineString").invoke(viewDTO);

      assertTrue("Returned MultiLineString from the database does not match the MultiLineString that was set on the object.", multiLineString.equalsExact(newMultiLineString));

      String polygonText2 = "MULTILINESTRING ((189141 244158, 189265 244817, 100000 150000, 175000 200000))";
      multiLineString = (MultiLineString) reader.read(polygonText2);

      testClass.getMethod("setTestMultiLineString", MultiLineString.class).invoke(viewDTO, multiLineString);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      newMultiLineString = (MultiLineString) testClass.getMethod("getTestMultiLineString").invoke(viewDTO);

      assertTrue("Returned MultiLineString from the database does not match the MultiLineString that was set on the object.", multiLineString.equalsExact(newMultiLineString));
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiLineStringDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiLineStringDAO.getId(), Operation.READ.name());
    }
  }

  /**
*
*/
  public void testMultiPolygonCRUDTypeSafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPolygonDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPolygonDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = null;

    try
    {
      Class<?> testClass = LoaderDecorator.load(GISMasterTestSetup.VIRTUAL_CLASS.getType() + TypeGeneratorInfo.DTO_SUFFIX);

      String multiPointText1 = "MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2, 3 2, 3 3, 2 3,2 2)),((3 3,6 2,6 4,3 3)))";

      WKTReader reader = new WKTReader();
      MultiPolygon multiPolygon = (MultiPolygon) reader.read(multiPointText1);

      viewDTO = (ViewDTO) testClass.getConstructor(ClientRequestIF.class).newInstance(tommyRequest);
      testClass.getMethod("setTestMultiPolygon", MultiPolygon.class).invoke(viewDTO, multiPolygon);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      MultiPolygon newMultiPolygon = (MultiPolygon) testClass.getMethod("getTestMultiPolygon").invoke(viewDTO);

      assertTrue("Returned MultiPolygon from the database does not match the MultiPolygon that was set on the object.", multiPolygon.equalsExact(newMultiPolygon));

      String polygonText2 = "MULTIPOLYGON(((1 1,5 1,10 10,1 5,1 1),(2 2, 3 2, 6 6, 2 3,2 2)),((3 3,6 2,6 4,3 3)))";
      multiPolygon = (MultiPolygon) reader.read(polygonText2);

      testClass.getMethod("setTestMultiPolygon", MultiPolygon.class).invoke(viewDTO, multiPolygon);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      newMultiPolygon = (MultiPolygon) testClass.getMethod("getTestMultiPolygon").invoke(viewDTO);

      assertTrue("Returned MultiPolygon from the database does not match the MultiPolygon that was set on the object.", multiPolygon.equalsExact(newMultiPolygon));
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPolygonDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPolygonDAO.getId(), Operation.READ.name());
    }
  }

  /**
*
*/
  public void testPointInvalidReadPermissionTypeSafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePointDAO.getId(), Operation.WRITE.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = null;

    try
    {
      Class<?> testClass = LoaderDecorator.load(GISMasterTestSetup.VIRTUAL_CLASS.getType() + TypeGeneratorInfo.DTO_SUFFIX);

      GeometryFactory geometryFactory = new GeometryFactory();
      Point point = geometryFactory.createPoint(new Coordinate(191232, 243118));

      viewDTO = (ViewDTO) testClass.getConstructor(ClientRequestIF.class).newInstance(tommyRequest);
      testClass.getMethod("setTestPoint", Point.class).invoke(viewDTO, point);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      point = (Point) testClass.getMethod("getTestPoint").invoke(viewDTO);

      assertEquals("User does not have adequate read permissions, yet was able to retreive the point.", null, point);
    }
    catch (Throwable e)
    {
      if (e instanceof InvocationTargetException)
      {
        InvocationTargetException inv = (InvocationTargetException) e;
        Throwable cause = inv.getCause();

        if (cause instanceof AttributeReadPermissionExceptionDTO)
        {
          // this is expected
        }
        else
        {
          fail(cause.getMessage());
        }
      }
      else
      {
        fail(e.getMessage());
      }
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePointDAO.getId(), Operation.WRITE.name());
    }
  }

  /**
*
*/
  public void testLineStringInvalidReadPermissionTypeSafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeLineStringDAO.getId(), Operation.WRITE.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = null;

    try
    {
      Class<?> testClass = LoaderDecorator.load(GISMasterTestSetup.VIRTUAL_CLASS.getType() + TypeGeneratorInfo.DTO_SUFFIX);

      GeometryFactory geometryFactory = new GeometryFactory();
      LineString lineString = geometryFactory.createLineString(new Coordinate[] { new Coordinate(191232, 243118), new Coordinate(191108, 243242) });

      viewDTO = (ViewDTO) testClass.getConstructor(ClientRequestIF.class).newInstance(tommyRequest);
      testClass.getMethod("setTestLineString", LineString.class).invoke(viewDTO, lineString);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      lineString = (LineString) testClass.getMethod("getTestLineString").invoke(viewDTO);

      assertEquals("User does not have adequate read permissions, yet was able to retreive the LineString.", null, lineString);
    }
    catch (Throwable e)
    {
      if (e instanceof InvocationTargetException)
      {
        InvocationTargetException inv = (InvocationTargetException) e;
        Throwable cause = inv.getCause();

        if (cause instanceof AttributeReadPermissionExceptionDTO)
        {
          // this is expected
        }
        else
        {
          fail(cause.getMessage());
        }
      }
      else
      {
        fail(e.getMessage());
      }
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeLineStringDAO.getId(), Operation.WRITE.name());
    }
  }

  /**
*
*/
  public void testPolygonInvalidReadPermissionTypeSafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePolygonDAO.getId(), Operation.WRITE.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = null;

    try
    {
      Class<?> testClass = LoaderDecorator.load(GISMasterTestSetup.VIRTUAL_CLASS.getType() + TypeGeneratorInfo.DTO_SUFFIX);

      String polygonText1 = "POLYGON (( 10 10, 10 20, 20 20, 20 15, 10 10))";

      WKTReader reader = new WKTReader();
      Polygon polygon = (Polygon) reader.read(polygonText1);

      viewDTO = (ViewDTO) testClass.getConstructor(ClientRequestIF.class).newInstance(tommyRequest);
      testClass.getMethod("setTestPolygon", Polygon.class).invoke(viewDTO, polygon);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      polygon = (Polygon) testClass.getMethod("getTestPolygon").invoke(viewDTO);

      assertEquals("User does not have adequate read permissions, yet was able to retreive the Polygon.", null, polygon);
    }
    catch (Throwable e)
    {
      if (e instanceof InvocationTargetException)
      {
        InvocationTargetException inv = (InvocationTargetException) e;
        Throwable cause = inv.getCause();

        if (cause instanceof AttributeReadPermissionExceptionDTO)
        {
          // this is expected
        }
        else
        {
          fail(cause.getMessage());
        }
      }
      else
      {
        fail(e.getMessage());
      }
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePolygonDAO.getId(), Operation.WRITE.name());
    }
  }

  /**
*
*/
  public void testMultiPointInvalidReadPermissionTypeSafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPointDAO.getId(), Operation.WRITE.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = null;

    try
    {
      Class<?> testClass = LoaderDecorator.load(GISMasterTestSetup.VIRTUAL_CLASS.getType() + TypeGeneratorInfo.DTO_SUFFIX);

      String multiPointText1 = "MULTIPOINT(191232 243118, 10000 20000)";

      WKTReader reader = new WKTReader();
      MultiPoint multiPoint = (MultiPoint) reader.read(multiPointText1);

      viewDTO = (ViewDTO) testClass.getConstructor(ClientRequestIF.class).newInstance(tommyRequest);
      testClass.getMethod("setTestMultiPoint", MultiPoint.class).invoke(viewDTO, multiPoint);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      multiPoint = (MultiPoint) testClass.getMethod("getTestMultiPoint").invoke(viewDTO);

      assertEquals("User does not have adequate read permissions, yet was able to retreive the MultiPoint.", null, multiPoint);
    }
    catch (Throwable e)
    {
      if (e instanceof InvocationTargetException)
      {
        InvocationTargetException inv = (InvocationTargetException) e;
        Throwable cause = inv.getCause();

        if (cause instanceof AttributeReadPermissionExceptionDTO)
        {
          // this is expected
        }
        else
        {
          fail(cause.getMessage());
        }
      }
      else
      {
        fail(e.getMessage());
      }
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPointDAO.getId(), Operation.WRITE.name());
    }
  }

  /**
*
*/
  public void testMultiLineStringInvalidReadPermissionTypeSafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiLineStringDAO.getId(), Operation.WRITE.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = null;

    try
    {
      Class<?> testClass = LoaderDecorator.load(GISMasterTestSetup.VIRTUAL_CLASS.getType() + TypeGeneratorInfo.DTO_SUFFIX);

      String multiLineStringText1 = "MULTILINESTRING ((191232 243118, 191108 243242, 200000 250000, 275000 300000))";

      WKTReader reader = new WKTReader();
      MultiLineString multiPoint = (MultiLineString) reader.read(multiLineStringText1);

      viewDTO = (ViewDTO) testClass.getConstructor(ClientRequestIF.class).newInstance(tommyRequest);
      testClass.getMethod("setTestMultiLineString", MultiLineString.class).invoke(viewDTO, multiPoint);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      multiPoint = (MultiLineString) testClass.getMethod("getTestMultiLineString").invoke(viewDTO);

      assertEquals("User does not have adequate read permissions, yet was able to retreive the MultiLineString.", null, multiPoint);
    }
    catch (Throwable e)
    {
      if (e instanceof InvocationTargetException)
      {
        InvocationTargetException inv = (InvocationTargetException) e;
        Throwable cause = inv.getCause();

        if (cause instanceof AttributeReadPermissionExceptionDTO)
        {
          // this is expected
        }
        else
        {
          fail(cause.getMessage());
        }
      }
      else
      {
        fail(e.getMessage());
      }
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiLineStringDAO.getId(), Operation.WRITE.name());
    }
  }

  /**
*
*/
  public void testMultiPolygonInvalidReadPermissionTypeSafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPolygonDAO.getId(), Operation.WRITE.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = null;

    try
    {
      Class<?> testClass = LoaderDecorator.load(GISMasterTestSetup.VIRTUAL_CLASS.getType() + TypeGeneratorInfo.DTO_SUFFIX);

      String multiPolygonText1 = "MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2, 3 2, 3 3, 2 3,2 2)),((3 3,6 2,6 4,3 3)))";

      WKTReader reader = new WKTReader();
      MultiPolygon multiPolygon = (MultiPolygon) reader.read(multiPolygonText1);

      viewDTO = (ViewDTO) testClass.getConstructor(ClientRequestIF.class).newInstance(tommyRequest);
      testClass.getMethod("setTestMultiPolygon", MultiPolygon.class).invoke(viewDTO, multiPolygon);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      multiPolygon = (MultiPolygon) testClass.getMethod("getTestMultiLineString").invoke(viewDTO);

      assertEquals("User does not have adequate read permissions, yet was able to retreive the MultiPolygon.", null, multiPolygon);
    }
    catch (Throwable e)
    {
      if (e instanceof InvocationTargetException)
      {
        InvocationTargetException inv = (InvocationTargetException) e;
        Throwable cause = inv.getCause();

        if (cause instanceof AttributeReadPermissionExceptionDTO)
        {
          // this is expected
        }
        else
        {
          fail(cause.getMessage());
        }
      }
      else
      {
        fail(e.getMessage());
      }
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPolygonDAO.getId(), Operation.WRITE.name());
    }
  }

  /**
*
*/
  public void testNullPointObjectTypeUnsafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePointDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePointDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = (ViewDTO) tommyRequest.newMutable(GISMasterTestSetup.testVirtualMdViewDAO.definesType());

    GeometryFactory geometryFactory = new GeometryFactory();

    try
    {
      viewDTO.setValue("testPoint", null);
      tommyRequest.createSessionComponent(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());

      Point point = (Point) viewDTO.getObjectValue("testPoint");
      assertEquals("Object created.  Point object should be null.", null, point);

      point = geometryFactory.createPoint(new Coordinate(191108, 243242));
      viewDTO.setValue("testPoint", point);
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      point = (Point) viewDTO.getObjectValue("testPoint");

      assertEquals("X Coordinate on the point was not the expected value.", 191108d, point.getX());
      assertEquals("Y Coordinate on the point was not the expected value.", 243242d, point.getY());

      viewDTO.setValue("testPoint", null);
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      point = (Point) viewDTO.getObjectValue("testPoint");
      assertEquals("Object Updated. Point object should be null.", null, point);

    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePointDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePointDAO.getId(), Operation.READ.name());
    }
  }

  /**
*
*/
  public void testNullLineStringObjectTypeUnsafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeLineStringDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeLineStringDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = (ViewDTO) tommyRequest.newMutable(GISMasterTestSetup.testVirtualMdViewDAO.definesType());

    GeometryFactory geometryFactory = new GeometryFactory();

    try
    {
      viewDTO.setValue("testLineString", null);
      tommyRequest.createSessionComponent(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());

      LineString lineString = (LineString) viewDTO.getObjectValue("testLineString");
      assertEquals("Object created.  LineString object should be null.", null, lineString);

      lineString = geometryFactory.createLineString(new Coordinate[] { new Coordinate(189141, 244158), new Coordinate(189265, 244817) });
      viewDTO.setValue("testLineString", lineString);
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      LineString newLineString = (LineString) viewDTO.getObjectValue("testLineString");

      assertTrue("Returned LineString from the database does not match the LineString that was set on the object.", lineString.equalsExact(newLineString));

      viewDTO.setValue("testLineString", null);
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      lineString = (LineString) viewDTO.getObjectValue("testLineString");
      assertEquals("Object Updated. LineString object should be null.", null, lineString);

    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeLineStringDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeLineStringDAO.getId(), Operation.READ.name());
    }
  }

  /**
*
*/
  public void testNullPolygonObjectTypeUnsafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePolygonDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePolygonDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = (ViewDTO) tommyRequest.newMutable(GISMasterTestSetup.testVirtualMdViewDAO.definesType());

    try
    {
      viewDTO.setValue("testPolygon", null);
      tommyRequest.createSessionComponent(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());

      Polygon polygon = (Polygon) viewDTO.getObjectValue("testPolygon");
      assertEquals("Object created.  Polygon object should be null.", null, polygon);

      String polygonText1 = "POLYGON (( 10 10, 10 20, 20 20, 20 15, 10 10))";

      WKTReader reader = new WKTReader();
      polygon = (Polygon) reader.read(polygonText1);

      viewDTO.setValue("testPolygon", polygon);
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      Polygon newPolygon = (Polygon) viewDTO.getObjectValue("testPolygon");

      assertTrue("Returned Polygon from the database does not match the Polygon that was set on the object.", polygon.equalsExact(newPolygon));

      viewDTO.setValue("testPolygon", null);
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      polygon = (Polygon) viewDTO.getObjectValue("testPolygon");
      assertEquals("Object Updated. Polygon object should be null.", null, polygon);

    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePolygonDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePolygonDAO.getId(), Operation.READ.name());
    }
  }

  /**
*
*/
  public void testNullMultiPointObjectTypeUnsafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPointDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPointDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = (ViewDTO) tommyRequest.newMutable(GISMasterTestSetup.testVirtualMdViewDAO.definesType());

    try
    {
      viewDTO.setValue("testMultiPoint", null);
      tommyRequest.createSessionComponent(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());

      MultiPoint multiPoint = (MultiPoint) viewDTO.getObjectValue("testMultiPoint");
      assertEquals("Object created.  MultiPoint object should be null.", null, multiPoint);

      String polygonText1 = "MULTIPOINT(191232 243118, 10000 20000)";

      WKTReader reader = new WKTReader();
      multiPoint = (MultiPoint) reader.read(polygonText1);

      viewDTO.setValue("testMultiPoint", multiPoint);
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      MultiPoint newMultiPoint = (MultiPoint) viewDTO.getObjectValue("testMultiPoint");

      assertTrue("Returned MultiPoint from the database does not match the MultiPoint that was set on the object.", multiPoint.equalsExact(newMultiPoint));

      viewDTO.setValue("testMultiPoint", null);
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      multiPoint = (MultiPoint) viewDTO.getObjectValue("testMultiPoint");
      assertEquals("Object Updated. MultiPoint object should be null.", null, multiPoint);

    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPointDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPointDAO.getId(), Operation.READ.name());
    }
  }

  /**
*
*/
  public void testNullMultiLineStringObjectTypeUnsafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiLineStringDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiLineStringDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = (ViewDTO) tommyRequest.newMutable(GISMasterTestSetup.testVirtualMdViewDAO.definesType());

    try
    {
      viewDTO.setValue("testMultiLineString", null);
      tommyRequest.createSessionComponent(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());

      MultiLineString multiLineString = (MultiLineString) viewDTO.getObjectValue("testMultiLineString");
      assertEquals("Object created.  MultiLineString object should be null.", null, multiLineString);

      String polygonText1 = "MULTILINESTRING ((191232 243118, 191108 243242, 200000 250000, 275000 300000))";

      WKTReader reader = new WKTReader();
      multiLineString = (MultiLineString) reader.read(polygonText1);

      viewDTO.setValue("testMultiLineString", multiLineString);
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      MultiLineString newMultiLineString = (MultiLineString) viewDTO.getObjectValue("testMultiLineString");

      assertTrue("Returned MultiLineString from the database does not match the MultiLineString that was set on the object.", multiLineString.equalsExact(newMultiLineString));

      viewDTO.setValue("testMultiLineString", null);
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      multiLineString = (MultiLineString) viewDTO.getObjectValue("testMultiLineString");
      assertEquals("Object Updated. MultiLineString object should be null.", null, multiLineString);

    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiLineStringDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiLineStringDAO.getId(), Operation.READ.name());
    }
  }

  /**
*
*/
  public void testNullMultiPolygonObjectTypeUnsafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPolygonDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPolygonDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = (ViewDTO) tommyRequest.newMutable(GISMasterTestSetup.testVirtualMdViewDAO.definesType());

    try
    {
      viewDTO.setValue("testMultiPolygon", null);
      tommyRequest.createSessionComponent(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());

      MultiPolygon multiPolygon = (MultiPolygon) viewDTO.getObjectValue("testMultiPolygon");
      assertEquals("Object created.  MultiPolygon object should be null.", null, multiPolygon);

      String polygonText1 = "MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2, 3 2, 3 3, 2 3,2 2)),((3 3,6 2,6 4,3 3)))";

      WKTReader reader = new WKTReader();
      multiPolygon = (MultiPolygon) reader.read(polygonText1);

      viewDTO.setValue("testMultiPolygon", multiPolygon);
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      MultiPolygon newMultiPolygon = (MultiPolygon) viewDTO.getObjectValue("testMultiPolygon");

      assertTrue("Returned MultiPolygon from the database does not match the MultiPolygon that was set on the object.", multiPolygon.equalsExact(newMultiPolygon));

      viewDTO.setValue("testMultiPolygon", null);
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      multiPolygon = (MultiPolygon) viewDTO.getObjectValue("testMultiPolygon");
      assertEquals("Object Updated. MultiPolygon object should be null.", null, multiPolygon);

    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPolygonDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPolygonDAO.getId(), Operation.READ.name());
    }
  }

  /**
*
*/
  public void testNullPointObjectTypeSafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePointDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePointDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = null;

    try
    {
      Class<?> testClass = LoaderDecorator.load(GISMasterTestSetup.VIRTUAL_CLASS.getType() + TypeGeneratorInfo.DTO_SUFFIX);

      GeometryFactory geometryFactory = new GeometryFactory();

      viewDTO = (ViewDTO) testClass.getConstructor(ClientRequestIF.class).newInstance(tommyRequest);
      testClass.getMethod("setTestPoint", Point.class).invoke(viewDTO, (Point) null);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      Point point = (Point) testClass.getMethod("getTestPoint").invoke(viewDTO);

      assertEquals("Object created.  Point object should be null.", null, point);

      point = geometryFactory.createPoint(new Coordinate(191108, 243242));

      testClass.getMethod("setTestPoint", Point.class).invoke(viewDTO, point);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      point = (Point) testClass.getMethod("getTestPoint").invoke(viewDTO);

      assertEquals("X Coordinate on the point was not the expected value.", 191108d, point.getX());
      assertEquals("Y Coordinate on the point was not the expected value.", 243242d, point.getY());

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      testClass.getMethod("setTestPoint", Point.class).invoke(viewDTO, (Point) null);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      point = (Point) testClass.getMethod("getTestPoint").invoke(viewDTO);

      assertEquals("Object created.  Point object should be null.", null, point);

    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePointDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePointDAO.getId(), Operation.READ.name());
    }
  }

  /**
*
*/
  public void testNullLineStringObjectTypeSafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeLineStringDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeLineStringDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = null;

    try
    {
      Class<?> testClass = LoaderDecorator.load(GISMasterTestSetup.VIRTUAL_CLASS.getType() + TypeGeneratorInfo.DTO_SUFFIX);

      GeometryFactory geometryFactory = new GeometryFactory();

      viewDTO = (ViewDTO) testClass.getConstructor(ClientRequestIF.class).newInstance(tommyRequest);
      testClass.getMethod("setTestLineString", LineString.class).invoke(viewDTO, (LineString) null);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      LineString lineString = (LineString) testClass.getMethod("getTestLineString").invoke(viewDTO);

      assertEquals("Object created.  LineString object should be null.", null, lineString);

      lineString = geometryFactory.createLineString(new Coordinate[] { new Coordinate(189141, 244158), new Coordinate(189265, 244817) });

      testClass.getMethod("setTestLineString", LineString.class).invoke(viewDTO, lineString);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      LineString newLineString = (LineString) testClass.getMethod("getTestLineString").invoke(viewDTO);

      assertTrue("Returned LineString from the database does not match the LineString that was set on the object.", lineString.equalsExact(newLineString));

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      testClass.getMethod("setTestLineString", LineString.class).invoke(viewDTO, (LineString) null);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      lineString = (LineString) testClass.getMethod("getTestLineString").invoke(viewDTO);

      assertEquals("Object created.  LineString object should be null.", null, lineString);

    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeLineStringDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeLineStringDAO.getId(), Operation.READ.name());
    }
  }

  /**
*
*/
  public void testNullPolygonObjectTypeSafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePolygonDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePolygonDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = null;

    try
    {
      Class<?> testClass = LoaderDecorator.load(GISMasterTestSetup.VIRTUAL_CLASS.getType() + TypeGeneratorInfo.DTO_SUFFIX);

      viewDTO = (ViewDTO) testClass.getConstructor(ClientRequestIF.class).newInstance(tommyRequest);
      testClass.getMethod("setTestPolygon", Polygon.class).invoke(viewDTO, (Polygon) null);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      Polygon polygon = (Polygon) testClass.getMethod("getTestPolygon").invoke(viewDTO);

      assertEquals("Object created.  Polygon object should be null.", null, polygon);

      String polygonText1 = "POLYGON (( 10 10, 10 20, 20 20, 20 15, 10 10))";

      WKTReader reader = new WKTReader();
      polygon = (Polygon) reader.read(polygonText1);

      testClass.getMethod("setTestPolygon", Polygon.class).invoke(viewDTO, polygon);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      Polygon newPolygon = (Polygon) testClass.getMethod("getTestPolygon").invoke(viewDTO);

      assertTrue("Returned Polygon from the database does not match the Polygon that was set on the object.", polygon.equalsExact(newPolygon));

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      testClass.getMethod("setTestPolygon", Polygon.class).invoke(viewDTO, (Polygon) null);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      polygon = (Polygon) testClass.getMethod("getTestPolygon").invoke(viewDTO);

      assertEquals("Object created.  Polygon object should be null.", null, polygon);

    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePolygonDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePolygonDAO.getId(), Operation.READ.name());
    }
  }

  /**
*
*/
  public void testNullMultiPointObjectTypeSafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPointDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPointDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = null;

    try
    {
      Class<?> testClass = LoaderDecorator.load(GISMasterTestSetup.VIRTUAL_CLASS.getType() + TypeGeneratorInfo.DTO_SUFFIX);

      viewDTO = (ViewDTO) testClass.getConstructor(ClientRequestIF.class).newInstance(tommyRequest);
      testClass.getMethod("setTestMultiPoint", MultiPoint.class).invoke(viewDTO, (MultiPoint) null);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      MultiPoint multiPoint = (MultiPoint) testClass.getMethod("getTestMultiPoint").invoke(viewDTO);

      assertEquals("Object created.  MultiPoint object should be null.", null, multiPoint);

      String polygonText1 = "MULTIPOINT(191108 243242, 30000 40000)";

      WKTReader reader = new WKTReader();
      multiPoint = (MultiPoint) reader.read(polygonText1);

      testClass.getMethod("setTestMultiPoint", MultiPoint.class).invoke(viewDTO, multiPoint);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      MultiPoint newMultiPoint = (MultiPoint) testClass.getMethod("getTestMultiPoint").invoke(viewDTO);

      assertTrue("Returned MultiPoint from the database does not match the MultiPoint that was set on the object.", multiPoint.equalsExact(newMultiPoint));

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      testClass.getMethod("setTestMultiPoint", MultiPoint.class).invoke(viewDTO, (MultiPoint) null);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      multiPoint = (MultiPoint) testClass.getMethod("getTestMultiPoint").invoke(viewDTO);

      assertEquals("Object created.  MultiPoint object should be null.", null, multiPoint);

    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPointDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPointDAO.getId(), Operation.READ.name());
    }
  }

  /**
*
*/
  public void testNullMultiLineStringObjectTypeSafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiLineStringDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiLineStringDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = null;

    try
    {
      Class<?> testClass = LoaderDecorator.load(GISMasterTestSetup.VIRTUAL_CLASS.getType() + TypeGeneratorInfo.DTO_SUFFIX);

      viewDTO = (ViewDTO) testClass.getConstructor(ClientRequestIF.class).newInstance(tommyRequest);
      testClass.getMethod("setTestMultiLineString", MultiLineString.class).invoke(viewDTO, (MultiLineString) null);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      MultiLineString multiLineString = (MultiLineString) testClass.getMethod("getTestMultiLineString").invoke(viewDTO);

      assertEquals("Object created.  MultiLineString object should be null.", null, multiLineString);

      String multiLineStringText1 = "MULTILINESTRING ((189141 244158, 189265 244817, 100000 150000, 175000 200000))";

      WKTReader reader = new WKTReader();
      multiLineString = (MultiLineString) reader.read(multiLineStringText1);

      testClass.getMethod("setTestMultiLineString", MultiLineString.class).invoke(viewDTO, multiLineString);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      MultiLineString newMultiLineString = (MultiLineString) testClass.getMethod("getTestMultiLineString").invoke(viewDTO);

      assertTrue("Returned MultiLineString from the database does not match the MultiLineString that was set on the object.", multiLineString.equalsExact(newMultiLineString));

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      testClass.getMethod("setTestMultiLineString", MultiLineString.class).invoke(viewDTO, (MultiLineString) null);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      multiLineString = (MultiLineString) testClass.getMethod("getTestMultiLineString").invoke(viewDTO);

      assertEquals("Object created.  MultiLineString object should be null.", null, multiLineString);

    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiLineStringDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiLineStringDAO.getId(), Operation.READ.name());
    }
  }

  /**
*
*/
  public void testNullMultiPolygonObjectTypeSafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPolygonDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPolygonDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = null;

    try
    {
      Class<?> testClass = LoaderDecorator.load(GISMasterTestSetup.VIRTUAL_CLASS.getType() + TypeGeneratorInfo.DTO_SUFFIX);

      viewDTO = (ViewDTO) testClass.getConstructor(ClientRequestIF.class).newInstance(tommyRequest);
      testClass.getMethod("setTestMultiPolygon", MultiPolygon.class).invoke(viewDTO, (MultiPolygon) null);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      MultiPolygon multiPolygon = (MultiPolygon) testClass.getMethod("getTestMultiPolygon").invoke(viewDTO);

      assertEquals("Object created.  MultiPolygon object should be null.", null, multiPolygon);

      String multiPolygonText1 = "MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2, 3 2, 3 3, 2 3,2 2)),((3 3,6 2,6 4,3 3)))";

      WKTReader reader = new WKTReader();
      multiPolygon = (MultiPolygon) reader.read(multiPolygonText1);

      testClass.getMethod("setTestMultiPolygon", MultiPolygon.class).invoke(viewDTO, multiPolygon);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      MultiPolygon newMultiPolygon = (MultiPolygon) testClass.getMethod("getTestMultiPolygon").invoke(viewDTO);

      assertTrue("Returned MultiPolygon from the database does not match the MultiPolygon that was set on the object.", multiPolygon.equalsExact(newMultiPolygon));

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      testClass.getMethod("setTestMultiPolygon", MultiPolygon.class).invoke(viewDTO, (MultiLineString) null);
      testClass.getMethod("apply").invoke(viewDTO);

      viewDTO = (ViewDTO) testClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, tommyRequest, viewDTO.getId());
      multiPolygon = (MultiPolygon) testClass.getMethod("getTestMultiPolygon").invoke(viewDTO);

      assertEquals("Object created.  MultiPolygon object should be null.", null, multiPolygon);

    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPolygonDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPolygonDAO.getId(), Operation.READ.name());
    }
  }

  /**
*
*/
  public void testEmptyStringPointTypeUnsafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePointDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePointDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = (ViewDTO) tommyRequest.newMutable(GISMasterTestSetup.testVirtualMdViewDAO.definesType());

    GeometryFactory geometryFactory = new GeometryFactory();

    try
    {
      viewDTO.setValue("testPoint", "");
      tommyRequest.createSessionComponent(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());

      Point point = (Point) viewDTO.getObjectValue("testPoint");
      assertEquals("Object created.  Point object should be null.", null, point);

      point = geometryFactory.createPoint(new Coordinate(191108, 243242));
      viewDTO.setValue("testPoint", point);
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      point = (Point) viewDTO.getObjectValue("testPoint");

      assertEquals("X Coordinate on the point was not the expected value.", 191108d, point.getX());
      assertEquals("Y Coordinate on the point was not the expected value.", 243242d, point.getY());

      viewDTO.setValue("testPoint", "");
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      point = (Point) viewDTO.getObjectValue("testPoint");
      assertEquals("Object Updated. Point object should be null.", null, point);
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePointDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePointDAO.getId(), Operation.READ.name());
    }
  }

  /**
   *
   */
  @Test
  @Request
  public void testEmptyStringLineStringTypeUnsafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeLineStringDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeLineStringDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = (ViewDTO) tommyRequest.newMutable(GISMasterTestSetup.testVirtualMdViewDAO.definesType());

    GeometryFactory geometryFactory = new GeometryFactory();

    try
    {
      viewDTO.setValue("testLineString", "");
      tommyRequest.createSessionComponent(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());

      LineString lineString = (LineString) viewDTO.getObjectValue("testLineString");
      assertEquals("Object created.  LineString object should be null.", null, lineString);

      lineString = geometryFactory.createLineString(new Coordinate[] { new Coordinate(189141, 244158), new Coordinate(189265, 244817) });
      viewDTO.setValue("testLineString", lineString);
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      LineString newLineString = (LineString) viewDTO.getObjectValue("testLineString");

      assertTrue("Returned LineString from the database does not match the LineString that was set on the object.", lineString.equalsExact(newLineString));

      viewDTO.setValue("testLineString", "");
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      lineString = (LineString) viewDTO.getObjectValue("testLineString");
      assertEquals("Object Updated. LineString object should be null.", null, lineString);
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeLineStringDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeLineStringDAO.getId(), Operation.READ.name());
    }
  }

  /**
   *
   */
  @Test
  @Request
  public void testEmptyStringPolygonTypeUnsafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePolygonDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePolygonDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = (ViewDTO) tommyRequest.newMutable(GISMasterTestSetup.testVirtualMdViewDAO.definesType());

    try
    {
      viewDTO.setValue("testPolygon", "");
      tommyRequest.createSessionComponent(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());

      Polygon polygon = (Polygon) viewDTO.getObjectValue("testPolygon");
      assertEquals("Object created.  Polygon object should be null.", null, polygon);

      String polygonText1 = "POLYGON (( 10 10, 10 20, 20 20, 20 15, 10 10))";

      WKTReader reader = new WKTReader();
      polygon = (Polygon) reader.read(polygonText1);
      viewDTO.setValue("testPolygon", polygon);
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      Polygon newPolygon = (Polygon) viewDTO.getObjectValue("testPolygon");

      assertTrue("Returned Polygon from the database does not match the Polygon that was set on the object.", polygon.equalsExact(newPolygon));

      viewDTO.setValue("testPolygon", "");
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      polygon = (Polygon) viewDTO.getObjectValue("testPolygon");
      assertEquals("Object Updated. Polygon object should be null.", null, polygon);
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePolygonDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePolygonDAO.getId(), Operation.READ.name());
    }
  }

  /**
   *
   */
  @Test
  @Request
  public void testEmptyStringMultiPointTypeUnsafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPointDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPointDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = (ViewDTO) tommyRequest.newMutable(GISMasterTestSetup.testVirtualMdViewDAO.definesType());

    try
    {
      viewDTO.setValue("testMultiPoint", "");
      tommyRequest.createSessionComponent(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());

      MultiPoint multiPoint = (MultiPoint) viewDTO.getObjectValue("testMultiPoint");
      assertEquals("Object created.  MultiPoint object should be null.", null, multiPoint);

      String multiPointText1 = "MULTIPOINT(191232 243118, 10000 20000)";

      WKTReader reader = new WKTReader();
      multiPoint = (MultiPoint) reader.read(multiPointText1);
      viewDTO.setValue("testMultiPoint", multiPoint);
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      MultiPoint newMultiPoint = (MultiPoint) viewDTO.getObjectValue("testMultiPoint");

      assertTrue("Returned MultiPoint from the database does not match the MultiPoint that was set on the object.", multiPoint.equalsExact(newMultiPoint));

      viewDTO.setValue("testMultiPoint", "");
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      multiPoint = (MultiPoint) viewDTO.getObjectValue("testMultiPoint");
      assertEquals("Object Updated. MultiPoint object should be null.", null, multiPoint);
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPointDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPointDAO.getId(), Operation.READ.name());
    }
  }

  /**
   *
   */
  @Test
  @Request
  public void testEmptyStringMultiLineStringTypeUnsafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiLineStringDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiLineStringDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = (ViewDTO) tommyRequest.newMutable(GISMasterTestSetup.testVirtualMdViewDAO.definesType());

    try
    {
      viewDTO.setValue("testMultiLineString", "");
      tommyRequest.createSessionComponent(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());

      MultiLineString multiLineString = (MultiLineString) viewDTO.getObjectValue("testMultiLineString");
      assertEquals("Object created.  MultiLineString object should be null.", null, multiLineString);

      String multiLineString1 = "MULTILINESTRING ((191232 243118, 191108 243242, 200000 250000, 275000 300000))";

      WKTReader reader = new WKTReader();
      multiLineString = (MultiLineString) reader.read(multiLineString1);
      viewDTO.setValue("testMultiLineString", multiLineString);
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      MultiLineString newMultiLineString = (MultiLineString) viewDTO.getObjectValue("testMultiLineString");

      assertTrue("Returned MultiLineString from the database does not match the MultiLineString that was set on the object.", multiLineString.equalsExact(newMultiLineString));

      viewDTO.setValue("testMultiLineString", "");
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      multiLineString = (MultiLineString) viewDTO.getObjectValue("testMultiLineString");
      assertEquals("Object Updated. MultiLineString object should be null.", null, multiLineString);
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiLineStringDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiLineStringDAO.getId(), Operation.READ.name());
    }
  }

  /**
   *
   */
  @Test
  @Request
  public void testEmptyStringMultiPolygonTypeUnsafe()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPolygonDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPolygonDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = (ViewDTO) tommyRequest.newMutable(GISMasterTestSetup.testVirtualMdViewDAO.definesType());

    try
    {
      viewDTO.setValue("testMultiPolygon", "");
      tommyRequest.createSessionComponent(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());

      MultiPolygon multiPolygon = (MultiPolygon) viewDTO.getObjectValue("testMultiPolygon");
      assertEquals("Object created.  MultiPolygon object should be null.", null, multiPolygon);

      String multiPolygon1 = "MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2, 3 2, 3 3, 2 3,2 2)),((3 3,6 2,6 4,3 3)))";

      WKTReader reader = new WKTReader();
      multiPolygon = (MultiPolygon) reader.read(multiPolygon1);
      viewDTO.setValue("testMultiPolygon", multiPolygon);
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      MultiPolygon newMultiPolygon = (MultiPolygon) viewDTO.getObjectValue("testMultiPolygon");

      assertTrue("Returned MultiPolygon from the database does not match the MultiPolygon that was set on the object.", multiPolygon.equalsExact(newMultiPolygon));

      viewDTO.setValue("testMultiPolygon", "");
      tommyRequest.update(viewDTO);

      viewDTO = (ViewDTO) tommyRequest.get(viewDTO.getId());
      multiPolygon = (MultiPolygon) viewDTO.getObjectValue("testMultiPolygon");
      assertEquals("Object Updated. MultiPolygon object should be null.", null, multiPolygon);
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPolygonDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPolygonDAO.getId(), Operation.READ.name());
    }
  }

  @Test
  @Request
  public void testInvalidPointStringObject()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePointDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePointDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = (ViewDTO) tommyRequest.newMutable(GISMasterTestSetup.testVirtualMdViewDAO.definesType());

    try
    {
      viewDTO.setValue("testPoint", "Total Garbage");
      tommyRequest.createSessionComponent(viewDTO);

      fail("An invalid WKT value was set on a point attribute.");
    }
    catch (AttributePointParseExceptionDTO e)
    {
      // this is expected
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePointDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePointDAO.getId(), Operation.READ.name());
    }
  }

  @Test
  @Request
  public void testInvalidLineStringStringObject()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeLineStringDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeLineStringDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = (ViewDTO) tommyRequest.newMutable(GISMasterTestSetup.testVirtualMdViewDAO.definesType());

    try
    {
      viewDTO.setValue("testLineString", "Total Garbage");
      tommyRequest.createSessionComponent(viewDTO);

      fail("An invalid WKT value was set on a LineString attribute.");
    }
    catch (AttributeLineStringParseExceptionDTO e)
    {
      // this is expected
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeLineStringDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeLineStringDAO.getId(), Operation.READ.name());
    }
  }

  @Test
  @Request
  public void testInvalidPolygonString()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePolygonDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePolygonDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = (ViewDTO) tommyRequest.newMutable(GISMasterTestSetup.testVirtualMdViewDAO.definesType());

    try
    {
      viewDTO.setValue("testPolygon", "Total Garbage");
      tommyRequest.createSessionComponent(viewDTO);

      fail("An invalid WKT value was set on a Polygon attribute.");
    }
    catch (AttributePolygonParseExceptionDTO e)
    {
      // this is expected
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePolygonDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributePolygonDAO.getId(), Operation.READ.name());
    }
  }

  @Test
  @Request
  public void testInvalidMultiPointString()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPointDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPointDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = (ViewDTO) tommyRequest.newMutable(GISMasterTestSetup.testVirtualMdViewDAO.definesType());

    try
    {
      viewDTO.setValue("testMultiPoint", "Total Garbage");
      tommyRequest.createSessionComponent(viewDTO);

      fail("An invalid WKT value was set on a MultiPoint attribute.");
    }
    catch (AttributeMultiPointParseExceptionDTO e)
    {
      // this is expected
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPointDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiPointDAO.getId(), Operation.READ.name());
    }
  }

  @Test
  @Request
  public void testInvalidMultiLineStringString()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiLineStringDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiLineStringDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = (ViewDTO) tommyRequest.newMutable(GISMasterTestSetup.testVirtualMdViewDAO.definesType());

    try
    {
      viewDTO.setValue("testMultiLineString", "Total Garbage");
      tommyRequest.createSessionComponent(viewDTO);

      fail("An invalid WKT value was set on a MultiLineString attribute.");
    }
    catch (AttributeMultiLineStringParseExceptionDTO e)
    {
      // this is expected
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiLineStringDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiLineStringDAO.getId(), Operation.READ.name());
    }
  }

  @Test
  @Request
  public void testInvalidMultiPolygonString()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiLineStringDAO.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiLineStringDAO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    ViewDTO viewDTO = (ViewDTO) tommyRequest.newMutable(GISMasterTestSetup.testVirtualMdViewDAO.definesType());

    try
    {
      viewDTO.setValue("testMultiPolygon", "Total Garbage");
      tommyRequest.createSessionComponent(viewDTO);

      fail("An invalid WKT value was set on a MultiPolygon attribute.");
    }
    catch (AttributeMultiPolygonParseExceptionDTO e)
    {
      // this is expected
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), GISMasterTestSetup.testVirtualMdViewDAO.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiLineStringDAO.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), GISMasterTestSetup.mdAttributeMultiLineStringDAO.getId(), Operation.READ.name());
    }
  }
}
