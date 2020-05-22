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
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.google.inject.Inject;
import com.runwaysdk.RunwayMetadataVersion;
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
import com.runwaysdk.dataaccess.DuplicateGraphPathException;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDecDAOIF;
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

import oracle.jdbc.pool.OracleConnectionCacheManager;
import oracle.jdbc.pool.OracleDataSource;


public class Oracle extends AbstractDatabase
{
  private final static String CACHE_NAME = "RunwaySDKOracleDBCachePool";

  private String       objectSequenceName;

  public static String UNIQUE_OBJECT_ID_SEQUENCE = "object_sequence_unique_id";

  private String       transactionSequenceName;

  public static String TRANSACTION_SEQUENCE = "transaction_record_sequence";


  /**
   * Initialize the datasource to point to a MySQL database.
   */
  @Inject
  public Oracle()
  {
    super();
    this.objectSequenceName      = UNIQUE_OBJECT_ID_SEQUENCE;
    this.transactionSequenceName = TRANSACTION_SEQUENCE;
  }
  
  public void initializeConnection()
  {
    throw new DatabaseException("Not implemented");
  }
  
  public void initializeRootConnection(String rootUser, String rootPass, String rootDb)
  {
    throw new DatabaseException("Not implemented");
  }

  /**
   * The generic implementation of getConnection() assumes that the data source is set up
   * already - with Oracle we set it up lazily. This override sets up the data source if
   * necessary then delegates to super to get the connection.
   *
   * @see com.runwaysdk.dataaccess.database.general.AbstractDatabase#getConnection()
   */
  @Override
  public Connection getConnectionRaw()
  {
    // Lazily set up the data source
    if (dataSource==null)
      setupDataSource();
    return super.getConnectionRaw();
  }

  /**
   * Sets up the pooled dataSource that provides connections to the database. Setup is
   * pulled out of the constructor to allow for lazy instantiation.
   */
  private void setupDataSource()
  {
    int portNumber = DatabaseProperties.getPort();
    String server = DatabaseProperties.getServerName();
    try
    {
      // If environment is not configured for connection pooling, do not pool
      // connections
      // OracleConnectionPoolDataSource //OracleDataSource
      OracleDataSource oracleDataSource = new OracleDataSource();

      /* Set Host name */
      oracleDataSource.setServerName(server);
      /* Set Database SID */
      oracleDataSource.setServiceName(DatabaseProperties.getDatabaseName());
      /* Set Port number */
      oracleDataSource.setPortNumber(portNumber);
      /* Set Driver type */
      oracleDataSource.setDriverType("thin");
      /* Set User name */
      oracleDataSource.setUser(DatabaseProperties.getUser());
      /* Set Password */
      oracleDataSource.setPassword(DatabaseProperties.getPassword());

      this.dataSource = oracleDataSource;

      int initialDbConnections = DatabaseProperties.getInitialConnections();
      int maxDbConnections = DatabaseProperties.getMaxConnections() - 1;

      if (maxDbConnections < 2)
      {
        maxDbConnections = 2;
      }

      boolean pooling = DatabaseProperties.getConnectionPooling();
      if (pooling)
      {
        /* Enable cahcing */
        oracleDataSource.setConnectionCachingEnabled(true);

        /* Set the cache name */
        oracleDataSource.setConnectionCacheName(CACHE_NAME);

        /* Initialize the Connection Cache */
        OracleConnectionCacheManager connMgr = OracleConnectionCacheManager
            .getConnectionCacheManagerInstance();

        /*
         * This object holds the properties of the cache and is passed to the
         * ConnectionCacheManager while creating the cache. Based on these
         * properties the connection cache manager created the connection
         * cache.
         */
        Properties properties = new Properties();

        /*
         * Set Min Limit for the Cache. This sets the minimum number of
         * PooledConnections that the cache maintains. This guarantees that
         * the cache will not shrink below this minimum limit.
         */
        properties.setProperty("MinLimit", "" + initialDbConnections);

        /*
         * Set Max Limit for the Cache. This sets the maximum number of
         * PooledConnections the cache can hold. There is no default MaxLimit
         * assumed meaning connections in the cache could reach as many as the
         * database allows.
         */
// Rather than put threads to sleep that request a connection object beyond this value, the oracle
// Connection pool implementation seems to be throwing a null pointer exception.
//          properties.setProperty("MaxLimit", "" + this.maxDbConnections);


        /*
         * Set the Initial Limit. This sets the size of the connection cache
         * when the cache is initially created or reinitialized. When this
         * property is set to a value greater than 0, that many connections
         * are pre-created and are ready for use.
         */
        properties.setProperty("InitialLimit", "" + initialDbConnections);

        /*
         * Create the cache by passing the cache name, data source and the
         * cache properties
         */
        connMgr.createCache(CACHE_NAME, oracleDataSource, properties);
      }
    }
    catch (SQLException sqlEx)
    {
      this.throwDatabaseException(sqlEx);
    }
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.database.general.AbstractDatabase#initialSetup()
   */
  public void initialSetup(String rootUser, String rootPass, String rootDb)
  {
    try
    {
      // Set up the root connection
      OracleDataSource oracleRootSource = new OracleDataSource();

      oracleRootSource.setServerName(DatabaseProperties.getServerName());
      oracleRootSource.setServiceName(rootDb);
      oracleRootSource.setPortNumber(DatabaseProperties.getPort());
      oracleRootSource.setDriverType("thin");
      oracleRootSource.setUser(rootUser);
      oracleRootSource.setPassword(rootPass);

      this.rootDataSource = oracleRootSource;
    }
    catch (SQLException e)
    {
      throwDatabaseException(e);
    }

    this.dropUser();

    this.createUser();
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
  public void dropUser()
  {
    LinkedList<String> statements = new LinkedList<String>();
    String userName = DatabaseProperties.getUser();

    try
    {
      statements.add("DROP USER " + userName + " CASCADE");
      executeAsRoot(statements, false);
    }
    catch (DatabaseException e)
    {
      // This happens if the user didn't exist to be dropped.  Keep going.
    }
  }

  /**
   * Creates the database user.
   */
  @Override
  public void createUser()
  {
    LinkedList<String> statements = new LinkedList<String>();
    String userName = DatabaseProperties.getUser();
    String userPass = DatabaseProperties.getPassword();

    statements.add("CREATE USER " + userName + " IDENTIFIED BY " + userPass);
    statements.add("GRANT CONNECT TO " + userName);
    statements.add("GRANT RESOURCE TO " + userName);
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
   * @see com.runwaysdk.dataaccess.AbstractDatabase#createClassTable(java.lang.String)
   */
  public void createClassTable(String tableName)
  {
    String statement = startCreateClassTable(tableName)+" "+endCreateClassTable(tableName);

    String undo = "DROP TABLE " + tableName;

    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * @see com.runwaysdk.dataaccess.AbstractDatabase#startCreateClassTable(java.lang.String)
   */
  public String startCreateClassTable(String tableName)
  {
    return "CREATE TABLE " + tableName + " ( " + EntityDAOIF.ID_COLUMN + " CHAR("
    + Database.DATABASE_ID_SIZE + ") NOT NULL PRIMARY KEY";
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
   * Creates a new table in the database for a relationships. Automatically adds the Component.OID columnName as the primary
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
   * Automatically adds the Component.OID columnName as the primary key.
   *
   * @param tableName  The name of the new table.
   */
  @Override
  public String startCreateRelationshipTableBatch(String tableName)
  {
    return "CREATE TABLE " + tableName + " ( " + EntityDAOIF.ID_COLUMN + " CHAR("
    + Database.DATABASE_ID_SIZE + ") NOT NULL PRIMARY KEY, \n" + RelationshipDAOIF.PARENT_OID_COLUMN
    + "                    CHAR(" + Database.DATABASE_ID_SIZE + ") NOT NULL, \n"
    + RelationshipDAOIF.CHILD_OID_COLUMN + "                     CHAR(" + Database.DATABASE_ID_SIZE
    + ") NOT NULL \n";
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
    String statement = "CREATE";
    if (isUnique)
    {
      statement += " UNIQUE ";
    }
    statement += " INDEX " + index1Name + " ON " + tableName + " (" + RelationshipDAOIF.PARENT_OID_COLUMN + ", "
        + RelationshipDAOIF.CHILD_OID_COLUMN + ")";
    String undo = "DROP INDEX " + index1Name;
    new DDLCommand(statement, undo, false).doIt();

    // Create the second index
    statement = "CREATE INDEX " + index2Name + " ON " + tableName + " (" + RelationshipDAOIF.CHILD_OID_COLUMN + ")";
    undo = "DROP INDEX " + index2Name;
    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * @see com.runwaysdk.dataaccess.database.Database#createEnumerationTable(String, String);
   */
  public void createEnumerationTable(String tableName, String oid)
  {
    String statement = "CREATE TABLE " + tableName + " ( " + MdEnumerationDAOIF.SET_ID_COLUMN
        + "                    CHAR(" + Database.DATABASE_SET_ID_SIZE + ") NOT NULL, \n"
        + MdEnumerationDAOIF.ITEM_ID_COLUMN + "  CHAR(" + Database.DATABASE_ID_SIZE + ") NOT NULL \n" + " )";
    String undo = "DROP TABLE " + tableName;
    new DDLCommand(statement, undo, false).doIt();

    // Create the first index
    String indexName = this.createIdentifierFromId(oid);
    statement = "CREATE UNIQUE INDEX " + indexName + " ON " + tableName + " ("
        + MdEnumerationDAOIF.SET_ID_COLUMN + ", " + MdEnumerationDAOIF.ITEM_ID_COLUMN + ")";
    undo = "DROP INDEX " + indexName;
    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * @see com.runwaysdk.dataaccess.AbstractDatabase#dropClassTable(java.lang.String)
   */
  public void dropClassTable(String tableName)
  {
    String statement = "DROP TABLE " + tableName;
    String undo = "CREATE TABLE " + tableName + " ( " + EntityDAOIF.ID_COLUMN + " CHAR("
        + Database.DATABASE_ID_SIZE + ") NOT NULL PRIMARY KEY )";

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
    // Create the first index
    String statement = "DROP INDEX " + index1Name;
    String undo = "CREATE";
    if (isUnique)
    {
      undo += " UNIQUE ";
    }
    undo += " INDEX " + index1Name + " ON " + tableName + " (" + RelationshipDAOIF.PARENT_OID_COLUMN + ", "
        + RelationshipDAOIF.CHILD_OID_COLUMN + ")";
    new DDLCommand(statement, undo, true).doIt();

    // Create the second index
    statement = "DROP INDEX " + index2Name;
    undo = "CREATE INDEX " + index2Name + " ON " + tableName + " (" + RelationshipDAOIF.CHILD_OID_COLUMN + ")";
    new DDLCommand(statement, undo, true).doIt();

    statement = "DROP TABLE " + tableName;
    undo = "CREATE TABLE " + tableName + " ( " + EntityDAOIF.ID_COLUMN + " CHAR("
        + Database.DATABASE_ID_SIZE + ") NOT NULL PRIMARY KEY, \n" + RelationshipDAOIF.PARENT_OID_COLUMN
        + "                    CHAR(" + Database.DATABASE_ID_SIZE + ") NOT NULL, \n"
        + RelationshipDAOIF.CHILD_OID_COLUMN + "                     CHAR(" + Database.DATABASE_ID_SIZE
        + ") NOT NULL \n" + " )";
    new DDLCommand(statement, undo, true).doIt();
  }

  /**
   * @see com.runwaysdk.dataaccess.database.Database#dropEnumerationTable(String, String);
   */
  public void dropEnumerationTable(String tableName, String oid)
  {
    // Create the first index
    String indexName = this.createIdentifierFromId(oid);
    String statement = "DROP INDEX " + indexName;
    String undo = "CREATE UNIQUE INDEX " + indexName + " ON " + tableName + " ("
        + MdEnumerationDAOIF.SET_ID_COLUMN + ", " + MdEnumerationDAOIF.ITEM_ID_COLUMN + ")";
    new DDLCommand(statement, undo, true).doIt();

    statement = "DROP TABLE " + tableName;
    undo = "CREATE TABLE " + tableName + " ( " + MdEnumerationDAOIF.SET_ID_COLUMN
        + "                    CHAR(" + Database.DATABASE_SET_ID_SIZE + ") NOT NULL, \n"
        + MdEnumerationDAOIF.ITEM_ID_COLUMN + "  CHAR(" + Database.DATABASE_ID_SIZE + ") NOT NULL \n" + " )";
    new DDLCommand(statement, undo, true).doIt();
  }

  /**
   * @see com.runwaysdk.dataaccess.AbstractDatabase#addUniqueIndex(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public void addUniqueIndex(String table, String columnName, String indexName)
  {
    String statement = "CREATE UNIQUE INDEX " + indexName + " ON " + table + " (" + columnName + ")";
    String undo = "DROP INDEX " + indexName;

    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * @see com.runwaysdk.dataaccess.AbstractDatabase#addNonUniqueIndex(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public void addNonUniqueIndex(String table, String columnName, String indexName)
  {
    String statement = "CREATE INDEX " + indexName + " ON " + table + " (" + columnName + ")";
    String undo = "DROP INDEX " + indexName;

    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * @see com.runwaysdk.dataaccess.AbstractDatabase#dropUniqueIndex(java.lang.String,
   *      java.lang.String, java.lang.String, delete)
   */
  public void dropUniqueIndex(String table, String columnName, String indexName, boolean delete)
  {
    String statement = "DROP INDEX " + indexName;
    String undo = "CREATE UNIQUE INDEX " + indexName + " ON " + table + " (" + columnName + ")";

    new DDLCommand(statement, undo, delete).doIt();
  }

  /**
   * @see com.runwaysdk.dataaccess.AbstractDatabase#dropNonUniqueIndex(java.lang.String,
   *      java.lang.String, java.lang.String, boolean)
   */
  public void dropNonUniqueIndex(String table, String columnName, String indexName, boolean delete)
  {
    String statement = "DROP INDEX " + indexName;
    String undo = "CREATE INDEX " + indexName + " ON " + table + " (" + columnName + ")";

    new DDLCommand(statement, undo, delete).doIt();
  }

  /**
   * @see com.runwaysdk.dataaccess.database.Database#uniqueAttributeExists(String, String, String);
   */
  public boolean uniqueAttributeExists(String table, String columnName, String indexName)
  {
    String sqlStmt = "SELECT column_name \n" + "  FROM user_ind_columns \n" + " WHERE index_name = '"
        + indexName.toUpperCase() + "' \n" + "   AND column_name = '" + columnName.toUpperCase() + "'";

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
   * @see com.runwaysdk.dataaccess.database.Database#nonUniqueAttributeExists(String, String, String);
   */
  public boolean nonUniqueAttributeExists(String table, String columnName, String indexName)
  {
    return uniqueAttributeExists(table, columnName, indexName);
  }

  /**
   * Creates an index on the given table on the columns with the given names.
   * @param table name of the database table.
   * @param indexName name of the database index.
   * @param attributeColumnNames name of the database columns.
   * @param isUnique true if the index should be unique, false otherwise.
   */
  public void addGroupAttributeIndex(String tableName, String indexName, List<String> attributeNames, boolean isUnique)
  {
    String statement = "CREATE ";

    if (isUnique)
    {
      statement += " UNIQUE ";
    }

    statement += " INDEX " + indexName + " ON " + tableName + " (";

    for (int i = 0; i < attributeNames.size(); i++)
    {
      if (i != 0)
      {
        statement += ", ";
      }
      statement += attributeNames.get(i);
    }

    statement += ")";

    String undo = "DROP INDEX " + indexName;

    new AddGroupIndexDDLCommand(tableName, indexName, statement, undo).doIt();
  }

  /**
   * Drops the index with the given name.  The attributes and unique flag are used to rebuild the index in the
   * case of a rolledback transaction.
   * @param tableName name of the database table.
   * @param indexName name of the database index.
   * @param attributeColumnNames name of the database columns.
   * @param isUnique true if the index should be unique, false otherwise.
   * @param delete true if this index is being deleted in this transaction, false otherwise.  The index may
   * be deleted if an attribute is being added to it.  In that case, the value should be <code>false</code>.
   */
  public void dropGroupAttributeIndex(String tableName, String indexName, List<String> attributeNames, boolean isUnique, boolean delete)
  {
    String statement = "DROP INDEX " + indexName;

    String undo = "CREATE ";

    if (isUnique)
    {
      undo += " UNIQUE ";
    }

    undo += " INDEX " + indexName + " ON " + tableName + " (";

    for (int i = 0; i < attributeNames.size(); i++)
    {
      if (i != 0)
      {
        undo += ", ";
      }

      undo += attributeNames.get(i);
    }

    undo += ")";

    // do not set the delete flag to true. Althougth this deletes an index,
    // we want this to occur during the transaction and not after the
    // transaction.
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
    String sqlStmt = " SELECT column_name \n" + "  FROM user_ind_columns \n" + " WHERE index_name = '"
        + indexName.toUpperCase() + "' \n";

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
//  /**
//   * Returns true if the given index exists on the given table, false otherwise.
//   *
//   * @param table
//   * @param indexName
//   * @return true if the given index exists on the given table, false otherwise.
//   */
//  public boolean indexExists(String table, String indexName)
//  {
//    String statement = " SELECT column_name \n" + "  FROM user_ind_columns \n" + " WHERE index_name = '"
//        + indexName.toUpperCase() + "' \n";
//
//    List result = this.query(statement);
//
//    if (result.size() > 0)
//    {
//      return true;
//    }
//    else
//    {
//      return false;
//    }
//  }

  /**
   * Returns true if a group attribute index exists with the given name and the given attributes on the given table.
   * @param tableName
   * @param indexName
   * @param attributeColumnNames
   */
  public boolean groupAttributeIndexExists(String table, String indexName, List<String> attributeColumnNames)
  {
    String sqlStmt = " SELECT column_name \n" + "  FROM user_ind_columns \n" + " WHERE index_name = '"
        + indexName.toUpperCase() + "' \n";

    ResultSet resultSet = query(sqlStmt);

    boolean returnResult = true;

    try
    {
      int resultCount = 0;
      while (resultSet.next())
      {
        resultCount ++;

        String attrName = resultSet.getString("column_name").toLowerCase();
        if (!attributeColumnNames.contains(attrName))
        {
          returnResult = false;
        }

      }

      if (resultCount != attributeColumnNames.size())
      {
        returnResult = false;
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
//  /**
//   * Returns true if a group attribute index exists with the given name and the given attributes on the given table.
//   * @param tableName
//   * @param indexName
//   * @param attributeColumnNames
//   */
//  public boolean groupAttributeIndexExists(String table, String indexName, List<String> attributeColumnNames)
//  {
//    String statement = " SELECT column_name \n" + "  FROM user_ind_columns \n" + " WHERE index_name = '"
//        + indexName.toUpperCase() + "' \n";
//
//    List result = this.query(statement);
//
//    if (attributeColumnNames.size() != result.size())
//    {
//      return false;
//    }
//
//    for (int i = 0; i < result.size(); i++)
//    {
//      String attrName = ( ( (DynaBean) result.get(i) ).get("column_name").toString() ).toLowerCase();
//      if (!attributeColumnNames.contains(attrName))
//      {
//        return false;
//      }
//    }
//
//    return true;
//  }

  /**
   * Returns true if a group attribute index exists with the given name on the given table.
   * @param tableName
   * @param indexName
   */
  public boolean groupAttributeIndexExists(String table, String indexName)
  {
    String sqlStmt = " SELECT column_name \n" + "  FROM user_ind_columns \n" + " WHERE index_name = '"
        + indexName.toUpperCase() + "' \n";

    ResultSet resultSet = query(sqlStmt);

    boolean returnValue = false;

    try
    {
      if (resultSet.next())
      {
        returnValue = true;
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

// Heads up:
//  /**
//   * Returns true if a group attribute index exists with the given name on the given table.
//   * @param tableName
//   * @param indexName
//   */
//  public boolean groupAttributeIndexExists(String table, String indexName)
//  {
//    String statement = " SELECT column_name \n" + "  FROM user_ind_columns \n" + " WHERE index_name = '"
//        + indexName.toUpperCase() + "' \n";
//
//    List result = this.query(statement);
//
//    if (result.size() > 0)
//    {
//      return true;
//    }
//    else
//    {
//      return false;
//    }
//  }

  /**
   * Returns a list of string names of the attributes that participate in a group  index for
   * the given table with the index of the given name.
   * @param table
   * @param indexName
   */
  public List<String> getGroupIndexAttributes(String table, String indexName)
  {
    Connection conn = Database.getConnection();
    return this.getGroupIndexAttributesFromIndexName(indexName, conn);
  }

  /**
   * Returns a list of string names of the attributes that participate in a
   * group unique with the given name.
   *
   * @param indexName
   * @param conn it is up to the client to manage the connection object.
   * @param attributeNames
   */
  public List<String> getGroupIndexAttributesFromIndexName(String indexName, Connection conn)
  {
    List<String> attributeNames = new LinkedList<String>();

    String sqlStmt = " SELECT column_name \n" + "   FROM user_ind_columns \n"
      + " WHERE index_name = '" + indexName.toUpperCase() + "' \n";

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
   * @see com.runwaysdk.dataaccess.AbstractDatabase#addDecFieldAlterTable(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public void addDecField(String table, String columnName, String type, String length, String decimal)
  {

    String ddlType = formatDDLDecField(type, length, decimal);
    String statement = "ALTER TABLE " + table + " ADD (" + columnName + "  " + ddlType + ")";

    String undo = "ALTER TABLE " + table + " DROP COLUMN " + columnName;

    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * @see com.runwaysdk.dataaccess.AbstractDatabase#addDecField(java.lang.String,
   *      java.lang.String, java.lang.String)
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
    return "ALTER TABLE " + table + " DROP COLUMN " + columnName;
  }

  /**
   * @see com.runwaysdk.dataaccess.AbstractDatabase#alterFieldType(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String)
   */
  public void alterFieldType(String table, String columnName, String newDbColumnType, String oldDbColumnType)
  {
    String statement = "ALTER TABLE " + table + " MODIFY " + columnName + " " + newDbColumnType;

    String undo = "ALTER TABLE " + table + " MODIFY " + columnName + " " + oldDbColumnType;

    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * @see com.runwaysdk.dataaccess.AbstractDatabase#tableExists(java.lang.String)
   */
  public boolean tableExists(String tableName)
  {
    String sqlStmt = "SELECT table_name FROM user_tables WHERE table_name = '"
      + tableName.toUpperCase() + "'";

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
   * @see com.runwaysdk.dataaccess.AbstractDatabase#getColumnNames(java.lang.String)
   */
  public List<String> getColumnNames(String tableName)
  {
    String queryString = "SELECT column_name \n" + "  FROM user_tab_columns \n"
        + " WHERE table_name = '" + tableName.toUpperCase() + "'";

    ResultSet resultSet = query(queryString);
    LinkedList<String> columnNames = new LinkedList<String>();

    try
    {
      while (resultSet.next())
      {
        columnNames.add((String)resultSet.getString("column_name").toLowerCase());
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
    String queryString =
      " SELECT column_name \n" +
      "  FROM user_tab_columns \n" +
      " WHERE table_name = '" + tableName.toUpperCase() + "' \n"+
      " WHERE column_name = '" + columnName.toUpperCase() + "'";

    ResultSet resultSet = query(queryString);

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
   * @see com.runwaysdk.dataaccess.AbstractDatabase#createObjectSequence()
   */
  public void createObjectSequence()
  {
    this.execute("CREATE SEQUENCE " + this.objectSequenceName + " INCREMENT BY 1 START WITH "
        + Database.STARTING_SEQUENCE_NUMBER);
  }

  /**
   * @see com.runwaysdk.dataaccess.AbstractDatabase#getNextSequenceNumber()
   */
  public String getNextSequenceNumber()
  {
    // get the sequence value
    String sqlStmt = "SELECT " + this.objectSequenceName + ".nextval AS nextval FROM DUAL";

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

// Heads up:
//  /**
//   * @see com.runwaysdk.dataaccess.AbstractDatabase#getNextSequenceNumber()
//   */
//  public String getNextSequenceNumber()
//  {
//    // get the sequence value
//    List results = this.query("SELECT " + this.idSequenceName + ".nextval AS nextval FROM DUAL");
//    return ( (DynaBean) results.get(0) ).get("nextval").toString();
//  }


  /**
   * @see com.runwaysdk.dataaccess.AbstractDatabase#createTransactionSequence()
   */
  @Override
  public void createTransactionSequence()
  {
    this.execute("CREATE SEQUENCE " + this.transactionSequenceName + " INCREMENT BY 1 START WITH 1");
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
      this.execute("DROP SEQUENCE " + this.transactionSequenceName);
      this.createTransactionSequence();
    }
    else
    {
      String errorMsg = "Reseting the transaction sequence only during runway development testing.";
      throw new UnsupportedOperationException(errorMsg);
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.AbstractDatabase#getNextTransactionSequence()
   */
  @Override
  public String getNextTransactionSequence()
  {
    // get the sequence value
    String sqlStmt = "SELECT " + this.transactionSequenceName + ".nextval AS nextval FROM DUAL";

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
   * @see com.runwaysdk.dataaccess.AbstractDatabase#addFieldBatch(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String)
   */
  public void addField(String table, String columnName, String type, String size)
  {
    String statement = "ALTER TABLE " + table + " ADD (" + columnName + "  " + type;

    if (size != null)
    {
      statement += "(" + size + ")";
    }
    statement += ')';

    String undo = "ALTER TABLE " + table + " DROP COLUMN " + columnName;

    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * @see com.runwaysdk.dataaccess.AbstractDatabase#addFieldBatch(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String)
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
   * @see com.runwaysdk.dataaccess.AbstractDatabase#formatColumnAlias(java.lang.String)
   */
  public String formatColumnAlias(String columnAlias)
  {
    return columnAlias;
  }

  /**
   * Formats the column for a comparison in a select statement.
   *
   * @param formatted
   *          column name.
   * @return formatted column name.
   */
  public String formatColumnForCompare(String columnName, String dataType)
  {
    String formattedColumnName = columnName;

    if (dataType.equals(MdAttributeTextInfo.CLASS) ||
        dataType.equals(MdAttributeClobInfo.CLASS))
    {
      formattedColumnName = "TO_CHAR(" + columnName + ")";
    }

    return formattedColumnName;
  }

  /**
   * Formats a column for a select statement.
   * @param columnName
   * @param mdAttribute MdAttribute that defines the attribute that uses the given column.
   * @return
   */
  @Override
  public String formatSelectClauseColumn(String columnName, MdAttributeConcreteDAOIF mdAttribute)
  {
    if (mdAttribute instanceof MdAttributeDecDAOIF)
    {
      int length = Integer.parseInt(((MdAttributeDecDAOIF)mdAttribute).getLength()) ;
      int precision = Integer.parseInt(((MdAttributeDecDAOIF)mdAttribute).getDecimal()) ;

      String precisionString = "";

      for (int i=0; i<precision; i++)
      {
        precisionString += "9";
      }

      int leftHandSideLength = length - precision;
      String leftHandSideString = "";

      for (int i=0; i<leftHandSideLength; i++)
      {
        leftHandSideString += "9";
      }

      String formatString = leftHandSideString+"."+precisionString;

      return "TO_CHAR("+columnName+", '"+formatString+"')";
    }
    else
    {
      return columnName;
    }
  }


  /**
   * Creates an alias in the syntax of the specific database vendor for a
   * fictitious column of the given datatype. This allows Select statements to be
   * created with extra columns that do not exist on a table. This is useful for
   * performing a UNION between two select statements.
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
         dataType.equals(MdAttributeCharacterInfo.CLASS) ||
         dataType.equals(MdAttributeStructInfo.CLASS) ||
         dataType.equals(MdAttributeLocalCharacterInfo.CLASS)  ||
         dataType.equals(MdAttributeLocalTextInfo.CLASS)       ||
         dataType.equals(MdAttributeHashInfo.CLASS) ||

        // References
        dataType.equals(MdAttributeReferenceInfo.CLASS) ||
        dataType.equals(MdAttributeTermInfo.CLASS) ||
        dataType.equals(MdAttributeFileInfo.CLASS) ||
        dataType.equals(MdAttributeEnumerationInfo.CLASS) ||
        dataType.equals(MdAttributeIndicatorInfo.CLASS))
    {
      bogusValue = "''";
    }
    else if (dataType.equals(MdAttributeTextInfo.CLASS) ||
             dataType.equals(MdAttributeClobInfo.CLASS) ||
             dataType.equals(MdAttributeSymmetricInfo.CLASS))
    {
      bogusValue = "EMPTY_CLOB()";
    }
    else if (dataType.equals(MdAttributeBlobInfo.CLASS))
    {
      bogusValue = "EMPTY_BLOB()";
    }
    else if (dataType.equals(MdAttributeTimeInfo.CLASS))
    {
      bogusValue = "TO_DATE('12:00:00', '" + DatabaseProperties.getTimeFormat() + "')";
    }
    else if (dataType.equals(MdAttributeDateInfo.CLASS))
    {
      bogusValue = "TO_DATE('2000-01-01', '" + DatabaseProperties.getDateFormat() + "')";
    }
    else if (dataType.equals(MdAttributeDateTimeInfo.CLASS))
    {
      bogusValue = "TO_DATE('2000-01-01 12:00:00', '" + DatabaseProperties.getDateTimeFormat() + "')";
    }
    // Don't format attributes of these types.
    else if (// Primitive
    dataType.equals(MdAttributeIntegerInfo.CLASS) || dataType.equals(MdAttributeLongInfo.CLASS)
        || dataType.equals(MdAttributeFloatInfo.CLASS) || dataType.equals(MdAttributeDoubleInfo.CLASS)
        || dataType.equals(MdAttributeDecimalInfo.CLASS)
        || dataType.equals(MdAttributeBooleanInfo.CLASS))
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
   * Throws the appropriate exception based on the severity of the error.  Some DB errors indicate a
   * bug in the core.
   * @param ex  SQLException thrown.
   * @param sqlStmt SQL statement that caused the exception to be thrown.
   */
  public void throwDatabaseException(SQLException ex, String debugMsg)
  {
    String errorCode = new String( new Integer(ex.getErrorCode()).toString() );
    String errorMessage = ex.getMessage();

    errorCode = errorCode.trim();

    if (errorCode.equals("1"))
    {
      int startIndex = errorMessage.indexOf(".")+1;
      int endIndex = errorMessage.indexOf(")", startIndex);
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

    if (errorCode.equals("1427"))
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
   * Returns the name of the table on which the given index applies.
   * @param indexName
   * @param conx
   * @return name of the table on which the given index applies.
   */
  public String getTableNameForIndex(String indexName, Connection conx)
  {
    String sqlStmt = " SELECT table_name \n" + "   FROM user_ind_columns \n" +
        " WHERE index_name = '" + indexName.toUpperCase() + "' \n "+
        "   AND ROWNUM = 1";

    ResultSet resultSet = query(sqlStmt);

    String tableName = "";

    try
    {
      while (resultSet.next())
      {
        tableName = resultSet.getString("table_name").toLowerCase();
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
    return tableName;

  }

// Heads up:
//  /**
//   * Returns the name of the table on which the given index applies.
//   * @param indexName
//   * @param conx
//   * @return name of the table on which the given index applies.
//   */
//  public String getTableNameForIndex(String indexName, Connection conx)
//  {
//    String statement = " SELECT table_name \n" + "   FROM user_ind_columns \n" +
//        " WHERE index_name = '" + indexName.toUpperCase() + "' \n "+
//        "   AND ROWNUM = 1";
//
//    List result = this.query(statement);
//
//    String tableName = "";
//
//    for (int i = 0; i < result.size(); i++)
//    {
//      tableName = ( ( (DynaBean) result.get(i) ).get("table_name").toString() ).toLowerCase();
//    }
//
//    return tableName;
//  }

  /**
   * @see com.runwaysdk.dataaccess.AbstractDatabase#formatJavaToSQL(java.lang.String,
   *      java.lang.String, boolean)
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
        dataType.equals(MdAttributeCharacterInfo.CLASS) ||
        dataType.equals(MdAttributeStructInfo.CLASS)    ||
        dataType.equals(MdAttributeLocalCharacterInfo.CLASS)  ||
        dataType.equals(MdAttributeLocalTextInfo.CLASS)       ||
        // Encryption
        dataType.equals(MdAttributeHashInfo.CLASS)      ||
        dataType.equals(MdAttributeSymmetricInfo.CLASS) ||
        // References
        dataType.equals(MdAttributeReferenceInfo.CLASS) ||
        dataType.equals(MdAttributeTermInfo.CLASS) ||
        dataType.equals(MdAttributeFileInfo.CLASS)      ||
        dataType.equals(MdAttributeEnumerationInfo.CLASS) ||
        dataType.equals(MdAttributeIndicatorInfo.CLASS))
    {
      sqlStmt = "'" + sqlStmt + "'";

      // only character data has mixed case
      if (ignoreCase)
      {
        sqlStmt = "UPPER(" + sqlStmt + ")";
      }
    }
    else if (dataType.equals(MdAttributeTextInfo.CLASS) ||
        dataType.equals(MdAttributeClobInfo.CLASS) ||
        dataType.equals(MdAttributeHashInfo.CLASS)      ||
        dataType.equals(MdAttributeSymmetricInfo.CLASS))
    {
      sqlStmt = "'" + sqlStmt + "'";
    }
    else if (dataType.equals(MdAttributeTimeInfo.CLASS))
    {
      sqlStmt = "TO_DATE('" + sqlStmt + "', '" + DatabaseProperties.getTimeFormat() + "')";
    }
    else if (dataType.equals(MdAttributeDateInfo.CLASS))
    {
      sqlStmt = "TO_DATE('" + sqlStmt + "', '" + DatabaseProperties.getDateFormat() + "')";
    }
    else if (dataType.equals(MdAttributeDateTimeInfo.CLASS))
    {
      sqlStmt = "TO_DATE('" + sqlStmt + "', '" + DatabaseProperties.getDateTimeFormat() + "')";
    }
    // Don't format attributes of these types.
    else if (// Primitive
         dataType.equals(MdAttributeBooleanInfo.CLASS)   ||
         dataType.equals(MdAttributeIntegerInfo.CLASS) ||
         dataType.equals(MdAttributeLongInfo.CLASS)    ||
         dataType.equals(MdAttributeFloatInfo.CLASS)   ||
         dataType.equals(MdAttributeDoubleInfo.CLASS)  ||
         dataType.equals(MdAttributeDecimalInfo.CLASS) ||
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
   * @param ignoreCase if true, the value is converted to UPPERCASE
   */
  public String formatJavaToSQLForQuery(String value, String dataType, boolean ignoreCase)
  {
    if (dataType.equals(MdAttributeTimeInfo.CLASS))
    {
      String sqlStmt = value;

      if (sqlStmt == null || sqlStmt.trim().equals(""))
      {
        return "NULL";
      }
      else
      {
        // Escape all characters that are harmful to an SQL statement
        sqlStmt = escapeSQLCharacters(sqlStmt);

        return sqlStmt = "TO_DATE('1970-01-01 " + sqlStmt + "', '" + DatabaseProperties.getDateTimeFormat() + "')";
      }
    }
    else
    {
      return formatJavaToSQL(value, dataType, ignoreCase);
    }
  }

  /**
   * Formats an SQL value to a java value.
   *
   * @param value
   * @param dataType
   * @param ignoreCase
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
   * Formats an SQL date value to a Java String.
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
// Heads up:
//  /**
//   * @see com.runwaysdk.dataaccess.AbstractDatabase#RowSetDynaClass(org.apache.commons.beanutils.RowSetDynaClass,
//   *      boolean)
//   */
//  protected RowSetDynaClass getRowSetDynaClass(ResultSet resultSet, boolean lowerCase)
//  {
//    RowSetDynaClass rowSetDynaClass = null;
//    try
//    {
//      rowSetDynaClass = new OracleRowSetDynaClass(resultSet, lowerCase);
//    }
//    catch (SQLException ex)
//    {
//      this.throwDatabaseException(ex);
//    }
//    return rowSetDynaClass;
//  }

  /**
   * Builds a database specific substring function call string.
   *
   * @param stringName
   *          name of the original string.
   * @param position
   *          starting position.
   * @param length
   *          string length.
   * @return a database specific substring function call string.
   */
  public String buildSubstringFunctionCall(String stringName, int position, int length)
  {
    return "SUBSTR(" + stringName + ", " + position + ", " + length + ")";
  }

  /**
   * Builds a database specific string position function call.
   *
   * @param stringToFind string to find in hte search string
   * @param searchString starting position.
   * @return database specific string position function call.
   */
  public String buildStringPositionFunctionCall(String stringToFind, String searchString)
  {
    return "INSTR("+searchString+", '"+stringToFind+"')";
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
    PreparedStatement prepared = null;
    Statement statement = null;
    ResultSet resultSet = null;
    int written = 0;
    try
    {
      // get the blob
      statement = conn.createStatement();
      String select = "SELECT " + columnName + " FROM " + table + " WHERE " + EntityDAOIF.ID_COLUMN + " = '" + oid
          + "' FOR UPDATE";
      String update = "UPDATE " + table + " SET " + columnName + " = " + "? WHERE " + EntityDAOIF.ID_COLUMN + " = '"
          + oid + "'";
      resultSet = statement.executeQuery(select);
      resultSet.next();
      Blob blob = resultSet.getBlob(columnName);

      // null check
      if (blob == null)
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
        written = blob.setBytes(pos, bytes, offset, length);
        if (conn.getMetaData().locatorsUpdateCopy())
        {
          // The current database needs to be manually updated (it doesn't
          // support auto blob updates)
          prepared = conn.prepareStatement(update);
          prepared.setBlob(1, blob);
          prepared.executeUpdate();
        }
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
        if (prepared != null)
          prepared.close();
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
    Statement statement = null;
    ResultSet resultSet = null;
    int written = 0;
    try
    {
      // get the blob
      statement = conn.createStatement();
      String select = "SELECT " + columnName + " FROM " + table + " WHERE " + EntityDAOIF.ID_COLUMN + " = '" + oid
          + "' FOR UPDATE";
      String update = "UPDATE " + table + " SET " + columnName + " = " + "? WHERE " + EntityDAOIF.ID_COLUMN + " = '"
          + oid + "'";
      resultSet = statement.executeQuery(select);
      boolean resultSetFound = resultSet.next();
      if (!resultSetFound)
      {
        return 0;
      }

      Blob blob = resultSet.getBlob(columnName);

      // null check
      if (blob == null)
      {
        // add the bytes directly
        prepared = conn.prepareStatement(update);
        prepared.setBytes(1, bytes);
        prepared.executeUpdate();
        written = bytes.length;
      }
      else
      {
        // modify the blob
        written = blob.setBytes(1, bytes);
        if (conn.getMetaData().locatorsUpdateCopy())
        {
          // The current database needs to be manually updated (it doesn't
          // support auto blob updates)
          prepared = conn.prepareStatement(update);
          prepared.setBlob(1, blob);
          prepared.executeUpdate();
        }
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
        if (prepared != null)
          prepared.close();
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
   * Truncates a blob by the specified length.
   *
   * @param table
   * @param columnName
   * @param oid
   * @param length
   */
  public void truncateBlob(String table, String columnName, String oid, long length, Connection conn)
  {
    PreparedStatement prepared = null;
    Statement statement = null;
    ResultSet resultSet = null;
    try
    {
      // get the blob
      statement = conn.createStatement();
      String select = "SELECT " + columnName + " FROM " + table + " WHERE " + EntityDAOIF.ID_COLUMN + " = '" + oid
          + "' FOR UPDATE";
      String update = "UPDATE " + table + " SET " + columnName + " = " + "? WHERE " + EntityDAOIF.ID_COLUMN + " = '"
          + oid + "'";
      resultSet = statement.executeQuery(select);
      boolean resultSetFound = resultSet.next();
      if (!resultSetFound)
      {
        return;
      }

      Blob blob = resultSet.getBlob(columnName);

      // null check
      if (blob != null)
      {
        blob.truncate(length);

        // modify the blob
        if (conn.getMetaData().locatorsUpdateCopy())
        {
          // The current database needs to be manually updated (it doesn't
          // support auto blob updates)
          prepared = conn.prepareStatement(update);
          prepared.setBlob(1, blob);
          prepared.executeUpdate();
        }
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
        if (prepared != null)
          prepared.close();
      }
      catch (SQLException e)
      {
        this.throwDatabaseException(e);
      }
    }
  }

  /**
   * This is a special method used to update the baseClass attribute of MdType
   * and it is used only within the TransactionManagement aspect, hence it takes
   * a JDBC connection object as a parameter.
   * @param mdTypeId
   * @param table
   * @param classColumnName
   * @param classBytes
   * @param sourceColumnName
   * @param source
   * @param conn
   */
  @Override
  public int updateClassAndSource(String mdTypeId, String updateTable, String classColumnName, byte[] classBytes, String sourceColumnName, String source, Connection conn)
  {
    PreparedStatement prepared = null;
    Statement statement = null;
    ResultSet resultSet = null;
    int written = 0;

    try
    {
      // clear the blob
      this.truncateBlob(updateTable, classColumnName, mdTypeId, 0, conn);

      // get the blob
      statement = conn.createStatement();
      String select = "SELECT " + classColumnName + " FROM " + updateTable + " WHERE " + EntityDAOIF.ID_COLUMN + " = '"
          + mdTypeId + "' FOR UPDATE";
      String update = "UPDATE " + updateTable + " SET " + classColumnName + " = " + "?, " + sourceColumnName
          + " = ? WHERE " + EntityDAOIF.ID_COLUMN + " = '" + mdTypeId + "'";
      resultSet = statement.executeQuery(select);

      boolean resultSetFound = resultSet.next();
      if (!resultSetFound)
      {
        return 0;
      }

      Blob classBlob = resultSet.getBlob(classColumnName);

      prepared = conn.prepareStatement(update);
      written = addBlobToStatement(prepared, 1, classBlob, classBytes);
      prepared.setString(2, source);
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
    PreparedStatement prepared = null;
    Statement statement = null;
    ResultSet resultSet = null;
    int written = 0;

    try
    {
      // clear the blob
      this.truncateBlob(table, serverClassesColumnName, mdFacadeId, 0, conn);
      this.truncateBlob(table, commonClassesColumnName, mdFacadeId, 0, conn);
      this.truncateBlob(table, clientClassesColumnName, mdFacadeId, 0, conn);

      // get the blob
      statement = conn.createStatement();
      String select =
        "SELECT " + serverClassesColumnName + ", " + commonClassesColumnName + ", " + clientClassesColumnName +
        " FROM " + table + " WHERE " + EntityDAOIF.ID_COLUMN + " = '" + mdFacadeId + "' FOR UPDATE";
      String update =
        "UPDATE " + table +
        " SET " + serverClassesColumnName + " = " + "?, " + commonClassesColumnName + " = ?, " + clientClassesColumnName + " = ? " +
        " WHERE " + EntityDAOIF.ID_COLUMN + " = '" + mdFacadeId + "'";
      resultSet = statement.executeQuery(select);

      boolean resultSetFound = resultSet.next();
      if (!resultSetFound)
      {
        return 0;
      }

      Blob serverClassesBlob = resultSet.getBlob(serverClassesColumnName);
      Blob commonClassesBlob = resultSet.getBlob(commonClassesColumnName);
      Blob clientClassesBlob = resultSet.getBlob(clientClassesColumnName);

      prepared = conn.prepareStatement(update);
      written += addBlobToStatement(prepared, 1, serverClassesBlob, serverClassesBytes);
      written += addBlobToStatement(prepared, 2, commonClassesBlob, commonClassesBytes);
      written += addBlobToStatement(prepared, 3, clientClassesBlob, clientClassesBytes);
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
   * Add a blob attribute to a PreparedStatement and the given index.
   *
   * @param prepared The statement to add the blob to
   * @param index The index to add the blob to
   * @param current The current value of the blob
   * @param newBytes The new bytes to write to the blob
   * @return
   * @throws SQLException
   */
  private static int addBlobToStatement(PreparedStatement prepared, int index, Blob current, byte[] newBytes) throws SQLException
  {
    int written = 0;

    if(current == null)
    {
      prepared.setBytes(index, newBytes);
      written = newBytes.length;
    }
    else
    {
      written = current.setBytes(1, newBytes);
      prepared.setBlob(index, current);
    }

    return written;
  }

  @Override
  public void buildDynamicPropertiesTable()
  {
    String statement = "CREATE TABLE " + Database.PROPERTIES_TABLE + " ( " +
        EntityDAOIF.ID_COLUMN + " CHAR(" + Database.DATABASE_ID_SIZE + ") NOT NULL," +
        Database.VERSION_NUMBER + " CHAR(255) NOT NULL PRIMARY KEY)";

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
SELECT * FROM (
  SELECT ROW_NUMBER() OVER (ORDER BY oid ASC) AS rn, oid, type FROM (
      SELECT
      oid, type FROM  metadata
      UNION ALL
      SELECT oid, type
      FROM  metadata
  )
)
WHERE rn > 5 AND rn <= 10

     */
    StringBuffer limitSqlStmt = new StringBuffer("");

    limitSqlStmt.append("SELECT * FROM (\n");
    limitSqlStmt.append("SELECT ROW_NUMBER() OVER ("+orderByClause+") AS rn, "+selectClauseAttributes+" FROM (\n");
    limitSqlStmt.append(sqlStmt);
    limitSqlStmt.append("\n)");
    int z = skip+limit;
    limitSqlStmt.append("\n)\n WHERE rn > "+skip+" AND rn <= ("+z+")");
    return limitSqlStmt;
  }

  ////////////////////////////////////////////////////////////////
  //////// Relationships
  ////////////////////////////////////////////////////////////////

  /**
   * @see com.runwaysdk.dataaccess.database.relationship.AbstractDatabase#getChildCountForParent(java.lang.String, java.lang.String)
   */
  public long getChildCountForParent(String parent_oid, String relationshipTableName )
  {
    String query = " SELECT COUNT(*) AS CT \n" +
                   " FROM "+relationshipTableName+" \n"+
                   " WHERE "+RelationshipDAOIF.PARENT_OID_COLUMN+" = '"+parent_oid+"' \n"+
                   " AND "+RelationshipDAOIF.CHILD_OID_COLUMN+" IN "+
                   "   (SELECT DISTINCT "+RelationshipDAOIF.CHILD_OID_COLUMN+" \n"+
                   "    FROM "+relationshipTableName+" \n"+
                   "    WHERE "+RelationshipDAOIF.PARENT_OID_COLUMN +" = '"+parent_oid+"')";


    ResultSet resultSet = this.query(query);

    long returnValue = 0;

    try
    {
      if (resultSet.next())
      {
        BigDecimal number = (BigDecimal) resultSet.getBigDecimal("ct");
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
//      BigDecimal number = (BigDecimal)dynaBean.get("ct");
//      return number.longValue();
//    }
  }


  /**
   * @see com.runwaysdk.dataaccess.database.relationship.AbstractDatabase#getParentCountForChild(java.lang.String, java.lang.String)
   */
  public long getParentCountForChild(String child_oid, String relationshipTableName )
  {
    String query = " SELECT COUNT(*) AS CT \n" +
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
        BigDecimal number = (BigDecimal) resultSet.getBigDecimal("ct");
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
//      BigDecimal number = (BigDecimal)dynaBean.get("ct");
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
  public String backup(List<String> tableNames, String backupFileLocation, String backupFileRootName,  PrintStream out, PrintStream errOut, boolean dropSchema)
  {
    throw new UnsupportedOperationException("Backup method is not yet implemented for Oracle");
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
    throw new UnsupportedOperationException("ImportFromSQL method is not yet implemented for Oracle");
  }

  @Override
  public String backup(String namespace, String backupFileLocation, String backupFileRootName, PrintStream out, PrintStream errOut, boolean dropSchema)
  {
    throw new UnsupportedOperationException("Backup method is not yet implemented for Oracle");
  }

  @Override
  public void close()
  {
    try
    {
      ((OracleDataSource)this.dataSource).close();
    }
    catch (SQLException e)
    {
      Database.throwDatabaseException(e);
    }
  }

  @Override
  public void createTempTable(String tableName, List<String> columns, String onCommit)
  {
    // TODO : This method is untested
    if (onCommit.equals("DROP"))
    {
      // Can we simply set the dropOnEndOfTransaction flag below? I don't know...
      throw new UnsupportedOperationException();
    }
    
    String statement = "CREATE GLOBAL TEMPORARY TABLE " + tableName + " (" + StringUtils.join(columns, ",") + ") ON COMMIT " + onCommit;

    String undo = "DROP TABLE IF EXISTS " + tableName;

    new DDLCommand(statement, undo, false).doIt();
  }
}
