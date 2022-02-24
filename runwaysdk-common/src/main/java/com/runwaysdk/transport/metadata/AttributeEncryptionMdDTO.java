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
package com.runwaysdk.transport.metadata;

/**
 * Describes the metadata for an attribute encryption.
 */
public abstract class AttributeEncryptionMdDTO extends AttributeMdDTO
{
  /**
   * 
   */
  private static final long serialVersionUID = -8381845005075908797L;

  /**
   * The encryption method that this hash uses
   */
  private String encryptionMethod;
  
  /**
   * Default constructor.
   */
  protected AttributeEncryptionMdDTO()
  {
    super();
  }
  
  /**
   * Gets the encryption method for this attribute.
   * 
   * @return String name of encryption method
   */
  public String getEncryptionMethod()
  {
    return encryptionMethod;
  }
  
  /**
   * Sets the encryption method for this attribute.
   *
   */
  protected void setEncryptionMethod(String encryptionMethod)
  {
    this.encryptionMethod = encryptionMethod;
  }
}
