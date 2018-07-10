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
package com.runwaysdk.gis.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.session.StandardSession;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.AttributeNotificationDTO;
import com.runwaysdk.ClientSession;
import com.runwaysdk.business.DummyRequest;
import com.runwaysdk.business.DummyResponse;
import com.runwaysdk.constants.ClientConstants;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.MdActionInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdControllerInfo;
import com.runwaysdk.constants.MdParameterInfo;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.controller.ServletDispatcher;
import com.runwaysdk.dataaccess.metadata.MdActionDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdControllerDAO;
import com.runwaysdk.dataaccess.metadata.MdPackage;
import com.runwaysdk.dataaccess.metadata.MdParameterDAO;
import com.runwaysdk.gis.AttributeLineStringParseProblemDTO;
import com.runwaysdk.gis.AttributeMultiLineStringParseProblemDTO;
import com.runwaysdk.gis.AttributeMultiPointParseProblemDTO;
import com.runwaysdk.gis.AttributeMultiPolygonParseProblemDTO;
import com.runwaysdk.gis.AttributePointParseProblemDTO;
import com.runwaysdk.gis.AttributePolygonParseProblemDTO;
import com.runwaysdk.gis.constants.MdAttributeLineStringInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiLineStringInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiPointInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiPolygonInfo;
import com.runwaysdk.gis.constants.MdAttributePointInfo;
import com.runwaysdk.gis.constants.MdAttributePolygonInfo;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeLineStringDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiLineStringDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiPointDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiPolygonDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributePointDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributePolygonDAO;
import com.runwaysdk.session.Request;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class GISControllerGenTest
{
  class MockSession extends StandardSession
  {
    /**
     * 
     */
    private static final long       serialVersionUID = 3674944194141854328L;

    private HashMap<String, Object> attributes;

    public MockSession()
    {
      super(null);
      this.attributes = new HashMap<String, Object>();
    }

    public Object getAttribute(String arg0)
    {
      return attributes.get(arg0);
    }

    public void setAttribute(String arg0, Object arg1)
    {
      attributes.put(arg0, arg1);
    }
  }

  class MockRequest extends DummyRequest
  {
    private String                    method;

    private HashMap<String, String[]> parameters;

    private HashMap<String, Object>   attributes;

    // private HashMap<String, String[]> parameterValues;

    private MockSession               session;

    public MockRequest(String method, String uri)
    {
      this.method = method;
      this.parameters = new HashMap<String, String[]>();
      this.attributes = new HashMap<String, Object>();
      this.session = new MockSession();

      String[] split = uri.split("/");

      this.setRequestURI(uri);
      this.setContextPath("/" + split[1]);
      this.setServletPath("/" + split[2]);
    }

    @Override
    public Map<String, String[]> getParameterMap()
    {
      return parameters;
    }

    @Override
    public String getMethod()
    {
      return method;
    }

    @Override
    public String getParameter(String name)
    {
      String[] params = parameters.get(name);
      if (params != null && params.length > 0)
      {
        return params[0];
      }
      else
      {
        return null;
      }
    }

    public void setParameter(String name, String value)
    {
      parameters.put(name, new String[] { value });
    }

    @Override
    public String[] getParameterValues(String name)
    {
      return parameters.get(name);
      // return parameterValues.get(name);
    }

    public void setParameterValues(String name, String[] values)
    {
      parameters.put(name, values);
      // parameterValues.put(name, values);
    }

    @Override
    public Locale getLocale()
    {
      return Locale.US;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Enumeration getParameterNames()
    {
      StringBuffer buffer = new StringBuffer();

      for (String key : parameters.keySet())
      {
        buffer.append(" " + key);
      }

      StringTokenizer tok = new StringTokenizer(buffer.toString().replaceFirst(" ", ""));

      return tok;
    }

    @Override
    public Object getAttribute(String name)
    {
      return attributes.get(name);
    }

    @Override
    public void setAttribute(String name, Object value)
    {
      attributes.put(name, value);
    }

    @Override
    public HttpSession getSession()
    {
      return session;
    }
  }

  private static final String    pack = "test.generated";

  private static MdControllerDAO mdController;

  private static MdBusinessDAO   mdBusiness;

  private static String          testActionUri;

  @BeforeClass
  @Request
  public static void classSetUp()
  {
    mdBusiness = MdBusinessDAO.newInstance();
    mdBusiness.setValue(MdBusinessInfo.PACKAGE, pack);
    mdBusiness.setValue(MdBusinessInfo.NAME, "TestBusiness");
    mdBusiness.setValue(MdBusinessInfo.DISPLAY_LABEL, "Test Business");
    mdBusiness.apply();

    MdAttributePointDAO mdAttributePoint = MdAttributePointDAO.newInstance();
    mdAttributePoint.setValue(MdAttributePointInfo.NAME, "testPoint");
    mdAttributePoint.setValue(MdAttributePointInfo.DISPLAY_LABEL, "Test Point");
    mdAttributePoint.setValue(MdAttributePointInfo.DEFINING_MD_CLASS, mdBusiness.getId());
    mdAttributePoint.setValue(MdAttributePointInfo.SRID, "4326");
    mdAttributePoint.apply();

    MdAttributeLineStringDAO mdAttributeLineString = MdAttributeLineStringDAO.newInstance();
    mdAttributeLineString.setValue(MdAttributeLineStringInfo.NAME, "testLine");
    mdAttributeLineString.setValue(MdAttributeLineStringInfo.DISPLAY_LABEL, "Test Line");
    mdAttributeLineString.setValue(MdAttributeLineStringInfo.DEFINING_MD_CLASS, mdBusiness.getId());
    mdAttributeLineString.setValue(MdAttributeLineStringInfo.SRID, "4326");
    mdAttributeLineString.apply();

    MdAttributePolygonDAO mdAttributePolygon = MdAttributePolygonDAO.newInstance();
    mdAttributePolygon.setValue(MdAttributePolygonInfo.NAME, "testPolygon");
    mdAttributePolygon.setValue(MdAttributePolygonInfo.DISPLAY_LABEL, "Test Polygon");
    mdAttributePolygon.setValue(MdAttributePolygonInfo.DEFINING_MD_CLASS, mdBusiness.getId());
    mdAttributePolygon.setValue(MdAttributePolygonInfo.SRID, "4326");
    mdAttributePolygon.apply();

    MdAttributeMultiLineStringDAO mdAttributeMultiLineString = MdAttributeMultiLineStringDAO.newInstance();
    mdAttributeMultiLineString.setValue(MdAttributeMultiLineStringInfo.NAME, "testMultiLine");
    mdAttributeMultiLineString.setValue(MdAttributeMultiLineStringInfo.DISPLAY_LABEL, "Test Multiline");
    mdAttributeMultiLineString.setValue(MdAttributeMultiLineStringInfo.DEFINING_MD_CLASS, mdBusiness.getId());
    mdAttributeMultiLineString.setValue(MdAttributeMultiLineStringInfo.SRID, "4326");
    mdAttributeMultiLineString.apply();

    MdAttributeMultiPointDAO mdAttributeMultiPoint = MdAttributeMultiPointDAO.newInstance();
    mdAttributeMultiPoint.setValue(MdAttributeMultiPointInfo.NAME, "testMultiPoint");
    mdAttributeMultiPoint.setValue(MdAttributeMultiPointInfo.DISPLAY_LABEL, "Test Multi Point");
    mdAttributeMultiPoint.setValue(MdAttributeMultiPointInfo.DEFINING_MD_CLASS, mdBusiness.getId());
    mdAttributeMultiPoint.setValue(MdAttributeMultiPointInfo.SRID, "4326");
    mdAttributeMultiPoint.apply();

    MdAttributeMultiPolygonDAO mdAttributeMultiPolygon = MdAttributeMultiPolygonDAO.newInstance();
    mdAttributeMultiPolygon.setValue(MdAttributeMultiPolygonInfo.NAME, "testMultiPolygon");
    mdAttributeMultiPolygon.setValue(MdAttributeMultiPolygonInfo.DISPLAY_LABEL, "Test Multi Polygon");
    mdAttributeMultiPolygon.setValue(MdAttributeMultiPolygonInfo.DEFINING_MD_CLASS, mdBusiness.getId());
    mdAttributeMultiPolygon.setValue(MdAttributeMultiPolygonInfo.SRID, "4326");
    mdAttributeMultiPolygon.apply();

    mdController = MdControllerDAO.newInstance();
    mdController.setValue(MdControllerInfo.NAME, "TestController");
    mdController.setValue(MdControllerInfo.PACKAGE, pack);
    mdController.setValue(MdControllerInfo.DISPLAY_LABEL, "Test Controller");
    mdController.setValue(MdControllerInfo.DESCRIPTION, "Test Controller");
    mdController.apply();

    MdActionDAO mdAction = MdActionDAO.newInstance();
    mdAction.setValue(MdActionInfo.NAME, "testAction");
    mdAction.setValue(MdActionInfo.ENCLOSING_MD_CONTROLLER, mdController.getId());
    mdAction.setValue(MdActionInfo.DISPLAY_LABEL, "Test Action");
    mdAction.setValue(MdActionInfo.DESCRIPTION, "Test Action");
    mdAction.setValue(MdActionInfo.IS_POST, MdAttributeBooleanInfo.TRUE);
    mdAction.apply();

    MdParameterDAO seqParameter = MdParameterDAO.newInstance();
    seqParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, mdAction.getId());
    seqParameter.setValue(MdParameterInfo.TYPE, mdBusiness.definesType());
    seqParameter.setValue(MdParameterInfo.NAME, "dto");
    seqParameter.setValue(MdParameterInfo.ORDER, "0");
    seqParameter.setValue(MdParameterInfo.DISPLAY_LABEL, "dto");
    seqParameter.apply();

    testActionUri = "/contextPath/" + mdController.definesType() + "." + mdAction.getName() + MdActionInfo.ACTION_SUFFIX;

    finalizeSetup();
  }

  private static void finalizeSetup()
  {
    MdControllerDAO updateController = MdControllerDAO.get(mdController.getId()).getBusinessDAO();
    updateController.setValue(MdControllerInfo.STUB_SOURCE, getSource());
    updateController.apply();
  }

  @AfterClass
  @Request
  public static void classTearDown()
  {
    new MdPackage(pack).delete();
  }

  @Test
  @Request
  public void testComparison() throws ParseException
  {
    WKTReader reader = new WKTReader();
    MultiPolygon multiPolygon = (MultiPolygon) reader.read("MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2, 3 2, 3 3, 2 3,2 2)),((3 3,6 2,6 4,3 3)))");
    MultiPolygon multiPolygon2 = (MultiPolygon) reader.read("MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2, 3 2, 3 3, 2 3,2 2)),((3 3,6 2,6 4,3 3)))");

    assertTrue(multiPolygon.equalsExact(multiPolygon2));
  }

  @Test
  @Request
  public void testDispatcherBusinessParameters() throws Exception
  {
    ClientSession systemSession = ClientSession.createUserSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { Locale.US });
    ClientRequestIF clientRequest = systemSession.getRequest();

    MockRequest req = new MockRequest("POST", testActionUri);
    HttpServletResponse resp = new DummyResponse();

    req.setAttribute(ClientConstants.CLIENTSESSION, systemSession);
    req.setAttribute(ClientConstants.CLIENTREQUEST, clientRequest);
    req.setParameter("dto.isNew", MdAttributeBooleanInfo.TRUE);
    req.setParameter("dto.componentId", "unknown");
    req.setParameter("dto.testPoint", "POINT(191232 243118)");
    req.setParameter("dto.testLine", "LINESTRING (191232 243118, 191108 243242)");
    req.setParameter("dto.testPolygon", "POLYGON (( 10 10, 10 20, 20 20, 20 15, 10 10))");
    req.setParameter("dto.testMultiPoint", "MULTIPOINT(191232 243118, 10000 20000)");
    req.setParameter("dto.testMultiLine", "MULTILINESTRING ((191232 243118, 191108 243242, 200000 250000, 275000 300000))");
    req.setParameter("dto.testMultiPolygon", "MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2, 3 2, 3 3, 2 3,2 2)),((3 3,6 2,6 4,3 3)))");

    try
    {
      new ServletDispatcher().service(req, resp);
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      systemSession.logout();
    }
  }

  @Test
  @Request
  public void testPropigatePointParseProblem() throws Exception
  {
    ClientSession systemSession = ClientSession.createUserSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { Locale.US });
    ClientRequestIF clientRequest = systemSession.getRequest();

    MockRequest req = new MockRequest("POST", testActionUri);
    HttpServletResponse resp = new DummyResponse();

    req.setAttribute(ClientConstants.CLIENTSESSION, systemSession);
    req.setAttribute(ClientConstants.CLIENTREQUEST, clientRequest);
    req.setParameter("dto.isNew", MdAttributeBooleanInfo.TRUE);
    req.setParameter("dto.componentId", "unknown");
    req.setParameter("dto.testPoint", "this is giberish");
    req.setParameter("dto.testLine", "LINESTRING (191232 243118, 191108 243242)");
    req.setParameter("dto.testPolygon", "POLYGON (( 10 10, 10 20, 20 20, 20 15, 10 10))");
    req.setParameter("dto.testMultiPoint", "MULTIPOINT(191232 243118, 10000 20000)");
    req.setParameter("dto.testMultiLine", "MULTILINESTRING ((191232 243118, 191108 243242, 200000 250000, 275000 300000))");
    req.setParameter("dto.testMultiPolygon", "MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2, 3 2, 3 3, 2 3,2 2)),((3 3,6 2,6 4,3 3)))");

    try
    {
      new ServletDispatcher().service(req, resp);

      String id = (String) req.getAttribute("id");
      List<AttributeNotificationDTO> notifications = clientRequest.getAttributeNotifications(id, "testPoint");

      assertEquals("fail", req.getAttribute("fail"));
      assertNotNull(notifications);
      assertEquals(1, notifications.size());
      assertTrue(notifications.get(0) instanceof AttributePointParseProblemDTO);
    }
    finally
    {
      systemSession.logout();
    }
  }

  @Test
  @Request
  public void testPropigateLineParseProblem() throws Exception
  {
    ClientSession systemSession = ClientSession.createUserSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { Locale.US });
    ClientRequestIF clientRequest = systemSession.getRequest();

    MockRequest req = new MockRequest("POST", testActionUri);
    HttpServletResponse resp = new DummyResponse();

    req.setAttribute(ClientConstants.CLIENTSESSION, systemSession);
    req.setAttribute(ClientConstants.CLIENTREQUEST, clientRequest);
    req.setParameter("dto.isNew", MdAttributeBooleanInfo.TRUE);
    req.setParameter("dto.componentId", "unknown");
    req.setParameter("dto.testPoint", "POINT(191232 243118)");
    req.setParameter("dto.testLine", "gibberish");
    req.setParameter("dto.testPolygon", "POLYGON (( 10 10, 10 20, 20 20, 20 15, 10 10))");
    req.setParameter("dto.testMultiPoint", "MULTIPOINT(191232 243118, 10000 20000)");
    req.setParameter("dto.testMultiLine", "MULTILINESTRING ((191232 243118, 191108 243242, 200000 250000, 275000 300000))");
    req.setParameter("dto.testMultiPolygon", "MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2, 3 2, 3 3, 2 3,2 2)),((3 3,6 2,6 4,3 3)))");

    try
    {
      new ServletDispatcher().service(req, resp);

      String id = (String) req.getAttribute("id");
      List<AttributeNotificationDTO> notifications = clientRequest.getAttributeNotifications(id, "testLine");

      assertEquals("fail", req.getAttribute("fail"));
      assertNotNull(notifications);
      assertEquals(1, notifications.size());
      assertTrue(notifications.get(0) instanceof AttributeLineStringParseProblemDTO);
    }
    finally
    {
      systemSession.logout();
    }
  }

  @Test
  @Request
  public void testPropigatePolygonParseProblem() throws Exception
  {
    ClientSession systemSession = ClientSession.createUserSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { Locale.US });
    ClientRequestIF clientRequest = systemSession.getRequest();

    MockRequest req = new MockRequest("POST", testActionUri);
    HttpServletResponse resp = new DummyResponse();

    req.setAttribute(ClientConstants.CLIENTSESSION, systemSession);
    req.setAttribute(ClientConstants.CLIENTREQUEST, clientRequest);
    req.setParameter("dto.isNew", MdAttributeBooleanInfo.TRUE);
    req.setParameter("dto.componentId", "unknown");
    req.setParameter("dto.testPoint", "POINT(191232 243118)");
    req.setParameter("dto.testLine", "LINESTRING (191232 243118, 191108 243242)");
    req.setParameter("dto.testPolygon", "does not compute");
    req.setParameter("dto.testMultiPoint", "MULTIPOINT(191232 243118, 10000 20000)");
    req.setParameter("dto.testMultiLine", "MULTILINESTRING ((191232 243118, 191108 243242, 200000 250000, 275000 300000))");
    req.setParameter("dto.testMultiPolygon", "MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2, 3 2, 3 3, 2 3,2 2)),((3 3,6 2,6 4,3 3)))");

    try
    {
      new ServletDispatcher().service(req, resp);

      String id = (String) req.getAttribute("id");
      List<AttributeNotificationDTO> notifications = clientRequest.getAttributeNotifications(id, "testPolygon");

      assertEquals("fail", req.getAttribute("fail"));
      assertNotNull(notifications);
      assertEquals(1, notifications.size());
      assertTrue(notifications.get(0) instanceof AttributePolygonParseProblemDTO);
    }
    finally
    {
      systemSession.logout();
    }
  }

  @Test
  @Request
  public void testPropigateMultiPointParseProblem() throws Exception
  {
    ClientSession systemSession = ClientSession.createUserSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { Locale.US });
    ClientRequestIF clientRequest = systemSession.getRequest();

    MockRequest req = new MockRequest("POST", testActionUri);
    HttpServletResponse resp = new DummyResponse();

    req.setAttribute(ClientConstants.CLIENTSESSION, systemSession);
    req.setAttribute(ClientConstants.CLIENTREQUEST, clientRequest);
    req.setParameter("dto.isNew", MdAttributeBooleanInfo.TRUE);
    req.setParameter("dto.componentId", "unknown");
    req.setParameter("dto.testPoint", "POINT(191232 243118)");
    req.setParameter("dto.testLine", "LINESTRING (191232 243118, 191108 243242)");
    req.setParameter("dto.testPolygon", "POLYGON (( 10 10, 10 20, 20 20, 20 15, 10 10))");
    req.setParameter("dto.testMultiPoint", "gibberish");
    req.setParameter("dto.testMultiLine", "MULTILINESTRING ((191232 243118, 191108 243242, 200000 250000, 275000 300000))");
    req.setParameter("dto.testMultiPolygon", "MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2, 3 2, 3 3, 2 3,2 2)),((3 3,6 2,6 4,3 3)))");

    try
    {
      new ServletDispatcher().service(req, resp);

      String id = (String) req.getAttribute("id");
      List<AttributeNotificationDTO> notifications = clientRequest.getAttributeNotifications(id, "testMultiPoint");

      assertEquals("fail", req.getAttribute("fail"));
      assertNotNull(notifications);
      assertEquals(1, notifications.size());
      assertTrue(notifications.get(0) instanceof AttributeMultiPointParseProblemDTO);
    }
    finally
    {
      systemSession.logout();
    }
  }

  @Test
  @Request
  public void testPropigateMultiLineParseProblem() throws Exception
  {
    ClientSession systemSession = ClientSession.createUserSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { Locale.US });
    ClientRequestIF clientRequest = systemSession.getRequest();

    MockRequest req = new MockRequest("POST", testActionUri);
    HttpServletResponse resp = new DummyResponse();

    req.setAttribute(ClientConstants.CLIENTSESSION, systemSession);
    req.setAttribute(ClientConstants.CLIENTREQUEST, clientRequest);
    req.setParameter("dto.isNew", MdAttributeBooleanInfo.TRUE);
    req.setParameter("dto.componentId", "unknown");
    req.setParameter("dto.testPoint", "POINT(191232 243118)");
    req.setParameter("dto.testLine", "LINESTRING (191232 243118, 191108 243242)");
    req.setParameter("dto.testPolygon", "POLYGON (( 10 10, 10 20, 20 20, 20 15, 10 10))");
    req.setParameter("dto.testMultiPoint", "MULTIPOINT(191232 243118, 10000 20000)");
    req.setParameter("dto.testMultiLine", "gibberish");
    req.setParameter("dto.testMultiPolygon", "MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2, 3 2, 3 3, 2 3,2 2)),((3 3,6 2,6 4,3 3)))");

    try
    {
      new ServletDispatcher().service(req, resp);

      String id = (String) req.getAttribute("id");
      List<AttributeNotificationDTO> notifications = clientRequest.getAttributeNotifications(id, "testMultiLine");

      assertEquals("fail", req.getAttribute("fail"));
      assertNotNull(notifications);
      assertEquals(1, notifications.size());
      assertTrue(notifications.get(0) instanceof AttributeMultiLineStringParseProblemDTO);
    }
    finally
    {
      systemSession.logout();
    }
  }

  @Test
  @Request
  public void testPropigateMultiPolygonParseProblem() throws Exception
  {
    ClientSession systemSession = ClientSession.createUserSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { Locale.US });
    ClientRequestIF clientRequest = systemSession.getRequest();

    MockRequest req = new MockRequest("POST", testActionUri);
    HttpServletResponse resp = new DummyResponse();

    req.setAttribute(ClientConstants.CLIENTSESSION, systemSession);
    req.setAttribute(ClientConstants.CLIENTREQUEST, clientRequest);
    req.setParameter("dto.isNew", MdAttributeBooleanInfo.TRUE);
    req.setParameter("dto.componentId", "unknown");
    req.setParameter("dto.testPoint", "POINT(191232 243118)");
    req.setParameter("dto.testLine", "LINESTRING (191232 243118, 191108 243242)");
    req.setParameter("dto.testPolygon", "POLYGON (( 10 10, 10 20, 20 20, 20 15, 10 10))");
    req.setParameter("dto.testMultiPoint", "MULTIPOINT(191232 243118, 10000 20000)");
    req.setParameter("dto.testMultiLine", "MULTILINESTRING ((191232 243118, 191108 243242, 200000 250000, 275000 300000))");
    req.setParameter("dto.testMultiPolygon", "gibberish");

    try
    {
      new ServletDispatcher().service(req, resp);

      String id = (String) req.getAttribute("id");
      List<AttributeNotificationDTO> notifications = clientRequest.getAttributeNotifications(id, "testMultiPolygon");

      assertEquals("fail", req.getAttribute("fail"));
      assertNotNull(notifications);
      assertEquals(1, notifications.size());
      assertTrue(notifications.get(0) instanceof AttributeMultiPolygonParseProblemDTO);
    }
    finally
    {
      systemSession.logout();
    }
  }

  private static String getSource()
  {
    String source = "package test.generated;\n" + "import com.runwaysdk.business.BusinessDTO;\n" + "import com.vividsolutions.jts.geom.LineString;\n" + "import com.vividsolutions.jts.geom.MultiLineString;\n" + "import com.vividsolutions.jts.geom.MultiPoint;\n" + "import com.vividsolutions.jts.geom.MultiPolygon;\n" + "import com.vividsolutions.jts.geom.Point;\n" + "import com.vividsolutions.jts.geom.Polygon;\n" + "import com.vividsolutions.jts.io.WKTReader;\n" + "public class TestController extends TestControllerBase \n" + "{\n" + "  private static final long serialVersionUID = 1238004456805L;      \n"
        + "  public TestController(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)\n" + "  {\n" + "    super(req, resp, isAsynchronous);\n" + "  }       \n" + "  @Override\n" + "  public void testAction(test.generated.TestBusinessDTO dto) throws java.io.IOException, javax.servlet.ServletException\n" + "  {\n" + "    try\n" + "    {\n" + "    WKTReader reader = new WKTReader();\n" + "    Point point = (Point) reader.read(\"POINT(191232 243118)\");\n" + "    LineString line = (LineString) reader.read(\"LINESTRING (191232 243118, 191108 243242)\");\n" + "    Polygon polygon = (Polygon) reader.read(\"POLYGON (( 10 10, 10 20, 20 20, 20 15, 10 10))\");\n"
        + "    MultiLineString multiLine = (MultiLineString) reader.read(\"MULTILINESTRING ((191232 243118, 191108 243242, 200000 250000, 275000 300000))\");\n" + "    MultiPoint multiPoint = (MultiPoint) reader.read(\"MULTIPOINT(191232 243118, 10000 20000)\");\n" + "    MultiPolygon multiPolygon = (MultiPolygon) reader.read(\"MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2, 3 2, 3 3, 2 3,2 2)),((3 3,6 2,6 4,3 3)))\");\n" + "    if(!dto.getTestPoint().equalsExact(point)) throw new RuntimeException(\"Expected: \" + point.toText() + \", Actual: \" + dto.getTestPoint().toText());   \n" + "    if(!dto.getTestLine().equalsExact(line)) throw new RuntimeException(\"Expected: \" + line.toText() + \", Actual: \" + dto.getTestLine().toText());\n"
        + "    if(!dto.getTestPolygon().equalsExact(polygon)) throw new RuntimeException(\"Expected: \" + polygon.toText() + \", Actual: \" + dto.getTestPolygon().toText());\n" + "    if(!dto.getTestMultiPoint().equalsExact(multiPoint)) throw new RuntimeException(\"Expected: \" + multiPoint.toText() + \", Actual: \" + dto.getTestMultiPoint().toText());\n" + "    if(!dto.getTestMultiLine().equalsExact(multiLine)) throw new RuntimeException(\"Expected: \" + multiLine.toText() + \", Actual: \" + dto.getTestMultiLine().toText());\n" + "    if(!dto.getTestMultiPolygon().equalsExact(multiPolygon)) throw new RuntimeException(\"Expected: \" + multiPolygon.toText() + \", Actual: \" + dto.getTestMultiPolygon().toText());\n" + "    }\n" + "    catch(" + ParseException.class.getName() + " e)\n"
        + "    {\n" + "      throw new RuntimeException(e);\n" + "    }\n" + "  } \n" + "  @Override\n" + "  public void failTestAction(test.generated.TestBusinessDTO dto) throws java.io.IOException, javax.servlet.ServletException\n" + "  {\n" + "    super.getRequest().setAttribute(\"id\", dto.getId());\n" + "    super.getRequest().setAttribute(\"fail\", \"fail\");\n" + "  }\n" + "}\n";

    return source;
  }

}
