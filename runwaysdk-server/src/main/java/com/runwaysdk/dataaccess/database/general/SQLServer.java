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
package com.runwaysdk.dataaccess.database.general;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.locks.ReentrantLock;

import net.sourceforge.jtds.jdbcx.JtdsDataSource;

import org.apache.commons.dbcp.datasources.SharedPoolDataSource;

import com.google.inject.Inject;
import com.runwaysdk.RunwayVersion;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.DatabaseProperties;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdAttributeBlobInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeClobInfo;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.MdAttributeDateTimeInfo;
import com.runwaysdk.constants.MdAttributeDecimalInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeFileInfo;
import com.runwaysdk.constants.MdAttributeFloatInfo;
import com.runwaysdk.constants.MdAttributeHashInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalTextInfo;
import com.runwaysdk.constants.MdAttributeLongInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.constants.MdAttributeSymmetricInfo;
import com.runwaysdk.constants.MdAttributeTermInfo;
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.constants.MdAttributeTimeInfo;
import com.runwaysdk.dataaccess.DuplicateGraphPathException;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.database.AddColumnBatchDDLCommand;
import com.runwaysdk.dataaccess.database.AddColumnSingleDDLCommand;
import com.runwaysdk.dataaccess.database.AddGroupIndexDDLCommand;
import com.runwaysdk.dataaccess.database.DDLCommand;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.DatabaseException;
import com.runwaysdk.dataaccess.database.DropColumnDDLCommand;
import com.runwaysdk.dataaccess.database.DropGroupAttributeDDLCommand;
import com.runwaysdk.dataaccess.database.DuplicateDataDatabaseException;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.query.SubSelectReturnedMultipleRowsException;


public class SQLServer extends AbstractDatabase
{
  private String        objectSequenceTableName;
  private String        transactionSequenceTableName;
  private Connection    conn;
  private final ReentrantLock nextSequenceNumberLock;
  private static final String COLLATE_LATIN = "COLLATE Latin1_General_CS_AS";

  /**
   * Initializes datasource to point to SQL Server
   */
  @Inject
  public SQLServer()
  {
    super();

    this.nextSequenceNumberLock       = new ReentrantLock();
    this.objectSequenceTableName      = "object_seq_table";
    this.transactionSequenceTableName = "transaction_seq_table";

    // The container is not providing a pooled datasource
    if (this.dataSource == null)
    {
      JtdsDataSource serverDataSource = new JtdsDataSource();
      serverDataSource.setServerName(DatabaseProperties.getServerName());
      serverDataSource.setPortNumber(DatabaseProperties.getPort());
      serverDataSource.setDatabaseName(DatabaseProperties.getDatabaseName());
      serverDataSource.setUser(DatabaseProperties.getUser());
      serverDataSource.setPassword(DatabaseProperties.getPassword());

      int maxDbConnections = DatabaseProperties.getMaxConnections() - 1;

      if (maxDbConnections < 2)
      {
        maxDbConnections = 2;
      }

      boolean pooling = DatabaseProperties.getConnectionPooling();
      if (pooling)
      {
        SharedPoolDataSource sharedPoolDataSource = new SharedPoolDataSource();
        sharedPoolDataSource.setConnectionPoolDataSource(serverDataSource);
        sharedPoolDataSource.setMaxActive(maxDbConnections);
        sharedPoolDataSource.setTestOnBorrow(true);
        sharedPoolDataSource.setValidationQuery("SELECT 1");
        this.dataSource = sharedPoolDataSource;
      }
      else
      {
        this.dataSource = serverDataSource;
      }
    }
  }

  public void initialSetup(String rootUser, String rootPass, String rootDb)
  {
    // Set up the root data source
    JtdsDataSource rootSource = new JtdsDataSource();
    rootSource.setServerName(DatabaseProperties.getServerName());
    rootSource.setPortNumber(DatabaseProperties.getPort());
    rootSource.setDatabaseName(rootDb);
    rootSource.setUser(rootUser);
    rootSource.setPassword(rootPass);
    this.rootDataSource = rootSource;

    LinkedList<String> statements = new LinkedList<String>();
    String dbName = DatabaseProperties.getDatabaseName();

    this.dropUser();

    this.dropDb();

    this.createDb(rootDb);

    this.createUser();

    statements.clear();
    statements.add("USE " + dbName);
    statements.add("EXEC sp_dbcmptlevel "+dbName+", 80");
    executeAsRoot(statements, true);
  }

  /**
   * Drop the database.
   */
  @Override
  public void dropDb()
  {
    LinkedList<String> statements = new LinkedList<String>();
    String dbName = DatabaseProperties.getDatabaseName();
    try
    {
      statements.add("DROP DATABASE " + dbName);
      executeAsRoot(statements, false);
    }
    catch (DatabaseException e)
    {
      System.out.println(e);
      // This happens if the database doesn't exist to be dropped.  Keep going.
    }
  }

  /**
   * Creates the database.
   */
  @Override
  public void createDb(String rootDb)
  {
    LinkedList<String> statements = new LinkedList<String>();
    String dbName = DatabaseProperties.getDatabaseName();
    statements.add("CREATE DATABASE " + dbName);
    executeAsRoot(statements, false);
  }

  /**
   * Drops the database user.
   */
  @Override
  public void dropUser()
  {
    // OK, not exactly dropping a user here, but this is the action that is performed for
    // this database acording to the template pattern used for this database hierarchy
    LinkedList<String> statements = new LinkedList<String>();
    String userName = DatabaseProperties.getUser();
    String pass = DatabaseProperties.getPassword();
    try
    {
      statements.add("CREATE LOGIN " + userName + " WITH PASSWORD = '" + pass + "'");
      executeAsRoot(statements, false);
    }
    catch (DatabaseException e)
    {
      // This happens if the user already exists.  Keep going.
    }
  }

  /**
   * Creates the database user.
   */
  @Override
  public void createUser()
  {
    String userName = DatabaseProperties.getUser();
    String dbName = DatabaseProperties.getDatabaseName();

    LinkedList<String> statements = new LinkedList<String>();
    statements.add("USE " + dbName);
    statements.add("CREATE USER " + userName + " FOR LOGIN " + userName);
    statements.add("EXEC sp_addrolemember 'db_owner', " + userName);
    executeAsRoot(statements,  false);
  }

  /**
   *
   * @see com.runwaysdk.dataaccess.AbstractDatabase#sharesDDLandDMLconnection()
   */
  @Override
  public boolean sharesDDLandDMLconnection()
  {
    return false;
  }

  /**
   * Returns the variance function name for the database in use.
   * @return variance function name for the database in use.
   */
  public String varianceFunction()
  {
     return "VAR";
  }

  /**
   * Returns the standard deviation function name for the database in use.
   * @return standard deviation function name for the database in use.
   */
  public String stdDevFunction()
  {
     return "STDEV";
  }

  /**
   *
   * @see com.runwaysdk.dataaccess.AbstractDatabase#addTempFieldsToTable(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.Integer)
   */
  @Override
  public void addTempFieldsToTable(String tableName, String columnName, String columnType, Integer numberOfTempFields)
  {
    String statement = "ALTER TABLE " + tableName + " ADD (";
    for (int i = 0; i < numberOfTempFields; i++)
    {
      if (i != 0)
      {
        statement += ", ";
      }

      statement += columnName + "_" + i + " " + columnType;
    }

    statement += ")";

    String undo = "ALTER TABLE " + tableName + " DROP (";
    for (int i = 0; i < numberOfTempFields; i++)
    {
      if (i != 0)
      {
        undo += ", ";
      }

      undo += " "+columnName + "_" + i;
    }
    undo += ")";

    new DDLCommand(statement, undo, false).doIt();
  }
  
  /**
   *
   * @see com.runwaysdk.dataaccess.AbstractDatabase#dropField(java.lang.String,
   *      java.lang.String, java.lang.String, com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO)
   */
  @Override
  public void dropField(String table, String columnName, String formattedColumnType, MdAttributeConcreteDAO mdAttributeConcreteDAO)
  {
    String statement = buildDropColumnString(table, columnName);

    String undo = buildAddColumnString(table, columnName, formattedColumnType);

    new DropColumnDDLCommand(mdAttributeConcreteDAO, table, columnName, formattedColumnType, statement, undo, true).doIt();
  }

  /**
   *
   * @see com.runwaysdk.dataaccess.AbstractDatabase#buildAddColumnString(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  @Override
  public String buildAddColumnString(String table, String columnName, String formattedColumnType)
  {
    return "ALTER TABLE " + table + " ADD " + columnName + " " + formattedColumnType;
  }

 /**
  *
  * @see com.runwaysdk.dataaccess.AbstractDatabase#buildDropColumnString(java.lang.String,
  *      java.lang.String)
  */
  @Override
  public String buildDropColumnString(String table, String columnName)
  {
    return "ALTER TABLE " + table + " DROP COLUMN " + columnName;
  }


  @Override
  public void alterFieldType(String table, String columnName, String newDbColumnType, String oldDbColumnType)
  {
    String statement = "ALTER TABLE " + table + " ALTER COLUMN " + columnName + " " + newDbColumnType;
    String undo = "ALTER TABLE " + table + " ALTER COLUMN " + columnName + " " + oldDbColumnType;

    new DDLCommand(statement, undo, false).doIt();

  }

  @Override
  public void addField(String table, String columnName, String type, String size)
  {
    String statement = "ALTER TABLE " + table + " ADD " + columnName + " " + type;

    if (size != null)
    {
      statement += "(" + size + ") " + COLLATE_LATIN;
    }

    String undo = "ALTER TABLE " + table + " DROP COLUMN " + columnName;
    new DDLCommand(statement, undo, false).doIt();
  }

  @Override
  public String addFieldBatch(String table, String columnName, String type, String size)
  {
    String statement = columnName + " " + type;

    if (size != null)
    {
      statement += "(" + size + ") " + COLLATE_LATIN;
    }

    return statement;
  }

  /**
   *
   * @see com.runwaysdk.dataaccess.AbstractDatabase#addField(java.lang.String,
   *      java.lang.String, java.lang.String, com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO)
   */
  @Override
  public void addField(String table, String columnName, String formattedColumnType, MdAttributeConcreteDAO mdAttributeConcreteDAO)
  {
    String statement = this.buildAddColumnString(table, columnName, formattedColumnType);

    String undo = this.buildDropColumnString(table, columnName);

    new AddColumnSingleDDLCommand(mdAttributeConcreteDAO, table, columnName, formattedColumnType,
        statement, undo, false).doIt();
  }

  /**
   *
   * @see com.runwaysdk.dataaccess.AbstractDatabase#addFieldBatch(java.lang.String,
   *      java.lang.String, java.lang.String, com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO)
   */
  @Override
  public String addFieldBatch(String tableName, String columnName, String formattedType, MdAttributeConcreteDAO mdAttributeConcreteDAO)
  {
    AddColumnBatchDDLCommand addColumnBatchDDLCommand = new AddColumnBatchDDLCommand(mdAttributeConcreteDAO, tableName, columnName, formattedType, false);

    addColumnBatchDDLCommand.doIt();

    return addColumnBatchDDLCommand.getColumnNameForDatabase() + "  " + formattedType;
  }


  @Override
  public void addDecField(String table, String columnName, String type, String length, String decimal)
  {
    String ddlType = formatDDLDecField(type, length, decimal);
    String statement = "ALTER TABLE " + table + " ADD " + columnName + " " + ddlType;
    String undo = "ALTER TABLE " + table + " DROP COLUMN " + columnName;

    new DDLCommand(statement, undo, false).doIt();
  }

  @Override
  public String addDecFieldBatch(String table, String columnName, String type, String length, String decimal)
  {
    String ddlType = formatDDLDecField(type, length, decimal);
    return columnName + " " + ddlType;
  }

  @Override
  public void addUniqueIndex(String table, String columnName, String indexName)
  {
    String statement = "CREATE UNIQUE INDEX "+indexName+" ON "+table+" ("+columnName+")";
    String undo = "DROP INDEX "+table+"."+indexName;

    new DDLCommand(statement, undo, false).doIt();
  }

  @Override
  public void addNonUniqueIndex(String table, String columnName, String indexName)
  {
    String statement = "CREATE INDEX "+indexName+" ON "+table+" ("+columnName+")";
    String undo = "DROP INDEX "+table+"."+indexName;

    new DDLCommand(statement, undo, false).doIt();
  }

  @Override
  public void dropUniqueIndex(String table, String columnName, String indexName, boolean delete)
  {
    String statement = "DROP INDEX "+table+"."+indexName;
    String undo = "CREATE UNIQUE INDEX "+indexName+" ON "+table+" ("+columnName+")";
    new DDLCommand(statement, undo, delete).doIt();
  }

  @Override
  public void dropNonUniqueIndex(String table, String columnName, String indexName, boolean delete)
  {
    String statement = "DROP INDEX "+table+"."+indexName;
    String undo = "CREATE INDEX "+indexName+" ON "+table+" ("+columnName+")";
    new DDLCommand(statement, undo, delete).doIt();
  }

  @Override
  public void addGroupAttributeIndex(String tableName,  String indexName, List<String> attributeNames, boolean isUnique)
  {
    String statement = "CREATE ";

    if (isUnique)
    {
      statement += " UNIQUE ";
    }

    statement += " INDEX "+indexName+" ON "+tableName+" (";

    for (int i=0; i<attributeNames.size(); i++)
    {
      if ( i != 0)
      {
        statement += ", ";
      }
      statement += attributeNames.get(i);
    }

    statement += ")";

    String undo = "DROP INDEX "+tableName+"."+indexName;

    new AddGroupIndexDDLCommand(tableName, indexName, statement, undo).doIt();
  }

  @Override
  public void dropGroupAttributeIndex(String tableName, String indexName, List<String> attributeNames, boolean isUnique, boolean delete)
  {
    String statement = "DROP INDEX "+tableName+"."+indexName;
    String undo = "ALTER TABLE "+tableName+" ADD ";

    if (isUnique)
    {
      undo += " UNIQUE ";
    }
    undo += indexName+" (";

    for (int i=0; i<attributeNames.size(); i++)
    {
      if ( i != 0)
      {
        undo += ", ";
      }

      undo += attributeNames.get(i);
    }

    undo += ")";

    new DropGroupAttributeDDLCommand(tableName, indexName, statement, undo, delete).doIt();

  }

  @Override
  public void createClassTable(String tableName)
  {
    String statement = startCreateClassTable(tableName)+" "+endCreateClassTable(tableName);
    String undo = "DROP TABLE " + tableName;

    new DDLCommand(statement, undo, false).doIt();
  }

  @Override
  public String startCreateClassTable(String tableName)
  {
    return "CREATE TABLE " + tableName + " ( " + EntityDAOIF.ID_COLUMN + " CHAR(" + Database.DATABASE_ID_SIZE + ") " + COLLATE_LATIN + "  NOT NULL PRIMARY KEY";
  }

  @Override
  public void createClassTableBatch(String tableName, List<String> columnDefs)
  {
    String statement = startCreateClassTable(tableName);
    for (String columnDef : columnDefs)
    {
      statement += "\n,"+columnDef;
    }
    statement += " "+endCreateClassTable(tableName);

    String undo = "DROP TABLE " + tableName;

    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * Creates a new table in the database for a relationships. Automatically adds the Component.ID column as the primary
   * key.
   *
   * @param tableName The name of the new table.
   * @param index1Name The name of the 1st index used by the given table.
   * @param index2Name The name of the 1st index used by the given table.
   * @param isUnique Indicates whether the parent_id child_id pair should be made unique.  This should only be
   *                 done on concrete relationship types.
   */
  @Override
  public void createRelationshipTable(String tableName, String index1Name, String index2Name, boolean isUnique)
  {
    String statement =
      this.startCreateRelationshipTableBatch(tableName)+" "+
      this.endCreateClassTable(tableName);

    String undo = "DROP TABLE " + tableName;
    new DDLCommand(statement, undo, false).doIt();

    this.createRelationshipTableIndexesBatch(tableName, index1Name, index2Name, isUnique);
  }

  /**
   * Creates a new table in the database for relationship, including all columns for that table.
   *
   * @param tableName table name
   * @param columnDefs columnDefs column definitions.
   */
  public void createRelationshipTableBatch(String tableName, List<String> columnDefs)
  {
    String statement = startCreateRelationshipTableBatch(tableName);
    for (String columnDef : columnDefs)
    {
      statement += "\n,"+columnDef;
    }
    statement += " "+endCreateClassTable(tableName);

    String undo = "DROP TABLE " + tableName;

    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * Returns the SQL string for a new table in the database for a relationship, minus the closing parenthesis.
   * Automatically adds the Component.ID column as the primary key.
   *
   * @param tableName  The name of the new table.
   */
  @Override
  public String startCreateRelationshipTableBatch(String tableName)
  {
    return "CREATE TABLE "+tableName+" \n"+
    "("+EntityDAOIF.ID_COLUMN+" CHAR("+Database.DATABASE_ID_SIZE+") " + COLLATE_LATIN + "  NOT NULL PRIMARY KEY, \n"+
    RelationshipDAOIF.PARENT_ID_COLUMN+"                    CHAR("+Database.DATABASE_ID_SIZE+") " + COLLATE_LATIN + "  NOT NULL, \n"+
    RelationshipDAOIF.CHILD_ID_COLUMN+"                     CHAR("+Database.DATABASE_ID_SIZE+") " + COLLATE_LATIN + "  NOT NULL";
  }

  /**
   * Performs an alter table command on the given table and adds the given column definitions.
   *
   * @param tableName table name
   * @param columnNames column names
   * @param columnDefs columnDefs column definitions.
   */
  @Override
  public void alterClassTableBatch(String tableName, List<String> columnNames, List<String> columnDefs)
  {
    String statement = "ALTER TABLE "+tableName+" ADD (";

    String undo = "ALTER TABLE "+tableName+" DROP (";

    boolean firstIteration = true;
    for (String columnDef : columnDefs)
    {
      if (!firstIteration)
      {
        statement += ", ";
      }
      else
      {
        firstIteration = false;
      }

      statement += " "+columnDef;
    }
    statement += ")";

    firstIteration = true;
    for (String columnName : columnNames)
    {
      if (!firstIteration)
      {
        undo += ", ";
      }
      else
      {
        firstIteration = false;
      }

      undo += " "+columnName;
    }
    undo += ")";

    new DDLCommand(statement, undo, false).doIt();
  }
  /**
   * Creates indexes on a relationship table.
   *
   * @param tableName  The name of the new table.
   * @param index1Name The name of the 1st index used by the given table.
   * @param index2Name The name of the 1st index used by the given table.
   * @param isUnique Indicates whether the parent_id child_id pair should be made unique.  This should only be
   *                 done on concrete relationship types.
   */
  @Override
  public void createRelationshipTableIndexesBatch(String tableName, String index1Name, String index2Name, boolean isUnique)
  {
    // Create the first index
    String statement = "CREATE ";
    if (isUnique)
    {
      statement += " UNIQUE ";
    }
    statement += " INDEX "+index1Name+" ON "+tableName+" ("+RelationshipDAOIF.PARENT_ID_COLUMN+", "+RelationshipDAOIF.CHILD_ID_COLUMN+")";
    String undo = "DROP INDEX "+tableName+"."+index1Name;
    new DDLCommand(statement, undo, false).doIt();

    // Create the second index
    statement = "CREATE INDEX "+index2Name+" ON "+tableName+
      " ("+RelationshipDAOIF.CHILD_ID_COLUMN+")";
    undo = "DROP INDEX "+tableName+"."+index2Name;
    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * @see com.runwaysdk.dataaccess.database.Database#createEnumerationTable(String, String);
   */
  @Override
  public void createEnumerationTable(String tableName, String id)
  {
    String statement =
      "CREATE TABLE "+tableName+" \n"+
      "("+MdEnumerationDAOIF.SET_ID_COLUMN+               " CHAR("+Database.DATABASE_SET_ID_SIZE+") " + COLLATE_LATIN + "  NOT NULL, \n"+
      MdEnumerationDAOIF.ITEM_ID_COLUMN+ " CHAR("+Database.DATABASE_ID_SIZE+") " + COLLATE_LATIN + "  NOT NULL) ";
    String undo = "DROP TABLE " + tableName;
    new DDLCommand(statement, undo, false).doIt();

    // Create the first index
    String indexName = this.createIdentifierFromId(id);
    statement = "CREATE UNIQUE INDEX "+indexName+" ON "+tableName+" ("+MdEnumerationDAOIF.SET_ID_COLUMN+", "+MdEnumerationDAOIF.ITEM_ID_COLUMN+")";
    undo = "DROP INDEX "+tableName+"."+indexName;
    new DDLCommand(statement, undo, false).doIt();
  }

  @Override
  public void dropClassTable(String tableName)
  {
    String statement = "DROP TABLE " + tableName;
    String undo = "CREATE TABLE " + tableName + " ( "+EntityDAOIF.ID_COLUMN+" CHAR(" + Database.DATABASE_ID_SIZE
        + ") " + COLLATE_LATIN + "  NOT NULL PRIMARY KEY )";

    new DDLCommand(statement, undo, true).doIt();
  }

  /**
   * Drops an entire table from the database for a relationship. An undo command is created that will
   * recreate the table if transaction management requires a rollback. However, the undo
   * will <b>not </b> recreate all of the columns in the table, only the ID.
   *
   * @param table The name of the table to drop.
   * @param index1Name The name of the 1st index used by the given table.
   * @param index2Name The name of the 1st index used by the given table.
   * @param isUnique Indicates whether the parent_id child_id pair should be made unique.  This should only be
   *                 done on concrete relationship types.
   */
  @Override
  public void dropRelationshipTable(String tableName, String index1Name, String index2Name, boolean isUnique)
  {
    // Drop the first index
    String statement = "DROP INDEX "+tableName+"."+index1Name;
    String undo = "CREATE INDEX "+index1Name+" ON "+tableName+
      " ("+RelationshipDAOIF.PARENT_ID_COLUMN+", "+RelationshipDAOIF.CHILD_ID_COLUMN+")";
    new DDLCommand(statement, undo, true).doIt();

    // Drop the second index
    statement = "DROP INDEX "+tableName+"."+index2Name;
    undo = "CREATE ";
    if (isUnique)
    {
      undo += " UNIQUE ";
    }
    undo += " INDEX "+index2Name+" ON "+tableName+" ("+RelationshipDAOIF.CHILD_ID_COLUMN+")";
    new DDLCommand(statement, undo, true).doIt();

    // Drop the table
    statement = "DROP TABLE " + tableName;
    undo = "CREATE TABLE "+tableName+" \n"+
      "("+EntityDAOIF.ID_COLUMN+" CHAR("+Database.DATABASE_ID_SIZE+") " + COLLATE_LATIN + "  NOT NULL PRIMARY KEY, \n"+
      RelationshipDAOIF.PARENT_ID_COLUMN+"                    CHAR("+Database.DATABASE_ID_SIZE+") " + COLLATE_LATIN + "  NOT NULL, \n"+
      RelationshipDAOIF.CHILD_ID_COLUMN+"                     CHAR("+Database.DATABASE_ID_SIZE+") " + COLLATE_LATIN + "  NOT NULL) ";
    new DDLCommand(statement, undo, true).doIt();
  }

  /**
   * @see com.runwaysdk.dataaccess.database.Database#dropEnumerationTable(String, String);
   */
  @Override
  public void dropEnumerationTable(String tableName, String id)
  {
    // Create the first index
    String indexName = this.createIdentifierFromId(id);
    String statement = "DROP INDEX "+tableName+"."+indexName;
    String undo = "CREATE UNIQUE INDEX "+indexName+" ON "+tableName+" ("+MdEnumerationDAOIF.SET_ID_COLUMN+", "+MdEnumerationDAOIF.ITEM_ID_COLUMN+")";
    new DDLCommand(statement, undo, true).doIt();

    statement = "DROP TABLE " + tableName;
    undo =
      "CREATE TABLE "+tableName+" \n"+
      "("+MdEnumerationDAOIF.SET_ID_COLUMN+               " CHAR("+Database.DATABASE_SET_ID_SIZE+") " + COLLATE_LATIN + "  NOT NULL, \n"+
      MdEnumerationDAOIF.ITEM_ID_COLUMN+ " CHAR("+Database.DATABASE_ID_SIZE+") " + COLLATE_LATIN + "  NOT NULL) ";
    new DDLCommand(statement, undo, true).doIt();
  }


//Heads up:
// @Override
// public List<String> getFields(String table)
// {
//   String statement = "SELECT column_name AS field FROM information_schema.columns WHERE table_name = '"+table+"'";
//   List<DynaBean> rows = query(statement);
//
//   LinkedList<String> fields = new LinkedList<String>();
//
//   Iterator<DynaBean> i = rows.iterator();
//   while (i.hasNext())
//   {
//     fields.add((String) i.next().get("field"));
//   }
//   return fields;
// }

  @Override
  public List<String> getColumnNames(String tableName)
  {
    String sqlStmt = "SELECT column_name AS field FROM information_schema.columns WHERE table_name = '"+tableName+"'";
    ResultSet resultSet = query(sqlStmt);
    LinkedList<String> columnNames = new LinkedList<String>();

    try
    {
      while (resultSet.next())
      {
        columnNames.add((String)resultSet.getString("field"));
      }
    }
    catch (SQLException sqlEx1)
    {
      Database.throwDatabaseException(sqlEx1);
    }
    finally
    {
      try
      {
        java.sql.Statement statement = resultSet.getStatement();
        resultSet.close();
        statement.close();
      }
      catch (SQLException sqlEx2)
      {
        Database.throwDatabaseException(sqlEx2);
      }
    }
    return columnNames;
  }

  /**
   * Returns true if a column with the given name exists on the table with the given name, false otherwise.
   *
   * @param columnName assumes column name is lower case.
   * @param tableName
   *
   * @return true if a column with the given name exists on the table with the given name, false otherwise.
   */
  @Override
  public boolean columnExists(String columnName, String tableName)
  {
    String sqlStmt =
      " SELECT column_name AS field \n"+
      "   FROM information_schema.columns \n"+
      "  WHERE table_name = '"+tableName+"' \n"+
      "    AND column_name = '"+columnName+"'";
    ResultSet resultSet = query(sqlStmt);

    try
    {
      while (resultSet.next())
      {
        return true;
      }
    }
    catch (SQLException sqlEx1)
    {
      Database.throwDatabaseException(sqlEx1);
    }
    finally
    {
      try
      {
        java.sql.Statement statement = resultSet.getStatement();
        resultSet.close();
        statement.close();
      }
      catch (SQLException sqlEx2)
      {
        Database.throwDatabaseException(sqlEx2);
      }
    }
    return false;
  }



//@Override
//public List<String> getTables()
//{
//  String statement = "SELECT table_name FROM information_schema.tables";
//  List<DynaBean> rows = query(statement);
//
//  LinkedList<String> tables = new LinkedList<String>();
//
//  Iterator<DynaBean> i = rows.iterator();
//  while (i.hasNext())
//  {
//    tables.add((String) i.next().get("table_name"));
//  }
//  return tables;
//}

  @Override
  public boolean tableExists(String tableName)
  {
    String sqlStmt = "SELECT * FROM information_schema.tables " +
      "WHERE table_name = '"+tableName+"'";

    ResultSet resultSet = query(sqlStmt);

    boolean returnResult = false;

    try
    {
      if (resultSet.next())
      {
        returnResult =  true;
      }
    }
    catch (SQLException sqlEx1)
    {
      Database.throwDatabaseException(sqlEx1);
    }
    finally
    {
      try
      {
        java.sql.Statement statement = resultSet.getStatement();
        resultSet.close();
        statement.close();
      }
      catch (SQLException sqlEx2)
      {
        Database.throwDatabaseException(sqlEx2);
      }
    }

    return returnResult;
  }

// Heads up:
//  @Override
//  public boolean tableExists(String tableName)
//  {
//    List<DynaBean> rows = this.query("SELECT * FROM information_schema.tables " +
//            "WHERE table_name = '"+tableName+"'");
//
//    if (rows.size() == 0)
//      return false;
//    else
//      return true;
//  }

  @Override
  public void createTransactionSequence()
  {
    /*
    if (this.tableExists(this.sequenceTableName))
    {
      return;
    }
    this.execute("CREATE TABLE "+this.sequenceTableName+" (seq INTEGER IDENTITY("+Constants.STARTING_SEQUENCE_NUMBER+",1) NOT NULL," +
                                                        "  dummy INTEGER NOT NULL)");
    this.execute("INSERT INTO "+this.sequenceTableName+" (dummy) VALUES(5)");
    */
    if (this.tableExists(this.objectSequenceTableName))
    {
      return;
    }
    this.execute("CREATE TABLE "+this.transactionSequenceTableName+" (seq INTEGER NOT NULL)");
    this.execute("INSERT INTO "+this.transactionSequenceTableName+" VALUES(1)");
  }

  /**
   *
   * @see com.runwaysdk.dataaccess.AbstractDatabase#resetTransactionSequence()
   */
  @Override
  public void resetTransactionSequence()
  {
    if (LocalProperties.isRunwayEnvironment())
    {
      this.execute("DROP TABLE " + this.transactionSequenceTableName);
      this.createTransactionSequence();
    }
    else
    {
      String errorMsg = "Reseting the transaction sequence only during runway development testing.";
      throw new UnsupportedOperationException(errorMsg);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public synchronized String getNextTransactionSequence()
  {
    this.nextSequenceNumberLock.lock();

    Statement statement = null;
    ResultSet resultSet = null;

    try
    {
      String nextSeq = "";

      String sqlStmt = null;
      try
      {
        // update the sequence
        // update the sequence
        if (this.conn == null || this.conn.isClosed())
        {
          this.conn = Database.getConnection();
        }

        sqlStmt = "UPDATE "+this.transactionSequenceTableName+" SET seq = seq+1";
        statement = this.conn.createStatement();
        statement.executeUpdate(sqlStmt);

        sqlStmt = "SELECT seq AS nextseq FROM "+this.transactionSequenceTableName;

        resultSet = statement.executeQuery(sqlStmt);

        resultSet.next();

        nextSeq = resultSet.getString("nextseq");

        statement.close();
        this.conn.commit();

      }
      catch (SQLException ex)
      {
        this.throwDatabaseException(ex, sqlStmt);

        try
        {
          if (resultSet != null)
            resultSet.close();
          if (statement != null)
            statement.close();
          this.conn.close();
        }
        catch (SQLException e)
        {
          this.throwDatabaseException(e);
        }
        finally
        {
          this.conn = null;
        }
      }
      // get the sequence value
      return nextSeq;

    }
    finally
    {
      try
      {
        if (this.conn != null)
        {
          if (resultSet != null)
            resultSet.close();
          if (statement != null)
            statement.close();
        }
      }
      catch (SQLException e)
      {
        this.throwDatabaseException(e);
      }

      nextSequenceNumberLock.unlock();
    }
  }

  @Override
  public void createObjectSequence()
  {
    /*
    if (this.tableExists(this.sequenceTableName))
    {
      return;
    }
    this.execute("CREATE TABLE "+this.sequenceTableName+" (seq INTEGER IDENTITY("+Constants.STARTING_SEQUENCE_NUMBER+",1) NOT NULL," +
                                                        "  dummy INTEGER NOT NULL)");
    this.execute("INSERT INTO "+this.sequenceTableName+" (dummy) VALUES(5)");
    */
    if (this.tableExists(this.objectSequenceTableName))
    {
      return;
    }
    this.execute("CREATE TABLE "+this.objectSequenceTableName+" (seq INTEGER NOT NULL)");
    this.execute("INSERT INTO "+this.objectSequenceTableName+" VALUES("+Database.STARTING_SEQUENCE_NUMBER+")");
  }

  @SuppressWarnings("unchecked")
  @Override
  public synchronized String getNextSequenceNumber()
  {
    this.nextSequenceNumberLock.lock();

    Statement statement = null;
    ResultSet resultSet = null;

    try
    {
      String nextSeq = "";

      String sqlStmt = null;
      try
      {
        // update the sequence
        // update the sequence
        if (this.conn == null || this.conn.isClosed())
        {
          this.conn = Database.getConnection();
        }

        sqlStmt = "UPDATE "+this.objectSequenceTableName+" SET seq = seq+1";
        statement = this.conn.createStatement();
        statement.executeUpdate(sqlStmt);

        sqlStmt = "SELECT seq AS nextseq FROM "+this.objectSequenceTableName;

        resultSet = statement.executeQuery(sqlStmt);

        resultSet.next();

        nextSeq = resultSet.getString("nextseq");

        statement.close();
        this.conn.commit();

      }
      catch (SQLException ex)
      {
        this.throwDatabaseException(ex, sqlStmt);

        try
        {
          if (resultSet != null)
            resultSet.close();
          if (statement != null)
            statement.close();
          this.conn.close();
        }
        catch (SQLException e)
        {
          this.throwDatabaseException(e);
        }
        finally
        {
          this.conn = null;
        }
      }
      // get the sequence value
      return nextSeq;


   // Heads up:
//         try
//         {
//           List<DynaBean> results = null;
   //
//           String sqlStmt = null;
//           try
//           {
//             // update the sequence
//             // update the sequence
//             if (this.conn == null || this.conn.isClosed())
//             {
//               this.conn = Database.getConnection();
//             }
   //
//             sqlStmt = "UPDATE "+this.sequenceTableName+" SET seq = seq+1";
//             statement = this.conn.createStatement();
//             statement.executeUpdate(sqlStmt);
   //
//             sqlStmt = "SELECT seq AS nextseq FROM "+this.sequenceTableName;
   //
//             resultSet = statement.executeQuery(sqlStmt);
//             RowSetDynaClass rsdc = this.getRowSetDynaClass(resultSet, true);
   //
//             statement.close();
//             this.conn.commit();
   //
//             results = rsdc.getRows();
//           }
//           catch (SQLException ex)
//           {
//             this.throwDatabaseException(ex, sqlStmt);
   //
//             try
//             {
//               if (resultSet != null)
//                 resultSet.close();
//               if (statement != null)
//                 statement.close();
//               this.conn.close();
//             }
//             catch (SQLException e)
//             {
//               this.throwDatabaseException(e);
//             }
//             finally
//             {
//               this.conn = null;
//             }
//           }
//           // get the sequence value
//           return ( results.get(0) ).get("nextseq").toString();


    }
    finally
    {
      try
      {
        if (this.conn != null)
        {
          if (resultSet != null)
            resultSet.close();
          if (statement != null)
            statement.close();
        }
      }
      catch (SQLException e)
      {
        this.throwDatabaseException(e);
      }

      nextSequenceNumberLock.unlock();
    }
  }

  @Override
  public String formatColumnAlias(String columnAlias)
  {
    return "AS " + columnAlias;
  }

  /**
   * Creates an alias in the syntax of the specific database vendor for
   * a fictitous column of the given datatype.  This allows Select statements to
   * be created with extra columns that do not exist on a table.  This is useful for
   * performing a UNION between two select statements.
   * @param columnAlias
   * @param datatype core column datatype.
   * @return given String column alias formatted to the syntax of the database vendor.
   */
  public String formatColumnAlias(String _columnAlias, String dataType)
  {
    String columnAlias = _columnAlias;

    String bogusValue = "";

    // Format quotes
    if ( // Primitives
        dataType.equals(MdAttributeCharacterInfo.CLASS)       ||
        dataType.equals(MdAttributeTextInfo.CLASS)            ||
        dataType.equals(MdAttributeClobInfo.CLASS)            ||
        dataType.equals(MdAttributeStructInfo.CLASS)          ||
        dataType.equals(MdAttributeLocalCharacterInfo.CLASS)  ||
        dataType.equals(MdAttributeLocalTextInfo.CLASS)       ||
        dataType.equals(MdAttributeDateTimeInfo.CLASS)        ||
        dataType.equals(MdAttributeDateInfo.CLASS)            ||
        dataType.equals(MdAttributeTimeInfo.CLASS)            ||
        // Encryption
        dataType.equals(MdAttributeHashInfo.CLASS)            ||
        dataType.equals(MdAttributeSymmetricInfo.CLASS)       ||
        // References
        dataType.equals(MdAttributeReferenceInfo.CLASS)       ||
        dataType.equals(MdAttributeTermInfo.CLASS)            ||
        dataType.equals(MdAttributeFileInfo.CLASS)            ||
        dataType.equals(MdAttributeEnumerationInfo.CLASS)     ||
        // Non Primitives
        dataType.equals(MdAttributeBlobInfo.CLASS))
    {
      bogusValue = "''";
    }
    // Don't format attributes of these types.
    else if (// Primitive
        dataType.equals(MdAttributeIntegerInfo.CLASS)   ||
        dataType.equals(MdAttributeLongInfo.CLASS)      ||
        dataType.equals(MdAttributeFloatInfo.CLASS)     ||
        dataType.equals(MdAttributeDoubleInfo.CLASS)    ||
        dataType.equals(MdAttributeDecimalInfo.CLASS)   ||
        dataType.equals(MdAttributeBooleanInfo.CLASS))
    {
      bogusValue = "0";
    }
    else
    {
      String error = "Database layer does not recognize attribute type [" + dataType + "]";
      throw new DatabaseException(error);
    }

    return bogusValue + " " +  this.formatColumnAlias(columnAlias);
  }

  @Override
  public String formatJavaToSQL(String value, String dataType, boolean ignoreCase)
  {
    String sqlStmt = value;

    if (sqlStmt == null)
    {
      return "NULL";
    }

    if (dataType.equals(MdAttributeCharacterInfo.CLASS)       ||
        dataType.equals(MdAttributeTextInfo.CLASS)            ||
        dataType.equals(MdAttributeClobInfo.CLASS))
    {
      if (sqlStmt.equals(""))
      {
        return "NULL";
      }
    }
    else
    {
      if (sqlStmt.trim().equals(""))
      {
        return "NULL";
      }
    }

    // Escape all characters that are harmful to an SQL statement
    sqlStmt = escapeSQLCharacters(sqlStmt);

    // Format quotes
    if ( // Primitives
        dataType.equals(MdAttributeCharacterInfo.CLASS)       ||
        dataType.equals(MdAttributeDateTimeInfo.CLASS)        ||
        dataType.equals(MdAttributeDateInfo.CLASS)            ||
        dataType.equals(MdAttributeTimeInfo.CLASS)            ||
        dataType.equals(MdAttributeTextInfo.CLASS)            ||
        dataType.equals(MdAttributeClobInfo.CLASS)            ||
        dataType.equals(MdAttributeStructInfo.CLASS)          ||
        dataType.equals(MdAttributeLocalCharacterInfo.CLASS)  ||
        dataType.equals(MdAttributeLocalTextInfo.CLASS)       ||
        // Encryption
        dataType.equals(MdAttributeHashInfo.CLASS)            ||
        dataType.equals(MdAttributeSymmetricInfo.CLASS)       ||
        // References
        dataType.equals(MdAttributeReferenceInfo.CLASS)       ||
        dataType.equals(MdAttributeTermInfo.CLASS)            ||
        dataType.equals(MdAttributeEnumerationInfo.CLASS))
    {
      sqlStmt = "'" + sqlStmt + "'";

      // only character data has mixed case
      if (ignoreCase)
      {
        sqlStmt = "UPPER(" + sqlStmt + ")";
      }
    }
    // Don't format attributes of these types.
    else if (// Primitive
        dataType.equals(MdAttributeBooleanInfo.CLASS)   ||
        dataType.equals(MdAttributeIntegerInfo.CLASS)   ||
        dataType.equals(MdAttributeLongInfo.CLASS)      ||
        dataType.equals(MdAttributeFloatInfo.CLASS)     ||
        dataType.equals(MdAttributeDoubleInfo.CLASS)    ||
        dataType.equals(MdAttributeDecimalInfo.CLASS)   ||
        // Non Primitives
        dataType.equals(MdAttributeBlobInfo.CLASS)) {}
    else
    {
      String error = "Database layer does not recognize attribute type [" + dataType + "]";
      throw new DatabaseException(error);
    }

    return sqlStmt;
  }


  /**
   * Throws the approprate exception based on the severity of the error.  Some DB errors indicate a
   * bug in the core.
   * @param ex  SQLException thrown.
   * @param sqlStmt SQL statement that caused the exception to be thrown.
   */
  public void throwDatabaseException(SQLException ex, String debugMsg)
  {
    String errorCode = new String( new Integer(ex.getErrorCode()).toString() );
    String errorMessage = ex.getMessage();

    errorCode = errorCode.trim();

    if (errorCode.equals("2601"))
    {
//      int startIndex = errorMessage.indexOf(".")+1;
//      int endIndex = errorMessage.indexOf("'", startIndex);
//      String tableName = errorMessage.substring(startIndex, endIndex);

      int startIndex = errorMessage.indexOf("unique index '")+14;
      int endIndex = errorMessage.indexOf("'", startIndex);
      String indexName = errorMessage.substring(startIndex, endIndex);

      if (indexName.substring(0, 4).equalsIgnoreCase(MdRelationshipDAOIF.INDEX_PREFIX))
      {
        String error = "Constraint ["+indexName+"] on relationship violated";

        throw new DuplicateGraphPathException(error);
      }
      else
      {
        String error = "Constraint ["+indexName+"] on object violated";

        throw new DuplicateDataDatabaseException(error, ex, indexName);
      }

    }

    if (errorCode.equals("512"))
    {
      String errMsg = "Subquery returns more than 1 row";
      throw new SubSelectReturnedMultipleRowsException(errMsg);
    }

    if (DatabaseProperties.isSeriousError(errorCode))
    {
      throw new ProgrammingErrorException(debugMsg, ex);
    }
    else
    {
      throw new DatabaseException(errorMessage, debugMsg);
    }
  }

  /**
   * Returns true if, in order to produce a meaningful error message, the database must
   * manually check uniqueness constraints, rather than relying on the database.  Some
   * databases do not return enough useful information in the error message to produce
   * a meaningful message to the end user.
   *
   * @return true must manually check uniqueness constraints for the given database,
   * false otherwise.

  public boolean manuallyCheckForDuplicates()
  {
    return true;
  } */

  /**
   * Builds a database specific string position function call.
   *
   * @param stringToFind string to find in hte search string
   * @param searchString starting position.
   * @return database specific string position function call.
   */
  public String buildStringPositionFunctionCall(String stringToFind, String searchString)
  {
    return "CHARINDEX('"+stringToFind+"', "+searchString+")";
  }

  /**
   * Builds a database specific trim function call string.
   *
   * @param columnName name of the column to trim.
   * @return a database specific trim function call string.
   */
  public String buildTrimFunctionCall(String columnName)
  {
    return "RTRIM(LTRIM("+columnName+"))";
  }

  /**
   * Builds a database specific length function call.
   *
   * @param columnName column to calculate the length on.
   * @return database specific length function call.
   */
  public String buildLenthFunctionCall(String columnName)
  {
    return "LEN("+columnName+")";
  }

  /**
   *Formats an SQL time value to a Java String.
   *
   * @param value
   * @return
   */
  public String formatSQLToJavaTime(String value)
  {
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIME_FORMAT);
    java.util.Date date = sdf.parse(value, new ParsePosition(0));
    sdf = new SimpleDateFormat(Constants.TIME_FORMAT);
    return sdf.format(date);
  }

  /**
   *Formats an SQL date value to a Java String.
   *
   * @param value
   * @param dataType
   * @param ignoreCase
   * @return
   */
  public String formatSQLToJavaDate(String value)
  {
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIME_FORMAT);
    java.util.Date date = sdf.parse(value, new ParsePosition(0));
    sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
    return sdf.format(date);
  }

  public String formatCharacterField( String type, String length)
  {
    return  type + "(" + length + ") " + COLLATE_LATIN;
  }

 /**
  * Returns a list of string names of the attributes that participate in a group  index for
  * the given table with the index of the given name.
  * @param table
  * @param indexName
  */
 public List<String> getGroupIndexAttributes(String table, String indexName)
 {
   Connection conn = Database.getConnection();
   return this.getGroupIndexAttributesFromIndexName(table, indexName, conn);
 }

 /**
  * Returns a list of string names of the attributes that participate in a
  * group unique with the given name.
  *
  * @param indexName
  * @param conn it is up to the client to manage the connection object.
  * @param attributeNames
  */
 public List<String> getGroupIndexAttributesFromIndexName(String table, String indexName, Connection conn)
 {
   String sqlStmt = "sp_helpindex "+table;

   ResultSet resultSet =  this.query(sqlStmt, conn);

   List<String> attributeNames = new LinkedList<String>();

   try
   {
     while (resultSet.next())
     {
       String attrName = resultSet.getString("index_keys").toLowerCase();
  //     String indexType =  ( ((DynaBean)result.get(i)).get("index_description").toString() ).toLowerCase();
       String keyName = resultSet.getString("index_name").toLowerCase();

  //     if (keyName.equals(indexName) && indexType.contains("unique"))
       if (keyName.equals(indexName))
       {
         StringTokenizer st = new StringTokenizer(attrName, ",", false);

         while (st.hasMoreElements())
         {
           attributeNames.add(((String)st.nextElement()).trim());
         }
       }
     }
   }
   catch (SQLException sqlEx1)
   {
     Database.throwDatabaseException(sqlEx1);
   }
   finally
   {
     try
     {
       java.sql.Statement statement = resultSet.getStatement();
       resultSet.close();
       statement.close();
     }
     catch (SQLException sqlEx2)
     {
       Database.throwDatabaseException(sqlEx2);
     }
   }
   return attributeNames;
 }

// Heads up:
// public List<String> getGroupIndexAttributesFromIndexName(String table, String indexName, Connection conn)
// {
//   String statement = "sp_helpindex "+table;
//
//   List<DynaBean> result =  this.query(statement, conn);
//
//   List<String> attributeNames = new LinkedList<String>();
//
//   for (int i=0; i<result.size(); i++)
//   {
//     String attrName = ( result.get(i).get("index_keys").toString() ).toLowerCase();
////     String indexType =  ( ((DynaBean)result.get(i)).get("index_description").toString() ).toLowerCase();
//     String keyName = ( result.get(i).get("index_name").toString() ).toLowerCase();
//
////     if (keyName.equals(indexName) && indexType.contains("unique"))
//     if (keyName.equals(indexName))
//     {
//       StringTokenizer st = new StringTokenizer(attrName, ",", false);
//
//       while (st.hasMoreElements())
//       {
//         attributeNames.add(((String)st.nextElement()).trim());
//       }
//     }
//   }
//   return attributeNames;
// }

 /**
  * @see com.runwaysdk.dataaccess.database.Database#uniqueAttributeExists(String, String, String);
  */
  @Override
  public boolean uniqueAttributeExists(String table, String columnName, String indexName)
  {
    String sqlStmt = "sp_helpindex "+table;

    ResultSet resultSet = query(sqlStmt);

    boolean returnResult = false;

    try
    {
      while (resultSet.next())
      {
        /* column name */
        String attrName = resultSet.getString("index_keys").toLowerCase();
        String indexType =  resultSet.getString("index_description").toLowerCase();
        String keyName = resultSet.getString("index_name").toLowerCase();
        if (keyName.equals(indexName) && attrName.equals(columnName.toLowerCase()) && indexType.contains("unique"))
        {
          returnResult = true;
        }
      }
    }
    catch (SQLException sqlEx1)
    {
      Database.throwDatabaseException(sqlEx1);
    }
    finally
    {
      try
      {
        java.sql.Statement statement = resultSet.getStatement();
        resultSet.close();
        statement.close();
      }
      catch (SQLException sqlEx2)
      {
        Database.throwDatabaseException(sqlEx2);
      }
    }

    return returnResult;
  }
// Heads up:
//  @Override
//  public boolean uniqueAttributeExists(String table, String field)
//  {
//    String indexName = this.uniqueAttributeIndexName(table, field);
//
//    String sqlStmt = "sp_helpindex "+table;
//    List result =  this.query(sqlStmt);
//
//    for (int i=0; i<result.size(); i++)
//    {
//      /* column name */String attrName =( ((DynaBean)result.get(i)).get("index_keys").toString() ).toLowerCase();
//      String indexType =  ( ((DynaBean)result.get(i)).get("index_description").toString() ).toLowerCase();
//      String keyName =( ((DynaBean)result.get(i)).get("index_name").toString() ).toLowerCase();
//      if (keyName.equals(indexName) && attrName.equals(field.toLowerCase()) && indexType.contains("unique"))
//      {
//        return true;
//      }
//    }
//    return false;
//  }

  /**
   * @see com.runwaysdk.dataaccess.database.Database#nonUniqueAttributeExists(String, String, String);
   */
  @Override
  public boolean nonUniqueAttributeExists(String table, String columnName, String indexName)
  {
    String sqlStmt = "sp_helpindex "+table;
    ResultSet resultSet = query(sqlStmt);

    boolean returnResult = false;

    try
    {
      while (resultSet.next())
      {
        /* column name */
        String attrName = resultSet.getString("index_keys").toLowerCase();
        String keyName = resultSet.getString("index_name").toLowerCase();
        if (keyName.equals(indexName) && attrName.equals(columnName.toLowerCase()))
        {
          returnResult = true;
        }
      }
    }
    catch (SQLException sqlEx1)
    {
      Database.throwDatabaseException(sqlEx1);
    }
    finally
    {
      try
      {
        java.sql.Statement statement = resultSet.getStatement();
        resultSet.close();
        statement.close();
      }
      catch (SQLException sqlEx2)
      {
        Database.throwDatabaseException(sqlEx2);
      }
    }

    return returnResult;
  }
//  Heads up:
//  @Override
//  public boolean nonUniqueAttributeExists(String table, String field)
//  {
//    String indexName = this.uniqueAttributeIndexName(table, field);
//    String statement = "sp_helpindex "+table;
//    List result =  this.query(statement);
//
//    for (int i=0; i<result.size(); i++)
//    {
//      /* column name */String attrName =( ((DynaBean)result.get(i)).get("index_keys").toString() ).toLowerCase();
//      String keyName =( ((DynaBean)result.get(i)).get("index_name").toString() ).toLowerCase();
//      if (keyName.equals(indexName) && attrName.equals(field.toLowerCase()))
//      {
//        return true;
//      }
//    }
//    return false;
//  }

  /**
   * Returns true if the given index exists on the given table, false otherwise.
   *
   * @param table
   * @param indexName
   * @return true if the given index exists on the given table, false otherwise.
   */
  public boolean indexExists(String table, String indexName)
  {
    String sqlStmt = "sp_helpindex " + table;

    ResultSet resultSet = query(sqlStmt);

    boolean returnResult = false;

    try
    {
      while (resultSet.next())
      {
        String keyName = resultSet.getString("index_name").toLowerCase();

        if (keyName.equals(indexName) )
        {
          returnResult = true;
        }
      }
    }
    catch (SQLException sqlEx1)
    {
      Database.throwDatabaseException(sqlEx1);
    }
    finally
    {
      try
      {
        java.sql.Statement statement = resultSet.getStatement();
        resultSet.close();
        statement.close();
      }
      catch (SQLException sqlEx2)
      {
        Database.throwDatabaseException(sqlEx2);
      }
    }

    return returnResult;
  }

//  Heads up:
//  /**
//   * Returns true if the given index exists on the given table, false otherwise.
//   *
//   * @param table
//   * @param indexName
//   * @return true if the given index exists on the given table, false otherwise.
//   */
//  public boolean indexExists(String table, String indexName)
//  {
//    String sqlStmt = "sp_helpindex " + table;
//
//    List result = this.query(sqlStmt);
//
//    for (int i = 0; i < result.size(); i++)
//    {
//      String keyName = ( ( (DynaBean) result.get(i) ).get("index_name").toString() ).toLowerCase();
//      if (keyName.equals(indexName))
//      {
//        return true;
//      }
//    }
//    return false;
//  }

  /**
   * Returns true if indexes need to be rebuilt if a column is modified, false
   * otherwise. Some databases don't like it when you alter a column that has an
   * index on it.
   *
   * @return true if indexes need to be rebuilt if a column is modified, false
   *         otherwise.
   */
  public boolean rebuildIndexOnModifyColumn()
  {
    return true;
  }

  @Override
  public boolean groupAttributeIndexExists(String table, String indexName, List<String> attributeColumnNames)
  {
    String sqlStmt = "sp_helpindex "+table;

    ResultSet resultSet = query(sqlStmt);

    List<String> attributeNameResultList = new LinkedList<String>();

    try
    {
      while (resultSet.next())
      {
        String attrName = resultSet.getString("index_keys").toLowerCase();
  //      String indexType =  resultSet.getString("index_description").toLowerCase();
        String keyName = resultSet.getString("index_name").toLowerCase();

        // strip whitespace and convert to array
        attrName = attrName.replaceAll(" ","");
        String[] tempNames = attrName.split(",");

        for(int j=0; j<tempNames.length; j++)
        {
  //        if (keyName.equals(indexName) && attributeColumnNames.contains(tempNames[j]) && indexType.contains("unique"))
          if (keyName.equals(indexName) && attributeColumnNames.contains(tempNames[j]))
          {
            attributeNameResultList.add(tempNames[j]);
          }
        }
      }

    }
    catch (SQLException sqlEx1)
    {
      Database.throwDatabaseException(sqlEx1);
    }
    finally
    {
      try
      {
        java.sql.Statement statement = resultSet.getStatement();
        resultSet.close();
        statement.close();
      }
      catch (SQLException sqlEx2)
      {
        Database.throwDatabaseException(sqlEx2);
      }
    }

    if (attributeColumnNames.size() != attributeNameResultList.size())
    {
      return false;
    }
    else
    {
      return true;
    }

  }
// Heads up:
//  @Override
//  public boolean groupAttributeIndexExists(String table, String indexName, List<String> attributeColumnNames)
//  {
//    String statement = "sp_helpindex "+table;
//
//    List result =  this.query(statement);
//
//    List<String> attributeNameResultList = new LinkedList<String>();
//
//    for (int i=0; i<result.size(); i++)
//    {
//      String attrName =( ((DynaBean)result.get(i)).get("index_keys").toString() ).toLowerCase();
////      String indexType =  ( ((DynaBean)result.get(i)).get("index_description").toString() ).toLowerCase();
//      String keyName =( ((DynaBean)result.get(i)).get("index_name").toString() ).toLowerCase();
//
//      // strip whitespace and convert to array
//      attrName = attrName.replaceAll(" ","");
//      String[] tempNames = attrName.split(",");
//
//      for(int j=0; j<tempNames.length; j++)
//      {
////        if (keyName.equals(indexName) && attributeColumnNames.contains(tempNames[j]) && indexType.contains("unique"))
//        if (keyName.equals(indexName) && attributeColumnNames.contains(tempNames[j]))
//        {
//          attributeNameResultList.add(tempNames[j]);
//        }
//      }
//
//    }
//
//    if (attributeColumnNames.size() != attributeNameResultList.size())
//    {
//      return false;
//    }
//    else
//    {
//      return true;
//    }
//  }

  @Override
  public boolean groupAttributeIndexExists(String table, String indexName)
  {
    String sqlStmt = "sp_helpindex "+table;

    ResultSet resultSet = query(sqlStmt);

    boolean indexExists = false;

    try
    {
      while (resultSet.next())
      {
        String attrName = resultSet.getString("index_keys").toLowerCase();
        String keyName = resultSet.getString("index_name").toLowerCase();

        // strip whitespace and convert to array
        attrName = attrName.replaceAll(" ","");
        String[] tempNames = attrName.split(",");

        for(int j=0; j<tempNames.length; j++)
        {
          if (keyName.equals(indexName))
          {
            indexExists = true;
            break;
          }
        }
      }

    }
    catch (SQLException sqlEx1)
    {
      Database.throwDatabaseException(sqlEx1);
    }
    finally
    {
      try
      {
        java.sql.Statement statement = resultSet.getStatement();
        resultSet.close();
        statement.close();
      }
      catch (SQLException sqlEx2)
      {
        Database.throwDatabaseException(sqlEx2);
      }
    }

    return indexExists;
  }

//  Heads up:
//  @Override
//  public boolean groupAttributeIndexExists(String table, String indexName)
//  {
//    String statement = "sp_helpindex "+table;
//
//    List result =  this.query(statement);
//
//    boolean indexExists = false;
//
//    for (int i=0; i<result.size(); i++)
//    {
//      String attrName =( ((DynaBean)result.get(i)).get("index_keys").toString() ).toLowerCase();
//      String keyName =( ((DynaBean)result.get(i)).get("index_name").toString() ).toLowerCase();
//
//      // strip whitespace and convert to array
//      attrName = attrName.replaceAll(" ","");
//      String[] tempNames = attrName.split(",");
//
//      for(int j=0; j<tempNames.length; j++)
//      {
//        if (keyName.equals(indexName))
//        {
//          indexExists = true;
//          break;
//        }
//      }
//
//    }
//
//    return indexExists;
//  }

  @Override
  public void buildDynamicPropertiesTable()
  {
    String statement = "CREATE TABLE " + Database.PROPERTIES_TABLE + " ( " +
      EntityDAOIF.ID_COLUMN + " CHAR(" + Database.DATABASE_ID_SIZE + ") " + COLLATE_LATIN + "  NOT NULL," +
      Database.VERSION_NUMBER + " CHAR(255) NOT NULL  PRIMARY KEY)";

    String undo = "DROP TABLE " + Database.PROPERTIES_TABLE;

    new DDLCommand(statement, undo, false).doIt();
    
    new DDLCommand("INSERT INTO " + Database.PROPERTIES_TABLE + "(" + EntityDAOIF.ID_COLUMN + ", " + Database.VERSION_NUMBER + ") VALUES ('" + Database.RUNWAY_VERSION_PROPERTY + "', '" + RunwayVersion.getCurrentVersion().toString() + "');", "", false).doIt();
  }

  /**
   * Builds a database specific concat function call string.
   *
   * @param concatString1 name of the original string.
   * @param concatString2 starting position.
   * @return a database specific concat function call string.
   */
  public String buildConcatFunctionCall(String concatString1, String concatString2)
  {
    return concatString1 + " + " + concatString2;
  }

  /**
   * Surrounds the given SQL statement with more SQL that will limit the range of rows returned.
   *
   * @param sqlStmt
   * @param limit number of rows to limit.
   * @param skip number of rows to skip from the beginning of the result.
   * @param selectClauseAttributes used by some databases.
   * @param orderByClause used by some databases.
   * @return
   */
  public StringBuffer buildRowRangeRestriction(StringBuffer sqlStmt, int limit, int skip, String selectClauseAttributes, String orderByClause)
  {
    /*
SELECT RowNumber, * FROM
(
  SELECT ROW_NUMBER() OVER (ORDER BY id ASC) 'RowNumber', * FROM
  (
    SELECT id
    FROM  metadata
    UNION ALL
    SELECT id
    FROM  metadata
  ) i
) j
WHERE RowNumber BETWEEN 5 AND 10
     */
    StringBuffer limitSqlStmt = new StringBuffer("");

    limitSqlStmt.append("SELECT * FROM (\n");
    limitSqlStmt.append("SELECT ROW_NUMBER() OVER ("+orderByClause+") AS 'rn', * FROM (\n");
    limitSqlStmt.append(sqlStmt);
    limitSqlStmt.append("\n) i ");
    int startAt = skip+1;
    int endUntil = skip+limit;
    limitSqlStmt.append("\n) j \n WHERE rn BETWEEN "+startAt+" AND "+endUntil);
    return limitSqlStmt;
  }

  ////////////////////////////////////////////////////////////////
  //////// Relationships
  ////////////////////////////////////////////////////////////////


  /**
   * @see com.runwaysdk.dataaccess.database.relationship.AbstractDatabase#getChildCountForParent(java.lang.String, java.lang.String)
   */
  public long getChildCountForParent(String parent_id, String relationshipTableName )
  {
    String query = " SELECT COUNT(*) AS CT \n" +
                   " FROM "+relationshipTableName+" \n"+
                   " WHERE "+RelationshipDAOIF.PARENT_ID_COLUMN+" = '"+parent_id+"' \n"+
                   " AND "+RelationshipDAOIF.CHILD_ID_COLUMN+" IN "+
                   "   (SELECT DISTINCT "+RelationshipDAOIF.CHILD_ID_COLUMN+" \n"+
                   "    FROM "+relationshipTableName+" \n"+
                   "    WHERE "+RelationshipDAOIF.PARENT_ID_COLUMN +" = '"+parent_id+"')";

    ResultSet resultSet = this.query(query);

    long returnValue = 0;

    try
    {
      if (resultSet.next())
      {
        Long number = (Long) resultSet.getLong("ct");
        returnValue = number.longValue();
      }
    }
    catch (SQLException sqlEx1)
    {
      Database.throwDatabaseException(sqlEx1);
    }
    finally
    {
      try
      {
        java.sql.Statement statement = resultSet.getStatement();
        resultSet.close();
        statement.close();
      }
      catch (SQLException sqlEx2)
      {
        Database.throwDatabaseException(sqlEx2);
      }
    }

    return returnValue;

// Heads up:
//    List<DynaBean> dynaBeanList = this.select(query);
//
//    if (dynaBeanList.size() == 0)
//    {
//      return 0;
//    }
//    else
//    {
//      DynaBean dynaBean = dynaBeanList.get(0);
//      Integer number = (Integer)dynaBean.get("ct");
//      return number.longValue();
//    }
  }

  /**
   * @see com.runwaysdk.dataaccess.database.relationship.AbstractDatabase#getParentCountForChild(java.lang.String, java.lang.String)
   */
  public long getParentCountForChild(String child_id, String relationshipTableName )
  {
    String query = " SELECT COUNT(*) AS CT \n" +
                   " FROM "+relationshipTableName+" \n"+
                   " WHERE "+RelationshipDAOIF.CHILD_ID_COLUMN+" = '"+child_id+"' \n"+
                   " AND "+RelationshipDAOIF.PARENT_ID_COLUMN+" IN "+
                   "   (SELECT DISTINCT "+RelationshipDAOIF.PARENT_ID_COLUMN+" \n"+
                   "    FROM "+relationshipTableName+" \n"+
                   "    WHERE "+RelationshipDAOIF.CHILD_ID_COLUMN +" = '"+child_id+"')";

    ResultSet resultSet = this.query(query);

    long returnValue = 0;

    try
    {
      if (resultSet.next())
      {
        Long number = (Long) resultSet.getLong("ct");
        returnValue = number.longValue();
      }
    }
    catch (SQLException sqlEx1)
    {
      Database.throwDatabaseException(sqlEx1);
    }
    finally
    {
      try
      {
        java.sql.Statement statement = resultSet.getStatement();
        resultSet.close();
        statement.close();
      }
      catch (SQLException sqlEx2)
      {
        Database.throwDatabaseException(sqlEx2);
      }
    }

    return returnValue;

// Heads up:
//    List<DynaBean> dynaBeanList = this.select(query);
//
//    if (dynaBeanList.size() == 0)
//    {
//      return 0;
//    }
//    else
//    {
//      DynaBean dynaBean = dynaBeanList.get(0);
//      Integer number = (Integer)dynaBean.get("ct");
//      return number.longValue();
//    }
  }

  /**
   * Backs up the install to a file name in the given location.
   *
   * @param tableNames list of tables to backup
   * @param backupFileLocation location of the backup file to generate.
   * @param backupFileRootName root of the file name (minus the file extension).
   * @param dropSchema true if backup should include commands to drop the schema
   */
  @Override
  public String backup(List<String> tableNames, String backupFileLocation, String backupFileRootName, boolean dropSchema)
  {
    throw new UnsupportedOperationException("Backup method is not yet implemented for SQLServr");
  }

  /**
   * Imports the given SQL file into the database
   *
   * @param restoreSQLFile
   * @param printStream
   */
  @Override
  public void importFromSQL(String restoreSQLFile, PrintStream printStream)
  {
    throw new UnsupportedOperationException("Backup method is not yet implemented for SQLServr");
  }

  @Override
  public String backup(String namespace, String backupFileLocation, String backupFileRootName, boolean dropSchema)
  {
    throw new UnsupportedOperationException("Backup method is not yet implemented for SQLServr");
  }
}
