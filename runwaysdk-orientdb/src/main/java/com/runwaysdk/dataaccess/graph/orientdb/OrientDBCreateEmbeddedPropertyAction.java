package com.runwaysdk.dataaccess.graph.orientdb;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OClass.INDEX_TYPE;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.graph.GraphRequest;

public class OrientDBCreateEmbeddedPropertyAction extends OrientDBDDLAction
{
  protected String   className;

  protected String   attributeName;

  private   String   embeddedClassName;

  private   boolean  required;
  

  /**
   * @param className
   * @param attributeName
   * @param embeddedClassName
   * @param required
   */
  public OrientDBCreateEmbeddedPropertyAction(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, String embeddedClassName, boolean required)
  {
    super(graphRequest, ddlGraphDBRequest);

    this.className = className;
    this.attributeName = attributeName;
    this.embeddedClassName = embeddedClassName;
    this.required = required;
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
    }
  }

  protected void configure(OProperty oProperty)
  {
    oProperty.setMandatory(this.required);
  }

}
