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

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;

public interface ObjectStore
{
  /**
   * Initializes the global cache.
   */
  public void removeAll();
  
  /**
   * Returns the number of items stored in the cache.
   */
  public int cacheSize();

  /**
   * Returns true if the cache is initialized, false otherwise.
   */
  public boolean isCacheInitialized();

  /**
   * Initializes the cache.
   */
  public void initializeCache();

  /**
   * Returns the name of the cache.
   * 
   * @return name of the cache.
   */
  public String getCacheName();

  /**
   * Returns a list of parent relationships of the given type from the cache for
   * a {@link BusinessDAOIF} with the given id.
   * 
   * @param id
   * @param relationshipType
   * @return
   */
  public List<RelationshipDAOIF> getParentRelationshipsFromCache(String id, String relationshipType);

  /**
   * Returns a list of child relationships of the given type from the cache for
   * a {@link BusinessDAOIF} with the given id.
   * 
   * @param id
   * @param relationshipType
   * 
   * @return
   */
  public List<RelationshipDAOIF> getChildRelationshipsFromCache(String id, String relationshipType);

  /**
   * Adds the {@link RelationshipDAOIF} to the parent and child relationships of
   * the parent and child objects in the cache.
   * 
   * @param relationshipDAOIF
   */
  public void addRelationshipDAOIFtoCache(RelationshipDAOIF relationshipDAOIF);

  /**
   * Updates the stored id if it has changed for the {@link RelationshipDAOIF} to the 
   * parent and child relationships of the parent and child objects in the cache.
   * 
   * @param hasIdChanged
   * @param relationshipDAOIF
   */
  public void updateRelationshipDAOIFinCache(Boolean hasIdChanged, RelationshipDAOIF relationshipDAOIF);

  /**
   * Updates the changed id for the given {@link EntityDAOIF} in the cache.
   * 
   * <br/><b>Precondition:</b> Calling method has checked whether the id has changed.
   * 
   * @param oldEntityId
   * @param entityDAOIF
   */
  public void updateIdEntityDAOIFinCache(String oldEntityId, EntityDAOIF entityDAOIF);
  
  /**
   * Removes the {@link RelationshipDAOIF} from the parent relationship of the
   * child object in the cache.
   * 
   * @param relationshipDAO
   * 
   * @param deletedObject
   *          indicates the object is being deleted from the application.
   * 
   * @return True if the child object still has parent relationships of this
   *         type.
   */
  public boolean removeParentRelationshipDAOIFtoCache(RelationshipDAO relationshipDAO, boolean deletedObject);

  /**
   * Removes all parent relationships of the given type for the
   * {@link BusinessDAOIF} with the given id.
   * 
   * @param childId
   * @param relationshipType
   * 
   * @param deletedObject
   *          indicates the object is being deleted from the application.
   */
  public void removeAllParentRelationshipsOfType(String childId, String relationshipType, boolean deletedObject);

  /**
   * Removes the {@link RelationshipDAOIF} from the child relationship of the
   * parent object in the cache.
   * 
   * @param relationshipDAOIF
   * 
   * @return True if the parent object still has children relationships of this
   *         type.
   * 
   * @param deletedObject
   *          indicates the object is being deleted from the application.
   */
  public boolean removeChildRelationshipDAOIFtoCache(RelationshipDAOIF relationshipDAOIF, boolean deletedObject);

  /**
   * Removes all child relationships of the given type for the
   * {@link BusinessDAOIF} with the given id.
   * 
   * @param parentId
   * @param relationshipType
   * 
   * @param deletedObject
   *          indicates the object is being deleted from the application.
   */
  public void removeAllChildRelationshipsOfType(String parentId, String relationshipType, boolean deletedObject);

  /**
   * Returns a {@link Set} of the keys that are in the cache. The keys may or
   * may not represent {@link EntityDAOIF} ids.
   * 
   * @return {@link Set} of the keys that are in the cache.
   */
  public Set<String> getCacheKeys();

  /**
   * Persists the collections to the cache so that it can be persisted to the
   * disk store.
   * 
   * @param collectionMap
   */
  public void backupCollectionClasses(Map<String, CacheStrategy> collectionMap);

  /**
   * Returns the collections from the cache.
   * 
   */
  public Map<String, CacheStrategy> restoreCollectionClasses();

  /**
   * Removes the set of collection classes from the cache.
   * 
   */
  public void removeCollectionClasses();

  /**
   * Starts transaction for the current thread.
   */
  public void beginTransaction();

  /**
   * Commits transaction for the current thread.
   */
  public void commitTransaction();

  /**
   * Rollback transaction for the current thread.
   */
  public void rollbackTransaction();

  public void flush();

  public void shutdown();
  

  /**
   * Returns the {@link EntityDAOIF} from the cache with the given id or null if
   * the object with the given id is not in the cache.
   * 
   * @param id
   * @return {@link EntityDAOIF} from the cache with the given id or null if the
   *         object with the given id is not in the cache.
   */
  public EntityDAOIF getEntityDAOIFfromCache(String id);

  /**
   * Puts the given {@link EntityDAOIF} into the global cache.
   * 
   * @param entityDAOIF
   *          {@link EntityDAOIF} that goes into the the global cache.
   */
  public void putEntityDAOIFintoCache(EntityDAOIF entityDAOIF);

  /**
   * Removes the item from the cache with the given key
   * 
   * @param entityDAOIFid
   *          key of the item to be removed from the cache.
   * @param deletedObject
   *          indicates the object is being deleted from the application.
   */
  public void removeEntityDAOIFfromCache(String id, boolean deletedObject);
}
