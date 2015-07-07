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
package com.runwaysdk.query;

public class STRING_AGG extends AggregateFunction
{
  private String delimiter;
  
  /**
   * 
   * @param attributePrimitive
   *          Attribute that is a parameter to the function.
   */
  protected STRING_AGG(EntityQuery _entityQuery, String _delimiter)
  {
    this(_entityQuery, _delimiter, null, null);
  }

  /**
   * 
   * @param attributePrimitive
   *          Attribute that is a parameter to the function.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected STRING_AGG(EntityQuery _entityQuery, String _delimiter, String _userDefinedAlias, String _userDefinedDisplayLabel)
  {   
    super(_entityQuery, _userDefinedAlias, _userDefinedDisplayLabel);
    
    this.delimiter = _delimiter;
  }

  /**
   * 
   * @param selectable
   */
  protected STRING_AGG(Selectable _selectable, String _delimiter)
  {
    this(_selectable, _delimiter, null, null);
  }

  /**
   * 
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected STRING_AGG(Selectable _selectable, String _delimiter, String _userDefinedAlias, String _userDefinedDisplayLabel)
  {
    super(_selectable, _userDefinedAlias, _userDefinedDisplayLabel);
    
    this.delimiter = _delimiter;
  }

  /**
   * Returns the name of the database function.
   * 
   * @return name of the database function.
   */
  protected String getFunctionName()
  {
    return "STRING_AGG";
  }

  public STRING_AGG clone() throws CloneNotSupportedException
  {
    return (STRING_AGG) super.clone();
  }

  /**
   * Used for functions that need to append to the function.
   */
  protected String appendSQL()
  {
    return ", '"+this.delimiter+"'";
  }

}
