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
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;


/**
 *
 * @author nathan
 *
 */
public class CacheAllBusinessDAOstrategy extends CacheAllStrategy
{

  /**
   * 
   */
  private static final long serialVersionUID = 377351044925975832L;

  /**
   *
   * @param classType
   */
  public CacheAllBusinessDAOstrategy(String classType)
  {
    super(classType);
  }

  /**
   *
   * @param mdEntityIF
   */
  public CacheAllBusinessDAOstrategy(MdBusinessDAOIF mdBusinessIF)
  {
    super(mdBusinessIF.definesType());
  }


  /**
   * Reloads the cache of this collection.  The cache is cleared.  All EntityDAOs of the
   *   class stored in this collection are instantiated and put in the cache.
   *
   * <br/><b>Precondition:</b>   true
   *
   * <br/><b>Postcondition:</b>  Cache contains all EntityDAOs of the class that are to be stored in this
   *        collection.
   *
   */
  public synchronized void reload()
  {
    this.removeCacheStrategy();
    
    super.reload();

    BusinessDAOFactory.getBusinessDAOtypeInstances(this.entityType, this);

    reload = false;
  }

  /**
   *
   * @param id
   * @return
   */
  protected EntityDAOIF getFromFactory(String id)
  {
    return BusinessDAOFactory.get(id);
  }

  /**
   *
   * @param type
   * @param key
   * @return
   */
  protected EntityDAOIF getFromFactory(String type, String key)
  {
    return BusinessDAOFactory.get(type, key);
  }

}
