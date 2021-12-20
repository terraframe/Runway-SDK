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
package com.runwaysdk.dataaccess.cache;

import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.util.IdParser;


/**
 *
 * @author nathan
 *
 */
public abstract class CacheAllStrategy extends CacheStrategy
{

  /**
   * 
   */
  private static final long serialVersionUID = -7548792204949045029L;

  /**
   * Copies the content from the given collection into this one.
   */
  public void addAll(CacheStrategy cacheStrategy)
  {
    super.addAll(cacheStrategy);
  }
  
  /**
   * Initializes the CacheAllStrategy with the entity of EntityDAOs that
   *   reside in this collection.
   *
   * <br/><b>Precondition:</b>  mdEntity != null
   * <br/><b>Postcondition:</b> true
   *
   * @param classType Class metadata of the EntityDAOs stored in this collection
   */
  public CacheAllStrategy(String classType)
  {
    super(classType);
  }

  /**
   * Called when the main {@link ObjectCache} does not have the {@link EntityDAO} cached. 
   * If this collection is supposed to cache the object, the main {@link ObjectCache} is updated.
   * If it is not in the cache, perhaps it was marked to be refreshed by removing it from the cache.
   *
   * <br/><b>Precondition:</b>  oid != null
   * <br/><b>Precondition:</b>  !oid.trim().equals("")
   * <br/><b>Postcondition:</b> return value may not be null
   *
   * @param  oid of the desired EntityDAO
   * @return EntityDAO with the given oid
   * @throws DataAccessException a EntityDAO with the given oid does not exist
   *         in the database and the cache
   */
  public EntityDAOIF getEntityInstance(String oid)
  {
    if (this.reload == true)
    {
      this.reload();
    }

    synchronized(oid)
    {
      EntityDAOIF entityDAOIF = this.getFromFactory(oid);

      if (entityDAOIF != null)
      {
        this.updateCache((EntityDAO)entityDAOIF);
        this.entityDAOIdSet.add(oid);
      } 
      else
      {
        MdClassDAOIF metadata = MdClassDAO.getMdClassByRootId(IdParser.parseMdTypeRootIdFromId(oid));

        String errMsg = "An instance of [" + metadata.definesType() + "] with oid [" + oid
          + "] could not be found. [" + metadata.definesType() + "] caches all.";

        throw new DataNotFoundException(errMsg, metadata);
      }

      // Make sure the global cache has it.
      ObjectCache.putEntityDAOIFintoCache(entityDAOIF);

      return entityDAOIF;
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
    if (this.reload == true)
    {
      this.reload();
    }

    EntityDAOIF entityDAOIF = null;
    
    String entityDAOId = this.entityDAOIdByKeyMap.get(key);
   
    if (entityDAOId != null)
    {
      entityDAOIF = ObjectCache.getEntityDAOIFfromCache(entityDAOId);
    }

    // If it is not in the cache, perhaps it was marked to be refreshed by
    // removing it from the cache.
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
          + "] could not be found. [" + metadata.definesType() + "] caches all.";

        throw new DataNotFoundException(errMsg, metadata);
      }
    }

    return entityDAOIF;
  }

  /**
   *
   * @param oid
   * @param entityType
   * @return
   */
  protected abstract EntityDAOIF getFromFactory(String oid);
  
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
      syncId = entityDAO.getOid();
    }
    
    String oldKey = null;
    if (entityDAO.hasKeyChanged())
    {
      oldKey = entityDAO.getKey();
      entityDAO.clearOldKey();
    }
  
    synchronized(syncId)
    {
      entityDAO.setIsFromCacheAll(true);
      
      if (entityDAO.hasIdChanged())
      {
        String oldId = entityDAO.getOldId();
        this.entityDAOIdSet.remove(oldId);
        entityDAO.clearOldId();
        ObjectCache.updateIdEntityDAOIFinCache(oldId, entityDAO);
      }
      else
      {
        ObjectCache.putEntityDAOIFintoCache(entityDAO);
      }
      this.entityDAOIdSet.add(entityDAO.getOid());
      if (oldKey != null)
      {
        this.entityDAOIdByKeyMap.remove(oldKey);
      }
      this.entityDAOIdByKeyMap.put(entityDAO.getKey(), entityDAO.getOid());
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
    String syncId;
    if (entityDAO.hasIdChanged())
    {
      syncId = entityDAO.getOldId();
    }
    else
    {
      syncId = entityDAO.getOid();
    }
    
    String oldKey = null;
    if (entityDAO.hasKeyChanged())
    {
      oldKey = entityDAO.getKey();
      entityDAO.clearOldKey();
    }

    synchronized(syncId)
    {      
      if (entityDAO.hasIdChanged())
      {
        this.entityDAOIdSet.remove(entityDAO.getOldId());
        ObjectCache.removeEntityDAOIFfromCache(entityDAO.getOldId(), true);
      } 
      
      this.entityDAOIdSet.remove(entityDAO.getOid());

      ObjectCache.removeEntityDAOIFfromCache(entityDAO.getOid(), true);      
    }    
    

    if (oldKey != null)
    {
      synchronized(oldKey)
      {   
        this.entityDAOIdByKeyMap.remove(oldKey);
      }
    }
    
    synchronized(entityDAO.getKey())
    {
      String keyId = this.entityDAOIdByKeyMap.get(entityDAO.getKey());
          
      if (keyId != null)
      {
        if (keyId.equals(entityDAO.getOid()) ||
            (entityDAO.hasIdChanged() && keyId.equals(entityDAO.getOldId()))
           )
        {
          this.entityDAOIdByKeyMap.remove(entityDAO.getKey());
        }
      }
    }
  }

  /**
   * Removes the {@link EntityDAO} with the given oid from the cache so that it can be refreshed
   * on the next request for the object.
   *
   * <br/><b>Precondition:</b>  {@link EntityDAO} != null
   *
   * <br/><b>Postcondition:</b> cache no longer contains the given {@link EntityDAO}
   *
   * @param  oid for the {@link EntityDAO} to remove from this collection
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
