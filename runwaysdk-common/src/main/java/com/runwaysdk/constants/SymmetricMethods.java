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

public enum SymmetricMethods
{
  AES   ("0000000000000000000000000000062400000000000000000000000000000626", "AES/CBC/PKCS5Padding"),
  
  DES   ("0000000000000000000000000000063500000000000000000000000000000626", "DES/CBC/PKCS5Padding");
  
  /**
   * The name of the attribute that specifies what kind of symmetric 
   * method transformation to use.
   */
  public static final String TRANSFORMATION = "transformation";
  
  /**
   * The oid of the symmetric method.
   */
  private String oid;
  
  /**
   * The transformation of the symmetric method.
   */
  private String transformation;
  
  /**
   * Enum constructor.
   */
  private SymmetricMethods()
  {
    oid = null;
    transformation = null;
  }
  
  /**
   * Enum constructor.
   * 
   * @param oid
   * @param transformation
   */
  private SymmetricMethods(String oid, String transformation)
  {
    this();
    this.oid = oid;
    this.transformation = transformation;
  }
  
  /**
   * @return The oid of the method.
   */
  public String getOid()
  {
    return oid;
  }
  
  /**
   * @return the message digest of the method.
   */
  public String getTransformation()
  {
    return transformation;
  }
}
