package com.runwaysdk.dataaccess.graph.orientdb;

import com.orientechnologies.orient.core.metadata.schema.OProperty;

public class OrientDBCreateCharacterAction extends OrientDBCreatePropertyAction
{
  private int maxLength;

  /**
   * @param className
   * @param attributeName
   * @param columnType
   * @param required
   */
  public OrientDBCreateCharacterAction(String className, String attributeName, String columnType, boolean required, int maxLength)
  {
    super(className, attributeName, columnType, required);

    this.maxLength = maxLength;
  }

  protected void configure(OProperty oProperty)
  {
    oProperty.setMax(Integer.toString(this.maxLength));
  }

}
