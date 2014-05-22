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
package com.runwaysdk.dataaccess.transaction;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.cache.globalcache.ehcache.CachedBusinessDAOinfo.RelationshipMapList;

public class TransactionBusinessDAORelationships
{
  /**
   * Stores added parent relationships for this {@link EntityDAOIF}.
   * 
   * Key: relationship type, Value: {@link RelationshipMapList}
   */
  private Map<String, RelationshipMapList> addedParentRelationshipMap;

  /**
   * Stores deleted parent relationships for this {@link EntityDAOIF}.
   * 
   * Key: relationship type, Value: {@link RelationshipMapList}
   */
  private Map<String, RelationshipMapList> deletedParentRelationshipMap;
  
  /**
   * Stores added child relationships for this {@link EntityDAOIF}.
   * 
   * Key: relationship type, Value: {@link RelationshipMapList}
   */
  private Map<String, RelationshipMapList> addedChildRelationshipMap;
  
  /**
   * Stores deleted child relationships for this {@link EntityDAOIF}.
   * 
   * Key: relationship type, Value: {@link RelationshipMapList}
   */
  private Map<String, RelationshipMapList> deletedChildRelationshipMap;
  
  protected TransactionBusinessDAORelationships()
  {   
    this.addedParentRelationshipMap = new ConcurrentHashMap<String, RelationshipMapList>();
    this.deletedParentRelationshipMap = new ConcurrentHashMap<String, RelationshipMapList>();
    
    this.addedChildRelationshipMap = new ConcurrentHashMap<String, RelationshipMapList>();
    this.deletedChildRelationshipMap = new ConcurrentHashMap<String, RelationshipMapList>();
  }

  /**
   * Copies the contents from the given transaction relationship object.
   * 
   * @param transactionBusinessDAORelationships
   */
  protected void addAllRelationships(TransactionBusinessDAORelationships transactionBusinessDAORelationships)
  {
    Map<String, RelationshipMapList> _addedParentRelationshipMap = transactionBusinessDAORelationships.addedParentRelationshipMap;
    for (String relType : _addedParentRelationshipMap.keySet())
    {
      RelationshipMapList _relationshipMapList = _addedParentRelationshipMap.get(relType);

      if (!this.addedParentRelationshipMap.containsKey(relType))
      {
        this.addedParentRelationshipMap.put(relType, _relationshipMapList);
      }
      else
      {
        this.addedParentRelationshipMap.get(relType).addAll(_relationshipMapList);
      }
    }
    
    Map<String, RelationshipMapList> _deletedParentRelationshipMap = transactionBusinessDAORelationships.deletedParentRelationshipMap;
    for (String relType : _deletedParentRelationshipMap.keySet())
    {
      RelationshipMapList _relationshipMapList = _deletedParentRelationshipMap.get(relType);

      if (!this.deletedParentRelationshipMap.containsKey(relType))
      {
        this.deletedParentRelationshipMap.put(relType, _relationshipMapList);
      }
      else
      {
        this.deletedParentRelationshipMap.get(relType).addAll(_relationshipMapList);
      }
    }

    Map<String, RelationshipMapList> _addedChildRelationshipMap = transactionBusinessDAORelationships.addedChildRelationshipMap;
    for (String relType : _addedChildRelationshipMap.keySet())
    {
      RelationshipMapList _relationshipMapList = _addedChildRelationshipMap.get(relType);

      if (!this.addedChildRelationshipMap.containsKey(relType))
      {
        this.addedChildRelationshipMap.put(relType, _relationshipMapList);
      }
      else
      {
        this.addedChildRelationshipMap.get(relType).addAll(_relationshipMapList);
      }
    }
   
    Map<String, RelationshipMapList> _deletedChildRelationshipMap = transactionBusinessDAORelationships.deletedChildRelationshipMap;
    for (String relType : _deletedChildRelationshipMap.keySet())
    {
      RelationshipMapList _relationshipMapList = _deletedChildRelationshipMap.get(relType);

      if (!this.deletedChildRelationshipMap.containsKey(relType))
      {
        this.deletedChildRelationshipMap.put(relType, _relationshipMapList);
      }
      else
      {
        this.deletedChildRelationshipMap.get(relType).addAll(_relationshipMapList);
      }
    }
    
  }
  
  /**
   * Adds the parent relationship to this {@link BusinessDAOIF}.
   * <br/>
   * <b>Precondition:</b> assumes the child on the given relationship equals this {@link BusinessDAOIF}.
   * 
   * @param relationshipDAOIF
   */
  protected void addParentRelationship(RelationshipDAOIF relationshipDAOIF)
  {
    RelationshipMapList addedRelationshipMapList = this.addedParentRelationshipMap.get(relationshipDAOIF.getType());
    
    if (addedRelationshipMapList == null)
    {
      addedRelationshipMapList = new RelationshipMapList();
      this.addedParentRelationshipMap.put(relationshipDAOIF.getType(), addedRelationshipMapList);
    }

    addedRelationshipMapList.add(relationshipDAOIF);
    
    // Heads up: test
    // If a new relationship with the same id has been added, make sure we delete any reference to a previous relationship 
    // that was deleted with the same id
    RelationshipMapList deletedRelationshipMapList = this.deletedParentRelationshipMap.get(relationshipDAOIF.getType());
    
    if (deletedRelationshipMapList != null)
    {
      deletedRelationshipMapList.remove(relationshipDAOIF);
    }
  }
  
  /**
   * Updates the id of the relationship object of the parent relationship to this {@link BusinessDAOIF}.
   * <br/>
   * <b>Precondition:</b> assumes the child on the given relationship equals this {@link BusinessDAOIF}.
   * 
   * @param relationshipDAOIF
   */
  protected void updateParentRelationship(RelationshipDAOIF relationshipDAOIF)
  {
    RelationshipMapList addedRelationshipMapList = this.addedParentRelationshipMap.get(relationshipDAOIF.getType());
    
    if (addedRelationshipMapList == null)
    {
      addedRelationshipMapList = new RelationshipMapList();
      this.addedParentRelationshipMap.put(relationshipDAOIF.getType(), addedRelationshipMapList);
    }

    addedRelationshipMapList.replace(relationshipDAOIF);
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
  protected void deletedParentRelationship(RelationshipDAOIF relationshipDAOIF)
  {
    RelationshipMapList relationshipMapList = this.deletedParentRelationshipMap.get(relationshipDAOIF.getType());
    
    if (relationshipMapList == null)
    {
      relationshipMapList = new RelationshipMapList();
      this.deletedParentRelationshipMap.put(relationshipDAOIF.getType(), relationshipMapList);
    }

    relationshipMapList.add(relationshipDAOIF);
  }

  /**
   * Adds the child relationship to this {@link BusinessDAOIF}.
   * <br/>
   * <b>Precondition:</b> assumes the parent on the given relationship equals this {@link BusinessDAOIF}.
   * 
   * @param relationshipDAOIF
   */
  protected void addChildRelationship(RelationshipDAOIF relationshipDAOIF)
  {
    RelationshipMapList addedRelationshipMapList = this.addedChildRelationshipMap.get(relationshipDAOIF.getType());
    
    if (addedRelationshipMapList == null)
    {
      addedRelationshipMapList = new RelationshipMapList();
      this.addedChildRelationshipMap.put(relationshipDAOIF.getType(), addedRelationshipMapList);
    }

    addedRelationshipMapList.add(relationshipDAOIF);
    
    // Heads up: test
    // If a new relationship with the same id has been added, make sure we delete any reference to a previous relationship 
    // that was deleted with the same id
    RelationshipMapList deletedRelationshipMapList = this.deletedChildRelationshipMap.get(relationshipDAOIF.getType());
    
    if (deletedRelationshipMapList != null)
    {
      deletedRelationshipMapList.remove(relationshipDAOIF);
    }

  }
  
  /**
   * Updates the id relationship object of the child relationship to this {@link BusinessDAOIF}.
   * <br/>
   * <b>Precondition:</b> assumes the parent on the given relationship equals this {@link BusinessDAOIF}.
   * 
   * @param relationshipDAOIF
   */
  protected void updateChildRelationship(RelationshipDAOIF relationshipDAOIF)
  {
    RelationshipMapList addedRelationshipMapList = this.addedChildRelationshipMap.get(relationshipDAOIF.getType());
    
    if (addedRelationshipMapList == null)
    {
      addedRelationshipMapList = new RelationshipMapList();
      this.addedChildRelationshipMap.put(relationshipDAOIF.getType(), addedRelationshipMapList);
    }

    addedRelationshipMapList.replace(relationshipDAOIF);
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
  protected void deletedChildRelationship(RelationshipDAOIF relationshipDAOIF)
  {
    RelationshipMapList relationshipMapList = this.deletedChildRelationshipMap.get(relationshipDAOIF.getType());
    
    if (relationshipMapList == null)
    {
      relationshipMapList = new RelationshipMapList();
      this.deletedChildRelationshipMap.put(relationshipDAOIF.getType(), relationshipMapList);
    }

    relationshipMapList.add(relationshipDAOIF);
  }
  
  /**
   * Returns the added parent relationships for this {@link BusinessDAOIF}.
   *  
   * @param relationshipType
   * 
   * @return parent relationships for this {@link BusinessDAOIF}.
   */
  protected Set<String> getAddedParentRelationshipDAOset(String relationshipType)
  {
    RelationshipMapList relationshipMapList = this.addedParentRelationshipMap.get(relationshipType);
    
    if (relationshipMapList != null)
    {
      return relationshipMapList.getRelationshipDAOset();
    }
    else
    {
      return new HashSet<String>();
    }
  }
  
  /**
   * Returns true if added parent relationships for this {@link BusinessDAOIF}, false otherwise.
   *  
   * @param relationshipType
   * 
   * @return true if added parent relationships for this {@link BusinessDAOIF}, false otherwise.
   */
  protected boolean hasAddedParentRelationshipDAOset(String relationshipType)
  {
    RelationshipMapList relationshipMapList = this.addedParentRelationshipMap.get(relationshipType);
    
    if (relationshipMapList != null && relationshipMapList.getRelationshipDAOset().size() > 0)
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  
  /**
   * Returns the deleted parent relationships for this {@link BusinessDAOIF}.
   *  
   * @param relationshipType
   * 
   * @return deleted parent relationships for this {@link BusinessDAOIF}.
   */
  protected Set<String> getDeletedParentRelationshipDAOset(String relationshipType)
  {
    RelationshipMapList relationshipMapList = this.deletedParentRelationshipMap.get(relationshipType);
    
    if (relationshipMapList != null)
    {
      return relationshipMapList.getRelationshipDAOset();
    }
    else
    {
      return new HashSet<String>();
    }
  }

  /**
   * Returns true if deleted parent relationships for this {@link BusinessDAOIF}, false otherwise.
   *  
   * @param relationshipType
   * 
   * @return true if deleted parent relationships for this {@link BusinessDAOIF}, false otherwise.
   */
  protected boolean hasDeletedParentRelationshipDAOset(String relationshipType)
  {
    RelationshipMapList relationshipMapList = this.deletedParentRelationshipMap.get(relationshipType);
    
    if (relationshipMapList != null && relationshipMapList.getRelationshipDAOset().size() > 0)
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  
  /**
   * Returns the added child relationships for this {@link BusinessDAOIF}.
   *  
   * @param relationshipType
   * 
   * @return added child relationships for this {@link BusinessDAOIF}.
   */
  protected Set<String> getAddedChildRelationshipDAOset(String relationshipType)
  {
    RelationshipMapList relationshipMapList = this.addedChildRelationshipMap.get(relationshipType);
    
    if (relationshipMapList != null)
    {
      return relationshipMapList.getRelationshipDAOset();
    }
    else
    {
      return new HashSet<String>();
    }
  }
  
  /**
   * Returns true if added child relationships for this {@link BusinessDAOIF}, false otherwise.
   *  
   * @param relationshipType
   * 
   * @return true if added child relationships for this {@link BusinessDAOIF}, false otherwise.
   */
  protected boolean hasAddedChildRelationshipDAOset(String relationshipType)
  {
    RelationshipMapList relationshipMapList = this.addedChildRelationshipMap.get(relationshipType);
    
    if (relationshipMapList != null && relationshipMapList.getRelationshipDAOset().size() > 0)
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  
  /**
   * Returns the deleted child relationships for this {@link BusinessDAOIF}.
   *  
   * @param relationshipType
   * 
   * @return deleted child relationships for this {@link BusinessDAOIF}.
   */
  protected Set<String> getDeletedChildRelationshipDAOset(String relationshipType)
  {
    RelationshipMapList relationshipMapList = this.deletedChildRelationshipMap.get(relationshipType);
    
    if (relationshipMapList != null)
    {
      return relationshipMapList.getRelationshipDAOset();
    }
    else
    {
      return new HashSet<String>();
    }
  }

  /**
   * Returns true if deleted child relationships for this {@link BusinessDAOIF}, false otherwise.
   *  
   * @param relationshipType
   * 
   * @return true if deleted child relationships for this {@link BusinessDAOIF}, false otherwise.
   */
  protected boolean hasDeletedChildRelationshipDAOset(String relationshipType)
  {
    RelationshipMapList relationshipMapList = this.deletedChildRelationshipMap.get(relationshipType);
    
    if (relationshipMapList != null && relationshipMapList.getRelationshipDAOset().size() > 0)
    {
      return true;
    }
    else
    {
      return false;
    }
  }  
}
