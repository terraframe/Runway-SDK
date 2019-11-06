package com.runwaysdk.dataaccess.graph.orientdb;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.graph.GraphRequest;

public class OrientDBCreateEmbeddedPropertyAction extends OrientDBDDLAction
{
  protected String className;

  protected String attributeName;

  private String   embeddedClassName;

  private boolean  required;

  private boolean  cot;

  /**
   * @param className
   * @param attributeName
   * @param embeddedClassName
   * @param required
   * @param cot
   *          TODO
   */
  public OrientDBCreateEmbeddedPropertyAction(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, String embeddedClassName, boolean required, boolean cot)
  {
    super(graphRequest, ddlGraphDBRequest);

    this.className = className;
    this.attributeName = attributeName;
    this.embeddedClassName = embeddedClassName;
    this.required = required;
    this.cot = cot;
  }

  @Override
  protected void executeDDL(ODatabaseSession db)
  {
    OClass oClass = db.getClass(this.className);

    if (oClass != null)
    {
      OSchema schema = db.getMetadata().getSchema();
      OClass linkClass = schema.getClass(this.embeddedClassName);

      if (linkClass == null)
      {
        throw new ProgrammingErrorException("Unable to find embedded class type");
      }

      OProperty oProperty = oClass.createProperty(this.attributeName, OType.EMBEDDED, linkClass);

      configure(oProperty);

      this.createIndex(oClass, oProperty);

      if (this.cot)
      {
        oClass.createProperty(this.attributeName + OrientDBConstant.COT_SUFFIX, OType.EMBEDDEDLIST, OrientDBImpl.getOrCreateChangeOverTime(db, linkClass));
      }
    }
  }

  protected void createIndex(OClass oClass, OProperty oProperty)
  {
    // Do nothing: behavior might be overwritten in sub classes
  }

  protected void configure(OProperty oProperty)
  {
    oProperty.setMandatory(this.required);
  }

}
