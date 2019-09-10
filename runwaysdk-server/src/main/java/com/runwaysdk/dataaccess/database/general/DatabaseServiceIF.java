package com.runwaysdk.dataaccess.database.general;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdElementDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.cache.HardCodedMetadataIterator;
import com.runwaysdk.dataaccess.database.Changelog;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;

public interface DatabaseServiceIF
{

  String UNIQUE_ATTRIBUTE_GROUP_INDEX_PREFIX = "_group_unique";

  /**
   * Installs the runway core. This entails creating a new database, creating a
   * user for the runway to log in with, and setting any necessary permissions.
   */
  void initialSetup(String rootUser, String rootPass, String rootDb);

  /**
   * Drop the database.
   */
  void dropDb();

  /**
   * Creates the database.
   */
  void createDb(String rootDb);

  /**
   * Drops the database user.
   */
  void dropUser();

  /**
   * Creates the database user.
   */
  void createUser();

  /**
   * Closes all active connections to the database and cleans up resources.
   */
  void close();

  /**
   * Returns a savepoint.
   * 
   * @return a savepoint.
   */
  Savepoint setSavepoint();

  /**
   * Rolls back the savepoint.
   * 
   * @param savepoint
   *          to rollback.
   */
  void rollbackSavepoint(Savepoint savepoint);

  /**
   * Returns a DDL connection savepoint.
   * 
   * @return a DDL connection savepoint.
   */
  Savepoint setDDLsavepoint();

  /**
   * Rolls back the DDL connection savepoint.
   * 
   * @param DDL
   *          connection savepoint to rollback.
   */
  void rollbackDDLsavepoint(Savepoint savepoint);

  /**
   * Releases the savepoint.
   * 
   * @param savepoint
   *          to release.
   */
  void releaseSavepoint(Savepoint savepoint);

  /**
   * Releases the savepoint.
   * 
   * @param savepoint
   *          to release.
   */
  void releaseDDLsavepoint(Savepoint savepoint);

  Connection getConnectionRaw();

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
  Connection getConnection();

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
  Connection getDDLConnection();

  /**
   * Returns true if the database implementation shares the same connection
   * object for DDL and DML operations, false otherwise.
   * 
   * @return true if the database implementation shares the same connection
   *         object for DDL and DML operations, false otherwise.
   */
  boolean sharesDDLandDMLconnection();

  /**
   * All connections managed by the framework need to be closed using this
   * method. This is a hook method for the session management aspect.
   * 
   * @param conn
   * @throws SQLException
   */
  void closeConnection(Connection conn) throws SQLException;

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
  LinkedHashMap<String, String> getSuperEntityTypes(String type);

  void dropAll();

  /**
   * Returns the oid to the MdEntity that defines the given type. given OID.
   * 
   * @param type
   * @return oid to the MdEntity that defines the given type.
   */
  String getMdEntityId(String type);

  /**
   * Returns the name of the table in the database used to store instances of
   * the given class.
   * 
   * @param type
   * @return name of the table in the database used to store instances of the
   *         given class.
   */
  String getMdEntityTableName(String type);

  /**
   * Returns the type of the object with the given oid.
   * 
   * @param instanceId
   * @return the type of the object with the given oid.
   */
  String getTypeFromInstanceId(String instanceId);

  /**
   * Returns the type of the object with the given oid.
   * 
   * @param instanceId
   * @return the type of the object with the given oid.
   */
  Map<String, String> getTypeAndTableFromInstanceId(String instanceId);

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
  List<String> getConcreteSubClasses(String mdEntityId);

  /**
   * Returns a Map of Attribute objects for the EnityObject with the given OID
   * and class name. It only returns attributes that are explicitly defined by
   * the given class name.
   * 
   * <br/>
   * <b>Precondition:</b> type != null <br/>
   * <b>Precondition:</b> !type.trim().equals("") <br/>
   * <b>Precondition:</b> oid != null <br/>
   * <b>Precondition:</b> !oid.trim().equals("")
   * 
   * @param oid
   * @param type
   * @param tableName
   * @param relationshipAttributesHackMap
   *          this is a total hack. If the instance is a relationship, then
   *          return the parent_oid and child_oid values in this map.
   * @return Map of Attribute objects for the EnityObject with the given OID and
   *         class.
   */
  Map<String, Attribute> getAttributesForHardcodedMetadataObject(String oid, String type, String tableName, Map<String, String> relationshipAttributesHackMap, boolean rootClass);

  /**
   * Returns a Map of a Map of Attribute objects for the given type. It only
   * returns attributes that are explicitly defined by the given type.
   * 
   * <br/>
   * <b>Precondition:</b> type != null <br/>
   * <b>Precondition:</b> !type.trim().equals("") <br/>
   * <b>Precondition:</b> oid != null <br/>
   * <b>Precondition:</b> !oid.trim().equals("")
   * 
   * @param _cacheTypeTable
   * @param _type
   * @param _tableName
   * @param relationshipAttributesHackMap
   *          this is a total hack. If the instance is a relationship, then
   *          return the parent_oid and child_oid values in this map.
   * @return Map of Attribute objects for the EnityObject with the given OID and
   *         class.
   */
  HardCodedMetadataIterator getAttributesForHardcodedMetadataType(String _cacheTypeTable, String _type, String _tableName, Map<String, Map<String, String>> _relationshipAttributesHackMap, boolean _rootClass);

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
  boolean isValidType(String type);

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
   *         queriying the database.
   */
  List<String> getEntityIds(String type);

  /**
   * Drop a column from a table.
   * 
   * @param table
   *          The table containing the field to drop.
   * @param columnName
   *          The field being dropped.
   * @param dbColumnType
   *          the database column type formatted to the database vendor syntax.
   * @param mdAttributeConcreteDAO
   *          metadata that defines the column.
   */
  void dropField(String table, String columnName, String dbColumnType, MdAttributeConcreteDAO mdAttributeConcreteDAO);

  /**
   * Returns a string that adds a column to the given table.
   * 
   * @param table
   * @param columnName
   * @param formattedColumnTree
   * 
   * @return string that adds a column to the given table.
   */
  String buildAddColumnString(String table, String columnName, String formattedColumnTree);

  /**
   * Returns a string that drops a column from the given table.
   * 
   * @param table
   * @param columnName
   * @return string that drops a column from the given table.
   */
  String buildDropColumnString(String table, String columnName);

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
  void alterFieldType(String table, String columnName, String newDbColumnType, String oldDbColumnType);

  /**
   * Adds temporary fields for the given column on the given table.
   * 
   * @param tableName
   * @param columnName
   * @param columnType
   * @param numberOfTempFields
   */
  void addTempFieldsToTable(String tableName, String columnName, String columnType, Integer numberOfTempFields);

  /**
   * Creates a temporary table that lasts for at most the duration of the
   * session. The behavior on transaction commit is configurable with the
   * onCommit parameter.
   * 
   * @param tableName
   *          The name of the temp table.
   * @param columns
   *          An array of MdAttribute class names that represent the columns in
   *          the table.
   * @param onCommit
   *          Decides the fate of the temporary table upon transaction commit.
   */
  void createTempTable(String tableName, List<String> columns, String onCommit);

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
  void addField(String table, String columnName, String type, String size);

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
  String addFieldBatch(String table, String columnName, String type, String size);

  /**
   * Adds a field (column) to a table in the database with an alter table
   * statement. Creates an undo DROP command in case transaction management
   * requires a rollback.
   * 
   * @param table
   *          The table that the field is being added to.
   * @param columnName
   *          The name of the new field.
   * @param formattedType
   *          DDL column type definition formatted to the syntax of the DB
   *          vendor.
   * @param mdAttributeConcreteDAO
   *          metadata that defines the column
   */
  void addField(String table, String columnName, String formattedType, MdAttributeConcreteDAO mdAttributeConcreteDAO);

  /**
   * Returns SQL to add a field (column) to a table in the database.
   * 
   * @param table
   *          The table that the field is being added to.
   * @param columnName
   *          The name of the new field.
   * @param formattedType
   *          DDL column type definition formatted to the syntax of the DB
   *          vendor.
   * @param type
   *          The database type of the new field.
   * @param size
   *          The size of new field. <code><b>null</b></code> if the type does
   *          not require a size parameter.
   * @param mdAttributeConcreteDAO
   *          metadata that defines the column
   */
  String addFieldBatch(String table, String columnName, String formattedType, MdAttributeConcreteDAO mdAttributeConcreteDAO);

  /**
   * Adds a floating-point field (column) to a table in the database with an
   * alter table statement. Creates an undo DROP command in case transaction
   * management requires a rollback.
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
  void addDecField(String table, String columnName, String type, String length, String decimal);

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
  String addDecFieldBatch(String table, String columnName, String type, String length, String decimal);

  /**
   * Returns the type formatted for a DDL command to the vendor syntax.
   * 
   * @param type
   *          the numerical decimal type
   * @param length
   * @param decimal
   * @return
   */
  String formatDDLDecField(String type, String length, String decimal);

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
  String formatCharacterField(String type, String length);

  /**
   * Returns the text type formatted for a DDL command to the vendor syntax.
   * 
   * @param type
   *          the numerical decimal type
   * @return
   */
  String formatTextField(String type);

  /**
   * Returns the CLOB type formatted for a DDL command to the vendor syntax.
   * 
   * @param type
   *          the numerical decimal type
   * @return
   */
  String formatClobField(String type);

  /**
   * 
   * @param table
   * @param columnName
   * @param indexName
   */
  void addUniqueIndex(String table, String columnName, String indexName);

  /**
   * 
   * @param table
   * @param columnName
   * @param indexName
   * @param delete
   */
  void dropUniqueIndex(String table, String columnName, String indexName, boolean delete);

  /**
   * @see com.runwaysdk.dataaccess.database.Database#addNonUniqueIndex(String,
   *      String, String);
   */
  void addNonUniqueIndex(String table, String columnName, String indexName);

  /**
   * @see com.runwaysdk.dataaccess.database.Database#dropNonUniqueIndex(String,
   *      String, String, boolean);
   * @param table
   * @param columnName
   * @param indexName
   * @param delete
   */
  void dropNonUniqueIndex(String table, String columnName, String indexName, boolean delete);

  /**
   * @see com.runwaysdk.dataaccess.database.Database#nonUniqueAttributeExists(String,
   *      String, String);
   */
  boolean nonUniqueAttributeExists(String table, String columnName, String indexName);

  /**
   * @see com.runwaysdk.dataaccess.database.Database#uniqueAttributeExists(String,
   *      String, String);
   */
  boolean uniqueAttributeExists(String table, String columnName, String indexName);

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
  void addGroupAttributeIndex(String table, String indexName, List<String> columnNames, boolean isUnique);

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
  void dropGroupAttributeIndex(String table, String indexName, List<String> columnNames, boolean isUnique, boolean delete);

  /**
   * Returns true if the given index exists on the given table, false otherwise.
   * 
   * @param table
   * @param indexName
   * @return true if the given index exists on the given table, false otherwise.
   */
  boolean indexExists(String table, String indexName);

  /**
   * Returns true if indexes need to be rebuilt if a column is modified, false
   * otherwise. Some databases don't like it when you alter a column that has an
   * index on it.
   * 
   * @return true if indexes need to be rebuilt if a column is modified, false
   *         otherwise.
   */
  boolean rebuildIndexOnModifyColumn();

  /**
   * Returns true if a group attribute index exists with the given name and the
   * given attributes on the given table.
   * 
   * @param tableName
   * @param indexName
   * @param columnNames
   */
  boolean groupAttributeIndexExists(String tableName, String indexName, List<String> columnNames);

  /**
   * Returns true if a group attribute index exists with the given name on the
   * given table.
   * 
   * @param tableName
   * @param indexName
   */
  boolean groupAttributeIndexExists(String tableName, String indexName);

  /**
   * 
   * @param table
   * @param columnName
   * @return
   */
  String attributeIndexName(String table, String columnName);

  /**
   * 
   * @param stringToHash
   * @return
   */
  String getOidentifierHashName(String stringToHash);

  /**
   * Creates a database friendly identifier from the given oid.
   * 
   * @param oid
   * 
   * @return database friendly identifier from the given oid.
   */
  String createIdentifierFromId(String oid);

  /**
   * Returns a list of string names of the attributes that participate in a
   * group index for the given table with the index of the given name.
   * 
   * @param table
   * @param indexName
   */
  List<String> getGroupIndexAttributes(String table, String indexName);

  /**
   * Creates a new table in the database for a class. Automatically adds the
   * Component.OID field as the primary key.
   * 
   * @param tableName
   *          The name of the new table.
   */
  void createClassTable(String tableName);

  /**
   * Returns the SQL string for a new table in the database for a class, minus
   * the closing parenthesis. Automatically adds the Component.OID field as the
   * primary key.
   * 
   * @param tableName
   *          The name of the new table.
   */
  String startCreateClassTable(String tableName);

  /**
   * Creates a new table in the database for a class, including all columns for
   * that table.
   * 
   * @param tableName
   *          table name
   * @param columnDefs
   *          columnDefs column definitions.
   */
  void createClassTableBatch(String tableName, List<String> columnDefs);

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
  void alterClassTableBatch(String tableName, List<String> columnNames, List<String> columnDefs);

  /**
   * Returns the SQL string that concludes a table definition. Typically it is a
   * closing parenthesis.
   * 
   * @param tableName
   *          The name of the new table.
   */
  String endCreateClassTable(String tableName);

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
  void createRelationshipTable(String tableName, String index1Name, String index2Name, boolean isUnique);

  /**
   * Creates a new table in the database for relationship, including all columns
   * for that table.
   * 
   * @param tableName
   *          table name
   * @param columnDefs
   *          columnDefs column definitions.
   */
  void createRelationshipTableBatch(String tableName, List<String> columnDefs);

  /**
   * Returns the SQL string for a new table in the database for a relationship,
   * minus the closing parenthesis. Automatically adds the Component.OID field
   * as the primary key.
   * 
   * @param tableName
   *          The name of the new table.
   */
  String startCreateRelationshipTableBatch(String tableName);

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
  void createRelationshipTableIndexesBatch(String tableName, String index1Name, String index2Name, boolean isUnique);

  /**
   * @see com.runwaysdk.dataaccess.database.Database#createEnumerationTable(String,
   *      String);
   */
  void createEnumerationTable(String tableName, String oid);

  /**
   * Builds an SQL statement to delete an entry row.
   * 
   * @param table
   *          The table name where the row to delete can be found.
   * @param oid
   *          The oid of the record to delete.
   * @return The SQL delete statement.
   */
  String buildSQLDeleteStatement(String table, String oid);

  /**
   * Builds an SQL statement to delete an entry row.
   * 
   * @param table
   *          The table name where the row to delete can be found.
   * @param oid
   *          The oid of the record to delete.
   * @param seq
   *          The sequence number of the record to delete.
   * @return The SQL delete statement.
   */
  String buildSQLDeleteStatement(String table, String oid, long seq);

  /**
   * Builds an SQL statement to delete an entry row.
   * 
   * @param table
   *          The table name where the row to delete can be found.
   * @param oid
   *          The oid of the record to delete.
   * @param seq
   *          The sequence number of the record to delete.
   * @return The SQL delete statement.
   */
  String buildSQLDeleteWhereStatement(String table, List<String> conditions);

  /**
   * A generalized delete that deletes all entries in the specifice table that
   * satisfy the given condition.
   * 
   * @param table
   *          The name of the table.
   * @param condition
   *          The condition that all items to be deleted must satisfy.
   */
  void deleteWhere(String table, String condition);

  /**
   * Creates a view.
   * 
   * @param view
   * @param selectClause
   */
  void createView(String view, String selectClause);

  /**
   * Drops a view.
   * 
   * @param view
   * @param selectClause
   */
  void dropView(String view, String selectClause, Boolean dropOnEndOfTransaction);

  List<String> getViewsByPrefix(String prefix);

  /**
   * Drops an entire table from the database for a class. An undo command is
   * created that will recreate the table if transaction management requires a
   * rollback. However, the undo will <b>not </b> recreate all of the fields in
   * the table, only the Component.OID.
   * 
   * @param table
   *          The name of the table to drop.
   */
  void dropClassTable(String tableName);

  /**
   * Deletes all records in the given table.
   * 
   * @param tableName
   *          The name of the table to remove all records from.
   */
  void deleteAllTableRecords(String tableName);

  /**
   * Drops an entire table from the database for a relationship. An undo command
   * is created that will recreate the table if transaction management requires
   * a rollback. However, the undo will <b>not </b> recreate all of the fields
   * in the table, only the Component.OID.
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
  void dropRelationshipTable(String tableName, String index1Name, String index2Name, boolean isUnique);

  /**
   * @see com.runwaysdk.dataaccess.database.Database#dropEnumerationTable(String,
   *      String);
   */
  void dropEnumerationTable(String tableName, String oid);

  /**
   * Selects all instances of a field from a table.
   * 
   * @param columnName
   *          The name of the field being selected.
   * @param table
   *          The table containing the field.
   * @return List of DynaBeans representing the rows of the result set.
   */
  ResultSet selectFrom(String columnName, String table);

  /**
   * Selects all instances of a field in the table that satisfy the given
   * condition.
   * 
   * @param columnName
   *          The name of the field being selected.
   * @param table
   *          The table containing the field.
   * @param condition
   *          The condition that entries must satisfy to be included in the
   *          result set.
   * @return List of DynaBeans representing the rows of the result set.
   */
  ResultSet selectFromWhere(String columnName, String table, String condition);

  /**
   * Builds a String representation of an SQL SELECT statement. Allows for
   * multiple fields, tables, and conditions. See
   * {@link #select(List, List, List)}for more information.
   * 
   * @param columnNames
   *          List of the fields being selected.
   * @param tables
   *          List of the tables being joined.
   * @param conditions
   *          List of conditions that must be satisfied to be included in the
   *          result set.
   * @return The SQL query String representing the parameters.
   */
  String selectClause(List<String> columnNames, List<String> tables, List<String> conditions);

  /**
   * Generalized <code>SELECT</code> statement. Selects a variable number of
   * fields from a variable number of tables that satisfy a variable number of
   * conditions. <code>fields</code> and <code>tables</code> assume a minimum of
   * one element in their respective <code>List</code>s, while
   * <code>conditions</code> makes no assumption. A query with multiple tables
   * will use the database's default join mechanism (usually a cartesian
   * product). This is obviously very inefficient, and should be countered with
   * conditions that will narrow the join space.
   * 
   * @param columnNames
   *          List of the fields being selected.
   * @param tables
   *          List of the tables being joined.
   * @param conditions
   *          List of conditions that must be satisfied to be included in the
   *          result set.
   * @return List of DynaBeans representing the rows of the result set.
   */
  ResultSet select(List<String> columnNames, List<String> tables, List<String> conditions);

  /**
   * Builds a JDBC prepared <code>INSERT</code> statement for the given fields.
   * <br>
   * 
   * @param table
   *          The table to insert into.
   * @param columnNames
   *          The names of the fields being inserted.
   * @param prepStmtVars
   *          usually just a "?", but some types require special functions.
   * @param values
   *          The values of the fields being inserted.
   * @param attributeTypes
   *          The core datatypes of the fields being inserted.
   * 
   * @return SQL insert statement
   */
  PreparedStatement buildPreparedSQLInsertStatement(String table, List<String> columnNames, List<String> prepStmtVars, List<Object> values, List<String> attributeTypes);

  /**
   * Builds an <code>INSERT</code> statement for the given fields. This is used
   * primarily for logging SQL statements. For actual database insert
   * statements, please use the method that returns a
   * <code>PreparedStatement</code> object. <br>
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
   * @return SQL <code>INSERT</code> statement
   */
  String buildSQLinsertStatement(String table, List<String> columnNames, List<Object> values, List<String> attributeTypes);

  /**
   * Builds a JDBC prepared <code>UPDATE</code> statement for the given fields.
   * <br>
   * 
   * @param table
   *          The table to insert into.
   * @param columnNames
   *          The names of the fields being inserted.
   * @param prepStmtVars
   *          usually just a "?", but some types require special functions.
   * @param values
   *          The values of the fields to update.
   * @param attributeTypes
   *          The core datatypes of the fields to update.
   * @param oid
   *          oid of the object to update.
   * 
   * @return <code>UPDATE</code> PreparedStatement
   */
  PreparedStatement buildPreparedSQLUpdateStatement(String table, List<String> columnNames, List<String> prepStmtVars, List<Object> values, List<String> attributeTypes, String oid);

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
   * @param oid
   *          oid of the object to update.
   * 
   * @return <code>UPDATE</code> SQL statement String
   */
  String buildSQLupdateStatement(String table, List<String> columnNames, List<Object> values, List<String> attributeTypes, String oid);

  /**
   * Builds a JDBC prepared <code>UPDATE</code> statement for the given fields.
   * <br>
   * 
   * @param table
   *          The table to insert into.
   * @param columnNames
   *          The names of the fields being inserted.
   * @param prepStmtVars
   *          usually just a "?", but some types require special functions.
   * @param values
   *          The values of the fields to update.
   * @param attributeTypes
   *          The core datatypes of the fields to update.
   * @param oid
   *          oid of the object to update.
   * @param seq
   *          sequence of the object to update.
   * 
   * @return <code>UPDATE</code> PreparedStatement
   */
  PreparedStatement buildPreparedSQLUpdateStatement(String table, List<String> columnNames, List<String> prepStmtVars, List<Object> values, List<String> attributeTypes, String oid, long seq);

  /**
   * Builds a JDBC prepared <code>UPDATE</code> statement for the given field on
   * the object with the given oid. <br>
   * 
   * @param table
   *          The table to insert into.
   * @param columnName
   *          The name of the field being updated.
   * @param entityId
   *          entity OID
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
  PreparedStatement buildPreparedUpdateFieldStatement(String table, String entityId, String columnName, String prepStmtVar, Object oldValue, Object newValue, String attributeType);

  /**
   * Builds a JDBC prepared <code>UPDATE</code> statement for the given field on
   * the object with the given oid. <br>
   * 
   * @param table
   *          The table to insert into.
   * @param columnName
   *          The name of the field being updated.
   * @param entityId
   *          entity OID
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
  PreparedStatement buildPreparedUpdateFieldStatement(String table, String entityId, String columnName, String prepStmtVar, Object newValue, String attributeType);

  /**
   * Builds a SQL <code>UPDATE</code> statement for the given fields. <br>
   * 
   * @param table
   *          The table to insert into.
   * @param columnName
   *          The name of the field being updated.
   * @param entityId
   *          entity OID
   * @param prepStmtVar
   *          usually just a "?", but some types require special functions.
   * @param oldValue
   *          The original value
   * @param newValue
   *          The value of the field to update.
   * @param attributeType
   *          The core datatype of the field to update
   * 
   * @return <code>UPDATE</code> SQL string
   */
  String buildPreparedSQLUpdateField(String table, String entityId, String columnName, Object oldValue, Object newValue, String attributeType);

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
  int[] executeBatch(List<String> sqlStmts);

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
  int[] executeStatementBatch(List<PreparedStatement> preparedStmts);

  /**
   * Returns the value of a clob for the column on the table for the object with
   * the given oid.
   * 
   * <br/>
   * precondition: oid must represent a valid object <br/>
   * 
   * @param table
   * @param columnName
   * @param oid
   * @return value of the clob.
   */
  String getClob(String table, String columnName, String oid);

  /**
   * Sets the value of this blob as the specified bytes.
   * 
   * @param table
   * @param columnName
   * @param oid
   * @param clobString
   * @return The number of bytes written.
   */
  void setClob(String table, String columnName, String oid, String clobString);

  /**
   * Returns the value of a blob as a byte array.
   * 
   * @param table
   * @param columnName
   * @param oid
   * @return byte[] value of the blob.
   */
  byte[] getBlobAsBytes(String table, String columnName, String oid);

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
  byte[] getBlobAsBytes(String table, String columnName, String oid, Connection conn);

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
  byte[] getBlobAsBytes(String table, String columnName, String oid, long pos, int length);

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
  int setBlobAsBytes(String table, String columnName, String oid, long pos, byte[] bytes, int offset, int length);

  /**
   * Sets the value of this blob as the specified bytes.
   * 
   * @param table
   * @param columnName
   * @param oid
   * @param bytes
   * @return The number of bytes written.
   */
  int setBlobAsBytes(String table, String columnName, String oid, byte[] bytes);

  /**
   * Returns the blob as an array of bytes.
   * 
   * @param table
   * @param columnName
   * @param oid
   * @return The byte array value of this blob attribute.
   */
  long getBlobSize(String table, String columnName, String oid);

  /**
   * Truncates a blob by the specified length.
   * 
   * @param table
   * @param columnName
   * @param oid
   * @param length
   */
  void truncateBlob(String table, String columnName, String oid, long length, Connection conn);

  /**
   * Throws the appropriate exception based on the severity of the error. Some
   * DB errors indicate a bug in the core.
   * 
   * @param ex
   *          SQLException thrown.
   */
  void throwDatabaseException(SQLException ex);

  /**
   * Returns true if, in order to produce a meaningful error message, the
   * database must manually check uniqueness constraints, rather than relying on
   * the database. Some databases do not return enough useful information in the
   * error message to produce a meaningful message to the end user.
   * 
   * @return true must manually check uniqueness constraints for the given
   *         database, false otherwise.
   */
  boolean manuallyCheckForDuplicates();

  /**
   * Throws the appropriate exception based on the severity of the error. Some
   * DB errors indicate a bug in the core.
   * 
   * @param ex
   *          SQLException thrown.
   * @param sqlStmt
   *          SQL statement that caused the exception to be thrown.
   */
  void throwDatabaseException(SQLException ex, String debugMsg);

  /**
   * Returns a List containing the fields in a table.
   * 
   * @param tableName
   *          The table to get the field list from.
   * @return The List of the fields in the table.
   */
  List<String> getColumnNames(String tableName);

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
  boolean columnExists(String columnName, String tableName);

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
  boolean tableExists(String tableName);

  /**
   * Executes a SQL query (which is assumed to be valid) against the database,
   * returning a RowSet object the result. It is up to the client to close the
   * <code>ResultSet</code>, the statement, and the connection. <br>
   * <b>Precondition: </b> statement != null <br>
   * <b>Precondition: </b> sqlStmt != null <br>
   * <b>Precondition: </b> !sqlStmt.trim().equals("") <br>
   * <b>Postcondition: </b> return value may not be null
   * 
   * @param sqlStmt
   *          SQL query statement
   */
  ResultSet query(String sqlStmt);

  /**
   * Returns fields that are needed by <code>MdAttributeDimensionDAOIF</code>
   * objects. If the given parameter is null, then all objects are returned.
   * Otherwise, it returns fields just for object associated with the given
   * <code>MdAttributeDAOIF</code> oid.
   * 
   * @return ResultSet contains fields that are needed by
   *         <code>MdAttributeDimensionDAOIF</code> objects. If the given
   *         parameter is null, then all objects are returned. Otherwise, it
   *         returns fields just for object associated with the given
   *         <code>MdAttributeDAOIF</code> oid.
   */
  ResultSet getMdAttributeDimensionFields(String mdAttributeId);

  /**
   * Returns ids for <code>MdAttributeDimensionDAOIF</code>s. If the given oid
   * is null, then all objects are returned. Otherwise, the
   * <code>MdAttributeDimensionDAOIF</code>s for the
   * <code>MdDimensionDAOIF</code> with the given oid.
   * 
   * @param mdDimensionId
   * @return ids for <code>MdAttributeDimensionDAOIF</code>s. If the given oid
   *         is null, then all objects are returned. Otherwise, the
   *         <code>MdAttributeDimensionDAOIF</code>s for the
   *         <code>MdDimensionDAOIF</code> with the given oid.
   */
  ResultSet getMdAttributeDimensionIds(String mdDimensionId);

  /**
   * Used by the system. It is the responsibility of the caller to provide and
   * close the connection object. Executes a SQL query (which is assumed to be
   * valid) against the database, returning a List of the rows of the result.
   * Each object in the list is a row, represented by a DynaBean. Specific
   * attributes can be retrieved from the DynaBean with the get(String
   * attributeName) method. Attribute names are case-sensitive. <br>
   * <br>
   * <b>Precondition: </b> statement != null <br>
   * <b>Precondition: </b> sqlStmt != null <br>
   * <b>Precondition: </b> !sqlStmt.trim().equals("") <br>
   * <b>Postcondition: </b> return value may not be null
   * 
   * @param sqlStmt
   *          SQL query statement
   * @param conx
   *          JDBC Conneciton object
   */
  ResultSet query(String sqlStmt, Connection conx);

  /**
   * {@link #execute(String)}s an SQL statement.
   * 
   * @param statement
   *          The SQL statement being parsed and executed.
   * @see #execute(String)
   */
  void parseAndExecute(String statement);

  /**
   * Gets the next sequence number from the database. Concrete implementations
   * should be <code><b>synchronized</b></code>.
   * 
   * @return The next sequence number from the database.
   */
  String getNextSequenceNumber();

  /**
   * Hardcoded database commands that create the database sequence that is used
   * to help generate unique ids.
   */
  void createObjectSequence();

  /**
   * Gets the next sequence number from the database. Concrete implementations
   * should be <code><b>synchronized</b></code>.
   * 
   * @return The next sequence number from the database.
   */
  String getNextTransactionSequence();

  /**
   * Hardcoded database commands that create the database sequence that is used
   * to help generate unique transaction numbers.
   */
  void createTransactionSequence();

  /**
   * Resets the transaction sequence. This should ONLY be called for runway
   * development testing purposes.
   */
  void resetTransactionSequence();

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
  String formatJavaToSQL(String value, String dataType, boolean ignoreCase);

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
  String formatJavaToSQLForQuery(String value, String dataType, boolean ignoreCase);

  /**
   * Different databases format column aliases differently in the column clause
   * of a select statement. Returns the given String column alias formatted to
   * the syntax of the database vendor.
   * 
   * @param columnAlias
   * @return given String column alias formatted to the syntax of the database
   *         vendor.
   */
  String formatColumnAlias(String columnAlias);

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
  String formatColumnAlias(String columnAlias, String dataType);

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
  void bindPreparedStatementValue(PreparedStatement prepStmt, int index, Object value, String dataType);

  /**
   * Returns the given sql expression wrapped in a SQL uppercase function.
   * 
   * @param sqlExpression
   * @return sqlExpression wrapped in an uppercase function call in the sytnax
   *         of the database in use.
   */
  String toUpperFunction(String sqlExpression);

  /**
   * Returns the variance function name for the database in use.
   * 
   * @return variance function name for the database in use.
   */
  String varianceFunction();

  /**
   * Returns the standard deviation function name for the database in use.
   * 
   * @return standard deviation function name for the database in use.
   */
  String stdDevFunction();

  /**
   * Formats an SQL time value to a Java String.
   * 
   * @param value
   * @param dataType
   * @param ignoreCase
   * @return
   */
  String formatSQLToJavaTime(String value);

  /**
   * Formats the column for a comparison in a select statement.
   * 
   * @param formatted
   *          column name.
   * @return formatted column name.
   */
  String formatColumnForCompare(String qualifiedColumnName, String dataType);

  /**
   * Formats a column for a select statement.
   * 
   * @param qualifiedColumnName
   * @param mdAttribute
   *          MdAttribute that defines the attribute that uses the given column.
   * @return
   */
  String formatSelectClauseColumn(String qualifiedColumnName, MdAttributeConcreteDAOIF mdAttribute);

  /**
   * Formats an SQL date value to a Java String.
   * 
   * @param value
   * @param dataType
   * @param ignoreCase
   * @return
   */
  String formatSQLToJavaDate(String value);

  /**
   * Escapes characters that are harmful to SQL statements. This helps to
   * prevent SQL Injections and secures the database.
   * 
   * <br>
   * <b>Precondition: </b> sqlStmt != null <br>
   * <b>Precondition: </b> !sqlStmt.trim().equals("")
   * 
   * @param sqlStmt
   */
  String escapeSQLCharacters(String sqlStmt);

  /**
   * Returns true if an object with the given oid exists in the database.
   * 
   * @param oid
   * @param tableName
   * @return true if it exists, false otherwise.
   */
  boolean doesObjectExist(String oid, String tableName);

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
  String buildSubstringFunctionCall(String stringName, int position, int length);

  /**
   * Builds a database specific trim function call string.
   * 
   * @param columnName
   *          name of the column to trim.
   * @return a database specific trim function call string.
   */
  String buildTrimFunctionCall(String columnName);

  /**
   * Builds a database specific left trim function call string.
   * 
   * @param columnName
   *          name of the column to trim.
   * @return a database specific left trim function call string.
   */
  String buildLeftTrimFunctionCall(String columnName);

  /**
   * Builds a database specific right trim function call string.
   * 
   * @param columnName
   *          name of the column to trim.
   * @return a database specific right trim function call string.
   */
  String buildRightTrimFunctionCall(String columnName);

  /**
   * Builds a database specific uppercase function call string.
   * 
   * @param columnName
   *          name of the column to uppercase.
   * @return a database specific uppercase function call string.
   */
  String buildUpperCaseFunctionCall(String columnName);

  /**
   * Builds a database specific lowercase function call string.
   * 
   * @param columnName
   *          name of the column to lowercase.
   * @return a database specific lowercase function call string.
   */
  String buildLowerCaseFunctionCall(String columnName);

  /**
   * Builds a database specific concat function call string.
   * 
   * @param concatString1
   *          name of the original string.
   * @param concatString2
   *          starting position.
   * @return a database specific concat function call string.
   */
  String buildConcatFunctionCall(String concatString1, String concatString2);

  /**
   * Builds a database specific string position function call.
   * 
   * @param stringToFind
   *          string to find in hte search string
   * @param searchString
   *          starting position.
   * @return database specific string position function call.
   */
  String buildStringPositionFunctionCall(String stringToFind, String searchString);

  /**
   * Builds a database specific length function call.
   * 
   * @param columnName
   *          column to calculate the length on.
   * @return database specific length function call.
   */
  String buildLenthFunctionCall(String columnName);

  /**
   * Create the VERSION table. This must be done before building the metadata.
   */
  void buildDynamicPropertiesTable();

  /**
   * 
   */
  void buildChangelogTable();

  /**
   * This is a special method used to update the baseClass attribute of MdType
   * and it is used only within the TransactionManagement aspect, hence it takes
   * a JDBC connection object as a parameter. It is up to the client to close
   * the connection object.
   * 
   * @param mdTypeId
   * @param updateTable
   * @param classColumnName
   * @param classBytes
   * @param sourceColumnName
   * @param conn
   */
  int updateClassAndSource(String mdTypeId, String table, String classColumnName, byte[] classBytes, String sourceColumnName, String source, Connection conn);

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
  int updateMdFacadeGeneratedClasses(String mdFacadeId, String table, String serverClassesColumnName, byte[] serverClassesBytes, String commonClassesColumnName, byte[] commonClassesBytes, String clientClassesColumnName, byte[] clientClassesBytes, Connection conn);

  /**
   * This is a special method used get the MdType base class from the database.
   * This method is used when a transaction is rolled back to restore the base
   * class to the file system from the database. It is used only within the
   * TransactionManagement aspect, hence it takes a JDBC connection object as a
   * parameter. It is up to the client to close the connection object.
   * 
   * <b>Precondition: </b>Assumes an MdType exists in the database with the
   * given oid.
   * 
   * @param mdTypeId
   * @param conn
   */
  byte[] getMdTypeBaseClass(String mdTypeId, Connection conn);

  /**
   * This is a special method used get the MdType base source from the database.
   * This method is used when a transaction is rolled back to restore the stub
   * source on the file system from the database. It is used only within the
   * TransactionManagement aspect, hence it takes a JDBC connection object as a
   * parameter. It is up to the client to close the connection object.
   * 
   * <b>Precondition: </b>Assumes an MdType exists in the database with the
   * given oid.
   * 
   * @param mdTypeId
   * @param conn
   */
  String getMdTypeBaseSource(String mdTypeId, Connection conn);

  /**
   * This is a special method used get the MdType DTO class from the database.
   * This method is used when a transaction is rolled back to restore the base
   * class to the file system from the database. It is used only within the
   * TransactionManagement aspect, hence it takes a JDBC connection object as a
   * parameter. It is up to the client to close the connection object.
   * 
   * <b>Precondition: </b>Assumes an MdType exists in the database with the
   * given oid.
   * 
   * @param mdTypeId
   * @param conn
   */
  byte[] getMdTypeDTOclass(String mdTypeId, Connection conn);

  /**
   * This is a special method used get the MdType DTO source from the database.
   * This method is used when a transaction is rolled back to restore the stub
   * source on the file system from the database. It is used only within the
   * TransactionManagement aspect, hence it takes a JDBC connection object as a
   * parameter. It is up to the client to close the connection object.
   * 
   * <b>Precondition: </b>Assumes an MdType exists in the database with the
   * given oid.
   * 
   * @param mdTypeId
   * @param conn
   */
  String getMdTypeDTOsource(String mdTypeId, Connection conn);

  /**
   * This is a special method used get the MdClass dto stub class from the
   * database. This method is used when a transaction is rolled back to restore
   * the dto stub class to the file system from the database. It is used only
   * within the TransactionManagement aspect, hence it takes a JDBC connection
   * object as a parameter. It is up to the client to close the connection
   * object.
   * 
   * <b>Precondition: </b>Assumes an MdClass exists in the database with the
   * given oid.
   * 
   * @param mdClassId
   * @param conn
   */
  byte[] getMdClassDTOStubClass(String mdClassId, Connection conn);

  /**
   * This is a special method used get the MdClass dto stub source from the
   * database. This method is used when a transaction is rolled back to restore
   * the dto stub source on the file system from the database. It is used only
   * within the TransactionManagement aspect, hence it takes a JDBC connection
   * object as a parameter. It is up to the client to close the connection
   * object.
   * 
   * <b>Precondition: </b>Assumes an MdClass exists in the database with the
   * given oid.
   * 
   * @param mdClassId
   * @param conn
   */
  String getMdClassDTOStubSource(String mdClassId, Connection conn);

  /**
   * This is a special method used get the MdClass stub class from the database.
   * This method is used when a transaction is rolled back to restore the stub
   * class to the file system from the database. It is used only within the
   * TransactionManagement aspect, hence it takes a JDBC connection object as a
   * parameter. It is up to the client to close the connection object.
   * 
   * <b>Precondition: </b>Assumes an MdClass exists in the database with the
   * given oid.
   * 
   * @param mdClassId
   * @param conn
   */
  byte[] getMdClassStubClass(String mdClassId, Connection conn);

  /**
   * This is a special method used get the MdClass stub source from the
   * database. This method is used when a transaction is rolled back to restore
   * the stub source on the file system from the database. It is used only
   * within the TransactionManagement aspect, hence it takes a JDBC connection
   * object as a parameter. It is up to the client to close the connection
   * object.
   * 
   * <b>Precondition: </b>Assumes an MdClass exists in the database with the
   * given oid.
   * 
   * @param mdClassId
   * @param conn
   */
  String getMdClassStubSource(String mdClassId, Connection conn);

  /**
   * This is a special method used get the MdEntity query API class from the
   * database. This method is used when a transaction is rolled back to restore
   * the query API class to the file system from the database. It is used only
   * within the TransactionManagement aspect, hence it takes a JDBC connection
   * object as a parameter. It is up to the client to close the connection
   * object.
   * 
   * <b>Precondition: </b>Assumes an MdEntity exists in the database with the
   * given oid.
   * 
   * @param mdEntityId
   * @param conn
   */
  byte[] getMdEntityQueryAPIclass(String mdEntityId, Connection conn);

  /**
   * This is a special method used get the MdEntity query DTO class from the
   * database. This method is used when a transaction is rolled back to restore
   * the query DTO class to the file system from the database. It is used only
   * within the TransactionManagement aspect, hence it takes a JDBC connection
   * object as a parameter. It is up to the client to close the connection
   * object.
   * 
   * <b>Precondition: </b>Assumes an MdEntity exists in the database with the
   * given oid.
   * 
   * @param mdEntityId
   * @param conn
   */
  byte[] getEntityQueryDTOclass(String mdEntityId, Connection conn);

  /**
   * This is a special method used get the MdView query DTO class from the
   * database. This method is used when a transaction is rolled back to restore
   * the query DTO class to the file system from the database. It is used only
   * within the TransactionManagement aspect, hence it takes a JDBC connection
   * object as a parameter. It is up to the client to close the connection
   * object.
   * 
   * <b>Precondition: </b>Assumes an MdView exists in the database with the
   * given oid.
   * 
   * @param mdEntityId
   * @param conn
   */
  byte[] getViewQueryDTOclass(String mdEntityId, Connection conn);

  /**
   * This is a special method used get the MdView query base source from the
   * database. This method is used when a transaction is rolled back to restore
   * the query source on the file system from the database. It is used only
   * within the TransactionManagement aspect, hence it takes a JDBC connection
   * object as a parameter. It is up to the client to close the connection
   * object.
   * 
   * <b>Precondition: </b>Assumes an MdView exists in the database with the
   * given oid.
   * 
   * @param mdViewId
   * @param conn
   */
  String getMdViewBaseQuerySource(String mdViewId, Connection conn);

  /**
   * This is a special method used get the MdView base query class from the
   * database. This method is used when a transaction is rolled back to restore
   * the query class to the file system from the database. It is used only
   * within the TransactionManagement aspect, hence it takes a JDBC connection
   * object as a parameter. It is up to the client to close the connection
   * object.
   * 
   * <b>Precondition: </b>Assumes an MdView exists in the database with the
   * given oid.
   * 
   * @param mdViewId
   * @param conn
   */
  byte[] getMdViewBaseQueryClass(String mdViewId, Connection conn);

  /**
   * This is a special method used get the MdView query stub source from the
   * database. This method is used when a transaction is rolled back to restore
   * the query source on the file system from the database. It is used only
   * within the TransactionManagement aspect, hence it takes a JDBC connection
   * object as a parameter. It is up to the client to close the connection
   * object.
   * 
   * <b>Precondition: </b>Assumes an MdView exists in the database with the
   * given oid.
   * 
   * @param mdViewId
   * @param conn
   */
  String getMdViewStubQuerySource(String mdViewId, Connection conn);

  /**
   * This is a special method used get the MdView query stub class from the
   * database. This method is used when a transaction is rolled back to restore
   * the query class to the file system from the database. It is used only
   * within the TransactionManagement aspect, hence it takes a JDBC connection
   * object as a parameter. It is up to the client to close the connection
   * object.
   * 
   * <b>Precondition: </b>Assumes an MdView exists in the database with the
   * given oid.
   * 
   * @param mdViewId
   * @param conn
   */
  byte[] getMdViewStubQueryClass(String mdViewId, Connection conn);

  /**
   * This is a special method used get the MdEntity query API source from the
   * database. This method is used when a transaction is rolled back to restore
   * the query API source on the file system from the database. It is used only
   * within the TransactionManagement aspect, hence it takes a JDBC connection
   * object as a parameter. It is up to the client to close the connection
   * object.
   * 
   * <b>Precondition: </b>Assumes an MdEntity exists in the database with the
   * given oid.
   * 
   * @param mdEntityId
   * @param conn
   */
  String getMdEntityQueryAPIsource(String mdEntityId, Connection conn);

  /**
   * This is a special method used get the MdEntity query DTO source from the
   * database. This method is used when a transaction is rolled back to restore
   * the query DTO source on the file system from the database. It is used only
   * within the TransactionManagement aspect, hence it takes a JDBC connection
   * object as a parameter. It is up to the client to close the connection
   * object.
   * 
   * <b>Precondition: </b>Assumes an MdEntity exists in the database with the
   * given oid.
   * 
   * @param mdEntityId
   * @param conn
   */
  String getEntityQueryDTOsource(String mdEntityId, Connection conn);

  /**
   * This is a special method used get the MdView query DTO source from the
   * database. This method is used when a transaction is rolled back to restore
   * the query DTO source on the file system from the database. It is used only
   * within the TransactionManagement aspect, hence it takes a JDBC connection
   * object as a parameter. It is up to the client to close the connection
   * object.
   * 
   * <b>Precondition: </b>Assumes an MdView exists in the database with the
   * given oid.
   * 
   * @param mdViewId
   * @param conn
   */
  String getViewQueryDTOsource(String mdViewId, Connection conn);

  /**
   * This is a special method used get the source from the database. This method
   * is used when a transaction is rolled back to restore the source on the file
   * system from the database. It is used only within the TransactionManagement
   * aspect, hence it takes a JDBC connection object as a parameter. It is up to
   * the client to close the connection object.
   * 
   * <b>Precondition: </b>Assumes an MdType exists in the database with the
   * given oid.
   * 
   * @param mdTypeId
   * @param conn
   * @param table
   * @param columnName
   */
  String getSourceField(String mdTypeId, Connection conn, String table, String columnName);

  /**
   * Returns the value of the database column that is used to cache enumeration
   * mappings for an attribute.
   * 
   * @param tableName
   *          name of the table
   * @param columnName
   *          name of the attribute
   * @param entityId
   *          oid of an entity
   * @return value of the database column that is used to cache enumeration
   *         mappings for an attribute.
   */
  String getEnumCacheFieldInTable(String tableName, String columnName, String entityId);

  /**
   * Returns the SQL that inserts a mapping in the given enumeration table
   * between the given set oid and the given enumeration item oid.
   * 
   * @param enumTableName
   * @param setOid
   * @param enumItemID
   */
  String buildAddItemStatement(String enumTableName, String setOid, String enumItemID);

  /**
   * Returns the SQL that updates an enum item oid with the provided new enum
   * item oid.
   * 
   * @param enumTableName
   * @param oldEnumItemId
   * @param newEnumItemId
   */
  String buildUpdateEnumItemStatement(String enumTableName, String oldEnumItemId, String newEnumItemId);

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
  StringBuffer buildRowRangeRestriction(StringBuffer sqlStmt, int limit, int skip, String selectClauseAttributes, String orderByClause);

  /**
   * Returns the ids of the enumeration items that are mapped to the given
   * setOid.
   */
  Set<String> getEnumItemIds(String enumTableName, String setOid);

  /**
   * Removes the enumeration item from the enumeration mapping table.
   * 
   * @param enumTableName
   * @param enumItemID
   */
  void deleteEumerationItemFromLinkTable(String enumTableName, String enumItemID);

  /**
   * Deletes all instances of the setOid from the given enumeration mapping
   * table.
   * 
   * @param enumTableName
   * @param setOid
   */
  void deleteSetIdFromLinkTable(String enumTableName, String setOid);

  /**
   * Deletes all mapping instances for a particular attribute.
   * 
   * @param enumTableName
   * @param tableName
   * @param columnName
   */
  void deleteAllEnumAttributeInstances(String enumTableName, String tableName, String columnName);

  /**
   * Gets a list of all enumerated types in the system.
   * 
   * @return list of all enumerated types in the system.
   */
  List<String> getAllEnumTypes();

  /**
   * Gets a list of all tables used by the application, including runway
   * metadata.
   * 
   * @return list of all tables used by the application, including runway
   *         metadata.
   */
  List<String> getAllApplicationTables();

  /**
   * Gets the schema/namespace used by the application
   * 
   * @return schema/namespace used by the application
   */
  String getApplicationNamespace();

  /**
   * Gets a list of all entity tables.
   * 
   * @return list of all entity tables.
   */
  List<String> getAllEntityTables();

  /**
   * Gets a list of all enumeration tables.
   * 
   * @return list of all enumeration tables.
   */
  List<String> getAllEnumerationTables();

  /**
   * Returns the number of distinct child instances for a given parent of the
   * given relationship type.
   * 
   * @param parent_oid
   * @param relationshipTableName
   * @return
   */
  long getChildCountForParent(String parent_oid, String relationshipTableName);

  /**
   * Returns the number of distinct parent instances for a given child of the
   * given relationship type.
   * 
   * @param child_oid
   * @param relationshipTableName
   * @return
   */
  long getParentCountForChild(String child_oid, String relationshipTableName);

  /**
   * Returns the number of instances of Relationships of the given type. This
   * method queries the database. More specifically, it queries the table that
   * stores instances of this relationship and counts the records.
   * 
   * <br/>
   * <b>Precondition:</b> mdRelationship != null
   * 
   * @param mdRelationship
   *          defines the relationship type
   * @return number of instances of Relationships of the given className
   */
  int numberOfRelInstances(MdRelationshipDAOIF mdRelationship);

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
  String backup(List<String> tableNames, String backupFileLocation, String backupFileRootName, PrintStream out, PrintStream errOut, boolean dropSchema);

  /**
   * Backs up the install to a file name in the given location.
   * 
   * @param namespace
   *          DB namespace used by the application
   * @param backupFileLocation
   *          location of the backup file to generate.
   * @param backupFileRootName
   *          root of the file name (minus the file extension).
   * @param dropSchema
   *          true if backup should include commands to drop the schema
   */
  String backup(String namespace, String backupFileLocation, String backupFileRootName, PrintStream out, PrintStream errOut, boolean dropSchema);

  /**
   * Imports the given SQL file into the database
   * 
   * @param restoreSQLFile
   * @param printStream
   */
  void importFromSQL(String restoreSQLFile, PrintStream out, PrintStream errOut);

  /**
   * Drops all of the tables given in the list. This method does not use the
   * command pattern.
   * 
   * @param tableNames
   *          list of tables to drop.
   */
  void dropTables(List<String> tableNames);

  /**
   * Drops all of the tables given in the list. This method does not use the
   * command pattern.
   * 
   * @param tableNames
   *          list of tables to drop.
   */
  void cascadeDropTables(List<String> tableNames);

  /**
   * Drops all of the views given in the list. This method does not use the
   * command pattern.
   * 
   * @param viewNames
   *          list of views to drop.
   */
  void dropViews(List<String> viewNames);

  /**
   * Creates a geometry column in the database.
   * 
   * @param tableName
   * @param columnName
   * @param srid
   * @param geometryType
   * @param dimension
   */
  void addGeometryColumn(String tableName, String columnName, int srid, String geometryType, int dimension);

  /**
   * Drops a geometry column in the database.
   * 
   * @param tableName
   * @param columnName
   */
  void dropGeometryColumn(String tableName, String columnName);

  /**
   * Returns the database specific union operator.
   */
  String getUnionOperator();

  /**
   * Returns the database specific union all operator.
   */
  String getUnionAllOperator();

  /**
   * Returns the database specific minus operator.
   */
  String getMinusOperator();

  /**
   * Returns the database specific intersect operator.
   */
  String getIntersectOperator();

  /**
   * @return <code>true</code> if the database allows nonrequired columns to
   *         enforce uniqueness
   */
  boolean allowsUniqueNonRequiredColumns();

  /**
   * @param value
   * @param attributeIF
   * @return
   */
  void validateClobLength(String value, AttributeIF attributeIF);

  List<Changelog> getChangelogEntries();

  List<String> getReferencingViews(MdElementDAOIF mdElement);

  int getMaxColumnSize();

  /**
   * Casts the given sql to a decimal.
   * 
   * Note, this has only been tested against Postgres
   * 
   * @param sql
   * @return Casts the given sql to a decimal.
   */
  String castToDecimal(String sql);

  String generateRootId(MdTypeDAO mdTypeDAO);

  /**
   * Converts a database specific type into its generic representation.
   * 
   * @param value
   * @return
   */
  Object convertDatabaseValue(Object value);

}