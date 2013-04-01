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

import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import com.runwaysdk.MessageExceptionDTO;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.BusinessQueryDTO;
import com.runwaysdk.business.ClassQueryDTO;
import com.runwaysdk.business.ComponentQueryDTO;
import com.runwaysdk.business.EntityDTO;
import com.runwaysdk.business.EntityQueryDTO;
import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.business.RelationshipDTO;
import com.runwaysdk.business.RelationshipQueryDTO;
import com.runwaysdk.business.SessionDTO;
import com.runwaysdk.business.StructDTO;
import com.runwaysdk.business.StructQueryDTO;
import com.runwaysdk.business.ValueQueryDTO;
import com.runwaysdk.business.generation.json.JSONFacade;
import com.runwaysdk.facade.Facade;
import com.runwaysdk.session.Request;
import com.runwaysdk.transport.conversion.ConversionFacade;
import com.runwaysdk.transport.conversion.json.JSONReturnObject;
import com.runwaysdk.transport.conversion.json.JSONUtil;

/**
 * Class for all JSON adapters to delegate too, since all methods behaviors are
 * identical.
 */
public class JSONAdapterDelegate
{
  public static String checkAdminScreenAccess(String sessionId)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();

    try
    {
      Facade.checkAdminScreenAccess(sessionId);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
    }

    return returnJSON.toString();
  }

  /**
   * @see com.runwaysdk.ClientRequest#addChild(java.lang.String,
   *      java.lang.String, java.lang.String,
   *      com.runwaysdk.business.RelationshipDTO)
   */
  public static String addChild(String sessionId, String parentId, String childId, String relationshipType)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    RelationshipDTO relationshipDTO;

    try
    {
      relationshipDTO = Facade.addChild(sessionId, parentId, childId, relationshipType);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      relationshipDTO = (RelationshipDTO) me.getReturnObject();
    }

    JSONObject value = JSONFacade.getJSONFromComponentDTO(relationshipDTO);
    returnJSON.setReturnValue(value);
    return returnJSON.toString();
  }

  /**
   * @see com.runwaysdk.ClientRequest#addParent(java.lang.String,
   *      java.lang.String, java.lang.String,
   *      com.runwaysdk.business.RelationshipDTO)
   */
  public static String addParent(String sessionId, String parentId, String childId, String relationshipType)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    RelationshipDTO relationshipDTO;

    try
    {
      relationshipDTO = Facade.addParent(sessionId, parentId, childId, relationshipType);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      relationshipDTO = (RelationshipDTO) me.getReturnObject();
    }

    JSONObject value = JSONFacade.getJSONFromComponentDTO(relationshipDTO);
    returnJSON.setReturnValue(value);
    return returnJSON.toString();
  }

  /**
   * @see com.runwaysdk.ClientRequest#delete(java.lang.String,
   *      java.lang.String)
   */
  public static String delete(String sessionId, String id)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();

    try
    {
      Facade.delete(sessionId, id);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
    }

    return returnJSON.toString();
  }

  /**
   * @see com.runwaysdk.ClientRequest#get(java.lang.String,
   *      java.lang.String)
   */
  public static String get(String sessionId, String id)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    MutableDTO mutableDTO;

    try
    {
      mutableDTO = Facade.get(sessionId, id);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      mutableDTO = (MutableDTO) me.getReturnObject();
    }

    JSONObject value = JSONFacade.getJSONFromComponentDTO(mutableDTO);
    returnJSON.setReturnValue(value);
    return returnJSON.toString();
  }

  public static String getQuery(String sessionId, String type)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    ClassQueryDTO queryDTO;

    try
    {
      queryDTO = Facade.getQuery(sessionId, type);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      queryDTO = (ClassQueryDTO) me.getReturnObject();
    }

    JSONObject value = JSONFacade.getJSONFromQueryDTO(queryDTO, false);
    returnJSON.setReturnValue(value);
    return returnJSON.toString();
  }

  public static String groovyObjectQuery(String sessionId, String queryDTOJSON)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    ComponentQueryDTO queryDTO;

    try
    {
      Locale locale = Facade.getSessionLocale(sessionId);
      queryDTO = JSONFacade.getQueryDTOFromJSON(sessionId, locale, queryDTOJSON);
      queryDTO = Facade.groovyObjectQuery(sessionId, queryDTO);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      queryDTO = (ComponentQueryDTO) me.getReturnObject();
    }

    JSONObject value = JSONFacade.getJSONFromQueryDTO(queryDTO, false);
    returnJSON.setReturnValue(value);
    return returnJSON.toString();
  }

  public static String groovyValueQuery(String sessionId, String queryDTOJSON)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    ValueQueryDTO queryDTO;

    try
    {
      Locale locale = Facade.getSessionLocale(sessionId);
      queryDTO = (ValueQueryDTO) JSONFacade.getQueryDTOFromJSON(sessionId, locale, queryDTOJSON);
      queryDTO = Facade.groovyValueQuery(sessionId, queryDTO);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      queryDTO = (ValueQueryDTO) me.getReturnObject();
    }

    JSONObject value = JSONFacade.getJSONFromQueryDTO(queryDTO, false);
    returnJSON.setReturnValue(value);
    return returnJSON.toString();
  }

  /**
   * @see com.runwaysdk.ClientRequest#createSessionComponent(java.lang.String,
   *      com.runwaysdk.transport.Session)
   */
  public static String createSessionComponent(String sessionId, String json)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    SessionDTO sessionDTO;

    try
    {
      Locale locale = Facade.getSessionLocale(sessionId);
      sessionDTO = (SessionDTO) JSONUtil.getComponentDTOFromJSON(sessionId, locale, json);
      sessionDTO = Facade.createSessionComponent(sessionId, sessionDTO);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      sessionDTO = (SessionDTO) me.getReturnObject();
    }

    JSONObject value = JSONFacade.getJSONFromComponentDTO(sessionDTO);
    returnJSON.setReturnValue(value);
    return returnJSON.toString();
  }

  /**
   * @see com.runwaysdk.ClientRequest#createBusiness(java.lang.String,
   *      com.runwaysdk.business.BusinessDTO)
   */
  public static String createBusiness(String sessionId, String businessJSON)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    BusinessDTO businessDTO;

    try
    {
      Locale locale = Facade.getSessionLocale(sessionId);
      businessDTO = (BusinessDTO) JSONUtil.getComponentDTOFromJSON(sessionId, locale, businessJSON);
      businessDTO = Facade.createBusiness(sessionId, businessDTO);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      businessDTO = (BusinessDTO) me.getReturnObject();
    }

    JSONObject value = JSONFacade.getJSONFromComponentDTO(businessDTO);
    returnJSON.setReturnValue(value);
    return returnJSON.toString();
  }

  public static String createRelationship(String sessionId, String relationshipJSON)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    RelationshipDTO relationshipDTO;

    try
    {
      Locale locale = Facade.getSessionLocale(sessionId);
      relationshipDTO = (RelationshipDTO) JSONUtil.getComponentDTOFromJSON(sessionId, locale, relationshipJSON);
      relationshipDTO = Facade.createRelationship(sessionId, relationshipDTO);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      relationshipDTO = (RelationshipDTO) me.getReturnObject();
    }

    JSONObject value = JSONFacade.getJSONFromComponentDTO(relationshipDTO);
    returnJSON.setReturnValue(value);
    return returnJSON.toString();
  }

  /**
   * @see com.runwaysdk.ClientRequest#login(java.lang.String,
   *      java.lang.String, java.lang.String[])
   */
  public static String login(String username, String password, String[] stringLocales)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    String sessionId;

    try
    {
      Locale[] locales = ConversionFacade.convertLocales(stringLocales);

      sessionId = Facade.login(username, password, locales);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      sessionId = (String) me.getReturnObject();
    }

    returnJSON.setReturnValue(sessionId);
    return returnJSON.toString();
  }

  /**
   * @see com.runwaysdk.ClientRequest#login(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String[])
   */
  public static String login(String username, String password, String dimensionKey, String[] stringLocales)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    String sessionId;

    try
    {
      Locale[] locales = ConversionFacade.convertLocales(stringLocales);

      sessionId = Facade.login(username, password, dimensionKey, locales);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      sessionId = (String) me.getReturnObject();
    }

    returnJSON.setReturnValue(sessionId);
    return returnJSON.toString();
  }

  /**
   * @see com.runwaysdk.ClientRequest#setDimension(java.lang.String,
   *      java.lang.String)
   */
  public static String setDimension(String sessionId, String dimensionKey)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    try
    {
      Facade.setDimension(sessionId, dimensionKey);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
    }
    return returnJSON.toString();
  }

  /**
   * @see com.runwaysdk.ClientRequest#loginAnonymous(Locale[])
   */
  public static String loginAnonymous(String[] stringLocales)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    String sessionId;

    try
    {
      Locale[] locales = ConversionFacade.convertLocales(stringLocales);

      sessionId = Facade.loginAnonymous(locales);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      sessionId = (String) me.getReturnObject();
    }

    returnJSON.setReturnValue(sessionId);
    return returnJSON.toString();
  }

  /**
   * @see com.runwaysdk.ClientRequest#loginAnonymous(java.lang.String, Locale[])
   */
  public static String loginAnonymous(String dimensionKey, String[] stringLocales)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    String sessionId;

    try
    {
      Locale[] locales = ConversionFacade.convertLocales(stringLocales);

      sessionId = Facade.loginAnonymous(dimensionKey, locales);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      sessionId = (String) me.getReturnObject();
    }

    returnJSON.setReturnValue(sessionId);
    return returnJSON.toString();
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
  public static String changeLogin(String sessionId, String username, String password)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();

    try
    {
      Facade.changeLogin(sessionId, username, password);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
    }

    return returnJSON.toString();
  }

  /**
   * @see com.runwaysdk.ClientRequest#getSessionUser()
   */
  public static String getSessionUser(String sessionId)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    BusinessDTO businessDTO;

    try
    {
      businessDTO = Facade.getSessionUser(sessionId);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      businessDTO = (BusinessDTO) me.getReturnObject();
    }

    JSONObject value = JSONFacade.getJSONFromComponentDTO(businessDTO);
    returnJSON.setReturnValue(value);
    return returnJSON.toString();
  }

  /**
   * @see com.runwaysdk.ClientRequest#getSessionUserRoles()
   */
  public static String getSessionUserRoles(String sessionId)
  {
    throw new RuntimeException("Unimplemented Method");
  }

  /**
   * @see com.runwaysdk.ClientRequest#logout(java.lang.String)
   */
  public static String logout(String sessionId)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();

    try
    {
      Facade.logout(sessionId);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
    }

    return returnJSON.toString();
  }

  /**
   * @see com.runwaysdk.ClientRequest#newBusiness(java.lang.String,
   *      java.lang.String)
   */
  public static String newBusiness(String sessionId, String type)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    BusinessDTO businessDTO;

    try
    {
      businessDTO = Facade.newBusiness(sessionId, type);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      businessDTO = (BusinessDTO) me.getReturnObject();
    }

    JSONObject value = JSONFacade.getJSONFromComponentDTO(businessDTO);
    returnJSON.setReturnValue(value);
    return returnJSON.toString();
  }

  /**
   * @see com.runwaysdk.ClientRequest#update(java.lang.String,
   *      java.lang.String)
   */
  public static String update(String sessionId, String json)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    MutableDTO mutableDTO;

    try
    {
      Locale locale = Facade.getSessionLocale(sessionId);
      mutableDTO = (MutableDTO) JSONUtil.getComponentDTOFromJSON(sessionId, locale, json);
      mutableDTO = Facade.update(sessionId, mutableDTO);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      mutableDTO = (MutableDTO) me.getReturnObject();
    }

    JSONObject value = JSONFacade.getJSONFromComponentDTO(mutableDTO);
    returnJSON.setReturnValue(value);
    return returnJSON.toString();
  }

  /**
   * @see com.runwaysdk.ClientRequest#assignMember(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public static String assignMember(String sessionId, String userId, String... roles)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();

    try
    {
      Facade.assignMember(sessionId, userId, roles);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
    }

    return returnJSON.toString();
  }

  /**
   * @see com.runwaysdk.ClientRequest#removeMember(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public static String removeMember(String sessionId, String userId, String... roles)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();

    try
    {
      Facade.removeMember(sessionId, userId, roles);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
    }

    return returnJSON.toString();
  }

  /**
   * @see com.runwaysdk.ClientRequest#grantTypePermission(java.lang.String,
   *      java.lang.String, java.lang.String, String...)
   */
  public static String grantTypePermission(String sessionId, String actorId, String mdTypeId, String... operationNames)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();

    try
    {
      Facade.grantTypePermission(sessionId, actorId, mdTypeId, operationNames);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
    }

    return returnJSON.toString();
  }

  /**
   * @see com.runwaysdk.ClientRequest#grantMethodPermission(java.lang.String,
   *      java.lang.String, java.lang.String, String...)
   */
  public static String grantMethodPermission(String sessionId, String actorId, String mdTypeId, String... operationNames)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();

    try
    {
      Facade.grantMethodPermission(sessionId, actorId, mdTypeId, operationNames);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
    }

    return returnJSON.toString();
  }

  /**
   * @see com.runwaysdk.ClientRequest#grantStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String)
   */
  public static String grantStatePermission(String sessionId, String actorId, String stateId, String... operationNames)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();

    try
    {
      Facade.grantStatePermission(sessionId, actorId, stateId, operationNames);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
    }

    return returnJSON.toString();
  }

  /**
   * @see com.runwaysdk.ClientRequest#grantAttributePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public static String grantAttributePermission(String sessionId, String actorId, String mdAttributeId, String... operationNames)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();

    try
    {
      Facade.grantAttributePermission(sessionId, actorId, mdAttributeId, operationNames);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
    }

    return returnJSON.toString();
  }

  /**
   * @see com.runwaysdk.ClientRequest#grantAttributeStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String,
   *      java.lang.String...)
   */
  public static String grantAttributeStatePermission(String sessionId, String actorId, String mdAttributeId, String stateId, String... operationNames)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();

    try
    {
      Facade.grantAttributeStatePermission(sessionId, actorId, mdAttributeId, stateId, operationNames);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
    }

    return returnJSON.toString();
  }

  /**
   * @see com.runwaysdk.ClientRequest#promoteObject(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public static String promoteObject(String sessionId, String businessJSON, String transitionName)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    BusinessDTO businessDTO;

    try
    {
      Locale locale = Facade.getSessionLocale(sessionId);
      businessDTO = (BusinessDTO) JSONUtil.getComponentDTOFromJSON(sessionId, locale, businessJSON);
      businessDTO = Facade.promoteObject(sessionId, businessDTO, transitionName);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      businessDTO = (BusinessDTO) me.getReturnObject();
    }

    JSONObject value = JSONFacade.getJSONFromComponentDTO(businessDTO);
    returnJSON.setReturnValue(value);
    return returnJSON.toString();
  }

  /**
   * @see com.runwaysdk.ClientRequest#revokeTypePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public static String revokeTypePermission(String sessionId, String actorId, String mdTypeId, String... operationNames)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();

    try
    {
      Facade.revokeTypePermission(sessionId, actorId, mdTypeId, operationNames);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
    }

    return returnJSON.toString();
  }

  /**
   * @see com.runwaysdk.ClientRequest#revokeMethodPermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public static String revokeMethodPermission(String sessionId, String actorId, String mdMethodId, String... operationNames)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();

    try
    {
      Facade.revokeMethodPermission(sessionId, actorId, mdMethodId, operationNames);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
    }

    return returnJSON.toString();
  }

  /**
   * @see com.runwaysdk.ClientRequest#revokeStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public static String revokeStatePermission(String sessionId, String actorId, String stateId, String... operationNames)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();

    try
    {
      Facade.revokeStatePermission(sessionId, actorId, stateId, operationNames);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
    }

    return returnJSON.toString();
  }

  /**
   * @see com.runwaysdk.ClientRequest#revokeAttributePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public static String revokeAttributePermission(String sessionId, String actorId, String mdAttributeId, String... operationNames)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();

    try
    {
      Facade.revokeAttributePermission(sessionId, actorId, mdAttributeId, operationNames);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
    }

    return returnJSON.toString();
  }

  /**
   * @see com.runwaysdk.ClientRequest#revokeAttributeStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String,
   *      java.lang.String)
   */
  public static String revokeAttributeStatePermission(String sessionId, String actorId, String mdAttributeId, String stateId, String... operationNames)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();

    try
    {
      Facade.revokeAttributeStatePermission(sessionId, actorId, mdAttributeId, stateId, operationNames);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
    }

    return returnJSON.toString();
  }

  /**
   * @see com.runwaysdk.ClientRequest#lock(java.lang.String,
   *      java.lang.String)
   */
  public static String lock(String sessionId, String id)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    EntityDTO entityDTO;

    try
    {
      entityDTO = Facade.lock(sessionId, id);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      entityDTO = (EntityDTO) me.getReturnObject();
    }

    JSONObject value = JSONFacade.getJSONFromComponentDTO(entityDTO);
    returnJSON.setReturnValue(value);
    return returnJSON.toString();
  }

  /**
   * @see com.runwaysdk.ClientRequest#unlock(java.lang.String,
   *      java.lang.String)
   */
  public static String unlock(String sessionId, String id)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    EntityDTO entityDTO;

    try
    {
      entityDTO = Facade.unlock(sessionId, id);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      entityDTO = (EntityDTO) me.getReturnObject();
    }

    JSONObject value = JSONFacade.getJSONFromComponentDTO(entityDTO);
    returnJSON.setReturnValue(value);
    return returnJSON.toString();
  }

  /**
   * @see com.runwaysdk.ClientRequest#deleteChild(java.lang.String,
   *      java.lang.String)
   */
  public static String deleteChild(String sessionId, String relationshipId)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();

    try
    {
      Facade.deleteChild(sessionId, relationshipId);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
    }

    return returnJSON.toString();
  }

  /**
   * @see com.runwaysdk.ClientRequest#deleteParent(java.lang.String,
   *      java.lang.String)
   */
  public static String deleteParent(String sessionId, String relationshipId)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();

    try
    {
      Facade.deleteParent(sessionId, relationshipId);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
    }

    return returnJSON.toString();
  }

  /**
   * @see com.runwaysdk.ClientRequest#getChildren(java.lang.String,
   *      com.runwaysdk.business.BusinessDTO, java.lang.String)
   */
  @SuppressWarnings("unchecked")
  public static String getChildren(String sessionId, String id, String relationshipType)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    List<BusinessDTO> businessDTOs;

    try
    {
      businessDTOs = Facade.getChildren(sessionId, id, relationshipType);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      businessDTOs = (List<BusinessDTO>) me.getReturnObject();
    }

    JSONArray value = JSONFacade.getJSONArrayFromComponentDTOs(businessDTOs);
    returnJSON.setReturnValue(value);
    return returnJSON.toString();
  }

  @SuppressWarnings("unchecked")
  public static String getChildRelationships(String sessionId, String id, String relationshipType)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    List<RelationshipDTO> relationshipDTOs;

    try
    {
      relationshipDTOs = Facade.getChildRelationships(sessionId, id, relationshipType);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      relationshipDTOs = (List<RelationshipDTO>) me.getReturnObject();
    }

    JSONArray value = JSONFacade.getJSONArrayFromComponentDTOs(relationshipDTOs);
    returnJSON.setReturnValue(value);
    return returnJSON.toString();
  }

  @SuppressWarnings("unchecked")
  public static String getParentRelationships(String sessionId, String id, String relationshipType)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    List<RelationshipDTO> relationshipDTOs;

    try
    {
      relationshipDTOs = Facade.getParentRelationships(sessionId, id, relationshipType);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      relationshipDTOs = (List<RelationshipDTO>) me.getReturnObject();
    }

    JSONArray value = JSONFacade.getJSONArrayFromComponentDTOs(relationshipDTOs);
    returnJSON.setReturnValue(value);
    return returnJSON.toString();
  }

  @SuppressWarnings("unchecked")
  public static String getParents(String sessionId, String id, String relationshipType)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    List<BusinessDTO> businessDTOs;

    try
    {
      businessDTOs = Facade.getParents(sessionId, id, relationshipType);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      businessDTOs = (List<BusinessDTO>) me.getReturnObject();
    }

    JSONArray value = JSONFacade.getJSONArrayFromComponentDTOs(businessDTOs);
    returnJSON.setReturnValue(value);
    return returnJSON.toString();
  }

  public static String deleteChildren(String sessionId, String id, String relationshipType)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();

    try
    {
      Facade.deleteChildren(sessionId, id, relationshipType);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
    }

    return returnJSON.toString();
  }

  public static String deleteParents(String sessionId, String id, String relationshipType)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();

    try
    {
      Facade.deleteParents(sessionId, id, relationshipType);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
    }

    return returnJSON.toString();
  }

  public static String queryBusinesses(String sessionId, String queryJSON)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    BusinessQueryDTO queryDTO;

    try
    {
      Locale locale = Facade.getSessionLocale(sessionId);
      queryDTO = (BusinessQueryDTO) JSONFacade.getQueryDTOFromJSON(sessionId, locale, queryJSON);
      queryDTO = Facade.queryBusinesses(sessionId, queryDTO);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      queryDTO = (BusinessQueryDTO) me.getReturnObject();
    }

    JSONObject value = JSONFacade.getJSONFromQueryDTO(queryDTO, false);
    returnJSON.setReturnValue(value);
    return returnJSON.toString();
  }

  public static String queryStructs(String sessionId, String queryJSON)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    StructQueryDTO queryDTO;

    try
    {
      Locale locale = Facade.getSessionLocale(sessionId);
      queryDTO = (StructQueryDTO) JSONFacade.getQueryDTOFromJSON(sessionId, locale, queryJSON);
      queryDTO = Facade.queryStructs(sessionId, queryDTO);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      queryDTO = (StructQueryDTO) me.getReturnObject();
    }

    JSONObject value = JSONFacade.getJSONFromQueryDTO(queryDTO, false);
    returnJSON.setReturnValue(value);
    return returnJSON.toString();
  }

  public static String queryEntities(String sessionId, String queryJSON)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    EntityQueryDTO queryDTO;

    try
    {
      Locale locale = Facade.getSessionLocale(sessionId);
      queryDTO = (EntityQueryDTO) JSONFacade.getQueryDTOFromJSON(sessionId, locale, queryJSON);
      queryDTO = Facade.queryEntities(sessionId, queryDTO);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      queryDTO = (EntityQueryDTO) me.getReturnObject();
    }

    JSONObject value = JSONFacade.getJSONFromQueryDTO(queryDTO, false);
    returnJSON.setReturnValue(value);
    return returnJSON.toString();
  }

  public static String queryRelationships(String sessionId, String queryJSON)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    RelationshipQueryDTO queryDTO;

    try
    {
      Locale locale = Facade.getSessionLocale(sessionId);
      queryDTO = (RelationshipQueryDTO) JSONFacade.getQueryDTOFromJSON(sessionId, locale, queryJSON);
      queryDTO = Facade.queryRelationships(sessionId, queryDTO);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      queryDTO = (RelationshipQueryDTO) me.getReturnObject();
    }

    JSONObject value = JSONFacade.getJSONFromQueryDTO(queryDTO, false);
    returnJSON.setReturnValue(value);
    return returnJSON.toString();
  }

  public static String invokeMethod(String sessionId, String metadata, String entityJSON, String parameters)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    JSONArray array;

    try
    {
      Locale locale = Facade.getSessionLocale(sessionId);
      array = JSONFacade.jsonInvokeMethod(sessionId, locale, metadata, entityJSON, parameters);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      Object[] object = (Object[]) me.getReturnObject();
      array = JSONFacade.getJSONFromInvokedMethod(sessionId, object);
    }

    returnJSON.setReturnValue(array);
    return returnJSON.toString();
  }

  public static String importTypes(String sessionId, String[] types, Boolean typesOnly)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    String imported;

    try
    {
      imported = Facade.importTypes(sessionId, types);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      imported = (String) me.getReturnObject();
    }

    if (typesOnly)
    {
      return imported;
    }

    returnJSON.setReturnValue(imported);
    return returnJSON.toString();
  }

  public static String getNewestUpdateDate(String sessionId, String[] types, Boolean typesOnly)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    String imported;

    try
    {
      imported = Facade.getNewestUpdateDate(sessionId, types);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      imported = (String) me.getReturnObject();
    }

    if (typesOnly)
    {
      return imported;
    }

    returnJSON.setReturnValue(imported);
    return returnJSON.toString();
  }

  public static String getEnumeration(String sessionId, String enumType, String enumName)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    BusinessDTO businessDTO;

    try
    {
      businessDTO = Facade.getEnumeration(sessionId, enumType, enumName);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      businessDTO = (BusinessDTO) me.getReturnObject();
    }

    JSONObject value = JSONFacade.getJSONFromComponentDTO(businessDTO);
    returnJSON.setReturnValue(value);
    return returnJSON.toString();
  }

  @SuppressWarnings("unchecked")
  public static String getEnumerations(String sessionId, String enumType, String[] enumNames)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    List<BusinessDTO> businessDTOs;

    try
    {
      businessDTOs = Facade.getEnumerations(sessionId, enumType, enumNames);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      businessDTOs = (List<BusinessDTO>) me.getReturnObject();
    }

    JSONArray value = JSONFacade.getJSONArrayFromComponentDTOs(businessDTOs);
    returnJSON.setReturnValue(value);
    return returnJSON.toString();
  }

  @SuppressWarnings("unchecked")
  public static String getAllEnumerations(String sessionId, String enumType)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    List<BusinessDTO> businessDTOs;

    try
    {
      businessDTOs = Facade.getAllEnumerations(sessionId, enumType);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      businessDTOs = (List<BusinessDTO>) me.getReturnObject();
    }

    JSONArray value = JSONFacade.getJSONArrayFromComponentDTOs(businessDTOs);
    returnJSON.setReturnValue(value);
    return returnJSON.toString();
  }

  public static String createStruct(String sessionId, String structJSON)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    StructDTO structDTO;

    try
    {
      Locale locale = Facade.getSessionLocale(sessionId);
      structDTO = (StructDTO) JSONUtil.getComponentDTOFromJSON(sessionId, locale, structJSON);
      structDTO = Facade.createStruct(sessionId, structDTO);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      structDTO = (StructDTO) me.getReturnObject();
    }

    JSONObject value = JSONFacade.getJSONFromComponentDTO(structDTO);
    returnJSON.setReturnValue(value);
    return returnJSON.toString();
  }

  public static String newStruct(String sessionId, String type)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    StructDTO structDTO;

    try
    {
      structDTO = Facade.newStruct(sessionId, type);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      structDTO = (StructDTO) me.getReturnObject();
    }

    JSONObject value = JSONFacade.getJSONFromComponentDTO(structDTO);
    returnJSON.setReturnValue(value);
    return returnJSON.toString();
  }

  public static String newMutable(String sessionId, String type)
  {
    JSONReturnObject returnJSON = new JSONReturnObject();
    MutableDTO mutableDTO;

    try
    {
      mutableDTO = Facade.newMutable(sessionId, type);
    }
    catch (MessageExceptionDTO me)
    {
      returnJSON.extractMessages(me);
      mutableDTO = (MutableDTO) me.getReturnObject();
    }

    JSONObject value = JSONFacade.getJSONFromComponentDTO(mutableDTO);
    returnJSON.setReturnValue(value);
    return returnJSON.toString();
  }
}
