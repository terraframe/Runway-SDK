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

import org.ehcache.Cache;
import org.ehcache.CacheManagerBuilder;
import org.ehcache.PersistentCacheManager;
import org.ehcache.config.CacheConfigurationBuilder;
import org.ehcache.config.ResourcePoolsBuilder;
import org.ehcache.config.persistence.CacheManagerPersistenceConfiguration;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;

import com.runwaysdk.constants.ServerProperties;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.cache.TransactionStoreIF;

public class TransactionDiskstore implements TransactionStoreIF
{
  private static PersistentCacheManager manager                   = null;
  
  private Cache<String, EntityDAO> cache;
  
  private String              cacheName;

  public TransactionDiskstore(String cacheName)
  {
    this.cacheName = cacheName;
    
//    this.cache = getCacheManager().getCache(cacheName, String.class, EntityDAO.class);
//    this.cache = null;
//    
//    if (cache == null)
//    {
      int diskSize = ServerProperties.getTransactionDiskstoreSize();
      
      cache = getCacheManager().createCache(cacheName,
        CacheConfigurationBuilder.newCacheConfigurationBuilder()
          .withResourcePools(ResourcePoolsBuilder.newResourcePoolsBuilder() 
                  .heap(10, EntryUnit.ENTRIES)
//                  .offheap(cacheMemorySize, MemoryUnit.MB)
                  .disk(diskSize, MemoryUnit.MB, true)
          )
          .buildConfig(String.class, EntityDAO.class));
//    }
  }
  
  private synchronized static PersistentCacheManager getCacheManager()
  {
    if (manager == null)
    {
      String cacheFileLocation = ServerProperties.getTransactionCacheFileLocation();
      
      manager = CacheManagerBuilder.newCacheManagerBuilder()
          .with(new CacheManagerPersistenceConfiguration(new File(cacheFileLocation))) 
          .build(true);
    }

    return manager;
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

  protected Cache<String, EntityDAO> getCache()
  {
    return this.cache;
  }

  /**
   * Removes all items in the global cache.
   */
  public void removeAll()
  {
    this.getCache().clear();
  }

  public void close()
  {
    TransactionDiskstore.manager.close();
    TransactionDiskstore.manager.toMaintenance().destroy();
    TransactionDiskstore.manager = null;
    this.cache = null;
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
      return this.getCache().get(id);
    }
  }

  /**
   * Puts the given {@link EntityDAOIF} into the global cache.
   * 
   * @param entityDAOIF
   *          {@link EntityDAOIF} that goes into the the global cache.
   */
  public void putEntityDAOIFintoCache(EntityDAOIF entityDAOIF, boolean transaction)
  {
    synchronized (entityDAOIF.getId())
    {
      this.getCache().put(entityDAOIF.getId(), (EntityDAO) entityDAOIF);
    }
  }
  
  public void putEntityDAOIFintoCache(EntityDAOIF entityDAOIF)
  {
    putEntityDAOIFintoCache(entityDAOIF, true);
  }

  /**
   * Removes the item from the cache with the given key
   * 
   * @param entityDAOIFid
   *          key of the item to be removed from the cache.
   * @param deletedObject
   *          indicates the object is being deleted from the application.
   */
  public void removeEntityDAOIFfromCache(String id)
  {
    synchronized (id)
    {
      this.getCache().remove(id);
    }
  }

  /**
   * Returns true if the cache is empty
   */
  public boolean isEmpty()
  {
    return !this.getCache().iterator().hasNext();
  }
}
