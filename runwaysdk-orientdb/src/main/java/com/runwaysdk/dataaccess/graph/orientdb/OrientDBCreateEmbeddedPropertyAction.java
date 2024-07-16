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

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.graph.GraphEmbeddedFieldProperties;
import com.runwaysdk.dataaccess.graph.GraphRequest;

public class OrientDBCreateEmbeddedPropertyAction extends OrientDBDDLAction
{
  private GraphEmbeddedFieldProperties properties;

  /**
   * @param className
   * @param attributeName
   * @param embeddedClassName
   * @param required
   * @param cot 
   *          TODO
   */
  public OrientDBCreateEmbeddedPropertyAction(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, GraphEmbeddedFieldProperties properties)
  {
    super(graphRequest, ddlGraphDBRequest);

    this.properties = properties;
  }

  public GraphEmbeddedFieldProperties getProperties()
  {
    return properties;
  }

  @Override
  protected void executeDDL(ODatabaseSession db)
  {
    OClass oClass = db.getClass(this.properties.getClassName());

    if (oClass != null)
    {
      OSchema schema = db.getMetadata().getSchema();
      OClass linkClass = schema.getClass(this.properties.getEmbeddedClassName());

      if (linkClass == null)
      {
        throw new ProgrammingErrorException("Unable to find embedded class type");
      }

      OProperty oProperty = oClass.createProperty(this.properties.getAttributeName(), OType.EMBEDDED, linkClass);

      configure(oProperty);

      this.createIndex(oClass, oProperty);

      if (this.properties.isCot())
      {
        oClass.createProperty(this.properties.getAttributeName() + OrientDBConstant.COT_SUFFIX, OType.EMBEDDEDLIST, OrientDBImpl.getOrCreateChangeOverTime(db, linkClass, OType.EMBEDDED));
      }
    }
  }

  protected void createIndex(OClass oClass, OProperty oProperty)
  {
    // Do nothing: behavior might be overwritten in sub classes
  }

  protected void configure(OProperty oProperty)
  {
    oProperty.setMandatory(this.properties.isRequired());
  }

}
