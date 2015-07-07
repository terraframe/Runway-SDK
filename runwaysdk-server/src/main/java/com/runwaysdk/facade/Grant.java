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
package com.runwaysdk.facade;

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.business.rbac.ActorDAO;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.OperationManager;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.metadata.TypeTupleDAO;
import com.runwaysdk.dataaccess.metadata.TypeTupleDAOIF;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;


public class Grant
{
  /**
   *
   * @param sessionId
   * @param actorId
   * @param stateId
   * @param operationIds
   */
  @Request(RequestType.SESSION)
  protected static void grantStatePermission(String sessionId, String actorId, String stateId, String... operationIds)
  {
    ActorDAO actor = (ActorDAO)(BusinessDAO.get(actorId).getBusinessDAO());

    List<Operation> operationList = new LinkedList<Operation>();

    for (String operationId : operationIds)
    {
      operationList.add(OperationManager.getOperation(operationId));
    }

    actor.grantPermission(operationList, stateId);
  }

  /**
   *
   * @param sessionId
   * @param actorId
   * @param mdAttributeId
   * @param operationIds
   */
  @Request(RequestType.SESSION)
  protected static void grantAttributePermission(String sessionId, String actorId, String mdAttributeId, String... operationIds)
  {
    ActorDAO actor = (ActorDAO)(BusinessDAO.get(actorId).getBusinessDAO());

    List<Operation> operationList = new LinkedList<Operation>();

    for (String operationId : operationIds)
    {
      operationList.add(OperationManager.getOperation(operationId));
    }

    actor.grantPermission(operationList, mdAttributeId);
  }

  /**
   *
   * @param sessionId
   * @param actorId
   * @param mdAttributeId
   * @param stateId
   * @param operationIds
   */
  @Request(RequestType.SESSION)
  protected static void grantAttributeStatePermission(String sessionId, String actorId, String mdAttributeId, String stateId, String... operationIds)
  {
    ActorDAO actor = (ActorDAO)(BusinessDAO.get(actorId).getBusinessDAO());

    List<Operation> operationList = new LinkedList<Operation>();

    for (String operationId : operationIds)
    {
      operationList.add(OperationManager.getOperation(operationId));
    }

    TypeTupleDAOIF tuple = TypeTupleDAO.findTuple(mdAttributeId, stateId);

    //If the tuple does not exist then create the tuple
    if(tuple == null)
    {
      TypeTupleDAO newTuple = TypeTupleDAO.newInstance();
      newTuple.setStateMaster(stateId);
      newTuple.setMetaData(mdAttributeId);
      newTuple.setStructValue(TypeTupleDAOIF.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Type Permission Tuple");

      newTuple.apply();

      tuple = newTuple;
    }

    actor.grantPermission(operationList, tuple.getId());
  }

  /**
   * Grants permission to a role or user to execute an operation on a type.
   *
   * @pre: get(metadataId)instanceof MetaData
   * @param actorId of the actor to receive the given operation permissions.
   * @param metadataId The id of the metadata.
   * @param operationIds id of operation to grant.
   * @param session id.
   */
  @Request(RequestType.SESSION)
  protected static void grantMetaDataPermission(String sessionId, String actorId, String metadataId, String... operationIds)
  {
    ActorDAO actor = (ActorDAO)(BusinessDAO.get(actorId).getBusinessDAO());

    List<Operation> operationList = new LinkedList<Operation>();

    for (String operationId : operationIds)
    {
      operationList.add(OperationManager.getOperation(operationId));
    }

    actor.grantPermission(operationList, metadataId);
  }

  /**
   * Removes an operation permission from a user or role on a type.
   * Does nothing if the operation is not in the permission set.
   *
   * @pre: get(metadataId)instanceof MetaData
   * @param actorId of the actor to revoke the given operation permissions.
   * @param mdTypeId The id of the type.
   * @param session id.
   * @param operationId id of operation to revoke.
   */
  @Request(RequestType.SESSION)
  protected static void revokeMetaDataPermission(String sessionId, String actorId, String metadataId, String... operationIds)
  {
    ActorDAO actor = (ActorDAO)(BusinessDAO.get(actorId).getBusinessDAO());

    actor.revokePermission(OperationManager.getOperations(operationIds), metadataId);
  }

  /**
   *
   * @param sessionId
   * @param actorId
   * @param stateId
   * @param operationId
   */
  @Request(RequestType.SESSION)
  protected static void revokeStatePermission(String sessionId, String actorId, String stateId, String... operationIds)
  {
    ActorDAO actor = (ActorDAO)(BusinessDAO.get(actorId).getBusinessDAO());

    actor.revokePermission(OperationManager.getOperations(operationIds), stateId);
  }

  /**
   *
   * @param sessionId
   * @param actorId
   * @param mdAttributeId
   * @param operationId
   */
  @Request(RequestType.SESSION)
  protected static void revokeAttributePermission(String sessionId, String actorId, String mdAttributeId, String... operationIds)
  {
    ActorDAO actor = (ActorDAO)(BusinessDAO.get(actorId).getBusinessDAO());

    actor.revokePermission(OperationManager.getOperations(operationIds), mdAttributeId);
  }

  /**
   *
   * @param sessionId
   * @param actorId
   * @param mdAttributeId
   * @param stateId
   * @param operationId
   */
  @Request(RequestType.SESSION)
  protected static void revokeAttributeStatePermission(String sessionId, String actorId, String mdAttributeId, String stateId, String... operationIds)
  {
    ActorDAO actor = (ActorDAO)(BusinessDAO.get(actorId).getBusinessDAO());

    TypeTupleDAOIF tuple = TypeTupleDAO.findTuple(mdAttributeId, stateId);

    if(tuple != null)
    {
      actor.revokePermission(OperationManager.getOperations(operationIds), tuple.getId());

      //If there does not exist anymore permissions involving the tuple
      //then remove it from the database
      List<RelationshipDAOIF> list = tuple.getParents(RelationshipTypes.TYPE_PERMISSION.getType());

      if(list.size() == 0)
      {
        tuple.getBusinessDAO().delete();
      }
    }
  }


}
