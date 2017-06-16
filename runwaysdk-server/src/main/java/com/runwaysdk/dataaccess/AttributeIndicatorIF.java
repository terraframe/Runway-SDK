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

import java.util.List;

public interface AttributeIndicatorIF extends AttributeIF
{
  /**
   * Returns the unaggregated value of the indicator for this single object.
   * 
   * @return the unaggregated value of the indicator for this single object.
   */
  public Object evalNonAggregateValue();
  
  /**
   * Returns a List<{@link MdAttributePrimitiveDAOIF}> of attributes referenced by the indicator.
   * 
   * @return List<{@link MdAttributePrimitiveDAOIF}> of attributes referenced by the indicator.
   */
  public List<MdAttributePrimitiveDAOIF> getMdAttributePrimitives();
  
  /**
   * Returns the {@link MdAttributeIndicatorDAOIF} that defines the this attribute.
   * 
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   * 
   * @return {@link MdAttributeIndicatorDAOIF} that defines the this attribute
   */
  public MdAttributeIndicatorDAOIF getMdAttribute();
  
  /**
   * Returns the {@IndicatorElementDAOIF} definition metadata for the attribute.  
   * 
   * @return the {@IndicatorElementDAOIF} definition metadata for the attribute.  
   */
  public IndicatorElementDAOIF getIndicatorMD();
}
