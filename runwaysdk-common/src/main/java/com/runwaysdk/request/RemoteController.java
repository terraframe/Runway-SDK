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

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.BusinessQueryDTO;
import com.runwaysdk.business.ClassQueryDTO;
import com.runwaysdk.business.ComponentQueryDTO;
import com.runwaysdk.business.ElementDTO;
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


public interface RemoteController extends Remote
{
  public ClassQueryDTO getQuery(String sessionId, String type) throws RemoteException;
  
  public BusinessDTO getUser(String sessionid) throws RemoteException;
  
  /**
   * @see com.runwaysdk.ClientRequest#addChild(java.lang.String,
   *      java.lang.String, java.lang.String,
   *      com.runwaysdk.business.RelationshipDTO)
   * @throws RemoteException
   */
  public RelationshipDTO addChild(String sessionId, String parentOid, String childOid,
      String relationshipType) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#addParent(java.lang.String,
   *      java.lang.String, java.lang.String,
   *      com.runwaysdk.business.RelationshipDTO)
   * @throws RemoteException
   */
  public RelationshipDTO addParent(String sessionId, String parentOid, String childOid,
      String relationshipType) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#delete(java.lang.String,
   *      java.lang.String)
   * @throws RemoteException
   */
  public void delete(String sessionId, String oid) throws RemoteException;
  
  /**
   * @see com.runwaysdk.ClientRequest#get(java.lang.String,
   *      java.lang.String)
   * @throws RemoteException
   */
  public MutableDTO get(String sessionId, String oid) throws RemoteException;

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
  public RelationshipDTO createRelationship(String sessionId, RelationshipDTO relationshipDTO)
      throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#login(java.lang.String,
   *      java.lang.String)
   * @throws RemoteException
   */
  public String login(String username, String password) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#loginAnonymous()
   * @throws RemoteException
   */
  public String loginAnonymous() throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#changeLoginUser(java.lang.String,
   *      java.lang.String, java.lang.String)
   * @throws RemoteException
   */
  public void changeLogin(String sessionId, String username, String password) throws RemoteException;

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
   * @see com.runwaysdk.ClientRequest#grantTypePermission(java.lang.String,
   *      java.lang.String, java.util.List, java.lang.String)
   * @throws RemoteExceptio
   */
  public void grantTypePermission(String sessionId, String actorId, List<String> operationIds,
      String mdTypeId) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#grantStatePermission(java.lang.String,
   *      java.lang.String, java.util.List, java.lang.String)
   * @throws RemoteExceptio
   */
  public void grantStatePermission(String sessionId, String actorId, List<String> operationIds,
      String stateId) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#grantAttributePermission(java.lang.String,
   *      java.lang.String, java.util.List, java.lang.String)
   * @throws RemoteExceptio
   */
  public void grantAttributePermission(String sessionId, String actorId, List<String> operationIds,
      String mdAttributeId) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#grantAttributeStatePermission(java.lang.String,
   *      java.lang.String, java.util.List, java.lang.String, java.lang.String)
   * @throws RemoteExceptio
   */
  public void grantAttributeStatePermission(String sessionId, String actorId, List<String> operationIds,
      String mdAttributeId, String stateId) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#grantTypePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String)
   * @throws RemoteExceptio
   */
  public void grantTypePermission(String sessionId, String actorId, String operationId, String mdTypeId)
      throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#grantStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String)
   * @throws RemoteExceptio
   */
  public void grantStatePermission(String sessionId, String actorId, String operationId, String stateId)
      throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#grantAttributePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String)
   * @throws RemoteExceptio
   */
  public void grantAttributePermission(String sessionId, String actorId, String operationId,
      String mdAttributeId) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#grantAttributeStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String,
   *      java.lang.String)
   * @throws RemoteExceptio
   */
  public void grantAttributeStatePermission(String sessionId, String actorId, String operationId,
      String mdAttributeId, String stateId) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#promoteObject(java.lang.String,
   *      java.lang.String, java.lang.String)
   * @throws RemoteExceptio
   */
  public BusinessDTO promoteObject(String sessionId, BusinessDTO businessDTO, String transitionName)
      throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#revokeTypePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String)
   * @throws RemoteExceptio
   */
  public void revokeTypePermission(String sessionId, String actorId, String operationId, String mdTypeId)
      throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#revokeStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String)
   * @throws RemoteExceptio
   */
  public void revokeStatePermission(String sessionId, String actorId, String operationId, String stateId)
      throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#revokeAttributePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String)
   * @throws RemoteExceptio
   */
  public void revokeAttributePermission(String sessionId, String actorId, String operationId,
      String mdAttributeId) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#revokeAttributeStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String,
   *      java.lang.String)
   * @throws RemoteExceptio
   */
  public void revokeAttributeStatePermission(String sessionId, String actorId, String operationId,
      String mdAttributeId, String stateId) throws RemoteException;
  
  /**
   * @see com.runwaysdk.ClientRequest#revokeTypePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String)
   * @throws RemoteExceptio
   */
  public void revokeTypePermission(String sessionId, String actorId, List<String> operationIds, String mdTypeId)
      throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#revokeStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String)
   * @throws RemoteExceptio
   */
  public void revokeStatePermission(String sessionId, String actorId, List<String> operationId, String stateId)
      throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#revokeAttributePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String)
   * @throws RemoteExceptio
   */
  public void revokeAttributePermission(String sessionId, String actorId, List<String> operationId,
      String mdAttributeId) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#revokeAttributeStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String,
   *      java.lang.String)
   * @throws RemoteExceptio
   */
  public void revokeAttributeStatePermission(String sessionId, String actorId, List<String> operationId,
      String mdAttributeId, String stateId) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#lock(java.lang.String,
   *      java.lang.String)
   * @throws RemoteExceptio
   */
  public ElementDTO lock(String sessionId, String oid) throws RemoteException;

  /**
   * @see com.runwaysdk.ClientRequest#unlock(java.lang.String,
   *      java.lang.String)
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

  public List<?extends RelationshipDTO> getChildRelationships(String sessionId, String oid, String relationshipType) throws RemoteException;

  public List<?extends RelationshipDTO> getParentRelationships(String sessionId, String oid, String relationshipType) throws RemoteException;
  
  public List<?extends BusinessDTO> getChildren(String sessionId, String oid, String relationshipType) throws RemoteException;
  
  public List<?extends BusinessDTO> getParents(String sessionId, String oid, String relationshipType) throws RemoteException;

  public ViewQueryDTO queryViews(String sessionId, ViewQueryDTO queryDTO)  throws RemoteException;
  
  public BusinessQueryDTO queryBusinesses(String sessionId, BusinessQueryDTO queryDTO) throws RemoteException;
  
  public StructQueryDTO queryStructs(String sessionId, StructQueryDTO queryDTO) throws RemoteException;

  public ComponentQueryDTO groovyObjectQuery(String sessionId, ComponentQueryDTO componentQueryDTO) throws RemoteException;
  
  public ValueQueryDTO groovyValueQuery(String sessionId, ValueQueryDTO valueQueryDTO) throws RemoteException;

  public EntityQueryDTO queryEntities(String sessionId, EntityQueryDTO queryDTO) throws RemoteException;
  
  public RelationshipQueryDTO queryRelationships(String sessionId, RelationshipQueryDTO queryDTO) throws RemoteException;
  
  public void deleteChildren(String sessionId, String oid, String relationshipType) throws RemoteException;

  public void deleteParents(String sessionId, String oid, String relationshipType) throws RemoteException;
  
  /**
   * Invokes a method defined by a MdMethod on a Entity in the server.
   * 
   * @param sessionId The sessionId
   * @param metadata Metadata containing information about the method to invoke
   * @param mutableDTO The entityDTO the method is invoked upon
   * @param parameters The parameter objects
   * @return
   * @throws RemoteException
   */
  public Object invokeMethod(String sessionId, MethodMetaData metadata, MutableDTO mutableDTO, Object[] parameters) throws RemoteException;

  /**
   * Returns a BusinessDTO containing the values of an enumerated item.
   * 
   * @param sessionId The sessionId
   * @param enumType The type of the enumeration
   * @param enumName The name of the enumerated item
   * @return
   * @throws RemoteException
   */
  public BusinessDTO getEnumeration(String sessionId, String enumType, String enumName) throws RemoteException;

  /**
   * Retrieves a globally readable file from the server
   * 
   * @param sessionId The oid of the session
   * @param fileId The oid of the WebFile
   * @return Byte array of the file
   * @throws RemoteException
   */
  public Byte[] getFile(String sessionId, String fileId) throws RemoteException;

  /**
   * Retrieves a secure file from the file valut on the server
   * 
   * @param sessionId The oid of the session
   * @param fileId The oid of the VaultFile
   * @return Byte array of the file
   * @throws RemoteException
   */
  public Byte[] getSecureFile(String sessionId, String fileId) throws RemoteException;

  /**
   * Create a new secure file in a file vault on the server
   * 
   * @param sessionId sessionId
   * @param filename Name of the file to create
   * @param extension Extension of the file to create
   * @param bytes The bytes of the file
   * @return A businessDTO representing a VaultFile
   */
  public BusinessDTO newSecureFile(String sessionId, String filename, String extension, Byte[] bytes) throws RemoteException;

  public BusinessDTO newFile(String sessionId, String path, String filename, String extension, Byte[] bytes) throws RemoteException;

  public BusinessDTO getVaultFileDTO(String sessionId, String type, String attributeName, String fileId) throws RemoteException;

  public Byte[] getSecureFile(String sessionId, String type, String attributeName, String fileId) throws RemoteException;
  
  public MutableDTO newMutable(String sessionId, String type) throws RemoteException;
  
  public void importDomainModel(String sessionId, String xml) throws RemoteException;
}
