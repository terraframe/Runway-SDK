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
package com.runwaysdk.dataaccess.io.dataDefinition;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.rbac.ActorDAOIF;
import com.runwaysdk.business.rbac.MethodActorDAOIF;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.OperationManager;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.business.rbac.SingleActorDAOIF;
import com.runwaysdk.business.rbac.UserDAOIF;
import com.runwaysdk.business.state.MdStateMachineDAOIF;
import com.runwaysdk.business.state.StateMasterDAOIF;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDimensionDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdDimensionDAOIF;
import com.runwaysdk.dataaccess.MdFacadeDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.MdUtilDAOIF;
import com.runwaysdk.dataaccess.MdViewDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.io.MarkupWriter;
import com.runwaysdk.dataaccess.metadata.TypeTupleDAO;
import com.runwaysdk.dataaccess.metadata.TypeTupleDAOIF;

public abstract class PermissionVisitor extends MarkupVisitor
{
  private MarkupWriter writer;

  private List<String> exportedTypes;

  public PermissionVisitor(MarkupWriter writer)
  {
    this.writer = writer;
    this.exportedTypes = new LinkedList<String>();
  }

  /**
   * @return The XML tag associated with the action to export
   */
  protected abstract String getAction();

  public void visit(ComponentIF component)
  {
    if (component instanceof UserDAOIF)
    {
      // Export the user definition and the permissions of the user
      visitUser((UserDAOIF) component);
    }
    else if (component instanceof RoleDAOIF)
    {
      visitRole((RoleDAOIF) component);
    }
    else if (component instanceof MethodActorDAOIF)
    {
      visitMethodActor((MethodActorDAOIF) component);
    }
    else if (component instanceof PermissionComponent)
    {
      visitPermissionComponent((PermissionComponent) component);
    }
  }

  public void visitPermissionComponent(PermissionComponent component)
  {
    RoleDAOIF roleIF = component.getComponent();

    HashMap<String, String> attributes = new HashMap<String, String>();
    attributes.put(XMLTags.ROLENAME_ATTRIBUTE, roleIF.getRoleName());
    writer.openEscapedTag(XMLTags.ROLE_TAG, attributes);

    exportSuperRoles(roleIF);

    // Export all permissions
    writer.openTag(this.getAction());

    if (component.exportRole())
    {
      for (RelationshipDAOIF permission : roleIF.getAllPermissions())
      {
        visitPermission(permission, roleIF);
      }
    }

    List<MdBusinessDAOIF> mdBusinesses = component.getAllPermissions();

    for (MdBusinessDAOIF mdBusiness : mdBusinesses)
    {
      this.visitMdBusiness(null, roleIF, mdBusiness, true);
    }

    writer.closeTag();

    writer.closeTag();
  }

  public void visitMethodActor(MethodActorDAOIF methodActor)
  {
    MdMethodDAOIF mdMethodIF = methodActor.getMdMethodDAO();
    HashMap<String, String> attributes = new HashMap<String, String>();
    attributes.put(XMLTags.TYPE_ATTRIBUTE, mdMethodIF.getEnclosingMdTypeDAO().definesType());
    attributes.put(XMLTags.METHOD_NAME_ATTRIBUTE, mdMethodIF.getName());

    writer.openEscapedTag(XMLTags.METHOD_TAG, attributes);

    if (methodActor != null)
    {
      visitSingleActor(methodActor);
    }

    writer.closeTag();
  }

  public void visitRole(RoleDAOIF roleIF)
  {
    HashMap<String, String> attributes = new HashMap<String, String>();
    attributes.put(XMLTags.ROLENAME_ATTRIBUTE, roleIF.getRoleName());
    writer.openEscapedTag(XMLTags.ROLE_TAG, attributes);

    exportSuperRoles(roleIF);

    // Export all permissions
    writer.openTag(this.getAction());

    for (RelationshipDAOIF permission : roleIF.getAllPermissions())
    {
      visitPermission(permission, roleIF);
    }

    writer.closeTag();

    writer.closeTag();
  }

  protected void exportSuperRoles(RoleDAOIF roleIF)
  {
    for (RoleDAOIF superRole : roleIF.getSuperRoles())
    {
      HashMap<String, String> roleAttribute = new HashMap<String, String>();
      roleAttribute.put(XMLTags.ROLENAME_ATTRIBUTE, superRole.getRoleName());

      writer.writeEmptyEscapedTag(XMLTags.SUPER_ROLE_TAG, roleAttribute);
    }
  }

  private void visitSingleActor(SingleActorDAOIF singleActor)
  {
    exportAssignedRoles(singleActor);

    // Export all permissions
    writer.openTag(this.getAction());

    for (RelationshipDAOIF permission : singleActor.getAllPermissions())
    {
      visitPermission(permission, singleActor);
    }

    writer.closeTag();
  }

  protected void exportAssignedRoles(SingleActorDAOIF singleActor)
  {
    for (RoleDAOIF superRole : singleActor.assignedRoles())
    {
      HashMap<String, String> roleAttribute = new HashMap<String, String>();
      roleAttribute.put(XMLTags.ROLENAME_ATTRIBUTE, superRole.getRoleName());

      writer.writeEmptyEscapedTag(XMLTags.ASSIGNED_ROLE_TAG, roleAttribute);
    }
  }

  public void visitUser(UserDAOIF userIF)
  {
    HashMap<String, String> attributes = new HashMap<String, String>();
    attributes.put(XMLTags.USERNAME_ATTRIBUTE, userIF.getSingleActorName());

    writer.openEscapedTag(XMLTags.USER_TAG, attributes);

    visitSingleActor(userIF);

    writer.closeTag();
  }

  private void visitPermission(RelationshipDAOIF permission, ActorDAOIF actorIF)
  {
    // Get the MetaData of the permission
    BusinessDAOIF metadata = permission.getChild();

    if (metadata instanceof MdMethodDAOIF)
    {
      MdMethodDAOIF mdMethod = (MdMethodDAOIF) metadata;
      MdTypeDAOIF enclosingMdType = mdMethod.getEnclosingMdTypeDAO();

      if (!exportedTypes.contains(actorIF.getId() + enclosingMdType.getId()))
      {
        visitPermission(actorIF, enclosingMdType);
      }
    }
    else if (metadata instanceof MdAttributeConcreteDAOIF)
    {
      MdAttributeConcreteDAOIF mdAttribute = (MdAttributeConcreteDAOIF) metadata;
      MdClassDAOIF mdClass = mdAttribute.definedByClass();

      if (!exportedTypes.contains(actorIF.getId() + mdClass.getId()))
      {
        visitPermission(actorIF, mdClass);
      }
    }
    else if (metadata instanceof MdAttributeDimensionDAOIF)
    {
      MdAttributeDimensionDAOIF mdAttribute = (MdAttributeDimensionDAOIF) metadata;
      MdClassDAOIF mdClass = mdAttribute.definingMdAttribute().definedByClass();

      if (!exportedTypes.contains(actorIF.getId() + mdClass.getId()))
      {
        visitPermission(actorIF, mdClass);
      }
    }
    else
    {
      visitPermission(actorIF, metadata);
    }
  }

  private void visitPermission(ActorDAOIF actorIF, BusinessDAOIF metadata)
  {
    try
    {
      for (RelationshipDAOIF permission : RelationshipDAO.get(actorIF.getId(), metadata.getId(), RelationshipTypes.TYPE_PERMISSION.getType()))
      {
        this.visitPermission(permission, actorIF, metadata);
      }
    }
    catch (DataNotFoundException e)
    {
      this.visitPermission(RelationshipDAO.newInstance(actorIF.getId(), metadata.getId(), RelationshipTypes.TYPE_PERMISSION.getType()), actorIF, metadata);
    }
  }

  private void visitPermission(RelationshipDAOIF permission, ActorDAOIF actorIF, BusinessDAOIF metadata)
  {
    if (!exportedTypes.contains(actorIF.getId() + metadata.getId()))
    {
      // Only export MdEntities as top level permission tags
      if (metadata instanceof MdBusinessDAOIF)
      {
        visitMdBusiness(permission, actorIF, (MdBusinessDAOIF) metadata, false);
        exportedTypes.add(actorIF.getId() + metadata.getId());
      }
      else if (metadata instanceof MdRelationshipDAOIF)
      {
        // Export the attribute permissions of the MdEntity
        visitMdRelationship(permission, actorIF, (MdRelationshipDAOIF) metadata);
        exportedTypes.add(actorIF.getId() + metadata.getId());
      }
      else if (metadata instanceof MdStructDAOIF)
      {
        visitMdStruct(permission, actorIF, (MdStructDAOIF) metadata);
        exportedTypes.add(actorIF.getId() + metadata.getId());
      }
      else if (metadata instanceof MdViewDAOIF)
      {
        visitMdView(permission, actorIF, (MdViewDAOIF) metadata);
        exportedTypes.add(actorIF.getId() + metadata.getId());
      }
      else if (metadata instanceof MdUtilDAOIF)
      {
        visitMdUtil(permission, actorIF, (MdUtilDAOIF) metadata);
        exportedTypes.add(actorIF.getId() + metadata.getId());
      }
      else if (metadata instanceof MdFacadeDAOIF)
      {
        visitMdFacade(permission, actorIF, (MdFacadeDAOIF) metadata);
        exportedTypes.add(actorIF.getId() + metadata.getId());
      }
    }
  }

  private void visitMdRelationship(RelationshipDAOIF permission, ActorDAOIF actorIF, MdRelationshipDAOIF mdRelationship)
  {
    HashMap<String, String> attributes = new HashMap<String, String>();
    attributes.put(XMLTags.TYPE_ATTRIBUTE, mdRelationship.definesType());

    // Open MdBusiness Permission tag
    writer.openEscapedTag(XMLTags.MD_RELATIONSHIP_PERMISSION_TAG, attributes);

    // Export the permissions on the mdBusiness
    visitOperations(OperationManager.getOperations(permission));

    // Export the attribute permissions of the MdEntity
    for (MdAttributeConcreteDAOIF mdAttribute : mdRelationship.definesAttributes())
    {
      visitMdAttribute(mdAttribute, actorIF);
    }

    // If the MdEntity is a MdRelationship and its parent or child have a
    // MdStateMachine then
    // export the directional state permissions
    visitParentState(actorIF, mdRelationship);

    visitChildState(actorIF, mdRelationship);

    // Close MdBusiness Permission tag
    writer.closeTag();
  }

  private void visitParentState(ActorDAOIF actorIF, MdRelationshipDAOIF mdRelationship)
  {
    MdBusinessDAOIF parentMdBusiness = mdRelationship.getParentMdBusiness();

    MdStateMachineDAOIF mdStateMachine = null;

    if (parentMdBusiness.hasStateMachine())
    {
      mdStateMachine = parentMdBusiness.definesMdStateMachine();
    }

    if (mdStateMachine != null)
    {
      for (StateMasterDAOIF stateMaster : mdStateMachine.definesStateMasters())
      {
        TypeTupleDAOIF tuple = TypeTupleDAO.findTuple(mdRelationship.getId(), stateMaster.getId());

        if (tuple != null)
        {
          HashMap<String, String> attributes = new HashMap<String, String>();
          attributes.put(XMLTags.STATE_NAME_ATTRIBUTE, stateMaster.getName());

          // Open MdBusiness Permission tag
          writer.openEscapedTag(XMLTags.PARENT_STATE_PERMISSION_TAG, attributes);

          visitOperations(actorIF.getAllPermissions(tuple));

          writer.closeTag();
        }
      }
    }
  }

  private void visitChildState(ActorDAOIF actorIF, MdRelationshipDAOIF mdRelationship)
  {
    MdBusinessDAOIF childMdBusiness = mdRelationship.getChildMdBusiness();

    MdStateMachineDAOIF mdStateMachine = null;

    if (childMdBusiness.hasStateMachine())
    {
      mdStateMachine = childMdBusiness.definesMdStateMachine();
    }

    if (mdStateMachine != null)
    {
      for (StateMasterDAOIF stateMaster : mdStateMachine.definesStateMasters())
      {
        TypeTupleDAOIF tuple = TypeTupleDAO.findTuple(mdRelationship.getId(), stateMaster.getId());

        if (tuple != null)
        {
          HashMap<String, String> attributes = new HashMap<String, String>();
          attributes.put(XMLTags.STATE_NAME_ATTRIBUTE, stateMaster.getName());

          // Open MdBusiness Permission tag
          writer.openEscapedTag(XMLTags.CHILD_STATE_PERMISSION_TAG, attributes);

          visitOperations(actorIF.getAllPermissions(tuple));

          writer.closeTag();
        }
      }
    }
  }

  private void visitMdClass(RelationshipDAOIF permission, ActorDAOIF actorIF, MdClassDAOIF mdClass, Boolean all)
  {
    // Export the permissions on the mdBusiness
    if (permission != null && !all)
    {
      visitOperations(OperationManager.getOperations(permission));
    }
    else
    {
      HashMap<String, String> attributes = new HashMap<String, String>();
      attributes.put(XMLTags.NAME_ATTRIBUTE, XMLTags.ALL);

      writer.writeEmptyEscapedTag(XMLTags.OPERATION_TAG, attributes);
    }

    // Export the attribute permissions of the MdClass
    for (MdAttributeDAOIF mdAttribute : mdClass.definesAttributes())
    {
      visitMdAttribute(mdAttribute, actorIF);
    }

    // Export the MdMethod permissions of the MdClass
    for (MdMethodDAOIF mdMethod : mdClass.getMdMethods())
    {
      visitMdMethod(mdMethod, actorIF);
    }
  }

  private void visitMdStruct(RelationshipDAOIF permission, ActorDAOIF actorIF, MdStructDAOIF mdStruct)
  {
    HashMap<String, String> attributes = new HashMap<String, String>();
    attributes.put(XMLTags.TYPE_ATTRIBUTE, mdStruct.definesType());

    // Open MdBusiness Permission tag
    writer.openEscapedTag(XMLTags.MD_STRUCT_PERMISSION_TAG, attributes);

    visitMdClass(permission, actorIF, mdStruct, false);

    writer.closeTag();
  }

  private void visitMdUtil(RelationshipDAOIF permission, ActorDAOIF actorIF, MdUtilDAOIF mdUtil)
  {
    HashMap<String, String> attributes = new HashMap<String, String>();
    attributes.put(XMLTags.TYPE_ATTRIBUTE, mdUtil.definesType());

    // Open MdBusiness Permission tag
    writer.openEscapedTag(XMLTags.MD_UTIL_PERMISSION_TAG, attributes);

    visitMdClass(permission, actorIF, mdUtil, false);

    writer.closeTag();
  }

  private void visitMdView(RelationshipDAOIF permission, ActorDAOIF actorIF, MdViewDAOIF mdView)
  {
    HashMap<String, String> attributes = new HashMap<String, String>();
    attributes.put(XMLTags.TYPE_ATTRIBUTE, mdView.definesType());

    // Open MdBusiness Permission tag
    writer.openEscapedTag(XMLTags.MD_VIEW_PERMISSION_TAG, attributes);

    visitMdClass(permission, actorIF, mdView, false);

    writer.closeTag();
  }

  private void visitMdFacade(RelationshipDAOIF permission, ActorDAOIF actorIF, MdFacadeDAOIF mdFacade)
  {
    HashMap<String, String> attributes = new HashMap<String, String>();
    attributes.put(XMLTags.TYPE_ATTRIBUTE, mdFacade.definesType());

    // Open MdBusiness Permission tag
    writer.openEscapedTag(XMLTags.MD_FACADE_PERMISSION_TAG, attributes);

    // Export the MdMethod permissions of the MdClass
    for (MdMethodDAOIF mdMethod : mdFacade.getMdMethods())
    {
      visitMdMethod(mdMethod, actorIF);
    }

    writer.closeTag();
  }

  private void visitMdBusiness(RelationshipDAOIF permission, ActorDAOIF actorIF, MdBusinessDAOIF mdBusiness, Boolean all)
  {
    HashMap<String, String> attributes = new HashMap<String, String>();
    attributes.put(XMLTags.TYPE_ATTRIBUTE, mdBusiness.definesType());

    // Open MdBusiness Permission tag
    writer.openEscapedTag(XMLTags.MD_BUSINESS_PERMISSION_TAG, attributes);

    visitMdClass(permission, actorIF, mdBusiness, all);

    // If the MdEntity is a MdBusiness and it has a MdStateMachine then
    // export the state permissions and the attribute permissions of that state

    // Get the MdStateStateMachine if it exists
    MdStateMachineDAOIF mdStateMachine = null;

    if (mdBusiness.hasStateMachine())
    {
      mdStateMachine = mdBusiness.definesMdStateMachine();
    }

    // Export the permissions for all of the states of the MdStateMachine
    if (mdStateMachine != null)
    {
      visitMdStateMachine(actorIF, mdStateMachine, mdBusiness);
    }

    // Close MdBusiness Permission tag
    writer.closeTag();
  }

  private void visitMdStateMachine(ActorDAOIF actorIF, MdStateMachineDAOIF mdStateMachine, MdBusinessDAOIF mdBusiness)
  {
    for (StateMasterDAOIF state : mdStateMachine.definesStateMasters())
    {
      HashMap<String, String> attributes = new HashMap<String, String>();
      attributes.put(XMLTags.STATE_NAME_ATTRIBUTE, state.getName());

      // Open StatePermission tag
      writer.openEscapedTag(XMLTags.STATE_PERMISSION_TAG, attributes);

      // Export all of the permissions defined for the StateMasterIF
      visitOperations(actorIF.getAllPermissions(state));

      // Export all of the attribute permissions for a given state
      for (MdAttributeConcreteDAOIF mdAttribute : mdBusiness.definesAttributes())
      {
        TypeTupleDAOIF tuple = TypeTupleDAO.findTuple(mdAttribute.getId(), state.getId());

        if (tuple != null)
        {
          HashMap<String, String> attributeAttributes = new HashMap<String, String>();
          attributeAttributes.put(XMLTags.PERMISSION_ATTRIBUTE_NAME, mdAttribute.definesAttribute());

          // Open AttributePermission tag
          writer.openEscapedTag(XMLTags.ATTRIBUTE_PERMISSION_TAG, attributeAttributes);

          visitOperations(actorIF.getAllPermissions(tuple));

          // Close the AttributePermission tag
          writer.closeTag();
        }
      }

      // Close the StatePermission tag
      writer.closeTag();
    }
  }

  private void visitMdAttribute(MdAttributeDAOIF mdAttribute, ActorDAOIF actorIF)
  {
    HashMap<String, String> attributes = new HashMap<String, String>();
    Set<Operation> operations = actorIF.getAllPermissions(mdAttribute);

    // Open Attriubte Permission tag
    if (operations.size() > 0)
    {
      attributes.put(XMLTags.PERMISSION_ATTRIBUTE_NAME, mdAttribute.definesAttribute());
      writer.openEscapedTag(XMLTags.ATTRIBUTE_PERMISSION_TAG, attributes);

      // Export operations
      visitOperations(operations);

      // Close the AttributePermission tag
      writer.closeTag();
    }

    this.visitMdAttributeDimensions(mdAttribute, actorIF);
  }

  private void visitMdAttributeDimensions(MdAttributeDAOIF mdAttribute, ActorDAOIF actorIF)
  {
    List<MdAttributeDimensionDAOIF> list = mdAttribute.getMdAttributeDimensions();

    for (MdAttributeDimensionDAOIF mdAttributeDimension : list)
    {
      HashMap<String, String> attributes = new HashMap<String, String>();
      Set<Operation> operations = actorIF.getAllPermissions(mdAttributeDimension);

      // Open Attribute Permission tag
      if (operations.size() > 0)
      {
        MdDimensionDAOIF mdDimension = mdAttributeDimension.definingMdDimension();

        attributes.put(XMLTags.PERMISSION_ATTRIBUTE_NAME, mdAttribute.definesAttribute());
        attributes.put(XMLTags.DIMENSION_ATTRIBUTE, mdDimension.getName());

        writer.openEscapedTag(XMLTags.ATTRIBUTE_PERMISSION_TAG, attributes);

        // Export operations
        visitOperations(operations);

        // Close the AttributePermission tag
        writer.closeTag();
      }
    }
  }

  private void visitMdMethod(MdMethodDAOIF mdMethod, ActorDAOIF actorIF)
  {
    HashMap<String, String> attributes = new HashMap<String, String>();
    Set<Operation> operations = actorIF.getAllPermissions(mdMethod);

    // Open MdMethod Permission tag
    if (operations.size() > 0)
    {
      attributes.put(XMLTags.METHOD_PERMISSION_NAME_ATTRIBUTE, mdMethod.getName());
      writer.openEscapedTag(XMLTags.MD_METHOD_PERMISSION_TAG, attributes);

      // Export operations
      visitOperations(operations);

      // Close the MdMethodPermission tag
      writer.closeTag();
    }
  }

  private void visitOperations(Set<Operation> operations)
  {
    for (Operation operation : operations)
    {
      HashMap<String, String> attributes = new HashMap<String, String>();
      attributes.put(XMLTags.NAME_ATTRIBUTE, operation.name());

      writer.writeEmptyEscapedTag(XMLTags.OPERATION_TAG, attributes);
    }
  }

}
