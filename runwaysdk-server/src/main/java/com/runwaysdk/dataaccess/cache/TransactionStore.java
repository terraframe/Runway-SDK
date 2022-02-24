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

import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.cache.globalcache.ehcache.TransactionDiskstore;
import com.runwaysdk.util.IDGenerator;

/**
 * This TransactionStore will wrap either an underlying memory store or a disk store. We first start off with a memory store,
 * which is a performance optimization for small transactions (which are very common). If that memory store gets full we copy
 * it to disk so as to not lose any data.
 * 
 * @author terraframe
 */
public class TransactionStore implements TransactionStoreIF
{
  /**
   * Number of objects which must be in memory before the transaction store
   * switches from an in-memory EntityStore to a on disk EntityStore
   */
  private int                memorySize;

  /**
   * EntityStore being used to store the objects
   */
  private TransactionStoreIF store;

  /**
   * Unique identifier of this EntityStore. This is used for synchronization
   * purposes.
   */
  private String             storeName;

  public TransactionStore(int memorySize)
  {
    this.storeName = IDGenerator.nextID().replaceAll("-", "");
    this.memorySize = memorySize;
    this.store = new TransactionMemorystore();
  }

  @Override
  public EntityDAOIF getEntityDAOIFfromCache(String oid)
  {
    synchronized (this.storeName)
    {
      EntityDAOIF entity = store.getEntityDAOIFfromCache(oid);     
      
      return entity;
    }
  }

  @Override
  public void putEntityDAOIFintoCache(EntityDAOIF entityDAOIF)
  {   
    synchronized (this.storeName)
    {
      if ( this.store instanceof TransactionMemorystore )
      {
        TransactionMemorystore memstore = (TransactionMemorystore) this.store;
        
        if (memstore.getCount() > this.memorySize)
        {
          TransactionDiskstore diskstore = new TransactionDiskstore(this.storeName, this.memorySize);
          memstore.copyToDisk(diskstore);
//          memstore.close();
  
          this.store = diskstore;
        }
      }

      this.store.putEntityDAOIFintoCache(entityDAOIF);
    }
  }

  @Override
  public void removeEntityDAOIFfromCache(String oid)
  {
    synchronized (this.storeName)
    {
      this.store.removeEntityDAOIFfromCache(oid);
    }
  }

  @Override
  public void close()
  {
    this.store.close();
  }

  @Override
  public boolean isEmpty()
  {
    return this.store.isEmpty();
  }
}
