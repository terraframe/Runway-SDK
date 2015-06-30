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

public interface MdDimensionInfo extends MetadataInfo
{
  /**
   * ID.
   */
  public static final String ID_VALUE  = "6nyuo8p2bb4now7x8owuvvhhekl28k7300000000000000000000000000000001";  
  
  /**
   * Class MetaData.
   */
  public static final String CLASS                       = Constants.METADATA_PACKAGE+".MdDimension";
  /**
   * Name of the dimension
   */
  public static final String NAME                        = "name";

  /**
   * Name of the attribute that stores the label of this metadata object.
   */
  public static final String DISPLAY_LABEL               = "displayLabel";
  
  /**
   * The key of a dimension is this string plus the dimension name.
   */
  public static final String KEY_ROOT                    = "Dimension.";
}
