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
package com.runwaysdk.constants;


public enum MdEnumerationTypes 
{
  /**
   * Cache algorithm options.
   */
  OBJECT_TYPE_CACHE          (Constants.METADATA_PACKAGE+".OBJECT_TYPE_CACHE",       "object_type_cache"),

  /**
   * Attribute database index options.
   */
  MD_ATTRIBUTE_INDICES       (Constants.SYSTEM_PACKAGE+".MdAttributeIndices",        "md_attribute_indices"),
  
  /**
   * RBAC permission options
   */
  ALL_OPERATIONS              (Constants.SYSTEM_PACKAGE+".AllOperations",            "all_operations");
  
  private String classType;
  private String tableName;
  
  private MdEnumerationTypes(String classType, String tableName)
  {
   this.classType = classType;
   this.tableName = tableName;
  }
  
  public String getType()
  {
    return this.classType;
  }
  
  public String getTableName()
  {
    return this.tableName;
  }
}
