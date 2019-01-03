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
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;


public class CacheNoneBusinessDAOStrategy extends CacheNoneStrategy
{

  /**
   * 
   */
  private static final long serialVersionUID = -1065624514519079738L;

  /**
   *
   * @param classType
   */
  public CacheNoneBusinessDAOStrategy(String classType)
  {
    super(classType);
  }

  /**
   *
   * @param oid
   * @return
   */
  protected EntityDAOIF getFromFactory(String oid)
  {
    return BusinessDAOFactory.get(oid);
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
