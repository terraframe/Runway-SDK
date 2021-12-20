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


public interface MdAttributeEnumerationInfo extends MdAttributeConcreteInfo
{

  /**
   * Class.
   */
  public static final String CACHE_COLUMN_DATATYPE             = MdAttributeClobInfo.CLASS;
  /**
   * Class.
   */
  public static final String CLASS                             = Constants.METADATA_PACKAGE+".MdAttributeEnumeration";
  /**
   * Name of the attribute that references the enumeration definition
   * that this attribute uses.
   */
  public static final String MD_ENUMERATION                    = "mdEnumeration";
  /**
   * Name of the attribute that indicates if this attribute can select more than one
   *  concurrent value from the enumeration list.
   */
  public static final String SELECT_MULTIPLE                   = "selectMultiple";

  /**
   * Name of the attribute that defines the default value for instances of this attribute.
   */
  public static final String DEFAULT_VALUE                     = MdAttributeConcreteInfo.DEFAULT_VALUE;
  /**
   * Name of the attribute that references the enumeration definition
   * that this attribute uses.
   */
  public static final String CACHE_COLUMN_NAME                 = "cacheColumnName";
}
