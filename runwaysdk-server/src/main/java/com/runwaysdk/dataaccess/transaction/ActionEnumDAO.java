/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
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
 ******************************************************************************/
package com.runwaysdk.dataaccess.transaction;


public enum ActionEnumDAO
{
  /**
   * Create Action
   */
  CREATE
  ("stitl8uzd1t2v6xgbhmiqyl2dcwy6i4nz62bgzkm0p8s4i08714kboipputg2pty"),

  /**
   *  Update Action
   */
  UPDATE
  ("xqcb4i3nlkdd4ipji5y5ifg308sozkbhz62bgzkm0p8s4i08714kboipputg2pty"),

  /**
   *  Delete Action
   */
  DELETE
  ("2dkmcf9bsv3mhwl8ij97rsap4nnaq236z62bgzkm0p8s4i08714kboipputg2pty"),

  /**
   *  No Operation Action
   */
  NO_OPERATION
  ("0oiyrpw65ysskspuxxml8gpoi1iuxiquz62bgzkm0p8s4i08714kboipputg2pty");

  private String id;

  private ActionEnumDAO(String id)
  {
    this.id = id;
  }

  public String getId()
  {
    return this.id;
  }

}
