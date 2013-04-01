/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.dataaccess.cache;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.transaction.TransactionItem;

/**
 * 
 * @author nathan
 * 
 */
public abstract class CacheStrategy implements TransactionItem, Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = -532344721677986160L;

  /**
   * Indicates if the collection should be refreshed. If set to true, then the
   * cached collection will be refreshed.
   **/
  protected volatile boolean    reload;

  /**
   * Data structure used to store ids of {@link EntityDAO}s that should be
   * cached according to this collection. <br/>
   * <b>invariant</b> entityDAOIdSet != null
   **/
  protected Set<String>         entityDAOIdSet;

  /**
   * Data structure used to store key of {@link EntityDAO}s that should be
   * cached according to this collection. The value is the id of the object. <br/>
   * <b>invariant</b> entityDAOIdByKeyMap != null
   * 
   * Key: object keyname Value: obectId
   **/
  protected Map<String, String> entityDAOIdByKeyMap;

  /**
   * Name of the class that this collection manages <br/>
   * <b>invariant</b> entityType != null, !entityType.trim.equals("")
   **/
  protected String              entityType;

  /**
   * Returns the name of the class of objects within this collection.
   * 
   * @return name of the class of objects within this collection.
   */
  public String getEntityType()
  {
    return this.entityType;
  }

  /**
   * Copies the content from the given collection into this one.
   */
  public void addAll(CacheStrategy cacheStrategy)
  {
    this.entityDAOIdSet.addAll(cacheStrategy.entityDAOIdSet);
    this.entityDAOIdByKeyMap.putAll(cacheStrategy.entityDAOIdByKeyMap);
  }

  /**
   * Initializes the EntityDAOCollection with the type of EntityDAOs that reside
   * in this collection
   * 
   * <br/>
   * <b>Precondition:</b> entityType != null <br/>
   * <b>Precondition:</b> !entityType.trim().equals("") <br/>
   * <b>Precondition:</b> entityType is a valid type ||
   * entityType.equals("NO_CACHE") <br/>
   * <b>Postcondition:</b> true
   * 
   * @param entityType
   *          name of the class in the collection.
   */
  public CacheStrategy(String entityType)
  {
    this.entityDAOIdSet = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
    this.entityDAOIdByKeyMap = new ConcurrentHashMap<String, String>();

    this.entityType = entityType;

    this.reload = true;
  }

  /**
   * Marks the cache to be refreshed.
   * 
   * <br/>
   * <b>Precondition:</b> true <br/>
   * <b>Postcondition:</b> this.entityDAOMap.clear()
   */
  public void reload()
  {
    this.entityDAOIdSet.clear();
    this.entityDAOIdByKeyMap.clear();
  }

  /**
   * If set to true, then the cached collection will be refreshed, false
   * otherwise.
   * 
   * @param reload
   *          if true, then the cached collection will be refreshed, false
   *          otherwise.
   */
  public void setReload(boolean reload)
  {
    this.reload = reload;
  }

  /**
   * Returns a linked list of Strings that are IDs for all EntityDAOs cached in
   * this collection.
   * 
   * <br/>
   * <b>Precondition:</b> true <br/>
   * <b>Postcondition:</b> true
   * 
   * @throws LinkedList
   *           of IDs for all EntityDAOs cached in this collection
   */
  protected List<String> getCachedIds()
  {
    List<String> idList = new LinkedList<String>();

    Iterator<String> i = entityDAOIdSet.iterator();

    while (i.hasNext())
    {
      idList.add(i.next());
    }
    return idList;
  }

  /**
   * Returns an iterator of all EntityDAOs cached in the collection.
   * 
   * <br/>
   * <b>Precondition:</b> true <br/>
   * <b>Postcondition:</b> true
   * 
   * @return Iterator of all EntityDAOs cached in the collection
   */
  protected List<? extends EntityDAOIF> getCachedEntityDAOs()
  {
    List<EntityDAOIF> entityDAOlist = new LinkedList<EntityDAOIF>();

    Iterator<String> i = entityDAOIdSet.iterator();

    while (i.hasNext())
    {
      entityDAOlist.add(ObjectCache.getEntityDAOIFfromCache(i.next()));
    }

    return entityDAOlist;
  }

  public abstract void updateCache(EntityDAO entityDAO);

  public abstract void removeCache(EntityDAO entityDAO);

  /**
   * Performs any cleanup on the global cache when this cache is removed.
   */
  protected synchronized void removeCacheStrategy()
  {
    ObjectCache.removeFromGlobalCache(this.entityDAOIdSet);
  }

  public abstract EntityDAOIF getEntityInstance(String id);

  public abstract EntityDAOIF getEntityInstance(String type, String key);

  /**
   * 
   * @param id
   * @return
   */
  protected abstract EntityDAOIF getFromFactory(String id);

  /**
   * 
   * @param type
   * @param key
   * @return
   */
  protected abstract EntityDAOIF getFromFactory(String type, String key);

  public boolean contains(String type, String key)
  {
    return this.entityDAOIdByKeyMap.containsKey(key);
  }

}
