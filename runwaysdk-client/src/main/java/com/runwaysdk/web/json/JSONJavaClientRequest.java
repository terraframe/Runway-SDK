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

import com.runwaysdk.constants.AdapterInfo;
import com.runwaysdk.constants.FacadeMethods;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.transport.conversion.ClientConversionFacade;

/**
 * Java clientRequest class for use with JSON. JSONController delegates to this class.
 */
public class JSONJavaClientRequest extends JSONClientRequest
{
  /**
   *
   */
  private static final long serialVersionUID = -4693986245497915843L;

  public JSONJavaClientRequest(String label, String address)
  {
    super(label, address);
  }
  
  public String moveBusiness(String sessionId, String newParentId, String childId, String oldRelationshipId, String newRelationshipType)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod(FacadeMethods.MOVE_BUSINESS.getName(), String.class, String.class, String.class, String.class, String.class).
        invoke(null, sessionId, newParentId, childId, oldRelationshipId, newRelationshipType);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }
  
  public String getTermAllChildren(String sessionId, String parentId, Integer pageNum, Integer pageSize)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);
    
    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod(FacadeMethods.GET_TERM_ALL_CHILDREN.getName(), String.class, String.class, Integer.class, Integer.class).
        invoke(null, sessionId, parentId, pageNum, pageSize);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String getChildren(String sessionId, String parentId, String relationshipType)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("getChildren", String.class, String.class, String.class).
        invoke(null, sessionId, parentId, relationshipType);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String getParents(String sessionId, String childId, String relationshipType)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("getParents", String.class, String.class, String.class).
        invoke(null, sessionId, childId, relationshipType);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String getChildRelationships(String sessionId, String parentId, String relationshipType)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("getChildRelationships", String.class, String.class, String.class).
        invoke(null, sessionId, parentId, relationshipType);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String getParentRelationships(String sessionId, String childId, String relationshipType)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("getParentRelationships", String.class, String.class, String.class).
        invoke(null, sessionId, childId, relationshipType);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String lock(String sessionId, String id)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("lock", String.class, String.class).
        invoke(null, sessionId, id);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String unlock(String sessionId, String id)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("unlock", String.class, String.class).
        invoke(null, sessionId, id);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String addChild(String sessionId, String parentId, String childId, String relationshipType)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("addChild", String.class, String.class, String.class, String.class).
        invoke(null, sessionId, parentId, childId, relationshipType);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String deleteChild(String sessionId, String relationshipId)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("deleteChild", String.class, String.class).
        invoke(null, sessionId, relationshipId);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String addParent(String sessionId, String parentId, String childId, String relationshipType)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("addParent", String.class, String.class, String.class, String.class).
        invoke(null, sessionId, parentId, childId, relationshipType);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String deleteParent(String sessionId, String relationshipId)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("deleteParent", String.class, String.class).
        invoke(null, sessionId, relationshipId);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String delete(String sessionId, String id)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("delete", String.class, String.class).
        invoke(null, sessionId, id);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String get(String sessionId, String id)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("get", String.class, String.class).
        invoke(null, sessionId, id);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String createSessionComponent(String sessionId, String json)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("createSessionComponent", String.class, String.class).
        invoke(null, sessionId, json);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String createBusiness(String sessionId, String businessJSON)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("createBusiness", String.class, String.class).
        invoke(null, sessionId, businessJSON);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String createRelationship(String sessionId, String relationshipJSON)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("createRelationship", String.class, String.class).
        invoke(null, sessionId, relationshipJSON);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String login(String username, String password, String[] locales)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("login", String.class, String.class, String[].class).
        invoke(null, username, password, locales);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, "", false);
    }

    return returnJson;
  }

  public String login(String username, String password, String dimensionKey, String[] locales)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("login", String.class, String.class, String.class, String[].class).
        invoke(null, username, password, dimensionKey, locales);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, "", false);
    }

    return returnJson;
  }

  public String setDimension(String sessionId, String dimensionKey)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("setDimension", String.class, String.class).
        invoke(null, sessionId, dimensionKey);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, "", false);
    }

    return returnJson;
  }

  public String loginAnonymous(String[] locales)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("loginAnonymous", String[].class).
        invoke(null, (Object)locales);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, "", false);
    }

    return returnJson;
  }

  public String loginAnonymous(String dimensionKey, String[] locales)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("loginAnonymous", String[].class).
        invoke(null, dimensionKey, (Object)locales);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, "", false);
    }

    return returnJson;
  }

  public String changeLogin(String sessionId, String username, String password)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("changeLogin", String.class, String.class, String.class).
        invoke(null, sessionId, username, password);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String getSessionUser(String sessionId)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("getSessionUser", String.class).
        invoke(null, sessionId);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String logout(String sessionId)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("logout", String.class).
        invoke(null, sessionId);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String newBusiness(String sessionId, String type)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("newBusiness", String.class, String.class ).
        invoke(null, sessionId, type);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String update(String sessionId, String entityJSON)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("update", String.class, String.class).
        invoke(null, sessionId, entityJSON);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  /**
   * @see com.runwaysdk.ClientRequest#assignMember(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public String assignMember(String sessionId, String userId, String ... roles)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("assignMember", String.class, String.class, String[].class).
        invoke(null, sessionId, userId, roles);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  /**
   * @see com.runwaysdk.ClientRequest#removeMember(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public String removeMember(String sessionId, String userId, String ... roles)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("removeMember", String.class, String.class, String[].class).
        invoke(null, sessionId, userId, roles);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String grantTypePermission(String sessionId, String actorId, String mdTypeId, String ... operationNames)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("grantTypePermission", String.class, String.class, String.class, String[].class).
        invoke(null, sessionId, actorId, mdTypeId, operationNames);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String grantMethodPermission(String sessionId, String actorId, String mdMethodId, String ... operationNames)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("grantMethodPermission", String.class, String.class, String.class, String[].class).
        invoke(null, sessionId, actorId, mdMethodId, operationNames);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String grantStatePermission(String sessionId, String actorId, String stateId, String ... operationNames)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("grantStatePermission", String.class, String.class, String.class, String[].class).
        invoke(null, sessionId, actorId, stateId, operationNames);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String grantAttributePermission(String sessionId, String actorId, String mdAttributeId, String ... operationNames)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("grantAttributePermission", String.class, String.class, String.class, String[].class).
        invoke(null, sessionId, actorId, mdAttributeId, operationNames);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;

  }

  public String grantAttributeStatePermission(String sessionId, String actorId, String mdAttributeId, String stateId, String ... operationNames)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("grantAttributeStatePermission", String.class, String.class, String.class, String.class, String[].class).
        invoke(null, sessionId, actorId, mdAttributeId, stateId, operationNames);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String promoteObject(String sessionId, String businessJSON, String transitionName)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("promoteObject", String.class, String.class, String.class).
        invoke(null, sessionId, businessJSON, transitionName);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String revokeTypePermission(String sessionId, String actorId, String mdTypeId, String ... operationNames)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("revokeTypePermission", String.class, String.class, String.class, String[].class).
        invoke(null, sessionId, actorId, mdTypeId, operationNames);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String revokeMethodPermission(String sessionId, String actorId, String mdMethodId, String ... operationNames)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("revokeMethodPermission", String.class, String.class, String.class, String[].class).
        invoke(null, sessionId, actorId, mdMethodId, operationNames);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String revokeStatePermission(String sessionId, String actorId, String stateId, String ... operationNames)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("revokeStatePermission", String.class, String.class, String.class, String[].class).
        invoke(null, sessionId, actorId, stateId, operationNames);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String revokeAttributePermission(String sessionId, String actorId, String mdAttributeId, String ... operationNames)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("revokeAttributePermission", String.class, String.class, String.class, String[].class).
        invoke(null, sessionId, actorId, mdAttributeId, operationNames);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String revokeAttributeStatePermission(String sessionId, String actorId, String mdAttributeId, String stateId, String ... operationNames)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("revokeAttributeStatePermission", String.class, String.class, String.class, String.class, String[].class).
        invoke(null, sessionId, actorId, mdAttributeId, stateId, operationNames);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String queryBusinesses(String sessionId, String queryJSON)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("queryBusinesses", String.class, String.class).
        invoke(null, sessionId, queryJSON);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String queryViews(String sessionId, String queryJSON)
  {
    String returnJson;
    
    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);
    
    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("queryViews", String.class, String.class).
          invoke(null, sessionId, queryJSON);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    
    return returnJson;
  }
  
  public String queryRelationships(String sessionId, String queryJSON)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("queryRelationships", String.class, String.class).
        invoke(null, sessionId, queryJSON);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String queryStructs(String sessionId, String queryJSON)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("queryStructs", String.class, String.class).
        invoke(null, sessionId, queryJSON);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String queryEntities(String sessionId, String queryJSON)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("queryEntities", String.class, String.class).
        invoke(null, sessionId, queryJSON);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String deleteChildren(String sessionId, String id, String relationshipType)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("deleteChildren", String.class, String.class, String.class).
        invoke(null, sessionId, id, relationshipType);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String deleteParents(String sessionId, String id, String relationshipType)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("deleteParents", String.class, String.class, String.class).
        invoke(null, sessionId, id, relationshipType);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String invokeMethod(String sessionId, String metadata, String entityJSON, String parameters)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("invokeMethod", String.class, String.class, String.class, String.class).
        invoke(null, sessionId, metadata, entityJSON, parameters);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String importTypes(String sessionId, String[] types, Boolean typesOnly)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("importTypes", String.class, String[].class, Boolean.class).
        invoke(null, sessionId, types, typesOnly);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String getNewestUpdateDate(String sessionId, String[] types, Boolean typesOnly)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
     returnJson = (String)jsonJavaAdapterClass.getMethod("getNewestUpdateDate", String.class, String[].class, Boolean.class).
        invoke(null, sessionId, types, typesOnly);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String getEnumeration(String sessionId, String enumType, String enumName)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("getEnumeration", String.class, String.class, String.class).
        invoke(null, sessionId, enumType, enumName);
    }
    catch(Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String getEnumerations(String sessionId, String enumType, String[] enumNames)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("getEnumerations", String.class, String.class, String[].class).
        invoke(null, sessionId, enumType, enumNames);
    }
    catch(Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String getAllEnumerations(String sessionId, String enumType)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("getAllEnumerations", String.class, String.class).
        invoke(null, sessionId, enumType);
    }
    catch(Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String createStruct(String sessionId, String structJSON)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("createStruct", String.class, String.class).
        invoke(null, sessionId, structJSON);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String newStruct(String sessionId, String type)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("newStruct", String.class, String.class).
        invoke(null, sessionId, type);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String newMutable(String sessionId, String type)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("newMutable", String.class, String.class).
        invoke(null, sessionId, type);
    }
    catch (Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String getQuery(String sessionId, String type)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("getQuery", String.class, String.class).
        invoke(null, sessionId, type);
    }
    catch(Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String groovyObjectQuery(String sessionId, String queryDTOJSON)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("groovyObjectQuery", String.class, String.class).
        invoke(null, sessionId, queryDTOJSON);
    }
    catch(Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String groovyValueQuery(String sessionId, String queryDTOJSON)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("groovyValueQuery", String.class, String.class).
        invoke(null, sessionId, queryDTOJSON);
    }
    catch(Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }

  public String checkAdminScreenAccess(String sessionId)
  {
    String returnJson;

    Class<?> jsonJavaAdapterClass = LoaderDecorator.load(AdapterInfo.JSON_JAVA_ADAPTER_CLASS);

    try
    {
      returnJson = (String)jsonJavaAdapterClass.getMethod("checkAdminScreenAccess", String.class).
        invoke(null, sessionId);
    }
    catch(Throwable e)
    {
      throw ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }

    return returnJson;
  }
}
