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
package com.runwaysdk.dataaccess.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdControllerDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;

public class ControllerDAOFactory
{

  public static List<String> getAllControllerNames()
  {
    List<String> mdBusinessFields = new LinkedList<String>();
    mdBusinessFields.add(MdTypeDAOIF.TYPE_NAME_COLUMN);
    mdBusinessFields.add(MdTypeDAOIF.PACKAGE_NAME_COLUMN);

    List<String> mdBusinessTable = new LinkedList<String>();
    mdBusinessTable.add(MdControllerDAOIF.TABLE);
    mdBusinessTable.add(MdTypeDAOIF.TABLE);

    List<String> conditions = new LinkedList<String>();
    conditions.add(MdControllerDAOIF.TABLE + "." + EntityDAOIF.ID_COLUMN + " = " + MdTypeDAOIF.TABLE + "."
        + EntityDAOIF.ID_COLUMN);

    ResultSet resultSet = Database.query(Database.selectClause(mdBusinessFields, mdBusinessTable, conditions));

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
  
}
