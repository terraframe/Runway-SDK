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
package com.runwaysdk.dataaccess.cache.globalcache.ehcache;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.Status;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.DiskStoreConfiguration;

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

  private Ehcache             mainCache;

  private static final String COLLECTION_MAP_KEY = "COLLECTION_MAP_KEY";

  private String              cacheName;

  private String              cacheFileName;

  private String              cacheFileLocation;

  private int                 cacheMemorySize;

  public Diskstore(String _cacheName, String _cacheFileLocation, int _cacheMemorySize)
  {
    this.cacheName = _cacheName;
    this.cacheFileName = this.cacheName + ".data";
    this.cacheFileLocation = _cacheFileLocation;
    this.cacheMemorySize = _cacheMemorySize;

    this.manager = this.initializeManager();

    this.configureCache();

    this.initializeCache();

  }

  private CacheManager initializeManager()
  {
    DiskStoreConfiguration diskStoreConfiguration = new DiskStoreConfiguration();
    diskStoreConfiguration.setPath(cacheFileLocation);

    Configuration configuration = new Configuration();
    configuration.diskStore(diskStoreConfiguration);

    return new CacheManager(configuration);
  }

  private void configureCache()
  {
    CacheConfiguration cacheConfiguration = new CacheConfiguration(this.cacheName, this.cacheMemorySize);
   
//    CacheConfiguration cacheConfiguration = new CacheConfiguration();
//    cacheConfiguration.setName(this.cacheName);
//    cacheConfiguration.setMaxBytesLocalHeap("1000M");

    cacheConfiguration.setEternal(true);
    cacheConfiguration.setDiskPersistent(true);
    cacheConfiguration.overflowToDisk(true);
    cacheConfiguration.setMaxElementsOnDisk(0);
    cacheConfiguration.setStatistics(ServerProperties.getGlobalCacheStats());

    // cacheConfiguration.setTransactionalMode(TransactionalMode.LOCAL.toString());
    // make sure to add a longer default.
    // transactionController.setDefaultTransactionTimeout(int
    // defaultTransactionTimeoutSeconds
    // transactionController.begin(int transactionTimeoutSeconds)

    // CacheWriterConfiguration cacheWriterConfiguration = new
    // CacheWriterConfiguration();
    // cacheWriterConfiguration.writeMode(WriteMode.WRITE_BEHIND);
    // cacheWriterConfiguration.setWriteBatching(true);
    // cacheWriterConfiguration.setWriteCoalescing(true);
    // cacheConfiguration.cacheWriter(cacheWriterConfiguration);

    this.mainCache = new Cache(cacheConfiguration);

    this.manager.addCache(mainCache);
  }

  public void initializeCache()
  {
    if (!isCacheManagerInitialized())
    {
      this.manager = initializeManager();
    }

    if (!isCacheInitialized())
    {
      // delete old disk store.
      File file = new File(this.cacheFileLocation + File.separatorChar + this.cacheFileName);
      if (file.exists())
      {
        file.delete();
      }

      // Initialize the cache
      this.manager.removeCache(this.cacheName);

      configureCache();
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
   * Initializes the global cache.
   */
  public void removeAll()
  {
    mainCache.removeAll();  
  }
  
  /**
   * Returns the number of items stored in the cache.
   */
  public int cacheSize()
  {
    return mainCache.getSize();
  }

  /**
   * Shuts down the cache and flushes memory contents to a persistent store.
   */
  public void shutdown()
  {
    manager.shutdown();
  }

  /**
   * Returns true if the cache is initialized, false otherwise.
   */
  private boolean isCacheManagerInitialized()
  {
    boolean initialized = false;

    try
    {
      this.manager.getEhcache(this.cacheName).getStatus();
      initialized = true;
    }
    catch (IllegalStateException e)
    {
      initialized = false;
    }

    return initialized;
  }

  /**
   * Returns true if the cache is initialized, false otherwise.
   */
  public boolean isCacheInitialized()
  {
    boolean initialized = false;

    Ehcache echache = this.manager.getEhcache(this.cacheName);

    if (echache != null)
    {
      Status status = echache.getStatus();

      if (status.equals(Status.STATUS_ALIVE) && mainCache.getDiskStoreSize() > 0)
      {
        initialized = true;
      }
    }
    return initialized;
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

      Element element = mainCache.get(id);

      if (element == null)
      {
        returnList = new LinkedList<RelationshipDAOIF>();
      }
      else
      {
        CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) element.getObjectValue();

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

      Element element = mainCache.get(id);

      if (element == null)
      {
        returnList = new LinkedList<RelationshipDAOIF>();
      }
      else
      {
        CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) element.getObjectValue();

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
    synchronized (relationshipDAOIF.getParentId())
    {
      Element element = getCachedEntityDAOinfo(true, RelationshipDAO.getOldParentId(relationshipDAOIF), relationshipDAOIF.getParentId(), CachedEntityDAOinfo.Types.BUSINESS);
      CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) element.getObjectValue();
       
      if (!cachedBusinessDAOinfo.isMarkedForDelete())
      {
        cachedBusinessDAOinfo.addChildRelationship(relationshipDAOIF);
        mainCache.put(element);
      }
      
// Heads up: test
//      CachedBusinessDAOinfo cachedBusinessDAOinfo;
//      Element element = mainCache.get(relationshipDAOIF.getParentId());
//      if (element == null)
//      {
//        cachedBusinessDAOinfo = new CachedBusinessDAOinfo();
//        element = new Element(relationshipDAOIF.getParentId(), cachedBusinessDAOinfo);
//      }
//      else
//      {
//        cachedBusinessDAOinfo = (CachedBusinessDAOinfo) element.getObjectValue();
//      }
//
//      if (!cachedBusinessDAOinfo.isMarkedForDelete())
//      {
//        cachedBusinessDAOinfo.addChildRelationship(relationshipDAOIF);
//        mainCache.put(element);
//      }
    }

    synchronized (relationshipDAOIF.getChildId())
    {
      
      Element element = getCachedEntityDAOinfo(true, RelationshipDAO.getOldChildId(relationshipDAOIF), relationshipDAOIF.getChildId(), CachedEntityDAOinfo.Types.BUSINESS);
      CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) element.getObjectValue();
       
      if (!cachedBusinessDAOinfo.isMarkedForDelete())
      {
        cachedBusinessDAOinfo.addParentRelationship(relationshipDAOIF);
        mainCache.put(element);
      }
      
// Heads up: test
//      CachedBusinessDAOinfo cachedBusinessDAOinfo;
//      Element element = mainCache.get(relationshipDAOIF.getChildId());
//      if (element == null)
//      {
//        cachedBusinessDAOinfo = new CachedBusinessDAOinfo();
//        element = new Element(relationshipDAOIF.getChildId(), cachedBusinessDAOinfo);
//
//      }
//      else
//      {
//        cachedBusinessDAOinfo = (CachedBusinessDAOinfo) element.getObjectValue();
//      }
//
//      if (!cachedBusinessDAOinfo.isMarkedForDelete())
//      {
//        cachedBusinessDAOinfo.addParentRelationship(relationshipDAOIF);
//        mainCache.put(element);
//      }
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
    
    synchronized (relationshipDAO.getParentId())
    {
      Element element = getCachedEntityDAOinfo(true, RelationshipDAO.getOldParentId(relationshipDAOIF), relationshipDAOIF.getParentId(), CachedEntityDAOinfo.Types.BUSINESS);
      CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) element.getObjectValue();
       
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
        mainCache.put(element);
      }
     
      
// Heads up: test
//      Element element;
//      CachedBusinessDAOinfo cachedBusinessDAOinfo;
//      
//      element = mainCache.get(relationshipDAO.getParentId());     
//      
//      if (element == null)
//      {
//        cachedBusinessDAOinfo = new CachedBusinessDAOinfo();
//        element = new Element(relationshipDAO.getParentId(), cachedBusinessDAOinfo);
//      }
//      else
//      {
//        cachedBusinessDAOinfo = (CachedBusinessDAOinfo) element.getObjectValue();
//      }
//
//      if (!cachedBusinessDAOinfo.isMarkedForDelete())
//      {
//        if (hasIdChanged)
//        {
//          cachedBusinessDAOinfo.updateChildRelationship(relationshipDAO);
//        }
//        else
//        {
//          cachedBusinessDAOinfo.addChildRelationship(relationshipDAO);
//        }
//        mainCache.put(element);
//      }
    }
    
    synchronized (relationshipDAO.getChildId())
    {
      Element element = getCachedEntityDAOinfo(true, RelationshipDAO.getOldChildId(relationshipDAOIF), relationshipDAOIF.getChildId(), CachedEntityDAOinfo.Types.BUSINESS);
      CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) element.getObjectValue();
      
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
        mainCache.put(element);
      }    

      
// Heads up: test      
//      Element element;
//      CachedBusinessDAOinfo cachedBusinessDAOinfo;
//      
//      element = mainCache.get(relationshipDAO.getChildId());
//      
//      if (element == null)
//      {
//        cachedBusinessDAOinfo = new CachedBusinessDAOinfo();
//        element = new Element(relationshipDAO.getChildId(), cachedBusinessDAOinfo);
//
//      }
//      else
//      {
//        cachedBusinessDAOinfo = (CachedBusinessDAOinfo) element.getObjectValue();
//      }
//
//      
//      if (!cachedBusinessDAOinfo.isMarkedForDelete())
//      {
//        if (hasIdChanged)
//        {
//          cachedBusinessDAOinfo.updateParentRelationship(relationshipDAO);
//        }
//        else
//        {
//          cachedBusinessDAOinfo.addParentRelationship(relationshipDAO);
//        }
//
//        mainCache.put(element);
//      }
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
      Element element = getCachedEntityDAOinfo(true, EntityDAO.getOldId(entityDAOIF), entityDAOIF.getId(), entityDAOIF);
      CachedEntityDAOinfo cachedEntityDAOinfo = (CachedEntityDAOinfo) element.getObjectValue();

      if (element != null)
      {
        cachedEntityDAOinfo = (CachedEntityDAOinfo) element.getObjectValue();  
        cachedEntityDAOinfo.addEntityDAOIF(entityDAOIF);
        element = new Element(entityDAOIF.getId(), cachedEntityDAOinfo);
        if (!cachedEntityDAOinfo.isMarkedForDelete())
        {
          mainCache.put(element);
        }        
      }

      
// Heads up: test
//      CachedEntityDAOinfo cachedEntityDAOinfo;
//      Element element = mainCache.get(oldEntityId);
//      if (element != null)
//      {
//        cachedEntityDAOinfo = (CachedEntityDAOinfo) element.getObjectValue();        
//        mainCache.remove(oldEntityId);
//        cachedEntityDAOinfo.addEntityDAOIF(entityDAOIF);
//        element = new Element(entityDAOIF.getId(), cachedEntityDAOinfo);
//        if (!cachedEntityDAOinfo.isMarkedForDelete())
//        {
//          mainCache.put(element);
//        }
//      }
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
    synchronized (relationshipDAO.getChildId())
    {
      boolean stillHasParents = false;

      Element element = mainCache.get(relationshipDAO.getChildId());
      if (element != null)
      {
        CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) element.getObjectValue();
        stillHasParents = cachedBusinessDAOinfo.removeParentRelationship(relationshipDAO);

        if (cachedBusinessDAOinfo.removeFromGlobalCache())
        {
          if (deletedObject)
          {
            cachedBusinessDAOinfo.setMarkedForDelete();
            mainCache.put(element);
          }

          mainCache.remove(element);
        }
        else
        {
          if (!cachedBusinessDAOinfo.isMarkedForDelete())
          {
            mainCache.put(element);
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
      Element element = mainCache.get(childId);
      if (element != null)
      {
        CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) element.getObjectValue();
        cachedBusinessDAOinfo.removeAllParentRelationshipsOfType(relationshipType);

        if (cachedBusinessDAOinfo.removeFromGlobalCache())
        {
          if (deletedObject)
          {
            cachedBusinessDAOinfo.setMarkedForDelete();
            mainCache.put(element);
          }

          mainCache.remove(element);
        }
        else
        {
          if (!cachedBusinessDAOinfo.isMarkedForDelete())
          {
            mainCache.put(element);
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
    synchronized (relationshipDAOIF.getParentId())
    {
      boolean stillHasChildren = false;

      Element element = mainCache.get(relationshipDAOIF.getParentId());
      if (element != null)
      {
        CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) element.getObjectValue();
        stillHasChildren = cachedBusinessDAOinfo.removeChildRelationship(relationshipDAOIF);

        if (cachedBusinessDAOinfo.removeFromGlobalCache())
        {
          if (deletedObject)
          {
            cachedBusinessDAOinfo.setMarkedForDelete();
            mainCache.put(element);
          }
          mainCache.remove(element);
        }
        else
        {
          if (!cachedBusinessDAOinfo.isMarkedForDelete())
          {
            mainCache.put(element);
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
      Element element = mainCache.get(parentId);
      if (element != null)
      {
        CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) element.getObjectValue();
        cachedBusinessDAOinfo.removeAllChildRelationshipsOfType(relationshipType);

        if (cachedBusinessDAOinfo.removeFromGlobalCache())
        {
          if (deletedObject)
          {
            cachedBusinessDAOinfo.setMarkedForDelete();
            mainCache.put(element);
          }
          mainCache.remove(element);
        }
        else
        {
          if (!cachedBusinessDAOinfo.isMarkedForDelete())
          {
            mainCache.put(element);
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

      Element element = mainCache.get(id);

      if (element != null)
      {
        CachedEntityDAOinfo cachedEntityDAOinfo = (CachedEntityDAOinfo) element.getObjectValue();

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
      Element element = getCachedEntityDAOinfo(true, EntityDAO.getOldId(entityDAOIF), entityDAOIF.getId(), entityDAOIF);
      CachedEntityDAOinfo cachedEntityDAOinfo = (CachedEntityDAOinfo) element.getObjectValue();
      
      if (!cachedEntityDAOinfo.isMarkedForDelete())
      {
        cachedEntityDAOinfo.addEntityDAOIF(entityDAOIF);
        mainCache.put(element);
      }
      
// Heads up: test      
//      Element element = mainCache.get(entityDAOIF.getId());
//
//      CachedEntityDAOinfo cachedEntityDAOinfo;
//
//      if (element == null)
//      {
//        // Cast is OK because we are not modifying the state of the object.
//        cachedEntityDAOinfo = ( (EntityDAO) entityDAOIF ).createGlobalCacheWrapper();
//        element = new Element(entityDAOIF.getId(), cachedEntityDAOinfo);
//      }
//      else
//      {
//        cachedEntityDAOinfo = (CachedEntityDAOinfo) element.getObjectValue();
//      }
//
//      // If, for whatever reason, the object has been marked for deletion, we do
//      // not want to add it back into the cache, or else we risk cache memory
//      // leaks.
//      if (!cachedEntityDAOinfo.isMarkedForDelete())
//      {
//        cachedEntityDAOinfo.addEntityDAOIF(entityDAOIF);
//        mainCache.put(element);
//      }
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
      Element element = mainCache.get(id);

      if (element != null)
      {
        CachedEntityDAOinfo cachedEntityDAOinfo = (CachedEntityDAOinfo) element.getObjectValue();
        cachedEntityDAOinfo.removeEntityDAOIF();

        if (cachedEntityDAOinfo.removeFromGlobalCache())
        {
          if (deletedObject)
          {
            cachedEntityDAOinfo.setMarkedForDelete();
            mainCache.put(element);
          }
          mainCache.remove(id);
        }
        else
        {
          if (!cachedEntityDAOinfo.isMarkedForDelete())
          {
            mainCache.put(element);
          }
        }
      }
      // else do nothing
    }
  }

  /**
   * Returns a {@link Set} of the keys that are in the cache. The keys may or
   * may not represent {@link EntityDAOIF} ids.
   * 
   * @return {@link Set} of the keys that are in the cache.
   */
  @SuppressWarnings( { "unchecked" })
  public Set<String> getCacheKeys()
  {
    return new HashSet<String>(mainCache.getKeys());
  }

  /**
   * Persists the collections to the cache so that it can be persisted to the
   * disk store.
   * 
   * @param collectionMap
   */
  public void backupCollectionClasses(Map<String, CacheStrategy> collectionMap)
  {
    Element element = new Element(COLLECTION_MAP_KEY, collectionMap);
    mainCache.put(element);
  }

  /**
   * Returns the collections from the cache.
   * 
   */
  @SuppressWarnings("unchecked")
  public Map<String, CacheStrategy> restoreCollectionClasses()
  {
    Element element = mainCache.get(COLLECTION_MAP_KEY);

    if (element != null)
    {
      return (Map<String, CacheStrategy>) element.getObjectValue();
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

  /**
   * Starts transaction for the current thread.
   */
  public void beginTransaction()
  {
    manager.getTransactionController().begin();
  }

  /**
   * Commits transaction for the current thread.
   */
  public void commitTransaction()
  {
    manager.getTransactionController().commit();
  }

  /**
   * Rollback transaction for the current thread.
   */
  public void rollbackTransaction()
  {
    manager.getTransactionController().rollback();
  }

  public void flush()
  {
    mainCache.flush();
  }
  
  private synchronized Element getCachedEntityDAOinfo(boolean createIfNotExists, String oldId, String newId, EntityDAOIF entityDAOIF)
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
  
  private synchronized Element getCachedEntityDAOinfo(boolean createIfNotExists, String oldId, String newId, CachedEntityDAOinfo.Types infoType)
  {
    Element element = null;
    
    if (oldId != null && !oldId.trim().equals(""))
    {
      element = mainCache.get(oldId);
            
      if (element != null)
      {
        // If an item with the old id exists in the cache, remove it under the old id
        // and replace it with the new one.
        this.mainCache.remove(oldId);
        CachedEntityDAOinfo cachedEntityDAOinfo = (CachedEntityDAOinfo) element.getObjectValue();  
        element = new Element(newId, cachedEntityDAOinfo);
        this.mainCache.put(element);
      }
    }

    if (element == null)
    { 
      element = mainCache.get(newId); 
    }
       
    if (element == null && createIfNotExists)
    {
      CachedEntityDAOinfo cachedEntityDAOinfo = infoType.createInfo();
      element = new Element(newId, cachedEntityDAOinfo);
    }
        
    return element;
  }
}
