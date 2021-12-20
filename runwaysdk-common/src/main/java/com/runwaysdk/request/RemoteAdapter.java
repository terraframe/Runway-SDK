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
package com.runwaysdk.request;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.healthmarketscience.rmiio.RemoteInputStream;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.BusinessQueryDTO;
import com.runwaysdk.business.ClassQueryDTO;
import com.runwaysdk.business.ElementDTO;
import com.runwaysdk.business.EntityDTO;
import com.runwaysdk.business.EntityQueryDTO;
import com.runwaysdk.business.MethodMetaData;
import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.business.RelationshipDTO;
import com.runwaysdk.business.RelationshipQueryDTO;
import com.runwaysdk.business.SessionDTO;
import com.runwaysdk.business.StructDTO;
import com.runwaysdk.business.StructQueryDTO;
import com.runwaysdk.business.ViewQueryDTO;
import com.runwaysdk.business.ontology.TermAndRelDTO;

public interface RemoteAdapter extends Remote
{
  public ClassQueryDTO getQuery(String sessionId, String type) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#addChild(java.lang.String,
   *      java.lang.String, java.lang.String,
   *      com.runwaysdk.business.RelationshipDTO)
   * @throws RemoteException
   */
  public RelationshipDTO addChild(String sessionId, String parentOid, String childOid, String relationshipType) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#addParent(java.lang.String,
   *      java.lang.String, java.lang.String,
   *      com.runwaysdk.business.RelationshipDTO)
   * @throws RemoteException
   */
  public RelationshipDTO addParent(String sessionId, String parentOid, String childOid, String relationshipType) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#delete(java.lang.String, java.lang.String)
   * @throws RemoteException
   */
  public void delete(String sessionId, String oid) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#get(java.lang.String, java.lang.String)
   * @throws RemoteException
   */
  public MutableDTO get(String sessionId, String oid) throws RemoteException;

  public void checkAdminScreenAccess(String sessionId) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#createBusiness(java.lang.String,
   *      com.runwaysdk.business.BusinessDTO)
   * @throws RemoteException
   */
  public BusinessDTO createBusiness(String sessionId, BusinessDTO BusinessDTO) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#createBusiness(java.lang.String,
   *      com.runwaysdk.business.SessionDTO)
   * @throws RemoteException
   */
  public SessionDTO createSessionComponent(String sessionId, SessionDTO sessionDTO) throws RemoteException;

  public StructDTO newStruct(String sessionId, String type) throws RemoteException;

  public StructDTO createStruct(String sessionId, StructDTO structDTO) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#createBusiness(java.lang.String,
   *      com.runwaysdk.business.BusinessDTO)
   * @throws RemoteException
   */
  public RelationshipDTO createRelationship(String sessionId, RelationshipDTO relationshipDTO) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#login(java.lang.String, java.lang.String,
   *      java.lang.String, Locale[])
   * @throws RemoteException
   */
  public String login(String username, String password, Locale[] locales) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#setDimension(java.lang.String,
   *      java.lang.String)
   * @throws RemoteException
   */
  public void setDimension(String sessionId, String dimensionKey) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#login(java.lang.String, java.lang.String,
   *      Locale[])
   * @throws RemoteException
   */
  public String login(String username, String password, String dimensionKey, Locale[] locales) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#changeLogin(java.lang.String,
   *      java.lang.String, java.lang.String)
   * @throws RemoteException
   */
  public void changeLogin(String sessionId, String username, String password) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#getSessionUser(java.lang.String)
   */
  public BusinessDTO getSessionUser(String sessionId) throws RemoteException;

  /**
   * Returns a Map representing all of the roles assigned to the given user,
   * either implicitly or explicitly. The key is the role name. The value is the
   * role display label.
   * 
   * @param sessionId
   * @return Map representing all of the roles assigned to the given user,
   *         either implicitly or explicitly.
   */
  public Map<String, String> getSessionUserRoles(String sessionId) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#loginAnonymous(Locale[])
   * @throws RemoteException
   */
  public String loginAnonymous(Locale[] locales) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#loginAnonymous(java.lang.String, Locale[])
   * @throws RemoteException
   */
  public String loginAnonymous(String string, Locale[] locales) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#logout(java.lang.String)
   * @throws RemoteException
   */
  public void logout(String sessionId) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#newBusiness(java.lang.String,
   *      java.lang.String)
   * @throws RemoteException
   */
  public BusinessDTO newBusiness(String sessionId, String type) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#update(java.lang.String,
   *      com.runwaysdk.business.MutableDTO)
   * @throws RemoteException
   */
  public MutableDTO update(String sessionId, MutableDTO mutableDTO) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#assignMember(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public void assignMember(String sessionId, String userId, String... roles) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#removeMember(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public void removeMember(String sessionId, String userId, String... roles) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#grantAttributePermission(java.lang.String,
   *      java.lang.String, java.lang.String, String...)
   * @throws RemoteExceptio
   */
  public void grantAttributePermission(String sessionId, String actorId, String mdAttributeId, String... operationIds) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#grantTypePermission(java.lang.String,
   *      java.lang.String, java.lang.String, String...)
   * @throws RemoteExceptio
   */
  public void grantTypePermission(String sessionId, String actorId, String mdTypeId, String... operationIds) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#grantMethodPermission(java.lang.String,
   *      java.lang.String, java.lang.String, String...)
   * @throws RemoteExceptio
   */
  public void grantMethodPermission(String sessionId, String actorId, String mdMethodId, String... operationIds) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#revokeTypePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String)
   * @throws RemoteExceptio
   */
  public void revokeTypePermission(String sessionId, String actorId, String mdTypeId, String... operationIds) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#revokeMethodPermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String)
   * @throws RemoteExceptio
   */
  public void revokeMethodPermission(String sessionId, String actorId, String mdMethodId, String... operationIds) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#revokeAttributePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String)
   * @throws RemoteExceptio
   */
  public void revokeAttributePermission(String sessionId, String actorId, String mdAttributeId, String... operationId) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#lock(java.lang.String, java.lang.String)
   * @throws RemoteExceptio
   */
  public ElementDTO lock(String sessionId, String oid) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#unlock(java.lang.String, java.lang.String)
   * @throws RemoteExceptio
   */
  public ElementDTO unlock(String sessionId, String oid) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#deleteChild(java.lang.String,
   *      java.lang.String)
   * 
   * @throws RemoteExceptio
   */
  public void deleteChild(String sessionId, String relationshipId) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#deleteParent(java.lang.String,
   *      java.lang.String)
   * @throws RemoteExceptio
   */
  public void deleteParent(String sessionId, String relationshipId) throws RemoteException;

  public List<RelationshipDTO> getChildRelationships(String sessionId, String oid, String relationshipType) throws RemoteException;

  public List<RelationshipDTO> getParentRelationships(String sessionId, String oid, String relationshipType) throws RemoteException;

  public List<BusinessDTO> getChildren(String sessionId, String oid, String relationshipType) throws RemoteException;

  public List<BusinessDTO> getParents(String sessionId, String oid, String relationshipType) throws RemoteException;

  public BusinessQueryDTO queryBusinesses(String sessionId, BusinessQueryDTO queryDTO) throws RemoteException;

  public StructQueryDTO queryStructs(String sessionId, StructQueryDTO queryDTO) throws RemoteException;

  public ViewQueryDTO queryViews(String sessionId, ViewQueryDTO queryDTO) throws RemoteException;

    public EntityQueryDTO queryEntities(String sessionId, EntityQueryDTO queryDTO) throws RemoteException;

  public RelationshipQueryDTO queryRelationships(String sessionId, RelationshipQueryDTO queryDTO) throws RemoteException;

  public void deleteChildren(String sessionId, String oid, String relationshipType) throws RemoteException;

  public void deleteParents(String sessionId, String oid, String relationshipType) throws RemoteException;

  /**
   * Invokes a method defined by a MdMethod on a Entity in the server.
   * 
   * @param sessionId
   *          The sessionId
   * @param metadata
   *          Metadata containing information about the method to invoke
   * @param mutableDTO
   *          The entityDTO the method is invoked upon
   * @param parameters
   *          The parameter objects
   * @return
   * @throws RemoteException
   */
  public Object invokeMethod(String sessionId, MethodMetaData metadata, MutableDTO mutableDTO, Object[] parameters) throws RemoteException, IOException;

  /**
   * Returns a BusinessDTO containing the values of an enumerated item.
   * 
   * @param sessionId
   *          The sessionId
   * @param enumType
   *          The type of the enumeration
   * @param enumName
   *          The name of the enumerated item
   * @return
   * @throws RemoteException
   */
  public BusinessDTO getEnumeration(String sessionId, String enumType, String enumName) throws RemoteException;

  /**
   * Returns the BusinessDTO of the enumItems for the given MdEnumeration type.
   * 
   * @param sessionId
   *          The session Id
   * @param enumType
   *          The type of the enumeration
   * @return BusinessDTO of the enumItem for the give MdEnumeration type.
   */
  public List<BusinessDTO> getAllEnumerations(String sessionId, String enumType) throws RemoteException;

  /**
   * Returns the BusinessDTO of the enumeration items for the given
   * MdEnumeration type with the given names.
   * 
   * @param sessionId
   *          The session Id
   * @param enumType
   *          The type of the enumeration
   * @param enumNames
   *          names of the enumeration items.
   * @return BusinessDTO of the enumeration items for the give MdEnumeration
   *         type.
   */
  public List<BusinessDTO> getEnumerations(String sessionId, String enumType, String[] enumNames) throws RemoteException;

  public BusinessDTO getVaultFileDTO(String sessionId, String type, String attributeName, String fileId) throws RemoteException;

  public MutableDTO newMutable(String sessionId, String type) throws RemoteException;

  public void importDomainModel(String sessionId, String xml, String xsd) throws RemoteException;

  public void importInstanceXML(String sessionId, String xml) throws RemoteException;

  public RemoteInputStream getFile(String sessionId, String fileId) throws RemoteException;

  public RemoteInputStream getSecureFile(String sessionId, String type, String attributeName, String fileId) throws RemoteException;

  public RemoteInputStream getSecureFile(String sessionId, String fileId) throws RemoteException;

  public BusinessDTO newFile(String sessionId, String path, String filename, String extension, RemoteInputStream stream) throws RemoteException, IOException;

  public BusinessDTO newSecureFile(String sessionId, String filename, String extension, RemoteInputStream stream) throws RemoteException, IOException;

  public EntityQueryDTO getAllInstances(String sessionId, String type, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber) throws RemoteException;
  
  public RemoteInputStream exportExcelFile(String sessionId, String exportMdClassType, String excelListenerBuilderClass, String listenerMethod, String[] params) throws RemoteException;
  
  public RemoteInputStream exportExcelFile(String sessionId, String type, String listenerMethod, String... params) throws RemoteException;

  public RemoteInputStream importExcelFile(String sessionId, RemoteInputStream stream, String type, String listenerMethod, String... params) throws RemoteException, IOException;

  /**
   * @param parentOid
   * @see com.runwaysdk.ClientRequest#getTermAllChildren(java.lang.String,
   *      java.lang.Integer, java.lang.Integer)
   */
  public List<TermAndRelDTO> getTermAllChildren(String sessionId, String parentOid, Integer pageNum, Integer pageSize) throws RemoteException;

  /**
   * @param sessionId
   * @param type
   * @return
   */
  public EntityDTO newDisconnectedEntity(String sessionId, String type) throws RemoteException;

}
