/**
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
 */
package com.runwaysdk.session;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.aspectj.lang.JoinPoint;

import com.runwaysdk.ProblemIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.DatabaseException;

public class RequestState
{
  private boolean             debug = false;

  // Connection used for DML statements
  private volatile Connection conn;

  private volatile Thread     mainThread;

  protected SessionIF         session;

  protected RequestState()
  {
    this.conn = Database.getConnection();
    this.session = null;
    this.mainThread = Thread.currentThread();
  }

  /**
   * Returns the main thread.
   * 
   * @return the main thread.
   */
  public Thread getMainThread()
  {
    return this.mainThread;
  }

  public boolean isDebug()
  {
    return this.debug;
  }

  protected Connection getDatabaseConnection()
  {
    return this.conn;
  }
  
  public synchronized Boolean isSessionNull() {
    return this.session == null;
  }
  
  protected synchronized SessionIF getSession()
  {
    return this.session;
  }

  protected synchronized void setSession(SessionIF _sessionIF)
  {
    this.session = _sessionIF;
  }

  /**
   * Commits the DML database connection.
   * 
   * @param joinPoint
   */
  protected synchronized void commitConnection(JoinPoint joinPoint)
  {
    if (conn != null)
    {
      try
      {
        // PostgreSQL requires a commit to close the transaction, otherwise
        // other connections might
        // hang when executing a DDL connection on a table if this connection
        // performed a query
        // on that table
        if (!conn.isClosed())
        {
          conn.commit();

          if (this.isDebug())
          {
            System.out.println("\n-----------close connection " + conn + "---" + joinPoint + "--------");
          }
        }
      }
      catch (SQLException ex)
      {
        throw new DatabaseException(ex);
      }
    }
  }

  /**
   * Commits and closes the DML database connection.
   * 
   * @param joinPoint
   */
  protected synchronized void commitAndCloseConnection(JoinPoint joinPoint)
  {
    if (conn != null)
    {
      try
      {
        // PostgreSQL requires a commit to close the transaction, otherwise
        // other connections might
        // hang when executing a DDL connection on a table if this connection
        // performed a query
        // on that table
        if (!conn.isClosed())
        {
          conn.commit();

          if (this.isDebug())
          {
            System.out.println("\n-----------close connection " + conn + "---" + joinPoint + "--------");
          }

          conn.close();
        }
      }
      catch (SQLException ex)
      {
        throw new DatabaseException(ex);
      }
    }
  }

  /**
   * Rolls back the DML database connection;
   * 
   * @param joinPoint
   */
  protected synchronized void rollbackConnection(JoinPoint joinPoint)
  {
    if (conn != null)
    {
      try
      {
        if (!conn.isClosed())
        {
          conn.rollback();

          if (this.isDebug())
          {
            System.out.println("\n-----------rolling back connection " + conn + "---" + joinPoint + "--------");
          }
        }
      }
      catch (SQLException ex)
      {
        throw new DatabaseException(ex);
      }
    }
  }

  /**
   * Rolls back and closes the DML database connection;
   * 
   * @param joinPoint
   */
  protected synchronized void rollbackAndCloseConnection(JoinPoint joinPoint)
  {
    if (conn != null)
    {
      try
      {
        if (!conn.isClosed())
        {
          conn.rollback();

          if (this.isDebug())
          {
            System.out.println("\n-----------rolling back connection " + conn + "---" + joinPoint + "--------");
          }

          conn.close();
        }
      }
      catch (SQLException ex)
      {
        throw new DatabaseException(ex);
      }
    }
  }

  /**
   * Returns the request state of this request.
   * 
   * @return request state of this request.
   */
  public static RequestState getCurrentRequestState()
  {
    return null;
  }

  /**
   * Returns list of problems that have occurred thus far in the transaction. An
   * aspect will actually provide the list. If the session is not in a
   * transaction, then the list will be empty.
   * 
   * @return list of problems that have occurred thus far in the transaction.
   */
  public static List<ProblemIF> getProblemsInCurrentRequest()
  {
    return new LinkedList<ProblemIF>();
  }

}
