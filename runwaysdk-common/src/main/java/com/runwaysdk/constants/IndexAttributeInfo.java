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

/**
 * Relationship that defines a database index constraint on an entity.
 */
public interface IndexAttributeInfo extends RelationshipInfo
{
  public static final String CLASS = Constants.METADATA_PACKAGE+"."+"IndexAttribute";

  public static final String ID_VALUE          = "ba7e4fd7-3497-35ae-8c0d-c2f214000067";

  public static final String TABLE             = "index_attribute";

  public static final String INDEX_ORDER       = "indexOrder";

}
