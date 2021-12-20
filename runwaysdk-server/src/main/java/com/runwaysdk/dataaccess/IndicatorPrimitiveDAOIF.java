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
package com.runwaysdk.dataaccess;

public interface IndicatorPrimitiveDAOIF extends IndicatorElementDAOIF
{
  /**
   * Name of the table used to store instances of this class.
   */
  public static final String TABLE                   = "indicator_primitive";
  
  /**
   * Returns the {@link MdAttributePrimitiveDAOIF} that defines the primitive attribute used in the ratio equation.
   * 
   * @return the {@link MdAttributePrimitiveDAOIF} that defines the primitive attribute used in the ratio equation.
   */
  public MdAttributePrimitiveDAOIF getMdAttributePrimitive();
  
  /**
   * Returns the function enumeration item and assumes it is not null (precondition).
   * 
   * @return the function enumeration item and assumes it is not null (precondition).
   */
  public EnumerationItemDAOIF getAggregateFunction();
  
  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public IndicatorPrimitiveDAO getBusinessDAO();
}
