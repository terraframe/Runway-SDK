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
package com.runwaysdk.business;

import java.util.List;


/**
 * A class to query Entity instances.
 */
public abstract class EntityQueryDTO extends ClassQueryDTO
{  
  /**
   * 
   */
  private static final long serialVersionUID = 6359632816288510262L;

  /**
   * Constructor to set the query type.
   * 
   * @param type
   */
  protected EntityQueryDTO(String type)
  {
    super(type);
  }

  /**
   * Copies properties from the given componentQueryDTO into this one.
   * @param componentQueryDTO
   */
  public void copyProperties(EntityQueryDTO componentQueryDTO)
  {
    super.copyProperties(componentQueryDTO);
  }
  
  /**
   * Returns the result set.
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<? extends EntityDTO> getResultSet()
  {
    return (List<? extends EntityDTO>)super.getResultSet();
  }

}
