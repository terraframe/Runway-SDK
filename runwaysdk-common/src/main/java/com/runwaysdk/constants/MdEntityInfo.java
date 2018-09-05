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

public interface MdEntityInfo extends MdClassInfo
{
  /**
   * Class MdEntity.
   */
  public static final String CLASS               = Constants.METADATA_PACKAGE + ".MdEntity";

  public static final String ID_VALUE            = "a7715ccf-fcf2-3805-91b1-1e5603ef0058";

  /**
   * Name of the attribute that stores the name of the the database table used
   * to store instances of the class defined by this metadata object.
   */
  public static final String TABLE_NAME          = "tableName";

  /**
   * Name of the attribute that specifies the caching algorithm used for this
   * type.
   */
  public static final String CACHE_ALGORITHM     = "cacheAlgorithm";

  /**
   * Name of the attribute that specifies the independent cache size.
   */
  public static final String CACHE_SIZE          = "cacheSize";

  /**
   * Name of the attribute that stores the query .java clob
   */
  public static final String QUERY_SOURCE        = "querySource";

  /**
   * Name of the attribute that stores the query .class blob
   */
  public static final String QUERY_CLASS         = "queryClass";

  /**
   * Name of the attribute that stores the query DTO .java clob
   */
  public static final String QUERY_DTO_SOURCE    = "queryDTOsource";

  /**
   * Name of the attribute that stores the query DTO .class blob
   */
  public static final String QUERY_DTO_CLASS     = "queryDTOclass";

  /**
   * Name of the attribute which denotes if the entity should enforce site
   * master constraints
   */
  public static final String ENFORCE_SITE_MASTER = "enforceSiteMaster";
  
  /**
   * If set to TRUE, then IDs that are generated are deterministic, false 
   * otherwise. Deterministic IDs are generated from a hash of the KeyName value.
   */
  public static final String HAS_DETERMINISTIC_IDS = "hasDeterministicIds";
  
}
