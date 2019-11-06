package com.runwaysdk.dataaccess.graph.orientdb;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.runwaysdk.dataaccess.graph.GraphRequest;

public class OrientDBCreateEmbeddedSetPropertyAction extends OrientDBDDLAction
{
  protected String className;

  protected String attributeName;

  private boolean  required;

  private boolean  cot;

  /**
   * @param className
   * @param attributeName
   * @param required
   * @param cot
   *          TODO
   */
  public OrientDBCreateEmbeddedSetPropertyAction(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, boolean required, boolean cot)
  {
    super(graphRequest, ddlGraphDBRequest);

    this.className = className;
    this.attributeName = attributeName;
    this.required = required;
    this.cot = cot;
  }

  @Override
  protected void executeDDL(ODatabaseSession db)
  {
    OClass oClass = db.getClass(this.className);

    if (oClass != null)
    {
      OProperty oProperty = oClass.createProperty(this.attributeName, OType.EMBEDDEDSET, OType.STRING);

      configure(oProperty);

      if (this.cot)
      {
        oClass.createProperty(this.attributeName + OrientDBConstant.COT_SUFFIX, OType.EMBEDDEDLIST, OrientDBImpl.getOrCreateChangeOverTime(db));
      }
    }
  }

  protected void configure(OProperty oProperty)
  {
    oProperty.setMandatory(this.required);
  }

}
