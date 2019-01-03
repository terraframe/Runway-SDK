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

import org.apache.commons.io.FileUtils;
import org.ehcache.PersistentUserManagedCache;
import org.ehcache.UserManagedCacheBuilder;
import org.ehcache.config.ResourcePoolsBuilder;
import org.ehcache.config.persistence.DefaultPersistenceConfiguration;
import org.ehcache.config.persistence.UserManagedPersistenceContext;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.internal.persistence.DefaultLocalPersistenceService;
import org.ehcache.spi.service.LocalPersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.constants.ServerProperties;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.cache.TransactionStoreIF;

public class TransactionDiskstore implements TransactionStoreIF
{
  final static Logger logger = LoggerFactory.getLogger(TransactionDiskstore.class);
  
  private PersistentUserManagedCache<String, EntityDAO> cache;
  
  private String              cacheName;
  
  private String              cacheFileLocation;
  
  private LocalPersistenceService persistenceService;

  public TransactionDiskstore(String cacheName, int memorySize)
  {
    this.cacheName = cacheName;
    int diskSize = ServerProperties.getTransactionDiskstoreSize();
    this.cacheFileLocation = ServerProperties.getTransactionCacheFileLocation();
    
    this.persistenceService = new DefaultLocalPersistenceService(new DefaultPersistenceConfiguration(new File(this.cacheFileLocation, this.cacheName)));
    
    this.cache = UserManagedCacheBuilder.newUserManagedCacheBuilder(String.class, EntityDAO.class)
        .with(new UserManagedPersistenceContext<String, EntityDAO>(cacheName, this.persistenceService)) 
        .withResourcePools(ResourcePoolsBuilder.newResourcePoolsBuilder() 
                  .heap(memorySize, EntryUnit.ENTRIES)
//                  .offheap(cacheMemorySize, MemoryUnit.MB)
                  .disk(diskSize, MemoryUnit.MB, true)
         )
        .build(true);
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
   * Removes all items in the global cache.
   */
  public void removeAll()
  {
    cache.clear();
  }

  public void close()
  {
    this.cache.close();
    this.persistenceService.destroyAllPersistenceSpaces();
    this.persistenceService.stop();
    try
    {
      FileUtils.deleteDirectory(new File(this.cacheFileLocation, this.cacheName));
    }
    catch (IOException e)
    {
      logger.info("Error happened while deleting transaction cache directory. This probably shouldn't matter if ehcache shut down correctly.", e);
    }
  }

  /**
   * Returns the {@link EntityDAOIF} from the cache with the given oid or null if
   * the object with the given oid is not in the cache.
   * 
   * @param oid
   * @return {@link EntityDAOIF} from the cache with the given oid or null if the
   *         object with the given oid is not in the cache.
   */
  public EntityDAOIF getEntityDAOIFfromCache(String oid)
  {
    synchronized (oid)
    {
      return cache.get(oid);
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
      cache.put(entityDAOIF.getOid(), (EntityDAO) entityDAOIF);
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
  public void removeEntityDAOIFfromCache(String oid)
  {
    synchronized (oid)
    {
      cache.remove(oid);
    }
  }

  /**
   * Returns true if the cache is empty
   */
  public boolean isEmpty()
  {
    return !cache.iterator().hasNext();
  }
}
