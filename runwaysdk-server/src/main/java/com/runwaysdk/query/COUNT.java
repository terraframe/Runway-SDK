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

import com.runwaysdk.dataaccess.attributes.value.MdAttributeInteger_Q;

public class COUNT extends AggregateFunction implements SelectableInteger
{
  /**
   *
   * @param selectable Attribute that is a parameter to the function.
   */
  protected COUNT(EntityQuery entityQuery)
  {
    this(entityQuery, null, null);
  }

  /**
   *
   * @param selectable Attribute that is a parameter to the function.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected COUNT(EntityQuery entityQuery, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(entityQuery, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   *
   * @param selectable
   */
  protected COUNT(Selectable selectable)
  {
    this(selectable, null, null);
  }

  /**
   *
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected COUNT(Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  protected void setMdAttributeIF(Selectable attributePrimitive)
  {
    this.setMdAttributeIF(new MdAttributeInteger_Q(attributePrimitive.getMdAttributeIF()));
  }

  /**
   * Returns the name of the database function.
   * @return name of the database function.
   */
  protected String getFunctionName()
  {
    return "COUNT";
  }

  /**
   * Equals.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition EQ(SelectableInteger selectable)
  {
    return QueryUtil.EQ(this, selectable);
  }

  /**
   * Not Equals.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition NE(SelectableInteger selectable)
  {
    return QueryUtil.NE(this, selectable);
  }

  /**
   * Greater Than.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition GT(SelectableInteger selectable)
  {
    return QueryUtil.GT(this, selectable);
  }

  /**
   * Greater Than or Equals.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition GE(SelectableInteger selectable)
  {
    return QueryUtil.GE(this, selectable);
  }

  /**
   * Less Than.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition LT(SelectableInteger selectable)
  {
    return QueryUtil.LT(this, selectable);
  }

  /**
   * Less Than or Equal.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition LE(SelectableInteger selectable)
  {
    return QueryUtil.LE(this, selectable);
  }
}
