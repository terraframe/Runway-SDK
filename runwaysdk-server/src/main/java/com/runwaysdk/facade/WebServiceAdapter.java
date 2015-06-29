/**
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
 */
package com.runwaysdk.facade;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.AxisFault;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.runwaysdk.MessageExceptionDTO;
import com.runwaysdk.ProblemExceptionDTO;
import com.runwaysdk.RunwayExceptionDTO;
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
import com.runwaysdk.business.SmartExceptionDTO;
import com.runwaysdk.business.StructDTO;
import com.runwaysdk.business.StructQueryDTO;
import com.runwaysdk.business.ValueQueryDTO;
import com.runwaysdk.business.ViewQueryDTO;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.metadata.MdDimension;
import com.runwaysdk.transport.conversion.ConversionFacade;
import com.runwaysdk.transport.conversion.dom.MessageExceptionDTOtoDoc;
import com.runwaysdk.transport.conversion.dom.ProblemExceptionDTOtoDoc;
import com.runwaysdk.transport.conversion.dom.RunwayExceptionDTOtoDoc;
import com.runwaysdk.transport.conversion.dom.SmartExceptionDTOtoDoc;
import com.runwaysdk.util.FileIO;

public class WebServiceAdapter
{

  /**
   *
   * @param sessionId
   * @param queryDTO
   * @return
   */
  public static Document getQuery(String sessionId, String type) throws RemoteException
  {
    try
    {
      ClassQueryDTO queryDTO = Facade.getQuery(sessionId, type);

      return ConversionFacade.getDocumentFromQueryDTO(queryDTO);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
    return null;
  }

  public static void checkAdminScreenAccess(String sessionId) throws RemoteException
  {
    try
    {
      Facade.checkAdminScreenAccess(sessionId);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
  }

  /**
   * Creates a new SessionComponent.
   *
   * @param sessionId
   * @param document
   * @return A Document object representing the created Business.
   * @throws ParserConfigurationException
   */

  public static Document createSessionComponent(String sessionId, Document document) throws RemoteException
  {
    try
    {
      Document returnDocument = null;
      SessionDTO sessionDTO = (SessionDTO)ConversionFacade.getComponentDTOIFfromDocument(null, document);

      SessionDTO createdDTO = Facade.createSessionComponent(sessionId, sessionDTO);

      returnDocument = ConversionFacade.getDocumentFromComponentDTO(createdDTO, true);

      return returnDocument;
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, document);
      return null;
    }
  }

  /**
   * Creates a new Business.
   *
   * @param sessionId
   * @param businessDTO
   * @return A Document object representing the created Business.
   * @throws ParserConfigurationException
   */

  public static Document createBusiness(String sessionId, Document document) throws RemoteException
  {
    try
    {
      Document returnDocument = null;
      // convert the Document to a BusinessDTO
      BusinessDTO businessDTO = ConversionFacade.getBusinessDTOFromDocument(null, document);

      BusinessDTO createdDTO = Facade.createBusiness(sessionId, businessDTO);

      returnDocument = ConversionFacade.getDocumentFromComponentDTO(createdDTO, true);

      return returnDocument;
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, document);
    }
    return null;
  }

  public static Document createStruct(String sessionId, Document document) throws RemoteException
  {
    try
    {
      Document returnDocument = null;

      // convert the Document to a StructDTO
      StructDTO structDTO = ConversionFacade.getStructDTOFromDocument(null, document);

      StructDTO createdDTO = Facade.createStruct(sessionId, structDTO);

      returnDocument = ConversionFacade.getDocumentFromComponentDTO(createdDTO, true);

      return returnDocument;
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, document);
    }
    return null;
  }

  public static Document createRelationship(String sessionId, Document document) throws RemoteException
  {
    try
    {
      Document returnDocument = null;

      // convert the Document to a RelationshipDTO
      RelationshipDTO relationshipDTO = ConversionFacade.getRelationshipDTOFromDocument(null, document);

      RelationshipDTO createdDTO;
      createdDTO = Facade.createRelationship(sessionId, relationshipDTO);
      returnDocument = ConversionFacade.getDocumentFromComponentDTO(createdDTO, true);

      return returnDocument;
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, document);
    }
    return null;
  }

  /**
   * Updates an existing entity.
   *
   * @param sessionId
   * @param document
   * @return Document object representing the updated entity.
   * @throws ParserConfigurationException
   */

  public static Document update(String sessionId, Document document) throws RemoteException
  {
    try
    {
      // convert the Document to a BusinessDTO
      MutableDTO mutableDTO = (MutableDTO)ConversionFacade.getComponentDTOIFfromDocument(null, document);

      MutableDTO updatedDTO = Facade.update(sessionId, mutableDTO);
      // convert the BusinessDTO back into a Document and return it.

      return ConversionFacade.getDocumentFromComponentDTO(updatedDTO, true);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, document);
    }
    return null;
  }

  /**
   * Deletes an entity.
   *
   * @param sessionId
   * @param id
   * @param type
   */
  public static void delete(String sessionId, String id) throws RemoteException
  {
    try
    {
      Facade.delete(sessionId, id);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
  }

  /**
   * Returns a new instance of a specified type.
   *
   * @param sessionId
   * @param type
   * @return Document object representing the new instance.
   */
  public static Document newBusiness(String sessionId, String type) throws RemoteException
  {
    try
    {
      BusinessDTO businessDTO = Facade.newBusiness(sessionId, type);

      // convert the BusinessDTO to a Document and return it
      return ConversionFacade.getDocumentFromComponentDTO(businessDTO, true);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
    return null;
  }
  
  public static Document newDisconnectedEntity(String sessionId, String type) throws RemoteException
  {
    try
    {
      EntityDTO entityDTO = Facade.newDisconnectedEntity(sessionId, type);
      
      // convert the BusinessDTO to a Document and return it
      return ConversionFacade.getDocumentFromComponentDTO(entityDTO, true);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
    
    return null;
  }

  public static Document newStruct(String sessionId, String type) throws RemoteException
  {
    try
    {
      StructDTO structDTO = Facade.newStruct(sessionId, type);

      // convert the BusinessDTO to a Document and return it
      return ConversionFacade.getDocumentFromComponentDTO(structDTO, true);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
    return null;
  }

  /**
   *
   * @param sessionId
   * @param type
   * @return
   */
  public static Document newMutable(String sessionId, String type) throws RemoteException
  {
    try
    {
      MutableDTO mutableDTO = Facade.newMutable(sessionId, type);

      return ConversionFacade.getDocumentFromComponentDTO(mutableDTO, true);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
    return null;
  }

  /**
   * Returns the instance associated with the specified id and type.
   *
   * @param sessionId
   * @param id
   * @param type
   * @return Document object representing the new isntance.
   */
  public static Document get(String sessionId, String id) throws RemoteException
  {
    try
    {
      MutableDTO mutableDTO = Facade.get(sessionId, id);

      // convert the Entity into a Document and return it
      return ConversionFacade.getDocumentFromComponentDTO(mutableDTO, true);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
    return null;
  }

  /**
   * Adds a child to a parent for a given relationship.
   *
   * @param sessionId
   * @param parentId
   * @param childId
   * @param relationshipDTO
   * @return Document representing the business relationship.
   */

  public static Document addChild(String sessionId, String parentId, String childId, String relationshipType) throws RemoteException
  {
    try
    {
      RelationshipDTO updatedDTO = Facade.addChild(sessionId, parentId, childId, relationshipType);

      // convert the RelationshipDTO into a Document and return it
      return ConversionFacade.getDocumentFromComponentDTO(updatedDTO, true);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
    return null;
  }

  /**
   * Adds a parent to a child for a given relationship.
   *
   * @param sessionId
   * @param parentId
   * @param childId
   * @param relationshipDTO
   * @return Document representing the business relationship.
   */

  public static Document addParent(String sessionId, String parentId, String childId, String relationshipType) throws RemoteException
  {
    try
    {
      RelationshipDTO updatedDTO = Facade.addParent(sessionId, parentId, childId, relationshipType);

      // convert the RelationshipDTO into a Document and return it
      return ConversionFacade.getDocumentFromComponentDTO(updatedDTO, true);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
    return null;
  }

  /**
   * Deletes a child from a parent in a relationship.
   *
   * @param sessionId
   * @param relationshipId
   */

  public static void deleteChild(String sessionId, String relationshipId) throws RemoteException
  {
    try
    {
      Facade.deleteChild(sessionId, relationshipId);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
  }

  /**
   * Deletes a parent from a child in a relationship.
   *
   * @param sessionId
   * @param relationshipId
   */

  public static void deleteParent(String sessionId, String relationshipId) throws RemoteException
  {
    try
    {
      Facade.deleteParent(sessionId, relationshipId);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
  }

  /**
   * Attempts to log a user in with the specified username and password.
   *
   * @param username
   * @param password
   * @param stringLocales
   * @return the session id of the logged in user
   */
  public static String login(String username, String password, String[] stringLocales) throws RemoteException
  {
    try
    {
      Locale[] locales = ConversionFacade.convertLocales(stringLocales);

      return Facade.login(username, password, locales);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
    return null;
  }

  /**
   * Attempts to log a user in with the specified username and password for the given dimesnion.
   *
   * @param username
   * @param password
   * @param stringLocales
   * @return the session id of the logged in user
   */
  public static String login(String username, String password, String dimensionKey, String[] stringLocales) throws RemoteException
  {
    try
    {
      Locale[] locales = ConversionFacade.convertLocales(stringLocales);

      return Facade.login(username, password, dimensionKey, locales);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
    return null;
  }

  /**
   * Sets the dimension of an existing {@link Session}.
   *
   * @param sessionId The id of the {@link Session}.
   * @param dimensionKey key of a {@link MdDimension}.
   */
  public static void setDimension(String sessionId, String dimensionKey) throws RemoteException
  {
    try
    {
      Facade.setDimension(sessionId, dimensionKey);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
  }

  /**
   * Changes login user of the session with the given sessionid.
   *
   * @param sessionId
   * @param username
   * @param password
   * @return the session id of the logged in user
   */
  public static void changeLogin(String sessionId, String username, String password) throws RemoteException
  {
    try
    {
      Facade.changeLogin(sessionId, username, password);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
  }

  /**
   * @see com.runwaysdk.ClientRequest#getSessionUser()
   */
  public static Document getSessionUser(String sessionId) throws RemoteException
  {
    try
    {
      BusinessDTO userDTO = Facade.getSessionUser(sessionId);
      return ConversionFacade.getDocumentFromComponentDTO(userDTO, true);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
    return null;
  }

  /**
   * @see com.runwaysdk.ClientRequest#getSessionUserRoles()
   */
  public static Document getSessionUserRoles(String sessionId) throws RemoteException
  {
    throw new RuntimeException("Unimplemented Method");
  }

  /**
   * Attempts to log a user in with the specified username and password.
   *
   * @param stringLocales
   * @return session id of the logged in user.
   */
  public static String loginAnonymous(String[] stringLocales) throws RemoteException
  {
    try
    {
      Locale[] locales = ConversionFacade.convertLocales(stringLocales);

      return Facade.loginAnonymous(locales);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
    return null;
  }

  /**
   * Attempts to log a user in with the specified username and password for the given dimension.
   *
   * @param dimensionKey
   * @param stringLocales
   * @return session id of the logged in user.
   */
  public static String loginAnonymous(String dimensionKey, String[] stringLocales) throws RemoteException
  {
    try
    {
      Locale[] locales = ConversionFacade.convertLocales(stringLocales);

      return Facade.loginAnonymous(dimensionKey, locales);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
    return null;
  }

  /**
   * Logs a user out.
   *
   * @param sessionId
   */

  public static void logout(String sessionId) throws RemoteException
  {
    try
    {
      Facade.logout(sessionId);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
  }

  /**
   * Grants permission to a role or user to execute a set of operations on a
   * type.
   *
   * @pre: get(mdTypeId)instanceof MdType
   * @param sessionId.
   * @param actorId
   *          of the actor to receive the given operation permissions.
   * @param mdTypeId
   *          The id of the type.
   * @param operationNames
   *          A list of ids of operations to grant.
   */

  public static void grantTypePermission(String sessionId, String actorId, String mdTypeId, String... operationNames) throws RemoteException
  {
    try
    {
      Facade.grantTypePermission(sessionId, actorId, mdTypeId, operationNames);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
  }

  /**
   * Grants permission to a role or user to execute a set of operations on a
   * method.
   *
   * @pre: get(mdMethodId)instanceof MdType
   * @param sessionId.
   * @param actorId
   *          of the actor to receive the given operation permissions.
   * @param mdMethodId
   *          The id of the type.
   * @param operationNames
   *          A list of ids of operations to grant.
   */

  public static void grantMethodPermission(String sessionId, String actorId, String mdMethodId, String... operationNames) throws RemoteException
  {
    try
    {
      Facade.grantMethodPermission(sessionId, actorId, mdMethodId, operationNames);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
  }

  /**
   * @see com.runwaysdk.ClientRequest#assignMember(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public static void assignMember(String sessionId, String userId, String ... roles) throws RemoteException
  {
    try
    {
      Facade.assignMember(sessionId, userId, roles);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
  }

  /**
   * @see com.runwaysdk.ClientRequest#removeMember(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String...)
   */
  public static void removeMember(String sessionId, String userId, String ... roles) throws RemoteException
  {
    try
    {
      Facade.removeMember(sessionId, userId, roles);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
  }

  /**
   * Grants state permission.
   *
   * @param sessionId
   * @param actorId
   * @param stateId
   * @param operationNames
   */
  public static void grantStatePermission(String sessionId, String actorId, String stateId, String... operationNames) throws RemoteException
  {
    try
    {
      Facade.grantStatePermission(sessionId, actorId, stateId, operationNames);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
  }

  /**
   * Grants attribute permission.
   *
   * @param sessionId
   * @param actorId
   * @param mdAttributeId
   * @param operationNames
   */

  public static void grantAttributePermission(String sessionId, String actorId, String mdAttributeId, String... operationNames) throws RemoteException
  {
    try
    {
      Facade.grantAttributePermission(sessionId, actorId, mdAttributeId, operationNames);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
  }

  /**
   * Grants attribute permission on a state.
   *
   * @param sessionId
   * @param actorId
   * @param mdAttributeId
   * @param stateId
   * @param operationNames
   */

  public static void grantAttributeStatePermission(String sessionId, String actorId, String mdAttributeId, String stateId, String... operationNames) throws RemoteException
  {
    try
    {
      Facade.grantAttributeStatePermission(sessionId, actorId, mdAttributeId, stateId, operationNames);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
  }

  /**
   * Removes an operation permission from a user or role on a type. Does nothing
   * if the operation is not in the permission set.
   *
   * @pre: get(mdTypeId)instanceof MdType
   * @param sessionId.
   * @param actorId
   *          of the actor to revoke the given operation permissions.
   * @param mdTypeId
   *          The id of the type.
   * @param operationId
   *          id of operation to revoke.
   */

  public static void revokeTypePermission(String sessionId, String actorId, String mdTypeId, String... operationNames) throws RemoteException
  {
    try
    {
      Facade.revokeTypePermission(sessionId, actorId, mdTypeId, operationNames);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
  }

  /**
   * Removes an operation permission from a user or role on a method. Does nothing
   * if the operation is not in the permission set.
   *
   * @pre: get(mdMethodId)instanceof MdMethod
   * @param sessionId.
   * @param actorId
   *          of the actor to revoke the given operation permissions.
   * @param mdMethodId
   *          The id of the type.
   * @param operationId
   *          id of operation to revoke.
   */

  public static void revokeMethodPermission(String sessionId, String actorId, String mdMethodId, String... operationNames) throws RemoteException
  {
    try
    {
      Facade.revokeMethodPermission(sessionId, actorId, mdMethodId, operationNames);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
  }

  /**
   * Revokes state permission.
   *
   * @param sessionId
   * @param actorId
   * @param stateId
   * @param operationNames
   */

  public static void revokeStatePermission(String sessionId, String actorId, String stateId, String... operationNames) throws RemoteException
  {
    try
    {
      Facade.revokeStatePermission(sessionId, actorId, stateId, operationNames);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
  }

  /**
   * Revokes attribute permission.
   *
   * @param sessionId
   * @param actorId
   * @param mdAttributeId
   * @param operationNames
   */

  public static void revokeAttributePermission(String sessionId, String actorId, String mdAttributeId, String... operationNames) throws RemoteException
  {
    try
    {
      Facade.revokeAttributePermission(sessionId, actorId, mdAttributeId, operationNames);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
  }

  /**
   * Revokes attribute permission on a state.
   *
   * @param sessionId
   * @param actorId
   * @param mdAttributeId
   * @param stateId
   * @param operationNames
   */

  public static void revokeAttributeStatePermission(String sessionId, String actorId, String mdAttributeId, String stateId, String... operationNames) throws RemoteException
  {
    try
    {
      Facade.revokeAttributeStatePermission(sessionId, actorId, mdAttributeId, stateId, operationNames);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
  }

  /**
   * Promotes the object with the given id to the state with the given name.
   *
   * @param sessionid
   * @param objectId
   * @param transitionName
   */

  public static Document promoteObject(String sessionid, Document businessDoc, String transitionName) throws RemoteException
  {
    try
    {
      BusinessDTO businessDTO = (BusinessDTO)ConversionFacade.getComponentDTOIFfromDocument(null, businessDoc);

      businessDTO = Facade.promoteObject(sessionid, businessDTO, transitionName);

      return ConversionFacade.getDocumentFromComponentDTO(businessDTO, true);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
    return null;
  }

  /**
   * Locks a business entity with the specified id.
   *
   * @param sessionId
   * @param id
   *          The id of the business entity to lock.
   * @return An Document representing the locked Entity.
   */

  public static Document lock(String sessionId, String id) throws RemoteException
  {
    try
    {
      EntityDTO entityDTO = Facade.lock(sessionId, id);

      return ConversionFacade.getDocumentFromComponentDTO(entityDTO, true);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
    return null;
  }

  /**
   * Unlocks a business entity with the specified id.
   *
   * @param sessionId
   * @param id
   *          The id of the business entity to unlock.
   * @return A Document representing the unlocked Entity.
   */

  public static Document unlock(String sessionId, String id) throws RemoteException
  {
    try
    {
      EntityDTO entityDTO = Facade.unlock(sessionId, id);

      // convert the Entity to a Document and convert it.
      return ConversionFacade.getDocumentFromComponentDTO(entityDTO, true);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
    return null;
  }

  public static Document getChildren(String sessionId, String id, String relationshipType) throws RemoteException
  {
    try
    {
      List<BusinessDTO> businessDTOs = Facade.getChildren(sessionId, id, relationshipType);

      BusinessDTO[] returnArray = new BusinessDTO[businessDTOs.size()];
      businessDTOs.toArray(returnArray);

      return ConversionFacade.getDocumentFromObject(returnArray, true);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
    return null;
  }

  public static Document getParents(String sessionId, String id, String relationshipType) throws RemoteException
  {
    try
    {
      List<BusinessDTO> businessDTOs = Facade.getParents(sessionId, id, relationshipType);

      BusinessDTO[] returnArray = new BusinessDTO[businessDTOs.size()];
      businessDTOs.toArray(returnArray);

      return ConversionFacade.getDocumentFromObject(returnArray, true);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
    return null;
  }

  public static Document getChildRelationships(String sessionId, String id, String relationshipType) throws RemoteException
  {
    try
    {
      List<RelationshipDTO> relationshipDTOs = Facade.getChildRelationships(sessionId, id, relationshipType);

      RelationshipDTO[] returnArray = new RelationshipDTO[relationshipDTOs.size()];
      relationshipDTOs.toArray(returnArray);

      return ConversionFacade.getDocumentFromObject(returnArray, true);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
    return null;
  }

  public static Document getParentRelationships(String sessionId, String id, String relationshipType) throws RemoteException
  {
    try
    {
      List<RelationshipDTO> relationshipDTOs = Facade.getParentRelationships(sessionId, id, relationshipType);

      RelationshipDTO[] returnArray = new RelationshipDTO[relationshipDTOs.size()];
      relationshipDTOs.toArray(returnArray);

      return ConversionFacade.getDocumentFromObject(returnArray, true);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
    return null;
  }

  public static Document queryBusinesses(String sessionId, Document document) throws RemoteException
  {
    try
    {
      BusinessQueryDTO queryDTO = (BusinessQueryDTO) ConversionFacade.getQueryDTOFromDocument(null, document, false);
      queryDTO = Facade.queryBusinesses(sessionId, queryDTO);
      return ConversionFacade.getDocumentFromQueryDTO(queryDTO);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, document);
    }
    return null;
  }

  public static Document queryStructs(String sessionId, Document document) throws RemoteException
  {
    try
    {
      StructQueryDTO queryDTO = (StructQueryDTO) ConversionFacade.getQueryDTOFromDocument(null, document, false);
      queryDTO = Facade.queryStructs(sessionId, queryDTO);
      return ConversionFacade.getDocumentFromQueryDTO(queryDTO);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, document);
    }
    return null;
  }

  public static Document queryViews(String sessionId, Document document) throws RemoteException
  {
    try
    {
      ViewQueryDTO queryDTO = (ViewQueryDTO) ConversionFacade.getQueryDTOFromDocument(null, document, false);
      queryDTO = Facade.queryViews(sessionId, queryDTO);
      return ConversionFacade.getDocumentFromQueryDTO(queryDTO);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, document);
    }
    return null;
  }


  public static Document queryEntities(String sessionId, Document document) throws RemoteException
  {
    try
    {
      EntityQueryDTO queryDTO = (EntityQueryDTO) ConversionFacade.getQueryDTOFromDocument(null, document, false);
      queryDTO = Facade.queryEntities(sessionId, queryDTO);
      return ConversionFacade.getDocumentFromQueryDTO(queryDTO);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, document);
    }
    return null;
  }

  /**
   * Returns a ComponentQueryDTO containing the results of an arbitrary query for a given type.
   * @param sessionId
   * @param ComponentQueryDTO
   * @return ComponentQueryDTO containing the query result.
   */
  public static Document groovyObjectQuery(String sessionId, Document document) throws RemoteException
  {
    try
    {
      ComponentQueryDTO componentQueryDTO =  ConversionFacade.getQueryDTOFromDocument(null, document, false);
      componentQueryDTO = Facade.groovyObjectQuery(sessionId, componentQueryDTO);
      return ConversionFacade.getDocumentFromQueryDTO(componentQueryDTO);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, document);
    }
    return null;
  }

  /**
   * Returns a ValueQueryDTO containing the results of an arbitrary value query.
   * @param sessionId
   * @param valueQueryDTO
   * @return ValueQueryDTO containing the query result.
   */
  public static Document groovyValueQuery(String sessionId, Document document) throws RemoteException
  {
    try
    {
      ValueQueryDTO valueQueryDTO = (ValueQueryDTO) ConversionFacade.getQueryDTOFromDocument(null, document, false);
      valueQueryDTO = Facade.groovyValueQuery(sessionId, valueQueryDTO);
      return ConversionFacade.getDocumentFromQueryDTO(valueQueryDTO);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, document);
    }
    return null;
  }

  public static Document queryRelationships(String sessionId, Document document) throws RemoteException
  {
    try
    {
      RelationshipQueryDTO queryDTO = (RelationshipQueryDTO) ConversionFacade.getQueryDTOFromDocument(null, document, false);
      queryDTO = Facade.queryRelationships(sessionId, queryDTO);
      return ConversionFacade.getDocumentFromQueryDTO(queryDTO);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, document);
    }
    return null;
  }

  public static void deleteChildren(String sessionId, String id, String relationshipType) throws RemoteException
  {
    try
    {
      Facade.deleteChildren(sessionId, id, relationshipType);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
  }

  public static void deleteParents(String sessionId, String id, String relationshipType) throws RemoteException
  {
    try
    {
      Facade.deleteParents(sessionId, id, relationshipType);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
  }

  public static Document invokeMethod(String sessionId, Document metaDoc, Document mutableObjectDocument, Document[] documents) throws RemoteException
  {
    try
    {
      MutableDTO mutableDTO = null;
      Object[] parameters = ConversionFacade.getObjectArrayFromDocuments(null, documents);
      MethodMetaData metadata = ConversionFacade.getMethodMetaDataFromDocument(metaDoc);

      if(mutableObjectDocument != null)
      {
        mutableDTO = (MutableDTO)ConversionFacade.getComponentDTOIFfromDocument(null, mutableObjectDocument);
      }

      Object output = Facade.invokeMethod(sessionId, metadata, mutableDTO, parameters);

      return ConversionFacade.getDocumentFromObject(output, true);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, metaDoc);
    }
    return null;
  }

  public static Document getEnumeration(String sessionId, String enumType, String enumName) throws RemoteException
  {
    try
    {
      Object output = Facade.getEnumeration(sessionId, enumType, enumName);

      return ConversionFacade.getDocumentFromObject(output, true);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
    return null;
  }

  public static Document getEnumerations(String sessionId, String enumType, String[] enumNames) throws RemoteException
  {
    try
    {
      List<BusinessDTO> businessDTOs = Facade.getEnumerations(sessionId, enumType, enumNames);

      BusinessDTO[] returnArray = new BusinessDTO[businessDTOs.size()];
      businessDTOs.toArray(returnArray);

      return ConversionFacade.getDocumentFromObject(returnArray, true);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
    return null;
  }

  public static Document getAllEnumerations(String sessionId, String enumType) throws RemoteException
  {
    try
    {
      List<BusinessDTO> businessDTOs = Facade.getAllEnumerations(sessionId, enumType);

      BusinessDTO[] returnArray = new BusinessDTO[businessDTOs.size()];
      businessDTOs.toArray(returnArray);

      return ConversionFacade.getDocumentFromObject(returnArray, true);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
    return null;
  }

  public static Document getFile(String sessionId, String fileId) throws RemoteException, IOException
  {
    try
    {
      Byte[] bytes = FileIO.getBytesFromStream(Facade.getFile(sessionId, fileId));

      return ConversionFacade.getDocumentFromObject(bytes, true);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
    return null;
  }

  public static Document newFile(String sessionId, String path, String filename, String extension, Document document) throws RemoteException
  {
    try
    {
      Byte[] bytes = (Byte[]) ConversionFacade.getObjectFromDocument(null, document);
      InputStream stream = new ByteArrayInputStream(FileIO.convertFromBytes(bytes));
      BusinessDTO fileDTO = Facade.newFile(sessionId, path, filename, extension, stream);

      return ConversionFacade.getDocumentFromObject(fileDTO, true);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, document);
    }
    return null;
  }

  public static Document getSecureFile(String sessionId, String fileId) throws RemoteException, IOException
  {
    try
    {
      InputStream stream = Facade.getSecureFile(sessionId, fileId);
      Byte[] bytes = FileIO.getBytesFromStream(stream);

      return ConversionFacade.getDocumentFromObject(bytes, true);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
    return null;
  }

  public static Document getSecureFile(String sessionId, String attributeName, String type, String fileId) throws RemoteException, IOException
  {
    try
    {
      InputStream stream = Facade.getSecureFile(sessionId, attributeName, type, fileId);
      Byte[] bytes = FileIO.getBytesFromStream(stream);

      return ConversionFacade.getDocumentFromObject(bytes, true);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
    return null;
  }

  public static Document getVaultFileDTO(String sessionId, String type, String attributeName, String fileId) throws RemoteException
  {
    try
    {
      BusinessDTO returnDTO = Facade.getVaultFileDTO(sessionId, type, attributeName, fileId);

      return ConversionFacade.getDocumentFromObject(returnDTO, true);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
    return null;
  }

  public static Document newSecureFile(String sessionId, String filename, String extension, Document document) throws RemoteException
  {
    try
    {
      Byte[] bytes = (Byte[]) ConversionFacade.getObjectFromDocument(null, document);
      InputStream stream = new ByteArrayInputStream(FileIO.convertFromBytes(bytes));

      BusinessDTO fileDTO = Facade.newSecureFile(sessionId, filename, extension, stream);

      return ConversionFacade.getDocumentFromObject(fileDTO, true);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
    return null;
  }

  public static void importDomainModel(String sessionId, String xml, String xsd) throws RemoteException
  {
    try
    {
      Facade.importDomainModel(sessionId, xml, xsd);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
  }

  public static void buildAxisFault(RuntimeException e, Document document) throws RemoteException
  {
    if (e instanceof MessageExceptionDTO   ||
        e instanceof SmartExceptionDTO     ||
        e instanceof RunwayExceptionDTO ||
        e instanceof ProblemExceptionDTO)
    {
      AxisFault axisFault = new AxisFault(e.getMessage());

      Document exceptionDocument;

      if (document == null)
      {
        exceptionDocument = ConversionFacade.initializeDocument();
      }
      else
      {
        exceptionDocument = document;
      }

      Element element = null;

      if (e instanceof MessageExceptionDTO)
      {
        MessageExceptionDTO messageExceptionDTO = (MessageExceptionDTO)e;
        MessageExceptionDTOtoDoc converter = new MessageExceptionDTOtoDoc(messageExceptionDTO, exceptionDocument);
        element = converter.populate();
      }
      else if (e instanceof SmartExceptionDTO)
      {
        SmartExceptionDTOtoDoc smartExceptionDTOtoDoc = new SmartExceptionDTOtoDoc((SmartExceptionDTO) e, exceptionDocument, false);

        element = (Element)smartExceptionDTOtoDoc.populate();
      }
      else if (e instanceof RunwayExceptionDTO)
      {
        RunwayExceptionDTOtoDoc runwayExceptionDTOtoDoc = new RunwayExceptionDTOtoDoc((RunwayExceptionDTO)e, exceptionDocument);
        element = runwayExceptionDTOtoDoc.populate();
      }
      else if (e instanceof ProblemExceptionDTO)
      {
        ProblemExceptionDTO problemExceptionDTO = (ProblemExceptionDTO)e;

        ProblemExceptionDTOtoDoc problemExceptionDTOtoDoc = new ProblemExceptionDTOtoDoc(problemExceptionDTO, exceptionDocument);
        element = problemExceptionDTOtoDoc.populate();
      }
      axisFault.addFaultDetail(element);

      throw axisFault;
    }
    else
    {
      throw e;
    }
  }

  public static Document getAllInstances(String sessionId, String type, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber) throws RemoteException
  {
    try
    {
      EntityQueryDTO query = Facade.getAllInstances(sessionId, type, sortAttribute, ascending, pageSize, pageNumber);

      // convert the Entity into a Document and return it
      return ConversionFacade.getDocumentFromQueryDTO(query);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
    return null;
  }

  public static void importInstanceXML(String sessionId, String xml) throws RemoteException
  {
    try
    {
      Facade.importInstanceXML(sessionId, xml);
    }
    catch (RuntimeException e)
    {
      buildAxisFault(e, null);
    }
  }
}
