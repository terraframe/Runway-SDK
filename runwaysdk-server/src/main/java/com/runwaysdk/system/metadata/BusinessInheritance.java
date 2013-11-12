package com.runwaysdk.system.metadata;

public class BusinessInheritance extends BusinessInheritanceBase
{
  private static final long serialVersionUID = -735294576;
  
  public BusinessInheritance(String parentId, String childId)
  {
    super(parentId, childId);
  }
  
  public BusinessInheritance(com.runwaysdk.system.metadata.MdBusiness parent, com.runwaysdk.system.metadata.MdBusiness child)
  {
    this(parent.getId(), child.getId());
  }
  
}
