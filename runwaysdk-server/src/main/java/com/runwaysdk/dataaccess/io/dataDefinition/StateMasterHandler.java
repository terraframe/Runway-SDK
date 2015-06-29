/**
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.runwaysdk.business.state.MdStateMachineDAO;
import com.runwaysdk.business.state.StateMasterDAO;
import com.runwaysdk.business.state.StateMasterDAOIF;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;

public class StateMasterHandler extends XMLHandler
{
  /**
   *  The MdStateMachine on which the state master is defined
   */
  private MdStateMachineDAO        mdStateMachine;

  /**
   * Constructor - Creates a StateMasterDAO and sets the parameters according to the attributes parse
   *
   * @param attributes The attibutes of the class tag
   * @param reader The XMLReader stream
   * @param previousHandler The Handler which passed control
   * @param manager ImportManager which provides communication between handlers for a single import
   * @param mdStateMachine The MdStateMachine for which the StateMasterDAO is defined.
   */
  public StateMasterHandler(Attributes attributes, XMLReader reader, XMLHandler previousHandler, ImportManager manager, MdStateMachineDAO mdStateMachine)
  {
    super(reader, previousHandler, manager);

    this.mdStateMachine = mdStateMachine;

    importStateMaster(attributes);
  }

  /**
   * Creates a StateMasterDAO
   *
   * @param attributes The attributes of an class tag
   */
  private final void importStateMaster(Attributes attributes)
  {
    String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    String entry = attributes.getValue(XMLTags.ENTRY_ATTRIBUTE);
    String entryId = StateMasterDAOIF.Entry.NOT_ENTRY_STATE.getId();

    if(entry.equals(XMLTags.DEFAULT_ENTRY_ENUM))
    {
      entryId = StateMasterDAOIF.Entry.DEFAULT_ENTRY_STATE.getId();
    }
    else if(entry.equals(XMLTags.ENTRY_ENUM))
    {
      entryId = StateMasterDAOIF.Entry.ENTRY_STATE.getId();
    }

    String key = StateMasterDAO.buildKey(mdStateMachine.definesType(), name);
    StateMasterDAO state = (StateMasterDAO) manager.getEntityDAO(mdStateMachine.definesType(), key).getEntityDAO();
    state.setValue(StateMasterDAOIF.ENTRY_STATE, entryId);
    state.setValue(StateMasterDAOIF.STATE_NAME, name);
    state.apply();
  }

  /* (non-Javadoc)
   * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
   */
  public void startElement(String namespaceURI, String localName, String fullName, Attributes attributes) throws SAXException
  {
  }

  /* (non-Javadoc)
   * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
   */
  public void endElement(String namespaceURI, String localName, String fullName)
  {
    if (localName.equals(XMLTags.STATE_TAG))
    {
      reader.setContentHandler(previousHandler);
      reader.setErrorHandler(previousHandler);
    }
  }
}
