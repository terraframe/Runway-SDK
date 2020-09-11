/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
import com.runwaysdk.dataaccess.graph.GraphRequest;
import com.runwaysdk.dataaccess.graph.GraphDBService;

public class RequestState
{
  private boolean             debug = false;

  // Connection used for DML statements
  private volatile Connection conn;

  protected GraphRequest      graphDBRequest;

  private volatile Thread     mainThread;

  protected SessionIF         session;

  protected RequestState()
  {
    try
    {
      this.conn = Database.getConnectionRaw();

      this.graphDBRequest = GraphDBService.getInstance().getGraphDBRequest();

      this.session = null;
      this.mainThread = Thread.currentThread();
    }
    catch (RuntimeException e)
    {
      if (this.conn != null)
      {
        try
        {
          this.conn.close();
        }
        catch (SQLException ex)
        {

        }
      }

      if (this.graphDBRequest != null)
      {
        this.graphDBRequest.close();
      }

      throw e;
    }
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

  public GraphRequest getGraphDBRequest()
  {
    return this.graphDBRequest;
  }

  public synchronized Boolean isSessionNull()
  {
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
    // Close the graph db connection, but do not commit it
    this.graphDBRequest.close();

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
    this.graphDBRequest.rollback();

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
    this.graphDBRequest.rollback();
    this.graphDBRequest.close();

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
   * Returns the database connection to the connection pool. This should only be
   * used when the request is not in a transaction. If it is in a transaction,
   * then the transaction will be in an inconsistent state. This method should
   * be used if the request is waiting for something to finish, and while it is
   * waiting the connection resource might be needed. The original reason for
   * this method is that a request would make an RMI call to a service running
   * on the same instance and sharing the same pool. The calling request should
   * release its connection to the pool and then grab another one once the call
   * returns.
   */
  public void returnDatabaseConnectionToPool()
  {
    try
    {
      if (!conn.isClosed())
      {
        conn.close();
      }
    }
    catch (SQLException ex)
    {
      throw new DatabaseException(ex);
    }
  }

  /**
   * Returns the existing database connection to the pool (if it has not been
   * returned already) and fetches a new one from the pool to be used in this
   * request. This should only be used when the request is not in a transaction.
   * If it is in a transaction, then the transaction will be in an inconsistent
   * state. This method should be used if the request is waiting for something
   * to finish, and while it is waiting the connection resource might be needed.
   * The original reason for this method is that a request would make an RMI
   * call to a service running on the same instance and sharing the same pool.
   * The calling request should release its connection to the pool and then grab
   * another one once the call returns.
   * 
   */
  public void getNewDatabaseConnectionFromPool()
  {
    try
    {
      if (!conn.isClosed())
      {
        conn.close();
      }

      conn = Database.getConnection();
    }
    catch (SQLException ex)
    {
      throw new DatabaseException(ex);
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
