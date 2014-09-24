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

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import com.runwaysdk.constants.FacadeMethods;
import com.runwaysdk.constants.JSONWebServiceAdapterInfo;
import com.runwaysdk.request.WebServiceClientRequestException;
import com.runwaysdk.transport.conversion.ClientConversionFacade;

public class JSONWebServiceClientRequest extends JSONClientRequest
{

  /**
   *
   */
  private static final long serialVersionUID = -2789106138941734169L;

  /**
   * Constructor
   *
   * @param label
   * @param address
   */
  public JSONWebServiceClientRequest(String label, String address)
  {
    super(label, address);
  }

  private Call newCall()
  {
    try
    {
      Service service = new Service();
      Call call = (Call) service.createCall();
      call.setTargetEndpointAddress(new URL(getAddress() + JSONWebServiceAdapterInfo.CLASS));
      return call;
    }
    catch (ServiceException e)
    {
      throw new WebServiceClientRequestException(e);
    }
    catch (MalformedURLException e)
    {
      throw new WebServiceClientRequestException(e);
    }
  }

  public String checkAdminScreenAccess(String sessionId)
  {
    try
    {
      Object[] params = {sessionId};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.CHECK_ADMIN_SCREEN_ACCESS.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }
  
  public String moveBusiness(String sessionId, String newParentId, String childId, String oldRelationshipId, String newRelationshipType)
  {
    try
    {
      Object[] params = {sessionId, newParentId, childId, oldRelationshipId, newRelationshipType};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.MOVE_BUSINESS.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }
  
  public String getTermAllChildren(String sessionId, String parentId, Integer pageNum, Integer pageSize)
  {
    try
    {
      Object[] params = {sessionId, parentId, pageNum, pageSize};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.GET_TERM_ALL_CHILDREN.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String getChildren(String sessionId, String parentId, String relationshipType)
  {
    try
    {
      Object[] params = {sessionId, parentId, relationshipType};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.GET_CHILDREN.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String getParents(String sessionId, String childId, String relationshipType)
  {
    try
    {
      Object[] params = {sessionId, childId, relationshipType};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.GET_PARENTS.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String getChildRelationships(String sessionId, String parentId, String relationshipType)
  {
    try
    {
      Object[] params = {sessionId, parentId, relationshipType};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.GET_CHILD_RELATIONSHIPS.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String getParentRelationships(String sessionId, String childId, String relationshipType)
  {
    try
    {
      Object[] params = {sessionId, childId, relationshipType};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.GET_PARENT_RELATIONSHIPS.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String lock(String sessionId, String id)
  {
    try
    {
      Object[] params = {sessionId, id};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.LOCK.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String unlock(String sessionId, String id)
  {
    try
    {
      Object[] params = {sessionId, id};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.UNLOCK.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String addChild(String sessionId, String parentId, String childId, String relationshipType)
  {
    try
    {
      Object[] params = {sessionId, parentId, childId, relationshipType};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.ADD_CHILD.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String deleteChild(String sessionId, String relationshipId)
  {
    try
    {
      Object[] params = {sessionId, relationshipId};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.DELETE_CHILD.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String addParent(String sessionId, String parentId, String childId, String relationshipType)
  {
    try
    {
      Object[] params = {sessionId, parentId, childId, relationshipType};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.ADD_PARENT.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String deleteParent(String sessionId, String relationshipId)
  {
    try
    {
      Object[] params = {sessionId, relationshipId};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.DELETE_PARENT.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String delete(String sessionId, String id)
  {
    try
    {
      Object[] params = {sessionId, id};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.DELETE.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String get(String sessionId, String id)
  {
    try
    {
      Object[] params = {sessionId, id};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.GET_INSTANCE.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String createSessionComponent(String sessionId, String json)
  {
    try
    {
      Object[] params = {sessionId, json};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.CREATE_SESSION_COMPONENT.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String createBusiness(String sessionId, String businessJSON)
  {
    try
    {
      Object[] params = {sessionId, businessJSON};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.CREATE_BUSINESS.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String createRelationship(String sessionId, String relationshipJSON)
  {
    try
    {
      Object[] params = {sessionId, relationshipJSON};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.CREATE_RELATIONSHIP.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  /**
   * I need comments. ;)
   * @param sessionId
   * @param username
   * @param password
   * @return
   */
  public String login(String username, String password, String[] locales)
  {
    try
    {
      Object[] params = {username, password, locales};

      Call call = newCall();
      return (String) call.invoke(FacadeMethods.LOGIN.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, "", true);
    }
  }


  /**
   * I need comments. ;)
   * @param sessionId
   * @param username
   * @param password
   * @param dimensionKey
   * @param locales
   * @return
   */
  public String login(String username, String password, String dimensionKey, String[] locales)
  {
    try
    {
      Object[] params = {username, password, dimensionKey, locales};

      Call call = newCall();
      return (String) call.invoke(FacadeMethods.LOGIN.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, "", true);
    }
  }

  public String loginAnonymous(String[] locales)
  {
    try
    {
      Object[] params = {locales};

      Call call = newCall();
      return (String) call.invoke(FacadeMethods.LOGIN_ANONYMOUS.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, "", true);
    }
  }

  public String loginAnonymous(String dimensionKey, String[] locales)
  {
    try
    {
      Object[] params = {dimensionKey, locales};

      Call call = newCall();
      return (String) call.invoke(FacadeMethods.LOGIN_ANONYMOUS.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, "", true);
    }
  }


  public String changeLogin(String sessionId, String username, String password)
  {
    try
    {
      Object[] params = {sessionId, username, password};

      Call call = newCall();
      return (String) call.invoke(FacadeMethods.CHANGE_LOGIN.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String setDimension(String sessionId, String dimensionKey)
  {
    try
    {
      Object[] params = {sessionId, dimensionKey};

      Call call = newCall();
      return (String) call.invoke(FacadeMethods.SET_DIMENSION.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String getSessionUser(String sessionId)
  {
    try
    {
      Object[] params = {sessionId};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.GET_SESSION_USER.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String logout(String sessionId)
  {
    try
    {
      Object[] params = {sessionId};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.LOGOUT.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String newBusiness(String sessionId, String type)
  {
    try
    {
      Object[] params = {sessionId, type};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.NEW_BUSINESS.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String update(String sessionId, String entityJSON)
  {
    try
    {
      Object[] params = {sessionId, entityJSON};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.UPDATE.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
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
      Object[] params = {sessionId, userId, roles};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.ASSIGN_MEMBER.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
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
      Object[] params = {sessionId, userId, roles};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.REMOVE_MEMBER.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String grantTypePermission(String sessionId, String actorId, String mdTypeId, String ... operationNames)
  {
    try
    {
      Object[] params = {sessionId, actorId, mdTypeId, operationNames};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.GRANT_TYPE_PERMISSION.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String grantMethodPermission(String sessionId, String actorId, String mdMethodId, String ... operationNames)
  {
    try
    {
      Object[] params = {sessionId, actorId, mdMethodId, operationNames};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.GRANT_METHOD_PERMISSION.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String grantStatePermission(String sessionId, String actorId, String stateId, String ... operationNames)
  {
    try
    {
      Object[] params = {sessionId, actorId, stateId, operationNames};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.GRANT_STATE_PERMISSION.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String grantAttributePermission(String sessionId, String actorId, String mdAttributeId, String ... operationNames)
  {
    try
    {
      Object[] params = {sessionId, actorId, mdAttributeId, operationNames};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.GRANT_ATTRIBUTE_PERMISSION.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String grantAttributeStatePermission(String sessionId, String actorId, String mdAttributeId, String stateId, String ... operationNames)
  {
    try
    {
      Object[] params = {sessionId, actorId, mdAttributeId, stateId, operationNames};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.GRANT_ATTRIBUTE_STATE_PERMISSION.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String promoteObject(String sessionId, String businessDTO, String transitionName)
  {
    try
    {
      Object[] params = {sessionId, businessDTO, transitionName};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.PROMOTE_OBJECT.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String revokeTypePermission(String sessionId, String actorId, String mdTypeId, String ... operationNames)
  {
    try
    {
      Object[] params = {sessionId, actorId, mdTypeId, operationNames};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.REVOKE_TYPE_PERMISSION.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String revokeMethodPermission(String sessionId, String actorId, String mdMethodId, String ... operationNames)
  {
    try
    {
      Object[] params = {sessionId, actorId, mdMethodId, operationNames};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.REVOKE_METHOD_PERMISSION.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String revokeStatePermission(String sessionId, String actorId, String stateId, String ... operationNames)
  {
    try
    {
      Object[] params = {sessionId, actorId, stateId, operationNames};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.REVOKE_STATE_PERMISSION.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String revokeAttributePermission(String sessionId, String actorId, String mdAttributeId, String ... operationNames)
  {
    try
    {
      Object[] params = {sessionId, actorId, mdAttributeId, operationNames};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.REVOKE_ATTRIBUTE_PERMISSION.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String revokeAttributeStatePermission(String sessionId, String actorId, String mdAttributeId, String stateId, String ... operationNames)
  {
    try
    {
      Object[] params = {sessionId, actorId, mdAttributeId, stateId, operationNames};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.REVOKE_ATTRIBUTE_STATE_PERMISSION.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String queryBusinesses(String sessionId, String queryJSON)
  {
    try
    {
      Object[] params = {sessionId, queryJSON};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.QUERY_BUSINESSES.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String queryViews(String sessionId, String queryJSON)
  {
    try
    {
      Object[] params = {sessionId, queryJSON};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.QUERY_VIEWS.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }
  
  public String queryStructs(String sessionId, String queryJSON)
  {
    try
    {
      Object[] params = {sessionId, queryJSON};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.QUERY_STRUCTS.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String queryEntities(String sessionId, String queryJSON)
  {
    try
    {
      Object[] params = {sessionId, queryJSON};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.QUERY_ENTITIES.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String queryRelationships(String sessionId, String queryJSON)
  {
    try
    {
      Object[] params = {sessionId, queryJSON};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.QUERY_RELATIONSHIPS.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String deleteChildren(String sessionId, String id, String relationshipType)
  {
    try
    {
      Object[] params = {sessionId, id, relationshipType};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.DELETE_CHILDREN.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String deleteParents(String sessionId, String id, String relationshipType)
  {
    try
    {
      Object[] params = {sessionId, id, relationshipType};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.DELETE_PARENTS.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String invokeMethod(String sessionId, String metadata, String entityJSON, String parameters)
  {
    try
    {
      Object[] params = {sessionId, metadata, entityJSON, parameters};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.INVOKE_METHOD.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String importTypes(String sessionId, String[] types, Boolean typesOnly)
  {
    try
    {
      Object[] params = {sessionId, types, typesOnly};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.IMPORT_TYPES.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String getNewestUpdateDate(String sessionId, String[] types, Boolean typesOnly)
  {
    try
    {
      Object[] params = {sessionId, types, typesOnly};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.GET_NEWEST_UPDATED_DATE.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String getEnumeration(String sessionId, String enumType, String enumName)
  {
    try
    {
      Object[] params = {sessionId, enumType, enumName};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.GET_ENUMERATION.getName(), params);
    }
    catch(RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String getEnumerations(String sessionId, String enumType, String[] enumName)
  {
    try
    {
      Object[] params = {sessionId, enumType, enumName};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.GET_ENUMERATIONS.getName(), params);
    }
    catch(RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String getAllEnumerations(String sessionId, String enumType)
  {
    try
    {
      Object[] params = {sessionId, enumType};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.GET_ALL_ENUMERATIONS.getName(), params);
    }
    catch(RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String createStruct(String sessionId, String structJSON)
  {
    try
    {
      Object[] params = {sessionId, structJSON};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.CREATE_STRUCT.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String newStruct(String sessionId, String type)
  {
    try
    {
      Object[] params = {sessionId, type};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.NEW_STRUCT.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String newMutable(String sessionId, String type)
  {
    try
    {
      Object[] params = {sessionId, type};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.NEW_MUTABLE.getName(), params);
    }
    catch (RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String getQuery(String sessionId, String type)
  {
    try
    {
      Object[] params = {sessionId, type};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.GET_QUERY.getName(), params);
    }
    catch(RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String groovyObjectQuery(String sessionId, String queryDTOJSON)
  {
    try
    {
      Object[] params = {sessionId, queryDTOJSON};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.GROOVY_OBJECT_QUERY.getName(), params);
    }
    catch(RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

  public String groovyValueQuery(String sessionId, String queryDTOJSON)
  {
    try
    {
      Object[] params = {sessionId, queryDTOJSON};
      Call call = newCall();
      return (String) call.invoke(FacadeMethods.GROOVY_VALUE_QUERY.getName(), params);
    }
    catch(RemoteException e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }

}
