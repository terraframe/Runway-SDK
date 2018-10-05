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
package com.runwaysdk.facade;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.runwaysdk.ClasspathTestRunner;
import com.runwaysdk.ClientSession;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.MethodMetaData;
import com.runwaysdk.business.generation.json.JSONFacade;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.JSONClientRequestIF;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.session.Request;
import com.runwaysdk.transport.conversion.json.JSONReturnObject;
import com.runwaysdk.transport.conversion.json.JSONUtil;
import com.runwaysdk.util.DTOConversionUtilInfo;
import com.runwaysdk.web.json.JSONJavaClientRequest;

@RunWith(ClasspathTestRunner.class)
public class JSONInvokeMethodTest extends InvokeMethodTestBase
{
  protected static volatile JSONClientRequestIF jsonProxy = null;

  protected static Locale                       locale    = CommonProperties.getDefaultLocale();

  @BeforeClass
  public static void classSetUp()
  {
    jsonProxy = new JSONJavaClientRequest("default", "");

    systemSession = ClientSession.createUserSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      clientRequest = systemSession.getRequest();
      classSetUpRequest();
      noPermissionSession = ClientSession.createUserSession("smethie", "aaa", new Locale[] { CommonProperties.getDefaultLocale() });
      noPermissionRequest = noPermissionSession.getRequest();
      finalizeSetup();
    }
    catch (Exception e)
    {
      systemSession.logout();
    }
  }

  @Request
  @Test
  public void testInvokeEmptyMethod() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

    BusinessDTO businessDAO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", "142");
    collectionClass.getMethod("apply").invoke(businessDAO);
    String oid = businessDAO.getOid();

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    BusinessDTO object = (BusinessDTO) get.invoke(null, clientRequest, oid);

    String returnJSON1 = jsonProxy.lock(clientRequest.getSessionId(), object.getOid());
    JSONObject returnObject1 = new JSONObject(returnJSON1);

    String businessJSON = returnObject1.getJSONObject(JSONReturnObject.RETURN_VALUE).toString();

    String[] declaredTypes = {};
    MethodMetaData metadata = new MethodMetaData(collectionType, "poopNothing", declaredTypes);
    String metadataJSON = JSONFacade.getJSONFromMethodMetadata(metadata);
    String returnJSON = jsonProxy.invokeMethod(clientRequest.getSessionId(), metadataJSON, businessJSON, "[]");
    JSONObject returnObject = new JSONObject(returnJSON);

    JSONArray jsonArray = returnObject.getJSONArray(JSONReturnObject.RETURN_VALUE);
    JSONObject jsonObject = jsonArray.getJSONObject(DTOConversionUtilInfo.JSON_CALLED_OBJECT);

    BusinessDTO output = (BusinessDTO) JSONUtil.getComponentDTOFromJSON("", locale, jsonObject.toString());

    Assert.assertEquals(new Long(142), new Long(output.getValue("aLong"))); // should
    // be the
    // same
    // value
  }

  @Request
  @Test
  public void testInvokeArrayMethod() throws Exception
  {
    String booleanInput = Boolean.toString(true);
    String longInput = "374364";

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

    BusinessDTO businessDAO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO);
    String oid = businessDAO.getOid();

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    BusinessDTO object = (BusinessDTO) get.invoke(null, clientRequest, oid);

    String returnJSON1 = jsonProxy.lock(clientRequest.getSessionId(), object.getOid());

    JSONObject returnObject1 = new JSONObject(returnJSON1);

    String businessJSON = returnObject1.getJSONObject(JSONReturnObject.RETURN_VALUE).toString();

    String[] methodTypes = { "[Ljava.lang.Long;", "java.lang.Boolean" };
    String[] parameters = { "[3,4,8,9, 10923012910]", MdAttributeBooleanInfo.TRUE };

    MethodMetaData metadata = new MethodMetaData(collectionType, "sortNumbers", methodTypes);
    String metadataJSON = JSONFacade.getJSONFromMethodMetadata(metadata);

    String parametersJSON = JSONFacade.getJSONFromObject(clientRequest.getSessionId(), parameters).toString();
    String returnJSON = jsonProxy.invokeMethod(clientRequest.getSessionId(), metadataJSON, businessJSON, parametersJSON);

    JSONObject returnObject = new JSONObject(returnJSON);

    JSONArray jsonArray = returnObject.getJSONArray(JSONReturnObject.RETURN_VALUE);

    BusinessDTO output = (BusinessDTO) JSONUtil.getComponentDTOFromJSON("", locale, jsonArray.getJSONObject(DTOConversionUtilInfo.JSON_RETURN_OBJECT).toString());

    if (!output.getType().equals(object.getType()))
    {
      Assert.fail("The invoked method [sortNumbers] did not return an object of the proper type");
    }

    Assert.assertEquals(Boolean.parseBoolean(booleanInput), Boolean.parseBoolean(output.getValue("aBoolean")));
    Assert.assertEquals(new Long(3), new Long(Long.parseLong(output.getValue("aLong"))));
  }

  @Request
  @Test
  public void testInvokeDefinedAttributeMethod() throws Exception
  {
    String input = "164";

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

    BusinessDTO businessDAO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", input + "3");
    collectionClass.getMethod("apply").invoke(businessDAO);
    String oid = businessDAO.getOid();

    BusinessDTO businessDAO2 = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO2.setValue("aLong", input);
    collectionClass.getMethod("apply").invoke(businessDAO2);
    String id2 = businessDAO2.getOid();

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    BusinessDTO object = (BusinessDTO) get.invoke(null, clientRequest, oid);
    BusinessDTO object2 = (BusinessDTO) get.invoke(null, clientRequest, id2);

    collectionClass.getMethod("lock").invoke(object);
    collectionClass.getMethod("lock").invoke(object2);

    String businessJSON = JSONFacade.getJSONFromComponentDTO(object).toString();

    String[] methodTypes = { object.getType() };
    BusinessDTO[] parameters = { object2 };
    String parametersJSON = JSONFacade.getJSONFromObject(clientRequest.getSessionId(), parameters).toString();

    MethodMetaData metadata = new MethodMetaData(collectionType, "testMethod", methodTypes);
    String metadataJSON = JSONFacade.getJSONFromMethodMetadata(metadata);

    String returnJSON = jsonProxy.invokeMethod(clientRequest.getSessionId(), metadataJSON, businessJSON, parametersJSON);
    JSONObject returnObject = new JSONObject(returnJSON);

    JSONArray jsonArray = returnObject.getJSONArray(JSONReturnObject.RETURN_VALUE);
    BusinessDTO returnDTO = (BusinessDTO) JSONUtil.getComponentDTOFromJSON(clientRequest.getSessionId(), locale, jsonArray.getString(DTOConversionUtilInfo.JSON_CALLED_OBJECT));

    Assert.assertEquals(JSONObject.NULL, jsonArray.get(DTOConversionUtilInfo.JSON_RETURN_OBJECT));
    Assert.assertEquals(Long.parseLong(input), Long.parseLong(returnDTO.getValue("aLong")));
  }

  @Request
  @Test
  public void testInvokeDefinedArrayMethod() throws Exception
  {
    String input = "Har har bar bar";
    String longInput = "1";

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

    BusinessDTO businessDAO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO);
    String oid = businessDAO.getOid();

    BusinessDTO businessDAO2 = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO2.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO2);
    String id2 = businessDAO2.getOid();

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    BusinessDTO object = (BusinessDTO) get.invoke(null, clientRequest, oid);
    BusinessDTO object2 = (BusinessDTO) get.invoke(null, clientRequest, id2);

    Object array = Array.newInstance(collectionClass, 1);
    Array.set(array, 0, object2);

    collectionClass.getMethod("lock").invoke(object);
    collectionClass.getMethod("lock").invoke(object2);

    String businessJSON = JSONFacade.getJSONFromComponentDTO(object).toString();
    String[] methodTypes = { "[L" + pack + ".Collection;", String.class.getName() };

    // convert the type-safe array into a type-unsafe DTO JSON array
    BusinessDTO[] dtoArr = (BusinessDTO[]) array;
    String[] parameters = { JSONFacade.getJSONArrayFromComponentDTOs((List<BusinessDTO>) Arrays.asList(dtoArr)).toString(), input };

    MethodMetaData metadata = new MethodMetaData(collectionType, "sortCollections", methodTypes);
    String metadataJSON = JSONFacade.getJSONFromMethodMetadata(metadata);

    String parametersJSON = JSONFacade.getJSONFromObject(clientRequest.getSessionId(), parameters).toString();
    String returnJSON = jsonProxy.invokeMethod(clientRequest.getSessionId(), metadataJSON, businessJSON, parametersJSON);
    JSONObject returnObject = new JSONObject(returnJSON);

    JSONArray jsonArray = returnObject.getJSONArray(JSONReturnObject.RETURN_VALUE);

    JSONArray output = jsonArray.getJSONArray(DTOConversionUtilInfo.JSON_RETURN_OBJECT);

    Assert.assertEquals(Array.getLength(array), output.length());

    for (int i = 0; i < output.length(); i++)
    {
      BusinessDTO dto = (BusinessDTO) JSONUtil.getComponentDTOFromJSON("", locale, output.getString(i));

      Assert.assertEquals(input, dto.getValue("aCharacter"));
      Assert.assertEquals(longInput, dto.getValue("aLong"));
    }
  }

  @Request
  @Test
  public void testInvokeEmptyArrayMethod() throws Exception
  {
    String input = "Har har de dar dar";
    String longInput = "152";

    // Create the existing BusinessDAO
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

    BusinessDTO businessDAO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO);
    String oid = businessDAO.getOid();

    BusinessDTO businessDAO2 = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO2.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO2);
    String id2 = businessDAO2.getOid();

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    BusinessDTO object = (BusinessDTO) get.invoke(null, clientRequest, oid);
    BusinessDTO object2 = (BusinessDTO) get.invoke(null, clientRequest, id2);

    collectionClass.getMethod("lock").invoke(object);
    collectionClass.getMethod("lock").invoke(object2);

    String businessJSON = JSONFacade.getJSONFromComponentDTO(object).toString();
    String[] methodTypes = { "[L" + pack + ".Collection;", String.class.getName() };

    // convert the type-safe array into a type-unsafe DTO JSON array
    BusinessDTO[] dtoArr = new BusinessDTO[0];
    String[] parameters = { JSONFacade.getJSONArrayFromComponentDTOs((List<BusinessDTO>) Arrays.asList(dtoArr)).toString(), input };

    MethodMetaData metadata = new MethodMetaData(collectionType, "sortCollections", methodTypes);
    String metadataJSON = JSONFacade.getJSONFromMethodMetadata(metadata);

    String parametersJSON = JSONFacade.getJSONFromObject(clientRequest.getSessionId(), parameters).toString();
    String returnJSON = jsonProxy.invokeMethod(clientRequest.getSessionId(), metadataJSON, businessJSON, parametersJSON);
    JSONObject returnObject = new JSONObject(returnJSON);

    JSONArray jsonArray = returnObject.getJSONArray(JSONReturnObject.RETURN_VALUE);

    JSONArray output = jsonArray.getJSONArray(DTOConversionUtilInfo.JSON_RETURN_OBJECT);

    Assert.assertEquals(dtoArr.length, output.length());

    for (int i = 0; i < output.length(); i++)
    {
      BusinessDTO dto = (BusinessDTO) JSONUtil.getComponentDTOFromJSON("", locale, output.getString(i));

      Assert.assertEquals(input, dto.getValue("aCharacter"));
      Assert.assertEquals(longInput, dto.getValue("aLong"));
    }
  }

  @Request
  @Test
  public void testInvokeMultiArrayMethod() throws Exception
  {
    // Create the existing BusinessDAO
    String longInput = "163";
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

    BusinessDTO businessDAO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO);
    String oid = businessDAO.getOid();

    BusinessDTO businessDAO2 = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO2.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO2);
    String id2 = businessDAO2.getOid();

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    BusinessDTO object = (BusinessDTO) get.invoke(null, clientRequest, oid);
    BusinessDTO object2 = (BusinessDTO) get.invoke(null, clientRequest, id2);

    Object array = Array.newInstance(collectionClass, 1);
    Array.set(array, 0, object2);

    Object array2 = Array.newInstance(array.getClass(), 1);
    Array.set(array2, 0, array);

    Object array3 = Array.newInstance(array2.getClass(), 1);
    Array.set(array3, 0, array2);

    Object array4 = Array.newInstance(array3.getClass(), 1);
    Array.set(array4, 0, array3);

    String inputArray = JSONFacade.getJSONFromObject(clientRequest.getSessionId(), array4).toString();

    collectionClass.getMethod("lock").invoke(object);
    collectionClass.getMethod("lock").invoke(object2);

    String businessJSON = JSONFacade.getJSONFromComponentDTO(object).toString();
    String[] methodTypes = { "[[[[L" + pack + ".Collection;" };
    String[] parameters = { inputArray };

    MethodMetaData metadata = new MethodMetaData(collectionType, "testMultiArray", methodTypes);
    String metadataJSON = JSONFacade.getJSONFromMethodMetadata(metadata);

    String parametersJSON = JSONFacade.getJSONFromObject(clientRequest.getSessionId(), parameters).toString();
    String returnJSON = jsonProxy.invokeMethod(clientRequest.getSessionId(), metadataJSON, businessJSON, parametersJSON);
    JSONObject returnObject = new JSONObject(returnJSON);

    JSONArray jsonArray = returnObject.getJSONArray(JSONReturnObject.RETURN_VALUE);

    JSONArray multi = jsonArray.getJSONArray(DTOConversionUtilInfo.JSON_RETURN_OBJECT);

    Assert.assertEquals(2, multi.length());
    Assert.assertEquals(2, multi.getJSONArray(0).length());
    Assert.assertEquals("Yo my nizzle", multi.getJSONArray(0).getString(0));
    Assert.assertEquals("Leroy Im witha or against ya.", multi.getJSONArray(0).getString(1));
    Assert.assertEquals(2, multi.getJSONArray(1).length());
    Assert.assertEquals("[[[[L" + collectionType + ";", multi.getJSONArray(1).getString(0));
    Assert.assertEquals("Collection[][][][]", multi.getJSONArray(1).getString(1));
  }

  @Request
  @Test
  public void testInvokeMethodOnSubclass() throws Exception
  {
    String longInput = "278";

    Class<?> bagClass = LoaderDecorator.load(bagDTO);

    BusinessDTO businessDAO = (BusinessDTO) bagClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", longInput + "0");
    bagClass.getMethod("apply").invoke(businessDAO);
    String oid = businessDAO.getOid();

    BusinessDTO businessDAO2 = (BusinessDTO) bagClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO2.setValue("aLong", longInput);
    bagClass.getMethod("apply").invoke(businessDAO2);
    String id2 = businessDAO2.getOid();

    Method get = bagClass.getMethod("get", ClientRequestIF.class, String.class);
    BusinessDTO object = (BusinessDTO) get.invoke(null, clientRequest, oid);
    BusinessDTO object2 = (BusinessDTO) get.invoke(null, clientRequest, id2);

    bagClass.getMethod("lock").invoke(object);
    bagClass.getMethod("lock").invoke(object2);

    String businessJSON = JSONFacade.getJSONFromComponentDTO(object).toString();

    String[] methodTypes = { "" + pack + ".Collection" };
    String parameter = JSONFacade.getJSONFromComponentDTO(object2).toString();
    String[] parameters = { parameter };

    MethodMetaData metadata = new MethodMetaData(collectionType, "testMethod", methodTypes);
    String metadataJSON = JSONFacade.getJSONFromMethodMetadata(metadata);

    String parametersJSON = JSONFacade.getJSONFromObject(clientRequest.getSessionId(), parameters).toString();
    String returnJSON = jsonProxy.invokeMethod(clientRequest.getSessionId(), metadataJSON, businessJSON, parametersJSON);
    JSONObject returnObject = new JSONObject(returnJSON);

    JSONArray jsonArray = returnObject.getJSONArray(JSONReturnObject.RETURN_VALUE);
    BusinessDTO returnDTO = (BusinessDTO) JSONUtil.getComponentDTOFromJSON(clientRequest.getSessionId(), locale, jsonArray.getString(DTOConversionUtilInfo.JSON_CALLED_OBJECT));

    Assert.assertEquals(JSONObject.NULL, jsonArray.get(DTOConversionUtilInfo.JSON_RETURN_OBJECT));
    Assert.assertEquals(Long.parseLong(longInput) + 10L, Long.parseLong(returnDTO.getValue("aLong")));
  }

  @Request
  @Test
  public void testInvokeMethodOnSubArray() throws Exception
  {
    String longInput = "142";
    String input = "H to this izzo, E to the izza";

    Class<?> bagClass = LoaderDecorator.load(bagDTO);

    BusinessDTO businessDAO = (BusinessDTO) bagClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", longInput + "0");
    bagClass.getMethod("apply").invoke(businessDAO);
    String oid = businessDAO.getOid();

    BusinessDTO businessDAO2 = (BusinessDTO) bagClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO2.setValue("aLong", longInput);
    bagClass.getMethod("apply").invoke(businessDAO2);
    String id2 = businessDAO2.getOid();

    Method get = bagClass.getMethod("get", ClientRequestIF.class, String.class);
    BusinessDTO object = (BusinessDTO) get.invoke(null, clientRequest, oid);
    BusinessDTO object2 = (BusinessDTO) get.invoke(null, clientRequest, id2);

    Object array = Array.newInstance(bagClass, 1);
    Array.set(array, 0, object2);

    bagClass.getMethod("lock").invoke(object);
    bagClass.getMethod("lock").invoke(object2);

    String businessJSON = JSONFacade.getJSONFromComponentDTO(object).toString();
    String[] methodTypes = { "[L" + pack + ".Collection;", String.class.getName() };

    // convert the type-safe array into a type-unsafe DTO JSON array
    BusinessDTO[] dtoArr = (BusinessDTO[]) array;
    String[] parameters = { JSONFacade.getJSONArrayFromComponentDTOs((List<BusinessDTO>) Arrays.asList(dtoArr)).toString(), input };

    MethodMetaData metadata = new MethodMetaData(collectionType, "sortCollections", methodTypes);
    String metadataJSON = JSONFacade.getJSONFromMethodMetadata(metadata);

    String parametersJSON = JSONFacade.getJSONFromObject(clientRequest.getSessionId(), parameters).toString();
    String returnJSON = jsonProxy.invokeMethod(clientRequest.getSessionId(), metadataJSON, businessJSON, parametersJSON);
    JSONObject returnObject = new JSONObject(returnJSON);

    JSONArray jsonArray = returnObject.getJSONArray(JSONReturnObject.RETURN_VALUE);

    JSONArray output = jsonArray.getJSONArray(DTOConversionUtilInfo.JSON_RETURN_OBJECT);

    Assert.assertEquals(Array.getLength(array), output.length());

    for (int i = 0; i < output.length(); i++)
    {
      BusinessDTO dto = (BusinessDTO) JSONUtil.getComponentDTOFromJSON("", locale, output.getString(i));

      Assert.assertEquals(input, dto.getValue("aCharacter"));
      Assert.assertEquals(longInput, dto.getValue("aLong"));
    }
  }
}
