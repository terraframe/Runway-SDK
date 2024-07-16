package com.runwaysdk.dataaccess.graph;

public class GraphCharacterFieldProperties extends GraphFieldProperties
{
  private int maxLength;

  public int getMaxLength()
  {
    return maxLength;
  }

  public void setMaxLength(int maxLength)
  {
    this.maxLength = maxLength;
  }

  public static GraphCharacterFieldProperties build(String dbClassName, String dbAttrName, boolean required, boolean changeOverTime, int maxLength)
  {
    GraphCharacterFieldProperties properties = new GraphCharacterFieldProperties();
    properties.setClassName(dbClassName);
    properties.setAttributeName(dbAttrName);
    properties.setRequired(required);
    properties.setCot(changeOverTime);
    properties.setMaxLength(maxLength);

    return properties;
  }
}
