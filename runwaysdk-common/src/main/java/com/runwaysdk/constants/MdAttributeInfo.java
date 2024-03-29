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

public interface MdAttributeInfo extends MetadataInfo
{
  /**
   * Interface {@link MdAttributeInfo}.
   */
  public static final String CLASS_NAME        = "MdAttribute";
  
  public static final String CLASS             = Constants.METADATA_PACKAGE + "." + CLASS_NAME;

  /**
   * OID.
   */
  public static final String ID_VALUE          = "c07c55c2-2f17-374a-8f57-710a2000003a";

  public static final String GENERATE_ACCESSOR = "generateAccessor";
}
