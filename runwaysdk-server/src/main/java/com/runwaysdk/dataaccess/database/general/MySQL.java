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
 * Created on Jun 23, 2005
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
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.dbcp.datasources.SharedPoolDataSource;

import com.google.inject.Inject;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.runwaysdk.RunwayException;
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
import com.runwaysdk.constants.MdAttributeFileInfo;
import com.runwaysdk.constants.MdAttributeFloatInfo;
import com.runwaysdk.constants.MdAttributeHashInfo;
import com.runwaysdk.constants.MdAttributeIndicatorInfo;
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
import com.runwaysdk.dataaccess.database.DuplicateDataDatabaseException;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.query.SubSelectReturnedMultipleRowsException;

/**
 * Concrete implementation of <a href="http://www.mysql.com/>mysql </a> in the
 * Database class family. Contains mysql-specific implemetation of certain
 * methods.
 * 
 * @author Eric
 * @version $Revision 1.0 $
 * @since
 */
public class MySQL extends AbstractDatabase
{
  /**
   * Name of the table used to define a MySQL sequence.
   */
  private String              objectSequenceTableName;

  private String              transactionSequenceTableName;

  // @GuardedBy("this")
  private Connection          conn;

  private final ReentrantLock nextSequenceNumberLock;

  /**
   * Initialize the datasource to point to a MySQL database.
   */
  @Inject
  public MySQL()
  {
    super();

    this.nextSequenceNumberLock = new ReentrantLock();
    this.objectSequenceTableName = "object_seq_table";
    this.transactionSequenceTableName = "transaction_seq_table";

    // The container is not providing a pooled datasource
    if (this.dataSource == null)
    {
      // We subtract 1 because we'll reserve a connection for sequence numbers
      int maxDbConnections = DatabaseProperties.getMaxConnections() - 1;

      if (maxDbConnections < 2)
      {
        maxDbConnections = 2;
      }

      boolean pooling = DatabaseProperties.getConnectionPooling();

      MysqlDataSource mysqlDataSource = null;
      if (pooling)
      {
        mysqlDataSource = new MysqlConnectionPoolDataSource();
      }
      else
      {
        mysqlDataSource = new MysqlDataSource();
      }

      // useServerPrepStmts=false
      // jdbc:mysql://[host][,failoverhost...][:port]/[database]
      // [?propertyName1][=propertyValue1][&propertyName2][=propertyValue2]...
      String url = "jdbc:mysql://" + DatabaseProperties.getServerName() + ":" + DatabaseProperties.getPort() + "/" + DatabaseProperties.getDatabaseName() + "?useServerPrepStmts=false";

      // is-connection-validation-required=true s
      // ?connectTimeout=1000"
      mysqlDataSource.setURL(url);
      mysqlDataSource.setUser(DatabaseProperties.getUser());
      mysqlDataSource.setPassword(DatabaseProperties.getPassword());

      /*
       * Alternate method for setting up the connection
       * mysqlDataSource.setServerName(vendorProps.getString(this.serverName));
       * mysqlDataSource.setPort(portNumber);
       * mysqlDataSource.setDatabaseName(vendorProps
       * .getString(this.databaseName));
       * mysqlDataSource.setUser(vendorProps.getString(this.user));
       * mysqlDataSource.setPassword(vendorProps.getString(this.password));
       */

      if (pooling)
      {
        SharedPoolDataSource sharedPoolDataSource = new SharedPoolDataSource();
        sharedPoolDataSource.setConnectionPoolDataSource((MysqlConnectionPoolDataSource) mysqlDataSource);
        sharedPoolDataSource.setMaxActive(maxDbConnections);
        sharedPoolDataSource.setTestOnBorrow(true);
        sharedPoolDataSource.setValidationQuery("SELECT 1");
        // sharedPoolDataSource.setMaxWait(50);
        this.dataSource = sharedPoolDataSource;
      }
      // If environment is not configured for connection pooling, do not pool
      // connections
      // This does not actually create connection pooling. It is used by the app
      // server.
      else
      {
        this.dataSource = mysqlDataSource;
      }
    }
  }

  /**
   * Installs the runway core. This entails creating a new database, creating a
   * user for the runway to log in with, and setting any necessary permissions.
   */
  public void initialSetup(String rootUser, String rootPass, String rootDb)
  {
    // Set up the root connection
    String rootURL = "jdbc:mysql://" + DatabaseProperties.getServerName() + ":" + DatabaseProperties.getPort() + "/" + rootDb + "?useServerPrepStmts=false";
    MysqlDataSource rootSource = new MysqlDataSource();
    rootSource.setURL(rootURL);
    rootSource.setUser(rootUser);
    rootSource.setPassword(rootPass);
    this.rootDataSource = rootSource;

    this.dropUser();

    this.dropDb();

    this.createDb(rootDb);

    this.createUser();
  }

  /**
   * Drop the database.
   */
  @Override
  public void dropDb()
  {
    String dbName = DatabaseProperties.getDatabaseName();
    LinkedList<String> statements = new LinkedList<String>();
    statements.add("DROP DATABASE IF EXISTS " + dbName);
    executeAsRoot(statements, false);
  }

  /**
   * Creates the database.
   */
  @Override
  public void createDb(String rootDb)
  {
    String dbName = DatabaseProperties.getDatabaseName();
    LinkedList<String> statements = new LinkedList<String>();
    statements.add("CREATE DATABASE " + dbName + " CHARACTER SET utf8");
    executeAsRoot(statements, false);
  }

  /**
   * Drops the database user.
   */
  @Override
  public void dropUser()
  {
    String userName = DatabaseProperties.getUser();
    String host = DatabaseProperties.getServerName();
    LinkedList<String> statements = new LinkedList<String>();
    try
    {
      statements.add("DROP USER " + userName + "@" + host);
      executeAsRoot(statements, false);
    }
    catch (DatabaseException e)
    {
      // This happens if the user doesn't exist already. Keep going.
    }
  }

  /**
   * Creates the database user.
   */
  @Override
  public void createUser()
  {
    String userName = DatabaseProperties.getUser();
    String pass = DatabaseProperties.getPassword();
    String host = DatabaseProperties.getServerName();
    String dbName = DatabaseProperties.getDatabaseName();

    LinkedList<String> statements = new LinkedList<String>();

    // mysql 5 syntax
    statements.add("CREATE USER '" + userName + "'@'" + host + "' IDENTIFIED BY '" + pass + "'");

    statements.add("FLUSH PRIVILEGES");
    statements.add("GRANT ALL PRIVILEGES ON " + dbName + ".* TO " + userName + "@'" + host + "'");
    statements.add("FLUSH PRIVILEGES");

    executeAsRoot(statements, false);
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
   * Creates a new table in the database. Automatically adds the Component.OID
   * field as the primary key.
   * 
   * @param tableName
   *          The name of the new table.
   */
  public void createClassTable(String tableName)
  {
    String statement = startCreateClassTable(tableName) + " " + endCreateClassTable(tableName);

    String undo = "DROP TABLE " + tableName;

    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * Returns the SQL string for a new table in the database for a class, minus
   * the closing parenthesis. Automatically adds the Component.OID field as the
   * primary key.
   * 
   * @param tableName
   *          The name of the new table.
   */
  public String startCreateClassTable(String tableName)
  {
    return "CREATE TABLE " + tableName + " ( " + EntityDAOIF.ID_COLUMN + " CHAR(" + Database.DATABASE_ID_SIZE + ") BINARY NOT NULL PRIMARY KEY";
  }

  /**
   * @see 
   *      com.runwaysdk.dataaccess.AbstractDatabase#createClassTableBatch(java.lang
   *      .String, java.util.List<java.lang.String>)
   */
  @Override
  public void createClassTableBatch(String tableName, List<String> columnDefs)
  {
    String statement = startCreateClassTable(tableName);
    for (String columnDef : columnDefs)
    {
      statement += "\n," + columnDef;
    }
    statement += " " + endCreateClassTable(tableName);

    String undo = "DROP TABLE " + tableName;

    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * Performs an alter table command on the given table and adds the given
   * column definitions.
   * 
   * @param tableName
   *          table name
   * @param columnNames
   *          column names
   * @param columnDefs
   *          columnDefs column definitions.
   */
  public void alterClassTableBatch(String tableName, List<String> columnNames, List<String> columnDefs)
  {
    String statement = "ALTER TABLE " + tableName + " ADD (";

    String undo = "ALTER TABLE " + tableName + " DROP (";

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

      statement += " " + columnDef;
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

      undo += " " + columnName;
    }
    undo += ")";

    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * Returns the SQL string that concludes a table definition. Typically it is a
   * closing parenthesis.
   * 
   * @param tableName
   *          The name of the new table.
   */
  public String endCreateClassTable(String tableName)
  {
    return ") ENGINE = InnoDB";
  }

  /**
   * Creates a new table in the database for a relationships. Automatically adds
   * the Component.OID field as the primary key.
   * 
   * @param tableName
   *          The name of the new table.
   * @param index1Name
   *          The name of the 1st index used by the given table.
   * @param index2Name
   *          The name of the 1st index used by the given table.
   * @param isUnique
   *          Indicates whether the parent_oid child_oid pair should be made
   *          unique. This should only be done on concrete relationship types.
   */
  public void createRelationshipTable(String tableName, String index1Name, String index2Name, boolean isUnique)
  {
    String statement = this.startCreateRelationshipTableBatch(tableName) + " " + this.endCreateClassTable(tableName);

    String undo = "DROP TABLE " + tableName;
    new DDLCommand(statement, undo, false).doIt();

    this.createRelationshipTableIndexesBatch(tableName, index1Name, index2Name, isUnique);
  }

  /**
   * Creates a new table in the database for relationship, including all columns
   * for that table.
   * 
   * @param tableName
   *          table name
   * @param columnDefs
   *          columnDefs column definitions.
   */
  public void createRelationshipTableBatch(String tableName, List<String> columnDefs)
  {
    String statement = startCreateRelationshipTableBatch(tableName);
    for (String columnDef : columnDefs)
    {
      statement += "\n," + columnDef;
    }
    statement += " " + endCreateClassTable(tableName);

    String undo = "DROP TABLE " + tableName;

    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * Returns the SQL string for a new table in the database for a relationship,
   * minus the closing parenthesis. Automatically adds the Component.OID field as
   * the primary key.
   * 
   * @param tableName
   *          The name of the new table.
   */
  @Override
  public String startCreateRelationshipTableBatch(String tableName)
  {
    return "CREATE TABLE " + tableName + " ( " + EntityDAOIF.ID_COLUMN + " CHAR(" + Database.DATABASE_ID_SIZE + ") BINARY NOT NULL PRIMARY KEY, \n" + RelationshipDAOIF.PARENT_OID_COLUMN + "                    CHAR(" + Database.DATABASE_ID_SIZE + ") BINARY NOT NULL, \n" + RelationshipDAOIF.CHILD_OID_COLUMN + "                     CHAR(" + Database.DATABASE_ID_SIZE + ") BINARY NOT NULL \n";
  }

  /**
   * Creates indexes on a relationship table.
   * 
   * @param tableName
   *          The name of the new table.
   * @param index1Name
   *          The name of the 1st index used by the given table.
   * @param index2Name
   *          The name of the 1st index used by the given table.
   * @param isUnique
   *          Indicates whether the parent_oid child_oid pair should be made
   *          unique. This should only be done on concrete relationship types.
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
    statement += " INDEX " + index1Name + " ON " + tableName + " (" + RelationshipDAOIF.PARENT_OID_COLUMN + ", " + RelationshipDAOIF.CHILD_OID_COLUMN + ")";
    String undo = "ALTER TABLE " + tableName + " DROP INDEX " + index1Name;
    new DDLCommand(statement, undo, false).doIt();

    // Create the second index
    statement = "CREATE INDEX " + index2Name + " ON " + tableName + " (" + RelationshipDAOIF.CHILD_OID_COLUMN + ")";
    undo = "ALTER TABLE " + tableName + " DROP INDEX " + index2Name;
    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * @see com.runwaysdk.dataaccess.database.Database#createEnumerationTable(String,
   *      String);
   */
  public void createEnumerationTable(String tableName, String oid)
  {
    String statement = "CREATE TABLE " + tableName + " \n" + "(" + MdEnumerationDAOIF.SET_ID_COLUMN + "                CHAR(" + Database.DATABASE_SET_ID_SIZE + ") BINARY NOT NULL, \n" + MdEnumerationDAOIF.ITEM_ID_COLUMN + " CHAR(" + Database.DATABASE_ID_SIZE + ") BINARY NOT NULL) \n" + "ENGINE = InnoDB;";
    String undo = "DROP TABLE " + tableName;
    new DDLCommand(statement, undo, false).doIt();

    String indexName = this.createIdentifierFromId(oid);
    statement = "CREATE UNIQUE INDEX " + indexName + " ON " + tableName + " (" + MdEnumerationDAOIF.SET_ID_COLUMN + ", " + MdEnumerationDAOIF.ITEM_ID_COLUMN + ")";
    undo = "ALTER TABLE " + tableName + " DROP INDEX " + indexName;
    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * Drops an entire table from the database for a class. An undo command is
   * created that will recreate the table if transaction management requires a
   * rollback. However, the undo will <b>not </b> recreate all of the fields in
   * the table, only the OID.
   * 
   * @param table
   *          The name of the table to drop.
   */
  public void dropClassTable(String tableName)
  {
    String statement = "DROP TABLE " + tableName;
    String undo = "CREATE TABLE " + tableName + " ( " + EntityDAOIF.ID_COLUMN + " CHAR(" + Database.DATABASE_ID_SIZE + ") BINARY NOT NULL PRIMARY KEY ) ENGINE = InnoDB ";

    new DDLCommand(statement, undo, true).doIt();
  }

  /**
   * Drops an entire table from the database for a relationship. An undo command
   * is created that will recreate the table if transaction management requires
   * a rollback. However, the undo will <b>not </b> recreate all of the fields
   * in the table, only the OID.
   * 
   * @param table
   *          The name of the table to drop.
   * @param index1Name
   *          The name of the 1st index used by the given table.
   * @param index2Name
   *          The name of the 1st index used by the given tablle.
   * @param isUnique
   *          Indicates whether the parent_oid child_oid pair should be made
   *          unique. This should only be done on concrete relationship types.
   */
  public void dropRelationshipTable(String tableName, String index1Name, String index2Name, boolean isUnique)
  {
    String statement = "ALTER TABLE " + tableName + " DROP INDEX " + index1Name;
    String undo = "CREATE ";
    if (isUnique)
    {
      undo += " UNIQUE ";
    }
    undo += " INDEX " + index1Name + " ON " + tableName + " (" + RelationshipDAOIF.PARENT_OID_COLUMN + ", " + RelationshipDAOIF.CHILD_OID_COLUMN + ")";
    new DDLCommand(statement, undo, true).doIt();

    statement = "ALTER TABLE " + tableName + " DROP INDEX " + index2Name;
    undo = "CREATE INDEX " + index2Name + " ON " + tableName + " (" + RelationshipDAOIF.PARENT_OID_COLUMN + ")";
    new DDLCommand(statement, undo, true).doIt();

    statement = "DROP TABLE " + tableName;

    undo = "CREATE TABLE " + tableName + " ( " + EntityDAOIF.ID_COLUMN + " CHAR(" + Database.DATABASE_ID_SIZE + ") BINARY NOT NULL PRIMARY KEY, \n" + RelationshipDAOIF.PARENT_OID_COLUMN + "                    CHAR(" + Database.DATABASE_ID_SIZE + ") BINARY NOT NULL, \n" + RelationshipDAOIF.CHILD_OID_COLUMN + "                     CHAR(" + Database.DATABASE_ID_SIZE + ") BINARY NOT NULL \n" + " ) ENGINE = InnoDB ";
    new DDLCommand(statement, undo, true).doIt();
  }

  /**
   * @see com.runwaysdk.dataaccess.database.Database#dropEnumerationTable(String,
   *      String);
   */
  public void dropEnumerationTable(String tableName, String oid)
  {
    String indexName = this.createIdentifierFromId(oid);
    String statement = "ALTER TABLE " + tableName + " DROP INDEX " + indexName;
    String undo = "CREATE UNIQUE INDEX " + indexName + " ON " + tableName + " (" + MdEnumerationDAOIF.SET_ID_COLUMN + ", " + MdEnumerationDAOIF.ITEM_ID_COLUMN + ")";
    new DDLCommand(statement, undo, true).doIt();

    statement = "DROP TABLE " + tableName;
    undo = "CREATE TABLE " + tableName + " \n" + "(" + MdEnumerationDAOIF.SET_ID_COLUMN + "                CHAR(" + Database.DATABASE_SET_ID_SIZE + ") BINARY NOT NULL, \n" + MdEnumerationDAOIF.ITEM_ID_COLUMN + " CHAR(" + Database.DATABASE_ID_SIZE + ") BINARY NOT NULL) \n" + "ENGINE = InnoDB;";
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
    String statement = "ALTER TABLE " + table + " ADD UNIQUE " + indexName + " (" + columnName + ")";
    String undo = "ALTER TABLE " + table + " DROP INDEX " + indexName;

    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * Adds a non unique index as the given attribute to the table.
   * 
   * @param table
   * @param columnName
   * @param indexName
   */
  public void addNonUniqueIndex(String table, String columnName, String indexName)
  {
    String statement = "ALTER TABLE " + table + " ADD INDEX " + indexName + " (" + columnName + ")";
    String undo = "ALTER TABLE " + table + " DROP INDEX " + indexName;

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
    String statement = "ALTER TABLE " + table + " DROP INDEX " + indexName;
    String undo = "ALTER TABLE " + table + " ADD UNIQUE " + indexName + " (" + columnName + ")";

    new DDLCommand(statement, undo, delete).doIt();
  }

  /**
   * Drops a non unique index.
   * 
   * @param table
   * @param columnName
   * @param indexName
   * @param delete
   */
  public void dropNonUniqueIndex(String table, String columnName, String indexName, boolean delete)
  {
    String statement = "ALTER TABLE " + table + " DROP INDEX " + indexName;
    String undo = "ALTER TABLE " + table + " ADD INDEX " + indexName + " (" + columnName + ")";

    new DDLCommand(statement, undo, delete).doIt();
  }

  /**
   * @see com.runwaysdk.dataaccess.database.Database#uniqueAttributeExists(String,
   *      String, String);
   */
  public boolean uniqueAttributeExists(String table, String columnName, String indexName)
  {
    String sqlStmt = "SHOW INDEX FROM " + table;
    ResultSet resultSet = query(sqlStmt);

    boolean returnResult = false;

    try
    {
      while (resultSet.next())
      {
        String attrName = resultSet.getString("column_name").toLowerCase();
        String keyName = resultSet.getString("key_name").toLowerCase();
        String nonUnique = resultSet.getString("non_unique").toLowerCase();

        if (keyName.equals(indexName) && attrName.equals(columnName.toLowerCase()) && nonUnique.equals("0"))
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

  /**
   * @see com.runwaysdk.dataaccess.database.Database#nonUniqueAttributeExists(String,
   *      String, String);
   */
  public boolean nonUniqueAttributeExists(String table, String columnName, String indexName)
  {
    String sqlStmt = "SHOW INDEX FROM " + table;
    ResultSet resultSet = query(sqlStmt);

    boolean returnResult = false;

    try
    {
      while (resultSet.next())
      {
        String attrName = resultSet.getString("column_name").toLowerCase();
        String keyName = resultSet.getString("key_name").toLowerCase();
        String nonUnique = resultSet.getString("non_unique").toLowerCase();

        if (keyName.equals(indexName) && attrName.equals(columnName.toLowerCase()) && nonUnique.equals("1"))
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

  /**
   * Creates an index on the given table on the columns with the given names.
   * 
   * @param table
   *          name of the database table.
   * @param indexName
   *          name of the database index.
   * @param columnNames
   *          name of the database columns.
   * @param isUnique
   *          true if the index should be unique, false otherwise.
   */
  public void addGroupAttributeIndex(String tableName, String indexName, List<String> columnNames, boolean isUnique)
  {
    String statement = "ALTER TABLE " + tableName + " ADD ";

    if (isUnique)
    {
      statement += " UNIQUE ";
    }
    else
    {
      statement += " INDEX ";
    }

    statement += indexName + " (";

    for (int i = 0; i < columnNames.size(); i++)
    {
      if (i != 0)
      {
        statement += ", ";
      }

      statement += columnNames.get(i);
    }

    statement += ")";

    String undo = "ALTER TABLE " + tableName + " DROP INDEX " + indexName;

    new AddGroupIndexDDLCommand(tableName, indexName, statement, undo).doIt();
  }

  /**
   * Drops the index with the given name. The attributes and unique flag are
   * used to rebuild the index in the case of a rolledback transaction.
   * 
   * @param tableName
   *          name of the database table.
   * @param indexName
   *          name of the database index.
   * @param columnNames
   *          name of the database columns.
   * @param isUnique
   *          true if the index should be unique, false otherwise.
   * @param delete
   *          true if this index is being deleted in this transaction, false
   *          otherwise. The index may be deleted if an attribute is being added
   *          to it. In that case, the value should be <code>false</code>.
   */
  public void dropGroupAttributeIndex(String tableName, String indexName, List<String> columnNames, boolean isUnique, boolean delete)
  {
    String statement = "ALTER TABLE " + tableName + " DROP INDEX " + indexName;

    String undo = "ALTER TABLE " + tableName + " ADD ";

    if (isUnique)
    {
      undo += "UNIQUE ";
    }

    undo += indexName + " (";

    for (int i = 0; i < columnNames.size(); i++)
    {
      if (i != 0)
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
    String sqlStmt = "SHOW INDEX FROM " + table;

    ResultSet resultSet = query(sqlStmt);

    boolean returnResult = false;

    try
    {
      while (resultSet.next())
      {
        String keyName = resultSet.getString("key_name").toLowerCase();

        if (keyName.equals(indexName))
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
    String sqlStmt = "SHOW INDEX FROM " + table;

    ResultSet resultSet = query(sqlStmt);

    List<String> attributeNameResultList = new LinkedList<String>();

    boolean returnResult = false;

    try
    {
      while (resultSet.next())
      {
        String attrName = resultSet.getString("column_name").toLowerCase();
        String keyName = resultSet.getString("key_name").toLowerCase();

        if (keyName.equals(indexName) && columnNames.contains(attrName))
        {
          attributeNameResultList.add(attrName);
        }
      }

      if (columnNames.size() == attributeNameResultList.size())
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
   * Returns true if a group attribute index exists with the given name on the
   * given table.
   * 
   * @param tableName
   * @param indexName
   */
  public boolean groupAttributeIndexExists(String table, String indexName)
  {
    String sqlStmt = "SHOW INDEX FROM " + table;

    ResultSet resultSet = query(sqlStmt);

    boolean indexExists = false;

    try
    {
      while (resultSet.next())
      {
        // Commented out because it is never used: Is this an error?
        String keyName = resultSet.getString("key_name").toLowerCase();

        if (keyName.equals(indexName))
        {
          indexExists = true;
          break;
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

  /**
   * Returns a list of string names of the attributes that participate in a
   * group index for the given table with the index of the given name.
   * 
   * @param table
   * @param indexName
   */
  public List<String> getGroupIndexAttributes(String table, String indexName)
  {
    List<String> attributeNames = new LinkedList<String>();

    if (table.trim().equals("") || table == null)
      return attributeNames;

    String sqlStmt = "SHOW INDEX FROM " + table;

    ResultSet resultSet = query(sqlStmt);

    try
    {
      while (resultSet.next())
      {
        String attrName = resultSet.getString("column_name").toLowerCase();
        String keyName = resultSet.getString("key_name").toLowerCase();

        if (keyName.equals(indexName))
        {
          attributeNames.add(attrName);
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

  /**
   * Adds a floating-point field (column) to a table in the database. Creates an
   * undo DROP command in case transaction management requiies a rollback.
   * 
   * @param table
   *          The table that the field is being added to.
   * @param columnName
   *          The name of the new field.
   * @param type
   *          The database type of the new field.
   * @param length
   *          The total number of digits in the new field.
   * @param decimal
   *          The number of digits after the decimal in the new field.
   */
  public void addDecField(String table, String columnName, String type, String length, String decimal)
  {

    String ddlType = formatDDLDecField(type, length, decimal);
    String statement = "ALTER TABLE " + table + " ADD (" + columnName + "  " + ddlType + ")";

    String undo = "ALTER TABLE " + table + " DROP " + columnName;

    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * Returns SQL to add a floating-point field (column) to a table in the
   * database.
   * 
   * @param table
   *          The table that the field is being added to.
   * @param columnName
   *          The name of the new field.
   * @param type
   *          The database type of the new field.
   * @param length
   *          The total number of digits in the new field.
   * @param decimal
   *          The number of digits after the decimal in the new field.
   */
  public String addDecFieldBatch(String table, String columnName, String type, String length, String decimal)
  {
    String ddlType = formatDDLDecField(type, length, decimal);
    return columnName + "  " + ddlType;
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.AbstractDatabase#dropField(java.lang.String,
   *      java.lang.String, java.lang.String,
   *      com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO)
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
   * Changes the size of a field in the database. Creates a backup of the
   * original field parameters in case transaction management requires a
   * rollback.
   * 
   * @param table
   *          The table containing the CHAR field.
   * @param columnName
   *          The CHAR field being modified.
   * @param newDbColumnType
   *          the new database column type formatted to the database vendor
   *          syntax.
   * @param oldDbColumnType
   *          the current database column type formatted to the database vendor
   *          syntax.
   */
  public void alterFieldType(String table, String columnName, String newDbColumnType, String oldDbColumnType)
  {
    String statement = "ALTER TABLE " + table + " MODIFY " + columnName + " " + newDbColumnType;

    String undo = "ALTER TABLE " + table + " MODIFY " + columnName + " " + oldDbColumnType;

    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * Returns true if a table with the given name already exists in the database,
   * false otherwise.
   * 
   * <br/>
   * <b>Precondition:</b> tableName != null <br/>
   * <b>Precondition:</b> !tableName.trim().equals("")
   * 
   * @param tableName
   *          name of a table in the database
   * @return true if a table with the given name already exists in the database,
   *         false otherwise.
   */
  public boolean tableExists(String tableName)
  {
    String sqlStmt = "show tables like '" + tableName + "'";

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
   * Returns a List containing the fields in a table.
   * 
   * @param table
   *          The table to get the field list from.
   * @return The List of the fields in the table.
   */
  public List<String> getColumnNames(String tableName)
  {
    ResultSet resultSet = query("desc " + tableName);
    LinkedList<String> fields = new LinkedList<String>();

    try
    {
      while (resultSet.next())
      {
        fields.add((String) resultSet.getString("field"));
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
   * Returns true if a column with the given name exists on the table with the
   * given name, false otherwise.
   * 
   * @param columnName
   *          assumes column name is lower case.
   * @param tableName
   * 
   * @return true if a column with the given name exists on the table with the
   *         given name, false otherwise.
   */
  @Override
  public boolean columnExists(String columnName, String tableName)
  {
    ResultSet resultSet = query("desc " + tableName);

    try
    {
      while (resultSet.next())
      {
        if (resultSet.getString("field").toLowerCase().equals(columnName))
        {
          return true;
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
    return false;
  }

  /**
   * Gets the next sequence number from the database.
   * 
   * @return The next sequence number from the database.
   */
  public String getNextSequenceNumber()
  {
    this.nextSequenceNumberLock.lock();
    try
    {
      return this.getNextSequenceNumber(0);
    }
    finally
    {
      this.nextSequenceNumberLock.unlock();
    }
  }

  /**
   * Gets the next sequence number from the database.
   * 
   * @return The next sequence number from the database.
   */
  private String getNextSequenceNumber(int tries)
  {
    ResultSet resultSet = null;

    String updateSqlStmt = "UPDATE " + this.objectSequenceTableName + " SET SEQ = LAST_INSERT_ID(SEQ+1)";
    String selectSqlStmt = "SELECT LAST_INSERT_ID() nextseq";
    Statement statement = null;

    try
    {
      try
      {
        // update the sequence
        if (this.conn == null || this.conn.isClosed())
        {
          this.conn = Database.getConnection();
        }

        statement = this.conn.createStatement();

        statement.executeUpdate(updateSqlStmt);
      }
      catch (SQLException ex)
      {
        if (ex.getCause() != null && ex.getCause() instanceof java.io.EOFException)
        {
          throw ex.getCause();
        }
        else
        {
          this.throwDatabaseException(ex, updateSqlStmt);
        }
      }
      finally
      {
        if (statement != null)
        {
          statement.close();
        }
      }

      String nextSeq = "";
      try
      {
        statement = this.conn.createStatement();
        resultSet = statement.executeQuery(selectSqlStmt);

        resultSet.next();

        // get the sequence value
        nextSeq = resultSet.getString("nextseq");

      }
      catch (SQLException ex)
      {
        if (ex.getCause() != null && ex.getCause() instanceof java.io.EOFException)
        {
          throw ex.getCause();
        }
        else
        {
          this.throwDatabaseException(ex, selectSqlStmt);
        }
      }
      finally
      {
        if (resultSet != null)
        {
          resultSet.close();
        }
        if (statement != null)
        {
          statement.close();
        }
      }

      // get the sequence value
      return nextSeq;
    }
    catch (RunwayException ex)
    {
      throw ex;
    }
    // java.io.EOFException
    catch (Throwable ex)
    {
      try
      {
        this.conn.close();
      }
      catch (SQLException e)
      {
        this.throwDatabaseException(e);
      }
      this.conn = null;

      if (tries < 3)
      {
        return this.getNextSequenceNumber(tries + 1);
      }
      else
      {
        String errorMessage = "A problem occured with the MySQL resource used to update the table " + "used for sequences.  ";
        throw new ProgrammingErrorException(errorMessage, ex);
      }
    }
    finally
    {
      try
      {
        if (this.conn != null && !this.conn.isClosed())
        {
          this.conn.commit();
        }
      }
      catch (SQLException e)
      {
        this.throwDatabaseException(e);
      }
    }
  }

  /**
   * Creates a table in the database used to generate sequence numbers.
   */
  @Override
  public void createObjectSequence()
  {
    if (this.tableExists(this.objectSequenceTableName))
    {
      return;
    }
    this.execute("CREATE TABLE " + this.objectSequenceTableName + " (seq INT UNSIGNED NOT NULL) ENGINE = InnoDB");
    this.execute("INSERT INTO " + this.objectSequenceTableName + " VALUES(" + Database.STARTING_SEQUENCE_NUMBER + ")");
  }

  /**
   * Creates a table in the database used to generate sequence numbers.
   */
  @Override
  public void createTransactionSequence()
  {
    if (this.tableExists(this.transactionSequenceTableName))
    {
      return;
    }
    this.execute("CREATE TABLE " + this.transactionSequenceTableName + " (seq INT UNSIGNED NOT NULL) ENGINE = InnoDB");
    this.execute("INSERT INTO " + this.transactionSequenceTableName + " VALUES(1)");
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

  /**
   * Gets the next sequence number from the database.
   * 
   * @return The next sequence number from the database.
   */
  @Override
  public String getNextTransactionSequence()
  {
    this.nextSequenceNumberLock.lock();
    try
    {
      return this.getTransactionSequenceNumber(0);
    }
    finally
    {
      this.nextSequenceNumberLock.unlock();
    }
  }

  /**
   * Gets the next sequence number from the database.
   * 
   * @return The next sequence number from the database.
   */
  private String getTransactionSequenceNumber(int tries)
  {
    ResultSet resultSet = null;

    String updateSqlStmt = "UPDATE " + this.transactionSequenceTableName + " SET SEQ = LAST_INSERT_ID(SEQ+1)";
    String selectSqlStmt = "SELECT LAST_INSERT_ID() nextseq";
    Statement statement = null;

    try
    {
      try
      {
        // update the sequence
        if (this.conn == null || this.conn.isClosed())
        {
          this.conn = Database.getConnection();
        }

        statement = this.conn.createStatement();

        statement.executeUpdate(updateSqlStmt);
      }
      catch (SQLException ex)
      {
        if (ex.getCause() != null && ex.getCause() instanceof java.io.EOFException)
        {
          throw ex.getCause();
        }
        else
        {
          this.throwDatabaseException(ex, updateSqlStmt);
        }
      }
      finally
      {
        if (statement != null)
        {
          statement.close();
        }
      }

      String nextSeq = "";
      try
      {
        statement = this.conn.createStatement();
        resultSet = statement.executeQuery(selectSqlStmt);

        resultSet.next();

        // get the sequence value
        nextSeq = resultSet.getString("nextseq");

      }
      catch (SQLException ex)
      {
        if (ex.getCause() != null && ex.getCause() instanceof java.io.EOFException)
        {
          throw ex.getCause();
        }
        else
        {
          this.throwDatabaseException(ex, selectSqlStmt);
        }
      }
      finally
      {
        if (resultSet != null)
        {
          resultSet.close();
        }
        if (statement != null)
        {
          statement.close();
        }
      }

      // get the sequence value
      return nextSeq;
    }
    catch (RunwayException ex)
    {
      throw ex;
    }
    // java.io.EOFException
    catch (Throwable ex)
    {
      try
      {
        this.conn.close();
      }
      catch (SQLException e)
      {
        this.throwDatabaseException(e);
      }
      this.conn = null;

      if (tries < 3)
      {
        return this.getTransactionSequenceNumber(tries + 1);
      }
      else
      {
        String errorMessage = "A problem occured with the MySQL resource used to update the table " + "used for sequences.  ";
        throw new ProgrammingErrorException(errorMessage, ex);
      }
    }
    finally
    {
      try
      {
        if (this.conn != null && !this.conn.isClosed())
        {
          this.conn.commit();
        }
      }
      catch (SQLException e)
      {
        this.throwDatabaseException(e);
      }
    }
  }

  /**
   * Adds a field (column) to a table in the database with an alter table
   * statement. Creates an undo DROP command in case transaction management
   * requires a rollback.
   * 
   * @param table
   *          The table that the field is being added to.
   * @param columnName
   *          The name of the new field.
   * @param type
   *          The database type of the new field.
   * @param size
   *          The size of new field. <code><b>null</b></code> if the type does
   *          not require a size parameter.
   */
  public void addField(String table, String columnName, String type, String size)
  {
    String statement = "ALTER TABLE " + table + " ADD (" + columnName + "  " + type;

    if (size != null)
    {
      statement += "(" + size + ") BINARY";
    }
    statement += ')';

    String undo = buildDropColumnString(table, columnName);

    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * Returns SQL to add a field (column) to a table in the database.
   * 
   * @param table
   *          The table that the field is being added to.
   * @param columnName
   *          The name of the new field.
   * @param type
   *          The database type of the new field.
   * @param size
   *          The size of new field. <code><b>null</b></code> if the type does
   *          not require a size parameter.
   */
  public String addFieldBatch(String table, String columnName, String type, String size)
  {
    String statement = columnName + "  " + type;

    if (size != null)
    {
      statement += "(" + size + ") BINARY";
    }

    return statement;
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

      undo += " " + columnName + "_" + i;
    }
    undo += ")";

    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.AbstractDatabase#addField(java.lang.String,
   *      java.lang.String, java.lang.String,
   *      com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO)
   */
  @Override
  public void addField(String table, String columnName, String formattedColumnType, MdAttributeConcreteDAO mdAttributeConcreteDAO)
  {
    String statement = this.buildAddColumnString(table, columnName, formattedColumnType);

    String undo = this.buildDropColumnString(table, columnName);

    new AddColumnSingleDDLCommand(mdAttributeConcreteDAO, table, columnName, formattedColumnType, statement, undo, false).doIt();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.AbstractDatabase#addFieldBatch(java.lang.String,
   *      java.lang.String, java.lang.String,
   *      com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO)
   */
  @Override
  public String addFieldBatch(String tableName, String columnName, String formattedType, MdAttributeConcreteDAO mdAttributeConcreteDAO)
  {
    AddColumnBatchDDLCommand addColumnBatchDDLCommand = new AddColumnBatchDDLCommand(mdAttributeConcreteDAO, tableName, columnName, formattedType, false);

    addColumnBatchDDLCommand.doIt();

    return addColumnBatchDDLCommand.getColumnNameForDatabase() + "  " + formattedType;
  }

  /**
   * Returns the character type formatted for a DDL command to the vendor
   * syntax.
   * 
   * @param type
   *          the numerical decimal type
   * @param length
   * @param decimal
   * @return
   */
  @Override
  public String formatCharacterField(String type, String length)
  {
    return type + "(" + length + ") BINARY";
  }

  /**
   * Returns the text type formatted for a DDL command to the vendor syntax.
   * 
   * @param type
   *          the numerical decimal type
   * @return
   */
  @Override
  public String formatTextField(String type)
  {
    return type + " BINARY";
  }

  /**
   * Returns the CLOB type formatted for a DDL command to the vendor syntax.
   * 
   * @param type
   *          the numerical decimal type
   * @return
   */
  @Override
  public String formatClobField(String type)
  {
    return type + " BINARY";
  }

  /**
   * Different databases format column aliases differently in the column clause
   * of a select statement. Returns the given String column alias formatted to
   * the syntax of the database vendor.
   * 
   * @return given String column alias formatted to the syntax of the database
   *         vendor.
   */
  public String formatColumnAlias(String columnAlias)
  {
    return columnAlias;
  }

  /**
   * Creates an alias in the syntax of the specific database vendor for a
   * fictitious column of the given datatype. This allows Select statements to
   * be created with extra columns that do not exist on a table. This is useful
   * for performing a UNION between two select statements.
   * 
   * @param columnAlias
   * @param datatype
   *          core column datatype.
   * @return given String column alias formatted to the syntax of the database
   *         vendor.
   */
  public String formatColumnAlias(String _columnAlias, String dataType)
  {
    String columnAlias = _columnAlias;

    String bogusValue = "";

    // Format quotes
    if ( // Primitives
    dataType.equals(MdAttributeCharacterInfo.CLASS) || dataType.equals(MdAttributeTextInfo.CLASS) || dataType.equals(MdAttributeClobInfo.CLASS) || dataType.equals(MdAttributeStructInfo.CLASS) || dataType.equals(MdAttributeLocalCharacterInfo.CLASS) || dataType.equals(MdAttributeLocalTextInfo.CLASS) || dataType.equals(MdAttributeDateTimeInfo.CLASS) || dataType.equals(MdAttributeDateInfo.CLASS) || dataType.equals(MdAttributeTimeInfo.CLASS) ||
    // Encryption
        dataType.equals(MdAttributeHashInfo.CLASS) || dataType.equals(MdAttributeSymmetricInfo.CLASS) ||
        // References
        dataType.equals(MdAttributeReferenceInfo.CLASS) || dataType.equals(MdAttributeTermInfo.CLASS) ||dataType.equals(MdAttributeFileInfo.CLASS) || dataType.equals(MdAttributeEnumerationInfo.CLASS) || dataType.equals(MdAttributeIndicatorInfo.CLASS) ||
        // Non Primitives
        dataType.equals(MdAttributeBlobInfo.CLASS))
    {
      bogusValue = "''";
    }
    // Don't format attributes of these types.
    else if (// Primitive
    dataType.equals(MdAttributeIntegerInfo.CLASS) || dataType.equals(MdAttributeLongInfo.CLASS) || dataType.equals(MdAttributeFloatInfo.CLASS) || dataType.equals(MdAttributeDoubleInfo.CLASS) || dataType.equals(MdAttributeDecimalInfo.CLASS) || dataType.equals(MdAttributeBooleanInfo.CLASS))
    {
      bogusValue = "0";
    }
    else
    {
      String error = "Database layer does not recognize attribute type [" + dataType + "]";
      throw new DatabaseException(error);
    }

    return bogusValue + " " + this.formatColumnAlias(columnAlias);
  }

  /**
   * Converts the given String value and formats it to a String that can be used
   * in a SQL statement. <br>
   * 
   * <br>
   * <b>Precondition: </b> value != null <br>
   * <b>Precondition: </b> dataType != null <br>
   * <b>Precondition: </b> !dataType.trim().equals("") <br>
   * <b>Precondition: </b> dataType is a valid core attribute value <br>
   * <b>Postcondition: </b> return value may not be null
   * 
   * @param value
   *          value to format
   * @param dataType
   *          dataType of the value
   * @param ignoreCase
   *          if true, then wrap the formatted value in "UPPER("+value+")"
   */
  public String formatJavaToSQL(String value, String dataType, boolean ignoreCase)
  {
    String sqlStmt = value;

    if (sqlStmt == null)
    {
      return "NULL";
    }

    if (dataType.equals(MdAttributeCharacterInfo.CLASS) || dataType.equals(MdAttributeTextInfo.CLASS) || dataType.equals(MdAttributeClobInfo.CLASS))
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
    dataType.equals(MdAttributeCharacterInfo.CLASS) || dataType.equals(MdAttributeDateTimeInfo.CLASS) || dataType.equals(MdAttributeDateInfo.CLASS) || dataType.equals(MdAttributeTimeInfo.CLASS) || dataType.equals(MdAttributeTextInfo.CLASS) || dataType.equals(MdAttributeClobInfo.CLASS) || dataType.equals(MdAttributeStructInfo.CLASS) || dataType.equals(MdAttributeLocalCharacterInfo.CLASS) || dataType.equals(MdAttributeLocalTextInfo.CLASS) ||
    // Encryption
        dataType.equals(MdAttributeHashInfo.CLASS) || dataType.equals(MdAttributeSymmetricInfo.CLASS) ||
        // References
        dataType.equals(MdAttributeReferenceInfo.CLASS) || dataType.equals(MdAttributeTermInfo.CLASS)  || dataType.equals(MdAttributeFileInfo.CLASS) || dataType.equals(MdAttributeEnumerationInfo.CLASS) || dataType.equals(MdAttributeIndicatorInfo.CLASS))
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
    dataType.equals(MdAttributeBooleanInfo.CLASS) || dataType.equals(MdAttributeIntegerInfo.CLASS) || dataType.equals(MdAttributeLongInfo.CLASS) || dataType.equals(MdAttributeFloatInfo.CLASS) || dataType.equals(MdAttributeDoubleInfo.CLASS) || dataType.equals(MdAttributeDecimalInfo.CLASS) ||
    // Non Primitives
        dataType.equals(MdAttributeBlobInfo.CLASS))
    {
    }
    else
    {
      String error = "Database layer does not recognize attribute type [" + dataType + "]";
      throw new DatabaseException(error);
    }

    return sqlStmt;
  }

  /**
   * Throws the appropriate exception based on the severity of the error. Some
   * DB errors indicate a bug in the core.
   * 
   * @param ex
   *          SQLException thrown.
   * @param sqlStmt
   *          SQL statement that caused the exception to be thrown.
   */
  public void throwDatabaseException(SQLException ex, String debugMsg)
  {
    String errorCode = new String(new Integer(ex.getErrorCode()).toString());
    String errorMessage = ex.getMessage();

    errorCode = errorCode.trim();

    if (errorCode.equals("1062"))
    {
      String error = "An object already exists with values on attributes that are unique.";
      throw new DuplicateDataDatabaseException(error, ex);
    }

    if (errorCode.equals("1242"))
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
   * Returns true if, in order to produce a meaningful error message, the
   * database must manually check uniqueness constraints, rather than relying on
   * the database. Some databases do not return enough useful information in the
   * error message to produce a meaningful message to the end user.
   * 
   * @return true must manually check uniqueness constraints for the given
   *         database, false otherwise.
   */
  public boolean manuallyCheckForDuplicates()
  {
    return true;
  }

  @Override
  public void buildDynamicPropertiesTable()
  {
    StringBuffer statement = new StringBuffer();
    statement.append("CREATE TABLE " + Database.PROPERTIES_TABLE + " ( " + EntityDAOIF.ID_COLUMN + " CHAR(" + Database.DATABASE_ID_SIZE + ") BINARY NOT NULL," + Database.VERSION_NUMBER + " CHAR(255) NOT NULL PRIMARY KEY) ENGINE = InnoDB; ");
    String undo = "DROP TABLE " + Database.PROPERTIES_TABLE;

    new DDLCommand(statement.toString(), undo, false).doIt();
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



  /**
   * Builds a database specific concat function call string.
   * 
   * @param concatString1
   *          name of the original string.
   * @param concatString2
   *          starting position.
   * @return a database specific concat function call string.
   */
  public String buildConcatFunctionCall(String concatString1, String concatString2)
  {
    return "CONCAT(" + concatString1 + ", " + concatString2 + ")";
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
      String select = "SELECT " + columnName + " FROM " + table + " WHERE " + EntityDAOIF.ID_COLUMN + " = '" + oid + "'";
      String update = "UPDATE " + table + " SET " + columnName + " = " + "? WHERE " + EntityDAOIF.ID_COLUMN + " = '" + oid + "'";
      resultSet = statement.executeQuery(select);
      resultSet.next();
      byte[] resultBytes = resultSet.getBytes(columnName);

      // truncate the bytes
      byte[] temp = new byte[(int) length];
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

  // //////////////////////////////////////////////////////////////
  // ////// Relationships
  // //////////////////////////////////////////////////////////////

  /**
   * @see com.runwaysdk.dataaccess.database.relationship.AbstractDatabase#getChildCountForParent(java.lang.String,
   *      java.lang.String)
   */
  public long getChildCountForParent(String parent_oid, String relationshipTableName)
  {
    String query = " SELECT COUNT(*) AS CT \n" + " FROM " + relationshipTableName + " \n" + " WHERE " + RelationshipDAOIF.PARENT_OID_COLUMN + " = '" + parent_oid + "' \n" + " AND " + RelationshipDAOIF.CHILD_OID_COLUMN + " IN " + "   (SELECT DISTINCT " + RelationshipDAOIF.CHILD_OID_COLUMN + " \n" + "    FROM " + relationshipTableName + " \n" + "    WHERE " + RelationshipDAOIF.PARENT_OID_COLUMN + " = '" + parent_oid + "')";

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
   * @see com.runwaysdk.dataaccess.database.relationship.AbstractDatabase#getParentCountForChild(java.lang.String,
   *      java.lang.String)
   */
  public long getParentCountForChild(String child_oid, String relationshipTableName)
  {
    String query = " SELECT COUNT(*) AS CT \n" + " FROM " + relationshipTableName + " \n" + " WHERE " + RelationshipDAOIF.CHILD_OID_COLUMN + " = '" + child_oid + "' \n" + " AND " + RelationshipDAOIF.PARENT_OID_COLUMN + " IN " + "   (SELECT DISTINCT " + RelationshipDAOIF.PARENT_OID_COLUMN + " \n" + "    FROM " + relationshipTableName + " \n" + "    WHERE " + RelationshipDAOIF.CHILD_OID_COLUMN + " = '" + child_oid + "')";

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
   * @param tableNames
   *          list of tables to backup
   * @param backupFileLocation
   *          location of the backup file to generate.
   * @param backupFileRootName
   *          root of the file name (minus the file extension).
   * @param dropSchema
   *          true if backup should include commands to drop the schema
   */
  @Override
  public String backup(List<String> tableNames, String backupFileLocation, String backupFileRootName,  PrintStream out, PrintStream errOut, boolean dropSchema)
  {
    throw new UnsupportedOperationException("Backup method is not yet implemented for MySQL.");
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
    throw new UnsupportedOperationException("Backup method is not yet implemented for MySQL.");
  }

  @Override
  public String backup(String namespace, String backupFileLocation, String backupFileRootName,  PrintStream out, PrintStream errOut, boolean dropSchema)
  {
    throw new UnsupportedOperationException("Backup method is not yet implemented for MySQL.");
  }

  @Override
  public void close()
  {
    throw new UnsupportedOperationException("Close method is not yet implemented for MySQL.");
  }

  @Override
  public void createTempTable(String tableName, List<String> columns, String onCommit)
  {
    throw new UnsupportedOperationException("createTempTable is not yet implemented for MySQL.");
  }
}
