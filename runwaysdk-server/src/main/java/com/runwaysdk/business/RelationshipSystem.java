package com.runwaysdk.business;

public abstract class RelationshipSystem extends RelationshipSystemBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1191165235;
  
  public RelationshipSystem(String parentOid, String childOid)
  {
    super(parentOid, childOid);
  }
  
  public RelationshipSystem(com.runwaysdk.business.Business parent, com.runwaysdk.business.Business child)
  {
    this(parent.getOid(), child.getOid());
  }
  
}
