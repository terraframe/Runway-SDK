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
package com.runwaysdk.mobile;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class RunwayMemoryCache<K, V>
{
  private Map<K, V> impl;
  private final int maxSize;
  
  public RunwayMemoryCache(int maxSize) {
    impl = new LinkedHashMapWrapper<K, V>();
    this.maxSize = maxSize;
  }
  
  public void put(K key, V value) {
    impl.put(key, value);
    onPut(key, value);
  }
  
  public V get(K key) {
    V value = impl.get(key);
    
    if (value == null) {
      value = onMiss(key);
    }
    
    return value;
  }
  
  public V remove(K key) {
    return impl.remove(key);
  }
  
  public int getMaxSize() {
    return maxSize;
  }
  
  public boolean containsKey(K key) {
    return impl.containsKey(key);
  }
  
  protected abstract void onPut(K key, V value);
  protected abstract V onMiss(K key);
  
  @SuppressWarnings("hiding")
  private class LinkedHashMapWrapper<K,V> extends LinkedHashMap<K,V> {
    /**
     * 
     */
    private static final long serialVersionUID = -1140116846915580255L;

    private LinkedHashMapWrapper() { super(); }
    
    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
      return size() > maxSize;
    }
  }
}
