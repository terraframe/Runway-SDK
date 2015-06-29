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

import com.runwaysdk.constants.MdIndexInfo;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.MdIndexDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdElementDAO;
import com.runwaysdk.dataaccess.metadata.MdIndexDAO;

public class MdIndexStrategy extends MetaDataObjectStrategy
{
  /**
   * 
   */
  private static final long serialVersionUID = -4620044332335351809L;
  
  /**
   * Maps database index names to the MdIndex objects that define them.
   * The key is the index name and the value is the MdIndex that defines it.
   */
  private HashMap<String, MdIndexDAO> indexNameMap;

  /**
   * Initializes the EntityDAOCollection with the all MdIndex objects.
   *
   * <br/><b>Precondition:</b>  classType != null
   * <br/><b>Precondition:</b>  !classType.trim().equals("")
   *
   * @param classType name of the type of the objects in this collection )
   */
  public MdIndexStrategy(String classType)
  {
    super(classType);
    this.indexNameMap = new HashMap<String, MdIndexDAO>();
  }

  /**
   * Returns a MdIndex object that defines the index with the given name in the database.
   *
   * <br/><b>Precondition:</b>  indexName != null
   * <br/><b>Precondition:</b>  !indexName.trim().equals("")
   * <br/><b>Postcondition:</b> Returns a MdIndexIF where
   *                            (mdIndexIF.getIndexName().equals(indexName)
   *
   * @param  indexName Name of the database index.
   * @return MdIndex object that defines the index with the given name in the database.
   */
  public synchronized MdIndexDAOIF getMdIndex(String indexName)
  {
    if (reload == true)
    {
      this.reload();
    }

    MdIndexDAO mdIndex = this.indexNameMap.get(indexName);
    if (mdIndex==null)
    {
      String error = "The MdIndex that defines the database index [" + indexName + "] was not found.";
      throw new DataNotFoundException(error, MdElementDAO.getMdElementDAO(MdIndexInfo.CLASS));
    }

    return mdIndex;
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
    this.indexNameMap.clear();

    super.reload();

    reload = false;
  }

  /**
   * Returns a list of sub types(including this type).
   * @return a list of sub types(including this type).
   */
  protected List<String> getSubTypesFromDatabase()
  {
    return Database.getConcreteSubClasses(MdIndexInfo.ID_VALUE);
  }

  /**
   * Places the given EntityDAO into the cache.
   *
   * <br/><b>Precondition:</b>  entityDAO != null
   * <br/><b>Precondition:</b>  entityDAO.getTypeName().equals(MdIndexInfo.CLASS)
   *
   * <br/><b>Postcondition:</b> cache contains the given EntityDAO
   *
   * @param  mdIndex EntityDAO to add to this collection
   */
  public void updateCache(EntityDAO entityDAO)
  {
    synchronized(entityDAO.getId())
    {
      super.updateCache(entityDAO);

      MdIndexDAO mdIndex = (MdIndexDAO) entityDAO;
      this.indexNameMap.put(mdIndex.getIndexName(), mdIndex);
    }
  }

  /**
   * Removes the given EntityDAO from the cache.
   *
   * <br/><b>Precondition:</b>  entityDAO != null
   * <br/><b>Precondition:</b>  entityDAO.getTypeName().equals(MdIndexInfo.CLASS)
   *
   * <br/><b>Postcondition:</b> cache no longer contains the given EntityDAO
   *
   * @param  mdEntity EntityDAO to remove from this collection
   */
  public void removeCache(EntityDAO entityDAO)
  {
    synchronized(entityDAO.getId())
    {
      super.removeCache(entityDAO);

      MdIndexDAO mdIndex = (MdIndexDAO) entityDAO;

      this.indexNameMap.remove(mdIndex.getIndexName());
    }
  }
}
