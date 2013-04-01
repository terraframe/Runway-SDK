/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
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
 ******************************************************************************/
package com.runwaysdk.dataaccess.database;

// import java.sql.PreparedStatement;

/**
 * When a Runway metadata refactor occurs, we must log the DML and DDL statements so that we can
 * build a SQL update script.
 */
public aspect LogDatabaseRefactor
{
//  protected pointcut logPreparedStatements(PreparedStatement preparedStatement)
//  :(call (* java.sql.Statement+.executeUpdate(..)) && target(preparedStatement));
//  before (PreparedStatement preparedStatement)
//  : logPreparedStatements(preparedStatement)
//  {
//    if (Database.loggingDMLandDDLstatements() == true)
//    {
//      System.out.println(preparedStatement.toString());
//    }
//  }

  protected pointcut logStatements(String sqlStmt)
  :(call (* java.sql.Statement+.executeUpdate(String)) && args(sqlStmt));
  before (String sqlStmt)
  : logStatements(sqlStmt)
  {
    if (Database.loggingDMLandDDLstatements() == true)
    {
      System.out.println(sqlStmt+";");
    }
  }

}
