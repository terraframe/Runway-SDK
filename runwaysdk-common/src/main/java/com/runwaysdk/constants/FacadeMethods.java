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

public enum FacadeMethods {
  CHECK_ADMIN_SCREEN_ACCESS("checkAdminScreenAccess"),

  GET_QUERY("getQuery"),

  LOGIN("login"),

  LOGIN_USER("loginUser"),

  LOGIN_ANONYMOUS("loginAnonymous"),

  CHANGE_LOGIN("changeLogin"),

  SET_DIMENSION("setDimension"),

  GET_SESSION_USER("getSessionUser"),

  LOGOUT("logout"),

  NEW_BUSINESS("newBusiness"),

  NEW_DISCONNECTED_ENTITY("newDisconnectedEntity"),

  CREATE_SESSION_COMPONENT("createSessionComponent"),

  CREATE_BUSINESS("createBusiness"),

  NEW_STRUCT("newStruct"),

  NEW_MUTABLE("newMutable"),

  CREATE_STRUCT("createStruct"),

  CREATE_RELATIONSHIP("createRelationship"),

  DELETE("delete"),

  GET_INSTANCE("get"),

  UPDATE("update"),

  ADD_PARENT("addParent"),

  ADD_CHILD("addChild"),

  DELETE_CHILD("deleteChild"),

  DELETE_PARENT("deleteParent"),

  DELETE_CHILDREN("deleteChildren"),

  DELETE_PARENTS("deleteParents"),

  GET_CHILDREN("getChildren"),

  GET_CHILD_RELATIONSHIPS("getChildRelationships"),

  GET_PARENTS("getParents"),

  GET_PARENT_RELATIONSHIPS("getParentRelationships"),

  GET_ENUMERATION("getEnumeration"),

  GET_ALL_ENUMERATIONS("getAllEnumerations"),

  GET_ENUMERATIONS("getEnumerations"),

  GET_FACADES("getFacades"),

  IMPORT_TYPES("importTypes"),

  GET_NEWEST_UPDATED_DATE("getNewestUpdateDate"),

  LOCK("lock"),

  UNLOCK("unlock"),

  ASSIGN_MEMBER("assignMember"),

  REMOVE_MEMBER("removeMember"),

  GRANT_STATE_PERMISSION("grantStatePermission"),

  GRANT_TYPE_PERMISSION("grantTypePermission"),

  GRANT_METHOD_PERMISSION("grantMethodPermission"),

  GRANT_ATTRIBUTE_PERMISSION("grantAttributePermission"),

  GRANT_ATTRIBUTE_STATE_PERMISSION("grantAttributeStatePermission"),

  PROMOTE_OBJECT("promoteObject"),

  REVOKE_TYPE_PERMISSION("revokeTypePermission"),

  REVOKE_METHOD_PERMISSION("revokeMethodPermission"),

  REVOKE_STATE_PERMISSION("revokeStatePermission"),

  REVOKE_ATTRIBUTE_PERMISSION("revokeAttributePermission"),

  REVOKE_ATTRIBUTE_STATE_PERMISSION("revokeAttributeStatePermission"),

  QUERY_BUSINESSES("queryBusinesses"),

  QUERY_VIEWS("queryViews"),

  QUERY_STRUCTS("queryStructs"),

  QUERY_ENTITIES("queryEntities"),

  QUERY_RELATIONSHIPS("queryRelationships"),

  INVOKE_METHOD("invokeMethod"),

  GET_FILE("getFile"),

  GET_SECURE_FILE("getSecureFile"),

  NEW_SECURE_FILE("newSecureFile"),

  NEW_FILE("newFile"),

  GET_VAULT_FILE_DTO("getVaultFileDTO"),

  GROOVY_VALUE_QUERY("groovyValueQuery"),

  GROOVY_OBJECT_QUERY("groovyObjectQuery"),

  IMPORT_DOMAIN_MODEL("importDomainModel"),

  IMPORT_INSTANCE_XML("importInstanceXML"),

  GET_ALL_INSTANCES("getAllInstances"),

  // Term methods :
  GET_TERM_ALL_CHILDREN("getTermAllChildren"), MOVE_BUSINESS("moveBusiness");

  private String name;

  private FacadeMethods(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }
}
