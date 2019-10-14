package com.runwaysdk.dataaccess.graph.orientdb;

import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.runwaysdk.dataaccess.graph.GraphRequest;

public class OrientDBCreateCharacterAction extends OrientDBCreatePropertyAction
{
  private int maxLength;

  /**
   * @param className
   * @param attributeName
   * @param columnType
   * @param required
   */
  public OrientDBCreateCharacterAction(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, String columnType, boolean required, int maxLength)
  {
    super(graphRequest, ddlGraphDBRequest, className, attributeName, columnType, required);

    this.maxLength = maxLength;
  }

  protected void configure(OProperty oProperty)
  {
    super.configure(oProperty);
    
    oProperty.setMax(Integer.toString(this.maxLength));
  }

}
