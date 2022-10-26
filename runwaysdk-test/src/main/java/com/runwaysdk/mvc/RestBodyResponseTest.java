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
public class RestBodyResponseTest
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

  @Request
  @Test
  public void testHandleBusinessPaameter() throws Exception
  {
    String value = "Test Value";

    ClientRequestIF request = session.getRequest();

    BusinessDTO dto = request.newBusiness(mdBusinessType);
    dto.setValue(TestFixConst.ATTRIBUTE_CHARACTER, value);

    MockServletRequest req = new MockServletRequest();
    MockServletResponse resp = new MockServletResponse();
    RequestManager manager = new RequestManager(req, resp, ServletMethod.GET, null, null);

    RestBodyResponse response = new RestBodyResponse(dto);
    response.handle(manager);

    ByteArrayOutputStream baos = ( (ByteArrayOutputStream) resp.getOutputStream() );

    String result = new String(baos.toByteArray(), "UTF-8");

    JSONObject object = new JSONObject(result);

    Assert.assertEquals(value, object.get(TestFixConst.ATTRIBUTE_CHARACTER));
    Assert.assertEquals(mdBusinessType, object.get(EntityInfo.TYPE));
  }

  @Request
  @Test
  public void testHandleRelationshipPaameter() throws Exception
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

      RestBodyResponse response = new RestBodyResponse(relationship);
      response.handle(manager);

      ByteArrayOutputStream baos = ( (ByteArrayOutputStream) resp.getOutputStream() );

      String result = new String(baos.toByteArray(), "UTF-8");

      JSONObject object = new JSONObject(result);

      Assert.assertEquals(mdRelationshipType, object.get(EntityInfo.TYPE));
      Assert.assertEquals(dto.getOid(), object.get(JSON.RELATIONSHIP_DTO_PARENT_OID.getLabel()));
      Assert.assertEquals(dto.getOid(), object.get(JSON.RELATIONSHIP_DTO_CHILD_OID.getLabel()));
    }
    finally
    {
      request.delete(dto.getOid());
    }
  }

  @Request
  @Test
  public void testHandleBasicPaameter() throws Exception
  {
    Integer value = Integer.valueOf(12);

    MockServletRequest req = new MockServletRequest();
    MockServletResponse resp = new MockServletResponse();
    RequestManager manager = new RequestManager(req, resp, ServletMethod.GET, null, null);

    RestBodyResponse response = new RestBodyResponse(value);
    response.handle(manager);

    ByteArrayOutputStream baos = ( (ByteArrayOutputStream) resp.getOutputStream() );

    String result = new String(baos.toByteArray(), "UTF-8");

    Integer test = Integer.valueOf(result);

    Assert.assertEquals(value, test);
  }

  @Request
  @Test
  public void testHandleSerializablePaameter() throws Exception
  {
    String testValue = "Test Value";

    ClientRequestIF request = session.getRequest();

    BusinessDTO dto = request.newBusiness(mdBusinessType);
    dto.setValue(TestFixConst.ATTRIBUTE_CHARACTER, testValue);
    request.createBusiness(dto);

    try
    {
      RelationshipDTO relationship = request.addChild(dto.getOid(), dto.getOid(), mdRelationshipType);
      JsonSerializable serializable = new MockJsonSerializable(request, relationship);

      MockServletRequest req = new MockServletRequest();
      MockServletResponse resp = new MockServletResponse();
      RequestManager manager = new RequestManager(req, resp, ServletMethod.GET, null, null);

      RestBodyResponse response = new RestBodyResponse(serializable);
      response.handle(manager);

      ByteArrayOutputStream baos = ( (ByteArrayOutputStream) resp.getOutputStream() );

      String result = new String(baos.toByteArray(), "UTF-8");

      JSONObject test = new JSONObject(result);

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
