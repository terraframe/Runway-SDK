package com.runwaysdk.dataaccess.transaction;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.ehcache.Cache.Entry;
import org.ehcache.PersistentUserManagedCache;
import org.ehcache.UserManagedCacheBuilder;
import org.ehcache.config.ResourcePoolsBuilder;
import org.ehcache.config.persistence.DefaultPersistenceConfiguration;
import org.ehcache.config.persistence.UserManagedPersistenceContext;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.internal.persistence.DefaultLocalPersistenceService;
import org.ehcache.spi.service.LocalPersistenceService;

import com.runwaysdk.constants.ServerProperties;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.util.IDGenerator;

public class NewEntityIdStringCache
{
  
  private static int                                         newEntityIdMemorySize = ServerProperties.getNewEntityIdMemorySize();
  
  private Set<String>                                        newEntityIdStringSet;
   
  /**
   * A cache to the file system for storing ids of newly created entities from which 
   */
  protected PersistentUserManagedCache<String, String>       newEntityIdStringFileCache;
  private String                                             entityIdFileCacheName;
  private String                                             entityIdCacheFileLocation;
  private LocalPersistenceService                            filePersistenceService;
  

  public NewEntityIdStringCache()
  {
    this.newEntityIdStringSet = new HashSet<String>();
    
    this.newEntityIdStringFileCache = null;
    this.entityIdFileCacheName = null;
    this.entityIdCacheFileLocation = null;
    this.filePersistenceService = null;
  }
  


  /**
   * If the file cache used to check whether an {@link EntityDAO} of a non-cached type is not initialized, then
   * initialize it. Checks if the {@code this.newEntityIdStringFileCach} equals null. 
   * 
   */
  synchronized private void checkAndInitializeEntityIdFileCache()
  {
    if (this.newEntityIdStringSet.size() > newEntityIdMemorySize &&
        this.newEntityIdStringFileCache == null)
    {
      this.entityIdFileCacheName = IDGenerator.nextID();;
      int diskSize = ServerProperties.getTransactionDiskstoreSize();
      this.entityIdCacheFileLocation = ServerProperties.getTransactionCacheFileLocation();
 
      this.filePersistenceService = new DefaultLocalPersistenceService(new DefaultPersistenceConfiguration(new File(this.entityIdCacheFileLocation, this.entityIdFileCacheName)));
    
      this.newEntityIdStringFileCache = UserManagedCacheBuilder.newUserManagedCacheBuilder(String.class, String.class)
          .with(new UserManagedPersistenceContext<String, String>(this.entityIdFileCacheName, this.filePersistenceService)) 
          .withResourcePools(ResourcePoolsBuilder.newResourcePoolsBuilder() 
                    .heap(0, EntryUnit.ENTRIES)
//                    .offheap(cacheMemorySize, MemoryUnit.MB)
                    .disk(diskSize, MemoryUnit.MB, true)
           )
          .build(true);
      
      this.newEntityIdStringSet.clear();
    }
  }
  
  /**
   * Records that the {@link EntityDAOIF} has been created during this
   * transaction.
   * <br/>
   * <b>Pre: {@link EntityDAOIF} is of a type that is not cached<b/>
   * <b>Pre: {@link EntityDAOIF.isNew()} equals true<b/>
   * 
   * @param entityDAOIF
   *          {@link EntityDAOIF} that goes into the the global cache.
   */
  synchronized protected void recordNewlyCreatedNonCachedEntity(EntityDAOIF entityDAOIF)
  {
    this.recordNewlyCreatedNonCachedEntity(entityDAOIF.getId());
  }
  
  /**
   * Records that the {@link EntityDAOIF} has been created during this
   * transaction.
   * <br/>
   * <b>Pre: {@link EntityDAOIF} is of a type that is not cached<b/>
   * <b>Pre: {@link EntityDAOIF.isNew()} equals true<b/>
   * 
   * @param entityId
   *          ID of the {@link EntityDAOIF} that goes into the the global cache.
   */
  synchronized protected void recordNewlyCreatedNonCachedEntity(String entityId)
  {
    this.checkAndInitializeEntityIdFileCache();
    
    // If the cache file has been initialized then use it
    if (this.newEntityIdStringFileCache != null)
    {
      this.newEntityIdStringFileCache.put(entityId, "");
    }
    else
    {
      this.newEntityIdStringSet.add(entityId);
    }
  }
  
  /**
   * Returns true if the given id is from a newly created {@link EntityDAO} who's type 
   * is not cached.
   * 
   * @param entityDAOid
   * @return true if the given id is from a newly created {@link EntityDAO} who's type 
   * is not cached.
   */
  synchronized protected boolean isNewUncachedEntity(String entityDAOid)
  {   
    this.checkAndInitializeEntityIdFileCache();  
    
    // If the cache file has been initialized then use it
    if (this.newEntityIdStringFileCache != null)
    {
      return this.newEntityIdStringFileCache.containsKey(entityDAOid);
    }
    else
    {
      return this.newEntityIdStringSet.contains(entityDAOid);
    }    
  }
  
  
  /**
   * Changes the id of the {@link EntityDAO} in the transaction cache.
   * 
   * @param oldId
   * @param entityDAO
   */
  synchronized protected void changeEntityIdInCache(String oldId, EntityDAO entityDAO)
  {
    this.checkAndInitializeEntityIdFileCache();
    
    // If the cache file has been initialized then use it
    if (this.newEntityIdStringFileCache != null)
    {
      this.newEntityIdStringFileCache.remove(oldId);
      this.newEntityIdStringFileCache.put(entityDAO.getId(), "");
    }
    else
    {
      this.newEntityIdStringSet.remove(oldId);
      this.newEntityIdStringSet.add(entityDAO.getId());
    }    
  }
  
  /**
   * Close the eChache instance used to simply store ids of newly created {@link EntityDAO} objects
   * who's type are not cached.
   */
  synchronized protected void close() throws IOException
  {
    if (this.newEntityIdStringFileCache != null)
    {
      this.newEntityIdStringFileCache.close();
      this.filePersistenceService.destroyAllPersistenceSpaces();
      this.filePersistenceService.stop();

      FileUtils.deleteDirectory(new File(this.entityIdCacheFileLocation, this.entityIdFileCacheName));
    }
    
    this.newEntityIdStringSet.clear();
  } 
  
  /**
   * Returns an iterator of all IDs in the collection that stores all IDs in memory. Be cautious about using this.
   * 
   * @return Returns an iterator of all IDs in the collection that stores all IDs in memory. 
   */
  synchronized protected Iterator<String> getIds()
  {
    if (this.newEntityIdStringFileCache != null)
    {
      Iterator<Entry<String, String>> entryIterator = this.newEntityIdStringFileCache.iterator();
      
      List<String> returnList = new LinkedList<String>();
      
      while (entryIterator.hasNext())
      {
        returnList.add(entryIterator.next().getKey());
      }
      
      return returnList.iterator();
    }
    else
    {
      return this.newEntityIdStringSet.iterator();
    }
  }
  
}
