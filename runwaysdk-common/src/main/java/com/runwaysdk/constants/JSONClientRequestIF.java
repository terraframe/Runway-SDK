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
package com.runwaysdk.constants;

import java.lang.reflect.InvocationTargetException;

import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.RelationshipDTO;

public interface JSONClientRequestIF extends ClientRequestMarker
{
  /**
   * Moves the business from one parent to another by first deleting the oldRelationship, then creating a new relationship between newParent
   * and child. All operations happen within a transaction. This method created with Term (ontology) in mind.
   * 
   * @param sessionId The id of a previously established session.
   * @param newParentId The id of the business that the child will be appended under.
   * @param childId The id of the business that will be either moved or copied.
   * @param oldRelationshipId The id of the relationship that currently exists between parent and child.
   * @param newRelationshipType The type string of the new relationship to create.
   */
  public String moveBusiness(String sessionId, String newParentId, String childId, String oldRelationshipId, String newRelationshipType);
  
  /**
   * Clones the business and appends the clone to newParentId. All operations happen within a transaction. This method created with Term (ontology) in mind.
   * 
   * @param sessionId The id of a previously established session.
   * @param cloneDTO The dto to clone.
   * @param parentId The id of the business that the child will be appended under.
   * @param newRelationshipType The type string of the new relationship to create.
   */
  public String cloneBusinessAndCreateRelationship(String sessionId, String cloneDTOjson, String newParentId, String newRelationshipType);
  
  /**
   * Returns all children of and their relationship with the given term.
   * 
   * @param sessionId The id of a previously established session.
   * @param parentId The id of the term to get all children.
   * @param pageNum Used to break large returns into chunks (pages), this denotes the page number in the iteration request. Set to 0 to not use pages.
   * @param pageSize Denotes the number of TermAndRel objects per page. A pageSize of 0 will be treated as infinity.
   * @return A list of TermAndRel objects of size pageSize.
   */
  public String getTermAllChildren(String sessionId, String parentId, Integer pageNum, Integer pageSize);
  
  /**
   * Checks if the user with the given session has admin screen access.
   *
   * @param sessionId
   */
  public String checkAdminScreenAccess(String sessionId);

  /**
   * Returns a Javascript definition for every type defined in the parameter.
   *
   * @param types
   * @return
   */
  public String importTypes(String sessionId, String[] types, Boolean typesOnly);

  /**
   * Returns the newest last updated date of a type in a list of types
   *
   * @param types
   * @return
   */
  public String getNewestUpdateDate(String sessionId, String[] types, Boolean typesOnly);

  /**
   * Returns a list of BusinessDTO objects that represents all children in a relationship where
   * the parent has the given parentId.
   *
   * @param sessionId
   * @param parentId
   * @param relationshipType
   * @return
   */
  public String getChildren(String sessionId, String parentId, String relationshipType);

  /**
   * Returns a list of BusinessDTO objects that represent all parents in a relationship where the
   * child has the given childId.
   *
   * @param sessionId
   * @param childId
   * @param relationshipType
   */
  public String getParents(String sessionId, String childId, String relationshipType);

  /**
   * Returns a list of RelationshipDTO objects (with children information)
   * of the specified relationhip type for the given business object id.
   *
   * @param sessionId
   * @param id
   * @param relationshipType
   * @return
   */
  public String getChildRelationships(String sessionId, String parentId, String relationshipType);

  /**
   * Returns a list of RelationshipDTO objects (with parent information)
   * of the specified relationhip type for the given business object id.
   *
   * @param sessionId
   * @param id
   * @param relationshipType
   * @return
   */
  public String getParentRelationships(String sessionId, String childId, String relationshipType);

  public String getEnumeration(String sessionId, String enumType, String enumName);

  public String getEnumerations(String sessionId, String enumType, String[] enumName);

  public String getAllEnumerations(String sessionId, String enumType);

  /**
   * Locks an object with the specified id.
   *
   * @param sessionId
   * @param id
   * @return EntityDTO representing the object that was locked.
   */
  public String lock(String sessionId, String id);

  /**
   * Unlocks an object with the specified id.
   *
   * @param sessionId
   * @param id
   * @return EntityDTO representing the object that was unlocked.
   */
  public String unlock(String sessionId, String id);

  /**
   * Adds a child to a parent for a given relationship.
   *
   * @param sessionId
   * @param parentId
   * @param childId
   * @param relationshipType
   * @return RelationshipDTO representing the new relationship.
   */
  public String addChild(String sessionId, String parentId,
      String childId, String relationshipType);

  /**
   * Deletes a child from a parent for a given relationship.
   *
   * @param sessionId
   * @param relationshipId
   */
  public String deleteChild(String sessionId, String relationshipId);

  /**
   * Adds a parent to a child for a given relationship.
   *
   * @param sessionId
   * @param parentId
   * @param childId
   * @param relationshipType
   * @return RelationshipDTO representing the new relationship.
   */
  public String addParent(String sessionId, String parentId,
      String childId, String relationshipType);

  /**
   * Deletes a parent from a child for a given relationship.
   *
   * @param sessionId
   * @param relationshipId
   */
  public String deleteParent(String sessionId, String relationshipId);

  /**
   * Deletes a business object.
   *
   * @param sessionId
   * @param id
   */
  public String delete(String sessionId, String id);

  /**
   * Returns the entity instance associated with the specified id.
   *
   * @param sessionId
   * @param id
   * @return An EntityDTO object representing the requested instance.
   */
  public String get(String sessionId, String id);

  /**
   * Returns an ClassQueryDTO instance (as a json string) that represents
   * a query for the given type.
   *
   * @param sessionId
   * @param type
   * @return
   */
  public String getQuery(String sessionId, String type);

  /**
   * Performs a groovy query for objects.
   */
  public String groovyObjectQuery(String sessionId, String queryDTOJSON);

  /**
   * Performs a groovy query for values.
   */
  public String groovyValueQuery(String sessionId, String queryDTOJSON);

  /**
   * Creates a new SessionDTO.
   *
   * @param sessionId
   * @param sessionDTO
   * @return A SessionDTO object representing the newly created entity.
   */
  public String createSessionComponent(String sessionId, String json);

  /**
   * Creates a new business object from the information in the provided BusinessDTO.
   *
   * @param sessionId
   * @param businessDTO
   * @return A BusinessDTO object representing the newly created entity.
   */
  public String createBusiness(String sessionId, String businessJSON);

  public String createStruct(String sessionId, String structJSON);

  /**
   * Creates a new business relationship from the information in the provided RelationshipDTO.
   *
   * @param sessionId
   * @param relationshipDTO
   * @return A RelationshipDTO object representing the newly created entity.
   */
  public String createRelationship(String sessionId, String relationshipJSON);

  /**
   * Attempts to log a user in with the specified username and password.
   *
   * @param username
   * @param password
   * @param locales
   * @return A String representing the user's session id.
   */
  public String login(String username, String password, String[] locales);

  /**
   * Attempts to log a user in with the specified username and password for the given dimension.
   *
   * @param username
   * @param password
   * @param dimensionKey
   * @param locales
   * @return A String representing the user's session id.
   */
  public String login(String username, String password, String dimensionKey, String[] locales);

  /**
   * Sets the dimension of an existing Session.
   *
   * @param sessionId The id of the Session.
   * @param dimensionKey key of a MdDimension.
   */
  public String setDimension(String sessionId, String dimensionKey);

  /**
   * Creates a session with the public user.
   *
   * @param locales
   * @return id of the new session.
   */
  public String loginAnonymous(String[] locales);

  /**
   * Creates a session with the public user for the given dimension.
   *
   * @param dimensionKey
   * @param locales
   * @return id of the new session.
   */
  public String loginAnonymous(String dimensionKey, String[] locales);

  /**
   * Changes the user for the given session.
   *
   * @param sessionId id of a session.
   * @param username The name of the user.
   * @param password The password of the user.
   */
  public String changeLogin(String sessionId, String username, String password);

  /**
   * Returns the UserDTO for the session with the given session id
   *
   * @param sessionId id of a session.
   */
  public String getSessionUser(String sessionId);

  /**
   * Logs a user out with the specified session id.
   *
   * @param sessionId
   */
  public String logout(String sessionId);

  /**
   * Returns a new instance of a business object as a BusinessDTO of the specified
   * type.
   *
   * @param sessionId
   * @param type
   * @return A BusinessDTO object representing the new instance.
   */
  public String newBusiness(String sessionId, String type);

  /**
   * Returns a new instance of a struct.
   *
   * @param sessionId
   * @param type
   * @return
   */
  public String newStruct(String sessionId, String type);

  /**
   * Returns a new instance of an entity.
   *
   * @param sessionId
   * @param type
   * @return
   */
  public String newMutable(String sessionId, String type);

  /**
   * Updates an existing entity.
   *
   * @param sessionId
   * @param json
   * @return An MutableDTO object representing the updated component.
   */
  public String update(String sessionId, String json);

  /**
   * Grants permission to a role or user to execute an operation on a type.
   *
   * @pre: get(mdTypeId)instanceof MdType
   *
   * @param session
   *          id.
   * @param actorId
   *          of the actor to receive the given operation permissions.
   * @param operationId
   *          id of operation to grant.
   * @param mdTypeId
   *          The id of the type.
   */
  public String grantTypePermission(String sessionId, String actorId, String mdTypeId, String ... operationIds);

  /**
   * Grants permission to a role or user to execute an operation on a method.
   *
   * @pre: get(mdMethodId)instanceof MdMethod
   *
   * @param session
   *          id.
   * @param actorId
   *          of the actor to receive the given operation permissions.
   * @param operationId
   *          id of operation to grant.
   * @param mdMethodId
   *          The id of the method.
   */
  public String grantMethodPermission(String sessionId, String actorId, String mdMethodId, String ... operationIds);

  /**
   *
   * @param sessionId
   * @param actorId
   * @param operationId
   * @param stateId
   */
  public String grantStatePermission(String sessionId, String actorId, String stateId, String ... operationIds);

  /**
   *
   * @param sessionId
   * @param actorId
   * @param operationId
   * @param mdAttributeId
   */
  public String grantAttributePermission(String sessionId, String actorId, String mdAttributeId, String ... operationIds);

  /**
   *
   * @param sessionId
   * @param actorId
   * @param operationId
   * @param mdAttributeId
   * @param stateId
   */
  public String grantAttributeStatePermission(String sessionId, String actorId, String mdAttributeId, String stateId, String ... operationId);

  /**
   * Promotes the object with the given id to the state with the given
   * name.
   *
   * @param sessionid
   * @param objectId
   * @param transitionName
   */
  public String promoteObject(String sessionId, String objectId, String transitionName);

  /**
   * Removes an operation permission from a user or role on a type. Does nothing
   * if the operation is not in the permission set.
   *
   * @pre: get(mdTypeId)instanceof MdType
   *
   * @param session
   *          id.
   * @param actorId
   *          of the actor to revoke the given operation permissions.
   * @param operationId
   *          id of operation to revoke.
   * @param mdTypeId
   *          The id of the type.
   */
  public String revokeTypePermission(String sessionId, String actorId, String mdTypeId, String ... operationIds);

  /**
   * Removes an operation permission from a user or role on a method. Does nothing
   * if the operation is not in the permission set.
   *
   * @pre: get(mdMethodId)instanceof MdMethod
   *
   * @param session
   *          id.
   * @param actorId
   *          of the actor to revoke the given operation permissions.
   * @param operationId
   *          id of operation to revoke.
   * @param mdMethodId
   *          The id of the type.
   */
  public String revokeMethodPermission(String sessionId, String actorId, String mdMethodId, String ... operationIds);

  /**
   *
   * @param sessionId
   * @param actorId
   * @param operationId
   * @param stateId
   */
  public String revokeStatePermission(String sessionId, String actorId, String stateId, String ... operationIds);

  /**
   *
   * @param sessionId
   * @param actorId
   * @param operationId
   * @param mdAttributeId
   */
  public String revokeAttributePermission(String sessionId, String actorId, String mdAttributeId, String ... operationIds);

  /**
   *
   * @param sessionId
   * @param actorId
   * @param operationId
   * @param mdAttributeId
   * @param stateId
   */
  public String revokeAttributeStatePermission(String sessionId, String actorId, String mdAttributeId, String stateId, String ... operationIds);

  /**
   *
   * @param sessionId
   * @param queryDTO
   * @return
   */
  public String queryBusinesses(String sessionId, String queryJSON);

  /**
   *
   * @param sessionId
   * @param queryDTO
   * @return
   */
  public String queryRelationships(String sessionId, String queryJSON);

  /**
   *
   * @param sessionId
   * @param queryDTO
   * @return
   */
  public String queryStructs(String sessionId, String queryJSON);

  /**
   *
   * @param sessionId
   * @param queryDTO
   * @return
   */
  public String queryEntities(String sessionId, String queryJSON);

  /**
   *
   * @param sessionId
   * @param id
   * @param relationshipType
   */
  public String deleteChildren(String sessionId, String id, String relationshipType);

  /**
   *
   * @param sessionId
   * @param id
   * @param relationshipType
   */
  public String deleteParents(String sessionId, String id, String relationshipType);

  /**
   * Invokes a method deifned by a MdMethod on the given EntityDTO in the BusinessLayer
   *
   * @param sessionId SessionId of the session invoking the method
   * @param method metadata
   * @param entityDTO EntityDTO on which to invoke the method
   * @param parameters Parameters to invoke the mehtod with
   * @return Return type of the invoked method or null if there is no object returned
   *
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   * @throws NoSuchMethodException
   */
  public String invokeMethod(String sessionId, String metadata, String entityJSON, String parameters);
}
