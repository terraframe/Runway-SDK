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

@com.runwaysdk.business.ClassSignature(hash = 2084379673)
public abstract class MdAttributeNumberDTOBase extends com.runwaysdk.system.metadata.MdAttributePrimitiveDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdAttributeNumber";
  private static final long serialVersionUID = 2084379673;
  
  protected MdAttributeNumberDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdAttributeNumberDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String REJECTNEGATIVE = "rejectNegative";
  public static java.lang.String REJECTPOSITIVE = "rejectPositive";
  public static java.lang.String REJECTZERO = "rejectZero";
  public Boolean getRejectNegative()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(REJECTNEGATIVE));
  }
  
  public void setRejectNegative(Boolean value)
  {
    if(value == null)
    {
      setValue(REJECTNEGATIVE, "");
    }
    else
    {
      setValue(REJECTNEGATIVE, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isRejectNegativeWritable()
  {
    return isWritable(REJECTNEGATIVE);
  }
  
  public boolean isRejectNegativeReadable()
  {
    return isReadable(REJECTNEGATIVE);
  }
  
  public boolean isRejectNegativeModified()
  {
    return isModified(REJECTNEGATIVE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getRejectNegativeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(REJECTNEGATIVE).getAttributeMdDTO();
  }
  
  public Boolean getRejectPositive()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(REJECTPOSITIVE));
  }
  
  public void setRejectPositive(Boolean value)
  {
    if(value == null)
    {
      setValue(REJECTPOSITIVE, "");
    }
    else
    {
      setValue(REJECTPOSITIVE, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isRejectPositiveWritable()
  {
    return isWritable(REJECTPOSITIVE);
  }
  
  public boolean isRejectPositiveReadable()
  {
    return isReadable(REJECTPOSITIVE);
  }
  
  public boolean isRejectPositiveModified()
  {
    return isModified(REJECTPOSITIVE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getRejectPositiveMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(REJECTPOSITIVE).getAttributeMdDTO();
  }
  
  public Boolean getRejectZero()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(REJECTZERO));
  }
  
  public void setRejectZero(Boolean value)
  {
    if(value == null)
    {
      setValue(REJECTZERO, "");
    }
    else
    {
      setValue(REJECTZERO, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isRejectZeroWritable()
  {
    return isWritable(REJECTZERO);
  }
  
  public boolean isRejectZeroReadable()
  {
    return isReadable(REJECTZERO);
  }
  
  public boolean isRejectZeroModified()
  {
    return isModified(REJECTZERO);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getRejectZeroMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(REJECTZERO).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeNumberDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.MdAttributeNumberDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdAttributeNumberQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdAttributeNumberQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdAttributeNumberDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeNumberDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeNumberDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeNumberDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeNumberDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeNumberDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeNumberDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
