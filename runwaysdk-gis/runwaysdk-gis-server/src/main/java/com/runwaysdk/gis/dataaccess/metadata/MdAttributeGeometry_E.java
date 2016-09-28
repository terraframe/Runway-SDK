/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.gis.dataaccess.metadata;

import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcrete_E;



public class MdAttributeGeometry_E extends MdAttributeConcrete_E
{
  /**
   *
   */
  private static final long serialVersionUID = -3969398976285120647L;

  /**
   * @param mdAttribute
   */
  public MdAttributeGeometry_E(MdAttributeConcreteDAO mdAttribute)
  {
    super(mdAttribute);
  }

  /**
   * Returns the MdAttribute.
   *
   * @return the MdAttribute
   */
  protected MdAttributeGeometryDAO getMdAttribute()
  {
    return (MdAttributeGeometryDAO)super.getMdAttribute();
  }

  /**
   * Adds a column in the database to the given table.
   *
   * <br/><b>Precondition:</b>  tableName != null
   * <br/><b>Precondition:</b>  !tableName.trim().equals("")
   *
   */
  protected void createDbColumn(String tableName)
  {
    MdAttributeGeometryDAO mdAttributeGeometryDAO = this.getMdAttribute();

    Database.addGeometryColumn(tableName, mdAttributeGeometryDAO.getColumnName(), mdAttributeGeometryDAO.getSRID(), this.dbColumnType, mdAttributeGeometryDAO.getDimension());
  }

  protected void dropAttribute(String tableName, String attributeName, String dbColumnType)
  {
    Database.dropGeometryColumn(tableName, attributeName);
  }
}
