package com.runwaysdk.system.ontology;

public class ClassifierIsARelationship extends ClassifierIsARelationshipBase
{
  private static final long serialVersionUID = 2146338014;
  
  public ClassifierIsARelationship(String parentId, String childId)
  {
    super(parentId, childId);
  }
  
  public ClassifierIsARelationship(com.runwaysdk.system.ontology.Classifier parent, com.runwaysdk.system.ontology.Classifier child)
  {
    this(parent.getId(), child.getId());
  }
  
}
