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


public interface MdTypeInfo extends MetadataInfo
{
  /**
   * Class MdType.
   */
  public static final String CLASS   = Constants.METADATA_PACKAGE+".MdType";

  /**
   * ID.
   */
  public static final String ID_VALUE  = "0000000000000000000000000000018000000000000000000000000000000001";  
  
  /**
   * Name of the attribute that stores the label of this metadata object.
   */
  public static final String DISPLAY_LABEL        = "displayLabel";
  
  /**
   * Name of the attribute that stores the name of the relationship that is defined.
   */
  public static final String NAME                 = "typeName";
  
  /**
   * Name of the attribute that stores the name of the package of the relationship that is defined.
   */
  public static final String PACKAGE              = "packageName";

  /**
   * Indicates if the given type is exported or not.
   */
  public static final String EXPORTED             = "exported";
  
  /**
   * Name of the attribute that stores the base .class blob.
   */
  public static final String BASE_CLASS           = "baseClass";

  /**
   * Name of the attribute that stores the base .java text.
   */
  public static final String BASE_SOURCE          = "baseSource";
  
  /**
   * Name of the attribute that stores the dto .class blob 
   */
  public static final String DTO_BASE_CLASS       = "dtoClass";
  
  /**
   * Name of the attribute that stores the dto .java text
   */
  public static final String DTO_BASE_SOURCE      = "dtoSource";
  
}
