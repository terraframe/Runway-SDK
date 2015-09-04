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

import com.runwaysdk.business.state.MdStateMachineDAO;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdElementInfo;
import com.runwaysdk.constants.MdStateMachineInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MetadataInfo;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;

/**
 * @author Justin
 *
 */
public class MdStateMachineHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  private static class StatesHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public StatesHandler(ImportManager manager)
    {
      super(manager);

      this.addHandler(XMLTags.STATE_TAG, new StateMasterHandler(manager));
    }
  }

  private static class TransitionsHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public TransitionsHandler(ImportManager manager)
    {
      super(manager);

      this.addHandler(XMLTags.TRANSITION_TAG, new TransitionHandler(manager));
    }
  }

  public MdStateMachineHandler(ImportManager manager)
  {
    super(manager);

    this.addHandler(XMLTags.CREATE_TAG, this);
    this.addHandler(XMLTags.STATES_TAG, new StatesHandler(manager));
    this.addHandler(XMLTags.TRANSITIONS_TAG, new TransitionsHandler(manager));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    if (localName.equals(XMLTags.CREATE_TAG))
    {
      this.getManager().enterCreateState();
    }
    else
    {
      MdBusinessDAO mdBusiness = (MdBusinessDAO) context.getObject(MdTypeInfo.CLASS);

      MdStateMachineDAO mdStateMachine = (MdStateMachineDAO) this.getManager().getEntityDAO(MdStateMachineInfo.CLASS, attributes.getValue(XMLTags.NAME_ATTRIBUTE)).getEntityDAO();

      String type = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
      String name = BusinessDAOFactory.getClassNameFromType(type);
      String pack = BusinessDAOFactory.getPackageFromType(type);

      mdStateMachine.setValue(MdStateMachineInfo.NAME, name);
      mdStateMachine.setValue(MdStateMachineInfo.PACKAGE, pack);
      mdStateMachine.setValue(MdStateMachineInfo.STATE_MACHINE_OWNER, mdBusiness.getId());
      mdStateMachine.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, MdStateMachineInfo.STATE_MASTER);

      ImportManager.setLocalizedValue(mdStateMachine, MetadataInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);

      ImportManager.setLocalizedValue(mdStateMachine, MdElementInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);

      mdStateMachine.apply();

      context.setObject(MdStateMachineInfo.CLASS, mdStateMachine);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onEndElement(java.lang.String, java.lang.String, java.lang.String, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onEndElement(String uri, String localName, String name, TagContext context)
  {
    if (localName.equals(XMLTags.CREATE_TAG))
    {
      this.getManager().leavingCurrentState();
    }
  }
}
