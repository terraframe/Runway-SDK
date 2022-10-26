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

@com.runwaysdk.business.ClassSignature(hash = 255115855)
public abstract class StateEnumDTOBase extends com.runwaysdk.system.EnumerationMasterDTO implements com.runwaysdk.generation.loader.
{
  public final static String CLASS = "com.runwaysdk.jstest.StateEnum";
  private static final long serialVersionUID = 255115855;
  
  protected StateEnumDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected StateEnumDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public final static java.lang.String ENUMINT = "enumInt";
  public final static java.lang.String STATECODE = "stateCode";
  public final static java.lang.String STATENAME = "stateName";
  public final static java.lang.String STATEPHONE = "statePhone";
  public Integer getEnumInt()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(ENUMINT));
  }
  
  public void setEnumInt(Integer value)
  {
    if(value == null)
    {
      setValue(ENUMINT, "");
    }
    else
    {
      setValue(ENUMINT, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isEnumIntWritable()
  {
    return isWritable(ENUMINT);
  }
  
  public boolean isEnumIntReadable()
  {
    return isReadable(ENUMINT);
  }
  
  public boolean isEnumIntModified()
  {
    return isModified(ENUMINT);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getEnumIntMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(ENUMINT).getAttributeMdDTO();
  }
  
  public String getStateCode()
  {
    return getValue(STATECODE);
  }
  
  public void setStateCode(String value)
  {
    if(value == null)
    {
      setValue(STATECODE, "");
    }
    else
    {
      setValue(STATECODE, value);
    }
  }
  
  public boolean isStateCodeWritable()
  {
    return isWritable(STATECODE);
  }
  
  public boolean isStateCodeReadable()
  {
    return isReadable(STATECODE);
  }
  
  public boolean isStateCodeModified()
  {
    return isModified(STATECODE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getStateCodeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(STATECODE).getAttributeMdDTO();
  }
  
  public String getStateName()
  {
    return getValue(STATENAME);
  }
  
  public void setStateName(String value)
  {
    if(value == null)
    {
      setValue(STATENAME, "");
    }
    else
    {
      setValue(STATENAME, value);
    }
  }
  
  public boolean isStateNameWritable()
  {
    return isWritable(STATENAME);
  }
  
  public boolean isStateNameReadable()
  {
    return isReadable(STATENAME);
  }
  
  public boolean isStateNameModified()
  {
    return isModified(STATENAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getStateNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(STATENAME).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.PhoneNumberDTO getStatePhone()
  {
    return (com.runwaysdk.system.PhoneNumberDTO) this.getAttributeStructDTO(STATEPHONE).getStructDTO();
  }
  
  public boolean isStatePhoneWritable()
  {
    return isWritable(STATEPHONE);
  }
  
  public boolean isStatePhoneReadable()
  {
    return isReadable(STATEPHONE);
  }
  
  public boolean isStatePhoneModified()
  {
    return isModified(STATEPHONE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeStructMdDTO getStatePhoneMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeStructMdDTO) getAttributeDTO(STATEPHONE).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.jstest.StateEnumDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.jstest.StateEnumDTO) dto;
  }
  
  public void apply()
  {
    if(isNewInstance())
    {
      getRequest().createBusiness(this);
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
  
  public static com.runwaysdk.jstest.StateEnumQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.jstest.StateEnumQueryDTO) clientRequest.getAllInstances(com.runwaysdk.jstest.StateEnumDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.jstest.StateEnumDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.StateEnumDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.jstest.StateEnumDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.jstest.StateEnumDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.StateEnumDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.jstest.StateEnumDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
