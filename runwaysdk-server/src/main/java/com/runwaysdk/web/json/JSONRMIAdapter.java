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
package com.runwaysdk.web.json;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.Locale;

import com.runwaysdk.request.JSONRemoteAdapter;
import com.runwaysdk.session.Request;

public class JSONRMIAdapter extends UnicastRemoteObject implements JSONRemoteAdapter
{
  /**
   * auto-generated id
   */
  private static final long serialVersionUID = 2267386320233346121L;
 
  /**
   * Constructor.
   *
   * @throws RemoteException
   */
  public JSONRMIAdapter() throws RemoteException
  {

  }

  /**
   * @param port
   * @param csf
   * @param ssf
   * @throws RemoteException
   */
  public JSONRMIAdapter(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException
  {
    super(port, csf, ssf);
  }

  /**
   * @see com.runwaysdk.facade.Facade#moveBusiness(String sessionId, String newParentId, String childId, String oldRelationshipId, String newRelationshipType)
   */
  public String moveBusiness(String sessionId, String newParentId, String childId, String oldRelationshipId, String newRelationshipType)
  {
    return JSONAdapterDelegate.moveBusiness(sessionId, newParentId, childId, oldRelationshipId, newRelationshipType);
  }

  /**
   * @see com.runwaysdk.facade.Facade#getTermAllChildren(java.lang.String sessionId, java.lang.String parentId, java.lang.Integer pageNum, java.lang.Integer pageSize)
   */
  public String getTermAllChildren(String sessionId, String parentId, Integer pageNum, Integer pageSize)
  {
    return JSONAdapterDelegate.getTermAllChildren(sessionId, parentId, pageNum, pageSize);
  }

  public String checkAdminScreenAccess(String sessionId)
  {
    return JSONAdapterDelegate.checkAdminScreenAccess(sessionId);
  }

  /**
   * @see com.runwaysdk.ClientRequest#addChild(java.lang.String, java.lang.String, java.lang.String, com.runwaysdk.business.RelationshipDTO)
   */
  public String addChild(String sessionId, String parentId, String childId, String relationshipType)
  {
    return JSONAdapterDelegate.addChild(sessionId, parentId, childId, relationshipType);
  }

  /**
   * @see com.runwaysdk.ClientRequest#addParent(java.lang.String, java.lang.String, java.lang.String, com.runwaysdk.business.RelationshipDTO)
   */
  public String addParent(String sessionId, String parentId, String childId, String relationshipType)
  {
    return JSONAdapterDelegate.addParent(sessionId, parentId, childId, relationshipType);
  }

  /**
   * @see com.runwaysdk.ClientRequest#delete(java.lang.String, java.lang.String)
   */
  public String delete(String sessionId, String id)
  {
    return JSONAdapterDelegate.delete(sessionId, id);
  }

  /**
   * @see com.runwaysdk.ClientRequest#get(java.lang.String, java.lang.String)
   */
  public String get(String sessionId, String id)
  {
    return JSONAdapterDelegate.get(sessionId, id);
  }

  public String getQuery(String sessionId, String type)
  {
    return JSONAdapterDelegate.getQuery(sessionId, type);
  }

  public String groovyObjectQuery(String sessionId, String queryDTOJSON)
  {
    return JSONAdapterDelegate.groovyObjectQuery(sessionId, queryDTOJSON);
  }

  public String groovyValueQuery(String sessionId, String queryDTOJSON)
  {
    return JSONAdapterDelegate.groovyValueQuery(sessionId, queryDTOJSON);
  }

  /**
   * @see com.runwaysdk.ClientRequest#createSessionComponent(java.lang.String, com.runwaysdk.transport.Session)
   */
  public String createSessionComponent(String sessionId, String json)
  {
    return JSONAdapterDelegate.createSessionComponent(sessionId, json);
  }

  /**
   * @see com.runwaysdk.ClientRequest#createBusiness(java.lang.String, com.runwaysdk.business.BusinessDTO)
   */
  public String createBusiness(String sessionId, String businessJSON)
  {
    return JSONAdapterDelegate.createBusiness(sessionId, businessJSON);
  }

  public String createRelationship(String sessionId, String relationshipJSON)
  {
    return JSONAdapterDelegate.createRelationship(sessionId, relationshipJSON);
  }

  /**
   * @see com.runwaysdk.ClientRequest#login(java.lang.String, java.lang.String, java.lang.String[])
   */
  public String login(String username, String password, String[] stringLocales)
  {
    return JSONAdapterDelegate.login(username, password, stringLocales);
  }

  /**
   * @see com.runwaysdk.ClientRequest#login(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])
   */
  public String login(String username, String password, String dimensionKey, String[] stringLocales)
  {
    return JSONAdapterDelegate.login(username, password, dimensionKey, stringLocales);
  }

  /**
   * @see com.runwaysdk.ClientRequest#setDimension(java.lang.String, java.lang.String)
   */
  public String setDimension(String sessionId, String dimensionKey)
  {
    return JSONAdapterDelegate.setDimension(sessionId, dimensionKey);
  }

  /**
   * @see com.runwaysdk.ClientRequest#loginAnonymous(Locale[])
   */
  public String loginAnonymous(String[] stringLocales)
  {
    return JSONAdapterDelegate.loginAnonymous(stringLocales);
  }

  /**
   * @see com.runwaysdk.ClientRequest#loginAnonymous(java.lang.String, Locale[])
   */
  public String loginAnonymous(String dimensionKey, String[] stringLocales)
  {
    return JSONAdapterDelegate.loginAnonymous(dimensionKey, stringLocales);
  }

  /**
   * Changes the user for the given session.
   *
   * @param sessionId
   *          id of a session.
   * @param username
   *          The name of the user.
   * @param password
   *          The password of the user.
   */
  @Request
  public String changeLogin(String sessionId, String username, String password)
  {
    return JSONAdapterDelegate.changeLogin(sessionId, username, password);
  }

  /**
   * @see com.runwaysdk.ClientRequest#getSessionUser()
   */
  public String getSessionUser(String sessionId)
  {
    return JSONAdapterDelegate.getSessionUser(sessionId);
  }

  /**
   * @see com.runwaysdk.ClientRequest#logout(java.lang.String)
   */
  public String logout(String sessionId)
  {
    return JSONAdapterDelegate.logout(sessionId);
  }

  /**
   * @see com.runwaysdk.ClientRequest#newBusiness(java.lang.String, java.lang.String)
   */
  public String newBusiness(String sessionId, String type)
  {
    return JSONAdapterDelegate.newBusiness(sessionId, type);
  }

  /**
   * @see com.runwaysdk.ClientRequest#update(java.lang.String, java.lang.String)
   */
  public String update(String sessionId, String json)
  {
    return JSONAdapterDelegate.update(sessionId, json);
  }

  /**
   * @see com.runwaysdk.ClientRequest#assignMember(java.lang.String, java.lang.String, java.lang.String, java.lang.String...)
   */
  public String assignMember(String sessionId, String userId, String... roles)
  {
    return JSONAdapterDelegate.assignMember(sessionId, userId, roles);
  }

  /**
   * @see com.runwaysdk.ClientRequest#removeMember(java.lang.String, java.lang.String, java.lang.String, java.lang.String...)
   */
  public String removeMember(String sessionId, String userId, String... roles)
  {
    return JSONAdapterDelegate.removeMember(sessionId, userId, roles);
  }

  /**
   * @see com.runwaysdk.ClientRequest#grantTypePermission(java.lang.String, java.lang.String, java.lang.String, String...)
   */
  public String grantTypePermission(String sessionId, String actorId, String mdTypeId, String... operationNames)
  {
    return JSONAdapterDelegate.grantTypePermission(sessionId, actorId, mdTypeId, operationNames);
  }

  /**
   * @see com.runwaysdk.ClientRequest#grantMethodPermission(java.lang.String, java.lang.String, java.lang.String, String...)
   */
  public String grantMethodPermission(String sessionId, String actorId, String mdTypeId, String... operationNames)
  {
    return JSONAdapterDelegate.grantMethodPermission(sessionId, actorId, mdTypeId, operationNames);
  }

  /**
   * @see com.runwaysdk.ClientRequest#grantStatePermission(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
   */
  public String grantStatePermission(String sessionId, String actorId, String stateId, String... operationNames)
  {
    return JSONAdapterDelegate.grantStatePermission(sessionId, actorId, stateId, operationNames);
  }

  /**
   * @see com.runwaysdk.ClientRequest#grantAttributePermission(java.lang.String, java.lang.String, java.lang.String, java.lang.String...)
   */
  public String grantAttributePermission(String sessionId, String actorId, String mdAttributeId, String... operationNames)
  {
    return JSONAdapterDelegate.grantAttributePermission(sessionId, actorId, mdAttributeId, operationNames);
  }

  /**
   * @see com.runwaysdk.ClientRequest#grantAttributeStatePermission(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String...)
   */
  public String grantAttributeStatePermission(String sessionId, String actorId, String mdAttributeId, String stateId, String... operationNames)
  {
    return JSONAdapterDelegate.grantAttributeStatePermission(sessionId, actorId, mdAttributeId, stateId, operationNames);
  }

  /**
   * @see com.runwaysdk.ClientRequest#promoteObject(java.lang.String, java.lang.String, java.lang.String)
   */
  public String promoteObject(String sessionId, String businessJSON, String transitionName)
  {
    return JSONAdapterDelegate.promoteObject(sessionId, businessJSON, transitionName);
  }

  /**
   * @see com.runwaysdk.ClientRequest#revokeTypePermission(java.lang.String, java.lang.String, java.lang.String, java.lang.String...)
   */
  public String revokeTypePermission(String sessionId, String actorId, String mdTypeId, String... operationNames)
  {
    return JSONAdapterDelegate.revokeTypePermission(sessionId, actorId, mdTypeId, operationNames);
  }

  /**
   * @see com.runwaysdk.ClientRequest#revokeMethodPermission(java.lang.String, java.lang.String, java.lang.String, java.lang.String...)
   */
  public String revokeMethodPermission(String sessionId, String actorId, String mdMethodId, String... operationNames)
  {
    return JSONAdapterDelegate.revokeMethodPermission(sessionId, actorId, mdMethodId, operationNames);
  }

  /**
   * @see com.runwaysdk.ClientRequest#revokeStatePermission(java.lang.String, java.lang.String, java.lang.String, java.lang.String...)
   */
  public String revokeStatePermission(String sessionId, String actorId, String stateId, String... operationNames)
  {
    return JSONAdapterDelegate.revokeStatePermission(sessionId, actorId, stateId, operationNames);
  }

  /**
   * @see com.runwaysdk.ClientRequest#revokeAttributePermission(java.lang.String, java.lang.String, java.lang.String, java.lang.String...)
   */
  public String revokeAttributePermission(String sessionId, String actorId, String mdAttributeId, String... operationNames)
  {
    return JSONAdapterDelegate.revokeAttributePermission(sessionId, actorId, mdAttributeId, operationNames);
  }

  /**
   * @see com.runwaysdk.ClientRequest#revokeAttributeStatePermission(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
   */
  public String revokeAttributeStatePermission(String sessionId, String actorId, String mdAttributeId, String stateId, String... operationNames)
  {
    return JSONAdapterDelegate.revokeAttributeStatePermission(sessionId, actorId, mdAttributeId, stateId, operationNames);
  }

  /**
   * @see com.runwaysdk.ClientRequest#lock(java.lang.String, java.lang.String)
   */
  public String lock(String sessionId, String id)
  {
    return JSONAdapterDelegate.lock(sessionId, id);
  }

  /**
   * @see com.runwaysdk.ClientRequest#unlock(java.lang.String, java.lang.String)
   */
  public String unlock(String sessionId, String id)
  {
    return JSONAdapterDelegate.unlock(sessionId, id);
  }

  /**
   * @see com.runwaysdk.ClientRequest#deleteChild(java.lang.String, java.lang.String)
   */
  public String deleteChild(String sessionId, String relationshipId)
  {
    return JSONAdapterDelegate.deleteChild(sessionId, relationshipId);
  }

  /**
   * @see com.runwaysdk.ClientRequest#deleteParent(java.lang.String, java.lang.String)
   */
  public String deleteParent(String sessionId, String relationshipId)
  {
    return JSONAdapterDelegate.deleteParent(sessionId, relationshipId);
  }

  /**
   * @see com.runwaysdk.ClientRequest#getChildren(java.lang.String, com.runwaysdk.business.BusinessDTO, java.lang.String)
   */

  public String getChildren(String sessionId, String id, String relationshipType)
  {
    return JSONAdapterDelegate.getChildren(sessionId, id, relationshipType);
  }

  public String getChildRelationships(String sessionId, String id, String relationshipType)
  {
    return JSONAdapterDelegate.getChildRelationships(sessionId, id, relationshipType);
  }

  public String getParentRelationships(String sessionId, String id, String relationshipType)
  {
    return JSONAdapterDelegate.getParentRelationships(sessionId, id, relationshipType);
  }

  public String getParents(String sessionId, String id, String relationshipType)
  {
    return JSONAdapterDelegate.getParents(sessionId, id, relationshipType);
  }

  public String deleteChildren(String sessionId, String id, String relationshipType)
  {
    return JSONAdapterDelegate.deleteChildren(sessionId, id, relationshipType);
  }

  public String deleteParents(String sessionId, String id, String relationshipType)
  {
    return JSONAdapterDelegate.deleteParents(sessionId, id, relationshipType);
  }

  public String queryBusinesses(String sessionId, String queryJSON)
  {
    return JSONAdapterDelegate.queryBusinesses(sessionId, queryJSON);
  }

  public String queryViews(String sessionId, String queryJSON)
  {
    return JSONAdapterDelegate.queryViews(sessionId, queryJSON);
  }

  public String queryStructs(String sessionId, String queryJSON)
  {
    return JSONAdapterDelegate.queryStructs(sessionId, queryJSON);
  }

  public String queryEntities(String sessionId, String queryJSON)
  {
    return JSONAdapterDelegate.queryEntities(sessionId, queryJSON);
  }

  public String queryRelationships(String sessionId, String queryJSON)
  {
    return JSONAdapterDelegate.queryRelationships(sessionId, queryJSON);
  }

  public String invokeMethod(String sessionId, String metadata, String entityJSON, String parameters)
  {
    return JSONAdapterDelegate.invokeMethod(sessionId, metadata, entityJSON, parameters);
  }

  public String importTypes(String sessionId, String[] types, Boolean typesOnly)
  {
    return JSONAdapterDelegate.importTypes(sessionId, types, typesOnly);
  }

  public String getNewestUpdateDate(String sessionId, String[] types, Boolean typesOnly)
  {
    return JSONAdapterDelegate.getNewestUpdateDate(sessionId, types, typesOnly);
  }

  public String getEnumeration(String sessionId, String enumType, String enumName)
  {
    return JSONAdapterDelegate.getEnumeration(sessionId, enumType, enumName);
  }

  public String getEnumerations(String sessionId, String enumType, String[] enumNames)
  {
    return JSONAdapterDelegate.getEnumerations(sessionId, enumType, enumNames);
  }

  public String getAllEnumerations(String sessionId, String enumType)
  {
    return JSONAdapterDelegate.getAllEnumerations(sessionId, enumType);
  }

  public String createStruct(String sessionId, String structJSON)
  {
    return JSONAdapterDelegate.createStruct(sessionId, structJSON);
  }

  public String newStruct(String sessionId, String type)
  {
    return JSONAdapterDelegate.newStruct(sessionId, type);
  }

  public String newMutable(String sessionId, String type)
  {
    return JSONAdapterDelegate.newMutable(sessionId, type);
  }
}
