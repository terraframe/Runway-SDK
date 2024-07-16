/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.dataaccess.graph.orientdb;

import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OClass.INDEX_TYPE;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.runwaysdk.dataaccess.graph.GraphEmbeddedFieldProperties;
import com.runwaysdk.dataaccess.graph.GraphGeometryFieldProperties;
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
   * @param createIndex
   *          TODO
   */
  public OrientDBCreateGeometryPropertyAction(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, GraphGeometryFieldProperties properties)
  {
    super(graphRequest, ddlGraphDBRequest, properties);
  }
  
  @Override
  public GraphGeometryFieldProperties getProperties()
  {
    return (GraphGeometryFieldProperties) super.getProperties();
  }

  protected void createIndex(OClass oClass, OProperty oProperty)
  {
    if (this.getProperties().isCreateIndex())
    {

      /*
       * Create the spatial index
       */
      String indexName = OrientDBImpl.generateIndexName();

      oClass.createIndex(indexName, INDEX_TYPE.SPATIAL.name(), null, null, "LUCENE", new String[] { oProperty.getName() });
    }
  }
}
