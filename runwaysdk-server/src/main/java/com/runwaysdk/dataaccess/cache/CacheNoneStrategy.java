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
package com.runwaysdk.dataaccess.cache;

import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.util.IdParser;


public abstract class CacheNoneStrategy extends CacheStrategy
{

  /**
   * 
   */
  private static final long serialVersionUID = 5943037764713439773L;

  /**
   *
   * @param type
   */
  public CacheNoneStrategy(String type)
  {
    super(type);
  }

  /**
   * Retrieves the EntityDAO with the given id from the collection.  The
   * database is queried since this collection does not cache any EntityDAOs.
   *
   * <br/><b>Precondition:</b>  id != null
   * <br/><b>Precondition:</b>  !id.trim().equals("")
   * <br/><b>Precondition:</b>  id is a valid id of a EntityDAO
   * <br/><b>Postcondition:</b> return value may not be null
   *
   * @param  id of the desire EntityDAO
   * @return EntityDAO with the given id
   * @throws DataAccessException a EntityDAO with the given id does not exist
   *         in the database
   */
  public EntityDAOIF getEntityInstance(String id)
  {
    EntityDAOIF returnEntityDAO = null;

    returnEntityDAO = this.getFromFactory(id);

    if (returnEntityDAO == null)
    {
      MdClassDAOIF metadata = MdClassDAO.getMdClassByRootId(IdParser.parseMdTypeRootIdFromId(id));

      String errMsg = "An instance of [" + metadata.definesType() + "] with id [" + id
        + "] could not be found. [" + metadata.definesType() + "] caches none.";

      throw new DataNotFoundException(errMsg, metadata);
    }
    return returnEntityDAO;
  }


  /**
   * Retrieves the EntityDAO with the given type and keyname from the collection.  If
   *  the cache does not contain a EntityDAO with the given type and keyname, then
   *  the database will be queried.  If a EntityDAO with the type and keyname is found
   *  in the database, then it is added to the collection.
   *
   * <br/><b>Precondition:</b>  type != null
   * <br/><b>Precondition:</b>  !type.trim().equals("")
   * <br/><b>Postcondition:</b> return value may not be null
   *
   * <br/><b>Precondition:</b>  key != null
   * <br/><b>Precondition:</b>  !key.trim().equals("")
   * <br/><b>Postcondition:</b> return value may not be null
   *
   * @param  type of the desired EntityDAO
   * @param  key of the desired EntityDAO
   * @return EntityDAO with the given type and keyname
   * @throws DataAccessException a EntityDAO with the given type and keyname does not exist
   *         in the database and the cache
   */
  public synchronized EntityDAOIF getEntityInstance(String type, String key)
  {
    EntityDAOIF returnEntityDAO = null;

    returnEntityDAO = this.getFromFactory(type, key);

    if (returnEntityDAO == null)
    {
      MdClassDAOIF metadata = MdClassDAO.getMdClassDAO(type);

      String errMsg = "An instance of [" + metadata.definesType() + "] with key [" + key
        + "] could not be found. [" + metadata.definesType() + "] caches none.";

      throw new DataNotFoundException(errMsg, metadata);
    }
    return returnEntityDAO;
  }

  /**
   * This collection does not cache anything, so this method does nothing.
   *
   * <br/><b>Precondition:</b>   true
   * <br/><b>Postcondition:</b>  this.reload == false
   */
  public void reload()
  {
    super.reload();
    this.reload = false;
  }

  /**
   * This collection does not cache anything, so this method does nothing.
   *
   * <br/><b>Precondition:</b>   true
   * <br/><b>Postcondition:</b>  true
   */
  public void updateCache(EntityDAO entityDAO) {}
  
  /**
   * Updates the changed id for the given {@link EntityDAOIF} in the cache.
   *
   * This collection does not cache anything, so this method does nothing.
   *
   * <br/><b>Precondition:</b>   true
   * <br/><b>Postcondition:</b>  true
   */
  public void updateCacheWithNewId(String oldId, EntityDAO entityDAO) {}

  /**
   * This collection does not cache anything, so this method does nothing.
   *
   * <br/><b>Precondition:</b>   true
   * <br/><b>Postcondition:</b>  true
   */
  public void removeCache(EntityDAO entityDAO) {}

  /**
   * This collection does not cache anything, so this method does nothing.
   *
   * <br/><b>Precondition:</b>   true
   * <br/><b>Postcondition:</b>  true
   */
  public void clearCacheForRefresh(String entityId) {}
  
}
