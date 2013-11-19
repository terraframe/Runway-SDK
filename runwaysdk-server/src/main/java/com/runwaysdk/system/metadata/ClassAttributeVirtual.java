package com.runwaysdk.system.metadata;

public class ClassAttributeVirtual extends ClassAttributeVirtualBase
{
  private static final long serialVersionUID = -1471084713;
  
  public ClassAttributeVirtual(String parentId, String childId)
  {
    super(parentId, childId);
  }
  
  public ClassAttributeVirtual(com.runwaysdk.system.metadata.MdView parent, com.runwaysdk.system.metadata.MdAttributeVirtual child)
  {
    this(parent.getId(), child.getId());
  }
  
}
