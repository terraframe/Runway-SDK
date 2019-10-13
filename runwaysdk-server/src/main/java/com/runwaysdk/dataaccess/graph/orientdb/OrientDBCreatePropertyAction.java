package com.runwaysdk.dataaccess.graph.orientdb;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.orientechnologies.orient.core.metadata.schema.OType;

public class OrientDBCreatePropertyAction extends OrientDBDDLAction
{
  private String  className;

  private String  attributeName;

  private String  columnType;

  private boolean required;

  /**
   * @param className
   * @param attributeName
   * @param columnType
   * @param required
   */
  public OrientDBCreatePropertyAction(String className, String attributeName, String columnType, boolean required)
  {
    super();

    this.className = className;
    this.attributeName = attributeName;
    this.columnType = columnType;
    this.required = required;
  }

  @Override
  protected void executeDDL(ODatabaseSession db)
  {
    OClass oClass = db.getClass(this.className);

    if (oClass != null)
    {
      OType oType = OType.valueOf(this.columnType);

      OProperty oProperty = oClass.createProperty(this.attributeName, oType);

      configure(oProperty);
    }
  }

  protected void configure(OProperty oProperty)
  {
    oProperty.setMandatory(this.required);
  }

}
