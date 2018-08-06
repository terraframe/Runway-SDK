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

@com.runwaysdk.business.ClassSignature(hash = 1106431070)
public abstract class ExecutableJobDTOBase extends com.runwaysdk.system.scheduler.AbstractJobDTO
{
  public final static String CLASS = "com.runwaysdk.system.scheduler.ExecutableJob";
  private static final long serialVersionUID = 1106431070;
  
  protected ExecutableJobDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected ExecutableJobDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String DESCRIPTION = "description";
  public static java.lang.String ENTRYDATE = "entryDate";
  public static java.lang.String JOBID = "jobId";
  public static java.lang.String RECORDHISTORY = "recordHistory";
  public static java.lang.String RUNASDIMENSION = "runAsDimension";
  public static java.lang.String RUNASUSER = "runAsUser";
  public com.runwaysdk.system.scheduler.ExecutableJobDescriptionDTO getDescription()
  {
    return (com.runwaysdk.system.scheduler.ExecutableJobDescriptionDTO) this.getAttributeStructDTO(DESCRIPTION).getStructDTO();
  }
  
  public boolean isDescriptionWritable()
  {
    return isWritable(DESCRIPTION);
  }
  
  public boolean isDescriptionReadable()
  {
    return isReadable(DESCRIPTION);
  }
  
  public boolean isDescriptionModified()
  {
    return isModified(DESCRIPTION);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeLocalCharacterMdDTO getDescriptionMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeLocalCharacterMdDTO) getAttributeDTO(DESCRIPTION).getAttributeMdDTO();
  }
  
  public java.util.Date getEntryDate()
  {
    return com.runwaysdk.constants.MdAttributeDateTimeUtil.getTypeSafeValue(getValue(ENTRYDATE));
  }
  
  public void setEntryDate(java.util.Date value)
  {
    if(value == null)
    {
      setValue(ENTRYDATE, "");
    }
    else
    {
      setValue(ENTRYDATE, new java.text.SimpleDateFormat(com.runwaysdk.constants.Constants.DATETIME_FORMAT).format(value));
    }
  }
  
  public boolean isEntryDateWritable()
  {
    return isWritable(ENTRYDATE);
  }
  
  public boolean isEntryDateReadable()
  {
    return isReadable(ENTRYDATE);
  }
  
  public boolean isEntryDateModified()
  {
    return isModified(ENTRYDATE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO getEntryDateMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO) getAttributeDTO(ENTRYDATE).getAttributeMdDTO();
  }
  
  public String getJobId()
  {
    return getValue(JOBID);
  }
  
  public void setJobId(String value)
  {
    if(value == null)
    {
      setValue(JOBID, "");
    }
    else
    {
      setValue(JOBID, value);
    }
  }
  
  public boolean isJobIdWritable()
  {
    return isWritable(JOBID);
  }
  
  public boolean isJobIdReadable()
  {
    return isReadable(JOBID);
  }
  
  public boolean isJobIdModified()
  {
    return isModified(JOBID);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getJobIdMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(JOBID).getAttributeMdDTO();
  }
  
  public Boolean getRecordHistory()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(RECORDHISTORY));
  }
  
  public void setRecordHistory(Boolean value)
  {
    if(value == null)
    {
      setValue(RECORDHISTORY, "");
    }
    else
    {
      setValue(RECORDHISTORY, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isRecordHistoryWritable()
  {
    return isWritable(RECORDHISTORY);
  }
  
  public boolean isRecordHistoryReadable()
  {
    return isReadable(RECORDHISTORY);
  }
  
  public boolean isRecordHistoryModified()
  {
    return isModified(RECORDHISTORY);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getRecordHistoryMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(RECORDHISTORY).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MdDimensionDTO getRunAsDimension()
  {
    if(getValue(RUNASDIMENSION) == null || getValue(RUNASDIMENSION).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdDimensionDTO.get(getRequest(), getValue(RUNASDIMENSION));
    }
  }
  
  public String getRunAsDimensionId()
  {
    return getValue(RUNASDIMENSION);
  }
  
  public void setRunAsDimension(com.runwaysdk.system.metadata.MdDimensionDTO value)
  {
    if(value == null)
    {
      setValue(RUNASDIMENSION, "");
    }
    else
    {
      setValue(RUNASDIMENSION, value.getOid());
    }
  }
  
  public boolean isRunAsDimensionWritable()
  {
    return isWritable(RUNASDIMENSION);
  }
  
  public boolean isRunAsDimensionReadable()
  {
    return isReadable(RUNASDIMENSION);
  }
  
  public boolean isRunAsDimensionModified()
  {
    return isModified(RUNASDIMENSION);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getRunAsDimensionMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(RUNASDIMENSION).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.SingleActorDTO getRunAsUser()
  {
    if(getValue(RUNASUSER) == null || getValue(RUNASUSER).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.SingleActorDTO.get(getRequest(), getValue(RUNASUSER));
    }
  }
  
  public String getRunAsUserId()
  {
    return getValue(RUNASUSER);
  }
  
  public void setRunAsUser(com.runwaysdk.system.SingleActorDTO value)
  {
    if(value == null)
    {
      setValue(RUNASUSER, "");
    }
    else
    {
      setValue(RUNASUSER, value.getOid());
    }
  }
  
  public boolean isRunAsUserWritable()
  {
    return isWritable(RUNASUSER);
  }
  
  public boolean isRunAsUserReadable()
  {
    return isReadable(RUNASUSER);
  }
  
  public boolean isRunAsUserModified()
  {
    return isModified(RUNASUSER);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getRunAsUserMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(RUNASUSER).getAttributeMdDTO();
  }
  
  public final void cancel()
  {
    String[] _declaredTypes = new String[]{};
    Object[] _parameters = new Object[]{};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.scheduler.ExecutableJobDTO.CLASS, "cancel", _declaredTypes);
    getRequest().invokeMethod(_metadata, this, _parameters);
  }
  
  public static final void cancel(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.scheduler.ExecutableJobDTO.CLASS, "cancel", _declaredTypes);
    clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public final void pause()
  {
    String[] _declaredTypes = new String[]{};
    Object[] _parameters = new Object[]{};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.scheduler.ExecutableJobDTO.CLASS, "pause", _declaredTypes);
    getRequest().invokeMethod(_metadata, this, _parameters);
  }
  
  public static final void pause(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.scheduler.ExecutableJobDTO.CLASS, "pause", _declaredTypes);
    clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public final void resume()
  {
    String[] _declaredTypes = new String[]{};
    Object[] _parameters = new Object[]{};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.scheduler.ExecutableJobDTO.CLASS, "resume", _declaredTypes);
    getRequest().invokeMethod(_metadata, this, _parameters);
  }
  
  public static final void resume(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.scheduler.ExecutableJobDTO.CLASS, "resume", _declaredTypes);
    clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public final com.runwaysdk.system.scheduler.JobHistoryDTO start()
  {
    String[] _declaredTypes = new String[]{};
    Object[] _parameters = new Object[]{};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.scheduler.ExecutableJobDTO.CLASS, "start", _declaredTypes);
    return (com.runwaysdk.system.scheduler.JobHistoryDTO) getRequest().invokeMethod(_metadata, this, _parameters);
  }
  
  public static final com.runwaysdk.system.scheduler.JobHistoryDTO start(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.scheduler.ExecutableJobDTO.CLASS, "start", _declaredTypes);
    return (com.runwaysdk.system.scheduler.JobHistoryDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public final void stop()
  {
    String[] _declaredTypes = new String[]{};
    Object[] _parameters = new Object[]{};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.scheduler.ExecutableJobDTO.CLASS, "stop", _declaredTypes);
    getRequest().invokeMethod(_metadata, this, _parameters);
  }
  
  public static final void stop(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.scheduler.ExecutableJobDTO.CLASS, "stop", _declaredTypes);
    clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.scheduler.JobHistoryDTO> getAllJobHistory()
  {
    return (java.util.List<? extends com.runwaysdk.system.scheduler.JobHistoryDTO>) getRequest().getChildren(this.getOid(), com.runwaysdk.system.scheduler.JobHistoryRecordDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.scheduler.JobHistoryDTO> getAllJobHistory(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.scheduler.JobHistoryDTO>) clientRequestIF.getChildren(oid, com.runwaysdk.system.scheduler.JobHistoryRecordDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.scheduler.JobHistoryRecordDTO> getAllJobHistoryRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.scheduler.JobHistoryRecordDTO>) getRequest().getChildRelationships(this.getOid(), com.runwaysdk.system.scheduler.JobHistoryRecordDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.scheduler.JobHistoryRecordDTO> getAllJobHistoryRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.scheduler.JobHistoryRecordDTO>) clientRequestIF.getChildRelationships(oid, com.runwaysdk.system.scheduler.JobHistoryRecordDTO.CLASS);
  }
  
  public com.runwaysdk.system.scheduler.JobHistoryRecordDTO addJobHistory(com.runwaysdk.system.scheduler.JobHistoryDTO child)
  {
    return (com.runwaysdk.system.scheduler.JobHistoryRecordDTO) getRequest().addChild(this.getOid(), child.getOid(), com.runwaysdk.system.scheduler.JobHistoryRecordDTO.CLASS);
  }
  
  public static com.runwaysdk.system.scheduler.JobHistoryRecordDTO addJobHistory(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.scheduler.JobHistoryDTO child)
  {
    return (com.runwaysdk.system.scheduler.JobHistoryRecordDTO) clientRequestIF.addChild(oid, child.getOid(), com.runwaysdk.system.scheduler.JobHistoryRecordDTO.CLASS);
  }
  
  public void removeJobHistory(com.runwaysdk.system.scheduler.JobHistoryRecordDTO relationship)
  {
    getRequest().deleteChild(relationship.getOid());
  }
  
  public static void removeJobHistory(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.scheduler.JobHistoryRecordDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getOid());
  }
  
  public void removeAllJobHistory()
  {
    getRequest().deleteChildren(this.getOid(), com.runwaysdk.system.scheduler.JobHistoryRecordDTO.CLASS);
  }
  
  public static void removeAllJobHistory(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteChildren(oid, com.runwaysdk.system.scheduler.JobHistoryRecordDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.scheduler.ExecutableJobDTO> getAllDownstreamJob()
  {
    return (java.util.List<? extends com.runwaysdk.system.scheduler.ExecutableJobDTO>) getRequest().getChildren(this.getOid(), com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.scheduler.ExecutableJobDTO> getAllDownstreamJob(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.scheduler.ExecutableJobDTO>) clientRequestIF.getChildren(oid, com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO> getAllDownstreamJobRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO>) getRequest().getChildRelationships(this.getOid(), com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO> getAllDownstreamJobRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO>) clientRequestIF.getChildRelationships(oid, com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO.CLASS);
  }
  
  public com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO addDownstreamJob(com.runwaysdk.system.scheduler.ExecutableJobDTO child)
  {
    return (com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO) getRequest().addChild(this.getOid(), child.getOid(), com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO.CLASS);
  }
  
  public static com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO addDownstreamJob(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.scheduler.ExecutableJobDTO child)
  {
    return (com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO) clientRequestIF.addChild(oid, child.getOid(), com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO.CLASS);
  }
  
  public void removeDownstreamJob(com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO relationship)
  {
    getRequest().deleteChild(relationship.getOid());
  }
  
  public static void removeDownstreamJob(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getOid());
  }
  
  public void removeAllDownstreamJob()
  {
    getRequest().deleteChildren(this.getOid(), com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO.CLASS);
  }
  
  public static void removeAllDownstreamJob(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteChildren(oid, com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.scheduler.ExecutableJobDTO> getAllUpstreamJob()
  {
    return (java.util.List<? extends com.runwaysdk.system.scheduler.ExecutableJobDTO>) getRequest().getParents(this.getOid(), com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.scheduler.ExecutableJobDTO> getAllUpstreamJob(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.scheduler.ExecutableJobDTO>) clientRequestIF.getParents(oid, com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO> getAllUpstreamJobRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO>) getRequest().getParentRelationships(this.getOid(), com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO> getAllUpstreamJobRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO>) clientRequestIF.getParentRelationships(oid, com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO.CLASS);
  }
  
  public com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO addUpstreamJob(com.runwaysdk.system.scheduler.ExecutableJobDTO parent)
  {
    return (com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO) getRequest().addParent(parent.getOid(), this.getOid(), com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO.CLASS);
  }
  
  public static com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO addUpstreamJob(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.scheduler.ExecutableJobDTO parent)
  {
    return (com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO) clientRequestIF.addParent(parent.getOid(), oid, com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO.CLASS);
  }
  
  public void removeUpstreamJob(com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO relationship)
  {
    getRequest().deleteParent(relationship.getOid());
  }
  
  public static void removeUpstreamJob(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getOid());
  }
  
  public void removeAllUpstreamJob()
  {
    getRequest().deleteParents(this.getOid(), com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO.CLASS);
  }
  
  public static void removeAllUpstreamJob(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteParents(oid, com.runwaysdk.system.scheduler.DownstreamJobRelationshipDTO.CLASS);
  }
  
  public static com.runwaysdk.system.scheduler.ExecutableJobDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.scheduler.ExecutableJobDTO) dto;
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
  
  public static com.runwaysdk.system.scheduler.ExecutableJobQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.scheduler.ExecutableJobQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.scheduler.ExecutableJobDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.scheduler.ExecutableJobDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.scheduler.ExecutableJobDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.scheduler.ExecutableJobDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.scheduler.ExecutableJobDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.scheduler.ExecutableJobDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.scheduler.ExecutableJobDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
