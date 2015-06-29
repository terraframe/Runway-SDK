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

/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.query;

public class RANK extends AggregateFunction
{
  /**
   * 
   */
  protected RANK(EntityQuery entityQuery)
  {
    this(entityQuery, null, null);
  }

  /**
   *
   * @param entityQuery 
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected RANK(EntityQuery entityQuery, String userDefinedAlias)
  {
    super(entityQuery, userDefinedAlias, null);
  }
  
  /**
   *
   * @param entityQuery 
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected RANK(EntityQuery entityQuery, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(entityQuery, userDefinedAlias, userDefinedDisplayLabel);
  }
  
  /**
   *
   * @param selectableSpoof 
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected RANK(SelectableSpoof selectableSpoof, String userDefinedAlias)
  {
    super(selectableSpoof, userDefinedAlias, null);
  }
  
  /**
   *
   * @param selectableSpoof 
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected RANK(SelectableSpoof selectableSpoof)
  {
    super(selectableSpoof, null, null);
  }
 
  /**
   * Returns the name of the database function.
   * 
   * @return name of the database function.
   */
  protected String getFunctionName()
  {
    return "RANK";
  }

  public RANK clone() throws CloneNotSupportedException
  {
    return (RANK) super.clone();
  }

  /**
   *
   */
  public String getSQL()
  {
    String sql = this.getFunctionName()+"()";

    sql = getOverClause(sql);
     
    return sql;
  }
  
}
