package com.runwaysdk.system.metadata;

public class AttributeRatio extends AttributeRatioBase
{
  private static final long serialVersionUID = 375907337;
  
  public AttributeRatio(String parentId, String childId)
  {
    super(parentId, childId);
  }
  
  public AttributeRatio(com.runwaysdk.system.metadata.MdAttributeRatio parent, com.runwaysdk.system.Ratio child)
  {
    this(parent.getId(), child.getId());
  }
  
}
