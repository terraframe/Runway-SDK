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

import java.util.Set;

import com.runwaysdk.constants.QueryConditions;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributeGeometryDAOIF;

public abstract class AttributeGeometry extends Attribute implements SelectableGeometry
{
  protected AttributeGeometry(MdAttributeGeometryDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
  }

  protected AttributeGeometry(MdAttributeGeometryDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel, MdAttributeStructDAOIF mdAttributeStructIF)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel, mdAttributeStructIF);
  }

  /**
   * Returns the a nested aggregate function in this composite function tree, if
   * there is one, or return null;
   * 
   * @return nested aggregate function in this composite function tree, if there
   *         is one, or return null;
   */
  public SelectableAggregate getAggregateFunction()
  {
    return null;
  }

  /**
   * Returns true if this selectable is an aggregate function or contains an
   * aggregate function. False otherwise.
   * 
   * @return true if this selectable is an aggregate function or contains an
   *         aggregate function. False otherwise.
   */
  public boolean isAggregateFunction()
  {
    if (this.getAggregateFunction() != null)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  /**
   * Returns the MdAttributeIF that defines the attribute.
   * 
   * @return MdAttributeIF that defines the attribute.
   */
  public MdAttributeGeometryDAOIF getMdAttributeIF()
  {
    return (MdAttributeGeometryDAOIF) super.getMdAttributeIF();
  }

  @Override
  public BasicCondition EQ(String wkt)
  {
    StatementPrimitive statementPrimitive;

    if (wkt == null || wkt.trim().equals(""))
    {
      statementPrimitive = new StatementPrimitive("NULL");
    }
    else
    {
      String formattedValue = "ST_GeomFromEWKT(SRID=" + this.getMdAttributeIF().getSRID() + ";'" + wkt + "')";
      statementPrimitive = new StatementPrimitive(formattedValue);
    }
    return new BasicConditionEq(this, statementPrimitive);
  }

  public BasicCondition NE(String wkt)
  {
    StatementPrimitive statementPrimitive;

    if (wkt == null || wkt.trim().equals(""))
    {
      statementPrimitive = new StatementPrimitive("NULL");
    }
    else
    {
      String formattedValue = "ST_GeomFromEWKT(SRID=" + this.getMdAttributeIF().getSRID() + ";'" + wkt + "')";
      statementPrimitive = new StatementPrimitive(formattedValue);
    }
    return new BasicConditionNotEq(this, statementPrimitive);
  }

  /**
   * Returns a condition based on the String version of the operator and the
   * String version of the value.
   * 
   * @param operator
   * @param value
   * @return condition based on the String version of the operator and the
   *         String version of the value.
   */
  public Condition getCondition(String operator, String value)
  {
    String trimmedOperator = operator.trim();
    if (trimmedOperator.equals(QueryConditions.EQUALS))
    {
      return this.EQ(value);
    }

    String errMsg = "Operator [" + trimmedOperator + "] is invalid for a string.";

    throw new InvalidComparisonOperator(errMsg, trimmedOperator, InvalidComparisonOperator.Enum.STRING);
  }
}
