/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.dataaccess.resolver;

import java.io.File;
import java.util.List;

import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.dataaccess.transaction.TransactionImportManager;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.Roles;

public class ResolvedExportBuilder extends ExportBuilder<List<String>>
{
  private File         first;

  private File         second;

  private List<String> existing;

  public ResolvedExportBuilder(File first, File second)
  {
    super();

    this.first = first;
    this.second = second;
    this.existing = UserDAO.getEntityIdsDB(Roles.CLASS);
  }

  @Request
  protected List<String> doIt()
  {
    return this.doItTransaction();
  }

  @Transaction
  protected List<String> doItTransaction()
  {
    // IMPORTANT: IT IS ASSUMED THAT THE FIRST IMPORT CREATES THREE ROLES:
    // number.Role0, number.Role1, and number.Role2
    new TransactionImportManager(first.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();

    // Test Basic role specific resolver
    new TransactionImportManager(second.getAbsolutePath(), new RoleResolver()).importTransactions();

    return RoleDAO.getEntityIdsDB(Roles.CLASS);
  }

  @Override
  protected void undoIt()
  {
    // Delete all roles
    List<String> ids = RoleDAO.getEntityIdsDB(Roles.CLASS);

    for (String id : ids)
    {
      if (!existing.contains(id))
      {
        RoleDAO.get(id).getBusinessDAO().delete();
      }
    }
  }

}
