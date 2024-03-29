/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.build;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.constants.DatabaseProperties;
import com.runwaysdk.dataaccess.CoreException;
import com.runwaysdk.dataaccess.InstallerCP;
import com.runwaysdk.dataaccess.io.XMLImporter;
import com.runwaysdk.resource.ClasspathResource;

/**
 * Provides utility methods for checking status of a database and bootstrapping
 * Runway into it.
 * 
 * @author rrowlands
 */
public class DatabaseBootstrapper
{
  private static Logger logger = LoggerFactory.getLogger(DatabaseBootstrapper.class);

  public static void main(String[] args)
  {
    System.out.println(ClasspathResource.getResourcesInPackage("domain"));
  }

  /**
   * Checks to see if Runway is installed in the current database. If it is not
   * installed (or clean) is specified, Runway will be bootstrapped (and
   * metadata.xml imported) into the database. If the database or the user does
   * not exist it will also be created.
   * 
   * Do not invoke this method inside a request or transaction.
   */
  public static void bootstrap(String rootUser, String rootPass, String template, Boolean clean)
  {
    if (template == null)
    {
      template = "postgres";
    }

    if (clean)
    {
      doBootstrap(rootUser, rootPass, template, clean);
    }
    else
    {
      if (isRunwayInstalled(rootUser, rootPass, template))
      {
        throw new CoreException("Detected an existing installation of Runway and clean is not set to true. Aborting.");
      }
      else
      {
        doBootstrap(rootUser, rootPass, template, clean);
      }
    }
  }

  private static void doBootstrap(String rootUser, String rootPass, String template, Boolean clean)
  {
    if (clean)
    {
      logger.info("Destroying ALL data in the [" + DatabaseProperties.getDatabaseName() + "] database and reinstalling Runway.");
    }
    else
    {
      logger.info("Bootstrapping Runway into an empty database.");
    }

    if (rootUser == null)
    {
      rootUser = DatabaseProperties.getRootUser();
    }
    if (rootPass == null)
    {
      rootPass = DatabaseProperties.getRootPassword();
    }

    if (rootUser != null && rootUser.length() > 0 && rootPass != null && rootPass.length() > 0)
    {
      // This is what actually drops the database and then creates it clean,
      // along with the app user
      com.runwaysdk.dataaccess.database.Database.initialSetup(rootUser, rootPass, template);
    }

    InputStream schema = Thread.currentThread().getContextClassLoader().getResourceAsStream("com/runwaysdk/resources/xsd/schema.xsd");

    try
    {
      InputStream[] xmlFilesIS = InstallerCP.buildMetadataInputStreamList();

      XMLImporter importer = new XMLImporter(schema, xmlFilesIS);
      importer.toDatabase();
    }
    catch (IOException e)
    {
      throw new CoreException(e);
    }
    finally
    {
      try
      {
        schema.close();
      }
      catch (IOException e)
      {
        throw new CoreException(e);
      }
    }
  }

  public static Boolean isRunwayInstalled(String rootUser, String rootPass, String rootDb)
  {
    try
    {
      if (rootDb == null)
      {
        rootDb = "postgres";
      }

      if (rootUser != null && rootPass != null)
      {
        if (!rawDatabaseExistCheck(DatabaseProperties.getDatabaseName(), rootUser, rootPass, rootDb))
        {
          return false;
        }

        if (!rawDatabaseUserExistCheck(DatabaseProperties.getUser(), rootUser, rootPass, rootDb))
        {
          return false;
        }
      }

      if (!rawTableExistCheck("md_class"))
      {
        return false;
      }
    }
    catch (Throwable ex)
    {
      return false;
    }

    return true;
  }

  /**
   * Returns true if a database user exists with the provided name.
   */
  public static Boolean rawDatabaseExistCheck(String dbName, String rootUser, String rootPass, String rootDb)
  {
    // First check to make sure the user exists
    final String sqlStmt = "SELECT datname FROM pg_catalog.pg_database WHERE lower(datname) = lower(?);";

    boolean returnResult = false;

    try (Connection conx = com.runwaysdk.dataaccess.database.Database.getConnectionRoot(rootUser, rootPass, rootDb))
    {
      ResultSet resultSet = null;

      try (PreparedStatement statement = conx.prepareStatement(sqlStmt))
      {
        statement.setString(1, dbName);

        boolean isResultSet = statement.execute();

        while (true)
        {
          if (isResultSet)
          {
            resultSet = statement.getResultSet();
            break;
          }
          else if (statement.getUpdateCount() == -1)
          {
            throw new SQLException("No results were returned by the query.");
          }

          isResultSet = statement.getMoreResults();
        }

        if (resultSet.next())
        {
          returnResult = true;
        }
      }
    }
    catch (SQLException ex)
    {
      com.runwaysdk.dataaccess.database.Database.throwDatabaseException(ex);
    }

    com.runwaysdk.dataaccess.database.Database.close();

    return returnResult;
  }

  /**
   * Returns true if a database user exists with the provided name.
   */
  public static Boolean rawDatabaseUserExistCheck(String userName, String rootUser, String rootPass, String rootDb)
  {
    // First check to make sure the user exists
    final String sqlStmt = "SELECT 1 FROM pg_roles WHERE rolname=?";

    boolean returnResult = false;

    try (Connection conx = com.runwaysdk.dataaccess.database.Database.getConnectionRoot(rootUser, rootPass, rootDb))
    {
      ResultSet resultSet = null;

      try (PreparedStatement statement = conx.prepareStatement(sqlStmt))
      {
        statement.setString(1, userName);

        boolean isResultSet = statement.execute();

        while (true)
        {
          if (isResultSet)
          {
            resultSet = statement.getResultSet();
            break;
          }
          else if (statement.getUpdateCount() == -1)
          {
            throw new SQLException("No results were returned by the query.");
          }

          isResultSet = statement.getMoreResults();
        }

        if (resultSet.next())
        {
          returnResult = true;
        }
      }
    }
    catch (SQLException ex)
    {
      com.runwaysdk.dataaccess.database.Database.throwDatabaseException(ex);
    }

    com.runwaysdk.dataaccess.database.Database.close();

    return returnResult;
  }

  /**
   * Returns true if the specified table exists in the database.
   * 
   * @param tableName
   * @return
   */
  public static Boolean rawTableExistCheck(String tableName)
  {
    String sqlStmt = "SELECT relname FROM pg_class WHERE relname = ?";

    boolean returnResult = false;

    try (Connection conx = com.runwaysdk.dataaccess.database.Database.getConnectionRaw())
    {
      ResultSet resultSet = null;

      try (PreparedStatement statement = conx.prepareStatement(sqlStmt))
      {
        statement.setString(1, tableName);

        boolean isResultSet = statement.execute();

        while (true)
        {
          if (isResultSet)
          {
            resultSet = statement.getResultSet();
            break;
          }
          else if (statement.getUpdateCount() == -1)
          {
            throw new SQLException("No results were returned by the query.");
          }

          isResultSet = statement.getMoreResults();
        }

        if (resultSet.next())
        {
          returnResult = true;
        }
      }
    }
    catch (SQLException ex)
    {
      com.runwaysdk.dataaccess.database.Database.throwDatabaseException(ex);
    }

    com.runwaysdk.dataaccess.database.Database.close();

    return returnResult;
  }
}
