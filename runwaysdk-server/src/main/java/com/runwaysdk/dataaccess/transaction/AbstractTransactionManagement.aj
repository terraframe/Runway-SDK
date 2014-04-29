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
/*
 * Created on Mar 29, 2004
 * 
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.runwaysdk.dataaccess.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.SuppressAjWarnings;

import com.runwaysdk.ProblemException;
import com.runwaysdk.ProblemIF;
import com.runwaysdk.RunwayException;
import com.runwaysdk.business.Entity;
import com.runwaysdk.business.SmartException;
import com.runwaysdk.business.rbac.ActorDAOIF;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.business.state.MdStateMachineDAO;
import com.runwaysdk.business.state.StateMasterDAO;
import com.runwaysdk.business.state.StateMasterDAOIF;
import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.ServerProperties;
import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.dataaccess.ElementDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.EnumerationAttributeItem;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdFacadeDAOIF;
import com.runwaysdk.dataaccess.MdIndexDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MissingKeyNameValue;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.StaleEntityException;
import com.runwaysdk.dataaccess.TransientDAO;
import com.runwaysdk.dataaccess.TransitionDAO;
import com.runwaysdk.dataaccess.TransitionDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.cache.CacheAllRelationshipDAOStrategy;
import com.runwaysdk.dataaccess.cache.CacheNoneBusinessDAOStrategy;
import com.runwaysdk.dataaccess.cache.CacheStrategy;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.database.AddGroupIndexDDLCommand;
import com.runwaysdk.dataaccess.database.DDLCommand;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.DatabaseException;
import com.runwaysdk.dataaccess.database.EntityDAOFactory;
import com.runwaysdk.dataaccess.database.MetadataDisplayLabelDDLCommand;
import com.runwaysdk.dataaccess.database.ServerIDGenerator;
import com.runwaysdk.dataaccess.metadata.MdActionDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdMethodDAO;
import com.runwaysdk.dataaccess.metadata.MdParameterDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.logging.RunwayLogUtil;
import com.runwaysdk.session.PermissionEntity;
import com.runwaysdk.session.RequestState;
import com.runwaysdk.session.Session;
import com.runwaysdk.util.IdParser;

/**
 * @author nathan
 * 
 *         To change the template for this generated comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
privileged public abstract aspect AbstractTransactionManagement percflow(topLevelTransactions())
{
  protected static boolean   debug        = false;

  // Connection used for DML statements. This comes from the Request Aspect.
  protected Connection       conn         = Database.getConnection();

  protected TransactionState transactionState;

  protected Log              log          = LogFactory.getLog(AbstractTransactionManagement.class.getName());

  protected boolean          haveCommited = false;

  protected TransactionState getState()
  {
    return this.transactionState;
  }

  protected TransactionCacheIF getTransactionCache()
  {
    return this.getState().getCache();
  }

  public pointcut nonAnnotatedTransactions()
  // Data Access
    :  execution (* com.runwaysdk.dataaccess.metadata.MdEntityDAO+.deleteAllRecords(..))
    || execution (* com.runwaysdk.dataaccess.EntityDAO+.save(..))
    || execution (* com.runwaysdk.dataaccess.EntityDAO+.apply())
    || execution (* com.runwaysdk.dataaccess.EntityDAO+.delete(..))
    || execution (* com.runwaysdk.dataaccess.RelationshipDAO+.save(..))
    || execution (* com.runwaysdk.dataaccess.RelationshipDAO+.delete(..))
    || execution (* com.runwaysdk.dataaccess.TransientDAO+.apply())

// Business Transaction
    || execution (* com.runwaysdk.business.Entity+.apply())
    || execution (* com.runwaysdk.business.Entity+.delete())
    || execution (* com.runwaysdk.business.Element+.lock())
    || execution (* com.runwaysdk.business.Element+.unlock())
//    || execution (* com.runwaysdk.business.ElementBase.userLock(..))
    || execution (* com.runwaysdk.business.Element+.appLock(..))
//    || execution (* com.runwaysdk.business.ElementBase.releaseUserLock(..))
    || execution (* com.runwaysdk.business.Element+.releaseAppLock(..))
    || execution (* com.runwaysdk.business.SessionComponent+.apply())
    || execution (* com.runwaysdk.business.SessionComponent+.applyNoPersist())
    || execution (* com.runwaysdk.business.SessionComponent+.delete())

 // used by the XML import routine
    || execution (* com.runwaysdk.dataaccess.database.Database.deleteAllTableRecords(..))
    || execution (* com.runwaysdk.dataaccess.database.Database.insert(..))
    || execution (* com.runwaysdk.dataaccess.io.dataDefinition.SAXImporter.runImport(..))
    || execution (* com.runwaysdk.dataaccess.database.Database.buildSQLInsertStatement(..))
    || execution (* com.runwaysdk.dataaccess.database.Database.buildSQLUpdateStatement(..))
    || execution (* com.runwaysdk.dataaccess.database.Database.deleteWhere(String, ..))
    || execution (* com.runwaysdk.dataaccess.database.Database.createClassTable(..))
    || execution (* com.runwaysdk.dataaccess.database.Database.createView(..))
    || execution (* com.runwaysdk.dataaccess.database.Database.dropClassTable(..))
    || execution (* com.runwaysdk.dataaccess.database.Database.dropTables(..))
    || execution (* com.runwaysdk.dataaccess.database.Database.dropView(..))
    || execution (* com.runwaysdk.dataaccess.database.Database.dropViews(..))
    || execution (* com.runwaysdk.dataaccess.database.Database.createRelationshipTable(..))
    || execution (* com.runwaysdk.dataaccess.database.Database.deleteRelationshipTable(..))
    || execution (* com.runwaysdk.dataaccess.database.Database.createEnumerationTable(..))
    || execution (* com.runwaysdk.dataaccess.database.Database.deleteEnumerationTable(..))
    || execution (* com.runwaysdk.dataaccess.database.Database.addDecField(..))
    || execution (* com.runwaysdk.dataaccess.database.Database.addUniqueIndex(..))
    || execution (* com.runwaysdk.dataaccess.database.Database.addNonUniqueIndex(..))
    || execution (* com.runwaysdk.dataaccess.database.Database.dropNonUniqueIndex(..))
    || execution (* com.runwaysdk.dataaccess.database.Database.dropUniqueIndex(..))
    || execution (* com.runwaysdk.dataaccess.database.Database.addUniqueGroupAttributeIndex(..))
    || execution (* com.runwaysdk.dataaccess.database.Database.dropUniqueGroupAttributeIndex(..))
    || execution (* com.runwaysdk.dataaccess.database.Database.alterFieldType(..))
    || execution (* com.runwaysdk.dataaccess.database.Database.dropField(..))
    || execution (* com.runwaysdk.dataaccess.database.Database.addField(..))
    || execution (* com.runwaysdk.dataaccess.database.Database.deleteEumerationItemFromLinkTable(..))
    || execution (* com.runwaysdk.dataaccess.database.Database.deleteAllEnumAttributeInstances(..))
    || execution (* com.runwaysdk.dataaccess.database.Database.deleteSetIdFromLinkTable(..))


  // State transactions
    || execution (* com.runwaysdk.dataaccess.BusinessDAO.promote(..))

  // JUnit DDL
//    || execution (* junit.framework.TestCase+.classSetUp(..))
//    || execution (* junit.framework.TestCase+.classTearDown(..))
//    || execution (* junit.framework.TestCase+.setUp(..))
//    || execution (* junit.framework.TestCase+.tearDown(..))

    || junitTransaction();

  public abstract pointcut transactions();

  public pointcut defaultTransaction(Transaction transaction)
  : execution (@com.runwaysdk.dataaccess.transaction.Transaction * *+.*(..))
  && @annotation(transaction) && (if (transaction.value() == TransactionType.DEFAULT));

  public pointcut nonThreadedTransaction()
  : (nonAnnotatedTransactions() || defaultTransaction(Transaction));

  public pointcut threadedTransaction(Transaction transaction)
  : execution (@com.runwaysdk.dataaccess.transaction.Transaction * *+.*(..))
  && @annotation(transaction) && (if (transaction.value() == TransactionType.THREAD));

  // Database deadlock detection
  protected pointcut dmlTableOperations(String tableName)
  :  (execution (* com.runwaysdk.dataaccess.database.Database.buildSQLInsertStatement(String, ..)) && args(tableName, ..))
  || (execution (* com.runwaysdk.dataaccess.database.Database.buildSQLUpdateStatement(String, ..)) && args(tableName, ..))
  || (execution (* com.runwaysdk.dataaccess.database.general.AbstractDatabase+.deleteWhere(String, ..)) && args(tableName, ..));

  protected pointcut ddlTableOperations(String tableName)
  :  (execution (* com.runwaysdk.dataaccess.database.general.AbstractDatabase+.createClassTable(String, ..)) && args(tableName, ..))
  || (execution (* com.runwaysdk.dataaccess.database.general.AbstractDatabase+.createView(String, ..)) && args(tableName, ..))
  || (execution (* com.runwaysdk.dataaccess.database.general.AbstractDatabase+.createRelationshipTable(String, ..)) && args(tableName, ..))
  || (execution (* com.runwaysdk.dataaccess.database.general.AbstractDatabase+.createEnumerationTable(String, ..)) && args(tableName, ..))
  || (execution (* com.runwaysdk.dataaccess.database.general.AbstractDatabase+.addDecField(String, ..)) && args(tableName, ..))
  || (execution (* com.runwaysdk.dataaccess.database.general.AbstractDatabase+.addField(String, ..)) && args(tableName, ..))
  || (execution (* com.runwaysdk.dataaccess.database.general.AbstractDatabase+.addUniqueIndex(String, ..)) && args(tableName, ..))
  || (execution (* com.runwaysdk.dataaccess.database.general.AbstractDatabase+.addNonUniqueIndex(String, ..)) && args(tableName, ..))
  || (execution (* com.runwaysdk.dataaccess.database.general.AbstractDatabase+.addUniqueGroupAttributeIndex(String, ..)) && args(tableName, ..))
  || (execution (* com.runwaysdk.dataaccess.database.general.AbstractDatabase+.alterFieldType(String, ..)) && args(tableName, ..))
  || (execution (* com.runwaysdk.dataaccess.database.general.AbstractDatabase+.addField(String, ..)) && args(tableName, ..));

  // We do not need to check for deadlocks when deleting/droping, as they are
  // performed only at the end of a transaction
  // || (execution (*
  // com.runwaysdk.dataaccess.database.general.AbstractDatabase+.deleteClassTable(String,
  // ..)) && args(tableName, ..))
  // || (execution (*
  // com.runwaysdk.dataaccess.database.general.AbstractDatabase+.deleteRelationshipTable(String,
  // ..)) && args(tableName, ..))
  // || (execution (*
  // com.runwaysdk.dataaccess.database.general.AbstractDatabase+.deleteEnumerationTable(String,
  // ..)) && args(tableName, ..))
  // || (execution (*
  // com.runwaysdk.dataaccess.database.general.AbstractDatabase+.dropNonUniqueIndex(String,
  // ..)) && args(tableName, ..))
  // || (execution (*
  // com.runwaysdk.dataaccess.database.general.AbstractDatabase+.dropUniqueIndex(String,
  // ..)) && args(tableName, ..))
  // || (execution (*
  // com.runwaysdk.dataaccess.database.general.AbstractDatabase+.dropUniqueGroupAttributeIndex(String,
  // ..)) && args(tableName, ..))
  // || (execution (*
  // com.runwaysdk.dataaccess.database.general.AbstractDatabase+.dropField(String,
  // ..)) && args(tableName, ..))

  protected pointcut junitTransaction()
  : (execution (* junit.framework.TestCase+.classSetUp(..))
    || execution (* junit.framework.TestCase+.classTearDown(..))
    || execution (* junit.framework.TestCase+.setUp(..))
    || execution (* junit.framework.TestCase+.tearDown(..)) )
    &&
    (
        !within(com.runwaysdk.facade.ConversionTest) &&
        !within(com.runwaysdk.facade.AdapterTest+) &&
        !within(com.runwaysdk.facade.SessionDTOAdapterTest+) &&
        !within(com.runwaysdk.facade.FacadeGenerationTest+) &&
        !within(com.runwaysdk.facade.InvokeSessionComponentMethodTestBase+) &&
        !within(com.runwaysdk.facade.InvokeMethodTestBase+) &&
        !within(com.runwaysdk.facade.MessageTest+) &&
        !within(com.runwaysdk.facade.*SeleniumTest*) &&
        !within(com.runwaysdk.facade.RMI*Test) &&
        !within(com.runwaysdk.facade.WebService*Test) &&
        !within(com.runwaysdk.facade.JSON*Test) &&

        !within(com.runwaysdk.DoNotWeave+)
    );

  public pointcut topLevelTransactions()
  :  transactions()
  && !cflowbelow(transactions());

  pointcut getCurrentTransactionState()
  : execution (* com.runwaysdk.dataaccess.transaction.TransactionState.getCurrentTransactionState());

  Object around() : getCurrentTransactionState()
  {
    return this.getState();
  }
  
  protected pointcut getCurrentTransactionCache() :
    call(* com.runwaysdk.dataaccess.transaction.TransactionCache.getCurrentTransactionCache());

  Object around() : getCurrentTransactionCache()
  {
    return this.getTransactionCache();
  }

  // These objects should be unlocked at the end of the transaction
  protected pointcut clearAppLock(Entity entity)
  : (  (
            execution (* com.runwaysdk.business.Entity.apply())
         || execution (* com.runwaysdk.business.Entity.delete())
         || execution (* com.runwaysdk.business.Element.unlock())
       )
      && target(entity))
  && cflow(topLevelTransactions());

  after(Entity entity) : clearAppLock(entity)
  {
    this.getState().addUnsetAppLocksSet(entity.getId());
  }

  // these objects should be unlocked at the end of the session request
  protected pointcut setRelLock(String parentId, String childId)
    : (execution (* com.runwaysdk.dataaccess.transaction.LockRelationship.recordRelLock(String, String)) && args(parentId, childId))
  && cflow(topLevelTransactions());

  before(String parentId, String childId) :  setRelLock(parentId, childId)
  {
    this.getState().addSetAppLocksSet(parentId);
    this.getState().addSetAppLocksSet(childId);
  }

  protected pointcut reloadGlobalPublicPermissions()
  : (call (* com.runwaysdk.session.PermissionCache.reload()))
  && cflow(topLevelTransactions())
  && !within(AbstractTransactionManagement+)
  && !within(Session);

  Object around() : reloadGlobalPublicPermissions()
  {
    this.getState().setReloadGlobalPermissions(true);
    return null;
  }

  // Database deadlock prevention
  before(String tableName) :  dmlTableOperations(tableName)
  {
    this.getTransactionCache().addDMLTableName(tableName);
  }

  before(String tableName) :  ddlTableOperations(tableName)
  {
    this.getTransactionCache().performDDLTable(tableName);
  }

  // Set the date for this transaction
  protected pointcut saveElementDAO(ElementDAO elementDAO, boolean validateRequired, Date date)
  :(call (* com.runwaysdk.dataaccess.ElementDAO.save(boolean, Date)) && target(elementDAO) && args(validateRequired, date));

  Object around(ElementDAO elementDAO, boolean validateRequired, Date date)
    : saveElementDAO(elementDAO, validateRequired, date)
  {
    return proceed(elementDAO, validateRequired, this.getState().getTransactionDate());
  }

  // Record all EntityDAOs that were updated in the transaction
  protected pointcut updatedEntityDAO(EntityDAO entityDAO)
  :(call (* com.runwaysdk.dataaccess.EntityDAO.save(..)) && target(entityDAO));

  before(EntityDAO entityDAO)
    : updatedEntityDAO(entityDAO)
  {
    // Set a default key value if one has not been not been provided
    // Set the key to the id if no value has been specified.
    Attribute keyAttribute = entityDAO.getAttribute(ComponentInfo.KEY);
    String keyValue = keyAttribute.getValue();

    MdEntityDAOIF mdEntityDAOIF = getMdEntityDAO(entityDAO.getType());   

    // Only generate deterministic IDs if the type is defined to have deterministic IDs
    if (mdEntityDAOIF.hasDeterministicIds())
    {
      if (keyValue.trim().equals(""))
      {
        String devMessg = 
            "Objects of type ["+mdEntityDAOIF.definesType()+"] require a value for the ["+EntityInfo.KEY+"] "+
            "attribute in order to generate a deterministic ID.";
         throw new MissingKeyNameValue(devMessg, mdEntityDAOIF);
      }
      else
      {
        // If the key has been modified, then we must change the ID
        if (keyAttribute.isModified() && !keyAttribute.getValue().equals(entityDAO.getId()))
        {          
          String mdTypeRootId = IdParser.parseMdTypeRootIdFromId(entityDAO.getId());
          String newRootId = ServerIDGenerator.hashedId(keyValue);
          String newId = IdParser.buildId(newRootId, mdTypeRootId);
          entityDAO.setId(newId);
             
          if (entityDAO.isAppliedToDB() && entityDAO.hasIdChanged())
          {
            EntityDAOFactory.floatObjectIdReferences(entityDAO, entityDAO.getOldId(), newId);
            this.getTransactionCache().changeCacheId(entityDAO.getOldId(), entityDAO);
          }  
        }
      }
    }
    else
    {
      if (keyValue.trim().equals(""))
      {
        keyAttribute.setValue(entityDAO.getId());
      }
    }    

// Heads up: test      
//      
//      if (keyValue.trim().equals(""))
//      {
//        keyAttribute.setValue(entityDAO.getId());
//      }
//      else
//      {
//        // If the key has been modified, then we must change the ID
//        if (keyAttribute.isModified() && !keyAttribute.getValue().equals(entityDAO.getId()))
//        {
//          MdEntityDAOIF mdEntityDAOIF = getMdEntityDAO(entityDAO.getType());
//          
//          // Only generate deterministic IDs if the type is defined to have deterministic IDs
//          if (mdEntityDAOIF.hasDeterministicIds())
//          {
//            String mdTypeRootId = IdParser.parseMdTypeRootIdFromId(entityDAO.getId());
//            String newRootId = ServerIDGenerator.hashedId(keyValue);
//            String newId = IdParser.buildId(newRootId, mdTypeRootId);
//            entityDAO.setId(newId);
//            
//            if (entityDAO.isAppliedToDB() && entityDAO.hasIdChanged())
//            {
//              EntityDAOFactory.floatObjectIdReferences(entityDAO, entityDAO.getOldId(), newId);
//              this.getTransactionCache().changeCacheId(entityDAO.getOldId(), entityDAO);
//            }  
//          }
//        } 
//      
//      
//      if (!entityDAO.isAppliedToDB())
//      {
//        String mdTypeRootId = IdParser.parseMdTypeRootIdFromId(entityDAO.getId());
//        String newRootId = ServerIDGenerator.hashedId(keyValue);
//        String newId = IdParser.buildId(newRootId, mdTypeRootId);
//        entityDAO.setId(newId);
//      }
//    }
    
    this.getTransactionCache().updateEntityDAO(entityDAO);
  }

  protected pointcut updatedRelationshipObject(RelationshipDAO relationshipDAO)
  :(call (* com.runwaysdk.dataaccess.EntityDAO.save(..)) && target(relationshipDAO));

  before(RelationshipDAO relationshipDAO)
    : updatedRelationshipObject(relationshipDAO)
  {
    if (relationshipDAO.isNew())
    {
      this.getTransactionCache().addRelationship(relationshipDAO);
    }
    else
    {
      if (relationshipDAO.hasIdChanged())
      {
        this.getTransactionCache().updateRelationshipId(relationshipDAO);
      }
//  Heads up: test
//      if (relationshipDAO.hasParentIdChanged() )
//      {
//        String newParentId = relationshipDAO.getParentId();
//        String oldParentId = relationshipDAO.getOldParentId();
//        
//        this.getTransactionCache().updateParentRelationshipId(relationshipDAO);
//      }
//      if (relationshipDAO.hasChildIdChanged())
//      {
//        this.getTransactionCache().updateChildRelationshipId(relationshipDAO);
//      }
    }
  }

  // Record all TransientDAOs that were updated in the transaction
  protected pointcut updateTransientDAO(TransientDAO transientDAO)
  :(call (* com.runwaysdk.dataaccess.TransientDAO.save(..)) && target(transientDAO));

  before(TransientDAO transientDAO)
    : updateTransientDAO(transientDAO)
  {
    this.getTransactionCache().updateTransientDAO(transientDAO);
  }

  // MdAttribute objects that have been deleted.
  protected pointcut deletedMdAttribute(MdAttributeDAO mdAttribute)
  :(execution (* com.runwaysdk.dataaccess.metadata.MdAttributeDAO.delete(..)) && target(mdAttribute));

  before(MdAttributeDAO mdAttribute)
    : deletedMdAttribute(mdAttribute)
  {
    if (!mdAttribute.isImport())
    {
      this.getTransactionCache().deletedMdAttribute_CodeGen(mdAttribute);
    }
  }

  // MdRelationship objects that have been created.
  protected pointcut updatedMdRelationship(MdRelationshipDAO mdRelationship)
  :(execution (* com.runwaysdk.dataaccess.metadata.MdRelationshipDAO.save(..)) && target(mdRelationship));

  before(MdRelationshipDAO mdRelationship)
    : updatedMdRelationship(mdRelationship)
  {
    if (!mdRelationship.isImport())
    {
      this.getTransactionCache().newMdRelationship_CodeGen(mdRelationship);
    }
  }

  // MdRelationship objects that have been deleted.
  protected pointcut deletedMdRelationship(MdRelationshipDAO mdRelationship)
  :(execution (* com.runwaysdk.dataaccess.metadata.MdRelationshipDAO.delete(..)) && target(mdRelationship));

  before(MdRelationshipDAO mdRelationship)
    : deletedMdRelationship(mdRelationship)
  {
    if (!mdRelationship.isImport())
    {
      this.getTransactionCache().deletedMdRelationship_CodeGen(mdRelationship);
    }
  }

  // EnumerationAttributeItem objects that have been created.
  protected pointcut updatedEnumerationAttributeItem(EnumerationAttributeItem enumerationAttributeItem)
  :(execution (* com.runwaysdk.dataaccess.EnumerationAttributeItem.save(..)) && target(enumerationAttributeItem));

  before(EnumerationAttributeItem enumerationAttributeItem)
    : updatedEnumerationAttributeItem(enumerationAttributeItem)
  {
    // Only the addition of a new enum item will require the java enumeration be
    // regenerated.
    if (!enumerationAttributeItem.isImport() && enumerationAttributeItem.isNew())
    {
      this.getTransactionCache().newEnumerationAttributeItem_CodeGen(enumerationAttributeItem);
    }
  }

  // EnumerationAttributeItem objects that have been deleted.
  protected pointcut deletedEnumerationAttributeItem(EnumerationAttributeItem enumerationAttributeItem)
  :(execution (* com.runwaysdk.dataaccess.EnumerationAttributeItem.delete(..)) && target(enumerationAttributeItem));

  before(EnumerationAttributeItem enumerationAttributeItem)
    : deletedEnumerationAttributeItem(enumerationAttributeItem)
  {
    if (!enumerationAttributeItem.isImport())
    {
      this.getTransactionCache().deletedEnumerationAttributeItem_CodeGen(enumerationAttributeItem);
    }
  }

  // MdMethod objects that have been created or the return type is updated..
  protected pointcut updatedMdMethod(MdMethodDAO mdMethod)
  :(execution (* com.runwaysdk.dataaccess.metadata.MdMethodDAO.save(..)) && target(mdMethod));

  before(MdMethodDAO mdMethod)
    : updatedMdMethod(mdMethod)
  {
    if (!mdMethod.isImport())
    {
      this.getTransactionCache().updatedMdMethod_CodeGen(mdMethod);
    }
    this.getTransactionCache().updatedMdMethod_WebServiceDeploy(mdMethod);
  }

  // MdMethod objects that have been deleted.
  protected pointcut deletedMdMethod(MdMethodDAO mdMethod)
  :(execution (* com.runwaysdk.dataaccess.metadata.MdMethodDAO.delete(..)) && target(mdMethod));

  before(MdMethodDAO mdMethod)
    : deletedMdMethod(mdMethod)
  {
    if (!mdMethod.isImport())
    {
      this.getTransactionCache().updatedMdMethod_CodeGen(mdMethod);
    }
    this.getTransactionCache().updatedMdMethod_WebServiceDeploy(mdMethod);
  }

  // MdAction objects that have been updated or created.
  protected pointcut updateMdAction(MdActionDAO mdAction)
  :(execution (* com.runwaysdk.dataaccess.metadata.MdActionDAO.save(..)) && target(mdAction));

  before(MdActionDAO mdAction)
    : updateMdAction(mdAction)
  {
    if (!mdAction.isImport())
    {
      this.getTransactionCache().updatedMdAction_CodeGen(mdAction);
    }
  }

  // MdAction objects that have been deleted.
  protected pointcut deletedMdAction(MdActionDAO mdAction)
  :(execution (* com.runwaysdk.dataaccess.metadata.MdActionDAO.delete(..)) && target(mdAction));

  before(MdActionDAO mdAction)
    : deletedMdAction(mdAction)
  {
    if (!mdAction.isImport())
    {
      this.getTransactionCache().updatedMdAction_CodeGen(mdAction);
    }
  }

  // MdParameter objects that have been created or the return type is updated..
  protected pointcut updatedMdParameter(MdParameterDAO mdParameter)
  :(execution (* com.runwaysdk.dataaccess.metadata.MdParameterDAO.save(..)) && target(mdParameter));

  before(MdParameterDAO mdParameter)
    : updatedMdParameter(mdParameter)
  {
    if (!mdParameter.isImport())
    {
      this.getTransactionCache().updatedMdParameter_CodeGen(mdParameter);
    }

    this.getTransactionCache().updatedMdParameter_WebServiceDeploy(mdParameter);
  }

  // MdParameter objects that have been deleted.
  protected pointcut deletedMdParameter(MdParameterDAO mdParameter)
  :(execution (* com.runwaysdk.dataaccess.metadata.MdParameterDAO.delete(..)) && target(mdParameter));

  before(MdParameterDAO mdParameter)
    : deletedMdParameter(mdParameter)
  {
    if (!mdParameter.isImport())
    {
      this.getTransactionCache().updatedMdParameter_CodeGen(mdParameter);
    }

    this.getTransactionCache().updatedMdParameter_WebServiceDeploy(mdParameter);
  }

  protected pointcut createStateMaster(StateMasterDAO stateMaster)
  :(execution (* com.runwaysdk.business.state.StateMasterDAO.save(..)) && this(stateMaster));

  before(StateMasterDAO stateMaster)
  : createStateMaster(stateMaster)
  {
    this.getTransactionCache().updatedStateMaster(stateMaster);
  }

  protected pointcut getDefinesStateMasters(MdStateMachineDAO mdStateMachine)
  :(call (* com.runwaysdk.business.state.MdStateMachineDAOIF.definesStateMasters()) && target(mdStateMachine));

  List<StateMasterDAOIF> around(MdStateMachineDAO mdStateMachine)
  : getDefinesStateMasters(mdStateMachine)
  {
    List<StateMasterDAOIF> cachedStateMasterList = this.getTransactionCache().getUpdatedStateMasters(mdStateMachine.definesType());

    if (cachedStateMasterList.size() > 0)
    {
      List<StateMasterDAOIF> states = new LinkedList<StateMasterDAOIF>();
      List<String> list = EntityDAO.getEntityIdsDB(mdStateMachine.definesType());

      for (String id : list)
      {
        states.add(StateMasterDAO.get(id));
      }

      return states;
    }
    else
    {
      return proceed(mdStateMachine);
    }

  }

  protected pointcut createTransition(TransitionDAO transitionDAO)
  :(execution (* com.runwaysdk.dataaccess.TransitionDAO.save(..)) && this(transitionDAO));

  before(TransitionDAO transitionDAO)
  : createTransition(transitionDAO)
  {
    this.getTransactionCache().updatedTransition(transitionDAO);
  }

  protected pointcut deleteTransition(TransitionDAO transitionDAO)
  :(execution (* com.runwaysdk.dataaccess.TransitionDAO.delete(..)) && this(transitionDAO));

  before(TransitionDAO transitionDAO)
  : deleteTransition(transitionDAO)
  {
    this.getTransactionCache().updatedTransition(transitionDAO);
  }

  protected pointcut getDefinesTransitions(MdStateMachineDAO mdStateMachine)
  :(call (* com.runwaysdk.business.state.MdStateMachineDAOIF.definesTransitions()) && target(mdStateMachine));

  List<TransitionDAOIF> around(MdStateMachineDAO mdStateMachine)
  : getDefinesTransitions(mdStateMachine)
  {
    MdRelationshipDAOIF mdTransition = mdStateMachine.getMdTransition();

    Set<TransitionDAOIF> cachedTransitionList = this.getTransactionCache().getUpdatedTransitions(mdTransition.definesType());

    if (cachedTransitionList.size() > 0)
    {

      List<TransitionDAOIF> transitions = new LinkedList<TransitionDAOIF>();

      List<String> list = EntityDAO.getEntityIdsDB(mdTransition.definesType());

      for (String id : list)
      {
        transitions.add((TransitionDAOIF) RelationshipDAO.get(id));
      }

      return transitions;
    }
    else
    {
      return proceed(mdStateMachine);
    }
  }

  // MdType objects that have been modified.
  protected pointcut updatedMdType(MdTypeDAO mdType)
  :(execution (* com.runwaysdk.dataaccess.metadata.MdTypeDAO.save(..)) && target(mdType));

  before(MdTypeDAO mdType)
    : updatedMdType(mdType)
  {
    if (mdType.isImport())
    {
      this.getState().addImportedModifiedMdTypeDAOIF(mdType);
    }
    else
    {
      this.getTransactionCache().updatedMdType(mdType);
    }
  }

  // MdType objects that have been deleted.
  protected pointcut deletedMdType(MdTypeDAO mdType)
  :(execution (* com.runwaysdk.dataaccess.metadata.MdTypeDAO.delete(..)) && target(mdType));

  before(MdTypeDAO mdType)
    : deletedMdType(mdType)
  {
    this.getTransactionCache().deletedMdType(mdType);
  }

  protected pointcut deletedEntity(Entity entity)
  :  (execution (* com.runwaysdk.business.Mutable+.delete()) && target(entity));

  void around(Entity entity)
  : deletedEntity(entity)
  {
    String methodSignature = thisJoinPointStaticPart.getSignature().toLongString();

    // this.getTransactionCache() should not be null, as this should always
    // occur within a transaction

    // Do not execute the delete method on a business entity if the same entity
    // has been deleted before.
    if (!entity.isDeleted())
    {
      if (!this.getTransactionCache().hasExecutedEntityDeleteMethod(entity.entityDAO, methodSignature))
      {
        this.getTransactionCache().setExecutedEntityDeleteMethod(entity.entityDAO, methodSignature);
        proceed(entity);
        boolean hasCompletedDelete = 
          this.getTransactionCache().removeExecutedDeleteMethod(entity.entityDAO, methodSignature);
        if (hasCompletedDelete)
        {
          entity.entityDAO.setIsDeleted(true);
        }
      }
    }
    // This is true if the delete method is called on an object that has been deleted previously 
    // on a reference that was created before the object was deleted.
    // If the entity is marked as deleted and it has not been deleted in this transaction, then
    // throw a stale entity exception
    else // entity.isDeleted()
    {
      if (!this.getTransactionCache().hasBeenDeletedInTransaction(entity.entityDAO))
      {
        String error = "Object [" + entity.entityDAO.getId() + "] has already been deleted.";
        throw new StaleEntityException(error, entity.entityDAO);
      }
    }
  }


  protected pointcut beforeDeletedEntityDAO(EntityDAO entityDAO)
  :  (execution (* com.runwaysdk.dataaccess.EntityDAO+.delete(..))
   && target(entityDAO) );

  void around(EntityDAO entityDAO)
  : beforeDeletedEntityDAO(entityDAO)
  {
    String methodSignature = thisJoinPointStaticPart.getSignature().toLongString();
    // this.getTransactionCache() should not be null, as this should always
    // occur within a transaction

    // Do not execute the delete method on a business entity if the same entity
    // has been deleted before.
    if (!entityDAO.isDeleted())
    {
      if (!this.getTransactionCache().hasExecutedEntityDeleteMethod(entityDAO, methodSignature))
      {
        this.getTransactionCache().setExecutedEntityDeleteMethod(entityDAO, methodSignature);
        proceed(entityDAO);
        boolean hasCompletedDelete = 
          this.getTransactionCache().removeExecutedDeleteMethod(entityDAO, methodSignature);
        if (hasCompletedDelete)
        {
          entityDAO.setIsDeleted(true);
        }
      }
    }
    // This is true if the delete method is called on an object that has been deleted previously 
    // on a reference that was created before the object was deleted.
    // If the entity is marked as deleted and it has not been deleted in this transaction, then
    // throw a stale entity exception
    else // entityDAO.isDeleted()
    {
      if (!this.getTransactionCache().hasBeenDeletedInTransaction(entityDAO))
      {
        String error = "Object [" + entityDAO.getId() + "] has already been deleted.";
        throw new StaleEntityException(error, entityDAO);
      }
    }
  }

  protected pointcut throwProblem(ProblemIF problemIF)
  : (execution (* com.runwaysdk.ProblemIF+.throwIt())
  && target(problemIF));

  before(ProblemIF problemIF) : throwProblem(problemIF)
  {
    List<ProblemIF> problemList = this.getRequestProblemList();

    // Abort transaction if there are too many problems.
    if (problemList.size() >= 100)
    {
      List<ProblemIF> newProblemList = new LinkedList<ProblemIF>();
      newProblemList.addAll(problemList);

      ProblemException.throwProblemException(newProblemList);
    }
  }

  // //////////////////////////////////////////////////////////////////////////////////

  /*
   * Actors should only be notified of their permission changes until the end of
   * a transaction.
   */
  protected pointcut refreshActorPermissions(ActorDAOIF actorIF)
  : (call (* com.runwaysdk.session.PermissionObserver.notify(ActorDAOIF)) && args(actorIF))
  && !within(AbstractTransactionCache+);

  void around(ActorDAOIF actorIF)
    : refreshActorPermissions(actorIF)
  {
    this.getTransactionCache().notifyActors(actorIF);
  }

  /*
   * Actors should only be notified of their permission changes until the end of
   * a transaction.
   */
  protected pointcut unregisterPermissionEntity(PermissionEntity permissionEntity)
  : (call (* com.runwaysdk.session.PermissionObserver.unregister(PermissionEntity)) && args(permissionEntity))
  && !within(AbstractTransactionCache+);

  void around(PermissionEntity permissionEntity)
    : unregisterPermissionEntity(permissionEntity)
  {
    this.getTransactionCache().unregisterPermissionEntity(permissionEntity);
  }

  protected pointcut getDDLConnection()
  :  call(Connection Database.getDDLConnection())
  && !within(AbstractTransactionManagement+)
  && !within(TransactionState+);

  Object around() : getDDLConnection()
  {
    return this.getState().getDDLConn();
  }

  protected pointcut requestAlreadyHasDDLConnection()
  :  call(boolean Database.requestAlreadyHasDDLConnection())
  && !within(AbstractTransactionManagement+);

  @SuppressAjWarnings({"adviceDidNotMatch"})
  boolean around() : requestAlreadyHasDDLConnection()
  {
    return this.getState().getRequestAlreadyHasDDLConnection();
  }

  protected pointcut setSavepoint()
  :  call(Savepoint Database.setSavepoint())
  && !within(AbstractTransactionManagement+);

  @SuppressAjWarnings({"adviceDidNotMatch"})
  Savepoint around() : setSavepoint()
  {
    Savepoint savepoint = proceed();

    this.getState().savepointPush(savepoint);

    return savepoint;
  }

  protected pointcut peekCurrentSavepoint()
  :  call(Savepoint Database.peekCurrentSavepoint())
  && !within(AbstractTransactionManagement+);

  Savepoint around() : peekCurrentSavepoint()
  {
    return this.getState().savepointPeek();
  }

  protected pointcut popCurrentSavepoint()
  :  call(Savepoint Database.popCurrentSavepoint())
  && !within(AbstractTransactionManagement+);

  @SuppressAjWarnings({"adviceDidNotMatch"})
  Savepoint around() : popCurrentSavepoint()
  {
    return this.getState().savepointPop();
  }

  protected pointcut releaseSavepoint(Savepoint savepoint)
  :  (call(void Database.releaseSavepoint(..))  && args(savepoint))
  && !within(AbstractTransactionManagement+);

  @SuppressAjWarnings({"adviceDidNotMatch"})
  after(Savepoint savepoint) : releaseSavepoint(savepoint)
  {
    this.getState().savepointRemove(savepoint);
  }

  // do not commit transactions in the core code
  protected pointcut commitTransaction()
  :  call(* Connection.commit())
  && !within(DDLCommand+)
  && !within(MetadataDisplayLabelDDLCommand+)
  && !within(AbstractTransactionManagement+)
  && !within(RequestState)
  && !withincode(* com.runwaysdk.dataaccess.database.general.MySQL.getNextSequenceNumber(..))
  && !withincode(* com.runwaysdk.dataaccess.database.general.SQLServer.getNextSequenceNumber(..))
  && !withincode(* com.runwaysdk.dataaccess.database.general.AbstractDatabase.executeAsRoot(..));

  void around() : commitTransaction()
  {
    // System.out.println(" Would have commited transaction (but didn't) " +
    // conn + " "+thisEnclosingJoinPointStaticPart.getSourceLocation());
  }

  // do not rollback transactions in the core code
  protected pointcut rollbackTransaction()
  :  call(* Connection.rollback())
  && !within(DDLCommand+)
  && !within(MetadataDisplayLabelDDLCommand+)
  && !within(RequestState)
  && !within(AbstractTransactionManagement+)
//  && !within(com.mysql.jdbc.Connection)
  && !withincode(* com.runwaysdk.dataaccess.database.general.PostgreSQL.throwDatabaseException(..));

  @SuppressAjWarnings({"adviceDidNotMatch"})
  void around() : rollbackTransaction()
  {
    // throwDatabaseException
    // System.out.println(" Would have rolledback transaction (but didn't) " +
    // conn + " "+thisEnclosingJoinPointStaticPart.getSourceLocation());
  }

  protected pointcut skipIfProblem()
  : call (@SkipIfProblem * *+.*(..));

  @SuppressAjWarnings({"adviceDidNotMatch"})
  void around() : skipIfProblem()
  {
    List<ProblemIF> problemList = this.getRequestProblemList();

    // Skip calling the method if problems exist.
    if (problemList.size() == 0)
    {
      proceed();
    }
  }

  protected pointcut abortIfProblem()
  : call (@AbortIfProblem * *+.*(..));

  before() : abortIfProblem()
  {
    List<ProblemIF> problemList = this.getRequestProblemList();

    // Skip calling the method if problems exist.
    if (problemList.size() > 0)
    {
      List<ProblemIF> newProblemList = new LinkedList<ProblemIF>();
      newProblemList.addAll(problemList);

      ProblemException.throwProblemException(newProblemList);
    }
  }

  protected pointcut getTransactionRecord()
  :  call(* com.runwaysdk.dataaccess.transaction.TransactionRecordDAO.getCurrentTransactionRecord(..));

  Object around() : getTransactionRecord()
  {
    if (ServerProperties.logTransactions())
    {
      return this.getState().getTransactionRecordDAO();
    }
    else
    {
      // this will return null
      return proceed();
    }
  }

  // Rollback the transaction if an error occured
  Object around() : topLevelTransactions()
  {

    Object operationResult = null;
    try
    {
      haveCommited = false;

      operationResult = proceed();

      if (debug)
      {
        System.out.println("\n-----------commit transaction " + conn + "---" + thisJoinPoint + "--------");
      }

      // Problems can abort the forked threaded transaction as well.
      List<ProblemIF> problemList = this.getRequestProblemList();
      if (problemList.size() > 0)
      {
        List<ProblemIF> newProblemList = new LinkedList<ProblemIF>();
        newProblemList.addAll(problemList);

        ProblemException.throwProblemException(newProblemList);
      }

      this.processTransaction();

    }
    catch (Throwable ex)
    {
      this.rollbackTransaction(ex);
    }
    finally
    {
      this.doFinally();

      // This transaction is completed, so reset
      this.getState().clearAllCommands();

      this.getState().setReloadGlobalPermissions(false);

      List<ProblemIF> problemList = this.getRequestProblemList();
      problemList.clear();
    }

    return operationResult;
  }

  /**
   * Performs actions at the successful end of the transaction.
   */
  protected abstract void processTransaction() throws SQLException;

  /**
   * Rollback the transaction.
   * 
   * @param ex
   */
  protected abstract void rollbackTransaction(Throwable ex);

  /**
   * Completes actions at the end of the transaction regardless of success or
   * failure.
   */
  protected abstract void doFinally();

  after() returning : topLevelTransactions()
  {
    try
    {
      if (!Database.sharesDDLandDMLconnection())
      {
        this.ddlCommitAndClose();
      }
    }
    catch (SQLException ex)
    {
      throw new DatabaseException(ex);
    }
    // this.transactionState = null;
  }

  after() throwing : topLevelTransactions()
  {
    try
    {
      if (!Database.sharesDDLandDMLconnection())
      {
        this.ddlRollbackAndClose();
      }
    }
    catch (SQLException ex)
    {
      throw new DatabaseException(ex);
    }
    // this.transactionState = null;
  }

  // Record the command, even if it occurs within this aspect.
  protected pointcut commandDoItRecordCommand(Command targetCommand, Object callObject)
  : call(* com.runwaysdk.dataaccess.Command+.doIt())
  && !within(TransactionState+)
  && target(targetCommand)
  && this(callObject);

  // do not execute the Command. This aspect will take care of it.
  before(Command targetCommand, Object callObject) : commandDoItRecordCommand(targetCommand, callObject)
  {
    // Do nothing if the calling object and the target object are the same.
    // This will occur when an object calls its own doIt() method. This
    // Will be the case when super.doIt() is called.
    if (!targetCommand.equals(callObject))
    {
      if (!targetCommand.isUndoable())
      {
        this.getState().addNotUndoableCommand(targetCommand);
      }
      else
      {
        this.getState().addUndoableCommand(targetCommand);
      }
    }
  }

  protected pointcut commandDoIt(Command targetCommand, Object callObject)
    : call(* com.runwaysdk.dataaccess.Command+.doIt())
    && !within(AbstractTransactionManagement+)
    && !within(TransactionState+)
    && target(targetCommand)
    && this(callObject);

  // do not execute the Command. This aspect will take care of it.
  void around(Command targetCommand, Object callObject) : commandDoIt(targetCommand, callObject)
  {
    // Do nothing if the calling object and the target object are the same.
    // This will occur when an object calls its own doIt() method. This
    // Will be the case when super.doIt() is called.
    if (targetCommand.equals(callObject))
    {
      proceed(targetCommand, callObject);
    }
    else
    {
      if (targetCommand.isUndoable())
      {
        if (targetCommand instanceof AddGroupIndexDDLCommand)
        {
          AddGroupIndexDDLCommand groupIndexDDL = (AddGroupIndexDDLCommand) targetCommand;
          this.getState().addGroupIndexDDLCommand(groupIndexDDL);
        }
        else
        {
          proceed(targetCommand, callObject);
          this.getState().completedUndoableCommand(targetCommand);
        }
      }
    }
  }

  protected pointcut metadataDAODeleteLog()
  : execution(* com.runwaysdk.dataaccess.SpecializedDAOImplementationIF+.delete(boolean));

  protected pointcut deleteDAOLog(EntityDAO entityDAO)
  // Don't invoke this multiple times for each sub-classed delete method. Only do
  // it once.
  : (
      (execution (* com.runwaysdk.dataaccess.EntityDAO.delete(boolean)) && target(entityDAO))
      && within(com.runwaysdk.dataaccess.EntityDAO)
      && !cflow( metadataDAODeleteLog())
    )
  ||
    // This is for our own internal extensions to the DAO classes.  Log only one delete, regardless additional
    // deletes are called in the process.
    (
      (metadataDAODeleteLog() && target(entityDAO))
      && !cflowbelow( metadataDAODeleteLog())
    );

  after(EntityDAO entityDAO) returning : deleteDAOLog(entityDAO)
  {
    MdEntityDAOIF mdEntityDAOIF = entityDAO.getMdClassDAO();

    if (ServerProperties.logTransactions() && mdEntityDAOIF.isExported())
    {
      EntityDAOFactory.logTransactionItem(entityDAO, ActionEnumDAO.DELETE);
    }
  }

  /**
   * Updating the key cache needs to occur after the call, as the key is not
   * updated until after the object is applied.
   * 
   * @param entityDAO
   */
  protected pointcut afterUpdatedEntityDAO(EntityDAO entityDAO)
  :(call (* com.runwaysdk.dataaccess.EntityDAO.save(..)) && target(entityDAO));

  after(EntityDAO entityDAO)
    : afterUpdatedEntityDAO(entityDAO)
  {
    this.getTransactionCache().addUpdatedEntityToKeyNameMap(entityDAO);
    this.getTransactionCache().removeHasBeenDeletedInTransaction(entityDAO);
  }

  // These three after returning advices may not use pointcuts that are a part
  // of the topLevelTransaction
  // pointcut. Otherwise, they will
  // execute after the transaction around advice. Consequently, their effects
  // will not be applied to
  // the cache, as the transaction cache will already have been flushed.
  protected pointcut deletedEntityDAO(EntityDAO entityDAO)
  :  (execution (* com.runwaysdk.dataaccess.database.EntityDAOFactory.delete(..))
   && args(entityDAO) );

  after(EntityDAO entityDAO) returning
  : deletedEntityDAO(entityDAO)
  {
    this.getTransactionCache().deleteEntityDAO(entityDAO);

    if (entityDAO instanceof RelationshipDAO)
    {
      this.getTransactionCache().deleteRelationship((RelationshipDAO) entityDAO);
    }
  }

  protected pointcut updatedMdAttributeConcreteDAO(MdAttributeConcreteDAO mdAttributeConcreteDAO)
  :(execution (* com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO.save(..)) && target(mdAttributeConcreteDAO));

  after(MdAttributeConcreteDAO mdAttributeConcreteDAO) returning
    : updatedMdAttributeConcreteDAO(mdAttributeConcreteDAO)
  {
    this.getTransactionCache().setMdAttributeWithIndex(mdAttributeConcreteDAO);
  }

  protected pointcut refreshEntireCache()
  :  call (* com.runwaysdk.dataaccess.cache.ObjectCache.refreshTheEntireCache());

  void around() : refreshEntireCache()
  {
    this.getState().setRefreshEntireCache(true);
  }

  // Build a linked list relationships that were created during this transaction

  // if a EntityDAO has been modified during the transaction, it is not applied
  // to the database or to the EntityDAO caches until the transaction has
  // completed.
  // Should this object be requested later in the transaction, used the version
  // that was
  // modified but not yet applied.
  protected pointcut getEntityById(String id)
    :  call (* com.runwaysdk.dataaccess.cache.ObjectCache.getEntityDAO(String))
    && args(id);

  Object around(String id) : getEntityById(id)
  {
    EntityDAOIF entityDAO = null;

    entityDAO = this.getTransactionCache().getEntityDAO(id);

    if (entityDAO == null)
    {
      entityDAO = (EntityDAOIF) proceed(id);
    }

    return entityDAO;
  }

  // if a EntityDAO has been modified during the transaction, it is not applied
  // to the database or to the EntityDAO caches until the transaction has
  // completed.
  // Should this object be requested later in the transaction, used the version
  // that was
  // modified but not yet applied.
  protected pointcut getEntityByKey(String type, String key)
    :  call (* com.runwaysdk.dataaccess.EntityDAO+.get(String, String))
    && args(type, key);

  Object around(String type, String key) : getEntityByKey(type, key)
  {
    EntityDAOIF entityDAOIF = null;

    entityDAOIF = this.getTransactionCache().getEntityDAO(type, key);

    if (entityDAOIF == null)
    {
      entityDAOIF = (EntityDAOIF) proceed(type, key);
    }

    return entityDAOIF;
  }

  // If a MdClass were added during this transaction, then it will not exist yet
  // in the metadata
  // cache. Consequently, requests for the metadata for this class from the
  // metadata cache within
  // this transaction will result in a 'Metadata not defined for class' error.
// Heads up: test
//  protected pointcut getMdClassDAO(String classType)
//  :  call (* com.runwaysdk.dataaccess.cache.ObjectCache.getMdClassDAO(String))
//  && !within(AbstractTransactionManagement+)
//  && args(classType);

  protected pointcut getMdClassDAO(String classType)
  :  (call (* com.runwaysdk.dataaccess.cache.ObjectCache.getMdClassDAO(String)) ||
      call (* com.runwaysdk.dataaccess.cache.ObjectCache.getMdClassDAOReturnNull(String)))
  && !within(AbstractTransactionManagement+)
  && args(classType); 
  
  Object around(String classType) : getMdClassDAO(classType)
  {
    MdClassDAOIF mdClassDAOIF = null;

    mdClassDAOIF = this.getTransactionCache().getMdClass(classType);

    if (mdClassDAOIF == null)
    {
      mdClassDAOIF = (MdClassDAOIF) proceed(classType);
    }

    return mdClassDAOIF;
  }

  // If a MdClass were added during this transaction, then it will not exist yet
  // in the metadata
  // cache. Consequently, requests for the metadata for this class from the
  // metadata cache within
  // this transaction will result in a 'Metadata not defined for class' error.
  protected pointcut getMdClassDAOByRootId(String classRootId)
  :  call (* com.runwaysdk.dataaccess.cache.ObjectCache.getMdClassDAOByRootId(String))
  && !within(AbstractTransactionManagement+)
  && args(classRootId);

  Object around(String classRootId) : getMdClassDAOByRootId(classRootId)
  {
    MdClassDAOIF mdClassDAOIF = null;

    mdClassDAOIF = this.getTransactionCache().getMdClassDAOByRootId(classRootId);

    if (mdClassDAOIF == null)
    {
      mdClassDAOIF = (MdClassDAOIF) proceed(classRootId);
    }

    return mdClassDAOIF;
  }

  protected pointcut getMdAttributeDAOWithKey(String mdAttributeKey)
  :  call (* com.runwaysdk.dataaccess.cache.ObjectCache.getMdAttributeDAOWithKey(String))
  && !within(AbstractTransactionManagement+)
  && args(mdAttributeKey);

  Object around(String mdAttributeKey) : getMdAttributeDAOWithKey(mdAttributeKey)
  {
    MdAttributeDAOIF mdAttributeIF = null;

    mdAttributeIF = this.getTransactionCache().getAddedMdAttribute(mdAttributeKey);

    if (mdAttributeIF == null)
    {
      mdAttributeIF = (MdAttributeDAOIF) proceed(mdAttributeKey);
    }

    return mdAttributeIF;
  }

  // If an MNdFacade were added during this transaction, then it will not exist
  // yet in the metadata
  // cache. Consequently, requests for the metadata for this class from the
  // metadata cache within
  // this transaction will result in a 'Metadata not defined for class' error.
// Heads up: test
  protected pointcut getMdFacadeDAO(String facadeType)
  :  (call (* com.runwaysdk.dataaccess.cache.ObjectCache.getMdFacadeDAO(String)) ||
      call (* com.runwaysdk.dataaccess.cache.ObjectCache.getMdFacadeDAOReturnNull(String)) )
  && !within(AbstractTransactionManagement+)
  && args(facadeType);

//  protected pointcut getMdFacadeDAO(String facadeType)
//  :  call (* com.runwaysdk.dataaccess.cache.ObjectCache.getMdFacadeDAO(String))
//  && !within(AbstractTransactionManagement+)
//  && args(facadeType); 
  
  Object around(String facadeType) : getMdFacadeDAO(facadeType)
  {
    MdFacadeDAOIF mdFacadeIF = null;

    mdFacadeIF = this.getTransactionCache().getMdFacade(facadeType);

    if (mdFacadeIF == null)
    {
      mdFacadeIF = (MdFacadeDAOIF) proceed(facadeType);
    }

    return mdFacadeIF;
  }

  // If an MdEnumeration were added during this transaction, then it will not
  // exist yet in the metadata
  // cache. Consequently, requests for the metadata for this enumeration name
  // from the metadata cache within
  // this transaction will result in a 'Metadata not defined for class' error.
  protected pointcut getMdEnumerationDAO(String enumerationName)
  :  call (* com.runwaysdk.dataaccess.cache.ObjectCache.getMdEnumerationDAO(String))
  && !within(AbstractTransactionManagement+)
  && args(enumerationName);

  Object around(String enumerationName) : getMdEnumerationDAO(enumerationName)
  {
    MdEnumerationDAOIF mdEnumerationIF = null;

    mdEnumerationIF = this.getTransactionCache().getMdEnumerationDAO(enumerationName);

    if (mdEnumerationIF == null)
    {
      mdEnumerationIF = (MdEnumerationDAOIF) proceed(enumerationName);
    }

    return mdEnumerationIF;
  }

  // If an MdIndex were added during this transaction, then it will not exist
  // yet in the metadata
  // cache. Consequently, requests for the metadata for this index name from the
  // metadata cache within
  // this transaction will result in a 'Metadata not defined for class' error.
  protected pointcut getMdIndexDAO(String indexName)
  :  call (* com.runwaysdk.dataaccess.cache.ObjectCache.getMdIndexDAO(String))
  && !within(AbstractTransactionManagement+)
  && args(indexName);

  Object around(String indexName) : getMdIndexDAO(indexName)
  {
    MdIndexDAOIF mdIndexDAOIF = null;

    mdIndexDAOIF = this.getTransactionCache().getMdIndexDAO(indexName);

    if (mdIndexDAOIF == null)
    {
      mdIndexDAOIF = (MdIndexDAOIF) proceed(indexName);
    }

    return mdIndexDAOIF;
  }

  // Returns an MdAttributeConcreteDAOIF that has the database index of the
  // given name.
  protected pointcut getMdAttributeDAOWithIndex(String indexName)
  :  call (* com.runwaysdk.dataaccess.cache.ObjectCache.getMdAttributeDAOWithIndex(String))
  && !within(AbstractTransactionManagement+)
  && args(indexName);

  Object around(String indexName) : getMdAttributeDAOWithIndex(indexName)
  {
    MdAttributeConcreteDAOIF mdAttributeConcreteDAOIF = null;

    mdAttributeConcreteDAOIF = this.getTransactionCache().getMdAttributeWithIndex(indexName);

    if (mdAttributeConcreteDAOIF == null)
    {
      mdAttributeConcreteDAOIF = (MdAttributeConcreteDAOIF) proceed(indexName);
    }

    return mdAttributeConcreteDAOIF;
  }

  protected pointcut updatedMdAttributeKey(MdAttributeDAO mdAttribute)
  :(execution (* com.runwaysdk.dataaccess.metadata.MdAttributeDAO.setAndBuildKey(..)) && target(mdAttribute));

  after(MdAttributeDAO mdAttribute)
    : updatedMdAttribute(mdAttribute)
  {
    this.getTransactionCache().newMdAttribute(mdAttribute);
  }

  // //////////////////////////////////////////////////////////////////////////////////
  // advice for determining which MdType's should have classes
  // generated/regenerated.
  // no generation should occur for objects that are imported.
  // //////////////////////////////////////////////////////////////////////////////////
  // MdAttribute objects that have been created.
  protected pointcut updatedMdAttribute(MdAttributeDAO mdAttribute)
  :(execution (* com.runwaysdk.dataaccess.metadata.MdAttributeDAO.save(..)) && target(mdAttribute));

  after(MdAttributeDAO mdAttribute)
    : updatedMdAttribute(mdAttribute)
  {
    if (!mdAttribute.isImport())
    {
      this.getTransactionCache().modifiedMdAttribute_CodeGen(mdAttribute);
    }
    this.getTransactionCache().newMdAttribute(mdAttribute);
  }

  // If a Role was added during this transaction, then it will not exist yet in
  // the metadata
  // cache. Consequently, requests for the Role from the metadata cache within
  // this transaction will result in a data not found exception.
  protected pointcut getRoleIF(String roleName)
  :  call (* com.runwaysdk.dataaccess.cache.ObjectCache.getRole(String))
  && !within(AbstractTransactionManagement+)
  && args(roleName);

  Object around(String roleName) : getRoleIF(roleName)
  {
    RoleDAOIF roleIF = null;

    roleIF = this.getTransactionCache().getRoleIF(roleName);

    if (roleIF == null)
    {
      roleIF = (RoleDAOIF) proceed(roleName);
    }

    return roleIF;
  }

  /**
   * Returns a set of <code>MdRelationshipDAOIF</code> ids for relationships in
   * which the <code>MdBusinessDAOIF</code> with the given id participates as a
   * parent.
   * 
   * @return set of <code>MdRelationshipDAOIF</code> ids
   */
  protected pointcut getParentMdRelationshipDAOids(String mdBusinessDAOid)
  :  call (* com.runwaysdk.dataaccess.cache.ObjectCache.getParentMdRelationshipDAOids(String))
  && args(mdBusinessDAOid)
  && !within(AbstractTransactionManagement+)
  && !within(AbstractTransactionCache+);

  Set<String> around(String mdBusinessDAOid)
  : getParentMdRelationshipDAOids(mdBusinessDAOid)
  {
    return this.getTransactionCache().getParentMdRelationshipDAOids(mdBusinessDAOid);
  }

  /**
   * Returns a set of <code>MdRelationshipDAOIF</code> ids for relationships in
   * which the <code>MdBusinessDAOIF</code> with the given id participates as a
   * child.
   * 
   * @return set of <code>MdRelationshipDAOIF</code> ids
   */
  protected pointcut getChildMdRelationshipDAOids(String mdBusinessDAOid)
  :  call (* com.runwaysdk.dataaccess.cache.ObjectCache.getChildMdRelationshipDAOids(String))
  && args(mdBusinessDAOid)
  && !within(AbstractTransactionManagement+)
  && !within(AbstractTransactionCache+);

  Set<String> around(String mdBusinessDAOid)
  : getChildMdRelationshipDAOids(mdBusinessDAOid)
  {
    return this.getTransactionCache().getChildMdRelationshipDAOids(mdBusinessDAOid);
  }

  // Get parents that were created during this transaction as well as existing
  // parents.
  // Parents created during this transaction are not in the main cache.
  protected pointcut getParents(CacheAllRelationshipDAOStrategy relationshipCollection, String businessDAOid, String relationshipType)
  :  call (* com.runwaysdk.dataaccess.cache.RelationshipDAOCollection.getParents(String, String))
  && target(relationshipCollection)
  && !within(AbstractTransactionManagement+)
  && !within(AbstractTransactionCache+)
  && args(businessDAOid, relationshipType);

  List<RelationshipDAOIF> around(CacheAllRelationshipDAOStrategy relationshipCollection, String businessDAOid, String relationshipType)
  : getParents(relationshipCollection, businessDAOid, relationshipType)
  {
    // ID could have been changed during the transaction and the id in the global cache could be different
    String objectCacheId = this.getTransactionCache().getBusIdForGetParentsMethod(businessDAOid, relationshipType);
    
    List<RelationshipDAOIF> relationshipList = proceed(relationshipCollection, objectCacheId, relationshipType);

    return this.getTransactionCache().getParents(relationshipList, businessDAOid, relationshipType);
  }



  // Get children that were created during this transaction as well as existing
  // children.
  // Children created during this transaction are not in the main cache.
  protected pointcut getChildren(CacheAllRelationshipDAOStrategy relationshipCollection, String businessDAOid, String relationshipType)
  :  call (* com.runwaysdk.dataaccess.cache.RelationshipDAOCollection.getChildren(String, String))
  && target(relationshipCollection)
  && !within(AbstractTransactionManagement+)
  && !within(AbstractTransactionCache+)
  && args(businessDAOid, relationshipType);

  List<RelationshipDAOIF> around(CacheAllRelationshipDAOStrategy relationshipCollection, String businessDAOid, String relationshipType)
  : getChildren(relationshipCollection, businessDAOid, relationshipType)
  {
    // ID could have been changed during the transaction and the id in the global cache could be different
    String objectCacheId = this.getTransactionCache().getBusIdForGetParentsMethod(businessDAOid, relationshipType);
    
    List<RelationshipDAOIF> relationshipList = proceed(relationshipCollection, objectCacheId, relationshipType);

    return this.getTransactionCache().getChildren(relationshipList, businessDAOid, relationshipType);
  }

  // Class collections
  protected pointcut addClassTypeCacheStrategy(String classType, CacheStrategy collection)
  :  call (* com.runwaysdk.dataaccess.cache.ObjectCache.addCacheStrategy(String, CacheStrategy))
  && args(classType, collection)
  && (!within(AbstractTransactionManagement+)
      && !within(AbstractTransactionCache+));

  void around(String classType, CacheStrategy collection)
    : addClassTypeCacheStrategy(classType, collection)
  {
    // this.getTransactionCache() should not be null, as this should occur
    // within a transaction
    this.getTransactionCache().updateEntityDAOCollection(classType, collection);
  }

  protected pointcut removeClassTypeCacheStrategy(String classType)
  :  call (* com.runwaysdk.dataaccess.cache.ObjectCache.removeCacheStrategy(String))
  && args(classType)
  && (!within(AbstractTransactionManagement+)
      && !within(AbstractTransactionCache+));

  void around(String classType)
    : removeClassTypeCacheStrategy(classType)
  {
    // this.getTransactionCache() should not be null, as this should occur
    // within a transaction
    CacheNoneBusinessDAOStrategy dummyCollection = new CacheNoneBusinessDAOStrategy(classType);
    this.getTransactionCache().deleteEntityDAOCollection(classType, dummyCollection);
  }

  /*
   * database deadlock detection Get DML tables -------------------
   * Database.buildSQLInsertStatement(String table, List<String> fields,
   * List<String> values) Database.buildSQLUpdateStatement(String table,
   * Map<String, String> values, String id)
   * 
   * 
   * Get DDL tables ------------------- Database.createClassTable(String table)
   * Database.createRelationshipTable(String table, boolean isUnique)
   * Database.createEnumerationTable(String tableName)
   * Database.deleteClassTable(String table)
   * Database.deleteRelationshipTable(String table, boolean isUnique)
   * Database.deleteEnumerationTable(String tableName)
   * Database.addDecField(String table, String field, String type, String
   * length, String decimal) Database.addField(String table, String field,
   * String formattedType) Database.addUniqueIndex(String table, String field)
   * Database.addNonUniqueIndex(String table, String field)
   * Database.dropNonUniqueIndex(String table, String field)
   * Database.dropUniqueIndex(String table, String field)
   * Database.addUniqueGroupAttributeIndex(String table, List<String>
   * attributeColumnNames) Database.dropUniqueGroupAttributeIndex(String table,
   * List<String> attributeColumnNames) Database.alterFieldType(String table,
   * String field, String newDbColumnType, String oldDbColumnType)
   * Database.dropField(String table, String field, String dbColumnType)
   * Database.addField(String, String, String, String)
   */

  protected abstract List<ProblemIF> getRequestProblemList();

  /**
   * Commits the DML database connection or releases the savepoint.
   */
  protected abstract void dmlCommit() throws SQLException;

  /**
   * Rollsback the DML database connection or rollsback and releases the
   * savepoint.
   */
  protected abstract void dmlRollback() throws SQLException;

  /**
   * Commits the DDL database connection or releases the savepoint.
   */
  protected abstract void ddlCommitAndClose() throws SQLException;

  /**
   * Rollsback the DDL database connection or rollsback and releases the
   * savepoint.
   */
  protected abstract void ddlRollbackAndClose() throws SQLException;

  protected void processThrowable(Throwable ex)
  {    
    if (ex instanceof RunwayException)
    {
      throw (RunwayException) ex;
    }
    if (ex instanceof SmartException)
    {
      throw (SmartException) ex;
    }
    else
    {
      // This should never happen, unless we (or a developer of stored
      // procedures) has made a mistake
      ProgrammingErrorException programmingErrorException = new ProgrammingErrorException(ex);
      programmingErrorException.setStackTrace(ex.getStackTrace());
      log.error(RunwayLogUtil.getExceptionLoggableMessage(ex), ex);
      throw programmingErrorException;
    }
  }
  
  /**
   * 
   * @param entityType
   * @return returns the <code>MdEntityDAOIF</code> that defines the given type.
   */
  protected MdEntityDAOIF getMdEntityDAO(String entityType)
  {
    MdEntityDAOIF mdEntityDAOIF = (MdEntityDAOIF)this.getTransactionCache().getMdClass(entityType);

    if (mdEntityDAOIF == null)
    {
      mdEntityDAOIF = ObjectCache.getMdEntityDAO(entityType);
    }
    
    return mdEntityDAOIF;
  }

}
