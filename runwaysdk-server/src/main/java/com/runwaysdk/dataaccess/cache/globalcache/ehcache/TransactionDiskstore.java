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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.Status;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.DiskStoreConfiguration;
import net.sf.ehcache.event.CacheManagerEventListener;

import com.runwaysdk.constants.ServerProperties;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.cache.TransactionStoreIF;
import com.runwaysdk.logging.LogLevel;
import com.runwaysdk.logging.RunwayLogUtil;

public class TransactionDiskstore implements TransactionStoreIF
{

  private static final class CacheManagerShutdownListener implements CacheManagerEventListener
  {
    @Override
    public void notifyCacheRemoved(String arg0)
    {
    }

    @Override
    public void notifyCacheAdded(String arg0)
    {
    }

    @Override
    public void init() throws CacheException
    {
    }

    @Override
    public Status getStatus()
    {
      return null;
    }

    @Override
    public void dispose() throws CacheException
    {
      TransactionDiskstore.setTransactionCacheManager(null);
    }
  }

  private static final String TRANSACTION_CACHE_MANAGER = "TransactionCacheManager";

  private static CacheManager manager                   = null;

  private String              cacheName;

  private int                 cacheMemorySize;

  public TransactionDiskstore(String cacheName, int cacheMemorySize)
  {
    this.cacheName = cacheName;
    this.cacheMemorySize = cacheMemorySize;

    CacheConfiguration cacheConfiguration = new CacheConfiguration(this.cacheName, this.cacheMemorySize);
    // CacheConfiguration cacheConfiguration = new CacheConfiguration();
    cacheConfiguration.setEternal(true);
    cacheConfiguration.setDiskPersistent(false);
    cacheConfiguration.overflowToDisk(true);
    cacheConfiguration.setMaxElementsOnDisk(0);
    cacheConfiguration.setStatistics(ServerProperties.getTransactionCacheStats());
    // cacheConfiguration.setName(this.cacheName);
    // cacheConfiguration.setMaxBytesLocalHeap(this.cacheMemorySize * 1024 *
    // 1024);

    TransactionDiskstore.getTransactionCacheManager().addCache(new Cache(cacheConfiguration));
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

  protected Cache getCache()
  {
    return TransactionDiskstore.getTransactionCacheManager().getCache(this.cacheName);
  }

  /**
   * Initializes the global cache.
   */
  public void removeAll()
  {
    this.getCache().removeAll();
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
    return new HashSet<String>(this.getCache().getKeys());
  }

  public void close()
  {
    if (ServerProperties.getTransactionCacheStats())
    {
      RunwayLogUtil.logToLevel(LogLevel.INFO, this.getCache().getStatistics().toString());
    }

    TransactionDiskstore.getTransactionCacheManager().removeCache(this.cacheName);
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
      Element element = this.getCache().get(id);

      if (element != null)
      {
        return (EntityDAOIF) element.getObjectValue();
      }

      return null;
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
      Element element = new Element(entityDAOIF.getId(), entityDAOIF);

      this.getCache().put(element);
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
  public void removeEntityDAOIFfromCache(String id)
  {
    synchronized (id)
    {
      this.getCache().remove(id);
    }
  }

  public void flush()
  {
    this.getCache().flush();
  }

  @Override
  public void addAll(TransactionStoreIF store)
  {
    Iterator<EntityDAOIF> it = store.getIterator();

    while (it.hasNext())
    {
      this.putEntityDAOIFintoCache(it.next());
    }
  }

  @Override
  public int getCount()
  {
    return this.getCache().getSize();
  }

  @Override
  public Iterator<EntityDAOIF> getIterator()
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isMemoryStore()
  {
    return false;
  }

  private synchronized static CacheManager getTransactionCacheManager()
  {
    if (manager == null)
    {
      String cacheFileLocation = ServerProperties.getTransactionCacheFileLocation();

      DiskStoreConfiguration diskStoreConfiguration = new DiskStoreConfiguration();
      diskStoreConfiguration.setPath(cacheFileLocation);

      Configuration configuration = new Configuration();
      configuration.diskStore(diskStoreConfiguration);
      configuration.setName(TRANSACTION_CACHE_MANAGER);

      manager = new CacheManager(configuration);
      manager.setCacheManagerEventListener(new CacheManagerShutdownListener());
    }

    return manager;
  }

  private synchronized static void setTransactionCacheManager(CacheManager manager)
  {
    TransactionDiskstore.manager = manager;
  }
}
