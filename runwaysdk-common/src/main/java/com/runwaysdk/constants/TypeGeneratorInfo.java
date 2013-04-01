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
package com.runwaysdk.constants;



public interface TypeGeneratorInfo
{
  /**
   * The suffix added to all class names for DTO generated classes
   */
  public static final String DTO_SUFFIX = "DTO";
  
  /**
   * The suffix added to all base class names for generated classes
   */
  public static final String BASE_SUFFIX = "Base";
  
  public static final String DTO_BASE_SUFFIX = DTO_SUFFIX + BASE_SUFFIX;

  public static final String QUERY_DTO_SUFFIX = "QueryDTO";
  
  /**
   * The suffix for the type-safe attribute enumeration getter that returns
   * the names of the currently stored enumerated items.
   */
  public static final String ATTRIBUTE_ENUMERATION_ENUM_NAMES_SUFFIX = "EnumNames";

  /**
   * Suffix added to hardcoded classes
   */
  public static final String SYSTEM_SUFFIX = "System";

  /**
   * Suffix added to hardcoded classes
   */
  public static final String SYSTEM_BASE_SUFFIX = SYSTEM_SUFFIX+BASE_SUFFIX;

  public static final String DTO_SYSTEM_BASE_SUFFIX = SYSTEM_SUFFIX + DTO_SUFFIX + BASE_SUFFIX;
  
  public static final String DTO_SYSTEM_SUFFIX = SYSTEM_SUFFIX + DTO_SUFFIX;
  
}
