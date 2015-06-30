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
package com.runwaysdk.dataaccess.database;

import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;

public class AddColumnBatchDDLCommand extends AddColumnDDLCommand
{
  public AddColumnBatchDDLCommand(MdAttributeConcreteDAO mdAttributeConcreteDAO, String tableName, String columnName, String formattedType,
       boolean dropOnEndOfTransaction)
  {
    super(mdAttributeConcreteDAO, tableName, columnName, formattedType, "", "", dropOnEndOfTransaction);
  }

  /**
   * Do not add the column, as it will be added in batch later.
   */
  protected void callSuperDoIt() {}

  /**
   * Do not add a hashed column for batched DDL commands, as the hashed attribute
   * will be in the table definition.
   */
  protected void addHashedColumn() { }

  /**
   * There is nothing to undo because this command did not actually create anything.  The
   * attribute was created/updated in batch on the table.
   */
  public void undoIt() {}
}
