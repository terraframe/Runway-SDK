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
import org.junit.Assert;

import com.runwaysdk.ClientSession;
import com.runwaysdk.DoNotWeave;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.RelationshipDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.JSON;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.controller.RequestManager;
import com.runwaysdk.controller.ServletMethod;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;
import com.runwaysdk.request.MockServletRequest;
import com.runwaysdk.request.MockServletResponse;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

public class RestResponseTest extends TestCase implements DoNotWeave
{
  private static ClientSession session;

  private static BusinessDTO   mdBusiness;

  private static BusinessDTO   mdRelationship;

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(RestResponseTest.class);

    TestSetup wrapper = new TestSetup(suite)
    {
      @Override
      protected void setUp() throws Exception
      {
        classSetup();
      }

      @Override
      protected void tearDown() throws Exception
      {
        classTeardown();
      }
    };

    return wrapper;
  }

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

  public static void classSetup()
  {
    session = ClientSession.createUserSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });
    ClientRequestIF clientRequest = session.getRequest();

    mdBusiness = clientRequest.newBusiness(MdBusinessInfo.CLASS);
    mdBusiness.setValue(MdBusinessInfo.NAME, "TestBusiness");
    mdBusiness.setValue(MdBusinessInfo.PACKAGE, "test");
    mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Suit Enumeration Master List");
    mdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.setValue(MdTypeInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    clientRequest.createBusiness(mdBusiness);

    BusinessDTO mdAttributeCharacterDTO = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    mdAttributeCharacterDTO.setValue(MdAttributeCharacterInfo.NAME, TestFixConst.ATTRIBUTE_CHARACTER);
    mdAttributeCharacterDTO.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Character");
    mdAttributeCharacterDTO.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Character desc");
    mdAttributeCharacterDTO.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacterDTO.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacterDTO.setValue(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.toString());
    mdAttributeCharacterDTO.setValue(MdAttributeCharacterInfo.SIZE, "64");
    mdAttributeCharacterDTO.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdBusiness.getId());
    clientRequest.createBusiness(mdAttributeCharacterDTO);

    mdRelationship = clientRequest.newBusiness(MdRelationshipInfo.CLASS);
    mdRelationship.setValue(MdRelationshipInfo.NAME, "TestRelationship");
    mdRelationship.setValue(MdRelationshipInfo.PACKAGE, "test");
    mdRelationship.setValue(MdRelationshipInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    mdRelationship.setStructValue(MdRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship");
    mdRelationship.setValue(MdRelationshipInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdRelationship.setValue(MdRelationshipInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdRelationship.setValue(MdRelationshipInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdRelationship.setValue(MdRelationshipInfo.PARENT_MD_BUSINESS, mdBusiness.getId());
    mdRelationship.setValue(MdRelationshipInfo.PARENT_CARDINALITY, "*");
    mdRelationship.setStructValue(MdRelationshipInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "parent dto");
    mdRelationship.setValue(MdRelationshipInfo.CHILD_MD_BUSINESS, mdBusiness.getId());
    mdRelationship.setValue(MdRelationshipInfo.CHILD_CARDINALITY, "*");
    mdRelationship.setStructValue(MdRelationshipInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "child dto");
    mdRelationship.setValue(MdRelationshipInfo.PARENT_METHOD, "testParent");
    mdRelationship.setValue(MdRelationshipInfo.CHILD_METHOD, "testChild");
    mdRelationship.setValue(MdRelationshipInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    clientRequest.createBusiness(mdRelationship);

  }

  public static void classTeardown()
  {
    ClientRequestIF request = session.getRequest();

    request.delete(mdBusiness.getId());

    session.logout();
  }

  public void testHandleBusinessParameter() throws Exception
  {
    String value = "Test Value";
    String type = "test.TestBusiness";

    ClientRequestIF request = session.getRequest();

    BusinessDTO dto = request.newBusiness(type);
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
    Assert.assertEquals(type, test.get(EntityInfo.TYPE));
  }

  public void testHandleJsonConfiguration() throws Exception
  {
    String value = "Test Value";
    String type = "test.TestBusiness";

    ClientRequestIF request = session.getRequest();

    BusinessDTO dto = request.newBusiness(type);
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
    Assert.assertEquals(type, test.get(EntityInfo.TYPE));
  }

  public void testHandleRelationshipParameter() throws Exception
  {
    String type = "test.TestRelationship";
    ClientRequestIF request = session.getRequest();

    BusinessDTO dto = request.newBusiness("test.TestBusiness");
    dto.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Value");
    request.createBusiness(dto);

    try
    {
      RelationshipDTO relationship = request.addChild(dto.getId(), dto.getId(), type);

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

      Assert.assertEquals(type, test.get(EntityInfo.TYPE));
      Assert.assertEquals(dto.getId(), test.get(JSON.RELATIONSHIP_DTO_PARENT_ID.getLabel()));
      Assert.assertEquals(dto.getId(), test.get(JSON.RELATIONSHIP_DTO_CHILD_ID.getLabel()));
    }
    finally
    {
      request.delete(dto.getId());
    }
  }

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

  public void testHandleSerializableParameter() throws Exception
  {
    String businessType = "test.TestBusiness";
    String relType = "test.TestRelationship";
    String testValue = "Test Value";

    ClientRequestIF request = session.getRequest();

    BusinessDTO dto = request.newBusiness(businessType);
    dto.setValue(TestFixConst.ATTRIBUTE_CHARACTER, testValue);
    request.createBusiness(dto);

    try
    {
      RelationshipDTO relationship = request.addChild(dto.getId(), dto.getId(), relType);

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

      Assert.assertEquals(relType, test.get(EntityInfo.TYPE));
      Assert.assertTrue(test.has("parent"));
      Assert.assertTrue(test.has("child"));

      JSONObject parent = test.getJSONObject("parent");

      Assert.assertEquals(testValue, parent.get(TestFixConst.ATTRIBUTE_CHARACTER));
      Assert.assertEquals(businessType, parent.get(EntityInfo.TYPE));
      Assert.assertEquals(dto.getId(), parent.get(EntityInfo.ID));

      JSONObject child = test.getJSONObject("child");

      Assert.assertEquals(testValue, child.get(TestFixConst.ATTRIBUTE_CHARACTER));
      Assert.assertEquals(businessType, child.get(EntityInfo.TYPE));
      Assert.assertEquals(dto.getId(), child.get(EntityInfo.ID));
    }
    finally
    {
      request.delete(dto.getId());
    }
  }
}
