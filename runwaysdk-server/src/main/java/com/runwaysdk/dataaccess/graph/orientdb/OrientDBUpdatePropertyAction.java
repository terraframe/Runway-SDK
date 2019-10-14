package com.runwaysdk.dataaccess.graph.orientdb;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OProperty;

public abstract class OrientDBUpdatePropertyAction extends OrientDBDDLAction
{
  private String className;

  private String attributeName;

  /**
   * @param className
   * @param attributeName
   * @param columnType
   * @param required
   */
  public OrientDBUpdatePropertyAction(String className, String attributeName)
  {
    super();

    this.className = className;
    this.attributeName = attributeName;
  }

  @Override
  protected void executeDDL(ODatabaseSession db)
  {
    OClass oClass = db.getClass(className);

    if (oClass != null)
    {
      OProperty oProperty = oClass.getProperty(attributeName);

      this.configure(oProperty);
    }
  }

  protected abstract void configure(OProperty oProperty);

}
