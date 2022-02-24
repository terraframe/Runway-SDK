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
/*
 * Created on Aug 8, 2005
 *
 */
package com.runwaysdk.dataaccess;

/**
 * @author nathan
 *
 * TODO To change the template for this generated comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface AttributeBooleanIF extends AttributeNumericalIF
{
  /**
   * Tests the string value of the bolean, and returns a primitive
   * <code><b>boolean</b></code>.  Equivalent to <code>!value</code>.
   * 
   * @return <code><b>true</b></code> if the boolean attribute is MdAttributeBooleanIF.FALSE
   */
  public boolean isFalse();
  
  /**
   * Tests the string value of the bolean, and returns a primitive
   * <code><b>boolean</b></code>.
   * 
   * @return <code><b>true</b></code> if the boolean attribute is MdAttributeBooleanIF.TRUE
   */
  public boolean isTrue();
  
  /**
   * Returns the boolean value of this attribute.
   * @return boolean value of this attribute.
   */
  public boolean getBooleanValue();
  
  /**
   *Returns 1 if the given attributeBoolean is true, 0.
   *
   * @return 1 if the given attributeBoolean is true, 0.
   */
  public int getBooleanValueInt();
  
  /**
   * Returns the Java primitive type of the value.
   * 
   * @return the Java primitive type of the value.
   */
  public Boolean getTypeSafeValue();
}
