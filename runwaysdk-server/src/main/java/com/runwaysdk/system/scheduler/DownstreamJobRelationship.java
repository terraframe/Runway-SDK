package com.runwaysdk.system.scheduler;

public class DownstreamJobRelationship extends DownstreamJobRelationshipBase
{
  private static final long serialVersionUID = -562014163;
  
  public DownstreamJobRelationship(String parentId, String childId)
  {
    super(parentId, childId);
  }
  
  public DownstreamJobRelationship(com.runwaysdk.system.scheduler.ExecutableJob parent, com.runwaysdk.system.scheduler.ExecutableJob child)
  {
    this(parent.getId(), child.getId());
  }
  
}
