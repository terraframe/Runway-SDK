/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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
 ******************************************************************************/
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

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

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
import com.runwaysdk.constants.DatabaseProperties;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.constants.TestConstants;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.constants.UserInfo;
import com.runwaysdk.dataaccess.io.XMLImporter;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.generation.loader.WebTestGeneratedClassLoader;
import com.runwaysdk.session.ExecuteInstancePermissionExceptionDTO;
import com.runwaysdk.session.ExecuteStaticPermissionExceptionDTO;
import com.runwaysdk.transport.metadata.AttributeCharacterMdDTO;

public class InvokeMethodTest extends InvokeMethodTestBase
{
  /**
   * Launch-point for the standalone textui JUnit tests in this class.
   * 
   * @param args
   */
  public static void main(String[] args)
  {
    if (DatabaseProperties.getDatabaseClass().equals("hsqldb"))
      XMLImporter.main(new String[] { TestConstants.Path.schema_xsd, TestConstants.Path.metadata_xml });

    junit.textui.TestRunner.run(InvokeMethodTest.suite());
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(InvokeMethodTest.class);

    TestSetup wrapper = new TestSetup(suite)
    {
      protected void setUp()
      {
        systemSession = ClientSession.createUserSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });
        clientRequest = systemSession.getRequest();
        classSetUp();
        noPermissionSession = ClientSession.createUserSession("smethie", "aaa", new Locale[] { CommonProperties.getDefaultLocale() });
        noPermissionRequest = noPermissionSession.getRequest();
        finalizeSetup();
      }

      protected void tearDown()
      {
        classTearDown();
      }
    };

    return wrapper;
  }

  public void testMethodActorReadPermissions() throws Exception
  {
    clientRequest.grantTypePermission(methodActor.getId(), collection.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(methodActor.getId(), collection.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(methodActor.getId(), mdAttributeLong.getId(), Operation.READ.name());

    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);

    BusinessDTO businessDTO = null;
    try
    {
      businessDTO = (BusinessDTO) collectionClass.getMethod("methodActorRead", ClientRequestIF.class).invoke(null, noPermissionRequest);
      assertTrue("Attribute is not readable even though method actor has adequate permissions.", businessDTO.isReadable("aLong2"));
    }
    finally
    {
      clientRequest.revokeTypePermission(methodActor.getId(), collection.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(methodActor.getId(), collection.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(methodActor.getId(), mdAttributeLong.getId(), Operation.READ.name());

      if (businessDTO != null)
      {
        clientRequest.delete(businessDTO.getId());
      }

    }
  }

  public void testMethodActorNoReadPermissions() throws Exception
  {
    clientRequest.grantTypePermission(methodActor.getId(), collection.getId(), Operation.CREATE.name());

    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);

    BusinessDTO businessDTO = null;
    try
    {
      businessDTO = (BusinessDTO) collectionClass.getMethod("methodActorRead", ClientRequestIF.class).invoke(null, noPermissionRequest);
      assertFalse("Attribute is readable even though method actor does not have adequate permissions.", businessDTO.isReadable("aLong2"));
    }
    finally
    {
      clientRequest.revokeTypePermission(methodActor.getId(), collection.getId(), Operation.CREATE.name());

      if (businessDTO != null)
      {
        clientRequest.delete(businessDTO.getId());
      }

    }
  }

  @SuppressWarnings("unchecked")
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
      Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);

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

      assertEquals(recordCount, rowCount);
    }
    finally
    {
      clientRequest.delete(collectionObj1.getId());
      clientRequest.delete(collectionObj2.getId());
    }
  }

  @SuppressWarnings("unchecked")
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
      Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);

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

      assertEquals(recordCount, rowCount);
    }
    finally
    {
      clientRequest.delete(collectionObj1.getId());
      clientRequest.delete(collectionObj2.getId());
    }
  }

  public void testInvokeArrayMethod() throws Exception
  {
    String booleanInput = Boolean.toString(true);
    String longInput = "374364";

    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);

    BusinessDTO businessDAO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO);
    String id = businessDAO.getId();

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    BusinessDTO object = (BusinessDTO) get.invoke(null, clientRequest, id);

    Boolean bool = Boolean.TRUE;
    Long[] array = new Long[] { 3L, 4L, 8L, 9L, 10923012910L };

    collectionClass.getMethod("lock").invoke(object);
    BusinessDTO output = (BusinessDTO) collectionClass.getMethod("sortNumbers", array.getClass(), Boolean.class).invoke(object, array, bool);

    assertTrue(collectionClass.isInstance(output));
    assertEquals(Boolean.parseBoolean(booleanInput), collectionClass.getMethod("getABoolean").invoke(output));
    assertEquals(new Long(3), collectionClass.getMethod("getALong").invoke(output));
  }

  public void testInvokeNullParameter() throws Exception
  {
    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);

    Integer[] output = (Integer[]) collectionClass.getMethod("sortIntegers", ClientRequestIF.class, Integer[].class).invoke(null, clientRequest, null);

    assertEquals(null, output);
  }

  public void testInvokeDefinedAttributeMethod() throws Exception
  {
    String input = "164";

    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);

    BusinessDTO businessDAO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", input + "3");
    collectionClass.getMethod("apply").invoke(businessDAO);
    String id = businessDAO.getId();

    BusinessDTO businessDAO2 = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO2.setValue("aLong", input);
    collectionClass.getMethod("apply").invoke(businessDAO2);
    String id2 = businessDAO2.getId();

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    BusinessDTO object = (BusinessDTO) get.invoke(null, clientRequest, id);
    BusinessDTO object2 = (BusinessDTO) get.invoke(null, clientRequest, id2);

    collectionClass.getMethod("lock").invoke(object);
    collectionClass.getMethod("lock").invoke(object2);
    Object output = collectionClass.getMethod("testMethod", collectionClass).invoke(object, object2);

    assertNull(output);
    assertEquals(Long.parseLong(input), collectionClass.getMethod("getALong").invoke(object));
  }

  public void testInvokeDefinedArrayMethod() throws Exception
  {
    String input = "Har har bar bar";
    String longInput = "1";

    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);

    BusinessDTO businessDAO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO);
    String id = businessDAO.getId();

    BusinessDTO businessDAO2 = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO2.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO2);
    String id2 = businessDAO2.getId();

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    BusinessDTO object = (BusinessDTO) get.invoke(null, clientRequest, id);
    BusinessDTO object2 = (BusinessDTO) get.invoke(null, clientRequest, id2);

    Object array = Array.newInstance(collectionClass, 1);
    Array.set(array, 0, object2);

    collectionClass.getMethod("lock").invoke(object);
    collectionClass.getMethod("lock").invoke(object2);
    BusinessDTO[] output = (BusinessDTO[]) collectionClass.getMethod("sortCollections", array.getClass(), String.class).invoke(object, array, input);

    assertEquals(Array.getLength(array), output.length);

    for (BusinessDTO dto : output)
    {
      assertEquals(input, dto.getValue("aCharacter"));
      assertEquals(longInput, dto.getValue("aLong"));
    }
  }

  public void testInvokeEmptyArrayMethod() throws Exception
  {
    String input = "Har har de dar dar";
    String longInput = "152";

    // Create the existing BusinessDAO
    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);

    BusinessDTO businessDAO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO);
    String id = businessDAO.getId();

    BusinessDTO businessDAO2 = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO2.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO2);
    String id2 = businessDAO2.getId();

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    BusinessDTO object = (BusinessDTO) get.invoke(null, clientRequest, id);
    BusinessDTO object2 = (BusinessDTO) get.invoke(null, clientRequest, id2);

    Object array = Array.newInstance(collectionClass, 0);

    collectionClass.getMethod("lock").invoke(object);
    collectionClass.getMethod("lock").invoke(object2);
    BusinessDTO[] output = (BusinessDTO[]) collectionClass.getMethod("sortCollections", array.getClass(), String.class).invoke(object, array, input);

    assertEquals(Array.getLength(array), output.length);

    for (BusinessDTO dto : output)
    {
      assertEquals(input, dto.getValue("aCharacter"));
      assertEquals(longInput, dto.getValue("aLong"));
    }
  }

  public void testInvokeMultiArrayMethod() throws Exception
  {
    // Create the existing BusinessDAO
    String longInput = "163";
    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);

    BusinessDTO businessDAO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO);
    String id = businessDAO.getId();

    BusinessDTO businessDAO2 = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO2.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO2);
    String id2 = businessDAO2.getId();

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    BusinessDTO object = (BusinessDTO) get.invoke(null, clientRequest, id);
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

    assertEquals(2, output.length);
    assertEquals(2, output[0].length);
    assertEquals("Yo my nizzle", output[0][0]);
    assertEquals("Leroy Im witha or against ya.", output[0][1]);
    assertEquals(2, output[1].length);
    assertEquals("[[[[L" + collectionType + ";", output[1][0]);
    assertEquals("Collection[][][][]", output[1][1]);
  }

  public void testInvokeMethodOnSubclass() throws Exception
  {
    String longInput = "278";

    Class<?> bagClass = WebTestGeneratedClassLoader.load(bagDTO);
    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);

    BusinessDTO businessDAO = (BusinessDTO) bagClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", longInput + "0");
    bagClass.getMethod("apply").invoke(businessDAO);
    String id = businessDAO.getId();

    BusinessDTO businessDAO2 = (BusinessDTO) bagClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO2.setValue("aLong", longInput);
    bagClass.getMethod("apply").invoke(businessDAO2);
    String id2 = businessDAO2.getId();

    Method get = bagClass.getMethod("get", ClientRequestIF.class, String.class);
    BusinessDTO object = (BusinessDTO) get.invoke(null, clientRequest, id);
    BusinessDTO object2 = (BusinessDTO) get.invoke(null, clientRequest, id2);

    bagClass.getMethod("lock").invoke(object);
    bagClass.getMethod("lock").invoke(object2);
    Object output = bagClass.getMethod("testMethod", collectionClass).invoke(object, object2);

    assertNull(output);
    assertEquals(Long.parseLong(longInput) + 10L, collectionClass.getMethod("getALong").invoke(object));
  }

  public void testInvokeMethodOnSubArray() throws Exception
  {
    String longInput = "142";
    String input = "H to this izzo, E to the izza";

    Class<?> bagClass = WebTestGeneratedClassLoader.load(bagDTO);
    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);

    BusinessDTO businessDAO = (BusinessDTO) bagClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", longInput + "0");
    bagClass.getMethod("apply").invoke(businessDAO);
    String id = businessDAO.getId();

    BusinessDTO businessDAO2 = (BusinessDTO) bagClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO2.setValue("aLong", longInput);
    bagClass.getMethod("apply").invoke(businessDAO2);
    String id2 = businessDAO2.getId();

    Method get = bagClass.getMethod("get", ClientRequestIF.class, String.class);
    BusinessDTO object = (BusinessDTO) get.invoke(null, clientRequest, id);
    BusinessDTO object2 = (BusinessDTO) get.invoke(null, clientRequest, id2);

    Class<?> arrayClass = Array.newInstance(collectionClass, 0).getClass();
    Object array = Array.newInstance(bagClass, 1);
    Array.set(array, 0, object2);

    bagClass.getMethod("lock").invoke(object);
    bagClass.getMethod("lock").invoke(object2);
    BusinessDTO[] output = (BusinessDTO[]) bagClass.getMethod("sortCollections", arrayClass, String.class).invoke(object, array, input);

    assertEquals(Array.getLength(array), output.length);

    for (BusinessDTO dto : output)
    {
      assertEquals(input, dto.getValue("aCharacter"));
      assertEquals("142", dto.getValue("aLong"));
    }
  }

  public void testInvokeRelationship() throws Exception
  {
    String longInput = "152";

    // Create the existing BusinessDAO
    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);
    Class<?> referenceClass = WebTestGeneratedClassLoader.load(referenceType + TypeGeneratorInfo.DTO_SUFFIX);

    BusinessDTO businessDAO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO);

    BusinessDTO businessDAO2 = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO2.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO2);

    RelationshipDTO relationship = (RelationshipDTO) collectionClass.getMethod("addCollection2", collectionClass).invoke(businessDAO, businessDAO2);
    referenceClass.getMethod("apply").invoke(relationship);

    RelationshipDTO[] output = (RelationshipDTO[]) collectionClass.getMethod("getReferences", referenceClass).invoke(businessDAO, relationship);

    assertEquals(5, output.length);

    for (RelationshipDTO dto : output)
    {
      assertEquals(businessDAO.getId(), dto.getParentId());
      assertEquals(businessDAO2.getId(), dto.getChildId());
    }
  }

  public void testInvokeNewDefinedAttributeMethod() throws Exception
  {
    String input = "164";

    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);

    BusinessDTO businessDAO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", input + "3");

    BusinessDTO businessDAO2 = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO2.setValue("aLong", input);

    Object output = collectionClass.getMethod("testMethod", collectionClass).invoke(businessDAO, businessDAO2);

    assertNull(output);
    assertEquals(Long.parseLong(input), collectionClass.getMethod("getALong").invoke(businessDAO));
  }

  public void testInvokeNewRelationship() throws Exception
  {
    String longInput = "152";

    // Create the existing BusinessDAO
    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);
    Class<?> referenceClass = WebTestGeneratedClassLoader.load(referenceType + TypeGeneratorInfo.DTO_SUFFIX);

    BusinessDTO businessDAO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO);

    BusinessDTO businessDAO2 = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO2.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO2);

    RelationshipDTO relationship = (RelationshipDTO) collectionClass.getMethod("addCollection2", collectionClass).invoke(businessDAO, businessDAO2);

    RelationshipDTO[] output = (RelationshipDTO[]) collectionClass.getMethod("getReferences", referenceClass).invoke(businessDAO, relationship);

    assertEquals(5, output.length);

    for (RelationshipDTO dto : output)
    {
      assertEquals(businessDAO.getId(), dto.getParentId());
      assertEquals(businessDAO2.getId(), dto.getChildId());
    }
  }

  public void testInvokeEnumerationItem() throws Exception
  {
    Class<?> stateClass = WebTestGeneratedClassLoader.load(stateType + TypeGeneratorInfo.DTO_SUFFIX);

    EnumerationDTOIF enumerationDTOIF = (EnumerationDTOIF) stateClass.getMethod("valueOf", String.class).invoke(null, colorado.getValue(EnumerationMasterInfo.NAME));

    BusinessDTO colBusDTO = (BusinessDTO) stateClass.getMethod("item", ClientRequestIF.class).invoke(enumerationDTOIF, clientRequest);

    assertEquals("item method on the enum returned the wrong object.", colorado.getId(), colBusDTO.getId());

    assertEquals("item method on the enum returned an object of the wrong class.", colorado.getClass().getName(), colBusDTO.getClass().getName());

    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);

    String longInput = "152";
    BusinessDTO businessDTO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDTO.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDTO);

    try
    {
      EnumerationDTOIF[] output = (EnumerationDTOIF[]) collectionClass.getMethod("getStates", stateClass).invoke(businessDTO, enumerationDTOIF);
      assertEquals(5, output.length);

      for (EnumerationDTOIF dto : output)
      {
        assertEquals(enumerationDTOIF.name(), dto.name());
      }
    }
    finally
    {
      clientRequest.delete(businessDTO.getId());
    }
  }

  public void testReturnStates() throws Exception
  {
    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);
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

    assertEquals(3, output.length);
    for (int i = 0; i < 3; i++)
    {
      assertEquals(enums[i].name(), output[i].name());
    }
  }

  /**
   * Test the isModified persistance on the invoke method when the entityDAO
   * invoked is applied during the execution of the method.
   * 
   * @throws Exception
   */
  public void testIsModified() throws Exception
  {
    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);
    String input = "164";

    BusinessDTO businessDAO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", input + "3");
    collectionClass.getMethod("apply").invoke(businessDAO);
    String id = businessDAO.getId();

    BusinessDTO businessDAO2 = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO2.setValue("aLong", input);
    collectionClass.getMethod("apply").invoke(businessDAO2);
    String id2 = businessDAO2.getId();

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    BusinessDTO object = (BusinessDTO) get.invoke(null, clientRequest, id);
    BusinessDTO object2 = (BusinessDTO) get.invoke(null, clientRequest, id2);

    collectionClass.getMethod("lock").invoke(object);
    collectionClass.getMethod("lock").invoke(object2);

    object.setValue("aDouble", "46");
    assertEquals(true, object.isModified());
    assertEquals(true, object.isModified("aDouble"));

    Object output = collectionClass.getMethod("testMethod", collectionClass).invoke(object, object2);

    assertNull(output);
    assertEquals(Long.parseLong(input), collectionClass.getMethod("getALong").invoke(object));

    // Since object was applied to the database during the execution of the
    // invoked method
    // the isModified flag should be false because the returned object is the
    // current
    // value stored in the database.
    assertEquals(false, object.isModified("aDouble"));
    assertEquals(false, object.isModified());
  }

  /**
   * Tests that if an object is modified within an MdMethod that the returned
   * object (or the instance itself), is marked as modified.
   * 
   * @throws Exception
   */
  public void testModifiedNoPersist() throws Exception
  {
    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);

    BusinessDTO businessDTO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDTO.setValue("aLong", "11");
    collectionClass.getMethod("apply").invoke(businessDTO);

    String id = businessDTO.getId();

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    BusinessDTO object = (BusinessDTO) get.invoke(null, clientRequest, id);

    BusinessDTO inputDTO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    inputDTO.setValue("aLong", "22");
    collectionClass.getMethod("apply").invoke(inputDTO);

    inputDTO = (BusinessDTO) get.invoke(null, clientRequest, inputDTO.getId());

    collectionClass.getMethod("lock").invoke(object);
    collectionClass.getMethod("lock").invoke(inputDTO);

    BusinessDTO output = (BusinessDTO) collectionClass.getMethod("modifyNoPersist", collectionClass).invoke(object, inputDTO);

    assertEquals("77", object.getValue("aLong"));
    assertEquals(true, object.isModified("aLong"));
    assertEquals(true, object.isModified());

    assertEquals("77", output.getValue("aLong"));
    assertEquals(true, output.isModified("aLong"));
    assertEquals(true, output.isModified());
  }

  /**
   * Test the isModified persistance on the invoke method when the entityDAO
   * invoked is not applied during the execution of the method.
   * 
   * @throws Exception
   */
  public void testIsModifiedNoApply() throws Exception
  {
    String booleanInput = Boolean.toString(true);
    String longInput = "374364";

    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);

    BusinessDTO businessDAO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO);
    String id = businessDAO.getId();

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    BusinessDTO object = (BusinessDTO) get.invoke(null, clientRequest, id);

    Boolean bool = Boolean.TRUE;
    Long[] array = new Long[] { 3L, 4L, 8L, 9L, 10923012910L };

    collectionClass.getMethod("lock").invoke(object);

    object.setValue("aDouble", "46");
    assertEquals(true, object.isModified());
    assertEquals(true, object.isModified("aDouble"));

    BusinessDTO output = (BusinessDTO) collectionClass.getMethod("sortNumbers", array.getClass(), Boolean.class).invoke(object, array, bool);

    assertTrue(collectionClass.isInstance(output));
    assertEquals(Boolean.parseBoolean(booleanInput), collectionClass.getMethod("getABoolean").invoke(output));
    assertEquals(new Long(3), collectionClass.getMethod("getALong").invoke(output));
    assertEquals(true, object.isModified("aDouble"));
    assertEquals(true, object.isModified());
  }

  /**
   * Test that the isModified flag is false after invocation when the object has
   * not been altered during or before the method invocation.
   * 
   * @throws Exception
   */
  public void testNotModified() throws Exception
  {
    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);

    BusinessDTO businessDAO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", "142");
    collectionClass.getMethod("apply").invoke(businessDAO);
    String id = businessDAO.getId();

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    BusinessDTO object = (BusinessDTO) get.invoke(null, clientRequest, id);

    collectionClass.getMethod("poopNothing").invoke(object);

    assertEquals(false, object.isModified());

    for (String name : object.getAttributeNames())
    {
      assertEquals(false, object.isModified(name));
    }
  }

  public void testStaticMethod() throws Exception
  {
    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);
    Integer[] integers = { 12, 34, 6, 4, 33, 2 };

    Integer[] output = (Integer[]) collectionClass.getMethod("sortIntegers", ClientRequestIF.class, integers.getClass()).invoke(null, clientRequest, (Object) integers);

    assertEquals(integers.length, output.length);

    for (int i = 0; i < integers.length - 1; i++)
    {
      assertTrue(output[i] < output[i + 1]);
    }
  }

  public void testStaticMethodNoPermission() throws Exception
  {
    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);
    Integer[] integers = { 12, 34, 6, 4, 33, 2 };

    try
    {
      collectionClass.getMethod("sortIntegers", ClientRequestIF.class, integers.getClass()).invoke(null, noPermissionRequest, (Object) integers);
      fail("Able to invoke a method with no permissions");
    }
    catch (InvocationTargetException e)
    {
      // This is expexcted, ensure that the cause of the
      // InvocationTargetExcepiton is a DomainErrorExceptionDTO
      Throwable cause = e.getCause();

      assertNotNull(cause);
      assertTrue(cause instanceof ExecuteStaticPermissionExceptionDTO);
    }
  }

  public void testInvokeMethodNoPermissions() throws Exception
  {
    String longInput = "374364";

    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);

    BusinessDTO businessDAO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO);
    String id = businessDAO.getId();

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    BusinessDTO object = (BusinessDTO) get.invoke(null, noPermissionRequest, id);

    Boolean bool = Boolean.TRUE;
    Long[] array = new Long[] { 3L, 4L, 8L, 9L, 10923012910L };

    collectionClass.getMethod("lock").invoke(object);
    try
    {
      collectionClass.getMethod("sortNumbers", array.getClass(), Boolean.class).invoke(object, array, bool);
      fail("Able to execute a instance method without permissions");
    }
    catch (InvocationTargetException e)
    {
      // This is expexcted, ensure that the cause of the
      // InvocationTargetExcepiton
      // is a TypePermissionException_EXECUTE_INSTANCEDTO
      Throwable cause = e.getCause();

      assertNotNull(cause);
      assertTrue(cause instanceof ExecuteInstancePermissionExceptionDTO);
    }
  }

  public void testDateMethod() throws Exception
  {
    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);
    Date date = new Date(System.currentTimeMillis());

    Date[] output = (Date[]) collectionClass.getMethod("getDates", ClientRequestIF.class, date.getClass()).invoke(null, clientRequest, date);

    assertEquals(4, output.length);

    for (int i = 0; i < output.length; i++)
    {
      assertEquals(date.getTime() + 10L * i, output[i].getTime());
    }
  }

  /**
   * Ensure that an exception is thrown when the actual type is not a child of
   * the declared type
   * 
   * @throws Exception
   */
  public void testInvalidMetadata() throws Exception
  {
    String longInput = "152";

    // Create the existing BusinessDAO
    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);

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

      fail("Able to invoke a method with invalid metadata");
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
  public void testInvalidEntityDTO() throws Exception
  {
    String longInput = "152";

    // Create the existing BusinessDAO
    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);

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

      fail("Able to invoke a method where the entityDTO does not match the metadata class type");
    }
    catch (RuntimeException e)
    {
      // Expected to fail
    }
  }

  public void testStaticInstanceMethod() throws Exception
  {
    String booleanInput = Boolean.toString(true);
    String longInput = "374364";
    Boolean bool = Boolean.TRUE;
    Long[] array = new Long[] { 3L, 4L, 8L, 9L, 10923012910L };

    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);
    BusinessDTO businessDTO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDTO.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDTO);
    String id = businessDTO.getId();

    BusinessDTO output = (BusinessDTO) collectionClass.getMethod("sortNumbers", ClientRequestIF.class, String.class, array.getClass(), Boolean.class).invoke(null, clientRequest, id, array, bool);

    assertTrue(collectionClass.isInstance(output));
    assertEquals(Boolean.parseBoolean(booleanInput), collectionClass.getMethod("getABoolean").invoke(output));
    assertEquals(new Long(3), collectionClass.getMethod("getALong").invoke(output));
  }

  public void testInvokeMethodWithUtilParam() throws Exception
  {
    // Create the existing BusinessDAO
    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);

    BusinessDTO businessDTO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    collectionClass.getMethod("apply").invoke(businessDTO);
    String id = businessDTO.getId();

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    businessDTO = (BusinessDTO) get.invoke(null, clientRequest, id);

    String someCharValue = "Some Char Value";

    Class<?> utilClass = WebTestGeneratedClassLoader.load(utilDTO);

    Object input = utilClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    utilClass.getMethod("setACharacter", String.class).invoke(input, someCharValue);
    utilClass.getMethod("apply").invoke(input);
    String inputId = (String) utilClass.getMethod("getId").invoke(input);

    collectionClass.getMethod("lock").invoke(businessDTO);
    Object output = collectionClass.getMethod("getUtil", utilClass).invoke(businessDTO, input);

    String someCharValue2 = (String) utilClass.getMethod("getACharacter").invoke(input);

    String outputId = (String) utilClass.getMethod("getId").invoke(output);

    assertEquals(inputId, outputId);

    assertEquals(someCharValue, someCharValue2);

    collectionClass.getMethod("delete").invoke(businessDTO);
    utilClass.getMethod("delete").invoke(input);
  }

  public void testInvokeMethodWithViewParam() throws Exception
  {
    // Create the existing BusinessDAO
    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);

    BusinessDTO businessDTO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    collectionClass.getMethod("apply").invoke(businessDTO);
    String id = businessDTO.getId();

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    businessDTO = (BusinessDTO) get.invoke(null, clientRequest, id);

    String someCharValue = "Some Char Value";

    Class<?> viewClass = WebTestGeneratedClassLoader.load(viewDTO);

    Object input = viewClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    viewClass.getMethod("setACharacter", String.class).invoke(input, someCharValue);
    viewClass.getMethod("apply").invoke(input);
    String inputId = (String) viewClass.getMethod("getId").invoke(input);

    collectionClass.getMethod("lock").invoke(businessDTO);
    Object output = collectionClass.getMethod("getView", viewClass).invoke(businessDTO, input);

    String someCharValue2 = (String) viewClass.getMethod("getACharacter").invoke(input);

    String outputId = (String) viewClass.getMethod("getId").invoke(output);

    assertEquals(inputId, outputId);

    assertEquals(someCharValue, someCharValue2);

    collectionClass.getMethod("delete").invoke(businessDTO);
    viewClass.getMethod("delete").invoke(input);
  }

  public void testInvokeMethodWithUtilArrayParam() throws Exception
  {
    // Create the existing BusinessDAO
    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);

    BusinessDTO businessDTO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    collectionClass.getMethod("apply").invoke(businessDTO);
    String id = businessDTO.getId();

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    businessDTO = (BusinessDTO) get.invoke(null, clientRequest, id);

    Class<?> utilClass = WebTestGeneratedClassLoader.load(utilDTO);
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

    assertEquals(array.length, output.length);

    for (int i = 0; i < array.length; i++)
    {
      assertEquals(array[i].getValue("aCharacter"), output[i].getValue("aCharacter"));
    }

    collectionClass.getMethod("delete").invoke(businessDTO);
  }

  public void testInvokeMethodWithViewArrayParam() throws Exception
  {
    // Create the existing BusinessDAO
    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);

    BusinessDTO businessDTO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    collectionClass.getMethod("apply").invoke(businessDTO);
    String id = businessDTO.getId();

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    businessDTO = (BusinessDTO) get.invoke(null, clientRequest, id);

    Class<?> viewClass = WebTestGeneratedClassLoader.load(viewDTO);
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

    assertEquals(array.length, output.length);

    for (int i = 0; i < array.length; i++)
    {
      assertEquals(array[i].getValue("aCharacter"), output[i].getValue("aCharacter"));
    }

    collectionClass.getMethod("delete").invoke(businessDTO);
  }

  public void testInvokeMethodWithQueryReturnTypeCheckAttributeMetadata() throws Exception
  {
    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);

    Method get = collectionClass.getMethod("getCollectionQuery", ClientRequestIF.class);

    BusinessQueryDTO queryDTO = (BusinessQueryDTO) get.invoke(null, clientRequest);

    List<? extends BusinessDTO> resultSet = queryDTO.getResultSet();

    if (resultSet.size() > 1)
    {
      BusinessDTO instance = resultSet.get(0);
      AttributeCharacterMdDTO md = ComponentDTOFacade.getAttributeCharacterDTO(instance, "aCharacter").getAttributeMdDTO();
      AdapterTest.checkAttributeMd(collectionMdAttributeCharacterDTO, md);
    }
    else
    {
      fail("Result set should not be size 0.");
    }
  }

  public void testInvokeMethodWithViewQueryReturnType() throws Exception
  {
    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);

    Method getCount = collectionClass.getMethod("getCollectionObjectCount", ClientRequestIF.class);
    Integer recordCount = (Integer) getCount.invoke(null, clientRequest);

    Method get = collectionClass.getMethod("getViewQuery", ClientRequestIF.class);

    ViewQueryDTO queryDTO = (ViewQueryDTO) get.invoke(null, clientRequest);

    assertEquals(recordCount.intValue(), queryDTO.getResultSet().size());

    assertEquals(recordCount.intValue(), queryDTO.getCount());
  }

  public void testInvokeMethodWithBusinessQueryReturnTypeRestictRows() throws Exception
  {
    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);
    Method get = collectionClass.getMethod("getCollectionQueryRestrictRows", ClientRequestIF.class);

    BusinessQueryDTO queryDTO = (BusinessQueryDTO) get.invoke(null, clientRequest);

    assertEquals(2, queryDTO.getResultSet().size());
  }

  public void testInvokeMethodWithViewQueryReturnTypeRestrictRows() throws Exception
  {
    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);
    Method get = collectionClass.getMethod("getViewQueryRestrictRows", ClientRequestIF.class);

    ViewQueryDTO queryDTO = (ViewQueryDTO) get.invoke(null, clientRequest);

    assertEquals(2, queryDTO.getResultSet().size());
  }

  public void testInvokeMethodOnDisconnectedEntity() throws Exception
  {
    BusinessDTO user = clientRequest.newBusiness(UserInfo.CLASS);
    user.setValue(UserInfo.USERNAME, "Test");
    user.setValue(UserInfo.PASSWORD, "Test");
    clientRequest.createBusiness(user);

    try
    {
      clientRequest.grantTypePermission(user.getId(), collection.getId(), Operation.READ.name());
      clientRequest.grantTypePermission(user.getId(), collection.getId(), Operation.READ_ALL.name());

      ClientSession session = ClientSession.createUserSession("default", "Test", "Test", new Locale[] { CommonProperties.getDefaultLocale() });

      try
      {
        ClientRequestIF request = session.getRequest();

        String input = "Har har de dar dar";
        String longInput = "152";

        // Create the existing BusinessDAO
        Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);
        Object array = Array.newInstance(collectionClass, 0);

        BusinessDTO business = (BusinessDTO) request.newDisconnectedEntity(collectionType);
        business.setValue("aLong", longInput);
        business.setValue("aCharacter", input);

        BusinessDTO[] output = (BusinessDTO[]) collectionClass.getMethod("sortCollections", array.getClass(), String.class).invoke(business, array, input);

        assertEquals(Array.getLength(array), output.length);

        for (BusinessDTO dto : output)
        {
          assertEquals(input, dto.getValue("aCharacter"));
          assertEquals(longInput, dto.getValue("aLong"));
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
      clientRequest.delete(user.getId());
    }
  }

  public void testInvokeMethodWithBusinessQueryReturnType() throws Exception
  {
    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);

    Method getCount = collectionClass.getMethod("getCollectionObjectCount", ClientRequestIF.class);
    Integer recordCount = (Integer) getCount.invoke(null, clientRequest);

    Method get = collectionClass.getMethod("getCollectionQuery", ClientRequestIF.class);

    BusinessQueryDTO queryDTO = (BusinessQueryDTO) get.invoke(null, clientRequest);

    assertEquals(recordCount.intValue(), queryDTO.getResultSet().size());

    assertEquals(recordCount.intValue(), queryDTO.getCount());
  }

  public void testInvokeMethodWithGenericBusinessQueryReturnType() throws Exception
  {
    Class<?> collectionClass = WebTestGeneratedClassLoader.load(collectionDTO);

    Method getCount = collectionClass.getMethod("getCollectionObjectCount", ClientRequestIF.class);
    Integer recordCount = (Integer) getCount.invoke(null, clientRequest);

    Method get = collectionClass.getMethod("getBusinessQuery", ClientRequestIF.class);

    ComponentQueryDTO queryDTO = (ComponentQueryDTO) get.invoke(null, clientRequest);

    assertEquals(recordCount.intValue(), queryDTO.getResultSet().size());

    assertEquals(recordCount.intValue(), queryDTO.getCount());
  }

}
