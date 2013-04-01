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
   * @see com.runwaysdk.ClientRequest#addChild(java.lang.String,
   *      java.lang.String, java.lang.String,
   *      com.runwaysdk.transport.RelationshipDTO)
   */
  public static String addChild(String sessionId, String parentId, String childId,
      String relationshipType)
  {
    return jsonClientRequestIF.addChild(sessionId, parentId, childId, relationshipType);
  }

  /**
   * @see com.runwaysdk.ClientRequest#addParent(java.lang.String,
   *      java.lang.String, java.lang.String,
   *      com.runwaysdk.transport.RelationshipDTO)
   */
  public static String addParent(String sessionId, String parentId, String childId,
      String relationshipJSON)
  {
    return jsonClientRequestIF.addParent(sessionId, parentId, childId, relationshipJSON);
  }

  /**
   * @see com.runwaysdk.ClientRequest#delete(java.lang.String,
   *      java.lang.String)
   */
  public static String delete(String sessionId, String id)
  {
    return jsonClientRequestIF.delete(sessionId, id);
  }

  /**
   * @see com.runwaysdk.ClientRequest#get(java.lang.String,
   *      java.lang.String)
   */
  public static String get(String sessionId, String id)
  {
    return jsonClientRequestIF.get(sessionId, id);
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
   * @see com.runwaysdk.ClientRequest#grantStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String)
   */
  public static String grantStatePermission(String sessionId, String actorId,
      String stateId, String ... operationNames)
  {
    return jsonClientRequestIF.grantStatePermission(sessionId, actorId, stateId, operationNames);
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
   * @see com.runwaysdk.ClientRequest#grantAttributeStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String,
   *      java.lang.String...)
   */
  public static String grantAttributeStatePermission(String sessionId, String actorId,
      String mdAttributeId, String stateId, String ... operationNames)
  {
    return jsonClientRequestIF.grantAttributeStatePermission(sessionId, actorId, mdAttributeId, stateId, operationNames);
  }

  /**
   * @see com.runwaysdk.ClientRequest#promoteObject(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public static String promoteObject(String sessionId, String businessJSON, String transitionName)
  {
    return jsonClientRequestIF.promoteObject(sessionId, businessJSON, transitionName);
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
   * @see com.runwaysdk.ClientRequest#revokeStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public static String revokeStatePermission(String sessionId, String actorId, String stateId, String ... operationNames)
  {
    return jsonClientRequestIF.revokeStatePermission(sessionId, actorId, stateId, operationNames);
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
   * @see com.runwaysdk.ClientRequest#revokeAttributeStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String,
   *      java.lang.String...)
   */
  public static String revokeAttributeStatePermission(String sessionId, String actorId, String mdAttributeId, String stateId, String ... operationNames)
  {
    return jsonClientRequestIF.revokeAttributeStatePermission(sessionId, actorId, mdAttributeId, stateId, operationNames);
  }

  /**
   * @see com.runwaysdk.ClientRequest#lock(java.lang.String,
   *      java.lang.String)
   */
  public static String lock(String sessionId, String id)
  {
    return jsonClientRequestIF.lock(sessionId, id);
  }

  /**
   * @see com.runwaysdk.ClientRequest#unlock(java.lang.String,
   *      java.lang.String)
   */
  public static String unlock(String sessionId, String id)
  {
    return jsonClientRequestIF.unlock(sessionId, id);
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
  public static String getChildren(String sessionId, String parentId,
      String relationshipType)
  {
    return jsonClientRequestIF.getChildren(sessionId, parentId, relationshipType);
  }

  public static String getChildRelationships(String sessionId, String parentId,
      String relationshipType)
  {
    return jsonClientRequestIF.getChildRelationships(sessionId, parentId, relationshipType);
  }

  public static String getParentRelationships(String sessionId, String childId,
      String relationshipType)
  {
    return jsonClientRequestIF.getParentRelationships(sessionId, childId, relationshipType);
  }

  public static String getParents(String sessionId, String childId,
      String relationshipType)
  {
    return jsonClientRequestIF.getParents(sessionId, childId, relationshipType);
  }

  public static String deleteChildren(String sessionId, String parentId, String relationshipType)
  {
    return jsonClientRequestIF.deleteChildren(sessionId, parentId, relationshipType);
  }

  public static String deleteParents(String sessionId, String childId, String relationshipType)
  {
    return jsonClientRequestIF.deleteParents(sessionId, childId, relationshipType);
  }

  public static String queryBusinesses(String sessionId, String queryJSON)
  {
    return jsonClientRequestIF.queryBusinesses(sessionId, queryJSON);
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

  /**
   * Performs a groovy query for objects.
   */
  public static String groovyObjectQuery(String sessionId, String queryJSON)
  {
    return jsonClientRequestIF.groovyObjectQuery(sessionId, queryJSON);
  }

  /**
   * Performs a groovy query for values.
   */
  public static String groovyValueQuery(String sessionId, String queryJSON)
  {
    return jsonClientRequestIF.groovyValueQuery(sessionId, queryJSON);
  }

  public static String checkAdminScreenAccess(String sessionId)
  {
    return jsonClientRequestIF.checkAdminScreenAccess(sessionId);
  }

}
