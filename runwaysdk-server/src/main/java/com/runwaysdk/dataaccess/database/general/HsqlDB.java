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
package com.runwaysdk.dataaccess.database.general;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.hsqldb.jdbc.JDBCDataSource;

import com.google.inject.Inject;
import com.runwaysdk.RunwayMetadataVersion;
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
import com.runwaysdk.constants.MdAttributeUUIDInfo;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
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
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;


public class HsqlDB extends AbstractDatabase
{
  private String objectSequenceName;

  private String transactionSequenceName;

  @Inject
  public HsqlDB()
  {
    super();
    this.objectSequenceName = "objectSeq";
    this.transactionSequenceName = "transactionSeq";

    try
    {
      Context initContext = new InitialContext();
      Context envContext  = (Context)initContext.lookup("java:/comp/env");
      if (envContext == null) { throw new Exception("No Context Defined");}
      dataSource = (DataSource) envContext.lookup(DatabaseProperties.getJNDIDataSource());
    }
    catch (Exception e)
    {
      JDBCDataSource hsqlDataSource = new JDBCDataSource();
//      String path = "jdbc:hsqldb:mem:" + DatabaseProperties.getDatabaseName();
      // Database runs as a server
      String path = "jdbc:hsqldb:hsql://127.0.0.1/" + DatabaseProperties.getDatabaseName();
//      "jdbc:hsqldb:hsql://localhost/xdb
//      "jdbc:hsqldb:file:/opt/db/testdb", "sa", ""

      hsqlDataSource.setDatabase(path);
      hsqlDataSource.setUser(DatabaseProperties.getUser());
      hsqlDataSource.setPassword(DatabaseProperties.getPassword());

      this.dataSource = hsqlDataSource;

//      jdbcDataSource rootSource = new jdbcDataSource();
//      rootSource.setDatabase(path);
//      rootSource.setUser(DatabaseProperties.getRootUser());
//      rootSource.setPassword(DatabaseProperties.getRootPassword());
//      this.rootDataSource = rootSource;
    }
  }
  
  public void initializeConnection()
  {
    throw new DatabaseException("Not implemented");
  }
  
  public void initializeRootConnection(String rootUser, String rootPass, String rootDb)
  {
    throw new DatabaseException("Not implemented");
  }

  public void initialSetup(String rootUser, String rootPass, String rootDb)
  {
//	LinkedList<String> statements = new LinkedList<String>();
//	String dbName = DatabaseProperties.getDatabaseName();
//
//	statements.clear();
//    statements.add("DROP DATABASE IF EXISTS " + dbName);
//	statements.add("CREATE DATABASE " + dbName);
//
//    executeAsRoot(statements, false);
    throw new DatabaseException("executeAsRoot(String) has not been implemented yet");
  }

  /**
   * Drop the database.
   */
  @Override
  public void dropDb() {}

  /**
   * Creates the database.
   */
  @Override
  public void createDb(String rootDb) {}

  /**
   * Drops the database user.
   */
  @Override
  public void dropUser() {}

  /**
   * Creates the database user.
   */
  @Override
  public void createUser() {}

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
   * Creates a new table in the database. Automatically adds the Component.OID field as the primary
   * key.
   *
   * @param tableName The name of the new table.
   */
  public void createClassTable(String tableName)
  {
    String statement = startCreateClassTable(tableName)+" "+endCreateClassTable(tableName);

    String undo = "DROP TABLE " + tableName;

    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * Returns the SQL string for a new table in the database for a class, minus the closing parenthesis.
   * Automatically adds the Component.OID field as the primary key.
   *
   * @param tableName The name of the new table.
   */
  public String startCreateClassTable(String tableName)
  {
    return "CREATE TABLE " + tableName +
    " ( "+EntityDAOIF.ID_COLUMN+" CHAR(" + Database.DATABASE_ID_SIZE + ") NOT NULL PRIMARY KEY ";
  }

  /**
   * @see com.runwaysdk.dataaccess.AbstractDatabase#createClassTableBatch(java.lang.String, java.util.List<java.lang.String>)
   */
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
   * Performs an alter table command on the given table and adds the given column definitions.
   *
   * @param tableName table name
   * @param columnNames column names
   * @param columnDefs columnDefs column definitions.
   */
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
   * Creates a new table in the database for a relationships. Automatically adds the Component.OID field as the primary
   * key.
   *
   * @param tableName The name of the new table.
   * @param index1Name The name of the 1st index used by the given table.
   * @param index2Name The name of the 1st index used by the given table.
   * @param isUnique Indicates whether the parent_oid child_oid pair should be made unique.  This should only be
   *                 done on concrete relationship types.
   */
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
   * Automatically adds the Component.OID field as the primary key.
   *
   * @param tableName  The name of the new table.
   */
  @Override
  public String startCreateRelationshipTableBatch(String tableName)
  {
    return "CREATE TABLE " + tableName +
    " ( "+EntityDAOIF.ID_COLUMN+" CHAR(" + Database.DATABASE_ID_SIZE + ") NOT NULL PRIMARY KEY, \n"+
    RelationshipDAOIF.PARENT_OID_COLUMN+"                    CHAR("+Database.DATABASE_ID_SIZE+") NOT NULL, \n"+
    RelationshipDAOIF.CHILD_OID_COLUMN+"                     CHAR("+Database.DATABASE_ID_SIZE+") NOT NULL \n";
  }

  /**
   * Creates indexes on a relationship table.
   *
   * @param tableName  The name of the new table.
   * @param index1Name The name of the 1st index used by the given table.
   * @param index2Name The name of the 1st index used by the given table.
   * @param isUnique Indicates whether the parent_oid child_oid pair should be made unique.  This should only be
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
    statement += " INDEX "+index1Name+" ON "+tableName+" ("+RelationshipDAOIF.PARENT_OID_COLUMN+", "+RelationshipDAOIF.CHILD_OID_COLUMN+")";
    String undo = "DROP INDEX "+index1Name;
    new DDLCommand(statement, undo, false).doIt();

    // Create the second index
    statement = "CREATE INDEX "+index2Name+" ON "+tableName+" ("+RelationshipDAOIF.CHILD_OID_COLUMN+")";
    undo = "DROP INDEX "+index2Name;
    new DDLCommand(statement, undo, false).doIt();
  }


  /**
   * @see com.runwaysdk.dataaccess.database.Database#createEnumerationTable(String, String);
   */
  public void createEnumerationTable(String tableName, String oid)
  {
    String statement = "CREATE TABLE "+tableName+" \n"+
        "("+MdEnumerationDAOIF.SET_ID_COLUMN+"    CHAR("+Database.DATABASE_ID_SIZE+") NOT NULL, \n"+
         MdEnumerationDAOIF.ITEM_ID_COLUMN+"      CHAR("+Database.DATABASE_ID_SIZE+") NOT NULL)";
    String undo = "DROP TABLE " + tableName;
    new DDLCommand(statement, undo, false).doIt();

    String indexName = this.createIdentifierFromId(oid);
    statement = "CREATE UNIQUE INDEX "+indexName+" ON "+tableName+
        " ("+MdEnumerationDAOIF.SET_ID_COLUMN+", "+MdEnumerationDAOIF.ITEM_ID_COLUMN+")";
    undo = "DROP INDEX "+indexName;
    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * Drops an entire table from the database for a class. An undo command is created that will
   * recreate the table if transaction management requires a rollback. However, the undo
   * will <b>not </b> recreate all of the fields in the table, only the OID.
   *
   * @param table The name of the table to drop.
   */
  public void dropClassTable(String tableName)
  {
    String statement = "DROP TABLE " + tableName;
    String undo = "CREATE TABLE " + tableName + " ( "+EntityDAOIF.ID_COLUMN+" CHAR(" + Database.DATABASE_ID_SIZE
        + ") NOT NULL PRIMARY KEY )";

    new DDLCommand(statement, undo, true).doIt();
  }

  /**
   * Drops an entire table from the database for a relationship. An undo command is created that will
   * recreate the table if transaction managaement requires a rollback. However, the undo
   * will <b>not </b> recreate all of the fields in the table, only the OID.
   *
   * @param table The name of the table to drop.
   * @param index1Name The name of the 1st index used by the given table.
   * @param index2Name The name of the 1st index used by the given tablle.
   * @param isUnique Indicates whether the parent_oid child_oid pair should be made unique.  This should only be
   *                 done on concrete relationship types.
   */
  public void dropRelationshipTable(String tableName, String index1Name, String index2Name, boolean isUnique)
  {
    String statement = "DROP INDEX "+index1Name;
    String undo = "CREATE ";
    if (isUnique)
    {
      undo += " UNIQUE ";
    }
    undo += " INDEX "+index1Name+" ON "+tableName+" ("+RelationshipDAOIF.PARENT_OID_COLUMN+", "+RelationshipDAOIF.CHILD_OID_COLUMN+")";
    new DDLCommand(statement, undo, true).doIt();

    statement = "DROP INDEX "+index2Name;
    undo = "CREATE INDEX "+index2Name+" ON "+tableName+" ("+RelationshipDAOIF.PARENT_OID_COLUMN+")";
    new DDLCommand(statement, undo, true).doIt();

    statement = "DROP TABLE " + tableName;

    undo = "CREATE TABLE " + tableName +
           " ( "+EntityDAOIF.ID_COLUMN+" CHAR(" + Database.DATABASE_ID_SIZE + ") NOT NULL PRIMARY KEY, \n"+
                 RelationshipDAOIF.PARENT_OID_COLUMN+"                    CHAR("+Database.DATABASE_ID_SIZE+") NOT NULL, \n"+
                 RelationshipDAOIF.CHILD_OID_COLUMN+"                     CHAR("+Database.DATABASE_ID_SIZE+") NOT NULL \n" +
           " )";
    new DDLCommand(statement, undo, true).doIt();
  }


  /**
   * @see com.runwaysdk.dataaccess.database.Database#dropEnumerationTable(String, String);
   */
  public void dropEnumerationTable(String tableName, String oid)
  {
    String indexName = this.createIdentifierFromId(oid);
    String statement = "DROP INDEX "+indexName;
    String undo = "CREATE UNIQUE INDEX "+indexName+" ON "+tableName+
        " ("+MdEnumerationDAOIF.SET_ID_COLUMN+", "+MdEnumerationDAOIF.ITEM_ID_COLUMN+")";
    new DDLCommand(statement, undo, true).doIt();

    statement = "DROP TABLE " + tableName;
    undo = "CREATE TABLE "+tableName+" \n"+
        "("+MdEnumerationDAOIF.SET_ID_COLUMN+"       CHAR("+Database.DATABASE_ID_SIZE+") NOT NULL, \n"+
         MdEnumerationDAOIF.ITEM_ID_COLUMN+"         CHAR("+Database.DATABASE_ID_SIZE+") NOT NULL)";

    new DDLCommand(statement, undo, true).doIt();

  }

  /**
   *
   * @param table
   * @param columnName
   * @param indexName
   */
  public void addUniqueIndex(String table, String columnName, String indexName)
  {
    String statement = "CREATE UNIQUE INDEX "+indexName+" ON "+table+"("+columnName+")";
    String undo      = "DROP INDEX "+indexName;

    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * Adds a non unique index as the given attribute to the table.
   * @param table
   * @param columnName
   * @param indexName
   */
  public void addNonUniqueIndex(String table, String columnName, String indexName)
  {
    String statement = "CREATE INDEX "+indexName+" ON "+table+"("+columnName+")";
    String undo      = "DROP INDEX "+indexName;

    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   *
   * @param table
   * @param columnName
   * @param indexName
   * @param delete
   */
  public void dropUniqueIndex(String table, String columnName, String indexName, boolean delete)
  {
    String statement = "DROP INDEX "+indexName;
    String undo = "CREATE UNIQUE INDEX "+indexName+" ON "+table+"("+columnName+")";

    new DDLCommand(statement, undo, delete).doIt();
  }

  /**
   * Drops a non unique index.
   * @param table
   * @param columnName
   * @param indexName
   * @param delete
   */
  public void dropNonUniqueIndex(String table, String columnName, String indexName, boolean delete)
  {
    String statement = "DROP INDEX "+indexName.toUpperCase();
    String undo = "CREATE INDEX "+indexName.toUpperCase()+" ON "+table+"("+columnName+")";

    new DDLCommand(statement, undo, delete).doIt();
  }

  /**
   * @see com.runwaysdk.dataaccess.database.Database#uniqueAttributeExists(String, String, String);
   */
  public boolean uniqueAttributeExists(String table, String columnName, String indexName)
  {
    String sqlStmt = "SELECT * FROM information_schema.system_indexinfo WHERE table_name = '"+table.toUpperCase()+"' "
      +"AND index_name = '"+indexName.toUpperCase()+"' AND column_name = '"+columnName.toUpperCase()+"'";

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

  /**
   * @see com.runwaysdk.dataaccess.database.Database#nonUniqueAttributeExists(String, String, String);
   */
  @Override
  public boolean nonUniqueAttributeExists(String table, String columnName, String indexName)
  {
    String sqlStmt = "SELECT * FROM information_schema.system_indexinfo WHERE table_name = '"+table.toUpperCase()+"' "
      +"AND index_name = '"+indexName.toUpperCase()+"' AND column_name = '"+columnName.toUpperCase()+"'";
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

  /**
   * Creates an index on the given table on the columns with the given names.
   * @param table name of the database table.
   * @param indexName name of the database index.
   * @param columnNames name of the database columns.
   * @param isUnique true if the index should be unique, false otherwise.
   */
  public void addGroupAttributeIndex(String tableName, String indexName, List<String> columnNames, boolean isUnique)
  {
    String statement = "ALTER TABLE "+tableName+" ADD CONSTRAINT "+indexName;

    if (isUnique)
    {
      statement += " UNIQUE ";
    }

    statement += " (";

    for (int i=0; i<columnNames.size(); i++)
    {
      if ( i != 0)
      {
        statement += ", ";
      }

      statement += columnNames.get(i);
    }

    statement += ")";
    String undo = "ALTER TABLE "+tableName+" DROP INDEX "+indexName;

    new AddGroupIndexDDLCommand(tableName, indexName, statement, undo).doIt();
  }

  /**
   * Drops the index with the given name.  The attributes and unique flag are used to rebuild the index in the
   * case of a rolledback transaction.
   * @param table name of the database table.
   * @param indexName name of the database index.
   * @param columnNames name of the database columns.
   * @param isUnique true if the index should be unique, false otherwise.
   * @param delete true if this index is being deleted in this transaction, false otherwise.  The index may
   * be deleted if an attribute is being added to it.  In that case, the value should be <code>false</code>.
   */
  public void dropGroupAttributeIndex(String tableName, String indexName, List<String> columnNames, boolean isUnique, boolean delete)
  {
    // get the index name
    String statement = "ALTER TABLE "+tableName+" DROP CONSTRAINT "+indexName;

    String undo = "CREATE ";

    if (isUnique)
    {
      undo += " UNIQUE ";
    }

    undo += " INDEX "+indexName+" ON "+tableName+" (";

    for (int i=0; i<columnNames.size(); i++)
    {
      if ( i != 0)
      {
        undo += ", ";
      }

      undo += columnNames.get(i);
    }

    undo += ")";

    new DropGroupAttributeDDLCommand(tableName, indexName, statement, undo, delete).doIt();
  }

  /**
   * Returns true if the given index exists on the given table, false otherwise.
   *
   * @param table
   * @param indexName
   * @return true if the given index exists on the given table, false otherwise.
   */
  public boolean indexExists(String table, String indexName)
  {
    String sqlStmt = "SELECT * FROM information_schema.system_indexinfo WHERE table_name = '"+table.toUpperCase()+"'" +
      " AND index_name LIKE 'SYS_IDX_"+indexName.toUpperCase()+"%'";

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

  /**
   * Returns true if a group attribute index exists with the given name and the
   * given attributes on the given table.
   *
   * @param tableName
   * @param indexName
   * @param columnNames
   */
  public boolean groupAttributeIndexExists(String table, String indexName, List<String> columnNames)
  {
    String sqlStmt = "SELECT * FROM information_schema.system_indexinfo WHERE table_name = '"
        + table.toUpperCase() + "'" + " AND index_name LIKE 'SYS_IDX_" + indexName.toUpperCase()
        + "%' AND (";
    for (int i = 0; i < columnNames.size(); i++)
    {
      sqlStmt += "column_name = '" + ( (String) columnNames.get(i) ).toUpperCase() + "'";
      if (i < ( columnNames.size() - 1 ))
        sqlStmt += " OR ";
    }
    sqlStmt += ")";

    boolean returnResult = false;

    ResultSet resultSet = query(sqlStmt);

    try
    {
      int resultCount = 0;
      while (resultSet.next())
      {
        resultCount ++;
      }

      if (resultCount == columnNames.size())
      {
        returnResult = true;
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

  /**
   * Returns true if a group attribute index exists with the given name on the given table.
   *
   * @param tableName
   * @param indexName
   */
  public boolean groupAttributeIndexExists(String table, String indexName)
  {
    String sqlStmt = "SELECT * FROM information_schema.system_indexinfo WHERE table_name = '"
        + table.toUpperCase() + "'" + " AND index_name LIKE 'SYS_IDX_" + indexName.toUpperCase()
        + "%'";

    ResultSet resultSet = query(sqlStmt);

    boolean returnResult = false;

    try
    {
      if (resultSet.next())
      {
        returnResult = true;
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

  /**
   * Returns a list of string names of the attributes that participate in a group  index for
   * the given table with the index of the given name.
   * @param table
   * @param indexName
   */
  public List<String> getGroupIndexAttributes(String table, String indexName)
  {
    List<String> attributeNames = new LinkedList<String>();

    if (table.trim().equals("") || table==null)
      return attributeNames;

    String sqlStmt = "SELECT column_name FROM information_schema.system_indexinfo WHERE index_name like 'SYS_IDX_"+indexName.toUpperCase()+"%' " +
            "AND table_name = '"+table.toUpperCase()+"'";

    ResultSet resultSet = query(sqlStmt);

    try
    {
      while (resultSet.next())
      {
        String attrName = resultSet.getString("column_name").toLowerCase();
        attributeNames.add(attrName);
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

  /**
   * Adds a floating-point field (column) to a table in the database. Creates an undo DROP
   * command in case transaction management requires a rollback.
   *
   * @param table The table that the field is being added to.
   * @param columnName The name of the new field.
   * @param type The database type of the new field.
   * @param length The total number of digits in the new field.
   * @param decimal The number of digits after the decimal in the new field.
   */
  public void addDecField(String table, String columnName, String type, String length, String decimal)
  {

    String ddlType = formatDDLDecField(type, length, decimal);
    String statement = "ALTER TABLE " + table + " ADD (" + columnName + "  " + ddlType+")";

    String undo = "ALTER TABLE " + table + " DROP " + columnName;

    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * Returns SQL to add a floating-point field (column) to a table in the database.
   *
   * @param table The table that the field is being added to.
   * @param columnName The name of the new field.
   * @param type The database type of the new field.
   * @param length The total number of digits in the new field.
   * @param decimal The number of digits after the decimal in the new field.
   */
  public String addDecFieldBatch(String table, String columnName, String type, String length, String decimal)
  {
    String ddlType = formatDDLDecField(type, length, decimal);
    return columnName + "  " + ddlType;
  }

  /**
   *
   * @see com.runwaysdk.dataaccess.AbstractDatabase#dropField(java.lang.String,
   *      java.lang.String, java.lang.String, com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO)
   */
  @Override
  public void dropField(String table, String columnName, String formattedColumnType, MdAttributeConcreteDAO mdAttributeConcreteDAO)
  {
    String statement = this.buildDropColumnString(table, columnName);

    String undo = this.buildAddColumnString(table, columnName, formattedColumnType);

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
    return "ALTER TABLE " + table + " ADD (" + columnName + " " + formattedColumnType + ")";
  }

  /**
   *
   * @see com.runwaysdk.dataaccess.AbstractDatabase#buildDropColumnString(java.lang.String,
   *      java.lang.String)
   */
  @Override
  public String buildDropColumnString(String table, String columnName)
  {
    return "ALTER TABLE " + table + " DROP " + columnName;
  }

  /**
   * Changes the size of a field in the database. Creates a backup of the original
   * field parameters in case transaction management requires a rollback.
   *
   * @param table The table containing the CHAR field.
   * @param columnName The CHAR field being modified.
   * @param newDbColumnType the new database column type formatted to the database vendor syntax.
   * @param oldDbColumnType the current database column type formatted to the database vendor syntax.
   */
  public void alterFieldType(String table, String columnName, String newDbColumnType, String oldDbColumnType)
  {
    String statement = "ALTER TABLE " + table + " ALTER COLUMN " + columnName + " " + newDbColumnType;
    String undo = "ALTER TABLE " + table + " ALTER COLUMN " + columnName + " " + oldDbColumnType;
    new DDLCommand(statement, undo, false).doIt();
  }


  /**
   * Returns a LinkedList of all tables in the database.
   *
   * @return The LinkedList of all the tables in the database.
   */
  public List<String> getTables()
  {
    LinkedList<String> tables = new LinkedList<String>();

    String sqlStmt = "SELECT table_name FROM information_schema.system_tables";

    ResultSet resultSet = query(sqlStmt);

    try
    {
      while (resultSet.next())
      {
        tables.add(resultSet.getString("table_name"));
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
    return tables;

  }

  /**
   * Returns true if a table with the given name already exists in the database,
   *   false otherwise.
   *
   * <br/><b>Precondition:</b>  tableName != null
   * <br/><b>Precondition:</b>  !tableName.trim().equals("")
   *
   * @param  tableName name of a table in the database
   * @return true if a table with the given name already exists in the database,
   *         false otherwise.
   */
  public boolean tableExists(String tableName)
  {
    String sqlStmt = "SELECT * FROM information_schema.system_tables WHERE TABLE_NAME = '"+tableName.toUpperCase()+"'";

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

  /**
   * Returns a List containing the fields in a table.
   *
   * @param table The table to get the field list from.
   * @return The List of the fields in the table.
   */
  public List<String> getColumnNames(String tableName)
  {
    String query = "SELECT column_name FROM information_schema.system_columns WHERE table_name = '"+tableName.toUpperCase()+"'";
    ResultSet resultSet = query(query);
    LinkedList<String> fields = new LinkedList<String>();

    try
    {
      while (resultSet.next())
      {
        fields.add((String)resultSet.getString("column_name"));
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
    return fields;
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
    String query =
      " SELECT column_name \n"+
      "   FROM information_schema.system_columns \n"+
      "  WHERE table_name = '"+tableName.toUpperCase()+"' \n"+
      "    AND column_name = '"+columnName.toUpperCase()+"'";
    ResultSet resultSet = query(query);

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

  /**
   *
   * @see com.runwaysdk.dataaccess.AbstractDatabase#createObjectSequence()
   */
  @Override
  public void createObjectSequence()
  {
    this.execute("CREATE SEQUENCE "+this.objectSequenceName+" AS INTEGER START WITH "+Database.STARTING_SEQUENCE_NUMBER+" INCREMENT BY 1");
  }

  /**
   *
   * @see com.runwaysdk.dataaccess.AbstractDatabase#getNextSequenceNumber()
   */
  @Override
  public String getNextSequenceNumber()
  {
    String sqlStmt = "SELECT NEXT VALUE FOR "+this.objectSequenceName+" AS nextval FROM information_schema.system_tables";

    ResultSet resultSet = query(sqlStmt);

    String returnResult = "";

    try
    {
      resultSet.next();

      return resultSet.getString("nextval");
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

  /**
   *
   * @see com.runwaysdk.dataaccess.AbstractDatabase#createTransactionSequence()
   */
  @Override
  public void createTransactionSequence()
  {
    this.execute("CREATE SEQUENCE "+this.transactionSequenceName+" AS INTEGER START WITH 1 INCREMENT BY 1");
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
      this.execute("DROP SEQUENCE "+this.transactionSequenceName+"");
      this.createTransactionSequence();
    }
    else
    {
      String errorMsg = "Reseting the transaction sequence only during runway development testing.";
      throw new UnsupportedOperationException(errorMsg);
    }
  }

  /**
   *
   * @see com.runwaysdk.dataaccess.AbstractDatabase#getNextTransactionSequence()
   */
  @Override
  public String getNextTransactionSequence()
  {
    String sqlStmt = "SELECT NEXT VALUE FOR "+this.transactionSequenceName+" AS nextval FROM information_schema.system_tables";

    ResultSet resultSet = query(sqlStmt);

    String returnResult = "";

    try
    {
      resultSet.next();

      return resultSet.getString("nextval");
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

  /**
   * Adds a field (column) to a table in the database with an alter table statement. Creates an undo DROP command in
   * case transaction management requires a rollback.
   *
   * @param table The table that the field is being added to.
   * @param columnName The name of the new field.
   * @param type The database type of the new field.
   * @param size The size of new field. <code><b>null</b></code> if the type does not
   *          require a size parameter.
   */
  public void addField(String table, String columnName, String type, String size)
  {
    String statement = "ALTER TABLE " + table + " ADD " + columnName + "  " + type;

    if (size != null)
    {
      statement += "(" + size + ")";
    }

    String undo = "ALTER TABLE " + table + " DROP " + columnName;

    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * Returns SQL to add a field (column) to a table in the database.
   *
   * @param table The table that the field is being added to.
   * @param columnName The name of the new field.
   * @param type The database type of the new field.
   * @param size The size of new field. <code><b>null</b></code> if the type does not
   *          require a size parameter.
   */
  public String addFieldBatch(String table, String columnName, String type, String size)
  {
    String statement = columnName + "  " + type;

    if (size != null)
    {
      statement += "(" + size + ")";
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

  /**
   * Returns the character type formatted for a DDL command to the vendor syntax.
   * @param type the numerical decimal type
   * @param length
   * @param decimal
   * @return
   */
  public String formatCharacterField( String type, String length)
  {
    return  type + "(" + length + ")";
  }

  /**
   * Converts the given String value and formats it to a String that can be used in a SQL
   * statement. <br>
   *
   * <br>
   * <b>Precondition: </b> value != null <br>
   * <b>Precondition: </b> dataType != null <br>
   * <b>Precondition: </b> !dataType.trim().equals("") <br>
   * <b>Precondition: </b> dataType is a valid core attribute value <br>
   * <b>Postcondition: </b> return value may not be null
   *
   * @param value value to format
   * @param dataType dataType of the value
   * @param ignoreCase if true, then wrap the formatted value in "UPPER("+value+")"
   */
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
        dataType.equals(MdAttributeBooleanInfo.CLASS)         ||
        dataType.equals(MdAttributeTextInfo.CLASS)            ||
        dataType.equals(MdAttributeClobInfo.CLASS)            ||
        dataType.equals(MdAttributeStructInfo.CLASS)          ||
        dataType.equals(MdAttributeLocalCharacterInfo.CLASS)  ||
        dataType.equals(MdAttributeLocalTextInfo.CLASS)       ||
        // Encryption
        dataType.equals(MdAttributeHashInfo.CLASS)            ||
        dataType.equals(MdAttributeSymmetricInfo.CLASS)       ||
        // References
        dataType.equals(MdAttributeReferenceInfo.CLASS) ||
        dataType.equals(MdAttributeUUIDInfo.CLASS) ||
        dataType.equals(MdAttributeTermInfo.CLASS) ||
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
   *Different databases format column aliases differently in the column clause of a select statement.
   * Returns the given String column alias formatted to the syntax of the database vendor.
   * @return given String column alias formatted to the syntax of the database vendor.
   */
  public String formatColumnAlias(String columnAlias)
  {
    return " AS "+columnAlias;
  }

  /**
   * Creates an alias in the syntax of the specific database vendor for
   * a fictitious column of the given datatype.  This allows Select statements to
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
        dataType.equals(MdAttributeReferenceInfo.CLASS) ||
        dataType.equals(MdAttributeUUIDInfo.CLASS) ||
        dataType.equals(MdAttributeTermInfo.CLASS) ||
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

  /**
   * Throws the appropriate exception based on the severity of the error.  Some DB errors indicate a
   * bug in the core.
   * @param ex  SQLException thrown.
   * @param sqlStmt SQL statement that caused the exception to be thrown.
   */
  public void throwDatabaseException(SQLException ex, String debugMsg)
  {
    String errorCode = new String( new Integer(ex.getErrorCode()).toString() );
    String errorMessage = ex.getMessage();

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
   * Sets the value of this blob as the specified bytes. This method works the
   * same as the Blob.setBytes(long pos, byte[], int offset, int length) as
   * specified in the JDBC 3.0 API. Because of this, the first element in the
   * bytes to write to is actually element 1 (as opposed to the standard array
   * treatment where the first element is at position 0).
   *
   * @param table
   * @param columnName
   * @param oid
   * @param pos
   * @param bytes
   * @param offset
   * @param length
   * @return
   */
  public int setBlobAsBytes(String table, String columnName, String oid, long pos, byte[] bytes, int offset,
      int length)
  {
    Connection conn = Database.getConnection();
    Statement statement = null;
    ResultSet resultSet = null;
    int written = 0;
    try
    {
      // get the blob
      statement = conn.createStatement();
      String select = "SELECT " + columnName + " FROM " + table + " WHERE " + EntityDAOIF.ID_COLUMN + " = '" + oid
          + "'";
      String update = "UPDATE " + table + " SET " + columnName + " = " + "? WHERE " + EntityDAOIF.ID_COLUMN + " = '"
          + oid + "'";
      resultSet = statement.executeQuery(select);
      resultSet.next();
      byte[] resultBytes = resultSet.getBytes(columnName);

      // null check
      if (resultBytes == null)
      {
        // because this method is used to place byte in specific positions, it
        // wouldn't
        // make sense to insert the bytes into a null field as it defeats the
        // purpose of
        // this method. Just return a write count of 0 and don't do anything
        // else.
        return written;
      }
      else
      {
        // modify the blob
        written = length;
        byte[] setBytes = null;

        pos = pos - 1; // subtract one to use positioning like a normal array

        // check to see if the bytes will run longer than the current length of the blob length.
        if ( ( pos + length ) > resultBytes.length)
        {
          byte[] temp = new byte[(int) ( pos + length )];
          // get the old bytes, up until pos
          for (int i = 0; i < pos; i++)
          {
            temp[i] = resultBytes[i];
          }

          // set the new bytes
          for (int i = 0; i < length; i++)
          {
            temp[(int) pos] = bytes[offset];
            offset++;
            pos++;
            written++;
          }
          setBytes = temp;
        }
        else
        {
          // set the new bytes
          for (int i = 0; i < length; i++)
          {
            resultBytes[(int) pos] = bytes[offset];
            offset++;
            pos++;
            written++;
          }
          setBytes = resultBytes;
        }

        // save the changes to the blob
        PreparedStatement prepared = conn.prepareStatement(update);
        prepared.setBytes(1, setBytes);
        prepared.executeUpdate();
      }
    }
    catch (SQLException e)
    {
      this.throwDatabaseException(e);
    }
    finally
    {
      try
      {
        if (resultSet != null)
          resultSet.close();
        if (statement != null)
          statement.close();
        this.closeConnection(conn);
      }
      catch (SQLException e)
      {
        this.throwDatabaseException(e);
      }
    }
    return written;
  }

  /**
   * Sets the value of this blob as the specified bytes.
   *
   * @param table
   * @param columnName
   * @param oid
   * @param bytes
   * @return The number of bytes written.
   */
  public int setBlobAsBytes(String table, String columnName, String oid, byte[] bytes)
  {
    Connection conn = Database.getConnection();
    PreparedStatement prepared = null;
    int written = 0;
    try
    {
      // get the blob
      String update = "UPDATE " + table + " SET " + columnName + " = " + "? WHERE " + EntityDAOIF.ID_COLUMN + " = '"
          + oid + "'";
      prepared = conn.prepareStatement(update);
      prepared.setBytes(1, bytes);
      prepared.executeUpdate();
      written = bytes.length;
    }
    catch (SQLException e)
    {
      this.throwDatabaseException(e);
    }
    finally
    {
    	try {
    		if(prepared != null)
    		{
    			prepared.close();
    		}
    		this.closeConnection(conn);
		} catch (SQLException e) {
			this.throwDatabaseException(e);
		}
    }
    return written;
  }

  /**
   * Returns the value of a blob as a byte array.
   *
   * @param table
   * @param columnName
   * @param oid
   * @return byte[] value of the blob.
   */
  @Override
  public byte[] getBlobAsBytes(String table, String columnName, String oid)
  {
    Connection conn = Database.getConnection();
    Statement statement = null;
    ResultSet resultSet = null;
    byte[] returnBytes = null;
    try
    {
      // get the blob
      statement = conn.createStatement();
      String query = "SELECT " + columnName + " FROM " + table + " WHERE " + EntityDAOIF.ID_COLUMN + " = '" + oid + "'";
      resultSet = statement.executeQuery(query);
      resultSet.next();

      returnBytes = resultSet.getBytes(columnName);
    }
    catch (SQLException e)
    {
      this.throwDatabaseException(e);
    }
    finally
    {
      try
      {
        if (resultSet != null)
          resultSet.close();
        if (statement != null)
          statement.close();
        this.closeConnection(conn);
      }
      catch (SQLException e)
      {
        this.throwDatabaseException(e);
      }
    }

    return returnBytes;
  }

  /**
   * Returns the value of a blob as a byte array. It is up to the client to close the
   * database connection.
   *
   * @param table
   * @param columnName
   * @param oid
   * @param conn
   * @return byte[] value of the blob.
   */
  public byte[] getBlobAsBytes(String table, String columnName, String oid, Connection conn)
  {
    Statement statement = null;
    ResultSet resultSet = null;
    byte[] returnBytes = null;
    try
    {
      // get the blob
      statement = conn.createStatement();
      String query = "SELECT " + columnName + " FROM " + table + " WHERE " + EntityDAOIF.ID_COLUMN + " = '" + oid + "'";
      resultSet = statement.executeQuery(query);
      resultSet.next();

      returnBytes = resultSet.getBytes(columnName);
    }
    catch (SQLException e)
    {
      this.throwDatabaseException(e);
    }
    finally
    {
      try
      {
        if (resultSet != null)
          resultSet.close();
        if (statement != null)
          statement.close();
      }
      catch (SQLException e)
      {
        this.throwDatabaseException(e);
      }
    }

    return returnBytes;
  }

  /**
   * Returns the value of a blob as a byte array. This method allows you to
   * specify a start position in the blob (where the first element starts at
   * position 1 to comply with the JDBC 3.0 API) and the total length
   * (inclusive) beyond the start position to return.
   *
   * @param table
   * @param columnName
   * @param oid
   * @param pos
   * @param length
   * @return
   */
  public byte[] getBlobAsBytes(String table, String columnName, String oid, long pos, int length)
  {
    Connection conn = Database.getConnection();
    Statement statement = null;
    ResultSet resultSet = null;
    byte[] returnBytes = null;
    try
    {
      // get the blob
      statement = conn.createStatement();
      String query = "SELECT " + columnName + " FROM " + table + " WHERE " + EntityDAOIF.ID_COLUMN + " = '" + oid + "'";
      resultSet = statement.executeQuery(query);
      resultSet.next();

      byte[] resultBytes = resultSet.getBytes(columnName);
      byte[] temp = new byte[length];
      pos = pos - 1;
      for (int i = 0; i < length; i++)
      {
        temp[i] = resultBytes[(int) pos];
        pos++;
      }
      returnBytes = temp;
    }
    catch (SQLException e)
    {
      this.throwDatabaseException(e);
    }
    finally
    {
      try
      {
        if (resultSet != null)
          resultSet.close();
        if (statement != null)
          statement.close();
        this.closeConnection(conn);
      }
      catch (SQLException e)
      {
        this.throwDatabaseException(e);
      }
    }
    return returnBytes;
  }

  /**
   * Truncates a blob by the specified length.
   *
   * @param table
   * @param columnName
   * @param oid
   * @param length
   */
  public void truncateBlob(String table, String columnName, String oid, long length, Connection conn)
  {
    Statement statement = null;
    ResultSet resultSet = null;
    try
    {
      // get the blob
      statement = conn.createStatement();
      String select = "SELECT " + columnName + " FROM " + table + " WHERE " + EntityDAOIF.ID_COLUMN + " = '" + oid
          + "'";
      String update = "UPDATE " + table + " SET " + columnName + " = " + "? WHERE " + EntityDAOIF.ID_COLUMN + " = '"
          + oid + "'";
      resultSet = statement.executeQuery(select);
      resultSet.next();
      byte[] resultBytes = resultSet.getBytes(columnName);

      // truncate the bytes
      byte[] temp = new byte[(int)length];
      for (int i = 0; i < length; i++)
      {
        temp[i] = resultBytes[i];
      }

      // save the truncated blob
      PreparedStatement prepared = conn.prepareStatement(update);
      prepared.setBytes(1, temp);
      prepared.executeUpdate();
    }
    catch (SQLException e)
    {
      this.throwDatabaseException(e);
    }
    finally
    {
      try
      {
        if (resultSet != null)
          resultSet.close();
        if (statement != null)
          statement.close();
      }
      catch (SQLException e)
      {
        this.throwDatabaseException(e);
      }
    }
  }

  /**
   * Returns the blob as an array of bytes.
   *
   * @param table
   * @param columnName
   * @param oid
   * @return The byte array value of this blob attribute.
   */
  @Override
  public long getBlobSize(String table, String columnName, String oid)
  {
    Connection conn = Database.getConnection();
    Statement statement = null;
    ResultSet resultSet = null;
    long size = 0;
    try
    {
      // get the blob
      statement = conn.createStatement();
      String query = "SELECT " + columnName + " FROM " + table + " WHERE " + EntityDAOIF.ID_COLUMN + " = '" + oid + "'";
      resultSet = statement.executeQuery(query);
      resultSet.next();

      byte[] bytes = resultSet.getBytes(columnName);
      if(bytes != null)
      {
        size = resultSet.getBytes(columnName).length;
      }
    }
    catch (SQLException e)
    {
      this.throwDatabaseException(e);
    }
    finally
    {
      try
      {
        if (resultSet != null)
          resultSet.close();
        if (statement != null)
          statement.close();
        this.closeConnection(conn);
      }
      catch (SQLException e)
      {
        this.throwDatabaseException(e);
      }
    }
    return size;
  }

  /**
   * This is a special method used to update the baseClass attribute of MdType and it is used only within
   * the TransactionManagement aspect, hence it takes a JDBC connection object as a parameter.
   * @param mdTypeId
   * @param table
   * @param classColumnName
   * @param classBytes
   * @param sourceColumnName
   * @param source
   * @param conn
   */
  @Override
  public int updateClassAndSource(String mdTypeId, String table, String classColumnName, byte[] classBytes, String sourceColumnName, String source, Connection conn)
  {
    int written = 0;

    PreparedStatement prepared = null;
    try
    {
      // clear the blob
      this.truncateBlob(table, classColumnName, mdTypeId, 0, conn);

      // get the blob
      String update = "UPDATE " + table + " SET " + classColumnName + " = " + "?, " + sourceColumnName
          + " = ? WHERE " + EntityDAOIF.ID_COLUMN + " = '" + mdTypeId + "'";
      prepared = conn.prepareStatement(update);
      prepared.setBytes(1, classBytes);
      prepared.setString(2, source);
      prepared.executeUpdate();
      written = classBytes.length;
    }
    catch (SQLException e)
    {
      this.throwDatabaseException(e);
    }
    finally
    {
      try
      {
        if (prepared != null)
          prepared.close();

      }
      catch (SQLException e)
      {
        this.throwDatabaseException(e);
      }
    }
    return written;
  }

  /**
   * This is a special method used to update the generated server, common, and client classes for an MdType.
   * This method is used only within the TransactionManagement aspect, hence it takes a JDBC connection object as a parameter.
   * It is up to the client to close the connection object.
   *
   * @param table
   * @param updateTable
   * @param serverClassesColumnName
   * @param serverClassesBytes
   * @param commonClassesColumnName
   * @param commonClassesBytes
   * @param clientClassesColumnName
   * @param clientClassesBytes
   * @param conn
   */
  @Override
  public int updateMdFacadeGeneratedClasses(String mdFacadeId, String table,
      String serverClassesColumnName, byte[] serverClassesBytes,
      String commonClassesColumnName, byte[] commonClassesBytes,
      String clientClassesColumnName, byte[] clientClassesBytes,
      Connection conn)
  {
    int written = 0;

    PreparedStatement prepared = null;
    try
    {
      // clear the blob
      this.truncateBlob(table, serverClassesColumnName, mdFacadeId, 0, conn);

      // get the blob
      String update =
        "UPDATE " + table + " SET "
        + serverClassesColumnName + " = " + "?, " + commonClassesColumnName + " = ?, " + clientClassesColumnName + " = ? "+
        " WHERE " + EntityDAOIF.ID_COLUMN + " = '" + mdFacadeId + "'";
      prepared = conn.prepareStatement(update);
      prepared.setBytes(1, serverClassesBytes);
      prepared.setBytes(2, commonClassesBytes);
      prepared.setBytes(3, clientClassesBytes);
      prepared.executeUpdate();
      written += serverClassesBytes.length;
      written += commonClassesBytes.length;
      written += clientClassesBytes.length;
    }
    catch (SQLException e)
    {
      this.throwDatabaseException(e);
    }
    finally
    {
      try
      {
        if (prepared != null)
          prepared.close();

      }
      catch (SQLException e)
      {
        this.throwDatabaseException(e);
      }
    }
    return written;
  }

  @Override
  public void buildDynamicPropertiesTable()
  {
    String statement = "CREATE TABLE " + Database.PROPERTIES_TABLE + " ( " +
    EntityDAOIF.ID_COLUMN + " CHAR(" + Database.DATABASE_ID_SIZE + ") NOT NULL," +
    Database.VERSION_NUMBER + " CHAR(16) NOT NULL PRIMARY KEY)";

    String undo = "DROP TABLE " + Database.PROPERTIES_TABLE;

    new DDLCommand(statement, undo, false).doIt();
    new DDLCommand("INSERT INTO " + Database.PROPERTIES_TABLE + "(" + EntityDAOIF.ID_COLUMN + ", " + Database.VERSION_NUMBER + ") VALUES ('" + Database.RUNWAY_METADATA_VERSION_PROPERTY + "', '" + RunwayMetadataVersion.getCurrentVersion().toString() + "');", "", false).doIt();
  }
  

  @Override
  public void buildChangelogTable()
  {
    StringBuffer statement = new StringBuffer();
    statement.append("CREATE TABLE changelog ( change_number BIGINT NOT NULL, complete_dt TIMESTAMP NOT NULL, applied_by VARCHAR(100) NOT NULL, description VARCHAR(500) NOT NULL);");

    new DDLCommand(statement.toString(), "DROP TABLE changelog", false).doIt();
    new DDLCommand("ALTER TABLE changelog ADD CONSTRAINT Pkchangelog PRIMARY KEY (change_number);", "", false).doIt();
  }


  ////////////////////////////////////////////////////////////////
  //////// Relationships
  ////////////////////////////////////////////////////////////////

  /**
   * @see com.runwaysdk.dataaccess.database.relationship.AbstractDatabase#getChildCountForParent(java.lang.String, java.lang.String)
   */
  public long getChildCountForParent(String parent_oid, String relationshipTableName)
  {
    String query = " SELECT COUNT(*) AS ct \n" + " FROM " + relationshipTableName + " \n" +
    " WHERE " + RelationshipDAOIF.PARENT_OID_COLUMN + " = '" + parent_oid + "' \n" +
    " AND " + RelationshipDAOIF.CHILD_OID_COLUMN + " IN " +
    "   (SELECT DISTINCT " + RelationshipDAOIF.CHILD_OID_COLUMN + " \n" +
    "    FROM " + relationshipTableName + " \n" +
    "    WHERE " + RelationshipDAOIF.PARENT_OID_COLUMN + " = '" + parent_oid + "')";

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
  }

  /**
   * @see com.runwaysdk.dataaccess.database.relationship.AbstractDatabase#getParentCountForChild(java.lang.String, java.lang.String)
   */
  public long getParentCountForChild(String child_oid, String relationshipTableName)
  {
    String query = " SELECT COUNT(*) AS ct \n" +
                   " FROM "+relationshipTableName+" \n"+
                   " WHERE "+RelationshipDAOIF.CHILD_OID_COLUMN+" = '"+child_oid+"' \n"+
                   " AND "+RelationshipDAOIF.PARENT_OID_COLUMN+" IN "+
                   "   (SELECT DISTINCT "+RelationshipDAOIF.PARENT_OID_COLUMN+" \n"+
                   "    FROM "+relationshipTableName+" \n"+
                   "    WHERE "+RelationshipDAOIF.CHILD_OID_COLUMN +" = '"+child_oid+"')";

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
  public String backup(List<String> tableNames, String backupFileLocation, String backupFileRootName,  PrintStream out, PrintStream errOut, boolean dropSchema)
  {
    throw new UnsupportedOperationException("Backup method is not yet implemented for HsqlDB");
  }

  /**
   * Imports the given SQL file into the database
   *
   * @param restoreSQLFile
   * @param printStream
   */
  @Override
  public void importFromSQL(String restoreSQLFile, PrintStream out, PrintStream errOut)
  {
    throw new UnsupportedOperationException("Backup method is not yet implemented for HsqlDB");
  }

  @Override
  public String backup(String namespace, String backupFileLocation, String backupFileRootName,  PrintStream out, PrintStream errOut, boolean dropSchema)
  {
    throw new UnsupportedOperationException("Backup method is not yet implemented for HsqlDB");
  }

  @Override
  public void close()
  {
    throw new UnsupportedOperationException("Close method is not yet implemented for HsqlDB");
  }

  @Override
  public void createTempTable(String tableName, List<String> columns, String onCommit)
  {
    throw new UnsupportedOperationException("createTempTable method is not yet implemented for HsqlDB");
  }

}
