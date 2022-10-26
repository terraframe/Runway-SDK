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
package com.runwaysdk.system.scheduler;

@com.runwaysdk.business.ClassSignature(hash = -559616087)
public abstract class JobHistoryViewDTOBase extends com.runwaysdk.business.ViewDTO
{
  public final static String CLASS = "com.runwaysdk.system.scheduler.JobHistoryView";
  private static final long serialVersionUID = -559616087;
  
  protected JobHistoryViewDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public final static java.lang.String CREATEDATE = "createDate";
  public final static java.lang.String CRONEXPRESSION = "cronExpression";
  public final static java.lang.String DESCRIPTION = "description";
  public final static java.lang.String DISPLAYLABEL = "displayLabel";
  public final static java.lang.String ENDTIME = "endTime";
  public final static java.lang.String HISTORYCOMMENT = "historyComment";
  public final static java.lang.String HISTORYINFORMATION = "historyInformation";
  public final static java.lang.String JOBOPERATION = "jobOperation";
  public final static java.lang.String LASTRUN = "lastRun";
  public final static java.lang.String OID = "oid";
  public final static java.lang.String STARTTIME = "startTime";
  public final static java.lang.String STATUS = "status";
  public final static java.lang.String STATUSLABEL = "statusLabel";
  public final static java.lang.String WORKPROGRESS = "workProgress";
  public final static java.lang.String WORKTOTAL = "workTotal";
  public java.util.Date getCreateDate()
  {
    return com.runwaysdk.constants.MdAttributeDateTimeUtil.getTypeSafeValue(getValue(CREATEDATE));
  }
  
  public void setCreateDate(java.util.Date value)
  {
    if(value == null)
    {
      setValue(CREATEDATE, "");
    }
    else
    {
      setValue(CREATEDATE, new java.text.SimpleDateFormat(com.runwaysdk.constants.Constants.DATETIME_FORMAT).format(value));
    }
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
  
  public com.runwaysdk.system.scheduler.AbstractJobDisplayLabelDTO getDisplayLabel()
  {
    return (com.runwaysdk.system.scheduler.AbstractJobDisplayLabelDTO) this.getAttributeStructDTO(DISPLAYLABEL).getStructDTO();
  }
  
  public boolean isDisplayLabelWritable()
  {
    return isWritable(DISPLAYLABEL);
  }
  
  public boolean isDisplayLabelReadable()
  {
    return isReadable(DISPLAYLABEL);
  }
  
  public boolean isDisplayLabelModified()
  {
    return isModified(DISPLAYLABEL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeLocalCharacterMdDTO getDisplayLabelMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeLocalCharacterMdDTO) getAttributeDTO(DISPLAYLABEL).getAttributeMdDTO();
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
  
  public String getStatusLabel()
  {
    return getValue(STATUSLABEL);
  }
  
  public void setStatusLabel(String value)
  {
    if(value == null)
    {
      setValue(STATUSLABEL, "");
    }
    else
    {
      setValue(STATUSLABEL, value);
    }
  }
  
  public boolean isStatusLabelWritable()
  {
    return isWritable(STATUSLABEL);
  }
  
  public boolean isStatusLabelReadable()
  {
    return isReadable(STATUSLABEL);
  }
  
  public boolean isStatusLabelModified()
  {
    return isModified(STATUSLABEL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getStatusLabelMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(STATUSLABEL).getAttributeMdDTO();
  }
  
  public Long getWorkProgress()
  {
    return com.runwaysdk.constants.MdAttributeLongUtil.getTypeSafeValue(getValue(WORKPROGRESS));
  }
  
  public void setWorkProgress(Long value)
  {
    if(value == null)
    {
      setValue(WORKPROGRESS, "");
    }
    else
    {
      setValue(WORKPROGRESS, java.lang.Long.toString(value));
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
  
  public Long getWorkTotal()
  {
    return com.runwaysdk.constants.MdAttributeLongUtil.getTypeSafeValue(getValue(WORKTOTAL));
  }
  
  public void setWorkTotal(Long value)
  {
    if(value == null)
    {
      setValue(WORKTOTAL, "");
    }
    else
    {
      setValue(WORKTOTAL, java.lang.Long.toString(value));
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
  
  public static final com.runwaysdk.system.scheduler.JobHistoryViewQueryDTO getJobHistories(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String sortAttribute, java.lang.Boolean isAscending, java.lang.Integer pageSize, java.lang.Integer pageNumber)
  {
    String[] _declaredTypes = new String[]{"java.lang.String", "java.lang.Boolean", "java.lang.Integer", "java.lang.Integer"};
    Object[] _parameters = new Object[]{sortAttribute, isAscending, pageSize, pageNumber};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.scheduler.JobHistoryViewDTO.CLASS, "getJobHistories", _declaredTypes);
    return (com.runwaysdk.system.scheduler.JobHistoryViewQueryDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static JobHistoryViewDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.ViewDTO dto = (com.runwaysdk.business.ViewDTO)clientRequest.get(oid);
    
    return (JobHistoryViewDTO) dto;
  }
  
  public void apply()
  {
    if(isNewInstance())
    {
      getRequest().createSessionComponent(this);
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
  
}
