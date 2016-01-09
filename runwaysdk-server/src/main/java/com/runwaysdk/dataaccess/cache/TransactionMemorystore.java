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
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.cache.globalcache.ehcache.TransactionDiskstore;

public class TransactionMemorystore implements TransactionStoreIF
{
  private SortedMap<String, EntityDAOIF> entityMap;

  public TransactionMemorystore()
  {
    this.entityMap = Collections.synchronizedSortedMap(new TreeMap<String, EntityDAOIF>());
  }

  @Override
  public void close()
  {
    this.entityMap.clear();
  }

  public int getCount()
  {
    return this.entityMap.size();
  }

  @Override
  public EntityDAOIF getEntityDAOIFfromCache(String id)
  {
    return this.entityMap.get(id);
  }

  @Override
  public void putEntityDAOIFintoCache(EntityDAOIF entityDAOIF)
  {    
    this.entityMap.put(entityDAOIF.getId(), entityDAOIF);
  }

  @Override
  public void removeEntityDAOIFfromCache(String id)
  {
    this.entityMap.remove(id);
  }
  
  public EntityDAOIF removeLast()
  {
    return this.entityMap.remove(this.entityMap.lastKey());
  }
  
  public void copyToDisk(TransactionDiskstore disk)
  {
    Iterator<EntityDAOIF> it = this.entityMap.values().iterator();
    while (it.hasNext())
    {
      EntityDAOIF ent = it.next();
      disk.putEntityDAOIFintoCache(ent);
    }
  }
  
  @Override
  public boolean isEmpty()
  {
    return this.getCount() == 0;
  }
}
