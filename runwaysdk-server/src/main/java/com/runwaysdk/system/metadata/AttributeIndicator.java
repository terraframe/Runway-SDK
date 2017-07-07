package com.runwaysdk.system.metadata;

public class AttributeIndicator extends AttributeIndicatorBase
{
  private static final long serialVersionUID = -551637739;
  
  public AttributeIndicator(String parentId, String childId)
  {
    super(parentId, childId);
  }
  
  public AttributeIndicator(com.runwaysdk.system.metadata.MdAttributeIndicator parent, com.runwaysdk.system.metadata.IndicatorComposite child)
  {
    this(parent.getId(), child.getId());
  }
  
}
