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
package com.runwaysdk.dataaccess;

import com.runwaysdk.dataaccess.metadata.DeleteContext;

/**
 * This is a marker interface to be implemented by all specialized subclasses of the standard DAO classes 
 * (e.g. BusinessDAO, RelationshipDAO, and StructDAO).  This interface is used by aspects.
 * @author nathan
 *
 */
public interface SpecializedDAOImplementationIF
{
  /**
   * Deletes the <code>EntityDAO</code>.
   *
   * <br/><b>Precondition:</b> isNew == false
   *
   * @param businessContext
   *            true if this is being called from a business context, false
   *            otherwise. If true then cascading deletes of other Entity
   *            objects will happen at the Business layer instead of the data
   *            access layer.
   * @param context TODO
   *
   */
  public void delete(DeleteContext context);
}
