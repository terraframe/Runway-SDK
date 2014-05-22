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
 * Describes metadata for an attribute dec
 */
public abstract class AttributeDecMdDTO extends AttributeNumberMdDTO
{
  /**
   * 
   */
  private static final long serialVersionUID = -5119144497577127549L;

  /**
   * The total length of the attribute dec
   */
  private int totalLength;
  
  /**
   * The decimal length of the attribute dec 
   */
  private int decimalLength;
  
  protected AttributeDecMdDTO()
  {
    super();
  }
  
  /**
   * Returns the total length of the attribute.
   * 
   * @return The total length of the attribute
   */
  public int getTotalLength()
  {
    return totalLength;
  }
  
  /**
   * Returns the decimal length of the attribute.
   * 
   * @return The decimal length of the attribute
   */
  public int getDecimalLength()
  {
    return decimalLength;
  }
  
  /**
   * Sets the total length of this attribute.
   * 
   * @param totalLength
   */
  protected void setTotalLength(int totalLength)
  {
    this.totalLength = totalLength;
  }
  
  /**
   * Sets the decimal length of this attribute.
   * 
   * @param decimalLength
   */
  protected void setDecimalLength(int decimalLength)
  {
    this.decimalLength = decimalLength;
  }
}
