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
package com.runwaysdk.dataaccess.resolver;

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;

public class UserExportBuilder extends ExportBuilder<List<UserDAO>>
{
  private int           count = 3;

  private List<UserDAO> list;

  public UserExportBuilder()
  {
    list = new LinkedList<UserDAO>();
  }

  @Request
  public List<UserDAO> doIt()
  {
    doItTransaction();

    return list;
  }

  @Transaction
  private void doItTransaction()
  {
    for (int i = 0; i < count; i++)
    {
      UserDAO user = UserDAO.newInstance();
      user.setUsername("TestUser" + i);
      user.setPassword("TestUser" + i);
      user.apply();

      list.add(user);
    }
  }

  @Override
  public void undoIt()
  {
    for (UserDAO user : list)
    {
      user.delete();
    }
  }
}
