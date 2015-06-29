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
package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = 751353802)
public abstract class MdAttributeCharacterDTOBase extends com.runwaysdk.system.metadata.MdAttributeCharDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdAttributeCharacter";
  private static final long serialVersionUID = 751353802;
  
  protected MdAttributeCharacterDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdAttributeCharacterDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String DATABASESIZE = "databaseSize";
  public static java.lang.String DEFAULTVALUE = "defaultValue";
  public Integer getDatabaseSize()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(DATABASESIZE));
  }
  
  public void setDatabaseSize(Integer value)
  {
    if(value == null)
    {
      setValue(DATABASESIZE, "");
    }
    else
    {
      setValue(DATABASESIZE, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isDatabaseSizeWritable()
  {
    return isWritable(DATABASESIZE);
  }
  
  public boolean isDatabaseSizeReadable()
  {
    return isReadable(DATABASESIZE);
  }
  
  public boolean isDatabaseSizeModified()
  {
    return isModified(DATABASESIZE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getDatabaseSizeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(DATABASESIZE).getAttributeMdDTO();
  }
  
  public String getDefaultValue()
  {
    return getValue(DEFAULTVALUE);
  }
  
  public void setDefaultValue(String value)
  {
    if(value == null)
    {
      setValue(DEFAULTVALUE, "");
    }
    else
    {
      setValue(DEFAULTVALUE, value);
    }
  }
  
  public boolean isDefaultValueWritable()
  {
    return isWritable(DEFAULTVALUE);
  }
  
  public boolean isDefaultValueReadable()
  {
    return isReadable(DEFAULTVALUE);
  }
  
  public boolean isDefaultValueModified()
  {
    return isModified(DEFAULTVALUE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getDefaultValueMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(DEFAULTVALUE).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeCharacterDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.MdAttributeCharacterDTO) dto;
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
    getRequest().delete(this.getId());
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeCharacterQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdAttributeCharacterQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdAttributeCharacterDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeCharacterDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeCharacterDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeCharacterDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeCharacterDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeCharacterDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeCharacterDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
