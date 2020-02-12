package com.runwaysdk.system.scheduler;

public class JobHasStage extends JobHasStageBase
{
  private static final long serialVersionUID = 1982204482;
  
  public JobHasStage(String parentOid, String childOid)
  {
    super(parentOid, childOid);
  }
  
  public JobHasStage(com.runwaysdk.system.scheduler.MultiStageJob parent, com.runwaysdk.system.scheduler.AbstractJob child)
  {
    this(parent.getOid(), child.getOid());
  }
  
}
