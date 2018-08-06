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

/**
 * Lists all possible parameters that the JSONClientRequest will send to the server.
 */
public enum JSONClientRequestConstants {

  /**
   * Parent oid parameter.
   */
  PARENT_ID("parentId"),
  
  /**
   * Child oid parameter.
   */
  CHILD_ID("childId"),
  
  /**
   * Relationship type parameter.
   */
  RELATIONSHIP_TYPE("relationshipType"),
  
  /**
   * Generic oid parameter.
   */
  OID("oid"),
  
  /**
   * Type parameter.
   */
  TYPE("type"),
  
  /**
   * Types parameter for importing
   */
  TYPES("types"),
  
  /**
   * Groovy Query
   */
  GROOVY_QUERY("queryQuery"),
  
  /**
   * StructDTO parameter.
   */
  STRUCT_DTO("structDTO"),
  
  /**
   * Enum type parameter.
   */
  ENUM_TYPE("enumType"),
  
  /**
   * Enum name parameter.
   */
  ENUM_NAME("enumName"),
  
  /**
   * Enum name array parameter.
   */
  ENUM_NAMES("enumNames"),
  
  /**
   * QueryDTO parameter.
   */
  QUERY_DTO("queryDTO"),
  
  /**
   * EntityDTO parameter.
   */
  ENTITY_DTO("entityDTO"),
  
  /**
   * MutableDTO parameter.
   */
  MUTABLE_DTO("mutableDTO"),
  
  /**
   * Metadata parameters.
   */
  METADATA("metadata"),
  
  /**
   * Parameters parameter.
   */
  PARAMETERS("parameters"),
  
  /**
   * Relationship oid parameter.
   */
  RELATIONSHIP_ID("relationshipId"),
  
  /**
   * BusinessDTO parameter.
   */
  BUSINESS_DTO("businessDTO"),
  
  /**
   * SessionDTO parameters
   */
  SESSION_DTO("sessionDTO"),
  
  /**
   * RelationshipDTO parameter.
   */
  RELATIONSHIP_DTO("relationshipDTO"),
  
  /**
   * Username parameter.
   */
  USERNAME("username"),
  
  /**
   * Password parameter.
   */
  PASSWORD("password"),
  
  /**
   * Password parameter.
   */
  LOCALE("locale"),
  
  /**
   * Actor oid parameter.
   */
  ACTOR_ID("actorId"),
  
  /**
   * Operation ids parameter (also functions for single operations since
   * we can't overload in javascript).
   */
  OPERATION_NAMES("operationNames"),
  
  /**
   * MdAttribute oid parameter.
   */
  MDATTRIBUTE_ID("mdAttributeId"),
  
  /**
   * State oid parameter.
   */
  STATE_ID("stateId"),
  
  /**
   * MdType oid parameter.
   */
  MDTYPE_ID("mdTypeId"),
  
  /**
   * MdMethod oid parameter.
   */
  MDMETHOD_ID("mdMethodId"),
  
  /**
   * Transition name parameter.
   */
  TRANSITION_NAME("transitionName"),
  
  /**
   * PageNum name parameter for getTermAllChildren.
   */
  PAGE_NUM("pageNum"),
  
  /**
   * pageSize name parameter for getTermAllChildren.
   */
  PAGE_SIZE("pageSize"),
  
  /**
   * Method POST parameter (describes what method to execute server-side).
   */
  METHOD("method"),
  
  CLONE_ID("cloneId");
  
  private String name;
  
  private JSONClientRequestConstants(String name)
  {
    this.name = name;
  }
  
  public String getName()
  {
    return name;
  }
  
}
