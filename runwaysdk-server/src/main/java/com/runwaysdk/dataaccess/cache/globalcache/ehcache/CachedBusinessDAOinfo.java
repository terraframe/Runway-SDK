/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.dataaccess.cache.globalcache.ehcache;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.cache.ObjectCache;

public class CachedBusinessDAOinfo extends CachedEntityDAOinfo
{
  /**
   * 
   */
  private static final long serialVersionUID = 2692795654180092103L;

  /**
   * Stores parent relationships for this {@link EntityDAOIF}.
   * 
   * Key: relationship type, Value: {@link RelationshipMapList}
   */
  private Map<String, RelationshipMapList> parentRelationshipMap;

  /**
   * Stores child relationships for this {@link EntityDAOIF}.
   * 
   * Key: relationship type, Value: {@link RelationshipMapList}
   */
  private Map<String, RelationshipMapList> childRelationshipMap;
  
  public CachedBusinessDAOinfo()
  {
    super();
    this.parentRelationshipMap = new ConcurrentHashMap<String, RelationshipMapList>();
    this.childRelationshipMap = new ConcurrentHashMap<String, RelationshipMapList>();
  }
  
  /**
   * Returns true if no information about the {@link EntityDAOIF} is stored, false otherwise.
   * If no information on this object is stored, then it should be removed from the global cache.
   * 
   * @return true if no information about the {@link EntityDAOIF} is stored, false otherwise.
   * If no information on this object is stored, then it should be removed from the global cache.
   */
  public boolean removeFromGlobalCache()
  {
    if (super.removeFromGlobalCache() &&
        this.parentRelationshipMap.size() == 0 &&
        this.childRelationshipMap.size() == 0)
    {
      return true;
    }
    else
    {
      return false;
    }
  }  
  
  /**
   * Adds the parent relationship to this {@link BusinessDAOIF}.
   * <br/>
   * <b>Precondition:</b> assumes the child on the given relationship equals this {@link BusinessDAOIF}.
   * 
   * @param relationshipDAOIF
   */
  public void addParentRelationship(RelationshipDAOIF relationshipDAOIF)
  {
    RelationshipMapList relationshipMapList = this.parentRelationshipMap.get(relationshipDAOIF.getType());
    
    if (relationshipMapList == null)
    {
      relationshipMapList = new RelationshipMapList();
      this.parentRelationshipMap.put(relationshipDAOIF.getType(), relationshipMapList);
    }

    relationshipMapList.add(relationshipDAOIF);
  }
  
  /**
   * Updates the oid of the relationship of the parent relationship to this {@link BusinessDAOIF}.
   * <br/>
   * <b>Precondition:</b> assumes the child on the given relationship equals this {@link BusinessDAOIF}.
   * 
   * @param relationshipDAOIF
   */
  public void updateParentRelationship(RelationshipDAOIF relationshipDAOIF)
  {
    RelationshipMapList relationshipMapList = this.parentRelationshipMap.get(relationshipDAOIF.getType());
    
    if (relationshipMapList == null)
    {
      relationshipMapList = new RelationshipMapList();
      this.parentRelationshipMap.put(relationshipDAOIF.getType(), relationshipMapList);
    }

    relationshipMapList.replace(relationshipDAOIF);
  }
  
  /**
   * Removes the parent relationship from this {@link BusinessDAOIF}.
   * <br/>
   * <b>Precondition:</b> assumes the child on the given relationship equals this {@link BusinessDAOIF}.
   * 
   * @param relationshipDAOIF
   * 
   * @return True if the child object still has parent relationships of this type.
   */
  public boolean removeParentRelationship(RelationshipDAOIF relationshipDAOIF)
  {
    boolean stillHasParents = false;
    
    RelationshipMapList relationshipMapList = this.parentRelationshipMap.get(relationshipDAOIF.getType());
    
    if (relationshipMapList != null)
    {
      relationshipMapList.remove(relationshipDAOIF);
      
      if (relationshipMapList.size() == 0)
      {
        this.parentRelationshipMap.remove(relationshipDAOIF.getType());
      }
      else
      {
        stillHasParents = true; 
      }
    }
    
    return stillHasParents;
  }

  /**
   * Removes all parent relationships with this object of the given type.
   * 
   * @param relationshipType
   */
  public void removeAllParentRelationshipsOfType(String relationshipType)
  {
    this.parentRelationshipMap.remove(relationshipType);
  }
  
  /**
   * Returns the parent relationships for this {@link BusinessDAOIF}.
   *  
   * @param relationshipType
   * 
   * @return parent relationships for this {@link BusinessDAOIF}.
   */
  public List<RelationshipDAOIF> getParentRelationshipDAOlist(String relationshipType)
  {
    RelationshipMapList relationshipMapList = this.parentRelationshipMap.get(relationshipType);
    
    if (relationshipMapList != null)
    {
      return relationshipMapList.getRelationshipDAOlist();
    }
    else
    {
      return new LinkedList<RelationshipDAOIF>();
    }
  }
  
  /**
   * Adds the child relationship to this {@link BusinessDAOIF}.
   * <br/>
   * <b>Precondition:</b> assumes the parent on the given relationship equals this {@link BusinessDAOIF}.
   * 
   * @param relationshipDAOIF
   */
  public void addChildRelationship(RelationshipDAOIF relationshipDAOIF)
  {
    RelationshipMapList relationshipMapList = this.childRelationshipMap.get(relationshipDAOIF.getType());
    
    if (relationshipMapList == null)
    {
      relationshipMapList = new RelationshipMapList();
      this.childRelationshipMap.put(relationshipDAOIF.getType(), relationshipMapList);
    }

    relationshipMapList.add(relationshipDAOIF);
  }
  
  /**
   * Updates the oid of the the child relationship to this {@link BusinessDAOIF}.
   * <br/>
   * <b>Precondition:</b> assumes the parent on the given relationship equals this {@link BusinessDAOIF}.
   * 
   * @param relationshipDAOIF
   */
  public void updateChildRelationship(RelationshipDAOIF relationshipDAOIF)
  {
    RelationshipMapList relationshipMapList = this.childRelationshipMap.get(relationshipDAOIF.getType());
    
    if (relationshipMapList == null)
    {
      relationshipMapList = new RelationshipMapList();
      this.childRelationshipMap.put(relationshipDAOIF.getType(), relationshipMapList);
    }

    relationshipMapList.replace(relationshipDAOIF);
  }
  
  /**
   * Removes the child relationship from this {@link BusinessDAOIF}.
   * <br/>
   * <b>Precondition:</b> assumes the parent on the given relationship equals this {@link BusinessDAOIF}.
   * 
   * @param relationshipDAOIF
   * 
   * @return True if the child object still has child relationships of this type.
   */
  public boolean removeChildRelationship(RelationshipDAOIF relationshipDAOIF)
  {
    boolean stillHasChildren = false;
    
    RelationshipMapList relationshipMapList = this.childRelationshipMap.get(relationshipDAOIF.getType());
    
    if (relationshipMapList != null)
    {
      relationshipMapList.remove(relationshipDAOIF);
      
      if (relationshipMapList.size() == 0)
      {
        this.childRelationshipMap.remove(relationshipDAOIF.getType());
      }
      else
      {
        stillHasChildren = true;
      }
    }
    
    return stillHasChildren;
  }  

  /**
   * Removes all child relationships with this object of the given type.
   * 
   * @param relationshipType
   */
  public void removeAllChildRelationshipsOfType(String relationshipType)
  {
    this.childRelationshipMap.remove(relationshipType);
  }
  
  /**
   * Returns the child relationships for this {@link BusinessDAOIF}.
   *  
   * @param relationshipType
   * 
   * @return child relationships for this {@link BusinessDAOIF}.
   */
  public List<RelationshipDAOIF> getChildRelationshipDAOlist(String relationshipType)
  {
    RelationshipMapList relationshipMapList = this.childRelationshipMap.get(relationshipType);
    
    if (relationshipMapList != null)
    {
      return relationshipMapList.getRelationshipDAOlist();
    }
    else
    {
      return new LinkedList<RelationshipDAOIF>();
    }
  }
  
  /**
  *
  * @author nathan
  *
  * @param <T>
  */
 public static class RelationshipMapList implements Serializable
 {
   /**
    * 
    */
   private static final long serialVersionUID = -672781730361988469L;

   private Set<String> relationshipDAOidSet;

   /**
    *
    *
    */
   public RelationshipMapList()
   {
     this.relationshipDAOidSet = new HashSet<String>();
   }

   public void addAll(RelationshipMapList relationshipMapList)
   {
     this.relationshipDAOidSet.addAll(relationshipMapList.relationshipDAOidSet);
   }
  
   /**
    *
    * @param relationshipDAO
    */
   public void add(RelationshipDAOIF relationshipDAOIF)
   {
     this.relationshipDAOidSet.add(relationshipDAOIF.getOid());
   }

   /**
   *
   * @param relationshipDAO
   */
  public void replace(RelationshipDAOIF relationshipDAOIF)
  {
    this.relationshipDAOidSet.remove(((RelationshipDAO)relationshipDAOIF).getOldId());
    
    this.relationshipDAOidSet.add(relationshipDAOIF.getOid());
  }
   
   /**
    *
    * @param relationshipDAO
    */
   public void remove(RelationshipDAOIF relationshipDAOIF)
   {
     this.relationshipDAOidSet.remove(relationshipDAOIF.getOid());
   }

   /**
    *
    * @return
    */
   public int size()
   {
     return this.relationshipDAOidSet.size();
   }

   /**
   * Returns reference of set of ids of relationships.
   * @return
   */
  public Set<String> getRelationshipDAOset()
  {
    return this.relationshipDAOidSet;
  }
   
   /**
    *
    * @return
    */
   public List<RelationshipDAOIF> getRelationshipDAOlist()
   {
     // This is not the most cohesive place to place this, but this is an attempt
     // to save a little bit of performance by only iterating over the set once.  Otherwise
     // This method should return the set an a client should fetch the relationship objects.
     List<RelationshipDAOIF> relationshipList = new LinkedList<RelationshipDAOIF>();
     Iterator<String> i = relationshipDAOidSet.iterator();
     while (i.hasNext())
     {
       String oid = i.next();
       RelationshipDAOIF relationshipDAOIF = (RelationshipDAOIF)ObjectCache.getEntityDAOIFfromCache(oid);
       relationshipList.add(relationshipDAOIF);       
     }
     return relationshipList;
   }
 }
}
