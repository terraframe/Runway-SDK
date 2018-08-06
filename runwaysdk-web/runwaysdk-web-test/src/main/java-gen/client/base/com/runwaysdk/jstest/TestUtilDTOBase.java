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
package com.runwaysdk.jstest;

@com.runwaysdk.business.ClassSignature(hash = 336400629)
public abstract class TestUtilDTOBase extends com.runwaysdk.business.UtilDTO implements com.runwaysdk.generation.loader.
{
  public final static String CLASS = "com.runwaysdk.jstest.TestUtil";
  private static final long serialVersionUID = 336400629;
  
  protected TestUtilDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String OID = "oid";
  public static java.lang.String UTILCHARACTER = "utilCharacter";
  public static java.lang.String UTILPHONE = "utilPhone";
  public static java.lang.String UTILREFERENCEOBJECT = "utilReferenceObject";
  public static java.lang.String UTILSINGLESTATE = "utilSingleState";
  public String getUtilCharacter()
  {
    return getValue(UTILCHARACTER);
  }
  
  public void setUtilCharacter(String value)
  {
    if(value == null)
    {
      setValue(UTILCHARACTER, "");
    }
    else
    {
      setValue(UTILCHARACTER, value);
    }
  }
  
  public boolean isUtilCharacterWritable()
  {
    return isWritable(UTILCHARACTER);
  }
  
  public boolean isUtilCharacterReadable()
  {
    return isReadable(UTILCHARACTER);
  }
  
  public boolean isUtilCharacterModified()
  {
    return isModified(UTILCHARACTER);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getUtilCharacterMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(UTILCHARACTER).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.PhoneNumberDTO getUtilPhone()
  {
    return (com.runwaysdk.system.PhoneNumberDTO) this.getAttributeStructDTO(UTILPHONE).getStructDTO();
  }
  
  public boolean isUtilPhoneWritable()
  {
    return isWritable(UTILPHONE);
  }
  
  public boolean isUtilPhoneReadable()
  {
    return isReadable(UTILPHONE);
  }
  
  public boolean isUtilPhoneModified()
  {
    return isModified(UTILPHONE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeStructMdDTO getUtilPhoneMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeStructMdDTO) getAttributeDTO(UTILPHONE).getAttributeMdDTO();
  }
  
  public com.runwaysdk.jstest.RefClassDTO getUtilReferenceObject()
  {
    if(getValue(UTILREFERENCEOBJECT) == null || getValue(UTILREFERENCEOBJECT).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.jstest.RefClassDTO.get(getRequest(), getValue(UTILREFERENCEOBJECT));
    }
  }
  
  public String getUtilReferenceObjectId()
  {
    return getValue(UTILREFERENCEOBJECT);
  }
  
  public void setUtilReferenceObject(com.runwaysdk.jstest.RefClassDTO value)
  {
    if(value == null)
    {
      setValue(UTILREFERENCEOBJECT, "");
    }
    else
    {
      setValue(UTILREFERENCEOBJECT, value.getOid());
    }
  }
  
  public boolean isUtilReferenceObjectWritable()
  {
    return isWritable(UTILREFERENCEOBJECT);
  }
  
  public boolean isUtilReferenceObjectReadable()
  {
    return isReadable(UTILREFERENCEOBJECT);
  }
  
  public boolean isUtilReferenceObjectModified()
  {
    return isModified(UTILREFERENCEOBJECT);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getUtilReferenceObjectMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(UTILREFERENCEOBJECT).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<com.runwaysdk.jstest.StatesDTO> getUtilSingleState()
  {
    return (java.util.List<com.runwaysdk.jstest.StatesDTO>) com.runwaysdk.transport.conversion.ConversionFacade.convertEnumDTOsFromEnumNames(getRequest(), com.runwaysdk.jstest.StatesDTO.CLASS, getEnumNames(UTILSINGLESTATE));
  }
  
  public java.util.List<String> getUtilSingleStateEnumNames()
  {
    return getEnumNames(UTILSINGLESTATE);
  }
  
  public void addUtilSingleState(com.runwaysdk.jstest.StatesDTO enumDTO)
  {
    addEnumItem(UTILSINGLESTATE, enumDTO.toString());
  }
  
  public void removeUtilSingleState(com.runwaysdk.jstest.StatesDTO enumDTO)
  {
    removeEnumItem(UTILSINGLESTATE, enumDTO.toString());
  }
  
  public void clearUtilSingleState()
  {
    clearEnum(UTILSINGLESTATE);
  }
  
  public boolean isUtilSingleStateWritable()
  {
    return isWritable(UTILSINGLESTATE);
  }
  
  public boolean isUtilSingleStateReadable()
  {
    return isReadable(UTILSINGLESTATE);
  }
  
  public boolean isUtilSingleStateModified()
  {
    return isModified(UTILSINGLESTATE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO getUtilSingleStateMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO) getAttributeDTO(UTILSINGLESTATE).getAttributeMdDTO();
  }
  
  public static final java.lang.Integer doubleAnInt(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.Integer num)
  {
    String[] _declaredTypes = new String[]{"java.lang.Integer"};
    Object[] _parameters = new Object[]{num};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestUtilDTO.CLASS, "doubleAnInt", _declaredTypes);
    return (java.lang.Integer) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public final com.runwaysdk.jstest.TestUtilDTO returnUtil(com.runwaysdk.jstest.TestUtilDTO input)
  {
    String[] _declaredTypes = new String[]{"com.runwaysdk.jstest.TestUtil"};
    Object[] _parameters = new Object[]{input};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestUtilDTO.CLASS, "returnUtil", _declaredTypes);
    return (com.runwaysdk.jstest.TestUtilDTO) getRequest().invokeMethod(_metadata, this, _parameters);
  }
  
  public static final com.runwaysdk.jstest.TestUtilDTO returnUtil(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid, com.runwaysdk.jstest.TestUtilDTO input)
  {
    String[] _declaredTypes = new String[]{"java.lang.String", "com.runwaysdk.jstest.TestUtil"};
    Object[] _parameters = new Object[]{oid, input};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestUtilDTO.CLASS, "returnUtil", _declaredTypes);
    return (com.runwaysdk.jstest.TestUtilDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static TestUtilDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.UtilDTO dto = (com.runwaysdk.business.UtilDTO)clientRequest.get(oid);
    
    return (TestUtilDTO) dto;
  }
  
  public void apply()
  {
    if(isNewInstance())
    {
      getRequest().createSessionComponent(this);
    }
    else
    {
      getRequest().update(this);
    }
  }
  public void delete()
  {
    getRequest().delete(this.getOid());
  }
  
}
