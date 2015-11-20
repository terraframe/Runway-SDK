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

import java.util.Iterator;

import org.ehcache.Cache.Entry;

import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.cache.globalcache.ehcache.TransactionDiskstore;
import com.runwaysdk.util.IDGenerator;

public class TransactionStore implements TransactionStoreIF
{
  /**
   * EntityStore being used to store the objects
   */
  private TransactionStoreIF diskStore;
  
  /**
   * Ehcache's heap storage does not retain object references, so we can't use it for in memory storage. See (github) runway ticket #4.
   */
  private TransactionMemorystore memoryStore;

  /**
   * Unique identifier of this EntityStore. This is used for synchronization
   * purposes.
   */
  private String             storeName;
  
  private int memorySize;

  public TransactionStore(int memorySize)
  {
    this.storeName = IDGenerator.nextID();
    this.diskStore = new TransactionDiskstore(storeName);
    this.memoryStore = new TransactionMemorystore();
    this.memorySize = memorySize;
  }

  @Override
  public EntityDAOIF getEntityDAOIFfromCache(String id)
  {
    synchronized (this.storeName)
    {
      EntityDAOIF entity = memoryStore.getEntityDAOIFfromCache(id);
      
      if (entity == null)
      {
        entity = diskStore.getEntityDAOIFfromCache(id);
      }
      
      return entity;
    }
  }

  @Override
  public void putEntityDAOIFintoCache(EntityDAOIF entityDAOIF)
  {   
    synchronized (this.storeName)
    {
      if (this.memoryStore.getCount() + 1 > memorySize)
      {
        this.diskStore.putEntityDAOIFintoCache(this.memoryStore.removeLast());
      }
      
      this.memoryStore.putEntityDAOIFintoCache(entityDAOIF);
    }
  }

  @Override
  public void removeEntityDAOIFfromCache(String id)
  {
    synchronized (this.storeName)
    {
      this.memoryStore.removeEntityDAOIFfromCache(id);
      this.diskStore.removeEntityDAOIFfromCache(id);
    }
  }

  @Override
  public void close()
  {
    this.memoryStore.close();
    this.diskStore.close();
  }

  @Override
  public boolean isEmpty()
  {
    return this.memoryStore.isEmpty() && this.diskStore.isEmpty();
  }
}
