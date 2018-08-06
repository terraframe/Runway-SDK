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

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.database.RelationshipDAOFactory;


public class CacheNoneRelationshipDAOStrategy extends CacheNoneStrategy implements RelationshipDAOCollection
{
  /**
   * 
   */
  private static final long serialVersionUID = 3867294558941427697L;

  /**
   *
   *
   */
  public CacheNoneRelationshipDAOStrategy(String relationshipType)
  {
    super(relationshipType);
  }

  /**
   * Copies the content from the given collection into this one.
   */
  public void addAll(RelationshipDAOCollection cacheStrategy)
  {
    super.addAll((CacheStrategy)cacheStrategy);
  }
  
  /**
   * Returns an empty array of Relationship since this class represents no caching.
   *
   * <br/><b>Precondition:</b>  relationshipId != null
   * <br/><b>Precondition:</b>  !relationshipId.trim().equals("")
   * <br/><b>Precondition:</b>  relationshipType != null
   * <br/><b>Precondition:</b>  !relationshipType.trim().equals("")
   * <br/><b>Postcondition:</b> Returns LinkedList of Relationship objects of the given type
   *         that are children of the BusinessDAO with given BusinessDAO ID
   *
   * @param relationshipId  ID of the BusinessDAO from which you want to retrieve the
   *                     children relationships
   * @return Array of Relationship objects of the given
   *         that are children of the BusinessDAO with given BusinessDAO ID
   */
  public List<RelationshipDAOIF> getChildrenFromCache(String relationshipId, String relationshipType)
  {
    return new LinkedList<RelationshipDAOIF>();
  }

  /**
   * Returns an array of Relationship objects of the given type
   * that are children of the BusinessDAO with given BusinessDAO oid.  Relationships
   * are retrieved from the database
   *
   * <br/><b>Precondition:</b>  businessDAOid != null
   * <br/><b>Precondition:</b>  !businessDAOid.trim().equals("")

   * <br/><b>Precondition:</b>  relationshipType != null
   * <br/><b>Precondition:</b>  !relationshipType.trim().equals("")
   * <br/><b>Postcondition:</b> Returns LinkedList of Relationship objects of the given type
   *         that are children of the BusinessDAO with given BusinessDAO ID
   *
   * @param businessDAOid  oid of the BusinessDAO from which you want to retrieve the
   *                     children relationships
   * @return Array of Relationship objects of the given type
   *         that are children of the BusinessDAO with given BusinessDAO oid.
   */
  public List<RelationshipDAOIF> getChildren(String businessDAOid, String relationshipType)
  {
    return RelationshipDAOFactory.getChildren(businessDAOid, relationshipType);
  }

  /**
   *Returns an array of Relationship objects of the given type
   * that are parents of the BusinessDAO with given BusinessDAO ID. Relationships
   * are retrieved from the database.
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
   * @return Array of Relationship objects of the given type
   *         that are parents of the BusinessDAO with given BusinessDAO ID
   */
  public List<RelationshipDAOIF> getParents(String businessDAOid, String relationshipType)
  {
    return RelationshipDAOFactory.getParents(businessDAOid, relationshipType);
  }


  /**
   *Returns an array of Relationship objects of the given type
   * that are parents of the BusinessDAO with given BusinessDAO ID. Relationships
   * are retrieved from the database.
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
   * @param relationshipType type of the relationship
   * @return Array of Relationship objects of the given type
   *         that are parents of the BusinessDAO with given BusinessDAO ID
   */
  public List<RelationshipDAOIF> getParentsFromCache(String businessDAOid, String relationshipType)
  {
    return new LinkedList<RelationshipDAOIF>();
  }

  /**
   *
   * @param oid
   * @return
   */
  protected EntityDAOIF getFromFactory(String oid)
  {
    return RelationshipDAOFactory.get(oid);
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
}
