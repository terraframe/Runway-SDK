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
package com.runwaysdk.request;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Locale;

public interface JSONRemoteAdapter extends Remote
{
  public String checkAdminScreenAccess(String sessionId) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#addChild(java.lang.String,
   *      java.lang.String, java.lang.String,
   *      com.runwaysdk.transport.RelationshipDTO)
   */
  public String addChild(String sessionId, String parentId, String childId,
      String relationshipType) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#addParent(java.lang.String,
   *      java.lang.String, java.lang.String,
   *      com.runwaysdk.transport.RelationshipDTO)
   */
  public String addParent(String sessionId, String parentId, String childId,
      String relationshipType) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#deleteBusiness(java.lang.String,
   *      java.lang.String)
   */
  public String delete(String sessionId, String id) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#get(java.lang.String,
   *      java.lang.String)
   */
  public String get(String sessionId, String id) throws RemoteException;

  public String getQuery(String sessionId, String type) throws RemoteException;

  public String groovyObjectQuery(String sessionId, String queryDTOJSON) throws RemoteException;

  public String groovyValueQuery(String sessionId, String queryDTOJSON) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#createSessionComponent(java.lang.String,
   *      com.runwaysdk.transport.BusinessDTO)
   */
  public String createSessionComponent(String sessionId, String json) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#createBusiness(java.lang.String,
   *      com.runwaysdk.transport.BusinessDTO)
   */
  public String createBusiness(String sessionId, String businessJSON) throws RemoteException;

  public String createStruct(String sessionId, String structJSON) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#createBusiness(java.lang.String,
   *      com.runwaysdk.transport.BusinessDTO)
   */
  public String createRelationship(String sessionId, String relationshipJSON) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#login(java.lang.String,
   *      java.lang.String, java.lang.String[])
   */
  public String login(String username, String password, String[] stringLocales) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#login(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String[])
   */
  public String login(String username, String password, String dimensionKey, String[] stringLocales) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#setDimension(java.lang.String,
   *      java.lang.String)
   */
  public String setDimension(String sessionId, String dimensionKey) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#loginAnonymous(Locale[])
   */
  public String loginAnonymous(String[] stringLocales) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#loginAnonymous(java.lang.String, Locale[])
   */
  public String loginAnonymous(String dimensionKey, String[] stringLocales) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#changeLoginUser(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public String changeLogin(String sessionId, String username, String password) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#getSessionUser()
   */
  public String getSessionUser(String sessionId) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#logout(java.lang.String)
   */
  public String logout(String sessionId) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#newBusiness(java.lang.String,
   *      java.lang.String)
   */
  public String newBusiness(String sessionId, String type) throws RemoteException;

  public String newStruct(String sessionId, String type) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#update(java.lang.String,
   *      com.runwaysdk.transport.EntityDTO)
   */
  public String update(String sessionId, String entityJSON) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#assignMember(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public String assignMember(String sessionId, String userId, String ... roles) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#removeMember(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public String removeMember(String sessionId, String userId, String ... roles) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#grantTypePermission(java.lang.String,
   *      java.lang.String, java.lang.String, String...)
   */
  public String grantTypePermission(String sessionId, String actorId, String mdTypeId,
      String ... operationId) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#grantMethodPermission(java.lang.String,
   *      java.lang.String, java.lang.String, String...)
   */
  public String grantMethodPermission(String sessionId, String actorId, String mdMethodId,
      String ... operationId) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#grantStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String)
   */
  public String grantStatePermission(String sessionId, String actorId, String stateId,
      String ... operationId) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#grantAttributePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String)
   */
  public String grantAttributePermission(String sessionId, String actorId, String mdAttributeId,
      String ... operationId) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#grantAttributeStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String,
   *      java.lang.String)
   */
  public String grantAttributeStatePermission(String sessionId, String actorId, String mdAttributeId,
      String stateId, String ... operationId) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#promoteObject(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public String promoteObject(String sessionId, String businessJSON, String transitionName) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#revokeTypePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String)
   */
  public String revokeTypePermission(String sessionId, String actorId, String mdTypeId,
      String ... operationIds) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#revokeMethodPermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String)
   */
  public String revokeMethodPermission(String sessionId, String actorId, String mdMethodId,
      String ... operationIds) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#revokeStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String)
   */
  public String revokeStatePermission(String sessionId, String actorId, String stateId,
      String ... operationIds) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#revokeAttributePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String)
   */
  public String revokeAttributePermission(String sessionId, String actorId, String mdAttributeId,
      String ... operationIds) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#revokeAttributeStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String,
   *      java.lang.String)
   */
  public String revokeAttributeStatePermission(String sessionId, String actorId,
      String mdAttributeId, String stateId, String ... operationIds) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#lock(java.lang.String,
   *      java.lang.String)
   */
  public String lock(String sessionId, String id) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#unlock(java.lang.String,
   *      java.lang.String)
   */
  public String unlock(String sessionId, String id) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#deleteChild(java.lang.String,
   *      java.lang.String)
   */
  public String deleteChild(String sessionId, String relationshipId) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#deleteParent(java.lang.String,
   *      java.lang.String)
   */
  public String deleteParent(String sessionId, String relationshipId) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#getChildren(java.lang.String, com.runwaysdk.transport.BusinessDTO, java.lang.String)
   */
  public String getChildren(String sessionId, String id, String relationshipType) throws RemoteException;

  public String getChildRelationships(String sessionId, String id, String relationshipType) throws RemoteException;

  public String getParentRelationships(String sessionId, String id, String relationshipType) throws RemoteException;

  public String getParents(String sessionId, String id, String relationshipType) throws RemoteException;

  public String deleteChildren(String sessionId, String id, String relationshipType) throws RemoteException;

  public String deleteParents(String sessionId, String id, String relationshipType) throws RemoteException;

  public String queryBusinesses(String sessionId, String queryJSON) throws RemoteException;

  public String queryStructs(String sessionId, String queryJSON) throws RemoteException;

  public String queryEntities(String sessionId, String queryJSON) throws RemoteException;

  public String queryRelationships(String sessionId, String queryJSOn) throws RemoteException;

  public String invokeMethod(String sessionId, String metadata, String entityJSON, String parameters) throws RemoteException;

  public String importTypes(String sessionId, String[] types, Boolean typesOnly) throws RemoteException;

  public String getNewestUpdateDate(String sessionId, String[] types, Boolean typesOnly) throws RemoteException;

  public String getEnumeration(String sessionId, String enumType, String enumName) throws RemoteException;

  public String getEnumerations(String sessionId, String enumType, String[] enumNames) throws RemoteException;

  public String getAllEnumerations(String sessionId, String enumType) throws RemoteException;

  public String newMutable(String sessionId, String type) throws RemoteException;

  public String getTermAllChildren(String sessionId, String parentId, Integer pageNum, Integer pageSize) throws RemoteException;
  
  public String cloneBusinessAndCreateRelationship(String sessionId, String cloneDTOid, String newParentId, String newRelationshipType) throws RemoteException;
  
  public String moveBusiness(String sessionId, String newParentId, String childId, String oldRelationshipId, String newRelationshipType) throws RemoteException;
}
