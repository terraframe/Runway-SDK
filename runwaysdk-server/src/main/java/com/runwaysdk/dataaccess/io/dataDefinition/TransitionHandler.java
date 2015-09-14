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

import org.xml.sax.Attributes;

import com.runwaysdk.business.state.MdStateMachineDAO;
import com.runwaysdk.constants.MdStateMachineInfo;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.TransitionDAO;
import com.runwaysdk.dataaccess.TransitionDAOIF;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.io.ImportManager;

public class TransitionHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  public TransitionHandler(ImportManager manager)
  {
    super(manager);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    MdStateMachineDAO mdStateMachine = (MdStateMachineDAO) context.getObject(MdStateMachineInfo.CLASS);

    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    String source = attributes.getValue(XMLTags.SOURCE_ATTRIBUTE);
    String sink = attributes.getValue(XMLTags.SINK_ATTRIBUTE);

    String sourceId = mdStateMachine.definesStateMaster(source).getId();
    String sinkId = mdStateMachine.definesStateMaster(sink).getId();

    MdRelationshipDAOIF mdTransition = mdStateMachine.getMdTransition();
    TransitionDAO newTransition = null;

    if (this.getManager().isCreateState())
    {
      newTransition = TransitionDAO.newInstance(sourceId, sinkId, mdTransition.definesType());
      newTransition.setName(name);
    }
    else if (this.getManager().isCreateOrUpdateState())
    {
      try
      {
        String key = TransitionDAO.buildKey(mdStateMachine.definesType(), name);
        newTransition = (TransitionDAO) RelationshipDAO.get(mdTransition.definesType(), key);
      }
      catch (DataNotFoundException e)
      {
        newTransition = TransitionDAO.newInstance(sourceId, sinkId, mdTransition.definesType());
        newTransition.setName(name);
      }
    }
    else
    {
      String key = TransitionDAO.buildKey(mdStateMachine.definesType(), name);
      newTransition = (TransitionDAO) RelationshipDAO.get(mdTransition.definesType(), key);
    }

    ImportManager.setLocalizedValue(newTransition, TransitionDAOIF.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    newTransition.apply();
  }
}
