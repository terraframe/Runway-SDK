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
/*
 * Created on Jul 11, 2004
 */
package com.runwaysdk.dataaccess.cache;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.business.generation.ClientMarker;
import com.runwaysdk.business.generation.ServerMarker;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.constants.BusinessInfo;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.EntityTypes;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdAttributeDimensionInfo;
import com.runwaysdk.constants.MdAttributeInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdControllerInfo;
import com.runwaysdk.constants.MdElementInfo;
import com.runwaysdk.constants.MdEntityInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdEnumerationTypes;
import com.runwaysdk.constants.MdExceptionInfo;
import com.runwaysdk.constants.MdFacadeInfo;
import com.runwaysdk.constants.MdFormInfo;
import com.runwaysdk.constants.MdGraphInfo;
import com.runwaysdk.constants.MdIndexInfo;
import com.runwaysdk.constants.MdInformationInfo;
import com.runwaysdk.constants.MdLocalStructInfo;
import com.runwaysdk.constants.MdMessageInfo;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.constants.MdProblemInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.MdStateMachineInfo;
import com.runwaysdk.constants.MdStructInfo;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.constants.MdTermRelationshipInfo;
import com.runwaysdk.constants.MdTransientInfo;
import com.runwaysdk.constants.MdTreeInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MdUtilInfo;
import com.runwaysdk.constants.MdViewInfo;
import com.runwaysdk.constants.MdWarningInfo;
import com.runwaysdk.constants.RelationshipInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.constants.ServerProperties;
import com.runwaysdk.dataaccess.AttributeLocalIF;
import com.runwaysdk.dataaccess.AttributeStructIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.ElementDAOIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.EnumerationItemDAO;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdControllerDAOIF;
import com.runwaysdk.dataaccess.MdElementDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdExceptionDAOIF;
import com.runwaysdk.dataaccess.MdFacadeDAOIF;
import com.runwaysdk.dataaccess.MdFormDAOIF;
import com.runwaysdk.dataaccess.MdIndexDAOIF;
import com.runwaysdk.dataaccess.MdInformationDAOIF;
import com.runwaysdk.dataaccess.MdLocalStructDAOIF;
import com.runwaysdk.dataaccess.MdMessageDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdProblemDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.MdTermDAOIF;
import com.runwaysdk.dataaccess.MdTransientDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.MdUtilDAOIF;
import com.runwaysdk.dataaccess.MdViewDAOIF;
import com.runwaysdk.dataaccess.MdWarningDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.StructDAOIF;
import com.runwaysdk.dataaccess.TransientDAOFactory;
import com.runwaysdk.dataaccess.UnexpectedTypeException;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.cache.globalcache.ehcache.Diskstore;
import com.runwaysdk.dataaccess.database.ControllerDAOFactory;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.DefaultEnumerationInfo;
import com.runwaysdk.dataaccess.database.DefaultMdEntityInfo;
import com.runwaysdk.dataaccess.database.EntityDAOFactory;
import com.runwaysdk.dataaccess.database.StructDAOFactory;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdControllerDAO;
import com.runwaysdk.dataaccess.metadata.MdElementDAO;
import com.runwaysdk.dataaccess.metadata.MdEntityDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdFacadeDAO;
import com.runwaysdk.dataaccess.metadata.MdTransientDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.metadata.MetadataException;
import com.runwaysdk.dataaccess.transaction.ITaskListener;
import com.runwaysdk.dataaccess.transaction.LockObject;
import com.runwaysdk.facade.WebServiceAdapter;
import com.runwaysdk.facade.wsdd.WebServiceDeployer;
import com.runwaysdk.generation.CommonMarker;
import com.runwaysdk.util.IdParser;
import com.runwaysdk.util.ServerInitializerFacade;
import com.runwaysdk.web.json.JSONWebServiceAdapter;

/**
 * Manages collections of all EntityDAO classes. All EntityDAO CRUD operations
 * are routed through this class and are dispatched to the appropriate
 * collection based on the EntityDAO's class.
 * 
 * @author nathan
 * @version $Revision: 1.30 $
 * @since 1.4
 */
public class ObjectCache
{
  private static ObjectStore                         globalCache;
  
  final static Logger logger = LoggerFactory.getLogger(ObjectCache.class);
  
  static
  {
    try
    {
      if (ServerProperties.memoryOnlyCache())
      {
        globalCache = new Memorystore();
      }
      else
      {
        globalCache = new Diskstore(ServerProperties.getGlobalCacheName(), ServerProperties.getGlobalCacheFileLocation(), ServerProperties.getGlobalCacheMemorySize(), ServerProperties.getGlobalCacheOffheapMemorySize());
      }
    }
    catch (Throwable t)
    {
      logger.error("An error occurred while initializing the object cache. This is the root cause for what will later cause a NoClassDefFoundError.", t);
      throw t;
    }
  }

  /**
   * Each item in the map is a collection of EntityDAOs. The map key is the
   * String name of a class and the value is the EntityDAO collection for that
   * class.
   * 
   * <br/>
   * <b>invariant</b> collectionMap != null
   */
  private static volatile Map<String, CacheStrategy> strategyMap;

  private static volatile boolean                    initialized                     = false;

  /**
   * Indicates whether, on system startup, the source and class files should be
   * extracted from the database and placed on the file system.
   */
  private static boolean                             extractDatabaseSourceAndClasses = true;

  /**
   * Hard coded key used in the map collection for struct types that are set to
   * cache none.
   */
  private static final String                        STRUCT                          = "Struct";

  /**
   * Collection of listeners interested in listening to work event of the method
   * ObjectCache.init();
   */
  private static List<ITaskListener>                 listeners                       = new LinkedList<ITaskListener>();

  public static void addListener(ITaskListener listener)
  {
    listeners.add(listener);
  }

  public static void removeListener(ITaskListener listener)
  {
    listeners.remove(listener);
  }

  private static void fireTaskProgress(int amount)
  {
    for (ITaskListener listener : listeners)
    {
      listener.taskProgress(amount);
    }
  }

  private static void fireStartTask(String name, int totalAmount)
  {
    for (ITaskListener listener : listeners)
    {
      listener.taskStart(name, totalAmount);
    }
  }

  private static void fireStart()
  {
    for (ITaskListener listener : listeners)
    {
      listener.start();
    }
  }

  private static void fireDone(boolean status)
  {
    for (ITaskListener listener : listeners)
    {
      listener.done(status);
    }
  }

  /**
   * Reinitializes the class collections.
   * 
   * <br/>
   * <b>Precondition:</b> true <br/>
   * <b>Postcondition:</b> true
   * 
   */
  public static void refreshCache()
  {
    initialized = false;
    try
    {
      globalCache.removeAll();
    }
    catch (IllegalStateException e)
    {
      // Do nothing, if the cache is not valid, it will be rebuilt in the init()
      // method.
    }
  }

  /**
   * Returns a BusinessDAO of the given id in the database.
   * 
   * <br/>
   * <b>Precondition:</b> id != null <br/>
   * <b>Precondition:</b> !id.trim().equals("") <br/>
   * <b>Precondition:</b> given id represents a valid item in the database
   * 
   * <br/>
   * <b>Postcondition:</b> return value may not be null <br/>
   * <b>Postcondition:</b> BusinessDAO representing the item in the database of
   * the given id is returned
   * 
   * @param id
   *          element id of an item in the database
   * @return DataAccessIF instance of the given id
   */
  public static BusinessDAOIF getBusinessDAO(String id)
  {
    EntityDAOIF entityDAO = getEntityDAO(id);

    if (! ( entityDAO instanceof BusinessDAO ))
    {
      String errmsg = "Object with id [" + id + "] exists but it is not a BusinessDAO.";
      throw new UnexpectedTypeException(errmsg);
    }

    return (BusinessDAOIF) entityDAO;
  }

  /**
   * Returns a StructDAO of the given id in the database.
   * 
   * <br/>
   * <b>Precondition:</b> id != null <br/>
   * <b>Precondition:</b> !id.trim().equals("") <br/>
   * <b>Precondition:</b> given id represents a valid item in the database
   * 
   * <br/>
   * <b>Postcondition:</b> return value may not be null <br/>
   * <b>Postcondition:</b> StructDAO representing the item in the database of
   * the given id is returned
   * 
   * @param id
   *          element id of an item in the database
   * @return DataAccessIF instance of the given id
   */
  public static StructDAOIF getSructObject(String id)
  {
    EntityDAOIF entityDAO = getEntityDAO(id);

    if (! ( entityDAO instanceof StructDAO ))
    {
      String errmsg = "Object with id [" + id + "] exists but it is not a StructDAO.";
      throw new UnexpectedTypeException(errmsg);
    }

    return (StructDAOIF) entityDAO;
  }

  /**
   * 
   * @param id
   * @return
   */
  public static RelationshipDAOIF getRelationshipDAO(String id)
  {
    EntityDAOIF entityDAO = getEntityDAO(id);

    if (! ( entityDAO instanceof RelationshipDAOIF ))
    {
      String errmsg = "Object with id [" + id + "] exists but is not a Relationship as expected.";
      throw new UnexpectedTypeException(errmsg);
    }

    return (RelationshipDAOIF) entityDAO;
  }

  /**
   * 
   * @param id
   * @return
   */
  public static ElementDAOIF getElementDAO(String id)
  {
    EntityDAOIF entityDAO = getEntityDAO(id);

    if (! ( entityDAO instanceof ElementDAOIF ))
    {
      String errmsg = "Object with id [" + id + "] exists but is not a Element as expected.";
      throw new UnexpectedTypeException(errmsg);
    }

    return (ElementDAOIF) entityDAO;
  }

  /**
   * 
   * @param id
   * @return
   * @throws DataNotFoundException
   *           when the id does not represent a valid entity.
   */
  public static EntityDAOIF getEntityDAO(String id)
  {
    ( LockObject.getLockObject() ).checkTransactionLock(id);

    EntityDAOIF returnObject = ObjectCache.getEntityDAOIFfromCache(id);

    if (returnObject == null)
    {
      // get the appropriate collection class for the entity type
      MdClassDAOIF mdClassIF = MdClassDAO.getMdClassByRootId(IdParser.parseMdTypeRootIdFromId(id));

      if (! ( mdClassIF instanceof MdEntityDAOIF ))
      {
        String errMsg = "Object with id [" + id + "] is not defined by a [" + MdEntityInfo.CLASS + "]";
        throw new DataNotFoundException(errMsg, mdClassIF);
      }

      CacheStrategy entityDAOCollection = getTypeCollection(mdClassIF.definesType());

      // The collection class may or may not use caching. If caching is used,
      // a cached EntityDAO is returned
      returnObject = entityDAOCollection.getEntityInstance(id);
    }

    return returnObject;

  }

  /**
   * 
   * @param type
   * @param key
   * @return
   * @throws DataNotFoundException
   *           when the id does not represent a valid entity.
   */
  public static EntityDAOIF getEntityDAO(String type, String key)
  {
    ( LockObject.getLockObject() ).checkTransactionLock(type, key);

    // get the appropriate collection class for the entity type
    MdClassDAOIF mdClassIF = MdClassDAO.getMdClassDAO(type);

    if (! ( mdClassIF instanceof MdEntityDAOIF ))
    {
      String errMsg = "Object with key [" + key + "] is not defined by a [" + MdEntityInfo.CLASS + "]";
      throw new DataNotFoundException(errMsg, mdClassIF);
    }

    CacheStrategy entityDAOCollection = getTypeCollection(mdClassIF.definesType());

    // The collection class may or may not use caching. If caching is used,
    // a cached EntityDAO is returned
    return entityDAOCollection.getEntityInstance(type, key);
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
  public static List<? extends EntityDAOIF> getCachedEntityDAOs(String type)
  {
    // get the appropriate collection class for the type
    return getTypeCollection(type).getCachedEntityDAOs();
  }

  /**
   * Refreshes the EntityDAO in the appropriate collection.
   * 
   * <br/>
   * <b>Precondition:</b> businessDAO != null <br/>
   * <b>Postcondition:</b> businessDAO is added to the appropriate collection.
   * 
   * @param entityDAO
   *          EntityDAO to add to a collection.
   */
  public static void updateCache(EntityDAO entityDAO)
  {
    // Look up the type of the given id
    String entityType = entityDAO.getType();

    // get the appropriate collection class for the type
    CacheStrategy cacheStrategy = getTypeCollection(entityType);
    cacheStrategy.updateCache(entityDAO);
  }

  /**
   * Removes the EntityDAO from the appropriate cache.
   * 
   * <br/>
   * <b>Precondition:</b> entityDAO != null <br/>
   * <b>Postcondition:</b> entityDAO is added to the appropriate collection.
   * 
   * @param entityDAO
   *          EntityDAO to remove from a collection.
   */
  public static void removeCache(EntityDAO entityDAO)
  {    
    // Look up the type of the given id
    String entityType = entityDAO.getType();

    // get the appropriate collection class for the type
    CacheStrategy entityDAOCollection = getTypeCollection(entityType);

    entityDAOCollection.removeCache(entityDAO);
  }
  
  /**
   * Removes the {@link EntityDAO} with the given id from the cache so that it can be refreshed
   * on the next request for the object.
   *
   * <br/><b>Precondition:</b>  {@link EntityDAO} != null
   *
   * <br/><b>Postcondition:</b> cache no longer contains the given {@link EntityDAO}
   *
   * @param  id for the {@link EntityDAO} to remove from this collection
   */
  public static void clearCacheForRefresh(String entityId)
  {
    // Look up the type of the given id
    String entityType = MdClassDAO.getMdClassByRootId(IdParser.parseMdTypeRootIdFromId(entityId)).definesType();

    // get the appropriate collection class for the type
    CacheStrategy entityDAOCollection = getTypeCollection(entityType);

    entityDAOCollection.clearCacheForRefresh(entityId);
  }

  /**
   * Returns a list of parent relationships of the given type from the cache for
   * a {@link BusinessDAOIF} with the given id.
   * 
   * @param id
   * @param relationshipType
   * @return
   */
  protected static List<RelationshipDAOIF> getParentRelationshipsFromCache(String id, String relationshipType)
  {
    return globalCache.getParentRelationshipsFromCache(id, relationshipType);
  }

  /**
   * Returns a list of child relationships of the given type from the cache for
   * a {@link BusinessDAOIF} with the given id.
   * 
   * @param id
   * @param relationshipType
   * 
   * @return
   */
  protected static List<RelationshipDAOIF> getChildRelationshipsFromCache(String id, String relationshipType)
  {
    return globalCache.getChildRelationshipsFromCache(id, relationshipType);
  }

  /**
   * Adds the {@link RelationshipDAOIF} to the parent and child relationships of
   * the parent and child objects in the cache.
   * 
   * @param relationshipDAOIF
   */
  protected static void addRelationshipDAOIFtoCache(RelationshipDAOIF relationshipDAOIF)
  {
    globalCache.addRelationshipDAOIFtoCache(relationshipDAOIF);
  }

  /**
   * Updates the stored id if it has changed for the {@link RelationshipDAOIF} to the 
   * parent and child relationships of the parent and child objects in the cache.
   * 
   * @param hasIdChanged
   * @param relationshipDAOIF
   */
  public static void updateRelationshipDAOIFinCache(Boolean hasIdChanged, RelationshipDAOIF relationshipDAOIF)
  {
    globalCache.updateRelationshipDAOIFinCache(hasIdChanged, relationshipDAOIF);
  }
  
  /**
   * Removes the {@link RelationshipDAOIF} from the parent relationship of the
   * child object in the cache.
   * 
   * @param relationshipDAOIF
   * @param deletedObject
   *          indicates the object is being deleted from the application.
   * 
   * @return True if the child object still has parent relationships of this
   *         type.
   */
  protected static boolean removeParentRelationshipDAOIFtoCache(RelationshipDAOIF relationshipDAOIF, boolean deletedObject)
  {
    return globalCache.removeParentRelationshipDAOIFtoCache((RelationshipDAO)relationshipDAOIF, deletedObject);
  }

  /**
   * Removes all parent relationships of the given type for the
   * {@link BusinessDAOIF} with the given id.
   * 
   * @param childId
   * @param relationshipType
   * @param deletedObject
   *          indicates the object is being deleted from the application.
   */
  protected static void removeAllParentRelationshipsOfType(String childId, String relationshipType, boolean deletedObject)
  {
    globalCache.removeAllParentRelationshipsOfType(childId, relationshipType, deletedObject);
  }

  /**
   * Removes the {@link RelationshipDAOIF} from the child relationship of the
   * parent object in the cache.
   * 
   * @param relationshipDAOIF
   * @param deletedObject
   *          indicates the object is being deleted from the application.
   * @return True if the parent object still has children relationships of this
   *         type.
   */
  protected static boolean removeChildRelationshipDAOIFtoCache(RelationshipDAOIF relationshipDAOIF, boolean deletedObject)
  {
    return globalCache.removeChildRelationshipDAOIFtoCache(relationshipDAOIF, deletedObject);
  }

  /**
   * Removes all child relationships of the given type for the
   * {@link BusinessDAOIF} with the given id.
   * 
   * @param parentId
   * @param relationshipType
   * @param deletedObject
   *          indicates the object is being deleted from the application.
   */
  protected static void removeAllChildRelationshipsOfType(String parentId, String relationshipType, boolean deletedObject)
  {
    globalCache.removeAllChildRelationshipsOfType(parentId, relationshipType, deletedObject);
  }

  /**
   * Returns the {@link EntityDAOIF} from the cache with the given id or null if
   * the object with the given id is not in the cache.
   * 
   * @param id
   * @return {@link EntityDAOIF} from the cache with the given id or null if the
   *         object with the given id is not in the cache.
   */
  public static EntityDAOIF getEntityDAOIFfromCache(String id)
  {
    return globalCache.getEntityDAOIFfromCache(id);
  }

   
  /**
   * DO NOT CALL THIS METHOD UNLESS YOU KNOW WHAT YOU ARE DOING - Puts the given
   * {@link EntityDAOIF} into the global cache.
   * 
   * @param entityDAOIF
   *          {@link EntityDAOIF} that goes into the the global cache.
   */
  public static void putEntityDAOIFintoCache(EntityDAOIF entityDAOIF)
  { 
    globalCache.putEntityDAOIFintoCache(entityDAOIF);
  }

  
  /**
   * DO NOT CALL THIS METHOD UNLESS YOU KNOW WHAT YOU ARE DOING - Updates the 
   * changed id for the given {@link EntityDAOIF} in the cache.
   * 
   * <br/><b>Precondition:</b> Calling method has checked whether the id has changed.
   * 
   * @param oldEntityId
   * @param entityDAOIF
   */
  public static void updateIdEntityDAOIFinCache(String oldEntityId, EntityDAOIF entityDAOIF)
  {
    globalCache.updateIdEntityDAOIFinCache(oldEntityId, entityDAOIF);
  }

  /**
   * Removes the item from the cache with the given key
   * 
   * @param entityDAOIFid
   *          key of the item to be removed from the cache.
   * @param deletedObject
   *          indicates the object is being deleted from the application.
   */
  protected static void removeEntityDAOIFfromCache(String id, boolean deletedObject)
  {
    globalCache.removeEntityDAOIFfromCache(id, deletedObject);
  }

  /**
   * Fetches all <code>StructDAO</code>s for attribute structs used in the
   * metadata.
   * 
   * @return all <code>StructDAO</code>s for attribute structs used in the
   *         metadata.
   */
  private static void getStructDAOforAttributeStructs(List<String> structDAOids)
  {
    HardCodedMetadataIterator i = Database.getAttributesForHardcodedMetadataType(EntityTypes.METADATADISPLAYLABEL.getTableName(), EntityTypes.METADATADISPLAYLABEL.getType(), EntityTypes.METADATADISPLAYLABEL.getTableName(), null, true);

    Map<String, Map<String, Attribute>> tupleMap = i.next();
    while (tupleMap != null)
    {
      String id = tupleMap.keySet().iterator().next();
      structDAOids.add(id);
      Map<String, Attribute> attributeMap = tupleMap.get(id);
      StructDAO structDAO = StructDAOFactory.factoryMethod(attributeMap, EntityTypes.METADATADISPLAYLABEL.getType());
      ObjectCache.putEntityDAOIFintoCache(structDAO);

      tupleMap = i.next();
    }

    i = Database.getAttributesForHardcodedMetadataType(EntityTypes.MD_LOCALIZABLE_MESSAGE.getTableName(), EntityTypes.MD_LOCALIZABLE_MESSAGE.getType(), EntityTypes.MD_LOCALIZABLE_MESSAGE.getTableName(), null, true);

    tupleMap = i.next();
    while (tupleMap != null)
    {
      String id = tupleMap.keySet().iterator().next();
      structDAOids.add(id);
      Map<String, Attribute> attributeMap = tupleMap.get(id);
      StructDAO structDAO = StructDAOFactory.factoryMethod(attributeMap, EntityTypes.MD_LOCALIZABLE_MESSAGE.getType());
      ObjectCache.putEntityDAOIFintoCache(structDAO);

      tupleMap = i.next();
    }
  }

  /**
   * Persists the state of the global cache to disk.
   */
  public static void shutdownGlobalCache()
  {
    if (!globalCache.isCacheInitialized())
    {
      logger.warn("We were told to shutdown the global cache, but it was not initialized. This is only a problem if the application subsequently hangs.");
      return;
    }
    
    // Check to see if the cache has been marked to shutdown. If so, the
    // collection
    // classes will not be in the cache.
    if (!globalCache.isEmpty())
    {
      globalCache.backupCollectionClasses(strategyMap);
    }

    globalCache.shutdown();
    initialized = false;
  }

  /**
   * Initializes all EntityDAO collections.
   * 
   */
  @SuppressWarnings("unused")
  private static void init()
  {
    fireStart();

    LockObject.getLockObject().lockCache();
    try
    {
      // Collections have been initialized by another thread
      if (initialized == true)
      {
        return;
      }

      java.util.Date startTime = new java.util.Date();

      ServerInitializerFacade.init();

      // Ensure that the generated .class directories exist
      new File(ClientMarker.SOURCE_DIRECTORY).mkdirs();
      new File(ClientMarker.BASE_DIRECTORY).mkdirs();
      new File(ClientMarker.CLASS_DIRECTORY).mkdirs();
      new File(CommonMarker.SOURCE_DIRECTORY).mkdirs();
      new File(CommonMarker.BASE_DIRECTORY).mkdirs();
      new File(CommonMarker.CLASS_DIRECTORY).mkdirs();
      new File(ServerMarker.SOURCE_DIRECTORY).mkdirs();
      new File(ServerMarker.BASE_DIRECTORY).mkdirs();
      new File(ServerMarker.CLASS_DIRECTORY).mkdirs();

      // Reset temporary data structures used to help initialize the collections
      DefaultMdEntityInfo.refresh();
      DefaultEnumerationInfo.refresh();

      globalCache.initializeCache();

      Map<String, CacheStrategy> cacheStrategyMap = globalCache.restoreCollectionClasses();

      if (cacheStrategyMap == null)
      {
        logger.info("The global cache returned NULL for collection classes");
        initializeGlobalCacheWithMetadata();
      }
      else
      {
        fireStartTask("INIT_CACHE", 100);

        strategyMap = cacheStrategyMap;
        globalCache.removeCollectionClasses();

        fireTaskProgress(100);
      }

      if (LocalProperties.isDevelopEnvironment() || LocalProperties.isTestEnvironment() || !LocalProperties.getCopyArtifactsOnStart())
      {
        extractDatabaseSourceAndClasses = false;
      }

      // Write .class files for enums
      for (String enumType : Database.getAllEnumTypes())
      {
        MdEnumerationDAOIF mdEnumIF = ObjectCache.getMdEnumerationDAO(enumType);

        if (mdEnumIF == null)
        {
          String error = "MdEnumeration [" + enumType + "] was not found";
          throw new DataNotFoundException(error, MdTypeDAO.getMdTypeDAO(MdEnumerationInfo.CLASS));
        }

        if (extractDatabaseSourceAndClasses)
        {
          // This cast is OK. The method does not modify the object.
          ( (MdEnumerationDAO) mdEnumIF ).writeJavaToFile();
        }
      }

      // add collection of transient types
      for (String transientType : TransientDAOFactory.getAllTransientNames())
      {
        MdTransientDAOIF mdTransientIF = ObjectCache.getMdTransientDAO(transientType);

        if (extractDatabaseSourceAndClasses)
        {
          // This cast is OK. The method does not modify the object.
          ( (MdTransientDAO) mdTransientIF ).writeJavaToFile();
        }
      }

      // add collection of transient types
      for (String controllerType : ControllerDAOFactory.getAllControllerNames())
      {
        MdControllerDAOIF mdControllerIF = ObjectCache.getMdControllerDAO(controllerType);

        if (extractDatabaseSourceAndClasses)
        {
          // This cast is OK. The method does not modify the object.
          ( (MdControllerDAO) mdControllerIF ).writeJavaToFile();
        }
      }

      // Build collections based off of metadata. Metadata has been cached
      // from
      // above
      for (String entityType : EntityDAOFactory.getAllEntityNames())
      {
        MdEntityDAOIF mdEntityIF = ObjectCache.getMdEntityDAO(entityType);

        if (extractDatabaseSourceAndClasses)
        {
          // This cast is OK. The method does not modify the object.
          ( (MdEntityDAO) mdEntityIF ).writeJavaToFile();
        }
      }

      // add the default services

      // write .class files for facades
      Set<MdFacadeDAOIF> mdFacadeList = EntityDAOFactory.getAllMdFacades();
      for (MdFacadeDAOIF mdFacadeIF : mdFacadeList)
      {
        if (extractDatabaseSourceAndClasses)
        {
          // This cast is OK. The method does not modify the object.
          ( (MdFacadeDAO) mdFacadeIF ).writeJavaToFile();
        }
      }

      if (CommonProperties.getContainerWebServiceEnabled())
      {
        // deploy the web services
        WebServiceDeployer serviceDeployer = new WebServiceDeployer();
        serviceDeployer.addService(WebServiceAdapter.class);
        serviceDeployer.addService(JSONWebServiceAdapter.class);
        serviceDeployer.addServices(mdFacadeList);
        serviceDeployer.deploy();
      }

      extractDatabaseSourceAndClasses = true;

      initialized = true;

      String dateTimeFormat = Constants.DATETIME_FORMAT;
      java.util.Date endTime = new java.util.Date();
      long totalTime = endTime.getTime() - startTime.getTime();
      logger.info("The global cache has been initialized. Start Time: " + new SimpleDateFormat(dateTimeFormat).format(startTime) + " End Time: " + new SimpleDateFormat(dateTimeFormat).format(endTime) + " Total Time: " + totalTime + "\n");
    }
    finally
    {
      LockObject.getLockObject().unlockCache();
    }
    
    fireDone(true);
  }

  /**
   * 
   */
  private static void initializeGlobalCacheWithMetadata()
  {   
    /*
     * numberOfCollections: Number of hard coded steps of work
     */
    int numberOfHardedCodedSteps = 24;

    List<String> allEntityNames = EntityDAOFactory.getAllEntityNames();

    fireStartTask("INIT_CACHE", numberOfHardedCodedSteps + allEntityNames.size());

    // Initialize struct attributes used in attribute structs.
    List<String> structDAOids = new LinkedList<String>();
    getStructDAOforAttributeStructs(structDAOids);

    fireTaskProgress(1);

    Map<String, CacheStrategy> tempStrategyMap = new ConcurrentHashMap<String, CacheStrategy>();

    MdClassStrategy mdClassStrategy = new MdClassStrategy(MdClassInfo.CLASS);
    mdClassStrategy.reload();
    tempStrategyMap.put(MdClassInfo.CLASS, mdClassStrategy);

    fireTaskProgress(1);

    MdAttributeStrategy mdAttributeStrategy = new MdAttributeStrategy(MdAttributeInfo.CLASS);
    mdAttributeStrategy.reload();
    tempStrategyMap.put(MdAttributeInfo.CLASS, mdAttributeStrategy);

    fireTaskProgress(1);

    MdEnumerationStrategy mdEnumerationStrategy = new MdEnumerationStrategy(MdEnumerationInfo.CLASS);
    mdEnumerationStrategy.reload();
    tempStrategyMap.put(MdEnumerationInfo.CLASS, mdEnumerationStrategy);

    fireTaskProgress(1);

    MdFacadeStrategy mdFacadeStrategy = new MdFacadeStrategy(MdFacadeInfo.CLASS);
    mdFacadeStrategy.reload();
    tempStrategyMap.put(MdFacadeInfo.CLASS, mdFacadeStrategy);

    fireTaskProgress(1);

    MdControllerStrategy mdControllerStrategy = new MdControllerStrategy(MdControllerInfo.CLASS);
    mdControllerStrategy.reload();
    tempStrategyMap.put(MdControllerInfo.CLASS, mdControllerStrategy);

    fireTaskProgress(1);

    MdFormStrategy mdFormStrategy = new MdFormStrategy(MdFormInfo.CLASS);
    mdFormStrategy.reload();
    tempStrategyMap.put(MdFormInfo.CLASS, mdFormStrategy);

    fireTaskProgress(1);

    MdIndexStrategy mdIndexStrategy = new MdIndexStrategy(MdIndexInfo.CLASS);
    mdIndexStrategy.reload();
    tempStrategyMap.put(MdIndexInfo.CLASS, mdIndexStrategy);

    fireTaskProgress(1);

    HardcodedRelationshipStrategy businessInheritanceCollection = new HardcodedRelationshipStrategy(RelationshipTypes.BUSINESS_INHERITANCE.getType());
    businessInheritanceCollection.reload();
    tempStrategyMap.put(RelationshipTypes.BUSINESS_INHERITANCE.getType(), businessInheritanceCollection);

    fireTaskProgress(1);

    HardcodedRelationshipStrategy relationshipInheritanceCollection = new HardcodedRelationshipStrategy(RelationshipTypes.RELATIONSHIP_INHERITANCE.getType());
    relationshipInheritanceCollection.reload();
    tempStrategyMap.put(RelationshipTypes.RELATIONSHIP_INHERITANCE.getType(), relationshipInheritanceCollection);

    fireTaskProgress(1);

    HardcodedRelationshipStrategy classAttributeConcreteCollection = new HardcodedRelationshipStrategy(RelationshipTypes.CLASS_ATTRIBUTE_CONCRETE.getType());
    classAttributeConcreteCollection.reload();
    tempStrategyMap.put(RelationshipTypes.CLASS_ATTRIBUTE_CONCRETE.getType(), classAttributeConcreteCollection);

    fireTaskProgress(1);

    HardcodedRelationshipStrategy classAttributeVirtualCollection = new HardcodedRelationshipStrategy(RelationshipTypes.CLASS_ATTRIBUTE_VIRTUAL.getType());
    classAttributeVirtualCollection.reload();
    tempStrategyMap.put(RelationshipTypes.CLASS_ATTRIBUTE_VIRTUAL.getType(), classAttributeVirtualCollection);

    fireTaskProgress(1);

    // map has the up-to-date metadata collections. Thread safety aspect will
    // prevent other threads
    // from doing anything until this method finishes.
    strategyMap = tempStrategyMap;   

    // Default collection class for objects
    CacheNoneBusinessDAOStrategy defaultObjectCollection = new CacheNoneBusinessDAOStrategy(BusinessInfo.CLASS);
    defaultObjectCollection.reload();
    strategyMap.put(BusinessInfo.CLASS, defaultObjectCollection);

    fireTaskProgress(1);

    // Default collection class for structs
    CacheNoneStructDAOStrategy defaultStructCollection = new CacheNoneStructDAOStrategy(STRUCT);
    defaultStructCollection.reload();
    strategyMap.put(STRUCT, defaultStructCollection);

    fireTaskProgress(1);

    // Default collection class for relationships
    CacheNoneRelationshipDAOStrategy defaultRelationshipCollection = new CacheNoneRelationshipDAOStrategy(RelationshipInfo.CLASS);
    defaultRelationshipCollection.reload();
    strategyMap.put(RelationshipInfo.CLASS, defaultRelationshipCollection);

    fireTaskProgress(1);

    MdBusinessDAOIF rootEnumMdBusinessIF = ObjectCache.getMdBusinessDAO(EnumerationMasterInfo.CLASS);

    fireTaskProgress(1);

    EnumerationMasterStrategy enumerationAttributeCollection = new EnumerationMasterStrategy(rootEnumMdBusinessIF);
    enumerationAttributeCollection.reload();
    strategyMap.put(EnumerationMasterInfo.CLASS, enumerationAttributeCollection);

    fireTaskProgress(1);

    RoleStrategy roleStrategy = new RoleStrategy(RoleDAOIF.CLASS);
    roleStrategy.reload();
    strategyMap.put(RoleDAOIF.CLASS, roleStrategy);

    fireTaskProgress(1);

    CacheAllRelationshipDAOStrategy viewInheritanceCollection = new CacheAllRelationshipDAOStrategy(RelationshipTypes.VIEW_INHERITANCE.getType());
    viewInheritanceCollection.reload();
    strategyMap.put(RelationshipTypes.VIEW_INHERITANCE.getType(), viewInheritanceCollection);

    fireTaskProgress(1);

    CacheAllRelationshipDAOStrategy utilInheritanceCollection = new CacheAllRelationshipDAOStrategy(RelationshipTypes.UTIL_INHERITANCE.getType());
    utilInheritanceCollection.reload();
    strategyMap.put(RelationshipTypes.UTIL_INHERITANCE.getType(), utilInheritanceCollection);

    fireTaskProgress(1);

    CacheAllRelationshipDAOStrategy exceptionInheritanceCollection = new CacheAllRelationshipDAOStrategy(RelationshipTypes.EXCEPTION_INHERITANCE.getType());
    exceptionInheritanceCollection.reload();
    strategyMap.put(RelationshipTypes.EXCEPTION_INHERITANCE.getType(), exceptionInheritanceCollection);

    fireTaskProgress(1);

    CacheAllRelationshipDAOStrategy problemInheritanceCollection = new CacheAllRelationshipDAOStrategy(RelationshipTypes.PROBLEM_INHERITANCE.getType());
    problemInheritanceCollection.reload();
    strategyMap.put(RelationshipTypes.PROBLEM_INHERITANCE.getType(), problemInheritanceCollection);

    fireTaskProgress(1);

    CacheAllRelationshipDAOStrategy warningInheritanceCollection = new CacheAllRelationshipDAOStrategy(RelationshipTypes.WARNING_INHERITANCE.getType());
    warningInheritanceCollection.reload();
    strategyMap.put(RelationshipTypes.WARNING_INHERITANCE.getType(), warningInheritanceCollection);

    fireTaskProgress(1);
    
    MdAttributeDimensionDAOStrategy mdAttributeDimensionDAOCollection = new MdAttributeDimensionDAOStrategy(MdAttributeDimensionInfo.CLASS);
    mdAttributeDimensionDAOCollection.reload();
    strategyMap.put(MdAttributeDimensionInfo.CLASS, mdAttributeDimensionDAOCollection);

    fireTaskProgress(1);
    

    // Remove the structs used in attribute structs from the cache.
    for (String structId : structDAOids)
    {
      ObjectCache.removeEntityDAOIFfromCache(structId, false);
    }

    fireTaskProgress(1);

    // Build collections based off of metadata. Metadata has been cached from
    // above
    for (String entityType : allEntityNames)
    {
      MdEntityDAOIF mdEntityIF = ObjectCache.getMdEntityDAO(entityType);

      BusinessDAOIF cacheEnumItem = mdEntityIF.getCacheAlgorithm();
      int cacheCode = new Integer(cacheEnumItem.getAttributeIF(EntityCacheMaster.CACHE_CODE).getValue()).intValue();

      // Cache everything.
      if (cacheCode == EntityCacheMaster.CACHE_EVERYTHING.getCacheCode())
      {
        CacheAllStrategy cacheAllStrategy;
        if (mdEntityIF instanceof MdBusinessDAOIF)
        {
          cacheAllStrategy = new CacheAllBusinessDAOstrategy((MdBusinessDAOIF) mdEntityIF);
        }
        else if (mdEntityIF instanceof MdStructDAOIF)
        {
          cacheAllStrategy = new CacheAllStructDAOStrategy((MdStructDAOIF) mdEntityIF);
        }
        else
        {
          cacheAllStrategy = new CacheAllRelationshipDAOStrategy((MdRelationshipDAOIF) mdEntityIF);
        }

        cacheAllStrategy.reload();
        strategyMap.put(entityType, cacheAllStrategy);
      }
      // Cache nothing. Do nothing as all CRUD will be routed to the default
      // no cache collection.
      else if (cacheCode == EntityCacheMaster.CACHE_NOTHING.getCacheCode())
      {
      }
      // Cache most recently used.
      else if (cacheCode == EntityCacheMaster.CACHE_MOST_RECENTLY_USED.getCacheCode())
      {
        CacheMRUStrategy cacheMRUStrategy = new CacheMRUStrategy(entityType, mdEntityIF.getCacheSize());
        cacheMRUStrategy.reload();
        strategyMap.put(entityType, cacheMRUStrategy);
      }
      // Hard-coded in the core. The hard-coded entity types were taken care of
      // above.
      else if (cacheCode == EntityCacheMaster.CACHE_HARDCODED.getCacheCode())
      {
      }
      else
      {
        String error = "Cache algorithm [" + cacheEnumItem.getValue(EnumerationMasterInfo.NAME) + "] for objects is undefined.";
        MdTypeDAOIF cache_definitions = MdTypeDAO.getMdTypeDAO(MdEnumerationTypes.OBJECT_TYPE_CACHE.getType());
        throw new DataNotFoundException(error, cache_definitions);
      }

      fireTaskProgress(1);
    }
  }

  /**
   * We want to update the collection that caches a particular class, but the
   * class might me managed by a parent collection. If the cache algorithm for
   * the current class is hardcoded, most recently used, or everyting, then
   * refresh the collection. Otherwise recursively call the parent class.
   * 
   * <br/>
   * <b>Precondition: </b> mdEntity != null <br/>
   * <b>Postcondition: </b> cache that manages objects of the given class (if
   * any) is rebuilt.
   * 
   * @param mdEntity
   *          current class
   */
  public static void updateParentCacheStrategy(MdEntityDAO mdEntity)
  {
    BusinessDAOIF cacheEnumItem = mdEntity.getCacheAlgorithm();
    int cacheCode = new Integer(cacheEnumItem.getAttributeIF(EntityCacheMaster.CACHE_CODE).getValue()).intValue();

    if (cacheCode == EntityCacheMaster.CACHE_EVERYTHING.getCacheCode() || cacheCode == EntityCacheMaster.CACHE_MOST_RECENTLY_USED.getCacheCode() || cacheCode == EntityCacheMaster.CACHE_HARDCODED.getCacheCode())

    {
      updateCacheStrategy(mdEntity);
    }
    else
    {
      MdEntityDAOIF parentMdEntityIF = mdEntity.getSuperClass();
      if (parentMdEntityIF != null)
      {
        updateParentCacheStrategy(parentMdEntityIF.getBusinessDAO());
      }
    }
  }

  /**
   * 
   * @param mdEntity
   */
  public static void updateCacheStrategy(MdEntityDAO mdEntity)
  {
    BusinessDAOIF cacheEnumItem = mdEntity.getCacheAlgorithm();
    int cacheCode = new Integer(cacheEnumItem.getAttributeIF(EntityCacheMaster.CACHE_CODE).getValue()).intValue();

    // Skip checking the parent entity if the entity is the root of a hierarchy
    // (and consequently no parent)

    if (! ( mdEntity.isRootOfHierarchy() ))
    {
      MdEntityDAOIF parentMdEntity = mdEntity.getSuperClass();
      validateCacheCodeWithParent((MdElementDAOIF) parentMdEntity, (MdElementDAOIF) mdEntity);
    }

    String entityType = mdEntity.definesType();

    // Cache everything.
    if (cacheCode == EntityCacheMaster.CACHE_EVERYTHING.getCacheCode())
    {
      CacheAllStrategy cacheAllStrategy;
      if (mdEntity instanceof MdBusinessDAOIF)
      {
        cacheAllStrategy = new CacheAllBusinessDAOstrategy((MdBusinessDAOIF) mdEntity);
      }
      else if (mdEntity instanceof MdStructDAOIF)
      {
        cacheAllStrategy = new CacheAllStructDAOStrategy((MdStructDAOIF) mdEntity);
      }
      else
      {
        cacheAllStrategy = new CacheAllRelationshipDAOStrategy((MdRelationshipDAOIF) mdEntity);
      }
      addCacheStrategy(entityType, cacheAllStrategy);
    }
    // Cache nothing. Remove any existing collection for this class
    else if (cacheCode == EntityCacheMaster.CACHE_NOTHING.getCacheCode())
    {
      removeCacheStrategy(entityType);
    }
    // Cache most recently used.
    else if (cacheCode == EntityCacheMaster.CACHE_MOST_RECENTLY_USED.getCacheCode())
    {
      CacheMRUStrategy cacheMRUStrategy = new CacheMRUStrategy(entityType, mdEntity.getCacheSize());
      addCacheStrategy(entityType, cacheMRUStrategy);
    }
    // Hardcoded in the core. If a hardcoded metadata collection needs to be
    // refreshed, it is
    // simpler just to refresh the entire cache.
    else if (cacheCode == EntityCacheMaster.CACHE_HARDCODED.getCacheCode())
    {
      refreshTheEntireCache();
    }
    else
    {
      String error = "Cache algorithm [" + cacheEnumItem.getValue(EnumerationMasterInfo.NAME) + "] for objects is undefined.";
      MdTypeDAOIF cache_definitions = MdTypeDAO.getMdTypeDAO(MdEnumerationTypes.OBJECT_TYPE_CACHE.getType());
      throw new DataNotFoundException(error, cache_definitions);
    }

    // Update all applicable child class caches
    List<? extends MdEntityDAOIF> mdEntityList = mdEntity.getSubClasses();
    for (MdEntityDAOIF childMdEntityIF : mdEntityList)
    {
      MdEntityDAO childMdEntinty = childMdEntityIF.getBusinessDAO();
      updateChildCacheCode((MdElementDAO) mdEntity, (MdElementDAO) childMdEntinty);
    }

    // Update parent class cache contents
    MdElementDAO parentMdElement = null;

    // We can only call getBusinessDAO if there is a superEntity (i.e. it's not
    // null)
    MdEntityDAOIF superMdEntity = mdEntity.getSuperClass();
    if (superMdEntity != null)
    {
      // If it has a super entity, then it is an element object.
      parentMdElement = ( (MdElementDAO) superMdEntity ).getBusinessDAO();
      updateParentCacheContents(parentMdElement, (MdElementDAO) mdEntity);
    }
    // Do nothing when deferring to the parent class's algorithm
  }

  /**
   * This is a hook method for aspects. If this method is called during a
   * transaction, then the entire cache needs to be refreshed.
   */
  public static void refreshTheEntireCache()
  {

  }

  /**
   * 
   * @param entityType
   */
  public static void addCacheStrategy(String entityType, CacheStrategy collection)
  {
    // Remove references in the global cache, if there are any
    CacheStrategy collectionOld = strategyMap.get(entityType);

    if (collectionOld != null)
    {
      strategyMap.remove(entityType);
      collectionOld.removeCacheStrategy();

    }

    strategyMap.put(entityType, collection);
  }

  /**
   * Removes any collection that manages the given type from the database.
   * 
   * <br/>
   * <b>Precondition:</b> !entityType != null
   * 
   * @param entityType
   *          class who's collection (if any) is removed.
   */
  public static void removeCacheStrategy(String entityType)
  {
    CacheStrategy collection = strategyMap.get(entityType);

    if (collection != null)
    {
      strategyMap.remove(entityType);
      collection.removeCacheStrategy();
    }
  }

  /**
   * Removes the entity objects in the given map from the global cache.
   * 
   * @param _entityDAOIdSet
   */
  protected static void removeFromGlobalCache(Set<String> _entityDAOIdSet)
  {
    Iterator<String> i = _entityDAOIdSet.iterator();

    while (i.hasNext())
    {
      ObjectCache.removeEntityDAOIFfromCache(i.next(), false);
    }
  }

  /**
   *
   *
   */
  public static Iterator<String> getCollectionMapKeys()
  {
    return strategyMap.keySet().iterator();
  }

  /**
   * 
   * @param parentMdEntity
   * @param childMdEntity
   */
  private static void validateCacheCodeWithParent(MdElementDAOIF parentMdEntity, MdElementDAOIF childMdEntity)
  {
    BusinessDAOIF parentCacheEnumItem = parentMdEntity.getCacheAlgorithm();
    int parentCacheCode = Integer.parseInt(parentCacheEnumItem.getAttributeIF(EntityCacheMaster.CACHE_CODE).getValue());

    BusinessDAOIF childCacheEnumItem = childMdEntity.getCacheAlgorithm();
    int childCacheCode = Integer.parseInt(childCacheEnumItem.getAttributeIF(EntityCacheMaster.CACHE_CODE).getValue());

    // If child is Hardcoded, only parent Hardcoded is valid
    // The Component class is not hardcoded, but that is OK.
    if (childCacheCode == EntityCacheMaster.CACHE_HARDCODED.getCacheCode() && ( parentCacheCode != EntityCacheMaster.CACHE_HARDCODED.getCacheCode() && parentCacheCode != EntityCacheMaster.CACHE_NOTHING.getCacheCode() ))
    {
      String error = "A class can only select Hardcoded caching if its parent is also Hardcoded. [" + ( (AttributeLocalIF) childMdEntity.getAttributeIF(MdTypeInfo.DISPLAY_LABEL) ).getValue(MdAttributeLocalInfo.DEFAULT_LOCALE) + "]'s parent [" + ( (AttributeLocalIF) parentMdEntity.getAttributeIF(MdTypeInfo.DISPLAY_LABEL) ).getValue(MdAttributeLocalInfo.DEFAULT_LOCALE) + "] is set to cache " + ( (AttributeLocalIF) parentCacheEnumItem.getAttributeIF(EntityCacheMaster.DISPLAY_LABEL) ).getValue(MdAttributeLocalInfo.DEFAULT_LOCALE);
      throw new CacheCodeException(error, childMdEntity, parentMdEntity);
    }

    // If parent is Hardcoded, only None and Hardcoded are valid for the child
    if (parentCacheCode == EntityCacheMaster.CACHE_HARDCODED.getCacheCode() && ( childCacheCode == EntityCacheMaster.CACHE_EVERYTHING.getCacheCode() || childCacheCode == EntityCacheMaster.CACHE_MOST_RECENTLY_USED.getCacheCode() ))
    {
      String error = "Caching " + ( (AttributeLocalIF) childCacheEnumItem.getAttributeIF(EntityCacheMaster.DISPLAY_LABEL) ).getValue(MdAttributeLocalInfo.DEFAULT_LOCALE) + " is invalid for [" + ( (AttributeStructIF) childMdEntity.getAttributeIF(MdTypeInfo.DISPLAY_LABEL) ).getValue(MdAttributeLocalInfo.DEFAULT_LOCALE) + "]. Its parent class [" + ( (AttributeStructIF) parentMdEntity.getAttributeIF(MdTypeInfo.DISPLAY_LABEL) ).getValue(MdAttributeLocalInfo.DEFAULT_LOCALE) + "] is Hardcoded, and all children of Hardcoded classes must cache Hardcoded or Nothing.";
      throw new CacheCodeException(error, childMdEntity, parentMdEntity);
    }

    // If child is MRU, only None is valid for its parent
    if (childCacheCode == EntityCacheMaster.CACHE_MOST_RECENTLY_USED.getCacheCode() && parentCacheCode != EntityCacheMaster.CACHE_NOTHING.getCacheCode())
    {
      String error = "[" + ( (AttributeStructIF) childMdEntity.getAttributeIF(MdTypeInfo.DISPLAY_LABEL) ).getValue(MdAttributeLocalInfo.DEFAULT_LOCALE) + "] cannot cache Most Recently Used.  MRU is only valid if the parent caches Nothing. [" + ( (AttributeStructIF) parentMdEntity.getAttributeIF(MdTypeInfo.DISPLAY_LABEL) ).getValue(MdAttributeLocalInfo.DEFAULT_LOCALE) + "] is caching " + ( (AttributeLocalIF) parentCacheEnumItem.getAttributeIF(EntityCacheMaster.DISPLAY_LABEL) ).getValue(MdAttributeLocalInfo.DEFAULT_LOCALE) + ".";
      throw new CacheCodeException(error, childMdEntity, parentMdEntity);
    }

    // If parent and child both cache all, yell about it
    if (parentCacheCode == EntityCacheMaster.CACHE_EVERYTHING.getCacheCode() && childCacheCode == EntityCacheMaster.CACHE_EVERYTHING.getCacheCode())
    {
      String error = "[" + ( (AttributeStructIF) childMdEntity.getAttributeIF(MdTypeInfo.DISPLAY_LABEL) ).getValue(MdAttributeLocalInfo.DEFAULT_LOCALE) + "] cannot cache Everything.  Its parent class [" + ( (AttributeStructIF) parentMdEntity.getAttributeIF(MdTypeInfo.DISPLAY_LABEL) ).getValue(MdAttributeLocalInfo.DEFAULT_LOCALE) + "] is already caching Everything.";
      throw new CacheCodeException(error, childMdEntity, parentMdEntity);
    }

    MdElementDAOIF grandParentMdEntity = parentMdEntity.getSuperClass();

    if (grandParentMdEntity != null)
    {
      validateCacheCodeWithParent(grandParentMdEntity, childMdEntity);
    }
  }

  /**
   * 
   * @param parentMdElement
   * @param childMdElement
   */
  private static void updateChildCacheCode(MdElementDAO parentMdElement, MdElementDAO childMdElement)
  {
    BusinessDAOIF parentCacheEnumItem = parentMdElement.getCacheAlgorithm();
    int parentCacheCode = Integer.parseInt(parentCacheEnumItem.getAttributeIF(EntityCacheMaster.CACHE_CODE).getValue());

    BusinessDAOIF childCacheEnumItem = childMdElement.getCacheAlgorithm();
    int childCacheCode = Integer.parseInt(childCacheEnumItem.getAttributeIF(EntityCacheMaster.CACHE_CODE).getValue());

    // Cache everything.
    if (parentCacheCode == EntityCacheMaster.CACHE_EVERYTHING.getCacheCode())
    {
      if (childCacheCode == EntityCacheMaster.CACHE_EVERYTHING.getCacheCode() || childCacheCode == EntityCacheMaster.CACHE_MOST_RECENTLY_USED.getCacheCode())
      {
        clearChildCollection(childMdElement);
      }
    }
    // Cache nothing. And consequently, do nothing. It does not matter what the
    // child cache algorithm is
    else if (parentCacheCode == EntityCacheMaster.CACHE_NOTHING.getCacheCode())
    {
    }
    else if (parentCacheCode == EntityCacheMaster.CACHE_MOST_RECENTLY_USED.getCacheCode())
    {
      if (childCacheCode == EntityCacheMaster.CACHE_MOST_RECENTLY_USED.getCacheCode() || childCacheCode == EntityCacheMaster.CACHE_EVERYTHING.getCacheCode())
      {
        clearChildCollection(childMdElement);
      }
    }
    else if (parentCacheCode == EntityCacheMaster.CACHE_HARDCODED.getCacheCode())
    {
      if (childCacheCode == EntityCacheMaster.CACHE_EVERYTHING.getCacheCode() || childCacheCode == EntityCacheMaster.CACHE_MOST_RECENTLY_USED.getCacheCode())
      {
        clearChildCollection(childMdElement);
      }
    }

    List<? extends MdElementDAOIF> mdElementIFList = childMdElement.getSubClasses();
    for (MdElementDAOIF mdElementIF : mdElementIFList)
    {
      MdElementDAO grandChildMdElement = mdElementIF.getBusinessDAO();
      updateChildCacheCode(parentMdElement, grandChildMdElement);
    }
  }

  /**
   * When a parent caches Most Recently Used and a child starts caching
   * Everything, the parent collection might contain instances of the child. We
   * don't want that, so this method checks for that condition, and forces a
   * refresh of the parent cache when necessary.
   * 
   * @param parent
   *          The parent MdEntity
   * @param child
   *          The child MdEntity
   */
  private static void updateParentCacheContents(MdElementDAO parent, MdElementDAO child)
  {
    int childCacheCode = Integer.parseInt(child.getCacheAlgorithm().getAttributeIF(EntityCacheMaster.CACHE_CODE).getValue());
    int parentCacheCode = Integer.parseInt(parent.getCacheAlgorithm().getAttributeIF(EntityCacheMaster.CACHE_CODE).getValue());

    CacheStrategy parentCollection = getTypeCollection(parent.definesType());

    // The only interesting case is parent caching MRU and child caching
    // everything
    if (parentCacheCode == EntityCacheMaster.CACHE_MOST_RECENTLY_USED.getCacheCode() && childCacheCode == EntityCacheMaster.CACHE_EVERYTHING.getCacheCode())
    {
      parentCollection.reload();
    }

    MdElementDAOIF grandparent = parent.getSuperClass();
    if (grandparent != null)
    {
      updateParentCacheContents(grandparent.getBusinessDAO(), child);
    }
  }

  /**
   * 
   * @param childMdElement
   */
  private static void clearChildCollection(MdElementDAO childMdElement)
  {
    childMdElement.getAttribute(MdElementInfo.CACHE_ALGORITHM).setValue(EntityCacheMaster.CACHE_NOTHING.getId());
    childMdElement.save(true);
  }

  /**
   * Returns the collection that is responsible for EntityDAOs of the given
   * type.
   * 
   * <br/>
   * <b>Precondition:</b> entityType != null <br/>
   * <b>Precondition:</b> !entityType.trim().equals("") <br/>
   * <b>Postcondition:</b> return value may not be null
   * 
   * @param entityType
   *          Returns the collection responsible for the entity of this name.
   * @return the collection that is responsible for EntityDAOs of the given
   *         type.
   */
  public static CacheStrategy getTypeCollection(String entityType)
  {
    // use the collection defined for this class, if there is one
    if (strategyMap.containsKey(entityType))
    {
      return strategyMap.get(entityType);
    }

    // This is necessary or else infinite recursion is caused, as calling
    // getSuperClasses will call this method again.
    if (entityType.equals(MdStateMachineInfo.CLASS) || entityType.equals(MdTermInfo.CLASS) || entityType.equals(MdBusinessInfo.CLASS) || entityType.equals(MdStructInfo.CLASS) || entityType.equals(MdGraphInfo.CLASS) || entityType.equals(MdTermRelationshipInfo.CLASS) || entityType.equals(MdTreeInfo.CLASS) || entityType.equals(MdRelationshipInfo.CLASS) || entityType.equals(MdViewInfo.CLASS) || entityType.equals(MdUtilInfo.CLASS) || entityType.equals(MdExceptionInfo.CLASS) || entityType.equals(MdProblemInfo.CLASS) || entityType.equals(MdInformationInfo.CLASS) || entityType.equals(MdWarningInfo.CLASS))
    {
      return strategyMap.get(MdClassInfo.CLASS);
    }

    // If no collection is defined for this class, look for one
    // that is defined for the most immediate parent class

    MdEntityDAOIF mdEntityIF = MdEntityDAO.getMdEntityDAO(entityType);

    List<? extends MdEntityDAOIF> superMdEntityList = mdEntityIF.getSuperClasses();
    for (MdEntityDAOIF currMdEntityIF : superMdEntityList)
    {
      String inheritedEntityType = currMdEntityIF.definesType();
      if (strategyMap.containsKey(inheritedEntityType))
      {
        return strategyMap.get(inheritedEntityType);
      }
    }

    // If no match has been found, then use the collection for the Component
    // class which is the default
    // collection class.
    if (mdEntityIF instanceof MdBusinessDAOIF)
    {
      return strategyMap.get(BusinessInfo.CLASS);
    }
    else if (mdEntityIF instanceof MdRelationshipDAOIF)
    {
      return strategyMap.get(RelationshipInfo.CLASS);
    }
    else
    {
      return strategyMap.get(STRUCT);
    }
  }

  /**
   * Returns a MdBusinessIF instance of the metadata for the given type.
   * 
   * <br/>
   * <b>Precondition:</b> entityType != null <br/>
   * <b>Precondition:</b> !entityType.trim().equals("") <br/>
   * <b>Precondition:</b> entityType is a valid class defined in the database <br/>
   * <b>Postcondition:</b> return value is not null <br/>
   * <b>Postcondition:</b> Returns a mdStructIF instance of the metadata for the
   * given class (mdStructIF().definesType().equals(entityType)
   * 
   * @param entityType
   * @return MdBusinessIF instance of the metadata for the given type.
   */
  public static MdBusinessDAOIF getMdBusinessDAO(String entityType)
  {
    MdClassDAOIF mdClassIF = getMdClassDAO(entityType);

    if (! ( mdClassIF instanceof MdBusinessDAOIF ))
    {
      String errmsg = "Type [" + entityType + "] is not an MdBusiness.";
      throw new UnexpectedTypeException(errmsg);
    }

    return (MdBusinessDAOIF) mdClassIF;
  }

  /**
   * @param entityType
   * @return
   */
  public static MdTermDAOIF getMdTermDAO(String entityType)
  {
    MdClassDAOIF mdClassIF = getMdClassDAO(entityType);

    if (! ( mdClassIF instanceof MdTermDAOIF ))
    {
      String errmsg = "Type [" + entityType + "] is not an MdTerm.";
      throw new UnexpectedTypeException(errmsg);
    }

    return (MdTermDAOIF) mdClassIF;
  }

  /**
   * Returns a MdStructIF instance of the metadata for the given type.
   * 
   * <br/>
   * <b>Precondition:</b> entityType != null <br/>
   * <b>Precondition:</b> !entityType.trim().equals("") <br/>
   * <b>Precondition:</b> entityType is a valid class defined in the database <br/>
   * <b>Postcondition:</b> return value is not null <br/>
   * <b>Postcondition:</b> Returns a MdStructIF instance of the metadata for the
   * given class (MdStructIF().definesType().equals(entityType)
   * 
   * @param entityType
   * @return MdStructIF instance of the metadata for the given type.
   */
  public static MdStructDAOIF getMdStructDAO(String entityType)
  {
    MdClassDAOIF mdClassIF = getMdClassDAO(entityType);

    if (! ( mdClassIF instanceof MdStructDAOIF ))
    {
      String errmsg = "Type [" + entityType + "] is not an  [" + MdStructInfo.CLASS + "]";
      throw new UnexpectedTypeException(errmsg);
    }

    return (MdStructDAOIF) mdClassIF;
  }

  /**
   * Returns a MdLocalStructDAOIF instance of the metadata for the given class.
   * 
   * <br/>
   * <b>Precondition:</b> localStructType != null <br/>
   * <b>Precondition:</b> !localStructType.trim().equals("") <br/>
   * <b>Precondition:</b> localStructType is a valid class defined in the
   * database <br/>
   * <b>Postcondition:</b> return value is not null <br/>
   * <b>Postcondition:</b> Returns a MdLocalStructDAOIF instance of the metadata
   * for the given class
   * (MdLocalStructDAOIF().definesType().equals(localStructType)
   * 
   * @param localStructType
   *          class type
   * @return MdLocalStructDAOIF instance of the metadata for the given class
   *         type.
   */
  public static MdLocalStructDAOIF getMdLocalStructDAO(String entityType)
  {
    MdClassDAOIF mdClassIF = getMdClassDAO(entityType);

    if (! ( mdClassIF instanceof MdLocalStructDAOIF ))
    {
      String errmsg = "Type [" + entityType + "] is not an  [" + MdLocalStructInfo.CLASS + "]";
      throw new UnexpectedTypeException(errmsg);
    }

    return (MdLocalStructDAOIF) mdClassIF;
  }

  /**
   * 
   * @param type
   * @return
   */
  public static MdTypeDAOIF getMdTypeDAO(String type)
  {    
    MdTypeDAOIF mdType = null;

    mdType = getMdClassDAOReturnNull(type);
    if (mdType == null)
    {
      mdType = getMdFacadeDAOReturnNull(type);
      if (mdType == null)
      {
        mdType = getMdControllerDAOReturnNull(type);
        if (mdType == null)
        {
          mdType = getMdFormDAOReturnNull(type);
          if (mdType == null)
          {
            mdType = getMdEnumerationDAO(type);
          }
        }
      }
    }

    return mdType;
  }

  /**
   * Returns an <code>MdEntityDAOIF</code> instance of the metadata for the given type.
   * 
   * <br/>
   * <b>Precondition:</b> entityType != null <br/>
   * <b>Precondition:</b> !entityType.trim().equals("") <br/>
   * <b>Precondition:</b> entityType is a valid class defined in the database <br/>
   * <b>Postcondition:</b> Returns a MdEntityIF instance of the metadata for the
   * given class (MdEntityIF().definesType().equals(entityType)
   * 
   * @param entityType
   * @return MdEntityIF instance of the metadata for the given type.
   */
  public static MdEntityDAOIF getMdEntityDAO(String entityType)
  {
    MdClassDAOIF mdClassIF = getMdClassDAO(entityType);

    if (! ( mdClassIF instanceof MdEntityDAOIF ))
    {
      String errmsg = "Type [" + entityType + "] is not an [" + MdEntityInfo.CLASS + "].";
      throw new UnexpectedTypeException(errmsg);
    }

    return (MdEntityDAOIF) mdClassIF;
  }
  

  /**
   * Returns an MdUtilIF instance of the metadata for the given type.
   * 
   * <br/>
   * <b>Precondition:</b> utilType != null <br/>
   * <b>Precondition:</b> !utilType.trim().equals("") <br/>
   * <b>Precondition:</b> utilType is a valid class defined in the database <br/>
   * <b>Postcondition:</b> Returns a MdUtilIF instance of the metadata for the
   * given class (MdUtilIF().definesType().equals(utilType)
   * 
   * @param veiwType
   * @return MdUtilIF instance of the metadata for the given type.
   */
  public static MdUtilDAOIF getMdUtilDAO(String utilType)
  {
    MdClassDAOIF mdClassIF = getMdClassDAO(utilType);

    if (! ( mdClassIF instanceof MdUtilDAOIF ))
    {
      String errmsg = "Type [" + utilType + "] is not an [" + MdUtilInfo.CLASS + "].";
      throw new UnexpectedTypeException(errmsg);
    }

    return (MdUtilDAOIF) mdClassIF;
  }

  /**
   * Returns an MdViewIF instance of the metadata for the given type.
   * 
   * <br/>
   * <b>Precondition:</b> veiwType != null <br/>
   * <b>Precondition:</b> !veiwType.trim().equals("") <br/>
   * <b>Precondition:</b> veiwType is a valid class defined in the database <br/>
   * <b>Postcondition:</b> Returns a MdViewIF instance of the metadata for the
   * given class (MdViewIF().definesType().equals(veiwType)
   * 
   * @param veiwType
   * @return MdViewIF instance of the metadata for the given type.
   */
  public static MdViewDAOIF getMdViewDAO(String veiwType)
  {
    MdClassDAOIF mdClassIF = getMdClassDAO(veiwType);

    if (! ( mdClassIF instanceof MdViewDAOIF ))
    {
      String errmsg = "Type [" + veiwType + "] is not an [" + MdViewInfo.CLASS + "].";
      throw new UnexpectedTypeException(errmsg);
    }

    return (MdViewDAOIF) mdClassIF;
  }

  /**
   * Returns an MdTransientIF instance of the metadata for the given type.
   * 
   * <br/>
   * <b>Precondition:</b> transientType != null <br/>
   * <b>Precondition:</b> !transientType.trim().equals("") <br/>
   * <b>Precondition:</b> transientType is a valid class defined in the database <br/>
   * <b>Postcondition:</b> Returns a MdTransientIF instance of the metadata for
   * the given class (MdTransientIF().definesType().equals(transientType)
   * 
   * @param transientType
   * @return MdTransientIF instance of the metadata for the given type.
   */
  public static MdTransientDAOIF getMdTransientDAO(String transientType)
  {
    MdClassDAOIF mdClassIF = getMdClassDAO(transientType);

    if (! ( mdClassIF instanceof MdTransientDAOIF ))
    {
      String errmsg = "Type [" + transientType + "] is not an [" + MdTransientInfo.CLASS + "].";
      throw new UnexpectedTypeException(errmsg);
    }

    return (MdTransientDAOIF) mdClassIF;
  }

  /**
   * Returns an MdExceptionIF instance of the metadata for the given type.
   * 
   * <br/>
   * <b>Precondition:</b> exceptionType != null <br/>
   * <b>Precondition:</b> !exceptionType.trim().equals("") <br/>
   * <b>Precondition:</b> exceptionType is a valid class defined in the database <br/>
   * <b>Postcondition:</b> Returns a MdExceptionIF instance of the metadata for
   * the given class (MdExceptionIF().definesType().equals(exceptionType)
   * 
   * @param exceptionType
   * @return MdExceptionIF instance of the metadata for the given type.
   */
  public static MdExceptionDAOIF getMdExceptionDAO(String exceptionType)
  {
    MdClassDAOIF mdClassIF = getMdClassDAO(exceptionType);

    if (! ( mdClassIF instanceof MdExceptionDAOIF ))
    {
      String errmsg = "Type [" + exceptionType + "] is not an [" + MdExceptionInfo.CLASS + "].";
      throw new UnexpectedTypeException(errmsg);
    }

    return (MdExceptionDAOIF) mdClassIF;
  }

  /**
   * Returns an MdProblemIF instance of the metadata for the given type.
   * 
   * <br/>
   * <b>Precondition:</b> problemType != null <br/>
   * <b>Precondition:</b> !problemType.trim().equals("") <br/>
   * <b>Precondition:</b> problemType is a valid class defined in the database <br/>
   * <b>Postcondition:</b> Returns a MdProblemIF instance of the metadata for
   * the given class (MdProblemIF().definesType().equals(problemType)
   * 
   * @param problemType
   * @return MdProblemIF instance of the metadata for the given type.
   */
  public static MdProblemDAOIF getMdProblemDAO(String problemType)
  {
    MdClassDAOIF mdClassIF = getMdClassDAO(problemType);

    if (! ( mdClassIF instanceof MdProblemDAOIF ))
    {
      String errmsg = "Type [" + problemType + "] is not an [" + MdProblemInfo.CLASS + "].";
      throw new UnexpectedTypeException(errmsg);
    }

    return (MdProblemDAOIF) mdClassIF;
  }

  /**
   * Returns an MdMessageIF instance of the metadata for the given type.
   * 
   * <br/>
   * <b>Precondition:</b> messageType != null <br/>
   * <b>Precondition:</b> !messageType.trim().equals("") <br/>
   * <b>Precondition:</b> messageType is a valid class defined in the database <br/>
   * <b>Postcondition:</b> Returns a MdMessageIF instance of the metadata for
   * the given class (MdMessageIF().definesType().equals(messageType)
   * 
   * @param problemType
   * @return MdProblemIF instance of the metadata for the given type.
   */
  public static MdMessageDAOIF getMdMessageDAO(String messageType)
  {
    MdClassDAOIF mdClassIF = getMdClassDAO(messageType);

    if (! ( mdClassIF instanceof MdMessageDAOIF ))
    {
      String errmsg = "Type [" + messageType + "] is not an [" + MdMessageInfo.CLASS + "].";
      throw new UnexpectedTypeException(errmsg);
    }

    return (MdMessageDAOIF) mdClassIF;
  }

  /**
   * Returns an MdWarningIF instance of the metadata for the given type.
   * 
   * <br/>
   * <b>Precondition:</b> warningType != null <br/>
   * <b>Precondition:</b> !warningType.trim().equals("") <br/>
   * <b>Precondition:</b> warningType is a valid class defined in the database <br/>
   * <b>Postcondition:</b> Returns a MdWarningIF instance of the metadata for
   * the given class (MdWarningIF().definesType().equals(warningType)
   * 
   * @param problemType
   * @return MdWarningIF instance of the metadata for the given type.
   */
  public static MdWarningDAOIF getMdWarningDAO(String warningType)
  {
    MdClassDAOIF mdClassIF = getMdClassDAO(warningType);

    if (! ( mdClassIF instanceof MdWarningDAOIF ))
    {
      String errmsg = "Type [" + warningType + "] is not an [" + MdWarningInfo.CLASS + "].";
      throw new UnexpectedTypeException(errmsg);
    }

    return (MdWarningDAOIF) mdClassIF;
  }

  /**
   * Returns an MdInformationIF instance of the metadata for the given type.
   * 
   * <br/>
   * <b>Precondition:</b> informationType != null <br/>
   * <b>Precondition:</b> !informationType.trim().equals("") <br/>
   * <b>Precondition:</b> informationType is a valid class defined in the
   * database <br/>
   * <b>Postcondition:</b> Returns a MdInformationIF instance of the metadata
   * for the given class
   * (MdInformationIF().definesType().equals(informationType)
   * 
   * @param problemType
   * @return MdInformationIF instance of the metadata for the given type.
   */
  public static MdInformationDAOIF getMdInformationDAO(String informationType)
  {
    MdClassDAOIF mdClassIF = getMdClassDAO(informationType);

    if (! ( mdClassIF instanceof MdInformationDAOIF ))
    {
      String errmsg = "Type [" + informationType + "] is not an [" + MdWarningInfo.CLASS + "].";
      throw new UnexpectedTypeException(errmsg);
    }

    return (MdInformationDAOIF) mdClassIF;
  }

  /**
   * Returns and object of the specified master type with the specified
   * enumeration name from the database.
   * 
   * @param masterType
   *          The type of the Enumeration Master
   * @param enumName
   *          The name of the Enumerated item.
   * @return EnumerationItem representing the enumerated item in the database
   */
  public static EnumerationItemDAO getEnumeration(String masterType, String enumName)
  {
    EnumerationMasterStrategy enumerationMasterStrategy = (EnumerationMasterStrategy) strategyMap.get(EnumerationMasterInfo.CLASS);

    return enumerationMasterStrategy.getEnumeration(masterType, enumName);
  }

  /**
   * Returns a <code>MdClassDAOIF</code> instance that defines the class of the given type.
   * Throws <code>DataNotFoundException</code> if type unknown.
   * 
   * <br/>
   * <b>Precondition:</b> type != null <br/>
   * <b>Precondition:</b> !type.trim().equals("") <br/>
   * <b>Precondition:</b> type is a valid class defined in the database <br/>
   * <b>Postcondition:</b> Returns a MdClassIF where
   * (mdClass.getType().equals(type)
   * 
   * @param type
   *          Name of the class.
   * @return <code>MdClassDAOIF</code> instance that defines the class of the given type.
   */
  public static MdClassDAOIF getMdClassDAO(String classType)
  {
    MdClassStrategy mdClassStrategy = (MdClassStrategy) strategyMap.get(MdClassInfo.CLASS);

    return mdClassStrategy.getMdClass(classType);
  }
  
 
  /**
   * Returns a <code>MdClassDAOIF</code> instance that defines the class of the given type.
   * Returns null if type is unknown.
   * 
   * <br/>
   * <b>Precondition:</b> type != null <br/>
   * <b>Precondition:</b> !type.trim().equals("") <br/>
   * <b>Precondition:</b> type is a valid class defined in the database <br/>
   * <b>Postcondition:</b> Returns a MdClassIF where
   * (mdClass.getType().equals(type)
   * 
   * @param type
   *          Name of the class.
   * @return <code>MdClassDAOIF</code> instance that defines the class of the given type.
   */
  public static MdClassDAOIF getMdClassDAOReturnNull(String classType)
  {
    MdClassStrategy mdClassStrategy = (MdClassStrategy) strategyMap.get(MdClassInfo.CLASS);

    return mdClassStrategy.getMdClassReturnNull(classType);
  }
  

  /**
   * Returns the <code>MdEntityDAOIF</code> instance that defines the given table name.
   * 
   * @param tableName
   * 
   * @return <code>MdEntityDAOIF</code> that defines the table with the given name.
   */
  public static MdEntityDAOIF getMdEntityByTableName(String tableName)
  {
    MdClassStrategy mdClassStrategy = (MdClassStrategy) strategyMap.get(MdClassInfo.CLASS);

    return mdClassStrategy.getMdEntityByTableName(tableName);
  }
  
  /**
   * Returns a MdClassIF instance with a root id that matches the given value.
   * 
   * <br/>
   * <b>Precondition:</b> rootId != null <br/>
   * <b>Precondition:</b> !rootId.trim().equals("") <br/>
   * <b>Precondition:</b> rootId is the root of an id that is a valid class
   * defined in the database <br/>
   * <b>Postcondition:</b> Returns a MdClassIF where
   * IdParser.parseRootFromId(mdClass.getId()).equals(rootId)
   * 
   * @param rootId
   *          of the MdClass.
   * @return MdClassIF instance with a root id that matches the given value.
   */
  public static MdClassDAOIF getMdClassDAOByRootId(String rootId)
  {
    CacheStrategy mdClassStrategy = (MdClassStrategy) strategyMap.get(MdClassInfo.CLASS);

    if (mdClassStrategy == null)
    {
      String errMsg = "No cache collection for [" + MdClassInfo.CLASS + "] defined.";
      throw new MetadataException(errMsg);
    }

    return ( (MdClassStrategy) mdClassStrategy ).getMdClassByRootId(rootId);
  }

  /**
   * Returns a set of <code>MdRelationshipDAOIF</code> ids for relationships in
   * which the <code>MdBusinessDAOIF</code> with the given id participates as a
   * parent.
   * 
   * @return set of <code>MdRelationshipDAOIF</code> ids
   */
  public static Set<String> getParentMdRelationshipDAOids(String mdBusinessDAOid)
  {
    CacheStrategy mdClassStrategy = (MdClassStrategy) strategyMap.get(MdClassInfo.CLASS);

    if (mdClassStrategy == null)
    {
      String errMsg = "No cache collection for [" + MdClassInfo.CLASS + "] defined.";
      throw new MetadataException(errMsg);
    }

    return ( (MdClassStrategy) mdClassStrategy ).getParentMdRelationshipDAOids(mdBusinessDAOid);
  }

  /**
   * Returns a set of <code>MdRelationshipDAOIF</code> ids for relationships in
   * which the <code>MdBusinessDAOIF</code> with the given id participates as a
   * child.
   * 
   * @return set of <code>MdRelationshipDAOIF</code> ids
   */
  public static Set<String> getChildMdRelationshipDAOids(String mdBusinessDAOid)
  {
    CacheStrategy mdClassStrategy = (MdClassStrategy) strategyMap.get(MdClassInfo.CLASS);

    if (mdClassStrategy == null)
    {
      String errMsg = "No cache collection for [" + MdClassInfo.CLASS + "] defined.";
      throw new MetadataException(errMsg);
    }

    return ( (MdClassStrategy) mdClassStrategy ).getChildMdRelationshipDAOids(mdBusinessDAOid);
  }

  /**
   * Returns an <code>MdFacadeDAOIF</code> instance of the metadata for the given type.
   * Throws <code>DataNotFoundException</code> if the type is unknown.
   * 
   * <br/>
   * <b>Precondition:</b> facadeType != null <br/>
   * <b>Precondition:</b> !facadeType.trim().equals("") <br/>
   * <b>Precondition:</b> facadeType is a valid class defined in the database <br/>
   * <b>Postcondition:</b> Returns a MdFacadeIF instance of the metadata for the
   * given type (MdFacadeIF().definesType().equals(facadeType)
   * 
   * @param facadeType
   * @return <code>MdFacadeDAOIF</code> instance of the metadata for the given type.
   */
  public static MdFacadeDAOIF getMdFacadeDAO(String facadeType)
  {
    MdFacadeStrategy mdFacadeStrategy = (MdFacadeStrategy) strategyMap.get(MdFacadeInfo.CLASS);

    return mdFacadeStrategy.getMdFacade(facadeType);
  }

  /**
   * Returns an <code>MdFacadeDAOIF</code> instance of the metadata for the given type.
   * Returns null if the type is unknown.
   * 
   * <br/>
   * <b>Precondition:</b> facadeType != null <br/>
   * <b>Precondition:</b> !facadeType.trim().equals("") <br/>
   * <b>Precondition:</b> facadeType is a valid class defined in the database <br/>
   * <b>Postcondition:</b> Returns a <code>MdFacadeDAOIF</code> instance of the metadata for the
   * given type (MdFacadeIF().definesType().equals(facadeType)
   * 
   * @param facadeType
   * @return <code>MdFacadeDAOIF</code> instance of the metadata for the given type.
   */
  public static MdFacadeDAOIF getMdFacadeDAOReturnNull(String facadeType)
  {
    MdFacadeStrategy mdFacadeStrategy = (MdFacadeStrategy) strategyMap.get(MdFacadeInfo.CLASS);

    return mdFacadeStrategy.getMdFacadeReturnNull(facadeType);
  }
  
  /**
   * Returns an <code>MdControllerDAOIF</code> instance of the metadata for the given type.
   * Throws <code>DataNotFoundException</code> if the type is unknown.
   * 
   * @param type
   * @return <code>MdControllerDAOIF</code>
   */
  public static MdControllerDAOIF getMdControllerDAO(String type)
  {
    MdControllerStrategy mdControllerStrategy = (MdControllerStrategy) strategyMap.get(MdControllerInfo.CLASS);

    String controllerId =  mdControllerStrategy.getMdControllerId(type);
    
    return (MdControllerDAOIF)ObjectCache.getEntityDAO(controllerId);
  }
  
  /**
   * Returns an <code>MdControllerDAOIF</code> instance of the metadata for the given type.
   * Throws null if the type is unknown.
   * 
   * @param type
   * @return <code>MdControllerDAOIF</code>
   */
  public static MdControllerDAOIF getMdControllerDAOReturnNull(String type)
  {
    MdControllerStrategy mdControllerStrategy = (MdControllerStrategy) strategyMap.get(MdControllerInfo.CLASS);

    String controllerId =  mdControllerStrategy.getMdControllerIdReturnNull(type);
    
    if (controllerId != null )
    {
      return (MdControllerDAOIF)ObjectCache.getEntityDAO(controllerId);
    }
    else
    {
      return null;
    }
  }

  public static MdFormDAOIF getMdFormDAOgetMdFormDAO(String type)
  {
    MdFormStrategy mdFormStrategy = (MdFormStrategy) strategyMap.get(MdFormInfo.CLASS);

    return (MdFormDAOIF)ObjectCache.getEntityDAO(mdFormStrategy.getMdFormId(type));
  }
  
  public static MdFormDAOIF getMdFormDAOReturnNull(String type)
  {
    MdFormStrategy mdFormStrategy = (MdFormStrategy) strategyMap.get(MdFormInfo.CLASS);

    String mdFormDAOid =  mdFormStrategy.getMdFormReturnIdNull(type);
    
    if (mdFormDAOid != null)
    {
      return (MdFormDAOIF)ObjectCache.getEntityDAO(mdFormStrategy.getMdFormId(type));
    }
    else
    {
      return null;
    }
  }

  /**
   * Returns the MdIndexIF that defines the database index of the given name.
   * 
   * <br/>
   * <b>Precondition:</b> indexName != null <br/>
   * <b>Precondition:</b> !indexName.trim().equals("") <br/>
   * <b>Postcondition:</b> MdIndexIF that defines the database index of the
   * given name.
   * 
   * @param indexName
   *          indexName
   * @return MdIndexIF that defines the database index of the given name.
   */
  public static MdIndexDAOIF getMdIndexDAO(String indexName)
  {
    MdIndexStrategy mdIndexStrategy = (MdIndexStrategy) strategyMap.get(MdIndexInfo.CLASS);

    return mdIndexStrategy.getMdIndex(indexName);
  }

  /**
   * Returns an {@link MdMethodDAOIF} with the given key. The key may not
   * contain the exact type that defines the method, but all types in the
   * hierarchy of the type defined in the key are checked. If a match is not
   * found, then a {@link DataNotFoundException} is thrown.
   * 
   * @param key
   * @return
   * @throws DataNotFoundException
   *           when no method is found with the given key.
   */
  public static MdMethodDAOIF getMdMethod(String key)
  {
    String methodName = key.substring(key.lastIndexOf(".") + 1, key.length());
    String type = key.substring(0, key.lastIndexOf("."));

    // get the appropriate collection class for the entity type
    MdClassDAOIF mdClassIF = MdClassDAO.getMdClassDAO(type);

    CacheAllStrategy mdMethodDAOCollection = (CacheAllStrategy) getTypeCollection(MdMethodInfo.CLASS);

    for (String typeOrSupertype : mdClassIF.getSuperTypes())
    {
      if (mdMethodDAOCollection.entityDAOIdByKeyMap.containsKey(typeOrSupertype + "." + methodName))
      {
        String mdMethodDAOid = mdMethodDAOCollection.entityDAOIdByKeyMap.get(typeOrSupertype + "." + methodName);
        return (MdMethodDAOIF) ( getEntityDAOIFfromCache(mdMethodDAOid) );
      }
    }

    MdClassDAOIF mdMethodMdClassDAOIF = MdClassDAO.getMdClassDAO(MdMethodInfo.CLASS);

    String errMsg = "Unable to locate a method with key [" + key + "]";
    throw new DataNotFoundException(errMsg, mdMethodMdClassDAOIF);
  }

  /**
   * Returns a MdAttributeIF that uses the database index of a given name.
   * 
   * <br/>
   * <b>Precondition:</b> indexName != null <br/>
   * <b>Precondition:</b> !indexName.trim().equals("") <br/>
   * <b>Postcondition:</b> Returns a MdAttributeIF where
   * (MdAttributeIF.getIndexName().equals(indexName)
   * 
   * @param indexName
   *          Name of the database index.
   * @return MdAttributeIF that uses the database index of a given name.
   */
  public static MdAttributeConcreteDAOIF getMdAttributeDAOWithIndex(String indexName)
  {
    MdAttributeStrategy mdAttributeStrategy = (MdAttributeStrategy) strategyMap.get(MdAttributeInfo.CLASS);

    return mdAttributeStrategy.getMdAttributeWithIndex(indexName);
  }

  /**
   * Returns the <code>MdAttributeIF</code> with the given key.
   * 
   * @param key
   * @return <code>MdAttributeIF</code> with the given key.
   */
  public static MdAttributeDAOIF getMdAttributeDAOWithKey(String key)
  {
    MdAttributeStrategy mdAttributeStrategy = (MdAttributeStrategy) strategyMap.get(MdAttributeInfo.CLASS);

    return mdAttributeStrategy.getMdAttributeWithKey(key);
  }

  /**
   * Returns the Role object with the given name.
   * 
   * <br/>
   * <b>Precondition:</b> roleName != null <br/>
   * <b>Precondition:</b> !roleNametrim().equals("") <br/>
   * <b>Precondition:</b> return value is not null
   * 
   * @param roleName
   *          Name of the Role.
   * @return Role with the given name.
   */
  public static RoleDAOIF getRole(String roleName)
  {
    RoleStrategy roleStrategy = (RoleStrategy) strategyMap.get(RoleDAOIF.CLASS);

    return roleStrategy.getRole(roleName);
  }

  /**
   * Returns the MdRelationshipIF that defines the relationship type with the
   * given type.
   * 
   * <br/>
   * <b>Precondition:</b> relationshipType != null <br/>
   * <b>Precondition:</b> !relationshipType.trim().equals("") <br/>
   * <b>Precondition:</b> relationshipType is a valid relationship type defined
   * in the database <br/>
   * <b>Postcondition:</b> return value is not null
   * 
   * @param relationshipType
   *          name of the relationship
   * @return MdRelationshipIF that defines the relationship with the given type
   * @throws DataAccessException
   *           if the relationship type is not valid.
   */
  public static MdRelationshipDAOIF getMdRelationshipDAO(String relationshipType)
  {
    MdClassDAOIF mdClassIF = getMdClassDAO(relationshipType);

    if (! ( mdClassIF instanceof MdRelationshipDAOIF ))
    {
      String errmsg = "Type [" + relationshipType + "] is not an [" + MdRelationshipInfo.CLASS + "].";
      throw new UnexpectedTypeException(errmsg);
    }

    return (MdRelationshipDAOIF) mdClassIF;
  }

  /**
   * Returns the MdEnumerationIF that defines the enumeration with the given
   * type.
   * 
   * @param enumerationType
   * @return MdEnumerationIF that defines the enumeration with the given type.
   */
  public static MdEnumerationDAOIF getMdEnumerationDAO(String enumerationType)
  {
    MdEnumerationStrategy mdEnumerationStrategy = (MdEnumerationStrategy) strategyMap.get(MdEnumerationInfo.CLASS);
    return mdEnumerationStrategy.getMdEnumerationDAO(enumerationType);
  }

  /**
   * Returns all child Relationship objects for the BusinessDAO with the given
   * id that are of the given relationship type. Request is routed to the
   * collection responsible for relationships of the given relationship type.
   * 
   * <br/>
   * <b>Precondition:</b> businessDAOid != null <br/>
   * <b>Precondition:</b> !businessDAOid.trim().equals("") <br/>
   * <b>Precondition:</b> businessDAOid represents a valid BusinessDAO in the
   * database <br/>
   * <b>Precondition:</b> relationshipType != null <br/>
   * <b>Precondition:</b> !relationshipType.trim().equals("") <br/>
   * <b>Precondition:</b> relationshipType.trim represents a valid relationship
   * name <br/>
   * <b>Postcondition:</b> Returns all child Relationship objects for the
   * BusinessDAO with the given id that are of the given relationship type.
   * 
   * @param businessDAOid
   *          BusinessDAO id.
   * @param relationshipType
   *          name of the relationship type.
   * @return all child Relationship objects for the BusinessDAO with the given
   *         id that are of the given relationship type.
   * @throws Throwable
   */
  public static List<RelationshipDAOIF> getChildren(String businessDAOid, String relationshipType)
  {
    ( LockObject.getLockObject() ).checkTransactionTypeLock(relationshipType);
    RelationshipDAOCollection relationshipCollection = (RelationshipDAOCollection) getTypeCollection(relationshipType);

    return relationshipCollection.getChildren(businessDAOid, relationshipType);

  }

  /**
   * Returns all the child relationship objects of the BusinessDAO and with the
   * relationship type. The results are forced from the cache.
   * 
   * @param businessDAOid
   * @param relationshipType
   * @return
   */
  public static List<RelationshipDAOIF> getChildrenFromCache(String businessDAOid, String relationshipType)
  {
    ( LockObject.getLockObject() ).checkTransactionTypeLock(relationshipType);

    RelationshipDAOCollection relationshipCollection = (RelationshipDAOCollection) getTypeCollection(relationshipType);

    return relationshipCollection.getChildrenFromCache(businessDAOid, relationshipType);
  }

  /**
   * Returns all parent Relationship objects for the BusinessDAO with the given
   * id that are of the given type. Request is routed to the collection
   * responsible for relationships of the given type.
   * 
   * <br/>
   * <b>Precondition:</b> businessDAOid != null <br/>
   * <b>Precondition:</b> !businessDAOid.trim().equals("") <br/>
   * <b>Precondition:</b> relationshipType != null <br/>
   * <b>Precondition:</b> !relationshipType.trim().equals("") <br/>
   * <b>Precondition:</b> relationshipType represents a valid relationship type <br/>
   * <b>Postcondition:</b> Returns all parent Relationship objects for the
   * BusinessDAO with the given id that are of the given type.
   * 
   * @param businessDAOid
   *          BusinessDAO id
   * @param relationshipType
   *          name of the relationship
   * @return all parent Relationship objects for the BusinessDAO with the given
   *         id that are of the given type
   */
  public static List<RelationshipDAOIF> getParents(String businessDAOid, String relationshipType)
  {
    ( LockObject.getLockObject() ).checkTransactionTypeLock(relationshipType);

    RelationshipDAOCollection relationshipCollection = (RelationshipDAOCollection) getTypeCollection(relationshipType);

    return relationshipCollection.getParents(businessDAOid, relationshipType);
  }

  /**
   * Returns all parent Relationship objects for the BusinessDAO with the given
   * id that are of the given type. Request is routed to the collection
   * responsible for relationships of the given type.
   * 
   * <br/>
   * <b>Precondition:</b> businessDAOid != null <br/>
   * <b>Precondition:</b> !businessDAOid.trim().equals("") <br/>
   * <b>Precondition:</b> relationshipType != null <br/>
   * <b>Precondition:</b> !relationshipType.trim().equals("") <br/>
   * <b>Precondition:</b> relationshipType.trim() represents a valid
   * relationship type <br/>
   * <b>Postcondition:</b> Returns all parent Relationship objects for the
   * BusinessDAO with the given id that are of the given type.
   * 
   * @param businessDAOid
   *          BusinessDAO id.
   * @param relationshipType
   *          type of the relationship.
   * @return all parent Relationship objects for the BusinessDAO with the given
   *         id that are of the given type.
   */
  public static List<RelationshipDAOIF> getParentsFromCache(String businessDAOid, String relationshipType)
  {
    ( LockObject.getLockObject() ).checkTransactionTypeLock(relationshipType);

    RelationshipDAOCollection relationshipCollection = (RelationshipDAOCollection) getTypeCollection(relationshipType);

    return relationshipCollection.getParentsFromCache(businessDAOid, relationshipType);
  }
  
  public static boolean globalCacheContainsId(String entityId)
  {
    return globalCache.containsKey(entityId);
  }

  public static boolean contains(String type, String key)
  {
    ( LockObject.getLockObject() ).checkTransactionLock(type, key);

    // get the appropriate collection class for the entity type
    MdClassDAOIF mdClassIF = MdClassDAO.getMdClassDAO(type);

    if (! ( mdClassIF instanceof MdEntityDAOIF ))
    {
      String errMsg = "Object with key [" + key + "] is not defined by a [" + MdEntityInfo.CLASS + "]";
      throw new DataNotFoundException(errMsg, mdClassIF);
    }

    CacheStrategy entityDAOCollection = getTypeCollection(mdClassIF.definesType());

    // The collection class may or may not use caching. If caching is used,
    // a cached EntityDAO is returned
    return entityDAOCollection.contains(type, key);
  }
}
