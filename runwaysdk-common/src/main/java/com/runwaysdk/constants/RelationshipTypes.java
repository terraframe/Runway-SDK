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
package com.runwaysdk.constants;

public enum RelationshipTypes {
  /**
   * Root metadata relationship attribute class
   */
  METADATA_RELATIONSHIP(Constants.METADATA_PACKAGE + "." + "MetadataRelationship", "326abc0d-31be-311d-9d65-d000a6000067", "metadata_relationship"),

  /**
   * Relationship that defines inheritance between entities.
   */
  CLASS_INHERITANCE(Constants.METADATA_PACKAGE + "." + "ClassInheritance", "6550aa0a-296c-3340-a1bc-2209ee000067", "class_inheritance"),

  /**
   * Relationship that defines inheritance between classes.
   */
  BUSINESS_INHERITANCE(Constants.METADATA_PACKAGE + "." + "BusinessInheritance", "085b97e6-2053-334c-a947-d656e3000067", "business_inheritance"),

  /**
   * Relationship that defines inheritance between relationships.
   */
  RELATIONSHIP_INHERITANCE(Constants.METADATA_PACKAGE + "." + "RelationshipInheritance", "ade9d3b2-546f-3ab3-bad0-6ddeba000067", "relationship_inheritance"),

  /**
   * Relationship that defines inheritance between views.
   */
  VIEW_INHERITANCE(Constants.METADATA_PACKAGE + "." + "ViewInheritance", "6cc3d11a-a40b-3f5f-b646-40b967000067", "view_inheritance"),

  /**
   * Relationship that defines inheritance between util classes.
   */
  UTIL_INHERITANCE(Constants.METADATA_PACKAGE + "." + "UtilInheritance", "1b88b11a-edaa-3791-b6fd-2f29bb000067", "util_inheritance"),

  /**
   * Relationship that defines inheritance between exceptions.
   */
  EXCEPTION_INHERITANCE(Constants.METADATA_PACKAGE + "." + "ExceptionInheritance", "83271048-9de5-3092-af3d-e35ae7000067", "exception_inheritance"),

  /**
   * Relationship that defines inheritance between problems.
   */
  PROBLEM_INHERITANCE(Constants.METADATA_PACKAGE + "." + "ProblemInheritance", "c0bce397-0946-35e3-b075-cf01ee000067", "problem_inheritance"),

  /**
   * Relationship that defines inheritance between warnings.
   */
  WARNING_INHERITANCE(Constants.METADATA_PACKAGE + "." + "WarningInheritance", "8c78c80c-f240-3055-a058-d72891000067", "warning_inheritance"),

  /**
   * Relationship that defines inheritance between information classes.
   */
  INFORMATION_INHERITANCE(Constants.METADATA_PACKAGE + "." + "InformationInheritance", "f02d2137-9f0b-31de-a3cf-3b152e000067", "information_inheritance"),

  /**
   * Relationship that defines Views that inherit virtual attributes from
   * entities
   */
  INCLUDE_ATTRIBUTES(Constants.METADATA_PACKAGE + "." + "IncludeAttributes", "7d234879-9249-34a5-8644-50772d000067", "include_attributes"),

  /**
   * Relationship that defines attributes that are defined by a class.
   */
  CLASS_ATTRIBUTE(Constants.METADATA_PACKAGE + "." + "ClassAttribute", "7d234879-9249-34a5-8644-50772d000067", "class_attribute"),

  /**
   * Relationship that defines concrete attributes that are defined by a class.
   */
  CLASS_ATTRIBUTE_CONCRETE(Constants.METADATA_PACKAGE + "." + "ClassAttributeConcrete", "355f2001-6615-3c76-9694-fe9b30000067", "class_attribute_concrete"),

  /**
   * Relationship that defines virtual attributes that are defined by a class.
   */
  CLASS_ATTRIBUTE_VIRTUAL(Constants.METADATA_PACKAGE + "." + "ClassAttributeVirtual", "44c2bbab-0f52-3f03-b5a5-82d00f000067", "class_attribute_virtual"),

  /**
   * Relationship that represents a link between a concrete attribute and
   * virtual attributes that reference it.
   */
  VIRTUALIZE_ATTRIBUTE(Constants.METADATA_PACKAGE + "." + "VirtualizeAttribute", "bdfda293-cbab-3f4d-b3dd-94dc4f000067", "virtualize_attribute"),

  /**
   * Relationship that defines attributes for an enumeration.
   */
  ENUMERATION_ATTRIBUTE(Constants.METADATA_PACKAGE + "." + "EnumerationAttribute", "bfbcec24-ec78-35e8-987c-404aa7000067", "enumeration_attribute"),

  /**
   * Relationship that defines attributes for an enumeration.
   */
  ENUMERATION_ATTRIBUTE_ITEM(Constants.METADATA_PACKAGE + "." + "EnumerationAttributeItem", "2c61e54e-cf3e-3ce9-9f03-b4ea4c000067", "enumeration_attribute_item"),

  /**
   * Relationship that defines a database index constraint on an entity.
   */
  ENTITY_INDEX(Constants.METADATA_PACKAGE + "." + "EntityIndex", "9e4b774e-987e-3e8c-900c-af1ca6000067", "entity_index"),

  /**
   * Relationship that defines MdMethods for a MdEntity
   */
  MD_TYPE_MD_METHOD(Constants.METADATA_PACKAGE + "." + "TypeMethod", "55a567ea-f0af-3ebb-9c63-1c5fc3000067", "type_method"),

  /**
   * Relationship that defines MdParameters for a MdMethod or MdAction
   */
  METADATA_PARAMETER(Constants.SYSTEM_PACKAGE + "." + "MetadataParameter", "7823d5f2-7ac3-328a-804b-953102000067", "metadata_parameter"),

  /**
   * Relationship that defines an SingeActor's assignment to a Role
   */
  ASSIGNMENTS(Constants.SYSTEM_PACKAGE + "." + "Assignments", "5317285b-40da-3bd2-aedb-e0901e000067", "assignments"),

  /**
   * Relationship for permissions between an Actor and MetaData
   */
  TYPE_PERMISSION(Constants.SYSTEM_PACKAGE + "." + "TypePermission", "959079e6-fcd0-3b8a-886e-91f09d000067", "type_permissions"),

  /**
   * Relationship for MethodActor and MdMethod
   */
  MD_METHOD_METHOD_ACTOR(Constants.METADATA_PACKAGE + "." + "MdMethodMethodActor", "0067006e-8bd6-339f-8159-2adc86000067", "md_method_method_actor"),

  /**
   * Relationship that defines MdActions for a MdController
   */
  CONTROLLER_ACTION(Constants.SYSTEM_PACKAGE + "." + "ControllerAction", "9abe08c1-8f64-37e3-8d9f-398503000067", "controller_action"),

  /**
   * MdDimensions add attributes to local struct types.
   */
  DIMENSION_DEFINES_LOCAL_STRUCT_ATTRIBUTE(Constants.METADATA_PACKAGE + "." + "DimensionDefinesLocalStructAttribute", "8da8939c-999c-38eb-90ac-98854c000067", "dimension_def_struct_attr"),

  DIMENSION_HAS_CLASS(Constants.METADATA_PACKAGE + "." + "DimensionHasClass", "04fd898c-996d-35a6-adf1-dbf854000067", "dimension_has_class"),

  CLASS_HAS_DIMENSION(Constants.METADATA_PACKAGE + "." + "ClassHasDimension", "fd9563a0-2e0d-3884-b007-38b905000067", "class_has_dimension"),

  WEB_GROUP_FIELD(Constants.METADATA_PACKAGE + "." + "WebGroupField", "z2epz1og1pmychr9c0twnyl74akosv6220060824000000000000000000000011", "web_group_field"),

  MOBILE_GROUP_FIELD(Constants.METADATA_PACKAGE + "." + "MobileGroupField", "7b45xkrb8yskw2d2b4birvy400wbo1pr20060824000000000000000000000011", "mobile_group_field"),

  WEB_GRID_FIELD(Constants.METADATA_PACKAGE + "." + "WebGridField", "eb03ca9a-1c0b-38b7-9163-e053a800003e", "web_grid_field"),

  /**
   * {@link MdAttributeRatioDAOIF} references a {@linL RatioDAOIF}.
   */

  ATTRIBUTE_INDICATOR(Constants.METADATA_PACKAGE + "." + "AttributeIndicator", "6d0b9bb2-6df8-3d7a-9c94-60789d000067", "attribute_indicator"),
  
  /**
   * Relationship that defines inheritance between vertex classes.
   */
  VERTEX_INHERITANCE(Constants.METADATA_PACKAGE + "." + "VertexInheritance", "12b33fd5-1697-31b0-8807-e6063e000067", "vertex_inheritance"),
  
  /**
   * Relationship that defines inheritance between edge classes.
   */
  EDGE_INHERITANCE(Constants.SYSTEM_GRAPH_PACKAGE + "." + "EdgeInheritance", "b2989c40-558b-35e3-a996-ea587f000067", "edge_inheritance"),
  
  /**
   * Relationship that defines inheritance between embedded graph classes.
   */
  EMBEDDED_GRAPH_INHERITANCE(Constants.SYSTEM_GRAPH_PACKAGE + "." + "EmbeddedGraphInheritance", "d69aa92e-076c-3f36-b80a-c7172b000067", "embedded_graph_inheritance");
  
  
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
