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
package com.runwaysdk.session;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.AttributeNotification;
import com.runwaysdk.DomainErrorException;
import com.runwaysdk.ProblemIF;
import com.runwaysdk.business.Business;
import com.runwaysdk.business.Element;
import com.runwaysdk.business.Entity;
import com.runwaysdk.business.Information;
import com.runwaysdk.business.LockException;
import com.runwaysdk.business.Message;
import com.runwaysdk.business.Mutable;
import com.runwaysdk.business.Relationship;
import com.runwaysdk.business.Struct;
import com.runwaysdk.business.Warning;
import com.runwaysdk.business.graph.GraphObject;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.business.rbac.MethodActorDAOIF;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.SingleActorDAOIF;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.transaction.AttributeNotificationMap;
import com.runwaysdk.dataaccess.transaction.LockObject;
import com.runwaysdk.logging.RunwayLogUtil;
import com.runwaysdk.util.IdParser;
import com.runwaysdk.business.BusinessFacade;

public class AbstractRequestAspectState
{
  protected RequestState                        requestState;

  protected boolean                             topLevel                    = true;

  protected Set<String>                         setAppLocksSet              = new HashSet<String>();

  private Stack<MdMethodDAOIF>                  mdMethodIFStack             = new Stack<MdMethodDAOIF>();

  protected List<Message>                       messageList                 = new LinkedList<Message>();

  protected Map<String, String>                 idMap                       = new HashMap<String, String>();

  private Map<String, AttributeNotificationMap> attributeNotificationMapMap = new HashMap<String, AttributeNotificationMap>();

  private List<ProblemIF>                       problemList                 = new LinkedList<ProblemIF>();

  protected Logger                              log                         = LoggerFactory.getLogger(AbstractRequestAspectState.class);

  public RequestState getRequestState()
  {
    return this.requestState;
  }

  public List<ProblemIF> getProblemList()
  {
    return this.problemList;
  }

  /**
   * Some things, like permission checking, does not need to be done if the
   * request does not occur in an initialized session.
   *
   * @return true if the session has been initialized, false otherwise;
   */
  protected boolean isSessionInitialized()
  {
    if (this.getRequestState().getSession() == null)
    {
      return false;
    }
    else
    {
      return true;
    }
  }

  public void addAppLock(String oid)
  {
    this.setAppLocksSet.add(oid);
  }

  public void throwProblem(ProblemIF problemIF)
  {
    this.log.info(RunwayLogUtil.formatLoggableMessage(problemIF.getDeveloperMessage(), problemIF.getLocalizedMessage()));
    this.problemList.add(problemIF);
  }

  public void addAttributeNotificationMap(AttributeNotificationMap attributeNotificationMap)
  {
    this.attributeNotificationMapMap.put(attributeNotificationMap.getMapKey(), attributeNotificationMap);
  }

  public Object getConnection()
  {
    return this.getRequestState().getDatabaseConnection();
  }

  public Object getGraphRequest()
  {
    return this.getRequestState().getGraphDBRequest();
  }

  public void mapNewInstanceTempId(String oldTempId, String newId)
  {
    this.idMap.put(newId, oldTempId);
  }

  public SessionIF getCurrentSession()
  {
    return this.getRequestState().getSession();
  }

  public MdMethodDAOIF removeMethodFromStack()
  {
    return this.mdMethodIFStack.pop();
  }

  public void checkMethodExecutePermission(String type, String methodName, Mutable mutable)
  {
    MdClassDAOIF mdClassIF = MdClassDAO.getMdClassDAO(type);

    MdMethodDAOIF mdMethodToExecute = mdClassIF.getMdMethod(methodName);

    // Check execute permission on this method
    if (this.mdMethodIFStack.size() > 0)
    {
      MdMethodDAOIF mdMethodIF = this.mdMethodIFStack.peek();
      this.checkMethodExecutePermission_INSTANCE(mutable, mdMethodIF, mdMethodToExecute);
    }
    else
    {
      SingleActorDAOIF userIF = this.getRequestState().getSession().getUser();
      this.checkMethodExecutePermission_INSTANCE(userIF, mutable, mdMethodToExecute);
    }

    this.mdMethodIFStack.push(mdMethodToExecute);

    if (this.getRequestState().getSession() != null)
    {
      ( (Session) this.getRequestState().getSession() ).setFirstMdMethodDAOIF(mdMethodToExecute);
    }

  }

  public void checkStaticMethodExecutePermission(String type, String methodName)
  {

    MdTypeDAOIF mdTypeIF = MdTypeDAO.getMdTypeDAO(type);
    MdMethodDAOIF mdMethodToExecute = mdTypeIF.getMdMethod(methodName);
    Session _session = (Session) this.getRequestState().getSession();

    // Check execute permission on this method
    if (this.mdMethodIFStack.size() > 0)
    {
      MdMethodDAOIF mdMethodIF = this.mdMethodIFStack.peek();
      this.checkMethodExecutePermission_STATIC(mdTypeIF, mdMethodIF, mdMethodToExecute);
    }
    else
    {
      if (this.isSessionInitialized())
      {
        SingleActorDAOIF userIF = _session.getUser();
        this.checkMethodExecutePermission_STATIC(userIF, mdTypeIF, mdMethodToExecute);
      }
    }

    this.mdMethodIFStack.push(mdMethodToExecute);

    if (_session != null)
    {
      _session.setFirstMdMethodDAOIF(mdMethodToExecute);
    }
  }

  public void getNewBusinessFromFacade(Entity business)
  {
    if (this.isSessionInitialized())
    {
      // Check execute permission on this method
      if (this.mdMethodIFStack.size() > 0)
      {
        MdMethodDAOIF mdMethodIF = this.mdMethodIFStack.peek();
        this.checkEntityCreatePermission(mdMethodIF, business);
      }
      else
      {
        this.checkEntityCreatePermission(business);
      }
    }
  }

  public void getNewDisconnectedEntityFromFacade(Entity business)
  {
    if (this.isSessionInitialized())
    {
      // Check execute permission on this method
      if (this.mdMethodIFStack.size() > 0)
      {
        MdMethodDAOIF mdMethodIF = this.mdMethodIFStack.peek();
        this.checkEntityReadPermission(mdMethodIF, business);
      }
      else
      {
        this.checkEntityReadPermission(business);
      }
    }
  }

  public void applyEntity(Entity entity)
  {
    if (this.isSessionInitialized())
    {
      EntityDAO entityDAO = (EntityDAO) BusinessFacade.getEntityDAO(entity);
      SingleActorDAOIF userIF = this.getRequestState().getSession().getUser();

      // Check if userIF has a lock on the object.
      if (entity instanceof Element)
      {
        checkLock((Element) entity);
      }

      if (entityDAO.isNew())
      {
        // If the entityDAOIF is new and no owner has been defined, then set the
        // owner to the userIF currently performing the action

        // Do not overwrite the owner if we are importing the object.
        if (!entityDAO.isImport() && entityDAO.hasAttribute(ElementInfo.OWNER) && ( entityDAO.getAttributeIF(ElementInfo.OWNER).getValue().trim().equals("") ))
        {
          entityDAO.getAttribute(ElementInfo.OWNER).setValue(userIF.getOid());
        }

        if (this.mdMethodIFStack.size() > 0)
        {
          MdMethodDAOIF mdMethodIF = this.mdMethodIFStack.peek();
          this.checkEntityCreatePermission(mdMethodIF, entity);
        }
        else
        {
          this.checkEntityCreatePermission(entity);
        }
      }
      else
      {
        if (this.mdMethodIFStack.size() > 0)
        {
          MdMethodDAOIF mdMethodIF = this.mdMethodIFStack.peek();
          checkEntityWritePermission(mdMethodIF, entity);
        }
        else
        {
          this.checkEntityWritePermission(entity);
        }
      }

      if (entityDAO.hasAttribute(ElementInfo.LAST_UPDATED_BY))
      {

        if (this.mdMethodIFStack.size() > 0 && entity.hasAttribute(ElementInfo.LOCKED_BY) && !entity.getValue(ElementInfo.LOCKED_BY).equals(userIF.getOid()))
        {
          MdMethodDAOIF mdMethodIF = this.mdMethodIFStack.peek();
          MethodActorDAOIF methodActorIF = MethodFacade.getMethodActorIF(mdMethodIF);
          // I don't think this will ever be null, as the above
          // checkEntityWritePermission method would have thrown an exception.
          if (methodActorIF != null)
          {
            if (!entityDAO.isImport() && ( !entityDAO.isImportResolution() || ( entityDAO.isImportResolution() && entityDAO.isMasteredHere() ) ))
            {
              entityDAO.getAttribute(ElementInfo.LAST_UPDATED_BY).setValue(methodActorIF.getOid());
            }
          }
        }
        else
        {
          if (!entityDAO.isImport() && ( !entityDAO.isImportResolution() || ( entityDAO.isImportResolution() && entityDAO.isMasteredHere() ) ))
          {
            entityDAO.getAttribute(ElementInfo.LAST_UPDATED_BY).setValue(userIF.getOid());
          }
        }
      }
    }
  }

  // Check write permission on an object. Check on userLock().
  public void elementLockedByCheck(Element element)
  {
    if (this.isSessionInitialized())
    {
      if (this.mdMethodIFStack.size() > 0)
      {
        MdMethodDAOIF mdMethodIF = this.mdMethodIFStack.peek();
        this.checkEntityWritePermission(mdMethodIF, element);
      }
      else
      {
        this.checkEntityWritePermission(element);
      }
    }
  }

  // Check write permission on an object attribute
  public void modifyEntityAttribute(Entity entity, String attributeName)
  {
    if (this.isSessionInitialized())
    {
      if (entity instanceof Struct)
      {
        checkAttributePermissions((Struct) entity, attributeName);
      }
      else
      {
        checkAttributePermissions((Element) entity, attributeName);
      }
    }
  }

  private void checkAttributePermissions(Mutable mutable, String attributeName)
  {
    SingleActorDAOIF userIF = this.getRequestState().getSession().getUser();

    // Write permissions are not checked when the containing object is new.
    if (mutable.isNew())
    {
      return;
    }

    // check to make sure the object is locked before setting a value on an
    // attribute
    if (mutable instanceof Element)
    {
      checkLock((Element) mutable);
    }

    MdAttributeDAOIF mdAttribute = mutable.getMdAttributeDAO(attributeName);

    // boolean hasLock = mutable.hasAttribute(ElementInfo.LOCKED_BY);
    // String lockById = mutable.getValue(ElementInfo.LOCKED_BY);

    if (this.mdMethodIFStack.size() > 0)
    {
      MdMethodDAOIF mdMethodIF = this.mdMethodIFStack.peek();

      boolean access = MethodFacade.checkAttributeAccess(mdMethodIF, Operation.WRITE, mutable, mdAttribute);

      // IMPORTANT: If the method does not have access check if the user has
      // permissions.
      if (!access)
      {
        access = SessionFacade.checkAttributeAccess(this.getRequestState().getSession().getOid(), Operation.WRITE, mutable, mdAttribute);
      }

      if (!access)
      {
        String errorMsg = "Method [" + mdMethodIF.getName() + "] does not have the write permission for attribute [" + mdAttribute.definesAttribute() + "] on type [" + mutable.getType() + "] with oid [" + mutable.getOid() + "]";
        throw new DomainErrorException(errorMsg);
      }
    }
    else
    {
      boolean access = SessionFacade.checkAttributeAccess(this.getRequestState().getSession().getOid(), Operation.WRITE, mutable, mdAttribute);

      if (!access)
      {
        String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have the write permission for attribute [" + mdAttribute.definesAttribute() + "] on type [" + mutable.getType() + "]";
        throw new AttributeWritePermissionException(errorMsg, mutable, mdAttribute, userIF);
      }
    }
  }

  private void checkAttributePermissions(Struct struct, String attributeName)
  {
    // Check permissions on Struct objects when they are instantiated directly
    // and not
    // through a containing Entity
    if (struct.getParent() == null)
    {
      // Write permissions are not checked when the containing object is new.
      if (struct.isNew())
      {
        return;
      }

      StructDAO structDAO = struct.getStructDAO();

      MdAttributeConcreteDAOIF mdAttribute = structDAO.getMdAttributeDAO(attributeName);

      if (this.mdMethodIFStack.size() > 0)
      {
        MdMethodDAOIF mdMethodIF = this.mdMethodIFStack.peek();
        boolean access = MethodFacade.checkAttributeAccess(mdMethodIF, Operation.WRITE, struct, mdAttribute);

        if (!access)
        {
          String errorMsg = "Method [" + mdMethodIF.getName() + "] does not have the write permission for attribute [" + mdAttribute.definesAttribute() + "] on type [" + struct.getType() + "] with oid [" + struct.getOid() + "]";
          throw new DomainErrorException(errorMsg);
        }
      }
      else
      {
        boolean access = SessionFacade.checkAttributeAccess(this.getRequestState().getSession().getOid(), Operation.WRITE, struct, mdAttribute);

        if (!access)
        {
          SingleActorDAOIF userIF = this.getRequestState().getSession().getUser();

          String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have the write permission for attribute [" + mdAttribute.definesAttribute() + "] on type [" + struct.getType() + "]";
          throw new AttributeWritePermissionException(errorMsg, struct, mdAttribute, userIF);
        }
      }
    }
  }

  // Check delete permission on an object
  public void checkDeletePermissions(Mutable mutable)
  {
    if (this.isSessionInitialized())
    {
      SingleActorDAOIF userIF = this.getRequestState().getSession().getUser();

      if (this.mdMethodIFStack.size() > 0)
      {
        MdMethodDAOIF mdMethodIF = this.mdMethodIFStack.peek();
        boolean access = MethodFacade.checkAccess(mdMethodIF, Operation.DELETE, mutable);

        if (!access && !checkUserDeletePermissions(mutable, userIF))
        {
          String errorMsg = "Method [" + mdMethodIF.getName() + "] does not have permission to delete [" + mutable.getType() + "] instance with oid [" + mutable.getOid() + "].";
          throw new DomainErrorException(errorMsg);
        }
      }
      else
      {
        boolean access = checkUserDeletePermissions(mutable, userIF);

        if (!access)
        {
          String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have permission to delete [" + mutable.getType() + "] instances.";
          throw new DeletePermissionException(errorMsg, mutable, userIF);
        }
      }
    }
  }

  // Check permission for adding a child to an object
  public void checkAddChildObject(Business parentBusiness, Business childBusiness, String relationshipType)
  {
    this.checkAddChildObject(parentBusiness, childBusiness.getOid(), relationshipType);
  }

  /**
   * Checks if session userIF has permission to add the given child object to
   * the given parent object.
   *
   * @param parentBusiness
   *          object that the child object is added to.
   * @param childOid
   *          reference to the child object.
   * @param relationship
   *          type of involved.
   */
  public void checkAddChildObject(Business parentBusiness, String childOid, String relationshipType)
  {
    if (this.isSessionInitialized())
    {

      BusinessDAO parentBusinessDAO = parentBusiness.businessDAO();

      MdRelationshipDAOIF mdRelationshipIF = MdRelationshipDAO.getMdRelationshipDAO(relationshipType);

      if (this.mdMethodIFStack.size() > 0)
      {
        MdMethodDAOIF mdMethodIF = this.mdMethodIFStack.peek();
        boolean access = MethodFacade.checkRelationshipAccess(mdMethodIF, Operation.ADD_CHILD, parentBusiness, mdRelationshipIF.getOid());

        if (!access)
        {

          String errorMsg = "Method [" + mdMethodIF.getName() + "] does not have the add_child permission for relationship [" + mdRelationshipIF.definesType() + "] on type [" + parentBusinessDAO.getType() + "]";
          throw new DomainErrorException(errorMsg);
        }
      }
      else
      {
        boolean access = SessionFacade.checkRelationshipAccess(this.getRequestState().getSession().getOid(), Operation.ADD_CHILD, parentBusiness, mdRelationshipIF.getOid());

        if (!access)
        {
          Business childBusiness = Business.get(childOid);
          SingleActorDAOIF userIF = this.getRequestState().getSession().getUser();

          String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have the add_child permission for relationship [" + mdRelationshipIF.definesType() + "] on type [" + parentBusinessDAO.getType() + "]";
          throw new AddChildPermissionException(errorMsg, parentBusiness, childBusiness, mdRelationshipIF, userIF);
        }
      }
    }
  }

  // Check permission for removing a child from an object
  public void removeChildObject(Business parentBusiness, Relationship relationship)
  {
    if (this.isSessionInitialized())
    {
      this.checkRemoveChildObject(parentBusiness, relationship.getChildOid(), relationship.getType());
    }
  }

  public void removeChildOid(Business parentBusiness, String relationshipId)
  {
    if (this.isSessionInitialized())
    {
      MdClassDAOIF mdClassIF = MdClassDAO.getMdClassByRootId(IdParser.parseMdTypeRootIdFromId(relationshipId));
      Relationship relationship = Relationship.get(relationshipId);

      this.checkRemoveChildObject(parentBusiness, relationship.getChildOid(), mdClassIF.definesType());
    }
  }

  public void removeAllChildObjects(Business parentBusiness, Business childBusiness, String relationshipType)
  {
    if (this.isSessionInitialized())
    {
      this.checkRemoveChildObject(parentBusiness, childBusiness.getOid(), relationshipType);
    }
  }

  public void removeAllChildOid(Business parentBusiness, String childOid, String relationshipType)
  {
    if (this.isSessionInitialized())
    {
      this.checkRemoveChildObject(parentBusiness, childOid, relationshipType);
    }
  }

  // Check permission for adding a parent to an object
  public void addParentObject(Business parentBusiness, Business childBusiness, String relationshipType)
  {
    if (this.isSessionInitialized())
    {
      this.checkAddParentObject(childBusiness, parentBusiness.getOid(), relationshipType);
    }
  }

  // Check permission for adding a parent to an object
  public void addParentOid(String parentOid, Business childBusiness, String relationshipType)
  {
    if (this.isSessionInitialized())
    {
      this.checkAddParentObject(childBusiness, parentOid, relationshipType);
    }
  }

  // Check permission for adding a parent to an object
  public void removeParentObject(Business childBusiness, Relationship relationship)
  {
    if (this.isSessionInitialized())
    {
      this.checkRemoveParentObject(childBusiness, relationship.getParentOid(), relationship.getType());
    }
  }

  public void removeParentOid(Business childBusiness, String relationshipId)
  {
    if (this.isSessionInitialized())
    {
      MdClassDAOIF mdClassIF = MdClassDAO.getMdClassByRootId(IdParser.parseMdTypeRootIdFromId(relationshipId));
      Relationship relationship = Relationship.get(relationshipId);
      this.checkRemoveParentObject(childBusiness, relationship.getParentOid(), mdClassIF.definesType());
    }
  }

  public void removeAllParentObjects(Business parentBusiness, Business childBusiness, String relationshipType)
  {
    if (this.isSessionInitialized())
    {
      checkRemoveParentObject(childBusiness, parentBusiness.getOid(), relationshipType);
    }
  }

  // Check permission for adding a parent to an object
  public void removeAllParentOids(String parentOid, Business childBusiness, String relationshipType)
  {
    if (this.isSessionInitialized())
    {
      checkRemoveParentObject(childBusiness, parentOid, relationshipType);
    }
  }

  /**
   * Checks if session userIF has permission to remove the given parent object
   * from the given child object.
   *
   * @param childBusiness
   *          object that the parent object is removed from.
   * @param parentOid
   *          reference to the child object.
   * @param relationshipType
   *          name of the relationship type.
   * @param relationshipId
   *          reference to the relationship.
   */
  private void checkRemoveParentObject(Business childBusiness, String parentOid, String relationshipType)
  {
    BusinessDAO childBusinessDAO = childBusiness.businessDAO();

    MdRelationshipDAOIF mdRelationshipIF = MdRelationshipDAO.getMdRelationshipDAO(relationshipType);

    if (this.mdMethodIFStack.size() > 0)
    {
      MdMethodDAOIF mdMethodIF = this.mdMethodIFStack.peek();
      boolean access = MethodFacade.checkRelationshipAccess(mdMethodIF, Operation.DELETE_PARENT, childBusiness, mdRelationshipIF.getOid());

      if (!access)
      {
        String errorMsg = "Method [" + mdMethodIF.getName() + "] does not have the delete_parent permission for relationship [" + mdRelationshipIF.definesType() + "] on type [" + childBusinessDAO.getType() + "]";
        throw new DomainErrorException(errorMsg);
      }
    }
    else
    {
      boolean access = SessionFacade.checkRelationshipAccess(this.getRequestState().getSession().getOid(), Operation.DELETE_PARENT, childBusiness, mdRelationshipIF.getOid());

      if (!access)
      {
        Business parentBusiness = Business.get(parentOid);
        SingleActorDAOIF userIF = this.getRequestState().getSession().getUser();

        String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have the delete_parent permission for relationship [" + mdRelationshipIF.definesType() + "] on type [" + childBusinessDAO.getType() + "]";
        throw new DeleteParentPermissionException(errorMsg, parentBusiness, childBusiness, mdRelationshipIF, userIF);
      }
    }
  }

  /**
   * Throws a business exception if the userIF bound to the given session does
   * not have a lock on the given element.
   *
   * @param element
   */
  private void checkLock(Element element)
  {
    if (element.isNew())
    {
      return;
    }

    // If a thread has a lock on it, it could be an application or a user lock.
    Thread threadThatLocksObject = ( LockObject.getLockObject() ).getThreadForAppLock(element.getOid());

    if (threadThatLocksObject != null)
    {
      // User has an app lock
      if (LockObject.getCurrentThread() == threadThatLocksObject)
      {
        return;
      }
      else
      {
        String err = "You must obtain an application lock before modifying an object. " + "Another thread has a lock on the object with oid [" + element.getOid() + "].";
        throw new LockException(err);
      }
    }
    ( LockObject.getLockObject() ).appLock(element.getOid());

    SingleActorDAOIF userIF = this.getRequestState().getSession().getUser();

    // Check if the object is locked by another user.
    if (element.hasAttribute(ElementInfo.LOCKED_BY))
    {
      String lockedBy = element.getValue(ElementInfo.LOCKED_BY);
      // we do not perform the lockedby check on new BusinessDAOs
      if (!lockedBy.equals(userIF.getOid()) && !lockedBy.trim().equals(""))
      {
        ( LockObject.getLockObject() ).releaseAppLock(element.getOid());

        String err = "User [" + userIF.getSingleActorName() + "] must have a lock on the object with oid [" + element.getOid() + "] in order to modify it. " + "It is currently locked by someone else.";
        throw new LockException(err, element, "ExistingLockException");
      }
      // User has a user lock
      else if (lockedBy.equals(userIF.getOid()))
      {
        return;
      }
    }

    ( LockObject.getLockObject() ).releaseAppLock(element.getOid());

    String err = "User [" + userIF.getSingleActorName() + "] must have a lock on the object with oid [" + element.getOid() + "] in order to modify it. ";
    throw new LockException(err, element, "NeedLockException");
  }

  /**
   * Checks if session userIF has permission to add the given parent object to
   * the given child object.
   *
   * @param childBusiness
   *          object that the parent object is added to.
   * @param parentOid
   *          oid to the child object.
   * @param relationship
   *          type of relationship involved.
   */
  private void checkAddParentObject(Business childBusiness, String parentOid, String relationshipType)
  {
    BusinessDAO childBusinessDAO = childBusiness.businessDAO();

    MdRelationshipDAOIF mdRelationshipIF = MdRelationshipDAO.getMdRelationshipDAO(relationshipType);

    if (this.mdMethodIFStack.size() > 0)
    {
      MdMethodDAOIF mdMethodIF = this.mdMethodIFStack.peek();
      boolean access = MethodFacade.checkRelationshipAccess(mdMethodIF, Operation.ADD_PARENT, childBusiness, mdRelationshipIF.getOid());

      if (!access)
      {
        String errorMsg = "Method [" + mdMethodIF.getName() + "] does not have the add_parent permission for relationship [" + mdRelationshipIF.definesType() + "] on type [" + childBusinessDAO.getType() + "]";
        throw new DomainErrorException(errorMsg);
      }
    }
    else
    {
      boolean access = SessionFacade.checkRelationshipAccess(this.getRequestState().getSession().getOid(), Operation.ADD_PARENT, childBusiness, mdRelationshipIF.getOid());

      if (!access)
      {
        Business parentBusiness = Business.get(parentOid);
        SingleActorDAOIF userIF = this.getRequestState().getSession().getUser();

        String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have the add_parent permission for relationship [" + mdRelationshipIF.definesType() + "] on type [" + childBusinessDAO.getType() + "]";
        throw new AddParentPermissionException(errorMsg, parentBusiness, childBusiness, mdRelationshipIF, userIF);
      }
    }
  }

  /**
   * Checks if session userIF has permission to remove the given child object
   * from the given parent object. Either childOid or relationshipId is null,
   * but not both.
   *
   *
   * @param parentBusiness
   *          object that the child object is removed from.
   * @param childOid
   *          oid to the child object.
   * @param relationshipType
   *          name of the relationship type.
   * @param relationshipId
   *          oid to the relationship.
   */
  private void checkRemoveChildObject(Business parentBusiness, String childOid, String relationshipType)
  {
    BusinessDAO parentBusinessDAO = parentBusiness.businessDAO();

    MdRelationshipDAOIF mdRelationshipIF = MdRelationshipDAO.getMdRelationshipDAO(relationshipType);

    if (this.mdMethodIFStack.size() > 0)
    {
      MdMethodDAOIF mdMethodIF = this.mdMethodIFStack.peek();
      boolean access = MethodFacade.checkRelationshipAccess(mdMethodIF, Operation.DELETE_CHILD, parentBusiness, mdRelationshipIF.getOid());

      if (!access)
      {
        SingleActorDAOIF userIF = this.getRequestState().getSession().getUser();

        String errorMsg = "Method [" + userIF.getSingleActorName() + "] does not have the delete_child permission for relationship [" + mdRelationshipIF.definesType() + "] on type [" + parentBusinessDAO.getType() + "]";
        throw new DomainErrorException(errorMsg);
      }
    }
    else
    {
      boolean access = SessionFacade.checkRelationshipAccess(this.getRequestState().getSession().getOid(), Operation.DELETE_CHILD, parentBusiness, mdRelationshipIF.getOid());

      if (!access)
      {
        Business childBusiness = Business.get(childOid);
        SingleActorDAOIF userIF = this.getRequestState().getSession().getUser();

        String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have the delete_child permission for relationship [" + mdRelationshipIF.definesType() + "] on type [" + parentBusinessDAO.getType() + "]";
        throw new DeleteChildPermissionException(errorMsg, parentBusiness, childBusiness, mdRelationshipIF, userIF);
      }
    }
  }

  private boolean checkUserDeletePermissions(Mutable mutable, SingleActorDAOIF userIF)
  {
    // Check if userIF has a lock on the object.
    if (mutable.hasAttribute(ElementInfo.LOCKED_BY))
    {
      Element element = (Element) mutable;

      ( LockObject.getLockObject() ).appLock(mutable.getOid());

      element.setDataEntity( ( EntityDAO.get(element.getOid()).getEntityDAO() ));

      String lockedBy = element.getValue(ElementInfo.LOCKED_BY);

      // we do not perform the lockedby check on new BusinessDAOs
      if (!lockedBy.equals(userIF.getOid()) && !lockedBy.trim().equals(""))
      {
        // Release the lock on this object
        ( LockObject.getLockObject() ).releaseAppLock(element.getOid());

        String err = "User [" + userIF.getSingleActorName() + "] must have a lock on the object in order to delete it.";
        throw new LockException(err, element, "ExistingLockException");
      }
    }

    return SessionFacade.checkAccess(this.getRequestState().getSession().getOid(), Operation.DELETE, mutable);
  }

  private void checkMethodExecutePermission_INSTANCE(Mutable mutable, MdMethodDAOIF mdMethodIF, MdMethodDAOIF mdMethodToExecute)
  {
    boolean access = MethodFacade.checkExecuteAccess(mdMethodIF, mdMethodToExecute);

    if (!access)
    {
      String errorMsg = "Method [" + mdMethodIF.getName() + "] does not have execute permission for the method [" + mdMethodToExecute.getName() + "] on entity [" + mutable.getOid() + "]";
      throw new DomainErrorException(errorMsg);
    }
  }

  private void checkMethodExecutePermission_STATIC(MdTypeDAOIF mdTypeIF, MdMethodDAOIF mdMethodIF, MdMethodDAOIF mdMethodToExecute)
  {
    boolean access = MethodFacade.checkExecuteAccess(mdMethodIF, mdMethodToExecute);

    if (!access)
    {
      String errorMsg = "Method [" + mdMethodIF.getName() + "] does not have execute permission for the method [" + mdMethodToExecute.getName() + "] on [" + mdTypeIF.definesType() + "]";
      throw new DomainErrorException(errorMsg);
    }
  }

  private void checkMethodExecutePermission_INSTANCE(SingleActorDAOIF userIF, Mutable mutable, MdMethodDAOIF mdMethodToExecute)
  {
    boolean access = SessionFacade.checkMethodAccess(this.getRequestState().getSession().getOid(), Operation.EXECUTE, mutable, mdMethodToExecute);

    if (!access)
    {
      String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have the execute permission for the method [" + mdMethodToExecute.getName() + "] on entity [" + mutable.getOid() + "]";
      throw new ExecuteInstancePermissionException(errorMsg, mutable, mdMethodToExecute, userIF);
    }
  }

  private void checkMethodExecutePermission_STATIC(SingleActorDAOIF userIF, MdTypeDAOIF mdTypeIF, MdMethodDAOIF mdMethodToExecute)
  {
    boolean access = SessionFacade.checkMethodAccess(this.getRequestState().getSession().getOid(), Operation.EXECUTE, mdMethodToExecute);

    if (!access)
    {
      String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have the execute permission for the method [" + mdMethodToExecute.getName() + "] on [" + mdTypeIF.definesType() + "]";
      throw new ExecuteStaticPermissionException(errorMsg, mdTypeIF, mdMethodToExecute, userIF);
    }
  }

  /**
   * Checks if the session userIF has create permissions on the given Entity.
   *
   * @param entity
   */
  private void checkEntityCreatePermission(Mutable mutable)
  {
    boolean access = SessionFacade.checkAccess(this.getRequestState().getSession().getOid(), Operation.CREATE, mutable);

    if (!access)
    {
      SingleActorDAOIF sessionUserIF = this.getRequestState().getSession().getUser();
      String errorMsg = "User [" + sessionUserIF.getSingleActorName() + "] does not have permission to create [" + mutable.getType() + "] instances.";
      throw new CreatePermissionException(errorMsg, mutable, sessionUserIF);
    }
  }

  /**
   * Checks if the currently executing method has create permissions on the
   * given Entity.
   *
   * @param mdMethodIF
   * @param entity
   */
  private void checkEntityCreatePermission(MdMethodDAOIF mdMethodIF, Entity entity)
  {
    boolean access = MethodFacade.checkAccess(mdMethodIF, Operation.CREATE, entity);

    if (!access)
    {
      String errorMsg = "Method [" + mdMethodIF.getName() + "] does not have permission to create [" + entity.getType() + "] instances.";
      throw new DomainErrorException(errorMsg);
    }
  }

  /**
   * Checks if the session userIF has create permissions on the given Entity.
   *
   * @param entity
   */
  private void checkEntityReadPermission(Entity entity)
  {
    boolean access = SessionFacade.checkAccess(this.getRequestState().getSession().getOid(), Operation.READ, entity);

    if (!access)
    {
      SingleActorDAOIF sessionUserIF = this.getRequestState().getSession().getUser();
      String errorMsg = "User [" + sessionUserIF.getSingleActorName() + "] does not have permission to read [" + entity.getType() + "] instances.";
      throw new ReadPermissionException(errorMsg, entity, sessionUserIF);
    }
  }

  /**
   * Checks if the currently executing method has read permissions on the given
   * Entity.
   *
   * @param mdMethodIF
   * @param entity
   */
  private void checkEntityReadPermission(MdMethodDAOIF mdMethodIF, Entity entity)
  {
    boolean access = MethodFacade.checkAccess(mdMethodIF, Operation.READ, entity);

    if (!access)
    {
      String errorMsg = "Method [" + mdMethodIF.getName() + "] does not have permission to read [" + entity.getType() + "] instances.";
      throw new DomainErrorException(errorMsg);
    }
  }

  /**
   * Checks if the session userIF has write permissions on the given Entity.
   *
   * @param entity
   */
  private void checkEntityWritePermission(Mutable entity)
  {
    boolean access = SessionFacade.checkAccess(this.getRequestState().getSession().getOid(), Operation.WRITE, entity);

    if (!access)
    {
      SingleActorDAOIF sessionUserIF = this.getRequestState().getSession().getUser();
      String errorMsg = "User [" + sessionUserIF.getSingleActorName() + "] does not have permission to write to [" + entity.getType() + "] instances.";
      throw new WritePermissionException(errorMsg, entity, sessionUserIF);
    }
  }

  /**
   * Checks if the currently executing method has write permissions on the given
   * Entity.
   *
   * @param mdMethodIF
   * @param entity
   */
  private void checkEntityWritePermission(MdMethodDAOIF mdMethodIF, Entity entity)
  {
    boolean access = MethodFacade.checkAccess(mdMethodIF, Operation.WRITE, entity);

    if (!access && !SessionFacade.checkAccess(this.getRequestState().getSession().getOid(), Operation.WRITE, entity))
    {
      String errorMsg = "Method [" + mdMethodIF.getName() + "] does not have permission to write to [" + entity.getType() + "] instance with oid [" + entity.getOid() + "].";
      throw new DomainErrorException(errorMsg);
    }
  }

  public void throwMessage(Message message)
  {
    if (message instanceof Warning)
    {
      log.info(RunwayLogUtil.formatLoggableMessage(message.getDeveloperMessage(), message.getLocalizedMessage()));
    }
    else if (message instanceof Information)
    {
      log.info(RunwayLogUtil.formatLoggableMessage(message.getDeveloperMessage(), message.getLocalizedMessage()));
    }

    messageList.add(message);
  }

  public void throwAttributeNotification(AttributeNotification attributeNotification)
  {
    String mapKey = AttributeNotificationMap.getMapKey(attributeNotification.getComponentId(), attributeNotification.getAttributeName());
    AttributeNotificationMap attributeNotificationMap = this.attributeNotificationMapMap.get(mapKey);

    if (attributeNotificationMap != null)
    {
      attributeNotificationMap.convertAttributeNotification(attributeNotification);
    }
  }

  // //////////////////////////////////////////////////////////////////////////////////////////////////////
  // GRAPH OBJECT ASPECTS AND METHODS
  // //////////////////////////////////////////////////////////////////////////////////////////////////////

  // Used to ensure that a userIF must have a lock on the object in order to
  // modify it.
  public void applyGraphObject(GraphObject graph)
  {
    if (this.isSessionInitialized())
    {
      if (graph.isNew())
      {
        this.checkEntityCreatePermission(graph);
      }
      else
      {
        this.checkEntityWritePermission(graph);
      }
    }
  }

  // Check permission for adding a child to an object
  public void addChildVertex(VertexObject parent, VertexObject child, MdEdgeDAOIF mdEdge)
  {
    if (this.isSessionInitialized())
    {
      this.checkAddChildObject(parent, child.getOid(), mdEdge);
    }
  }

  /**
   * Checks if session userIF has permission to add the given child object to
   * the given parent object.
   *
   * @param parentBusiness
   *          object that the child object is added to.
   * @param childOid
   *          reference to the child object.
   * @param relationship
   *          type of involved.
   */
  private void checkAddChildObject(VertexObject parent, String childOid, MdEdgeDAOIF mdEdge)
  {
    boolean access = SessionFacade.checkEdgeAccess(this.getRequestState().getSession().getOid(), Operation.ADD_CHILD, parent, mdEdge.getOid());

    if (!access)
    {
      VertexObject child = VertexObject.get(mdEdge.getChildMdVertex(), childOid);
      SingleActorDAOIF userIF = this.getRequestState().getSession().getUser();

      String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have the add_child permission for relationship [" + mdEdge.definesType() + "] on type [" + parent.getType() + "]";
      throw new AddChildPermissionException(errorMsg, parent, child, mdEdge, userIF);
    }
  }

  // Check permission for removing a child from an object
  public void removeChildVertex(VertexObject parent, VertexObject child, MdEdgeDAOIF mdEdge)
  {
    if (this.isSessionInitialized())
    {
      this.checkRemoveChildObject(parent, child.getOid(), mdEdge);
    }
  }

  /**
   * Checks if session userIF has permission to remove the given child object
   * from the given parent object. Either childOid or relationshipId is null,
   * but not both.
   *
   *
   * @param parent
   *          object that the child object is removed from.
   * @param childOid
   *          oid to the child object.
   * @param relationshipType
   *          name of the relationship type.
   * @param relationshipId
   *          oid to the relationship.
   */
  private void checkRemoveChildObject(VertexObject parent, String childOid, MdEdgeDAOIF mdEdge)
  {
    boolean access = SessionFacade.checkEdgeAccess(this.getRequestState().getSession().getOid(), Operation.DELETE_CHILD, parent, mdEdge.getOid());

    if (!access)
    {
      VertexObject child = VertexObject.get(mdEdge.getChildMdVertex(), childOid);
      SingleActorDAOIF userIF = this.getRequestState().getSession().getUser();

      String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have the add_child permission for relationship [" + mdEdge.definesType() + "] on type [" + parent.getType() + "]";
      throw new DeleteChildPermissionException(errorMsg, parent, child, mdEdge, userIF);
    }
  }

  // Check permission for adding a parent to an object
  public void addParentVertex(VertexObject child, VertexObject parent, MdEdgeDAOIF mdEdge)
  {
    if (this.isSessionInitialized())
    {
      this.checkAddParentObject(child, parent.getOid(), mdEdge);
    }
  }

  /**
   * Checks if session userIF has permission to add the given parent object to
   * the given child object.
   *
   * @param childBusiness
   *          object that the parent object is added to.
   * @param parentOid
   *          reference to the parent object.
   * @param relationship
   *          type of involved.
   */
  private void checkAddParentObject(VertexObject child, String parentOid, MdEdgeDAOIF mdEdge)
  {
    boolean access = SessionFacade.checkEdgeAccess(this.getRequestState().getSession().getOid(), Operation.ADD_CHILD, child, mdEdge.getOid());

    if (!access)
    {
      VertexObject parent = VertexObject.get(mdEdge.getParentMdVertex(), parentOid);
      SingleActorDAOIF userIF = this.getRequestState().getSession().getUser();

      String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have the add_parent permission for relationship [" + mdEdge.definesType() + "] on type [" + child.getType() + "]";
      throw new AddParentPermissionException(errorMsg, child, parent, mdEdge, userIF);
    }
  }

  // Check permission for removing a parent from an object
  public void removeParentVertex(VertexObject parent, VertexObject child, MdEdgeDAOIF mdEdge)
  {
    if (this.isSessionInitialized())
    {
      this.checkRemoveParentObject(child, parent.getOid(), mdEdge);
    }
  }

  /**
   * Checks if session userIF has permission to remove the given parent object
   * from the given child object. Either parentOid or relationshipId is null,
   * but not both.
   *
   *
   * @param child
   *          object that the parent object is removed from.
   * @param parentOid
   *          oid to the parent object.
   * @param relationshipType
   *          name of the relationship type.
   * @param relationshipId
   *          oid to the relationship.
   */
  private void checkRemoveParentObject(VertexObject child, String parentOid, MdEdgeDAOIF mdEdge)
  {
    boolean access = SessionFacade.checkEdgeAccess(this.getRequestState().getSession().getOid(), Operation.DELETE_CHILD, child, mdEdge.getOid());

    if (!access)
    {
      VertexObject parent = VertexObject.get(mdEdge.getParentMdVertex(), parentOid);
      SingleActorDAOIF userIF = this.getRequestState().getSession().getUser();

      String errorMsg = "User [" + userIF.getSingleActorName() + "] does not have the add_parent permission for relationship [" + mdEdge.definesType() + "] on type [" + child.getType() + "]";
      throw new DeleteParentPermissionException(errorMsg, child, parent, mdEdge, userIF);
    }
  }

  // Check write permission on an object attribute
  public void modifyGraphAttribute(GraphObject entity, String attributeName)
  {
    if (this.isSessionInitialized())
    {
      checkAttributePermissions((GraphObject) entity, attributeName);
    }
  }

}
