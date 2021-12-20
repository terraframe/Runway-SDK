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
package com.runwaysdk.dataaccess.transaction;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.runwaysdk.ProblemIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.logging.RunwayLogUtil;
import com.runwaysdk.session.ThreadRequestManagement;

public privileged aspect ThreadTransactionManagement extends AbstractTransactionManagement
{
  // public pointcut transactions()
  // : threadedTransaction(Transaction);

  // Reference to the transaction state of the main thread;
  private TransactionState mainTransactionState;

  // private Savepoint dmlSavepoint;
  // private Savepoint ddlSavepoint;

  public pointcut transactions()
  : threadedTransaction(Transaction) && !cflow(nonThreadedTransaction());

  protected pointcut rootThreadTransaction(ThreadTransactionState threadTransactionState)
  : transactions() && !cflowbelow(transactions()) && args(threadTransactionState, ..);

  before(ThreadTransactionState threadTransactionState) :rootThreadTransaction(threadTransactionState)
  {
    this.mainTransactionState = threadTransactionState.getTransactionState();

    // Share some transaction state from the main thread with this one.
    this.transactionState = new TransactionState(mainTransactionState);

    this.transactionState.savepointPush(Database.setSavepoint());

    // this.dmlSavepoint = Database.setSavepoint();
    // this.ddlSavepoint = Database.setDDLsavepoint();
  }

  public ThreadTransactionManagement()
  {
    // this.dmlSavepoint = Database.setSavepoint();
    // this.ddlSavepoint = Database.setDDLsavepoint();
  }

  protected ThreadTransactionCache getTransactionCache()
  {
    return (ThreadTransactionCache) this.getState().getCache();
  }

  /**
   * New Description:
   * Update the cache after the object has been applied to the DB with the correct
   * OID and KEY values.
   * 
   * Old description:
   * Updating the key cache needs to occur after the call, as the key is not
   * updated until after the object is applied.
   * 
   * @param entityDAO
   */
  protected pointcut afterEntityApply(EntityDAO entityDAO)
  :(execution (* com.runwaysdk.dataaccess.EntityDAO.apply(..)) && target(entityDAO))
   && within(com.runwaysdk.dataaccess.EntityDAO);  
  after(EntityDAO entityDAO)
    : afterEntityApply(entityDAO)
  {     
      // Mark the original reference as having participated in the transaction
      entityDAO.setTransactionState();
  }

  
  /**
   * Performs actions at the successful end of the transaction.
   */
  protected void processTransaction() throws SQLException
  {
    this.dmlCommit();

    Map<String, TransactionItemEntityDAOAction> entityDAOIDsMap = this.getTransactionCache().getEntityDAOIDsMap();

    Collection<TransactionItemEntityDAOAction> actions = entityDAOIDsMap.values();

    for (TransactionItemEntityDAOAction action : actions)
    {
      EntityDAOIF entityDAO = action.getEntityDAO();
      ( (EntityDAO) entityDAO ).clearSavepoint();
    }

    this.mainTransactionState.copyFromThreadToMain(this.transactionState);

    // ****Undoable Commands:****
    // Do nothing special for the undoable command data structures.

    // ****Not Undoable Commands:****
    // Do nothing special for the not-undoable commands
  }

  /**
   * Rollback the transaction.
   * 
   * @param ex
   */
  protected void rollbackTransaction(Throwable ex)
  {
    Savepoint savepoint = this.getState().savepointPeek();

    try
    {
      Integer savepointId = new Integer(savepoint.getSavepointId());

      try
      {
        this.dmlRollback();
      }
      catch (SQLException ex2)
      {
        // Should never happen
        ProgrammingErrorException programmingErrorException = new ProgrammingErrorException(ex2);
        programmingErrorException.setStackTrace(ex2.getStackTrace());
        log.error(RunwayLogUtil.getExceptionLoggableMessage(programmingErrorException), programmingErrorException);
        throw programmingErrorException;
      }

      // ****Undoable Commands:****
      // Rollback the completed undoable command data structures
      this.getState().undoCompletedUndoableCommands();
      this.getState().doFinallyCompletedUndoableCommands();

      // ****Not Undoable Commands:****
      // Not undoable commands are not actually executed until the end of the
      // main
      // thread
      // Therefore, there is nothing to do.

      Map<String, TransactionItemEntityDAOAction> entityDAOIDsMap = this.getTransactionCache().getEntityDAOIDsMap();

      Collection<TransactionItemEntityDAOAction> actions = entityDAOIDsMap.values();

      for (TransactionItemEntityDAOAction action : actions)
      {
        EntityDAOIF entityDAO = action.getEntityDAO();
        ( (EntityDAO) entityDAO ).rollbackState(savepointId);
      }

      this.processThrowable(ex);
    }
    catch (SQLException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Override
  protected void dmlCommit() throws SQLException
  {
    Database.releaseSavepoint(this.getState().savepointPop());
  }

  @Override
  protected void dmlRollback() throws SQLException
  {
    Savepoint dmlSavepoint = this.getState().savepointPop();
    Database.rollbackSavepoint(dmlSavepoint);
    Database.releaseSavepoint(dmlSavepoint);
  }

  @Override
  protected void ddlCommitAndClose() throws SQLException
  {
    // Do nothing. Threaded transactions do not commit DDL connections. Rather,
    // the main thread does.
  }

  @Override
  protected void ddlRollbackAndClose() throws SQLException
  {
    // Do nothing. Threaded transactions do not commit DDL connections. Rather,
    // the main thread does.
  }

  @Override
  protected List<ProblemIF> getRequestProblemList()
  {
    return ThreadRequestManagement.aspectOf().getProblemList();
  }

}
