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


public enum EntityCacheMaster
{
  /**
   * Cache all BusinessDAOs of a certain class.
   */
  CACHE_EVERYTHING                   ("0000000000000000000000000000023000000000000000000000000000000222", 1),

  /**
   * Cache no BusinessDAOs of a certain type.
   */
  CACHE_NOTHING                      ("0000000000000000000000000000023100000000000000000000000000000222", 2),

  /**
   * Cache BusinessDAOs that were the most recently accessed.
   */
  CACHE_MOST_RECENTLY_USED           ("0000000000000000000000000000023200000000000000000000000000000222", 3),

  /**
   * Cache BusinessDAO algorithm is hardcoded.
   */
  CACHE_HARDCODED                    ("0000000000000000000000000000024000000000000000000000000000000222", 4);


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
