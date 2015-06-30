/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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

import com.runwaysdk.dataaccess.attributes.value.MdAttributeDouble_Q;

public class AVG extends AggregateFunction implements SelectableDouble
{
  /**
   *
   * @param attributePrimitive Attribute that is a parameter to the function.
   */
  protected AVG(EntityQuery entityQuery)
  {
    this(entityQuery, null, null);
  }

  /**
   *
   * @param attributePrimitive Attribute that is a parameter to the function.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected AVG(EntityQuery entityQuery, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(entityQuery, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   *
   * @param selectable
   */
  protected AVG(Selectable selectable)
  {
    this(selectable, null, null);
  }

  /**
   *
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected AVG(Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  protected void setMdAttributeIF(Selectable attribute)
  {
    if (attribute instanceof SelectableNumber)
    {
      this.setMdAttributeIF(new MdAttributeDouble_Q(attribute.getMdAttributeIF()));
    }
    else
    {
      super.setMdAttributeIF(attribute);
    }
  }

  /**
   * Returns the name of the database function.
   * @return name of the database function.
   */
  protected String getFunctionName()
  {
    return "AVG";
  }

  public AVG clone() throws CloneNotSupportedException
  {
    return (AVG)super.clone();
  }
}
