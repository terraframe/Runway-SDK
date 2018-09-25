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
package com.runwaysdk.dataaccess.transaction;


public enum ActionEnumDAO
{
  /**
   * Create Action
   */
  CREATE
  ("66bc0c43-3ba0-3588-ae76-06df5a000059"),

  /**
   *  Update Action
   */
  UPDATE
  ("57e6a139-0e54-3c3b-876f-eb3746000059"),

  /**
   *  Delete Action
   */
  DELETE
  ("c5ea7e8c-c21c-34c5-b84e-c69dd6000059"),

  /**
   *  No Operation Action
   */
  NO_OPERATION
  ("dd749023-e595-3a0f-a0a4-ac378f000059");

  private String oid;

  private ActionEnumDAO(String oid)
  {
    this.oid = oid;
  }

  public String getOid()
  {
    return this.oid;
  }

}
