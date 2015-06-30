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
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.database.StructDAOFactory;

public class CacheAllStructDAOStrategy extends CacheAllStrategy
{
  /**
   * 
   */
  private static final long serialVersionUID = -1365847381748906682L;

  /**
   *
   * @param mdEntityIF
   */
  public CacheAllStructDAOStrategy(MdStructDAOIF mdStructIF)
  {
    super(mdStructIF.definesType());
  }

  /**
   * Reloads the cache of this collection.  The cache is cleared.  All StructDAOs of the
   *   class stored in this collection are instantiated and put in the cache.
   *
   * <br/><b>Precondition:</b>   true
   *
   * <br/><b>Postcondition:</b>  Cache contains all StructDAOs of the class that are to be stored in this
   *        collection.
   *
   */
  public synchronized void reload()
  {
    this.removeCacheStrategy();
    
    super.reload();

    StructDAOFactory.getStructDAOTypeInstances(this.entityType, this);

    reload = false;
  }

  /**
   *
   * @param id
   * @return
   */
  protected EntityDAOIF getFromFactory(String id)
  {
    return StructDAOFactory.get(id);
  }

  /**
   *
   * @param type
   * @param key
   * @return
   */
  protected EntityDAOIF getFromFactory(String type, String key)
  {
    return StructDAOFactory.get(type, key);
  }
}
