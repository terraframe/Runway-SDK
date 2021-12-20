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
package com.runwaysdk.business;

public abstract class ElementQueryDTO extends EntityQueryDTO
{

  /**
   * 
   */
  private static final long serialVersionUID = -4968674492962422390L;

  /**
   * Denotes if the query type is an abstract type. The default is false.
   */
  protected boolean                     isAbstract;
  
  /**
   * Constructor to set the query type.
   * 
   * @param type
   */
  protected ElementQueryDTO(String type)
  {
    super(type);

    isAbstract = false;
  }
  
  /**
   * Copies properties from the given componentQueryDTO into this one.
   * @param componentQueryDTO
   */
  public void copyProperties(ElementQueryDTO componentQueryDTO)
  {
    super.copyProperties(componentQueryDTO);
    this.isAbstract = componentQueryDTO.isAbstract;
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
}
