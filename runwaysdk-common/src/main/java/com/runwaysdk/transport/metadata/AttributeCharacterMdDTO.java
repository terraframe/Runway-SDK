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
package com.runwaysdk.transport.metadata;

/**
 * Describes metadata for an attribute character.
 */
public class AttributeCharacterMdDTO extends AttributeMdDTO
{
  /**
   * 
   */
  private static final long serialVersionUID = 4202771160969470252L;
  
  /**
   * The size of the character attribute.
   */
  private int size;
  
  /**
   * Default constructor.
   *
   */
  protected AttributeCharacterMdDTO()
  {
    super();
    size = 0;
  }
  
  /**
   * Returns the size of the character attribute.
   *
   */
  public int getSize()
  {
    return size;
  }
  
  /**
   * Sets the character size.
   * 
   * @param size
   * @return
   */
  protected void setSize(int size)
  {
    this.size = size;
  }

  @Override
  public Class<?> getJavaType()
  {
    return String.class;
  }

}
