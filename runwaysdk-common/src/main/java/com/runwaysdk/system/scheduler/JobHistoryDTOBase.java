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
package com.runwaysdk.system.scheduler;

@com.runwaysdk.business.ClassSignature(hash = -1079621008)
public abstract class JobHistoryDTOBase extends com.runwaysdk.business.BusinessDTO
{
  public final static String CLASS = "com.runwaysdk.system.scheduler.JobHistory";
  private static final long serialVersionUID = -1079621008;
  
  protected JobHistoryDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected JobHistoryDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String CREATEDATE = "createDate";
  public static java.lang.String CREATEDBY = "createdBy";
  public static java.lang.String ENDTIME = "endTime";
  public static java.lang.String ENTITYDOMAIN = "entityDomain";
  public static java.lang.String HISTORYCOMMENT = "historyComment";
  public static java.lang.String HISTORYINFORMATION = "historyInformation";
  public static java.lang.String OID = "oid";
  public static java.lang.String KEYNAME = "keyName";
  public static java.lang.String LASTUPDATEDATE = "lastUpdateDate";
  public static java.lang.String LASTUPDATEDBY = "lastUpdatedBy";
  public static java.lang.String LOCKEDBY = "lockedBy";
  public static java.lang.String OWNER = "owner";
  public static java.lang.String RETRIES = "retries";
  public static java.lang.String SEQ = "seq";
  public static java.lang.String SITEMASTER = "siteMaster";
  public static java.lang.String STARTTIME = "startTime";
  public static java.lang.String STATUS = "status";
  public static java.lang.String TYPE = "type";
  public static java.lang.String WORKPROGRESS = "workProgress";
  public java.util.Date getCreateDate()
  {
    return com.runwaysdk.constants.MdAttributeDateTimeUtil.getTypeSafeValue(getValue(CREATEDATE));
  }
  
  public boolean isCreateDateWritable()
  {
    return isWritable(CREATEDATE);
  }
  
  public boolean isCreateDateReadable()
  {
    return isReadable(CREATEDATE);
  }
  
  public boolean isCreateDateModified()
  {
    return isModified(CREATEDATE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO getCreateDateMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO) getAttributeDTO(CREATEDATE).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.SingleActorDTO getCreatedBy()
  {
    if(getValue(CREATEDBY) == null || getValue(CREATEDBY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.SingleActorDTO.get(getRequest(), getValue(CREATEDBY));
    }
  }
  
  public String getCreatedById()
  {
    return getValue(CREATEDBY);
  }
  
  public boolean isCreatedByWritable()
  {
    return isWritable(CREATEDBY);
  }
  
  public boolean isCreatedByReadable()
  {
    return isReadable(CREATEDBY);
  }
  
  public boolean isCreatedByModified()
  {
    return isModified(CREATEDBY);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getCreatedByMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(CREATEDBY).getAttributeMdDTO();
  }
  
  public java.util.Date getEndTime()
  {
    return com.runwaysdk.constants.MdAttributeDateTimeUtil.getTypeSafeValue(getValue(ENDTIME));
  }
  
  public void setEndTime(java.util.Date value)
  {
    if(value == null)
    {
      setValue(ENDTIME, "");
    }
    else
    {
      setValue(ENDTIME, new java.text.SimpleDateFormat(com.runwaysdk.constants.Constants.DATETIME_FORMAT).format(value));
    }
  }
  
  public boolean isEndTimeWritable()
  {
    return isWritable(ENDTIME);
  }
  
  public boolean isEndTimeReadable()
  {
    return isReadable(ENDTIME);
  }
  
  public boolean isEndTimeModified()
  {
    return isModified(ENDTIME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO getEndTimeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO) getAttributeDTO(ENDTIME).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MdDomainDTO getEntityDomain()
  {
    if(getValue(ENTITYDOMAIN) == null || getValue(ENTITYDOMAIN).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdDomainDTO.get(getRequest(), getValue(ENTITYDOMAIN));
    }
  }
  
  public String getEntityDomainId()
  {
    return getValue(ENTITYDOMAIN);
  }
  
  public void setEntityDomain(com.runwaysdk.system.metadata.MdDomainDTO value)
  {
    if(value == null)
    {
      setValue(ENTITYDOMAIN, "");
    }
    else
    {
      setValue(ENTITYDOMAIN, value.getOid());
    }
  }
  
  public boolean isEntityDomainWritable()
  {
    return isWritable(ENTITYDOMAIN);
  }
  
  public boolean isEntityDomainReadable()
  {
    return isReadable(ENTITYDOMAIN);
  }
  
  public boolean isEntityDomainModified()
  {
    return isModified(ENTITYDOMAIN);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getEntityDomainMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(ENTITYDOMAIN).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.scheduler.JobHistoryHistoryCommentDTO getHistoryComment()
  {
    return (com.runwaysdk.system.scheduler.JobHistoryHistoryCommentDTO) this.getAttributeStructDTO(HISTORYCOMMENT).getStructDTO();
  }
  
  public boolean isHistoryCommentWritable()
  {
    return isWritable(HISTORYCOMMENT);
  }
  
  public boolean isHistoryCommentReadable()
  {
    return isReadable(HISTORYCOMMENT);
  }
  
  public boolean isHistoryCommentModified()
  {
    return isModified(HISTORYCOMMENT);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeLocalTextMdDTO getHistoryCommentMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeLocalTextMdDTO) getAttributeDTO(HISTORYCOMMENT).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.scheduler.JobHistoryHistoryInformationDTO getHistoryInformation()
  {
    return (com.runwaysdk.system.scheduler.JobHistoryHistoryInformationDTO) this.getAttributeStructDTO(HISTORYINFORMATION).getStructDTO();
  }
  
  public boolean isHistoryInformationWritable()
  {
    return isWritable(HISTORYINFORMATION);
  }
  
  public boolean isHistoryInformationReadable()
  {
    return isReadable(HISTORYINFORMATION);
  }
  
  public boolean isHistoryInformationModified()
  {
    return isModified(HISTORYINFORMATION);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeLocalTextMdDTO getHistoryInformationMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeLocalTextMdDTO) getAttributeDTO(HISTORYINFORMATION).getAttributeMdDTO();
  }
  
  public String getKeyName()
  {
    return getValue(KEYNAME);
  }
  
  public void setKeyName(String value)
  {
    if(value == null)
    {
      setValue(KEYNAME, "");
    }
    else
    {
      setValue(KEYNAME, value);
    }
  }
  
  public boolean isKeyNameWritable()
  {
    return isWritable(KEYNAME);
  }
  
  public boolean isKeyNameReadable()
  {
    return isReadable(KEYNAME);
  }
  
  public boolean isKeyNameModified()
  {
    return isModified(KEYNAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getKeyNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(KEYNAME).getAttributeMdDTO();
  }
  
  public java.util.Date getLastUpdateDate()
  {
    return com.runwaysdk.constants.MdAttributeDateTimeUtil.getTypeSafeValue(getValue(LASTUPDATEDATE));
  }
  
  public boolean isLastUpdateDateWritable()
  {
    return isWritable(LASTUPDATEDATE);
  }
  
  public boolean isLastUpdateDateReadable()
  {
    return isReadable(LASTUPDATEDATE);
  }
  
  public boolean isLastUpdateDateModified()
  {
    return isModified(LASTUPDATEDATE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO getLastUpdateDateMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO) getAttributeDTO(LASTUPDATEDATE).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.SingleActorDTO getLastUpdatedBy()
  {
    if(getValue(LASTUPDATEDBY) == null || getValue(LASTUPDATEDBY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.SingleActorDTO.get(getRequest(), getValue(LASTUPDATEDBY));
    }
  }
  
  public String getLastUpdatedById()
  {
    return getValue(LASTUPDATEDBY);
  }
  
  public boolean isLastUpdatedByWritable()
  {
    return isWritable(LASTUPDATEDBY);
  }
  
  public boolean isLastUpdatedByReadable()
  {
    return isReadable(LASTUPDATEDBY);
  }
  
  public boolean isLastUpdatedByModified()
  {
    return isModified(LASTUPDATEDBY);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getLastUpdatedByMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(LASTUPDATEDBY).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.SingleActorDTO getLockedBy()
  {
    if(getValue(LOCKEDBY) == null || getValue(LOCKEDBY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.SingleActorDTO.get(getRequest(), getValue(LOCKEDBY));
    }
  }
  
  public String getLockedById()
  {
    return getValue(LOCKEDBY);
  }
  
  public boolean isLockedByWritable()
  {
    return isWritable(LOCKEDBY);
  }
  
  public boolean isLockedByReadable()
  {
    return isReadable(LOCKEDBY);
  }
  
  public boolean isLockedByModified()
  {
    return isModified(LOCKEDBY);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getLockedByMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(LOCKEDBY).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.ActorDTO getOwner()
  {
    if(getValue(OWNER) == null || getValue(OWNER).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.ActorDTO.get(getRequest(), getValue(OWNER));
    }
  }
  
  public String getOwnerId()
  {
    return getValue(OWNER);
  }
  
  public void setOwner(com.runwaysdk.system.ActorDTO value)
  {
    if(value == null)
    {
      setValue(OWNER, "");
    }
    else
    {
      setValue(OWNER, value.getOid());
    }
  }
  
  public boolean isOwnerWritable()
  {
    return isWritable(OWNER);
  }
  
  public boolean isOwnerReadable()
  {
    return isReadable(OWNER);
  }
  
  public boolean isOwnerModified()
  {
    return isModified(OWNER);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getOwnerMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(OWNER).getAttributeMdDTO();
  }
  
  public Integer getRetries()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(RETRIES));
  }
  
  public void setRetries(Integer value)
  {
    if(value == null)
    {
      setValue(RETRIES, "");
    }
    else
    {
      setValue(RETRIES, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isRetriesWritable()
  {
    return isWritable(RETRIES);
  }
  
  public boolean isRetriesReadable()
  {
    return isReadable(RETRIES);
  }
  
  public boolean isRetriesModified()
  {
    return isModified(RETRIES);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getRetriesMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(RETRIES).getAttributeMdDTO();
  }
  
  public Long getSeq()
  {
    return com.runwaysdk.constants.MdAttributeLongUtil.getTypeSafeValue(getValue(SEQ));
  }
  
  public boolean isSeqWritable()
  {
    return isWritable(SEQ);
  }
  
  public boolean isSeqReadable()
  {
    return isReadable(SEQ);
  }
  
  public boolean isSeqModified()
  {
    return isModified(SEQ);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getSeqMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(SEQ).getAttributeMdDTO();
  }
  
  public String getSiteMaster()
  {
    return getValue(SITEMASTER);
  }
  
  public boolean isSiteMasterWritable()
  {
    return isWritable(SITEMASTER);
  }
  
  public boolean isSiteMasterReadable()
  {
    return isReadable(SITEMASTER);
  }
  
  public boolean isSiteMasterModified()
  {
    return isModified(SITEMASTER);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getSiteMasterMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(SITEMASTER).getAttributeMdDTO();
  }
  
  public java.util.Date getStartTime()
  {
    return com.runwaysdk.constants.MdAttributeDateTimeUtil.getTypeSafeValue(getValue(STARTTIME));
  }
  
  public void setStartTime(java.util.Date value)
  {
    if(value == null)
    {
      setValue(STARTTIME, "");
    }
    else
    {
      setValue(STARTTIME, new java.text.SimpleDateFormat(com.runwaysdk.constants.Constants.DATETIME_FORMAT).format(value));
    }
  }
  
  public boolean isStartTimeWritable()
  {
    return isWritable(STARTTIME);
  }
  
  public boolean isStartTimeReadable()
  {
    return isReadable(STARTTIME);
  }
  
  public boolean isStartTimeModified()
  {
    return isModified(STARTTIME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO getStartTimeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO) getAttributeDTO(STARTTIME).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<com.runwaysdk.system.scheduler.AllJobStatusDTO> getStatus()
  {
    return (java.util.List<com.runwaysdk.system.scheduler.AllJobStatusDTO>) com.runwaysdk.transport.conversion.ConversionFacade.convertEnumDTOsFromEnumNames(getRequest(), com.runwaysdk.system.scheduler.AllJobStatusDTO.CLASS, getEnumNames(STATUS));
  }
  
  public java.util.List<String> getStatusEnumNames()
  {
    return getEnumNames(STATUS);
  }
  
  public void addStatus(com.runwaysdk.system.scheduler.AllJobStatusDTO enumDTO)
  {
    addEnumItem(STATUS, enumDTO.toString());
  }
  
  public void removeStatus(com.runwaysdk.system.scheduler.AllJobStatusDTO enumDTO)
  {
    removeEnumItem(STATUS, enumDTO.toString());
  }
  
  public void clearStatus()
  {
    clearEnum(STATUS);
  }
  
  public boolean isStatusWritable()
  {
    return isWritable(STATUS);
  }
  
  public boolean isStatusReadable()
  {
    return isReadable(STATUS);
  }
  
  public boolean isStatusModified()
  {
    return isModified(STATUS);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO getStatusMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO) getAttributeDTO(STATUS).getAttributeMdDTO();
  }
  
  public Integer getWorkProgress()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(WORKPROGRESS));
  }
  
  public void setWorkProgress(Integer value)
  {
    if(value == null)
    {
      setValue(WORKPROGRESS, "");
    }
    else
    {
      setValue(WORKPROGRESS, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isWorkProgressWritable()
  {
    return isWritable(WORKPROGRESS);
  }
  
  public boolean isWorkProgressReadable()
  {
    return isReadable(WORKPROGRESS);
  }
  
  public boolean isWorkProgressModified()
  {
    return isModified(WORKPROGRESS);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getWorkProgressMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(WORKPROGRESS).getAttributeMdDTO();
  }
  
  public static final void clearHistory(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    String[] _declaredTypes = new String[]{};
    Object[] _parameters = new Object[]{};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.scheduler.JobHistoryDTO.CLASS, "clearHistory", _declaredTypes);
    clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.scheduler.ExecutableJobDTO> getAllJob()
  {
    return (java.util.List<? extends com.runwaysdk.system.scheduler.ExecutableJobDTO>) getRequest().getParents(this.getOid(), com.runwaysdk.system.scheduler.JobHistoryRecordDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.scheduler.ExecutableJobDTO> getAllJob(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.scheduler.ExecutableJobDTO>) clientRequestIF.getParents(oid, com.runwaysdk.system.scheduler.JobHistoryRecordDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.scheduler.JobHistoryRecordDTO> getAllJobRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.scheduler.JobHistoryRecordDTO>) getRequest().getParentRelationships(this.getOid(), com.runwaysdk.system.scheduler.JobHistoryRecordDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.scheduler.JobHistoryRecordDTO> getAllJobRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.scheduler.JobHistoryRecordDTO>) clientRequestIF.getParentRelationships(oid, com.runwaysdk.system.scheduler.JobHistoryRecordDTO.CLASS);
  }
  
  public com.runwaysdk.system.scheduler.JobHistoryRecordDTO addJob(com.runwaysdk.system.scheduler.ExecutableJobDTO parent)
  {
    return (com.runwaysdk.system.scheduler.JobHistoryRecordDTO) getRequest().addParent(parent.getOid(), this.getOid(), com.runwaysdk.system.scheduler.JobHistoryRecordDTO.CLASS);
  }
  
  public static com.runwaysdk.system.scheduler.JobHistoryRecordDTO addJob(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.scheduler.ExecutableJobDTO parent)
  {
    return (com.runwaysdk.system.scheduler.JobHistoryRecordDTO) clientRequestIF.addParent(parent.getOid(), oid, com.runwaysdk.system.scheduler.JobHistoryRecordDTO.CLASS);
  }
  
  public void removeJob(com.runwaysdk.system.scheduler.JobHistoryRecordDTO relationship)
  {
    getRequest().deleteParent(relationship.getOid());
  }
  
  public static void removeJob(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.scheduler.JobHistoryRecordDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getOid());
  }
  
  public void removeAllJob()
  {
    getRequest().deleteParents(this.getOid(), com.runwaysdk.system.scheduler.JobHistoryRecordDTO.CLASS);
  }
  
  public static void removeAllJob(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteParents(oid, com.runwaysdk.system.scheduler.JobHistoryRecordDTO.CLASS);
  }
  
  public static com.runwaysdk.system.scheduler.JobHistoryDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.scheduler.JobHistoryDTO) dto;
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
  
  public static com.runwaysdk.system.scheduler.JobHistoryQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.scheduler.JobHistoryQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.scheduler.JobHistoryDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.scheduler.JobHistoryDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.scheduler.JobHistoryDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.scheduler.JobHistoryDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.scheduler.JobHistoryDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.scheduler.JobHistoryDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.scheduler.JobHistoryDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
