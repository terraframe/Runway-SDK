package com.runwaysdk.dataaccess.graph.attributes;

public class AttributeCharacter extends Attribute
{

  /**
   * @param name
   * @param mdAttributeKey
   * @param definingGraphClass
   * @param value
   */
  public AttributeCharacter(String name, String mdAttributeKey, String definingGraphClass, String value)
  {
    super(name, mdAttributeKey, definingGraphClass, value);
    // TODO Auto-generated constructor stub
  }

  /**
   * @param name
   * @param mdAttributeKey
   * @param definingGraphClass
   */
  public AttributeCharacter(String name, String mdAttributeKey, String definingGraphClass)
  {
    super(name, mdAttributeKey, definingGraphClass);
    // TODO Auto-generated constructor stub
  }

  @Override
  public Attribute attributeClone()
  {
    // TODO Auto-generated method stub
    return null;
  }

}
