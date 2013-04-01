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
 * Describes the metadata for an attribute number
 */
public abstract class AttributeNumberMdDTO extends AttributeMdDTO
{
  /**
   * 
   */
  private static final long serialVersionUID = -5825551444868469791L;

  /**
   * Denotes if this number rejects zero.
   */
  private boolean rejectZero;
  
  /**
   * Denotes if this number rejects negative values.
   */
  private boolean rejectNegative;
  
  /**
   * Denotes if this number rejects positive values.
   */
  private boolean rejectPositive;
  
  /**
   * Default constructor.
   */
  protected AttributeNumberMdDTO()
  {
    super();
    rejectZero = false;
    rejectNegative = false;
    rejectPositive = false;
  }
  
  /**
   * Checks if this attribute rejects zero as a value.
   * 
   * @return true if zero is rejected, false otherwise.
   */
  public boolean rejectZero()
  {
    return rejectZero;
  }
  
  /**
   * Checks if this attribute rejects negative numbers.
   * 
   * @return true if negative numbers are rejected, false otherwise.
   */
  public boolean rejectNegative()
  {
    return rejectNegative;
  }
  
  /**
   * Checks if this attribute rejects positive numbers.
   * 
   * @return true if positive numbers are rejected, false otherwise.
   */
  public boolean rejectPositive()
  {
    return rejectPositive;
  }
  
  /**
   * Sets if this attribute rejects zero.
   * 
   * @param rejectZero
   */
  protected void setRejectZero(boolean rejectZero)
  {
    this.rejectZero = rejectZero;
  }
  
  /**
   * Sets if this attribute rejects negative numbers.
   * 
   * @param rejectNegative
   */
  protected void setRejectNegative(boolean rejectNegative)
  {
    this.rejectNegative = rejectNegative;
  }
  
  /**
   * Sets if this attribute rejects positive numbers
   * 
   * @param rejectPositive
   */
  protected void setRejectPositive(boolean rejectPositive)
  {
    this.rejectPositive = rejectPositive;
  }

}
