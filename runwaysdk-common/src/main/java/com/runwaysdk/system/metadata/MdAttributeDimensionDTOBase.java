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

@com.runwaysdk.business.ClassSignature(hash = -1653722200)
public abstract class MdAttributeDimensionDTOBase extends com.runwaysdk.system.metadata.MetadataDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdAttributeDimension";
  private static final long serialVersionUID = -1653722200;
  
  protected MdAttributeDimensionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdAttributeDimensionDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String DEFAULTVALUE = "defaultValue";
  public static java.lang.String DEFININGMDATTRIBUTE = "definingMdAttribute";
  public static java.lang.String DEFININGMDDIMENSION = "definingMdDimension";
  public static java.lang.String REQUIRED = "required";
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
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getDefaultValueMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(DEFAULTVALUE).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MdAttributeDTO getDefiningMdAttribute()
  {
    if(getValue(DEFININGMDATTRIBUTE) == null || getValue(DEFININGMDATTRIBUTE).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdAttributeDTO.get(getRequest(), getValue(DEFININGMDATTRIBUTE));
    }
  }
  
  public String getDefiningMdAttributeId()
  {
    return getValue(DEFININGMDATTRIBUTE);
  }
  
  public void setDefiningMdAttribute(com.runwaysdk.system.metadata.MdAttributeDTO value)
  {
    if(value == null)
    {
      setValue(DEFININGMDATTRIBUTE, "");
    }
    else
    {
      setValue(DEFININGMDATTRIBUTE, value.getOid());
    }
  }
  
  public boolean isDefiningMdAttributeWritable()
  {
    return isWritable(DEFININGMDATTRIBUTE);
  }
  
  public boolean isDefiningMdAttributeReadable()
  {
    return isReadable(DEFININGMDATTRIBUTE);
  }
  
  public boolean isDefiningMdAttributeModified()
  {
    return isModified(DEFININGMDATTRIBUTE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getDefiningMdAttributeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(DEFININGMDATTRIBUTE).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MdDimensionDTO getDefiningMdDimension()
  {
    if(getValue(DEFININGMDDIMENSION) == null || getValue(DEFININGMDDIMENSION).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdDimensionDTO.get(getRequest(), getValue(DEFININGMDDIMENSION));
    }
  }
  
  public String getDefiningMdDimensionId()
  {
    return getValue(DEFININGMDDIMENSION);
  }
  
  public void setDefiningMdDimension(com.runwaysdk.system.metadata.MdDimensionDTO value)
  {
    if(value == null)
    {
      setValue(DEFININGMDDIMENSION, "");
    }
    else
    {
      setValue(DEFININGMDDIMENSION, value.getOid());
    }
  }
  
  public boolean isDefiningMdDimensionWritable()
  {
    return isWritable(DEFININGMDDIMENSION);
  }
  
  public boolean isDefiningMdDimensionReadable()
  {
    return isReadable(DEFININGMDDIMENSION);
  }
  
  public boolean isDefiningMdDimensionModified()
  {
    return isModified(DEFININGMDDIMENSION);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getDefiningMdDimensionMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(DEFININGMDDIMENSION).getAttributeMdDTO();
  }
  
  public Boolean getRequired()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(REQUIRED));
  }
  
  public void setRequired(Boolean value)
  {
    if(value == null)
    {
      setValue(REQUIRED, "");
    }
    else
    {
      setValue(REQUIRED, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isRequiredWritable()
  {
    return isWritable(REQUIRED);
  }
  
  public boolean isRequiredReadable()
  {
    return isReadable(REQUIRED);
  }
  
  public boolean isRequiredModified()
  {
    return isModified(REQUIRED);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getRequiredMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(REQUIRED).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeDimensionDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.MdAttributeDimensionDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdAttributeDimensionQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdAttributeDimensionQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdAttributeDimensionDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeDimensionDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeDimensionDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeDimensionDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeDimensionDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeDimensionDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeDimensionDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
