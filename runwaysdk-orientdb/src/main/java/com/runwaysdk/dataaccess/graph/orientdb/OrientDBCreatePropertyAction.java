/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.dataaccess.graph.orientdb;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.runwaysdk.dataaccess.graph.GraphRequest;

public class OrientDBCreatePropertyAction extends OrientDBDDLAction
{
  private String  className;

  private String  attributeName;

  private String  columnType;

  private boolean required;

  private boolean cot;

  /**
   * @param className
   * @param attributeName
   * @param columnType
   * @param required
   * @param cot
   *          TODO
   */
  public OrientDBCreatePropertyAction(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, String attributeName, String columnType, boolean required, boolean cot)
  {
    super(graphRequest, ddlGraphDBRequest);

    this.className = className;
    this.attributeName = attributeName;
    this.columnType = columnType;
    this.required = required;
    this.cot = cot;
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
