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

public enum AssociationType {
  /**
   * Index type of no index.
   */
  RELATIONSHIP("ac0ed8ec-7d6a-3181-9512-d0864ed30212"),

  /**
   * Index type of non unique index.
   */
  TREE("ddad29c9-dfc8-3cad-8140-e43355660212"),

  /**
   * Index type of unique index.
   */
  GRAPH("8f9cd2b8-8554-38db-b72d-47a5a5ff0212");

  /**
   * Index type of unique group index.
   */

  private String oid = null;

  private AssociationType(String oid)
  {
    this.oid = oid;
  }

  public String getOid()
  {
    return this.oid;
  }
}
