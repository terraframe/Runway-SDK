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
package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = 1325653525)
public abstract class MdMobileCharacterDTOBase extends com.runwaysdk.system.metadata.MdMobilePrimitiveDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdMobileCharacter";
  private static final long serialVersionUID = 1325653525;
  
  protected MdMobileCharacterDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdMobileCharacterDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public final static java.lang.String DISPLAYLENGTH = "displayLength";
  public final static java.lang.String MAXLENGTH = "maxLength";
  public Integer getDisplayLength()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(DISPLAYLENGTH));
  }
  
  public void setDisplayLength(Integer value)
  {
    if(value == null)
    {
      setValue(DISPLAYLENGTH, "");
    }
    else
    {
      setValue(DISPLAYLENGTH, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isDisplayLengthWritable()
  {
    return isWritable(DISPLAYLENGTH);
  }
  
  public boolean isDisplayLengthReadable()
  {
    return isReadable(DISPLAYLENGTH);
  }
  
  public boolean isDisplayLengthModified()
  {
    return isModified(DISPLAYLENGTH);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getDisplayLengthMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(DISPLAYLENGTH).getAttributeMdDTO();
  }
  
  public Integer getMaxLength()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(MAXLENGTH));
  }
  
  public void setMaxLength(Integer value)
  {
    if(value == null)
    {
      setValue(MAXLENGTH, "");
    }
    else
    {
      setValue(MAXLENGTH, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isMaxLengthWritable()
  {
    return isWritable(MAXLENGTH);
  }
  
  public boolean isMaxLengthReadable()
  {
    return isReadable(MAXLENGTH);
  }
  
  public boolean isMaxLengthModified()
  {
    return isModified(MAXLENGTH);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getMaxLengthMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(MAXLENGTH).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.metadata.MdMobileCharacterDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.MdMobileCharacterDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdMobileCharacterQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdMobileCharacterQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdMobileCharacterDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdMobileCharacterDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdMobileCharacterDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdMobileCharacterDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdMobileCharacterDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdMobileCharacterDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdMobileCharacterDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
