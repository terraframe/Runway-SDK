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
   * <br/><b>Precondition:</b>  id != null
   * <br/><b>Precondition:</b>  !id.trim().equals("")
   * <br/><b>Postcondition:</b> return value may not be null
   *
   * @param  id of the desired EntityDAO
   * @return EntityDAO with the given id
   * @throws DataAccessException a EntityDAO with the given id does not exist
   *         in the database and the cache
   */
  public EntityDAOIF getEntityInstance(String id)
  {
    if (this.reload == true)
    {
      this.reload();
    }

    synchronized(id)
    {
      EntityDAOIF entityDAOIF = this.getFromFactory(id);

      if (entityDAOIF != null)
      {
        this.updateCache((EntityDAO)entityDAOIF);
        this.entityDAOIdSet.add(id);
      } 
      else
      {
        MdClassDAOIF metadata = MdClassDAO.getMdClassByRootId(IdParser.parseMdTypeRootIdFromId(id));

        String errMsg = "An instance of [" + metadata.definesType() + "] with id [" + id
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
   * @param id
   * @param entityType
   * @return
   */
  protected abstract EntityDAOIF getFromFactory(String id);


  /**
   * Places the given EntityDAO into the cache.
   *
   * <br/><b>Precondition:</b>  EntityDAO != null
   *
   * <br/><b>Postcondition:</b> cache contains the given EntityDAO
   *
   * @param  entityDAO to add to this collection
   */
  public void updateCache(EntityDAO entityDAO)
  {
    synchronized(entityDAO.getId())
    {
      entityDAO.setIsFromCacheAll(true);

      this.entityDAOIdSet.add(entityDAO.getId()); 

      this.entityDAOIdByKeyMap.put(entityDAO.getKey(), entityDAO.getId());

      ObjectCache.putEntityDAOIFintoCache(entityDAO);
    }
  }

  /**
   * Removes the given EntityDAO from the cache.
   *
   * <br/><b>Precondition:</b>  EntityDAO != null
   *
   * <br/><b>Postcondition:</b> cache no longer contains the given EntityDAO
   *
   * @param  EntityDAO to remove from this collection
   */
  public void removeCache(EntityDAO entityDAO)
  {
    synchronized(entityDAO.getId())
    {
      this.entityDAOIdSet.remove(entityDAO.getId());

      this.entityDAOIdByKeyMap.remove(entityDAO.getKey());

      ObjectCache.removeEntityDAOIFfromCache(entityDAO.getId(), true);
    }
  }

}
