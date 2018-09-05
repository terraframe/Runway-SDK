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

public enum HashMethods
{
  MD5   ("188cd5ce-a5c1-3ecd-a76c-e40416fb0093", "MD5"),
  
  SHA   ("07019643-5238-39ba-bc66-fc559ec40093", "SHA");
  
  /**
   * The name of the attribute that specifies what kind of hash 
   * method message digest to use.
   */
  public static final String MESSAGE_DIGEST = "messageDigest";
  
  /**
   * The oid of the symmetric method.
   */
  private String oid;
  
  /**
   * The message digest of the symmetric method.
   */
  private String messageDigest;
  
  /**
   * Enum constructor.
   */
  private HashMethods()
  {
    oid = null;
    messageDigest = null;
  }
  
  /**
   * Enum constructor.
   * 
   * @param oid
   * @param messageDigest
   */
  private HashMethods(String oid, String messageDigest)
  {
    this();
    this.oid = oid;
    this.messageDigest = messageDigest;
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
  public String getMessageDigest()
  {
    return messageDigest;
  }
}
