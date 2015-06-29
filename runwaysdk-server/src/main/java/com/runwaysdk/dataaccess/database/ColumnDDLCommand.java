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

import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.transaction.TransactionState;

public abstract class ColumnDDLCommand extends DDLCommand
{
  private MdAttributeConcreteDAO mdAttributeConcreteDAO;

  private TransactionState transactionState;

  private String tableName;

  // Actual database column name (may be hashed)
  private String columnName;

  // The column name defined in the metadata
  private String definedColumnName;

  private String formattedType;

  public ColumnDDLCommand(MdAttributeConcreteDAO mdAttributeConcreteDAO, String tableName, String columnName, String formattedType,
      String stmt, String undoStmt, boolean dropOnEndOfTransaction)
  {
    super(stmt, undoStmt, dropOnEndOfTransaction);

    this.mdAttributeConcreteDAO = mdAttributeConcreteDAO;

    this.tableName = tableName;
    this.columnName = columnName;

    if (mdAttributeConcreteDAO instanceof MdAttributeEnumerationDAO)
    {
      MdAttributeEnumerationDAO mdAttributeEnumerationDAO = (MdAttributeEnumerationDAO)mdAttributeConcreteDAO;

      // find out if this DDL command is for the cached database column name
      if (columnName.equals(mdAttributeEnumerationDAO.getCacheColumnName()))
      {
        this.definedColumnName = mdAttributeEnumerationDAO.getDefinedCacheColumnName();
      }
      else
      {
        this.definedColumnName = mdAttributeEnumerationDAO.getDefinedColumnName();
      }
    }
    else
    {
      this.definedColumnName = mdAttributeConcreteDAO.getDefinedColumnName();
    }

    this.formattedType = formattedType;

    this.transactionState = TransactionState.getCurrentTransactionState();
  }

  public MdAttributeConcreteDAO getMdAttributeConcreteDAO()
  {
    return mdAttributeConcreteDAO;
  }

  protected TransactionState getTransactionState()
  {
    return this.transactionState;
  }

  public String getTableName()
  {
    return this.tableName;
  }

  public String getColumnName()
  {
    return this.columnName;
  }

  public String getDefinedColumnName()
  {
    return this.definedColumnName;
  }

  public String getFormattedType()
  {
    return this.formattedType;
  }

  /**
   * Returns a key based on the table name and the column name that is currently being
   * used for the attribute.  It may be a temporary hashed value.
   *
   * @return key based on the table name and the column name that is currently being
   * used for the attribute.  It may be a temporary hashed value.
   */
  public String getCurrentDatabaseColumnNameKey()
  {
    return this.getTableName()+"."+this.getColumnName();
  }

  /**
   * Returns a key based on the table name and the column name that is defined
   * on the attribute metadata.
   *
   * @return key based on the table name and the column name that is defined
   * on the attribute metadata.
   */
  public String getDefinedColumnNameKey()
  {
    return this.getTableName()+"."+this.getDefinedColumnName();
  }

  /**
   * Builds a key based on the table name and the column name that is currently being
   * used for the attribute.  It may be a temporary hashed value.
   *
   * @param _tableName
   * @param _databaseColumnName
   *
   * @return key based on the table name and the column name that is currently being
   * used for the attribute.  It may be a temporary hashed value.
   */
  public static String buildCurrentDatabaseColumnNameKey(String _tableName, String _databaseColumnName)
  {
    return _tableName+"."+_databaseColumnName;
  }

  /**
   * Builds a key based on the table name and the column name that is defined
   * on the attribute metadata.
   *
   * @return key based on the table name and the column name that is defined
   * on the attribute metadata.
   */
  public static String buildDefinedColumnNameKey(String _tableName, String _metadataColumnName)
  {
    return _tableName+"."+_metadataColumnName;
  }
}
