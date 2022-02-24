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
package com.runwaysdk.web.json;

import java.util.Locale;

import org.json.JSONException;

import com.runwaysdk.constants.JSONClientRequestIF;
import com.runwaysdk.web.javascript.JavascriptCache;

public class JSONController
{
  private static JSONClientRequestIF jsonClientRequestIF = JSONClientRequest.getDefaultRequest();

  /**
   * Returns a Javascript definition for every type defined in the parameter.
   *
   * @param types
   * @return
   */
  public static String importTypes(String sessionId, String[] types, Boolean typesOnly)
  {
    return jsonClientRequestIF.importTypes(sessionId, types, typesOnly);
  }

  /**
   *
   * @param sessionId
   * @param types
   * @return
   */
  public static String importTypes(String sessionId, String[] types)
  {
    return jsonClientRequestIF.importTypes(sessionId, types, true);
  }

  /**
   * Returns the newest last updated date of a type in a list of types
   *
   * @param types
   * @return
   */
  public static String getNewestUpdateDate(String sessionId, String[] types, Boolean typesOnly)
  {
    return jsonClientRequestIF.getNewestUpdateDate(sessionId, types, typesOnly);
  }

  /**
   * Returns the javascript necessary for all javascript DTO/ajax calls.
   *
   * @return
   */
  public static String getJavascript(String sessionId)
  {
    return JavascriptCache.getJavascript();
  }
  
  /**
   * @see com.runwaysdk.constants.JSONClientRequestIF#moveBusiness(java.lang.String,
   *      java.lang.String, java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public static String moveBusiness(String sessionId, String newParentOid, String childOid, String oldRelationshipId, String newRelationshipType) {
    return jsonClientRequestIF.moveBusiness(sessionId, newParentOid, childOid, oldRelationshipId, newRelationshipType);
  }
  
  /**
   * @see com.runwaysdk.constants.JSONClientRequestIF#getTermAllChildren(java.lang.String,
   *      java.lang.String, java.lang.Integer,
   *      java.lang.Integer)
   */
  public static String getTermAllChildren(String sessionId, String parentOid, Integer pageNum, Integer pageSize) {
    return jsonClientRequestIF.getTermAllChildren(sessionId, parentOid, pageNum, pageSize);
  }

  /**
   * @see com.runwaysdk.ClientRequest#addChild(java.lang.String,
   *      java.lang.String, java.lang.String,
   *      com.runwaysdk.transport.RelationshipDTO)
   */
  public static String addChild(String sessionId, String parentOid, String childOid,
      String relationshipType)
  {
    return jsonClientRequestIF.addChild(sessionId, parentOid, childOid, relationshipType);
  }

  /**
   * @see com.runwaysdk.ClientRequest#addParent(java.lang.String,
   *      java.lang.String, java.lang.String,
   *      com.runwaysdk.transport.RelationshipDTO)
   */
  public static String addParent(String sessionId, String parentOid, String childOid,
      String relationshipJSON)
  {
    return jsonClientRequestIF.addParent(sessionId, parentOid, childOid, relationshipJSON);
  }

  /**
   * @see com.runwaysdk.ClientRequest#delete(java.lang.String,
   *      java.lang.String)
   */
  public static String delete(String sessionId, String oid)
  {
    return jsonClientRequestIF.delete(sessionId, oid);
  }

  /**
   * @see com.runwaysdk.ClientRequest#get(java.lang.String,
   *      java.lang.String)
   */
  public static String get(String sessionId, String oid)
  {
    return jsonClientRequestIF.get(sessionId, oid);
  }

  /**
   * @see com.runwaysdk.ClientRequest#createSessionComponent(java.lang.String,
   *      com.runwaysdk.transport.SessionDTO)
   */
  public static String createSessionComponent(String sessionId, String json)
  {
    return jsonClientRequestIF.createSessionComponent(sessionId, json);
  }

  /**
   * @see com.runwaysdk.ClientRequest#createBusiness(java.lang.String,
   *      com.runwaysdk.transport.BusinessDTO)
   */
  public static String createBusiness(String sessionId, String businessJSON)
  {
    return jsonClientRequestIF.createBusiness(sessionId, businessJSON);
  }

  public static String createRelationship(String sessionId, String relationshipsJSON)
  {
    return jsonClientRequestIF.createRelationship(sessionId, relationshipsJSON);
  }

  /**
   * @throws JSONException
   * @see com.runwaysdk.ClientRequest#login(java.lang.String,
   *      java.lang.String, java.lang.String[])
   */
  public static String login(String username, String password, String[] locales)
  {
    return jsonClientRequestIF.login(username, password, locales);
  }

  /**
   * @throws JSONException
   * @see com.runwaysdk.ClientRequest#login(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String[])
   */
  public static String login(String username, String password, String dimensionKey, String[] locales)
  {
    return jsonClientRequestIF.login(username, password, dimensionKey, locales);
  }

  /**
   * @throws JSONException
   * @see com.runwaysdk.ClientRequest#setDimension(java.lang.String,
   *      java.lang.String)
   */
  public static String setDimension(String sessionId, String dimensionKey)
  {
    return jsonClientRequestIF.setDimension(sessionId, dimensionKey);
  }

  /**
   * @throws JSONException
   * @see com.runwaysdk.ClientRequest#loginAnonymous(Locale[])
   */
  public static String loginAnonymous(String[] locales)
  {
    return jsonClientRequestIF.loginAnonymous(locales);
  }

  /**
   * @throws JSONException
   * @see com.runwaysdk.ClientRequest#loginAnonymous(java.lang.String, Locale[])
   */
  public static String loginAnonymous(String dimensionKey, String[] locales)
  {
    return jsonClientRequestIF.loginAnonymous(dimensionKey, locales);
  }

  /**
   * @throws JSONException
   * @see com.runwaysdk.ClientRequest#changeLoginUser(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public static String changeLogin(String sessionId, String username, String password)
  {
    return jsonClientRequestIF.changeLogin(sessionId, username, password);
  }

  /**
   * @throws JSONException
   * @see com.runwaysdk.ClientRequest#getSessionUser()
   */
  public static String getSessionUser(String sessionId)
  {
    return jsonClientRequestIF.getSessionUser(sessionId);
  }

  /**
   * @see com.runwaysdk.ClientRequest#logout(java.lang.String)
   */
  public static String logout(String sessionId)
  {
    return jsonClientRequestIF.logout(sessionId);
  }

  /**
   * @see com.runwaysdk.ClientRequest#newBusiness(java.lang.String,
   *      java.lang.String)
   */
  public static String newBusiness(String sessionId, String type)
  {
    return jsonClientRequestIF.newBusiness(sessionId, type);
  }

  /**
   * @see com.runwaysdk.ClientRequest#newMutable(java.lang.String,
   *      java.lang.String)
   */
  public static String newMutable(String sessionId, String type)
  {
    return jsonClientRequestIF.newMutable(sessionId, type);
  }

  /**
   * @see com.runwaysdk.ClientRequest#update(java.lang.String,
   *      com.runwaysdk.transport.EntityDTO)
   */
  public static String update(String sessionId, String json)
  {
    return jsonClientRequestIF.update(sessionId, json);
  }

  /**
   * @see com.runwaysdk.ClientRequestIF#grantTypePermission(java.lang.String,
   *      java.lang.String, java.lang.String, String...)
   */
  public static String grantTypePermission(String sessionId, String actorId, String mdTypeId, String ... operationNames)
  {
    return jsonClientRequestIF.grantTypePermission(sessionId, actorId, mdTypeId, operationNames);
  }

  /**
   * @see com.runwaysdk.ClientRequestIF#grantTypePermission(java.lang.String,
   *      java.lang.String, java.lang.String, String...)
   */
  public static String grantMethodPermission(String sessionId, String actorId, String mdMethodId, String ... operationNames)
  {
    return jsonClientRequestIF.grantMethodPermission(sessionId, actorId, mdMethodId, operationNames);
  }

  /**
   * @see com.runwaysdk.ClientRequest#grantAttributePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String)
   */
  public static String grantAttributePermission(String sessionId, String actorId,
      String mdAttributeId, String ... operationNames)
  {
    return jsonClientRequestIF.grantAttributePermission(sessionId, actorId, mdAttributeId, operationNames);
  }

  /**
   * @see com.runwaysdk.ClientRequest#revokeTypePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public static String revokeTypePermission(String sessionId, String actorId, String mdTypeId, String ... operationNames)
  {
    return jsonClientRequestIF.revokeTypePermission(sessionId, actorId, mdTypeId, operationNames);
  }

  /**
   * @see com.runwaysdk.ClientRequest#revokeMethodPermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public static String revokeMethodPermission(String sessionId, String actorId, String mdMethodId, String ... operationNames)
  {
    return jsonClientRequestIF.revokeMethodPermission(sessionId, actorId, mdMethodId, operationNames);
  }

  /**
   * @see com.runwaysdk.ClientRequest#revokeAttributePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public static String revokeAttributePermission(String sessionId, String actorId, String mdAttributeId, String ... operationNames)
  {
    return jsonClientRequestIF.revokeAttributePermission(sessionId, actorId, mdAttributeId, operationNames);
  }

  /**
   * @see com.runwaysdk.ClientRequest#lock(java.lang.String,
   *      java.lang.String)
   */
  public static String lock(String sessionId, String oid)
  {
    return jsonClientRequestIF.lock(sessionId, oid);
  }

  /**
   * @see com.runwaysdk.ClientRequest#unlock(java.lang.String,
   *      java.lang.String)
   */
  public static String unlock(String sessionId, String oid)
  {
    return jsonClientRequestIF.unlock(sessionId, oid);
  }

  /**
   * @see com.runwaysdk.ClientRequest#deleteChild(java.lang.String,
   *      java.lang.String)
   */
  public static String deleteChild(String sessionId, String relationshipId)
  {
    return jsonClientRequestIF.deleteChild(sessionId, relationshipId);
  }

  /**
   * @see com.runwaysdk.ClientRequest#deleteParent(java.lang.String,
   *      java.lang.String)
   */
  public static String deleteParent(String sessionId, String relationshipId)
  {
    return jsonClientRequestIF.deleteParent(sessionId, relationshipId);
  }

  /**
   * @see com.runwaysdk.ClientRequest#getChildren(java.lang.String,
   *      com.runwaysdk.transport.BusinessDTO, java.lang.String)
   */
  public static String getChildren(String sessionId, String parentOid,
      String relationshipType)
  {
    return jsonClientRequestIF.getChildren(sessionId, parentOid, relationshipType);
  }

  public static String getChildRelationships(String sessionId, String parentOid,
      String relationshipType)
  {
    return jsonClientRequestIF.getChildRelationships(sessionId, parentOid, relationshipType);
  }

  public static String getParentRelationships(String sessionId, String childOid,
      String relationshipType)
  {
    return jsonClientRequestIF.getParentRelationships(sessionId, childOid, relationshipType);
  }

  public static String getParents(String sessionId, String childOid,
      String relationshipType)
  {
    return jsonClientRequestIF.getParents(sessionId, childOid, relationshipType);
  }

  public static String deleteChildren(String sessionId, String parentOid, String relationshipType)
  {
    return jsonClientRequestIF.deleteChildren(sessionId, parentOid, relationshipType);
  }

  public static String deleteParents(String sessionId, String childOid, String relationshipType)
  {
    return jsonClientRequestIF.deleteParents(sessionId, childOid, relationshipType);
  }

  public static String queryBusinesses(String sessionId, String queryJSON)
  {
    return jsonClientRequestIF.queryBusinesses(sessionId, queryJSON);
  }

  public static String queryViews(String sessionId, String queryJSON)
  {
    return jsonClientRequestIF.queryViews(sessionId, queryJSON);
  }
  
  public static String queryStructs(String sessionId, String queryJSON)
  {
    return jsonClientRequestIF.queryStructs(sessionId, queryJSON);
  }

  public static String queryEntities(String sessionId, String queryJSON)
  {
    return jsonClientRequestIF.queryEntities(sessionId, queryJSON);
  }

  public static String queryRelationships(String sessionId, String queryJSON)
  {
    return jsonClientRequestIF.queryRelationships(sessionId, queryJSON);
  }

  public static String invokeMethod(String sessionId, String metadata, String entityJSON, String parameters)
  {
    return jsonClientRequestIF.invokeMethod(sessionId, metadata, entityJSON, parameters);
  }

  public static String getEnumeration(String sessionId, String enumType, String enumName)
  {
    return jsonClientRequestIF.getEnumeration(sessionId, enumType, enumName);
  }

  public static String getEnumerations(String sessionId, String enumType, String[] enumNames)
  {
    return jsonClientRequestIF.getEnumerations(sessionId, enumType, enumNames);
  }

  public static String getAllEnumerations(String sessionId, String enumType)
  {
    return jsonClientRequestIF.getAllEnumerations(sessionId, enumType);
  }

  public static String createStruct(String sessionId, String json)
  {
    return jsonClientRequestIF.createStruct(sessionId, json);
  }

  public static String newStruct(String sessionId, String type)
  {
    return jsonClientRequestIF.newStruct(sessionId, type);
  }

  public static String getQuery(String sessionId, String type)
  {
    return jsonClientRequestIF.getQuery(sessionId, type);
  }

  public static String checkAdminScreenAccess(String sessionId)
  {
    return jsonClientRequestIF.checkAdminScreenAccess(sessionId);
  }
}
