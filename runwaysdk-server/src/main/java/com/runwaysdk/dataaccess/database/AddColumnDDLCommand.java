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
package com.runwaysdk.dataaccess.database;

import java.util.List;

import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.transaction.TransactionState;

public abstract class AddColumnDDLCommand extends ColumnDDLCommand
{
  private String hashedColumnName;

  public AddColumnDDLCommand(MdAttributeConcreteDAO mdAttributeConcreteDAO, String tableName, String columnName, String formattedType,
      String stmt, String undoStmt, boolean dropOnEndOfTransaction)
  {
    super(mdAttributeConcreteDAO, tableName, columnName, formattedType, stmt, undoStmt, dropOnEndOfTransaction);

    this.hashedColumnName = "";
  }

  public String getHashedColumnName()
  {
    return this.hashedColumnName;
  }

  /**
   * True if a cache column was created, false otherwise.
   *
   * @return true if a cache column was created, false otherwise.
   */
  public boolean isHashColumnCreated()
  {
    if (this.hashedColumnName.equals(""))
    {
      return false;
    }
    else
    {
      return true;
    }
  }

  /**
   * If a hashed column was created return that name, otherwise return the name of the column.
   *
   * @return hashed column was created return that name, otherwise return the name of the column.
   */
  public String getColumnNameForDatabase()
  {
    if (this.isHashColumnCreated())
    {
      return this.getHashedColumnName();
    }
    else
    {
      return this.getColumnName();
    }
  }

  /**
   * Executes the statement in this Command. Does not close the connection.
   */
  @Override
  public void doIt()
  {
    TransactionState transactionState = this.getTransactionState();

    List<Command> notUndoableCommandList = transactionState.getNotUndoableCommandList();

    String thisKey = this.getCurrentDatabaseColumnNameKey();

    // Check and see if a command object is set to execute at the end of the transaction that
    // deletes this column.  If so, remove it from the transaction state.
    for (Command command : notUndoableCommandList)
    {
      // We don't want a false positive on the same object
      if (command.equals(this))
      {
        continue;
      }

      if (command instanceof DropColumnDDLCommand)
      {
        DropColumnDDLCommand dropColumnDDLCommand = (DropColumnDDLCommand)command;

        if (thisKey.equals(dropColumnDDLCommand.getCurrentDatabaseColumnNameKey()))
        {
          // If a command object exists that drops this exact column name, then remove it.
          transactionState.removeCommandObject(dropColumnDDLCommand);
        }

        if (
            (thisKey.equals(dropColumnDDLCommand.getCurrentDatabaseColumnNameKey()) ||
            thisKey.equals(dropColumnDDLCommand.getDefinedColumnNameKey()))
            &&
            this.isHashColumnCreated() == false
           )
        {
          this.hashedColumnName = ServerIDGenerator.generateUniqueDatabaseIdentifier();

          MdAttributeConcreteDAO mdAttributeConcreteDAO = this.getMdAttributeConcreteDAO();

          if (mdAttributeConcreteDAO instanceof MdAttributeEnumerationDAO)
          {
            MdAttributeEnumerationDAO mdAttributeEnumerationDAO = (MdAttributeEnumerationDAO)mdAttributeConcreteDAO;

            if (this.getDefinedColumnName().equals(mdAttributeEnumerationDAO.getDefinedCacheColumnName()))
            {
              mdAttributeEnumerationDAO.setHashedTempCacheColumnName(this.hashedColumnName);
            }
            else
            {
              mdAttributeConcreteDAO.setHashedTempColumnName(this.hashedColumnName);
            }
          }
          else
          {
            mdAttributeConcreteDAO.setHashedTempColumnName(this.hashedColumnName);
          }

          this.addHashedColumn();
        }
      }
    }

    this.callSuperDoIt();
  }

  /**
   * Calls the super doIt method.  Not all subclasses will call this method.
   */
  protected void callSuperDoIt()
  {
    // do not add the column if it already exists, but will be dropped at the end of the transaction.
    if (!this.isHashColumnCreated())
    {
      super.doIt();
    }
  }

  /**
   * Add a hashed column.
   */
  protected abstract void addHashedColumn();

  @Override
  public void doFinally()
  {
    if (this.isHashColumnCreated())
    {
      // Hashed column may have been previously deleted when yet another attribute of the same name is added
      if (Database.columnExists(this.getHashedColumnName(), this.getTableName()))
      {
        String dropColumnSQL;

        if (Database.columnExists(this.getColumnName(), this.getTableName()))
        {
          // Drop the column from the previous attribute.
          dropColumnSQL = Database.buildDropColumnString(this.getTableName(), this.getColumnName());
          this.executeSQL(dropColumnSQL, true);
        }

        // Create the column for this attribute;
        String createColumnSQL = Database.buildAddColumnString(this.getTableName(), this.getColumnName(), this.getFormattedType());
        this.executeSQL(createColumnSQL, true);

        // Copy all values from the temporary hashed column into the final column for this attribute
        String copySQL =
          "UPDATE "+this.getTableName()+"\n"+
          " SET "+this.getColumnName()+" = "+this.getHashedColumnName();

        this.executeSQL(copySQL, true);

        // Drop the hashed column
        dropColumnSQL = Database.buildDropColumnString(this.getTableName(), this.getHashedColumnName());
        this.executeSQL(dropColumnSQL, true);
      }
    }
    else
    {
      super.doFinally();
    }
  }
}
