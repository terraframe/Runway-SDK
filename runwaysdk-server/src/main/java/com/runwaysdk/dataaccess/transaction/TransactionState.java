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
package com.runwaysdk.dataaccess.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.locks.ReentrantLock;

import com.runwaysdk.constants.ServerProperties;
import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.database.AddGroupIndexDDLCommand;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.DatabaseException;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;

public class TransactionState
{
  private Connection                           ddlConn;

  private ReentrantLock                        transactionStateLock;

  private volatile TransactionCacheIF          transactionCache;

  private List<Command>                        undoableCommandList;

  private List<Command>                        doFinallyUndoableCommandList;

  private Stack<Command>                       completedUndoableCommandStack;

  private List<Command>                        notUndoableCommandList;

  private List<Command>                        doFinallyNotUndoableCommandList;

  private Stack<Command>                       completedNotUndoableCommandStack;

  private Map<String, AddGroupIndexDDLCommand> groupIndexDDLCommandMap;

  private volatile Date                        transactionDate;

  protected volatile TransactionRecordDAO      transactionRecordDAO;

  private volatile boolean                     refreshEntireCache;

  // Indicates if global permissions need to be reloaded.
  private volatile boolean                     reloadGlobalPermissions;

  private Stack<Savepoint>                     savepointStack;

  private Map<String, MdTypeDAOIF>             importedModifiedMdTypeDAOIF;

  private Map<String, MdTypeDAOIF>             markedToWriteArtifact;

  // application locks that should be released on objects when the transaction
  // completes
  private volatile Set<String>                 unsetAppLocksSet;

  private volatile Set<String>                 setRelLocksSet;

  private volatile int                         metadataTempColumnCounter;

  protected TransactionState()
  {
    this.transactionStateLock = new ReentrantLock();
    this.transactionCache = new TransactionCache(this.transactionStateLock);

    this.init();
  }

  /**
   * Copies the state from the main thread to the spawned thread.
   * 
   * @param mainTransactionState
   *          state from the main thread
   */
  protected TransactionState(TransactionState mainTransactionState)
  {
    this.transactionStateLock = mainTransactionState.transactionStateLock;
    this.transactionCache = new ThreadTransactionCache(this.transactionStateLock, (TransactionCache) mainTransactionState.transactionCache);

    this.init();

    this.ddlConn = mainTransactionState.ddlConn;
    this.transactionRecordDAO = mainTransactionState.transactionRecordDAO;
    this.metadataTempColumnCounter = mainTransactionState.metadataTempColumnCounter;
  }

  private void init()
  {
    this.undoableCommandList = new LinkedList<Command>();
    this.doFinallyUndoableCommandList = new LinkedList<Command>();
    this.completedUndoableCommandStack = new Stack<Command>();

    this.notUndoableCommandList = new LinkedList<Command>();
    this.doFinallyNotUndoableCommandList = new LinkedList<Command>();
    this.completedNotUndoableCommandStack = new Stack<Command>();

    this.groupIndexDDLCommandMap = new HashMap<String, AddGroupIndexDDLCommand>();

    this.transactionDate = new Date();

    this.refreshEntireCache = false;

    this.reloadGlobalPermissions = false;

    this.savepointStack = new Stack<Savepoint>();

    this.importedModifiedMdTypeDAOIF = new HashMap<String, MdTypeDAOIF>();
    this.markedToWriteArtifact = new HashMap<String, MdTypeDAOIF>();

    this.unsetAppLocksSet = new HashSet<String>();
    this.setRelLocksSet = new HashSet<String>();

    this.metadataTempColumnCounter = 0;
  }

  /**
   * Returns the temporary metadata column counter.
   * 
   * @return temporary metadata column counter.
   */
  public int getMetadataTempColumnCounter()
  {
    return this.metadataTempColumnCounter;
  }

  /**
   * Increments the temporary metadata column counter.
   */
  public void incrementMetadataTempColumnCounter()
  {
    this.metadataTempColumnCounter++;
  }

  /**
   * Returns the list of commands that are not undoable.
   * 
   * @return the list of commands that are not undoable.
   */
  public List<Command> getNotUndoableCommandList()
  {
    return new LinkedList<Command>(this.notUndoableCommandList);
  }

  /**
   * Returns the list of commands that are undoable.
   * 
   * @return the list of commands that are undoable.
   */
  public List<Command> getUndoableCommandList()
  {
    return new LinkedList<Command>(this.undoableCommandList);
  }

  /**
   * Copies the state from the spawned thread to the main thread.
   * 
   * @param transactionState
   *          state object from a threaded transaction.
   */
  protected void copyFromThreadToMain(TransactionState transactionState)
  {
    this.transactionStateLock = transactionState.transactionStateLock;
    this.ddlConn = transactionState.ddlConn;
    this.transactionRecordDAO = transactionState.transactionRecordDAO;

    if (this.transactionCache instanceof TransactionCache && transactionState.transactionCache instanceof ThreadTransactionCache)
    {
      ( (TransactionCache) this.transactionCache ).addCacheState((ThreadTransactionCache) transactionState.transactionCache);
    }

    this.undoableCommandList.addAll(transactionState.undoableCommandList);
    this.doFinallyUndoableCommandList.addAll(transactionState.doFinallyUndoableCommandList);
    this.completedUndoableCommandStack.addAll(transactionState.completedUndoableCommandStack);

    this.notUndoableCommandList.addAll(transactionState.notUndoableCommandList);
    this.doFinallyNotUndoableCommandList.addAll(transactionState.doFinallyNotUndoableCommandList);
    this.completedNotUndoableCommandStack.addAll(transactionState.completedNotUndoableCommandStack);

    this.groupIndexDDLCommandMap.putAll(transactionState.groupIndexDDLCommandMap);

    if (this.isRefreshEntireCache() || transactionState.isRefreshEntireCache())
    {
      this.refreshEntireCache = true;
    }

    if (this.isReloadGlobalPermissions() || transactionState.isReloadGlobalPermissions())
    {
      this.reloadGlobalPermissions = true;
    }

    this.importedModifiedMdTypeDAOIF.putAll(transactionState.importedModifiedMdTypeDAOIF);
    this.markedToWriteArtifact.putAll(transactionState.markedToWriteArtifact);

    this.unsetAppLocksSet.addAll(transactionState.unsetAppLocksSet);
    this.setRelLocksSet.addAll(transactionState.setRelLocksSet);

    this.metadataTempColumnCounter = transactionState.metadataTempColumnCounter;
  }

  protected void addUnsetAppLocksSet(String entityId)
  {
    this.unsetAppLocksSet.add(entityId);
  }

  protected Set<String> getUnsetAppLockIds()
  {
    return this.unsetAppLocksSet;
  }

  protected void clearUnsetAppLockIds()
  {
    this.unsetAppLocksSet.clear();
  }

  protected void addSetAppLocksSet(String entityId)
  {
    this.transactionStateLock.lock();
    try
    {
      this.setRelLocksSet.add(entityId);
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  protected Set<String> getSetAppLockIds()
  {
    return this.setRelLocksSet;
  }

  protected void clearSetAppLockIds()
  {
    this.setRelLocksSet.clear();
  }

  protected void addImportedModifiedMdTypeDAOIF(MdTypeDAOIF mdTypeDAOIF)
  {
    this.transactionStateLock.lock();
    try
    {
      this.importedModifiedMdTypeDAOIF.put(mdTypeDAOIF.definesType(), mdTypeDAOIF);
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  protected Collection<MdTypeDAOIF> getImportedModifiedMdTypeDAOIFs()
  {
    this.transactionStateLock.lock();
    try
    {
      return this.importedModifiedMdTypeDAOIF.values();
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * Return the database connection for DDL commands.
   * 
   * @return database connection for DDL commands.
   */
  protected Connection getDDLConn()
  {
    this.transactionStateLock.lock();
    try
    {
      if (this.ddlConn == null)
      {
        this.ddlConn = Database.getDDLConnection();
      }
      return this.ddlConn;
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * Indicates whether a DDL connection has already been made for this
   * transaction.
   * 
   * @return indicates whether a DDL connection has already been made for this
   *         transaction.
   */
  protected boolean getRequestAlreadyHasDDLConnection()
  {
    this.transactionStateLock.lock();
    try
    {
      if (this.ddlConn == null)
      {
        return false;
      }
      else
      {
        return true;
      }
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * Returns the transaction record for the transaction, or null if transaction
   * logging is not enabled.
   * 
   * @return transaction record for the transaction, or null if transaction
   *         logging is not enabled.
   */
  protected TransactionRecordDAO getTransactionRecordDAO()
  {
    if (ServerProperties.logTransactions() && this.transactionRecordDAO == null)
    {
      this.transactionRecordDAO = TransactionRecordDAO.newInstance();
      this.transactionRecordDAO.apply();
    }

    return this.transactionRecordDAO;
  }

  /**
   * Returns the date that the transaction began.
   * 
   * @return
   */
  protected Date getTransactionDate()
  {
    return this.transactionDate;
  }

  protected TransactionCacheIF getCache()
  {
    return this.transactionCache;
  }

  protected void addUndoableCommand(Command command)
  {
    this.transactionStateLock.lock();
    try
    {
      this.undoableCommandList.add(command);
      this.doFinallyUndoableCommandList.add(command);
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  protected void completedUndoableCommand(Command command)
  {
    this.transactionStateLock.lock();
    try
    {
      this.completedUndoableCommandStack.add(command);
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * All undoable commands that have been process will not be undone, because we
   * are past the point in the transaction where we would undo them.
   */
  protected void clearAllCompletedUndoableCommands()
  {
    this.transactionStateLock.lock();
    try
    {
      this.completedUndoableCommandStack.clear();
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * Undo all processed commands that are undoable.
   */
  protected void undoCompletedUndoableCommands()
  {
    this.transactionStateLock.lock();
    try
    {
      // Undo completedUndoableCommandStack
      while (this.completedUndoableCommandStack.size() > 0)
      {
        Command command = (Command) completedUndoableCommandStack.pop();
        command.undoIt();
      }
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * Execute the do-finally block on all completed undoable commands.
   */
  protected void doFinallyCompletedUndoableCommands()
  {
    this.transactionStateLock.lock();
    try
    {
      for (Command command : this.doFinallyUndoableCommandList)
      {
        command.doFinally();
      }
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  protected void addNotUndoableCommand(Command command)
  {
    this.transactionStateLock.lock();
    try
    {
      this.notUndoableCommandList.add(command);
      this.doFinallyNotUndoableCommandList.add(command);
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * Executes all non undoable commands.
   */
  protected void processNotUndoableCommands()
  {
    this.transactionStateLock.lock();
    try
    {
      while (this.notUndoableCommandList.size() > 0)
      {
        Command command = (Command) this.notUndoableCommandList.get(0);
        command.doIt();
        this.notUndoableCommandList.remove(command);

        this.completedNotUndoableCommandStack.push(command);
      }
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  protected void unprocesssedNotUndoableCommandsExeption(Throwable ex)
  {
    this.transactionStateLock.lock();
    try
    {
      if (notUndoableCommandList.size() > 0)
      {
        String errMsg = "A [" + ex.getClass().getName() + "] prevented some statements from " + "executing. Check to see if these need to be executed to prevent data corruption:";
        for (int i = 0; i < notUndoableCommandList.size(); i++)
        {
          errMsg += "\n  [" + ( notUndoableCommandList.get(i) ).doItString() + "]";
        }

        errMsg += "\n\nRoot cause:\n" + ex.getMessage();

        throw new DatabaseException(errMsg);
      }
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * Process doFinally for all commands
   */
  protected void allCommandsDoFinally()
  {
    this.transactionStateLock.lock();
    try
    {
      for (Command command : this.doFinallyUndoableCommandList)
      {
        command.doFinally();
      }

      for (Command command : this.doFinallyNotUndoableCommandList)
      {
        command.doFinally();
      }
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * Group index DDL commands.
   * 
   * @return Group index DDL commands.
   */
  protected Collection<AddGroupIndexDDLCommand> getGroupIndexDDLCommands()
  {
    this.transactionStateLock.lock();
    try
    {
      return this.groupIndexDDLCommandMap.values();
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * Adds the given {@link AddGroupIndexDDLCommand} to the transaction.
   * 
   * @param groupIndexDDL
   */
  protected void addGroupIndexDDLCommand(AddGroupIndexDDLCommand groupIndexDDL)
  {
    this.transactionStateLock.lock();
    try
    {
      this.groupIndexDDLCommandMap.remove(groupIndexDDL.getIndexName());
      this.groupIndexDDLCommandMap.put(groupIndexDDL.getIndexName(), groupIndexDDL);
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * True if the global cache should be refreshed, false otherwise.
   * 
   * @return True if the global cache should be refreshed, false otherwise.
   */
  public boolean isRefreshEntireCache()
  {
    return this.refreshEntireCache;
  }

  /**
   * Sets the refresh cache flag. True if the global cache should be refreshed,
   * false otherwise.
   * 
   * @param refreshEntireCache
   */
  public void setRefreshEntireCache(boolean refreshEntireCache)
  {
    this.refreshEntireCache = refreshEntireCache;
  }

  /**
   * True if the global permission cache needs to be reloaded, false otherwise.
   * 
   * @return True if the global permission cache needs to be reloaded, false
   *         otherwise.
   */
  public boolean isReloadGlobalPermissions()
  {
    return this.reloadGlobalPermissions;
  }

  /**
   * Sets a flag to determine whether the global permission cache should be
   * reloaded.
   * 
   * @param reloadGlobalPermissions
   *          true if the global permission cache needs to be reloaded, false
   *          otherwise.
   */
  public void setReloadGlobalPermissions(boolean reloadGlobalPermissions)
  {
    this.reloadGlobalPermissions = reloadGlobalPermissions;
  }

  public Savepoint savepointPop()
  {
    this.transactionStateLock.lock();
    try
    {
      return this.savepointStack.pop();
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * Returns null if no {@link Savepoint} exists on the stack.
   * 
   * @return
   */
  public Savepoint savepointPeek()
  {
    this.transactionStateLock.lock();
    try
    {
      if (this.savepointStack.size() != 0)
      {
        return this.savepointStack.peek();
      }
      else
      {
        return null;
      }
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  public void savepointPush(Savepoint savepoint)
  {
    this.transactionStateLock.lock();
    try
    {
      this.savepointStack.push(savepoint);
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  public void savepointRemove(Savepoint savepoint)
  {
    this.transactionStateLock.lock();
    try
    {
      this.savepointStack.remove(savepoint);
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * Clears all commands.
   */
  protected void clearAllCommands()
  {
    this.transactionStateLock.lock();
    try
    {
      this.undoableCommandList.clear();
      this.doFinallyUndoableCommandList.clear();
      this.completedUndoableCommandStack.clear();

      this.notUndoableCommandList.clear();
      this.doFinallyNotUndoableCommandList.clear();
      this.completedNotUndoableCommandStack.clear();

      this.groupIndexDDLCommandMap.clear();
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * Removes the given command object from the command object data structures on
   * the state of this transaction. Some commands nullify others and should be
   * removed.
   * 
   * @param command
   *          command object to be removed.
   */
  public void removeCommandObject(Command command)
  {
    this.undoableCommandList.remove(command);
    this.doFinallyUndoableCommandList.remove(command);
    this.completedUndoableCommandStack.remove(command);

    this.notUndoableCommandList.remove(command);
    this.doFinallyNotUndoableCommandList.remove(command);
    this.completedNotUndoableCommandStack.remove(command);

    this.groupIndexDDLCommandMap.remove(command);
  }

  /**
   * Returns true if the {@link Savepoint} being rolled back occurs after the
   * {@link Savepoint} to check.
   * 
   * @param earlierSavepoint
   * @param laterSavePoint
   * @return true if the current given savepoint occurs after the savepoint to
   *         check.
   */
  public boolean isNestedSavepoint(Integer earlierSavepointId, Integer laterSavePointId)
  {
    int earlierSavepointIndex = this.getSavepointIndex(earlierSavepointId);
    int laterSavepointIndex = this.getSavepointIndex(laterSavePointId);

    if (earlierSavepointIndex <= laterSavepointIndex)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  public int getSavepointIndex(int savepointId)
  {
    for (int i = 0; i < this.savepointStack.size(); i++)
    {
      Savepoint savepoint = this.savepointStack.get(i);

      try
      {
        if (savepoint.getSavepointId() == savepointId)
        {
          return i;
        }
      }
      catch (SQLException e)
      {
        throw new ProgrammingErrorException(e);
      }
    }

    return 0;
  }

  /**
   * @param mdTypeDAO
   */
  public void markToWriteNewArtifact(MdTypeDAO mdTypeDAO)
  {
    this.transactionStateLock.lock();

    try
    {
      this.markedToWriteArtifact.put(mdTypeDAO.definesType(), mdTypeDAO);
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  protected Collection<MdTypeDAOIF> getListOfMarkedArtifacts()
  {
    this.transactionStateLock.lock();

    try
    {
      return this.markedToWriteArtifact.values();
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * Returns the transaction state of this request.
   * 
   * @return transaction state of this request.
   */
  public static TransactionState getCurrentTransactionState()
  {
    return null;
  }
}
