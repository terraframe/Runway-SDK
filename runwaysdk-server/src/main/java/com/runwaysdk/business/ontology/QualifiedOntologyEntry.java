package com.runwaysdk.business.ontology;

public class QualifiedOntologyEntry implements QualifiedOntologyEntryIF
{
  private String id;

  private String label;

  private String qualifier;

  /**
   * @param id
   * @param label
   * @param qualifier
   */
  public QualifiedOntologyEntry(String id, String label, String qualifier)
  {
    super();
    this.id = id;
    this.label = label;
    this.qualifier = qualifier;
  }

  /**
   * @return the id
   */
  public String getId()
  {
    return id;
  }

  /**
   * @return the label
   */
  public String getLabel()
  {
    return label;
  }

  /**
   * @return the qualifier
   */
  public String getQualifier()
  {
    return qualifier;
  }

}
