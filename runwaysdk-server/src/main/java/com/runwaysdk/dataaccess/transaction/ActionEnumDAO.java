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
  ("a5706271-b915-30e0-bfd4-ac03ed4e0137"),

  /**
   *  Update Action
   */
  UPDATE
  ("9865578f-e05b-3362-8b3b-64676f070137"),

  /**
   *  Delete Action
   */
  DELETE
  ("ffdb5810-652f-3010-95c6-187290b80137"),

  /**
   *  No Operation Action
   */
  NO_OPERATION
  ("be2184e3-1d2e-36fa-b105-1d7bf59a0137");

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
