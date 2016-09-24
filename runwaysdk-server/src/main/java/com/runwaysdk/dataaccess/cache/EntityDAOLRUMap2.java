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

import java.util.LinkedHashMap;
import java.util.Map;

public class EntityDAOLRUMap2 extends LinkedHashMap
{
  /**
   * 
   */
  private static final long serialVersionUID = 2907032866669212506L;
  private int maxSize;
  
  public EntityDAOLRUMap2(int initialCapacity, float loadFactor, boolean accessOrder)
  {
    super(initialCapacity, loadFactor, accessOrder);
    this.maxSize = initialCapacity;
  }
  
  
  public boolean removeEldestEntry(@SuppressWarnings("rawtypes") Map.Entry eldest)
  { 
    if (this.size() > this.maxSize)
    {
      ObjectCache.removeEntityDAOIFfromCache((String)eldest.getKey(), false);
      return true;
    }
    else
    {
      return false;
    }
  }
  
}
