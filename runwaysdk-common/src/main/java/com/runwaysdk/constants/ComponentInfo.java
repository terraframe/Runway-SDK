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

public interface ComponentInfo
{
  public static final String CLASS = Constants.SYSTEM_BUSINESS_PACKAGE+".Component";

  public static final String ID_VALUE          = "e4277b02-d7a5-3efe-995b-7154bc00003a";

  public static final String TABLE             = "component";

  /**
   * Name of the attribute that specifies the OID.
   */
  public static final String OID               = "oid";

  /**
   * Name root of the oid.  Denormalized for metadata loading performance.
   */
  public static final String ROOT_ID          = "rootId";

  /**
   * Name of the attribute that specifies the type.
   */
  public static final String TYPE             = "type";

  /**
   * Name of the attribute that specifies the key
   */
  public static final String KEY              = "keyName";
}
