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
package com.runwaysdk.constants;

public enum RelationshipTypes {
  /**
   * Root metadata relationship attribute class
   */
  METADATA_RELATIONSHIP(Constants.METADATA_PACKAGE + "." + "MetadataRelationship", "0000000000000000000000000000056320060824000000000000000000000011", "metadata_relationship"),

  /**
   * Relationship that defines inheritance between entites.
   */
  CLASS_INHERITANCE(Constants.METADATA_PACKAGE + "." + "ClassInheritance", "0000000000000000000000000000007220060824000000000000000000000011", "class_inheritance"),

  /**
   * Relationship that defines inheritance between classes.
   */
  BUSINESS_INHERITANCE(Constants.METADATA_PACKAGE + "." + "BusinessInheritance", "20070926NM000000000000000000000420060824000000000000000000000011", "business_inheritance"),

  /**
   * Relationship that defines inheritance between relationships.
   */
  RELATIONSHIP_INHERITANCE(Constants.METADATA_PACKAGE + "." + "RelationshipInheritance", "20070926NM000000000000000000001020060824000000000000000000000011", "relationship_inheritance"),

  /**
   * Relationship that defines inheritance between views.
   */
  VIEW_INHERITANCE(Constants.METADATA_PACKAGE + "." + "ViewInheritance", "20071130NM000000000000000000000720060824000000000000000000000011", "view_inheritance"),

  /**
   * Relationship that defines inheritance between util classes.
   */
  UTIL_INHERITANCE(Constants.METADATA_PACKAGE + "." + "UtilInheritance", "20080227NM000000000000000000004020060824000000000000000000000011", "util_inheritance"),

  /**
   * Relationship that defines inheritance between exceptions.
   */
  EXCEPTION_INHERITANCE(Constants.METADATA_PACKAGE + "." + "ExceptionInheritance", "20071130NM000000000000000000000320060824000000000000000000000011", "exception_inheritance"),

  /**
   * Relationship that defines inheritance between problems.
   */
  PROBLEM_INHERITANCE(Constants.METADATA_PACKAGE + "." + "ProblemInheritance", "20071130NM000000000000000000002120060824000000000000000000000011", "problem_inheritance"),

  /**
   * Relationship that defines inheritance between warnings.
   */
  WARNING_INHERITANCE(Constants.METADATA_PACKAGE + "." + "WarningInheritance", "20071130NM000000000000000000001520060824000000000000000000000011", "warning_inheritance"),

  /**
   * Relationship that defines inheritance between information classes.
   */
  INFORMATION_INHERITANCE(Constants.METADATA_PACKAGE + "." + "InformationInheritance", "NM20080815000000000000000000000520060824000000000000000000000011", "information_inheritance"),

  /**
   * Relationship that defines Views that inherit virtual attributes from
   * entities
   */
  INCLUDE_ATTRIBUTES(Constants.METADATA_PACKAGE + "." + "IncludeAttributes", "NM20081105000000000000000000000120060824000000000000000000000011", "include_attributes"),

  /**
   * Relationship that defines attributes that are defined by a class.
   */
  CLASS_ATTRIBUTE(Constants.METADATA_PACKAGE + "." + "ClassAttribute", "NM20081105000000000000000000000120060824000000000000000000000011", "class_attribute"),

  /**
   * Relationship that defines concrete attributes that are defined by a class.
   */
  CLASS_ATTRIBUTE_CONCRETE(Constants.METADATA_PACKAGE + "." + "ClassAttributeConcrete", "0000000000000000000000000000007320060824000000000000000000000011", "class_attribute_concrete"),

  /**
   * Relationship that defines virtual attributes that are defined by a class.
   */
  CLASS_ATTRIBUTE_VIRTUAL(Constants.METADATA_PACKAGE + "." + "ClassAttributeVirtual", "NM20081106000000000000000000000520060824000000000000000000000011", "class_attribute_virtual"),

  /**
   * Relationship that represents a link between a concrete attribute and
   * virtual attributes that reference it.
   */
  VIRTUALIZE_ATTRIBUTE(Constants.METADATA_PACKAGE + "." + "VirtualizeAttribute", "NM20081120000000000000000000002320060824000000000000000000000011", "virtualize_attribute"),

  /**
   * Relationship that defines attributes for an enumeration.
   */
  ENUMERATION_ATTRIBUTE(Constants.METADATA_PACKAGE + "." + "EnumerationAttribute", "0000000000000000000000000000021220060824000000000000000000000011", "enumeration_attribute"),

  /**
   * Relationship that defines attributes for an enumeration.
   */
  ENUMERATION_ATTRIBUTE_ITEM(Constants.METADATA_PACKAGE + "." + "EnumerationAttributeItem", "0000000000000000000000000000029420060824000000000000000000000011", "enumeration_attribute_item"),

  /**
   * Relationship that defines a database index constraint on an entity.
   */
  ENTITY_INDEX(Constants.METADATA_PACKAGE + "." + "EntityIndex", "NM20070418000000000000000000006720060824000000000000000000000011", "entity_index"),

  /**
   * Root transition relationship Metadata class
   */
  TRANSITION_RELATIONSHIP(Constants.SYSTEM_PACKAGE + "." + "Transition", "0000000000000000000000000000093920060824000000000000000000000001", "transition"),

  /**
   * Relationship that defines MdMethods for a MdEntity
   */
  MD_TYPE_MD_METHOD(Constants.METADATA_PACKAGE + "." + "TypeMethod", "20070427JS000000000000000000152020060824000000000000000000000011", "type_method"),

  /**
   * Relationship that defines MdParameters for a MdMethod or MdAction
   */
  METADATA_PARAMETER(Constants.SYSTEM_PACKAGE + "." + "MetadataParameter", "20070427JS000000000000000000152420060824000000000000000000000011", "metadata_parameter"),

  /**
   * Relationship that defines an SingeActor's assignment to a Role
   */
  ASSIGNMENTS(Constants.SYSTEM_PACKAGE + "." + "Assignments", "0000000000000000000000000000050520060824000000000000000000000011", "assignments"),

  /**
   * Relationship for permissions between an Actor and MetaData
   */
  TYPE_PERMISSION(Constants.SYSTEM_PACKAGE + "." + "TypePermission", "0000000000000000000000000000051320060824000000000000000000000011", "type_permissions"),

  /**
   * Relationship for MethodActor and MdMethod
   */
  MD_METHOD_METHOD_ACTOR(Constants.METADATA_PACKAGE + "." + "MdMethodMethodActor", "JS20071106000000000000000011111120060824000000000000000000000011", "md_method_method_actor"),

  /**
   * Relationship that defines MdActions for a MdController
   */
  CONTROLLER_ACTION(Constants.SYSTEM_PACKAGE + "." + "ControllerAction", "JS20080723000000000000000011111120060824000000000000000000000011", "controller_action"),

  /**
   * A MdDimension has MdAttributeDimensiion
   */
  DIMENSION_HAS_ATTRIBUTES(Constants.METADATA_PACKAGE + "." + "DimensionHasAttribute", "qs6a32c2wsxz7xp75r3kcxryhqx3qwui20060824000000000000000000000011", "dimension_has_attribute"),

  /**
   * MdAttributes have MdAttributeDimensiion
   */
  ATTRIBUTE_HAS_DIMENSION(Constants.METADATA_PACKAGE + "." + "AttributeHasDimension", "088krl4llbs2hzg1n65ub3mavye46rhe20060824000000000000000000000011", "attribute_has_dimension"),

  /**
   * MdDimensions add attributes to local struct types.
   */
  DIMENSION_DEFINES_LOCAL_STRUCT_ATTRIBUTE(Constants.METADATA_PACKAGE + "." + "DimensionDefinesLocalStructAttribute", "xy0s4cyyh27st67rjjb5apdx64cm8ali20060824000000000000000000000011", "dimension_def_struct_attr"),

  DIMENSION_HAS_CLASS(Constants.METADATA_PACKAGE + "." + "DimensionHasClass", "JS06222010000000000000000000000720060824000000000000000000000011", "dimension_has_class"),

  CLASS_HAS_DIMENSION(Constants.METADATA_PACKAGE + "." + "ClassHasDimension", "JS06222010000000000000000000000620060824000000000000000000000011", "class_has_dimension"),

  WEB_GROUP_FIELD(Constants.METADATA_PACKAGE + "." + "WebGroupField", "z2epz1og1pmychr9c0twnyl74akosv6220060824000000000000000000000011", "web_group_field"),

  MOBILE_GROUP_FIELD(Constants.METADATA_PACKAGE + "." + "MobileGroupField", "7b45xkrb8yskw2d2b4birvy400wbo1pr20060824000000000000000000000011", "mobile_group_field");

  private String relationshipType;

  private String id;

  private String tableName;

  private RelationshipTypes(String relationshipType, String id, String tableName)
  {
    this.relationshipType = relationshipType;
    this.id = id;
    this.tableName = tableName;
  }

  public String getType()
  {
    return this.relationshipType;
  }

  public String getId()
  {
    return this.id;
  }

  public String getTableName()
  {
    return this.tableName;
  }
}
