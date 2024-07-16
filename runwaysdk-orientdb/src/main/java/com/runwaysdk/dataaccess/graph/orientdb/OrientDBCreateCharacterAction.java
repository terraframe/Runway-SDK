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

import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.runwaysdk.dataaccess.graph.GraphCharacterFieldProperties;
import com.runwaysdk.dataaccess.graph.GraphFieldProperties;
import com.runwaysdk.dataaccess.graph.GraphRequest;

public class OrientDBCreateCharacterAction extends OrientDBCreatePropertyAction
{
  /**
   * @param className
   * @param attributeName
   * @param columnType
   * @param required
   * @param cot
   *          TODO
   */
  public OrientDBCreateCharacterAction(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, GraphCharacterFieldProperties properties)
  {
    super(graphRequest, ddlGraphDBRequest, properties);

    this.getProperties().setColumnType(OType.STRING.name());
  }

  @Override
  public GraphCharacterFieldProperties getProperties()
  {
    return (GraphCharacterFieldProperties) super.getProperties();
  }

  protected void configure(OProperty oProperty)
  {
    super.configure(oProperty);

    oProperty.setMax(Integer.toString(this.getProperties().getMaxLength()));
  }

}
