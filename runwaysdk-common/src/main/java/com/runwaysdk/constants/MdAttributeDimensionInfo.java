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

public interface MdAttributeDimensionInfo extends MetadataInfo
{
  /**
   * OID.
   */
  public static final String ID_VALUE              = "a914a15f-be1c-39f9-a87e-99e65a00003a";

  /**
   * Class MetaData.
   */
  public static final String CLASS                 = Constants.METADATA_PACKAGE + ".MdAttributeDimension";

  public static final String REQUIRED              = "required";
  
  public static final String DEFAULT_VALUE         = "defaultValue";
  
  public static final String DEFINING_MD_ATTRIBUTE = "definingMdAttribute";

  public static final String DEFINING_MD_DIMENSION = "definingMdDimension";

}
