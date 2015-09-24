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

import com.runwaysdk.constants.MdAttributeInfo;
import com.runwaysdk.constants.MdIndexInfo;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdElementDAO;


public class MdAttributeStrategy extends MetaDataObjectStrategy
{

  /**
   * 
   */
  private static final long serialVersionUID = -1886554497544272985L;

  /**
   * Maps database index names to the MdAttribute objects that define them.
   * The key is the index name and the value is the MdAttribute that defines it.
   */
  private Map<String, String> indexNameMap;

  /**
   *
   * @param classType
   */
  public MdAttributeStrategy(String classType)
  {
    super(classType);
    this.indexNameMap = new HashMap<String, String>();
  }

  /**
   * Returns a MdAttributeIF that uses the database index of a given name.
   *
   * <br/><b>Precondition:</b>  indexName != null
   * <br/><b>Precondition:</b>  !indexName.trim().equals("")
   * <br/><b>Postcondition:</b> Returns a MdAttributeIF where
   *                            (MdAttributeIF.getIndexName().equals(indexName)
   *
   * @param  indexName Name of the database index.
   * @return MdAttributeIF that uses the database index of a given name.
   */
  public synchronized MdAttributeConcreteDAOIF getMdAttributeWithIndex(String indexName)
  {
    if (reload == true)
    {
      this.reload();
    }

    String mdAttrID = this.indexNameMap.get(indexName);

    if (mdAttrID == null)
    {
      String error = "The no ["+MdAttributeConcreteDAO.class+"] uses a database index with name [" + indexName + "].";
      throw new DataNotFoundException(error, MdElementDAO.getMdElementDAO(MdIndexInfo.CLASS));
    }
    else
    {
      return (MdAttributeConcreteDAOIF)ObjectCache.getEntityDAOIFfromCache(mdAttrID);
    }
  }

  /**
   * Returns a MdAttributeIF that uses the given key name, or null if none
   *
   * <br/><b>Precondition:</b>  key != null
   * <br/><b>Precondition:</b>  !key.trim().equals("")
   * <br/><b>Postcondition:</b> Returns a MdAttributeIF where
   *                            (MdAttributeIF.getKey().equals(key)
   *
   * @param  key
   * @return MdAttributeIF that uses the given key name, or null if none
   */
  public synchronized MdAttributeDAOIF getMdAttributeWithKey(String key)
  {
    if (reload == true)
    {
      this.reload();
    }
    
    String mdAttrID = this.entityDAOIdByKeyMap.get(key);   
    
    if (mdAttrID != null)
    {
      return (MdAttributeDAOIF)ObjectCache.getEntityDAOIFfromCache(mdAttrID); 
    }
    else
    {
      return null;
    }
  }

  /**
   * Populates the cache with all attribute metadata BusinessDAOs
   *  that are defined in the database.
   *
   * <br/><b>Precondition:</b>  true
   *
   * <br/><b>Postcondition:</b> cache contains BusinessDAOs of all attribute metadata BusinessDAOs
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
    return Database.getConcreteSubClasses(MdAttributeInfo.ID_VALUE);
  }

  /**
   * Places the given EntityDAO into the cache.
   *
   * <br/><b>Precondition:</b>  EntityDAO != null
   *
   * <br/><b>Postcondition:</b> cache contains the given EntityDAO
   *
   * @param  entityDAO to add to this collection
   */
  public void updateCache(EntityDAO entityDAO)
  {
    String syncId;
    String oldId;
    boolean idHasChanged = entityDAO.hasIdChanged();
    if (idHasChanged)
    {
      syncId = entityDAO.getOldId();
      oldId = entityDAO.getOldId();
    }
    else
    {
      syncId = entityDAO.getId();
      oldId = entityDAO.getId();
    }
    
    synchronized(syncId)
    {
      super.updateCache(entityDAO);

      MdAttributeDAO mdAttribute = (MdAttributeDAO)entityDAO;

      if (mdAttribute instanceof MdAttributeConcreteDAOIF)
      {
        MdAttributeConcreteDAO mdAttributeConcrete = (MdAttributeConcreteDAO)mdAttribute;

        if (idHasChanged)
        {
          this.indexNameMap.remove(oldId);
        }
        
        if (!mdAttributeConcrete.getIndexName().trim().equals(""))
        {         
          this.indexNameMap.put(mdAttributeConcrete.getIndexName(), mdAttributeConcrete.getId());
        }
      }
    }
  }

  /**
   * Removes the given EntityDAO from the cache.
   *
   * <br/><b>Precondition:</b>  EntityDAO != null
   *
   * <br/><b>Postcondition:</b> cache no longer contains the given EntityDAO
   *
   * @param  EntityDAO to remove from this collection
   */
  public void removeCache(EntityDAO entityDAO)
  {
    synchronized(entityDAO.getId())
    {
      super.removeCache(entityDAO);

      MdAttributeDAO mdAttribute = (MdAttributeDAO)entityDAO;

      if (mdAttribute instanceof MdAttributeConcreteDAOIF)
      {
        MdAttributeConcreteDAO mdAttributeConcrete = (MdAttributeConcreteDAO)mdAttribute;

        if (!mdAttributeConcrete.getIndexName().trim().equals(""))
        {
          this.indexNameMap.remove(mdAttributeConcrete.getIndexName());
        }
      }
    }
  }
}
