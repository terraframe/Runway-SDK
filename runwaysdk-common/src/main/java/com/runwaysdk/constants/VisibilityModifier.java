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

public enum VisibilityModifier 
{
  PUBLIC    ("public", "78a04c02-65e4-3aef-b74c-318f28360113"),
  
  PROTECTED ("protected", "dfb3d269-f82b-37a6-8a05-a6a72b980113");
  
  private String javaModifier;
  
  private String oid;
  
  private VisibilityModifier(String javaModifier, String oid)
  {
    this.javaModifier = javaModifier;
    this.oid = oid;
  }
  
  public String getJavaModifier()
  {
    return this.javaModifier;
  }
  
  public String getOid()
  {
    return this.oid;
  } 

}
