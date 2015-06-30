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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.runwaysdk.constants.DatabaseProperties;
import com.runwaysdk.constants.EntityTypes;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.dataaccess.MdAttributeLocalDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalDAO;
import com.runwaysdk.dataaccess.transaction.TransactionState;

public class MetadataDisplayLabelDDLCommand implements Command
{
  private String tableName = EntityTypes.METADATADISPLAYLABEL.getTableName();
  
  private String addedColumnName;
  
  private String tempColumnName;
  
  private String formatedType;
  
  private MdAttributeConcreteDAO mdAttributeConcreteDAO;
  
  /**
   * The private, persistent connection to the Database. The connection is used for one
   * instance only.
   */
  protected Connection conx;
  
  public MetadataDisplayLabelDDLCommand(MdAttributeConcreteDAO mdAttributeConcreteDAO)
  {
    this.mdAttributeConcreteDAO = mdAttributeConcreteDAO;
    
    int counter = TransactionState.getCurrentTransactionState().getMetadataTempColumnCounter();
    TransactionState.getCurrentTransactionState().incrementMetadataTempColumnCounter();
    
    this.tableName = EntityTypes.METADATADISPLAYLABEL.getTableName();
    
    this.addedColumnName = mdAttributeConcreteDAO.getDefinedColumnName();
    
    this.tempColumnName = MdAttributeLocalDAO.buildMetadataTempColumn(counter);
    
    String displayLabelColumnType = DatabaseProperties.getDatabaseType(MdAttributeCharacterInfo.CLASS);
    String displayColumnSize = MdAttributeLocalDAOIF.METADATA_DISPLAY_LABEL_COLUMN_SIZE;
    this.formatedType = Database.formatCharacterField(displayLabelColumnType, displayColumnSize);
    
    
    this.mdAttributeConcreteDAO.setHashedTempColumnName(this.tempColumnName);
    
    this.conx = Database.getDDLConnection(); 
  }

  /**
   * @see com.runwaysdk.dataaccess.Command#isUndoable()
   */
  @Override
  // Execute at the end of the transaction.
  public boolean isUndoable()
  {
    return false;
  }

  /**
   * @see com.runwaysdk.dataaccess.Command#doIt()
   */
  @Override
  public void doIt() 
  {
    // Create the column for this attribute;
    String createColumnSQL = Database.buildAddColumnString(this.tableName, this.addedColumnName, this.formatedType);
    this.executeSQL(createColumnSQL);
    
    // Copy all values from the temporary hashed column into the final column for this attribute
    String copySQL =
      "UPDATE "+this.tableName+"\n"+
      " SET "+this.addedColumnName+" = "+this.tempColumnName;
    this.executeSQL(copySQL);
    
    String clearSQL = 
      "UPDATE "+this.tableName+"\n"+
      " SET "+this.tempColumnName+" = NULL";
    this.executeSQL(clearSQL);    

  }
  
  /**
   * @see com.runwaysdk.dataaccess.Command#undoIt()
   */
  // Nothing to undo, as the command is executed at the end of the transaction.
  @Override
  public void undoIt() {}
  
  /**
   * @see com.runwaysdk.dataaccess.Command#doFinally()
   */
  @Override
  public void doFinally() {}
  
  
  /**
   * @see com.runwaysdk.dataaccess.Command#doItString()
   */
  public String doItString() { return ""; }

  /**
   * @see com.runwaysdk.dataaccess.Command#undoItString()
   */
  public String undoItString() { return ""; }
  
  
  /**
   * Executes the given sql string.  Aspects to not interfere with the database connection objects.
   *
   * @param sqlStmt
   *
   */
  protected void executeSQL(String sqlStmt)
  {
    try
    {
      Statement statement = conx.createStatement();

      statement.executeUpdate(sqlStmt);
      statement.close();

      conx.commit();
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
}
