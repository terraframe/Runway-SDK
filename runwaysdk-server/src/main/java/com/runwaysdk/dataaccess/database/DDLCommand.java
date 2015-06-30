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
/*
 * Created on Jun 28, 2005
 */
package com.runwaysdk.dataaccess.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.runwaysdk.dataaccess.Command;


/**
 * <code>DDLCommand</code> allows for multi-statement transactions by holding an undo
 * statement for a statement that causes an autocommit in the database. A private
 * connection to the database is maintained by every instance. In the event that a
 * transaction cannot be completed, but an autocommit has occurred, the undoIt() method
 * will return the database to its original state, maintaining safe transactions.
 *
 * @author Eric
 * @version $Revision 1.0 $
 * @since
 */
public class DDLCommand implements Command
{
  /**
   * The sql statement to be executed by this command.
   */
  protected String     stmt;

  /**
   * The undo sql statement for this command.
   */
  protected String     undoStmt;

  /**
   * The private, persistent connection to the Database. The connection is used for one
   * instance only.
   */
  protected Connection conx;

  /**
   * <code><b>true</b></code> if this DDLCommand is a drop and the drop should happen at the end of the transaction,.
   * <code><b>false</b></code> if the drop should happen immediately.
   */
  private boolean    dropOnEndOfTransaction;

  public DDLCommand(String stmt, String undoStmt, boolean dropOnEndOfTransaction)
  {
    this.stmt = stmt;
    this.undoStmt = undoStmt;
    this.dropOnEndOfTransaction = dropOnEndOfTransaction;
    this.conx = Database.getDDLConnection();
  }

  /**
   * Executes the statement in this Command. Does not close the connection.
   */
  public void doIt()
  {
    // If this is not undoable, then it is executing at the end of a transaction.
    // Therefore, the connection should commit in case the connection object between
    // DDL and DML is the same.
    boolean forceCommit = false;
    if (!this.isUndoable())
    {
      forceCommit = true;
    }
    this.executeSQL(this.stmt, forceCommit);
  }

  /**
   * Executes the undo in this Command, and closes the connection.
   */
  public void undoIt()
  {
    // if the connections are shared, then the DDL commands will automatically be rolled back with the transaction.
    if (!Database.sharesDDLandDMLconnection())
    {
      this.executeSQL(this.undoStmt, false);
    }
  }

  /**
   * Acts as a finally block in that, regardless of a successful or unsuccessful
   * transaction, this action is performed at the end of the transaction.
   */
  @Override
  public void doFinally(){}

  /**
   * Closes the connection to the database.
   */
  public void close()
  {
    try
    {
      Database.closeConnection(conx);
    }
    catch (SQLException ex)
    {
      Database.throwDatabaseException(ex);
    }
  }

  /**
   * Executes the given sql string.  Aspects to not interfere with the database connection objects.
   *
   * @param sqlStmt
   * @param forceCommit true if a commit must be forced.
   */
  protected void executeSQL(String sqlStmt, boolean forceCommit)
  {
    try
    {
      Statement statement = conx.createStatement();

      statement.executeUpdate(sqlStmt);
      statement.close();

      if (forceCommit || !Database.sharesDDLandDMLconnection())
      {
        conx.commit();
      }
    }
    catch (SQLException ex)
    {
      String error = ex.getMessage() + " - SQL Statement That caused the error: [" + sqlStmt + ']';
      throw new DatabaseException(error, ex, sqlStmt);
    }
    finally
    {
      this.close();
    }
  }

  /**
   * Indicates if this DDLCommand removes data from the database.
   * @return <code><b>false</b></code> if this DDLCommand is a drop or a delete
   */
  public boolean isUndoable()
  {
    return !dropOnEndOfTransaction;
  }

  /**
   * Returns a human readable string describing what this command
   * is trying to do.
   * @return human readable string describing what this command
   * is trying to do.
   */
  public String doItString()
  {
    return "Database SQL statement: "+this.getStmt();
  }

  /**
   * Returns a human readable string describing what this command
   * needs to do in order to undo itself.
   * @return human readable string describing what this command
   * needs to do in order to undo itself.
   */
  public String undoItString()
  {
    return "Database SQL statement: "+this.getUndoStmt();
  }

  /**
   * @return Returns the statement.
   */
  public String getStmt()
  {
    return stmt;
  }

  /**
   * @return Returns the undo statement.
   */
  public String getUndoStmt()
  {
    return undoStmt;
  }
}
