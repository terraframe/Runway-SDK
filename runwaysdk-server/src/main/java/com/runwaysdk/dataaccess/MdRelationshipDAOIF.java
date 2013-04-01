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
/**
 * Created on Aug 23, 2005
 *
 */
package com.runwaysdk.dataaccess;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.constants.VisibilityModifier;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;

/**
 * @author nathan
 *
 */
public interface MdRelationshipDAOIF extends MdElementDAOIF
{
  /**
   * Name of the table used to store intances of this class.
   */
  public static final String TABLE   = "md_relationship";

  /**
   * Prefix used on the relationship indexes.
   */
  public static final String INDEX_PREFIX                = "rel_";

  /**
   * Name of the 1st database index. (parent_id, child_id)
   */
  public static final String INDEX1_NAME                 = "index1Name";

  /**
   * Name of the 2nd database index. (child_id)
   */
  public static final String INDEX2_NAME                 = "index2Name";

  /**
   *Returns the name of the package of the relationship that this relationship metadata object defines.
   * @return name of the package of the relationship that this relationship metadata object defines.
   */
  public String getPackage();

  /**
   * Returns true if this is a composition relationship, false otherwise.
   * @return true if this is a composition relationship, false otherwise.
   */
  public boolean isComposition();

  /**
   *Returns the DataAccessIF object that specifies the cache algorithm used
   * for relationships of this type.
   * @return DataAccessIF object that specifies the cache algorithm used
   *         for relationships of this type.
   */
  public BusinessDAOIF getCacheAlgorithm();

  /**
   *Returns true if instances of this relationship type are cached, false otherwise.
   * @return true if instances of this relationship type are cached, false otherwise.
   */
  public boolean cacheAllInstances();

  /**
   *Returns the metadata object that defines the type of objects that are parents
   * in this relationship.
   * @return the metadata object that defines the type of objects that are parents
   * in this relationship.
   */
  public MdBusinessDAOIF getParentMdBusiness();

  /**
   *Returns the cardinality of parent instances in this relationship type.  Value is either a
   * positive integer or "*" to represent unlimited instances.
   * @return the cardinality of parent instances in this relationship type.
   */
  public String getParentCardinality();

  /**
   *Returns the display label of the role of the parents in this relationship.
   *
   *@param locale
   *
   * @return the display label of the role of the parents in this relationship.
   */
  public String getParentDisplayLabel(Locale locale);

  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   *
   * @return map where the key is the locale and the value is the localized
   *   String value.
   */
  public Map<String, String> getParentDisplayLabes();

  /**
   * Specifies the name of the getter method for the parent in the relationship.
   * Note that the method actually exists on the child.  For example, in the
   * relationship Person Owns Vehicle (Person is the parent), an appropriate
   * parent_method could be "Owners".  This would result in the method
   * Vehicle.getAllOwners().
   */
  public String getParentMethod();

  /**
   * Returns the visibility modifier of the parent method.
   * @return the visibility modifier of the parent method.
   */
  public VisibilityModifier getParentVisibility();

  /**
   *Returns the metadata object that defines the type of objects that are children
   * in this relationship.
   * @return the metadata object that defines the type of objects that are parents
   * in this relationship.
   */
  public MdBusinessDAOIF getChildMdBusiness();

  /**
   *Returns the cardinality of child instances in this relationship type.  Value is either a
   * positive integer or "*" to represent unlimited instances.
   * @return the cardinality of child instances in this relationship type.
   */
  public String getChildCardinality();

  /**
   *Returns the display label of the role of the children in this relationship.
   *
   *@param locale
   *
   * @return the display label of the role of the children in this relationship.
   */
  public String getChildDisplayLabel(Locale locale);

  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   *
   * @return map where the key is the locale and the value is the localized
   *   String value.
   */
  public Map<String, String> getChildDisplayLabes();

  /**
   * Specifies the name of the getter method for the child in the relationship.
   * Note that the method actually exists on the parent.  For example, in the
   * relationship Person Owns Vehicle (Vehicle is the child), an appropriate
   * child_method could be "VehiclesOwned".  This would result in the method
   * Person.getAllVehiclesOwned().
   */
  public String getChildMethod();

  /**
   * Returns the visibility modifier of the child method.
   * @return the visibility modifier of the child method.
   */
  public VisibilityModifier getChildVisibility();

  /**
   * Returns the MdAttribute object that defines the attribute that this relationship should
   * be sorted by default.  If no MdAttribute is referenced, then null is returned.
   * @return MdAttribute object that defines the attribute that this relationship should
   * be sorted by default.  If no MdAttribute is referenced, then null is returned.
   */
  public MdAttributeConcreteDAOIF getSortMdAttribute();

  /**
   * Returns an array of MdRelationshipIF that defines immediate subentites of this
   * entity.
   *
   * @return an array of MdRelationshipIF that defines immediate subentites of this
   *         entity.
   */
  public List<MdRelationshipDAOIF> getSubClasses();

  /**
   * Returns a list of MdRelationshipIF objects that represent entites that are
   * subclasses of the given entity, including all recursive entities.
   *
   * @return list of MdRelationshipIF objects that represent entites that are
   *         subclasses of the given entity, including all recursive entities.
   */
  public List<MdRelationshipDAOIF> getAllSubClasses();

  /**
   * Returns a list of MdRelationshipIF objects that are subclasses of the given
   * entity. Only non abstract entities are returned (i.e. entities that can be
   * instantiated)
   *
   * @return list of MdRelationshipIF objects that are subclasses of the given entity.
   *         Only non abstract entities are returned (i.e. entities that can be
   *         instantiated)
   */
  public List<MdRelationshipDAOIF> getAllConcreteSubClasses();

  /**
   * Returns a list of MdRelationshipIF object representing every parent of this
   * MdRelationshipIF partaking in an inheritance relationship, including this class.
   *
   * @return a list of parent MdRelationshipIF objects.
   */
  public List<MdRelationshipDAOIF> getSuperClasses();

  /**
   * Returns an MdRelationshipIF representing the super entity of this entity, or null if
   * it does not have one.
   *
   * @return an MdRelationshipIF representing the super entity of this entity, or null if
   * it does not have one.
   */
  public MdRelationshipDAOIF getSuperClass();

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAOIF#getBusinessDAO()
   */
  public MdRelationshipDAO getBusinessDAO();

}
