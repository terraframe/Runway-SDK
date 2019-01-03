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

import java.util.List;

import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.transaction.TransactionItem;


public interface RelationshipDAOCollection extends TransactionItem
{

  /**
   *Returns an array of Relationship objects of the given type
   * that are children of the BusinessDAO with given BusinessDAO OID.
   * 
   * <br/><b>Precondition:</b>  businessDAOid != null
   * <br/><b>Precondition:</b>  !businessDAOid.trim().equals("")
   * <br/><b>Precondition:</b>  relationshipType != null
   * <br/><b>Precondition:</b>  !relationshipType.trim().equals("")
   * <br/><b>Postcondition:</b> Returns LinkedList of Relationship objects of the given type
   *         that are children of the BusinessDAO with given BusinessDAO oid
   * 
   * @param businessDAOid oid of the BusinessDAO from which you want to retrieve the 
   *                     children relationships
   * @return An array of Relationship objects of the given type
   *         that are children of the BusinessDAO with given BusinessDAO oid
   */
  public List<RelationshipDAOIF> getChildren(String businessDAOid, String relationshipType); 
  
  /**
   * Returns an array of Relationship objects of the given type
   * that are children of the BusinessDAO with given BusinessDAO oid.
   * 
   * <br/><b>Precondition:</b>  businessDAOid != null
   * <br/><b>Precondition:</b>  !businessDAOid.trim().equals("")
   * <br/><b>Precondition:</b>  relationshipType != null
   * <br/><b>Precondition:</b>  !relationshipType.trim().equals("")
   * <br/><b>Postcondition:</b> Returns LinkedList of Relationship objects of the given type
   *         that are children of the BusinessDAO with given BusinessDAO oid
   * 
   * @param businessDAOid  oid of the BusinessDAO from which you want to retrieve the 
   *                     children relationships
   * @return An array of Relationship objects of the given type
   *         that are children of the BusinessDAO with given BusinessDAO oid from the cache.
   */
  public List<RelationshipDAOIF> getChildrenFromCache(String businessDAOid, String relationshipType); 
  
  /**
   * Returns an array of Relationship objects of the given type
   * that are parents of the BusinessDAO with given BusinessDAO oid.
   * 
   * <br/><b>Precondition:</b>  businessDAOid != null
   * <br/><b>Precondition:</b>  !businessDAOid.trim().equals("")
   * <br/><b>Precondition:</b>  relationshipType != null
   * <br/><b>Precondition:</b>  !relationshipType.trim().equals("")
   * <br/><b>Postcondition:</b> Returns LinkedList of Relationship objects of the given type
   *         that are parents of the BusinessDAO with given BusinessDAO OID
   * 
   * @param businessDAOid  Id of the BusinessDAO from which you want to retrieve the 
   *                     parents relationships
   * @return Array of Relationship objects of the given type
   *         that are parents of the BusinessDAO with given BusinessDAO oid.
   */
  public List<RelationshipDAOIF> getParents(String businessDAOid, String relationshipType);
  
  /**
   * Returns an array of Relationship objects of the given type
   * that are parents of the BusinessDAO with given BusinessDAO oid.
   * 
   * <br/><b>Precondition:</b>  businessDAOid != null
   * <br/><b>Precondition:</b>  !businessDAOid.trim().equals("")
   * <br/><b>Precondition:</b>  relationshipType != null
   * <br/><b>Precondition:</b>  !relationshipType.trim().equals("")
   * <br/><b>Postcondition:</b> Returns LinkedList of Relationship objects of the given type
   *         that are parents of the BusinessDAO with given BusinessDAO OID
   * 
   * @param businessDAOid  oid of the BusinessDAO from which you want to retrieve the 
   *                     parents relationships
   * @return Array of Relationship objects of the given type
   *         that are parents of the BusinessDAO with given BusinessDAO oid from
   *         the cache.
   */
  public List<RelationshipDAOIF> getParentsFromCache(String businessDAOid, String relationshipType);
  
  
  /**
   * Marks the cache to be refreshed.
   * 
   * <br/><b>Precondition:</b>  true
   * <br/><b>Postcondition:</b> this.entityDAOMap.clear()
   */ 
  public void reload();
  
  /**
   * Places the given EntityDAO into the cache.
   * 
   * <br/><b>Precondition:</b>  EntityDAO != null 
   * 
   * <br/><b>Postcondition:</b> cache contains the given EntityDAO
   * 
   * @param  entityDAO to add to this collection
   */  
  public void updateCache(EntityDAO entityDAO);

  /**
   * Copies the content from the given collection into this one.
   */
  public void addAll(RelationshipDAOCollection cacheStrategy);
}
