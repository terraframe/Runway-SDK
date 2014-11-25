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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.healthmarketscience.rmiio.RemoteInputStream;
import com.healthmarketscience.rmiio.RemoteInputStreamClient;
import com.healthmarketscience.rmiio.RemoteInputStreamServer;
import com.healthmarketscience.rmiio.RemoteOutputStream;
import com.healthmarketscience.rmiio.RemoteOutputStreamClient;
import com.healthmarketscience.rmiio.SimpleRemoteInputStream;
import com.healthmarketscience.rmiio.SimpleRemoteOutputStream;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.BusinessQueryDTO;
import com.runwaysdk.business.ClassQueryDTO;
import com.runwaysdk.business.ComponentQueryDTO;
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
import com.runwaysdk.business.ValueQueryDTO;
import com.runwaysdk.business.ViewQueryDTO;
import com.runwaysdk.business.ontology.TermAndRelDTO;
import com.runwaysdk.request.RemoteAdapter;
import com.runwaysdk.util.DTOConversionUtilInfo;

public class RMIAdapter extends UnicastRemoteObject implements RemoteAdapter
{
  /**
   * Generated serial id.
   */
  private static final long serialVersionUID = 3978704188014521740L;

  /**
   * Constructor.
   * 
   * @throws RemoteException
   */
  public RMIAdapter() throws RemoteException
  {
    super();
  }

  /**
   * 
   * @param sessionId
   * @param queryDTO
   * @return
   */
  public ClassQueryDTO getQuery(String sessionId, String type)
  {
    return Facade.getQuery(sessionId, type);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#addChild(java.lang.String,
   *      java.lang.String, java.lang.String,
   *      com.runwaysdk.business.RelationshipDTO)
   */
  public RelationshipDTO addChild(String sessionId, String parentId, String childId, String relationshipType)
  {
    return Facade.addChild(sessionId, parentId, childId, relationshipType);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#addParent(java.lang.String,
   *      java.lang.String, java.lang.String,
   *      com.runwaysdk.business.RelationshipDTO)
   */
  public RelationshipDTO addParent(String sessionId, String parentId, String childId, String relationshipType)
  {
    return Facade.addParent(sessionId, parentId, childId, relationshipType);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#delete(java.lang.String,
   *      java.lang.String)
   */
  public void delete(String sessionId, String id)
  {
    Facade.delete(sessionId, id);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#get(java.lang.String)
   */
  public MutableDTO get(String sessionId, String id)
  {
    return Facade.get(sessionId, id);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#getTermAllChildren(java.lang.String,
   *      java.lang.Integer, java.lang.Integer)
   */
  public List<TermAndRelDTO> getTermAllChildren(String sessionId, String parentId, Integer pageNum, Integer pageSize)
  {
    return Facade.getTermAllChildren(sessionId, parentId, pageNum, pageSize);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#createSessionComponent(java.lang.String,
   *      com.runwaysdk.business.SessionDTO)
   */
  public SessionDTO createSessionComponent(String sessionId, SessionDTO sessionDTO)
  {
    return Facade.createSessionComponent(sessionId, sessionDTO);
  }

  public void checkAdminScreenAccess(String sessionId)
  {
    Facade.checkAdminScreenAccess(sessionId);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#createBusiness(java.lang.String,
   *      com.runwaysdk.business.BusinessDTO)
   */
  public BusinessDTO createBusiness(String sessionId, BusinessDTO businessDTO)
  {
    return Facade.createBusiness(sessionId, businessDTO);
  }

  public RelationshipDTO createRelationship(String sessionId, RelationshipDTO relationshipDTO)
  {
    return Facade.createRelationship(sessionId, relationshipDTO);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#login(java.lang.String,
   *      java.lang.String, java.util.Locale[])
   */
  public String login(String username, String password, Locale[] locales)
  {
    return Facade.login(username, password, locales);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#login(java.lang.String,
   *      java.lang.String, java.lang.String, java.util.Locale[])
   */
  public String login(String username, String password, String dimensionKey, Locale[] locales)
  {
    return Facade.login(username, password, dimensionKey, locales);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#setDimension(java.lang.String,
   *      java.lang.String)
   */
  public void setDimension(String sessionId, String dimensionKey)
  {
    Facade.setDimension(sessionId, dimensionKey);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#login(java.lang.String,
   *      java.lang.String, dimensionKey[])
   */
  public void changeLogin(String sessionId, String username, String password)
  {
    Facade.changeLogin(sessionId, username, password);
  }

  /**
   * @see com.runwaysdk.ClientRequest#getSessionUser(java.lang.String)
   */
  public BusinessDTO getSessionUser(String sessionId)
  {
    return Facade.getSessionUser(sessionId);
  }

  /**
   * @see com.runwaysdk.ClientRequest#getSessionUserRoles()
   */
  public Map<String, String> getSessionUserRoles(String sessionId)
  {
    return Facade.getSessionUserRoles(sessionId);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#loginAnonymous(Locale[])
   */
  public String loginAnonymous(Locale[] locales)
  {
    return Facade.loginAnonymous(locales);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#loginAnonymous(String, Locale[])
   */
  public String loginAnonymous(String dimensionKey, Locale[] locales)
  {
    return Facade.loginAnonymous(dimensionKey, locales);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#logout(java.lang.String)
   */
  public void logout(String sessionId)
  {
    Facade.logout(sessionId);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#newBusiness(java.lang.String,
   *      java.lang.String)
   */
  public BusinessDTO newBusiness(String sessionId, String type)
  {
    return Facade.newBusiness(sessionId, type);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.request.RemoteAdapter#newDisconnectedEntity(java.lang.String,
   * java.lang.String)
   */
  @Override
  public EntityDTO newDisconnectedEntity(String sessionId, String type) throws RemoteException
  {
    return Facade.newDisconnectedEntity(sessionId, type);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#update(java.lang.String,
   *      com.runwaysdk.transport.EntityDTO)
   */
  public MutableDTO update(String sessionId, MutableDTO mutableDTO)
  {
    return Facade.update(sessionId, mutableDTO);
  }

  /**
   * @see com.runwaysdk.ClientRequest#assignMember(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public void assignMember(String sessionId, String userId, String... roles)
  {
    Facade.assignMember(sessionId, userId, roles);
  }

  /**
   * @see com.runwaysdk.ClientRequest#removeMember(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public void removeMember(String sessionId, String userId, String... roles)
  {
    Facade.removeMember(sessionId, userId, roles);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#grantStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, String...)
   */
  public void grantStatePermission(String sessionId, String actorId, String stateId, String... operationNames)
  {
    Facade.grantStatePermission(sessionId, actorId, stateId, operationNames);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#grantAttributePermission(java.lang.String,
   *      java.lang.String, java.lang.String, String...)
   */
  public void grantAttributePermission(String sessionId, String actorId, String mdAttributeId, String... operationNames)
  {
    Facade.grantAttributePermission(sessionId, actorId, mdAttributeId, operationNames);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#grantAttributeStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String, String...)
   */
  public void grantAttributeStatePermission(String sessionId, String actorId, String mdAttributeId, String stateId, String... operationNames)
  {
    Facade.grantAttributeStatePermission(sessionId, actorId, mdAttributeId, stateId, operationNames);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#grantTypePermission(java.lang.String,
   *      java.lang.String, java.lang.String, String...)
   */
  public void grantTypePermission(String sessionId, String actorId, String mdTypeId, String... operationNames)
  {
    Facade.grantTypePermission(sessionId, actorId, mdTypeId, operationNames);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#grantMethodPermission(java.lang.String,
   *      java.lang.String, java.lang.String, String...)
   */
  public void grantMethodPermission(String sessionId, String actorId, String mdMethodId, String... operationNames)
  {
    Facade.grantMethodPermission(sessionId, actorId, mdMethodId, operationNames);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#promoteObject(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public BusinessDTO promoteObject(String sessionId, BusinessDTO businessDTO, String transitionName)
  {
    return Facade.promoteObject(sessionId, businessDTO, transitionName);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#revokeTypePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public void revokeTypePermission(String sessionId, String actorId, String mdTypeId, String... operationNames)
  {
    Facade.revokeTypePermission(sessionId, actorId, mdTypeId, operationNames);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#revokeMethodPermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public void revokeMethodPermission(String sessionId, String actorId, String mdMethodId, String... operationNames)
  {
    Facade.revokeMethodPermission(sessionId, actorId, mdMethodId, operationNames);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#revokeStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public void revokeStatePermission(String sessionId, String actorId, String stateId, String... operationNames)
  {
    Facade.revokeStatePermission(sessionId, actorId, stateId, operationNames);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#revokeAttributePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public void revokeAttributePermission(String sessionId, String actorId, String mdAttributeId, String... operationNames)
  {
    Facade.revokeAttributePermission(sessionId, actorId, mdAttributeId, operationNames);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#revokeAttributeStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String,
   *      java.lang.String...)
   */
  public void revokeAttributeStatePermission(String sessionId, String actorId, String mdAttributeId, String stateId, String... operationNames)
  {
    Facade.revokeAttributeStatePermission(sessionId, actorId, mdAttributeId, stateId, operationNames);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#lock(java.lang.String,
   *      java.lang.String)
   */
  public ElementDTO lock(String sessionId, String id)
  {
    return Facade.lock(sessionId, id);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#unlock(java.lang.String,
   *      java.lang.String)
   */
  public ElementDTO unlock(String sessionId, String id)
  {
    return Facade.unlock(sessionId, id);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#deleteChild(java.lang.String,
   *      java.lang.String)
   */
  public void deleteChild(String sessionId, String relationshipId)
  {
    Facade.deleteChild(sessionId, relationshipId);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#deleteParent(java.lang.String,
   *      java.lang.String)
   */
  public void deleteParent(String sessionId, String relationshipId)
  {
    Facade.deleteParent(sessionId, relationshipId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.request.RemoteAdapter#getChildren(java.lang.String,
   * java.lang.String, java.lang.String)
   */
  public List<BusinessDTO> getChildren(String sessionId, String id, String relationshipType) throws RemoteException
  {
    return Facade.getChildren(sessionId, id, relationshipType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.request.RemoteAdapter#getParents(java.lang.String,
   * java.lang.String, java.lang.String)
   */
  public List<BusinessDTO> getParents(String sessionId, String id, String relationshipType) throws RemoteException
  {
    return Facade.getParents(sessionId, id, relationshipType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.request.RemoteAdapter#getChildRelationships(java.lang
   * .String, java.lang.String, java.lang.String)
   */
  public List<RelationshipDTO> getChildRelationships(String sessionId, String id, String relationshipType) throws RemoteException
  {
    return Facade.getChildRelationships(sessionId, id, relationshipType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.request.RemoteAdapter#getParentRelationships(java.lang
   * .String, java.lang.String, java.lang.String)
   */
  public List<RelationshipDTO> getParentRelationships(String sessionId, String id, String relationshipType) throws RemoteException
  {
    return Facade.getParentRelationships(sessionId, id, relationshipType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.request.RemoteAdapter#queryBusinesses(java.lang.String,
   * com.runwaysdk.transport.QueryDTO)
   */
  public BusinessQueryDTO queryBusinesses(String sessionId, BusinessQueryDTO queryDTO) throws RemoteException
  {
    return Facade.queryBusinesses(sessionId, queryDTO);
  }

  public StructQueryDTO queryStructs(String sessionId, StructQueryDTO queryDTO)
  {
    return Facade.queryStructs(sessionId, queryDTO);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.request.RemoteController#queryViews(java.lang.String,
   * com.runwaysdk.transport.QueryDTO)
   */
  public ViewQueryDTO queryViews(String sessionId, ViewQueryDTO queryDTO) throws RemoteException
  {
    return Facade.queryViews(sessionId, queryDTO);
  }

  /**
   * Returns a ComponentQueryDTO containing the results of an arbitrary query
   * for a given type.
   * 
   * @param sessionId
   * @param ComponentQueryDTO
   * @return ComponentQueryDTO containing the query result.
   */
  public ComponentQueryDTO groovyObjectQuery(String sessionId, ComponentQueryDTO componentQueryDTO)
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
  public ValueQueryDTO groovyValueQuery(String sessionId, ValueQueryDTO valueQueryDTO)
  {
    return Facade.groovyValueQuery(sessionId, valueQueryDTO);
  }

  public EntityQueryDTO queryEntities(String sessionId, EntityQueryDTO queryDTO)
  {
    return Facade.queryEntities(sessionId, queryDTO);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.request.RemoteAdapter#queryRelationships(java.lang.
   * String, com.runwaysdk.transport.QueryDTO)
   */
  public RelationshipQueryDTO queryRelationships(String sessionId, RelationshipQueryDTO queryDTO) throws RemoteException
  {
    return Facade.queryRelationships(sessionId, queryDTO);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.request.RemoteAdapter#deleteChildren(java.lang.String,
   * java.lang.String, java.lang.String)
   */
  public void deleteChildren(String sessionId, String id, String relationshipType)
  {
    Facade.deleteChildren(sessionId, id, relationshipType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.request.RemoteAdapter#deleteParents(java.lang.String,
   * java.lang.String, java.lang.String)
   */
  public void deleteParents(String sessionId, String id, String relationshipType)
  {
    Facade.deleteParents(sessionId, id, relationshipType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.request.RemoteAdapter#invokeMethod(java.lang.String,
   * com.runwaysdk.transport.MutableDTO, java.lang.String, java.lang.String[],
   * java.lang.String[], java.lang.Object[])
   */
  public Object invokeMethod(String sessionId, MethodMetaData metadata, MutableDTO mutableDTO, Object[] parameters) throws RemoteException, IOException
  {
    for (int i = 0; i < parameters.length; i++)
    {
      if (parameters[i] instanceof RemoteInputStream)
      {
        parameters[i] = RemoteInputStreamClient.wrap((RemoteInputStream) parameters[i]);
      }
      else if (parameters[i] instanceof RemoteOutputStream)
      {
        parameters[i] = RemoteOutputStreamClient.wrap((RemoteOutputStream) parameters[i]);
      }
    }

    Object[] returned = (Object[]) Facade.invokeMethod(sessionId, metadata, mutableDTO, parameters);

    if (returned[DTOConversionUtilInfo.RETURN_OBJECT] instanceof InputStream)
    {
      InputStream stream = (InputStream) returned[DTOConversionUtilInfo.RETURN_OBJECT];

      returned[DTOConversionUtilInfo.RETURN_OBJECT] = new SimpleRemoteInputStream(stream);
    }
    else if (returned[DTOConversionUtilInfo.RETURN_OBJECT] instanceof OutputStream)
    {
      OutputStream stream = (OutputStream) returned[DTOConversionUtilInfo.RETURN_OBJECT];

      returned[DTOConversionUtilInfo.RETURN_OBJECT] = new SimpleRemoteOutputStream(stream);
    }

    return returned;
  }

  /**
   * 
   * @see com.runwaysdk.request.RemoteAdapter#getEnumeration(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public BusinessDTO getEnumeration(String sessionId, String enumType, String enumName) throws RemoteException
  {
    return Facade.getEnumeration(sessionId, enumType, enumName);
  }

  /**
   * @see com.runwaysdk.constants.ClientRequestIF#getEnumerations(String,
   *      String, String[])
   */
  public List<BusinessDTO> getEnumerations(String sessionId, String enumType, String[] enumNames)
  {
    return Facade.getEnumerations(sessionId, enumType, enumNames);
  }

  /**
   * @see com.runwaysdk.request.RemoteAdapter#getAllEnumerations(java.lang.String,
   *      java.lang.String)
   */
  public List<BusinessDTO> getAllEnumerations(String sessionId, String enumType)
  {
    return Facade.getAllEnumerations(sessionId, enumType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.request.RemoteAdapter#getVaultFileDTO(java.lang.String,
   * com.runwaysdk.transport.BusinessDTO, java.lang.String)
   */
  public BusinessDTO getVaultFileDTO(String sessionId, String type, String attributeName, String fileId) throws RemoteException
  {
    return Facade.getVaultFileDTO(sessionId, type, attributeName, fileId);
  }

  public StructDTO createStruct(String sessionId, StructDTO structDTO) throws RemoteException
  {
    return Facade.createStruct(sessionId, structDTO);
  }

  public StructDTO newStruct(String sessionId, String type) throws RemoteException
  {
    return Facade.newStruct(sessionId, type);
  }

  public MutableDTO newMutable(String sessionId, String type)
  {
    return Facade.newMutable(sessionId, type);
  }

  public void importDomainModel(String sessionId, String xml, String xsd) throws RemoteException
  {
    Facade.importDomainModel(sessionId, xml, xsd);
  }

  public RemoteInputStream getFile(String sessionId, String fileId) throws RemoteException
  {
    InputStream stream = Facade.getFile(sessionId, fileId);
    RemoteInputStreamServer remoteStream = new SimpleRemoteInputStream(stream);

    return remoteStream.export();
  }

  public RemoteInputStream getSecureFile(String sessionId, String type, String attributeName, String fileId) throws RemoteException
  {
    InputStream stream = Facade.getSecureFile(sessionId, attributeName, type, fileId);
    RemoteInputStreamServer remoteStream = new SimpleRemoteInputStream(stream);

    return remoteStream.export();
  }

  public RemoteInputStream getSecureFile(String sessionId, String fileId) throws RemoteException
  {
    InputStream stream = Facade.getSecureFile(sessionId, fileId);
    RemoteInputStreamServer remoteStream = new SimpleRemoteInputStream(stream);

    return remoteStream.export();
  }

  public BusinessDTO newFile(String sessionId, String path, String filename, String extension, RemoteInputStream stream) throws RemoteException, IOException
  {
    return Facade.newFile(sessionId, path, filename, extension, RemoteInputStreamClient.wrap(stream));
  }

  public BusinessDTO newSecureFile(String sessionId, String filename, String extension, RemoteInputStream stream) throws RemoteException, IOException
  {
    return Facade.newSecureFile(sessionId, filename, extension, RemoteInputStreamClient.wrap(stream));
  }

  public EntityQueryDTO getAllInstances(String sessionId, String type, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber) throws RemoteException
  {
    return Facade.getAllInstances(sessionId, type, sortAttribute, ascending, pageSize, pageNumber);
  }

  public void importInstanceXML(String sessionId, String xml) throws RemoteException
  {
    Facade.importInstanceXML(sessionId, xml);
  }

  public RemoteInputStream exportExcelFile(String sessionId, String type, String listenerMethod, String... params) throws RemoteException
  {
    InputStream stream = Facade.exportExcelFile(sessionId, type, listenerMethod, params);
    RemoteInputStreamServer remoteStream = new SimpleRemoteInputStream(stream);

    return remoteStream.export();
  }

  public RemoteInputStream importExcelFile(String sessionId, RemoteInputStream stream, String type, String listenerMethod, String... params) throws RemoteException, IOException
  {
    InputStream returnStream = Facade.importExcelFile(sessionId, RemoteInputStreamClient.wrap(stream), type, listenerMethod, params);
    RemoteInputStreamServer remoteStream = new SimpleRemoteInputStream(returnStream);

    return remoteStream.export();
  }
}
