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

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.request.JSONRemoteAdapter;
import com.runwaysdk.request.RMIClientException;
import com.runwaysdk.transport.conversion.ClientConversionFacade;

public class JSONRMIClientRequest extends JSONClientRequest
{
  /**
   *
   */
  private static final long serialVersionUID = 7157122665755164046L;
  /**
   * RemoteController interface object to id server-side RMI
   * implementation.
   */
  private JSONRemoteAdapter rmiAdapter = null;

  /**
   * Constructor
   *
   * @param label
   * @param address
   */
  public JSONRMIClientRequest(String label, String address)
  {
    super(label, address);

    try
    {
      // Connect to the server and use the interface
      rmiAdapter = (JSONRemoteAdapter) Naming.lookup(getAddress() + CommonProperties.getJSONRMIService());
    }
    catch (MalformedURLException e)
    {
      throw new RMIClientException(e);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    catch (NotBoundException e)
    {
      throw new RMIClientException(e);
    }
  }

  /**
   * Unbind the current RMIClientRequest.
   * After this is called, this RMIClientRequest cannot be used again.
   */
  public void unbindRMIClientRequest()
  {
    try
    {
      Naming.unbind(getAddress() + CommonProperties.getJSONRMIService());
    }
    catch (MalformedURLException e)
    {
      throw new RMIClientException(e);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
    catch (NotBoundException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String checkAdminScreenAccess(String sessionId)
  {
    try
    {
      return rmiAdapter.checkAdminScreenAccess(sessionId);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }
  
  public String moveBusiness(String sessionId, String newParentId, String childId, String oldRelationshipId, String newRelationshipType)
  {
    try
    {
      return rmiAdapter.moveBusiness(sessionId, newParentId, childId, oldRelationshipId, newRelationshipType);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }
  
  public String getTermAllChildren(String sessionId, String parentId, Integer pageNum, Integer pageSize)
  {
    try
    {
      return rmiAdapter.getTermAllChildren(sessionId, parentId, pageNum, pageSize);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String getChildren(String sessionId, String parentId, String relationshipType)
  {
    try
    {
      return rmiAdapter.getChildren(sessionId, parentId, relationshipType);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String getParents(String sessionId, String childId, String relationshipType)
  {
    try
    {
      return rmiAdapter.getParents(sessionId, childId, relationshipType);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String getChildRelationships(String sessionId, String parentId, String relationshipType)
  {
    try
    {
      return rmiAdapter.getChildRelationships(sessionId, parentId, relationshipType);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String getParentRelationships(String sessionId, String childId, String relationshipType)
  {
    try
    {
      return rmiAdapter.getParentRelationships(sessionId, childId, relationshipType);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String lock(String sessionId, String id)
  {
    try
    {
      return rmiAdapter.lock(sessionId, id);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String unlock(String sessionId, String id)
  {
    try
    {
       return rmiAdapter.unlock(sessionId, id);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String addChild(String sessionId, String parentId, String childId, String relationshipType)
  {
    try
    {
      return rmiAdapter.addChild(sessionId, parentId, childId, relationshipType);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String deleteChild(String sessionId, String relationshipId)
  {
    try
    {
      return rmiAdapter.deleteChild(sessionId, relationshipId);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String addParent(String sessionId, String parentId, String childId, String relationshipType)
  {
    try
    {
      return rmiAdapter.addParent(sessionId, parentId, childId, relationshipType);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String deleteParent(String sessionId, String relationshipId)
  {
    try
    {
      return rmiAdapter.deleteParent(sessionId, relationshipId);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String delete(String sessionId, String id)
  {
    try
    {
      return rmiAdapter.delete(sessionId, id);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String get(String sessionId, String id)
  {
    try
    {
      return rmiAdapter.get(sessionId, id);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String createSessionComponent(String sessionId, String json)
  {
    try
    {
      return rmiAdapter.createSessionComponent(sessionId, json);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String createBusiness(String sessionId, String businessJSON)
  {
    try
    {
      return rmiAdapter.createBusiness(sessionId, businessJSON);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String createRelationship(String sessionId, String relationshipJSON)
  {
    try
    {
      return rmiAdapter.createRelationship(sessionId, relationshipJSON);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  /**
   * I need comments.
   */
  public String login(String username, String password, String[] locales)
  {
    try
    {
      return rmiAdapter.login(username, password, locales);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, "", false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  /**
   * I need comments.
   */
  public String login(String username, String password, String dimensionKey, String[] locales)
  {
    try
    {
      return rmiAdapter.login(username, password, dimensionKey, locales);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, "", false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  /**
   * I need comments.
   */
  public String setDimension(String sessionId, String dimensionKey)
  {
    try
    {
      return rmiAdapter.setDimension(sessionId, dimensionKey);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, "", false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String loginAnonymous(String[] locales)
  {
    try
    {
      return rmiAdapter.loginAnonymous(locales);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, "", false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String loginAnonymous(String dimensionKey, String[] locales)
  {
    try
    {
      return rmiAdapter.loginAnonymous(dimensionKey, locales);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, "", false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String changeLogin(String sessionId, String username, String password)
  {
    try
    {
      return rmiAdapter.changeLogin(sessionId, username, password);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String getSessionUser(String sessionId)
  {
    try
    {
      return rmiAdapter.getSessionUser(sessionId);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String logout(String sessionId)
  {
    try
    {
      return rmiAdapter.logout(sessionId);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String newBusiness(String sessionId, String type)
  {
    try
    {
      return rmiAdapter.newBusiness(sessionId, type);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String update(String sessionId, String entityJSON)
  {
    try
    {
      return rmiAdapter.update(sessionId, entityJSON);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  /**
   * @see com.runwaysdk.ClientRequest#assignMember(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public String assignMember(String sessionId, String userId, String ... roles)
  {
    try
    {
      return rmiAdapter.assignMember(sessionId, userId, roles);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  /**
   * @see com.runwaysdk.ClientRequest#removeMember(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public String removeMember(String sessionId, String userId, String ... roles)
  {
    try
    {
      return rmiAdapter.removeMember(sessionId, userId, roles);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String grantTypePermission(String sessionId, String actorId, String mdTypeId, String ... operationNames)
  {
    try
    {
      return rmiAdapter.grantTypePermission(sessionId, actorId, mdTypeId, operationNames);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String grantMethodPermission(String sessionId, String actorId, String mdMethodId, String ... operationNames)
  {
    try
    {
      return rmiAdapter.grantMethodPermission(sessionId, actorId, mdMethodId, operationNames);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String grantStatePermission(String sessionId, String actorId, String stateId, String ... operationNames)
  {
    try
    {
      return rmiAdapter.grantStatePermission(sessionId, actorId, stateId, operationNames);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String grantAttributePermission(String sessionId, String actorId, String mdAttributeId, String ... operationNames)
  {
    try
    {
      return rmiAdapter.grantAttributePermission(sessionId, actorId, mdAttributeId, operationNames);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String grantAttributeStatePermission(String sessionId, String actorId, String mdAttributeId, String stateId, String ... operationNames)
  {
    try
    {
      return rmiAdapter.grantAttributeStatePermission(sessionId, actorId, mdAttributeId, stateId, operationNames);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String promoteObject(String sessionId, String businessJSON, String transitionName)
  {
    try
    {
      return rmiAdapter.promoteObject(sessionId, businessJSON, transitionName);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String revokeTypePermission(String sessionId, String actorId, String mdTypeId, String ... operationNames)
  {
    try
    {
      return rmiAdapter.revokeTypePermission(sessionId, actorId, mdTypeId, operationNames);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String revokeMethodPermission(String sessionId, String actorId, String mdMethodId, String ... operationNames)
  {
    try
    {
      return rmiAdapter.revokeMethodPermission(sessionId, actorId, mdMethodId, operationNames);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String revokeStatePermission(String sessionId, String actorId, String stateId, String ... operationNames)
  {
    try
    {
      return rmiAdapter.revokeStatePermission(sessionId, actorId, stateId, operationNames);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String revokeAttributePermission(String sessionId, String actorId, String mdAttributeId, String ... operationNames)
  {
    try
    {
      return rmiAdapter.revokeAttributePermission(sessionId, actorId, mdAttributeId, operationNames);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String revokeAttributeStatePermission(String sessionId, String actorId, String mdAttributeId, String stateId, String ... operationNames)
  {
    try
    {
      return rmiAdapter.revokeAttributeStatePermission(sessionId, actorId, mdAttributeId, stateId, operationNames);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String queryBusinesses(String sessionId, String queryJSON)
  {
    try
    {
      return rmiAdapter.queryBusinesses(sessionId, queryJSON);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String queryViews(String sessionId, String queryJSON)
  {
    try
    {
      return rmiAdapter.queryViews(sessionId, queryJSON);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }
  
  public String queryStructs(String sessionId, String queryJSON)
  {
    try
    {
      return rmiAdapter.queryStructs(sessionId, queryJSON);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String queryEntities(String sessionId, String queryJSON)
  {
    try
    {
      return rmiAdapter.queryEntities(sessionId, queryJSON);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String queryRelationships(String sessionId, String queryJSON)
  {
    try
    {
      return rmiAdapter.queryRelationships(sessionId, queryJSON);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String deleteChildren(String sessionId, String parentId, String relationshipType)
  {
    try
    {
      return rmiAdapter.deleteChildren(sessionId, parentId, relationshipType);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String deleteParents(String sessionId, String childId, String relationshipType)
  {
    try
    {
      return rmiAdapter.deleteParents(sessionId, childId, relationshipType);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String invokeMethod(String sessionId, String metadata, String entityJSON, String parameters)
  {
    try
    {
      return rmiAdapter.invokeMethod(sessionId, metadata, entityJSON, parameters);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch(RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String importTypes(String sessionId, String[] types, Boolean typesOnly)
  {
    try
    {
      return rmiAdapter.importTypes(sessionId, types, typesOnly);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch(RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String getNewestUpdateDate(String sessionId, String[] types, Boolean typesOnly)
  {
    try
    {
      return rmiAdapter.getNewestUpdateDate(sessionId, types, typesOnly);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch(RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String getEnumeration(String sessionId, String enumType, String enumName)
  {
    try
    {
      return rmiAdapter.getEnumeration(sessionId, enumType, enumName);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch(RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String getEnumerations(String sessionId, String enumType, String[] enumNames)
  {
    try
    {
      return rmiAdapter.getEnumerations(sessionId, enumType, enumNames);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch(RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String getAllEnumerations(String sessionId, String enumType)
  {
    try
    {
      return rmiAdapter.getAllEnumerations(sessionId, enumType);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch(RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String createStruct(String sessionId, String structJSON)
  {
    try
    {
      return rmiAdapter.createStruct(sessionId, structJSON);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String newStruct(String sessionId, String type)
  {
    try
    {
      return rmiAdapter.newStruct(sessionId, type);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String newMutable(String sessionId, String type)
  {
    try
    {
      return rmiAdapter.newMutable(sessionId, type);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch (RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }

  public String getQuery(String sessionId, String type)
  {
    try
    {
      return rmiAdapter.getQuery(sessionId, type);
    }
    catch (RuntimeException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch(RemoteException e)
    {
      throw new RMIClientException(e);
    }
  }
}
