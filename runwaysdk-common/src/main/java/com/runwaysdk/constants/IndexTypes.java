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
package com.runwaysdk.constants;

/**
 * An enumeration of the possible index types for an attribute.
 */
public enum IndexTypes
{
  /**
   * Index type of no index.
   */
  NO_INDEX                              ("328b6a77-c37c-3e8d-b35c-a15c0e210085"),
  
  /**
   * Index type of non unique index.
   */
  NON_UNIQUE_INDEX                      ("9ebbb1e6-7746-321d-97bd-344a323e0085"),
  
  /**
   * Index type of unique index.
   */
  UNIQUE_INDEX                          ("44284f74-6d1b-3921-87b1-32d866a30085");

  
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
