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

import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.state.MdStateMachineDAOIF;
import com.runwaysdk.business.state.StateMasterDAOIF;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EnumerationItemDAOIF;
import com.runwaysdk.dataaccess.MdActionDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdControllerDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdExceptionDAOIF;
import com.runwaysdk.dataaccess.MdInformationDAOIF;
import com.runwaysdk.dataaccess.MdLocalStructDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdParameterDAOIF;
import com.runwaysdk.dataaccess.MdProblemDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.MdUtilDAOIF;
import com.runwaysdk.dataaccess.MdViewDAOIF;
import com.runwaysdk.dataaccess.MdWebFormDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.TransitionDAOIF;
import com.runwaysdk.dataaccess.io.MarkupWriter;
import com.runwaysdk.dataaccess.io.dataDefinition.ExportMetadata.NewParameterMarker;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdLocalStructDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFieldDAO;
import com.runwaysdk.dataaccess.metadata.ParameterMarker;

public class UpdateVisitor extends ExportVisitor
{
  private ExportMetadata metadata;

  public UpdateVisitor(MarkupWriter writer, ExportMetadata metadata)
  {
    super(writer, metadata);

    this.metadata = metadata;
  }
  
  public UpdateVisitor(MarkupWriter writer, boolean exportSource) {
    super(writer, new ExportMetadata(exportSource));
  }

  public void visit(ComponentIF component)
  {
    if (component instanceof MdParameterDAOIF || component instanceof MdStateMachineDAOIF || component instanceof StateMasterDAOIF)
    {
      // Do nothing, filter out. These are exported as part of MdBusiness,
      // MdRelationship, etc.

      return;
    }

    if (component instanceof MdBusinessDAOIF)
    {
      MdBusinessDAOIF mdBusinessIF = (MdBusinessDAOIF) component;

      // Determine if the mdBusinessIF extends ENUMERATION_MASTER
      List<MdBusinessDAOIF> superMdBusinessIFList = mdBusinessIF.getSuperClasses();
      MdBusinessDAOIF enumeration = MdBusinessDAO.getMdBusinessDAO(EnumerationMasterInfo.CLASS);

      if (superMdBusinessIFList.contains(enumeration))
      {
        visitMdBusinessEnum(mdBusinessIF);
      }
      else
      {
        visitMdBusiness(mdBusinessIF);
      }
    }
    else if (component instanceof MdLocalStructDAO)
    {
      visitMdLocalStruct((MdLocalStructDAOIF) component);
    }
    else if (component instanceof MdStructDAO)
    {
      visitMdStruct((MdStructDAOIF) component);
    }
    else if (component instanceof MdRelationshipDAO)
    {
      MdRelationshipDAOIF mdRelationship = (MdRelationshipDAOIF) component;
      visitMdRelationship(mdRelationship);
    }
    // Export MdEnumerations
    else if (component instanceof MdEnumerationDAOIF)
    {
      MdEnumerationDAOIF enumeration = (MdEnumerationDAOIF) component;
      visitMdEnumeration(enumeration);
    }
    else if (component instanceof MdControllerDAOIF)
    {
      visitMdController((MdControllerDAOIF) component);
    }
    else if (component instanceof MdExceptionDAOIF)
    {
      visitMdException((MdExceptionDAOIF) component);
    }
    else if (component instanceof MdProblemDAOIF)
    {
      visitMdProblem((MdProblemDAOIF) component);
    }
    else if (component instanceof MdInformationDAOIF)
    {
      visitMdInformation((MdInformationDAOIF) component);
    }
    else if (component instanceof MdViewDAOIF)
    {
      visitMdView((MdViewDAOIF) component);
    }
    else if (component instanceof MdUtilDAOIF)
    {
      visitMdUtil((MdUtilDAOIF) component);
    }
    else if (component instanceof MdWebFormDAOIF)
    {
      visitMdWebForm((MdWebFormDAOIF) component);
    }
    else if (component instanceof MdActionDAOIF)
    {
      visitMdAction((MdActionDAOIF) component, new LinkedList<MdParameterDAOIF>());
    }
    else if (component instanceof RelationshipDAOIF)
    {
      visitRelationship((RelationshipDAOIF) component);
    }
    else
    {
      visitObject((BusinessDAOIF) component);
    }
  }

  @Override
  protected void exitMdMethod(MdMethodDAOIF methodIF)
  {
    if (metadata.hasNewComponents(methodIF))
    {
      // Write the attributes of the entity
      writer.openTag(XMLTags.CREATE_TAG);
      for (MdParameterDAOIF mdParameter : metadata.getNewMdParameters(methodIF))
      {
        visitMdParameter(mdParameter);
      }
      writer.closeTag();
    }

    super.exitMdMethod(methodIF);
  }

  @Override
  protected void exitMdAction(MdActionDAOIF mdAction)
  {
    if (metadata.hasNewComponents(mdAction))
    {
      // Write the attributes of the entity
      writer.openTag(XMLTags.CREATE_TAG);
      for (MdParameterDAOIF mdParameter : metadata.getNewMdParameters(mdAction))
      {
        visitMdParameter(mdParameter);
      }
      writer.closeTag();
    }

    super.exitMdAction(mdAction);
  }

  @Override
  protected void exitMdController(MdControllerDAOIF mdController)
  {
    if (metadata.hasNewComponents(mdController))
    {
      // Write the attributes of the entity
      writer.openTag(XMLTags.CREATE_TAG);
      exportNewTypeComponents(mdController);
      writer.closeTag();
    }

    super.exitMdController(mdController);
  }

  @Override
  protected void exitMdStateMachine(MdStateMachineDAOIF mdStateMachine)
  {
    if (metadata.hasNewComponents(mdStateMachine))
    {
      // Write the attributes of the entity
      writer.openTag(XMLTags.CREATE_TAG);

      writer.openTag(XMLTags.STATES_TAG);
      List<StateMasterDAOIF> newStates = metadata.getNewStates(mdStateMachine);
      for (StateMasterDAOIF stateMaster : newStates)
      {
        visitStateMaster(stateMaster);
      }
      writer.closeTag();

      // Write the Transitions defined by the mdStateMachine
      writer.openTag(XMLTags.TRANSITIONS_TAG);
      for (TransitionDAOIF transition : metadata.getNewTransitions(mdStateMachine))
      {
        StateMasterDAOIF source = this.getStateMaster(transition.getParentId(), newStates);
        StateMasterDAOIF sink = this.getStateMaster(transition.getChildId(), newStates);

        visitTransition(transition, source, sink);
      }
      writer.closeTag();

      writer.closeTag();
    }

    super.exitMdStateMachine(mdStateMachine);
  }

  @Override
  protected void exitMdBusiness(MdBusinessDAOIF mdBusinessIF)
  {
    if (metadata.hasNewComponents(mdBusinessIF))
    {
      // Write the attributes of the entity
      writer.openTag(XMLTags.CREATE_TAG);

      exportNewBusinessComponents(mdBusinessIF);
      writer.closeTag();
    }

    super.exitMdBusiness(mdBusinessIF);
  }

  @Override
  protected void exitMdStruct(MdStructDAOIF mdStruct)
  {
    if (metadata.hasNewComponents(mdStruct))
    {
      writer.openTag(XMLTags.CREATE_TAG);
      exportNewEntityComponents(mdStruct);
      writer.closeTag();
    }

    super.exitMdStruct(mdStruct);
  }

  @Override
  protected void exitMdLocalStruct(MdLocalStructDAOIF mdLocalStructIF)
  {
    if (metadata.hasNewComponents(mdLocalStructIF))
    {
      writer.openTag(XMLTags.CREATE_TAG);
      exportNewEntityComponents(mdLocalStructIF);
      writer.closeTag();
    }

    super.exitMdLocalStruct(mdLocalStructIF);
  }

  @Override
  protected void exitMdBusinessEnum(MdBusinessDAOIF mdBusinessIF)
  {
    if (metadata.hasNewComponents(mdBusinessIF))
    {
      // Write the attributes of the entity
      writer.openTag(XMLTags.CREATE_TAG);
      exportNewEntityComponents(mdBusinessIF);
      writer.closeTag();
    }

    super.exitMdBusinessEnum(mdBusinessIF);
  }

  @Override
  protected void exitMdRelationship(MdRelationshipDAOIF mdRelationship)
  {
    if (metadata.hasNewComponents(mdRelationship))
    {
      // Write the attributes of the entity
      writer.openTag(XMLTags.CREATE_TAG);
      exportNewEntityComponents(mdRelationship);
      writer.closeTag();
    }

    super.exitMdRelationship(mdRelationship);
  }

  @Override
  protected void exitMdException(MdExceptionDAOIF mdException)
  {
    if (metadata.hasNewComponents(mdException))
    {
      // Write the attributes of the entity
      writer.openTag(XMLTags.CREATE_TAG);

      List<MdAttributeDAO> mdAttributes = metadata.getNewMdAttributes(mdException);

      if (mdAttributes.size() > 0)
      {
        visitMdAttributes(mdAttributes);
      }

      writer.closeTag();
    }

    super.exitMdException(mdException);
  }

  @Override
  protected void exitMdProblem(MdProblemDAOIF mdProblem)
  {
    if (metadata.hasNewComponents(mdProblem))
    {
      // Write the attributes of the entity
      writer.openTag(XMLTags.CREATE_TAG);

      List<MdAttributeDAO> mdAttributes = metadata.getNewMdAttributes(mdProblem);

      if (mdAttributes.size() > 0)
      {
        visitMdAttributes(mdAttributes);
      }

      writer.closeTag();
    }

    super.exitMdProblem(mdProblem);
  }

  @Override
  protected void exitMdInformation(MdInformationDAOIF mdInformation)
  {
    if (metadata.hasNewComponents(mdInformation))
    {
      // Write the attributes of the entity
      writer.openTag(XMLTags.CREATE_TAG);

      List<MdAttributeDAO> mdAttributes = metadata.getNewMdAttributes(mdInformation);

      if (mdAttributes.size() > 0)
      {
        visitMdAttributes(mdAttributes);
      }

      writer.closeTag();
    }

    super.exitMdInformation(mdInformation);
  }

  @Override
  protected void exitMdView(MdViewDAOIF mdView)
  {
    if (metadata.hasNewComponents(mdView))
    {
      // Write the attributes of the entity
      writer.openTag(XMLTags.CREATE_TAG);

      List<MdAttributeDAO> mdAttributes = metadata.getNewMdAttributes(mdView);

      if (mdAttributes.size() > 0)
      {
        visitMdAttributes(mdAttributes);
      }

      writer.closeTag();
    }

    super.exitMdView(mdView);
  }

  @Override
  protected void exitMdUtil(MdUtilDAOIF mdUtil)
  {
    if (metadata.hasNewComponents(mdUtil))
    {
      // Write the attributes of the entity
      writer.openTag(XMLTags.CREATE_TAG);

      List<MdAttributeDAO> mdAttributes = metadata.getNewMdAttributes(mdUtil);

      if (mdAttributes.size() > 0)
      {
        visitMdAttributes(mdAttributes);
      }

      writer.closeTag();
    }

    super.exitMdUtil(mdUtil);
  }

  @Override
  protected void exitMdEnumeration(MdEnumerationDAOIF mdEnumeration)
  {
    if (!mdEnumeration.includeAllEnumerationItems())
    {
      List<BusinessDAOIF> items = metadata.getRemoveEnumItems(mdEnumeration);

      for (BusinessDAOIF item : items)
      {
        HashMap<String, String> param = new HashMap<String, String>();

        param.put(XMLTags.ENUM_NAME_ATTRIBUTE, ( (EnumerationItemDAOIF) item ).getName());

        writer.writeEmptyEscapedTag(XMLTags.REMOVE_ENUM_ITEM_TAG, param);
      }
    }

    super.exitMdEnumeration(mdEnumeration);
  }

  @Override
  public void visitRelationship(RelationshipDAOIF relationship)
  {
    HashMap<String, String> parameters = new HashMap<String, String>();
    // The name of the class being instaniated
    parameters.put(XMLTags.TYPE_ATTRIBUTE, relationship.getType());
    parameters.put(XMLTags.KEY_ATTRIBUTE, relationship.getKey());

    if (metadata.isRekeyed(relationship))
    {
      parameters.put(XMLTags.NEW_KEY_ATTRIBUTE, metadata.getRekey(relationship));
    }

    writer.openEscapedTag(XMLTags.RELATIONSHIP_TAG, parameters);

    // Write all of the values of the instance
    visitValues(relationship.getAttributeArrayIF());

    writer.closeTag();
  }

  @Override
  public void visitObject(BusinessDAOIF businessDAO)
  {
    HashMap<String, String> parameters = new HashMap<String, String>();
    // The type of the instance
    parameters.put(XMLTags.TYPE_ATTRIBUTE, businessDAO.getType());
    parameters.put(XMLTags.KEY_ATTRIBUTE, businessDAO.getKey());

    if (metadata.isRekeyed(businessDAO))
    {
      parameters.put(XMLTags.NEW_KEY_ATTRIBUTE, metadata.getRekey(businessDAO));
    }

    writer.openEscapedTag(XMLTags.OBJECT_TAG, parameters);

    // Write all of the values of the instance
    visitValues(businessDAO.getAttributeArrayIF());

    writer.closeTag();
  }

  private void exportNewTypeComponents(MdTypeDAOIF mdType)
  {
    // Write the mdMethod defined by the entity
    List<NewParameterMarker> newMarkers = metadata.getNewParameterMarkers(mdType);

    for (NewParameterMarker newMarker : newMarkers)
    {
      ParameterMarker marker = newMarker.getMarker();

      if (marker instanceof MdMethodDAOIF)
      {
        visitMdMethod((MdMethodDAOIF) marker, newMarker.getMdParameters());
      }
      else
      {
        visitMdAction((MdActionDAOIF) marker, newMarker.getMdParameters());
      }
    }
  }

  private void exportNewEntityComponents(MdEntityDAOIF mdEntity)
  {
    List<MdAttributeDAO> mdAttributes = metadata.getNewMdAttributes(mdEntity);

    if (mdAttributes.size() > 0)
    {
      visitMdAttributes(mdAttributes);
    }

    exportNewTypeComponents(mdEntity);
  }

  private void exportNewBusinessComponents(MdBusinessDAOIF mdBusinessIF)
  {
    exportNewEntityComponents(mdBusinessIF);

    if (metadata.getNewMdStateMachine(mdBusinessIF) != null)
    {
      MdStateMachineDAOIF mdStateMachine = metadata.getNewMdStateMachine(mdBusinessIF);
      List<StateMasterDAOIF> states = metadata.getNewStates(mdBusinessIF);
      List<TransitionDAOIF> transitions = metadata.getNewTransitions(mdBusinessIF);
      visitMdStateMachine(mdStateMachine, states, transitions);
    }
  }

  @Override
  protected void exitMdWebForm(MdWebFormDAOIF mdWebForm)
  {
    if (metadata.hasNewComponents(mdWebForm))
    {
      // Write the attributes of the entity
      writer.openTag(XMLTags.CREATE_TAG);

      List<MdWebFieldDAO> fields = metadata.getNewMdWebFields(mdWebForm);

      if (fields.size() > 0)
      {
        visitMdWebFields(fields);
      }

      writer.closeTag();
    }

    super.exitMdWebForm(mdWebForm);
  }

}
