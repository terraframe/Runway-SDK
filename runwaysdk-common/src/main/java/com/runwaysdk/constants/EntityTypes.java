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

public enum EntityTypes
{
  /**
   * Class that defines enumeration items for caching types.
   */
  ENTITY_CACHE_MASTER(Constants.METADATA_PACKAGE + ".EntityCacheMaster", "entity_cache_master", "28f858a7-c5f8-3b30-b7fb-b58357950058"),

  /**
   * Type that defines relationships.
   */
  PHONE_NUMBER(Constants.SYSTEM_PACKAGE + ".PhoneNumber", "phone_number", "a1d1fedd-943d-3366-8a39-766c50620100"),

  /**
   * Class that defines the symmetric encryption methods
   */
  SYMMETRIC_METHOD (Constants.METADATA_PACKAGE + ".SymmetricMethods",  "symmetric_methods", "dc7d6734-738c-32a8-827b-b5ca5cc60058"),

  /**
   * Class that defines the hash encryption methods
   */
  HASH_METHOD (Constants.METADATA_PACKAGE + ".HashMethods", "hash_methods", "e5af71db-08d8-3c65-9d9b-ee6cb5400058"),

  /**
   * Class that defines the localized metadata fields
   */
  METADATADISPLAYLABEL (Constants.METADATA_PACKAGE + ".MetadataDisplayLabel", "metadata_display_label", "9ed3ec54-7e79-3d17-9389-45cd7f2c0134"),
  
  /**
   * Class that defines the localized metadata fields
   */
  MD_LOCALIZABLE_MESSAGE (Constants.METADATA_PACKAGE + ".MdLocalizableMessage", "md_localizable_message", "9ed3ec54-7e79-3d17-9389-45cd7f2c0134");

  private String classType;
  private String tableName;
  private String oid;

  private EntityTypes(String classType, String tableName, String oid)
  {
   this.classType = classType;
   this.tableName = tableName;
   this.oid = oid;
  }

  public String getType()
  {
    return this.classType;
  }

  public String getTableName()
  {
    return this.tableName;
  }

  public String getOid()
  {
    return this.oid;
  }

}
