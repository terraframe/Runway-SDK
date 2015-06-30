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

public interface MdIndexInfo extends MetadataInfo
{
  
  /**
   * MdIndex ID.
   */
  public static final String ID_VALUE                    = "NM20070418000000000000000000007200000000000000000000000000000001";
  /**
   * Class MdIndex.
   */
  public static final String CLASS                       = Constants.METADATA_PACKAGE+".MdIndex";
  
  /**
   * Name of the attribute that stores the label of this metadata object.
   */
  public static final String DISPLAY_LABEL               = "displayLabel";
  
  /**
   * MdEntity that the index is defined on.
   */
  public static final String MD_ENTITY                   = "mdEntity";
  /**
   * Indicates whether the index is a unique constraing.
   */
  public static final String UNIQUE                      = "uniqueValue";

  /**
   * Indicates whether the index has been applied to the database.
   */
  public static final String ACTIVE                      = "active";
  
  /**
   * Name of the database index.
   */
  public static final String INDEX_NAME                  = "indexName";
}
