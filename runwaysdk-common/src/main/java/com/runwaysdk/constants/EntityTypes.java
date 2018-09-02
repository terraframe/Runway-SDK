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
  ENTITY_CACHE_MASTER(Constants.METADATA_PACKAGE + ".EntityCacheMaster", "entity_cache_master", "000000000000000000000000000002220058"),

  /**
   * Type that defines relationships.
   */
  PHONE_NUMBER(Constants.SYSTEM_PACKAGE + ".PhoneNumber", "phone_number", "0000000000000000000000000000031800000000000000000000000000000979"),

  /**
   * Class that defines the symmetric encryption methods
   */
  SYMMETRIC_METHOD (Constants.METADATA_PACKAGE + ".SymmetricMethods",  "symmetric_methods", "000000000000000000000000000006260058"),

  /**
   * Class that defines the hash encryption methods
   */
  HASH_METHOD (Constants.METADATA_PACKAGE + ".HashMethods", "hash_methods", "000000000000000000000000000006400058"),

  /**
   * Class that defines the localized metadata fields
   */
  METADATADISPLAYLABEL (Constants.METADATA_PACKAGE + ".MetadataDisplayLabel", "metadata_display_label", "NM2009041200000000000000000000300000000000000000000MdLocalStruct"),
  
  /**
   * Class that defines the localized metadata fields
   */
  MD_LOCALIZABLE_MESSAGE (Constants.METADATA_PACKAGE + ".MdLocalizableMessage", "md_localizable_message", "NM2009041200000000000000000000300000000000000000000MdLocalStruct");

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
