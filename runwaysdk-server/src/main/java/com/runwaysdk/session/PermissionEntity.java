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
package com.runwaysdk.session;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.Element;
import com.runwaysdk.business.Mutable;
import com.runwaysdk.business.Relationship;
import com.runwaysdk.business.Struct;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.SingleActorDAOIF;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdDimensionDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.metadata.DomainTupleDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDimensionDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDimensionDAO;
import com.runwaysdk.util.IdParser;

/**
 * Abstract Permissions class that caches the permissions of an Actor and does
 * standard access checking. It only performs checks on the cached permissions,
 * any global permissions are disregarded.
 *
 * @author Justin Smethie
 */
public abstract class PermissionEntity implements Serializable
{
  /**
   *
   */
  private static final long                           serialVersionUID = 347902374534234L;

  /**
   * Guards to ensure that invariants between multiple state fields hold.
   */
  protected final ReentrantLock                       permissionLock   = new ReentrantLock();

  /**
   * A hash map of the users metadata permissions
   */
  protected ConcurrentHashMap<String, Set<Operation>> permissions;

  /**
   * Flag denoting if the entity inherits the administrator role
   */
  protected volatile boolean                          isAdmin;

  /**
   * Constructs an empty permissions mapping
   */
  public PermissionEntity()
  {
    this.permissions = new ConcurrentHashMap<String, Set<Operation>>();
    this.isAdmin = false;
  }

  /**
   * Registers the PermissionEntity with the PermissionManager
   */
  public void register()
  {
    PermissionObserver.register(this);
  }

  /**
   * Unregisters the PermissionEntity from the PermissionManager
   */
  public void unregister()
  {
    PermissionObserver.unregister(this);
  }

  /**
   * Notify the PermissionEntity that a PermissionEvent has occured
   *
   * @param e
   */
  public abstract void notify(PermissionEvent e);

  /**
   * Returns the actor of the PermissionEntity
   *
   * @return
   */
  public abstract SingleActorDAOIF getUser(); 

  /**
   * @return The current dimension of the permission entity
   */
  public abstract MdDimensionDAOIF getDimension();

  /**
   * Checks if the session has permissions to execute an operation on a type.
   *
   * @param o
   *          The operation to execute
   * @param type
   *          The type to test permissions on
   *
   * @return If access has been granted
   */
  protected boolean checkTypeAccess(Operation o, String type)
  {
    this.permissionLock.lock();
    try
    {
      MdClassDAOIF mdClassIF = MdClassDAO.getMdClassDAO(type);

      return this.checkTypeAccess(o, mdClassIF);
    }
    finally
    {
      this.permissionLock.unlock();
    }
  }

  /**
   * Checks all types involved in building a {@link ValueObject} to see if the
   * given permission exists for that type.
   *
   * @param o
   *          The operation to execute
   * @param valueObject
   *          The object to test permissions on
   *
   * @return If access has been granted
   */
  protected boolean checkTypeAccess(Operation o, ValueObject valueObject)
  {
    this.permissionLock.lock();
    try
    {
      if (this.isAdmin)
      {
        return true;
      }

      for (MdEntityDAOIF mdEntityIF : valueObject.getDefiningMdEntities().values())
      {
        if (!checkTypeAccess(o, mdEntityIF))
        {
          return false;
        }
      }

      return true;
    }
    finally
    {
      this.permissionLock.unlock();
    }
  }

  /**
   * Checks to see if the given permission exists for that type.
   *
   * @param o
   *          The operation to execute
   * @param mdTypeIF
   *          The object to test permissions on
   *
   * @return If access has been granted
   */
  protected boolean checkTypeAccess(Operation o, MdTypeDAOIF mdTypeIF)
  {
    this.permissionLock.lock();
    try
    {
      if (this.isAdmin)
      {
        return true;
      }

      // Check for permissions on the type, regardless of state
      Set<Operation> typeOperations = this.getTypeOperations(mdTypeIF);

      if (typeOperations == null || !typeOperations.contains(o))
      {
        return false;
      }

      return true;
    }
    finally
    {
      this.permissionLock.unlock();
    }
  }

  /**
   * Checks if the session has permissions to execute an operation on an object.
   *
   * @param o
   *          The operation to execute
   * @param component
   *          The object to test permissions on
   *
   * @return If access has been granted
   */
  protected boolean checkAccess(Operation o, Mutable component)
  {
    this.permissionLock.lock();
    try
    {
      if (this.isAdmin)
      {
        return true;
      }

      String type = component.getType();

      MdClassDAOIF mdClassIF = MdClassDAO.getMdClassDAO(type);

      if (this.getOperations(component, mdClassIF).contains(o))
      {
        return true;
      }

      // Actor does not have permission to directly modify the relationship from
      // any direction.
      // However, it may have directional permissions.
      if (component instanceof Relationship)
      {
        Relationship relationship = (Relationship) component;
        Business parent = null;
        Business child = null;

        try
        {
          if (IdParser.validId(relationship.getParentId()))
          {
            parent = relationship.getParent();
          }

          if (IdParser.validId(relationship.getChildId()))
          {
            child = relationship.getChild();
          }
        }
        catch (Exception e)
        {
          // Do nothing, a relationship may or may not have a valid
          // parent/child oid. If the oid is not valid then no further
          // permission checks can be done on the entity
        }

        if (Operation.CREATE.equals(o))
        {
          // Check if actor via the parent object can add a child
          if (parent != null && this.checkRelationshipAccess(Operation.ADD_CHILD, parent, mdClassIF.getOid()))
          {
            return true;
          }

          // Check if actor via the child object can add a parent
          if (child != null && this.checkRelationshipAccess(Operation.ADD_PARENT, child, mdClassIF.getOid()))
          {
            return true;
          }
        }
        else if (Operation.DELETE.equals(o))
        {
          // Check if actor via the parent object can delete a child
          if (parent != null && this.checkRelationshipAccess(Operation.DELETE_CHILD, parent, mdClassIF.getOid()))
          {
            return true;
          }

          // Check if actor via the child object can delete a parent
          if (child != null && this.checkRelationshipAccess(Operation.DELETE_PARENT, child, mdClassIF.getOid()))
          {
            return true;
          }
        }
        else if (Operation.WRITE.equals(o))
        {
          // Check if actor via the parent object can modify a child
          if (parent != null && this.checkRelationshipAccess(Operation.WRITE_CHILD, parent, mdClassIF.getOid()))
          {
            return true;
          }

          // Check if actor via the child object can modify a parent
          if (child != null && this.checkRelationshipAccess(Operation.WRITE_PARENT, child, mdClassIF.getOid()))
          {
            return true;
          }
        }
        else if (Operation.READ.equals(o))
        {
          // Check if actor via the parent object can read a child
          if (parent != null && this.checkRelationshipAccess(Operation.READ_CHILD, parent, mdClassIF.getOid()))
          {
            return true;
          }

          // Check if actor via the child object can read a parent
          if (child != null && this.checkRelationshipAccess(Operation.READ_PARENT, child, mdClassIF.getOid()))
          {
            return true;
          }
        }
      }

      return false;
    }
    finally
    {
      this.permissionLock.unlock();
    }
  }

  protected Set<Operation> getOperations(Mutable component, MdClassDAOIF mdClassIF)
  {
    this.permissionLock.lock();
    try
    {
      Set<Operation> operations = this.getTypeOperations(mdClassIF);

      if (component instanceof Element)
      {
        String domainId = component.getValue(ElementInfo.DOMAIN);

        // Load domain-type permissions
        if (!domainId.equals(""))
        {
          String key = DomainTupleDAO.buildKey(domainId, mdClassIF.getOid(), null);

          if (permissions.containsKey(key))
          {
            operations.addAll(permissions.get(key));
          }
        }
      }

      return operations;
    }
    finally
    {
      this.permissionLock.unlock();
    }
  }

  private Set<Operation> getTypeOperations(MdTypeDAOIF mdTypeIF)
  {
    Set<Operation> operations = new TreeSet<Operation>();

    // Load type permissions
    if (permissions.containsKey(mdTypeIF.getOid()))
    {
      operations.addAll(permissions.get(mdTypeIF.getOid()));
    }

    if (mdTypeIF instanceof MdClassDAOIF)
    {
      // Check for permissions of the MdClassDimension
      MdDimensionDAOIF dimension = this.getDimension();

      if (dimension != null)
      {
        String key = MdClassDimensionDAO.getPermissionKey((MdClassDAOIF)mdTypeIF, dimension);

        if (permissions.containsKey(key))
        {
          operations.addAll(permissions.get(key));
        }
      }
    }
    return operations;
  }

  /**
   * Check access on a ADD_CHILD, ADD_PARENT, DELETE_CHILD, and DELETE_PARENT
   * operation.
   *
   * @param o
   *          The operation to check access for
   * @param business
   *          The Business object the relationship is being added/removed
   *          to/from.
   * @param mdRelationshipId
   *          The relationship metadata for the relationship being created
   * @return If the user of this session has permission to execute the given
   *         operation on the given object
   */
  protected boolean checkRelationshipAccess(Operation o, Business business, String mdRelationshipId)
  {
    this.permissionLock.lock();
    try
    {
      if (this.isAdmin)
      {
        return true;
      }

      Set<Operation> operations = this.getRelationshipOperations(business, mdRelationshipId);

      // Check for permissions on the type, regardless of state
      if (operations.contains(o))
      {
        return true;
      }

      if (this.checkRelationshipAccess(o, operations))
      {
        return true;
      }

      return false;
    }
    finally
    {
      this.permissionLock.unlock();
    }
  }

  /**
   * Load directional permissions
   *
   * @param business
   * @param mdRelationshipId
   * @return
   */
  protected Set<Operation> getRelationshipOperations(Business business, String mdRelationshipId)
  {
    this.permissionLock.lock();

    try
    {
      Set<Operation> operations = new TreeSet<Operation>();

      // Load mdRelationshipId permissions
      if (permissions.containsKey(mdRelationshipId))
      {
        operations.addAll(permissions.get(mdRelationshipId));
      }

      // Load domain-MdRelationship
      String domainId = business.getValue(ElementInfo.DOMAIN);

      if (!domainId.equals(""))
      {
        String key = DomainTupleDAO.buildKey(domainId, mdRelationshipId, null);

        if (permissions.containsKey(key))
        {
          operations.addAll(permissions.get(key));
        }
      }

      return operations;
    }
    finally
    {
      this.permissionLock.unlock();
    }
  }

  /**
   * Used internally to check if a by-directional permission has been defined.
   * For example: Returns true if the operation equals ADD_CHILD or ADD_PARENT
   * and the set of operations contains CREATE. Same for other directional
   * permissions.
   *
   * @param o
   * @param operations
   *
   * @return Returns true if the operation equals ADD_CHILD or ADD_PARENT and
   *         the set of operations contains CREATE. Same for other directional
   *         permissions.
   */
  protected boolean checkRelationshipAccess(Operation o, Set<Operation> operations)
  {
    if (operations != null)
    {
      if (operations.contains(o))
      {
        return true;
      }

      if ( ( Operation.ADD_CHILD.equals(o) || Operation.ADD_PARENT.equals(o) ) && operations.contains(Operation.CREATE))
      {
        return true;
      }

      if ( ( Operation.DELETE_CHILD.equals(o) || Operation.DELETE_PARENT.equals(o) ) && operations.contains(Operation.DELETE))
      {
        return true;
      }

      if ( ( Operation.WRITE_CHILD.equals(o) || Operation.WRITE_PARENT.equals(o) ) && operations.contains(Operation.WRITE))
      {
        return true;
      }

      if ( ( Operation.READ_CHILD.equals(o) || Operation.READ_PARENT.equals(o) ) && operations.contains(Operation.READ))
      {
        return true;
      }
    }

    return false;
  }

  /**
   * Check if the user has type permission on the entity attribute for a given
   * operation.
   *
   * @param operation
   *          The operation to check access for
   * @param mdAttribute
   *          The MdAttribute which defines the given attribute
   *
   * @return If the user has access permissions for a given operation on a given
   *         attribute
   */
  protected boolean checkAttributeTypeAccess(Operation operation, MdAttributeDAOIF mdAttribute)
  {
    this.permissionLock.lock();
    try
    {
      if (this.isAdmin)
      {
        return true;
      }

      // Check if permissions exist of the mdAttribute type
      if (permissions.containsKey(mdAttribute.getOid()))
      {
        Set<Operation> operations = permissions.get(mdAttribute.getOid());

        if (operations != null && operations.contains(operation))
        {
          return true;
        }
      }

      return false;
    }
    finally
    {
      this.permissionLock.unlock();
    }
  }

  /**
   * Check if the user has attribute permissions for a given operation
   *
   * @param operation
   *          The operation to check access for
   * @param component
   *          The entity the attribute is a struct of
   * @param mdAttribute
   *          The MdAttribute which defines the given attribute
   *
   * @return If the user has access permissions for a given operation on a given
   *         attribute
   */
  protected boolean checkAttributeAccess(Operation operation, Mutable component, MdAttributeDAOIF mdAttribute)
  {
    this.permissionLock.lock();
    try
    {
      if (this.isAdmin)
      {
        return true;
      }

      if (this.getAttributeOperations(component, mdAttribute).contains(operation))
      {
        return true;
      }

      if (component instanceof Relationship)
      {
        Relationship relationship = (Relationship) component;
        Business parent = null;
        Business child = null;

        // It is possible that the child or parent oid is invalid and can't be
        // dereferenced

        try
        {
          if (IdParser.validId(relationship.getParentId()))
          {
            parent = relationship.getParent();
          }

          if (IdParser.validId(relationship.getChildId()))
          {
            child = relationship.getChild();
          }
        }
        catch (Exception e)
        {
          // Do nothing, a relationship may or may not have a valid
          // parent/child oid. If the oid is not valid then no further
          // permission checks can be done on the entity
        }

        if (operation.equals(Operation.WRITE))
        {
          // Check if actor via the parent object can modify a child
          if (parent != null && this.checkRelationshipAttributeAccess(Operation.WRITE_CHILD, parent, mdAttribute))
          {
            return true;
          }

          // Check if actor via the child object can modify a parent
          if (child != null && this.checkRelationshipAttributeAccess(Operation.WRITE_PARENT, child, mdAttribute))
          {
            return true;
          }
        }
        else if (Operation.READ.equals(operation))
        {
          // Check if actor via the parent object can read a child
          if (parent != null && this.checkRelationshipAttributeAccess(Operation.READ_CHILD, parent, mdAttribute))
          {
            return true;
          }

          // Check if actor via the child object can read a parent
          if (child != null && this.checkRelationshipAttributeAccess(Operation.READ_PARENT, child, mdAttribute))
          {
            return true;
          }
        }
      }

      return false;

    }
    finally
    {
      this.permissionLock.unlock();
    }
  }

  protected Set<Operation> getAttributeOperations(Mutable component, MdAttributeDAOIF mdAttribute)
  {
    this.permissionLock.lock();

    try
    {
      Set<Operation> operations = this.getAttributeOperations(mdAttribute);

      if (component instanceof Element)
      {
        String domainId = component.getValue(ElementInfo.DOMAIN);

        // Load domain-attribute permissions
        if (!domainId.equals(""))
        {
          String key = DomainTupleDAO.buildKey(domainId, mdAttribute.getOid(), null);

          if (permissions.containsKey(key))
          {
            operations.addAll(permissions.get(key));
          }
        }
      }
      return operations;
    }
    finally
    {
      permissionLock.unlock();
    }
  }

  protected Set<Operation> getAttributeOperations(MdAttributeDAOIF mdAttribute)
  {
    Set<Operation> operations = new TreeSet<Operation>();

    // Check if permissions exist of the mdAttribute type
    if (permissions.containsKey(mdAttribute.getOid()))
    {
      operations.addAll(permissions.get(mdAttribute.getOid()));
    }

    // Check for permissions of the MdAttributeDimension this permission entity
    // has a dimension
    MdDimensionDAOIF dimension = this.getDimension();

    if (dimension != null)
    {
      String key = MdAttributeDimensionDAO.getPermissionKey(mdAttribute, dimension);

      if (permissions.containsKey(key))
      {
        operations.addAll(permissions.get(key));
      }
    }

    return operations;
  }

  /**
   * Check access on directional CRUD operations. Eg. WRITE_CHILD, WRITE_PARENT,
   * READ_CHILD, ...
   *
   * @pre (operation instanceof WRITE_CHILD) || (operation instanceof
   *      WRITE_PARENT) || (operation instanceof READ_CHILD) || (operation
   *      instanceof READ_PARENT) || ...
   *
   * @param operation
   *          The operation to check access for
   * @param business
   *          The source business object the attribute is defined for
   * @param mdAttribute
   *          The MdAttribute which defines the given attribute
   *
   * @return If the user has a directional access permission on the given
   *         MdAttribute
   */
  protected boolean checkRelationshipAttributeAccess(Operation operation, Business business, MdAttributeDAOIF mdAttribute)
  {
    this.permissionLock.lock();
    try
    {
      if (this.isAdmin)
      {
        return true;
      }

      Set<Operation> operations = this.getAttributeOperations(business, mdAttribute);

      // Check if the directional permission, CHILD/PARENT, exist for the
      // MdAttribute
      if (operations.contains(operation))
      {
        return true;
      }

      // Check if the unidirection permission exists for the MdAttribute
      if (this.checkRelationshipAccess(operation, operations))
      {
        return true;
      }

      return false;

    }
    finally
    {
      this.permissionLock.unlock();
    }
  }

  /**
   * Check if the user has attribute permissions for a given operation
   *
   * @param operation
   *          The operation to check access for
   * @param struct
   *          The struct the attribute is a member of
   * @param mdAttribute
   *          The MdAttribute which defines the given attribute
   *
   * @return If the user has access permissions for a given operation on a given
   *         attribute
   */
  protected boolean checkAttributeAccess(Operation operation, Struct struct, MdAttributeDAOIF mdAttribute)
  {
    // Structs do not have state or owner's thus only check the MdAttributeIF
    // for permissions
    return checkAttributeAccess(operation, mdAttribute);
  }

  /**
   * Check if the user has attribute permissions for a given operation. This
   * method only checks against the MetaData. It does not take into account
   * permissions that exist on an entity state or an entity's owner.
   *
   * @param operation
   *          The operation to check
   * @param mdAttribute
   *          The MdAttribute which defines the given attribute
   *
   * @return If the user has access permissions for a given operation on a given
   *         attribute
   */
  protected boolean checkAttributeAccess(Operation operation, MdAttributeDAOIF mdAttribute)
  {
    this.permissionLock.lock();
    try
    {
      if (this.isAdmin)
      {
        return true;
      }

      Set<Operation> operations = this.getAttributeOperations(mdAttribute);

      if (operations != null && operations.contains(operation))
      {
        return true;
      }

      return false;

    }
    finally
    {
      this.permissionLock.unlock();
    }
  }

  /**
   * Check if the permission entity has permissions to execute a given MdMethod.
   *
   * @param mdMethod
   * @return
   */
  protected boolean checkMethodAccess(Operation operation, MdMethodDAOIF mdMethod)
  {
    this.permissionLock.lock();
    try
    {
      if (this.isAdmin)
      {
        return true;
      }

      // Check if permissions exist on the MdMethod
      if (permissions.containsKey(mdMethod.getOid()))
      {
        Set<Operation> operations = permissions.get(mdMethod.getOid());

        if (operations != null && operations.contains(operation))
        {
          return true;
        }
      }

      return false;

    }
    finally
    {
      this.permissionLock.unlock();
    }
  }
}
