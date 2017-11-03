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
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.runwaysdk.constants.DatabaseProperties;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdAttributeBlobInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeClobInfo;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.MdAttributeDateTimeInfo;
import com.runwaysdk.constants.MdAttributeDecimalInfo;
import com.runwaysdk.constants.MdAttributeDimensionInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeFileInfo;
import com.runwaysdk.constants.MdAttributeFloatInfo;
import com.runwaysdk.constants.MdAttributeHashInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalTextInfo;
import com.runwaysdk.constants.MdAttributeLongInfo;
import com.runwaysdk.constants.MdAttributeMultiReferenceInfo;
import com.runwaysdk.constants.MdAttributeMultiTermInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.constants.MdAttributeSymmetricInfo;
import com.runwaysdk.constants.MdAttributeTermInfo;
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.constants.MdAttributeTimeInfo;
import com.runwaysdk.constants.RelationshipInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.ElementDAOIF;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDimensionDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdElementDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.MdViewDAOIF;
import com.runwaysdk.dataaccess.MetadataDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.attributes.AttributeLengthCharacterException;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeEnumeration;
import com.runwaysdk.dataaccess.attributes.entity.AttributeFactory;
import com.runwaysdk.dataaccess.attributes.entity.AttributeStruct;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.cache.HardCodedMetadataIterator;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.database.Changelog;
import com.runwaysdk.dataaccess.database.DDLCommand;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.DatabaseException;
import com.runwaysdk.dataaccess.database.DefaultMdEntityInfo;
import com.runwaysdk.dataaccess.database.EntityDAOFactory;
import com.runwaysdk.dataaccess.database.ServerIDGenerator;
import com.runwaysdk.dataaccess.database.StructDAOFactory;
import com.runwaysdk.dataaccess.metadata.ForbiddenMethodException;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdEntityDAO;
import com.runwaysdk.util.IdParser;

/**
 * The abstract root of the Database class family. Contains general SQL syntax
 * for interaction with the database. Concrete implementations will override
 * methods in this class to allow for specific databases.
 * 
 * @author Eric
 * @version $Revision 1.0 $
 * @since
 */
public abstract class AbstractDatabase
{
  public static final String  UNIQUE_ATTRIBUTE_GROUP_INDEX_PREFIX = "_group_unique";

  public static int           MAX_LENGTH                          = 65535;

  protected DataSource        dataSource;

  protected DataSource        rootDataSource;

  protected String            logDirectory;

  private final ReentrantLock connlock;

  /**
   */
  protected AbstractDatabase()
  {
    this.connlock = new ReentrantLock();
    this.logDirectory = LocalProperties.getLogDirectory();

    try
    {
      Context initContext = new InitialContext();
      Context envContext = (Context) initContext.lookup("java:/comp/env");
      if (envContext != null)
      {
        dataSource = (DataSource) envContext.lookup(DatabaseProperties.getJNDIDataSource());
      }
    }
    catch (NamingException e)
    {
      // if a jndiDataSource has not been configured with the app server, the
      // subclasses
      // will initialize the datasource.
    }
  }

  /**
   * Installs the runway core. This entails creating a new database, creating a
   * user for the runway to log in with, and setting any necessary permissions.
   */
  public abstract void initialSetup(String rootUser, String rootPass, String rootDb);

  /**
   * Drop the database.
   */
  public abstract void dropDb();

  /**
   * Creates the database.
   */
  public abstract void createDb(String rootDb);

  /**
   * Drops the database user.
   */
  public abstract void dropUser();

  /**
   * Creates the database user.
   */
  public abstract void createUser();
  
  /**
   * Closes all active connections to the database and cleans up resources.
   */
  public abstract void close();

  /**
   * Returns a savepoint.
   * 
   * @return a savepoint.
   */
  public Savepoint setSavepoint()
  {
    try
    {
      Connection conn = Database.getConnection();
      return conn.setSavepoint();
    }
    catch (SQLException ex)
    {
      throw new DatabaseException(ex);
    }
  }

  /**
   * Rolls back the savepoint.
   * 
   * @param savepoint
   *          to rollback.
   */
  public void rollbackSavepoint(Savepoint savepoint)
  {
    try
    {
      Connection conn = Database.getConnection();
      conn.rollback(savepoint);
    }
    catch (SQLException ex)
    {
      throw new DatabaseException(ex);
    }
  }

  /**
   * Returns a DDL connection savepoint.
   * 
   * @return a DDL connection savepoint.
   */
  public Savepoint setDDLsavepoint()
  {
    try
    {
      return Database.getDDLConnection().setSavepoint();
    }
    catch (SQLException ex)
    {
      throw new DatabaseException(ex);
    }
  }

  /**
   * Rolls back the DDL connection savepoint.
   * 
   * @param DDL
   *          connection savepoint to rollback.
   */
  public void rollbackDDLsavepoint(Savepoint savepoint)
  {
    try
    {
      Database.getDDLConnection().rollback(savepoint);
    }
    catch (SQLException ex)
    {
      throw new DatabaseException(ex);
    }
  }

  /**
   * Releases the savepoint.
   * 
   * @param savepoint
   *          to release.
   */
  public void releaseSavepoint(Savepoint savepoint)
  {
    try
    {
      Database.getConnection().releaseSavepoint(savepoint);
    }
    catch (SQLException ex)
    {
      throw new DatabaseException(ex);
    }
  }

  /**
   * Releases the savepoint.
   * 
   * @param savepoint
   *          to release.
   */
  public void releaseDDLsavepoint(Savepoint savepoint)
  {
    try
    {
      Database.getDDLConnection().releaseSavepoint(savepoint);
    }
    catch (SQLException ex)
    {
      throw new DatabaseException(ex);
    }
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
  public Connection getConnection()
  {
    this.connlock.lock();
    try
    {
      Connection conn = null;
      PreparedStatement statement = null;

      try
      {
        /*
         * java.util.Date startTime = new java.util.Date();
         */
        conn = this.dataSource.getConnection();
        conn.setAutoCommit(false);
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

        statement = conn.prepareStatement("SET CLIENT_ENCODING TO 'UTF8'");
        statement.execute();
        /*
         * java.util.Date endTime = new java.util.Date(); long totalTime =
         * endTime.getTime() - startTime.getTime();
         * System.out.println("\n----------------------\nTotal Connection Time: "
         * + totalTime+"\n----------------------");
         */
      }
      catch (SQLException ex)
      {
        throw new DatabaseException(ex);
      }
      return conn;
    }
    finally
    {
      this.connlock.unlock();
    }
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
    this.connlock.lock();
    try
    {
      Connection conn = null;
      PreparedStatement statement = null;
      try
      {
        conn = this.dataSource.getConnection();
        conn.setAutoCommit(false);

        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

        statement = conn.prepareStatement("SET CLIENT_ENCODING TO 'UTF8'");
        statement.execute();
      }
      catch (SQLException ex)
      {
        throw new DatabaseException(ex);
      }
      return conn;
    }
    finally
    {
      this.connlock.unlock();
    }
  }

  /**
   * Returns true if the database implementation shares the same connection
   * object for DDL and DML operations, false otherwise.
   * 
   * @return true if the database implementation shares the same connection
   *         object for DDL and DML operations, false otherwise.
   */
  public boolean sharesDDLandDMLconnection()
  {
    return false;
  }

  /**
   * All connections managed by the framework need to be closed using this
   * method. This is a hook method for the session management aspect.
   * 
   * @param conn
   * @throws SQLException
   */
  public void closeConnection(Connection conn) throws SQLException
  {
    conn.close();
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
  public LinkedHashMap<String, String> getSuperEntityTypes(String type)
  {
    String typeName = EntityDAOFactory.getTypeNameFromType(type);
    String packageName = EntityDAOFactory.getPackageFromType(type);

    LinkedHashMap<String, String> superTypeMap = new LinkedHashMap<String, String>();

    String subSqlSelect = "SELECT " + RelationshipTypes.CLASS_INHERITANCE.getTableName() + "." + RelationshipDAOIF.PARENT_ID_COLUMN + ", " + RelationshipTypes.CLASS_INHERITANCE.getTableName() + "." + RelationshipDAOIF.CHILD_ID_COLUMN + ", " + MdTypeDAOIF.TABLE + "." + MdTypeDAOIF.PACKAGE_NAME_COLUMN + ", " + MdTypeDAOIF.TABLE + "." + MdTypeDAOIF.TYPE_NAME_COLUMN + "\n" + "  FROM " + MdTypeDAOIF.TABLE + ", " + RelationshipTypes.CLASS_INHERITANCE.getTableName() + " " + " WHERE " + MdTypeDAOIF.TABLE + "." + EntityDAOIF.ID_COLUMN + " = " + RelationshipTypes.CLASS_INHERITANCE.getTableName() + "." + RelationshipDAOIF.PARENT_ID_COLUMN;

    String sqlSelect = "SELECT parent." + RelationshipDAOIF.PARENT_ID_COLUMN + ", " + "parent." + MdTypeDAOIF.PACKAGE_NAME_COLUMN + ", " + "parent." + MdTypeDAOIF.TYPE_NAME_COLUMN + ",\n" +
    // Table name of the child
        MdEntityDAOIF.TABLE + "." + MdEntityDAOIF.TABLE_NAME_COLUMN + "\n" + "  FROM " + MdEntityDAOIF.TABLE + ", " + MdTypeDAOIF.TABLE + " LEFT JOIN " + "(" + subSqlSelect + ") parent " + " ON " + MdTypeDAOIF.TABLE + "." + EntityDAOIF.ID_COLUMN + " = parent." + RelationshipDAOIF.CHILD_ID_COLUMN + "\n" + " WHERE " + MdTypeDAOIF.TABLE + "." + EntityDAOIF.ID_COLUMN + " = " + MdEntityDAOIF.TABLE + "." + EntityDAOIF.ID_COLUMN + "\n" + " AND " + MdTypeDAOIF.TABLE + "." + MdTypeDAOIF.TYPE_NAME_COLUMN + " = '" + typeName + "'\n" + " AND " + MdTypeDAOIF.TABLE + "." + MdTypeDAOIF.PACKAGE_NAME_COLUMN + " = '" + packageName + "'\n";

    ResultSet resultSet = this.query(sqlSelect);

    try
    {
      int loopCount = 0;
      while (resultSet.next())
      {
        if (loopCount == 0)
        {
          // String returnPackageName =
          // resultSet.getString(MdTypeDAOIF.PACKAGE_NAME_COLUMN);
          // String returnTypeName =
          // resultSet.getString(MdTypeDAOIF.TYPE_NAME_COLUMN);
          // String tableName =
          // resultSet.getString(MdEntityDAOIF.TABLE_NAME_COLUMN);
          // String returnType = EntityDAOFactory.buildType(returnPackageName,
          // returnTypeName);
          // superTypeMap.put(returnType, tableName);

          String tableName = resultSet.getString(MdEntityDAOIF.TABLE_NAME_COLUMN);
          superTypeMap.put(type, tableName);
        }

        loopCount++;

        if (resultSet.getString(RelationshipDAOIF.PARENT_ID_COLUMN) != null)
        {
          String parentPackageName = resultSet.getString(MdTypeDAOIF.PACKAGE_NAME_COLUMN);
          String parentTypeName = resultSet.getString(MdTypeDAOIF.TYPE_NAME_COLUMN);
          String parentType = EntityDAOFactory.buildType(parentPackageName, parentTypeName);

          superTypeMap.putAll(getSuperEntityTypes(parentType));
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

    return superTypeMap;
  }
  
  public void dropAll()
  {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the id to the MdEntity that defines the given type. given ID.
   * 
   * @param type
   * @return id to the MdEntity that defines the given type.
   */
  public String getMdEntityId(String type)
  {
    List<String> mdEntityFields = new LinkedList<String>();
    mdEntityFields.add(MetadataDAOIF.TABLE + "." + EntityDAOIF.ID_COLUMN);

    List<String> mdEntityTable = new LinkedList<String>();
    mdEntityTable.add(MdTypeDAOIF.TABLE);
    mdEntityTable.add(MetadataDAOIF.TABLE);

    String typeName = EntityDAOFactory.getTypeNameFromType(type);
    String packageName = EntityDAOFactory.getPackageFromType(type);

    List<String> mdEntityConditions = new LinkedList<String>();
    mdEntityConditions.add(MdTypeDAOIF.TABLE + "." + MdTypeDAOIF.TYPE_NAME_COLUMN + " = '" + typeName + "'");
    mdEntityConditions.add(MdTypeDAOIF.TABLE + "." + MdTypeDAOIF.PACKAGE_NAME_COLUMN + " = '" + packageName + "'");
    mdEntityConditions.add(MetadataDAOIF.TABLE + "." + EntityDAOIF.ID_COLUMN + " = " + MdTypeDAOIF.TABLE + "." + EntityDAOIF.ID_COLUMN);

    ResultSet resultSet = this.query(this.selectClause(mdEntityFields, mdEntityTable, mdEntityConditions));

    String id = "";

    try
    {
      if (resultSet.next())
      {
        id = resultSet.getString(EntityDAOIF.ID_COLUMN);
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

    return id;
  }

  /**
   * Returns the name of the table in the database used to store instances of
   * the given class.
   * 
   * @param type
   * @return name of the table in the database used to store instances of the
   *         given class.
   */
  public String getMdEntityTableName(String type)
  {
    String className = BusinessDAOFactory.getClassNameFromType(type);
    String packageName = BusinessDAOFactory.getPackageFromType(type);

    List<String> mdBusinessFields = new LinkedList<String>();
    mdBusinessFields.add(MdEntityDAOIF.TABLE_NAME_COLUMN);

    List<String> mdBusinessTable = new LinkedList<String>();
    mdBusinessTable.add(MdTypeDAOIF.TABLE);
    mdBusinessTable.add(MdEntityDAOIF.TABLE);

    List<String> mdBusiniessConditions = new LinkedList<String>();
    mdBusiniessConditions.add(MdTypeDAOIF.PACKAGE_NAME_COLUMN + " = '" + packageName + "'");
    mdBusiniessConditions.add(MdTypeDAOIF.TYPE_NAME_COLUMN + " = '" + className + "'");
    mdBusiniessConditions.add(MdTypeDAOIF.TABLE + "." + MdTypeDAOIF.ID_COLUMN + " = " + MdEntityDAOIF.TABLE + "." + MdElementDAOIF.ID_COLUMN);

    ResultSet resultSet = this.query(this.selectClause(mdBusinessFields, mdBusinessTable, mdBusiniessConditions));

    String tableName = "";

    try
    {
      if (resultSet.next())
      {
        tableName = resultSet.getString(MdElementDAOIF.TABLE_NAME_COLUMN);
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
   * Returns the type of the object with the given id.
   * 
   * @param instanceId
   * @return the type of the object with the given id.
   */
  public String getTypeFromInstanceId(String instanceId)
  {
    String mdTypeRootId = IdParser.parseMdTypeRootIdFromId(instanceId);

    List<String> mdTypeFields = new LinkedList<String>();
    mdTypeFields.add(MdTypeDAOIF.TYPE_NAME_COLUMN);
    mdTypeFields.add(MdTypeDAOIF.PACKAGE_NAME_COLUMN);

    List<String> mdTypeTable = new LinkedList<String>();
    mdTypeTable.add(MdTypeDAOIF.TABLE);

    List<String> mdBusiniessConditions = new LinkedList<String>();
    mdBusiniessConditions.add(MdTypeDAOIF.ROOT_ID_COLUMN + " = '" + mdTypeRootId + "'");

    ResultSet resultSet = this.query(this.selectClause(mdTypeFields, mdTypeTable, mdBusiniessConditions));

    String typeName = "";
    String packagName = "";

    try
    {
      if (resultSet.next())
      {
        typeName = resultSet.getString(MdTypeDAOIF.TYPE_NAME_COLUMN);
        packagName = resultSet.getString(MdTypeDAOIF.PACKAGE_NAME_COLUMN);
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

    return EntityDAOFactory.buildType(packagName, typeName);
  }

  /**
   * Returns the type of the object with the given id.
   * 
   * @param instanceId
   * @return the type of the object with the given id.
   */
  public Map<String, String> getTypeAndTableFromInstanceId(String instanceId)
  {
    Map<String, String> returnMap = new HashMap<String, String>(1);

    String mdTypeRootId = IdParser.parseMdTypeRootIdFromId(instanceId);

    List<String> mdTypeFields = new LinkedList<String>();
    mdTypeFields.add(MdTypeDAOIF.TYPE_NAME_COLUMN);
    mdTypeFields.add(MdTypeDAOIF.PACKAGE_NAME_COLUMN);
    mdTypeFields.add(MdEntityDAOIF.TABLE_NAME_COLUMN);

    List<String> mdTypeTable = new LinkedList<String>();
    mdTypeTable.add(MdTypeDAOIF.TABLE);
    mdTypeTable.add(MdEntityDAOIF.TABLE);

    List<String> mdBusiniessConditions = new LinkedList<String>();
    mdBusiniessConditions.add(MdTypeDAOIF.ROOT_ID_COLUMN + " = '" + mdTypeRootId + "'");
    mdBusiniessConditions.add(MdTypeDAOIF.TABLE + "." + MdTypeDAOIF.ID_COLUMN + " = " + MdEntityDAOIF.TABLE + "." + MdEntityDAOIF.ID_COLUMN);

    ResultSet resultSet = this.query(this.selectClause(mdTypeFields, mdTypeTable, mdBusiniessConditions));

    String typeName = "";
    String packagName = "";
    String tableName = "";

    try
    {
      if (resultSet.next())
      {
        typeName = resultSet.getString(MdTypeDAOIF.TYPE_NAME_COLUMN);
        packagName = resultSet.getString(MdTypeDAOIF.PACKAGE_NAME_COLUMN);
        tableName = resultSet.getString(MdEntityDAOIF.TABLE_NAME_COLUMN);
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

    String type = EntityDAOFactory.buildType(packagName, typeName);
    returnMap.put(type, tableName);

    return returnMap;
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
  public List<String> getConcreteSubClasses(String mdEntityId)
  {
    List<String> subClassTypeList = new LinkedList<String>();

    String sqlSelect = "SELECT " + RelationshipTypes.CLASS_INHERITANCE.getTableName() + "." + RelationshipDAOIF.CHILD_ID_COLUMN + ", " + MdTypeDAOIF.TABLE + "." + MdTypeDAOIF.PACKAGE_NAME_COLUMN + ", " + MdTypeDAOIF.TABLE + "." + MdTypeDAOIF.TYPE_NAME_COLUMN + ", " + MdElementDAOIF.TABLE + "." + MdElementDAOIF.ABSTRACT_COLUMN + "\n" + "  FROM " + MdTypeDAOIF.TABLE + " LEFT JOIN " + RelationshipTypes.CLASS_INHERITANCE.getTableName() + " ON " + MdTypeDAOIF.TABLE + "." + EntityDAOIF.ID_COLUMN + " = " + RelationshipTypes.CLASS_INHERITANCE.getTableName() + "." + RelationshipDAOIF.PARENT_ID_COLUMN + ", " + MdElementDAOIF.TABLE + "\n " + " WHERE " + MdTypeDAOIF.TABLE + "." + EntityDAOIF.ID_COLUMN + " = " + MdElementDAOIF.TABLE + "." + EntityDAOIF.ID_COLUMN + "\n " + "   AND " + MdTypeDAOIF.TABLE
        + "." + EntityDAOIF.ID_COLUMN + " = '" + mdEntityId + "'";

    ResultSet resultSet = this.query(sqlSelect);

    try
    {
      int loopCount = 0;
      while (resultSet.next())
      {
        if (loopCount == 0)
        {
          String abstractClass = resultSet.getString(MdElementDAOIF.ABSTRACT_COLUMN);

          // Only add this class to the list if it is not an abstract class
          if (abstractClass.trim().equalsIgnoreCase(MdAttributeBooleanDAOIF.DB_FALSE))
          {
            String packageName = resultSet.getString(MdTypeDAOIF.PACKAGE_NAME_COLUMN);
            String typeName = resultSet.getString(MdTypeDAOIF.TYPE_NAME_COLUMN);
            String type = EntityDAOFactory.buildType(packageName, typeName);
            subClassTypeList.add(type);
          }
        }

        if (resultSet.getString(RelationshipDAOIF.CHILD_ID_COLUMN) != null)
        {
          String childId = resultSet.getString(RelationshipDAOIF.CHILD_ID_COLUMN);

          subClassTypeList.addAll(getConcreteSubClasses(childId));
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

    return subClassTypeList;
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
  public Map<String, Attribute> getAttributesForHardcodedMetadataObject(String id, String type, String tableName, Map<String, String> relationshipAttributesHackMap, boolean rootClass)
  {
    Map<String, Attribute> attributeMap = new HashMap<String, Attribute>();

    ResultSet resultSet = this.selectFromWhere("*", tableName, EntityDAOIF.ID_COLUMN + "='" + id + "'");

    try
    {
      if (!resultSet.next())
      {
        MdEntityDAOIF mdEntity = MdEntityDAO.getMdEntityDAO(type);
        String error = "No results found in table [" + tableName + "] with id [" + id + ']';
        throw new DataNotFoundException(error, mdEntity);
      }

      if (relationshipAttributesHackMap != null)
      {
        relationshipAttributesHackMap.put(RelationshipInfo.PARENT_ID, resultSet.getString(RelationshipDAOIF.PARENT_ID_COLUMN).toString());
        relationshipAttributesHackMap.put(RelationshipInfo.CHILD_ID, resultSet.getString(RelationshipDAOIF.CHILD_ID_COLUMN).toString());
      }

      Map<String, Map<String, String>> mdAttributeInfoMap = DefaultMdEntityInfo.getAttributeMapForType(type);

      // Iterate over the fields
      for (String columnName : mdAttributeInfoMap.keySet())
      {
        if ( ( !columnName.equals(EntityDAOIF.ID_COLUMN) || type.equals(ElementInfo.CLASS) || rootClass ))
        {
          String attributeName = DefaultMdEntityInfo.getAttributeName(type, columnName);

          String attributeType = DefaultMdEntityInfo.getAttributeType(type, columnName);

          Object value = AttributeFactory.getColumnValueFromRow(resultSet, columnName, attributeType, false);

          Map<String, String> attributePropertyMap = mdAttributeInfoMap.get(columnName);
          String attributeTypeName = attributePropertyMap.get(EntityInfo.TYPE);
          String mdAttributeKey = attributePropertyMap.get(EntityInfo.KEY);
          Attribute attribute = AttributeFactory.createAttribute(mdAttributeKey, attributeTypeName, attributeName, type, value);

          if (attribute instanceof AttributeEnumeration)
          {
            AttributeEnumeration attributeEnumeration = (AttributeEnumeration) attribute;
            String cacheColumnName = MdAttributeEnumerationDAO.getCacheDbColumnName(columnName);
            String cachedEnumerationMappings = "";

            cachedEnumerationMappings = resultSet.getString(cacheColumnName);

            attributeEnumeration.initEnumMappingCache(cachedEnumerationMappings);
          }
          else if (attribute instanceof AttributeStruct)
          {
            String structId = attribute.getValue();

            if (!structId.trim().equals(""))
            {
              Map<String, String> structMap = getTypeAndTableFromInstanceId(structId);

              String structType = "";
              String structTable = "";

              // The last item is the root type.
              for (String someStructType : structMap.keySet())
              {
                structType = someStructType;
                structTable = structMap.get(someStructType);
              }

              Map<String, Attribute> structAttributeMap = getAttributesForHardcodedMetadataObject(structId, structType, structTable, null, true);

              StructDAO structDAO = StructDAOFactory.factoryMethod(structAttributeMap, structType);

              AttributeStruct attributeStruct = (AttributeStruct) attribute;
              attributeStruct.setStructDAO(structDAO);
            }
          }

          attributeMap.put(attributeName, attribute);
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

    return attributeMap;
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
   * @param _cacheTypeTable
   * @param _type
   * @param _tableName
   * @param relationshipAttributesHackMap
   *          this is a total hack. If the instance is a relationship, then
   *          return the parent_id and child_id values in this map.
   * @return Map of Attribute objects for the EnityObject with the given ID and
   *         class.
   */
  public HardCodedMetadataIterator getAttributesForHardcodedMetadataType(String _cacheTypeTable, String _type, String _tableName, Map<String, Map<String, String>> _relationshipAttributesHackMap, boolean _rootClass)
  {
    String sqlStmt = "SELECT * FROM " + _tableName + " WHERE " + _tableName + "." + EntityDAOIF.ID_COLUMN + " IN (SELECT " + EntityDAOIF.ID_COLUMN + " FROM " + _cacheTypeTable + ")";

    ResultSet resultSet = this.query(sqlStmt);

    HardCodedMetadataIterator i = new HardCodedMetadataIterator(resultSet, _type, _relationshipAttributesHackMap, _rootClass);

    return i;
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
  public boolean isValidType(String type)
  {
    String packageName = BusinessDAOFactory.getPackageFromType(type);
    String className = BusinessDAOFactory.getClassNameFromType(type);

    List<String> mdTypeFields = new LinkedList<String>();
    mdTypeFields.add(MdTypeDAOIF.TYPE_NAME_COLUMN);
    mdTypeFields.add(MdTypeDAOIF.PACKAGE_NAME_COLUMN);

    List<String> mdBusinessTable = new LinkedList<String>();
    mdBusinessTable.add(MdTypeDAOIF.TABLE);

    List<String> mdTypeConditions = new LinkedList<String>();
    mdTypeConditions.add(MdTypeDAOIF.PACKAGE_NAME_COLUMN + " = '" + packageName + "'");
    mdTypeConditions.add(MdTypeDAOIF.TYPE_NAME_COLUMN + " = '" + className + "'");

    ResultSet resultSet = this.select(mdTypeFields, mdBusinessTable, mdTypeConditions);

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
  public List<String> getEntityIds(String type)
  {
    String tableName = getMdEntityTableName(type);

    List<String> idList = new LinkedList<String>();
    ResultSet resultSet = this.selectFrom(EntityDAOIF.ID_COLUMN, tableName);

    try
    {
      while (resultSet.next())
      {
        idList.add(resultSet.getString(EntityDAOIF.ID_COLUMN));
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

    return idList;
  }

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
  public abstract void dropField(String table, String columnName, String dbColumnType, MdAttributeConcreteDAO mdAttributeConcreteDAO);

  /**
   * Returns a string that adds a column to the given table.
   * 
   * @param table
   * @param columnName
   * @param formattedColumnTree
   * 
   * @return string that adds a column to the given table.
   */
  public abstract String buildAddColumnString(String table, String columnName, String formattedColumnTree);

  /**
   * Returns a string that drops a column from the given table.
   * 
   * @param table
   * @param columnName
   * @return string that drops a column from the given table.
   */
  public abstract String buildDropColumnString(String table, String columnName);

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
  public abstract void alterFieldType(String table, String columnName, String newDbColumnType, String oldDbColumnType);

  /**
   * Adds temporary fields for the given column on the given table.
   * 
   * @param tableName
   * @param columnName
   * @param columnType
   * @param numberOfTempFields
   */
  public abstract void addTempFieldsToTable(String tableName, String columnName, String columnType, Integer numberOfTempFields);

  /**
   * Creates a temporary table that lasts for at most the duration of the session. The behavior on transaction commit is configurable with the onCommit parameter.
   * 
   * @param tableName The name of the temp table.
   * @param columns An array of MdAttribute class names that represent the columns in the table.
   * @param onCommit Decides the fate of the temporary table upon transaction commit.
   */
  public abstract void createTempTable(String tableName, List<String> columns, String onCommit);

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
  public abstract void addField(String table, String columnName, String type, String size);

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
  public abstract String addFieldBatch(String table, String columnName, String type, String size);

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
  public abstract void addField(String table, String columnName, String formattedType, MdAttributeConcreteDAO mdAttributeConcreteDAO);

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
  public abstract String addFieldBatch(String table, String columnName, String formattedType, MdAttributeConcreteDAO mdAttributeConcreteDAO);

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
  public abstract void addDecField(String table, String columnName, String type, String length, String decimal);

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
  public abstract String addDecFieldBatch(String table, String columnName, String type, String length, String decimal);

  /**
   * Returns the type formatted for a DDL command to the vendor syntax.
   * 
   * @param type
   *          the numerical decimal type
   * @param length
   * @param decimal
   * @return
   */
  public String formatDDLDecField(String type, String length, String decimal)
  {
    return type + "(" + length + "," + decimal + ")";
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
  public String formatCharacterField(String type, String length)
  {
    return type + "(" + length + ")";
  }

  /**
   * Returns the text type formatted for a DDL command to the vendor syntax.
   * 
   * @param type
   *          the numerical decimal type
   * @return
   */
  public String formatTextField(String type)
  {
    return type;
  }

  /**
   * Returns the CLOB type formatted for a DDL command to the vendor syntax.
   * 
   * @param type
   *          the numerical decimal type
   * @return
   */
  public String formatClobField(String type)
  {
    return type;
  }

  /**
   * 
   * @param table
   * @param columnName
   * @param indexName
   */
  public abstract void addUniqueIndex(String table, String columnName, String indexName);

  /**
   * 
   * @param table
   * @param columnName
   * @param indexName
   * @param delete
   */
  public abstract void dropUniqueIndex(String table, String columnName, String indexName, boolean delete);

  /**
   * @see com.runwaysdk.dataaccess.database.Database#addNonUniqueIndex(String,
   *      String, String);
   */
  public abstract void addNonUniqueIndex(String table, String columnName, String indexName);

  /**
   * @see com.runwaysdk.dataaccess.database.Database#dropNonUniqueIndex(String,
   *      String, String, boolean);
   * @param table
   * @param columnName
   * @param indexName
   * @param delete
   */
  public abstract void dropNonUniqueIndex(String table, String columnName, String indexName, boolean delete);

  /**
   * @see com.runwaysdk.dataaccess.database.Database#nonUniqueAttributeExists(String,
   *      String, String);
   */
  public abstract boolean nonUniqueAttributeExists(String table, String columnName, String indexName);

  /**
   * @see com.runwaysdk.dataaccess.database.Database#uniqueAttributeExists(String,
   *      String, String);
   */
  public abstract boolean uniqueAttributeExists(String table, String columnName, String indexName);

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
  public abstract void addGroupAttributeIndex(String table, String indexName, List<String> columnNames, boolean isUnique);

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
  public abstract void dropGroupAttributeIndex(String table, String indexName, List<String> columnNames, boolean isUnique, boolean delete);

  /**
   * Returns true if the given index exists on the given table, false otherwise.
   * 
   * @param table
   * @param indexName
   * @return true if the given index exists on the given table, false otherwise.
   */
  public abstract boolean indexExists(String table, String indexName);

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
    return false;
  }

  /**
   * Returns true if a group attribute index exists with the given name and the
   * given attributes on the given table.
   * 
   * @param tableName
   * @param indexName
   * @param columnNames
   */
  public abstract boolean groupAttributeIndexExists(String tableName, String indexName, List<String> columnNames);

  /**
   * Returns true if a group attribute index exists with the given name on the
   * given table.
   * 
   * @param tableName
   * @param indexName
   */
  public abstract boolean groupAttributeIndexExists(String tableName, String indexName);

  /**
   * 
   * @param table
   * @param columnName
   * @return
   */
  public String attributeIndexName(String table, String columnName)
  {
    return this.getIdentifierHashName(table + "_" + columnName);
  }

  /**
   * 
   * @param stringToHash
   * @return
   */
  public String getIdentifierHashName(String stringToHash)
  {
    // hash needs to be one character shorter than the max size, as
    // we will append a character to the front to ensure it is a valid
    // db identifier.
    int maxBaseHashLength = Database.MAX_DB_IDENTIFIER_SIZE - 1;

    String hashString = ServerIDGenerator.hash(stringToHash);

    if (hashString.length() < maxBaseHashLength)
    {
      maxBaseHashLength = hashString.length();
    }

    hashString = hashString.substring(0, maxBaseHashLength);

    hashString = "a" + hashString;

    return hashString;
  }

  /**
   * Creates a database friendly identifier from the given id.
   * 
   * @param id
   * 
   * @return database friendly identifier from the given id.
   */
  public String createIdentifierFromId(String id)
  {
    // hash needs to be one character shorter than the max size, as
    // we will append a character to the front to ensure it is a valid
    // db identifier.
    int maxBaseHashLength = Database.MAX_DB_IDENTIFIER_SIZE - 1;

    String dbIdentifier = id;

    if (dbIdentifier.length() < maxBaseHashLength)
    {
      maxBaseHashLength = dbIdentifier.length();
    }

    dbIdentifier = dbIdentifier.substring(0, maxBaseHashLength);

    dbIdentifier = "a" + dbIdentifier;

    return dbIdentifier;

  }

  /**
   * Returns a list of string names of the attributes that participate in a
   * group index for the given table with the index of the given name.
   * 
   * @param table
   * @param indexName
   */
  public abstract List<String> getGroupIndexAttributes(String table, String indexName);

  /**
   * Creates a new table in the database for a class. Automatically adds the
   * Component.ID field as the primary key.
   * 
   * @param tableName
   *          The name of the new table.
   */
  public abstract void createClassTable(String tableName);

  /**
   * Returns the SQL string for a new table in the database for a class, minus
   * the closing parenthesis. Automatically adds the Component.ID field as the
   * primary key.
   * 
   * @param tableName
   *          The name of the new table.
   */
  public abstract String startCreateClassTable(String tableName);

  /**
   * Creates a new table in the database for a class, including all columns for
   * that table.
   * 
   * @param tableName
   *          table name
   * @param columnDefs
   *          columnDefs column definitions.
   */
  public abstract void createClassTableBatch(String tableName, List<String> columnDefs);

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
  public abstract void alterClassTableBatch(String tableName, List<String> columnNames, List<String> columnDefs);

  /**
   * Returns the SQL string that concludes a table definition. Typically it is a
   * closing parenthesis.
   * 
   * @param tableName
   *          The name of the new table.
   */
  public String endCreateClassTable(String tableName)
  {
    return ")";
  }

  /**
   * Creates a new table in the database for a relationships. Automatically adds
   * the Component.ID field as the primary key.
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
  public abstract void createRelationshipTable(String tableName, String index1Name, String index2Name, boolean isUnique);

  /**
   * Creates a new table in the database for relationship, including all columns
   * for that table.
   * 
   * @param tableName
   *          table name
   * @param columnDefs
   *          columnDefs column definitions.
   */
  public abstract void createRelationshipTableBatch(String tableName, List<String> columnDefs);

  /**
   * Returns the SQL string for a new table in the database for a relationship,
   * minus the closing parenthesis. Automatically adds the Component.ID field as
   * the primary key.
   * 
   * @param tableName
   *          The name of the new table.
   */
  public abstract String startCreateRelationshipTableBatch(String tableName);

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
  public abstract void createRelationshipTableIndexesBatch(String tableName, String index1Name, String index2Name, boolean isUnique);

  /**
   * @see com.runwaysdk.dataaccess.database.Database#createEnumerationTable(String,
   *      String);
   */
  public abstract void createEnumerationTable(String tableName, String id);

  /**
   * Builds an SQL statement to delete an entry row.
   * 
   * @param table
   *          The table name where the row to delete can be found.
   * @param id
   *          The id of the record to delete.
   * @return The SQL delete statement.
   */
  public String buildSQLDeleteStatement(String table, String id)
  {
    String statement = "DELETE FROM " + table + " WHERE " + EntityDAOIF.ID_COLUMN + " = '" + id + "'";
    return statement;
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
  public String buildSQLDeleteStatement(String table, String id, long seq)
  {
    String statement = "DELETE FROM " + table + " WHERE " + EntityDAOIF.ID_COLUMN + " = '" + id + "'";
    statement += " AND " + ElementDAOIF.SEQUENCE_COLUMN + " = " + seq;
    return statement;
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
  public String buildSQLDeleteWhereStatement(String table, List<String> conditions)
  {
    String statement = "DELETE FROM " + table + " WHERE ";

    for (String condition : conditions)
    {
      statement += " AND " + condition;
    }

    statement = statement.replaceFirst(" AND ", "");

    return statement;
  }

  /**
   * A generalized delete that deletes all entries in the specifice table that
   * satisfy the given condition.
   * 
   * @param table
   *          The name of the table.
   * @param condition
   *          The condition that all items to be deleted must satisfy.
   */
  public void deleteWhere(String table, String condition)
  {
    List<String> conditions = new LinkedList<String>();
    conditions.add(condition);

    execute(this.buildSQLDeleteWhereStatement(table, conditions));
  }

  /**
   * Creates a view.
   * 
   * @param view
   * @param selectClause
   */
  public void createView(String view, String selectClause)
  {
    String statement = "CREATE VIEW " + view + " AS (" + selectClause + ")";

    String undo = "DROP VIEW " + view;

    new DDLCommand(statement, undo, false).doIt();
  }

  /**
   * Drops a view.
   * 
   * @param view
   * @param selectClause
   */
  public void dropView(String view, String selectClause, Boolean dropOnEndOfTransaction)
  {
    String statement = "DROP VIEW IF EXISTS " + view;

    String undo = "CREATE VIEW " + view + " AS (" + selectClause + ")";

    new DDLCommand(statement, undo, dropOnEndOfTransaction).doIt();
  }

  public List<String> getViewsByPrefix(String prefix)
  {
    throw new ForbiddenMethodException("getViewsByPrefix is not yet implemented for your database");
  }

  /**
   * Drops an entire table from the database for a class. An undo command is
   * created that will recreate the table if transaction management requires a
   * rollback. However, the undo will <b>not </b> recreate all of the fields in
   * the table, only the Component.ID.
   * 
   * @param table
   *          The name of the table to drop.
   */
  public abstract void dropClassTable(String tableName);

  /**
   * Deletes all records in the given table.
   * 
   * @param tableName
   *          The name of the table to remove all records from.
   */
  public void deleteAllTableRecords(String tableName)
  {
    String sql = "DELETE FROM " + tableName;

    this.execute(sql);
  }

  /**
   * Drops an entire table from the database for a relationship. An undo command
   * is created that will recreate the table if transaction management requires
   * a rollback. However, the undo will <b>not </b> recreate all of the fields
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
  public abstract void dropRelationshipTable(String tableName, String index1Name, String index2Name, boolean isUnique);

  /**
   * @see com.runwaysdk.dataaccess.database.Database#dropEnumerationTable(String,
   *      String);
   */
  public abstract void dropEnumerationTable(String tableName, String id);

  /**
   * Selects all instances of a field from a table.
   * 
   * @param columnName
   *          The name of the field being selected.
   * @param table
   *          The table containing the field.
   * @return List of DynaBeans representing the rows of the result set.
   */
  public ResultSet selectFrom(String columnName, String table)
  {
    String statement = "SELECT " + columnName + " FROM " + table;
    return query(statement);
  }

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
  public ResultSet selectFromWhere(String columnName, String table, String condition)
  {
    String statement = "SELECT " + columnName + " FROM " + table + " WHERE " + condition;
    return query(statement);
  }

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
  public String selectClause(List<String> columnNames, List<String> tables, List<String> conditions)
  {
    Iterator<String> i = columnNames.iterator();
    String statement = "SELECT " + i.next();
    while (i.hasNext())
    {
      statement += ", " + i.next();
    }

    statement += "\n";

    i = tables.iterator();
    statement += " FROM " + i.next();
    while (i.hasNext())
    {
      statement += ", " + i.next();
    }

    statement += "\n";

    if (conditions.size() > 0)
    {
      i = conditions.iterator();
      statement += " WHERE " + i.next();
      while (i.hasNext())
      {
        statement += " AND " + i.next();
      }
    }

    statement += "\n";

    return statement;
  }

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
  public ResultSet select(List<String> columnNames, List<String> tables, List<String> conditions)
  {
    return query(selectClause(columnNames, tables, conditions));
  }

  /**
   * Builds a JDBC prepared <code>INSERT</code> statement for the given fields. <br>
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
  public PreparedStatement buildPreparedSQLInsertStatement(String table, List<String> columnNames, List<String> prepStmtVars, List<Object> values, List<String> attributeTypes)
  {
    if (Database.loggingDMLandDDLstatements() == true)
    {
      System.out.println(this.buildSQLinsertStatement(table, columnNames, values, attributeTypes) + ";");
    }

    String sqlStmt = "INSERT INTO " + table;

    Iterator<String> interator = columnNames.iterator();
    sqlStmt += " (" + interator.next();
    while (interator.hasNext())
    {
      sqlStmt += ", " + interator.next();
    }

    interator = prepStmtVars.iterator();
    sqlStmt += ") VALUES (";

    boolean firstIteration = true;
    while (interator.hasNext())
    {
      if (!firstIteration)
      {
        sqlStmt += ", ";
      }
      firstIteration = false;

      String prepStmtVar = interator.next();

      sqlStmt += prepStmtVar;
    }
    sqlStmt += ")";

    Connection conn = Database.getConnection();
    PreparedStatement prepared = null;

    try
    {
      prepared = conn.prepareStatement(sqlStmt);
    }
    catch (SQLException e)
    {
      this.throwDatabaseException(e);
    }

    // Bind the variables
    for (int i = 0; i < columnNames.size(); i++)
    {
      this.bindPreparedStatementValue(prepared, i + 1, values.get(i), attributeTypes.get(i));
    }

    return prepared;
  }

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
  public String buildSQLinsertStatement(String table, List<String> columnNames, List<Object> values, List<String> attributeTypes)
  {
    String sqlStmt = "INSERT INTO " + table;

    Iterator<String> columnNameIterator = columnNames.iterator();
    Iterator<Object> valuesIterator = values.iterator();

    sqlStmt += " (";

    StringBuffer columnNamesSQL = new StringBuffer();

    while (columnNameIterator.hasNext())
    {
      String columnName = columnNameIterator.next();
      Object value = valuesIterator.next();

      // We are not logging binary values
      if (! ( value instanceof String ))
      {
        continue;
      }

      columnNamesSQL.append(", " + columnName);
    }

    valuesIterator = values.iterator();
    Iterator<String> typesInterator = attributeTypes.iterator();

    sqlStmt += columnNamesSQL.toString().replaceFirst(",", "") + ") VALUES (";

    boolean firstIteration = true;
    while (valuesIterator.hasNext())
    {
      Object value = valuesIterator.next();
      String attributeType = typesInterator.next();

      // We are not logging binary values
      if (! ( value instanceof String ))
      {
        continue;
      }

      if (!firstIteration)
      {
        sqlStmt += ", ";
      }
      firstIteration = false;

      sqlStmt += this.formatJavaToSQL(value.toString(), attributeType, false);
    }
    sqlStmt += ")";

    return sqlStmt;
  }

  /**
   * Builds a JDBC prepared <code>UPDATE</code> statement for the given fields. <br>
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
   * @param id
   *          id of the object to update.
   * 
   * @return <code>UPDATE</code> PreparedStatement
   */
  public PreparedStatement buildPreparedSQLUpdateStatement(String table, List<String> columnNames, List<String> prepStmtVars, List<Object> values, List<String> attributeTypes, String id)
  {
    if (Database.loggingDMLandDDLstatements() == true)
    {
      // if (table.trim().equalsIgnoreCase(MdEntityDAOIF.TABLE))
      System.out.println(this.buildSQLupdateStatement(table, columnNames, values, attributeTypes, id) + ";");
    }

    String sqlStmt = "UPDATE " + table;

    Iterator<String> fieldIterator = columnNames.iterator();
    Iterator<String> prepStmtIterator = prepStmtVars.iterator();

    sqlStmt += " SET ";

    boolean firstIteration = true;
    while (fieldIterator.hasNext())
    {
      String field = fieldIterator.next();
      String prepStmtVar = prepStmtIterator.next();

      if (!firstIteration)
      {
        sqlStmt += ", ";
      }

      firstIteration = false;
      sqlStmt += field + "= " + prepStmtVar + " ";
    }

    sqlStmt += " WHERE " + EntityDAOIF.ID_COLUMN + "='" + id + "'";

    Connection conn = Database.getConnection();
    PreparedStatement prepared = null;

    try
    {
      prepared = conn.prepareStatement(sqlStmt);
    }
    catch (SQLException e)
    {
      this.throwDatabaseException(e);
    }

    // Bind the variables
    for (int i = 0; i < columnNames.size(); i++)
    {
      this.bindPreparedStatementValue(prepared, i + 1, values.get(i), attributeTypes.get(i));
    }

    return prepared;
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
  public String buildSQLupdateStatement(String table, List<String> columnNames, List<Object> values, List<String> attributeTypes, String id)
  {
    String sqlStmt = "UPDATE " + table;

    Iterator<String> fieldIterator = columnNames.iterator();
    Iterator<Object> valueIterator = values.iterator();
    Iterator<String> attributeTypeIterator = attributeTypes.iterator();

    sqlStmt += " SET ";

    boolean firstIteration = true;
    while (fieldIterator.hasNext())
    {
      String field = fieldIterator.next();
      Object value = valueIterator.next();
      String attributeType = attributeTypeIterator.next();

      // We are not logging binary values
      if (! ( value instanceof String ))
      {
        continue;
      }

      if (!firstIteration)
      {
        sqlStmt += ", ";
      }

      firstIteration = false;
      sqlStmt += field + "= " + this.formatJavaToSQL(value.toString(), attributeType, false) + " ";
    }

    sqlStmt += " WHERE " + EntityDAOIF.ID_COLUMN + "='" + id + "'";

    return sqlStmt;
  }

  /**
   * Builds a JDBC prepared <code>UPDATE</code> statement for the given fields. <br>
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
   * @param id
   *          id of the object to update.
   * @param seq
   *          sequence of the object to update.
   * 
   * @return <code>UPDATE</code> PreparedStatement
   */
  public PreparedStatement buildPreparedSQLUpdateStatement(String table, List<String> columnNames, List<String> prepStmtVars, List<Object> values, List<String> attributeTypes, String id, long seq)
  {
    if (Database.loggingDMLandDDLstatements() == true)
    {
      // if (table.trim().equalsIgnoreCase(MdEntityDAOIF.TABLE))
      System.out.println(this.buildSQLupdateStatement(table, columnNames, values, attributeTypes, id) + ";");
    }

    String sqlStmt = "UPDATE " + table;

    Iterator<String> fieldIterator = columnNames.iterator();
    Iterator<String> prepStmtIterator = prepStmtVars.iterator();

    sqlStmt += " SET ";

    boolean firstIteration = true;
    while (fieldIterator.hasNext())
    {
      String field = fieldIterator.next();
      String prepStmtVar = prepStmtIterator.next();

      if (!firstIteration)
      {
        sqlStmt += ", ";
      }

      firstIteration = false;
      sqlStmt += field + "= " + prepStmtVar + " ";
    }

    sqlStmt += " WHERE " + EntityDAOIF.ID_COLUMN + "='" + id + "'";
    sqlStmt += " AND " + EntityDAOIF.SEQUENCE_COLUMN + " = " + seq;

    Connection conn = Database.getConnection();
    PreparedStatement prepared = null;

    try
    {
      prepared = conn.prepareStatement(sqlStmt);
    }
    catch (SQLException e)
    {
      this.throwDatabaseException(e);
    }

    // Bind the variables
    for (int i = 0; i < columnNames.size(); i++)
    {
      this.bindPreparedStatementValue(prepared, i + 1, values.get(i), attributeTypes.get(i));
    }

    return prepared;
  }

  /**
   * Builds a JDBC prepared <code>UPDATE</code> statement for the given field on
   * the object with the given id. <br>
   * 
   * @param table
   *          The table to insert into.
   * @param columnName
   *          The name of the field being updated.
   * @param entityId
   *          entity ID
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
  public PreparedStatement buildPreparedUpdateFieldStatement(String table, String entityId, String columnName, String prepStmtVar, Object oldValue, Object newValue, String attributeType)
  {
    if (Database.loggingDMLandDDLstatements() == true)
    {
      // We are not logging binary values
      if ( ( oldValue instanceof String ))
      {
        // if (table.trim().equalsIgnoreCase(MdEntityDAOIF.TABLE))
        System.out.println(this.buildPreparedSQLUpdateField(table, entityId, columnName, oldValue, newValue, attributeType) + ";");
      }
    }

    String sqlStmt = "UPDATE " + table;

    sqlStmt += " SET " + columnName + " = " + prepStmtVar + " ";
    sqlStmt += " WHERE " + columnName + " = " + prepStmtVar + " ";

    if (entityId != null)
    {
      sqlStmt += " AND " + EntityDAOIF.ID_COLUMN + " = " + prepStmtVar + " ";
    }

    Connection conn = Database.getConnection();
    PreparedStatement prepared = null;

    try
    {
      prepared = conn.prepareStatement(sqlStmt);
    }
    catch (SQLException e)
    {
      this.throwDatabaseException(e);
    }

    // Bind the variables
    this.bindPreparedStatementValue(prepared, 1, newValue, attributeType);
    this.bindPreparedStatementValue(prepared, 2, oldValue, attributeType);

    if (entityId != null)
    {
      this.bindPreparedStatementValue(prepared, 3, entityId, MdAttributeCharacterInfo.CLASS);
    }

    return prepared;
  }

  /**
   * Builds a JDBC prepared <code>UPDATE</code> statement for the given field on
   * the object with the given id. <br>
   * 
   * @param table
   *          The table to insert into.
   * @param columnName
   *          The name of the field being updated.
   * @param entityId
   *          entity ID
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
  public PreparedStatement buildPreparedUpdateFieldStatement(String table, String entityId, String columnName, String prepStmtVar, Object newValue, String attributeType)
  {
    String sqlStmt = "UPDATE " + table;

    sqlStmt += " SET " + columnName + " = " + prepStmtVar + " ";
    sqlStmt += " WHERE " + EntityDAOIF.ID_COLUMN + " = " + prepStmtVar + " ";

    Connection conn = Database.getConnection();
    PreparedStatement prepared = null;

    try
    {
      prepared = conn.prepareStatement(sqlStmt);
    }
    catch (SQLException e)
    {
      this.throwDatabaseException(e);
    }

    // Bind the variables
    this.bindPreparedStatementValue(prepared, 1, newValue, attributeType);
    this.bindPreparedStatementValue(prepared, 2, entityId, MdAttributeCharacterInfo.CLASS);

    return prepared;
  }

  /**
   * Builds a SQL <code>UPDATE</code> statement for the given fields. <br>
   * 
   * @param table
   *          The table to insert into.
   * @param columnName
   *          The name of the field being updated.
   * @param entityId
   *          entity ID
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
  public String buildPreparedSQLUpdateField(String table, String entityId, String columnName, Object oldValue, Object newValue, String attributeType)
  {
    String sqlStmt = "UPDATE " + table;

    sqlStmt += " SET " + columnName + " = " + this.formatJavaToSQL(newValue.toString(), attributeType, false) + " ";
    sqlStmt += " WHERE " + columnName + " = " + this.formatJavaToSQL(oldValue.toString(), attributeType, false) + " ";

    if (entityId != null)
    {
      sqlStmt += " AND " + EntityDAOIF.ID_COLUMN + " = " + this.formatJavaToSQL(entityId, MdAttributeCharacterInfo.CLASS, false) + " ";
    }

    return sqlStmt;
  }

  /**
   * Executes a statement in the database. Any result set from the execution is
   * discarded. The statement is assumed to be valid and is not checked before
   * execution.
   * 
   * @param stmt
   *          The SQL statement to be executed in the database.
   */
  protected void execute(String stmt)
  {
    Connection conx = Database.getConnection();
    Statement statement = null;
    try
    {
      statement = conx.createStatement();
      statement.executeUpdate(stmt);
      conx.commit();
    }
    catch (SQLException ex)
    {
      this.throwDatabaseException(ex, stmt);
    }
    finally
    {
      try
      {
        if (statement != null)
          statement.close();
        Database.closeConnection(conx);
      }
      catch (SQLException ex)
      {
        this.throwDatabaseException(ex);
      }
    }
  }

  /**
   * Executes a statement with root permissions in the database. Any result set
   * from the execution is discarded. The statement is assumed to be valid and
   * is not checked before execution.
   * 
   * @param stmt
   *          The SQL statement to be executed in the database.
   * @param autoCommit
   *          True if statements should auto-commit, false otherwise.
   */
  protected void executeAsRoot(List<String> stmt, boolean autoCommit)
  {
    Connection conx = null;
    try
    {
      conx = this.rootDataSource.getConnection();
      conx.setAutoCommit(autoCommit);
    }
    catch (SQLException ex)
    {
      this.throwDatabaseException(ex);
    }

    this.executeAsRoot(conx, stmt, autoCommit);
  }

  /**
   * Executes a statement with root permissions in the database. Any result set
   * from the execution is discarded. The statement is assumed to be valid and
   * is not checked before execution.
   * 
   * @param stmt
   *          The SQL statement to be executed in the database.
   * @param autoCommit
   *          True if statements should auto-commit, false otherwise.
   */
  protected void executeAsRoot(Connection conx, List<String> stmt, boolean autoCommit)
  {
    Statement statement = null;
    try
    {
      statement = conx.createStatement();
      for (String sql : stmt)
      {
        statement.execute(sql);
      }

      if (!autoCommit)
      {
        conx.commit();
      }
    }
    catch (SQLException ex)
    {
      String error = "\nBEGIN Batch SQL Statments";
      for (String sql : stmt)
        error += "\n  " + sql;
      error += "\nEND Batch SQL Statements";

      this.throwDatabaseException(ex, error);
    }
    finally
    {
      try
      {
        if (statement != null)
          statement.close();
        conx.close();
      }
      catch (SQLException ex)
      {
        this.throwDatabaseException(ex);
      }
    }
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
  public int[] executeBatch(List<String> sqlStmts)
  {
    int[] batchResults = new int[0];
    if (sqlStmts.size() == 0)
    {
      return batchResults;
    }

    Connection conx = Database.getConnection();
    Statement statement = null;
    try
    {
      statement = conx.createStatement();

      for (String stmt : sqlStmts)
      {
        statement.addBatch(stmt);

        if (Database.loggingDMLandDDLstatements())
        {
          System.out.println(stmt + ";");
        }
      }
      batchResults = statement.executeBatch();
      conx.commit();
    }
    catch (SQLException ex)
    {
      String errMsg = "One of the following SQL statements executed in batch, one or more \n" + "caused an error in the database:\n " + "------------------------------------------------------------------\n";

      SQLException exception = ex.getNextException();
      while (exception != null)
      {
        errMsg += "Exception: " + exception.getMessage();
        exception = exception.getNextException();
      }

      for (String stmt : sqlStmts)
      {
        errMsg += "Statement: " + stmt + "\n";
      }

      this.throwDatabaseException(ex, errMsg);
    }
    finally
    {
      try
      {
        if (statement != null)
          statement.close();
        Database.closeConnection(conx);
      }
      catch (SQLException ex)
      {
        this.throwDatabaseException(ex);
      }
    }
    return batchResults;
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
  public int[] executeStatementBatch(List<PreparedStatement> preparedStmts)
  {
    int[] batchResults = new int[0];
    if (preparedStmts.size() == 0)
    {
      batchResults = new int[0];
      return batchResults;
    }
    else
    {
      batchResults = new int[preparedStmts.size()];
    }

    try
    {
      for (int i = 0; i < preparedStmts.size(); i++)
      {
        PreparedStatement preparedStmt = preparedStmts.get(i);
        batchResults[i] = preparedStmt.executeUpdate();
      }
    }
    catch (SQLException ex)
    {
      this.throwDatabaseException(ex);
    }
    finally
    {
      try
      {
        for (int i = 0; i < preparedStmts.size(); i++)
        {
          PreparedStatement preparedStmt = preparedStmts.get(i);
          preparedStmt.close();
        }
      }
      catch (SQLException ex)
      {
        this.throwDatabaseException(ex);
      }
    }
    return batchResults;
  }

  /**
   * Returns the value of a clob for the column on the table for the object with
   * the given id.
   * 
   * <br/>
   * precondition: id must represent a valid object <br/>
   * 
   * @param table
   * @param columnName
   * @param id
   * @return value of the clob.
   */
  public String getClob(String table, String columnName, String id)
  {
    Connection conn = Database.getConnection();

    String returnString = null;
    Statement statement = null;
    ResultSet resultSet = null;
    try
    {
      // get the blob
      statement = conn.createStatement();
      String query = "SELECT " + columnName + " FROM " + table + " WHERE " + EntityDAOIF.ID_COLUMN + " = '" + id + "'";
      resultSet = statement.executeQuery(query);

      boolean hasNext = resultSet.next();

      if (!hasNext)
      {
        return "";
      }

      Object returnObject = resultSet.getObject(columnName);

      if (returnObject == null)
      {
        return "";
      }
      else if (returnObject instanceof Clob)
      {
        Clob clob = resultSet.getClob(columnName);

        // null check
        if (clob == null)
        {
          return "";
        }

        returnString = clob.getSubString(1, (int) clob.length());
      }
      else if (returnObject instanceof String)
      {
        String clobString = (String) resultSet.getObject(columnName);

        // null check
        if (clobString == null)
        {
          return "";
        }

        returnString = clobString;
      }
      else
      {
        String errMsg = "Database [" + ResultSet.class.getName() + "] object did not return " + "a String or a Clob for a [" + MdAttributeClobInfo.CLASS + "] attribute.";
        throw new ProgrammingErrorException(errMsg);
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
    return returnString;
  }

  /**
   * Sets the value of this blob as the specified bytes.
   * 
   * @param table
   * @param columnName
   * @param id
   * @param clobString
   * @return The number of bytes written.
   */
  public void setClob(String table, String columnName, String id, String clobString)
  {
    Connection conn = Database.getConnection();
    Statement statement = null;
    ResultSet resultSet = null;
    try
    {
      // get the blob
      statement = conn.createStatement();
      String select = "SELECT " + columnName + " FROM " + table + " WHERE " + EntityDAOIF.ID_COLUMN + " = '" + id + "'";
      String update = "UPDATE " + table + " SET " + columnName + " = " + "? WHERE " + EntityDAOIF.ID_COLUMN + " = '" + id + "'";
      resultSet = statement.executeQuery(select);
      boolean resultSetFound = resultSet.next();
      if (!resultSetFound)
      {
        return;
      }

      boolean setNull = false;
      if (clobString.equals(""))
      {
        setNull = true;
      }

      Object returnObject = resultSet.getObject(columnName);

      if (returnObject instanceof Clob)
      {
        Clob clob = resultSet.getClob(columnName);

        // null check
        if (clob == null)
        {
          // add the bytes directly
          PreparedStatement prepared = conn.prepareStatement(update);
          if (setNull)
          {
            prepared.setNull(1, java.sql.Types.CLOB);
          }
          else
          {
            prepared.setString(1, clobString);
          }
          prepared.executeUpdate();
        }
        else
        {
          // modify the CLOB
          if (setNull)
          {
            clob.setString(1, null);
          }
          else
          {
            clob.setString(1, clobString);
          }

          if (conn.getMetaData().locatorsUpdateCopy())
          {
            // The current database needs to be manually updated (it doesn't
            // support auto blob updates)
            PreparedStatement prepared = conn.prepareStatement(update);
            prepared.setClob(1, clob);
            prepared.executeUpdate();
          }
        }
      }
      else if (returnObject == null || returnObject instanceof String)
      {
        // The current database needs to be manually updated (it doesn't support
        // auto blob updates)
        PreparedStatement prepared = conn.prepareStatement(update);
        if (setNull)
        {
          prepared.setNull(1, java.sql.Types.CLOB);
        }
        else
        {
          prepared.setString(1, clobString);
        }
        prepared.executeUpdate();
      }
      else
      {
        String errMsg = "Database [" + ResultSet.class.getName() + "] object did not return " + "a String or a Clob for a [" + MdAttributeClobInfo.CLASS + "] attribute.";
        throw new ProgrammingErrorException(errMsg);
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
  }

  /**
   * Returns the value of a blob as a byte array.
   * 
   * @param table
   * @param columnName
   * @param id
   * @return byte[] value of the blob.
   */
  public byte[] getBlobAsBytes(String table, String columnName, String id)
  {
    Connection conn = Database.getConnection();
    byte[] byteArray = this.getBlobAsBytes(table, columnName, id, conn);

    try
    {
      this.closeConnection(conn);
    }
    catch (SQLException e)
    {
      this.throwDatabaseException(e);
    }

    return byteArray;
  }

  /**
   * Returns the value of a blob as a byte array. It is up to the client to
   * close the database connection.
   * 
   * @param table
   * @param columnName
   * @param id
   * @param conn
   * @return byte[] value of the blob.
   */
  public byte[] getBlobAsBytes(String table, String columnName, String id, Connection conn)
  {
    byte[] returnBytes = null;
    Statement statement = null;
    ResultSet resultSet = null;
    try
    {
      // get the blob
      statement = conn.createStatement();
      String query = "SELECT " + columnName + " FROM " + table + " WHERE " + EntityDAOIF.ID_COLUMN + " = '" + id + "'";
      resultSet = statement.executeQuery(query);
      resultSet.next();
      Blob blob = resultSet.getBlob(columnName);

      // null check
      if (blob == null)
        return new byte[0];

      returnBytes = blob.getBytes(1, (int) blob.length());
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
   * @param id
   * @param pos
   * @param length
   * @return
   */
  public byte[] getBlobAsBytes(String table, String columnName, String id, long pos, int length)
  {
    Connection conn = Database.getConnection();
    byte[] returnBytes = null;
    Statement statement = null;
    ResultSet resultSet = null;
    try
    {
      // get the blob
      statement = conn.createStatement();
      String query = "SELECT " + columnName + " FROM " + table + " WHERE " + EntityDAOIF.ID_COLUMN + " = '" + id + "'";
      resultSet = statement.executeQuery(query);
      resultSet.next();
      Blob blob = resultSet.getBlob(columnName);

      // null check
      if (blob == null)
        return new byte[0];

      returnBytes = blob.getBytes(pos, length);
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
  public int setBlobAsBytes(String table, String columnName, String id, long pos, byte[] bytes, int offset, int length)
  {
    Connection conn = Database.getConnection();
    int written = 0;
    Statement statement = null;
    ResultSet resultSet = null;
    try
    {
      // get the blob
      statement = conn.createStatement();
      String select = "SELECT " + columnName + " FROM " + table + " WHERE " + EntityDAOIF.ID_COLUMN + " = '" + id + "'";
      String update = "UPDATE " + table + " SET " + columnName + " = " + "? WHERE " + EntityDAOIF.ID_COLUMN + " = '" + id + "'";
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
          PreparedStatement prepared = conn.prepareStatement(update);
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
   * @param id
   * @param bytes
   * @return The number of bytes written.
   */
  public int setBlobAsBytes(String table, String columnName, String id, byte[] bytes)
  {
    Connection conn = Database.getConnection();
    int written = 0;
    Statement statement = null;
    ResultSet resultSet = null;
    try
    {
      // get the blob
      statement = conn.createStatement();
      String select = "SELECT " + columnName + " FROM " + table + " WHERE " + EntityDAOIF.ID_COLUMN + " = '" + id + "'";
      String update = "UPDATE " + table + " SET " + columnName + " = " + "? WHERE " + EntityDAOIF.ID_COLUMN + " = '" + id + "'";
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
        PreparedStatement prepared = conn.prepareStatement(update);
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
          PreparedStatement prepared = conn.prepareStatement(update);
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
   * Returns the blob as an array of bytes.
   * 
   * @param table
   * @param columnName
   * @param id
   * @return The byte array value of this blob attribute.
   */
  public long getBlobSize(String table, String columnName, String id)
  {
    Connection conn = Database.getConnection();
    long size = 0;
    Statement statement = null;
    ResultSet resultSet = null;
    try
    {
      // get the blob
      statement = conn.createStatement();
      String query = "SELECT " + columnName + " FROM " + table + " WHERE " + EntityDAOIF.ID_COLUMN + " = '" + id + "'";
      resultSet = statement.executeQuery(query);
      resultSet.next();
      Blob blob = resultSet.getBlob(columnName);

      // check for a null blob value
      if (blob == null)
        return 0; // the blob is null, hence it has no size.

      size = blob.length();
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
   * Truncates a blob by the specified length.
   * 
   * @param table
   * @param columnName
   * @param id
   * @param length
   */
  public void truncateBlob(String table, String columnName, String id, long length, Connection conn)
  {
    Statement statement = null;
    ResultSet resultSet = null;
    try
    {
      // get the blob
      statement = conn.createStatement();
      String select = "SELECT " + columnName + " FROM " + table + " WHERE " + EntityDAOIF.ID_COLUMN + " = '" + id + "'";
      String update = "UPDATE " + table + " SET " + columnName + " = " + "? WHERE " + EntityDAOIF.ID_COLUMN + " = '" + id + "'";
      resultSet = statement.executeQuery(select);
      resultSet.next();
      Blob blob = resultSet.getBlob(columnName);

      if (blob != null)
      {
        blob.truncate(length);
        // modify the blob
        if (conn.getMetaData().locatorsUpdateCopy())
        {
          // The current database needs to be manually updated (it doesn't
          // support auto blob updates)
          PreparedStatement prepared = conn.prepareStatement(update);
          prepared.setBlob(1, blob);
          prepared.executeUpdate();
        }
      }
      // else do nothing because there is nothing to truncate
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
   * Throws the appropriate exception based on the severity of the error. Some
   * DB errors indicate a bug in the core.
   * 
   * @param ex
   *          SQLException thrown.
   */
  public void throwDatabaseException(SQLException ex)
  {
    this.throwDatabaseException(ex, new String(""));
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
    return false;
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
  public abstract void throwDatabaseException(SQLException ex, String debugMsg);

  /**
   * Returns a List containing the fields in a table.
   * 
   * @param tableName
   *          The table to get the field list from.
   * @return The List of the fields in the table.
   */
  public abstract List<String> getColumnNames(String tableName);

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
  public abstract boolean columnExists(String columnName, String tableName);

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
  public abstract boolean tableExists(String tableName);

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
  public ResultSet query(String sqlStmt)
  {
    Connection conx = Database.getConnection();
    String selectStatement = sqlStmt;

    Statement statement = null;
    ResultSet resultSet = null;

    try
    {
      statement = conx.createStatement();
      
      boolean isResultSet = statement.execute(selectStatement);
      
      while(true) {
        if (isResultSet) {
          resultSet = statement.getResultSet();

          return resultSet;
        }
        else if (statement.getUpdateCount() == -1) {
          throw new SQLException("No results were returned by the query.");
        }
        
        isResultSet = statement.getMoreResults();
      }
    }
    catch (SQLException ex)
    {
      this.throwDatabaseException(ex, sqlStmt);
    }
    finally
    {
      try
      {
        Database.closeConnection(conx);
      }
      catch (SQLException e)
      {
        this.throwDatabaseException(e);
      }
    }

    return resultSet;
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
  public ResultSet getMdAttributeDimensionFields(String mdAttributeId)
  {
    List<String> columnNames = new LinkedList<String>();
    List<String> tables = new LinkedList<String>();
    List<String> conditions = new LinkedList<String>();

    columnNames.add(MdAttributeDimensionInfo.ID);
    columnNames.add(MdAttributeDimensionInfo.REQUIRED);
    columnNames.add(MdAttributeDimensionDAOIF.DEFAULT_VALUE);
    columnNames.add(MdAttributeDimensionDAOIF.DEFINING_MD_ATTRIBUTE);
    columnNames.add(MdAttributeDimensionDAOIF.DEFINING_MD_DIMENSION);

    tables.add(MdAttributeDimensionDAOIF.TABLE);

    if (mdAttributeId != null)
    {
      conditions.add(MdAttributeDimensionDAOIF.DEFINING_MD_ATTRIBUTE + " = '" + mdAttributeId + "'");
    }

    return Database.select(columnNames, tables, conditions);
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
  public ResultSet getMdAttributeDimensionIds(String mdDimensionId)
  {
    String sqlStmt = "SELECT " + MdAttributeDimensionInfo.ID + " FROM " + MdAttributeDimensionDAOIF.TABLE;

    if (mdDimensionId != null)
    {
      sqlStmt += " WHERE " + MdAttributeDimensionDAOIF.DEFINING_MD_DIMENSION + " = '" + mdDimensionId + "' ";
    }

    return this.query(sqlStmt);
  }

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
  public ResultSet query(String sqlStmt, Connection conx)
  {
    String selectStatement = sqlStmt;

    Statement statement = null;
    ResultSet resultSet = null;

    try
    {
      statement = conx.createStatement();
      resultSet = statement.executeQuery(selectStatement);
    }
    catch (SQLException ex)
    {
      this.throwDatabaseException(ex, sqlStmt);
    }

    return resultSet;
  }

  /**
   * {@link #execute(String)}s an SQL statement.
   * 
   * @param statement
   *          The SQL statement being parsed and executed.
   * @see #execute(String)
   */
  public void parseAndExecute(String statement)
  {
    execute(statement);
  }

  /**
   * Gets the next sequence number from the database. Concrete implementations
   * should be <code><b>synchronized</b></code>.
   * 
   * @return The next sequence number from the database.
   */
  public abstract String getNextSequenceNumber();

  /**
   * Hardcoded database commands that create the database sequence that is used
   * to help generate unique ids.
   */
  public abstract void createObjectSequence();

  /**
   * Gets the next sequence number from the database. Concrete implementations
   * should be <code><b>synchronized</b></code>.
   * 
   * @return The next sequence number from the database.
   */
  public abstract String getNextTransactionSequence();

  /**
   * Hardcoded database commands that create the database sequence that is used
   * to help generate unique transaction numbers.
   */
  public abstract void createTransactionSequence();

  /**
   * Resets the transaction sequence. This should ONLY be called for runway
   * development testing purposes.
   */
  public abstract void resetTransactionSequence();

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
  public abstract String formatJavaToSQL(String value, String dataType, boolean ignoreCase);

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
  public String formatJavaToSQLForQuery(String value, String dataType, boolean ignoreCase)
  {
    return this.formatJavaToSQL(value, dataType, ignoreCase);
  }

  /**
   * Different databases format column aliases differently in the column clause
   * of a select statement. Returns the given String column alias formatted to
   * the syntax of the database vendor.
   * 
   * @param columnAlias
   * @return given String column alias formatted to the syntax of the database
   *         vendor.
   */
  public abstract String formatColumnAlias(String columnAlias);

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
  public abstract String formatColumnAlias(String columnAlias, String dataType);

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
  public void bindPreparedStatementValue(PreparedStatement prepStmt, int index, Object value, String dataType)
  {
    try
    {
      if (dataType.equals(MdAttributeCharacterInfo.CLASS) || dataType.equals(MdAttributeStructInfo.CLASS) || dataType.equals(MdAttributeLocalCharacterInfo.CLASS) || dataType.equals(MdAttributeLocalTextInfo.CLASS) || dataType.equals(MdAttributeReferenceInfo.CLASS) || dataType.equals(MdAttributeTermInfo.CLASS) || dataType.equals(MdAttributeFileInfo.CLASS) || dataType.equals(MdAttributeEnumerationInfo.CLASS) || dataType.equals(MdAttributeMultiReferenceInfo.CLASS) || dataType.equals(MdAttributeMultiTermInfo.CLASS) || dataType.equals(MdAttributeHashInfo.CLASS))
      {
        if ( ( (String) value ).equals(""))
        {
          prepStmt.setNull(index, java.sql.Types.VARCHAR);
        }
        else
        {
          prepStmt.setString(index, (String) value);
        }
      }
      else if (dataType.equals(MdAttributeDateTimeInfo.CLASS))
      {
        if ( ( (String) value ).trim().equals(""))
        {
          prepStmt.setNull(index, java.sql.Types.TIMESTAMP);
        }
        else
        {
          prepStmt.setTimestamp(index, Timestamp.valueOf((String) value));
        }
      }
      else if (dataType.equals(MdAttributeDateInfo.CLASS))
      {
        if ( ( (String) value ).trim().equals(""))
        {
          prepStmt.setNull(index, java.sql.Types.DATE);
        }
        else
        {
          prepStmt.setDate(index, Date.valueOf((String) value));
        }
      }
      else if (dataType.equals(MdAttributeTimeInfo.CLASS))
      {
        if ( ( (String) value ).trim().equals(""))
        {
          prepStmt.setNull(index, java.sql.Types.TIME);
        }
        else
        {
          prepStmt.setTime(index, Time.valueOf((String) value));
        }
      }
      else if (dataType.equals(MdAttributeIntegerInfo.CLASS) || dataType.equals(MdAttributeBooleanInfo.CLASS))
      {
        if ( ( (String) value ).trim().equals(""))
        {
          prepStmt.setNull(index, java.sql.Types.INTEGER);
        }
        else
        {
          prepStmt.setInt(index, Integer.parseInt((String) value));
        }
      }
      else if (dataType.equals(MdAttributeLongInfo.CLASS))
      {
        if ( ( (String) value ).trim().equals(""))
        {
          prepStmt.setNull(index, java.sql.Types.BIGINT);
        }
        else
        {
          prepStmt.setLong(index, Long.parseLong((String) value));
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
          prepStmt.setFloat(index, Float.parseFloat((String) value));
        }
      }
      else if (dataType.equals(MdAttributeDoubleInfo.CLASS))
      {
        if ( ( (String) value ).trim().equals(""))
        {
          prepStmt.setNull(index, java.sql.Types.DOUBLE);
        }
        else
        {
          prepStmt.setDouble(index, Double.parseDouble((String) value));
        }
      }
      else if (dataType.equals(MdAttributeDecimalInfo.CLASS))
      {
        if ( ( (String) value ).trim().equals(""))
        {
          prepStmt.setNull(index, java.sql.Types.DECIMAL);
        }
        else
        {
          prepStmt.setBigDecimal(index, new BigDecimal((String) value));
        }
      }
      else if (dataType.equals(MdAttributeTextInfo.CLASS) || dataType.equals(MdAttributeClobInfo.CLASS) || dataType.equals(MdAttributeSymmetricInfo.CLASS))
      {
        if ( ( (String) value ).equals(""))
        {
          prepStmt.setNull(index, java.sql.Types.CLOB);
        }
        else
        {
          // here is another way of setting the value:
          // byte[] byteValue = ((String)value).getBytes();
          // prepStmt.setAsciiStream (1, new
          // ByteArrayInputStream(byteValue),byteValue.length);
          prepStmt.setString(index, (String) value);
        }
      }
      else if (dataType.equals(MdAttributeBlobInfo.CLASS))
      {
        if (value instanceof byte[])
        {
          if ( ( (byte[]) value ).length == 0)
          {
            // prepStmt.setNull(index, java.sql.Types.BLOB);
            prepStmt.setBytes(index, new byte[0]);
          }
          else
          {
            // prepStmt.setBlob(index, (Blob)value);
            prepStmt.setBytes(index, (byte[]) value);
          }
        }
        else
        {
          prepStmt.setBytes(index, new byte[0]);
        }
      }
      else
      {
        throw new ProgrammingErrorException("AbstractDatabase doesn't know how to bind attribute type [" + dataType + "]");
      }
    }
    catch (SQLException ex)
    {
      this.throwDatabaseException(ex);
    }
  }

  /**
   * Returns the given sql expression wrapped in a SQL uppercase function.
   * 
   * @param sqlExpression
   * @return sqlExpression wrapped in an uppercase function call in the sytnax
   *         of the database in use.
   */
  public String toUpperFunction(String sqlExpression)
  {
    return "UPPER(" + sqlExpression + ")";
  }

  /**
   * Returns the variance function name for the database in use.
   * 
   * @return variance function name for the database in use.
   */
  public String varianceFunction()
  {
    return "VARIANCE";
  }

  /**
   * Returns the standard deviation function name for the database in use.
   * 
   * @return standard deviation function name for the database in use.
   */
  public String stdDevFunction()
  {
    return "STDDEV";
  }

  /**
   * Formats an SQL time value to a Java String.
   * 
   * @param value
   * @param dataType
   * @param ignoreCase
   * @return
   */
  public String formatSQLToJavaTime(String value)
  {
    return value;
  }

  /**
   * Formats the column for a comparison in a select statement.
   * 
   * @param formatted
   *          column name.
   * @return formatted column name.
   */
  public String formatColumnForCompare(String qualifiedColumnName, String dataType)
  {
    return qualifiedColumnName;
  }

  /**
   * Formats a column for a select statement.
   * 
   * @param qualifiedColumnName
   * @param mdAttribute
   *          MdAttribute that defines the attribute that uses the given column.
   * @return
   */
  public String formatSelectClauseColumn(String qualifiedColumnName, MdAttributeConcreteDAOIF mdAttribute)
  {
    return qualifiedColumnName;
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
    return value;
  }

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
  public String escapeSQLCharacters(String sqlStmt)
  {
    // Escape a single quote
    String newSqlStmt = sqlStmt.replaceAll("'", "''");

    // escape backslashes
    newSqlStmt = newSqlStmt.replaceAll("\\\\", "\\\\\\\\");

    return newSqlStmt;
  }

  /**
   * Returns true if an object with the given id exists in the database.
   * 
   * @param id
   * @param tableName
   * @return true if it exists, false otherwise.
   */
  public boolean doesObjectExist(String id, String tableName)
  {
    ResultSet resultSet = this.selectFromWhere(EntityDAOIF.ID_COLUMN, tableName, EntityDAOIF.ID_COLUMN + "='" + id + "'");

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
    return "SUBSTRING(" + stringName + ", " + position + ", " + length + ")";
  }

  /**
   * Builds a database specific trim function call string.
   * 
   * @param columnName
   *          name of the column to trim.
   * @return a database specific trim function call string.
   */
  public String buildTrimFunctionCall(String columnName)
  {
    return "TRIM(" + columnName + ")";
  }

  /**
   * Builds a database specific left trim function call string.
   * 
   * @param columnName
   *          name of the column to trim.
   * @return a database specific left trim function call string.
   */
  public String buildLeftTrimFunctionCall(String columnName)
  {
    return "LTRIM(" + columnName + ")";
  }

  /**
   * Builds a database specific right trim function call string.
   * 
   * @param columnName
   *          name of the column to trim.
   * @return a database specific right trim function call string.
   */
  public String buildRightTrimFunctionCall(String columnName)
  {
    return "RTRIM(" + columnName + ")";
  }

  /**
   * Builds a database specific uppercase function call string.
   * 
   * @param columnName
   *          name of the column to uppercase.
   * @return a database specific uppercase function call string.
   */
  public String buildUpperCaseFunctionCall(String columnName)
  {
    return "UPPER(" + columnName + ")";
  }

  /**
   * Builds a database specific lowercase function call string.
   * 
   * @param columnName
   *          name of the column to lowercase.
   * @return a database specific lowercase function call string.
   */
  public String buildLowerCaseFunctionCall(String columnName)
  {
    return "LOWER(" + columnName + ")";
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
    return concatString1 + " || " + concatString2;
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
  public String buildStringPositionFunctionCall(String stringToFind, String searchString)
  {
    return "POSITION('" + stringToFind + "' IN  " + searchString + ")";
  }

  /**
   * Builds a database specific length function call.
   * 
   * @param columnName
   *          column to calculate the length on.
   * @return database specific length function call.
   */
  public String buildLenthFunctionCall(String columnName)
  {
    return "LENGTH(" + columnName + ")";
  }

  /**
   * Create the VERSION table. This must be done before building the metadata.
   */
  public abstract void buildDynamicPropertiesTable();

  /**
   * 
   */
  public abstract void buildChangelogTable();

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
  public int updateClassAndSource(String mdTypeId, String table, String classColumnName, byte[] classBytes, String sourceColumnName, String source, Connection conn)
  {
    int written = 0;
    Statement statement = null;
    ResultSet resultSet = null;

    try
    {
      // clear the blobs
      this.truncateBlob(table, classColumnName, mdTypeId, 0, conn);

      // get the blob
      statement = conn.createStatement();
      String select = "SELECT " + classColumnName + " FROM " + table + " WHERE " + EntityDAOIF.ID_COLUMN + " = '" + mdTypeId + "'";
      String update = "UPDATE " + table + " SET " + classColumnName + " = ?, " + sourceColumnName + " = ?  WHERE " + EntityInfo.ID + " = '" + mdTypeId + "'";
      resultSet = statement.executeQuery(select);
      boolean resultSetFound = resultSet.next();
      if (!resultSetFound)
      {
        return 0;
      }

      Blob classBlob = resultSet.getBlob(classColumnName);

      PreparedStatement prepared = conn.prepareStatement(update);
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
    int written = 0;
    Statement statement = null;
    ResultSet resultSet = null;

    try
    {
      // clear the blobs
      this.truncateBlob(table, serverClassesColumnName, mdFacadeId, 0, conn);
      this.truncateBlob(table, commonClassesColumnName, mdFacadeId, 0, conn);
      this.truncateBlob(table, clientClassesColumnName, mdFacadeId, 0, conn);

      // get the blob
      statement = conn.createStatement();
      String select = "SELECT " + serverClassesColumnName + ", " + commonClassesColumnName + ", " + clientClassesColumnName + " FROM " + table + " WHERE " + EntityDAOIF.ID_COLUMN + " = '" + mdFacadeId + "'";
      String update = "UPDATE " + table + " SET " + serverClassesColumnName + " = ?, " + commonClassesColumnName + " = ?, " + clientClassesColumnName + " = ? " + " WHERE " + EntityDAOIF.ID_COLUMN + " = '" + mdFacadeId + "'";
      resultSet = statement.executeQuery(select);
      boolean resultSetFound = resultSet.next();
      if (!resultSetFound)
      {
        return 0;
      }

      Blob serverClassesBlob = resultSet.getBlob(serverClassesColumnName);
      Blob commonClassesBlob = resultSet.getBlob(commonClassesColumnName);
      Blob clientClassesBlob = resultSet.getBlob(clientClassesColumnName);

      PreparedStatement prepared = conn.prepareStatement(update);
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
   * @param prepared
   *          The statement to add the blob to
   * @param index
   *          The index to add the blob to
   * @param current
   *          The current value of the blob
   * @param newBytes
   *          The new bytes to write to the blob
   * @return
   * @throws SQLException
   */
  private static int addBlobToStatement(PreparedStatement prepared, int index, Blob current, byte[] newBytes) throws SQLException
  {
    int written = 0;

    if (current == null)
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
  public byte[] getMdTypeBaseClass(String mdTypeId, Connection conn)
  {
    String columnName = MdTypeDAOIF.BASE_CLASS_COLUMN;
    String table = MdTypeDAOIF.TABLE;

    return this.getBlobAsBytes(table, columnName, mdTypeId, conn);
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
  public String getMdTypeBaseSource(String mdTypeId, Connection conn)
  {
    return this.getSourceField(mdTypeId, conn, MdTypeDAOIF.TABLE, MdTypeDAOIF.BASE_SOURCE_COLUMN);
  }

  /**
   * This is a special method used get the MdType DTO class from the database.
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
  public byte[] getMdTypeDTOclass(String mdTypeId, Connection conn)
  {
    String columnName = MdTypeDAOIF.DTO_BASE_CLASS_COLUMN;
    String table = MdTypeDAOIF.TABLE;

    return this.getBlobAsBytes(table, columnName, mdTypeId, conn);
  }

  /**
   * This is a special method used get the MdType DTO source from the database.
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
  public String getMdTypeDTOsource(String mdTypeId, Connection conn)
  {
    return this.getSourceField(mdTypeId, conn, MdTypeDAOIF.TABLE, MdTypeDAOIF.DTO_BASE_SOURCE_COLUMN);
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
  public byte[] getMdClassDTOStubClass(String mdClassId, Connection conn)
  {
    String columnName = MdClassDAOIF.DTO_STUB_CLASS_COLUMN;
    String table = MdClassDAOIF.TABLE;

    return this.getBlobAsBytes(table, columnName, mdClassId, conn);
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
  public String getMdClassDTOStubSource(String mdClassId, Connection conn)
  {
    return this.getSourceField(mdClassId, conn, MdClassDAOIF.TABLE, MdClassDAOIF.DTO_STUB_SOURCE_COLUMN);
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
  public byte[] getMdClassStubClass(String mdClassId, Connection conn)
  {
    String columnName = MdClassDAOIF.STUB_CLASS_COLUMN;
    String table = MdClassDAOIF.TABLE;

    return this.getBlobAsBytes(table, columnName, mdClassId, conn);
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
  public String getMdClassStubSource(String mdClassId, Connection conn)
  {
    return this.getSourceField(mdClassId, conn, MdClassDAOIF.TABLE, MdClassDAOIF.STUB_SOURCE_COLUMN);
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
  public byte[] getMdEntityQueryAPIclass(String mdEntityId, Connection conn)
  {
    String columnName = MdEntityDAOIF.QUERY_CLASS_COLUMN;
    String table = MdEntityDAOIF.TABLE;

    return this.getBlobAsBytes(table, columnName, mdEntityId, conn);
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
  public byte[] getEntityQueryDTOclass(String mdEntityId, Connection conn)
  {
    String columnName = MdEntityDAOIF.QUERY_DTO_CLASS_COLUMN;
    String table = MdEntityDAOIF.TABLE;

    return this.getBlobAsBytes(table, columnName, mdEntityId, conn);
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
  public byte[] getViewQueryDTOclass(String mdEntityId, Connection conn)
  {
    String columnName = MdViewDAOIF.QUERY_DTO_CLASS_COLUMN;
    String table = MdViewDAOIF.TABLE;

    return this.getBlobAsBytes(table, columnName, mdEntityId, conn);
  }

  /**
   * This is a special method used get the MdView query base source from the
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
  public String getMdViewBaseQuerySource(String mdViewId, Connection conn)
  {
    return this.getSourceField(mdViewId, conn, MdViewDAOIF.TABLE, MdViewDAOIF.QUERY_BASE_SOURCE_COLUMN);
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
  public byte[] getMdViewBaseQueryClass(String mdViewId, Connection conn)
  {
    String columnName = MdViewDAOIF.QUERY_BASE_CLASS_COLUMN;
    String table = MdViewDAOIF.TABLE;

    return this.getBlobAsBytes(table, columnName, mdViewId, conn);
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
  public String getMdViewStubQuerySource(String mdViewId, Connection conn)
  {
    return this.getSourceField(mdViewId, conn, MdViewDAOIF.TABLE, MdViewDAOIF.QUERY_STUB_SOURCE_COLUMN);
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
  public byte[] getMdViewStubQueryClass(String mdViewId, Connection conn)
  {
    String columnName = MdViewDAOIF.QUERY_STUB_CLASS_COLUMN;
    String table = MdViewDAOIF.TABLE;

    return this.getBlobAsBytes(table, columnName, mdViewId, conn);
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
  public String getMdEntityQueryAPIsource(String mdEntityId, Connection conn)
  {
    return this.getSourceField(mdEntityId, conn, MdEntityDAOIF.TABLE, MdEntityDAOIF.QUERY_SOURCE_COLUMN);
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
  public String getEntityQueryDTOsource(String mdEntityId, Connection conn)
  {
    return this.getSourceField(mdEntityId, conn, MdEntityDAOIF.TABLE, MdEntityDAOIF.QUERY_DTO_SOURCE_COLUMN);
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
  public String getViewQueryDTOsource(String mdViewId, Connection conn)
  {
    return this.getSourceField(mdViewId, conn, MdViewDAOIF.TABLE, MdViewDAOIF.QUERY_DTO_SOURCE_COLUMN);
  }

  /**
   * This is a special method used get the source from the database. This method
   * is used when a transaction is rolled back to restore the source on the file
   * system from the database. It is used only within the TransactionManagement
   * aspect, hence it takes a JDBC connection object as a parameter. It is up to
   * the client to close the connection object.
   * 
   * <b>Precondition: </b>Assumes an MdType exists in the database with the
   * given id.
   * 
   * @param mdTypeId
   * @param conn
   * @param table
   * @param columnName
   */
  public String getSourceField(String mdTypeId, Connection conn, String table, String columnName)
  {
    String stubSource = null;
    Statement statement = null;
    ResultSet resultSet = null;

    try
    {
      statement = conn.createStatement();
      String select = "SELECT " + columnName + " FROM " + table + " WHERE " + EntityDAOIF.ID_COLUMN + " = '" + mdTypeId + "'";
      resultSet = statement.executeQuery(select);

      if (resultSet.next())
      {
        stubSource = resultSet.getString(columnName);
      }
      else
      {
        stubSource = "";
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
    return stubSource;
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
  public String getEnumCacheFieldInTable(String tableName, String columnName, String entityId)
  {
    Connection conn = Database.getConnection();
    Statement statement = null;
    ResultSet resultSet = null;
    try
    {
      // get the blob
      statement = conn.createStatement();
      String query = "SELECT " + columnName + " FROM " + tableName + " WHERE " + EntityDAOIF.ID_COLUMN + " = '" + entityId + "'";
      resultSet = statement.executeQuery(query);

      String databaseCachedEnumIds = "";

      while (resultSet.next())
      {
        databaseCachedEnumIds = resultSet.getString(1);

        if (databaseCachedEnumIds == null)
        {
          databaseCachedEnumIds = "";
        }

        return databaseCachedEnumIds;

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

    return "";
  }

  /**
   * Returns the SQL that inserts a mapping in the given enumeration table
   * between the given set id and the given enumeration item id.
   * 
   * @param enumTableName
   * @param setId
   * @param enumItemID
   */
  public String buildAddItemStatement(String enumTableName, String setId, String enumItemID)
  {
    LinkedList<String> columnNames = new LinkedList<String>();
    columnNames.add(MdEnumerationDAOIF.SET_ID_COLUMN);
    columnNames.add(MdEnumerationDAOIF.ITEM_ID_COLUMN);

    LinkedList<String> values = new LinkedList<String>();
    values.add("'" + setId + "'");
    values.add("'" + enumItemID + "'");

    String sqlStmt = "INSERT INTO " + enumTableName + " (" + MdEnumerationDAOIF.SET_ID_COLUMN + ", " + MdEnumerationDAOIF.ITEM_ID_COLUMN + ") " + " VALUES " + " ('" + setId + "', '" + enumItemID + "')";

    return sqlStmt;
  }

  /**
   * Returns the SQL that updates an enum item id with the provided new enum
   * item id.
   * 
   * @param enumTableName
   * @param oldEnumItemId
   * @param newEnumItemId
   */
  public String buildUpdateEnumItemStatement(String enumTableName, String oldEnumItemId, String newEnumItemId)
  {
    String sqlStmt = "UPDATE " + enumTableName + " SET " + MdEnumerationDAOIF.ITEM_ID_COLUMN + " = '" + newEnumItemId + "' " + " WHERE " + MdEnumerationDAOIF.ITEM_ID_COLUMN + " = '" + oldEnumItemId + "' ";

    if (Database.loggingDMLandDDLstatements())
    {
      System.out.println(sqlStmt + ";");
    }

    return sqlStmt;
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
  public StringBuffer buildRowRangeRestriction(StringBuffer sqlStmt, int limit, int skip, String selectClauseAttributes, String orderByClause)
  {
    sqlStmt.append("\n");
    sqlStmt.append(orderByClause);
    sqlStmt.append("\nLIMIT " + limit + " OFFSET " + skip);
    return sqlStmt;
  }

  // //////////////////////////////////////////////////////////////
  // ////// Enumerations
  // //////////////////////////////////////////////////////////////

  /**
   * Returns the ids of the enumeration items that are mapped to the given
   * setId.
   */
  public Set<String> getEnumItemIds(String enumTableName, String setId)
  {
    Set<String> enumIdSet = new HashSet<String>();

    LinkedList<String> tables = new LinkedList<String>();
    tables.add(enumTableName);

    LinkedList<String> columnNames = new LinkedList<String>();
    columnNames.add(MdEnumerationDAOIF.ITEM_ID_COLUMN);

    LinkedList<String> conditions = new LinkedList<String>();
    if (!setId.trim().equals(""))
    {
      conditions.add(MdEnumerationDAOIF.SET_ID_COLUMN + " ='" + setId + "'");
    }

    ResultSet resultSet = this.select(columnNames, tables, conditions);

    try
    {
      while (resultSet.next())
      {
        enumIdSet.add("" + resultSet.getString(MdEnumerationDAOIF.ITEM_ID_COLUMN));
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

    return enumIdSet;
  }

  /**
   * Removes the enumeration item from the enumeration mapping table.
   * 
   * @param enumTableName
   * @param enumItemID
   */
  public void deleteEumerationItemFromLinkTable(String enumTableName, String enumItemID)
  {
    this.deleteWhere(enumTableName, MdEnumerationDAOIF.ITEM_ID_COLUMN + "='" + enumItemID + "'");
  }

  /**
   * Deletes all instances of the setId from the given enumeration mapping
   * table.
   * 
   * @param enumTableName
   * @param setId
   */
  public void deleteSetIdFromLinkTable(String enumTableName, String setId)
  {
    this.deleteWhere(enumTableName, MdEnumerationDAOIF.SET_ID_COLUMN + "='" + setId + "'");
  }

  /**
   * Deletes all mapping instances for a particular attribute.
   * 
   * @param enumTableName
   * @param tableName
   * @param columnName
   */
  public void deleteAllEnumAttributeInstances(String enumTableName, String tableName, String columnName)
  {
    String dmlStmt = "DELETE FROM " + enumTableName + " \n" + "  WHERE " + MdEnumerationDAOIF.SET_ID_COLUMN + " IN \n " + "    (SELECT " + columnName + " FROM " + tableName + ")";

    this.parseAndExecute(dmlStmt);
  }

  /**
   * Gets a list of all enumerated types in the system.
   * 
   * @return list of all enumerated types in the system.
   */
  public List<String> getAllEnumTypes()
  {
    List<String> mdEnumFields = new LinkedList<String>();
    mdEnumFields.add(MdTypeDAOIF.TYPE_NAME_COLUMN);
    mdEnumFields.add(MdTypeDAOIF.PACKAGE_NAME_COLUMN);

    List<String> mdEnumTable = new LinkedList<String>();
    mdEnumTable.add(MdEnumerationDAOIF.TABLE);
    mdEnumTable.add(MdTypeDAOIF.TABLE);

    List<String> conditions = new LinkedList<String>();
    conditions.add(MdEnumerationDAOIF.TABLE + "." + EntityDAOIF.ID_COLUMN + " = " + MdTypeDAOIF.TABLE + "." + EntityDAOIF.ID_COLUMN);

    ResultSet resultSet = this.query(this.selectClause(mdEnumFields, mdEnumTable, conditions));

    List<String> returnList = new LinkedList<String>();

    try
    {
      while (resultSet.next())
      {
        String className = resultSet.getString(MdTypeDAOIF.TYPE_NAME_COLUMN);
        String packageName = resultSet.getString(MdTypeDAOIF.PACKAGE_NAME_COLUMN);
        String type = EntityDAOFactory.buildType(packageName, className);
        returnList.add(type);
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
    return returnList;
  }

  /**
   * Gets a list of all tables used by the application, including runway
   * metadata.
   * 
   * @return list of all tables used by the application, including runway
   *         metadata.
   */
  public List<String> getAllApplicationTables()
  {
    List<String> returnList = new LinkedList<String>();

    returnList.addAll(this.getAllEntityTables());

    returnList.addAll(this.getAllEnumerationTables());

    return returnList;
  }

  /**
   * Gets the schema/namespace used by the application
   * 
   * @return schema/namespace used by the application
   */
  public String getApplicationNamespace()
  {
    return DatabaseProperties.getNamespace();
  }

  /**
   * Gets a list of all entity tables.
   * 
   * @return list of all entity tables.
   */
  public List<String> getAllEntityTables()
  {
    List<String> returnList = new LinkedList<String>();

    List<String> tableFields = new LinkedList<String>();
    tableFields.add(MdEntityDAOIF.TABLE_NAME_COLUMN);

    List<String> mdEntityTable = new LinkedList<String>();
    mdEntityTable.add(MdEntityDAOIF.TABLE);

    List<String> conditions = new LinkedList<String>();

    ResultSet resultSet = this.query(this.selectClause(tableFields, mdEntityTable, conditions));

    try
    {
      while (resultSet.next())
      {
        String tableName = resultSet.getString(MdEntityDAOIF.TABLE_NAME_COLUMN);
        returnList.add(tableName);
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

    return returnList;
  }

  /**
   * Gets a list of all enumeration tables.
   * 
   * @return list of all enumeration tables.
   */
  public List<String> getAllEnumerationTables()
  {
    List<String> returnList = new LinkedList<String>();

    List<String> tableFields = new LinkedList<String>();
    tableFields.add(MdEnumerationDAOIF.TABLE_NAME_COLUMN);

    List<String> mdEnumTable = new LinkedList<String>();
    mdEnumTable.add(MdEnumerationDAOIF.TABLE);

    List<String> conditions = new LinkedList<String>();

    ResultSet resultSet = this.query(this.selectClause(tableFields, mdEnumTable, conditions));

    try
    {
      while (resultSet.next())
      {
        String tableName = resultSet.getString(MdEnumerationDAOIF.TABLE_NAME_COLUMN);
        returnList.add(tableName);
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
    return returnList;
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
   * @return
   */
  public abstract long getChildCountForParent(String parent_id, String relationshipTableName);

  /**
   * Returns the number of distinct parent instances for a given child of the
   * given relationship type.
   * 
   * @param child_id
   * @param relationshipTableName
   * @return
   */
  public abstract long getParentCountForChild(String child_id, String relationshipTableName);

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
  public int numberOfRelInstances(MdRelationshipDAOIF mdRelationship)
  {
    String columnAlias = this.formatColumnAlias("countnum");

    ResultSet resultSet = this.selectFrom("COUNT(*) " + columnAlias, mdRelationship.getTableName());

    int result = 0;

    try
    {
      if (resultSet.next())
      {
        result = resultSet.getInt("countnum");
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

    return result;
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
  public abstract String backup(List<String> tableNames, String backupFileLocation, String backupFileRootName, PrintStream out, PrintStream errOut, boolean dropSchema);

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
  public abstract String backup(String namespace, String backupFileLocation, String backupFileRootName, PrintStream out, PrintStream errOut, boolean dropSchema);

  /**
   * Imports the given SQL file into the database
   * 
   * @param restoreSQLFile
   * @param printStream
   */
  public abstract void importFromSQL(String restoreSQLFile, PrintStream out, PrintStream errOut);

  /**
   * Drops all of the tables given in the list. This method does not use the
   * command pattern.
   * 
   * @param tableNames
   *          list of tables to drop.
   */
  public void dropTables(List<String> tableNames)
  {
    for (String tableName : tableNames)
    {
      String statement = "DROP TABLE IF EXISTS " + tableName;
      this.execute(statement);
    }
  }

  /**
   * Drops all of the tables given in the list. This method does not use the
   * command pattern.
   * 
   * @param tableNames
   *          list of tables to drop.
   */
  public void cascadeDropTables(List<String> tableNames)
  {
    for (String tableName : tableNames)
    {
      String statement = "DROP TABLE IF EXISTS " + tableName + " CASCADE";
      this.execute(statement);
    }
  }

  /**
   * Drops all of the views given in the list. This method does not use the
   * command pattern.
   * 
   * @param viewNames
   *          list of views to drop.
   */
  public void dropViews(List<String> viewNames)
  {
    for (String view : viewNames)
    {
      String statement = "DROP VIEW IF EXISTS " + view;
      this.execute(statement);
    }
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
  public void addGeometryColumn(String tableName, String columnName, int srid, String geometryType, int dimension)
  {
    String errMsg = this.getClass().getName() + ".addGeometryColumn() method is not supported for this database";
    throw new ForbiddenMethodException(errMsg);
  }

  /**
   * Drops a geometry column in the database.
   * 
   * @param tableName
   * @param columnName
   */
  public void dropGeometryColumn(String tableName, String columnName)
  {
    String errMsg = this.getClass().getName() + ".addGeometryColumn() method is not supported for this database";
    throw new ForbiddenMethodException(errMsg);
  }

  /**
   * Returns the database specific union operator.
   */
  public String getUnionOperator()
  {
    return "UNION";
  }

  /**
   * Returns the database specific union all operator.
   */
  public String getUnionAllOperator()
  {
    return "UNION ALL";
  }

  /**
   * Returns the database specific minus operator.
   */
  public String getMinusOperator()
  {
    return "MINUS";
  }

  /**
   * Returns the database specific intersect operator.
   */
  public String getIntersectOperator()
  {
    return "INTERSECT";
  }

  /**
   * @return <code>true</code> if the database allows nonrequired columns to
   *         enforce uniqueness
   */
  public boolean allowsUniqueNonRequiredColumns()
  {
    return false;
  }

  /**
   * @param value
   * @param attributeIF
   * @return
   */
  public void validateClobLength(String value, AttributeIF attributeIF)
  {
    if (value.length() > MAX_LENGTH)
    {
      String error = "Attribute [" + attributeIF.getName() + "] on type [" + attributeIF.getDefiningClassType() + "] is too long.";
      throw new AttributeLengthCharacterException(error, attributeIF, MAX_LENGTH);
    }
  }

  public List<Changelog> getChangelogEntries()
  {
    try
    {
      List<Changelog> entries = new LinkedList<Changelog>();

      Calendar calendar = GregorianCalendar.getInstance(Locale.US);

      List<String> columnNames = new LinkedList<String>();
      columnNames.add("change_number");
      columnNames.add("complete_dt");
      columnNames.add("applied_by");
      columnNames.add("description");

      List<String> tables = new LinkedList<String>();
      tables.add("changelog");

      ResultSet results = null;

      try
      {
        results = this.select(columnNames, tables, new LinkedList<String>());

        while (results.next())
        {
          long changeNumber = results.getLong(1);
          Timestamp completeDate = results.getTimestamp(2, calendar);
          String appliedBy = results.getString(3);
          String description = results.getString(4);

          Changelog log = new Changelog();
          log.setChangeNumber(changeNumber);
          log.setCompleteDate(new Date(completeDate.getTime()));
          log.setAppliedBy(appliedBy);
          log.setDescription(description);

          entries.add(log);
        }

        Collections.sort(entries, new Comparator<Changelog>()
        {

          @Override
          public int compare(Changelog o1, Changelog o2)
          {
            return o1.getChangeNumber().compareTo(o2.getChangeNumber());
          }
        });

        return entries;
      }
      finally
      {
        if (results != null)
        {
          results.close();
        }
      }
    }
    catch (SQLException e)
    {
      throw new DatabaseException(e);
    }
  }

  public List<String> getReferencingViews(MdElementDAOIF mdElement)
  {
    throw new UnsupportedOperationException();
  }

  public int getMaxColumnSize()
  {
    return 28;
  }
  
  /**
   * Casts the given sql to a decimal. 
   * 
   * Note, this has only been tested against Postgres
   * 
   * @param sql
   * @return Casts the given sql to a decimal. 
   */
  public String castToDecimal(String sql)
  {
    return sql+"::dec";
  }

}
