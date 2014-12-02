package com.runwaysdk.jstest.business.ontology;

public class AlphabetAttributeRoot extends AlphabetAttributeRootBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 1250958818;
  
  public AlphabetAttributeRoot(String parentId, String childId)
  {
    super(parentId, childId);
  }
  
  public AlphabetAttributeRoot(com.runwaysdk.system.metadata.MdAttributeTerm parent, com.runwaysdk.jstest.business.ontology.Alphabet child)
  {
    this(parent.getId(), child.getId());
  }
  
}
