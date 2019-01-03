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
package com.runwaysdk.mvc;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.runwaysdk.ClasspathTestRunner;
import com.runwaysdk.ClientSession;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.RelationshipDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.JSON;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.controller.RequestManager;
import com.runwaysdk.controller.ServletMethod;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.request.MockServletRequest;
import com.runwaysdk.request.MockServletResponse;
import com.runwaysdk.session.Request;

@RunWith(ClasspathTestRunner.class)
public class RestResponseTest
{
  private static ClientSession     session;

  private static MdBusinessDAO     mdBusiness;

  private static MdRelationshipDAO mdRelationship;

  private static String            mdRelationshipType;

  private static String            mdBusinessType;

  @Request
  @BeforeClass
  public static void classSetup()
  {
    mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.apply();

    mdBusinessType = mdBusiness.definesType();

    TestFixtureFactory.addCharacterAttribute(mdBusiness).apply();

    mdRelationship = TestFixtureFactory.createMdRelationship1(mdBusiness, mdBusiness);
    mdRelationship.apply();

    mdRelationshipType = mdRelationship.definesType();

    session = ClientSession.createUserSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });
  }

  @Request
  @AfterClass
  public static void classTeardown()
  {
    session.logout();

    TestFixtureFactory.delete(mdRelationship);
    TestFixtureFactory.delete(mdBusiness);
  }

  @Test
  public void testHandleBusinessParameter() throws Exception
  {
    String value = "Test Value";

    ClientRequestIF request = session.getRequest();

    BusinessDTO dto = request.newBusiness(mdBusinessType);
    dto.setValue(TestFixConst.ATTRIBUTE_CHARACTER, value);

    MockServletRequest req = new MockServletRequest();
    MockServletResponse resp = new MockServletResponse();
    RequestManager manager = new RequestManager(req, resp, ServletMethod.GET, null, null);

    RestResponse response = new RestResponse();
    response.set("dto", dto);
    response.handle(manager);

    ByteArrayOutputStream baos = ( (ByteArrayOutputStream) resp.getOutputStream() );

    String result = new String(baos.toByteArray(), "UTF-8");

    JSONObject object = new JSONObject(result);

    Assert.assertTrue(object.has("dto"));

    JSONObject test = object.getJSONObject("dto");

    Assert.assertEquals(value, test.get(TestFixConst.ATTRIBUTE_CHARACTER));
    Assert.assertEquals(mdBusinessType, test.get(EntityInfo.TYPE));
  }

  @Test
  public void testHandleJsonConfiguration() throws Exception
  {
    String value = "Test Value";

    ClientRequestIF request = session.getRequest();

    BusinessDTO dto = request.newBusiness(mdBusinessType);
    dto.setValue(TestFixConst.ATTRIBUTE_CHARACTER, value);

    MockServletRequest req = new MockServletRequest();
    MockServletResponse resp = new MockServletResponse();
    RequestManager manager = new RequestManager(req, resp, ServletMethod.GET, null, null);

    JsonConfiguration config = new JsonConfiguration()
    {
      @Override
      public boolean supports(Class<?> clazz)
      {
        return true;
      }

      @Override
      public boolean exclude(String name)
      {
        return name.equals(TestFixConst.ATTRIBUTE_CHARACTER);
      }
    };

    RestResponse response = new RestResponse();
    response.set("dto", dto, config);
    response.handle(manager);

    ByteArrayOutputStream baos = ( (ByteArrayOutputStream) resp.getOutputStream() );

    String result = new String(baos.toByteArray(), "UTF-8");

    JSONObject object = new JSONObject(result);

    Assert.assertTrue(object.has("dto"));

    JSONObject test = object.getJSONObject("dto");

    Assert.assertFalse(test.has(TestFixConst.ATTRIBUTE_CHARACTER));
    Assert.assertEquals(mdBusinessType, test.get(EntityInfo.TYPE));
  }

  @Test
  public void testHandleRelationshipParameter() throws Exception
  {
    ClientRequestIF request = session.getRequest();

    BusinessDTO dto = request.newBusiness(mdBusinessType);
    dto.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Value");
    request.createBusiness(dto);

    try
    {
      RelationshipDTO relationship = request.addChild(dto.getOid(), dto.getOid(), mdRelationshipType);

      MockServletRequest req = new MockServletRequest();
      MockServletResponse resp = new MockServletResponse();
      RequestManager manager = new RequestManager(req, resp, ServletMethod.GET, null, null);

      RestResponse response = new RestResponse();
      response.set("relationship", relationship);
      response.handle(manager);

      ByteArrayOutputStream baos = ( (ByteArrayOutputStream) resp.getOutputStream() );

      String result = new String(baos.toByteArray(), "UTF-8");

      JSONObject object = new JSONObject(result);

      Assert.assertTrue(object.has("relationship"));

      JSONObject test = object.getJSONObject("relationship");

      Assert.assertEquals(mdRelationshipType, test.get(EntityInfo.TYPE));
      Assert.assertEquals(dto.getOid(), test.get(JSON.RELATIONSHIP_DTO_PARENT_OID.getLabel()));
      Assert.assertEquals(dto.getOid(), test.get(JSON.RELATIONSHIP_DTO_CHILD_OID.getLabel()));
    }
    finally
    {
      request.delete(dto.getOid());
    }
  }

  @Test
  public void testHandleBasicParameter() throws Exception
  {
    Integer value = new Integer(12);

    MockServletRequest req = new MockServletRequest();
    MockServletResponse resp = new MockServletResponse();
    RequestManager manager = new RequestManager(req, resp, ServletMethod.GET, null, null);

    RestResponse response = new RestResponse();
    response.set("basic", value);
    response.handle(manager);

    ByteArrayOutputStream baos = ( (ByteArrayOutputStream) resp.getOutputStream() );

    String result = new String(baos.toByteArray(), "UTF-8");

    JSONObject object = new JSONObject(result);

    Assert.assertTrue(object.has("basic"));
    Assert.assertEquals(value, object.get("basic"));
  }

  @Test
  public void testHandleSerializableParameter() throws Exception
  {
    String testValue = "Test Value";

    ClientRequestIF request = session.getRequest();

    BusinessDTO dto = request.newBusiness(mdBusinessType);
    dto.setValue(TestFixConst.ATTRIBUTE_CHARACTER, testValue);
    request.createBusiness(dto);

    try
    {
      RelationshipDTO relationship = request.addChild(dto.getOid(), dto.getOid(), mdRelationshipType);

      MockServletRequest req = new MockServletRequest();
      MockServletResponse resp = new MockServletResponse();
      RequestManager manager = new RequestManager(req, resp, ServletMethod.GET, null, null);

      RestResponse response = new RestResponse();
      response.set("relationship", new MockJsonSerializable(request, relationship));
      response.handle(manager);

      ByteArrayOutputStream baos = ( (ByteArrayOutputStream) resp.getOutputStream() );

      String result = new String(baos.toByteArray(), "UTF-8");

      JSONObject object = new JSONObject(result);

      Assert.assertTrue(object.has("relationship"));

      JSONObject test = object.getJSONObject("relationship");

      Assert.assertEquals(mdRelationshipType, test.get(EntityInfo.TYPE));
      Assert.assertTrue(test.has("parent"));
      Assert.assertTrue(test.has("child"));

      JSONObject parent = test.getJSONObject("parent");

      Assert.assertEquals(testValue, parent.get(TestFixConst.ATTRIBUTE_CHARACTER));
      Assert.assertEquals(mdBusinessType, parent.get(EntityInfo.TYPE));
      Assert.assertEquals(dto.getOid(), parent.get(EntityInfo.OID));

      JSONObject child = test.getJSONObject("child");

      Assert.assertEquals(testValue, child.get(TestFixConst.ATTRIBUTE_CHARACTER));
      Assert.assertEquals(mdBusinessType, child.get(EntityInfo.TYPE));
      Assert.assertEquals(dto.getOid(), child.get(EntityInfo.OID));
    }
    finally
    {
      request.delete(dto.getOid());
    }
  }
}
