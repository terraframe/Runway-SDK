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
package com.runwaysdk.mobile;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.BusinessQueryDTO;
import com.runwaysdk.business.ClassQueryDTO;
import com.runwaysdk.business.ComponentDTO;
import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.business.ComponentQueryDTO;
import com.runwaysdk.business.ElementDTO;
import com.runwaysdk.business.EntityQueryDTO;
import com.runwaysdk.business.MethodMetaData;
import com.runwaysdk.business.MobileDTOConversionHelper;
import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.business.RelationshipDTO;
import com.runwaysdk.business.RelationshipQueryDTO;
import com.runwaysdk.business.SessionDTO;
import com.runwaysdk.business.StructDTO;
import com.runwaysdk.business.StructQueryDTO;
import com.runwaysdk.business.ValueQueryDTO;
import com.runwaysdk.business.ViewQueryDTO;
import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.facade.Facade;
import com.runwaysdk.session.SessionFacade;
import com.runwaysdk.transport.attributes.AttributeDTO;
import com.runwaysdk.transport.metadata.caching.ClassMdSession;
import com.runwaysdk.util.DTOConversionUtilInfo;

/**
 * The mobile adapter is used in Runway's mobile space (Android, JME) and sits
 * inbetween the Facade and the mobile adapters (AndroidAdapter, AdapterMe). The
 * mobile adapter's sole purpose, currently, is to convert runway's globally
 * unique id strings into more compact local ids and to convert session ids to
 * and from mobile ids. These local ids are unique to a given mobile id. Mobile
 * ids exist outside of the scope of a session and can be used to uniquely
 * identify a given mobile device. Mobile ids usually are the phone number of
 * the mobile device, but they could easily also be a MAC address or some
 * generated string for mobile devices without phone numbers.
 * 
 * @author Richard Rowlands
 * 
 */
public class MobileAdapter
{
  private static String      MOBILE_ID_MAP = "MOBILEIDMAP";

  private static IdConverter idConverter   = IdConverter.getInstance();

  /**
   * Constructor.
   */
  public MobileAdapter()
  {
    super();
  }

  /**
   * @see com.runwaysdk.facade.Facade#getMetadata(String, String[])
   */
  public static ClassMdSession[] getMetadata(String mobileId, String[] types)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    return Facade.getMetadata(sessionId, types);
  }

  /**
   * 
   * @param mobileid
   * @param queryDTO
   * @return
   */
  public static ClassQueryDTO getQuery(String mobileId, String type)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    return (ClassQueryDTO) convertToLocalId(mobileId, Facade.getQuery(sessionId, type));
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#addChild(java.lang.String,
   *      java.lang.String, java.lang.String,
   *      com.runwaysdk.business.RelationshipDTO)
   */
  public static RelationshipDTO addChild(String mobileId, String parentId, String childId, String relationshipType)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    parentId = idConverter.getGlobalIdFromLocalId(mobileId, parentId);
    childId = idConverter.getGlobalIdFromLocalId(mobileId, childId);
    return (RelationshipDTO) convertToLocalId(mobileId, Facade.addChild(sessionId, parentId, childId, relationshipType));
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#addParent(java.lang.String,
   *      java.lang.String, java.lang.String,
   *      com.runwaysdk.business.RelationshipDTO)
   */
  public static RelationshipDTO addParent(String mobileId, String parentId, String childId, String relationshipType)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    parentId = idConverter.getGlobalIdFromLocalId(mobileId, parentId);
    childId = idConverter.getGlobalIdFromLocalId(mobileId, childId);
    return (RelationshipDTO) convertToLocalId(mobileId, Facade.addParent(sessionId, parentId, childId, relationshipType));
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#delete(java.lang.String,
   *      java.lang.String)
   */
  public static void delete(String mobileId, String id)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    id = idConverter.getGlobalIdFromLocalId(mobileId, id);
    Facade.delete(sessionId, id);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#get(java.lang.String)
   */
  public static MutableDTO get(String mobileId, String id)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    id = idConverter.getGlobalIdFromLocalId(mobileId, id);
    return (MutableDTO) convertToLocalId(mobileId, Facade.get(sessionId, id));
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#createSessionComponent(java.lang.String,
   *      com.runwaysdk.business.SessionDTO)
   */
  public static SessionDTO createSessionComponent(String mobileId, SessionDTO sessionDTO)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    convertToGlobalId(mobileId, sessionDTO);
    sessionDTO = Facade.createSessionComponent(sessionId, sessionDTO);
    return (SessionDTO) convertToLocalId(mobileId, sessionDTO);
  }

  public static void checkAdminScreenAccess(String mobileId)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    Facade.checkAdminScreenAccess(sessionId);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#createBusiness(java.lang.String,
   *      com.runwaysdk.business.BusinessDTO)
   */
  public static BusinessDTO createBusiness(String mobileId, BusinessDTO dto)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    convertToGlobalId(mobileId, dto);
    dto = Facade.createBusiness(sessionId, dto);
    return (BusinessDTO) convertToLocalId(mobileId, dto);
  }

  public static BusinessDTO createGenericBusiness(String mobileId, String type)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    return (BusinessDTO) convertToLocalId(mobileId, Facade.newBusiness(sessionId, type));
  }

  public static RelationshipDTO createRelationship(String mobileId, RelationshipDTO dto)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    convertToGlobalId(mobileId, dto);
    dto = Facade.createRelationship(sessionId, dto);
    return (RelationshipDTO) convertToLocalId(mobileId, dto);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#setDimension(java.lang.String,
   *      java.lang.String)
   */
  public static void setDimension(String mobileId, String dimensionKey)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    Facade.setDimension(sessionId, dimensionKey);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#login(java.lang.String,
   *      java.lang.String, dimensionKey[])
   */
  public static void changeLogin(String mobileId, String username, String password)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    Facade.changeLogin(sessionId, username, password);
  }

  /**
   * @see com.runwaysdk.ClientRequest#getSessionUser(java.lang.String)
   */
  public static BusinessDTO getSessionUser(String mobileId)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    return (BusinessDTO) convertToLocalId(mobileId, Facade.getSessionUser(sessionId));
  }

  /**
   * @see com.runwaysdk.ClientRequest#getSessionUserRoles()
   */
  public static Map<String, String> getSessionUserRoles(String mobileId)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    return Facade.getSessionUserRoles(sessionId);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#login(java.lang.String,
   *      java.lang.String, java.util.Locale[])
   */
  public static String login(String mobileId, String username, String password, Locale[] locales)
  {
    String sessionId = Facade.login(username, password, locales);
    SessionFacade.setSessionTime(sessionId, 0);
    idConverter.mapSessionIdToMobileId(sessionId, mobileId);
    return mobileId;
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#login(java.lang.String,
   *      java.lang.String, java.lang.String, java.util.Locale[])
   */
  public static String login(String mobileId, String username, String password, String dimensionKey, Locale[] locales)
  {

    String sessionId = Facade.login(username, password, dimensionKey, locales);
    SessionFacade.setSessionTime(sessionId, 0);
    idConverter.mapSessionIdToMobileId(sessionId, mobileId);
    return mobileId;
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#loginAnonymous(Locale[])
   */
  public static String loginAnonymous(String mobileId, Locale[] locales)
  {

    String sessionId = Facade.loginAnonymous(locales);
    SessionFacade.setSessionTime(sessionId, 0);
    idConverter.mapSessionIdToMobileId(sessionId, mobileId);
    return mobileId;
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#loginAnonymous(String, Locale[])
   */
  public static String loginAnonymous(String mobileId, String dimensionKey, Locale[] locales)
  {

    String sessionId = Facade.loginAnonymous(dimensionKey, locales);
    SessionFacade.setSessionTime(sessionId, 0);
    idConverter.mapSessionIdToMobileId(sessionId, mobileId);
    return mobileId;
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#logout(java.lang.String)
   */
  public static void logout(String mobileId)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    Facade.logout(sessionId);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#newBusiness(java.lang.String,
   *      java.lang.String)
   */
  public static BusinessDTO newBusiness(String mobileId, String type)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    return (BusinessDTO) convertToLocalId(mobileId, Facade.newBusiness(sessionId, type));
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#update(java.lang.String,
   *      com.runwaysdk.transport.EntityDTO)
   */
  public static MutableDTO update(String mobileId, MutableDTO dto)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);

    convertToGlobalId(mobileId, dto);

    dto = Facade.update(sessionId, dto);
    return (MutableDTO) convertToLocalId(mobileId, dto);
  }

  /**
   * @see com.runwaysdk.ClientRequest#assignMember(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public static void assignMember(String mobileId, String userId, String... roles)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    Facade.assignMember(sessionId, userId, roles);
  }

  /**
   * @see com.runwaysdk.ClientRequest#removeMember(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public static void removeMember(String mobileId, String userId, String... roles)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    Facade.removeMember(sessionId, userId, roles);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#grantStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, String...)
   */
  public static void grantStatePermission(String mobileId, String actorId, String stateId, String... operationNames)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    actorId = idConverter.getGlobalIdFromLocalId(mobileId, actorId);
    Facade.grantStatePermission(sessionId, actorId, stateId, operationNames);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#grantAttributePermission(java.lang.String,
   *      java.lang.String, java.lang.String, String...)
   */
  public static void grantAttributePermission(String mobileId, String actorId, String mdAttributeId, String... operationNames)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    mdAttributeId = idConverter.getGlobalIdFromLocalId(mobileId, mdAttributeId);
    actorId = idConverter.getGlobalIdFromLocalId(mobileId, actorId);
    Facade.grantAttributePermission(sessionId, actorId, mdAttributeId, operationNames);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#grantAttributeStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String, String...)
   */
  public static void grantAttributeStatePermission(String mobileId, String actorId, String mdAttributeId, String stateId, String... operationNames)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    mdAttributeId = idConverter.getGlobalIdFromLocalId(mobileId, mdAttributeId);
    actorId = idConverter.getGlobalIdFromLocalId(mobileId, actorId);
    Facade.grantAttributeStatePermission(sessionId, actorId, mdAttributeId, stateId, operationNames);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#grantTypePermission(java.lang.String,
   *      java.lang.String, java.lang.String, String...)
   */
  public static void grantTypePermission(String mobileId, String actorId, String mdTypeId, String... operationNames)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    mdTypeId = idConverter.getGlobalIdFromLocalId(mobileId, mdTypeId);
    actorId = idConverter.getGlobalIdFromLocalId(mobileId, actorId);
    Facade.grantTypePermission(sessionId, actorId, mdTypeId, operationNames);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#grantMethodPermission(java.lang.String,
   *      java.lang.String, java.lang.String, String...)
   */
  public static void grantMethodPermission(String mobileId, String actorId, String mdMethodId, String... operationNames)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    mdMethodId = idConverter.getGlobalIdFromLocalId(mobileId, mdMethodId);
    actorId = idConverter.getGlobalIdFromLocalId(mobileId, actorId);
    Facade.grantMethodPermission(sessionId, actorId, mdMethodId, operationNames);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#promoteObject(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public static BusinessDTO promoteObject(String mobileId, BusinessDTO dto, String transitionName)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    convertToGlobalId(mobileId, dto);
    dto = Facade.promoteObject(sessionId, dto, transitionName);
    return (BusinessDTO) convertToLocalId(mobileId, dto);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#revokeTypePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public static void revokeTypePermission(String mobileId, String actorId, String mdTypeId, String... operationNames)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    mdTypeId = idConverter.getGlobalIdFromLocalId(mobileId, mdTypeId);
    actorId = idConverter.getGlobalIdFromLocalId(mobileId, actorId);
    Facade.revokeTypePermission(sessionId, actorId, mdTypeId, operationNames);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#revokeMethodPermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public static void revokeMethodPermission(String mobileId, String actorId, String mdMethodId, String... operationNames)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    mdMethodId = idConverter.getGlobalIdFromLocalId(mobileId, mdMethodId);
    actorId = idConverter.getGlobalIdFromLocalId(mobileId, actorId);
    Facade.revokeMethodPermission(sessionId, actorId, mdMethodId, operationNames);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#revokeStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public static void revokeStatePermission(String mobileId, String actorId, String stateId, String... operationNames)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    actorId = idConverter.getGlobalIdFromLocalId(mobileId, actorId);
    Facade.revokeStatePermission(sessionId, actorId, stateId, operationNames);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#revokeAttributePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public static void revokeAttributePermission(String mobileId, String actorId, String mdAttributeId, String... operationNames)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    mdAttributeId = idConverter.getGlobalIdFromLocalId(mobileId, mdAttributeId);
    actorId = idConverter.getGlobalIdFromLocalId(mobileId, actorId);
    Facade.revokeAttributePermission(sessionId, actorId, mdAttributeId, operationNames);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#revokeAttributeStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String,
   *      java.lang.String...)
   */
  public static void revokeAttributeStatePermission(String mobileId, String actorId, String mdAttributeId, String stateId, String... operationNames)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    mdAttributeId = idConverter.getGlobalIdFromLocalId(mobileId, mdAttributeId);
    actorId = idConverter.getGlobalIdFromLocalId(mobileId, actorId);
    Facade.revokeAttributeStatePermission(sessionId, actorId, mdAttributeId, stateId, operationNames);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#lock(java.lang.String,
   *      java.lang.String)
   */
  public static ElementDTO lock(String mobileId, String id)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    id = idConverter.getGlobalIdFromLocalId(mobileId, id);
    return (ElementDTO) convertToLocalId(mobileId, Facade.lock(sessionId, id));
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#unlock(java.lang.String,
   *      java.lang.String)
   */
  public static ElementDTO unlock(String mobileId, String id)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    id = idConverter.getGlobalIdFromLocalId(mobileId, id);
    return (ElementDTO) convertToLocalId(mobileId, Facade.unlock(sessionId, id));
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#deleteChild(java.lang.String,
   *      java.lang.String)
   */
  public static void deleteChild(String mobileId, String relationshipId)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    relationshipId = idConverter.getGlobalIdFromLocalId(mobileId, relationshipId);
    Facade.deleteChild(sessionId, relationshipId);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#deleteParent(java.lang.String,
   *      java.lang.String)
   */
  public static void deleteParent(String mobileId, String relationshipId)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    relationshipId = idConverter.getGlobalIdFromLocalId(mobileId, relationshipId);
    Facade.deleteParent(sessionId, relationshipId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.request.RemoteAdapter#getChildren(java.lang.String,
   * java.lang.String, java.lang.String)
   */
  @SuppressWarnings("unchecked")
  public static List<BusinessDTO> getChildren(String mobileId, String id, String relationshipType)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    id = idConverter.getGlobalIdFromLocalId(mobileId, id);
    List<BusinessDTO> list = Facade.getChildren(sessionId, id, relationshipType);
    return (List<BusinessDTO>) convertListToLocalId(mobileId, list);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.request.RemoteAdapter#getParents(java.lang.String,
   * java.lang.String, java.lang.String)
   */
  @SuppressWarnings("unchecked")
  public static List<BusinessDTO> getParents(String mobileId, String id, String relationshipType)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    id = idConverter.getGlobalIdFromLocalId(mobileId, id);

    List<BusinessDTO> list = Facade.getParents(sessionId, id, relationshipType);
    return (List<BusinessDTO>) convertListToLocalId(mobileId, list);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.request.RemoteAdapter#getChildRelationships(java.lang
   * .String, java.lang.String, java.lang.String)
   */
  @SuppressWarnings("unchecked")
  public static List<RelationshipDTO> getChildRelationships(String mobileId, String id, String relationshipType)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    id = idConverter.getGlobalIdFromLocalId(mobileId, id);
    List<RelationshipDTO> list = Facade.getChildRelationships(sessionId, id, relationshipType);
    return (List<RelationshipDTO>) convertListToLocalId(mobileId, list);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.request.RemoteAdapter#getParentRelationships(java.lang
   * .String, java.lang.String, java.lang.String)
   */
  @SuppressWarnings("unchecked")
  public static List<RelationshipDTO> getParentRelationships(String mobileId, String id, String relationshipType)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    id = idConverter.getGlobalIdFromLocalId(mobileId, id);
    List<RelationshipDTO> list = Facade.getParentRelationships(sessionId, id, relationshipType);
    return (List<RelationshipDTO>) convertListToLocalId(mobileId, list);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.request.RemoteAdapter#queryBusinesses(java.lang.String,
   * com.runwaysdk.transport.QueryDTO)
   */
  public static BusinessQueryDTO queryBusinesses(String mobileId, BusinessQueryDTO dto)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    convertToGlobalId(mobileId, dto);
    dto = Facade.queryBusinesses(sessionId, dto);
    return (BusinessQueryDTO) convertToLocalId(mobileId, dto);
  }

  public static StructQueryDTO queryStructs(String mobileId, StructQueryDTO dto)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    convertToGlobalId(mobileId, dto);
    dto = Facade.queryStructs(sessionId, dto);
    return (StructQueryDTO) convertToLocalId(mobileId, dto);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.request.RemoteController#queryViews(java.lang.String,
   * com.runwaysdk.transport.QueryDTO)
   */
  public static ViewQueryDTO queryViews(String mobileId, ViewQueryDTO dto)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    convertToGlobalId(mobileId, dto);
    dto = Facade.queryViews(sessionId, dto);
    return (ViewQueryDTO) convertToLocalId(mobileId, dto);
  }

  /**
   * Returns a ComponentQueryDTO containing the results of an arbitrary query
   * for a given type.
   * 
   * @param mobileid
   * @param ComponentQueryDTO
   * @return ComponentQueryDTO containing the query result.
   */
  public static ComponentQueryDTO groovyObjectQuery(String mobileId, ComponentQueryDTO dto)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    convertToGlobalId(mobileId, dto);
    dto = Facade.groovyObjectQuery(sessionId, dto);
    return (ComponentQueryDTO) convertToLocalId(mobileId, dto);
  }

  /**
   * Returns a ValueQueryDTO containing the results of an arbitrary value query.
   * 
   * @param mobileid
   * @param valueQueryDTO
   * @return ValueQueryDTO containing the query result.
   */
  public static ValueQueryDTO groovyValueQuery(String mobileId, ValueQueryDTO dto)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    convertToGlobalId(mobileId, dto);
    dto = Facade.groovyValueQuery(sessionId, dto);
    return (ValueQueryDTO) convertToLocalId(mobileId, dto);
  }

  public static EntityQueryDTO queryEntities(String mobileId, EntityQueryDTO dto)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    convertToGlobalId(mobileId, dto);
    dto = Facade.queryEntities(sessionId, dto);
    return (EntityQueryDTO) convertToLocalId(mobileId, dto);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.request.RemoteAdapter#queryRelationships(java.lang.
   * String, com.runwaysdk.transport.QueryDTO)
   */
  public static RelationshipQueryDTO queryRelationships(String mobileId, RelationshipQueryDTO dto)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    convertToGlobalId(mobileId, dto);
    dto = Facade.queryRelationships(sessionId, dto);
    return (RelationshipQueryDTO) convertToLocalId(mobileId, dto);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.request.RemoteAdapter#deleteChildren(java.lang.String,
   * java.lang.String, java.lang.String)
   */
  public static void deleteChildren(String mobileId, String id, String relationshipType)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    id = idConverter.getGlobalIdFromLocalId(mobileId, id);
    Facade.deleteChildren(sessionId, id, relationshipType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.request.RemoteAdapter#deleteParents(java.lang.String,
   * java.lang.String, java.lang.String)
   */
  public static void deleteParents(String mobileId, String id, String relationshipType)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    id = idConverter.getGlobalIdFromLocalId(mobileId, id);
    Facade.deleteParents(sessionId, id, relationshipType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.request.RemoteAdapter#invokeMethod(java.lang.String,
   * com.runwaysdk.transport.MutableDTO, java.lang.String, java.lang.String[],
   * java.lang.String[], java.lang.Object[])
   */
  public static Object invokeMethod(String mobileId, MethodMetaData metaData, MutableDTO mutableDTO, Object[] parameters)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);

    MobileAdapter.convertToGlobalId(mobileId, mutableDTO);

    for (Object obj : parameters)
    {
      MobileAdapter.convertParameterToGlobalId(mobileId, obj);
    }

    Object[] returned = (Object[]) Facade.invokeMethod(sessionId, metaData, mutableDTO, parameters);

    MobileAdapter.convertReturnToLocalId(mobileId, returned[DTOConversionUtilInfo.RETURN_OBJECT]);
    MobileAdapter.convertReturnToLocalId(mobileId, returned[DTOConversionUtilInfo.CALLED_OBJECT]);

    return returned;
  }

  private static void convertParameterToGlobalId(String mobileId, Object obj)
  {
    if (obj != null)
    {
      if (obj instanceof ComponentDTO)
      {
        MobileAdapter.convertToGlobalId(mobileId, (ComponentDTO) obj);
      }
      else if (obj instanceof ComponentQueryDTO)
      {
        MobileAdapter.convertToGlobalId(mobileId, (ComponentQueryDTO) obj);
      }
      else if (obj.getClass().isArray())
      {
        Object[] array = (Object[]) obj;

        for (int i = 0; i < array.length; i++)
        {
          MobileAdapter.convertParameterToGlobalId(mobileId, array[i]);
        }
      }
    }
  }

  private static void convertReturnToLocalId(String mobileId, Object obj)
  {
    if (obj != null)
    {
      if (obj instanceof ComponentDTO)
      {
        MobileAdapter.convertToLocalId(mobileId, (ComponentDTO) obj);
      }
      else if (obj instanceof ComponentQueryDTO)
      {
        MobileAdapter.convertToLocalId(mobileId, (ComponentQueryDTO) obj);
      }
      else if (obj.getClass().isArray())
      {
        Object[] array = (Object[]) obj;

        for (int i = 0; i < array.length; i++)
        {
          MobileAdapter.convertReturnToLocalId(mobileId, array[i]);
        }
      }
    }
  }

  /**
   * 
   * @see com.runwaysdk.request.RemoteAdapter#getEnumeration(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public static BusinessDTO getEnumeration(String mobileId, String enumType, String enumName)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    return (BusinessDTO) convertToLocalId(mobileId, Facade.getEnumeration(sessionId, enumType, enumName));
  }

  /**
   * @see com.runwaysdk.constants.ClientRequestIF#getEnumerations(String,
   *      String, String[])
   */
  @SuppressWarnings("unchecked")
  public static List<BusinessDTO> getEnumerations(String mobileId, String enumType, String[] enumNames)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    List<BusinessDTO> list = Facade.getEnumerations(sessionId, enumType, enumNames);
    return (List<BusinessDTO>) convertListToLocalId(mobileId, list);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#getAllEnumerations(java.lang.String,
   *      java.lang.String)
   */
  @SuppressWarnings("unchecked")
  public static List<BusinessDTO> getAllEnumerations(String mobileId, String enumType)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    List<BusinessDTO> list = Facade.getAllEnumerations(sessionId, enumType);
    return (List<BusinessDTO>) convertListToLocalId(mobileId, list);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.request.RemoteAdapter#getVaultFileDTO(java.lang.String,
   * com.runwaysdk.transport.BusinessDTO, java.lang.String)
   */
  public static BusinessDTO getVaultFileDTO(String mobileId, String type, String attributeName, String fileId)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    BusinessDTO dto = Facade.getVaultFileDTO(sessionId, type, attributeName, fileId);
    return (BusinessDTO) convertToLocalId(mobileId, dto);
  }

  public static StructDTO createStruct(String mobileId, StructDTO dto)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    convertToGlobalId(mobileId, dto);
    dto = Facade.createStruct(sessionId, dto);
    return (StructDTO) convertToLocalId(mobileId, dto);
  }

  public static StructDTO newStruct(String mobileId, String type)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    StructDTO dto = Facade.newStruct(sessionId, type);
    return (StructDTO) convertToLocalId(mobileId, dto);
  }

  public static MutableDTO newMutable(String mobileId, String type)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    MutableDTO dto = Facade.newMutable(sessionId, type);
    return (MutableDTO) convertToLocalId(mobileId, dto);
  }

  public static void importDomainModel(String mobileId, String xml, String xsd)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    Facade.importDomainModel(sessionId, xml, xsd);
  }

  /*
   * public static RemoteInputStream getFile(String mobileId, String fileId) {
   * InputStream stream = Facade.getFile(mobileId, fileId);
   * RemoteInputStreamServer remoteStream = new SimpleRemoteInputStream(stream);
   * 
   * return remoteStream.export(); }
   * 
   * public static RemoteInputStream getSecureFile(String mobileId, String type,
   * String attributeName, String fileId) { InputStream stream =
   * Facade.getSecureFile(mobileId, attributeName, type, fileId);
   * RemoteInputStreamServer remoteStream = new SimpleRemoteInputStream(stream);
   * 
   * return remoteStream.export(); }
   * 
   * public static RemoteInputStream getSecureFile(String mobileId, String
   * fileId) { InputStream stream = Facade.getSecureFile(mobileId, fileId);
   * RemoteInputStreamServer remoteStream = new SimpleRemoteInputStream(stream);
   * 
   * return remoteStream.export(); }
   * 
   * public static BusinessDTO newFile(String mobileId, String path, String
   * filename, String extension, RemoteInputStream stream) throws IOException {
   * return Facade.newFile(mobileId, path, filename, extension,
   * RemoteInputStreamClient.wrap(stream)); }
   * 
   * public static BusinessDTO newSecureFile(String mobileId, String filename,
   * String extension, RemoteInputStream stream) throws IOException { try {
   * return Facade.newSecureFile(mobileId, filename, extension,
   * RemoteInputStreamClient.wrap(stream)); } catch (Throwable e) { throw
   * convertException(e); } }
   */
  public static EntityQueryDTO getAllInstances(String mobileId, String type, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    EntityQueryDTO dto = Facade.getAllInstances(sessionId, type, sortAttribute, ascending, pageSize, pageNumber);
    return (EntityQueryDTO) convertToLocalId(mobileId, dto);
  }

  public static void importInstanceXML(String mobileId, String xml)
  {

    String sessionId = convertMobileIdToSessionId(mobileId);
    Facade.importInstanceXML(sessionId, xml);
  }

  /*
   * public static RemoteInputStream exportExcelFile(String mobileId, String
   * type, String listenerMethod, String... params) { InputStream stream =
   * Facade.exportExcelFile(mobileId, type, listenerMethod, params);
   * RemoteInputStreamServer remoteStream = new SimpleRemoteInputStream(stream);
   * 
   * return remoteStream.export(); }
   * 
   * public static RemoteInputStream importExcelFile(String mobileId,
   * RemoteInputStream stream, String type, String listenerMethod, String...
   * params) throws IOException { InputStream returnStream =
   * Facade.importExcelFile(mobileId, RemoteInputStreamClient.wrap(stream),
   * type, listenerMethod, params); RemoteInputStreamServer remoteStream = new
   * SimpleRemoteInputStream(returnStream);
   * 
   * return remoteStream.export(); }
   */
  private static String convertMobileIdToSessionId(String mobileId)
  {
    return idConverter.getSessionIdFromMobileId(mobileId);
  }

  public static String getMobileId(String connectionId)
  {
    return idConverter.generateLocalIdFromGlobalId(MOBILE_ID_MAP, connectionId);
  }

  public static void setMobileId(String connectionId, String mobileId)
  {
    idConverter.setLocalIdFromGlobalId(MOBILE_ID_MAP, connectionId, mobileId);
  }

  public static String getSessionId(String mobileId)
  {
    return idConverter.getSessionIdFromMobileId(mobileId);
  }

  private static ComponentQueryDTO convertToGlobalId(String mobileId, ComponentQueryDTO dto)
  {
    // UNTESTED
    AttributeDTO attr = dto.getAttributeDTO(ComponentInfo.ID);
    String globalId = idConverter.getGlobalIdFromLocalId(mobileId, attr.getValue());
    attr.setValue(globalId);

    List<? extends ComponentDTOIF> results = dto.getResultSet();
    for (ComponentDTOIF resultDTO : results)
    {
      convertToGlobalId(mobileId, (ComponentDTO) resultDTO);
    }

    return dto;
  }

  private static ComponentQueryDTO convertToLocalId(String mobileId, ComponentQueryDTO dto)
  {
    // UNTESTED
    AttributeDTO attr = dto.getAttributeDTO(ComponentInfo.ID);
    String localId = idConverter.generateLocalIdFromGlobalId(mobileId, attr.getValue());
    attr.setValue(localId);

    List<? extends ComponentDTOIF> results = dto.getResultSet();
    for (ComponentDTOIF resultDTO : results)
    {
      convertToLocalId(mobileId, (ComponentDTO) resultDTO);
    }

    return dto;
  }

  private static ComponentDTO convertToGlobalId(String mobileId, ComponentDTO dto)
  {
    if (dto != null)
    {
      Map<String, AttributeDTO> map = MobileDTOConversionHelper.getComponentDTOAttributeMap(dto);

      AttributeDTO attr = map.get(ComponentInfo.ID);
      boolean modified = attr.isModified();
      String localId = attr.getValue();

      String globalId = localId;

      if (dto instanceof MutableDTO)
      {
        if (! ( (MutableDTO) dto ).isNewInstance())
        {
          globalId = idConverter.getGlobalIdFromLocalId(mobileId, localId);
        }
      }
      else
      {
        globalId = idConverter.getGlobalIdFromLocalId(mobileId, localId);
      }

      attr.setValue(globalId);
      attr.setModified(modified);

      dto.copyProperties(dto, map);

      if (dto instanceof RelationshipDTO)
      {
        RelationshipDTO relat = (RelationshipDTO) dto;
        String parentId = idConverter.getGlobalIdFromLocalId(mobileId, relat.getParentId());
        String childId = idConverter.getGlobalIdFromLocalId(mobileId, relat.getChildId());
        MobileDTOConversionHelper.setRelationshipIds(relat, parentId, childId);
      }
    }

    return dto;
  }

  private static ComponentDTO convertToLocalId(String mobileId, ComponentDTO dto)
  {
    if (dto != null)
    {
      Map<String, AttributeDTO> map = MobileDTOConversionHelper.getComponentDTOAttributeMap(dto);

      AttributeDTO attr = map.get(ComponentInfo.ID);
      boolean modified = attr.isModified();
      String globalId = attr.getValue();

      String localId = idConverter.generateLocalIdFromGlobalId(mobileId, globalId);
      attr.setValue(localId);
      attr.setModified(modified);

      dto.copyProperties(dto, map);

      if (dto instanceof RelationshipDTO)
      {
        RelationshipDTO relat = (RelationshipDTO) dto;
        String parentId = idConverter.generateLocalIdFromGlobalId(mobileId, relat.getParentId());
        String childId = idConverter.generateLocalIdFromGlobalId(mobileId, relat.getChildId());
        MobileDTOConversionHelper.setRelationshipIds(relat, parentId, childId);
      }
    }

    return dto;
  }

  private static List<? extends ComponentDTO> convertListToLocalId(String mobileId, List<? extends ComponentDTO> dtos)
  {
    for (ComponentDTO dto : dtos)
    {
      convertToLocalId(mobileId, dto);
    }
    return dtos;
  }

  /**
   * This is a work around method used for testing. It should not be used
   * outside of a testing context.
   * 
   * @param mobileId
   * @param globalId
   * @return
   */
  public static String generateLocalIdFromGlobalId(String mobileId, String globalId)
  {
    return idConverter.generateLocalIdFromGlobalId(mobileId, globalId);
  }

  /**
   * This is a work around method used for testing. It should not be used
   * outside of a testing context.
   * 
   * @param mobileId
   * @param globalId
   * @return
   */
  public static String getGlobalId(String mobileId, String id)
  {
    return idConverter.getGlobalIdFromLocalId(mobileId, id);
  }
}
