package com.runwaysdk.system.scheduler;

@com.runwaysdk.business.ClassSignature(hash = 1937558960)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to ExecutableJob.java
 *
 * @author Autogenerated by RunwaySDK
 */
public class ExecutableJobQueryDTO extends com.runwaysdk.system.scheduler.AbstractJobQueryDTO
{
private static final long serialVersionUID = 1937558960;

  protected ExecutableJobQueryDTO(String type)
  {
    super(type);
  }

@SuppressWarnings("unchecked")
public java.util.List<? extends com.runwaysdk.system.scheduler.ExecutableJobDTO> getResultSet()
{
  return (java.util.List<? extends com.runwaysdk.system.scheduler.ExecutableJobDTO>)super.getResultSet();
}
}