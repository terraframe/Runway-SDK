package com.runwaysdk.system.scheduler;

@com.runwaysdk.business.ClassSignature(hash = -824728701)
public abstract class AbstractJobDTOBase extends com.runwaysdk.business.BusinessDTO
{
  public final static String CLASS = "com.runwaysdk.system.scheduler.AbstractJob";
  private static final long serialVersionUID = -824728701;
  
  protected AbstractJobDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected AbstractJobDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String CANCELABLE = "cancelable";
  public static java.lang.String CANCELED = "canceled";
  public static java.lang.String COMPLETED = "completed";
  public static java.lang.String CREATEDATE = "createDate";
  public static java.lang.String CREATEDBY = "createdBy";
  public static java.lang.String CRONEXPRESSION = "cronExpression";
  public static java.lang.String ENDTIME = "endTime";
  public static java.lang.String ENTITYDOMAIN = "entityDomain";
  public static java.lang.String ID = "id";
  public static java.lang.String JOBOPERATION = "jobOperation";
  public static java.lang.String KEYNAME = "keyName";
  public static java.lang.String LASTRUN = "lastRun";
  public static java.lang.String LASTUPDATEDATE = "lastUpdateDate";
  public static java.lang.String LASTUPDATEDBY = "lastUpdatedBy";
  public static java.lang.String LOCKEDBY = "lockedBy";
  public static java.lang.String MAXRETRIES = "maxRetries";
  public static java.lang.String OWNER = "owner";
  public static java.lang.String PAUSEABLE = "pauseable";
  public static java.lang.String PAUSED = "paused";
  public static java.lang.String REMOVEONCOMPLETE = "removeOnComplete";
  public static java.lang.String REPEATED = "repeated";
  public static java.lang.String RETRIES = "retries";
  public static java.lang.String RUNNING = "running";
  public static java.lang.String SEQ = "seq";
  public static java.lang.String SITEMASTER = "siteMaster";
  public static java.lang.String STARTONCREATE = "startOnCreate";
  public static java.lang.String STARTTIME = "startTime";
  public static java.lang.String TIMEOUT = "timeout";
  public static java.lang.String TYPE = "type";
  public static java.lang.String WORKPROGRESS = "workProgress";
  public static java.lang.String WORKTOTAL = "workTotal";
  public Boolean getCancelable()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(CANCELABLE));
  }
  
  public void setCancelable(Boolean value)
  {
    if(value == null)
    {
      setValue(CANCELABLE, "");
    }
    else
    {
      setValue(CANCELABLE, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isCancelableWritable()
  {
    return isWritable(CANCELABLE);
  }
  
  public boolean isCancelableReadable()
  {
    return isReadable(CANCELABLE);
  }
  
  public boolean isCancelableModified()
  {
    return isModified(CANCELABLE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getCancelableMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(CANCELABLE).getAttributeMdDTO();
  }
  
  public Boolean getCanceled()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(CANCELED));
  }
  
  public void setCanceled(Boolean value)
  {
    if(value == null)
    {
      setValue(CANCELED, "");
    }
    else
    {
      setValue(CANCELED, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isCanceledWritable()
  {
    return isWritable(CANCELED);
  }
  
  public boolean isCanceledReadable()
  {
    return isReadable(CANCELED);
  }
  
  public boolean isCanceledModified()
  {
    return isModified(CANCELED);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getCanceledMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(CANCELED).getAttributeMdDTO();
  }
  
  public Boolean getCompleted()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(COMPLETED));
  }
  
  public void setCompleted(Boolean value)
  {
    if(value == null)
    {
      setValue(COMPLETED, "");
    }
    else
    {
      setValue(COMPLETED, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isCompletedWritable()
  {
    return isWritable(COMPLETED);
  }
  
  public boolean isCompletedReadable()
  {
    return isReadable(COMPLETED);
  }
  
  public boolean isCompletedModified()
  {
    return isModified(COMPLETED);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getCompletedMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(COMPLETED).getAttributeMdDTO();
  }
  
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
  
  public String getCronExpression()
  {
    return getValue(CRONEXPRESSION);
  }
  
  public void setCronExpression(String value)
  {
    if(value == null)
    {
      setValue(CRONEXPRESSION, "");
    }
    else
    {
      setValue(CRONEXPRESSION, value);
    }
  }
  
  public boolean isCronExpressionWritable()
  {
    return isWritable(CRONEXPRESSION);
  }
  
  public boolean isCronExpressionReadable()
  {
    return isReadable(CRONEXPRESSION);
  }
  
  public boolean isCronExpressionModified()
  {
    return isModified(CRONEXPRESSION);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getCronExpressionMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(CRONEXPRESSION).getAttributeMdDTO();
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
  
  @SuppressWarnings("unchecked")
  public java.util.List<com.runwaysdk.system.scheduler.AllJobOperationDTO> getJobOperation()
  {
    return (java.util.List<com.runwaysdk.system.scheduler.AllJobOperationDTO>) com.runwaysdk.transport.conversion.ConversionFacade.convertEnumDTOsFromEnumNames(getRequest(), com.runwaysdk.system.scheduler.AllJobOperationDTO.CLASS, getEnumNames(JOBOPERATION));
  }
  
  public java.util.List<String> getJobOperationEnumNames()
  {
    return getEnumNames(JOBOPERATION);
  }
  
  public void addJobOperation(com.runwaysdk.system.scheduler.AllJobOperationDTO enumDTO)
  {
    addEnumItem(JOBOPERATION, enumDTO.toString());
  }
  
  public void removeJobOperation(com.runwaysdk.system.scheduler.AllJobOperationDTO enumDTO)
  {
    removeEnumItem(JOBOPERATION, enumDTO.toString());
  }
  
  public void clearJobOperation()
  {
    clearEnum(JOBOPERATION);
  }
  
  public boolean isJobOperationWritable()
  {
    return isWritable(JOBOPERATION);
  }
  
  public boolean isJobOperationReadable()
  {
    return isReadable(JOBOPERATION);
  }
  
  public boolean isJobOperationModified()
  {
    return isModified(JOBOPERATION);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO getJobOperationMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO) getAttributeDTO(JOBOPERATION).getAttributeMdDTO();
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
  
  public java.util.Date getLastRun()
  {
    return com.runwaysdk.constants.MdAttributeDateTimeUtil.getTypeSafeValue(getValue(LASTRUN));
  }
  
  public void setLastRun(java.util.Date value)
  {
    if(value == null)
    {
      setValue(LASTRUN, "");
    }
    else
    {
      setValue(LASTRUN, new java.text.SimpleDateFormat(com.runwaysdk.constants.Constants.DATETIME_FORMAT).format(value));
    }
  }
  
  public boolean isLastRunWritable()
  {
    return isWritable(LASTRUN);
  }
  
  public boolean isLastRunReadable()
  {
    return isReadable(LASTRUN);
  }
  
  public boolean isLastRunModified()
  {
    return isModified(LASTRUN);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO getLastRunMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO) getAttributeDTO(LASTRUN).getAttributeMdDTO();
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
  
  public Integer getMaxRetries()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(MAXRETRIES));
  }
  
  public void setMaxRetries(Integer value)
  {
    if(value == null)
    {
      setValue(MAXRETRIES, "");
    }
    else
    {
      setValue(MAXRETRIES, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isMaxRetriesWritable()
  {
    return isWritable(MAXRETRIES);
  }
  
  public boolean isMaxRetriesReadable()
  {
    return isReadable(MAXRETRIES);
  }
  
  public boolean isMaxRetriesModified()
  {
    return isModified(MAXRETRIES);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getMaxRetriesMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(MAXRETRIES).getAttributeMdDTO();
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
  
  public Boolean getPauseable()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(PAUSEABLE));
  }
  
  public void setPauseable(Boolean value)
  {
    if(value == null)
    {
      setValue(PAUSEABLE, "");
    }
    else
    {
      setValue(PAUSEABLE, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isPauseableWritable()
  {
    return isWritable(PAUSEABLE);
  }
  
  public boolean isPauseableReadable()
  {
    return isReadable(PAUSEABLE);
  }
  
  public boolean isPauseableModified()
  {
    return isModified(PAUSEABLE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getPauseableMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(PAUSEABLE).getAttributeMdDTO();
  }
  
  public Boolean getPaused()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(PAUSED));
  }
  
  public void setPaused(Boolean value)
  {
    if(value == null)
    {
      setValue(PAUSED, "");
    }
    else
    {
      setValue(PAUSED, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isPausedWritable()
  {
    return isWritable(PAUSED);
  }
  
  public boolean isPausedReadable()
  {
    return isReadable(PAUSED);
  }
  
  public boolean isPausedModified()
  {
    return isModified(PAUSED);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getPausedMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(PAUSED).getAttributeMdDTO();
  }
  
  public Boolean getRemoveOnComplete()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(REMOVEONCOMPLETE));
  }
  
  public void setRemoveOnComplete(Boolean value)
  {
    if(value == null)
    {
      setValue(REMOVEONCOMPLETE, "");
    }
    else
    {
      setValue(REMOVEONCOMPLETE, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isRemoveOnCompleteWritable()
  {
    return isWritable(REMOVEONCOMPLETE);
  }
  
  public boolean isRemoveOnCompleteReadable()
  {
    return isReadable(REMOVEONCOMPLETE);
  }
  
  public boolean isRemoveOnCompleteModified()
  {
    return isModified(REMOVEONCOMPLETE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getRemoveOnCompleteMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(REMOVEONCOMPLETE).getAttributeMdDTO();
  }
  
  public Boolean getRepeated()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(REPEATED));
  }
  
  public void setRepeated(Boolean value)
  {
    if(value == null)
    {
      setValue(REPEATED, "");
    }
    else
    {
      setValue(REPEATED, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isRepeatedWritable()
  {
    return isWritable(REPEATED);
  }
  
  public boolean isRepeatedReadable()
  {
    return isReadable(REPEATED);
  }
  
  public boolean isRepeatedModified()
  {
    return isModified(REPEATED);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getRepeatedMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(REPEATED).getAttributeMdDTO();
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
  
  public Boolean getRunning()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(RUNNING));
  }
  
  public void setRunning(Boolean value)
  {
    if(value == null)
    {
      setValue(RUNNING, "");
    }
    else
    {
      setValue(RUNNING, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isRunningWritable()
  {
    return isWritable(RUNNING);
  }
  
  public boolean isRunningReadable()
  {
    return isReadable(RUNNING);
  }
  
  public boolean isRunningModified()
  {
    return isModified(RUNNING);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getRunningMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(RUNNING).getAttributeMdDTO();
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
  
  public Boolean getStartOnCreate()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(STARTONCREATE));
  }
  
  public void setStartOnCreate(Boolean value)
  {
    if(value == null)
    {
      setValue(STARTONCREATE, "");
    }
    else
    {
      setValue(STARTONCREATE, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isStartOnCreateWritable()
  {
    return isWritable(STARTONCREATE);
  }
  
  public boolean isStartOnCreateReadable()
  {
    return isReadable(STARTONCREATE);
  }
  
  public boolean isStartOnCreateModified()
  {
    return isModified(STARTONCREATE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getStartOnCreateMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(STARTONCREATE).getAttributeMdDTO();
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
  
  public Long getTimeout()
  {
    return com.runwaysdk.constants.MdAttributeLongUtil.getTypeSafeValue(getValue(TIMEOUT));
  }
  
  public void setTimeout(Long value)
  {
    if(value == null)
    {
      setValue(TIMEOUT, "");
    }
    else
    {
      setValue(TIMEOUT, java.lang.Long.toString(value));
    }
  }
  
  public boolean isTimeoutWritable()
  {
    return isWritable(TIMEOUT);
  }
  
  public boolean isTimeoutReadable()
  {
    return isReadable(TIMEOUT);
  }
  
  public boolean isTimeoutModified()
  {
    return isModified(TIMEOUT);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getTimeoutMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(TIMEOUT).getAttributeMdDTO();
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
  
  public Integer getWorkTotal()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(WORKTOTAL));
  }
  
  public void setWorkTotal(Integer value)
  {
    if(value == null)
    {
      setValue(WORKTOTAL, "");
    }
    else
    {
      setValue(WORKTOTAL, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isWorkTotalWritable()
  {
    return isWritable(WORKTOTAL);
  }
  
  public boolean isWorkTotalReadable()
  {
    return isReadable(WORKTOTAL);
  }
  
  public boolean isWorkTotalModified()
  {
    return isModified(WORKTOTAL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getWorkTotalMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(WORKTOTAL).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.scheduler.AbstractJobDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.scheduler.AbstractJobDTO) dto;
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
  
  public static com.runwaysdk.system.scheduler.AbstractJobQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.scheduler.AbstractJobQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.scheduler.AbstractJobDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.scheduler.AbstractJobDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.scheduler.AbstractJobDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.scheduler.AbstractJobDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.scheduler.AbstractJobDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.scheduler.AbstractJobDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.scheduler.AbstractJobDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
