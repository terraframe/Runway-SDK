package com.runwaysdk.system.metadata;

public class VertexInheritance extends VertexInheritanceBase
{
  private static final long serialVersionUID = 1496177772;
  
  public VertexInheritance(String parentOid, String childOid)
  {
    super(parentOid, childOid);
  }
  
  public VertexInheritance(com.runwaysdk.system.metadata.MdVertex parent, com.runwaysdk.system.metadata.MdVertex child)
  {
    this(parent.getOid(), child.getOid());
  }
  
}
