package com.runwaysdk.system.metadata.graph;

public class EmbeddedGraphInheritance extends EmbeddedGraphInheritanceBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1000043034;
  
  public EmbeddedGraphInheritance(String parentOid, String childOid)
  {
    super(parentOid, childOid);
  }
  
  public EmbeddedGraphInheritance(com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClass parent, com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClass child)
  {
    this(parent.getOid(), child.getOid());
  }
  
}
