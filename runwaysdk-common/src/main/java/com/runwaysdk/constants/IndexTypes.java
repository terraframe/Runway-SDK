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
package com.runwaysdk.constants;

/**
 * An enumeration of the possible index types for an attribute.
 */
public enum IndexTypes
{
  /**
   * Index type of no index.
   */
  NO_INDEX                              ("9a7f73ee-81a9-32e9-884e-c4be61000055"),
  
  /**
   * Index type of non unique index.
   */
  NON_UNIQUE_INDEX                      ("cb6c78c1-3d48-333a-af1e-1885ca000055"),
  
  /**
   * Index type of unique index.
   */
  UNIQUE_INDEX                          ("72b5580c-2a6f-3250-9435-1be1f2000055");

  
  /**
   * Index type of unique group index.
   */
  
  private String oid = null;
  
  private IndexTypes(String oid)
  {
   this.oid = oid;
  }
  
  public String getOid()
  {
    return this.oid;  
  }
}
