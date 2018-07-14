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

import org.junit.Before;

import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.transaction.TransactionImportManager;
import com.runwaysdk.session.Request;

public class DeleteExportBuilder extends ExportBuilder<Void>
{
  private List<UserDAO> list;

  private File          file;

  private String        id;

  public DeleteExportBuilder(List<UserDAO> list, File file, String id)
  {
    this.list = list;
    this.file = file;
    this.id = id;
  }

  @Override
  @Request
  @Before
  public void setUp()
  {
    new TransactionImportManager(file.getAbsolutePath(), new DefaultConflictResolver()).importTransactions();
  }

  @Override
  protected Void doIt()
  {

    UserDAO.get(id).getBusinessDAO().delete();

    return null;
  }

  @Override
  protected void undoIt()
  {
    for (UserDAO u : list)
    {
      TestFixtureFactory.delete(u);
    }
  }

}
