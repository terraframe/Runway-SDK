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
package com.runwaysdk.business.rbac;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.constants.UserInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.session.PermissionMap;
import com.runwaysdk.session.SingleActorPermissionBuilder;

public class SingleActorDAO extends ActorDAO implements SingleActorDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 7373888478904954019L;

  public SingleActorDAO()
  {
    super();
  }
  
  public SingleActorDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }
  
  @Override
  public String apply()
  {
    String oid = super.apply();

    // update role assignment relationship keys
    List<RelationshipDAOIF> childAssignments = this.assignedRolesRel();
    for (RelationshipDAOIF relationshipDAOIF : childAssignments)
    {
      RelationshipDAO inherit = relationshipDAOIF.getRelationshipDAO();
      inherit.setKey(RoleDAO.buildAssignmentsKey(inherit));
      inherit.apply();
    }
    
    
    return oid;
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String, java.lang.String)
   */
  public static SingleActorDAOIF get(String oid)
  {
    return (SingleActorDAOIF) BusinessDAO.get(oid);
  }
  
  /**
   * Return all of the roles a user directly participates in 
   * 
   * @return A set of all roles that a user participates in
   */
  public Set<RoleDAOIF> assignedRoles()
  {
    Set<RoleDAOIF> roles = new TreeSet<RoleDAOIF>();
    
    //Get all of the instances of the user-assignment relationship
    List<RelationshipDAOIF> list = this.getChildren(RelationshipTypes.ASSIGNMENTS.getType());
    
    for(RelationshipDAOIF relationship : list)
    {
      RoleDAOIF child = (RoleDAOIF) relationship.getChild();
      
      roles.add(child);
    }
        
    return roles;
  }
  
  /**
   * Return all of the roles a user directly participates in 
   * 
   * @return A set of all roles that a user participates in
   */
  public List<RelationshipDAOIF> assignedRolesRel()
  {   
    //Get all of the instances of the user-assignment relationship
    return this.getChildren(RelationshipTypes.ASSIGNMENTS.getType());
  }

  /**
   * Returns true if the actor is assigned to the role with the given oid, false otherwise.
   * 
   * @param roleId
   * 
   * @return true if the actor is assigned to the role with the given oid, false otherwise.
   */
  public boolean hasRole(String roleId)
  {
    for(RoleDAOIF roleIF : this.assignedRoles())
    {
      if (roleIF.getOid().equals(roleId))
      {
        return true;
      }
    }
    
    return false;
  }
  
  /**
   * Returns the set of roles directly assigned to a given user as well as the roles
   * that were inherited by the directly assigned roles
   * 
   * @return The set of roles the user participates in
   */
  public Set<RoleDAOIF> authorizedRoles()
  {
    Set<RoleDAOIF> set = new TreeSet<RoleDAOIF>();
    
    //Get a list of all the directly assigned roles of a user
    Set<RoleDAOIF> roleSet = this.assignedRoles();
    
    for(RoleDAOIF role : roleSet)
    {
      set.add(role);
      Set<RoleDAOIF> inherited = role.getSuperRoles();
      //Get the inherited Roles of each directly assigned role
      set.addAll(inherited);
    }
        
    return set;
  }  
  
  public PermissionMap getOperations()
  {
    return new SingleActorPermissionBuilder(this).build();
  }
  
  
  /**
   * Returns a list of all the objects in which the
   * user has been granted permission.
   * 
   * @return A list of Metadata ids the user has permissions for
   */
  public Set<RelationshipDAOIF> getAllPermissions()
  {      
    //Get the permissions for the individual user
    Set<RelationshipDAOIF> typeIds = super.getAllPermissions();    
    Set<RoleDAOIF> set = this.assignedRoles();
   
    //Get the permissions for all the roles the user is part of
    for(RoleDAOIF role : set)
    {
      typeIds.addAll(role.getAllPermissions());
    }
   
    return typeIds;
  }  
  
  /* (non-Javadoc)
   * @see com.runwaysdk.business.rbac.UserIF#isAdministrator()
   */
  public boolean isAdministrator()
  {
    RoleDAOIF admin = RoleDAO.findRole(RoleDAOIF.ADMIN_ROLE);

    Set<RoleDAOIF> roles = this.authorizedRoles();

    return roles.contains(admin);
  }
  
  
  /* (non-Javadoc)
   * @see com.runwaysdk.business.rbac.SingleActorIF#getSingleActorName()
   */
  public String getSingleActorName()
  {
    return this.getKey();
  }
  
  @Override
  public boolean isLoginSupported()
  {
    return true;
  }
  
  @Override
  public int getSessionLimit()
  {
    if(this.hasAttribute(UserInfo.SESSION_LIMIT))
    {
      return Integer.parseInt(super.getValue(UserInfo.SESSION_LIMIT));
    }
    
    return -1;
  }
  
  /**
   * Returns the locale of the user
   *
   * @return the String name of of the locale
   */
  public String getLocale()
  {
    return CommonProperties.getDefaultLocaleString();
  }
}
