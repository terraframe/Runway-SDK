/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.query;

import java.util.Map;
import java.util.Set;

import com.runwaysdk.gis.dataaccess.MdAttributeGeometryDAOIF;
import com.vividsolutions.jts.geom.Geometry;

public class ST_WITHIN extends Condition
{
  private StatementGeometry  statement;

  private SelectableGeometry selectable;

  public ST_WITHIN(Geometry geometry, SelectableGeometry selectable)
  {
    this.statement = new StatementGeometry(geometry, ( (MdAttributeGeometryDAOIF) selectable.getMdAttributeIF() ).getSRID());
    this.selectable = selectable;
  }

  @Override
  public Set<Join> getJoinStatements()
  {
    return null;
  }

  @Override
  public Map<String, String> getFromTableMap()
  {
    return null;
  }

  /**
   * Visitor to traverse the query object structure.
   * 
   * @param visitor
   */
  public void accept(Visitor visitor)
  {
    visitor.visit(this);

    this.statement.accept(visitor);
  }

  /**
   * Returns the SQL representation of this condition.
   *
   * @return SQL representation of this condition.
   */
  public String getSQL()
  {
    String statementSQL1 = this.statement.getSQL();
    String statementSQL2 = this.selectable.getSQL();

    return "ST_Within(" + statementSQL1 + ", " + statementSQL2 + ")";
  }
}
