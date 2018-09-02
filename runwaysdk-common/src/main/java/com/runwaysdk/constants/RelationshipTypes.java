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

public enum RelationshipTypes {
  /**
   * Root metadata relationship attribute class
   */
  METADATA_RELATIONSHIP(Constants.METADATA_PACKAGE + "." + "MetadataRelationship", "000000000000000000000000000005630103", "metadata_relationship"),

  /**
   * Relationship that defines inheritance between entites.
   */
  CLASS_INHERITANCE(Constants.METADATA_PACKAGE + "." + "ClassInheritance", "000000000000000000000000000000720103", "class_inheritance"),

  /**
   * Relationship that defines inheritance between classes.
   */
  BUSINESS_INHERITANCE(Constants.METADATA_PACKAGE + "." + "BusinessInheritance", "20070926NM00000000000000000000040103", "business_inheritance"),

  /**
   * Relationship that defines inheritance between relationships.
   */
  RELATIONSHIP_INHERITANCE(Constants.METADATA_PACKAGE + "." + "RelationshipInheritance", "20070926NM00000000000000000000100103", "relationship_inheritance"),

  /**
   * Relationship that defines inheritance between views.
   */
  VIEW_INHERITANCE(Constants.METADATA_PACKAGE + "." + "ViewInheritance", "20071130NM00000000000000000000070103", "view_inheritance"),

  /**
   * Relationship that defines inheritance between util classes.
   */
  UTIL_INHERITANCE(Constants.METADATA_PACKAGE + "." + "UtilInheritance", "20080227NM00000000000000000000400103", "util_inheritance"),

  /**
   * Relationship that defines inheritance between exceptions.
   */
  EXCEPTION_INHERITANCE(Constants.METADATA_PACKAGE + "." + "ExceptionInheritance", "20071130NM00000000000000000000030103", "exception_inheritance"),

  /**
   * Relationship that defines inheritance between problems.
   */
  PROBLEM_INHERITANCE(Constants.METADATA_PACKAGE + "." + "ProblemInheritance", "20071130NM00000000000000000000210103", "problem_inheritance"),

  /**
   * Relationship that defines inheritance between warnings.
   */
  WARNING_INHERITANCE(Constants.METADATA_PACKAGE + "." + "WarningInheritance", "20071130NM00000000000000000000150103", "warning_inheritance"),

  /**
   * Relationship that defines inheritance between information classes.
   */
  INFORMATION_INHERITANCE(Constants.METADATA_PACKAGE + "." + "InformationInheritance", "NM2008081500000000000000000000050103", "information_inheritance"),

  /**
   * Relationship that defines Views that inherit virtual attributes from
   * entities
   */
  INCLUDE_ATTRIBUTES(Constants.METADATA_PACKAGE + "." + "IncludeAttributes", "NM2008110500000000000000000000010103", "include_attributes"),

  /**
   * Relationship that defines attributes that are defined by a class.
   */
  CLASS_ATTRIBUTE(Constants.METADATA_PACKAGE + "." + "ClassAttribute", "NM2008110500000000000000000000010103", "class_attribute"),

  /**
   * Relationship that defines concrete attributes that are defined by a class.
   */
  CLASS_ATTRIBUTE_CONCRETE(Constants.METADATA_PACKAGE + "." + "ClassAttributeConcrete", "000000000000000000000000000000730103", "class_attribute_concrete"),

  /**
   * Relationship that defines virtual attributes that are defined by a class.
   */
  CLASS_ATTRIBUTE_VIRTUAL(Constants.METADATA_PACKAGE + "." + "ClassAttributeVirtual", "NM2008110600000000000000000000050103", "class_attribute_virtual"),

  /**
   * Relationship that represents a link between a concrete attribute and
   * virtual attributes that reference it.
   */
  VIRTUALIZE_ATTRIBUTE(Constants.METADATA_PACKAGE + "." + "VirtualizeAttribute", "NM2008112000000000000000000000230103", "virtualize_attribute"),

  /**
   * Relationship that defines attributes for an enumeration.
   */
  ENUMERATION_ATTRIBUTE(Constants.METADATA_PACKAGE + "." + "EnumerationAttribute", "000000000000000000000000000002120103", "enumeration_attribute"),

  /**
   * Relationship that defines attributes for an enumeration.
   */
  ENUMERATION_ATTRIBUTE_ITEM(Constants.METADATA_PACKAGE + "." + "EnumerationAttributeItem", "000000000000000000000000000002940103", "enumeration_attribute_item"),

  /**
   * Relationship that defines a database index constraint on an entity.
   */
  ENTITY_INDEX(Constants.METADATA_PACKAGE + "." + "EntityIndex", "NM2007041800000000000000000000670103", "entity_index"),

  /**
   * Relationship that defines MdMethods for a MdEntity
   */
  MD_TYPE_MD_METHOD(Constants.METADATA_PACKAGE + "." + "TypeMethod", "20070427JS00000000000000000015200103", "type_method"),

  /**
   * Relationship that defines MdParameters for a MdMethod or MdAction
   */
  METADATA_PARAMETER(Constants.SYSTEM_PACKAGE + "." + "MetadataParameter", "20070427JS00000000000000000015240103", "metadata_parameter"),

  /**
   * Relationship that defines an SingeActor's assignment to a Role
   */
  ASSIGNMENTS(Constants.SYSTEM_PACKAGE + "." + "Assignments", "000000000000000000000000000005050103", "assignments"),

  /**
   * Relationship for permissions between an Actor and MetaData
   */
  TYPE_PERMISSION(Constants.SYSTEM_PACKAGE + "." + "TypePermission", "000000000000000000000000000005130103", "type_permissions"),

  /**
   * Relationship for MethodActor and MdMethod
   */
  MD_METHOD_METHOD_ACTOR(Constants.METADATA_PACKAGE + "." + "MdMethodMethodActor", "JS2007110600000000000000001111110103", "md_method_method_actor"),

  /**
   * Relationship that defines MdActions for a MdController
   */
  CONTROLLER_ACTION(Constants.SYSTEM_PACKAGE + "." + "ControllerAction", "JS2008072300000000000000001111110103", "controller_action"),

  /**
   * MdDimensions add attributes to local struct types.
   */
  DIMENSION_DEFINES_LOCAL_STRUCT_ATTRIBUTE(Constants.METADATA_PACKAGE + "." + "DimensionDefinesLocalStructAttribute", "xy0s4cyyh27st67rjjb5apdx64cm8ali0103", "dimension_def_struct_attr"),

  DIMENSION_HAS_CLASS(Constants.METADATA_PACKAGE + "." + "DimensionHasClass", "JS0622201000000000000000000000070103", "dimension_has_class"),

  CLASS_HAS_DIMENSION(Constants.METADATA_PACKAGE + "." + "ClassHasDimension", "JS0622201000000000000000000000060103", "class_has_dimension"),

  WEB_GROUP_FIELD(Constants.METADATA_PACKAGE + "." + "WebGroupField", "z2epz1og1pmychr9c0twnyl74akosv620103", "web_group_field"),

  MOBILE_GROUP_FIELD(Constants.METADATA_PACKAGE + "." + "MobileGroupField", "7b45xkrb8yskw2d2b4birvy400wbo1pr0103", "mobile_group_field"),

  WEB_GRID_FIELD(Constants.METADATA_PACKAGE + "." + "WebGridField", "z4gu2leptmskoe7v4lk68izcvc15k3j40062", "web_grid_field"),

  /**
   * {@link MdAttributeRatioDAOIF} references a {@linL RatioDAOIF}.
   */

  ATTRIBUTE_INDICATOR(Constants.METADATA_PACKAGE + "." + "AttributeIndicator", "insw5s11o7uyyxpddw0mmv3v3rct7c1k0103", "attribute_indicator");
  
  
  private String relationshipType;

  private String oid;

  private String tableName;

  private RelationshipTypes(String relationshipType, String oid, String tableName)
  {
    this.relationshipType = relationshipType;
    this.oid = oid;
    this.tableName = tableName;
  }

  public String getType()
  {
    return this.relationshipType;
  }

  public String getOid()
  {
    return this.oid;
  }

  public String getTableName()
  {
    return this.tableName;
  }
}
