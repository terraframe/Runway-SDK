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

import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.cache.globalcache.ehcache.TransactionDiskstore;
import com.runwaysdk.util.IDGenerator;

/**
 * This TransactionStore will wrap either an underlying memory store or a disk store. We first start off with a memory store,
 * which is a performance optimization for small transactions (which are very common). If that memory store gets full we copy
 * it to disk so as to not lose any data.
 * 
 * @author rrowlands
 */
public class TransactionStore implements TransactionStoreIF
{
  /**
   * Number of objects which must be in memory before the transaction store
   * switches from an in-memory EntityStore to a on disk EntityStore
   */
  private int                memorySize;

  private TransactionMemorystore memStore;
  
  /**
   * This will be null if this.memStore.getCount() < memorySize
   */
  private TransactionDiskstore diskStore;

  /**
   * Unique identifier of this EntityStore. This is used for synchronization
   * purposes.
   */
  private String             storeName;

  public TransactionStore(int memorySize)
  {
    this.storeName = IDGenerator.nextID();
    this.memorySize = memorySize;
    this.memStore = new TransactionMemorystore();
  }

  @Override
  public EntityDAOIF getEntityDAOIFfromCache(String id)
  {
    synchronized (this.storeName)
    {
      if (this.memStore.containsKey(id))
      {
        return this.memStore.getEntityDAOIFfromCache(id);
      }
      if (this.diskStore != null)
      {
        return this.diskStore.getEntityDAOIFfromCache(id);
      }
      
      return null;
    }
  }

  @Override
  public void putEntityDAOIFintoCache(EntityDAOIF entityDAOIF)
  {
    synchronized (this.storeName)
    {
      if ( this.diskStore == null && this.memStore.getCount() > this.memorySize )
      {
        this.diskStore = new TransactionDiskstore(this.storeName);
        this.memStore.copyToDisk(this.diskStore);
        this.memStore.removeLast();
        this.diskStore.putEntityDAOIFintoCache(entityDAOIF);
      }
      else if ( this.diskStore != null )
      {
        this.memStore.removeLast();
        this.diskStore.putEntityDAOIFintoCache(entityDAOIF);
      }

      this.memStore.putEntityDAOIFintoCache(entityDAOIF);
    }
  }

  @Override
  public void removeEntityDAOIFfromCache(String id)
  {
    synchronized (this.storeName)
    {
      this.memStore.removeEntityDAOIFfromCache(id);
      
      if (this.diskStore != null)
      {
        this.diskStore.removeEntityDAOIFfromCache(id);
      }
    }
  }

  @Override
  public void close()
  {
    this.memStore.close();
    if (this.diskStore != null)
    {
      this.diskStore.close();
      this.diskStore = null;
    }
  }

  @Override
  public boolean isEmpty()
  {
    if (this.diskStore != null)
    {
      return this.diskStore.isEmpty() && this.memStore.isEmpty();
    }
    else
    {
      return this.memStore.isEmpty();
    }
  }
}
