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

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.business.rbac.ActorDAOIF;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeVirtualInfo;
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.constants.graph.MdEdgeInfo;
import com.runwaysdk.dataaccess.AttributeEnumerationIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.EnumerationAttributeItem;
import com.runwaysdk.dataaccess.EnumerationItemDAO;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdAttributeVirtualDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdIndexDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.TransientDAO;
import com.runwaysdk.dataaccess.cache.CacheStrategy;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.cache.RelationshipDAOCollection;
import com.runwaysdk.dataaccess.database.DatabaseException;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdEntityDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdIndexDAO;
import com.runwaysdk.dataaccess.metadata.MdMethodDAO;
import com.runwaysdk.dataaccess.metadata.MdParameterDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdGraphClassDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.PermissionEntity;
import com.runwaysdk.session.PermissionObserver;
import com.runwaysdk.system.metadata.MdClass;
import com.runwaysdk.system.metadata.MdRelationship;
import com.runwaysdk.system.metadata.MdType;
import com.runwaysdk.util.IdParser;

public abstract class AbstractTransactionCache implements TransactionCacheIF
{
  final static Logger                                        logger = LoggerFactory.getLogger(AbstractTransactionCache.class);

  protected ReentrantLock                                    transactionStateLock;

  /**
   * Keeps track of which entities have been deleted. Value: Entity OID + method
   * signature <br/>
   * <b>invariant</b> deletedEntitySignatures != null
   */
  protected Set<String>                                      deletedEntitySignatures;

  /**
   * Keeps track of how many times a delete method for a given Entity is called.
   * Every time a delete method is called, the count is incremented. Every time
   * a delete method completes execution, the counter is decremented. When the
   * count is zero, then we know that the object has been deleted.
   */
  protected Map<String, Integer>                             deletedEntitySignatureCount;

  /**
   * Contains all EntityDAOs that were added or modified during a transaction
   * where the key is the entity oid.. <br/>
   * <b>invariant</b> updatedEntityDAOIdMap != null
   */
  protected Map<String, TransactionItemEntityDAOAction>      updatedEntityDAOIdMap;

  /**
   * Contains all {@link EntityDAO}s that were added or modified during a
   * transaction where the key is the type plus the keyname of the entity. The
   * value is the oid of the {@link EntityDAO} <br/>
   * <b>invariant</b> updatedEntityDAOKeyMap != null
   */
  protected Map<String, String>                              updatedEntityDAOKeyMap;

  /**
   * Sometimes entities that are cached should be cleared from the global cache
   * at the end of a transaction. Such objects will be refreshed in the global
   * cache the next time they are referenced. {@link EntityDAO} ids should not
   * bee in both this map and in the updatedEntityDAOIdMap. If an oid is added
   * to updatedEntityDAOIdMap it should be removed from this set.
   * 
   * Key is the oid of a {@link EntityDAO} <br/>
   * 
   * <b>invariant</b> entitiesToRefreshFromGlobalCache != null
   */
  protected Set<String>                                      entitiesToRefreshFromGlobalCache;

  /**
   * key is the {@link BusinessDAO} value contains change in relationships
   */
  protected Map<String, TransactionBusinessDAORelationships> updatedBusinessDAORelationships;

  /**
   * Contains all EntityDAOColletions that were added or modified during a
   * transaction. <br/>
   * <b>invariant</b> updatedEntityNameCacheStrategyMap != null
   */
  protected Map<String, TransactionItemStrategyAction>       updatedEntityNameCacheStrategyMap;

  /**
   * Contains all EntityDAOColletions that were deleted during a transaction.
   * <br/>
   * <b>invariant</b> deleteEntityNameCacheStrategyMap != null
   */
  protected Map<String, TransactionItemStrategyAction>       deleteEntityNameCacheStrategyMap;

  /**
   * Contains all TransactionItems (that are transient) that were modified
   * during this transaction. Used to record the order in which TransactionItems
   * were modified. <br/>
   * <b>invariant</b> updatedTransientTransactionItemList != null
   */
  protected List<TransactionItemTransientDAOAction>          updatedTransientTransactionItemList;

  /**
   * Contains all TransactionItems (that are collections) that were modified
   * during this transaction. Used to record the order in which TransactionItems
   * were modified. <br/>
   * <b>invariant</b> updatedCollectionTransactionItemList != null
   */
  protected List<TransactionItemStrategyAction>              updatedCollectionTransactionItemList;

  /**
   * Contains a reference to all {@link MdClass} objects that were modified
   * during this transaction. The key is the name of the class. Used to allow
   * sets of classes to be defined within a transaction. The value is the oid of
   * the {@link MdClass}. <br/>
   * <b>invariant</b> updatedMdClassDefinedTypeMap != null
   */
  protected Map<String, String>                              updatedMdClassDefinedTypeMap;

  /**
   * Contains a reference to all {@link MdClass} objects that were modified
   * during this transaction. The key is the name of the class. Used to allow
   * sets of classes to be defined within a transaction. The value is the oid of
   * the {@link MdClass}. <br/>
   * <b>invariant</b> updatedMdClassDefinedTypeMap != null
   */
  protected Map<String, String>                              updatedMdClassTableNameMap;

  /**
   * Contains a reference to all {@link MdClass}. objects that were modified
   * during this transaction. The key is the root oid of the class. The value is
   * the oid of the {@link MdClass}.
   * 
   * <br/>
   * <b>invariant</b> updatedMdClassRootIdMap != null
   */
  protected Map<String, String>                              updatedMdClassRootIdMap;

  /**
   * Contains a reference to all {@link RoleDAOIF} that were modified during
   * this transaction. The key is the name of the role. The value is the oid of
   * the {@link RoleDAOIF}. Used to allow sets of roles to be defined and
   * assigned to users and actors within a transaction. <br/>
   * <b>invariant</b> updatedRoleIFMap != null
   */
  protected Map<String, String>                              updatedRoleIFMap;

  /**
   * Maps state master objects for a given state machine type that were modified
   * during this transaction. Key: State Master type Value: Id of
   * {@link StateMasterDAO} <br/>
   * <b>invariant</b> updatedStateMasterMap != null
   */
  protected Map<String, Set<String>>                         updatedStateMasterMap;

  /**
   * Maps transition objects for a given state machine type that were modified
   * during this transaction. Key: State Master type Value: Id of
   * {@link TransitionDAO} <br/>
   * <b>invariant</b> updatedTransitionMap != null
   */
  protected Map<String, Set<String>>                         updatedTransitionMap;

  /**
   * Contains a reference to all {@link MdAttributeDAO} objects that were
   * created during this transaction. The key is the oid of the object. Used to
   * determine MdEntities (the defining entity) that need to have Java classes
   * generated or regenerated.
   * 
   * <br/>
   * <b>invariant</b> newMdAttributeSet_CodeGeneration != null
   */
  protected Set<String>                                      newMdAttributeSet_CodeGeneration;

  /**
   * Contains a reference to {@link MdAttributeDAO} objects that were added
   * during this transaction. The key is the key of the object. The value is the
   * oid of the{@link MdAttributeDAO} object.
   */
  protected DualHashBidiMap<String, String>                  addedMdAttrubiteKeyMap;

  /**
   * Contains a reference to all {@link MdAttributeDAOIF} objects that were
   * deleted during this transaction. The value is the oid of the
   * {@link MdAttributeDAOIF} Used to determine MdEntities (the defining entity)
   * that need to have Java classes generated or regenerated.
   * 
   * <br/>
   * <b>invariant</b> deletedMdAttributeSet_CodeGeneration != null
   */
  protected Set<String>                                      deletedMdAttributeSet_CodeGeneration;

  /**
   * Contains a reference to all {@link MdRelationshipDAO} objects that were
   * created during this transaction. The key is the oid of the
   * {@link MdRelationshipDAO} object. Used to determine MdBusiness (parent or
   * child class) that need to have Java classes generated or regenerated.
   * 
   * <br/>
   * <b>invariant</b> newMdRelationshipSet_CodeGeneration != null
   */
  protected Set<String>                                      newMdRelationshipSet_CodeGeneration;

  /**
   * Contains a reference to all {@link MdRelationshipDAOIF} objects that were
   * deleted during this transaction. The key is the oid of the
   * {@link MdRelationshipDAOIF} object. Used to determine MdBusiness (parent or
   * child class) that need to have Java classes generated or regenerated.
   * 
   * <br/>
   * <b>invariant</b> deletedMdRelationshipSet_CodeGeneration != null
   */
  protected Set<String>                                      deletedMdRelationshipSet_CodeGeneration;

  /**
   * Contains a reference to all {@link EnumerationAttributeItem} objects that
   * were created during this transaction. The key is the oid of the
   * {@link EnumerationAttributeItem} object. Used to determine MdEnumeration
   * objects that need to have Java classes generated or regenerated.
   * 
   * <br/>
   * <b>invariant</b> newEnumerationAttributeItemSet_CodeGeneration != null
   */
  protected Set<String>                                      newEnumerationAttributeItemSet_CodeGeneration;

  /**
   * Contains a reference to all {@link EnumerationAttributeItem} objects that
   * were deleted during this transaction. The key is the oid of the
   * {@link EnumerationAttributeItem} object. Used to determine
   * {@link MdEnumerationDAO} objects that need to have Java classes generated
   * or regenerated.
   * 
   * <br/>
   * <b>invariant</b> deletedEnumerationAttributeItemSet_CodeGeneration != null
   */
  protected Set<String>                                      deletedEnumerationAttributeItemSet_CodeGeneration;

  /**
   * Contains a reference to all {@link MdMethodDAO} objects that were created,
   * updated, or deleted during this transaction. The key is the oid of the
   * object. Used to determine {@link MdEntityDAO} or {@link MdFacadeDAO}
   * objects that need to have Java classes generated or regenerated.
   * 
   * <br/>
   * <b>invariant</b> updatedMdMethod_CodeGeneration != null
   */
  protected Set<String>                                      updatedMdMethod_CodeGeneration;

  /**
   * Contains a reference to all {@link MdParameterDAO} objects that were
   * created, updated, or deleted during this transaction. The key is the oid of
   * the object. Used to determine {@link MdEntityDAO} or {@link MdFacadeDAO}
   * objects that need to have Java classes generated or regenerated.
   * 
   * <br/>
   * <b>invariant</b> updatedMdParameter_CodeGeneration != null
   */
  protected Set<String>                                      updatedMdParameter_CodeGeneration;

  /**
   * Contains a reference to all {@link MdType} objects that were created during
   * this transaction. The key is the oid of the object. Used to determine
   * {@link MdType} objects that need to have Java classes generated.
   * 
   * <br/>
   * <b>invariant</b> updatedMdTypeSet_CodeGeneration != null
   */
  protected Set<String>                                      updatedMdTypeSet_CodeGeneration;

  /**
   * Contains a reference to all {@link MdType} objects that were deleted during
   * this transaction. The key is the type defined by the {@link MdType} object.
   * Used to determine {@link MdType} objects that need to have Java classes
   * deleted and unloaded.
   * 
   * <br/>
   * <b>invariant</b> deletedMdTypeMap_CodeGeneration != null
   */
  protected Map<String, String>                              deletedMdTypeMap_CodeGeneration;

  /**
   * Contains a reference to all {@link EnumerationItemDAO} objects that were
   * modified during this transaction. The key is the oid of the object. Used to
   * determine {@link MdEnumerationDAO}s that need to be regenerated, as their
   * corresponding Java enumerations contain the values as strings within the
   * class definition.
   * 
   * <br/>
   * <b>invariant</b> updatedEnumerationItemSet_CodeGeneration != null
   */
  protected Set<String>                                      updatedEnumerationItemSet_CodeGeneration;

  /**
   * Contains a reference to all {@link MdEnumerationDAOIF} objects that were
   * created or deleted during this transaction. The key is the oid of the
   * object. The value is the oid of the {@link MdEnumerationDAOIF} object. Used
   * to determine the enumeration master list classes that need to be
   * regenerated.
   * 
   * <br/>
   * <b>invariant</b> updatedEnumerationItemMap != null
   */
  protected Map<String, String>                              updatedMdEnumerationMap_CodeGeneration;

  /**
   * Contains a reference to all {@link MdEnumerationDAO} objects that were
   * modified during this transaction. The key is the name of the enumeration
   * type. The value is the oid of the {@link MdEnumerationDAO}object. Used to
   * allow sets of classes to be defined within a transaction. <br/>
   * <b>invariant</b> updatedMdEnumerationMap != null
   */
  protected Map<String, String>                              updatedMdEnumerationMap;

  /**
   * Contains a reference to all {@link MdIndexDAO} objects that were modified
   * during this transaction. The key is the name of the index. The value is the
   * oid of the {@link MdIndexDAO} object. Used to allow sets of classes to be
   * defined within a transaction. <br/>
   * <b>invariant</b> updatedMdIndexMap != null
   */
  protected Map<String, String>                              updatedMdIndexMap;

  /**
   * Contains a reference to all {@link MdAttributeConcreteDAO} objects that
   * were modified during this transaction. The key is the name of the index.
   * The value is the oid of the {@link MdAttributeConcreteDAO} object. Used to
   * allow sets of classes to be defined within a transaction. <br/>
   * <b>invariant</b> indexNameMap != null
   */
  protected Map<String, String>                              indexNameMap;

  /**
   * Contains Actors that need to be notified that their permissions have
   * changed.
   */
  protected Set<ActorDAOIF>                                  notifyActors;

  /**
   * Contains Actors that need to be unregistered from the Permission manager so
   * that they do not need to be notified when their permissions change.
   */
  protected Set<PermissionEntity>                            unregisterPermissionEntity;

  /**
   * Set of table names used in SQL DML statements.
   */
  protected Set<String>                                      dmlTableName;

  /**
   * Contains instances of {@link RelationshipCacheAllStrategy} to record
   * relationships that have been added during this transaction.
   * {@link RelationshipCacheAllStrategy} keeps track of relationships that have
   * been added to individual EntityDAOs. <br/>
   * <b>invariant</b> cachedAddedRelationships != null
   */
  protected Map<String, RelationshipDAOCollection>           cachedAddedRelationships;

  /**
   * Contains instances of {@link RelationshipCacheAllStrategy} to record
   * relationships that have been removed during this transaction.
   * {@link RelationshipCacheAllStrategy} keeps track of relationships that have
   * been removed to individual EntityDAOs. <br/>
   * <b>invariant</b> cachedRemovedRelationships != null
   */
  protected Map<String, RelationshipDAOCollection>           cachedRemovedRelationships;

  /**
   * Key: {@link MdBusinessDAO} oid. Value: {@link MdRelationship} ids where
   * MdBusiness participates as a parent.
   */
  protected Map<String, Set<String>>                         mdBusinessParentMdRelationships;

  /**
   * Key: {@link MdBusinessDAO} oid. Value: {@link MdRelationship} ids where
   * MdBusiness participates as a child.
   */
  protected Map<String, Set<String>>                         mdBusinessChildMdRelationships;

  /**
   * Key: {@link MdVertexDAO} oid. Value: {@link MdEdgeDAO} ids where MdBusiness
   * participates as a parent.
   */
  protected Map<String, Set<String>>                         mdVertexParentMdEdges;

  /**
   * Key: {@link MdVertexDAO} oid. Value: {@link MdEdgeDAO} ids where MdBusiness
   * participates as a child.
   */
  protected Map<String, Set<String>>                         mdVertexChildMdEdges;

  /**
   * The key is the new Id. The value is the old Id.
   */
  protected Map<String, String>                              changedIds;

  /**
   * Indicates whether the transaction cache has been closed. True if closed
   * false if otherwise.
   */
  private Boolean                                            isClosed;

  /**
   * Records the types, as the class root oid, that have participated in this
   * transaction. If a type has not participated in the transaction than no need
   * to check the file system to see if an object instance of the type has
   * participated in the transaction.
   */
  protected Set<String>                                      typeRootIdsInTransaction;

  /**
   * Used to store the ids of objects that have been newly created in the
   * transaction but only of a non-cached type.
   */
  protected NewEntityIdStringCache                           newEntityIdStringCache;

  /**
   * Initializes all caches.
   * 
   * <br/>
   * <b>Precondition:</b> true <br/>
   * <b>Postcondition:</b> true
   * 
   */
  protected AbstractTransactionCache(ReentrantLock transactionStateLock)
  {
    this.transactionStateLock = transactionStateLock;

    this.deletedEntitySignatures = new HashSet<String>();
    this.deletedEntitySignatureCount = new HashMap<String, Integer>();

    this.updatedEntityDAOIdMap = new HashMap<String, TransactionItemEntityDAOAction>();
    this.updatedEntityDAOKeyMap = new HashMap<String, String>();

    this.entitiesToRefreshFromGlobalCache = new HashSet<String>();

    this.updatedBusinessDAORelationships = new HashMap<String, TransactionBusinessDAORelationships>();

    this.updatedEntityNameCacheStrategyMap = new HashMap<String, TransactionItemStrategyAction>();
    this.deleteEntityNameCacheStrategyMap = new HashMap<String, TransactionItemStrategyAction>();

    this.updatedTransientTransactionItemList = new LinkedList<TransactionItemTransientDAOAction>();
    this.updatedCollectionTransactionItemList = new LinkedList<TransactionItemStrategyAction>();

    this.updatedMdClassDefinedTypeMap = new HashMap<String, String>();
    this.updatedMdClassTableNameMap = new HashMap<String, String>();
    this.updatedMdClassRootIdMap = new HashMap<String, String>();
    this.updatedRoleIFMap = new HashMap<String, String>();

    this.updatedStateMasterMap = new HashMap<String, Set<String>>();

    this.updatedTransitionMap = new HashMap<String, Set<String>>();

    this.addedMdAttrubiteKeyMap = new DualHashBidiMap<String, String>();

    // Used for determining which Types need classes to be generated or
    // regenerated.
    this.newMdAttributeSet_CodeGeneration = new HashSet<String>();
    this.deletedMdAttributeSet_CodeGeneration = new HashSet<String>();
    this.newMdRelationshipSet_CodeGeneration = new HashSet<String>();
    this.deletedMdRelationshipSet_CodeGeneration = new HashSet<String>();
    this.newEnumerationAttributeItemSet_CodeGeneration = new HashSet<String>();
    this.deletedEnumerationAttributeItemSet_CodeGeneration = new HashSet<String>();

    this.updatedMdMethod_CodeGeneration = new HashSet<String>();
    this.updatedMdParameter_CodeGeneration = new HashSet<String>();

    this.updatedMdTypeSet_CodeGeneration = new HashSet<String>();
    this.deletedMdTypeMap_CodeGeneration = new HashMap<String, String>();
    this.updatedEnumerationItemSet_CodeGeneration = new HashSet<String>();

    this.updatedMdEnumerationMap_CodeGeneration = new HashMap<String, String>();

    this.updatedMdEnumerationMap = new HashMap<String, String>();

    this.updatedMdIndexMap = new HashMap<String, String>();

    this.indexNameMap = new HashMap<String, String>();

    this.cachedAddedRelationships = new HashMap<String, RelationshipDAOCollection>();

    this.cachedRemovedRelationships = new HashMap<String, RelationshipDAOCollection>();

    this.notifyActors = new HashSet<ActorDAOIF>();
    this.unregisterPermissionEntity = new HashSet<PermissionEntity>();

    this.dmlTableName = new HashSet<String>();

    this.mdBusinessParentMdRelationships = new HashMap<String, Set<String>>();
    this.mdBusinessChildMdRelationships = new HashMap<String, Set<String>>();

    this.mdVertexParentMdEdges = new HashMap<String, Set<String>>();
    this.mdVertexChildMdEdges = new HashMap<String, Set<String>>();

    this.changedIds = new HashMap<String, String>();

    this.isClosed = false;

    this.typeRootIdsInTransaction = new HashSet<String>();

    this.newEntityIdStringCache = new NewEntityIdStringCache();
  }

  /**
   * Indicates whether the transaction cache has been closed. True if closed
   * false if otherwise.
   */
  protected Boolean isClosed()
  {
    return isClosed;
  }

  // Heads up: test
  // /**
  // * If the file cache used to check whether an {@link EntityDAO} of a
  // non-cached type is not initialized, then
  // * initialize it. Checks if the {@code this.newEntityIdStringFileCach}
  // equals null.
  // *
  // */
  // private void checkAndInitializeEntityIdFileCache()
  // {
  // if (this.newEntityIdStringFileCache == null)
  // {
  // this.entityIdFileCacheName = IDGenerator.nextID();;
  // int diskSize = ServerProperties.getTransactionDiskstoreSize();
  // this.entityIdCacheFileLocation =
  // ServerProperties.getTransactionCacheFileLocation();
  //
  // this.filePersistenceService = new DefaultLocalPersistenceService(new
  // DefaultPersistenceConfiguration(new File(this.entityIdCacheFileLocation,
  // this.entityIdFileCacheName)));
  //
  // this.newEntityIdStringFileCache =
  // UserManagedCacheBuilder.newUserManagedCacheBuilder(String.class,
  // String.class)
  // .with(new UserManagedPersistenceContext<String,
  // String>(this.entityIdFileCacheName, this.filePersistenceService))
  // .withResourcePools(ResourcePoolsBuilder.newResourcePoolsBuilder()
  // .heap(0, EntryUnit.ENTRIES)
  //// .offheap(cacheMemorySize, MemoryUnit.MB)
  // .disk(diskSize, MemoryUnit.MB, true)
  // )
  // .build(true);
  // }
  // }
  //
  /**
   * Records that the {@link EntityDAOIF} has been created during this
   * transaction. <br/>
   * <b>Pre: {@link EntityDAOIF} is of a type that is not cached<b/> <b>Pre:
   * {@link EntityDAOIF.isNew()} equals true<b/>
   * 
   * @param entityDAOIF
   *          {@link EntityDAOIF} that goes into the the global cache.
   */
  public void recordNewlyCreatedNonCachedEntity(EntityDAOIF entityDAOIF)
  {
    this.newEntityIdStringCache.recordNewlyCreatedNonCachedEntity(entityDAOIF);
  }

  /**
   * Records that the {@link EntityDAOIF} has been created during this
   * transaction. <br/>
   * <b>Pre: {@link EntityDAOIF} is of a type that is not cached<b/> <b>Pre:
   * {@link EntityDAOIF.isNew()} equals true<b/>
   * 
   * @param entityId
   *          OID of the {@link EntityDAOIF} that goes into the the global
   *          cache.
   */
  public void recordNewlyCreatedNonCachedEntity(String entityId)
  {
    this.newEntityIdStringCache.recordNewlyCreatedNonCachedEntity(entityId);
  }

  /**
   * Returns true if the given oid is from a newly created {@link EntityDAO}
   * who's type is not cached.
   * 
   * @param entityDAOid
   * @return true if the given oid is from a newly created {@link EntityDAO}
   *         who's type is not cached.
   */
  public boolean isNewUncachedEntity(String entityDAOid)
  {
    return this.newEntityIdStringCache.isNewUncachedEntity(entityDAOid);
  }

  /**
   * Changes the oid of the {@link EntityDAO} in the transaction cache.
   * 
   * @param oldId
   * @param entityDAO
   */
  protected void changeEntityIdInCache(String oldId, EntityDAO entityDAO)
  {
    this.newEntityIdStringCache.changeEntityIdInCache(oldId, entityDAO);

    String mdTypeRootId = IdParser.parseMdTypeRootIdFromId(entityDAO.getOid());
    this.typeRootIdsInTransaction.add(mdTypeRootId);
  }

  /**
   * Close the eChache instance used to simply store ids of newly created
   * {@link EntityDAO} objects who's type are not cached.
   */
  public void close()
  {
    try
    {
      this.newEntityIdStringCache.close();
    }
    catch (IOException e)
    {
      logger.info("Error happened while deleting transaction cache directory. This probably shouldn't matter if ehcache shut down correctly.", e);
    }

    this.isClosed = true;
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#addDMLTableName(java.lang.String)
   */
  public void addDMLTableName(String tableName)
  {
    this.transactionStateLock.lock();
    try
    {
      this.dmlTableName.add(tableName);
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#addRelationship(com.runwaysdk.dataaccess.RelationshipDAO)
   */
  public void addRelationship(RelationshipDAO relationshipDAO)
  {
    this.transactionStateLock.lock();
    try
    {
      // Record this added relationship
      MdRelationshipDAOIF mdRelationshipIF = relationshipDAO.getMdRelationshipDAO();
      BusinessDAOIF cacheEnumItem = mdRelationshipIF.getCacheAlgorithm();

      int cacheCode = Integer.parseInt(cacheEnumItem.getAttributeIF(EntityCacheMaster.CACHE_CODE).getValue());

      if (cacheCode == EntityCacheMaster.CACHE_EVERYTHING.getCacheCode() || cacheCode == EntityCacheMaster.CACHE_HARDCODED.getCacheCode())
      {

        String parentOid = relationshipDAO.getParentOid();
        String childOid = relationshipDAO.getChildOid();

        TransactionBusinessDAORelationships parentRels;
        if (this.updatedBusinessDAORelationships.containsKey(parentOid))
        {
          parentRels = this.updatedBusinessDAORelationships.get(parentOid);
        }
        else
        {
          parentRels = new TransactionBusinessDAORelationships();
        }
        parentRels.addChildRelationship(relationshipDAO);
        this.updatedBusinessDAORelationships.put(parentOid, parentRels);

        TransactionBusinessDAORelationships childRels;
        if (this.updatedBusinessDAORelationships.containsKey(childOid))
        {
          childRels = this.updatedBusinessDAORelationships.get(childOid);
        }
        else
        {
          childRels = new TransactionBusinessDAORelationships();
        }
        childRels.addParentRelationship(relationshipDAO);
        this.updatedBusinessDAORelationships.put(childOid, childRels);

      }
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * Call this method if the oid of the relationshipId has changed during the
   * transaction.
   * 
   * <br/>
   * <b>Precondition:</b> relationshipDAO != null <br/>
   * <b>Precondition:</b> !relationshipDAO.isNew() <br/>
   * <b>Postcondition:</b> relationship is added to the transaction cache
   * 
   * @param relationshipDAO
   *          Relationship to add to the cache
   */
  public void updateRelationshipId(RelationshipDAO relationshipDAO)
  {
    this.transactionStateLock.lock();
    try
    {
      MdRelationshipDAOIF mdRelationshipIF = relationshipDAO.getMdRelationshipDAO();
      BusinessDAOIF cacheEnumItem = mdRelationshipIF.getCacheAlgorithm();

      int cacheCode = Integer.parseInt(cacheEnumItem.getAttributeIF(EntityCacheMaster.CACHE_CODE).getValue());

      if (cacheCode == EntityCacheMaster.CACHE_EVERYTHING.getCacheCode() || cacheCode == EntityCacheMaster.CACHE_HARDCODED.getCacheCode())
      {
        String parentOid = relationshipDAO.getParentOid();
        String childOid = relationshipDAO.getChildOid();

        TransactionBusinessDAORelationships parentRels;
        if (this.updatedBusinessDAORelationships.containsKey(parentOid))
        {
          parentRels = this.updatedBusinessDAORelationships.get(parentOid);
        }
        else
        {
          parentRels = new TransactionBusinessDAORelationships();
        }
        parentRels.updateChildRelationship(relationshipDAO);
        this.updatedBusinessDAORelationships.put(parentOid, parentRels);

        TransactionBusinessDAORelationships childRels;
        if (this.updatedBusinessDAORelationships.containsKey(childOid))
        {
          childRels = this.updatedBusinessDAORelationships.get(childOid);
        }
        else
        {
          childRels = new TransactionBusinessDAORelationships();
        }
        childRels.updateParentRelationship(relationshipDAO);
        this.updatedBusinessDAORelationships.put(childOid, childRels);
      }
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * Updates the cache when the oid of a relationship has changed.
   * 
   * <br/>
   * <b>Precondition:</b>Check to see if the child oid has changed has already
   * occurred.
   * 
   * @param mdRelationshipDAOIF
   * @param oldId
   * @param newId
   */
  private void updateBusinessDAORelationships(String oldId, String newId)
  {
    this.transactionStateLock.lock();
    try
    {
      TransactionBusinessDAORelationships rels;
      if (this.updatedBusinessDAORelationships.containsKey(oldId))
      {
        rels = this.updatedBusinessDAORelationships.get(oldId);
        this.updatedBusinessDAORelationships.remove(oldId);
        this.updatedBusinessDAORelationships.put(newId, rels);
      }
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#deleteEntityDAO(com.runwaysdk.dataaccess.EntityDAO)
   */
  public void deleteEntityDAO(EntityDAO entityDAO)
  {
    this.transactionStateLock.lock();
    try
    {
      // Record that this object's type is participating in this transaction.
      String mdTypeRootId = IdParser.parseMdTypeRootIdFromId(entityDAO.getOid());
      this.typeRootIdsInTransaction.add(mdTypeRootId);

      MdEntityDAOIF mdEntityDAOIF = entityDAO.getMdClassDAO();
      // We are only storing entities in the transaction cache that are cached
      // or metadata in the transaction cache.
      if (!mdEntityDAOIF.isNotCached())
      {
        TransactionItemEntityDAOAction transactionCacheItem = TransactionItemEntityDAOAction.factory(ActionEnumDAO.DELETE, entityDAO, this);

        this.storeTransactionEntityDAO(entityDAO);

        this.addUpdatedEntityToKeyNameMap(entityDAO);

        this.addToUpdatedEntityTransactionItemMap(transactionCacheItem);

        if (entityDAO instanceof MdEntityDAO)
        {
          MdEntityDAO mdEntityDAO = (MdEntityDAO) entityDAO;
          this.updatedMdClassDefinedTypeMap.put(mdEntityDAO.definesType(), mdEntityDAO.getOid());
          this.updatedMdClassTableNameMap.put(mdEntityDAO.getTableName(), mdEntityDAO.getOid());
          this.updatedMdClassRootIdMap.put(mdEntityDAO.getRootId(), mdEntityDAO.getOid());

          if (mdEntityDAO instanceof MdRelationshipDAO)
          {
            MdRelationshipDAO mdRelationshipDAO = (MdRelationshipDAO) mdEntityDAO;

            String parentMdBusinessId = mdRelationshipDAO.getAttribute(MdRelationshipInfo.PARENT_MD_BUSINESS).getValue();
            Set<String> parentRelationshipSet;
            if (this.mdBusinessParentMdRelationships.containsKey(parentMdBusinessId))
            {
              parentRelationshipSet = this.mdBusinessParentMdRelationships.get(parentMdBusinessId);
            }
            else
            {
              // Clone the Set in the global cache. This will be used for the
              // rest
              // of the transaction.
              parentRelationshipSet = new HashSet<String>(ObjectCache.getParentMdRelationshipDAOids(parentMdBusinessId));
              this.mdBusinessParentMdRelationships.put(parentMdBusinessId, parentRelationshipSet);
            }
            parentRelationshipSet.remove(mdRelationshipDAO.getOid());

            String childMdBusinessId = mdRelationshipDAO.getAttribute(MdRelationshipInfo.CHILD_MD_BUSINESS).getValue();
            Set<String> childRelationshipSet;
            if (this.mdBusinessChildMdRelationships.containsKey(childMdBusinessId))
            {
              childRelationshipSet = this.mdBusinessChildMdRelationships.get(childMdBusinessId);
            }
            else
            {
              // Clone the Set in the global cache. This will be used for the
              // rest
              // of the transaction.
              childRelationshipSet = new HashSet<String>(ObjectCache.getChildMdRelationshipDAOids(childMdBusinessId));
              this.mdBusinessChildMdRelationships.put(childMdBusinessId, childRelationshipSet);
            }
            childRelationshipSet.remove(mdRelationshipDAO.getOid());
          }
          else if (mdEntityDAO instanceof MdBusinessDAO)
          {
            MdBusinessDAO mdBusinessDAO = (MdBusinessDAO) mdEntityDAO;

            // Do not remove the entries from the map, otherwise, on an update,
            // the values in the global cache
            // will be placed here again.
            this.mdBusinessParentMdRelationships.put(mdBusinessDAO.getOid(), new HashSet<String>());
            this.mdBusinessChildMdRelationships.put(mdBusinessDAO.getOid(), new HashSet<String>());
          }
        }
        else if (entityDAO instanceof MdEnumerationDAO)
        {
          MdEnumerationDAO mdEnumeration = (MdEnumerationDAO) entityDAO;

          if (!entityDAO.isImport())
          {
            this.updatedMdEnumerationMap_CodeGeneration.put(mdEnumeration.definesEnumeration(), mdEnumeration.getOid());
          }
        }
        else if (entityDAO instanceof MdIndexDAO)
        {
          MdIndexDAO mdIndexDAO = (MdIndexDAO) entityDAO;
          this.updatedMdIndexMap.put(mdIndexDAO.getIndexName(), mdIndexDAO.getOid());
        }
        else if (entityDAO instanceof MdGraphClassDAO)
        {
          MdGraphClassDAO mdGraphClassDAO = (MdGraphClassDAO) entityDAO;
          this.updatedMdClassDefinedTypeMap.put(mdGraphClassDAO.definesType(), mdGraphClassDAO.getOid());
          this.updatedMdClassTableNameMap.put(mdGraphClassDAO.getDBClassName(), mdGraphClassDAO.getOid());
          this.updatedMdClassRootIdMap.put(mdGraphClassDAO.getRootId(), mdGraphClassDAO.getOid());

          if (mdGraphClassDAO instanceof MdEdgeDAO)
          {
            MdEdgeDAO mdEdgeDAO = (MdEdgeDAO) mdGraphClassDAO;

            String parentMdVertexId = mdEdgeDAO.getAttribute(MdEdgeInfo.PARENT_MD_VERTEX).getValue();
            Set<String> parentRelationshipSet;
            if (this.mdVertexParentMdEdges.containsKey(parentMdVertexId))
            {
              parentRelationshipSet = this.mdVertexParentMdEdges.get(parentMdVertexId);
            }
            else
            {
              // Clone the Set in the global cache. This will be used for the
              // rest
              // of the transaction.
              parentRelationshipSet = new HashSet<String>(ObjectCache.getParentMdEdgeDAOids(parentMdVertexId));
              this.mdVertexParentMdEdges.put(parentMdVertexId, parentRelationshipSet);
            }
            parentRelationshipSet.remove(mdEdgeDAO.getOid());

            String childMdVertexId = mdEdgeDAO.getAttribute(MdEdgeInfo.CHILD_MD_VERTEX).getValue();
            Set<String> childRelationshipSet;
            if (this.mdVertexChildMdEdges.containsKey(childMdVertexId))
            {
              childRelationshipSet = this.mdVertexChildMdEdges.get(childMdVertexId);
            }
            else
            {
              // Clone the Set in the global cache. This will be used for the
              // rest
              // of the transaction.
              childRelationshipSet = new HashSet<String>(ObjectCache.getChildMdEdgeDAOids(childMdVertexId));
              this.mdVertexChildMdEdges.put(childMdVertexId, childRelationshipSet);
            }
            childRelationshipSet.remove(mdEdgeDAO.getOid());
          }
        }
      }
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#deleteEntityDAOCollection(java.lang.String,
   *      com.runwaysdk.dataaccess.cache.CacheStrategy)
   */
  public void deleteEntityDAOCollection(String type, CacheStrategy entityDAOCollection)
  {
    this.transactionStateLock.lock();
    try
    {
      TransactionItemStrategyAction transactionCacheItem = new TransactionItemStrategyAction(ActionEnumDAO.DELETE, entityDAOCollection);
      this.deleteEntityNameCacheStrategyMap.put(type, transactionCacheItem);
      this.updatedCollectionTransactionItemList.add(transactionCacheItem);
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#deleteRelationship(com.runwaysdk.dataaccess.RelationshipDAO)
   */
  public void deleteRelationship(RelationshipDAO relationshipDAO)
  {
    this.transactionStateLock.lock();
    try
    {
      // Record this deleted relationship
      MdRelationshipDAOIF mdRelationshipIF = relationshipDAO.getMdRelationshipDAO();
      BusinessDAOIF cacheEnumItem = mdRelationshipIF.getCacheAlgorithm();

      int cacheCode = Integer.parseInt(cacheEnumItem.getAttributeIF(EntityCacheMaster.CACHE_CODE).getValue());

      if (cacheCode == EntityCacheMaster.CACHE_EVERYTHING.getCacheCode() || cacheCode == EntityCacheMaster.CACHE_HARDCODED.getCacheCode())
      {
        String parentOid = relationshipDAO.getParentOid();
        String childOid = relationshipDAO.getChildOid();

        TransactionBusinessDAORelationships parentRels;
        if (this.updatedBusinessDAORelationships.containsKey(parentOid))
        {
          parentRels = this.updatedBusinessDAORelationships.get(parentOid);
        }
        else
        {
          parentRels = new TransactionBusinessDAORelationships();
        }
        parentRels.deletedChildRelationship(relationshipDAO);
        this.updatedBusinessDAORelationships.put(parentOid, parentRels);

        TransactionBusinessDAORelationships childRels;
        if (this.updatedBusinessDAORelationships.containsKey(childOid))
        {
          childRels = this.updatedBusinessDAORelationships.get(childOid);
        }
        else
        {
          childRels = new TransactionBusinessDAORelationships();
        }
        childRels.deletedParentRelationship(relationshipDAO);
        this.updatedBusinessDAORelationships.put(childOid, childRels);

      }
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#deletedEnumerationAttributeItem_CodeGen(com.runwaysdk.dataaccess.EnumerationAttributeItem)
   */
  public void deletedEnumerationAttributeItem_CodeGen(EnumerationAttributeItem enumerationAttributeItem)
  {
    this.transactionStateLock.lock();
    try
    {
      this.deletedEnumerationAttributeItemSet_CodeGeneration.add(enumerationAttributeItem.getOid());
      this.storeTransactionEntityDAO(enumerationAttributeItem);
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#deletedMdAttribute_CodeGen(com.runwaysdk.dataaccess.metadata.MdAttributeDAO)
   */
  public void deletedMdAttribute_CodeGen(MdAttributeDAO mdAttribute)
  {
    this.transactionStateLock.lock();
    try
    {
      this.deletedMdAttributeSet_CodeGeneration.add(mdAttribute.getOid());
      this.storeTransactionEntityDAO(mdAttribute);
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#deletedMdRelationship_CodeGen(com.runwaysdk.dataaccess.metadata.MdRelationshipDAO)
   */
  public void deletedMdRelationship_CodeGen(MdRelationshipDAO mdRelationshipDAO)
  {
    this.transactionStateLock.lock();
    try
    {
      this.deletedMdRelationshipSet_CodeGeneration.add(mdRelationshipDAO.getOid());
      this.storeTransactionEntityDAO(mdRelationshipDAO);
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#deletedMdType(com.runwaysdk.dataaccess.MdTypeDAOIF)
   */
  public void deletedMdType(MdTypeDAOIF mdTypeIF)
  {
    this.transactionStateLock.lock();
    try
    {
      this.deletedMdTypeMap_CodeGeneration.put(mdTypeIF.definesType(), mdTypeIF.getOid());
      this.storeTransactionEntityDAO((MdTypeDAO) mdTypeIF);
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#hasAddedChildren(java.lang.String,
   *      java.lang.String)
   */
  public boolean hasAddedChildren(String businessDAOid, String relationshipType)
  {
    this.transactionStateLock.lock();

    boolean hasAddedChildren = false;

    try
    {
      TransactionBusinessDAORelationships rels = this.updatedBusinessDAORelationships.get(businessDAOid);

      if (rels != null)
      {
        hasAddedChildren = rels.hasAddedChildRelationshipDAOset(relationshipType);
      }

      return hasAddedChildren;
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#hasRemovedChildren(java.lang.String,
   *      java.lang.String)
   */
  public boolean hasRemovedChildren(String businessDAOid, String relationshipType)
  {
    this.transactionStateLock.lock();

    boolean removedChildren = false;

    try
    {

      TransactionBusinessDAORelationships rels = this.updatedBusinessDAORelationships.get(businessDAOid);

      if (rels != null)
      {
        removedChildren = rels.hasDeletedChildRelationshipDAOset(relationshipType);
      }

      return removedChildren;
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#hasAddedParents(java.lang.String,
   *      java.lang.String)
   */
  public boolean hasAddedParents(String businessDAOid, String relationshipType)
  {
    this.transactionStateLock.lock();

    boolean hasAddedParent = false;

    try
    {

      TransactionBusinessDAORelationships rels = this.updatedBusinessDAORelationships.get(businessDAOid);

      if (rels != null)
      {
        hasAddedParent = rels.hasAddedParentRelationshipDAOset(relationshipType);
      }

      return hasAddedParent;
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#hasRemovedParents(java.lang.String,
   *      java.lang.String)
   */
  public boolean hasRemovedParents(String businessDAOid, String relationshipType)
  {
    this.transactionStateLock.lock();

    boolean hasRemovedParents = false;

    try
    {
      TransactionBusinessDAORelationships rels = this.updatedBusinessDAORelationships.get(businessDAOid);

      if (rels != null)
      {
        hasRemovedParents = rels.hasDeletedParentRelationshipDAOset(relationshipType);
      }

      return hasRemovedParents;
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * Returns the added parent relationships for this {@link BusinessDAOIF}.
   * 
   * @param businessDAOid
   * @param relationshipType
   * 
   * @return parent relationships for this {@link BusinessDAOIF}.
   */
  public Set<String> getAddedParentRelationshipDAOset(String businessDAOid, String relationshipType)
  {
    Set<String> returnSet;

    TransactionBusinessDAORelationships rels = this.updatedBusinessDAORelationships.get(businessDAOid);

    if (rels != null)
    {
      returnSet = rels.getAddedParentRelationshipDAOset(relationshipType);
    }
    else
    {
      returnSet = new HashSet<String>();
    }

    return returnSet;
  }

  /**
   * Returns the added child relationships for this {@link BusinessDAOIF}.
   * 
   * @param businessDAOid
   * @param relationshipType
   * 
   * @return added child relationships for this {@link BusinessDAOIF}.
   */
  public Set<String> getAddedChildRelationshipDAOset(String businessDAOid, String relationshipType)
  {
    Set<String> returnSet;

    TransactionBusinessDAORelationships rels = this.updatedBusinessDAORelationships.get(businessDAOid);

    if (rels != null)
    {
      returnSet = rels.getAddedChildRelationshipDAOset(relationshipType);
    }
    else
    {
      returnSet = new HashSet<String>();
    }

    return returnSet;
  }

  /**
   * Returns the deleted parent relationships for this {@link BusinessDAOIF}.
   * 
   * @param businessDAOid
   * @param relationshipType
   * 
   * @return deleted parent relationships for this {@link BusinessDAOIF}.
   */
  public Set<String> getDeletedParentRelationshipDAOset(String businessDAOid, String relationshipType)
  {
    Set<String> returnSet;

    TransactionBusinessDAORelationships rels = this.updatedBusinessDAORelationships.get(businessDAOid);

    if (rels != null)
    {
      returnSet = rels.getDeletedParentRelationshipDAOset(relationshipType);
    }
    else
    {
      returnSet = new HashSet<String>();
    }

    return returnSet;
  }

  /**
   * Returns the deleted child relationships for this {@link BusinessDAOIF}.
   * 
   * @param businessDAOid
   * @param relationshipType
   * 
   * @return deleted child relationships for this {@link BusinessDAOIF}.
   */
  public Set<String> getDeletedChildRelationshipDAOset(String businessDAOid, String relationshipType)
  {
    Set<String> returnSet;

    TransactionBusinessDAORelationships rels = this.updatedBusinessDAORelationships.get(businessDAOid);

    if (rels != null)
    {
      returnSet = rels.getDeletedChildRelationshipDAOset(relationshipType);
    }
    else
    {
      returnSet = new HashSet<String>();
    }

    return returnSet;
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#getAddedMdAttribute(java.lang.String)
   */
  public MdAttributeDAOIF getAddedMdAttribute(String mdAttributeKey)
  {
    this.transactionStateLock.lock();
    try
    {
      String oid = (String) this.addedMdAttrubiteKeyMap.get(mdAttributeKey);

      return (MdAttributeDAOIF) this.internalGetEntityDAO(oid);
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#getAddedMdAttributes()
   */
  public Map<String, MdAttributeDAO> getAddedMdAttributes()
  {
    this.transactionStateLock.lock();
    try
    {
      Map<String, MdAttributeDAO> mdAttributeDAOmap = new HashMap<String, MdAttributeDAO>();

      for (Object mdAttributeDAOkey : this.addedMdAttrubiteKeyMap.keySet())
      {
        String mdAttributeDAOid = (String) this.addedMdAttrubiteKeyMap.get(mdAttributeDAOkey);

        mdAttributeDAOmap.put((String) mdAttributeDAOkey, (MdAttributeDAO) this.internalGetEntityDAO(mdAttributeDAOid));
      }

      return mdAttributeDAOmap;
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#getChildMdRelationshipDAOids(java.lang.String)
   */
  public Set<String> getChildMdRelationshipDAOids(String mdBusinessDAOid)
  {
    this.transactionStateLock.lock();
    try
    {
      if (this.mdBusinessChildMdRelationships.containsKey(mdBusinessDAOid))
      {
        return this.mdBusinessChildMdRelationships.get(mdBusinessDAOid);
      }
      else
      {
        String originalId = this.getOriginalId(mdBusinessDAOid);

        if (originalId == null)
        {
          originalId = mdBusinessDAOid;
        }

        return ObjectCache.getChildMdRelationshipDAOids(originalId);
      }
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#getChildMdEdgeDAOids(java.lang.String)
   */
  public Set<String> getChildMdEdgeDAOids(String mdVertexDAOid)
  {
    this.transactionStateLock.lock();
    try
    {
      if (this.mdVertexChildMdEdges.containsKey(mdVertexDAOid))
      {
        return this.mdVertexChildMdEdges.get(mdVertexDAOid);
      }
      else
      {
        String originalId = this.getOriginalId(mdVertexDAOid);

        if (originalId == null)
        {
          originalId = mdVertexDAOid;
        }

        return ObjectCache.getChildMdEdgeDAOids(originalId);
      }
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#getEntityDAO(java.lang.String)
   */
  public abstract EntityDAOIF getEntityDAO(String oid);

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#getEntityDAOIFfromCache(java.lang.String)
   */
  public abstract EntityDAOIF getEntityDAOIFfromCache(String oid);

  /**
   * Returns a map of all entities that were updated during this transaction.
   * 
   * @return map of all entities that were updated during this transaction.
   */
  public Map<String, TransactionItemEntityDAOAction> getEntityDAOIDsMap()
  {
    this.transactionStateLock.lock();
    try
    {
      return this.updatedEntityDAOIdMap;
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * Returns the OID that should be passed on to the ObjectCache for fetching
   * parents objects with the given oid. The oid could have been changed during
   * the transaction, and the oid in the global cache may be different than the
   * oid for the object in the current transaction.
   * 
   * @param businessDAOid
   * @param relationshipType
   * @return OID that should be passed on to the ObjectCache for fetching
   *         parents objects with the given oid.
   */
  public String getBusIdForGetParentsMethod(String businessDAOid, String relationshipType)
  {
    // Record this added relationship
    MdRelationshipDAOIF mdRelationshipDAOIF = MdRelationshipDAO.getMdRelationshipDAO(relationshipType);
    // BusinessDAOIF cacheEnumItem = mdRelationshipDAOIF.getCacheAlgorithm();

    AttributeEnumerationIF attributeEnumerationIF = (AttributeEnumerationIF) mdRelationshipDAOIF.getAttributeIF(MdRelationshipInfo.CACHE_ALGORITHM);

    Set<String> cachedObjectIds = attributeEnumerationIF.getCachedEnumItemIdSet();
    // There is always a cache algorithm.
    String cacheAlgorithmId = cachedObjectIds.iterator().next();

    // int cacheCode =
    // Integer.parseInt(cacheEnumItem.getAttributeIF(EntityCacheMaster.CACHE_CODE).getValue());

    if (cacheAlgorithmId.equals(EntityCacheMaster.CACHE_EVERYTHING.getOid()) || cacheAlgorithmId.equals(EntityCacheMaster.CACHE_HARDCODED.getOid()))
    {
      String originalId = this.getOriginalId(businessDAOid);

      if (originalId == null)
      {
        originalId = businessDAOid;
      }
      return originalId;
    }
    else
    {
      return businessDAOid;
    }
  }

  /**
   * Returns parent relationships for the object with the given oid in the
   * transaction.
   * 
   * @param relationshipList
   *          relationships returned from the global source.
   * @param businessDAOid
   * @param relationshipType
   * 
   * @return relationships modified based on what occurred in this transaction.
   */
  public List<RelationshipDAOIF> getParents(List<RelationshipDAOIF> relationshipList, String businessDAOid, String relationshipType)
  {
    boolean hasNewlyAddedRelationships = this.hasAddedParents(businessDAOid, relationshipType);
    boolean hasNewlyRemovedParents = this.hasRemovedParents(businessDAOid, relationshipType);

    if (hasNewlyAddedRelationships || hasNewlyRemovedParents)
    {
      List<RelationshipDAOIF> newRelationshipList = new LinkedList<RelationshipDAOIF>();

      Set<String> addedParentRelSet = this.getAddedParentRelationshipDAOset(businessDAOid, relationshipType);
      Set<String> deletedParentRelSet = this.getDeletedParentRelationshipDAOset(businessDAOid, relationshipType);

      // Set of the original ids (ids before they were changed in this
      // transaction. If not changed, than just
      // the relationship oid) of relationshipIds that have been added to the
      // relationship list that
      // will be returned by this function.
      Set<String> originalIdHasBeenAddedToTheReturnList = new HashSet<String>();

      // Now add the relationships that were added or modified in the
      // transaction
      Iterator<String> addedRelIterator = addedParentRelSet.iterator();
      while (addedRelIterator.hasNext())
      {
        String relId = addedRelIterator.next();
        if (!deletedParentRelSet.contains(relId))
        {
          RelationshipDAOIF relationshipDAOIF = (RelationshipDAOIF) this.getEntityDAO(relId);
          newRelationshipList.add(relationshipDAOIF);

          // In case the relationshipId has been modified during the transaction
          String originalRelId = this.getOriginalId(relId);
          originalIdHasBeenAddedToTheReturnList.add(originalRelId);
        }
      }

      for (RelationshipDAOIF relationshipDAOIF : relationshipList)
      {
        // Do not add to the list if the relationship has been added or deleted
        // in this transaction.
        if (!addedParentRelSet.contains(relationshipDAOIF.getOid()) && !deletedParentRelSet.contains(relationshipDAOIF.getOid()) &&
        // do not add the relationship from the global cache/database if it
        // has already been added to the return list under a different or the
        // same oid.
            !originalIdHasBeenAddedToTheReturnList.contains(relationshipDAOIF.getOid()))
        {
          newRelationshipList.add(relationshipDAOIF);
        }
      }

      relationshipList = newRelationshipList;
    }

    return relationshipList;
  }

  /**
   * Returns child relationships for the object with the given oid in the
   * transaction.
   * 
   * @param relationshipList
   *          relationships returned from the global source.
   * @param businessDAOid
   * @param relationshipType
   * 
   * @return relationships modified based on what occurred in this transaction.
   */
  public List<RelationshipDAOIF> getChildren(List<RelationshipDAOIF> relationshipList, String businessDAOid, String relationshipType)
  {
    boolean hasNewlyAddedRelationships = this.hasAddedChildren(businessDAOid, relationshipType);
    boolean hasNewlyRemovedRelationships = this.hasRemovedChildren(businessDAOid, relationshipType);

    if (hasNewlyAddedRelationships || hasNewlyRemovedRelationships)
    {
      List<RelationshipDAOIF> newRelationshipList = new LinkedList<RelationshipDAOIF>();

      // Relationships added during this transaction
      Set<String> addedChildRelSet = this.getAddedChildRelationshipDAOset(businessDAOid, relationshipType);
      // Relationships removed during this transaction
      Set<String> deletedChildRelSet = this.getDeletedChildRelationshipDAOset(businessDAOid, relationshipType);

      // Set of the original ids (ids before they were changed in this
      // transaction. If not changed, than just
      // the relationship oid) of relationshipIds that have been added to the
      // relationship list that
      // will be returned by this function.
      Set<String> originalIdHasBeenAddedToTheReturnList = new HashSet<String>();

      // Now add the relationships that were added or modified in the
      // transaction
      Iterator<String> addedRelIterator = addedChildRelSet.iterator();
      while (addedRelIterator.hasNext())
      {
        String relId = addedRelIterator.next();

        if (!deletedChildRelSet.contains(relId))
        {
          RelationshipDAOIF relationshipDAOIF = (RelationshipDAOIF) this.getEntityDAO(relId);
          newRelationshipList.add(relationshipDAOIF);

          // In case the relationshipId has been modified during the transaction
          String originalRelId = this.getOriginalId(relId);
          originalIdHasBeenAddedToTheReturnList.add(originalRelId);
        }
      }

      for (RelationshipDAOIF relationshipDAOIF : relationshipList)
      {
        // Do not add to the list if the relationship has been added or deleted
        // in this transaction.
        if (!addedChildRelSet.contains(relationshipDAOIF.getOid()) && !deletedChildRelSet.contains(relationshipDAOIF.getOid()) &&
        // do not add the relationship from the global cache/database if it
        // has already been added to the return list under a different or the
        // same oid.
            !originalIdHasBeenAddedToTheReturnList.contains(relationshipDAOIF.getOid()))
        {
          newRelationshipList.add(relationshipDAOIF);
        }
      }

      relationshipList = newRelationshipList;
    }

    return relationshipList;
  }

  /**
   * Used internally by this hierarchy. It differers from the public method in
   * that it will check the global cache for objects not in the transaction
   * cache.
   * 
   * @param oid
   * @return Null if the oid is null.
   * @throws {@link
   *           DataNotFoundException} if oid != null and the object does not
   *           exist in the transaction cache or the ObjectCache.
   */
  protected EntityDAOIF internalGetEntityDAO(String oid)
  {
    if (oid == null)
    {
      return null;
    }

    EntityDAOIF entityDAOIF = this.getEntityDAO(oid);

    if (entityDAOIF == null)
    {
      entityDAOIF = ObjectCache._internalGetEntityDAO(oid);
    }

    return entityDAOIF;
  }

  /**
   * Stores an {@link EntityDAO} that was modified in this transaction in a
   * transaction cache.
   * 
   * @param entityDAO
   */
  public abstract void storeTransactionEntityDAO(EntityDAO entityDAO);

  protected RelationshipDAOCollection getCachedRemovedRelationships(String relationshipType)
  {
    if (relationshipType.equals(RelationshipTypes.CLASS_ATTRIBUTE_CONCRETE.getType()) || relationshipType.equals(RelationshipTypes.CLASS_ATTRIBUTE_VIRTUAL.getType()))
    {
      return this.cachedRemovedRelationships.get(RelationshipTypes.CLASS_ATTRIBUTE.getType());
    }
    else
    {
      return this.cachedRemovedRelationships.get(relationshipType);
    }
  }

  protected RelationshipDAOCollection putCachedRemovedRelationships(String relationshipType, RelationshipDAOCollection relationshipDAOCollection)
  {
    if (relationshipType.equals(RelationshipTypes.CLASS_ATTRIBUTE_CONCRETE.getType()) || relationshipType.equals(RelationshipTypes.CLASS_ATTRIBUTE_VIRTUAL.getType()))
    {
      return this.cachedRemovedRelationships.put(RelationshipTypes.CLASS_ATTRIBUTE.getType(), relationshipDAOCollection);
    }
    else
    {
      return this.cachedRemovedRelationships.put(relationshipType, relationshipDAOCollection);
    }
  }

  protected TransactionItemEntityDAOAction createTransactionItemForEntity(EntityDAO entityDAO)
  {
    ActionEnumDAO actionEnumDAO = null;

    if (!entityDAO.isAppliedToDB())
    {
      actionEnumDAO = ActionEnumDAO.CREATE;
    }
    else
    {
      actionEnumDAO = ActionEnumDAO.UPDATE;
    }

    return TransactionItemEntityDAOAction.factory(actionEnumDAO, entityDAO, this);
  }

  /**
   * Adds the given entity to the map that stores references based on
   * type-keyname.
   * 
   * @param entityDAO
   */
  public void addUpdatedEntityToKeyNameMap(EntityDAO entityDAO)
  {
    MdEntityDAOIF mdEntityDAOIF = entityDAO.getMdClassDAO();
    if (!mdEntityDAOIF.isNotCached())
    {
      this.storeTransactionEntityDAO(entityDAO);

      for (String type : mdEntityDAOIF.getSuperTypes())
      {
        this.updatedEntityDAOKeyMap.put(type + "-" + entityDAO.getKey(), entityDAO.getOid());
      }
    }
  }

  /**
   * Returns an EntityDAO of the given type with the given key.
   * 
   * @param providedType
   * @param key
   * @return EntityDAO of the given type with the given key.
   */
  protected EntityDAOIF getUpdatedEntityFromKeyNameMap(String providedType, String key)
  {
    EntityDAOIF entityDAOIF = null;

    // Check to see if the type and the key have been modified in this
    // transaction
    String oid = this.updatedEntityDAOKeyMap.get(providedType + "-" + key);

    // The type provided could be a parent type of the object with the key.
    // Check all subclasses.
    if (oid == null)
    {
      MdEntityDAOIF rootMdEntityDAOIF = MdEntityDAO.getMdEntityDAO(providedType);
      List<? extends MdEntityDAOIF> subClasses = rootMdEntityDAOIF.getAllSubClasses();

      for (MdEntityDAOIF mdEntityDAOIF : subClasses)
      {
        String subType = mdEntityDAOIF.definesType();
        oid = this.updatedEntityDAOKeyMap.get(subType + "-" + key);

        if (oid != null)
        {
          // We have identified the type of the object that has been modified in
          // this transaction.
          // Type is also a cached type.
          break;
        }
      }
    }

    // Object has participated in the transaction and is of a cached type.
    if (oid != null)
    {
      entityDAOIF = this.internalGetEntityDAO(oid);
    }
    // Either the object has not participated in the transaction or is not of
    // a cached type.
    else
    {
      entityDAOIF = ObjectCache.getEntityDAO(providedType, key);
      if (this.isNewUncachedEntity(entityDAOIF.getOid()))
      {
        ( (EntityDAO) entityDAOIF ).setIsNew(true);
      }
    }

    return entityDAOIF;
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#getEntityDAO(java.lang.String,
   *      java.lang.String)
   */
  public EntityDAOIF getEntityDAO(String type, String key)
  {
    this.transactionStateLock.lock();
    try
    {
      return this.getUpdatedEntityFromKeyNameMap(type, key);
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#getMdAttributeWithIndex(java.lang.String)
   */
  public MdAttributeConcreteDAOIF getMdAttributeWithIndex(String indexName)
  {
    this.transactionStateLock.lock();
    try
    {
      return (MdAttributeConcreteDAOIF) this.internalGetEntityDAO(this.indexNameMap.get(indexName));
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#getMdClass(java.lang.String)
   */
  public MdClassDAOIF getMdClass(String type)
  {
    this.transactionStateLock.lock();
    try
    {
      return (MdClassDAOIF) this.internalGetEntityDAO(this.updatedMdClassDefinedTypeMap.get(type));
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  @Override
  public MdClassDAOIF getMdClassByTableName(String tableName)
  {
    this.transactionStateLock.lock();
    try
    {
      return (MdClassDAOIF) this.internalGetEntityDAO(this.updatedMdClassTableNameMap.get(tableName));
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#getMdClassDAOByRootId(java.lang.String)
   */
  public MdClassDAOIF getMdClassDAOByRootId(String mdClassRootId)
  {
    this.transactionStateLock.lock();
    try
    {
      return (MdClassDAOIF) this.internalGetEntityDAO(this.updatedMdClassRootIdMap.get(mdClassRootId));
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#getMdEnumerationDAO(java.lang.String)
   */
  public MdEnumerationDAOIF getMdEnumerationDAO(String enumerationType)
  {
    this.transactionStateLock.lock();
    try
    {
      return (MdEnumerationDAOIF) this.internalGetEntityDAO(this.updatedMdEnumerationMap.get(enumerationType));
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#getMdIndexDAO(java.lang.String)
   */
  public MdIndexDAOIF getMdIndexDAO(String indexName)
  {
    this.transactionStateLock.lock();
    try
    {
      return (MdIndexDAOIF) this.internalGetEntityDAO(this.updatedMdIndexMap.get(indexName));
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#getParentMdRelationshipDAOids(java.lang.String)
   */
  @Override
  public Set<String> getParentMdRelationshipDAOids(String mdBusinessDAOid)
  {
    this.transactionStateLock.lock();
    try
    {
      if (this.mdBusinessParentMdRelationships.containsKey(mdBusinessDAOid))
      {
        return this.mdBusinessParentMdRelationships.get(mdBusinessDAOid);
      }
      else
      {
        String originalId = this.getOriginalId(mdBusinessDAOid);

        if (originalId == null)
        {
          originalId = mdBusinessDAOid;
        }

        return ObjectCache.getParentMdRelationshipDAOids(originalId);
      }
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#getParentMdEdgeDAOids(java.lang.String)
   */
  @Override
  public Set<String> getParentMdEdgeDAOids(String mdVertexDAOid)
  {
    this.transactionStateLock.lock();
    try
    {
      if (this.mdVertexParentMdEdges.containsKey(mdVertexDAOid))
      {
        return this.mdVertexParentMdEdges.get(mdVertexDAOid);
      }
      else
      {
        String originalId = this.getOriginalId(mdVertexDAOid);

        if (originalId == null)
        {
          originalId = mdVertexDAOid;
        }

        return ObjectCache.getParentMdEdgeDAOids(originalId);
      }
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#getRoleIF(java.lang.String)
   */
  public RoleDAOIF getRoleIF(String roleName)
  {
    this.transactionStateLock.lock();
    try
    {
      return (RoleDAOIF) this.internalGetEntityDAO(this.updatedRoleIFMap.get(roleName));
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#hasExecutedEntityDeleteMethod(com.runwaysdk.dataaccess.EntityDAO,
   *      java.lang.String)
   */
  public boolean hasExecutedEntityDeleteMethod(EntityDAO entityDAO, String signature)
  {
    this.transactionStateLock.lock();
    try
    {
      String idSigKey = this.buildMethKeySignature(entityDAO, signature);

      if (!this.deletedEntitySignatures.contains(idSigKey))
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
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#modifiedMdAttribute_CodeGen(com.runwaysdk.dataaccess.metadata.MdAttributeDAO)
   */
  public void modifiedMdAttribute_CodeGen(MdAttributeDAO mdAttribute)
  {
    this.transactionStateLock.lock();
    try
    {
      if (mdAttribute.isNew())
      {
        this.newMdAttributeSet_CodeGeneration.add(mdAttribute.getOid());
        this.storeTransactionEntityDAO(mdAttribute);
      }
      else
      {
        // Regenerate if the visibility has been modified
        if ( ( mdAttribute.hasAttribute(MdAttributeConcreteInfo.SETTER_VISIBILITY) && mdAttribute.getAttribute(MdAttributeConcreteInfo.SETTER_VISIBILITY).isModified() ) || ( mdAttribute.hasAttribute(MdAttributeConcreteInfo.GETTER_VISIBILITY) && mdAttribute.getAttribute(MdAttributeConcreteInfo.GETTER_VISIBILITY).isModified() ) || mdAttribute.getAttribute(MdAttributeConcreteInfo.NAME).isModified())
        {
          this.deletedMdAttributeSet_CodeGeneration.add(mdAttribute.getOid());
          this.storeTransactionEntityDAO(mdAttribute);

          // Make sure all Virtual attributes are likewise updated.
          QueryFactory qf = new QueryFactory();
          BusinessDAOQuery q = qf.businessDAOQuery(MdAttributeVirtualInfo.CLASS);
          q.WHERE(q.aReference(MdAttributeVirtualInfo.MD_ATTRIBUTE_CONCRETE).EQ(mdAttribute.getOid()));

          OIterator<BusinessDAOIF> iterator = q.getIterator();

          for (BusinessDAOIF businessDAOIF : iterator)
          {
            MdAttributeVirtualDAOIF mdAttributeVirtualDAOIF = (MdAttributeVirtualDAOIF) businessDAOIF;

            this.deletedMdAttributeSet_CodeGeneration.add(mdAttributeVirtualDAOIF.getOid());
            this.storeTransactionEntityDAO((BusinessDAO) mdAttributeVirtualDAOIF);
          }

        }
      }
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#newEnumerationAttributeItem_CodeGen(com.runwaysdk.dataaccess.EnumerationAttributeItem)
   */
  public void newEnumerationAttributeItem_CodeGen(EnumerationAttributeItem enumerationAttributeItem)
  {
    this.transactionStateLock.lock();
    try
    {
      this.newEnumerationAttributeItemSet_CodeGeneration.add(enumerationAttributeItem.getOid());
      this.storeTransactionEntityDAO(enumerationAttributeItem);
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#newMdAttribute(com.runwaysdk.dataaccess.metadata.MdAttributeDAO)
   */
  public void newMdAttribute(MdAttributeDAO mdAttributeDAO)
  {
    this.transactionStateLock.lock();
    try
    {
      if (mdAttributeDAO.isNew())
      {
        this.addedMdAttrubiteKeyMap.put(mdAttributeDAO.getKey(), mdAttributeDAO.getOid());
        this.storeTransactionEntityDAO(mdAttributeDAO);
      }
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#newMdRelationship_CodeGen(com.runwaysdk.dataaccess.metadata.MdRelationshipDAO)
   */
  public void newMdRelationship_CodeGen(MdRelationshipDAO mdRelationship)
  {
    this.transactionStateLock.lock();
    try
    {
      this.newMdRelationshipSet_CodeGeneration.add(mdRelationship.getOid());
      this.storeTransactionEntityDAO(mdRelationship);
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#notifyActors(com.runwaysdk.business.rbac.ActorDAOIF)
   */
  public void notifyActors(ActorDAOIF actorIF)
  {
    this.transactionStateLock.lock();
    try
    {
      this.notifyActors.add(actorIF);
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#performDDLTable(java.lang.String)
   */
  public void performDDLTable(String tableName)
  {
    this.transactionStateLock.lock();
    try
    {
      if (this.dmlTableName.contains(tableName))
      {
        String error = "A SQL DDL operation was performed in a transaction on table [" + tableName + "] " + "after a SQL DML operation had been perfromed on the same table.  DDL statements " + "cannot be performed if DML statements have aleady been " + "performed on the same table within the same transaction.";
        throw new DatabaseException(error);
      }
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#setExecutedEntityDeleteMethod(com.runwaysdk.dataaccess.EntityDAO,
   *      java.lang.String)
   */
  public void setExecutedEntityDeleteMethod(EntityDAO entityDAO, String signature)
  {
    this.transactionStateLock.lock();
    try
    {
      String idSigKey = this.buildMethKeySignature(entityDAO, signature);
      this.deletedEntitySignatures.add(idSigKey);

      Integer deleteCount = this.deletedEntitySignatureCount.get(entityDAO.getOid());
      if (deleteCount == null)
      {
        deleteCount = new Integer(0);
      }

      deleteCount++;
      this.deletedEntitySignatureCount.put(entityDAO.getOid(), deleteCount);

    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#removeExecutedDeleteMethod(com.runwaysdk.dataaccess.EntityDAO,
   *      java.lang.String)
   */
  public boolean removeExecutedDeleteMethod(EntityDAO entityDAO, String signature)
  {
    this.transactionStateLock.lock();
    try
    {
      String idSigKey = this.buildMethKeySignature(entityDAO, signature);
      this.deletedEntitySignatures.remove(idSigKey);

      boolean hasCompletedDelete;

      Integer deleteCount = this.deletedEntitySignatureCount.get(entityDAO.getOid());
      if (deleteCount != null)
      {
        deleteCount--;

        if (deleteCount.intValue() <= 0)
        {
          // Do not remove the oid from the map, as it indicates that this
          // object
          // oid
          // has been deleted during the transaction.
          this.deletedEntitySignatureCount.put(entityDAO.getOid(), 0);
          hasCompletedDelete = true;
        }
        else
        {
          this.deletedEntitySignatureCount.put(entityDAO.getOid(), deleteCount);
          hasCompletedDelete = false;
        }
      }
      else
      {
        hasCompletedDelete = true;
      }

      return hasCompletedDelete;
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#hasBeenDeletedInTransaction(com.runwaysdk.dataaccess.EntityDAO)
   */
  public boolean hasBeenDeletedInTransaction(EntityDAO entityDAO)
  {
    boolean isBeingDeleted = false;

    Integer deleteCount = this.deletedEntitySignatureCount.get(entityDAO.getOid());
    if (deleteCount != null)
    {
      isBeingDeleted = true;
    }

    return isBeingDeleted;
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#removeHasBeenDeletedInTransaction(com.runwaysdk.dataaccess.EntityDAO)
   */
  public void removeHasBeenDeletedInTransaction(EntityDAO entityDAO)
  {
    this.deletedEntitySignatureCount.remove(entityDAO.getOid());
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#setMdAttributeWithIndex(com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO)
   */
  public void setMdAttributeWithIndex(MdAttributeConcreteDAO mdAttributeConcreteDAO)
  {
    this.transactionStateLock.lock();
    try
    {
      if (!mdAttributeConcreteDAO.getIndexName().trim().equals(""))
      {
        this.indexNameMap.put(mdAttributeConcreteDAO.getIndexName(), mdAttributeConcreteDAO.getOid());
        this.storeTransactionEntityDAO(mdAttributeConcreteDAO);
      }
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#unregisterPermissionEntity(com.runwaysdk.session.PermissionEntity)
   */
  public void unregisterPermissionEntity(PermissionEntity permissionEntity)
  {
    this.transactionStateLock.lock();
    try
    {
      this.unregisterPermissionEntity.add(permissionEntity);
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * Returns the oid of the object before it was changed in this transaction.
   * 
   * @return oid of the object before it was changed in this transaction, null
   *         if there is no original oid.
   */
  public String getOriginalId(String oid)
  {
    String oldId = null;

    String loopValue = oid;
    while (true)
    {
      loopValue = this.changedIds.get(loopValue);

      if (loopValue != null)
      {
        oldId = loopValue;
      }
      else
      {
        break;
      }
    }

    return oldId;

  }

  /**
   * When the oid of an object changes, update all caches.
   * 
   * @param oldId
   * @param entityDAO
   */
  public void changeCacheId(String oldId, EntityDAO entityDAO)
  {
    this.transactionStateLock.lock();
    try
    {
      this.changedIds.put(entityDAO.getOid(), oldId);

      TransactionItemEntityDAOAction transactionItemEntityDAOAction = updatedEntityDAOIdMap.get(oldId);
      if (transactionItemEntityDAOAction != null)
      {
        ActionEnumDAO actionEnumDAO = transactionItemEntityDAOAction.getAction();
        transactionItemEntityDAOAction = TransactionItemEntityDAOAction.factory(actionEnumDAO, entityDAO, this);
        this.updatedEntityDAOIdMap.put(entityDAO.getOid(), transactionItemEntityDAOAction);
        this.updatedEntityDAOIdMap.remove(oldId);
        this.entitiesToRefreshFromGlobalCache.remove(entityDAO.getOid());
      }

      this.changeEntityIdInCache(oldId, entityDAO);

      this.updateBusinessDAORelationships(oldId, entityDAO.getOid());

      if (entityDAO instanceof MdEntityDAO)
      {
        MdEntityDAO mdEntityDAO = (MdEntityDAO) entityDAO;
        String oldRootId = IdParser.parseRootFromId(oldId);
        this.updatedMdClassRootIdMap.remove(oldRootId);
        this.updatedMdClassRootIdMap.put(mdEntityDAO.getRootId(), mdEntityDAO.getOid());

        Set<String> parentRelationshipSet = this.mdBusinessParentMdRelationships.get(oldId);
        if (parentRelationshipSet != null)
        {
          this.mdBusinessParentMdRelationships.remove(oldId);
          this.mdBusinessParentMdRelationships.put(entityDAO.getOid(), parentRelationshipSet);
        }

        Set<String> childRelationshipSet = this.mdBusinessChildMdRelationships.get(oldId);
        if (childRelationshipSet != null)
        {
          this.mdBusinessChildMdRelationships.remove(oldId);
          this.mdBusinessChildMdRelationships.put(entityDAO.getOid(), childRelationshipSet);
        }
      }
      else if (entityDAO instanceof MdGraphClassDAO)
      {
        MdGraphClassDAO mdGraphClassDAO = (MdGraphClassDAO) entityDAO;
        String oldRootId = IdParser.parseRootFromId(oldId);
        this.updatedMdClassRootIdMap.remove(oldRootId);
        this.updatedMdClassRootIdMap.put(mdGraphClassDAO.getRootId(), mdGraphClassDAO.getOid());

        Set<String> parentRelationshipSet = this.mdVertexParentMdEdges.get(oldId);
        if (parentRelationshipSet != null)
        {
          this.mdVertexParentMdEdges.remove(oldId);
          this.mdVertexParentMdEdges.put(entityDAO.getOid(), parentRelationshipSet);
        }

        Set<String> childRelationshipSet = this.mdVertexChildMdEdges.get(oldId);
        if (childRelationshipSet != null)
        {
          this.mdVertexChildMdEdges.remove(oldId);
          this.mdVertexChildMdEdges.put(entityDAO.getOid(), childRelationshipSet);
        }
      }

      if (entityDAO instanceof MdAttributeDAO)
      {
        MdAttributeDAO mdAttribute = (MdAttributeDAO) entityDAO;

        this.addedMdAttrubiteKeyMap.removeValue(oldId);
        this.addedMdAttrubiteKeyMap.put(mdAttribute.getKey(), mdAttribute.getOid());
      }

      if (this.updatedEnumerationItemSet_CodeGeneration.contains(oldId))
      {
        this.updatedEnumerationItemSet_CodeGeneration.remove(oldId);
        this.updatedEnumerationItemSet_CodeGeneration.add(entityDAO.getOid());
      }

      if (this.newMdAttributeSet_CodeGeneration.contains(oldId))
      {
        this.newMdAttributeSet_CodeGeneration.remove(oldId);
        this.newMdAttributeSet_CodeGeneration.add(entityDAO.getOid());
      }

      if (this.newMdRelationshipSet_CodeGeneration.contains(oldId))
      {
        this.newMdRelationshipSet_CodeGeneration.remove(oldId);
        this.newMdRelationshipSet_CodeGeneration.add(entityDAO.getOid());
      }

      if (this.newEnumerationAttributeItemSet_CodeGeneration.contains(oldId))
      {
        this.newEnumerationAttributeItemSet_CodeGeneration.remove(oldId);
        this.newEnumerationAttributeItemSet_CodeGeneration.add(entityDAO.getOid());
      }

      if (this.updatedMdMethod_CodeGeneration.contains(oldId))
      {
        this.updatedMdMethod_CodeGeneration.remove(oldId);
        this.updatedMdMethod_CodeGeneration.add(entityDAO.getOid());
      }

      if (this.updatedMdParameter_CodeGeneration.contains(oldId))
      {
        this.updatedMdParameter_CodeGeneration.remove(oldId);
        this.updatedMdParameter_CodeGeneration.add(entityDAO.getOid());
      }

      if (this.updatedMdTypeSet_CodeGeneration.contains(oldId))
      {
        this.updatedMdTypeSet_CodeGeneration.remove(oldId);
        this.updatedMdTypeSet_CodeGeneration.add(entityDAO.getOid());
      }

    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#updateEntityDAO(com.runwaysdk.dataaccess.EntityDAO)
   */
  public void updateEntityDAO(EntityDAO entityDAO)
  {
    this.transactionStateLock.lock();
    try
    {
      MdEntityDAOIF mdEntityDAOIF = entityDAO.getMdClassDAO();

      // Record that this object's type is participating in this transaction.
      String mdTypeRootId = IdParser.parseMdTypeRootIdFromId(entityDAO.getOid());
      this.typeRootIdsInTransaction.add(mdTypeRootId);

      if (mdEntityDAOIF.isNotCached() && entityDAO.isNew())
      {
        this.recordNewlyCreatedNonCachedEntity(entityDAO);
      } // (!mdEntityDAOIF.isNotCached() || !entityDAO.isNew())
      else
      {
        TransactionItemEntityDAOAction transactionCacheItem = this.createTransactionItemForEntity(entityDAO);

        this.storeTransactionEntityDAO(entityDAO);
        this.addToUpdatedEntityTransactionItemMap(transactionCacheItem);

        if (entityDAO instanceof MdClassDAO)
        {
          MdClassDAO mdClassDAO = (MdClassDAO) entityDAO;

          this.updatedMdClassDefinedTypeMap.put(mdClassDAO.definesType(), mdClassDAO.getOid());
          this.updatedMdClassRootIdMap.put(mdClassDAO.getRootId(), mdClassDAO.getOid());

          if (mdClassDAO instanceof MdEntityDAO)
          {
            this.updatedMdClassTableNameMap.put( ( (MdEntityDAO) mdClassDAO ).getTableName(), mdClassDAO.getOid());
          }

          if (mdClassDAO instanceof MdGraphClassDAO)
          {
            this.updatedMdClassTableNameMap.put( ( (MdGraphClassDAO) mdClassDAO ).getDBClassName(), mdClassDAO.getOid());
          }

          if (mdClassDAO instanceof MdRelationshipDAO)
          {
            MdRelationshipDAO mdRelationshipDAO = (MdRelationshipDAO) mdClassDAO;

            if (mdRelationshipDAO.getAttributeIF(MdRelationshipInfo.PARENT_MD_BUSINESS).isModified())
            {
              String parentMdBusinessId = mdRelationshipDAO.getAttributeIF(MdRelationshipInfo.PARENT_MD_BUSINESS).getValue();
              Set<String> parentRelationshipSet;
              if (this.mdBusinessParentMdRelationships.containsKey(parentMdBusinessId))
              {
                parentRelationshipSet = this.mdBusinessParentMdRelationships.get(parentMdBusinessId);
              }
              else
              {
                // Clone the Set in the global cache. This will be used for the
                // rest of the transaction.
                parentRelationshipSet = new HashSet<String>(ObjectCache.getParentMdRelationshipDAOids(parentMdBusinessId));
                this.mdBusinessParentMdRelationships.put(parentMdBusinessId, parentRelationshipSet);
              }
              parentRelationshipSet.add(mdRelationshipDAO.getOid());
            }

            if (mdRelationshipDAO.getAttributeIF(MdRelationshipInfo.CHILD_MD_BUSINESS).isModified())
            {
              String childMdBusinessId = mdRelationshipDAO.getAttribute(MdRelationshipInfo.CHILD_MD_BUSINESS).getValue();
              Set<String> childRelationshipSet;
              if (this.mdBusinessChildMdRelationships.containsKey(childMdBusinessId))
              {
                childRelationshipSet = this.mdBusinessChildMdRelationships.get(childMdBusinessId);
              }
              else
              {
                // Clone the Set in the global cache. This will be used for the
                // rest of the transaction.
                childRelationshipSet = new HashSet<String>(ObjectCache.getChildMdRelationshipDAOids(childMdBusinessId));
                this.mdBusinessChildMdRelationships.put(childMdBusinessId, childRelationshipSet);
              }
              childRelationshipSet.add(mdRelationshipDAO.getOid());
            }
          }
          if (mdClassDAO instanceof MdEdgeDAO)
          {
            MdEdgeDAO mdEdgeDAO = (MdEdgeDAO) mdClassDAO;

            if (mdEdgeDAO.getAttributeIF(MdEdgeInfo.PARENT_MD_VERTEX).isModified())
            {
              String parentMdVertexId = mdEdgeDAO.getAttributeIF(MdEdgeInfo.PARENT_MD_VERTEX).getValue();
              Set<String> parentRelationshipSet;
              if (this.mdVertexParentMdEdges.containsKey(parentMdVertexId))
              {
                parentRelationshipSet = this.mdVertexParentMdEdges.get(parentMdVertexId);
              }
              else
              {
                // Clone the Set in the global cache. This will be used for the
                // rest of the transaction.
                parentRelationshipSet = new HashSet<String>(ObjectCache.getParentMdEdgeDAOids(parentMdVertexId));
                this.mdVertexParentMdEdges.put(parentMdVertexId, parentRelationshipSet);
              }
              parentRelationshipSet.add(mdEdgeDAO.getOid());
            }

            if (mdEdgeDAO.getAttributeIF(MdEdgeInfo.CHILD_MD_VERTEX).isModified())
            {
              String childMdVertexId = mdEdgeDAO.getAttribute(MdEdgeInfo.CHILD_MD_VERTEX).getValue();
              Set<String> childRelationshipSet;
              if (this.mdVertexChildMdEdges.containsKey(childMdVertexId))
              {
                childRelationshipSet = this.mdVertexChildMdEdges.get(childMdVertexId);
              }
              else
              {
                // Clone the Set in the global cache. This will be used for the
                // rest of the transaction.
                childRelationshipSet = new HashSet<String>(ObjectCache.getChildMdEdgeDAOids(childMdVertexId));
                this.mdVertexChildMdEdges.put(childMdVertexId, childRelationshipSet);
              }
              childRelationshipSet.add(mdEdgeDAO.getOid());
            }
          }
        } // if (entityDAO instanceof MdClassDAO)
        else if (entityDAO instanceof MdEnumerationDAO)
        {
          MdEnumerationDAO mdEnumeration = (MdEnumerationDAO) entityDAO;
          this.updatedMdEnumerationMap.put(mdEnumeration.definesEnumeration(), mdEnumeration.getOid());

          // Regenerate the EnumerationMaster class
          if (!mdEnumeration.isImport() && mdEnumeration.isNew())
          {
            this.updatedMdEnumerationMap_CodeGeneration.put(mdEnumeration.definesEnumeration(), mdEnumeration.getOid());
          }
        }
        else if (entityDAO instanceof MdIndexDAO)
        {
          MdIndexDAO mdIndexDAO = (MdIndexDAO) entityDAO;
          this.updatedMdIndexMap.put(mdIndexDAO.getIndexName(), mdIndexDAO.getOid());
        }
        else if (entityDAO instanceof EnumerationItemDAO && entityDAO.getAttribute(EnumerationMasterInfo.NAME).isModified())
        {
          if (!entityDAO.isImport())
          {
            this.updatedEnumerationItemSet_CodeGeneration.add(entityDAO.getOid());
          }
        }
        else if (entityDAO instanceof RoleDAOIF)
        {
          RoleDAOIF roleIF = (RoleDAOIF) entityDAO;
          this.updatedRoleIFMap.put(roleIF.getRoleName(), roleIF.getOid());
        }
      } // mdEntityDAOIF.isNotCached()
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#updateEntityDAOCollection(java.lang.String,
   *      com.runwaysdk.dataaccess.cache.CacheStrategy)
   */
  public void updateEntityDAOCollection(String type, CacheStrategy entityDAOCollectoin)
  {
    this.transactionStateLock.lock();
    try
    {
      TransactionItemStrategyAction transactionCacheItem = new TransactionItemStrategyAction(ActionEnumDAO.UPDATE, entityDAOCollectoin);
      this.updatedEntityNameCacheStrategyMap.put(type, transactionCacheItem);
      this.updatedCollectionTransactionItemList.add(transactionCacheItem);
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#updateTransientDAO(com.runwaysdk.dataaccess.TransientDAO)
   */
  public void updateTransientDAO(TransientDAO transientDAO)
  {
    this.transactionStateLock.lock();
    try
    {
      TransactionItemTransientDAOAction transactionCacheItem;

      if (transientDAO.isNew())
      {
        transactionCacheItem = new TransactionItemTransientDAOAction(ActionEnumDAO.CREATE, transientDAO);
      }
      else
      {
        transactionCacheItem = new TransactionItemTransientDAOAction(ActionEnumDAO.UPDATE, transientDAO);
      }

      this.updatedTransientTransactionItemList.add(transactionCacheItem);
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#updatedMdMethod_CodeGen(com.runwaysdk.dataaccess.metadata.MdMethodDAO)
   */
  public void updatedMdMethod_CodeGen(MdMethodDAO mdMethodDAO)
  {
    this.transactionStateLock.lock();
    try
    {
      this.updatedMdMethod_CodeGeneration.add(mdMethodDAO.getOid());
      this.storeTransactionEntityDAO(mdMethodDAO);
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#updatedMdParameter_CodeGen(com.runwaysdk.dataaccess.metadata.MdParameterDAO)
   */
  public void updatedMdParameter_CodeGen(MdParameterDAO mdParameterDAO)
  {
    this.transactionStateLock.lock();
    try
    {
      this.updatedMdParameter_CodeGeneration.add(mdParameterDAO.getOid());
      this.storeTransactionEntityDAO(mdParameterDAO);
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#updatedMdType(com.runwaysdk.dataaccess.metadata.MdTypeDAO)
   */
  public void updatedMdType(MdTypeDAO mdType)
  {
    this.transactionStateLock.lock();
    try
    {
      if (!mdType.shouldGenerateCode())
      {
        return;
      }

      this.updatedMdTypeSet_CodeGeneration.add(mdType.getOid());
      this.storeTransactionEntityDAO(mdType);

      if (mdType instanceof MdClassDAO)
      {
        MdClassDAO mdClass = (MdClassDAO) mdType;

        if (mdClass.getAttributeIF(MdClassInfo.PUBLISH).isModified())
        {
          if (mdClass instanceof MdStructDAO)
          {
            MdStructDAO mdStruct = (MdStructDAO) mdClass;

            for (MdAttributeStructDAOIF mdAttributeStructIF : mdStruct.getMdAttributeStruct())
            {
              // These attributes were not necessarily modified
              this.deletedMdAttributeSet_CodeGeneration.add(mdAttributeStructIF.getOid());
            }
          }
          else if (mdClass instanceof MdBusinessDAO)
          {
            MdBusinessDAO mdBusiness = (MdBusinessDAO) mdClass;

            // Get all MdAttriubteReference objects that reference this class.
            // Make sure their DTOs
            // are regenerated to not include references to this class
            List<MdAttributeReferenceDAOIF> mdAttrRefList = mdBusiness.getMdAttributeReferences();
            for (MdAttributeReferenceDAOIF mdAttributeReferenceIF : mdAttrRefList)
            {
              // These attributes were not necessarily modified
              this.deletedMdAttributeSet_CodeGeneration.add(mdAttributeReferenceIF.getOid());
            }
          }
          else if (mdClass instanceof MdRelationshipDAO)
          {
            MdRelationshipDAO mdRelationship = (MdRelationshipDAO) mdClass;
            this.deletedMdRelationshipSet_CodeGeneration.add(mdRelationship.getOid());
          }
        }
        else if (mdType instanceof MdRelationshipDAO)
        {
          MdRelationshipDAO mdRelationship = (MdRelationshipDAO) mdType;

          if (!mdRelationship.isNew() && ( mdRelationship.getAttributeIF(MdRelationshipInfo.PARENT_VISIBILITY).isModified() || mdRelationship.getAttributeIF(MdRelationshipInfo.CHILD_VISIBILITY).isModified() ))
          {
            this.deletedMdRelationshipSet_CodeGeneration.add(mdRelationship.getOid());
          }
        }
      }
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * Add the <code>MdTypeDAOIF</code> to the map if the package is not system or
   * the environment is runway
   */
  protected void addMdTypeToMapForGen(MdTypeDAOIF mdTypeDAOIF, Map<String, MdTypeDAOIF> mdTypeMap)
  {
    if (GenerationUtil.shouldGenerate(mdTypeDAOIF))
    {
      // Some types are generated as a result of the modification of other
      // types. These types
      // need to have their sequence number incremented, need to be logged, and
      // need to be updated
      // in the cache.
      if (!this.updatedEntityDAOIdMap.containsKey(mdTypeDAOIF.getOid()))
      {
        MdTypeDAO mdTypeDAO = (MdTypeDAO) mdTypeDAOIF.getBusinessDAO();
        TransactionItemEntityDAOAction transactionCacheItem = this.createTransactionItemForEntity(mdTypeDAO);
        this.storeTransactionEntityDAO(mdTypeDAO);
        this.addToUpdatedEntityTransactionItemMap(transactionCacheItem);

        mdTypeMap.put(mdTypeDAO.definesType(), mdTypeDAO);
      }
      else
      {
        // Make sure we add the one to the list that is from the transaction
        // cache, as this object was fetched
        // from in the control flow of the TransactionManagement around advice
        // and it may not be this
        // transaction's cached copy.
        TransactionItemEntityDAOAction transactionCacheItem = updatedEntityDAOIdMap.get(mdTypeDAOIF.getOid());
        MdTypeDAOIF cachedMdTypeDAOIF = (MdTypeDAOIF) transactionCacheItem.getEntityDAO();
        mdTypeMap.put(mdTypeDAOIF.definesType(), cachedMdTypeDAOIF);
      }
    }
  }

  protected String buildMethKeySignature(EntityDAO entityDAO, String signature)
  {
    return entityDAO.getOid() + "-" + signature;
  }

  /**
   * Adds the updated item to the list that records updated entities that are
   * then updated in the global cache.
   * 
   * @param _transactionCacheItem
   */
  protected void addToUpdatedEntityTransactionItemMap(TransactionItemEntityDAOAction _transactionCacheItem)
  {
    this.transactionStateLock.lock();
    try
    {
      this.updatedEntityDAOIdMap.put(_transactionCacheItem.getEntityDAOid(), _transactionCacheItem);
      this.entitiesToRefreshFromGlobalCache.remove(_transactionCacheItem.getEntityDAOid());
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * Marks entities to be cleared from the global cache. Should the entity be of
   * a cached type, the next request for the object from the global cache will
   * refresh the object from the global cache.
   * 
   * @param entityId
   */
  public void refreshEntityInGlobalCache(String entityId)
  {
    this.entitiesToRefreshFromGlobalCache.add(entityId);
  }

  /**
   * Update actors of permission changes.
   */
  protected void updateActorPermissions()
  {
    for (ActorDAOIF actorIF : this.notifyActors)
    {
      PermissionObserver.notify(actorIF);
    }
  }

  /**
   * Unregisters actors from being notified of permission changes.
   */
  protected void unregisterPermissionEntities()
  {
    for (PermissionEntity permissionEntity : this.unregisterPermissionEntity)
    {
      PermissionObserver.unregister(permissionEntity);
    }
  }

}
