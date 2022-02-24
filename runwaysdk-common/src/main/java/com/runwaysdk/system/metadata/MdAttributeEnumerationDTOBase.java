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

@com.runwaysdk.business.ClassSignature(hash = 1808157984)
public abstract class MdAttributeEnumerationDTOBase extends com.runwaysdk.system.metadata.MdAttributeRefDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdAttributeEnumeration";
  private static final long serialVersionUID = 1808157984;
  
  protected MdAttributeEnumerationDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdAttributeEnumerationDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String CACHECOLUMNNAME = "cacheColumnName";
  public static java.lang.String DEFAULTVALUE = "defaultValue";
  public static java.lang.String MDENUMERATION = "mdEnumeration";
  public static java.lang.String SELECTMULTIPLE = "selectMultiple";
  public String getCacheColumnName()
  {
    return getValue(CACHECOLUMNNAME);
  }
  
  public boolean isCacheColumnNameWritable()
  {
    return isWritable(CACHECOLUMNNAME);
  }
  
  public boolean isCacheColumnNameReadable()
  {
    return isReadable(CACHECOLUMNNAME);
  }
  
  public boolean isCacheColumnNameModified()
  {
    return isModified(CACHECOLUMNNAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getCacheColumnNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(CACHECOLUMNNAME).getAttributeMdDTO();
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
  
  public com.runwaysdk.system.metadata.MdEnumerationDTO getMdEnumeration()
  {
    if(getValue(MDENUMERATION) == null || getValue(MDENUMERATION).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdEnumerationDTO.get(getRequest(), getValue(MDENUMERATION));
    }
  }
  
  public String getMdEnumerationId()
  {
    return getValue(MDENUMERATION);
  }
  
  public void setMdEnumeration(com.runwaysdk.system.metadata.MdEnumerationDTO value)
  {
    if(value == null)
    {
      setValue(MDENUMERATION, "");
    }
    else
    {
      setValue(MDENUMERATION, value.getOid());
    }
  }
  
  public boolean isMdEnumerationWritable()
  {
    return isWritable(MDENUMERATION);
  }
  
  public boolean isMdEnumerationReadable()
  {
    return isReadable(MDENUMERATION);
  }
  
  public boolean isMdEnumerationModified()
  {
    return isModified(MDENUMERATION);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getMdEnumerationMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(MDENUMERATION).getAttributeMdDTO();
  }
  
  public Boolean getSelectMultiple()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(SELECTMULTIPLE));
  }
  
  public void setSelectMultiple(Boolean value)
  {
    if(value == null)
    {
      setValue(SELECTMULTIPLE, "");
    }
    else
    {
      setValue(SELECTMULTIPLE, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isSelectMultipleWritable()
  {
    return isWritable(SELECTMULTIPLE);
  }
  
  public boolean isSelectMultipleReadable()
  {
    return isReadable(SELECTMULTIPLE);
  }
  
  public boolean isSelectMultipleModified()
  {
    return isModified(SELECTMULTIPLE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getSelectMultipleMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(SELECTMULTIPLE).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeEnumerationDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.MdAttributeEnumerationDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdAttributeEnumerationQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdAttributeEnumerationQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdAttributeEnumerationDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeEnumerationDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeEnumerationDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeEnumerationDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeEnumerationDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeEnumerationDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeEnumerationDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
