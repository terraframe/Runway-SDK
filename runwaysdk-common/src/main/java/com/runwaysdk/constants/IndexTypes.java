/**
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
 */
package com.runwaysdk.constants;

/**
 * An enumeration of the possible index types for an attribute.
 */
public enum IndexTypes
{
  /**
   * Index type of no index.
   */
  NO_INDEX                              ("0000000000000000000000000000040100000000000000000000000000000403"),
  
  /**
   * Index type of non unique index.
   */
  NON_UNIQUE_INDEX                      ("0000000000000000000000000000040000000000000000000000000000000403"),
  
  /**
   * Index type of unique index.
   */
  UNIQUE_INDEX                          ("0000000000000000000000000000040900000000000000000000000000000403");

  
  /**
   * Index type of unique group index.
   */
  
  private String id = null;
  
  private IndexTypes(String id)
  {
   this.id = id;
  }
  
  public String getId()
  {
    return this.id;  
  }
}
