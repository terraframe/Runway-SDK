package com.runwaysdk.dataaccess.graph;

public class GraphFieldProperties
{
  private String  className;

  private String  attributeName;

  private String  columnType;

  private boolean required;

  private boolean cot;

  public String getClassName()
  {
    return className;
  }

  public void setClassName(String className)
  {
    this.className = className;
  }

  public String getAttributeName()
  {
    return attributeName;
  }

  public void setAttributeName(String attributeName)
  {
    this.attributeName = attributeName;
  }

  public boolean isRequired()
  {
    return required;
  }

  public void setRequired(boolean required)
  {
    this.required = required;
  }

  public boolean isCot()
  {
    return cot;
  }

  public void setCot(boolean cot)
  {
    this.cot = cot;
  }

  public String getColumnType()
  {
    return columnType;
  }

  public void setColumnType(String columnType)
  {
    this.columnType = columnType;
  }

  public static GraphFieldProperties build(String dbClassName, String dbAttrName, boolean required, boolean changeOverTime, String dbColumnType)
  {
    GraphFieldProperties properties = new GraphFieldProperties();
    properties.setClassName(dbClassName);
    properties.setAttributeName(dbAttrName);
    properties.setRequired(required);
    properties.setCot(changeOverTime);
    properties.setColumnType(dbColumnType);

    return properties;
  }

}
