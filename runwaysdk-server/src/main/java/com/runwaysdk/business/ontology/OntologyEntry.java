package com.runwaysdk.business.ontology;

public class OntologyEntry implements OntologyEntryIF
{
  private String label;

  private String id;

  /**
   * @param label
   * @param id
   */
  public OntologyEntry(String label, String id)
  {
    super();
    this.label = label;
    this.id = id;
  }

  @Override
  public String getId()
  {
    return id;
  }

  @Override
  public String getLabel()
  {
    return label;
  }

}
