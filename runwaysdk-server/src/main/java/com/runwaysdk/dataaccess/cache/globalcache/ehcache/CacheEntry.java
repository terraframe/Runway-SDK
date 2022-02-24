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
package com.runwaysdk.dataaccess.cache.globalcache.ehcache;

import java.io.Serializable;
import java.util.Map;

import com.runwaysdk.dataaccess.cache.CacheStrategy;

public class CacheEntry implements Serializable
{
  private static final long serialVersionUID = -4393595344924817752L;

  private CachedEntityDAOinfo entityDAO;
  
  private Map<String, CacheStrategy> collectionMap;
  
  public CacheEntry(CachedEntityDAOinfo entityDAO)
  {
    this.entityDAO = entityDAO;
  }
  
  public CacheEntry(Map<String, CacheStrategy> collectionMap)
  {
    this.collectionMap = collectionMap;
  }

  /**
   * @return the entityDAO
   */
  public CachedEntityDAOinfo getEntityDAO()
  {
    return entityDAO;
  }

  /**
   * @param entityDAO the entityDAO to set
   */
  public void setEntityDAO(CachedEntityDAOinfo entityDAO)
  {
    this.entityDAO = entityDAO;
  }

  /**
   * @return the collectionMap
   */
  public Map<String, CacheStrategy> getCollectionMap()
  {
    return collectionMap;
  }

  /**
   * @param collectionMap the collectionMap to set
   */
  public void setCollectionMap(Map<String, CacheStrategy> collectionMap)
  {
    this.collectionMap = collectionMap;
  }
  
  
}
