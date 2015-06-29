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

@com.runwaysdk.business.ClassSignature(hash = -174170198)
public abstract class MdAttributeLongDTOBase extends com.runwaysdk.system.metadata.MdAttributeIntDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdAttributeLong";
  private static final long serialVersionUID = -174170198;
  
  protected MdAttributeLongDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdAttributeLongDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String DEFAULTVALUE = "defaultValue";
  public static java.lang.String ENDRANGE = "endRange";
  public static java.lang.String STARTRANGE = "startRange";
  public Long getDefaultValue()
  {
    return com.runwaysdk.constants.MdAttributeLongUtil.getTypeSafeValue(getValue(DEFAULTVALUE));
  }
  
  public void setDefaultValue(Long value)
  {
    if(value == null)
    {
      setValue(DEFAULTVALUE, "");
    }
    else
    {
      setValue(DEFAULTVALUE, java.lang.Long.toString(value));
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
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getDefaultValueMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(DEFAULTVALUE).getAttributeMdDTO();
  }
  
  public Long getEndRange()
  {
    return com.runwaysdk.constants.MdAttributeLongUtil.getTypeSafeValue(getValue(ENDRANGE));
  }
  
  public void setEndRange(Long value)
  {
    if(value == null)
    {
      setValue(ENDRANGE, "");
    }
    else
    {
      setValue(ENDRANGE, java.lang.Long.toString(value));
    }
  }
  
  public boolean isEndRangeWritable()
  {
    return isWritable(ENDRANGE);
  }
  
  public boolean isEndRangeReadable()
  {
    return isReadable(ENDRANGE);
  }
  
  public boolean isEndRangeModified()
  {
    return isModified(ENDRANGE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getEndRangeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(ENDRANGE).getAttributeMdDTO();
  }
  
  public Long getStartRange()
  {
    return com.runwaysdk.constants.MdAttributeLongUtil.getTypeSafeValue(getValue(STARTRANGE));
  }
  
  public void setStartRange(Long value)
  {
    if(value == null)
    {
      setValue(STARTRANGE, "");
    }
    else
    {
      setValue(STARTRANGE, java.lang.Long.toString(value));
    }
  }
  
  public boolean isStartRangeWritable()
  {
    return isWritable(STARTRANGE);
  }
  
  public boolean isStartRangeReadable()
  {
    return isReadable(STARTRANGE);
  }
  
  public boolean isStartRangeModified()
  {
    return isModified(STARTRANGE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getStartRangeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(STARTRANGE).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeLongDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.MdAttributeLongDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdAttributeLongQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdAttributeLongQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdAttributeLongDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeLongDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeLongDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeLongDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeLongDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeLongDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeLongDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
