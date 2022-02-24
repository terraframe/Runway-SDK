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
package com.runwaysdk.dataaccess.attributes.value;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributePrimitiveDAOIF;

public abstract class MdAttributePrimitive_Q extends MdAttributeConcrete_Q implements MdAttributePrimitiveDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = -4126247804974833141L;

  /**
   * Used in value objects with attributes that contain values that are the result of functions, where the function result 
   * data type does not match the datatype of the column.
   * @param mdAttributeIF metadata that defines the column.
   */
  public MdAttributePrimitive_Q(MdAttributeConcreteDAOIF mdAttributeIF)
  {
    super(mdAttributeIF);
  }
  
  /**
   * Returns the concrete <code>MdAttributePrimitiveDAOIF</code> that this object references.
   * 
   * @return concrete <code>MdAttributePrimitiveDAOIF</code> that this object references.
   */
  private MdAttributePrimitiveDAOIF getMdAttributePrimitiveDAOIF()
  {
    return (MdAttributePrimitiveDAOIF)this.mdAttributeConcreteIF;
  }
  
  /**
   * True if the value of the attribute is calculated as a result of a user defined expression, false otherwise.
   * 
   * @return True if the value of the attribute is calculated as a result of a user defined expression, false otherwise.
   */
  public boolean isExpression()
  {
    return this.getMdAttributePrimitiveDAOIF().isExpression();
  }
  
  /**
   * Returns the user defined expression string. If none is defined then an empty string is returned.
   * 
   * @return user defined expression string. If none is defined then an empty string is returned.
   */
  public String getExpression()
  {
    return this.getMdAttributePrimitiveDAOIF().getExpression();
  }
}
