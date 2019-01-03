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
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.AttributeLocalIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MetadataDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.RoleNamespaceException;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.session.PermissionBuilder;
import com.runwaysdk.session.PermissionMap;

public class RoleDAO extends ActorDAO implements RoleDAOIF
{
  /**
   *
   */
  private static final long serialVersionUID = -6485859830726982632L;

  /**
   * The default constructor, does not set any attributes
   */
  public RoleDAO()
  {
    super();
  }

  public RoleDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public void setRoleName(String roleName)
  {
    super.setValue(RoleDAOIF.ROLENAME, roleName);
  }

  public void setDefaultDisplayLabel(String defaultDisplayLabel)
  {
    super.setStructValue(RoleDAOIF.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, defaultDisplayLabel);
  }

  public String getRoleName()
  {
    return super.getValue(RoleDAOIF.ROLENAME);
  }

  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   * 
   * @return map where the key is the locale and the value is the localized
   *         String value.
   */
  public Map<String, String> getDisplayLabels()
  {
    return ( (AttributeLocalIF) this.getAttributeIF(RoleDAOIF.DISPLAY_LABEL) ).getLocalValues();
  }

  /**
   * Returns the display label of this metadata object
   * 
   * @param locale
   * 
   * @return the display label of this metadata object
   */
  public String getDisplayLabel(Locale locale)
  {
    return ( (AttributeLocalIF) this.getAttributeIF(RoleDAOIF.DISPLAY_LABEL) ).getValue(locale);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public RoleDAO getBusinessDAO()
  {
    return (RoleDAO) super.getBusinessDAO();
  }

  @Override
  public PermissionMap getOperations()
  {
    return new PermissionBuilder(this).build();
  }

  /**
   * Returns a list of all the objects in which the role has been granted
   * permission.
   * 
   * @return A set of metadata ids which the role has permissions on
   */
  public Set<RelationshipDAOIF> getAllPermissions()
  {
    Set<RelationshipDAOIF> objectIds = super.getAllPermissions();

    Set<RoleDAOIF> set = this.getSuperRoles();

    for (RoleDAOIF role : set)
    {
      objectIds.addAll(role.getAllPermissions());
    }

    // Get the permissions of the role
    return objectIds;
  }

  /**
   * Returns all of the users assigned to a role
   * 
   * @return A list of all users that participate in a role
   */
  public Set<SingleActorDAOIF> assignedActors()
  {
    Set<SingleActorDAOIF> members = new TreeSet<SingleActorDAOIF>();

    // Get all of the instances of the singleactor-role relationship
    List<RelationshipDAOIF> list = this.getParents(RelationshipTypes.ASSIGNMENTS.getType());

    for (RelationshipDAOIF relationship : list)
    {
      SingleActorDAOIF parent = (SingleActorDAOIF) relationship.getParent();

      members.add(parent);
    }

    return members;
  }

  /**
   * Assigns a singleActor member to a role
   * 
   * @pre: singleActor != null
   * 
   * @param singleActor
   *          The singleActor member to assign to the role
   */
  public void assignMember(SingleActorDAOIF singleActor)
  {
    // Ensure that this role is not the owner role
    if (RoleDAOIF.OWNER_ROLE.equals(this.getRoleName()))
    {
      String error = "SingleActor [" + singleActor.getSingleActorName() + "] cannont be assigned to role [" + this.getRoleName() + "]";
      throw new RBACExceptionOwnerRole(error, singleActor, this);
    }

    Set<RoleDAOIF> roleHierarchy = this.getSuperRoles();
    roleHierarchy.add(this);

    for (RoleDAOIF role : roleHierarchy)
    {
      if (!role.checkMember(singleActor))
      {
        String error = "SingleActor [" + singleActor.getSingleActorName() + "] cannot be assigned to role [" + getRoleName() + "] because it already belongs to conflicting role [" + role.getRoleName() + "]";
        throw new RBACExceptionSingleActorConflictingRole(error, singleActor, this, role);
      }
    }

    // Don't re-assign actors to roles they already have
    if (!singleActor.assignedRoles().contains(this))
    {
      RelationshipDAO relationshipDAO = RelationshipDAO.newInstance(singleActor.getOid(), super.getOid(), RelationshipTypes.ASSIGNMENTS.getType());
      relationshipDAO.setKey(RoleDAO.buildAssignmentsKey(relationshipDAO));    
      relationshipDAO.apply();
    }
  }

  /**
   * Removes a user from a role
   * 
   * @pre: get(userId) instanceof Users
   * @pre: getRelationship(userId, roleId, USERASSIGNMENT) != null
   * 
   * @param singleActorIF
   *          The user to remove from the role
   */
  public void deassignMember(SingleActorDAOIF singleActorIF)
  {
    // If the actor doens't have this role to deassign, just return.
    if (!singleActorIF.assignedRoles().contains(this))
    {
      return;
    }

    List<RelationshipDAOIF> roleRelationshipList = RelationshipDAO.get(singleActorIF.getOid(), super.getOid(), RelationshipTypes.ASSIGNMENTS.getType());

    if (roleRelationshipList.size() > 0)
    {
      RelationshipDAO relationship = roleRelationshipList.get(0).getRelationshipDAO();
      relationship.delete();
    }
  }

  /**
   * Returns all of the operations a role has on a object
   * 
   * @pre get(mdTypeId) instanceof Metadata
   * @param metadata
   *          The oid of the MdType
   * 
   * @return A list of all operations the role has permissions for
   */
  public Set<Operation> getAllPermissions(MetadataDAOIF metadata)
  {
    Set<Operation> operations = super.getAllPermissions(metadata);

    Set<RoleDAOIF> set = this.getSuperRoles();

    for (RoleDAOIF role : set)
    {
      operations.addAll(role.getAllPermissions(metadata));
    }

    return operations;
  }

  /**
   * Enforces partial ordering on roles. The child inherits all of the
   * permissions of the parent, Effectively all users assigned to a child role
   * are also assigned to the parent role.
   * 
   * @param childRole
   *          The role to which inherits the parent role
   */
  public synchronized void addInheritance(RoleDAO childRole)
  {
    // Apply the inheritance
    RelationshipDAO inherit = RelationshipDAO.newInstance(super.getOid(), childRole.getOid(), ROLE_INHERITANCE);
    inherit.setKey(RoleDAO.buildInheritanceKey(inherit));
    inherit.apply();

    // Check if existing SSD constraints have been violated
    // Get a list of all SSD contraint sets the parent role participates in
    Set<SDutyDAOIF> set = this.roleSSDSet();

    // For each SSDSet check if the inheritance has violated the cardinality
    for (SDutyDAOIF ssd : set)
    {
      Set<RoleDAOIF> roleIds = ssd.SSDSetRoles();

      int cardinality = ssd.getSSDSetCardinality();

      if (!SDutyDAO.validate(roleIds, cardinality))
      {
        // The SSDSet has violated the cardinality - rollback the inheritance
        // and throw an exception
        inherit.delete();

        String error = "Role [" + childRole.getRoleName() + "] cannot inherit from role [" + getRoleName() + "] because it would break existing user assignment conflict " + "of intrest constraints";
        throw new RBACExceptionInheritance(error, childRole, this);
      }
    }
  }

  /**
   * Removes ordering on from roles
   * 
   * @param childRole
   *          The child of this role to remove inheritance from
   */
  public void deleteInheritance(RoleDAOIF childRole)
  {
    RelationshipDAO inherit = RelationshipDAO.get(super.getOid(), childRole.getOid(), ROLE_INHERITANCE).get(0).getRelationshipDAO();

    inherit.delete();
  }

  /**
   * Creates a new role and sets it as the parent of this role
   * 
   * @param roleName
   *          The name of the new role to create
   */
  public RoleDAO addAscendant(String roleName, String defaultDisplayLabel)
  {
    RoleDAO newRole = RoleDAO.createRole(roleName, defaultDisplayLabel);

    this.addAscendant(newRole);

    return newRole;
  }

  /**
   * Assigns a role as a parent of this role
   * 
   * @param role
   *          The role to assign as a parent
   */
  public void addAscendant(RoleDAOIF role)
  {
    List<RelationshipDAOIF> existing;

    try
    {
      existing = RelationshipDAO.get(role.getOid(), this.getOid(), ROLE_INHERITANCE);
    }
    catch (DataNotFoundException e)
    {
      existing = null;
    }

    if (existing == null || existing.size() == 0)
    {
      RelationshipDAO inherit = RelationshipDAO.newInstance(role.getOid(), super.getOid(), ROLE_INHERITANCE);
      inherit.setKey(RoleDAO.buildInheritanceKey(inherit));
      inherit.apply();
    }
  }

  public void removeAscendant(RoleDAOIF role)
  {
    try
    {
      List<RelationshipDAOIF> relationships = RelationshipDAO.get(role.getOid(), this.getOid(), ROLE_INHERITANCE);

      for (RelationshipDAOIF relationship : relationships)
      {
        relationship.getRelationshipDAO().delete();
      }
    }
    catch (DataNotFoundException e)
    {
      // Do nothing: there is no inheritance relationship to delete
    }
  }

  public void removeDecendant(RoleDAOIF role)
  {
    try
    {
      List<RelationshipDAOIF> relationships = RelationshipDAO.get(this.getOid(), role.getOid(), ROLE_INHERITANCE);

      for (RelationshipDAOIF relationship : relationships)
      {
        relationship.getRelationshipDAO().delete();
      }
    }
    catch (DataNotFoundException e)
    {
      // Do nothing: there is no inheritance relationship to delete
    }
  }

  /**
   * Creates a new role and sets it as the child of this role
   * 
   * @param roleName
   *          The name of the new role to create
   */
  public RoleDAO addDescendant(String roleName, String defaultDisplayLabel)
  {
    RoleDAO newRole = RoleDAO.createRole(roleName, defaultDisplayLabel);

    this.addDescendant(newRole);

    return newRole;
  }

  /**
   * Assigns a role as the child of this role
   * 
   * @param role
   *          The role to assign as a parent
   */
  public void addDescendant(RoleDAOIF role)
  {
    List<RelationshipDAOIF> existing;

    try
    {
      existing = RelationshipDAO.get(this.getOid(), role.getOid(), ROLE_INHERITANCE);
    }
    catch (DataNotFoundException e)
    {
      existing = null;
    }

    if (existing == null || existing.size() == 0)
    {
      RelationshipDAO inherit = RelationshipDAO.newInstance(super.getOid(), role.getOid(), ROLE_INHERITANCE);
      inherit.setKey(RoleDAO.buildInheritanceKey(inherit));
      inherit.apply();
    }
  }

  /**
   * Returns all of the roles which a role inherits
   * 
   * @return all of the roles which a role inherits.
   */
  public Set<RoleDAOIF> getSuperRoles()
  {
    Stack<RoleDAOIF> stack = new Stack<RoleDAOIF>();

    Set<RoleDAOIF> set = new TreeSet<RoleDAOIF>();

    // Add root oid to the stack
    stack.push(this);

    while (!stack.empty())
    {
      RoleDAOIF role = stack.pop();

      List<RelationshipDAOIF> list = role.getParents(ROLE_INHERITANCE);

      // Add the parent of the current oid to the set role ids
      for (RelationshipDAOIF relationship : list)
      {
        RoleDAOIF parent = (RoleDAOIF) relationship.getParent();

        // If the parent is not already in the set of ids add to the stack
        if (set.add(parent))
        {
          stack.push(parent);
        }
      }
    }

    return set;
  }

  /**
   * Returns all of the roles which inherit from a role
   * 
   * @param roleId
   *          The oid of the role
   * @return A list of all role ids which inherit the given role
   */
  public Set<RoleDAOIF> getSubRoles()
  {
    Stack<RoleDAOIF> stack = new Stack<RoleDAOIF>();

    Set<RoleDAOIF> set = new TreeSet<RoleDAOIF>();

    // Add root oid to the stack
    stack.push(this);

    while (!stack.empty())
    {
      RoleDAOIF role = stack.pop();

      List<RelationshipDAOIF> list = role.getChildren(ROLE_INHERITANCE);

      // Add the parent of the current oid to the set role ids
      for (RelationshipDAOIF relationship : list)
      {
        RoleDAOIF parent = RoleDAO.get(relationship.getChildOid());

        // If the parent is not already in the set of ids add to the stack
        if (set.add(parent))
        {
          stack.push(parent);
        }
      }
    }

    return set;
  }

  /**
   * Returns the set of users directly assigned to a given role as well as those
   * who were members of roles that inherited the give role
   * 
   * @return The set of users the belong to this role
   */
  public Set<SingleActorDAOIF> authorizedActors()
  {
    Set<SingleActorDAOIF> memberIds = new TreeSet<SingleActorDAOIF>();

    memberIds.addAll(assignedActors());

    Set<RoleDAOIF> set = this.getSubRoles();

    for (RoleDAOIF role : set)
    {
      memberIds.addAll(role.assignedActors());
    }

    return memberIds;
  }

  /**
   * Returns a list of conflict of intrest sets the role is involved in
   * 
   * @return A set of SSD sets the role belongs to
   */
  public Set<SDutyDAOIF> roleSSDSet()
  {
    Set<SDutyDAOIF> ssds = new TreeSet<SDutyDAOIF>();

    List<RelationshipDAOIF> list = this.getChildren(SDutyDAO.SDCONFLICTINGROLES);

    for (RelationshipDAOIF relationship : list)
    {
      SDutyDAOIF ssdSet = (SDutyDAOIF) relationship.getChild();

      ssds.add(ssdSet);
    }

    return ssds;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.business.rbac.RoleIF#checkMember(com.runwaysdk
   * .business.rbac.SingleActorIF)
   */
  public boolean checkMember(SingleActorDAOIF singleActor)
  {
    // Get the list of all SSD set the role belongs to
    Set<SDutyDAOIF> ssdIds = this.roleSSDSet();

    for (SDutyDAOIF ssd : ssdIds)
    {
      if (!ssd.checkSSD(singleActor))
      {
        return false;
      }
    }

    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.business.rbac.RoleIF#isAuthorizedMember(com.runwaysdk.business
   * .rbac.SingleActorIF)
   */
  public boolean isAuthorizedMember(SingleActorDAOIF singleActorIF)
  {
    Set<SingleActorDAOIF> members = this.authorizedActors();

    if (members.contains(singleActorIF))
    {
      return true;
    }

    return false;
  }

  /**
   * Determines the authorizedUsers of two roles intersect with each other
   * 
   * @param r1Id
   *          The oid of the first role
   * @param r2Id
   *          The oid of the second role
   * @return If two roles intersect
   */
  public boolean intersect(RoleDAO role)
  {
    Set<SingleActorDAOIF> members = this.authorizedActors();

    for (SingleActorDAOIF member : members)
    {
      if (role.isAuthorizedMember(member))
      {
        return true;
      }
    }

    return false;
  }

  /**
   * Creates a new role
   * 
   * @pre: roleName is unique
   * 
   * @param name
   *          The unqiue name of the new role
   */
  public static RoleDAO createRole(String roleName, String defaultDisplayLabel)
  {
    // Create Role Test
    RoleDAO newRole = RoleDAO.newInstance();

    newRole.setValue(RoleDAOIF.ROLENAME, roleName);

    newRole.setStructValue(RoleDAOIF.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, defaultDisplayLabel);

    newRole.apply();

    return newRole;
  }

  /**
   * Get the instance associated with a given role name
   * 
   * @param roleName
   *          The name of the role to search for
   * @return The BusinessDAO instance of the role
   */
  public static RoleDAOIF findRole(String roleName)
  {
    return ObjectCache.getRole(roleName);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Map<String,
   * Attribute>, java.lang.String)
   */
  public RoleDAO create(Map<String, Attribute> attributeMap, String type)
  {
    return new RoleDAO(attributeMap, type);
  }

  public static RoleDAO newInstance()
  {
    return (RoleDAO) BusinessDAO.newInstance(RoleDAOIF.CLASS);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static RoleDAOIF get(String oid)
  {
    return (RoleDAOIF) BusinessDAO.get(oid);
  }

  @Override
  public String apply()
  {
    validateRolename();
// Heads up: check: Should this also allow the role name to be changed?
    if (this.isNew())
    {
      this.setKey(buildKey(this.getRoleName()));
    }
    
    if (!this.isNew() || this.isAppliedToDB())
    {
      // update role inheritance relationship keys
      List<RelationshipDAOIF> inheritanceList = this.getChildren(RoleDAOIF.ROLE_INHERITANCE);
      for (RelationshipDAOIF relationshipDAOIF : inheritanceList)
      {
        RelationshipDAO inherit = relationshipDAOIF.getRelationshipDAO();
        inherit.setKey(RoleDAO.buildInheritanceKey(inherit));
        inherit.apply();
      }
      inheritanceList = this.getParents(RoleDAOIF.ROLE_INHERITANCE);
      for (RelationshipDAOIF relationshipDAOIF : inheritanceList)
      {
        RelationshipDAO inherit = relationshipDAOIF.getRelationshipDAO();
        inherit.setKey(RoleDAO.buildInheritanceKey(inherit));
        inherit.apply();
      }
      
      // update role assignment relationship keys
      List<RelationshipDAOIF> parentAssignments = this.getChildren(RelationshipTypes.ASSIGNMENTS.getType());
      for (RelationshipDAOIF relationshipDAOIF : parentAssignments)
      {
        RelationshipDAO inherit = relationshipDAOIF.getRelationshipDAO();
        inherit.setKey(RoleDAO.buildAssignmentsKey(inherit));
        inherit.apply();
      }
      
    }

    return super.apply();
  }

  /**
   * Validates the rolename
   */
  private void validateRolename()
  {
    if (!this.isImport() && this.getRoleName() != null && !this.getRoleName().contains("."))
    {
      String msg = "The rolename [" + this.getRoleName() + "] must be namespaced and contain at least one '.'";

      throw new RoleNamespaceException(msg, this.getRoleName());
    }
  }

  /**
   * Creates a key: {@link RoleDAOIF#TYPENAME}.[Role Name].
   * 
   * @param roleName
   * @return
   */
  public static String buildKey(String roleName)
  {
    return ActorDAO.buildKey(RoleDAOIF.TYPENAME, roleName);
  }

  public static String buildInheritanceKey(RelationshipDAO inherit)
  {
    return inherit.getChild().getKey() + "_inherits_" + inherit.getParent().getKey();
  }

  public static String buildAssignmentsKey(RelationshipDAO assignment)
  {
    return assignment.getChild().getKey() + "_is_assigned_" + assignment.getParent().getKey();
  }

}
