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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.CacheManagerBuilder;
import org.ehcache.Status;
import org.ehcache.config.CacheConfigurationBuilder;
import org.ehcache.config.ResourcePoolsBuilder;
import org.ehcache.config.persistence.CacheManagerPersistenceConfiguration;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;

import com.runwaysdk.constants.ServerProperties;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.cache.CacheStrategy;
import com.runwaysdk.dataaccess.cache.ObjectStore;

public class Diskstore implements ObjectStore
{
  private CacheManager        manager;

  private Cache<String, CacheEntry> mainCache;

  private static final String COLLECTION_MAP_KEY = "COLLECTION_MAP_KEY";

  private String              cacheName;

  private String              cacheFileName;

  private String              cacheFileLocation;

  private int                 cacheMemorySize;
  
  private Integer                 offheapSize;

  public Diskstore(String _cacheName, String _cacheFileLocation, int _cacheMemorySize, int _offheapSize)
  {
    this.cacheName = _cacheName;
    this.cacheFileName = this.cacheName + ".data";
    this.cacheFileLocation = _cacheFileLocation;
    this.cacheMemorySize = _cacheMemorySize;
    this.offheapSize = _offheapSize;

    this.manager = this.initialize();

    this.initializeCache();

  }

  private synchronized CacheManager getCacheManager()
  {
    if (manager == null)
    {
      manager = (CacheManager) CacheManagerBuilder.newCacheManagerBuilder()
          .with(new CacheManagerPersistenceConfiguration(new File(this.cacheFileLocation))) 
          .build(true);
    }

    return manager;
  }
  
  private CacheManager initialize()
  {
    // TODO: Yeah, I realize this is redundant, but I'm under a timecrunch here.
    CacheManager _manager = getCacheManager();
    if (this.offheapSize == null)
    {
      Integer diskSize = ServerProperties.getGlobalCacheDiskSize();
      if (diskSize == null)
      {
        diskSize = 5000; // 5GB
      }
      
      this.mainCache = getCacheManager().createCache(this.cacheName,
          CacheConfigurationBuilder.newCacheConfigurationBuilder()
            .withResourcePools(ResourcePoolsBuilder.newResourcePoolsBuilder() 
                    .heap(cacheMemorySize, EntryUnit.ENTRIES)
//                    .offheap(cacheMemorySize, MemoryUnit.MB)
                    .disk(10, MemoryUnit.MB, true) // TODO : Should the user be able to configure this?
            )
            .buildConfig(String.class, CacheEntry.class));
    }
    else
    {
      this.mainCache = getCacheManager().createCache(this.cacheName,
          CacheConfigurationBuilder.newCacheConfigurationBuilder()
            .withResourcePools(ResourcePoolsBuilder.newResourcePoolsBuilder() 
                    .heap(cacheMemorySize, EntryUnit.ENTRIES)
                    .offheap(offheapSize, MemoryUnit.MB)
                    .disk(1, MemoryUnit.TB, true) // TODO : Should the user be able to configure this?
            )
            .buildConfig(String.class, CacheEntry.class));
    }
    
    return _manager;
  }

  public void initializeCache()
  {
    if (!isCacheManagerInitialized())
    {
      this.manager = initialize();
    }
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

  public String getCacheFileName()
  {
    return this.cacheFileName;
  }

  /**
   * Returns true if the cache contains the provided key (entity id).
   */
  public boolean containsKey(String key)
  {
    return this.mainCache.containsKey(key);
  }
  
  /**
   * Initializes the global cache.
   */
  public void removeAll()
  {
    mainCache.clear();  
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
    manager.close();
  }

  /**
   * Returns true if the cache is initialized, false otherwise.
   */
  @SuppressWarnings("rawtypes")
  private boolean isCacheManagerInitialized()
  {
    return this.mainCache != null && !(((org.ehcache.Ehcache)this.mainCache).getStatus().equals(Status.UNINITIALIZED));
  }

  /**
   * Returns true if the cache is initialized, false otherwise.
   */
  public boolean isCacheInitialized()
  {
//    boolean initialized = false;
//
//    Ehcache echache = this.manager.getEhcache(this.cacheName);
//
//    if (echache != null)
//    {
//      Status status = echache.getStatus();
//
//      if (status.equals(Status.STATUS_ALIVE) && mainCache.getDiskStoreSize() > 0)
//      {
//        initialized = true;
//      }
//    }
//    return initialized;
    
    return this.isCacheManagerInitialized();
  }

  /**
   * Returns a list of parent relationships of the given type from the cache for
   * a {@link BusinessDAOIF} with the given id.
   * 
   * @param id
   * @param relationshipType
   * @return
   */
  public List<RelationshipDAOIF> getParentRelationshipsFromCache(String id, String relationshipType)
  {
    synchronized (id)
    {
      List<RelationshipDAOIF> returnList = null;

      CacheEntry entry = mainCache.get(id);

      if (entry == null)
      {
        returnList = new LinkedList<RelationshipDAOIF>();
      }
      else
      {
        CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) entry.getEntityDAO(); 
        
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
   * Returns a list of child relationships of the given type from the cache for
   * a {@link BusinessDAOIF} with the given id.
   * 
   * @param id
   * @param relationshipType
   * 
   * @return
   */
  public List<RelationshipDAOIF> getChildRelationshipsFromCache(String id, String relationshipType)
  {
    synchronized (id)
    {
      List<RelationshipDAOIF> returnList = null;

      CacheEntry entry = mainCache.get(id);

      if (entry == null)
      {
        returnList = new LinkedList<RelationshipDAOIF>();
      }
      else
      {
        CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) entry.getEntityDAO();
        
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
   * Adds the {@link RelationshipDAOIF} to the parent and child relationships of
   * the parent and child objects in the cache.
   * 
   * @param relationshipDAOIF
   */
  public void addRelationshipDAOIFtoCache(RelationshipDAOIF relationshipDAOIF)
  {
    String parentId = relationshipDAOIF.getParentId();
    synchronized (parentId)
    {
      CacheEntry entry = getCachedEntityDAOinfo(true, RelationshipDAO.getOldParentId(relationshipDAOIF), parentId, CachedEntityDAOinfo.Types.BUSINESS);
      CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) entry.getEntityDAO();
       
      if (!cachedBusinessDAOinfo.isMarkedForDelete())
      {
        cachedBusinessDAOinfo.addChildRelationship(relationshipDAOIF);
        mainCache.put(parentId, entry);
      }
    }

    String childId = relationshipDAOIF.getChildId();
    synchronized (childId)
    {
      CacheEntry entry = getCachedEntityDAOinfo(true, RelationshipDAO.getOldChildId(relationshipDAOIF), childId, CachedEntityDAOinfo.Types.BUSINESS);
      CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) entry.getEntityDAO();
       
      if (!cachedBusinessDAOinfo.isMarkedForDelete())
      {
        cachedBusinessDAOinfo.addParentRelationship(relationshipDAOIF);
        mainCache.put(childId, entry);
      }
    }
  }

  /**
   * Updates the stored id if it has changed for the {@link RelationshipDAOIF} to the 
   * parent and child relationships of the parent and child objects in the cache.
   * 
   * @param hasIdChanged
   * @param relationshipDAOIF
   */
  public void updateRelationshipDAOIFinCache(Boolean hasIdChanged, RelationshipDAOIF relationshipDAOIF)
  {
    RelationshipDAO relationshipDAO = (RelationshipDAO)relationshipDAOIF;
    
    String parentId = relationshipDAO.getParentId();
    synchronized (parentId)
    {
      CacheEntry entry = getCachedEntityDAOinfo(true, RelationshipDAO.getOldParentId(relationshipDAOIF), parentId, CachedEntityDAOinfo.Types.BUSINESS);
      CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) entry.getEntityDAO();
       
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
        mainCache.put(parentId, entry);
      }
    }
    
    String childId = relationshipDAO.getChildId();
    synchronized (childId)
    {
      CacheEntry entry = getCachedEntityDAOinfo(true, RelationshipDAO.getOldChildId(relationshipDAOIF), childId, CachedEntityDAOinfo.Types.BUSINESS);
      CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) entry.getEntityDAO();
      
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
        mainCache.put(childId, entry);
      }    
    }
  }
  
  /**
   * Updates the changed id for the given {@link EntityDAOIF} in the cache.
   * 
   * <br/><b>Precondition:</b> Calling method has checked whether the id has changed.
   * 
   * @param oldEntityId
   * @param entityDAOIF
   */
  public void updateIdEntityDAOIFinCache(String oldEntityId, EntityDAOIF entityDAOIF)
  {
    synchronized (oldEntityId)
    {      
      CacheEntry entry = getCachedEntityDAOinfo(true, EntityDAO.getOldId(entityDAOIF), entityDAOIF.getId(), entityDAOIF);

      if (entry != null)
      {
        CachedEntityDAOinfo cachedEntityDAOinfo = entry.getEntityDAO();
        cachedEntityDAOinfo.addEntityDAOIF(entityDAOIF);
        if (!cachedEntityDAOinfo.isMarkedForDelete())
        {
          mainCache.put(entityDAOIF.getId(), entry);
        }        
      }
    }
  }

  
  /**
   * Removes the {@link RelationshipDAOIF} from the parent relationship of the
   * child object in the cache.
   * 
   * @param relationshipDAO
   * @param deletedObject
   *          indicates the object is being deleted from the application.
   * 
   * 
   * @return True if the child object still has parent relationships of this
   *         type.
   */
  public boolean removeParentRelationshipDAOIFtoCache(RelationshipDAO relationshipDAO, boolean deletedObject)
  {
    String childId = relationshipDAO.getChildId();
    synchronized (childId)
    {
      boolean stillHasParents = false;

      CacheEntry entry = mainCache.get(childId);
      if (entry != null)
      {
        CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) entry.getEntityDAO();
        stillHasParents = cachedBusinessDAOinfo.removeParentRelationship(relationshipDAO);

        if (cachedBusinessDAOinfo.removeFromGlobalCache())
        {
          if (deletedObject)
          {
            cachedBusinessDAOinfo.setMarkedForDelete();
            mainCache.put(childId, entry);
          }

          mainCache.remove(childId);
        }
        else
        {
          if (!cachedBusinessDAOinfo.isMarkedForDelete())
          {
            mainCache.put(childId, entry);
          }
        }
      }

      return stillHasParents;
    }
  }

  /**
   * Removes all parent relationships of the given type for the
   * {@link BusinessDAOIF} with the given id.
   * 
   * @param childId
   * @param relationshipType
   * @param deletedObject
   *          indicates the object is being deleted from the application.
   */
  public void removeAllParentRelationshipsOfType(String childId, String relationshipType, boolean deletedObject)
  {
    synchronized (childId)
    {
      CacheEntry entry = mainCache.get(childId);
      if (entry != null)
      {
        CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) entry.getEntityDAO();
        cachedBusinessDAOinfo.removeAllParentRelationshipsOfType(relationshipType);

        if (cachedBusinessDAOinfo.removeFromGlobalCache())
        {
          if (deletedObject)
          {
            cachedBusinessDAOinfo.setMarkedForDelete();
            mainCache.put(childId, entry);
          }

          mainCache.remove(childId);
        }
        else
        {
          if (!cachedBusinessDAOinfo.isMarkedForDelete())
          {
            mainCache.put(childId, entry);
          }
        }
      }
    }
  }

  /**
   * Removes the {@link RelationshipDAOIF} from the child relationship of the
   * parent object in the cache.
   * 
   * @param relationshipDAOIF
   * @param deletedObject
   *          indicates the object is being deleted from the application.
   * @return True if the parent object still has children relationships of this
   *         type.
   */
  public boolean removeChildRelationshipDAOIFtoCache(RelationshipDAOIF relationshipDAOIF, boolean deletedObject)
  {
    String parentId = relationshipDAOIF.getParentId();
    synchronized (parentId)
    {
      boolean stillHasChildren = false;

      CacheEntry entry = mainCache.get(parentId);
      if (entry != null)
      {
        CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) entry.getEntityDAO();
        stillHasChildren = cachedBusinessDAOinfo.removeChildRelationship(relationshipDAOIF);

        if (cachedBusinessDAOinfo.removeFromGlobalCache())
        {
          if (deletedObject)
          {
            cachedBusinessDAOinfo.setMarkedForDelete();
            mainCache.put(parentId, entry);
          }
          mainCache.remove(parentId);
        }
        else
        {
          if (!cachedBusinessDAOinfo.isMarkedForDelete())
          {
            mainCache.put(parentId, entry);
          }
        }
      }

      return stillHasChildren;
    }
  }

  /**
   * Removes all child relationships of the given type for the
   * {@link BusinessDAOIF} with the given id.
   * 
   * @param parentId
   * @param relationshipType
   * @param deletedObject
   *          indicates the object is being deleted from the application.
   */
  public void removeAllChildRelationshipsOfType(String parentId, String relationshipType, boolean deletedObject)
  {
    synchronized (parentId)
    {
      CacheEntry entry = mainCache.get(parentId);
      if (entry != null)
      {
        CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) entry.getEntityDAO(); 
        cachedBusinessDAOinfo.removeAllChildRelationshipsOfType(relationshipType);

        if (cachedBusinessDAOinfo.removeFromGlobalCache())
        {
          if (deletedObject)
          {
            cachedBusinessDAOinfo.setMarkedForDelete();
            mainCache.put(parentId, entry);
          }
          mainCache.remove(parentId);
        }
        else
        {
          if (!cachedBusinessDAOinfo.isMarkedForDelete())
          {
            mainCache.put(parentId, entry);
          }
        }
      }
    }
  }

  /**
   * Returns the {@link EntityDAOIF} from the cache with the given id or null if
   * the object with the given id is not in the cache.
   * 
   * @param id
   * @return {@link EntityDAOIF} from the cache with the given id or null if the
   *         object with the given id is not in the cache.
   */
  public EntityDAOIF getEntityDAOIFfromCache(String id)
  {
    synchronized (id)
    {
      EntityDAOIF entityDAOIF = null;

      CacheEntry entry = mainCache.get(id);

      if (entry != null)
      {
        CachedEntityDAOinfo cachedEntityDAOinfo = (CachedEntityDAOinfo) entry.getEntityDAO();
        
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
    synchronized (entityDAOIF.getId())
    {
      CacheEntry entry = getCachedEntityDAOinfo(true, EntityDAO.getOldId(entityDAOIF), entityDAOIF.getId(), entityDAOIF);
      CachedEntityDAOinfo cachedEntityDAOinfo = (CachedEntityDAOinfo) entry.getEntityDAO();
      
      if (!cachedEntityDAOinfo.isMarkedForDelete())
      {
        cachedEntityDAOinfo.addEntityDAOIF(entityDAOIF);
        mainCache.put(entityDAOIF.getId(), entry);
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
  public void removeEntityDAOIFfromCache(String id, boolean deletedObject)
  {
    synchronized (id)
    {
      CacheEntry entry = mainCache.get(id);

      if (entry != null)
      {
        CachedEntityDAOinfo cachedEntityDAOinfo = (CachedEntityDAOinfo) entry.getEntityDAO();
        cachedEntityDAOinfo.removeEntityDAOIF();

        if (cachedEntityDAOinfo.removeFromGlobalCache())
        {
          if (deletedObject)
          {
            cachedEntityDAOinfo.setMarkedForDelete();
            mainCache.put(id, entry);
          }
          mainCache.remove(id);
        }
        else
        {
          if (!cachedEntityDAOinfo.isMarkedForDelete())
          {
            mainCache.put(id, entry);
          }
        }
      }
      // else do nothing
    }
  }

  /**
   * Persists the collections to the cache so that it can be persisted to the
   * disk store.
   * 
   * @param collectionMap
   */
  public void backupCollectionClasses(Map<String, CacheStrategy> collectionMap)
  {
    CacheEntry element = new CacheEntry(collectionMap);
    mainCache.put(COLLECTION_MAP_KEY, element);
  }

  /**
   * Returns the collections from the cache.
   * 
   */
  public Map<String, CacheStrategy> restoreCollectionClasses()
  {
    CacheEntry element = mainCache.get(COLLECTION_MAP_KEY);

    if (element != null)
    {
      return element.getCollectionMap();
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
  
  private synchronized CacheEntry getCachedEntityDAOinfo(boolean createIfNotExists, String oldId, String newId, EntityDAOIF entityDAOIF)
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
  
  private synchronized CacheEntry getCachedEntityDAOinfo(boolean createIfNotExists, String oldId, String newId, CachedEntityDAOinfo.Types infoType)
  {
    CacheEntry entry = null;
    
    if (oldId != null && !oldId.trim().equals(""))
    {
      entry = mainCache.get(oldId);
            
      if (entry != null)
      {
        // Create a blank placeholder object and mark it for delete. This way we know the object
        // at the index of the old ID is not supposed to be there and is only there now because
        // the cache has not yet been flushed.
        CachedEntityDAOinfo placeholderCachedEntityDAOinfo = infoType.createInfo();
        placeholderCachedEntityDAOinfo.setMarkedForDelete();
        CacheEntry placeholderEntry = new CacheEntry(placeholderCachedEntityDAOinfo);
        this.mainCache.put(oldId, placeholderEntry);

        // Now tell the cache it should be removed
        this.mainCache.remove(oldId);

//        if (!cachedEntityDAOinfo.isMarkedForDelete())
//        {
//          this.mainCache.put(element);
//        }
      }
    }

    if (entry == null)
    { 
      entry = mainCache.get(newId); 
    }
       
    if (entry == null)
    {
      if (createIfNotExists)
      {
        entry = new CacheEntry(infoType.createInfo());
      }
    }
    else
    {
      // A record exists in the cache only because it has not been flushed. It should not be there, so 
      // we are creating a brand new one. It is up to the client to determine what to do with this record,
      // whether to add it to the cache or not.
      if (entry.getEntityDAO().isMarkedForDelete())
      {
        entry = new CacheEntry(infoType.createInfo());
      }
    }

    return entry;
  }
}
