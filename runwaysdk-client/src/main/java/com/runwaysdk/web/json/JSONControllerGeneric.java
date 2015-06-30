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
package com.runwaysdk.web.json;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import com.runwaysdk.constants.JSONClientRequestConstants;
import com.runwaysdk.transport.conversion.ConversionExceptionDTO;

public class JSONControllerGeneric
{
  /**
   * @see com.runwaysdk.constants.JSONClientRequestIF#moveBusiness(java.lang.String,
   *      java.lang.String, java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public static String moveBusiness(String sessionId, Map<?, ?> parameters) {
    
    String newParentId = ( (String[]) parameters.get(JSONClientRequestConstants.PARENT_ID.getName()) )[0];
    String childId = ( (String[]) parameters.get(JSONClientRequestConstants.CHILD_ID.getName()) )[0];
    String oldRelationshipId = ( (String[]) parameters.get(JSONClientRequestConstants.RELATIONSHIP_ID.getName()) )[0];
    String newRelationshipType = ( (String[]) parameters.get(JSONClientRequestConstants.RELATIONSHIP_TYPE.getName()) )[0];
    
    return JSONController.moveBusiness(sessionId, newParentId, childId, oldRelationshipId, newRelationshipType);
  }
  
  /**
   * @see com.runwaysdk.constants.JSONClientRequestIF#getTermAllChildren(java.lang.String,
   *      java.lang.String, java.lang.String,
   *      com.runwaysdk.transport.RelationshipDTO)
   */
  public static String getTermAllChildren(String sessionId, Map<?, ?> parameters)
  {
    String parentId = ( (String[]) parameters.get(JSONClientRequestConstants.PARENT_ID.getName()) )[0];
    Integer pageNum = Integer.parseInt(( (String[]) parameters.get(JSONClientRequestConstants.PAGE_NUM.getName()) )[0]);
    Integer pageSize = Integer.parseInt(( (String[]) parameters.get(JSONClientRequestConstants.PAGE_SIZE.getName()) )[0]);

    return JSONController.getTermAllChildren(sessionId, parentId, pageNum, pageSize);
  }
  
  /**
   * @see com.runwaysdk.ClientRequest#addChild(java.lang.String,
   *      java.lang.String, java.lang.String,
   *      com.runwaysdk.transport.RelationshipDTO)
   */
  public static String addChild(String sessionId, Map<?, ?> parameters)
  {
    String parentId = ( (String[]) parameters.get(JSONClientRequestConstants.PARENT_ID.getName()) )[0];
    String childId = ( (String[]) parameters.get(JSONClientRequestConstants.CHILD_ID.getName()) )[0];
    String relationshipType = ( (String[]) parameters.get(JSONClientRequestConstants.RELATIONSHIP_TYPE.getName()) )[0];

    return JSONController.addChild(sessionId, parentId, childId, relationshipType);
  }

  /**
   * @see com.runwaysdk.ClientRequest#addParent(java.lang.String,
   *      java.lang.String, java.lang.String,
   *      com.runwaysdk.transport.RelationshipDTO)
   */
  public static String addParent(String sessionId, Map<?, ?> parameters)
  {
    String parentId = ( (String[]) parameters.get(JSONClientRequestConstants.PARENT_ID.getName()) )[0];
    String childId = ( (String[]) parameters.get(JSONClientRequestConstants.CHILD_ID.getName()) )[0];
    String relationshipType = ( (String[]) parameters.get(JSONClientRequestConstants.RELATIONSHIP_TYPE.getName()) )[0];

    return JSONController.addParent(sessionId, parentId, childId, relationshipType);
  }

  /**
   * @see com.runwaysdk.ClientRequest#delete(java.lang.String,
   *      java.lang.String)
   */
  public static String delete(String sessionId, Map<?, ?> parameters)
  {
    String id = ( (String[]) parameters.get(JSONClientRequestConstants.ID.getName()) )[0];

    return JSONController.delete(sessionId, id);
  }

  /**
   * @see com.runwaysdk.ClientRequest#get(java.lang.String,
   *      java.lang.String)
   */
  public static String get(String sessionId, Map<?, ?> parameters)
  {
    String id = ( (String[]) parameters.get(JSONClientRequestConstants.ID.getName()) )[0];

    return JSONController.get(sessionId, id);
  }

  /**
   * @see com.runwaysdk.ClientRequest#createSessionComponent(java.lang.String,
   *      com.runwaysdk.transport.BusinessDTO)
   */
  public static String createSessionComponent(String sessionId, Map<?, ?> parameters)
  {
    String json = ( (String[]) parameters.get(JSONClientRequestConstants.SESSION_DTO.getName()) )[0];

    return JSONController.createSessionComponent(sessionId, json);
  }

  /**
   * @see com.runwaysdk.ClientRequest#createBusiness(java.lang.String,
   *      com.runwaysdk.transport.BusinessDTO)
   */
  public static String createBusiness(String sessionId, Map<?, ?> parameters)
  {
    String json = ( (String[]) parameters.get(JSONClientRequestConstants.BUSINESS_DTO.getName()) )[0];

    return JSONController.createBusiness(sessionId, json);
  }

  public static String createRelationship(String sessionId, Map<?, ?> parameters)
  {
    String json = ( (String[]) parameters.get(JSONClientRequestConstants.RELATIONSHIP_DTO.getName()) )[0];

    return JSONController.createRelationship(sessionId, json);
  }

  /**
   * @see com.runwaysdk.ClientRequest#getSessionUser()
   */
  public static String getSessionUser(String sessionId)
  {
    return JSONController.getSessionUser(sessionId);
  }

  /**
   * @see com.runwaysdk.ClientRequest#newBusiness(java.lang.String,
   *      java.lang.String)
   */
  public static String newBusiness(String sessionId, Map<?, ?> parameters)
  {
    String type = ( (String[]) parameters.get(JSONClientRequestConstants.TYPE.getName()) )[0];

    return JSONController.newBusiness(sessionId, type);
  }

  /**
   * @see com.runwaysdk.ClientRequest#newBusiness(java.lang.String,
   *      java.lang.String)
   */
  public static String newEntity(String sessionId, Map<?, ?> parameters)
  {
    String type = ( (String[]) parameters.get(JSONClientRequestConstants.TYPE.getName()) )[0];

    return JSONController.newMutable(sessionId, type);
  }

  /**
   * @see com.runwaysdk.ClientRequest#update(java.lang.String,
   *      com.runwaysdk.transport.EntityDTO)
   */
  public static String update(String sessionId, Map<?, ?> parameters)
  {
    String json = ( (String[]) parameters.get(JSONClientRequestConstants.MUTABLE_DTO.getName()) )[0];

    return JSONController.update(sessionId, json);
  }

  /**
   * @see com.runwaysdk.ClientRequest#grantTypePermission(java.lang.String,
   *      java.lang.String, java.util.List, java.lang.String)
   */
  public static String grantTypePermission(String sessionId, Map<?, ?> parameters)
  {
    String actorId = ( (String[]) parameters.get(JSONClientRequestConstants.ACTOR_ID.getName()) )[0];
    String operationNames = ( (String[]) parameters.get(JSONClientRequestConstants.OPERATION_NAMES.getName()) )[0];
    String mdTypeId = ( (String[]) parameters.get(JSONClientRequestConstants.MDTYPE_ID.getName()) )[0];

    String[] opNames = convertJSONStringArrayToJavaArray(operationNames);
    return JSONController.grantTypePermission(sessionId, actorId, mdTypeId, opNames);
  }

  /**
   * @see com.runwaysdk.ClientRequest#grantMethodPermission(java.lang.String,
   *      java.lang.String, java.util.List, java.lang.String)
   */
  public static String grantMethodPermission(String sessionId, Map<?, ?> parameters)
  {
    String actorId = ( (String[]) parameters.get(JSONClientRequestConstants.ACTOR_ID.getName()) )[0];
    String operationNames = ( (String[]) parameters.get(JSONClientRequestConstants.OPERATION_NAMES.getName()) )[0];
    String mdMethodId = ( (String[]) parameters.get(JSONClientRequestConstants.MDMETHOD_ID.getName()) )[0];

    String[] opNames = convertJSONStringArrayToJavaArray(operationNames);
    return JSONController.grantMethodPermission(sessionId, actorId, mdMethodId, opNames);
  }

  /**
   * @see com.runwaysdk.ClientRequest#grantStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, String...)
   */
  public static String grantStatePermission(String sessionId, Map<?, ?> parameters)
  {
    String actorId = ( (String[]) parameters.get(JSONClientRequestConstants.ACTOR_ID.getName()) )[0];
    String operationNames = ( (String[]) parameters.get(JSONClientRequestConstants.OPERATION_NAMES.getName()) )[0];
    String stateId = ( (String[]) parameters.get(JSONClientRequestConstants.STATE_ID.getName()) )[0];

    String[] opNames = convertJSONStringArrayToJavaArray(operationNames);
    return JSONController.grantStatePermission(sessionId, actorId, stateId, opNames);
  }

  public static String importTypes(String sessionId, Map<?, ?> parameters)
  {
    String types = ( (String[]) parameters.get(JSONClientRequestConstants.TYPES.getName()) )[0];

    String[] typesArr = convertJSONStringArrayToJavaArray(types);

    return JSONController.importTypes(sessionId, typesArr, false);
  }

  /**
   * @see com.runwaysdk.ClientRequest#grantAttributePermission(java.lang.String,
   *      java.lang.String, java.lang.String, String...)
   */
  public static String grantAttributePermission(String sessionId, Map<?, ?> parameters)
  {
    String actorId = ( (String[]) parameters.get(JSONClientRequestConstants.ACTOR_ID.getName()) )[0];
    String operationNames = ( (String[]) parameters.get(JSONClientRequestConstants.OPERATION_NAMES.getName()) )[0];
    String mdAttributeId = ( (String[]) parameters.get(JSONClientRequestConstants.MDATTRIBUTE_ID.getName()) )[0];

    String[] opNames = convertJSONStringArrayToJavaArray(operationNames);
    return JSONController.grantAttributePermission(sessionId, actorId, mdAttributeId, opNames);
  }

  /**
   * @see com.runwaysdk.ClientRequest#grantAttributeStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String, String...)
   */
  public static String grantAttributeStatePermission(String sessionId, Map<?, ?> parameters)
  {
    String actorId = ( (String[]) parameters.get(JSONClientRequestConstants.ACTOR_ID.getName()) )[0];
    String operationNames = ( (String[]) parameters.get(JSONClientRequestConstants.OPERATION_NAMES.getName()) )[0];
    String mdAttributeId = ( (String[]) parameters.get(JSONClientRequestConstants.MDATTRIBUTE_ID.getName()) )[0];
    String stateId = ( (String[]) parameters.get(JSONClientRequestConstants.STATE_ID.getName()) )[0];

    String[] opNames = convertJSONStringArrayToJavaArray(operationNames);
    return JSONController.grantAttributeStatePermission(sessionId, actorId, mdAttributeId,
        stateId, opNames);
  }

  /**
   * @see com.runwaysdk.ClientRequest#promoteObject(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public static String promoteObject(String sessionId, Map<?, ?> parameters)
  {
    String businessJSON = ( (String[]) parameters.get(JSONClientRequestConstants.BUSINESS_DTO.getName()) )[0];
    String transitionName = ( (String[]) parameters.get(JSONClientRequestConstants.TRANSITION_NAME.getName()) )[0];

    return JSONController.promoteObject(sessionId, businessJSON, transitionName);
  }

  /**
   * @see com.runwaysdk.ClientRequest#revokeTypePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String)
   */
  public static String revokeTypePermission(String sessionId, Map<?, ?> parameters)
  {
    String actorId = ( (String[]) parameters.get(JSONClientRequestConstants.ACTOR_ID.getName()) )[0];
    String operationIds = ( (String[]) parameters.get(JSONClientRequestConstants.OPERATION_NAMES.getName()) )[0];
    String mdTypeId = ( (String[]) parameters.get(JSONClientRequestConstants.MDTYPE_ID.getName()) )[0];

    String[] opIds = convertJSONStringArrayToJavaArray(operationIds);
    return JSONController.revokeTypePermission(sessionId, actorId, mdTypeId, opIds);
  }

  /**
   * @see com.runwaysdk.ClientRequest#revokeTypePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String)
   */
  public static String revokeMethodPermission(String sessionId, Map<?, ?> parameters)
  {
    String actorId = ( (String[]) parameters.get(JSONClientRequestConstants.ACTOR_ID.getName()) )[0];
    String operationIds = ( (String[]) parameters.get(JSONClientRequestConstants.OPERATION_NAMES.getName()) )[0];
    String mdMethodId = ( (String[]) parameters.get(JSONClientRequestConstants.MDMETHOD_ID.getName()) )[0];

    String[] opIds = convertJSONStringArrayToJavaArray(operationIds);
    return JSONController.revokeMethodPermission(sessionId, actorId, mdMethodId, opIds);
  }

  /**
   * @see com.runwaysdk.ClientRequest#revokeStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String)
   */
  public static String revokeStatePermission(String sessionId, Map<?, ?> parameters)
  {
    String actorId = ( (String[]) parameters.get(JSONClientRequestConstants.ACTOR_ID.getName()) )[0];
    String operationNames = ( (String[]) parameters.get(JSONClientRequestConstants.OPERATION_NAMES.getName()) )[0];
    String stateId = ( (String[]) parameters.get(JSONClientRequestConstants.STATE_ID.getName()) )[0];

    String[] opNames = convertJSONStringArrayToJavaArray(operationNames);
    return JSONController.revokeStatePermission(sessionId, actorId, stateId, opNames);
  }

  /**
   * @see com.runwaysdk.ClientRequest#revokeAttributePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String)
   */
  public static String revokeAttributePermission(String sessionId, Map<?, ?> parameters)
  {
    String actorId = ( (String[]) parameters.get(JSONClientRequestConstants.ACTOR_ID.getName()) )[0];
    String operationNames = ( (String[]) parameters.get(JSONClientRequestConstants.OPERATION_NAMES.getName()) )[0];
    String mdAttributeId = ( (String[]) parameters.get(JSONClientRequestConstants.MDATTRIBUTE_ID.getName()) )[0];

    String[] opNames = convertJSONStringArrayToJavaArray(operationNames);
    return JSONController.revokeAttributePermission(sessionId, actorId, mdAttributeId, opNames);
  }

  /**
   * @see com.runwaysdk.ClientRequest#revokeAttributeStatePermission(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String,
   *      java.lang.String)
   */
  public static String revokeAttributeStatePermission(String sessionId, Map<?, ?> parameters)
  {
    String actorId = ( (String[]) parameters.get(JSONClientRequestConstants.ACTOR_ID.getName()) )[0];
    String operationNames = ( (String[]) parameters.get(JSONClientRequestConstants.OPERATION_NAMES.getName()) )[0];
    String mdAttributeId = ( (String[]) parameters.get(JSONClientRequestConstants.MDATTRIBUTE_ID.getName()) )[0];
    String stateId = ( (String[]) parameters.get(JSONClientRequestConstants.STATE_ID.getName()) )[0];

    String[] opNames = convertJSONStringArrayToJavaArray(operationNames);
    return JSONController.revokeAttributeStatePermission(sessionId, actorId, mdAttributeId, stateId, opNames);
  }

  /**
   * @see com.runwaysdk.ClientRequest#lock(java.lang.String,
   *      java.lang.String)
   */
  public static String lock(String sessionId, Map<?, ?> parameters)
  {
    String id = ( (String[]) parameters.get(JSONClientRequestConstants.ID.getName()) )[0];

    return JSONController.lock(sessionId, id);
  }

  /**
   * @see com.runwaysdk.ClientRequest#unlock(java.lang.String,
   *      java.lang.String)
   */
  public static String unlock(String sessionId, Map<?, ?> parameters)
  {
    String id = ( (String[]) parameters.get(JSONClientRequestConstants.ID.getName()) )[0];

    return JSONController.unlock(sessionId, id);
  }

  /**
   * @see com.runwaysdk.ClientRequest#deleteChild(java.lang.String,
   *      java.lang.String)
   */
  public static String deleteChild(String sessionId, Map<?, ?> parameters)
  {
    String relationshipId = ( (String[]) parameters.get(JSONClientRequestConstants.RELATIONSHIP_ID.getName()) )[0];

    return JSONController.deleteChild(sessionId, relationshipId);
  }

  /**
   * @see com.runwaysdk.ClientRequest#deleteParent(java.lang.String,
   *      java.lang.String)
   */
  public static String deleteParent(String sessionId, Map<?, ?> parameters)
  {
    String relationshipId = ( (String[]) parameters.get(JSONClientRequestConstants.RELATIONSHIP_ID.getName()) )[0];

    return JSONController.deleteParent(sessionId, relationshipId);
  }

  /**
   * @see com.runwaysdk.ClientRequest#getChildren(java.lang.String,
   *      com.runwaysdk.transport.BusinessDTO, java.lang.String)
   */
  public static String getChildren(String sessionId, Map<?, ?> parameters)
  {
    String parentId = ( (String[]) parameters.get(JSONClientRequestConstants.PARENT_ID.getName()) )[0];
    String relationshipType = ( (String[]) parameters.get(JSONClientRequestConstants.RELATIONSHIP_TYPE.getName()) )[0];

    return JSONController.getChildren(sessionId, parentId, relationshipType);
  }

  public static String getChildRelationships(String sessionId, Map<?, ?> parameters)
  {
    String parentId = ( (String[]) parameters.get(JSONClientRequestConstants.PARENT_ID.getName()) )[0];
    String relationshipType = ( (String[]) parameters.get(JSONClientRequestConstants.RELATIONSHIP_TYPE.getName()) )[0];

    return JSONController.getChildRelationships(sessionId, parentId, relationshipType);
  }

  public static String getParentRelationships(String sessionId, Map<?, ?> parameters)
  {
    String childId = ( (String[]) parameters.get(JSONClientRequestConstants.CHILD_ID.getName()) )[0];
    String relationshipType = ( (String[]) parameters.get(JSONClientRequestConstants.RELATIONSHIP_TYPE.getName()) )[0];

    return JSONController.getParentRelationships(sessionId, childId, relationshipType);
  }

  public static String getParents(String sessionId, Map<?, ?> parameters)
  {
    String childId = ( (String[]) parameters.get(JSONClientRequestConstants.CHILD_ID.getName()) )[0];
    String relationshipType = ( (String[]) parameters.get(JSONClientRequestConstants.RELATIONSHIP_TYPE.getName()) )[0];

    return JSONController.getParents(sessionId, childId, relationshipType);
  }

  public static String deleteChildren(String sessionId, Map<?, ?> parameters)
  {
    String parentId = ( (String[]) parameters.get(JSONClientRequestConstants.PARENT_ID.getName()) )[0];
    String relationshipType = ( (String[]) parameters.get(JSONClientRequestConstants.RELATIONSHIP_TYPE.getName()) )[0];

    return JSONController.deleteChildren(sessionId, parentId, relationshipType);
  }

  public static String deleteParents(String sessionId, Map<?, ?> parameters)
  {
    String childId = ( (String[]) parameters.get(JSONClientRequestConstants.CHILD_ID.getName()) )[0];
    String relationshipType = ( (String[]) parameters.get(JSONClientRequestConstants.RELATIONSHIP_TYPE.getName()) )[0];

    return JSONController.deleteParents(sessionId, childId, relationshipType);
  }

  public static String queryBusinesses(String sessionId, Map<?, ?> parameters)
  {
    String queryJSON = ( (String[]) parameters.get(JSONClientRequestConstants.QUERY_DTO.getName()) )[0];

    return JSONController.queryBusinesses(sessionId, queryJSON);
  }
  
  public static String queryViews(String sessionId, Map<?, ?> parameters)
  {
    String queryJSON = ( (String[]) parameters.get(JSONClientRequestConstants.QUERY_DTO.getName()) )[0];
    
    return JSONController.queryViews(sessionId, queryJSON);
  }

  public static String queryEntities(String sessionId, Map<?, ?> parameters)
  {
    String queryJSON = ( (String[]) parameters.get(JSONClientRequestConstants.QUERY_DTO.getName()) )[0];

    return JSONController.queryEntities(sessionId, queryJSON);
  }

  public static String queryStructs(String sessionId, Map<?, ?> parameters)
  {
    String queryJSON = ( (String[]) parameters.get(JSONClientRequestConstants.QUERY_DTO.getName()) )[0];

    return JSONController.queryStructs(sessionId, queryJSON);
  }

  public static String queryRelationships(String sessionId, Map<?, ?> parameters)
  {
    String queryJSON = ( (String[]) parameters.get(JSONClientRequestConstants.QUERY_DTO.getName()) )[0];

    return JSONController.queryRelationships(sessionId, queryJSON);
  }

  public static String invokeMethod(String sessionId, Map<?, ?> parameters) throws IllegalAccessException,
      InvocationTargetException, NoSuchMethodException
  {
    String mutableDTO = ( (String[]) parameters.get(JSONClientRequestConstants.MUTABLE_DTO.getName()) )[0];
    String metadata = ( (String[]) parameters.get(JSONClientRequestConstants.METADATA.getName()) )[0];
    String invokeParameters = ( (String[]) parameters.get(JSONClientRequestConstants.PARAMETERS.getName()) )[0];

    return JSONController.invokeMethod(sessionId, metadata, mutableDTO, invokeParameters);
  }

  public static String getEnumeration(String sessionId, Map<?, ?> parameters)
  {
    String enumType = ( (String[]) parameters.get(JSONClientRequestConstants.ENUM_TYPE.getName()) )[0];
    String enumName = ( (String[]) parameters.get(JSONClientRequestConstants.ENUM_NAME.getName()) )[0];

    return JSONController.getEnumeration(sessionId, enumType, enumName);
  }

  public static String getEnumerations(String sessionId, Map<?, ?> parameters)
  {
    String enumType = ( (String[]) parameters.get(JSONClientRequestConstants.ENUM_TYPE.getName()))[0];
    String enumNames = ( (String[]) parameters.get(JSONClientRequestConstants.ENUM_NAMES.getName()))[0];
    String[] enumNamesArr = convertJSONStringArrayToJavaArray(enumNames);

    return JSONController.getEnumerations(sessionId, enumType, enumNamesArr);
  }

  public static String getAllEnumerations(String sessionId, Map<?, ?> parameters)
  {
    String enumType = ( (String[]) parameters.get(JSONClientRequestConstants.ENUM_TYPE.getName()) )[0];

    return JSONController.getAllEnumerations(sessionId, enumType);
  }

  public static String createStruct(String sessionId, Map<?, ?> parameters)
  {
    String json = ( (String[]) parameters.get(JSONClientRequestConstants.STRUCT_DTO.getName()) )[0];

    return JSONController.createStruct(sessionId, json);
  }

  public static String newStruct(String sessionId, Map<?, ?> parameters)
  {
    String type = ( (String[]) parameters.get(JSONClientRequestConstants.TYPE.getName()) )[0];

    return JSONController.newStruct(sessionId, type);
  }

  public static String getQuery(String sessionId, Map<?, ?> parameters)
  {
    String type = ( (String[]) parameters.get(JSONClientRequestConstants.TYPE.getName()) )[0];

    return JSONController.getQuery(sessionId, type);
  }

  public static String groovyValueQuery(String sessionId, Map<?, ?> parameters)
  {
    String queryJSON = ( (String[]) parameters.get(JSONClientRequestConstants.QUERY_DTO.getName()) )[0];

    return JSONController.groovyValueQuery(sessionId, queryJSON);
  }

  public static String checkAdminScreenAccess(String sessionId)
  {
    return JSONController.checkAdminScreenAccess(sessionId);
  }

  /**
   * Converts a JSON string array into a Java String array.
   *
   * @param jsonArray
   * @return
   */
  private static String[] convertJSONStringArrayToJavaArray(String jsonArray)
  {
    try
    {
      JSONArray array = new JSONArray(jsonArray);
      String[] strArr = new String[array.length()];

      for(int i=0; i<array.length(); i++)
      {
        strArr[i] = array.getString(i);
      }

      return strArr;
    }
    catch (JSONException e)
    {
      throw new ConversionExceptionDTO(e.getMessage(), e);
    }
  }

}
