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
package com.runwaysdk.business;

import java.util.List;

import com.runwaysdk.constants.ViewQueryDTOInfo;


public class ViewQueryDTO extends ClassQueryDTO
{
  /**
   * 
   */
  private static final long serialVersionUID = -5133997104094902850L;

  public static final String CLASS = ViewQueryDTOInfo.CLASS;
  
  /**
   * Denotes if the query type is an abstract type. The default is false.
   */
  protected boolean                     isAbstract;

  /**
   * Constructor to set the query type.
   * 
   * @param type
   */
  protected ViewQueryDTO(String type)
  {
    super(type);
  }

  /**
   * Sets if the query type is an abstract type.
   * 
   * @param isAbstract
   */
  public void setAbstract(boolean isAbstract)
  {
    this.isAbstract = isAbstract;
  }
  
  /**
   * Checks if this query type is an abstract type.
   * 
   * @return true if the query type is abstract, false otherwise.
   */
  public boolean isAbstract()
  {
    return isAbstract;
  }
  
  /**
   * Copies properties from the given componentQueryDTO into this one.
   * @param componentQueryDTO
   */
  public void copyProperties(ViewQueryDTO componentQueryDTO)
  {
    super.copyProperties(componentQueryDTO);
  }
  
  /**
   * Returns the result set.
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<? extends ViewDTO> getResultSet()
  {
    return (List<? extends ViewDTO>)super.getResultSet();
  }
}
