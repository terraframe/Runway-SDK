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
package com.runwaysdk.dataaccess.database;

public class AddGroupIndexDDLCommand extends DDLCommand
{
  private String tableName;
  private String indexName;
  
  public AddGroupIndexDDLCommand(String tableName, String indexName, String stmt, String undoStmt)
  {
    super(stmt, undoStmt, false);

    this.tableName = tableName;
    this.indexName = indexName;
  }
  
  public String getIndexName()
  {
    return this.indexName;
  }

  public void undoIt()
  {
    if (Database.indexExists(this.tableName, this.getIndexName()))
    {
      super.undoIt();
    }
  }
}
