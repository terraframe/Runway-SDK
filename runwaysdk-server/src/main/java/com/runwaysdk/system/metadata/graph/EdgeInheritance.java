package com.runwaysdk.system.metadata.graph;

public class EdgeInheritance extends EdgeInheritanceBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1224355699;
  
  public EdgeInheritance(String parentOid, String childOid)
  {
    super(parentOid, childOid);
  }
  
  public EdgeInheritance(com.runwaysdk.system.metadata.MdEdge parent, com.runwaysdk.system.metadata.MdEdge child)
  {
    this(parent.getOid(), child.getOid());
  }
  
}
