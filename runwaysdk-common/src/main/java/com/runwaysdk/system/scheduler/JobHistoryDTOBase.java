package com.runwaysdk.system.scheduler;

@com.runwaysdk.business.ClassSignature(hash = -1573713313)
public abstract class JobHistoryDTOBase extends com.runwaysdk.business.BusinessDTO
{
  public final static String CLASS = "com.runwaysdk.system.scheduler.JobHistory";
  private static final long serialVersionUID = -1573713313;
  
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
  public static java.lang.String ENTITYDOMAIN = "entityDomain";
  public static java.lang.String HISTORYCOMMENT = "historyComment";
  public static java.lang.String HISTORYINFORMATION = "historyInformation";
  public static java.lang.String ID = "id";
  public static java.lang.String JOBSNAPSHOT = "jobSnapshot";
  public static java.lang.String KEYNAME = "keyName";
  public static java.lang.String LASTUPDATEDATE = "lastUpdateDate";
  public static java.lang.String LASTUPDATEDBY = "lastUpdatedBy";
  public static java.lang.String LOCKEDBY = "lockedBy";
  public static java.lang.String OWNER = "owner";
  public static java.lang.String SEQ = "seq";
  public static java.lang.String SITEMASTER = "siteMaster";
  public static java.lang.String TYPE = "type";
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
      setValue(ENTITYDOMAIN, value.getId());
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
  
  public com.runwaysdk.system.scheduler.ExecutableJobDTO getJobSnapshot()
  {
    if(getValue(JOBSNAPSHOT) == null || getValue(JOBSNAPSHOT).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.scheduler.ExecutableJobDTO.get(getRequest(), getValue(JOBSNAPSHOT));
    }
  }
  
  public String getJobSnapshotId()
  {
    return getValue(JOBSNAPSHOT);
  }
  
  public void setJobSnapshot(com.runwaysdk.system.scheduler.ExecutableJobDTO value)
  {
    if(value == null)
    {
      setValue(JOBSNAPSHOT, "");
    }
    else
    {
      setValue(JOBSNAPSHOT, value.getId());
    }
  }
  
  public boolean isJobSnapshotWritable()
  {
    return isWritable(JOBSNAPSHOT);
  }
  
  public boolean isJobSnapshotReadable()
  {
    return isReadable(JOBSNAPSHOT);
  }
  
  public boolean isJobSnapshotModified()
  {
    return isModified(JOBSNAPSHOT);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getJobSnapshotMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(JOBSNAPSHOT).getAttributeMdDTO();
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
  
  public com.runwaysdk.system.UsersDTO getLockedBy()
  {
    if(getValue(LOCKEDBY) == null || getValue(LOCKEDBY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.UsersDTO.get(getRequest(), getValue(LOCKEDBY));
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
      setValue(OWNER, value.getId());
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
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.scheduler.ExecutableJobDTO> getAllJob()
  {
    return (java.util.List<? extends com.runwaysdk.system.scheduler.ExecutableJobDTO>) getRequest().getParents(this.getId(), com.runwaysdk.system.scheduler.JobHistoryRecordDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.scheduler.ExecutableJobDTO> getAllJob(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.scheduler.ExecutableJobDTO>) clientRequestIF.getParents(id, com.runwaysdk.system.scheduler.JobHistoryRecordDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.scheduler.JobHistoryRecordDTO> getAllJobRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.scheduler.JobHistoryRecordDTO>) getRequest().getParentRelationships(this.getId(), com.runwaysdk.system.scheduler.JobHistoryRecordDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.scheduler.JobHistoryRecordDTO> getAllJobRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.scheduler.JobHistoryRecordDTO>) clientRequestIF.getParentRelationships(id, com.runwaysdk.system.scheduler.JobHistoryRecordDTO.CLASS);
  }
  
  public com.runwaysdk.system.scheduler.JobHistoryRecordDTO addJob(com.runwaysdk.system.scheduler.ExecutableJobDTO parent)
  {
    return (com.runwaysdk.system.scheduler.JobHistoryRecordDTO) getRequest().addParent(parent.getId(), this.getId(), com.runwaysdk.system.scheduler.JobHistoryRecordDTO.CLASS);
  }
  
  public static com.runwaysdk.system.scheduler.JobHistoryRecordDTO addJob(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.scheduler.ExecutableJobDTO parent)
  {
    return (com.runwaysdk.system.scheduler.JobHistoryRecordDTO) clientRequestIF.addParent(parent.getId(), id, com.runwaysdk.system.scheduler.JobHistoryRecordDTO.CLASS);
  }
  
  public void removeJob(com.runwaysdk.system.scheduler.JobHistoryRecordDTO relationship)
  {
    getRequest().deleteParent(relationship.getId());
  }
  
  public static void removeJob(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.scheduler.JobHistoryRecordDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getId());
  }
  
  public void removeAllJob()
  {
    getRequest().deleteParents(this.getId(), com.runwaysdk.system.scheduler.JobHistoryRecordDTO.CLASS);
  }
  
  public static void removeAllJob(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteParents(id, com.runwaysdk.system.scheduler.JobHistoryRecordDTO.CLASS);
  }
  
  public static com.runwaysdk.system.scheduler.JobHistoryDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
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
    getRequest().delete(this.getId());
  }
  
  public static com.runwaysdk.system.scheduler.JobHistoryQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.scheduler.JobHistoryQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.scheduler.JobHistoryDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.scheduler.JobHistoryDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.scheduler.JobHistoryDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.scheduler.JobHistoryDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.scheduler.JobHistoryDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.scheduler.JobHistoryDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.scheduler.JobHistoryDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
