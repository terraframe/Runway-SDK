/**
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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
import com.runwaysdk.dataaccess.database.Database;

public class VARIANCE extends AggregateFunction implements SelectableDouble
{
  /**
   *
   * @param selectable Attribute that is a parameter to the function.
   */
  protected VARIANCE(EntityQuery entityQuery)
  {
    this(entityQuery, null, null);
  }

  /**
   *
   * @param selectable Attribute that is a parameter to the function.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected VARIANCE(EntityQuery entityQuery, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(entityQuery, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   *
   * @param selectable
   */
  protected VARIANCE(Selectable selectable)
  {
    this(selectable, null, null);
  }

  /**
   *
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected VARIANCE(Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
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
    return Database.varianceFunction();
  }

}
