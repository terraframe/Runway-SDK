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
package com.runwaysdk.constants;


public interface MdRelationshipInfo extends MdElementInfo
{

  /**
   * Class MdRelationship.
   */
  public static final String CLASS   = Constants.METADATA_PACKAGE+".MdRelationship";
  
  public static final String ID_VALUE                   = "7658319f-d93c-3ee2-9056-ba352f00003a";
  
  /**
   * Name of the attribute that stores the oid of the super class.
   */
  public static final String SUPER_MD_RELATIONSHIP     = "superMdRelationship";
  /**
   * NAme of the attribute that describes if the relationship is a composition relationship.
   */
  public static final String COMPOSITION               = "composition";
  /**
   * Name of the attribute that specifies the caching algorithm used for this relationship.
   */
  public static final String CACHE_ALGORITHM           = "cacheAlgorithm";
  /**
   * Name of the attribute that specifies the oid of the metadata that defines the type of 
   * objects that are parents in this relationship type.
   */
  public static final String PARENT_MD_BUSINESS        = "parentMdBusiness";
  /**
   * Name of the attribute that specifies the oid of the metadata that defines the type of 
   * objects that are children in this relationship type.
   */
  public static final String CHILD_MD_BUSINESS         = "childMdBusiness";
  /**
   * Name of the attribute that specifies cardinality of parent instances in this
   * relationship type.
   */
  public static final String PARENT_CARDINALITY         = "parentCardinality";
  /**
   * Name of the attribute that specifies cardinality of child instances in this
   * relationship type.
   */
  public static final String CHILD_CARDINALITY          = "childCardinality";
  /**
   * Name of the attribute that specifies the display label of the role of the
   * parents in this relationship.
   */
  public static final String PARENT_DISPLAY_LABEL       = "parentDisplayLabel";
  /**
   * Name of the attribute that specifies the display label of the role of the
   * children in this relationship.
   */
  public static final String CHILD_DISPLAY_LABEL        = "childDisplayLabel";
  /**
   * Specifies the name of the getter method for the parent in the relationship.
   * Note that the method actually exists on the child.  For example, in the
   * relationship Person Owns Vehicle (Person is the parent), an appropriate
   * parent_method could be "Owners".  This would result in the method
   * Vehicle.getAllOwners().
   */
  public static final String PARENT_METHOD              = "parentMethod";
  /**
   * Specifies the name of the getter method for the child in the relationship.
   * Note that the method actually exists on the parent.  For example, in the
   * relationship Person Owns Vehicle (Vehicle is the child), an appropriate
   * child_method could be "VehiclesOwned".  This would result in the method
   * Person.getAllVehiclesOwned().
   */
  public static final String CHILD_METHOD               = "childMethod";
  
  /**
   * Specifies the visibility of the parent accessor method.
   */
  public static final String PARENT_VISIBILITY          = "parentVisibility";
  
  /**
   * Specifies the visibility of the child accessor method.
   */
  public static final String CHILD_VISIBILITY           = "childVisibility";
  
  /**
   * Reference of the attribute that specifies the oid of the metadata that defines 
   * the type used to store attributes on this relationship.
   */
  public static final String SORT_MD_ATTRIBUTE          = "sortMdAttribute";

}
