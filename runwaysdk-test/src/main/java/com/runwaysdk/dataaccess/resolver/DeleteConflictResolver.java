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
package com.runwaysdk.dataaccess.resolver;

import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.dataaccess.EntityDAO;

public class DeleteConflictResolver extends ConflictAdapter
{
  private UserDAO reference;
  
  public DeleteConflictResolver(UserDAO reference)
  {
    this.reference = reference;
  }
  
  @Override
  public void resolve(final ImportConflict conflict)
  {
    new SynchronusExecutor(new Runnable()
    {
      @Override
      public void run()
      {
        EntityDAO entityDAO = conflict.getEntityDAO();
        EntityDAO referenceDAO = strategy.get(reference.getId()).getEntityDAO();

        strategy.delete(referenceDAO);
        strategy.delete(entityDAO);
      }
    }).start();
  }
}
