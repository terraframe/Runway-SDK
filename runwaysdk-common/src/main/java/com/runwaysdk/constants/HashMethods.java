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
  MD5   ("0000000000000000000000000000062800000000000000000000000000000640", "MD5"),
  
  SHA   ("0000000000000000000000000000063400000000000000000000000000000640", "SHA");
  
  /**
   * The name of the attribute that specifies what kind of hash 
   * method message digest to use.
   */
  public static final String MESSAGE_DIGEST = "messageDigest";
  
  /**
   * The id of the symmetric method.
   */
  private String id;
  
  /**
   * The message digest of the symmetric method.
   */
  private String messageDigest;
  
  /**
   * Enum constructor.
   */
  private HashMethods()
  {
    id = null;
    messageDigest = null;
  }
  
  /**
   * Enum constructor.
   * 
   * @param id
   * @param messageDigest
   */
  private HashMethods(String id, String messageDigest)
  {
    this();
    this.id = id;
    this.messageDigest = messageDigest;
  }
  
  /**
   * @return The id of the method.
   */
  public String getId()
  {
    return id;
  }
  
  /**
   * @return the message digest of the method.
   */
  public String getMessageDigest()
  {
    return messageDigest;
  }
}
