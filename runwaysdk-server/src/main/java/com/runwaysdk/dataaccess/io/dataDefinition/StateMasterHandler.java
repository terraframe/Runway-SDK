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
import com.runwaysdk.business.state.StateMasterDAO;
import com.runwaysdk.business.state.StateMasterDAOIF;
import com.runwaysdk.constants.MdStateMachineInfo;
import com.runwaysdk.dataaccess.io.ImportManager;

public class StateMasterHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  public StateMasterHandler(ImportManager manager)
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
    String entry = attributes.getValue(XMLTags.ENTRY_ATTRIBUTE);
    String entryId = StateMasterDAOIF.Entry.NOT_ENTRY_STATE.getId();

    if (entry.equals(XMLTags.DEFAULT_ENTRY_ENUM))
    {
      entryId = StateMasterDAOIF.Entry.DEFAULT_ENTRY_STATE.getId();
    }
    else if (entry.equals(XMLTags.ENTRY_ENUM))
    {
      entryId = StateMasterDAOIF.Entry.ENTRY_STATE.getId();
    }

    String key = StateMasterDAO.buildKey(mdStateMachine.definesType(), name);

    StateMasterDAO state = (StateMasterDAO) this.getManager().getEntityDAO(mdStateMachine.definesType(), key).getEntityDAO();
    state.setValue(StateMasterDAOIF.ENTRY_STATE, entryId);
    state.setValue(StateMasterDAOIF.STATE_NAME, name);
    state.apply();
  }
}
