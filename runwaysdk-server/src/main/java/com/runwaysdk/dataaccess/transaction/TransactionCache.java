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
 * Created on Mar 9, 2005
 */
package com.runwaysdk.dataaccess.transaction;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.ServerProperties;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.EnumerationAttributeItem;
import com.runwaysdk.dataaccess.EnumerationItemDAO;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.TransientDAO;
import com.runwaysdk.dataaccess.cache.CacheStrategy;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.cache.RelationshipDAOCollection;
import com.runwaysdk.dataaccess.cache.TransactionMemorystore;
import com.runwaysdk.dataaccess.cache.TransactionStore;
import com.runwaysdk.dataaccess.cache.TransactionStoreIF;
import com.runwaysdk.dataaccess.metadata.MdActionDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdMethodDAO;
import com.runwaysdk.dataaccess.metadata.MdParameterDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;

/**
 *Caches all Relationships and EntityDAOs that were added, removed, or modified
 * during a single transaction.
 * 
 * @author nathan
 * @version $Revision: 1.15 $
 * @since 1.4
 */
public class TransactionCache extends AbstractTransactionCache
{
  private TransactionStoreIF cache;

  /**
   * Initializes all caches.
   * 
   * <br/>
   * <b>Precondition:</b> true <br/>
   * <b>Postcondition:</b> true
   * 
   */
  protected TransactionCache(ReentrantLock transactionStateLock)
  {
    this(transactionStateLock, ServerProperties.getTransactionCacheMemorySize(), ServerProperties.memoryOnlyTransactionCache());
  }

  /**
   * Initializes all caches.
   * 
   * <br/>
   * <b>Precondition:</b> true <br/>
   * <b>Postcondition:</b> true
   * 
   */
  protected TransactionCache(ReentrantLock transactionStateLock, int memorySize, boolean memoryOnly)
  {
    super(transactionStateLock);

    if (memoryOnly)
    {
      this.cache = new TransactionMemorystore();
    }
    else
    {
      this.cache = new TransactionStore(memorySize);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.dataaccess.transaction.TransactionCacheIF#addCacheState(com
   * .runwaysdk.dataaccess.transaction.TransactionCacheIF)
   */
  public void addCacheState(ThreadTransactionCache threadTransactionCache)
  {
    this.deletedEntitySignatures.addAll(threadTransactionCache.deletedEntitySignatures);

    this.updatedEntityDAOIdMap.putAll(threadTransactionCache.updatedEntityDAOIdMap);
    this.updatedEntityDAOKeyMap.putAll(threadTransactionCache.updatedEntityDAOKeyMap);

    for (String businessDAOid : threadTransactionCache.updatedBusinessDAORelationships.keySet())
    {
      TransactionBusinessDAORelationships threadRels = threadTransactionCache.updatedBusinessDAORelationships.get(businessDAOid);

      if (!this.updatedBusinessDAORelationships.containsKey(businessDAOid))
      {
        this.updatedBusinessDAORelationships.put(businessDAOid, threadRels);
      }
      else
      {
        TransactionBusinessDAORelationships rels = this.updatedBusinessDAORelationships.get(businessDAOid);
        rels.addAllRelationships(threadRels);
      }
    }

    this.updatedEntityNameCacheStrategyMap.putAll(threadTransactionCache.updatedEntityNameCacheStrategyMap);
    this.deleteEntityNameCacheStrategyMap.putAll(threadTransactionCache.deleteEntityNameCacheStrategyMap);

    this.updatedCollectionTransactionItemList.addAll(threadTransactionCache.updatedCollectionTransactionItemList);
    // Heads up: test
    // this.updatedEntityTransactionItemList_UpdateCaches.addAll(threadTransactionCache.updatedEntityTransactionItemList_UpdateCaches);
    this.updatedTransientTransactionItemList.addAll(threadTransactionCache.updatedTransientTransactionItemList);

    this.updatedMdClassDefinedTypeMap.putAll(threadTransactionCache.updatedMdClassDefinedTypeMap);
    this.updatedMdClassRootIdMap.putAll(threadTransactionCache.updatedMdClassRootIdMap);
    this.updatedRoleIFMap.putAll(threadTransactionCache.updatedRoleIFMap);
    this.updatedMdFacadeMap.putAll(threadTransactionCache.updatedMdFacadeMap);

    this.updatedStateMasterMap.putAll(threadTransactionCache.updatedStateMasterMap);

    this.updatedTransitionMap.putAll(threadTransactionCache.updatedTransitionMap);

    this.addedMdAttrubiteKeyMap.putAll(threadTransactionCache.addedMdAttrubiteKeyMap);

    // Used for determining which Types need classes to be generated or
    // regenerated.
    this.newMdAttributeSet_CodeGeneration.addAll(threadTransactionCache.newMdAttributeSet_CodeGeneration);
    this.deletedMdAttributeSet_CodeGeneration.addAll(threadTransactionCache.deletedMdAttributeSet_CodeGeneration);
    this.newMdRelationshipSet_CodeGeneration.addAll(threadTransactionCache.newMdRelationshipSet_CodeGeneration);
    this.deletedMdRelationshipSet_CodeGeneration.addAll(threadTransactionCache.deletedMdRelationshipSet_CodeGeneration);
    this.newEnumerationAttributeItemSet_CodeGeneration.addAll(threadTransactionCache.newEnumerationAttributeItemSet_CodeGeneration);
    this.deletedEnumerationAttributeItemSet_CodeGeneration.addAll(threadTransactionCache.deletedEnumerationAttributeItemSet_CodeGeneration);

    this.updatedMdMethod_CodeGeneration.addAll(threadTransactionCache.updatedMdMethod_CodeGeneration);
    this.updatedMdAction_CodeGeneration.addAll(threadTransactionCache.updatedMdAction_CodeGeneration);
    this.updatedMdParameter_CodeGeneration.addAll(threadTransactionCache.updatedMdParameter_CodeGeneration);

    this.updatedMdTypeSet_CodeGeneration.addAll(threadTransactionCache.updatedMdTypeSet_CodeGeneration);
    this.deletedMdTypeMap_CodeGeneration.putAll(threadTransactionCache.deletedMdTypeMap_CodeGeneration);
    this.updatedEnumerationItemSet_CodeGeneration.addAll(threadTransactionCache.updatedEnumerationItemSet_CodeGeneration);

    this.updatedMdEnumerationMap_CodeGeneration.putAll(threadTransactionCache.updatedMdEnumerationMap_CodeGeneration);

    this.updatedMdEnumerationMap.putAll(threadTransactionCache.updatedMdEnumerationMap);

    this.updatedMdIndexMap.putAll(threadTransactionCache.updatedMdIndexMap);

    this.indexNameMap.putAll(threadTransactionCache.indexNameMap);

    this.notifyActors.addAll(threadTransactionCache.notifyActors);
    this.unregisterPermissionEntity.addAll(threadTransactionCache.unregisterPermissionEntity);

    this.dmlTableName.addAll(threadTransactionCache.dmlTableName);

    this.webServiceMdFacadeMapDeploy.putAll(threadTransactionCache.webServiceMdFacadeMapDeploy);
    this.webServiceMdFacadeMapUndeploy.putAll(threadTransactionCache.webServiceMdFacadeMapUndeploy);

    for (String key : threadTransactionCache.cachedAddedRelationships.keySet())
    {
      RelationshipDAOCollection threadCollection = threadTransactionCache.cachedAddedRelationships.get(key);
      RelationshipDAOCollection mainCollection = this.cachedAddedRelationships.get(key);

      if (mainCollection == null)
      {
        this.cachedAddedRelationships.put(key, threadCollection);
      }
      else
      {
        mainCollection.addAll(threadCollection);
      }
    }

    for (String key : threadTransactionCache.cachedRemovedRelationships.keySet())
    {
      RelationshipDAOCollection threadCollection = threadTransactionCache.cachedRemovedRelationships.get(key);
      RelationshipDAOCollection mainCollection = this.cachedRemovedRelationships.get(key);

      if (mainCollection == null)
      {
        this.cachedRemovedRelationships.put(key, threadCollection);
      }
      else
      {
        mainCollection.addAll(threadCollection);
      }
    }

    for (String key : threadTransactionCache.mdBusinessParentMdRelationships.keySet())
    {
      Set<String> threadParentRelationships = threadTransactionCache.mdBusinessParentMdRelationships.get(key);
      Set<String> mainParentRelationships = this.mdBusinessParentMdRelationships.get(key);

      if (mainParentRelationships == null)
      {
        this.mdBusinessParentMdRelationships.put(key, threadParentRelationships);
      }
      else
      {
        mainParentRelationships.addAll(threadParentRelationships);
      }
    }

    for (String key : threadTransactionCache.mdBusinessChildMdRelationships.keySet())
    {
      Set<String> threadChildRelationships = threadTransactionCache.mdBusinessChildMdRelationships.get(key);
      Set<String> mainChildRelationships = this.mdBusinessChildMdRelationships.get(key);

      if (mainChildRelationships == null)
      {
        this.mdBusinessChildMdRelationships.put(key, threadChildRelationships);
      }
      else
      {
        mainChildRelationships.addAll(threadChildRelationships);
      }
    }

    // Heads up: test
    // this.transactionObjectCache.putAll(threadTransactionCache.transactionObjectCache);

    for (EntityDAO entityDAO : threadTransactionCache.transactionObjectCache.values())
    {
      this.storeTransactionEntityDAO(entityDAO);
    }

  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#getEntityDAO(java.lang.String)
   */
  public EntityDAOIF getEntityDAO(String id)
  {
    EntityDAOIF entityDAOIF = null;

    this.transactionStateLock.lock();
    try
    {
      TransactionItemEntityDAOAction transactionCacheItem = this.updatedEntityDAOIdMap.get(id);
      if (transactionCacheItem != null)
      {
        entityDAOIF = (EntityDAOIF) this.cache.getEntityDAOIFfromCache(id);
      }

      return entityDAOIF;

    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * Stores an {@link EntityDAO} that was modified in this transaction in a
   * transaction cache.
   * 
   * @param entityDAO
   */
  protected void storeTransactionEntityDAO(EntityDAO entityDAO)
  {
    this.transactionStateLock.lock();
    try
    {
      this.cache.putEntityDAOIFintoCache(entityDAO);
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * Returns the current transaction cache object of this transaction.
   * 
   * @return current transaction cache object of this transaction.
   */
  public static TransactionCacheIF getCurrentTransactionCache()
  {
    // Aspect will return the current object.
    return null;
  }

  /**
   * Returns a list of MdTypes that will have Java classes deleted and unloaded.
   * 
   * @return list of MdTypes that will have Java classes deleted and unloaded.
   */
  protected Collection<MdTypeDAOIF> getMdTypesForClassRemoval()
  {
    this.transactionStateLock.lock();
    try
    {
      Collection<MdTypeDAOIF> mdTypesForClassRemoval = new LinkedList<MdTypeDAOIF>();

      for (String mdTypeId : this.deletedMdTypeMap_CodeGeneration.values())
      {
        mdTypesForClassRemoval.add((MdTypeDAOIF) this.internalGetEntityDAO(mdTypeId));
      }

      return mdTypesForClassRemoval;
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * Returns a list of MdTypes that will have Java classes
   * generated/regenerated.
   * 
   * @return list of MdTypes that will have Java classes generated/regenerated.
   */
  protected Collection<MdTypeDAOIF> getMdTypesForCodeGeneration()
  {
    this.transactionStateLock.lock();
    try
    {
      Map<String, MdTypeDAOIF> mdTypeMap = new HashMap<String, MdTypeDAOIF>();

      Iterator<String> updatedMdMethodIterator = this.updatedMdMethod_CodeGeneration.iterator();
      while (updatedMdMethodIterator.hasNext())
      {
        String updatedMdMethodId = updatedMdMethodIterator.next();
        MdMethodDAO mdMethod = (MdMethodDAO) this.internalGetEntityDAO(updatedMdMethodId);
        MdTypeDAOIF mdTypeDAOIF = mdMethod.getEnclosingMdTypeDAO();
        this.addMdTypeToMapForGen(mdTypeDAOIF, mdTypeMap);
      }

      Iterator<String> updatedMdActionIterator = this.updatedMdAction_CodeGeneration.iterator();
      while (updatedMdActionIterator.hasNext())
      {
        String updatedMdActionId = updatedMdActionIterator.next();
        MdActionDAO mdAction = (MdActionDAO) this.internalGetEntityDAO(updatedMdActionId);
        MdTypeDAOIF mdTypeDAOIF = mdAction.getEnclosingMdTypeDAO();
        this.addMdTypeToMapForGen(mdTypeDAOIF, mdTypeMap);
      }

      Iterator<String> updatedMdParameterIterator = this.updatedMdParameter_CodeGeneration.iterator();
      while (updatedMdParameterIterator.hasNext())
      {
        String updatedMdParameterId = updatedMdParameterIterator.next();
        MdParameterDAO mdParameter = (MdParameterDAO) this.internalGetEntityDAO(updatedMdParameterId);
        MdTypeDAOIF mdTypeDAOIF = mdParameter.getEnclosingMetadata().getEnclosingMdTypeDAO();
        this.addMdTypeToMapForGen(mdTypeDAOIF, mdTypeMap);
      }

      Iterator<String> newMdAttrId = this.newMdAttributeSet_CodeGeneration.iterator();
      while (newMdAttrId.hasNext())
      {
        String mdAttributeDAOid = newMdAttrId.next();
        MdAttributeDAO mdAttribute = (MdAttributeDAO) this.internalGetEntityDAO(mdAttributeDAOid);
        MdClassDAOIF definingMdClassDAOIF = (MdClassDAOIF) mdAttribute.definedByClass();
        this.addMdTypeToMapForGen(definingMdClassDAOIF, mdTypeMap);
      }

      Iterator<String> deletedMdAttributeIttr = this.deletedMdAttributeSet_CodeGeneration.iterator();

      while (deletedMdAttributeIttr.hasNext())
      {
        String deletedMdAttributeId = deletedMdAttributeIttr.next();
        MdAttributeDAOIF mdAttributeIF = (MdAttributeDAOIF) this.internalGetEntityDAO(deletedMdAttributeId);

        MdClassDAOIF definingMdClassDAOIF = (MdClassDAOIF) mdAttributeIF.definedByClass();
        this.addMdTypeToMapForGen(definingMdClassDAOIF, mdTypeMap);

        if (definingMdClassDAOIF instanceof MdBusinessDAOIF)
        {
          MdBusinessDAOIF definingMdBusinessIF = (MdBusinessDAOIF) definingMdClassDAOIF;

          MdBusinessDAOIF superMdBusinessIF = definingMdBusinessIF.getSuperClass();

          if (superMdBusinessIF != null)
          {
            if (definingMdBusinessIF.getSuperClass().definesType().equals(EnumerationMasterInfo.CLASS))
            {
              for (MdEnumerationDAOIF mdEnumerationDAOIF : definingMdBusinessIF.getMdEnumerationDAOs())
              {
                this.addMdTypeToMapForGen(mdEnumerationDAOIF, mdTypeMap);
              }
            }
          }
        }
      }

      // If we delete a business class, any MdAttributeReference that used the
      // class is implicitly deleated as well.
      // Consequently, the map above will contain an entry for the attribute.
      // The same is true for deleting a struct class. The MdAttributeStruct is
      // dropped.
      Iterator<String> mdRelationshipIterator = this.newMdRelationshipSet_CodeGeneration.iterator();
      while (mdRelationshipIterator.hasNext())
      {
        String mdRelationshipId = mdRelationshipIterator.next();
        MdRelationshipDAO mdRelationship = (MdRelationshipDAO) this.internalGetEntityDAO(mdRelationshipId);

        MdBusinessDAOIF parentMdBusinessDAOIF = mdRelationship.getParentMdBusiness();
        MdBusinessDAOIF childMdBusinessDAOIF = mdRelationship.getChildMdBusiness();

        this.addMdTypeToMapForGen(parentMdBusinessDAOIF, mdTypeMap);
        this.addMdTypeToMapForGen(childMdBusinessDAOIF, mdTypeMap);
      }

      Iterator<String> deletedMdRelationshipIttr = this.deletedMdRelationshipSet_CodeGeneration.iterator();
      while (deletedMdRelationshipIttr.hasNext())
      {
        String mdRelationshipId = deletedMdRelationshipIttr.next();
        MdRelationshipDAOIF mdRelationshipIF = (MdRelationshipDAOIF) this.internalGetEntityDAO(mdRelationshipId);

        MdBusinessDAOIF parentMdBusinessDAOIF = mdRelationshipIF.getParentMdBusiness();
        MdBusinessDAOIF childMdBusinessDAOIF = mdRelationshipIF.getChildMdBusiness();
        this.addMdTypeToMapForGen(parentMdBusinessDAOIF, mdTypeMap);
        this.addMdTypeToMapForGen(childMdBusinessDAOIF, mdTypeMap);
      }

      // If we delete a business class, any relationship class that it
      // participates in is implicitly deleted as well.
      // Consequently, the map above will contain an entry.
      Iterator<String> enumItemIterator = this.newEnumerationAttributeItemSet_CodeGeneration.iterator();
      while (enumItemIterator.hasNext())
      {
        String enumItemId = enumItemIterator.next();
        EnumerationAttributeItem enumerationAttributeItem = (EnumerationAttributeItem) this.internalGetEntityDAO(enumItemId);
        MdEnumerationDAOIF mdEnumerationDAOIF = enumerationAttributeItem.getMdEnumerationDAO();
        this.addMdTypeToMapForGen(mdEnumerationDAOIF, mdTypeMap);
      }

      Iterator<String> delEnumIterator = this.deletedEnumerationAttributeItemSet_CodeGeneration.iterator();
      while (delEnumIterator.hasNext())
      {
        String enumItemId = delEnumIterator.next();
        EnumerationAttributeItem enumerationAttributeItem = (EnumerationAttributeItem) this.internalGetEntityDAO(enumItemId);
        MdEnumerationDAOIF mdEnumerationDAOIF = enumerationAttributeItem.getMdEnumerationDAO();
        this.addMdTypeToMapForGen(mdEnumerationDAOIF, mdTypeMap);
      }

      Iterator<String> updatedMdTypeIterator = this.updatedMdTypeSet_CodeGeneration.iterator();
      while (updatedMdTypeIterator.hasNext())
      {
        String updatedMdTypeId = updatedMdTypeIterator.next();
        MdTypeDAO mdTypeDAO = (MdTypeDAO) this.internalGetEntityDAO(updatedMdTypeId);
        this.addMdTypeToMapForGen(mdTypeDAO, mdTypeMap);
      }

      Iterator<String> enumIterator = this.updatedEnumerationItemSet_CodeGeneration.iterator();
      while (enumIterator.hasNext())
      {
        String enumIteratorId = enumIterator.next();
        EnumerationItemDAO enumerationItem = (EnumerationItemDAO) this.internalGetEntityDAO(enumIteratorId);

        for (MdEnumerationDAOIF mdEnumerationDAOIF : enumerationItem.getParticipatingMdEnumerations())
        {
          this.addMdTypeToMapForGen(mdEnumerationDAOIF, mdTypeMap);
        }
      }

      // Regenerate the EnumerationMaster class
      for (String mdEnumerationDAOIid : this.updatedMdEnumerationMap_CodeGeneration.values())
      {
        MdEnumerationDAOIF mdEnumerationDAOIF = (MdEnumerationDAOIF) this.internalGetEntityDAO(mdEnumerationDAOIid);

        MdBusinessDAOIF mdBusinessDAOIF = mdEnumerationDAOIF.getMasterListMdBusinessDAO();
        this.addMdTypeToMapForGen(mdBusinessDAOIF, mdTypeMap);
      }

      // Remove types that are to be deleted
      for (String definesType : deletedMdTypeMap_CodeGeneration.keySet())
      {
        mdTypeMap.remove(definesType);
      }

      return mdTypeMap.values();
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * Returns true if an operation has occured in the transaction that should
   * cause the entire code stream to recompile. This should happen when a
   * metadata object has been deleted.
   * 
   * @return
   */
  protected boolean shouldCompileAll()
  {
    this.transactionStateLock.lock();
    try
    {
      if (this.deletedMdAttributeSet_CodeGeneration.size() > 0 || this.deletedMdRelationshipSet_CodeGeneration.size() > 0 || this.deletedEnumerationAttributeItemSet_CodeGeneration.size() > 0 || this.deletedMdTypeMap_CodeGeneration.size() > 0)
      {
        return true;
      }
      else
      {
        return false;
      }
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * 
   * @return
   */
  protected Map<String, TransactionItemAction> getClassTypeCollectionsMap()
  {
    this.transactionStateLock.lock();
    try
    {
      HashMap<String, TransactionItemAction> temp = new HashMap<String, TransactionItemAction>(this.updatedEntityNameCacheStrategyMap);
      temp.putAll(this.deleteEntityNameCacheStrategyMap);
      return temp;
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * Flushes the objects in the transaction cache into ObjectCache classes where
   * the objects are dispatched to the appropriate collections. <br/>
   * <b>Precondition:</b> true <br/>
   * <b>Postcondition:</b> true
   */
  public void updateCaches()
  {
    this.transactionStateLock.lock();
    try
    {
      List<MdBusinessDAO> mdBusinessList = new LinkedList<MdBusinessDAO>();
      // Heads up: test
      // for (TransactionItemEntityDAOAction transCacheItem :
      // this.updatedEntityTransactionItemList_UpdateCaches)
      for (TransactionItemEntityDAOAction transCacheItem : this.updatedEntityDAOIdMap.values())
      {
        EntityDAO entityDAO = (EntityDAO) transCacheItem.getEntityDAO();

        if (transCacheItem.getAction() == ActionEnumDAO.CREATE || transCacheItem.getAction() == ActionEnumDAO.UPDATE)
        {
          entityDAO.setCommitState();

          ObjectCache.updateCache(entityDAO);
        }
        else if (transCacheItem.getAction() == ActionEnumDAO.DELETE)
        {
          // remove MdBusiness last
          if (entityDAO instanceof MdBusinessDAO)
          {
            mdBusinessList.add((MdBusinessDAO) entityDAO);
          }
          else
          {
            ObjectCache.removeCache(entityDAO);
          }
        }
      }

      for (TransactionItemTransientDAOAction transCacheItem : this.updatedTransientTransactionItemList)
      {
        TransientDAO transientDAO = (TransientDAO) transCacheItem.getTransientDAO();
        transientDAO.setCommitState();
      }

      // Save MdBusiness for last. Removing other classes from the cache
      // sometimes reference MdBusiness objects.
      // If you remove the MdBusiness first, then it could cause an error
      for (MdBusinessDAO mdBusiness : mdBusinessList)
      {
        ObjectCache.removeCache(mdBusiness);
      }
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * Reloads all caches that have been updated during this transactions.
   */
  public void updateCachStrategies()
  {
    // Heads up: uncomment or clean up
    // this.transactionStateLock.lock();
    // try
    // {
    // // Reload the caches, but do not update ObjectCache.
    // for (String keyString : this.updatedEntityNameCacheStrategyMap.keySet())
    // {
    // // do not reload the cache if the collection has been removed
    // if (!this.deleteEntityNameCacheStrategyMap.containsKey(keyString))
    // {
    // TransactionItemStrategyAction transCacheItem =
    // this.updatedEntityNameCacheStrategyMap.get(keyString);
    // CacheStrategy updateEntityCollection = transCacheItem.getCacheStrategy();
    // //System.out.print("\n\n Adding Cache "+keyString+" "+updateEntityCollection.getCachedIds().size());
    // updateEntityCollection.reload();
    // //System.out.println(" "+updateEntityCollection.getCachedIds().size());
    // }
    // }
    // }
    // finally
    // {
    // this.transactionStateLock.unlock();
    // }
  }

  /**
   * Applies collection classes that have been created in the database to the
   * global cache of collections.
   */
  protected void applyCollectionCaches()
  {
    this.transactionStateLock.lock();
    try
    {
      for (TransactionItemStrategyAction transCacheItem : this.updatedCollectionTransactionItemList)
      {
        CacheStrategy entityDAOCollection = transCacheItem.getCacheStrategy();
        if (transCacheItem.getAction() == ActionEnumDAO.UPDATE)
        {
          ObjectCache.addCacheStrategy(entityDAOCollection.getEntityType(), entityDAOCollection);
          // Heads up: test
          entityDAOCollection.reload();
        }
        else if (transCacheItem.getAction() == ActionEnumDAO.DELETE)
        {
          ObjectCache.removeCacheStrategy(entityDAOCollection.getEntityType());
        }
      }
      ObjectCache.flushCache();
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#updatePermissionEntities()
   */
  public void updatePermissionEntities()
  {
    this.transactionStateLock.lock();
    try
    {
      this.updateActorPermissions();
      this.unregisterPermissionEntities();
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.transaction.TransactionCacheIF#resetObjectsFromRollback()
   */
  protected void resetObjectsFromRollback()
  {
    this.transactionStateLock.lock();
    try
    {
      for (TransactionItemEntityDAOAction transactionItemAction : this.updatedEntityDAOIdMap.values())
      {
        EntityDAO entityDAO = (EntityDAO) transactionItemAction.getEntityDAO();

        if (entityDAO != null)
        {
          entityDAO.rollbackState();
        }
      }
    }
    finally
    {
      this.transactionStateLock.unlock();
    }
  }

  /**
   * Rollback the transaction cache.
   */
  public void rollbackTransactionCache()
  {
    // transactionCache.rollbackTransaction();
  }

  public void close()
  {
    cache.close();
  }

  public void put(EntityDAOIF entityDAO)
  {
    cache.putEntityDAOIFintoCache(entityDAO);
  }
}
