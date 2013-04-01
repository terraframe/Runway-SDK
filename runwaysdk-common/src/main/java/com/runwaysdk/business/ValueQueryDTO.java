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

import com.runwaysdk.constants.ValueQueryDTOInfo;



public class ValueQueryDTO extends ComponentQueryDTO
{

  public static final String CLASS = ValueQueryDTOInfo.CLASS;
  
  /**
   * 
   */
  private static final long serialVersionUID = 9119116382453948688L;
  
  /**
   * Constructor to set the query type.
   * 
   * @param type
   */
  public ValueQueryDTO(String groovyQuery)
  {
    super(ValueQueryDTO.class.getName());
    this.setGroovyQuery(groovyQuery);
  }

  
  /**
   * Returns the result set.
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ValueObjectDTO> getResultSet()
  {
    return (List<ValueObjectDTO>)super.getResultSet();
  }
  
}
