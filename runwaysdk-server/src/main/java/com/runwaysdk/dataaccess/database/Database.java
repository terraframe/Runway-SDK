/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
/**
 * Created on Aug 11, 2004
 * 
 */
package com.runwaysdk.dataaccess.database;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.cache.HardCodedMetadataIterator;
import com.runwaysdk.dataaccess.database.general.AbstractDatabase;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;

/**
 * Database manages access to the database. It contains a singleton instance of
 * <code>AbstractDatabase</code>. The concrete class is chosen based on the
 * value of "databaseVendor" in config/server/server.properties. If the
 * databaseVendor is unrecognized, Database defaults to mysql, and prints an
 * error.
 * 
 * @author Eric
 * @version $Revision 1.0 $
 * @since 1.4
 */
public class Database
{
  /**
   * Logs DML and DDL SQL statements to standard out. This is used to produce a
   * SQL script that will record a Runway refactor.
   */
  private static boolean     logDMLandDDLStatements     = false;

  public static final int    STARTING_SEQUENCE_NUMBER   = 1000;

  /**
   * Maximum length of a database identifier.
   * 
   * This might need to be a property set per application, but databases generally support
   * 64 character identifiers except for Postgres, which states that it's 63:
   * http://www.postgresql.org/docs/9.1/static/sql-syntax-lexical.html Section 4.1.1.
   */
  public static final int    MAX_DB_IDENTIFIER_SIZE     = 30;

  /**
   * Maximum length of an attribute name
   */
  public static final int    MAX_ATTRIBUTE_NAME_SIZE    = 28;

  /**
   * The size, in characters, of the ID strings for each object in the database.
   */
  public static final String DATABASE_ID_SIZE           = "64";

  /**
   * Magic number for an unlimited text length on clob attributes
   */
  public static final int    UNLIMITED_TEXT_LENGTH      = -1;

  /**
   * The size, in characters, of the type strings for each object in the
   * database.
   */
  public static final String DATABASE_TYPE_SIZE         = "255";

  public static final String DATABASE_SET_ID_SIZE       = "32";

  public static final String FIXED_CHARACTER_SUFFIX     = "_FIXED";

  /**
   * The name of the table containing the current version
   */
  public static final String PROPERTIES_TABLE           = "dynamic_properties";

  /**
   * The key of the only row in the dynamic properties table
   */
  public static final String VERSION_TIMESTAMP_PROPERTY = "000000000000000000000";

  /**
   * The key of the only row in the dynamic properties table
   */
  public static final String RUNWAY_VERSION_PROPERTY    = "000000000000000000001";

  /**
   * The name of the version number column on the properties
   */
  public static final String VERSION_NUMBER             = "version_number";

  public static final String PROPERTIES_ID_COLUMN       = "id";

  /**
   * The initial version number
   */
  public static final String INITIAL_VERSION            = "0000000000000000";

  /**
   * The singleton {@link Database} instance.
   */
  private static Database    instance;

  /**
   * Class used to communicate to the concrete database
   */
  private AbstractDatabase   database;

  /**
   * Uses google Guice to inject the concrete database implementation.
   * 
   * @param database
   *          Concrete database class
   */
  @Inject
  protected Database(AbstractDatabase database)
  {
    this.database = database;
  }

  /**
   * The accessor function to the instance of <code>AbstractDatabase</code>.
   * 
   * @return The singelton instance of <code>AbstractDatabase</code>
   */
  public synchronized static AbstractDatabase instance()
  {
    if (instance == null)
    {
      instance = new DatabaseInjector().getDatabase();
    }

    return instance.database;
  }

  /**
   * 
   * @return
   */
  public static boolean loggingDMLandDDLstatements()
  {
    return logDMLandDDLStatements;
  }

  public static void enableLoggingDMLAndDDLstatements(boolean enable)
  {
    logDMLandDDLStatements = enable;
  }

  /**
   * Builds a JDBC prepared <code>INSERT</code> statement for the given columns. <br>
   * 
   * @param table
   *          The table to insert into.
   * @param columnNames
   *          The names of the columns being inserted.
   * @param prepStmtVars
   *          usually just a "?", but some types require special functions.
   * @param values
   *          The values of the columns being inserted.
   * 
   * @return SQL insert statement
   */
  public static PreparedStatement buildPreparedSQLInsertStatement(String table, List<String> columnNames, List<String> prepStmtVars, List<Object> values, List<String> attributeTypes)
  {
    return instance().buildPreparedSQLInsertStatement(table, columnNames, prepStmtVars, values, attributeTypes);
  }

  /**
   * Builds an <code>INSERT</code> statement for the given fields. <br>
   * 
   * @param table
   *          The table to insert into.
   * @param columnNames
   *          The names of the fields being inserted.
   * @param values
   *          The values of the fields being inserted.
   * @param attributeTypes
   *          The core datatypes of the fields being inserted.
   * 
   * @return SQL insert statement
   */
  public static String buildSQLinsertStatement(String table, List<String> columnNames, List<Object> values, List<String> attributeTypes)
  {
    return instance().buildSQLinsertStatement(table, columnNames, values, attributeTypes);
  }

  /**
   * Builds an <code>Update</code> statement for the given fields. This is used
   * primarily for logging SQL statements. For actual database update
   * statements, please use the method that returns a
   * <code>PreparedStatement</code> object. <br>
   * 
   * @param table
   *          The table to insert into.
   * @param columnNames
   *          The names of the fields being inserted.
   * @param values
   *          The values of the fields to update.
   * @param attributeTypes
   *          The core datatypes of the fields to update.
   * @param id
   *          id of the object to update.
   * 
   * @return <code>UPDATE</code> SQL statement String
   */
  public static String buildSQLupdateStatement(String table, List<String> columnNames, List<Object> values, List<String> attributeTypes, String id)
  {
    return instance().buildSQLupdateStatement(table, columnNames, values, attributeTypes, id);
  }

  /**
   * Installs the runway core. This entails creating a new database, creating a
   * user for the runway to log in with, and setting any necessary permissions.
   */
  public static void initialSetup(String rootUser, String rootPass, String rootDb)
  {
    instance().initialSetup(rootUser, rootPass, rootDb);
  }

  /**
   * Executes a List of DML SQL statements in the database as one batch. This
   * method simply returns if there are no elements in the List.
   * 
   * <br>
   * <b>Precondition: </b> sqlStmts is not null
   * 
   * @param sqlStmts
   *          List of SQL DML statements to execute in batch.
   * @return int array where each element corresponds to one statement result in
   *         the batch.
   */
  public static int[] executeBatch(List<String> sqlStmts)
  {
    return instance().executeBatch(sqlStmts);
  }

  /**
   * Executes List of PreparedStatements in the database. This method simply
   * returns if there are no elements in the List.
   * 
   * <br>
   * <b>Precondition: </b> preparedStmts is not null
   * 
   * @param preparedStmts
   *          List of PreparedStatements.
   * @return int array where each element corresponds to the execution of one
   *         statement from the list.
   */
  public static int[] executeStatementBatch(List<PreparedStatement> preparedStmts)
  {
    return instance().executeStatementBatch(preparedStmts);
  }

  /**
   * Returns the value of a clob for the column on the table for the object with
   * the given id.
   * 
   * @param table
   * @param columnName
   * @param id
   * @return value of the clob.
   */
  public static String getClob(String table, String columnName, String id)
  {
    return instance().getClob(table, columnName, id);
  }

  /**
   * Sets the value of this blob as the specified bytes.
   * 
   * @param table
   * @param columnName
   * @param id
   * @param string
   * @return The number of bytes written.
   */
  public static void setClob(String table, String columnName, String id, String string)
  {
    instance().setClob(table, columnName, id, string);
  }

  /**
   * Returns the value of a blob as a byte array.
   * 
   * @param table
   * @param columnName
   * @param id
   * @return byte[] value of the blob.
   */
  public static byte[] getBlobAsBytes(String table, String columnName, String id)
  {
    return instance().getBlobAsBytes(table, columnName, id);
  }

  /**
   * Returns the value of a blob as a byte array. This method allows you to
   * specify a start position in the blob (where the first element starts at
   * position 1 to comply with the JDBC 3.0 API) and the total length
   * (inclusive) beyond the start position to return.
   * 
   * @param table
   * @param columnName
   * @param id
   * @param pos
   * @param length
   * @return
   */
  public static byte[] getBlobAsBytes(String table, String columnName, String id, long pos, int length)
  {
    return instance().getBlobAsBytes(table, columnName, id, pos, length);
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
   * @param id
   * @param pos
   * @param bytes
   * @param offset
   * @param length
   * @return
   */
  public static int setBlobAsBytes(String table, String columnName, String id, long pos, byte[] bytes, int offset, int length)
  {
    return instance().setBlobAsBytes(table, columnName, id, pos, bytes, offset, length);
  }

  /**
   * Sets the value of this blob as the specified bytes.
   * 
   * @param table
   * @param columnName
   * @param id
   * @param bytes
   * @return The number of bytes written.
   */
  public static int setBlobAsBytes(String table, String columnName, String id, byte[] bytes)
  {
    // clear any previous value
    Database.truncateBlob(table, columnName, id, 0);

    return instance().setBlobAsBytes(table, columnName, id, bytes);
  }

  /**
   * Returns the blob as an array of bytes.
   * 
   * @param table
   * @param columnName
   * @param id
   * @return The byte array value of this blob attribute.
   */
  public static long getBlobSize(String table, String columnName, String id)
  {
    return instance().getBlobSize(table, columnName, id);
  }

  /**
   * Gets the value of this blob as the specified bytes. This method works the
   * same as the Blob.getBytes(long pos, int length) as specified in the JDBC
   * 3.0 API. Because of this, the first element in the bytes to write to is
   * actually element 1 (as opposed to the standard array treatment where the
   * first element is at position 0).
   * 
   * @param table
   * @param columnName
   * @param id
   * @param pos
   *          The starting position. The first element is at position 1.
   * @param length
   *          The length in bytes to grab after (and including) the starting
   *          position.
   * @return byte[]
   */
  public static void truncateBlob(String table, String columnName, String id, long length)
  {
    Connection conn = Database.getConnection();
    Database.truncateBlob(table, columnName, id, length, conn);
    try
    {
      instance().closeConnection(conn);
    }
    catch (SQLException e)
    {
      instance().throwDatabaseException(e);
    }
  }

  /**
   * Gets the value of this blob as the specified bytes. This method is used
   * only within the TransactionManagement aspect, hence it takes a JDBC
   * Connection object as a parameter. This method works the same as the
   * Blob.getBytes(long pos, int length) as specified in the JDBC 3.0 API.
   * Because of this, the first element in the bytes to write to is actually
   * element 1 (as opposed to the standard array treatment where the first
   * element is at position 0).
   * 
   * @param table
   * @param columnName
   * @param id
   * @param pos
   *          The starting position. The first element is at position 1.
   * @param length
   *          The length in bytes to grab after (and including) the starting
   *          position.
   * @return byte[]
   */
  public static void truncateBlob(String table, String columnName, String id, long length, Connection con)
  {
    // if the truncate size is greater than or equal to the current size, do
    // nothing.
    if (length >= instance().getBlobSize(table, columnName, id))
    {
      return;
    }
    else
    {
      instance().truncateBlob(table, columnName, id, length, con);
    }
  }

  /**
   * Builds a JDBC prepared <code>UPDATE</code> statement for the given columns. <br>
   * 
   * @param table
   *          The table to insert into.
   * @param columnNames
   *          The names of the columns being inserted.
   * @param prepStmtVars
   *          usually just a "?", but some types require special functions.
   * @param values
   *          The values of the columns to update.
   * @param attributeTypes
   *          The core datatypes of the columns to update.
   * @param id
   *          id of the object to update.
   * 
   * @return <code>UPDATE</code> PreparedStatement
   */
  public static PreparedStatement buildPreparedSQLUpdateStatement(String table, List<String> columnNames, List<String> prepStmtVars, List<Object> values, List<String> attributeTypes, String id)
  {
    return instance().buildPreparedSQLUpdateStatement(table, columnNames, prepStmtVars, values, attributeTypes, id);
  }

  /**
   * Builds a JDBC prepared <code>UPDATE</code> statement for the given fields. <br>
   * 
   * @param table
   *          The table to insert into.
   * @param entityId
   *          entity ID
   * @param columnName
   *          The name of the field being updated.
   * @param prepStmtVar
   *          usually just a "?", but some types require special functions.
   * @param oldValue
   *          The original value
   * @param newValue
   *          The value of the field to update.
   * @param attributeType
   *          The core datatype of the field to update
   * 
   * @return <code>UPDATE</code> PreparedStatement
   */
  public static PreparedStatement buildPreparedUpdateFieldStatement(String table, String entityId, String columnName, String prepStmtVar, Object oldValue, Object newValue, String attributeType)
  {
    return instance().buildPreparedUpdateFieldStatement(table, entityId, columnName, prepStmtVar, oldValue, newValue, attributeType);
  }

  /**
   * Builds a JDBC prepared <code>UPDATE</code> statement for the given columns. <br>
   * 
   * @param table
   *          The table to insert into.
   * @param columnNames
   *          The names of the columns being inserted.
   * @param prepStmtVars
   *          usually just a "?", but some types require special functions.
   * @param values
   *          The values of the columns to update.
   * @param attributeTypes
   *          The core datatypes of the columns to update.
   * @param id
   *          id of the object to update.
   * @param seq
   *          sequence of the object to update.
   * 
   * @return <code>UPDATE</code> PreparedStatement
   */
  public static PreparedStatement buildPreparedSQLUpdateStatement(String table, List<String> columnNames, List<String> prepStmtVars, List<Object> values, List<String> attributeTypes, String id, long seq)
  {
    return instance().buildPreparedSQLUpdateStatement(table, columnNames, prepStmtVars, values, attributeTypes, id, seq);
  }

  /**
   * Builds a String representation of an SQL SELECT statement. Allows for
   * multiple columns, tables, and conditions. See
   * {@link #select(List, List, List)}for more information.
   * 
   * @param columnNames
   *          List of the columns being selected.
   * @param tables
   *          List of the tables being joined.
   * @param conditions
   *          List of conditions that must be satisfied to be included in the
   *          result set.
   * @return The SQL query String representing the parameters.
   */
  public static String selectClause(List<String> columnNames, List<String> tables, List<String> conditions)
  {
    return instance().selectClause(columnNames, tables, conditions);
  }

  /**
   * Executes a SQL query (which is assumed to be valid) against the database,
   * returning a RowSet object the result. It is up to the client to close the
   * resultset, the statement, and the connection. <br>
   * 
   * @param columnNames
   *          List of the columns being selected.
   * @param tables
   *          List of the tables being joined.
   * @param conditions
   *          List of conditions that must be satisfied to be included in the
   *          result set.
   * @return The SQL query String representing the parameters.
   */
  public static ResultSet select(List<String> columnNames, List<String> tables, List<String> conditions)
  {
    return instance().select(columnNames, tables, conditions);
  }

  /**
   * Executes a SQL query (which is assumed to be valid) against the database,
   * returning a RowSet object the result. It is up to the client to close the
   * resultset, the statement, and the connection. <br>
   * <b>Precondition: </b> statement != null <br>
   * <b>Precondition: </b> sqlStmt != null <br>
   * <b>Precondition: </b> !sqlStmt.trim().equals("") <br>
   * <b>Postcondition: </b> return value may not be null
   * 
   * @param sqlStmt
   *          SQL query statement
   */
  public static ResultSet query(String sqlStmt)
  {
    return instance().query(sqlStmt);
  }

  /**
   * Returns fields that are needed by <code>MdAttributeDimensionDAOIF</code>
   * objects. If the given parameter is null, then all objects are returned.
   * Otherwise, it returns fields just for object associated with the given
   * <code>MdAttributeDAOIF</code> id.
   * 
   * @return ResultSet contains fields that are needed by
   *         <code>MdAttributeDimensionDAOIF</code> objects. If the given
   *         parameter is null, then all objects are returned. Otherwise, it
   *         returns fields just for object associated with the given
   *         <code>MdAttributeDAOIF</code> id.
   */
  public static ResultSet getMdAttributeDimensionFields(String mdAttributeId)
  {
    return instance().getMdAttributeDimensionFields(mdAttributeId);
  }

  /**
   * Returns ids for <code>MdAttributeDimensionDAOIF</code>s. If the given id is
   * null, then all objects are returned. Otherwise, the
   * <code>MdAttributeDimensionDAOIF</code>s for the
   * <code>MdDimensionDAOIF</code> with the given id.
   * 
   * @param mdDimensionId
   * @return ids for <code>MdAttributeDimensionDAOIF</code>s. If the given id is
   *         null, then all objects are returned. Otherwise, the
   *         <code>MdAttributeDimensionDAOIF</code>s for the
   *         <code>MdDimensionDAOIF</code> with the given id.
   */
  public static ResultSet getMdAttributeDimensionIds(String mdDimensionId)
  {
    return instance().getMdAttributeDimensionIds(mdDimensionId);
  }

  /**
   * Gets the next sequence number from the database. Concrete implementations
   * should be <code><b>synchronized</b></code>.
   * 
   * @return The next sequence number from the database.
   */
  public static String getNextSequenceNumber()
  {
    return instance().getNextSequenceNumber();
  }

  /**
   * Gets the next sequence number from the database. Concrete implementations
   * should be <code><b>synchronized</b></code>.
   * 
   * @return The next sequence number from the database.
   */
  public static String getNextTransactionSequence()
  {
    return instance().getNextTransactionSequence();
  }

  /**
   * Creates a new table in the database for a class. Automatically adds the
   * Component.ID column as the primary key.
   * 
   * @param table
   *          The name of the new table.
   */
  public static void createClassTable(String table)
  {
    instance().createClassTable(table);
  }

  /**
   * Creates a new table in the database for a class, including all columns for
   * that table or.
   * 
   * @param tableName
   *          table name
   * @param columnDefs
   *          columnDefs column definitions.
   */
  public static void createClassTableBatch(String tableName, List<String> columnDefs)
  {
    instance().createClassTableBatch(tableName, columnDefs);
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
  public static void alterClassTableBatch(String tableName, List<String> columnNames, List<String> columnDefs)
  {
    instance().alterClassTableBatch(tableName, columnNames, columnDefs);
  }

  /**
   * @return <code>true</code> if the database allows nonrequired columns to
   *         enforce uniqueness
   */
  public static boolean allowsUniqueNonRequiredColumns()
  {
    return instance().allowsUniqueNonRequiredColumns();
  }

  /**
   * Creates the dynamic properties table and sets the default values for the
   * dynamic properties.
   */
  public static void setupPropertiesTable()
  {
    // Create the properties table
    instance().buildDynamicPropertiesTable();

    // // Populate the properties table
    // List<String> fields = new LinkedList<String>();
    // fields.add(EntityInfo.ID);
    // fields.add(Database.VERSION_NUMBER);
    //
    // List<String> prepStmtVars = new LinkedList<String>();
    // prepStmtVars.add("?");
    // prepStmtVars.add("?");
    //
    // List<Object> values = new LinkedList<Object>();
    // values.add(Database.VERSION_TIMESTAMP_PROPERTY);
    // values.add(Database.INITIAL_VERSION);
    //
    // List<String> attributeTypes = new LinkedList<String>();
    // attributeTypes.add(MdAttributeCharacterInfo.CLASS);
    // attributeTypes.add(MdAttributeCharacterInfo.CLASS);
    //
    // List<PreparedStatement> statements = new LinkedList<PreparedStatement>();
    // statements.add(Database.buildPreparedSQLInsertStatement(Database.PROPERTIES_TABLE,
    // fields, prepStmtVars, values, attributeTypes));
    //
    // Database.executeStatementBatch(statements);
  }

  /**
   * Set a value in the dynamic properties table.
   * 
   * @param columnName
   *          Name of the column to set
   * @param attributeType
   *          Type of the column being set
   * @param value
   *          The value to set to the value
   */
  public static void setPropertyValue(String columnName, String attributeType, String value)
  {
    List<String> columns = new LinkedList<String>();
    columns.add(columnName);

    List<String> prepStmtVars = new LinkedList<String>();
    prepStmtVars.add("?");

    List<Object> values = new LinkedList<Object>();
    values.add(value);

    List<String> attributeTypes = new LinkedList<String>();
    attributeTypes.add(attributeType);

    List<PreparedStatement> statements = new LinkedList<PreparedStatement>();
    statements.add(Database.buildPreparedSQLUpdateStatement(Database.PROPERTIES_TABLE, columns, prepStmtVars, values, attributeTypes, Database.VERSION_TIMESTAMP_PROPERTY));

    Database.executeStatementBatch(statements);
  }

  public static void addPropertyValue(String columnName, String attributeType, String value, String propertyId)
  {
    List<String> columnNames = new LinkedList<String>();
    columnNames.add(columnName);
    columnNames.add(Database.PROPERTIES_ID_COLUMN);

    List<String> prepStmtVars = new LinkedList<String>();
    prepStmtVars.add("?");
    prepStmtVars.add("?");

    List<Object> values = new LinkedList<Object>();
    values.add(value);
    values.add(propertyId);

    List<String> attributeTypes = new LinkedList<String>();
    attributeTypes.add(attributeType);
    attributeTypes.add(MdAttributeCharacterInfo.CLASS);

    List<PreparedStatement> statements = new LinkedList<PreparedStatement>();
    statements.add(Database.buildPreparedSQLInsertStatement(Database.PROPERTIES_TABLE, columnNames, prepStmtVars, values, attributeTypes));

    Database.executeStatementBatch(statements);
  }

  public static void removePropertyValue(String columnName, String attributeType, String value, String propertyId)
  {
    List<String> statements = new LinkedList<String>();
    List<String> conditions = new LinkedList<String>();

    conditions.add(columnName + " = '" + value + "'");
    conditions.add(Database.PROPERTIES_ID_COLUMN + " = '" + propertyId + "'");

    statements.add(Database.buildSQLDeleteWhere(Database.PROPERTIES_TABLE, conditions));

    Database.executeBatch(statements);
  }

  public static List<String> getPropertyValue(String propertyId)
  {
    List<String> results = new LinkedList<String>();

    String condition = EntityDAOIF.ID_COLUMN + " = " + Database.formatJavaToSQL(propertyId, MdAttributeCharacterInfo.CLASS, false);

    ResultSet resultSet = Database.selectFromWhere(Database.VERSION_NUMBER, Database.PROPERTIES_TABLE, condition);

    try
    {
      while (resultSet.next())
      {
        results.add(resultSet.getString(Database.VERSION_NUMBER).trim());
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

    return results;
  }

  /**
   * Returns a Map where the key is a parent of the given type and the value is
   * the database value..
   * 
   * <br/>
   * <b>Precondition:</b> mdEntityId != null <br/>
   * <b>Precondition:</b> !mdEntityId().equals("")
   * 
   * @param type
   * @return Map where the key is a parent of the given type and the value is
   *         the database value..
   */
  public static LinkedHashMap<String, String> getSuperEntityTypes(String type)
  {
    return instance().getSuperEntityTypes(type);
  }

  /**
   * Returns the id to the MdEntity that defines the given type. given ID.
   * 
   * @param type
   * @return id to the MdEntity that defines the given type.
   */
  public static String getMdEntityId(String type)
  {
    return instance().getMdEntityId(type);
  }

  /**
   * Returns a List of Strings representing the names of subclasses of the
   * entity defined by the object with the given it, but only those classes that
   * are not abstract.
   * 
   * <br/>
   * <b>Precondition:</b> mdEntityId != null <br/>
   * <b>Precondition:</b> !mdEntityId().equals("")
   * 
   * @param mdEntityId
   * @return List of Strings representing the names of subclasses of the entity
   *         defined by the object with the given it, but only those classes
   *         that are not abstract.
   */
  public static List<String> getConcreteSubClasses(String mdEntityId)
  {
    return instance().getConcreteSubClasses(mdEntityId);
  }

  /**
   * Returns a List of Strings for all IDs of EntiyDAOs of the given type or
   * that are sub entities. Gets the table name for the entity by queriying the
   * database.
   * 
   * <br/>
   * <b>Precondition: </b> type != null <br/>
   * <b>Precondition: </b> !type().equals("")
   * 
   * @param type
   * @return List of Strings for all IDs of EntiyObjects of the given type or
   *         that are sub entities. Gets the table name for the entity by
   *         querying the database.
   */
  public static List<String> getEntityIds(String type)
  {
    return instance().getEntityIds(type);
  }

  /**
   * Checks if the given class name is a valid.
   * 
   * <br/>
   * <b>Precondition:</b> type != null <br/>
   * <b>Precondition:</b> !type.trim().equals("")
   * 
   * @param type
   * @return True if given type is valid, false otherwise.
   */
  public static boolean isValidType(String type)
  {
    return instance().isValidType(type);
  }

  /**
   * Returns a Map of Attribute objects for the EnityObject with the given ID
   * and class name. It only returns attributes that are explicitly defined by
   * the given class name.
   * 
   * <br/>
   * <b>Precondition:</b> type != null <br/>
   * <b>Precondition:</b> !type.trim().equals("") <br/>
   * <b>Precondition:</b> id != null <br/>
   * <b>Precondition:</b> !id.trim().equals("")
   * 
   * @param id
   * @param type
   * @param tableName
   * @param relationshipAttributesHackMap
   *          this is a total hack. If the instance is a relationship, then
   *          return the parent_id and child_id values in this map.
   * @return Map of Attribute objects for the EnityObject with the given ID and
   *         class.
   */
  public static Map<String, Attribute> getAttributesForHardcodedMetadataObject(String id, String type, String tableName, Map<String, String> relationshipAttributesHackMap, boolean rootClass)
  {
    return instance().getAttributesForHardcodedMetadataObject(id, type, tableName, relationshipAttributesHackMap, rootClass);
  }

  /**
   * Returns a Map of a Map of Attribute objects for the given type. It only
   * returns attributes that are explicitly defined by the given type.
   * 
   * <br/>
   * <b>Precondition:</b> type != null <br/>
   * <b>Precondition:</b> !type.trim().equals("") <br/>
   * <b>Precondition:</b> id != null <br/>
   * <b>Precondition:</b> !id.trim().equals("")
   * 
   * @param cacheTypeTable
   * @param type
   * @param tableName
   * @param relationshipAttributesHackMap
   *          this is a total hack. If the instance is a relationship, then
   *          return the parent_id and child_id values in this map.
   * @return Map of Attribute objects for the EnityObject with the given ID and
   *         class.
   */
  public static HardCodedMetadataIterator getAttributesForHardcodedMetadataType(String cacheTypeTable, String type, String tableName, Map<String, Map<String, String>> relationshipAttributesHackMap, boolean rootClass)
  {
    return instance().getAttributesForHardcodedMetadataType(cacheTypeTable, type, tableName, relationshipAttributesHackMap, rootClass);
  }

  /**
   * Returns the type of the object with the given id.
   * 
   * @param instanceId
   * @return the type of the object with the given id.
   */
  public static String getTypeFromInstanceId(String instanceId)
  {
    return instance().getTypeFromInstanceId(instanceId);
  }

  /**
   * Creates a new table in the database for a relationships. Automatically adds
   * the Component.ID column as the primary key.
   * 
   * @param tableName
   *          The name of the new table.
   * @param index1Name
   *          The name of the 1st index used by the given table.
   * @param index2Name
   *          The name of the 1st index used by the given table.
   * @param isUnique
   *          Indicates whether the parent_id child_id pair should be made
   *          unique. This should only be done on concrete relationship types.
   */
  public static void createRelationshipTable(String table, String index1Name, String index2Name, Boolean isUnique)
  {
    instance().createRelationshipTable(table, index1Name, index2Name, isUnique);
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
  public static void createRelationshipTableBatch(String tableName, List<String> columnDefs)
  {
    instance().createRelationshipTableBatch(tableName, columnDefs);
  }

  /**
   * Returns the SQL string for a new table in the database for a relationship,
   * minus the closing parenthesis. Automatically adds the Component.ID column
   * as the primary key.
   * 
   * @param tableName
   *          The name of the new table.
   */
  public static String startCreateRelationshipTableBatch(String tableName)
  {
    return instance().startCreateRelationshipTableBatch(tableName);
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
   *          Indicates whether the parent_id child_id pair should be made
   *          unique. This should only be done on concrete relationship types.
   */
  public static void createRelationshipTableIndexesBatch(String tableName, String index1Name, String index2Name, boolean isUnique)
  {
    instance().createRelationshipTableIndexesBatch(tableName, index1Name, index2Name, isUnique);
  }

  /**
   * @see com.runwaysdk.dataaccess.database.Database#createEnumerationTable(String,
   *      String);
   */
  public static void createEnumerationTable(String tableName, String id)
  {
    instance().createEnumerationTable(tableName, id);
  }

  /**
   * Drops an entire table from the database for a class. An undo command is
   * created that will recreate the table if transaction management requires a
   * rollback. However, the undo will <b>not </b> recreate all of the columns in
   * the table, only the Component.ID.
   * 
   * @param table
   *          The name of the table to drop.
   */
  public static void dropClassTable(String table)
  {
    instance().dropClassTable(table);
  }

  /**
   * Deletes all records in the given table.
   * 
   * @param tableName
   *          The name of the table to remove all records from.
   */
  public static void deleteAllTableRecords(String tableName)
  {
    instance().deleteAllTableRecords(tableName);
  }

  /**
   * Drops an entire table from the database for a relationship. An undo command
   * is created that will recreate the table if transaction management requires
   * a rollback. However, the undo will <b>not </b> recreate all of the columns
   * in the table, only the Component.ID.
   * 
   * @param table
   *          The name of the table to drop.
   * @param index1Name
   *          The name of the 1st index used by the given table.
   * @param index2Name
   *          The name of the 1st index used by the given table.
   * @param isUnique
   *          Indicates whether the parent_id child_id pair should be made
   *          unique. This should only be done on concrete relationship types.
   */
  public static void dropRelationshipTable(String table, String index1Name, String index2Name, boolean isUnique)
  {
    instance().dropRelationshipTable(table, index1Name, index2Name, isUnique);
  }

  /**
   * Deletes an enumeration value mapping table in the database.
   * 
   * @param tableName
   * @param id
   */
  public static void dropEnumerationTable(String tableName, String id)
  {
    instance().dropEnumerationTable(tableName, id);
  }

  /**
   * Adds a floating-point column to a table in the database with an alter table
   * statement. Creates an undo DROP command in case transaction management
   * requires a rollback.
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
  public static void addDecField(String table, String columnName, String type, String length, String decimal)
  {
    instance().addDecField(table, columnName, type, length, decimal);
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
  public static String addDecFieldBatch(String table, String columnName, String type, String length, String decimal)
  {
    return instance().addDecFieldBatch(table, columnName, type, length, decimal);
  }

  /**
   * Adds a column to a table in the database with an alter table statement.
   * Creates an undo DROP command in case transaction management requires a
   * rollback.
   * 
   * @param table
   *          The table that the column is being added to.
   * @param columnName
   *          The name of the new column.
   * @param formattedType
   *          DDL column type definition formatted to the syntax of the DB
   *          vendor.
   * @param mdAttributeConcreteDAO
   *          metadata that defines the column
   */
  public static void addField(String table, String columnName, String formattedType, MdAttributeConcreteDAO mdAttributeConcreteDAO)
  {
    instance().addField(table, columnName, formattedType, mdAttributeConcreteDAO);
  }

  /**
   * Returns SQL to add a column to a table in the database.
   * 
   * @param table
   *          The table that the column is being added to.
   * @param columnName
   *          The name of the new column.
   * @param formattedType
   *          DDL column type definition formatted to the syntax of the DB
   *          vendor.
   * @param size
   *          The size of new column. <code><b>null</b></code> if the type does
   *          not require a size parameter.
   * @param mdAttributeConcreteDAO
   *          metadata that defines the column
   */
  public static String addFieldBatch(String table, String columnName, String formattedType, MdAttributeConcreteDAO mdAttributeConcreteDAO)
  {
    return instance().addFieldBatch(table, columnName, formattedType, mdAttributeConcreteDAO);
  }

  /**
   * 
   * 
   * @param table
   * @param columnName
   * @return
   */
  public static String attributeIndexName(String table, String columnName)
  {
    return instance().attributeIndexName(table, columnName);
  }

  /**
   * 
   * @param table
   * @param columnName
   * @param indexName
   */
  public static void addUniqueIndex(String table, String columnName, String indexName)
  {
    instance().addUniqueIndex(table, columnName, indexName);
  }

  /**
   * Adds a non unique index to the specified table as the given column.
   * 
   * @param table
   * @param columnName
   * @param indexName
   */
  public static void addNonUniqueIndex(String table, String columnName, String indexName)
  {
    instance().addNonUniqueIndex(table, columnName, indexName);
  }

  /**
   * Drops a non unique index on the specified table with the given column.
   * 
   * @param table
   * @param columnName
   * @param indexName
   * @param delete
   */
  public static void dropNonUniqueIndex(String table, String columnName, String indexName, boolean delete)
  {
    instance().dropNonUniqueIndex(table, columnName, indexName, delete);
  }

  /**
   * 
   * @param table
   * @param columnName
   * @param indexName
   * @param delete
   */
  public static void dropUniqueIndex(String table, String columnName, String indexName, boolean delete)
  {
    instance().dropUniqueIndex(table, columnName, indexName, delete);
  }

  /**
   * Returns true if the given index exists for the given attribute on the given
   * table.
   * 
   * @param table
   * @param columnName
   * @param indexName
   */
  public static boolean uniqueAttributeExists(String table, String columnName, String indexName)
  {
    return instance().uniqueAttributeExists(table, columnName, indexName);
  }

  /**
   * Returns true if the non unique index exists for the given attribute on the
   * given table.
   * 
   * @param table
   * @param columnName
   * @param indexName
   */
  public static boolean nonUniqueAttributeExists(String table, String columnName, String indexName)
  {
    return instance().nonUniqueAttributeExists(table, columnName, indexName);
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
  public static void addGroupAttributeIndex(String table, String indexName, List<String> columnNames, Boolean isUnique)
  {
    instance().addGroupAttributeIndex(table, indexName, columnNames, isUnique);
  }

  /**
   * Drops the index with the given name. The attributes and unique flag are
   * used to rebuild the index in the case of a rolledback transaction.
   * 
   * @param table
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
  public static void dropGroupAttributeIndex(String table, String indexName, List<String> columnNames, boolean isUnique, boolean delete)
  {
    instance().dropGroupAttributeIndex(table, indexName, columnNames, isUnique, delete);
  }

  /**
   * Returns true if the given index exists on the given table, false otherwise.
   * 
   * @param table
   * @param indexName
   * @return true if the given index exists on the given table, false otherwise.
   */
  public static boolean indexExists(String table, String indexName)
  {
    return instance().indexExists(table, indexName);
  }

  /**
   * Returns true if indexes need to be rebuilt if a column is modified, false
   * otherwise. Some databases don't like it when you alter a column that has an
   * index on it.
   * 
   * @return true if indexes need to be rebuilt if a column is modified, false
   *         otherwise.
   */
  public static boolean rebuildIndexOnModifyColumn()
  {
    return instance().rebuildIndexOnModifyColumn();
  }

  /**
   * Returns true if a group attribute index exists with the given name and the
   * given attributes on the given table.
   * 
   * @param tableName
   * @param indexName
   * @param columnNames
   */
  public static boolean groupAttributeIndexExists(String tableName, String indexName, List<String> columnNames)
  {
    return instance().groupAttributeIndexExists(tableName, indexName, columnNames);
  }

  /**
   * Returns true if a group attribute index exists with the given name on the
   * given table.
   * 
   * @param tableName
   * @param indexName
   */
  public static boolean groupAttributeIndexExists(String tableName, String indexName)
  {
    return instance().groupAttributeIndexExists(tableName, indexName);
  }

  /**
   * Returns a list of string names of the attributes that participate in a
   * group index for the given table with the index of the given name.
   * 
   * @param table
   * @param indexName
   */
  public static List<String> getGroupIndexAttributes(String table, String indexName)
  {
    return instance().getGroupIndexAttributes(table, indexName);
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
  public static boolean manuallyCheckForDuplicates()
  {
    return instance().manuallyCheckForDuplicates();
  }

  /**
   * Changes the size of a column in the database. Creates a backup of the
   * original column parameters in case transaction management requires a
   * rollback.
   * 
   * @param table
   *          The table containing the CHAR column.
   * @param columnName
   *          The CHAR column being modified.
   * @param newDbColumnType
   *          the new database column type formatted to the database vendor
   *          syntax.
   * @param oldDbColumnType
   *          the current database column type formatted to the database vendor
   *          syntax.
   */
  public static void alterFieldType(String table, String columnName, String newDbColumnType, String oldDbColumnType)
  {
    instance().alterFieldType(table, columnName, newDbColumnType, oldDbColumnType);
  }

  /**
   * Drop a column from a table.
   * 
   * @param table
   *          The table containing the column to drop.
   * @param columnName
   *          The column being dropped.
   * @param dbColumnType
   *          the database column type formatted to the database vendor syntax.
   * @param mdAttributeConcreteDAO
   *          metadata that defines the column.
   */
  public static void dropField(String table, String columnName, String dbColumnType, MdAttributeConcreteDAO mdAttributeConcreteDAO)
  {
    instance().dropField(table, columnName, dbColumnType, mdAttributeConcreteDAO);
  }

  /**
   * Adds temporary fields for the given column on the given table.
   * 
   * @param tableName
   * @param columnName
   * @param columnType
   * @param numberOfTempFields
   */
  public static void addTempFieldsToTable(String tableName, String columnName, String columnType, Integer numberOfTempFields)
  {
    instance().addTempFieldsToTable(tableName, columnName, columnType, numberOfTempFields);
  }

  /**
   * Adds a column to a table in the database with an alter table statement.
   * Creates an undo DROP command in case transaction management requires a
   * rollback.
   * 
   * @param table
   *          The table that the column is being added to.
   * @param columnName
   *          The name of the new column.
   * @param type
   *          The database type of the new column.
   * @param size
   *          The size of new column. <code><b>null</b></code> if the type does
   *          not require a size parameter.
   */
  public static void addField(String table, String columnName, String type, String size)
  {
    instance().addField(table, columnName, type, size);
  }

  /**
   * Returns a string that adds a column to the given table.
   * 
   * @param table
   * @param columnName
   * @param formattedColumnTree
   * 
   * @return string that adds a column to the given table.
   */
  public static String buildAddColumnString(String table, String columnName, String formattedColumnTree)
  {
    return instance().buildAddColumnString(table, columnName, formattedColumnTree);
  }

  /**
   * Returns a string that drops a column from the given table.
   * 
   * @param table
   * @param columnName
   * @return string that drops a column from the given table.
   */
  public static String buildDropColumnString(String table, String columnName)
  {
    return instance().buildDropColumnString(table, columnName);
  }

  /**
   * Returns SQL to add a column to a table in the database.
   * 
   * @param table
   *          The table that the column is being added to.
   * @param columnName
   *          The name of the new column.
   * @param type
   *          The database type of the new column.
   * @param size
   *          The size of new column. <code><b>null</b></code> if the type does
   *          not require a size parameter.
   */
  public static String addFieldBatch(String table, String columnName, String type, String size)
  {
    return instance().addFieldBatch(table, columnName, type, size);
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
  public static boolean tableExists(String tableName)
  {
    return instance().tableExists(tableName);
  }

  /**
   * Returns a savepoint.
   * 
   * @return a savepoint.
   */
  public static Savepoint setSavepoint()
  {
    return instance().setSavepoint();
  }

  /**
   * Returns the most recent savepoint, but does not pop it from the stack.
   * 
   * @return most recent savepoint, but does not pop it from the stack.
   * @throws {@link EmptyStackException} if there is no savepoint on the request
   *         stack.
   */
  public static Savepoint peekCurrentSavepoint()
  {
    // Aspects will return the savepoint.
    return null;
  }

  /**
   * Returns the most recent savepoint and pops it it from the stack.
   * 
   * @return most recent savepoint and pops it it from the stack.
   * @throws {@link EmptyStackException} if there is no savepoint on the request
   *         stack.
   */
  public static Savepoint popCurrentSavepoint()
  {
    // Aspects will return the savepoint.
    return null;
  }

  /**
   * Rollsback the savepoint.z
   * 
   * @param savepoint
   *          to rollback.
   */
  public static void rollbackSavepoint(Savepoint savepoint)
  {
    instance().rollbackSavepoint(savepoint);
  }

  /**
   * Returns a DDL connection savepoint.
   * 
   * @return a DDL connection savepoint.
   */
  public static Savepoint setDDLsavepoint()
  {
    return instance().setDDLsavepoint();
  }

  /**
   * Rolls back the DDL connection savepoint.
   * 
   * @param DDL
   *          connection savepoint to rollback.
   */
  public static void rollbackDDLsavepoint(Savepoint savepoint)
  {
    instance().rollbackDDLsavepoint(savepoint);
  }

  /**
   * Releases the savepoint.
   * 
   * @param savepoint
   *          to release.
   */
  public static void releaseDDLsavepoint(Savepoint savepoint)
  {
    instance().releaseDDLsavepoint(savepoint);
  }

  /**
   * Releases the savepoint.
   * 
   * @param savepoint
   *          to release.
   */
  public static void releaseSavepoint(Savepoint savepoint)
  {
    instance().releaseSavepoint(savepoint);
  }

  /**
   * Returns a java.sql.Connection object for the database.
   * 
   * <br/>
   * <b>Precondition:</b> database is running. <br/>
   * <b>Precondition:</b> database.properities file contains correct DB
   * connection settings. <br/>
   * <b>Postcondition:</b> true
   * 
   * @return java.sql.Connection object
   */
  public static Connection getConnection()
  {
    return instance().getConnection();
  }

  /**
   * All connections managed by the framework need to be closed using this
   * method. This is a hook method for the sesion management aspect.
   * 
   * @param conn
   * @throws SQLException
   */
  public static void closeConnection(Connection conn) throws SQLException
  {
    instance().closeConnection(conn);
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
  public static Connection getDDLConnection()
  {
    return instance().getDDLConnection();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.AbstractDatabase#sharesDDLandDMLconnection()
   */
  public static boolean sharesDDLandDMLconnection()
  {
    return instance().sharesDDLandDMLconnection();
  }

  /**
   * Returns true if the current request has already established a DDL
   * connection, false otherwise.
   * 
   * <br/>
   * <b>Precondition:</b> database is running. <br/>
   * <b>Precondition:</b> database.properities file contains correct DB
   * connection settings.
   * 
   * @return boolean true if the current request has already established a DDL
   *         connection, false otherwise.
   */
  public static boolean requestAlreadyHasDDLConnection()
  {
    // Aspects will weave in the correct behavior.
    return false;
  }

  /**
   * Returns a List containing the columns in a table.
   * 
   * @param table
   *          The table to get the column list from.
   * @return The List of the columns in the table.
   */
  public static List<String> getColumnNames(String table)
  {
    return instance().getColumnNames(table);
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
  public static boolean columnExists(String columnName, String tableName)
  {
    return instance().columnExists(columnName, tableName);
  }

  /**
   * {@link #execute(String)}s an SQL statement.
   * 
   * @param statement
   *          The SQL statement being parsed and executed.
   * @see #execute(String)
   */
  public static void parseAndExecute(String statement)
  {
    instance().parseAndExecute(statement);
  }

  /**
   * Hard-coded database commands that create the database sequence used to help
   * create unique ids.
   */
  public static void createObjectSequence()
  {
    instance().createObjectSequence();
  }

  /**
   * Hard-coded database commands that create the database sequence used to help
   * create unique ids for transactions.
   */
  public static void createTransactionSequence()
  {
    instance().createTransactionSequence();
  }

  /**
   * Resets the transaction sequence. This should ONLY be called for Runway
   * development testing purposes.
   */
  public static void resetTransactionSequence()
  {
    instance().resetTransactionSequence();
  }

  /**
   * Different databases format column aliases differently in the column clause
   * of a select statement. Returns the given String column alias formatted to
   * the syntax of the database vendor.
   * 
   * @return given String column alias formatted to the syntax of the database
   *         vendor.
   */
  public static String formatColumnAlias(String columnAlias)
  {
    return instance().formatColumnAlias(columnAlias);
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
  public static String formatColumnAlias(String columnAlias, String dataType)
  {
    return instance().formatColumnAlias(columnAlias, dataType);
  }

  /**
   * Formats the column for a comparison in a select statement.
   * 
   * @param formatted
   *          column name.
   * @return column datatype.
   */
  public static String formatColumnForCompare(String qualifiedColumnName, String dataType)
  {
    return instance().formatColumnForCompare(qualifiedColumnName, dataType);
  }

  /**
   * Formats a column for a select statement.
   * 
   * @param qualifiedColumnName
   * @param mdAttribute
   *          MdAttribute that defines the attribute that uses the given column.
   * @return
   */
  public static String formatSelectClauseColumn(String qualifiedColumnName, MdAttributeConcreteDAOIF mdAttribute)
  {
    return instance().formatSelectClauseColumn(qualifiedColumnName, mdAttribute);
  }

  /**
   * Returns the character type formatted for a DDL command to the vendor
   * syntax.
   * 
   * @param type
   * 
   * @return
   */
  public static String formatCharacterField(String type, String length)
  {
    return instance().formatCharacterField(type, length);
  }

  /**
   * Returns the text type formatted for a DDL command to the vendor syntax.
   * 
   * @param type
   * 
   * @return
   */
  public static String formatTextField(String type)
  {
    return instance().formatTextField(type);
  }

  /**
   * Returns the CLOB type formatted for a DDL command to the vendor syntax.
   * 
   * @param type
   * 
   * @return
   */
  public static String formatClobField(String type)
  {
    return instance().formatClobField(type);
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
   *          if true, the value is converted to UPPERCASE
   */
  public static String formatJavaToSQL(String value, String dataType, boolean ignoreCase)
  {
    return instance().formatJavaToSQL(value, dataType, ignoreCase);
  }

  /**
   * Converts the given String value and formats it to a String that can be used
   * in a SQL Query. <br>
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
   *          if true, the value is converted to UPPERCASE
   */
  public static String formatJavaToSQLForQuery(String value, String dataType, boolean ignoreCase)
  {
    return instance().formatJavaToSQLForQuery(value, dataType, ignoreCase);
  }

  /**
   * Sets a binding on the prepared statement object with the given value at the
   * given index. Uses the dataType parameter, which represents a core attribute
   * value, to determine which setter method to call on the prepared statement
   * object.
   * 
   * <br>
   * <b>Precondition: </b> prepStmt != null <br>
   * <b>Precondition: </b> index represents a valid index binding for the given
   * prepared statement. <br>
   * <b>Precondition: </b> value != null <br>
   * <b>Precondition: </b> dataType != null <br>
   * <b>Precondition: </b> !dataType.trim().equals("") <br>
   * <b>Precondition: </b> dataType is a valid core attribute value <br>
   * 
   * @param prepStmt
   *          value to format.
   * @param index
   *          index of the column in the prepared statement.
   * @param value
   *          value of the column in the prepared statement.
   * @param dataType
   *          dataType of the value.
   */
  public static void bindPreparedStatementValue(PreparedStatement prepStmt, int index, Object value, String dataType)
  {
    instance().bindPreparedStatementValue(prepStmt, index, value, dataType);
  }

  /**
   * Returns the given sql expression wrapped in a SQL uppercase function.
   * 
   * @param sqlExpression
   * @return sqlExpression wrapped in an uppercase function call in the sytnax
   *         of the database in use.
   */
  public static String toUpperFunction(String sqlExpression)
  {
    return instance().toUpperFunction(sqlExpression);
  }

  /**
   * Returns the variance function name for the database in use.
   * 
   * @return variance function name for the database in use.
   */
  public static String varianceFunction()
  {
    return instance().varianceFunction();
  }

  /**
   * Returns the standard deviation function name for the database in use.
   * 
   * @return standard deviation function name for the database in use.
   */
  public static String stdDevFunction()
  {
    return instance().stdDevFunction();
  }

  /**
   * Formats an SQL time value to a Java String.
   * 
   * @param value
   * @param dataType
   * @param ignoreCase
   * @return
   */
  public static String formatSQLToJavaTime(String value)
  {
    return instance().formatSQLToJavaTime(value);
  }

  /**
   * Formats an SQL date value to a Java String.
   * 
   * @param value
   * @param dataType
   * @param ignoreCase
   * @return
   */
  public static String formatSQLToJavaDate(String value)
  {
    return instance().formatSQLToJavaDate(value);
  }

  /**
   * Returns the type formatted for a DDL command to the vendor syntax.
   * 
   * @param type
   *          the numerical decimal type
   * @param length
   * @param decimal
   * @return
   */
  public static String formatDDLDecField(String type, String length, String decimal)
  {
    return instance().formatDDLDecField(type, length, decimal);
  }

  /**
   * Selects all instances of a column in the table that satisfy the given
   * condition.
   * 
   * @param columnName
   *          The name of the column being selected.
   * @param table
   *          The table containing the column.
   * @param condition
   *          The condition that entries must satisfy to be included in the
   *          result set.
   * @return List of DynaBeans representing the rows of the result set.
   */
  public static ResultSet selectFromWhere(String columnName, String table, String condition)
  {
    return instance().selectFromWhere(columnName, table, condition);
  }

  /**
   * Builds an SQL statement to delete an entry row.
   * 
   * @param table
   *          The table name where the row to delete can be found.
   * @param id
   *          The id of the record to delete.
   * @param seq
   *          The sequence number of the record to delete.
   * @return The SQL delete statement.
   */
  public static String buildSQLDeleteStatement(String table, String id, long seq)
  {
    return instance().buildSQLDeleteStatement(table, id, seq);
  }

  /**
   * Builds an SQL statement to delete an entry row.
   * 
   * @param table
   *          The table name where the row to delete can be found.
   * @param id
   *          The id of the record to delete.
   * @return The SQL delete statement.
   */
  public static String buildSQLDeleteStatement(String table, String id)
  {
    return instance().buildSQLDeleteStatement(table, id);
  }

  public static String buildSQLDeleteWhere(String table, List<String> conditions)
  {
    return instance().buildSQLDeleteWhereStatement(table, conditions);
  }

  /**
   * Throws the appropriate exception based on the severity of the error. Some
   * DB errors indicate a bug in the core.
   * 
   * @param ex
   */
  public static void throwDatabaseException(SQLException ex)
  {
    instance().throwDatabaseException(ex);
  }

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
  public static String buildSubstringFunctionCall(String stringName, int position, int length)
  {
    return instance().buildSubstringFunctionCall(stringName, position, length);
  }

  /**
   * Builds a database specific trim function call string.
   * 
   * @param columnName
   *          name of the column to trim.
   * @return a database specific trim function call string.
   */
  public static String buildTrimFunctionCall(String columnName)
  {
    return instance().buildTrimFunctionCall(columnName);
  }

  /**
   * Builds a database specific left trim function call string.
   * 
   * @param columnName
   *          name of the column to trim.
   * @return a database specific left trim function call string.
   */
  public static String buildLeftTrimFunctionCall(String columnName)
  {
    return instance().buildLeftTrimFunctionCall(columnName);
  }

  /**
   * Builds a database specific right trim function call string.
   * 
   * @param columnName
   *          name of the column to trim.
   * @return a database specific right trim function call string.
   */
  public static String buildRightTrimFunctionCall(String columnName)
  {
    return instance().buildRightTrimFunctionCall(columnName);
  }

  /**
   * Builds a database specific uppercase function call string.
   * 
   * @param columnName
   *          name of the column to uppercase.
   * @return a database specific uppercase function call string.
   */
  public static String buildUpperCaseFunctionCall(String columnName)
  {
    return instance().buildUpperCaseFunctionCall(columnName);
  }

  /**
   * Builds a database specific lowercase function call string.
   * 
   * @param columnName
   *          name of the column to lowercase.
   * @return a database specific lowercase function call string.
   */
  public static String buildLowerCaseFunctionCall(String columnName)
  {
    return instance().buildLowerCaseFunctionCall(columnName);
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
  public static String buildConcatFunctionCall(String concatString1, String concatString2)
  {
    return instance().buildConcatFunctionCall(concatString1, concatString2);
  }

  /**
   * Builds a database specific string position function call.
   * 
   * @param stringToFind
   *          string to find in hte search string
   * @param searchString
   *          starting position.
   * @return database specific string position function call.
   */
  public static String buildStringPositionFunctionCall(String stringToFind, String searchString)
  {
    return instance().buildStringPositionFunctionCall(stringToFind, searchString);
  }

  /**
   * Builds a database specific length function call.
   * 
   * @param columnName
   *          column to calculate the length on.
   * @return database specific length function call.
   */
  public static String buildLenthFunctionCall(String columnName)
  {
    return instance().buildLenthFunctionCall(columnName);
  }

  /**
   * Returns true if an object with the given id exists in the database.
   * 
   * @param id
   * @param tableName
   * @return true if it exists, false otherwise.
   */
  public static boolean doesObjectExist(String id, String tableName)
  {
    return instance().doesObjectExist(id, tableName);
  }

  /**
   * This is a special method used to update the baseClass attribute of MdType
   * and it is used only within the TransactionManagement aspect, hence it takes
   * a JDBC connection object as a parameter.
   * 
   * @param mdTypeId
   *          Id of the Type to update
   * @param updateTable
   *          Table to update
   * @param classColumnName
   *          Name of the column
   * @param classBytes
   *          The new byte array
   * @param sourceColumnName
   *          Name of the source column
   * @param source
   *          The new source value
   * @param conn
   */
  public static int updateClassAndSource(String mdTypeId, String updateTable, String classColumnName, byte[] classBytes, String sourceColumnName, String source, Connection conn)
  {
    return instance().updateClassAndSource(mdTypeId, updateTable, classColumnName, classBytes, sourceColumnName, source, conn);
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
  public static int updateMdFacadeGeneratedClasses(String mdFacadeId, String table, String serverClassesColumnName, byte[] serverClassesBytes, String commonClassesColumnName, byte[] commonClassesBytes, String clientClassesColumnName, byte[] clientClassesBytes, Connection conn)
  {
    return instance().updateMdFacadeGeneratedClasses(mdFacadeId, table, serverClassesColumnName, serverClassesBytes, commonClassesColumnName, commonClassesBytes, clientClassesColumnName, clientClassesBytes, conn);
  }

  /**
   * This is a special method used get the generated MdFacade server classes
   * from the database. This method is used when a transaction is rolled back to
   * restore the generated server classes to the file system from the database.
   * It is used only within the TransactionManagement aspect, hence it takes a
   * JDBC connection object as a parameter. It is up to the client to close the
   * connection object.
   * 
   * <b>Precondition: </b>Assumes an MdFacade exists in the database with the
   * given id.
   * 
   * @param mdFacadeId
   * @param conn
   */
  public static byte[] getMdFacadeServerClasses(String mdFacadeId, Connection conn)
  {
    return instance().getMdFacadeServerClasses(mdFacadeId, conn);
  }

  /**
   * This is a special method used get the generated MdFacade common classes
   * from the database. This method is used when a transaction is rolled back to
   * restore the generated common classes to the file system from the database.
   * It is used only within the TransactionManagement aspect, hence it takes a
   * JDBC connection object as a parameter. It is up to the client to close the
   * connection object.
   * 
   * <b>Precondition: </b>Assumes an MdFacade exists in the database with the
   * given id.
   * 
   * @param mdFacadeId
   * @param conn
   */
  public static byte[] getMdFacadeCommonClasses(String mdFacadeId, Connection conn)
  {
    return instance().getMdFacadeCommonClasses(mdFacadeId, conn);
  }

  /**
   * This is a special method used get the generated MdFacade client classes
   * from the database. This method is used when a transaction is rolled back to
   * restore the generated client classes to the file system from the database.
   * It is used only within the TransactionManagement aspect, hence it takes a
   * JDBC connection object as a parameter. It is up to the client to close the
   * connection object.
   * 
   * <b>Precondition: </b>Assumes an MdFacade exists in the database with the
   * given id.
   * 
   * @param mdFacadeId
   * @param conn
   */
  public static byte[] getMdFacadeClientClasses(String mdFacadeId, Connection conn)
  {
    return instance().getMdFacadeClientClasses(mdFacadeId, conn);
  }

  /**
   * This is a special method used get the MdFacade stub source from the
   * database. This method is used when a transaction is rolled back to restore
   * the stub source on the file system from the database. It is used only
   * within the TransactionManagement aspect, hence it takes a JDBC connection
   * object as a parameter. It is up to the client to close the connection
   * object.
   * 
   * <b>Precondition: </b>Assumes an MdFacade exists in the database with the
   * given id.
   * 
   * @param mdFacadeId
   * @param conn
   */
  public static String getMdFacadeStubSource(String mdFacadeId, Connection conn)
  {
    return instance().getMdFacadeStubSource(mdFacadeId, conn);
  }

  /**
   * This is a special method used get the MdFacade stub class from the
   * database. This method is used when a transaction is rolled back to restore
   * the stub class to the file system from the database. It is used only within
   * the TransactionManagement aspect, hence it takes a JDBC connection object
   * as a parameter. It is up to the client to close the connection object.
   * 
   * <b>Precondition: </b>Assumes an MdFacade exists in the database with the
   * given id.
   * 
   * @param mdFacadeId
   * @param conn
   */
  public static byte[] getMdFacadeStubClass(String mdFacadeId, Connection conn)
  {
    return instance().getMdFacadeStubClass(mdFacadeId, conn);
  }

  /**
   * This is a special method used get the MdClass dto stub source from the
   * database. This method is used when a transaction is rolled back to restore
   * the dto stub source on the file system from the database. It is used only
   * within the TransactionManagement aspect, hence it takes a JDBC connection
   * object as a parameter. It is up to the client to close the connection
   * object.
   * 
   * <b>Precondition: </b>Assumes an MdClass exists in the database with the
   * given id.
   * 
   * @param mdClassId
   * @param conn
   */
  public static String getMdClassDTOStubSource(String mdClassId, Connection conn)
  {
    return instance().getMdClassDTOStubSource(mdClassId, conn);
  }

  /**
   * This is a special method used get the MdClass dto stub class from the
   * database. This method is used when a transaction is rolled back to restore
   * the dto stub class to the file system from the database. It is used only
   * within the TransactionManagement aspect, hence it takes a JDBC connection
   * object as a parameter. It is up to the client to close the connection
   * object.
   * 
   * <b>Precondition: </b>Assumes an MdClass exists in the database with the
   * given id.
   * 
   * @param mdClassId
   * @param conn
   */
  public static byte[] getMdClassDTOStubClass(String mdClassId, Connection conn)
  {
    return instance().getMdClassDTOStubClass(mdClassId, conn);
  }

  /**
   * This is a special method used get the MdClass stub source from the
   * database. This method is used when a transaction is rolled back to restore
   * the stub source on the file system from the database. It is used only
   * within the TransactionManagement aspect, hence it takes a JDBC connection
   * object as a parameter. It is up to the client to close the connection
   * object.
   * 
   * <b>Precondition: </b>Assumes an MdClass exists in the database with the
   * given id.
   * 
   * @param mdClassId
   * @param conn
   */
  public static String getMdClassStubSource(String mdClassId, Connection conn)
  {
    return instance().getMdClassStubSource(mdClassId, conn);
  }

  /**
   * This is a special method used get the MdClass stub class from the database.
   * This method is used when a transaction is rolled back to restore the stub
   * class to the file system from the database. It is used only within the
   * TransactionManagement aspect, hence it takes a JDBC connection object as a
   * parameter. It is up to the client to close the connection object.
   * 
   * <b>Precondition: </b>Assumes an MdClass exists in the database with the
   * given id.
   * 
   * @param mdClassId
   * @param conn
   */
  public static byte[] getMdClassStubClass(String mdClassId, Connection conn)
  {
    return instance().getMdClassStubClass(mdClassId, conn);
  }

  /**
   * This is a special method used get the MdType base source from the database.
   * This method is used when a transaction is rolled back to restore the stub
   * source on the file system from the database. It is used only within the
   * TransactionManagement aspect, hence it takes a JDBC connection object as a
   * parameter. It is up to the client to close the connection object.
   * 
   * <b>Precondition: </b>Assumes an MdType exists in the database with the
   * given id.
   * 
   * @param mdTypeId
   * @param conn
   */
  public static String getMdTypeBaseSource(String mdTypeId, Connection conn)
  {
    return instance().getMdTypeBaseSource(mdTypeId, conn);
  }

  /**
   * This is a special method used get the MdType base class from the database.
   * This method is used when a transaction is rolled back to restore the base
   * class to the file system from the database. It is used only within the
   * TransactionManagement aspect, hence it takes a JDBC connection object as a
   * parameter. It is up to the client to close the connection object.
   * 
   * <b>Precondition: </b>Assumes an MdType exists in the database with the
   * given id.
   * 
   * @param mdTypeId
   * @param conn
   */
  public static byte[] getMdTypeBaseClass(String mdTypeId, Connection conn)
  {
    return instance().getMdTypeBaseClass(mdTypeId, conn);
  }

  public static byte[] getBlobAsBytes(MdTypeDAOIF mdType, String attributeName, Connection conn)
  {
    MdAttributeConcreteDAOIF mdAttributeConcreteDAOIF = (MdAttributeConcreteDAOIF) mdType.getMdAttributeDAO(attributeName);
    MdEntityDAOIF definedBy = (MdEntityDAOIF) mdAttributeConcreteDAOIF.definedByClass();
    String tableName = definedBy.getTableName();

    return instance().getBlobAsBytes(tableName, mdAttributeConcreteDAOIF.getColumnName(), mdType.getId(), conn);
  }

  public static String getSource(MdTypeDAOIF mdType, String attributeName, Connection conn)
  {
    MdAttributeConcreteDAOIF mdAttributeConcreteDAOIF = (MdAttributeConcreteDAOIF) mdType.getMdAttributeDAO(attributeName);
    MdEntityDAOIF definedBy = (MdEntityDAOIF) mdAttributeConcreteDAOIF.definedByClass();
    String tableName = definedBy.getTableName();

    return instance().getSourceField(mdType.getId(), conn, tableName, mdAttributeConcreteDAOIF.getColumnName());
  }

  /**
   * This is a special method used get the MdType dto source from the database.
   * This method is used when a transaction is rolled back to restore the stub
   * source on the file system from the database. It is used only within the
   * TransactionManagement aspect, hence it takes a JDBC connection object as a
   * parameter. It is up to the client to close the connection object.
   * 
   * <b>Precondition: </b>Assumes an MdType exists in the database with the
   * given id.
   * 
   * @param mdTypeId
   * @param conn
   */
  public static String getMdTypeDTOsource(String mdTypeId, Connection conn)
  {
    return instance().getMdTypeDTOsource(mdTypeId, conn);
  }

  /**
   * This is a special method used get the MdType dto class from the database.
   * This method is used when a transaction is rolled back to restore the base
   * class to the file system from the database. It is used only within the
   * TransactionManagement aspect, hence it takes a JDBC connection object as a
   * parameter. It is up to the client to close the connection object.
   * 
   * <b>Precondition: </b>Assumes an MdType exists in the database with the
   * given id.
   * 
   * @param mdTypeId
   * @param conn
   */
  public static byte[] getMdTypeDTOclass(String mdTypeId, Connection conn)
  {
    return instance().getMdTypeDTOclass(mdTypeId, conn);
  }

  /**
   * This is a special method used get the MdEntity query API source from the
   * database. This method is used when a transaction is rolled back to restore
   * the query API source on the file system from the database. It is used only
   * within the TransactionManagement aspect, hence it takes a JDBC connection
   * object as a parameter. It is up to the client to close the connection
   * object.
   * 
   * <b>Precondition: </b>Assumes an MdEntity exists in the database with the
   * given id.
   * 
   * @param mdEntityId
   * @param conn
   */
  public static String getMdEntityQueryAPIsource(String mdEntityId, Connection conn)
  {
    return instance().getMdEntityQueryAPIsource(mdEntityId, conn);
  }

  /**
   * This is a special method used get the MdEntity query DTO source from the
   * database. This method is used when a transaction is rolled back to restore
   * the query DTO source on the file system from the database. It is used only
   * within the TransactionManagement aspect, hence it takes a JDBC connection
   * object as a parameter. It is up to the client to close the connection
   * object.
   * 
   * <b>Precondition: </b>Assumes an MdEntity exists in the database with the
   * given id.
   * 
   * @param mdEntityId
   * @param conn
   */
  public static String getEntityQueryDTOsource(String mdEntityId, Connection conn)
  {
    return instance().getEntityQueryDTOsource(mdEntityId, conn);
  }

  /**
   * This is a special method used get the MdView query DTO source from the
   * database. This method is used when a transaction is rolled back to restore
   * the query DTO source on the file system from the database. It is used only
   * within the TransactionManagement aspect, hence it takes a JDBC connection
   * object as a parameter. It is up to the client to close the connection
   * object.
   * 
   * <b>Precondition: </b>Assumes an MdView exists in the database with the
   * given id.
   * 
   * @param mdViewId
   * @param conn
   */
  public static String getViewQueryDTOsource(String mdViewId, Connection conn)
  {
    return instance().getViewQueryDTOsource(mdViewId, conn);
  }

  /**
   * This is a special method used get the MdEntity query API class from the
   * database. This method is used when a transaction is rolled back to restore
   * the query API class to the file system from the database. It is used only
   * within the TransactionManagement aspect, hence it takes a JDBC connection
   * object as a parameter. It is up to the client to close the connection
   * object.
   * 
   * <b>Precondition: </b>Assumes an MdEntity exists in the database with the
   * given id.
   * 
   * @param mdEntityId
   * @param conn
   */
  public static byte[] getMdEntityQueryAPIclass(String mdEntityId, Connection conn)
  {
    return instance().getMdEntityQueryAPIclass(mdEntityId, conn);
  }

  /**
   * This is a special method used get the MdEntity query DTO class from the
   * database. This method is used when a transaction is rolled back to restore
   * the query DTO class to the file system from the database. It is used only
   * within the TransactionManagement aspect, hence it takes a JDBC connection
   * object as a parameter. It is up to the client to close the connection
   * object.
   * 
   * <b>Precondition: </b>Assumes an MdEntity exists in the database with the
   * given id.
   * 
   * @param mdEntityId
   * @param conn
   */
  public static byte[] getEntityQueryDTOclass(String mdEntityId, Connection conn)
  {
    return instance().getEntityQueryDTOclass(mdEntityId, conn);
  }

  /**
   * This is a special method used get the MdView query DTO class from the
   * database. This method is used when a transaction is rolled back to restore
   * the query DTO class to the file system from the database. It is used only
   * within the TransactionManagement aspect, hence it takes a JDBC connection
   * object as a parameter. It is up to the client to close the connection
   * object.
   * 
   * <b>Precondition: </b>Assumes an MdView exists in the database with the
   * given id.
   * 
   * @param mdEntityId
   * @param conn
   */
  public static byte[] getViewQueryDTOclass(String mdViewId, Connection conn)
  {
    return instance().getViewQueryDTOclass(mdViewId, conn);
  }

  /**
   * This is a special method used get the MdView base query source from the
   * database. This method is used when a transaction is rolled back to restore
   * the query source on the file system from the database. It is used only
   * within the TransactionManagement aspect, hence it takes a JDBC connection
   * object as a parameter. It is up to the client to close the connection
   * object.
   * 
   * <b>Precondition: </b>Assumes an MdView exists in the database with the
   * given id.
   * 
   * @param mdViewId
   * @param conn
   */
  public static String getMdViewBaseQuerySource(String mdViewId, Connection conn)
  {
    return instance().getMdViewBaseQuerySource(mdViewId, conn);
  }

  /**
   * This is a special method used get the MdView base query class from the
   * database. This method is used when a transaction is rolled back to restore
   * the query class to the file system from the database. It is used only
   * within the TransactionManagement aspect, hence it takes a JDBC connection
   * object as a parameter. It is up to the client to close the connection
   * object.
   * 
   * <b>Precondition: </b>Assumes an MdView exists in the database with the
   * given id.
   * 
   * @param mdViewId
   * @param conn
   */
  public static byte[] getMdViewBaseQueryClass(String mdViewId, Connection conn)
  {
    return instance().getMdViewBaseQueryClass(mdViewId, conn);
  }

  /**
   * This is a special method used get the MdView query stub source from the
   * database. This method is used when a transaction is rolled back to restore
   * the query source on the file system from the database. It is used only
   * within the TransactionManagement aspect, hence it takes a JDBC connection
   * object as a parameter. It is up to the client to close the connection
   * object.
   * 
   * <b>Precondition: </b>Assumes an MdView exists in the database with the
   * given id.
   * 
   * @param mdViewId
   * @param conn
   */
  public static String getMdViewStubQuerySource(String mdViewId, Connection conn)
  {
    return instance().getMdViewStubQuerySource(mdViewId, conn);
  }

  /**
   * This is a special method used get the MdView query stub class from the
   * database. This method is used when a transaction is rolled back to restore
   * the query class to the file system from the database. It is used only
   * within the TransactionManagement aspect, hence it takes a JDBC connection
   * object as a parameter. It is up to the client to close the connection
   * object.
   * 
   * <b>Precondition: </b>Assumes an MdView exists in the database with the
   * given id.
   * 
   * @param mdViewId
   * @param conn
   */
  public static byte[] getMdViewStubQueryClass(String mdViewId, Connection conn)
  {
    return instance().getMdViewStubQueryClass(mdViewId, conn);
  }

  /**
   * Returns the value of the database column that is used to cache enumeration
   * mappings for an attribute.
   * 
   * @param tableName
   *          name of the table
   * @param columnName
   *          name of the attribute
   * @param entityId
   *          id of an entity
   * @return value of the database column that is used to cache enumeration
   *         mappings for an attribute.
   */
  public static String getEnumCacheFieldInTable(String tableName, String columnName, String entityId)
  {
    return instance().getEnumCacheFieldInTable(tableName, columnName, entityId);
  }

  /**
   * Surrounds the given SQL statement with more SQL that will limit the range
   * of rows returned.
   * 
   * @param sqlStmt
   * @param limit
   *          number of rows to limit.
   * @param skip
   *          number of rows to skip from the beginning of the result.
   * @param selectClauseAttributes
   *          used by some databases.
   * @param orderByClause
   *          used by some databases.
   * @return
   */
  public static StringBuffer buildRowRangeRestriction(StringBuffer sqlStmt, int limit, int skip, String selectClauseAttributes, String orderByClause)
  {
    return instance().buildRowRangeRestriction(sqlStmt, limit, skip, selectClauseAttributes, orderByClause);
  }

  // //////////////////////////////////////////////////////////////
  // ////// Enumerations
  // //////////////////////////////////////////////////////////////

  /**
   * Returns the ids of the enumeration items that are mapped to the given
   * setId.
   * 
   * @param enumTableName
   * @param setId
   */
  public static Set<String> getEnumItemIds(String enumTableName, String setId)
  {
    return instance().getEnumItemIds(enumTableName, setId);
  }

  /**
   * Returns the SQL that inserts a mapping in the given enumeration table
   * between the given set id and the given enumeration item id.
   * 
   * @param enumTableName
   * @param setId
   * @param enumItemID
   */
  public static String buildAddItemStatement(String enumTableName, String setId, String enumItemID)
  {
    return instance().buildAddItemStatement(enumTableName, setId, enumItemID);
  }

  /**
   * Returns the SQL that updates an enum item id with the provided new enum
   * item id.
   * 
   * @param enumTableName
   * @param oldEnumItemId
   * @param newEnumItemId
   */
  public static String buildUpdateEnumItemStatement(String enumTableName, String oldEnumItemId, String newEnumItemId)
  {
    return instance().buildUpdateEnumItemStatement(enumTableName, oldEnumItemId, newEnumItemId);
  }

  /**
   * Removes the enumeration item from the enumeration mapping table.
   * 
   * @param enumTableName
   * @param enumItemID
   */
  public static void deleteEumerationItemFromLinkTable(String enumTableName, String enumItemID)
  {
    instance().deleteEumerationItemFromLinkTable(enumTableName, enumItemID);
  }

  /**
   * Deletes all instances of the setId from the given enumeration mapping
   * table.
   * 
   * @param enumTableName
   * @param setId
   */
  public static void deleteSetIdFromLinkTable(String enumTableName, String setId)
  {
    instance().deleteSetIdFromLinkTable(enumTableName, setId);
  }

  /**
   * Deletes all mapping instances for a particular attribute.
   * 
   * @param enumTableName
   * @param tableName
   * @param columnName
   */
  public static void deleteAllEnumAttributeInstances(String enumTableName, String tableName, String columnName)
  {
    instance().deleteAllEnumAttributeInstances(enumTableName, tableName, columnName);
  }

  /**
   * Gets a list of all enumerated types in the system.
   * 
   * @return list of all enumerated types in the system.
   */
  public static List<String> getAllEnumTypes()
  {
    return instance().getAllEnumTypes();
  }

  /**
   * Gets a list of all tables used by the application, including Runway
   * metadata.
   * 
   * @return list of all tables used by the application, including Runway
   *         metadata.
   */
  public static List<String> getAllApplicationTables()
  {
    List<String> tables = instance().getAllApplicationTables();
    tables.add(Database.PROPERTIES_TABLE);

    return tables;
  }

  /**
   * Gets the namespace for the Runway application
   * 
   * 
   * @return the namespace for the Runway application
   * 
   */
  public static String getApplicationNamespace()
  {
    return instance().getApplicationNamespace();
  }

  // //////////////////////////////////////////////////////////////
  // ////// Relationships
  // //////////////////////////////////////////////////////////////

  /**
   * Returns the number of distinct child instances for a given parent of the
   * given relationship type.
   * 
   * @param parent_id
   * @param relationshipTableName
   * @return number of distinct child instances for a given parent of the given
   *         relationship type.
   */
  public static long getChildCountForParent(String parent_id, String relationshipTableName)
  {
    return instance().getChildCountForParent(parent_id, relationshipTableName);
  }

  /**
   * Returns the number of distinct parent instances for a given child of the
   * given relationship type.
   * 
   * @param child_id
   * @param relationshipTableName
   * @return number of distinct parent instances for a given child of the given
   *         relationship type.
   */
  public static long getParentCountForChild(String child_id, String relationshipTableName)
  {
    return instance().getParentCountForChild(child_id, relationshipTableName);
  }

  /**
   * Creates a view.
   * 
   * @param viewName
   * @param selectClause
   */
  public static void createView(String viewName, String selectClause)
  {
    instance().createView(viewName, selectClause);
  }

  /**
   * Drops a view.
   * 
   * @param view
   * @param selectClause
   */
  public static void dropView(String viewName, String selectClause, Boolean dropOnEndOfTransaction)
  {
    instance().dropView(viewName, selectClause, dropOnEndOfTransaction);
  }

  /**
   * Gets a list of all views that start with the given prefix
   * 
   * @param prefix
   * @return
   */
  public static List<String> getViewsByPrefix(String prefix)
  {
    return instance().getViewsByPrefix(prefix);
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
  public static String backup(List<String> tableNames, String backupFileLocation, String backupFileRootName, boolean dropSchema)
  {
    return instance().backup(tableNames, backupFileLocation, backupFileRootName, dropSchema);
  }

  /**
   * Backs up the install to a file name in the given location.
   * 
   * @param namespace
   *          schema/namespace of Runway application
   * @param backupFileLocation
   *          location of the backup file to generate.
   * @param backupFileRootName
   *          root of the file name (minus the file extension).
   * @param dropSchema
   *          true if backup should include commands to drop the schema
   */
  public static String backup(String namespace, String backupFileLocation, String backupFileRootName, boolean dropSchema)
  {
    return instance().backup(namespace, backupFileLocation, backupFileRootName, dropSchema);
  }

  /**
   * Imports the given SQL file into the database
   * 
   * @param restoreSQLFile
   * @param logPrintStream
   */
  public static void importFromSQL(String restoreSQLFile, PrintStream logPrintStream)
  {
    instance().importFromSQL(restoreSQLFile, logPrintStream);
  }

  /**
   * Drops all of the tables given in the list. This method does not use the
   * command pattern.
   * 
   * @param tableNames
   *          list of tables to drop.
   */
  public static void dropTables(List<String> tableNames)
  {
    instance().dropTables(tableNames);
  }

  /**
   * Drops all of the views given in the list. This method does not use the
   * command pattern.
   * 
   * @param viewNames
   *          list of views to drop.
   */
  public static void dropViews(List<String> viewNames)
  {
    instance().dropViews(viewNames);
  }

  /**
   * Creates a geometry column in the database.
   * 
   * @param tableName
   * @param columnName
   * @param srid
   * @param geometryType
   * @param dimension
   */
  public static void addGeometryColumn(String tableName, String columnName, Integer srid, String geometryType, Integer dimension)
  {
    instance().addGeometryColumn(tableName, columnName, srid, geometryType, dimension);
  }

  /**
   * Drops a geometry column in the database.
   * 
   * @param tableName
   * @param columnName
   */
  public static void dropGeometryColumn(String tableName, String columnName)
  {
    instance().dropGeometryColumn(tableName, columnName);
  }

  /**
   * Returns the database specific union operator.
   */
  public static String getUnionOperator()
  {
    return instance().getUnionOperator();
  }

  /**
   * Returns the database specific union all operator.
   */
  public static String getUnionAllOperator()
  {
    return instance().getUnionAllOperator();
  }

  /**
   * Returns the database specific minus operator.
   */
  public static String getMinusOperator()
  {
    return instance().getMinusOperator();
  }

  /**
   * Returns the database specific intersect operator.
   */
  public static String getIntersectOperator()
  {
    return instance().getIntersectOperator();
  }

  /**
   * @param value
   * @param attributeIF
   */
  public static void validateClobLength(String value, AttributeIF attributeIF)
  {
    instance().validateClobLength(value, attributeIF);
  }
}
