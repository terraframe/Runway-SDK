package com.runwaysdk.dataaccess.graph.orientdb;

import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OClass.INDEX_TYPE;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.runwaysdk.dataaccess.graph.GraphRequest;

public class OrientDBCreateGeometryPropertyAction extends OrientDBCreateEmbeddedPropertyAction
{
  /**
   * @param className
   * @param attributeName
   * @param embeddedClassName
   * @param required
   * @param cot
   *          TODO
   */
  public OrientDBCreateGeometryPropertyAction(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, String embeddedClassName, boolean required, boolean cot)
  {
    super(graphRequest, ddlGraphDBRequest, className, attributeName, embeddedClassName, required, cot);
  }

  protected void createIndex(OClass oClass, OProperty oProperty)
  {
    /*
     * Create the spatial index
     */
    String indexName = OrientDBImpl.generateIndexName();

    oClass.createIndex(indexName, INDEX_TYPE.SPATIAL.name(), null, null, "LUCENE", new String[] { oProperty.getName() });
  }
}
