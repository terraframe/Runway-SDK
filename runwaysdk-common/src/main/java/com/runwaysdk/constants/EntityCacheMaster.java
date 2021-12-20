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


public enum EntityCacheMaster
{
  /**
   * Cache all BusinessDAOs of a certain class.
   */
  CACHE_EVERYTHING                   ("bfc8ffc0-e021-3746-abaf-6fba7700004d", 1),

  /**
   * Cache no BusinessDAOs of a certain type.
   */
  CACHE_NOTHING                      ("53f84de0-954a-3d48-a57f-29b95c00004d", 2),

  /**
   * Cache BusinessDAOs that were the most recently accessed.
   */
  CACHE_MOST_RECENTLY_USED           ("8ea897b9-393d-3c45-87e7-7d998000004d", 3),

  /**
   * Cache BusinessDAO algorithm is hardcoded.
   */
  CACHE_HARDCODED                    ("5bce2da7-3746-301c-8c10-4f420100004d", 4);


  /**
   * Attribute that indicates the cache level.
   */
  public static final String CACHE_CODE    = "cacheCode";

  /**
   * DisplayLabel
   */
  public static final String DISPLAY_LABEL = EnumerationMasterInfo.DISPLAY_LABEL;

  /**
   * Description of the cache level.
   */
  public static final String DESCRIPTION   = "description";


  private String oid;
  private int cacheCode;

  private EntityCacheMaster(String oid, int cacheCode)
  {
   this.oid = oid;
   this.cacheCode = cacheCode;
  }

  public String getOid()
  {
    return this.oid;
  }

  public int getCacheCode()
  {
    return this.cacheCode;
  }
}
