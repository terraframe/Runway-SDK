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
package com.runwaysdk.dataaccess.resolver;

import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;

public class RoleExportBuilder extends ExportBuilder<Void>
{
  private int     count = 3;

  private String  prefix;

  private boolean transaction;

  public RoleExportBuilder(String prefix, boolean transaction)
  {
    this.prefix = prefix;
    this.transaction = transaction;
  }

  @Request
  public Void doIt()
  {
    if (transaction)
    {
      doItTransaction();
    }
    else
    {
      createRoles();
    }

    return null;
  }

  @Transaction
  private void doItTransaction()
  {
    createRoles();
  }

  private void createRoles()
  {
    RoleDAO role = null;

    for (int i = 0; i < count; i++)
    {
      if (role == null)
      {
        role = RoleDAO.createRole(prefix + i, "Numbered Role " + i);
      }
      else
      {
        role = role.addAscendant(prefix + i, "Numbered Role " + i);
      }
    }
  }

  @Override
  public void undoIt()
  {
    for (int i = 0; i < count; i++)
    {
      RoleDAO.findRole(prefix + i).getBusinessDAO().delete();
    }
  }

}
