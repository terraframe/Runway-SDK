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
package com.runwaysdk.facade;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.runwaysdk.ClasspathTestRunner;
import com.runwaysdk.ClientSession;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.BusinessQueryDTO;
import com.runwaysdk.business.ComponentDTOFacade;
import com.runwaysdk.business.ComponentQueryDTO;
import com.runwaysdk.business.EnumerationDTOIF;
import com.runwaysdk.business.MethodMetaData;
import com.runwaysdk.business.RelationshipDTO;
import com.runwaysdk.business.UtilDTO;
import com.runwaysdk.business.ViewDTO;
import com.runwaysdk.business.ViewQueryDTO;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.constants.UserInfo;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.session.ExecuteInstancePermissionExceptionDTO;
import com.runwaysdk.session.ExecuteStaticPermissionExceptionDTO;
import com.runwaysdk.session.Request;
import com.runwaysdk.transport.metadata.AttributeCharacterMdDTO;

@RunWith(ClasspathTestRunner.class)
public class InvokeMethodTest extends InvokeMethodTestBase
{
  @BeforeClass
  public static void classSetUp()
  {
    systemSession = ClientSession.createUserSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });
    clientRequest = systemSession.getRequest();
    classSetUpRequest();
    
    noPermissionSession = ClientSession.createUserSession("smethie", "aaa", new Locale[] { CommonProperties.getDefaultLocale() });
    noPermissionRequest = noPermissionSession.getRequest();
    finalizeSetup();
  }

  @Request
  @Test
  public void testMethodActorReadPermissions() throws Exception
  {
    clientRequest.grantTypePermission(methodActor.getOid(), collection.getOid(), Operation.CREATE.name());
    clientRequest.grantTypePermission(methodActor.getOid(), collection.getOid(), Operation.READ.name());
    clientRequest.grantAttributePermission(methodActor.getOid(), mdAttributeLong.getOid(), Operation.READ.name());

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

    BusinessDTO businessDTO = null;
    try
    {
      businessDTO = (BusinessDTO) collectionClass.getMethod("methodActorRead", ClientRequestIF.class).invoke(null, noPermissionRequest);
      Assert.assertTrue("Attribute is not readable even though method actor has adequate permissions.", businessDTO.isReadable("aLong2"));
    }
    finally
    {
      clientRequest.revokeTypePermission(methodActor.getOid(), collection.getOid(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(methodActor.getOid(), collection.getOid(), Operation.READ.name());
      clientRequest.revokeAttributePermission(methodActor.getOid(), mdAttributeLong.getOid(), Operation.READ.name());

      if (businessDTO != null)
      {
        clientRequest.delete(businessDTO.getOid());
      }

    }
  }

  @Request
  @Test
  public void testMethodActorNoReadPermissions() throws Exception
  {
    clientRequest.grantTypePermission(methodActor.getOid(), collection.getOid(), Operation.CREATE.name());

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

    BusinessDTO businessDTO = null;
    try
    {
      businessDTO = (BusinessDTO) collectionClass.getMethod("methodActorRead", ClientRequestIF.class).invoke(null, noPermissionRequest);
      Assert.assertFalse("Attribute is readable even though method actor does not have adequate permissions.", businessDTO.isReadable("aLong2"));
    }
    finally
    {
      clientRequest.revokeTypePermission(methodActor.getOid(), collection.getOid(), Operation.CREATE.name());

      if (businessDTO != null)
      {
        clientRequest.delete(businessDTO.getOid());
      }

    }
  }

  @Request
  @Test
  public void testInvokeMethodWithByteArrayReturnType() throws Exception
  {
    BusinessDTO collectionObj1 = clientRequest.newBusiness(collectionType);
    collectionObj1.setValue("aCharacter", "some value");
    clientRequest.createBusiness(collectionObj1);

    BusinessDTO collectionObj2 = clientRequest.newBusiness(collectionType);
    collectionObj2.setValue("aCharacter", "some other value");
    clientRequest.createBusiness(collectionObj2);

    try
    {
      Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

      Method getCount = collectionClass.getMethod("getCollectionObjectCount", ClientRequestIF.class);
      Integer recordCount = (Integer) getCount.invoke(null, clientRequest);

      Method getExcelBytes = collectionClass.getMethod("getExcelFile", ClientRequestIF.class);
      Byte[] excelBytes = (Byte[]) getExcelBytes.invoke(null, clientRequest);

      // FileOutputStream fileBytes = new FileOutputStream(new
      // File(ExcelTest.path+"/ValueQueryTest.xls"));

      byte[] bytes = new byte[excelBytes.length];

      for (int i = 0; i < bytes.length; i++)
      {
        bytes[i] = excelBytes[i];
      }

      // fileBytes.write(bytes);
      //
      // fileBytes.flush();
      // fileBytes.close();

      InputStream stream = new ByteArrayInputStream(bytes);

      POIFSFileSystem fileSystem = new POIFSFileSystem(stream);
      Workbook workbook = new HSSFWorkbook(fileSystem);
      Sheet sheet = workbook.getSheetAt(0);
      Iterator<Row> rowIterator = sheet.rowIterator();

      Integer rowCount = 0;
      while (rowIterator.hasNext())
      {
        rowIterator.next();
        rowCount++;
      }

      // Minus 1 for the header row
      rowCount--;

      Assert.assertEquals(recordCount, rowCount);
      
      workbook.close();
    }
    finally
    {
      clientRequest.delete(collectionObj1.getOid());
      clientRequest.delete(collectionObj2.getOid());
    }
  }

  @Request
  @Test
  public void testInvokeMethodWithInputStreamReturnType() throws Exception
  {
    BusinessDTO collectionObj1 = clientRequest.newBusiness(collectionType);
    collectionObj1.setValue("aCharacter", "some value");
    clientRequest.createBusiness(collectionObj1);

    BusinessDTO collectionObj2 = clientRequest.newBusiness(collectionType);
    collectionObj2.setValue("aCharacter", "some other value");
    clientRequest.createBusiness(collectionObj2);

    try
    {
      Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

      Method getCount = collectionClass.getMethod("getCollectionObjectCount", ClientRequestIF.class);
      Integer recordCount = (Integer) getCount.invoke(null, clientRequest);

      Method getExcelBytes = collectionClass.getMethod("getFileStream", ClientRequestIF.class);
      InputStream stream = (InputStream) getExcelBytes.invoke(null, clientRequest);

      POIFSFileSystem fileSystem = new POIFSFileSystem(stream);
      Workbook workbook = new HSSFWorkbook(fileSystem);
      Sheet sheet = workbook.getSheetAt(0);
      Iterator<Row> rowIterator = sheet.rowIterator();

      Integer rowCount = 0;
      while (rowIterator.hasNext())
      {
        rowIterator.next();
        rowCount++;
      }

      // Minus 1 for the header row
      rowCount--;

      Assert.assertEquals(recordCount, rowCount);
      
      workbook.close();
    }
    finally
    {
      clientRequest.delete(collectionObj1.getOid());
      clientRequest.delete(collectionObj2.getOid());
    }
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

    Boolean bool = Boolean.TRUE;
    Long[] array = new Long[] { 3L, 4L, 8L, 9L, 10923012910L };

    collectionClass.getMethod("lock").invoke(object);
    BusinessDTO output = (BusinessDTO) collectionClass.getMethod("sortNumbers", array.getClass(), Boolean.class).invoke(object, array, bool);

    Assert.assertTrue(collectionClass.isInstance(output));
    Assert.assertEquals(Boolean.parseBoolean(booleanInput), collectionClass.getMethod("getABoolean").invoke(output));
    Assert.assertEquals(Long.valueOf(3), collectionClass.getMethod("getALong").invoke(output));
  }

  @Request
  @Test
  public void testInvokeNullParameter() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

    Integer[] output = (Integer[]) collectionClass.getMethod("sortIntegers", ClientRequestIF.class, Integer[].class).invoke(null, clientRequest, null);

    Assert.assertNull(output);
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
    Object output = collectionClass.getMethod("testMethod", collectionClass).invoke(object, object2);

    Assert.assertNull(output);
    Assert.assertEquals(Long.parseLong(input), collectionClass.getMethod("getALong").invoke(object));
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
    BusinessDTO[] output = (BusinessDTO[]) collectionClass.getMethod("sortCollections", array.getClass(), String.class).invoke(object, array, input);

    Assert.assertEquals(Array.getLength(array), output.length);

    for (BusinessDTO dto : output)
    {
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

    Object array = Array.newInstance(collectionClass, 0);

    collectionClass.getMethod("lock").invoke(object);
    collectionClass.getMethod("lock").invoke(object2);
    BusinessDTO[] output = (BusinessDTO[]) collectionClass.getMethod("sortCollections", array.getClass(), String.class).invoke(object, array, input);

    Assert.assertEquals(Array.getLength(array), output.length);

    for (BusinessDTO dto : output)
    {
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

    collectionClass.getMethod("lock").invoke(object);
    collectionClass.getMethod("lock").invoke(object2);
    String[][] output = (String[][]) collectionClass.getMethod("testMultiArray", array4.getClass()).invoke(object, array4);

    Assert.assertEquals(2, output.length);
    Assert.assertEquals(2, output[0].length);
    Assert.assertEquals("Yo my nizzle", output[0][0]);
    Assert.assertEquals("Leroy Im witha or against ya.", output[0][1]);
    Assert.assertEquals(2, output[1].length);
    Assert.assertEquals("[[[[L" + collectionType + ";", output[1][0]);
    Assert.assertEquals("Collection[][][][]", output[1][1]);
  }

  @Request
  @Test
  public void testInvokeMethodOnSubclass() throws Exception
  {
    String longInput = "278";

    Class<?> bagClass = LoaderDecorator.load(bagDTO);
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

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
    Object output = bagClass.getMethod("testMethod", collectionClass).invoke(object, object2);

    Assert.assertNull(output);
    Assert.assertEquals(Long.parseLong(longInput) + 10L, collectionClass.getMethod("getALong").invoke(object));
  }

  @Request
  @Test
  public void testInvokeMethodOnSubArray() throws Exception
  {
    String longInput = "142";
    String input = "H to this izzo, E to the izza";

    Class<?> bagClass = LoaderDecorator.load(bagDTO);
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

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

    Class<?> arrayClass = Array.newInstance(collectionClass, 0).getClass();
    Object array = Array.newInstance(bagClass, 1);
    Array.set(array, 0, object2);

    bagClass.getMethod("lock").invoke(object);
    bagClass.getMethod("lock").invoke(object2);
    BusinessDTO[] output = (BusinessDTO[]) bagClass.getMethod("sortCollections", arrayClass, String.class).invoke(object, array, input);

    Assert.assertEquals(Array.getLength(array), output.length);

    for (BusinessDTO dto : output)
    {
      Assert.assertEquals(input, dto.getValue("aCharacter"));
      Assert.assertEquals("142", dto.getValue("aLong"));
    }
  }

  @Request
  @Test
  public void testInvokeRelationship() throws Exception
  {
    String longInput = "152";

    // Create the existing BusinessDAO
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Class<?> referenceClass = LoaderDecorator.load(referenceType + TypeGeneratorInfo.DTO_SUFFIX);

    BusinessDTO businessDAO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO);

    BusinessDTO businessDAO2 = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO2.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO2);

    RelationshipDTO relationship = (RelationshipDTO) collectionClass.getMethod("addCollection2", collectionClass).invoke(businessDAO, businessDAO2);
    referenceClass.getMethod("apply").invoke(relationship);

    RelationshipDTO[] output = (RelationshipDTO[]) collectionClass.getMethod("getReferences", referenceClass).invoke(businessDAO, relationship);

    Assert.assertEquals(5, output.length);

    for (RelationshipDTO dto : output)
    {
      Assert.assertEquals(businessDAO.getOid(), dto.getParentOid());
      Assert.assertEquals(businessDAO2.getOid(), dto.getChildOid());
    }
  }

  @Request
  @Test
  public void testInvokeNewDefinedAttributeMethod() throws Exception
  {
    String input = "164";

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

    BusinessDTO businessDAO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", input + "3");

    BusinessDTO businessDAO2 = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO2.setValue("aLong", input);

    Object output = collectionClass.getMethod("testMethod", collectionClass).invoke(businessDAO, businessDAO2);

    Assert.assertNull(output);
    Assert.assertEquals(Long.parseLong(input), collectionClass.getMethod("getALong").invoke(businessDAO));
  }

  @Request
  @Test
  public void testInvokeNewRelationship() throws Exception
  {
    String longInput = "152";

    // Create the existing BusinessDAO
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Class<?> referenceClass = LoaderDecorator.load(referenceType + TypeGeneratorInfo.DTO_SUFFIX);

    BusinessDTO businessDAO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO);

    BusinessDTO businessDAO2 = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO2.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO2);

    RelationshipDTO relationship = (RelationshipDTO) collectionClass.getMethod("addCollection2", collectionClass).invoke(businessDAO, businessDAO2);

    RelationshipDTO[] output = (RelationshipDTO[]) collectionClass.getMethod("getReferences", referenceClass).invoke(businessDAO, relationship);

    Assert.assertEquals(5, output.length);

    for (RelationshipDTO dto : output)
    {
      Assert.assertEquals(businessDAO.getOid(), dto.getParentOid());
      Assert.assertEquals(businessDAO2.getOid(), dto.getChildOid());
    }
  }

  @Request
  @Test
  public void testInvokeEnumerationItem() throws Exception
  {
    Class<?> stateClass = LoaderDecorator.load(stateType + TypeGeneratorInfo.DTO_SUFFIX);

    EnumerationDTOIF enumerationDTOIF = (EnumerationDTOIF) stateClass.getMethod("valueOf", String.class).invoke(null, colorado.getValue(EnumerationMasterInfo.NAME));

    BusinessDTO colBusDTO = (BusinessDTO) stateClass.getMethod("item", ClientRequestIF.class).invoke(enumerationDTOIF, clientRequest);

    Assert.assertEquals("item method on the enum returned the wrong object.", colorado.getOid(), colBusDTO.getOid());

    Assert.assertEquals("item method on the enum returned an object of the wrong class.", colorado.getType() + "DTO", colBusDTO.getClass().getName());

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

    String longInput = "152";
    BusinessDTO businessDTO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDTO.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDTO);

    try
    {
      EnumerationDTOIF[] output = (EnumerationDTOIF[]) collectionClass.getMethod("getStates", stateClass).invoke(businessDTO, enumerationDTOIF);
      Assert.assertEquals(5, output.length);

      for (EnumerationDTOIF dto : output)
      {
        Assert.assertEquals(enumerationDTOIF.name(), dto.name());
      }
    }
    finally
    {
      clientRequest.delete(businessDTO.getOid());
    }
  }

  @Request
  @Test
  public void testReturnStates() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Class<?> stateClass = LoaderDecorator.load(stateType + TypeGeneratorInfo.DTO_SUFFIX);

    BusinessDTO businessDTO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    collectionClass.getMethod("apply").invoke(businessDTO);

    EnumerationDTOIF[] enums = new EnumerationDTOIF[3];

    enums[0] = (EnumerationDTOIF) stateClass.getMethod("valueOf", String.class).invoke(null, "CA");
    enums[1] = (EnumerationDTOIF) stateClass.getMethod("valueOf", String.class).invoke(null, "CO");
    enums[2] = (EnumerationDTOIF) stateClass.getMethod("valueOf", String.class).invoke(null, "CT");

    Object arr = Array.newInstance(stateClass, 3);
    Array.set(arr, 0, enums[0]);
    Array.set(arr, 1, enums[1]);
    Array.set(arr, 2, enums[2]);

    Method method = collectionClass.getMethod("returnStates", arr.getClass());

    EnumerationDTOIF[] output = (EnumerationDTOIF[]) method.invoke(businessDTO, arr);

    Assert.assertEquals(3, output.length);
    for (int i = 0; i < 3; i++)
    {
      Assert.assertEquals(enums[i].name(), output[i].name());
    }
  }

  /**
   * Test the isModified persistance on the invoke method when the entityDAO
   * invoked is applied during the execution of the method.
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testIsModified() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    String input = "164";

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

    object.setValue("aDouble", "46");
    Assert.assertEquals(true, object.isModified());
    Assert.assertEquals(true, object.isModified("aDouble"));

    Object output = collectionClass.getMethod("testMethod", collectionClass).invoke(object, object2);

    Assert.assertNull(output);
    Assert.assertEquals(Long.parseLong(input), collectionClass.getMethod("getALong").invoke(object));

    // Since object was applied to the database during the execution of the
    // invoked method
    // the isModified flag should be false because the returned object is the
    // current
    // value stored in the database.
    Assert.assertEquals(false, object.isModified("aDouble"));
    Assert.assertEquals(false, object.isModified());
  }

  /**
   * Tests that if an object is modified within an MdMethod that the returned
   * object (or the instance itself), is marked as modified.
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testModifiedNoPersist() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

    BusinessDTO businessDTO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDTO.setValue("aLong", "11");
    collectionClass.getMethod("apply").invoke(businessDTO);

    String oid = businessDTO.getOid();

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    BusinessDTO object = (BusinessDTO) get.invoke(null, clientRequest, oid);

    BusinessDTO inputDTO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    inputDTO.setValue("aLong", "22");
    collectionClass.getMethod("apply").invoke(inputDTO);

    inputDTO = (BusinessDTO) get.invoke(null, clientRequest, inputDTO.getOid());

    collectionClass.getMethod("lock").invoke(object);
    collectionClass.getMethod("lock").invoke(inputDTO);

    BusinessDTO output = (BusinessDTO) collectionClass.getMethod("modifyNoPersist", collectionClass).invoke(object, inputDTO);

    Assert.assertEquals("77", object.getValue("aLong"));
    Assert.assertEquals(true, object.isModified("aLong"));
    Assert.assertEquals(true, object.isModified());

    Assert.assertEquals("77", output.getValue("aLong"));
    Assert.assertEquals(true, output.isModified("aLong"));
    Assert.assertEquals(true, output.isModified());
  }

  /**
   * Test the isModified persistance on the invoke method when the entityDAO
   * invoked is not applied during the execution of the method.
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testIsModifiedNoApply() throws Exception
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

    Boolean bool = Boolean.TRUE;
    Long[] array = new Long[] { 3L, 4L, 8L, 9L, 10923012910L };

    collectionClass.getMethod("lock").invoke(object);

    object.setValue("aDouble", "46");
    Assert.assertEquals(true, object.isModified());
    Assert.assertEquals(true, object.isModified("aDouble"));

    BusinessDTO output = (BusinessDTO) collectionClass.getMethod("sortNumbers", array.getClass(), Boolean.class).invoke(object, array, bool);

    Assert.assertTrue(collectionClass.isInstance(output));
    Assert.assertEquals(Boolean.parseBoolean(booleanInput), collectionClass.getMethod("getABoolean").invoke(output));
    Assert.assertEquals(Long.valueOf(3), collectionClass.getMethod("getALong").invoke(output));
    Assert.assertEquals(true, object.isModified("aDouble"));
    Assert.assertEquals(true, object.isModified());
  }

  /**
   * Test that the isModified flag is false after invocation when the object has
   * not been altered during or before the method invocation.
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testNotModified() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

    BusinessDTO businessDAO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", "142");
    collectionClass.getMethod("apply").invoke(businessDAO);
    String oid = businessDAO.getOid();

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    BusinessDTO object = (BusinessDTO) get.invoke(null, clientRequest, oid);

    collectionClass.getMethod("poopNothing").invoke(object);

    Assert.assertEquals(false, object.isModified());

    for (String name : object.getAttributeNames())
    {
      Assert.assertEquals(false, object.isModified(name));
    }
  }

  @Request
  @Test
  public void testStaticMethod() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Integer[] integers = { 12, 34, 6, 4, 33, 2 };

    Integer[] output = (Integer[]) collectionClass.getMethod("sortIntegers", ClientRequestIF.class, integers.getClass()).invoke(null, clientRequest, (Object) integers);

    Assert.assertEquals(integers.length, output.length);

    for (int i = 0; i < integers.length - 1; i++)
    {
      Assert.assertTrue(output[i] < output[i + 1]);
    }
  }

  @Request
  @Test
  public void testStaticMethodNoPermission() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Integer[] integers = { 12, 34, 6, 4, 33, 2 };

    try
    {
      collectionClass.getMethod("sortIntegers", ClientRequestIF.class, integers.getClass()).invoke(null, noPermissionRequest, (Object) integers);
      Assert.fail("Able to invoke a method with no permissions");
    }
    catch (InvocationTargetException e)
    {
      // This is expexcted, ensure that the cause of the
      // InvocationTargetExcepiton is a DomainErrorExceptionDTO
      Throwable cause = e.getCause();

      Assert.assertNotNull(cause);
      Assert.assertTrue(cause instanceof ExecuteStaticPermissionExceptionDTO);
    }
  }

  @Request
  @Test
  public void testInvokeMethodNoPermissions() throws Exception
  {
    String longInput = "374364";

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

    BusinessDTO businessDAO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO);
    String oid = businessDAO.getOid();

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    BusinessDTO object = (BusinessDTO) get.invoke(null, noPermissionRequest, oid);

    Boolean bool = Boolean.TRUE;
    Long[] array = new Long[] { 3L, 4L, 8L, 9L, 10923012910L };

    collectionClass.getMethod("lock").invoke(object);
    try
    {
      collectionClass.getMethod("sortNumbers", array.getClass(), Boolean.class).invoke(object, array, bool);
      Assert.fail("Able to execute a instance method without permissions");
    }
    catch (InvocationTargetException e)
    {
      // This is expexcted, ensure that the cause of the
      // InvocationTargetExcepiton
      // is a TypePermissionException_EXECUTE_INSTANCEDTO
      Throwable cause = e.getCause();

      Assert.assertNotNull(cause);
      Assert.assertTrue(cause instanceof ExecuteInstancePermissionExceptionDTO);
    }
  }

  @Request
  @Test
  public void testDateMethod() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Date date = new Date(System.currentTimeMillis());

    Date[] output = (Date[]) collectionClass.getMethod("getDates", ClientRequestIF.class, date.getClass()).invoke(null, clientRequest, date);

    Assert.assertEquals(4, output.length);

    for (int i = 0; i < output.length; i++)
    {
      Assert.assertEquals(date.getTime() + 10L * i, output[i].getTime());
    }
  }

  /**
   * Ensure that an exception is thrown when the actual type is not a child of
   * the declared type
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testInvalidMetadata() throws Exception
  {
    String longInput = "152";

    // Create the existing BusinessDAO
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

    BusinessDTO businessDAO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO);

    BusinessDTO businessDAO2 = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO2.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO2);

    RelationshipDTO relationship = (RelationshipDTO) collectionClass.getMethod("addCollection2", collectionClass).invoke(businessDAO, businessDAO2);

    String[] declaredTypes = { collectionType };

    MethodMetaData metadata = new MethodMetaData(collectionType, "testMethod", declaredTypes);

    try
    {
      clientRequest.invokeMethod(metadata, null, new Object[] { relationship });

      Assert.fail("Able to invoke a method with invalid metadata");
    }
    catch (RuntimeException e)
    {
      // Expected to fail
    }
  }

  /**
   * Test that an exception is thrown when the entity type is different from the
   * class type in the metadata
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testInvalidEntityDTO() throws Exception
  {
    String longInput = "152";

    // Create the existing BusinessDAO
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

    BusinessDTO businessDAO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO);

    BusinessDTO businessDAO2 = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO2.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO2);

    RelationshipDTO relationship = (RelationshipDTO) collectionClass.getMethod("addCollection2", collectionClass).invoke(businessDAO, businessDAO2);

    String[] declaredTypes = {};
    String[] actualTypes = {};

    MethodMetaData metadata = new MethodMetaData(collectionType, "testMethod", declaredTypes);
    metadata.setActualTypes(actualTypes);

    try
    {
      clientRequest.invokeMethod(metadata, relationship, new Object[] {});

      Assert.fail("Able to invoke a method where the entityDTO does not match the metadata class type");
    }
    catch (RuntimeException e)
    {
      // Expected to fail
    }
  }

  @Request
  @Test
  public void testStaticInstanceMethod() throws Exception
  {
    String booleanInput = Boolean.toString(true);
    String longInput = "374364";
    Boolean bool = Boolean.TRUE;
    Long[] array = new Long[] { 3L, 4L, 8L, 9L, 10923012910L };

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    BusinessDTO businessDTO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDTO.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDTO);
    String oid = businessDTO.getOid();

    BusinessDTO output = (BusinessDTO) collectionClass.getMethod("sortNumbers", ClientRequestIF.class, String.class, array.getClass(), Boolean.class).invoke(null, clientRequest, oid, array, bool);

    Assert.assertTrue(collectionClass.isInstance(output));
    Assert.assertEquals(Boolean.parseBoolean(booleanInput), collectionClass.getMethod("getABoolean").invoke(output));
    Assert.assertEquals(Long.valueOf(3), collectionClass.getMethod("getALong").invoke(output));
  }

  @Request
  @Test
  public void testInvokeMethodWithUtilParam() throws Exception
  {
    // Create the existing BusinessDAO
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

    BusinessDTO businessDTO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    collectionClass.getMethod("apply").invoke(businessDTO);
    String oid = businessDTO.getOid();

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    businessDTO = (BusinessDTO) get.invoke(null, clientRequest, oid);

    String someCharValue = "Some Char Value";

    Class<?> utilClass = LoaderDecorator.load(utilDTO);

    Object input = utilClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    utilClass.getMethod("setACharacter", String.class).invoke(input, someCharValue);
    utilClass.getMethod("apply").invoke(input);
    String inputId = (String) utilClass.getMethod("getOid").invoke(input);

    collectionClass.getMethod("lock").invoke(businessDTO);
    Object output = collectionClass.getMethod("getUtil", utilClass).invoke(businessDTO, input);

    String someCharValue2 = (String) utilClass.getMethod("getACharacter").invoke(input);

    String outputId = (String) utilClass.getMethod("getOid").invoke(output);

    Assert.assertEquals(inputId, outputId);

    Assert.assertEquals(someCharValue, someCharValue2);

    collectionClass.getMethod("delete").invoke(businessDTO);
    utilClass.getMethod("delete").invoke(input);
  }

  @Request
  @Test
  public void testInvokeMethodWithViewParam() throws Exception
  {
    // Create the existing BusinessDAO
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

    BusinessDTO businessDTO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    collectionClass.getMethod("apply").invoke(businessDTO);
    String oid = businessDTO.getOid();

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    businessDTO = (BusinessDTO) get.invoke(null, clientRequest, oid);

    String someCharValue = "Some Char Value";

    Class<?> viewClass = LoaderDecorator.load(viewDTO);

    Object input = viewClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    viewClass.getMethod("setACharacter", String.class).invoke(input, someCharValue);
    viewClass.getMethod("apply").invoke(input);
    String inputId = (String) viewClass.getMethod("getOid").invoke(input);

    collectionClass.getMethod("lock").invoke(businessDTO);
    Object output = collectionClass.getMethod("getView", viewClass).invoke(businessDTO, input);

    String someCharValue2 = (String) viewClass.getMethod("getACharacter").invoke(input);

    String outputId = (String) viewClass.getMethod("getOid").invoke(output);

    Assert.assertEquals(inputId, outputId);

    Assert.assertEquals(someCharValue, someCharValue2);

    collectionClass.getMethod("delete").invoke(businessDTO);
    viewClass.getMethod("delete").invoke(input);
  }

  @Request
  @Test
  public void testInvokeMethodWithUtilArrayParam() throws Exception
  {
    // Create the existing BusinessDAO
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

    BusinessDTO businessDTO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    collectionClass.getMethod("apply").invoke(businessDTO);
    String oid = businessDTO.getOid();

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    businessDTO = (BusinessDTO) get.invoke(null, clientRequest, oid);

    Class<?> utilClass = LoaderDecorator.load(utilDTO);
    UtilDTO[] array = (UtilDTO[]) Array.newInstance(utilClass, 5);

    Object input = utilClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    utilClass.getMethod("setACharacter", String.class).invoke(input, "1");
    Array.set(array, 0, input);

    input = utilClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    utilClass.getMethod("setACharacter", String.class).invoke(input, "2");
    Array.set(array, 1, input);

    input = utilClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    utilClass.getMethod("setACharacter", String.class).invoke(input, "3");
    Array.set(array, 2, input);

    input = utilClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    utilClass.getMethod("setACharacter", String.class).invoke(input, "4");
    Array.set(array, 3, input);

    input = utilClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    utilClass.getMethod("setACharacter", String.class).invoke(input, "5");
    Array.set(array, 4, input);

    collectionClass.getMethod("lock").invoke(businessDTO);
    UtilDTO[] output = (UtilDTO[]) collectionClass.getMethod("utilArray", array.getClass()).invoke(businessDTO, (Object) array);

    Assert.assertEquals(array.length, output.length);

    for (int i = 0; i < array.length; i++)
    {
      Assert.assertEquals(array[i].getValue("aCharacter"), output[i].getValue("aCharacter"));
    }

    collectionClass.getMethod("delete").invoke(businessDTO);
  }

  @Request
  @Test
  public void testInvokeMethodWithViewArrayParam() throws Exception
  {
    // Create the existing BusinessDAO
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

    BusinessDTO businessDTO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    collectionClass.getMethod("apply").invoke(businessDTO);
    String oid = businessDTO.getOid();

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    businessDTO = (BusinessDTO) get.invoke(null, clientRequest, oid);

    Class<?> viewClass = LoaderDecorator.load(viewDTO);
    ViewDTO[] array = (ViewDTO[]) Array.newInstance(viewClass, 5);

    Object input = viewClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    viewClass.getMethod("setACharacter", String.class).invoke(input, "1");
    Array.set(array, 0, input);

    input = viewClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    viewClass.getMethod("setACharacter", String.class).invoke(input, "2");
    Array.set(array, 1, input);

    input = viewClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    viewClass.getMethod("setACharacter", String.class).invoke(input, "3");
    Array.set(array, 2, input);

    input = viewClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    viewClass.getMethod("setACharacter", String.class).invoke(input, "4");
    Array.set(array, 3, input);

    input = viewClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    viewClass.getMethod("setACharacter", String.class).invoke(input, "5");
    Array.set(array, 4, input);

    collectionClass.getMethod("lock").invoke(businessDTO);
    ViewDTO[] output = (ViewDTO[]) collectionClass.getMethod("viewArray", array.getClass()).invoke(businessDTO, (Object) array);

    Assert.assertEquals(array.length, output.length);

    for (int i = 0; i < array.length; i++)
    {
      Assert.assertEquals(array[i].getValue("aCharacter"), output[i].getValue("aCharacter"));
    }

    collectionClass.getMethod("delete").invoke(businessDTO);
  }

  @Request
  @Test
  public void testInvokeMethodWithQueryReturnTypeCheckAttributeMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

    Method get = collectionClass.getMethod("getCollectionQuery", ClientRequestIF.class);

    BusinessQueryDTO queryDTO = (BusinessQueryDTO) get.invoke(null, clientRequest);

    List<? extends BusinessDTO> resultSet = queryDTO.getResultSet();

    if (resultSet.size() > 1)
    {
      BusinessDTO instance = resultSet.get(0);
      AttributeCharacterMdDTO md = ComponentDTOFacade.getAttributeCharacterDTO(instance, "aCharacter").getAttributeMdDTO();
//      AdapterTest.checkAttributeMd(collectionMdAttributeCharacterDTO, md);
    }
    else
    {
      Assert.fail("Result set should not be size 0.");
    }
  }

  @Request
  @Test
  public void testInvokeMethodWithViewQueryReturnType() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

    Method getCount = collectionClass.getMethod("getCollectionObjectCount", ClientRequestIF.class);
    Integer recordCount = (Integer) getCount.invoke(null, clientRequest);

    Method get = collectionClass.getMethod("getViewQuery", ClientRequestIF.class);

    ViewQueryDTO queryDTO = (ViewQueryDTO) get.invoke(null, clientRequest);

    Assert.assertEquals(recordCount.intValue(), queryDTO.getResultSet().size());

    Assert.assertEquals(recordCount.intValue(), queryDTO.getCount());
  }

  @Request
  @Test
  public void testInvokeMethodWithBusinessQueryReturnTypeRestictRows() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("getCollectionQueryRestrictRows", ClientRequestIF.class);

    BusinessQueryDTO queryDTO = (BusinessQueryDTO) get.invoke(null, clientRequest);

    Assert.assertEquals(2, queryDTO.getResultSet().size());
  }

  @Request
  @Test
  public void testInvokeMethodWithViewQueryReturnTypeRestrictRows() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("getViewQueryRestrictRows", ClientRequestIF.class);

    ViewQueryDTO queryDTO = (ViewQueryDTO) get.invoke(null, clientRequest);

    Assert.assertEquals(2, queryDTO.getResultSet().size());
  }

  @Request
  @Test
  public void testInvokeMethodOnDisconnectedEntity() throws Exception
  {
    BusinessDTO user = clientRequest.newBusiness(UserInfo.CLASS);
    user.setValue(UserInfo.USERNAME, "Test");
    user.setValue(UserInfo.PASSWORD, "Test");
    clientRequest.createBusiness(user);

    try
    {
      clientRequest.grantTypePermission(user.getOid(), collection.getOid(), Operation.READ.name());
      clientRequest.grantTypePermission(user.getOid(), collection.getOid(), Operation.READ_ALL.name());

      ClientSession session = ClientSession.createUserSession("default", "Test", "Test", new Locale[] { CommonProperties.getDefaultLocale() });

      try
      {
        ClientRequestIF request = session.getRequest();

        String input = "Har har de dar dar";
        String longInput = "152";

        // Create the existing BusinessDAO
        Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
        Object array = Array.newInstance(collectionClass, 0);

        BusinessDTO business = (BusinessDTO) request.newDisconnectedEntity(collectionType);
        business.setValue("aLong", longInput);
        business.setValue("aCharacter", input);

        BusinessDTO[] output = (BusinessDTO[]) collectionClass.getMethod("sortCollections", array.getClass(), String.class).invoke(business, array, input);

        Assert.assertEquals(Array.getLength(array), output.length);

        for (BusinessDTO dto : output)
        {
          Assert.assertEquals(input, dto.getValue("aCharacter"));
          Assert.assertEquals(longInput, dto.getValue("aLong"));
        }
      }
      finally
      {
        if (session != null)
        {
          session.logout();
        }
      }
    }
    finally
    {
      clientRequest.delete(user.getOid());
    }
  }

  @Request
  @Test
  public void testInvokeMethodWithBusinessQueryReturnType() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

    Method getCount = collectionClass.getMethod("getCollectionObjectCount", ClientRequestIF.class);
    Integer recordCount = (Integer) getCount.invoke(null, clientRequest);

    Method get = collectionClass.getMethod("getCollectionQuery", ClientRequestIF.class);

    BusinessQueryDTO queryDTO = (BusinessQueryDTO) get.invoke(null, clientRequest);

    Assert.assertEquals(recordCount.intValue(), queryDTO.getResultSet().size());

    Assert.assertEquals(recordCount.intValue(), queryDTO.getCount());
  }

  @Request
  @Test
  public void testInvokeMethodWithGenericBusinessQueryReturnType() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

    Method getCount = collectionClass.getMethod("getCollectionObjectCount", ClientRequestIF.class);
    Integer recordCount = (Integer) getCount.invoke(null, clientRequest);

    Method get = collectionClass.getMethod("getBusinessQuery", ClientRequestIF.class);

    ComponentQueryDTO queryDTO = (ComponentQueryDTO) get.invoke(null, clientRequest);

    Assert.assertEquals(recordCount.intValue(), queryDTO.getResultSet().size());

    Assert.assertEquals(recordCount.intValue(), queryDTO.getCount());
  }

}
