/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.dataaccess.cache.globalcache.ehcache;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.ehcache.Cache;
import org.ehcache.CacheManagerBuilder;
import org.ehcache.Maintainable;
import org.ehcache.PersistentCacheManager;
import org.ehcache.Status;
import org.ehcache.UserManagedCache;
import org.ehcache.config.CacheConfigurationBuilder;
import org.ehcache.config.ResourcePoolsBuilder;
import org.ehcache.config.persistence.CacheManagerPersistenceConfiguration;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.constants.ServerProperties;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.cache.CacheStrategy;
import com.runwaysdk.dataaccess.cache.ObjectStore;

public class Diskstore implements ObjectStore
{
  private static final String         COLLECTION_MAP_KEY = "COLLECTION_MAP_KEY";

  private static final String         CACHE_STATUS_KEY   = "CACHE_STATUS_KEY";

  final static Logger                 logger             = LoggerFactory.getLogger(Diskstore.class);

  private PersistentCacheManager      manager;

  private Cache<String, Serializable> mainCache;

  private String                      cacheName;

  private String                      cacheFileLocation;

  private Integer                     cacheMemorySize;

  private Integer                     offheapSize;

  private Boolean                     isInitialized;

  public Diskstore(String _cacheName, String _cacheFileLocation, Integer _cacheMemorySize, Integer _offheapSize)
  {
    this.cacheName = _cacheName;
    this.cacheFileLocation = _cacheFileLocation;
    this.cacheMemorySize = _cacheMemorySize;
    this.offheapSize = _offheapSize;
    this.isInitialized = false;
    
    this.initializeCache();
  }

  private synchronized PersistentCacheManager getCacheManager()
  {
    if (this.manager == null)
    {
      this.manager = CacheManagerBuilder.newCacheManagerBuilder().with(new CacheManagerPersistenceConfiguration(new File(this.cacheFileLocation, this.cacheName))).build(true);
    }

    return this.manager;
  }

  private synchronized void setupCache()
  {
    try
    {
      this.configureCache();

      if (!this.isCacheInitialized())
      {
        // Cache isn't usable, clear it out
        this.removeAll();
      }
      else
      {
        /*
         * When loading a cache for the first time its status should always be shutdown.
         * If it is not that means the cache wasn't shut down
         * with the shutdown method and should not be used.
         */
        if (this.isCacheShutdown())
        {
          this.setCacheStatus(new ActiveCacheMarker());
        }
        else
        {
          // The cache wasn't shutdown properly, clear it out
          this.removeAll();
        }
      }
    }
    catch (IllegalStateException e)
    {
      // Cache is corrupt. Delete the files
      this.deleteCacheFiles();
    }

    this.isInitialized = true;
  }

  public synchronized void initializeCache()
  {
    if (!this.isInitialized)
    {
      this.setupCache();
    }

    if (!isCacheInitialized())
    {
      /*
       * Delete the old disk store.
       */
      if (this.mainCache != null)
      {
        this.removeAll();
      }
      else
      {
        deleteCacheFiles();
      }

      // Initialize the cache
      configureCache();

      this.setCacheStatus(new ActiveCacheMarker());
    }
  }

  private void deleteCacheFiles()
  {
    if (this.manager != null)
    {
      this.removeAll();
    }
    
    File cacheDirectory = new File(this.cacheFileLocation, this.cacheName);

    try
    {
      FileUtils.deleteDirectory(cacheDirectory);
    }
    catch (IOException e)
    {
      // TODO Change exception type??
      throw new ProgrammingErrorException(e);
    }
  }

  private void configureCache()
  {
    Integer diskSize = ServerProperties.getGlobalCacheDiskSize();

    ResourcePoolsBuilder poolsBuilder = ResourcePoolsBuilder.newResourcePoolsBuilder();
    poolsBuilder = poolsBuilder.heap(cacheMemorySize, EntryUnit.ENTRIES);
    if (diskSize != -1)
    {
      poolsBuilder = poolsBuilder.disk(diskSize, MemoryUnit.MB, true);
    }

    if (this.offheapSize != null)
    {
      poolsBuilder = poolsBuilder.offheap(offheapSize, MemoryUnit.MB);
    }

    CacheConfigurationBuilder<Object, Object> cacheBuilder = CacheConfigurationBuilder.newCacheConfigurationBuilder();
    cacheBuilder = cacheBuilder.withResourcePools(poolsBuilder);

    this.mainCache = this.getCacheManager().createCache(this.cacheName, cacheBuilder.buildConfig(String.class, Serializable.class));
  }

  /**
   * Returns the name of the cache.
   * 
   * @return name of the cache.
   */
  public String getCacheName()
  {
    return this.cacheName;
  }

  /**
   * Returns true if the cache contains the provided key (entity oid).
   */
  public boolean containsKey(String key)
  {
    return this.mainCache.containsKey(key);
  }

  /**
   * Destroys this cache in one atmoic operation and leaves it in a state of (UNINITIALIZED). You have to call initializeCache after calling this method if you want to continue using the cache.
   */
  public synchronized void removeAll()
  {
    try
    {
      if (this.manager != null)
      {
        this.manager.close();
        Maintainable maintCache = this.manager.toMaintenance();
        maintCache.destroy();
        maintCache.close();

        this.manager = null;
        this.mainCache = null;
        this.isInitialized = false;
      }
    }
    catch (Throwable t)
    {
      logger.error("Encountered exception while destroying cache.", t);
    }
  }

  /**
   * Returns true if the cache is empty
   */
  public boolean isEmpty()
  {
    return !mainCache.iterator().hasNext();
  }

  /**
   * Shuts down the cache and flushes memory contents to a persistent store.
   */
  public void shutdown()
  {
    System.out.println("Shutting down object cache");

    if (this.manager != null)
    {
      this.setCacheStatus(new ShutdownCacheMarker());
      this.manager.close();

      this.manager = null;
      this.mainCache = null;
      this.isInitialized = false;
    }
  }

  /**
   * Returns true if the cache is initialized, false otherwise.
   */

  public synchronized boolean isCacheInitialized()
  {
    if (this.manager != null)
    {
      try
      {
        UserManagedCache<String, Serializable> cache = (UserManagedCache<String, Serializable>) this.getCacheManager().getCache(this.cacheName, String.class, Serializable.class);

        if (cache != null)
        {
          Status status = cache.getStatus();

          return status.equals(Status.AVAILABLE);
        }
      }
      catch (Exception e)
      {
        // Error occured while trying to read the cache
        // It must not exist
      }
    }

    return false;
  }

  /**
   * Returns a list of parent relationships of the given type from the cache for a {@link BusinessDAOIF} with the given oid.
   * 
   * @param oid
   * @param relationshipType
   * @return
   */
  public List<RelationshipDAOIF> getParentRelationshipsFromCache(String oid, String relationshipType)
  {
    synchronized (oid)
    {
      List<RelationshipDAOIF> returnList = null;

      Serializable entry = mainCache.get(oid);

      if (entry == null)
      {
        returnList = new LinkedList<RelationshipDAOIF>();
      }
      else
      {
        CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) entry;

        if (cachedBusinessDAOinfo.isMarkedForDelete())
        {
          returnList = new LinkedList<RelationshipDAOIF>();
        }
        else
        {
          returnList = cachedBusinessDAOinfo.getParentRelationshipDAOlist(relationshipType);
        }
      }

      return returnList;
    }
  }

  /**
   * Returns a list of child relationships of the given type from the cache for a {@link BusinessDAOIF} with the given oid.
   * 
   * @param oid
   * @param relationshipType
   * 
   * @return
   */
  public List<RelationshipDAOIF> getChildRelationshipsFromCache(String oid, String relationshipType)
  {
    synchronized (oid)
    {
      List<RelationshipDAOIF> returnList = null;

      Serializable entry = mainCache.get(oid);

      if (entry == null)
      {
        returnList = new LinkedList<RelationshipDAOIF>();
      }
      else
      {
        CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) entry;

        if (cachedBusinessDAOinfo.isMarkedForDelete())
        {
          returnList = new LinkedList<RelationshipDAOIF>();
        }
        else
        {
          returnList = cachedBusinessDAOinfo.getChildRelationshipDAOlist(relationshipType);
        }
      }

      return returnList;
    }
  }

  /**
   * Adds the {@link RelationshipDAOIF} to the parent and child relationships of the parent and child objects in the cache.
   * 
   * @param relationshipDAOIF
   */
  public void addRelationshipDAOIFtoCache(RelationshipDAOIF relationshipDAOIF)
  {
    String parentOid = relationshipDAOIF.getParentOid();
    synchronized (parentOid)
    {
      Serializable entry = getCachedEntityDAOinfo(true, RelationshipDAO.getOldParentOid(relationshipDAOIF), parentOid, CachedEntityDAOinfo.Types.BUSINESS);
      CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) entry;

      if (!cachedBusinessDAOinfo.isMarkedForDelete())
      {
        cachedBusinessDAOinfo.addChildRelationship(relationshipDAOIF);
        mainCache.put(parentOid, entry);
      }
    }

    String childOid = relationshipDAOIF.getChildOid();
    synchronized (childOid)
    {
      Serializable entry = getCachedEntityDAOinfo(true, RelationshipDAO.getOldChildOid(relationshipDAOIF), childOid, CachedEntityDAOinfo.Types.BUSINESS);
      CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) entry;

      if (!cachedBusinessDAOinfo.isMarkedForDelete())
      {
        cachedBusinessDAOinfo.addParentRelationship(relationshipDAOIF);
        mainCache.put(childOid, entry);
      }
    }
  }

  /**
   * Updates the stored oid if it has changed for the {@link RelationshipDAOIF} to the parent and child relationships of the parent and child objects in the cache.
   * 
   * @param hasIdChanged
   * @param relationshipDAOIF
   */
  public void updateRelationshipDAOIFinCache(Boolean hasIdChanged, RelationshipDAOIF relationshipDAOIF)
  {
    RelationshipDAO relationshipDAO = (RelationshipDAO) relationshipDAOIF;

    String parentOid = relationshipDAO.getParentOid();
    synchronized (parentOid)
    {
      Serializable entry = getCachedEntityDAOinfo(true, RelationshipDAO.getOldParentOid(relationshipDAOIF), parentOid, CachedEntityDAOinfo.Types.BUSINESS);
      CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) entry;

      if (!cachedBusinessDAOinfo.isMarkedForDelete())
      {
        if (hasIdChanged)
        {
          cachedBusinessDAOinfo.updateChildRelationship(relationshipDAO);
        }
        else
        {
          cachedBusinessDAOinfo.addChildRelationship(relationshipDAO);
        }
        mainCache.put(parentOid, entry);
      }
    }

    String childOid = relationshipDAO.getChildOid();
    synchronized (childOid)
    {
      Serializable entry = getCachedEntityDAOinfo(true, RelationshipDAO.getOldChildOid(relationshipDAOIF), childOid, CachedEntityDAOinfo.Types.BUSINESS);
      CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) entry;

      if (!cachedBusinessDAOinfo.isMarkedForDelete())
      {
        if (hasIdChanged)
        {
          cachedBusinessDAOinfo.updateParentRelationship(relationshipDAO);
        }
        else
        {
          cachedBusinessDAOinfo.addParentRelationship(relationshipDAO);
        }
        mainCache.put(childOid, entry);
      }
    }
  }

  /**
   * Updates the changed oid for the given {@link EntityDAOIF} in the cache.
   * 
   * <br/>
   * <b>Precondition:</b> Calling method has checked whether the oid has changed.
   * 
   * @param oldEntityId
   * @param entityDAOIF
   */
  public void updateIdEntityDAOIFinCache(String oldEntityId, EntityDAOIF entityDAOIF)
  {
    synchronized (oldEntityId)
    {
      CachedEntityDAOinfo entry = getCachedEntityDAOinfo(true, EntityDAO.getOldId(entityDAOIF), entityDAOIF.getOid(), entityDAOIF);

      if (entry != null)
      {
        entry.addEntityDAOIF(entityDAOIF);
        if (!entry.isMarkedForDelete())
        {
          mainCache.put(entityDAOIF.getOid(), entry);
        }
      }
    }
  }

  /**
   * Removes the {@link RelationshipDAOIF} from the parent relationship of the child object in the cache.
   * 
   * @param relationshipDAO
   * @param deletedObject
   *          indicates the object is being deleted from the application.
   * 
   * 
   * @return True if the child object still has parent relationships of this type.
   */
  public boolean removeParentRelationshipDAOIFtoCache(RelationshipDAO relationshipDAO, boolean deletedObject)
  {
    String childOid = relationshipDAO.getChildOid();
    synchronized (childOid)
    {
      boolean stillHasParents = false;

      Serializable entry = mainCache.get(childOid);
      if (entry != null)
      {
        CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) entry;
        stillHasParents = cachedBusinessDAOinfo.removeParentRelationship(relationshipDAO);

        if (cachedBusinessDAOinfo.removeFromGlobalCache())
        {
          if (deletedObject)
          {
            cachedBusinessDAOinfo.setMarkedForDelete();
            mainCache.put(childOid, entry);
          }

          mainCache.remove(childOid);
        }
        else
        {
          if (!cachedBusinessDAOinfo.isMarkedForDelete())
          {
            mainCache.put(childOid, entry);
          }
        }
      }

      return stillHasParents;
    }
  }

  /**
   * Removes all parent relationships of the given type for the {@link BusinessDAOIF} with the given oid.
   * 
   * @param childOid
   * @param relationshipType
   * @param deletedObject
   *          indicates the object is being deleted from the application.
   */
  public void removeAllParentRelationshipsOfType(String childOid, String relationshipType, boolean deletedObject)
  {
    synchronized (childOid)
    {
      Serializable entry = mainCache.get(childOid);
      if (entry != null)
      {
        CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) entry;
        cachedBusinessDAOinfo.removeAllParentRelationshipsOfType(relationshipType);

        if (cachedBusinessDAOinfo.removeFromGlobalCache())
        {
          if (deletedObject)
          {
            cachedBusinessDAOinfo.setMarkedForDelete();
            mainCache.put(childOid, entry);
          }

          mainCache.remove(childOid);
        }
        else
        {
          if (!cachedBusinessDAOinfo.isMarkedForDelete())
          {
            mainCache.put(childOid, entry);
          }
        }
      }
    }
  }

  /**
   * Removes the {@link RelationshipDAOIF} from the child relationship of the parent object in the cache.
   * 
   * @param relationshipDAOIF
   * @param deletedObject
   *          indicates the object is being deleted from the application.
   * @return True if the parent object still has children relationships of this type.
   */
  public boolean removeChildRelationshipDAOIFtoCache(RelationshipDAOIF relationshipDAOIF, boolean deletedObject)
  {
    String parentOid = relationshipDAOIF.getParentOid();
    synchronized (parentOid)
    {
      boolean stillHasChildren = false;

      Serializable entry = mainCache.get(parentOid);
      if (entry != null)
      {
        CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) entry;
        stillHasChildren = cachedBusinessDAOinfo.removeChildRelationship(relationshipDAOIF);

        if (cachedBusinessDAOinfo.removeFromGlobalCache())
        {
          if (deletedObject)
          {
            cachedBusinessDAOinfo.setMarkedForDelete();
            mainCache.put(parentOid, entry);
          }
          mainCache.remove(parentOid);
        }
        else
        {
          if (!cachedBusinessDAOinfo.isMarkedForDelete())
          {
            mainCache.put(parentOid, entry);
          }
        }
      }

      return stillHasChildren;
    }
  }

  /**
   * Removes all child relationships of the given type for the {@link BusinessDAOIF} with the given oid.
   * 
   * @param parentOid
   * @param relationshipType
   * @param deletedObject
   *          indicates the object is being deleted from the application.
   */
  public void removeAllChildRelationshipsOfType(String parentOid, String relationshipType, boolean deletedObject)
  {
    synchronized (parentOid)
    {
      Serializable entry = mainCache.get(parentOid);
      if (entry != null)
      {
        CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) entry;
        cachedBusinessDAOinfo.removeAllChildRelationshipsOfType(relationshipType);

        if (cachedBusinessDAOinfo.removeFromGlobalCache())
        {
          if (deletedObject)
          {
            cachedBusinessDAOinfo.setMarkedForDelete();
            mainCache.put(parentOid, entry);
          }
          mainCache.remove(parentOid);
        }
        else
        {
          if (!cachedBusinessDAOinfo.isMarkedForDelete())
          {
            mainCache.put(parentOid, entry);
          }
        }
      }
    }
  }

  /**
   * Returns the {@link EntityDAOIF} from the cache with the given oid or null if the object with the given oid is not in the cache.
   * 
   * @param oid
   * @return {@link EntityDAOIF} from the cache with the given oid or null if the object with the given oid is not in the cache.
   */
  public EntityDAOIF getEntityDAOIFfromCache(String oid)
  {
    synchronized (oid)
    {
      EntityDAOIF entityDAOIF = null;

      Serializable entry = mainCache.get(oid);

      if (entry != null)
      {
        CachedEntityDAOinfo cachedEntityDAOinfo = (CachedEntityDAOinfo) entry;

        if (!cachedEntityDAOinfo.isMarkedForDelete())
        {
          entityDAOIF = cachedEntityDAOinfo.getEntityDAOIF();
        }
      }

      return entityDAOIF;
    }
  }

  /**
   * Puts the given {@link EntityDAOIF} into the global cache.
   * 
   * @param entityDAOIF
   *          {@link EntityDAOIF} that goes into the the global cache.
   */
  public void putEntityDAOIFintoCache(EntityDAOIF entityDAOIF)
  {
    synchronized (entityDAOIF.getOid())
    {
      CachedEntityDAOinfo entry = getCachedEntityDAOinfo(true, EntityDAO.getOldId(entityDAOIF), entityDAOIF.getOid(), entityDAOIF);

      if (!entry.isMarkedForDelete())
      {
        entry.addEntityDAOIF(entityDAOIF);
        // String putId = entityDAOIF.getOid();

        mainCache.put(entityDAOIF.getOid(), entry);
      }
    }
  }

  /**
   * Removes the item from the cache with the given key
   * 
   * @param entityDAOIFid
   *          key of the item to be removed from the cache.
   * @param deletedObject
   *          indicates the object is being deleted from the application.
   */
  public void removeEntityDAOIFfromCache(String oid, boolean deletedObject)
  {
    synchronized (oid)
    {
      Serializable entry = mainCache.get(oid);

      if (entry != null)
      {
        CachedEntityDAOinfo cachedEntityDAOinfo = (CachedEntityDAOinfo) entry;
        cachedEntityDAOinfo.removeEntityDAOIF();

        if (cachedEntityDAOinfo.removeFromGlobalCache())
        {
          if (deletedObject)
          {
            cachedEntityDAOinfo.setMarkedForDelete();
            mainCache.put(oid, entry);
          }
          mainCache.remove(oid);
        }
        else
        {
          if (!cachedEntityDAOinfo.isMarkedForDelete())
          {
            mainCache.put(oid, entry);
          }
        }
      }
      // else do nothing
    }
  }

  private void setCacheStatus(Serializable status)
  {
    mainCache.put(CACHE_STATUS_KEY, status);
  }

  public boolean isCacheShutdown()
  {
    Serializable element = mainCache.get(CACHE_STATUS_KEY);

    if (element != null && element instanceof ShutdownCacheMarker)
    {
      return true;
    }

    return false;
  }

  /**
   * Persists the collections to the cache so that it can be persisted to the disk store.
   * 
   * @param collectionMap
   */
  public void backupCollectionClasses(Map<String, CacheStrategy> collectionMap)
  {
    mainCache.put(COLLECTION_MAP_KEY, (Serializable) collectionMap);
  }

  /**
   * Returns the collections from the cache.
   * 
   */
  @SuppressWarnings("unchecked")
  public Map<String, CacheStrategy> restoreCollectionClasses()
  {
    Serializable element = mainCache.get(COLLECTION_MAP_KEY);

    if (element != null)
    {
      return (Map<String, CacheStrategy>) element;
    }
    else
    {
      return null;
    }
  }

  /**
   * Removes the set of collection classes from the cache.
   * 
   */
  public void removeCollectionClasses()
  {
    mainCache.remove(COLLECTION_MAP_KEY);
  }

  private synchronized CachedEntityDAOinfo getCachedEntityDAOinfo(boolean createIfNotExists, String oldId, String newId, EntityDAOIF entityDAOIF)
  {
    if (entityDAOIF instanceof BusinessDAOIF)
    {
      return getCachedEntityDAOinfo(createIfNotExists, oldId, newId, CachedEntityDAOinfo.Types.BUSINESS);
    }
    else
    {
      return getCachedEntityDAOinfo(createIfNotExists, oldId, newId, CachedEntityDAOinfo.Types.ENTITY);
    }
  }

  private synchronized CachedEntityDAOinfo getCachedEntityDAOinfo(boolean createIfNotExists, String oldId, String newId, CachedEntityDAOinfo.Types infoType)
  {
    CachedEntityDAOinfo entry = null;

    if (oldId != null && !oldId.trim().equals(""))
    {
      entry = (CachedEntityDAOinfo) mainCache.get(oldId);

      if (entry != null)
      {
        // Create a blank placeholder object and mark it for delete. This way we know the object
        // at the index of the old OID is not supposed to be there and is only there now because
        // the cache has not yet been flushed.
        CachedEntityDAOinfo placeholderCachedEntityDAOinfo = infoType.createInfo();
        placeholderCachedEntityDAOinfo.setMarkedForDelete();

        this.mainCache.put(oldId, placeholderCachedEntityDAOinfo);

        // Now tell the cache it should be removed
        this.mainCache.remove(oldId);
      }
    }
    entry = (CachedEntityDAOinfo) mainCache.get(newId);

    if (entry == null)
    {
      if (createIfNotExists)
      {
        return infoType.createInfo();
      }
    }
    else
    {
      // A record exists in the cache only because it has not been flushed. It should not be there, so
      // we are creating a brand new one. It is up to the client to determine what to do with this record,
      // whether to add it to the cache or not.
      if (entry.isMarkedForDelete())
      {
        return infoType.createInfo();
      }
    }

    return entry;
  }
}
