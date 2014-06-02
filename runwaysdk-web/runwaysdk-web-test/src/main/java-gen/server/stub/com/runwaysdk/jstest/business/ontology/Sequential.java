package com.runwaysdk.jstest.business.ontology;

public class Sequential extends SequentialBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 1067329153;
  
  public Sequential(String parentId, String childId)
  {
    super(parentId, childId);
  }
  
  public Sequential(com.runwaysdk.jstest.business.ontology.Alphabet parent, com.runwaysdk.jstest.business.ontology.Alphabet child)
  {
    this(parent.getId(), child.getId());
  }
  
}
