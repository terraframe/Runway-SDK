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

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.business.rbac.ActorDAOIF;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.business.state.StateMasterDAO;
import com.runwaysdk.business.state.StateMasterDAOIF;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.EnumerationAttributeItem;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdFacadeDAOIF;
import com.runwaysdk.dataaccess.MdIndexDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.TransientDAO;
import com.runwaysdk.dataaccess.TransitionDAO;
import com.runwaysdk.dataaccess.TransitionDAOIF;
import com.runwaysdk.dataaccess.cache.CacheStrategy;
import com.runwaysdk.dataaccess.metadata.MdActionDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdMethodDAO;
import com.runwaysdk.dataaccess.metadata.MdParameterDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.session.PermissionEntity;

public interface TransactionCacheIF
{
  /**
   * Returns a set of <code>MdRelationshipDAOIF</code> ids for relationships in
   * which the <code>MdBusinessDAOIF</code> with the given id participates as a
   * parent.
   * 
   * @return set of <code>MdRelationshipDAOIF</code> ids
   */
  public abstract Set<String> getParentMdRelationshipDAOids(String mdBusinessDAOid);

  /**
   * Returns a set of <code>MdRelationshipDAOIF</code> ids for relationships in
   * which the <code>MdBusinessDAOIF</code> with the given id participates as a
   * child.
   * 
   * @return set of <code>MdRelationshipDAOIF</code> ids
   */
  public abstract Set<String> getChildMdRelationshipDAOids(String mdBusinessDAOid);

  /**
   * Adds the given new or updated EntityDAO to the transaction cache.
   * 
   * <br/>
   * <b>Precondition:</b> entityDAO != null <br/>
   * <b>Postcondition:</b> entityDAO is added to the transaction cache
   * 
   * @param entityDAO
   *          EntityDAO to add to the cache.
   */
  public abstract void updateEntityDAO(EntityDAO entityDAO);
    
  /**
   * Stores an {@link EntityDAO} that was modified in this transaction in a transaction cache.
   * 
   * @param entityDAO
   */
  public abstract void storeTransactionEntityDAO(EntityDAO entityDAO);
  
  /**
   * Returns the id of the object before it was changed in this transaction.
   * 
   * @return id of the object before it was changed in this transaction.
   */
  public abstract String getOriginalId(String id);

  
  /**
   * When the id of an object changes, update all caches.
   * 
   * @param oldId
   * @param entityDAO
   */
  public abstract void changeCacheId(String oldId, EntityDAO entityDAO);

  /**
   * Adds the given entity to the map that stores references based on
   * type-keyname.
   * 
   * @param entityDAO
   */
  public abstract void addUpdatedEntityToKeyNameMap(EntityDAO entityDAO);

  /**
   * Marks entities to be cleared from the global cache. Should the entity be of a cached type,
   * the next request for the object from the global cache will refresh the object from the global cache.
   * 
   * @param entityId
   */
  public abstract void refreshEntityInGlobalCache(String entityId);
  
  /**
   * Adds the given new or updated TransientDAO to the transaction cache.
   * 
   * <br/>
   * <b>Precondition:</b> transientDAO != null <br/>
   * <b>Postcondition:</b> transientDAO is added to the transaction cache
   * 
   * @param transientDAO
   *          TransientDAO to add to the cache.
   */
  public abstract void updateTransientDAO(TransientDAO transientDAO);

  /**
   * Adds the given stateMaster to the cache.
   * 
   * @param stateMasterDAO
   */
  public abstract void updatedStateMaster(StateMasterDAO stateMasterDAO);

  /**
   * Returns all States that were defined for the given State Machine type that
   * were updated during this transaction.
   * 
   * @param type
   *          State Machine type.
   * 
   * @return all States that were defined for the given State Machine type that
   *         were updated during this transaction.
   */
  public abstract List<StateMasterDAOIF> getUpdatedStateMasters(String type);

  /**
   * Adds the given transitions to the cache.
   * 
   * @param transitionDAO
   */
  public abstract void updatedTransition(TransitionDAO transitionDAO);

  /**
   * Returns all Transitions that were defined for the given State Machine type
   * that were updated during this transaction.
   * 
   * @param type
   *          State Machine type.
   * 
   * @return all Transitions that were defined for the given State Machine type
   *         that were updated during this transaction.
   */
  public abstract Set<TransitionDAOIF> getUpdatedTransitions(String type);

  /**
   * Adds a reference to a newly created MdAttribute to the cache. If the
   * instance is not new, check if the visibility modifiers on the getter and
   * setter have changed. If so, then regenerate DTO code accordingly.
   * 
   * <br/>
   * <b>Precondition:</b> mdAttribute != null <br/>
   * <b>Postcondition:</b> mdAttribute is added to the transaction cache
   * 
   * @param mdAttribute
   */
  public abstract void modifiedMdAttribute_CodeGen(MdAttributeDAO mdAttribute);

  /**
   * Adds a <code>MdAttributeIF</code> that was added during this transaction
   * with the given key. This is used to fecth the attribute later by its key.
   * 
   * @param mdAttribute
   */
  public abstract void newMdAttribute(MdAttributeDAO mdAttribute);

  /**
   * Adds a reference to a newly deleted MdAttribute to the cache.
   * 
   * <br/>
   * <b>Precondition:</b> mdAttribute != null <br/>
   * <b>Postcondition:</b> mdAttribute is added to the transaction cache
   * 
   * @param mdAttribute
   */
  public abstract void deletedMdAttribute_CodeGen(MdAttributeDAO mdAttribute);

  /**
   * Adds a reference to a newly created MdRelationship to the cache.
   * 
   * <br/>
   * <b>Precondition:</b> MdRelationship != null <br/>
   * <b>Postcondition:</b> MdRelationship is added to the transaction cache
   * 
   * @param mdRelationship
   */
  public abstract void newMdRelationship_CodeGen(MdRelationshipDAO mdRelationship);

  /**
   * Adds a reference to a newly deleted MdRelationship to the cache.
   * 
   * <br/>
   * <b>Precondition:</b> MdRelationship != null <br/>
   * <b>Postcondition:</b> MdRelationship is added to the transaction cache
   * 
   * @param mdRelationship
   */
  public abstract void deletedMdRelationship_CodeGen(MdRelationshipDAO mdRelationship);

  /**
   * Adds a reference to a newly created EnumerationAttributeItem to the cache.
   * 
   * <br/>
   * <b>Precondition:</b> EnumerationAttributeItem != null <br/>
   * <b>Postcondition:</b> EnumerationAttributeItem is added to the transaction
   * cache
   * 
   * @param enumerationAttributeItem
   *          EnumerationAttributeItem to add to the cache.
   */
  public abstract void newEnumerationAttributeItem_CodeGen(EnumerationAttributeItem enumerationAttributeItem);

  /**
   * Adds a reference to a newly deleted EnumerationAttributeItem to the cache.
   * 
   * <br/>
   * <b>Precondition:</b> EnumerationAttributeItem != null <br/>
   * <b>Postcondition:</b> EnumerationAttributeItem is added to the transaction
   * cache
   * 
   * @param enumerationAttributeItem
   *          EnumerationAttributeItem to add to the cache.
   */
  public abstract void deletedEnumerationAttributeItem_CodeGen(EnumerationAttributeItem enumerationAttributeItem);

  /**
   * Adds a reference to a newly created or updated MdMethod to the cache.
   * 
   * <br/>
   * <b>Precondition:</b> mdMethod != null <br/>
   * <b>Postcondition:</b> mdMethod is added to the transaction cache
   * 
   * @param mdMethod
   *          MdMethod to add to the cache.
   */
  public abstract void updatedMdMethod_CodeGen(MdMethodDAO mdMethod);

  /**
   * Adds a reference to a newly created or updated MdMethod to the cache.
   * 
   * <br/>
   * <b>Precondition:</b> mdMethod != null <br/>
   * <b>Postcondition:</b> mdMethod is added to the transaction cache
   * 
   * @param mdMethod
   *          MdMethod to add to the cache.
   */
  public abstract void updatedMdMethod_WebServiceDeploy(MdMethodDAO mdMethod);

  /**
   * Adds a reference to a newly created or updated MdAction to the cache.
   * 
   * <br/>
   * <b>Precondition:</b> mdAction != null <br/>
   * <b>Postcondition:</b> mdAction is added to the transaction cache
   * 
   * @param mdAction
   *          MdAction to add to the cache.
   */
  public abstract void updatedMdAction_CodeGen(MdActionDAO mdAction);

  /**
   * Adds a reference to a newly created or updated MdParameter to the cache.
   * 
   * <br/>
   * <b>Precondition:</b> mdParameter != null <br/>
   * <b>Postcondition:</b> mdParameter is added to the transaction cache
   * 
   * @param mdParameter
   *          MdParameter to add to the cache.
   */
  public abstract void updatedMdParameter_CodeGen(MdParameterDAO mdParameter);

  /**
   * Adds a reference to a newly created or updated MdParameter to the cache.
   * 
   * <br/>
   * <b>Precondition:</b> mdParameter != null <br/>
   * <b>Postcondition:</b> mdParameter is added to the transaction cache
   * 
   * @param mdParameter
   *          MdParameter to add to the cache.
   */
  public abstract void updatedMdParameter_WebServiceDeploy(MdParameterDAO mdParameter);

  /**
   * Adds a reference to a newly created MdType to the cache.
   * 
   * <br/>
   * <b>Precondition:</b> MdType != null <br/>
   * <b>Postcondition:</b> MdType is added to the transaction cache
   * 
   * @param mdAttribute
   *          MdType to add to the cache.
   */
  public abstract void updatedMdType(MdTypeDAO mdType);

  /**
   * Adds a reference to a deleted MdType to the cache.
   * 
   * <br/>
   * <b>Precondition:</b> MdType != null <br/>
   * <b>Postcondition:</b> MdType is added to the transaction cache
   * 
   * @param mdAttribute
   *          MdType to add to the cache.
   */
  public abstract void deletedMdType(MdTypeDAOIF mdTypeIF);

  /**
   * Adds the given new or updated EntityDAOCollection to the transaction cache.
   * 
   * <br/>
   * <b>Precondition:</b> type != null <br/>
   * <b>Precondition:</b> EntityDAOCollection != null <br/>
   * <b>Postcondition:</b> EntityDAOCollection is added to the transaction cache
   * 
   * @param entityDAOCollectoin
   *          EntityDAOCollection to add to the cache
   */
  public abstract void updateEntityDAOCollection(String type, CacheStrategy entityDAOCollectoin);

  /**
   * Returns the <code>EntityDAOIF</code> with the given id from the Transaction cache.
   * 
   * <br/>
   * <b>Precondition:</b> id != null <br/>
   * <b>Precondition:</b> !id.trim().equals("") <br/>
   * <b>Postcondition:</b> true
   * 
   * @param id entity id
   *          
   * @return <code>EntityDAOIF</code>  with the given id if it exists in the transaction cache,
   *         false otherwise.
   */
  public abstract EntityDAOIF getEntityDAO(String id);
  
  /**
   * Returns the <code>EntityDAOIF</code> with the given id directly from the Transaction cache,
   * null if no such object with the id exists.
   * 
   * @param id entity id
   * 
   * @return <code>EntityDAOIF</code>  with the given id if it exists in the transaction cache,
   *         false otherwise.
   */
  public abstract EntityDAOIF getEntityDAOIFfromCache(String id);

  /**
   * Returns a map of all entities that were updated during this transaction.
   * 
   * @return map of all entities that were updated during this transaction.
   */
  public Map<String, TransactionItemEntityDAOAction> getEntityDAOIDsMap();

  /**
   * Returns the ID that should be passed on to the ObjectCache for fetching parents objects with the given id.
   * The id could have been changed during the transaction, and the id in the global cache may be different 
   * than the id for the object in the current transaction.
   * 
   * @param businessDAOid
   * @param relationshipType
   * @return ID that should be passed on to the ObjectCache for fetching parents objects with the given id.
   */
  public abstract String getBusIdForGetParentsMethod(String businessDAOid, String relationshipType);
  
  /**
   * Returns parent relationships for the object with the given id in the
   * transaction.
   * 
   * @param relationshipList
   *          relationships returned from the global source.
   * @param businessDAOid
   * @param relationshipType
   * 
   * @return relationships modified based on what occurred in this transaction.
   */
  public abstract List<RelationshipDAOIF> getParents(List<RelationshipDAOIF> relationshipList, String businessDAOid, String relationshipType);

  /**
   * Returns child relationships for the object with the given id in the
   * transaction.
   * 
   * @param relationshipList
   *          relationships returned from the global source.
   * @param businessDAOid
   * @param relationshipType
   * 
   * @return relationships modified based on what occurred in this transaction.
   */
  public abstract List<RelationshipDAOIF> getChildren(List<RelationshipDAOIF> relationshipList, String businessDAOid, String relationshipType);

  /**
   * Returns the EntityDAO with the given type and keyname from the Transaction
   * cache.
   * 
   * <br/>
   * <b>Precondition:</b> type != null <br/>
   * <b>Precondition:</b> !type.trim().equals("") <br/>
   * <b>Precondition:</b> key != null <br/>
   * <b>Precondition:</b> !key.trim().equals("")
   * 
   * @param id
   *          entity id
   * @return EntityDAO with the given id if it exists in the transaction cache,
   *         false otherwise.
   */
  public abstract EntityDAO getEntityDAO(String type, String key);

  /**
   * Returns a <code>MdClassDAOIF</code> instance of the metadata for the given
   * type. Returns null if the <code>MdClassDAOIF</code> is not in this
   * transaction cache.
   * 
   * <br/>
   * <b>Precondition:</b> type != null <br/>
   * <b>Precondition:</b> !type.trim().equals("") <br/>
   * <b>Precondition:</b> type is a valid class defined in the database <br/>
   * <b>Postcondition:</b> return value null if <code>MdClassDAOIF</code> is not
   * in the cache. <br/>
   * <b>Postcondition:</b> Returns a <code>MdClassDAOIF</code> instance if the
   * metadata for the given type (
   * <code>MdClassDAOIF.definesType().equals(type)</code>)
   * 
   * @param type
   *          Name of the class
   * @return <code>MdClassDAOIF</code> instance of the metadata for the given
   *         type or null if it is not in the transaction cache.
   */
  public abstract MdClassDAOIF getMdClass(String type);

  /**
   * Returns a {@link MdClassDAOIF} instance of the metadata for the given
   * mdClassRootId. Returns null if the {@link MdClassDAOIF} is not in this
   * transaction cache.
   * 
   * <br/>
   * <b>Precondition:</b> mdClassRootId != null <br/>
   * <b>Precondition:</b> !mdClassRootId.trim().equals("") <br/>
   * <b>Precondition:</b> mdClassRootId is a valid class defined in the database <br/>
   * <b>Postcondition:</b> return value null if <code>MdClassDAOIF</code> is not
   * in the cache. <br/>
   * <b>Postcondition:</b> Returns a <code>MdClassDAOIF</code> instance if the
   * metadata for the given mdClassRootId
   * 
   * @param mdClassRootId
   *          Name of the class
   * @return {@link MdClassDAOIF} instance of the metadata for the given
   *         mdClassRootId or null if it is not in the transaction cache.
   */
  public abstract MdClassDAOIF getMdClassDAOByRootId(String mdClassRootId);

  /**
   * Returns {@link MdAttributeDAOIF} that was added during this transaction
   * with the given key, returns null if no such attribute was created during
   * this transaction.
   * 
   * @param mdAttributeKey
   *          of the {@link MdAttributeDAOIF}
   * @return {@link MdAttributeDAOIF} that was added during this transaction
   *         with the given key, returns null if no such attribute was created
   *         during this transaction.
   */
  public abstract MdAttributeDAOIF getAddedMdAttribute(String mdAttributeKey);

  /**
   * Returns {@link MdAttributeDAOIF}s that were added during this transaction
   * where the key of the map is the key of the {@link MdAttributeDAOIF}.
   * 
   * @return {@link MdAttributeDAOIF}s that were added during this transaction
   *         where the key of the map is the key of the {@link MdAttributeDAOIF}
   *         .
   */
  public abstract Map<String, MdAttributeDAO> getAddedMdAttributes();

  /**
   * Returns a MdFacadeIF instance of the metadata for the given type. Returns
   * null if the MdFacadeIF is not in this transaction cache.
   * 
   * <br/>
   * <b>Precondition:</b> type != null <br/>
   * <b>Precondition:</b> !type.trim().equals("") <br/>
   * <b>Precondition:</b> type is a valid class defined in the database <br/>
   * <b>Postcondition:</b> return value null if MdFacadeIF is not in the cache. <br/>
   * <b>Postcondition:</b> Returns a MdFacadeIF instance of the metadata for the
   * given type (MdFacadeIF.definesType().equals(type)
   * 
   * @param type
   *          Name of the class
   * @return MdFacadeIF instance of the metadata for the given type or null if
   *         it is not in the transaction cache.
   */
  public abstract MdFacadeDAOIF getMdFacade(String type);

  /**
   * Returns the role with the given name if it was modified during this
   * transaction.
   * 
   * @param roleName
   * @return role with the given name if it was modified during this
   *         transaction.
   */
  public abstract RoleDAOIF getRoleIF(String roleName);

  /**
   * 
   * @param type
   * @return
   */
  public abstract MdEnumerationDAOIF getMdEnumerationDAO(String enumerationType);

  /**
   * 
   * @param type
   * @return
   */
  public abstract MdIndexDAOIF getMdIndexDAO(String indexName);

  /**
   * Returns a MdAttributeIF that uses the database index of a given name.
   * 
   * <br/>
   * <b>Precondition:</b> indexName != null <br/>
   * <b>Precondition:</b> !indexName.trim().equals("") <br/>
   * <b>Postcondition:</b> Returns a MdAttributeIF where
   * (MdAttributeIF.getIndexName().equals(indexName)
   * 
   * @param indexName
   *          Name of the database index.
   * @return MdAttributeIF that uses the database index of a given name.
   */
  public abstract MdAttributeConcreteDAOIF getMdAttributeWithIndex(String indexName);

  /**
   * Should be called AFTER the save method on an MdAttributeConcreteDAO,
   * otherwise the indexName will not have been calculated.
   * 
   * @param mdAttributeConcreteDAO
   */
  public abstract void setMdAttributeWithIndex(MdAttributeConcreteDAO mdAttributeConcreteDAO);

  /**
   * Returns true if the given delete method signature has already been executed
   * on the entity with the given id, false otherwise.
   * 
   * @param entityDAO
   *          EntityDAO.
   * @param signature
   *          method signature.
   * 
   * @return true if the given delete method signature has already been executed
   *         on the entity with the given id, false otherwise.
   */
  public abstract boolean hasExecutedEntityDeleteMethod(EntityDAO entityDAO, String signature);

  /**
   * Sets the given delete method signature for the entity with the given id has
   * having been executed in this transaction.
   * 
   * @param entityDAO
   *          EntityDAO.
   * @param signature
   *          method signature
   */
  public abstract void setExecutedEntityDeleteMethod(EntityDAO entityDAO, String signature);

  /**
   * Removes the deleted method signature for the entity with the given id. Once the outer most delete method for
   * the entity has been executed, we no longer keep track of it.
   * 
   * @param entityDAO
   *          EntityDAO.
   * @param signature
   *          method signature
   *          
   * @return true if the root delete method has completed execution, false otherwise.
   */
  public boolean removeExecutedDeleteMethod(EntityDAO entityDAO, String signature);
  
  /**
   * Indicates whether is object has been deleted during the transaction.
   * 
   * @param entityDAO
   */
  public boolean hasBeenDeletedInTransaction(EntityDAO entityDAO);

  /**
   * Called when an <code>EntityDAO</code> is being created in case within the transaction
   * and object with the same id has been previously deleted.
   * 
   * @param entityDAO
   */
  public void removeHasBeenDeletedInTransaction(EntityDAO entityDAO);

  /**
   * Adds the given EntityDAO to the transaction cache to be marked to be
   * deleted.
   * 
   * <br/>
   * <b>Precondition:</b> entityDAO != null <br/>
   * <b>Postcondition:</b> entityDAO is added to the transaction cache to be
   * deleted
   * 
   * @param entityDAO
   *          EntityDAO to add to the cache to be delted
   */
  public abstract void deleteEntityDAO(EntityDAO entityDAO);

  /**
   * Returns all MdFacades that need to have web services regenerated for them.
   * 
   * @return
   */
  public abstract Set<MdFacadeDAOIF> getMdFacadesForServicesDeploy();

  /**
   * Returns all MdFacades that need to have web services undeployed.
   * 
   * @return
   */
  public abstract Set<MdFacadeDAOIF> getMdFacadesForServicesUndeploy();

  /**
   * Removes the given deleted EntityDAOCollection from the cache.
   * 
   * <br/>
   * <b>Precondition:</b> type != null <br/>
   * <b>Precondition:</b> EntityDAOCollection != null <br/>
   * <b>Postcondition:</b> EntityDAOCollection is added to the transaction cache
   * 
   * @param elementDAO
   *          EntityDAOCollection to add to the cache
   */
  public abstract void deleteEntityDAOCollection(String type, CacheStrategy entityDAOCollection);

  /**
   * Adds the given Relationship to the transaction cache.
   * 
   * <br/>
   * <b>Precondition:</b> relationshipDAO != null <br/>
   * <b>Postcondition:</b> relationshipDAO is added to the transaction cache
   * 
   * @param relationshipDAO
   *          Relationship to add to the cache
   */
  public abstract void addRelationship(RelationshipDAO relationshipDAO);

  /**
   * Call this method if the id of the relationshipId has changed during the transaction.
   * 
   * <br/>
   * <b>Precondition:</b> relationshipDAO != null <br/>
   * <b>Precondition:</b> !relationshipDAO.isNew() <br/>
   * <b>Postcondition:</b> relationship is added to the transaction cache
   * 
   * @param relationshipDAO
   *          Relationship to add to the cache
   */
  public abstract void updateRelationshipId(RelationshipDAO relationshipDAO);
 
  /**
   * True if parents have been added to the given object of the given
   * relationship type.
   * 
   * <br/>
   * <b>Precondition:</b> businessDAOid != null <br/>
   * <b>Precondition:</b> !businessDAOid.trim().equals("") <br/>
   * <b>Precondition:</b> relationshipType != null <br/>
   * <b>Precondition:</b> !relationshipType.trim().equals("") <br/>
   * <b>Postcondition:</b> Returns a Map of Relationship objects of the given
   * relationship type that are parents of the BusinessDAO with given
   * BusinessDAO ID that have been added during this transaction.
   * 
   * @param businessDAOid
   * 
   * @return True if parents have been added to the given object of the given
   *         relationship type.
   */
  public abstract boolean hasAddedParents(String businessDAOid, String relationshipType);

  /**
   * True if parents have been removed from the given object of the given
   * relationship type.
   * 
   * <br/>
   * <b>Precondition:</b> businessDAOid != null <br/>
   * <b>Precondition:</b> !businessDAOid.trim().equals("") <br/>
   * <b>Precondition:</b> relationshipType != null <br/>
   * <b>Precondition:</b> !relationshipTypetrim().equals("") <br/>
   * <b>Postcondition:</b> Returns a Map of Relationship objects of the given
   * relationship type that are parents of the BusinessDAO with given
   * BusinessDAO ID that have been removed during this transaction.
   * 
   * @param businessDAOid
   * 
   * @return True if parents have been removed from the given object of the
   *         given relationship type..
   */
  public abstract boolean hasRemovedParents(String businessDAOid, String relationshipType);

  /**
   * True if children have been added to the given object of the given
   * relationship type, false otherwise.
   * 
   * <br/>
   * <b>Precondition:</b> businessDAOid != null <br/>
   * <b>Precondition:</b> !businessDAOid.trim().equals("") <br/>
   * <b>Precondition:</b> relationshipType != null <br/>
   * <b>Precondition:</b> !relationshipType.trim().equals("") <br/>
   * <b>Postcondition:</b> Returns a Map of Relationship objects of the given
   * relationship type that are children of the BusinessDAO with given
   * BusinessDAO ID that have been added during this transaction.
   * 
   * @param businessDAOid
   * @return True if children have been added to the given object of the given
   *         relationship type, false otherwise.
   */
  public abstract boolean hasAddedChildren(String businessDAOid, String relationshipType);

  /**
   * True if children have been removed to the given object of the given
   * relationship type, false otherwise.
   * 
   * <br/>
   * <b>Precondition:</b> businessDAOid != null <br/>
   * <b>Precondition:</b> !businessDAOid.trim().equals("") <br/>
   * <b>Precondition:</b> relationshipType != null <br/>
   * <b>Precondition:</b> !relationshipType.trim().equals("") <br/>
   * <b>Postcondition:</b> Returns a Map of Relationship objects of the given
   * relationship type that are children of the BusinessDAO with given
   * BusinessDAO ID that have been added during this transaction.
   * 
   * @param businessDAOid
   * @return True if children have been removed to the given object of the given
   *         relationship type, false otherwise.
   */
  public abstract boolean hasRemovedChildren(String businessDAOid, String relationshipType);

  /**
   * Returns the added parent relationships for this {@link BusinessDAOIF}.
   * 
   * @param businessDAOid
   * @param relationshipType
   * 
   * @return parent relationships for this {@link BusinessDAOIF}.
   */
  public abstract Set<String> getAddedParentRelationshipDAOset(String businessDAOid, String relationshipType);

  /**
   * Returns the added child relationships for this {@link BusinessDAOIF}.
   * 
   * @param businessDAOid
   * @param relationshipType
   * 
   * @return added child relationships for this {@link BusinessDAOIF}.
   */
  public abstract Set<String> getAddedChildRelationshipDAOset(String businessDAOid, String relationshipType);

  /**
   * Returns the deleted child relationships for this {@link BusinessDAOIF}.
   * 
   * @param businessDAOid
   * @param relationshipType
   * 
   * @return deleted child relationships for this {@link BusinessDAOIF}.
   */
  public abstract Set<String> getDeletedChildRelationshipDAOset(String businessDAOid, String relationshipType);

  /**
   * Returns the deleted parent relationships for this {@link BusinessDAOIF}.
   * 
   * @param businessDAOid
   * @param relationshipType
   * 
   * @return deleted parent relationships for this {@link BusinessDAOIF}.
   */
  public abstract Set<String> getDeletedParentRelationshipDAOset(String businessDAOid, String relationshipType);

  /**
   * Adds the given Relationship to the transaction cache to be marked to be
   * deleted.
   * 
   * <br/>
   * <b>Precondition:</b> relationship != null <br/>
   * <b>Postcondition:</b> relationship is added to the transaction cache to be
   * deleted
   * 
   * @param relationshipDAO
   *          Relationship to add to the cache to be delted
   */
  public abstract void deleteRelationship(RelationshipDAO relationshipDAO);

  /**
   * Notify the actor that its permissions have been changed.
   * 
   * @param actorIF
   */
  public abstract void notifyActors(ActorDAOIF actorIF);

  /**
   * Unregisters an actor from being notified of permission changes
   * 
   * @param actorIF
   */
  public abstract void unregisterPermissionEntity(PermissionEntity permissionEntity);

  /**
   * 
   * @param tableName
   */
  public abstract void addDMLTableName(String tableName);

  /**
   * 
   * @param tableName
   */
  public abstract void performDDLTable(String tableName);

  public void close();
}
