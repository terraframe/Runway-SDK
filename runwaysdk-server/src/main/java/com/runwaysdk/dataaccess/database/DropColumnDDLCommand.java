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
package com.runwaysdk.dataaccess.database;

import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;

/**
 * This is a marker class to record column deletes.
 *
 * @author nathan
 */
public class DropColumnDDLCommand extends ColumnDDLCommand
{
  public DropColumnDDLCommand(MdAttributeConcreteDAO mdAttributeConcreteDAO, String tableName, String columnName, String formattedType,
      String stmt, String undoStmt, boolean dropOnEndOfTransaction)
  {
    super(mdAttributeConcreteDAO, tableName, columnName, formattedType, stmt, undoStmt, dropOnEndOfTransaction);

    // If we are dropping the hash, then we should drop the original as well, as the original command object
    // to drop the original column was removed when the hashed column was created.
    if (!this.getColumnName().equals(this.getDefinedColumnName()))
    {
      Database.dropField(this.getTableName(), this.getDefinedColumnName(), this.getFormattedType(), this.getMdAttributeConcreteDAO());
    }

  }


  /**
   * Executes the statement in this Command. Does not close the connection.
   */
  @Override
  public void doIt()
  {
    super.doIt();
  }
}
