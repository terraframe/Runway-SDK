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

import java.util.HashMap;
import java.util.List;

import com.runwaysdk.constants.MdFacadeInfo;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.MdFacadeDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdElementDAO;
import com.runwaysdk.dataaccess.metadata.MdFacadeDAO;

public class MdFacadeStrategy extends MetaDataObjectStrategy
{
  /**
   * 
   */
  private static final long serialVersionUID = -922211002965423401L;

  /**
   * Maps facade types to the MdFacade objects that define them.
   * <br/><b>invariant</b> this.mdFacadeMap.size() == this.entityDAOMap.size()
   * <br/><b>invariant</b> this.mdFacadeMap references the same objects as this.entityDAOMap
   *                       (i.e. they are in-sync)
   */
  private HashMap<String, MdFacadeDAO> mdFacadeMap;


  /**
   * Initializes the EntityDAOCollection with the all MdFacade objects.
   *
   * <br/><b>Precondition:</b>  classType != null
   * <br/><b>Precondition:</b>  !classType.trim().equals("")
   *
   * @param classType name of the type of the objects in this collection
   * @param bucketSize
   */
  public MdFacadeStrategy(String classType)
  {
    super(classType);
    this.mdFacadeMap = new HashMap<String, MdFacadeDAO>();
  }

  /**
   * Returns a <code>MdFacadeDAOIF</code> that defines the facade with the given type.
   * Note that this method <i>will</i> return <code><b>null</b></code> if
   * the type is invalid (cannot be found). It is the responsibility of the
   * caller to account for a <code><b>null</b></code> scenario.
   *
   * <br/><b>Precondition:</b>  facadeType != null
   * <br/><b>Precondition:</b>  !facadeType.trim().equals("")
   * <br/><b>Postcondition:</b> Returns a MdFacadeIF where
   *                            (mdFacade.getType().equals(classType)
   *
   * @param  facadeType Name of the entity.
   * @return <code>MdFacadeDAOIF</code> that defines the facade with the given type.
   */
  public synchronized MdFacadeDAOIF getMdFacade(String facadeType)
  {
    if (reload == true)
    {
      this.reload();
    }

    MdFacadeDAOIF mdFacade = this.getMdFacadeReturnNull(facadeType);
    if (mdFacade==null)
    {
      String error = "The MdFacade that defines [" + facadeType + "] was not found.";
      throw new DataNotFoundException(error, MdElementDAO.getMdElementDAO(MdFacadeInfo.CLASS));
    }

    return mdFacade;
  }

  /**
   * Returns null if type type is unknown.
   * 
   * @param facadeType
   * @return
   */
  public synchronized MdFacadeDAOIF getMdFacadeReturnNull(String facadeType)
  {
    if (reload == true)
    {
      this.reload();
    }

    return this.mdFacadeMap.get(facadeType);
  }

  
  
  /**
   *Reloads the cache of this collection.  The cache is cleared.  All EntityDAOs of this
   * type stored in this collection are instantiated an put in the cache.  This method
   * uses the database instead of the metadata cache.
   *
   * <br/><b>Precondition:</b>   true
   *
   * <br/><b>Postcondition:</b>  Cache contains all EntityDAOs of this type that are to be stored in this
   *        collection
   *
   */
  public synchronized void reload()
  {
    this.removeCacheStrategy();
    
    this.entityDAOIdSet.clear();
    this.entityDAOIdByKeyMap.clear();
    this.mdFacadeMap.clear();

    super.reload();

    reload = false;
  }

  /**
   * Returns a list of sub types(including this type).
   * @return a list of sub types(including this type).
   */
  protected List<String> getSubTypesFromDatabase()
  {
    return Database.getConcreteSubClasses(MdFacadeInfo.ID_VALUE);
  }

  /**
   * Places the given EntityDAO into the cache.
   *
   * <br/><b>Precondition:</b>  entityDAO != null
   * <br/><b>Precondition:</b>  entityDAO.getTypeName().equals(MdFacadeIF.CLASS)
   *
   * <br/><b>Postcondition:</b> cache contains the given EntityDAO
   *
   * @param  entityDAO EntityDAO to add to this collection
   */
  public void updateCache(EntityDAO entityDAO)
  {
    synchronized(entityDAO.getId())
    {
      super.updateCache(entityDAO);

      MdFacadeDAO mdFacade = (MdFacadeDAO) entityDAO;
      this.mdFacadeMap.put(mdFacade.definesType(), mdFacade);
    }
  }

  /**
   * Removes the given EntityDAO from the cache.
   *
   * <br/><b>Precondition:</b>  mdEntity != null
   * <br/><b>Precondition:</b>  mdEntity.getTypeName().equals(MdFacadeIF.CLASS)
   *
   * <br/><b>Postcondition:</b> cache no longer contains the given EntityDAO
   *
   * @param  entityDAO EntityDAO to remove from this collection
   */
  public void removeCache(EntityDAO entityDAO)
  {
    synchronized(entityDAO.getId())
    {
      super.removeCache(entityDAO);

      this.mdFacadeMap.remove(((MdFacadeDAOIF)entityDAO).definesType());
    }
  }

}
