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

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.database.RelationshipDAOFactory;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;


public class CacheAllRelationshipDAOStrategy extends CacheAllStrategy implements RelationshipDAOCollection
{
  /**
   * 
   */
  private static final long serialVersionUID = 1683894186404083128L;

  /**
   * Set of ids of all {@link BusinessDAOIF} that have parent relationships  with the given type.  Each key
   * <br/><b>invariant</b> parentRelSet != null
   */
  protected Set<String> parentRelSet;
  
  /**
   * Set of ids of all {@link BusinessDAOIF} that have child relationships  with the given type.  Each key
   * <br/><b>invariant</b> childRelSet != null
   */
  protected Set<String> childRelSet;

  /**
   * Copies the content from the given collection into this one.
   */
  public void addAll(RelationshipDAOCollection cacheStrategy)
  {
    this.addAll((CacheStrategy)cacheStrategy);
  }

  /**
   * Copies the content from the given collection into this one.
   */
  @Override
  public void addAll(CacheStrategy cacheStrategy)
  {
    super.addAll(cacheStrategy);
    
    CacheAllRelationshipDAOStrategy cacheAllRelCollection = (CacheAllRelationshipDAOStrategy)cacheStrategy;
    
    this.parentRelSet.addAll(cacheAllRelCollection.parentRelSet);
    this.childRelSet.addAll(cacheAllRelCollection.childRelSet);    
  }
  
  /**
   *
   * @param relationshipType
   */
  public CacheAllRelationshipDAOStrategy(String relationshipType)
  {
    super(relationshipType);

    this.parentRelSet = new HashSet<String>();
    this.childRelSet = new HashSet<String>();
  }

  /**
   *
   * @param mdEntityIF
   */
  public CacheAllRelationshipDAOStrategy(MdRelationshipDAOIF mdRelationshipIF)
  {
    this(mdRelationshipIF.definesType());
  }

  /**
   * Returns an array of Relationship objects of the given type
   * that are children of the BusinessDAO with given BusinessDAO id.
   *
   * <br/><b>Precondition:</b>  businessDAOid != null
   * <br/><b>Precondition:</b>  !businessDAOid.trim().equals("")
   * <br/><b>Precondition:</b>  relationshipType != null
   * <br/><b>Precondition:</b>  !relationshipType.trim().equals("")
   * <br/><b>Postcondition:</b> Returns LinkedList of Relationship objects of the given type
   *         that are children of the BusinessDAO with given BusinessDAO id
   *
   * @param businessDAOid  id of the BusinessDAO from which you want to retrieve the
   *                     children relationships
   * @return An array of Relationship objects of the given type
   *         that are children of the BusinessDAO with given BusinessDAO id from the cache.
   */
  public List<RelationshipDAOIF> getChildrenFromCache(String businessDAOid, String relationshipType)
  {
      return this.getChildren(businessDAOid, relationshipType);
  }

  /**
   * Returns an array of Relationship objects of the given type
   * that are children of the BusinessDAO with given BusinessDAO id.  Relationships
   * are retrieved from the cache.
   *
   * <br/><b>Precondition:</b>  businessDAOid != null
   * <br/><b>Precondition:</b>  !businessDAOid.trim().equals("")
   * <br/><b>Precondition:</b>  relationshipType != null
   * <br/><b>Precondition:</b>  !relationshipType.trim().equals("")
   * <br/><b>Postcondition:</b> Returns LinkedList of Relationship objects of the given type
   *         that are children of the BusinessDAO with given BusinessDAO id
   *
   * @param businessDAOid  id of the BusinessDAO from which you want to retrieve the
   *                     children relationships
   * @return Array of Relationship objects of the given type
   *         that are children of the BusinessDAO with given BusinessDAO id.
   */
  public List<RelationshipDAOIF> getChildren(String businessDAOid, String relationshipType)
  {
    return ObjectCache.getChildRelationshipsFromCache(businessDAOid, relationshipType);
  }
  
  /**
   * Returns true if this cache has children for the object with the given id, false otherwise.
   * 
   * @param businessDAOid
   * @return true if this cache has children for the object with the given id, false otherwise.
   */
  public boolean hasChildren(String businessDAOid)
  {
    // Reload the cache if it is marked to be reloaded
    if (this.reload == true)
    {
      this.reload();
    }
    
    synchronized(businessDAOid)
    {
      if (this.childRelSet.contains(businessDAOid))
      {
        return true;
      }
      else
      {
        return false;
      }
    }
  }
  
  /**
   *Returns an array of Relationship objects of the given type
   * that are parents of the BusinessDAO with given BusinessDAO ID. Relationships
   * are retrieved from the cache.
   *
   * <br/><b>Precondition:</b>  businessDAOid != null
   * <br/><b>Precondition:</b>  !businessDAOid.trim().equals("")
   * <br/><b>Precondition:</b>  relationshipType != null
   * <br/><b>Precondition:</b>  !relationshipType.trim().equals("")
   * <br/><b>Postcondition:</b> Returns LinkedList of Relationship objects of the given type
   *         that are parents of the BusinessDAO with given BusinessDAO ID
   *
   * @param businessDAOid  ID of the BusinessDAO from which you want to retrieve the
   *                     parents relationships
   * @return array of Relationship objects of the given type
   *         that are parents of the BusinessDAO with given BusinessDAO ID
   */
  public List<RelationshipDAOIF> getParents(String businessDAOid, String relationshipType)
  {   
    return ObjectCache.getParentRelationshipsFromCache(businessDAOid, relationshipType);
  }

  /**
   * Returns an array of Relationship objects of the given type
   * that are parents of the BusinessDAO with given BusinessDAO id. Relationships
   * are retrieved from the cache.
   *
   * <br/><b>Precondition:</b>  businessDAOid != null
   * <br/><b>Precondition:</b>  !businessDAOid.trim().equals("")
   * <br/><b>Precondition:</b>  relationshipType != null
   * <br/><b>Precondition:</b>  !relationshipType.trim().equals("")
   * <br/><b>Postcondition:</b> Returns LinkedList of Relationship objects of the given type
   *         that are parents of the BusinessDAO with given BusinessDAO id.
   *
   * @param businessDAOid  id of the BusinessDAO from which you want to retrieve the
   *                     parents relationships
   * @return array of Relationship objects of the given type
   *         that are parents of the BusinessDAO with given BusinessDAO id.
   */
  public List<RelationshipDAOIF> getParentsFromCache(String businessDAOid, String relationshipType)
  {
      return this.getParents(businessDAOid, relationshipType);
  }

  /**
   * Returns true if this cache has parents for the object with the given id, false otherwise.
   * 
   * @param businessDAOid
   * @return true if this cache has parents for the object with the given id, false otherwise.
   */
  public boolean hasParents(String businessDAOid)
  {
    // Reload the cache if it is marked to be reloaded
    if (this.reload == true)
    {
      this.reload();
    }
    
    synchronized(businessDAOid)
    {
      if (parentRelSet.contains(businessDAOid))
      {
        return true;
      }
      else
      {
        return false;
      }
    }
  }
  

  /**
   * Reloads the cache of this collection.  The cache is cleared.  All EntityDAOs of the
   *   class stored in this collection are instantiated and put in the cache.
   *
   * <br/><b>Precondition:</b>   true
   *
   * <br/><b>Postcondition:</b>  Cache contains all EntityDAOs of the class that are to be stored in this
   *        collection.
   *
   */
  public synchronized void reload()
  {
    this.removeCacheStrategy();
    
    super.reload();

    // Get a list of the ids for all EntityDAOs of this class or are child classes
    MdRelationshipDAOIF mdRelationshipIF = MdRelationshipDAO.getMdRelationshipDAO(this.entityType);

    // Instantiate EntityDAOs for each ID in the list
    RelationshipDAOFactory.getRelationshipTypeInstances(mdRelationshipIF, this);

    reload = false;
  }


  /**
   *Adds the given relationship to the cache.
   *
   * <br/><b>Precondition:</b>  entityDAO != null
   *
   * <br/><b>Postcondition:</b>  Updates the cache with the given relationship
   *
   * @param relationship Relationship to add to the cache
   */
  public void updateCache(EntityDAO entityDAO)
  {
    synchronized(entityDAO.getId())
    {
      RelationshipDAO relationshipDAO = (RelationshipDAO)entityDAO;

      if (relationshipDAO.hasParentIdChanged())
      {
        parentRelSet.remove(relationshipDAO.getOldParentId());
      }
      if (relationshipDAO.hasChildIdChanged())
      {
        childRelSet.add(relationshipDAO.getOldChildId());
      }
      
      String parentId  = relationshipDAO.getParentId();
      String childId   = relationshipDAO.getChildId();

      parentRelSet.add(childId);
      childRelSet.add(parentId);

      Boolean hasIdChanged = relationshipDAO.hasIdChanged();
      String oldId = relationshipDAO.getOldId();
      
      Boolean hasParentIdChanged = relationshipDAO.hasParentIdChanged();
      String oldParentId = relationshipDAO.getOldParentId();
      
      Boolean hasChildIdChanged = relationshipDAO.hasChildIdChanged();
      String oldChildId = relationshipDAO.getOldChildId();

      // Needs to be cleared for storage in the global cache
      relationshipDAO.clearOldRelIds();

      super.updateCache(relationshipDAO);
        
      if (hasIdChanged || hasParentIdChanged || hasChildIdChanged)
      {
        // Need to set the old id variable, so that the code below will work.
        if (hasIdChanged)
        {
          relationshipDAO.setOldId(oldId);
        }
        
        if (hasParentIdChanged)
        {
          relationshipDAO.setOldParentId(oldParentId);
        }

        if (hasChildIdChanged)
        {
          relationshipDAO.setOldChildId(oldChildId);          
        }
        
        ObjectCache.updateRelationshipDAOIFinCache(hasIdChanged, relationshipDAO);
        
        relationshipDAO.clearOldId();
        relationshipDAO.clearOldRelIds();
      }
      else
      {
        ObjectCache.addRelationshipDAOIFtoCache(relationshipDAO);
      }   

    }
  }
  
  /**
   * This method makes me feel cheap and dirty.  This is used within the transaction cache
   * to record relationships.  This class does not actually store the relationships in this context,
   * rather just records relationship activity.  If relationship activity was performed 
   * on a {@link BusinessDAOIF} for relationships of this classes' type, then relationships are
   * read from the database.
   * 
   * @param relationship
   */
  public void updateCacheInTransactionCache(RelationshipDAO relationship)
  {
    synchronized(relationship.getId())
    {
      String parentId  = relationship.getParentId();
      String childId   = relationship.getChildId();
    
      this.parentRelSet.add(childId);
      this.childRelSet.add(parentId);
    }
  }

  /**
   *Remove the given relationship from the cache.
   *
   * <br/><b>Precondition:</b>  relationship != null
   *
   * <br/><b>Postcondition:</b>  Removes the cache with the given relationship
   *
   * @param relationship Relationship to remove from the cache
   */
  public void removeCache(EntityDAO entityDAO)
  {
    synchronized(entityDAO.getId())
    {   
      RelationshipDAO relationship = (RelationshipDAO)entityDAO;
     
      String parentId  = relationship.getParentId();
      String childId   = relationship.getChildId();
      
      boolean stillHasParents = ObjectCache.removeParentRelationshipDAOIFtoCache(relationship, true);
      if (stillHasParents)
      {
        parentRelSet.remove(childId);
      }

      boolean stillHasChildren = ObjectCache.removeChildRelationshipDAOIFtoCache(relationship, true);
      if (stillHasChildren)
      {
        childRelSet.remove(parentId);
      }
      
      super.removeCache(entityDAO);
    }
  }

//  Heads up: test
//  public void removeCache(EntityDAO entityDAO)
//  {
//    synchronized(entityDAO.getId())
//    {   
//      RelationshipDAO relationship = (RelationshipDAO)entityDAO;
//
//      String parentId  = relationship.getParentId();
//      String childId   = relationship.getChildId();
//
//      boolean stillHasParents = ObjectCache.removeParentRelationshipDAOIFtoCache(relationship, true);
//      if (stillHasParents)
//      {
//        parentRelSet.remove(childId);
//      }
//
//      boolean stillHasChildren = ObjectCache.removeChildRelationshipDAOIFtoCache(relationship, true);
//      if (stillHasChildren)
//      {
//        childRelSet.remove(parentId);
//      }
//
//      super.removeCache(entityDAO);
//    }
//  }
  

  /**
   *
   * @param id
   * @return
   */
  protected EntityDAOIF getFromFactory(String id)
  {
    return RelationshipDAOFactory.get(id);
  }

  /**
   *
   * @param type
   * @param key
   * @return
   */
  protected EntityDAOIF getFromFactory(String type, String key)
  {
    return RelationshipDAOFactory.get(type, key);
  }

  /**
   * Performs any cleanup on the global cache when this cache is removed.
   */
  @Override
  protected synchronized void removeCacheStrategy()
  {
    super.removeCacheStrategy();
    
    String relationshipType = this.getEntityType();
    
    Iterator<String> i = parentRelSet.iterator();
    while(i.hasNext())
    {
      String childId = i.next();
      ObjectCache.removeAllParentRelationshipsOfType(childId, relationshipType, false);
    }
    
    i = childRelSet.iterator();
    while(i.hasNext())
    {
      String parentId = i.next();
      ObjectCache.removeAllChildRelationshipsOfType(parentId, relationshipType, false);
    }
  }
}
