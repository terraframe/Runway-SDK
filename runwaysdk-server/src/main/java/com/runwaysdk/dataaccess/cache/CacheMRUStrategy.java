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

import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.util.IdParser;


public class CacheMRUStrategy extends CacheStrategy
{
  /**
   * 
   */
  private static final long serialVersionUID = 5890309786227148888L;

  private       int                  mostRecentlyUsedCacheSize;

  /**
   * Initializes the EntityDAOCollection with the type of EntityDAOs that
   *   reside in this collection.  MdRelationship types are not allowed.
   *
   * <br/><b>Precondition:</b>  type != null
   * <br/><b>Precondition:</b>  !type.trim().equals("")
   * <br/><b>Precondition:</b>  type is a class or a struct and not a relationship
   * <br/><b>Postcondition:</b> true
   *
   * @param type name of the class of  EntityDAOs stored in this collection
   * @param bucketSize
   */
  @SuppressWarnings("unchecked")
  public CacheMRUStrategy(String type, int bucketSize)
  {
    super(type);
    this.mostRecentlyUsedCacheSize = bucketSize;

    if (this.mostRecentlyUsedCacheSize  == 0)
    {
      this.mostRecentlyUsedCacheSize = 1;
    }
    
    this.entityDAOIdSet = Collections.newSetFromMap(Collections.synchronizedMap(new EntityDAOLRUMap2(this.mostRecentlyUsedCacheSize, (float)0.75, true)));
    this.entityDAOIdByKeyMap = Collections.synchronizedMap(new EntityDAOLRUMap2(this.mostRecentlyUsedCacheSize, (float)0.75, true));
  }

  /**
   * Called when the main {@link ObjectCache} does not have the {@link EntityDAO} cached. 
   * If this collection is supposed to cache the object, the main {@link ObjectCache} is updated.
   * If it is not in the cache, perhaps it was marked to be refreshed by removing it from the cache.
   *
   * <br/><b>Precondition:</b>  id != null
   * <br/><b>Precondition:</b>  !id.trim().equals("")
   * <br/><b>Precondition:</b>  id is a valid id of a EntityDAO
   * <br/><b>Postcondition:</b> return value may not be null
   *
   * @param  id of the desired EntityDAO
   * @return EntityDAO with the given id
   * @throws DataAccessException a EntityDAO with the given id does not exist
   *         in the database and the cache
   */
  public EntityDAOIF getEntityInstance(String id)
  {
    if (reload == true)
    {
      this.reload();
    }

    synchronized(id)
    {
      EntityDAOIF entityDAO = this.getFromFactory(id);

      if (entityDAO != null)
      {
        this.updateCache((EntityDAO)entityDAO);
      }
      else
      {
        if (entityDAO == null)
        {
          MdClassDAOIF metadata = MdClassDAO.getMdClassByRootId(IdParser.parseMdTypeRootIdFromId(id));

          String errMsg = "An instance of [" + metadata.definesType() + "] with id [" + id
            + "] could not be found. [" + metadata.definesType() + "] caches none.";

          throw new DataNotFoundException(errMsg, metadata);
        }
      }

      ObjectCache.putEntityDAOIFintoCache(entityDAO);
    
      return entityDAO;
    }
  }

  /**
   * Retrieves the EntityDAO with the given type and keyname from the collection.  If
   *  the cache does not contain a EntityDAO with the given type and keyname, then
   *  the database will be queried.  If a EntityDAO with the type and keyname is found
   *  in the database, then it is added to the collection.
   *
   * <br/><b>Precondition:</b>  type != null
   * <br/><b>Precondition:</b>  !type.trim().equals("")
   * <br/><b>Postcondition:</b> return value may not be null
   *
   * <br/><b>Precondition:</b>  key != null
   * <br/><b>Precondition:</b>  !key.trim().equals("")
   * <br/><b>Postcondition:</b> return value may not be null
   *
   * @param  type of the desired EntityDAO
   * @param  key of the desired EntityDAO
   * @return EntityDAO with the given type and keyname
   * @throws DataAccessException a EntityDAO with the given type and keyname does not exist
   *         in the database and the cache
   */
  public synchronized EntityDAOIF getEntityInstance(String type, String key)
  {
    if (reload == true)
    {
      this.reload();
    }

    EntityDAOIF entityDAOIF = null;

    String entityDAOId = this.entityDAOIdByKeyMap.get(key);

    if (entityDAOId != null)
    {
      entityDAOIF = ObjectCache.getEntityDAOIFfromCache(entityDAOId);
    }
    
    if (entityDAOIF == null)
    {
      entityDAOIF = this.getFromFactory(type, key);

      if (entityDAOIF != null)
      {
        this.updateCache((EntityDAO)entityDAOIF);
      }
      else
      {
        MdClassDAOIF metadata = MdClassDAO.getMdClassDAO(type);

        String errMsg = "An instance of [" + metadata.definesType() + "] with key [" + key
          + "] could not be found. [" + metadata.definesType() + "] caches none.";

        throw new DataNotFoundException(errMsg, metadata);
      }
    }

    return entityDAOIF;
  }

  /**
   *
   * @param id
   * @return
   */
  protected EntityDAOIF getFromFactory(String id)
  {
    return BusinessDAOFactory.get(id);
  }

  /**
   *
   * @param type
   * @param key
   * @return
   */
  protected EntityDAOIF getFromFactory(String type, String key)
  {
    return BusinessDAOFactory.get(type, key);
  }

  /**
   * Reloads the cache of this collection.  The cache is cleared.  All EntityDAOs of the
   *   type stored in this collection are instantiated and put in the cache.
   *
   * <br/><b>Precondition:</b>   true
   *
   * <br/><b>Postcondition:</b>  Cache contains all EntityDAOs of the type that are to be stored in this
   *        collection
   *
   */
  public synchronized void reload()
  {
    this.removeCacheStrategy();
    
    super.reload();
    this.reload = false;
  }

  /**
   * Places the given EntityDAO into the cache.
   *
   * <br/><b>Precondition:</b>  EntityDAO != null
   *
   * <br/><b>Postcondition:</b> cache contains the given EntityDAO
   *
   * @param  EntityDAO to add to this collection
   */
  public void updateCache(EntityDAO entityDAO)
  {
    String syncId;
    if (entityDAO.hasIdChanged())
    {
      syncId = entityDAO.getOldId();
    }
    else
    {
      syncId = entityDAO.getId();
    }
    
    String oldKey = null;
    if (entityDAO.hasKeyChanged())
    {
      oldKey = entityDAO.getKey();
      entityDAO.clearOldKey();
    }
    
    synchronized(syncId)
    {
      entityDAO.setIsFromCacheMRU(true);

      if (entityDAO.hasIdChanged())
      {
        this.entityDAOIdSet.remove(entityDAO.getId());
        String oldId = entityDAO.getOldId();
        entityDAO.clearOldId();
        ObjectCache.updateIdEntityDAOIFinCache(oldId, entityDAO);
      }
      else
      {
        ObjectCache.putEntityDAOIFintoCache(entityDAO);
      }
      this.entityDAOIdSet.add(entityDAO.getId());

      if (oldKey != null)
      {
        this.entityDAOIdByKeyMap.remove(oldKey);
      }
      this.entityDAOIdByKeyMap.put(entityDAO.getKey(), entityDAO.getId());
    }
  }
  

  /**
   * Removes the given {@link EntityDAO} from the cache.
   *
   * <br/><b>Precondition:</b>  {@link EntityDAO} != null
   *
   * <br/><b>Postcondition:</b> cache no longer contains the given {@link EntityDAO}
   *
   * @param  {@link EntityDAO} to remove from this collection
   */
  public void removeCache(EntityDAO entityDAO)
  {
    String oldKey = null;
    if (entityDAO.hasKeyChanged())
    {
      oldKey = entityDAO.getKey();
      entityDAO.clearOldKey();
    }
    
    synchronized(entityDAO.getId())
    {
      if (entityDAO.hasIdChanged())
      {
        this.entityDAOIdSet.remove(entityDAO.getOldId());
        ObjectCache.removeEntityDAOIFfromCache(entityDAO.getOldId(), true);
      }
      
      this.entityDAOIdSet.remove(entityDAO.getId());
      
      if (oldKey != null)
      {
        this.entityDAOIdByKeyMap.remove(oldKey);
      }
      
      this.entityDAOIdByKeyMap.remove(entityDAO.getKey());

      ObjectCache.removeEntityDAOIFfromCache(entityDAO.getId(), true);
    }
  }

  /**
   * Removes the {@link EntityDAO} with the given id from the cache so that it can be refreshed
   * on the next request for the object.
   *
   * <br/><b>Precondition:</b>  {@link EntityDAO} != null
   *
   * <br/><b>Postcondition:</b> cache no longer contains the given {@link EntityDAO}
   *
   * @param  id for the {@link EntityDAO} to remove from this collection
   */
  public void clearCacheForRefresh(String entityId)
  {
    synchronized(entityId)
    {      
      EntityDAOIF entityDAOIF = ObjectCache.getEntityDAOIFfromCache(entityId);
            
      if (entityDAOIF != null)
      {
        ObjectCache.removeEntityDAOIFfromCache(entityId, false);
        this.entityDAOIdByKeyMap.remove(entityDAOIF.getKey());
      }
      
      this.entityDAOIdSet.remove(entityId);
    }
  }

}
