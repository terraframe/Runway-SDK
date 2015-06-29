/**
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
 */
package com.runwaysdk.dataaccess;

public interface StructDAOIF extends EntityDAOIF
{
  /**
   * Returns a deep cloned-copy of this StructDAO.
   */
  public StructDAO getStructDAO();

  /**
   * Returns a MdStruct that defines this object's type.
   *
   * <br/><b>Precondition:</b> true <br/><b>Postcondition:</b> true
   *
   * @return a MdStruct that defines this object's type.
   */
  public MdStructDAOIF getMdStructDAO();

  /**
   * Returns a copy of the given StructDAO instance, with a new id and mastered at the current site.
   * The state of the object is new and has not been applied to the database.
   *
   * @return a copy of the given StructDAO instance
   */
  public StructDAO copy();
}
