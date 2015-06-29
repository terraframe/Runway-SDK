/**
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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
import java.util.Date;

import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.EnumerationDTOIF;
import com.runwaysdk.business.SessionDTO;
import com.runwaysdk.business.UtilDTO;
import com.runwaysdk.business.ViewDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.generation.loader.WebTestGeneratedClassLoader;

public abstract class InvokeSessionComponentMethodTest extends InvokeSessionComponentMethodTestBase
{

  public void testInvokeArrayMethod() throws Exception
  {
    String booleanInput = Boolean.toString(true);
    String longInput = "374364";
    
    Class<?> sessionClass = WebTestGeneratedClassLoader.load(sessionDTOtype);
    
    SessionDTO sessionDTO = (SessionDTO) sessionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    sessionDTO.setValue("aLong", longInput);
    sessionClass.getMethod("apply").invoke(sessionDTO);
    String id = sessionDTO.getId();
 
    Method get = sessionClass.getMethod("get", ClientRequestIF.class, String.class);
    SessionDTO object = (SessionDTO) get.invoke(null, clientRequest, id);
    
    Boolean bool = Boolean.TRUE;
    Long[] array = new Long[]{3L, 4L, 8L, 9L, 10923012910L};
    
    SessionDTO output = (SessionDTO) sessionClass.getMethod("sortNumbers", array.getClass(), Boolean.class).invoke(object, array, bool);    
    
    assertTrue(sessionClass.isInstance(output));
    assertEquals(Boolean.parseBoolean(booleanInput), sessionClass.getMethod("getABoolean").invoke(output));
    assertEquals(new Long(3), sessionClass.getMethod("getALong").invoke(output));
  }
  
  public void testInvokeDefinedAttributeMethod() throws Exception
  {
    String input = "164";
    
    Class<?> sessionClass = WebTestGeneratedClassLoader.load(sessionDTOtype);
    
    SessionDTO sessionDTO1 = (SessionDTO) sessionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    sessionDTO1.setValue("aLong", input + "3");
    sessionClass.getMethod("apply").invoke(sessionDTO1);
    String id = sessionDTO1.getId();
    
    SessionDTO sessionDTO2 = (SessionDTO) sessionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    sessionDTO2.setValue("aLong", input);
    sessionClass.getMethod("apply").invoke(sessionDTO2);
    String id2 = sessionDTO2.getId();
 
    Method get = sessionClass.getMethod("get", ClientRequestIF.class, String.class);
    SessionDTO object = (SessionDTO) get.invoke(null, clientRequest, id);
    SessionDTO object2 = (SessionDTO) get.invoke(null, clientRequest, id2);

    Object output = sessionClass.getMethod("testMethod", sessionClass).invoke(object, object2);    

    assertNull(output);
    assertEquals(Long.parseLong(input), sessionClass.getMethod("getALong").invoke(object));
  }

  public void testInvokeDefinedArrayMethod() throws Exception
  {
    String input = "Har har bar bar";
    String longInput = "1";

    Class<?> sessionClass = WebTestGeneratedClassLoader.load(sessionDTOtype);
    
    SessionDTO sessionDTO1 = (SessionDTO) sessionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    sessionDTO1.setValue("aLong", longInput);
    sessionClass.getMethod("apply").invoke(sessionDTO1);
    String id = sessionDTO1.getId();
    
    SessionDTO sessionDTO2 = (SessionDTO) sessionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    sessionDTO2.setValue("aLong", longInput);
    sessionClass.getMethod("apply").invoke(sessionDTO2);
    String id2 = sessionDTO2.getId();
 
    Method get = sessionClass.getMethod("get", ClientRequestIF.class, String.class);
    SessionDTO object = (SessionDTO) get.invoke(null, clientRequest, id);
    SessionDTO object2 = (SessionDTO) get.invoke(null, clientRequest, id2);
    
    Object array = Array.newInstance(sessionClass, 1);
    Array.set(array, 0, object2);

    SessionDTO[] output = (SessionDTO[]) sessionClass.getMethod("sortSessions", array.getClass(), String.class).invoke(object, array, input);    

    assertEquals(Array.getLength(array), output.length);
    
    for(SessionDTO dto : output)
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
    Class<?> sessionClass = WebTestGeneratedClassLoader.load(sessionDTOtype);
    
    SessionDTO sessionDTO1 = (SessionDTO) sessionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    sessionDTO1.setValue("aLong", longInput);
    sessionClass.getMethod("apply").invoke(sessionDTO1);
    String id = sessionDTO1.getId();
    
    SessionDTO sessionDTO2 = (SessionDTO) sessionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    sessionDTO2.setValue("aLong", longInput);
    sessionClass.getMethod("apply").invoke(sessionDTO2);
 
    Method get = sessionClass.getMethod("get", ClientRequestIF.class, String.class);
    SessionDTO object = (SessionDTO) get.invoke(null, clientRequest, id);
    
    Object array = Array.newInstance(sessionClass, 0);

    SessionDTO[] output = (SessionDTO[]) sessionClass.getMethod("sortSessions", array.getClass(), String.class).invoke(object, array, input);    

    assertEquals(Array.getLength(array), output.length);
    
    for(SessionDTO dto : output)
    {
      assertEquals(input, dto.getValue("aCharacter"));
      assertEquals(longInput, dto.getValue("aLong"));
    }
  }
  
  public void testInvokeMultiArrayMethod() throws Exception
  {
    // Create the existing BusinessDAO
    String longInput = "163";
    Class<?> sessionClass = WebTestGeneratedClassLoader.load(sessionDTOtype);
    
    SessionDTO sessionDTO1 = (SessionDTO) sessionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    sessionDTO1.setValue("aLong", longInput);
    sessionClass.getMethod("apply").invoke(sessionDTO1);
    String id = sessionDTO1.getId();
    
    SessionDTO sessionDAO2 = (SessionDTO) sessionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    sessionDAO2.setValue("aLong", longInput);
    sessionClass.getMethod("apply").invoke(sessionDAO2);
    String id2 = sessionDAO2.getId();
 
    Method get = sessionClass.getMethod("get", ClientRequestIF.class, String.class);
    SessionDTO object = (SessionDTO) get.invoke(null, clientRequest, id);
    SessionDTO object2 = (SessionDTO) get.invoke(null, clientRequest, id2);
    
    Object array = Array.newInstance(sessionClass, 1);
    Array.set(array, 0, object2);

    Object array2 = Array.newInstance(array.getClass(), 1);
    Array.set(array2, 0, array);
    
    Object array3 = Array.newInstance(array2.getClass(), 1);
    Array.set(array3, 0, array2);
    
    Object array4 = Array.newInstance(array3.getClass(), 1);
    Array.set(array4, 0, array3);
       
    String[][] output = (String[][]) sessionClass.getMethod("testMultiArray", array4.getClass()).invoke(object, array4);    

    assertEquals(2, output.length);
    assertEquals(2, output[0].length);
    assertEquals("Yo my nizzle", output[0][0]);
    assertEquals("Leroy Im witha or against ya.", output[0][1]);
    assertEquals(2, output[1].length);
    assertEquals("[[[[L" + sessionType + ";", output[1][0]);
    assertEquals(sessionTypeName+"[][][][]", output[1][1]);
  }

  public void testInvokeMethodOnSubclass() throws Exception
  {
    String longInput = "278";
    
    Class<?> bagClass = WebTestGeneratedClassLoader.load(bagDTO);
    Class<?> sessionClass = WebTestGeneratedClassLoader.load(sessionDTOtype);
    
    SessionDTO sessionDTO1 = (SessionDTO) bagClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    sessionDTO1.setValue("aLong", longInput + "0");
    bagClass.getMethod("apply").invoke(sessionDTO1);
    String id = sessionDTO1.getId();
    
    SessionDTO sessionDTO2 = (SessionDTO) bagClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    sessionDTO2.setValue("aLong", longInput);
    bagClass.getMethod("apply").invoke(sessionDTO2);
    String id2 = sessionDTO2.getId();
 
    Method get = bagClass.getMethod("get", ClientRequestIF.class, String.class);
    SessionDTO object = (SessionDTO) get.invoke(null, clientRequest, id);
    SessionDTO object2 = (SessionDTO) get.invoke(null, clientRequest, id2);

    Object output = bagClass.getMethod("testMethod", sessionClass).invoke(object, object2);    
   
    assertNull(output);
    assertEquals(Long.parseLong(longInput) + 10L, sessionClass.getMethod("getALong").invoke(object));
  }

  public void testInvokeMethodOnSubArray() throws Exception
  {
    String longInput =  "142";
    String input = "H to this izzo, E to the izza";
    
    Class<?> bagClass = WebTestGeneratedClassLoader.load(bagDTO);
    Class<?> sessionClass = WebTestGeneratedClassLoader.load(sessionDTOtype);
    
    SessionDTO sessionDTO1 = (SessionDTO) bagClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    sessionDTO1.setValue("aLong", longInput + "0");
    bagClass.getMethod("apply").invoke(sessionDTO1);
    String id = sessionDTO1.getId();
    
    SessionDTO sessionDTO2 = (SessionDTO) bagClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    sessionDTO2.setValue("aLong", longInput);
    bagClass.getMethod("apply").invoke(sessionDTO2);
    String id2 = sessionDTO2.getId();
 
    Method get = bagClass.getMethod("get", ClientRequestIF.class, String.class);
    SessionDTO object = (SessionDTO) get.invoke(null, clientRequest, id);
    SessionDTO object2 = (SessionDTO) get.invoke(null, clientRequest, id2);
    
    Class<?> arrayClass = Array.newInstance(sessionClass, 0).getClass();
    Object array = Array.newInstance(bagClass, 1);
    Array.set(array, 0, object2);

    SessionDTO[] output = (SessionDTO[]) bagClass.getMethod("sortSessions", arrayClass, String.class).invoke(object, array, input);    

    assertEquals(Array.getLength(array), output.length);
    
    for(SessionDTO dto : output)
    {
      assertEquals(input, dto.getValue("aCharacter"));
      assertEquals("142", dto.getValue("aLong"));
    }
  }

  public void testInvokeNewDefinedAttributeMethod() throws Exception
  {
    String input = "164";
    
    Class<?> sessionClass = WebTestGeneratedClassLoader.load(sessionDTOtype);
    
    SessionDTO sessionDTO1 = (SessionDTO) sessionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    sessionDTO1.setValue("aLong", input + "3");
    
    SessionDTO sessionDTO2 = (SessionDTO) sessionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    sessionDTO2.setValue("aLong", input);
 
    Object output = sessionClass.getMethod("testMethod", sessionClass).invoke(sessionDTO1, sessionDTO2);    

    assertNull(output);
    assertEquals(Long.parseLong(input), sessionClass.getMethod("getALong").invoke(sessionDTO1));
  }

  public void testInvokeEnumeration() throws Exception
  {
    Class<?> stateClass = WebTestGeneratedClassLoader.load(stateType + TypeGeneratorInfo.DTO_SUFFIX);

    EnumerationDTOIF enumerationDTOIF = (EnumerationDTOIF) stateClass.getMethod("valueOf", String.class).invoke(null, colorado.getValue(EnumerationMasterInfo.NAME));
    
    BusinessDTO colBusDTO = (BusinessDTO)stateClass.getMethod("item", ClientRequestIF.class).invoke(enumerationDTOIF, clientRequest);

    assertEquals("item method on the enum returned the wrong object.", colorado.getId(), colBusDTO.getId());
    
    assertEquals("item method on the enum returned an object of the wrong class.", colorado.getClass().getName(), colBusDTO.getClass().getName());
    
    Class<?> sessionClass = WebTestGeneratedClassLoader.load(sessionDTOtype);
    
    String longInput = "152";
    SessionDTO sessionDTO = (SessionDTO) sessionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    sessionDTO.setValue("aLong", longInput);
    sessionClass.getMethod("apply").invoke(sessionDTO);
    
    EnumerationDTOIF[] output = (EnumerationDTOIF[]) sessionClass.getMethod("getStates", stateClass).invoke(sessionDTO, enumerationDTOIF);       
    assertEquals(5, output.length);
   
    for(EnumerationDTOIF dto : output)
    {
      assertEquals(enumerationDTOIF.name(), dto.name());
    }  
  }
  
  /**
   * Test the isModified persistance on the invoke method when the 
   * sessionDTO invoked is applied during the execution of the method.
   * 
   * @throws Exception
   */
  public void testIsModified() throws Exception
  {
    Class<?> collectionClass = WebTestGeneratedClassLoader.load(sessionDTOtype);
    String input = "164";
        
    SessionDTO sessionDTO1 = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    sessionDTO1.setValue("aLong", input + "3");
    collectionClass.getMethod("apply").invoke(sessionDTO1);
    String id = sessionDTO1.getId();
    
    SessionDTO sessionDTO2 = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    sessionDTO2.setValue("aLong", input);
    collectionClass.getMethod("apply").invoke(sessionDTO2);
    String id2 = sessionDTO2.getId();
 
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    SessionDTO object = (SessionDTO) get.invoke(null, clientRequest, id);
    SessionDTO object2 = (SessionDTO) get.invoke(null, clientRequest, id2);
    
    object.setValue("aDouble", "46");
    assertEquals(true, object.isModified());
    assertEquals(true, object.isModified("aDouble"));
        
    Object output = collectionClass.getMethod("testMethod", collectionClass).invoke(object, object2);    

    assertNull(output);
    assertEquals(Long.parseLong(input), collectionClass.getMethod("getALong").invoke(object));
    
    //Since object was applied to the database during the execution of the invoked method
    //the isModified flag should be false because the returned object is the current
    //value stored in the database.
    assertEquals(false, object.isModified("aDouble"));
    assertEquals(false, object.isModified());
  }

  /**
   * Test the isModified persistance on the invoke method when the 
   * sessionDTO invoked is not applied during the execution of the method.
   * 
   * @throws Exception
   */
  public void testIsModifiedNoApply() throws Exception
  {
    String booleanInput = Boolean.toString(true);
    String longInput = "374364";
    
    Class<?> sessionClass = WebTestGeneratedClassLoader.load(sessionDTOtype);
    
    SessionDTO sessionDTO1 = (SessionDTO) sessionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    sessionDTO1.setValue("aLong", longInput);
    sessionClass.getMethod("apply").invoke(sessionDTO1);
    String id = sessionDTO1.getId();
 
    Method get = sessionClass.getMethod("get", ClientRequestIF.class, String.class);
    SessionDTO object = (SessionDTO) get.invoke(null, clientRequest, id);
    
    Boolean bool = Boolean.TRUE;
    Long[] array = new Long[]{3L, 4L, 8L, 9L, 10923012910L};
    
    object.setValue("aDouble", "46");
    assertEquals(true, object.isModified());
    assertEquals(true, object.isModified("aDouble"));

    SessionDTO output = (SessionDTO) sessionClass.getMethod("sortNumbers", array.getClass(), Boolean.class).invoke(object, array, bool);    
    
    assertTrue(sessionClass.isInstance(output));
    assertEquals(Boolean.parseBoolean(booleanInput), sessionClass.getMethod("getABoolean").invoke(output));
    assertEquals(new Long(3), sessionClass.getMethod("getALong").invoke(output));
    assertEquals(true, object.isModified("aDouble"));
    assertEquals(true, object.isModified());
  }

  /**
   * Test that the isModified flag is false after invocation
   * when the object has not been altered during or before
   * the method invocation.
   * 
   * @throws Exception
   */
  public void testNotModified() throws Exception
  {
    Class<?> collectionClass = WebTestGeneratedClassLoader.load(sessionDTOtype);
    
    SessionDTO sessionDTO1 = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    sessionDTO1.setValue("aLong", "142");
    collectionClass.getMethod("apply").invoke(sessionDTO1);
    String id = sessionDTO1.getId();
 
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    SessionDTO object = (SessionDTO) get.invoke(null, clientRequest, id);

    collectionClass.getMethod("poopNothing").invoke(object);        

    assertEquals(false, object.isModified());

    for(String name : object.getAttributeNames())
    {
      assertEquals(false, object.isModified(name));
    }
  }

  public void testStaticMethod() throws Exception
  {
    Class<?> collectionClass = WebTestGeneratedClassLoader.load(sessionDTOtype);
    Integer[] integers = {12, 34, 6, 4, 33, 2};
    
    Integer[] output = (Integer[]) collectionClass.getMethod("sortIntegers", ClientRequestIF.class, integers.getClass()).invoke(null, clientRequest, (Object) integers);        

    assertEquals(integers.length, output.length);
    
    for(int i = 0; i < integers.length - 1; i++)
    {
      assertTrue(output[i] < output[i+1]);
    }
  }
  
  public void testDateMethod() throws Exception
  {
    Class<?> collectionClass = WebTestGeneratedClassLoader.load(sessionDTOtype);
    Date date = new Date(System.currentTimeMillis());
    
    Date[] output = (Date[]) collectionClass.getMethod("getDates", ClientRequestIF.class, date.getClass()).invoke(null, clientRequest, date);        

    assertEquals(4, output.length);
    
    for(int i = 0; i < output.length; i++)
    {
      assertEquals(date.getTime() + 10L * i, output[i].getTime());
    }
  }


  public void testStaticInstanceMethod() throws Exception
  {
    String booleanInput = Boolean.toString(true);
    String longInput = "374364";
    Boolean bool = Boolean.TRUE;
    Long[] array = new Long[]{3L, 4L, 8L, 9L, 10923012910L};
    
    Class<?> sessionClass = WebTestGeneratedClassLoader.load(sessionDTOtype);    
    SessionDTO sessionDTO1 = (SessionDTO) sessionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    sessionDTO1.setValue("aLong", longInput);
    sessionClass.getMethod("apply").invoke(sessionDTO1);
    String id = sessionDTO1.getId();    
    
    SessionDTO output = (SessionDTO) sessionClass.getMethod("sortNumbers", ClientRequestIF.class, String.class, array.getClass(), Boolean.class).invoke(null, clientRequest, id,  array, bool);    
    
    assertTrue(sessionClass.isInstance(output));
    assertEquals(Boolean.parseBoolean(booleanInput), sessionClass.getMethod("getABoolean").invoke(output));
    assertEquals(new Long(3), sessionClass.getMethod("getALong").invoke(output));
  }
  
  
  public void testInvokeMethodWithUtilParam() throws Exception
  {   
    // Create the existing SessionDTO
    Class<?> sessionClass = WebTestGeneratedClassLoader.load(sessionDTOtype);
    
    SessionDTO sessionDTO1 = (SessionDTO) sessionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    sessionClass.getMethod("apply").invoke(sessionDTO1);
    String id = sessionDTO1.getId();
 
    Method get = sessionClass.getMethod("get", ClientRequestIF.class, String.class);
    sessionDTO1 = (SessionDTO) get.invoke(null, clientRequest, id);

    String someCharValue = "Some Char Value";
    
    Class<?> utilClass = WebTestGeneratedClassLoader.load(utilParamDTO);
    
    Object input = utilClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    utilClass.getMethod("setACharacter", String.class).invoke(input, someCharValue);
    utilClass.getMethod("apply").invoke(input);  
    String inputId = (String)utilClass.getMethod("getId").invoke(input);

    Object output = sessionClass.getMethod("getUtil", utilClass).invoke(sessionDTO1, input);    

    String someCharValue2 = (String)utilClass.getMethod("getACharacter").invoke(input);
    
    String outputId = (String)utilClass.getMethod("getId").invoke(output);
    
    assertEquals(inputId, outputId);

    assertEquals(someCharValue, someCharValue2);
    
    sessionClass.getMethod("delete").invoke(sessionDTO1);
    utilClass.getMethod("delete").invoke(input);
  }

  public void testInvokeMethodWithViewParam() throws Exception
  {   
    // Create the existing SessionDTO
    Class<?> sessionClass = WebTestGeneratedClassLoader.load(sessionDTOtype);
    
    SessionDTO sessionDTO1 = (SessionDTO) sessionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    sessionClass.getMethod("apply").invoke(sessionDTO1);
    String id = sessionDTO1.getId();
 
    Method get = sessionClass.getMethod("get", ClientRequestIF.class, String.class);
    sessionDTO1 = (SessionDTO) get.invoke(null, clientRequest, id);

    String someCharValue = "Some Char Value";
    
    Class<?> viewClass = WebTestGeneratedClassLoader.load(viewParamDTO);
    
    Object input = viewClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    viewClass.getMethod("setACharacter", String.class).invoke(input, someCharValue);
    viewClass.getMethod("apply").invoke(input);  
    String inputId = (String)viewClass.getMethod("getId").invoke(input);

    Object output = sessionClass.getMethod("getView", viewClass).invoke(sessionDTO1, input);    

    String someCharValue2 = (String)viewClass.getMethod("getACharacter").invoke(input);
    
    String outputId = (String)viewClass.getMethod("getId").invoke(output);
    
    assertEquals(inputId, outputId);

    assertEquals(someCharValue, someCharValue2);
    
    sessionClass.getMethod("delete").invoke(sessionDTO1);
    viewClass.getMethod("delete").invoke(input);
  }
  
  public void testInvokeMethodWithUtilArrayParam() throws Exception
  {   
    // Create the existing SessionDTO
    Class<?> sessionClass = WebTestGeneratedClassLoader.load(sessionDTOtype);
    
    SessionDTO sessionDTO1 = (SessionDTO) sessionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    sessionClass.getMethod("apply").invoke(sessionDTO1);
    String id = sessionDTO1.getId();
 
    Method get = sessionClass.getMethod("get", ClientRequestIF.class, String.class);
    sessionDTO1 = (SessionDTO) get.invoke(null, clientRequest, id);
    
    Class<?> utilClass = WebTestGeneratedClassLoader.load(utilParamDTO);
    UtilDTO[] array = (UtilDTO[])Array.newInstance(utilClass, 5); 
    
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

    UtilDTO[] output = (UtilDTO[])sessionClass.getMethod("utilArray", array.getClass()).invoke(sessionDTO1, (Object)array); 

    assertEquals(array.length, output.length);
    
    for (int i = 0; i< array.length; i++)
    {
      assertEquals(array[i].getValue("aCharacter"), output[i].getValue("aCharacter")); 
    }
    
    sessionClass.getMethod("delete").invoke(sessionDTO1);
  }

  public void testInvokeMethodWithViewArrayParam() throws Exception
  {   
    // Create the existing SessionDTO
    Class<?> sessionClass = WebTestGeneratedClassLoader.load(sessionDTOtype);
    
    SessionDTO businessDTO = (SessionDTO) sessionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    sessionClass.getMethod("apply").invoke(businessDTO);
    String id = businessDTO.getId();
 
    Method get = sessionClass.getMethod("get", ClientRequestIF.class, String.class);
    businessDTO = (SessionDTO) get.invoke(null, clientRequest, id);
    
    Class<?> viewClass = WebTestGeneratedClassLoader.load(viewParamDTO);
    ViewDTO[] array = (ViewDTO[])Array.newInstance(viewClass, 5); 
    
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

    ViewDTO[] output = (ViewDTO[])sessionClass.getMethod("viewArray", array.getClass()).invoke(businessDTO, (Object)array); 

    assertEquals(array.length, output.length);
    
    for (int i = 0; i< array.length; i++)
    {
      assertEquals(array[i].getValue("aCharacter"), output[i].getValue("aCharacter")); 
    }
    
    sessionClass.getMethod("delete").invoke(businessDTO);
  }
}
