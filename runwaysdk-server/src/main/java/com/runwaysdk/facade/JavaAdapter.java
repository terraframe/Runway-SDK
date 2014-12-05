/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.runwaysdk.facade;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.BusinessQueryDTO;
import com.runwaysdk.business.ClassQueryDTO;
import com.runwaysdk.business.ComponentQueryDTO;
import com.runwaysdk.business.EntityDTO;
import com.runwaysdk.business.EntityQueryDTO;
import com.runwaysdk.business.MethodMetaData;
import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.business.RelationshipDTO;
import com.runwaysdk.business.RelationshipQueryDTO;
import com.runwaysdk.business.SessionDTO;
import com.runwaysdk.business.StructDTO;
import com.runwaysdk.business.StructQueryDTO;
import com.runwaysdk.business.ValueQueryDTO;
import com.runwaysdk.business.ViewQueryDTO;
import com.runwaysdk.business.ontology.TermAndRelDTO;

public class JavaAdapter
{

  /**
   * 
   * @param sessionId
   * @param queryDTO
   * @return
   */
  public static ClassQueryDTO getQuery(String sessionId, String type)
  {
    return Facade.getQuery(sessionId, type);
  }

  /**
   * @see com.runwaysdk.ClientRequest#getTermAllChildren(java.lang.String,
   *      java.lang.String, java.lang.Integer, java.lang.Integer)
   */
  public static List<TermAndRelDTO> getTermAllChildren(String sessionId, String parentId, Integer pageNum, Integer pageSize)
  {
    return Facade.getTermAllChildren(sessionId, parentId, pageNum, pageSize);
  }

  /**
   * @see com.runwaysdk.ClientRequest#addChild(java.lang.String,
   *      java.lang.String, java.lang.String,
   *      com.runwaysdk.business.RelationshipDTO)
   */
  public static RelationshipDTO addChild(String sessionId, String parentId, String childId, String relationshipType)
  {
    return Facade.addChild(sessionId, parentId, childId, relationshipType);
  }

  /**
   * @see com.runwaysdk.ClientRequest#addParent(java.lang.String,
   *      java.lang.String, java.lang.String,
   *      com.runwaysdk.business.RelationshipDTO)
   */
  public static RelationshipDTO addParent(String sessionId, String parentId, String childId, String relationshipType)
  {
    return Facade.addParent(sessionId, parentId, childId, relationshipType);
  }

  /**
   * @see com.runwaysdk.ClientRequest#delete(java.lang.String, java.lang.String)
   */
  public static void delete(String sessionId, String id)
  {
    Facade.delete(sessionId, id);
  }

  /**
   * @see com.runwaysdk.ClientRequest#get(java.lang.String, java.lang.String)
   */
  public static MutableDTO get(String sessionId, String id)
  {
    return Facade.get(sessionId, id);
  }

  public static EntityQueryDTO getAllInstances(String sessionId, String type, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return Facade.getAllInstances(sessionId, type, sortAttribute, ascending, pageSize, pageNumber);
  }

  /**
   * @see com.runwaysdk.ClientRequest#createSessionComponent(java.lang.String,
   *      com.runwaysdk.business.MutableDTO)
   */
  public static SessionDTO createSessionComponent(String sessionId, SessionDTO sessionDTO)
  {
    return Facade.createSessionComponent(sessionId, sessionDTO);
  }

  public static void checkAdminScreenAccess(String sessionId)
  {
    Facade.checkAdminScreenAccess(sessionId);
  }

  /**
   * @see com.runwaysdk.ClientRequest#createBusiness(java.lang.String,
   *      com.runwaysdk.business.BusinessDTO)
   */
  public static BusinessDTO createBusiness(String sessionId, BusinessDTO businessDTO)
  {
    return Facade.createBusiness(sessionId, businessDTO);
  }

  public static RelationshipDTO createRelationship(String sessionId, RelationshipDTO relationshipDTO)
  {
    return Facade.createRelationship(sessionId, relationshipDTO);
  }

  /**
   * @see com.runwaysdk.ClientRequest#login(java.lang.String, java.lang.String,
   *      Locale[])
   */
  public static String login(String username, String password, Locale[] locales)
  {
    return Facade.login(username, password, locales);
  }

  /**
   * @see com.runwaysdk.ClientRequest#login(java.lang.String, java.lang.String,
   *      java.lang.String, Locale[])
   */
  public static String login(String username, String password, String dimensionKey, Locale[] locales)
  {
    return Facade.login(username, password, dimensionKey, locales);
  }

  /**
   * @see com.runwaysdk.ClientRequest#setDimension(java.lang.String,
   *      java.lang.String)
   */
  public static void setDimension(String sessionId, String dimensionKey)
  {
    Facade.setDimension(sessionId, dimensionKey);
  }

  /**
   * @see com.runwaysdk.ClientRequest#changeLogin(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public static void changeLogin(String sessionId, String username, String password)
  {
    Facade.changeLogin(sessionId, username, password);
  }

  /**
   * @see com.runwaysdk.ClientRequest#getSessionUser()
   */
  public static BusinessDTO getSessionUser(String sessionId)
  {
    return Facade.getSessionUser(sessionId);
  }

  /**
   * @see com.runwaysdk.ClientRequest#getSessionUserRoles()
   */
  public static Map<String, String> getSessionUserRoles(String sessionId)
  {
    return Facade.getSessionUserRoles(sessionId);
  }

  /**
   * @see com.runwaysdk.ClientRequest#loginAnonymous(java.util.Locale[])
   */
  public static String loginAnonymous(Locale[] locales)
  {
    return Facade.loginAnonymous(locales);
  }

  /**
   * @see com.runwaysdk.ClientRequest#loginAnonymous(java.lang.String,
   *      java.util.Locale[])
   */
  public static String loginAnonymous(String dimensionKey, Locale[] locales)
  {
    return Facade.loginAnonymous(dimensionKey, locales);
  }

  /**
   * @see com.runwaysdk.ClientRequest#logout(java.lang.String)
   */
  public static void logout(String sessionId)
  {
    Facade.logout(sessionId);
  }

  /**
   * @see com.runwaysdk.ClientRequest#newBusiness(java.lang.String,
   *      java.lang.String)
   */
  public static BusinessDTO newBusiness(String sessionId, String type)
  {
    return Facade.newBusiness(sessionId, type);
  }

  /**
   * @see com.runwaysdk.ClientRequest#newDisconnectedEntity(java.lang.String,
   *      java.lang.String)
   */
  public static EntityDTO newDisconnectedEntity(String sessionId, String type)
  {
    return Facade.newDisconnectedEntity(sessionId, type);
  }

  /**
   * @see com.runwaysdk.ClientRequest#update(java.lang.String,
   *      com.runwaysdk.business.MutableDTO)
   */
  public static MutableDTO update(String sessionId, MutableDTO mutableDTO)
  {
    return Facade.update(sessionId, mutableDTO);
  }

  /**
   * @see com.runwaysdk.ClientRequest#assignMember(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public static void assignMember(String sessionId, String userId, String... roles)
  {
    Facade.assignMember(sessionId, userId, roles);
  }

  /**
   * @see com.runwaysdk.ClientRequest#removeMember(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public static void removeMember(String sessionId, String userId, String... roles)
  {
    Facade.removeMember(sessionId, userId, roles);
  }

  /**
   * @see com.runwaysdk.ClientRequest#grantStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, String...)
   */
  public static void grantStatePermission(String sessionId, String actorId, String stateId, String... operationNames)
  {
    Facade.grantStatePermission(sessionId, actorId, stateId, operationNames);
  }

  /**
   * @see com.runwaysdk.ClientRequest#grantAttributePermission(java.lang.String,
   *      java.lang.String, java.lang.String, String...)
   */
  public static void grantAttributePermission(String sessionId, String actorId, String mdAttributeId, String... operationNames)
  {
    Facade.grantAttributePermission(sessionId, actorId, mdAttributeId, operationNames);
  }

  /**
   * @see com.runwaysdk.ClientRequest#grantAttributeStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String, String...)
   */
  public static void grantAttributeStatePermission(String sessionId, String actorId, String mdAttributeId, String stateId, String... operationNames)
  {
    Facade.grantAttributeStatePermission(sessionId, actorId, mdAttributeId, stateId, operationNames);
  }

  /**
   * @see com.runwaysdk.ClientRequest#grantTypePermission(java.lang.String,
   *      java.lang.String, java.lang.String, String...)
   */
  public static void grantTypePermission(String sessionId, String actorId, String mdTypeId, String... operationNames)
  {
    Facade.grantTypePermission(sessionId, actorId, mdTypeId, operationNames);
  }

  /**
   * @see com.runwaysdk.ClientRequest#grantMethodPermission(java.lang.String,
   *      java.lang.String, java.lang.String, String...)
   */
  public static void grantMethodPermission(String sessionId, String actorId, String mdMethodId, String... operationNames)
  {
    Facade.grantMethodPermission(sessionId, actorId, mdMethodId, operationNames);
  }

  /**
   * @see com.runwaysdk.ClientRequest#promoteObject(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public static BusinessDTO promoteObject(String sessionId, BusinessDTO businessDTO, String transitionName)
  {
    return Facade.promoteObject(sessionId, businessDTO, transitionName);
  }

  /**
   * @see com.runwaysdk.ClientRequest#revokeTypePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public static void revokeTypePermission(String sessionId, String actorId, String mdTypeId, String... operationNames)
  {
    Facade.revokeTypePermission(sessionId, actorId, mdTypeId, operationNames);
  }

  /**
   * @see com.runwaysdk.ClientRequest#revokeMethodPermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public static void revokeMethodPermission(String sessionId, String actorId, String mdMethodId, String... operationNames)
  {
    Facade.revokeMethodPermission(sessionId, actorId, mdMethodId, operationNames);
  }

  /**
   * @see com.runwaysdk.ClientRequest#revokeStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public static void revokeStatePermission(String sessionId, String actorId, String stateId, String... operationNames)
  {
    Facade.revokeStatePermission(sessionId, actorId, stateId, operationNames);
  }

  /**
   * @see com.runwaysdk.ClientRequest#revokeAttributePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public static void revokeAttributePermission(String sessionId, String actorId, String mdAttributeId, String... operationNames)
  {
    Facade.revokeAttributePermission(sessionId, actorId, mdAttributeId, operationNames);
  }

  /**
   * @see com.runwaysdk.ClientRequest#revokeAttributeStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String,
   *      java.lang.String...)
   */
  public static void revokeAttributeStatePermission(String sessionId, String actorId, String mdAttributeId, String stateId, String... operationNames)
  {
    Facade.revokeAttributeStatePermission(sessionId, actorId, mdAttributeId, stateId, operationNames);
  }

  /**
   * @see com.runwaysdk.ClientRequest#lock(java.lang.String, java.lang.String)
   */
  public static EntityDTO lock(String sessionId, String id)
  {
    return Facade.lock(sessionId, id);
  }

  /**
   * @see com.runwaysdk.ClientRequest#unlock(java.lang.String, java.lang.String)
   */
  public static EntityDTO unlock(String sessionId, String id)
  {
    return Facade.unlock(sessionId, id);
  }

  /**
   * @see com.runwaysdk.ClientRequest#deleteChild(java.lang.String,
   *      java.lang.String)
   */
  public static void deleteChild(String sessionId, String relationshipId)
  {
    Facade.deleteChild(sessionId, relationshipId);
  }

  /**
   * @see com.runwaysdk.ClientRequest#deleteParent(java.lang.String,
   *      java.lang.String)
   */
  public static void deleteParent(String sessionId, String relationshipId)
  {
    Facade.deleteParent(sessionId, relationshipId);
  }

  public static ViewQueryDTO queryViews(String sessionId, ViewQueryDTO queryDTO)
  {
    return Facade.queryViews(sessionId, queryDTO);
  }

  /**
   * @see com.runwaysdk.ClientRequest#getChildren(java.lang.String,
   *      com.runwaysdk.business.BusinessDTO, java.lang.String)
   */
  public static List<BusinessDTO> getChildren(String sessionId, String id, String relationshipType)
  {
    return Facade.getChildren(sessionId, id, relationshipType);
  }

  public static List<RelationshipDTO> getChildRelationships(String sessionId, String id, String relationshipType)
  {
    return Facade.getChildRelationships(sessionId, id, relationshipType);
  }

  public static List<RelationshipDTO> getParentRelationships(String sessionId, String id, String relationshipType)
  {
    return Facade.getParentRelationships(sessionId, id, relationshipType);
  }

  public static List<BusinessDTO> getParents(String sessionId, String id, String relationshipType)
  {
    return Facade.getParents(sessionId, id, relationshipType);
  }

  public static void deleteChildren(String sessionId, String id, String relationshipType)
  {
    Facade.deleteChildren(sessionId, id, relationshipType);
  }

  public static void deleteParents(String sessionId, String id, String relationshipType)
  {
    Facade.deleteParents(sessionId, id, relationshipType);
  }

  public static BusinessQueryDTO queryBusinesses(String sessionId, BusinessQueryDTO queryDTO)
  {
    return Facade.queryBusinesses(sessionId, queryDTO);
  }

  public static StructQueryDTO queryStructs(String sessionId, StructQueryDTO queryDTO)
  {
    return Facade.queryStructs(sessionId, queryDTO);
  }

  /**
   * Returns a ComponentQueryDTO containing the results of an arbitrary query
   * for a given type.
   * 
   * @param sessionId
   * @param ComponentQueryDTO
   * @return ComponentQueryDTO containing the query result.
   */
  public static ComponentQueryDTO groovyObjectQuery(String sessionId, ComponentQueryDTO componentQueryDTO)
  {
    return Facade.groovyObjectQuery(sessionId, componentQueryDTO);
  }

  /**
   * Returns a ValueQueryDTO containing the results of an arbitrary value query.
   * 
   * @param sessionId
   * @param valueQueryDTO
   * @return ValueQueryDTO containing the query result.
   */
  public static ValueQueryDTO groovyValueQuery(String sessionId, ValueQueryDTO valueQueryDTO)
  {
    return Facade.groovyValueQuery(sessionId, valueQueryDTO);
  }

  public static EntityQueryDTO queryEntities(String sessionId, EntityQueryDTO queryDTO)
  {
    return Facade.queryEntities(sessionId, queryDTO);
  }

  public static RelationshipQueryDTO queryRelationships(String sessionId, RelationshipQueryDTO queryDTO)
  {
    return Facade.queryRelationships(sessionId, queryDTO);
  }

  /**
   * @see com.runwaysdk.constants.ClientRequestIF#invokeMethod(String,
   *      MethodMetaData, MutableDTO, Object[])
   */
  public static Object invokeMethod(String sessionId, MethodMetaData metadata, MutableDTO mutableDTO, Object[] parameters)
  {
    return Facade.invokeMethod(sessionId, metadata, mutableDTO, parameters);
  }

  /**
   * @see com.runwaysdk.constants.ClientRequestIF#getEnumeration(String, String,
   *      String)
   */
  public static BusinessDTO getEnumeration(String sessionId, String enumType, String enumName)
  {
    return Facade.getEnumeration(sessionId, enumType, enumName);
  }

  /**
   * @see com.runwaysdk.constants.ClientRequestIF#getEnumerations(String,
   *      String, String[])
   */
  public static List<BusinessDTO> getEnumerations(String sessionId, String enumType, String[] enumNames)
  {
    return Facade.getEnumerations(sessionId, enumType, enumNames);
  }

  /**
   * @see com.runwaysdk.constants.ClientRequestIF#getAllEnumerations(String,
   *      String)
   */
  public static List<BusinessDTO> getAllEnumerations(String sessionId, String enumType)
  {
    return Facade.getAllEnumerations(sessionId, enumType);
  }

  public static BusinessDTO getVaultFileDTO(String sessionId, String type, String attributeName, String fileId)
  {
    return Facade.getVaultFileDTO(sessionId, type, attributeName, fileId);
  }

  public static StructDTO createStruct(String sessionId, StructDTO structDTO)
  {
    return Facade.createStruct(sessionId, structDTO);
  }

  public static StructDTO newStruct(String sessionId, String type)
  {
    return Facade.newStruct(sessionId, type);
  }

  public static MutableDTO newMutable(String sessionId, String type)
  {
    return Facade.newMutable(sessionId, type);
  }

  public static void importDomainModel(String sessionId, String xml, String xsd)
  {
    Facade.importDomainModel(sessionId, xml, xsd);
  }

  public static InputStream getFile(String sessionId, String fileId)
  {
    return Facade.getFile(sessionId, fileId);
  }

  public static InputStream getSecureFile(String sessionId, String type, String attributeName, String fileId)
  {
    return Facade.getSecureFile(sessionId, attributeName, type, fileId);
  }

  public static InputStream getSecureFile(String sessionId, String fileId)
  {
    return Facade.getSecureFile(sessionId, fileId);
  }

  public static BusinessDTO newFile(String sessionId, String path, String filename, String extension, InputStream stream)
  {
    return Facade.newFile(sessionId, path, filename, extension, stream);
  }

  public static BusinessDTO newSecureFile(String sessionId, String filename, String extension, InputStream stream)
  {
    return Facade.newSecureFile(sessionId, filename, extension, stream);
  }

  public static void importInstanceXML(String sessionId, String xml)
  {
    Facade.importInstanceXML(sessionId, xml);
  }

  public static InputStream exportExcelFile(String sessionId, String type, String listenerMethod, String[] params)
  {
    return Facade.exportExcelFile(sessionId, type, listenerMethod, params);
  }

  public static InputStream importExcelFile(String sessionId, InputStream stream, String type, String listenerMethod, String[] params)
  {
    return Facade.importExcelFile(sessionId, stream, type, listenerMethod, params);
  }
}
