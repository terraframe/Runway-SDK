/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.mvc;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Locale;

import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.ClientSession;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.controller.IllegalURIMethodException;
import com.runwaysdk.controller.RequestManager;
import com.runwaysdk.controller.ServletMethod;
import com.runwaysdk.controller.URLConfigurationManager;
import com.runwaysdk.controller.UnknownServletException;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;
import com.runwaysdk.request.MockServletRequest;
import com.runwaysdk.request.MockServletResponse;
import com.runwaysdk.session.Request;
import com.runwaysdk.transport.conversion.json.ComponentDTOIFToJSON;

public class DelegatingServletTest
{
  private static ClientSession session;

  private static BusinessDTO   mdBusiness;

  private static BusinessDTO   mdRelationship;

  @Request
  @BeforeClass
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

  @Request
  @AfterClass
  public static void classTeardown()
  {
    ClientRequestIF request = session.getRequest();

    request.delete(mdBusiness.getId());

    session.logout();
  }

  @Request
  @Test
  public void testdelegate() throws Exception
  {
    InputStream istream = this.getClass().getResourceAsStream("/testmap.xml");

    Assert.assertNotNull(istream);

    try
    {
      URLConfigurationManager manager = new URLConfigurationManager();
      manager.readMappings(istream);

      MockServletRequest req = new MockServletRequest();
      req.setServletPath("test/testMethod");

      MockServletResponse resp = new MockServletResponse();

      RequestManager request = new RequestManager(req, resp, ServletMethod.GET, null, null);

      DelegatingServlet dispatcher = new DelegatingServlet(manager);
      dispatcher.delegate(request);

      ByteArrayOutputStream baos = ( (ByteArrayOutputStream) resp.getOutputStream() );

      String result = new String(baos.toByteArray(), "UTF-8");

      Integer test = new Integer(result);

      Assert.assertEquals(new Integer(15), test);
      Assert.assertEquals(200, resp.getStatus());
      Assert.assertEquals("application/json", resp.getContentType());
    }
    finally
    {
      istream.close();
    }
  }

  @Request
  @Test
  public void testBasicParameter() throws Exception
  {
    InputStream istream = this.getClass().getResourceAsStream("/testmap.xml");

    Assert.assertNotNull(istream);

    try
    {
      URLConfigurationManager manager = new URLConfigurationManager();
      manager.readMappings(istream);

      MockServletRequest req = new MockServletRequest();
      req.setServletPath("test/number");
      req.setParameter("value", "100");

      MockServletResponse resp = new MockServletResponse();

      RequestManager request = new RequestManager(req, resp, ServletMethod.GET, null, null);

      DelegatingServlet dispatcher = new DelegatingServlet(manager);
      dispatcher.delegate(request);

      ByteArrayOutputStream baos = ( (ByteArrayOutputStream) resp.getOutputStream() );

      String result = new String(baos.toByteArray(), "UTF-8");

      Integer test = new Integer(result);

      Assert.assertEquals(new Integer(100), test);
      Assert.assertEquals(200, resp.getStatus());
      Assert.assertEquals("application/json", resp.getContentType());
    }
    finally
    {
      istream.close();
    }
  }

  @Request
  @Test
  public void testBasicDTOParameter() throws Exception
  {
    ClientRequestIF request = session.getRequest();

    BusinessDTO dto = request.newBusiness("test.TestBusiness");
    dto.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Value");
    request.createBusiness(dto);

    try
    {
      InputStream istream = this.getClass().getResourceAsStream("/testmap.xml");

      Assert.assertNotNull(istream);

      try
      {
        URLConfigurationManager manager = new URLConfigurationManager();
        manager.readMappings(istream);

        MockServletRequest req = new MockServletRequest();
        req.setServletPath("test/dto");
        req.setParameter("dto", new RestSerializer().serialize(dto, new NullConfiguration()).toString());

        MockServletResponse resp = new MockServletResponse();

        RequestManager rm = new RequestManager(req, resp, ServletMethod.POST, session, request);

        DelegatingServlet dispatcher = new DelegatingServlet(manager);
        dispatcher.delegate(rm);

        ByteArrayOutputStream baos = ( (ByteArrayOutputStream) resp.getOutputStream() );

        JSONObject response = new JSONObject(new String(baos.toByteArray(), "UTF-8"));
        JSONObject test = response.getJSONObject("dto");

        Assert.assertEquals(dto.getValue(TestFixConst.ATTRIBUTE_CHARACTER), test.getString(TestFixConst.ATTRIBUTE_CHARACTER));
        Assert.assertEquals(dto.getId(), test.getString(ComponentInfo.ID));
        Assert.assertEquals(dto.getType(), test.getString(ComponentInfo.TYPE));

        Assert.assertEquals(200, resp.getStatus());
        Assert.assertEquals("application/json", resp.getContentType());
      }
      finally
      {
        istream.close();
      }
    }
    finally
    {
      request.delete(dto.getId());
    }
  }

  @Request
  @Test
  public void testRunwayJSONDTOParameter() throws Exception
  {
    ClientRequestIF request = session.getRequest();

    BusinessDTO dto = request.newBusiness("test.TestBusiness");
    dto.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Test Value");
    request.createBusiness(dto);

    try
    {
      InputStream istream = this.getClass().getResourceAsStream("/testmap.xml");

      Assert.assertNotNull(istream);

      try
      {
        URLConfigurationManager manager = new URLConfigurationManager();
        manager.readMappings(istream);

        MockServletRequest req = new MockServletRequest();
        req.setServletPath("test/runway");
        req.setParameter("dto", ComponentDTOIFToJSON.getConverter(dto).populate().toString());

        MockServletResponse resp = new MockServletResponse();

        RequestManager rm = new RequestManager(req, resp, ServletMethod.POST, session, request);

        DelegatingServlet dispatcher = new DelegatingServlet(manager);
        dispatcher.delegate(rm);

        ByteArrayOutputStream baos = ( (ByteArrayOutputStream) resp.getOutputStream() );

        JSONObject response = new JSONObject(new String(baos.toByteArray(), "UTF-8"));
        JSONObject test = response.getJSONObject("dto");

        Assert.assertEquals(dto.getValue(TestFixConst.ATTRIBUTE_CHARACTER), test.getString(TestFixConst.ATTRIBUTE_CHARACTER));
        Assert.assertEquals(dto.getId(), test.getString(ComponentInfo.ID));
        Assert.assertEquals(dto.getType(), test.getString(ComponentInfo.TYPE));

        Assert.assertEquals(200, resp.getStatus());
        Assert.assertEquals("application/json", resp.getContentType());
      }
      finally
      {
        istream.close();
      }
    }
    finally
    {
      request.delete(dto.getId());
    }
  }

  @Request
  @Test
  public void testInvalidUri() throws Exception
  {
    InputStream istream = this.getClass().getResourceAsStream("/testmap.xml");

    Assert.assertNotNull(istream);

    try
    {
      MockServletRequest req = new MockServletRequest();
      req.setServletPath("test/bad");

      MockServletResponse resp = new MockServletResponse();

      RequestManager request = new RequestManager(req, resp, ServletMethod.GET, null, null);

      URLConfigurationManager manager = new URLConfigurationManager();
      manager.readMappings(istream);

      try
      {
        DelegatingServlet dispatcher = new DelegatingServlet(manager);
        dispatcher.delegate(request);

        Assert.fail();
      }
      catch (UnknownServletException e)
      {
        // This is expected
      }
    }
    finally
    {
      istream.close();
    }
  }

  @Request
  @Test
  public void testGetOnPost() throws Exception
  {
    InputStream istream = this.getClass().getResourceAsStream("/testmap.xml");

    Assert.assertNotNull(istream);

    try
    {
      MockServletRequest req = new MockServletRequest();
      req.setServletPath("test/generate");

      MockServletResponse resp = new MockServletResponse();

      RequestManager request = new RequestManager(req, resp, ServletMethod.GET, null, null);

      URLConfigurationManager manager = new URLConfigurationManager();
      manager.readMappings(istream);

      try
      {
        DelegatingServlet dispatcher = new DelegatingServlet(manager);
        dispatcher.delegate(request);

        Assert.fail();
      }
      catch (IllegalURIMethodException e)
      {
        // This is expected
      }
    }
    finally
    {
      istream.close();
    }
  }

}