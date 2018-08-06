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

@com.runwaysdk.business.ClassSignature(hash = 166736944)
public abstract class JobViewDTOBase extends com.runwaysdk.business.ViewDTO
{
  public final static String CLASS = "com.runwaysdk.system.scheduler.JobView";
  private static final long serialVersionUID = 166736944;
  
  protected JobViewDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String DOWNSTREAMJOB = "downstreamJob";
  public static java.lang.String DOWNSTREAMJOBDISPLAYLABEL = "downstreamJobDisplayLabel";
  public static java.lang.String ID = "oid";
  public static java.lang.String JOB = "job";
  public static java.lang.String TRIGGERONFAILURE = "triggerOnFailure";
  public com.runwaysdk.system.scheduler.ExecutableJobDTO getDownstreamJob()
  {
    if(getValue(DOWNSTREAMJOB) == null || getValue(DOWNSTREAMJOB).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.scheduler.ExecutableJobDTO.get(getRequest(), getValue(DOWNSTREAMJOB));
    }
  }
  
  public String getDownstreamJobId()
  {
    return getValue(DOWNSTREAMJOB);
  }
  
  public void setDownstreamJob(com.runwaysdk.system.scheduler.ExecutableJobDTO value)
  {
    if(value == null)
    {
      setValue(DOWNSTREAMJOB, "");
    }
    else
    {
      setValue(DOWNSTREAMJOB, value.getOid());
    }
  }
  
  public boolean isDownstreamJobWritable()
  {
    return isWritable(DOWNSTREAMJOB);
  }
  
  public boolean isDownstreamJobReadable()
  {
    return isReadable(DOWNSTREAMJOB);
  }
  
  public boolean isDownstreamJobModified()
  {
    return isModified(DOWNSTREAMJOB);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getDownstreamJobMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(DOWNSTREAMJOB).getAttributeMdDTO();
  }
  
  public String getDownstreamJobDisplayLabel()
  {
    return getValue(DOWNSTREAMJOBDISPLAYLABEL);
  }
  
  public void setDownstreamJobDisplayLabel(String value)
  {
    if(value == null)
    {
      setValue(DOWNSTREAMJOBDISPLAYLABEL, "");
    }
    else
    {
      setValue(DOWNSTREAMJOBDISPLAYLABEL, value);
    }
  }
  
  public boolean isDownstreamJobDisplayLabelWritable()
  {
    return isWritable(DOWNSTREAMJOBDISPLAYLABEL);
  }
  
  public boolean isDownstreamJobDisplayLabelReadable()
  {
    return isReadable(DOWNSTREAMJOBDISPLAYLABEL);
  }
  
  public boolean isDownstreamJobDisplayLabelModified()
  {
    return isModified(DOWNSTREAMJOBDISPLAYLABEL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getDownstreamJobDisplayLabelMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(DOWNSTREAMJOBDISPLAYLABEL).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.scheduler.ExecutableJobDTO getJob()
  {
    if(getValue(JOB) == null || getValue(JOB).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.scheduler.ExecutableJobDTO.get(getRequest(), getValue(JOB));
    }
  }
  
  public String getJobId()
  {
    return getValue(JOB);
  }
  
  public void setJob(com.runwaysdk.system.scheduler.ExecutableJobDTO value)
  {
    if(value == null)
    {
      setValue(JOB, "");
    }
    else
    {
      setValue(JOB, value.getOid());
    }
  }
  
  public boolean isJobWritable()
  {
    return isWritable(JOB);
  }
  
  public boolean isJobReadable()
  {
    return isReadable(JOB);
  }
  
  public boolean isJobModified()
  {
    return isModified(JOB);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getJobMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(JOB).getAttributeMdDTO();
  }
  
  public Boolean getTriggerOnFailure()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(TRIGGERONFAILURE));
  }
  
  public void setTriggerOnFailure(Boolean value)
  {
    if(value == null)
    {
      setValue(TRIGGERONFAILURE, "");
    }
    else
    {
      setValue(TRIGGERONFAILURE, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isTriggerOnFailureWritable()
  {
    return isWritable(TRIGGERONFAILURE);
  }
  
  public boolean isTriggerOnFailureReadable()
  {
    return isReadable(TRIGGERONFAILURE);
  }
  
  public boolean isTriggerOnFailureModified()
  {
    return isModified(TRIGGERONFAILURE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getTriggerOnFailureMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(TRIGGERONFAILURE).getAttributeMdDTO();
  }
  
  public final void applyWithJob(com.runwaysdk.system.scheduler.ExecutableJobDTO job)
  {
    String[] _declaredTypes = new String[]{"com.runwaysdk.system.scheduler.ExecutableJob"};
    Object[] _parameters = new Object[]{job};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.scheduler.JobViewDTO.CLASS, "applyWithJob", _declaredTypes);
    getRequest().invokeMethod(_metadata, this, _parameters);
  }
  
  public static final void applyWithJob(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid, com.runwaysdk.system.scheduler.ExecutableJobDTO job)
  {
    String[] _declaredTypes = new String[]{"java.lang.String", "com.runwaysdk.system.scheduler.ExecutableJob"};
    Object[] _parameters = new Object[]{oid, job};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.scheduler.JobViewDTO.CLASS, "applyWithJob", _declaredTypes);
    clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final com.runwaysdk.system.scheduler.JobViewDTO lockJob(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String jobId)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{jobId};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.scheduler.JobViewDTO.CLASS, "lockJob", _declaredTypes);
    return (com.runwaysdk.system.scheduler.JobViewDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static JobViewDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.ViewDTO dto = (com.runwaysdk.business.ViewDTO)clientRequest.get(oid);
    
    return (JobViewDTO) dto;
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
