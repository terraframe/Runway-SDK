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
package com.runwaysdk.constants;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.AttributeNotificationDTO;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.BusinessQueryDTO;
import com.runwaysdk.business.ClassQueryDTO;
import com.runwaysdk.business.ComponentQueryDTO;
import com.runwaysdk.business.ElementDTO;
import com.runwaysdk.business.EntityDTO;
import com.runwaysdk.business.EntityQueryDTO;
import com.runwaysdk.business.InformationDTO;
import com.runwaysdk.business.MessageDTO;
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
import com.runwaysdk.business.WarningDTO;
import com.runwaysdk.business.ontology.TermAndRelDTO;

public interface ClientRequestIF extends ClientRequestMarker
{
  public static final String CLASS               = Constants.ROOT_PACKAGE + ".ClientRequest";

  public static final String ERROR_MSG_DELIMITER = "##tferrormsg##";

  /**
   * True if messages should be kept between requests to the server, false
   * otherwise.
   * 
   * @param keepMessages
   */
  public void setKeepMessages(boolean keepMessages);

  /**
   * Returns true if messages should be kept between requests to the server,
   * false otherwise.
   * 
   * @return true if messages should be kept between requests to the server,
   *         false otherwise.
   */
  public boolean getKeepMessages();

  /**
   * Returns all messages made in the previous request.
   * 
   * @return all messages made in the previous request.
   */
  public List<MessageDTO> getMessages();

  /**
   * Returns all warnings made in the previous request.
   * 
   * @return all warnings made in the previous request.
   */
  public List<WarningDTO> getWarnings();

  /**
   * Returns all information messages made in the previous request.
   * 
   * @return all information messages made in the previous request.
   */
  public List<InformationDTO> getInformation();

  /**
   * Returns a list of all AttributeNotifications that pertain to the attribute
   * from the component with the given id from the last request.
   * 
   * @param componentId
   * @param attributeName
   * @return list of all AttributeNotifications that pertain to the attribute
   *         from the component with the given id from the last request.
   */
  public List<AttributeNotificationDTO> getAttributeNotifications(String componentId, String attributeName);

  /**
   * Returns a list of all AttributeNotifications that pertain to ANY attribute
   * from the component with the given id from the last request.
   * 
   * @param componentId
   * @return list of all AttributeNotifications that pertain to ANY attribute
   *         from the component with the given id from the last request.
   */
  public List<AttributeNotificationDTO> getAttributeNotifications(String componentId);

  /**
   * Checks if the user with the given session can access the admin screen.
   * 
   */
  public void checkAdminScreenAccess();

  /**
   * Returns a list of BusinessDTO objects that represents all children in a
   * relationship where the parent has the given parentId.
   * 
   * @param parentId
   * @param relationshipType
   * @return
   */
  public List<? extends BusinessDTO> getChildren(String parentId, String relationshipType);

  /**
   * Returns a list of BusinessDTO objects that represent all parents in a
   * relationship where the child has the given childId.
   * 
   * @param childId
   * @param relationshipType
   */
  public List<? extends BusinessDTO> getParents(String childid, String relationshipType);

  /**
   * Returns a list of RelationshipDTO objects (with children information) of
   * the specified relationship type for the given business object id.
   * 
   * @param id
   * @param relationshipType
   * @return
   */
  public List<? extends RelationshipDTO> getChildRelationships(String id, String relationshipType);

  /**
   * Returns a list of RelationshipDTO objects (with parent information) of the
   * specified relationship type for the given business object id.
   * 
   * @param id
   * @param relationshipType
   * @return
   */
  public List<? extends RelationshipDTO> getParentRelationships(String id, String relationshipType);

  /**
   * Returns all children of and their relationship with the given term.
   * 
   * @param parentId
   *          The id of the term to get all children.
   * @param pageNum
   *          Used to break large returns into chunks (pages), this denotes the
   *          page number in the iteration request. Set to 0 to not use pages.
   * @param pageSize
   *          Denotes the number of TermAndRel objects per page. A pageSize of 0
   *          will be treated as infinity.
   * @return A list of TermAndRelDTO objects of size pageSize.
   */
  public List<TermAndRelDTO> getTermAllChildren(String parentId, Integer pageNum, Integer pageSize);

  /**
   * Returns the session id used by the clientRequest to connect to the
   * back-end.
   * 
   * @return session id used by the clientRequest to connect to the back-end.
   */
  public String getSessionId();

  /**
   * Returns the <code>Locale</code> used by the clientRequest.
   * 
   * @return <code>Locale</code> used by the clientRequest.
   */
  public Locale[] getLocales();

  /**
   * Locks an object with the specified id.
   * 
   * @param id
   * @return ElementDTO representing the object that was locked.
   */
  public void lock(ElementDTO elementDTO);

  /**
   * Unlocks an object with the specified id.
   * 
   * @param id
   * @return ElementDTO representing the object that was unlocked.
   */
  public void unlock(ElementDTO elementDTO);

  /**
   * Adds a child to a parent for a given relationship.
   * 
   * @param parentId
   * @param childId
   * @param relationshipType
   * @return RelationshipDTO representing the new relationship.
   */
  public RelationshipDTO addChild(String parentId, String childId, String relationshipType);

  /**
   * Deletes a child from a parent for a given relationship.
   * 
   * @param relationshipId
   */
  public void deleteChild(String relationshipId);

  /**
   * Adds a parent to a child for a given relationship.
   * 
   * @param parentId
   * @param childId
   * @param relationshipType
   * @return RelationshipDTO representing the new relationship.
   */
  public RelationshipDTO addParent(String parentId, String childId, String relationshipType);

  /**
   * Deletes a parent from a child for a given relationship.
   * 
   * @param relationshipId
   */
  public void deleteParent(String relationshipId);

  /**
   * Deletes an entity object.
   * 
   * @param id
   */
  public void delete(String id);

  /**
   * Returns the entity instance associated with the specified id.
   * 
   * @param id
   * @return An EntityDTO object representing the requested instance.
   */
  public MutableDTO get(String id);

  public EntityQueryDTO getAllInstances(String type, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber);

  // public String getLabel();

  /**
   * Creates a new business object from the information in the provided
   * SessionDTO.
   * 
   * @param sessionDTO
   * @return A SessionDTO object representing the newly created entity.
   */
  public void createSessionComponent(SessionDTO sessionDTO);

  /**
   * Creates a new business object from the information in the provided
   * BusinessDTO.
   * 
   * @param businessDTO
   * @return A BusinessDTO object representing the newly created entity.
   */
  public void createBusiness(BusinessDTO businessDTO);

  /**
   * Creates a new business relationship from the information in the provided
   * RelationshipDTO.
   * 
   * @param RelationshipDTO
   * @return A RelationshipDTO object representing the newly created entity.
   */
  public void createRelationship(RelationshipDTO relationshipDTO);

  /**
   * Creates a new struct from the information in the provided StructDTO.
   * 
   * @param struct
   */
  public void createStruct(StructDTO structDTO);

  /**
   * Returns true if the clientRequest is logged in as the anonymouse public
   * user, false otherwise.
   * 
   * @return true if the clientRequest is logged in as the anonymouse public
   *         user, false otherwise.
   */
  public boolean isPublicUser();

  /**
   * Returns true if the clientRequest is loggedIn, false otherwise. This check
   * does not hit the server, and as such is only a "best guess" as to whether
   * or not the user is logged in.
   * 
   * @return true if the clientRequest is logged in, false otherwise.
   */
  public boolean isLoggedIn();

  /**
   * Returns a DTO representing the object of the user who is logged into the
   * session with the given session id.
   * 
   * @param sessionId
   * @return DTO representing the object of the user who is logged into the
   *         session with the given session id.
   */
  public BusinessDTO getSessionUser();

  /**
   * Returns a Map representing all of the roles assigned to the given user,
   * either implicitly or explicitly. The key is the role name. The value is the
   * role display label.
   * 
   * @param sessionId
   * @return Map representing all of the roles assigned to the given user,
   *         either implicitly or explicitly.
   */
  public Map<String, String> getSessionUserRoles();

  /**
   * Returns a new instance of a business object as a BusinessDTO of the
   * specified type.
   * 
   * @param type
   * @return A BusinessDTO object representing the new instance.
   */
  public BusinessDTO newBusiness(String type);

  /**
   * Returns a new instance of a disconnected EntityDTO of the specified type.
   * 
   * @param type
   * @return
   */
  public EntityDTO newDisconnectedEntity(String type);

  /**
   * Returns a new instance of a business object as a BusinessDTO of the
   * specified type that is Generic, but the attributes are type safe.
   * 
   * @param type
   * @return A BusinessDTO object representing the new instance.
   */
  public BusinessDTO newGenericBusiness(String type);

  /**
   * Returns a new struct instance.
   * 
   * @param type
   * @return
   */
  public StructDTO newStruct(String type);

  /**
   * Returns a new instance of a struct object as a StructDTO of the specified
   * type that is Generic, but the attributes are type safe.
   * 
   * @param type
   * @return A StructDTO object representing the new instance.
   */
  public StructDTO newGenericStruct(String type);

  /**
   * Returns a new Mutable
   * 
   * @param type
   * @return
   */
  public MutableDTO newMutable(String type);

  /**
   * Returns a new instance of an entity object as a MutableDTO of the specified
   * type that is Generic, but the attributes are type safe.
   * 
   * @param type
   * @return A MutableDTO object representing the new instance.
   */
  public MutableDTO newGenericMutable(String type);

  /**
   * Returns a new instance of an entity object as a SmartExceptionDTO of the
   * specified type that is Generic, but the attributes are type safe.
   * 
   * @param type
   * @return A SmartExceptionDTO object representing the new instance.
   */
  public SmartExceptionDTO newGenericException(String type);

  /**
   * Updates an existing entity.
   * 
   * @param businessDTO
   * @return An MutableDTO object representing the updated entity.
   */
  public void update(MutableDTO mutableDTO);

  /**
   * Adds the user with the given userId to the role with the given role name.
   * 
   * @param role
   * @param userId
   */
  public void assignMember(String userId, String... roles);

  /**
   * Removes the roles with the given names from the user with the given userId.
   * 
   * @param role
   * @param userId
   */
  public void removeMember(String userId, String... roles);

  /**
   * 
   * @param actorId
   * @param stateId
   * @param operationNames
   */
  public void grantStatePermission(String actorId, String stateId, String... operationNames);

  /**
   * 
   * @param actorId
   * @param mdAttributeId
   * @param operationNames
   */
  public void grantAttributePermission(String actorId, String mdAttributeId, String... operationNames);

  /**
   * 
   * @param actorId
   * @param mdAttributeId
   * @param stateId
   * @param operationNames
   */
  public void grantAttributeStatePermission(String actorId, String mdAttributeId, String stateId, String... operationNames);

  /**
   * Grants permission to a role or user to execute an operation on a type.
   * 
   * @pre: get(mdTypeId)instanceof MdType
   * @param actorId
   *          of the actor to receive the given operation permissions.
   * @param mdTypeId
   *          The id of the type.
   * @param operationNames
   *          names of operation to grant.
   */
  public void grantTypePermission(String actorId, String mdTypeId, String... operationNames);

  /**
   * Grants permission to a role or user to execute an operation on a method.
   * 
   * @pre: get(mdMethodId)instanceof MdMethod
   * @param actorId
   *          of the actor to receive the given operation permissions.
   * @param mdMethodId
   *          The id of the method.
   * @param operationNames
   *          names of operation to grant.
   */
  public void grantMethodPermission(String actorId, String mdMethodId, String... operationNames);

  /**
   * Promotes the object with the given id to the state with the given name.
   * 
   * @param businessDTO
   * @param transitionName
   */
  public void promoteObject(BusinessDTO businessDTO, String transitionName);

  /**
   * Removes an operation permission from a user or role on a type. Does nothing
   * if the operation is not in the permission set.
   * 
   * @pre: get(mdTypeId)instanceof MdType
   * @param actorId
   *          of the actor to revoke the given operation permissions.
   * @param mdTypeId
   *          The id of the type.
   * @param operationNames
   *          names of operation to revoke.
   */
  public void revokeTypePermission(String actorId, String mdTypeId, String... operationNames);

  /**
   * Removes an operation permission from a user or role on a method. Does
   * nothing if the operation is not in the permission set.
   * 
   * @pre: get(mdMethodId)instanceof MdMethod
   * @param actorId
   *          of the actor to revoke the given operation permissions.
   * @param mdMethodId
   *          The id of the type.
   * @param operationNames
   *          names of operation to revoke.
   */
  public void revokeMethodPermission(String actorId, String mdMethodId, String... operationNames);

  /**
   * 
   * @param actorId
   * @param stateId
   * @param operationNames
   */
  public void revokeStatePermission(String actorId, String stateId, String... operationNames);

  /**
   * 
   * @param actorId
   * @param mdAttributeId
   * @param operationNames
   */
  public void revokeAttributePermission(String actorId, String mdAttributeId, String... operationNames);

  /**
   * 
   * @param actorId
   * @param mdAttributeId
   * @param stateId
   * @param operationNames
   */
  public void revokeAttributeStatePermission(String actorId, String mdAttributeId, String stateId, String... operationNames);

  /**
   * 
   * @param type
   * @return ClassQueryDTO
   */
  public ClassQueryDTO getQuery(String type);

  /**
   *
   */
  public ViewQueryDTO queryViews(ViewQueryDTO queryDTO);

  /**
   * 
   * @param queryDTO
   * @return
   */
  public BusinessQueryDTO queryBusinesses(BusinessQueryDTO queryDTO);

  /**
   * 
   * @param queryDTO
   * @return
   */
  public RelationshipQueryDTO queryRelationships(RelationshipQueryDTO queryDTO);

  /**
   * Queries for structs.
   * 
   * @param queryDTO
   * @return
   */
  public StructQueryDTO queryStructs(StructQueryDTO queryDTO);

  /**
   * Returns a ComponentQueryDTO containing the results of an arbitrary query
   * for a given type.
   * 
   * @param ComponentQueryDTO
   * @return ComponentQueryDTO containing the query result.
   */
  public ComponentQueryDTO groovyObjectQuery(ComponentQueryDTO componentQueryDTO);

  /**
   * Returns a ValueQueryDTO containing the results of an arbitrary value query.
   * 
   * @param valueQueryDTO
   * @return ValueQueryDTO containing the query result.
   */
  public ValueQueryDTO groovyValueQuery(ValueQueryDTO valueQueryDTO);

  /**
   * Queries for EntityDTOs
   * 
   * @param queryDTO
   * @return
   */
  public EntityQueryDTO queryEntities(EntityQueryDTO queryDTO);

  /**
   * 
   * @param id
   * @param relationshipType
   */
  public void deleteChildren(String id, String relationshipType);

  /**
   * 
   * @param id
   * @param relationshipType
   */
  public void deleteParents(String id, String relationshipType);

  /**
   * Returns a BusinessDTO which contains the values of an Enumeration.
   * 
   * @param enumType
   *          The fully qualified type name of the enumeration.
   * @param enumName
   *          The name of the enumeration item to retrieve
   * @return
   */
  public BusinessDTO getEnumeration(String enumType, String enumName);

  /**
   * Returns the BusinessDTO of the enumeration items for the given
   * MdEnumeration type with the given names.
   * 
   * @param enumType
   *          The type of the enumeration
   * @param enumNames
   *          names of the enumeration items.
   * @return BusinessDTO of the enumeration items for the give MdEnumeration
   *         type.
   */
  public List<? extends BusinessDTO> getEnumerations(String enumType, String[] enumNames);

  /**
   * Returns the BusinessDTO of the enumeration items for the given
   * MdEnumeration type.
   * 
   * @param sessionId
   *          The session Id
   * @param enumType
   *          The type of the enumeration
   * @return BusinessDTO of the enumeration items for the give MdEnumeration
   *         type.
   */
  public List<? extends BusinessDTO> getAllEnumerations(String enumType);

  /**
   * Invokes a method deifned by a MdMethod on the given MutableDTO in the
   * BusinessLayer
   * 
   * @param metadata
   *          Metadata containing information about the method to invoke
   * @param mutableDTO
   *          MutableDTO on which to invoke the method
   * @param parameters
   *          Parameters to invoke the mehtod with
   * @return Return type of the invoked method or null if there is no object
   *         returned
   */
  public Object invokeMethod(MethodMetaData metadata, MutableDTO mutableDTO, Object[] parameters);

  /**
   * @param fileId
   *          The id of the file to retrieve
   * 
   * @return The content of a file as a {@link InputStream} of data
   */
  public InputStream getFile(String fileId);

  /**
   * Creates a new globally viewable file on the server
   * 
   * @param path
   *          The fully qualified path on the server to put the file
   * @param filename
   *          The name of the file
   * @param extension
   *          The extension of the file
   * @param stream
   *          An {@link InputStream} containing the contents to write
   * @return
   */
  public BusinessDTO newFile(String path, String filename, String extension, InputStream stream);

  /**
   * Returns a {@link BusinessDTO} containing the permissions and information on
   * an AttributeFile for an instance of a given type. The {@link BusinessDTO}
   * is a copy of the information contained in a VaultFileIF on the server.
   * 
   * @param type
   *          Fully qualified type name of the MdType defining the AttributeFile
   * @param attributeName
   *          Name of the file attribute to retrieve
   * @param fileId
   *          The id of the vault file to retrieve
   * @return
   */
  public BusinessDTO getVaultFileDTO(String type, String attributeName, String fileId);

  /**
   * @param attributeName
   *          Name of the file attribute to retrieve
   * @param type
   *          Fully qualified type name of the MdType defining the AttributeFile
   * @param fileId
   *          The id of the file to retrieve
   * 
   * @return The content of a secure file as a {@link InputStream} of data
   */
  public InputStream getSecureFile(String attributeName, String type, String fileId);

  /**
   * @param fileId
   *          The id of the vault file to retrieve
   * 
   * @return The content of a secure file as a {@link InputStream} of data
   */
  public InputStream getSecureFile(String fileId);

  /**
   * Create a new secure file in a file valult on the sever
   * 
   * @param filename
   *          Name of the file to create
   * @param extension
   *          Extension of the file to create
   * @param Stream
   *          containing the contents to be written
   * 
   * @return The {@link BusinessDTO} representing the new vault file which was
   *         written
   */
  public BusinessDTO newSecureFile(String filename, String extension, InputStream stream);

  /**
   * Import the given domain model specified in a xml string into the database.
   * 
   * @param xml
   *          In memory string of the xml to import
   * @param xsd
   *          filename of the xsd, which must be on the classpath
   */
  public void importDomainModel(String xml, String xsd);

  /**
   * Imports instance data as XML.
   * 
   * @param xml
   */
  public void importInstanceXML(String xml);

  /**
   * Imports data in an excel file into the system.
   * 
   * @param stream
   *          Contains the Excel File
   */
  public InputStream importExcelFile(InputStream stream, String type, String listenerMethod, String... params);

  /**
   * Exports a given type to an excel file, which is readable from the input
   * stream
   * 
   * @param type
   *          Fully qualified type to export to excel
   * @return
   */
  public InputStream exportExcelFile(String type, String listenerMethod, String... params);
}
