package com.runwaysdk.dataaccess.graph.orientdb;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.orientechnologies.orient.core.metadata.schema.OClass.INDEX_TYPE;
import com.runwaysdk.dataaccess.graph.GraphRequest;

public class OrientDBCreateGeometryPropertyAction extends OrientDBCreateEmbeddedPropertyAction
{
  /**
   * @param className
   * @param attributeName
   * @param embeddedClassName
   * @param required
   */
  public OrientDBCreateGeometryPropertyAction(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, String embeddedClassName, boolean required)
  {
    super(graphRequest, ddlGraphDBRequest, className, attributeName, embeddedClassName, required);
  }
  
  @Override
  protected void executeDDL(ODatabaseSession db)
  {
    super.executeDDL(db);
    
    OClass oClass = db.getClass(this.className);
    
    if (oClass != null)
    {
      OProperty oProperty = oClass.getProperty(this.attributeName);
      
      if (oProperty != null)
      {
        /*
         * Create the spatial index
         */
        String indexName = OrientDBImpl.generateIndexName();

        oClass.createIndex(indexName, INDEX_TYPE.SPATIAL.name(), null, null, "LUCENE", new String[] { oProperty.getName() });
      }
    }
  }
}
