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
package com.runwaysdk.business.rbac;

import java.util.List;
import java.util.Set;

import com.runwaysdk.constants.Constants;
import com.runwaysdk.dataaccess.RelationshipDAOIF;

public interface SingleActorDAOIF extends ActorDAOIF
{
  public static final String CLASS = Constants.SYSTEM_PACKAGE + ".SingleActor";
  
  /**
   * Return all of the roles a user directly participates in 
   * 
   * @return A set of all roles that a user participates in
   */
  public Set<RoleDAOIF> assignedRoles();
  
  /**
   * Return all of the roles a user directly participates in 
   * 
   * @return A set of all roles that a user participates in
   */
  public List<RelationshipDAOIF> assignedRolesRel();

  /**
   * Returns the set of roles directly assigned to a given user as well as the roles
   * that were inherited by the directly assigned roles
   * 
   * @return The set of roles the user participates in
   */
  public Set<RoleDAOIF> authorizedRoles();
  
  /**
   * Returns true if the actor is assigned to the role with the given id, false otherwise.
   * 
   * @return true if the actor is assigned to the role with the given id, false otherwise.
   */
  public boolean hasRole(String roleId);
  
  
  /* (non-Javadoc)
   * @see com.runwaysdk.business.rbac.ActorIF#getPermissions(java.lang.String)
   */
  public Set<RelationshipDAOIF> getAllPermissions();
  
  /**
   * Returns the name of the SingleActor object.  Eg, User name is the name
   * of User objects, while methodName is the name of MethodActor objects.
   * 
   * @return
   */
  public String getSingleActorName();

  /**
   * If the user belongs to the administrator role
   * 
   * @return If the user belongs to the administrator role
   */
  public boolean isAdministrator();
  
  /**
   * @return Flag denoting if login of this class is supported.  Mostly overwritten by sub-types
   */
  public boolean isLoginSupported();
  
  /**
   * Return the maximum number of sessions a user can have open concurrently
   * 
   * @return The maximum number of sessions a user can have open concurrently
   */
  public int getSessionLimit();
  
  /**
   * Return the locale of the user.
   * 
   * @return A string representing the locale of the user.
   */
  public String getLocale();        
}
