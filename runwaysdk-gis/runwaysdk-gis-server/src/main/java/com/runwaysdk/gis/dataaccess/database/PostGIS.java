/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.gis.dataaccess.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.sql.DataSource;

import org.postgresql.PGConnection;
import org.postgresql.ds.PGSimpleDataSource;
import org.postgresql.ds.common.BaseDataSource;

import com.google.inject.Inject;
import com.runwaysdk.constants.DatabaseProperties;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.database.general.PostgreSQL;
import com.runwaysdk.gis.constants.MdAttributeLineStringInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiLineStringInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiPointInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiPolygonInfo;
import com.runwaysdk.gis.constants.MdAttributePointInfo;
import com.runwaysdk.gis.constants.MdAttributePolygonInfo;
import com.vividsolutions.jts.geom.Geometry;

public class PostGIS extends PostgreSQL
{
  public static final String SPATIAL_REF_SYS  = "SPATIAL_REF_SYS";

  public static final String GEOMETRY_COLUMNS = "GEOMETRY_COLUMNS";

  /**
   * Initialize the datasource to point to a PostgreSQL database.
   */
  @Inject
  public PostGIS()
  {
    super();
  }

  /**
   * Installs the runway core. This entails creating a new database and a user
   * for the runway to log in with.
   */
  public void initialSetup(String rootUser, String rootPass, String rootDb)
  {
    super.initialSetup(rootUser, rootPass, rootDb);

    /*
     * GRANT { { SELECT | INSERT | UPDATE | DELETE | RULE | REFERENCES | TRIGGER
     * } [,...] | ALL [ PRIVILEGES ] } ON [ TABLE ] tablename [, ...] TO {
     * username | GROUP groupname | PUBLIC } [, ...] [ WITH GRANT OPTION ]
     */

    String dbName = DatabaseProperties.getDatabaseName();

    BaseDataSource pgRootDataSource = new PGSimpleDataSource();
    pgRootDataSource.setServerName(DatabaseProperties.getServerName());
    pgRootDataSource.setPortNumber(DatabaseProperties.getPort());

    pgRootDataSource.setUser(rootUser);
    pgRootDataSource.setPassword(rootPass);

    // Log on as root to the newly created database.
    pgRootDataSource.setDatabaseName(dbName);

    DataSource tempDataSource = (DataSource) pgRootDataSource;

    String userName = DatabaseProperties.getUser();

    LinkedList<String> statements = new LinkedList<String>();

    // statements.add("GRANT SELECT, INSERT, UPDATE, DELETE ON "+SPATIAL_REF_SYS+" TO "
    // + userName);
    // statements.add("GRANT SELECT, INSERT, UPDATE, DELETE ON "+GEOMETRY_COLUMNS+" TO "
    // + userName);

    statements.add("CREATE EXTENSION IF NOT EXISTS postgis");
    statements.add("GRANT ALL ON TABLE " + SPATIAL_REF_SYS + " TO " + userName);
    statements.add("GRANT ALL ON TABLE " + GEOMETRY_COLUMNS + " TO " + userName);

    Connection tempConn = null;

    try
    {
      tempConn = tempDataSource.getConnection();
    }
    catch (SQLException ex)
    {
      this.throwDatabaseException(ex);
    }

    this.executeAsRoot(tempConn, statements, true);

  }

  // @Override
  // protected void revokePermissions(String userName)
  // {
  // try
  // {
  // LinkedList<String> statements = new LinkedList<String>();
  // statements.add("REVOKE ALL ON "+ SPATIAL_REF_SYS+" FROM " + userName);
  // statements.add("REVOKE ALL ON "+ GEOMETRY_COLUMNS+" FROM " + userName);
  // executeAsRoot(statements, true);
  // }
  // catch (DatabaseException e)
  // {
  // // This happens if the user doesn't exist to be dropped. Keep going.
  // }
  // }

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
    Connection conn = super.getConnection();

    PGConnection pgConn = (PGConnection) conn;

    return (Connection) mapColumnTypes(pgConn);
  }

  public static PGConnection mapColumnTypes(PGConnection pgConn)
  {
    try
    {
      pgConn.addDataType("geometry", org.postgis.jts.JtsGeometry.class);
      pgConn.addDataType("box3d", org.postgis.PGbox3d.class);
      pgConn.addDataType("box2d", org.postgis.PGbox2d.class);
    }
    catch (SQLException e)
    {
      String errMsg = e.getMessage();
      throw new ProgrammingErrorException(errMsg, e);
    }

    return pgConn;
  }
  
  /**
   * Drops and then remakes the application schema, effectively dropping all tables. If the database is spatially 
   * enabled and the application schema is 'public' then PostGIS will be recreated as well.
   */
  public void dropAll()
  {
    String schema = this.getApplicationNamespace();
    
    this.parseAndExecute("DROP SCHEMA " + schema + " CASCADE;\n" + 
        "CREATE SCHEMA " + schema + ";\n" + 
        "GRANT ALL ON SCHEMA " + schema + " TO postgres;");
    
    if (schema.equals("public"))
    {
      this.parseAndExecute("CREATE EXTENSION postgis");
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
    String statement = "SELECT AddGeometryColumn('" + tableName + "', '" + columnName.toLowerCase() + "', " + srid + ", '" + geometryType + "', " + dimension + " )";

    this.query(statement);
  }

  /**
   * Drops a geometry column in the database.
   * 
   * @param tableName
   * @param columnName
   */
  public void dropGeometryColumn(String tableName, String columnName)
  {
    String statement = "SELECT DropGeometryColumn('" + tableName + "', '" + columnName.toLowerCase() + "')";

    this.query(statement);
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
  public void bindPreparedStatementValue(PreparedStatement prepStmt, int index, Object value, String dataType)
  {
    try
    {
      if (dataType.equals(MdAttributePointInfo.CLASS) || dataType.equals(MdAttributeLineStringInfo.CLASS) || dataType.equals(MdAttributePolygonInfo.CLASS) || dataType.equals(MdAttributeMultiPointInfo.CLASS) || dataType.equals(MdAttributeMultiLineStringInfo.CLASS) || dataType.equals(MdAttributeMultiPolygonInfo.CLASS))
      {
        if (value == null)
        {
          prepStmt.setNull(index, java.sql.Types.OTHER, "Geometry");
        }
        else
        {
          Geometry geometry = (Geometry) value;
          String toText = "SRID=" + geometry.getSRID() + ";" + geometry.toText();
          prepStmt.setString(index, toText);
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

    if (dataType.equals(MdAttributePointInfo.CLASS) || dataType.equals(MdAttributeLineStringInfo.CLASS) || dataType.equals(MdAttributePolygonInfo.CLASS) || dataType.equals(MdAttributeMultiPointInfo.CLASS) || dataType.equals(MdAttributeMultiLineStringInfo.CLASS) || dataType.equals(MdAttributeMultiPolygonInfo.CLASS))
    {
      bogusValue = "ST_GeometryFromText(NULL)";
    }
    else
    {
      return super.formatColumnAlias(_columnAlias, dataType);
    }

    return bogusValue + " " + this.formatColumnAlias(columnAlias);
  }

}
