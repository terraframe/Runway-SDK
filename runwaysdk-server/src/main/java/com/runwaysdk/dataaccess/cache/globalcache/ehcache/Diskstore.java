package com.runwaysdk.dataaccess.cache.globalcache.ehcache;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.ehcache.Cache;
import org.ehcache.CacheManagerBuilder;
import org.ehcache.PersistentCacheManager;
import org.ehcache.Status;
import org.ehcache.UserManagedCache;
import org.ehcache.config.CacheConfigurationBuilder;
import org.ehcache.config.ResourcePoolsBuilder;
import org.ehcache.config.persistence.CacheManagerPersistenceConfiguration;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;

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
  private static final String       COLLECTION_MAP_KEY = "COLLECTION_MAP_KEY";

  private PersistentCacheManager    manager;

  private Cache<String, CacheEntry> mainCache;

  private String                    cacheName;

  private String                    cacheFileName;

  private String                    cacheFileLocation;

  private Integer                   cacheMemorySize;

  private Integer                   offheapSize;

  public Diskstore(String _cacheName, String _cacheFileLocation, Integer _cacheMemorySize, Integer _offheapSize)
  {
    this.cacheName = _cacheName;
    this.cacheFileName = this.cacheName + ".data";
    this.cacheFileLocation = _cacheFileLocation;
    this.cacheMemorySize = _cacheMemorySize;
    this.offheapSize = _offheapSize;

    this.setupCache();
  }

  private synchronized PersistentCacheManager getCacheManager()
  {
    if (this.manager == null)
    {
      this.manager = CacheManagerBuilder.newCacheManagerBuilder().with(new CacheManagerPersistenceConfiguration(new File(this.cacheFileLocation, this.cacheName))).build(true);
    }

    return this.manager;
  }

  private void setupCache()
  {
    try
    {
      this.configureCache();

      if (!this.isCacheInitialized())
      {
        // Cache ins't usable, clear it out
        this.removeAll();
      }
    }
    catch (IllegalStateException e)
    {
      // Cache is corrupt. Delete the files
      this.deleteCacheFiles();
    }
  }

  public void initializeCache()
  {
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
    }
  }

  private void deleteCacheFiles()
  {
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
    poolsBuilder.heap(cacheMemorySize, EntryUnit.ENTRIES);
    poolsBuilder.disk(diskSize, MemoryUnit.MB, true);

    if (this.offheapSize != null)
    {
      poolsBuilder.offheap(offheapSize, MemoryUnit.MB);
    }

    CacheConfigurationBuilder<Object, Object> cacheBuilder = CacheConfigurationBuilder.newCacheConfigurationBuilder();
    cacheBuilder.withResourcePools(poolsBuilder);

    this.mainCache = this.getCacheManager().createCache(this.cacheName, cacheBuilder.buildConfig(String.class, CacheEntry.class));
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
   * Destroys this cache in one atmoic operation and leaves it in a state of (UNINITIALIZED). You have to call initializeCache after calling this method if you want to continue using the cache.
   */
  public synchronized void removeAll()
  {
    try
    {
      if (this.manager != null)
      {
        this.manager.close();
        this.manager.toMaintenance().destroy();

        this.manager = null;
        this.mainCache = null;
      }
    }
    catch (Throwable t)
    {
      System.out.println("Encountered exception while destroying cache.");
      t.printStackTrace();
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
    this.manager.close();

    this.manager = null;
    this.mainCache = null;
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
        UserManagedCache<String, CacheEntry> cache = (UserManagedCache<String, CacheEntry>) this.getCacheManager().getCache(this.cacheName, String.class, CacheEntry.class);

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
   * Returns a list of parent relationships of the given type from the cache for a {@link BusinessDAOIF} with the given id.
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
   * Returns a list of child relationships of the given type from the cache for a {@link BusinessDAOIF} with the given id.
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
   * Adds the {@link RelationshipDAOIF} to the parent and child relationships of the parent and child objects in the cache.
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
   * Updates the stored id if it has changed for the {@link RelationshipDAOIF} to the parent and child relationships of the parent and child objects in the cache.
   * 
   * @param hasIdChanged
   * @param relationshipDAOIF
   */
  public void updateRelationshipDAOIFinCache(Boolean hasIdChanged, RelationshipDAOIF relationshipDAOIF)
  {
    RelationshipDAO relationshipDAO = (RelationshipDAO) relationshipDAOIF;

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
   * <br/>
   * <b>Precondition:</b> Calling method has checked whether the id has changed.
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
   * Removes all parent relationships of the given type for the {@link BusinessDAOIF} with the given id.
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
   * Removes the {@link RelationshipDAOIF} from the child relationship of the parent object in the cache.
   * 
   * @param relationshipDAOIF
   * @param deletedObject
   *          indicates the object is being deleted from the application.
   * @return True if the parent object still has children relationships of this type.
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
   * Removes all child relationships of the given type for the {@link BusinessDAOIF} with the given id.
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
   * Returns the {@link EntityDAOIF} from the cache with the given id or null if the object with the given id is not in the cache.
   * 
   * @param id
   * @return {@link EntityDAOIF} from the cache with the given id or null if the object with the given id is not in the cache.
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
        // String putId = entityDAOIF.getId();

        mainCache.put(entityDAOIF.getId(), entry);

        // String getId = entityDAOIF.getId();
        CacheEntry entry2 = mainCache.get(entityDAOIF.getId());
        //
        if (entry2 == null)
        {
          cachedEntityDAOinfo.addEntityDAOIF(null);
          mainCache.put(entityDAOIF.getId(), entry);

          CacheEntry entry3 = mainCache.get(entityDAOIF.getId());
          // Heads up: Test
          System.out.println("Heads up: remove: " + entry3 + " " + entityDAOIF.getType() + " " + entityDAOIF.getId());
          // int i = 1;
          //
          // // if (entry3 != null)
          // // {
          // // CachedEntityDAOinfo cachedEntityDAOinfo3 = (CachedEntityDAOinfo) entry3.getEntityDAO();
          // // cachedEntityDAOinfo3.addEntityDAOIF(entityDAOIF);
          // // mainCache.put(entityDAOIF.getId(), entry3);
          // //
          // // CacheEntry entry4 = mainCache.get(entityDAOIF.getId());
          // // }
        }

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
   * Persists the collections to the cache so that it can be persisted to the disk store.
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
        // Heads up: remove?
        // if (!cachedEntityDAOinfo.isMarkedForDelete())
        // {
        // this.mainCache.put(element);
        // }
      }
    }
    // Heads up: test
    // if (entry == null)
    // {
    entry = mainCache.get(newId);
    // }

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
