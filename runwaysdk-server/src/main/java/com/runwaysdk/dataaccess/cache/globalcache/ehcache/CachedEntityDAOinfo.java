/**
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
 */
package com.runwaysdk.dataaccess.cache.globalcache.ehcache;

import java.io.Serializable;

import com.runwaysdk.constants.BusinessInfo;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.dataaccess.EntityDAOIF;

/**
 * Caches an {@link EntityDAOIF} with its parent and child relationships. Some
 * of the fields may be null, as not all fields are necessarily cached.
 * Relationships could be cached but the actual {@link EntityDAOIF} may not be.
 * If all fields are null, then this object should be removed from the global
 * cache.
 * 
 * Access to this class should be synchronized on the id of the
 * {@link EntityDAOIF}.
 * 
 * @author nathan
 * 
 */
public class CachedEntityDAOinfo implements Serializable
{

  /**
   * True if this entry in the cache has been marked for deletion, false
   * otherwise. Some cache implementations do not remove items immediately, and
   * we need another mechanism for specifying whether an item has been deleted.
   */
  private boolean           isMarkedForDelete;

  /**
   * 
   */
  private static final long serialVersionUID = 5933649229875133282L;

  private EntityDAOIF       entityDAOIF;

  public CachedEntityDAOinfo()
  {
    this.entityDAOIF = null;

    this.isMarkedForDelete = false;
  }

  /**
   * True if this entry in the cache has been marked for deletion, false
   * otherwise. Some cache implementations do not remove items immediately, and
   * we need another mechanism for specifying whether an item has been deleted.
   */

  public boolean isMarkedForDelete()
  {
    return this.isMarkedForDelete;
  }

  /**
   * Indicates this object has been marked for deletion in the cache.
   * 
   */
  public void setMarkedForDelete()
  {
    this.isMarkedForDelete = true;
  }

  /**
   * Returns true if no information about the {@link EntityDAOIF} is stored,
   * false otherwise. If no information on this object is stored, then it should
   * be removed from the global cache.
   * 
   * @return
   */
  public boolean removeFromGlobalCache()
  {
    if (this.entityDAOIF == null)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  /**
   * Called when the {@link EntityDAOIF} is added or updated in the cache.
   * 
   * @param _entityDAOIF
   */
  public void addEntityDAOIF(EntityDAOIF _entityDAOIF)
  {
    this.entityDAOIF = _entityDAOIF;
  }

  /**
   * Returns the {@link EntityDAOIF} on this object, or null if there is none.
   * 
   * @return {@link EntityDAOIF} on this object, or null if there is none.
   * 
   */
  public EntityDAOIF getEntityDAOIF()
  {
    return this.entityDAOIF;
  }

  /**
   * Called when the {@link EntityDAOIF} is removed from the cache.
   */
  public void removeEntityDAOIF()
  {
    this.entityDAOIF = null;
  }

  public String getId()
  {
    return this.entityDAOIF.getId();
  }
  
  
  public enum Types
  {
    ENTITY(EntityInfo.CLASS),
    
    BUSINESS(BusinessInfo.CLASS);
    
    private String type;
    
    private Types(String type)
    {
      this.type = type;
    }
    
    public CachedEntityDAOinfo createInfo()
    {
      if (this.type.equals(BusinessInfo.CLASS))
      {
        return new CachedBusinessDAOinfo();
      }
      else
      {
        return new CachedEntityDAOinfo();
      }
    }
  }
}
