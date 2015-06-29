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

@com.runwaysdk.business.ClassSignature(hash = 1669329841)
public abstract class MdWebDateDTOBase extends com.runwaysdk.system.metadata.MdWebMomentDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdWebDate";
  private static final long serialVersionUID = 1669329841;
  
  protected MdWebDateDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdWebDateDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String AFTERTODAYEXCLUSIVE = "afterTodayExclusive";
  public static java.lang.String AFTERTODAYINCLUSIVE = "afterTodayInclusive";
  public static java.lang.String BEFORETODAYEXCLUSIVE = "beforeTodayExclusive";
  public static java.lang.String BEFORETODAYINCLUSIVE = "beforeTodayInclusive";
  public static java.lang.String ENDDATE = "endDate";
  public static java.lang.String STARTDATE = "startDate";
  public Boolean getAfterTodayExclusive()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(AFTERTODAYEXCLUSIVE));
  }
  
  public void setAfterTodayExclusive(Boolean value)
  {
    if(value == null)
    {
      setValue(AFTERTODAYEXCLUSIVE, "");
    }
    else
    {
      setValue(AFTERTODAYEXCLUSIVE, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isAfterTodayExclusiveWritable()
  {
    return isWritable(AFTERTODAYEXCLUSIVE);
  }
  
  public boolean isAfterTodayExclusiveReadable()
  {
    return isReadable(AFTERTODAYEXCLUSIVE);
  }
  
  public boolean isAfterTodayExclusiveModified()
  {
    return isModified(AFTERTODAYEXCLUSIVE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getAfterTodayExclusiveMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(AFTERTODAYEXCLUSIVE).getAttributeMdDTO();
  }
  
  public Boolean getAfterTodayInclusive()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(AFTERTODAYINCLUSIVE));
  }
  
  public void setAfterTodayInclusive(Boolean value)
  {
    if(value == null)
    {
      setValue(AFTERTODAYINCLUSIVE, "");
    }
    else
    {
      setValue(AFTERTODAYINCLUSIVE, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isAfterTodayInclusiveWritable()
  {
    return isWritable(AFTERTODAYINCLUSIVE);
  }
  
  public boolean isAfterTodayInclusiveReadable()
  {
    return isReadable(AFTERTODAYINCLUSIVE);
  }
  
  public boolean isAfterTodayInclusiveModified()
  {
    return isModified(AFTERTODAYINCLUSIVE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getAfterTodayInclusiveMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(AFTERTODAYINCLUSIVE).getAttributeMdDTO();
  }
  
  public Boolean getBeforeTodayExclusive()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(BEFORETODAYEXCLUSIVE));
  }
  
  public void setBeforeTodayExclusive(Boolean value)
  {
    if(value == null)
    {
      setValue(BEFORETODAYEXCLUSIVE, "");
    }
    else
    {
      setValue(BEFORETODAYEXCLUSIVE, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isBeforeTodayExclusiveWritable()
  {
    return isWritable(BEFORETODAYEXCLUSIVE);
  }
  
  public boolean isBeforeTodayExclusiveReadable()
  {
    return isReadable(BEFORETODAYEXCLUSIVE);
  }
  
  public boolean isBeforeTodayExclusiveModified()
  {
    return isModified(BEFORETODAYEXCLUSIVE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getBeforeTodayExclusiveMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(BEFORETODAYEXCLUSIVE).getAttributeMdDTO();
  }
  
  public Boolean getBeforeTodayInclusive()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(BEFORETODAYINCLUSIVE));
  }
  
  public void setBeforeTodayInclusive(Boolean value)
  {
    if(value == null)
    {
      setValue(BEFORETODAYINCLUSIVE, "");
    }
    else
    {
      setValue(BEFORETODAYINCLUSIVE, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isBeforeTodayInclusiveWritable()
  {
    return isWritable(BEFORETODAYINCLUSIVE);
  }
  
  public boolean isBeforeTodayInclusiveReadable()
  {
    return isReadable(BEFORETODAYINCLUSIVE);
  }
  
  public boolean isBeforeTodayInclusiveModified()
  {
    return isModified(BEFORETODAYINCLUSIVE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getBeforeTodayInclusiveMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(BEFORETODAYINCLUSIVE).getAttributeMdDTO();
  }
  
  public java.util.Date getEndDate()
  {
    return com.runwaysdk.constants.MdAttributeDateUtil.getTypeSafeValue(getValue(ENDDATE));
  }
  
  public void setEndDate(java.util.Date value)
  {
    if(value == null)
    {
      setValue(ENDDATE, "");
    }
    else
    {
      setValue(ENDDATE, new java.text.SimpleDateFormat(com.runwaysdk.constants.Constants.DATE_FORMAT).format(value));
    }
  }
  
  public boolean isEndDateWritable()
  {
    return isWritable(ENDDATE);
  }
  
  public boolean isEndDateReadable()
  {
    return isReadable(ENDDATE);
  }
  
  public boolean isEndDateModified()
  {
    return isModified(ENDDATE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeDateMdDTO getEndDateMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeDateMdDTO) getAttributeDTO(ENDDATE).getAttributeMdDTO();
  }
  
  public java.util.Date getStartDate()
  {
    return com.runwaysdk.constants.MdAttributeDateUtil.getTypeSafeValue(getValue(STARTDATE));
  }
  
  public void setStartDate(java.util.Date value)
  {
    if(value == null)
    {
      setValue(STARTDATE, "");
    }
    else
    {
      setValue(STARTDATE, new java.text.SimpleDateFormat(com.runwaysdk.constants.Constants.DATE_FORMAT).format(value));
    }
  }
  
  public boolean isStartDateWritable()
  {
    return isWritable(STARTDATE);
  }
  
  public boolean isStartDateReadable()
  {
    return isReadable(STARTDATE);
  }
  
  public boolean isStartDateModified()
  {
    return isModified(STARTDATE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeDateMdDTO getStartDateMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeDateMdDTO) getAttributeDTO(STARTDATE).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.metadata.MdWebDateDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.MdWebDateDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdWebDateQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdWebDateQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdWebDateDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdWebDateDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdWebDateDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdWebDateDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdWebDateDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdWebDateDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdWebDateDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
