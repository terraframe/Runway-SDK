/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;

import com.runwaysdk.business.state.MdStateMachineDAOIF;
import com.runwaysdk.business.state.StateMasterDAOIF;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MetadataInfo;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.TypeTupleDAO;
import com.runwaysdk.dataaccess.metadata.TypeTupleDAOIF;

public class DirectionalStatePermissionHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{

  public DirectionalStatePermissionHandler(ImportManager manager)
  {
    super(manager);

    this.addHandler(XMLTags.OPERATION_TAG, new OperationHandler(manager));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.MdClassPermissionHandler#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    String stateName = attributes.getValue(XMLTags.STATE_NAME_ATTRIBUTE);
    MdRelationshipDAOIF mdRelationship = (MdRelationshipDAOIF) context.getObject(MdTypeInfo.CLASS);

    MdStateMachineDAOIF mdStateMachine = this.getStateMachine(localName, mdRelationship);

    StateMasterDAOIF stateMaster = mdStateMachine.definesStateMaster(stateName);

    TypeTupleDAOIF tuple = TypeTupleDAO.findTuple(mdRelationship.getId(), stateMaster.getId());

    if (tuple == null)
    {
      TypeTupleDAO newTuple = TypeTupleDAO.newInstance();
      newTuple.setValue(TypeTupleDAOIF.METADATA, mdRelationship.getId());
      newTuple.setValue(TypeTupleDAOIF.STATE_MASTER, stateMaster.getId());
      newTuple.setStructValue(TypeTupleDAOIF.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, mdRelationship.definesType() + "-" + stateMaster.getName() + " tuple");
      newTuple.apply();

      tuple = newTuple;
    }

    context.setObject(MetadataInfo.CLASS, tuple);
  }

  private MdStateMachineDAOIF getStateMachine(String localName, MdRelationshipDAOIF mdRelationship)
  {
    MdStateMachineDAOIF mdStateMachine = null;

    if (localName.equals(XMLTags.PARENT_STATE_PERMISSION_TAG))
    {
      mdStateMachine = mdRelationship.getParentMdBusiness().definesMdStateMachine();
    }
    else
    {
      mdStateMachine = mdRelationship.getChildMdBusiness().definesMdStateMachine();
    }
    return mdStateMachine;
  }
}
