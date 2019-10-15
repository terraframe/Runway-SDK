package com.runwaysdk.dataaccess.graph.orientdb;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.runwaysdk.dataaccess.graph.GraphRequest;

public class OrientDBCreateEmbeddedPropertyAction extends OrientDBDDLAction
{
  private String  className;

  private String  attributeName;

  private String  schemaType;

  private boolean required;

  /**
   * @param className
   * @param attributeName
   * @param schemaType
   * @param required
   */
  public OrientDBCreateEmbeddedPropertyAction(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, String schemaType, boolean required)
  {
    super(graphRequest, ddlGraphDBRequest);

    this.className = className;
    this.attributeName = attributeName;
    this.schemaType = schemaType;
    this.required = required;
  }

  @Override
  protected void executeDDL(ODatabaseSession db)
  {
    OClass oClass = db.getClass(this.className);

    if (oClass != null)
    {
      OSchema schema = db.getMetadata().getSchema();
      OProperty oProperty = oClass.createProperty(this.attributeName, OType.EMBEDDED, schema.getClass(this.schemaType));

      configure(oProperty);
    }
  }

  protected void configure(OProperty oProperty)
  {
    oProperty.setMandatory(this.required);
  }

}
