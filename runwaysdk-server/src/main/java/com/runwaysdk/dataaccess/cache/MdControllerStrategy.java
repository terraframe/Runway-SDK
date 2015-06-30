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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.runwaysdk.constants.MdControllerInfo;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.MdControllerDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdControllerDAO;
import com.runwaysdk.dataaccess.metadata.MdElementDAO;

public class MdControllerStrategy extends MetaDataObjectStrategy
{
  /**
   * 
   */
  private static final long serialVersionUID = -4071096141689162041L;
  
  /**
   * Maps facade types to the IDs of the <code>MdControllerDAO</code> objects that define them. <br/><b>invariant</b>
   * this.mdControllerMap.size() == this.entityDAOMap.size() <br/><b>invariant</b>
   * this.mdControllerMap references the same object ids as this.entityDAOMap (i.e.
   * they are in-sync)
   * 
   * Key: Type
   * Value: IDs of <code>MdControllerDAO</code> 
   */
  private Map<String, String> mdControllerIdMap;

  /**
   * Initializes the EntityDAOCollection with the all MdController objects.
   *
   * <br/><b>Precondition:</b> classType != null <br/><b>Precondition:</b>
   * !classType.trim().equals("")
   *
   * @param classType name of the type of the objects in this collection
   */
  public MdControllerStrategy(String classType)
  {
    super(classType);

    this.mdControllerIdMap = new HashMap<String, String>();
  }

  /**
   * Returns an id of a <code>MdControllerDAOIF</code> that defines the facade with the given type. Note that
   * this method <i>will</i> return <code><b>null</b></code> if the type is
   * invalid (cannot be found). It is the responsibility of the caller to
   * account for a <code><b>null</b></code> scenario.
   *
   * <br/><b>Precondition:</b> facadeType != null <br/><b>Precondition:</b>
   * !facadeType.trim().equals("") <br/><b>Postcondition:</b> Returns a
   * MdControllerIF where (mdController.getType().equals(classType)
   *
   * @param type Name of the entity.
   * @return id of a <code>MdControllerDAOIF</code> that defines the facade with the given type.
   */
  public synchronized String getMdControllerId(String type)
  {
    if (reload == true)
    {
      this.reload();
    }

    String mdControllerId = this.getMdControllerIdReturnNull(type);
    if (mdControllerId == null)
    {
      String error = "The ["+MdControllerInfo.CLASS+" ] that defines [" + type + "] was not found.";
      throw new DataNotFoundException(error, MdElementDAO.getMdElementDAO(MdControllerInfo.CLASS));
    }

    return mdControllerId;
  }
  
  /**
   * Returns null if the type is unknown.
   * 
   * @param type
   * @return
   */
  public synchronized String getMdControllerIdReturnNull(String type)
  {
    if (reload == true)
    {
      this.reload();
    }

    return this.mdControllerIdMap.get(type);
  }

  /**
   * Reloads the cache of this collection. The cache is cleared. All EntityDAOs
   * of this type stored in this collection are instantiated an put in the
   * cache. This method uses the database instead of the metadata cache.
   *
   * <br/><b>Precondition:</b> true
   *
   * <br/><b>Postcondition:</b> Cache contains all EntityDAOs of this type
   * that are to be stored in this collection
   *
   */
  public synchronized void reload()
  {
    this.removeCacheStrategy();
    
    this.entityDAOIdSet.clear();
    this.entityDAOIdByKeyMap.clear();
    
    this.mdControllerIdMap.clear();

    super.reload();

    reload = false;
  }

  /**
   * Returns a list of sub types(including this type).
   * @return a list of sub types(including this type).
   */
  protected List<String> getSubTypesFromDatabase()
  {
    return Database.getConcreteSubClasses(MdControllerInfo.ID_VALUE);
  }

  /**
   * Places the given EntityDAO into the cache.
   *
   * <br/><b>Precondition:</b> entityDAO != null <br/><b>Precondition:</b>
   * entityDAO.getTypeName().equals(MdControllerIF.CLASS)
   *
   * <br/><b>Postcondition:</b> cache contains the given EntityDAO
   *
   * @param entityDAO EntityDAO to add to this collection
   */
  public void updateCache(EntityDAO entityDAO)
  {
    synchronized(entityDAO.getId())
    {
      super.updateCache(entityDAO);

      MdControllerDAO mdController = (MdControllerDAO) entityDAO;
      this.mdControllerIdMap.put(mdController.definesType(), mdController.getId());
    }
  }

  /**
   * Removes the given EntityDAO from the cache.
   *
   * <br/><b>Precondition:</b> mdEntity != null <br/><b>Precondition:</b>
   * mdEntity.getTypeName().equals(MdControllerIF.CLASS)
   *
   * <br/><b>Postcondition:</b> cache no longer contains the given EntityDAO
   *
   * @param entityDAO EntityDAO to remove from this collection
   */
  public void removeCache(EntityDAO entityDAO)
  {
    synchronized(entityDAO.getId())
    {
      super.removeCache(entityDAO);

      this.mdControllerIdMap.remove( ( (MdControllerDAOIF) entityDAO ).definesType());
    }
  }
}
