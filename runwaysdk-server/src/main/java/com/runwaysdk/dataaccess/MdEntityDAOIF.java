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
package com.runwaysdk.dataaccess;

import java.util.List;
import java.util.Map;

import com.runwaysdk.dataaccess.metadata.MdEntityDAO;
import com.runwaysdk.query.EntityQuery;

public interface MdEntityDAOIF extends MdTableClassIF
{
  /**
   * Name of the table used to store instances of this class.
   */
  public static final String TABLE   = "md_entity";
  /**
   * Column name of the attribute that stores the name of the the database table used
   * to store instances of the class defined by this metadata object.
   */
  public static final String TABLE_NAME_COLUMN        = "table_name";

  /**
   * Column name of the attribute that stores the query .java clob
   */
  public static final String QUERY_SOURCE_COLUMN      = "query_source";

  /**
   * Column name of the attribute that stores the query .class blob
   */
  public static final String QUERY_CLASS_COLUMN       = "query_class";

  /**
   * Column name of the attribute that stores the query DTO .java clob
   */
  public static final String QUERY_DTO_SOURCE_COLUMN  = "query_dto_source";

  /**
   * Column name of the attribute that stores the query DTO .class blob
   */
  public static final String QUERY_DTO_CLASS_COLUMN   = "query_dto_class";

  /**
   * Returns the name of the table used to store instances of the class that this object defines.
   * @return name of the table used to store instances of the class that this object defines.
   */
  public String getTableName();

  /**
   * Returns the MdAttribute that defines the given attribute for the this entity.  This method
   * only works if the attribute is explicitly defined by the this.  In other words, it
   * will return null if the attribute exits for the given entity, but is inherited from a
   * super entity.
   *
   * <br/><b>Precondition:</b>   attributeName != null
   * <br/><b>Precondition:</b>   !attributeName.trim().equals("")
   */
  public MdAttributeConcreteDAOIF definesAttribute(String attributeName);

  /**
   * Returns a sorted list of <code>MdAttributeConcreteDAOIF</code> objects that this <code>MdEntityDAOIF</code> defines.
   *
   * @return an List of <code>MdAttributeConcreteDAOIF</code> objects that this <code>MdEntityDAOIF</code> defines.
   */
  public List<? extends MdAttributeConcreteDAOIF> definesAttributes();

  /**
   * Returns a sorted list of <code>MdAttributeConcreteDAOIF</code> objects that this <code>MdEntityDAOIF</code> defines.
   * The list is sorted by the alphabetical order of the attribute names
   *
   * @return an List of <code>MdAttributeConcreteDAOIF</code> objects that this <code>MdEntityDAOIF</code> defines.
   */
  public List<? extends MdAttributeConcreteDAOIF> definesAttributesOrdered();

  /**
   * Returns a map of MdAttributeIF objects defined by this entity.
   * Key: attribute name in lower case Value: MdAttributeIF
   * @return map of MdAttributeIF objects defined by this entity.
   */
  public Map<String, ? extends MdAttributeConcreteDAOIF> getDefinedMdAttributeMap();

  /**
   * Returns a map of MdAttributeIF objects defined by this entity type plus all
   * attributes defined by parent entities.
   * <p/>
   * <br/>Map Key: mdAttributeID
   * <br/>Map Value: MdAttributeIF
   * <p/>
   * @return map of MdAttributeIF objects defined by this entity type plus all
   * attributes defined by parent entities.
   */
  public Map<String, ? extends MdAttributeConcreteDAOIF> getAllDefinedMdAttributeIDMap();

  /**
   * Returns the DataAccessIF object that specifies the cache algorithm used by this type.
   * @return DataAccessIF object that specifies the cache algorithm used by this type.
   */
  public BusinessDAOIF getCacheAlgorithm();
  
  /**
   * Returns true if objects defined by this type are not cached in the global cache, 
   * false otherwise.
   * 
   * @return true if objects defined by this type are not cached in the global cache, 
   * false otherwise.
   */
  public boolean isNotCached();

  /**
   * Returns the integer value of cachSize, which is the optional
   * independent size of type for most recently used.
   * @return the integer value of cacheSize
   */
  public int getCacheSize();

  /**
   * Returns all indexes on this type.
   * @return all indexes on this type.
   */
  public List<MdIndexDAOIF> getIndexes();

  /**
   * Returns all unique indexes on this type.
   * @return all unique indexes on this type.
   */
  public List<MdIndexDAOIF> getUniqueIndexes();

  /**
   * Returns an array of MdEntityIF that defines immediate subentities of this entity.
   * @return an array of MdEntityIF that defines immediate subentities of this entity.
   */
  public List<? extends MdEntityDAOIF> getSubClasses();

  /**
   *Returns a list of MdEntityIF objects that represent entites
   * that are subclasses of the given entity, including all recursive entities (grandchildren).
   *
   * @return list of MdEntityIF objects that represent entites
   * that are subclasses of the given entity, including all recursive entities.
   */
  public List<? extends MdEntityDAOIF> getAllSubClasses();

  /**
   * Returns an MdEntityIF representing the super entity of this entity, or null if
   * it does not have one.
   *
   * @return an MdEntityIF representing the super entity of this entity, or null if
   * it does not have one.
   */
  public MdEntityDAOIF getSuperClass();

  /**
   *Returns a list of MdEntityIF objects that are subclasses of the given entity, including all recursive
   * entities. Only non abstract entities are returned (i.e. entities that can be instantiated)
   *
   * @return list of MdEntityIF objects
   * that are subclasses of the given entity.  Only non abstract entities
   * are returned (i.e. entities that can be instantiated)
   */
  public List<? extends MdEntityDAOIF> getAllConcreteSubClasses();

  /**
   * Returns a list of MdEntityIF instances representing every
   * parent of this MdEntity partaking in an inheritance relationship.
   *
   * @return a list of MdEntityIF instances that are parents of this class.
   */
  public List<? extends MdEntityDAOIF> getSuperClasses();

  /**
   *Returns the MdEntity that is the root of the hierarchy that this type belongs to.
   * returns a reference to inself if it is the root.
   *
   * @return MdEntity that is the root of the hierarchy that this type belongs to.
   * returns a reference to inself if it is the root.
   */
  public MdEntityDAOIF getRootMdClassDAO();

  /**
   * Returns a EntityQuery object for instances of the given type.
   * @return EntityQuery object for instances of the given type.
   */
  public EntityQuery getEntityQuery();

  /*
   * (non-Javadoc)
   *
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdEntityDAO getBusinessDAO();

  /**
   * @return A flag denoting if entities of the MdEntity should enforce the site master
   */
  public boolean getEnforceSiteMaster();
  
  /**
   * @return TRUE if IDs that are generated are deterministic, FALSE 
   * otherwise. Deterministic IDs are generated from a hash of the KeyName value.
   */
  public boolean hasDeterministicIds();
}
