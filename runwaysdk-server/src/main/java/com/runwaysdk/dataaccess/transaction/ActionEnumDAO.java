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
package com.runwaysdk.dataaccess.transaction;


public enum ActionEnumDAO
{
  /**
   * Create Action
   */
  CREATE
  ("0a51155b-8f3e-3f56-a420-e92ddd000089"),

  /**
   *  Update Action
   */
  UPDATE
  ("2d8e3c76-984b-3a5e-90d6-4785e0000089"),

  /**
   *  Delete Action
   */
  DELETE
  ("1bab1368-187a-3fd5-97ea-6c9bc3000089"),

  /**
   *  No Operation Action
   */
  NO_OPERATION
  ("1f7287d2-806e-3a4b-9438-fc1ee6000089");

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
