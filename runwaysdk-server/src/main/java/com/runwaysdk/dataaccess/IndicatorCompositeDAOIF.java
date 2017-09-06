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
package com.runwaysdk.dataaccess;


public interface IndicatorCompositeDAOIF extends IndicatorElementDAOIF
{
  /**
   * Name of the table used to store instances of this class.
   */
  public static final String TABLE                   = "indicator";
  
  
  public static final String OPERATOR_COLUMN         = "math_operator";
  
  /**
   * Returns the {@link IndicatorElementDAOIF} of the left hand operand.
   * 
   * @return the {@link IndicatorElementDAOIF} of the left hand operand.
   */
  public IndicatorElementDAOIF getLeftOperand();
  
  /**
   * Returns the operator enumeration and assumes it is not null (precondition).
   * 
   * @return the operator enumeration and assumes it is not null (precondition).
   */
  public EnumerationItemDAOIF getOperator();
  
  /**
   * Returns the {@link IndicatorElementDAOIF} of the left hand operand.
   * 
   * @return the {@link IndicatorElementDAOIF} of the left hand operand.
   */
  public IndicatorElementDAOIF getRightOperand();
  
  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public IndicatorCompositeDAO getBusinessDAO();
  
}
