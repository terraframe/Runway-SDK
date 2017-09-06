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

import com.runwaysdk.dataaccess.IndicatorElementDAO.IndicatorVisitor;

public interface IndicatorElementDAOIF extends BusinessDAOIF
{
  /**
   * Name of the table used to store instances of this class.
   */
  public static final String TABLE = "indicator_element";

  /**
   * Returns a localized readable label of the ratio.
   * 
   * @return a localized readable label of the ratio.
   */
  public String getLocalizedLabel();
  
  /**
   * Returns the java type that is the return type of the ration equation.
   * 
   * @return the java type that is the return type of the ration equation.
   */
  public String javaType();
  
  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public IndicatorElementDAO getBusinessDAO();

  /**
   * A visitor that visits the indicator composite object structure.
   * 
   * @param indicatorVisitor
   */
  public void accept(IndicatorVisitor _indicatorVisitor);

  /**
   * Returns the unaggregated value of the indicator for the given {@link ComponentDAOIF}.
   * 
   * @param _mdAttributeIndicator the attribute that defines the indicator.
   * @param _componentDAOIF the component with the attributes to evaluate the indicator.
   * 
   * @return the unaggregated value of the indicator for the given {@link ComponentDAOIF}.
   */
  public Object evalNonAggregateValue(MdAttributeIndicatorDAOIF _mdAttributeIndicator, ComponentDAOIF _componentDAOIF);

  public boolean isPercentage();
}
