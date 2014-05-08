package com.runwaysdk.system.scheduler;

public class JobHistoryRecord extends JobHistoryRecordBase
{
  private static final long serialVersionUID = -764169172;
  
  public JobHistoryRecord(String parentId, String childId)
  {
    super(parentId, childId);
  }
  
  public JobHistoryRecord(com.runwaysdk.system.scheduler.ExecutableJob parent, com.runwaysdk.system.scheduler.JobHistory child)
  {
    this(parent.getId(), child.getId());
  }
  
}
