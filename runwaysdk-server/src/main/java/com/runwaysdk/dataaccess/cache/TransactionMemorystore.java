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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.runwaysdk.dataaccess.EntityDAOIF;

public class TransactionMemorystore implements TransactionStoreIF
{
  private Map<String, EntityDAOIF> entityMap;

  public TransactionMemorystore()
  {
    this.entityMap = Collections.synchronizedMap(new HashMap<String, EntityDAOIF>());
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
  public void close()
  {
  }

  @Override
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
  public Iterator<EntityDAOIF> getIterator()
  {
    return this.entityMap.values().iterator();
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
  
  @Override
  public boolean isMemoryStore()
  {
    return true;
  }
}
