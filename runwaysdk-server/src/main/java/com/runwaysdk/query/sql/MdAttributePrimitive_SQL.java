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
package com.runwaysdk.query.sql;

import com.runwaysdk.dataaccess.MdAttributePrimitiveDAOIF;
import com.runwaysdk.query.ValueQuery;

public abstract class MdAttributePrimitive_SQL extends MdAttributeConcrete_SQL implements MdAttributePrimitiveDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = -2491931300650593549L;

  /**
   * Used to spoof metadata for writing SQL directly to the {@link ValueQuery} API.
   *
   * @param attributeName
   * @param displayLabel
   */
  public MdAttributePrimitive_SQL(String attributeName, String displayLabel)
  {
    super(attributeName, displayLabel);
  }
  
  /**
   * True if the value of the attribute is calculated as a result of a user defined expression, false otherwise.
   * 
   * @return True if the value of the attribute is calculated as a result of a user defined expression, false otherwise.
   */
  public boolean isExpression()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }
  
  /**
   * Returns the user defined expression string. If none is defined then an empty string is returned.
   * 
   * @return user defined expression string. If none is defined then an empty string is returned.
   */
  public String getExpression()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }
}
