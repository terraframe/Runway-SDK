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
package com.runwaysdk.dataaccess;


public interface MdAttributePrimitiveDAOIF extends MdAttributeConcreteDAOIF
{
  /**
   * Name of the table used to store instances of this class.
   */
  public static final String TABLE   = "md_attribute_primitive";
  
  /**
   * True if the value of the attribute is calculated as a result of a user defined expression, false otherwise.
   * 
   * @return True if the value of the attribute is calculated as a result of a user defined expression, false otherwise.
   */
  public boolean isExpression();
  
  /**
   * Returns the user defined expression string. If none is defined then an empty string is returned.
   * 
   * @return user defined expression string. If none is defined then an empty string is returned.
   */
  public String getExpression();
  
  /**
   * Returns the java class object for the attribute type.
   * 
   * @return the java class object for the attribute type.
   */
  public Class<?> javaClass();
}
