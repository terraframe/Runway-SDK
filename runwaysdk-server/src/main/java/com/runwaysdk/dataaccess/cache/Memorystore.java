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
package com.runwaysdk.dataaccess.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.cache.globalcache.ehcache.CachedBusinessDAOinfo;
import com.runwaysdk.dataaccess.cache.globalcache.ehcache.CachedEntityDAOinfo;

public class Memorystore implements ObjectStore
{
  /**
   * Key:{@link EntityDAOIF} id Value: {@link CachedEntityDAOinfo}
   */
  Map<String, CachedEntityDAOinfo> entityMap;

  public Memorystore()
  {
    this.entityMap = Collections.synchronizedMap(new HashMap<String, CachedEntityDAOinfo>());
  }

  /**
   * Initializes the global cache.
   */
  public void removeAll()
  {
    this.entityMap.clear();
  }
  
  /**
   * Returns the number of items stored in the cache.
   */
  public int cacheSize()
  {
    return this.entityMap.size();
  }

  /**
   * Shuts down the cache and flushes memory contents to a persistent store.
   */
  public void shutdown()
  {
  }

  /**
   * Returns true if the cache is initialized, false otherwise.
   */
  public boolean isCacheInitialized()
  {
    return true;
  }

  /**
   * Initializes the cache.
   */
  public void initializeCache()
  {
    this.removeAll();
  }
  
  /**
   * Returns true if the cache contains the provided key (entity id).
   */
  public boolean containsKey(String key)
  {
    return this.entityMap.containsKey(key);
  }

  /**
   * Returns the name of the cache.
   * 
   * @return name of the cache.
   */
  public String getCacheName()
  {
    return "MAIN_MEMORY_CACHE";
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
      CachedEntityDAOinfo cachedEntityDAOinfo = this.entityMap.get(id);

      if (cachedEntityDAOinfo == null)
      {
        return new LinkedList<RelationshipDAOIF>();
      }
      else
      {
        CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) cachedEntityDAOinfo;
        return cachedBusinessDAOinfo.getParentRelationshipDAOlist(relationshipType);
      }
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
      CachedEntityDAOinfo cachedEntityDAOinfo = this.entityMap.get(id);

      if (cachedEntityDAOinfo == null)
      {
        return new LinkedList<RelationshipDAOIF>();
      }
      else
      {
        CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) cachedEntityDAOinfo;
        return cachedBusinessDAOinfo.getChildRelationshipDAOlist(relationshipType);
      }
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
      CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo)getCachedEntityDAOinfo(true, RelationshipDAO.getOldParentId(relationshipDAOIF), relationshipDAOIF.getParentId(), CachedEntityDAOinfo.Types.BUSINESS);

      cachedBusinessDAOinfo.addChildRelationship(relationshipDAOIF);
    }

    synchronized (relationshipDAOIF.getChildId())
    {
      CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo)getCachedEntityDAOinfo(true, RelationshipDAO.getOldChildId(relationshipDAOIF), relationshipDAOIF.getChildId(), CachedEntityDAOinfo.Types.BUSINESS);

      cachedBusinessDAOinfo.addParentRelationship(relationshipDAOIF);
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
      CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo)getCachedEntityDAOinfo(true, RelationshipDAO.getOldParentId(relationshipDAOIF), relationshipDAOIF.getParentId(), CachedEntityDAOinfo.Types.BUSINESS);

      if (hasIdChanged)
      {
        cachedBusinessDAOinfo.updateChildRelationship(relationshipDAO);
      }
      else
      {
        cachedBusinessDAOinfo.addChildRelationship(relationshipDAO);        
      }
    }

    synchronized (relationshipDAOIF.getChildId())
    {
      CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo)getCachedEntityDAOinfo(true, RelationshipDAO.getOldChildId(relationshipDAOIF), relationshipDAOIF.getChildId(), CachedEntityDAOinfo.Types.BUSINESS);

      if (hasIdChanged)
      {
        cachedBusinessDAOinfo.updateParentRelationship(relationshipDAO);
      }
      else
      {
        cachedBusinessDAOinfo.addParentRelationship(relationshipDAO);        
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
      CachedEntityDAOinfo cachedEntityDAOinfo = getCachedEntityDAOinfo(true, oldEntityId, entityDAOIF.getId(), entityDAOIF);
      
      if (cachedEntityDAOinfo != null)
      {    
        cachedEntityDAOinfo.addEntityDAOIF(entityDAOIF);
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
   * @return True if the child object still has parent relationships of this
   *         type.
   */
  public boolean removeParentRelationshipDAOIFtoCache(RelationshipDAO relationshipDAO, boolean deletedObject)
  {
    synchronized (relationshipDAO.getChildId())
    {
      boolean stillHasParents = false;

      CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) this.entityMap.get(relationshipDAO.getChildId());
      if (cachedBusinessDAOinfo != null)
      {
        stillHasParents = cachedBusinessDAOinfo.removeParentRelationship(relationshipDAO);

        if (cachedBusinessDAOinfo.removeFromGlobalCache())
        {
          this.removeFromEntityMap(relationshipDAO.getChildId());
        }
      }
      
//      if (relationshipDAO.hasChildIdChanged())
//      {
//        cachedBusinessDAOinfo = (CachedBusinessDAOinfo) this.entityMap.get(relationshipDAO.getOldChildId());
//        if (cachedBusinessDAOinfo != null)
//        {
//          this.entityMap.remove(relationshipDAO.getOldChildId());
//        }
//      }

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
      CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) this.entityMap.get(childId);
      if (cachedBusinessDAOinfo != null)
      {
        cachedBusinessDAOinfo.removeAllParentRelationshipsOfType(relationshipType);

        if (cachedBusinessDAOinfo.removeFromGlobalCache())
        {
          this.removeFromEntityMap(childId);
        }
      }
    }
  }

  /**
   * Removes the {@link RelationshipDAOIF} from the child relationship of the
   * parent object in the cache.
   * 
   * @param relationshipDAO
   * @param deletedObject
   *          indicates the object is being deleted from the application.
   * 
   * @return True if the parent object still has children relationships of this
   *         type.
   */
  public boolean removeChildRelationshipDAOIFtoCache(RelationshipDAOIF relationshipDAO, boolean deletedObject)
  {
    synchronized (relationshipDAO.getParentId())
    {
      boolean stillHasChildren = false;

      CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) this.entityMap.get(relationshipDAO.getParentId());
      if (cachedBusinessDAOinfo != null)
      {
        stillHasChildren = cachedBusinessDAOinfo.removeChildRelationship(relationshipDAO);

        if (cachedBusinessDAOinfo.removeFromGlobalCache())
        {
          this.removeFromEntityMap(relationshipDAO.getParentId());
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
      CachedBusinessDAOinfo cachedBusinessDAOinfo = (CachedBusinessDAOinfo) this.entityMap.get(parentId);
      if (cachedBusinessDAOinfo != null)
      {
        cachedBusinessDAOinfo.removeAllChildRelationshipsOfType(relationshipType);

        if (cachedBusinessDAOinfo.removeFromGlobalCache())
        {
          this.removeFromEntityMap(parentId);
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
      CachedEntityDAOinfo cachedEntityDAOinfo = this.entityMap.get(id);

      if (cachedEntityDAOinfo == null)
      {
        return null;
      }
      else
      {                
        return cachedEntityDAOinfo.getEntityDAOIF();
      }
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
      CachedEntityDAOinfo cachedEntityDAOinfo = getCachedEntityDAOinfo(true, EntityDAO.getOldId(entityDAOIF), entityDAOIF.getId(), entityDAOIF);
      cachedEntityDAOinfo.addEntityDAOIF(entityDAOIF);
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
      CachedEntityDAOinfo cachedEntityDAOinfo = this.entityMap.get(id);

      if (cachedEntityDAOinfo != null)
      {
        cachedEntityDAOinfo.removeEntityDAOIF();
        if (cachedEntityDAOinfo.removeFromGlobalCache())
        {
          this.removeFromEntityMap(id);
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
  public Set<String> getCacheKeys()
  {
    return this.entityMap.keySet();
  }

  /**
   * Persists the collections to the cache so that it can be persisted to the
   * disk store.
   * 
   * @param collectionMap
   */
  public void backupCollectionClasses(Map<String, CacheStrategy> collectionMap)
  {
  }

  /**
   * Returns the collections from the cache.
   * 
   */
  public Map<String, CacheStrategy> restoreCollectionClasses()
  {
    return null;
  }

  /**
   * Removes the set of collection classes from the cache.
   * 
   */
  public void removeCollectionClasses()
  {
  }

  /**
   * Starts transaction for the current thread.
   */
  public void beginTransaction()
  {
  }

  /**
   * Commits transaction for the current thread.
   */
  public void commitTransaction()
  {
  }

  /**
   * Rollback transaction for the current thread.
   */
  public void rollbackTransaction()
  {
  }

  public void flush()
  {
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
    CachedEntityDAOinfo cachedEntityDAOinfo = null;
    
    if (oldId != null && !oldId.trim().equals(""))
    {
      cachedEntityDAOinfo = this.entityMap.get(oldId);
      
      if (cachedEntityDAOinfo != null)
      {
        this.entityMap.remove(oldId);
      }
    }
    
    if (cachedEntityDAOinfo == null)
    {
      cachedEntityDAOinfo = this.entityMap.get(newId);
    }
      
    if (cachedEntityDAOinfo == null && createIfNotExists)
    {
      cachedEntityDAOinfo = infoType.createInfo();
    }

    if (cachedEntityDAOinfo != null)
    {      
      this.entityMap.put(newId, cachedEntityDAOinfo);
    }
      
    return cachedEntityDAOinfo;
  }
  
  private void removeFromEntityMap(String id)
  {
    this.entityMap.remove(id);
  }

  @Override
  public boolean isEmpty()
  {
    return this.cacheSize() == 0;
  }
}


