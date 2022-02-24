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
package com.runwaysdk.util;

import java.util.HashMap;
import java.util.LinkedList;

public class Cache<K, V>
{
  protected int           cacheSize;

  protected HashMap<K, V> map;

  protected LinkedList<K> list;

  public Cache(int cacheSize)
  {
    this.cacheSize = cacheSize;
    this.map = new HashMap<K, V>(cacheSize);
    this.list = new LinkedList<K>();
  }

  public void put(K key, V val)
  {
    // check if pruning is needed
    if (list.size() == this.cacheSize)
    {
      this.prune();
    }

    list.addFirst(key);
    map.put(key, val);
  }

  public V get(K key)
  {
    boolean res = list.remove(key);

    if (res)
    {
      list.addFirst(key);
      return map.get(key);
    }
    return null;
  }

  public void invalidate(Object key)
  {
    list.remove(key);
    map.remove(key);
  }

  public void prune()// removes the tail
  {
    K key = list.removeLast();
    map.remove(key);
  }

  public boolean containsKey(K key)
  {
    return this.map.containsKey(key);
  }

}