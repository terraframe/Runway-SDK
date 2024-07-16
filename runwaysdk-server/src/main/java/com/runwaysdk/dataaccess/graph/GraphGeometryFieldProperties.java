package com.runwaysdk.dataaccess.graph;

public class GraphGeometryFieldProperties extends GraphEmbeddedFieldProperties
{
  boolean createIndex;

  public boolean isCreateIndex()
  {
    return createIndex;
  }

  public void setCreateIndex(boolean createIndex)
  {
    this.createIndex = createIndex;
  }

  public static GraphGeometryFieldProperties build(String dbClassName, String dbAttrName, boolean required, boolean changeOverTime, String linkedAttributeClassName, boolean createIndex)
  {
    GraphGeometryFieldProperties properties = new GraphGeometryFieldProperties();
    properties.setClassName(dbClassName);
    properties.setAttributeName(dbAttrName);
    properties.setRequired(required);
    properties.setCot(changeOverTime);
    properties.setEmbeddedClassName(linkedAttributeClassName);
    properties.setCreateIndex(createIndex);

    return properties;
  }


  
}
