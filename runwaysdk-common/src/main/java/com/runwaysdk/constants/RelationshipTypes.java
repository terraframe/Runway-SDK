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
  METADATA_RELATIONSHIP(Constants.METADATA_PACKAGE + "." + "MetadataRelationship", "30603755-1131-3398-8344-4d92c6730103", "metadata_relationship"),

  /**
   * Relationship that defines inheritance between entites.
   */
  CLASS_INHERITANCE(Constants.METADATA_PACKAGE + "." + "ClassInheritance", "033a23b2-3984-3b19-a2e8-f10ec7670103", "class_inheritance"),

  /**
   * Relationship that defines inheritance between classes.
   */
  BUSINESS_INHERITANCE(Constants.METADATA_PACKAGE + "." + "BusinessInheritance", "c26dd709-3607-3b5f-9321-2ead6a9a0103", "business_inheritance"),

  /**
   * Relationship that defines inheritance between relationships.
   */
  RELATIONSHIP_INHERITANCE(Constants.METADATA_PACKAGE + "." + "RelationshipInheritance", "a14da600-593e-3b22-b4cb-6ee4f6ef0103", "relationship_inheritance"),

  /**
   * Relationship that defines inheritance between views.
   */
  VIEW_INHERITANCE(Constants.METADATA_PACKAGE + "." + "ViewInheritance", "36a3121f-b04e-3243-a079-eff8e0400103", "view_inheritance"),

  /**
   * Relationship that defines inheritance between util classes.
   */
  UTIL_INHERITANCE(Constants.METADATA_PACKAGE + "." + "UtilInheritance", "f3ce6406-a98a-3574-830f-3fff8e230103", "util_inheritance"),

  /**
   * Relationship that defines inheritance between exceptions.
   */
  EXCEPTION_INHERITANCE(Constants.METADATA_PACKAGE + "." + "ExceptionInheritance", "c4834782-692d-351f-9aeb-08b3fc870103", "exception_inheritance"),

  /**
   * Relationship that defines inheritance between problems.
   */
  PROBLEM_INHERITANCE(Constants.METADATA_PACKAGE + "." + "ProblemInheritance", "7f7d1481-2901-3bf4-9838-6c950abf0103", "problem_inheritance"),

  /**
   * Relationship that defines inheritance between warnings.
   */
  WARNING_INHERITANCE(Constants.METADATA_PACKAGE + "." + "WarningInheritance", "47f1f5c5-98e6-3062-92cc-f0d5d5340103", "warning_inheritance"),

  /**
   * Relationship that defines inheritance between information classes.
   */
  INFORMATION_INHERITANCE(Constants.METADATA_PACKAGE + "." + "InformationInheritance", "82b70bb2-bd47-372c-bd84-60e5e1fc0103", "information_inheritance"),

  /**
   * Relationship that defines Views that inherit virtual attributes from
   * entities
   */
  INCLUDE_ATTRIBUTES(Constants.METADATA_PACKAGE + "." + "IncludeAttributes", "dd422d91-2379-3870-b95a-ad9bcdba0103", "include_attributes"),

  /**
   * Relationship that defines attributes that are defined by a class.
   */
  CLASS_ATTRIBUTE(Constants.METADATA_PACKAGE + "." + "ClassAttribute", "dd422d91-2379-3870-b95a-ad9bcdba0103", "class_attribute"),

  /**
   * Relationship that defines concrete attributes that are defined by a class.
   */
  CLASS_ATTRIBUTE_CONCRETE(Constants.METADATA_PACKAGE + "." + "ClassAttributeConcrete", "0acce99e-22f2-3d48-87a2-fb0a100e0103", "class_attribute_concrete"),

  /**
   * Relationship that defines virtual attributes that are defined by a class.
   */
  CLASS_ATTRIBUTE_VIRTUAL(Constants.METADATA_PACKAGE + "." + "ClassAttributeVirtual", "ae98ea4c-15e2-335b-863b-3fb8682c0103", "class_attribute_virtual"),

  /**
   * Relationship that represents a link between a concrete attribute and
   * virtual attributes that reference it.
   */
  VIRTUALIZE_ATTRIBUTE(Constants.METADATA_PACKAGE + "." + "VirtualizeAttribute", "11b1b3ff-26a9-3cf6-9ecb-022589510103", "virtualize_attribute"),

  /**
   * Relationship that defines attributes for an enumeration.
   */
  ENUMERATION_ATTRIBUTE(Constants.METADATA_PACKAGE + "." + "EnumerationAttribute", "3c0da934-60e9-3c23-9b71-7e3ded4c0103", "enumeration_attribute"),

  /**
   * Relationship that defines attributes for an enumeration.
   */
  ENUMERATION_ATTRIBUTE_ITEM(Constants.METADATA_PACKAGE + "." + "EnumerationAttributeItem", "a05fecd1-3fb8-369d-a303-1db4ac540103", "enumeration_attribute_item"),

  /**
   * Relationship that defines a database index constraint on an entity.
   */
  ENTITY_INDEX(Constants.METADATA_PACKAGE + "." + "EntityIndex", "2e822096-5c11-3d51-ae6e-01594ad10103", "entity_index"),

  /**
   * Relationship that defines MdMethods for a MdEntity
   */
  MD_TYPE_MD_METHOD(Constants.METADATA_PACKAGE + "." + "TypeMethod", "0f4aa778-0e64-332f-9f3d-5c09c8a40103", "type_method"),

  /**
   * Relationship that defines MdParameters for a MdMethod or MdAction
   */
  METADATA_PARAMETER(Constants.SYSTEM_PACKAGE + "." + "MetadataParameter", "a08d28a4-f3f8-3ddc-95d0-e0bd1f020103", "metadata_parameter"),

  /**
   * Relationship that defines an SingeActor's assignment to a Role
   */
  ASSIGNMENTS(Constants.SYSTEM_PACKAGE + "." + "Assignments", "5d659532-e83c-3b1c-854f-7e8906240103", "assignments"),

  /**
   * Relationship for permissions between an Actor and MetaData
   */
  TYPE_PERMISSION(Constants.SYSTEM_PACKAGE + "." + "TypePermission", "ed5581d4-c3e3-3f53-8fd6-88b232290103", "type_permissions"),

  /**
   * Relationship for MethodActor and MdMethod
   */
  MD_METHOD_METHOD_ACTOR(Constants.METADATA_PACKAGE + "." + "MdMethodMethodActor", "8d4b0216-296e-31d2-ac70-a77c7fa50103", "md_method_method_actor"),

  /**
   * Relationship that defines MdActions for a MdController
   */
  CONTROLLER_ACTION(Constants.SYSTEM_PACKAGE + "." + "ControllerAction", "eb58ae6e-cf1c-3378-aaad-424d2d3b0103", "controller_action"),

  /**
   * MdDimensions add attributes to local struct types.
   */
  DIMENSION_DEFINES_LOCAL_STRUCT_ATTRIBUTE(Constants.METADATA_PACKAGE + "." + "DimensionDefinesLocalStructAttribute", "5d77e12b-dbe6-3e0b-9910-8a12dd360103", "dimension_def_struct_attr"),

  DIMENSION_HAS_CLASS(Constants.METADATA_PACKAGE + "." + "DimensionHasClass", "53035b2c-502d-3f73-ab20-13bd2cc60103", "dimension_has_class"),

  CLASS_HAS_DIMENSION(Constants.METADATA_PACKAGE + "." + "ClassHasDimension", "05b53b19-5ece-377f-b284-3a888a560103", "class_has_dimension"),

  WEB_GROUP_FIELD(Constants.METADATA_PACKAGE + "." + "WebGroupField", "z2epz1og1pmychr9c0twnyl74akosv6220060824000000000000000000000011", "web_group_field"),

  MOBILE_GROUP_FIELD(Constants.METADATA_PACKAGE + "." + "MobileGroupField", "7b45xkrb8yskw2d2b4birvy400wbo1pr20060824000000000000000000000011", "mobile_group_field"),

  WEB_GRID_FIELD(Constants.METADATA_PACKAGE + "." + "WebGridField", "56e337fa-9ed2-3d5c-91ef-b360abc90062", "web_grid_field"),

  /**
   * {@link MdAttributeRatioDAOIF} references a {@linL RatioDAOIF}.
   */

  ATTRIBUTE_INDICATOR(Constants.METADATA_PACKAGE + "." + "AttributeIndicator", "fc80090f-fe2d-32de-a289-fd3c7fd90103", "attribute_indicator");
  
  
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
