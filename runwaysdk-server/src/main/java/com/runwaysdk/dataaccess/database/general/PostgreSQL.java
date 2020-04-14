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
/**
 * Created on Sep 17, 2005
 * 
 */
package com.runwaysdk.dataaccess.database.general;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.postgresql.ds.PGSimpleDataSource;
import org.postgresql.ds.common.BaseDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.runwaysdk.constants.MdAttributeFileInfo;
import com.runwaysdk.constants.MdAttributeFloatInfo;
import com.runwaysdk.constants.MdAttributeHashInfo;
import com.runwaysdk.constants.MdAttributeIndicatorInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalTextInfo;
import com.runwaysdk.constants.MdAttributeLongInfo;
import com.runwaysdk.constants.MdAttributeMultiReferenceInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.constants.MdAttributeSymmetricInfo;
import com.runwaysdk.constants.MdAttributeTermInfo;
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.constants.MdAttributeTimeInfo;
import com.runwaysdk.constants.MdAttributeUUIDInfo;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.DuplicateGraphPathException;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdElementDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.AttributeLengthCharacterException;
import com.runwaysdk.dataaccess.database.AddColumnBatchDDLCommand;
import com.runwaysdk.dataaccess.database.AddColumnSingleDDLCommand;
import com.runwaysdk.dataaccess.database.AddGroupIndexDDLCommand;
import com.runwaysdk.dataaccess.database.DDLCommand;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.DatabaseException;
import com.runwaysdk.dataaccess.database.DropColumnDDLCommand;
import com.runwaysdk.dataaccess.database.DuplicateDataDatabaseException;
import com.runwaysdk.dataaccess.database.NumericFieldOverflowException;
import com.runwaysdk.dataaccess.io.CountingOutputStream;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.SubSelectReturnedMultipleRowsException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * @author nathan
 * 
 */
public class PostgreSQL extends AbstractDatabase
{
  public static final String ROOT_SEQUENCE          = "root_sequence";

  public static final String OBJECT_UPDATE_SEQUENCE = "object_sequence_unique_id";

  public static final String TRANSACTION_SEQUENCE   = "transaction_record_sequence";

  public static final String PRIMARY_KEY_SUFFIX     = "_pkey";

  private Logger             logger                 = LoggerFactory.getLogger(PostgreSQL.class);

  private String             databaseNamespace;

  /**
   * Initialize the datasource to point to a PostgreSQL database.
   */
  @Inject
  public PostgreSQL()
  {
    super();

    try
    {
      Class.forName("org.postgresql.Driver");
    }
    catch (ClassNotFoundException e)
    {
    }

    this.databaseNamespace = DatabaseProperties.getNamespace();

    // The container is not providing a pooled datasource
    getDataSource();
  }

  protected synchronized void getDataSource()
  {
    if (this.dataSource == null)
    {
      boolean pooling = DatabaseProperties.getConnectionPooling();

      int maxDbConnections = DatabaseProperties.getMaxConnections();

      if (maxDbConnections < 2)
      {
        maxDbConnections = 2;
      }

      if (pooling)
      {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(this.getDatabaseUrl());
        config.setUsername(DatabaseProperties.getUser());
        config.setPassword(DatabaseProperties.getPassword());
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setMaximumPoolSize(maxDbConnections);

        // If environment is configured for connection pooling, pool connections
        this.dataSource = new HikariDataSource(config);
      }
      // If environment is not configured for connection pooling, do not pool
      // connections
      else
      {
        BaseDataSource pgDataSource = new PGSimpleDataSource();
        pgDataSource.setUrl(this.getDatabaseUrl());
        pgDataSource.setUser(DatabaseProperties.getUser());
        pgDataSource.setPassword(DatabaseProperties.getPassword());

        this.dataSource = (DataSource) pgDataSource;
      }
    }
  }

  protected String getDatabaseUrl()
  {
    return getDatabaseUrl(DatabaseProperties.getDatabaseName());
  }

  protected String getDatabaseUrl(String databaseName)
  {
    return "jdbc:postgresql://" + DatabaseProperties.getServerName() + ":" + DatabaseProperties.getPort() + "/" + databaseName;
  }

  /**
   * Creates a temporary table that lasts for at most the duration of the
   * session. The behavior on transaction commit is configurable with the
   * onCommit parameter.
   * 
   * @param tableName
   *          The name of the temp table.
   * @param columns
   *          An array of vendor-specific formatted columns.
   * @param onCommit
   *          Decides the fate of the temporary table upon transaction commit.
   */
  @Override
  public void createTempTable(String tableName, List<String> columns, String onCommit)
  {
    String statement = "CREATE TEMPORARY TABLE " + tableName + " (" + StringUtils.join(columns, ",") + ") ON COMMIT " + onCommit;

    String undo = "DROP TABLE IF EXISTS " + tableName;

    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * Closes all active connections to the database and cleans up any resources.
   * Depending on your context, you may wish to revoke connect permissions
   * before invoking this.
   */
  public void close()
  {
    if (this.dataSource instanceof HikariDataSource)
    {
      synchronized (this)
      {
        ( (HikariDataSource) this.dataSource ).close();

        this.dataSource = null;
      }
    }
    else
    {
      // Terminate all connections manually.
      LinkedList<String> statements = new LinkedList<String>();
      String dbName = DatabaseProperties.getDatabaseName();

      statements.add("SELECT \n" + "    pg_terminate_backend(pid) \n" + "FROM \n" + "    pg_stat_activity \n" + "WHERE \n" + "    pid <> pg_backend_pid()\n" + "    AND datname = '" + dbName + "'\n" + "    ;");

      executeAsRoot(statements, true);
    }
  }

  /**
   * True if a PosgreSQL namespace has been defined, false otherwise.
   * 
   * @return True if a PosgreSQL namespace has been defined, false otherwise.
   */
  public boolean hasNamespace()
  {
    if (this.databaseNamespace == null || this.databaseNamespace.trim().length() <= 0)
    {
      return false;
    }
    else
    {
      return true;
    }
  }

  /**
   * Returns the defined PostgreSQL namespace, assuming one has been defined.
   * 
   * <b>Postcondition:</b>Assumes {@link this.hasNamespace()} == true
   * 
   * @return the defined PostgreSQL namespace, assuming one has been defined.
   */
  public String getNamespace()
  {
    return this.databaseNamespace;
  }

  /**
   * Installs the runway core. This entails creating a new database and a user
   * for the runway to log in with.
   */
  public void initialSetup(String rootUser, String rootPass, String rootDb)
  {
    // Close any sort of connection to the database
    this.close();

    // Set up the root connection
    BaseDataSource pgRootDataSource = new PGSimpleDataSource();
    pgRootDataSource.setUrl(this.getDatabaseUrl(rootDb));
    pgRootDataSource.setUser(rootUser);
    pgRootDataSource.setPassword(rootPass);
    this.rootDataSource = (DataSource) pgRootDataSource;

    // this.dropNamespace(rootUser, rootPass);
    this.dropDb();
    this.dropUser();
    this.createDb(rootDb);
    this.createUser();

    if (this.hasNamespace())
    {
      this.createNamespace(rootUser, rootPass);
    }

    this.createExtension(rootUser, rootPass);

    this.getDataSource();
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
    executeAsRoot(statements, true);
  }

  /**
   * Creates the database.
   */
  @Override
  public void createDb(String rootDb)
  {
    String dbName = DatabaseProperties.getDatabaseName();
    LinkedList<String> statements = new LinkedList<String>();
//    statements.add("CREATE DATABASE " + dbName + " WITH TEMPLATE = " + rootDb + " ENCODING = 'UTF8'");
    statements.add("CREATE DATABASE " + dbName + " ENCODING = 'UTF8'");
    executeAsRoot(statements, true);
  }

  /**
   * Drops a namespace in the database.
   * 
   * <b>Postcondition:</b>Assumes {@link this.hasNamespace()} == true
   * 
   */
  public void dropNamespace(String rootUser, String rootPass)
  {
    // root needs to log into the application database to create the schema.
    PGSimpleDataSource tempRootDatasource = new PGSimpleDataSource();
    tempRootDatasource.setUrl(this.getDatabaseUrl());
    tempRootDatasource.setUser(rootUser);
    tempRootDatasource.setPassword(rootPass);

    Connection conn = null;
    Statement statement = null;

    try
    {
      conn = tempRootDatasource.getConnection();
      statement = conn.createStatement();
      statement.execute(" DROP SCHEMA " + this.getNamespace() + " CASCADE");
    }
    catch (SQLException e)
    {
      throw new DatabaseException(e);
    }
    finally
    {
      try
      {
        if (statement != null)
        {
          statement.close();
        }
        if (conn != null)
        {
          conn.close();
        }
      }
      catch (Exception exception)
      {
      }
    }
  }

  /**
   * Creates a namespace in the database.
   * 
   * <b>Postcondition:</b>Assumes {@link this.hasNamespace()} == true
   * 
   */
  public void createNamespace(String rootUser, String rootPass)
  {
    // root needs to log into the application database to create the schema.
    PGSimpleDataSource tempRootDatasource = new PGSimpleDataSource();
    tempRootDatasource.setUrl(this.getDatabaseUrl());
    tempRootDatasource.setUser(rootUser);
    tempRootDatasource.setPassword(rootPass);

    Connection conn = null;
    Statement statement = null;

    try
    {
      String userName = DatabaseProperties.getUser();
      String namespace = this.getNamespace();

      conn = tempRootDatasource.getConnection();
      statement = conn.createStatement();
      if (!namespace.trim().equals(userName.trim()))
      {
        statement.execute("CREATE SCHEMA " + namespace + " AUTHORIZATION " + userName);
      }
      else
      {
        statement.execute("ALTER SCHEMA " + namespace + " OWNER TO " + userName);
      }

      LinkedList<String> statements = new LinkedList<String>();
      statements.add("ALTER USER " + userName + " SET search_path = " + namespace + ", public");
      executeAsRoot(statements, true);

    }
    catch (SQLException e)
    {
      throw new DatabaseException(e);
    }
    finally
    {
      try
      {
        if (statement != null)
        {
          statement.close();
        }
        if (conn != null)
        {
          conn.close();
        }
      }
      catch (Exception exception)
      {
      }
    }
  }

  /**
   * Creates a namespace in the database.
   * 
   * <b>Postcondition:</b>Assumes {@link this.hasNamespace()} == true
   * 
   */
  public void createExtension(String rootUser, String rootPass)
  {
    // root needs to log into the application database to create the schema.
    PGSimpleDataSource tempRootDatasource = new PGSimpleDataSource();
    tempRootDatasource.setUrl(this.getDatabaseUrl());
    tempRootDatasource.setUser(rootUser);
    tempRootDatasource.setPassword(rootPass);

    Connection conn = null;
    Statement statement = null;

    try
    {
      conn = tempRootDatasource.getConnection();
      statement = conn.createStatement();
      statement.execute("CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\";");
    }
    catch (SQLException e)
    {
      throw new DatabaseException(e);
    }
    finally
    {
      try
      {
        if (statement != null)
        {
          statement.close();
        }
        if (conn != null)
        {
          conn.close();
        }
      }
      catch (Exception exception)
      {
      }
    }
  }

  /**
   * Drops the database user.
   */
  @Override
  public void dropUser()
  {
    String userName = DatabaseProperties.getUser();

    LinkedList<String> statements = new LinkedList<String>();
    statements.add("DROP USER IF EXISTS " + userName);
    executeAsRoot(statements, true);
  }

  /**
   * Creates the database user.
   */
  @Override
  public void createUser()
  {
    String userName = DatabaseProperties.getUser();

    LinkedList<String> statements = new LinkedList<String>();
    statements.add("CREATE USER " + userName + " ENCRYPTED PASSWORD '" + DatabaseProperties.getPassword() + "'");
    executeAsRoot(statements, true);
  }

  /**
   * Returns a java.sql.Connection object for the database to be used for
   * database DDL statements.
   * 
   * <br/>
   * <b>Precondition:</b> database is running. <br/>
   * <b>Precondition:</b> database.properities file contains correct DB
   * connection settings. <br/>
   * <b>Postcondition:</b> true
   * 
   * @return java.sql.Connection object
   */
  public Connection getDDLConnection()
  {
    return Database.getConnection();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.AbstractDatabase#sharesDDLandDMLconnection()
   */
  @Override
  public boolean sharesDDLandDMLconnection()
  {
    return true;
  }

  /**
   * @return <code>true</code> if the database allows nonrequired columns to
   *         enforce uniqueness
   */
  public boolean allowsUniqueNonRequiredColumns()
  {
    return true;
  }

  /**
   * 
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
   * 
   * @see com.runwaysdk.dataaccess.AbstractDatabase#dropUniqueIndex(java.lang.String,
   *      java.lang.String, java.lang.String, boolean)
   */
  public void dropUniqueIndex(String table, String columnName, String indexName, boolean delete)
  {
    String statement = "DROP INDEX " + indexName;

    String undo = "CREATE UNIQUE INDEX " + indexName + " ON " + table + " (" + columnName + ")";

    new DDLCommand(statement, undo, false).doIt();
  }

  @Transaction
  @Override
  public void dropView(String view, String selectClause, Boolean dropOnEndOfTransaction)
  {
    super.dropView(view, selectClause, dropOnEndOfTransaction);
  }

  @Transaction
  @Override
  public void createView(String view, String selectClause)
  {
    super.createView(view, selectClause);
  }

  @Override
  @Transaction
  public List<String> getViewsByPrefix(String prefix)
  {
    final String viewName = "viewname";
    String sqlStmt = "SELECT " + viewName + " FROM pg_views WHERE viewowner='" + DatabaseProperties.getUser() + "' AND " + viewName + " LIKE '" + prefix.toLowerCase() + "%' ORDER BY " + viewName;
    ResultSet resultSet = this.query(sqlStmt);
    List<String> list = new LinkedList<String>();

    try
    {
      while (resultSet.next())
      {
        list.add(resultSet.getString(viewName));
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
    return list;
  }

  /**
   * @see com.runwaysdk.dataaccess.AbstractDatabase#dropNonUniqueIndex(java.lang.String,
   *      java.lang.String, java.lang.String, delete)
   */
  public void dropNonUniqueIndex(String table, String columnName, String indexName, boolean delete)
  {
    String statement = "DROP INDEX " + indexName;

    String undo = "CREATE INDEX " + indexName + " ON " + table + " (" + columnName + ")";

    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * @see com.runwaysdk.dataaccess.database.Database#uniqueAttributeExists(String,
   *      String, String);
   */
  public boolean uniqueAttributeExists(String table, String columnName, String indexName)
  {
    String sqlStmt = "SELECT pg_attribute.attname \n" + "  FROM pg_attribute, pg_class \n" + " WHERE pg_class.relname = '" + indexName + "' \n" + "   AND pg_attribute.attrelid = pg_class.oid";

    ResultSet resultSet = query(sqlStmt);

    boolean returnResult = false;

    try
    {
      int loopCount = 0;

      if (resultSet.next())
      {
        String attrName = resultSet.getString("attname");

        if (attrName.equals(columnName.toLowerCase()))
        {
          returnResult = true;
        }
        loopCount++;
      }

      if (loopCount != 1)
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

  /**
   * @see com.runwaysdk.dataaccess.database.Database#nonUniqueAttributeExists(String,
   *      String, String);
   */
  public boolean nonUniqueAttributeExists(String table, String columnName, String indexName)
  {
    return uniqueAttributeExists(table, columnName, indexName);
  }

  /**
   * Creates an index on the given table on the columns with the given names.
   * 
   * @param tableName
   *          name of the database table.
   * @param indexName
   *          name of the database index.
   * @param attributeColumnNames
   *          name of the database columns.
   * @param isUnique
   *          true if the index should be unique, false otherwise.
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
   * Drops the index with the given name. The attributes and unique flag are
   * used to rebuild the index in the case of a rolledback transaction.
   * 
   * @param tableName
   *          name of the database table.
   * @param indexName
   *          name of the database index.
   * @param attributeColumnNames
   *          name of the database columns.
   * @param isUnique
   *          true if the index should be unique, false otherwise.
   * @param delete
   *          true if this index is being deleted in this transaction, false
   *          otherwise. The index may be deleted if an attribute is being added
   *          to it. In that case, the value should be <code>false</code>.
   */
  public void dropGroupAttributeIndex(String tableName, String indexName, List<String> attributeNames, boolean isUnique, boolean delete)
  {
    if (this.indexExists(tableName, indexName))
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

      new AddGroupIndexDDLCommand(tableName, indexName, statement, undo).doIt();
    }
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
    String sqlStmt = "SELECT pg_attribute.attname \n" + "  FROM pg_attribute, pg_class \n" + " WHERE pg_class.relname = '" + indexName + "' \n" + "   AND pg_attribute.attrelid = pg_class.oid";

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
   * Returns true if a group attribute index exists with the given name and the
   * given attributes on the given table.
   * 
   * @param tableName
   * @param indexName
   * @param attributeColumnNames
   */
  public boolean groupAttributeIndexExists(String table, String indexName, List<String> attributeColumnNames)
  {
    String sqlStmt = "SELECT pg_attribute.attname \n" + "  FROM pg_attribute, pg_class \n" + " WHERE pg_class.relname = '" + indexName + "' \n" + "   AND pg_attribute.attrelid = pg_class.oid";

    ResultSet resultSet = query(sqlStmt);

    boolean returnResult = true;

    try
    {
      int resultCount = 0;
      while (resultSet.next())
      {
        resultCount++;

        String attrName = resultSet.getString("attname").toLowerCase();
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

  /**
   * Returns true if a group attribute index exists with the given name on the
   * given table.
   * 
   * @param tableName
   * @param indexName
   */
  public boolean groupAttributeIndexExists(String table, String indexName)
  {
    String sqlStmt = "SELECT pg_attribute.attname \n" + "  FROM pg_attribute, pg_class \n" + " WHERE pg_class.relname = '" + indexName + "' \n" + "   AND pg_attribute.attrelid = pg_class.oid";

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

  /**
   * Returns a list of string names of the attributes that participate in a
   * group index for the given table with the index of the given name.
   * 
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
   * @param conn
   *          it is up to the client to manage the connection object.
   * @param attributeNames
   */
  public List<String> getGroupIndexAttributesFromIndexName(String indexName, Connection conn)
  {
    String sqlStmt = "SELECT pg_attribute.attname \n" + "  FROM pg_attribute, pg_class \n" + " WHERE pg_class.relname = '" + indexName + "' \n" + "   AND pg_attribute.attrelid = pg_class.oid";

    ResultSet resultSet = this.query(sqlStmt, conn);

    List<String> attributeNames = new LinkedList<String>();

    try
    {
      while (resultSet.next())
      {
        String attrName = resultSet.getString("attname").toLowerCase();
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
   * Returns the name of the table on which the given index applies. It is up to
   * the client to close the given connection object.
   * 
   * @param indexName
   * @param conx
   * @return name of the table on which the given index applies.
   */
  public String getTableNameForIndex(String indexName, Connection conx)
  {
    String sqlStmt = "SELECT class1.relname " + "  FROM pg_class class1, pg_index index, pg_class class2 " + " WHERE class2.relname = '" + indexName + "' " + "   AND index.indexrelid = class2.oid " + "   AND index.indrelid = class1.oid ";

    ResultSet resultSet = this.query(sqlStmt, conx);

    String tableName = "";

    try
    {
      while (resultSet.next())
      {
        tableName = resultSet.getString("relname").toLowerCase();
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

  /**
   * Adds a floating-point column to a table in the database. Creates an undo
   * DROP command in case transaction management requires a rollback.
   * 
   * @param table
   *          The table that the column is being added to.
   * @param columnName
   *          The name of the new column.
   * @param type
   *          The database type of the new column.
   * @param length
   *          The total number of digits in the new column.
   * @param decimal
   *          The number of digits after the decimal in the new column.
   */
  public void addDecField(String table, String columnName, String type, String length, String decimal)
  {
    String ddlType = formatDDLDecField(type, length, decimal);

    String statement = "ALTER TABLE " + table + " ADD " + columnName + "  " + ddlType;

    String undo = buildDropColumnString(table, columnName);

    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * Returns SQL to add a floating-point column to a table in the database.
   * 
   * @param table
   *          The table that the column is being added to.
   * @param columnName
   *          The name of the new column.
   * @param type
   *          The database type of the new column.
   * @param length
   *          The total number of digits in the new column.
   * @param decimal
   *          The number of digits after the decimal in the new column.
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
    return "ALTER TABLE " + table + " ADD COLUMN " + columnName + "  " + formattedColumnType;
  }

  @Override
  public String formatCharacterField(String type, String length)
  {
    if (type.equals("uuid"))
    {
      return type;
    }

    return super.formatCharacterField(type, length);
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
   * Changes the size of a CHAR columnName in the database. Creates a backup of
   * the original columnName parameters in case transaction management requires
   * a rollback.
   * 
   * @param table
   *          The table containing the CHAR columnName.
   * @param columnName
   *          The CHAR columnName being modified.
   * @param newDbColumnType
   *          the new database column type formatted to the database vendor
   *          syntax.
   * @param oldDbColumnType
   *          the current database column type formatted to the database vendor
   *          syntax.
   */
  public void alterFieldType(String table, String columnName, String newDbColumnType, String oldDbColumnType)
  {
    String statement = "ALTER TABLE " + table + " ALTER COLUMN " + columnName + " TYPE " + newDbColumnType;

    String undo = "ALTER TABLE " + table + " ALTER COLUMN " + columnName + " TYPE " + oldDbColumnType;

    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.AbstractDatabase#createClassTable(java.lang.String)
   */
  public void createClassTable(String tableName)
  {
    String statement = startCreateClassTable(tableName) + " " + endCreateClassTable(tableName);

    String undo = "DROP TABLE " + tableName;

    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.AbstractDatabase#startCreateClassTable(java.lang.String)
   */
  public String startCreateClassTable(String tableName)
  {
    return "CREATE TABLE " + tableName + " ( " + EntityDAOIF.ID_COLUMN + " UUID NOT NULL PRIMARY KEY";
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.dataaccess.AbstractDatabase#createClassTableBatch(java.lang
   * .String, java.util.List<java.lang.String>)
   */
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
    String statement = "ALTER TABLE " + tableName + " ";

    String undo = "ALTER TABLE " + tableName + " ";

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

      statement += "ADD COLUMN " + columnDef;
    }

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

      undo += "DROP " + columnName;
    }

    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * Creates a new table in the database for a relationships. Automatically adds
   * the Component.OID columnName as the primary key.
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
   * minus the closing parenthesis. Automatically adds the Component.OID
   * columnName as the primary key.
   * 
   * @param tableName
   *          The name of the new table.
   */
  @Override
  public String startCreateRelationshipTableBatch(String tableName)
  {
    return "CREATE TABLE " + tableName + " ( " + EntityDAOIF.ID_COLUMN + " UUID NOT NULL PRIMARY KEY, \n" + RelationshipDAOIF.PARENT_OID_COLUMN + " UUID NOT NULL, \n" + RelationshipDAOIF.CHILD_OID_COLUMN + " UUID NOT NULL";
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
    String statement = "CREATE ";
    if (isUnique)
    {
      statement += " UNIQUE ";
    }
    statement += " INDEX " + index1Name + " ON " + tableName + " (" + RelationshipDAOIF.PARENT_OID_COLUMN + ", " + RelationshipDAOIF.CHILD_OID_COLUMN + ")";

    String undo = "DROP INDEX " + index1Name;
    new DDLCommand(statement, undo, false).doIt();

    statement = "CREATE INDEX " + index2Name + " ON " + tableName + " (" + RelationshipDAOIF.CHILD_OID_COLUMN + ")";

    undo = "DROP INDEX " + index1Name;
    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * @see com.runwaysdk.dataaccess.database.Database#createEnumerationTable(String,
   *      String);
   */
  public void createEnumerationTable(String tableName, String oid)
  {
    String statement = "CREATE TABLE " + tableName + " \n" + "(" + MdEnumerationDAOIF.SET_ID_COLUMN + "                CHAR(" + Database.DATABASE_SET_ID_SIZE + ") NOT NULL, \n" + MdEnumerationDAOIF.ITEM_ID_COLUMN + " UUID NOT NULL)";

    String undo = "DROP TABLE " + tableName;
    new DDLCommand(statement, undo, false).doIt();

    String indexName = this.createIdentifierFromId(oid);
    statement = "CREATE UNIQUE INDEX " + indexName + " ON " + tableName + " (" + MdEnumerationDAOIF.SET_ID_COLUMN + ", " + MdEnumerationDAOIF.ITEM_ID_COLUMN + ")";

    undo = "DROP INDEX " + indexName;
    new DDLCommand(statement, undo, false).doIt();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.dataaccess.AbstractDatabase#deleteClassTable(java.lang.String
   * )
   */
  public void dropClassTable(String tableName)
  {
    String statement = "DROP TABLE " + tableName;

    String undo = "CREATE TABLE " + tableName + " ( " + EntityDAOIF.ID_COLUMN + " CHAR(" + Database.DATABASE_ID_SIZE + ") NOT NULL PRIMARY KEY )";
    new DDLCommand(statement, undo, true).doIt();
  }

  /**
   * Drops an entire table from the database for a relationship. An undo command
   * is created that will recreate the table if transaction management requires
   * a rollback. However, the undo will <b>not </b> recreate all of the columns
   * in the table, only the OID.
   * 
   * @param table
   *          The name of the table to drop.
   * @param index1Name
   *          The name of the 1st index used by the given table.
   * @param index2Name
   *          The name of the 1st index used by the given table.
   * @param isUnique
   *          Indicates whether the parent_oid child_oid pair should be made
   *          unique. This should only be done on concrete relationship types.
   */
  public void dropRelationshipTable(String tableName, String index1Name, String index2Name, boolean isUnique)
  {
    String statement = "DROP INDEX " + index1Name;

    String undo = "CREATE ";
    if (isUnique)
    {
      undo += " UNIQUE ";
    }
    undo += " INDEX " + index1Name + " ON " + tableName + " (" + RelationshipDAOIF.PARENT_OID_COLUMN + ", " + RelationshipDAOIF.CHILD_OID_COLUMN + ")";
    new DDLCommand(statement, undo, true).doIt();

    statement = "DROP INDEX " + index2Name;
    undo = "CREATE INDEX " + index2Name + " ON " + tableName + " (" + RelationshipDAOIF.CHILD_OID_COLUMN + ")";
    new DDLCommand(statement, undo, true).doIt();

    statement = "DROP TABLE " + tableName;
    undo = this.startCreateRelationshipTableBatch(tableName) + " " + this.endCreateClassTable(tableName);
    new DDLCommand(statement, undo, true).doIt();
  }

  /**
   * @see com.runwaysdk.dataaccess.database.Database#dropEnumerationTable(String,
   *      String);
   */
  public void dropEnumerationTable(String tableName, String oid)
  {
    String statement = "DROP TABLE " + tableName;

    String indexName = this.createIdentifierFromId(oid);

    String undo = "CREATE UNIQUE INDEX " + indexName + " ON " + tableName + " (" + MdEnumerationDAOIF.SET_ID_COLUMN + ", " + MdEnumerationDAOIF.ITEM_ID_COLUMN + ")";

    new DDLCommand(statement, undo, true).doIt();
  }

  // /*
  // * (non-Javadoc)
  // *
  // * @see com.runwaysdk.dataaccess.AbstractDatabase#getTables()
  // */
  // public List<String> getTables()
  // {
  // String username = DatabaseProperties.getUser();
  // LinkedList<String> tables = new LinkedList<String>();
  //
  // // get the sequence value
  // List results =
  // this.query("SELECT tablename FROM pg_tables WHERE tableowner ='" + username
  // + "'");
  //
  // Iterator i = results.iterator();
  // while (i.hasNext())
  // {
  // tables.add( ( (DynaBean) i.next()
  // ).get("tablename").toString().toLowerCase());
  // }
  // return tables;
  // }

  /**
   * @see com.runwaysdk.dataaccess.AbstractDatabase#tableExists(java.lang.String)
   */
  public boolean tableExists(String tableName)
  {
    String sqlStmt = "SELECT relname FROM pg_class WHERE relname = '" + tableName + "'";

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
   * 
   * @see com.runwaysdk.dataaccess.AbstractDatabase#getColumnNames(java.lang.String)
   */
  @Override
  public List<String> getColumnNames(String table)
  {
    Connection conx = Database.getConnection();
    Statement statement = null;
    ResultSet resultSet = null;

    LinkedList<String> attributeList = new LinkedList<String>();

    try
    {
      String sqlStmt = "SELECT * FROM " + table + " WHERE 1=0";

      statement = conx.createStatement();
      resultSet = statement.executeQuery(sqlStmt);

      ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

      for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++)
      {
        attributeList.add( ( resultSetMetaData.getColumnName(i) ).toLowerCase());
      }

      conx.commit();

      return attributeList;
    }
    catch (SQLException ex)
    {
      this.throwDatabaseException(ex);
      return null;
    }
    finally
    {
      try
      {
        if (resultSet != null)
          resultSet.close();
        if (statement != null)
          statement.close();
        Database.closeConnection(conx);
      }
      catch (SQLException e)
      {
        this.throwDatabaseException(e);
      }
    }
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
    String sqlStmt = "SELECT pg_attribute.attname  \n" + "  FROM pg_attribute, pg_class  \n" + " WHERE pg_class.relname = '" + tableName + "' \n" + "   AND pg_attribute.attrelid = pg_class.oid \n" + "   AND pg_attribute.attname = '" + columnName + "' \n";

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
   * 
   * @see com.runwaysdk.dataaccess.AbstractDatabase#createObjectSequence()
   */
  @Override
  public void createObjectSequence()
  {
    this.execute("CREATE SEQUENCE " + OBJECT_UPDATE_SEQUENCE + " INCREMENT 1 START " + Database.STARTING_SEQUENCE_NUMBER);

    this.execute("CREATE SEQUENCE " + ROOT_SEQUENCE + " INCREMENT 1 START 1280");
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.AbstractDatabase#getNextSequenceNumber()
   */
  @Override
  public String getNextSequenceNumber()
  {
    // get the sequence value
    String sqlStmt = "SELECT NEXTVAL('" + OBJECT_UPDATE_SEQUENCE + "') AS nextval";

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
   * @see com.runwaysdk.dataaccess.AbstractDatabase#getNextSequenceNumber()
   */
  @Override
  public String generateRootId(MdTypeDAO mdTypeDAO)
  {
    // get the sequence value
    String sqlStmt = "SELECT NEXTVAL('" + ROOT_SEQUENCE + "') AS nextval";

    ResultSet resultSet = query(sqlStmt);

    String returnResult = "";

    try
    {
      resultSet.next();

      long value = resultSet.getLong("nextval");

      String id = String.format("%06x", ( 0xFFFFFF & value ));

      return id;
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
    this.execute("CREATE SEQUENCE " + TRANSACTION_SEQUENCE + " INCREMENT 1 START 1");
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
      this.execute("DROP SEQUENCE " + TRANSACTION_SEQUENCE);
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
    // get the sequence value
    String sqlStmt = "SELECT NEXTVAL('" + TRANSACTION_SEQUENCE + "') AS nextval";

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
    String statement = "ALTER TABLE " + tableName + " ";
    for (int i = 0; i < numberOfTempFields; i++)
    {
      if (i != 0)
      {
        statement += ", ";
      }

      statement += "ADD COLUMN " + columnName + "_" + i + " " + columnType;
    }

    statement += ")";

    String undo = "ALTER TABLE " + tableName + " ";
    for (int i = 0; i < numberOfTempFields; i++)
    {
      if (i != 0)
      {
        undo += ", ";
      }

      undo += "DROP " + columnName + "_" + i;
    }
    undo += ")";

    new DDLCommand(statement, undo, false).doIt();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.AbstractDatabase#addField(java.lang.String,
   * java.lang.String, java.lang.String, java.lang.String)
   */
  public void addField(String table, String columnName, String type, String size)
  {
    String statement = buildAddColumnString(table, columnName, type);

    if (size != null && !type.equalsIgnoreCase("uuid"))
    {
      statement += "(" + size + ")";
    }

    String undo = "ALTER TABLE " + table + " DROP " + columnName;

    new DDLCommand(statement, undo, false).doIt();
  }

  public void bindPreparedStatementValue(PreparedStatement prepStmt, int index, Object value, String dataType)
  {
    try
    {
      if (dataType.equals(MdAttributeReferenceInfo.CLASS) || dataType.equals(MdAttributeTermInfo.CLASS) || dataType.equals(MdAttributeUUIDInfo.CLASS) || dataType.equals(MdAttributeStructInfo.CLASS) || dataType.equals(MdAttributeLocalCharacterInfo.CLASS) || dataType.equals(MdAttributeLocalTextInfo.CLASS))
      {
        String va = (String) value;
        if (value == null || va.equals(""))
        {
          prepStmt.setNull(index, java.sql.Types.OTHER);
        }
        else
        {
          prepStmt.setObject(index, UUID.fromString(va));
        }
      }
      else if (dataType.equals(MdAttributeFloatInfo.CLASS))
      {
        if ( ( (String) value ).trim().equals(""))
        {
          prepStmt.setNull(index, java.sql.Types.FLOAT);
        }
        else
        {
          double fl = Double.parseDouble((String) value);

          prepStmt.setDouble(index, fl);
        }
      }
      else
      {
        super.bindPreparedStatementValue(prepStmt, index, value, dataType);
      }
    }
    catch (SQLException ex)
    {
      this.throwDatabaseException(ex);
    }

  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.dataaccess.AbstractDatabase#addFieldBatch(java.lang.String,
   * java.lang.String, java.lang.String, java.lang.String)
   */
  public String addFieldBatch(String table, String columnName, String type, String size)
  {
    String statement = columnName + "  " + type;

    if (size != null && !type.equalsIgnoreCase("uuid"))
    {
      statement += "(" + size + ")";
    }

    return statement;
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
    String statement = buildAddColumnString(table, columnName, formattedColumnType);

    String undo = buildDropColumnString(table, columnName);

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

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.dataaccess.AbstractDatabase#formatJavaToSQL(java.lang.String,
   * java.lang.String, boolean)
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
        dataType.equals(MdAttributeUUIDInfo.CLASS) || dataType.equals(MdAttributeReferenceInfo.CLASS) || dataType.equals(MdAttributeTermInfo.CLASS) || dataType.equals(MdAttributeFileInfo.CLASS) || dataType.equals(MdAttributeEnumerationInfo.CLASS) || dataType.equals(MdAttributeMultiReferenceInfo.CLASS) || dataType.equals(MdAttributeIndicatorInfo.CLASS))
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
    dataType.equals(MdAttributeBooleanInfo.CLASS) || dataType.equals(MdAttributeUUIDInfo.CLASS) || dataType.equals(MdAttributeIntegerInfo.CLASS) || dataType.equals(MdAttributeLongInfo.CLASS) || dataType.equals(MdAttributeFloatInfo.CLASS) || dataType.equals(MdAttributeDoubleInfo.CLASS) || dataType.equals(MdAttributeDecimalInfo.CLASS) ||
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

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.AbstractDatabase#formatColumnAlias(java.lang.
   * String )
   */
  public String formatColumnAlias(String columnAlias)
  {
    return "AS " + columnAlias;
  }

  /**
   * Creates an alias in the syntax of the specific database vendor for a
   * fictitous column of the given datatype. This allows Select statements to be
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
    dataType.equals(MdAttributeCharacterInfo.CLASS) || dataType.equals(MdAttributeTextInfo.CLASS) || dataType.equals(MdAttributeClobInfo.CLASS) ||
    // Encryption
        dataType.equals(MdAttributeHashInfo.CLASS) || dataType.equals(MdAttributeSymmetricInfo.CLASS) ||
        // References
        dataType.equals(MdAttributeFileInfo.CLASS) || dataType.equals(MdAttributeEnumerationInfo.CLASS) || dataType.equals(MdAttributeIndicatorInfo.CLASS)
    // Non Primitives
    )
    {
      bogusValue = "''";
    }
    else if (dataType.equals(MdAttributeUUIDInfo.CLASS) || dataType.equals(MdAttributeStructInfo.CLASS) || dataType.equals(MdAttributeLocalCharacterInfo.CLASS) || dataType.equals(MdAttributeLocalTextInfo.CLASS) || dataType.equals(MdAttributeReferenceInfo.CLASS) || dataType.equals(MdAttributeTermInfo.CLASS))
    {
      bogusValue = "'00000000-0000-0000-0000-000000000000'::uuid";
    }
    else if (dataType.equals(MdAttributeBlobInfo.CLASS))
    {
      bogusValue = "''::bytea";
    }
    else if (dataType.equals(MdAttributeTimeInfo.CLASS))
    {
      bogusValue = "time '12:00:00'";
    }
    else if (dataType.equals(MdAttributeDateInfo.CLASS))
    {
      bogusValue = "date '2000-01-01'";
    }
    else if (dataType.equals(MdAttributeDateTimeInfo.CLASS))
    {
      bogusValue = "timestamp '2000-01-01 12:00:00'";
    }
    // Don't format attributes of these types.
    else if (// Primitive
    dataType.equals(MdAttributeIntegerInfo.CLASS) || dataType.equals(MdAttributeLongInfo.CLASS) || dataType.equals(MdAttributeFloatInfo.CLASS) || dataType.equals(MdAttributeDoubleInfo.CLASS) || dataType.equals(MdAttributeBooleanInfo.CLASS) || dataType.equals(MdAttributeDecimalInfo.CLASS))
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
   * 
   * @param errorCode
   * @param errorMessage
   */
  public void throwDatabaseException(SQLException ex, String debugMsg)
  {
    String errorCode = ex.getSQLState();
    String errorMessage = ex.getMessage();

    // In PostgreSQL, no more queries to the database during the session can
    // occur until the
    // transaction block has been closed. This is done with a rollback.
    try
    {
      try
      {
        // If there is a savepoint, then don't rollback. The calling code that
        // has set a savepoint
        // should release it.
        Database.peekCurrentSavepoint();
      }
      catch (EmptyStackException e)
      {
        Connection conn = Database.getConnection();
        conn.rollback();
      }
    }
    catch (SQLException sqlEx)
    {
      throw new DatabaseException(sqlEx);
    }

    if (errorCode == null)
    {
      throw new DatabaseException(errorMessage, debugMsg);
    }

    errorCode = errorCode.trim();

    if (errorCode.equals("23505"))
    {
      int startIndex = errorMessage.indexOf("\"") + 1;
      int endIndex = errorMessage.indexOf("\"", startIndex);
      String indexName = errorMessage.substring(startIndex, endIndex);

      if (indexName.substring(0, 4).equalsIgnoreCase(MdRelationshipDAOIF.INDEX_PREFIX))
      {
        String error = "Constraint [" + indexName + "] on relationship violated";

        throw new DuplicateGraphPathException(error);
      }
      else
      {
        String error = "Constraint [" + indexName + "] on object violated";

        int pkeyStartIndex = indexName.indexOf(PRIMARY_KEY_SUFFIX);

        if (indexName.contains(PRIMARY_KEY_SUFFIX) && indexName.substring(pkeyStartIndex, indexName.length()).equals(PRIMARY_KEY_SUFFIX))
        {
          String tableName = indexName.substring(0, pkeyStartIndex).trim();

          throw new DuplicateDataDatabaseException(error, ex, indexName, tableName);
        }
        else
        {
          throw new DuplicateDataDatabaseException(error, ex, indexName);
        }
      }

    }

    if (errorCode.equals("21000"))
    {
      String errMsg = "Subquery returns more than 1 row";
      throw new SubSelectReturnedMultipleRowsException(errMsg);
    }

    if (errorCode.equals("22003"))
    {
      String errMsg = "Numeric input overflowed the bounds of its column";
      throw new NumericFieldOverflowException(errMsg);
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
  public int setBlobAsBytes(String table, String columnName, String oid, long pos, byte[] bytes, int offset, int length)
  {
    Connection conn = Database.getConnection();
    Statement statement = null;
    ResultSet resultSet = null;
    int written = 0;
    try
    {
      // get the blob
      statement = conn.createStatement();
      String select = "SELECT " + columnName + " FROM " + table + " WHERE " + EntityDAOIF.ID_COLUMN + " = '" + oid + "'";
      String update = "UPDATE " + table + " SET " + columnName + " = " + "? WHERE " + EntityDAOIF.ID_COLUMN + " = '" + oid + "'";
      resultSet = statement.executeQuery(select);
      resultSet.next();
      byte[] resultBytes = resultSet.getBytes(columnName);

      // null check
      if (resultBytes == null)
      {
        // because this method is used to place byte in specific positions, it
        // wouldn't
        // make sense to insert the bytes into a null columnName as it defeats
        // the
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

        // check to see if the bytes will run longer than the current length of
        // the blob length.
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
    int written = 0;
    PreparedStatement prepared = null;
    try
    {
      // get the blob
      String update = "UPDATE " + table + " SET " + columnName + " = " + "? WHERE " + EntityDAOIF.ID_COLUMN + " = '" + oid + "'";
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
      try
      {
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
   * Returns the value of a blob as a byte array. It is up to the client to
   * close the database connection.
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

      if (resultSet.next())
      {
        returnBytes = resultSet.getBytes(columnName);
      }
      else
      {
        returnBytes = new byte[0];
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

      if (bytes != null)
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
   * This is a special method used to update the baseClass attribute of MdType
   * and it is used only within the TransactionManagement aspect, hence it takes
   * a JDBC connection object as a parameter.
   * 
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
    PreparedStatement prepared = null;

    int written = 0;

    try
    {
      // clear the blob
      this.truncateBlob(table, classColumnName, mdTypeId, 0, conn);

      // get the blob
      String update = "UPDATE " + table + " SET " + classColumnName + " = ?, " + sourceColumnName + " = ? WHERE " + EntityDAOIF.ID_COLUMN + " = '" + mdTypeId + "'";
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
   * This is a special method used to update the generated server, common, and
   * client classes for an MdType. This method is used only within the
   * TransactionManagement aspect, hence it takes a JDBC connection object as a
   * parameter. It is up to the client to close the connection object.
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
  public int updateMdFacadeGeneratedClasses(String mdFacadeId, String table, String serverClassesColumnName, byte[] serverClassesBytes, String commonClassesColumnName, byte[] commonClassesBytes, String clientClassesColumnName, byte[] clientClassesBytes, Connection conn)
  {
    PreparedStatement prepared = null;

    int written = 0;

    try
    {
      // clear the blob
      this.truncateBlob(table, serverClassesColumnName, mdFacadeId, 0, conn);
      this.truncateBlob(table, commonClassesColumnName, mdFacadeId, 0, conn);
      this.truncateBlob(table, clientClassesColumnName, mdFacadeId, 0, conn);

      // get the blob
      String update = "UPDATE " + table + " SET " + serverClassesColumnName + " = ?, " + commonClassesColumnName + " = ?, " + clientClassesColumnName + " = ? " + " WHERE " + EntityDAOIF.ID_COLUMN + " = '" + mdFacadeId + "'";
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
    StringBuffer statement = new StringBuffer();
    statement.append("CREATE TABLE " + Database.PROPERTIES_TABLE + " ( " + EntityDAOIF.ID_COLUMN + " CHAR(" + Database.DATABASE_ID_SIZE + ") NOT NULL," + Database.VERSION_NUMBER + " CHAR(16) NOT NULL PRIMARY KEY);");
    statement.append("INSERT INTO " + Database.PROPERTIES_TABLE + "(" + EntityDAOIF.ID_COLUMN + ", " + Database.VERSION_NUMBER + ") VALUES ('" + Database.RUNWAY_METADATA_VERSION_PROPERTY + "', '" + RunwayMetadataVersion.getCurrentVersion().toString() + "');");

    this.execute(statement.toString());
  }

  @Override
  public void buildChangelogTable()
  {
    StringBuffer statement = new StringBuffer();
    statement.append("CREATE TABLE changelog ( change_number BIGINT NOT NULL, complete_dt TIMESTAMP NOT NULL, applied_by VARCHAR(100) NOT NULL, description VARCHAR(500) NOT NULL);");
    statement.append("ALTER TABLE changelog ADD CONSTRAINT Pkchangelog PRIMARY KEY (change_number);");

    this.execute(statement.toString());
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
  public String backup(List<String> tableNames, String backupFileLocation, String backupFileRootName, PrintStream out, PrintStream errOut, boolean dropSchema)
  {
    String backupFileName = backupFileRootName + ".sql";

    String qualifiedBackupFile = backupFileLocation + File.separator + backupFileName;

    String databaseBinDirectory = DatabaseProperties.getDatabaseBinDirectory();

    String dbDumpTool = "" + databaseBinDirectory + File.separator + DatabaseProperties.getDataDumpExecutable() + "";

    ArrayList<String> argList = new ArrayList<String>();
    argList.add(dbDumpTool);
    argList.add("-U");
    argList.add(DatabaseProperties.getUser());
    argList.add("-h");
    argList.add("127.0.0.1");
    argList.add("-p");
    argList.add(Integer.toString(DatabaseProperties.getPort()));

    // -D is for 8.3, it is not needed for 8.4
    // argList.add("-D");
    argList.add("-b");
    if (dropSchema)
    {
      argList.add("-c");
    }

    // pg dump compressed format
    // argList.add("--format");
    // argList.add("c");

    argList.add("-f");
    argList.add("" + qualifiedBackupFile + "");

    for (String tableName : tableNames)
    {
      argList.add("-t");
      argList.add(tableName);
    }

    argList.add(DatabaseProperties.getDatabaseName());

    ProcessBuilder pb = new ProcessBuilder(argList);

    logger.info("Running backup command [" + StringUtils.join(argList, " ") + "].");

    try
    {
      ProcessReader reader = new ProcessReader(pb, out, errOut);
      reader.start();
    }
    catch (Exception e)
    {
      throw new ProgrammingErrorException(e);
    }

    return qualifiedBackupFile;
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
  public String backup(String namespace, String backupFileLocation, String backupFileRootName, PrintStream out, PrintStream errOut, boolean dropSchema)
  {
    String backupFileName = backupFileRootName + ".sql";

    String qualifiedBackupFile = backupFileLocation + File.separator + backupFileName;

    String databaseBinDirectory = DatabaseProperties.getDatabaseBinDirectory();

    String dbDumpTool = "" + databaseBinDirectory + File.separator + DatabaseProperties.getDataDumpExecutable() + "";

    ArrayList<String> argList = new ArrayList<String>();
    argList.add(dbDumpTool);
    argList.add("-U");
    argList.add(DatabaseProperties.getUser());
    argList.add("-h");
    argList.add("127.0.0.1");
    argList.add("-p");
    argList.add(Integer.toString(DatabaseProperties.getPort()));

    // -D is for 8.3, it is not needed for 8.4
    // argList.add("-D");
    argList.add("-b");
    if (dropSchema)
    {
      argList.add("-c");
    }

    // pg dump compressed format
    // argList.add("--format");
    // argList.add("c");

    argList.add("-f");
    argList.add("" + qualifiedBackupFile + "");

    // thanks to the ddms schema, we no longer need to manually specify every
    // table name
    argList.add("-n");
    argList.add(namespace);

    argList.add(DatabaseProperties.getDatabaseName());

    ProcessBuilder pb = new ProcessBuilder(argList);

    logger.info("Running backup command [" + StringUtils.join(argList, " ") + "].");

    try
    {
      ProcessReader reader = new ProcessReader(pb, out, errOut);
      reader.start();
    }
    catch (Exception e)
    {
      throw new ProgrammingErrorException(e);
    }

    return qualifiedBackupFile;
  }

  /**
   * Imports the given SQL file into the database. The password will be read
   * from a pgpass file, so make sure it exists there before running this.
   * 
   * @param restoreSQLFile
   * @param printStream
   */
  @Override
  public void importFromSQL(String restoreSQLFile, PrintStream out, PrintStream errOut)
  {
    String databaseBinDirectory = DatabaseProperties.getDatabaseBinDirectory();

    String dbImportTool = "" + databaseBinDirectory + File.separator + "psql" + "";

    ArrayList<String> argList = new ArrayList<String>();
    argList.add(dbImportTool);
    argList.add("-h");
    argList.add("127.0.0.1");
    argList.add("-p");
    argList.add(Integer.toString(DatabaseProperties.getPort()));
    argList.add("-U");
    argList.add(DatabaseProperties.getUser());
    argList.add("-d");
    argList.add(DatabaseProperties.getDatabaseName());
    argList.add("--file");
    argList.add(restoreSQLFile);
    argList.add("--no-password");
    argList.add("--quiet");

    logger.info("Importing SQL file with command [" + StringUtils.join(argList, " ") + "] in a new process and waiting for the process to exit.");

    ProcessBuilder pb = new ProcessBuilder(argList);

    try
    {
      ProcessReader reader = new ProcessReader(pb, out, errOut);
      reader.start();
    }
    catch (Exception e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Override
  public String getMinusOperator()
  {
    return "EXCEPT";
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.database.general.AbstractDatabase#
   * validateClobLength (java.lang.String, com.runwaysdk.dataaccess.AttributeIF)
   */
  @Override
  public void validateClobLength(String value, AttributeIF attributeIF)
  {
    // super.validateClobLength(value, attributeIF);
    //
    /*
     * Size of 1 gb: 1 gb = 1024M, 1M = 1024K, 1K=1024 bytes
     */
    int maxLength = 1024 * 1024 * 1024;

    CountingOutputStream cos = new CountingOutputStream();

    try
    {
      Writer writer = new OutputStreamWriter(cos, "UTF-8");
      writer.write(value);
      writer.flush();
      writer.close();

      int total = cos.getTotal();

      if (total > maxLength)
      {
        String error = "Attribute [" + attributeIF.getName() + "] on type [" + attributeIF.getDefiningClassType() + "] is too long.";
        throw new AttributeLengthCharacterException(error, attributeIF, maxLength);
      }
    }
    catch (IOException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  public List<String> getReferencingViews(MdElementDAOIF mdElement)
  {
    List<String> viewNames = new LinkedList<String>();

    StringBuffer buffer = new StringBuffer();
    buffer.append("SELECT DISTINCT dependee.relname AS view_name ");
    buffer.append("FROM pg_depend ");
    buffer.append("JOIN pg_rewrite ON pg_depend.objid = pg_rewrite.oid ");
    buffer.append("JOIN pg_class as dependee ON pg_rewrite.ev_class = dependee.oid ");
    buffer.append("JOIN pg_class as dependent ON pg_depend.refobjid = dependent.oid ");
    buffer.append("JOIN pg_attribute ON pg_depend.refobjid = pg_attribute.attrelid ");
    buffer.append("    AND pg_depend.refobjsubid = pg_attribute.attnum ");
    buffer.append("WHERE dependent.relname = '" + mdElement.getTableName() + "' ");
    buffer.append("AND pg_attribute.attnum > 0 ");

    try
    {
      ResultSet result = null;

      try
      {
        result = this.query(buffer.toString());

        while (result.next())
        {
          String viewName = result.getString("view_name");

          viewNames.add(viewName);
        }
      }
      finally
      {
        if (result != null)
        {
          result.close();
        }
      }
    }
    catch (SQLException e)
    {
      throw new DatabaseException(e);
    }

    return viewNames;
  }
}
