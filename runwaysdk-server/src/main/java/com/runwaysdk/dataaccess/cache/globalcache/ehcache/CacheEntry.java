package com.runwaysdk.dataaccess.cache.globalcache.ehcache;

import java.io.Serializable;
import java.util.Map;

import com.runwaysdk.dataaccess.cache.CacheStrategy;

public class CacheEntry implements Serializable
{
  private static final long serialVersionUID = -4393595344924817752L;

  private CachedEntityDAOinfo entityDAO;
  
  private Map<String, CacheStrategy> collectionMap;
  
  public CacheEntry(CachedEntityDAOinfo entityDAO)
  {
    this.entityDAO = entityDAO;
  }
  
  public CacheEntry(Map<String, CacheStrategy> collectionMap)
  {
    this.collectionMap = collectionMap;
  }

  /**
   * @return the entityDAO
   */
  public CachedEntityDAOinfo getEntityDAO()
  {
    return entityDAO;
  }

  /**
   * @param entityDAO the entityDAO to set
   */
  public void setEntityDAO(CachedEntityDAOinfo entityDAO)
  {
    this.entityDAO = entityDAO;
  }

  /**
   * @return the collectionMap
   */
  public Map<String, CacheStrategy> getCollectionMap()
  {
    return collectionMap;
  }

  /**
   * @param collectionMap the collectionMap to set
   */
  public void setCollectionMap(Map<String, CacheStrategy> collectionMap)
  {
    this.collectionMap = collectionMap;
  }
  
  
}
