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
package com.runwaysdk.business.rbac;

import java.util.Set;

import com.runwaysdk.constants.Constants;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MetadataDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.session.PermissionMap;



public interface ActorDAOIF extends BusinessDAOIF
{
  
  /**
   * The type of the users class.
   */
  public static final String CLASS = Constants.SYSTEM_PACKAGE + "."+"Actor";
          
  /**
   * The operations enumeration attribute on the permission relationship
   */
  public static final String OPERATION_ATTR = "operations";

  /**
   * Returns all operations this ActorIF has permission to use on the given MetaDataIF
   *  
   * @param metadata MetaDataIF entity
   * @return A set of operations an actor has on an MetaDataIF entity
   */
  public Set<Operation> getAllPermissions(MetadataDAOIF metadata);
  
  /**
   * Returns all operations which this ActorIF has been explicitly assigned permission for on the given MetaDataIF
   *  
   * @param metadata MetaDataIF entity
   * @return A set of explicity operations an actor has on an MetaDataIF entity
   */
  public Set<Operation> getAssignedPermissions(MetadataDAOIF metadata);
  
  /**
   * Returns a list of all the permissions Relationships defined for this actor,
   * including all permissions which are inherited from super actors or from assignments.
   * The child of the returned Relationship is MetaDataIF entity of the permission.
   * The parent of the returned Relationship is this ActorIF.
   * 
   * @return A list of permission Relationship
   */
  public Set<RelationshipDAOIF> getAllPermissions();    

  /**
   * Returns a list of all the permissions Relationships defined directly for this actor.
   * The child of the returned Relationship is MetaDataIF entity of the permission.
   * The parent of the returned Relationship is this ActorIF.
   * 
   * @return A list of permission Relationship
   */
  public Set<RelationshipDAOIF> getPermissions();
  
  public PermissionMap getOperations();
}
