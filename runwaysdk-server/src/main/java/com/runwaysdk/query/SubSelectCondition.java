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
package com.runwaysdk.query;

public abstract class SubSelectCondition extends AttributeCondition
{
  /**
   * True if the comparison should be case insensitive, false otherwise.
   */
  protected boolean ignoreCase;

  public SubSelectCondition(Selectable attributeLeft)
  {
    super(attributeLeft);
    this.ignoreCase = false;
  }

  public SubSelectCondition(Selectable attributeLeft, boolean ignoreCase)
  {
    super(attributeLeft);
    this.ignoreCase = ignoreCase;
  }


  /**
   * Returns string with a value that is either "IN" or "NOT IN", depending on what is appropriate for the condition.
   *
   * @return string with a value that is either "IN" or "NOT IN", depending on what is appropriate for the condition.
   */
  protected String getSubSelectOperatorSymbol()
  {
    return "IN";
  }

  /**
   * Visitor to traverse the query object structure.
   * @param visitor
   */
  public void accept(Visitor visitor)
  {
    super.accept(visitor);
  }

  /**
   *
   * @param attribute1
   * @param attribute2
   */
  protected static void validateSubSelect(Selectable attribute1, Selectable attribute2)
  {
    if (attribute1.getRootQuery() == attribute2.getRootQuery())
    {
      String error = "Attributes [" + attribute1._getAttributeName() + "] and [" + attribute2._getAttributeName()
          + "] are both from the same query object.  Subselect criteria can only select"
          + " attributes from a different query object.";
      throw new QueryException(error);
    }
    else if (attribute1.getRootQuery().queryFactory !=
      attribute2.getRootQuery().queryFactory)
    {
      String error = "Attributes [" + attribute1._getAttributeName() + "] and [" + attribute2._getAttributeName()
          + "] are from query objects from different factories.  Subselect criteria can "
          + "only compare attributes from query objects produced by the same query factory.";
      throw new QueryException(error);
    }
  }

}
