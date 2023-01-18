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
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.ProblemIF;
import com.runwaysdk.business.generation.CompilerException;
import com.runwaysdk.business.generation.GenerationFacade;
import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.RunwayProperties;
import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.database.AddGroupIndexDDLCommand;
import com.runwaysdk.dataaccess.graph.GraphDBService;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.logging.RunwayLogUtil;
import com.runwaysdk.session.PermissionCache;
import com.runwaysdk.session.RequestManagement;
import com.runwaysdk.session.Session;
import com.runwaysdk.session.SessionIF;

public privileged aspect TransactionManagement extends AbstractTransactionManagement
{
  private static Logger logger      = LoggerFactory.getLogger(TransactionManagement.class);

  boolean               lockedCache = false;

  public pointcut transactions()
  // : (nonAnnotatedTransactions() || nonThreadedTransaction(Transaction)) &&
  // !cflow(threadedTransaction(Transaction));
  : (nonThreadedTransaction()) && !cflow(threadedTransaction(Transaction));

  public TransactionManagement()
  {
    this.transactionState = new TransactionState();
    // ObjectCacheFacade.beginGlobalCacheTransaction();
  }

  protected TransactionCache getTransactionCache()
  {
    return (TransactionCache) this.getState().getCache();
  }

  protected List<ProblemIF> getRequestProblemList()
  {
    return RequestManagement.aspectOf().getState().getProblemList();
  }

  /**
   * New Description: Update the cache after the object has been applied to the
   * DB with the correct OID and KEY values.
   * 
   * Old description: Updating the key cache needs to occur after the call, as
   * the key is not updated until after the object is applied.
   * 
   * @param entityDAO
   */
  protected pointcut afterEntityApply(EntityDAO entityDAO)
  :(execution (* com.runwaysdk.dataaccess.EntityDAO.apply(..)) && target(entityDAO))
   && within(com.runwaysdk.dataaccess.EntityDAO);

  after(EntityDAO entityDAO)
    : afterEntityApply(entityDAO)
  {
    // This can be called if this is the root most entity in a transaction and
    // the
    // transaction cache has been closed. This can happen due to aspect weave
    // ordering
    // with the main transaction AROUND advice
    if (!this.getTransactionCache().isClosed())
    {
      this.getTransactionCache().put(entityDAO);
    }

    // Mark the original reference as having participated in the transaction
    entityDAO.setTransactionState();
  }

  /**
   *
   */
  protected void processTransaction() throws SQLException
  {
    // 1. Lock all objects that were modified during this transaction
    ( LockObject.getLockObject() ).addTransactionLocks(this.getTransactionCache().getEntityDAOIDsMap());

    // 2. Delete all Java source and class files for types that are deleted in
    // this transaction.
    Collection<MdTypeDAOIF> mdTypeIFDeleteClasses = this.getTransactionCache().getMdTypesForClassRemoval();

    // 2b. Write all java artifacts which have been marked as needed to be
    // written before the compile
    this.generateJavaFiles(this.getState().getListOfMarkedArtifacts());

    // 3. Compile Java source files and produce class files for types that
    // were created or modified in this transaction.
    Collection<MdTypeDAOIF> mdTypeIFGenerateClasses = this.getTransactionCache().getMdTypesForCodeGeneration();

    // 4. Lock all collection classes who's caching algorithm was modified
    // during this transaction

    // Type has been modified (either forcing a compile or changing a cache
    // algorithm.
    // Stop all object instantiations until this transaction is complete.
    if (this.getTransactionCache().getClassTypeCollectionsMap().size() > 0 || ( mdTypeIFGenerateClasses != null && mdTypeIFGenerateClasses.size() > 0 ) || ( mdTypeIFDeleteClasses != null && mdTypeIFDeleteClasses.size() > 0 ))
    {
      lockedCache = true;
      LockObject.getLockObject().lockCache();
    }

    this.removeClassFiles(mdTypeIFDeleteClasses);
    // Delete the Java artifacts for imported types that are deleted
    this.generateAndCompileClasses(mdTypeIFGenerateClasses);

    // Write Java artifacts for imported types that are added or modified.
    this.generateJavaFiles(this.getState().getImportedModifiedMdTypeDAOIFs());

    for (AddGroupIndexDDLCommand addGroupIndexDDLCommand : this.getState().getGroupIndexDDLCommands())
    {
      addGroupIndexDDLCommand.doIt();
    }

    if (lockedCache == true)
    {
      if (!this.getState().isRefreshEntireCache())
      {
        // Cache can be corrupt if an error occurs within this if block
        this.getTransactionCache().updateCaches();
      }

      // 5. commit DML
      this.dmlCommit();
      haveCommited = true;

      if (!this.getState().isRefreshEntireCache())
      {
        // Since the database committed and there were no errors refreshing
        // the updated caches,
        // it is safe to apply those caches to Cache.
        this.getTransactionCache().applyCollectionCaches();
      }
      else
      {
        ObjectCache.refreshCache();
      }
    }
    else
    {
      // 5. commit DML
      this.dmlCommit();
      haveCommited = true;

      if (!this.getState().isRefreshEntireCache())
      {
        this.getTransactionCache().updateCaches();
      }
      else
      {
        ObjectCache.refreshCache();
      }
    }

    // ObjectCacheFacade.commitGlobalCacheTransaction();

    this.getTransactionCache().updatePermissionEntities();

    // (new 6 and 7) reload the classloader if any MdTypes have been modified
    // or deleted

    // Clear all thread locks
    ( LockObject.getLockObject() ).releaseTransactionLocks(this.getTransactionCache());
    ( LockObject.getLockObject() ).releaseAppLocks(this.getState().getUnsetAppLockIds());
    this.getState().clearUnsetAppLockIds();
    ( LockRelationship.getLockRelationship() ).releaseRelLocks(this.getState().getSetAppLockIds());
    this.getState().clearSetAppLockIds();

    if (lockedCache == true)
    {
      LockObject.getLockObject().unlockCache();
      lockedCache = false;
    }

    // 8. Update the global permissions cache
    if (this.getState().isReloadGlobalPermissions())
    {
      PermissionCache.reload();
    }

    // clear completedUndoableCommandStack
    this.getState().clearAllCompletedUndoableCommands();

    // From here on out there is no going back.
    // Perform DELETE DML. Delete DML cannot be undone
    this.getState().processNotUndoableCommands();
  }

  protected void rollbackTransaction(Throwable ex)
  {
    try
    {
      if (conn != null)
      {
        if (debug)
        {
          System.out.println("\n-----------rollback transaction-----------");
        }
        this.dmlRollback();
      }

      // Resets the state of entity objects that were modified during the
      // transaction
      this.getTransactionCache().resetObjectsFromRollback();

      // Unregister ids, object classes, and relationship types from the
      // LockObject
      ( LockObject.getLockObject() ).releaseTransactionLocks(this.getTransactionCache());

      ( LockObject.getLockObject() ).releaseAppLocks(this.getState().getUnsetAppLockIds());
      this.getState().clearUnsetAppLockIds();

      ( LockRelationship.getLockRelationship() ).releaseRelLocks(this.getState().getSetAppLockIds());
      this.getState().clearSetAppLockIds();

      // ObjectCacheFacade.rollbackGlobalCacheTransaction();

      if (lockedCache == true)
      {
        LockObject.getLockObject().unlockCache();
        lockedCache = false;
      }

      // Undo completedUndoableCommandStack
      this.getState().undoCompletedUndoableCommands();

      // Only instruct administrator to drop tables if the transaction
      // committed. This means that the
      // tables/columns were supposed to be dropped.
      if (haveCommited)
      {
        this.getState().unprocesssedNotUndoableCommandsExeption(ex);
      }
      this.processThrowable(ex);
    }
    catch (SQLException ex2)
    {
      // Should never happen
      ProgrammingErrorException programmingErrorException = new ProgrammingErrorException(ex2);
      programmingErrorException.setStackTrace(ex2.getStackTrace());
      log.error(RunwayLogUtil.getExceptionLoggableMessage(programmingErrorException), programmingErrorException);
      throw programmingErrorException;
    }
  }

  /**
   * Completes actions at the end of the transaction regardless of success or
   * failure.
   */
  protected void doFinally()
  {
    super.doFinally();

    this.getState().allCommandsDoFinally();
  }

  /**
   * Copy Java source and class files stored in the given MdType objects over to
   * the file system.
   */
  private void generateJavaFiles(Collection<MdTypeDAOIF> mdTypeIFGenerateClasses)
  {
    // First, generate the source files.
    for (MdTypeDAOIF mdTypeIF : mdTypeIFGenerateClasses)
    {
      if (GenerationUtil.isSkipCompileAndCodeGeneration(mdTypeIF) || !mdTypeIF.isGenerateSource())
      {
        continue;
      }
      
      // Do not generate source if this is a test environment or Runway development and the type is a system type.
      if (!RunwayProperties.getIsImportWorking() &&
          (LocalProperties.isTestEnvironment() || LocalProperties.isRunwayEnvironment()) && 
          mdTypeIF.isSystemPackage())
      {
        continue;
      }

      // Delete generated artifacts
      // if (!LocalProperties.isKeepSource())
      // {
      // Command command =
      // ((MdTypeDAO)mdTypeIF).getCleanJavaArtifactCommand(conn);
      // command.doIt();
      // }

      // no need to undo the delete. Should something happen the undo called on
      // either the create or
      // update will roll back the Java artifacts from the database.
      // this.getState().completedUndoableCommand(command);

      Command command = ( (MdTypeDAO) mdTypeIF ).getCreateUpdateJavaArtifactCommand(conn);

      if (command != null)
      {
        command.doIt();

        // Push onto the stack first, in case there is an error with generation.
        // This will
        // ensure proper cleanup.
        this.getState().completedUndoableCommand(command);
      }

    }
  }

  /**
   * Removes java files, removes class files, and unloads java classes.
   */
  private void removeClassFiles(Collection<MdTypeDAOIF> mdTypeIFDeleteClasses)
  {
    // If this is a development environment, and we're keeping source, don't
    // delete anything
    if ( ( LocalProperties.isKeepSource() && LocalProperties.isDevelopEnvironment() ) || mdTypeIFDeleteClasses.size() == 0)
    {
      return;
    }

    String bcuz = LocalProperties.isKeepSource() == false ? "because isKeepSource=false" : "because isDevelopEnvironment=false";
    logger.info("Deleting source files for types [" + mdTypeIFDeleteClasses.toString() + "] " + bcuz + ".");

    for (MdTypeDAOIF mdTypeIF : mdTypeIFDeleteClasses)
    {
      Command command = ( (MdTypeDAO) mdTypeIF ).getDeleteJavaArtifactCommand(conn);
      if (command != null)
      {
        command.doIt();
        this.getState().completedUndoableCommand(command);
      }
    }
  }

  /**
   * Generates and compiles java source and class files.
   * 
   * @param mdTypeIFGenerateClasses
   */
  private void generateAndCompileClasses(Collection<MdTypeDAOIF> mdTypeIFGenerateClasses)
  {
    if (LocalProperties.isSkipCodeGenAndCompile() || ! ( LocalProperties.isDevelopEnvironment() || LocalProperties.isRunwayEnvironment() || LocalProperties.isTestEnvironment() ))
    {
      return;
    }

    // Remove all mdTypes which don't generate source
    List<MdTypeDAOIF> mdTypeDAOIFs = mdTypeIFGenerateClasses.stream().filter(t -> t.isGenerateSource()).collect(Collectors.toList());

    // Even if the size of the collection is 0, continue executing this method.
    // Additional important stuff happens.

    // First, generate the source files.
    this.generateJavaFiles(mdTypeDAOIFs);

    // Only compile if we are in a test environment
    if (LocalProperties.isTestEnvironment() || LocalProperties.isRunwayEnvironment())
    {
      try
      {
        // Second, compile
        if (this.getTransactionCache().shouldCompileAll())
        {
          // First use the Eclipse CompileAll to check for errors
          GenerationFacade.compileAllNoOutput();
          // Then use AspectJ to weave into regenerated classes
          GenerationFacade.compile(mdTypeDAOIFs);
        }
        else
        {
          GenerationFacade.compile(mdTypeDAOIFs);
        }
      }
      catch (CompilerException e)
      {
        // If this is a development environment, we do not abort on a compiler
        // exception.
        if (LocalProperties.isRunwayEnvironment())
        {
          logger.error("An error occurred while compiling. Your source has not been reverted because the environment is set to develop.", e);
          System.err.println(e.getMessage());
          return;
        }
        else
        {
          throw e;
        }
      }
    }

    // // Third, store result in the database
    // for (MdTypeDAOIF mdTypeDAOIF : mdTypeDAOIFs)
    // {
    // if (GenerationUtil.isSkipCompileAndCodeGeneration(mdTypeDAOIF))
    // {
    // continue;
    // }
    //
    // // Store result in the database
    // // This cast is OK, as we are not modifying the object itself.
    // ( (MdTypeDAO) mdTypeDAOIF ).writeFileArtifactsToDatabaseAndObjects(conn);
    //
    // // Increment the sequence numbers to create a transaction log.
    // // This cast is OK, as the object has already been copied from the cached
    // // version (see TransactionCacheIF.addMdTypeToMapForGen(..))
    // EntityDAOFactory.update((MdTypeDAO) mdTypeDAOIF, false);
    //
    // // Add the newly updated EntityDAO back into the transaction cache
    // this.getTransactionCache().put(mdTypeDAOIF);
    // }
  }

  /**
   * Commits the DML database connection or releases the savepoint.
   */
  @Override
  protected void dmlCommit() throws SQLException
  { 
    // If an error is generated when the graph database commits, then convert the error 
    // to a runway error and rollback the transaction for the relational database.
    try
    {
      this.getGraphDBRequest().commit();
    }
    catch (RuntimeException graphDbEx)
    {
      try
      {
        SessionIF sessionIF = Session.getCurrentSession();
        java.util.Locale locale;
      
        if (sessionIF == null)
        {
          locale = CommonProperties.getDefaultLocale();
        }
        else
        {
          locale = sessionIF.getLocale();
        }
        
        throw GraphDBService.getInstance().processException(locale, graphDbEx);
      }
      finally
      {
        if (this.conn != null)
        {
          this.conn.rollback();
        }
      }
    }  
      
    if (this.conn != null)
    {
      this.conn.commit();
    }
  }

  /**
   * Rolls back the DML database connection or rolls back and releases the
   * savepoint.
   */
  @Override
  protected void dmlRollback() throws SQLException
  {
    this.getGraphDBRequest().rollback();
    
    if (this.conn != null)
    {
      this.conn.rollback();
    }
  }

  /**
   * Commits the DDL database connection or releases the savepoint.
   */
  protected void ddlCommit() throws SQLException
  {
    if (this.getState().getRequestAlreadyHasDDLConnection())
    {
      // PostgreSQL requires a commit to close the transaction, otherwise other
      // connections might
      // hang when executing a DDL connection on a table if this connection
      // performed a query
      // on that table
      if (!this.getState().getDDLConn().isClosed())
      {
        this.getState().getDDLConn().commit();
      }
    }
  }

  /**
   * Commits the DDL database connection or releases the savepoint.
   */
  @Override
  protected void ddlCommitAndClose() throws SQLException
  {
    if (this.getState().getRequestAlreadyHasDDLConnection())
    {
      // PostgreSQL requires a commit to close the transaction, otherwise other
      // connections might
      // hang when executing a DDL connection on a table if this connection
      // performed a query
      // on that table
      if (!this.getState().getDDLConn().isClosed())
      {
        this.getState().getDDLConn().commit();
        this.getState().getDDLConn().close();
      }
    }
  }

  /**
   * Rolls back the DDL database connection or rolls back and releases the
   * savepoint.
   */
  @Override
  protected void ddlRollbackAndClose() throws SQLException
  {
    if (this.getState().getRequestAlreadyHasDDLConnection())
    {
      // PostgreSQL requires a commit to close the transaction, otherwise other
      // connections might
      // hang when executing a DDL connection on a table if this connection
      // performed a query
      // on that table
      if (!this.getState().getDDLConn().isClosed())
      {
        this.getState().getDDLConn().rollback();
        this.getState().getDDLConn().close();
      }
    }
  }
}
