package com.runwaysdk.dataaccess.graph;

public class GraphEmbeddedFieldProperties extends GraphFieldProperties
{
  private String embeddedClassName;

  public String getEmbeddedClassName()
  {
    return embeddedClassName;
  }

  public void setEmbeddedClassName(String embeddedClassName)
  {
    this.embeddedClassName = embeddedClassName;
  }

  public static GraphEmbeddedFieldProperties build(String dbClassName, String dbAttrName, boolean required, boolean changeOverTime, String linkedAttributeClassName)
  {
    GraphEmbeddedFieldProperties properties = new GraphEmbeddedFieldProperties();
    properties.setClassName(dbClassName);
    properties.setAttributeName(dbAttrName);
    properties.setRequired(required);
    properties.setCot(changeOverTime);
    properties.setEmbeddedClassName(linkedAttributeClassName);

    return properties;
  }

}
